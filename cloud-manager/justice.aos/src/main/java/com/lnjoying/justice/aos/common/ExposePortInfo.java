package com.lnjoying.justice.aos.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

/**
 * @Description:定义一个端口暴露的类，用于服务穿透，其中包含用途、端口映射的信息
 * @Author: Regulus
 * @Date: 12/9/23 4:17 PM
 */
@Data
public class ExposePortInfo
{
    @SerializedName("exposePortMap")
    @JsonProperty("exposePortMap")
    private Map<Integer, ExposePort> exposePortMap;
    private String purpose;
}
