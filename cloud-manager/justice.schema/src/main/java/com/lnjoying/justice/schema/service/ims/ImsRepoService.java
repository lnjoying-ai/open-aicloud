package com.lnjoying.justice.schema.service.ims;

import io.swagger.annotations.ApiParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/9/13 11:06
 */

public interface ImsRepoService
{

    /**
     * delete repo chart
     */
    void deleteRepoChart(@ApiParam(name = "repoChart")@NotNull RepoChart repoChart);

    final class RepoChart implements Serializable
    {
        private static final long serialVersionUID = 4937091889934236242L;

        private String repo;

        private String chartName;

        private String version;

        private String url;

        private String userName;

        private String password;

        public String getRepo()
        {
            return repo;
        }

        public void setRepo(String repo)
        {
            this.repo = repo;
        }

        public String getChartName()
        {
            return chartName;
        }

        public void setChartName(String chartName)
        {
            this.chartName = chartName;
        }

        public String getVersion()
        {
            return version;
        }

        public void setVersion(String version)
        {
            this.version = version;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public String getPassword()
        {
            return password;
        }

        public void setPassword(String password)
        {
            this.password = password;
        }
    }
}
