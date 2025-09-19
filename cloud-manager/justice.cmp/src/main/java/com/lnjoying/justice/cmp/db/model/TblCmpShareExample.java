package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpShareExample {
    protected String orderByClause;

    protected boolean distinct;

    protected Integer startRow;

    protected Integer pageSize;

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public TblCmpShareExample() {
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

        public Criteria andCloudIdIsNull() {
            addCriterion("cloud_id is null");
            return (Criteria) this;
        }

        public Criteria andCloudIdIsNotNull() {
            addCriterion("cloud_id is not null");
            return (Criteria) this;
        }

        public Criteria andCloudIdEqualTo(String value) {
            addCriterion("cloud_id =", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotEqualTo(String value) {
            addCriterion("cloud_id <>", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdGreaterThan(String value) {
            addCriterion("cloud_id >", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdGreaterThanOrEqualTo(String value) {
            addCriterion("cloud_id >=", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdLessThan(String value) {
            addCriterion("cloud_id <", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdLessThanOrEqualTo(String value) {
            addCriterion("cloud_id <=", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdLike(String value) {
            addCriterion("cloud_id like", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotLike(String value) {
            addCriterion("cloud_id not like", value, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdIn(List<String> values) {
            addCriterion("cloud_id in", values, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotIn(List<String> values) {
            addCriterion("cloud_id not in", values, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdBetween(String value1, String value2) {
            addCriterion("cloud_id between", value1, value2, "cloudId");
            return (Criteria) this;
        }

        public Criteria andCloudIdNotBetween(String value1, String value2) {
            addCriterion("cloud_id not between", value1, value2, "cloudId");
            return (Criteria) this;
        }

        public Criteria andShareIdIsNull() {
            addCriterion("share_id is null");
            return (Criteria) this;
        }

        public Criteria andShareIdIsNotNull() {
            addCriterion("share_id is not null");
            return (Criteria) this;
        }

        public Criteria andShareIdEqualTo(String value) {
            addCriterion("share_id =", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdNotEqualTo(String value) {
            addCriterion("share_id <>", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdGreaterThan(String value) {
            addCriterion("share_id >", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdGreaterThanOrEqualTo(String value) {
            addCriterion("share_id >=", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdLessThan(String value) {
            addCriterion("share_id <", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdLessThanOrEqualTo(String value) {
            addCriterion("share_id <=", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdLike(String value) {
            addCriterion("share_id like", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdNotLike(String value) {
            addCriterion("share_id not like", value, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdIn(List<String> values) {
            addCriterion("share_id in", values, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdNotIn(List<String> values) {
            addCriterion("share_id not in", values, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdBetween(String value1, String value2) {
            addCriterion("share_id between", value1, value2, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdNotBetween(String value1, String value2) {
            addCriterion("share_id not between", value1, value2, "shareId");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentIsNull() {
            addCriterion("share_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentIsNotNull() {
            addCriterion("share_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentEqualTo(String value) {
            addCriterion("share_id_from_agent =", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotEqualTo(String value) {
            addCriterion("share_id_from_agent <>", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentGreaterThan(String value) {
            addCriterion("share_id_from_agent >", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("share_id_from_agent >=", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentLessThan(String value) {
            addCriterion("share_id_from_agent <", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("share_id_from_agent <=", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentLike(String value) {
            addCriterion("share_id_from_agent like", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotLike(String value) {
            addCriterion("share_id_from_agent not like", value, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentIn(List<String> values) {
            addCriterion("share_id_from_agent in", values, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotIn(List<String> values) {
            addCriterion("share_id_from_agent not in", values, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentBetween(String value1, String value2) {
            addCriterion("share_id_from_agent between", value1, value2, "shareIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andShareIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("share_id_from_agent not between", value1, value2, "shareIdFromAgent");
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

        public Criteria andBaremetalIdIsNull() {
            addCriterion("baremetal_id is null");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdIsNotNull() {
            addCriterion("baremetal_id is not null");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdEqualTo(String value) {
            addCriterion("baremetal_id =", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdNotEqualTo(String value) {
            addCriterion("baremetal_id <>", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdGreaterThan(String value) {
            addCriterion("baremetal_id >", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdGreaterThanOrEqualTo(String value) {
            addCriterion("baremetal_id >=", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdLessThan(String value) {
            addCriterion("baremetal_id <", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdLessThanOrEqualTo(String value) {
            addCriterion("baremetal_id <=", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdLike(String value) {
            addCriterion("baremetal_id like", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdNotLike(String value) {
            addCriterion("baremetal_id not like", value, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdIn(List<String> values) {
            addCriterion("baremetal_id in", values, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdNotIn(List<String> values) {
            addCriterion("baremetal_id not in", values, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdBetween(String value1, String value2) {
            addCriterion("baremetal_id between", value1, value2, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andBaremetalIdNotBetween(String value1, String value2) {
            addCriterion("baremetal_id not between", value1, value2, "baremetalId");
            return (Criteria) this;
        }

        public Criteria andImageIdIsNull() {
            addCriterion("image_id is null");
            return (Criteria) this;
        }

        public Criteria andImageIdIsNotNull() {
            addCriterion("image_id is not null");
            return (Criteria) this;
        }

        public Criteria andImageIdEqualTo(String value) {
            addCriterion("image_id =", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotEqualTo(String value) {
            addCriterion("image_id <>", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdGreaterThan(String value) {
            addCriterion("image_id >", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdGreaterThanOrEqualTo(String value) {
            addCriterion("image_id >=", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdLessThan(String value) {
            addCriterion("image_id <", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdLessThanOrEqualTo(String value) {
            addCriterion("image_id <=", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdLike(String value) {
            addCriterion("image_id like", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotLike(String value) {
            addCriterion("image_id not like", value, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdIn(List<String> values) {
            addCriterion("image_id in", values, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotIn(List<String> values) {
            addCriterion("image_id not in", values, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdBetween(String value1, String value2) {
            addCriterion("image_id between", value1, value2, "imageId");
            return (Criteria) this;
        }

        public Criteria andImageIdNotBetween(String value1, String value2) {
            addCriterion("image_id not between", value1, value2, "imageId");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorIsNull() {
            addCriterion("iscsi_initiator is null");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorIsNotNull() {
            addCriterion("iscsi_initiator is not null");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorEqualTo(String value) {
            addCriterion("iscsi_initiator =", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotEqualTo(String value) {
            addCriterion("iscsi_initiator <>", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorGreaterThan(String value) {
            addCriterion("iscsi_initiator >", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorGreaterThanOrEqualTo(String value) {
            addCriterion("iscsi_initiator >=", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorLessThan(String value) {
            addCriterion("iscsi_initiator <", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorLessThanOrEqualTo(String value) {
            addCriterion("iscsi_initiator <=", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorLike(String value) {
            addCriterion("iscsi_initiator like", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotLike(String value) {
            addCriterion("iscsi_initiator not like", value, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorIn(List<String> values) {
            addCriterion("iscsi_initiator in", values, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotIn(List<String> values) {
            addCriterion("iscsi_initiator not in", values, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorBetween(String value1, String value2) {
            addCriterion("iscsi_initiator between", value1, value2, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiInitiatorNotBetween(String value1, String value2) {
            addCriterion("iscsi_initiator not between", value1, value2, "iscsiInitiator");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetIsNull() {
            addCriterion("iscsi_target is null");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetIsNotNull() {
            addCriterion("iscsi_target is not null");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetEqualTo(String value) {
            addCriterion("iscsi_target =", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotEqualTo(String value) {
            addCriterion("iscsi_target <>", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetGreaterThan(String value) {
            addCriterion("iscsi_target >", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetGreaterThanOrEqualTo(String value) {
            addCriterion("iscsi_target >=", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetLessThan(String value) {
            addCriterion("iscsi_target <", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetLessThanOrEqualTo(String value) {
            addCriterion("iscsi_target <=", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetLike(String value) {
            addCriterion("iscsi_target like", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotLike(String value) {
            addCriterion("iscsi_target not like", value, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetIn(List<String> values) {
            addCriterion("iscsi_target in", values, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotIn(List<String> values) {
            addCriterion("iscsi_target not in", values, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetBetween(String value1, String value2) {
            addCriterion("iscsi_target between", value1, value2, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiTargetNotBetween(String value1, String value2) {
            addCriterion("iscsi_target not between", value1, value2, "iscsiTarget");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportIsNull() {
            addCriterion("iscsi_ipport is null");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportIsNotNull() {
            addCriterion("iscsi_ipport is not null");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportEqualTo(String value) {
            addCriterion("iscsi_ipport =", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotEqualTo(String value) {
            addCriterion("iscsi_ipport <>", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportGreaterThan(String value) {
            addCriterion("iscsi_ipport >", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportGreaterThanOrEqualTo(String value) {
            addCriterion("iscsi_ipport >=", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportLessThan(String value) {
            addCriterion("iscsi_ipport <", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportLessThanOrEqualTo(String value) {
            addCriterion("iscsi_ipport <=", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportLike(String value) {
            addCriterion("iscsi_ipport like", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotLike(String value) {
            addCriterion("iscsi_ipport not like", value, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportIn(List<String> values) {
            addCriterion("iscsi_ipport in", values, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotIn(List<String> values) {
            addCriterion("iscsi_ipport not in", values, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportBetween(String value1, String value2) {
            addCriterion("iscsi_ipport between", value1, value2, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andIscsiIpportNotBetween(String value1, String value2) {
            addCriterion("iscsi_ipport not between", value1, value2, "iscsiIpport");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIsNull() {
            addCriterion("phase_status is null");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIsNotNull() {
            addCriterion("phase_status is not null");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusEqualTo(Short value) {
            addCriterion("phase_status =", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotEqualTo(Short value) {
            addCriterion("phase_status <>", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusGreaterThan(Short value) {
            addCriterion("phase_status >", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("phase_status >=", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusLessThan(Short value) {
            addCriterion("phase_status <", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusLessThanOrEqualTo(Short value) {
            addCriterion("phase_status <=", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIn(List<Short> values) {
            addCriterion("phase_status in", values, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotIn(List<Short> values) {
            addCriterion("phase_status not in", values, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusBetween(Short value1, Short value2) {
            addCriterion("phase_status between", value1, value2, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotBetween(Short value1, Short value2) {
            addCriterion("phase_status not between", value1, value2, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoIsNull() {
            addCriterion("phase_info is null");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoIsNotNull() {
            addCriterion("phase_info is not null");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoEqualTo(String value) {
            addCriterion("phase_info =", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoNotEqualTo(String value) {
            addCriterion("phase_info <>", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoGreaterThan(String value) {
            addCriterion("phase_info >", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoGreaterThanOrEqualTo(String value) {
            addCriterion("phase_info >=", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoLessThan(String value) {
            addCriterion("phase_info <", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoLessThanOrEqualTo(String value) {
            addCriterion("phase_info <=", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoLike(String value) {
            addCriterion("phase_info like", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoNotLike(String value) {
            addCriterion("phase_info not like", value, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoIn(List<String> values) {
            addCriterion("phase_info in", values, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoNotIn(List<String> values) {
            addCriterion("phase_info not in", values, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoBetween(String value1, String value2) {
            addCriterion("phase_info between", value1, value2, "phaseInfo");
            return (Criteria) this;
        }

        public Criteria andPhaseInfoNotBetween(String value1, String value2) {
            addCriterion("phase_info not between", value1, value2, "phaseInfo");
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

        public Criteria andEeBpIsNull() {
            addCriterion("ee_bp is null");
            return (Criteria) this;
        }

        public Criteria andEeBpIsNotNull() {
            addCriterion("ee_bp is not null");
            return (Criteria) this;
        }

        public Criteria andEeBpEqualTo(String value) {
            addCriterion("ee_bp =", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotEqualTo(String value) {
            addCriterion("ee_bp <>", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpGreaterThan(String value) {
            addCriterion("ee_bp >", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpGreaterThanOrEqualTo(String value) {
            addCriterion("ee_bp >=", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpLessThan(String value) {
            addCriterion("ee_bp <", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpLessThanOrEqualTo(String value) {
            addCriterion("ee_bp <=", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpLike(String value) {
            addCriterion("ee_bp like", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotLike(String value) {
            addCriterion("ee_bp not like", value, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpIn(List<String> values) {
            addCriterion("ee_bp in", values, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotIn(List<String> values) {
            addCriterion("ee_bp not in", values, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpBetween(String value1, String value2) {
            addCriterion("ee_bp between", value1, value2, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeBpNotBetween(String value1, String value2) {
            addCriterion("ee_bp not between", value1, value2, "eeBp");
            return (Criteria) this;
        }

        public Criteria andEeUserIsNull() {
            addCriterion("ee_user is null");
            return (Criteria) this;
        }

        public Criteria andEeUserIsNotNull() {
            addCriterion("ee_user is not null");
            return (Criteria) this;
        }

        public Criteria andEeUserEqualTo(String value) {
            addCriterion("ee_user =", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotEqualTo(String value) {
            addCriterion("ee_user <>", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserGreaterThan(String value) {
            addCriterion("ee_user >", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserGreaterThanOrEqualTo(String value) {
            addCriterion("ee_user >=", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserLessThan(String value) {
            addCriterion("ee_user <", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserLessThanOrEqualTo(String value) {
            addCriterion("ee_user <=", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserLike(String value) {
            addCriterion("ee_user like", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotLike(String value) {
            addCriterion("ee_user not like", value, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserIn(List<String> values) {
            addCriterion("ee_user in", values, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotIn(List<String> values) {
            addCriterion("ee_user not in", values, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserBetween(String value1, String value2) {
            addCriterion("ee_user between", value1, value2, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeUserNotBetween(String value1, String value2) {
            addCriterion("ee_user not between", value1, value2, "eeUser");
            return (Criteria) this;
        }

        public Criteria andEeStatusIsNull() {
            addCriterion("ee_status is null");
            return (Criteria) this;
        }

        public Criteria andEeStatusIsNotNull() {
            addCriterion("ee_status is not null");
            return (Criteria) this;
        }

        public Criteria andEeStatusEqualTo(Integer value) {
            addCriterion("ee_status =", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusNotEqualTo(Integer value) {
            addCriterion("ee_status <>", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusGreaterThan(Integer value) {
            addCriterion("ee_status >", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("ee_status >=", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusLessThan(Integer value) {
            addCriterion("ee_status <", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("ee_status <=", value, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusIn(List<Integer> values) {
            addCriterion("ee_status in", values, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusNotIn(List<Integer> values) {
            addCriterion("ee_status not in", values, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusBetween(Integer value1, Integer value2) {
            addCriterion("ee_status between", value1, value2, "eeStatus");
            return (Criteria) this;
        }

        public Criteria andEeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("ee_status not between", value1, value2, "eeStatus");
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