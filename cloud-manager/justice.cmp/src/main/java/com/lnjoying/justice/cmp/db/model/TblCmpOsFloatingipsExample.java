package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsFloatingipsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsFloatingipsExample() {
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

        public Criteria andFloatingIpAddressIsNull() {
            addCriterion("floating_ip_address is null");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressIsNotNull() {
            addCriterion("floating_ip_address is not null");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressEqualTo(String value) {
            addCriterion("floating_ip_address =", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressNotEqualTo(String value) {
            addCriterion("floating_ip_address <>", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressGreaterThan(String value) {
            addCriterion("floating_ip_address >", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressGreaterThanOrEqualTo(String value) {
            addCriterion("floating_ip_address >=", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressLessThan(String value) {
            addCriterion("floating_ip_address <", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressLessThanOrEqualTo(String value) {
            addCriterion("floating_ip_address <=", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressLike(String value) {
            addCriterion("floating_ip_address like", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressNotLike(String value) {
            addCriterion("floating_ip_address not like", value, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressIn(List<String> values) {
            addCriterion("floating_ip_address in", values, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressNotIn(List<String> values) {
            addCriterion("floating_ip_address not in", values, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressBetween(String value1, String value2) {
            addCriterion("floating_ip_address between", value1, value2, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingIpAddressNotBetween(String value1, String value2) {
            addCriterion("floating_ip_address not between", value1, value2, "floatingIpAddress");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdIsNull() {
            addCriterion("floating_network_id is null");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdIsNotNull() {
            addCriterion("floating_network_id is not null");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdEqualTo(String value) {
            addCriterion("floating_network_id =", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdNotEqualTo(String value) {
            addCriterion("floating_network_id <>", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdGreaterThan(String value) {
            addCriterion("floating_network_id >", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdGreaterThanOrEqualTo(String value) {
            addCriterion("floating_network_id >=", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdLessThan(String value) {
            addCriterion("floating_network_id <", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdLessThanOrEqualTo(String value) {
            addCriterion("floating_network_id <=", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdLike(String value) {
            addCriterion("floating_network_id like", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdNotLike(String value) {
            addCriterion("floating_network_id not like", value, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdIn(List<String> values) {
            addCriterion("floating_network_id in", values, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdNotIn(List<String> values) {
            addCriterion("floating_network_id not in", values, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdBetween(String value1, String value2) {
            addCriterion("floating_network_id between", value1, value2, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingNetworkIdNotBetween(String value1, String value2) {
            addCriterion("floating_network_id not between", value1, value2, "floatingNetworkId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdIsNull() {
            addCriterion("floating_port_id is null");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdIsNotNull() {
            addCriterion("floating_port_id is not null");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdEqualTo(String value) {
            addCriterion("floating_port_id =", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdNotEqualTo(String value) {
            addCriterion("floating_port_id <>", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdGreaterThan(String value) {
            addCriterion("floating_port_id >", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdGreaterThanOrEqualTo(String value) {
            addCriterion("floating_port_id >=", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdLessThan(String value) {
            addCriterion("floating_port_id <", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdLessThanOrEqualTo(String value) {
            addCriterion("floating_port_id <=", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdLike(String value) {
            addCriterion("floating_port_id like", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdNotLike(String value) {
            addCriterion("floating_port_id not like", value, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdIn(List<String> values) {
            addCriterion("floating_port_id in", values, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdNotIn(List<String> values) {
            addCriterion("floating_port_id not in", values, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdBetween(String value1, String value2) {
            addCriterion("floating_port_id between", value1, value2, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFloatingPortIdNotBetween(String value1, String value2) {
            addCriterion("floating_port_id not between", value1, value2, "floatingPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdIsNull() {
            addCriterion("fixed_port_id is null");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdIsNotNull() {
            addCriterion("fixed_port_id is not null");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdEqualTo(String value) {
            addCriterion("fixed_port_id =", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdNotEqualTo(String value) {
            addCriterion("fixed_port_id <>", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdGreaterThan(String value) {
            addCriterion("fixed_port_id >", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdGreaterThanOrEqualTo(String value) {
            addCriterion("fixed_port_id >=", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdLessThan(String value) {
            addCriterion("fixed_port_id <", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdLessThanOrEqualTo(String value) {
            addCriterion("fixed_port_id <=", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdLike(String value) {
            addCriterion("fixed_port_id like", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdNotLike(String value) {
            addCriterion("fixed_port_id not like", value, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdIn(List<String> values) {
            addCriterion("fixed_port_id in", values, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdNotIn(List<String> values) {
            addCriterion("fixed_port_id not in", values, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdBetween(String value1, String value2) {
            addCriterion("fixed_port_id between", value1, value2, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedPortIdNotBetween(String value1, String value2) {
            addCriterion("fixed_port_id not between", value1, value2, "fixedPortId");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressIsNull() {
            addCriterion("fixed_ip_address is null");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressIsNotNull() {
            addCriterion("fixed_ip_address is not null");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressEqualTo(String value) {
            addCriterion("fixed_ip_address =", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressNotEqualTo(String value) {
            addCriterion("fixed_ip_address <>", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressGreaterThan(String value) {
            addCriterion("fixed_ip_address >", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressGreaterThanOrEqualTo(String value) {
            addCriterion("fixed_ip_address >=", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressLessThan(String value) {
            addCriterion("fixed_ip_address <", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressLessThanOrEqualTo(String value) {
            addCriterion("fixed_ip_address <=", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressLike(String value) {
            addCriterion("fixed_ip_address like", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressNotLike(String value) {
            addCriterion("fixed_ip_address not like", value, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressIn(List<String> values) {
            addCriterion("fixed_ip_address in", values, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressNotIn(List<String> values) {
            addCriterion("fixed_ip_address not in", values, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressBetween(String value1, String value2) {
            addCriterion("fixed_ip_address between", value1, value2, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andFixedIpAddressNotBetween(String value1, String value2) {
            addCriterion("fixed_ip_address not between", value1, value2, "fixedIpAddress");
            return (Criteria) this;
        }

        public Criteria andRouterIdIsNull() {
            addCriterion("router_id is null");
            return (Criteria) this;
        }

        public Criteria andRouterIdIsNotNull() {
            addCriterion("router_id is not null");
            return (Criteria) this;
        }

        public Criteria andRouterIdEqualTo(String value) {
            addCriterion("router_id =", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdNotEqualTo(String value) {
            addCriterion("router_id <>", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdGreaterThan(String value) {
            addCriterion("router_id >", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdGreaterThanOrEqualTo(String value) {
            addCriterion("router_id >=", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdLessThan(String value) {
            addCriterion("router_id <", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdLessThanOrEqualTo(String value) {
            addCriterion("router_id <=", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdLike(String value) {
            addCriterion("router_id like", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdNotLike(String value) {
            addCriterion("router_id not like", value, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdIn(List<String> values) {
            addCriterion("router_id in", values, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdNotIn(List<String> values) {
            addCriterion("router_id not in", values, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdBetween(String value1, String value2) {
            addCriterion("router_id between", value1, value2, "routerId");
            return (Criteria) this;
        }

        public Criteria andRouterIdNotBetween(String value1, String value2) {
            addCriterion("router_id not between", value1, value2, "routerId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdIsNull() {
            addCriterion("last_known_router_id is null");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdIsNotNull() {
            addCriterion("last_known_router_id is not null");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdEqualTo(String value) {
            addCriterion("last_known_router_id =", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdNotEqualTo(String value) {
            addCriterion("last_known_router_id <>", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdGreaterThan(String value) {
            addCriterion("last_known_router_id >", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdGreaterThanOrEqualTo(String value) {
            addCriterion("last_known_router_id >=", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdLessThan(String value) {
            addCriterion("last_known_router_id <", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdLessThanOrEqualTo(String value) {
            addCriterion("last_known_router_id <=", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdLike(String value) {
            addCriterion("last_known_router_id like", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdNotLike(String value) {
            addCriterion("last_known_router_id not like", value, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdIn(List<String> values) {
            addCriterion("last_known_router_id in", values, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdNotIn(List<String> values) {
            addCriterion("last_known_router_id not in", values, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdBetween(String value1, String value2) {
            addCriterion("last_known_router_id between", value1, value2, "lastKnownRouterId");
            return (Criteria) this;
        }

        public Criteria andLastKnownRouterIdNotBetween(String value1, String value2) {
            addCriterion("last_known_router_id not between", value1, value2, "lastKnownRouterId");
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

        public Criteria andPublishedDnsNameIsNull() {
            addCriterion("published_dns_name is null");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameIsNotNull() {
            addCriterion("published_dns_name is not null");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameEqualTo(String value) {
            addCriterion("published_dns_name =", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameNotEqualTo(String value) {
            addCriterion("published_dns_name <>", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameGreaterThan(String value) {
            addCriterion("published_dns_name >", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameGreaterThanOrEqualTo(String value) {
            addCriterion("published_dns_name >=", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameLessThan(String value) {
            addCriterion("published_dns_name <", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameLessThanOrEqualTo(String value) {
            addCriterion("published_dns_name <=", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameLike(String value) {
            addCriterion("published_dns_name like", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameNotLike(String value) {
            addCriterion("published_dns_name not like", value, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameIn(List<String> values) {
            addCriterion("published_dns_name in", values, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameNotIn(List<String> values) {
            addCriterion("published_dns_name not in", values, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameBetween(String value1, String value2) {
            addCriterion("published_dns_name between", value1, value2, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsNameNotBetween(String value1, String value2) {
            addCriterion("published_dns_name not between", value1, value2, "publishedDnsName");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainIsNull() {
            addCriterion("published_dns_domain is null");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainIsNotNull() {
            addCriterion("published_dns_domain is not null");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainEqualTo(String value) {
            addCriterion("published_dns_domain =", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainNotEqualTo(String value) {
            addCriterion("published_dns_domain <>", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainGreaterThan(String value) {
            addCriterion("published_dns_domain >", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainGreaterThanOrEqualTo(String value) {
            addCriterion("published_dns_domain >=", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainLessThan(String value) {
            addCriterion("published_dns_domain <", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainLessThanOrEqualTo(String value) {
            addCriterion("published_dns_domain <=", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainLike(String value) {
            addCriterion("published_dns_domain like", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainNotLike(String value) {
            addCriterion("published_dns_domain not like", value, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainIn(List<String> values) {
            addCriterion("published_dns_domain in", values, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainNotIn(List<String> values) {
            addCriterion("published_dns_domain not in", values, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainBetween(String value1, String value2) {
            addCriterion("published_dns_domain between", value1, value2, "publishedDnsDomain");
            return (Criteria) this;
        }

        public Criteria andPublishedDnsDomainNotBetween(String value1, String value2) {
            addCriterion("published_dns_domain not between", value1, value2, "publishedDnsDomain");
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