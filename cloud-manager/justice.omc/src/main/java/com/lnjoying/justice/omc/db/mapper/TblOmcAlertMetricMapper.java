package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcAlertMetric;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblOmcAlertMetricMapper
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
    int insert(TblOmcAlertMetric record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcAlertMetric record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcAlertMetric selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcAlertMetric record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcAlertMetric record);

    List<TblOmcAlertMetric> selectAll(@Param("groupId") String groupId, @Param("metricName") String metricName);
}