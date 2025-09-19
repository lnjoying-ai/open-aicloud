package com.lnjoying.justice.iam.domain.dto.request.user;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.SensitiveField;
import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Data
@ApiModel(value = "userRegisterRequest")
@ToString(callSuper = true)
public class UserAddReq extends UserExBasicReq
{

    @ApiModelProperty(required = true, example = "TestPassword1")
    //@Pattern(regexp = ServiceConfig.PATTERN_PASSWORD, message = "")
    @SensitiveField(SensitiveType.PASSWORD)
    private String password;

    /**
     * check basic data by trim.
     */
    public void trim() {
        super.trim();
        this.password = StringUtils.trimWhitespace(this.password);
    }
}
