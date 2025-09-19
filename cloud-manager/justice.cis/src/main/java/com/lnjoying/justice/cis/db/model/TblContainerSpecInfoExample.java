package com.lnjoying.justice.cis.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblContainerSpecInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected int startRow;

    protected int pageSize;

    public int getStartRow()
    {
        return startRow;
    }

    public void setStartRow(int startRow)
    {
        this.startRow = startRow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    protected List<Criteria> oredCriteria;

    public TblContainerSpecInfoExample() {
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

        public Criteria andSpecIdIsNull() {
            addCriterion("spec_id is null");
            return (Criteria) this;
        }

        public Criteria andSpecIdIsNotNull() {
            addCriterion("spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andSpecIdEqualTo(String value) {
            addCriterion("spec_id =", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotEqualTo(String value) {
            addCriterion("spec_id <>", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdGreaterThan(String value) {
            addCriterion("spec_id >", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdGreaterThanOrEqualTo(String value) {
            addCriterion("spec_id >=", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLessThan(String value) {
            addCriterion("spec_id <", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLessThanOrEqualTo(String value) {
            addCriterion("spec_id <=", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdLike(String value) {
            addCriterion("spec_id like", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotLike(String value) {
            addCriterion("spec_id not like", value, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdIn(List<String> values) {
            addCriterion("spec_id in", values, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotIn(List<String> values) {
            addCriterion("spec_id not in", values, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdBetween(String value1, String value2) {
            addCriterion("spec_id between", value1, value2, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecIdNotBetween(String value1, String value2) {
            addCriterion("spec_id not between", value1, value2, "specId");
            return (Criteria) this;
        }

        public Criteria andSpecNameIsNull() {
            addCriterion("spec_name is null");
            return (Criteria) this;
        }

        public Criteria andSpecNameIsNotNull() {
            addCriterion("spec_name is not null");
            return (Criteria) this;
        }

        public Criteria andSpecNameEqualTo(String value) {
            addCriterion("spec_name =", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotEqualTo(String value) {
            addCriterion("spec_name <>", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameGreaterThan(String value) {
            addCriterion("spec_name >", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameGreaterThanOrEqualTo(String value) {
            addCriterion("spec_name >=", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameLessThan(String value) {
            addCriterion("spec_name <", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameLessThanOrEqualTo(String value) {
            addCriterion("spec_name <=", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameLike(String value) {
            addCriterion("spec_name like", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotLike(String value) {
            addCriterion("spec_name not like", value, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameIn(List<String> values) {
            addCriterion("spec_name in", values, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotIn(List<String> values) {
            addCriterion("spec_name not in", values, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameBetween(String value1, String value2) {
            addCriterion("spec_name between", value1, value2, "specName");
            return (Criteria) this;
        }

        public Criteria andSpecNameNotBetween(String value1, String value2) {
            addCriterion("spec_name not between", value1, value2, "specName");
            return (Criteria) this;
        }

        public Criteria andImageNameIsNull() {
            addCriterion("image_name is null");
            return (Criteria) this;
        }

        public Criteria andImageNameIsNotNull() {
            addCriterion("image_name is not null");
            return (Criteria) this;
        }

        public Criteria andImageNameEqualTo(String value) {
            addCriterion("image_name =", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotEqualTo(String value) {
            addCriterion("image_name <>", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameGreaterThan(String value) {
            addCriterion("image_name >", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameGreaterThanOrEqualTo(String value) {
            addCriterion("image_name >=", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLessThan(String value) {
            addCriterion("image_name <", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLessThanOrEqualTo(String value) {
            addCriterion("image_name <=", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameLike(String value) {
            addCriterion("image_name like", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotLike(String value) {
            addCriterion("image_name not like", value, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameIn(List<String> values) {
            addCriterion("image_name in", values, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotIn(List<String> values) {
            addCriterion("image_name not in", values, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameBetween(String value1, String value2) {
            addCriterion("image_name between", value1, value2, "imageName");
            return (Criteria) this;
        }

        public Criteria andImageNameNotBetween(String value1, String value2) {
            addCriterion("image_name not between", value1, value2, "imageName");
            return (Criteria) this;
        }

        public Criteria andRegistryIdIsNull() {
            addCriterion("registry_id is null");
            return (Criteria) this;
        }

        public Criteria andRegistryIdIsNotNull() {
            addCriterion("registry_id is not null");
            return (Criteria) this;
        }

        public Criteria andRegistryIdEqualTo(String value) {
            addCriterion("registry_id =", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotEqualTo(String value) {
            addCriterion("registry_id <>", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdGreaterThan(String value) {
            addCriterion("registry_id >", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdGreaterThanOrEqualTo(String value) {
            addCriterion("registry_id >=", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdLessThan(String value) {
            addCriterion("registry_id <", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdLessThanOrEqualTo(String value) {
            addCriterion("registry_id <=", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdLike(String value) {
            addCriterion("registry_id like", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotLike(String value) {
            addCriterion("registry_id not like", value, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdIn(List<String> values) {
            addCriterion("registry_id in", values, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotIn(List<String> values) {
            addCriterion("registry_id not in", values, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdBetween(String value1, String value2) {
            addCriterion("registry_id between", value1, value2, "registryId");
            return (Criteria) this;
        }

        public Criteria andRegistryIdNotBetween(String value1, String value2) {
            addCriterion("registry_id not between", value1, value2, "registryId");
            return (Criteria) this;
        }

        public Criteria andCmdIsNull() {
            addCriterion("cmd is null");
            return (Criteria) this;
        }

        public Criteria andCmdIsNotNull() {
            addCriterion("cmd is not null");
            return (Criteria) this;
        }

        public Criteria andCmdEqualTo(String value) {
            addCriterion("cmd =", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotEqualTo(String value) {
            addCriterion("cmd <>", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdGreaterThan(String value) {
            addCriterion("cmd >", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdGreaterThanOrEqualTo(String value) {
            addCriterion("cmd >=", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdLessThan(String value) {
            addCriterion("cmd <", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdLessThanOrEqualTo(String value) {
            addCriterion("cmd <=", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdLike(String value) {
            addCriterion("cmd like", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotLike(String value) {
            addCriterion("cmd not like", value, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdIn(List<String> values) {
            addCriterion("cmd in", values, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotIn(List<String> values) {
            addCriterion("cmd not in", values, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdBetween(String value1, String value2) {
            addCriterion("cmd between", value1, value2, "cmd");
            return (Criteria) this;
        }

        public Criteria andCmdNotBetween(String value1, String value2) {
            addCriterion("cmd not between", value1, value2, "cmd");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyIsNull() {
            addCriterion("scheduling_strategy is null");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyIsNotNull() {
            addCriterion("scheduling_strategy is not null");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyEqualTo(String value) {
            addCriterion("scheduling_strategy =", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotEqualTo(String value) {
            addCriterion("scheduling_strategy <>", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyGreaterThan(String value) {
            addCriterion("scheduling_strategy >", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyGreaterThanOrEqualTo(String value) {
            addCriterion("scheduling_strategy >=", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyLessThan(String value) {
            addCriterion("scheduling_strategy <", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyLessThanOrEqualTo(String value) {
            addCriterion("scheduling_strategy <=", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyLike(String value) {
            addCriterion("scheduling_strategy like", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotLike(String value) {
            addCriterion("scheduling_strategy not like", value, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyIn(List<String> values) {
            addCriterion("scheduling_strategy in", values, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotIn(List<String> values) {
            addCriterion("scheduling_strategy not in", values, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyBetween(String value1, String value2) {
            addCriterion("scheduling_strategy between", value1, value2, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andSchedulingStrategyNotBetween(String value1, String value2) {
            addCriterion("scheduling_strategy not between", value1, value2, "schedulingStrategy");
            return (Criteria) this;
        }

        public Criteria andCpuNumIsNull() {
            addCriterion("cpu_num is null");
            return (Criteria) this;
        }

        public Criteria andCpuNumIsNotNull() {
            addCriterion("cpu_num is not null");
            return (Criteria) this;
        }

        public Criteria andCpuNumEqualTo(Integer value) {
            addCriterion("cpu_num =", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotEqualTo(Integer value) {
            addCriterion("cpu_num <>", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumGreaterThan(Integer value) {
            addCriterion("cpu_num >", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("cpu_num >=", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumLessThan(Integer value) {
            addCriterion("cpu_num <", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumLessThanOrEqualTo(Integer value) {
            addCriterion("cpu_num <=", value, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumIn(List<Integer> values) {
            addCriterion("cpu_num in", values, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotIn(List<Integer> values) {
            addCriterion("cpu_num not in", values, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumBetween(Integer value1, Integer value2) {
            addCriterion("cpu_num between", value1, value2, "cpuNum");
            return (Criteria) this;
        }

        public Criteria andCpuNumNotBetween(Integer value1, Integer value2) {
            addCriterion("cpu_num not between", value1, value2, "cpuNum");
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

        public Criteria andTransmitBandLimitIsNull() {
            addCriterion("transmit_band_limit is null");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitIsNotNull() {
            addCriterion("transmit_band_limit is not null");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitEqualTo(Integer value) {
            addCriterion("transmit_band_limit =", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitNotEqualTo(Integer value) {
            addCriterion("transmit_band_limit <>", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitGreaterThan(Integer value) {
            addCriterion("transmit_band_limit >", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("transmit_band_limit >=", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitLessThan(Integer value) {
            addCriterion("transmit_band_limit <", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitLessThanOrEqualTo(Integer value) {
            addCriterion("transmit_band_limit <=", value, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitIn(List<Integer> values) {
            addCriterion("transmit_band_limit in", values, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitNotIn(List<Integer> values) {
            addCriterion("transmit_band_limit not in", values, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitBetween(Integer value1, Integer value2) {
            addCriterion("transmit_band_limit between", value1, value2, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andTransmitBandLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("transmit_band_limit not between", value1, value2, "transmitBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitIsNull() {
            addCriterion("recv_band_limit is null");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitIsNotNull() {
            addCriterion("recv_band_limit is not null");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitEqualTo(Integer value) {
            addCriterion("recv_band_limit =", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitNotEqualTo(Integer value) {
            addCriterion("recv_band_limit <>", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitGreaterThan(Integer value) {
            addCriterion("recv_band_limit >", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitGreaterThanOrEqualTo(Integer value) {
            addCriterion("recv_band_limit >=", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitLessThan(Integer value) {
            addCriterion("recv_band_limit <", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitLessThanOrEqualTo(Integer value) {
            addCriterion("recv_band_limit <=", value, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitIn(List<Integer> values) {
            addCriterion("recv_band_limit in", values, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitNotIn(List<Integer> values) {
            addCriterion("recv_band_limit not in", values, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitBetween(Integer value1, Integer value2) {
            addCriterion("recv_band_limit between", value1, value2, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andRecvBandLimitNotBetween(Integer value1, Integer value2) {
            addCriterion("recv_band_limit not between", value1, value2, "recvBandLimit");
            return (Criteria) this;
        }

        public Criteria andGpuNumIsNull() {
            addCriterion("gpu_num is null");
            return (Criteria) this;
        }

        public Criteria andGpuNumIsNotNull() {
            addCriterion("gpu_num is not null");
            return (Criteria) this;
        }

        public Criteria andGpuNumEqualTo(Integer value) {
            addCriterion("gpu_num =", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotEqualTo(Integer value) {
            addCriterion("gpu_num <>", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumGreaterThan(Integer value) {
            addCriterion("gpu_num >", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("gpu_num >=", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumLessThan(Integer value) {
            addCriterion("gpu_num <", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumLessThanOrEqualTo(Integer value) {
            addCriterion("gpu_num <=", value, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumIn(List<Integer> values) {
            addCriterion("gpu_num in", values, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotIn(List<Integer> values) {
            addCriterion("gpu_num not in", values, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumBetween(Integer value1, Integer value2) {
            addCriterion("gpu_num between", value1, value2, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andGpuNumNotBetween(Integer value1, Integer value2) {
            addCriterion("gpu_num not between", value1, value2, "gpuNum");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIsNull() {
            addCriterion("dev_need_info is null");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIsNotNull() {
            addCriterion("dev_need_info is not null");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoEqualTo(String value) {
            addCriterion("dev_need_info =", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotEqualTo(String value) {
            addCriterion("dev_need_info <>", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoGreaterThan(String value) {
            addCriterion("dev_need_info >", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoGreaterThanOrEqualTo(String value) {
            addCriterion("dev_need_info >=", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLessThan(String value) {
            addCriterion("dev_need_info <", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLessThanOrEqualTo(String value) {
            addCriterion("dev_need_info <=", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoLike(String value) {
            addCriterion("dev_need_info like", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotLike(String value) {
            addCriterion("dev_need_info not like", value, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoIn(List<String> values) {
            addCriterion("dev_need_info in", values, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotIn(List<String> values) {
            addCriterion("dev_need_info not in", values, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoBetween(String value1, String value2) {
            addCriterion("dev_need_info between", value1, value2, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andDevNeedInfoNotBetween(String value1, String value2) {
            addCriterion("dev_need_info not between", value1, value2, "devNeedInfo");
            return (Criteria) this;
        }

        public Criteria andContainerParamsIsNull() {
            addCriterion("container_params is null");
            return (Criteria) this;
        }

        public Criteria andContainerParamsIsNotNull() {
            addCriterion("container_params is not null");
            return (Criteria) this;
        }

        public Criteria andContainerParamsEqualTo(String value) {
            addCriterion("container_params =", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotEqualTo(String value) {
            addCriterion("container_params <>", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsGreaterThan(String value) {
            addCriterion("container_params >", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsGreaterThanOrEqualTo(String value) {
            addCriterion("container_params >=", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsLessThan(String value) {
            addCriterion("container_params <", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsLessThanOrEqualTo(String value) {
            addCriterion("container_params <=", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsLike(String value) {
            addCriterion("container_params like", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotLike(String value) {
            addCriterion("container_params not like", value, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsIn(List<String> values) {
            addCriterion("container_params in", values, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotIn(List<String> values) {
            addCriterion("container_params not in", values, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsBetween(String value1, String value2) {
            addCriterion("container_params between", value1, value2, "containerParams");
            return (Criteria) this;
        }

        public Criteria andContainerParamsNotBetween(String value1, String value2) {
            addCriterion("container_params not between", value1, value2, "containerParams");
            return (Criteria) this;
        }

        public Criteria andAutoRunIsNull() {
            addCriterion("auto_run is null");
            return (Criteria) this;
        }

        public Criteria andAutoRunIsNotNull() {
            addCriterion("auto_run is not null");
            return (Criteria) this;
        }

        public Criteria andAutoRunEqualTo(Boolean value) {
            addCriterion("auto_run =", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotEqualTo(Boolean value) {
            addCriterion("auto_run <>", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunGreaterThan(Boolean value) {
            addCriterion("auto_run >", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunGreaterThanOrEqualTo(Boolean value) {
            addCriterion("auto_run >=", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunLessThan(Boolean value) {
            addCriterion("auto_run <", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunLessThanOrEqualTo(Boolean value) {
            addCriterion("auto_run <=", value, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunIn(List<Boolean> values) {
            addCriterion("auto_run in", values, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotIn(List<Boolean> values) {
            addCriterion("auto_run not in", values, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunBetween(Boolean value1, Boolean value2) {
            addCriterion("auto_run between", value1, value2, "autoRun");
            return (Criteria) this;
        }

        public Criteria andAutoRunNotBetween(Boolean value1, Boolean value2) {
            addCriterion("auto_run not between", value1, value2, "autoRun");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIsNull() {
            addCriterion("replica_num is null");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIsNotNull() {
            addCriterion("replica_num is not null");
            return (Criteria) this;
        }

        public Criteria andReplicaNumEqualTo(Integer value) {
            addCriterion("replica_num =", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotEqualTo(Integer value) {
            addCriterion("replica_num <>", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumGreaterThan(Integer value) {
            addCriterion("replica_num >", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("replica_num >=", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumLessThan(Integer value) {
            addCriterion("replica_num <", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumLessThanOrEqualTo(Integer value) {
            addCriterion("replica_num <=", value, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumIn(List<Integer> values) {
            addCriterion("replica_num in", values, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotIn(List<Integer> values) {
            addCriterion("replica_num not in", values, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumBetween(Integer value1, Integer value2) {
            addCriterion("replica_num between", value1, value2, "replicaNum");
            return (Criteria) this;
        }

        public Criteria andReplicaNumNotBetween(Integer value1, Integer value2) {
            addCriterion("replica_num not between", value1, value2, "replicaNum");
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

        public Criteria andRestartPolicyIsNull() {
            addCriterion("restart_policy is null");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyIsNotNull() {
            addCriterion("restart_policy is not null");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyEqualTo(String value) {
            addCriterion("restart_policy =", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyNotEqualTo(String value) {
            addCriterion("restart_policy <>", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyGreaterThan(String value) {
            addCriterion("restart_policy >", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyGreaterThanOrEqualTo(String value) {
            addCriterion("restart_policy >=", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyLessThan(String value) {
            addCriterion("restart_policy <", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyLessThanOrEqualTo(String value) {
            addCriterion("restart_policy <=", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyLike(String value) {
            addCriterion("restart_policy like", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyNotLike(String value) {
            addCriterion("restart_policy not like", value, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyIn(List<String> values) {
            addCriterion("restart_policy in", values, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyNotIn(List<String> values) {
            addCriterion("restart_policy not in", values, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyBetween(String value1, String value2) {
            addCriterion("restart_policy between", value1, value2, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andRestartPolicyNotBetween(String value1, String value2) {
            addCriterion("restart_policy not between", value1, value2, "restartPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyIsNull() {
            addCriterion("failover_policy is null");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyIsNotNull() {
            addCriterion("failover_policy is not null");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyEqualTo(String value) {
            addCriterion("failover_policy =", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotEqualTo(String value) {
            addCriterion("failover_policy <>", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyGreaterThan(String value) {
            addCriterion("failover_policy >", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyGreaterThanOrEqualTo(String value) {
            addCriterion("failover_policy >=", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyLessThan(String value) {
            addCriterion("failover_policy <", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyLessThanOrEqualTo(String value) {
            addCriterion("failover_policy <=", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyLike(String value) {
            addCriterion("failover_policy like", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotLike(String value) {
            addCriterion("failover_policy not like", value, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyIn(List<String> values) {
            addCriterion("failover_policy in", values, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotIn(List<String> values) {
            addCriterion("failover_policy not in", values, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyBetween(String value1, String value2) {
            addCriterion("failover_policy between", value1, value2, "failoverPolicy");
            return (Criteria) this;
        }

        public Criteria andFailoverPolicyNotBetween(String value1, String value2) {
            addCriterion("failover_policy not between", value1, value2, "failoverPolicy");
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