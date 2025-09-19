package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsRoutersExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsRoutersExample() {
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

        public Criteria andAdminStateUpIsNull() {
            addCriterion("admin_state_up is null");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpIsNotNull() {
            addCriterion("admin_state_up is not null");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpEqualTo(Short value) {
            addCriterion("admin_state_up =", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpNotEqualTo(Short value) {
            addCriterion("admin_state_up <>", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpGreaterThan(Short value) {
            addCriterion("admin_state_up >", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpGreaterThanOrEqualTo(Short value) {
            addCriterion("admin_state_up >=", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpLessThan(Short value) {
            addCriterion("admin_state_up <", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpLessThanOrEqualTo(Short value) {
            addCriterion("admin_state_up <=", value, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpIn(List<Short> values) {
            addCriterion("admin_state_up in", values, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpNotIn(List<Short> values) {
            addCriterion("admin_state_up not in", values, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpBetween(Short value1, Short value2) {
            addCriterion("admin_state_up between", value1, value2, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andAdminStateUpNotBetween(Short value1, Short value2) {
            addCriterion("admin_state_up not between", value1, value2, "adminStateUp");
            return (Criteria) this;
        }

        public Criteria andGwPortIdIsNull() {
            addCriterion("gw_port_id is null");
            return (Criteria) this;
        }

        public Criteria andGwPortIdIsNotNull() {
            addCriterion("gw_port_id is not null");
            return (Criteria) this;
        }

        public Criteria andGwPortIdEqualTo(String value) {
            addCriterion("gw_port_id =", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdNotEqualTo(String value) {
            addCriterion("gw_port_id <>", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdGreaterThan(String value) {
            addCriterion("gw_port_id >", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdGreaterThanOrEqualTo(String value) {
            addCriterion("gw_port_id >=", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdLessThan(String value) {
            addCriterion("gw_port_id <", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdLessThanOrEqualTo(String value) {
            addCriterion("gw_port_id <=", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdLike(String value) {
            addCriterion("gw_port_id like", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdNotLike(String value) {
            addCriterion("gw_port_id not like", value, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdIn(List<String> values) {
            addCriterion("gw_port_id in", values, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdNotIn(List<String> values) {
            addCriterion("gw_port_id not in", values, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdBetween(String value1, String value2) {
            addCriterion("gw_port_id between", value1, value2, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andGwPortIdNotBetween(String value1, String value2) {
            addCriterion("gw_port_id not between", value1, value2, "gwPortId");
            return (Criteria) this;
        }

        public Criteria andEnableSnatIsNull() {
            addCriterion("enable_snat is null");
            return (Criteria) this;
        }

        public Criteria andEnableSnatIsNotNull() {
            addCriterion("enable_snat is not null");
            return (Criteria) this;
        }

        public Criteria andEnableSnatEqualTo(Short value) {
            addCriterion("enable_snat =", value, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatNotEqualTo(Short value) {
            addCriterion("enable_snat <>", value, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatGreaterThan(Short value) {
            addCriterion("enable_snat >", value, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatGreaterThanOrEqualTo(Short value) {
            addCriterion("enable_snat >=", value, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatLessThan(Short value) {
            addCriterion("enable_snat <", value, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatLessThanOrEqualTo(Short value) {
            addCriterion("enable_snat <=", value, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatIn(List<Short> values) {
            addCriterion("enable_snat in", values, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatNotIn(List<Short> values) {
            addCriterion("enable_snat not in", values, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatBetween(Short value1, Short value2) {
            addCriterion("enable_snat between", value1, value2, "enableSnat");
            return (Criteria) this;
        }

        public Criteria andEnableSnatNotBetween(Short value1, Short value2) {
            addCriterion("enable_snat not between", value1, value2, "enableSnat");
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

        public Criteria andDistributedIsNull() {
            addCriterion("distributed is null");
            return (Criteria) this;
        }

        public Criteria andDistributedIsNotNull() {
            addCriterion("distributed is not null");
            return (Criteria) this;
        }

        public Criteria andDistributedEqualTo(Short value) {
            addCriterion("distributed =", value, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedNotEqualTo(Short value) {
            addCriterion("distributed <>", value, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedGreaterThan(Short value) {
            addCriterion("distributed >", value, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedGreaterThanOrEqualTo(Short value) {
            addCriterion("distributed >=", value, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedLessThan(Short value) {
            addCriterion("distributed <", value, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedLessThanOrEqualTo(Short value) {
            addCriterion("distributed <=", value, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedIn(List<Short> values) {
            addCriterion("distributed in", values, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedNotIn(List<Short> values) {
            addCriterion("distributed not in", values, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedBetween(Short value1, Short value2) {
            addCriterion("distributed between", value1, value2, "distributed");
            return (Criteria) this;
        }

        public Criteria andDistributedNotBetween(Short value1, Short value2) {
            addCriterion("distributed not between", value1, value2, "distributed");
            return (Criteria) this;
        }

        public Criteria andServiceRouterIsNull() {
            addCriterion("service_router is null");
            return (Criteria) this;
        }

        public Criteria andServiceRouterIsNotNull() {
            addCriterion("service_router is not null");
            return (Criteria) this;
        }

        public Criteria andServiceRouterEqualTo(Short value) {
            addCriterion("service_router =", value, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterNotEqualTo(Short value) {
            addCriterion("service_router <>", value, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterGreaterThan(Short value) {
            addCriterion("service_router >", value, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterGreaterThanOrEqualTo(Short value) {
            addCriterion("service_router >=", value, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterLessThan(Short value) {
            addCriterion("service_router <", value, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterLessThanOrEqualTo(Short value) {
            addCriterion("service_router <=", value, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterIn(List<Short> values) {
            addCriterion("service_router in", values, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterNotIn(List<Short> values) {
            addCriterion("service_router not in", values, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterBetween(Short value1, Short value2) {
            addCriterion("service_router between", value1, value2, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andServiceRouterNotBetween(Short value1, Short value2) {
            addCriterion("service_router not between", value1, value2, "serviceRouter");
            return (Criteria) this;
        }

        public Criteria andHaIsNull() {
            addCriterion("ha is null");
            return (Criteria) this;
        }

        public Criteria andHaIsNotNull() {
            addCriterion("ha is not null");
            return (Criteria) this;
        }

        public Criteria andHaEqualTo(Short value) {
            addCriterion("ha =", value, "ha");
            return (Criteria) this;
        }

        public Criteria andHaNotEqualTo(Short value) {
            addCriterion("ha <>", value, "ha");
            return (Criteria) this;
        }

        public Criteria andHaGreaterThan(Short value) {
            addCriterion("ha >", value, "ha");
            return (Criteria) this;
        }

        public Criteria andHaGreaterThanOrEqualTo(Short value) {
            addCriterion("ha >=", value, "ha");
            return (Criteria) this;
        }

        public Criteria andHaLessThan(Short value) {
            addCriterion("ha <", value, "ha");
            return (Criteria) this;
        }

        public Criteria andHaLessThanOrEqualTo(Short value) {
            addCriterion("ha <=", value, "ha");
            return (Criteria) this;
        }

        public Criteria andHaIn(List<Short> values) {
            addCriterion("ha in", values, "ha");
            return (Criteria) this;
        }

        public Criteria andHaNotIn(List<Short> values) {
            addCriterion("ha not in", values, "ha");
            return (Criteria) this;
        }

        public Criteria andHaBetween(Short value1, Short value2) {
            addCriterion("ha between", value1, value2, "ha");
            return (Criteria) this;
        }

        public Criteria andHaNotBetween(Short value1, Short value2) {
            addCriterion("ha not between", value1, value2, "ha");
            return (Criteria) this;
        }

        public Criteria andHaVrIdIsNull() {
            addCriterion("ha_vr_id is null");
            return (Criteria) this;
        }

        public Criteria andHaVrIdIsNotNull() {
            addCriterion("ha_vr_id is not null");
            return (Criteria) this;
        }

        public Criteria andHaVrIdEqualTo(Integer value) {
            addCriterion("ha_vr_id =", value, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdNotEqualTo(Integer value) {
            addCriterion("ha_vr_id <>", value, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdGreaterThan(Integer value) {
            addCriterion("ha_vr_id >", value, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("ha_vr_id >=", value, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdLessThan(Integer value) {
            addCriterion("ha_vr_id <", value, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdLessThanOrEqualTo(Integer value) {
            addCriterion("ha_vr_id <=", value, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdIn(List<Integer> values) {
            addCriterion("ha_vr_id in", values, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdNotIn(List<Integer> values) {
            addCriterion("ha_vr_id not in", values, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdBetween(Integer value1, Integer value2) {
            addCriterion("ha_vr_id between", value1, value2, "haVrId");
            return (Criteria) this;
        }

        public Criteria andHaVrIdNotBetween(Integer value1, Integer value2) {
            addCriterion("ha_vr_id not between", value1, value2, "haVrId");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsIsNull() {
            addCriterion("availability_zone_hints is null");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsIsNotNull() {
            addCriterion("availability_zone_hints is not null");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsEqualTo(String value) {
            addCriterion("availability_zone_hints =", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsNotEqualTo(String value) {
            addCriterion("availability_zone_hints <>", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsGreaterThan(String value) {
            addCriterion("availability_zone_hints >", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsGreaterThanOrEqualTo(String value) {
            addCriterion("availability_zone_hints >=", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsLessThan(String value) {
            addCriterion("availability_zone_hints <", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsLessThanOrEqualTo(String value) {
            addCriterion("availability_zone_hints <=", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsLike(String value) {
            addCriterion("availability_zone_hints like", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsNotLike(String value) {
            addCriterion("availability_zone_hints not like", value, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsIn(List<String> values) {
            addCriterion("availability_zone_hints in", values, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsNotIn(List<String> values) {
            addCriterion("availability_zone_hints not in", values, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsBetween(String value1, String value2) {
            addCriterion("availability_zone_hints between", value1, value2, "availabilityZoneHints");
            return (Criteria) this;
        }

        public Criteria andAvailabilityZoneHintsNotBetween(String value1, String value2) {
            addCriterion("availability_zone_hints not between", value1, value2, "availabilityZoneHints");
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