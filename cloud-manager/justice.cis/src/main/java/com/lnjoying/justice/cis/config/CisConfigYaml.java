package com.lnjoying.justice.cis.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

//@Configuration
public class CisConfigYaml
{
	private static Logger LOGGER = LogManager.getLogger();
	@PostConstruct
	void createCisConfigBean()
	{
		loadWsConfig();
	}

	CisConfig cisConfig;

	void loadWsConfig()
	{
		String path = System.getProperty("lj_config") + "/cis_config.yaml";
		LOGGER.info("config path {}", path);

		try
		{
			Yaml yaml = new Yaml();
			FileReader fileReader = new FileReader(path);
			cisConfig = yaml.loadAs(fileReader, CisConfig.class);
			fileReader.close();
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

//	@PostConstruct
	@Bean("wsConfig")
	WSConfig createWSConfigBean()
	{
		return cisConfig.getWs();
	}
}
