package com.lnjoying.justice.cluster.manager.domain.dto.model.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.JkeConfig;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.K3sConfig;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 10:29
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({ "version_id", "owner", "bp", "creator"})
public class UpdateTemplateVersionReq
{
    @NotBlank(message = "version id can not be empty")
    @ApiModelProperty(value = "version id")
    private String versionId;

    @ApiModelProperty(value = "version desc")
    @JsonProperty(value = "desc")
    private String description;

//    @NotNull(message = "jkeConfig not be empty")
    @ApiModelProperty(value = "jkeConfig")
    private JkeConfig jkeConfig;

    @ApiModelProperty(value = "k3sConfig")
    private K3sConfig k3sConfig;

    @NotBlank(message = "enable not be empty")
    @ApiModelProperty(value = "enable")
    private boolean enable = true;

    @ApiModelProperty(value = "key identification information")
    private List<String> tags;

    private String owner;

    private String  bp;

    private String creator;
}
