package com.lnjoying.justice.api.config.sms;

import lombok.Data;

@Data
public class AlibabaConfig
{
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String signName;
}
