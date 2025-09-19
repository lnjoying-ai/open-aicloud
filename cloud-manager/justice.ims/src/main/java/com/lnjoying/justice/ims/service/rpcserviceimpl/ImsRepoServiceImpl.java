package com.lnjoying.justice.ims.service.rpcserviceimpl;

import com.lnjoying.justice.ims.harbor.api.ChartRepositoryApi;
import com.lnjoying.justice.schema.service.ims.ImsRepoService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.provider.pojo.RpcSchema;

import javax.validation.constraints.NotNull;

import static com.lnjoying.justice.ims.harbor.ApiClient.buildRepoHTTPSRequestUrl;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/9/13 11:10
 */

@RpcSchema(schemaId = "ImsRepoService")
@Slf4j
public class ImsRepoServiceImpl implements ImsRepoService
{

    @Override
    public void deleteRepoChart(@ApiParam(name = "repoChart") @NotNull RepoChart repoChart)
    {
        //https://192.168.1.250:18443/chartrepo/helm-202
        String url = repoChart.getUrl();
        String repo = "";
        if (url.indexOf('/') != -1)
        {
            int repoIndex = url.lastIndexOf('/') + 1;
            repo = url.substring(repoIndex);
        }
        else
        {
            repo = repoChart.getRepo();
        }
        ChartRepositoryApi chartRepositoryApi = new ChartRepositoryApi(buildRepoHTTPSRequestUrl(url), repoChart.getUserName(), repoChart.getPassword());

        String chartName = repoChart.getChartName();
        String version = repoChart.getVersion();
        if (StringUtils.isNotBlank(version))
        {
            chartRepositoryApi.chartrepoRepoChartsNameVersionIdDelete(repo, chartName, version);
        }
        else
        {
            chartRepositoryApi.chartrepoRepoChartsNameDelete(repo, chartName);
        }
    }
}
