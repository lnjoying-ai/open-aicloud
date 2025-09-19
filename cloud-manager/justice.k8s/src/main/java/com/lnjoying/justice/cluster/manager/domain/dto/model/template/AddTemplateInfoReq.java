package com.lnjoying.justice.cluster.manager.domain.dto.model.template;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cluster.manager.common.ClusterType;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.MemberInfo;
import com.lnjoying.justice.cluster.manager.validation.Enum;
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
@JsonIgnoreProperties({ "type", "bp", "creator"})
public class AddTemplateInfoReq
{
    @NotBlank(message = "template name can not be empty")
    @ApiModelProperty(value = "template name")
    private String name;

    @NotBlank(message = "template desc can not be empty")
    @ApiModelProperty(value = "template desc")
    @JsonProperty(value = "desc")
    private String description;

    @ApiModelProperty(value = "shared members")
    private MemberInfo members;

    @ApiModelProperty(value = "key identification information")
    private List<String> tags;

    @Enum(clazz = TblClusterTemplateInfo.TemplateType.class, message = "template type(0:created template;1:from cluster)")
    @ApiModelProperty(value = "template type(0:created template;1:from cluster)", example = "0")
    private int type = 0;

    private String owner;

    private String  bp;

    private String creator;

    private String clusterType = ClusterType.K8S;
}
