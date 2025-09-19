package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.List;

public class TblCmpNicInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpNicInfoExample() {
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

        public Criteria andNicIdIsNull() {
            addCriterion("nic_id is null");
            return (Criteria) this;
        }

        public Criteria andNicIdIsNotNull() {
            addCriterion("nic_id is not null");
            return (Criteria) this;
        }

        public Criteria andNicIdEqualTo(String value) {
            addCriterion("nic_id =", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdNotEqualTo(String value) {
            addCriterion("nic_id <>", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdGreaterThan(String value) {
            addCriterion("nic_id >", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdGreaterThanOrEqualTo(String value) {
            addCriterion("nic_id >=", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdLessThan(String value) {
            addCriterion("nic_id <", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdLessThanOrEqualTo(String value) {
            addCriterion("nic_id <=", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdLike(String value) {
            addCriterion("nic_id like", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdNotLike(String value) {
            addCriterion("nic_id not like", value, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdIn(List<String> values) {
            addCriterion("nic_id in", values, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdNotIn(List<String> values) {
            addCriterion("nic_id not in", values, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdBetween(String value1, String value2) {
            addCriterion("nic_id between", value1, value2, "nicId");
            return (Criteria) this;
        }

        public Criteria andNicIdNotBetween(String value1, String value2) {
            addCriterion("nic_id not between", value1, value2, "nicId");
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

        public Criteria andNicNameIsNull() {
            addCriterion("nic_name is null");
            return (Criteria) this;
        }

        public Criteria andNicNameIsNotNull() {
            addCriterion("nic_name is not null");
            return (Criteria) this;
        }

        public Criteria andNicNameEqualTo(String value) {
            addCriterion("nic_name =", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameNotEqualTo(String value) {
            addCriterion("nic_name <>", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameGreaterThan(String value) {
            addCriterion("nic_name >", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameGreaterThanOrEqualTo(String value) {
            addCriterion("nic_name >=", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameLessThan(String value) {
            addCriterion("nic_name <", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameLessThanOrEqualTo(String value) {
            addCriterion("nic_name <=", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameLike(String value) {
            addCriterion("nic_name like", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameNotLike(String value) {
            addCriterion("nic_name not like", value, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameIn(List<String> values) {
            addCriterion("nic_name in", values, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameNotIn(List<String> values) {
            addCriterion("nic_name not in", values, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameBetween(String value1, String value2) {
            addCriterion("nic_name between", value1, value2, "nicName");
            return (Criteria) this;
        }

        public Criteria andNicNameNotBetween(String value1, String value2) {
            addCriterion("nic_name not between", value1, value2, "nicName");
            return (Criteria) this;
        }

        public Criteria andLinkStateIsNull() {
            addCriterion("link_state is null");
            return (Criteria) this;
        }

        public Criteria andLinkStateIsNotNull() {
            addCriterion("link_state is not null");
            return (Criteria) this;
        }

        public Criteria andLinkStateEqualTo(String value) {
            addCriterion("link_state =", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateNotEqualTo(String value) {
            addCriterion("link_state <>", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateGreaterThan(String value) {
            addCriterion("link_state >", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateGreaterThanOrEqualTo(String value) {
            addCriterion("link_state >=", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateLessThan(String value) {
            addCriterion("link_state <", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateLessThanOrEqualTo(String value) {
            addCriterion("link_state <=", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateLike(String value) {
            addCriterion("link_state like", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateNotLike(String value) {
            addCriterion("link_state not like", value, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateIn(List<String> values) {
            addCriterion("link_state in", values, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateNotIn(List<String> values) {
            addCriterion("link_state not in", values, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateBetween(String value1, String value2) {
            addCriterion("link_state between", value1, value2, "linkState");
            return (Criteria) this;
        }

        public Criteria andLinkStateNotBetween(String value1, String value2) {
            addCriterion("link_state not between", value1, value2, "linkState");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeIsNull() {
            addCriterion("network_type is null");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeIsNotNull() {
            addCriterion("network_type is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeEqualTo(Short value) {
            addCriterion("network_type =", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotEqualTo(Short value) {
            addCriterion("network_type <>", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeGreaterThan(Short value) {
            addCriterion("network_type >", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("network_type >=", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeLessThan(Short value) {
            addCriterion("network_type <", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeLessThanOrEqualTo(Short value) {
            addCriterion("network_type <=", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeIn(List<Short> values) {
            addCriterion("network_type in", values, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotIn(List<Short> values) {
            addCriterion("network_type not in", values, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeBetween(Short value1, Short value2) {
            addCriterion("network_type between", value1, value2, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotBetween(Short value1, Short value2) {
            addCriterion("network_type not between", value1, value2, "networkType");
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

        public Criteria andIpIsNull() {
            addCriterion("ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ip not between", value1, value2, "ip");
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