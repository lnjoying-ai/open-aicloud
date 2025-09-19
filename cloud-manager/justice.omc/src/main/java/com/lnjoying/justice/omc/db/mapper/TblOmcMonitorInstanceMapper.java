package com.lnjoying.justice.omc.db.mapper;

import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblOmcMonitorInstanceMapper
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
    int insert(TblOmcMonitorInstance record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblOmcMonitorInstance record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblOmcMonitorInstance selectByPrimaryKey(String id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblOmcMonitorInstance record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblOmcMonitorInstance record);

    int batchInsert(@Param("list") List<TblOmcMonitorInstance> list);

    List<TblOmcMonitorInstance> selectAll(@Param("bpId") String bpId, @Param("userId") String userId, @Param("id") String id, @Param("status") Integer status,
                                          @Param("taskType") Integer taskType);

    List<String> selectIdByStatusAndTaskType(@Param("status") Integer status, @Param("taskType") Integer taskType);

    TblOmcMonitorInstance selectForUpdate(String id);
}