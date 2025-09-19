package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpNicSpecExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpNicSpecExample() {
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

        public Criteria andNicSpecIdIsNull() {
            addCriterion("nic_spec_id is null");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdIsNotNull() {
            addCriterion("nic_spec_id is not null");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdEqualTo(String value) {
            addCriterion("nic_spec_id =", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdNotEqualTo(String value) {
            addCriterion("nic_spec_id <>", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdGreaterThan(String value) {
            addCriterion("nic_spec_id >", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdGreaterThanOrEqualTo(String value) {
            addCriterion("nic_spec_id >=", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdLessThan(String value) {
            addCriterion("nic_spec_id <", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdLessThanOrEqualTo(String value) {
            addCriterion("nic_spec_id <=", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdLike(String value) {
            addCriterion("nic_spec_id like", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdNotLike(String value) {
            addCriterion("nic_spec_id not like", value, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdIn(List<String> values) {
            addCriterion("nic_spec_id in", values, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdNotIn(List<String> values) {
            addCriterion("nic_spec_id not in", values, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdBetween(String value1, String value2) {
            addCriterion("nic_spec_id between", value1, value2, "nicSpecId");
            return (Criteria) this;
        }

        public Criteria andNicSpecIdNotBetween(String value1, String value2) {
            addCriterion("nic_spec_id not between", value1, value2, "nicSpecId");
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

        public Criteria andNicProductNameIsNull() {
            addCriterion("nic_product_name is null");
            return (Criteria) this;
        }

        public Criteria andNicProductNameIsNotNull() {
            addCriterion("nic_product_name is not null");
            return (Criteria) this;
        }

        public Criteria andNicProductNameEqualTo(String value) {
            addCriterion("nic_product_name =", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameNotEqualTo(String value) {
            addCriterion("nic_product_name <>", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameGreaterThan(String value) {
            addCriterion("nic_product_name >", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameGreaterThanOrEqualTo(String value) {
            addCriterion("nic_product_name >=", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameLessThan(String value) {
            addCriterion("nic_product_name <", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameLessThanOrEqualTo(String value) {
            addCriterion("nic_product_name <=", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameLike(String value) {
            addCriterion("nic_product_name like", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameNotLike(String value) {
            addCriterion("nic_product_name not like", value, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameIn(List<String> values) {
            addCriterion("nic_product_name in", values, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameNotIn(List<String> values) {
            addCriterion("nic_product_name not in", values, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameBetween(String value1, String value2) {
            addCriterion("nic_product_name between", value1, value2, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andNicProductNameNotBetween(String value1, String value2) {
            addCriterion("nic_product_name not between", value1, value2, "nicProductName");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNull() {
            addCriterion("speed is null");
            return (Criteria) this;
        }

        public Criteria andSpeedIsNotNull() {
            addCriterion("speed is not null");
            return (Criteria) this;
        }

        public Criteria andSpeedEqualTo(String value) {
            addCriterion("speed =", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotEqualTo(String value) {
            addCriterion("speed <>", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThan(String value) {
            addCriterion("speed >", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedGreaterThanOrEqualTo(String value) {
            addCriterion("speed >=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThan(String value) {
            addCriterion("speed <", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLessThanOrEqualTo(String value) {
            addCriterion("speed <=", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedLike(String value) {
            addCriterion("speed like", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotLike(String value) {
            addCriterion("speed not like", value, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedIn(List<String> values) {
            addCriterion("speed in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotIn(List<String> values) {
            addCriterion("speed not in", values, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedBetween(String value1, String value2) {
            addCriterion("speed between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andSpeedNotBetween(String value1, String value2) {
            addCriterion("speed not between", value1, value2, "speed");
            return (Criteria) this;
        }

        public Criteria andMacIsNull() {
            addCriterion("mac is null");
            return (Criteria) this;
        }

        public Criteria andMacIsNotNull() {
            addCriterion("mac is not null");
            return (Criteria) this;
        }

        public Criteria andMacEqualTo(String value) {
            addCriterion("mac =", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotEqualTo(String value) {
            addCriterion("mac <>", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacGreaterThan(String value) {
            addCriterion("mac >", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacGreaterThanOrEqualTo(String value) {
            addCriterion("mac >=", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacLessThan(String value) {
            addCriterion("mac <", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacLessThanOrEqualTo(String value) {
            addCriterion("mac <=", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacLike(String value) {
            addCriterion("mac like", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotLike(String value) {
            addCriterion("mac not like", value, "mac");
            return (Criteria) this;
        }

        public Criteria andMacIn(List<String> values) {
            addCriterion("mac in", values, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotIn(List<String> values) {
            addCriterion("mac not in", values, "mac");
            return (Criteria) this;
        }

        public Criteria andMacBetween(String value1, String value2) {
            addCriterion("mac between", value1, value2, "mac");
            return (Criteria) this;
        }

        public Criteria andMacNotBetween(String value1, String value2) {
            addCriterion("mac not between", value1, value2, "mac");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpIsNull() {
            addCriterion("link_is_up is null");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpIsNotNull() {
            addCriterion("link_is_up is not null");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpEqualTo(Boolean value) {
            addCriterion("link_is_up =", value, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpNotEqualTo(Boolean value) {
            addCriterion("link_is_up <>", value, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpGreaterThan(Boolean value) {
            addCriterion("link_is_up >", value, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpGreaterThanOrEqualTo(Boolean value) {
            addCriterion("link_is_up >=", value, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpLessThan(Boolean value) {
            addCriterion("link_is_up <", value, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpLessThanOrEqualTo(Boolean value) {
            addCriterion("link_is_up <=", value, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpIn(List<Boolean> values) {
            addCriterion("link_is_up in", values, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpNotIn(List<Boolean> values) {
            addCriterion("link_is_up not in", values, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpBetween(Boolean value1, Boolean value2) {
            addCriterion("link_is_up between", value1, value2, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andLinkIsUpNotBetween(Boolean value1, Boolean value2) {
            addCriterion("link_is_up not between", value1, value2, "linkIsUp");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentIsNull() {
            addCriterion("slot_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentIsNotNull() {
            addCriterion("slot_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentEqualTo(String value) {
            addCriterion("slot_id_from_agent =", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentNotEqualTo(String value) {
            addCriterion("slot_id_from_agent <>", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentGreaterThan(String value) {
            addCriterion("slot_id_from_agent >", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("slot_id_from_agent >=", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentLessThan(String value) {
            addCriterion("slot_id_from_agent <", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("slot_id_from_agent <=", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentLike(String value) {
            addCriterion("slot_id_from_agent like", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentNotLike(String value) {
            addCriterion("slot_id_from_agent not like", value, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentIn(List<String> values) {
            addCriterion("slot_id_from_agent in", values, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentNotIn(List<String> values) {
            addCriterion("slot_id_from_agent not in", values, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentBetween(String value1, String value2) {
            addCriterion("slot_id_from_agent between", value1, value2, "slotIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSlotIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("slot_id_from_agent not between", value1, value2, "slotIdFromAgent");
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

        public Criteria andSwitchIdFromAgentIsNull() {
            addCriterion("switch_id_from_agent is null");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentIsNotNull() {
            addCriterion("switch_id_from_agent is not null");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentEqualTo(String value) {
            addCriterion("switch_id_from_agent =", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentNotEqualTo(String value) {
            addCriterion("switch_id_from_agent <>", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentGreaterThan(String value) {
            addCriterion("switch_id_from_agent >", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentGreaterThanOrEqualTo(String value) {
            addCriterion("switch_id_from_agent >=", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentLessThan(String value) {
            addCriterion("switch_id_from_agent <", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentLessThanOrEqualTo(String value) {
            addCriterion("switch_id_from_agent <=", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentLike(String value) {
            addCriterion("switch_id_from_agent like", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentNotLike(String value) {
            addCriterion("switch_id_from_agent not like", value, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentIn(List<String> values) {
            addCriterion("switch_id_from_agent in", values, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentNotIn(List<String> values) {
            addCriterion("switch_id_from_agent not in", values, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentBetween(String value1, String value2) {
            addCriterion("switch_id_from_agent between", value1, value2, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchIdFromAgentNotBetween(String value1, String value2) {
            addCriterion("switch_id_from_agent not between", value1, value2, "switchIdFromAgent");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceIsNull() {
            addCriterion("switch_interface is null");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceIsNotNull() {
            addCriterion("switch_interface is not null");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceEqualTo(String value) {
            addCriterion("switch_interface =", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceNotEqualTo(String value) {
            addCriterion("switch_interface <>", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceGreaterThan(String value) {
            addCriterion("switch_interface >", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceGreaterThanOrEqualTo(String value) {
            addCriterion("switch_interface >=", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceLessThan(String value) {
            addCriterion("switch_interface <", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceLessThanOrEqualTo(String value) {
            addCriterion("switch_interface <=", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceLike(String value) {
            addCriterion("switch_interface like", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceNotLike(String value) {
            addCriterion("switch_interface not like", value, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceIn(List<String> values) {
            addCriterion("switch_interface in", values, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceNotIn(List<String> values) {
            addCriterion("switch_interface not in", values, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceBetween(String value1, String value2) {
            addCriterion("switch_interface between", value1, value2, "switchInterface");
            return (Criteria) this;
        }

        public Criteria andSwitchInterfaceNotBetween(String value1, String value2) {
            addCriterion("switch_interface not between", value1, value2, "switchInterface");
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