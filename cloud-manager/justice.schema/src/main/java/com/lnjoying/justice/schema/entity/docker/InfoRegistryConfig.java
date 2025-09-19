package com.lnjoying.justice.schema.entity.docker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/24/21 5:02 PM
 * @Description:
 */
@Data
public class InfoRegistryConfig implements Serializable
{
    @JsonProperty("IndexConfigs")
    private Map<String, IndexConfig> indexConfigs;
    @JsonProperty("InsecureRegistryCIDRs")
    private List<String> insecureRegistryCIDRs;
    @JsonProperty("Mirrors")
    private Object mirrors;
}
