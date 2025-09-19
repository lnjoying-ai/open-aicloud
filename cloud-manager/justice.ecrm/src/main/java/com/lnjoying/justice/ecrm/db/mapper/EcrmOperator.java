package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.NameIDInfo;
import com.lnjoying.justice.ecrm.domain.model.search.GwSearchCritical;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.UserHeadInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

@Mapper
public interface EcrmOperator
{
    @Results(id = "edgeGwInfo", value = {
            @Result(column = "node_id", property = "nodeId"),
            @Result(column = "node_name", property = "nodeName"),
            @Result(column = "host_name", property = "hostName"),
            @Result(column = "host_public_ip", property = "hostPublicIp"),
            @Result(column = "host_public_port", property = "hostPublicPort"),
            @Result(column = "host_inner_ip", property = "hostInnerIp"),
            @Result(column = "host_inner_port", property = "hostInnerPort"),
            @Result(column = "mac", property = "mac"),
            @Result(column = "nic_name", property = "nicName"),
            @Result(column = "active_status", property = "activeStatus"),
            @Result(column = "online_status", property = "onlineStatus"),
            @Result(column = "vender", property = "vender"),
            @Result(column = "uuid", property = "uuid"),
            @Result(column = "product", property = "product"),
            @Result(column = "sn", property = "sn"),
            @Result(column = "cpu_logic_num", property = "cpuLogicNum"),
            @Result(column = "cpu_physical_num", property = "cpuPhysicalNum"),
            @Result(column = "cpu_model", property = "cpuModel"),
            @Result(column = "cpu_frequency", property = "cpuFrequency"),
            @Result(column = "mem_total", property = "memTotal"),
            @Result(column = "disk_total", property = "diskTotal"),
            @Result(column = "disk_type", property = "diskType"),
            @Result(column = "disk_detail", property = "diskDetail"),
            @Result(column = "os", property = "os"),
            @Result(column = "core_version", property = "coreVersion"),
            @Result(column = "software_version", property = "softwareVersion"),
            @Result(column = "network", property = "network"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    @SelectProvider(type = GwProvider.class, method = "getGws")
    public List<TblEdgeGwInfo> getEdgeGws(GwSearchCritical critical);

    @SelectProvider(type = GwProvider.class, method = "countGws")
    public long countEdgeGws(GwSearchCritical critical);

    @SelectProvider(type = GwProvider.class, method = "countEdgeActiveCores")
    public long countEdgeActiveCores();

    @ResultMap("edgeGwInfo")
    @SelectProvider(type = GwProvider.class, method = "getGwsByRegion")
    public List<TblEdgeGwInfo> getGwsByRegion(String region);

    @Select("SELECT region_id FROM tbl_region_bind_info  WHERE node_id= #{nodeId}")
    public List<String> getRegionIdsByGwId(@Param("nodeId")String nodeId);

    @SelectProvider(type = GwProvider.class, method = "getRegionInfosByGwId")
    public List<NameIDInfo> getRegionInfosByGwId(String nodeId);

    @Select("SELECT site_id FROM tbl_site_info  WHERE adcode in #{adcodes}")
    public List<String> getSiteIdsByAdcodes(@Param("adcodes") String adcodes);

    @SelectProvider(type = GwProvider.class, method = "getRegionIdsByGwNodeId")
    public List<String> getRegionIdsByGwNodeId(String nodeId);
//    @Select("SELECT node_id FROM tbl_region_bind_info  WHERE region_id= #{regionId}")
//    public List<String> getGwIdsByReionId(@Param("regionId")String regionId);

    @SelectProvider(type = GwProvider.class, method = "getRegionBinds")
    public List<NameIDInfo> getRegionBinds(String regionId);

    class GwProvider
    {
        private static Logger LOGGER = LogManager.getLogger();

        public String getGws(GwSearchCritical critical)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("*");
                    FROM("tbl_edge_gw_info as a");
                    if (critical.getRegion() != null)
                    {
                        FROM("tbl_region_bind_info as c");
                        AND();
                        WHERE(String.format("c.region_id =\'%s\'", critical.getRegion()));
                        AND();
                        WHERE("a.node_id = c.node_id");
                    }
                    if (critical.getActive_status() != null)
                    {
                        AND();
                        WHERE("a.active_status = " + critical.getActive_status());
                    }
                    else
                    {
                        AND();
                        WHERE("a.active_status != " + ActiveStatus.REMOVED);
                    }
                    if (critical.getOnline_status() != null)
                    {
                        AND();
                        WHERE("a.online_status = " + critical.getOnline_status());
                    }
                }
            };
            StringBuilder retSql = new StringBuilder(sql.toString());
            if (critical.getPageNum() > 0)
            {
                int begin = ((critical.getPageNum()-1) * critical.getPageSize());
                retSql.append(String.format(" limit %d offset %d", critical.getPageSize(), begin));
            }

            LOGGER.info("get gws by critical sql {}", retSql);
            return retSql.toString();
        }

        public String countGws(GwSearchCritical critical)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("count(*)");
                    FROM("tbl_edge_gw_info as a");
                    if (critical.getRegion() != null)
                    {
                        FROM("tbl_region_bind_info as c");
                        AND();
                        WHERE(String.format("c.region_id =\'%s\'", critical.getRegion()));
                        AND();
                        WHERE("a.node_id = c.node_id");
                    }
                    if (critical.getActive_status() != null)
                    {
                        AND();
                        WHERE("a.active_status = " + critical.getActive_status());
                    }
                    else
                    {
                        AND();
                        WHERE("a.active_status != " + ActiveStatus.REMOVED);
                    }
                    if (critical.getOnline_status() != null)
                    {
                        AND();
                        WHERE("a.online_status = " + critical.getOnline_status());
                    }
                }
            };

            LOGGER.info("count gws by critical sql {}", sql.toString());
            return sql.toString();
        }

        public String getGwsByRegion(String regionId)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("a.*");
                    FROM("tbl_edge_gw_info as a");
                    FROM("tbl_region_bind_info as c");
                    AND();
                    WHERE(String.format("c.region_id =\'%s\'", regionId));
                    AND();
                    WHERE("a.node_id = c.node_id");
                    AND();
                    WHERE("a.active_status != " + ActiveStatus.REMOVED);
                }
            };
            LOGGER.info("get gws  {}", sql.toString());
            return sql.toString();
        }

        public String getRegionIdsByGwNodeId(String nodeId)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("a.region_id");
                    FROM("tbl_region_info as a");
                    FROM("tbl_region_bind_info as c");
                    AND();
                    AND();
                    WHERE(String.format("c.node_id =\'%s\'", nodeId));
                    AND();
                    WHERE("a.region_id = c.region_id");
                }
            };
            LOGGER.debug("get regions  {}", sql.toString());
            return sql.toString();
        }


        public String getRegionBinds(String regionId)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("a.node_id as id, a.node_name as name");
                    FROM("tbl_edge_gw_info as a");
                    FROM("tbl_region_bind_info as b");
                    AND();
                    AND();
                    WHERE(String.format("b.region_id =\'%s\'", regionId));
                    AND();
                    WHERE("a.node_id = b.node_id");
                    AND();
                    WHERE(String.format("a.active_status = %d", ActiveStatus.ACITVE));
                }
            };
            LOGGER.debug("get region bind gws  {}", sql.toString());
            return sql.toString();
        }

        public String getRegionInfosByGwId(String nodeId)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("a.region_id as id, a.region_name as name");
                    FROM("tbl_region_info as a");
                    FROM("tbl_region_bind_info as b");
                    AND();
                    WHERE(String.format("b.node_id ='%s'", nodeId));
                    AND();
                    WHERE("a.region_id = b.region_id");
                    AND();
                    WHERE(String.format("a.status = %d", ActiveStatus.ACITVE));
                }
            };
            LOGGER.debug("get gw bind regions  {}", sql.toString());
            return sql.toString();
        }

        public String countEdgeActiveCores()
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("sum(cpu_logic_num)");
                    FROM("tbl_edge_compute_info");
                    AND();
                    WHERE(String.format("active_status = %d", ActiveStatus.ACITVE));
                }
            };
            LOGGER.debug("get active core num  {}", sql.toString());
            return sql.toString();
        }
    }

}
