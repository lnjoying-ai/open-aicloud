package com.lnjoying.justice.iam.config.filter;

import lombok.Data;

@Data
public class SmsAuthenticationBean
{
    private String phone;
    private String code;
}
