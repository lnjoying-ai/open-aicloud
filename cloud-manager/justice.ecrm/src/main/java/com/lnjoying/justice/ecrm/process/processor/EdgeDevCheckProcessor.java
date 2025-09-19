package com.lnjoying.justice.ecrm.process.processor;

import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.DeepCopyUtils;
import com.lnjoying.justice.commonweb.util.TemplateEngineUtils;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.db.model.TblEdgeComputeInfo;
import com.lnjoying.justice.ecrm.db.model.TblEdgeGwInfo;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.GwRepository;
import com.lnjoying.justice.ecrm.handler.actiondescription.i18n.zh_cn.EdgeActionDescriptionTemplate;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.EdgeResourceSupervisor;
import com.lnjoying.justice.ecrm.handler.resourcesupervisor.statedict.EdgeResourceStateDesProvider;
import com.lnjoying.justice.schema.common.ChannelState;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeDevIfStateInfos;
import com.lnjoying.justice.schema.entity.edgeif.EdgeGwIfState;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class EdgeDevCheckProcessor extends AbstractRunnableProcessor
{
	private static Logger LOGGER = LogManager.getLogger();

	long ifLifeCycle = 20*60*1000;
	long longNoStateLifeCycle = 40*60*1000;
	public EdgeDevCheckProcessor()
	{
		LOGGER.info("init edge dev state check processor");
	}

	@Autowired
	EdgeRepository edgeRepo;

	@Autowired
	GwRepository gwRepo;

	@Autowired
	private EdgeResourceSupervisor edgeResourceSupervisor;

	@Override
	public void start()
	{
		LOGGER.info("start edge dev state check process");
	}

	@Override
	public void stop()
	{
		LOGGER.info("stop render task process");
	}

	@Override
	public void run()
	{
		LOGGER.info("analysis edge dev if state info.");
		try
		{
			checkEdgeConnecAndFlushToDB();
			analysisDevIFState();
			syncGWConnectInfoToDB();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			LOGGER.error("analysis failed. {}", e.getMessage());
		}
	}

	void syncGWConnectInfoToDB()
	{
		try
		{
		    LOGGER.info("sync gw connect status to db");
    		List<TblEdgeGwInfo> tblEdgeGwInfoList = gwRepo.getAllGws();
    		if (tblEdgeGwInfoList == null || tblEdgeGwInfoList.isEmpty())
    		{
    			return;
    		}

    		long current = System.currentTimeMillis();

    		for (TblEdgeGwInfo gwInfo : tblEdgeGwInfoList)
    		{
    			boolean updatedb = false;
    			Object object = RedisUtil.oget(RedisCache.GW_IF, gwInfo.getNodeId());
    			if (object == null)
    			{
    				if (gwInfo.getOnlineStatus() == null || gwInfo.getOnlineStatus() != OnlineStatus.OFFLINE)
    				{
    					gwInfo.setOnlineStatus(OnlineStatus.OFFLINE);
    					updatedb = true;
    				}

    			}
    			else
    			{
    				EdgeGwIfState edgeGwIfState = (EdgeGwIfState) object;

    				if (gwInfo.getOnlineStatus() == null)
    				{
    					if (edgeGwIfState.getStatus() == ChannelState.CONNECTED.getValue() && gwInfo.getOnlineStatus() != OnlineStatus.ONLINE)
    					{
    						gwInfo.setOnlineStatus(OnlineStatus.ONLINE);
    						updatedb = true;
    					}
    					else if (edgeGwIfState.getStatus() == ChannelState.DISCONNECTED.getValue() && gwInfo.getOnlineStatus() != OnlineStatus.OFFLINE)
    					{
    						gwInfo.setOnlineStatus(OnlineStatus.OFFLINE);
    						updatedb = true;
    					}
    				}
    			}

    			if (updatedb == true)
    			{
    				gwInfo.setUpdateTime(Utils.buildDate(current));
    				gwRepo.updateGw(gwInfo);
    			}
    		}
		} catch (Exception e)
		{
		    e.printStackTrace();
		    LOGGER.error("syncGWConnectInfoToDB error {}", e.getMessage());
		}
	}

	void checkEdgeConnecAndFlushToDB()
	{
		try
		{
		    LOGGER.info("sync edge connect status from redis to db.");
    		long currentTime  = System.currentTimeMillis();
    		List<String> edges = RedisUtil.keys(RedisCache.EDGE_IF + "*");
    		if (edges == null || edges.isEmpty())
    		{
    			LOGGER.info("no edge to sync");
    			return;
    		}

    		for (String edgeKey : edges)
    		{
    			Object ifObj = RedisUtil.oget(edgeKey,"");
    			if (ifObj == null) continue;

    			EdgeDevIfStateInfos devIfStates = (EdgeDevIfStateInfos) ifObj;
    			String nodeId = devIfStates.getNodeId();

    			int status = OnlineStatus.OFFLINE;
    			for (Map.Entry<String, EdgeDevIfState> entry : devIfStates.getEdgeDevIfStateMap().entrySet())
    			{
    				if (entry.getValue().getStatus() != OnlineStatus.ONLINE)
    				{
    					continue;
    				}

    				if (entry.getValue().getStatus() == OnlineStatus.ONLINE)
    				{
    					long timeInterval = currentTime - entry.getValue().getLastTime();
    					LOGGER.info("{} last  time interval: {}", nodeId,timeInterval);
    					if (timeInterval > ifLifeCycle)
    					{
    						LOGGER.info("{} last state is unknow. may be disconnected. remove it.", nodeId);
    						entry.getValue().setStatus(OnlineStatus.OFFLINE);
    					}
    				}

    				status = entry.getValue().getStatus();
    			}

    			RedisUtil.oset(RedisCache.EDGE_IF, nodeId, devIfStates);


    			TblEdgeComputeInfo tblEdgeComputeInfo = edgeRepo.getEdgeNode(nodeId);
    			if (tblEdgeComputeInfo == null)
    			{
    				LOGGER.info("edge info is null in db. for {}", nodeId);
    				RedisUtil.delete(edgeKey);
    				continue;
    			}

				TblEdgeComputeInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblEdgeComputeInfo);
				long updateInterval = currentTime - tblEdgeComputeInfo.getUpdateTime().getTime();
    			if (tblEdgeComputeInfo.getActiveStatus() == ActiveStatus.REMOVED)
    			{
    				LOGGER.info("edge have been removed in db. for {}", nodeId);
    				RedisUtil.delete(edgeKey);
    				continue;
    			}

    			if (tblEdgeComputeInfo.getOnlineStatus() == null ||  tblEdgeComputeInfo.getOnlineStatus() != status || updateInterval>ifLifeCycle)
    			{
    				LOGGER.info("sync node: {} status from {} to {}", nodeId, tblEdgeComputeInfo.getOnlineStatus(), status);
    				tblEdgeComputeInfo.setOnlineStatus(status);
    				tblEdgeComputeInfo.setUpdateTime(Utils.buildDate(currentTime));
    				edgeRepo.updateEdge(tblEdgeComputeInfo);
					//记录资源更新事件
					publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "checkEdgeConnecAndFlushToDB");

    				if (tblEdgeComputeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
    				{
    					//online edge set, use for scheduling
    					RedisUtil.sadd(RedisCache.ALL_ONLINE_EDGE, "", tblEdgeComputeInfo.getNodeId());
    					RedisUtil.sadd(RedisCache.REGION_ONLINE_EDGE, tblEdgeComputeInfo.getRegionId(), tblEdgeComputeInfo.getNodeId());
    					RedisUtil.sadd(RedisCache.SITE_ONLINE_EDGE, tblEdgeComputeInfo.getSiteId(), tblEdgeComputeInfo.getNodeId());
    				}
    				else
    				{
    					//online edge set, use for scheduling
    					RedisUtil.srem(RedisCache.ALL_ONLINE_EDGE, "", tblEdgeComputeInfo.getNodeId());
    					RedisUtil.srem(RedisCache.REGION_ONLINE_EDGE, tblEdgeComputeInfo.getRegionId(), tblEdgeComputeInfo.getNodeId());
    					RedisUtil.srem(RedisCache.SITE_ONLINE_EDGE, tblEdgeComputeInfo.getSiteId(), tblEdgeComputeInfo.getNodeId());
    				}
    			}

    		}
			//获取在线状态但长时间未更新的节点列表
			List<TblEdgeComputeInfo> longUnupdatedOnlineEdgeList = edgeRepo.getEdges(edgeRepo.buildEdgeOnlineStatusWithUnchangedSinceExample(longNoStateLifeCycle));
			if (!CollectionUtils.isEmpty(longUnupdatedOnlineEdgeList))
			{
				for (TblEdgeComputeInfo beforeUpdateEntity : longUnupdatedOnlineEdgeList)
				{
					TblEdgeComputeInfo record = new TblEdgeComputeInfo();
					record.setNodeId(beforeUpdateEntity.getNodeId());
					record.setOnlineStatus(OnlineStatus.OFFLINE);
					edgeRepo.updateEdgeSelective(record);
					TblEdgeComputeInfo tblEdgeComputeInfo = DeepCopyUtils.deepCopy(beforeUpdateEntity);
					tblEdgeComputeInfo.setOnlineStatus(record.getOnlineStatus());
					//记录资源更新事件
					publishEdgeComputeInfoUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, "checkEdgeConnecAndFlushToDB");
				}
			}
		} catch (Exception e)
		{
		    e.printStackTrace();
		    LOGGER.error("checkEdgeConnecAndFlushToDB error {}", e.getMessage());
		}
	}

	private void publishEdgeComputeInfoUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
	{
		if (tblEdgeComputeInfo == null || beforeUpdateEntity == null)
		{
			LOGGER.debug("tblEdgeComputeInfo或beforeUpdateEntity为空, 跳过此次发布节点更新事件!");
			return;
		}

		if (!Objects.equals(tblEdgeComputeInfo.getActiveStatus(), beforeUpdateEntity.getActiveStatus()))
		{
			publishEdgeComputeInfoActiveStatusUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, action);
			return;
		} else if (!Objects.equals(tblEdgeComputeInfo.getOnlineStatus(), beforeUpdateEntity.getOnlineStatus()))
		{
			publishEdgeComputeInfoOnlineStatusUpdateEvent(tblEdgeComputeInfo, beforeUpdateEntity, action);
			return;
		}

		try
		{
			edgeResourceSupervisor.publishUpdateEvent("节点更新", null, false,
					beforeUpdateEntity, tblEdgeComputeInfo, action,
					TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE,
							false,
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
									beforeUpdateEntity.getNodeName())));
		} catch (Exception e)
		{
			LOGGER.error("发布节点更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	private void publishEdgeComputeInfoActiveStatusUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
	{
		try
		{
			Map<Integer, BizModelStateInfo> activeStatusDesDict = EdgeResourceStateDesProvider.INSTANCE.getStateDesDict().get(EdgeResourceStateDesProvider.ACTIVE_STATUS_FIELD);
			BizModelStateInfo stateInfo = activeStatusDesDict.get(tblEdgeComputeInfo.getActiveStatus());
			edgeResourceSupervisor.publishUpdateEvent("节点激活状态更新", null, true,
					beforeUpdateEntity, tblEdgeComputeInfo, action,
					TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE_ACTIVE_STATUS,
							false,
							TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
									.map(x -> x.getCnDescription())
									.orElse(Optional.ofNullable(tblEdgeComputeInfo.getActiveStatus())
											.map(x -> x.toString())
											.orElse(""))),
							TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
									tblEdgeComputeInfo.getNodeName())));
		} catch (Exception e)
		{
			LOGGER.error("发布节点激活状态更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}

	}

	private void publishEdgeComputeInfoOnlineStatusUpdateEvent(TblEdgeComputeInfo tblEdgeComputeInfo, TblEdgeComputeInfo beforeUpdateEntity, String action)
	{
		try
		{
			Map<Integer, BizModelStateInfo> onlineStatusDesDict = EdgeResourceStateDesProvider.INSTANCE.getStateDesDict().get(EdgeResourceStateDesProvider.ONLINE_STATUS_FIELD);
			BizModelStateInfo stateInfo = onlineStatusDesDict.get(tblEdgeComputeInfo.getOnlineStatus());
			edgeResourceSupervisor.publishUpdateEvent("节点在线状态更新", beforeUpdateEntity.getNodeName(), false,
					beforeUpdateEntity, tblEdgeComputeInfo, action,
					TemplateEngineUtils.render0(EdgeActionDescriptionTemplate.Descriptions.UPDATE_NODE_ONLINE_STATUS,
							false,
							TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
									.map(x -> x.getCnDescription())
									.orElse(Optional.ofNullable(tblEdgeComputeInfo.getOnlineStatus())
											.map(x -> x.toString())
											.orElse("")))));
		} catch (Exception e)
		{
			LOGGER.error("发布节点在线状态更新事件失败! stackTrace:{}, errorMessage:{}",
					ExceptionUtils.getStackTrace(e), e.getMessage());
		}
	}

	void analysisDevIFState()
	{
		try
		{
		    long currentTime  = System.currentTimeMillis();
    		//site pattern ECRM:SITE:{region_id}:{site_id}:{gw_id}
    		List<String> sites = RedisUtil.keys(RedisCache.SITE_GW_EDGE + "*:*:*");
    		if (sites == null || sites.isEmpty())
    		{
    			LOGGER.info("no site to check");
    			return;
    		}

    		for (Object obj : sites)
    		{
    			String site = (String) obj;
    			Set<String> nodes = RedisUtil.smembers(site, "");
    			for (String nodeId : nodes)
    			{
    				Object ifObj = RedisUtil.oget(RedisCache.EDGE_IF, nodeId);
    				if (ifObj == null)
    				{
    					RedisUtil.srem(site, "", nodeId);
    					continue;
    				}

    				EdgeDevIfStateInfos devIfStates = (EdgeDevIfStateInfos) ifObj;
    				boolean remFromSite = true;
    				for (Map.Entry<String, EdgeDevIfState> entry : devIfStates.getEdgeDevIfStateMap().entrySet())
    				{
    					if (site.contains(entry.getKey()) && entry.getValue().getStatus() == OnlineStatus.ONLINE)
    					{
    						remFromSite = false;
    						break;
    					}
    				}

    				if (remFromSite == true)
    				{
    					RedisUtil.srem(site, "", devIfStates.getNodeId());
    				}
    			}
    		}
		} catch (Exception e)
		{
		    e.printStackTrace();
		    LOGGER.error("analysisDevIFState error {}", e.getMessage());
		}
	}
}
