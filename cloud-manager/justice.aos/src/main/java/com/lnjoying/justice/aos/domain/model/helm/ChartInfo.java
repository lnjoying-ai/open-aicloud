package com.lnjoying.justice.aos.domain.model.helm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.db.model.TblRepoAppInfo;
import com.lnjoying.justice.aos.db.model.TblRepoAppVersionInfo;
import com.lnjoying.justice.aos.db.model.TblRepoCategoryInfo;
import com.lnjoying.justice.aos.db.model.TblRepoInfo;
import com.lnjoying.justice.aos.db.repo.HelmRepository;
import com.lnjoying.justice.aos.facade.HelmFacade;
import com.lnjoying.justice.aos.helm.command.impl.ListCommand;
import com.lnjoying.justice.aos.util.JacksonUtils;
import com.lnjoying.justice.aos.util.VersionUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 16:20
 */

@Slf4j
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChartInfo
{

    /**
     * app name
     */
    @ApiModelProperty(value = "app name")
    @JsonProperty("name")
    private String appName;

    /**
     * repo_name+”-”+”app_name”
     */
    @ApiModelProperty(value = "repo_name+”-”+”app_name”")
    private String appId;

    /**
     * repo name
     */
    @ApiModelProperty(value = "repo name")
    private String repoName;


    private List<CategoryInfo> categories;

    private String description;

    private String icon;

    private String status;

    private ChartVersionInfo latestVersion;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private boolean remove = false;

    public static ChartInfo of(TblRepoAppInfo info, HelmRepository helmRepository, HelmFacade helmFacade)
    {
        ChartInfo chartInfo = new ChartInfo();
        BeanUtils.copyProperties(info, chartInfo);

        String latestVersion = getLatestVersion(info);
        TblRepoAppVersionInfo versionInfo = helmRepository.selectAppVersionByAppIdAndVersion(info.getAppId(), latestVersion);
        if (Objects.nonNull(versionInfo))
        {
            ChartVersionInfo chartVersionInfo = ChartVersionInfo.of(versionInfo, helmFacade);
            chartInfo.description = chartVersionInfo.getDescription();
            chartInfo.icon = chartVersionInfo.getIcon();
            chartInfo.status = chartVersionInfo.isDeprecated() ? "deprecated" : "active";
            chartInfo.setLatestVersion(chartVersionInfo);
            List<TblRepoCategoryInfo> tblRepoCategoryInfos = helmRepository.selectAllCategoryInfosByAppId(info.getAppId());
            List<CategoryInfo> categoryInfos = tblRepoCategoryInfos.stream().map(CategoryInfo::of).collect(Collectors.toList());
            chartInfo.setCategories(categoryInfos);
        }

        TblRepoInfo repoInfo = helmFacade.getRepoInfoByRepoName(info.getRepoName());

        try
        {
            String access = (String) repoInfo.getAccess();
            if (StringUtils.hasText(access))
            {
                List<String> accessList = JacksonUtils.strToObjType(access, new TypeReference<List<String>>(){});
                if (accessList.contains(TblRepoInfo.AccessType.REMOVE.getVerb()))
                {
                    chartInfo.setRemove(true);
                }
            }

        }
        catch (IOException e)
        {
            log.error("repo access parse error:{}", e);
        }

        return chartInfo;
    }

    private static String getLatestVersion(TblRepoAppInfo info)
    {
        String versions = (String) info.getVersions();
        if (StringUtils.hasText(versions))
        {
            try
            {
                List<String> versionList = JacksonUtils.strToObjType(versions, new TypeReference<List<String>>(){});
                if (!CollectionUtils.isEmpty(versionList))
                {
                    Collections.sort(versionList, (o1, o2) -> {
                        return VersionUtils.versionCompare(o2, o1);
                    });

                    return versionList.get(0);
                }
            }
            catch (IOException e)
            {
               log.warn("version list parse error:{}", e);
            }

        }
        return "";
    }
}
