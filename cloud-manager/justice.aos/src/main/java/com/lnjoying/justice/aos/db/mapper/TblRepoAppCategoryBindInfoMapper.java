package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblRepoAppCategoryBindInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TblRepoAppCategoryBindInfoMapper
{
    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblRepoAppCategoryBindInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblRepoAppCategoryBindInfo record);

    int batchInsert(@Param("list") List<TblRepoAppCategoryBindInfo> record);

    int deleteByRepoId(@Param("repoId")Integer repoId);

    int deleteByRepoIdAndAppId(@Param("repoId")Integer repoId,@Param("appId")String appId);


}