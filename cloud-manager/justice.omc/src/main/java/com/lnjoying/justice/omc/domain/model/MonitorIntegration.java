package com.lnjoying.justice.omc.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.commonweb.util.JacksonUtils;
import com.lnjoying.justice.omc.db.model.TblOmcMonitorInstance;
import com.lnjoying.justice.omc.domain.dto.rsp.BaseRsp;
import com.lnjoying.justice.schema.entity.aos.StackDeploySimpleInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 16:58
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MonitorIntegration extends BaseRsp
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String name;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务,),,")
    @JsonProperty("integration_type")
    private Integer taskType;

    @ApiModelProperty(value = "")
    private String exporterType;

    @ApiModelProperty(value = "")
    private String configs;

    /**
     * 部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing)
     */
    @ApiModelProperty(value = "部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing),")
    private Integer status;

    @ApiModelProperty(value = "")
    private String dataSourceId;

    @ApiModelProperty(value="")
    private String dataSourceName;

    @ApiModelProperty(value = "")
    private String endpoint;

    /**
     * 目标类型(1:轻量级节点;2:nextstack;3:openstack)
     */
    @ApiModelProperty(value = "目标类型(1:轻量级节点;2:nextstack;3:openstack)")
    private Integer targetType;

    /**
     * 目标(节点的target保存节点信息，云target保存云id)
     */
    @ApiModelProperty(value = "目标(节点的target保存节点信息，云target保存云id)")
    private String targetId;


    @ApiModelProperty(value = "")
    private String regionId;

    @ApiModelProperty(value = "")
    private String regionName;

    @ApiModelProperty(value = "")
    private String siteId;

    @ApiModelProperty(value = "")
    private String siteName;

    @ApiModelProperty(value = "")
    private String nodeId;

    @ApiModelProperty(value = "")
    private String nodeName;

    @ApiModelProperty(value = "")
    private String bpId;

    @ApiModelProperty(value = "")
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String specId;

    private int replicaNum;

    private int readyNum;

    private int availableNum;

    private int processingNum;

    private int failedNum;

    private List<String> targets = new ArrayList<>();

    private List<String> targetNames;

    public static MonitorIntegration of(TblOmcMonitorInstance tblOmcMonitorInstance)
    {
        MonitorIntegration monitorIntegration = new MonitorIntegration();
        BeanUtils.copyProperties(tblOmcMonitorInstance, monitorIntegration);

        String targets = (String)tblOmcMonitorInstance.getTargets();
        if (StringUtils.isNotEmpty(targets))
        {
            List<String> targetList = JacksonUtils.strToObjTypeDefault(targets, new TypeReference<List<String>>()
            {
            });
            monitorIntegration.getTargets().addAll(targetList);
        }
        return monitorIntegration;
    }
}
