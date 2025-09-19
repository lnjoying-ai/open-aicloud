package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpBaremetalDeviceSpecExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpBaremetalDeviceSpecExample() {
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

        public Criteria andDeviceSpecIdIsNull() {
            addCriterion("device_spec_id is null");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdIsNotNull() {
            addCriterion("device_spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdEqualTo(String value) {
            addCriterion("device_spec_id =", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotEqualTo(String value) {
            addCriterion("device_spec_id <>", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdGreaterThan(String value) {
            addCriterion("device_spec_id >", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdGreaterThanOrEqualTo(String value) {
            addCriterion("device_spec_id >=", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdLessThan(String value) {
            addCriterion("device_spec_id <", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdLessThanOrEqualTo(String value) {
            addCriterion("device_spec_id <=", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdLike(String value) {
            addCriterion("device_spec_id like", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotLike(String value) {
            addCriterion("device_spec_id not like", value, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdIn(List<String> values) {
            addCriterion("device_spec_id in", values, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotIn(List<String> values) {
            addCriterion("device_spec_id not in", values, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdBetween(String value1, String value2) {
            addCriterion("device_spec_id between", value1, value2, "deviceSpecId");
            return (Criteria) this;
        }

        public Criteria andDeviceSpecIdNotBetween(String value1, String value2) {
            addCriterion("device_spec_id not between", value1, value2, "deviceSpecId");
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

        public Criteria andProductNameIsNull() {
            addCriterion("product_name is null");
            return (Criteria) this;
        }

        public Criteria andProductNameIsNotNull() {
            addCriterion("product_name is not null");
            return (Criteria) this;
        }

        public Criteria andProductNameEqualTo(String value) {
            addCriterion("product_name =", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotEqualTo(String value) {
            addCriterion("product_name <>", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThan(String value) {
            addCriterion("product_name >", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameGreaterThanOrEqualTo(String value) {
            addCriterion("product_name >=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThan(String value) {
            addCriterion("product_name <", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLessThanOrEqualTo(String value) {
            addCriterion("product_name <=", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameLike(String value) {
            addCriterion("product_name like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotLike(String value) {
            addCriterion("product_name not like", value, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameIn(List<String> values) {
            addCriterion("product_name in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotIn(List<String> values) {
            addCriterion("product_name not in", values, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameBetween(String value1, String value2) {
            addCriterion("product_name between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andProductNameNotBetween(String value1, String value2) {
            addCriterion("product_name not between", value1, value2, "productName");
            return (Criteria) this;
        }

        public Criteria andSerialNumberIsNull() {
            addCriterion("serial_number is null");
            return (Criteria) this;
        }

        public Criteria andSerialNumberIsNotNull() {
            addCriterion("serial_number is not null");
            return (Criteria) this;
        }

        public Criteria andSerialNumberEqualTo(String value) {
            addCriterion("serial_number =", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberNotEqualTo(String value) {
            addCriterion("serial_number <>", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberGreaterThan(String value) {
            addCriterion("serial_number >", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberGreaterThanOrEqualTo(String value) {
            addCriterion("serial_number >=", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberLessThan(String value) {
            addCriterion("serial_number <", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberLessThanOrEqualTo(String value) {
            addCriterion("serial_number <=", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberLike(String value) {
            addCriterion("serial_number like", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberNotLike(String value) {
            addCriterion("serial_number not like", value, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberIn(List<String> values) {
            addCriterion("serial_number in", values, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberNotIn(List<String> values) {
            addCriterion("serial_number not in", values, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberBetween(String value1, String value2) {
            addCriterion("serial_number between", value1, value2, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andSerialNumberNotBetween(String value1, String value2) {
            addCriterion("serial_number not between", value1, value2, "serialNumber");
            return (Criteria) this;
        }

        public Criteria andManufacturerIsNull() {
            addCriterion("manufacturer is null");
            return (Criteria) this;
        }

        public Criteria andManufacturerIsNotNull() {
            addCriterion("manufacturer is not null");
            return (Criteria) this;
        }

        public Criteria andManufacturerEqualTo(String value) {
            addCriterion("manufacturer =", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerNotEqualTo(String value) {
            addCriterion("manufacturer <>", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerGreaterThan(String value) {
            addCriterion("manufacturer >", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerGreaterThanOrEqualTo(String value) {
            addCriterion("manufacturer >=", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerLessThan(String value) {
            addCriterion("manufacturer <", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerLessThanOrEqualTo(String value) {
            addCriterion("manufacturer <=", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerLike(String value) {
            addCriterion("manufacturer like", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerNotLike(String value) {
            addCriterion("manufacturer not like", value, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerIn(List<String> values) {
            addCriterion("manufacturer in", values, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerNotIn(List<String> values) {
            addCriterion("manufacturer not in", values, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerBetween(String value1, String value2) {
            addCriterion("manufacturer between", value1, value2, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andManufacturerNotBetween(String value1, String value2) {
            addCriterion("manufacturer not between", value1, value2, "manufacturer");
            return (Criteria) this;
        }

        public Criteria andArchitectureIsNull() {
            addCriterion("architecture is null");
            return (Criteria) this;
        }

        public Criteria andArchitectureIsNotNull() {
            addCriterion("architecture is not null");
            return (Criteria) this;
        }

        public Criteria andArchitectureEqualTo(String value) {
            addCriterion("architecture =", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotEqualTo(String value) {
            addCriterion("architecture <>", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureGreaterThan(String value) {
            addCriterion("architecture >", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureGreaterThanOrEqualTo(String value) {
            addCriterion("architecture >=", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureLessThan(String value) {
            addCriterion("architecture <", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureLessThanOrEqualTo(String value) {
            addCriterion("architecture <=", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureLike(String value) {
            addCriterion("architecture like", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotLike(String value) {
            addCriterion("architecture not like", value, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureIn(List<String> values) {
            addCriterion("architecture in", values, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotIn(List<String> values) {
            addCriterion("architecture not in", values, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureBetween(String value1, String value2) {
            addCriterion("architecture between", value1, value2, "architecture");
            return (Criteria) this;
        }

        public Criteria andArchitectureNotBetween(String value1, String value2) {
            addCriterion("architecture not between", value1, value2, "architecture");
            return (Criteria) this;
        }

        public Criteria andProcessorCountIsNull() {
            addCriterion("processor_count is null");
            return (Criteria) this;
        }

        public Criteria andProcessorCountIsNotNull() {
            addCriterion("processor_count is not null");
            return (Criteria) this;
        }

        public Criteria andProcessorCountEqualTo(Integer value) {
            addCriterion("processor_count =", value, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountNotEqualTo(Integer value) {
            addCriterion("processor_count <>", value, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountGreaterThan(Integer value) {
            addCriterion("processor_count >", value, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountGreaterThanOrEqualTo(Integer value) {
            addCriterion("processor_count >=", value, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountLessThan(Integer value) {
            addCriterion("processor_count <", value, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountLessThanOrEqualTo(Integer value) {
            addCriterion("processor_count <=", value, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountIn(List<Integer> values) {
            addCriterion("processor_count in", values, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountNotIn(List<Integer> values) {
            addCriterion("processor_count not in", values, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountBetween(Integer value1, Integer value2) {
            addCriterion("processor_count between", value1, value2, "processorCount");
            return (Criteria) this;
        }

        public Criteria andProcessorCountNotBetween(Integer value1, Integer value2) {
            addCriterion("processor_count not between", value1, value2, "processorCount");
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

        public Criteria andCpuModelNameIsNull() {
            addCriterion("cpu_model_name is null");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameIsNotNull() {
            addCriterion("cpu_model_name is not null");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameEqualTo(String value) {
            addCriterion("cpu_model_name =", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameNotEqualTo(String value) {
            addCriterion("cpu_model_name <>", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameGreaterThan(String value) {
            addCriterion("cpu_model_name >", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameGreaterThanOrEqualTo(String value) {
            addCriterion("cpu_model_name >=", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameLessThan(String value) {
            addCriterion("cpu_model_name <", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameLessThanOrEqualTo(String value) {
            addCriterion("cpu_model_name <=", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameLike(String value) {
            addCriterion("cpu_model_name like", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameNotLike(String value) {
            addCriterion("cpu_model_name not like", value, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameIn(List<String> values) {
            addCriterion("cpu_model_name in", values, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameNotIn(List<String> values) {
            addCriterion("cpu_model_name not in", values, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameBetween(String value1, String value2) {
            addCriterion("cpu_model_name between", value1, value2, "cpuModelName");
            return (Criteria) this;
        }

        public Criteria andCpuModelNameNotBetween(String value1, String value2) {
            addCriterion("cpu_model_name not between", value1, value2, "cpuModelName");
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