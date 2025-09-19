package com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ClusterImportCmdRspDto
{
    @ApiModelProperty(required = true, example = "kubectl apply -f https://xx/import/import.yaml")
    @SerializedName("command")
    @JsonProperty("command")
    private String          command;
    
    @ApiModelProperty(required = true, example = "curl --insecure -sfL https://xx/import/import.yaml | kubectl apply -f -")
    @SerializedName("insecureCommand")
    @JsonProperty("insecureCommand")
    private  String insecureCommand;
}
