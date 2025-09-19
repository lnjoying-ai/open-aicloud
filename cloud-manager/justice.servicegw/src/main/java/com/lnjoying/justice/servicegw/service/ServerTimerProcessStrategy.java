package com.lnjoying.justice.servicegw.service;

import com.lnjoying.justice.servicegw.service.processor.TunnelProcessor;
import com.micro.core.process.service.ScheduleProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("clusterTimerProcessStrategy")
public class ServerTimerProcessStrategy extends ScheduleProcessStrategy
{
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
    TunnelProcessor tunnelProcessor;
	
	//60s
	Integer cycle = 10000;
	
	public ServerTimerProcessStrategy()
	{
		super("spec timer service",1);
		LOGGER.info("init SpecTimerProcessStrategy");
	}
	
	@PostConstruct
	public void start()
	{
		LOGGER.info("start processor");
		
		{
			super.start(() -> tunnelProcessor, 1000, cycle, null);
			
		}
	}
}
