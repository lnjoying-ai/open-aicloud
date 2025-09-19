package com.lnjoying.justice.cmp.domain.dto.response.openstack.nova;

import lombok.Data;

import java.util.List;

@Data
public class OSFlavorsRsp
{
    private List<OSFlavorBasicInfo> flavors;
}
