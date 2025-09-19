package com.lnjoying.justice.scheduler.domain.model;

import com.lnjoying.justice.schema.entity.scheduler.BindRelation;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class SchedulerBean
{
    private String schedulerMsgType;
    private Object originReq;
    private AssignEdgeReq req;
    private Set<String> regions;
    private String regionRedisKey;
    private Set<String> sites;
    private String siteRedisKey;
    private List<String> nodes;
    private String nodeRedisKey;
    private Integer affinityState;
    private Integer strategyMask;
    private Integer schedulerState;
    private List<BindRelation> bindRelations;
}
