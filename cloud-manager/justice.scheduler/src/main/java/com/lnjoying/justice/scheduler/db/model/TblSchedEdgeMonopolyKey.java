package com.lnjoying.justice.scheduler.db.model;

import java.io.Serializable;

public class TblSchedEdgeMonopolyKey implements Serializable {
    private String nodeId;

    private String refId;

    private static final long serialVersionUID = 1L;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId == null ? null : refId.trim();
    }
}