package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpSecurityGroupRuleExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpSecurityGroupRuleExample() {
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

        public Criteria andRuleIdIsNull() {
            addCriterion("rule_id is null");
            return (Criteria) this;
        }

        public Criteria andRuleIdIsNotNull() {
            addCriterion("rule_id is not null");
            return (Criteria) this;
        }

        public Criteria andRuleIdEqualTo(String value) {
            addCriterion("rule_id =", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotEqualTo(String value) {
            addCriterion("rule_id <>", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThan(String value) {
            addCriterion("rule_id >", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdGreaterThanOrEqualTo(String value) {
            addCriterion("rule_id >=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThan(String value) {
            addCriterion("rule_id <", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLessThanOrEqualTo(String value) {
            addCriterion("rule_id <=", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdLike(String value) {
            addCriterion("rule_id like", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotLike(String value) {
            addCriterion("rule_id not like", value, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdIn(List<String> values) {
            addCriterion("rule_id in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotIn(List<String> values) {
            addCriterion("rule_id not in", values, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdBetween(String value1, String value2) {
            addCriterion("rule_id between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andRuleIdNotBetween(String value1, String value2) {
            addCriterion("rule_id not between", value1, value2, "ruleId");
            return (Criteria) this;
        }

        public Criteria andSgIdIsNull() {
            addCriterion("sg_id is null");
            return (Criteria) this;
        }

        public Criteria andSgIdIsNotNull() {
            addCriterion("sg_id is not null");
            return (Criteria) this;
        }

        public Criteria andSgIdEqualTo(String value) {
            addCriterion("sg_id =", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotEqualTo(String value) {
            addCriterion("sg_id <>", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdGreaterThan(String value) {
            addCriterion("sg_id >", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdGreaterThanOrEqualTo(String value) {
            addCriterion("sg_id >=", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdLessThan(String value) {
            addCriterion("sg_id <", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdLessThanOrEqualTo(String value) {
            addCriterion("sg_id <=", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdLike(String value) {
            addCriterion("sg_id like", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotLike(String value) {
            addCriterion("sg_id not like", value, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdIn(List<String> values) {
            addCriterion("sg_id in", values, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotIn(List<String> values) {
            addCriterion("sg_id not in", values, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdBetween(String value1, String value2) {
            addCriterion("sg_id between", value1, value2, "sgId");
            return (Criteria) this;
        }

        public Criteria andSgIdNotBetween(String value1, String value2) {
            addCriterion("sg_id not between", value1, value2, "sgId");
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

        public Criteria andPriorityIsNull() {
            addCriterion("priority is null");
            return (Criteria) this;
        }

        public Criteria andPriorityIsNotNull() {
            addCriterion("priority is not null");
            return (Criteria) this;
        }

        public Criteria andPriorityEqualTo(Short value) {
            addCriterion("priority =", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityNotEqualTo(Short value) {
            addCriterion("priority <>", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityGreaterThan(Short value) {
            addCriterion("priority >", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityGreaterThanOrEqualTo(Short value) {
            addCriterion("priority >=", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityLessThan(Short value) {
            addCriterion("priority <", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityLessThanOrEqualTo(Short value) {
            addCriterion("priority <=", value, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityIn(List<Short> values) {
            addCriterion("priority in", values, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityNotIn(List<Short> values) {
            addCriterion("priority not in", values, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityBetween(Short value1, Short value2) {
            addCriterion("priority between", value1, value2, "priority");
            return (Criteria) this;
        }

        public Criteria andPriorityNotBetween(Short value1, Short value2) {
            addCriterion("priority not between", value1, value2, "priority");
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

        public Criteria andDirectionEqualTo(Short value) {
            addCriterion("direction =", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotEqualTo(Short value) {
            addCriterion("direction <>", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionGreaterThan(Short value) {
            addCriterion("direction >", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionGreaterThanOrEqualTo(Short value) {
            addCriterion("direction >=", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionLessThan(Short value) {
            addCriterion("direction <", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionLessThanOrEqualTo(Short value) {
            addCriterion("direction <=", value, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionIn(List<Short> values) {
            addCriterion("direction in", values, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotIn(List<Short> values) {
            addCriterion("direction not in", values, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionBetween(Short value1, Short value2) {
            addCriterion("direction between", value1, value2, "direction");
            return (Criteria) this;
        }

        public Criteria andDirectionNotBetween(Short value1, Short value2) {
            addCriterion("direction not between", value1, value2, "direction");
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

        public Criteria andProtocolEqualTo(Short value) {
            addCriterion("protocol =", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotEqualTo(Short value) {
            addCriterion("protocol <>", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolGreaterThan(Short value) {
            addCriterion("protocol >", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolGreaterThanOrEqualTo(Short value) {
            addCriterion("protocol >=", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLessThan(Short value) {
            addCriterion("protocol <", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolLessThanOrEqualTo(Short value) {
            addCriterion("protocol <=", value, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolIn(List<Short> values) {
            addCriterion("protocol in", values, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotIn(List<Short> values) {
            addCriterion("protocol not in", values, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolBetween(Short value1, Short value2) {
            addCriterion("protocol between", value1, value2, "protocol");
            return (Criteria) this;
        }

        public Criteria andProtocolNotBetween(Short value1, Short value2) {
            addCriterion("protocol not between", value1, value2, "protocol");
            return (Criteria) this;
        }

        public Criteria andPortIsNull() {
            addCriterion("port is null");
            return (Criteria) this;
        }

        public Criteria andPortIsNotNull() {
            addCriterion("port is not null");
            return (Criteria) this;
        }

        public Criteria andPortEqualTo(String value) {
            addCriterion("port =", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotEqualTo(String value) {
            addCriterion("port <>", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortGreaterThan(String value) {
            addCriterion("port >", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortGreaterThanOrEqualTo(String value) {
            addCriterion("port >=", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLessThan(String value) {
            addCriterion("port <", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLessThanOrEqualTo(String value) {
            addCriterion("port <=", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLike(String value) {
            addCriterion("port like", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotLike(String value) {
            addCriterion("port not like", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortIn(List<String> values) {
            addCriterion("port in", values, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotIn(List<String> values) {
            addCriterion("port not in", values, "port");
            return (Criteria) this;
        }

        public Criteria andPortBetween(String value1, String value2) {
            addCriterion("port between", value1, value2, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotBetween(String value1, String value2) {
            addCriterion("port not between", value1, value2, "port");
            return (Criteria) this;
        }

        public Criteria andCidrIsNull() {
            addCriterion("cidr is null");
            return (Criteria) this;
        }

        public Criteria andCidrIsNotNull() {
            addCriterion("cidr is not null");
            return (Criteria) this;
        }

        public Criteria andCidrEqualTo(String value) {
            addCriterion("cidr =", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrNotEqualTo(String value) {
            addCriterion("cidr <>", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrGreaterThan(String value) {
            addCriterion("cidr >", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrGreaterThanOrEqualTo(String value) {
            addCriterion("cidr >=", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrLessThan(String value) {
            addCriterion("cidr <", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrLessThanOrEqualTo(String value) {
            addCriterion("cidr <=", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrLike(String value) {
            addCriterion("cidr like", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrNotLike(String value) {
            addCriterion("cidr not like", value, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrIn(List<String> values) {
            addCriterion("cidr in", values, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrNotIn(List<String> values) {
            addCriterion("cidr not in", values, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrBetween(String value1, String value2) {
            addCriterion("cidr between", value1, value2, "cidr");
            return (Criteria) this;
        }

        public Criteria andCidrNotBetween(String value1, String value2) {
            addCriterion("cidr not between", value1, value2, "cidr");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceIsNull() {
            addCriterion("sg_id_reference is null");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceIsNotNull() {
            addCriterion("sg_id_reference is not null");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceEqualTo(String value) {
            addCriterion("sg_id_reference =", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceNotEqualTo(String value) {
            addCriterion("sg_id_reference <>", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceGreaterThan(String value) {
            addCriterion("sg_id_reference >", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceGreaterThanOrEqualTo(String value) {
            addCriterion("sg_id_reference >=", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceLessThan(String value) {
            addCriterion("sg_id_reference <", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceLessThanOrEqualTo(String value) {
            addCriterion("sg_id_reference <=", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceLike(String value) {
            addCriterion("sg_id_reference like", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceNotLike(String value) {
            addCriterion("sg_id_reference not like", value, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceIn(List<String> values) {
            addCriterion("sg_id_reference in", values, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceNotIn(List<String> values) {
            addCriterion("sg_id_reference not in", values, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceBetween(String value1, String value2) {
            addCriterion("sg_id_reference between", value1, value2, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andSgIdReferenceNotBetween(String value1, String value2) {
            addCriterion("sg_id_reference not between", value1, value2, "sgIdReference");
            return (Criteria) this;
        }

        public Criteria andPoolIdIsNull() {
            addCriterion("pool_id is null");
            return (Criteria) this;
        }

        public Criteria andPoolIdIsNotNull() {
            addCriterion("pool_id is not null");
            return (Criteria) this;
        }

        public Criteria andPoolIdEqualTo(String value) {
            addCriterion("pool_id =", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdNotEqualTo(String value) {
            addCriterion("pool_id <>", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdGreaterThan(String value) {
            addCriterion("pool_id >", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdGreaterThanOrEqualTo(String value) {
            addCriterion("pool_id >=", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdLessThan(String value) {
            addCriterion("pool_id <", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdLessThanOrEqualTo(String value) {
            addCriterion("pool_id <=", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdLike(String value) {
            addCriterion("pool_id like", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdNotLike(String value) {
            addCriterion("pool_id not like", value, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdIn(List<String> values) {
            addCriterion("pool_id in", values, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdNotIn(List<String> values) {
            addCriterion("pool_id not in", values, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdBetween(String value1, String value2) {
            addCriterion("pool_id between", value1, value2, "poolId");
            return (Criteria) this;
        }

        public Criteria andPoolIdNotBetween(String value1, String value2) {
            addCriterion("pool_id not between", value1, value2, "poolId");
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

        public Criteria andActionIsNull() {
            addCriterion("action is null");
            return (Criteria) this;
        }

        public Criteria andActionIsNotNull() {
            addCriterion("action is not null");
            return (Criteria) this;
        }

        public Criteria andActionEqualTo(Short value) {
            addCriterion("action =", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotEqualTo(Short value) {
            addCriterion("action <>", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionGreaterThan(Short value) {
            addCriterion("action >", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionGreaterThanOrEqualTo(Short value) {
            addCriterion("action >=", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLessThan(Short value) {
            addCriterion("action <", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionLessThanOrEqualTo(Short value) {
            addCriterion("action <=", value, "action");
            return (Criteria) this;
        }

        public Criteria andActionIn(List<Short> values) {
            addCriterion("action in", values, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotIn(List<Short> values) {
            addCriterion("action not in", values, "action");
            return (Criteria) this;
        }

        public Criteria andActionBetween(Short value1, Short value2) {
            addCriterion("action between", value1, value2, "action");
            return (Criteria) this;
        }

        public Criteria andActionNotBetween(Short value1, Short value2) {
            addCriterion("action not between", value1, value2, "action");
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