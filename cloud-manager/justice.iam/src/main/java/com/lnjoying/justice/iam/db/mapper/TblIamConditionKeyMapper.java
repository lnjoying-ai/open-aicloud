package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamConditionKey;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblIamConditionKeyMapper
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
    int insert(TblIamConditionKey record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamConditionKey record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamConditionKey selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamConditionKey record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamConditionKey record);

    List<TblIamConditionKey> selectAllByServiceId(@Param("serviceId") String serviceId);

    List<TblIamConditionKey> selectAllByResourceId(@Param("resourceId")String resourceId);

    Integer countByResourceId(@Param("resourceId")String resourceId);


}