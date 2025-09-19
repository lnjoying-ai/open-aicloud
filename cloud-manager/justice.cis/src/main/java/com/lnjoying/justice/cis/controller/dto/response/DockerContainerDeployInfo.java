package com.lnjoying.justice.cis.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Lists;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import lombok.Builder;
import lombok.Data;

import java.util.*;

import static com.lnjoying.justice.schema.common.ErrorCode.INST_DEPLOY_STATUS_ERROR;

/**
 * @Description: container deployment domain
 * @Author: Merak
 * @Date: 2022/1/10 10:31
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DockerContainerDeployInfo
{
    /**
     * container name
     */
    private String name;

    /**
     * deployment id
     */
    private String id;

    /**
     * image name
     */
    private String imageName;

    /**
     * total number of peplica
     */
    private int replicaNum;

    /**
     * number of deployments completed,
     * Ready means running and exiting after running.
     * Ready means that the container has been successfully installed and deployed on the edge side.
     * The corresponding status should be running or automatically exit after running
     */
    private int readyNum;

    /**
     * Container status not UNKNOWN
     */
    private int availableNum;

    /**
     * Deploying (status before 103)
     */
    private int processingNum;

    /**
     * Deployment failed (other status)
     */
    private int failedNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private String userId;

    private String bpId;

    private String userName;

    private String bpName;

    public enum DeployStatus
    {
        /**
         * Deploying (status before 103)
         */
        PROGRESSING(1100),
        /**
         * Deployment completed (all replica deployments have been completed, status: 103 ~ 111)
         */
         COMPLETE(1101),
        /**
         * Deployment failed (other status)
         */
        FAILED(1102);

        private int code;

        DeployStatus(int code)
        {
            this.code = code;
        }

        public static void checkStatus(int value) {
            Optional<DeployStatus> first = Arrays.stream(DeployStatus.values()).filter(deployStatus -> deployStatus.code == value).findFirst();
            DeployStatus deployStatus = first.orElseThrow(() -> new WebSystemException(INST_DEPLOY_STATUS_ERROR, ErrorLevel.ERROR));
        }
    }


    /**
     * Divide into different stages according to {@link InstanceState}
     *
     * @see InstanceState
     */
    public enum DeployStage
    {
        /**
         * InstanceState 1100, 1101
         */
        PROGRESSING_STAGE(1201),

        /**
         *  InstanceState 1103
         */
        AVAILABLE_STAGE(1202),

        /**
         *  InstanceState 1103, 1104
         */
        READY_STAGE(1203),

        /**
         * InstanceState 1102, 1105
         */
        FAILED_STAGE(1204);

        /**
         * delpoy stage ==> InstanceState code
         */
        public static Map<DeployStage, List<Integer>> simpleMap = new HashMap<>();

        /**
         * InstanceState code ==> delpoy stage
         */
        public static Map<Integer, DeployStage> fullMap = new HashMap<>();

        static {
            List<Integer> instanceState1100 = Lists.newArrayList(InstanceState.simpleMap.get(1100));
            List<Integer> instanceState1101 = Lists.newArrayList(InstanceState.simpleMap.get(1101));
            List<Integer> instanceState1102 = Lists.newArrayList(InstanceState.simpleMap.get(1102));
            List<Integer> instanceState1103 = Lists.newArrayList(InstanceState.simpleMap.get(1103));
            List<Integer> instanceState1104 = Lists.newArrayList(InstanceState.simpleMap.get(1104));
            List<Integer> instanceState1105 = Lists.newArrayList(InstanceState.simpleMap.get(1105));
            List<Integer> instanceState1110 = Lists.newArrayList(InstanceState.simpleMap.get(1110));

            List<Integer> progressingStageList = new ArrayList<>();
            progressingStageList.addAll(instanceState1100);
            progressingStageList.addAll(instanceState1101);
            simpleMap.put(PROGRESSING_STAGE, progressingStageList);

            List<Integer> availableStageList = new ArrayList<>();
            availableStageList.addAll(instanceState1103);
            simpleMap.put(AVAILABLE_STAGE, availableStageList);

            List<Integer> readyStageList = new ArrayList<>();
            readyStageList.addAll(instanceState1103);
            readyStageList.addAll(instanceState1104);
            simpleMap.put(READY_STAGE, readyStageList);

            List<Integer> failedStageList = new ArrayList<>();
            failedStageList.addAll(instanceState1102);
            failedStageList.addAll(instanceState1105);
            failedStageList.addAll(instanceState1110);
            simpleMap.put(FAILED_STAGE, failedStageList);

            simpleMap.entrySet().stream().forEach(entry -> {
                List<Integer> values = entry.getValue();
                values.stream().forEach(value -> {
                    int key = entry.getKey().code;
                    fullMap.put(value, fromValue(key));
                });
            });
        }

        public static DeployStage fromValue(int code)
        {
            return Arrays.stream(DeployStage.values()).filter(x -> x.getCode() == code).findFirst().get();
        }

        public static List<Integer> getFullStatus(DeployStage stage) throws WebSystemException
        {
            return simpleMap.get(stage);
        }

        private int code;

        DeployStage(int code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }

        public void setCode(int code)
        {
            this.code = code;
        }
    }
}
