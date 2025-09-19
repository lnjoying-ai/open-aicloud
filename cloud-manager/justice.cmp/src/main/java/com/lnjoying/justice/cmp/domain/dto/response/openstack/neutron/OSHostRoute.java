package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSHostRoute
{
    private static final long serialVersionUID = 1L;
    private String destination;
    private String nexthop;
}
