package com.lnjoying.justice.ecrm.config;

import com.lnjoying.justice.ecrm.config.model.AdminDivisionBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class EcrmConfigYaml
{
	@PostConstruct
	void creatEcrmParamBean()
	{
		loadDivisionnYaml();
	}

	Map<String, AdminDivisionBean> adminDivisionBeanMap = new HashMap<>();

	List<AdminDivisionBean> adminDivisionBeanList = null;

	void setAdminDivisionMap(List<AdminDivisionBean> adminDivisionBeanList)
	{
		for (AdminDivisionBean adminDivisionBean : adminDivisionBeanList)
		{
			adminDivisionBeanMap.put(adminDivisionBean.getAdcode(), adminDivisionBean);
			if (adminDivisionBean.getDivision() != null && ! adminDivisionBean.getDivision().isEmpty())
			{
				setAdminDivisionMap(adminDivisionBean.getDivision());
			}
		}
	}

	void loadDivisionnYaml()
	{
		String path = System.getProperty("lj_config") + "/admin_division/cn.yaml";
		try
		{
			Yaml yaml = new Yaml();
			FileReader fileReader = new FileReader(path);
			adminDivisionBeanList = yaml.load(fileReader);
			fileReader.close();

			if (adminDivisionBeanList != null && !adminDivisionBeanList.isEmpty())
			{
				setAdminDivisionMap(adminDivisionBeanList);
			}

//			yaml.dump(adminDivisionBeanList, new FileWriter(path));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	@Bean
	List<AdminDivisionBean> createAdminDivisionListBean()
	{
		return adminDivisionBeanList;
	}

	@Bean
	Map<String, AdminDivisionBean>  createAdminDivisionMapBean()
	{
		return adminDivisionBeanMap;
	}

}
