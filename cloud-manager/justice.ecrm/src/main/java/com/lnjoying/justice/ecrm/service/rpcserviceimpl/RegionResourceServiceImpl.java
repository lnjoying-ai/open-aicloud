package com.lnjoying.justice.ecrm.service.rpcserviceimpl;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.ecrm.config.LabelProperty;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.ecrm.domain.dto.model.RegionNode;
import com.lnjoying.justice.ecrm.service.CombRpcService;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.NodesStatusMetrics;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.lnjoying.justice.schema.service.ums.UmsService;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.servicecomb.provider.pojo.RpcSchema;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isBpAdmin;
import static com.lnjoying.justice.schema.service.ecrm.RegionResourceService.NodeStatus.*;

/**
 * region service impl
 *
 * @author merak
 **/
@RpcSchema(schemaId = "regionResourceService")
public class RegionResourceServiceImpl implements RegionResourceService {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    LabelProperty labelProperty;
    
    Gson gson = new Gson();

    private static Logger LOGGER = LogManager.getLogger();
    @Override
    public Set<String> getRegionIds() {
        return regionRepository.getAllRegionIds();
    }

    @Override
    public Set<String> getRegionIdsByName(@ApiParam(name = "regionName")String regionName) {
        return regionRepository.getRegionsByName(regionName).stream()
                .map(record -> record.getRegionId()).collect(Collectors.toSet());
    }

    @Override
    public List<Region> getRegionByRegionIds(@ApiParam(name = "regionIds") Set<String> regionIds) {

        List<Region> res = new ArrayList<>();

        if (! CollectionUtils.isEmpty(regionIds)) {
            List<TblRegionInfo> tblRegionInfos = regionIds.parallelStream().map(regionId -> regionRepository.getRegion(regionId))
                    .filter(region -> {
                        return Objects.nonNull(region) ? region.getStatus() != ActiveStatus.REMOVED : false;
                    })
                    .collect(Collectors.toList());
            List<Region> regions = tblRegionInfos.stream().map(info -> new Region(info.getRegionId(), info.getRegionName())).collect(Collectors.toList());
            res.addAll(regions);
        }
        return res;
    }

    @Override
    public List<RegionDto> selectAllNodesByRegionIds(@ApiParam(name = "regionIds")Set<String> regionIds) {
        List<RegionNode> regionNodes = new ArrayList<>();
        if (! CollectionUtils.isEmpty(regionIds)) {
            regionIds.stream().forEach(regionid -> regionNodes.addAll(regionRepository.getAllNodes(regionid)));
        } else {
            // Get all regions
            Set<String> allRegionIds = regionRepository.getAllRegionIds();
            allRegionIds.stream().forEach(regionid -> regionNodes.addAll(regionRepository.getAllNodes(regionid)));
        }

        if (! CollectionUtils.isEmpty(regionNodes)) {
            return buildTree(regionNodes);
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getRegionNameById(@ApiParam(name = "regionId")String regionId)
    {
        return regionRepository.getRegion(regionId).getRegionName();
    }

    @Override
    public String getSiteNameById(@ApiParam(name = "siteId")String siteId)
    {
        return siteRepository.getSite(siteId).getSiteName();
    }

    @Override
    public String getNodeNameById(@ApiParam(name = "nodeId")String nodeId)
    {
        TblEdgeComputeInfo edgeNode = edgeRepository.getEdgeNode(nodeId);
        if (Objects.nonNull(edgeNode))
        {
            return edgeNode.getNodeName();
        }
       return "";
    }

    @Override
    public int checkOperUser(@ApiParam(name = "nodeId")String nodeId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {
        TblEdgeComputeInfo edgeNode = edgeRepository.getEdgeNode(nodeId);
        if (Objects.isNull(edgeNode))
        {
            return NODE_NOT_EXIST.value();
        }

        UmsService.UserInfo userInfo = combRpcService.getUmsService().getUserInfoByUseId(userId);
        if (Objects.isNull(userInfo))
        {
            return PERMISSION_DENIED.value();
        }

        String kind = String.valueOf(userInfo.getKind());
        if (!(isBpAdmin(kind) || isAdmin(kind)))
        {
            return PERMISSION_DENIED.value();
        }

        if (isAdmin(kind))
        {
            return CAN_OPERATE.value();
        }
        else
        {
            String bpIdValue = getUserInfoFromLabels(edgeNode, labelProperty.getNodeBpId());
            if (StringUtils.isNotBlank(bpId) && StringUtils.isNotBlank(bpIdValue))
            {
                if (bpId.equals(bpIdValue))
                {
                    return CAN_OPERATE.value();
                }
                else
                {
                    return USER_NOT_GRANT.value();
                }
            }
        }
        return USER_NOT_GRANT.value();
    }

    @Override
    public NodeInfo getNodeInfoByNodeId(@ApiParam(name = "nodeId")String nodeId)
    {
        return edgeRepository.selectByNodeId(nodeId);
    }

    private List<RegionDto> buildTree(List<RegionNode> nodes) {

        Map<RegionDto, Map<SiteDto, Set<NodeDto>>> group = nodes.stream().collect(Collectors.groupingBy(regionNode -> new RegionDto(regionNode.getRegionId(), regionNode.getRegionName()),
                Collectors.groupingBy(regionNode -> new SiteDto(regionNode.getSiteId(), regionNode.getSiteName()),
                        Collectors.mapping(regionNode -> new NodeDto(regionNode.getNodeId(), regionNode.getNodeName()), Collectors.toSet()))));

        List<RegionDto> res = new ArrayList<>();
        for (Map.Entry<RegionDto, Map<SiteDto, Set<NodeDto>>> regionDto : group.entrySet()) {
            RegionDto key = regionDto.getKey();
            Map<SiteDto, Set<NodeDto>> siteDtos = regionDto.getValue();
            key.setChildren(Sets.newHashSet(siteDtos.keySet()));
            for (Map.Entry<SiteDto, Set<NodeDto>> siteDto : siteDtos.entrySet()) {
                SiteDto siteDtoKey = siteDto.getKey();
                Set<NodeDto> siteDtoValue = siteDto.getValue();
                siteDtoKey.setChildren(siteDtoValue);
            }
            res.add(key);
        }

        return res;
    }


    private String getUserInfoFromLabels(TblEdgeComputeInfo edgeNode, String labelKey)
    {
        String labelValue = "";
        TblEdgeComputeLabelInfoExample edgeLabelExample = new TblEdgeComputeLabelInfoExample();
        TblEdgeComputeLabelInfoExample.Criteria edgeLabelCriteria = edgeLabelExample.createCriteria();
        edgeLabelCriteria.andNodeIdEqualTo(edgeNode.getNodeId());
        edgeLabelCriteria.andLabelKeyEqualTo(labelKey);
        List<TblEdgeComputeLabelInfo> edgeLabels = edgeRepository.getEdgeLabels(edgeLabelExample);
        if (! CollectionUtils.isEmpty(edgeLabels))
        {
            TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo = edgeLabels.get(0);
            labelValue = tblEdgeComputeLabelInfo.getLabelValue();
        }

        return labelValue;
    }

    @Override
    public NodesStatusMetrics getNodeStatusMetrics(@ApiParam(name = "regionId")String regionId, @ApiParam(name = "siteId")String siteId, @ApiParam(name = "filter")String filter, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId)
    {
        NodesStatusMetrics nodesStatusMetrics = new NodesStatusMetrics();

        List<String> categories = Arrays.asList(filter.split("\\|"));

        if (categories.size() == 0 )
        {
            return nodesStatusMetrics;
        }

        if (StringUtils.isNotBlank(siteId))
        {
            nodesStatusMetrics.setSiteNodeMetrics(edgeRepository.getSiteStatusMetrics(Arrays.asList(siteId.split("\\|")), categories, bpId, userId));
        }
        else if (StringUtils.isNotBlank(regionId))
        {
            nodesStatusMetrics.setRegionNodeMetrics(edgeRepository.getRegionStatusMetrics(Arrays.asList(regionId.split("\\|")), categories, bpId, userId));
        }
        else
        {
            nodesStatusMetrics.setAllNodeMetrics(edgeRepository.getAllNodeStatusMetrics(categories, bpId, userId));
        }

        LOGGER.info("node status metrics {}", nodesStatusMetrics);
        return nodesStatusMetrics;
    }

    @Override
    public int getRegionNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        TblRegionInfoExample example = new TblRegionInfoExample();
        TblRegionInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
        return (int)regionRepository.countByExample(example);
    }

    @Override
    public int getSiteNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        TblSiteInfoExample example = new TblSiteInfoExample();
        TblSiteInfoExample.Criteria criteria = example.createCriteria();
        criteria.andStatusNotEqualTo(ActiveStatus.REMOVED);

        if (StringUtils.isNotEmpty(bpId))
        {
            if (StringUtils.isEmpty(userId))
            {
                criteria.andForBpAdmin(labelProperty.getSiteBpId(), bpId, labelProperty.getSiteOwner(), userId);
            }
            else
            {
                criteria.andForBpUser(labelProperty.getSiteBpId(), bpId, labelProperty.getSiteOwner(), userId);
            }
        }
        else if (StringUtils.isNotEmpty(userId))
        {
            criteria.andForPersonalUser(labelProperty.getSiteBpId(), labelProperty.getSiteOwner(), userId);
        }

        return (int)siteRepository.countByExample(example);
    }

    @Override
    public int getNodeNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
        TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();
        criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);

        if (StringUtils.isNotEmpty(bpId))
        {
            criteria.andBpIdEqualTo(bpId);
        }
        if (StringUtils.isNotEmpty(userId))
        {
            criteria.andUserIdEqualTo(userId);
        }

        return (int)edgeRepository.countEdge(bpId, userId);
    }

    @Override
    public List<String> getNodeIds(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
        TblEdgeComputeInfoExample example = new TblEdgeComputeInfoExample();
        TblEdgeComputeInfoExample.Criteria criteria = example.createCriteria();

        criteria.andActiveStatusNotEqualTo(ActiveStatus.REMOVED);

        if (StringUtils.isNotEmpty(bpId))
        {
            if (StringUtils.isNotEmpty(userId))
            {
                criteria.andLabelJoin(labelProperty.getNodeBpId(), bpId, labelProperty.getNodeOwner(), userId);
            }
            else
            {
                criteria.andLabelEqualTo(labelProperty.getNodeBpId(), bpId);
            }
        }
        else
        {
            if (StringUtils.isNotEmpty(userId))
            {
                criteria.andLabelEqualTo(labelProperty.getNodeOwner(), userId);
            }
        }

        List<TblEdgeComputeInfo> tblEdgeComputeInfoList = edgeRepository.getEdges(example);

        return tblEdgeComputeInfoList.stream().map(TblEdgeComputeInfo::getNodeId).collect(Collectors.toList());
    }

    @Override
    public String getNodeHostIP(@ApiParam(name = "nodeId")String nodeId)
    {
        return edgeRepository.getHostIpv4(nodeId);
    }

    @Override
    public GetStatusMetricsRsp getEdgeStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId)
    {
            return edgeRepository.getEdgeStatusMetrics(userId, bpId);
    }
    
    /**
     *
     * @param site site id
     * @param labelSelector condition to filter node
     * @param nodeIds current nodes in site
     * @return
     */
    @Override
    public SelectNode getMultiNodeInSite(@ApiParam(name = "site")String site, @ApiParam(name = "labelSelector") List<LabelSelector> labelSelector,@ApiParam(name = "nodeIds") List<String> nodeIds)
    {
        List<TblEdgeComputeInfo> edgeComputeInfos = edgeRepository.getNodesBySite(site);
        if (CollectionUtils.isEmpty(edgeComputeInfos))
        {
            return null;
        }
        
        SelectNode selectNode = new SelectNode();
        selectNode.setSiteId(site);
        selectNode.setRegionId(edgeComputeInfos.get(0).getRegionId());
    
        boolean checkLabel = !CollectionUtils.isEmpty(labelSelector);
    
        List<String> newNodes = new ArrayList<>();
        List<String> oldNodes = new ArrayList<>();
        edgeComputeInfos.forEach(tblEdgeComputeInfo -> {
            boolean isNew = false;
            boolean isOld = false;
            String nodeId = tblEdgeComputeInfo.getNodeId();

            boolean siteExist = false;
            if (!CollectionUtils.isEmpty(nodeIds))
            {
                siteExist = nodeIds.contains(nodeId);
            }

            if (checkLabel)
            {
                Map<String, String> labels = edgeRepository.getNodeLabels(nodeId);
                Map<String, String> taints = edgeRepository.getNodeTaints(nodeId);
            
                if (acceptable(labelSelector, labels, taints))
                {
                    if (! siteExist)
                    {
                        isNew = true;
                    }
                }
                else
                {
                    if (siteExist)
                    {
                        isOld = true;
                    }
                }
            }
            else
            {
                if (! siteExist)
                {
                    isNew = true;
                }
            }
        
            if (isNew)
            {
                newNodes.add(nodeId);
            }
            else
            {
                if (isOld)
                {
                    oldNodes.add(nodeId);
                }
                if (!CollectionUtils.isEmpty(nodeIds))
                {
                    nodeIds.remove(nodeId);
                }

            }
        });

        selectNode.setNodes(new Pair<>(newNodes, oldNodes));
        return selectNode;
    }
    
    /**
     *
     * @param site
     * @param labelSelector
     * @param nodeId
     * @return
     */
    @Override
    public SelectNode getSingleNodeInSite(@ApiParam(name = "site")String site, @ApiParam(name = "labelSelector") List<LabelSelector> labelSelector, @ApiParam(name = "nodeId")String nodeId)
    {
        boolean checkLabel = ! CollectionUtils.isEmpty(labelSelector);
        
        if (! checkLabel && CollectionUtils.hasContent(nodeId))
        {
            return null;
        }

        if (StringUtils.isNotBlank(nodeId))
        {
            TblEdgeComputeInfo edgeInfo = edgeRepository.getEdgeNode(nodeId);
            if (edgeInfo.getActiveStatus() == ActiveStatus.ACITVE && edgeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
            {
                Map<String, String> labels = edgeRepository.getNodeLabels(nodeId);
                Map<String, String> taints = edgeRepository.getNodeTaints(nodeId);
                if (acceptable(labelSelector, labels, taints))
                {
                    return null;
                }
            }
        }


        List<TblEdgeComputeInfo> edgeComputeInfos = edgeRepository.getNodesBySite(site);
        if (CollectionUtils.isEmpty(edgeComputeInfos))
        {
            return null;
        }
    
        SelectNode selectNode = new SelectNode();
        selectNode.setSiteId(site);
        selectNode.setRegionId(edgeComputeInfos.get(0).getRegionId());
    
        
    
        String ndId = "";
        boolean isNew = false;
        boolean isOld = false;
    
        for (TblEdgeComputeInfo tblEdgeComputeInfo : edgeComputeInfos)
        {
            ndId = tblEdgeComputeInfo.getNodeId();

            boolean nodeExist = false;
            if (StringUtils.isNotBlank(nodeId))
            {
                nodeExist = nodeId.equals(ndId);
            }
    
            if (checkLabel)
            {
                Map<String, String> labels = edgeRepository.getNodeLabels(ndId);
                Map<String, String> taints = edgeRepository.getNodeTaints(ndId);

                if (acceptable(labelSelector, labels, taints))
                {
                    if (! nodeExist)
                    {
                        isNew = true;
                    }

                    TblEdgeComputeInfo edgeInfo = edgeRepository.getEdgeNode(ndId);
                    if (edgeInfo.getActiveStatus() == ActiveStatus.ACITVE && edgeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
                    {
                        break;
                    }

                }
                else
                {
                    if (nodeExist)
                    {
                        isOld = true;
                    }
                }
            }
            else
            {
                if (! nodeExist)
                {
                    isNew = true;
                }
                else
                {
                    return null;
                }

                TblEdgeComputeInfo edgeInfo = edgeRepository.getEdgeNode(ndId);
                if (edgeInfo.getActiveStatus() == ActiveStatus.ACITVE && edgeInfo.getOnlineStatus() == OnlineStatus.ONLINE)
                {
                    break;
                }
            }
        }
    
        List<String> newNodes = new ArrayList<>();
        List<String> oldNodes = new ArrayList<>();
        if (isNew)
        {
            newNodes.add(ndId);
        }
        else
        {
            if (isOld)
            {
                oldNodes.add(nodeId);
            }
        }
        
    
        selectNode.setNodes(new Pair<>(newNodes, oldNodes));
        return selectNode;
    }
    
    private enum LabelSelectorType
    {
        MUST("Must"),
        PREFER("Prefer"),
        MUST_NOT("MustNot"),
        PREFER_NOT("PreferNot");
        
        private String value;
        
        LabelSelectorType(String value)
        {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static LabelSelectorType fromValue(String value)
        {
            for (LabelSelectorType s : LabelSelectorType.values())
            {
                if (s.getValue().equals(value))
                {
                    return s;
                }
            }
            return null;
        }
    }
    
    /**
     * In 　 　label的值在某个列表中
     * NotIn label的值不在某个列表中
     * Exists 某个label存在
     * DoesNotExist 某个label不存在
     * Gt label的值大于某个值（字符串比较）
     * Lt label的值小于某个值（字符串比较）
     */
    private enum LabelSelectorOperator
    {
        EQ("Eq"),
        IN("In"),
        NOT_IN("NotIn"),
        EXISTS("Exists"),
        NOT_EXISTS("NotExists"),
        GT("Gt"),
        LT("Lt"),
        GTE("Gte"),
        LTE("Lte");
        
        private String value;
        
        LabelSelectorOperator(String value)
        {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static LabelSelectorOperator fromValue(String value)
        {
            for (LabelSelectorOperator s : LabelSelectorOperator.values())
            {
                if (s.getValue().equals(value))
                {
                    return s;
                }
            }
            return null;
        }
    }
    
    
    
    /**
     * 若是存在于label中，并且取值满足，返回true
     * 若是污点中存在，取值满足，返回false
     * 若是label中不存在，taints中也不存在，返回接受
     * @param labelSelector
     * @param labels
     * @param taints
     * @return
     */
    private boolean acceptable(List<LabelSelector>labelSelector, Map<String, String> labels, Map<String, String> taints)
    {
        for (LabelSelector selector : labelSelector)
        {
            LabelSelectorOperator operator = LabelSelectorOperator.fromValue(selector.getOperator());
            LabelSelectorType selectorType = LabelSelectorType.fromValue(selector.getType());
            
            boolean checkRet =  checkLabels(labels, selector, operator);
            if (selectorType.getValue().equals(LabelSelectorType.MUST.getValue()))
            {
                if (! checkRet)
                {
                    return false;
                }
            }
    
            if (selectorType.getValue().equals(LabelSelectorType.MUST_NOT.getValue()))
            {
                if (checkRet)
                {
                    return false;
                }
            }
    
            boolean checkTaintRet =  checkLabels(taints, selector, operator);
            if (selectorType.getValue().equals(LabelSelectorType.MUST.getValue()))
            {
                if (checkTaintRet)
                {
                    return false;
                }
            }
    
            if (selectorType.getValue().equals(LabelSelectorType.MUST_NOT.getValue()))
            {
                if (!checkTaintRet )
                {
                    return false;
                }
            }
        }
        return true;
    }
    
    /**
     * 用于检查标签的匹配情况
     * @param labels
     * @param selector
     * @param operator
     * @return
     */
    boolean checkLabels(Map<String, String> labels, LabelSelector selector, LabelSelectorOperator operator)
    {
        String desireValue = selector.getValue();
        boolean checkRet = false;
        if (! CollectionUtils.isEmpty(labels))
        {
            String value = labels.get(selector.getKey());
        
            if (CollectionUtils.hasContent(value))
            {
                value = value.trim();
                checkRet = labelOperatorCheck(operator, desireValue, value);
            }
            else
            {
                if (operator.equals(LabelSelectorOperator.NOT_EXISTS))
                {
                    checkRet = true;
                }
            }
        }
        else
        {
            if (operator.equals(LabelSelectorOperator.NOT_EXISTS))
            {
                checkRet = true;
            }
        }
        return checkRet;
    }
    
    /**
     * 用于检查值的匹配情况
     * @param operator
     * @param desireValue
     * @param value
     * @return
     */
    boolean labelOperatorCheck(LabelSelectorOperator operator, String desireValue, String value)
    {
        boolean checkRet = false;
        switch (operator)
        {
            case IN:
                checkRet = value.toLowerCase().contains(desireValue.toLowerCase());
                break;
            case NOT_IN:
                checkRet = ! value.toLowerCase().contains(desireValue.toLowerCase());
                break;
            case EXISTS:
                checkRet = true;
                break;
            case NOT_EXISTS:
                break;
            case GT:
                checkRet = value.compareToIgnoreCase(desireValue) > 0;
                break;
            case LT:
                checkRet = value.compareToIgnoreCase(desireValue) < 0;
                break;
            case GTE:
                checkRet = value.compareToIgnoreCase(desireValue) >= 0;
                break;
            case LTE:
                checkRet = value.compareToIgnoreCase(desireValue) <= 0;
                break;
        }
        return checkRet;
    }
    
    /**
     *
     * @param labelSelector    筛选条件
     * @param sites            已使用的站点列表
     * @return
     */
    @Override
    public Pair<Set<String>, Set<String>> getSites(@ApiParam(name = "labelSelector")List<LabelSelector> labelSelector, @ApiParam(name = "sites") Set<String> sites)
    {
        List<TblSiteInfo> tblSiteInfoList = siteRepository.getAllSite();
        if (CollectionUtils.isEmpty(tblSiteInfoList))
        {
            return null;
        }
    
        boolean checkLabel = !CollectionUtils.isEmpty(labelSelector);
        
        Set<String> newSites = new HashSet<>();
        Set<String> oldSites = new HashSet<>();
        tblSiteInfoList.forEach(tblSiteInfo -> {
            boolean isNew = false;
            boolean isOld = false;
            boolean siteExist = false;
            if (!CollectionUtils.isEmpty(sites))
            {
                siteExist = sites.contains(tblSiteInfo.getSiteId());
            }

            if (checkLabel)
            {
                Map<String, String> labels = gson.fromJson(tblSiteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
                Map<String, String> taints = gson.fromJson(tblSiteInfo.getTaints(), new TypeToken<Map<String, String>>(){}.getType());
    
                if (acceptable(labelSelector, labels, taints))
                {
                    if (! siteExist)
                    {
                        isNew = true;
                    }
                }
                else
                {
                    if (siteExist)
                    {
                        isOld = true;
                    }
                }
            }
            else
            {
                if (! siteExist)
                {
                    isNew = true;
                }
            }
            
            if (isNew)
            {
                newSites.add(tblSiteInfo.getSiteId());
            }
            else
            {
                if (isOld)
                {
                    oldSites.add(tblSiteInfo.getSiteId());
                }
                if (CollectionUtils.isEmpty(sites))
                {
                    sites.remove(tblSiteInfo.getSiteId());
                }

            }
        });
        
        if (! CollectionUtils.isEmpty(sites))
        {
            oldSites.addAll(sites);
        }
        return new Pair<>(newSites, oldSites);
    }
}
