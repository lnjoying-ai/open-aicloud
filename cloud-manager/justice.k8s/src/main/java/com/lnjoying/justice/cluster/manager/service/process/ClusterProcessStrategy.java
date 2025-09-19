package com.lnjoying.justice.cluster.manager.service.process;

import com.lnjoying.justice.cluster.manager.common.ProcessorName;
import com.lnjoying.justice.cluster.manager.service.process.processor.BuildPlanProcessor;
import com.lnjoying.justice.cluster.manager.service.process.processor.ClusterProcessor;
import com.lnjoying.justice.cluster.manager.service.process.processor.DeployProcessor;
import com.lnjoying.justice.cluster.manager.service.process.processor.MonitorProcessor;
import com.micro.core.process.service.ProcessMultiStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("clusterProcessStrategy")
public class ClusterProcessStrategy extends ProcessMultiStrategy
{
	public ClusterProcessStrategy(String desc, int threadNum, int maxQueueSize)
	{
		super(desc, threadNum, maxQueueSize);
	}
	
	private static Logger LOGGER = LogManager.getLogger();
	
	@Autowired
	private DeployProcessor deployProcessor;
	
	@Autowired
	private BuildPlanProcessor buildPlanProcessor;
	
	@Autowired
	private ClusterProcessor clusterProcessor;

	@Autowired
	private MonitorProcessor monitorProcessor;
	
	public ClusterProcessStrategy()
	{
		super("cluster manager message service",4, 10000);
		LOGGER.info("init cluster process strategy");
	}
	
	@PostConstruct
	public void start()
	{
		super.start(() -> deployProcessor,1,    ProcessorName.DEPLOY);
		super.start(() -> buildPlanProcessor,1, ProcessorName.PLAN);
		super.start(() -> clusterProcessor,1,   ProcessorName.CLUSTER);
		super.start(() -> monitorProcessor,1,   ProcessorName.MONITOR);
	}
}
