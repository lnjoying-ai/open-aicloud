package com.lnjoying.justice.cis.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblContainerRunInfo implements Serializable {
    private String runId;

    private String instId;

    private Date startTime;

    private Date stopTime;

    private Integer state;

    private String instDetailInfo;

    private static final long serialVersionUID = 1L;

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId == null ? null : runId.trim();
    }

    public String getInstId() {
        return instId;
    }

    public void setInstId(String instId) {
        this.instId = instId == null ? null : instId.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getInstDetailInfo() {
        return instDetailInfo;
    }

    public void setInstDetailInfo(String instDetailInfo) {
        this.instDetailInfo = instDetailInfo == null ? null : instDetailInfo.trim();
    }
}