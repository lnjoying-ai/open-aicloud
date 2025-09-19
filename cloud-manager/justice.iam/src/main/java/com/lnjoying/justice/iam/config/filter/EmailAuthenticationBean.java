package com.lnjoying.justice.iam.config.filter;

import lombok.Data;

@Data
public class EmailAuthenticationBean
{
    private String email;
    private String code;
}
