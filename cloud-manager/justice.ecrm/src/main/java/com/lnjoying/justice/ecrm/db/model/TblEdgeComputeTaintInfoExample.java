package com.lnjoying.justice.ecrm.db.model;

import java.util.ArrayList;
import java.util.List;

public class TblEdgeComputeTaintInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblEdgeComputeTaintInfoExample() {
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

        public Criteria andNodeIdIsNull() {
            addCriterion("node_id is null");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNotNull() {
            addCriterion("node_id is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIdEqualTo(String value) {
            addCriterion("node_id =", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotEqualTo(String value) {
            addCriterion("node_id <>", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThan(String value) {
            addCriterion("node_id >", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("node_id >=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThan(String value) {
            addCriterion("node_id <", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThanOrEqualTo(String value) {
            addCriterion("node_id <=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLike(String value) {
            addCriterion("node_id like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotLike(String value) {
            addCriterion("node_id not like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIn(List<String> values) {
            addCriterion("node_id in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotIn(List<String> values) {
            addCriterion("node_id not in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdBetween(String value1, String value2) {
            addCriterion("node_id between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotBetween(String value1, String value2) {
            addCriterion("node_id not between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andTaintKeyIsNull() {
            addCriterion("taint_key is null");
            return (Criteria) this;
        }

        public Criteria andTaintKeyIsNotNull() {
            addCriterion("taint_key is not null");
            return (Criteria) this;
        }

        public Criteria andTaintKeyEqualTo(String value) {
            addCriterion("taint_key =", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyNotEqualTo(String value) {
            addCriterion("taint_key <>", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyGreaterThan(String value) {
            addCriterion("taint_key >", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyGreaterThanOrEqualTo(String value) {
            addCriterion("taint_key >=", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyLessThan(String value) {
            addCriterion("taint_key <", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyLessThanOrEqualTo(String value) {
            addCriterion("taint_key <=", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyLike(String value) {
            addCriterion("taint_key like", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyNotLike(String value) {
            addCriterion("taint_key not like", value, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyIn(List<String> values) {
            addCriterion("taint_key in", values, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyNotIn(List<String> values) {
            addCriterion("taint_key not in", values, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyBetween(String value1, String value2) {
            addCriterion("taint_key between", value1, value2, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintKeyNotBetween(String value1, String value2) {
            addCriterion("taint_key not between", value1, value2, "taintKey");
            return (Criteria) this;
        }

        public Criteria andTaintValueIsNull() {
            addCriterion("taint_value is null");
            return (Criteria) this;
        }

        public Criteria andTaintValueIsNotNull() {
            addCriterion("taint_value is not null");
            return (Criteria) this;
        }

        public Criteria andTaintValueEqualTo(String value) {
            addCriterion("taint_value =", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueNotEqualTo(String value) {
            addCriterion("taint_value <>", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueGreaterThan(String value) {
            addCriterion("taint_value >", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueGreaterThanOrEqualTo(String value) {
            addCriterion("taint_value >=", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueLessThan(String value) {
            addCriterion("taint_value <", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueLessThanOrEqualTo(String value) {
            addCriterion("taint_value <=", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueLike(String value) {
            addCriterion("taint_value like", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueNotLike(String value) {
            addCriterion("taint_value not like", value, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueIn(List<String> values) {
            addCriterion("taint_value in", values, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueNotIn(List<String> values) {
            addCriterion("taint_value not in", values, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueBetween(String value1, String value2) {
            addCriterion("taint_value between", value1, value2, "taintValue");
            return (Criteria) this;
        }

        public Criteria andTaintValueNotBetween(String value1, String value2) {
            addCriterion("taint_value not between", value1, value2, "taintValue");
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