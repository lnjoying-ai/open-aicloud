package com.lnjoying.justice.aos.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/** 
* @Description: 
* @Author: Regulus 
* @Date: 12/12/23 5:34 PM 
* */
public class TblStackSpecInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblStackSpecInfoExample() {
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

        public Criteria andSpecNameIsNull() {
            addCriterion("spec_name is null");
            return (Criteria) this;
        }

        public Criteria andSpecNameIsNotNull() {
            addCriterion("spec_name is not null");
            return (Criteria) this;
        }

        public Criteria andSpecNameEqualTo(String value) {
            addCriterion("spec_name =", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotEqualTo(String value) {
            addCriterion("spec_name <>", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameGreaterThan(String value) {
            addCriterion("spec_name >", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameGreaterThanOrEqualTo(String value) {
            addCriterion("spec_name >=", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameLessThan(String value) {
            addCriterion("spec_name <", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameLessThanOrEqualTo(String value) {
            addCriterion("spec_name <=", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameLike(String value) {
            addCriterion("spec_name like", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotLike(String value) {
            addCriterion("spec_name not like", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameIn(List<String> values) {
            addCriterion("spec_name in", values, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotIn(List<String> values) {
            addCriterion("spec_name not in", values, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameBetween(String value1, String value2) {
            addCriterion("spec_name between", value1, value2, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotBetween(String value1, String value2) {
            addCriterion("spec_name not between", value1, value2, "specName");
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

        public Criteria andTemplateIdIsNull() {
            addCriterion("template_id is null");
            return (Criteria) this;
        }

        public Criteria andTemplateIdIsNotNull() {
            addCriterion("template_id is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateIdEqualTo(String value) {
            addCriterion("template_id =", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotEqualTo(String value) {
            addCriterion("template_id <>", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdGreaterThan(String value) {
            addCriterion("template_id >", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdGreaterThanOrEqualTo(String value) {
            addCriterion("template_id >=", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdLessThan(String value) {
            addCriterion("template_id <", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdLessThanOrEqualTo(String value) {
            addCriterion("template_id <=", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdLike(String value) {
            addCriterion("template_id like", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotLike(String value) {
            addCriterion("template_id not like", value, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdIn(List<String> values) {
            addCriterion("template_id in", values, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotIn(List<String> values) {
            addCriterion("template_id not in", values, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdBetween(String value1, String value2) {
            addCriterion("template_id between", value1, value2, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateIdNotBetween(String value1, String value2) {
            addCriterion("template_id not between", value1, value2, "templateId");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIsNull() {
            addCriterion("template_name is null");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIsNotNull() {
            addCriterion("template_name is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateNameEqualTo(String value) {
            addCriterion("template_name =", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotEqualTo(String value) {
            addCriterion("template_name <>", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameGreaterThan(String value) {
            addCriterion("template_name >", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameGreaterThanOrEqualTo(String value) {
            addCriterion("template_name >=", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLessThan(String value) {
            addCriterion("template_name <", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLessThanOrEqualTo(String value) {
            addCriterion("template_name <=", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameLike(String value) {
            addCriterion("template_name like", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotLike(String value) {
            addCriterion("template_name not like", value, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameIn(List<String> values) {
            addCriterion("template_name in", values, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotIn(List<String> values) {
            addCriterion("template_name not in", values, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameBetween(String value1, String value2) {
            addCriterion("template_name between", value1, value2, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateNameNotBetween(String value1, String value2) {
            addCriterion("template_name not between", value1, value2, "templateName");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionIsNull() {
            addCriterion("template_version is null");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionIsNotNull() {
            addCriterion("template_version is not null");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionEqualTo(String value) {
            addCriterion("template_version =", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionNotEqualTo(String value) {
            addCriterion("template_version <>", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionGreaterThan(String value) {
            addCriterion("template_version >", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionGreaterThanOrEqualTo(String value) {
            addCriterion("template_version >=", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionLessThan(String value) {
            addCriterion("template_version <", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionLessThanOrEqualTo(String value) {
            addCriterion("template_version <=", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionLike(String value) {
            addCriterion("template_version like", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionNotLike(String value) {
            addCriterion("template_version not like", value, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionIn(List<String> values) {
            addCriterion("template_version in", values, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionNotIn(List<String> values) {
            addCriterion("template_version not in", values, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionBetween(String value1, String value2) {
            addCriterion("template_version between", value1, value2, "templateVersion");
            return (Criteria) this;
        }

        public Criteria andTemplateVersionNotBetween(String value1, String value2) {
            addCriterion("template_version not between", value1, value2, "templateVersion");
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

        public Criteria andTargetNodesIsNull() {
            addCriterion("target_nodes is null");
            return (Criteria) this;
        }

        public Criteria andTargetNodesIsNotNull() {
            addCriterion("target_nodes is not null");
            return (Criteria) this;
        }

        public Criteria andTargetNodesEqualTo(String value) {
            addCriterion("target_nodes =", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesNotEqualTo(String value) {
            addCriterion("target_nodes <>", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesGreaterThan(String value) {
            addCriterion("target_nodes >", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesGreaterThanOrEqualTo(String value) {
            addCriterion("target_nodes >=", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesLessThan(String value) {
            addCriterion("target_nodes <", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesLessThanOrEqualTo(String value) {
            addCriterion("target_nodes <=", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesLike(String value) {
            addCriterion("target_nodes like", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesNotLike(String value) {
            addCriterion("target_nodes not like", value, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesIn(List<String> values) {
            addCriterion("target_nodes in", values, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesNotIn(List<String> values) {
            addCriterion("target_nodes not in", values, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesBetween(String value1, String value2) {
            addCriterion("target_nodes between", value1, value2, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andTargetNodesNotBetween(String value1, String value2) {
            addCriterion("target_nodes not between", value1, value2, "targetNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesIsNull() {
            addCriterion("dst_nodes is null");
            return (Criteria) this;
        }

        public Criteria andDstNodesIsNotNull() {
            addCriterion("dst_nodes is not null");
            return (Criteria) this;
        }

        public Criteria andDstNodesEqualTo(Object value) {
            addCriterion("dst_nodes =", value, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesNotEqualTo(Object value) {
            addCriterion("dst_nodes <>", value, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesGreaterThan(Object value) {
            addCriterion("dst_nodes >", value, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesGreaterThanOrEqualTo(Object value) {
            addCriterion("dst_nodes >=", value, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesLessThan(Object value) {
            addCriterion("dst_nodes <", value, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesLessThanOrEqualTo(Object value) {
            addCriterion("dst_nodes <=", value, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesIn(List<Object> values) {
            addCriterion("dst_nodes in", values, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesNotIn(List<Object> values) {
            addCriterion("dst_nodes not in", values, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesBetween(Object value1, Object value2) {
            addCriterion("dst_nodes between", value1, value2, "dstNodes");
            return (Criteria) this;
        }

        public Criteria andDstNodesNotBetween(Object value1, Object value2) {
            addCriterion("dst_nodes not between", value1, value2, "dstNodes");
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

        public Criteria andStackComposeIsNull() {
            addCriterion("stack_compose is null");
            return (Criteria) this;
        }

        public Criteria andStackComposeIsNotNull() {
            addCriterion("stack_compose is not null");
            return (Criteria) this;
        }

        public Criteria andStackComposeEqualTo(String value) {
            addCriterion("stack_compose =", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeNotEqualTo(String value) {
            addCriterion("stack_compose <>", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeGreaterThan(String value) {
            addCriterion("stack_compose >", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeGreaterThanOrEqualTo(String value) {
            addCriterion("stack_compose >=", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeLessThan(String value) {
            addCriterion("stack_compose <", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeLessThanOrEqualTo(String value) {
            addCriterion("stack_compose <=", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeLike(String value) {
            addCriterion("stack_compose like", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeNotLike(String value) {
            addCriterion("stack_compose not like", value, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeIn(List<String> values) {
            addCriterion("stack_compose in", values, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeNotIn(List<String> values) {
            addCriterion("stack_compose not in", values, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeBetween(String value1, String value2) {
            addCriterion("stack_compose between", value1, value2, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andStackComposeNotBetween(String value1, String value2) {
            addCriterion("stack_compose not between", value1, value2, "stackCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeIsNull() {
            addCriterion("justice_compose is null");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeIsNotNull() {
            addCriterion("justice_compose is not null");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeEqualTo(String value) {
            addCriterion("justice_compose =", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeNotEqualTo(String value) {
            addCriterion("justice_compose <>", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeGreaterThan(String value) {
            addCriterion("justice_compose >", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeGreaterThanOrEqualTo(String value) {
            addCriterion("justice_compose >=", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeLessThan(String value) {
            addCriterion("justice_compose <", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeLessThanOrEqualTo(String value) {
            addCriterion("justice_compose <=", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeLike(String value) {
            addCriterion("justice_compose like", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeNotLike(String value) {
            addCriterion("justice_compose not like", value, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeIn(List<String> values) {
            addCriterion("justice_compose in", values, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeNotIn(List<String> values) {
            addCriterion("justice_compose not in", values, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeBetween(String value1, String value2) {
            addCriterion("justice_compose between", value1, value2, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andJusticeComposeNotBetween(String value1, String value2) {
            addCriterion("justice_compose not between", value1, value2, "justiceCompose");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIsNull() {
            addCriterion("replica_num is null");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIsNotNull() {
            addCriterion("replica_num is not null");
            return (Criteria) this;
        }

        public Criteria andReplicaNumEqualTo(Integer value) {
            addCriterion("replica_num =", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotEqualTo(Integer value) {
            addCriterion("replica_num <>", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumGreaterThan(Integer value) {
            addCriterion("replica_num >", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("replica_num >=", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumLessThan(Integer value) {
            addCriterion("replica_num <", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumLessThanOrEqualTo(Integer value) {
            addCriterion("replica_num <=", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIn(List<Integer> values) {
            addCriterion("replica_num in", values, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotIn(List<Integer> values) {
            addCriterion("replica_num not in", values, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumBetween(Integer value1, Integer value2) {
            addCriterion("replica_num between", value1, value2, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotBetween(Integer value1, Integer value2) {
            addCriterion("replica_num not between", value1, value2, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andAosTypeIsNull() {
            addCriterion("aos_type is null");
            return (Criteria) this;
        }

        public Criteria andAosTypeIsNotNull() {
            addCriterion("aos_type is not null");
            return (Criteria) this;
        }

        public Criteria andAosTypeEqualTo(String value) {
            addCriterion("aos_type =", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeNotEqualTo(String value) {
            addCriterion("aos_type <>", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeGreaterThan(String value) {
            addCriterion("aos_type >", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeGreaterThanOrEqualTo(String value) {
            addCriterion("aos_type >=", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeLessThan(String value) {
            addCriterion("aos_type <", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeLessThanOrEqualTo(String value) {
            addCriterion("aos_type <=", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeLike(String value) {
            addCriterion("aos_type like", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeNotLike(String value) {
            addCriterion("aos_type not like", value, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeIn(List<String> values) {
            addCriterion("aos_type in", values, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeNotIn(List<String> values) {
            addCriterion("aos_type not in", values, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeBetween(String value1, String value2) {
            addCriterion("aos_type between", value1, value2, "aosType");
            return (Criteria) this;
        }

        public Criteria andAosTypeNotBetween(String value1, String value2) {
            addCriterion("aos_type not between", value1, value2, "aosType");
            return (Criteria) this;
        }

        public Criteria andContentTypeIsNull() {
            addCriterion("content_type is null");
            return (Criteria) this;
        }

        public Criteria andContentTypeIsNotNull() {
            addCriterion("content_type is not null");
            return (Criteria) this;
        }

        public Criteria andContentTypeEqualTo(String value) {
            addCriterion("content_type =", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeNotEqualTo(String value) {
            addCriterion("content_type <>", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeGreaterThan(String value) {
            addCriterion("content_type >", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeGreaterThanOrEqualTo(String value) {
            addCriterion("content_type >=", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeLessThan(String value) {
            addCriterion("content_type <", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeLessThanOrEqualTo(String value) {
            addCriterion("content_type <=", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeLike(String value) {
            addCriterion("content_type like", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeNotLike(String value) {
            addCriterion("content_type not like", value, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeIn(List<String> values) {
            addCriterion("content_type in", values, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeNotIn(List<String> values) {
            addCriterion("content_type not in", values, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeBetween(String value1, String value2) {
            addCriterion("content_type between", value1, value2, "contentType");
            return (Criteria) this;
        }

        public Criteria andContentTypeNotBetween(String value1, String value2) {
            addCriterion("content_type not between", value1, value2, "contentType");
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

        public Criteria andRegistryIdIsNull() {
            addCriterion("registry_id is null");
            return (Criteria) this;
        }

        public Criteria andRegistryIdIsNotNull() {
            addCriterion("registry_id is not null");
            return (Criteria) this;
        }

        public Criteria andRegistryIdEqualTo(String value) {
            addCriterion("registry_id =", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotEqualTo(String value) {
            addCriterion("registry_id <>", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdGreaterThan(String value) {
            addCriterion("registry_id >", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdGreaterThanOrEqualTo(String value) {
            addCriterion("registry_id >=", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdLessThan(String value) {
            addCriterion("registry_id <", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdLessThanOrEqualTo(String value) {
            addCriterion("registry_id <=", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdLike(String value) {
            addCriterion("registry_id like", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotLike(String value) {
            addCriterion("registry_id not like", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdIn(List<String> values) {
            addCriterion("registry_id in", values, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotIn(List<String> values) {
            addCriterion("registry_id not in", values, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdBetween(String value1, String value2) {
            addCriterion("registry_id between", value1, value2, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotBetween(String value1, String value2) {
            addCriterion("registry_id not between", value1, value2, "registryId");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyIsNull() {
            addCriterion("scheduling_strategy is null");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyIsNotNull() {
            addCriterion("scheduling_strategy is not null");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyEqualTo(String value) {
            addCriterion("scheduling_strategy =", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotEqualTo(String value) {
            addCriterion("scheduling_strategy <>", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyGreaterThan(String value) {
            addCriterion("scheduling_strategy >", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyGreaterThanOrEqualTo(String value) {
            addCriterion("scheduling_strategy >=", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyLessThan(String value) {
            addCriterion("scheduling_strategy <", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyLessThanOrEqualTo(String value) {
            addCriterion("scheduling_strategy <=", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyLike(String value) {
            addCriterion("scheduling_strategy like", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotLike(String value) {
            addCriterion("scheduling_strategy not like", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyIn(List<String> values) {
            addCriterion("scheduling_strategy in", values, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotIn(List<String> values) {
            addCriterion("scheduling_strategy not in", values, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyBetween(String value1, String value2) {
            addCriterion("scheduling_strategy between", value1, value2, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotBetween(String value1, String value2) {
            addCriterion("scheduling_strategy not between", value1, value2, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andExtraParamsIsNull() {
            addCriterion("extra_params is null");
            return (Criteria) this;
        }

        public Criteria andExtraParamsIsNotNull() {
            addCriterion("extra_params is not null");
            return (Criteria) this;
        }

        public Criteria andExtraParamsEqualTo(Object value) {
            addCriterion("extra_params =", value, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsNotEqualTo(Object value) {
            addCriterion("extra_params <>", value, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsGreaterThan(Object value) {
            addCriterion("extra_params >", value, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsGreaterThanOrEqualTo(Object value) {
            addCriterion("extra_params >=", value, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsLessThan(Object value) {
            addCriterion("extra_params <", value, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsLessThanOrEqualTo(Object value) {
            addCriterion("extra_params <=", value, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsIn(List<Object> values) {
            addCriterion("extra_params in", values, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsNotIn(List<Object> values) {
            addCriterion("extra_params not in", values, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsBetween(Object value1, Object value2) {
            addCriterion("extra_params between", value1, value2, "extraParams");
            return (Criteria) this;
        }

        public Criteria andExtraParamsNotBetween(Object value1, Object value2) {
            addCriterion("extra_params not between", value1, value2, "extraParams");
            return (Criteria) this;
        }

        public Criteria andImageNamesIsNull() {
            addCriterion("image_names is null");
            return (Criteria) this;
        }

        public Criteria andImageNamesIsNotNull() {
            addCriterion("image_names is not null");
            return (Criteria) this;
        }

        public Criteria andImageNamesEqualTo(Object value) {
            addCriterion("image_names =", value, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesNotEqualTo(Object value) {
            addCriterion("image_names <>", value, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesGreaterThan(Object value) {
            addCriterion("image_names >", value, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesGreaterThanOrEqualTo(Object value) {
            addCriterion("image_names >=", value, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesLessThan(Object value) {
            addCriterion("image_names <", value, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesLessThanOrEqualTo(Object value) {
            addCriterion("image_names <=", value, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesIn(List<Object> values) {
            addCriterion("image_names in", values, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesNotIn(List<Object> values) {
            addCriterion("image_names not in", values, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesBetween(Object value1, Object value2) {
            addCriterion("image_names between", value1, value2, "imageNames");
            return (Criteria) this;
        }

        public Criteria andImageNamesNotBetween(Object value1, Object value2) {
            addCriterion("image_names not between", value1, value2, "imageNames");
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

        public Criteria andFailoverPolicyIsNull() {
            addCriterion("failover_policy is null");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyIsNotNull() {
            addCriterion("failover_policy is not null");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyEqualTo(String value) {
            addCriterion("failover_policy =", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotEqualTo(String value) {
            addCriterion("failover_policy <>", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyGreaterThan(String value) {
            addCriterion("failover_policy >", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyGreaterThanOrEqualTo(String value) {
            addCriterion("failover_policy >=", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyLessThan(String value) {
            addCriterion("failover_policy <", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyLessThanOrEqualTo(String value) {
            addCriterion("failover_policy <=", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyLike(String value) {
            addCriterion("failover_policy like", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotLike(String value) {
            addCriterion("failover_policy not like", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyIn(List<String> values) {
            addCriterion("failover_policy in", values, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotIn(List<String> values) {
            addCriterion("failover_policy not in", values, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyBetween(String value1, String value2) {
            addCriterion("failover_policy between", value1, value2, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotBetween(String value1, String value2) {
            addCriterion("failover_policy not between", value1, value2, "failoverPolicy");
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

        public Criteria andDeployStrategyIsNull() {
            addCriterion("deploy_strategy is null");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyIsNotNull() {
            addCriterion("deploy_strategy is not null");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyEqualTo(Integer value) {
            addCriterion("deploy_strategy =", value, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyNotEqualTo(Integer value) {
            addCriterion("deploy_strategy <>", value, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyGreaterThan(Integer value) {
            addCriterion("deploy_strategy >", value, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyGreaterThanOrEqualTo(Integer value) {
            addCriterion("deploy_strategy >=", value, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyLessThan(Integer value) {
            addCriterion("deploy_strategy <", value, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyLessThanOrEqualTo(Integer value) {
            addCriterion("deploy_strategy <=", value, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyIn(List<Integer> values) {
            addCriterion("deploy_strategy in", values, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyNotIn(List<Integer> values) {
            addCriterion("deploy_strategy not in", values, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyBetween(Integer value1, Integer value2) {
            addCriterion("deploy_strategy between", value1, value2, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andDeployStrategyNotBetween(Integer value1, Integer value2) {
            addCriterion("deploy_strategy not between", value1, value2, "deployStrategy");
            return (Criteria) this;
        }

        public Criteria andCfgsIsNull() {
            addCriterion("cfgs is null");
            return (Criteria) this;
        }

        public Criteria andCfgsIsNotNull() {
            addCriterion("cfgs is not null");
            return (Criteria) this;
        }

        public Criteria andCfgsEqualTo(String value) {
            addCriterion("cfgs =", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsNotEqualTo(String value) {
            addCriterion("cfgs <>", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsGreaterThan(String value) {
            addCriterion("cfgs >", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsGreaterThanOrEqualTo(String value) {
            addCriterion("cfgs >=", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsLessThan(String value) {
            addCriterion("cfgs <", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsLessThanOrEqualTo(String value) {
            addCriterion("cfgs <=", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsLike(String value) {
            addCriterion("cfgs like", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsNotLike(String value) {
            addCriterion("cfgs not like", value, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsIn(List<String> values) {
            addCriterion("cfgs in", values, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsNotIn(List<String> values) {
            addCriterion("cfgs not in", values, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsBetween(String value1, String value2) {
            addCriterion("cfgs between", value1, value2, "cfgs");
            return (Criteria) this;
        }

        public Criteria andCfgsNotBetween(String value1, String value2) {
            addCriterion("cfgs not between", value1, value2, "cfgs");
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