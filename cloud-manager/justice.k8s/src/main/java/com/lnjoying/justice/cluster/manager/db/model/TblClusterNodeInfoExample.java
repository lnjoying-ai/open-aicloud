package com.lnjoying.justice.cluster.manager.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblClusterNodeInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblClusterNodeInfoExample() {
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

        public Criteria andClusterIdIsNull() {
            addCriterion("cluster_id is null");
            return (Criteria) this;
        }

        public Criteria andClusterIdIsNotNull() {
            addCriterion("cluster_id is not null");
            return (Criteria) this;
        }

        public Criteria andClusterIdEqualTo(String value) {
            addCriterion("cluster_id =", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotEqualTo(String value) {
            addCriterion("cluster_id <>", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThan(String value) {
            addCriterion("cluster_id >", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_id >=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThan(String value) {
            addCriterion("cluster_id <", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLessThanOrEqualTo(String value) {
            addCriterion("cluster_id <=", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdLike(String value) {
            addCriterion("cluster_id like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotLike(String value) {
            addCriterion("cluster_id not like", value, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdIn(List<String> values) {
            addCriterion("cluster_id in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotIn(List<String> values) {
            addCriterion("cluster_id not in", values, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdBetween(String value1, String value2) {
            addCriterion("cluster_id between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andClusterIdNotBetween(String value1, String value2) {
            addCriterion("cluster_id not between", value1, value2, "clusterId");
            return (Criteria) this;
        }

        public Criteria andRegionIdIsNull() {
            addCriterion("region_id is null");
            return (Criteria) this;
        }

        public Criteria andRegionIdIsNotNull() {
            addCriterion("region_id is not null");
            return (Criteria) this;
        }

        public Criteria andRegionIdEqualTo(String value) {
            addCriterion("region_id =", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotEqualTo(String value) {
            addCriterion("region_id <>", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdGreaterThan(String value) {
            addCriterion("region_id >", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdGreaterThanOrEqualTo(String value) {
            addCriterion("region_id >=", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLessThan(String value) {
            addCriterion("region_id <", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLessThanOrEqualTo(String value) {
            addCriterion("region_id <=", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdLike(String value) {
            addCriterion("region_id like", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotLike(String value) {
            addCriterion("region_id not like", value, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdIn(List<String> values) {
            addCriterion("region_id in", values, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotIn(List<String> values) {
            addCriterion("region_id not in", values, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdBetween(String value1, String value2) {
            addCriterion("region_id between", value1, value2, "regionId");
            return (Criteria) this;
        }

        public Criteria andRegionIdNotBetween(String value1, String value2) {
            addCriterion("region_id not between", value1, value2, "regionId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNull() {
            addCriterion("site_id is null");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNotNull() {
            addCriterion("site_id is not null");
            return (Criteria) this;
        }

        public Criteria andSiteIdEqualTo(String value) {
            addCriterion("site_id =", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotEqualTo(String value) {
            addCriterion("site_id <>", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThan(String value) {
            addCriterion("site_id >", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThanOrEqualTo(String value) {
            addCriterion("site_id >=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThan(String value) {
            addCriterion("site_id <", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThanOrEqualTo(String value) {
            addCriterion("site_id <=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLike(String value) {
            addCriterion("site_id like", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotLike(String value) {
            addCriterion("site_id not like", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIn(List<String> values) {
            addCriterion("site_id in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotIn(List<String> values) {
            addCriterion("site_id not in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdBetween(String value1, String value2) {
            addCriterion("site_id between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotBetween(String value1, String value2) {
            addCriterion("site_id not between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNull() {
            addCriterion("node_id is null");
            return (Criteria) this;
        }

        public Criteria andNodeIdIsNotNull() {
            addCriterion("node_id is not null");
            return (Criteria) this;
        }

        public Criteria andNodeIdEqualTo(String value) {
            addCriterion("node_id =", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotEqualTo(String value) {
            addCriterion("node_id <>", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThan(String value) {
            addCriterion("node_id >", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("node_id >=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThan(String value) {
            addCriterion("node_id <", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLessThanOrEqualTo(String value) {
            addCriterion("node_id <=", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdLike(String value) {
            addCriterion("node_id like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotLike(String value) {
            addCriterion("node_id not like", value, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdIn(List<String> values) {
            addCriterion("node_id in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotIn(List<String> values) {
            addCriterion("node_id not in", values, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdBetween(String value1, String value2) {
            addCriterion("node_id between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeIdNotBetween(String value1, String value2) {
            addCriterion("node_id not between", value1, value2, "nodeId");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNull() {
            addCriterion("node_name is null");
            return (Criteria) this;
        }

        public Criteria andNodeNameIsNotNull() {
            addCriterion("node_name is not null");
            return (Criteria) this;
        }

        public Criteria andNodeNameEqualTo(String value) {
            addCriterion("node_name =", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotEqualTo(String value) {
            addCriterion("node_name <>", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThan(String value) {
            addCriterion("node_name >", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameGreaterThanOrEqualTo(String value) {
            addCriterion("node_name >=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThan(String value) {
            addCriterion("node_name <", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLessThanOrEqualTo(String value) {
            addCriterion("node_name <=", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameLike(String value) {
            addCriterion("node_name like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotLike(String value) {
            addCriterion("node_name not like", value, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameIn(List<String> values) {
            addCriterion("node_name in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotIn(List<String> values) {
            addCriterion("node_name not in", values, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameBetween(String value1, String value2) {
            addCriterion("node_name between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andNodeNameNotBetween(String value1, String value2) {
            addCriterion("node_name not between", value1, value2, "nodeName");
            return (Criteria) this;
        }

        public Criteria andClusterRolesIsNull() {
            addCriterion("cluster_roles is null");
            return (Criteria) this;
        }

        public Criteria andClusterRolesIsNotNull() {
            addCriterion("cluster_roles is not null");
            return (Criteria) this;
        }

        public Criteria andClusterRolesEqualTo(String value) {
            addCriterion("cluster_roles =", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesNotEqualTo(String value) {
            addCriterion("cluster_roles <>", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesGreaterThan(String value) {
            addCriterion("cluster_roles >", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesGreaterThanOrEqualTo(String value) {
            addCriterion("cluster_roles >=", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesLessThan(String value) {
            addCriterion("cluster_roles <", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesLessThanOrEqualTo(String value) {
            addCriterion("cluster_roles <=", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesLike(String value) {
            addCriterion("cluster_roles like", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesNotLike(String value) {
            addCriterion("cluster_roles not like", value, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesIn(List<String> values) {
            addCriterion("cluster_roles in", values, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesNotIn(List<String> values) {
            addCriterion("cluster_roles not in", values, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesBetween(String value1, String value2) {
            addCriterion("cluster_roles between", value1, value2, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andClusterRolesNotBetween(String value1, String value2) {
            addCriterion("cluster_roles not between", value1, value2, "clusterRoles");
            return (Criteria) this;
        }

        public Criteria andInternalAddressIsNull() {
            addCriterion("internal_address is null");
            return (Criteria) this;
        }

        public Criteria andInternalAddressIsNotNull() {
            addCriterion("internal_address is not null");
            return (Criteria) this;
        }

        public Criteria andInternalAddressEqualTo(String value) {
            addCriterion("internal_address =", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressNotEqualTo(String value) {
            addCriterion("internal_address <>", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressGreaterThan(String value) {
            addCriterion("internal_address >", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressGreaterThanOrEqualTo(String value) {
            addCriterion("internal_address >=", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressLessThan(String value) {
            addCriterion("internal_address <", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressLessThanOrEqualTo(String value) {
            addCriterion("internal_address <=", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressLike(String value) {
            addCriterion("internal_address like", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressNotLike(String value) {
            addCriterion("internal_address not like", value, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressIn(List<String> values) {
            addCriterion("internal_address in", values, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressNotIn(List<String> values) {
            addCriterion("internal_address not in", values, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressBetween(String value1, String value2) {
            addCriterion("internal_address between", value1, value2, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andInternalAddressNotBetween(String value1, String value2) {
            addCriterion("internal_address not between", value1, value2, "internalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressIsNull() {
            addCriterion("external_address is null");
            return (Criteria) this;
        }

        public Criteria andExternalAddressIsNotNull() {
            addCriterion("external_address is not null");
            return (Criteria) this;
        }

        public Criteria andExternalAddressEqualTo(String value) {
            addCriterion("external_address =", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressNotEqualTo(String value) {
            addCriterion("external_address <>", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressGreaterThan(String value) {
            addCriterion("external_address >", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressGreaterThanOrEqualTo(String value) {
            addCriterion("external_address >=", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressLessThan(String value) {
            addCriterion("external_address <", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressLessThanOrEqualTo(String value) {
            addCriterion("external_address <=", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressLike(String value) {
            addCriterion("external_address like", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressNotLike(String value) {
            addCriterion("external_address not like", value, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressIn(List<String> values) {
            addCriterion("external_address in", values, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressNotIn(List<String> values) {
            addCriterion("external_address not in", values, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressBetween(String value1, String value2) {
            addCriterion("external_address between", value1, value2, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andExternalAddressNotBetween(String value1, String value2) {
            addCriterion("external_address not between", value1, value2, "externalAddress");
            return (Criteria) this;
        }

        public Criteria andDockerInfoIsNull() {
            addCriterion("docker_info is null");
            return (Criteria) this;
        }

        public Criteria andDockerInfoIsNotNull() {
            addCriterion("docker_info is not null");
            return (Criteria) this;
        }

        public Criteria andDockerInfoEqualTo(String value) {
            addCriterion("docker_info =", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoNotEqualTo(String value) {
            addCriterion("docker_info <>", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoGreaterThan(String value) {
            addCriterion("docker_info >", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoGreaterThanOrEqualTo(String value) {
            addCriterion("docker_info >=", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoLessThan(String value) {
            addCriterion("docker_info <", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoLessThanOrEqualTo(String value) {
            addCriterion("docker_info <=", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoLike(String value) {
            addCriterion("docker_info like", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoNotLike(String value) {
            addCriterion("docker_info not like", value, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoIn(List<String> values) {
            addCriterion("docker_info in", values, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoNotIn(List<String> values) {
            addCriterion("docker_info not in", values, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoBetween(String value1, String value2) {
            addCriterion("docker_info between", value1, value2, "dockerInfo");
            return (Criteria) this;
        }

        public Criteria andDockerInfoNotBetween(String value1, String value2) {
            addCriterion("docker_info not between", value1, value2, "dockerInfo");
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

        public Criteria andStatusEqualTo(Integer value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Integer value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Integer value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Integer value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Integer value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Integer> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Integer> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Integer value1, Integer value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andLabelsIsNull() {
            addCriterion("labels is null");
            return (Criteria) this;
        }

        public Criteria andLabelsIsNotNull() {
            addCriterion("labels is not null");
            return (Criteria) this;
        }

        public Criteria andLabelsEqualTo(String value) {
            addCriterion("labels =", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotEqualTo(String value) {
            addCriterion("labels <>", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsGreaterThan(String value) {
            addCriterion("labels >", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsGreaterThanOrEqualTo(String value) {
            addCriterion("labels >=", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLessThan(String value) {
            addCriterion("labels <", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLessThanOrEqualTo(String value) {
            addCriterion("labels <=", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsLike(String value) {
            addCriterion("labels like", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotLike(String value) {
            addCriterion("labels not like", value, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsIn(List<String> values) {
            addCriterion("labels in", values, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotIn(List<String> values) {
            addCriterion("labels not in", values, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsBetween(String value1, String value2) {
            addCriterion("labels between", value1, value2, "labels");
            return (Criteria) this;
        }

        public Criteria andLabelsNotBetween(String value1, String value2) {
            addCriterion("labels not between", value1, value2, "labels");
            return (Criteria) this;
        }

        public Criteria andTaintsIsNull() {
            addCriterion("taints is null");
            return (Criteria) this;
        }

        public Criteria andTaintsIsNotNull() {
            addCriterion("taints is not null");
            return (Criteria) this;
        }

        public Criteria andTaintsEqualTo(String value) {
            addCriterion("taints =", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsNotEqualTo(String value) {
            addCriterion("taints <>", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsGreaterThan(String value) {
            addCriterion("taints >", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsGreaterThanOrEqualTo(String value) {
            addCriterion("taints >=", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsLessThan(String value) {
            addCriterion("taints <", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsLessThanOrEqualTo(String value) {
            addCriterion("taints <=", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsLike(String value) {
            addCriterion("taints like", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsNotLike(String value) {
            addCriterion("taints not like", value, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsIn(List<String> values) {
            addCriterion("taints in", values, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsNotIn(List<String> values) {
            addCriterion("taints not in", values, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsBetween(String value1, String value2) {
            addCriterion("taints between", value1, value2, "taints");
            return (Criteria) this;
        }

        public Criteria andTaintsNotBetween(String value1, String value2) {
            addCriterion("taints not between", value1, value2, "taints");
            return (Criteria) this;
        }

        public Criteria andAnnotationsIsNull() {
            addCriterion("annotations is null");
            return (Criteria) this;
        }

        public Criteria andAnnotationsIsNotNull() {
            addCriterion("annotations is not null");
            return (Criteria) this;
        }

        public Criteria andAnnotationsEqualTo(String value) {
            addCriterion("annotations =", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotEqualTo(String value) {
            addCriterion("annotations <>", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsGreaterThan(String value) {
            addCriterion("annotations >", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsGreaterThanOrEqualTo(String value) {
            addCriterion("annotations >=", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsLessThan(String value) {
            addCriterion("annotations <", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsLessThanOrEqualTo(String value) {
            addCriterion("annotations <=", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsLike(String value) {
            addCriterion("annotations like", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotLike(String value) {
            addCriterion("annotations not like", value, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsIn(List<String> values) {
            addCriterion("annotations in", values, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotIn(List<String> values) {
            addCriterion("annotations not in", values, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsBetween(String value1, String value2) {
            addCriterion("annotations between", value1, value2, "annotations");
            return (Criteria) this;
        }

        public Criteria andAnnotationsNotBetween(String value1, String value2) {
            addCriterion("annotations not between", value1, value2, "annotations");
            return (Criteria) this;
        }

        public Criteria andDeployPlanIsNull() {
            addCriterion("deploy_plan is null");
            return (Criteria) this;
        }

        public Criteria andDeployPlanIsNotNull() {
            addCriterion("deploy_plan is not null");
            return (Criteria) this;
        }

        public Criteria andDeployPlanEqualTo(String value) {
            addCriterion("deploy_plan =", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanNotEqualTo(String value) {
            addCriterion("deploy_plan <>", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanGreaterThan(String value) {
            addCriterion("deploy_plan >", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanGreaterThanOrEqualTo(String value) {
            addCriterion("deploy_plan >=", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanLessThan(String value) {
            addCriterion("deploy_plan <", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanLessThanOrEqualTo(String value) {
            addCriterion("deploy_plan <=", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanLike(String value) {
            addCriterion("deploy_plan like", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanNotLike(String value) {
            addCriterion("deploy_plan not like", value, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanIn(List<String> values) {
            addCriterion("deploy_plan in", values, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanNotIn(List<String> values) {
            addCriterion("deploy_plan not in", values, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanBetween(String value1, String value2) {
            addCriterion("deploy_plan between", value1, value2, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andDeployPlanNotBetween(String value1, String value2) {
            addCriterion("deploy_plan not between", value1, value2, "deployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanIsNull() {
            addCriterion("undeploy_plan is null");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanIsNotNull() {
            addCriterion("undeploy_plan is not null");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanEqualTo(String value) {
            addCriterion("undeploy_plan =", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanNotEqualTo(String value) {
            addCriterion("undeploy_plan <>", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanGreaterThan(String value) {
            addCriterion("undeploy_plan >", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanGreaterThanOrEqualTo(String value) {
            addCriterion("undeploy_plan >=", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanLessThan(String value) {
            addCriterion("undeploy_plan <", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanLessThanOrEqualTo(String value) {
            addCriterion("undeploy_plan <=", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanLike(String value) {
            addCriterion("undeploy_plan like", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanNotLike(String value) {
            addCriterion("undeploy_plan not like", value, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanIn(List<String> values) {
            addCriterion("undeploy_plan in", values, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanNotIn(List<String> values) {
            addCriterion("undeploy_plan not in", values, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanBetween(String value1, String value2) {
            addCriterion("undeploy_plan between", value1, value2, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andUndeployPlanNotBetween(String value1, String value2) {
            addCriterion("undeploy_plan not between", value1, value2, "undeployPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanIsNull() {
            addCriterion("monitor_plan is null");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanIsNotNull() {
            addCriterion("monitor_plan is not null");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanEqualTo(String value) {
            addCriterion("monitor_plan =", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanNotEqualTo(String value) {
            addCriterion("monitor_plan <>", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanGreaterThan(String value) {
            addCriterion("monitor_plan >", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanGreaterThanOrEqualTo(String value) {
            addCriterion("monitor_plan >=", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanLessThan(String value) {
            addCriterion("monitor_plan <", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanLessThanOrEqualTo(String value) {
            addCriterion("monitor_plan <=", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanLike(String value) {
            addCriterion("monitor_plan like", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanNotLike(String value) {
            addCriterion("monitor_plan not like", value, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanIn(List<String> values) {
            addCriterion("monitor_plan in", values, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanNotIn(List<String> values) {
            addCriterion("monitor_plan not in", values, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanBetween(String value1, String value2) {
            addCriterion("monitor_plan between", value1, value2, "monitorPlan");
            return (Criteria) this;
        }

        public Criteria andMonitorPlanNotBetween(String value1, String value2) {
            addCriterion("monitor_plan not between", value1, value2, "monitorPlan");
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