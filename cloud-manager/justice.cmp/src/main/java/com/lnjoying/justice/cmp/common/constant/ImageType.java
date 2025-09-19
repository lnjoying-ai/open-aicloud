package com.lnjoying.justice.cmp.common.constant;

import lombok.Data;

@Data
public class ImageType
{
    public static final int RAW_TYPE = 1;
    public static final int QCOW2_TYPE = 2;

    public static final int VM_IMAGE_TYPE = 3;
    public static final int BAREMETAL_IMAGE_TYPE = 4;
}
