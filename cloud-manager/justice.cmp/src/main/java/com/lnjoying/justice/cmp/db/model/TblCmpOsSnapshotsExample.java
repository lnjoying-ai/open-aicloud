package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsSnapshotsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsSnapshotsExample() {
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

        public Criteria andCreatedAtIsNull() {
            addCriterion("created_at is null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIsNotNull() {
            addCriterion("created_at is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedAtEqualTo(Date value) {
            addCriterion("created_at =", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotEqualTo(Date value) {
            addCriterion("created_at <>", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThan(Date value) {
            addCriterion("created_at >", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("created_at >=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThan(Date value) {
            addCriterion("created_at <", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtLessThanOrEqualTo(Date value) {
            addCriterion("created_at <=", value, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtIn(List<Date> values) {
            addCriterion("created_at in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotIn(List<Date> values) {
            addCriterion("created_at not in", values, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtBetween(Date value1, Date value2) {
            addCriterion("created_at between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andCreatedAtNotBetween(Date value1, Date value2) {
            addCriterion("created_at not between", value1, value2, "createdAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNull() {
            addCriterion("updated_at is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIsNotNull() {
            addCriterion("updated_at is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtEqualTo(Date value) {
            addCriterion("updated_at =", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotEqualTo(Date value) {
            addCriterion("updated_at <>", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThan(Date value) {
            addCriterion("updated_at >", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("updated_at >=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThan(Date value) {
            addCriterion("updated_at <", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtLessThanOrEqualTo(Date value) {
            addCriterion("updated_at <=", value, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtIn(List<Date> values) {
            addCriterion("updated_at in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotIn(List<Date> values) {
            addCriterion("updated_at not in", values, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtBetween(Date value1, Date value2) {
            addCriterion("updated_at between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andUpdatedAtNotBetween(Date value1, Date value2) {
            addCriterion("updated_at not between", value1, value2, "updatedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtIsNull() {
            addCriterion("deleted_at is null");
            return (Criteria) this;
        }

        public Criteria andDeletedAtIsNotNull() {
            addCriterion("deleted_at is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedAtEqualTo(Date value) {
            addCriterion("deleted_at =", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtNotEqualTo(Date value) {
            addCriterion("deleted_at <>", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtGreaterThan(Date value) {
            addCriterion("deleted_at >", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("deleted_at >=", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtLessThan(Date value) {
            addCriterion("deleted_at <", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtLessThanOrEqualTo(Date value) {
            addCriterion("deleted_at <=", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtIn(List<Date> values) {
            addCriterion("deleted_at in", values, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtNotIn(List<Date> values) {
            addCriterion("deleted_at not in", values, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtBetween(Date value1, Date value2) {
            addCriterion("deleted_at between", value1, value2, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtNotBetween(Date value1, Date value2) {
            addCriterion("deleted_at not between", value1, value2, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Short value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Short value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Short value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Short value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Short value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Short value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Short> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Short> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Short value1, Short value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Short value1, Short value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andVolumeIdIsNull() {
            addCriterion("volume_id is null");
            return (Criteria) this;
        }

        public Criteria andVolumeIdIsNotNull() {
            addCriterion("volume_id is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeIdEqualTo(String value) {
            addCriterion("volume_id =", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdNotEqualTo(String value) {
            addCriterion("volume_id <>", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdGreaterThan(String value) {
            addCriterion("volume_id >", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdGreaterThanOrEqualTo(String value) {
            addCriterion("volume_id >=", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdLessThan(String value) {
            addCriterion("volume_id <", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdLessThanOrEqualTo(String value) {
            addCriterion("volume_id <=", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdLike(String value) {
            addCriterion("volume_id like", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdNotLike(String value) {
            addCriterion("volume_id not like", value, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdIn(List<String> values) {
            addCriterion("volume_id in", values, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdNotIn(List<String> values) {
            addCriterion("volume_id not in", values, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdBetween(String value1, String value2) {
            addCriterion("volume_id between", value1, value2, "volumeId");
            return (Criteria) this;
        }

        public Criteria andVolumeIdNotBetween(String value1, String value2) {
            addCriterion("volume_id not between", value1, value2, "volumeId");
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

        public Criteria andProjectIdIsNull() {
            addCriterion("project_id is null");
            return (Criteria) this;
        }

        public Criteria andProjectIdIsNotNull() {
            addCriterion("project_id is not null");
            return (Criteria) this;
        }

        public Criteria andProjectIdEqualTo(String value) {
            addCriterion("project_id =", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotEqualTo(String value) {
            addCriterion("project_id <>", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThan(String value) {
            addCriterion("project_id >", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdGreaterThanOrEqualTo(String value) {
            addCriterion("project_id >=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThan(String value) {
            addCriterion("project_id <", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLessThanOrEqualTo(String value) {
            addCriterion("project_id <=", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdLike(String value) {
            addCriterion("project_id like", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotLike(String value) {
            addCriterion("project_id not like", value, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdIn(List<String> values) {
            addCriterion("project_id in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotIn(List<String> values) {
            addCriterion("project_id not in", values, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdBetween(String value1, String value2) {
            addCriterion("project_id between", value1, value2, "projectId");
            return (Criteria) this;
        }

        public Criteria andProjectIdNotBetween(String value1, String value2) {
            addCriterion("project_id not between", value1, value2, "projectId");
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

        public Criteria andStatusEqualTo(String value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(String value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(String value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(String value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(String value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(String value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLike(String value) {
            addCriterion("status like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotLike(String value) {
            addCriterion("status not like", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<String> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<String> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(String value1, String value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(String value1, String value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andProgressIsNull() {
            addCriterion("progress is null");
            return (Criteria) this;
        }

        public Criteria andProgressIsNotNull() {
            addCriterion("progress is not null");
            return (Criteria) this;
        }

        public Criteria andProgressEqualTo(String value) {
            addCriterion("progress =", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotEqualTo(String value) {
            addCriterion("progress <>", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThan(String value) {
            addCriterion("progress >", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThanOrEqualTo(String value) {
            addCriterion("progress >=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThan(String value) {
            addCriterion("progress <", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThanOrEqualTo(String value) {
            addCriterion("progress <=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLike(String value) {
            addCriterion("progress like", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotLike(String value) {
            addCriterion("progress not like", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressIn(List<String> values) {
            addCriterion("progress in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotIn(List<String> values) {
            addCriterion("progress not in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressBetween(String value1, String value2) {
            addCriterion("progress between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotBetween(String value1, String value2) {
            addCriterion("progress not between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeIsNull() {
            addCriterion("volume_size is null");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeIsNotNull() {
            addCriterion("volume_size is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeEqualTo(Integer value) {
            addCriterion("volume_size =", value, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeNotEqualTo(Integer value) {
            addCriterion("volume_size <>", value, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeGreaterThan(Integer value) {
            addCriterion("volume_size >", value, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("volume_size >=", value, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeLessThan(Integer value) {
            addCriterion("volume_size <", value, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeLessThanOrEqualTo(Integer value) {
            addCriterion("volume_size <=", value, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeIn(List<Integer> values) {
            addCriterion("volume_size in", values, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeNotIn(List<Integer> values) {
            addCriterion("volume_size not in", values, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeBetween(Integer value1, Integer value2) {
            addCriterion("volume_size between", value1, value2, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andVolumeSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("volume_size not between", value1, value2, "volumeSize");
            return (Criteria) this;
        }

        public Criteria andScheduledAtIsNull() {
            addCriterion("scheduled_at is null");
            return (Criteria) this;
        }

        public Criteria andScheduledAtIsNotNull() {
            addCriterion("scheduled_at is not null");
            return (Criteria) this;
        }

        public Criteria andScheduledAtEqualTo(Date value) {
            addCriterion("scheduled_at =", value, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtNotEqualTo(Date value) {
            addCriterion("scheduled_at <>", value, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtGreaterThan(Date value) {
            addCriterion("scheduled_at >", value, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtGreaterThanOrEqualTo(Date value) {
            addCriterion("scheduled_at >=", value, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtLessThan(Date value) {
            addCriterion("scheduled_at <", value, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtLessThanOrEqualTo(Date value) {
            addCriterion("scheduled_at <=", value, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtIn(List<Date> values) {
            addCriterion("scheduled_at in", values, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtNotIn(List<Date> values) {
            addCriterion("scheduled_at not in", values, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtBetween(Date value1, Date value2) {
            addCriterion("scheduled_at between", value1, value2, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andScheduledAtNotBetween(Date value1, Date value2) {
            addCriterion("scheduled_at not between", value1, value2, "scheduledAt");
            return (Criteria) this;
        }

        public Criteria andDisplayNameIsNull() {
            addCriterion("display_name is null");
            return (Criteria) this;
        }

        public Criteria andDisplayNameIsNotNull() {
            addCriterion("display_name is not null");
            return (Criteria) this;
        }

        public Criteria andDisplayNameEqualTo(String value) {
            addCriterion("display_name =", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameNotEqualTo(String value) {
            addCriterion("display_name <>", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameGreaterThan(String value) {
            addCriterion("display_name >", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameGreaterThanOrEqualTo(String value) {
            addCriterion("display_name >=", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameLessThan(String value) {
            addCriterion("display_name <", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameLessThanOrEqualTo(String value) {
            addCriterion("display_name <=", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameLike(String value) {
            addCriterion("display_name like", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameNotLike(String value) {
            addCriterion("display_name not like", value, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameIn(List<String> values) {
            addCriterion("display_name in", values, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameNotIn(List<String> values) {
            addCriterion("display_name not in", values, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameBetween(String value1, String value2) {
            addCriterion("display_name between", value1, value2, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayNameNotBetween(String value1, String value2) {
            addCriterion("display_name not between", value1, value2, "displayName");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionIsNull() {
            addCriterion("display_description is null");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionIsNotNull() {
            addCriterion("display_description is not null");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionEqualTo(String value) {
            addCriterion("display_description =", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionNotEqualTo(String value) {
            addCriterion("display_description <>", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionGreaterThan(String value) {
            addCriterion("display_description >", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionGreaterThanOrEqualTo(String value) {
            addCriterion("display_description >=", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionLessThan(String value) {
            addCriterion("display_description <", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionLessThanOrEqualTo(String value) {
            addCriterion("display_description <=", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionLike(String value) {
            addCriterion("display_description like", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionNotLike(String value) {
            addCriterion("display_description not like", value, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionIn(List<String> values) {
            addCriterion("display_description in", values, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionNotIn(List<String> values) {
            addCriterion("display_description not in", values, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionBetween(String value1, String value2) {
            addCriterion("display_description between", value1, value2, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andDisplayDescriptionNotBetween(String value1, String value2) {
            addCriterion("display_description not between", value1, value2, "displayDescription");
            return (Criteria) this;
        }

        public Criteria andProviderLocationIsNull() {
            addCriterion("provider_location is null");
            return (Criteria) this;
        }

        public Criteria andProviderLocationIsNotNull() {
            addCriterion("provider_location is not null");
            return (Criteria) this;
        }

        public Criteria andProviderLocationEqualTo(String value) {
            addCriterion("provider_location =", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationNotEqualTo(String value) {
            addCriterion("provider_location <>", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationGreaterThan(String value) {
            addCriterion("provider_location >", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationGreaterThanOrEqualTo(String value) {
            addCriterion("provider_location >=", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationLessThan(String value) {
            addCriterion("provider_location <", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationLessThanOrEqualTo(String value) {
            addCriterion("provider_location <=", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationLike(String value) {
            addCriterion("provider_location like", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationNotLike(String value) {
            addCriterion("provider_location not like", value, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationIn(List<String> values) {
            addCriterion("provider_location in", values, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationNotIn(List<String> values) {
            addCriterion("provider_location not in", values, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationBetween(String value1, String value2) {
            addCriterion("provider_location between", value1, value2, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andProviderLocationNotBetween(String value1, String value2) {
            addCriterion("provider_location not between", value1, value2, "providerLocation");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdIsNull() {
            addCriterion("encryption_key_id is null");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdIsNotNull() {
            addCriterion("encryption_key_id is not null");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdEqualTo(String value) {
            addCriterion("encryption_key_id =", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdNotEqualTo(String value) {
            addCriterion("encryption_key_id <>", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdGreaterThan(String value) {
            addCriterion("encryption_key_id >", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdGreaterThanOrEqualTo(String value) {
            addCriterion("encryption_key_id >=", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdLessThan(String value) {
            addCriterion("encryption_key_id <", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdLessThanOrEqualTo(String value) {
            addCriterion("encryption_key_id <=", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdLike(String value) {
            addCriterion("encryption_key_id like", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdNotLike(String value) {
            addCriterion("encryption_key_id not like", value, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdIn(List<String> values) {
            addCriterion("encryption_key_id in", values, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdNotIn(List<String> values) {
            addCriterion("encryption_key_id not in", values, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdBetween(String value1, String value2) {
            addCriterion("encryption_key_id between", value1, value2, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andEncryptionKeyIdNotBetween(String value1, String value2) {
            addCriterion("encryption_key_id not between", value1, value2, "encryptionKeyId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdIsNull() {
            addCriterion("volume_type_id is null");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdIsNotNull() {
            addCriterion("volume_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdEqualTo(String value) {
            addCriterion("volume_type_id =", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdNotEqualTo(String value) {
            addCriterion("volume_type_id <>", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdGreaterThan(String value) {
            addCriterion("volume_type_id >", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdGreaterThanOrEqualTo(String value) {
            addCriterion("volume_type_id >=", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdLessThan(String value) {
            addCriterion("volume_type_id <", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdLessThanOrEqualTo(String value) {
            addCriterion("volume_type_id <=", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdLike(String value) {
            addCriterion("volume_type_id like", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdNotLike(String value) {
            addCriterion("volume_type_id not like", value, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdIn(List<String> values) {
            addCriterion("volume_type_id in", values, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdNotIn(List<String> values) {
            addCriterion("volume_type_id not in", values, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdBetween(String value1, String value2) {
            addCriterion("volume_type_id between", value1, value2, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIdNotBetween(String value1, String value2) {
            addCriterion("volume_type_id not between", value1, value2, "volumeTypeId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdIsNull() {
            addCriterion("cgsnapshot_id is null");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdIsNotNull() {
            addCriterion("cgsnapshot_id is not null");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdEqualTo(String value) {
            addCriterion("cgsnapshot_id =", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdNotEqualTo(String value) {
            addCriterion("cgsnapshot_id <>", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdGreaterThan(String value) {
            addCriterion("cgsnapshot_id >", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdGreaterThanOrEqualTo(String value) {
            addCriterion("cgsnapshot_id >=", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdLessThan(String value) {
            addCriterion("cgsnapshot_id <", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdLessThanOrEqualTo(String value) {
            addCriterion("cgsnapshot_id <=", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdLike(String value) {
            addCriterion("cgsnapshot_id like", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdNotLike(String value) {
            addCriterion("cgsnapshot_id not like", value, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdIn(List<String> values) {
            addCriterion("cgsnapshot_id in", values, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdNotIn(List<String> values) {
            addCriterion("cgsnapshot_id not in", values, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdBetween(String value1, String value2) {
            addCriterion("cgsnapshot_id between", value1, value2, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andCgsnapshotIdNotBetween(String value1, String value2) {
            addCriterion("cgsnapshot_id not between", value1, value2, "cgsnapshotId");
            return (Criteria) this;
        }

        public Criteria andProviderIdIsNull() {
            addCriterion("provider_id is null");
            return (Criteria) this;
        }

        public Criteria andProviderIdIsNotNull() {
            addCriterion("provider_id is not null");
            return (Criteria) this;
        }

        public Criteria andProviderIdEqualTo(String value) {
            addCriterion("provider_id =", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotEqualTo(String value) {
            addCriterion("provider_id <>", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdGreaterThan(String value) {
            addCriterion("provider_id >", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdGreaterThanOrEqualTo(String value) {
            addCriterion("provider_id >=", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdLessThan(String value) {
            addCriterion("provider_id <", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdLessThanOrEqualTo(String value) {
            addCriterion("provider_id <=", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdLike(String value) {
            addCriterion("provider_id like", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotLike(String value) {
            addCriterion("provider_id not like", value, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdIn(List<String> values) {
            addCriterion("provider_id in", values, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotIn(List<String> values) {
            addCriterion("provider_id not in", values, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdBetween(String value1, String value2) {
            addCriterion("provider_id between", value1, value2, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderIdNotBetween(String value1, String value2) {
            addCriterion("provider_id not between", value1, value2, "providerId");
            return (Criteria) this;
        }

        public Criteria andProviderAuthIsNull() {
            addCriterion("provider_auth is null");
            return (Criteria) this;
        }

        public Criteria andProviderAuthIsNotNull() {
            addCriterion("provider_auth is not null");
            return (Criteria) this;
        }

        public Criteria andProviderAuthEqualTo(String value) {
            addCriterion("provider_auth =", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthNotEqualTo(String value) {
            addCriterion("provider_auth <>", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthGreaterThan(String value) {
            addCriterion("provider_auth >", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthGreaterThanOrEqualTo(String value) {
            addCriterion("provider_auth >=", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthLessThan(String value) {
            addCriterion("provider_auth <", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthLessThanOrEqualTo(String value) {
            addCriterion("provider_auth <=", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthLike(String value) {
            addCriterion("provider_auth like", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthNotLike(String value) {
            addCriterion("provider_auth not like", value, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthIn(List<String> values) {
            addCriterion("provider_auth in", values, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthNotIn(List<String> values) {
            addCriterion("provider_auth not in", values, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthBetween(String value1, String value2) {
            addCriterion("provider_auth between", value1, value2, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andProviderAuthNotBetween(String value1, String value2) {
            addCriterion("provider_auth not between", value1, value2, "providerAuth");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdIsNull() {
            addCriterion("group_snapshot_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdIsNotNull() {
            addCriterion("group_snapshot_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdEqualTo(String value) {
            addCriterion("group_snapshot_id =", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdNotEqualTo(String value) {
            addCriterion("group_snapshot_id <>", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdGreaterThan(String value) {
            addCriterion("group_snapshot_id >", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdGreaterThanOrEqualTo(String value) {
            addCriterion("group_snapshot_id >=", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdLessThan(String value) {
            addCriterion("group_snapshot_id <", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdLessThanOrEqualTo(String value) {
            addCriterion("group_snapshot_id <=", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdLike(String value) {
            addCriterion("group_snapshot_id like", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdNotLike(String value) {
            addCriterion("group_snapshot_id not like", value, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdIn(List<String> values) {
            addCriterion("group_snapshot_id in", values, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdNotIn(List<String> values) {
            addCriterion("group_snapshot_id not in", values, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdBetween(String value1, String value2) {
            addCriterion("group_snapshot_id between", value1, value2, "groupSnapshotId");
            return (Criteria) this;
        }

        public Criteria andGroupSnapshotIdNotBetween(String value1, String value2) {
            addCriterion("group_snapshot_id not between", value1, value2, "groupSnapshotId");
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