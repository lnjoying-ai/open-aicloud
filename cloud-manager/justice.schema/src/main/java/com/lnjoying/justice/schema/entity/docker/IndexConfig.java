package com.lnjoying.justice.schema.entity.docker;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/2/21 7:33 PM
 * @Description:
 */
@Data
public class IndexConfig
{
    @JsonProperty("Mirrors")
    private List<String> mirrors;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Official")
    private Boolean official;
    @JsonProperty("Secure")
    private Boolean secure;
}
