package com.lnjoying.justice.iam.domain.dto.request.user;

import com.lnjoying.justice.iam.config.ServiceConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;


@Data
public class UniqueReq
{

    @ApiModelProperty(required = true, example = "zhangtest")
    @Pattern(regexp = ServiceConfig.PATTERN_USERNAME)
    @Max(64)
    @Min(1)
    private String username;

    @ApiModelProperty(example = "15191881203")
    @Pattern(regexp = ServiceConfig.PATTERN_TELEPHONE)
    private String phone;

    @ApiModelProperty(example = "test@lnjoying.org")
    @Pattern(regexp = ServiceConfig.PATTERN_MAILADDRESS)
    private String email;

    public void stringTrim() {
        this.username = StringUtils.trimWhitespace(username);
        this.phone = StringUtils.trimWhitespace(phone);
        this.email = StringUtils.trimWhitespace(email);
    }
}
