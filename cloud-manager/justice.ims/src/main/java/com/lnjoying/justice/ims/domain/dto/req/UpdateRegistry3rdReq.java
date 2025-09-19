package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * add a third party registry for requestBody
 *
 * @author merak
 **/

@Data
@ApiModel(value = "UpdateRegistry3rdReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId", "userName", "bpName", "createTime", "updateTime"})
public class UpdateRegistry3rdReq extends BaseReq
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
    @ApiModelProperty(value = "registry name")
    private String registryName;

    /**
     * registry desc
     */
    @JsonProperty(value = "registry_desc")
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
    @Pattern(regexp = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]))?$",
            message = "registry url(ip:port|domain)")
    @ApiModelProperty(value = "registry url")
    private String url;

    /**
     * Third-party access key
     */
    @JsonProperty(value = "access_Key")
    @ApiModelProperty(value = "Third-party access key")
    private String accessKey;

    /**
     * Third-party access password
     */
    @JsonProperty(value = "access_secret")
    @ApiModelProperty(value = "Third-party access password")
    private String accessSecret;

    /**
     * Verify remote certificate
     */
    @ApiModelProperty(value = "Verify remote certificate")
    private Boolean insecure;

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

}
