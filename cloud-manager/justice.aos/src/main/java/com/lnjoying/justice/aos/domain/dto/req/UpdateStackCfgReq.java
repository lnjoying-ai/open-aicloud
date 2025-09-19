package com.lnjoying.justice.aos.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UpdateStackCfgReq implements Serializable
{
    @JsonProperty(value = "cfg")
    private String cfg;

    @JsonProperty(value = "stack_ids")
    private List<String> stackIds;

    @JsonProperty(value = "restart")
    private boolean restart;
}
