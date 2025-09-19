package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsPortsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsPortsExample() {
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNull() {
            addCriterion("project_id is null");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNotNull() {
            addCriterion("project_id is not null");
            return (Criteria) this;
        }

        public Criteria andProjectIdEqualTo(String value) {
            addCriterion("project_id =", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotEqualTo(String value) {
            addCriterion("project_id <>", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThan(String value) {
            addCriterion("project_id >", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThanOrEqualTo(String value) {
            addCriterion("project_id >=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThan(String value) {
            addCriterion("project_id <", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThanOrEqualTo(String value) {
            addCriterion("project_id <=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLike(String value) {
            addCriterion("project_id like", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotLike(String value) {
            addCriterion("project_id not like", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIn(List<String> values) {
            addCriterion("project_id in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotIn(List<String> values) {
            addCriterion("project_id not in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdBetween(String value1, String value2) {
            addCriterion("project_id between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotBetween(String value1, String value2) {
            addCriterion("project_id not between", value1, value2, "projectId");
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

        public Criteria andNetworkIdIsNull() {
            addCriterion("network_id is null");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIsNotNull() {
            addCriterion("network_id is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkIdEqualTo(String value) {
            addCriterion("network_id =", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotEqualTo(String value) {
            addCriterion("network_id <>", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdGreaterThan(String value) {
            addCriterion("network_id >", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdGreaterThanOrEqualTo(String value) {
            addCriterion("network_id >=", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLessThan(String value) {
            addCriterion("network_id <", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLessThanOrEqualTo(String value) {
            addCriterion("network_id <=", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLike(String value) {
            addCriterion("network_id like", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotLike(String value) {
            addCriterion("network_id not like", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIn(List<String> values) {
            addCriterion("network_id in", values, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotIn(List<String> values) {
            addCriterion("network_id not in", values, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdBetween(String value1, String value2) {
            addCriterion("network_id between", value1, value2, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotBetween(String value1, String value2) {
            addCriterion("network_id not between", value1, value2, "networkId");
            return (Criteria) this;
        }

        public Criteria andMacAddressIsNull() {
            addCriterion("mac_address is null");
            return (Criteria) this;
        }

        public Criteria andMacAddressIsNotNull() {
            addCriterion("mac_address is not null");
            return (Criteria) this;
        }

        public Criteria andMacAddressEqualTo(String value) {
            addCriterion("mac_address =", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressNotEqualTo(String value) {
            addCriterion("mac_address <>", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressGreaterThan(String value) {
            addCriterion("mac_address >", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressGreaterThanOrEqualTo(String value) {
            addCriterion("mac_address >=", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressLessThan(String value) {
            addCriterion("mac_address <", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressLessThanOrEqualTo(String value) {
            addCriterion("mac_address <=", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressLike(String value) {
            addCriterion("mac_address like", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressNotLike(String value) {
            addCriterion("mac_address not like", value, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressIn(List<String> values) {
            addCriterion("mac_address in", values, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressNotIn(List<String> values) {
            addCriterion("mac_address not in", values, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressBetween(String value1, String value2) {
            addCriterion("mac_address between", value1, value2, "macAddress");
            return (Criteria) this;
        }

        public Criteria andMacAddressNotBetween(String value1, String value2) {
            addCriterion("mac_address not between", value1, value2, "macAddress");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpIsNull() {
            addCriterion("admin_state_up is null");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpIsNotNull() {
            addCriterion("admin_state_up is not null");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpEqualTo(Short value) {
            addCriterion("admin_state_up =", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpNotEqualTo(Short value) {
            addCriterion("admin_state_up <>", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpGreaterThan(Short value) {
            addCriterion("admin_state_up >", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpGreaterThanOrEqualTo(Short value) {
            addCriterion("admin_state_up >=", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpLessThan(Short value) {
            addCriterion("admin_state_up <", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpLessThanOrEqualTo(Short value) {
            addCriterion("admin_state_up <=", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpIn(List<Short> values) {
            addCriterion("admin_state_up in", values, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpNotIn(List<Short> values) {
            addCriterion("admin_state_up not in", values, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpBetween(Short value1, Short value2) {
            addCriterion("admin_state_up between", value1, value2, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpNotBetween(Short value1, Short value2) {
            addCriterion("admin_state_up not between", value1, value2, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
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

        public Criteria andDeviceOwnerIsNull() {
            addCriterion("device_owner is null");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerIsNotNull() {
            addCriterion("device_owner is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerEqualTo(String value) {
            addCriterion("device_owner =", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerNotEqualTo(String value) {
            addCriterion("device_owner <>", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerGreaterThan(String value) {
            addCriterion("device_owner >", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerGreaterThanOrEqualTo(String value) {
            addCriterion("device_owner >=", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerLessThan(String value) {
            addCriterion("device_owner <", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerLessThanOrEqualTo(String value) {
            addCriterion("device_owner <=", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerLike(String value) {
            addCriterion("device_owner like", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerNotLike(String value) {
            addCriterion("device_owner not like", value, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerIn(List<String> values) {
            addCriterion("device_owner in", values, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerNotIn(List<String> values) {
            addCriterion("device_owner not in", values, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerBetween(String value1, String value2) {
            addCriterion("device_owner between", value1, value2, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andDeviceOwnerNotBetween(String value1, String value2) {
            addCriterion("device_owner not between", value1, value2, "deviceOwner");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdIsNull() {
            addCriterion("standard_attr_id is null");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdIsNotNull() {
            addCriterion("standard_attr_id is not null");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdEqualTo(Long value) {
            addCriterion("standard_attr_id =", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdNotEqualTo(Long value) {
            addCriterion("standard_attr_id <>", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdGreaterThan(Long value) {
            addCriterion("standard_attr_id >", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdGreaterThanOrEqualTo(Long value) {
            addCriterion("standard_attr_id >=", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdLessThan(Long value) {
            addCriterion("standard_attr_id <", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdLessThanOrEqualTo(Long value) {
            addCriterion("standard_attr_id <=", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdIn(List<Long> values) {
            addCriterion("standard_attr_id in", values, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdNotIn(List<Long> values) {
            addCriterion("standard_attr_id not in", values, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdBetween(Long value1, Long value2) {
            addCriterion("standard_attr_id between", value1, value2, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdNotBetween(Long value1, Long value2) {
            addCriterion("standard_attr_id not between", value1, value2, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andIpAllocationIsNull() {
            addCriterion("ip_allocation is null");
            return (Criteria) this;
        }

        public Criteria andIpAllocationIsNotNull() {
            addCriterion("ip_allocation is not null");
            return (Criteria) this;
        }

        public Criteria andIpAllocationEqualTo(String value) {
            addCriterion("ip_allocation =", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationNotEqualTo(String value) {
            addCriterion("ip_allocation <>", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationGreaterThan(String value) {
            addCriterion("ip_allocation >", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationGreaterThanOrEqualTo(String value) {
            addCriterion("ip_allocation >=", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationLessThan(String value) {
            addCriterion("ip_allocation <", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationLessThanOrEqualTo(String value) {
            addCriterion("ip_allocation <=", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationLike(String value) {
            addCriterion("ip_allocation like", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationNotLike(String value) {
            addCriterion("ip_allocation not like", value, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationIn(List<String> values) {
            addCriterion("ip_allocation in", values, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationNotIn(List<String> values) {
            addCriterion("ip_allocation not in", values, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationBetween(String value1, String value2) {
            addCriterion("ip_allocation between", value1, value2, "ipAllocation");
            return (Criteria) this;
        }

        public Criteria andIpAllocationNotBetween(String value1, String value2) {
            addCriterion("ip_allocation not between", value1, value2, "ipAllocation");
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

        public Criteria andDataPlaneStatusIsNull() {
            addCriterion("data_plane_status is null");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusIsNotNull() {
            addCriterion("data_plane_status is not null");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusEqualTo(String value) {
            addCriterion("data_plane_status =", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusNotEqualTo(String value) {
            addCriterion("data_plane_status <>", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusGreaterThan(String value) {
            addCriterion("data_plane_status >", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusGreaterThanOrEqualTo(String value) {
            addCriterion("data_plane_status >=", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusLessThan(String value) {
            addCriterion("data_plane_status <", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusLessThanOrEqualTo(String value) {
            addCriterion("data_plane_status <=", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusLike(String value) {
            addCriterion("data_plane_status like", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusNotLike(String value) {
            addCriterion("data_plane_status not like", value, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusIn(List<String> values) {
            addCriterion("data_plane_status in", values, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusNotIn(List<String> values) {
            addCriterion("data_plane_status not in", values, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusBetween(String value1, String value2) {
            addCriterion("data_plane_status between", value1, value2, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andDataPlaneStatusNotBetween(String value1, String value2) {
            addCriterion("data_plane_status not between", value1, value2, "dataPlaneStatus");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameIsNull() {
            addCriterion("current_dns_name is null");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameIsNotNull() {
            addCriterion("current_dns_name is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameEqualTo(String value) {
            addCriterion("current_dns_name =", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameNotEqualTo(String value) {
            addCriterion("current_dns_name <>", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameGreaterThan(String value) {
            addCriterion("current_dns_name >", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameGreaterThanOrEqualTo(String value) {
            addCriterion("current_dns_name >=", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameLessThan(String value) {
            addCriterion("current_dns_name <", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameLessThanOrEqualTo(String value) {
            addCriterion("current_dns_name <=", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameLike(String value) {
            addCriterion("current_dns_name like", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameNotLike(String value) {
            addCriterion("current_dns_name not like", value, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameIn(List<String> values) {
            addCriterion("current_dns_name in", values, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameNotIn(List<String> values) {
            addCriterion("current_dns_name not in", values, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameBetween(String value1, String value2) {
            addCriterion("current_dns_name between", value1, value2, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsNameNotBetween(String value1, String value2) {
            addCriterion("current_dns_name not between", value1, value2, "currentDnsName");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainIsNull() {
            addCriterion("current_dns_domain is null");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainIsNotNull() {
            addCriterion("current_dns_domain is not null");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainEqualTo(String value) {
            addCriterion("current_dns_domain =", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainNotEqualTo(String value) {
            addCriterion("current_dns_domain <>", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainGreaterThan(String value) {
            addCriterion("current_dns_domain >", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainGreaterThanOrEqualTo(String value) {
            addCriterion("current_dns_domain >=", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainLessThan(String value) {
            addCriterion("current_dns_domain <", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainLessThanOrEqualTo(String value) {
            addCriterion("current_dns_domain <=", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainLike(String value) {
            addCriterion("current_dns_domain like", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainNotLike(String value) {
            addCriterion("current_dns_domain not like", value, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainIn(List<String> values) {
            addCriterion("current_dns_domain in", values, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainNotIn(List<String> values) {
            addCriterion("current_dns_domain not in", values, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainBetween(String value1, String value2) {
            addCriterion("current_dns_domain between", value1, value2, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andCurrentDnsDomainNotBetween(String value1, String value2) {
            addCriterion("current_dns_domain not between", value1, value2, "currentDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameIsNull() {
            addCriterion("previous_dns_name is null");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameIsNotNull() {
            addCriterion("previous_dns_name is not null");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameEqualTo(String value) {
            addCriterion("previous_dns_name =", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameNotEqualTo(String value) {
            addCriterion("previous_dns_name <>", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameGreaterThan(String value) {
            addCriterion("previous_dns_name >", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameGreaterThanOrEqualTo(String value) {
            addCriterion("previous_dns_name >=", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameLessThan(String value) {
            addCriterion("previous_dns_name <", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameLessThanOrEqualTo(String value) {
            addCriterion("previous_dns_name <=", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameLike(String value) {
            addCriterion("previous_dns_name like", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameNotLike(String value) {
            addCriterion("previous_dns_name not like", value, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameIn(List<String> values) {
            addCriterion("previous_dns_name in", values, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameNotIn(List<String> values) {
            addCriterion("previous_dns_name not in", values, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameBetween(String value1, String value2) {
            addCriterion("previous_dns_name between", value1, value2, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsNameNotBetween(String value1, String value2) {
            addCriterion("previous_dns_name not between", value1, value2, "previousDnsName");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainIsNull() {
            addCriterion("previous_dns_domain is null");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainIsNotNull() {
            addCriterion("previous_dns_domain is not null");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainEqualTo(String value) {
            addCriterion("previous_dns_domain =", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainNotEqualTo(String value) {
            addCriterion("previous_dns_domain <>", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainGreaterThan(String value) {
            addCriterion("previous_dns_domain >", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainGreaterThanOrEqualTo(String value) {
            addCriterion("previous_dns_domain >=", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainLessThan(String value) {
            addCriterion("previous_dns_domain <", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainLessThanOrEqualTo(String value) {
            addCriterion("previous_dns_domain <=", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainLike(String value) {
            addCriterion("previous_dns_domain like", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainNotLike(String value) {
            addCriterion("previous_dns_domain not like", value, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainIn(List<String> values) {
            addCriterion("previous_dns_domain in", values, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainNotIn(List<String> values) {
            addCriterion("previous_dns_domain not in", values, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainBetween(String value1, String value2) {
            addCriterion("previous_dns_domain between", value1, value2, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPreviousDnsDomainNotBetween(String value1, String value2) {
            addCriterion("previous_dns_domain not between", value1, value2, "previousDnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsNameIsNull() {
            addCriterion("dns_name is null");
            return (Criteria) this;
        }

        public Criteria andDnsNameIsNotNull() {
            addCriterion("dns_name is not null");
            return (Criteria) this;
        }

        public Criteria andDnsNameEqualTo(String value) {
            addCriterion("dns_name =", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameNotEqualTo(String value) {
            addCriterion("dns_name <>", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameGreaterThan(String value) {
            addCriterion("dns_name >", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameGreaterThanOrEqualTo(String value) {
            addCriterion("dns_name >=", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameLessThan(String value) {
            addCriterion("dns_name <", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameLessThanOrEqualTo(String value) {
            addCriterion("dns_name <=", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameLike(String value) {
            addCriterion("dns_name like", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameNotLike(String value) {
            addCriterion("dns_name not like", value, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameIn(List<String> values) {
            addCriterion("dns_name in", values, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameNotIn(List<String> values) {
            addCriterion("dns_name not in", values, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameBetween(String value1, String value2) {
            addCriterion("dns_name between", value1, value2, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsNameNotBetween(String value1, String value2) {
            addCriterion("dns_name not between", value1, value2, "dnsName");
            return (Criteria) this;
        }

        public Criteria andDnsDomainIsNull() {
            addCriterion("dns_domain is null");
            return (Criteria) this;
        }

        public Criteria andDnsDomainIsNotNull() {
            addCriterion("dns_domain is not null");
            return (Criteria) this;
        }

        public Criteria andDnsDomainEqualTo(String value) {
            addCriterion("dns_domain =", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainNotEqualTo(String value) {
            addCriterion("dns_domain <>", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainGreaterThan(String value) {
            addCriterion("dns_domain >", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainGreaterThanOrEqualTo(String value) {
            addCriterion("dns_domain >=", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainLessThan(String value) {
            addCriterion("dns_domain <", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainLessThanOrEqualTo(String value) {
            addCriterion("dns_domain <=", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainLike(String value) {
            addCriterion("dns_domain like", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainNotLike(String value) {
            addCriterion("dns_domain not like", value, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainIn(List<String> values) {
            addCriterion("dns_domain in", values, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainNotIn(List<String> values) {
            addCriterion("dns_domain not in", values, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainBetween(String value1, String value2) {
            addCriterion("dns_domain between", value1, value2, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andDnsDomainNotBetween(String value1, String value2) {
            addCriterion("dns_domain not between", value1, value2, "dnsDomain");
            return (Criteria) this;
        }

        public Criteria andPolicyIdIsNull() {
            addCriterion("policy_id is null");
            return (Criteria) this;
        }

        public Criteria andPolicyIdIsNotNull() {
            addCriterion("policy_id is not null");
            return (Criteria) this;
        }

        public Criteria andPolicyIdEqualTo(String value) {
            addCriterion("policy_id =", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdNotEqualTo(String value) {
            addCriterion("policy_id <>", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdGreaterThan(String value) {
            addCriterion("policy_id >", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdGreaterThanOrEqualTo(String value) {
            addCriterion("policy_id >=", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdLessThan(String value) {
            addCriterion("policy_id <", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdLessThanOrEqualTo(String value) {
            addCriterion("policy_id <=", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdLike(String value) {
            addCriterion("policy_id like", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdNotLike(String value) {
            addCriterion("policy_id not like", value, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdIn(List<String> values) {
            addCriterion("policy_id in", values, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdNotIn(List<String> values) {
            addCriterion("policy_id not in", values, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdBetween(String value1, String value2) {
            addCriterion("policy_id between", value1, value2, "policyId");
            return (Criteria) this;
        }

        public Criteria andPolicyIdNotBetween(String value1, String value2) {
            addCriterion("policy_id not between", value1, value2, "policyId");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusIsNull() {
            addCriterion("propagate_uplink_status is null");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusIsNotNull() {
            addCriterion("propagate_uplink_status is not null");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusEqualTo(Short value) {
            addCriterion("propagate_uplink_status =", value, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusNotEqualTo(Short value) {
            addCriterion("propagate_uplink_status <>", value, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusGreaterThan(Short value) {
            addCriterion("propagate_uplink_status >", value, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("propagate_uplink_status >=", value, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusLessThan(Short value) {
            addCriterion("propagate_uplink_status <", value, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusLessThanOrEqualTo(Short value) {
            addCriterion("propagate_uplink_status <=", value, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusIn(List<Short> values) {
            addCriterion("propagate_uplink_status in", values, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusNotIn(List<Short> values) {
            addCriterion("propagate_uplink_status not in", values, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusBetween(Short value1, Short value2) {
            addCriterion("propagate_uplink_status between", value1, value2, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andPropagateUplinkStatusNotBetween(Short value1, Short value2) {
            addCriterion("propagate_uplink_status not between", value1, value2, "propagateUplinkStatus");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledIsNull() {
            addCriterion("mac_learning_enabled is null");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledIsNotNull() {
            addCriterion("mac_learning_enabled is not null");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledEqualTo(Short value) {
            addCriterion("mac_learning_enabled =", value, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledNotEqualTo(Short value) {
            addCriterion("mac_learning_enabled <>", value, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledGreaterThan(Short value) {
            addCriterion("mac_learning_enabled >", value, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledGreaterThanOrEqualTo(Short value) {
            addCriterion("mac_learning_enabled >=", value, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledLessThan(Short value) {
            addCriterion("mac_learning_enabled <", value, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledLessThanOrEqualTo(Short value) {
            addCriterion("mac_learning_enabled <=", value, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledIn(List<Short> values) {
            addCriterion("mac_learning_enabled in", values, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledNotIn(List<Short> values) {
            addCriterion("mac_learning_enabled not in", values, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledBetween(Short value1, Short value2) {
            addCriterion("mac_learning_enabled between", value1, value2, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andMacLearningEnabledNotBetween(Short value1, Short value2) {
            addCriterion("mac_learning_enabled not between", value1, value2, "macLearningEnabled");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
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

        public Criteria andRevisionNumberIsNull() {
            addCriterion("revision_number is null");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberIsNotNull() {
            addCriterion("revision_number is not null");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberEqualTo(Long value) {
            addCriterion("revision_number =", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberNotEqualTo(Long value) {
            addCriterion("revision_number <>", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberGreaterThan(Long value) {
            addCriterion("revision_number >", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("revision_number >=", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberLessThan(Long value) {
            addCriterion("revision_number <", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberLessThanOrEqualTo(Long value) {
            addCriterion("revision_number <=", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberIn(List<Long> values) {
            addCriterion("revision_number in", values, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberNotIn(List<Long> values) {
            addCriterion("revision_number not in", values, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberBetween(Long value1, Long value2) {
            addCriterion("revision_number between", value1, value2, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberNotBetween(Long value1, Long value2) {
            addCriterion("revision_number not between", value1, value2, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledIsNull() {
            addCriterion("port_security_enabled is null");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledIsNotNull() {
            addCriterion("port_security_enabled is not null");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledEqualTo(Short value) {
            addCriterion("port_security_enabled =", value, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledNotEqualTo(Short value) {
            addCriterion("port_security_enabled <>", value, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledGreaterThan(Short value) {
            addCriterion("port_security_enabled >", value, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledGreaterThanOrEqualTo(Short value) {
            addCriterion("port_security_enabled >=", value, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledLessThan(Short value) {
            addCriterion("port_security_enabled <", value, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledLessThanOrEqualTo(Short value) {
            addCriterion("port_security_enabled <=", value, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledIn(List<Short> values) {
            addCriterion("port_security_enabled in", values, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledNotIn(List<Short> values) {
            addCriterion("port_security_enabled not in", values, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledBetween(Short value1, Short value2) {
            addCriterion("port_security_enabled between", value1, value2, "portSecurityEnabled");
            return (Criteria) this;
        }

        public Criteria andPortSecurityEnabledNotBetween(Short value1, Short value2) {
            addCriterion("port_security_enabled not between", value1, value2, "portSecurityEnabled");
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