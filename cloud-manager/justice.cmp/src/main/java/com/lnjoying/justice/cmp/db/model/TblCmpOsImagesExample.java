package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsImagesExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsImagesExample() {
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

        public Criteria andSizeIsNull() {
            addCriterion("size is null");
            return (Criteria) this;
        }

        public Criteria andSizeIsNotNull() {
            addCriterion("size is not null");
            return (Criteria) this;
        }

        public Criteria andSizeEqualTo(Long value) {
            addCriterion("size =", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotEqualTo(Long value) {
            addCriterion("size <>", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThan(Long value) {
            addCriterion("size >", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("size >=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThan(Long value) {
            addCriterion("size <", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeLessThanOrEqualTo(Long value) {
            addCriterion("size <=", value, "size");
            return (Criteria) this;
        }

        public Criteria andSizeIn(List<Long> values) {
            addCriterion("size in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotIn(List<Long> values) {
            addCriterion("size not in", values, "size");
            return (Criteria) this;
        }

        public Criteria andSizeBetween(Long value1, Long value2) {
            addCriterion("size between", value1, value2, "size");
            return (Criteria) this;
        }

        public Criteria andSizeNotBetween(Long value1, Long value2) {
            addCriterion("size not between", value1, value2, "size");
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

        public Criteria andDiskFormatIsNull() {
            addCriterion("disk_format is null");
            return (Criteria) this;
        }

        public Criteria andDiskFormatIsNotNull() {
            addCriterion("disk_format is not null");
            return (Criteria) this;
        }

        public Criteria andDiskFormatEqualTo(String value) {
            addCriterion("disk_format =", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatNotEqualTo(String value) {
            addCriterion("disk_format <>", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatGreaterThan(String value) {
            addCriterion("disk_format >", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatGreaterThanOrEqualTo(String value) {
            addCriterion("disk_format >=", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatLessThan(String value) {
            addCriterion("disk_format <", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatLessThanOrEqualTo(String value) {
            addCriterion("disk_format <=", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatLike(String value) {
            addCriterion("disk_format like", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatNotLike(String value) {
            addCriterion("disk_format not like", value, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatIn(List<String> values) {
            addCriterion("disk_format in", values, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatNotIn(List<String> values) {
            addCriterion("disk_format not in", values, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatBetween(String value1, String value2) {
            addCriterion("disk_format between", value1, value2, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andDiskFormatNotBetween(String value1, String value2) {
            addCriterion("disk_format not between", value1, value2, "diskFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatIsNull() {
            addCriterion("container_format is null");
            return (Criteria) this;
        }

        public Criteria andContainerFormatIsNotNull() {
            addCriterion("container_format is not null");
            return (Criteria) this;
        }

        public Criteria andContainerFormatEqualTo(String value) {
            addCriterion("container_format =", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatNotEqualTo(String value) {
            addCriterion("container_format <>", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatGreaterThan(String value) {
            addCriterion("container_format >", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatGreaterThanOrEqualTo(String value) {
            addCriterion("container_format >=", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatLessThan(String value) {
            addCriterion("container_format <", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatLessThanOrEqualTo(String value) {
            addCriterion("container_format <=", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatLike(String value) {
            addCriterion("container_format like", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatNotLike(String value) {
            addCriterion("container_format not like", value, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatIn(List<String> values) {
            addCriterion("container_format in", values, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatNotIn(List<String> values) {
            addCriterion("container_format not in", values, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatBetween(String value1, String value2) {
            addCriterion("container_format between", value1, value2, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andContainerFormatNotBetween(String value1, String value2) {
            addCriterion("container_format not between", value1, value2, "containerFormat");
            return (Criteria) this;
        }

        public Criteria andChecksumIsNull() {
            addCriterion("checksum is null");
            return (Criteria) this;
        }

        public Criteria andChecksumIsNotNull() {
            addCriterion("checksum is not null");
            return (Criteria) this;
        }

        public Criteria andChecksumEqualTo(String value) {
            addCriterion("checksum =", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumNotEqualTo(String value) {
            addCriterion("checksum <>", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumGreaterThan(String value) {
            addCriterion("checksum >", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumGreaterThanOrEqualTo(String value) {
            addCriterion("checksum >=", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumLessThan(String value) {
            addCriterion("checksum <", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumLessThanOrEqualTo(String value) {
            addCriterion("checksum <=", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumLike(String value) {
            addCriterion("checksum like", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumNotLike(String value) {
            addCriterion("checksum not like", value, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumIn(List<String> values) {
            addCriterion("checksum in", values, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumNotIn(List<String> values) {
            addCriterion("checksum not in", values, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumBetween(String value1, String value2) {
            addCriterion("checksum between", value1, value2, "checksum");
            return (Criteria) this;
        }

        public Criteria andChecksumNotBetween(String value1, String value2) {
            addCriterion("checksum not between", value1, value2, "checksum");
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

        public Criteria andMinDiskIsNull() {
            addCriterion("min_disk is null");
            return (Criteria) this;
        }

        public Criteria andMinDiskIsNotNull() {
            addCriterion("min_disk is not null");
            return (Criteria) this;
        }

        public Criteria andMinDiskEqualTo(Integer value) {
            addCriterion("min_disk =", value, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskNotEqualTo(Integer value) {
            addCriterion("min_disk <>", value, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskGreaterThan(Integer value) {
            addCriterion("min_disk >", value, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskGreaterThanOrEqualTo(Integer value) {
            addCriterion("min_disk >=", value, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskLessThan(Integer value) {
            addCriterion("min_disk <", value, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskLessThanOrEqualTo(Integer value) {
            addCriterion("min_disk <=", value, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskIn(List<Integer> values) {
            addCriterion("min_disk in", values, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskNotIn(List<Integer> values) {
            addCriterion("min_disk not in", values, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskBetween(Integer value1, Integer value2) {
            addCriterion("min_disk between", value1, value2, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinDiskNotBetween(Integer value1, Integer value2) {
            addCriterion("min_disk not between", value1, value2, "minDisk");
            return (Criteria) this;
        }

        public Criteria andMinRamIsNull() {
            addCriterion("min_ram is null");
            return (Criteria) this;
        }

        public Criteria andMinRamIsNotNull() {
            addCriterion("min_ram is not null");
            return (Criteria) this;
        }

        public Criteria andMinRamEqualTo(Integer value) {
            addCriterion("min_ram =", value, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamNotEqualTo(Integer value) {
            addCriterion("min_ram <>", value, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamGreaterThan(Integer value) {
            addCriterion("min_ram >", value, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamGreaterThanOrEqualTo(Integer value) {
            addCriterion("min_ram >=", value, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamLessThan(Integer value) {
            addCriterion("min_ram <", value, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamLessThanOrEqualTo(Integer value) {
            addCriterion("min_ram <=", value, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamIn(List<Integer> values) {
            addCriterion("min_ram in", values, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamNotIn(List<Integer> values) {
            addCriterion("min_ram not in", values, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamBetween(Integer value1, Integer value2) {
            addCriterion("min_ram between", value1, value2, "minRam");
            return (Criteria) this;
        }

        public Criteria andMinRamNotBetween(Integer value1, Integer value2) {
            addCriterion("min_ram not between", value1, value2, "minRam");
            return (Criteria) this;
        }

        public Criteria andProtectedIsNull() {
            addCriterion("protected is null");
            return (Criteria) this;
        }

        public Criteria andProtectedIsNotNull() {
            addCriterion("protected is not null");
            return (Criteria) this;
        }

        public Criteria andProtectedEqualTo(Short value) {
            addCriterion("protected =", value, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedNotEqualTo(Short value) {
            addCriterion("protected <>", value, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedGreaterThan(Short value) {
            addCriterion("protected >", value, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedGreaterThanOrEqualTo(Short value) {
            addCriterion("protected >=", value, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedLessThan(Short value) {
            addCriterion("protected <", value, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedLessThanOrEqualTo(Short value) {
            addCriterion("protected <=", value, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedIn(List<Short> values) {
            addCriterion("protected in", values, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedNotIn(List<Short> values) {
            addCriterion("protected not in", values, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedBetween(Short value1, Short value2) {
            addCriterion("protected between", value1, value2, "protected");
            return (Criteria) this;
        }

        public Criteria andProtectedNotBetween(Short value1, Short value2) {
            addCriterion("protected not between", value1, value2, "protected");
            return (Criteria) this;
        }

        public Criteria andVirualSizeIsNull() {
            addCriterion("virual_size is null");
            return (Criteria) this;
        }

        public Criteria andVirualSizeIsNotNull() {
            addCriterion("virual_size is not null");
            return (Criteria) this;
        }

        public Criteria andVirualSizeEqualTo(Long value) {
            addCriterion("virual_size =", value, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeNotEqualTo(Long value) {
            addCriterion("virual_size <>", value, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeGreaterThan(Long value) {
            addCriterion("virual_size >", value, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeGreaterThanOrEqualTo(Long value) {
            addCriterion("virual_size >=", value, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeLessThan(Long value) {
            addCriterion("virual_size <", value, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeLessThanOrEqualTo(Long value) {
            addCriterion("virual_size <=", value, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeIn(List<Long> values) {
            addCriterion("virual_size in", values, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeNotIn(List<Long> values) {
            addCriterion("virual_size not in", values, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeBetween(Long value1, Long value2) {
            addCriterion("virual_size between", value1, value2, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVirualSizeNotBetween(Long value1, Long value2) {
            addCriterion("virual_size not between", value1, value2, "virualSize");
            return (Criteria) this;
        }

        public Criteria andVisibilityIsNull() {
            addCriterion("visibility is null");
            return (Criteria) this;
        }

        public Criteria andVisibilityIsNotNull() {
            addCriterion("visibility is not null");
            return (Criteria) this;
        }

        public Criteria andVisibilityEqualTo(String value) {
            addCriterion("visibility =", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityNotEqualTo(String value) {
            addCriterion("visibility <>", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityGreaterThan(String value) {
            addCriterion("visibility >", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityGreaterThanOrEqualTo(String value) {
            addCriterion("visibility >=", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityLessThan(String value) {
            addCriterion("visibility <", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityLessThanOrEqualTo(String value) {
            addCriterion("visibility <=", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityLike(String value) {
            addCriterion("visibility like", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityNotLike(String value) {
            addCriterion("visibility not like", value, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityIn(List<String> values) {
            addCriterion("visibility in", values, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityNotIn(List<String> values) {
            addCriterion("visibility not in", values, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityBetween(String value1, String value2) {
            addCriterion("visibility between", value1, value2, "visibility");
            return (Criteria) this;
        }

        public Criteria andVisibilityNotBetween(String value1, String value2) {
            addCriterion("visibility not between", value1, value2, "visibility");
            return (Criteria) this;
        }

        public Criteria andOsHiddenIsNull() {
            addCriterion("os_hidden is null");
            return (Criteria) this;
        }

        public Criteria andOsHiddenIsNotNull() {
            addCriterion("os_hidden is not null");
            return (Criteria) this;
        }

        public Criteria andOsHiddenEqualTo(Short value) {
            addCriterion("os_hidden =", value, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenNotEqualTo(Short value) {
            addCriterion("os_hidden <>", value, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenGreaterThan(Short value) {
            addCriterion("os_hidden >", value, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenGreaterThanOrEqualTo(Short value) {
            addCriterion("os_hidden >=", value, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenLessThan(Short value) {
            addCriterion("os_hidden <", value, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenLessThanOrEqualTo(Short value) {
            addCriterion("os_hidden <=", value, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenIn(List<Short> values) {
            addCriterion("os_hidden in", values, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenNotIn(List<Short> values) {
            addCriterion("os_hidden not in", values, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenBetween(Short value1, Short value2) {
            addCriterion("os_hidden between", value1, value2, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHiddenNotBetween(Short value1, Short value2) {
            addCriterion("os_hidden not between", value1, value2, "osHidden");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoIsNull() {
            addCriterion("os_hash_algo is null");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoIsNotNull() {
            addCriterion("os_hash_algo is not null");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoEqualTo(String value) {
            addCriterion("os_hash_algo =", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoNotEqualTo(String value) {
            addCriterion("os_hash_algo <>", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoGreaterThan(String value) {
            addCriterion("os_hash_algo >", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoGreaterThanOrEqualTo(String value) {
            addCriterion("os_hash_algo >=", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoLessThan(String value) {
            addCriterion("os_hash_algo <", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoLessThanOrEqualTo(String value) {
            addCriterion("os_hash_algo <=", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoLike(String value) {
            addCriterion("os_hash_algo like", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoNotLike(String value) {
            addCriterion("os_hash_algo not like", value, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoIn(List<String> values) {
            addCriterion("os_hash_algo in", values, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoNotIn(List<String> values) {
            addCriterion("os_hash_algo not in", values, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoBetween(String value1, String value2) {
            addCriterion("os_hash_algo between", value1, value2, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashAlgoNotBetween(String value1, String value2) {
            addCriterion("os_hash_algo not between", value1, value2, "osHashAlgo");
            return (Criteria) this;
        }

        public Criteria andOsHashValueIsNull() {
            addCriterion("os_hash_value is null");
            return (Criteria) this;
        }

        public Criteria andOsHashValueIsNotNull() {
            addCriterion("os_hash_value is not null");
            return (Criteria) this;
        }

        public Criteria andOsHashValueEqualTo(String value) {
            addCriterion("os_hash_value =", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueNotEqualTo(String value) {
            addCriterion("os_hash_value <>", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueGreaterThan(String value) {
            addCriterion("os_hash_value >", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueGreaterThanOrEqualTo(String value) {
            addCriterion("os_hash_value >=", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueLessThan(String value) {
            addCriterion("os_hash_value <", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueLessThanOrEqualTo(String value) {
            addCriterion("os_hash_value <=", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueLike(String value) {
            addCriterion("os_hash_value like", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueNotLike(String value) {
            addCriterion("os_hash_value not like", value, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueIn(List<String> values) {
            addCriterion("os_hash_value in", values, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueNotIn(List<String> values) {
            addCriterion("os_hash_value not in", values, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueBetween(String value1, String value2) {
            addCriterion("os_hash_value between", value1, value2, "osHashValue");
            return (Criteria) this;
        }

        public Criteria andOsHashValueNotBetween(String value1, String value2) {
            addCriterion("os_hash_value not between", value1, value2, "osHashValue");
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

        public Criteria andPropertiesIsNull() {
            addCriterion("properties is null");
            return (Criteria) this;
        }

        public Criteria andPropertiesIsNotNull() {
            addCriterion("properties is not null");
            return (Criteria) this;
        }

        public Criteria andPropertiesEqualTo(String value) {
            addCriterion("properties =", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotEqualTo(String value) {
            addCriterion("properties <>", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesGreaterThan(String value) {
            addCriterion("properties >", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesGreaterThanOrEqualTo(String value) {
            addCriterion("properties >=", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesLessThan(String value) {
            addCriterion("properties <", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesLessThanOrEqualTo(String value) {
            addCriterion("properties <=", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesLike(String value) {
            addCriterion("properties like", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotLike(String value) {
            addCriterion("properties not like", value, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesIn(List<String> values) {
            addCriterion("properties in", values, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotIn(List<String> values) {
            addCriterion("properties not in", values, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesBetween(String value1, String value2) {
            addCriterion("properties between", value1, value2, "properties");
            return (Criteria) this;
        }

        public Criteria andPropertiesNotBetween(String value1, String value2) {
            addCriterion("properties not between", value1, value2, "properties");
            return (Criteria) this;
        }

        public Criteria andImageTypeEqualTo(String value) {
            addCriterion("properties like", value, "properties");
            return (Criteria) this;
        }

        public Criteria andImageTypeNotEqualTo(String value) {
            addCriterion("properties not like", value, "properties");
            return (Criteria) this;
        }

        public Criteria andInstanceIdEqualTo(String value) {
            addCriterion("properties like", value, "properties");
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