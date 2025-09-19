package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpEipMapExample {
    protected String orderByClause;

    protected boolean distinct;

    protected Integer startRow;

    protected Integer pageSize;

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public TblCmpEipMapExample() {
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

        public Criteria andEipMapIdIsNull() {
            addCriterion("eip_map_id is null");
            return (Criteria) this;
        }

        public Criteria andEipMapIdIsNotNull() {
            addCriterion("eip_map_id is not null");
            return (Criteria) this;
        }

        public Criteria andEipMapIdEqualTo(String value) {
            addCriterion("eip_map_id =", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdNotEqualTo(String value) {
            addCriterion("eip_map_id <>", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdGreaterThan(String value) {
            addCriterion("eip_map_id >", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdGreaterThanOrEqualTo(String value) {
            addCriterion("eip_map_id >=", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdLessThan(String value) {
            addCriterion("eip_map_id <", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdLessThanOrEqualTo(String value) {
            addCriterion("eip_map_id <=", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdLike(String value) {
            addCriterion("eip_map_id like", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdNotLike(String value) {
            addCriterion("eip_map_id not like", value, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdIn(List<String> values) {
            addCriterion("eip_map_id in", values, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdNotIn(List<String> values) {
            addCriterion("eip_map_id not in", values, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdBetween(String value1, String value2) {
            addCriterion("eip_map_id between", value1, value2, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andEipMapIdNotBetween(String value1, String value2) {
            addCriterion("eip_map_id not between", value1, value2, "eipMapId");
            return (Criteria) this;
        }

        public Criteria andMapNameIsNull() {
            addCriterion("map_name is null");
            return (Criteria) this;
        }

        public Criteria andMapNameIsNotNull() {
            addCriterion("map_name is not null");
            return (Criteria) this;
        }

        public Criteria andMapNameEqualTo(String value) {
            addCriterion("map_name =", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameNotEqualTo(String value) {
            addCriterion("map_name <>", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameGreaterThan(String value) {
            addCriterion("map_name >", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameGreaterThanOrEqualTo(String value) {
            addCriterion("map_name >=", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameLessThan(String value) {
            addCriterion("map_name <", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameLessThanOrEqualTo(String value) {
            addCriterion("map_name <=", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameLike(String value) {
            addCriterion("map_name like", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameNotLike(String value) {
            addCriterion("map_name not like", value, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameIn(List<String> values) {
            addCriterion("map_name in", values, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameNotIn(List<String> values) {
            addCriterion("map_name not in", values, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameBetween(String value1, String value2) {
            addCriterion("map_name between", value1, value2, "mapName");
            return (Criteria) this;
        }

        public Criteria andMapNameNotBetween(String value1, String value2) {
            addCriterion("map_name not between", value1, value2, "mapName");
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

        public Criteria andIsStaticIpIsNull() {
            addCriterion("is_static_ip is null");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpIsNotNull() {
            addCriterion("is_static_ip is not null");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpEqualTo(Boolean value) {
            addCriterion("is_static_ip =", value, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpNotEqualTo(Boolean value) {
            addCriterion("is_static_ip <>", value, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpGreaterThan(Boolean value) {
            addCriterion("is_static_ip >", value, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_static_ip >=", value, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpLessThan(Boolean value) {
            addCriterion("is_static_ip <", value, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpLessThanOrEqualTo(Boolean value) {
            addCriterion("is_static_ip <=", value, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpIn(List<Boolean> values) {
            addCriterion("is_static_ip in", values, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpNotIn(List<Boolean> values) {
            addCriterion("is_static_ip not in", values, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpBetween(Boolean value1, Boolean value2) {
            addCriterion("is_static_ip between", value1, value2, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andIsStaticIpNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_static_ip not between", value1, value2, "isStaticIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpIsNull() {
            addCriterion("inside_ip is null");
            return (Criteria) this;
        }

        public Criteria andInsideIpIsNotNull() {
            addCriterion("inside_ip is not null");
            return (Criteria) this;
        }

        public Criteria andInsideIpEqualTo(String value) {
            addCriterion("inside_ip =", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpNotEqualTo(String value) {
            addCriterion("inside_ip <>", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpGreaterThan(String value) {
            addCriterion("inside_ip >", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpGreaterThanOrEqualTo(String value) {
            addCriterion("inside_ip >=", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpLessThan(String value) {
            addCriterion("inside_ip <", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpLessThanOrEqualTo(String value) {
            addCriterion("inside_ip <=", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpLike(String value) {
            addCriterion("inside_ip like", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpNotLike(String value) {
            addCriterion("inside_ip not like", value, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpIn(List<String> values) {
            addCriterion("inside_ip in", values, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpNotIn(List<String> values) {
            addCriterion("inside_ip not in", values, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpBetween(String value1, String value2) {
            addCriterion("inside_ip between", value1, value2, "insideIp");
            return (Criteria) this;
        }

        public Criteria andInsideIpNotBetween(String value1, String value2) {
            addCriterion("inside_ip not between", value1, value2, "insideIp");
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

        public Criteria andStatusEqualTo(Short value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Short value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Short value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Short value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Short value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Short value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Short> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Short> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Short value1, Short value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Short value1, Short value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andBandwidthIsNull() {
            addCriterion("bandwidth is null");
            return (Criteria) this;
        }

        public Criteria andBandwidthIsNotNull() {
            addCriterion("bandwidth is not null");
            return (Criteria) this;
        }

        public Criteria andBandwidthEqualTo(String value) {
            addCriterion("bandwidth =", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotEqualTo(String value) {
            addCriterion("bandwidth <>", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthGreaterThan(String value) {
            addCriterion("bandwidth >", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthGreaterThanOrEqualTo(String value) {
            addCriterion("bandwidth >=", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLessThan(String value) {
            addCriterion("bandwidth <", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLessThanOrEqualTo(String value) {
            addCriterion("bandwidth <=", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthLike(String value) {
            addCriterion("bandwidth like", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotLike(String value) {
            addCriterion("bandwidth not like", value, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthIn(List<String> values) {
            addCriterion("bandwidth in", values, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotIn(List<String> values) {
            addCriterion("bandwidth not in", values, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthBetween(String value1, String value2) {
            addCriterion("bandwidth between", value1, value2, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andBandwidthNotBetween(String value1, String value2) {
            addCriterion("bandwidth not between", value1, value2, "bandwidth");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneIsNull() {
            addCriterion("is_one_to_one is null");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneIsNotNull() {
            addCriterion("is_one_to_one is not null");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneEqualTo(Boolean value) {
            addCriterion("is_one_to_one =", value, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneNotEqualTo(Boolean value) {
            addCriterion("is_one_to_one <>", value, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneGreaterThan(Boolean value) {
            addCriterion("is_one_to_one >", value, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_one_to_one >=", value, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneLessThan(Boolean value) {
            addCriterion("is_one_to_one <", value, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneLessThanOrEqualTo(Boolean value) {
            addCriterion("is_one_to_one <=", value, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneIn(List<Boolean> values) {
            addCriterion("is_one_to_one in", values, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneNotIn(List<Boolean> values) {
            addCriterion("is_one_to_one not in", values, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneBetween(Boolean value1, Boolean value2) {
            addCriterion("is_one_to_one between", value1, value2, "isOneToOne");
            return (Criteria) this;
        }

        public Criteria andIsOneToOneNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_one_to_one not between", value1, value2, "isOneToOne");
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