package com.lnjoying.justice.iam.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.iam.db.mapper.TblIamAttachmentMapper;
import com.lnjoying.justice.iam.db.mapper.TblIamPolicyMapper;
import com.lnjoying.justice.iam.db.model.TblIamAttachment;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentsRsp;
import com.lnjoying.justice.iam.domain.dto.response.authz.RoleAttachmentsRsp;
import com.lnjoying.justice.iam.domain.model.IamPolicyAttachment;
import com.lnjoying.justice.iam.domain.model.IamRoleAttachment;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 19:25
 */

@Slf4j
@Repository
public class AuthzRepository
{

    @Autowired
    private TblIamAttachmentMapper attachmentMapper;

    @Autowired
    private TblIamPolicyMapper policyMapper;


    public int insertSelective(TblIamAttachment record)
    {
        return attachmentMapper.insertSelective(record);
    }


    public int batchInsert(List<TblIamAttachment> list)
    {

        if (!CollectionUtils.isEmpty(list))
        {
            int total = list.size();
            AtomicInteger failCount = new AtomicInteger(0);
            AtomicInteger successCount = new AtomicInteger(0);
            list.parallelStream().forEach(attachment -> {
                try
                {
                    String targetId = attachment.getTargetId();
                    TblIamAttachment tblIamAttachment = attachmentMapper.selectOne(attachment.getProjectId(), attachment.getPrincipalId(), attachment.getPrincipalType(), targetId,
                            attachment.getTargetType());
                    if (Objects.isNull(tblIamAttachment))
                    {
                        attachmentMapper.insertSelective(attachment);
                        if (TblIamAttachment.TargetType.POLICY.value() == attachment.getTargetType())
                        {
                            TblIamPolicy tblIamPolicy = policyMapper.selectByPrimaryKey(targetId);
                            if (Objects.nonNull(tblIamPolicy))
                            {
                                tblIamPolicy.setAttachable(true);
                                Integer attachmentCount = tblIamPolicy.getAttachmentCount();
                                int count = Objects.nonNull(attachmentCount) ? attachmentCount.intValue() : 0;
                                tblIamPolicy.setAttachmentCount(count + 1);
                                policyMapper.updateByPrimaryKeySelective(tblIamPolicy);
                            }
                        }
                    }
                    successCount.incrementAndGet();
                }
                catch (Exception e)
                {
                    failCount.incrementAndGet();
                    log.error("insert attachment error:{}", e);
                }

            });
            log.info("written attachment to db, total:{}, success:{}, fail:{}", total, successCount.get(), failCount.get());
            return successCount.get();
        }
        //return attachmentMapper.batchInsert(list);
        return 0;
    }


    public TblIamAttachment selectOne(String projectId, String principalId, Integer principalType, String targetId, Integer targetType)
    {
        return attachmentMapper.selectOne(projectId, principalId, principalType,targetId, targetType);
    }

    public int deleteByPrimaryKey(String id)
    {
        return attachmentMapper.deleteByPrimaryKey(id);
    }

    public AttachmentsRsp getAttachmentList(String policyName, String policyType, String queryBpId, String queryUserId, String groupId, String roleId, String projectId,
                                            Integer pageSize, Integer pageNum)
    {

        Integer principalType = null;
        String principalId = "";
        if (StringUtils.isNotBlank(queryUserId))
        {
            principalType = TblIamAttachment.PrincipalType.USER.value();
            principalId = queryUserId;
        }
        else if (StringUtils.isNotBlank(groupId))
        {
            principalType = TblIamAttachment.PrincipalType.GROUP.value();
            principalId = groupId;
        }
        else if (StringUtils.isNotBlank(roleId))
        {
            principalType = TblIamAttachment.PrincipalType.ROLE.value();
            principalId = roleId;
        }

        PageHelper.startPage(pageNum, pageSize);
        List<IamPolicyAttachment> iamPolicyAttachments = attachmentMapper.selectAllPolicyAttachments(queryBpId, null, projectId, principalId, principalType,
                TblIamAttachment.TargetType.POLICY.value(),
                null, policyName, policyType);
        PageInfo<IamPolicyAttachment> pageInfo = new PageInfo<>(iamPolicyAttachments);
        return AttachmentsRsp.builder().totalNum(pageInfo.getTotal())
                .attachments(iamPolicyAttachments).build();

    }


    public RoleAttachmentsRsp getRoleAttachmentList(String roleName, String roleType, String queryBpId, String queryUserId, String groupId, String projectId, Integer pageSize, Integer pageNum)
    {

        Integer principalType = null;
        String principalId = "";
        if (StringUtils.isNotBlank(queryUserId))
        {
            principalType = TblIamAttachment.PrincipalType.USER.value();
            principalId = queryUserId;
        }
        else if (StringUtils.isNotBlank(groupId))
        {
            principalType = TblIamAttachment.PrincipalType.GROUP.value();
            principalId = groupId;
        }

        PageHelper.startPage(pageNum, pageSize);
        List<IamRoleAttachment> roleAttachments = attachmentMapper.selectAllRoleAttachments(queryBpId, null, projectId, principalId, principalType,
                TblIamAttachment.TargetType.ROLE.value(), null, roleName, roleType);
        PageInfo<IamRoleAttachment> pageInfo = new PageInfo<>(roleAttachments);
        return RoleAttachmentsRsp.builder().totalNum(pageInfo.getTotal())
                .attachments(roleAttachments).build();
    }


    /**
     * get role ids
     * @param projectId
     * @param principalType
     * @param principalId
     * @return
     */
    public List<Long> selectAllRoleIdsByAttachments(String projectId, Integer principalType, String principalId)
    {
        List<TblIamAttachment> attachments = attachmentMapper.selectAllIamRoleAttachments(projectId, principalId, principalType, TblIamAttachment.TargetType.ROLE.value());
        if (!CollectionUtils.isEmpty(attachments))
        {
            return attachments.stream().map(attachment -> Long.parseLong(attachment.getTargetId())).collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    public List<TblIamAttachment> selectAllRoleAttachments(String projectId, Integer principalType, String principalId)
    {
        List<TblIamAttachment> attachments = attachmentMapper.selectAllIamRoleAttachments(projectId, principalId, principalType, TblIamAttachment.TargetType.ROLE.value());
        if (!CollectionUtils.isEmpty(attachments))
        {
            return attachments;
        }

        return Collections.emptyList();
    }

    public List<TblIamAttachment> selectByPolicyIdAndBPId(String policyId, String bpId)
    {
        return attachmentMapper.selectByTargetTypeAndTargetIdAndBPId(TblIamAttachment.TargetType.POLICY.value(), policyId, bpId);
    }

    public void batchDeleteAssignments(List<TblIamAttachment> attachments)
    {
        if (!CollectionUtils.isEmpty(attachments))
        {
            attachments.stream().parallel().forEach(tblIamAssignment -> {
                if (StringUtils.isNotBlank(tblIamAssignment.getProjectId()))
                {
                    attachmentMapper.deleteByProjectIdAndPrincipalIdAndPrincipalType(tblIamAssignment.getProjectId(), tblIamAssignment.getPrincipalId(), tblIamAssignment.getPrincipalType());
                }
            });
        }
    }

    public Integer countByUniqueId(String principalId)
    {
        return attachmentMapper.countByUniqueId(principalId);
    }
}
