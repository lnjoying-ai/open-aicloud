package com.lnjoying.justice.omc.prometheus.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/7 16:46
 */

@Slf4j
public class GrafanaUtils
{


    public static String assembleGrafanaDashboardUrl(GrafanaDashboardUrlParser parser)
    {
        try
        {
            URI uri = new URI(parser.getScheme(), null, parser.getHost(), parser.getPort(), parser.getPath(), parser.getQueryParams().getQueryParamsAsString(), null);

            URL url = uri.toURL();
            return url.toString();
        }
        catch (Exception e)
        {
            log.error("assemble grafana dashboard url error:{}", e);
        }

        return "";
    }



    @Data
    public static class GrafanaDashboardUrlParser
    {
        private String scheme;
        private String host;
        private int port;
        private String path;
        private QueryParams  queryParams;

    }

    @Data
    public static class QueryParams
    {
        private String orgId = "1";
        private String refresh = "10s";
        private String from;
        private String to;
        private String kiosk;

        public String getQueryParamsAsString() {
            StringBuilder queryParamsString = new StringBuilder();


            if (orgId != null) {
                queryParamsString.append("orgId=").append(orgId).append("&");
            }

            if (refresh != null) {
                queryParamsString.append("refresh=").append(refresh).append("&");
            }

            if (from != null) {
                queryParamsString.append("from=").append(from).append("&");
            }

            if (to != null) {
                queryParamsString.append("to=").append(to).append("&");
            }

            if (kiosk != null) {
                queryParamsString.append("kiosk=").append(kiosk).append("&");
            }

            if (queryParamsString.length() > 0) {
                queryParamsString.deleteCharAt(queryParamsString.length() - 1);
            }

            return queryParamsString.toString();
        }

    }


}
