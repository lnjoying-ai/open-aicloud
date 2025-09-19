package com.lnjoying.justice.cmp.domain.dto.response.openstack;

import lombok.Data;

@Data
public class OSLink
{
    private static final long serialVersionUID = 1L;
    private String rel;
    private String href;
    private String type;
}
