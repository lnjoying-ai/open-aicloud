package com.lnjoying.justice.schema.entity.scheduler;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName SchedulerResult
 */
@Data
public class SchedulerResult implements Serializable
{
    //scheduler state
    private int schedulerState;
    //result
    private List<BindRelation> bindRelations;
    //specId
    private String specId;
    //ref ids
    private List<String> waitAssignIdList;
}
