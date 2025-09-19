package com.lnjoying.justice.ims.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * Tenant's registry view
 *
 * @author merak
 **/

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIncludeProperties({"registry_id", "registry_name", "url", "regions", "status"})
public class ImsRegistryTenant extends ImsRegistry
{
    public static ImsRegistryTenant of(ImsRegistry imsRegistry)
    {
        ImsRegistryTenant imsRegistryTenant = new ImsRegistryTenant();
        BeanUtils.copyProperties(imsRegistry, imsRegistryTenant);
        return imsRegistryTenant;
    }
}
