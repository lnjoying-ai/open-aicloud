package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcAlertSendLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblOmcAlertSendLogMapper {
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
    int insert(TblOmcAlertSendLog record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcAlertSendLog record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    TblOmcAlertSendLog selectByPrimaryKey(String id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcAlertSendLog record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcAlertSendLog record);
}