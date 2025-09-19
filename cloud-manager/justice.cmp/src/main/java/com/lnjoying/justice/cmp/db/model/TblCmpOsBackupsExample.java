package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsBackupsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsBackupsExample() {
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

        public Criteria andHostIsNull() {
            addCriterion("host is null");
            return (Criteria) this;
        }

        public Criteria andHostIsNotNull() {
            addCriterion("host is not null");
            return (Criteria) this;
        }

        public Criteria andHostEqualTo(String value) {
            addCriterion("host =", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotEqualTo(String value) {
            addCriterion("host <>", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostGreaterThan(String value) {
            addCriterion("host >", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostGreaterThanOrEqualTo(String value) {
            addCriterion("host >=", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostLessThan(String value) {
            addCriterion("host <", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostLessThanOrEqualTo(String value) {
            addCriterion("host <=", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostLike(String value) {
            addCriterion("host like", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotLike(String value) {
            addCriterion("host not like", value, "host");
            return (Criteria) this;
        }

        public Criteria andHostIn(List<String> values) {
            addCriterion("host in", values, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotIn(List<String> values) {
            addCriterion("host not in", values, "host");
            return (Criteria) this;
        }

        public Criteria andHostBetween(String value1, String value2) {
            addCriterion("host between", value1, value2, "host");
            return (Criteria) this;
        }

        public Criteria andHostNotBetween(String value1, String value2) {
            addCriterion("host not between", value1, value2, "host");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneIsNull() {
            addCriterion("availability_zone is null");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneIsNotNull() {
            addCriterion("availability_zone is not null");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneEqualTo(String value) {
            addCriterion("availability_zone =", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneNotEqualTo(String value) {
            addCriterion("availability_zone <>", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneGreaterThan(String value) {
            addCriterion("availability_zone >", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneGreaterThanOrEqualTo(String value) {
            addCriterion("availability_zone >=", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneLessThan(String value) {
            addCriterion("availability_zone <", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneLessThanOrEqualTo(String value) {
            addCriterion("availability_zone <=", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneLike(String value) {
            addCriterion("availability_zone like", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneNotLike(String value) {
            addCriterion("availability_zone not like", value, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneIn(List<String> values) {
            addCriterion("availability_zone in", values, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneNotIn(List<String> values) {
            addCriterion("availability_zone not in", values, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneBetween(String value1, String value2) {
            addCriterion("availability_zone between", value1, value2, "availabilityZone");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneNotBetween(String value1, String value2) {
            addCriterion("availability_zone not between", value1, value2, "availabilityZone");
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

        public Criteria andContainerIsNull() {
            addCriterion("container is null");
            return (Criteria) this;
        }

        public Criteria andContainerIsNotNull() {
            addCriterion("container is not null");
            return (Criteria) this;
        }

        public Criteria andContainerEqualTo(String value) {
            addCriterion("container =", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerNotEqualTo(String value) {
            addCriterion("container <>", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerGreaterThan(String value) {
            addCriterion("container >", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerGreaterThanOrEqualTo(String value) {
            addCriterion("container >=", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerLessThan(String value) {
            addCriterion("container <", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerLessThanOrEqualTo(String value) {
            addCriterion("container <=", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerLike(String value) {
            addCriterion("container like", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerNotLike(String value) {
            addCriterion("container not like", value, "container");
            return (Criteria) this;
        }

        public Criteria andContainerIn(List<String> values) {
            addCriterion("container in", values, "container");
            return (Criteria) this;
        }

        public Criteria andContainerNotIn(List<String> values) {
            addCriterion("container not in", values, "container");
            return (Criteria) this;
        }

        public Criteria andContainerBetween(String value1, String value2) {
            addCriterion("container between", value1, value2, "container");
            return (Criteria) this;
        }

        public Criteria andContainerNotBetween(String value1, String value2) {
            addCriterion("container not between", value1, value2, "container");
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

        public Criteria andFailReasonIsNull() {
            addCriterion("fail_reason is null");
            return (Criteria) this;
        }

        public Criteria andFailReasonIsNotNull() {
            addCriterion("fail_reason is not null");
            return (Criteria) this;
        }

        public Criteria andFailReasonEqualTo(String value) {
            addCriterion("fail_reason =", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonNotEqualTo(String value) {
            addCriterion("fail_reason <>", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonGreaterThan(String value) {
            addCriterion("fail_reason >", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonGreaterThanOrEqualTo(String value) {
            addCriterion("fail_reason >=", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonLessThan(String value) {
            addCriterion("fail_reason <", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonLessThanOrEqualTo(String value) {
            addCriterion("fail_reason <=", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonLike(String value) {
            addCriterion("fail_reason like", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonNotLike(String value) {
            addCriterion("fail_reason not like", value, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonIn(List<String> values) {
            addCriterion("fail_reason in", values, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonNotIn(List<String> values) {
            addCriterion("fail_reason not in", values, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonBetween(String value1, String value2) {
            addCriterion("fail_reason between", value1, value2, "failReason");
            return (Criteria) this;
        }

        public Criteria andFailReasonNotBetween(String value1, String value2) {
            addCriterion("fail_reason not between", value1, value2, "failReason");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataIsNull() {
            addCriterion("service_metadata is null");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataIsNotNull() {
            addCriterion("service_metadata is not null");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataEqualTo(String value) {
            addCriterion("service_metadata =", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataNotEqualTo(String value) {
            addCriterion("service_metadata <>", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataGreaterThan(String value) {
            addCriterion("service_metadata >", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataGreaterThanOrEqualTo(String value) {
            addCriterion("service_metadata >=", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataLessThan(String value) {
            addCriterion("service_metadata <", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataLessThanOrEqualTo(String value) {
            addCriterion("service_metadata <=", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataLike(String value) {
            addCriterion("service_metadata like", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataNotLike(String value) {
            addCriterion("service_metadata not like", value, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataIn(List<String> values) {
            addCriterion("service_metadata in", values, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataNotIn(List<String> values) {
            addCriterion("service_metadata not in", values, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataBetween(String value1, String value2) {
            addCriterion("service_metadata between", value1, value2, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceMetadataNotBetween(String value1, String value2) {
            addCriterion("service_metadata not between", value1, value2, "serviceMetadata");
            return (Criteria) this;
        }

        public Criteria andServiceIsNull() {
            addCriterion("service is null");
            return (Criteria) this;
        }

        public Criteria andServiceIsNotNull() {
            addCriterion("service is not null");
            return (Criteria) this;
        }

        public Criteria andServiceEqualTo(String value) {
            addCriterion("service =", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotEqualTo(String value) {
            addCriterion("service <>", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceGreaterThan(String value) {
            addCriterion("service >", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceGreaterThanOrEqualTo(String value) {
            addCriterion("service >=", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceLessThan(String value) {
            addCriterion("service <", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceLessThanOrEqualTo(String value) {
            addCriterion("service <=", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceLike(String value) {
            addCriterion("service like", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotLike(String value) {
            addCriterion("service not like", value, "service");
            return (Criteria) this;
        }

        public Criteria andServiceIn(List<String> values) {
            addCriterion("service in", values, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotIn(List<String> values) {
            addCriterion("service not in", values, "service");
            return (Criteria) this;
        }

        public Criteria andServiceBetween(String value1, String value2) {
            addCriterion("service between", value1, value2, "service");
            return (Criteria) this;
        }

        public Criteria andServiceNotBetween(String value1, String value2) {
            addCriterion("service not between", value1, value2, "service");
            return (Criteria) this;
        }

        public Criteria andSizeIsNull() {
            addCriterion("size is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("size is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(Integer value) {
            addCriterion("size =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(Integer value) {
            addCriterion("size <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(Integer value) {
            addCriterion("size >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("size >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(Integer value) {
            addCriterion("size <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(Integer value) {
            addCriterion("size <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<Integer> values) {
            addCriterion("size in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<Integer> values) {
            addCriterion("size not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(Integer value1, Integer value2) {
            addCriterion("size between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("size not between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andObjectCountIsNull() {
            addCriterion("object_count is null");
            return (Criteria) this;
        }

        public Criteria andObjectCountIsNotNull() {
            addCriterion("object_count is not null");
            return (Criteria) this;
        }

        public Criteria andObjectCountEqualTo(Integer value) {
            addCriterion("object_count =", value, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountNotEqualTo(Integer value) {
            addCriterion("object_count <>", value, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountGreaterThan(Integer value) {
            addCriterion("object_count >", value, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("object_count >=", value, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountLessThan(Integer value) {
            addCriterion("object_count <", value, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountLessThanOrEqualTo(Integer value) {
            addCriterion("object_count <=", value, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountIn(List<Integer> values) {
            addCriterion("object_count in", values, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountNotIn(List<Integer> values) {
            addCriterion("object_count not in", values, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountBetween(Integer value1, Integer value2) {
            addCriterion("object_count between", value1, value2, "objectCount");
            return (Criteria) this;
        }

        public Criteria andObjectCountNotBetween(Integer value1, Integer value2) {
            addCriterion("object_count not between", value1, value2, "objectCount");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNull() {
            addCriterion("parent_id is null");
            return (Criteria) this;
        }

        public Criteria andParentIdIsNotNull() {
            addCriterion("parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andParentIdEqualTo(String value) {
            addCriterion("parent_id =", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotEqualTo(String value) {
            addCriterion("parent_id <>", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThan(String value) {
            addCriterion("parent_id >", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdGreaterThanOrEqualTo(String value) {
            addCriterion("parent_id >=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThan(String value) {
            addCriterion("parent_id <", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLessThanOrEqualTo(String value) {
            addCriterion("parent_id <=", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdLike(String value) {
            addCriterion("parent_id like", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotLike(String value) {
            addCriterion("parent_id not like", value, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdIn(List<String> values) {
            addCriterion("parent_id in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotIn(List<String> values) {
            addCriterion("parent_id not in", values, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdBetween(String value1, String value2) {
            addCriterion("parent_id between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andParentIdNotBetween(String value1, String value2) {
            addCriterion("parent_id not between", value1, value2, "parentId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdIsNull() {
            addCriterion("temp_volume_id is null");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdIsNotNull() {
            addCriterion("temp_volume_id is not null");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdEqualTo(String value) {
            addCriterion("temp_volume_id =", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdNotEqualTo(String value) {
            addCriterion("temp_volume_id <>", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdGreaterThan(String value) {
            addCriterion("temp_volume_id >", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdGreaterThanOrEqualTo(String value) {
            addCriterion("temp_volume_id >=", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdLessThan(String value) {
            addCriterion("temp_volume_id <", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdLessThanOrEqualTo(String value) {
            addCriterion("temp_volume_id <=", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdLike(String value) {
            addCriterion("temp_volume_id like", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdNotLike(String value) {
            addCriterion("temp_volume_id not like", value, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdIn(List<String> values) {
            addCriterion("temp_volume_id in", values, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdNotIn(List<String> values) {
            addCriterion("temp_volume_id not in", values, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdBetween(String value1, String value2) {
            addCriterion("temp_volume_id between", value1, value2, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempVolumeIdNotBetween(String value1, String value2) {
            addCriterion("temp_volume_id not between", value1, value2, "tempVolumeId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdIsNull() {
            addCriterion("temp_snapshot_id is null");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdIsNotNull() {
            addCriterion("temp_snapshot_id is not null");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdEqualTo(String value) {
            addCriterion("temp_snapshot_id =", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdNotEqualTo(String value) {
            addCriterion("temp_snapshot_id <>", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdGreaterThan(String value) {
            addCriterion("temp_snapshot_id >", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdGreaterThanOrEqualTo(String value) {
            addCriterion("temp_snapshot_id >=", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdLessThan(String value) {
            addCriterion("temp_snapshot_id <", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdLessThanOrEqualTo(String value) {
            addCriterion("temp_snapshot_id <=", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdLike(String value) {
            addCriterion("temp_snapshot_id like", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdNotLike(String value) {
            addCriterion("temp_snapshot_id not like", value, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdIn(List<String> values) {
            addCriterion("temp_snapshot_id in", values, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdNotIn(List<String> values) {
            addCriterion("temp_snapshot_id not in", values, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdBetween(String value1, String value2) {
            addCriterion("temp_snapshot_id between", value1, value2, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andTempSnapshotIdNotBetween(String value1, String value2) {
            addCriterion("temp_snapshot_id not between", value1, value2, "tempSnapshotId");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsIsNull() {
            addCriterion("num_dependent_backups is null");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsIsNotNull() {
            addCriterion("num_dependent_backups is not null");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsEqualTo(Integer value) {
            addCriterion("num_dependent_backups =", value, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsNotEqualTo(Integer value) {
            addCriterion("num_dependent_backups <>", value, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsGreaterThan(Integer value) {
            addCriterion("num_dependent_backups >", value, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsGreaterThanOrEqualTo(Integer value) {
            addCriterion("num_dependent_backups >=", value, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsLessThan(Integer value) {
            addCriterion("num_dependent_backups <", value, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsLessThanOrEqualTo(Integer value) {
            addCriterion("num_dependent_backups <=", value, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsIn(List<Integer> values) {
            addCriterion("num_dependent_backups in", values, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsNotIn(List<Integer> values) {
            addCriterion("num_dependent_backups not in", values, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsBetween(Integer value1, Integer value2) {
            addCriterion("num_dependent_backups between", value1, value2, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andNumDependentBackupsNotBetween(Integer value1, Integer value2) {
            addCriterion("num_dependent_backups not between", value1, value2, "numDependentBackups");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdIsNull() {
            addCriterion("snapshot_id is null");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdIsNotNull() {
            addCriterion("snapshot_id is not null");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdEqualTo(String value) {
            addCriterion("snapshot_id =", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdNotEqualTo(String value) {
            addCriterion("snapshot_id <>", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdGreaterThan(String value) {
            addCriterion("snapshot_id >", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdGreaterThanOrEqualTo(String value) {
            addCriterion("snapshot_id >=", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdLessThan(String value) {
            addCriterion("snapshot_id <", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdLessThanOrEqualTo(String value) {
            addCriterion("snapshot_id <=", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdLike(String value) {
            addCriterion("snapshot_id like", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdNotLike(String value) {
            addCriterion("snapshot_id not like", value, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdIn(List<String> values) {
            addCriterion("snapshot_id in", values, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdNotIn(List<String> values) {
            addCriterion("snapshot_id not in", values, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdBetween(String value1, String value2) {
            addCriterion("snapshot_id between", value1, value2, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andSnapshotIdNotBetween(String value1, String value2) {
            addCriterion("snapshot_id not between", value1, value2, "snapshotId");
            return (Criteria) this;
        }

        public Criteria andDataTimestampIsNull() {
            addCriterion("data_timestamp is null");
            return (Criteria) this;
        }

        public Criteria andDataTimestampIsNotNull() {
            addCriterion("data_timestamp is not null");
            return (Criteria) this;
        }

        public Criteria andDataTimestampEqualTo(Date value) {
            addCriterion("data_timestamp =", value, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampNotEqualTo(Date value) {
            addCriterion("data_timestamp <>", value, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampGreaterThan(Date value) {
            addCriterion("data_timestamp >", value, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampGreaterThanOrEqualTo(Date value) {
            addCriterion("data_timestamp >=", value, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampLessThan(Date value) {
            addCriterion("data_timestamp <", value, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampLessThanOrEqualTo(Date value) {
            addCriterion("data_timestamp <=", value, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampIn(List<Date> values) {
            addCriterion("data_timestamp in", values, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampNotIn(List<Date> values) {
            addCriterion("data_timestamp not in", values, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampBetween(Date value1, Date value2) {
            addCriterion("data_timestamp between", value1, value2, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andDataTimestampNotBetween(Date value1, Date value2) {
            addCriterion("data_timestamp not between", value1, value2, "dataTimestamp");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdIsNull() {
            addCriterion("restore_volume_id is null");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdIsNotNull() {
            addCriterion("restore_volume_id is not null");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdEqualTo(String value) {
            addCriterion("restore_volume_id =", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdNotEqualTo(String value) {
            addCriterion("restore_volume_id <>", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdGreaterThan(String value) {
            addCriterion("restore_volume_id >", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdGreaterThanOrEqualTo(String value) {
            addCriterion("restore_volume_id >=", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdLessThan(String value) {
            addCriterion("restore_volume_id <", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdLessThanOrEqualTo(String value) {
            addCriterion("restore_volume_id <=", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdLike(String value) {
            addCriterion("restore_volume_id like", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdNotLike(String value) {
            addCriterion("restore_volume_id not like", value, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdIn(List<String> values) {
            addCriterion("restore_volume_id in", values, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdNotIn(List<String> values) {
            addCriterion("restore_volume_id not in", values, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdBetween(String value1, String value2) {
            addCriterion("restore_volume_id between", value1, value2, "restoreVolumeId");
            return (Criteria) this;
        }

        public Criteria andRestoreVolumeIdNotBetween(String value1, String value2) {
            addCriterion("restore_volume_id not between", value1, value2, "restoreVolumeId");
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