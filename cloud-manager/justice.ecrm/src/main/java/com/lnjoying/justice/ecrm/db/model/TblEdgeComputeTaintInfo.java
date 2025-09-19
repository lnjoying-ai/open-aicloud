package com.lnjoying.justice.ecrm.db.model;

import java.io.Serializable;

public class TblEdgeComputeTaintInfo extends TblEdgeComputeTaintInfoKey implements Serializable {
    private String taintValue;

    private static final long serialVersionUID = 1L;

    public String getTaintValue() {
        return taintValue;
    }

    public void setTaintValue(String taintValue) {
        this.taintValue = taintValue == null ? null : taintValue.trim();
    }
}