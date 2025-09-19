package com.lnjoying.justice.cluster.manager.domain.dto.model.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 19:43
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClusterTemplateInfo
{
    private String id;

    private String name;

    @JsonProperty(value = "desc")
    private String description;

    private int type;

    private String clusterId;

    private String clusterType;

    @JsonRawValue
    private String tags;

    @JsonRawValue
    private String members;

    private int versionNum;

    private List<ClusterTemplateVersionInfo> versions;

    private String owner;

    private String bp;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private String ownerName;

    private String bpName;

    private String clusterName;
}
