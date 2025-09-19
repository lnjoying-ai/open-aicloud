package com.lnjoying.justice.aos.domain.model.helm;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.aos.db.model.TblRepoAppInfo;
import com.lnjoying.justice.aos.db.model.TblRepoAppVersionInfo;
import com.lnjoying.justice.aos.db.model.TblRepoInfo;
import com.lnjoying.justice.aos.facade.HelmFacade;
import com.lnjoying.justice.aos.helm.model.IndexFile;
import com.lnjoying.justice.aos.util.JacksonUtils;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/9 15:13
 */

@EqualsAndHashCode(callSuper=true)
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ChartVersionInfo extends IndexFile.ChartVersion
{
    public ChartVersionInfo()
    {
    }

    public ChartVersionInfo(String name, String home, List<String> sources, String version, String description, List<String> keywords, String icon, String apiVersion,
                            String condition, String appVersion, boolean deprecated, Map<String, String> annotations, List<String> urls, String created, String removed,
                            String digest, String engine, List<IndexFile.Maintainer> mainContainers, List<IndexFile.Dependency> dependencies, String type, String kubeVersion)
    {
        super(name, home, sources, version, description, keywords, icon, apiVersion, condition, appVersion, deprecated, annotations, urls, created, removed, digest, engine,
                mainContainers, dependencies, type, kubeVersion);
    }

    @SneakyThrows
    public static ChartVersionInfo of(TblRepoAppVersionInfo info, HelmFacade helmFacade)
    {
        ChartVersionInfo versionInfo = new ChartVersionInfo();
        ChartVersionInfo chartVersionInfo = JacksonUtils.strToObj((String) info.getAppInfo(), ChartVersionInfo.class);
        List<String> urls = chartVersionInfo.getUrls();
        if (!CollectionUtils.isEmpty(urls))
        {
            TblRepoInfo repoInfo = helmFacade.getRepoInfoByRepoName(info.getRepoName());
            List<String> collect = urls.stream().filter(url -> !(url.contains("https") || url.contains("http"))).map(url -> repoInfo.getRepoUrl() + "/" + url).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(collect))
            {
                chartVersionInfo.setUrls(collect);
            }
        }
        return chartVersionInfo;
    }


}
