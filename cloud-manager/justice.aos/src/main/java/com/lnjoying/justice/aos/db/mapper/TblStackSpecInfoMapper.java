package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.common.AosResources;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfoExample;
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
@MapperModel(AosResources.STACK_SPEC)
public interface TblStackSpecInfoMapper
{
    long countByExample(TblStackSpecInfoExample example);
    
    int deleteByExample(TblStackSpecInfoExample example);
    
    /**
     * delete by primary key
     *
     * @param specId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String specId);
    
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblStackSpecInfo record);
    
    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblStackSpecInfo record);
    
    List<TblStackSpecInfo> selectByExample(TblStackSpecInfoExample example);
    
    /**
     * select by primary key
     *
     * @param specId primary key
     * @return object by primary key
     */
    TblStackSpecInfo selectByPrimaryKey(String specId);
    
    int updateByExampleSelective(@Param("record") TblStackSpecInfo record, @Param("example") TblStackSpecInfoExample example);
    
    int updateByExample(@Param("record") TblStackSpecInfo record, @Param("example") TblStackSpecInfoExample example);
    
    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblStackSpecInfo record);
    
    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblStackSpecInfo record);
    
    long countAlwaysOnline();
    
    List<TblStackSpecInfo> selectAlwaysOnline(@Param("startRow") int startRow, @Param("pageSize") int pageSize);
}