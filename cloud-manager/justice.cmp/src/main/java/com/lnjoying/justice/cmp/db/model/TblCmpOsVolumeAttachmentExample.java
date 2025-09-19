package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsVolumeAttachmentExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsVolumeAttachmentExample() {
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

        public Criteria andAttachedHostIsNull() {
            addCriterion("attached_host is null");
            return (Criteria) this;
        }

        public Criteria andAttachedHostIsNotNull() {
            addCriterion("attached_host is not null");
            return (Criteria) this;
        }

        public Criteria andAttachedHostEqualTo(String value) {
            addCriterion("attached_host =", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostNotEqualTo(String value) {
            addCriterion("attached_host <>", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostGreaterThan(String value) {
            addCriterion("attached_host >", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostGreaterThanOrEqualTo(String value) {
            addCriterion("attached_host >=", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostLessThan(String value) {
            addCriterion("attached_host <", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostLessThanOrEqualTo(String value) {
            addCriterion("attached_host <=", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostLike(String value) {
            addCriterion("attached_host like", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostNotLike(String value) {
            addCriterion("attached_host not like", value, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostIn(List<String> values) {
            addCriterion("attached_host in", values, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostNotIn(List<String> values) {
            addCriterion("attached_host not in", values, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostBetween(String value1, String value2) {
            addCriterion("attached_host between", value1, value2, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andAttachedHostNotBetween(String value1, String value2) {
            addCriterion("attached_host not between", value1, value2, "attachedHost");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidIsNull() {
            addCriterion("instance_uuid is null");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidIsNotNull() {
            addCriterion("instance_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidEqualTo(String value) {
            addCriterion("instance_uuid =", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotEqualTo(String value) {
            addCriterion("instance_uuid <>", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidGreaterThan(String value) {
            addCriterion("instance_uuid >", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidGreaterThanOrEqualTo(String value) {
            addCriterion("instance_uuid >=", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidLessThan(String value) {
            addCriterion("instance_uuid <", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidLessThanOrEqualTo(String value) {
            addCriterion("instance_uuid <=", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidLike(String value) {
            addCriterion("instance_uuid like", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotLike(String value) {
            addCriterion("instance_uuid not like", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidIn(List<String> values) {
            addCriterion("instance_uuid in", values, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotIn(List<String> values) {
            addCriterion("instance_uuid not in", values, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidBetween(String value1, String value2) {
            addCriterion("instance_uuid between", value1, value2, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotBetween(String value1, String value2) {
            addCriterion("instance_uuid not between", value1, value2, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andMountpointIsNull() {
            addCriterion("mountpoint is null");
            return (Criteria) this;
        }

        public Criteria andMountpointIsNotNull() {
            addCriterion("mountpoint is not null");
            return (Criteria) this;
        }

        public Criteria andMountpointEqualTo(String value) {
            addCriterion("mountpoint =", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointNotEqualTo(String value) {
            addCriterion("mountpoint <>", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointGreaterThan(String value) {
            addCriterion("mountpoint >", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointGreaterThanOrEqualTo(String value) {
            addCriterion("mountpoint >=", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointLessThan(String value) {
            addCriterion("mountpoint <", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointLessThanOrEqualTo(String value) {
            addCriterion("mountpoint <=", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointLike(String value) {
            addCriterion("mountpoint like", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointNotLike(String value) {
            addCriterion("mountpoint not like", value, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointIn(List<String> values) {
            addCriterion("mountpoint in", values, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointNotIn(List<String> values) {
            addCriterion("mountpoint not in", values, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointBetween(String value1, String value2) {
            addCriterion("mountpoint between", value1, value2, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andMountpointNotBetween(String value1, String value2) {
            addCriterion("mountpoint not between", value1, value2, "mountpoint");
            return (Criteria) this;
        }

        public Criteria andAttachTimeIsNull() {
            addCriterion("attach_time is null");
            return (Criteria) this;
        }

        public Criteria andAttachTimeIsNotNull() {
            addCriterion("attach_time is not null");
            return (Criteria) this;
        }

        public Criteria andAttachTimeEqualTo(Date value) {
            addCriterion("attach_time =", value, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeNotEqualTo(Date value) {
            addCriterion("attach_time <>", value, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeGreaterThan(Date value) {
            addCriterion("attach_time >", value, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("attach_time >=", value, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeLessThan(Date value) {
            addCriterion("attach_time <", value, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeLessThanOrEqualTo(Date value) {
            addCriterion("attach_time <=", value, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeIn(List<Date> values) {
            addCriterion("attach_time in", values, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeNotIn(List<Date> values) {
            addCriterion("attach_time not in", values, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeBetween(Date value1, Date value2) {
            addCriterion("attach_time between", value1, value2, "attachTime");
            return (Criteria) this;
        }

        public Criteria andAttachTimeNotBetween(Date value1, Date value2) {
            addCriterion("attach_time not between", value1, value2, "attachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeIsNull() {
            addCriterion("detach_time is null");
            return (Criteria) this;
        }

        public Criteria andDetachTimeIsNotNull() {
            addCriterion("detach_time is not null");
            return (Criteria) this;
        }

        public Criteria andDetachTimeEqualTo(Date value) {
            addCriterion("detach_time =", value, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeNotEqualTo(Date value) {
            addCriterion("detach_time <>", value, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeGreaterThan(Date value) {
            addCriterion("detach_time >", value, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("detach_time >=", value, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeLessThan(Date value) {
            addCriterion("detach_time <", value, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeLessThanOrEqualTo(Date value) {
            addCriterion("detach_time <=", value, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeIn(List<Date> values) {
            addCriterion("detach_time in", values, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeNotIn(List<Date> values) {
            addCriterion("detach_time not in", values, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeBetween(Date value1, Date value2) {
            addCriterion("detach_time between", value1, value2, "detachTime");
            return (Criteria) this;
        }

        public Criteria andDetachTimeNotBetween(Date value1, Date value2) {
            addCriterion("detach_time not between", value1, value2, "detachTime");
            return (Criteria) this;
        }

        public Criteria andAttachModeIsNull() {
            addCriterion("attach_mode is null");
            return (Criteria) this;
        }

        public Criteria andAttachModeIsNotNull() {
            addCriterion("attach_mode is not null");
            return (Criteria) this;
        }

        public Criteria andAttachModeEqualTo(String value) {
            addCriterion("attach_mode =", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeNotEqualTo(String value) {
            addCriterion("attach_mode <>", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeGreaterThan(String value) {
            addCriterion("attach_mode >", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeGreaterThanOrEqualTo(String value) {
            addCriterion("attach_mode >=", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeLessThan(String value) {
            addCriterion("attach_mode <", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeLessThanOrEqualTo(String value) {
            addCriterion("attach_mode <=", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeLike(String value) {
            addCriterion("attach_mode like", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeNotLike(String value) {
            addCriterion("attach_mode not like", value, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeIn(List<String> values) {
            addCriterion("attach_mode in", values, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeNotIn(List<String> values) {
            addCriterion("attach_mode not in", values, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeBetween(String value1, String value2) {
            addCriterion("attach_mode between", value1, value2, "attachMode");
            return (Criteria) this;
        }

        public Criteria andAttachModeNotBetween(String value1, String value2) {
            addCriterion("attach_mode not between", value1, value2, "attachMode");
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

        public Criteria andConnectionInfoIsNull() {
            addCriterion("connection_info is null");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoIsNotNull() {
            addCriterion("connection_info is not null");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoEqualTo(String value) {
            addCriterion("connection_info =", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoNotEqualTo(String value) {
            addCriterion("connection_info <>", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoGreaterThan(String value) {
            addCriterion("connection_info >", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoGreaterThanOrEqualTo(String value) {
            addCriterion("connection_info >=", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoLessThan(String value) {
            addCriterion("connection_info <", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoLessThanOrEqualTo(String value) {
            addCriterion("connection_info <=", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoLike(String value) {
            addCriterion("connection_info like", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoNotLike(String value) {
            addCriterion("connection_info not like", value, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoIn(List<String> values) {
            addCriterion("connection_info in", values, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoNotIn(List<String> values) {
            addCriterion("connection_info not in", values, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoBetween(String value1, String value2) {
            addCriterion("connection_info between", value1, value2, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectionInfoNotBetween(String value1, String value2) {
            addCriterion("connection_info not between", value1, value2, "connectionInfo");
            return (Criteria) this;
        }

        public Criteria andConnectorIsNull() {
            addCriterion("connector is null");
            return (Criteria) this;
        }

        public Criteria andConnectorIsNotNull() {
            addCriterion("connector is not null");
            return (Criteria) this;
        }

        public Criteria andConnectorEqualTo(String value) {
            addCriterion("connector =", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorNotEqualTo(String value) {
            addCriterion("connector <>", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorGreaterThan(String value) {
            addCriterion("connector >", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorGreaterThanOrEqualTo(String value) {
            addCriterion("connector >=", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorLessThan(String value) {
            addCriterion("connector <", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorLessThanOrEqualTo(String value) {
            addCriterion("connector <=", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorLike(String value) {
            addCriterion("connector like", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorNotLike(String value) {
            addCriterion("connector not like", value, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorIn(List<String> values) {
            addCriterion("connector in", values, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorNotIn(List<String> values) {
            addCriterion("connector not in", values, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorBetween(String value1, String value2) {
            addCriterion("connector between", value1, value2, "connector");
            return (Criteria) this;
        }

        public Criteria andConnectorNotBetween(String value1, String value2) {
            addCriterion("connector not between", value1, value2, "connector");
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