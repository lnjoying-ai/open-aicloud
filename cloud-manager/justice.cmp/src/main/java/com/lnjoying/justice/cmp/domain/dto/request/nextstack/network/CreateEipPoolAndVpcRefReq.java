package com.lnjoying.justice.cmp.domain.dto.request.nextstack.network;

import lombok.Data;

@Data
public class CreateEipPoolAndVpcRefReq
{
    String eipPoolId;
    Integer vlanId;
}
