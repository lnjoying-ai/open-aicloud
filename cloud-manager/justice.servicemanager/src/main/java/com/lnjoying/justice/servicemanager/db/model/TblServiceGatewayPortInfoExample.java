package com.lnjoying.justice.servicemanager.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblServiceGatewayPortInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblServiceGatewayPortInfoExample() {
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

        public Criteria andServiceGatewayIdIsNull() {
            addCriterion("service_gateway_id is null");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdIsNotNull() {
            addCriterion("service_gateway_id is not null");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdEqualTo(String value) {
            addCriterion("service_gateway_id =", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdNotEqualTo(String value) {
            addCriterion("service_gateway_id <>", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdGreaterThan(String value) {
            addCriterion("service_gateway_id >", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdGreaterThanOrEqualTo(String value) {
            addCriterion("service_gateway_id >=", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdLessThan(String value) {
            addCriterion("service_gateway_id <", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdLessThanOrEqualTo(String value) {
            addCriterion("service_gateway_id <=", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdLike(String value) {
            addCriterion("service_gateway_id like", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdNotLike(String value) {
            addCriterion("service_gateway_id not like", value, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdIn(List<String> values) {
            addCriterion("service_gateway_id in", values, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdNotIn(List<String> values) {
            addCriterion("service_gateway_id not in", values, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdBetween(String value1, String value2) {
            addCriterion("service_gateway_id between", value1, value2, "serviceGatewayId");
            return (Criteria) this;
        }

        public Criteria andServiceGatewayIdNotBetween(String value1, String value2) {
            addCriterion("service_gateway_id not between", value1, value2, "serviceGatewayId");
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

        public Criteria andListenPortRangeMinIsNull() {
            addCriterion("listen_port_range_min is null");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinIsNotNull() {
            addCriterion("listen_port_range_min is not null");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinEqualTo(Integer value) {
            addCriterion("listen_port_range_min =", value, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinNotEqualTo(Integer value) {
            addCriterion("listen_port_range_min <>", value, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinGreaterThan(Integer value) {
            addCriterion("listen_port_range_min >", value, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinGreaterThanOrEqualTo(Integer value) {
            addCriterion("listen_port_range_min >=", value, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinLessThan(Integer value) {
            addCriterion("listen_port_range_min <", value, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinLessThanOrEqualTo(Integer value) {
            addCriterion("listen_port_range_min <=", value, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinIn(List<Integer> values) {
            addCriterion("listen_port_range_min in", values, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinNotIn(List<Integer> values) {
            addCriterion("listen_port_range_min not in", values, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinBetween(Integer value1, Integer value2) {
            addCriterion("listen_port_range_min between", value1, value2, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMinNotBetween(Integer value1, Integer value2) {
            addCriterion("listen_port_range_min not between", value1, value2, "listenPortRangeMin");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxIsNull() {
            addCriterion("listen_port_range_max is null");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxIsNotNull() {
            addCriterion("listen_port_range_max is not null");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxEqualTo(Integer value) {
            addCriterion("listen_port_range_max =", value, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxNotEqualTo(Integer value) {
            addCriterion("listen_port_range_max <>", value, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxGreaterThan(Integer value) {
            addCriterion("listen_port_range_max >", value, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxGreaterThanOrEqualTo(Integer value) {
            addCriterion("listen_port_range_max >=", value, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxLessThan(Integer value) {
            addCriterion("listen_port_range_max <", value, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxLessThanOrEqualTo(Integer value) {
            addCriterion("listen_port_range_max <=", value, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxIn(List<Integer> values) {
            addCriterion("listen_port_range_max in", values, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxNotIn(List<Integer> values) {
            addCriterion("listen_port_range_max not in", values, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxBetween(Integer value1, Integer value2) {
            addCriterion("listen_port_range_max between", value1, value2, "listenPortRangeMax");
            return (Criteria) this;
        }

        public Criteria andListenPortRangeMaxNotBetween(Integer value1, Integer value2) {
            addCriterion("listen_port_range_max not between", value1, value2, "listenPortRangeMax");
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

        public Criteria andTotalIsNull() {
            addCriterion("total is null");
            return (Criteria) this;
        }

        public Criteria andTotalIsNotNull() {
            addCriterion("total is not null");
            return (Criteria) this;
        }

        public Criteria andTotalEqualTo(Integer value) {
            addCriterion("total =", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotEqualTo(Integer value) {
            addCriterion("total <>", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThan(Integer value) {
            addCriterion("total >", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalGreaterThanOrEqualTo(Integer value) {
            addCriterion("total >=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThan(Integer value) {
            addCriterion("total <", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalLessThanOrEqualTo(Integer value) {
            addCriterion("total <=", value, "total");
            return (Criteria) this;
        }

        public Criteria andTotalIn(List<Integer> values) {
            addCriterion("total in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotIn(List<Integer> values) {
            addCriterion("total not in", values, "total");
            return (Criteria) this;
        }

        public Criteria andTotalBetween(Integer value1, Integer value2) {
            addCriterion("total between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andTotalNotBetween(Integer value1, Integer value2) {
            addCriterion("total not between", value1, value2, "total");
            return (Criteria) this;
        }

        public Criteria andLeftIsNull() {
            addCriterion("left is null");
            return (Criteria) this;
        }

        public Criteria andLeftIsNotNull() {
            addCriterion("left is not null");
            return (Criteria) this;
        }

        public Criteria andLeftEqualTo(Integer value) {
            addCriterion("left =", value, "left");
            return (Criteria) this;
        }

        public Criteria andLeftNotEqualTo(Integer value) {
            addCriterion("left <>", value, "left");
            return (Criteria) this;
        }

        public Criteria andLeftGreaterThan(Integer value) {
            addCriterion("left >", value, "left");
            return (Criteria) this;
        }

        public Criteria andLeftGreaterThanOrEqualTo(Integer value) {
            addCriterion("left >=", value, "left");
            return (Criteria) this;
        }

        public Criteria andLeftLessThan(Integer value) {
            addCriterion("left <", value, "left");
            return (Criteria) this;
        }

        public Criteria andLeftLessThanOrEqualTo(Integer value) {
            addCriterion("left <=", value, "left");
            return (Criteria) this;
        }

        public Criteria andLeftIn(List<Integer> values) {
            addCriterion("left in", values, "left");
            return (Criteria) this;
        }

        public Criteria andLeftNotIn(List<Integer> values) {
            addCriterion("left not in", values, "left");
            return (Criteria) this;
        }

        public Criteria andLeftBetween(Integer value1, Integer value2) {
            addCriterion("left between", value1, value2, "left");
            return (Criteria) this;
        }

        public Criteria andLeftNotBetween(Integer value1, Integer value2) {
            addCriterion("left not between", value1, value2, "left");
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