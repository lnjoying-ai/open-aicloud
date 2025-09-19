package com.lnjoying.justice.cmp.domain.info.baremetal;

import lombok.Data;

@Data
public class BaremetalDeviceAbbrInfo
{
    private String deviceId;
    private String name;
    private BaremetalDeviceSpecAbbrInfo deviceSpecInfo;
}
