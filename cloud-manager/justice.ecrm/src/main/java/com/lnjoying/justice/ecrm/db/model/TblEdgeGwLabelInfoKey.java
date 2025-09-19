package com.lnjoying.justice.ecrm.db.model;

import java.io.Serializable;

public class TblEdgeGwLabelInfoKey implements Serializable {
    private String labelKey;

    private String nodeId;

    private static final long serialVersionUID = 1L;

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey == null ? null : labelKey.trim();
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }
}