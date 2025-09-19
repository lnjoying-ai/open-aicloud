package com.lnjoying.justice.omc.db.mapper;
import java.util.List;

import com.lnjoying.justice.omc.db.model.TblOmcAlertLogProcess;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;

@Mapper
public interface TblOmcAlertLogProcessMapper
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
    int insert(TblOmcAlertLogProcess record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcAlertLogProcess record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcAlertLogProcess selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcAlertLogProcess record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcAlertLogProcess record);


    List<TblOmcAlertLogProcess> selectByAlertLogId(@Param("alertLogId")String alertLogId);


}