package com.lnjoying.justice.iam.db.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "tbl_iam_condition_key")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TblIamConditionKey
{
    @ApiModelProperty(value = "")
    private String id;

    @ApiModelProperty(value = "")
    private String projectId;

    /**
     * condition name:aos:tag, g:tag
     */
    @ApiModelProperty(value = "condition name:aos:tag, g:tag")
    private String conditionName;

    @ApiModelProperty(value = "")
    private Boolean fixedConditionName;

    @ApiModelProperty(value = "")
    private String conditionType;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private Boolean global;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "")
    private String serviceId;

    @ApiModelProperty(value = "")
    private String resourceId;
}