package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.util.AnonymizeUtils;
import com.lnjoying.justice.ims.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * region Registry domain for requestBody
 *
 * @author merak
 **/

@Data
@ApiModel(value = "AddRegistryReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId", "bpName", "userName", "createTime", "updateTime"})
public class AddRegistryReq implements Serializable
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
    @ApiModelProperty(value = "registry desc")
    @JsonProperty(value = "registry_desc")
    private String registryDesc;

    /**
     * registry type(0:harbor;1:docker-hub)
     */
    @Enum(clazz = TblImsRegistry.RegistryType.class, message = "registry type(0:harbor;1:docker-hub)")
    @ApiModelProperty(value = "registry type(0:harbor;1:docker-hub)", example = "0")
    private int type = 0;

    /**
     * registry url
     */
    @NotBlank(message = "registry url can not be empty")
    @Pattern(regexp = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:([0-9]|[1-9]\\d|[1-9]\\d{2}|[1-9]\\d{3}|[1-5]\\d{4}|6[0-4]\\d{3}|65[0-4]\\d{2}|655[0-2]\\d|6553[0-5]))?$",
            message = "registry url(ip:port|domain)")
    @ApiModelProperty(value = "registry url(ip:port|domain)")
    private String url;

    @NotEmpty(message = "at least one regionId is required")
    @ApiModelProperty(value = "regionId list")
    private List<String> regions;

    /**
     * administrator account
     */
    @ApiModelProperty(value = "administrator account", example = "admin")
    @Pattern(regexp = "^[a-zA-Z][^,~#$%]{1,63}$", message = "the username  must longer than 1 chars and less than 64 chars and beginning with the letter, and cannot contain ',~#$%'")
    @JsonProperty(value = "admin_name")
    private String adminName;

    /**
     * administrator password
     */
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "the password  must longer than 8 chars with at least 1 uppercase letter, 1 lowercase letter and 1 number")
    @ApiModelProperty(value = "administrator password", notes = "adminPasswd")
    @JsonProperty(value = "admin_passwd")
    private String adminPasswd;

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
     * bp name
     */
    @ApiModelProperty(value = "bp name")
    private String bpName;

    /**
     * user name
     */
    @ApiModelProperty(value = "user name")
    private String userName;


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

    public String anonymizePassword() {
        String anonymizePassword = AnonymizeUtils.anonymizePassword(this.getAdminPasswd());
        return anonymizePassword;
    }

    @Override
    public String toString()
    {
        return "AddRegistryReq{" +
                "registryId='" + registryId + '\'' +
                ", registryName='" + registryName + '\'' +
                ", registryDesc='" + registryDesc + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", regions=" + regions +
                ", adminName='" + adminName + '\'' +
                ", adminPasswd='" + this.anonymizePassword() + '\'' +
                ", bpId='" + bpId + '\'' +
                ", userId='" + userId + '\'' +
                ", bpName='" + bpName + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
