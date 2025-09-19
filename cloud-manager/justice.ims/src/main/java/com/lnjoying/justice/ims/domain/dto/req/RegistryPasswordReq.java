package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * registry password
 *
 * @author merak
 **/

@Data
@ApiModel(value = "RegistryPasswordReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId"})
public class RegistryPasswordReq
{
    /**
     * administrator old password
     */
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "the password  must longer than 8 chars with at least 1 uppercase letter, 1 lowercase letter and 1 number")
    @ApiModelProperty(value = "administrator old password", notes = "adminPasswd")
    @JsonProperty("old_password")
    private String oldPassword;

    /**
     * administrator new password
     */
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "the password  must longer than 8 chars with at least 1 uppercase letter, 1 lowercase letter and 1 number")
    @ApiModelProperty(value = "administrator new password", notes = "adminPasswd")
    @JsonProperty("new_password")
    private String newPassword;

    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

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

}
