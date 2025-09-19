package com.lnjoying.justice.cluster.manager.service.cluster;

import com.google.common.collect.Lists;
import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.config.ClusterManagerConfig;
import com.lnjoying.justice.cluster.manager.config.ClusterServerRootCA;
import com.lnjoying.justice.cluster.manager.config.EnvCfg;
import com.lnjoying.justice.cluster.manager.config.LabelProperty;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterTmplVerInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterTemplateRepo;
import com.lnjoying.justice.cluster.manager.domain.dto.model.KubeConfigData;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.AddClusterInfoReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.RepairClusterReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.req.cluster.UpdateClusterInfoReqDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.*;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.domain.model.UserBasicInfo;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterNodeSearchCritical;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterSearchCritical;
import com.lnjoying.justice.cluster.manager.handler.actiondescription.i18n.zh_cn.ClusterInfoActionDescriptionTemplate;
import com.lnjoying.justice.cluster.manager.handler.resourcesupervisor.ClusterInfoResourceSupervisor;
import com.lnjoying.justice.cluster.manager.handler.resourcesupervisor.statedict.ClusterStatusDesProvider;
import com.lnjoying.justice.cluster.manager.service.cert.CertService;
import com.lnjoying.justice.cluster.manager.service.client.K8sClientService;
import com.lnjoying.justice.cluster.manager.service.data.ClusterDataService;
import com.lnjoying.justice.cluster.manager.service.data.K3sClusterDataService;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.plan.k8s.SystemAddonPlanServiceImpl;
import com.lnjoying.justice.cluster.manager.service.process.ClusterProcessStrategy;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.BeanMapTool;
import com.lnjoying.justice.cluster.manager.util.ClsUtils;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.handler.operationevent.model.BizModelStateInfo;
import com.lnjoying.justice.commonweb.handler.template.constant.ActionDescriptionTemplateFields;
import com.lnjoying.justice.commonweb.util.*;
import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.lnjoying.justice.schema.common.scheduler.LabelType;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.dev.K8sDevNeed;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.lnjoying.justice.schema.entity.dev.SchedulingStrategy;
import com.lnjoying.justice.schema.entity.dev.TargetNode;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.msg.scheduler.AssignEdge2ClusterReq;
import com.lnjoying.justice.schema.service.ims.ImsRegistryService;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.AppsV1Api;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:  k8s cluster service implement for cluster service
 * @Author: Regulus
 * @Date: 1/6/22 9:04 PM
 */
@Service
public class K8sClusterServiceImpl implements ClusterService
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private ClusterRepo clusterRepo;
    
    @Autowired
    private ClusterTemplateRepo clusterTemplateRepo;
    
    @Autowired
    private ClusterProcessStrategy clusterProcessStrategy;
    
    @Autowired
    private CombRpcService combRpcService;
    
    @Autowired
    private CertService certService;

    @Autowired
    LabelProperty labelProperty;

    @Autowired
    private ClusterDataService clusterDataService;

    @Autowired
    private K3sClusterDataService k3sClusterDataService;

    @Autowired
    private ClusterServerRootCA clusterServerRootCA;
    
    @Autowired
    private  K8sInitService k8sInitService;

    @Autowired
    private  K3sInitService k3sInitService;
    @Autowired
    private ClusterManagerConfig clusterManagerConfig;

    @Autowired
    private K8sClientService k8sClientService;

    @Autowired
    private SystemAddonPlanServiceImpl systemAddonPlanService;

    @Autowired
    EnvCfg envCfg;

    @Autowired
    private ClusterInfoResourceSupervisor clusterInfoResourceSupervisor;
    
    /**
     * @description create cluster. generate cluster token
     * @author Regulus
     * @date 11/15/21 7:38 PM
     * @param clusterInfoReq
     * @param userId
     * @param bpId 
     * @return java.lang.String
     */
    @Override
    public String createCluster(AddClusterInfoReqDto clusterInfoReq, String userId, String bpId) throws WebSystemException
    {
        try
        {
            if (clusterInfoReq.getJkeConfig() != null) checkRegistryInfo(clusterInfoReq);
            TblClusterInfo tblClusterInfo = new TblClusterInfo();
            tblClusterInfo.setName(clusterInfoReq.getName());
            tblClusterInfo.setId(Utils.assignUUId());
            tblClusterInfo.setDescription(clusterInfoReq.getDescription());
            tblClusterInfo.setServiceState(ClusterActiveStatus.INACTIVE);
            tblClusterInfo.setCreator(userId);
            if (StringUtils.isEmpty(clusterInfoReq.getOwner()))
            {
                tblClusterInfo.setOwner(userId);
            }
            else
            {
                tblClusterInfo.setOwner(clusterInfoReq.getOwner());
            }
    
            if (StringUtils.isEmpty(clusterInfoReq.getBp()))
            {
                tblClusterInfo.setBp(bpId);
            }
            else
            {
                tblClusterInfo.setBp(clusterInfoReq.getBp());
            }
    
            if (! StringUtils.isEmpty(clusterInfoReq.getTmplVerId()))
            {
                tblClusterInfo.setTmplVersionId(clusterInfoReq.getTmplVerId());
            }
            
            tblClusterInfo.setCreateType(clusterInfoReq.getCreateType());
            tblClusterInfo.setClusterType(clusterInfoReq.getClusterType());
            if (clusterInfoReq.getClusterType().equals(ClusterType.K8S) && clusterInfoReq.getCreateType().equals(ClusterCreateType.CUSTOM))
            {
                String template = "";
                if (clusterInfoReq.getJkeConfig() != null)
                {
                    template = JsonUtils.toJson(clusterInfoReq.getJkeConfig());
                }
                else if (! StringUtils.isEmpty(clusterInfoReq.getTmplVerId()))
                {
                    TblClusterTmplVerInfo templateVersionInfo = clusterTemplateRepo.getTemplateVersionInfo(clusterInfoReq.getTmplVerId());
                    if (Objects.nonNull(templateVersionInfo))
                    {
                        if(templateVersionInfo.getEnable())
                        {
                            template = templateVersionInfo.getClusterEngineConfig();
                        }
                        else
                        {
                            LOGGER.error("cluster k8s template version disabled");
                            throw new WebSystemException(CLUSTER_K8S_TEMPLATE_VERSION_DISABLED, ErrorLevel.ERROR);
                        }
                    }
                }
                else
                {
                    throw new WebSystemException(ErrorCode.CLUSTER_K8S_JKE_ENGINE_NULL, ErrorLevel.INFO);
                }
                
                if (StringUtils.isBlank(template))
                {
                    throw new WebSystemException(ErrorCode.CLUSTER_K8S_JKE_ENGINE_NULL, ErrorLevel.INFO);
                }
                tblClusterInfo.setClusterEngineConfig(template);
            }
            else if (ClusterType.K3S.equalsIgnoreCase(clusterInfoReq.getClusterType()))
            {
                processk3sConfig(clusterInfoReq, tblClusterInfo);
            }
    
            tblClusterInfo.setDevNeed(ClsUtils.toJson(clusterInfoReq.getDevNeeds()));
            tblClusterInfo.setTargetNodes(ClsUtils.toJson(clusterInfoReq.getTargetNodes()));
            tblClusterInfo.setMembers(ClsUtils.toJson(clusterInfoReq.getMembers()));
    
            tblClusterInfo.setLabels(ClsUtils.toJson(clusterInfoReq.getLabels()));
    
            tblClusterInfo.setAnnotations(ClsUtils.toJson(clusterInfoReq.getAnnotations()));
    
            tblClusterInfo.setToken(Utils.assignUUId());
            tblClusterInfo.setStatus(ClusterStatus.CREATED.getCode());
            tblClusterInfo.setCreateTime(new Date());
            tblClusterInfo.setUpdateTime(tblClusterInfo.getCreateTime());
    
            clusterRepo.insertCluster(tblClusterInfo);
            //记录资源创建事件
            publishClusterInfoCreateEvent(tblClusterInfo, "createCluster");

            if (clusterInfoReq.getCreateType().equals(ClusterCreateType.CUSTOM)
                    && ! CollectionUtils.isEmpty(clusterInfoReq.getTargetNodes()))
            {
                TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
                tblClusterInfo.setStatus(ClusterStatus.SPAWN.getCode());
                clusterRepo.updateCluster(tblClusterInfo);
                //记录资源更新事件
                publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "createCluster");
                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(ClusterMsg.ASSIGN);
                messagePack.setMessageObj(tblClusterInfo);
                clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);
            }
            if (clusterInfoReq.getCreateType().equals(ClusterCreateType.IMPORT))
            {
                systemAddonPlanService.buildImportClusterAddon(tblClusterInfo.getId(), tblClusterInfo.getToken(), clusterInfoReq.getTargetNodes().get(0).getDstRegionId());
            }
            
            return tblClusterInfo.getId();
        }
        catch (DuplicateKeyException e)
        {
            e.printStackTrace();
            LOGGER.error("create cluster error. {}. for {}", e.getMessage(), clusterInfoReq.getName());
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_DUPLICATE, ErrorLevel.INFO);
        }
        catch (Exception e)
        {
            if (e instanceof WebSystemException)
            {
                throw e;
            }
            e.printStackTrace();
            LOGGER.error("create cluster error. {}. for {}", e.getMessage(), clusterInfoReq.getName());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO);
        }
    }

    private void publishClusterInfoUpdateEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            if (tblClusterInfo == null || beforeUpdateEntity == null)
            {
                LOGGER.debug("tblClusterInfo或beforeUpdateEntity为空，跳过发布集群信息更新事件!");
                return;
            }

            if (!Objects.equals(tblClusterInfo.getStatus(), beforeUpdateEntity.getStatus()))
            {
                //集群状态更新
                publishClusterInfoStatusUpdateEvent(tblClusterInfo, beforeUpdateEntity, action);
                return;
            }

            if (!Objects.equals(tblClusterInfo.getServiceState(), beforeUpdateEntity.getServiceState()))
            {
                //集群服务状态更新
                publishClusterInfoServiceStatusUpdateEvent(tblClusterInfo, beforeUpdateEntity, action);
                return;
            }

            clusterInfoResourceSupervisor.publishUpdateEvent("集群信息更新", beforeUpdateEntity.getName(),
                    false, beforeUpdateEntity, tblClusterInfo, action,

                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    beforeUpdateEntity.getName())));
        } catch (Exception e)
        {
            LOGGER.error("发布集群信息更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishClusterInfoStatusUpdateEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            Map<Integer, BizModelStateInfo> clusterInfoStatusDesDict = ClusterStatusDesProvider.INSTANCE.getStateDesDict().get(ClusterStatusDesProvider.STATUS_FIELD);
            BizModelStateInfo stateInfo = clusterInfoStatusDesDict.get(tblClusterInfo.getStatus());
            clusterInfoResourceSupervisor.publishUpdateEvent("集群状态更新",
                    tblClusterInfo.getName(), false,
                    beforeUpdateEntity, tblClusterInfo, action,
                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER_STATUS,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblClusterInfo.getName()),
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblClusterInfo.getStatus())
                                            .map(x -> x.toString())
                                            .orElse("")))
                    ));
        } catch (Exception e)
        {
            LOGGER.error("发布集群状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishClusterInfoServiceStatusUpdateEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            Map<Integer, BizModelStateInfo> clusterInfoServiceStateDesDict = ClusterStatusDesProvider.INSTANCE.getStateDesDict().get(ClusterStatusDesProvider.SERVICE_STATE_FIELD);
            BizModelStateInfo stateInfo = clusterInfoServiceStateDesDict.get(tblClusterInfo.getStatus());
            clusterInfoResourceSupervisor.publishUpdateEvent("集群服务状态更新",
                    tblClusterInfo.getName(), false,
                    beforeUpdateEntity, tblClusterInfo, action,
                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.UPDATE_CLUSTER_SERVICE_STATUS,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME, tblClusterInfo.getName()),
                            TemplateEngineUtils.newEntry("status", Optional.ofNullable(stateInfo)
                                    .map(x -> x.getCnDescription())
                                    .orElse(Optional.ofNullable(tblClusterInfo.getStatus())
                                            .map(x -> x.toString())
                                            .orElse("")))
                    ));
        } catch (Exception e)
        {
            LOGGER.error("发布集群服务状态更新事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    private void publishClusterInfoCreateEvent(TblClusterInfo tblClusterInfo, String action)
    {
        try
        {
            clusterInfoResourceSupervisor.publishCreateEvent(tblClusterInfo,
                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.ADD_CLUSTER,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    tblClusterInfo.getName())
                    ),
                    tblClusterInfo.getName(), action);
        } catch (Exception e)
        {
            LOGGER.error("发布集群创建事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }


    @Override
    public int removeCluster(String clusterId, UserBasicInfo userBasicInfo, boolean force) throws WebSystemException
    {
        clusterRepo.getClusterInnerInfo(clusterId);
        ClusterInnerInfo clusterInnerInfo = getValidClsInfo(clusterId);

        if (force)
        {
            clusterInnerInfo.getClusterNodePlanMap().forEach((k,v) ->{v.forEach(item -> item.setForceDelete(force));});
        }

        ClsUtils.checkUserGrantForCluster(userBasicInfo, clusterInnerInfo);
    
        clusterInnerInfo.setServiceStatus(ActiveStatus.INACTIVE);
        if (clusterInnerInfo.getStatus().equals(ClusterStatus.CREATED))
        {
            removeCluster(clusterInnerInfo, true);
            return SUCCESS.getCode();
        }

        if (clusterInnerInfo.getCreateType().equals(ClusterCreateType.IMPORT))
        {
            removeCluster(clusterInnerInfo, true);
            return SUCCESS.getCode();
        }
        
        if (clusterRepo.countClusterNode(clusterId) < 1)
        {
             removeCluster(clusterInnerInfo, true);
            return SUCCESS.getCode();
        }
        
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(clusterInnerInfo);
        messagePack.setMsgType(ClusterMsg.RELEASE);
        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);

        return SUCCESS.getCode();
    }

    public void removeCluster(ClusterInnerInfo clusterInnerInfo, boolean realRm)
    {
        TblClusterInfo beforeUpdateEntity = clusterRepo.getCluster(clusterInnerInfo.getClusterId());

        if (realRm)
        {
            RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, clusterInnerInfo.getClusterId());
            clusterRepo.deleteClusterCert(clusterInnerInfo.getClusterId());
            clusterRepo.deleteClusterNode(clusterInnerInfo.getClusterId());
            clusterRepo.deleteClusterAgent(clusterInnerInfo.getClusterId());
            clusterRepo.deleteClusterSaToken(clusterInnerInfo.getClusterId());
            clusterRepo.deleteCluster(clusterInnerInfo.getClusterId());
            //记录资源删除事件
            publishClusterInfoDeleteEvent(null, beforeUpdateEntity, "removeCluster");
        } else
        {
            clusterInnerInfo.setStatus(ClusterStatus.REMOVING);
            updateClusterInfo(clusterInnerInfo);
        }
    }

    public void updateClusterInfo(ClusterInnerInfo clusterInnerInfo)
    {
        TblClusterInfo tblClusterInfoSelective = new TblClusterInfo();
        tblClusterInfoSelective.setId(clusterInnerInfo.getClusterId());
        tblClusterInfoSelective.setStatus(clusterInnerInfo.getStatus().getCode());
        tblClusterInfoSelective.setServiceState(clusterInnerInfo.getServiceStatus());
        tblClusterInfoSelective.setUpdateTime(new Date());
        if (clusterInnerInfo.getClusterType().equals(ClusterType.K8S))
        {
            tblClusterInfoSelective.setClusterEngineConfig(JsonUtils.toJson(clusterInnerInfo.getJkeConfig()));
        }
        else
        {
            tblClusterInfoSelective.setClusterEngineConfig(JsonUtils.toJson(clusterInnerInfo.getK3sConfig()));
        }

        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(clusterInnerInfo.getClusterId());
        TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
        clusterRepo.updateCluster(tblClusterInfoSelective);
        BeanUtils.copyNonNullProperties(tblClusterInfoSelective, tblClusterInfo);
        //记录资源更新事件
        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "updateClusterInfo");

        if (clusterInnerInfo.getStatus().getCode() == ClusterStatus.REMOVED.getCode())
        {
            RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, clusterInnerInfo.getClusterId());
        }
        else
        {
            RedisUtil.oset(K8sRedisField.K8S_CLUSTER_INNER_INFO, clusterInnerInfo.getClusterId(), clusterInnerInfo);
        }
        return;
    }

    private void publishClusterInfoDeleteEvent(TblClusterInfo tblClusterInfo, TblClusterInfo beforeUpdateEntity, String action)
    {
        try
        {
            clusterInfoResourceSupervisor.publishDeleteEvent(beforeUpdateEntity, tblClusterInfo,
                    beforeUpdateEntity.getName(), action,
                    TemplateEngineUtils.render0(ClusterInfoActionDescriptionTemplate.Descriptions.DELETE_CLUSTER,
                            false,
                            TemplateEngineUtils.newEntry(ActionDescriptionTemplateFields.RESOURCE_INSTANCE_NAME,
                                    beforeUpdateEntity.getName())));
        } catch (Exception e)
        {
            LOGGER.error("发布集群删除事件失败! stackTrace:{}, errorMessage:{}",
                    ExceptionUtils.getStackTrace(e), e.getMessage());
        }
    }

    @Override
    public int updateCluster(String clusterId, UpdateClusterInfoReqDto clusterInfoReq, UserBasicInfo userBasicInfo) throws WebSystemException
    {
        TblClusterInfo tblClusterInfo = getValidClusterInfo(clusterId);
        TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);

        ClsUtils.checkUserGrantForCluster(userBasicInfo, tblClusterInfo);
    
        tblClusterInfo.setDescription(clusterInfoReq.getDescription());
        
        if (clusterInfoReq.getDevNeed() != null)
        {
            tblClusterInfo.setDevNeed(JsonUtils.toJson(clusterInfoReq.getDevNeed()));
        }

        int result = clusterRepo.updateCluster(tblClusterInfo);
        //记录资源更新事件
        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "updateCluster");
        return result;
    }
    
    private TblClusterInfo getValidClusterInfo(String clusterId) throws WebSystemException
    {
        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(clusterId);
        if (tblClusterInfo == null || tblClusterInfo.getStatus().equals(ClusterStatus.REMOVED.getCode()))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_REMOVED, ErrorLevel.INFO);
        }
        return tblClusterInfo;
    }
    
    private ClusterInnerInfo getValidClsInfo(String clusterId) throws WebSystemException
    {
        ClusterInnerInfo tblClusterInfo = clusterRepo.getClusterInnerInfo(clusterId);
        if (tblClusterInfo == null || tblClusterInfo.getStatus().equals(ClusterStatus.REMOVED.getCode()))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K8S_REMOVED, ErrorLevel.INFO);
        }
        return tblClusterInfo;
    }
    
    @Override
    public ClusterInfoRspDto getClusterInfo(String clusterId, UserBasicInfo userBasicInfo) throws WebSystemException
    {
        TblClusterInfo tblClusterInfo = getValidClusterInfo(clusterId);
        
        ClsUtils.checkUserGrantForCluster(userBasicInfo, tblClusterInfo);
        ClusterInfoRspDto clusterInfoRsp = ClusterInfoRspDto.of(tblClusterInfo);

        String dashboardUrlPattern = "/dashboard/clusters/%s/";
        //String clusterServerHost = RedisUtil.get(K8S_CLUSTER_SERVER_URL);
        String clusterServerHost = clusterManagerConfig.getClusterServerUrl();
        if (clusterServerHost != null)
        {
            dashboardUrlPattern = "https://" + clusterServerHost + "/dashboard/clusters/%s/";
        }
        clusterInfoRsp.setDashboardUrl(String.format(dashboardUrlPattern, clusterInfoRsp.getId()));

        // add template versions
        List<Cluster2Template> cluster2Templates = clusterRepo.getCluster2Templates(clusterId);
        clusterInfoRsp.setTemplates(cluster2Templates);
        clusterRepo.assembleUserInfo(clusterInfoRsp);
        clusterRepo.setOnlineState(clusterInfoRsp);
        return clusterInfoRsp;
    }
    
    @Override
    public ClusterListInfoRspDto getClusterList(ClusterSearchCritical clusterSearchCritical)
    {
        return clusterRepo.getClusterInfos(clusterSearchCritical);
    }
    
    @Override
    public int assignDev(TblClusterInfo tblClusterInfo)
    {
        AssignEdge2ClusterReq assignEdge2ClusterReq = new AssignEdge2ClusterReq();

        assignEdge2ClusterReq.setSpecId(tblClusterInfo.getId());
        assignEdge2ClusterReq.setBpId(tblClusterInfo.getBp());
        assignEdge2ClusterReq.setUserId(tblClusterInfo.getOwner());
        assignEdge2ClusterReq.setDevNeeds(JsonUtils.fromJson(tblClusterInfo.getDevNeed(), new com.google.gson.reflect.TypeToken<List<K8sDevNeed>>(){}.getType()));
        assignEdge2ClusterReq.setTargetNodes(JsonUtils.fromJson(tblClusterInfo.getTargetNodes(), new com.google.gson.reflect.TypeToken<List<TargetNode>>(){}.getType()));
        assignEdge2ClusterReq.setWaitAssignIdList(Lists.newArrayList(tblClusterInfo.getId()));
        assignEdge2ClusterReq.setClusterType(tblClusterInfo.getClusterType());

        if (assignEdge2ClusterReq.getClusterType().equals(ClusterType.K8S))
        {
            assignEdge2ClusterReq.getDevNeeds().sort(new Comparator<K8sDevNeed>() {
                @Override
                public int compare(K8sDevNeed o1, K8sDevNeed o2) {
                    if (o1.getRoles().contains(K8sRole.ETCD)) return -1;
                    if (o2.getRoles().contains(K8sRole.ETCD)) return 1;
                    if (o1.getRoles().contains(K8sRole.CONTROLLER)) return -1;
                    if (o2.getRoles().contains(K8sRole.CONTROLLER)) return 1;
                    return 0;
                }
            });
        }

        assignEdge2ClusterReq.setReplicaNum(1);

        SchedulingStrategy schedulingStrategy = new SchedulingStrategy();
        Map<String, List<LabelSelector>> LabelSelectorMap = new HashMap<>();
        List<LabelSelector> nodeLabelSelectorList = new ArrayList<>();
        nodeLabelSelectorList.add(new LabelSelector("Must", "In", labelProperty.getNodeOrchestration(), ClusterType.K8S));

        if (tblClusterInfo.getClusterType().equals(ClusterType.K8S))
        {
            JkeConfig jkeConfig = JsonUtils.fromJson(tblClusterInfo.getClusterEngineConfig(), JkeConfig.class);
            if (StringUtils.isNotEmpty(jkeConfig.getDockerVersion()))
            {
                nodeLabelSelectorList.add(new LabelSelector("Must", "In", labelProperty.getNodeDockerVersion(), jkeConfig.getDockerVersion()));
            }
        }

        LabelSelectorMap.put(LabelType.NODE.getValue(), nodeLabelSelectorList);
        schedulingStrategy.setLabelSelectorMap(LabelSelectorMap);
        assignEdge2ClusterReq.setSchedulingStrategy(schedulingStrategy);

        CombRetPacket retPacket = combRpcService.getSchedulerService().allockEdge2ClusterResources(assignEdge2ClusterReq);
        if (retPacket.getStatus() == ErrorCode.SUCCESS.getCode())
        {
            TblClusterInfo currentTblClusterInfo = clusterRepo.getCluster(tblClusterInfo.getId());
            if (currentTblClusterInfo != null)
            {
                TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(currentTblClusterInfo);
                currentTblClusterInfo.setStatus(ClusterStatus.ASSIGNING.getCode());
                clusterRepo.updateClusterStatus(tblClusterInfo.getId(), currentTblClusterInfo.getStatus());
                //记录资源更新事件
                publishClusterInfoUpdateEvent(currentTblClusterInfo, beforeUpdateEntity, "assignDev");
            }
        }
        
        return 0;
    }
    
    @Override
    public int releaseDev(ClusterInnerInfo clusterInnerInfo)
    {
        removeCluster(clusterInnerInfo, false);
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(clusterInnerInfo);
        messagePack.setMsgType(ClusterMsg.BUILD_UNDEPLOY_PLAN);
        clusterProcessStrategy.sendMessage(messagePack, ProcessorName.PLAN);
        return 0;
    }
    
    @Override
    public int updateCluster(ClusterInnerInfo clusterInnerInfo)
    {
        updateClusterInfo(clusterInnerInfo);
        return 0;
    }
    
    @Override
    public int prepareBuildCluster(ClusterInnerInfo clusterInnerInfo)
    {
        try
        {
            if (clusterInnerInfo.nodePlanSize() < 1)
            {
                clusterInnerInfo.setStatus(ClusterStatus.ASSIGNING);
                LOGGER.info("no node assigned");
                updateCluster(clusterInnerInfo);
                return 0;
            }

            if (ClusterType.K8S.equalsIgnoreCase(clusterInnerInfo.getClusterType()))
            {
                List<ClusterNodePlanInfo> planInfos = clusterInnerInfo.getClusterNodePlanInfos(K8sRole.CONTROLLER);
                if (CollectionUtils.isEmpty(planInfos))
                {
                    clusterInnerInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
                    updateCluster(clusterInnerInfo);
                    LOGGER.info("assigned error, no controller");
                    return ErrorCode.CLUSTER_DEPLOY_FAILED.getCode();
                }

                planInfos = clusterInnerInfo.getClusterNodePlanInfos(K8sRole.ETCD);
                if (CollectionUtils.isEmpty(planInfos))
                {
                    clusterInnerInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
                    updateCluster(clusterInnerInfo);
                    LOGGER.info("assigned error, no etcd");
                    return ErrorCode.CLUSTER_DEPLOY_FAILED.getCode();
                }

                planInfos = clusterInnerInfo.getClusterNodePlanInfos(K8sRole.WORKER);
                if (CollectionUtils.isEmpty(planInfos))
                {
                    clusterInnerInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
                    updateCluster(clusterInnerInfo);
                    LOGGER.info("assigned error, no worker");
                    return ErrorCode.CLUSTER_DEPLOY_FAILED.getCode();
                }

                k8sInitService.setDefaults(clusterInnerInfo);
                k8sInitService.initNetworkOption(clusterInnerInfo);
                certService.genClusterCerts(clusterInnerInfo, false);

            }
            else if (ClusterType.K3S.equalsIgnoreCase(clusterInnerInfo.getClusterType()))
            {
                Integer failed = doPrepareBuildK3sCluster(clusterInnerInfo);
                if (failed != null) return failed;
            }

            clusterRepo.insertClusterCert(clusterInnerInfo);
            updateCluster(clusterInnerInfo);
    
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(ClusterMsg.BUILD_DEPLOY_PLAN);
            messagePack.setMessageObj(clusterInnerInfo);
            clusterProcessStrategy.sendMessage(messagePack, ProcessorName.PLAN);
        }
        catch (Exception e)
        {
            clusterInnerInfo.setStatus(ClusterStatus.BUILD_FAILED);
            updateCluster(clusterInnerInfo);
            e.printStackTrace();
        }
        
        return 0;
    }

    @Override
    public ClusterNodesRspDto getClusterNodesInfo(ClusterNodeSearchCritical critical)
    {
        return clusterRepo.getClusterNodesInfo(critical);
    }

    @Override
    public String getKubeConfig(String clusterId, Pair<String, String> operUserInfo)
    {
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
        if (Objects.isNull(clusterInnerInfo))
        {
            throw new WebSystemException(CLUSTER_K8S_NOT_EXIST, ErrorLevel.ERROR);
        }

        return buildKubeConfigData(clusterInnerInfo);
    }
    
    @Override
    public void updateClsServiceState(String clusterId, String action, UserBasicInfo userBasicInfo)
    {
        ClusterInnerInfo clusterInnerInfo = getValidClsInfo(clusterId);
    
        ClsUtils.checkUserGrantForCluster(userBasicInfo, clusterInnerInfo);
        if (action.equals("active"))
        {
            String agent = clusterRepo.selectAgentIdByClusterId(clusterId);
            if (StringUtils.isBlank(agent))
            {
                throw new WebSystemException(WORKER_NOT_EXIST, ErrorLevel.INFO);
            }
            
            clusterInnerInfo.setServiceStatus(ActiveStatus.ACITVE);
        }
        else if(action.equals("deactive"))
        {
            clusterInnerInfo.setServiceStatus(ActiveStatus.INACTIVE);
        }
        
        updateClusterInfo(clusterInnerInfo);
    }
    
    @SneakyThrows
    private String buildKubeConfigData(ClusterInnerInfo clusterInnerInfo)
    {
        KubeConfigData kubeConfigData;
        String clusterId = clusterInnerInfo.getClusterId();
        String clusterName = clusterInnerInfo.getClusterName();
        //String clusterServerHost = RedisUtil.get(K8S_CLUSTER_SERVER_URL);
        String clusterServerHost = clusterManagerConfig.getClusterServerUrl();
        if (StringUtils.isBlank(clusterServerHost))
        {
            clusterServerHost = "127.0.0.1:16443";
        }

        String caCert = "";
        try
        {
            caCert = ClsUtils.getBase64(clusterServerRootCA.getSvrRootCrt());
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("ca cert base64 encode failed:{}", e);
        }

        List<KubeConfigData.KubeNode> nodes = new ArrayList<>();
        setDefaultNode(clusterId, clusterName, clusterServerHost, caCert, nodes);

        String token = clusterRepo.selectSecretTokenByClusterId(clusterId);
        LocalClusterAuthEndPoint localClusterAuthEndPoint = null;
        boolean endpointEnabled = false;
        if (clusterInnerInfo.getCreateType().equals(ClusterCreateType.CUSTOM) && clusterInnerInfo.getClusterType().equals(ClusterType.K8S))
        {
            localClusterAuthEndPoint = clusterInnerInfo.getJkeConfig().getLocalClusterAuthEndPoint();
        }
        if (Objects.nonNull(localClusterAuthEndPoint))
        {
            endpointEnabled = localClusterAuthEndPoint.isEnable();
        }

        if (endpointEnabled)
        {
            if (Objects.nonNull(localClusterAuthEndPoint) && StringUtils.isNotBlank(localClusterAuthEndPoint.getFqdn()))
            {
                setClusterNodeFQDN(clusterName, nodes, localClusterAuthEndPoint);
            }
            else
            {
                setClusterNode(clusterInnerInfo, clusterName, nodes);
            }

            kubeConfigData = getKubeConfigData(clusterInnerInfo, clusterName, clusterServerHost, nodes, token);
        }
        else
        {
            kubeConfigData = getBasicKubeConfigData(clusterId, clusterName, clusterServerHost, caCert, nodes, token);
        }

        Map<String, Object> data = BeanMapTool.beanToMap(kubeConfigData);

        String template = clusterDataService.getTemplate("kubeconfig", data);
        if (StringUtils.isNotBlank(template))
        {
            return template;
        }
        throw new WebSystemException(KUBECONFIG_TEMPLATE_FORMAT_FAILED, ErrorLevel.ERROR);
    }

    private KubeConfigData getBasicKubeConfigData(String clusterId, String clusterName, String clusterServerHost, String caCert, List<KubeConfigData.KubeNode> nodes, String token)
    {
        KubeConfigData kubeConfigData = new KubeConfigData();
        kubeConfigData.setClusterName(clusterName);
        kubeConfigData.setClusterId(clusterId);
        kubeConfigData.setHost(clusterServerHost);
        kubeConfigData.setCert(caCert);
        kubeConfigData.setUser(clusterName);
        kubeConfigData.setUsername("");
        kubeConfigData.setPassword("");
        kubeConfigData.setToken(token);
        kubeConfigData.setNodes(nodes);
        kubeConfigData.setEndpointEnabled(false);
        return kubeConfigData;
    }

    private KubeConfigData getKubeConfigData(ClusterInnerInfo clusterInnerInfo, String clusterName, String clusterServerHost, List<KubeConfigData.KubeNode> nodes, String token)
    {
        KubeConfigData kubeConfigData = new KubeConfigData();
        kubeConfigData.setClusterId(clusterInnerInfo.getClusterId());
        kubeConfigData.setHost(clusterServerHost);
        String owner = Objects.nonNull(clusterInnerInfo.getOwner()) ? clusterInnerInfo.getOwner() : clusterName;
        kubeConfigData.setUser(owner);
        kubeConfigData.setClusterName(clusterName);
        kubeConfigData.setCert("");
        kubeConfigData.setUsername("");
        kubeConfigData.setPassword("");
        kubeConfigData.setToken(token);
        kubeConfigData.setEndpointEnabled(true);
        kubeConfigData.setNodes(nodes);
        return kubeConfigData;
    }

    private void setClusterNodeFQDN(String clusterName, List<KubeConfigData.KubeNode> nodes, LocalClusterAuthEndPoint localClusterAuthEndPoint)
    {
        KubeConfigData.KubeNode clusterNode = new KubeConfigData.KubeNode();
        clusterNode.setServer("https://" + localClusterAuthEndPoint.getFqdn());
        clusterNode.setUser(clusterName);
        clusterNode.setClusterName(clusterName + "-fqdn");
        try {
            String fqdnCACerts = ClsUtils.getBase64(localClusterAuthEndPoint.getCacert());
            clusterNode.setCert(fqdnCACerts);
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("fqdn cert base64 encode failed:{}", e);
            clusterNode.setCert("");
        }
        nodes.add(clusterNode);
    }

    private void setClusterNode(ClusterInnerInfo clusterInnerInfo, String clusterName, List<KubeConfigData.KubeNode> nodes)
    {
        String endpointNodeIp = clusterInnerInfo.getEndpointNodeIp();
        ClusterNodePlanInfo clusterNodePlanInfo = clusterInnerInfo.getClusterNodePlanInfo(K8sRole.CONTROLLER);
        String nodeName= "";
        if (Objects.nonNull(clusterNodePlanInfo))
        {
            nodeName = clusterNodePlanInfo.getClusterNodeInfo().getNodeName();
        }
        X509CertificateInfo certInfo = clusterInnerInfo.getCertInfo(KeyCertName.CACertName);
        if (Objects.isNull(certInfo) || StringUtils.isBlank(certInfo.getCertificatePem()))
        {
            LOGGER.info("cluster k8s cert info:{}", certInfo);
            throw new WebSystemException(CLUSTER_K8S_CERTIFICATE_INCORRECT, ErrorLevel.ERROR);
        }
        KubeConfigData.KubeNode clusterNode = new KubeConfigData.KubeNode();
        nodeName = clusterName + "-" + nodeName;
        clusterNode.setClusterName(nodeName);
        clusterNode.setServer(String.format("https://%s:6443", endpointNodeIp));
        clusterNode.setUser(clusterName);
        try
        {
            clusterNode.setCert(ClsUtils.getBase64(certInfo.getCertificatePem()));
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        nodes.add(clusterNode);
    }

    private void setDefaultNode(String clusterId, String clusterName, String clusterServerHost, String caCert, List<KubeConfigData.KubeNode> nodes)
    {
        // set default kubenode
        KubeConfigData.KubeNode defaultNode = new KubeConfigData.KubeNode();
        defaultNode.setClusterName(clusterName);
        defaultNode.setUser(clusterName);
        defaultNode.setServer(String.format("https://%s/proxy/k8s/clusters/%s", clusterServerHost, clusterId));
        defaultNode.setCert(caCert);
        nodes.add(defaultNode);
    }

    @Override
    public List<ClusterBasicInfoRspDto> getClusterBasicInfoList(ClusterSearchCritical clusterSearchCritical)
    {
        return clusterRepo.getClusterBasicInfoList(clusterSearchCritical);
    }

    @Override
    public ClusterTerminalRspDto getClusterTerminal(String clusterId, UserBasicInfo userBasicInfo)
    {
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
        if (Objects.isNull(clusterInnerInfo))
        {
            throw new WebSystemException(CLUSTER_K8S_NOT_EXIST, ErrorLevel.ERROR);
        }
        ClsUtils.checkUserGrantForCluster(userBasicInfo, clusterInnerInfo);
        if (clusterInnerInfo.getStatus().getCode() < ClusterStatus.DEPLOYED.getCode()
                || clusterInnerInfo.getStatus().getCode() > ClusterStatus.IMPORTED.getCode())
        {
            throw new WebSystemException(CLUSTER_K8S_CANNOT_OPEN_TERMINAL, ErrorLevel.ERROR);
        }
        if (clusterInnerInfo.getServiceStatus() == ClusterActiveStatus.INACTIVE)
        {
            throw new WebSystemException(CLUSTER_K8S_INACTIVE, ErrorLevel.ERROR);
        }
        if (clusterRepo.getWorkerOnlineState(clusterId) == OnlineStatus.OFFLINE)
        {
            throw new WebSystemException(CLUSTER_K8S_WORKER_OFFLINE, ErrorLevel.ERROR);
        }

        ClusterTerminalRspDto clusterTerminalRspDto = new ClusterTerminalRspDto();

        String dashboardUrlPattern = "/dashboard/clusters/%s/#/shell/%s/%s/%s?namespace=%s";
        String clusterServerHost = clusterManagerConfig.getClusterServerUrl();
        if (clusterServerHost != null)
        {
            dashboardUrlPattern = "https://" + clusterServerHost + "/dashboard/clusters/%s/#/shell/%s/%s/%s?namespace=%s";
        }

        InputStream kubeConfigStream = new ByteArrayInputStream(getKubeConfig(clusterId, null).getBytes(StandardCharsets.UTF_8));

        try
        {
            CoreV1Api apiCore = k8sClientService.getCoreApiInstance(kubeConfigStream);
            V1PodList v1PodList = k8sClientService.listK8sPod(apiCore, "joy-terminal", "joy-system");
            if (v1PodList != null && v1PodList.getItems().size() > 0)
            {
                V1Pod pod = v1PodList.getItems().stream().filter(item -> item.getStatus().getContainerStatuses().get(0).getState().getRunning() != null).findFirst().orElse(null);
                if (pod == null)
                {
                    pod = v1PodList.getItems().get(0);
                }
                clusterTerminalRspDto.setNamespace(pod.getMetadata().getNamespace());
                clusterTerminalRspDto.setPodName(pod.getMetadata().getName());
                clusterTerminalRspDto.setContainerName(pod.getStatus().getContainerStatuses().get(0).getName());
                clusterTerminalRspDto.setDashboardUrl(String.format(dashboardUrlPattern, clusterId, clusterTerminalRspDto.getNamespace(),
                        clusterTerminalRspDto.getPodName(), clusterTerminalRspDto.getContainerName(), clusterTerminalRspDto.getNamespace()));
                clusterTerminalRspDto.setStatus(pod.getStatus().getContainerStatuses().get(0).getState().getRunning() !=null ? "running":"waiting");
            }
            else
            {
                kubeConfigStream = new ByteArrayInputStream(getKubeConfig(clusterId, null).getBytes(StandardCharsets.UTF_8));
                AppsV1Api apiInstance = k8sClientService.getApiInstance(kubeConfigStream);
                String image = null;
                V1Deployment terminalDeployment = null;
                if (clusterInnerInfo.getClusterType().equals(ClusterType.K8S))
                {
                    image = clusterDataService.getImage(clusterInnerInfo.getJkeConfig().getK8sVersion(), "kubelet");
                    terminalDeployment = buildTerminalDeployment(image);
                }
                else if (clusterInnerInfo.getClusterType().equals(ClusterType.K3S))
                {
                    image = k3sClusterDataService.getImage(clusterInnerInfo.getK3sConfig().getK3sVersion(), "k3s-server");
                    terminalDeployment = buildK3sTerminalDeployment(image);
                }

                V1Deployment v1Deployment = k8sClientService.createK8sDeployment(apiInstance, terminalDeployment, "joy-system");
                clusterTerminalRspDto.setStatus("waiting");
            }
        }
        catch (ApiException | IOException e)
        {
            clusterTerminalRspDto.setStatus("error");
            e.printStackTrace();
        }

        return clusterTerminalRspDto;
    }

    private V1Deployment buildTerminalDeployment(String image)
    {
        Map<String, String> labels = new HashMap<String, String>(){{put("app", "joy-terminal");}};
        return new V1Deployment()
                .apiVersion("apps/v1")
                .kind("Deployment")
                .metadata(new V1ObjectMeta()
                        .name("joy-terminal")
                        .namespace("joy-system")
                        .labels(labels))
                .spec(new V1DeploymentSpec()
                        .replicas(1)
                        .selector(new V1LabelSelector()
                                .matchLabels(labels))
                        .template(new V1PodTemplateSpec()
                                .metadata(new V1ObjectMeta()
                                        .name("joy-terminal")
                                        .labels(labels))
                                .spec(new V1PodSpec()
                                        .hostNetwork(true)
                                        .addContainersItem(new V1Container()
                                                .name("joy-terminal")
                                                .image(image)
                                                .addCommandItem("/bin/bash")
                                                .addEnvItem(new V1EnvVar()
                                                        .name("KUBECONFIG")
                                                        .value("/etc/kubernetes/ssl/kubecfg-kube-admin.yaml"))
                                                .securityContext(new V1SecurityContext()
                                                        .privileged(true))
                                                .addVolumeMountsItem(new V1VolumeMount()
                                                        .mountPath("/etc/kubernetes")
                                                        .name("etc-kubernetes"))
                                                .tty(true))
                                        .addVolumesItem(new V1Volume()
                                                .name("etc-kubernetes")
                                                .hostPath(new V1HostPathVolumeSource()
                                                        .path("/etc/kubernetes"))))));
    }

    private V1Deployment buildK3sTerminalDeployment(String image)
    {
        Map<String, String> labels = new HashMap<String, String>(){{put("app", "joy-terminal");}};
        return new V1Deployment()
                .apiVersion("apps/v1")
                .kind("Deployment")
                .metadata(new V1ObjectMeta()
                        .name("joy-terminal")
                        .namespace("joy-system")
                        .labels(labels))
                .spec(new V1DeploymentSpec()
                        .replicas(1)
                        .selector(new V1LabelSelector()
                                .matchLabels(labels))
                        .template(new V1PodTemplateSpec()
                                .metadata(new V1ObjectMeta()
                                        .name("joy-terminal")
                                        .labels(labels))
                                .spec(new V1PodSpec()
                                        .hostNetwork(true)
                                        .addContainersItem(new V1Container()
                                                .name("joy-terminal")
                                                .image(image)
                                                .addCommandItem("/bin/sh")
                                                .securityContext(new V1SecurityContext()
                                                        .privileged(true))
                                                .addVolumeMountsItem(new V1VolumeMount()
                                                        .mountPath("/etc/rancher/k3s")
                                                        .name("etc-rancher"))
                                                .tty(true))
                                        .addVolumesItem(new V1Volume()
                                                .name("etc-rancher")
                                                .hostPath(new V1HostPathVolumeSource()
                                                        .path("/etc/rancher/k3s"))))));
    }

    @Override
    public ClusterNodeRspDto getClusterNodeInfo(String nodeId)
    {
        TblClusterNodeInfo tblClusterNodeInfo = clusterRepo.getClusterNodeInfo(nodeId);

        if (null == tblClusterNodeInfo)
        {
            return null;
        }

        ClusterNodeInfoDto clusterNodeInfoDto = ClusterNodeInfoDto.of(combRpcService, tblClusterNodeInfo);

        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(tblClusterNodeInfo.getClusterId());

        if (tblClusterInfo.getStatus() == ClusterStatus.REMOVED.getCode())
        {
            return ClusterNodeRspDto.builder().clusterId(null).clusterName(null).clusterType(null).nodeInfo(clusterNodeInfoDto).build();
        }
        else
        {
            return ClusterNodeRspDto.builder().clusterId(tblClusterInfo.getId()).clusterName(tblClusterInfo.getName()).clusterType(tblClusterInfo.getClusterType()).nodeInfo(clusterNodeInfoDto).build();
        }
    }

    @Override
    public List<CoreServiceMetrics> getClusterCoreServiceMetrics(String clusterId)
    {
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
        if ( clusterInnerInfo == null || clusterInnerInfo.getCreateType().equals(ClusterCreateType.IMPORT) || clusterInnerInfo.getStatus().equals(ClusterStatus.REMOVED))
        {
            return null;
        }
        Map<String, CoreServiceMetrics> coreServiceMetricsMap = new HashMap<>();
        List<ClusterNodeInfoDto> clusterNodeInfoDtos = clusterRepo.getClusterNodesInfo(clusterId);
        clusterNodeInfoDtos.forEach(nodeInfo -> { if (!CollectionUtils.isEmpty(nodeInfo.getCoreServices()))
        {
            nodeInfo.getCoreServices().forEach(coreService -> {
                CoreServiceMetrics coreServiceMetrics = coreServiceMetricsMap.getOrDefault(coreService.getServiceName(), new CoreServiceMetrics(coreService.getServiceName(), 0, 0));
                if (coreService.getStatus().getCode() == InstanceState.RUNNING.getCode())
                {
                    coreServiceMetrics.setRunningNum(coreServiceMetrics.getRunningNum() + 1);
                }
                else
                {
                    coreServiceMetrics.setFailedNum(coreServiceMetrics.getFailedNum() + 1);
                }
                coreServiceMetricsMap.put(coreService.getServiceName(), coreServiceMetrics);
            });
        }});
        return new ArrayList<>(coreServiceMetricsMap.values());
    }

    private void processk3sConfig(AddClusterInfoReqDto clusterInfoReq, TblClusterInfo tblClusterInfo)
    {
        String template = "";
        if (clusterInfoReq.getK3sConfig()!= null)
        {
            template = JsonUtils.toJson(clusterInfoReq.getK3sConfig());
        }
        else if (! StringUtils.isEmpty(clusterInfoReq.getTmplVerId()))
        {
            TblClusterTmplVerInfo templateVersionInfo = clusterTemplateRepo.getTemplateVersionInfo(clusterInfoReq.getTmplVerId());
            if (Objects.nonNull(templateVersionInfo))
            {
                if(templateVersionInfo.getEnable())
                {
                    // todo get jkeconfig
                    template = templateVersionInfo.getClusterEngineConfig();
                }
                else
                {
                    LOGGER.error("cluster k3s template version disabled");
                    throw new WebSystemException(CLUSTER_K3S_TEMPLATE_VERSION_DISABLED, ErrorLevel.ERROR);
                }
            }
        }
        else
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_JKE_ENGINE_NULL, ErrorLevel.INFO);
        }

        if (StringUtils.isBlank(template))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_K3S_JKE_ENGINE_NULL, ErrorLevel.INFO);
        }
        tblClusterInfo.setClusterEngineConfig(template);
    }

    @Override
    public ClusterImportCmdRspDto getImportClusterCmd(String clusterId)
    {
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(clusterId);
        if ( clusterInnerInfo == null || !clusterInnerInfo.getCreateType().equals(ClusterCreateType.IMPORT))
        {
            throw new WebSystemException(ErrorCode.CLUSTER_IMPORT_CMDS_FAILED, ErrorLevel.INFO);
        }
        String addonFileUrl = String.format("%s%s/%s", envCfg.getAddonFileUrl(), clusterId, systemAddonPlanService.getAgentAddonFilename());
        ClusterImportCmdRspDto clusterImportCmdRspDto = new ClusterImportCmdRspDto();
        clusterImportCmdRspDto.setCommand(String.format("kubectl apply -f %s", addonFileUrl));
        clusterImportCmdRspDto.setInsecureCommand(String.format("curl --insecure -sfL %s | kubectl apply -f -", addonFileUrl));
        return clusterImportCmdRspDto;
    }

    @Nullable
    private Integer doPrepareBuildK3sCluster(ClusterInnerInfo clusterInnerInfo)
    {
        List<ClusterNodePlanInfo> planInfos = clusterInnerInfo.getClusterNodePlanInfos(K3sRole.CONTROLLER);
        if (CollectionUtils.isEmpty(planInfos))
        {
            clusterInnerInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
            updateCluster(clusterInnerInfo);
            LOGGER.info("assigned error, no k3s controller");
            return ErrorCode.CLUSTER_DEPLOY_FAILED.getCode();
        }

        planInfos = clusterInnerInfo.getClusterNodePlanInfos(K8sRole.WORKER);
        if (CollectionUtils.isEmpty(planInfos))
        {
            clusterInnerInfo.setStatus(ClusterStatus.DEPLOY_FAILED);
            updateCluster(clusterInnerInfo);
            LOGGER.info("assigned error, no k3s worker");
            return ErrorCode.CLUSTER_DEPLOY_FAILED.getCode();
        }

        k3sInitService.initNetworkOption(clusterInnerInfo);
        certService.genK3sClusterCerts(clusterInnerInfo, false);
        return null;
    }


    private void checkRegistryInfo(AddClusterInfoReqDto clusterInfoReq)
    {
        if (clusterInfoReq.getClusterType().equals(ClusterType.K8S))
        {
            JkeConfig jkeConfig = clusterInfoReq.getJkeConfig();
            List<RegistryInfo> registries = jkeConfig.getRegistries();
            if (!CollectionUtils.isEmpty(registries))
            {
                List<CompletableFuture<Void>> futureList = registries.stream().filter(registryInfo -> StringUtils.isNotBlank(registryInfo.getUsername()))
                        .map(registryInfo -> CompletableFuture.runAsync(() ->
                        {
                            try {
                                ImsRegistryService.Registry registry = combRpcService.getImsRegistryService().getRegistryByUrlAndName(registryInfo.getServer(), registryInfo.getUsername());
                                if (Objects.nonNull(registry))
                                {
                                    registryInfo.setPassword(registry.getRegistryPassword());
                                }
                            }
                            catch (Exception e)
                            {
                                LOGGER.error("get registry info error:{}", e);
                            }

                        })).collect(Collectors.toList());
                futureList.stream().map(CompletableFuture::join).count();
            }
        }
    }

    @Override
    public void repairCluster(String clusterId, RepairClusterReqDto repairClusterReqDto)
    {
        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(clusterId);
        if (Objects.isNull(tblClusterInfo))
        {
            throw new WebSystemException(CLUSTER_K8S_NOT_EXIST, ErrorLevel.ERROR);
        }
        String type = repairClusterReqDto.getType().toLowerCase(Locale.ROOT);
        switch(type)
        {
            case "redeploy":
                redeployCluster(tblClusterInfo);
                break;
            case "redeploy-node":
                redeployClusterNode(tblClusterInfo, repairClusterReqDto.getNodeIds());
                break;
            case "continue":
                contineDeployClusterNode(tblClusterInfo, repairClusterReqDto.getNodeIds());
                break;
            default:
        }
    }

    private void redeployCluster(TblClusterInfo tblClusterInfo)
    {
        TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
        List<TblClusterNodeInfo> tblClusterNodeInfos = clusterRepo.getClusterNodeInfoList(tblClusterInfo.getId());

        for (TblClusterNodeInfo tblClusterNodeInfo : tblClusterNodeInfos)
        {
            tblClusterNodeInfo.setDeployPlan("");
            tblClusterNodeInfo.setStatus(ClusterNodeStatus.BINDED.getCode());
            clusterRepo.updateClusterNodeInfo(tblClusterNodeInfo);
        }

        tblClusterInfo.setStatus(ClusterStatus.ASSIGNED.getCode());
        clusterRepo.updateCluster(tblClusterInfo);
        //记录资源更新事件
        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "redeployCluster");

        RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, tblClusterInfo.getId());
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(tblClusterInfo.getId());

        if (clusterInnerInfo.getStatus() == ClusterStatus.ASSIGNED)
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(ClusterMsg.BUILD_DEPLOY_PLAN);
            messagePack.setMessageObj(clusterInnerInfo);
            clusterProcessStrategy.sendMessage(messagePack, ProcessorName.PLAN);
        }
    }

    private void redeployClusterNode(TblClusterInfo tblClusterInfo, List<String> nodeIds)
    {
        TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
        List<TblClusterNodeInfo> tblClusterNodeInfos = clusterRepo.getClusterNodeInfoList(tblClusterInfo.getId());

        Set<String> nodeSet = new HashSet<>(nodeIds);

        for (TblClusterNodeInfo tblClusterNodeInfo : tblClusterNodeInfos)
        {
            if (nodeSet.contains(tblClusterNodeInfo.getNodeId()))
            {
                tblClusterNodeInfo.setDeployPlan("");
                tblClusterNodeInfo.setStatus(ClusterNodeStatus.BINDED.getCode());
                clusterRepo.updateClusterNodeInfo(tblClusterNodeInfo);
            }
        }

        tblClusterInfo.setStatus(ClusterStatus.ASSIGNED.getCode());
        clusterRepo.updateCluster(tblClusterInfo);
        //记录资源更新事件
        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "redeployClusterNode");

        RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, tblClusterInfo.getId());
        ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(tblClusterInfo.getId());

        if (clusterInnerInfo.getStatus() == ClusterStatus.ASSIGNED)
        {
            MessagePack messagePack = new MessagePack();
            messagePack.setMsgType(ClusterMsg.BUILD_DEPLOY_PLAN);
            messagePack.setMessageObj(clusterInnerInfo);
            clusterProcessStrategy.sendMessage(messagePack, ProcessorName.PLAN);
        }
    }

    private void contineDeployClusterNode(TblClusterInfo tblClusterInfo, List<String> nodeIds)
    {
        TblClusterInfo beforeUpdateEntity = DeepCopyUtils.deepCopy(tblClusterInfo);
        List<TblClusterNodeInfo> tblClusterNodeInfos = clusterRepo.getClusterNodeInfoList(tblClusterInfo.getId());

        Set<String> nodeSet = new HashSet<>(nodeIds);

        for (TblClusterNodeInfo tblClusterNodeInfo : tblClusterNodeInfos)
        {
            if (nodeSet.contains(tblClusterNodeInfo.getNodeId()))
            {
                ClusterNodePlanInfo clusterNodePlanInfo = clusterRepo.assembleClusterNodeInfo(tblClusterNodeInfo);
                ContainerPlan plan = clusterNodePlanInfo.getContainerPlan();
                plan.setErrorTime(0);
                plan.setCurAction(PlanAction.CREATE);
                plan.setNextAction(PlanAction.REMOVE_BEFORE);
                clusterRepo.updateClusterNodePlanInfo(clusterNodePlanInfo);

                tblClusterNodeInfo.setDeployPlan(null);
                tblClusterNodeInfo.setStatus(ClusterNodeStatus.DEPLOYING.getCode());
                clusterRepo.updateClusterNodeInfo(tblClusterNodeInfo);

                MessagePack messagePack = new MessagePack();
                messagePack.setMsgType(ClusterMsg.DEPLOY);
                messagePack.setMessageObj(clusterNodePlanInfo);
                clusterProcessStrategy.sendMessage(messagePack, ProcessorName.DEPLOY);
                RedisUtil.lpush(K8sRedisField.DEPLOY_CLUSTER_NODE_LIST, tblClusterNodeInfo.getClusterId(), tblClusterNodeInfo.getNodeId());
            }
        }

        tblClusterInfo.setStatus(ClusterStatus.PLANNED.getCode());
        clusterRepo.updateCluster(tblClusterInfo);
        //记录资源更新事件
        publishClusterInfoUpdateEvent(tblClusterInfo, beforeUpdateEntity, "contineDeployClusterNode");

        RedisUtil.odel(K8sRedisField.K8S_CLUSTER_INNER_INFO, tblClusterInfo.getId());
        RedisUtil.sadd(K8sRedisField.DEPLOY_CLUSTERID_SET, "", tblClusterInfo.getId());
    }
}
