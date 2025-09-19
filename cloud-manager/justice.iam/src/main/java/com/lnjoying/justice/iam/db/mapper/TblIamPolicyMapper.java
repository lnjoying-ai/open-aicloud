package com.lnjoying.justice.iam.db.mapper;
import java.util.Collection;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.iam.common.constant.IamResources;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import com.lnjoying.justice.iam.domain.model.IamPolicyDocument;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
@MapperModel(IamResources.POLICY)
public interface TblIamPolicyMapper
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
    int insert(TblIamPolicy record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamPolicy record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamPolicy selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamPolicy record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamPolicy record);

    List<TblIamPolicyDetail> selectAll(@Param("name") String name, @Param("bpId") String bpId, @Param("policyType") Integer policyType, @Param("autogen") Boolean autogen);

    Integer countByNameAndBpId(@Param("name") String name, @Param("bpId") String bpId);

    Integer countByIdAndBpId(@Param("id") String id, @Param("bpId") String bpId);

    TblIamPolicyDetail selectByIdAndBpId(@Param("id") String id, @Param("bpId") String bpId);

    List<IamPolicyDocument> selectByBase(@Param("base")Boolean base);

    List<TblIamPolicy> selectByNameAndBase(@Param("name")String name,@Param("base")Boolean base);

    Integer countByIdIn(@Param("idCollection")Collection<String> idCollection);

    String selectLatestNameByName(@Param("name")String name);


}