package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpVmInstanceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected Integer startRow;

    protected Integer pageSize;

    public Integer getStartRow()
    {
        return startRow;
    }

    public void setStartRow(Integer startRow) { this.startRow = startRow;}

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(Integer pageSize)
    {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public TblCmpVmInstanceExample() {
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

        public Criteria andInstanceIdFromAgentIsNull() {
            addCriterion("instance_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentIsNotNull() {
            addCriterion("instance_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentEqualTo(String value) {
            addCriterion("instance_id_from_agent =", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotEqualTo(String value) {
            addCriterion("instance_id_from_agent <>", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentGreaterThan(String value) {
            addCriterion("instance_id_from_agent >", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("instance_id_from_agent >=", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentLessThan(String value) {
            addCriterion("instance_id_from_agent <", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("instance_id_from_agent <=", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentLike(String value) {
            addCriterion("instance_id_from_agent like", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotLike(String value) {
            addCriterion("instance_id_from_agent not like", value, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentIn(List<String> values) {
            addCriterion("instance_id_from_agent in", values, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotIn(List<String> values) {
            addCriterion("instance_id_from_agent not in", values, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentBetween(String value1, String value2) {
            addCriterion("instance_id_from_agent between", value1, value2, "instanceIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andInstanceIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("instance_id_from_agent not between", value1, value2, "instanceIdFromAgent");
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

        public Criteria andPhaseStatusIsNull() {
            addCriterion("phase_status is null");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIsNotNull() {
            addCriterion("phase_status is not null");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusEqualTo(Integer value) {
            addCriterion("phase_status =", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotEqualTo(Integer value) {
            addCriterion("phase_status <>", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusGreaterThan(Integer value) {
            addCriterion("phase_status >", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("phase_status >=", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusLessThan(Integer value) {
            addCriterion("phase_status <", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusLessThanOrEqualTo(Integer value) {
            addCriterion("phase_status <=", value, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusIn(List<Integer> values) {
            addCriterion("phase_status in", values, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotIn(List<Integer> values) {
            addCriterion("phase_status not in", values, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusBetween(Integer value1, Integer value2) {
            addCriterion("phase_status between", value1, value2, "phaseStatus");
            return (Criteria) this;
        }

        public Criteria andPhaseStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("phase_status not between", value1, value2, "phaseStatus");
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

        public Criteria andFlavorIdIsNull() {
            addCriterion("flavor_id is null");
            return (Criteria) this;
        }

        public Criteria andFlavorIdIsNotNull() {
            addCriterion("flavor_id is not null");
            return (Criteria) this;
        }

        public Criteria andFlavorIdEqualTo(String value) {
            addCriterion("flavor_id =", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdNotEqualTo(String value) {
            addCriterion("flavor_id <>", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdGreaterThan(String value) {
            addCriterion("flavor_id >", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdGreaterThanOrEqualTo(String value) {
            addCriterion("flavor_id >=", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdLessThan(String value) {
            addCriterion("flavor_id <", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdLessThanOrEqualTo(String value) {
            addCriterion("flavor_id <=", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdLike(String value) {
            addCriterion("flavor_id like", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdNotLike(String value) {
            addCriterion("flavor_id not like", value, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdIn(List<String> values) {
            addCriterion("flavor_id in", values, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdNotIn(List<String> values) {
            addCriterion("flavor_id not in", values, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdBetween(String value1, String value2) {
            addCriterion("flavor_id between", value1, value2, "flavorId");
            return (Criteria) this;
        }

        public Criteria andFlavorIdNotBetween(String value1, String value2) {
            addCriterion("flavor_id not between", value1, value2, "flavorId");
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

        public Criteria andVpcIdIsNull() {
            addCriterion("vpc_id is null");
            return (Criteria) this;
        }

        public Criteria andVpcIdIsNotNull() {
            addCriterion("vpc_id is not null");
            return (Criteria) this;
        }

        public Criteria andVpcIdEqualTo(String value) {
            addCriterion("vpc_id =", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotEqualTo(String value) {
            addCriterion("vpc_id <>", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdGreaterThan(String value) {
            addCriterion("vpc_id >", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdGreaterThanOrEqualTo(String value) {
            addCriterion("vpc_id >=", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdLessThan(String value) {
            addCriterion("vpc_id <", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdLessThanOrEqualTo(String value) {
            addCriterion("vpc_id <=", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdLike(String value) {
            addCriterion("vpc_id like", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotLike(String value) {
            addCriterion("vpc_id not like", value, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdIn(List<String> values) {
            addCriterion("vpc_id in", values, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotIn(List<String> values) {
            addCriterion("vpc_id not in", values, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdBetween(String value1, String value2) {
            addCriterion("vpc_id between", value1, value2, "vpcId");
            return (Criteria) this;
        }

        public Criteria andVpcIdNotBetween(String value1, String value2) {
            addCriterion("vpc_id not between", value1, value2, "vpcId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdIsNull() {
            addCriterion("subnet_id is null");
            return (Criteria) this;
        }

        public Criteria andSubnetIdIsNotNull() {
            addCriterion("subnet_id is not null");
            return (Criteria) this;
        }

        public Criteria andSubnetIdEqualTo(String value) {
            addCriterion("subnet_id =", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotEqualTo(String value) {
            addCriterion("subnet_id <>", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdGreaterThan(String value) {
            addCriterion("subnet_id >", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdGreaterThanOrEqualTo(String value) {
            addCriterion("subnet_id >=", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdLessThan(String value) {
            addCriterion("subnet_id <", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdLessThanOrEqualTo(String value) {
            addCriterion("subnet_id <=", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdLike(String value) {
            addCriterion("subnet_id like", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotLike(String value) {
            addCriterion("subnet_id not like", value, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdIn(List<String> values) {
            addCriterion("subnet_id in", values, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotIn(List<String> values) {
            addCriterion("subnet_id not in", values, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdBetween(String value1, String value2) {
            addCriterion("subnet_id between", value1, value2, "subnetId");
            return (Criteria) this;
        }

        public Criteria andSubnetIdNotBetween(String value1, String value2) {
            addCriterion("subnet_id not between", value1, value2, "subnetId");
            return (Criteria) this;
        }

        public Criteria andPortIdIsNull() {
            addCriterion("port_id is null");
            return (Criteria) this;
        }

        public Criteria andPortIdIsNotNull() {
            addCriterion("port_id is not null");
            return (Criteria) this;
        }

        public Criteria andPortIdEqualTo(String value) {
            addCriterion("port_id =", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdNotEqualTo(String value) {
            addCriterion("port_id <>", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdGreaterThan(String value) {
            addCriterion("port_id >", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdGreaterThanOrEqualTo(String value) {
            addCriterion("port_id >=", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdLessThan(String value) {
            addCriterion("port_id <", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdLessThanOrEqualTo(String value) {
            addCriterion("port_id <=", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdLike(String value) {
            addCriterion("port_id like", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdNotLike(String value) {
            addCriterion("port_id not like", value, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdIn(List<String> values) {
            addCriterion("port_id in", values, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdNotIn(List<String> values) {
            addCriterion("port_id not in", values, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdBetween(String value1, String value2) {
            addCriterion("port_id between", value1, value2, "portId");
            return (Criteria) this;
        }

        public Criteria andPortIdNotBetween(String value1, String value2) {
            addCriterion("port_id not between", value1, value2, "portId");
            return (Criteria) this;
        }

        public Criteria andStaticIpIsNull() {
            addCriterion("static_ip is null");
            return (Criteria) this;
        }

        public Criteria andStaticIpIsNotNull() {
            addCriterion("static_ip is not null");
            return (Criteria) this;
        }

        public Criteria andStaticIpEqualTo(String value) {
            addCriterion("static_ip =", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotEqualTo(String value) {
            addCriterion("static_ip <>", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpGreaterThan(String value) {
            addCriterion("static_ip >", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpGreaterThanOrEqualTo(String value) {
            addCriterion("static_ip >=", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpLessThan(String value) {
            addCriterion("static_ip <", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpLessThanOrEqualTo(String value) {
            addCriterion("static_ip <=", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpLike(String value) {
            addCriterion("static_ip like", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotLike(String value) {
            addCriterion("static_ip not like", value, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpIn(List<String> values) {
            addCriterion("static_ip in", values, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotIn(List<String> values) {
            addCriterion("static_ip not in", values, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpBetween(String value1, String value2) {
            addCriterion("static_ip between", value1, value2, "staticIp");
            return (Criteria) this;
        }

        public Criteria andStaticIpNotBetween(String value1, String value2) {
            addCriterion("static_ip not between", value1, value2, "staticIp");
            return (Criteria) this;
        }

        public Criteria andHostNameIsNull() {
            addCriterion("host_name is null");
            return (Criteria) this;
        }

        public Criteria andHostNameIsNotNull() {
            addCriterion("host_name is not null");
            return (Criteria) this;
        }

        public Criteria andHostNameEqualTo(String value) {
            addCriterion("host_name =", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotEqualTo(String value) {
            addCriterion("host_name <>", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameGreaterThan(String value) {
            addCriterion("host_name >", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameGreaterThanOrEqualTo(String value) {
            addCriterion("host_name >=", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameLessThan(String value) {
            addCriterion("host_name <", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameLessThanOrEqualTo(String value) {
            addCriterion("host_name <=", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameLike(String value) {
            addCriterion("host_name like", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotLike(String value) {
            addCriterion("host_name not like", value, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameIn(List<String> values) {
            addCriterion("host_name in", values, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotIn(List<String> values) {
            addCriterion("host_name not in", values, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameBetween(String value1, String value2) {
            addCriterion("host_name between", value1, value2, "hostName");
            return (Criteria) this;
        }

        public Criteria andHostNameNotBetween(String value1, String value2) {
            addCriterion("host_name not between", value1, value2, "hostName");
            return (Criteria) this;
        }

        public Criteria andSysUsernameIsNull() {
            addCriterion("sys_username is null");
            return (Criteria) this;
        }

        public Criteria andSysUsernameIsNotNull() {
            addCriterion("sys_username is not null");
            return (Criteria) this;
        }

        public Criteria andSysUsernameEqualTo(String value) {
            addCriterion("sys_username =", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotEqualTo(String value) {
            addCriterion("sys_username <>", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameGreaterThan(String value) {
            addCriterion("sys_username >", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameGreaterThanOrEqualTo(String value) {
            addCriterion("sys_username >=", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameLessThan(String value) {
            addCriterion("sys_username <", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameLessThanOrEqualTo(String value) {
            addCriterion("sys_username <=", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameLike(String value) {
            addCriterion("sys_username like", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotLike(String value) {
            addCriterion("sys_username not like", value, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameIn(List<String> values) {
            addCriterion("sys_username in", values, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotIn(List<String> values) {
            addCriterion("sys_username not in", values, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameBetween(String value1, String value2) {
            addCriterion("sys_username between", value1, value2, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysUsernameNotBetween(String value1, String value2) {
            addCriterion("sys_username not between", value1, value2, "sysUsername");
            return (Criteria) this;
        }

        public Criteria andSysPasswordIsNull() {
            addCriterion("sys_password is null");
            return (Criteria) this;
        }

        public Criteria andSysPasswordIsNotNull() {
            addCriterion("sys_password is not null");
            return (Criteria) this;
        }

        public Criteria andSysPasswordEqualTo(String value) {
            addCriterion("sys_password =", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotEqualTo(String value) {
            addCriterion("sys_password <>", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordGreaterThan(String value) {
            addCriterion("sys_password >", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("sys_password >=", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordLessThan(String value) {
            addCriterion("sys_password <", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordLessThanOrEqualTo(String value) {
            addCriterion("sys_password <=", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordLike(String value) {
            addCriterion("sys_password like", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotLike(String value) {
            addCriterion("sys_password not like", value, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordIn(List<String> values) {
            addCriterion("sys_password in", values, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotIn(List<String> values) {
            addCriterion("sys_password not in", values, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordBetween(String value1, String value2) {
            addCriterion("sys_password between", value1, value2, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andSysPasswordNotBetween(String value1, String value2) {
            addCriterion("sys_password not between", value1, value2, "sysPassword");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdIsNull() {
            addCriterion("pubkey_id is null");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdIsNotNull() {
            addCriterion("pubkey_id is not null");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdEqualTo(String value) {
            addCriterion("pubkey_id =", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotEqualTo(String value) {
            addCriterion("pubkey_id <>", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdGreaterThan(String value) {
            addCriterion("pubkey_id >", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdGreaterThanOrEqualTo(String value) {
            addCriterion("pubkey_id >=", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdLessThan(String value) {
            addCriterion("pubkey_id <", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdLessThanOrEqualTo(String value) {
            addCriterion("pubkey_id <=", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdLike(String value) {
            addCriterion("pubkey_id like", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotLike(String value) {
            addCriterion("pubkey_id not like", value, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdIn(List<String> values) {
            addCriterion("pubkey_id in", values, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotIn(List<String> values) {
            addCriterion("pubkey_id not in", values, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdBetween(String value1, String value2) {
            addCriterion("pubkey_id between", value1, value2, "pubkeyId");
            return (Criteria) this;
        }

        public Criteria andPubkeyIdNotBetween(String value1, String value2) {
            addCriterion("pubkey_id not between", value1, value2, "pubkeyId");
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

        public Criteria andLastNodeIdIsNull() {
            addCriterion("last_node_id is null");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdIsNotNull() {
            addCriterion("last_node_id is not null");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdEqualTo(String value) {
            addCriterion("last_node_id =", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdNotEqualTo(String value) {
            addCriterion("last_node_id <>", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdGreaterThan(String value) {
            addCriterion("last_node_id >", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("last_node_id >=", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdLessThan(String value) {
            addCriterion("last_node_id <", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdLessThanOrEqualTo(String value) {
            addCriterion("last_node_id <=", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdLike(String value) {
            addCriterion("last_node_id like", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdNotLike(String value) {
            addCriterion("last_node_id not like", value, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdIn(List<String> values) {
            addCriterion("last_node_id in", values, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdNotIn(List<String> values) {
            addCriterion("last_node_id not in", values, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdBetween(String value1, String value2) {
            addCriterion("last_node_id between", value1, value2, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andLastNodeIdNotBetween(String value1, String value2) {
            addCriterion("last_node_id not between", value1, value2, "lastNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdIsNull() {
            addCriterion("dest_node_id is null");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdIsNotNull() {
            addCriterion("dest_node_id is not null");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdEqualTo(String value) {
            addCriterion("dest_node_id =", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdNotEqualTo(String value) {
            addCriterion("dest_node_id <>", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdGreaterThan(String value) {
            addCriterion("dest_node_id >", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdGreaterThanOrEqualTo(String value) {
            addCriterion("dest_node_id >=", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdLessThan(String value) {
            addCriterion("dest_node_id <", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdLessThanOrEqualTo(String value) {
            addCriterion("dest_node_id <=", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdLike(String value) {
            addCriterion("dest_node_id like", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdNotLike(String value) {
            addCriterion("dest_node_id not like", value, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdIn(List<String> values) {
            addCriterion("dest_node_id in", values, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdNotIn(List<String> values) {
            addCriterion("dest_node_id not in", values, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdBetween(String value1, String value2) {
            addCriterion("dest_node_id between", value1, value2, "destNodeId");
            return (Criteria) this;
        }

        public Criteria andDestNodeIdNotBetween(String value1, String value2) {
            addCriterion("dest_node_id not between", value1, value2, "destNodeId");
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

        public Criteria andStoragePoolIdIsNull() {
            addCriterion("storage_pool_id is null");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdIsNotNull() {
            addCriterion("storage_pool_id is not null");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdEqualTo(String value) {
            addCriterion("storage_pool_id =", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdNotEqualTo(String value) {
            addCriterion("storage_pool_id <>", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdGreaterThan(String value) {
            addCriterion("storage_pool_id >", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdGreaterThanOrEqualTo(String value) {
            addCriterion("storage_pool_id >=", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdLessThan(String value) {
            addCriterion("storage_pool_id <", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdLessThanOrEqualTo(String value) {
            addCriterion("storage_pool_id <=", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdLike(String value) {
            addCriterion("storage_pool_id like", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdNotLike(String value) {
            addCriterion("storage_pool_id not like", value, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdIn(List<String> values) {
            addCriterion("storage_pool_id in", values, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdNotIn(List<String> values) {
            addCriterion("storage_pool_id not in", values, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdBetween(String value1, String value2) {
            addCriterion("storage_pool_id between", value1, value2, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andStoragePoolIdNotBetween(String value1, String value2) {
            addCriterion("storage_pool_id not between", value1, value2, "storagePoolId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdIsNull() {
            addCriterion("instance_group_id is null");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdIsNullOrIsEmpty() {
            addCriterion("( instance_group_id is null or instance_group_id = '' )");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdIsNotNull() {
            addCriterion("instance_group_id is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdEqualTo(String value) {
            addCriterion("instance_group_id =", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdNotEqualTo(String value) {
            addCriterion("instance_group_id <>", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdGreaterThan(String value) {
            addCriterion("instance_group_id >", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdGreaterThanOrEqualTo(String value) {
            addCriterion("instance_group_id >=", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdLessThan(String value) {
            addCriterion("instance_group_id <", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdLessThanOrEqualTo(String value) {
            addCriterion("instance_group_id <=", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdLike(String value) {
            addCriterion("instance_group_id like", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdNotLike(String value) {
            addCriterion("instance_group_id not like", value, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdIn(List<String> values) {
            addCriterion("instance_group_id in", values, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdNotIn(List<String> values) {
            addCriterion("instance_group_id not in", values, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdBetween(String value1, String value2) {
            addCriterion("instance_group_id between", value1, value2, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andInstanceGroupIdNotBetween(String value1, String value2) {
            addCriterion("instance_group_id not between", value1, value2, "instanceGroupId");
            return (Criteria) this;
        }

        public Criteria andBootDevIsNull() {
            addCriterion("boot_dev is null");
            return (Criteria) this;
        }

        public Criteria andBootDevIsNotNull() {
            addCriterion("boot_dev is not null");
            return (Criteria) this;
        }

        public Criteria andBootDevEqualTo(String value) {
            addCriterion("boot_dev =", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevNotEqualTo(String value) {
            addCriterion("boot_dev <>", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevGreaterThan(String value) {
            addCriterion("boot_dev >", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevGreaterThanOrEqualTo(String value) {
            addCriterion("boot_dev >=", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevLessThan(String value) {
            addCriterion("boot_dev <", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevLessThanOrEqualTo(String value) {
            addCriterion("boot_dev <=", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevLike(String value) {
            addCriterion("boot_dev like", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevNotLike(String value) {
            addCriterion("boot_dev not like", value, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevIn(List<String> values) {
            addCriterion("boot_dev in", values, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevNotIn(List<String> values) {
            addCriterion("boot_dev not in", values, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevBetween(String value1, String value2) {
            addCriterion("boot_dev between", value1, value2, "bootDev");
            return (Criteria) this;
        }

        public Criteria andBootDevNotBetween(String value1, String value2) {
            addCriterion("boot_dev not between", value1, value2, "bootDev");
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

        public Criteria andCmpTenantIdIsNull() {
            addCriterion("cmp_tenant_id is null");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdIsNotNull() {
            addCriterion("cmp_tenant_id is not null");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdEqualTo(String value) {
            addCriterion("cmp_tenant_id =", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdNotEqualTo(String value) {
            addCriterion("cmp_tenant_id <>", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdGreaterThan(String value) {
            addCriterion("cmp_tenant_id >", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdGreaterThanOrEqualTo(String value) {
            addCriterion("cmp_tenant_id >=", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdLessThan(String value) {
            addCriterion("cmp_tenant_id <", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdLessThanOrEqualTo(String value) {
            addCriterion("cmp_tenant_id <=", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdLike(String value) {
            addCriterion("cmp_tenant_id like", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdNotLike(String value) {
            addCriterion("cmp_tenant_id not like", value, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdIn(List<String> values) {
            addCriterion("cmp_tenant_id in", values, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdNotIn(List<String> values) {
            addCriterion("cmp_tenant_id not in", values, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdBetween(String value1, String value2) {
            addCriterion("cmp_tenant_id between", value1, value2, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpTenantIdNotBetween(String value1, String value2) {
            addCriterion("cmp_tenant_id not between", value1, value2, "cmpTenantId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdIsNull() {
            addCriterion("cmp_user_id is null");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdIsNotNull() {
            addCriterion("cmp_user_id is not null");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdEqualTo(String value) {
            addCriterion("cmp_user_id =", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdNotEqualTo(String value) {
            addCriterion("cmp_user_id <>", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdGreaterThan(String value) {
            addCriterion("cmp_user_id >", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdGreaterThanOrEqualTo(String value) {
            addCriterion("cmp_user_id >=", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdLessThan(String value) {
            addCriterion("cmp_user_id <", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdLessThanOrEqualTo(String value) {
            addCriterion("cmp_user_id <=", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdLike(String value) {
            addCriterion("cmp_user_id like", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdNotLike(String value) {
            addCriterion("cmp_user_id not like", value, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdIn(List<String> values) {
            addCriterion("cmp_user_id in", values, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdNotIn(List<String> values) {
            addCriterion("cmp_user_id not in", values, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdBetween(String value1, String value2) {
            addCriterion("cmp_user_id between", value1, value2, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCmpUserIdNotBetween(String value1, String value2) {
            addCriterion("cmp_user_id not between", value1, value2, "cmpUserId");
            return (Criteria) this;
        }

        public Criteria andCpuCountIsNull() {
            addCriterion("cpu_count is null");
            return (Criteria) this;
        }

        public Criteria andCpuCountIsNotNull() {
            addCriterion("cpu_count is not null");
            return (Criteria) this;
        }

        public Criteria andCpuCountEqualTo(Integer value) {
            addCriterion("cpu_count =", value, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountNotEqualTo(Integer value) {
            addCriterion("cpu_count <>", value, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountGreaterThan(Integer value) {
            addCriterion("cpu_count >", value, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_count >=", value, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountLessThan(Integer value) {
            addCriterion("cpu_count <", value, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_count <=", value, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountIn(List<Integer> values) {
            addCriterion("cpu_count in", values, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountNotIn(List<Integer> values) {
            addCriterion("cpu_count not in", values, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountBetween(Integer value1, Integer value2) {
            addCriterion("cpu_count between", value1, value2, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andCpuCountNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_count not between", value1, value2, "cpuCount");
            return (Criteria) this;
        }

        public Criteria andMemSizeIsNull() {
            addCriterion("mem_size is null");
            return (Criteria) this;
        }

        public Criteria andMemSizeIsNotNull() {
            addCriterion("mem_size is not null");
            return (Criteria) this;
        }

        public Criteria andMemSizeEqualTo(Integer value) {
            addCriterion("mem_size =", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotEqualTo(Integer value) {
            addCriterion("mem_size <>", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeGreaterThan(Integer value) {
            addCriterion("mem_size >", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("mem_size >=", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeLessThan(Integer value) {
            addCriterion("mem_size <", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeLessThanOrEqualTo(Integer value) {
            addCriterion("mem_size <=", value, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeIn(List<Integer> values) {
            addCriterion("mem_size in", values, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotIn(List<Integer> values) {
            addCriterion("mem_size not in", values, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeBetween(Integer value1, Integer value2) {
            addCriterion("mem_size between", value1, value2, "memSize");
            return (Criteria) this;
        }

        public Criteria andMemSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("mem_size not between", value1, value2, "memSize");
            return (Criteria) this;
        }

        public Criteria andEipIdIsNull() {
            addCriterion("eip_id is null");
            return (Criteria) this;
        }

        public Criteria andEipIdIsNotNull() {
            addCriterion("eip_id is not null");
            return (Criteria) this;
        }

        public Criteria andEipIdEqualTo(String value) {
            addCriterion("eip_id =", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdNotEqualTo(String value) {
            addCriterion("eip_id <>", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdGreaterThan(String value) {
            addCriterion("eip_id >", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdGreaterThanOrEqualTo(String value) {
            addCriterion("eip_id >=", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdLessThan(String value) {
            addCriterion("eip_id <", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdLessThanOrEqualTo(String value) {
            addCriterion("eip_id <=", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdLike(String value) {
            addCriterion("eip_id like", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdNotLike(String value) {
            addCriterion("eip_id not like", value, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdIn(List<String> values) {
            addCriterion("eip_id in", values, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdNotIn(List<String> values) {
            addCriterion("eip_id not in", values, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdBetween(String value1, String value2) {
            addCriterion("eip_id between", value1, value2, "eipId");
            return (Criteria) this;
        }

        public Criteria andEipIdNotBetween(String value1, String value2) {
            addCriterion("eip_id not between", value1, value2, "eipId");
            return (Criteria) this;
        }

        public Criteria andRootDiskIsNull() {
            addCriterion("root_disk is null");
            return (Criteria) this;
        }

        public Criteria andRootDiskIsNotNull() {
            addCriterion("root_disk is not null");
            return (Criteria) this;
        }

        public Criteria andRootDiskEqualTo(Integer value) {
            addCriterion("root_disk =", value, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskNotEqualTo(Integer value) {
            addCriterion("root_disk <>", value, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskGreaterThan(Integer value) {
            addCriterion("root_disk >", value, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskGreaterThanOrEqualTo(Integer value) {
            addCriterion("root_disk >=", value, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskLessThan(Integer value) {
            addCriterion("root_disk <", value, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskLessThanOrEqualTo(Integer value) {
            addCriterion("root_disk <=", value, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskIn(List<Integer> values) {
            addCriterion("root_disk in", values, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskNotIn(List<Integer> values) {
            addCriterion("root_disk not in", values, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskBetween(Integer value1, Integer value2) {
            addCriterion("root_disk between", value1, value2, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRootDiskNotBetween(Integer value1, Integer value2) {
            addCriterion("root_disk not between", value1, value2, "rootDisk");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeIsNull() {
            addCriterion("recycle_mem_size is null");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeIsNotNull() {
            addCriterion("recycle_mem_size is not null");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeEqualTo(Integer value) {
            addCriterion("recycle_mem_size =", value, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeNotEqualTo(Integer value) {
            addCriterion("recycle_mem_size <>", value, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeGreaterThan(Integer value) {
            addCriterion("recycle_mem_size >", value, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeGreaterThanOrEqualTo(Integer value) {
            addCriterion("recycle_mem_size >=", value, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeLessThan(Integer value) {
            addCriterion("recycle_mem_size <", value, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeLessThanOrEqualTo(Integer value) {
            addCriterion("recycle_mem_size <=", value, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeIn(List<Integer> values) {
            addCriterion("recycle_mem_size in", values, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeNotIn(List<Integer> values) {
            addCriterion("recycle_mem_size not in", values, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeBetween(Integer value1, Integer value2) {
            addCriterion("recycle_mem_size between", value1, value2, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleMemSizeNotBetween(Integer value1, Integer value2) {
            addCriterion("recycle_mem_size not between", value1, value2, "recycleMemSize");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountIsNull() {
            addCriterion("recycle_cpu_count is null");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountIsNotNull() {
            addCriterion("recycle_cpu_count is not null");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountEqualTo(Integer value) {
            addCriterion("recycle_cpu_count =", value, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountNotEqualTo(Integer value) {
            addCriterion("recycle_cpu_count <>", value, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountGreaterThan(Integer value) {
            addCriterion("recycle_cpu_count >", value, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("recycle_cpu_count >=", value, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountLessThan(Integer value) {
            addCriterion("recycle_cpu_count <", value, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountLessThanOrEqualTo(Integer value) {
            addCriterion("recycle_cpu_count <=", value, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountIn(List<Integer> values) {
            addCriterion("recycle_cpu_count in", values, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountNotIn(List<Integer> values) {
            addCriterion("recycle_cpu_count not in", values, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountBetween(Integer value1, Integer value2) {
            addCriterion("recycle_cpu_count between", value1, value2, "recycleCpuCount");
            return (Criteria) this;
        }

        public Criteria andRecycleCpuCountNotBetween(Integer value1, Integer value2) {
            addCriterion("recycle_cpu_count not between", value1, value2, "recycleCpuCount");
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

        public Criteria andEeIdIsNull() {
            addCriterion("ee_id is null");
            return (Criteria) this;
        }

        public Criteria andEeIdIsNotNull() {
            addCriterion("ee_id is not null");
            return (Criteria) this;
        }

        public Criteria andEeIdEqualTo(String value) {
            addCriterion("ee_id =", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdNotEqualTo(String value) {
            addCriterion("ee_id <>", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdGreaterThan(String value) {
            addCriterion("ee_id >", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdGreaterThanOrEqualTo(String value) {
            addCriterion("ee_id >=", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdLessThan(String value) {
            addCriterion("ee_id <", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdLessThanOrEqualTo(String value) {
            addCriterion("ee_id <=", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdLike(String value) {
            addCriterion("ee_id like", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdNotLike(String value) {
            addCriterion("ee_id not like", value, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdIn(List<String> values) {
            addCriterion("ee_id in", values, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdNotIn(List<String> values) {
            addCriterion("ee_id not in", values, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdBetween(String value1, String value2) {
            addCriterion("ee_id between", value1, value2, "eeId");
            return (Criteria) this;
        }

        public Criteria andEeIdNotBetween(String value1, String value2) {
            addCriterion("ee_id not between", value1, value2, "eeId");
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