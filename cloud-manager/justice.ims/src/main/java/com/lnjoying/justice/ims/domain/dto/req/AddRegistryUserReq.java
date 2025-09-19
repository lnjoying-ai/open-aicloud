package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.ims.util.AnonymizeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Add registry user request body
 *
 * @author merak
 **/

@Data
@ApiModel(value = "AddRegistryUserReq")
@JsonIgnoreProperties({"bpId", "userId", "bpName", "userName"})
public class AddRegistryUserReq extends BaseReq
{
    @ApiModelProperty(value = "user name")
    @JsonProperty("user_name")
    @Pattern(regexp = "^[a-zA-Z][^,~#$%]{1,63}$", message = "the username  must longer than 1 chars and less than 64 chars and beginning with the letter, and cannot contain ',~#$%'")
    private String registryUserName;


    @ApiModelProperty("password")
    @JsonProperty("password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$", message = "the password  must longer than 8 chars with at least 1 uppercase letter, 1 lowercase letter and 1 number")
    private String registryPassword;

    public String anonymizePassword() {
        String anonymizePassword = AnonymizeUtils.anonymizePassword(this.getRegistryPassword());
        return anonymizePassword;
    }

    @Override
    public String toString()
    {
        return "AddRegistryUserReq{" +
                "bpId='" + this.getBpId() + '\'' +
                ", userId='" + this.getUserId() + '\'' +
                ", userName='" + this.getUserName() + '\'' +
                ", bpName='" + this.getBpName() + '\'' +
                "registryUserName='" + this.registryUserName + '\'' +
                ", registryPassword='" + this.anonymizePassword() + '\'' +
                '}';
    }
}
