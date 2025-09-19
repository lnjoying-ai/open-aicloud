package com.lnjoying.justice.schema.common.scheduler;

import com.google.common.collect.Sets;

import java.util.Set;

public enum SchedulerState
{
    WAITING_SCHEDULING("WaitScheduling",0),
    SCHEDULING("Scheduling",1),
    SUCCESS("Success",2),
//    FAIL("Fail",3),

    //used by error detail 11~100
    //system error
    REDIS_ERROR("RedisError",11),

    //result error
    RESULT_NOT_MATCH_REQUIRE("ResultNotMatchRequire",21),

    //resources(edge) error
    NO_MATCHED_REGION("NoMatchedRegion",31),
    NO_MATCHED_SITE("NoMatchedSite",32),
    NO_MATCHED_NODE("NoMatchedNode",33),

    //params error
    UNSUPPORTED_PRODUCT_TYPE("UnSupportedProductType",41),
    DEV_NEED_INFO_ERROR("DevNeedInfoError",42),
    ON_ONE_NODE_ERROR("OnOneNodeError",43),
    TARGET_NODES_ERROR("TargetNodesError",44),
    REPLICA_OR_REF_ERROR("ReplicaOrRefError",45),
    STRATEGY_NOT_EXISTS("StrategyNotExists",46),
    DIRECT_TARGET_ERROR("DirectTargetError",47),
    LABEL_SELECTOR_ERROR("LabelSelectorError",48),
    SCHEDULER_ERROR("SchedulerError",50);

    private String name;
    private int code;

    SchedulerState(String name, int code)
    {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }

    public static final Set<Integer> noMatchNodeSchedulerStatusSet = Sets.newHashSet(SchedulerState.NO_MATCHED_REGION.getCode(),
            SchedulerState.NO_MATCHED_SITE.getCode(), SchedulerState.NO_MATCHED_NODE.getCode());
}
