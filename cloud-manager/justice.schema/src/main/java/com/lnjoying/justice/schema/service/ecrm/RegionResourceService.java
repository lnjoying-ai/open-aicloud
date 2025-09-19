package com.lnjoying.justice.schema.service.ecrm;

import com.lnjoying.justice.schema.entity.dev.LabelSelector;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.NodesStatusMetrics;
import com.micro.core.common.Pair;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * region Service
 *
 * @author merak
 **/
public interface RegionResourceService {

    /**
     * get all regionIds exclude removed status
     * @return
     */
    Set<String> getRegionIds();

    /**
     * get regionIds by regionName full fuzzy match exclude removed status
     * @param regionName
     * @return regionIds
     */
    Set<String> getRegionIdsByName(@ApiParam(name = "regionName")String regionName);

    /**
     * get Regions by regionIds
     * @param regionIds
     * @return
     */
    List<Region> getRegionByRegionIds(@ApiParam(name = "regionIds")Set<String> regionIds);

    /**
     * Get the nodes under the regions
     * @param regionIds
     * @return
     */
    List<RegionDto> selectAllNodesByRegionIds(@ApiParam(name = "regionIds")Set<String> regionIds);

    /**
     * get region name by region id
     * @param regionId
     * @return
     */
    String getRegionNameById(@ApiParam(name = "regionId")String regionId);

    /**
     * get site name by site id
     * @param siteId
     * @return
     */
    String getSiteNameById(@ApiParam(name = "siteId")String siteId);

    /**
     * get node name by node id
     * @param nodeId
     * @return
     */
    String getNodeNameById(@ApiParam(name = "nodeId")String nodeId);

    /**
     * Check if the user can operate the node
     * @param nodeId
     * @param bpId
     * @param userId
     * @return
     */
    int checkOperUser(@ApiParam(name = "nodeId")String nodeId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);

    /**
     * get regionId, siteId, nodeId by nodeId
     * @param nodeId
     * @return
     */
    NodeInfo getNodeInfoByNodeId(@ApiParam(name = "nodeId")String nodeId);

    GetStatusMetricsRsp getEdgeStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    
    //to avoid a huge data, process one site data by one time.
    
    /**
     *根据传入的参数，筛选出当前新增加的节点，和陈旧的节点
     * @param site site id
     * @param labelSelector condition to filter node
     * @param nodeIds current nodes in site
     * @return first element:new node, second element:old node
     */
    SelectNode getMultiNodeInSite(@ApiParam(name = "site")String site, @ApiParam(name = "labelSelector") List<LabelSelector> labelSelector,@ApiParam(name = "nodeIds") List<String> nodeIds);
    
    /**
     *根据传入的参数，获得目标节点。若传入的节点符合要求，那么返回的节点和传入的一致。否则返回符合要求的节点，若没有满足的，返回为空
     * @param site
     * @param labelSelector
     * @param nodeId
     * @return
     */
    SelectNode getSingleNodeInSite(@ApiParam(name = "site")String site, @ApiParam(name = "labelSelector") List<LabelSelector> labelSelector, @ApiParam(name = "nodeId")String nodeId);
    
    
    /**
     * 根据指定的条件筛选站点
     * @param labelSelector    筛选条件
     * @param sites            已使用的站点列表
     * @return  first：新传的站点， second:旧的站点
     */
    Pair<Set<String>, Set<String>> getSites(@ApiParam(name = "labelSelector")List<LabelSelector> labelSelector, @ApiParam(name = "sites") Set<String> sites);
    
    final class SelectNode implements Serializable{
        
        private static final long serialVersionUID = -1881299030877564176L;
        
        private  String regionId;
        
        private  String regionName;
        
        private String siteId;
        private String siteName;
        
        private Pair<List<String> ,List<String>> nodes;
        
        public SelectNode() {};
        
        public String getRegionId() {
            return regionId;
        }
        
        public String getRegionName() {
            return regionName;
        }
        
        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }
        
        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }
    
        public String getSiteId() {
            return siteId;
        }
    
        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }
    
        public String getSiteName() {
            return siteName;
        }
    
        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }
    
        public Pair<List<String> ,List<String>>  getNodes() {
            return nodes;
        }
    
        public void setNodes(Pair<List<String> ,List<String>> nodes) {
            this.nodes = nodes;
        }
    }
    
    final class Region implements Serializable{

        private static final long serialVersionUID = -1881299030877564176L;

        private  String regionId;

        private  String regionName;

        public Region() {};
    
        public Region(String regionId, String regionName) {
            this.regionId = regionId;
            this.regionName = regionName;
        }

        public String getRegionId() {
            return regionId;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }
    }

    final class RegionDto implements Serializable {

        private static final long serialVersionUID = 5676245101563058526L;

        private String regionId;

        private String regionName;

        private Set<SiteDto> children;

        public RegionDto() {}

        public RegionDto(String regionId, String regionName) {
            this.regionId = regionId;
            this.regionName = regionName;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getRegionName() {
            return regionName;
        }

        public void setRegionName(String regionName) {
            this.regionName = regionName;
        }

        public Set<SiteDto> getChildren() {
            return children;
        }

        public void setChildren(Set<SiteDto> children) {
            this.children = children;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RegionDto regionDto = (RegionDto) o;
            return Objects.equals(regionId, regionDto.regionId) && Objects.equals(regionName, regionDto.regionName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(regionId, regionName);
        }
    }

    final class SiteDto implements Serializable{

        private static final long serialVersionUID = 261451173805988041L;

        private String siteId;

        private String siteName;

        private Set<NodeDto> children;

        public SiteDto(){}

        public SiteDto(String siteId, String siteName) {
            this.siteId = siteId;
            this.siteName = siteName;
        }

        public String getSiteId() {
            return siteId;
        }

        public void setSiteId(String siteId) {
            this.siteId = siteId;
        }

        public String getSiteName() {
            return siteName;
        }

        public void setSiteName(String siteName) {
            this.siteName = siteName;
        }

        public Set<NodeDto> getChildren() {
            return children;
        }

        public void setChildren(Set<NodeDto> children) {
            this.children = children;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            SiteDto siteDto = (SiteDto) o;
            return siteId.equals(siteDto.siteId) && siteName.equals(siteDto.siteName);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(siteId, siteName);
        }
    }

    final class NodeDto implements Serializable{

        private static final long serialVersionUID = 248593838193094806L;

        private String nodeId;

        private String nodeName;

        public NodeDto() {}

        public NodeDto(String nodeId, String nodeName) {
            this.nodeId = nodeId;
            this.nodeName = nodeName;
        }

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            NodeDto nodeDto = (NodeDto) o;
            return nodeId.equals(nodeDto.nodeId) && nodeName.equals(nodeDto.nodeName);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(nodeId, nodeName);
        }
    }

    public enum NodeStatus
    {
        /**
         * node not exist
         */
        NODE_NOT_EXIST(1),

        /**
         * permission denied
         */
        PERMISSION_DENIED(2),

        /**
         * user not grant
         */
        USER_NOT_GRANT(3),

        /**
         * can operate
         */
        CAN_OPERATE(4);

        private final int value;

        NodeStatus(int value)
        {
            this.value = value;
        }

        public int value()
        {
            return this.value;
        }
    }

    public final class NodeInfo implements Serializable
    {
        private static final long serialVersionUID = -4874314358143879016L;

        private String regionId;

        private String siteId;

        private String nodeId;

        private String regionName;

        private String siteName;

        private String nodeName;

        public NodeInfo(){};

        public NodeInfo(String regionId, String siteId, String nodeId)
        {
            this.regionId = regionId;
            this.siteId = siteId;
            this.nodeId = nodeId;
        }

        public NodeInfo(String regionId, String regionName, String siteId, String siteName, String nodeId, String nodeName)
        {
            this.regionId = regionId;
            this.regionName = regionName;
            this.siteId = siteId;
            this.siteName = siteName;
            this.nodeId = nodeId;
            this.nodeName = nodeName;
        }

        public String getRegionId()
        {
            return regionId;
        }

        public void setRegionId(String regionId)
        {
            this.regionId = regionId;
        }

        public String getSiteId()
        {
            return siteId;
        }

        public void setSiteId(String siteId)
        {
            this.siteId = siteId;
        }

        public String getNodeId()
        {
            return nodeId;
        }

        public void setNodeId(String nodeId)
        {
            this.nodeId = nodeId;
        }

        public String getRegionName()
        {
            return regionName;
        }

        public void setRegionName(String regionName)
        {
            this.regionName = regionName;
        }

        public String getSiteName()
        {
            return siteName;
        }

        public void setSiteName(String siteName)
        {
            this.siteName = siteName;
        }

        public String getNodeName()
        {
            return nodeName;
        }

        public void setNodeName(String nodeName)
        {
            this.nodeName = nodeName;
        }
    }

    NodesStatusMetrics getNodeStatusMetrics(@ApiParam(name = "regionId")String regionId, @ApiParam(name = "siteId")String siteId, @ApiParam(name = "filter")String filter, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);

    int getRegionNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

    int getSiteNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

    int getNodeNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

    List<String> getNodeIds(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);

    String getNodeHostIP(@ApiParam(name = "nodeId")String nodeId);



    @Data
    public class EdgeStatusCountsRsp
    {
        private String onlineStatus;

        private String bpId;

        private String userId;

        private Integer total;
    }

}
