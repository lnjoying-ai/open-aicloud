package com.lnjoying.justice.cmp.domain.dto.request.nextstack.vm;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class VmInstanceRenewReq
{
    @Valid
    @NotEmpty(message = "networkInfos is required")
    private List<VmInstanceCreateReq.NetworkInfo> networkInfos;

    private List<VmInstanceCreateReq.@NotNull DiskInfo> diskInfos;

    @NotBlank(message = "flavorId is required")
    private String flavorId;

    private String hostname;

    @NotEmpty(message = "sgIds is required")
    @Valid
    private List<String> sgIds;

    @NotBlank(message = "sysUsername is required")
    private String sysUsername;

    private String sysPassword;

    private String pubkeyId;

    @NotBlank(message = "name cannot be empty")
    @Length(max = 64, message = "name length must be less than 64")
    private String name;

    private String description;

    private String nodeId;

    @NotBlank(message = "storagePoolId is required")
    private String storagePoolId;
}
