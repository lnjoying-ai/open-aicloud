package com.lnjoying.justice.ims.domain.dto.req;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.domain.model.ImsCommandType;
import com.lnjoying.justice.ims.validation.Enum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Request for tenants to obtain related command
 *
 * @author merak
 **/

@Data
@ApiModel(value = "TenantCommandReq")
@JsonIgnoreProperties({"bpId", "userId", "bpName", "userName"})
public class TenantCommandReq extends BaseReq
{
    @ApiModelProperty(value = "region id")
    @JsonProperty(value = "region_id")
    private String regionId;

    @ApiModelProperty(value = "registry_id")
    @JsonProperty(value = "registry_id")
    private String registryId;

    @NotBlank
    @Enum(clazz = ImsCommandType.class, message = "command type(0:login;1:pull;2:push)")
    @ApiModelProperty(value = "command type(0:login;1:pull;2:push)", example = "0")
    @JsonProperty(value = "command_type")
    private int commandType;
}
