package com.lnjoying.justice.aos.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 12/12/23 5:34 PM
 */
@ApiModel(value = "tbl_stack_spec_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblStackSpecInfo implements Serializable
{
    @ApiModelProperty(value = "")
    private String specId;
    
    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String specName;
    
    @ApiModelProperty(value = "")
    private String description;
    
    @ApiModelProperty(value = "")
    private String templateId;
    
    @ApiModelProperty(value = "")
    private String templateName;
    
    @ApiModelProperty(value = "")
    private String templateVersion;
    
    @ApiModelProperty(value = "")
    private String bpId;
    
    @ApiModelProperty(value = "")
    private String userId;
    
    @ApiModelProperty(value = "")
    private String createUserId;
    
    @ApiModelProperty(value = "")
    private Integer status;
    
    @ApiModelProperty(value = "")
    private String targetNodes;
    
    @ApiModelProperty(value = "")
    private Object dstNodes;
    
    @ApiModelProperty(value = "")
    private String labels;
    
    @ApiModelProperty(value = "")
    private String stackCompose;
    
    @ApiModelProperty(value = "")
    private String justiceCompose;
    
    @ApiModelProperty(value = "")
    private Integer replicaNum;
    
    @ApiModelProperty(value = "")
    private String aosType;
    
    @ApiModelProperty(value = "")
    private String contentType;
    
    @ApiModelProperty(value = "")
    private String devNeedInfo;
    
    @ApiModelProperty(value = "")
    private Boolean autoRun;
    
    @ApiModelProperty(value = "")
    private Date createTime;
    
    @ApiModelProperty(value = "")
    private Date updateTime;
    
    @ApiModelProperty(value = "")
    private String registryId;
    
    @ApiModelProperty(value = "")
    private String schedulingStrategy;
    
    @ApiModelProperty(value = "")
    private Object extraParams;
    
    @ApiModelProperty(value = "")
    private Object imageNames;
    
    @ApiModelProperty(value = "")
    private Boolean alwaysOnline;
    
    @ApiModelProperty(value = "")
    private String failoverPolicy;
    
    @ApiModelProperty(value = "")
    private String stackType;
    
    @ApiModelProperty(value = "")
    private Boolean useServicePenetration;
    
    @ApiModelProperty(value = "")
    private Integer deployStrategy;
    
    @ApiModelProperty(value = "")
    private String cfgs;
    
    @ApiModelProperty(value = "")
    private Object exposePorts;
    
    private static final long serialVersionUID = 1L;
    
    public void assembleImagePullPolicy()
    {
        if (Objects.isNull(this.getExtraParams()))
        {
            this.setExtraParams("{\"image_pull_policy\":\"IfNotPresent\"}");
        }
        else
        {
            String extraParams = (String) this.getExtraParams();
            Map extraParamsMap = JsonUtils.fromJson(extraParams, Map.class);
            String imagePullPolicy = (String) extraParamsMap.get("image_pull_policy");
            if (StringUtils.isBlank(imagePullPolicy))
            {
                extraParamsMap.put("image_pull_policy", "IfNotPresent");
                this.setExtraParams(JsonUtils.toJson(extraParamsMap));
            }
        }
    }
}