package com.lnjoying.justice.cluster.manager.domain.dto.req.cluster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import com.lnjoying.justice.cluster.manager.config.ServiceConfig;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTemplateInfo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.MemberInfo;
import com.lnjoying.justice.cluster.manager.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@ApiModel(value = "ExportClusterTemplateReqDto")
@JsonIgnoreProperties({ "clusterId", "type", "bp", "owner", "creator"})
public class ExportClusterTemplateReqDto
{
    @ApiModelProperty(value = "cluster id")
    private String clusterId;

    @ApiModelProperty(required = true, example = "true")
    @SerializedName("overwrite")
    @JsonProperty("overwrite")
    private Boolean        overwrite;
    
    @ApiModelProperty(required = true, example = "template-name")
    @SerializedName("template_name")
    @JsonProperty("template_name")
    @NotBlank(message = "template name can not be empty")
    @Pattern(regexp = ServiceConfig.PATTERN_NAME)
    private  String     templateName;
    
    @ApiModelProperty(required = true, example = "template-desc")
    @SerializedName("template_desc")
    @JsonProperty("template_desc")
    private String     templateDesc;
    
    @ApiModelProperty(required = true, example = "template-version")
    @SerializedName("template_version")
    @JsonProperty("template_version")
    @NotBlank(message = "template version can not be empty")
    @Pattern(regexp = ServiceConfig.PATTERN_VERSION)
    private String  templateVersion;

    @ApiModelProperty(value = "shared members")
    private MemberInfo members;

    @ApiModelProperty(value = "key identification information")
    private List<String> tags;

    @Enum(clazz = TblClusterTemplateInfo.TemplateType.class, message = "template type(0:created template;1:from cluster)", method = "getValue")
    @ApiModelProperty(value = "template type(0:created template;1:from cluster)", example = "0")
    private int type = 0;

    private String owner;

    private String  bp;

    private String creator;
}
