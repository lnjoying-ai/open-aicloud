package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 6:43 PM
 * @Description:
 */
@Data
public class LxcConf
{
    @SerializedName("Key")
    public String key;
    @SerializedName("Value")
    public String value;
}
