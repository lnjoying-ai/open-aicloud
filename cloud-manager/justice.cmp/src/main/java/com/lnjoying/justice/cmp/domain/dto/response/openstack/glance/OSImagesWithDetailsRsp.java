package com.lnjoying.justice.cmp.domain.dto.response.openstack.glance;

import lombok.Data;

import java.util.List;

@Data
public class OSImagesWithDetailsRsp
{
    private List<OSImageInfo> images;
    private String first;
    private String next;
    private String schema;
}
