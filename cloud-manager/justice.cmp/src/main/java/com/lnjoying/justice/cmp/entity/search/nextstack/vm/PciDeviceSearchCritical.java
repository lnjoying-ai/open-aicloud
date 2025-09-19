package com.lnjoying.justice.cmp.entity.search.nextstack.vm;

import com.lnjoying.justice.schema.entity.search.PageSearchCritical;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PciDeviceSearchCritical extends PageSearchCritical
{
        private String pciDeviceName;
}
