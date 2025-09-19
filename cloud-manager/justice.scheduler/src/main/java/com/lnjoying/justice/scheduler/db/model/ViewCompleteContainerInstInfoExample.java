package com.lnjoying.justice.scheduler.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewCompleteContainerInstInfoExample {
    protected String orderByClause;

    protected boolean distinct;

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

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public ViewCompleteContainerInstInfoExample() {
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

        public Criteria andInstIdIsNull() {
            addCriterion("ci.inst_id is null");
            return (Criteria) this;
        }

        public Criteria andInstIdIsNotNull() {
            addCriterion("ci.inst_id is not null");
            return (Criteria) this;
        }

        public Criteria andInstIdEqualTo(String value) {
            addCriterion("ci.inst_id =", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdNotEqualTo(String value) {
            addCriterion("ci.inst_id <>", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdGreaterThan(String value) {
            addCriterion("ci.inst_id >", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdGreaterThanOrEqualTo(String value) {
            addCriterion("ci.inst_id >=", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdLessThan(String value) {
            addCriterion("ci.inst_id <", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdLessThanOrEqualTo(String value) {
            addCriterion("ci.inst_id <=", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdLike(String value) {
            addCriterion("ci.inst_id like", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdNotLike(String value) {
            addCriterion("ci.inst_id not like", value, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdIn(List<String> values) {
            addCriterion("ci.inst_id in", values, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdNotIn(List<String> values) {
            addCriterion("ci.inst_id not in", values, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdBetween(String value1, String value2) {
            addCriterion("ci.inst_id between", value1, value2, "instId");
            return (Criteria) this;
        }

        public Criteria andInstIdNotBetween(String value1, String value2) {
            addCriterion("ci.inst_id not between", value1, value2, "instId");
            return (Criteria) this;
        }

        public Criteria andRefIdIsNull() {
            addCriterion("ci.ref_id is null");
            return (Criteria) this;
        }

        public Criteria andRefIdIsNotNull() {
            addCriterion("ci.ref_id is not null");
            return (Criteria) this;
        }

        public Criteria andRefIdEqualTo(String value) {
            addCriterion("ci.ref_id =", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotEqualTo(String value) {
            addCriterion("ci.ref_id <>", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdGreaterThan(String value) {
            addCriterion("ci.ref_id >", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdGreaterThanOrEqualTo(String value) {
            addCriterion("ci.ref_id >=", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdLessThan(String value) {
            addCriterion("ci.ref_id <", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdLessThanOrEqualTo(String value) {
            addCriterion("ci.ref_id <=", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdLike(String value) {
            addCriterion("ci.ref_id like", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotLike(String value) {
            addCriterion("ci.ref_id not like", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdIn(List<String> values) {
            addCriterion("ci.ref_id in", values, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotIn(List<String> values) {
            addCriterion("ci.ref_id not in", values, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdBetween(String value1, String value2) {
            addCriterion("ci.ref_id between", value1, value2, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotBetween(String value1, String value2) {
            addCriterion("ci.ref_id not between", value1, value2, "refId");
            return (Criteria) this;
        }

        public Criteria andSpecIdIsNull() {
            addCriterion("ci.spec_id is null");
            return (Criteria) this;
        }

        public Criteria andSpecIdIsNotNull() {
            addCriterion("ci.spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpecIdEqualTo(String value) {
            addCriterion("ci.spec_id =", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotEqualTo(String value) {
            addCriterion("ci.spec_id <>", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdGreaterThan(String value) {
            addCriterion("ci.spec_id >", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdGreaterThanOrEqualTo(String value) {
            addCriterion("ci.spec_id >=", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLessThan(String value) {
            addCriterion("ci.spec_id <", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLessThanOrEqualTo(String value) {
            addCriterion("ci.spec_id <=", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLike(String value) {
            addCriterion("ci.spec_id like", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotLike(String value) {
            addCriterion("ci.spec_id not like", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdIn(List<String> values) {
            addCriterion("ci.spec_id in", values, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotIn(List<String> values) {
            addCriterion("ci.spec_id not in", values, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdBetween(String value1, String value2) {
            addCriterion("ci.spec_id between", value1, value2, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotBetween(String value1, String value2) {
            addCriterion("ci.spec_id not between", value1, value2, "specId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("cs.user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("cs.user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(String value) {
            addCriterion("cs.user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(String value) {
            addCriterion("cs.user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(String value) {
            addCriterion("cs.user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("cs.user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(String value) {
            addCriterion("cs.user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(String value) {
            addCriterion("cs.user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLike(String value) {
            addCriterion("cs.user_id like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotLike(String value) {
            addCriterion("cs.user_id not like", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<String> values) {
            addCriterion("cs.user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<String> values) {
            addCriterion("cs.user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(String value1, String value2) {
            addCriterion("cs.user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(String value1, String value2) {
            addCriterion("cs.user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andBpIdIsNull() {
            addCriterion("cs.bp_id is null");
            return (Criteria) this;
        }

        public Criteria andBpIdIsNotNull() {
            addCriterion("cs.bp_id is not null");
            return (Criteria) this;
        }

        public Criteria andBpIdEqualTo(String value) {
            addCriterion("cs.bp_id =", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotEqualTo(String value) {
            addCriterion("cs.bp_id <>", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdGreaterThan(String value) {
            addCriterion("cs.bp_id >", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdGreaterThanOrEqualTo(String value) {
            addCriterion("cs.bp_id >=", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLessThan(String value) {
            addCriterion("cs.bp_id <", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLessThanOrEqualTo(String value) {
            addCriterion("cs.bp_id <=", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLike(String value) {
            addCriterion("cs.bp_id like", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotLike(String value) {
            addCriterion("cs.bp_id not like", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdIn(List<String> values) {
            addCriterion("cs.bp_id in", values, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotIn(List<String> values) {
            addCriterion("cs.bp_id not in", values, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdBetween(String value1, String value2) {
            addCriterion("cs.bp_id between", value1, value2, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotBetween(String value1, String value2) {
            addCriterion("cs.bp_id not between", value1, value2, "bpId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNull() {
            addCriterion("ci.node_id is null");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNotNull() {
            addCriterion("ci.node_id is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIdEqualTo(String value) {
            addCriterion("ci.node_id =", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotEqualTo(String value) {
            addCriterion("ci.node_id <>", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThan(String value) {
            addCriterion("ci.node_id >", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("ci.node_id >=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThan(String value) {
            addCriterion("ci.node_id <", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThanOrEqualTo(String value) {
            addCriterion("ci.node_id <=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLike(String value) {
            addCriterion("ci.node_id like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotLike(String value) {
            addCriterion("ci.node_id not like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIn(List<String> values) {
            addCriterion("ci.node_id in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotIn(List<String> values) {
            addCriterion("ci.node_id not in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdBetween(String value1, String value2) {
            addCriterion("ci.node_id between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotBetween(String value1, String value2) {
            addCriterion("ci.node_id not between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andRegionIdIsNull() {
            addCriterion("ci.region_id is null");
            return (Criteria) this;
        }

        public Criteria andRegionIdIsNotNull() {
            addCriterion("ci.region_id is not null");
            return (Criteria) this;
        }

        public Criteria andRegionIdEqualTo(String value) {
            addCriterion("ci.region_id =", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotEqualTo(String value) {
            addCriterion("ci.region_id <>", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdGreaterThan(String value) {
            addCriterion("ci.region_id >", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdGreaterThanOrEqualTo(String value) {
            addCriterion("ci.region_id >=", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLessThan(String value) {
            addCriterion("ci.region_id <", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLessThanOrEqualTo(String value) {
            addCriterion("ci.region_id <=", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLike(String value) {
            addCriterion("ci.region_id like", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotLike(String value) {
            addCriterion("ci.region_id not like", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdIn(List<String> values) {
            addCriterion("ci.region_id in", values, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotIn(List<String> values) {
            addCriterion("ci.region_id not in", values, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdBetween(String value1, String value2) {
            addCriterion("ci.region_id between", value1, value2, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotBetween(String value1, String value2) {
            addCriterion("ci.region_id not between", value1, value2, "regionId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNull() {
            addCriterion("ci.site_id is null");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNotNull() {
            addCriterion("ci.site_id is not null");
            return (Criteria) this;
        }

        public Criteria andSiteIdEqualTo(String value) {
            addCriterion("ci.site_id =", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotEqualTo(String value) {
            addCriterion("ci.site_id <>", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThan(String value) {
            addCriterion("ci.site_id >", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThanOrEqualTo(String value) {
            addCriterion("ci.site_id >=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThan(String value) {
            addCriterion("ci.site_id <", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThanOrEqualTo(String value) {
            addCriterion("ci.site_id <=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLike(String value) {
            addCriterion("ci.site_id like", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotLike(String value) {
            addCriterion("ci.site_id not like", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIn(List<String> values) {
            addCriterion("ci.site_id in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotIn(List<String> values) {
            addCriterion("ci.site_id not in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdBetween(String value1, String value2) {
            addCriterion("ci.site_id between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotBetween(String value1, String value2) {
            addCriterion("ci.site_id not between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andContainerNameIsNull() {
            addCriterion("ci.container_name is null");
            return (Criteria) this;
        }

        public Criteria andContainerNameIsNotNull() {
            addCriterion("ci.container_name is not null");
            return (Criteria) this;
        }

        public Criteria andContainerNameEqualTo(String value) {
            addCriterion("ci.container_name =", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotEqualTo(String value) {
            addCriterion("ci.container_name <>", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameGreaterThan(String value) {
            addCriterion("ci.container_name >", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameGreaterThanOrEqualTo(String value) {
            addCriterion("ci.container_name >=", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameLessThan(String value) {
            addCriterion("ci.container_name <", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameLessThanOrEqualTo(String value) {
            addCriterion("ci.container_name <=", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameLike(String value) {
            addCriterion("ci.container_name like", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotLike(String value) {
            addCriterion("ci.container_name not like", value, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameIn(List<String> values) {
            addCriterion("ci.container_name in", values, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotIn(List<String> values) {
            addCriterion("ci.container_name not in", values, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameBetween(String value1, String value2) {
            addCriterion("ci.container_name between", value1, value2, "containerName");
            return (Criteria) this;
        }

        public Criteria andContainerNameNotBetween(String value1, String value2) {
            addCriterion("ci.container_name not between", value1, value2, "containerName");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("ci.status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("ci.status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("ci.status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("ci.status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("ci.status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("ci.status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("ci.status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("ci.status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("ci.status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("ci.status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("ci.status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("ci.status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andContainerParamsIsNull() {
            addCriterion("cs.container_params is null");
            return (Criteria) this;
        }

        public Criteria andContainerParamsIsNotNull() {
            addCriterion("cs.container_params is not null");
            return (Criteria) this;
        }

        public Criteria andContainerParamsEqualTo(String value) {
            addCriterion("cs.container_params =", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotEqualTo(String value) {
            addCriterion("cs.container_params <>", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsGreaterThan(String value) {
            addCriterion("cs.container_params >", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsGreaterThanOrEqualTo(String value) {
            addCriterion("cs.container_params >=", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsLessThan(String value) {
            addCriterion("cs.container_params <", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsLessThanOrEqualTo(String value) {
            addCriterion("cs.container_params <=", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsLike(String value) {
            addCriterion("cs.container_params like", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotLike(String value) {
            addCriterion("cs.container_params not like", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsIn(List<String> values) {
            addCriterion("cs.container_params in", values, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotIn(List<String> values) {
            addCriterion("cs.container_params not in", values, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsBetween(String value1, String value2) {
            addCriterion("cs.container_params between", value1, value2, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotBetween(String value1, String value2) {
            addCriterion("cs.container_params not between", value1, value2, "containerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsIsNull() {
            addCriterion("ci.inspet_container_params is null");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsIsNotNull() {
            addCriterion("ci.inspet_container_params is not null");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsEqualTo(String value) {
            addCriterion("ci.inspet_container_params =", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsNotEqualTo(String value) {
            addCriterion("ci.inspet_container_params <>", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsGreaterThan(String value) {
            addCriterion("ci.inspet_container_params >", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsGreaterThanOrEqualTo(String value) {
            addCriterion("ci.inspet_container_params >=", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsLessThan(String value) {
            addCriterion("ci.inspet_container_params <", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsLessThanOrEqualTo(String value) {
            addCriterion("ci.inspet_container_params <=", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsLike(String value) {
            addCriterion("ci.inspet_container_params like", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsNotLike(String value) {
            addCriterion("ci.inspet_container_params not like", value, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsIn(List<String> values) {
            addCriterion("ci.inspet_container_params in", values, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsNotIn(List<String> values) {
            addCriterion("ci.inspet_container_params not in", values, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsBetween(String value1, String value2) {
            addCriterion("ci.inspet_container_params between", value1, value2, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andInspetContainerParamsNotBetween(String value1, String value2) {
            addCriterion("ci.inspet_container_params not between", value1, value2, "inspetContainerParams");
            return (Criteria) this;
        }

        public Criteria andImageNameIsNull() {
            addCriterion("cs.image_name is null");
            return (Criteria) this;
        }

        public Criteria andImageNameIsNotNull() {
            addCriterion("cs.image_name is not null");
            return (Criteria) this;
        }

        public Criteria andImageNameEqualTo(String value) {
            addCriterion("cs.image_name =", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotEqualTo(String value) {
            addCriterion("cs.image_name <>", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameGreaterThan(String value) {
            addCriterion("cs.image_name >", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameGreaterThanOrEqualTo(String value) {
            addCriterion("cs.image_name >=", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLessThan(String value) {
            addCriterion("cs.image_name <", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLessThanOrEqualTo(String value) {
            addCriterion("cs.image_name <=", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLike(String value) {
            addCriterion("cs.image_name like", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotLike(String value) {
            addCriterion("cs.image_name not like", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameIn(List<String> values) {
            addCriterion("cs.image_name in", values, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotIn(List<String> values) {
            addCriterion("cs.image_name not in", values, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameBetween(String value1, String value2) {
            addCriterion("cs.image_name between", value1, value2, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotBetween(String value1, String value2) {
            addCriterion("cs.image_name not between", value1, value2, "imageName");
            return (Criteria) this;
        }

        public Criteria andCmdIsNull() {
            addCriterion("cs.cmd is null");
            return (Criteria) this;
        }

        public Criteria andCmdIsNotNull() {
            addCriterion("cs.cmd is not null");
            return (Criteria) this;
        }

        public Criteria andCmdEqualTo(String value) {
            addCriterion("cs.cmd =", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotEqualTo(String value) {
            addCriterion("cs.cmd <>", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdGreaterThan(String value) {
            addCriterion("cs.cmd >", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdGreaterThanOrEqualTo(String value) {
            addCriterion("cs.cmd >=", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdLessThan(String value) {
            addCriterion("cs.cmd <", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdLessThanOrEqualTo(String value) {
            addCriterion("cs.cmd <=", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdLike(String value) {
            addCriterion("cs.cmd like", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotLike(String value) {
            addCriterion("cs.cmd not like", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdIn(List<String> values) {
            addCriterion("cs.cmd in", values, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotIn(List<String> values) {
            addCriterion("cs.cmd not in", values, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdBetween(String value1, String value2) {
            addCriterion("cs.cmd between", value1, value2, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotBetween(String value1, String value2) {
            addCriterion("cs.cmd not between", value1, value2, "cmd");
            return (Criteria) this;
        }

        public Criteria andIpIsNull() {
            addCriterion("ci.ip is null");
            return (Criteria) this;
        }

        public Criteria andIpIsNotNull() {
            addCriterion("ci.ip is not null");
            return (Criteria) this;
        }

        public Criteria andIpEqualTo(String value) {
            addCriterion("ci.ip =", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotEqualTo(String value) {
            addCriterion("ci.ip <>", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThan(String value) {
            addCriterion("ci.ip >", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpGreaterThanOrEqualTo(String value) {
            addCriterion("ci.ip >=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThan(String value) {
            addCriterion("ci.ip <", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLessThanOrEqualTo(String value) {
            addCriterion("ci.ip <=", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpLike(String value) {
            addCriterion("ci.ip like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotLike(String value) {
            addCriterion("ci.ip not like", value, "ip");
            return (Criteria) this;
        }

        public Criteria andIpIn(List<String> values) {
            addCriterion("ci.ip in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotIn(List<String> values) {
            addCriterion("ci.ip not in", values, "ip");
            return (Criteria) this;
        }

        public Criteria andIpBetween(String value1, String value2) {
            addCriterion("ci.ip between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andIpNotBetween(String value1, String value2) {
            addCriterion("ci.ip not between", value1, value2, "ip");
            return (Criteria) this;
        }

        public Criteria andCpuNumIsNull() {
            addCriterion("cs.cpu_num is null");
            return (Criteria) this;
        }

        public Criteria andCpuNumIsNotNull() {
            addCriterion("cs.cpu_num is not null");
            return (Criteria) this;
        }

        public Criteria andCpuNumEqualTo(Integer value) {
            addCriterion("cs.cpu_num =", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotEqualTo(Integer value) {
            addCriterion("cs.cpu_num <>", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumGreaterThan(Integer value) {
            addCriterion("cs.cpu_num >", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("cs.cpu_num >=", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumLessThan(Integer value) {
            addCriterion("cs.cpu_num <", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumLessThanOrEqualTo(Integer value) {
            addCriterion("cs.cpu_num <=", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumIn(List<Integer> values) {
            addCriterion("cs.cpu_num in", values, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotIn(List<Integer> values) {
            addCriterion("cs.cpu_num not in", values, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumBetween(Integer value1, Integer value2) {
            addCriterion("cs.cpu_num between", value1, value2, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotBetween(Integer value1, Integer value2) {
            addCriterion("cs.cpu_num not between", value1, value2, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andMemLimitIsNull() {
            addCriterion("cs.mem_limit is null");
            return (Criteria) this;
        }

        public Criteria andMemLimitIsNotNull() {
            addCriterion("cs.mem_limit is not null");
            return (Criteria) this;
        }

        public Criteria andMemLimitEqualTo(Long value) {
            addCriterion("cs.mem_limit =", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitNotEqualTo(Long value) {
            addCriterion("cs.mem_limit <>", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitGreaterThan(Long value) {
            addCriterion("cs.mem_limit >", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("cs.mem_limit >=", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitLessThan(Long value) {
            addCriterion("cs.mem_limit <", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitLessThanOrEqualTo(Long value) {
            addCriterion("cs.mem_limit <=", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitIn(List<Long> values) {
            addCriterion("cs.mem_limit in", values, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitNotIn(List<Long> values) {
            addCriterion("cs.mem_limit not in", values, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitBetween(Long value1, Long value2) {
            addCriterion("cs.mem_limit between", value1, value2, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitNotBetween(Long value1, Long value2) {
            addCriterion("cs.mem_limit not between", value1, value2, "memLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitIsNull() {
            addCriterion("cs.disk_limit is null");
            return (Criteria) this;
        }

        public Criteria andDiskLimitIsNotNull() {
            addCriterion("cs.disk_limit is not null");
            return (Criteria) this;
        }

        public Criteria andDiskLimitEqualTo(Long value) {
            addCriterion("cs.disk_limit =", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitNotEqualTo(Long value) {
            addCriterion("cs.disk_limit <>", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitGreaterThan(Long value) {
            addCriterion("cs.disk_limit >", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("cs.disk_limit >=", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitLessThan(Long value) {
            addCriterion("cs.disk_limit <", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitLessThanOrEqualTo(Long value) {
            addCriterion("cs.disk_limit <=", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitIn(List<Long> values) {
            addCriterion("cs.disk_limit in", values, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitNotIn(List<Long> values) {
            addCriterion("cs.disk_limit not in", values, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitBetween(Long value1, Long value2) {
            addCriterion("cs.disk_limit between", value1, value2, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitNotBetween(Long value1, Long value2) {
            addCriterion("cs.disk_limit not between", value1, value2, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitIsNull() {
            addCriterion("cs.transmit_band_limit is null");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitIsNotNull() {
            addCriterion("cs.transmit_band_limit is not null");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitEqualTo(Integer value) {
            addCriterion("cs.transmit_band_limit =", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitNotEqualTo(Integer value) {
            addCriterion("cs.transmit_band_limit <>", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitGreaterThan(Integer value) {
            addCriterion("cs.transmit_band_limit >", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("cs.transmit_band_limit >=", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitLessThan(Integer value) {
            addCriterion("cs.transmit_band_limit <", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitLessThanOrEqualTo(Integer value) {
            addCriterion("cs.transmit_band_limit <=", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitIn(List<Integer> values) {
            addCriterion("cs.transmit_band_limit in", values, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitNotIn(List<Integer> values) {
            addCriterion("cs.transmit_band_limit not in", values, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitBetween(Integer value1, Integer value2) {
            addCriterion("cs.transmit_band_limit between", value1, value2, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("cs.transmit_band_limit not between", value1, value2, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitIsNull() {
            addCriterion("cs.recv_band_limit is null");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitIsNotNull() {
            addCriterion("cs.recv_band_limit is not null");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitEqualTo(Integer value) {
            addCriterion("cs.recv_band_limit =", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitNotEqualTo(Integer value) {
            addCriterion("cs.recv_band_limit <>", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitGreaterThan(Integer value) {
            addCriterion("cs.recv_band_limit >", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("cs.recv_band_limit >=", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitLessThan(Integer value) {
            addCriterion("cs.recv_band_limit <", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitLessThanOrEqualTo(Integer value) {
            addCriterion("cs.recv_band_limit <=", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitIn(List<Integer> values) {
            addCriterion("cs.recv_band_limit in", values, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitNotIn(List<Integer> values) {
            addCriterion("cs.recv_band_limit not in", values, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitBetween(Integer value1, Integer value2) {
            addCriterion("cs.recv_band_limit between", value1, value2, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("cs.recv_band_limit not between", value1, value2, "recvBandLimit");
            return (Criteria) this;
        }

        //
        public Criteria andGpuNumIsNull() {
            addCriterion("cs.gpu_num is null");
            return (Criteria) this;
        }

        public Criteria andGpuNumIsNotNull() {
            addCriterion("cs.gpu_num is not null");
            return (Criteria) this;
        }

        public Criteria andGpuNumEqualTo(Integer value) {
            addCriterion("cs.gpu_num =", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotEqualTo(Integer value) {
            addCriterion("cs.gpu_num <>", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumGreaterThan(Integer value) {
            addCriterion("cs.gpu_num >", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("cs.gpu_num >=", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumLessThan(Integer value) {
            addCriterion("cs.gpu_num <", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumLessThanOrEqualTo(Integer value) {
            addCriterion("cs.gpu_num <=", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumIn(List<Integer> values) {
            addCriterion("cs.gpu_num in", values, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotIn(List<Integer> values) {
            addCriterion("cs.gpu_num not in", values, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumBetween(Integer value1, Integer value2) {
            addCriterion("cs.gpu_num between", value1, value2, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotBetween(Integer value1, Integer value2) {
            addCriterion("cs.gpu_num not between", value1, value2, "gpuNum");
            return (Criteria) this;
        }
        //
        public Criteria andDevNeedInfoIsNull() {
            addCriterion("cs.dev_need_info is null");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIsNotNull() {
            addCriterion("cs.dev_need_info is not null");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoEqualTo(String value) {
            addCriterion("cs.dev_need_info =", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotEqualTo(String value) {
            addCriterion("cs.dev_need_info <>", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoGreaterThan(String value) {
            addCriterion("cs.dev_need_info >", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoGreaterThanOrEqualTo(String value) {
            addCriterion("cs.dev_need_info >=", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLessThan(String value) {
            addCriterion("cs.dev_need_info <", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLessThanOrEqualTo(String value) {
            addCriterion("cs.dev_need_info <=", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLike(String value) {
            addCriterion("cs.dev_need_info like", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotLike(String value) {
            addCriterion("cs.dev_need_info not like", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIn(List<String> values) {
            addCriterion("cs.dev_need_info in", values, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotIn(List<String> values) {
            addCriterion("cs.dev_need_info not in", values, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoBetween(String value1, String value2) {
            addCriterion("cs.dev_need_info between", value1, value2, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotBetween(String value1, String value2) {
            addCriterion("cs.dev_need_info not between", value1, value2, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andAutoRunIsNull() {
            addCriterion("cs.auto_run is null");
            return (Criteria) this;
        }

        public Criteria andAutoRunIsNotNull() {
            addCriterion("cs.auto_run is not null");
            return (Criteria) this;
        }

        public Criteria andAutoRunEqualTo(Boolean value) {
            addCriterion("cs.auto_run =", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotEqualTo(Boolean value) {
            addCriterion("cs.auto_run <>", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunGreaterThan(Boolean value) {
            addCriterion("cs.auto_run >", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunGreaterThanOrEqualTo(Boolean value) {
            addCriterion("cs.auto_run >=", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunLessThan(Boolean value) {
            addCriterion("cs.auto_run <", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunLessThanOrEqualTo(Boolean value) {
            addCriterion("cs.auto_run <=", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunIn(List<Boolean> values) {
            addCriterion("cs.auto_run in", values, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotIn(List<Boolean> values) {
            addCriterion("cs.auto_run not in", values, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunBetween(Boolean value1, Boolean value2) {
            addCriterion("cs.auto_run between", value1, value2, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotBetween(Boolean value1, Boolean value2) {
            addCriterion("cs.auto_run not between", value1, value2, "autoRun");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIsNull() {
            addCriterion("cs.replica_num is null");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIsNotNull() {
            addCriterion("cs.replica_num is not null");
            return (Criteria) this;
        }

        public Criteria andReplicaNumEqualTo(Integer value) {
            addCriterion("cs.replica_num =", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotEqualTo(Integer value) {
            addCriterion("cs.replica_num <>", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumGreaterThan(Integer value) {
            addCriterion("cs.replica_num >", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("cs.replica_num >=", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumLessThan(Integer value) {
            addCriterion("cs.replica_num <", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumLessThanOrEqualTo(Integer value) {
            addCriterion("cs.replica_num <=", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIn(List<Integer> values) {
            addCriterion("cs.replica_num in", values, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotIn(List<Integer> values) {
            addCriterion("cs.replica_num not in", values, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumBetween(Integer value1, Integer value2) {
            addCriterion("cs.replica_num between", value1, value2, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotBetween(Integer value1, Integer value2) {
            addCriterion("cs.replica_num not between", value1, value2, "replicaNum");
            return (Criteria) this;
        }
        //
        public Criteria andSendCreateNumIsNull() {
            addCriterion("ci.send_create_num is null");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumIsNotNull() {
            addCriterion("ci.send_create_num is not null");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumEqualTo(Integer value) {
            addCriterion("ci.send_create_num =", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumNotEqualTo(Integer value) {
            addCriterion("ci.send_create_num <>", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumGreaterThan(Integer value) {
            addCriterion("ci.send_create_num >", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("ci.send_create_num >=", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumLessThan(Integer value) {
            addCriterion("ci.send_create_num <", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumLessThanOrEqualTo(Integer value) {
            addCriterion("ci.send_create_num <=", value, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumIn(List<Integer> values) {
            addCriterion("ci.send_create_num in", values, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumNotIn(List<Integer> values) {
            addCriterion("ci.send_create_num not in", values, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumBetween(Integer value1, Integer value2) {
            addCriterion("ci.send_create_num between", value1, value2, "sendCreateNum");
            return (Criteria) this;
        }

        public Criteria andSendCreateNumNotBetween(Integer value1, Integer value2) {
            addCriterion("ci.send_create_num not between", value1, value2, "sendCreateNum");
            return (Criteria) this;
        }
        //
        public Criteria andStartNumIsNull() {
            addCriterion("ci.start_num is null");
            return (Criteria) this;
        }

        public Criteria andStartNumIsNotNull() {
            addCriterion("ci.start_num is not null");
            return (Criteria) this;
        }

        public Criteria andStartNumEqualTo(Integer value) {
            addCriterion("ci.start_num =", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumNotEqualTo(Integer value) {
            addCriterion("ci.start_num <>", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumGreaterThan(Integer value) {
            addCriterion("ci.start_num >", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("ci.start_num >=", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumLessThan(Integer value) {
            addCriterion("ci.start_num <", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumLessThanOrEqualTo(Integer value) {
            addCriterion("ci.start_num <=", value, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumIn(List<Integer> values) {
            addCriterion("ci.start_num in", values, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumNotIn(List<Integer> values) {
            addCriterion("ci.start_num not in", values, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumBetween(Integer value1, Integer value2) {
            addCriterion("ci.start_num between", value1, value2, "startNum");
            return (Criteria) this;
        }

        public Criteria andStartNumNotBetween(Integer value1, Integer value2) {
            addCriterion("ci.start_num not between", value1, value2, "startNum");
            return (Criteria) this;
        }
        //
        public Criteria andFailNumIsNull() {
            addCriterion("ci.fail_num is null");
            return (Criteria) this;
        }

        public Criteria andFailNumIsNotNull() {
            addCriterion("ci.fail_num is not null");
            return (Criteria) this;
        }

        public Criteria andFailNumEqualTo(Integer value) {
            addCriterion("ci.fail_num =", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumNotEqualTo(Integer value) {
            addCriterion("ci.fail_num <>", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumGreaterThan(Integer value) {
            addCriterion("ci.fail_num >", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("ci.fail_num >=", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumLessThan(Integer value) {
            addCriterion("ci.fail_num <", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumLessThanOrEqualTo(Integer value) {
            addCriterion("ci.fail_num <=", value, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumIn(List<Integer> values) {
            addCriterion("ci.fail_num in", values, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumNotIn(List<Integer> values) {
            addCriterion("ci.fail_num not in", values, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumBetween(Integer value1, Integer value2) {
            addCriterion("ci.fail_num between", value1, value2, "failNum");
            return (Criteria) this;
        }

        public Criteria andFailNumNotBetween(Integer value1, Integer value2) {
            addCriterion("ci.fail_num not between", value1, value2, "failNum");
            return (Criteria) this;
        }
        //

        public Criteria andCreateTimeIsNull() {
            addCriterion("ci.create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("ci.create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("ci.create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("ci.create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("ci.create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("ci.create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("ci.create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("ci.create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("ci.create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("ci.create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("ci.create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("ci.create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("ci.update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("ci.update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("ci.update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("ci.update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("ci.update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("ci.update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("ci.update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("ci.update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("ci.update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("ci.update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("ci.update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("ci.update_time not between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeIsNull() {
            addCriterion("ci.remove_time is null");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeIsNotNull() {
            addCriterion("ci.remove_time is not null");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeEqualTo(Date value) {
            addCriterion("ci.remove_time =", value, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeNotEqualTo(Date value) {
            addCriterion("ci.remove_time <>", value, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeGreaterThan(Date value) {
            addCriterion("ci.remove_time >", value, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("ci.remove_time >=", value, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeLessThan(Date value) {
            addCriterion("ci.remove_time <", value, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeLessThanOrEqualTo(Date value) {
            addCriterion("ci.remove_time <=", value, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeIn(List<Date> values) {
            addCriterion("ci.remove_time in", values, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeNotIn(List<Date> values) {
            addCriterion("ci.remove_time not in", values, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeBetween(Date value1, Date value2) {
            addCriterion("ci.remove_time between", value1, value2, "removeTime");
            return (Criteria) this;
        }

        public Criteria andRemoveTimeNotBetween(Date value1, Date value2) {
            addCriterion("ci.remove_time not between", value1, value2, "removeTime");
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