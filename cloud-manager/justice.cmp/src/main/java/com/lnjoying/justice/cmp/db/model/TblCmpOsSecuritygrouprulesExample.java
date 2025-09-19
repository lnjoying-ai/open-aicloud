package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsSecuritygrouprulesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsSecuritygrouprulesExample() {
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

        public Criteria andSecurityGroupIdIsNull() {
            addCriterion("security_group_id is null");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdIsNotNull() {
            addCriterion("security_group_id is not null");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdEqualTo(String value) {
            addCriterion("security_group_id =", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdNotEqualTo(String value) {
            addCriterion("security_group_id <>", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdGreaterThan(String value) {
            addCriterion("security_group_id >", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdGreaterThanOrEqualTo(String value) {
            addCriterion("security_group_id >=", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdLessThan(String value) {
            addCriterion("security_group_id <", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdLessThanOrEqualTo(String value) {
            addCriterion("security_group_id <=", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdLike(String value) {
            addCriterion("security_group_id like", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdNotLike(String value) {
            addCriterion("security_group_id not like", value, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdIn(List<String> values) {
            addCriterion("security_group_id in", values, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdNotIn(List<String> values) {
            addCriterion("security_group_id not in", values, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdBetween(String value1, String value2) {
            addCriterion("security_group_id between", value1, value2, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andSecurityGroupIdNotBetween(String value1, String value2) {
            addCriterion("security_group_id not between", value1, value2, "securityGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdIsNull() {
            addCriterion("remote_group_id is null");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdIsNotNull() {
            addCriterion("remote_group_id is not null");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdEqualTo(String value) {
            addCriterion("remote_group_id =", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdNotEqualTo(String value) {
            addCriterion("remote_group_id <>", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdGreaterThan(String value) {
            addCriterion("remote_group_id >", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdGreaterThanOrEqualTo(String value) {
            addCriterion("remote_group_id >=", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdLessThan(String value) {
            addCriterion("remote_group_id <", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdLessThanOrEqualTo(String value) {
            addCriterion("remote_group_id <=", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdLike(String value) {
            addCriterion("remote_group_id like", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdNotLike(String value) {
            addCriterion("remote_group_id not like", value, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdIn(List<String> values) {
            addCriterion("remote_group_id in", values, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdNotIn(List<String> values) {
            addCriterion("remote_group_id not in", values, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdBetween(String value1, String value2) {
            addCriterion("remote_group_id between", value1, value2, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andRemoteGroupIdNotBetween(String value1, String value2) {
            addCriterion("remote_group_id not between", value1, value2, "remoteGroupId");
            return (Criteria) this;
        }

        public Criteria andDirectionIsNull() {
            addCriterion("direction is null");
            return (Criteria) this;
        }

        public Criteria andDirectionIsNotNull() {
            addCriterion("direction is not null");
            return (Criteria) this;
        }

        public Criteria andDirectionEqualTo(String value) {
            addCriterion("direction =", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotEqualTo(String value) {
            addCriterion("direction <>", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionGreaterThan(String value) {
            addCriterion("direction >", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionGreaterThanOrEqualTo(String value) {
            addCriterion("direction >=", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionLessThan(String value) {
            addCriterion("direction <", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionLessThanOrEqualTo(String value) {
            addCriterion("direction <=", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionLike(String value) {
            addCriterion("direction like", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotLike(String value) {
            addCriterion("direction not like", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionIn(List<String> values) {
            addCriterion("direction in", values, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotIn(List<String> values) {
            addCriterion("direction not in", values, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionBetween(String value1, String value2) {
            addCriterion("direction between", value1, value2, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotBetween(String value1, String value2) {
            addCriterion("direction not between", value1, value2, "direction");
            return (Criteria) this;
        }

        public Criteria andEthertypeIsNull() {
            addCriterion("ethertype is null");
            return (Criteria) this;
        }

        public Criteria andEthertypeIsNotNull() {
            addCriterion("ethertype is not null");
            return (Criteria) this;
        }

        public Criteria andEthertypeEqualTo(String value) {
            addCriterion("ethertype =", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeNotEqualTo(String value) {
            addCriterion("ethertype <>", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeGreaterThan(String value) {
            addCriterion("ethertype >", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeGreaterThanOrEqualTo(String value) {
            addCriterion("ethertype >=", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeLessThan(String value) {
            addCriterion("ethertype <", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeLessThanOrEqualTo(String value) {
            addCriterion("ethertype <=", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeLike(String value) {
            addCriterion("ethertype like", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeNotLike(String value) {
            addCriterion("ethertype not like", value, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeIn(List<String> values) {
            addCriterion("ethertype in", values, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeNotIn(List<String> values) {
            addCriterion("ethertype not in", values, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeBetween(String value1, String value2) {
            addCriterion("ethertype between", value1, value2, "ethertype");
            return (Criteria) this;
        }

        public Criteria andEthertypeNotBetween(String value1, String value2) {
            addCriterion("ethertype not between", value1, value2, "ethertype");
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

        public Criteria andPortRangeMinIsNull() {
            addCriterion("port_range_min is null");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinIsNotNull() {
            addCriterion("port_range_min is not null");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinEqualTo(Integer value) {
            addCriterion("port_range_min =", value, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinNotEqualTo(Integer value) {
            addCriterion("port_range_min <>", value, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinGreaterThan(Integer value) {
            addCriterion("port_range_min >", value, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinGreaterThanOrEqualTo(Integer value) {
            addCriterion("port_range_min >=", value, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinLessThan(Integer value) {
            addCriterion("port_range_min <", value, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinLessThanOrEqualTo(Integer value) {
            addCriterion("port_range_min <=", value, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinIn(List<Integer> values) {
            addCriterion("port_range_min in", values, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinNotIn(List<Integer> values) {
            addCriterion("port_range_min not in", values, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinBetween(Integer value1, Integer value2) {
            addCriterion("port_range_min between", value1, value2, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMinNotBetween(Integer value1, Integer value2) {
            addCriterion("port_range_min not between", value1, value2, "portRangeMin");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxIsNull() {
            addCriterion("port_range_max is null");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxIsNotNull() {
            addCriterion("port_range_max is not null");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxEqualTo(Integer value) {
            addCriterion("port_range_max =", value, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxNotEqualTo(Integer value) {
            addCriterion("port_range_max <>", value, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxGreaterThan(Integer value) {
            addCriterion("port_range_max >", value, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxGreaterThanOrEqualTo(Integer value) {
            addCriterion("port_range_max >=", value, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxLessThan(Integer value) {
            addCriterion("port_range_max <", value, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxLessThanOrEqualTo(Integer value) {
            addCriterion("port_range_max <=", value, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxIn(List<Integer> values) {
            addCriterion("port_range_max in", values, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxNotIn(List<Integer> values) {
            addCriterion("port_range_max not in", values, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxBetween(Integer value1, Integer value2) {
            addCriterion("port_range_max between", value1, value2, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andPortRangeMaxNotBetween(Integer value1, Integer value2) {
            addCriterion("port_range_max not between", value1, value2, "portRangeMax");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixIsNull() {
            addCriterion("remote_ip_prefix is null");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixIsNotNull() {
            addCriterion("remote_ip_prefix is not null");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixEqualTo(String value) {
            addCriterion("remote_ip_prefix =", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixNotEqualTo(String value) {
            addCriterion("remote_ip_prefix <>", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixGreaterThan(String value) {
            addCriterion("remote_ip_prefix >", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixGreaterThanOrEqualTo(String value) {
            addCriterion("remote_ip_prefix >=", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixLessThan(String value) {
            addCriterion("remote_ip_prefix <", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixLessThanOrEqualTo(String value) {
            addCriterion("remote_ip_prefix <=", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixLike(String value) {
            addCriterion("remote_ip_prefix like", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixNotLike(String value) {
            addCriterion("remote_ip_prefix not like", value, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixIn(List<String> values) {
            addCriterion("remote_ip_prefix in", values, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixNotIn(List<String> values) {
            addCriterion("remote_ip_prefix not in", values, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixBetween(String value1, String value2) {
            addCriterion("remote_ip_prefix between", value1, value2, "remoteIpPrefix");
            return (Criteria) this;
        }

        public Criteria andRemoteIpPrefixNotBetween(String value1, String value2) {
            addCriterion("remote_ip_prefix not between", value1, value2, "remoteIpPrefix");
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