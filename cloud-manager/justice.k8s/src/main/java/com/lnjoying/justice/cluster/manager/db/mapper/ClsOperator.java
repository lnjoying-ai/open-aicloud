package com.lnjoying.justice.cluster.manager.db.mapper;

import com.lnjoying.justice.cluster.manager.common.ClusterActiveStatus;
import com.lnjoying.justice.cluster.manager.common.ClusterStatus;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterBasicInfoRspDto;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterSearchCritical;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;

@Mapper
public interface ClsOperator
{
    @SelectProvider(type = ClsProvider.class, method = "getClusterIdsByExtraCondition")
    Set<String> getClusterIdsByExtraCondition(Integer status, String clsIds);

    @Select("select id from tbl_cluster_info where status=#{status}")
    Set<String> getClusterIdsByStatus(@Param("status") Integer status);
    
    @SelectProvider(type = ClsProvider.class, method = "getClusterIdsByElapse")
    List<String> getClusterIdsByElapse(String elapseTime, int status);

    @Results(id = "ClusterBasicInfoRspDto", value = {
            @Result(column = "id", property = "id"),
            @Result(column = "name", property = "name")
    })
    @SelectProvider(type = ClsProvider.class, method = "getClusterBasicInfoByExample")
    List<ClusterBasicInfoRspDto> getClusterBasicInfoByExample(ClusterSearchCritical critical);

    class ClsProvider
    {
        private static Logger LOGGER = LogManager.getLogger();

        public String getClusterIdsByElapse(String elapseTime, int status)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("id");
                    FROM("tbl_cluster_info");
                    WHERE(String.format("status=%d", status));
                    AND();
                    WHERE(String.format("update_time + '%s' < now()", elapseTime));
                }
            };
            StringBuilder retSql = new StringBuilder(sql.toString()).append(" limit 100");
            LOGGER.debug("get stack id list {}" , retSql);
            return retSql.toString();
        }

        public String getClusterIdsByExtraCondition(Integer status, String clsIds)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("id");
                    FROM("tbl_cluster_info");
                    WHERE(String.format("status=%d", status));
                    AND();
                    WHERE(String.format("id not in %s", clsIds));
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get stack id list {}" , ret);
            return ret;
        }

        public String getClusterBasicInfoByExample(ClusterSearchCritical critical)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("id, name");
                    FROM("tbl_cluster_info");
                    if (critical.getName() != null)
                    {
                        WHERE(String.format("name =\'%s\'", critical.getName()));
                    }
                    if (critical.getOwner() != null)
                    {
                        WHERE(String.format("owner =\'%s\'", critical.getOwner()));
                    }
                    if (critical.getOperBpId() != null)
                    {
                        WHERE(String.format("bp =\'%s\'", critical.getOperBpId()));
                    }
//                    WHERE(String.format("status=%d", ClusterStatus.DEPLOYED.getCode()));
                    WHERE(String.format("service_state=%d", ClusterActiveStatus.ACTIVE));
                }
            };
            String ret = sql.toString();
            LOGGER.debug("get stack id list {}" , ret);
            return ret;
        }
    }
}
