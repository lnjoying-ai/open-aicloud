package com.lnjoying.justice.iam.db.mapper;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceDetail;
import com.lnjoying.justice.iam.domain.dto.response.resource.TblIamCommonResourceSummary;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.iam.db.model.TblIamCommonResource;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblIamCommonResourceMapper {
    /**
     * delete by primary key
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String id);

    /**
     * insert record to table
     * @param record the record
     * @return insert count
     */
    int insert(TblIamCommonResource record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamCommonResource record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    TblIamCommonResource selectByPrimaryKey(String id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamCommonResource record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamCommonResource record);

    List<TblIamCommonResourceSummary> selectAllByName(@Param("name")String name);

    TblIamCommonResourceDetail selectById(@Param("id")String id);

    TblIamCommonResource selectByServiceIdAndName(@Param("serviceId")String serviceId,@Param("name")String name);

    List<TblIamCommonResourceSummary> selectAll();


}