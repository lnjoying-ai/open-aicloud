package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:42 PM
 * @Description:
 */
@Data
public class LogConfig
{
    @SerializedName("Type")
    public String type;
    @SerializedName("Config")
    public Map<String, String> config;
}
