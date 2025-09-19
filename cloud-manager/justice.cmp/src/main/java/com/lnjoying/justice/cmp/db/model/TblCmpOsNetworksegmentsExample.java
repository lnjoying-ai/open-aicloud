package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsNetworksegmentsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsNetworksegmentsExample() {
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

        public Criteria andNetworkIdIsNull() {
            addCriterion("network_id is null");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIsNotNull() {
            addCriterion("network_id is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkIdEqualTo(String value) {
            addCriterion("network_id =", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotEqualTo(String value) {
            addCriterion("network_id <>", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdGreaterThan(String value) {
            addCriterion("network_id >", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdGreaterThanOrEqualTo(String value) {
            addCriterion("network_id >=", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLessThan(String value) {
            addCriterion("network_id <", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLessThanOrEqualTo(String value) {
            addCriterion("network_id <=", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdLike(String value) {
            addCriterion("network_id like", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotLike(String value) {
            addCriterion("network_id not like", value, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdIn(List<String> values) {
            addCriterion("network_id in", values, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotIn(List<String> values) {
            addCriterion("network_id not in", values, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdBetween(String value1, String value2) {
            addCriterion("network_id between", value1, value2, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkIdNotBetween(String value1, String value2) {
            addCriterion("network_id not between", value1, value2, "networkId");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeIsNull() {
            addCriterion("network_type is null");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeIsNotNull() {
            addCriterion("network_type is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeEqualTo(String value) {
            addCriterion("network_type =", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotEqualTo(String value) {
            addCriterion("network_type <>", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeGreaterThan(String value) {
            addCriterion("network_type >", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeGreaterThanOrEqualTo(String value) {
            addCriterion("network_type >=", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeLessThan(String value) {
            addCriterion("network_type <", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeLessThanOrEqualTo(String value) {
            addCriterion("network_type <=", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeLike(String value) {
            addCriterion("network_type like", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotLike(String value) {
            addCriterion("network_type not like", value, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeIn(List<String> values) {
            addCriterion("network_type in", values, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotIn(List<String> values) {
            addCriterion("network_type not in", values, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeBetween(String value1, String value2) {
            addCriterion("network_type between", value1, value2, "networkType");
            return (Criteria) this;
        }

        public Criteria andNetworkTypeNotBetween(String value1, String value2) {
            addCriterion("network_type not between", value1, value2, "networkType");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkIsNull() {
            addCriterion("physical_network is null");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkIsNotNull() {
            addCriterion("physical_network is not null");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkEqualTo(String value) {
            addCriterion("physical_network =", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkNotEqualTo(String value) {
            addCriterion("physical_network <>", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkGreaterThan(String value) {
            addCriterion("physical_network >", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkGreaterThanOrEqualTo(String value) {
            addCriterion("physical_network >=", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkLessThan(String value) {
            addCriterion("physical_network <", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkLessThanOrEqualTo(String value) {
            addCriterion("physical_network <=", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkLike(String value) {
            addCriterion("physical_network like", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkNotLike(String value) {
            addCriterion("physical_network not like", value, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkIn(List<String> values) {
            addCriterion("physical_network in", values, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkNotIn(List<String> values) {
            addCriterion("physical_network not in", values, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkBetween(String value1, String value2) {
            addCriterion("physical_network between", value1, value2, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andPhysicalNetworkNotBetween(String value1, String value2) {
            addCriterion("physical_network not between", value1, value2, "physicalNetwork");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdIsNull() {
            addCriterion("segmentation_id is null");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdIsNotNull() {
            addCriterion("segmentation_id is not null");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdEqualTo(Integer value) {
            addCriterion("segmentation_id =", value, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdNotEqualTo(Integer value) {
            addCriterion("segmentation_id <>", value, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdGreaterThan(Integer value) {
            addCriterion("segmentation_id >", value, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("segmentation_id >=", value, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdLessThan(Integer value) {
            addCriterion("segmentation_id <", value, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdLessThanOrEqualTo(Integer value) {
            addCriterion("segmentation_id <=", value, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdIn(List<Integer> values) {
            addCriterion("segmentation_id in", values, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdNotIn(List<Integer> values) {
            addCriterion("segmentation_id not in", values, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdBetween(Integer value1, Integer value2) {
            addCriterion("segmentation_id between", value1, value2, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andSegmentationIdNotBetween(Integer value1, Integer value2) {
            addCriterion("segmentation_id not between", value1, value2, "segmentationId");
            return (Criteria) this;
        }

        public Criteria andIsDynamicIsNull() {
            addCriterion("is_dynamic is null");
            return (Criteria) this;
        }

        public Criteria andIsDynamicIsNotNull() {
            addCriterion("is_dynamic is not null");
            return (Criteria) this;
        }

        public Criteria andIsDynamicEqualTo(Short value) {
            addCriterion("is_dynamic =", value, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicNotEqualTo(Short value) {
            addCriterion("is_dynamic <>", value, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicGreaterThan(Short value) {
            addCriterion("is_dynamic >", value, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicGreaterThanOrEqualTo(Short value) {
            addCriterion("is_dynamic >=", value, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicLessThan(Short value) {
            addCriterion("is_dynamic <", value, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicLessThanOrEqualTo(Short value) {
            addCriterion("is_dynamic <=", value, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicIn(List<Short> values) {
            addCriterion("is_dynamic in", values, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicNotIn(List<Short> values) {
            addCriterion("is_dynamic not in", values, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicBetween(Short value1, Short value2) {
            addCriterion("is_dynamic between", value1, value2, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andIsDynamicNotBetween(Short value1, Short value2) {
            addCriterion("is_dynamic not between", value1, value2, "isDynamic");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexIsNull() {
            addCriterion("segment_index is null");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexIsNotNull() {
            addCriterion("segment_index is not null");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexEqualTo(Integer value) {
            addCriterion("segment_index =", value, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexNotEqualTo(Integer value) {
            addCriterion("segment_index <>", value, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexGreaterThan(Integer value) {
            addCriterion("segment_index >", value, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("segment_index >=", value, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexLessThan(Integer value) {
            addCriterion("segment_index <", value, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexLessThanOrEqualTo(Integer value) {
            addCriterion("segment_index <=", value, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexIn(List<Integer> values) {
            addCriterion("segment_index in", values, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexNotIn(List<Integer> values) {
            addCriterion("segment_index not in", values, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexBetween(Integer value1, Integer value2) {
            addCriterion("segment_index between", value1, value2, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andSegmentIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("segment_index not between", value1, value2, "segmentIndex");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdIsNull() {
            addCriterion("standard_attr_id is null");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdIsNotNull() {
            addCriterion("standard_attr_id is not null");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdEqualTo(Long value) {
            addCriterion("standard_attr_id =", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdNotEqualTo(Long value) {
            addCriterion("standard_attr_id <>", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdGreaterThan(Long value) {
            addCriterion("standard_attr_id >", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdGreaterThanOrEqualTo(Long value) {
            addCriterion("standard_attr_id >=", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdLessThan(Long value) {
            addCriterion("standard_attr_id <", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdLessThanOrEqualTo(Long value) {
            addCriterion("standard_attr_id <=", value, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdIn(List<Long> values) {
            addCriterion("standard_attr_id in", values, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdNotIn(List<Long> values) {
            addCriterion("standard_attr_id not in", values, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdBetween(Long value1, Long value2) {
            addCriterion("standard_attr_id between", value1, value2, "standardAttrId");
            return (Criteria) this;
        }

        public Criteria andStandardAttrIdNotBetween(Long value1, Long value2) {
            addCriterion("standard_attr_id not between", value1, value2, "standardAttrId");
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

        public Criteria andRevisionNumberIsNull() {
            addCriterion("revision_number is null");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberIsNotNull() {
            addCriterion("revision_number is not null");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberEqualTo(Long value) {
            addCriterion("revision_number =", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberNotEqualTo(Long value) {
            addCriterion("revision_number <>", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberGreaterThan(Long value) {
            addCriterion("revision_number >", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberGreaterThanOrEqualTo(Long value) {
            addCriterion("revision_number >=", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberLessThan(Long value) {
            addCriterion("revision_number <", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberLessThanOrEqualTo(Long value) {
            addCriterion("revision_number <=", value, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberIn(List<Long> values) {
            addCriterion("revision_number in", values, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberNotIn(List<Long> values) {
            addCriterion("revision_number not in", values, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberBetween(Long value1, Long value2) {
            addCriterion("revision_number between", value1, value2, "revisionNumber");
            return (Criteria) this;
        }

        public Criteria andRevisionNumberNotBetween(Long value1, Long value2) {
            addCriterion("revision_number not between", value1, value2, "revisionNumber");
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