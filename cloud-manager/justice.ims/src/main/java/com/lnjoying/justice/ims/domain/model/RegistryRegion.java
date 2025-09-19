package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Region corresponding to registry
 *
 * @author merak
 **/

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class RegistryRegion
{
    private String registryId;

    private String regionId;

    private String regionName;
}
