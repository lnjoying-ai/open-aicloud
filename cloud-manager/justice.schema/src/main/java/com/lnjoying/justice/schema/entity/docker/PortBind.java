package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Author: Regulus
 * @Date: 11/16/21 8:18 PM
 * @Description:
 */
@Data
public class PortBind
{
    @SerializedName("HostIp")
    String hostIp;
    
    @SerializedName("HostPort")
    String hostPort;
}
