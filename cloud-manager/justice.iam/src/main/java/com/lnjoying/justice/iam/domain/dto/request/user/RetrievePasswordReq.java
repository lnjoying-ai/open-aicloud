package com.lnjoying.justice.iam.domain.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.SensitiveField;
import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.iam.config.ServiceConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;

@Data
public class RetrievePasswordReq
{

    @SerializedName("phone")
    @JsonProperty("phone")
    @ApiModelProperty(example = "15191881309")
    @Pattern(regexp = ServiceConfig.PATTERN_TELEPHONE)
    @SensitiveField(SensitiveType.PHONE)
    private String phone;

    @SerializedName("email")
    @JsonProperty("email")
    @ApiModelProperty(example = "test@lnjoying.com")
    @Pattern(regexp = ServiceConfig.PATTERN_MAILADDRESS)
    @SensitiveField(SensitiveType.EMAIL)
    private String email;


    @SerializedName("new_password")
    @JsonProperty("new_password")
    @ApiModelProperty(required = true, example = "TestPassword1")
    @Pattern(regexp = ServiceConfig.PATTERN_PASSWORD)
    @SensitiveField(SensitiveType.PASSWORD)
    private String new_password;

    @SerializedName("verification_code")
    @JsonProperty("verification_code")
    @ApiModelProperty(required = true, example = "123456")
    @Pattern(regexp = ServiceConfig.PATTERN_VERIFICATION_CODE)
    private String verification_code;


    /**
     * check basic data by trim.
     */
    public void stringTrim() {
        this.phone = StringUtils.trimWhitespace(this.phone);
        this.email = StringUtils.trimWhitespace(this.email);
        this.new_password = StringUtils.trimWhitespace(this.new_password);
        this.verification_code = StringUtils.trimWhitespace(this.verification_code);
    }
}
