package com.lnjoying.justice.ecrm.db.model;

import java.io.Serializable;

public class TblEdgeComputeTaintInfoKey implements Serializable {
    private String nodeId;

    private String taintKey;

    private static final long serialVersionUID = 1L;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getTaintKey() {
        return taintKey;
    }

    public void setTaintKey(String taintKey) {
        this.taintKey = taintKey == null ? null : taintKey.trim();
    }
}