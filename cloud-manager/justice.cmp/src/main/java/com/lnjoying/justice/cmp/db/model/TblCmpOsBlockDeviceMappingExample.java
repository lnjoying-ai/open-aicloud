package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsBlockDeviceMappingExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsBlockDeviceMappingExample() {
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

        public Criteria andDeviceNameIsNull() {
            addCriterion("device_name is null");
            return (Criteria) this;
        }

        public Criteria andDeviceNameIsNotNull() {
            addCriterion("device_name is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceNameEqualTo(String value) {
            addCriterion("device_name =", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotEqualTo(String value) {
            addCriterion("device_name <>", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameGreaterThan(String value) {
            addCriterion("device_name >", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameGreaterThanOrEqualTo(String value) {
            addCriterion("device_name >=", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameLessThan(String value) {
            addCriterion("device_name <", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameLessThanOrEqualTo(String value) {
            addCriterion("device_name <=", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameLike(String value) {
            addCriterion("device_name like", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotLike(String value) {
            addCriterion("device_name not like", value, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameIn(List<String> values) {
            addCriterion("device_name in", values, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotIn(List<String> values) {
            addCriterion("device_name not in", values, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameBetween(String value1, String value2) {
            addCriterion("device_name between", value1, value2, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeviceNameNotBetween(String value1, String value2) {
            addCriterion("device_name not between", value1, value2, "deviceName");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationIsNull() {
            addCriterion("delete_on_termination is null");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationIsNotNull() {
            addCriterion("delete_on_termination is not null");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationEqualTo(Short value) {
            addCriterion("delete_on_termination =", value, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationNotEqualTo(Short value) {
            addCriterion("delete_on_termination <>", value, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationGreaterThan(Short value) {
            addCriterion("delete_on_termination >", value, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationGreaterThanOrEqualTo(Short value) {
            addCriterion("delete_on_termination >=", value, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationLessThan(Short value) {
            addCriterion("delete_on_termination <", value, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationLessThanOrEqualTo(Short value) {
            addCriterion("delete_on_termination <=", value, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationIn(List<Short> values) {
            addCriterion("delete_on_termination in", values, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationNotIn(List<Short> values) {
            addCriterion("delete_on_termination not in", values, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationBetween(Short value1, Short value2) {
            addCriterion("delete_on_termination between", value1, value2, "deleteOnTermination");
            return (Criteria) this;
        }

        public Criteria andDeleteOnTerminationNotBetween(Short value1, Short value2) {
            addCriterion("delete_on_termination not between", value1, value2, "deleteOnTermination");
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

        public Criteria andNoDeviceIsNull() {
            addCriterion("no_device is null");
            return (Criteria) this;
        }

        public Criteria andNoDeviceIsNotNull() {
            addCriterion("no_device is not null");
            return (Criteria) this;
        }

        public Criteria andNoDeviceEqualTo(Short value) {
            addCriterion("no_device =", value, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceNotEqualTo(Short value) {
            addCriterion("no_device <>", value, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceGreaterThan(Short value) {
            addCriterion("no_device >", value, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceGreaterThanOrEqualTo(Short value) {
            addCriterion("no_device >=", value, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceLessThan(Short value) {
            addCriterion("no_device <", value, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceLessThanOrEqualTo(Short value) {
            addCriterion("no_device <=", value, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceIn(List<Short> values) {
            addCriterion("no_device in", values, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceNotIn(List<Short> values) {
            addCriterion("no_device not in", values, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceBetween(Short value1, Short value2) {
            addCriterion("no_device between", value1, value2, "noDevice");
            return (Criteria) this;
        }

        public Criteria andNoDeviceNotBetween(Short value1, Short value2) {
            addCriterion("no_device not between", value1, value2, "noDevice");
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

        public Criteria andSourceTypeIsNull() {
            addCriterion("source_type is null");
            return (Criteria) this;
        }

        public Criteria andSourceTypeIsNotNull() {
            addCriterion("source_type is not null");
            return (Criteria) this;
        }

        public Criteria andSourceTypeEqualTo(String value) {
            addCriterion("source_type =", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotEqualTo(String value) {
            addCriterion("source_type <>", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeGreaterThan(String value) {
            addCriterion("source_type >", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeGreaterThanOrEqualTo(String value) {
            addCriterion("source_type >=", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeLessThan(String value) {
            addCriterion("source_type <", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeLessThanOrEqualTo(String value) {
            addCriterion("source_type <=", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeLike(String value) {
            addCriterion("source_type like", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotLike(String value) {
            addCriterion("source_type not like", value, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeIn(List<String> values) {
            addCriterion("source_type in", values, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotIn(List<String> values) {
            addCriterion("source_type not in", values, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeBetween(String value1, String value2) {
            addCriterion("source_type between", value1, value2, "sourceType");
            return (Criteria) this;
        }

        public Criteria andSourceTypeNotBetween(String value1, String value2) {
            addCriterion("source_type not between", value1, value2, "sourceType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeIsNull() {
            addCriterion("destination_type is null");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeIsNotNull() {
            addCriterion("destination_type is not null");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeEqualTo(String value) {
            addCriterion("destination_type =", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeNotEqualTo(String value) {
            addCriterion("destination_type <>", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeGreaterThan(String value) {
            addCriterion("destination_type >", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeGreaterThanOrEqualTo(String value) {
            addCriterion("destination_type >=", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeLessThan(String value) {
            addCriterion("destination_type <", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeLessThanOrEqualTo(String value) {
            addCriterion("destination_type <=", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeLike(String value) {
            addCriterion("destination_type like", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeNotLike(String value) {
            addCriterion("destination_type not like", value, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeIn(List<String> values) {
            addCriterion("destination_type in", values, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeNotIn(List<String> values) {
            addCriterion("destination_type not in", values, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeBetween(String value1, String value2) {
            addCriterion("destination_type between", value1, value2, "destinationType");
            return (Criteria) this;
        }

        public Criteria andDestinationTypeNotBetween(String value1, String value2) {
            addCriterion("destination_type not between", value1, value2, "destinationType");
            return (Criteria) this;
        }

        public Criteria andGuestFormatIsNull() {
            addCriterion("guest_format is null");
            return (Criteria) this;
        }

        public Criteria andGuestFormatIsNotNull() {
            addCriterion("guest_format is not null");
            return (Criteria) this;
        }

        public Criteria andGuestFormatEqualTo(String value) {
            addCriterion("guest_format =", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatNotEqualTo(String value) {
            addCriterion("guest_format <>", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatGreaterThan(String value) {
            addCriterion("guest_format >", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatGreaterThanOrEqualTo(String value) {
            addCriterion("guest_format >=", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatLessThan(String value) {
            addCriterion("guest_format <", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatLessThanOrEqualTo(String value) {
            addCriterion("guest_format <=", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatLike(String value) {
            addCriterion("guest_format like", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatNotLike(String value) {
            addCriterion("guest_format not like", value, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatIn(List<String> values) {
            addCriterion("guest_format in", values, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatNotIn(List<String> values) {
            addCriterion("guest_format not in", values, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatBetween(String value1, String value2) {
            addCriterion("guest_format between", value1, value2, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andGuestFormatNotBetween(String value1, String value2) {
            addCriterion("guest_format not between", value1, value2, "guestFormat");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeIsNull() {
            addCriterion("device_type is null");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeIsNotNull() {
            addCriterion("device_type is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeEqualTo(String value) {
            addCriterion("device_type =", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeNotEqualTo(String value) {
            addCriterion("device_type <>", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeGreaterThan(String value) {
            addCriterion("device_type >", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeGreaterThanOrEqualTo(String value) {
            addCriterion("device_type >=", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeLessThan(String value) {
            addCriterion("device_type <", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeLessThanOrEqualTo(String value) {
            addCriterion("device_type <=", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeLike(String value) {
            addCriterion("device_type like", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeNotLike(String value) {
            addCriterion("device_type not like", value, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeIn(List<String> values) {
            addCriterion("device_type in", values, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeNotIn(List<String> values) {
            addCriterion("device_type not in", values, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeBetween(String value1, String value2) {
            addCriterion("device_type between", value1, value2, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDeviceTypeNotBetween(String value1, String value2) {
            addCriterion("device_type not between", value1, value2, "deviceType");
            return (Criteria) this;
        }

        public Criteria andDiskBusIsNull() {
            addCriterion("disk_bus is null");
            return (Criteria) this;
        }

        public Criteria andDiskBusIsNotNull() {
            addCriterion("disk_bus is not null");
            return (Criteria) this;
        }

        public Criteria andDiskBusEqualTo(String value) {
            addCriterion("disk_bus =", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusNotEqualTo(String value) {
            addCriterion("disk_bus <>", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusGreaterThan(String value) {
            addCriterion("disk_bus >", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusGreaterThanOrEqualTo(String value) {
            addCriterion("disk_bus >=", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusLessThan(String value) {
            addCriterion("disk_bus <", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusLessThanOrEqualTo(String value) {
            addCriterion("disk_bus <=", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusLike(String value) {
            addCriterion("disk_bus like", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusNotLike(String value) {
            addCriterion("disk_bus not like", value, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusIn(List<String> values) {
            addCriterion("disk_bus in", values, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusNotIn(List<String> values) {
            addCriterion("disk_bus not in", values, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusBetween(String value1, String value2) {
            addCriterion("disk_bus between", value1, value2, "diskBus");
            return (Criteria) this;
        }

        public Criteria andDiskBusNotBetween(String value1, String value2) {
            addCriterion("disk_bus not between", value1, value2, "diskBus");
            return (Criteria) this;
        }

        public Criteria andBootIndexIsNull() {
            addCriterion("boot_index is null");
            return (Criteria) this;
        }

        public Criteria andBootIndexIsNotNull() {
            addCriterion("boot_index is not null");
            return (Criteria) this;
        }

        public Criteria andBootIndexEqualTo(Integer value) {
            addCriterion("boot_index =", value, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexNotEqualTo(Integer value) {
            addCriterion("boot_index <>", value, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexGreaterThan(Integer value) {
            addCriterion("boot_index >", value, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("boot_index >=", value, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexLessThan(Integer value) {
            addCriterion("boot_index <", value, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexLessThanOrEqualTo(Integer value) {
            addCriterion("boot_index <=", value, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexIn(List<Integer> values) {
            addCriterion("boot_index in", values, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexNotIn(List<Integer> values) {
            addCriterion("boot_index not in", values, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexBetween(Integer value1, Integer value2) {
            addCriterion("boot_index between", value1, value2, "bootIndex");
            return (Criteria) this;
        }

        public Criteria andBootIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("boot_index not between", value1, value2, "bootIndex");
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

        public Criteria andTagIsNull() {
            addCriterion("tag is null");
            return (Criteria) this;
        }

        public Criteria andTagIsNotNull() {
            addCriterion("tag is not null");
            return (Criteria) this;
        }

        public Criteria andTagEqualTo(String value) {
            addCriterion("tag =", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotEqualTo(String value) {
            addCriterion("tag <>", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThan(String value) {
            addCriterion("tag >", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagGreaterThanOrEqualTo(String value) {
            addCriterion("tag >=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThan(String value) {
            addCriterion("tag <", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLessThanOrEqualTo(String value) {
            addCriterion("tag <=", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagLike(String value) {
            addCriterion("tag like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotLike(String value) {
            addCriterion("tag not like", value, "tag");
            return (Criteria) this;
        }

        public Criteria andTagIn(List<String> values) {
            addCriterion("tag in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotIn(List<String> values) {
            addCriterion("tag not in", values, "tag");
            return (Criteria) this;
        }

        public Criteria andTagBetween(String value1, String value2) {
            addCriterion("tag between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andTagNotBetween(String value1, String value2) {
            addCriterion("tag not between", value1, value2, "tag");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdIsNull() {
            addCriterion("attachment_id is null");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdIsNotNull() {
            addCriterion("attachment_id is not null");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdEqualTo(String value) {
            addCriterion("attachment_id =", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdNotEqualTo(String value) {
            addCriterion("attachment_id <>", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdGreaterThan(String value) {
            addCriterion("attachment_id >", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdGreaterThanOrEqualTo(String value) {
            addCriterion("attachment_id >=", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdLessThan(String value) {
            addCriterion("attachment_id <", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdLessThanOrEqualTo(String value) {
            addCriterion("attachment_id <=", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdLike(String value) {
            addCriterion("attachment_id like", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdNotLike(String value) {
            addCriterion("attachment_id not like", value, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdIn(List<String> values) {
            addCriterion("attachment_id in", values, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdNotIn(List<String> values) {
            addCriterion("attachment_id not in", values, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdBetween(String value1, String value2) {
            addCriterion("attachment_id between", value1, value2, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andAttachmentIdNotBetween(String value1, String value2) {
            addCriterion("attachment_id not between", value1, value2, "attachmentId");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIsNull() {
            addCriterion("volume_type is null");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIsNotNull() {
            addCriterion("volume_type is not null");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeEqualTo(String value) {
            addCriterion("volume_type =", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeNotEqualTo(String value) {
            addCriterion("volume_type <>", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeGreaterThan(String value) {
            addCriterion("volume_type >", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeGreaterThanOrEqualTo(String value) {
            addCriterion("volume_type >=", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeLessThan(String value) {
            addCriterion("volume_type <", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeLessThanOrEqualTo(String value) {
            addCriterion("volume_type <=", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeLike(String value) {
            addCriterion("volume_type like", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeNotLike(String value) {
            addCriterion("volume_type not like", value, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeIn(List<String> values) {
            addCriterion("volume_type in", values, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeNotIn(List<String> values) {
            addCriterion("volume_type not in", values, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeBetween(String value1, String value2) {
            addCriterion("volume_type between", value1, value2, "volumeType");
            return (Criteria) this;
        }

        public Criteria andVolumeTypeNotBetween(String value1, String value2) {
            addCriterion("volume_type not between", value1, value2, "volumeType");
            return (Criteria) this;
        }

        public Criteria andEncryptedIsNull() {
            addCriterion("encrypted is null");
            return (Criteria) this;
        }

        public Criteria andEncryptedIsNotNull() {
            addCriterion("encrypted is not null");
            return (Criteria) this;
        }

        public Criteria andEncryptedEqualTo(Short value) {
            addCriterion("encrypted =", value, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedNotEqualTo(Short value) {
            addCriterion("encrypted <>", value, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedGreaterThan(Short value) {
            addCriterion("encrypted >", value, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedGreaterThanOrEqualTo(Short value) {
            addCriterion("encrypted >=", value, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedLessThan(Short value) {
            addCriterion("encrypted <", value, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedLessThanOrEqualTo(Short value) {
            addCriterion("encrypted <=", value, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedIn(List<Short> values) {
            addCriterion("encrypted in", values, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedNotIn(List<Short> values) {
            addCriterion("encrypted not in", values, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedBetween(Short value1, Short value2) {
            addCriterion("encrypted between", value1, value2, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptedNotBetween(Short value1, Short value2) {
            addCriterion("encrypted not between", value1, value2, "encrypted");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidIsNull() {
            addCriterion("encryption_secret_uuid is null");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidIsNotNull() {
            addCriterion("encryption_secret_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidEqualTo(String value) {
            addCriterion("encryption_secret_uuid =", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidNotEqualTo(String value) {
            addCriterion("encryption_secret_uuid <>", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidGreaterThan(String value) {
            addCriterion("encryption_secret_uuid >", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidGreaterThanOrEqualTo(String value) {
            addCriterion("encryption_secret_uuid >=", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidLessThan(String value) {
            addCriterion("encryption_secret_uuid <", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidLessThanOrEqualTo(String value) {
            addCriterion("encryption_secret_uuid <=", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidLike(String value) {
            addCriterion("encryption_secret_uuid like", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidNotLike(String value) {
            addCriterion("encryption_secret_uuid not like", value, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidIn(List<String> values) {
            addCriterion("encryption_secret_uuid in", values, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidNotIn(List<String> values) {
            addCriterion("encryption_secret_uuid not in", values, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidBetween(String value1, String value2) {
            addCriterion("encryption_secret_uuid between", value1, value2, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionSecretUuidNotBetween(String value1, String value2) {
            addCriterion("encryption_secret_uuid not between", value1, value2, "encryptionSecretUuid");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatIsNull() {
            addCriterion("encryption_format is null");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatIsNotNull() {
            addCriterion("encryption_format is not null");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatEqualTo(String value) {
            addCriterion("encryption_format =", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatNotEqualTo(String value) {
            addCriterion("encryption_format <>", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatGreaterThan(String value) {
            addCriterion("encryption_format >", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatGreaterThanOrEqualTo(String value) {
            addCriterion("encryption_format >=", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatLessThan(String value) {
            addCriterion("encryption_format <", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatLessThanOrEqualTo(String value) {
            addCriterion("encryption_format <=", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatLike(String value) {
            addCriterion("encryption_format like", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatNotLike(String value) {
            addCriterion("encryption_format not like", value, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatIn(List<String> values) {
            addCriterion("encryption_format in", values, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatNotIn(List<String> values) {
            addCriterion("encryption_format not in", values, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatBetween(String value1, String value2) {
            addCriterion("encryption_format between", value1, value2, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionFormatNotBetween(String value1, String value2) {
            addCriterion("encryption_format not between", value1, value2, "encryptionFormat");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsIsNull() {
            addCriterion("encryption_options is null");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsIsNotNull() {
            addCriterion("encryption_options is not null");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsEqualTo(String value) {
            addCriterion("encryption_options =", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsNotEqualTo(String value) {
            addCriterion("encryption_options <>", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsGreaterThan(String value) {
            addCriterion("encryption_options >", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsGreaterThanOrEqualTo(String value) {
            addCriterion("encryption_options >=", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsLessThan(String value) {
            addCriterion("encryption_options <", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsLessThanOrEqualTo(String value) {
            addCriterion("encryption_options <=", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsLike(String value) {
            addCriterion("encryption_options like", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsNotLike(String value) {
            addCriterion("encryption_options not like", value, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsIn(List<String> values) {
            addCriterion("encryption_options in", values, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsNotIn(List<String> values) {
            addCriterion("encryption_options not in", values, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsBetween(String value1, String value2) {
            addCriterion("encryption_options between", value1, value2, "encryptionOptions");
            return (Criteria) this;
        }

        public Criteria andEncryptionOptionsNotBetween(String value1, String value2) {
            addCriterion("encryption_options not between", value1, value2, "encryptionOptions");
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