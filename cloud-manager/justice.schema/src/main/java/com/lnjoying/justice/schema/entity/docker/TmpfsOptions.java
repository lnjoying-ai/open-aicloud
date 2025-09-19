package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 8:08 PM
 * @Description:
 */
@Data
public class TmpfsOptions
{
    @SerializedName("SizeBytes")
    private Long sizeBytes;
    @SerializedName("Mode")
    private Integer mode;
}
