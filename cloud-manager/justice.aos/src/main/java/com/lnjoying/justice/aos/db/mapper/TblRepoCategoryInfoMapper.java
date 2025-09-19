package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.db.model.TblRepoAppInfo;
import com.lnjoying.justice.aos.db.model.TblRepoCategoryInfo;
import com.lnjoying.justice.aos.domain.model.helm.CategoryInfo;
import com.lnjoying.justice.aos.domain.model.helm.CategoryStatsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TblRepoCategoryInfoMapper
{
    /**
     * delete by primary key
     *
     * @param categoryId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer categoryId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(TblRepoCategoryInfo record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(TblRepoCategoryInfo record);

    /**
     * select by primary key
     *
     * @param categoryId primary key
     * @return object by primary key
     */
    TblRepoCategoryInfo selectByPrimaryKey(Integer categoryId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(TblRepoCategoryInfo record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(TblRepoCategoryInfo record);

    List<CategoryStatsInfo> selectAllChartStats(@Param("repoNameList")List<String> repoNameList,
                                                @Param("categoryList")List<String> categoryList);

    List<TblRepoCategoryInfo> selectAll(@Param("keyWord")String keyWord);


    List<TblRepoCategoryInfo> selectAllByAppId(@Param("appId")String appId);

    CategoryInfo selectCategoryInfoByPrimaryKey(Integer categoryId);

    TblRepoCategoryInfo selectUncategorizedCategory();


}