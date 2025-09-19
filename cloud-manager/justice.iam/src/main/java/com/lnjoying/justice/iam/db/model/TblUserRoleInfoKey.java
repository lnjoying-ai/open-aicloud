package com.lnjoying.justice.iam.db.model;

import java.io.Serializable;

public class TblUserRoleInfoKey implements Serializable {
    private String userId;

    private Long roleId;

    private static final long serialVersionUID = 1L;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}