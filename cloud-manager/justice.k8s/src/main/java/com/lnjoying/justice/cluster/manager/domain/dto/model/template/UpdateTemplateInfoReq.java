package com.lnjoying.justice.cluster.manager.domain.dto.model.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.MemberInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/26 10:29
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties({ "id", "bp", "creator"})
public class UpdateTemplateInfoReq
{
    private String id;

    @NotBlank(message = "template desc can not be empty")
    @ApiModelProperty(value = "template desc")
    @JsonProperty(value = "desc")
    private String description;

    @ApiModelProperty(value = "shared members")
    private MemberInfo members;

    @ApiModelProperty(value = "key identification information")
    private List<String> tags;

    private String  bp;

    private String creator;

    private String name;
}
