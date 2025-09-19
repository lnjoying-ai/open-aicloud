package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:42 PM
 * @Description:
 */
@Data
public class DeviceRequest
{
    @SerializedName("Driver")
    private String driver;
    @SerializedName("Count")
    private Integer count;
    @SerializedName("DeviceIDs")
    private List<String> deviceIds;
    @SerializedName("Capabilities")
    private List<List<String>> capabilities;
    @SerializedName("Options")
    private Map<String, String> options;
}
