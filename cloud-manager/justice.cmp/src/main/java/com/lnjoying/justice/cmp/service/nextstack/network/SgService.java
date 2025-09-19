package com.lnjoying.justice.cmp.service.nextstack.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cmp.common.CreateResourceMsg;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.model.search.SecurityGroupSearchCritical;
import com.lnjoying.justice.cmp.db.repo.NetworkRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.SecurityGroupCreateReqVo;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.SgRulesCreateUpdateReqVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstanceDetailInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.GetSecurityGroupDetailInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcSecurityGroupRule;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.SecurityGroupRspVo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.SecurityGroupsRspVo;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.rpc.RpcVmServiceImpl;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.*;

@Service
public class SgService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SgService.class);

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private RpcVmServiceImpl vmComputeService;

    @Autowired
    private UserService userService;

    public SecurityGroupsRspVo getSecurityGroups(String cloudId, SecurityGroupSearchCritical securityGroupSearchCritical, String bpId, String userId) throws WebSystemException
    {
        try
        {
            String sgId = securityGroupSearchCritical.getSgId();
            String name = securityGroupSearchCritical.getName();

            if (sgId == null && name == null && userId != null)
            {
                checkUserDefaultSecurityGroup(cloudId, bpId, userId);
            }

            long totalNum = networkRepository.countSecurityGroupBySearch(cloudId, userId, name, sgId, REMOVED, false);
            SecurityGroupsRspVo getSecurityGroupsRsp = new SecurityGroupsRspVo();
            getSecurityGroupsRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getSecurityGroupsRsp;
            }

            int pageSize = securityGroupSearchCritical.getPageSize();
            int pageNum = securityGroupSearchCritical.getPageNum();
            int begin = ((pageNum - 1) * pageSize);


            List<SecurityGroupRspVo> securityGroups = networkRepository.getSecurityGroups(cloudId, userId, name, sgId, REMOVED, false, pageSize, begin);

            securityGroups.forEach(securityGroupRspVo -> {
                securityGroupRspVo.setEeBpName(userService.getBpName(securityGroupRspVo.getEeBp()));
                securityGroupRspVo.setEeUserName(userService.getUserName(securityGroupRspVo.getEeUser()));
            });

            securityGroups.forEach(securityGroupRspVo -> cloudService.syncSingleData(cloudId, securityGroupRspVo.getSgId(), SyncMsg.NS_SECURITY_GROUP));

            getSecurityGroupsRsp.setSecurityGroups(securityGroups);

            return getSecurityGroupsRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get security groups error: {}",e.getMessage());
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.ERROR);
        }
    }

    public String checkUserDefaultSecurityGroup(String cloudId, String bpId, String userId)
    {
        try
        {
            TblCmpSecurityGroupExample example = new TblCmpSecurityGroupExample();
            TblCmpSecurityGroupExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeUserEqualTo(userId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andIsDefaultEqualTo(true);

            List<TblCmpSecurityGroup> tblCmpSecurityGroups = networkRepository.getSecurityGroups(example);

            if (! CollectionUtils.isEmpty(tblCmpSecurityGroups))
            {
                return tblCmpSecurityGroups.get(0).getSgId();
            }

            String sgId = null;
            String defaultSecurityGroupName = "sg-" + RandomStringUtils.randomAlphanumeric(8);
            JsonObject defaultSgObject = JsonParser.parseString("{\"name\":\"" + defaultSecurityGroupName + "\",\"description\":\"Default security group\"}").getAsJsonObject();

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, "/api/network/v1/sgs", HttpMethod.POST, defaultSgObject, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add security group error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    sgId = (String) resultMap.get("sgId");
                    if (StringUtils.isEmpty(sgId))
                    {
                        LOGGER.error("add security group error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpSecurityGroup tblSecurityGroup = new TblCmpSecurityGroup();
                        tblSecurityGroup.setSgId(sgId);
                        tblSecurityGroup.setName(defaultSecurityGroupName);
                        tblSecurityGroup.setCloudId(cloudId);
                        tblSecurityGroup.setEeBp(bpId);
                        tblSecurityGroup.setEeUser(userId);
                        tblSecurityGroup.setEeStatus(SYNCING);
                        tblSecurityGroup.setPhaseStatus(SYNCING);
                        tblSecurityGroup.setIsDefault(true);
                        try
                        {
                            networkRepository.createSecurityGroup(tblSecurityGroup);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpSecurityGroup updateTblCmpSecurityGroup = networkRepository.getSecurityGroupById(cloudId, sgId);
                            updateTblCmpSecurityGroup.setEeBp(bpId);
                            updateTblCmpSecurityGroup.setEeUser(userId);
                            updateTblCmpSecurityGroup.setEeStatus(SYNCING);
                            tblSecurityGroup.setIsDefault(true);
                            networkRepository.updateSecurityGroup(updateTblCmpSecurityGroup);
                        }

                        cloudService.syncSingleData(cloudId, sgId, SyncMsg.NS_SECURITY_GROUP);
                    }

                    JsonObject defaultSgRulesObject = JsonParser.parseString("{\"createSgRules\":[{\"description\":\"Linux SSH登录\",\"priority\":1,\"direction\":0,\"protocol\":0,\"port\":\"22\",\"addressType\":0,\"addressRef\":{\"cidr\":\"0.0.0.0/0\",\"sgId\":\"\"},\"action\":1}," +
                            "{\"description\":\"Windows远程登录\",\"priority\":1,\"direction\":0,\"protocol\":0,\"port\":\"3389\",\"addressType\":0,\"addressRef\":{\"cidr\":\"0.0.0.0/0\",\"sgId\":\"\"},\"action\":1}," +
                            "{\"description\":\"ICMP\",\"priority\":1,\"direction\":0,\"protocol\":4,\"port\":\"0\",\"addressType\":0,\"addressRef\":{\"cidr\":\"0.0.0.0/0\",\"sgId\":\"\"},\"action\":1}," +
                            "{\"description\":\"\",\"priority\":1,\"direction\":1,\"protocol\":3,\"port\":\"\",\"addressType\":0,\"addressRef\":{\"cidr\":\"0.0.0.0/0\",\"sgId\":\"\"},\"action\":1}," +
                            "{\"description\":\"\",\"priority\":100,\"direction\":0,\"protocol\":3,\"port\":\"\",\"addressType\":0,\"addressRef\":{\"cidr\":\"\",\"sgId\":\"" + sgId + "\"},\"action\":1}]}").getAsJsonObject();
                    ResponseEntity addRulesResponse = cloudService.sendHttpRequestToCloud(cloudId, "/api/network/v1/sgs/" + sgId + "/rules", HttpMethod.POST, defaultSgRulesObject, headers);
                    if (addRulesResponse.getStatusCode() == HttpStatus.CREATED || addRulesResponse.getStatusCode() == HttpStatus.OK)
                    {
                        List<String> resultList = (List) addRulesResponse.getBody();
                        if (null == resultList)
                        {
                            LOGGER.error("add security group rule error");
                            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                        } else
                        {
                            for (String ruleId : resultList)
                            {
                                if (StringUtils.isEmpty(ruleId))
                                {
                                    LOGGER.error("add security group rule error");
                                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                                } else
                                {
                                    TblCmpSecurityGroupRule tblCmpSecurityGroupRule = new TblCmpSecurityGroupRule();
                                    tblCmpSecurityGroupRule.setRuleId(ruleId);
                                    tblCmpSecurityGroupRule.setCloudId(cloudId);
                                    tblCmpSecurityGroupRule.setEeBp(bpId);
                                    tblCmpSecurityGroupRule.setEeUser(userId);
                                    tblCmpSecurityGroupRule.setEeStatus(SYNCING);
                                    try
                                    {
                                        networkRepository.createSecurityGroupRule(tblCmpSecurityGroupRule);
                                    } catch (DuplicateKeyException e)
                                    {
                                        TblCmpSecurityGroupRule updateTblCmpSecurityGroupRule = networkRepository.getSecurityGroupRuleById(cloudId, ruleId);
                                        updateTblCmpSecurityGroupRule.setEeBp(bpId);
                                        updateTblCmpSecurityGroupRule.setEeUser(userId);
                                        updateTblCmpSecurityGroupRule.setEeStatus(SYNCING);
                                        networkRepository.updateSecurityGroupRuleSelective(updateTblCmpSecurityGroupRule);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return sgId;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.info("check default security group error, userId: {}", userId);
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public GetSecurityGroupDetailInfoRsp getSecurityGroup(String cloudId, String sgId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpSecurityGroup tblCmpSecurityGroup = networkRepository.getSecurityGroupById(cloudId, sgId);
            if (null == tblCmpSecurityGroup || REMOVED == tblCmpSecurityGroup.getPhaseStatus())
            {
                LOGGER.info("security group  not exists, sgId: {}", sgId);
                throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (null == userId)
            {
                LOGGER.info("user admin, get securityGroupId: {}",tblCmpSecurityGroup.getSgId());
            }
            else if (checkUserIdSgIdInValid(cloudId, userId, sgId))
            {
                LOGGER.info("check userId and sgId error, userId:{}, sgId:{}", userId, sgId);
                throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
            }
            GetSecurityGroupDetailInfoRsp getSecurityGroupDetailInfoRsp = new GetSecurityGroupDetailInfoRsp();
            getSecurityGroupDetailInfoRsp.setName(tblCmpSecurityGroup.getName());
            getSecurityGroupDetailInfoRsp.setDescription(tblCmpSecurityGroup.getDescription());
            getSecurityGroupDetailInfoRsp.setSgId(tblCmpSecurityGroup.getSgId());
            getSecurityGroupDetailInfoRsp.setEeBp(tblCmpSecurityGroup.getEeBp());
            getSecurityGroupDetailInfoRsp.setEeUser(tblCmpSecurityGroup.getEeUser());
            getSecurityGroupDetailInfoRsp.setEeBpName(userService.getBpName(tblCmpSecurityGroup.getEeBp()));
            getSecurityGroupDetailInfoRsp.setEeUserName(userService.getUserName(tblCmpSecurityGroup.getEeUser()));
            TblCmpSecurityGroupRuleExample example = new TblCmpSecurityGroupRuleExample();
            TblCmpSecurityGroupRuleExample.Criteria criteria = example.createCriteria();
            criteria.andSgIdEqualTo(sgId);
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            example.setOrderByClause("create_time desc,rule_id desc");
            List<TblCmpSecurityGroupRule> sgRules = networkRepository.getSecurityGroupRules(example);
            if (sgRules.size() > 0)
            {
                List<RpcSecurityGroupRule> securityGroupRules = sgRules.stream().map(tblSecurityGroupRule ->
                {
                    RpcSecurityGroupRule securityGroupRule = new RpcSecurityGroupRule();
                    securityGroupRule.setSecurityGroupRule(tblSecurityGroupRule);
                    return securityGroupRule;
                }).collect(Collectors.toList());
                getSecurityGroupDetailInfoRsp.setRules(securityGroupRules);
            }
            TblCmpSgVmInstanceExample tblSgVmInstanceExample = new TblCmpSgVmInstanceExample();
            TblCmpSgVmInstanceExample.Criteria sgVmInstanceExampleCriteria = tblSgVmInstanceExample.createCriteria();
            sgVmInstanceExampleCriteria.andSgIdEqualTo(sgId);
            sgVmInstanceExampleCriteria.andCloudIdEqualTo(cloudId);
            sgVmInstanceExampleCriteria.andEeStatusNotEqualTo(REMOVED);
            if (null != userId) sgVmInstanceExampleCriteria.andEeUserEqualTo(userId);
            List<TblCmpSgVmInstance> tblSgVmInstances = networkRepository.getSgVmInstances(tblSgVmInstanceExample);
            List<String> instanceIds = tblSgVmInstances.stream().map(TblCmpSgVmInstance::getInstanceId).collect(Collectors.toList());
            List<RpcInstanceDetailInfo> vmInstanceDetailInfos = vmComputeService.getVmInstanceDetailInfos(cloudId, instanceIds);
            List<GetSecurityGroupDetailInfoRsp.VmInstance> vmInstances = new ArrayList<>();
            for (RpcInstanceDetailInfo rpcInstanceDetailInfo : vmInstanceDetailInfos)
            {
                if (rpcInstanceDetailInfo != null)
                {
                    GetSecurityGroupDetailInfoRsp.VmInstance vmInstance = new GetSecurityGroupDetailInfoRsp.VmInstance();
                    vmInstance.setName(rpcInstanceDetailInfo.getName());
                    TblCmpPort tblCmpPort = networkRepository.getPortById(cloudId, rpcInstanceDetailInfo.getPortId());
                    vmInstance.setIp(tblCmpPort == null ? null : tblCmpPort.getIpAddress());
                    vmInstance.setInstanceId(rpcInstanceDetailInfo.getInstanceId());
                    vmInstance.setFlavorName(rpcInstanceDetailInfo.getFlavorName());
                    vmInstance.setPhaseStatus(rpcInstanceDetailInfo.getPhaseStatus());
                    vmInstances.add(vmInstance);
                }
            }
            getSecurityGroupDetailInfoRsp.setVmInstances(vmInstances);

            cloudService.syncSingleData(cloudId, sgId, SyncMsg.NS_SECURITY_GROUP);
            return getSecurityGroupDetailInfoRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get security group detail info error: {}, sgId:{}, userId:{}",e.getMessage(),sgId,userId);
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity createSecurityGroup(String cloudId, SecurityGroupCreateReqVo req, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpSecurityGroup tblSecurityGroup = new TblCmpSecurityGroup();
            tblSecurityGroup.setSgId(Utils.buildStr("EE_", Utils.assignUUId()));
            tblSecurityGroup.setEeId(tblSecurityGroup.getSgId());
            tblSecurityGroup.setCloudId(cloudId);
            tblSecurityGroup.setEeBp(bpId);
            tblSecurityGroup.setEeUser(userId);
            tblSecurityGroup.setEeStatus(WAIT_CREATE);

            tblSecurityGroup.setName(req.getName());
            tblSecurityGroup.setDescription(req.getDescription());
            tblSecurityGroup.setPhaseStatus(PhaseStatus.ADDING);

            networkRepository.createSecurityGroup(tblSecurityGroup);

            cloudService.createResource(cloudId, tblSecurityGroup.getEeId(), bpId, userId, CreateResourceMsg.NS_SECURITY_GROUP, req);

            return ResponseEntity.status(HttpStatus.CREATED).body(tblSecurityGroup.getEeId());
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }
    public void createSecurityGroup(CreateResourceInfo createResourceInfo)
    {
        try
        {
            SecurityGroupCreateReqVo req = (SecurityGroupCreateReqVo) createResourceInfo.getObject();
            String cloudId = createResourceInfo.getCloudId();
            String bpId = createResourceInfo.getBpId();
            String userId = createResourceInfo.getUserId();
            String eeId = createResourceInfo.getEeId();

            String url = "/api/network/v1/sgs";
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, url, HttpMethod.POST, req, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add security group error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String sgId = (String) resultMap.get("sgId");
                    if (StringUtils.isEmpty(sgId))
                    {
                        LOGGER.error("add security group error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpSecurityGroup tblSecurityGroup = new TblCmpSecurityGroup();
                        tblSecurityGroup.setSgId(sgId);
                        tblSecurityGroup.setCloudId(cloudId);
                        tblSecurityGroup.setEeStatus(SYNCING);
                        tblSecurityGroup.setEeId(eeId);

                        try
                        {
                            networkRepository.updateSecurityGroupByEeIdSelective(tblSecurityGroup);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpSecurityGroup oldTblCmpSecurityGroup = networkRepository.getSecurityGroupByEeId(cloudId, eeId);
                            TblCmpSecurityGroup updateTblCmpSecurityGroup = networkRepository.getSecurityGroupById(cloudId, sgId);

                            updateTblCmpSecurityGroup.setEeBp(bpId);
                            updateTblCmpSecurityGroup.setEeUser(userId);
                            updateTblCmpSecurityGroup.setEeStatus(SYNCING);
                            updateTblCmpSecurityGroup.setEeId(eeId);
                            networkRepository.updateSecurityGroup(updateTblCmpSecurityGroup);

                            oldTblCmpSecurityGroup.setEeStatus(REMOVED);
                            networkRepository.updateSecurityGroup(oldTblCmpSecurityGroup);
                        }

                        cloudService.syncSingleData(cloudId, sgId, SyncMsg.NS_SECURITY_GROUP);
                    }
                }
            }
            else
            {
                TblCmpSecurityGroup tblCmpSecurityGroup = networkRepository.getSecurityGroupByEeId(cloudId, eeId);
                tblCmpSecurityGroup.setPhaseStatus(PhaseStatus.ADD_FAILED);
                networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("add security group failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeSecurityGroup(String cloudId, String sgId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpSecurityGroup tblCmpSecurityGroup = networkRepository.getSecurityGroupById(cloudId, sgId);

            if (tblCmpSecurityGroup == null)
            {
                LOGGER.error("get security group null: device id {}", sgId);
                throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpSecurityGroup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            if (tblCmpSecurityGroup.getSgId().equals(tblCmpSecurityGroup.getEeId()) && tblCmpSecurityGroup.getPhaseStatus() == PhaseStatus.ADD_FAILED)
            {
                tblCmpSecurityGroup.setEeStatus(REMOVED);
                networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpSecurityGroup.setEeStatus(REMOVED);
                tblCmpSecurityGroup.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateSecurityGroup(tblCmpSecurityGroup);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity createSgRule(String cloudId, SgRulesCreateUpdateReqVo createSgRulesReq, String sgId, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                List<String> resultList = (List) response.getBody();
                if (null == resultList)
                {
                    LOGGER.error("add security group rule error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    for (String ruleId : resultList)
                    {
                        if (StringUtils.isEmpty(ruleId))
                        {
                            LOGGER.error("add security group rule error");
                            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                        }
                        else
                        {
                            TblCmpSecurityGroupRule tblCmpSecurityGroupRule = new TblCmpSecurityGroupRule();
                            tblCmpSecurityGroupRule.setRuleId(ruleId);
                            tblCmpSecurityGroupRule.setCloudId(cloudId);
                            tblCmpSecurityGroupRule.setEeBp(bpId);
                            tblCmpSecurityGroupRule.setEeUser(userId);
                            tblCmpSecurityGroupRule.setEeStatus(SYNCING);
                            try
                            {
                                networkRepository.createSecurityGroupRule(tblCmpSecurityGroupRule);
                            }
                            catch (DuplicateKeyException e)
                            {
                                TblCmpSecurityGroupRule updateTblCmpSecurityGroupRule = networkRepository.getSecurityGroupRuleById(cloudId, ruleId);
                                updateTblCmpSecurityGroupRule.setEeBp(bpId);
                                updateTblCmpSecurityGroupRule.setEeUser(userId);
                                updateTblCmpSecurityGroupRule.setEeStatus(SYNCING);
                                networkRepository.updateSecurityGroupRuleSelective(updateTblCmpSecurityGroupRule);
                            }
                        }
                    }
                }

                cloudService.syncSingleData(cloudId, sgId, SyncMsg.NS_SECURITY_GROUP);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add security group rule failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity removeSgRule(String cloudId, String sgRuleId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpSecurityGroupRule tblCmpSecurityGroupRule = networkRepository.getSecurityGroupRuleById(cloudId, sgRuleId);

            if (tblCmpSecurityGroupRule == null)
            {
                LOGGER.error("get security group rule null: sgRuleId {}", sgRuleId);
                throw new WebSystemException(ErrorCode.SECURITY_GROUP_RULE_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpSecurityGroupRule.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED || response.getStatusCode() == HttpStatus.NO_CONTENT)
            {
                tblCmpSecurityGroupRule.setEeStatus(REMOVED);
                tblCmpSecurityGroupRule.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateSecurityGroupRuleSelective(tblCmpSecurityGroupRule);
            }

            cloudService.syncSingleData(cloudId, tblCmpSecurityGroupRule.getSgId(), SyncMsg.NS_SECURITY_GROUP);
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove security group rule failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkSgUser(String cloudId, String sgId, String userId) throws WebSystemException
    {
        TblCmpSecurityGroup tblCmpSecurityGroup = networkRepository.getSecurityGroupById(cloudId, sgId);

        if (tblCmpSecurityGroup == null)
        {
            LOGGER.error("get security group null: device id {}", sgId);
            throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpSecurityGroup.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public void checkSgRuleUser(String cloudId, String sgRuleId, String userId) throws WebSystemException
    {
        TblCmpSecurityGroupRule tblCmpSecurityGroupRule = networkRepository.getSecurityGroupRuleById(cloudId, sgRuleId);

        if (tblCmpSecurityGroupRule == null)
        {
            LOGGER.error("get security group rule null: device id {}", sgRuleId);
            throw new WebSystemException(ErrorCode.SECURITY_GROUP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpSecurityGroupRule.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public boolean checkUserIdSgIdInValid(String cloudId, String userId, String sgId)
    {
        TblCmpSecurityGroupExample example = new TblCmpSecurityGroupExample();
        TblCmpSecurityGroupExample.Criteria criteria = example.createCriteria();
        criteria.andSgIdEqualTo(sgId);
        criteria.andEeUserEqualTo(userId);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        long totalNum = networkRepository.countSecurityGroupByExample(example);
        return totalNum < 1;
    }
}
