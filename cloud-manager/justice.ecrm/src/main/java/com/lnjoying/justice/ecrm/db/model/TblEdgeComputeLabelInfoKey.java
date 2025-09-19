package com.lnjoying.justice.ecrm.db.model;

import java.io.Serializable;

public class TblEdgeComputeLabelInfoKey implements Serializable {
    private String nodeId;

    private String labelKey;

    private static final long serialVersionUID = 1L;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    public String getLabelKey() {
        return labelKey;
    }

    public void setLabelKey(String labelKey) {
        this.labelKey = labelKey == null ? null : labelKey.trim();
    }
}