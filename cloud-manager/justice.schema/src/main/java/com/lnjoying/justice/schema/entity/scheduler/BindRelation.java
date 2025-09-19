package com.lnjoying.justice.schema.entity.scheduler;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName BindRelation
 */
@Data
public class BindRelation implements Serializable
{
    private String waitAssignId;
    private String dstSiteId;
    private String dstRegionId;
    private String dstNodeId;
}
