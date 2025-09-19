package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcMonitorTask;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblOmcMonitorTaskMapper
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
    int insert(TblOmcMonitorTask record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcMonitorTask record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcMonitorTask selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcMonitorTask record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcMonitorTask record);

    List<TblOmcMonitorTask> selectAllByDeploymentStatus(@Param("deploymentStatus") Integer deploymentStatus);

    List<String> selectIdByDeploymentStatus(@Param("deploymentStatus") Integer deploymentStatus);
}