package com.lnjoying.justice.cmp.domain.dto.request.openstack.nova;

import com.lnjoying.justice.cmp.domain.dto.request.openstack.OSBasicUpdateReq;
import lombok.Data;

@Data
public class OSFlavorUpdateReq
{
    private static final long serialVersionUID = 1L;
    private OSBasicUpdateReq flavor;
}
