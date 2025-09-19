package com.lnjoying.justice.ecrm.db.mapper;

import com.lnjoying.justice.schema.entity.stat.NodeStatusMetrics;
import com.lnjoying.justice.schema.entity.stat.RegionNodeMetrics;
import com.lnjoying.justice.schema.entity.stat.SiteNodeMetrics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StatMapper
{
    NodeStatusMetrics getAllNodeStatusMetrics(@Param("categories") List<String> categories, @Param("bpLabel") String bpLabel, @Param("bpId") String bpId, @Param("userLabel") String userLabel, @Param("userId") String userId);

    List<SiteNodeMetrics> getSiteStatusMetrics(@Param("siteIds") List<String> siteIds, @Param("categories") List<String> categories, @Param("bpLabel") String bpLabel, @Param("bpId") String bpId, @Param("userLabel") String userLabel, @Param("userId") String userId);

    List<RegionNodeMetrics> getRegionStatusMetrics(@Param("regionIds")List<String> regionIds, @Param("categories") List<String> categories, @Param("bpLabel") String bpLabel, @Param("bpId") String bpId, @Param("userLabel") String userLabel, @Param("userId") String userId);
}
