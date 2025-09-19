package com.lnjoying.justice.cmp.domain.dto.response.nextstack.network;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RpcSgInfo implements Serializable
{
    private String sgId;
    private String sgName;
    private String description;
    private List<RpcSecurityGroupRule> rules;
}
