package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.db.model.TblContainerRunInfo;
import org.apache.ibatis.annotations.*;

@Mapper
public interface TblContainerRunInfoOperator
{
    @Select("select * from tbl_container_run_info where inst_id=#{instId} order by start_time desc limit 1")
    @Results({
            @Result(property = "runId", column = "run_id"),
            @Result(property = "instId", column = "inst_id"),
            @Result(property = "startTime", column = "start_time"),
            @Result(property = "stopTime", column = "stop_time"),
            @Result(property = "state", column = "state"),
            @Result(property = "instDetailInfo", column = "inst_detail_info")
    })
    TblContainerRunInfo getInstLatestRunInfo(@Param("instId") String instId);
}
