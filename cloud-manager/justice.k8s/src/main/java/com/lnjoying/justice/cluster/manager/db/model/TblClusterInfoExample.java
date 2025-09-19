package com.lnjoying.justice.cluster.manager.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblClusterInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblClusterInfoExample() {
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

        public Criteria andTmplVersionIdIsNull() {
            addCriterion("tmpl_version_id is null");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdIsNotNull() {
            addCriterion("tmpl_version_id is not null");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdEqualTo(String value) {
            addCriterion("tmpl_version_id =", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdNotEqualTo(String value) {
            addCriterion("tmpl_version_id <>", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdGreaterThan(String value) {
            addCriterion("tmpl_version_id >", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdGreaterThanOrEqualTo(String value) {
            addCriterion("tmpl_version_id >=", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdLessThan(String value) {
            addCriterion("tmpl_version_id <", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdLessThanOrEqualTo(String value) {
            addCriterion("tmpl_version_id <=", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdLike(String value) {
            addCriterion("tmpl_version_id like", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdNotLike(String value) {
            addCriterion("tmpl_version_id not like", value, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdIn(List<String> values) {
            addCriterion("tmpl_version_id in", values, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdNotIn(List<String> values) {
            addCriterion("tmpl_version_id not in", values, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdBetween(String value1, String value2) {
            addCriterion("tmpl_version_id between", value1, value2, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andTmplVersionIdNotBetween(String value1, String value2) {
            addCriterion("tmpl_version_id not between", value1, value2, "tmplVersionId");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
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

        public Criteria andClusterEngineConfigIsNull() {
            addCriterion("cluster_engine_config is null");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigIsNotNull() {
            addCriterion("cluster_engine_config is not null");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigEqualTo(String value) {
            addCriterion("cluster_engine_config =", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigNotEqualTo(String value) {
            addCriterion("cluster_engine_config <>", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigGreaterThan(String value) {
            addCriterion("cluster_engine_config >", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_engine_config >=", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigLessThan(String value) {
            addCriterion("cluster_engine_config <", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigLessThanOrEqualTo(String value) {
            addCriterion("cluster_engine_config <=", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigLike(String value) {
            addCriterion("cluster_engine_config like", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigNotLike(String value) {
            addCriterion("cluster_engine_config not like", value, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigIn(List<String> values) {
            addCriterion("cluster_engine_config in", values, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigNotIn(List<String> values) {
            addCriterion("cluster_engine_config not in", values, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigBetween(String value1, String value2) {
            addCriterion("cluster_engine_config between", value1, value2, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andClusterEngineConfigNotBetween(String value1, String value2) {
            addCriterion("cluster_engine_config not between", value1, value2, "clusterEngineConfig");
            return (Criteria) this;
        }

        public Criteria andDevNeedIsNull() {
            addCriterion("dev_need is null");
            return (Criteria) this;
        }

        public Criteria andDevNeedIsNotNull() {
            addCriterion("dev_need is not null");
            return (Criteria) this;
        }

        public Criteria andDevNeedEqualTo(String value) {
            addCriterion("dev_need =", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedNotEqualTo(String value) {
            addCriterion("dev_need <>", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedGreaterThan(String value) {
            addCriterion("dev_need >", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedGreaterThanOrEqualTo(String value) {
            addCriterion("dev_need >=", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedLessThan(String value) {
            addCriterion("dev_need <", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedLessThanOrEqualTo(String value) {
            addCriterion("dev_need <=", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedLike(String value) {
            addCriterion("dev_need like", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedNotLike(String value) {
            addCriterion("dev_need not like", value, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedIn(List<String> values) {
            addCriterion("dev_need in", values, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedNotIn(List<String> values) {
            addCriterion("dev_need not in", values, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedBetween(String value1, String value2) {
            addCriterion("dev_need between", value1, value2, "devNeed");
            return (Criteria) this;
        }

        public Criteria andDevNeedNotBetween(String value1, String value2) {
            addCriterion("dev_need not between", value1, value2, "devNeed");
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

        public Criteria andMembersIsNull() {
            addCriterion("members is null");
            return (Criteria) this;
        }

        public Criteria andMembersIsNotNull() {
            addCriterion("members is not null");
            return (Criteria) this;
        }

        public Criteria andMembersEqualTo(String value) {
            addCriterion("members =", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersNotEqualTo(String value) {
            addCriterion("members <>", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersGreaterThan(String value) {
            addCriterion("members >", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersGreaterThanOrEqualTo(String value) {
            addCriterion("members >=", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersLessThan(String value) {
            addCriterion("members <", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersLessThanOrEqualTo(String value) {
            addCriterion("members <=", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersLike(String value) {
            addCriterion("members like", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersNotLike(String value) {
            addCriterion("members not like", value, "members");
            return (Criteria) this;
        }

        public Criteria andMembersIn(List<String> values) {
            addCriterion("members in", values, "members");
            return (Criteria) this;
        }

        public Criteria andMembersNotIn(List<String> values) {
            addCriterion("members not in", values, "members");
            return (Criteria) this;
        }

        public Criteria andMembersBetween(String value1, String value2) {
            addCriterion("members between", value1, value2, "members");
            return (Criteria) this;
        }

        public Criteria andMembersNotBetween(String value1, String value2) {
            addCriterion("members not between", value1, value2, "members");
            return (Criteria) this;
        }

        public Criteria andCreateTypeIsNull() {
            addCriterion("create_type is null");
            return (Criteria) this;
        }

        public Criteria andCreateTypeIsNotNull() {
            addCriterion("create_type is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTypeEqualTo(String value) {
            addCriterion("create_type =", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeNotEqualTo(String value) {
            addCriterion("create_type <>", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeGreaterThan(String value) {
            addCriterion("create_type >", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeGreaterThanOrEqualTo(String value) {
            addCriterion("create_type >=", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeLessThan(String value) {
            addCriterion("create_type <", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeLessThanOrEqualTo(String value) {
            addCriterion("create_type <=", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeLike(String value) {
            addCriterion("create_type like", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeNotLike(String value) {
            addCriterion("create_type not like", value, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeIn(List<String> values) {
            addCriterion("create_type in", values, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeNotIn(List<String> values) {
            addCriterion("create_type not in", values, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeBetween(String value1, String value2) {
            addCriterion("create_type between", value1, value2, "createType");
            return (Criteria) this;
        }

        public Criteria andCreateTypeNotBetween(String value1, String value2) {
            addCriterion("create_type not between", value1, value2, "createType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeIsNull() {
            addCriterion("cluster_type is null");
            return (Criteria) this;
        }

        public Criteria andClusterTypeIsNotNull() {
            addCriterion("cluster_type is not null");
            return (Criteria) this;
        }

        public Criteria andClusterTypeEqualTo(String value) {
            addCriterion("cluster_type =", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotEqualTo(String value) {
            addCriterion("cluster_type <>", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeGreaterThan(String value) {
            addCriterion("cluster_type >", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_type >=", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeLessThan(String value) {
            addCriterion("cluster_type <", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeLessThanOrEqualTo(String value) {
            addCriterion("cluster_type <=", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeLike(String value) {
            addCriterion("cluster_type like", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotLike(String value) {
            addCriterion("cluster_type not like", value, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeIn(List<String> values) {
            addCriterion("cluster_type in", values, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotIn(List<String> values) {
            addCriterion("cluster_type not in", values, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeBetween(String value1, String value2) {
            addCriterion("cluster_type between", value1, value2, "clusterType");
            return (Criteria) this;
        }

        public Criteria andClusterTypeNotBetween(String value1, String value2) {
            addCriterion("cluster_type not between", value1, value2, "clusterType");
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

        public Criteria andAnnotationsIsNull() {
            addCriterion("annotations is null");
            return (Criteria) this;
        }

        public Criteria andAnnotationsIsNotNull() {
            addCriterion("annotations is not null");
            return (Criteria) this;
        }

        public Criteria andAnnotationsEqualTo(String value) {
            addCriterion("annotations =", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotEqualTo(String value) {
            addCriterion("annotations <>", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsGreaterThan(String value) {
            addCriterion("annotations >", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsGreaterThanOrEqualTo(String value) {
            addCriterion("annotations >=", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsLessThan(String value) {
            addCriterion("annotations <", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsLessThanOrEqualTo(String value) {
            addCriterion("annotations <=", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsLike(String value) {
            addCriterion("annotations like", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotLike(String value) {
            addCriterion("annotations not like", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsIn(List<String> values) {
            addCriterion("annotations in", values, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotIn(List<String> values) {
            addCriterion("annotations not in", values, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsBetween(String value1, String value2) {
            addCriterion("annotations between", value1, value2, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotBetween(String value1, String value2) {
            addCriterion("annotations not between", value1, value2, "annotations");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNull() {
            addCriterion("owner is null");
            return (Criteria) this;
        }

        public Criteria andOwnerIsNotNull() {
            addCriterion("owner is not null");
            return (Criteria) this;
        }

        public Criteria andOwnerEqualTo(String value) {
            addCriterion("owner =", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotEqualTo(String value) {
            addCriterion("owner <>", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThan(String value) {
            addCriterion("owner >", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerGreaterThanOrEqualTo(String value) {
            addCriterion("owner >=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThan(String value) {
            addCriterion("owner <", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLessThanOrEqualTo(String value) {
            addCriterion("owner <=", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerLike(String value) {
            addCriterion("owner like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotLike(String value) {
            addCriterion("owner not like", value, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerIn(List<String> values) {
            addCriterion("owner in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotIn(List<String> values) {
            addCriterion("owner not in", values, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerBetween(String value1, String value2) {
            addCriterion("owner between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andOwnerNotBetween(String value1, String value2) {
            addCriterion("owner not between", value1, value2, "owner");
            return (Criteria) this;
        }

        public Criteria andBpIsNull() {
            addCriterion("bp is null");
            return (Criteria) this;
        }

        public Criteria andBpIsNotNull() {
            addCriterion("bp is not null");
            return (Criteria) this;
        }

        public Criteria andBpEqualTo(String value) {
            addCriterion("bp =", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpNotEqualTo(String value) {
            addCriterion("bp <>", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpGreaterThan(String value) {
            addCriterion("bp >", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpGreaterThanOrEqualTo(String value) {
            addCriterion("bp >=", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpLessThan(String value) {
            addCriterion("bp <", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpLessThanOrEqualTo(String value) {
            addCriterion("bp <=", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpLike(String value) {
            addCriterion("bp like", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpNotLike(String value) {
            addCriterion("bp not like", value, "bp");
            return (Criteria) this;
        }

        public Criteria andBpIn(List<String> values) {
            addCriterion("bp in", values, "bp");
            return (Criteria) this;
        }

        public Criteria andBpNotIn(List<String> values) {
            addCriterion("bp not in", values, "bp");
            return (Criteria) this;
        }

        public Criteria andBpBetween(String value1, String value2) {
            addCriterion("bp between", value1, value2, "bp");
            return (Criteria) this;
        }

        public Criteria andBpNotBetween(String value1, String value2) {
            addCriterion("bp not between", value1, value2, "bp");
            return (Criteria) this;
        }

        public Criteria andTokenIsNull() {
            addCriterion("token is null");
            return (Criteria) this;
        }

        public Criteria andTokenIsNotNull() {
            addCriterion("token is not null");
            return (Criteria) this;
        }

        public Criteria andTokenEqualTo(String value) {
            addCriterion("token =", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotEqualTo(String value) {
            addCriterion("token <>", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThan(String value) {
            addCriterion("token >", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenGreaterThanOrEqualTo(String value) {
            addCriterion("token >=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThan(String value) {
            addCriterion("token <", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLessThanOrEqualTo(String value) {
            addCriterion("token <=", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenLike(String value) {
            addCriterion("token like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotLike(String value) {
            addCriterion("token not like", value, "token");
            return (Criteria) this;
        }

        public Criteria andTokenIn(List<String> values) {
            addCriterion("token in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotIn(List<String> values) {
            addCriterion("token not in", values, "token");
            return (Criteria) this;
        }

        public Criteria andTokenBetween(String value1, String value2) {
            addCriterion("token between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andTokenNotBetween(String value1, String value2) {
            addCriterion("token not between", value1, value2, "token");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNull() {
            addCriterion("creator is null");
            return (Criteria) this;
        }

        public Criteria andCreatorIsNotNull() {
            addCriterion("creator is not null");
            return (Criteria) this;
        }

        public Criteria andCreatorEqualTo(String value) {
            addCriterion("creator =", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotEqualTo(String value) {
            addCriterion("creator <>", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThan(String value) {
            addCriterion("creator >", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorGreaterThanOrEqualTo(String value) {
            addCriterion("creator >=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThan(String value) {
            addCriterion("creator <", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLessThanOrEqualTo(String value) {
            addCriterion("creator <=", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorLike(String value) {
            addCriterion("creator like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotLike(String value) {
            addCriterion("creator not like", value, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorIn(List<String> values) {
            addCriterion("creator in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotIn(List<String> values) {
            addCriterion("creator not in", values, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorBetween(String value1, String value2) {
            addCriterion("creator between", value1, value2, "creator");
            return (Criteria) this;
        }

        public Criteria andCreatorNotBetween(String value1, String value2) {
            addCriterion("creator not between", value1, value2, "creator");
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

        public Criteria andServiceStateIsNull() {
            addCriterion("service_state is null");
            return (Criteria) this;
        }

        public Criteria andServiceStateIsNotNull() {
            addCriterion("service_state is not null");
            return (Criteria) this;
        }

        public Criteria andServiceStateEqualTo(Integer value) {
            addCriterion("service_state =", value, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateNotEqualTo(Integer value) {
            addCriterion("service_state <>", value, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateGreaterThan(Integer value) {
            addCriterion("service_state >", value, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("service_state >=", value, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateLessThan(Integer value) {
            addCriterion("service_state <", value, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateLessThanOrEqualTo(Integer value) {
            addCriterion("service_state <=", value, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateIn(List<Integer> values) {
            addCriterion("service_state in", values, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateNotIn(List<Integer> values) {
            addCriterion("service_state not in", values, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateBetween(Integer value1, Integer value2) {
            addCriterion("service_state between", value1, value2, "serviceState");
            return (Criteria) this;
        }

        public Criteria andServiceStateNotBetween(Integer value1, Integer value2) {
            addCriterion("service_state not between", value1, value2, "serviceState");
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