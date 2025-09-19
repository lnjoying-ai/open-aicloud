package com.lnjoying.justice.cis.db.mapper;

import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mapper
public interface TblContainerInstInfoOperator
{

    @Select("select * from tbl_container_inst_info where status=#{status} and ((update_time + interval '1 min') < now())")
    @Results({
            @Result(property = "instId", column = "inst_id"),
            @Result(property = "refId", column = "ref_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "nodeId", column = "node_id"),
            @Result(property = "regionId", column = "region_id"),
            @Result(property = "siteId", column = "site_id"),
            @Result(property = "containerName", column = "container_name"),
            @Result(property = "imageName", column = "image_name"),
            @Result(property = "status", column = "status"),
            @Result(property = "containerParams", column = "container_params"),
            @Result(property = "cpuNum", column = "cpu_num"),
            @Result(property = "memLimit", column = "mem_limit"),
            @Result(property = "diskLimit", column = "disk_limit"),
            @Result(property = "transmitBandLimit", column = "transmit_band_limit"),
            @Result(property = "recvBandLimit", column = "recv_band_limit"),
            @Result(property = "devNeedInfo", column = "dev_need_info"),
            @Result(property = "autoRun", column = "auto_run"),
            @Result(property = "replicaNum", column = "replica_num"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "updateTime", column = "update_time"),
            @Result(property = "stopTime", column = "stop_time")
    })
    List<TblContainerInstInfo> getInstByStatus(@Param("status") Integer status);

    @SelectProvider(type = InstProvider.class, method = "getInstIdsByStatus")
    List<String> getInstIdsByStatus(Integer status);

    @SelectProvider(type = InstProvider.class, method = "getInstIdsByStatusWithTimeAndLtFailNum")
    List<String> getInstIdsByStatusWithTimeAndLtFailNum(Integer status, String timeParam, Integer failNum);

    @SelectProvider(type = InstProvider.class, method = "getInstIdsByStatusWithTimeAndGteFailNum")
    List<String> getInstIdsByStatusWithTimeAndGteFailNum(Integer status, String timeParam, Integer failNum);

    @SelectProvider(type = InstProvider.class, method = "getInstIdsByStatusWithTime")
    List<String> getInstIdsByStatusWithTime(Integer status, String timeParam);

    @SelectProvider(type = InstProvider.class, method = "getInstIdsLostStatusGT")
    List<String> getInstIdsLostStatusGT(String timeParam, Integer eventType);

    @SelectProvider(type = InstProvider.class, method = "getInstIdsLostStatusGTWithUpdateTime")
    List<String> getInstIdsLostStatusGTWithUpdateTime(String timeParams, Integer eventType);

    class InstProvider
    {
        private static Logger LOGGER = LogManager.getLogger();

        public String getInstIdsByStatus(Integer status)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_container_inst_info");
                    WHERE(String.format("status=%d", status));
                    WHERE("spec_id != ''");
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get inst id list {}" , ret);
            return ret;
        }

        public String getInstIdsByStatusWithTimeAndLtFailNum(Integer status, String timeParam, Integer failNum)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_container_inst_info");
                    WHERE(String.format("status=%d", status));
                    WHERE("spec_id != ''");
                    WHERE(String.format("fail_num<%d", failNum));
                    WHERE(String.format("update_time + '%s' < now()", timeParam));
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get inst id list {}" , ret);
            return ret;
        }

        public String getInstIdsByStatusWithTimeAndGteFailNum(Integer status, String timeParam, Integer failNum)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_container_inst_info");
                    WHERE(String.format("status=%d", status));
                    WHERE("spec_id != ''");
                    WHERE(String.format("fail_num>=%d", failNum));
                    WHERE(String.format("update_time + '%s' < now()", timeParam));
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get inst id list {}" , ret);
            return ret;
        }

        public String getInstIdsByStatusWithTime(Integer status, String timeParam)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_container_inst_info");
                    WHERE(String.format("status=%d", status));
                    WHERE("spec_id != ''");
                    WHERE(String.format("update_time + '%s' < now()", timeParam));
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get inst id list {}" , ret);
            return ret;
        }

        public String getInstIdsLostStatusGT(String timeParam, Integer eventType)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_container_inst_info");
                    WHERE("spec_id != ''");
                    WHERE(String.format("event_type = %d", eventType));
                    WHERE("status = 105");
                    WHERE(String.format("report_time + '%s' < now()", timeParam));
                    ORDER_BY("report_time");
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get inst id list {}" , ret);
            return ret;
        }

        public String getInstIdsLostStatusGTWithUpdateTime(String timeParams, Integer eventType)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_container_inst_info");
                    WHERE("spec_id != ''");
                    WHERE(String.format("event_type = %d", eventType));
                    WHERE("status = 105");
                    WHERE(String.format("report_time + '%s' < now()", timeParams));
                    WHERE(String.format("update_time + '%s' < now()", timeParams));
                    ORDER_BY("update_time");
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get inst id list {}" , ret);
            return ret;
        }
    }
}
