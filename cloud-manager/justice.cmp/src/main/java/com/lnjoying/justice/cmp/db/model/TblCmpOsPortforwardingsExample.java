package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.List;

public class TblCmpOsPortforwardingsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsPortforwardingsExample() {
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

        public Criteria andFloatingipIdIsNull() {
            addCriterion("floatingip_id is null");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdIsNotNull() {
            addCriterion("floatingip_id is not null");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdEqualTo(String value) {
            addCriterion("floatingip_id =", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdNotEqualTo(String value) {
            addCriterion("floatingip_id <>", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdGreaterThan(String value) {
            addCriterion("floatingip_id >", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdGreaterThanOrEqualTo(String value) {
            addCriterion("floatingip_id >=", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdLessThan(String value) {
            addCriterion("floatingip_id <", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdLessThanOrEqualTo(String value) {
            addCriterion("floatingip_id <=", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdLike(String value) {
            addCriterion("floatingip_id like", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdNotLike(String value) {
            addCriterion("floatingip_id not like", value, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdIn(List<String> values) {
            addCriterion("floatingip_id in", values, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdNotIn(List<String> values) {
            addCriterion("floatingip_id not in", values, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdBetween(String value1, String value2) {
            addCriterion("floatingip_id between", value1, value2, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andFloatingipIdNotBetween(String value1, String value2) {
            addCriterion("floatingip_id not between", value1, value2, "floatingipId");
            return (Criteria) this;
        }

        public Criteria andExternalPortIsNull() {
            addCriterion("external_port is null");
            return (Criteria) this;
        }

        public Criteria andExternalPortIsNotNull() {
            addCriterion("external_port is not null");
            return (Criteria) this;
        }

        public Criteria andExternalPortEqualTo(Integer value) {
            addCriterion("external_port =", value, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortNotEqualTo(Integer value) {
            addCriterion("external_port <>", value, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortGreaterThan(Integer value) {
            addCriterion("external_port >", value, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("external_port >=", value, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortLessThan(Integer value) {
            addCriterion("external_port <", value, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortLessThanOrEqualTo(Integer value) {
            addCriterion("external_port <=", value, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortIn(List<Integer> values) {
            addCriterion("external_port in", values, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortNotIn(List<Integer> values) {
            addCriterion("external_port not in", values, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortBetween(Integer value1, Integer value2) {
            addCriterion("external_port between", value1, value2, "externalPort");
            return (Criteria) this;
        }

        public Criteria andExternalPortNotBetween(Integer value1, Integer value2) {
            addCriterion("external_port not between", value1, value2, "externalPort");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdIsNull() {
            addCriterion("internal_neutron_port_id is null");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdIsNotNull() {
            addCriterion("internal_neutron_port_id is not null");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdEqualTo(String value) {
            addCriterion("internal_neutron_port_id =", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdNotEqualTo(String value) {
            addCriterion("internal_neutron_port_id <>", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdGreaterThan(String value) {
            addCriterion("internal_neutron_port_id >", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdGreaterThanOrEqualTo(String value) {
            addCriterion("internal_neutron_port_id >=", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdLessThan(String value) {
            addCriterion("internal_neutron_port_id <", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdLessThanOrEqualTo(String value) {
            addCriterion("internal_neutron_port_id <=", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdLike(String value) {
            addCriterion("internal_neutron_port_id like", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdNotLike(String value) {
            addCriterion("internal_neutron_port_id not like", value, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdIn(List<String> values) {
            addCriterion("internal_neutron_port_id in", values, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdNotIn(List<String> values) {
            addCriterion("internal_neutron_port_id not in", values, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdBetween(String value1, String value2) {
            addCriterion("internal_neutron_port_id between", value1, value2, "internalNeutronPortId");
            return (Criteria) this;
        }

        public Criteria andInternalNeutronPortIdNotBetween(String value1, String value2) {
            addCriterion("internal_neutron_port_id not between", value1, value2, "internalNeutronPortId");
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

        public Criteria andSocketIsNull() {
            addCriterion("socket is null");
            return (Criteria) this;
        }

        public Criteria andSocketIsNotNull() {
            addCriterion("socket is not null");
            return (Criteria) this;
        }

        public Criteria andSocketEqualTo(String value) {
            addCriterion("socket =", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketNotEqualTo(String value) {
            addCriterion("socket <>", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketGreaterThan(String value) {
            addCriterion("socket >", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketGreaterThanOrEqualTo(String value) {
            addCriterion("socket >=", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketLessThan(String value) {
            addCriterion("socket <", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketLessThanOrEqualTo(String value) {
            addCriterion("socket <=", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketLike(String value) {
            addCriterion("socket like", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketNotLike(String value) {
            addCriterion("socket not like", value, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketIn(List<String> values) {
            addCriterion("socket in", values, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketNotIn(List<String> values) {
            addCriterion("socket not in", values, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketBetween(String value1, String value2) {
            addCriterion("socket between", value1, value2, "socket");
            return (Criteria) this;
        }

        public Criteria andSocketNotBetween(String value1, String value2) {
            addCriterion("socket not between", value1, value2, "socket");
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