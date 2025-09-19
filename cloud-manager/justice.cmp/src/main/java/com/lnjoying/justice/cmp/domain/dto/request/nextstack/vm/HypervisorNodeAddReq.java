package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class HypervisorNodeAddReq
{
    @NotEmpty(message = "name is required")
    @Length(max = 64, message = "name length must be less than 64")
    private String name;

    @Pattern(regexp = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."+
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."  +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", message = "manageIp is invalid")
    private String manageIp;

    private String hostname;

    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    private String description;
}
