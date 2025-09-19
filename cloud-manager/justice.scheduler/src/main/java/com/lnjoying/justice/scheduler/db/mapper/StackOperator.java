package com.lnjoying.justice.scheduler.db.mapper;

import com.lnjoying.justice.scheduler.db.model.TblStackInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface StackOperator
{
	@Results(id = "TblStackInfo", value = {
			@Result(column = "stack_id", property = "stackId"),
			@Result(column = "spec_id", property = "specId"),
			@Result(column = "name", property="name"),
			@Result(column = "bp_id", property="bpId"),
			@Result(column = "user_id", property="userId"),
			@Result(column = "create_user_id", property="createUserId"),
			@Result(column = "status", property="status"),
			@Result(column = "dst_node", property="dstNode"),
			@Result(column = "labels", property="labels"),
			@Result(column = "dev_need_info", property="devNeedInfo"),
			@Result(column = "auto_run", property="autoRun"),
			@Result(column = "create_time", property="createTime"),
			@Result(column = "update_time", property="updateTime"),
			@Result(column = "send_create_num", property="sendCreateNum"),
			@Result(column = "start_num", property="startNum"),
			@Result(column = "fail_num", property="failNum"),
			@Result(column = "event_type", property="eventType"),
			@Result(column = "report_time", property="reportTime")
	})
	@SelectProvider(type = StackProvider.class, method = "getStackByNodeId")
	List<TblStackInfo> getStackByNodeId(@Param("nodeId") String nodeId, @Param("notInStatusList")List<Integer> notInStatusList);

	class StackProvider
	{
		private static Logger LOGGER = LogManager.getLogger();

		public String getStackByNodeId(String nodeId, List<Integer> notInStatusList)
		{
			SQL sql = new SQL()
			{
				{
					SELECT("stack_id, spec_id, name, bp_id, user_id, create_user_id, status, dst_node, labels, " +
							"dev_need_info, auto_run, create_time, update_time, send_create_num, start_num, fail_num, " +
							"event_type, report_time");
					FROM("tbl_stack_info");
					WHERE(String.format("dst_node @> '{\"dstNodeId\":\"%s\"}'", nodeId));
					WHERE(String.format("status not in (%s)", notInStatusList.stream().map(String::valueOf).collect(Collectors.joining(","))));
				}
			};
			String ret = sql.toString();
			LOGGER.debug("getStackByNodeId sql {}", ret);
			return ret;
		}
	}
}
