package com.lnjoying.justice.iam.domain.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.iam.config.ServiceConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "userRegisterRequest")
public class UserRegReq extends UserBasicReq
{

    @ApiModelProperty(required = true, example = "TestPassword1")
    @Pattern(regexp = ServiceConfig.PATTERN_PASSWORD)
    private String password;

    @SerializedName("verification_code")
    @JsonProperty("verification_code")
    @ApiModelProperty(example = "123456")
    @Pattern(regexp = ServiceConfig.PATTERN_VERIFICATION_CODE)
    private String verification_code;

    @SerializedName("invitation_code")
    @JsonProperty("invitation_code")
    private String invitation_code;

    /**
     * check basic data by trim.
     */
    public void trim() {
        super.trim();
        this.password = StringUtils.trimWhitespace(this.password);
        this.verification_code = StringUtils.trimWhitespace(this.verification_code);
    }
}
