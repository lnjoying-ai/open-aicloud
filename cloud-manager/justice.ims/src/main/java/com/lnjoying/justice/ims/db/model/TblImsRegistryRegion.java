package com.lnjoying.justice.ims.db.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "com-lnjoying-justice-ims-db-model-TblImsRegistryRegion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TblImsRegistryRegion
{
    /**
     * registry id
     */
    @ApiModelProperty(value = "registry id")
    private String registryId;

    /**
     * region id
     */
    @ApiModelProperty(value = "region id")
    private String regionId;
}