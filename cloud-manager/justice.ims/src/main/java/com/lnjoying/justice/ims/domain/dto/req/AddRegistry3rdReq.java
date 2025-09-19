package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.ims.util.AnonymizeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * add a third party registry for requestBody
 *
 * @author merak
 **/

@Data
@ApiModel(value = "AddRegistry3rdReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId", "bpName", "userName", "createTime", "updateTime"})
public class AddRegistry3rdReq extends BaseReq
{
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * registry name
     */
    @NotBlank(message = "registry name can not be empty")
    @ApiModelProperty(value = "registry name")
    @JsonProperty(value = "registry_name")
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
    @NotBlank(message = "registry url can not be empty")
    @Pattern(regexp = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]))?$",
            message = "registry url(ip:port|domain)")
    @ApiModelProperty(value = "registry url")
    private String url;

    /**
     * Third-party access key
     */
    @ApiModelProperty(value = "Third-party access key")
    @JsonProperty(value = "access_key")
    private String accessKey;

    /**
     * Third-party access password
     */
    @ApiModelProperty(value = "Third-party access password")
    @JsonProperty(value = "access_secret")
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

    public String anonymizeAccessSecret() {
        String anonymizePassword = AnonymizeUtils.anonymizePassword(this.getAccessSecret());
        return anonymizePassword;
    }

    @Override
    public String toString()
    {
        return "AddRegistry3rdReq{" +
                "registryId='" + registryId + '\'' +
                ", registryName='" + registryName + '\'' +
                ", registryDesc='" + registryDesc + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", accessKey='" + accessKey + '\'' +
                ", accessSecret='" + this.anonymizeAccessSecret() + '\'' +
                ", insecure=" + insecure +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
