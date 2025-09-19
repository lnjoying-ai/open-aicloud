package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblStackTemplateBaseInfo;
import com.lnjoying.justice.aos.db.model.TblStackTemplateBaseInfoExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 12/13/23 9:53 AM
 */
@Mapper
public interface TblStackTemplateBaseInfoMapper
{
    long countByExample(TblStackTemplateBaseInfoExample example);

    int deleteByExample(TblStackTemplateBaseInfoExample example);

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
    int insert(TblStackTemplateBaseInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblStackTemplateBaseInfo record);

    List<TblStackTemplateBaseInfo> selectByExample(TblStackTemplateBaseInfoExample example);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblStackTemplateBaseInfo selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TblStackTemplateBaseInfo record, @Param("example") TblStackTemplateBaseInfoExample example);

    int updateByExample(@Param("record") TblStackTemplateBaseInfo record, @Param("example") TblStackTemplateBaseInfoExample example);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblStackTemplateBaseInfo record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblStackTemplateBaseInfo record);

    /**
     * count by conditions
     *
     * @param name
     * @param bpId
     * @param userId
     * @return
     */
    Integer countByNameAndBpIdAndUserId(@Param("name") String name, @Param("bpId") String bpId, @Param("userId") String userId);

    /**
     * query by conditions
     *
     * @param name
     * @param bpId
     * @param userId
     * @return
     */
    List<TblStackTemplateBaseInfo> selectAllByNameAndBpIdAndUserId(@Param("id") String id, @Param("name") String name, @Param("bpId") String bpId, @Param("userId") String userId, @Param("containsPublic") boolean containsPublic, @Param("pageSize") int pageSize, @Param("startRow") int startRow);

    List<TblStackTemplateBaseInfo> selectAllByName(@Param("name") String name);
}