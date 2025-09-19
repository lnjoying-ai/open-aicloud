package com.lnjoying.justice.schema.service.cmp;

import com.lnjoying.justice.schema.common.CombRetPacket;
import com.lnjoying.justice.schema.entity.cluster.ClusterAuthParams;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuResourceMetric;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuResourceMetrics;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuUsages;
import com.lnjoying.justice.schema.entity.cmp.RpcGpuVmInstances;
import com.lnjoying.justice.schema.entity.scheduler.BindResourcesResult;
import com.lnjoying.justice.schema.entity.scheduler.SchedulerResult;
import com.lnjoying.justice.schema.entity.stat.GetCloudNumMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetStatusMetricsRsp;
import com.lnjoying.justice.schema.entity.stat.GetVmInstanceStatusMetricsRsp;
import com.lnjoying.justice.schema.msg.EdgeMessage;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

public interface CloudManagerService
{
    int deliver(@ApiParam(name = "message") EdgeMessage edgeMessage);
    int workerReg(@ApiParam(name = "message") EdgeMessage edgeMessage);
    int workerLogin(@ApiParam(name = "message") EdgeMessage edgeMessage);
    int getCloudNum(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "siteId")String siteId);
    BindResourcesResult bindResources(@ApiParam(name = "schedulerResult") SchedulerResult schedulerResult);
    void updateCloudAgentStatus(@ApiParam(name = "agentId") String agentId, @ApiParam(name = "status") Integer status);
    CombRetPacket authCloud(@ApiParam(name = "cloudAuthParams") ClusterAuthParams cloudAuthParams); //status:auth result, Object:set ClusterEntity
    GetStatusMetricsRsp getCloudStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    GetStatusMetricsRsp getCloudHealthStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    GetVmInstanceStatusMetricsRsp getCloudVmInstanceStatusMetrics(@ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    GetCloudNumMetricsRsp getCloudNumMetrics(@ApiParam(name = "filter")String filter, @ApiParam(name = "userId")String userId, @ApiParam(name = "bpId")String bpId);
    List<RpcFlavorInfo> getCloudFlavor(@ApiParam(name = "cloudId")String cloudId);
    RpcFlavorInfo getCloudFlavorById(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "flavorId")String flavorId);
    List<RpcDiskSpec> getCloudDiskSpec(@ApiParam(name = "cloudId")String cloudId);
    RpcDiskSpec getCloudDiskSpecById(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "diskSpecId")String diskSpecId);
    CombRetPacket getCloudAgent(@ApiParam(name = "cloudId")String cloudId);
    String getCloudName(@ApiParam(name = "cloudId")String cloudId);
    boolean checkOperUserByVmInstanceId(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "vmInstanceId")String vmInstanceId, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);
    boolean checkOperUserByHypervisorNodeIp(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "hypervisorNodeIp")String hypervisorNodeIp, @ApiParam(name = "bpId")String bpId, @ApiParam(name = "userId")String userId);
    String getVmInstanceName(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "vmInstanceId")String vmInstanceId);
    String getVolumeName(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "volumeId")String volumeId);
    RpcCloudInfos getClouds(@ApiParam(name = "pageNum")Integer pageNum, @ApiParam(name = "pageSize")Integer pageSize);
    int isVmCreateSuccess(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "vmInstanceId")String vmInstanceId);
    int updateCloudProvider(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "providerId")String providerId);
    List<String> getGpuNames(@ApiParam(name = "cloudIds")List<String> cloudIds, @ApiParam(name = "providerId")String providerId);
    RpcGpuResourceMetrics getGpuResourceMetrics(@ApiParam(name = "cloudIds")List<String> cloudIds, @ApiParam(name = "gpuName")String gpuName, @ApiParam(name = "providerId")String providerId, @ApiParam(name = "pageNum")Integer pageNum, @ApiParam(name = "pageSize")Integer pageSize);
    RpcGpuResourceMetric getGpuResourceMetric(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "gpuName")String gpuName);
    RpcGpuUsages getGpuUsage(@ApiParam(name = "cloudIds")List<String> cloudIds, @ApiParam(name = "gpuName")String gpuName, @ApiParam(name = "providerId")String providerId);
    RpcGpuVmInstances getGpuVmInstances(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "gpuName")String gpuName, @ApiParam(name = "pageNum")Integer pageNum, @ApiParam(name = "pageSize")Integer pageSize);
    List<RpcEipPool> getCloudEipPool(@ApiParam(name = "cloudId")String cloudId);
    RpcEipPool getCloudEipPoolById(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "poolId")String poolId);
    String getEipAddr(@ApiParam(name = "cloudId")String cloudId, @ApiParam(name = "eipId")String eipId);

    enum VmCreateStatus
    {
        CREATED(0),
        CREATEING(1),
        CREATE_FAILED(2);

        private final int code;

        VmCreateStatus(Integer code)
        {
            this.code = code;
        }

        public int getCode()
        {
            return code;
        }
    }

    @Data
    class RpcFlavorInfo
    {
        protected Integer cpu;
        protected Integer mem;
        protected String flavorId;
        protected String cloudId;
        protected String productId;
        protected String name;
    }

    @Data
    public static class RpcDiskSpec implements Serializable
    {
        private String cloudId;

        private String diskSpecId;

        private String diskType;

        private String vendor;

        private String model;

        private Long size;

        private Long transSpeed;
    }

    @Data
    public static class RpcEipPool implements Serializable
    {
        private String cloudId;

        private String poolId;

        private String name;

        private String description;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    class RpcCloudInfos
    {
        protected List<RpcCloudInfo> clouds;
        protected Long totalNum;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class RpcCloudInfo
    {
        protected String cloudId;
        protected String cloudName;
    }
}
