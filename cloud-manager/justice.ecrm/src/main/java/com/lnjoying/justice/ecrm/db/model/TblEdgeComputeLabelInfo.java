package com.lnjoying.justice.ecrm.db.model;

import java.io.Serializable;

public class TblEdgeComputeLabelInfo extends TblEdgeComputeLabelInfoKey implements Serializable {
    private String labelValue;

    private static final long serialVersionUID = 1L;

    public String getLabelValue() {
        return labelValue;
    }

    public void setLabelValue(String labelValue) {
        this.labelValue = labelValue == null ? null : labelValue.trim();
    }
}