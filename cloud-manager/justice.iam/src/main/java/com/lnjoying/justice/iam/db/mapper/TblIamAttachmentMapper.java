package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamAttachment;
import com.lnjoying.justice.iam.domain.model.IamPolicyAttachment;
import com.lnjoying.justice.iam.domain.model.IamRoleAttachment;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.Collection;import java.util.List;

@Mapper
public interface TblIamAttachmentMapper
{
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblIamAttachment record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamAttachment record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamAttachment selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamAttachment record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamAttachment record);

    int batchInsert(@Param("list") List<TblIamAttachment> list);

    int batchDelete(@Param("list") Collection<String> list);

    TblIamAttachment selectOne(@Param("projectId") String projectId, @Param("principalId") String principalId, @Param("principalType") Integer principalType,
                               @Param("targetId") String targetId, @Param("targetType") Integer targetType);

    // String policyName, String policyType, String queryBpId, String queryUserId, String groupId, String projectId, Integer pageSize, Integer pageNum
    List<IamPolicyAttachment> selectAllPolicyAttachments(@Param("bpId")String bpId, @Param("userId")String userId, @Param("projectId")String projectId,
                                                         @Param("principalId")String principalId,
                                                         @Param("principalType")Integer principalType, @Param("targetType")Integer targetType,
                                                         @Param("targetId")String targetId, @Param("policyName")String policyName, @Param("policyType")String policyType);

    List<IamRoleAttachment> selectAllRoleAttachments(@Param("bpId")String bpId, @Param("userId")String userId, @Param("projectId")String projectId,
                                                     @Param("principalId")String principalId,
                                                     @Param("principalType")Integer principalType, @Param("targetType")Integer targetType,
                                                     @Param("targetId")String targetId, @Param("roleName")String roleName, @Param("roleType")String roleType);


    List<TblIamAttachment> selectAllIamRoleAttachments(@Param("projectId")String projectId, @Param("principalId")String principalId, @Param("principalType")Integer principalType, @Param(
            "targetType")Integer targetType);



    List<TblIamAttachment> selectByTargetTypeAndTargetIdAndBPId(@Param("targetType")Integer targetType,@Param("targetId")String targetId,@Param("bpId")String bpId);


    int deleteByProjectIdAndPrincipalIdAndPrincipalType(@Param("projectId")String projectId,@Param("principalId")String principalId,@Param("principalType")Integer principalType);

    Integer countByUniqueId(@Param("uniqueId")String uniqueId);



}