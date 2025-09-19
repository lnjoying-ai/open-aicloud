package com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal;

import com.lnjoying.justice.cmp.domain.info.baremetal.BaremetalDeviceInfo;
import lombok.Data;

import java.util.List;

@Data
public class BaremetalDevicesRsp
{
    private long totalNum;

    private List<BaremetalDeviceInfo> devices;
}
