package com.lnjoying.justice.ecrm.process.service;

import com.lnjoying.justice.ecrm.process.processor.UpgradeProcessor;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("upgradeProcessStrategy")
public class UpgradeProcessStrategy extends ProcessMultiStrategy
{
	public UpgradeProcessStrategy(String desc, int threadNum, int maxQueueSize)
	{
		super(desc, threadNum, maxQueueSize);
	}

	private static Logger LOGGER = LogManager.getLogger();

	@Autowired
	private UpgradeProcessor upgradeProcessor;

	public UpgradeProcessStrategy()
	{
		super("upgrade process service",1, 10000);
		LOGGER.info("init upgrade process strategy");
	}

	@PostConstruct
	public void start()
	{
		super.start(() -> upgradeProcessor,1, "upgrade");
	}
}
