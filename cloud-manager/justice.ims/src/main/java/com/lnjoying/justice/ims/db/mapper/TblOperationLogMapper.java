package com.lnjoying.justice.ims.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.ims.common.ImsResources;
import com.lnjoying.justice.ims.db.model.TblOperationLog;

@MapperModel(ImsResources.OPERATION_LOG)
public interface TblOperationLogMapper
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
    int insert(TblOperationLog record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOperationLog record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOperationLog selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOperationLog record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOperationLog record);
}