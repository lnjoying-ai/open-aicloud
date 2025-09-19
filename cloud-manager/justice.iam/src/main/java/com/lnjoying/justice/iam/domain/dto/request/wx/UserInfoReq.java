package com.lnjoying.justice.iam.domain.dto.request.wx;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/10/21 10:20
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoReq
{
    private String rawData;

    private String signature;

    private String encryptedData;

    private String iv;
}
