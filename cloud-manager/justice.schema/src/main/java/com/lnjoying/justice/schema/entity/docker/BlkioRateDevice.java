package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:40 PM
 * @Description:
 */
@Data
public class BlkioRateDevice
{
    @SerializedName("Path")
    private String path;
    @SerializedName("Rate")
    private Long rate;
}
