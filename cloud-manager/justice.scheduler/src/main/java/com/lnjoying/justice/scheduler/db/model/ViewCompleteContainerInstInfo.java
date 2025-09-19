package com.lnjoying.justice.scheduler.db.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ViewCompleteContainerInstInfo implements Serializable {
    private String instId;

    private String refId;

    private String specId;

    private String userId;

    private String bpId;

    private String nodeId;

    private String regionId;

    private String siteId;

    private String containerName;

    private Integer status;

    private String containerParams;

    private String inspetContainerParams;

    private String imageName;

    private String cmd;

    private String ip;

    private Integer cpuNum;

    private Long memLimit;

    private Long diskLimit;

    private Integer transmitBandLimit;

    private Integer recvBandLimit;

    private Integer gpuNum;

    private String devNeedInfo;

    private Boolean autoRun;

    private Integer replicaNum;

    private Integer sendCreateNum;

    private Integer startNum;

    private Integer failNum;

    private Date createTime;

    private Date updateTime;

    private Date removeTime;
}