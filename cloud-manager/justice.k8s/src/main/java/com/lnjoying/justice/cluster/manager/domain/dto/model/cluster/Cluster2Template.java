package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.config.ServiceConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import java.util.List;

@Data
public class Cluster2Template
{
    @ApiModelProperty(example = "[]")
    @SerializedName("versions")
    @JsonProperty("versions")
    private List<Version>    versions;
    
    @ApiModelProperty(example = "template-name")
    @SerializedName("template_name")
    @JsonProperty("template_name")
    private String     templateName;

    @ApiModelProperty(example = "template-id")
    @SerializedName("template_id")
    @JsonProperty("template_id")
    private String templateId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Version
    {
        @ApiModelProperty(example = "version-id")
        @SerializedName("version_id")
        @JsonProperty("version_id")
        private String versionId;

        private String version;
    }
}
