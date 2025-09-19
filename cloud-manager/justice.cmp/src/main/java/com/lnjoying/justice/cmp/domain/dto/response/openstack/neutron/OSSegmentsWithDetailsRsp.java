package com.lnjoying.justice.cmp.domain.dto.response.openstack.neutron;

import lombok.Data;

import java.util.List;

@Data
public class OSSegmentsWithDetailsRsp
{
    private List<OSSegmentInfo> segments;
}
