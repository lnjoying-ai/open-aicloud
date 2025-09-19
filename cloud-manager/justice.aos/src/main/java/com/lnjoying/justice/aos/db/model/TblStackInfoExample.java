package com.lnjoying.justice.aos.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
* @Description: 
* @Author: Regulus 
* @Date: 12/12/23 5:34 PM 
* */
public class TblStackInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;
    
    protected int startRow;
    
    protected int pageSize;
    
    public int getStartRow()
    {
        return startRow;
    }
    
    public void setStartRow(int startRow)
    {
        this.startRow = startRow;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public void setPageSize(int pageSize) { this.pageSize = pageSize;}

    public TblStackInfoExample() {
        oredCriteria = new ArrayList<>();
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
            criteria = new ArrayList<>();
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

        public Criteria andSpecIdIsNull() {
            addCriterion("spec_id is null");
            return (Criteria) this;
        }

        public Criteria andSpecIdIsNotNull() {
            addCriterion("spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpecIdEqualTo(String value) {
            addCriterion("spec_id =", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotEqualTo(String value) {
            addCriterion("spec_id <>", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdGreaterThan(String value) {
            addCriterion("spec_id >", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdGreaterThanOrEqualTo(String value) {
            addCriterion("spec_id >=", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLessThan(String value) {
            addCriterion("spec_id <", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLessThanOrEqualTo(String value) {
            addCriterion("spec_id <=", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLike(String value) {
            addCriterion("spec_id like", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotLike(String value) {
            addCriterion("spec_id not like", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdIn(List<String> values) {
            addCriterion("spec_id in", values, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotIn(List<String> values) {
            addCriterion("spec_id not in", values, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdBetween(String value1, String value2) {
            addCriterion("spec_id between", value1, value2, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotBetween(String value1, String value2) {
            addCriterion("spec_id not between", value1, value2, "specId");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("\"name\" is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("\"name\" is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("\"name\" =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("\"name\" <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("\"name\" >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("\"name\" >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("\"name\" <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("\"name\" <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("\"name\" like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("\"name\" not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("\"name\" in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("\"name\" not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("\"name\" between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("\"name\" not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andBpIdIsNull() {
            addCriterion("bp_id is null");
            return (Criteria) this;
        }

        public Criteria andBpIdIsNotNull() {
            addCriterion("bp_id is not null");
            return (Criteria) this;
        }

        public Criteria andBpIdEqualTo(String value) {
            addCriterion("bp_id =", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotEqualTo(String value) {
            addCriterion("bp_id <>", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdGreaterThan(String value) {
            addCriterion("bp_id >", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdGreaterThanOrEqualTo(String value) {
            addCriterion("bp_id >=", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLessThan(String value) {
            addCriterion("bp_id <", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLessThanOrEqualTo(String value) {
            addCriterion("bp_id <=", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLike(String value) {
            addCriterion("bp_id like", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotLike(String value) {
            addCriterion("bp_id not like", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdIn(List<String> values) {
            addCriterion("bp_id in", values, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotIn(List<String> values) {
            addCriterion("bp_id not in", values, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdBetween(String value1, String value2) {
            addCriterion("bp_id between", value1, value2, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotBetween(String value1, String value2) {
            addCriterion("bp_id not between", value1, value2, "bpId");
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

        public Criteria andCreateUserIdIsNull() {
            addCriterion("create_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIsNotNull() {
            addCriterion("create_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdEqualTo(String value) {
            addCriterion("create_user_id =", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotEqualTo(String value) {
            addCriterion("create_user_id <>", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThan(String value) {
            addCriterion("create_user_id >", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("create_user_id >=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThan(String value) {
            addCriterion("create_user_id <", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLessThanOrEqualTo(String value) {
            addCriterion("create_user_id <=", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdLike(String value) {
            addCriterion("create_user_id like", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotLike(String value) {
            addCriterion("create_user_id not like", value, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdIn(List<String> values) {
            addCriterion("create_user_id in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotIn(List<String> values) {
            addCriterion("create_user_id not in", values, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdBetween(String value1, String value2) {
            addCriterion("create_user_id between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andCreateUserIdNotBetween(String value1, String value2) {
            addCriterion("create_user_id not between", value1, value2, "createUserId");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("\"status\" is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("\"status\" is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("\"status\" =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("\"status\" <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("\"status\" >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("\"status\" >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("\"status\" <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("\"status\" <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("\"status\" in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("\"status\" not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("\"status\" between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("\"status\" not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andDstNodeIsNull() {
            addCriterion("dst_node is null");
            return (Criteria) this;
        }

        public Criteria andDstNodeSiteLike(String siteLike) {
            addCriterion("dst_node ->> 'dstSiteId' = '" + siteLike + "'");
            return (Criteria) this;
        }

        public Criteria andDstNodeIsNotNull() {
            addCriterion("dst_node is not null");
            return (Criteria) this;
        }

        public Criteria andDstNodeEqualTo(Object value) {
            addCriterion("dst_node =", value, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeNotEqualTo(Object value) {
            addCriterion("dst_node <>", value, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeGreaterThan(Object value) {
            addCriterion("dst_node >", value, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeGreaterThanOrEqualTo(Object value) {
            addCriterion("dst_node >=", value, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeLessThan(Object value) {
            addCriterion("dst_node <", value, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeLessThanOrEqualTo(Object value) {
            addCriterion("dst_node <=", value, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeIn(List<Object> values) {
            addCriterion("dst_node in", values, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeNotIn(List<Object> values) {
            addCriterion("dst_node not in", values, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeBetween(Object value1, Object value2) {
            addCriterion("dst_node between", value1, value2, "dstNode");
            return (Criteria) this;
        }

        public Criteria andDstNodeNotBetween(Object value1, Object value2) {
            addCriterion("dst_node not between", value1, value2, "dstNode");
            return (Criteria) this;
        }

        public Criteria andLabelsIsNull() {
            addCriterion("labels is null");
            return (Criteria) this;
        }

        public Criteria andLabelsIsNotNull() {
            addCriterion("labels is not null");
            return (Criteria) this;
        }

        public Criteria andLabelsEqualTo(String value) {
            addCriterion("labels =", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotEqualTo(String value) {
            addCriterion("labels <>", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsGreaterThan(String value) {
            addCriterion("labels >", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsGreaterThanOrEqualTo(String value) {
            addCriterion("labels >=", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLessThan(String value) {
            addCriterion("labels <", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLessThanOrEqualTo(String value) {
            addCriterion("labels <=", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLike(String value) {
            addCriterion("labels like", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotLike(String value) {
            addCriterion("labels not like", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsIn(List<String> values) {
            addCriterion("labels in", values, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotIn(List<String> values) {
            addCriterion("labels not in", values, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsBetween(String value1, String value2) {
            addCriterion("labels between", value1, value2, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotBetween(String value1, String value2) {
            addCriterion("labels not between", value1, value2, "labels");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIsNull() {
            addCriterion("dev_need_info is null");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIsNotNull() {
            addCriterion("dev_need_info is not null");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoEqualTo(String value) {
            addCriterion("dev_need_info =", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotEqualTo(String value) {
            addCriterion("dev_need_info <>", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoGreaterThan(String value) {
            addCriterion("dev_need_info >", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoGreaterThanOrEqualTo(String value) {
            addCriterion("dev_need_info >=", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLessThan(String value) {
            addCriterion("dev_need_info <", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLessThanOrEqualTo(String value) {
            addCriterion("dev_need_info <=", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLike(String value) {
            addCriterion("dev_need_info like", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotLike(String value) {
            addCriterion("dev_need_info not like", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIn(List<String> values) {
            addCriterion("dev_need_info in", values, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotIn(List<String> values) {
            addCriterion("dev_need_info not in", values, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoBetween(String value1, String value2) {
            addCriterion("dev_need_info between", value1, value2, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotBetween(String value1, String value2) {
            addCriterion("dev_need_info not between", value1, value2, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andAutoRunIsNull() {
            addCriterion("auto_run is null");
            return (Criteria) this;
        }

        public Criteria andAutoRunIsNotNull() {
            addCriterion("auto_run is not null");
            return (Criteria) this;
        }

        public Criteria andAutoRunEqualTo(Boolean value) {
            addCriterion("auto_run =", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotEqualTo(Boolean value) {
            addCriterion("auto_run <>", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunGreaterThan(Boolean value) {
            addCriterion("auto_run >", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunGreaterThanOrEqualTo(Boolean value) {
            addCriterion("auto_run >=", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunLessThan(Boolean value) {
            addCriterion("auto_run <", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunLessThanOrEqualTo(Boolean value) {
            addCriterion("auto_run <=", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunIn(List<Boolean> values) {
            addCriterion("auto_run in", values, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotIn(List<Boolean> values) {
            addCriterion("auto_run not in", values, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunBetween(Boolean value1, Boolean value2) {
            addCriterion("auto_run between", value1, value2, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotBetween(Boolean value1, Boolean value2) {
            addCriterion("auto_run not between", value1, value2, "autoRun");
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

        public Criteria andSendCreateNumIsNull() {
            addCriterion("send_create_num is null");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumIsNotNull() {
            addCriterion("send_create_num is not null");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumEqualTo(Integer value) {
            addCriterion("send_create_num =", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumNotEqualTo(Integer value) {
            addCriterion("send_create_num <>", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumGreaterThan(Integer value) {
            addCriterion("send_create_num >", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("send_create_num >=", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumLessThan(Integer value) {
            addCriterion("send_create_num <", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumLessThanOrEqualTo(Integer value) {
            addCriterion("send_create_num <=", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumIn(List<Integer> values) {
            addCriterion("send_create_num in", values, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumNotIn(List<Integer> values) {
            addCriterion("send_create_num not in", values, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumBetween(Integer value1, Integer value2) {
            addCriterion("send_create_num between", value1, value2, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumNotBetween(Integer value1, Integer value2) {
            addCriterion("send_create_num not between", value1, value2, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andStartNumIsNull() {
            addCriterion("start_num is null");
            return (Criteria) this;
        }

        public Criteria andStartNumIsNotNull() {
            addCriterion("start_num is not null");
            return (Criteria) this;
        }

        public Criteria andStartNumEqualTo(Integer value) {
            addCriterion("start_num =", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumNotEqualTo(Integer value) {
            addCriterion("start_num <>", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumGreaterThan(Integer value) {
            addCriterion("start_num >", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("start_num >=", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumLessThan(Integer value) {
            addCriterion("start_num <", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumLessThanOrEqualTo(Integer value) {
            addCriterion("start_num <=", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumIn(List<Integer> values) {
            addCriterion("start_num in", values, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumNotIn(List<Integer> values) {
            addCriterion("start_num not in", values, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumBetween(Integer value1, Integer value2) {
            addCriterion("start_num between", value1, value2, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumNotBetween(Integer value1, Integer value2) {
            addCriterion("start_num not between", value1, value2, "startNum");
            return (Criteria) this;
        }

        public Criteria andFailNumIsNull() {
            addCriterion("fail_num is null");
            return (Criteria) this;
        }

        public Criteria andFailNumIsNotNull() {
            addCriterion("fail_num is not null");
            return (Criteria) this;
        }

        public Criteria andFailNumEqualTo(Integer value) {
            addCriterion("fail_num =", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumNotEqualTo(Integer value) {
            addCriterion("fail_num <>", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumGreaterThan(Integer value) {
            addCriterion("fail_num >", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("fail_num >=", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumLessThan(Integer value) {
            addCriterion("fail_num <", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumLessThanOrEqualTo(Integer value) {
            addCriterion("fail_num <=", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumIn(List<Integer> values) {
            addCriterion("fail_num in", values, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumNotIn(List<Integer> values) {
            addCriterion("fail_num not in", values, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumBetween(Integer value1, Integer value2) {
            addCriterion("fail_num between", value1, value2, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumNotBetween(Integer value1, Integer value2) {
            addCriterion("fail_num not between", value1, value2, "failNum");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNull() {
            addCriterion("event_type is null");
            return (Criteria) this;
        }

        public Criteria andEventTypeIsNotNull() {
            addCriterion("event_type is not null");
            return (Criteria) this;
        }

        public Criteria andEventTypeEqualTo(Integer value) {
            addCriterion("event_type =", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotEqualTo(Integer value) {
            addCriterion("event_type <>", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThan(Integer value) {
            addCriterion("event_type >", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("event_type >=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThan(Integer value) {
            addCriterion("event_type <", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeLessThanOrEqualTo(Integer value) {
            addCriterion("event_type <=", value, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeIn(List<Integer> values) {
            addCriterion("event_type in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotIn(List<Integer> values) {
            addCriterion("event_type not in", values, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeBetween(Integer value1, Integer value2) {
            addCriterion("event_type between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andEventTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("event_type not between", value1, value2, "eventType");
            return (Criteria) this;
        }

        public Criteria andReportTimeIsNull() {
            addCriterion("report_time is null");
            return (Criteria) this;
        }

        public Criteria andReportTimeIsNotNull() {
            addCriterion("report_time is not null");
            return (Criteria) this;
        }

        public Criteria andReportTimeEqualTo(Date value) {
            addCriterion("report_time =", value, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeNotEqualTo(Date value) {
            addCriterion("report_time <>", value, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeGreaterThan(Date value) {
            addCriterion("report_time >", value, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("report_time >=", value, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeLessThan(Date value) {
            addCriterion("report_time <", value, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeLessThanOrEqualTo(Date value) {
            addCriterion("report_time <=", value, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeIn(List<Date> values) {
            addCriterion("report_time in", values, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeNotIn(List<Date> values) {
            addCriterion("report_time not in", values, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeBetween(Date value1, Date value2) {
            addCriterion("report_time between", value1, value2, "reportTime");
            return (Criteria) this;
        }

        public Criteria andReportTimeNotBetween(Date value1, Date value2) {
            addCriterion("report_time not between", value1, value2, "reportTime");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineIsNull() {
            addCriterion("always_online is null");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineIsNotNull() {
            addCriterion("always_online is not null");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineEqualTo(Boolean value) {
            addCriterion("always_online =", value, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineNotEqualTo(Boolean value) {
            addCriterion("always_online <>", value, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineGreaterThan(Boolean value) {
            addCriterion("always_online >", value, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineGreaterThanOrEqualTo(Boolean value) {
            addCriterion("always_online >=", value, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineLessThan(Boolean value) {
            addCriterion("always_online <", value, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineLessThanOrEqualTo(Boolean value) {
            addCriterion("always_online <=", value, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineIn(List<Boolean> values) {
            addCriterion("always_online in", values, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineNotIn(List<Boolean> values) {
            addCriterion("always_online not in", values, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineBetween(Boolean value1, Boolean value2) {
            addCriterion("always_online between", value1, value2, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andAlwaysOnlineNotBetween(Boolean value1, Boolean value2) {
            addCriterion("always_online not between", value1, value2, "alwaysOnline");
            return (Criteria) this;
        }

        public Criteria andStackTypeIsNull() {
            addCriterion("stack_type is null");
            return (Criteria) this;
        }

        public Criteria andStackTypeIsNotNull() {
            addCriterion("stack_type is not null");
            return (Criteria) this;
        }

        public Criteria andStackTypeEqualTo(String value) {
            addCriterion("stack_type =", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeNotEqualTo(String value) {
            addCriterion("stack_type <>", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeGreaterThan(String value) {
            addCriterion("stack_type >", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeGreaterThanOrEqualTo(String value) {
            addCriterion("stack_type >=", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeLessThan(String value) {
            addCriterion("stack_type <", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeLessThanOrEqualTo(String value) {
            addCriterion("stack_type <=", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeLike(String value) {
            addCriterion("stack_type like", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeNotLike(String value) {
            addCriterion("stack_type not like", value, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeIn(List<String> values) {
            addCriterion("stack_type in", values, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeNotIn(List<String> values) {
            addCriterion("stack_type not in", values, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeBetween(String value1, String value2) {
            addCriterion("stack_type between", value1, value2, "stackType");
            return (Criteria) this;
        }

        public Criteria andStackTypeNotBetween(String value1, String value2) {
            addCriterion("stack_type not between", value1, value2, "stackType");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationIsNull() {
            addCriterion("use_service_penetration is null");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationIsNotNull() {
            addCriterion("use_service_penetration is not null");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationEqualTo(Boolean value) {
            addCriterion("use_service_penetration =", value, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationNotEqualTo(Boolean value) {
            addCriterion("use_service_penetration <>", value, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationGreaterThan(Boolean value) {
            addCriterion("use_service_penetration >", value, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationGreaterThanOrEqualTo(Boolean value) {
            addCriterion("use_service_penetration >=", value, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationLessThan(Boolean value) {
            addCriterion("use_service_penetration <", value, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationLessThanOrEqualTo(Boolean value) {
            addCriterion("use_service_penetration <=", value, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationIn(List<Boolean> values) {
            addCriterion("use_service_penetration in", values, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationNotIn(List<Boolean> values) {
            addCriterion("use_service_penetration not in", values, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationBetween(Boolean value1, Boolean value2) {
            addCriterion("use_service_penetration between", value1, value2, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andUseServicePenetrationNotBetween(Boolean value1, Boolean value2) {
            addCriterion("use_service_penetration not between", value1, value2, "useServicePenetration");
            return (Criteria) this;
        }

        public Criteria andExposePortsIsNull() {
            addCriterion("expose_ports is null");
            return (Criteria) this;
        }

        public Criteria andExposePortsIsNotNull() {
            addCriterion("expose_ports is not null");
            return (Criteria) this;
        }

        public Criteria andExposePortsEqualTo(Object value) {
            addCriterion("expose_ports =", value, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsNotEqualTo(Object value) {
            addCriterion("expose_ports <>", value, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsGreaterThan(Object value) {
            addCriterion("expose_ports >", value, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsGreaterThanOrEqualTo(Object value) {
            addCriterion("expose_ports >=", value, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsLessThan(Object value) {
            addCriterion("expose_ports <", value, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsLessThanOrEqualTo(Object value) {
            addCriterion("expose_ports <=", value, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsIn(List<Object> values) {
            addCriterion("expose_ports in", values, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsNotIn(List<Object> values) {
            addCriterion("expose_ports not in", values, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsBetween(Object value1, Object value2) {
            addCriterion("expose_ports between", value1, value2, "exposePorts");
            return (Criteria) this;
        }

        public Criteria andExposePortsNotBetween(Object value1, Object value2) {
            addCriterion("expose_ports not between", value1, value2, "exposePorts");
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