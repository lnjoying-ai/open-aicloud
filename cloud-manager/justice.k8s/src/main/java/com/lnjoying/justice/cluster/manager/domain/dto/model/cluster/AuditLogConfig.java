package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.PackagePrivate;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/19/21 8:18 PM
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PackagePrivate
public class AuditLogConfig implements Serializable
{
    @ApiModelProperty(example = "")
    @SerializedName("max_age")
    @JsonProperty("max_age")
    private int  maxAge;
    
    @ApiModelProperty(example = "")
    @SerializedName("max_backup")
    @JsonProperty("max_backup")
    private int maxBackup; // int             `yaml:"max_backup" json:"maxBackup,omitempty"`
    
    @ApiModelProperty(example = "")
    @SerializedName("max_size")
    @JsonProperty("max_size")
    private int maxSize;   //   int             `yaml:"max_size" json:"maxSize,omitempty"`
    
    @ApiModelProperty(example = "")
    @SerializedName("path")
    @JsonProperty("path")
    private String path;   //      string          `yaml:"path" json:"path,omitempty"`
    
    @ApiModelProperty(example = "")
    @SerializedName("format")
    @JsonProperty("format")
    private  String format; //    string          `yaml:"format" json:"format,omitempty"`
    
    @ApiModelProperty(example = "{}")
    @SerializedName("policy")
    @JsonProperty("policy")
    private AuditPolicy policy;
    //Policy    *auditv1.Policy `yaml:"policy" json:"policy,omitempty" norman:"type=map[json]"`
}
