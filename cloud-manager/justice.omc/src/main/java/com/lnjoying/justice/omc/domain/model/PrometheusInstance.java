package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.omc.db.model.TblOmcPrometheus;
import com.lnjoying.justice.omc.domain.dto.rsp.BaseRsp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/11/1 10:04
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PrometheusInstance extends BaseRsp
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

    @ApiModelProperty(value = "")
    private String siteId;

    @ApiModelProperty(value = "")
    private String siteName;

    @ApiModelProperty(value = "")
    private String nodeId;

    @ApiModelProperty(value = "")
    private String nodeName;

    /**
     * 状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)
     */
    @ApiModelProperty(value = "状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)")
    private Integer status;

    private Integer targetNum;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public static PrometheusInstance of (TblOmcPrometheus tblOmcPrometheus)
    {
        PrometheusInstance prometheusInstance = new PrometheusInstance();
        if (Objects.nonNull(tblOmcPrometheus))
        {
            BeanUtils.copyProperties(tblOmcPrometheus, prometheusInstance);
            if (!isAdmin())
            {
                prometheusInstance.setTargetNum(null);
            }
        }


        return prometheusInstance;
    }

}
