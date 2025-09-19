package com.lnjoying.justice.iam.domain.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.iam.config.ServiceConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;

@Data
public class VerificationReq
{

    @SerializedName("phone")
    @JsonProperty("phone")
    @ApiModelProperty(required = true, example = "15191881159")
    @Pattern(regexp = ServiceConfig.PATTERN_TELEPHONE)
    private String telephone;

    public void stringTrim() {
        this.telephone = StringUtils.trimWhitespace(telephone);
    }
}
