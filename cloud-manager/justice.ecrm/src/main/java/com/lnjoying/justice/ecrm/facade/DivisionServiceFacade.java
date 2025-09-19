package com.lnjoying.justice.ecrm.facade;

import com.lnjoying.justice.ecrm.config.model.AdminDivisionBean;
import com.lnjoying.justice.ecrm.config.model.AdminDivisionInfo;
import com.lnjoying.justice.ecrm.domain.dto.model.NationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DivisionServiceFacade
{
	@Autowired
	Map<String, AdminDivisionBean> cnAdminDivisionBeanMap;

	@Autowired
	List<AdminDivisionBean> cnAdminDivisionBeanList;

	public List<NationInfo> getNaitons()
	{
		List<NationInfo> nationInfoList = new ArrayList<>();
		NationInfo nationInfo = new NationInfo();
		nationInfo.setCn_name("中国");
		nationInfo.setEn_name("China");
		nationInfo.setNation("cn");
		nationInfoList.add(nationInfo);
		return nationInfoList;
	}


	Map<String, AdminDivisionBean> getAdminDivisionMap(String nation)
	{
		if (nation == null || nation.isEmpty() || nation.equals("cn"))
		{
			return cnAdminDivisionBeanMap;
		}
		return null;
	}

	List<AdminDivisionBean>  getAdminDivisionList(String nation)
	{
		if (nation == null || nation.isEmpty() || nation.equals("cn"))
		{
			return cnAdminDivisionBeanList;
		}
		return null;
	}

	public List<AdminDivisionInfo> getDivisions(String nation)
	{

		List<AdminDivisionBean> adminDivisionBeanList = getAdminDivisionList(nation);
		if (adminDivisionBeanList == null)
		{
			return null;
		}

		List<AdminDivisionInfo> adminDivisionInfos = new ArrayList<>();
		for (AdminDivisionBean ad : cnAdminDivisionBeanList)
		{
			AdminDivisionInfo adminDivisionInfo = new AdminDivisionInfo();
			adminDivisionInfo.setInfo(ad);
			adminDivisionInfos.add(adminDivisionInfo);
		}

		return adminDivisionInfos;
	}

	public List<AdminDivisionInfo> getDivisionByAdcode(String nation, String adcode)
	{
		Map<String, AdminDivisionBean> adminDivisionBeanMap = getAdminDivisionMap(nation);
		if (adminDivisionBeanMap == null)
		{
			return null;
		}

		AdminDivisionBean adminDivisionBean= adminDivisionBeanMap.get(adcode);
		if (adminDivisionBean == null)
		{
			return null;
		}

		List<AdminDivisionBean> adlist = adminDivisionBean.getDivision();
		if (adlist == null)
		{
			return null;
		}

		List<AdminDivisionInfo> adminDivisionInfos = new ArrayList<>();
		for (AdminDivisionBean ad : adlist)
		{
			AdminDivisionInfo adminDivisionInfo = new AdminDivisionInfo();
			adminDivisionInfo.setInfo(ad);
			adminDivisionInfos.add(adminDivisionInfo);
		}

		return adminDivisionInfos;
	}

	public void getChildAdcodes(String nation, String adcode, List<String> adcodeList)
	{
		Map<String, AdminDivisionBean> adminDivisionBeanMap = getAdminDivisionMap(nation);
		if (adminDivisionBeanMap == null)
		{
			return;
		}

		if (adcode == null)
		{
			getChildAdcodes(getAdminDivisionList(nation), adcodeList);
			return;
		}

		adcodeList.add(adcode);

		getChildAdcodes(adminDivisionBeanMap, adcode, adcodeList);
	}

	public void getChildAdcodes(Map<String, AdminDivisionBean> adminDivisionBeanMap, String adcode, List<String> adcodeList)
	{
		AdminDivisionBean adminDivisionBean= adminDivisionBeanMap.get(adcode);
		if (adminDivisionBean == null)
		{
			return;
		}

		List<AdminDivisionBean> adlist = adminDivisionBean.getDivision();
		if (adlist == null)
		{
			return;
		}

		getChildAdcodes(adlist, adcodeList);
	}

	public void getChildAdcodes(List<AdminDivisionBean> division, List<String> adcodeList)
	{
		for (AdminDivisionBean ad : division)
		{
			adcodeList.add(ad.getAdcode());
			if (ad.getDivision() != null)
			{
				getChildAdcodes(ad.getDivision(), adcodeList);
			}
		}
	}
}
