package com.lnjoying.justice.ecrm.domain.dto.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BindAck
{
    @SerializedName("uuid")
    @JsonProperty("uuid")
    private String uuid;

    @SerializedName("node_id")
    @JsonProperty("node_id")
    private String nodeId;

    @SerializedName("status")
    @JsonProperty("status")
    private int status;

    @SerializedName("message")
    @JsonProperty("message")
    private String message;
}
