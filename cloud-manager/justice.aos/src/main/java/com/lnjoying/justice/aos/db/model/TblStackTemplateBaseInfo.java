package com.lnjoying.justice.aos.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Regulus
 * @Date: 12/13/23 9:53 AM
 */
@ApiModel(value = "com-lnjoying-justice-aos-db-model-TblStackTemplateBaseInfo")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblStackTemplateBaseInfo implements Serializable
{
    @ApiModelProperty(value = "")
    private String id;
    
    @ApiModelProperty(value = "")
    private String name;
    
    @ApiModelProperty(value = "")
    private String userId;
    
    @ApiModelProperty(value = "")
    private String bpId;
    
    @ApiModelProperty(value = "")
    private String logoUrl;
    
    @ApiModelProperty(value = "")
    private String vendor;
    
    @ApiModelProperty(value = "")
    private String description;
    
    @ApiModelProperty(value = "")
    private String labels;
    
    @ApiModelProperty(value = "")
    private Integer status;
    
    @ApiModelProperty(value = "")
    private Date createTime;
    
    @ApiModelProperty(value = "")
    private Date updateTime;

    /**
     * scope(0:private;1:bp;2:public)
     */
    @ApiModelProperty(value = "scope(0:private;1:bp;2:public)")
    private Integer scope;
    
    private static final long serialVersionUID = 1L;
}