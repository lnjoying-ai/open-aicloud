package com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "addBaremetalInstanceRequest")
public class BaremetalInstanceCreateReq
{
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String deviceId;

    @NotBlank
    private String imageId;

    @NotBlank
    private String hostname;

    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    private String staticIp;

    @NotBlank
    private String vpcId;

    @NotBlank
    private String subnetId;

    public void trim()
    {
        this.name = StringUtils.trimWhitespace(this.name);
    }
}
