package com.lnjoying.justice.iam.domain.dto.request.user;

import com.lnjoying.justice.iam.config.ServiceConfig;
import com.lnjoying.justice.iam.domain.model.UserContactInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Data
public class UserBasicReq
{
    @ApiModelProperty(required = true, example = "test")
    @Pattern(regexp = ServiceConfig.PATTERN_USERNAME, message = " name that starts with a letter or number, can contain numbers, letters and -_.@, length 1-64 bytes")
    private String name;

    @ApiModelProperty(example = "lnjoying")
    private String bp_id;

    private int gender = 0;


    @Valid
    private UserContactInfo contact_info;

    /**
     * check basic data by trim.
     */
    public void trim()
    {
        this.name  =  StringUtils.trimWhitespace(this.name);
        this.bp_id =  StringUtils.trimWhitespace(this.bp_id);
        if (contact_info != null)
        {
            contact_info.trim();
        }
    }
}
