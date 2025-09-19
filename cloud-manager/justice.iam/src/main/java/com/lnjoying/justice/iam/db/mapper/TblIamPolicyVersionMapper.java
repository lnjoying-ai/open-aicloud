package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamPolicyVersion;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
@MapperModel(IamResources.POLICY_VERSION)
public interface TblIamPolicyVersionMapper
{
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblIamPolicyVersion record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamPolicyVersion record);

    List<TblIamPolicyVersion> selectByPolicyId(@Param("policyId") String policyId);

    List<TblIamPolicyVersion> selectByPolicyIdAndVersionId(@Param("policyId")String policyId,@Param("versionId")String versionId);

	int deleteByPolicyIdAndVersionId(@Param("policyId")String policyId,@Param("versionId")String versionId);

    Integer countByPolicyIdAndVersionId(@Param("policyId")String policyId,@Param("versionId")String versionId);

    String selectDocumentByMap(@Param("policyId")String policyId,@Param("versionId")String versionId);

    String selectLatestVersionIdByPolicyId(@Param("policyId")String policyId);

    int updateByPolicyIdAndVersionId(@Param("updated")TblIamPolicyVersion updated,@Param("policyId")String policyId,@Param("versionId")String versionId);

    Integer countByPolicyId(@Param("policyId")String policyId);



}