package com.lnjoying.justice.cmp.db.model;

import java.io.Serializable;

public class TblCmpOsPortforwardings extends TblCmpOsPortforwardingsKey implements Serializable {
    private String floatingipId;

    private Integer externalPort;

    private String internalNeutronPortId;

    private String protocol;

    private String socket;

    private String eeBp;

    private String eeUser;

    private Integer eeStatus;

    private static final long serialVersionUID = 1L;

    public String getFloatingipId() {
        return floatingipId;
    }

    public void setFloatingipId(String floatingipId) {
        this.floatingipId = floatingipId == null ? null : floatingipId.trim();
    }

    public Integer getExternalPort() {
        return externalPort;
    }

    public void setExternalPort(Integer externalPort) {
        this.externalPort = externalPort;
    }

    public String getInternalNeutronPortId() {
        return internalNeutronPortId;
    }

    public void setInternalNeutronPortId(String internalNeutronPortId) {
        this.internalNeutronPortId = internalNeutronPortId == null ? null : internalNeutronPortId.trim();
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol == null ? null : protocol.trim();
    }

    public String getSocket() {
        return socket;
    }

    public void setSocket(String socket) {
        this.socket = socket == null ? null : socket.trim();
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

    public Integer getEeStatus() {
        return eeStatus;
    }

    public void setEeStatus(Integer eeStatus) {
        this.eeStatus = eeStatus;
    }
}