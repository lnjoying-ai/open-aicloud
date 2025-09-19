package com.lnjoying.justice.cluster.manager.db.model;

import java.io.Serializable;
import java.util.Date;

public class TblClusterSaInfo implements Serializable {
    private String clusterId;

    private String secretToken;

    private String secretName;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId == null ? null : clusterId.trim();
    }

    public String getSecretToken() {
        return secretToken;
    }

    public void setSecretToken(String secretToken) {
        this.secretToken = secretToken == null ? null : secretToken.trim();
    }

    public String getSecretName() {
        return secretName;
    }

    public void setSecretName(String secretName) {
        this.secretName = secretName == null ? null : secretName.trim();
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