package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.commonweb.mapper.MapperModel;
import com.lnjoying.justice.omc.common.OmcResources;
import com.lnjoying.justice.omc.db.model.TblOmcAlarm;
import com.lnjoying.justice.omc.domain.model.Alarm;import com.lnjoying.justice.omc.domain.model.AlarmDetail;import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
@MapperModel(OmcResources.ALARM)
public interface TblOmcAlarmMapper
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
    int insert(TblOmcAlarm record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcAlarm record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcAlarm selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcAlarm record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcAlarm record);


    List<Alarm> selectAll(@Param("alertGroupId") String alertGroupId, @Param("bpId") String bpId, @Param("userId") String userId, @Param("name") String name,
                          @Param("ruleType") Integer ruleType);

    AlarmDetail selectById(@Param("id") String id);
}