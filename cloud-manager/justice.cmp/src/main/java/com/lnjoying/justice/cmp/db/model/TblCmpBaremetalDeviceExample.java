package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpBaremetalDeviceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpBaremetalDeviceExample() {
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
            addCriterion("cloud_id =", value, "cloudId");
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

        public Criteria andDeviceIdIsNull() {
            addCriterion("device_id is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNotNull() {
            addCriterion("device_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdEqualTo(String value) {
            addCriterion("device_id =", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotEqualTo(String value) {
            addCriterion("device_id <>", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThan(String value) {
            addCriterion("device_id >", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThanOrEqualTo(String value) {
            addCriterion("device_id >=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThan(String value) {
            addCriterion("device_id <", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThanOrEqualTo(String value) {
            addCriterion("device_id <=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLike(String value) {
            addCriterion("device_id like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotLike(String value) {
            addCriterion("device_id not like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIn(List<String> values) {
            addCriterion("device_id in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotIn(List<String> values) {
            addCriterion("device_id not in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdBetween(String value1, String value2) {
            addCriterion("device_id between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotBetween(String value1, String value2) {
            addCriterion("device_id not between", value1, value2, "deviceId");
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
            addCriterion("phase_status =", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotEqualTo(Integer value) {
            addCriterion("phase_status <>", value, "phaseStatus");
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

        public Criteria andClusterIdIsNull() {
            addCriterion("cluster_id is null");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNotNull() {
            addCriterion("cluster_id is not null");
            return (Criteria) this;
        }

        public Criteria andClusterIdEqualTo(String value) {
            addCriterion("cluster_id =", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotEqualTo(String value) {
            addCriterion("cluster_id <>", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThan(String value) {
            addCriterion("cluster_id >", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_id >=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThan(String value) {
            addCriterion("cluster_id <", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThanOrEqualTo(String value) {
            addCriterion("cluster_id <=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLike(String value) {
            addCriterion("cluster_id like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotLike(String value) {
            addCriterion("cluster_id not like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdIn(List<String> values) {
            addCriterion("cluster_id in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotIn(List<String> values) {
            addCriterion("cluster_id not in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdBetween(String value1, String value2) {
            addCriterion("cluster_id between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotBetween(String value1, String value2) {
            addCriterion("cluster_id not between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdIsNull() {
            addCriterion("device_spec_id is null");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdIsNotNull() {
            addCriterion("device_spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdEqualTo(String value) {
            addCriterion("device_spec_id =", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotEqualTo(String value) {
            addCriterion("device_spec_id <>", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdGreaterThan(String value) {
            addCriterion("device_spec_id >", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdGreaterThanOrEqualTo(String value) {
            addCriterion("device_spec_id >=", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdLessThan(String value) {
            addCriterion("device_spec_id <", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdLessThanOrEqualTo(String value) {
            addCriterion("device_spec_id <=", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdLike(String value) {
            addCriterion("device_spec_id like", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotLike(String value) {
            addCriterion("device_spec_id not like", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdIn(List<String> values) {
            addCriterion("device_spec_id in", values, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotIn(List<String> values) {
            addCriterion("device_spec_id not in", values, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdBetween(String value1, String value2) {
            addCriterion("device_spec_id between", value1, value2, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotBetween(String value1, String value2) {
            addCriterion("device_spec_id not between", value1, value2, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andAddressTypeIsNull() {
            addCriterion("address_type is null");
            return (Criteria) this;
        }

        public Criteria andAddressTypeIsNotNull() {
            addCriterion("address_type is not null");
            return (Criteria) this;
        }

        public Criteria andAddressTypeEqualTo(Short value) {
            addCriterion("address_type =", value, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeNotEqualTo(Short value) {
            addCriterion("address_type <>", value, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeGreaterThan(Short value) {
            addCriterion("address_type >", value, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("address_type >=", value, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeLessThan(Short value) {
            addCriterion("address_type <", value, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeLessThanOrEqualTo(Short value) {
            addCriterion("address_type <=", value, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeIn(List<Short> values) {
            addCriterion("address_type in", values, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeNotIn(List<Short> values) {
            addCriterion("address_type not in", values, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeBetween(Short value1, Short value2) {
            addCriterion("address_type between", value1, value2, "addressType");
            return (Criteria) this;
        }

        public Criteria andAddressTypeNotBetween(Short value1, Short value2) {
            addCriterion("address_type not between", value1, value2, "addressType");
            return (Criteria) this;
        }

        public Criteria andIpmiIpIsNull() {
            addCriterion("ipmi_ip is null");
            return (Criteria) this;
        }

        public Criteria andIpmiIpIsNotNull() {
            addCriterion("ipmi_ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpmiIpEqualTo(String value) {
            addCriterion("ipmi_ip =", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpNotEqualTo(String value) {
            addCriterion("ipmi_ip <>", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpGreaterThan(String value) {
            addCriterion("ipmi_ip >", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpGreaterThanOrEqualTo(String value) {
            addCriterion("ipmi_ip >=", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpLessThan(String value) {
            addCriterion("ipmi_ip <", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpLessThanOrEqualTo(String value) {
            addCriterion("ipmi_ip <=", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpLike(String value) {
            addCriterion("ipmi_ip like", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpNotLike(String value) {
            addCriterion("ipmi_ip not like", value, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpIn(List<String> values) {
            addCriterion("ipmi_ip in", values, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpNotIn(List<String> values) {
            addCriterion("ipmi_ip not in", values, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpBetween(String value1, String value2) {
            addCriterion("ipmi_ip between", value1, value2, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiIpNotBetween(String value1, String value2) {
            addCriterion("ipmi_ip not between", value1, value2, "ipmiIp");
            return (Criteria) this;
        }

        public Criteria andIpmiPortIsNull() {
            addCriterion("ipmi_port is null");
            return (Criteria) this;
        }

        public Criteria andIpmiPortIsNotNull() {
            addCriterion("ipmi_port is not null");
            return (Criteria) this;
        }

        public Criteria andIpmiPortEqualTo(Short value) {
            addCriterion("ipmi_port =", value, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortNotEqualTo(Short value) {
            addCriterion("ipmi_port <>", value, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortGreaterThan(Short value) {
            addCriterion("ipmi_port >", value, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortGreaterThanOrEqualTo(Short value) {
            addCriterion("ipmi_port >=", value, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortLessThan(Short value) {
            addCriterion("ipmi_port <", value, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortLessThanOrEqualTo(Short value) {
            addCriterion("ipmi_port <=", value, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortIn(List<Short> values) {
            addCriterion("ipmi_port in", values, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortNotIn(List<Short> values) {
            addCriterion("ipmi_port not in", values, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortBetween(Short value1, Short value2) {
            addCriterion("ipmi_port between", value1, value2, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiPortNotBetween(Short value1, Short value2) {
            addCriterion("ipmi_port not between", value1, value2, "ipmiPort");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameIsNull() {
            addCriterion("ipmi_username is null");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameIsNotNull() {
            addCriterion("ipmi_username is not null");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameEqualTo(String value) {
            addCriterion("ipmi_username =", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameNotEqualTo(String value) {
            addCriterion("ipmi_username <>", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameGreaterThan(String value) {
            addCriterion("ipmi_username >", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("ipmi_username >=", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameLessThan(String value) {
            addCriterion("ipmi_username <", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameLessThanOrEqualTo(String value) {
            addCriterion("ipmi_username <=", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameLike(String value) {
            addCriterion("ipmi_username like", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameNotLike(String value) {
            addCriterion("ipmi_username not like", value, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameIn(List<String> values) {
            addCriterion("ipmi_username in", values, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameNotIn(List<String> values) {
            addCriterion("ipmi_username not in", values, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameBetween(String value1, String value2) {
            addCriterion("ipmi_username between", value1, value2, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiUsernameNotBetween(String value1, String value2) {
            addCriterion("ipmi_username not between", value1, value2, "ipmiUsername");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordIsNull() {
            addCriterion("ipmi_password is null");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordIsNotNull() {
            addCriterion("ipmi_password is not null");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordEqualTo(String value) {
            addCriterion("ipmi_password =", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordNotEqualTo(String value) {
            addCriterion("ipmi_password <>", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordGreaterThan(String value) {
            addCriterion("ipmi_password >", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("ipmi_password >=", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordLessThan(String value) {
            addCriterion("ipmi_password <", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordLessThanOrEqualTo(String value) {
            addCriterion("ipmi_password <=", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordLike(String value) {
            addCriterion("ipmi_password like", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordNotLike(String value) {
            addCriterion("ipmi_password not like", value, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordIn(List<String> values) {
            addCriterion("ipmi_password in", values, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordNotIn(List<String> values) {
            addCriterion("ipmi_password not in", values, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordBetween(String value1, String value2) {
            addCriterion("ipmi_password between", value1, value2, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andIpmiPasswordNotBetween(String value1, String value2) {
            addCriterion("ipmi_password not between", value1, value2, "ipmiPassword");
            return (Criteria) this;
        }

        public Criteria andBmctypeIsNull() {
            addCriterion("bmctype is null");
            return (Criteria) this;
        }

        public Criteria andBmctypeIsNotNull() {
            addCriterion("bmctype is not null");
            return (Criteria) this;
        }

        public Criteria andBmctypeEqualTo(String value) {
            addCriterion("bmctype =", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeNotEqualTo(String value) {
            addCriterion("bmctype <>", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeGreaterThan(String value) {
            addCriterion("bmctype >", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeGreaterThanOrEqualTo(String value) {
            addCriterion("bmctype >=", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeLessThan(String value) {
            addCriterion("bmctype <", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeLessThanOrEqualTo(String value) {
            addCriterion("bmctype <=", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeLike(String value) {
            addCriterion("bmctype like", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeNotLike(String value) {
            addCriterion("bmctype not like", value, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeIn(List<String> values) {
            addCriterion("bmctype in", values, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeNotIn(List<String> values) {
            addCriterion("bmctype not in", values, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeBetween(String value1, String value2) {
            addCriterion("bmctype between", value1, value2, "bmctype");
            return (Criteria) this;
        }

        public Criteria andBmctypeNotBetween(String value1, String value2) {
            addCriterion("bmctype not between", value1, value2, "bmctype");
            return (Criteria) this;
        }

        public Criteria andIpmiMacIsNull() {
            addCriterion("ipmi_mac is null");
            return (Criteria) this;
        }

        public Criteria andIpmiMacIsNotNull() {
            addCriterion("ipmi_mac is not null");
            return (Criteria) this;
        }

        public Criteria andIpmiMacEqualTo(String value) {
            addCriterion("ipmi_mac =", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacNotEqualTo(String value) {
            addCriterion("ipmi_mac <>", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacGreaterThan(String value) {
            addCriterion("ipmi_mac >", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacGreaterThanOrEqualTo(String value) {
            addCriterion("ipmi_mac >=", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacLessThan(String value) {
            addCriterion("ipmi_mac <", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacLessThanOrEqualTo(String value) {
            addCriterion("ipmi_mac <=", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacLike(String value) {
            addCriterion("ipmi_mac like", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacNotLike(String value) {
            addCriterion("ipmi_mac not like", value, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacIn(List<String> values) {
            addCriterion("ipmi_mac in", values, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacNotIn(List<String> values) {
            addCriterion("ipmi_mac not in", values, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacBetween(String value1, String value2) {
            addCriterion("ipmi_mac between", value1, value2, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andIpmiMacNotBetween(String value1, String value2) {
            addCriterion("ipmi_mac not between", value1, value2, "ipmiMac");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentIsNull() {
            addCriterion("device_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentIsNotNull() {
            addCriterion("device_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentEqualTo(String value) {
            addCriterion("device_id_from_agent =", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentNotEqualTo(String value) {
            addCriterion("device_id_from_agent <>", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentGreaterThan(String value) {
            addCriterion("device_id_from_agent >", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("device_id_from_agent >=", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentLessThan(String value) {
            addCriterion("device_id_from_agent <", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("device_id_from_agent <=", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentLike(String value) {
            addCriterion("device_id_from_agent like", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentNotLike(String value) {
            addCriterion("device_id_from_agent not like", value, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentIn(List<String> values) {
            addCriterion("device_id_from_agent in", values, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentNotIn(List<String> values) {
            addCriterion("device_id_from_agent not in", values, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentBetween(String value1, String value2) {
            addCriterion("device_id_from_agent between", value1, value2, "deviceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andDeviceIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("device_id_from_agent not between", value1, value2, "deviceIdFromAgent");
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
            addCriterion("ee_status <>", value, "eeStatus");
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