package com.lnjoying.justice.cluster.manager.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblClusterCertInfo implements Serializable {
    private String clusterId;

    private String cert;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId == null ? null : clusterId.trim();
    }

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert == null ? null : cert.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}