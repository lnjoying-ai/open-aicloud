package com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "addBaremetalDeviceRequest")
public class BaremetalDeviceAddReq
{
    @NotBlank
    private String name;

    private String description;

    private String clusterId;

    private String userId;

    @NotBlank
    private String ipmiIp;

    @NotNull
    private short ipmiPort;

    @NotBlank
    private String ipmiUsername;

    @NotBlank
    private String ipmiPassword;

    @NotBlank
    private String bmcType;

    private String mac;

    public void trim()
    {
        this.name = StringUtils.trimWhitespace(this.name);
    }
}
