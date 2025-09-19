package com.lnjoying.justice.ims.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * operation log
 */
@ApiModel(value = "com-lnjoying-justice-ims-db-model-TblOperationLog")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblOperationLog
{
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "")
    private String description;

    @ApiModelProperty(value = "")
    private String userId;

    @ApiModelProperty(value = "")
    private String userName;

    @ApiModelProperty(value = "")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "")
    private Integer spendTime;

    @ApiModelProperty(value = "")
    private String uri;

    @ApiModelProperty(value = "")
    private String method;

    @ApiModelProperty(value = "")
    private String parameter;

    @ApiModelProperty(value = "")
    private String userAgent;

    @ApiModelProperty(value = "")
    private String ip;

    @ApiModelProperty(value = "")
    private String result;
}