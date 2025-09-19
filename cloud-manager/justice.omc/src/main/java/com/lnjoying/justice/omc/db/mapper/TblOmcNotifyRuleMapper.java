package com.lnjoying.justice.omc.db.mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.lnjoying.justice.omc.db.model.TblOmcNotifyRule;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TblOmcNotifyRuleMapper
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
    int insert(TblOmcNotifyRule record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcNotifyRule record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcNotifyRule selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcNotifyRule record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcNotifyRule record);

    List<TblOmcNotifyRule> selectAllByAlarmId(@Param("alarmId")String alarmId);

    List<String> selectIdsByAlarmId(@Param("alarmId")String alarmId);


    List<String> selectReceiversByAlarmId(@Param("alarmId")String alarmId);

}