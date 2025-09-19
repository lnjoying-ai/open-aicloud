package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.TblOmcPrometheus;
import com.lnjoying.justice.omc.domain.dto.rsp.BaseRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 10:04
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PrometheusInstanceDetail extends BaseRsp
{
    public static final String API_REMOTE_READ = "/api/v1/read";

    public static final String API_REMOTE_WRITE = "/api/v1/write";

    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    /**
     * 类型(0:server;1:agent)
     */
    @ApiModelProperty(value = "类型(0:server;1:agent)")
    private Integer type;

    /**
     * 状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)
     */
    @ApiModelProperty(value = "状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)")
    private Integer status;

    private String remoteWriteUrl;

    private String remoteReadUrl;

    private String internalRemoteWriteUrl;

    private String internalRemoteReadUrl;

    private String config;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @JsonRawValue
    private String basicAuth;

    public static PrometheusInstanceDetail of (TblOmcPrometheus tblOmcPrometheus)
    {
        PrometheusInstanceDetail prometheusInstance = new PrometheusInstanceDetail();
        if (Objects.nonNull(tblOmcPrometheus))
        {
            BeanUtils.copyProperties(tblOmcPrometheus, prometheusInstance);
            String externalEndpointUrl = tblOmcPrometheus.getExternalEndpointUrl();
            if (StringUtils.hasText(externalEndpointUrl))
            {
                prometheusInstance.setRemoteReadUrl(externalEndpointUrl + API_REMOTE_READ);
                prometheusInstance.setRemoteWriteUrl(externalEndpointUrl + API_REMOTE_WRITE);
            }

            String internalEndpointUrl = tblOmcPrometheus.getInternalEndpointUrl();
            if (StringUtils.hasText(internalEndpointUrl))
            {
                prometheusInstance.setInternalRemoteReadUrl(internalEndpointUrl + API_REMOTE_READ);
                prometheusInstance.setInternalRemoteWriteUrl(internalEndpointUrl + API_REMOTE_WRITE);
            }

            String auth = (String)tblOmcPrometheus.getAuth();
            if (StringUtils.hasText(auth))
            {
                prometheusInstance.setBasicAuth(auth);
            }
        }


        return prometheusInstance;
    }

}
