package com.lnjoying.justice.cis.service.rpcserviceimpl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lnjoying.justice.cis.common.constant.*;
import com.lnjoying.justice.cis.controller.dto.request.LogContainerInstReq;
import com.lnjoying.justice.cis.db.model.TblContainerClsinstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfo;
import com.lnjoying.justice.cis.db.model.TblContainerInstInfoExample;
import com.lnjoying.justice.cis.db.repo.CisRepository;
import com.lnjoying.justice.cis.db.repo.ClsInstRepository;
import com.lnjoying.justice.cis.facade.NetMessageServiceFacade;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.service.cis.CCInstService;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * @Author: Regulus
 * @Date: 12/29/21 11:46 AM
 * @Description:
 */
@RpcSchema(schemaId = "ccService")
public class CCInstServiceImpl implements CCInstService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private NetMessageServiceFacade netMessageServiceFacade;
    
    @Autowired
    private ClsInstRepository clsInstRepository;

    @Autowired
    private CisRepository cisRepository;
    
    private final long CHECK_INTERVALL = 1*1000;
    
    @Override
    public CombRetPacket createContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instParams")String instParams, @ApiParam(name = "instName")String instName, @ApiParam(name = "registry")Registry registry, @ApiParam(name = "autoRun")boolean autoRun, @ApiParam(name = "orchType")String orchType)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        TblContainerClsinstInfo tblContainerClsinstInfo = clsInstRepository.getInst(instName, targetNode.getDstNodeId());
        String instId = "";
        
        if (tblContainerClsinstInfo != null)
        {
            if ((tblContainerClsinstInfo.getStatus() >= InstanceState.QUEUEING.getCode() &&
                    tblContainerClsinstInfo.getStatus() < InstanceState.REMOVED.getCode()) ||
                    tblContainerClsinstInfo.getStatus() > InstanceState.OBJECT_AUTO_REMOVE.getCode())
            {
                combRetPacket.setStatus(tblContainerClsinstInfo.getStatus());
                return combRetPacket;
            }
            instId = tblContainerClsinstInfo.getInstId();
            LOGGER.info("may be recreate inst. {}:{}", instName, instId);
    
        }
        else
        {
            instId = Utils.buildStr(CisIdPrefix.K8S, Utils.assignUUId());
            LOGGER.info("new create inst. {}:{}", instName, instId);
        }
        
        String instPamrastr = instParams;
    
        JsonObject instParamsMap = JsonParser.parseString(instParams).getAsJsonObject();
        DevNeedInfo devNeedInfo = DevNeedInfo.builder().cpu(new CpuNeed()).mem(new MemNeed()).disk(new DiskNeed()).build();
    
        
        if (registry != null)
        {
            String imageName = instParamsMap.get("Image").getAsString();
            String [] imagepaths = imageName.split("/");
            if (imagepaths.length >= 3)
            {
                String regstryImage = imagepaths[0];
                if (! regstryImage.equals(registry.getServer()))
                {
                    registry = null;
                }
            }
            else if (! imageName.startsWith(registry.getServer()) && ! registry.getServer().startsWith("docker.io"))
            {
                instParamsMap.addProperty("Image",  Utils.buildStr(registry.getServer(), "/", imageName));
                instPamrastr = JsonUtils.toJson(instParamsMap);
            }
        }
    
//        String imagePolicy =  "{\"image_pull_policy\":\"Always\"}";
        String imagePolicy =  "{\"image_pull_policy\":\"IfNotPresent\"}";
        int errorCode = netMessageServiceFacade.createInst(registry, instPamrastr, JsonUtils.toJson(devNeedInfo), autoRun, 1, "cluster", "cluster", instName, instId, targetNode.getDstNodeId(), imagePolicy);
        if (errorCode != ErrorCode.SUCCESS.getCode())
        {
            LOGGER.info("create inst {} maybe failed to api server", instName);
            combRetPacket.setStatus(InstanceState.UNKNOWN.getCode());
        }
        else
        {
            LOGGER.info("create inst {} ,fwd to api server", instName);
            combRetPacket.setStatus(InstanceState.FWD.getCode());
        }
    
        //save in db
        if (tblContainerClsinstInfo == null)
        {
            tblContainerClsinstInfo = new TblContainerClsinstInfo();
            tblContainerClsinstInfo.setInstId(instId);
            tblContainerClsinstInfo.setContainerName(instName);
            tblContainerClsinstInfo.setRegionId(targetNode.getDstRegionId());
            tblContainerClsinstInfo.setSiteId(targetNode.getDstSiteId());
            tblContainerClsinstInfo.setNodeId(targetNode.getDstNodeId());
            tblContainerClsinstInfo.setParams(instParams);
            tblContainerClsinstInfo.setAutoRun(autoRun);
            tblContainerClsinstInfo.setStatus(InstanceState.FWD.getCode());
            if (registry != null)
            {
                tblContainerClsinstInfo.setRegistryInfo(JsonUtils.toJson(registry));
            }
            tblContainerClsinstInfo.setOrchType(orchType);
            tblContainerClsinstInfo.setCreateTime(new Date());
            tblContainerClsinstInfo.setUpdateTime(tblContainerClsinstInfo.getCreateTime());
            clsInstRepository.insertInst(tblContainerClsinstInfo);
        }
        
        return combRetPacket;
    }
    
    @Override
    public CombRetPacket logContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        //get k8s inst from db by instName and target node id
        TblContainerClsinstInfo clsinstInfo = clsInstRepository.getInst(instName, targetNode.getDstNodeId());
        if (clsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
            return combRetPacket;
        }
        
        LogContainerInstReq logReq = new LogContainerInstReq();
        logReq.setFlow(false);
        logReq.setHead_or_tail("tail");
        logReq.setInstId(clsinstInfo.getInstId()); //file with the inst query from  db
        String refId = clsinstInfo.getRefId();
        String logId = Utils.buildStr(CisIdPrefix.K8S, Utils.assignUUId());
        netMessageServiceFacade.logInstReq(logId, logReq, targetNode.getDstNodeId(), refId);
        RedisUtil.set(CisRedisField.CLS_LOG_ID_INSTID, logId, clsinstInfo.getInstId(),CisTimeConst.CacheLogTime);
        combRetPacket.setStatus(clsinstInfo.getStatus());
        return combRetPacket;
    }
    
    @Override
    public CombRetPacket listContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        //get k8s inst from db by instName and target node id
        TblContainerClsinstInfo clsinstInfo = clsInstRepository.getInst(instName, targetNode.getDstNodeId());
        if (clsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
            return combRetPacket;
        }
        
        long curTime = System.currentTimeMillis();
        if (curTime - clsinstInfo.getUpdateTime().getTime() < CHECK_INTERVALL)
        {
            combRetPacket.setStatus(clsinstInfo.getStatus());
            return combRetPacket;
        }
    
        netMessageServiceFacade.listInstReq(clsinstInfo.getInstId(), clsinstInfo.getNodeId());
        combRetPacket.setStatus(clsinstInfo.getStatus());
        return combRetPacket;
    }
    
    @Override
    public CombRetPacket getLogContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        
        //get inst from db by instName and target node id
        TblContainerClsinstInfo clsinstInfo = clsInstRepository.getInst(instName, targetNode.getDstNodeId());
        if (clsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
            return combRetPacket;
        }
        String logContent = RedisUtil.get(CisRedisField.CLS_LOG_CONTENT, clsinstInfo.getInstId());
        if (logContent != null)
        {
            combRetPacket.setObj(logContent);
            RedisUtil.delete(Utils.buildStr(CisRedisField.CLS_LOG_CONTENT, clsinstInfo.getInstId()));
        }
        
        return combRetPacket;
    }
    
    @Override
    public CombRetPacket removeContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName")String instName)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
    
        //get inst from db by instName and target node id
        TblContainerClsinstInfo clsinstInfo = clsInstRepository.getInst(instName, targetNode.getDstNodeId());
        if (clsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
            return combRetPacket;
        }
        
        if (! CollectionUtils.hasContent(clsinstInfo.getRefId()))
        {
            if ( clsinstInfo.getStatus() >= InstanceState.SUCCESS_QUIT.getCode())
            {
//                clsInstRepository.removeInst(clsinstInfo.getInstId());
                combRetPacket.setStatus(InstanceState.REMOVED.getCode());
                return combRetPacket;
            }
            else
            {
                combRetPacket.setStatus(InstanceState.KEEP_ON.getCode());
                return combRetPacket;
            }
        }
        clsinstInfo.setStatus(InstanceState.SPAWNED_CLOUD_REMOVE.getCode());
        clsInstRepository.updateInst(clsinstInfo);
        netMessageServiceFacade.lifeMngInstReq(clsinstInfo.getInstId(), clsinstInfo.getRefId(), InstAction.REMOVE.getName(), targetNode.getDstNodeId());
        combRetPacket.setStatus(clsinstInfo.getStatus());
        return combRetPacket;
    }
    
    @Override
    public CombRetPacket cleanJkeContainer(@ApiParam(name = "targetNode")TargetNode targetNode,  @ApiParam(name = "orchType")String orchType)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        List<TblContainerClsinstInfo> tblContainerClsinstInfoList = clsInstRepository.getInstByNodeIdAndType(targetNode.getDstNodeId(), orchType);
        if (CollectionUtils.isEmpty(tblContainerClsinstInfoList))
        {
            return combRetPacket;
        }
        
        for (TblContainerClsinstInfo clsinstInfo : tblContainerClsinstInfoList)
        {
            try
            {
                netMessageServiceFacade.lifeMngInstReq(clsinstInfo.getInstId(), clsinstInfo.getRefId(), InstAction.REMOVE.getName(), targetNode.getDstNodeId());
            }
            catch (Exception e)
            {
                LOGGER.error("clean {} error, msg {}", clsinstInfo.getContainerName(), e.getMessage());
            }
        }
        return combRetPacket;
    }

    @Override
    public CombRetPacket getContainerStatus(@ApiParam(name = "nodeId")String nodeId, @ApiParam(name = "instName")String instName)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        TblContainerClsinstInfo tblContainerClsinstInfo = clsInstRepository.getInst(instName, nodeId);

        if (tblContainerClsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
        }
        else
        {
            combRetPacket.setStatus(tblContainerClsinstInfo.getStatus());
        }
        return combRetPacket;
    }

    @Override
    public CombRetPacket actionContainer(@ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instName") String instName, @ApiParam(name = "action") String action)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        TblContainerClsinstInfo clsinstInfo = clsInstRepository.getInst(instName, targetNode.getDstNodeId());
        if (clsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
            return combRetPacket;
        }

        combRetPacket.setStatus(clsinstInfo.getStatus());

        try
        {
            netMessageServiceFacade.lifeMngInstReq(clsinstInfo.getInstId(), clsinstInfo.getRefId(), action, targetNode.getDstNodeId());
        }
        catch (Exception e)
        {
            LOGGER.error("clean {} error, msg {}", clsinstInfo.getContainerName(), e.getMessage());
        }

        return combRetPacket;
    }

    @Override
    public CombRetPacket createContainerWithInstId(@ApiParam(name = "instId")String instId, @ApiParam(name = "targetNode")TargetNode targetNode, @ApiParam(name = "instParams")String instParams, @ApiParam(name = "instName")String instName, @ApiParam(name = "registry")Registry registry, @ApiParam(name = "autoRun")boolean autoRun, @ApiParam(name = "orchType")String orchType)
    {
        CombRetPacket combRetPacket = new CombRetPacket();
        TblContainerClsinstInfo tblContainerClsinstInfo = clsInstRepository.getInst(instId);

        if (tblContainerClsinstInfo != null)
        {
            if ((tblContainerClsinstInfo.getStatus() >= InstanceState.QUEUEING.getCode() &&
                    tblContainerClsinstInfo.getStatus() < InstanceState.REMOVED.getCode()) ||
                    tblContainerClsinstInfo.getStatus() > InstanceState.OBJECT_AUTO_REMOVE.getCode())
            {
                combRetPacket.setStatus(tblContainerClsinstInfo.getStatus());
                return combRetPacket;
            }
            LOGGER.info("may be recreate inst. {}:{}", instName, instId);
        }

        String instPamrastr = instParams;

        JsonObject instParamsMap = JsonParser.parseString(instParams).getAsJsonObject();
        DevNeedInfo devNeedInfo = DevNeedInfo.builder().cpu(new CpuNeed()).mem(new MemNeed()).disk(new DiskNeed()).build();

        if (registry != null)
        {
            String imageName = instParamsMap.get("Image").getAsString();
            String [] imagepaths = imageName.split("/");
            if (imagepaths.length >= 3)
            {
                String regstryImage = imagepaths[0];
                if (! regstryImage.equals(registry.getServer()))
                {
                    registry = null;
                }
            }
            else if (! imageName.startsWith(registry.getServer()) && ! registry.getServer().startsWith("docker.io"))
            {
                instParamsMap.addProperty("Image",  Utils.buildStr(registry.getServer(), "/", imageName));
                instPamrastr = JsonUtils.toJson(instParamsMap);
            }
        }

        String imagePolicy =  "{\"image_pull_policy\":\"IfNotPresent\"}";
        int errorCode = netMessageServiceFacade.createInst(registry, instPamrastr, JsonUtils.toJson(devNeedInfo), autoRun, 1, "cmp", "cmp", instName, instId, targetNode.getDstNodeId(), imagePolicy);
        if (errorCode != ErrorCode.SUCCESS.getCode())
        {
            LOGGER.info("create inst {} maybe failed to api server", instName);
            combRetPacket.setStatus(InstanceState.UNKNOWN.getCode());
        }
        else
        {
            LOGGER.info("create inst {} ,fwd to api server", instName);
            combRetPacket.setStatus(InstanceState.FWD.getCode());
        }

        //save in db
        if (tblContainerClsinstInfo == null)
        {
            tblContainerClsinstInfo = new TblContainerClsinstInfo();
            tblContainerClsinstInfo.setInstId(instId);
            tblContainerClsinstInfo.setContainerName(instName);
            tblContainerClsinstInfo.setRegionId(targetNode.getDstRegionId());
            tblContainerClsinstInfo.setSiteId(targetNode.getDstSiteId());
            tblContainerClsinstInfo.setNodeId(targetNode.getDstNodeId());
            tblContainerClsinstInfo.setParams(instParams);
            tblContainerClsinstInfo.setAutoRun(autoRun);
            tblContainerClsinstInfo.setStatus(InstanceState.FWD.getCode());
            if (registry != null)
            {
                tblContainerClsinstInfo.setRegistryInfo(JsonUtils.toJson(registry));
            }
            tblContainerClsinstInfo.setOrchType(orchType);
            tblContainerClsinstInfo.setCreateTime(new Date());
            tblContainerClsinstInfo.setUpdateTime(tblContainerClsinstInfo.getCreateTime());
            clsInstRepository.insertInst(tblContainerClsinstInfo);
        }

        return combRetPacket;
    }

    @Override
    public CombRetPacket removeContainerByInstId(@ApiParam(name = "instId")String instId)
    {
        CombRetPacket combRetPacket = new CombRetPacket();

        //get inst from db by instName and target node id
        TblContainerClsinstInfo clsinstInfo = clsInstRepository.getInst(instId);
        if (clsinstInfo == null)
        {
            combRetPacket.setStatus(InstanceState.REMOVED.getCode());
            return combRetPacket;
        }

        if (! CollectionUtils.hasContent(clsinstInfo.getRefId()))
        {
            if ( clsinstInfo.getStatus() >= InstanceState.SUCCESS_QUIT.getCode())
            {
//                clsInstRepository.removeInst(clsinstInfo.getInstId());
                combRetPacket.setStatus(InstanceState.REMOVED.getCode());
                return combRetPacket;
            }
            else
            {
                combRetPacket.setStatus(InstanceState.KEEP_ON.getCode());
                return combRetPacket;
            }
        }
        clsinstInfo.setStatus(InstanceState.SPAWNED_CLOUD_REMOVE.getCode());
        clsInstRepository.updateInst(clsinstInfo);
        netMessageServiceFacade.lifeMngInstReq(clsinstInfo.getInstId(), clsinstInfo.getRefId(), InstAction.REMOVE.getName(), clsinstInfo.getNodeId());
        combRetPacket.setStatus(clsinstInfo.getStatus());
        return combRetPacket;
    }

    @Override
    public boolean checkOperUserByContainerRefId(@ApiParam(name = "containerRefId")String containerRefId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {

        if (StringUtils.isEmpty(containerRefId))
        {
            return false;
        }
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
        {
            return false;
        }

        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andUserIdEqualTo(userId);
        }
        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpIdEqualTo(bpId);
        }
        criteria.andRefIdEqualTo(containerRefId);

        List<TblContainerInstInfo> tblContainerInstInfos = cisRepository.selectByExample(example);
        return null != tblContainerInstInfos && tblContainerInstInfos.size() != 0;
    }

    @Override
    public boolean checkOperUserByContainerName(@ApiParam(name = "containerName")String containerName, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {

        if (StringUtils.isEmpty(containerName))
        {
            return false;
        }
        if (StringUtils.isEmpty(userId) && StringUtils.isEmpty(bpId))
        {
            return false;
        }

        TblContainerInstInfoExample example = new TblContainerInstInfoExample();
        TblContainerInstInfoExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andUserIdEqualTo(userId);
        }
        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpIdEqualTo(bpId);
        }
        criteria.andContainerNameEqualTo(containerName);

        List<TblContainerInstInfo> tblContainerInstInfos = cisRepository.selectByExample(example);
        return null != tblContainerInstInfos && tblContainerInstInfos.size() != 0;
    }
}
