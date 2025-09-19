package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSPool
{
    private static final long serialVersionUID = 1L;
    private String start;
    private String end;
}
