package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpEsFirewallBindings extends TblCmpEsFirewallBindingsKey implements Serializable {
    private Integer eeStatus;

    private String eeBp;

    private String eeUser;

    private static final long serialVersionUID = 1L;

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
    }

    public String getEeBp() {
        return eeBp;
    }

    public void setEeBp(String eeBp) {
        this.eeBp = eeBp == null ? null : eeBp.trim();
    }

    public String getEeUser() {
        return eeUser;
    }

    public void setEeUser(String eeUser) {
        this.eeUser = eeUser == null ? null : eeUser.trim();
    }
}