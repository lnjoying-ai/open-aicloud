package com.lnjoying.justice.cmp.domain.dto.response.openstack.cinder;

import lombok.Data;

import java.util.List;

@Data
public class OSVolumesRsp
{
    private List<OSVolumeBasicInfo> volumes;
}
