package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * update Registry domain for requestBody
 *
 * @author merak
 **/

@Data
@ApiModel(value = "UpdateRegistryReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId", "createTime", "updateTime", "userName", "bpName"})
public class UpdateRegistryReq implements Serializable
{
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * registry name
     */
    @JsonProperty(value = "registry_name")
    //@NotBlank(message = "registry name can not be empty")
    @ApiModelProperty(value = "registry name")
    private String registryName;

    /**
     * registry desc
     */
    @ApiModelProperty(value = "registry desc")
    @JsonProperty(value = "registry_desc")
    private String registryDesc;

    /**
     * registry url
     */
    //@NotBlank(message = "registry url can not be empty")
    @Pattern(regexp = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]))?$",
            message = "registry url(ip:port|domain)")
    @ApiModelProperty(value = "registry url(ip:port|domain)")
    private String url;

    //@NotEmpty(message = "at least one regionId is required")
    @ApiModelProperty(value = "regionId list")
    private List<String> regions;

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
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;

    /**
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

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

    private static final long serialVersionUID = 1L;

}
