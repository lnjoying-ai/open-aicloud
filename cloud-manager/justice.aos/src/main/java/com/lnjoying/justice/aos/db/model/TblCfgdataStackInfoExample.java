package com.lnjoying.justice.aos.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCfgdataStackInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCfgdataStackInfoExample() {
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

        public Criteria andCfgVolumeIdIsNull() {
            addCriterion("cfg_volume_id is null");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdIsNotNull() {
            addCriterion("cfg_volume_id is not null");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdEqualTo(String value) {
            addCriterion("cfg_volume_id =", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdNotEqualTo(String value) {
            addCriterion("cfg_volume_id <>", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdGreaterThan(String value) {
            addCriterion("cfg_volume_id >", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdGreaterThanOrEqualTo(String value) {
            addCriterion("cfg_volume_id >=", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdLessThan(String value) {
            addCriterion("cfg_volume_id <", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdLessThanOrEqualTo(String value) {
            addCriterion("cfg_volume_id <=", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdLike(String value) {
            addCriterion("cfg_volume_id like", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdNotLike(String value) {
            addCriterion("cfg_volume_id not like", value, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdIn(List<String> values) {
            addCriterion("cfg_volume_id in", values, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdNotIn(List<String> values) {
            addCriterion("cfg_volume_id not in", values, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdBetween(String value1, String value2) {
            addCriterion("cfg_volume_id between", value1, value2, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andCfgVolumeIdNotBetween(String value1, String value2) {
            addCriterion("cfg_volume_id not between", value1, value2, "cfgVolumeId");
            return (Criteria) this;
        }

        public Criteria andDataIdIsNull() {
            addCriterion("data_id is null");
            return (Criteria) this;
        }

        public Criteria andDataIdIsNotNull() {
            addCriterion("data_id is not null");
            return (Criteria) this;
        }

        public Criteria andDataIdEqualTo(String value) {
            addCriterion("data_id =", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotEqualTo(String value) {
            addCriterion("data_id <>", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdGreaterThan(String value) {
            addCriterion("data_id >", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdGreaterThanOrEqualTo(String value) {
            addCriterion("data_id >=", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdLessThan(String value) {
            addCriterion("data_id <", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdLessThanOrEqualTo(String value) {
            addCriterion("data_id <=", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdLike(String value) {
            addCriterion("data_id like", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotLike(String value) {
            addCriterion("data_id not like", value, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdIn(List<String> values) {
            addCriterion("data_id in", values, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotIn(List<String> values) {
            addCriterion("data_id not in", values, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdBetween(String value1, String value2) {
            addCriterion("data_id between", value1, value2, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataIdNotBetween(String value1, String value2) {
            addCriterion("data_id not between", value1, value2, "dataId");
            return (Criteria) this;
        }

        public Criteria andDataGroupIsNull() {
            addCriterion("data_group is null");
            return (Criteria) this;
        }

        public Criteria andDataGroupIsNotNull() {
            addCriterion("data_group is not null");
            return (Criteria) this;
        }

        public Criteria andDataGroupEqualTo(String value) {
            addCriterion("data_group =", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupNotEqualTo(String value) {
            addCriterion("data_group <>", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupGreaterThan(String value) {
            addCriterion("data_group >", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupGreaterThanOrEqualTo(String value) {
            addCriterion("data_group >=", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupLessThan(String value) {
            addCriterion("data_group <", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupLessThanOrEqualTo(String value) {
            addCriterion("data_group <=", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupLike(String value) {
            addCriterion("data_group like", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupNotLike(String value) {
            addCriterion("data_group not like", value, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupIn(List<String> values) {
            addCriterion("data_group in", values, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupNotIn(List<String> values) {
            addCriterion("data_group not in", values, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupBetween(String value1, String value2) {
            addCriterion("data_group between", value1, value2, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataGroupNotBetween(String value1, String value2) {
            addCriterion("data_group not between", value1, value2, "dataGroup");
            return (Criteria) this;
        }

        public Criteria andDataHashIsNull() {
            addCriterion("data_hash is null");
            return (Criteria) this;
        }

        public Criteria andDataHashIsNotNull() {
            addCriterion("data_hash is not null");
            return (Criteria) this;
        }

        public Criteria andDataHashEqualTo(String value) {
            addCriterion("data_hash =", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashNotEqualTo(String value) {
            addCriterion("data_hash <>", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashGreaterThan(String value) {
            addCriterion("data_hash >", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashGreaterThanOrEqualTo(String value) {
            addCriterion("data_hash >=", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashLessThan(String value) {
            addCriterion("data_hash <", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashLessThanOrEqualTo(String value) {
            addCriterion("data_hash <=", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashLike(String value) {
            addCriterion("data_hash like", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashNotLike(String value) {
            addCriterion("data_hash not like", value, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashIn(List<String> values) {
            addCriterion("data_hash in", values, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashNotIn(List<String> values) {
            addCriterion("data_hash not in", values, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashBetween(String value1, String value2) {
            addCriterion("data_hash between", value1, value2, "dataHash");
            return (Criteria) this;
        }

        public Criteria andDataHashNotBetween(String value1, String value2) {
            addCriterion("data_hash not between", value1, value2, "dataHash");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("user_id not between", value1, value2, "userId");
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

        public Criteria andStackIdIsNull() {
            addCriterion("stack_id is null");
            return (Criteria) this;
        }

        public Criteria andStackIdIsNotNull() {
            addCriterion("stack_id is not null");
            return (Criteria) this;
        }

        public Criteria andStackIdEqualTo(String value) {
            addCriterion("stack_id =", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdNotEqualTo(String value) {
            addCriterion("stack_id <>", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdGreaterThan(String value) {
            addCriterion("stack_id >", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdGreaterThanOrEqualTo(String value) {
            addCriterion("stack_id >=", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdLessThan(String value) {
            addCriterion("stack_id <", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdLessThanOrEqualTo(String value) {
            addCriterion("stack_id <=", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdLike(String value) {
            addCriterion("stack_id like", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdNotLike(String value) {
            addCriterion("stack_id not like", value, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdIn(List<String> values) {
            addCriterion("stack_id in", values, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdNotIn(List<String> values) {
            addCriterion("stack_id not in", values, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdBetween(String value1, String value2) {
            addCriterion("stack_id between", value1, value2, "stackId");
            return (Criteria) this;
        }

        public Criteria andStackIdNotBetween(String value1, String value2) {
            addCriterion("stack_id not between", value1, value2, "stackId");
            return (Criteria) this;
        }

        public Criteria andSyncStateIsNull() {
            addCriterion("sync_state is null");
            return (Criteria) this;
        }

        public Criteria andSyncStateIsNotNull() {
            addCriterion("sync_state is not null");
            return (Criteria) this;
        }

        public Criteria andSyncStateEqualTo(Integer value) {
            addCriterion("sync_state =", value, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateNotEqualTo(Integer value) {
            addCriterion("sync_state <>", value, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateGreaterThan(Integer value) {
            addCriterion("sync_state >", value, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("sync_state >=", value, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateLessThan(Integer value) {
            addCriterion("sync_state <", value, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateLessThanOrEqualTo(Integer value) {
            addCriterion("sync_state <=", value, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateIn(List<Integer> values) {
            addCriterion("sync_state in", values, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateNotIn(List<Integer> values) {
            addCriterion("sync_state not in", values, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateBetween(Integer value1, Integer value2) {
            addCriterion("sync_state between", value1, value2, "syncState");
            return (Criteria) this;
        }

        public Criteria andSyncStateNotBetween(Integer value1, Integer value2) {
            addCriterion("sync_state not between", value1, value2, "syncState");
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