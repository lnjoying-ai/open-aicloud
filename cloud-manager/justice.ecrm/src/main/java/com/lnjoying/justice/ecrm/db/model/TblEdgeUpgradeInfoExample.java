package com.lnjoying.justice.ecrm.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblEdgeUpgradeInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblEdgeUpgradeInfoExample() {
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

        public Criteria andUpgradeStatusIsNull() {
            addCriterion("upgrade_status is null");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusIsNotNull() {
            addCriterion("upgrade_status is not null");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusEqualTo(Integer value) {
            addCriterion("upgrade_status =", value, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusNotEqualTo(Integer value) {
            addCriterion("upgrade_status <>", value, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusGreaterThan(Integer value) {
            addCriterion("upgrade_status >", value, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("upgrade_status >=", value, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusLessThan(Integer value) {
            addCriterion("upgrade_status <", value, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusLessThanOrEqualTo(Integer value) {
            addCriterion("upgrade_status <=", value, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusIn(List<Integer> values) {
            addCriterion("upgrade_status in", values, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusNotIn(List<Integer> values) {
            addCriterion("upgrade_status not in", values, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusBetween(Integer value1, Integer value2) {
            addCriterion("upgrade_status between", value1, value2, "upgradeStatus");
            return (Criteria) this;
        }

        public Criteria andUpgradeStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("upgrade_status not between", value1, value2, "upgradeStatus");
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

        public Criteria andUpgradePlanIsNull() {
            addCriterion("upgrade_plan is null");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanIsNotNull() {
            addCriterion("upgrade_plan is not null");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanEqualTo(String value) {
            addCriterion("upgrade_plan =", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanNotEqualTo(String value) {
            addCriterion("upgrade_plan <>", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanGreaterThan(String value) {
            addCriterion("upgrade_plan >", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanGreaterThanOrEqualTo(String value) {
            addCriterion("upgrade_plan >=", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanLessThan(String value) {
            addCriterion("upgrade_plan <", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanLessThanOrEqualTo(String value) {
            addCriterion("upgrade_plan <=", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanLike(String value) {
            addCriterion("upgrade_plan like", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanNotLike(String value) {
            addCriterion("upgrade_plan not like", value, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanIn(List<String> values) {
            addCriterion("upgrade_plan in", values, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanNotIn(List<String> values) {
            addCriterion("upgrade_plan not in", values, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanBetween(String value1, String value2) {
            addCriterion("upgrade_plan between", value1, value2, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andUpgradePlanNotBetween(String value1, String value2) {
            addCriterion("upgrade_plan not between", value1, value2, "upgradePlan");
            return (Criteria) this;
        }

        public Criteria andOldVersionIsNull() {
            addCriterion("old_version is null");
            return (Criteria) this;
        }

        public Criteria andOldVersionIsNotNull() {
            addCriterion("old_version is not null");
            return (Criteria) this;
        }

        public Criteria andOldVersionEqualTo(String value) {
            addCriterion("old_version =", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionNotEqualTo(String value) {
            addCriterion("old_version <>", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionGreaterThan(String value) {
            addCriterion("old_version >", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionGreaterThanOrEqualTo(String value) {
            addCriterion("old_version >=", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionLessThan(String value) {
            addCriterion("old_version <", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionLessThanOrEqualTo(String value) {
            addCriterion("old_version <=", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionLike(String value) {
            addCriterion("old_version like", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionNotLike(String value) {
            addCriterion("old_version not like", value, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionIn(List<String> values) {
            addCriterion("old_version in", values, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionNotIn(List<String> values) {
            addCriterion("old_version not in", values, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionBetween(String value1, String value2) {
            addCriterion("old_version between", value1, value2, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andOldVersionNotBetween(String value1, String value2) {
            addCriterion("old_version not between", value1, value2, "oldVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionIsNull() {
            addCriterion("new_version is null");
            return (Criteria) this;
        }

        public Criteria andNewVersionIsNotNull() {
            addCriterion("new_version is not null");
            return (Criteria) this;
        }

        public Criteria andNewVersionEqualTo(String value) {
            addCriterion("new_version =", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotEqualTo(String value) {
            addCriterion("new_version <>", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionGreaterThan(String value) {
            addCriterion("new_version >", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionGreaterThanOrEqualTo(String value) {
            addCriterion("new_version >=", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionLessThan(String value) {
            addCriterion("new_version <", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionLessThanOrEqualTo(String value) {
            addCriterion("new_version <=", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionLike(String value) {
            addCriterion("new_version like", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotLike(String value) {
            addCriterion("new_version not like", value, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionIn(List<String> values) {
            addCriterion("new_version in", values, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotIn(List<String> values) {
            addCriterion("new_version not in", values, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionBetween(String value1, String value2) {
            addCriterion("new_version between", value1, value2, "newVersion");
            return (Criteria) this;
        }

        public Criteria andNewVersionNotBetween(String value1, String value2) {
            addCriterion("new_version not between", value1, value2, "newVersion");
            return (Criteria) this;
        }

        public Criteria andOperUserIsNull() {
            addCriterion("oper_user is null");
            return (Criteria) this;
        }

        public Criteria andOperUserIsNotNull() {
            addCriterion("oper_user is not null");
            return (Criteria) this;
        }

        public Criteria andOperUserEqualTo(String value) {
            addCriterion("oper_user =", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserNotEqualTo(String value) {
            addCriterion("oper_user <>", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserGreaterThan(String value) {
            addCriterion("oper_user >", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserGreaterThanOrEqualTo(String value) {
            addCriterion("oper_user >=", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserLessThan(String value) {
            addCriterion("oper_user <", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserLessThanOrEqualTo(String value) {
            addCriterion("oper_user <=", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserLike(String value) {
            addCriterion("oper_user like", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserNotLike(String value) {
            addCriterion("oper_user not like", value, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserIn(List<String> values) {
            addCriterion("oper_user in", values, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserNotIn(List<String> values) {
            addCriterion("oper_user not in", values, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserBetween(String value1, String value2) {
            addCriterion("oper_user between", value1, value2, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperUserNotBetween(String value1, String value2) {
            addCriterion("oper_user not between", value1, value2, "operUser");
            return (Criteria) this;
        }

        public Criteria andOperBpIsNull() {
            addCriterion("oper_bp is null");
            return (Criteria) this;
        }

        public Criteria andOperBpIsNotNull() {
            addCriterion("oper_bp is not null");
            return (Criteria) this;
        }

        public Criteria andOperBpEqualTo(String value) {
            addCriterion("oper_bp =", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpNotEqualTo(String value) {
            addCriterion("oper_bp <>", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpGreaterThan(String value) {
            addCriterion("oper_bp >", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpGreaterThanOrEqualTo(String value) {
            addCriterion("oper_bp >=", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpLessThan(String value) {
            addCriterion("oper_bp <", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpLessThanOrEqualTo(String value) {
            addCriterion("oper_bp <=", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpLike(String value) {
            addCriterion("oper_bp like", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpNotLike(String value) {
            addCriterion("oper_bp not like", value, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpIn(List<String> values) {
            addCriterion("oper_bp in", values, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpNotIn(List<String> values) {
            addCriterion("oper_bp not in", values, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpBetween(String value1, String value2) {
            addCriterion("oper_bp between", value1, value2, "operBp");
            return (Criteria) this;
        }

        public Criteria andOperBpNotBetween(String value1, String value2) {
            addCriterion("oper_bp not between", value1, value2, "operBp");
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