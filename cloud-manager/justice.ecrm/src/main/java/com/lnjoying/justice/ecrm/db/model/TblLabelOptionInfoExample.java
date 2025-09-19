package com.lnjoying.justice.ecrm.db.model;

import java.util.ArrayList;
import java.util.List;

public class TblLabelOptionInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblLabelOptionInfoExample() {
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

        public Criteria andKeyIsNull() {
            addCriterion("key is null");
            return (Criteria) this;
        }

        public Criteria andKeyIsNotNull() {
            addCriterion("key is not null");
            return (Criteria) this;
        }

        public Criteria andKeyEqualTo(String value) {
            addCriterion("key =", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyNotEqualTo(String value) {
            addCriterion("key <>", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyGreaterThan(String value) {
            addCriterion("key >", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyGreaterThanOrEqualTo(String value) {
            addCriterion("key >=", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyLessThan(String value) {
            addCriterion("key <", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyLessThanOrEqualTo(String value) {
            addCriterion("key <=", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyLike(String value) {
            addCriterion("key like", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyNotLike(String value) {
            addCriterion("key not like", value, "key");
            return (Criteria) this;
        }

        public Criteria andKeyIn(List<String> values) {
            addCriterion("key in", values, "key");
            return (Criteria) this;
        }

        public Criteria andKeyNotIn(List<String> values) {
            addCriterion("key not in", values, "key");
            return (Criteria) this;
        }

        public Criteria andKeyBetween(String value1, String value2) {
            addCriterion("key between", value1, value2, "key");
            return (Criteria) this;
        }

        public Criteria andKeyNotBetween(String value1, String value2) {
            addCriterion("key not between", value1, value2, "key");
            return (Criteria) this;
        }

        public Criteria andLabelTypeIsNull() {
            addCriterion("label_type is null");
            return (Criteria) this;
        }

        public Criteria andLabelTypeIsNotNull() {
            addCriterion("label_type is not null");
            return (Criteria) this;
        }

        public Criteria andLabelTypeEqualTo(String value) {
            addCriterion("label_type =", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeNotEqualTo(String value) {
            addCriterion("label_type <>", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeGreaterThan(String value) {
            addCriterion("label_type >", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeGreaterThanOrEqualTo(String value) {
            addCriterion("label_type >=", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeLessThan(String value) {
            addCriterion("label_type <", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeLessThanOrEqualTo(String value) {
            addCriterion("label_type <=", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeLike(String value) {
            addCriterion("label_type like", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeNotLike(String value) {
            addCriterion("label_type not like", value, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeIn(List<String> values) {
            addCriterion("label_type in", values, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeNotIn(List<String> values) {
            addCriterion("label_type not in", values, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeBetween(String value1, String value2) {
            addCriterion("label_type between", value1, value2, "labelType");
            return (Criteria) this;
        }

        public Criteria andLabelTypeNotBetween(String value1, String value2) {
            addCriterion("label_type not between", value1, value2, "labelType");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(String value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(String value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(String value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(String value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(String value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(String value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLike(String value) {
            addCriterion("type like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotLike(String value) {
            addCriterion("type not like", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<String> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<String> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(String value1, String value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(String value1, String value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andLabelOptionIsNull() {
            addCriterion("label_option is null");
            return (Criteria) this;
        }

        public Criteria andLabelOptionIsNotNull() {
            addCriterion("label_option is not null");
            return (Criteria) this;
        }

        public Criteria andLabelOptionEqualTo(String value) {
            addCriterion("label_option =", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionNotEqualTo(String value) {
            addCriterion("label_option <>", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionGreaterThan(String value) {
            addCriterion("label_option >", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionGreaterThanOrEqualTo(String value) {
            addCriterion("label_option >=", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionLessThan(String value) {
            addCriterion("label_option <", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionLessThanOrEqualTo(String value) {
            addCriterion("label_option <=", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionLike(String value) {
            addCriterion("label_option like", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionNotLike(String value) {
            addCriterion("label_option not like", value, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionIn(List<String> values) {
            addCriterion("label_option in", values, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionNotIn(List<String> values) {
            addCriterion("label_option not in", values, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionBetween(String value1, String value2) {
            addCriterion("label_option between", value1, value2, "labelOption");
            return (Criteria) this;
        }

        public Criteria andLabelOptionNotBetween(String value1, String value2) {
            addCriterion("label_option not between", value1, value2, "labelOption");
            return (Criteria) this;
        }

        public Criteria andOrderNumIsNull() {
            addCriterion("order_num is null");
            return (Criteria) this;
        }

        public Criteria andOrderNumIsNotNull() {
            addCriterion("order_num is not null");
            return (Criteria) this;
        }

        public Criteria andOrderNumEqualTo(Integer value) {
            addCriterion("order_num =", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotEqualTo(Integer value) {
            addCriterion("order_num <>", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThan(Integer value) {
            addCriterion("order_num >", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("order_num >=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThan(Integer value) {
            addCriterion("order_num <", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumLessThanOrEqualTo(Integer value) {
            addCriterion("order_num <=", value, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumIn(List<Integer> values) {
            addCriterion("order_num in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotIn(List<Integer> values) {
            addCriterion("order_num not in", values, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumBetween(Integer value1, Integer value2) {
            addCriterion("order_num between", value1, value2, "orderNum");
            return (Criteria) this;
        }

        public Criteria andOrderNumNotBetween(Integer value1, Integer value2) {
            addCriterion("order_num not between", value1, value2, "orderNum");
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