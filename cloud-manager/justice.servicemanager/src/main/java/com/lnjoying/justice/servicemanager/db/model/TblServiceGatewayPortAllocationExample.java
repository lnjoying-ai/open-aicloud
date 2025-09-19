package com.lnjoying.justice.servicemanager.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblServiceGatewayPortAllocationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblServiceGatewayPortAllocationExample() {
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

        public Criteria andPortRangeIdIsNull() {
            addCriterion("port_range_id is null");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdIsNotNull() {
            addCriterion("port_range_id is not null");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdEqualTo(String value) {
            addCriterion("port_range_id =", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdNotEqualTo(String value) {
            addCriterion("port_range_id <>", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdGreaterThan(String value) {
            addCriterion("port_range_id >", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdGreaterThanOrEqualTo(String value) {
            addCriterion("port_range_id >=", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdLessThan(String value) {
            addCriterion("port_range_id <", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdLessThanOrEqualTo(String value) {
            addCriterion("port_range_id <=", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdLike(String value) {
            addCriterion("port_range_id like", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdNotLike(String value) {
            addCriterion("port_range_id not like", value, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdIn(List<String> values) {
            addCriterion("port_range_id in", values, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdNotIn(List<String> values) {
            addCriterion("port_range_id not in", values, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdBetween(String value1, String value2) {
            addCriterion("port_range_id between", value1, value2, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andPortRangeIdNotBetween(String value1, String value2) {
            addCriterion("port_range_id not between", value1, value2, "portRangeId");
            return (Criteria) this;
        }

        public Criteria andExternalIpIsNull() {
            addCriterion("external_ip is null");
            return (Criteria) this;
        }

        public Criteria andExternalIpIsNotNull() {
            addCriterion("external_ip is not null");
            return (Criteria) this;
        }

        public Criteria andExternalIpEqualTo(String value) {
            addCriterion("external_ip =", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpNotEqualTo(String value) {
            addCriterion("external_ip <>", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpGreaterThan(String value) {
            addCriterion("external_ip >", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpGreaterThanOrEqualTo(String value) {
            addCriterion("external_ip >=", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpLessThan(String value) {
            addCriterion("external_ip <", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpLessThanOrEqualTo(String value) {
            addCriterion("external_ip <=", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpLike(String value) {
            addCriterion("external_ip like", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpNotLike(String value) {
            addCriterion("external_ip not like", value, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpIn(List<String> values) {
            addCriterion("external_ip in", values, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpNotIn(List<String> values) {
            addCriterion("external_ip not in", values, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpBetween(String value1, String value2) {
            addCriterion("external_ip between", value1, value2, "externalIp");
            return (Criteria) this;
        }

        public Criteria andExternalIpNotBetween(String value1, String value2) {
            addCriterion("external_ip not between", value1, value2, "externalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpIsNull() {
            addCriterion("internal_ip is null");
            return (Criteria) this;
        }

        public Criteria andInternalIpIsNotNull() {
            addCriterion("internal_ip is not null");
            return (Criteria) this;
        }

        public Criteria andInternalIpEqualTo(String value) {
            addCriterion("internal_ip =", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpNotEqualTo(String value) {
            addCriterion("internal_ip <>", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpGreaterThan(String value) {
            addCriterion("internal_ip >", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpGreaterThanOrEqualTo(String value) {
            addCriterion("internal_ip >=", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpLessThan(String value) {
            addCriterion("internal_ip <", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpLessThanOrEqualTo(String value) {
            addCriterion("internal_ip <=", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpLike(String value) {
            addCriterion("internal_ip like", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpNotLike(String value) {
            addCriterion("internal_ip not like", value, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpIn(List<String> values) {
            addCriterion("internal_ip in", values, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpNotIn(List<String> values) {
            addCriterion("internal_ip not in", values, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpBetween(String value1, String value2) {
            addCriterion("internal_ip between", value1, value2, "internalIp");
            return (Criteria) this;
        }

        public Criteria andInternalIpNotBetween(String value1, String value2) {
            addCriterion("internal_ip not between", value1, value2, "internalIp");
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

        public Criteria andPortEqualTo(Integer value) {
            addCriterion("port =", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotEqualTo(Integer value) {
            addCriterion("port <>", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortGreaterThan(Integer value) {
            addCriterion("port >", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("port >=", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLessThan(Integer value) {
            addCriterion("port <", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortLessThanOrEqualTo(Integer value) {
            addCriterion("port <=", value, "port");
            return (Criteria) this;
        }

        public Criteria andPortIn(List<Integer> values) {
            addCriterion("port in", values, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotIn(List<Integer> values) {
            addCriterion("port not in", values, "port");
            return (Criteria) this;
        }

        public Criteria andPortBetween(Integer value1, Integer value2) {
            addCriterion("port between", value1, value2, "port");
            return (Criteria) this;
        }

        public Criteria andPortNotBetween(Integer value1, Integer value2) {
            addCriterion("port not between", value1, value2, "port");
            return (Criteria) this;
        }

        public Criteria andListenPortIsNull() {
            addCriterion("listen_port is null");
            return (Criteria) this;
        }

        public Criteria andListenPortIsNotNull() {
            addCriterion("listen_port is not null");
            return (Criteria) this;
        }

        public Criteria andListenPortEqualTo(Integer value) {
            addCriterion("listen_port =", value, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortNotEqualTo(Integer value) {
            addCriterion("listen_port <>", value, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortGreaterThan(Integer value) {
            addCriterion("listen_port >", value, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortGreaterThanOrEqualTo(Integer value) {
            addCriterion("listen_port >=", value, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortLessThan(Integer value) {
            addCriterion("listen_port <", value, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortLessThanOrEqualTo(Integer value) {
            addCriterion("listen_port <=", value, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortIn(List<Integer> values) {
            addCriterion("listen_port in", values, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortNotIn(List<Integer> values) {
            addCriterion("listen_port not in", values, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortBetween(Integer value1, Integer value2) {
            addCriterion("listen_port between", value1, value2, "listenPort");
            return (Criteria) this;
        }

        public Criteria andListenPortNotBetween(Integer value1, Integer value2) {
            addCriterion("listen_port not between", value1, value2, "listenPort");
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
            addCriterion("tbl_service_gateway_port_allocation.status <>", value, "status");
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

        public Criteria andServicePortTargetServiceIdIsNull() {
            addCriterion("service_port_target_service_id is null");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdIsNotNull() {
            addCriterion("service_port_target_service_id is not null");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdEqualTo(String value) {
            addCriterion("service_port_target_service_id =", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdNotEqualTo(String value) {
            addCriterion("service_port_target_service_id <>", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdGreaterThan(String value) {
            addCriterion("service_port_target_service_id >", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdGreaterThanOrEqualTo(String value) {
            addCriterion("service_port_target_service_id >=", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdLessThan(String value) {
            addCriterion("service_port_target_service_id <", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdLessThanOrEqualTo(String value) {
            addCriterion("service_port_target_service_id <=", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdLike(String value) {
            addCriterion("service_port_target_service_id like", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdNotLike(String value) {
            addCriterion("service_port_target_service_id not like", value, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdIn(List<String> values) {
            addCriterion("service_port_target_service_id in", values, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdNotIn(List<String> values) {
            addCriterion("service_port_target_service_id not in", values, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdBetween(String value1, String value2) {
            addCriterion("service_port_target_service_id between", value1, value2, "servicePortTargetServiceId");
            return (Criteria) this;
        }

        public Criteria andServicePortTargetServiceIdNotBetween(String value1, String value2) {
            addCriterion("service_port_target_service_id not between", value1, value2, "servicePortTargetServiceId");
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

        public Criteria andInternalIpEqualToOrIsNull(String value) {
            addCriterion("(internal_ip is null or internal_ip = '" + value + "')");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdEqualTo(String value) {
            addCriterion("tbl_service_gateway_port_info.service_gateway_id = '" + value + "'");
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