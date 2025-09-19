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
public class UpdatePasswordReq
{

    @SerializedName("old_password")
    @JsonProperty("old_password")
    @ApiModelProperty(required = true, example = "TestPassword1")
    @Pattern(regexp = ServiceConfig.PATTERN_PASSWORD)
    @SensitiveField(SensitiveType.PASSWORD)
    private String old_password;


    @SerializedName("new_password")
    @JsonProperty("new_password")
    @ApiModelProperty(required = true, example = "TestPassword1")
    @Pattern(regexp = ServiceConfig.PATTERN_PASSWORD)
    @SensitiveField(SensitiveType.PASSWORD)
    private String new_password;

    /**
     * check basic data by trim.
     */
    public void stringTrim() {
        this.old_password = StringUtils.trimWhitespace(this.old_password);
        this.new_password = StringUtils.trimWhitespace(this.new_password);
    }
}
