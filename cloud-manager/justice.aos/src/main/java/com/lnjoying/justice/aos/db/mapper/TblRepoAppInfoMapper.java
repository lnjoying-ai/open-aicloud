package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblRepoAppInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TblRepoAppInfoMapper
{
    /**
     * delete by primary key
     *
     * @param appId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(String appId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblRepoAppInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblRepoAppInfo record);

    /**
     * select by primary key
     *
     * @param appId primary key
     * @return object by primary key
     */
    TblRepoAppInfo selectByPrimaryKey(String appId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblRepoAppInfo record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblRepoAppInfo record);

    int batchInsert(@Param("list") List<TblRepoAppInfo> list);

    List<TblRepoAppInfo> selectAllChartList( @Param("repoNameList")List<String> repoNameList, @Param("appName")String appName,
                                            @Param("category")String category);

    List<TblRepoAppInfo> selectAllPrecise(@Param("repoName")String repoName, @Param("appName")String appName);

    int deleteByRepoName(@Param("repoName")String repoName);

    int deleteByRepoNameAndAppName(@Param("repoName")String repoName,@Param("appName")String appName);



}