package com.lnjoying.justice.servicegw.service;

import com.lnjoying.justice.servicegw.common.ProcessorName;
import com.lnjoying.justice.servicegw.service.processor.CloudServerProcessor;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("cloudServerStrategy")
public class CloudServerProcessStrategy extends ProcessMultiStrategy
{
	public CloudServerProcessStrategy(String desc, int threadNum, int maxQueueSize)
	{
		super(desc, threadNum, maxQueueSize);
	}
	
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private CloudServerProcessor cloudServerProcessor;
	
	public CloudServerProcessStrategy()
	{
		super("cloud server message service",1);
		LOGGER.info("init cloud server process strategy");
	}
	
	@PostConstruct
	public void start()
	{
		LOGGER.info("start processor");
		
		super.start(() -> cloudServerProcessor,1,   ProcessorName.CLOUD_SERVER, 0);
	}
}
