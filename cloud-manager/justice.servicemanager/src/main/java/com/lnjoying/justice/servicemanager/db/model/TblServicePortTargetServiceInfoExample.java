package com.lnjoying.justice.servicemanager.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblServicePortTargetServiceInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblServicePortTargetServiceInfoExample() {
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

        public Criteria andServicePortIdIsNull() {
            addCriterion("service_port_id is null");
            return (Criteria) this;
        }

        public Criteria andServicePortIdIsNotNull() {
            addCriterion("service_port_id is not null");
            return (Criteria) this;
        }

        public Criteria andServicePortIdEqualTo(String value) {
            addCriterion("service_port_id =", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdNotEqualTo(String value) {
            addCriterion("service_port_id <>", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdGreaterThan(String value) {
            addCriterion("service_port_id >", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdGreaterThanOrEqualTo(String value) {
            addCriterion("service_port_id >=", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdLessThan(String value) {
            addCriterion("service_port_id <", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdLessThanOrEqualTo(String value) {
            addCriterion("service_port_id <=", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdLike(String value) {
            addCriterion("service_port_id like", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdNotLike(String value) {
            addCriterion("service_port_id not like", value, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdIn(List<String> values) {
            addCriterion("service_port_id in", values, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdNotIn(List<String> values) {
            addCriterion("service_port_id not in", values, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdBetween(String value1, String value2) {
            addCriterion("service_port_id between", value1, value2, "servicePortId");
            return (Criteria) this;
        }

        public Criteria andServicePortIdNotBetween(String value1, String value2) {
            addCriterion("service_port_id not between", value1, value2, "servicePortId");
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

        public Criteria andPortAllocationIdIsNull() {
            addCriterion("port_allocation_id is null");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdIsNotNull() {
            addCriterion("port_allocation_id is not null");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdEqualTo(String value) {
            addCriterion("port_allocation_id =", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdNotEqualTo(String value) {
            addCriterion("port_allocation_id <>", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdGreaterThan(String value) {
            addCriterion("port_allocation_id >", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdGreaterThanOrEqualTo(String value) {
            addCriterion("port_allocation_id >=", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdLessThan(String value) {
            addCriterion("port_allocation_id <", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdLessThanOrEqualTo(String value) {
            addCriterion("port_allocation_id <=", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdLike(String value) {
            addCriterion("port_allocation_id like", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdNotLike(String value) {
            addCriterion("port_allocation_id not like", value, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdIn(List<String> values) {
            addCriterion("port_allocation_id in", values, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdNotIn(List<String> values) {
            addCriterion("port_allocation_id not in", values, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdBetween(String value1, String value2) {
            addCriterion("port_allocation_id between", value1, value2, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andPortAllocationIdNotBetween(String value1, String value2) {
            addCriterion("port_allocation_id not between", value1, value2, "portAllocationId");
            return (Criteria) this;
        }

        public Criteria andSvcNodeIsNull() {
            addCriterion("svc_node is null");
            return (Criteria) this;
        }

        public Criteria andSvcNodeIsNotNull() {
            addCriterion("svc_node is not null");
            return (Criteria) this;
        }

        public Criteria andSvcNodeEqualTo(String value) {
            addCriterion("svc_node =", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeNotEqualTo(String value) {
            addCriterion("svc_node <>", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeGreaterThan(String value) {
            addCriterion("svc_node >", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeGreaterThanOrEqualTo(String value) {
            addCriterion("svc_node >=", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeLessThan(String value) {
            addCriterion("svc_node <", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeLessThanOrEqualTo(String value) {
            addCriterion("svc_node <=", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeLike(String value) {
            addCriterion("svc_node like", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeNotLike(String value) {
            addCriterion("svc_node not like", value, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeIn(List<String> values) {
            addCriterion("svc_node in", values, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeNotIn(List<String> values) {
            addCriterion("svc_node not in", values, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeBetween(String value1, String value2) {
            addCriterion("svc_node between", value1, value2, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcNodeNotBetween(String value1, String value2) {
            addCriterion("svc_node not between", value1, value2, "svcNode");
            return (Criteria) this;
        }

        public Criteria andSvcIpIsNull() {
            addCriterion("svc_ip is null");
            return (Criteria) this;
        }

        public Criteria andSvcIpIsNotNull() {
            addCriterion("svc_ip is not null");
            return (Criteria) this;
        }

        public Criteria andSvcIpEqualTo(String value) {
            addCriterion("svc_ip =", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpNotEqualTo(String value) {
            addCriterion("svc_ip <>", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpGreaterThan(String value) {
            addCriterion("svc_ip >", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpGreaterThanOrEqualTo(String value) {
            addCriterion("svc_ip >=", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpLessThan(String value) {
            addCriterion("svc_ip <", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpLessThanOrEqualTo(String value) {
            addCriterion("svc_ip <=", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpLike(String value) {
            addCriterion("svc_ip like", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpNotLike(String value) {
            addCriterion("svc_ip not like", value, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpIn(List<String> values) {
            addCriterion("svc_ip in", values, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpNotIn(List<String> values) {
            addCriterion("svc_ip not in", values, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpBetween(String value1, String value2) {
            addCriterion("svc_ip between", value1, value2, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcIpNotBetween(String value1, String value2) {
            addCriterion("svc_ip not between", value1, value2, "svcIp");
            return (Criteria) this;
        }

        public Criteria andSvcPortIsNull() {
            addCriterion("svc_port is null");
            return (Criteria) this;
        }

        public Criteria andSvcPortIsNotNull() {
            addCriterion("svc_port is not null");
            return (Criteria) this;
        }

        public Criteria andSvcPortEqualTo(Integer value) {
            addCriterion("svc_port =", value, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortNotEqualTo(Integer value) {
            addCriterion("svc_port <>", value, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortGreaterThan(Integer value) {
            addCriterion("svc_port >", value, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("svc_port >=", value, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortLessThan(Integer value) {
            addCriterion("svc_port <", value, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortLessThanOrEqualTo(Integer value) {
            addCriterion("svc_port <=", value, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortIn(List<Integer> values) {
            addCriterion("svc_port in", values, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortNotIn(List<Integer> values) {
            addCriterion("svc_port not in", values, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortBetween(Integer value1, Integer value2) {
            addCriterion("svc_port between", value1, value2, "svcPort");
            return (Criteria) this;
        }

        public Criteria andSvcPortNotBetween(Integer value1, Integer value2) {
            addCriterion("svc_port not between", value1, value2, "svcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeIsNull() {
            addCriterion("target_svc_node is null");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeIsNotNull() {
            addCriterion("target_svc_node is not null");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeEqualTo(String value) {
            addCriterion("target_svc_node =", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeNotEqualTo(String value) {
            addCriterion("target_svc_node <>", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeGreaterThan(String value) {
            addCriterion("target_svc_node >", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeGreaterThanOrEqualTo(String value) {
            addCriterion("target_svc_node >=", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeLessThan(String value) {
            addCriterion("target_svc_node <", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeLessThanOrEqualTo(String value) {
            addCriterion("target_svc_node <=", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeLike(String value) {
            addCriterion("target_svc_node like", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeNotLike(String value) {
            addCriterion("target_svc_node not like", value, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeIn(List<String> values) {
            addCriterion("target_svc_node in", values, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeNotIn(List<String> values) {
            addCriterion("target_svc_node not in", values, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeBetween(String value1, String value2) {
            addCriterion("target_svc_node between", value1, value2, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcNodeNotBetween(String value1, String value2) {
            addCriterion("target_svc_node not between", value1, value2, "targetSvcNode");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortIsNull() {
            addCriterion("target_svc_port is null");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortIsNotNull() {
            addCriterion("target_svc_port is not null");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortEqualTo(Integer value) {
            addCriterion("target_svc_port =", value, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortNotEqualTo(Integer value) {
            addCriterion("target_svc_port <>", value, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortGreaterThan(Integer value) {
            addCriterion("target_svc_port >", value, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("target_svc_port >=", value, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortLessThan(Integer value) {
            addCriterion("target_svc_port <", value, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortLessThanOrEqualTo(Integer value) {
            addCriterion("target_svc_port <=", value, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortIn(List<Integer> values) {
            addCriterion("target_svc_port in", values, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortNotIn(List<Integer> values) {
            addCriterion("target_svc_port not in", values, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortBetween(Integer value1, Integer value2) {
            addCriterion("target_svc_port between", value1, value2, "targetSvcPort");
            return (Criteria) this;
        }

        public Criteria andTargetSvcPortNotBetween(Integer value1, Integer value2) {
            addCriterion("target_svc_port not between", value1, value2, "targetSvcPort");
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

        public Criteria andSiteIsNull() {
            addCriterion("site is null");
            return (Criteria) this;
        }

        public Criteria andSiteIsNotNull() {
            addCriterion("site is not null");
            return (Criteria) this;
        }

        public Criteria andSiteEqualTo(String value) {
            addCriterion("site =", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotEqualTo(String value) {
            addCriterion("site <>", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteGreaterThan(String value) {
            addCriterion("site >", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteGreaterThanOrEqualTo(String value) {
            addCriterion("site >=", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteLessThan(String value) {
            addCriterion("site <", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteLessThanOrEqualTo(String value) {
            addCriterion("site <=", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteLike(String value) {
            addCriterion("site like", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotLike(String value) {
            addCriterion("site not like", value, "site");
            return (Criteria) this;
        }

        public Criteria andSiteIn(List<String> values) {
            addCriterion("site in", values, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotIn(List<String> values) {
            addCriterion("site not in", values, "site");
            return (Criteria) this;
        }

        public Criteria andSiteBetween(String value1, String value2) {
            addCriterion("site between", value1, value2, "site");
            return (Criteria) this;
        }

        public Criteria andSiteNotBetween(String value1, String value2) {
            addCriterion("site not between", value1, value2, "site");
            return (Criteria) this;
        }

        public Criteria andServiceIsNull() {
            addCriterion("service is null");
            return (Criteria) this;
        }

        public Criteria andServiceIsNotNull() {
            addCriterion("service is not null");
            return (Criteria) this;
        }

        public Criteria andServiceEqualTo(String value) {
            addCriterion("service =", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotEqualTo(String value) {
            addCriterion("service <>", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceGreaterThan(String value) {
            addCriterion("service >", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceGreaterThanOrEqualTo(String value) {
            addCriterion("service >=", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceLessThan(String value) {
            addCriterion("service <", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceLessThanOrEqualTo(String value) {
            addCriterion("service <=", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceLike(String value) {
            addCriterion("service like", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotLike(String value) {
            addCriterion("service not like", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceIn(List<String> values) {
            addCriterion("service in", values, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotIn(List<String> values) {
            addCriterion("service not in", values, "service");
            return (Criteria) this;
        }

        public Criteria andServiceBetween(String value1, String value2) {
            addCriterion("service between", value1, value2, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotBetween(String value1, String value2) {
            addCriterion("service not between", value1, value2, "service");
            return (Criteria) this;
        }

        public Criteria andTargetIpIsNull() {
            addCriterion("target_ip is null");
            return (Criteria) this;
        }

        public Criteria andTargetIpIsNotNull() {
            addCriterion("target_ip is not null");
            return (Criteria) this;
        }

        public Criteria andTargetIpEqualTo(String value) {
            addCriterion("target_ip =", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpNotEqualTo(String value) {
            addCriterion("target_ip <>", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpGreaterThan(String value) {
            addCriterion("target_ip >", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpGreaterThanOrEqualTo(String value) {
            addCriterion("target_ip >=", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpLessThan(String value) {
            addCriterion("target_ip <", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpLessThanOrEqualTo(String value) {
            addCriterion("target_ip <=", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpLike(String value) {
            addCriterion("target_ip like", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpNotLike(String value) {
            addCriterion("target_ip not like", value, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpIn(List<String> values) {
            addCriterion("target_ip in", values, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpNotIn(List<String> values) {
            addCriterion("target_ip not in", values, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpBetween(String value1, String value2) {
            addCriterion("target_ip between", value1, value2, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetIpNotBetween(String value1, String value2) {
            addCriterion("target_ip not between", value1, value2, "targetIp");
            return (Criteria) this;
        }

        public Criteria andTargetPortIsNull() {
            addCriterion("target_port is null");
            return (Criteria) this;
        }

        public Criteria andTargetPortIsNotNull() {
            addCriterion("target_port is not null");
            return (Criteria) this;
        }

        public Criteria andTargetPortEqualTo(Integer value) {
            addCriterion("target_port =", value, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortNotEqualTo(Integer value) {
            addCriterion("target_port <>", value, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortGreaterThan(Integer value) {
            addCriterion("target_port >", value, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("target_port >=", value, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortLessThan(Integer value) {
            addCriterion("target_port <", value, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortLessThanOrEqualTo(Integer value) {
            addCriterion("target_port <=", value, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortIn(List<Integer> values) {
            addCriterion("target_port in", values, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortNotIn(List<Integer> values) {
            addCriterion("target_port not in", values, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortBetween(Integer value1, Integer value2) {
            addCriterion("target_port between", value1, value2, "targetPort");
            return (Criteria) this;
        }

        public Criteria andTargetPortNotBetween(Integer value1, Integer value2) {
            addCriterion("target_port not between", value1, value2, "targetPort");
            return (Criteria) this;
        }

        public Criteria andCertIsNull() {
            addCriterion("cert is null");
            return (Criteria) this;
        }

        public Criteria andCertIsNotNull() {
            addCriterion("cert is not null");
            return (Criteria) this;
        }

        public Criteria andCertEqualTo(String value) {
            addCriterion("cert =", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertNotEqualTo(String value) {
            addCriterion("cert <>", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertGreaterThan(String value) {
            addCriterion("cert >", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertGreaterThanOrEqualTo(String value) {
            addCriterion("cert >=", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertLessThan(String value) {
            addCriterion("cert <", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertLessThanOrEqualTo(String value) {
            addCriterion("cert <=", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertLike(String value) {
            addCriterion("cert like", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertNotLike(String value) {
            addCriterion("cert not like", value, "cert");
            return (Criteria) this;
        }

        public Criteria andCertIn(List<String> values) {
            addCriterion("cert in", values, "cert");
            return (Criteria) this;
        }

        public Criteria andCertNotIn(List<String> values) {
            addCriterion("cert not in", values, "cert");
            return (Criteria) this;
        }

        public Criteria andCertBetween(String value1, String value2) {
            addCriterion("cert between", value1, value2, "cert");
            return (Criteria) this;
        }

        public Criteria andCertNotBetween(String value1, String value2) {
            addCriterion("cert not between", value1, value2, "cert");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
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