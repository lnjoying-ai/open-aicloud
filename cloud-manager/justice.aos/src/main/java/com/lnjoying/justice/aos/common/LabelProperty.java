package com.lnjoying.justice.aos.common;

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
}
