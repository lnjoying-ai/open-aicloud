package com.lnjoying.justice.schema.entity.scheduler;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class DstNode implements Serializable
{
    @JsonProperty(value = "dst_site_id")
    private String dstSiteId;

    @JsonProperty(value = "dst_region_id")
    private String dstRegionId;

    @JsonProperty(value = "dst_node_id")
    private String dstNodeId;
}
