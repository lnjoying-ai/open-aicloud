package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/16/21 8:06 PM
 * @Description:
 */
@Data
public class VolumeOptions
{
    @SerializedName("NoCopy")
    private Boolean noCopy;
    @SerializedName("Labels")
    private Map<String, String> labels;
    @SerializedName("DriverConfig")
    private Driver driverConfig;
}
