package com.lnjoying.justice.servicegw.service;

import com.lnjoying.justice.servicegw.common.ProcessorName;
import com.lnjoying.justice.servicegw.service.processor.ProxyServerProcessor;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("clusterServerStrategy")
public class ServerProcessStrategy extends ProcessMultiStrategy
{
	public ServerProcessStrategy(String desc, int threadNum, int maxQueueSize)
	{
		super(desc, threadNum, maxQueueSize);
	}
	
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private ProxyServerProcessor proxyServerProcessor;
	
	public ServerProcessStrategy()
	{
		super("cluster server message service",1);
		LOGGER.info("init cluster process strategy");
	}
	
	@PostConstruct
	public void start()
	{
		LOGGER.info("start processor");
		
		super.start(() -> proxyServerProcessor,1,   ProcessorName.PROXY_SERVER, 0);
	}
}
