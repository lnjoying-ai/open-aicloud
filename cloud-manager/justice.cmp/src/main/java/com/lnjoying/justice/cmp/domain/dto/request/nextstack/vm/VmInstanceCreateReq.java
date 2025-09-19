package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class VmInstanceCreateReq
{
    @ApiModelProperty(value = "申请新的EIP")
    private boolean applyEip = false;

    private List<@NotNull NetworkInfo> networkInfos;

    private List<@NotNull DiskInfo> diskInfos;

    private String imageId;

    @Min(0)
    @Max(1)
    private Integer imageOsType;

    @Min(60)
    @NotNull
    private Integer rootDisk;

    @NotBlank(message = "flavorId is required")
    private String flavorId;

    private String hostname;

    private String cmpTenantId;

    private String cmpUserId;

    private List<@NotBlank String> sgIds;

    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    private List<String> gpuIds;

    @NotBlank(message = "name cannot empty")
    @Length(max = 64, message = "name length must be less than 64")
    private String name;

    private String description;

    private String eipId;

    private String nodeId;

    @NotBlank(message = "storagePoolId is required")
    private String storagePoolId;

    @Data
    public static  class NetworkInfo
    {
        private String vpcId;
        private String subnetId;
        private String staticIp;
        private Boolean isVip;
    }

    @Data
    public static class DiskInfo
    {
        private Integer diskType;
        private Integer size;
        private String volumeId;
    }

    @AssertTrue(message = "volumeId or size is required")
    private boolean isValidDiskInfos()
    {
        if (diskInfos == null || diskInfos.isEmpty())
        {
            return true;
        }
        for (DiskInfo diskInfo : diskInfos)
        {
            if (diskInfo.getDiskType() == null || diskInfo.getSize() == null)
            {
                return false;
            }
        }
        return true;
    }

    @AssertTrue(message = "sysPassword or pubkeyId is required")
    private boolean isValid()
    {
        if (StringUtils.isNotBlank(imageId))
        {
            return !StringUtils.isBlank(sysPassword) || !StringUtils.isBlank(pubkeyId);
        }
        return true;
    }

    @AssertTrue(message = "username cannot be root")
    private boolean isValidUsername()
    {
        if (StringUtils.isBlank(sysUsername) && StringUtils.isBlank(imageId) && StringUtils.isBlank(hostname))
        {
            return true;
        }
        return !"root".equals(sysUsername) && sysUsername.matches("^[a-zA-Z0-9][a-zA-Z0-9-]{0,29}[a-zA-Z0-9]$");
    }

    @AssertTrue(message = "hostname is invalid")
    private boolean isValidHostname()
    {
        if(StringUtils.isNotBlank(imageId))
        {
            if (StringUtils.isBlank(hostname)) return true;
            if (hostname.equalsIgnoreCase("com") || hostname.equalsIgnoreCase("org") || hostname.equalsIgnoreCase("net") || hostname.equalsIgnoreCase("gov") || hostname.equalsIgnoreCase("edu") || hostname.equalsIgnoreCase("mil") || hostname.equalsIgnoreCase("arpa") || hostname.equalsIgnoreCase("cloud"))
            {
                return false;
            }
            return hostname.matches("^[a-zA-Z0-9][a-zA-Z0-9-]{0,61}[a-zA-Z0-9]$");
        }
        return true;
    }
}
