package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.List;

public class TblCmpEsFirewallRulesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpEsFirewallRulesExample() {
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

        public Criteria andProtocolIsNull() {
            addCriterion("protocol is null");
            return (Criteria) this;
        }

        public Criteria andProtocolIsNotNull() {
            addCriterion("protocol is not null");
            return (Criteria) this;
        }

        public Criteria andProtocolEqualTo(String value) {
            addCriterion("protocol =", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotEqualTo(String value) {
            addCriterion("protocol <>", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolGreaterThan(String value) {
            addCriterion("protocol >", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolGreaterThanOrEqualTo(String value) {
            addCriterion("protocol >=", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLessThan(String value) {
            addCriterion("protocol <", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLessThanOrEqualTo(String value) {
            addCriterion("protocol <=", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLike(String value) {
            addCriterion("protocol like", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotLike(String value) {
            addCriterion("protocol not like", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolIn(List<String> values) {
            addCriterion("protocol in", values, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotIn(List<String> values) {
            addCriterion("protocol not in", values, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolBetween(String value1, String value2) {
            addCriterion("protocol between", value1, value2, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotBetween(String value1, String value2) {
            addCriterion("protocol not between", value1, value2, "protocol");
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

        public Criteria andSourcePortIsNull() {
            addCriterion("source_port is null");
            return (Criteria) this;
        }

        public Criteria andSourcePortIsNotNull() {
            addCriterion("source_port is not null");
            return (Criteria) this;
        }

        public Criteria andSourcePortEqualTo(String value) {
            addCriterion("source_port =", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortNotEqualTo(String value) {
            addCriterion("source_port <>", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortGreaterThan(String value) {
            addCriterion("source_port >", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortGreaterThanOrEqualTo(String value) {
            addCriterion("source_port >=", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortLessThan(String value) {
            addCriterion("source_port <", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortLessThanOrEqualTo(String value) {
            addCriterion("source_port <=", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortLike(String value) {
            addCriterion("source_port like", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortNotLike(String value) {
            addCriterion("source_port not like", value, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortIn(List<String> values) {
            addCriterion("source_port in", values, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortNotIn(List<String> values) {
            addCriterion("source_port not in", values, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortBetween(String value1, String value2) {
            addCriterion("source_port between", value1, value2, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourcePortNotBetween(String value1, String value2) {
            addCriterion("source_port not between", value1, value2, "sourcePort");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressIsNull() {
            addCriterion("source_ip_address is null");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressIsNotNull() {
            addCriterion("source_ip_address is not null");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressEqualTo(String value) {
            addCriterion("source_ip_address =", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressNotEqualTo(String value) {
            addCriterion("source_ip_address <>", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressGreaterThan(String value) {
            addCriterion("source_ip_address >", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressGreaterThanOrEqualTo(String value) {
            addCriterion("source_ip_address >=", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressLessThan(String value) {
            addCriterion("source_ip_address <", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressLessThanOrEqualTo(String value) {
            addCriterion("source_ip_address <=", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressLike(String value) {
            addCriterion("source_ip_address like", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressNotLike(String value) {
            addCriterion("source_ip_address not like", value, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressIn(List<String> values) {
            addCriterion("source_ip_address in", values, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressNotIn(List<String> values) {
            addCriterion("source_ip_address not in", values, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressBetween(String value1, String value2) {
            addCriterion("source_ip_address between", value1, value2, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andSourceIpAddressNotBetween(String value1, String value2) {
            addCriterion("source_ip_address not between", value1, value2, "sourceIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressIsNull() {
            addCriterion("destination_ip_address is null");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressIsNotNull() {
            addCriterion("destination_ip_address is not null");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressEqualTo(String value) {
            addCriterion("destination_ip_address =", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressNotEqualTo(String value) {
            addCriterion("destination_ip_address <>", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressGreaterThan(String value) {
            addCriterion("destination_ip_address >", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressGreaterThanOrEqualTo(String value) {
            addCriterion("destination_ip_address >=", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressLessThan(String value) {
            addCriterion("destination_ip_address <", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressLessThanOrEqualTo(String value) {
            addCriterion("destination_ip_address <=", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressLike(String value) {
            addCriterion("destination_ip_address like", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressNotLike(String value) {
            addCriterion("destination_ip_address not like", value, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressIn(List<String> values) {
            addCriterion("destination_ip_address in", values, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressNotIn(List<String> values) {
            addCriterion("destination_ip_address not in", values, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressBetween(String value1, String value2) {
            addCriterion("destination_ip_address between", value1, value2, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andDestinationIpAddressNotBetween(String value1, String value2) {
            addCriterion("destination_ip_address not between", value1, value2, "destinationIpAddress");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdIsNull() {
            addCriterion("firewall_policy_id is null");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdIsNotNull() {
            addCriterion("firewall_policy_id is not null");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdEqualTo(String value) {
            addCriterion("firewall_policy_id =", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdNotEqualTo(String value) {
            addCriterion("firewall_policy_id <>", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdGreaterThan(String value) {
            addCriterion("firewall_policy_id >", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdGreaterThanOrEqualTo(String value) {
            addCriterion("firewall_policy_id >=", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdLessThan(String value) {
            addCriterion("firewall_policy_id <", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdLessThanOrEqualTo(String value) {
            addCriterion("firewall_policy_id <=", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdLike(String value) {
            addCriterion("firewall_policy_id like", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdNotLike(String value) {
            addCriterion("firewall_policy_id not like", value, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdIn(List<String> values) {
            addCriterion("firewall_policy_id in", values, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdNotIn(List<String> values) {
            addCriterion("firewall_policy_id not in", values, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdBetween(String value1, String value2) {
            addCriterion("firewall_policy_id between", value1, value2, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andFirewallPolicyIdNotBetween(String value1, String value2) {
            addCriterion("firewall_policy_id not between", value1, value2, "firewallPolicyId");
            return (Criteria) this;
        }

        public Criteria andPositionIsNull() {
            addCriterion("position is null");
            return (Criteria) this;
        }

        public Criteria andPositionIsNotNull() {
            addCriterion("position is not null");
            return (Criteria) this;
        }

        public Criteria andPositionEqualTo(Integer value) {
            addCriterion("position =", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotEqualTo(Integer value) {
            addCriterion("position <>", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionGreaterThan(Integer value) {
            addCriterion("position >", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionGreaterThanOrEqualTo(Integer value) {
            addCriterion("position >=", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLessThan(Integer value) {
            addCriterion("position <", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionLessThanOrEqualTo(Integer value) {
            addCriterion("position <=", value, "position");
            return (Criteria) this;
        }

        public Criteria andPositionIn(List<Integer> values) {
            addCriterion("position in", values, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotIn(List<Integer> values) {
            addCriterion("position not in", values, "position");
            return (Criteria) this;
        }

        public Criteria andPositionBetween(Integer value1, Integer value2) {
            addCriterion("position between", value1, value2, "position");
            return (Criteria) this;
        }

        public Criteria andPositionNotBetween(Integer value1, Integer value2) {
            addCriterion("position not between", value1, value2, "position");
            return (Criteria) this;
        }

        public Criteria andDestinationPortIsNull() {
            addCriterion("destination_port is null");
            return (Criteria) this;
        }

        public Criteria andDestinationPortIsNotNull() {
            addCriterion("destination_port is not null");
            return (Criteria) this;
        }

        public Criteria andDestinationPortEqualTo(String value) {
            addCriterion("destination_port =", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortNotEqualTo(String value) {
            addCriterion("destination_port <>", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortGreaterThan(String value) {
            addCriterion("destination_port >", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortGreaterThanOrEqualTo(String value) {
            addCriterion("destination_port >=", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortLessThan(String value) {
            addCriterion("destination_port <", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortLessThanOrEqualTo(String value) {
            addCriterion("destination_port <=", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortLike(String value) {
            addCriterion("destination_port like", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortNotLike(String value) {
            addCriterion("destination_port not like", value, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortIn(List<String> values) {
            addCriterion("destination_port in", values, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortNotIn(List<String> values) {
            addCriterion("destination_port not in", values, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortBetween(String value1, String value2) {
            addCriterion("destination_port between", value1, value2, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andDestinationPortNotBetween(String value1, String value2) {
            addCriterion("destination_port not between", value1, value2, "destinationPort");
            return (Criteria) this;
        }

        public Criteria andTenantIdIsNull() {
            addCriterion("tenant_id is null");
            return (Criteria) this;
        }

        public Criteria andTenantIdIsNotNull() {
            addCriterion("tenant_id is not null");
            return (Criteria) this;
        }

        public Criteria andTenantIdEqualTo(String value) {
            addCriterion("tenant_id =", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotEqualTo(String value) {
            addCriterion("tenant_id <>", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdGreaterThan(String value) {
            addCriterion("tenant_id >", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdGreaterThanOrEqualTo(String value) {
            addCriterion("tenant_id >=", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdLessThan(String value) {
            addCriterion("tenant_id <", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdLessThanOrEqualTo(String value) {
            addCriterion("tenant_id <=", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdLike(String value) {
            addCriterion("tenant_id like", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotLike(String value) {
            addCriterion("tenant_id not like", value, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdIn(List<String> values) {
            addCriterion("tenant_id in", values, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotIn(List<String> values) {
            addCriterion("tenant_id not in", values, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdBetween(String value1, String value2) {
            addCriterion("tenant_id between", value1, value2, "tenantId");
            return (Criteria) this;
        }

        public Criteria andTenantIdNotBetween(String value1, String value2) {
            addCriterion("tenant_id not between", value1, value2, "tenantId");
            return (Criteria) this;
        }

        public Criteria andEnabledIsNull() {
            addCriterion("enabled is null");
            return (Criteria) this;
        }

        public Criteria andEnabledIsNotNull() {
            addCriterion("enabled is not null");
            return (Criteria) this;
        }

        public Criteria andEnabledEqualTo(Short value) {
            addCriterion("enabled =", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotEqualTo(Short value) {
            addCriterion("enabled <>", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledGreaterThan(Short value) {
            addCriterion("enabled >", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledGreaterThanOrEqualTo(Short value) {
            addCriterion("enabled >=", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledLessThan(Short value) {
            addCriterion("enabled <", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledLessThanOrEqualTo(Short value) {
            addCriterion("enabled <=", value, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledIn(List<Short> values) {
            addCriterion("enabled in", values, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotIn(List<Short> values) {
            addCriterion("enabled not in", values, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledBetween(Short value1, Short value2) {
            addCriterion("enabled between", value1, value2, "enabled");
            return (Criteria) this;
        }

        public Criteria andEnabledNotBetween(Short value1, Short value2) {
            addCriterion("enabled not between", value1, value2, "enabled");
            return (Criteria) this;
        }

        public Criteria andActionIsNull() {
            addCriterion("action is null");
            return (Criteria) this;
        }

        public Criteria andActionIsNotNull() {
            addCriterion("action is not null");
            return (Criteria) this;
        }

        public Criteria andActionEqualTo(String value) {
            addCriterion("action =", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotEqualTo(String value) {
            addCriterion("action <>", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionGreaterThan(String value) {
            addCriterion("action >", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionGreaterThanOrEqualTo(String value) {
            addCriterion("action >=", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLessThan(String value) {
            addCriterion("action <", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLessThanOrEqualTo(String value) {
            addCriterion("action <=", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLike(String value) {
            addCriterion("action like", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotLike(String value) {
            addCriterion("action not like", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionIn(List<String> values) {
            addCriterion("action in", values, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotIn(List<String> values) {
            addCriterion("action not in", values, "action");
            return (Criteria) this;
        }

        public Criteria andActionBetween(String value1, String value2) {
            addCriterion("action between", value1, value2, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotBetween(String value1, String value2) {
            addCriterion("action not between", value1, value2, "action");
            return (Criteria) this;
        }

        public Criteria andIpVersionIsNull() {
            addCriterion("ip_version is null");
            return (Criteria) this;
        }

        public Criteria andIpVersionIsNotNull() {
            addCriterion("ip_version is not null");
            return (Criteria) this;
        }

        public Criteria andIpVersionEqualTo(Integer value) {
            addCriterion("ip_version =", value, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionNotEqualTo(Integer value) {
            addCriterion("ip_version <>", value, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionGreaterThan(Integer value) {
            addCriterion("ip_version >", value, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionGreaterThanOrEqualTo(Integer value) {
            addCriterion("ip_version >=", value, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionLessThan(Integer value) {
            addCriterion("ip_version <", value, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionLessThanOrEqualTo(Integer value) {
            addCriterion("ip_version <=", value, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionIn(List<Integer> values) {
            addCriterion("ip_version in", values, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionNotIn(List<Integer> values) {
            addCriterion("ip_version not in", values, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionBetween(Integer value1, Integer value2) {
            addCriterion("ip_version between", value1, value2, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andIpVersionNotBetween(Integer value1, Integer value2) {
            addCriterion("ip_version not between", value1, value2, "ipVersion");
            return (Criteria) this;
        }

        public Criteria andSharedIsNull() {
            addCriterion("shared is null");
            return (Criteria) this;
        }

        public Criteria andSharedIsNotNull() {
            addCriterion("shared is not null");
            return (Criteria) this;
        }

        public Criteria andSharedEqualTo(Short value) {
            addCriterion("shared =", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedNotEqualTo(Short value) {
            addCriterion("shared <>", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedGreaterThan(Short value) {
            addCriterion("shared >", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedGreaterThanOrEqualTo(Short value) {
            addCriterion("shared >=", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedLessThan(Short value) {
            addCriterion("shared <", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedLessThanOrEqualTo(Short value) {
            addCriterion("shared <=", value, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedIn(List<Short> values) {
            addCriterion("shared in", values, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedNotIn(List<Short> values) {
            addCriterion("shared not in", values, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedBetween(Short value1, Short value2) {
            addCriterion("shared between", value1, value2, "shared");
            return (Criteria) this;
        }

        public Criteria andSharedNotBetween(Short value1, Short value2) {
            addCriterion("shared not between", value1, value2, "shared");
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