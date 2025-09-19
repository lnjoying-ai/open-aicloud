package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.lnjoying.justice.cmp.common.CommonPhaseStatus;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.TblCmpNfs;
import com.lnjoying.justice.cmp.db.model.TblCmpNfsExample;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.NfsCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.network.RpcTenantNetworkPort;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.NfsInfoRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.NfsInfosRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.NfsSearchCritical;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.rpc.RpcNetworkServiceImpl;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class NfsServiceBiz
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VmSnapServiceBiz.class);

    @Autowired
    private CloudService cloudService;

    @Autowired
    private UserService userService;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    @Autowired
    private RpcNetworkServiceImpl rpcNetworkService;

    public ResponseEntity createNfs(String cloudId, NfsCreateReq req, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("create nfs error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String nfsId = (String) resultMap.get("nfsId");
                    if (StringUtils.isEmpty(nfsId))
                    {
                        LOGGER.error("create nfs error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpNfs tblCmpNfs = new TblCmpNfs();
                        tblCmpNfs.setNfsId(nfsId);
                        tblCmpNfs.setCloudId(cloudId);
                        tblCmpNfs.setEeBp(bpId);
                        tblCmpNfs.setEeUser(userId);
                        tblCmpNfs.setEeStatus(SYNCING);

                        tblCmpNfs.setName(req.getName());
                        tblCmpNfs.setDescription(req.getDescription());
                        try
                        {
                            vmComputeRepository.insertNfs(tblCmpNfs);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpNfs updateTblCmpNfs = vmComputeRepository.getNfsById(cloudId, nfsId);
                            updateTblCmpNfs.setEeBp(bpId);
                            updateTblCmpNfs.setEeUser(userId);
                            updateTblCmpNfs.setEeStatus(SYNCING);
                            vmComputeRepository.updateNfs(updateTblCmpNfs);
                        }

                        cloudService.syncSingleData(cloudId, nfsId, SyncMsg.NS_NFS_SERVER);
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
            LOGGER.error("create nfs failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public ResponseEntity removeNfs(String cloudId, String nfsId, String userId)
    {
        try
        {
            TblCmpNfs tblCmpNfs = vmComputeRepository.getNfsById(cloudId, nfsId);

            if (tblCmpNfs == null)
            {
                LOGGER.error("get nfs null: nfs id {}", nfsId);
                throw new WebSystemException(ErrorCode.NFS_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpNfs.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpNfs.setEeStatus(CommonPhaseStatus.REMOVED);
                tblCmpNfs.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                vmComputeRepository.updateNfs(tblCmpNfs);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove flavor failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public NfsInfoRsp getNfs(String cloudId, String nfsId, String userId) throws WebSystemException
    {
        TblCmpNfs tblNfs = vmComputeRepository.getNfsById(cloudId, nfsId);
        if (tblNfs == null || REMOVED == tblNfs.getPhaseStatus() || REMOVED == tblNfs.getEeStatus())
        {
            LOGGER.error("get nfs failed, nfsId:{}", nfsId);
            throw new WebSystemException(ErrorCode.NFS_NOT_EXIST, ErrorLevel.ERROR);
        }
        if (!StringUtils.isBlank(userId) && !userId.equals(tblNfs.getEeUser()))
        {
            LOGGER.error("get nfs failed, nfsId:{}, userId:{}", nfsId, userId);
            throw new WebSystemException(ErrorCode.InvalidAuthority, ErrorLevel.ERROR);
        }

        NfsInfoRsp rsp = new NfsInfoRsp();
        rsp.setNfsInfo(tblNfs);

        rsp.setEeBpName(userService.getBpName(tblNfs.getEeBp()));
        rsp.setEeUserName(userService.getUserName(tblNfs.getEeUser()));

        RpcTenantNetworkPort port = rpcNetworkService.getTenantNetworkPort(cloudId, tblNfs.getPortId());
        if (null != port)
        {
            if (StringUtils.isNotBlank(port.getSubnetCidr())) rsp.setSubnetCidr(port.getSubnetCidr());
            if (StringUtils.isNotBlank(port.getVpcCidr())) rsp.setVpcCidr(port.getVpcCidr());
            if (StringUtils.isNotBlank(port.getIpAddress())) rsp.setServicePath(port.getIpAddress() + ":/");
        }
        return rsp;
    }

    public NfsInfosRsp listNfs(String cloudId, NfsSearchCritical nfsSearchCritical, String userId) throws WebSystemException
    {
        TblCmpNfsExample example = new TblCmpNfsExample();
        TblCmpNfsExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(nfsSearchCritical.getName()))
        {
            criteria.andNameLike("%" + nfsSearchCritical.getName() + "%");
        }
        if (!StringUtils.isBlank(userId))
        {
            criteria.andEeUserEqualTo(userId);
        }

        NfsInfosRsp rsp = new NfsInfosRsp();

        long totalNum = vmComputeRepository.countNfsServersByExample(example);

        rsp.setTotalNum(totalNum);

        if (totalNum < 1)
        {
            return rsp;
        }

        int begin = ((nfsSearchCritical.getPageNum() - 1) * nfsSearchCritical.getPageSize());
        example.setOrderByClause("create_time desc, nfs_id desc");

        example.setStartRow(begin);
        example.setPageSize(nfsSearchCritical.getPageSize());

        List<TblCmpNfs> tblCmpNfsList = vmComputeRepository.getNfsServers(example);

        List<NfsInfoRsp> nfsInfoList = new ArrayList<>();

        for (TblCmpNfs tblNfs : tblCmpNfsList)
        {
            NfsInfoRsp nfsRsp = new NfsInfoRsp();
            nfsRsp.setNfsInfo(tblNfs);

            nfsRsp.setEeBpName(userService.getBpName(tblNfs.getEeBp()));
            nfsRsp.setEeUserName(userService.getUserName(tblNfs.getEeUser()));

            RpcTenantNetworkPort port = rpcNetworkService.getTenantNetworkPort(cloudId, tblNfs.getPortId());
            if (null != port)
            {
                if (StringUtils.isNotBlank(port.getSubnetCidr())) nfsRsp.setSubnetCidr(port.getSubnetCidr());
                if (StringUtils.isNotBlank(port.getVpcCidr())) nfsRsp.setVpcCidr(port.getVpcCidr());
                if (StringUtils.isNotBlank(port.getIpAddress())) nfsRsp.setServicePath(port.getIpAddress() + ":/");
                if (StringUtils.isNotBlank(port.getSubnetName())) nfsRsp.setSubnetName(port.getSubnetName());
                if (StringUtils.isNotBlank(port.getVpcName())) nfsRsp.setVpcName(port.getVpcName());
            }
            nfsInfoList.add(nfsRsp);

            cloudService.syncSingleData(cloudId, tblNfs.getNfsId(), SyncMsg.NS_NFS_SERVER);
        }
        rsp.setNfsInfos(nfsInfoList);
        return rsp;
    }

    public void checkNfsUser(String cloudId, String nfsId, String userId) throws WebSystemException
    {
        TblCmpNfs tblCmpNfs = vmComputeRepository.getNfsById(cloudId, nfsId);

        if (tblCmpNfs == null)
        {
            LOGGER.error("get nfs null: nfs id {}", nfsId);
            throw new WebSystemException(ErrorCode.NFS_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpNfs.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
