package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamResourceAttr;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblIamResourceAttrMapper
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
    int insert(TblIamResourceAttr record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamResourceAttr record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblIamResourceAttr selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamResourceAttr record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamResourceAttr record);

    List<TblIamResourceAttr> selectByResourceId(@Param("resourceId") String resourceId);

    Integer countByResourceId(@Param("resourceId") String resourceId);

    int deleteByResourceId(@Param("resourceId") String resourceId);

    int batchInsert(@Param("list") List<TblIamResourceAttr> list);
}