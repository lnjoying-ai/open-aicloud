package com.lnjoying.justice.cmp.domain.dto.response.nextstack.repo;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcImage implements Serializable
{
    private String imageId;
    private String name;
    private Integer format;
    private Integer imageOsType;
    private Integer imageOsVendor;
    private String imageOsVersion;
}