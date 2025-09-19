package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpHypervisorNodeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected Integer startRow;

    protected Integer pageSize;

    public Integer getStartRow()
    {
        return startRow;
    }

    public void setStartRow(Integer startRow) { this.startRow = startRow;}

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public TblCmpHypervisorNodeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
        startRow = null;
        pageSize = null;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andCloudIdIsNull() {
            addCriterion("cloud_id is null");
            return (Criteria) this;
        }

        public Criteria andCloudIdIsNotNull() {
            addCriterion("cloud_id is not null");
            return (Criteria) this;
        }

        public Criteria andCloudIdEqualTo(String value) {
            addCriterion("tbl_cmp_hypervisor_node.cloud_id =", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotEqualTo(String value) {
            addCriterion("cloud_id <>", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdGreaterThan(String value) {
            addCriterion("cloud_id >", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdGreaterThanOrEqualTo(String value) {
            addCriterion("cloud_id >=", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdLessThan(String value) {
            addCriterion("cloud_id <", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdLessThanOrEqualTo(String value) {
            addCriterion("cloud_id <=", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdLike(String value) {
            addCriterion("cloud_id like", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotLike(String value) {
            addCriterion("cloud_id not like", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdIn(List<String> values) {
            addCriterion("cloud_id in", values, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotIn(List<String> values) {
            addCriterion("cloud_id not in", values, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdBetween(String value1, String value2) {
            addCriterion("cloud_id between", value1, value2, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotBetween(String value1, String value2) {
            addCriterion("cloud_id not between", value1, value2, "cloudId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNull() {
            addCriterion("node_id is null");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNotNull() {
            addCriterion("node_id is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIdEqualTo(String value) {
            addCriterion("tbl_cmp_hypervisor_node.node_id =", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotEqualTo(String value) {
            addCriterion("node_id <>", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThan(String value) {
            addCriterion("node_id >", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("node_id >=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThan(String value) {
            addCriterion("node_id <", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThanOrEqualTo(String value) {
            addCriterion("node_id <=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLike(String value) {
            addCriterion("node_id like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotLike(String value) {
            addCriterion("node_id not like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIn(List<String> values) {
            addCriterion("tbl_cmp_hypervisor_node.node_id in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotIn(List<String> values) {
            addCriterion("node_id not in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdBetween(String value1, String value2) {
            addCriterion("node_id between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotBetween(String value1, String value2) {
            addCriterion("node_id not between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdIsNull() {
            addCriterion("instance_id is null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdIsNotNull() {
            addCriterion("instance_id is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdEqualTo(String value) {
            addCriterion("instance_id =", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotEqualTo(String value) {
            addCriterion("instance_id <>", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdGreaterThan(String value) {
            addCriterion("instance_id >", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("instance_id >=", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdLessThan(String value) {
            addCriterion("instance_id <", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdLessThanOrEqualTo(String value) {
            addCriterion("instance_id <=", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdLike(String value) {
            addCriterion("instance_id like", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotLike(String value) {
            addCriterion("instance_id not like", value, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdIn(List<String> values) {
            addCriterion("instance_id in", values, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotIn(List<String> values) {
            addCriterion("instance_id not in", values, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdBetween(String value1, String value2) {
            addCriterion("instance_id between", value1, value2, "instanceId");
            return (Criteria) this;
        }

        public Criteria andInstanceIdNotBetween(String value1, String value2) {
            addCriterion("instance_id not between", value1, value2, "instanceId");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIsNull() {
            addCriterion("phase_status is null");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIsNotNull() {
            addCriterion("phase_status is not null");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusEqualTo(Integer value) {
            addCriterion("tbl_cmp_hypervisor_node.phase_status =", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_hypervisor_node.phase_status <>", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusGreaterThan(Integer value) {
            addCriterion("phase_status >", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("phase_status >=", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusLessThan(Integer value) {
            addCriterion("phase_status <", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusLessThanOrEqualTo(Integer value) {
            addCriterion("phase_status <=", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIn(List<Integer> values) {
            addCriterion("phase_status in", values, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotIn(List<Integer> values) {
            addCriterion("phase_status not in", values, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusBetween(Integer value1, Integer value2) {
            addCriterion("phase_status between", value1, value2, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("phase_status not between", value1, value2, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andManageIpIsNull() {
            addCriterion("manage_ip is null");
            return (Criteria) this;
        }

        public Criteria andManageIpIsNotNull() {
            addCriterion("manage_ip is not null");
            return (Criteria) this;
        }

        public Criteria andManageIpEqualTo(String value) {
            addCriterion("manage_ip =", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpNotEqualTo(String value) {
            addCriterion("manage_ip <>", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpGreaterThan(String value) {
            addCriterion("manage_ip >", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpGreaterThanOrEqualTo(String value) {
            addCriterion("manage_ip >=", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpLessThan(String value) {
            addCriterion("manage_ip <", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpLessThanOrEqualTo(String value) {
            addCriterion("manage_ip <=", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpLike(String value) {
            addCriterion("manage_ip like", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpNotLike(String value) {
            addCriterion("manage_ip not like", value, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpIn(List<String> values) {
            addCriterion("manage_ip in", values, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpNotIn(List<String> values) {
            addCriterion("manage_ip not in", values, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpBetween(String value1, String value2) {
            addCriterion("manage_ip between", value1, value2, "manageIp");
            return (Criteria) this;
        }

        public Criteria andManageIpNotBetween(String value1, String value2) {
            addCriterion("manage_ip not between", value1, value2, "manageIp");
            return (Criteria) this;
        }

        public Criteria andHostNameIsNull() {
            addCriterion("host_name is null");
            return (Criteria) this;
        }

        public Criteria andHostNameIsNotNull() {
            addCriterion("host_name is not null");
            return (Criteria) this;
        }

        public Criteria andHostNameEqualTo(String value) {
            addCriterion("host_name =", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotEqualTo(String value) {
            addCriterion("host_name <>", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameGreaterThan(String value) {
            addCriterion("host_name >", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameGreaterThanOrEqualTo(String value) {
            addCriterion("host_name >=", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameLessThan(String value) {
            addCriterion("host_name <", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameLessThanOrEqualTo(String value) {
            addCriterion("host_name <=", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameLike(String value) {
            addCriterion("host_name like", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotLike(String value) {
            addCriterion("host_name not like", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameIn(List<String> values) {
            addCriterion("host_name in", values, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotIn(List<String> values) {
            addCriterion("host_name not in", values, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameBetween(String value1, String value2) {
            addCriterion("host_name between", value1, value2, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotBetween(String value1, String value2) {
            addCriterion("host_name not between", value1, value2, "hostName");
            return (Criteria) this;
        }

        public Criteria andSysUsernameIsNull() {
            addCriterion("sys_username is null");
            return (Criteria) this;
        }

        public Criteria andSysUsernameIsNotNull() {
            addCriterion("sys_username is not null");
            return (Criteria) this;
        }

        public Criteria andSysUsernameEqualTo(String value) {
            addCriterion("sys_username =", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotEqualTo(String value) {
            addCriterion("sys_username <>", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameGreaterThan(String value) {
            addCriterion("sys_username >", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("sys_username >=", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameLessThan(String value) {
            addCriterion("sys_username <", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameLessThanOrEqualTo(String value) {
            addCriterion("sys_username <=", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameLike(String value) {
            addCriterion("sys_username like", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotLike(String value) {
            addCriterion("sys_username not like", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameIn(List<String> values) {
            addCriterion("sys_username in", values, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotIn(List<String> values) {
            addCriterion("sys_username not in", values, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameBetween(String value1, String value2) {
            addCriterion("sys_username between", value1, value2, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotBetween(String value1, String value2) {
            addCriterion("sys_username not between", value1, value2, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysPasswordIsNull() {
            addCriterion("sys_password is null");
            return (Criteria) this;
        }

        public Criteria andSysPasswordIsNotNull() {
            addCriterion("sys_password is not null");
            return (Criteria) this;
        }

        public Criteria andSysPasswordEqualTo(String value) {
            addCriterion("sys_password =", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotEqualTo(String value) {
            addCriterion("sys_password <>", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordGreaterThan(String value) {
            addCriterion("sys_password >", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("sys_password >=", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordLessThan(String value) {
            addCriterion("sys_password <", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordLessThanOrEqualTo(String value) {
            addCriterion("sys_password <=", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordLike(String value) {
            addCriterion("sys_password like", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotLike(String value) {
            addCriterion("sys_password not like", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordIn(List<String> values) {
            addCriterion("sys_password in", values, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotIn(List<String> values) {
            addCriterion("sys_password not in", values, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordBetween(String value1, String value2) {
            addCriterion("sys_password between", value1, value2, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotBetween(String value1, String value2) {
            addCriterion("sys_password not between", value1, value2, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdIsNull() {
            addCriterion("pubkey_id is null");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdIsNotNull() {
            addCriterion("pubkey_id is not null");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdEqualTo(String value) {
            addCriterion("pubkey_id =", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotEqualTo(String value) {
            addCriterion("pubkey_id <>", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdGreaterThan(String value) {
            addCriterion("pubkey_id >", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdGreaterThanOrEqualTo(String value) {
            addCriterion("pubkey_id >=", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdLessThan(String value) {
            addCriterion("pubkey_id <", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdLessThanOrEqualTo(String value) {
            addCriterion("pubkey_id <=", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdLike(String value) {
            addCriterion("pubkey_id like", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotLike(String value) {
            addCriterion("pubkey_id not like", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdIn(List<String> values) {
            addCriterion("pubkey_id in", values, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotIn(List<String> values) {
            addCriterion("pubkey_id not in", values, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdBetween(String value1, String value2) {
            addCriterion("pubkey_id between", value1, value2, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotBetween(String value1, String value2) {
            addCriterion("pubkey_id not between", value1, value2, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNull() {
            addCriterion("description is null");
            return (Criteria) this;
        }

        public Criteria andDescriptionIsNotNull() {
            addCriterion("description is not null");
            return (Criteria) this;
        }

        public Criteria andDescriptionEqualTo(String value) {
            addCriterion("description =", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotEqualTo(String value) {
            addCriterion("description <>", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThan(String value) {
            addCriterion("description >", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("description >=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThan(String value) {
            addCriterion("description <", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLessThanOrEqualTo(String value) {
            addCriterion("description <=", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionLike(String value) {
            addCriterion("description like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotLike(String value) {
            addCriterion("description not like", value, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionIn(List<String> values) {
            addCriterion("description in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotIn(List<String> values) {
            addCriterion("description not in", values, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionBetween(String value1, String value2) {
            addCriterion("description between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andDescriptionNotBetween(String value1, String value2) {
            addCriterion("description not between", value1, value2, "description");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdIsNull() {
            addCriterion("backup_node_id is null");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdIsNotNull() {
            addCriterion("backup_node_id is not null");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdEqualTo(String value) {
            addCriterion("backup_node_id =", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdNotEqualTo(String value) {
            addCriterion("backup_node_id <>", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdGreaterThan(String value) {
            addCriterion("backup_node_id >", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("backup_node_id >=", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdLessThan(String value) {
            addCriterion("backup_node_id <", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdLessThanOrEqualTo(String value) {
            addCriterion("backup_node_id <=", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdLike(String value) {
            addCriterion("backup_node_id like", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdNotLike(String value) {
            addCriterion("backup_node_id not like", value, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdIn(List<String> values) {
            addCriterion("backup_node_id in", values, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdNotIn(List<String> values) {
            addCriterion("backup_node_id not in", values, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdBetween(String value1, String value2) {
            addCriterion("backup_node_id between", value1, value2, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andBackupNodeIdNotBetween(String value1, String value2) {
            addCriterion("backup_node_id not between", value1, value2, "backupNodeId");
            return (Criteria) this;
        }

        public Criteria andErrorCountIsNull() {
            addCriterion("error_count is null");
            return (Criteria) this;
        }

        public Criteria andErrorCountIsNotNull() {
            addCriterion("error_count is not null");
            return (Criteria) this;
        }

        public Criteria andErrorCountEqualTo(Integer value) {
            addCriterion("error_count =", value, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountNotEqualTo(Integer value) {
            addCriterion("error_count <>", value, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountGreaterThan(Integer value) {
            addCriterion("error_count >", value, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("error_count >=", value, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountLessThan(Integer value) {
            addCriterion("error_count <", value, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountLessThanOrEqualTo(Integer value) {
            addCriterion("error_count <=", value, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountIn(List<Integer> values) {
            addCriterion("error_count in", values, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountNotIn(List<Integer> values) {
            addCriterion("error_count not in", values, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountBetween(Integer value1, Integer value2) {
            addCriterion("error_count between", value1, value2, "errorCount");
            return (Criteria) this;
        }

        public Criteria andErrorCountNotBetween(Integer value1, Integer value2) {
            addCriterion("error_count not between", value1, value2, "errorCount");
            return (Criteria) this;
        }

        public Criteria andCpuModelIsNull() {
            addCriterion("cpu_model is null");
            return (Criteria) this;
        }

        public Criteria andCpuModelIsNotNull() {
            addCriterion("cpu_model is not null");
            return (Criteria) this;
        }

        public Criteria andCpuModelEqualTo(String value) {
            addCriterion("cpu_model =", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotEqualTo(String value) {
            addCriterion("cpu_model <>", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelGreaterThan(String value) {
            addCriterion("cpu_model >", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_model >=", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelLessThan(String value) {
            addCriterion("cpu_model <", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelLessThanOrEqualTo(String value) {
            addCriterion("cpu_model <=", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelLike(String value) {
            addCriterion("cpu_model like", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotLike(String value) {
            addCriterion("cpu_model not like", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelIn(List<String> values) {
            addCriterion("cpu_model in", values, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotIn(List<String> values) {
            addCriterion("cpu_model not in", values, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelBetween(String value1, String value2) {
            addCriterion("cpu_model between", value1, value2, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotBetween(String value1, String value2) {
            addCriterion("cpu_model not between", value1, value2, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountIsNull() {
            addCriterion("cpu_log_count is null");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountIsNotNull() {
            addCriterion("cpu_log_count is not null");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountEqualTo(Integer value) {
            addCriterion("cpu_log_count =", value, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountNotEqualTo(Integer value) {
            addCriterion("cpu_log_count <>", value, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountGreaterThan(Integer value) {
            addCriterion("cpu_log_count >", value, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_log_count >=", value, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountLessThan(Integer value) {
            addCriterion("cpu_log_count <", value, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_log_count <=", value, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountIn(List<Integer> values) {
            addCriterion("cpu_log_count in", values, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountNotIn(List<Integer> values) {
            addCriterion("cpu_log_count not in", values, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountBetween(Integer value1, Integer value2) {
            addCriterion("cpu_log_count between", value1, value2, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andCpuLogCountNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_log_count not between", value1, value2, "cpuLogCount");
            return (Criteria) this;
        }

        public Criteria andMemTotalIsNull() {
            addCriterion("mem_total is null");
            return (Criteria) this;
        }

        public Criteria andMemTotalIsNotNull() {
            addCriterion("mem_total is not null");
            return (Criteria) this;
        }

        public Criteria andMemTotalEqualTo(Integer value) {
            addCriterion("mem_total =", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalNotEqualTo(Integer value) {
            addCriterion("mem_total <>", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalGreaterThan(Integer value) {
            addCriterion("mem_total >", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("mem_total >=", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalLessThan(Integer value) {
            addCriterion("mem_total <", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalLessThanOrEqualTo(Integer value) {
            addCriterion("mem_total <=", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalIn(List<Integer> values) {
            addCriterion("mem_total in", values, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalNotIn(List<Integer> values) {
            addCriterion("mem_total not in", values, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalBetween(Integer value1, Integer value2) {
            addCriterion("mem_total between", value1, value2, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("mem_total not between", value1, value2, "memTotal");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountIsNull() {
            addCriterion("cpu_phy_count is null");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountIsNotNull() {
            addCriterion("cpu_phy_count is not null");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountEqualTo(Integer value) {
            addCriterion("cpu_phy_count =", value, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountNotEqualTo(Integer value) {
            addCriterion("cpu_phy_count <>", value, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountGreaterThan(Integer value) {
            addCriterion("cpu_phy_count >", value, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_phy_count >=", value, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountLessThan(Integer value) {
            addCriterion("cpu_phy_count <", value, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_phy_count <=", value, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountIn(List<Integer> values) {
            addCriterion("cpu_phy_count in", values, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountNotIn(List<Integer> values) {
            addCriterion("cpu_phy_count not in", values, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountBetween(Integer value1, Integer value2) {
            addCriterion("cpu_phy_count between", value1, value2, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andCpuPhyCountNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_phy_count not between", value1, value2, "cpuPhyCount");
            return (Criteria) this;
        }

        public Criteria andEeBpIsNull() {
            addCriterion("ee_bp is null");
            return (Criteria) this;
        }

        public Criteria andEeBpIsNotNull() {
            addCriterion("ee_bp is not null");
            return (Criteria) this;
        }

        public Criteria andEeBpEqualTo(String value) {
            addCriterion("ee_bp =", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotEqualTo(String value) {
            addCriterion("ee_bp <>", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpGreaterThan(String value) {
            addCriterion("ee_bp >", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpGreaterThanOrEqualTo(String value) {
            addCriterion("ee_bp >=", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpLessThan(String value) {
            addCriterion("ee_bp <", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpLessThanOrEqualTo(String value) {
            addCriterion("ee_bp <=", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpLike(String value) {
            addCriterion("ee_bp like", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotLike(String value) {
            addCriterion("ee_bp not like", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpIn(List<String> values) {
            addCriterion("ee_bp in", values, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotIn(List<String> values) {
            addCriterion("ee_bp not in", values, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpBetween(String value1, String value2) {
            addCriterion("ee_bp between", value1, value2, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotBetween(String value1, String value2) {
            addCriterion("ee_bp not between", value1, value2, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeUserIsNull() {
            addCriterion("ee_user is null");
            return (Criteria) this;
        }

        public Criteria andEeUserIsNotNull() {
            addCriterion("ee_user is not null");
            return (Criteria) this;
        }

        public Criteria andEeUserEqualTo(String value) {
            addCriterion("ee_user =", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotEqualTo(String value) {
            addCriterion("ee_user <>", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserGreaterThan(String value) {
            addCriterion("ee_user >", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserGreaterThanOrEqualTo(String value) {
            addCriterion("ee_user >=", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserLessThan(String value) {
            addCriterion("ee_user <", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserLessThanOrEqualTo(String value) {
            addCriterion("ee_user <=", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserLike(String value) {
            addCriterion("ee_user like", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotLike(String value) {
            addCriterion("ee_user not like", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserIn(List<String> values) {
            addCriterion("ee_user in", values, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotIn(List<String> values) {
            addCriterion("ee_user not in", values, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserBetween(String value1, String value2) {
            addCriterion("ee_user between", value1, value2, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotBetween(String value1, String value2) {
            addCriterion("ee_user not between", value1, value2, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeStatusIsNull() {
            addCriterion("ee_status is null");
            return (Criteria) this;
        }

        public Criteria andEeStatusIsNotNull() {
            addCriterion("ee_status is not null");
            return (Criteria) this;
        }

        public Criteria andEeStatusEqualTo(Integer value) {
            addCriterion("ee_status =", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_hypervisor_node.ee_status <>", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusGreaterThan(Integer value) {
            addCriterion("ee_status >", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("ee_status >=", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusLessThan(Integer value) {
            addCriterion("ee_status <", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("ee_status <=", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusIn(List<Integer> values) {
            addCriterion("ee_status in", values, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusNotIn(List<Integer> values) {
            addCriterion("ee_status not in", values, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusBetween(Integer value1, Integer value2) {
            addCriterion("ee_status between", value1, value2, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("ee_status not between", value1, value2, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountIsNull() {
            addCriterion("available_ib_count is null");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountIsNotNull() {
            addCriterion("available_ib_count is not null");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountEqualTo(Integer value) {
            addCriterion("available_ib_count =", value, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountNotEqualTo(Integer value) {
            addCriterion("available_ib_count <>", value, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountGreaterThan(Integer value) {
            addCriterion("available_ib_count >", value, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("available_ib_count >=", value, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountLessThan(Integer value) {
            addCriterion("available_ib_count <", value, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountLessThanOrEqualTo(Integer value) {
            addCriterion("available_ib_count <=", value, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountIn(List<Integer> values) {
            addCriterion("available_ib_count in", values, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountNotIn(List<Integer> values) {
            addCriterion("available_ib_count not in", values, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountBetween(Integer value1, Integer value2) {
            addCriterion("available_ib_count between", value1, value2, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andAvailableIbCountNotBetween(Integer value1, Integer value2) {
            addCriterion("available_ib_count not between", value1, value2, "availableIbCount");
            return (Criteria) this;
        }

        public Criteria andPciDeviceGroupIdIsNull() {
            addCriterion("tbl_cmp_pci_device_group.device_group_id is null");
            return (Criteria) this;
        }

        public Criteria andPciDeviceGroupIdIn(List<String> values) {
            addCriterion("tbl_cmp_pci_device_group.device_group_id in", values, "deviceGroupId");
            return (Criteria) this;
        }

        public Criteria andPciDevicePhaseStatusEqualTo(Integer value) {
            addCriterion("tbl_cmp_pci_device.phase_status =", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andVmInstancePhaseStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_vm_instance.phase_status <>", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andVmInstancePhaseStatusIsNullOrNotEqualTo(Integer value) {
            addCriterion("( tbl_cmp_vm_instance.phase_status is null or tbl_cmp_vm_instance.phase_status <> " + value + " ) ");
            return (Criteria) this;
        }

        public Criteria andFlavorCloudIdEqualTo(String value) {
            addCriterion("tbl_cmp_flavor.cloud_id =", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceCloudIdEqualTo(String value) {
            addCriterion("tbl_cmp_vm_instance.cloud_id =", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andPciDeviceCloudIdEqualTo(String value) {
            addCriterion("tbl_cmp_pci_device.cloud_id =", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andPciDeviceGroupCloudIdEqualTo(String value) {
            addCriterion("tbl_cmp_pci_device_group.cloud_id =", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andFlavorEeStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_flavor.ee_status <>", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andVmInstanceEeStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_vm_instance.ee_status <>", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andPciDeviceEeStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_pci_device.ee_status <>", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andPciDeviceGroupEeStatusNotEqualTo(Integer value) {
            addCriterion("tbl_cmp_pci_device_group.ee_status <>", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andIbCountIsNull() {
            addCriterion("ib_count is null");
            return (Criteria) this;
        }

        public Criteria andIbCountIsNotNull() {
            addCriterion("ib_count is not null");
            return (Criteria) this;
        }

        public Criteria andIbCountEqualTo(Integer value) {
            addCriterion("ib_count =", value, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountNotEqualTo(Integer value) {
            addCriterion("ib_count <>", value, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountGreaterThan(Integer value) {
            addCriterion("ib_count >", value, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("ib_count >=", value, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountLessThan(Integer value) {
            addCriterion("ib_count <", value, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountLessThanOrEqualTo(Integer value) {
            addCriterion("ib_count <=", value, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountIn(List<Integer> values) {
            addCriterion("ib_count in", values, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountNotIn(List<Integer> values) {
            addCriterion("ib_count not in", values, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountBetween(Integer value1, Integer value2) {
            addCriterion("ib_count between", value1, value2, "ibCount");
            return (Criteria) this;
        }

        public Criteria andIbCountNotBetween(Integer value1, Integer value2) {
            addCriterion("ib_count not between", value1, value2, "ibCount");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}