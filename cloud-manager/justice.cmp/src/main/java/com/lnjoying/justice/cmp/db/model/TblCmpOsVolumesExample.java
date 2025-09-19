package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsVolumesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsVolumesExample() {
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

        public Criteria andEc2IdIsNull() {
            addCriterion("ec2_id is null");
            return (Criteria) this;
        }

        public Criteria andEc2IdIsNotNull() {
            addCriterion("ec2_id is not null");
            return (Criteria) this;
        }

        public Criteria andEc2IdEqualTo(String value) {
            addCriterion("ec2_id =", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdNotEqualTo(String value) {
            addCriterion("ec2_id <>", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdGreaterThan(String value) {
            addCriterion("ec2_id >", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdGreaterThanOrEqualTo(String value) {
            addCriterion("ec2_id >=", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdLessThan(String value) {
            addCriterion("ec2_id <", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdLessThanOrEqualTo(String value) {
            addCriterion("ec2_id <=", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdLike(String value) {
            addCriterion("ec2_id like", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdNotLike(String value) {
            addCriterion("ec2_id not like", value, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdIn(List<String> values) {
            addCriterion("ec2_id in", values, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdNotIn(List<String> values) {
            addCriterion("ec2_id not in", values, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdBetween(String value1, String value2) {
            addCriterion("ec2_id between", value1, value2, "ec2Id");
            return (Criteria) this;
        }

        public Criteria andEc2IdNotBetween(String value1, String value2) {
            addCriterion("ec2_id not between", value1, value2, "ec2Id");
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

        public Criteria andAttachStatusIsNull() {
            addCriterion("attach_status is null");
            return (Criteria) this;
        }

        public Criteria andAttachStatusIsNotNull() {
            addCriterion("attach_status is not null");
            return (Criteria) this;
        }

        public Criteria andAttachStatusEqualTo(String value) {
            addCriterion("attach_status =", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusNotEqualTo(String value) {
            addCriterion("attach_status <>", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusGreaterThan(String value) {
            addCriterion("attach_status >", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusGreaterThanOrEqualTo(String value) {
            addCriterion("attach_status >=", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusLessThan(String value) {
            addCriterion("attach_status <", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusLessThanOrEqualTo(String value) {
            addCriterion("attach_status <=", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusLike(String value) {
            addCriterion("attach_status like", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusNotLike(String value) {
            addCriterion("attach_status not like", value, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusIn(List<String> values) {
            addCriterion("attach_status in", values, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusNotIn(List<String> values) {
            addCriterion("attach_status not in", values, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusBetween(String value1, String value2) {
            addCriterion("attach_status between", value1, value2, "attachStatus");
            return (Criteria) this;
        }

        public Criteria andAttachStatusNotBetween(String value1, String value2) {
            addCriterion("attach_status not between", value1, value2, "attachStatus");
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

        public Criteria andLaunchedAtIsNull() {
            addCriterion("launched_at is null");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtIsNotNull() {
            addCriterion("launched_at is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtEqualTo(Date value) {
            addCriterion("launched_at =", value, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtNotEqualTo(Date value) {
            addCriterion("launched_at <>", value, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtGreaterThan(Date value) {
            addCriterion("launched_at >", value, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("launched_at >=", value, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtLessThan(Date value) {
            addCriterion("launched_at <", value, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtLessThanOrEqualTo(Date value) {
            addCriterion("launched_at <=", value, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtIn(List<Date> values) {
            addCriterion("launched_at in", values, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtNotIn(List<Date> values) {
            addCriterion("launched_at not in", values, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtBetween(Date value1, Date value2) {
            addCriterion("launched_at between", value1, value2, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andLaunchedAtNotBetween(Date value1, Date value2) {
            addCriterion("launched_at not between", value1, value2, "launchedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtIsNull() {
            addCriterion("terminated_at is null");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtIsNotNull() {
            addCriterion("terminated_at is not null");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtEqualTo(Date value) {
            addCriterion("terminated_at =", value, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtNotEqualTo(Date value) {
            addCriterion("terminated_at <>", value, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtGreaterThan(Date value) {
            addCriterion("terminated_at >", value, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("terminated_at >=", value, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtLessThan(Date value) {
            addCriterion("terminated_at <", value, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtLessThanOrEqualTo(Date value) {
            addCriterion("terminated_at <=", value, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtIn(List<Date> values) {
            addCriterion("terminated_at in", values, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtNotIn(List<Date> values) {
            addCriterion("terminated_at not in", values, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtBetween(Date value1, Date value2) {
            addCriterion("terminated_at between", value1, value2, "terminatedAt");
            return (Criteria) this;
        }

        public Criteria andTerminatedAtNotBetween(Date value1, Date value2) {
            addCriterion("terminated_at not between", value1, value2, "terminatedAt");
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

        public Criteria andSourceVolidIsNull() {
            addCriterion("source_volid is null");
            return (Criteria) this;
        }

        public Criteria andSourceVolidIsNotNull() {
            addCriterion("source_volid is not null");
            return (Criteria) this;
        }

        public Criteria andSourceVolidEqualTo(String value) {
            addCriterion("source_volid =", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidNotEqualTo(String value) {
            addCriterion("source_volid <>", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidGreaterThan(String value) {
            addCriterion("source_volid >", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidGreaterThanOrEqualTo(String value) {
            addCriterion("source_volid >=", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidLessThan(String value) {
            addCriterion("source_volid <", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidLessThanOrEqualTo(String value) {
            addCriterion("source_volid <=", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidLike(String value) {
            addCriterion("source_volid like", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidNotLike(String value) {
            addCriterion("source_volid not like", value, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidIn(List<String> values) {
            addCriterion("source_volid in", values, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidNotIn(List<String> values) {
            addCriterion("source_volid not in", values, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidBetween(String value1, String value2) {
            addCriterion("source_volid between", value1, value2, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andSourceVolidNotBetween(String value1, String value2) {
            addCriterion("source_volid not between", value1, value2, "sourceVolid");
            return (Criteria) this;
        }

        public Criteria andBootableIsNull() {
            addCriterion("bootable is null");
            return (Criteria) this;
        }

        public Criteria andBootableIsNotNull() {
            addCriterion("bootable is not null");
            return (Criteria) this;
        }

        public Criteria andBootableEqualTo(Short value) {
            addCriterion("bootable =", value, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableNotEqualTo(Short value) {
            addCriterion("bootable <>", value, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableGreaterThan(Short value) {
            addCriterion("bootable >", value, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableGreaterThanOrEqualTo(Short value) {
            addCriterion("bootable >=", value, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableLessThan(Short value) {
            addCriterion("bootable <", value, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableLessThanOrEqualTo(Short value) {
            addCriterion("bootable <=", value, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableIn(List<Short> values) {
            addCriterion("bootable in", values, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableNotIn(List<Short> values) {
            addCriterion("bootable not in", values, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableBetween(Short value1, Short value2) {
            addCriterion("bootable between", value1, value2, "bootable");
            return (Criteria) this;
        }

        public Criteria andBootableNotBetween(Short value1, Short value2) {
            addCriterion("bootable not between", value1, value2, "bootable");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryIsNull() {
            addCriterion("provider_geometry is null");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryIsNotNull() {
            addCriterion("provider_geometry is not null");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryEqualTo(String value) {
            addCriterion("provider_geometry =", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryNotEqualTo(String value) {
            addCriterion("provider_geometry <>", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryGreaterThan(String value) {
            addCriterion("provider_geometry >", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryGreaterThanOrEqualTo(String value) {
            addCriterion("provider_geometry >=", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryLessThan(String value) {
            addCriterion("provider_geometry <", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryLessThanOrEqualTo(String value) {
            addCriterion("provider_geometry <=", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryLike(String value) {
            addCriterion("provider_geometry like", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryNotLike(String value) {
            addCriterion("provider_geometry not like", value, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryIn(List<String> values) {
            addCriterion("provider_geometry in", values, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryNotIn(List<String> values) {
            addCriterion("provider_geometry not in", values, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryBetween(String value1, String value2) {
            addCriterion("provider_geometry between", value1, value2, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andProviderGeometryNotBetween(String value1, String value2) {
            addCriterion("provider_geometry not between", value1, value2, "providerGeometry");
            return (Criteria) this;
        }

        public Criteria andNameIdIsNull() {
            addCriterion("_name_id is null");
            return (Criteria) this;
        }

        public Criteria andNameIdIsNotNull() {
            addCriterion("_name_id is not null");
            return (Criteria) this;
        }

        public Criteria andNameIdEqualTo(String value) {
            addCriterion("_name_id =", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdNotEqualTo(String value) {
            addCriterion("_name_id <>", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdGreaterThan(String value) {
            addCriterion("_name_id >", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdGreaterThanOrEqualTo(String value) {
            addCriterion("_name_id >=", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdLessThan(String value) {
            addCriterion("_name_id <", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdLessThanOrEqualTo(String value) {
            addCriterion("_name_id <=", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdLike(String value) {
            addCriterion("_name_id like", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdNotLike(String value) {
            addCriterion("_name_id not like", value, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdIn(List<String> values) {
            addCriterion("_name_id in", values, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdNotIn(List<String> values) {
            addCriterion("_name_id not in", values, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdBetween(String value1, String value2) {
            addCriterion("_name_id between", value1, value2, "nameId");
            return (Criteria) this;
        }

        public Criteria andNameIdNotBetween(String value1, String value2) {
            addCriterion("_name_id not between", value1, value2, "nameId");
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

        public Criteria andMigrationStatusIsNull() {
            addCriterion("migration_status is null");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusIsNotNull() {
            addCriterion("migration_status is not null");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusEqualTo(String value) {
            addCriterion("migration_status =", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusNotEqualTo(String value) {
            addCriterion("migration_status <>", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusGreaterThan(String value) {
            addCriterion("migration_status >", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusGreaterThanOrEqualTo(String value) {
            addCriterion("migration_status >=", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusLessThan(String value) {
            addCriterion("migration_status <", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusLessThanOrEqualTo(String value) {
            addCriterion("migration_status <=", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusLike(String value) {
            addCriterion("migration_status like", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusNotLike(String value) {
            addCriterion("migration_status not like", value, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusIn(List<String> values) {
            addCriterion("migration_status in", values, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusNotIn(List<String> values) {
            addCriterion("migration_status not in", values, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusBetween(String value1, String value2) {
            addCriterion("migration_status between", value1, value2, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andMigrationStatusNotBetween(String value1, String value2) {
            addCriterion("migration_status not between", value1, value2, "migrationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusIsNull() {
            addCriterion("replication_status is null");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusIsNotNull() {
            addCriterion("replication_status is not null");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusEqualTo(String value) {
            addCriterion("replication_status =", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusNotEqualTo(String value) {
            addCriterion("replication_status <>", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusGreaterThan(String value) {
            addCriterion("replication_status >", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusGreaterThanOrEqualTo(String value) {
            addCriterion("replication_status >=", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusLessThan(String value) {
            addCriterion("replication_status <", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusLessThanOrEqualTo(String value) {
            addCriterion("replication_status <=", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusLike(String value) {
            addCriterion("replication_status like", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusNotLike(String value) {
            addCriterion("replication_status not like", value, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusIn(List<String> values) {
            addCriterion("replication_status in", values, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusNotIn(List<String> values) {
            addCriterion("replication_status not in", values, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusBetween(String value1, String value2) {
            addCriterion("replication_status between", value1, value2, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationStatusNotBetween(String value1, String value2) {
            addCriterion("replication_status not between", value1, value2, "replicationStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusIsNull() {
            addCriterion("replication_extended_status is null");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusIsNotNull() {
            addCriterion("replication_extended_status is not null");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusEqualTo(String value) {
            addCriterion("replication_extended_status =", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusNotEqualTo(String value) {
            addCriterion("replication_extended_status <>", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusGreaterThan(String value) {
            addCriterion("replication_extended_status >", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusGreaterThanOrEqualTo(String value) {
            addCriterion("replication_extended_status >=", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusLessThan(String value) {
            addCriterion("replication_extended_status <", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusLessThanOrEqualTo(String value) {
            addCriterion("replication_extended_status <=", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusLike(String value) {
            addCriterion("replication_extended_status like", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusNotLike(String value) {
            addCriterion("replication_extended_status not like", value, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusIn(List<String> values) {
            addCriterion("replication_extended_status in", values, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusNotIn(List<String> values) {
            addCriterion("replication_extended_status not in", values, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusBetween(String value1, String value2) {
            addCriterion("replication_extended_status between", value1, value2, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationExtendedStatusNotBetween(String value1, String value2) {
            addCriterion("replication_extended_status not between", value1, value2, "replicationExtendedStatus");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataIsNull() {
            addCriterion("replication_driver_data is null");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataIsNotNull() {
            addCriterion("replication_driver_data is not null");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataEqualTo(String value) {
            addCriterion("replication_driver_data =", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataNotEqualTo(String value) {
            addCriterion("replication_driver_data <>", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataGreaterThan(String value) {
            addCriterion("replication_driver_data >", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataGreaterThanOrEqualTo(String value) {
            addCriterion("replication_driver_data >=", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataLessThan(String value) {
            addCriterion("replication_driver_data <", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataLessThanOrEqualTo(String value) {
            addCriterion("replication_driver_data <=", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataLike(String value) {
            addCriterion("replication_driver_data like", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataNotLike(String value) {
            addCriterion("replication_driver_data not like", value, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataIn(List<String> values) {
            addCriterion("replication_driver_data in", values, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataNotIn(List<String> values) {
            addCriterion("replication_driver_data not in", values, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataBetween(String value1, String value2) {
            addCriterion("replication_driver_data between", value1, value2, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andReplicationDriverDataNotBetween(String value1, String value2) {
            addCriterion("replication_driver_data not between", value1, value2, "replicationDriverData");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdIsNull() {
            addCriterion("consistencygroup_id is null");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdIsNotNull() {
            addCriterion("consistencygroup_id is not null");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdEqualTo(String value) {
            addCriterion("consistencygroup_id =", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdNotEqualTo(String value) {
            addCriterion("consistencygroup_id <>", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdGreaterThan(String value) {
            addCriterion("consistencygroup_id >", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdGreaterThanOrEqualTo(String value) {
            addCriterion("consistencygroup_id >=", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdLessThan(String value) {
            addCriterion("consistencygroup_id <", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdLessThanOrEqualTo(String value) {
            addCriterion("consistencygroup_id <=", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdLike(String value) {
            addCriterion("consistencygroup_id like", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdNotLike(String value) {
            addCriterion("consistencygroup_id not like", value, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdIn(List<String> values) {
            addCriterion("consistencygroup_id in", values, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdNotIn(List<String> values) {
            addCriterion("consistencygroup_id not in", values, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdBetween(String value1, String value2) {
            addCriterion("consistencygroup_id between", value1, value2, "consistencygroupId");
            return (Criteria) this;
        }

        public Criteria andConsistencygroupIdNotBetween(String value1, String value2) {
            addCriterion("consistencygroup_id not between", value1, value2, "consistencygroupId");
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

        public Criteria andMultiattchIsNull() {
            addCriterion("multiattch is null");
            return (Criteria) this;
        }

        public Criteria andMultiattchIsNotNull() {
            addCriterion("multiattch is not null");
            return (Criteria) this;
        }

        public Criteria andMultiattchEqualTo(Short value) {
            addCriterion("multiattch =", value, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchNotEqualTo(Short value) {
            addCriterion("multiattch <>", value, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchGreaterThan(Short value) {
            addCriterion("multiattch >", value, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchGreaterThanOrEqualTo(Short value) {
            addCriterion("multiattch >=", value, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchLessThan(Short value) {
            addCriterion("multiattch <", value, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchLessThanOrEqualTo(Short value) {
            addCriterion("multiattch <=", value, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchIn(List<Short> values) {
            addCriterion("multiattch in", values, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchNotIn(List<Short> values) {
            addCriterion("multiattch not in", values, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchBetween(Short value1, Short value2) {
            addCriterion("multiattch between", value1, value2, "multiattch");
            return (Criteria) this;
        }

        public Criteria andMultiattchNotBetween(Short value1, Short value2) {
            addCriterion("multiattch not between", value1, value2, "multiattch");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusIsNull() {
            addCriterion("previous_status is null");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusIsNotNull() {
            addCriterion("previous_status is not null");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusEqualTo(String value) {
            addCriterion("previous_status =", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusNotEqualTo(String value) {
            addCriterion("previous_status <>", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusGreaterThan(String value) {
            addCriterion("previous_status >", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusGreaterThanOrEqualTo(String value) {
            addCriterion("previous_status >=", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusLessThan(String value) {
            addCriterion("previous_status <", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusLessThanOrEqualTo(String value) {
            addCriterion("previous_status <=", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusLike(String value) {
            addCriterion("previous_status like", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusNotLike(String value) {
            addCriterion("previous_status not like", value, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusIn(List<String> values) {
            addCriterion("previous_status in", values, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusNotIn(List<String> values) {
            addCriterion("previous_status not in", values, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusBetween(String value1, String value2) {
            addCriterion("previous_status between", value1, value2, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andPreviousStatusNotBetween(String value1, String value2) {
            addCriterion("previous_status not between", value1, value2, "previousStatus");
            return (Criteria) this;
        }

        public Criteria andClusterNameIsNull() {
            addCriterion("cluster_name is null");
            return (Criteria) this;
        }

        public Criteria andClusterNameIsNotNull() {
            addCriterion("cluster_name is not null");
            return (Criteria) this;
        }

        public Criteria andClusterNameEqualTo(String value) {
            addCriterion("cluster_name =", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameNotEqualTo(String value) {
            addCriterion("cluster_name <>", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameGreaterThan(String value) {
            addCriterion("cluster_name >", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_name >=", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameLessThan(String value) {
            addCriterion("cluster_name <", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameLessThanOrEqualTo(String value) {
            addCriterion("cluster_name <=", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameLike(String value) {
            addCriterion("cluster_name like", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameNotLike(String value) {
            addCriterion("cluster_name not like", value, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameIn(List<String> values) {
            addCriterion("cluster_name in", values, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameNotIn(List<String> values) {
            addCriterion("cluster_name not in", values, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameBetween(String value1, String value2) {
            addCriterion("cluster_name between", value1, value2, "clusterName");
            return (Criteria) this;
        }

        public Criteria andClusterNameNotBetween(String value1, String value2) {
            addCriterion("cluster_name not between", value1, value2, "clusterName");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNull() {
            addCriterion("group_id is null");
            return (Criteria) this;
        }

        public Criteria andGroupIdIsNotNull() {
            addCriterion("group_id is not null");
            return (Criteria) this;
        }

        public Criteria andGroupIdEqualTo(String value) {
            addCriterion("group_id =", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotEqualTo(String value) {
            addCriterion("group_id <>", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThan(String value) {
            addCriterion("group_id >", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdGreaterThanOrEqualTo(String value) {
            addCriterion("group_id >=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThan(String value) {
            addCriterion("group_id <", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLessThanOrEqualTo(String value) {
            addCriterion("group_id <=", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdLike(String value) {
            addCriterion("group_id like", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotLike(String value) {
            addCriterion("group_id not like", value, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdIn(List<String> values) {
            addCriterion("group_id in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotIn(List<String> values) {
            addCriterion("group_id not in", values, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdBetween(String value1, String value2) {
            addCriterion("group_id between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andGroupIdNotBetween(String value1, String value2) {
            addCriterion("group_id not between", value1, value2, "groupId");
            return (Criteria) this;
        }

        public Criteria andServiceUuidIsNull() {
            addCriterion("service_uuid is null");
            return (Criteria) this;
        }

        public Criteria andServiceUuidIsNotNull() {
            addCriterion("service_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andServiceUuidEqualTo(String value) {
            addCriterion("service_uuid =", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidNotEqualTo(String value) {
            addCriterion("service_uuid <>", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidGreaterThan(String value) {
            addCriterion("service_uuid >", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidGreaterThanOrEqualTo(String value) {
            addCriterion("service_uuid >=", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidLessThan(String value) {
            addCriterion("service_uuid <", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidLessThanOrEqualTo(String value) {
            addCriterion("service_uuid <=", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidLike(String value) {
            addCriterion("service_uuid like", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidNotLike(String value) {
            addCriterion("service_uuid not like", value, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidIn(List<String> values) {
            addCriterion("service_uuid in", values, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidNotIn(List<String> values) {
            addCriterion("service_uuid not in", values, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidBetween(String value1, String value2) {
            addCriterion("service_uuid between", value1, value2, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andServiceUuidNotBetween(String value1, String value2) {
            addCriterion("service_uuid not between", value1, value2, "serviceUuid");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsIsNull() {
            addCriterion("shared_targets is null");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsIsNotNull() {
            addCriterion("shared_targets is not null");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsEqualTo(Short value) {
            addCriterion("shared_targets =", value, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsNotEqualTo(Short value) {
            addCriterion("shared_targets <>", value, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsGreaterThan(Short value) {
            addCriterion("shared_targets >", value, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsGreaterThanOrEqualTo(Short value) {
            addCriterion("shared_targets >=", value, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsLessThan(Short value) {
            addCriterion("shared_targets <", value, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsLessThanOrEqualTo(Short value) {
            addCriterion("shared_targets <=", value, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsIn(List<Short> values) {
            addCriterion("shared_targets in", values, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsNotIn(List<Short> values) {
            addCriterion("shared_targets not in", values, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsBetween(Short value1, Short value2) {
            addCriterion("shared_targets between", value1, value2, "sharedTargets");
            return (Criteria) this;
        }

        public Criteria andSharedTargetsNotBetween(Short value1, Short value2) {
            addCriterion("shared_targets not between", value1, value2, "sharedTargets");
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

        public Criteria andChargeTypeIsNull() {
            addCriterion("charge_type is null");
            return (Criteria) this;
        }

        public Criteria andChargeTypeIsNotNull() {
            addCriterion("charge_type is not null");
            return (Criteria) this;
        }

        public Criteria andChargeTypeEqualTo(Integer value) {
            addCriterion("charge_type =", value, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeNotEqualTo(Integer value) {
            addCriterion("charge_type <>", value, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeGreaterThan(Integer value) {
            addCriterion("charge_type >", value, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeGreaterThanOrEqualTo(Integer value) {
            addCriterion("charge_type >=", value, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeLessThan(Integer value) {
            addCriterion("charge_type <", value, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeLessThanOrEqualTo(Integer value) {
            addCriterion("charge_type <=", value, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeIn(List<Integer> values) {
            addCriterion("charge_type in", values, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeNotIn(List<Integer> values) {
            addCriterion("charge_type not in", values, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeBetween(Integer value1, Integer value2) {
            addCriterion("charge_type between", value1, value2, "chargeType");
            return (Criteria) this;
        }

        public Criteria andChargeTypeNotBetween(Integer value1, Integer value2) {
            addCriterion("charge_type not between", value1, value2, "chargeType");
            return (Criteria) this;
        }

        public Criteria andPriceUnitIsNull() {
            addCriterion("price_unit is null");
            return (Criteria) this;
        }

        public Criteria andPriceUnitIsNotNull() {
            addCriterion("price_unit is not null");
            return (Criteria) this;
        }

        public Criteria andPriceUnitEqualTo(Integer value) {
            addCriterion("price_unit =", value, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitNotEqualTo(Integer value) {
            addCriterion("price_unit <>", value, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitGreaterThan(Integer value) {
            addCriterion("price_unit >", value, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitGreaterThanOrEqualTo(Integer value) {
            addCriterion("price_unit >=", value, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitLessThan(Integer value) {
            addCriterion("price_unit <", value, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitLessThanOrEqualTo(Integer value) {
            addCriterion("price_unit <=", value, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitIn(List<Integer> values) {
            addCriterion("price_unit in", values, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitNotIn(List<Integer> values) {
            addCriterion("price_unit not in", values, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitBetween(Integer value1, Integer value2) {
            addCriterion("price_unit between", value1, value2, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPriceUnitNotBetween(Integer value1, Integer value2) {
            addCriterion("price_unit not between", value1, value2, "priceUnit");
            return (Criteria) this;
        }

        public Criteria andPeriodIsNull() {
            addCriterion("period is null");
            return (Criteria) this;
        }

        public Criteria andPeriodIsNotNull() {
            addCriterion("period is not null");
            return (Criteria) this;
        }

        public Criteria andPeriodEqualTo(Integer value) {
            addCriterion("period =", value, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodNotEqualTo(Integer value) {
            addCriterion("period <>", value, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodGreaterThan(Integer value) {
            addCriterion("period >", value, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodGreaterThanOrEqualTo(Integer value) {
            addCriterion("period >=", value, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodLessThan(Integer value) {
            addCriterion("period <", value, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodLessThanOrEqualTo(Integer value) {
            addCriterion("period <=", value, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodIn(List<Integer> values) {
            addCriterion("period in", values, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodNotIn(List<Integer> values) {
            addCriterion("period not in", values, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodBetween(Integer value1, Integer value2) {
            addCriterion("period between", value1, value2, "period");
            return (Criteria) this;
        }

        public Criteria andPeriodNotBetween(Integer value1, Integer value2) {
            addCriterion("period not between", value1, value2, "period");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataIsNull() {
            addCriterion("volume_glance_metadata is null");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataIsNotNull() {
            addCriterion("volume_glance_metadata is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataEqualTo(String value) {
            addCriterion("volume_glance_metadata =", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataNotEqualTo(String value) {
            addCriterion("volume_glance_metadata <>", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataGreaterThan(String value) {
            addCriterion("volume_glance_metadata >", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataGreaterThanOrEqualTo(String value) {
            addCriterion("volume_glance_metadata >=", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataLessThan(String value) {
            addCriterion("volume_glance_metadata <", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataLessThanOrEqualTo(String value) {
            addCriterion("volume_glance_metadata <=", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataLike(String value) {
            addCriterion("volume_glance_metadata like", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataNotLike(String value) {
            addCriterion("volume_glance_metadata not like", value, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataIn(List<String> values) {
            addCriterion("volume_glance_metadata in", values, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataNotIn(List<String> values) {
            addCriterion("volume_glance_metadata not in", values, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataBetween(String value1, String value2) {
            addCriterion("volume_glance_metadata between", value1, value2, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andVolumeGlanceMetadataNotBetween(String value1, String value2) {
            addCriterion("volume_glance_metadata not between", value1, value2, "volumeGlanceMetadata");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNull() {
            addCriterion("expire_time is null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIsNotNull() {
            addCriterion("expire_time is not null");
            return (Criteria) this;
        }

        public Criteria andExpireTimeEqualTo(Date value) {
            addCriterion("expire_time =", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotEqualTo(Date value) {
            addCriterion("expire_time <>", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThan(Date value) {
            addCriterion("expire_time >", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("expire_time >=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThan(Date value) {
            addCriterion("expire_time <", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeLessThanOrEqualTo(Date value) {
            addCriterion("expire_time <=", value, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeIn(List<Date> values) {
            addCriterion("expire_time in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotIn(List<Date> values) {
            addCriterion("expire_time not in", values, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeBetween(Date value1, Date value2) {
            addCriterion("expire_time between", value1, value2, "expireTime");
            return (Criteria) this;
        }

        public Criteria andExpireTimeNotBetween(Date value1, Date value2) {
            addCriterion("expire_time not between", value1, value2, "expireTime");
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