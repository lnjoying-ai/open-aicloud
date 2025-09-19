package com.lnjoying.justice.aos.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @Description:端口暴露的信息，包括在sevicemanager分配的信息和连侧的端口信息
 * @Author: Regulus
 * @Date: 12/9/23 4:17 PM
 */
@Data
public class ExposePort
{
    @SerializedName("edgePort")
    @JsonProperty("edgePort")
    private int edgePort;

    @SerializedName("cloudEP")
    @JsonProperty("cloudEP")
    private String cloudEP; //ip:address

    @SerializedName("portId")
    @JsonProperty("portId")
    private String portId;
    private String protocol = "tcp";
    private String cert;
    private String type;
}
