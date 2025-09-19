package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.AssertTrue;

@Data
public class ResetPasswordHostnameReq
{
    private String hostname;

    private String sysPassword;

    private String pubkeyId;

    @AssertTrue(message = "(sysPassword | pubkeyId) or hostname is required")
    private boolean isValid()
    {
        return StringUtils.isNotBlank(sysPassword) || StringUtils.isNotBlank(hostname) || StringUtils.isNotBlank(pubkeyId);
    }
}
