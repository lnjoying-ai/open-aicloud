package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblRepoInfo;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface TblRepoInfoMapper
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
    int insert(TblRepoInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblRepoInfo record);

    /**
     * select by primary key
     *
     * @param id primary key
     * @return object by primary key
     */
    TblRepoInfo selectByPrimaryKey(Integer id);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblRepoInfo record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblRepoInfo record);

    List<TblRepoInfo> selectByRepoName(@Param("repoName") String repoName);

    int deleteByRepoName(@Param("repoName") String repoName);

    List<TblRepoInfo> selectAll(@Param("repoName") String repoName, @Param("status") Integer status);

    List<TblRepoInfo> selectAllByStatus(@Param("list") List<Integer> list);
}