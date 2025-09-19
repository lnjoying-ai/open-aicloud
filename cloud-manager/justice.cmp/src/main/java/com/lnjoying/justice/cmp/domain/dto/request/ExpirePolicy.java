package com.lnjoying.justice.cmp.domain.dto.request;

import lombok.Data;

@Data
public class ExpirePolicy
{
    private Integer poweroffTimeAfterExpire;

    private Integer removeTimeAfterExpire;
}
