package com.lnjoying.justice.aos.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Lists;
import com.lnjoying.justice.aos.common.SimpleStackStatusEnum;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorLevel;
import lombok.Data;

import java.util.*;

import static com.lnjoying.justice.aos.common.SimpleStackStatusEnum.*;
import static com.lnjoying.justice.schema.common.ErrorCode.INST_DEPLOY_STATUS_ERROR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/1/13 21:10
 */

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StackDeployInfo
{
	/**
	 * spec name
	 */
	private String name;

	/**
	 * deployment id
	 */
	private String id;

	/**
	 * image names
	 */
	@JsonRawValue
	private String imageNames;

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
	
	private String stackType;

	private TblStackSpecInfo StackSpecInfo;

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
	 * Divide into different stages according to {@link StackState}
	 *
	 * @see StackState
	 * @seee SimpleStackStatusEnum
	 */
	public enum DeployStage
	{
		/**
		 * StackState 1100, 1101
		 */
		PROGRESSING_STAGE(1201),

		/**
		 *  StackState 1103
		 */
		AVAILABLE_STAGE(1202),

		/**
		 *  StackState 1103, 1104
		 */
		READY_STAGE(1203),

		/**
		 * StackState 1102, 1105
		 */
		FAILED_STAGE(1204);

		/**
		 * delpoy stage ==> StackState code
		 */
		public static Map<DeployStage, List<Integer>> simpleMap = new HashMap<>();

		/**
		 * StackState code ==> delpoy stage
		 */
		public static Map<Integer, DeployStage> fullMap = new HashMap<>();

		static {
			List<Integer> stackState1100 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(CREATING));
			List<Integer> stackState1101 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(CREATED_SUCCEED));
			List<Integer> stackState1102 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(CREATED_FAILED));
			List<Integer> stackState1103 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(RUNNING));
			List<Integer> stackState1104 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(STACK_QUIT));
			List<Integer> stackState1105 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(STACK_NOT_EXIST));
			List<Integer> stackState1110 = Lists.newArrayList(SimpleStackStatusEnum.simpleMap.get(SYSTEM_ABNORMAL));

			List<Integer> progressingStageList = new ArrayList<>();
			progressingStageList.addAll(stackState1100);
			progressingStageList.addAll(stackState1101);
			simpleMap.put(PROGRESSING_STAGE, progressingStageList);

			List<Integer> availableStageList = new ArrayList<>();
			availableStageList.addAll(stackState1103);
			simpleMap.put(AVAILABLE_STAGE, availableStageList);

			List<Integer> readyStageList = new ArrayList<>();
			readyStageList.addAll(stackState1103);
			readyStageList.addAll(stackState1104);
			simpleMap.put(READY_STAGE, readyStageList);

			List<Integer> failedStageList = new ArrayList<>();
			failedStageList.addAll(stackState1102);
			failedStageList.addAll(stackState1105);
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
