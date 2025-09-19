package com.lnjoying.justice.cmp.service.nextstack.vm;

import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroup;
import com.lnjoying.justice.cmp.db.model.TblCmpInstanceGroupExample;
import com.lnjoying.justice.cmp.db.model.TblCmpVmInstanceExample;
import com.lnjoying.justice.cmp.db.repo.VmComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm.InstanceGroupCreateReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.InstanceGroup;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.vm.InstanceGroupsBaseRsp;
import com.lnjoying.justice.cmp.entity.search.nextstack.vm.InstanceGroupSearchCritical;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class InstanceGroupServiceBiz
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private CloudService cloudService;

    @Autowired
    private VmComputeRepository vmComputeRepository;

    public ResponseEntity addInstanceGroup(String cloudId, InstanceGroupCreateReq addInstanceGroupReq, String userId, String bpId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("add instance group error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String instanceGroupId = (String) resultMap.get("instanceGroupId");
                    if (StringUtils.isEmpty(instanceGroupId))
                    {
                        LOGGER.error("add instance group error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpInstanceGroup tblCmpInstanceGroup = new TblCmpInstanceGroup();
                        tblCmpInstanceGroup.setInstanceGroupId(instanceGroupId);
                        tblCmpInstanceGroup.setCloudId(cloudId);
                        tblCmpInstanceGroup.setEeBp(bpId);
                        tblCmpInstanceGroup.setEeUser(userId);
                        tblCmpInstanceGroup.setEeStatus(SYNCING);

                        tblCmpInstanceGroup.setName(addInstanceGroupReq.getName());
                        tblCmpInstanceGroup.setDescription(addInstanceGroupReq.getDescription());
                        try
                        {
                            vmComputeRepository.insertInstanceGroup(tblCmpInstanceGroup);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpInstanceGroup updateTblCmpInstanceGroup = vmComputeRepository.getInstanceGroupById(cloudId, instanceGroupId);
                            updateTblCmpInstanceGroup.setEeBp(bpId);
                            updateTblCmpInstanceGroup.setEeUser(userId);
                            updateTblCmpInstanceGroup.setEeStatus(SYNCING);
                            vmComputeRepository.updateInstanceGroup(updateTblCmpInstanceGroup);
                        }

                        cloudService.syncSingleData(cloudId, instanceGroupId, SyncMsg.NS_INSTANCE_GROUP);
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
            LOGGER.error("add instance group failed, message:{}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public InstanceGroupsBaseRsp getInstanceGroups(String cloudId, InstanceGroupSearchCritical instanceGroupSearchCritical, String userId) throws WebSystemException
    {
        TblCmpInstanceGroupExample example = new TblCmpInstanceGroupExample();
        TblCmpInstanceGroupExample.Criteria criteria = example.createCriteria();
        criteria.andCloudIdEqualTo(cloudId);
        criteria.andEeStatusNotEqualTo(REMOVED);
        if (!StringUtils.isBlank(instanceGroupSearchCritical.getName()))
        {
            criteria.andNameLike("%" + instanceGroupSearchCritical.getName() + "%");
        }
        if (!StringUtils.isBlank(userId))
        {
            criteria.andEeUserEqualTo(userId);
        }
        criteria.andPhaseStatusNotEqualTo(REMOVED);
        long totalNum = vmComputeRepository.countInstanceGroupsByExample(example);
        InstanceGroupsBaseRsp instanceGroupsBaseRsp = new InstanceGroupsBaseRsp();
        instanceGroupsBaseRsp.setTotalNum(totalNum);
        if(totalNum < 1)
        {
            return instanceGroupsBaseRsp;
        }

        example.setOrderByClause("create_time desc,instance_group_id desc");

        int begin = ((instanceGroupSearchCritical.getPageNum() - 1) * instanceGroupSearchCritical.getPageSize());
        example.setStartRow(begin);
        example.setPageSize(instanceGroupSearchCritical.getPageSize());
        List<TblCmpInstanceGroup> tblCmpInstanceGroups = vmComputeRepository.getInstanceGroups(example);
        List<InstanceGroupsBaseRsp.InstanceGroupInfo> instanceGroupInfos = new ArrayList<>();
        for (TblCmpInstanceGroup tblCmpInstanceGroup : tblCmpInstanceGroups)
        {
            InstanceGroupsBaseRsp.InstanceGroupInfo instanceGroupInfo = new InstanceGroupsBaseRsp.InstanceGroupInfo();
            instanceGroupInfo.setInstanceGroupId(tblCmpInstanceGroup.getInstanceGroupId());
            instanceGroupInfo.setName(tblCmpInstanceGroup.getName());
            instanceGroupInfo.setDescription(tblCmpInstanceGroup.getDescription());
            instanceGroupInfo.setCreateTime(tblCmpInstanceGroup.getCreateTime());
            instanceGroupInfos.add(instanceGroupInfo);
            TblCmpVmInstanceExample vmInstanceExample = new TblCmpVmInstanceExample();
            TblCmpVmInstanceExample.Criteria vmInstanceCriteria = vmInstanceExample.createCriteria();
            vmInstanceCriteria.andCloudIdEqualTo(cloudId);
            vmInstanceCriteria.andEeStatusNotEqualTo(REMOVED);
            vmInstanceCriteria.andInstanceGroupIdEqualTo(tblCmpInstanceGroup.getInstanceGroupId());
            vmInstanceCriteria.andPhaseStatusNotEqualTo(REMOVED);
            instanceGroupInfo.setInstanceCount(vmComputeRepository.countVmInstancesByExample(vmInstanceExample));
        }
        instanceGroupsBaseRsp.setInstanceGroupInfos(instanceGroupInfos);

        instanceGroupInfos.forEach(instanceGroupInfo -> cloudService.syncSingleData(cloudId, instanceGroupInfo.getInstanceGroupId(), SyncMsg.NS_INSTANCE_GROUP));

        return instanceGroupsBaseRsp;
    }

    public InstanceGroup getInstanceGroup(String cloudId, String instanceGroupId, String userId)
    {
        TblCmpInstanceGroup tblCmpInstanceGroup = vmComputeRepository.getInstanceGroupById(cloudId, instanceGroupId);
        if (null == tblCmpInstanceGroup || REMOVED == tblCmpInstanceGroup.getPhaseStatus() || REMOVED == tblCmpInstanceGroup.getEeStatus())
        {
            LOGGER.info("instance group not exists, instanceGroupId:{}",instanceGroupId);
            throw new WebSystemException(ErrorCode.INSTANCE_GROUP_NOT_EXIST, ErrorLevel.ERROR);
        }
        if (!StringUtils.isBlank(userId) && !Objects.equals(tblCmpInstanceGroup.getEeUser(), userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }

        InstanceGroup instanceGroup = new InstanceGroup();
        instanceGroup.setInstanceGroupId(tblCmpInstanceGroup.getInstanceGroupId());
        instanceGroup.setName(tblCmpInstanceGroup.getName());
        instanceGroup.setDescription(tblCmpInstanceGroup.getDescription());
        instanceGroup.setPhaseStatus(tblCmpInstanceGroup.getPhaseStatus());
        instanceGroup.setCreateTime(tblCmpInstanceGroup.getCreateTime());
        instanceGroup.setUpdateTime(tblCmpInstanceGroup.getUpdateTime());

        cloudService.syncSingleData(cloudId, instanceGroupId, SyncMsg.NS_INSTANCE_GROUP);

        return instanceGroup;
    }

    public ResponseEntity removeInstanceGroup(String cloudId, String instanceGroupId, String userId)
    {
        try
        {
            TblCmpInstanceGroup tblCmpInstanceGroup = vmComputeRepository.getInstanceGroupById(cloudId, instanceGroupId);

            if (tblCmpInstanceGroup == null)
            {
                LOGGER.error("get instance group null: instance group id {}", instanceGroupId);
                throw new WebSystemException(ErrorCode.INSTANCE_GROUP_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpInstanceGroup.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpInstanceGroup.setEeStatus(REMOVED);
                tblCmpInstanceGroup.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                vmComputeRepository.updateInstanceGroup(tblCmpInstanceGroup);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove instance group failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public void checkInstanceGroupUser(String cloudId, String instanceGroupId, String userId) throws WebSystemException
    {
        TblCmpInstanceGroup tblCmpInstanceGroup = vmComputeRepository.getInstanceGroupById(cloudId, instanceGroupId);

        if (tblCmpInstanceGroup == null)
        {
            LOGGER.error("get instance group null: instance group id {}", instanceGroupId);
            throw new WebSystemException(ErrorCode.INSTANCE_GROUP_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpInstanceGroup.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
