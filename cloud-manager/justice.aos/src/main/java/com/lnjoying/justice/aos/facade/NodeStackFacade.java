package com.lnjoying.justice.aos.facade;

import com.lnjoying.justice.aos.common.SimpleStackStatusEnum;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.config.AosFrpConfig;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.db.model.TblStackTemplateInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.aos.db.repo.TemplateRepository;
import com.lnjoying.justice.aos.domain.dto.rsp.NodeStackRsp;
import com.lnjoying.justice.aos.domain.model.DockerComposeYaml;
import com.lnjoying.justice.aos.domain.model.StackInfo;
import com.lnjoying.justice.aos.domain.model.TtyStack;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.aos.util.AosTemplateUtil;
import com.lnjoying.justice.aos.util.TimeUtils;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.time.LocalDateTime;
import java.util.*;

import static com.lnjoying.justice.aos.common.RedisCache.*;
import static com.lnjoying.justice.aos.domain.dto.rsp.NodeStackRsp.NodeStackDeployStatus.NOT_DEPLOY;
import static com.lnjoying.justice.aos.domain.dto.rsp.NodeStackRsp.NodeStackDeployStatus.RUNNING;
import static com.lnjoying.justice.aos.domain.dto.rsp.NodeStackRsp.TtyStackDeployStatus.*;
import static com.lnjoying.justice.aos.util.TtyStackUtils.*;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getBpId;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserId;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.service.aos.AosService.TtyStackDeployStatus;
import static com.lnjoying.justice.schema.service.ecrm.RegionResourceService.NodeStatus.NODE_NOT_EXIST;
import static com.lnjoying.justice.schema.service.ecrm.RegionResourceService.NodeStatus.PERMISSION_DENIED;
import static com.lnjoying.justice.schema.service.ecrm.RegionResourceService.NodeStatus.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/4/13 19:03
 */
@Slf4j
@Service
public class NodeStackFacade
{
    Yaml yaml = new Yaml();

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private StackRepository stackRepo;;

    @Autowired
    private TemplateRepository templateRepo;

    @Autowired
    private StackServiceFacade stackServiceFacade;

    @Autowired
    @Lazy
    private AosFrpConfig aosFrpConfig;

    private  String PROXY_SERVER_URL = "http://%s/%s/tty";

    private static final String DEFAULT_PROXY_SERVER_URL_PORT = "16005";



    public Pair<NodeStackRsp.TtyStackDeployStatus, String> addNodeTtyStack(String nodeId, String type)
    {
        String bpId = getBpId();
        String userId = getUserId();

        if (checkNodeToBeDeleted(nodeId))
        {
            log.info("The tty on the node：{} is being deleted and needs to wait", nodeId);
            return new Pair<>(BUSY, "busy");
        }

        if (checkNode(nodeId, bpId, userId))
        {
            boolean deployed = checkTtyStackDeployed(nodeId);
            if (deployed)
            {
                log.info("tty has been deployed to the node：{}", nodeId);
                String ttyStackInfo = RedisUtil.get(String.format(TTY_STACK_NODES, nodeId));
                if (StringUtils.hasText(ttyStackInfo))
                {
                    TtyStack ttyStack = JsonUtils.fromJson(ttyStackInfo, TtyStack.class);
                    return new Pair<>(DEPLOYED, ttyStack.getTtyToken());
                }
            }
            else
            {
                String stackId = addTtyStack(nodeId, bpId, userId);
                log.info("newly deploy tty to node：{}, stackId: {}", nodeId, stackId);
                addNodeStackCache(nodeId, stackId, userId);
                return new Pair<>(NEW, stackId);
            }

        }

        log.error("Failed to deploy tty to node： {}", nodeId);
        return new Pair<>(NodeStackRsp.TtyStackDeployStatus.ERROR, "");
    }

    public NodeStackRsp getNodeStack(String stackId)
    {
        NodeStackRsp nodeStackRsp = new NodeStackRsp();

        StackInfo stack = null;
        try{
          stack  = stackServiceFacade.getStack(stackId, null, true);
        }
        catch (Exception e)
        {
            if (e instanceof WebSystemException)
            {
                ErrorCode code = ((WebSystemException) e).getCode();
                if (code.equals(STACK_NOT_EXIST) || code.equals(STACK_DROPPED))
                {
                    deleteNodeStackCache(stackId);
                    nodeStackRsp.setStatus(NodeStackRsp.NodeStackDeployStatus.of(NOT_DEPLOY));
                    return nodeStackRsp;
                }
                log.warn("The status of get tty stack is abnormal, the stack state code is：{}", code);
            }
        }

        if (Objects.isNull(stack))
        {
            nodeStackRsp.setStatus(NodeStackRsp.NodeStackDeployStatus.of(NOT_DEPLOY));
            return nodeStackRsp;
        }

        int code = stack.getStatus().getCode();
        SimpleStackStatusEnum simpleStatus = SimpleStackStatusEnum.getSimpleStatus(code);
        NodeStackRsp.NodeStackDeployStatus nodeStackDeployStatus = NodeStackRsp.NodeStackDeployStatus.getNodeStackDeployStatus(simpleStatus.getCode());
        updateNodeStackCache(stackId);
        nodeStackRsp.setStatus(NodeStackRsp.NodeStackDeployStatus.of(nodeStackDeployStatus));
        if (nodeStackDeployStatus.equals(RUNNING))
        {
            String nodeId = RedisUtil.get(String.format(TTY_STACK_STACKS, stack.getId()));
            if (StringUtils.hasText(nodeId))
            {
                TtyStack ttyStack = getTtyStackCache(nodeId);
                Map<String, String> extraInfo = new HashMap<>();
                extraInfo.put("url", ttyStack.getUrl());
                nodeStackRsp.setExtraInfo(extraInfo);
                if (log.isDebugEnabled())
                {
                    log.debug("get node:{} stack rsp:{}", nodeId, nodeStackRsp);
                }
            }
            else
            {
                log.error("tty stack deploy to node: {} status exception", nodeId);
                throw new WebSystemException(TTY_STACK_DEPLOY_STATUS_EXCEPTION, ERROR);
            }
        }

        return nodeStackRsp;
    }

    public void toBeDeleted(String stackId)
    {
        String nodeId = RedisUtil.get(String.format(TTY_STACK_STACKS, stackId));
        if (StringUtils.hasText(nodeId))
        {
            log.info("to be delete the tty stack: {} on the node:{} ", stackId, nodeId);
            // Join the queue to be deleted and check if it s really deleted using a scheduled task
            RedisUtil.sadd(TTY_STACK_TO_BE_DELETED, "", nodeId);
            deleteNodeStack(stackId);
        }
    }

    public void deleteNodeStack(String stackId)
    {
        TblStackInfo stack = stackRepo.getStack(stackId);
        if (Objects.nonNull(stack))
        {
            String specId = stack.getSpecId();
            try
            {
                stackServiceFacade.deleteStackDeployInfo(specId, null);
            }
            catch (Exception e)
            {
                log.error("delete node stack:{} error:{}", stackId, e);
                if (e instanceof WebSystemException)
                {
                    try
                    {
                        ErrorCode code = ((WebSystemException) e).getCode();
                        if (code.equals(STACK_NOT_EXIST) || code.equals(STACK_DROPPED))
                        {
                            log.warn("The stack spec:{} has been deleted. Try to delete the stack:{}", specId, stackId);
                            stackServiceFacade.deleteStack(stackId, null);
                        }
                    }
                    catch (Exception e2)
                    {
                        log.error("it also fails to delete stack directly error:{}", e2);
                    }
                }
            }
        }
    }

    public void checkNodeStackIsInUse()
    {
        Set<String> nodes = RedisUtil.smembers(TTY_STACK_ALL);
        if (!CollectionUtils.isEmpty(nodes))
        {
            nodes.stream().forEach(nodeId -> {
                TtyStack ttyStack = getTtyStackCache(nodeId);
                if (Objects.nonNull(ttyStack))
                {

                    String lastHeartbeatTimeStr = ttyStack.getLastHeartbeatTime();
                    if (StringUtils.hasText(lastHeartbeatTimeStr))
                    {
                        LocalDateTime lastHeartbeatTime = TimeUtils.parseFromString(lastHeartbeatTimeStr);
                        boolean expired = TimeUtils.compareNow(lastHeartbeatTime.plusSeconds(30));
                        if (expired)
                        {
                            RedisUtil.sadd(TTY_STACK_TO_BE_DELETED, "", nodeId);
                            deleteNodeStack(ttyStack.getTtyToken());
                        }
                    }
                    else
                    {
                        LocalDateTime startTime = TimeUtils.parseFromString(ttyStack.getStartDate());
                        boolean expired = TimeUtils.compareNow(startTime.plusSeconds(120));
                        if (expired)
                        {
                            RedisUtil.sadd(TTY_STACK_TO_BE_DELETED, "", nodeId);
                            deleteNodeStack(ttyStack.getTtyToken());
                        }
                    }
                }
            });
        }
    }

    public void checkNodeStackIsDeleted()
    {
        Long nodeIds =  RedisUtil.scard(TTY_STACK_TO_BE_DELETED);
        if (nodeIds.longValue() > 0)
        {
            RedisUtil.smembers(TTY_STACK_TO_BE_DELETED).stream().forEach(nodeId -> {
                TtyStack ttyStack = getTtyStackCache(nodeId);
                if (Objects.nonNull(ttyStack))
                {
                    String stackId = ttyStack.getTtyToken();

                    StackInfo stack = null;
                    try{
                        stack  = stackServiceFacade.getStack(stackId, null, true);
                    }
                    catch (Exception e)
                    {
                        if (e instanceof WebSystemException)
                        {
                            ErrorCode code = ((WebSystemException) e).getCode();
                            if (code.equals(STACK_NOT_EXIST) || code.equals(STACK_DROPPED))
                            {
                                deleteNodeStackCache(stackId);
                            }
                        }
                    }

                    if (Objects.nonNull(stack))
                    {
                        deleteNodeStack(stackId);
                    }
                }
            });
        }
    }

    private void addNodeStackCache(String nodeId, String stackId, String userId)
    {
        String now = TimeUtils.now();
        TtyStack ttyStack = TtyStack.builder().userId(userId).ttyToken(stackId)
                .url(buildTtyProxyServerUrl(nodeId)).startDate(now)
                .lastHeartbeatTime("").build();
        RedisUtil.set(String.format(TTY_STACK_NODES, nodeId), JsonUtils.toJson(ttyStack));
        RedisUtil.set(String.format(TTY_STACK_STACKS, stackId), nodeId);
        RedisUtil.sadd(TTY_STACK_ALL, "", nodeId);
    }

    private void deleteNodeStackCache(String stackId)
    {
        String nodeId = RedisUtil.get(String.format(TTY_STACK_STACKS, stackId));
        if (StringUtils.hasText(nodeId))
        {
            RedisUtil.delete(String.format(TTY_STACK_NODES, nodeId));
            RedisUtil.delete(String.format(TTY_STACK_STACKS, stackId));
            RedisUtil.srem(TTY_STACK_ALL, "", nodeId);
            RedisUtil.srem(TTY_STACK_TO_BE_DELETED, "", nodeId);
        }
    }

    private void updateNodeStackCache(String stackId)
    {
        String nodeId = RedisUtil.get(String.format(TTY_STACK_STACKS, stackId));
        if (StringUtils.hasText(nodeId))
        {
            String ttyStackInfo = RedisUtil.get(String.format(TTY_STACK_NODES, nodeId));
            if (StringUtils.hasText(ttyStackInfo))
            {
                TtyStack ttyStack = JsonUtils.fromJson(ttyStackInfo, TtyStack.class);
                ttyStack.setLastHeartbeatTime(TimeUtils.now());
                RedisUtil.set(String.format(TTY_STACK_NODES, nodeId), JsonUtils.toJson(ttyStack));
            }
            else
            {
                deleteNodeStack(stackId);
            }
        }
    }

    private TtyStack getTtyStackCache(String nodeId)
    {
        String ttyStackInfo = RedisUtil.get(String.format(TTY_STACK_NODES, nodeId));
        if (StringUtils.hasText(ttyStackInfo))
        {
            TtyStack ttyStack = JsonUtils.fromJson(ttyStackInfo, TtyStack.class);
            return ttyStack;
        }
        else
        {
            throw new WebSystemException(TTY_STACK_DEPLOY_STATUS_EXCEPTION, ERROR);
        }
    }

    private String addTtyStack(String nodeId, String bpId, String userId)
    {
        TblStackSpecInfo tblStackSpecInfo = addTtyStackSpec(nodeId, bpId, userId);
        return addTtyStack(tblStackSpecInfo);
    }

    private String addTtyStack(TblStackSpecInfo tblStackSpecInfo)
    {
        List<TblStackInfo> tblStackInfoList = stackServiceFacade.genStack(tblStackSpecInfo, true);
        return tblStackInfoList.get(0).getStackId();
    }

    private TblStackSpecInfo addTtyStackSpec(String nodeId, String bpId, String userId)
    {
        TblStackSpecInfo specInfo = new TblStackSpecInfo();
        String uuid = Utils.assignUUId();
        specInfo.setSpecId(uuid);
        specInfo.setSpecName("tty-stack" + "-" + uuid);
        specInfo.setCreateUserId(userId);
        specInfo.setBpId(bpId);
        specInfo.setUserId(userId);
        specInfo.setCreateTime(new Date());
        specInfo.setUpdateTime(specInfo.getCreateTime());
        specInfo.setDescription("tty-stack");

        TblStackTemplateInfo tblStackTemplateInfo = templateRepo.getTemplateByName(TTY_STACK_TEMPLATE);
        if (Objects.isNull(tblStackTemplateInfo))
        {
            log.error(TTY_STACK_TEMPLATE_NOT_FOUND.getMessage());
            throw new WebSystemException(TTY_STACK_TEMPLATE_NOT_FOUND, ERROR);
        }

        String stackCompose = tblStackTemplateInfo.getStackCompose();
        specInfo.setJusticeCompose(tblStackTemplateInfo.getJusticeCompose());
        Map<String, String> inputParams = new HashMap<>();
        inputParams.put(FRP_SERVER_ADDR_KEY, getFrpServerAddr());
        inputParams.put(NODE_ID_KEY, nodeId);
        inputParams.put(FRP_ADDR_KEY, getFrpIp());
        inputParams.put(FRP_TTY_SSH_HOST, getNodeHostIp(nodeId));
        stackCompose = AosTemplateUtil.format(specInfo.getSpecId(), stackCompose, inputParams);
        specInfo.setStackCompose(stackCompose);
        specInfo.setTemplateId(tblStackTemplateInfo.getTemplateId());
        specInfo.setAutoRun(true);
        specInfo.setAosType("docker-compose");
        specInfo.setContentType("yaml");
        specInfo.setReplicaNum(1);
        specInfo.setDevNeedInfo(JsonUtils.toJson(addDefaultDevNeedInfo()));
        specInfo.setTargetNodes(JsonUtils.toJson(addTargetNode(nodeId)));
        specInfo.setRegistryId("");
        specInfo.setSchedulingStrategy(JsonUtils.toJson(addDefaultSchedulingStrategy()));
        specInfo.setExtraParams(JsonUtils.toJson(addDefaultExtraParams()));
        List<String> stackComposeImages = getStackComposeImages(stackCompose);
        specInfo.setImageNames(JsonUtils.toJson(stackComposeImages));
        specInfo.setStatus(StackState.MAKED);
        // add default labels
        stackCompose = stackServiceFacade.addDefaultLabels(specInfo, stackCompose);
        stackRepo.insertStackSpecInfo(specInfo);
        return specInfo;
    }

    private boolean checkTtyStackDeployed(String nodeId)
    {
        String key = String.format(TTY_STACK_NODES,nodeId);
        Long exists = RedisUtil.exists(key);
        if (null != exists && exists.longValue() == 1)
        {
           return true;
        }
        return false;
    }

    /**
     * return true if node exist and has right permission
     * @param nodeId
     * @param bpId
     * @param userId
     * @return
     */
    private boolean checkNode(String nodeId, String bpId, String userId)
    {
        int status = combRpcService.getRegionResourceService().checkOperUser(nodeId, bpId, userId);
        if (CAN_OPERATE.value() == status)
        {
            return true;
        }
        else if (NODE_NOT_EXIST.value() == status)
        {
            throw new WebSystemException(ErrorCode.NODE_NOT_EXIST, ERROR);
        }
        else if (PERMISSION_DENIED.value() == status)
        {
            throw new WebSystemException(ErrorCode.PERMISSION_DENIED, ERROR);
        }
        else
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ERROR);
        }
    }

    private DevNeedInfo addDefaultDevNeedInfo()
    {
        DevNeedInfo devNeedInfo = new DevNeedInfo();
        CpuNeed cpuNeed = new CpuNeed();
        cpuNeed.setCpuNum(0);
        devNeedInfo.setCpu(cpuNeed);

        MemNeed memNeed = new MemNeed();
        memNeed.setMemLimit(0);
        devNeedInfo.setMem(memNeed);

        DiskNeed diskNeed = new DiskNeed();
        diskNeed.setDiskLimit(0);
        devNeedInfo.setDisk(diskNeed);

        NetworkBandNeed networkBandNeed = new NetworkBandNeed();
        networkBandNeed.setRecvBand(0);
        networkBandNeed.setTransmitBand(0);
        devNeedInfo.setNetworkBandNeed(networkBandNeed);

        return devNeedInfo;
    }

    private List<TargetNode> addTargetNode(String nodeId)
    {
        List<TargetNode> targetNodes = new ArrayList<>();

        RegionResourceService.NodeInfo nodeInfo = combRpcService.getRegionResourceService().getNodeInfoByNodeId(nodeId);
        if (Objects.isNull(nodeInfo))
        {
            throw new WebSystemException(ErrorCode.NODE_NOT_EXIST, ERROR);
        }
        TargetNode targetNode = new TargetNode();
        targetNode.setDstNodeId(nodeInfo.getNodeId());
        targetNode.setDstSiteId(nodeInfo.getSiteId());
        targetNode.setDstRegionId(nodeInfo.getRegionId());
        targetNodes.add(targetNode);
        return targetNodes;
    }

    private SchedulingStrategy addDefaultSchedulingStrategy()
    {
        SchedulingStrategy schedulingStrategy = new SchedulingStrategy();
        Map<String, List<LabelSelector>> labelSelectorMap = new HashMap<>();
        labelSelectorMap.put("node", Collections.emptyList());
        labelSelectorMap.put("site", Collections.emptyList());
        schedulingStrategy.setLabelSelectorMap(labelSelectorMap);
        return schedulingStrategy;
    }


    private Map<String, String> addDefaultExtraParams()
    {
        Map<String, String> extraParams = new HashMap<>();
        extraParams.put("image_pull_policy", "IfNotPresent");
        return extraParams;
    }

    private String buildTtyProxyServerUrl(String nodeId)
    {
       String ttyStackFrpServerUrl = aosFrpConfig.getProxyServerUrl();
        if (StringUtils.hasText(ttyStackFrpServerUrl))
        {
            return String.format(ttyStackFrpServerUrl, nodeId);
        }

        String url = String.format(PROXY_SERVER_URL, getFrpAddr(), nodeId);
        return url;
    }

    private String getFrpServerAddr()
    {

        return aosFrpConfig.getFrpsServerAddr();
    }

    private String getFrpAddr()
    {
        String frpAddr = doGetFrpAddr();
        String[] split = frpAddr.split(":");
        if (split.length > 1)
        {
            return frpAddr;
        }

        return frpAddr + ":" + DEFAULT_PROXY_SERVER_URL_PORT;
    }

    private String getFrpIp()
    {
        String frpAddr = getFrpAddr();
        String[] split = frpAddr.split(":");
        if (split.length >= 1)
        {
            return split[0];
        }

       log.error("frp config error");
        return "";
    }

    private String doGetFrpAddr()
    {

        return aosFrpConfig.getFrpAddr();
    }


    private boolean checkNodeToBeDeleted(String nodeId)
    {
        boolean isMember = RedisUtil.sismember(TTY_STACK_TO_BE_DELETED, "", nodeId);
        if (isMember)
        {
            return true;
        }

        return false;
    }


    /**
     * get all images in docker compose
     * @return
     */
    private List<String> getStackComposeImages(String stackCompose) {
        DockerComposeYaml composeInfo = yaml.loadAs(stackCompose, DockerComposeYaml.class);
        return composeInfo.getImages();
    }
    public Pair<TtyStackDeployStatus, String> addNodeTtyStack(String nodeId, String type, String bpId, String userId)
    {

        if (checkNodeToBeDeleted(nodeId))
        {
            return new Pair<>(TtyStackDeployStatus.ERROR, "");
        }

        boolean deployed = checkTtyStackDeployed(nodeId);
        if (deployed)
        {
            String ttyStackInfo = RedisUtil.get(String.format(TTY_STACK_NODES, nodeId));
            if (StringUtils.hasText(ttyStackInfo))
            {
                TtyStack ttyStack = JsonUtils.fromJson(ttyStackInfo, TtyStack.class);
                return new Pair<>(TtyStackDeployStatus.DEPLOYED, ttyStack.getTtyToken());
            }
        }
        else
        {
            String stackId = addTtyStack(nodeId, bpId, userId);

            addNodeStackCache(nodeId, stackId, userId);
            return new Pair<>(TtyStackDeployStatus.NEW, stackId);
        }
        return new Pair<>(TtyStackDeployStatus.ERROR, "");
    }

    public Pair<TtyStackDeployStatus, String>  getNodeTtyStack(String stackId)
    {
        StackInfo stack;
        try{
            stack  = stackServiceFacade.getStack(stackId, null, true);
        }
        catch (Exception e)
        {
            log.error("get tty stack error: {}", e.getMessage());
            return new Pair<>(TtyStackDeployStatus.ERROR, "");
        }

        if (Objects.isNull(stack))
        {
            log.error("tty stack null error");
            return new Pair<>(TtyStackDeployStatus.ERROR, "");
        }

        int code = stack.getStatus().getCode();

        TtyStackDeployStatus ttyStackDeployStatus = getTtyStackDeployStatus(code);
        updateNodeStackCache(stackId);
        if (ttyStackDeployStatus.equals(TtyStackDeployStatus.DEPLOYED))
        {
            String nodeId = RedisUtil.get(String.format(TTY_STACK_STACKS, stack.getId()));
            if (StringUtils.hasText(nodeId))
            {
                TtyStack ttyStack = getTtyStackCache(nodeId);
                return new Pair<>(TtyStackDeployStatus.DEPLOYED, ttyStack.getUrl());
            }
        }
        else if (ttyStackDeployStatus.equals(TtyStackDeployStatus.DEPLOYING))
        {
            return new Pair<>(TtyStackDeployStatus.DEPLOYING, "");
        }
        log.error("tty stack error, stack detail: {} ", stack);
        return new Pair<>(TtyStackDeployStatus.ERROR, "");
    }

    private TtyStackDeployStatus getTtyStackDeployStatus(int code)
    {
        TtyStackDeployStatus ttyStackDeployStatus;
        SimpleStackStatusEnum simpleStatus = SimpleStackStatusEnum.getSimpleStatus(code);

        switch (simpleStatus)
        {
            case CREATING:
            case CREATED_SUCCEED:
                ttyStackDeployStatus = TtyStackDeployStatus.DEPLOYING;
                break;
            case CREATED_FAILED:
            case STACK_QUIT:
            case STACK_NOT_EXIST:
            case SYSTEM_ABNORMAL:
                ttyStackDeployStatus = TtyStackDeployStatus.ERROR;
                break;
            case RUNNING:
                ttyStackDeployStatus = TtyStackDeployStatus.DEPLOYED;
                break;
            default:
                ttyStackDeployStatus = TtyStackDeployStatus.ERROR;
        }
        return ttyStackDeployStatus;
    }

    private String getNodeHostIp(String nodeId)
    {
        String nodeHostIP = "";
        try
        {
            nodeHostIP = combRpcService.getRegionResourceService().getNodeHostIP(nodeId);
        }
        catch (Exception e)
        {
            log.error("get nodeId: {} host ip failed: {}, ", nodeId, e);
        }

        return StringUtils.hasText(nodeHostIP) ? nodeHostIP : DEFAULT_FRP_TTY_SSH_HOST;
    }
}
