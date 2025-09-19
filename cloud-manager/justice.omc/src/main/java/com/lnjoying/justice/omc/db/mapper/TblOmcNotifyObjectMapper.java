package com.lnjoying.justice.omc.db.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.omc.db.model.TblOmcNotifyObject;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblOmcNotifyObjectMapper {
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
    int insert(TblOmcNotifyObject record);

    /**
     * insert record to table selective
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcNotifyObject record);

    /**
     * select by primary key
     * @param id primary key
     * @return object by primary key
     */
    TblOmcNotifyObject selectByPrimaryKey(String id);

    /**
     * update record selective
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcNotifyObject record);

    /**
     * update record
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcNotifyObject record);

    int batchInsert(@Param("list")List<TblOmcNotifyObject> list);

    int deleteByNotifyRuleId(@Param("notifyRuleId")String notifyRuleId);

    List<TblOmcNotifyObject> selectByAlarmId(@Param("alarmId")String alarmId);

    List<TblOmcNotifyObject> selectByNotifyRuleId(@Param("notifyRuleId")String notifyRuleId);


}