package com.lnjoying.justice.cmp.domain.dto.request.nextstack.repo;

import com.lnjoying.justice.cmp.common.constant.OsType;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class ImageCreateReq
{
    @NotBlank(message = "imageName is required")
    private String imageName;

    @Min(OsType.LINUX) @Max(OsType.WINDOWS)
    private Short imageOsType;

    private Short imageOsVendor;

    private Short imageFormat;

    private String imageOsVersion;

    private String userId;

    private String location;
}
