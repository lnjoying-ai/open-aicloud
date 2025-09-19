package com.lnjoying.justice.schema.entity.scheduler;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SchedulerResult
 */
@Data
public class BindResourcesResult implements Serializable
{
    //success result
    private List<BindRelation> successBindRelations;
    //fail result
    private List<BindRelation> failBindRelations;
}
