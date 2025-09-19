package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcAlertLog;
import com.lnjoying.justice.omc.domain.model.DailyAlertLog;import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.Date;import java.util.List;

@Mapper
public interface TblOmcAlertLogMapper
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
    int insert(TblOmcAlertLog record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcAlertLog record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcAlertLog selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcAlertLog record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcAlertLog record);

    List<TblOmcAlertLog> selectAll(@Param("bpId") String bpId, @Param("userId") String userId, @Param("alertGroupId") String alertGroupId, @Param("alertStatus") Integer alertStatus,
                                   @Param("processStatus") String processStatus, @Param(
            "level") Integer level, @Param(
            "resourceTypeId") String resourceTypeId,
                                   @Param("resourceId") String resourceId);

    List<TblOmcAlertLog> selectAllByAlertStatus(@Param("alertStatus") Integer alertStatus);

    List<String> selectIdByAlertStatus(@Param("alertStatus") Integer alertStatus);

    int selectAllByCreateTimeBetweenEqualAndLevel(@Param("minCreateTime") Date minCreateTime, @Param("maxCreateTime") Date maxCreateTime, @Param("level") Integer level, @Param("bpId") String bpId, @Param("userId") String userId);

    List<DailyAlertLog> selectByCreateTimeBetweenEqualAndLevel(@Param("minCreateTime") Date minCreateTime, @Param("maxCreateTime") Date maxCreateTime, @Param("level") Integer level
            , @Param("bpId") String bpId, @Param("userId") String userId);

    TblOmcAlertLog selectOneByUniqueHash(@Param("uniqueHash") Integer uniqueHash);
}