package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.common.AosResources;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.model.TblStackInfoExample;
import com.lnjoying.justice.commonweb.mapper.MapperModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 12/12/23 5:34 PM
 */
@Mapper
@MapperModel(AosResources.STACK)
public interface TblStackInfoMapper
{
    long countByExample(TblStackInfoExample example);
    
    int deleteByExample(TblStackInfoExample example);
    
    /**
     * delete by primary key
     *
     * @param stackId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String stackId);
    
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblStackInfo record);
    
    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblStackInfo record);
    
    List<TblStackInfo> selectByExample(TblStackInfoExample example);
    
    /**
     * select by primary key
     *
     * @param stackId primary key
     * @return object by primary key
     */
    TblStackInfo selectByPrimaryKey(String stackId);
    
    int updateByExampleSelective(@Param("record") TblStackInfo record, @Param("example") TblStackInfoExample example);
    
    int updateByExample(@Param("record") TblStackInfo record, @Param("example") TblStackInfoExample example);
    
    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblStackInfo record);
    
    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblStackInfo record);
    
    /**
     * @param specId
     * @param name
     * @param bpId
     * @param userId
     * @param statusCollection
     * @param region
     * @param site
     * @param nodeId
     * @param pageSize
     * @param startRow
     * @param notInStatusCollection
     * @return
     */
    List<TblStackInfo> selectAll(@Param("specId") String specId, @Param("name") String name, @Param("bpId") String bpId, @Param("userId") String userId, @Param("statusCollection") List<Integer> statusCollection, @Param("region") String region, @Param("site") String site, @Param("nodeId") String nodeId, @Param("pageSize") int pageSize, @Param("startRow") int startRow, @Param("notInStatusCollection") List<Integer> notInStatusCollection, @Param("stackType") String stackType);
    
    /**
     * count all
     *
     * @param name
     * @param userId
     * @param statusCollection
     * @param region
     * @param site
     * @param nodeId
     * @param notInStatusCollection
     * @return
     */
    int countAll(@Param("name") String name, @Param("bpId") String bpId, @Param("userId") String userId, @Param("statusCollection") List<Integer> statusCollection, @Param("region") String region, @Param("site") String site, @Param("nodeId") String nodeId, @Param("notInStatusCollection") List<Integer> notInStatusCollection, @Param("stackType") String stackType);
}