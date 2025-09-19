package com.lnjoying.justice.cis.controller.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.schema.entity.TipMessage;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DockerContainerInfo
{
    private String name;

    private String id;

    private String containerId;

    private String imageName;

    private InstanceState.StatusCode status;

    private String ip = "";

    private List<String> cmd;

    private String bpId;

    private String bpName;

    private String userId;

    private String userName;

    private String nodeId;

    private String nodeName;

    private TipMessage tip_message;

    private String siteId;

    private String siteName;

    private String regionId;

    private String regionName;

    private String createTime;

    private String stopTime = "";

    private String restartPolicy;

    private String refId;
}
