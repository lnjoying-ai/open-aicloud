package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * Add project request body
 *
 * @author merak
 **/

@Data
@ApiModel(value = "AddRegistryProjectReq")
@JsonIgnoreProperties({"registryId", "bpId", "userId", "userName", "bpName"})
public class AddRegistryProjectReq extends BaseReq
{
    /**
     * project name
     */
    @ApiModelProperty(value = "project name")
    @JsonProperty("name")
    @Pattern(regexp = "^[a-z][a-z0-9_-]{0,63}$", message = "The project name is composed of lowercase characters, numbers and ' _ - ' and at least 1 character and starts with a character")
    private String projectName;

    /**
     * project descripion
     */
    @JsonProperty("description")
    @ApiModelProperty(value = "project descripion")
    private String projectDesc;

    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * scope(0:private;1:bp;2:public)
     */
    @ApiModelProperty(value = "scope(0:private;1:bp;2:public)")
    @Enum(clazz = TblImsRegistryProject.ProjectScope.class, message = "scope(0:private;1:bp;2:public")
    private Integer scope;

}
