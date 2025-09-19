package com.lnjoying.justice.scheduler.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblEdgeComputeInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblEdgeComputeInfoExample() {
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

        public Criteria andNetworkIsNull() {
            addCriterion("network is null");
            return (Criteria) this;
        }

        public Criteria andNetworkIsNotNull() {
            addCriterion("network is not null");
            return (Criteria) this;
        }

        public Criteria andNetworkEqualTo(String value) {
            addCriterion("network =", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkNotEqualTo(String value) {
            addCriterion("network <>", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkGreaterThan(String value) {
            addCriterion("network >", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkGreaterThanOrEqualTo(String value) {
            addCriterion("network >=", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkLessThan(String value) {
            addCriterion("network <", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkLessThanOrEqualTo(String value) {
            addCriterion("network <=", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkLike(String value) {
            addCriterion("network like", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkNotLike(String value) {
            addCriterion("network not like", value, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkIn(List<String> values) {
            addCriterion("network in", values, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkNotIn(List<String> values) {
            addCriterion("network not in", values, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkBetween(String value1, String value2) {
            addCriterion("network between", value1, value2, "network");
            return (Criteria) this;
        }

        public Criteria andNetworkNotBetween(String value1, String value2) {
            addCriterion("network not between", value1, value2, "network");
            return (Criteria) this;
        }

        public Criteria andActiveStatusIsNull() {
            addCriterion("active_status is null");
            return (Criteria) this;
        }

        public Criteria andActiveStatusIsNotNull() {
            addCriterion("active_status is not null");
            return (Criteria) this;
        }

        public Criteria andActiveStatusEqualTo(Integer value) {
            addCriterion("active_status =", value, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusNotEqualTo(Integer value) {
            addCriterion("active_status <>", value, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusGreaterThan(Integer value) {
            addCriterion("active_status >", value, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("active_status >=", value, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusLessThan(Integer value) {
            addCriterion("active_status <", value, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusLessThanOrEqualTo(Integer value) {
            addCriterion("active_status <=", value, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusIn(List<Integer> values) {
            addCriterion("active_status in", values, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusNotIn(List<Integer> values) {
            addCriterion("active_status not in", values, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusBetween(Integer value1, Integer value2) {
            addCriterion("active_status between", value1, value2, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andActiveStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("active_status not between", value1, value2, "activeStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusIsNull() {
            addCriterion("online_status is null");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusIsNotNull() {
            addCriterion("online_status is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusEqualTo(Integer value) {
            addCriterion("online_status =", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusNotEqualTo(Integer value) {
            addCriterion("online_status <>", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusGreaterThan(Integer value) {
            addCriterion("online_status >", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("online_status >=", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusLessThan(Integer value) {
            addCriterion("online_status <", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusLessThanOrEqualTo(Integer value) {
            addCriterion("online_status <=", value, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusIn(List<Integer> values) {
            addCriterion("online_status in", values, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusNotIn(List<Integer> values) {
            addCriterion("online_status not in", values, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusBetween(Integer value1, Integer value2) {
            addCriterion("online_status between", value1, value2, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andOnlineStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("online_status not between", value1, value2, "onlineStatus");
            return (Criteria) this;
        }

        public Criteria andCpuLimitIsNull() {
            addCriterion("cpu_limit is null");
            return (Criteria) this;
        }

        public Criteria andCpuLimitIsNotNull() {
            addCriterion("cpu_limit is not null");
            return (Criteria) this;
        }

        public Criteria andCpuLimitEqualTo(Integer value) {
            addCriterion("cpu_limit =", value, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitNotEqualTo(Integer value) {
            addCriterion("cpu_limit <>", value, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitGreaterThan(Integer value) {
            addCriterion("cpu_limit >", value, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_limit >=", value, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitLessThan(Integer value) {
            addCriterion("cpu_limit <", value, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_limit <=", value, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitIn(List<Integer> values) {
            addCriterion("cpu_limit in", values, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitNotIn(List<Integer> values) {
            addCriterion("cpu_limit not in", values, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitBetween(Integer value1, Integer value2) {
            addCriterion("cpu_limit between", value1, value2, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andCpuLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_limit not between", value1, value2, "cpuLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitIsNull() {
            addCriterion("mem_limit is null");
            return (Criteria) this;
        }

        public Criteria andMemLimitIsNotNull() {
            addCriterion("mem_limit is not null");
            return (Criteria) this;
        }

        public Criteria andMemLimitEqualTo(Long value) {
            addCriterion("mem_limit =", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitNotEqualTo(Long value) {
            addCriterion("mem_limit <>", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitGreaterThan(Long value) {
            addCriterion("mem_limit >", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("mem_limit >=", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitLessThan(Long value) {
            addCriterion("mem_limit <", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitLessThanOrEqualTo(Long value) {
            addCriterion("mem_limit <=", value, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitIn(List<Long> values) {
            addCriterion("mem_limit in", values, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitNotIn(List<Long> values) {
            addCriterion("mem_limit not in", values, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitBetween(Long value1, Long value2) {
            addCriterion("mem_limit between", value1, value2, "memLimit");
            return (Criteria) this;
        }

        public Criteria andMemLimitNotBetween(Long value1, Long value2) {
            addCriterion("mem_limit not between", value1, value2, "memLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitIsNull() {
            addCriterion("disk_limit is null");
            return (Criteria) this;
        }

        public Criteria andDiskLimitIsNotNull() {
            addCriterion("disk_limit is not null");
            return (Criteria) this;
        }

        public Criteria andDiskLimitEqualTo(Long value) {
            addCriterion("disk_limit =", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitNotEqualTo(Long value) {
            addCriterion("disk_limit <>", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitGreaterThan(Long value) {
            addCriterion("disk_limit >", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitGreaterThanOrEqualTo(Long value) {
            addCriterion("disk_limit >=", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitLessThan(Long value) {
            addCriterion("disk_limit <", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitLessThanOrEqualTo(Long value) {
            addCriterion("disk_limit <=", value, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitIn(List<Long> values) {
            addCriterion("disk_limit in", values, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitNotIn(List<Long> values) {
            addCriterion("disk_limit not in", values, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitBetween(Long value1, Long value2) {
            addCriterion("disk_limit between", value1, value2, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andDiskLimitNotBetween(Long value1, Long value2) {
            addCriterion("disk_limit not between", value1, value2, "diskLimit");
            return (Criteria) this;
        }

        public Criteria andVenderIsNull() {
            addCriterion("vender is null");
            return (Criteria) this;
        }

        public Criteria andVenderIsNotNull() {
            addCriterion("vender is not null");
            return (Criteria) this;
        }

        public Criteria andVenderEqualTo(String value) {
            addCriterion("vender =", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderNotEqualTo(String value) {
            addCriterion("vender <>", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderGreaterThan(String value) {
            addCriterion("vender >", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderGreaterThanOrEqualTo(String value) {
            addCriterion("vender >=", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderLessThan(String value) {
            addCriterion("vender <", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderLessThanOrEqualTo(String value) {
            addCriterion("vender <=", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderLike(String value) {
            addCriterion("vender like", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderNotLike(String value) {
            addCriterion("vender not like", value, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderIn(List<String> values) {
            addCriterion("vender in", values, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderNotIn(List<String> values) {
            addCriterion("vender not in", values, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderBetween(String value1, String value2) {
            addCriterion("vender between", value1, value2, "vender");
            return (Criteria) this;
        }

        public Criteria andVenderNotBetween(String value1, String value2) {
            addCriterion("vender not between", value1, value2, "vender");
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

        public Criteria andProductIsNull() {
            addCriterion("product is null");
            return (Criteria) this;
        }

        public Criteria andProductIsNotNull() {
            addCriterion("product is not null");
            return (Criteria) this;
        }

        public Criteria andProductEqualTo(String value) {
            addCriterion("product =", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductNotEqualTo(String value) {
            addCriterion("product <>", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductGreaterThan(String value) {
            addCriterion("product >", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductGreaterThanOrEqualTo(String value) {
            addCriterion("product >=", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductLessThan(String value) {
            addCriterion("product <", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductLessThanOrEqualTo(String value) {
            addCriterion("product <=", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductLike(String value) {
            addCriterion("product like", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductNotLike(String value) {
            addCriterion("product not like", value, "product");
            return (Criteria) this;
        }

        public Criteria andProductIn(List<String> values) {
            addCriterion("product in", values, "product");
            return (Criteria) this;
        }

        public Criteria andProductNotIn(List<String> values) {
            addCriterion("product not in", values, "product");
            return (Criteria) this;
        }

        public Criteria andProductBetween(String value1, String value2) {
            addCriterion("product between", value1, value2, "product");
            return (Criteria) this;
        }

        public Criteria andProductNotBetween(String value1, String value2) {
            addCriterion("product not between", value1, value2, "product");
            return (Criteria) this;
        }

        public Criteria andSnIsNull() {
            addCriterion("sn is null");
            return (Criteria) this;
        }

        public Criteria andSnIsNotNull() {
            addCriterion("sn is not null");
            return (Criteria) this;
        }

        public Criteria andSnEqualTo(String value) {
            addCriterion("sn =", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotEqualTo(String value) {
            addCriterion("sn <>", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnGreaterThan(String value) {
            addCriterion("sn >", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnGreaterThanOrEqualTo(String value) {
            addCriterion("sn >=", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnLessThan(String value) {
            addCriterion("sn <", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnLessThanOrEqualTo(String value) {
            addCriterion("sn <=", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnLike(String value) {
            addCriterion("sn like", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotLike(String value) {
            addCriterion("sn not like", value, "sn");
            return (Criteria) this;
        }

        public Criteria andSnIn(List<String> values) {
            addCriterion("sn in", values, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotIn(List<String> values) {
            addCriterion("sn not in", values, "sn");
            return (Criteria) this;
        }

        public Criteria andSnBetween(String value1, String value2) {
            addCriterion("sn between", value1, value2, "sn");
            return (Criteria) this;
        }

        public Criteria andSnNotBetween(String value1, String value2) {
            addCriterion("sn not between", value1, value2, "sn");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumIsNull() {
            addCriterion("cpu_logic_num is null");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumIsNotNull() {
            addCriterion("cpu_logic_num is not null");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumEqualTo(Integer value) {
            addCriterion("cpu_logic_num =", value, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumNotEqualTo(Integer value) {
            addCriterion("cpu_logic_num <>", value, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumGreaterThan(Integer value) {
            addCriterion("cpu_logic_num >", value, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_logic_num >=", value, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumLessThan(Integer value) {
            addCriterion("cpu_logic_num <", value, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_logic_num <=", value, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumIn(List<Integer> values) {
            addCriterion("cpu_logic_num in", values, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumNotIn(List<Integer> values) {
            addCriterion("cpu_logic_num not in", values, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumBetween(Integer value1, Integer value2) {
            addCriterion("cpu_logic_num between", value1, value2, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuLogicNumNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_logic_num not between", value1, value2, "cpuLogicNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumIsNull() {
            addCriterion("cpu_physical_num is null");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumIsNotNull() {
            addCriterion("cpu_physical_num is not null");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumEqualTo(Integer value) {
            addCriterion("cpu_physical_num =", value, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumNotEqualTo(Integer value) {
            addCriterion("cpu_physical_num <>", value, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumGreaterThan(Integer value) {
            addCriterion("cpu_physical_num >", value, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_physical_num >=", value, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumLessThan(Integer value) {
            addCriterion("cpu_physical_num <", value, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_physical_num <=", value, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumIn(List<Integer> values) {
            addCriterion("cpu_physical_num in", values, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumNotIn(List<Integer> values) {
            addCriterion("cpu_physical_num not in", values, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumBetween(Integer value1, Integer value2) {
            addCriterion("cpu_physical_num between", value1, value2, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuPhysicalNumNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_physical_num not between", value1, value2, "cpuPhysicalNum");
            return (Criteria) this;
        }

        public Criteria andCpuModelIsNull() {
            addCriterion("cpu_model is null");
            return (Criteria) this;
        }

        public Criteria andCpuModelIsNotNull() {
            addCriterion("cpu_model is not null");
            return (Criteria) this;
        }

        public Criteria andCpuModelEqualTo(String value) {
            addCriterion("cpu_model =", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotEqualTo(String value) {
            addCriterion("cpu_model <>", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelGreaterThan(String value) {
            addCriterion("cpu_model >", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_model >=", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelLessThan(String value) {
            addCriterion("cpu_model <", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelLessThanOrEqualTo(String value) {
            addCriterion("cpu_model <=", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelLike(String value) {
            addCriterion("cpu_model like", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotLike(String value) {
            addCriterion("cpu_model not like", value, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelIn(List<String> values) {
            addCriterion("cpu_model in", values, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotIn(List<String> values) {
            addCriterion("cpu_model not in", values, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelBetween(String value1, String value2) {
            addCriterion("cpu_model between", value1, value2, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuModelNotBetween(String value1, String value2) {
            addCriterion("cpu_model not between", value1, value2, "cpuModel");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyIsNull() {
            addCriterion("cpu_frequency is null");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyIsNotNull() {
            addCriterion("cpu_frequency is not null");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyEqualTo(Double value) {
            addCriterion("cpu_frequency =", value, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyNotEqualTo(Double value) {
            addCriterion("cpu_frequency <>", value, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyGreaterThan(Double value) {
            addCriterion("cpu_frequency >", value, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyGreaterThanOrEqualTo(Double value) {
            addCriterion("cpu_frequency >=", value, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyLessThan(Double value) {
            addCriterion("cpu_frequency <", value, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyLessThanOrEqualTo(Double value) {
            addCriterion("cpu_frequency <=", value, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyIn(List<Double> values) {
            addCriterion("cpu_frequency in", values, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyNotIn(List<Double> values) {
            addCriterion("cpu_frequency not in", values, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyBetween(Double value1, Double value2) {
            addCriterion("cpu_frequency between", value1, value2, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andCpuFrequencyNotBetween(Double value1, Double value2) {
            addCriterion("cpu_frequency not between", value1, value2, "cpuFrequency");
            return (Criteria) this;
        }

        public Criteria andMemTotalIsNull() {
            addCriterion("mem_total is null");
            return (Criteria) this;
        }

        public Criteria andMemTotalIsNotNull() {
            addCriterion("mem_total is not null");
            return (Criteria) this;
        }

        public Criteria andMemTotalEqualTo(Long value) {
            addCriterion("mem_total =", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalNotEqualTo(Long value) {
            addCriterion("mem_total <>", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalGreaterThan(Long value) {
            addCriterion("mem_total >", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalGreaterThanOrEqualTo(Long value) {
            addCriterion("mem_total >=", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalLessThan(Long value) {
            addCriterion("mem_total <", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalLessThanOrEqualTo(Long value) {
            addCriterion("mem_total <=", value, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalIn(List<Long> values) {
            addCriterion("mem_total in", values, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalNotIn(List<Long> values) {
            addCriterion("mem_total not in", values, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalBetween(Long value1, Long value2) {
            addCriterion("mem_total between", value1, value2, "memTotal");
            return (Criteria) this;
        }

        public Criteria andMemTotalNotBetween(Long value1, Long value2) {
            addCriterion("mem_total not between", value1, value2, "memTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalIsNull() {
            addCriterion("disk_total is null");
            return (Criteria) this;
        }

        public Criteria andDiskTotalIsNotNull() {
            addCriterion("disk_total is not null");
            return (Criteria) this;
        }

        public Criteria andDiskTotalEqualTo(Long value) {
            addCriterion("disk_total =", value, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalNotEqualTo(Long value) {
            addCriterion("disk_total <>", value, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalGreaterThan(Long value) {
            addCriterion("disk_total >", value, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalGreaterThanOrEqualTo(Long value) {
            addCriterion("disk_total >=", value, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalLessThan(Long value) {
            addCriterion("disk_total <", value, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalLessThanOrEqualTo(Long value) {
            addCriterion("disk_total <=", value, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalIn(List<Long> values) {
            addCriterion("disk_total in", values, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalNotIn(List<Long> values) {
            addCriterion("disk_total not in", values, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalBetween(Long value1, Long value2) {
            addCriterion("disk_total between", value1, value2, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTotalNotBetween(Long value1, Long value2) {
            addCriterion("disk_total not between", value1, value2, "diskTotal");
            return (Criteria) this;
        }

        public Criteria andDiskTypeIsNull() {
            addCriterion("disk_type is null");
            return (Criteria) this;
        }

        public Criteria andDiskTypeIsNotNull() {
            addCriterion("disk_type is not null");
            return (Criteria) this;
        }

        public Criteria andDiskTypeEqualTo(String value) {
            addCriterion("disk_type =", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotEqualTo(String value) {
            addCriterion("disk_type <>", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeGreaterThan(String value) {
            addCriterion("disk_type >", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeGreaterThanOrEqualTo(String value) {
            addCriterion("disk_type >=", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeLessThan(String value) {
            addCriterion("disk_type <", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeLessThanOrEqualTo(String value) {
            addCriterion("disk_type <=", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeLike(String value) {
            addCriterion("disk_type like", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotLike(String value) {
            addCriterion("disk_type not like", value, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeIn(List<String> values) {
            addCriterion("disk_type in", values, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotIn(List<String> values) {
            addCriterion("disk_type not in", values, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeBetween(String value1, String value2) {
            addCriterion("disk_type between", value1, value2, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskTypeNotBetween(String value1, String value2) {
            addCriterion("disk_type not between", value1, value2, "diskType");
            return (Criteria) this;
        }

        public Criteria andDiskDetailIsNull() {
            addCriterion("disk_detail is null");
            return (Criteria) this;
        }

        public Criteria andDiskDetailIsNotNull() {
            addCriterion("disk_detail is not null");
            return (Criteria) this;
        }

        public Criteria andDiskDetailEqualTo(String value) {
            addCriterion("disk_detail =", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailNotEqualTo(String value) {
            addCriterion("disk_detail <>", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailGreaterThan(String value) {
            addCriterion("disk_detail >", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailGreaterThanOrEqualTo(String value) {
            addCriterion("disk_detail >=", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailLessThan(String value) {
            addCriterion("disk_detail <", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailLessThanOrEqualTo(String value) {
            addCriterion("disk_detail <=", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailLike(String value) {
            addCriterion("disk_detail like", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailNotLike(String value) {
            addCriterion("disk_detail not like", value, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailIn(List<String> values) {
            addCriterion("disk_detail in", values, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailNotIn(List<String> values) {
            addCriterion("disk_detail not in", values, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailBetween(String value1, String value2) {
            addCriterion("disk_detail between", value1, value2, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andDiskDetailNotBetween(String value1, String value2) {
            addCriterion("disk_detail not between", value1, value2, "diskDetail");
            return (Criteria) this;
        }

        public Criteria andOsIsNull() {
            addCriterion("os is null");
            return (Criteria) this;
        }

        public Criteria andOsIsNotNull() {
            addCriterion("os is not null");
            return (Criteria) this;
        }

        public Criteria andOsEqualTo(String value) {
            addCriterion("os =", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotEqualTo(String value) {
            addCriterion("os <>", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsGreaterThan(String value) {
            addCriterion("os >", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsGreaterThanOrEqualTo(String value) {
            addCriterion("os >=", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLessThan(String value) {
            addCriterion("os <", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLessThanOrEqualTo(String value) {
            addCriterion("os <=", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLike(String value) {
            addCriterion("os like", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotLike(String value) {
            addCriterion("os not like", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsIn(List<String> values) {
            addCriterion("os in", values, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotIn(List<String> values) {
            addCriterion("os not in", values, "os");
            return (Criteria) this;
        }

        public Criteria andOsBetween(String value1, String value2) {
            addCriterion("os between", value1, value2, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotBetween(String value1, String value2) {
            addCriterion("os not between", value1, value2, "os");
            return (Criteria) this;
        }

        public Criteria andCoreVersionIsNull() {
            addCriterion("core_version is null");
            return (Criteria) this;
        }

        public Criteria andCoreVersionIsNotNull() {
            addCriterion("core_version is not null");
            return (Criteria) this;
        }

        public Criteria andCoreVersionEqualTo(String value) {
            addCriterion("core_version =", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionNotEqualTo(String value) {
            addCriterion("core_version <>", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionGreaterThan(String value) {
            addCriterion("core_version >", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionGreaterThanOrEqualTo(String value) {
            addCriterion("core_version >=", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionLessThan(String value) {
            addCriterion("core_version <", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionLessThanOrEqualTo(String value) {
            addCriterion("core_version <=", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionLike(String value) {
            addCriterion("core_version like", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionNotLike(String value) {
            addCriterion("core_version not like", value, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionIn(List<String> values) {
            addCriterion("core_version in", values, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionNotIn(List<String> values) {
            addCriterion("core_version not in", values, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionBetween(String value1, String value2) {
            addCriterion("core_version between", value1, value2, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andCoreVersionNotBetween(String value1, String value2) {
            addCriterion("core_version not between", value1, value2, "coreVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionIsNull() {
            addCriterion("software_version is null");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionIsNotNull() {
            addCriterion("software_version is not null");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionEqualTo(String value) {
            addCriterion("software_version =", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionNotEqualTo(String value) {
            addCriterion("software_version <>", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionGreaterThan(String value) {
            addCriterion("software_version >", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionGreaterThanOrEqualTo(String value) {
            addCriterion("software_version >=", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionLessThan(String value) {
            addCriterion("software_version <", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionLessThanOrEqualTo(String value) {
            addCriterion("software_version <=", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionLike(String value) {
            addCriterion("software_version like", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionNotLike(String value) {
            addCriterion("software_version not like", value, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionIn(List<String> values) {
            addCriterion("software_version in", values, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionNotIn(List<String> values) {
            addCriterion("software_version not in", values, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionBetween(String value1, String value2) {
            addCriterion("software_version between", value1, value2, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andSoftwareVersionNotBetween(String value1, String value2) {
            addCriterion("software_version not between", value1, value2, "softwareVersion");
            return (Criteria) this;
        }

        public Criteria andScopeIsNull() {
            addCriterion("scope is null");
            return (Criteria) this;
        }

        public Criteria andScopeIsNotNull() {
            addCriterion("scope is not null");
            return (Criteria) this;
        }

        public Criteria andScopeEqualTo(String value) {
            addCriterion("scope =", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotEqualTo(String value) {
            addCriterion("scope <>", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeGreaterThan(String value) {
            addCriterion("scope >", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeGreaterThanOrEqualTo(String value) {
            addCriterion("scope >=", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeLessThan(String value) {
            addCriterion("scope <", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeLessThanOrEqualTo(String value) {
            addCriterion("scope <=", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeLike(String value) {
            addCriterion("scope like", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotLike(String value) {
            addCriterion("scope not like", value, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeIn(List<String> values) {
            addCriterion("scope in", values, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotIn(List<String> values) {
            addCriterion("scope not in", values, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeBetween(String value1, String value2) {
            addCriterion("scope between", value1, value2, "scope");
            return (Criteria) this;
        }

        public Criteria andScopeNotBetween(String value1, String value2) {
            addCriterion("scope not between", value1, value2, "scope");
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

        public Criteria andBpIdIsNull() {
            addCriterion("bp_id is null");
            return (Criteria) this;
        }

        public Criteria andBpIdIsNotNull() {
            addCriterion("bp_id is not null");
            return (Criteria) this;
        }

        public Criteria andBpIdEqualTo(String value) {
            addCriterion("bp_id =", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotEqualTo(String value) {
            addCriterion("bp_id <>", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdGreaterThan(String value) {
            addCriterion("bp_id >", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdGreaterThanOrEqualTo(String value) {
            addCriterion("bp_id >=", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLessThan(String value) {
            addCriterion("bp_id <", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLessThanOrEqualTo(String value) {
            addCriterion("bp_id <=", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdLike(String value) {
            addCriterion("bp_id like", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotLike(String value) {
            addCriterion("bp_id not like", value, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdIn(List<String> values) {
            addCriterion("bp_id in", values, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotIn(List<String> values) {
            addCriterion("bp_id not in", values, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdBetween(String value1, String value2) {
            addCriterion("bp_id between", value1, value2, "bpId");
            return (Criteria) this;
        }

        public Criteria andBpIdNotBetween(String value1, String value2) {
            addCriterion("bp_id not between", value1, value2, "bpId");
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