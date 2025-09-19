package com.lnjoying.justice.aos.util;

import com.lnjoying.justice.aos.domain.model.DockerComposeYaml;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.entity.dev.*;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.PostConstruct;
import java.util.*;

import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/10/30 20:36
 */

@Slf4j
@Component
public class StackUtils
{
    static Yaml yaml = new Yaml();

    @Autowired
    private CombRpcService combRpcService;

    private static StackUtils stackUtils;

    @PostConstruct
    public void init() {
        stackUtils = this;
        stackUtils.combRpcService = this.combRpcService;
    }


    public static DevNeedInfo addDefaultMonitorDevNeedInfo()
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

    public static List<TargetNode> addTargetNode(String nodeId)
    {
        List<TargetNode> targetNodes = new ArrayList<>();

        RegionResourceService.NodeInfo nodeInfo = stackUtils.combRpcService.getRegionResourceService().getNodeInfoByNodeId(nodeId);
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

    public static SchedulingStrategy addDefaultSchedulingStrategy()
    {
        SchedulingStrategy schedulingStrategy = new SchedulingStrategy();
        Map<String, List<LabelSelector>> labelSelectorMap = new HashMap<>();
        labelSelectorMap.put("node", Collections.emptyList());
        labelSelectorMap.put("site", Collections.emptyList());
        schedulingStrategy.setLabelSelectorMap(labelSelectorMap);
        return schedulingStrategy;
    }

    public static Map<String, String> addDefaultExtraParams()
    {
        Map<String, String> extraParams = new HashMap<>();
        extraParams.put("image_pull_policy", "IfNotPresent");
        return extraParams;
    }


    public static List<String> getStackComposeImages(String stackCompose) {
        DockerComposeYaml composeInfo = yaml.loadAs(stackCompose, DockerComposeYaml.class);
        return composeInfo.getImages();
    }
}
