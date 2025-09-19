package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpImageExample {
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

    public TblCmpImageExample() {
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

        public Criteria andFileIdFromAgentIsNull() {
            addCriterion("file_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentIsNotNull() {
            addCriterion("file_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentEqualTo(String value) {
            addCriterion("file_id_from_agent =", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentNotEqualTo(String value) {
            addCriterion("file_id_from_agent <>", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentGreaterThan(String value) {
            addCriterion("file_id_from_agent >", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("file_id_from_agent >=", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentLessThan(String value) {
            addCriterion("file_id_from_agent <", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("file_id_from_agent <=", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentLike(String value) {
            addCriterion("file_id_from_agent like", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentNotLike(String value) {
            addCriterion("file_id_from_agent not like", value, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentIn(List<String> values) {
            addCriterion("file_id_from_agent in", values, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentNotIn(List<String> values) {
            addCriterion("file_id_from_agent not in", values, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentBetween(String value1, String value2) {
            addCriterion("file_id_from_agent between", value1, value2, "fileIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andFileIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("file_id_from_agent not between", value1, value2, "fileIdFromAgent");
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

        public Criteria andImageOsTypeIsNull() {
            addCriterion("image_os_type is null");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeIsNotNull() {
            addCriterion("image_os_type is not null");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeEqualTo(Short value) {
            addCriterion("image_os_type =", value, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeNotEqualTo(Short value) {
            addCriterion("image_os_type <>", value, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeGreaterThan(Short value) {
            addCriterion("image_os_type >", value, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeGreaterThanOrEqualTo(Short value) {
            addCriterion("image_os_type >=", value, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeLessThan(Short value) {
            addCriterion("image_os_type <", value, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeLessThanOrEqualTo(Short value) {
            addCriterion("image_os_type <=", value, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeIn(List<Short> values) {
            addCriterion("image_os_type in", values, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeNotIn(List<Short> values) {
            addCriterion("image_os_type not in", values, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeBetween(Short value1, Short value2) {
            addCriterion("image_os_type between", value1, value2, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsTypeNotBetween(Short value1, Short value2) {
            addCriterion("image_os_type not between", value1, value2, "imageOsType");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorIsNull() {
            addCriterion("image_os_vendor is null");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorIsNotNull() {
            addCriterion("image_os_vendor is not null");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorEqualTo(Short value) {
            addCriterion("image_os_vendor =", value, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorNotEqualTo(Short value) {
            addCriterion("image_os_vendor <>", value, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorGreaterThan(Short value) {
            addCriterion("image_os_vendor >", value, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorGreaterThanOrEqualTo(Short value) {
            addCriterion("image_os_vendor >=", value, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorLessThan(Short value) {
            addCriterion("image_os_vendor <", value, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorLessThanOrEqualTo(Short value) {
            addCriterion("image_os_vendor <=", value, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorIn(List<Short> values) {
            addCriterion("image_os_vendor in", values, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorNotIn(List<Short> values) {
            addCriterion("image_os_vendor not in", values, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorBetween(Short value1, Short value2) {
            addCriterion("image_os_vendor between", value1, value2, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVendorNotBetween(Short value1, Short value2) {
            addCriterion("image_os_vendor not between", value1, value2, "imageOsVendor");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionIsNull() {
            addCriterion("image_os_version is null");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionIsNotNull() {
            addCriterion("image_os_version is not null");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionEqualTo(String value) {
            addCriterion("image_os_version =", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionNotEqualTo(String value) {
            addCriterion("image_os_version <>", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionGreaterThan(String value) {
            addCriterion("image_os_version >", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionGreaterThanOrEqualTo(String value) {
            addCriterion("image_os_version >=", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionLessThan(String value) {
            addCriterion("image_os_version <", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionLessThanOrEqualTo(String value) {
            addCriterion("image_os_version <=", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionLike(String value) {
            addCriterion("image_os_version like", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionNotLike(String value) {
            addCriterion("image_os_version not like", value, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionIn(List<String> values) {
            addCriterion("image_os_version in", values, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionNotIn(List<String> values) {
            addCriterion("image_os_version not in", values, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionBetween(String value1, String value2) {
            addCriterion("image_os_version between", value1, value2, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageOsVersionNotBetween(String value1, String value2) {
            addCriterion("image_os_version not between", value1, value2, "imageOsVersion");
            return (Criteria) this;
        }

        public Criteria andImageNameIsNull() {
            addCriterion("image_name is null");
            return (Criteria) this;
        }

        public Criteria andImageNameIsNotNull() {
            addCriterion("image_name is not null");
            return (Criteria) this;
        }

        public Criteria andImageNameEqualTo(String value) {
            addCriterion("image_name =", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotEqualTo(String value) {
            addCriterion("image_name <>", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameGreaterThan(String value) {
            addCriterion("image_name >", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameGreaterThanOrEqualTo(String value) {
            addCriterion("image_name >=", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLessThan(String value) {
            addCriterion("image_name <", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLessThanOrEqualTo(String value) {
            addCriterion("image_name <=", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLike(String value) {
            addCriterion("image_name like", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotLike(String value) {
            addCriterion("image_name not like", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameIn(List<String> values) {
            addCriterion("image_name in", values, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotIn(List<String> values) {
            addCriterion("image_name not in", values, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameBetween(String value1, String value2) {
            addCriterion("image_name between", value1, value2, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotBetween(String value1, String value2) {
            addCriterion("image_name not between", value1, value2, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageFormatIsNull() {
            addCriterion("image_format is null");
            return (Criteria) this;
        }

        public Criteria andImageFormatIsNotNull() {
            addCriterion("image_format is not null");
            return (Criteria) this;
        }

        public Criteria andImageFormatEqualTo(Short value) {
            addCriterion("image_format =", value, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatNotEqualTo(Short value) {
            addCriterion("image_format <>", value, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatGreaterThan(Short value) {
            addCriterion("image_format >", value, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatGreaterThanOrEqualTo(Short value) {
            addCriterion("image_format >=", value, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatLessThan(Short value) {
            addCriterion("image_format <", value, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatLessThanOrEqualTo(Short value) {
            addCriterion("image_format <=", value, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatIn(List<Short> values) {
            addCriterion("image_format in", values, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatNotIn(List<Short> values) {
            addCriterion("image_format not in", values, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatBetween(Short value1, Short value2) {
            addCriterion("image_format between", value1, value2, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andImageFormatNotBetween(Short value1, Short value2) {
            addCriterion("image_format not between", value1, value2, "imageFormat");
            return (Criteria) this;
        }

        public Criteria andAgentIpIsNull() {
            addCriterion("agent_ip is null");
            return (Criteria) this;
        }

        public Criteria andAgentIpIsNotNull() {
            addCriterion("agent_ip is not null");
            return (Criteria) this;
        }

        public Criteria andAgentIpEqualTo(String value) {
            addCriterion("agent_ip =", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotEqualTo(String value) {
            addCriterion("agent_ip <>", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpGreaterThan(String value) {
            addCriterion("agent_ip >", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpGreaterThanOrEqualTo(String value) {
            addCriterion("agent_ip >=", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpLessThan(String value) {
            addCriterion("agent_ip <", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpLessThanOrEqualTo(String value) {
            addCriterion("agent_ip <=", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpLike(String value) {
            addCriterion("agent_ip like", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotLike(String value) {
            addCriterion("agent_ip not like", value, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpIn(List<String> values) {
            addCriterion("agent_ip in", values, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotIn(List<String> values) {
            addCriterion("agent_ip not in", values, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpBetween(String value1, String value2) {
            addCriterion("agent_ip between", value1, value2, "agentIp");
            return (Criteria) this;
        }

        public Criteria andAgentIpNotBetween(String value1, String value2) {
            addCriterion("agent_ip not between", value1, value2, "agentIp");
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

        public Criteria andIsPublicIsNull() {
            addCriterion("is_public is null");
            return (Criteria) this;
        }

        public Criteria andIsPublicIsNotNull() {
            addCriterion("is_public is not null");
            return (Criteria) this;
        }

        public Criteria andIsPublicEqualTo(Boolean value) {
            addCriterion("is_public =", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicNotEqualTo(Boolean value) {
            addCriterion("is_public <>", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicGreaterThan(Boolean value) {
            addCriterion("is_public >", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_public >=", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicLessThan(Boolean value) {
            addCriterion("is_public <", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicLessThanOrEqualTo(Boolean value) {
            addCriterion("is_public <=", value, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicIn(List<Boolean> values) {
            addCriterion("is_public in", values, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicNotIn(List<Boolean> values) {
            addCriterion("is_public not in", values, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicBetween(Boolean value1, Boolean value2) {
            addCriterion("is_public between", value1, value2, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_public not between", value1, value2, "isPublic");
            return (Criteria) this;
        }

        public Criteria andIsPublicEqualToOrEeUserEqualTo(Boolean isPublic, String eeUser) {
            addCriterion("( is_public = " + isPublic + " or ee_user = '" + eeUser + "')");
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

        public Criteria andVmInstanceIdIsNull() {
            addCriterion("vm_instance_id is null");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdIsNotNull() {
            addCriterion("vm_instance_id is not null");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdEqualTo(String value) {
            addCriterion("vm_instance_id =", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdNotEqualTo(String value) {
            addCriterion("vm_instance_id <>", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdGreaterThan(String value) {
            addCriterion("vm_instance_id >", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdGreaterThanOrEqualTo(String value) {
            addCriterion("vm_instance_id >=", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdLessThan(String value) {
            addCriterion("vm_instance_id <", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdLessThanOrEqualTo(String value) {
            addCriterion("vm_instance_id <=", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdLike(String value) {
            addCriterion("vm_instance_id like", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdNotLike(String value) {
            addCriterion("vm_instance_id not like", value, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdIn(List<String> values) {
            addCriterion("vm_instance_id in", values, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdNotIn(List<String> values) {
            addCriterion("vm_instance_id not in", values, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdBetween(String value1, String value2) {
            addCriterion("vm_instance_id between", value1, value2, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andVmInstanceIdNotBetween(String value1, String value2) {
            addCriterion("vm_instance_id not between", value1, value2, "vmInstanceId");
            return (Criteria) this;
        }

        public Criteria andImageBaseIsNull() {
            addCriterion("image_base is null");
            return (Criteria) this;
        }

        public Criteria andImageBaseIsNotNull() {
            addCriterion("image_base is not null");
            return (Criteria) this;
        }

        public Criteria andImageBaseEqualTo(String value) {
            addCriterion("image_base =", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseNotEqualTo(String value) {
            addCriterion("image_base <>", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseGreaterThan(String value) {
            addCriterion("image_base >", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseGreaterThanOrEqualTo(String value) {
            addCriterion("image_base >=", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseLessThan(String value) {
            addCriterion("image_base <", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseLessThanOrEqualTo(String value) {
            addCriterion("image_base <=", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseLike(String value) {
            addCriterion("image_base like", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseNotLike(String value) {
            addCriterion("image_base not like", value, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseIn(List<String> values) {
            addCriterion("image_base in", values, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseNotIn(List<String> values) {
            addCriterion("image_base not in", values, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseBetween(String value1, String value2) {
            addCriterion("image_base between", value1, value2, "imageBase");
            return (Criteria) this;
        }

        public Criteria andImageBaseNotBetween(String value1, String value2) {
            addCriterion("image_base not between", value1, value2, "imageBase");
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

        public Criteria andEeVisibilityIsNull() {
            addCriterion("ee_visibility is null");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityIsNotNull() {
            addCriterion("ee_visibility is not null");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityEqualTo(Boolean value) {
            addCriterion("ee_visibility =", value, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityNotEqualTo(Boolean value) {
            addCriterion("ee_visibility <>", value, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityGreaterThan(Boolean value) {
            addCriterion("ee_visibility >", value, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityGreaterThanOrEqualTo(Boolean value) {
            addCriterion("ee_visibility >=", value, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityLessThan(Boolean value) {
            addCriterion("ee_visibility <", value, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityLessThanOrEqualTo(Boolean value) {
            addCriterion("ee_visibility <=", value, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityIn(List<Boolean> values) {
            addCriterion("ee_visibility in", values, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityNotIn(List<Boolean> values) {
            addCriterion("ee_visibility not in", values, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityBetween(Boolean value1, Boolean value2) {
            addCriterion("ee_visibility between", value1, value2, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityNotBetween(Boolean value1, Boolean value2) {
            addCriterion("ee_visibility not between", value1, value2, "eeVisibility");
            return (Criteria) this;
        }

        public Criteria andEeVisibilityIsNullOrEqualTo(Boolean value) {
            addCriterion("( ee_visibility is null or ee_visibility = " + value + " )");
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