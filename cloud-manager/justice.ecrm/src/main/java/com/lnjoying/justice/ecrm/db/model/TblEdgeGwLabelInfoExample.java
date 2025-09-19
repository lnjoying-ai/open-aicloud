package com.lnjoying.justice.ecrm.db.model;

import java.util.ArrayList;
import java.util.List;

public class TblEdgeGwLabelInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblEdgeGwLabelInfoExample() {
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

        public Criteria andLabelKeyIsNull() {
            addCriterion("label_key is null");
            return (Criteria) this;
        }

        public Criteria andLabelKeyIsNotNull() {
            addCriterion("label_key is not null");
            return (Criteria) this;
        }

        public Criteria andLabelKeyEqualTo(String value) {
            addCriterion("label_key =", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyNotEqualTo(String value) {
            addCriterion("label_key <>", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyGreaterThan(String value) {
            addCriterion("label_key >", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyGreaterThanOrEqualTo(String value) {
            addCriterion("label_key >=", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyLessThan(String value) {
            addCriterion("label_key <", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyLessThanOrEqualTo(String value) {
            addCriterion("label_key <=", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyLike(String value) {
            addCriterion("label_key like", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyNotLike(String value) {
            addCriterion("label_key not like", value, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyIn(List<String> values) {
            addCriterion("label_key in", values, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyNotIn(List<String> values) {
            addCriterion("label_key not in", values, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyBetween(String value1, String value2) {
            addCriterion("label_key between", value1, value2, "labelKey");
            return (Criteria) this;
        }

        public Criteria andLabelKeyNotBetween(String value1, String value2) {
            addCriterion("label_key not between", value1, value2, "labelKey");
            return (Criteria) this;
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

        public Criteria andLabelValueIsNull() {
            addCriterion("label_value is null");
            return (Criteria) this;
        }

        public Criteria andLabelValueIsNotNull() {
            addCriterion("label_value is not null");
            return (Criteria) this;
        }

        public Criteria andLabelValueEqualTo(String value) {
            addCriterion("label_value =", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueNotEqualTo(String value) {
            addCriterion("label_value <>", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueGreaterThan(String value) {
            addCriterion("label_value >", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueGreaterThanOrEqualTo(String value) {
            addCriterion("label_value >=", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueLessThan(String value) {
            addCriterion("label_value <", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueLessThanOrEqualTo(String value) {
            addCriterion("label_value <=", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueLike(String value) {
            addCriterion("label_value like", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueNotLike(String value) {
            addCriterion("label_value not like", value, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueIn(List<String> values) {
            addCriterion("label_value in", values, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueNotIn(List<String> values) {
            addCriterion("label_value not in", values, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueBetween(String value1, String value2) {
            addCriterion("label_value between", value1, value2, "labelValue");
            return (Criteria) this;
        }

        public Criteria andLabelValueNotBetween(String value1, String value2) {
            addCriterion("label_value not between", value1, value2, "labelValue");
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