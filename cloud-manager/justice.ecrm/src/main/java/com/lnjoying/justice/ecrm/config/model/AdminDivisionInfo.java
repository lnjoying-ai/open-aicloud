package com.lnjoying.justice.ecrm.config.model;

import lombok.Data;

@Data
public class AdminDivisionInfo
{
	String adcode;
	float min_longitude;
	float min_latitude;
	float max_longitude;
	float max_latitude;
	String cn_name;
	String en_name;
	int level;
	String parent;

	public void setInfo(AdminDivisionBean adminDivisionBean)
	{
		this.setAdcode(adminDivisionBean.getAdcode());
		this.setMin_latitude(adminDivisionBean.getMin_latitude());
		this.setMin_longitude(adminDivisionBean.getMin_longitude());
		this.setMax_latitude(adminDivisionBean.getMax_latitude());
		this.setMax_longitude(adminDivisionBean.getMax_longitude());
		this.setCn_name(adminDivisionBean.getCn_name());
		this.setEn_name(adminDivisionBean.getEn_name());
		this.setLevel(adminDivisionBean.getLevel());
		this.setParent(adminDivisionBean.getParent());
	}
}
