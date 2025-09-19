package com.lnjoying.justice.cluster.manager.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.cluster.manager.common.ClusterNodeStatus;
import com.lnjoying.justice.cluster.manager.common.ClusterStatus;
import com.lnjoying.justice.cluster.manager.common.ClusterType;
import com.lnjoying.justice.cluster.manager.common.K8sRole;
import com.lnjoying.justice.cluster.manager.config.ClusterManagerConfig;
import com.lnjoying.justice.cluster.manager.db.mapper.*;
import com.lnjoying.justice.cluster.manager.db.model.*;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.Cluster2Template;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.ClusterNodeInfoDto;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.JkeConfig;
import com.lnjoying.justice.cluster.manager.domain.dto.model.cluster.K3sConfig;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterBasicInfoRspDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterInfoRspDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterListInfoRspDto;
import com.lnjoying.justice.cluster.manager.domain.dto.rsp.cluster.ClusterNodesRspDto;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodeInfo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterNodePlanInfo;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterNodeSearchCritical;
import com.lnjoying.justice.cluster.manager.domain.search.ClusterSearchCritical;
import com.lnjoying.justice.cluster.manager.service.plan.ContainerPlan;
import com.lnjoying.justice.cluster.manager.service.rpc.CombRpcService;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.cluster.manager.util.cert.CertDao;
import com.lnjoying.justice.cluster.manager.util.cert.CertDaoFactory;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.StatusCode;
import com.lnjoying.justice.schema.entity.scheduler.ClusterBindNode;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.StatusMetrics;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ClusterRepo
{
    
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private TblClusterInfoMapper tblClusterInfoMapper;
    
    @Autowired
    private TblClusterNodeInfoMapper tblClusterNodeInfoMapper;
    
    @Autowired
    private TblClusterCertInfoMapper tblClusterCertInfoMapper;

    @Autowired
    private TblClusterAgentInfoMapper tblClusterAgentInfoMapper;

    @Autowired
    private TblClusterSaInfoMapper tblClusterSaInfoMapper;
    
    @Autowired
    private ClsOperator clsOperator;
    
    @Autowired
    CertDaoFactory certDaoFactory;

    @Autowired
    private TblClusterTemplateInfoMapper templateInfoMapper;

    @Autowired
    private ClusterManagerConfig clusterManagerConfig;

    @Autowired
    private CombRpcService combRpcService;
    
    private final  long MINUTE_TIME = 60*1000;
    private Gson gson  = new Gson();
   
    public int insertCluster(TblClusterInfo tblClusterInfo)
    {
        return tblClusterInfoMapper.insert(tblClusterInfo);
    }
    
    public int deleteCluster(String cluserId)
    {
        return tblClusterInfoMapper.deleteByPrimaryKey(cluserId);
    }
    
    public int updateCluster(TblClusterInfo tblClusterInfo)
    {
        tblClusterInfo.setUpdateTime(new Date());
        return tblClusterInfoMapper.updateByPrimaryKeySelective(tblClusterInfo);
    }
    
    public List<TblClusterInfo> getClusterInfosToPlan(int status)
    {
        TblClusterInfoExample example = new TblClusterInfoExample();
        TblClusterInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(status);
        Long cur = System.currentTimeMillis();
        Date date = new Date(cur - 60000);
        criteria.andUpdateTimeLessThan(date);
        return tblClusterInfoMapper.selectByExample(example);
    }
    
    public List<TblClusterInfo> getClusterInfos(int status, int timeElapse)
    {
        TblClusterInfoExample example = new TblClusterInfoExample();
        TblClusterInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(status);
        
        long checkDate = System.currentTimeMillis() - timeElapse*MINUTE_TIME;
        criteria.andUpdateTimeLessThan(new Date(checkDate));
        return tblClusterInfoMapper.selectByExample(example);
    }
    
    public ClusterListInfoRspDto getClusterInfos(ClusterSearchCritical critical)
    {
        PageHelper.startPage(critical.getPageNum(), critical.getPageSize());
        try
        {
            TblClusterInfoExample example = new TblClusterInfoExample();
            example.setOrderByClause("update_time desc");
            TblClusterInfoExample.Criteria criteria = example.createCriteria();
            if (CollectionUtils.hasContent(critical.getName()))
            {
                criteria.andNameLike("%" + critical.getName() + "%");
            }

            if (CollectionUtils.hasContent(critical.getOwner()))
            {
                criteria.andOwnerEqualTo(critical.getOwner());
            }
            else
            {
                if (CollectionUtils.hasContent(critical.getOperUserId()))
                {
                    criteria.andOwnerEqualTo(critical.getOperUserId());
                }
            }
            
            if (CollectionUtils.hasContent(critical.getOperBpId()))
            {
                criteria.andBpEqualTo(critical.getOperBpId());
            }
            
            if (CollectionUtils.hasContent(critical.getOperUserId()))
            {
                criteria.andMembersLike("%" + critical.getOperUserId() + "%");
            }
            criteria.andStatusNotEqualTo(ClusterStatus.REMOVED.getCode());
    
    
           /* TblClusterInfoExample.Criteria criteria2 = example.or();
            if (CollectionUtils.hasContent(critical.getOperUserId()))
            {
                criteria2.andOwnerEqualTo(critical.getOperUserId());
            }
            criteria2.andStatusNotEqualTo(ClusterStatus.REMOVED.getCode());*/
            
            List<TblClusterInfo> tblClusterInfoList = tblClusterInfoMapper.selectByExample(example);
            PageInfo<TblClusterInfo> pageInfo = new PageInfo<>(tblClusterInfoList);
            List<ClusterInfoRspDto> clusters = tblClusterInfoList.stream().map(ClusterInfoRspDto::of).collect(Collectors.toList());

            String dashboardUrlPattern = "/dashboard/clusters/%s/";
            //String clusterServerHost = RedisUtil.get(K8S_CLUSTER_SERVER_URL);
            String clusterServerHost = clusterManagerConfig.getClusterServerUrl();
            if (clusterServerHost != null)
            {
                dashboardUrlPattern = "https://" + clusterServerHost + "/dashboard/clusters/%s/";
            }

            for (ClusterInfoRspDto clusterInfoRspDto : clusters)
            {
                clusterInfoRspDto.setDashboardUrl(String.format(dashboardUrlPattern, clusterInfoRspDto.getId()));
                assembleUserInfo(clusterInfoRspDto);
                setOnlineState(clusterInfoRspDto);
            }
            return ClusterListInfoRspDto.builder().totalNum(pageInfo.getTotal()).clusters(clusters).build();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void setOnlineState(ClusterInfoRspDto clusterInfoRspDto)
    {
        TblClusterAgentInfoExample example = new TblClusterAgentInfoExample();
        TblClusterAgentInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterInfoRspDto.getId());
        example.setOrderByClause("online_status desc");
        List<TblClusterAgentInfo> tblClusterAgentInfos = tblClusterAgentInfoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(tblClusterAgentInfos))
        {
            clusterInfoRspDto.setOnlineState(tblClusterAgentInfos.get(0).getOnlineStatus());
        }
    }

    public boolean isWorkerOnline(String clusterId)
    {
        TblClusterAgentInfoExample example = new TblClusterAgentInfoExample();
        TblClusterAgentInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        example.setOrderByClause("online_status desc");
        List<TblClusterAgentInfo> tblClusterAgentInfos = tblClusterAgentInfoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(tblClusterAgentInfos))
        {
            return tblClusterAgentInfos.get(0).getOnlineStatus().equals(OnlineStatus.ONLINE);
        }
        return false;
    }

    public Integer getWorkerOnlineState(String clusterId)
    {
        TblClusterAgentInfoExample example = new TblClusterAgentInfoExample();
        TblClusterAgentInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        example.setOrderByClause("online_status desc");
        List<TblClusterAgentInfo> tblClusterAgentInfos = tblClusterAgentInfoMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(tblClusterAgentInfos))
        {
            return tblClusterAgentInfos.get(0).getOnlineStatus();
        }
        return null;
    }

    public void assembleUserInfo(ClusterInfoRspDto clusterInfoRspDto)
    {
        String recordBpId = clusterInfoRspDto.getBp();
        String recordBpName = "";
        if (StringUtils.isNotBlank(recordBpId))
        {
            try
            {
                recordBpName = combRpcService.getUmsService().getBpNameByBpId(recordBpId);
            }
            catch (Exception e)
            {
                LOGGER.error("get bp name failed");
            }
        }
        clusterInfoRspDto.setBpName(recordBpName);

        String ownerUserId = clusterInfoRspDto.getOwner();
        String ownerUserName = "";
        if (StringUtils.isNotBlank(ownerUserId))
        {
            try
            {
                ownerUserName = combRpcService.getUmsService().getUserNameByUserId(ownerUserId);
            }
            catch (Exception e)
            {
                LOGGER.error("get user name failed");
            }
        }
        clusterInfoRspDto.setOwnerName(ownerUserName);
    }
    
    public int updateClusterStatus(String clusterId, int status)
    {
        TblClusterInfo tblClusterInfo = new TblClusterInfo();
        tblClusterInfo.setId(clusterId);
        tblClusterInfo.setStatus(status);
        tblClusterInfo.setUpdateTime(new Date());
        return tblClusterInfoMapper.updateByPrimaryKeySelective(tblClusterInfo);
    }
    
    public TblClusterInfo getCluster(String clusterId)
    {
        return tblClusterInfoMapper.selectByPrimaryKey(clusterId);
    }
    
    public List<Cluster2Template> getCluster2Templates(String clusterId)
    {
        List<Cluster2Template> res = new ArrayList<>();
        List<TblClusterTemplateInfo.ClusterTemplateVersion> versionList = templateInfoMapper.selectVersionsByClusterId(clusterId);
        if (!CollectionUtils.isEmpty(versionList))
        {
            Map<String, List<TblClusterTemplateInfo.ClusterTemplateVersion>> collect = versionList.stream()
                    .collect(Collectors.groupingBy(TblClusterTemplateInfo.ClusterTemplateVersion::getTemplateId, Collectors.mapping(x -> x, Collectors.toList())));
            if (!CollectionUtils.isEmpty(collect))
            {
                collect.forEach((templateId, versions) -> {
                    Cluster2Template cluster2Template = new Cluster2Template();
                    cluster2Template.setTemplateId(templateId);
                    if (!CollectionUtils.isEmpty(versions))
                    {
                        cluster2Template.setTemplateName(versions.get(0).getTemplateName());
                        List<Cluster2Template.Version> innerVersions = versions.stream().map(version -> new Cluster2Template.Version(version.getVersionId(), version.getVersion()))
                                .collect(Collectors.toList());
                        cluster2Template.setVersions(innerVersions);
                    }

                    res.add(cluster2Template);
                });
            }

        }

        return res;
    }
    
    public int insertClusterNode(TblClusterNodeInfo tblClusterNodeInfo)
    {
        return tblClusterNodeInfoMapper.insert(tblClusterNodeInfo);
    }
    
    public ClusterInnerInfo getClusterInnerInfo(String clusterId)
    {
        Object innerObj = RedisUtil.oget(K8sRedisField.K8S_CLUSTER_INNER_INFO, clusterId);
        if (innerObj != null)
        {
            ClusterInnerInfo clusterInnerInfo = (ClusterInnerInfo) innerObj;
            if (! CollectionUtils.isEmpty(clusterInnerInfo.getCertMap()))
            {
                return (ClusterInnerInfo) innerObj;
            }
        }
        
        TblClusterInfo tblClusterInfo = getCluster(clusterId);
        if (tblClusterInfo == null || tblClusterInfo.getStatus().equals(ClusterStatus.REMOVED.getCode()))
        {
            return null;
        }
        
        ClusterInnerInfo clusterInnerInfo = new ClusterInnerInfo();
        if (ClusterType.K8S.equalsIgnoreCase(tblClusterInfo.getClusterType()))
        {
            JkeConfig jkeConfig = gson.fromJson(tblClusterInfo.getClusterEngineConfig(), JkeConfig.class);
            clusterInnerInfo.setJkeConfig(jkeConfig);
        }
        else if (ClusterType.K3S.equalsIgnoreCase(tblClusterInfo.getClusterType()))
        {
            K3sConfig k3sConfig = gson.fromJson(tblClusterInfo.getClusterEngineConfig(), K3sConfig.class);
            clusterInnerInfo.setK3sConfig(k3sConfig);
        }

        clusterInnerInfo.setClusterId(tblClusterInfo.getId());
        clusterInnerInfo.setClusterName(tblClusterInfo.getName());
        clusterInnerInfo.setStatus(ClusterStatus.fromCode(tblClusterInfo.getStatus()));
        clusterInnerInfo.setOwner(tblClusterInfo.getOwner());
        clusterInnerInfo.setServiceStatus(tblClusterInfo.getServiceState());
        
        List<TblClusterNodeInfo> tblClusterNodeInfoList = getClusterNodeInfoList(tblClusterInfo.getId());
        if (! CollectionUtils.isEmpty(tblClusterNodeInfoList))
        {
            for (TblClusterNodeInfo tblClusterNodeInfo : tblClusterNodeInfoList)
            {
                ClusterNodePlanInfo clusterNodePlanInfo = assembleClusterNodeInfo(tblClusterNodeInfo);
                clusterInnerInfo.addNodePlan(clusterNodePlanInfo, tblClusterNodeInfo.getClusterRoles());
            }
        }
        
        clusterInnerInfo.setCertMap(getCertficateInfo(tblClusterInfo.getId()));
        clusterInnerInfo.setClusterType(tblClusterInfo.getClusterType());
        clusterInnerInfo.setCreateType(tblClusterInfo.getCreateType());
        RedisUtil.oset(K8sRedisField.K8S_CLUSTER_INNER_INFO, tblClusterInfo.getId(), clusterInnerInfo);
        
        return clusterInnerInfo;
    }

    public ClusterNodePlanInfo assembleClusterNodeInfo(TblClusterNodeInfo tblClusterNodeInfo)
    {
        ClusterNodePlanInfo clusterNodePlanInfo = new ClusterNodePlanInfo();
        ClusterNodeInfo clusterNodeInfo = new ClusterNodeInfo();
        BeanUtils.copyProperties(tblClusterNodeInfo, clusterNodeInfo);

        List<ClusterBindNode> clusterBindNodes = Arrays.asList(clusterNodeInfo);
        clusterBindNodes = combRpcService.getEdgeResourceService().fillClusterBindNodeField(clusterBindNodes);
        if (! CollectionUtils.isEmpty(clusterBindNodes))
        {
            BeanUtils.copyProperties(clusterBindNodes.get(0), clusterNodeInfo);
        }

        LOGGER.info("assembleClusterNodeInfo: {}", clusterNodeInfo.getNodeId());
        clusterNodePlanInfo.setClusterNodeInfo(clusterNodeInfo);
        List<ContainerPlan> planList = gson.fromJson(tblClusterNodeInfo.getDeployPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
        if (planList != null)
        {
            clusterNodePlanInfo.setContainerPlanList(planList);
        }
        else
        {
            clusterNodePlanInfo.setContainerPlanList(new ArrayList<>());
        }
    
        List<ContainerPlan> uplanList = gson.fromJson(tblClusterNodeInfo.getUndeployPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
        if (uplanList != null)
        {
            clusterNodePlanInfo.setContainerUPlanList(uplanList);
        }
        else
        {
            clusterNodePlanInfo.setContainerUPlanList(new ArrayList<>());
        }

        List<ContainerPlan> monitorPlanList = gson.fromJson(tblClusterNodeInfo.getMonitorPlan(), new com.google.gson.reflect.TypeToken<List<ContainerPlan>>(){}.getType());
        if (monitorPlanList != null)
        {
            clusterNodePlanInfo.setMonitorPlanList(monitorPlanList);
        }
        else
        {
            clusterNodePlanInfo.setMonitorPlanList(new ArrayList<>());
        }
        
        clusterNodePlanInfo.setClusterId(tblClusterNodeInfo.getClusterId());
        clusterNodePlanInfo.setNodeStatus(ClusterNodeStatus.fromCode(tblClusterNodeInfo.getStatus()));
        return clusterNodePlanInfo;
    }
    
    public Map<String, X509CertificateInfo> getCertficateInfo(String clusterId)
    {
        TblClusterCertInfo tblClusterCertInfo = tblClusterCertInfoMapper.selectByPrimaryKey(clusterId);
        if (tblClusterCertInfo == null || StringUtils.isEmpty(tblClusterCertInfo.getCert()))
        {
            return null;
        }
        
        Map<String, X509CertificateInfo> certificateInfoMap = gson.fromJson(tblClusterCertInfo.getCert(), new TypeToken<Map<String, X509CertificateInfo>>(){}.getType());
        if (! CollectionUtils.isEmpty(certificateInfoMap))
        {
            certificateInfoMap.forEach((key,cert) -> {
                try
                {
                    CertDao certDao = null;
                    if (CollectionUtils.hasContent(cert.getKeyPem()))
                    {
                        certDao = certDaoFactory.getCertDaoByKey(cert.getKeyPem());
                        cert.setPrivateKey(certDao.getPrivatekey(cert.getKeyPem()));
                    }
            
                    if (CollectionUtils.hasContent(cert.getCertificatePem()))
                    {
                        certDao = certDaoFactory.getCertDaoByCert(cert.getCertificatePem());
                        cert.setCertificate(certDao.getX509Cert(cert.getCertificatePem()));
                        cert.setPublicKey(cert.getCertificate().getPublicKey());
                    }
            
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
        
            });
        }
        
        return certificateInfoMap;
    }
    
    public List<TblClusterNodeInfo> getClusterNodeInfoList(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        return tblClusterNodeInfoMapper.selectByExample(example);
    }
    
    public long countClusterNode(String clsId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clsId);
        return tblClusterNodeInfoMapper.countByExample(example);
    }
    
    TblClusterNodeInfo getClusterNodeInfo(TblClusterNodeInfoExample example)
    {
        List<TblClusterNodeInfo> clusterNodeInfos = tblClusterNodeInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(clusterNodeInfos))
        {
            return null;
        }
        return clusterNodeInfos.get(0);
    }
    
    public void updateClusterNodeReUnDeployPlanInfo(ClusterNodePlanInfo nodePlanInfo)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(nodePlanInfo.getClusterId());
        criteria.andNodeIdEqualTo(nodePlanInfo.getClusterNodeInfo().getNodeId());
        TblClusterNodeInfo tblClusterNodeInfo = new TblClusterNodeInfo();
        tblClusterNodeInfo.setNodeId(nodePlanInfo.getClusterNodeInfo().getNodeId());
        tblClusterNodeInfo.setStatus(nodePlanInfo.getNodeStatus().getCode());
        tblClusterNodeInfo.setDeployPlan("");
        tblClusterNodeInfo.setUndeployPlan("");
        tblClusterNodeInfoMapper.updateByExampleSelective(tblClusterNodeInfo, example);
    }
    
    public void updateClusterNodePlanInfo(ClusterNodePlanInfo nodePlanInfo)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(nodePlanInfo.getClusterId());
        criteria.andNodeIdEqualTo(nodePlanInfo.getClusterNodeInfo().getNodeId());
        criteria.andStatusLessThanOrEqualTo(nodePlanInfo.getNodeStatus().getCode());
        
        TblClusterNodeInfo tblClusterNodeInfo = getClusterNodeInfo(example);
        if (tblClusterNodeInfo == null)
        {
            return;
        }
        
        tblClusterNodeInfo.setStatus(nodePlanInfo.getNodeStatus().getCode());
        if (nodePlanInfo.getContainerPlanList() != null)
        {
            tblClusterNodeInfo.setDeployPlan(gson.toJson(nodePlanInfo.getContainerPlanList()));
        }
    
        if (nodePlanInfo.getContainerUPlanList() != null)
        {
            tblClusterNodeInfo.setUndeployPlan(gson.toJson(nodePlanInfo.getContainerUPlanList()));
        }

        if (nodePlanInfo.getMonitorPlanList() != null)
        {
            tblClusterNodeInfo.setMonitorPlan(gson.toJson(nodePlanInfo.getMonitorPlanList()));
        }
        
        tblClusterNodeInfoMapper.updateByExampleSelective(tblClusterNodeInfo, example);
    
        if (nodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.DEPLOYING))
        {
            String key = String.format(K8sRedisField.DEPLOY_CLUSTER_NODE,
                    nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            RedisUtil.oset(key, "", nodePlanInfo);
        }
    
        if (nodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.UDEPLOYING))
        {
            String key = String.format(K8sRedisField.UNDEPLOY_CLUSTER_NODE,
                    nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            RedisUtil.oset(key, "", nodePlanInfo);
        }

        if (nodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.DEPLOYED) || nodePlanInfo.getNodeStatus().equals(ClusterNodeStatus.ACTIVE))
        {
            String key = String.format(K8sRedisField.MONITOR_CLUSTER_NODE,
                    nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            RedisUtil.oset(key, "", nodePlanInfo);
        }
    
        if (nodePlanInfo.getNodeStatus().getCode() > ClusterNodeStatus.DEPLOYING.getCode())
        {
            String key = String.format(K8sRedisField.DEPLOY_CLUSTER_NODE,
                    nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            RedisUtil.odel(key, "");
        }
    
        if (nodePlanInfo.getNodeStatus().getCode() > ClusterNodeStatus.UDEPLOYING.getCode())
        {
            String key = String.format(K8sRedisField.UNDEPLOY_CLUSTER_NODE,
                    nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            RedisUtil.odel(key, "");
        }

        if (nodePlanInfo.getNodeStatus().getCode() > ClusterNodeStatus.ACTIVE.getCode())
        {
            String key = String.format(K8sRedisField.MONITOR_CLUSTER_NODE,
                    nodePlanInfo.getClusterId(), nodePlanInfo.getClusterNodeInfo().getNodeId());
            RedisUtil.odel(key, "");
        }
    }
    
    public List<ClusterNodePlanInfo> getDeployingClusterNodeInfo()
    {
        return getClusterNodePlanToDeploy(ClusterNodeStatus.DEPLOYING.getCode());
    }
    
    public List<ClusterNodePlanInfo> getUnDeployingClusterNodeInfo()
    {
        return getClusterNodePlanToDeploy(ClusterNodeStatus.UDEPLOYING.getCode());
    }
    
    public List<ClusterNodePlanInfo> getPlannedClusterNodeInfo()
    {
        return getClusterNodePlanToDeploy(ClusterNodeStatus.PLANNED.getCode());
    }
    
    public List<ClusterNodePlanInfo> getUnPlannedClusterNodeInfo()
    {
        return getClusterNodePlanToDeploy(ClusterNodeStatus.UPLANNED.getCode());
    }
    
    public List<ClusterNodePlanInfo> getClusterNodePlanToDeploy(int status)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(status);
        List<TblClusterNodeInfo> clusterNodeInfos = tblClusterNodeInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(clusterNodeInfos))
        {
            return null;
        }
        List<ClusterNodePlanInfo> clusterNodePlanInfos = new ArrayList<>();
        for (TblClusterNodeInfo tblClusterNodeInfo : clusterNodeInfos)
        {
            ClusterNodePlanInfo clusterNodePlanInfo = assembleClusterNodeInfo(tblClusterNodeInfo);
            clusterNodePlanInfos.add(clusterNodePlanInfo);
            String key = String.format(K8sRedisField.DEPLOY_CLUSTER_NODE, tblClusterNodeInfo.getClusterId(), tblClusterNodeInfo.getNodeId());
            RedisUtil.oset(key,"", clusterNodePlanInfo);
        }
        return clusterNodePlanInfos;
    }
    
    public void insertClusterCert(ClusterInnerInfo clusterInnerInfo)
    {
        TblClusterCertInfo tblClusterCertInfo = new TblClusterCertInfo();
        tblClusterCertInfo.setClusterId(clusterInnerInfo.getClusterId());
        tblClusterCertInfo.setCert(gson.toJson(clusterInnerInfo.getCertMap()));
        tblClusterCertInfo.setCreateTime(new Date());
        tblClusterCertInfo.setUpdateTime(tblClusterCertInfo.getCreateTime());
        tblClusterCertInfoMapper.insert(tblClusterCertInfo);
    }

    public void updateClusterCert(ClusterInnerInfo clusterInnerInfo)
    {
        TblClusterCertInfo tblClusterCertInfo = new TblClusterCertInfo();
        tblClusterCertInfo.setClusterId(clusterInnerInfo.getClusterId());
        tblClusterCertInfo.setCert(gson.toJson(clusterInnerInfo.getCertMap()));
        tblClusterCertInfo.setUpdateTime(tblClusterCertInfo.getCreateTime());
        tblClusterCertInfoMapper.updateByPrimaryKeySelective(tblClusterCertInfo);
    }
    
    public int deleteClusterCert(String clusterId)
    {
        return tblClusterCertInfoMapper.deleteByPrimaryKey(clusterId);
    }
    
    public int deleteClusterNode(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        return tblClusterNodeInfoMapper.deleteByExample(example);
    }
    
    public int deleteClusterAgent(String clusterId)
    {
        TblClusterAgentInfoExample example = new TblClusterAgentInfoExample();
        TblClusterAgentInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        
        return tblClusterAgentInfoMapper.deleteByExample(example);
    }
    
    public int deleteClusterSaToken(String clusterId)
    {
        return tblClusterSaInfoMapper.deleteByPrimaryKey(clusterId);
    }
    
    public ClusterNodesRspDto getClusterNodesInfo(ClusterNodeSearchCritical critical)
    {
        PageHelper.startPage(critical.getPageNum(), critical.getPageSize());
    
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(critical.getClusterId());
        
        List<TblClusterNodeInfo> tblClusterNodeInfoList = tblClusterNodeInfoMapper.selectByExample(example);
        PageInfo<TblClusterNodeInfo> pageInfo = new PageInfo<>(tblClusterNodeInfoList);
    
        List<ClusterNodeInfoDto> nodes = tblClusterNodeInfoList.stream().map(nodeInfo -> ClusterNodeInfoDto.of(combRpcService, nodeInfo)).collect(Collectors.toList());
        return ClusterNodesRspDto.builder().totalNum(pageInfo.getTotal()).nodes(nodes).build();
    }

    public TblClusterNodeInfo getClusterNodeInfo(String nodeId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andNodeIdEqualTo(nodeId);
        example.setOrderByClause("update_time desc");

        List<TblClusterNodeInfo> tblClusterNodeInfoList = tblClusterNodeInfoMapper.selectByExample(example);

        if (CollectionUtils.isEmpty(tblClusterNodeInfoList))
        {
            return null;
        }

        return tblClusterNodeInfoList.get(0);
    }

    public List<ClusterNodeInfoDto> getClusterNodesInfo(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);

        List<TblClusterNodeInfo> tblClusterNodeInfoList = tblClusterNodeInfoMapper.selectByExample(example);

        List<ClusterNodeInfoDto> nodes = tblClusterNodeInfoList.stream().map(nodeInfo -> ClusterNodeInfoDto.of(combRpcService, nodeInfo)).collect(Collectors.toList());
        return nodes;
    }
    
    public long countClusterNodeInfo(String clusterId, String role, int status)
    {
        
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andStatusEqualTo(status);
        criteria.andClusterRolesLike(role);
        return  tblClusterNodeInfoMapper.countByExample(example);
    }
    
    public long countUnReleaseClusterNode(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andStatusNotEqualTo(ClusterNodeStatus.RELEASED.getCode());
        return tblClusterNodeInfoMapper.countByExample(example);
    }
    
    public long countUnDeployingClusterNode(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andStatusEqualTo(ClusterNodeStatus.UDEPLOYING.getCode());
        return tblClusterNodeInfoMapper.countByExample(example);
    }
    
    public long countUnDeployFailedClusterNode(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andStatusEqualTo(ClusterNodeStatus.UDEPLOY_FAILED.getCode());
        return tblClusterNodeInfoMapper.countByExample(example);
    }
    
    public long countDeployingClusterNode(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andStatusIn(Arrays.asList(ClusterNodeStatus.PLANNED.getCode(), ClusterNodeStatus.DEPLOYING.getCode()));
        return tblClusterNodeInfoMapper.countByExample(example);
    }
    
    public long countDeployFailedClusterNode(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        criteria.andStatusEqualTo(ClusterNodeStatus.DEPLOY_FAILED.getCode());
        return tblClusterNodeInfoMapper.countByExample(example);
    }
    
    
    public boolean countDeployPartialSuccess(String clusterId)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        
        {
            TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
            criteria.andClusterIdEqualTo(clusterId);
            String queryRoles = Utils.buildStr("%",K8sRole.CONTROLLER,"%");
            criteria.andClusterRolesLike(queryRoles);
            criteria.andStatusEqualTo(ClusterNodeStatus.DEPLOYED.getCode());
            if (tblClusterNodeInfoMapper.countByExample(example) < 1)
            {
                return false;
            }
        }
        
        {
            example.clear();
            TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
            criteria.andClusterIdEqualTo(clusterId);
            String queryRoles = Utils.buildStr("%",K8sRole.ETCD,"%");
            criteria.andClusterRolesLike(queryRoles);
            if (tblClusterNodeInfoMapper.countByExample(example) < 1)
            {
                return false;
            }
        }
    
        {
            example.clear();
            TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
            String queryRoles = Utils.buildStr("%",K8sRole.WORKER,"%");
            criteria.andClusterRolesLike(queryRoles);
            if (tblClusterNodeInfoMapper.countByExample(example) < 1)
            {
                return false;
            }
        }
        
        return true;
    }
    
    public Set<String> getClusterIdsByCondition(int status, Set<String> excludeIds)
    {
        if (! CollectionUtils.isEmpty(excludeIds))
        {
            String excludeStr = StringUtils.join(excludeIds,"','");
            excludeStr = Utils.buildStr("('", excludeStr, "')");
            Set<String> retSet =  clsOperator.getClusterIdsByExtraCondition(status, excludeStr);
            if (! CollectionUtils.isEmpty(retSet))
            {
                excludeIds.addAll(retSet);
            }
            return excludeIds;
        }
        else
        {
            return clsOperator.getClusterIdsByStatus(status);
        }
    }

    public Set<String> getClusterIdsByStatus(int status)
    {
        return clsOperator.getClusterIdsByStatus(status);
    }
    
    public List<String> getClusterIdsByElapse(String elapseTime, int status)
    {
        return clsOperator.getClusterIdsByElapse(elapseTime, status);
    }
    
    public ClusterNodePlanInfo getDeployNodePlanInfo(String clusterId, String nodeId)
    {
        String key = String.format(K8sRedisField.DEPLOY_CLUSTER_NODE, clusterId, nodeId);
        ClusterNodePlanInfo clusterNodePlanInfo = (ClusterNodePlanInfo) RedisUtil.oget(key, "");
        if (clusterNodePlanInfo == null)
        {
            TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
            TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
            criteria.andClusterIdEqualTo(clusterId);
            criteria.andNodeIdEqualTo(nodeId);
            TblClusterNodeInfo tblClusterNodeInfo =  getClusterNodeInfo(example);
            return assembleClusterNodeInfo(tblClusterNodeInfo);
        }
        return clusterNodePlanInfo;
    }
    
    public ClusterNodePlanInfo getUnDeployNodePlanInfo(String clusterId, String nodeId)
    {
        String key = String.format(K8sRedisField.UNDEPLOY_CLUSTER_NODE, clusterId, nodeId);
        ClusterNodePlanInfo clusterNodePlanInfo = (ClusterNodePlanInfo) RedisUtil.oget(key, "");
        if (clusterNodePlanInfo == null)
        {
            TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
            TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
            criteria.andClusterIdEqualTo(clusterId);
            criteria.andNodeIdEqualTo(nodeId);
            TblClusterNodeInfo tblClusterNodeInfo =  getClusterNodeInfo(example);
            return assembleClusterNodeInfo(tblClusterNodeInfo);
        }
        return clusterNodePlanInfo;
    }

    public ClusterNodePlanInfo getMonitorNodePlanInfo(String clusterId, String nodeId)
    {
        String key = String.format(K8sRedisField.MONITOR_CLUSTER_NODE, clusterId, nodeId);
        ClusterNodePlanInfo clusterNodePlanInfo = (ClusterNodePlanInfo) RedisUtil.oget(key, "");
        if (clusterNodePlanInfo == null)
        {
            TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
            TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
            criteria.andClusterIdEqualTo(clusterId);
            criteria.andNodeIdEqualTo(nodeId);
            TblClusterNodeInfo tblClusterNodeInfo =  getClusterNodeInfo(example);
            if (tblClusterNodeInfo == null) return null;
            return assembleClusterNodeInfo(tblClusterNodeInfo);
        }
        return clusterNodePlanInfo;
    }

    public List<TblClusterInfo> getClusterByToken(String token)
    {
        TblClusterInfoExample example = new TblClusterInfoExample();
        TblClusterInfoExample.Criteria criteria = example.createCriteria();
        criteria.andTokenEqualTo(token);
        return tblClusterInfoMapper.selectByExample(example);
    }

    public int insertClusterAgent(TblClusterAgentInfo tblClusterAgentInfo)
    {
        return tblClusterAgentInfoMapper.insert(tblClusterAgentInfo);
    }

    public TblClusterAgentInfo getClusterAgentByPrimaryKey(String agentId)
    {
        return tblClusterAgentInfoMapper.selectByPrimaryKey(agentId);
    }

    public int updateClusterInfo(TblClusterInfo tblClusterInfo)
    {
        return tblClusterInfoMapper.updateByPrimaryKeySelective(tblClusterInfo);
    }

    public int updateClusterAgentInfo(TblClusterAgentInfo tblClusterAgentInfo)
    {
        return tblClusterAgentInfoMapper.updateByPrimaryKeySelective(tblClusterAgentInfo);
    }

    public int insertClusterSa(TblClusterSaInfo tblClusterSaInfo)
    {
        return tblClusterSaInfoMapper.insert(tblClusterSaInfo);
    }

    public TblClusterSaInfo getClusterSa(String clusterId)
    {
        TblClusterSaInfoExample example = new TblClusterSaInfoExample();
        TblClusterSaInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(clusterId);
        List<TblClusterSaInfo> tblClusterSaInfos = tblClusterSaInfoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(tblClusterSaInfos))
        {
            return null;
        }
        return tblClusterSaInfos.get(0);
    }

    public int updateByPrimaryKeySelective(TblClusterSaInfo tblClusterSaInfo)
    {
        return tblClusterSaInfoMapper.updateByPrimaryKeySelective(tblClusterSaInfo);
    }

    public String selectAgentIdByClusterId(String clusterId)
    {
        Assert.hasText(clusterId, "The given cluster id must not be null!");
        return tblClusterAgentInfoMapper.selectAgentIdByClusterId(clusterId);
    }

    public String selectSecretTokenByClusterId(String clusterId)
    {
        Assert.hasText(clusterId, "The given cluster id must not be null!");
        return tblClusterSaInfoMapper.selectSecretTokenByClusterId(clusterId);
    }

    public List<ClusterBasicInfoRspDto> getClusterBasicInfoList(ClusterSearchCritical critical)
    {
        List<ClusterBasicInfoRspDto> clusterBasicInfoRsp = new ArrayList<>();
        try
        {
            List<ClusterBasicInfoRspDto> clusterBasicInfoRspDtos = clsOperator.getClusterBasicInfoByExample(critical);

            String dashboardUrlPattern = "/dashboard/clusters/%s/";
            String clusterServerHost = clusterManagerConfig.getClusterServerUrl();
            if (clusterServerHost != null)
            {
                dashboardUrlPattern = "https://" + clusterServerHost + "/dashboard/clusters/%s/";
            }
            for (ClusterBasicInfoRspDto clusterBasicInfoRspDto : clusterBasicInfoRspDtos)
            {
                clusterBasicInfoRspDto.setDashboardUrl(String.format(dashboardUrlPattern, clusterBasicInfoRspDto.getId()));
                if (isWorkerOnline(clusterBasicInfoRspDto.getId()))
                {
                    clusterBasicInfoRsp.add(clusterBasicInfoRspDto);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return clusterBasicInfoRsp;
    }

    public long countByExample(TblClusterInfoExample example)
    {
        return tblClusterInfoMapper.countByExample(example);
    }

    public void updateClusterNodeInfo(TblClusterNodeInfo tblClusterNodeInfo)
    {
        TblClusterNodeInfoExample example = new TblClusterNodeInfoExample();
        TblClusterNodeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andClusterIdEqualTo(tblClusterNodeInfo.getClusterId());
        criteria.andNodeIdEqualTo(tblClusterNodeInfo.getNodeId());
        tblClusterNodeInfoMapper.updateByExampleSelective(tblClusterNodeInfo, example);
    }

    public GetStatusMetricsRsp getClusterStatusMetrics(String userId, String bpId)
    {
        GetStatusMetricsRsp getStatusMetricsRsp = new GetStatusMetricsRsp();
        TblClusterInfoExample example = new TblClusterInfoExample();
        TblClusterInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(ClusterStatus.REMOVED.getCode());
        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andOwnerEqualTo(userId);
        }
        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpEqualTo(bpId);
        }
        getStatusMetricsRsp.setTotalNum((int)countByExample(example));
        getStatusMetricsRsp.setStatusMetrics(new ArrayList<>());
        List<StatusMetrics> statusMetricsList = tblClusterInfoMapper.getClusterStatusMetrics(userId, bpId);

        Set<GetStatusMetricsRsp.StatusMetrics> statusMetricsSet = new HashSet<>();
        for (StatusMetrics statusMetrics : statusMetricsList)
        {
            GetStatusMetricsRsp.StatusMetrics metrics =  new GetStatusMetricsRsp.StatusMetrics();
            metrics.setCount(statusMetrics.getCount());
            Map<String, String> desc = new HashMap<>();
            desc.put("cn", ClusterStatus.fromCode(statusMetrics.getStatus()).getMessage().get("cn"));
            desc.put("en", ClusterStatus.fromCode(statusMetrics.getStatus()).getMessage().get("en"));
            StatusCode statusCode = new StatusCode(ClusterStatus.fromCode(statusMetrics.getStatus()).getCode(), desc);
            metrics.setStatus(statusCode);
            statusMetricsSet.add(metrics);

        }

        for (ClusterStatus current : ClusterStatus.values())
        {
            if (current.equals(ClusterStatus.REMOVED)) continue;
            GetStatusMetricsRsp.StatusMetrics metrics =  new GetStatusMetricsRsp.StatusMetrics();
            metrics.setCount(0);
            Map<String, String> desc = new HashMap<>();
            desc.put("cn", current.getMessage().get("cn"));
            desc.put("en", current.getMessage().get("en"));
            StatusCode statusCode = new StatusCode(current.getCode(), desc);
            metrics.setStatus(statusCode);
            statusMetricsSet.add(metrics);
        }

        getStatusMetricsRsp.getStatusMetrics().addAll(statusMetricsSet);
        return getStatusMetricsRsp;
    }
}
