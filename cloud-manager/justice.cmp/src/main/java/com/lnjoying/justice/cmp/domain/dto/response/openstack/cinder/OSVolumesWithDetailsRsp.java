package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import lombok.Data;

import java.util.List;

@Data
public class OSVolumesWithDetailsRsp
{
    private List<OSVolumeInfo> volumes;
}
