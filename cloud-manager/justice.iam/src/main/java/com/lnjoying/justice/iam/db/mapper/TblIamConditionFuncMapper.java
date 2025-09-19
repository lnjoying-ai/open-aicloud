package com.lnjoying.justice.iam.db.mapper;

import com.lnjoying.justice.iam.db.model.TblIamConditionFunc;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblIamConditionFuncMapper {
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
    int insert(TblIamConditionFunc record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblIamConditionFunc record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    TblIamConditionFunc selectByPrimaryKey(String id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblIamConditionFunc record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblIamConditionFunc record);
}