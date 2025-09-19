package com.lnjoying.justice.ecrm.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:application.properties")
public class LabelProperty
{
    @Value("${laebl.project.region:com.justice.region}")
    private String regionProjectName;

    @Value("${laebl.project.site:com.justice.site}")
    private String siteProjectName;

    @Value("${laebl.project.node:com.justice.node}")
    private String nodeProjectName;

    @Value("${laebl.project.region:com.justice.region}/orchestration")
    private String regionOrchestration;

    @Value("${laebl.project.region:com.justice.region}/owner")
    private String regionOwner;

    @Value("${laebl.project.site:com.justice.site}/owner")
    private String siteOwner;

    @Value("${laebl.project.site:com.justice.site}/orchestration")
    private String siteOrchestration;

    @Value("${laebl.project.node:com.justice.node}/orchestration")
    private String nodeOrchestration;

    @Value("${laebl.project.node:com.justice.node}/resource:gpu")
    private String nodeGpu;

    @Value("${laebl.project.node:com.justice.node}/os")
    private String nodeOs;

    @Value("${laebl.project.node:com.justice.node}/core:version")
    private String nodeCoreVersion;

    @Value("${laebl.project.node:com.justice.node}/docker:version")
    private String nodeDockerVersion;

    @Value("${laebl.project.node:com.justice.node}/owner")
    private String nodeOwner;

    @Value("${laebl.project.node:com.justice.node}/bpId")
    private String nodeBpId;

    @Value("${laebl.project.node:com.justice.node}/userId")
    private String nodeUserId;

    @Value("${laebl.project.node:com.justice.node}/ip:external")
    private String externalAddress;

    @Value("${laebl.project.node:com.justice.node}/ip:internal")
    private String internalAddress;

    @Value("${laebl.project.region:com.justice.region}/bpId")
    private String regionBpId;

    @Value("${laebl.project.site:com.justice.site}/bpId")
    private String siteBpId;

    @Value("com.justice.region/region_id")
    private String regionId;

    @Value("com.justice.region/gw")
    private String gw;

    @Value("com.justice.site/site_id")
    private String siteId;

    @Value("com.justice.node/node_name")
    private String nodeName;

}
