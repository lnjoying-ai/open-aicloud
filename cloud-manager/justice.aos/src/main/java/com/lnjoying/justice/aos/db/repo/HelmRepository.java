package com.lnjoying.justice.aos.db.repo;

import com.lnjoying.justice.aos.db.mapper.*;
import com.lnjoying.justice.aos.db.model.*;
import com.lnjoying.justice.aos.domain.model.helm.CategoryStatsInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 15:24
 */

@Repository
public class HelmRepository
{
    @Autowired
    private TblRepoInfoMapper repoInfoMapper;

    @Autowired
    private TblRepoAppInfoMapper repoAppInfoMapper;

    @Autowired
    private TblRepoCategoryInfoMapper categoryInfoMapper;

    @Autowired
    private TblRepoAppVersionInfoMapper appVersionInfoMapper;

    @Autowired
    private TblHelmStackInfoMapper stackInfoMapper;

    @Autowired
    private TblRepoAppCategoryBindInfoMapper bindingInfoMapper;

    public List<TblRepoInfo> selectByRepoName(String repoName)
    {
        Assert.hasText(repoName, "The given repo name must not be null!");
        return repoInfoMapper.selectByRepoName(repoName);
    }

    public int insertSelective(TblRepoInfo record)
    {
        return repoInfoMapper.insert(record);
    }

    public int deleteByRepoName(String repoName)
    {
        Assert.hasText(repoName, "The given repo name must not be null!");
        return repoInfoMapper.deleteByRepoName(repoName);
    }


    public List<TblRepoInfo> selectAll(String repoName, Integer status)
    {
        return repoInfoMapper.selectAll(repoName, status);
    }

    public List<TblRepoInfo> selectAllByStatus(List<Integer> statusList)
    {
        return repoInfoMapper.selectAllByStatus(statusList);
    }

    public int updateByPrimaryKeySelective(TblRepoInfo record)
    {
        return repoInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int insertAppInfoSelective(TblRepoAppInfo record)
    {
       return repoAppInfoMapper.insertSelective(record);
    }

    public int updateAppInfoByPrimaryKeySelective(TblRepoAppInfo record)
    {
        return repoAppInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int batchAppInfoInsert(List<TblRepoAppInfo> list)
    {
       return repoAppInfoMapper.batchInsert(list);
    }

    public int batchAppVersionInfoInsert(List<TblRepoAppVersionInfo> list)
    {
        return appVersionInfoMapper.batchInsert(list);
    }

    public List<TblRepoAppInfo> selectAllChartList(List<String> repoNameList, String appName, String category)
    {
        return repoAppInfoMapper.selectAllChartList(repoNameList, appName, category);
    }

    public List<TblRepoAppInfo> selectAppInfosAllPrecise(String repoName, String appName)
    {
        return repoAppInfoMapper.selectAllPrecise(repoName, appName);
    }

    public TblRepoAppInfo selectAppInfoByPrimaryKey(String appId)
    {
        return repoAppInfoMapper.selectByPrimaryKey(appId);
    }

    public List<CategoryStatsInfo> selectAllChartStats(List<String> repoNameList, List<String> categoryList)
    {
        return categoryInfoMapper.selectAllChartStats(repoNameList, categoryList);
    }

    public List<TblRepoAppVersionInfo> selectAppVersionsByAppId(String appId)
    {
        return appVersionInfoMapper.selectByAppId(appId);
    }

    public List<TblHelmStackInfo> selectStackByChartName(String chartName)
    {
        return stackInfoMapper.selectStackByChartName(chartName);
    }

    public int insertStackInfoSelective(TblHelmStackInfo record)
    {
        return stackInfoMapper.insertSelective(record);
    }

    public List<TblHelmStackInfo> selectAllStackList(String stackName, List<String> clusterIdList, String category, String bpId, String userId)
    {
       return stackInfoMapper.selectAllStacks(stackName, clusterIdList, category, bpId, userId);
    }

    public int updateStackByPrimaryKeySelective(TblHelmStackInfo record)
    {
        return stackInfoMapper.updateByPrimaryKeySelective(record);
    }

    public List<TblHelmStackInfo> selectStacksAllByReleaseName(String releaseName)
    {
        return stackInfoMapper.selectAllByReleaseName(releaseName);
    }

    public TblHelmStackInfo selectStackInfoByPrimaryKey(String stackId)
    {
        return stackInfoMapper.selectByPrimaryKey(stackId);
    }

    public List<TblRepoCategoryInfo> selectAllCategoryList(String keyWord)
    {
        return categoryInfoMapper.selectAll(keyWord);
    }

    public int batchRepoAppCategoryBindInfoInsert(@Param("list") List<TblRepoAppCategoryBindInfo> record)
    {
        return bindingInfoMapper.batchInsert(record);
    }


    public TblRepoAppVersionInfo selectAppVersionByAppIdAndVersion(String appId, String version)
    {
        return appVersionInfoMapper.selectByAppIdAndVersion(appId, version);
    }

    public TblHelmStackInfo selectStackByPrimaryKey(String stackId)
    {
        return stackInfoMapper.selectByPrimaryKey(stackId);
    }

    public int deleteByPrimaryKey(String stackId)
    {
        return stackInfoMapper.deleteByPrimaryKey(stackId);
    }

    public List<TblRepoCategoryInfo> selectAllCategoryInfosByAppId(String appId)
    {
        Assert.hasText(appId, "The given app id must not be null!");
        List<TblRepoCategoryInfo> tblRepoCategoryInfos = categoryInfoMapper.selectAllByAppId(appId);
        if (CollectionUtils.isEmpty(tblRepoCategoryInfos))
        {
            return Collections.emptyList();
        }

        return tblRepoCategoryInfos;
    }


    public Integer countHelmStack(String ownerBpId,String ownerId)
    {
        return stackInfoMapper.countByOwnerBpIdAndOwnerId(ownerBpId,ownerId);
    }


    public TblRepoCategoryInfo selectUncategorizedCategory()
    {
        return categoryInfoMapper.selectUncategorizedCategory();
    }

    public int deleteAppInfosByRepoName(String repoName)
    {
        Assert.hasText(repoName, "The given repo name must not be null!");
        return repoAppInfoMapper.deleteByRepoName(repoName);
    }


    public int deleteAppVersionInfosByRepoName(String repoName)
    {
        Assert.hasText(repoName, "The given repo name must not be null!");
        return appVersionInfoMapper.deleteByRepoName(repoName);
    }

    public int deleteAppCategoryBindInfoByRepoId(Integer repoId)
    {
        Assert.notNull(repoId, "The given repo id must not be null!");
        return bindingInfoMapper.deleteByRepoId(repoId);
    }

    public int deleteAppCategoryBindInfo(Integer repoId,String appId)
    {
        Assert.notNull(repoId, "The given repo id must not be null!");
        Assert.notNull(appId, "The given app id must not be null!");
        return bindingInfoMapper.deleteByRepoIdAndAppId(repoId,appId);
    }

    public List<TblRepoAppVersionInfo> selectAppVersionInfos(String repoName,String appName,String version)
    {
        return appVersionInfoMapper.selectByRepoNameAndAppNameAndVersion(repoName, appName, version);
    }

    public int insertAppVersionSelective(TblRepoAppVersionInfo record)
    {
        return appVersionInfoMapper.insertSelective(record);
    }

    public int updateAppVersionByPrimaryKeySelective(TblRepoAppVersionInfo record)
    {
        return appVersionInfoMapper.updateByPrimaryKeySelective(record);
    }

    public int deleteAppInfoByRepoNameAndAppName(String repoName, String appName)
    {
        Assert.notNull(repoName, "The given repo name must not be null!");
        Assert.notNull(appName, "The given app name must not be null!");
        return repoAppInfoMapper.deleteByRepoNameAndAppName(repoName, appName);
    }

    public int deleteAppVersion(String repoName,String appName,String version)
    {
        return appVersionInfoMapper.deleteByRepoNameAndAppNameAndVersion(repoName, appName, version);
    }

    public int insertAppCategoryBindInfoSelective(TblRepoAppCategoryBindInfo record)
    {
        return bindingInfoMapper.insertSelective(record);
    }
}
