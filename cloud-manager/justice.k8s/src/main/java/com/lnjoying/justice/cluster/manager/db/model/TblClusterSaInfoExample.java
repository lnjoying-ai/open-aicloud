package com.lnjoying.justice.cluster.manager.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblClusterSaInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblClusterSaInfoExample() {
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

        public Criteria andClusterIdIsNull() {
            addCriterion("cluster_id is null");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNotNull() {
            addCriterion("cluster_id is not null");
            return (Criteria) this;
        }

        public Criteria andClusterIdEqualTo(String value) {
            addCriterion("cluster_id =", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotEqualTo(String value) {
            addCriterion("cluster_id <>", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThan(String value) {
            addCriterion("cluster_id >", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_id >=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThan(String value) {
            addCriterion("cluster_id <", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThanOrEqualTo(String value) {
            addCriterion("cluster_id <=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLike(String value) {
            addCriterion("cluster_id like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotLike(String value) {
            addCriterion("cluster_id not like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdIn(List<String> values) {
            addCriterion("cluster_id in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotIn(List<String> values) {
            addCriterion("cluster_id not in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdBetween(String value1, String value2) {
            addCriterion("cluster_id between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotBetween(String value1, String value2) {
            addCriterion("cluster_id not between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andSecretTokenIsNull() {
            addCriterion("secret_token is null");
            return (Criteria) this;
        }

        public Criteria andSecretTokenIsNotNull() {
            addCriterion("secret_token is not null");
            return (Criteria) this;
        }

        public Criteria andSecretTokenEqualTo(String value) {
            addCriterion("secret_token =", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenNotEqualTo(String value) {
            addCriterion("secret_token <>", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenGreaterThan(String value) {
            addCriterion("secret_token >", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenGreaterThanOrEqualTo(String value) {
            addCriterion("secret_token >=", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenLessThan(String value) {
            addCriterion("secret_token <", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenLessThanOrEqualTo(String value) {
            addCriterion("secret_token <=", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenLike(String value) {
            addCriterion("secret_token like", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenNotLike(String value) {
            addCriterion("secret_token not like", value, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenIn(List<String> values) {
            addCriterion("secret_token in", values, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenNotIn(List<String> values) {
            addCriterion("secret_token not in", values, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenBetween(String value1, String value2) {
            addCriterion("secret_token between", value1, value2, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretTokenNotBetween(String value1, String value2) {
            addCriterion("secret_token not between", value1, value2, "secretToken");
            return (Criteria) this;
        }

        public Criteria andSecretNameIsNull() {
            addCriterion("secret_name is null");
            return (Criteria) this;
        }

        public Criteria andSecretNameIsNotNull() {
            addCriterion("secret_name is not null");
            return (Criteria) this;
        }

        public Criteria andSecretNameEqualTo(String value) {
            addCriterion("secret_name =", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameNotEqualTo(String value) {
            addCriterion("secret_name <>", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameGreaterThan(String value) {
            addCriterion("secret_name >", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameGreaterThanOrEqualTo(String value) {
            addCriterion("secret_name >=", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameLessThan(String value) {
            addCriterion("secret_name <", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameLessThanOrEqualTo(String value) {
            addCriterion("secret_name <=", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameLike(String value) {
            addCriterion("secret_name like", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameNotLike(String value) {
            addCriterion("secret_name not like", value, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameIn(List<String> values) {
            addCriterion("secret_name in", values, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameNotIn(List<String> values) {
            addCriterion("secret_name not in", values, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameBetween(String value1, String value2) {
            addCriterion("secret_name between", value1, value2, "secretName");
            return (Criteria) this;
        }

        public Criteria andSecretNameNotBetween(String value1, String value2) {
            addCriterion("secret_name not between", value1, value2, "secretName");
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