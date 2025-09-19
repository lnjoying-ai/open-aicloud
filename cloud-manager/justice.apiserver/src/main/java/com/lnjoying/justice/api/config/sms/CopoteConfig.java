package com.lnjoying.justice.api.config.sms;

import lombok.Data;

@Data
public class CopoteConfig
{
    private String url;
    private String msgEcName;
    private String msgApId;
    private String msgSecretKey;
    private String sign;
    private String addSerial;
}
