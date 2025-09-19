package com.lnjoying.justice.cmp.service.nextstack.network;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.common.nextstack.AgentConstant;
import com.lnjoying.justice.cmp.common.nextstack.PhaseStatus;
import com.lnjoying.justice.cmp.db.model.*;
import com.lnjoying.justice.cmp.db.model.search.EipPortMapSearchCritical;
import com.lnjoying.justice.cmp.db.model.search.EipSearchCritical;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.VmInstanceCreateReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.VmInstancesCreateReq;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.network.SubnetSearchCritical;
import com.lnjoying.justice.cmp.domain.info.network.NetSummeryInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.network.VpcSearchCritical;
import com.lnjoying.justice.cmp.db.repo.NetworkRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.network.*;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstance;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.RpcInstanceInfo;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.*;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.rpc.RpcBmServiceImpl;
import com.lnjoying.justice.cmp.service.rpc.RpcVmServiceImpl;
import com.lnjoying.justice.cmp.utils.JsonUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
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
import org.springframework.util.CollectionUtils;

import javax.validation.constraints.NotBlank;
import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

@Service
public class NetworkService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NetworkService.class);

    @Autowired
    private NetworkRepository networkRepository;

    @Autowired
    private CloudService cloudService;

    @Autowired
    private RpcVmServiceImpl rpcVmService;

    @Autowired
    private RpcBmServiceImpl rpcBmService;

    @Autowired
    private UserService userService;

    public VpcsRspVo getVpcs(String cloudId, VpcSearchCritical vpcCritical, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVpcExample example = new TblCmpVpcExample();
            TblCmpVpcExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);

            if (!StringUtils.isBlank(vpcCritical.getName()))
            {
                criteria.andNameLike("%" + vpcCritical.getName() + "%");
            }

            if (!StringUtils.isBlank(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }

            if (null != vpcCritical.getPhaseStatus())
            {
                criteria.andPhaseStatusEqualTo(vpcCritical.getPhaseStatus().shortValue());
            }
            else
            {
                criteria.andPhaseStatusNotEqualTo((short)REMOVED);
            }

            VpcsRspVo getVpcsRsp = new VpcsRspVo();

            long totalNum = networkRepository.countVpcByExample(example);
            getVpcsRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getVpcsRsp;
            }

            int begin = ((vpcCritical.getPageNum() - 1) * vpcCritical.getPageSize());
            example.setOrderByClause("create_time desc,vpc_id desc");

            example.setStartRow(begin);
            example.setPageSize(vpcCritical.getPageSize());

            List<TblCmpVpc> tblCmpVpcList = networkRepository.getVpcs(example);
            if (null == tblCmpVpcList)
            {
                return getVpcsRsp;
            }
            List<VpcDetailInfoRspVo> vpcDetailInfoRsps = tblCmpVpcList.stream().map(vpc ->
            {
                VpcDetailInfoRspVo vpcDetailInfoRsp = new VpcDetailInfoRspVo();
                vpcDetailInfoRsp.setVpcDetailInfoRsp(vpc);
                vpcDetailInfoRsp.setEeBpName(userService.getBpName(vpc.getEeBp()));
                vpcDetailInfoRsp.setEeUserName(userService.getUserName(vpc.getEeUser()));
                return vpcDetailInfoRsp;
            }).collect(Collectors.toList());
            getVpcsRsp.setVpcs(vpcDetailInfoRsps);

            vpcDetailInfoRsps.forEach(vpc -> cloudService.syncSingleData(cloudId, vpc.getVpcId(), SyncMsg.NS_VPC));

            return getVpcsRsp;
        }
        catch (Exception e)
        {
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public VpcDetailInfoRspVo getVpc(String cloudId, String vpcId, String userId) throws WebSystemException
    {
        TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);
        if (null == tblCmpVpc || tblCmpVpc.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.VPC_NOT_EXIST, ErrorLevel.INFO);
        }
        if (StringUtils.isBlank(userId))
        {
            LOGGER.info("user is owner, vpcId: {}",vpcId);
        }
        else if (!Objects.equals(tblCmpVpc.getEeUser(), userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        VpcDetailInfoRspVo getVpcDetailInfoRsp = new VpcDetailInfoRspVo();

        getVpcDetailInfoRsp.setVpcDetailInfoRsp(tblCmpVpc);
        getVpcDetailInfoRsp.setEeBpName(userService.getBpName(tblCmpVpc.getEeBp()));
        getVpcDetailInfoRsp.setEeUserName(userService.getUserName(tblCmpVpc.getEeUser()));

        TblCmpSubnetExample example = new TblCmpSubnetExample();
        TblCmpSubnetExample.Criteria criteria = example.createCriteria();
        criteria.andVpcIdEqualTo(vpcId);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andPhaseStatusNotEqualTo((short)REMOVED);
        criteria.andEeStatusNotEqualTo(REMOVED);
        Integer count = (int) networkRepository.countSubnetByExample(example);
        getVpcDetailInfoRsp.setCount(count);

        cloudService.syncSingleData(cloudId, vpcId, SyncMsg.NS_VPC);

        return getVpcDetailInfoRsp;
    }

    public ResponseEntity createVpc(String cloudId, VpcCreateReqVo vpcInfo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVpc tblCmpVpc = new TblCmpVpc();
            tblCmpVpc.setVpcId(Utils.buildStr("EE_", Utils.assignUUId()));
            tblCmpVpc.setEeId(tblCmpVpc.getVpcId());
            tblCmpVpc.setCloudId(cloudId);
            tblCmpVpc.setEeBp(bpId);
            tblCmpVpc.setEeUser(userId);
            tblCmpVpc.setEeStatus(WAIT_CREATE);

            tblCmpVpc.setName(vpcInfo.getName());
            tblCmpVpc.setVpcCidr(vpcInfo.getCidr());
            tblCmpVpc.setAddressType(vpcInfo.getAddressType() == null ? (short) 0 : vpcInfo.getAddressType());
            tblCmpVpc.setPhaseStatus((short)PhaseStatus.ADDING);

            networkRepository.createVpc(tblCmpVpc);

            cloudService.createResource(cloudId, tblCmpVpc.getEeId(), bpId, userId, CreateResourceMsg.NS_VPC, vpcInfo);

            Map<String, String> res = new HashMap<>();
            res.put("vpcId", tblCmpVpc.getEeId());
            return ResponseEntity.status(HttpStatus.CREATED).body(res);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create vpc failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void createVpc(CreateResourceInfo createResourceInfo)
    {
        try
        {
            VpcCreateReqVo vpcInfo = (VpcCreateReqVo) createResourceInfo.getObject();
            String cloudId = createResourceInfo.getCloudId();
            String bpId = createResourceInfo.getBpId();
            String userId = createResourceInfo.getUserId();
            String eeId = createResourceInfo.getEeId();

            String url = "/api/network/v1/vpcs";
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, url, HttpMethod.POST, vpcInfo, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("create vpc error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String vpcId = (String) resultMap.get("vpcId");
                    if (StringUtils.isEmpty(vpcId))
                    {
                        LOGGER.error("create vpc error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVpc tblCmpVpc = new TblCmpVpc();
                        tblCmpVpc.setVpcId(vpcId);
                        tblCmpVpc.setCloudId(cloudId);
                        tblCmpVpc.setEeStatus(SYNCING);
                        tblCmpVpc.setEeId(eeId);

                        try
                        {
                            networkRepository.updateVpcByEeIdSelective(tblCmpVpc);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVpc oldTblCmpVpc = networkRepository.getVpcByEeId(cloudId, eeId);
                            TblCmpVpc updateTblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);

                            updateTblCmpVpc.setEeBp(bpId);
                            updateTblCmpVpc.setEeUser(userId);
                            updateTblCmpVpc.setEeStatus(SYNCING);
                            updateTblCmpVpc.setEeId(eeId);
                            networkRepository.updateVpc(updateTblCmpVpc);

                            oldTblCmpVpc.setEeStatus(REMOVED);
                            networkRepository.updateVpc(oldTblCmpVpc);
                        }

                        cloudService.syncSingleData(cloudId, vpcId, SyncMsg.NS_VPC);
                    }
                }
            }
            else
            {
                TblCmpVpc tblCmpVpc = networkRepository.getVpcByEeId(cloudId, eeId);
                tblCmpVpc.setPhaseStatus((short)PhaseStatus.ADD_FAILED);
                networkRepository.updateVpc(tblCmpVpc);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("create vpc failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeVpc(String cloudId, String vpcId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);
            if (tblCmpVpc == null)
            {
                LOGGER.error("get vpc null: device id {}", vpcId);
                throw new WebSystemException(ErrorCode.BAREMETAL_DEVICE_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpVpc.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            if (tblCmpVpc.getVpcId().equals(tblCmpVpc.getEeId()) && tblCmpVpc.getPhaseStatus() == (short)PhaseStatus.ADD_FAILED)
            {
                tblCmpVpc.setEeStatus(REMOVED);
                networkRepository.updateVpc(tblCmpVpc);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpVpc.setEeStatus(REMOVED);
                tblCmpVpc.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateVpc(tblCmpVpc);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove vpc failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public Set<String> getSubnetIds(String vpcId)
    {
        TblCmpSubnetExample example = new TblCmpSubnetExample();
        TblCmpSubnetExample.Criteria criteria = example.createCriteria();
        criteria.andVpcIdEqualTo(vpcId);
        criteria.andPhaseStatusNotEqualTo((short)REMOVED);
        if (0 == networkRepository.countSubnetByExample(example))
        {
            return new HashSet<>();
        }
        return networkRepository.getSubnets(example).stream().map(TblCmpSubnet::getSubnetId).collect(Collectors.toSet());
    }

    public Object getSubnets(String cloudId, SubnetSearchCritical subnetCritical, String userId) throws WebSystemException
    {
        try
        {
            SubnetsRspVo getSubnetsRsp = new SubnetsRspVo();
            TblCmpSubnetExample example = new TblCmpSubnetExample();
            TblCmpSubnetExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (!StringUtils.isBlank(subnetCritical.getName()))
            {
                criteria.andNameLike("%" + subnetCritical.getName() + "%");
            }
            if (!StringUtils.isBlank(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (null != subnetCritical.getPhaseStatus())
            {
                criteria.andPhaseStatusEqualTo(subnetCritical.getPhaseStatus().shortValue());
            }
            else
            {
                criteria.andPhaseStatusNotEqualTo((short)REMOVED);
            }
            if (!StringUtils.isBlank(subnetCritical.getVpcId()))
            {
                criteria.andVpcIdEqualTo(subnetCritical.getVpcId());
            }
            long totalNum = networkRepository.countSubnetByExample(example);
            getSubnetsRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getSubnetsRsp;
            }
            int begin = ((subnetCritical.getPageNum() - 1) * subnetCritical.getPageSize());
            example.setOrderByClause("create_time desc,subnet_id desc");

            example.setStartRow(begin);
            example.setPageSize(subnetCritical.getPageSize());

            List<TblCmpSubnet> tblCmpSubnetList = networkRepository.getSubnets(example);
            if (null == tblCmpSubnetList)
            {
                return getSubnetsRsp;
            }
            List<SubnetDetailInfoRspVo> subnetDetailInfoRsps = tblCmpSubnetList.stream().map(tblSubnet ->
            {
                SubnetDetailInfoRspVo subnetDetailInfoRsp = new SubnetDetailInfoRspVo();
                subnetDetailInfoRsp.setSubnetDetailInfoRsp(tblSubnet);
                subnetDetailInfoRsp.setEeBpName(userService.getBpName(tblSubnet.getEeBp()));
                subnetDetailInfoRsp.setEeUserName(userService.getUserName(tblSubnet.getEeUser()));
                TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, tblSubnet.getVpcId());
                if (tblCmpVpc != null) subnetDetailInfoRsp.setVpcName(tblCmpVpc.getName());
                return subnetDetailInfoRsp;
            }).collect(Collectors.toList());
            getSubnetsRsp.setSubnets(subnetDetailInfoRsps);

            subnetDetailInfoRsps.forEach(subnet -> cloudService.syncSingleData(cloudId, subnet.getSubnetId(), SyncMsg.NS_SUBNET));

            return getSubnetsRsp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public SubnetDetailInfoRspVo getSubnet(String cloudId, @NotBlank String subnetId, String userId) throws WebSystemException
    {
        TblCmpSubnet tblCmpSubnet = networkRepository.getSubnetById(cloudId, subnetId);
        if (null == tblCmpSubnet || tblCmpSubnet.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
        }
        if (StringUtils.isBlank(userId))
        {
            LOGGER.info("user is admin, subnetId: {}",subnetId);
        }
        else if (!Objects.equals(tblCmpSubnet.getEeUser(), userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        SubnetDetailInfoRspVo getSubnetDetailInfoRsp = new SubnetDetailInfoRspVo();

        getSubnetDetailInfoRsp.setSubnetDetailInfoRsp(tblCmpSubnet);
        getSubnetDetailInfoRsp.setEeBpName(userService.getBpName(tblCmpSubnet.getEeBp()));
        getSubnetDetailInfoRsp.setEeUserName(userService.getUserName(tblCmpSubnet.getEeUser()));

        LOGGER.info("subnetId: {}, get vpcId:{}", subnetId, tblCmpSubnet.getVpcId());

        if (!StringUtils.isBlank(tblCmpSubnet.getVpcId()))
        {
            TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, tblCmpSubnet.getVpcId());
            if (null != tblCmpVpc)
            {
                getSubnetDetailInfoRsp.setVpcName(tblCmpVpc.getName());
            }
        }

        cloudService.syncSingleData(cloudId, subnetId, SyncMsg.NS_SUBNET);

        return getSubnetDetailInfoRsp;
    }

    public ResponseEntity createSubnet(String cloudId, CreateSubnetReq subnetInfo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
            tblCmpSubnet.setSubnetId(Utils.buildStr("EE_", Utils.assignUUId()));
            tblCmpSubnet.setEeId(tblCmpSubnet.getSubnetId());
            tblCmpSubnet.setCloudId(cloudId);
            tblCmpSubnet.setEeBp(bpId);
            tblCmpSubnet.setEeUser(userId);
            tblCmpSubnet.setEeStatus(WAIT_CREATE);
            tblCmpSubnet.setVpcId(subnetInfo.getVpcId());

            tblCmpSubnet.setName(subnetInfo.getName());
            tblCmpSubnet.setSubnetCidr(subnetInfo.getCidr());
            tblCmpSubnet.setAddressType(subnetInfo.getAddressType() == null ? (short) 0 : subnetInfo.getAddressType().shortValue());
            tblCmpSubnet.setPhaseStatus((short)PhaseStatus.ADDING);

            networkRepository.createSubnet(tblCmpSubnet);

            cloudService.createResource(cloudId, tblCmpSubnet.getEeId(), bpId, userId, CreateResourceMsg.NS_SUBNET, subnetInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(tblCmpSubnet.getEeId());
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create subnet failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }
    public void createSubnet(CreateResourceInfo createResourceInfo)
    {
        try
        {
            CreateSubnetReq subnetInfo = (CreateSubnetReq) createResourceInfo.getObject();
            String cloudId = createResourceInfo.getCloudId();
            String bpId = createResourceInfo.getBpId();
            String userId = createResourceInfo.getUserId();
            String eeId = createResourceInfo.getEeId();
            if (subnetInfo.getVpcId().startsWith("EE_"))
            {
                TblCmpVpc tblCmpVpc = networkRepository.getVpcByEeId(cloudId, subnetInfo.getVpcId());
                if (tblCmpVpc == null || tblCmpVpc.getVpcId().startsWith("EE_"))
                {
                    cloudService.createResource(cloudId, eeId, bpId, userId, CreateResourceMsg.NS_SUBNET, subnetInfo);
                    return;
                }
                else
                {
                    subnetInfo.setVpcId(tblCmpVpc.getVpcId());
                }
            }

            String url = "/api/network/v1/subnets";
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, url, HttpMethod.POST, subnetInfo, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("create subnet error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    Map body = (Map) resultMap.get("body");
                    if (body == null)
                    {
                        LOGGER.error("create subnet error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    String subnetId = (String) body.get("subnetId");
                    if (StringUtils.isEmpty(subnetId))
                    {
                        LOGGER.error("create subnet error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
                        tblCmpSubnet.setSubnetId(subnetId);
                        tblCmpSubnet.setCloudId(cloudId);
                        tblCmpSubnet.setEeId(eeId);
                        tblCmpSubnet.setEeStatus(SYNCING);

                        try
                        {
                            networkRepository.updateSubnetByEeIdSelective(tblCmpSubnet);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpSubnet oldTblCmpSubnet = networkRepository.getSubnetByEeId(cloudId, eeId);
                            TblCmpSubnet updateTblCmpSubnet = networkRepository.getSubnetById(cloudId, subnetId);

                            updateTblCmpSubnet.setEeBp(bpId);
                            updateTblCmpSubnet.setEeUser(userId);
                            updateTblCmpSubnet.setEeStatus(SYNCING);
                            updateTblCmpSubnet.setEeId(eeId);
                            networkRepository.updateSubnet(updateTblCmpSubnet);

                            oldTblCmpSubnet.setEeStatus(REMOVED);
                            networkRepository.updateSubnet(oldTblCmpSubnet);
                        }
                    }

                    cloudService.syncSingleData(cloudId, subnetId, SyncMsg.NS_SUBNET);
                }
            }
            else
            {
                TblCmpSubnet tblCmpSubnet = networkRepository.getSubnetByEeId(cloudId, eeId);
                tblCmpSubnet.setPhaseStatus((short)PhaseStatus.ADD_FAILED);
                networkRepository.updateSubnet(tblCmpSubnet);
            }
        }
        catch (Exception e)
        {
            LOGGER.error("create subnet failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeSubnet(String cloudId, String subnetId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpSubnet tblCmpSubnet = networkRepository.getSubnetById(cloudId, subnetId);

            if (tblCmpSubnet == null)
            {
                LOGGER.error("get subnet null: device id {}", subnetId);
                throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpSubnet.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            if (tblCmpSubnet.getSubnetId().equals(tblCmpSubnet.getEeId()) && tblCmpSubnet.getPhaseStatus() == (short)PhaseStatus.ADD_FAILED)
            {
                tblCmpSubnet.setEeStatus(REMOVED);
                networkRepository.updateSubnet(tblCmpSubnet);
                return ResponseEntity.status(HttpStatus.OK).body(null);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpSubnet.setEeStatus(REMOVED);
                tblCmpSubnet.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateSubnet(tblCmpSubnet);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove subnet failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity createEip(String cloudId, EipCreateReqVo eipInfo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                EipsBaseRspVo eipsBaseRspVo = JsonUtil.jsonToPojo(JsonUtil.objectToJson(response.getBody()), EipsBaseRspVo.class);
                if (null == eipsBaseRspVo)
                {
                    LOGGER.error("add eip error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    if (! CollectionUtils.isEmpty(eipsBaseRspVo.getEipIds()))
                    {
                        for (String eipId : eipsBaseRspVo.getEipIds())
                        {
                            if (StringUtils.isEmpty(eipId))
                            {
                                LOGGER.error("add eip error");
                                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                            }
                            else
                            {
                                TblCmpEip tblCmpEip = new TblCmpEip();
                                tblCmpEip.setEipId(eipId);
                                tblCmpEip.setCloudId(cloudId);
//                                tblCmpEip.setEeBp(bpId);
//                                tblCmpEip.setEeUser(userId);
                                tblCmpEip.setEeStatus(SYNCING);

                                tblCmpEip.setIpaddr(eipInfo.getStartIpAddress());
                                try
                                {
                                    networkRepository.createEip(tblCmpEip);
                                }
                                catch (DuplicateKeyException e)
                                {
                                    TblCmpEip updateTblCmpEip = networkRepository.getEipById(cloudId, eipId);
//                                    updateTblCmpEip.setEeBp(bpId);
//                                    updateTblCmpEip.setEeUser(userId);
                                    updateTblCmpEip.setEeStatus(SYNCING);
                                    networkRepository.updateEipByPrimaryKeySelective(updateTblCmpEip);
                                }

                                cloudService.syncSingleData(cloudId, eipId, SyncMsg.NS_EIP);
                            }
                        }
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add eip failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeEip(String cloudId, String eipId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipId);

            if (tblCmpEip == null)
            {
                LOGGER.error("get eip null: eip id {}", eipId);
                throw new WebSystemException(ErrorCode.EIP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEip.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpEip.setEeStatus(REMOVED);
                tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateEipByPrimaryKeySelective(tblCmpEip);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove eip failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public EipInfoVo getEip(String cloudId, @NotBlank String eipId, String userId)
    {
        TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipId);
        return  getEip(cloudId, tblCmpEip, userId);
    }

    public EipInfoVo getEip(String cloudId, TblCmpEip tblCmpEip, String userId) throws WebSystemException
    {
        if (null == tblCmpEip || tblCmpEip.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.EIP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (null == userId)
        {
            LOGGER.info("user is admin, eipId: {}", tblCmpEip.getEipId());
        }
        else if (!Objects.equals(tblCmpEip.getEeUser(), userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        StringBuilder boundName = new StringBuilder();
        String boundId = null;
        String boundType = null;
        if (StringUtils.isNotBlank(tblCmpEip.getBoundId()))
        {
            if (AgentConstant.NAT_BOUND.equals(tblCmpEip.getBoundType()))
            {
                String boundIdsStr = tblCmpEip.getBoundId();
                boundId = boundIdsStr;
                String[] boundIds = boundIdsStr.split(",");
                for (String id : boundIds)
                {
                    TblCmpEipMap tblCmpEipMap = networkRepository.getEipMapById(cloudId, id);
                    if (null == tblCmpEipMap || REMOVED == tblCmpEipMap.getStatus() || REMOVED == tblCmpEipMap.getEeStatus())
                    {
                        LOGGER.error("eipMap not exists, eipId: {}, eipMapId:{}", tblCmpEip.getEipId(), tblCmpEip.getBoundId());
                    }
                    else
                    {
                        boundName.append(tblCmpEipMap.getMapName()).append(",");
                        boundType = AgentConstant.NAT_BOUND;
                    }
                }
            }
            else if (AgentConstant.PORT_BOUND.equals(tblCmpEip.getBoundType()))
            {
                TblCmpPort tblCmpPort = networkRepository.getPortById(cloudId, tblCmpEip.getBoundId());
                if (null == tblCmpPort)
                {
                    LOGGER.error("port not exists, eipId: {}, portId:{}", tblCmpEip.getEipId(), tblCmpEip.getBoundId());
                }
                else
                {
                    RpcInstance instance = rpcVmService.getVmInstanceFromPortId(cloudId, tblCmpEip.getBoundId());
                    boundName = new StringBuilder(instance.getInstanceName());
                    boundId = instance.getInstanceId();
                    boundType = AgentConstant.PORT_BOUND;
                }
            }
        }

        cloudService.syncSingleData(cloudId, tblCmpEip.getEipId(), SyncMsg.NS_EIP);

        boundName = new StringBuilder(removeSuffix(boundName.toString(), ","));
        return EipInfoVo.builder()
                .eipId(tblCmpEip.getEipId())
                .addressType(tblCmpEip.getAddressType() == null ? null : tblCmpEip.getAddressType().intValue())
                .ipAddress(tblCmpEip.getIpaddr())
                .boundId(boundId)
                .boundName(boundName.toString())
                .boundType(boundType)
                .eipPoolId(tblCmpEip.getPoolId())
                .publicIp(tblCmpEip.getPublicIp())
                .phaseStatus(tblCmpEip.getStatus() == null ? null : tblCmpEip.getStatus().intValue())
                .createTime(Utils.formatDate(tblCmpEip.getCreateTime()))
                .updateTime(Utils.formatDate(tblCmpEip.getUpdateTime()))
                .eeBp(tblCmpEip.getEeBp())
                .eeUser(tblCmpEip.getEeUser())
                .eeBpName(userService.getBpName(tblCmpEip.getEeBp()))
                .eeUserName(userService.getUserName(tblCmpEip.getEeUser()))
                .chargeType(tblCmpEip.getChargeType())
                .period(tblCmpEip.getPeriod())
                .priceUnit(tblCmpEip.getPriceUnit())
                .expireTime(tblCmpEip.getExpireTime())
                .build();
    }

    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (!isEmpty(str) && !isEmpty(suffix)) {
            String str2 = str.toString();
            return str2.endsWith(suffix.toString()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        } else {
            return str(str);
        }
    }

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String subPre(CharSequence string, int toIndexExclude) {
        return sub(string, 0, toIndexExclude);
    }

    public static String sub(CharSequence str, int fromIndexInclude, int toIndexExclude) {
        if (isEmpty(str)) {
            return str(str);
        } else {
            int len = str.length();
            if (fromIndexInclude < 0) {
                fromIndexInclude += len;
                if (fromIndexInclude < 0) {
                    fromIndexInclude = 0;
                }
            } else if (fromIndexInclude > len) {
                fromIndexInclude = len;
            }

            if (toIndexExclude < 0) {
                toIndexExclude += len;
                if (toIndexExclude < 0) {
                    toIndexExclude = len;
                }
            } else if (toIndexExclude > len) {
                toIndexExclude = len;
            }

            if (toIndexExclude < fromIndexInclude) {
                int tmp = fromIndexInclude;
                fromIndexInclude = toIndexExclude;
                toIndexExclude = tmp;
            }

            return fromIndexInclude == toIndexExclude ? "" : str.toString().substring(fromIndexInclude, toIndexExclude);
        }
    }

    public static String str(CharSequence cs) {
        return null == cs ? null : cs.toString();
    }

    private TblCmpEipExample.Criteria checkEipBindingType(String boundType, TblCmpEipExample.Criteria criteria)
    {
        if (null == boundType)
        {
            return criteria;
        }
        switch (boundType)
        {
            case AgentConstant.NAT_BOUND:
                criteria.andBoundTypeEqualTo(AgentConstant.NAT_BOUND);
                criteria.andBoundIdIsNotNull();
                break;
            case AgentConstant.PORT_BOUND:
                criteria.andBoundTypeEqualTo(AgentConstant.PORT_BOUND);
                criteria.andBoundIdIsNotNull();
                break;
            case AgentConstant.UNBOUND:
                criteria.andBoundIdIsNull();
                break;
            case AgentConstant.BOUND:
                criteria.andBoundIdIsNotNull();
                break;
            default:
                throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
        return criteria;
    }

    public GetEipsRsp getEips(String cloudId, EipSearchCritical eipSearchCritical, String userId, Boolean oneTOne) throws WebSystemException
    {
        try
        {
            GetEipsRsp getEipsRsp = new GetEipsRsp();
            TblCmpEipExample example = new TblCmpEipExample();
            TblCmpEipExample.Criteria criteria = example.createCriteria();
            TblCmpEipMapExample tblCmpEipMapExample = new TblCmpEipMapExample();
            TblCmpEipMapExample.Criteria eipMapCriteria = tblCmpEipMapExample.createCriteria();

            criteria = checkEipBindingType(eipSearchCritical.getBoundType(), criteria);

            criteria.andStatusNotEqualTo((short)REMOVED);
            eipMapCriteria.andStatusNotEqualTo((short)REMOVED);
            tblCmpEipMapExample.setOrderByClause("create_time desc");

            criteria.andCloudIdEqualTo(cloudId);
            eipMapCriteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            eipMapCriteria.andEeStatusNotEqualTo(REMOVED);

            if (StringUtils.isNotEmpty(eipSearchCritical.getEipPoolId()))
            {
                criteria.andPoolIdEqualTo(eipSearchCritical.getEipPoolId());
            }

            if (StringUtils.isNotEmpty(eipSearchCritical.getVpcId()))
            {
                TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, eipSearchCritical.getVpcId());
                if (null != tblCmpVpc && tblCmpVpc.getVlanId() != null)
                {
                    String poolId = getEipPoolByVlanId(tblCmpVpc.getVlanId());
                    if (StringUtils.isEmpty(eipSearchCritical.getEipPoolId()))
                    {
                        criteria.andPoolIdEqualTo(poolId);
                    }
                    else if ( !Objects.equals(poolId,eipSearchCritical.getEipPoolId()))
                    {
                        return getEipsRsp;
                    }

                }
            }
            if (StringUtils.isNotEmpty(userId))
            {
                eipMapCriteria.andEeUserEqualTo(userId);
                criteria.andEeUserEqualTo(userId);
            }
            if (StringUtils.isNotEmpty(eipSearchCritical.getIpAddress()))
            {
                criteria.andIpaddrLike("%" + eipSearchCritical.getIpAddress() + "%");
            }
            if (null != oneTOne && !oneTOne)
            {
                eipMapCriteria.andIsOneToOneEqualTo(true);
                TblCmpSubnetExample subnetExample = new TblCmpSubnetExample();
                TblCmpSubnetExample.Criteria subnetCriteria = subnetExample.createCriteria();
                subnetCriteria.andCloudIdEqualTo(cloudId);
                subnetCriteria.andEeStatusNotEqualTo(REMOVED);
                subnetCriteria.andVpcIdEqualTo(eipSearchCritical.getVpcId());
                subnetCriteria.andPhaseStatusNotEqualTo((short)REMOVED);

                if (networkRepository.countSubnetByExample(subnetExample)>0)
                {
                    List<TblCmpSubnet> tblCmpSubnets = networkRepository.getSubnets(subnetExample);
                    if (StringUtils.isNotEmpty(userId))
                    {
                        tblCmpEipMapExample.or().andCloudIdEqualTo(cloudId).andEeStatusNotEqualTo(REMOVED)
                                .andSubnetIdNotIn(tblCmpSubnets.stream().map(TblCmpSubnet::getSubnetId).distinct().collect(Collectors.toList()))
                                .andStatusNotEqualTo((short)REMOVED).andEeUserEqualTo(userId);
                    }
                    else
                    {
                        tblCmpEipMapExample.or().andCloudIdEqualTo(cloudId).andEeStatusNotEqualTo(REMOVED)
                                .andSubnetIdNotIn(tblCmpSubnets.stream().map(TblCmpSubnet::getSubnetId).distinct().collect(Collectors.toList()))
                                .andStatusNotEqualTo((short)REMOVED);
                    }
                }
            }

            List<String> eipMapList = new ArrayList<>();
            List<EipInfoVo> eipInfoList;

            long totalNum = networkRepository.countEipByExample(example);

            if (null != oneTOne)
            {
                criteria.andBoundTypeEqualToOrBoundIdIsNull(AgentConstant.NAT_BOUND);
                criteria.andStatusNotEqualTo((short)REMOVED);
                totalNum = networkRepository.countEipByExample(example);
                eipMapList = networkRepository.getEipMaps(tblCmpEipMapExample).stream()
                        .map(TblCmpEipMap::getEipId).distinct().collect(Collectors.toList());
                long eipMapNum = eipMapList.size();
                totalNum -= eipMapNum;
            }
            getEipsRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getEipsRsp;
            }

            int begin = ((eipSearchCritical.getPageNum() - 1) * eipSearchCritical.getPageSize());
            example.setOrderByClause("create_time desc,ipaddr desc");

            example.setStartRow(begin);
            example.setPageSize(eipSearchCritical.getPageSize());

            List<TblCmpEip> tblCmpEipList = networkRepository.getEips(example);
            if (null == tblCmpEipList)
            {
                return getEipsRsp;
            }

            if (oneTOne != null)
            {
                List<String> eipIdList = tblCmpEipList.stream().map(TblCmpEip::getEipId).distinct().collect(Collectors.toList());
                eipIdList.removeAll(eipMapList);
                eipInfoList = eipIdList.stream().map(
                        eipId -> getEip(cloudId, eipId, userId)
                ).collect(Collectors.toList());
            }
            else
            {
                eipInfoList = tblCmpEipList.stream().map(
                        tblEip -> getEip(cloudId, tblEip, userId)
                ).collect(Collectors.toList());
            }

            getEipsRsp.setEips(eipInfoList);

            eipInfoList.forEach(eipInfoVo -> cloudService.syncSingleData(cloudId, eipInfoVo.getEipId(), SyncMsg.NS_EIP));

            return getEipsRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get eips error, {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public EipPortsRspVo getEipProtocolPorts(String cloudId, String userId, String eipId, int protocol)
    {
        EipPortsRspVo getEipPortsRsp = new EipPortsRspVo();
        List<Integer> ports = networkRepository.getEipProtocolPorts(cloudId, userId, eipId, (short) protocol);
        getEipPortsRsp.setPorts(ports);
        getEipPortsRsp.setProtocol(protocol);
        getEipPortsRsp.setEipId(eipId);
        return getEipPortsRsp;
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity createEipPortMap(String cloudId, EipPortMapCreateReqVo eipPortMapInfo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add eip port error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String uuid = (String) resultMap.get("natId");
                    if (StringUtils.isEmpty(uuid))
                    {
                        LOGGER.error("add eip port error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpEipMap tblCmpEipMap = new TblCmpEipMap();
                        tblCmpEipMap.setEipMapId(uuid);
                        tblCmpEipMap.setCloudId(cloudId);
                        tblCmpEipMap.setEeBp(bpId);
                        tblCmpEipMap.setEeUser(userId);
                        tblCmpEipMap.setEeStatus(SYNCING);

                        tblCmpEipMap.setMapName(eipPortMapInfo.getMapName());
                        try
                        {
                            networkRepository.createEipMap(tblCmpEipMap);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpEipMap updateTblCmpEipMap = networkRepository.getEipMapById(cloudId, uuid);
                            updateTblCmpEipMap.setEeBp(bpId);
                            updateTblCmpEipMap.setEeUser(userId);
                            updateTblCmpEipMap.setEeStatus(SYNCING);
                            networkRepository.updateEipMap(updateTblCmpEipMap);
                        }

                        cloudService.syncSingleData(cloudId, uuid, SyncMsg.NS_PORT_MAP);
                        cloudService.syncSingleData(cloudId, eipPortMapInfo.getEipId(), SyncMsg.NS_EIP, DataSyncLevel.LEVEL_6);
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add eip map failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public EipPortMapRspVo getEipPortMap(String cloudId, String eipMapId, String userId) throws WebSystemException
    {
        cloudService.syncSingleData(cloudId, eipMapId, SyncMsg.NS_PORT_MAP);

        TblCmpEipMap tblEipMap = networkRepository.getEipMapById(cloudId, eipMapId);
        if (null == tblEipMap || REMOVED == tblEipMap.getStatus())
        {
            throw new WebSystemException(ErrorCode.EIP_MAP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (StringUtils.isBlank(tblEipMap.getEipId()))
        {
            throw new WebSystemException(ErrorCode.EIP_MAP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (null == userId)
        {
            LOGGER.info("user is admin, eipId: {}", eipMapId);
        }
        else if (!Objects.equals(tblEipMap.getEeUser(), userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        EipPortMapRspVo eipPortMapInfo = new EipPortMapRspVo();

        TblCmpPortMapExample example = new TblCmpPortMapExample();
        TblCmpPortMapExample.Criteria criteria = example.createCriteria();
        criteria.andEipMapIdEqualTo(eipMapId);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        example.setOrderByClause("create_time desc,port_map_id desc");

        TblCmpSubnet tblSubnet = networkRepository.getSubnetById(cloudId, tblEipMap.getSubnetId());
        if (null == tblSubnet)
        {
            throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
        }
        TblCmpVpc tblVpc = networkRepository.getVpcById(cloudId, tblSubnet.getVpcId());
        if (null == tblVpc)
        {
            throw new WebSystemException(ErrorCode.VPC_NOT_EXIST, ErrorLevel.INFO);
        }
        TblCmpEip tblEip = networkRepository.getEipById(cloudId, tblEipMap.getEipId());

        eipPortMapInfo.setEipMapId(tblEipMap.getEipMapId());
        eipPortMapInfo.setEipId(tblEip.getEipId());
        eipPortMapInfo.setSubnetCidr(tblSubnet.getSubnetCidr());
        eipPortMapInfo.setSubnetId(tblSubnet.getSubnetId());
        eipPortMapInfo.setVpcId(tblSubnet.getVpcId());
        eipPortMapInfo.setSubnetName(tblSubnet.getName());
        eipPortMapInfo.setVpcName(tblVpc.getName());
        eipPortMapInfo.setBandwidth(tblEipMap.getBandwidth());
        eipPortMapInfo.setUserId(tblEipMap.getUserId());
        eipPortMapInfo.setMapName(tblEipMap.getMapName());
        eipPortMapInfo.setEipAddress(tblEip.getIpaddr());
        eipPortMapInfo.setInsideIp(tblEipMap.getInsideIp());
        eipPortMapInfo.setOneToOne(tblEipMap.getIsOneToOne());
        eipPortMapInfo.setPhaseStatus(tblEipMap.getStatus() == null ? null : tblEipMap.getStatus().intValue());
        eipPortMapInfo.setPublicIp(tblEip.getPublicIp());
        eipPortMapInfo.setCreateTime(Utils.formatDate(tblEipMap.getCreateTime()));

        TblCmpPort tblCmpPort = networkRepository.getPortById(cloudId, tblEipMap.getPortId());
        String vmInstanceId = tblCmpPort == null ? null : tblCmpPort.getInstanceId();
        RpcInstance instance;

        if (!StringUtils.isBlank(vmInstanceId))
        {
            eipPortMapInfo.setVm(true);
            instance = rpcVmService.getVmInstanceFromPortId(cloudId, tblEipMap.getPortId());
        }
        else
        {
            eipPortMapInfo.setVm(false);
            instance = rpcBmService.getBaremetalInstanceFromPortId(cloudId, tblEipMap.getPortId());
        }
        eipPortMapInfo.setInstanceName(instance.getInstanceName());
        eipPortMapInfo.setInstanceId(instance.getInstanceId());

        if (!tblEipMap.getIsOneToOne())
        {
            List<TblCmpPortMap> portMaps = networkRepository.getPortMaps(example);

            List<EipPortMapCreateReqVo.portMap> portMapList = new ArrayList<>();
            for (TblCmpPortMap tblPortMap : portMaps)
            {
                EipPortMapCreateReqVo.portMap tmpPortMap = new EipPortMapCreateReqVo.portMap();
                tmpPortMap.setGlobalPort(tblPortMap.getGlobalPort());
                tmpPortMap.setLocalPort(tblPortMap.getLocalPort());
                tmpPortMap.setPortMapId(tblPortMap.getPortMapId());
                tmpPortMap.setProtocol(tblPortMap.getProtocol() == null ? null : tblPortMap.getProtocol().intValue());
                tmpPortMap.setCreateTime(Utils.formatDate(tblPortMap.getCreateTime()));
                tmpPortMap.setUpdateTime(Utils.formatDate(tblPortMap.getUpdateTime()));
                portMapList.add(tmpPortMap);
            }

            eipPortMapInfo.setPortMaps(portMapList);
        }
        return eipPortMapInfo;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ResponseEntity removeEipPortMap(String cloudId, String eipMapId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEipMap tblCmpEipMap = networkRepository.getEipMapById(cloudId, eipMapId);

            if (tblCmpEipMap == null)
            {
                LOGGER.error("get eip port map null: eip map id {}", eipMapId);
                throw new WebSystemException(ErrorCode.PORT_MAP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEipMap.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpEipMap.setEeStatus(REMOVED);
                tblCmpEipMap.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateEipMap(tblCmpEipMap);

                cloudService.syncSingleData(cloudId, tblCmpEipMap.getEipId(), SyncMsg.NS_EIP, DataSyncLevel.LEVEL_6);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove eip port map failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public GetEipPortMapsRsp getEipPortMaps(String cloudId, EipPortMapSearchCritical searchCritical, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEipMapExample example = new TblCmpEipMapExample();
            TblCmpEipMapExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andStatusNotEqualTo((short) REMOVED);
            if (StringUtils.isNotEmpty(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (StringUtils.isNotEmpty(searchCritical.getName()))
            {
                criteria.andMapNameLike("%" + searchCritical.getName() + "%");
            }
            if (!StringUtils.isBlank(searchCritical.getEipId()))
            {
                criteria.andEipIdEqualTo(searchCritical.getEipId());
            }

            GetEipPortMapsRsp getEipPortMapsRsp = new GetEipPortMapsRsp();

            long totalNum = networkRepository.countEipMapByExample(example);
            getEipPortMapsRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getEipPortMapsRsp;
            }

            int begin = ((searchCritical.getPageNum() - 1) * searchCritical.getPageSize());
            example.setOrderByClause("create_time desc,eip_map_id desc");

            example.setStartRow(begin);
            example.setPageSize(searchCritical.getPageSize());

            List<TblCmpEipMap> tblCmpEipMapList = networkRepository.getEipMaps(example);
            if (null == tblCmpEipMapList || 0 == tblCmpEipMapList.size())
            {
                return getEipPortMapsRsp;
            }

            List<EipPortMapRspVo> eipPortMapInfoList = new ArrayList<>();
            for (TblCmpEipMap tblCmpEipMap : tblCmpEipMapList)
            {
                EipPortMapRspVo eipPortMap = getEipPortMap(cloudId, tblCmpEipMap.getEipMapId(), tblCmpEipMap.getEeUser());
                eipPortMapInfoList.add(eipPortMap);
            }

            getEipPortMapsRsp.setEipPortMaps(eipPortMapInfoList);

            return getEipPortMapsRsp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public TopologyRspVo getTopology(String cloudId, @NotBlank String vpcId, String userId) throws WebSystemException
    {
        try
        {
            LOGGER.info("get topology, vpcId: {}",vpcId);
            TblCmpSubnetExample example = new TblCmpSubnetExample();
            TblCmpSubnetExample.Criteria criteria = example.createCriteria();
            criteria.andVpcIdEqualTo(vpcId);
            criteria.andPhaseStatusNotEqualTo((short)REMOVED);
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (!StringUtils.isBlank(userId))
            {
                criteria.andEeUserEqualTo(userId);
            }

            List<TblCmpSubnet> tblCmpSubnets = networkRepository.getSubnets(example);
            TopologyRspVo topologyRsp = new TopologyRspVo();
            if (0 == tblCmpSubnets.size())
            {
                return topologyRsp;
            }
            List<String> subnetIds = tblCmpSubnets.stream().map(TblCmpSubnet::getSubnetId).collect(Collectors.toList());

            Map<String, List<RpcInstanceInfo>> bmInstanceInfos =  rpcBmService.getInstanceInfos(cloudId, subnetIds);
            Map<String, List<RpcInstanceInfo>> vmInstanceInfos = rpcVmService.getInstanceInfos(cloudId, subnetIds);
            Map<String, List<RpcInstanceInfo>> subnetInstancesMap = new HashMap<>();

            bmInstanceInfos.forEach(
                    (k,v)->{
                        List<RpcInstanceInfo> vmInstanceInfoList = vmInstanceInfos.get(k);
                        List<RpcInstanceInfo> bmInstanceInfoList = bmInstanceInfos.get(k);
                        vmInstanceInfoList.addAll(bmInstanceInfoList);
                        subnetInstancesMap.put(k,vmInstanceInfoList);
                    }
            );
            List<TopologyRspVo.SubnetTopology> subnetTopologies = tblCmpSubnets.stream().map(tblCmpSubnet -> {
                TopologyRspVo.SubnetTopology subnetTopology = new TopologyRspVo.SubnetTopology();
                subnetTopology.setSubnetId(tblCmpSubnet.getSubnetId());
                subnetTopology.setSubnetName(tblCmpSubnet.getName());
                subnetTopology.setCidr(tblCmpSubnet.getSubnetCidr());
                subnetTopology.setInstanceInfos(subnetInstancesMap.get(tblCmpSubnet.getSubnetId()).stream().peek(
                        instanceInfo ->
                        {
                            if (null == instanceInfo.getPortId())
                            {
                                return;
                            }
                            String ip = networkRepository.getPortById(cloudId, instanceInfo.getPortId()).getIpAddress();
                            instanceInfo.setIp(ip);
                        }
                ).collect(Collectors.toList()));
                return subnetTopology;
            }).collect(Collectors.toList());
            topologyRsp.setVpcId(vpcId);
            topologyRsp.setSubnetTopologies(subnetTopologies);
            return topologyRsp;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public ResponseEntity createEipPool(String cloudId, EipPoolCreateReqVo eipPoolCreateReqVo, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("create eip pool error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String uuid = (String) resultMap.get("eipPoolId");
                    if (StringUtils.isEmpty(uuid))
                    {
                        LOGGER.error("create eip pool error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpEipPool tblCmpEipPool = new TblCmpEipPool();
                        tblCmpEipPool.setPoolId(uuid);
                        tblCmpEipPool.setCloudId(cloudId);
                        tblCmpEipPool.setEeBp(bpId);
                        tblCmpEipPool.setEeUser(userId);
                        tblCmpEipPool.setEeStatus(SYNCING);

                        tblCmpEipPool.setName(eipPoolCreateReqVo.getName());
                        tblCmpEipPool.setDescription(eipPoolCreateReqVo.getDescription());
                        tblCmpEipPool.setPhaseStatus(PhaseStatus.ADDED);
                        try
                        {
                            networkRepository.createEipPool(tblCmpEipPool);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpEipPool updateTblCmpEipPool = networkRepository.getEipPoolById(cloudId, uuid);
                            updateTblCmpEipPool.setEeBp(bpId);
                            updateTblCmpEipPool.setEeUser(userId);
                            updateTblCmpEipPool.setEeStatus(SYNCING);
                            networkRepository.updateEipPool(updateTblCmpEipPool);
                        }

                        cloudService.syncSingleData(cloudId, uuid, SyncMsg.NS_EIP_POOL);
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("create eip pool failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeEipPool(String cloudId, String eipPoolId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEipPool tblCmpEipPool = networkRepository.getEipPoolById(cloudId, eipPoolId);

            if (tblCmpEipPool == null)
            {
                LOGGER.error("get eip pool null: eip pool id {}", eipPoolId);
                throw new WebSystemException(ErrorCode.EIP_POOL_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (!isAdmin() && !cloudService.isOwner(cloudId, userId) && !tblCmpEipPool.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpEipPool.setEeStatus(REMOVED);
                tblCmpEipPool.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateEipPool(tblCmpEipPool);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove eip pool failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public EipPoolDetailInfoRspVo getEipPool(String cloudId, String eipPoolId)
    {
        TblCmpEipPool tblCmpEipPool = networkRepository.getEipPoolById(cloudId, eipPoolId);
        if (null == tblCmpEipPool || REMOVED == tblCmpEipPool.getPhaseStatus())
        {
            throw new WebSystemException(ErrorCode.EIP_POOL_NOT_EXISTS,ErrorLevel.INFO);
        }
        TblCmpEipPoolVpcRefExample example = new TblCmpEipPoolVpcRefExample();
        TblCmpEipPoolVpcRefExample.Criteria criteria = example.createCriteria();
        criteria.andPoolIdEqualTo(eipPoolId);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        int vlanId = 0;
        if (networkRepository.countEipPoolAndVpcRefByExample(example) > 0 )
        {
            vlanId = networkRepository.getEipPoolAndVpcRefs(example).get(0).getVlanId();
        }

        EipPoolDetailInfoRspVo getEipPoolDetailInfoRsp = new EipPoolDetailInfoRspVo();
        getEipPoolDetailInfoRsp.setEipPool(tblCmpEipPool);
        getEipPoolDetailInfoRsp.setVlanId(vlanId);

        cloudService.syncSingleData(cloudId, eipPoolId, SyncMsg.NS_EIP_POOL);

        return getEipPoolDetailInfoRsp;
    }

    public EipPoolsRspVo getEipPools(String cloudId, String name, String userId)
    {
        EipPoolsRspVo getEipPoolsRsp = new EipPoolsRspVo();
        TblCmpEipPoolExample example = new TblCmpEipPoolExample();
        TblCmpEipPoolExample.Criteria criteria = example.createCriteria();
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(name))
        {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isBlank(userId))
        {
            criteria.andEeUserEqualTo(userId);
        }
        long totalNum = networkRepository.countEipPoolByExample(example);
        getEipPoolsRsp.setTotalNum(totalNum);
        if (totalNum > 0)
        {
            example.setOrderByClause("create_time desc,pool_id desc");
            List<TblCmpEipPool> tblCmpEipPools = networkRepository.getEipPools(example);

            List<String> poolIds = new ArrayList<>();
            List<EipPoolRspVo> eipPools = tblCmpEipPools.stream().map(tblCmpEipPool -> {
                EipPoolRspVo eipPoolRspVo = new EipPoolRspVo();
                eipPoolRspVo.setEipPoolRspVo(tblCmpEipPool);
                eipPoolRspVo.setAvailable(checkEipPoolAvailable(cloudId, tblCmpEipPool));
                if (eipPoolRspVo.isAvailable()) getEipPoolsRsp.setAvailable(true);
                poolIds.add(eipPoolRspVo.getPoolId());
                return eipPoolRspVo;
            }).collect(Collectors.toList());

            getEipPoolsRsp.setEipPools(eipPools);

            getEipPoolsRsp.getEipPools().forEach(eipPool -> cloudService.syncSingleData(cloudId, eipPool.getPoolId(), SyncMsg.NS_EIP_POOL));
        }
        return getEipPoolsRsp;
    }

    private boolean checkEipPoolAvailable(String cloudId, TblCmpEipPool tblCmpEipPool)
    {
        try
        {
            TblCmpEipExample example = new TblCmpEipExample();
            TblCmpEipExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andEeUserIsNull();
            criteria.andEeBpIsNull();
            criteria.andBoundIdIsNull();
            criteria.andBoundTypeIsNull();
            criteria.andPoolIdEqualTo(tblCmpEipPool.getPoolId());

            example.setPageSize(3);
            example.setStartRow(0);

            long availableNum = networkRepository.countEipByExample(example);

            if (availableNum != 0)
            {
                return true;
            }
            return false;
        }
        catch (Exception e)
        {
            LOGGER.error("check eipPool available failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity addEipPoolAndVpcRef(String cloudId, CreateEipPoolAndVpcRefReq request, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add eip port error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String eipPoolVpcRefId = (String) resultMap.get("eipPoolVpcRefId");
                    if (StringUtils.isEmpty(eipPoolVpcRefId))
                    {
                        LOGGER.error("add eip port error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef = new TblCmpEipPoolVpcRef();
                        tblCmpEipPoolVpcRef.setPoolVpcId(eipPoolVpcRefId);
                        tblCmpEipPoolVpcRef.setCloudId(cloudId);
                        tblCmpEipPoolVpcRef.setEeBp(bpId);
                        tblCmpEipPoolVpcRef.setEeUser(userId);
                        tblCmpEipPoolVpcRef.setEeStatus(SYNCING);
                        try
                        {
                            networkRepository.createEipPoolAndVpcRef(tblCmpEipPoolVpcRef);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpEipPoolVpcRef updateTblCmpEipPoolVpcRef = networkRepository.getEipPoolAndVpcRefById(cloudId, eipPoolVpcRefId);
                            updateTblCmpEipPoolVpcRef.setEeBp(bpId);
                            updateTblCmpEipPoolVpcRef.setEeUser(userId);
                            updateTblCmpEipPoolVpcRef.setEeStatus(SYNCING);
                            networkRepository.updateEipPoolAndVpcRef(updateTblCmpEipPoolVpcRef);
                        }
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("add eipPoolVpcRef failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeEipPoolAndVpcRef(String cloudId, String eipPoolVpcRefId, String userId)
    {
        try
        {
            TblCmpEipPoolVpcRef tblCmpEipPoolVpcRef = networkRepository.getEipPoolAndVpcRefById(cloudId, eipPoolVpcRefId);

            if (tblCmpEipPoolVpcRef == null)
            {
                LOGGER.error("get eip pool and vpc ref null: eipPoolVpcRefId {}", eipPoolVpcRefId);
                throw new WebSystemException(ErrorCode.EIP_POOL_VPC_RELATION_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (!isAdmin() && !cloudService.isOwner(cloudId, userId) && StringUtils.isNotEmpty(tblCmpEipPoolVpcRef.getEeUser()) && !tblCmpEipPoolVpcRef.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpEipPoolVpcRef.setEeStatus(REMOVED);
                tblCmpEipPoolVpcRef.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                networkRepository.updateEipPoolAndVpcRef(tblCmpEipPoolVpcRef);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove eip pool and vpc ref failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public String getEipPoolByVlanId(Integer vlanId)
    {
        try
        {
            TblCmpEipPoolVpcRefExample example = new TblCmpEipPoolVpcRefExample();
            TblCmpEipPoolVpcRefExample.Criteria criteria = example.createCriteria();
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            criteria.andVlanIdEqualTo(vlanId);
            if (networkRepository.countEipPoolAndVpcRefByExample(example) > 0)
            {
                return networkRepository.getEipPoolAndVpcRefs(example).stream().map(TblCmpEipPoolVpcRef::getPoolId).collect(Collectors.toList()).get(0);
            }
            example = new TblCmpEipPoolVpcRefExample();
            criteria = example.createCriteria();
            criteria.andVlanIdEqualTo(0);
            criteria.andPhaseStatusNotEqualTo(REMOVED);
            return networkRepository.getEipPoolAndVpcRefs(example).stream().map(TblCmpEipPoolVpcRef::getPoolId).collect(Collectors.toList()).get(0);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public List<String> getVips(String cloudId, String subnetId)
    {
        TblCmpPortExample example = new TblCmpPortExample();
        TblCmpPortExample.Criteria criteria = example.createCriteria();
        criteria.andPhaseStatusNotEqualTo((short)REMOVED);
        criteria.andSubnetIdEqualTo(subnetId);
        criteria.andTypeEqualTo(PortType.vip);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        List<TblCmpPort> tblCmpPorts = networkRepository.getPorts(example);
        return tblCmpPorts.stream().map(
                TblCmpPort::getIpAddress).distinct().collect(Collectors.toList());
    }

    public NetSummeryInfo getNetSummery(String cloudId, String userId)
    {
        TblCmpEipMapExample example = new TblCmpEipMapExample();
        TblCmpEipMapExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);

        TblCmpEipExample eipExample = new TblCmpEipExample();
        TblCmpEipExample.Criteria eipCriteria = eipExample.createCriteria();
        eipCriteria.andCloudIdEqualTo(cloudId);
        eipCriteria.andEeStatusNotEqualTo(REMOVED);

        TblCmpSecurityGroupExample sgExample = new TblCmpSecurityGroupExample();
        TblCmpSecurityGroupExample.Criteria sgCriteria = sgExample.createCriteria();
        sgCriteria.andPhaseStatusNotEqualTo(REMOVED);
        sgCriteria.andCloudIdEqualTo(cloudId);
        sgCriteria.andEeStatusNotEqualTo(REMOVED);

        if (userId != null)
        {
            criteria.andEeUserEqualTo(userId);
            eipCriteria.andEeUserEqualTo(userId);
            sgCriteria.andEeUserEqualTo(userId);
        }
        NetSummeryInfo netSummeryInfo = new NetSummeryInfo();
        netSummeryInfo.setNatCount((int)networkRepository.countEipMapByExample(example));
        netSummeryInfo.setEipCount((int)networkRepository.countEipByExample(eipExample));
        netSummeryInfo.setSgCount((int)networkRepository.countSecurityGroupByExample(sgExample));
        return netSummeryInfo;
    }

    public long getVpcCount(String cloudId, String userId)
    {
        TblCmpVpcExample  example = new TblCmpVpcExample();
        TblCmpVpcExample.Criteria criteria = example.createCriteria();
        criteria.andPhaseStatusNotEqualTo((short)REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (userId != null)
        {
            criteria.andEeUserEqualTo(userId);
        }
        return networkRepository.countVpcByExample(example);

    }

    public long getSubnetCount(String cloudId, String userId)
    {
        TblCmpSubnetExample example = new TblCmpSubnetExample();
        TblCmpSubnetExample.Criteria criteria = example.createCriteria();
        criteria.andPhaseStatusNotEqualTo((short)REMOVED);
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (userId != null)
        {
            criteria.andEeUserEqualTo(userId);
        }
        return networkRepository.countSubnetByExample(example);
    }

    public EipInfoVo applyEip(String cloudId, String vpcId, EipApplyReq req, String bpId, String userId) throws WebSystemException
    {
        try
        {
            String poolId = null;
            if (StringUtils.isNotEmpty(vpcId))
            {
                TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);
                if (null == tblCmpVpc || REMOVED == tblCmpVpc.getPhaseStatus())
                {
                    throw new WebSystemException(ErrorCode.VPC_NOT_EXIST,ErrorLevel.INFO);
                }
                poolId = getEipPoolByVlanId(tblCmpVpc.getVlanId());
            }

            TblCmpEipExample example = new TblCmpEipExample();
            TblCmpEipExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            criteria.andEeUserIsNull();
            criteria.andEeBpIsNull();
            criteria.andBoundIdIsNull();
            criteria.andBoundTypeIsNull();
            if (poolId != null)
            {
                criteria.andPoolIdEqualTo(poolId);
            }

            example.setPageSize(3);
            example.setStartRow(0);

            List<TblCmpEip> tblCmpEips = networkRepository.getEips(example);

            if (CollectionUtils.isEmpty(tblCmpEips))
            {
                LOGGER.error("no available eip left");
                throw new WebSystemException(ErrorCode.NO_AVAILABLE_EIP, ErrorLevel.INFO);
            }

            TblCmpEip tblCmpEip = tblCmpEips.get(0);

            tblCmpEip.setEeBp(bpId);
            tblCmpEip.setEeUser(userId);
            tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
            networkRepository.updateEipByPrimaryKey(tblCmpEip);

            return EipInfoVo.builder()
                    .addressType(tblCmpEip.getAddressType() == null ? null : tblCmpEip.getAddressType().intValue())
                    .eipId(tblCmpEip.getEipId())
                    .ipAddress(tblCmpEip.getIpaddr())
                    .eipPoolId(tblCmpEip.getPoolId())
                    .createTime(Utils.formatDate(tblCmpEip.getCreateTime()))
                    .updateTime(Utils.formatDate(tblCmpEip.getUpdateTime()))
                    .build();
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("apply eip failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void releaseEip(String cloudId, String eipId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipId);

            if (tblCmpEip == null)
            {
                LOGGER.error("get eip null: eip id {}", eipId);
                throw new WebSystemException(ErrorCode.EIP_NOT_EXISTS, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpEip.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }
            if (tblCmpEip.getBoundType() != null || tblCmpEip.getBoundId() != null)
            {
                throw new WebSystemException(ErrorCode.INSTANCE_USED_BY_EIP, ErrorLevel.INFO);
            }

            tblCmpEip.setEeBp(null);
            tblCmpEip.setEeUser(null);
            tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));

            tblCmpEip.setChargeType(null);
            tblCmpEip.setPriceUnit(null);
            tblCmpEip.setPeriod(null);
            tblCmpEip.setExpireTime(null);

            networkRepository.updateEipByPrimaryKey(tblCmpEip);
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("release eip failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkEipUser(String cloudId, String eipId, String userId) throws WebSystemException
    {
        TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, eipId);

        if (tblCmpEip == null)
        {
            LOGGER.error("get eip null: eip id {}", eipId);
            throw new WebSystemException(ErrorCode.EIP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpEip.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public void checkSubnetUser(String cloudId, String subnetId, String userId) throws WebSystemException
    {
        TblCmpSubnet tblCmpSubnet = networkRepository.getSubnetById(cloudId, subnetId);

        if (tblCmpSubnet == null)
        {
            LOGGER.error("get subnet null: device id {}", subnetId);
            throw new WebSystemException(ErrorCode.SUBNET_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpSubnet.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public void checkVpcUser(String cloudId, String vpcId, String userId) throws WebSystemException
    {
        TblCmpVpc tblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);

        if (tblCmpVpc == null)
        {
            LOGGER.error("get vpc null: device id {}", vpcId);
            throw new WebSystemException(ErrorCode.BAREMETAL_DEVICE_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpVpc.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public void checkPortMapUser(String cloudId, String eipMapId, String userId) throws WebSystemException
    {
        TblCmpEipMap tblCmpEipMap = networkRepository.getEipMapById(cloudId, eipMapId);

        if (tblCmpEipMap == null)
        {
            LOGGER.error("get eip port map null: eip map id {}", eipMapId);
            throw new WebSystemException(ErrorCode.PORT_MAP_NOT_EXISTS, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpEipMap.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }

    public TblCmpSubnet checkUserAvailableSubnet(String cloudId, String bpId, String userId)
    {
        try
        {
            TblCmpSubnetExample subnetExample = new TblCmpSubnetExample();
            TblCmpSubnetExample.Criteria subnetCriteria = subnetExample.createCriteria();
            subnetCriteria.andCloudIdEqualTo(cloudId);
            subnetCriteria.andEeStatusNotEqualTo(REMOVED);
            subnetCriteria.andEeUserEqualTo(userId);

            List<TblCmpSubnet> tblCmpSubnets = networkRepository.getSubnets(subnetExample);

            if (! CollectionUtils.isEmpty(tblCmpSubnets))
            {
                for (TblCmpSubnet tblCmpSubnet : tblCmpSubnets)
                {
                    if (!tblCmpSubnet.getSubnetId().startsWith("EE_"))
                    {
                        return tblCmpSubnet;
                    }
                }
            }

            TblCmpSubnet resTblCmpSubnet = null;
            String autoCreateVpcName = "VPC-" + RandomStringUtils.randomAlphanumeric(8);
            JsonObject autoCreateVpcObject = JsonParser.parseString("{\"name\":\"" + autoCreateVpcName + "\",\"addressType\":0,\"cidr\":\"10.0.0.0/16\"}").getAsJsonObject();

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId, "/api/network/v1/vpcs", HttpMethod.POST, autoCreateVpcObject, headers);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add vpc error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String vpcId = (String) resultMap.get("vpcId");
                    if (StringUtils.isEmpty(vpcId))
                    {
                        LOGGER.error("add vpc error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpVpc tblCmpVpc = new TblCmpVpc();
                        tblCmpVpc.setVpcId(vpcId);
                        tblCmpVpc.setName(autoCreateVpcName);
                        tblCmpVpc.setCloudId(cloudId);
                        tblCmpVpc.setEeBp(bpId);
                        tblCmpVpc.setEeUser(userId);
                        tblCmpVpc.setEeStatus(SYNCING);
                        tblCmpVpc.setPhaseStatus((short)PhaseStatus.ADDING);
                        try
                        {
                            networkRepository.createVpc(tblCmpVpc);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpVpc updateTblCmpVpc = networkRepository.getVpcById(cloudId, vpcId);
                            updateTblCmpVpc.setEeBp(bpId);
                            updateTblCmpVpc.setEeUser(userId);
                            updateTblCmpVpc.setEeStatus(SYNCING);
                            networkRepository.updateVpc(updateTblCmpVpc);
                        }

                        cloudService.syncSingleData(cloudId, vpcId, SyncMsg.NS_VPC);
                    }

                    String autoCreateSubnetName = "Subnet-" + RandomStringUtils.randomAlphanumeric(8);
                    JsonObject autoCreateSubnetObject = JsonParser.parseString("{\"name\":\"" + autoCreateSubnetName + "\",\"addressType\":0,\"cidr\":\"10.0.0.0/24\",\"vpcId\":\"" + vpcId + "\"}").getAsJsonObject();

                    ResponseEntity addSubnetResponse = cloudService.sendHttpRequestToCloud(cloudId, "/api/network/v1/subnets", HttpMethod.POST, autoCreateSubnetObject, headers);
                    if (addSubnetResponse.getStatusCode() == HttpStatus.CREATED || addSubnetResponse.getStatusCode() == HttpStatus.OK)
                    {
                        Map addSubnetResultMap = (Map) addSubnetResponse.getBody();
                        if (null == addSubnetResultMap)
                        {
                            LOGGER.error("add subnet error");
                            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                        }
                        else
                        {
                            Map body = (Map) addSubnetResultMap.get("body");
                            if (body == null)
                            {
                                LOGGER.error("create subnet error");
                                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                            }
                            String subnetId = (String) body.get("subnetId");
                            if (StringUtils.isEmpty(subnetId))
                            {
                                LOGGER.error("create subnet error");
                                throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                            }
                            else
                            {
                                TblCmpSubnet tblCmpSubnet = new TblCmpSubnet();
                                tblCmpSubnet.setSubnetId(subnetId);
                                tblCmpSubnet.setCloudId(cloudId);
                                tblCmpSubnet.setVpcId(vpcId);
                                tblCmpSubnet.setEeBp(bpId);
                                tblCmpSubnet.setEeUser(userId);
                                tblCmpSubnet.setEeStatus(SYNCING);

                                tblCmpSubnet.setName(autoCreateSubnetName);
                                tblCmpSubnet.setPhaseStatus((short)PhaseStatus.ADDING);
                                try
                                {
                                    networkRepository.createSubnet(tblCmpSubnet);
                                }
                                catch (DuplicateKeyException e)
                                {
                                    TblCmpSubnet updateTblCmpSubnet = networkRepository.getSubnetById(cloudId, subnetId);
                                    updateTblCmpSubnet.setEeBp(bpId);
                                    updateTblCmpSubnet.setEeUser(userId);
                                    updateTblCmpSubnet.setEeStatus(SYNCING);
                                    networkRepository.updateSubnet(updateTblCmpSubnet);
                                }

                                resTblCmpSubnet = tblCmpSubnet;
                            }
                            cloudService.syncSingleData(cloudId, subnetId, SyncMsg.NS_SUBNET);
                        }
                    }
                }
            }
            return resTblCmpSubnet;
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

    public TblCmpEip preAllocateEip(String cloudId, VmInstanceCreateReq addVmInstanceReq, String bpId, String userId)
    {
        try
        {
            if (StringUtils.isNotEmpty(addVmInstanceReq.getEipId()))
            {
                TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, addVmInstanceReq.getEipId());
                if (tblCmpEip == null || StringUtils.isNotEmpty(tblCmpEip.getBoundId()) || StringUtils.isNotEmpty(tblCmpEip.getBoundType()))
                {
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
                }
                else if (!isAdmin() && ((tblCmpEip.getEeBp() == null && bpId != null) || (tblCmpEip.getEeBp() != null && !tblCmpEip.getEeBp().equals(bpId)) ||
                        (tblCmpEip.getEeUser() == null && userId != null) || (tblCmpEip.getEeUser() != null && !tblCmpEip.getEeUser().equals(userId))))
                {
                    throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
                }
            }

            if (addVmInstanceReq.isApplyEip())
            {
                if (StringUtils.isNotEmpty(addVmInstanceReq.getEipId()))
                {
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
                }
                else
                {
                    TblCmpEipExample example = new TblCmpEipExample();
                    TblCmpEipExample.Criteria criteria = example.createCriteria();
                    criteria.andCloudIdEqualTo(cloudId);
                    criteria.andEeStatusNotEqualTo(REMOVED);
                    criteria.andEeUserIsNull();
                    criteria.andEeBpIsNull();
                    criteria.andBoundIdIsNull();
                    criteria.andBoundTypeIsNull();
                    example.setPageSize(3);
                    example.setStartRow(0);
                    List<TblCmpEip> tblCmpEips = networkRepository.getEips(example);
                    if (CollectionUtils.isEmpty(tblCmpEips))
                    {
                        LOGGER.error("no available eip left");
                        throw new WebSystemException(ErrorCode.NO_AVAILABLE_EIP, ErrorLevel.INFO);
                    }
                    TblCmpEip tblCmpEip = tblCmpEips.get(0);
                    tblCmpEip.setEeBp(bpId);
                    tblCmpEip.setEeUser(userId);
                    tblCmpEip.setEeStatus(PRE_ALLOCATION);
                    tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                    networkRepository.updateEipByPrimaryKey(tblCmpEip);

                    addVmInstanceReq.setEipId(tblCmpEip.getEipId());

                    return  tblCmpEip;
                }
            }
            return null;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("pre allocate eip failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public List<TblCmpEip> preAllocateEips(String cloudId, VmInstancesCreateReq addVmInstancesReq, String bpId, String userId)
    {
        try
        {
            VmInstanceCreateReq addVmInstanceReq = addVmInstancesReq.getVmInstanceCreateReq();
            Integer count = addVmInstancesReq.getCount();
            if (StringUtils.isNotEmpty(addVmInstanceReq.getEipId()))
            {
                if (count != 1)
                {
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
                }

                TblCmpEip tblCmpEip = networkRepository.getEipById(cloudId, addVmInstanceReq.getEipId());
                if (tblCmpEip == null || StringUtils.isNotEmpty(tblCmpEip.getBoundId()) || StringUtils.isNotEmpty(tblCmpEip.getBoundType()))
                {
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
                }
                else if ((tblCmpEip.getEeBp() == null && bpId != null) || (tblCmpEip.getEeBp() != null && !tblCmpEip.getEeBp().equals(bpId)) ||
                        (tblCmpEip.getEeUser() == null && userId != null) || (tblCmpEip.getEeUser() != null && !tblCmpEip.getEeUser().equals(userId)))
                {
                    throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
                }
            }

            if (addVmInstanceReq.isApplyEip())
            {
                if (StringUtils.isNotEmpty(addVmInstanceReq.getEipId()))
                {
                    throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
                }
                else
                {
                    TblCmpEipExample example = new TblCmpEipExample();
                    TblCmpEipExample.Criteria criteria = example.createCriteria();
                    criteria.andCloudIdEqualTo(cloudId);
                    criteria.andEeStatusNotEqualTo(REMOVED);
                    criteria.andEeUserIsNull();
                    criteria.andEeBpIsNull();
                    criteria.andBoundIdIsNull();
                    criteria.andBoundTypeIsNull();
                    example.setPageSize(count);
                    example.setStartRow(0);
                    List<TblCmpEip> tblCmpEips = networkRepository.getEips(example);
                    if (CollectionUtils.isEmpty(tblCmpEips) || tblCmpEips.size() != count)
                    {
                        LOGGER.error("no available eip left");
                        throw new WebSystemException(ErrorCode.NO_AVAILABLE_EIP, ErrorLevel.INFO);
                    }

                    tblCmpEips.forEach(tblCmpEip -> {
                        tblCmpEip.setEeBp(bpId);
                        tblCmpEip.setEeUser(userId);
                        tblCmpEip.setEeStatus(PRE_ALLOCATION);
                        tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                        networkRepository.updateEipByPrimaryKey(tblCmpEip);
                    });

                    return tblCmpEips;
                }
            }
            return null;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("pre allocate eips failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void releasePreAllocateEip(TblCmpEip tblCmpEip)
    {
        try
        {
            if (tblCmpEip == null) return;
            tblCmpEip.setEeBp(null);
            tblCmpEip.setEeUser(null);
            tblCmpEip.setEeStatus(SYNCING);
            tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
            networkRepository.updateEipByPrimaryKey(tblCmpEip);
        }
        catch (Exception e)
        {
            LOGGER.error("release pre allocate eip failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void updateEipBoundEeId(TblCmpEip tblCmpEip, String vmInstanceEeId)
    {
        try
        {
            if (tblCmpEip == null) return;
            tblCmpEip.setBoundEeId(vmInstanceEeId);
            tblCmpEip.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
            networkRepository.updateEipByPrimaryKey(tblCmpEip);
        }
        catch (Exception e)
        {
            LOGGER.error("update eip failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }
}
