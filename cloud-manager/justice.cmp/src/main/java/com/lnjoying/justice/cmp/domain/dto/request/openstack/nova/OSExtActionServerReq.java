package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import lombok.Data;

@Data
public class OSExtActionServerReq
{
    private static final long serialVersionUID = 1L;

    private Boolean confirmResize;
    private Boolean revertResize;
}
