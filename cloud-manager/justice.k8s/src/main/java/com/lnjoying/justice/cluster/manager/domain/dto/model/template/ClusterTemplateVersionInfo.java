package com.lnjoying.justice.cluster.manager.domain.dto.model.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cluster.manager.common.ClusterType;
import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 20:03
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClusterTemplateVersionInfo
{
    private String templateId;

    @JsonProperty(value = "id")
    private String versionId;

    private String version;

    @JsonProperty(value = "desc")
    private String description;

    @JsonRawValue
    private String jkeConfig;

    @JsonRawValue
    private String k3sConfig;

    private Boolean enable;

    @JsonRawValue
    private String tags;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    public void setClusterEngineConfig(String clusterType)
    {
        if (clusterType == null || clusterType.equals(ClusterType.K8S))
        {
            setK3sConfig(null);
        }
        else if (clusterType.equals(ClusterType.K3S))
        {
            setJkeConfig(null);
        }
    }
}
