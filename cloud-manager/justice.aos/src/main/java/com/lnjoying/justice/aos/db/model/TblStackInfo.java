package com.lnjoying.justice.aos.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 12/12/23 5:34 PM
 */
@ApiModel(value = "tbl_stack_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblStackInfo implements Serializable
{
    @ApiModelProperty(value = "")
    private String stackId;
    
    @ApiModelProperty(value = "")
    private String specId;
    
    @ApiModelProperty(value = "")
    @ResourceInstanceName
    private String name;
    
    @ApiModelProperty(value = "")
    private String bpId;
    
    @ApiModelProperty(value = "")
    private String userId;
    
    @ApiModelProperty(value = "")
    private String createUserId;
    
    @ApiModelProperty(value = "")
    private Integer status;
    
    @ApiModelProperty(value = "")
    private Object dstNode;
    
    @ApiModelProperty(value = "")
    private String labels;
    
    @ApiModelProperty(value = "")
    private String devNeedInfo;
    
    @ApiModelProperty(value = "")
    private Boolean autoRun;
    
    @ApiModelProperty(value = "")
    private Date createTime;
    
    @ApiModelProperty(value = "")
    private Date updateTime;
    
    @ApiModelProperty(value = "")
    private Integer sendCreateNum;
    
    @ApiModelProperty(value = "")
    private Integer startNum;
    
    @ApiModelProperty(value = "")
    private Integer failNum;
    
    @ApiModelProperty(value = "")
    private Integer eventType;
    
    @ApiModelProperty(value = "")
    private Date reportTime;
    
    @ApiModelProperty(value = "")
    private Boolean alwaysOnline;
    
    @ApiModelProperty(value = "")
    private String stackType;
    
    @ApiModelProperty(value = "")
    private Boolean useServicePenetration;
    
    @ApiModelProperty(value = "")
    private Object exposePorts;
    
    private static final long serialVersionUID = 1L;
}