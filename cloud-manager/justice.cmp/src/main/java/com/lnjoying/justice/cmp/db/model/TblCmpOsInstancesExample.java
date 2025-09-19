package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsInstancesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsInstancesExample() {
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

        public Criteria andUuidIsNull() {
            addCriterion("uuid is null");
            return (Criteria) this;
        }

        public Criteria andUuidIsNotNull() {
            addCriterion("uuid is not null");
            return (Criteria) this;
        }

        public Criteria andUuidEqualTo(String value) {
            addCriterion("uuid =", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotEqualTo(String value) {
            addCriterion("uuid <>", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThan(String value) {
            addCriterion("uuid >", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidGreaterThanOrEqualTo(String value) {
            addCriterion("uuid >=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThan(String value) {
            addCriterion("uuid <", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLessThanOrEqualTo(String value) {
            addCriterion("uuid <=", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidLike(String value) {
            addCriterion("uuid like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotLike(String value) {
            addCriterion("uuid not like", value, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidIn(List<String> values) {
            addCriterion("uuid in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotIn(List<String> values) {
            addCriterion("uuid not in", values, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidBetween(String value1, String value2) {
            addCriterion("uuid between", value1, value2, "uuid");
            return (Criteria) this;
        }

        public Criteria andUuidNotBetween(String value1, String value2) {
            addCriterion("uuid not between", value1, value2, "uuid");
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andInternalIdIsNull() {
            addCriterion("internal_id is null");
            return (Criteria) this;
        }

        public Criteria andInternalIdIsNotNull() {
            addCriterion("internal_id is not null");
            return (Criteria) this;
        }

        public Criteria andInternalIdEqualTo(Integer value) {
            addCriterion("internal_id =", value, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdNotEqualTo(Integer value) {
            addCriterion("internal_id <>", value, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdGreaterThan(Integer value) {
            addCriterion("internal_id >", value, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("internal_id >=", value, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdLessThan(Integer value) {
            addCriterion("internal_id <", value, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdLessThanOrEqualTo(Integer value) {
            addCriterion("internal_id <=", value, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdIn(List<Integer> values) {
            addCriterion("internal_id in", values, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdNotIn(List<Integer> values) {
            addCriterion("internal_id not in", values, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdBetween(Integer value1, Integer value2) {
            addCriterion("internal_id between", value1, value2, "internalId");
            return (Criteria) this;
        }

        public Criteria andInternalIdNotBetween(Integer value1, Integer value2) {
            addCriterion("internal_id not between", value1, value2, "internalId");
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

        public Criteria andImageRefIsNull() {
            addCriterion("image_ref is null");
            return (Criteria) this;
        }

        public Criteria andImageRefIsNotNull() {
            addCriterion("image_ref is not null");
            return (Criteria) this;
        }

        public Criteria andImageRefEqualTo(String value) {
            addCriterion("image_ref =", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefNotEqualTo(String value) {
            addCriterion("image_ref <>", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefGreaterThan(String value) {
            addCriterion("image_ref >", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefGreaterThanOrEqualTo(String value) {
            addCriterion("image_ref >=", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefLessThan(String value) {
            addCriterion("image_ref <", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefLessThanOrEqualTo(String value) {
            addCriterion("image_ref <=", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefLike(String value) {
            addCriterion("image_ref like", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefNotLike(String value) {
            addCriterion("image_ref not like", value, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefIn(List<String> values) {
            addCriterion("image_ref in", values, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefNotIn(List<String> values) {
            addCriterion("image_ref not in", values, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefBetween(String value1, String value2) {
            addCriterion("image_ref between", value1, value2, "imageRef");
            return (Criteria) this;
        }

        public Criteria andImageRefNotBetween(String value1, String value2) {
            addCriterion("image_ref not between", value1, value2, "imageRef");
            return (Criteria) this;
        }

        public Criteria andKernelIdIsNull() {
            addCriterion("kernel_id is null");
            return (Criteria) this;
        }

        public Criteria andKernelIdIsNotNull() {
            addCriterion("kernel_id is not null");
            return (Criteria) this;
        }

        public Criteria andKernelIdEqualTo(String value) {
            addCriterion("kernel_id =", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdNotEqualTo(String value) {
            addCriterion("kernel_id <>", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdGreaterThan(String value) {
            addCriterion("kernel_id >", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdGreaterThanOrEqualTo(String value) {
            addCriterion("kernel_id >=", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdLessThan(String value) {
            addCriterion("kernel_id <", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdLessThanOrEqualTo(String value) {
            addCriterion("kernel_id <=", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdLike(String value) {
            addCriterion("kernel_id like", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdNotLike(String value) {
            addCriterion("kernel_id not like", value, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdIn(List<String> values) {
            addCriterion("kernel_id in", values, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdNotIn(List<String> values) {
            addCriterion("kernel_id not in", values, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdBetween(String value1, String value2) {
            addCriterion("kernel_id between", value1, value2, "kernelId");
            return (Criteria) this;
        }

        public Criteria andKernelIdNotBetween(String value1, String value2) {
            addCriterion("kernel_id not between", value1, value2, "kernelId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdIsNull() {
            addCriterion("ramdisk_id is null");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdIsNotNull() {
            addCriterion("ramdisk_id is not null");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdEqualTo(String value) {
            addCriterion("ramdisk_id =", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdNotEqualTo(String value) {
            addCriterion("ramdisk_id <>", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdGreaterThan(String value) {
            addCriterion("ramdisk_id >", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdGreaterThanOrEqualTo(String value) {
            addCriterion("ramdisk_id >=", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdLessThan(String value) {
            addCriterion("ramdisk_id <", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdLessThanOrEqualTo(String value) {
            addCriterion("ramdisk_id <=", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdLike(String value) {
            addCriterion("ramdisk_id like", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdNotLike(String value) {
            addCriterion("ramdisk_id not like", value, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdIn(List<String> values) {
            addCriterion("ramdisk_id in", values, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdNotIn(List<String> values) {
            addCriterion("ramdisk_id not in", values, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdBetween(String value1, String value2) {
            addCriterion("ramdisk_id between", value1, value2, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andRamdiskIdNotBetween(String value1, String value2) {
            addCriterion("ramdisk_id not between", value1, value2, "ramdiskId");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexIsNull() {
            addCriterion("launch_index is null");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexIsNotNull() {
            addCriterion("launch_index is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexEqualTo(Integer value) {
            addCriterion("launch_index =", value, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexNotEqualTo(Integer value) {
            addCriterion("launch_index <>", value, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexGreaterThan(Integer value) {
            addCriterion("launch_index >", value, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("launch_index >=", value, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexLessThan(Integer value) {
            addCriterion("launch_index <", value, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexLessThanOrEqualTo(Integer value) {
            addCriterion("launch_index <=", value, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexIn(List<Integer> values) {
            addCriterion("launch_index in", values, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexNotIn(List<Integer> values) {
            addCriterion("launch_index not in", values, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexBetween(Integer value1, Integer value2) {
            addCriterion("launch_index between", value1, value2, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andLaunchIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("launch_index not between", value1, value2, "launchIndex");
            return (Criteria) this;
        }

        public Criteria andKeyNameIsNull() {
            addCriterion("key_name is null");
            return (Criteria) this;
        }

        public Criteria andKeyNameIsNotNull() {
            addCriterion("key_name is not null");
            return (Criteria) this;
        }

        public Criteria andKeyNameEqualTo(String value) {
            addCriterion("key_name =", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotEqualTo(String value) {
            addCriterion("key_name <>", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameGreaterThan(String value) {
            addCriterion("key_name >", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameGreaterThanOrEqualTo(String value) {
            addCriterion("key_name >=", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameLessThan(String value) {
            addCriterion("key_name <", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameLessThanOrEqualTo(String value) {
            addCriterion("key_name <=", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameLike(String value) {
            addCriterion("key_name like", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotLike(String value) {
            addCriterion("key_name not like", value, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameIn(List<String> values) {
            addCriterion("key_name in", values, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotIn(List<String> values) {
            addCriterion("key_name not in", values, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameBetween(String value1, String value2) {
            addCriterion("key_name between", value1, value2, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyNameNotBetween(String value1, String value2) {
            addCriterion("key_name not between", value1, value2, "keyName");
            return (Criteria) this;
        }

        public Criteria andKeyDataIsNull() {
            addCriterion("key_data is null");
            return (Criteria) this;
        }

        public Criteria andKeyDataIsNotNull() {
            addCriterion("key_data is not null");
            return (Criteria) this;
        }

        public Criteria andKeyDataEqualTo(String value) {
            addCriterion("key_data =", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataNotEqualTo(String value) {
            addCriterion("key_data <>", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataGreaterThan(String value) {
            addCriterion("key_data >", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataGreaterThanOrEqualTo(String value) {
            addCriterion("key_data >=", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataLessThan(String value) {
            addCriterion("key_data <", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataLessThanOrEqualTo(String value) {
            addCriterion("key_data <=", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataLike(String value) {
            addCriterion("key_data like", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataNotLike(String value) {
            addCriterion("key_data not like", value, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataIn(List<String> values) {
            addCriterion("key_data in", values, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataNotIn(List<String> values) {
            addCriterion("key_data not in", values, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataBetween(String value1, String value2) {
            addCriterion("key_data between", value1, value2, "keyData");
            return (Criteria) this;
        }

        public Criteria andKeyDataNotBetween(String value1, String value2) {
            addCriterion("key_data not between", value1, value2, "keyData");
            return (Criteria) this;
        }

        public Criteria andPowerStateIsNull() {
            addCriterion("power_state is null");
            return (Criteria) this;
        }

        public Criteria andPowerStateIsNotNull() {
            addCriterion("power_state is not null");
            return (Criteria) this;
        }

        public Criteria andPowerStateEqualTo(Integer value) {
            addCriterion("power_state =", value, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateNotEqualTo(Integer value) {
            addCriterion("power_state <>", value, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateGreaterThan(Integer value) {
            addCriterion("power_state >", value, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateGreaterThanOrEqualTo(Integer value) {
            addCriterion("power_state >=", value, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateLessThan(Integer value) {
            addCriterion("power_state <", value, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateLessThanOrEqualTo(Integer value) {
            addCriterion("power_state <=", value, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateIn(List<Integer> values) {
            addCriterion("power_state in", values, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateNotIn(List<Integer> values) {
            addCriterion("power_state not in", values, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateBetween(Integer value1, Integer value2) {
            addCriterion("power_state between", value1, value2, "powerState");
            return (Criteria) this;
        }

        public Criteria andPowerStateNotBetween(Integer value1, Integer value2) {
            addCriterion("power_state not between", value1, value2, "powerState");
            return (Criteria) this;
        }

        public Criteria andVmStateIsNull() {
            addCriterion("vm_state is null");
            return (Criteria) this;
        }

        public Criteria andVmStateIsNotNull() {
            addCriterion("vm_state is not null");
            return (Criteria) this;
        }

        public Criteria andVmStateEqualTo(String value) {
            addCriterion("vm_state =", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateNotEqualTo(String value) {
            addCriterion("vm_state <>", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateGreaterThan(String value) {
            addCriterion("vm_state >", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateGreaterThanOrEqualTo(String value) {
            addCriterion("vm_state >=", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateLessThan(String value) {
            addCriterion("vm_state <", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateLessThanOrEqualTo(String value) {
            addCriterion("vm_state <=", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateLike(String value) {
            addCriterion("vm_state like", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateNotLike(String value) {
            addCriterion("vm_state not like", value, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateIn(List<String> values) {
            addCriterion("vm_state in", values, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateNotIn(List<String> values) {
            addCriterion("vm_state not in", values, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateBetween(String value1, String value2) {
            addCriterion("vm_state between", value1, value2, "vmState");
            return (Criteria) this;
        }

        public Criteria andVmStateNotBetween(String value1, String value2) {
            addCriterion("vm_state not between", value1, value2, "vmState");
            return (Criteria) this;
        }

        public Criteria andMemoryMbIsNull() {
            addCriterion("memory_mb is null");
            return (Criteria) this;
        }

        public Criteria andMemoryMbIsNotNull() {
            addCriterion("memory_mb is not null");
            return (Criteria) this;
        }

        public Criteria andMemoryMbEqualTo(Integer value) {
            addCriterion("memory_mb =", value, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbNotEqualTo(Integer value) {
            addCriterion("memory_mb <>", value, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbGreaterThan(Integer value) {
            addCriterion("memory_mb >", value, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbGreaterThanOrEqualTo(Integer value) {
            addCriterion("memory_mb >=", value, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbLessThan(Integer value) {
            addCriterion("memory_mb <", value, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbLessThanOrEqualTo(Integer value) {
            addCriterion("memory_mb <=", value, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbIn(List<Integer> values) {
            addCriterion("memory_mb in", values, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbNotIn(List<Integer> values) {
            addCriterion("memory_mb not in", values, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbBetween(Integer value1, Integer value2) {
            addCriterion("memory_mb between", value1, value2, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andMemoryMbNotBetween(Integer value1, Integer value2) {
            addCriterion("memory_mb not between", value1, value2, "memoryMb");
            return (Criteria) this;
        }

        public Criteria andVcpusIsNull() {
            addCriterion("vcpus is null");
            return (Criteria) this;
        }

        public Criteria andVcpusIsNotNull() {
            addCriterion("vcpus is not null");
            return (Criteria) this;
        }

        public Criteria andVcpusEqualTo(Integer value) {
            addCriterion("vcpus =", value, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusNotEqualTo(Integer value) {
            addCriterion("vcpus <>", value, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusGreaterThan(Integer value) {
            addCriterion("vcpus >", value, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusGreaterThanOrEqualTo(Integer value) {
            addCriterion("vcpus >=", value, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusLessThan(Integer value) {
            addCriterion("vcpus <", value, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusLessThanOrEqualTo(Integer value) {
            addCriterion("vcpus <=", value, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusIn(List<Integer> values) {
            addCriterion("vcpus in", values, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusNotIn(List<Integer> values) {
            addCriterion("vcpus not in", values, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusBetween(Integer value1, Integer value2) {
            addCriterion("vcpus between", value1, value2, "vcpus");
            return (Criteria) this;
        }

        public Criteria andVcpusNotBetween(Integer value1, Integer value2) {
            addCriterion("vcpus not between", value1, value2, "vcpus");
            return (Criteria) this;
        }

        public Criteria andHostnameIsNull() {
            addCriterion("hostname is null");
            return (Criteria) this;
        }

        public Criteria andHostnameIsNotNull() {
            addCriterion("hostname is not null");
            return (Criteria) this;
        }

        public Criteria andHostnameEqualTo(String value) {
            addCriterion("hostname =", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotEqualTo(String value) {
            addCriterion("hostname <>", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameGreaterThan(String value) {
            addCriterion("hostname >", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameGreaterThanOrEqualTo(String value) {
            addCriterion("hostname >=", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameLessThan(String value) {
            addCriterion("hostname <", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameLessThanOrEqualTo(String value) {
            addCriterion("hostname <=", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameLike(String value) {
            addCriterion("hostname like", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotLike(String value) {
            addCriterion("hostname not like", value, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameIn(List<String> values) {
            addCriterion("hostname in", values, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotIn(List<String> values) {
            addCriterion("hostname not in", values, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameBetween(String value1, String value2) {
            addCriterion("hostname between", value1, value2, "hostname");
            return (Criteria) this;
        }

        public Criteria andHostnameNotBetween(String value1, String value2) {
            addCriterion("hostname not between", value1, value2, "hostname");
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

        public Criteria andUserDataIsNull() {
            addCriterion("user_data is null");
            return (Criteria) this;
        }

        public Criteria andUserDataIsNotNull() {
            addCriterion("user_data is not null");
            return (Criteria) this;
        }

        public Criteria andUserDataEqualTo(String value) {
            addCriterion("user_data =", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataNotEqualTo(String value) {
            addCriterion("user_data <>", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataGreaterThan(String value) {
            addCriterion("user_data >", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataGreaterThanOrEqualTo(String value) {
            addCriterion("user_data >=", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataLessThan(String value) {
            addCriterion("user_data <", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataLessThanOrEqualTo(String value) {
            addCriterion("user_data <=", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataLike(String value) {
            addCriterion("user_data like", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataNotLike(String value) {
            addCriterion("user_data not like", value, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataIn(List<String> values) {
            addCriterion("user_data in", values, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataNotIn(List<String> values) {
            addCriterion("user_data not in", values, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataBetween(String value1, String value2) {
            addCriterion("user_data between", value1, value2, "userData");
            return (Criteria) this;
        }

        public Criteria andUserDataNotBetween(String value1, String value2) {
            addCriterion("user_data not between", value1, value2, "userData");
            return (Criteria) this;
        }

        public Criteria andReservationIdIsNull() {
            addCriterion("reservation_id is null");
            return (Criteria) this;
        }

        public Criteria andReservationIdIsNotNull() {
            addCriterion("reservation_id is not null");
            return (Criteria) this;
        }

        public Criteria andReservationIdEqualTo(String value) {
            addCriterion("reservation_id =", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdNotEqualTo(String value) {
            addCriterion("reservation_id <>", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdGreaterThan(String value) {
            addCriterion("reservation_id >", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdGreaterThanOrEqualTo(String value) {
            addCriterion("reservation_id >=", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdLessThan(String value) {
            addCriterion("reservation_id <", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdLessThanOrEqualTo(String value) {
            addCriterion("reservation_id <=", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdLike(String value) {
            addCriterion("reservation_id like", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdNotLike(String value) {
            addCriterion("reservation_id not like", value, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdIn(List<String> values) {
            addCriterion("reservation_id in", values, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdNotIn(List<String> values) {
            addCriterion("reservation_id not in", values, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdBetween(String value1, String value2) {
            addCriterion("reservation_id between", value1, value2, "reservationId");
            return (Criteria) this;
        }

        public Criteria andReservationIdNotBetween(String value1, String value2) {
            addCriterion("reservation_id not between", value1, value2, "reservationId");
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

        public Criteria andLockedIsNull() {
            addCriterion("locked is null");
            return (Criteria) this;
        }

        public Criteria andLockedIsNotNull() {
            addCriterion("locked is not null");
            return (Criteria) this;
        }

        public Criteria andLockedEqualTo(Short value) {
            addCriterion("locked =", value, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedNotEqualTo(Short value) {
            addCriterion("locked <>", value, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedGreaterThan(Short value) {
            addCriterion("locked >", value, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedGreaterThanOrEqualTo(Short value) {
            addCriterion("locked >=", value, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedLessThan(Short value) {
            addCriterion("locked <", value, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedLessThanOrEqualTo(Short value) {
            addCriterion("locked <=", value, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedIn(List<Short> values) {
            addCriterion("locked in", values, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedNotIn(List<Short> values) {
            addCriterion("locked not in", values, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedBetween(Short value1, Short value2) {
            addCriterion("locked between", value1, value2, "locked");
            return (Criteria) this;
        }

        public Criteria andLockedNotBetween(Short value1, Short value2) {
            addCriterion("locked not between", value1, value2, "locked");
            return (Criteria) this;
        }

        public Criteria andOsTypeIsNull() {
            addCriterion("os_type is null");
            return (Criteria) this;
        }

        public Criteria andOsTypeIsNotNull() {
            addCriterion("os_type is not null");
            return (Criteria) this;
        }

        public Criteria andOsTypeEqualTo(String value) {
            addCriterion("os_type =", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotEqualTo(String value) {
            addCriterion("os_type <>", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeGreaterThan(String value) {
            addCriterion("os_type >", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeGreaterThanOrEqualTo(String value) {
            addCriterion("os_type >=", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLessThan(String value) {
            addCriterion("os_type <", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLessThanOrEqualTo(String value) {
            addCriterion("os_type <=", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLike(String value) {
            addCriterion("os_type like", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotLike(String value) {
            addCriterion("os_type not like", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeIn(List<String> values) {
            addCriterion("os_type in", values, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotIn(List<String> values) {
            addCriterion("os_type not in", values, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeBetween(String value1, String value2) {
            addCriterion("os_type between", value1, value2, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotBetween(String value1, String value2) {
            addCriterion("os_type not between", value1, value2, "osType");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnIsNull() {
            addCriterion("launched_on is null");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnIsNotNull() {
            addCriterion("launched_on is not null");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnEqualTo(String value) {
            addCriterion("launched_on =", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnNotEqualTo(String value) {
            addCriterion("launched_on <>", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnGreaterThan(String value) {
            addCriterion("launched_on >", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnGreaterThanOrEqualTo(String value) {
            addCriterion("launched_on >=", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnLessThan(String value) {
            addCriterion("launched_on <", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnLessThanOrEqualTo(String value) {
            addCriterion("launched_on <=", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnLike(String value) {
            addCriterion("launched_on like", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnNotLike(String value) {
            addCriterion("launched_on not like", value, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnIn(List<String> values) {
            addCriterion("launched_on in", values, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnNotIn(List<String> values) {
            addCriterion("launched_on not in", values, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnBetween(String value1, String value2) {
            addCriterion("launched_on between", value1, value2, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andLaunchedOnNotBetween(String value1, String value2) {
            addCriterion("launched_on not between", value1, value2, "launchedOn");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdIsNull() {
            addCriterion("instance_type_id is null");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdIsNotNull() {
            addCriterion("instance_type_id is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdEqualTo(String value) {
            addCriterion("instance_type_id =", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdNotEqualTo(String value) {
            addCriterion("instance_type_id <>", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdGreaterThan(String value) {
            addCriterion("instance_type_id >", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdGreaterThanOrEqualTo(String value) {
            addCriterion("instance_type_id >=", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdLessThan(String value) {
            addCriterion("instance_type_id <", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdLessThanOrEqualTo(String value) {
            addCriterion("instance_type_id <=", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdLike(String value) {
            addCriterion("instance_type_id like", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdNotLike(String value) {
            addCriterion("instance_type_id not like", value, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdIn(List<String> values) {
            addCriterion("instance_type_id in", values, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdNotIn(List<String> values) {
            addCriterion("instance_type_id not in", values, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdBetween(String value1, String value2) {
            addCriterion("instance_type_id between", value1, value2, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andInstanceTypeIdNotBetween(String value1, String value2) {
            addCriterion("instance_type_id not between", value1, value2, "instanceTypeId");
            return (Criteria) this;
        }

        public Criteria andVmModeIsNull() {
            addCriterion("vm_mode is null");
            return (Criteria) this;
        }

        public Criteria andVmModeIsNotNull() {
            addCriterion("vm_mode is not null");
            return (Criteria) this;
        }

        public Criteria andVmModeEqualTo(String value) {
            addCriterion("vm_mode =", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeNotEqualTo(String value) {
            addCriterion("vm_mode <>", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeGreaterThan(String value) {
            addCriterion("vm_mode >", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeGreaterThanOrEqualTo(String value) {
            addCriterion("vm_mode >=", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeLessThan(String value) {
            addCriterion("vm_mode <", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeLessThanOrEqualTo(String value) {
            addCriterion("vm_mode <=", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeLike(String value) {
            addCriterion("vm_mode like", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeNotLike(String value) {
            addCriterion("vm_mode not like", value, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeIn(List<String> values) {
            addCriterion("vm_mode in", values, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeNotIn(List<String> values) {
            addCriterion("vm_mode not in", values, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeBetween(String value1, String value2) {
            addCriterion("vm_mode between", value1, value2, "vmMode");
            return (Criteria) this;
        }

        public Criteria andVmModeNotBetween(String value1, String value2) {
            addCriterion("vm_mode not between", value1, value2, "vmMode");
            return (Criteria) this;
        }

        public Criteria andArchitectureIsNull() {
            addCriterion("architecture is null");
            return (Criteria) this;
        }

        public Criteria andArchitectureIsNotNull() {
            addCriterion("architecture is not null");
            return (Criteria) this;
        }

        public Criteria andArchitectureEqualTo(String value) {
            addCriterion("architecture =", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotEqualTo(String value) {
            addCriterion("architecture <>", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureGreaterThan(String value) {
            addCriterion("architecture >", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureGreaterThanOrEqualTo(String value) {
            addCriterion("architecture >=", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureLessThan(String value) {
            addCriterion("architecture <", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureLessThanOrEqualTo(String value) {
            addCriterion("architecture <=", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureLike(String value) {
            addCriterion("architecture like", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotLike(String value) {
            addCriterion("architecture not like", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureIn(List<String> values) {
            addCriterion("architecture in", values, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotIn(List<String> values) {
            addCriterion("architecture not in", values, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureBetween(String value1, String value2) {
            addCriterion("architecture between", value1, value2, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotBetween(String value1, String value2) {
            addCriterion("architecture not between", value1, value2, "architecture");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameIsNull() {
            addCriterion("root_device_name is null");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameIsNotNull() {
            addCriterion("root_device_name is not null");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameEqualTo(String value) {
            addCriterion("root_device_name =", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameNotEqualTo(String value) {
            addCriterion("root_device_name <>", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameGreaterThan(String value) {
            addCriterion("root_device_name >", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameGreaterThanOrEqualTo(String value) {
            addCriterion("root_device_name >=", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameLessThan(String value) {
            addCriterion("root_device_name <", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameLessThanOrEqualTo(String value) {
            addCriterion("root_device_name <=", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameLike(String value) {
            addCriterion("root_device_name like", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameNotLike(String value) {
            addCriterion("root_device_name not like", value, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameIn(List<String> values) {
            addCriterion("root_device_name in", values, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameNotIn(List<String> values) {
            addCriterion("root_device_name not in", values, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameBetween(String value1, String value2) {
            addCriterion("root_device_name between", value1, value2, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andRootDeviceNameNotBetween(String value1, String value2) {
            addCriterion("root_device_name not between", value1, value2, "rootDeviceName");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4IsNull() {
            addCriterion("access_ip_v4 is null");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4IsNotNull() {
            addCriterion("access_ip_v4 is not null");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4EqualTo(String value) {
            addCriterion("access_ip_v4 =", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4NotEqualTo(String value) {
            addCriterion("access_ip_v4 <>", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4GreaterThan(String value) {
            addCriterion("access_ip_v4 >", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4GreaterThanOrEqualTo(String value) {
            addCriterion("access_ip_v4 >=", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4LessThan(String value) {
            addCriterion("access_ip_v4 <", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4LessThanOrEqualTo(String value) {
            addCriterion("access_ip_v4 <=", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4Like(String value) {
            addCriterion("access_ip_v4 like", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4NotLike(String value) {
            addCriterion("access_ip_v4 not like", value, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4In(List<String> values) {
            addCriterion("access_ip_v4 in", values, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4NotIn(List<String> values) {
            addCriterion("access_ip_v4 not in", values, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4Between(String value1, String value2) {
            addCriterion("access_ip_v4 between", value1, value2, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV4NotBetween(String value1, String value2) {
            addCriterion("access_ip_v4 not between", value1, value2, "accessIpV4");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6IsNull() {
            addCriterion("access_ip_v6 is null");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6IsNotNull() {
            addCriterion("access_ip_v6 is not null");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6EqualTo(String value) {
            addCriterion("access_ip_v6 =", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6NotEqualTo(String value) {
            addCriterion("access_ip_v6 <>", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6GreaterThan(String value) {
            addCriterion("access_ip_v6 >", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6GreaterThanOrEqualTo(String value) {
            addCriterion("access_ip_v6 >=", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6LessThan(String value) {
            addCriterion("access_ip_v6 <", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6LessThanOrEqualTo(String value) {
            addCriterion("access_ip_v6 <=", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6Like(String value) {
            addCriterion("access_ip_v6 like", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6NotLike(String value) {
            addCriterion("access_ip_v6 not like", value, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6In(List<String> values) {
            addCriterion("access_ip_v6 in", values, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6NotIn(List<String> values) {
            addCriterion("access_ip_v6 not in", values, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6Between(String value1, String value2) {
            addCriterion("access_ip_v6 between", value1, value2, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andAccessIpV6NotBetween(String value1, String value2) {
            addCriterion("access_ip_v6 not between", value1, value2, "accessIpV6");
            return (Criteria) this;
        }

        public Criteria andConfigDriveIsNull() {
            addCriterion("config_drive is null");
            return (Criteria) this;
        }

        public Criteria andConfigDriveIsNotNull() {
            addCriterion("config_drive is not null");
            return (Criteria) this;
        }

        public Criteria andConfigDriveEqualTo(String value) {
            addCriterion("config_drive =", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveNotEqualTo(String value) {
            addCriterion("config_drive <>", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveGreaterThan(String value) {
            addCriterion("config_drive >", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveGreaterThanOrEqualTo(String value) {
            addCriterion("config_drive >=", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveLessThan(String value) {
            addCriterion("config_drive <", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveLessThanOrEqualTo(String value) {
            addCriterion("config_drive <=", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveLike(String value) {
            addCriterion("config_drive like", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveNotLike(String value) {
            addCriterion("config_drive not like", value, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveIn(List<String> values) {
            addCriterion("config_drive in", values, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveNotIn(List<String> values) {
            addCriterion("config_drive not in", values, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveBetween(String value1, String value2) {
            addCriterion("config_drive between", value1, value2, "configDrive");
            return (Criteria) this;
        }

        public Criteria andConfigDriveNotBetween(String value1, String value2) {
            addCriterion("config_drive not between", value1, value2, "configDrive");
            return (Criteria) this;
        }

        public Criteria andTaskStateIsNull() {
            addCriterion("task_state is null");
            return (Criteria) this;
        }

        public Criteria andTaskStateIsNotNull() {
            addCriterion("task_state is not null");
            return (Criteria) this;
        }

        public Criteria andTaskStateEqualTo(String value) {
            addCriterion("task_state =", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotEqualTo(String value) {
            addCriterion("task_state <>", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateGreaterThan(String value) {
            addCriterion("task_state >", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateGreaterThanOrEqualTo(String value) {
            addCriterion("task_state >=", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateLessThan(String value) {
            addCriterion("task_state <", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateLessThanOrEqualTo(String value) {
            addCriterion("task_state <=", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateLike(String value) {
            addCriterion("task_state like", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotLike(String value) {
            addCriterion("task_state not like", value, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateIn(List<String> values) {
            addCriterion("task_state in", values, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotIn(List<String> values) {
            addCriterion("task_state not in", values, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateBetween(String value1, String value2) {
            addCriterion("task_state between", value1, value2, "taskState");
            return (Criteria) this;
        }

        public Criteria andTaskStateNotBetween(String value1, String value2) {
            addCriterion("task_state not between", value1, value2, "taskState");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceIsNull() {
            addCriterion("default_ephemeral_device is null");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceIsNotNull() {
            addCriterion("default_ephemeral_device is not null");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceEqualTo(String value) {
            addCriterion("default_ephemeral_device =", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceNotEqualTo(String value) {
            addCriterion("default_ephemeral_device <>", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceGreaterThan(String value) {
            addCriterion("default_ephemeral_device >", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceGreaterThanOrEqualTo(String value) {
            addCriterion("default_ephemeral_device >=", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceLessThan(String value) {
            addCriterion("default_ephemeral_device <", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceLessThanOrEqualTo(String value) {
            addCriterion("default_ephemeral_device <=", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceLike(String value) {
            addCriterion("default_ephemeral_device like", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceNotLike(String value) {
            addCriterion("default_ephemeral_device not like", value, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceIn(List<String> values) {
            addCriterion("default_ephemeral_device in", values, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceNotIn(List<String> values) {
            addCriterion("default_ephemeral_device not in", values, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceBetween(String value1, String value2) {
            addCriterion("default_ephemeral_device between", value1, value2, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultEphemeralDeviceNotBetween(String value1, String value2) {
            addCriterion("default_ephemeral_device not between", value1, value2, "defaultEphemeralDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceIsNull() {
            addCriterion("default_swap_device is null");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceIsNotNull() {
            addCriterion("default_swap_device is not null");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceEqualTo(String value) {
            addCriterion("default_swap_device =", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceNotEqualTo(String value) {
            addCriterion("default_swap_device <>", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceGreaterThan(String value) {
            addCriterion("default_swap_device >", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceGreaterThanOrEqualTo(String value) {
            addCriterion("default_swap_device >=", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceLessThan(String value) {
            addCriterion("default_swap_device <", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceLessThanOrEqualTo(String value) {
            addCriterion("default_swap_device <=", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceLike(String value) {
            addCriterion("default_swap_device like", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceNotLike(String value) {
            addCriterion("default_swap_device not like", value, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceIn(List<String> values) {
            addCriterion("default_swap_device in", values, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceNotIn(List<String> values) {
            addCriterion("default_swap_device not in", values, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceBetween(String value1, String value2) {
            addCriterion("default_swap_device between", value1, value2, "defaultSwapDevice");
            return (Criteria) this;
        }

        public Criteria andDefaultSwapDeviceNotBetween(String value1, String value2) {
            addCriterion("default_swap_device not between", value1, value2, "defaultSwapDevice");
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

        public Criteria andProgressEqualTo(Integer value) {
            addCriterion("progress =", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotEqualTo(Integer value) {
            addCriterion("progress <>", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThan(Integer value) {
            addCriterion("progress >", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressGreaterThanOrEqualTo(Integer value) {
            addCriterion("progress >=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThan(Integer value) {
            addCriterion("progress <", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressLessThanOrEqualTo(Integer value) {
            addCriterion("progress <=", value, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressIn(List<Integer> values) {
            addCriterion("progress in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotIn(List<Integer> values) {
            addCriterion("progress not in", values, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressBetween(Integer value1, Integer value2) {
            addCriterion("progress between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andProgressNotBetween(Integer value1, Integer value2) {
            addCriterion("progress not between", value1, value2, "progress");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigIsNull() {
            addCriterion("auto_disk_config is null");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigIsNotNull() {
            addCriterion("auto_disk_config is not null");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigEqualTo(Short value) {
            addCriterion("auto_disk_config =", value, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigNotEqualTo(Short value) {
            addCriterion("auto_disk_config <>", value, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigGreaterThan(Short value) {
            addCriterion("auto_disk_config >", value, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigGreaterThanOrEqualTo(Short value) {
            addCriterion("auto_disk_config >=", value, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigLessThan(Short value) {
            addCriterion("auto_disk_config <", value, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigLessThanOrEqualTo(Short value) {
            addCriterion("auto_disk_config <=", value, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigIn(List<Short> values) {
            addCriterion("auto_disk_config in", values, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigNotIn(List<Short> values) {
            addCriterion("auto_disk_config not in", values, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigBetween(Short value1, Short value2) {
            addCriterion("auto_disk_config between", value1, value2, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andAutoDiskConfigNotBetween(Short value1, Short value2) {
            addCriterion("auto_disk_config not between", value1, value2, "autoDiskConfig");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateIsNull() {
            addCriterion("shutdown_terminate is null");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateIsNotNull() {
            addCriterion("shutdown_terminate is not null");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateEqualTo(Short value) {
            addCriterion("shutdown_terminate =", value, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateNotEqualTo(Short value) {
            addCriterion("shutdown_terminate <>", value, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateGreaterThan(Short value) {
            addCriterion("shutdown_terminate >", value, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateGreaterThanOrEqualTo(Short value) {
            addCriterion("shutdown_terminate >=", value, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateLessThan(Short value) {
            addCriterion("shutdown_terminate <", value, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateLessThanOrEqualTo(Short value) {
            addCriterion("shutdown_terminate <=", value, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateIn(List<Short> values) {
            addCriterion("shutdown_terminate in", values, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateNotIn(List<Short> values) {
            addCriterion("shutdown_terminate not in", values, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateBetween(Short value1, Short value2) {
            addCriterion("shutdown_terminate between", value1, value2, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andShutdownTerminateNotBetween(Short value1, Short value2) {
            addCriterion("shutdown_terminate not between", value1, value2, "shutdownTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateIsNull() {
            addCriterion("disable_terminate is null");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateIsNotNull() {
            addCriterion("disable_terminate is not null");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateEqualTo(Short value) {
            addCriterion("disable_terminate =", value, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateNotEqualTo(Short value) {
            addCriterion("disable_terminate <>", value, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateGreaterThan(Short value) {
            addCriterion("disable_terminate >", value, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateGreaterThanOrEqualTo(Short value) {
            addCriterion("disable_terminate >=", value, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateLessThan(Short value) {
            addCriterion("disable_terminate <", value, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateLessThanOrEqualTo(Short value) {
            addCriterion("disable_terminate <=", value, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateIn(List<Short> values) {
            addCriterion("disable_terminate in", values, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateNotIn(List<Short> values) {
            addCriterion("disable_terminate not in", values, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateBetween(Short value1, Short value2) {
            addCriterion("disable_terminate between", value1, value2, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andDisableTerminateNotBetween(Short value1, Short value2) {
            addCriterion("disable_terminate not between", value1, value2, "disableTerminate");
            return (Criteria) this;
        }

        public Criteria andRootGbIsNull() {
            addCriterion("root_gb is null");
            return (Criteria) this;
        }

        public Criteria andRootGbIsNotNull() {
            addCriterion("root_gb is not null");
            return (Criteria) this;
        }

        public Criteria andRootGbEqualTo(Integer value) {
            addCriterion("root_gb =", value, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbNotEqualTo(Integer value) {
            addCriterion("root_gb <>", value, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbGreaterThan(Integer value) {
            addCriterion("root_gb >", value, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbGreaterThanOrEqualTo(Integer value) {
            addCriterion("root_gb >=", value, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbLessThan(Integer value) {
            addCriterion("root_gb <", value, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbLessThanOrEqualTo(Integer value) {
            addCriterion("root_gb <=", value, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbIn(List<Integer> values) {
            addCriterion("root_gb in", values, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbNotIn(List<Integer> values) {
            addCriterion("root_gb not in", values, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbBetween(Integer value1, Integer value2) {
            addCriterion("root_gb between", value1, value2, "rootGb");
            return (Criteria) this;
        }

        public Criteria andRootGbNotBetween(Integer value1, Integer value2) {
            addCriterion("root_gb not between", value1, value2, "rootGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbIsNull() {
            addCriterion("ephemeral_gb is null");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbIsNotNull() {
            addCriterion("ephemeral_gb is not null");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbEqualTo(Integer value) {
            addCriterion("ephemeral_gb =", value, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbNotEqualTo(Integer value) {
            addCriterion("ephemeral_gb <>", value, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbGreaterThan(Integer value) {
            addCriterion("ephemeral_gb >", value, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbGreaterThanOrEqualTo(Integer value) {
            addCriterion("ephemeral_gb >=", value, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbLessThan(Integer value) {
            addCriterion("ephemeral_gb <", value, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbLessThanOrEqualTo(Integer value) {
            addCriterion("ephemeral_gb <=", value, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbIn(List<Integer> values) {
            addCriterion("ephemeral_gb in", values, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbNotIn(List<Integer> values) {
            addCriterion("ephemeral_gb not in", values, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbBetween(Integer value1, Integer value2) {
            addCriterion("ephemeral_gb between", value1, value2, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andEphemeralGbNotBetween(Integer value1, Integer value2) {
            addCriterion("ephemeral_gb not between", value1, value2, "ephemeralGb");
            return (Criteria) this;
        }

        public Criteria andCellNameIsNull() {
            addCriterion("cell_name is null");
            return (Criteria) this;
        }

        public Criteria andCellNameIsNotNull() {
            addCriterion("cell_name is not null");
            return (Criteria) this;
        }

        public Criteria andCellNameEqualTo(String value) {
            addCriterion("cell_name =", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotEqualTo(String value) {
            addCriterion("cell_name <>", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameGreaterThan(String value) {
            addCriterion("cell_name >", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameGreaterThanOrEqualTo(String value) {
            addCriterion("cell_name >=", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameLessThan(String value) {
            addCriterion("cell_name <", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameLessThanOrEqualTo(String value) {
            addCriterion("cell_name <=", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameLike(String value) {
            addCriterion("cell_name like", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotLike(String value) {
            addCriterion("cell_name not like", value, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameIn(List<String> values) {
            addCriterion("cell_name in", values, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotIn(List<String> values) {
            addCriterion("cell_name not in", values, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameBetween(String value1, String value2) {
            addCriterion("cell_name between", value1, value2, "cellName");
            return (Criteria) this;
        }

        public Criteria andCellNameNotBetween(String value1, String value2) {
            addCriterion("cell_name not between", value1, value2, "cellName");
            return (Criteria) this;
        }

        public Criteria andNodeIsNull() {
            addCriterion("node is null");
            return (Criteria) this;
        }

        public Criteria andNodeIsNotNull() {
            addCriterion("node is not null");
            return (Criteria) this;
        }

        public Criteria andNodeEqualTo(String value) {
            addCriterion("node =", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeNotEqualTo(String value) {
            addCriterion("node <>", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeGreaterThan(String value) {
            addCriterion("node >", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeGreaterThanOrEqualTo(String value) {
            addCriterion("node >=", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeLessThan(String value) {
            addCriterion("node <", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeLessThanOrEqualTo(String value) {
            addCriterion("node <=", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeLike(String value) {
            addCriterion("node like", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeNotLike(String value) {
            addCriterion("node not like", value, "node");
            return (Criteria) this;
        }

        public Criteria andNodeIn(List<String> values) {
            addCriterion("node in", values, "node");
            return (Criteria) this;
        }

        public Criteria andNodeNotIn(List<String> values) {
            addCriterion("node not in", values, "node");
            return (Criteria) this;
        }

        public Criteria andNodeBetween(String value1, String value2) {
            addCriterion("node between", value1, value2, "node");
            return (Criteria) this;
        }

        public Criteria andNodeNotBetween(String value1, String value2) {
            addCriterion("node not between", value1, value2, "node");
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

        public Criteria andDeletedEqualTo(Integer value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Integer value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Integer value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Integer value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Integer value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Integer value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Integer> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Integer> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Integer value1, Integer value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Integer value1, Integer value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andLockedByIsNull() {
            addCriterion("locked_by is null");
            return (Criteria) this;
        }

        public Criteria andLockedByIsNotNull() {
            addCriterion("locked_by is not null");
            return (Criteria) this;
        }

        public Criteria andLockedByEqualTo(String value) {
            addCriterion("locked_by =", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByNotEqualTo(String value) {
            addCriterion("locked_by <>", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByGreaterThan(String value) {
            addCriterion("locked_by >", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByGreaterThanOrEqualTo(String value) {
            addCriterion("locked_by >=", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByLessThan(String value) {
            addCriterion("locked_by <", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByLessThanOrEqualTo(String value) {
            addCriterion("locked_by <=", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByLike(String value) {
            addCriterion("locked_by like", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByNotLike(String value) {
            addCriterion("locked_by not like", value, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByIn(List<String> values) {
            addCriterion("locked_by in", values, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByNotIn(List<String> values) {
            addCriterion("locked_by not in", values, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByBetween(String value1, String value2) {
            addCriterion("locked_by between", value1, value2, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andLockedByNotBetween(String value1, String value2) {
            addCriterion("locked_by not between", value1, value2, "lockedBy");
            return (Criteria) this;
        }

        public Criteria andCleandIsNull() {
            addCriterion("cleand is null");
            return (Criteria) this;
        }

        public Criteria andCleandIsNotNull() {
            addCriterion("cleand is not null");
            return (Criteria) this;
        }

        public Criteria andCleandEqualTo(Integer value) {
            addCriterion("cleand =", value, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandNotEqualTo(Integer value) {
            addCriterion("cleand <>", value, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandGreaterThan(Integer value) {
            addCriterion("cleand >", value, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandGreaterThanOrEqualTo(Integer value) {
            addCriterion("cleand >=", value, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandLessThan(Integer value) {
            addCriterion("cleand <", value, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandLessThanOrEqualTo(Integer value) {
            addCriterion("cleand <=", value, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandIn(List<Integer> values) {
            addCriterion("cleand in", values, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandNotIn(List<Integer> values) {
            addCriterion("cleand not in", values, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandBetween(Integer value1, Integer value2) {
            addCriterion("cleand between", value1, value2, "cleand");
            return (Criteria) this;
        }

        public Criteria andCleandNotBetween(Integer value1, Integer value2) {
            addCriterion("cleand not between", value1, value2, "cleand");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidIsNull() {
            addCriterion("ephemeral_key_uuid is null");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidIsNotNull() {
            addCriterion("ephemeral_key_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidEqualTo(String value) {
            addCriterion("ephemeral_key_uuid =", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidNotEqualTo(String value) {
            addCriterion("ephemeral_key_uuid <>", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidGreaterThan(String value) {
            addCriterion("ephemeral_key_uuid >", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidGreaterThanOrEqualTo(String value) {
            addCriterion("ephemeral_key_uuid >=", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidLessThan(String value) {
            addCriterion("ephemeral_key_uuid <", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidLessThanOrEqualTo(String value) {
            addCriterion("ephemeral_key_uuid <=", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidLike(String value) {
            addCriterion("ephemeral_key_uuid like", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidNotLike(String value) {
            addCriterion("ephemeral_key_uuid not like", value, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidIn(List<String> values) {
            addCriterion("ephemeral_key_uuid in", values, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidNotIn(List<String> values) {
            addCriterion("ephemeral_key_uuid not in", values, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidBetween(String value1, String value2) {
            addCriterion("ephemeral_key_uuid between", value1, value2, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andEphemeralKeyUuidNotBetween(String value1, String value2) {
            addCriterion("ephemeral_key_uuid not between", value1, value2, "ephemeralKeyUuid");
            return (Criteria) this;
        }

        public Criteria andHiddenIsNull() {
            addCriterion("hidden is null");
            return (Criteria) this;
        }

        public Criteria andHiddenIsNotNull() {
            addCriterion("hidden is not null");
            return (Criteria) this;
        }

        public Criteria andHiddenEqualTo(Short value) {
            addCriterion("hidden =", value, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenNotEqualTo(Short value) {
            addCriterion("hidden <>", value, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenGreaterThan(Short value) {
            addCriterion("hidden >", value, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenGreaterThanOrEqualTo(Short value) {
            addCriterion("hidden >=", value, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenLessThan(Short value) {
            addCriterion("hidden <", value, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenLessThanOrEqualTo(Short value) {
            addCriterion("hidden <=", value, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenIn(List<Short> values) {
            addCriterion("hidden in", values, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenNotIn(List<Short> values) {
            addCriterion("hidden not in", values, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenBetween(Short value1, Short value2) {
            addCriterion("hidden between", value1, value2, "hidden");
            return (Criteria) this;
        }

        public Criteria andHiddenNotBetween(Short value1, Short value2) {
            addCriterion("hidden not between", value1, value2, "hidden");
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

        public Criteria andNetworkInfoIsNull() {
            addCriterion("network_info is null");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoIsNotNull() {
            addCriterion("network_info is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoEqualTo(String value) {
            addCriterion("network_info =", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoNotEqualTo(String value) {
            addCriterion("network_info <>", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoGreaterThan(String value) {
            addCriterion("network_info >", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoGreaterThanOrEqualTo(String value) {
            addCriterion("network_info >=", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoLessThan(String value) {
            addCriterion("network_info <", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoLessThanOrEqualTo(String value) {
            addCriterion("network_info <=", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoLike(String value) {
            addCriterion("network_info like", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoNotLike(String value) {
            addCriterion("network_info not like", value, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoIn(List<String> values) {
            addCriterion("network_info in", values, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoNotIn(List<String> values) {
            addCriterion("network_info not in", values, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoBetween(String value1, String value2) {
            addCriterion("network_info between", value1, value2, "networkInfo");
            return (Criteria) this;
        }

        public Criteria andNetworkInfoNotBetween(String value1, String value2) {
            addCriterion("network_info not between", value1, value2, "networkInfo");
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