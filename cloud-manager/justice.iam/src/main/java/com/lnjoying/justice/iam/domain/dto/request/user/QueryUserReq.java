package com.lnjoying.justice.iam.domain.dto.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class QueryUserReq
{
    private String username;

    private String mailAddress;

    private String telephone;

    @ApiModelProperty(example = "ADMIN")
    @Pattern(regexp = "ALL|ADMIN|TENANT|GUEST")
    private String role;

    @Min(value = -1)
    @Max(value = 1)
    private int status;

    private String createTimeBegin;

    private String createTimeEnd;

    @NotNull
    @Valid
    private QueryUserCtrl queryCtrl;

    /**
     * check basic data by trim.
     */
    public void stringTrim() {
        this.username = StringUtils.trimWhitespace(this.username);
        this.mailAddress =  StringUtils.trimWhitespace(this.mailAddress);
        this.telephone =  StringUtils.trimWhitespace(this.telephone);
        this.role =  StringUtils.trimWhitespace(this.role);
        this.createTimeBegin =  StringUtils.trimWhitespace(this.createTimeBegin);
        this.createTimeEnd =  StringUtils.trimWhitespace(this.createTimeEnd);
        this.queryCtrl.stringTrim();
    }
}
