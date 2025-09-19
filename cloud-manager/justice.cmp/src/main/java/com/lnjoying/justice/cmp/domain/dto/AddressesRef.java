package com.lnjoying.justice.cmp.domain.dto;

import lombok.Data;

@Data
public class AddressesRef
{
    private String cidr;
    private String sgId;
    private String ipPoolId;
}
