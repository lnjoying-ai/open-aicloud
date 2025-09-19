package com.lnjoying.justice.aos.domain.dto.rsp;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.google.common.collect.Lists;
import com.lnjoying.justice.aos.common.SimpleStackStatusEnum;
import com.lnjoying.justice.aos.domain.model.TemplateVerbose;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.lnjoying.justice.aos.common.SimpleStackStatusEnum.*;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@ApiModel(value = "NodeStackRsp")
public class NodeStackRsp
{
	StatusCode status;

	Map<String, String> extraInfo;

	@Data
	@ApiModel(value = "NodeStatusCode")
	public static final class StatusCode{
		private int code;
		private Map<String, String> desc;
	}

	public enum NodeStackDeployStatus
	{
		/**
		 * not deploy
		 */
		NOT_DEPLOY(1100, "not deploy", "未部署，请先创建"),

		/**
		 * node stack is deploying
		 */
		DEPLOYING(1101, "connecting", "连接中"),

		/**
		 * node stack deploy failed
		 */
		DEPLOY_FAILED(1102, "deploy failed", "部署失败"),

		/**
		 * node stack is running
		 */
		RUNNING(1103, "running", "运行中");

		private final int code;

		private final String enName;

		private final String cnName;

		NodeStackDeployStatus(int code, String enName, String cnName)
		{
			this.code = code;
			this.enName = enName;
			this.cnName = cnName;
		}

		/**
		 * nodeStackDeployStatus---> SimpleStackStatus code list
		 */
		public static Map<NodeStackDeployStatus, List<Integer>> nodeStackMap = new HashMap<>();

		/**
		 * SimpleStackStatus code ---> nodeStackDeployStatus
		 */
		public static Map<Integer, NodeStackDeployStatus> simpleStackStatusMap = new HashMap<>();

		static
		{
			List<Integer> deployingList = Lists.newArrayList(CREATING.getCode(), CREATED_SUCCEED.getCode());
			nodeStackMap.put(DEPLOYING, deployingList);

			List<Integer> deployFailedList = Lists.newArrayList(CREATED_FAILED.getCode(), STACK_QUIT.getCode(), STACK_NOT_EXIST.getCode(), SYSTEM_ABNORMAL.getCode());
			nodeStackMap.put(DEPLOY_FAILED, deployFailedList);

			List<Integer> runningList = Lists.newArrayList(SimpleStackStatusEnum.RUNNING.getCode());
			nodeStackMap.put(RUNNING, runningList);

			nodeStackMap.entrySet().stream().forEach(entry -> {
				List<Integer> values = entry.getValue();
				values.stream().forEach(value -> {
					simpleStackStatusMap.put(value, entry.getKey());
				});
			});
		}

		public static StatusCode of(NodeStackDeployStatus nodeStackDeployStatus)
		{
			StatusCode statusCode = new StatusCode();
			statusCode.setCode(nodeStackDeployStatus.getCode());
			Map<String, String> desc = new HashMap<>();
			desc.put("cn", nodeStackDeployStatus.getCnName());
			desc.put("en", nodeStackDeployStatus.getEnName());
			statusCode.setDesc(desc);
			return statusCode;
		}

		/**
		 * SimpleStackStatus code ---> nodeStackDeployStatus
		 * @param status
		 * @return
		 */
		public static NodeStackDeployStatus getNodeStackDeployStatus(int status)
		{
			return simpleStackStatusMap.get(status);
		}

		public int getCode()
		{
			return code;
		}

		public String getEnName()
		{
			return enName;
		}

		public String getCnName()
		{
			return cnName;
		}
	}

	public enum TtyStackDeployStatus
	{
		/**
		 * newly created
		 */
		NEW,

		/**
		 * deployed
		 */
		DEPLOYED,

		/**
		 * error
		 */
		ERROR,

		/**
		 * busy means the tty stack on the node may be deleting
		 */
		BUSY;
	}
}
