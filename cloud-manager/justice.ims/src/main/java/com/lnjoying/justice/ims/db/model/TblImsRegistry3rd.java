package com.lnjoying.justice.ims.db.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.ResourceInstanceName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * region image registry table
 */
@ApiModel(value = "TblImsRegistry3rd")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblImsRegistry3rd
{
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * registry name
     */
    @ApiModelProperty(value = "registry name")
    @ResourceInstanceName
    private String registryName;

    /**
     * registry desc
     */
    @ApiModelProperty(value = "registry desc")
    private String registryDesc;

    /**
     * registry type(0:harbor;1:docker-hub)
     */
    @ApiModelProperty(value = "registry type(0:harbor;1:docker-hub)")
    private Integer type;

    /**
     * registry url
     */
    @ApiModelProperty(value = "registry url")
    private String url;

    /**
     * Third-party access key
     */
    @ApiModelProperty(value = "Third-party access key")
    private String accessKey;

    /**
     * Third-party access password
     */
    @ApiModelProperty(value = "Third-party access password")
    private String accessSecret;

    /**
     * Verify remote certificate
     */
    @ApiModelProperty(value = "Verify remote certificate")
    private Boolean insecure;

    /**
     * bp id
     */
    @ApiModelProperty(value = "bp id")
    private String bpId;

    /**
     * user id
     */
    @ApiModelProperty(value = "user id")
    private String userId;

    /**
     * record status(1:normal;-1:deleted)
     */
    @ApiModelProperty(value = "record status(1:normal;-1:deleted)")
    private Integer status;

    /**
     * create time
     */
    @ApiModelProperty(value = "create time")
    private LocalDateTime createTime;

    /**
     * update time
     */
    @ApiModelProperty(value = "update time")
    private LocalDateTime updateTime;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;
}