package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpBaremetalInstanceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected Integer startRow;

    protected Integer pageSize;

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) { this.startRow = startRow;}

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public TblCmpBaremetalInstanceExample() {
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

        public Criteria andImageIdIsNull() {
            addCriterion("image_id is null");
            return (Criteria) this;
        }

        public Criteria andImageIdIsNotNull() {
            addCriterion("image_id is not null");
            return (Criteria) this;
        }

        public Criteria andImageIdEqualTo(String value) {
            addCriterion("image_id =", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotEqualTo(String value) {
            addCriterion("image_id <>", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdGreaterThan(String value) {
            addCriterion("image_id >", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdGreaterThanOrEqualTo(String value) {
            addCriterion("image_id >=", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdLessThan(String value) {
            addCriterion("image_id <", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdLessThanOrEqualTo(String value) {
            addCriterion("image_id <=", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdLike(String value) {
            addCriterion("image_id like", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotLike(String value) {
            addCriterion("image_id not like", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdIn(List<String> values) {
            addCriterion("image_id in", values, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotIn(List<String> values) {
            addCriterion("image_id not in", values, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdBetween(String value1, String value2) {
            addCriterion("image_id between", value1, value2, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotBetween(String value1, String value2) {
            addCriterion("image_id not between", value1, value2, "imageId");
            return (Criteria) this;
        }

        public Criteria andVpcIdIsNull() {
            addCriterion("vpc_id is null");
            return (Criteria) this;
        }

        public Criteria andVpcIdIsNotNull() {
            addCriterion("vpc_id is not null");
            return (Criteria) this;
        }

        public Criteria andVpcIdEqualTo(String value) {
            addCriterion("vpc_id =", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotEqualTo(String value) {
            addCriterion("vpc_id <>", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdGreaterThan(String value) {
            addCriterion("vpc_id >", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdGreaterThanOrEqualTo(String value) {
            addCriterion("vpc_id >=", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdLessThan(String value) {
            addCriterion("vpc_id <", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdLessThanOrEqualTo(String value) {
            addCriterion("vpc_id <=", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdLike(String value) {
            addCriterion("vpc_id like", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotLike(String value) {
            addCriterion("vpc_id not like", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdIn(List<String> values) {
            addCriterion("vpc_id in", values, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotIn(List<String> values) {
            addCriterion("vpc_id not in", values, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdBetween(String value1, String value2) {
            addCriterion("vpc_id between", value1, value2, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotBetween(String value1, String value2) {
            addCriterion("vpc_id not between", value1, value2, "vpcId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdIsNull() {
            addCriterion("subnet_id is null");
            return (Criteria) this;
        }

        public Criteria andSubnetIdIsNotNull() {
            addCriterion("subnet_id is not null");
            return (Criteria) this;
        }

        public Criteria andSubnetIdEqualTo(String value) {
            addCriterion("subnet_id =", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotEqualTo(String value) {
            addCriterion("subnet_id <>", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdGreaterThan(String value) {
            addCriterion("subnet_id >", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdGreaterThanOrEqualTo(String value) {
            addCriterion("subnet_id >=", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdLessThan(String value) {
            addCriterion("subnet_id <", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdLessThanOrEqualTo(String value) {
            addCriterion("subnet_id <=", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdLike(String value) {
            addCriterion("subnet_id like", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotLike(String value) {
            addCriterion("subnet_id not like", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdIn(List<String> values) {
            addCriterion("subnet_id in", values, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotIn(List<String> values) {
            addCriterion("subnet_id not in", values, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdBetween(String value1, String value2) {
            addCriterion("subnet_id between", value1, value2, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotBetween(String value1, String value2) {
            addCriterion("subnet_id not between", value1, value2, "subnetId");
            return (Criteria) this;
        }

        public Criteria andStaticIpIsNull() {
            addCriterion("static_ip is null");
            return (Criteria) this;
        }

        public Criteria andStaticIpIsNotNull() {
            addCriterion("static_ip is not null");
            return (Criteria) this;
        }

        public Criteria andStaticIpEqualTo(String value) {
            addCriterion("static_ip =", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotEqualTo(String value) {
            addCriterion("static_ip <>", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpGreaterThan(String value) {
            addCriterion("static_ip >", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpGreaterThanOrEqualTo(String value) {
            addCriterion("static_ip >=", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpLessThan(String value) {
            addCriterion("static_ip <", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpLessThanOrEqualTo(String value) {
            addCriterion("static_ip <=", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpLike(String value) {
            addCriterion("static_ip like", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotLike(String value) {
            addCriterion("static_ip not like", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpIn(List<String> values) {
            addCriterion("static_ip in", values, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotIn(List<String> values) {
            addCriterion("static_ip not in", values, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpBetween(String value1, String value2) {
            addCriterion("static_ip between", value1, value2, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotBetween(String value1, String value2) {
            addCriterion("static_ip not between", value1, value2, "staticIp");
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

        public Criteria andIscsiTargetIsNull() {
            addCriterion("iscsi_target is null");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetIsNotNull() {
            addCriterion("iscsi_target is not null");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetEqualTo(String value) {
            addCriterion("iscsi_target =", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotEqualTo(String value) {
            addCriterion("iscsi_target <>", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetGreaterThan(String value) {
            addCriterion("iscsi_target >", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetGreaterThanOrEqualTo(String value) {
            addCriterion("iscsi_target >=", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetLessThan(String value) {
            addCriterion("iscsi_target <", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetLessThanOrEqualTo(String value) {
            addCriterion("iscsi_target <=", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetLike(String value) {
            addCriterion("iscsi_target like", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotLike(String value) {
            addCriterion("iscsi_target not like", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetIn(List<String> values) {
            addCriterion("iscsi_target in", values, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotIn(List<String> values) {
            addCriterion("iscsi_target not in", values, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetBetween(String value1, String value2) {
            addCriterion("iscsi_target between", value1, value2, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotBetween(String value1, String value2) {
            addCriterion("iscsi_target not between", value1, value2, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorIsNull() {
            addCriterion("iscsi_initiator is null");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorIsNotNull() {
            addCriterion("iscsi_initiator is not null");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorEqualTo(String value) {
            addCriterion("iscsi_initiator =", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotEqualTo(String value) {
            addCriterion("iscsi_initiator <>", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorGreaterThan(String value) {
            addCriterion("iscsi_initiator >", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorGreaterThanOrEqualTo(String value) {
            addCriterion("iscsi_initiator >=", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorLessThan(String value) {
            addCriterion("iscsi_initiator <", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorLessThanOrEqualTo(String value) {
            addCriterion("iscsi_initiator <=", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorLike(String value) {
            addCriterion("iscsi_initiator like", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotLike(String value) {
            addCriterion("iscsi_initiator not like", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorIn(List<String> values) {
            addCriterion("iscsi_initiator in", values, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotIn(List<String> values) {
            addCriterion("iscsi_initiator not in", values, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorBetween(String value1, String value2) {
            addCriterion("iscsi_initiator between", value1, value2, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotBetween(String value1, String value2) {
            addCriterion("iscsi_initiator not between", value1, value2, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportIsNull() {
            addCriterion("iscsi_ipport is null");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportIsNotNull() {
            addCriterion("iscsi_ipport is not null");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportEqualTo(String value) {
            addCriterion("iscsi_ipport =", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotEqualTo(String value) {
            addCriterion("iscsi_ipport <>", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportGreaterThan(String value) {
            addCriterion("iscsi_ipport >", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportGreaterThanOrEqualTo(String value) {
            addCriterion("iscsi_ipport >=", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportLessThan(String value) {
            addCriterion("iscsi_ipport <", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportLessThanOrEqualTo(String value) {
            addCriterion("iscsi_ipport <=", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportLike(String value) {
            addCriterion("iscsi_ipport like", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotLike(String value) {
            addCriterion("iscsi_ipport not like", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportIn(List<String> values) {
            addCriterion("iscsi_ipport in", values, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotIn(List<String> values) {
            addCriterion("iscsi_ipport not in", values, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportBetween(String value1, String value2) {
            addCriterion("iscsi_ipport between", value1, value2, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotBetween(String value1, String value2) {
            addCriterion("iscsi_ipport not between", value1, value2, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentIsNull() {
            addCriterion("share_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentIsNotNull() {
            addCriterion("share_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentEqualTo(String value) {
            addCriterion("share_id_from_agent =", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotEqualTo(String value) {
            addCriterion("share_id_from_agent <>", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentGreaterThan(String value) {
            addCriterion("share_id_from_agent >", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("share_id_from_agent >=", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentLessThan(String value) {
            addCriterion("share_id_from_agent <", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("share_id_from_agent <=", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentLike(String value) {
            addCriterion("share_id_from_agent like", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotLike(String value) {
            addCriterion("share_id_from_agent not like", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentIn(List<String> values) {
            addCriterion("share_id_from_agent in", values, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotIn(List<String> values) {
            addCriterion("share_id_from_agent not in", values, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentBetween(String value1, String value2) {
            addCriterion("share_id_from_agent between", value1, value2, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("share_id_from_agent not between", value1, value2, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentIsNull() {
            addCriterion("nic_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentIsNotNull() {
            addCriterion("nic_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentEqualTo(String value) {
            addCriterion("nic_id_from_agent =", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentNotEqualTo(String value) {
            addCriterion("nic_id_from_agent <>", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentGreaterThan(String value) {
            addCriterion("nic_id_from_agent >", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("nic_id_from_agent >=", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentLessThan(String value) {
            addCriterion("nic_id_from_agent <", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("nic_id_from_agent <=", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentLike(String value) {
            addCriterion("nic_id_from_agent like", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentNotLike(String value) {
            addCriterion("nic_id_from_agent not like", value, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentIn(List<String> values) {
            addCriterion("nic_id_from_agent in", values, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentNotIn(List<String> values) {
            addCriterion("nic_id_from_agent not in", values, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentBetween(String value1, String value2) {
            addCriterion("nic_id_from_agent between", value1, value2, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andNicIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("nic_id_from_agent not between", value1, value2, "nicIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentIsNull() {
            addCriterion("port_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentIsNotNull() {
            addCriterion("port_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentEqualTo(String value) {
            addCriterion("port_id_from_agent =", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentNotEqualTo(String value) {
            addCriterion("port_id_from_agent <>", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentGreaterThan(String value) {
            addCriterion("port_id_from_agent >", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("port_id_from_agent >=", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentLessThan(String value) {
            addCriterion("port_id_from_agent <", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("port_id_from_agent <=", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentLike(String value) {
            addCriterion("port_id_from_agent like", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentNotLike(String value) {
            addCriterion("port_id_from_agent not like", value, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentIn(List<String> values) {
            addCriterion("port_id_from_agent in", values, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentNotIn(List<String> values) {
            addCriterion("port_id_from_agent not in", values, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentBetween(String value1, String value2) {
            addCriterion("port_id_from_agent between", value1, value2, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andPortIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("port_id_from_agent not between", value1, value2, "portIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentIsNull() {
            addCriterion("instance_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentIsNotNull() {
            addCriterion("instance_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentEqualTo(String value) {
            addCriterion("instance_id_from_agent =", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotEqualTo(String value) {
            addCriterion("instance_id_from_agent <>", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentGreaterThan(String value) {
            addCriterion("instance_id_from_agent >", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("instance_id_from_agent >=", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentLessThan(String value) {
            addCriterion("instance_id_from_agent <", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("instance_id_from_agent <=", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentLike(String value) {
            addCriterion("instance_id_from_agent like", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotLike(String value) {
            addCriterion("instance_id_from_agent not like", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentIn(List<String> values) {
            addCriterion("instance_id_from_agent in", values, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotIn(List<String> values) {
            addCriterion("instance_id_from_agent not in", values, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentBetween(String value1, String value2) {
            addCriterion("instance_id_from_agent between", value1, value2, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("instance_id_from_agent not between", value1, value2, "instanceIdFromAgent");
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