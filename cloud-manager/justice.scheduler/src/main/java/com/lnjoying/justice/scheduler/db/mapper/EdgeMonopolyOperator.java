package com.lnjoying.justice.scheduler.db.mapper;

import com.lnjoying.justice.scheduler.db.model.TblSchedEdgeMonopoly;
import org.apache.ibatis.annotations.*;

@Mapper
public interface EdgeMonopolyOperator
{
	@Insert("insert into tbl_sched_edge_monopoly (node_id, ref_id, status, create_time, update_time) values " +
			"(#{record.nodeId}, #{record.refId}, #{record.status}, #{record.createTime}, #{record.updateTime}) " +
			"on conflict (node_id, ref_id) do update set status=#{record.status}, update_time=#{record.createTime}")
	int insertOrUpdate(@Param("record") TblSchedEdgeMonopoly record);
}