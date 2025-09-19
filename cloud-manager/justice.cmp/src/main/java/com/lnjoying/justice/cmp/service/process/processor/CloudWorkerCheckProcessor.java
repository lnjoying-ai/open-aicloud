package com.lnjoying.justice.cmp.service.process.processor;

import com.lnjoying.justice.cmp.common.InstanceState;
import com.lnjoying.justice.cmp.common.RedisCache;
import com.lnjoying.justice.cmp.db.model.TblCloudAgentInfo;
import com.lnjoying.justice.cmp.db.model.TblCloudAgentInfoExample;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfStateInfos;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CloudWorkerCheckProcessor extends AbstractRunnableProcessor
{
	private static Logger LOGGER = LogManager.getLogger();

	long ifLifeCycle = 20*60*1000;

	public CloudWorkerCheckProcessor()
	{
		LOGGER.info("init cloud worker state check processor");
	}

	@Autowired
	private CloudRepository cloudRepository;

	@Override
	public void start()
	{
		LOGGER.info("start cloud worker state check process");
	}

	@Override
	public void stop()
	{
		LOGGER.info("stop cloud worker state check process");
	}

	@Override
	public void run()
	{
		LOGGER.info("analysis cloud worker if state info.");
		try
		{
			checkWorkerConnecAndFlushToDB();
			analysisWorkerIFState();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("analysis failed. {}", e.getMessage());
		}
	}

	void checkWorkerConnecAndFlushToDB()
	{
		try
		{
		    LOGGER.info("sync worker connect status from redis to db.");
    		long currentTime  = System.currentTimeMillis();
    		List<String> edges = RedisUtil.keys(RedisCache.WORKER_IF + "*");
    		if (edges == null || edges.isEmpty())
    		{
    			LOGGER.info("no worker to sync");
    			return;
    		}

    		for (String edgeKey : edges)
    		{
    			Object ifObj = RedisUtil.oget(edgeKey,"");
    			if (ifObj == null) continue;

    			EdgeWorkerIfStateInfos workerIfStates = (EdgeWorkerIfStateInfos) ifObj;
    			String workerId = workerIfStates.getWorkerId();

    			int status = OnlineStatus.OFFLINE;
    			for (Map.Entry<String, EdgeWorkerIfState> entry : workerIfStates.getEdgeWorkerIfStateMap().entrySet())
    			{
    				if (entry.getValue().getStatus() != OnlineStatus.ONLINE)
    				{
    					continue;
    				}

    				if (entry.getValue().getStatus() == OnlineStatus.ONLINE)
    				{
    					long timeInterval = currentTime - entry.getValue().getLastTime();
    					LOGGER.info(workerId + " last  time interval: " + timeInterval);
    					if (timeInterval > ifLifeCycle)
    					{
    						LOGGER.info("{} last state is unknow. may be disconnected. remove it.", workerId);
    						entry.getValue().setStatus(OnlineStatus.OFFLINE);
    					}
    				}

    				status = entry.getValue().getStatus();
    			}

    			RedisUtil.oset(RedisCache.WORKER_IF, workerId, workerIfStates);


    			TblCloudAgentInfoExample example = new TblCloudAgentInfoExample();
    			TblCloudAgentInfoExample.Criteria criteria = example.createCriteria();
    			criteria.andStatusNotIn(InstanceState.removeStatusList);
    			criteria.andWorkerIdEqualTo(workerId);
    			List<TblCloudAgentInfo> tblCloudAgentInfos = cloudRepository.getCloudAgents(example);
    			if (tblCloudAgentInfos == null || tblCloudAgentInfos.size() == 0)
    			{
    				LOGGER.info("worker info is null in db. for {}", workerId);
    				RedisUtil.delete(edgeKey);
    				continue;
    			}

    			TblCloudAgentInfo tblCloudAgentInfo	= tblCloudAgentInfos.get(0);
    			if (tblCloudAgentInfo.getOnlineStatus() == null || tblCloudAgentInfo.getOnlineStatus() != status)
    			{
    				LOGGER.info("sync worker: {} status from {} to {}", workerId, tblCloudAgentInfo.getOnlineStatus(), status);
    				tblCloudAgentInfo.setOnlineStatus(status);
    				tblCloudAgentInfo.setUpdateTime(Utils.buildDate(currentTime));
    				cloudRepository.updateCloudAgentInfo(tblCloudAgentInfo);
    			}
    		}
		} catch (Exception e)
		{
		    e.printStackTrace();
		    LOGGER.error("checkWorkerConnecAndFlushToDB error {}", e.getMessage());
		}
	}

	void analysisWorkerIFState()
	{
		try
		{
		    long currentTime  = System.currentTimeMillis();
    		List<String> regions = RedisUtil.keys(RedisCache.REGION_GW_WORKER + "*:*");
    		if (regions == null || regions.isEmpty())
    		{
    			LOGGER.info("no worker to check");
    			return;
    		}

    		for (String region : regions)
    		{
    			Set<String> workers = RedisUtil.smembers(region, "");
    			for (String workerId : workers)
    			{
    				Object ifObj = RedisUtil.oget(RedisCache.WORKER_IF, workerId);
    				if (ifObj == null)
    				{
    					RedisUtil.srem(region, "", workerId);
    					continue;
    				}

    				EdgeWorkerIfStateInfos workerIfStates = (EdgeWorkerIfStateInfos) ifObj;
    				boolean remFromSite = true;
    				for (Map.Entry<String, EdgeWorkerIfState> entry : workerIfStates.getEdgeWorkerIfStateMap().entrySet())
    				{
    					if (region.contains(entry.getKey()) && entry.getValue().getStatus() == OnlineStatus.ONLINE)
    					{
    						remFromSite = false;
    						break;
    					}
    				}

    				if (remFromSite == true)
    				{
    					RedisUtil.srem(region, "", workerIfStates.getWorkerId());
    				}
    			}
    		}
		} catch (Exception e)
		{
		    e.printStackTrace();
		    LOGGER.error("analysisWorkerIFState error {}", e.getMessage());
		}
	}
}
