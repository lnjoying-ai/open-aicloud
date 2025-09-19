package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblRepoAppVersionInfo;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblRepoAppVersionInfoMapper
{
    /**
     * delete by primary key
     *
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblRepoAppVersionInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblRepoAppVersionInfo record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblRepoAppVersionInfo selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblRepoAppVersionInfo record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblRepoAppVersionInfo record);

    List<TblRepoAppVersionInfo> selectByAppId(@Param("appId") String appId);

    int batchInsert(@Param("list") List<TblRepoAppVersionInfo> list);

    TblRepoAppVersionInfo selectByAppIdAndVersion(@Param("appId")String appId,@Param("version")String version);

    int deleteByRepoName(@Param("repoName")String repoName);

    List<TblRepoAppVersionInfo> selectByRepoNameAndAppNameAndVersion(@Param("repoName")String repoName, @Param("appName")String appName, @Param("version")String version);

    int deleteByRepoNameAndAppNameAndVersion(@Param("repoName")String repoName,@Param("appName")String appName,@Param("version")String version);


}