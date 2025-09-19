package com.lnjoying.justice.aos.db.mapper;

import com.lnjoying.justice.aos.common.TemplateSearchCritical;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.domain.model.TemplateNameBasic;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

@Mapper
public interface AosOperator
{
    @Select("select stack_id from tbl_stack_info where status=#{status} limit 100")
    public List<String> getStackIdsByStatus(@Param("status")Integer status);

    @SelectProvider(type = AosProvider.class, method = "getServiceIdsByStackId")
    public List<String> getServiceIdsByStackId(String stackId);

    @SelectProvider(type = AosProvider.class, method = "getInstIdByServiceId")
    public List<String> getInstIdByServiceId(String serviceId);

    @SelectProvider(type = AosProvider.class, method = "getStackIdsByStatusWithTimeAndLtFailNum")
    public List<String> getStackIdsByStatusWithTimeAndLtFailNum(Integer status, String timeParam, Integer failNum);

    @SelectProvider(type = AosProvider.class, method = "getStackIdsByStatusWithTimeAndGteFailNum")
    public List<String> getStackIdsByStatusWithTimeAndGteFailNum(Integer status, String timeParam, Integer failNum);

    @Select("select count(1) from (select name,user_id,bp_id from tbl_stack_template_info where status != #{exStatus} group by name, user_id, bp_id) templateCount")
    public long countTemplates(@Param("exStatus")Integer exStatus);

    @SelectProvider(type = AosProvider.class, method = "getTemplateBasic")
    public List<TemplateNameBasic> getTemplateBasic(int exStatus, TemplateSearchCritical searchCritical);

    @SelectProvider(type = AosProvider.class, method = "getStackIdsByStatusWithTime")
    public List<String> getStackIdsByStatusWithTime(Integer status, String timeParam);

    @SelectProvider(type = AosProvider.class, method = "getStackIdsLostStatusGTWithUpdateTime")
    List<String> getStackIdsLostStatusGTWithUpdateTime(String timeParam, Integer eventType);

    @SelectProvider(type = AosProvider.class, method = "getStackIdsLostStatusGT")
    List<String> getStackIdsLostStatusGT(String timeParam, Integer eventType);

    @Select("select spec_id from tbl_stack_spec_info where status=#{status} limit 100")
    public List<String> getSpecIdsByStatus(@Param("status")Integer status);
    
    @SelectProvider(type = AosProvider.class, method = "getStackInfo")
    public List<String> getStackInfo(@Param("spec_id")String spec_id, @Param("status")List<Integer> status, @Param("resLevel")String resLevel, @Param("resId")String resId);
    
    
    @Select("select spec_id from tbl_stack_spec_info where status=#{status}  and stack_type='daemonset' limit 100")
    public List<String> getDaemonsetSpecIdsByStatus(@Param("status")Integer status);

    class AosProvider
    {
        private static Logger LOGGER = LogManager.getLogger();

        public String getStackIdsByStatusWithTimeAndLtFailNum(Integer status, String timeParam, Integer failNum)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("stack_id");
                    FROM("tbl_stack_info");
                    WHERE(String.format("status=%d", status));
                    WHERE(String.format("fail_num<%d", failNum));
                    WHERE(String.format("update_time + '%s' < now()", timeParam));
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack id list {}" , ret);
            }

            return ret;
        }

        public String getStackIdsByStatusWithTimeAndGteFailNum(Integer status, String timeParam, Integer failNum)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("stack_id");
                    FROM("tbl_stack_info");
                    WHERE(String.format("status=%d", status));
                    WHERE(String.format("fail_num>=%d", failNum));
                    WHERE(String.format("update_time + '%s' < now()", timeParam));
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack id list {}" , ret);
            }

            return ret;
        }

        public String getServiceIdsByStackId(String stackId)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("service_id");
                    FROM("tbl_stack_service_info");
                    WHERE(String.format("stack_id='%s'", stackId));
                }
            };
            String ret = sql.toString();
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack id list " + ret);
            }

            return ret;
        }

        public String getInstIdByServiceId(String serviceId)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("inst_id");
                    FROM("tbl_stack_inst_info");
                    WHERE(String.format("service_id='%s'", serviceId));
                }
            };
            String ret = sql.toString();

            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get service id list {}" , ret);
            }

            return ret;
        }

        public String getTemplateBasic(int exStatus, TemplateSearchCritical searchCritical)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("name, user_id as userId, bp_id as bpId");
                    FROM("tbl_stack_template_info");
                    WHERE(String.format("status != %d", exStatus));
                    if (! StringUtils.isEmpty(searchCritical.getName()))
                    {
                        WHERE(String.format("name='%s'", searchCritical.getName()));
                    }

                    if (! StringUtils.isEmpty(searchCritical.getUserId()))
                    {
                        WHERE(String.format("user_id='%s'", searchCritical.getUserId()));
                    }

                    if (! StringUtils.isEmpty(searchCritical.getBpId()))
                    {
                        WHERE(String.format("bp_id='%s'", searchCritical.getBpId()));
                    }

                    GROUP_BY("name, user_id, bp_id");
                    if (searchCritical.getPageNum() > 0)
                    {
                        int begin = ((searchCritical.getPageNum()-1) * searchCritical.getPageSize());
                        LIMIT(searchCritical.getPageSize());
                        OFFSET(begin);
                    }
                }
            };

            String ret = sql.toString();
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get template id list {}" , ret);
            }

            return ret;
        }

        public String getStackIdsByStatusWithTime(Integer status, String timeParam)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("stack_id");
                    FROM("tbl_stack_info");
                    WHERE(String.format("status=%d", status));
                    WHERE(String.format("update_time + '%s' < now()", timeParam));
                    LIMIT(100);
                }
            };
            String ret = sql.toString();
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack id list {}" , ret);
            }

            return ret;
        }

        public String getStackIdsLostStatusGTWithUpdateTime(String timeParams, Integer eventType)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("stack_id");
                    FROM("tbl_stack_info");
                    WHERE("status = 105");
                    WHERE(String.format("event_type = %d", eventType));
                    WHERE(String.format("report_time + '%s' < now()", timeParams));
                    WHERE(String.format("update_time + '%s' < now()", timeParams));
                    ORDER_BY("update_time");
                    LIMIT(100);
                }
            };
            String ret = sql.toString();

            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack id list {}" , ret);
            }

            return ret;
        }

        public String getStackIdsLostStatusGT(String timeParam, Integer eventType)
        {
            SQL sql = new SQL()
            {
                {
                    SELECT("stack_id");
                    FROM("tbl_stack_info");
                    WHERE("status = 105");
                    WHERE(String.format("event_type = %d", eventType));
                    WHERE(String.format("report_time + '%s' < now()", timeParam));
                    ORDER_BY("update_time");
                    LIMIT(100);
                }
            };
            String ret = sql.toString();

            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack id list {}" , ret);
            }

            return ret;
        }
    
        public String getStackInfo(@Param("spec_id")String spec_id, @Param("status")List<Integer> status, @Param("resLevel")String resLevel, @Param("resId")String resId)
        {
            String statusList = StringUtils.join(status,",");
            String finalStatusList = Utils.buildStr("(", statusList, ")");
            SQL sql = new SQL()
            {
                {
                    SELECT("*");
                    FROM("tbl_stack_info");
                    WHERE(String.format("status not in %s", finalStatusList));
                    WHERE(String.format("spec_id = \'%s\'", spec_id));
                    WHERE(String.format("dst_node ->>\'%s\'=\'%s\'",resLevel, resId));
                }
            };
            String ret = sql.toString();
        
            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("get stack list {}" , ret);
            }
        
            return ret;
        }
        
    }

}
