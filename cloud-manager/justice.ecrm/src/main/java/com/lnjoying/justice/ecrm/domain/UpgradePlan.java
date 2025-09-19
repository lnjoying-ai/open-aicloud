package com.lnjoying.justice.ecrm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import com.lnjoying.justice.ecrm.common.constant.PlanAction;
import com.lnjoying.justice.ecrm.common.constant.UpgradePlanStatus;
import com.lnjoying.justice.schema.entity.TipMessage;
import com.lnjoying.justice.schema.entity.docker.DockerInstParams;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @Author: Regulus
 * @Date: 11/15/21 11:34 AM
 * @Description:
 */
@Data
@ToString
public class UpgradePlan implements Serializable
{
    private String nodeId;
    private String planName;
    private ContainerPlan containerPlan;
    private StackPlan stackPlan;
    private int status = UpgradePlanStatus.WAITING;
    private String log;

    public UpgradePlan(String nodeId, String planName)
    {
        this.nodeId = nodeId;
        this.planName = planName;
    }

    public UpgradePlan(String nodeId, String planName, String containerName)
    {
        this(nodeId, planName);
        containerPlan = new ContainerPlan();
        containerPlan.containerName = containerName;
        containerPlan.dockerInstParams = new DockerInstParams();
    }

    @Data
    @ToString
    public static class ContainerPlan
    {
        private DockerInstParams     dockerInstParams;
        private String               containerName;
        Boolean autoRun      = true;

        @JsonProperty("pre_action")
        private PlanAction preAction;

        //inited/creating/loging/checking
        @SerializedName("cur_action")
        @JsonProperty("cur_action")
        private PlanAction           curAction = PlanAction.INIT;

        private String logSucess = "";
        private String logFailed = "";
        private TipMessage tipMessage;

        //inited/creating/loging/checking
        @SerializedName("next_action")
        @JsonProperty("next_action")
        private PlanAction           nextAction = PlanAction.REMOVE_BEFORE;

        private long actionTime = 0;
        private int  errorTime  = 0;
    }

    @Data
    @ToString
    public static class StackPlan
    {
        private String ttyUrl;
        private String stackId;
        private Long timestamp;
        private boolean deployed = false;
        private int errorTime = 0;
    }
}
