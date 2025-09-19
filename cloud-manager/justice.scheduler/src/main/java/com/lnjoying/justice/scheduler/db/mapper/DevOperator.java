package com.lnjoying.justice.scheduler.db.mapper;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DevOperator
{
	@Select("select region_id from tbl_region_info where status!='-1'")
	List<String> getAllRegionId();

	@Select("select site_id from tbl_site_info where status!='-1'")
	List<String> getAllSiteId();

	@Select("select site_id from tbl_site_info where status!='-1' and region_id=#{regionId}")
	List<String> getSiteByRegionId(@Param("regionId") String regionId);
}
