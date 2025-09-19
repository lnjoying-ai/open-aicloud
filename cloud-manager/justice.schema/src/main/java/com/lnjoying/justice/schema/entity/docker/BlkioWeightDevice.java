package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 11:03 AM
 * @Description:
 */
@Data
public class BlkioWeightDevice
{
    @SerializedName("Path")
    private String path;
    @SerializedName("Weight")
    private Integer weight;
}
