package com.lnjoying.justice.schema.entity.servicemanager;

import lombok.Data;

import java.util.List;

@Data
public class RegisterServiceGatewayRsp
{
    private List<RpcCreatePortReq> ports;
}
