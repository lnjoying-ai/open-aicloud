package com.lnjoying.justice.iam.domain.dto.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.commonweb.handler.aspect.annotation.SensitiveField;
import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.iam.config.ServiceConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;

@Data
@ApiModel(value = "userUpdateRequest")
public class UserUpdateReq extends UserExBasicReq
{

    @ApiModelProperty(required = true, example = "TestPassword1")
    @Pattern(regexp = ServiceConfig.PATTERN_PASSWORD)
    @SensitiveField(SensitiveType.PASSWORD)
    private String password;

    @SerializedName("inviter")
    @JsonProperty("inviter")
    private String inviter;
    /**
     * check basic data by trim.
     */
    public void trim() {
        super.trim();
        this.password = StringUtils.trimWhitespace(this.password);
    }
}
