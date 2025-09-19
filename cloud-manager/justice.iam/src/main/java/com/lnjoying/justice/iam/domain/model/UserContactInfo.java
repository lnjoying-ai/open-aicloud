package com.lnjoying.justice.iam.domain.model;

import com.lnjoying.justice.commonweb.handler.aspect.annotation.SensitiveField;
import com.lnjoying.justice.commonweb.handler.aspect.enums.SensitiveType;
import com.lnjoying.justice.iam.config.ServiceConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Pattern;

@Data
public class UserContactInfo
{
    @ApiModelProperty(example = "test@edgegallery.org")
    @Pattern(regexp = "^$|" +  ServiceConfig.PATTERN_MAILADDRESS, message = "email is illegal")
    @SensitiveField(SensitiveType.EMAIL)
    private String email;

    @ApiModelProperty(required = true, example = "15533449966")
    @Pattern(regexp = "^$|" + ServiceConfig.PATTERN_TELEPHONE, message = "phone number is illegal")
    @SensitiveField(SensitiveType.PHONE)
    private String phone;

    private String address;

    public void trim()
    {
        this.email    =  StringUtils.trimWhitespace(this.email);
        this.phone    =  StringUtils.trimWhitespace(this.phone);
    }
}
