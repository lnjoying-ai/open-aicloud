package com.lnjoying.justice.cmp.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblCmpOsInstanceExtraExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblCmpOsInstanceExtraExample() {
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

        public Criteria andInstanceUuidIsNull() {
            addCriterion("instance_uuid is null");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidIsNotNull() {
            addCriterion("instance_uuid is not null");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidEqualTo(String value) {
            addCriterion("instance_uuid =", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotEqualTo(String value) {
            addCriterion("instance_uuid <>", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidGreaterThan(String value) {
            addCriterion("instance_uuid >", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidGreaterThanOrEqualTo(String value) {
            addCriterion("instance_uuid >=", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidLessThan(String value) {
            addCriterion("instance_uuid <", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidLessThanOrEqualTo(String value) {
            addCriterion("instance_uuid <=", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidLike(String value) {
            addCriterion("instance_uuid like", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotLike(String value) {
            addCriterion("instance_uuid not like", value, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidIn(List<String> values) {
            addCriterion("instance_uuid in", values, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotIn(List<String> values) {
            addCriterion("instance_uuid not in", values, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidBetween(String value1, String value2) {
            addCriterion("instance_uuid between", value1, value2, "instanceUuid");
            return (Criteria) this;
        }

        public Criteria andInstanceUuidNotBetween(String value1, String value2) {
            addCriterion("instance_uuid not between", value1, value2, "instanceUuid");
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

        public Criteria andDeletedAtIsNull() {
            addCriterion("deleted_at is null");
            return (Criteria) this;
        }

        public Criteria andDeletedAtIsNotNull() {
            addCriterion("deleted_at is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedAtEqualTo(Date value) {
            addCriterion("deleted_at =", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtNotEqualTo(Date value) {
            addCriterion("deleted_at <>", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtGreaterThan(Date value) {
            addCriterion("deleted_at >", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtGreaterThanOrEqualTo(Date value) {
            addCriterion("deleted_at >=", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtLessThan(Date value) {
            addCriterion("deleted_at <", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtLessThanOrEqualTo(Date value) {
            addCriterion("deleted_at <=", value, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtIn(List<Date> values) {
            addCriterion("deleted_at in", values, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtNotIn(List<Date> values) {
            addCriterion("deleted_at not in", values, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtBetween(Date value1, Date value2) {
            addCriterion("deleted_at between", value1, value2, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedAtNotBetween(Date value1, Date value2) {
            addCriterion("deleted_at not between", value1, value2, "deletedAt");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNull() {
            addCriterion("deleted is null");
            return (Criteria) this;
        }

        public Criteria andDeletedIsNotNull() {
            addCriterion("deleted is not null");
            return (Criteria) this;
        }

        public Criteria andDeletedEqualTo(Integer value) {
            addCriterion("deleted =", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotEqualTo(Integer value) {
            addCriterion("deleted <>", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThan(Integer value) {
            addCriterion("deleted >", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedGreaterThanOrEqualTo(Integer value) {
            addCriterion("deleted >=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThan(Integer value) {
            addCriterion("deleted <", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedLessThanOrEqualTo(Integer value) {
            addCriterion("deleted <=", value, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedIn(List<Integer> values) {
            addCriterion("deleted in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotIn(List<Integer> values) {
            addCriterion("deleted not in", values, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedBetween(Integer value1, Integer value2) {
            addCriterion("deleted between", value1, value2, "deleted");
            return (Criteria) this;
        }

        public Criteria andDeletedNotBetween(Integer value1, Integer value2) {
            addCriterion("deleted not between", value1, value2, "deleted");
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

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyIsNull() {
            addCriterion("numa_topology is null");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyIsNotNull() {
            addCriterion("numa_topology is not null");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyEqualTo(String value) {
            addCriterion("numa_topology =", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyNotEqualTo(String value) {
            addCriterion("numa_topology <>", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyGreaterThan(String value) {
            addCriterion("numa_topology >", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyGreaterThanOrEqualTo(String value) {
            addCriterion("numa_topology >=", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyLessThan(String value) {
            addCriterion("numa_topology <", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyLessThanOrEqualTo(String value) {
            addCriterion("numa_topology <=", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyLike(String value) {
            addCriterion("numa_topology like", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyNotLike(String value) {
            addCriterion("numa_topology not like", value, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyIn(List<String> values) {
            addCriterion("numa_topology in", values, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyNotIn(List<String> values) {
            addCriterion("numa_topology not in", values, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyBetween(String value1, String value2) {
            addCriterion("numa_topology between", value1, value2, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andNumaTopologyNotBetween(String value1, String value2) {
            addCriterion("numa_topology not between", value1, value2, "numaTopology");
            return (Criteria) this;
        }

        public Criteria andPciRequestsIsNull() {
            addCriterion("pci_requests is null");
            return (Criteria) this;
        }

        public Criteria andPciRequestsIsNotNull() {
            addCriterion("pci_requests is not null");
            return (Criteria) this;
        }

        public Criteria andPciRequestsEqualTo(String value) {
            addCriterion("pci_requests =", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsNotEqualTo(String value) {
            addCriterion("pci_requests <>", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsGreaterThan(String value) {
            addCriterion("pci_requests >", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsGreaterThanOrEqualTo(String value) {
            addCriterion("pci_requests >=", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsLessThan(String value) {
            addCriterion("pci_requests <", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsLessThanOrEqualTo(String value) {
            addCriterion("pci_requests <=", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsLike(String value) {
            addCriterion("pci_requests like", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsNotLike(String value) {
            addCriterion("pci_requests not like", value, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsIn(List<String> values) {
            addCriterion("pci_requests in", values, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsNotIn(List<String> values) {
            addCriterion("pci_requests not in", values, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsBetween(String value1, String value2) {
            addCriterion("pci_requests between", value1, value2, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andPciRequestsNotBetween(String value1, String value2) {
            addCriterion("pci_requests not between", value1, value2, "pciRequests");
            return (Criteria) this;
        }

        public Criteria andFlavorIsNull() {
            addCriterion("flavor is null");
            return (Criteria) this;
        }

        public Criteria andFlavorIsNotNull() {
            addCriterion("flavor is not null");
            return (Criteria) this;
        }

        public Criteria andFlavorEqualTo(String value) {
            addCriterion("flavor =", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorNotEqualTo(String value) {
            addCriterion("flavor <>", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorGreaterThan(String value) {
            addCriterion("flavor >", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorGreaterThanOrEqualTo(String value) {
            addCriterion("flavor >=", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorLessThan(String value) {
            addCriterion("flavor <", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorLessThanOrEqualTo(String value) {
            addCriterion("flavor <=", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorLike(String value) {
            addCriterion("flavor like", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorNotLike(String value) {
            addCriterion("flavor not like", value, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorIn(List<String> values) {
            addCriterion("flavor in", values, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorNotIn(List<String> values) {
            addCriterion("flavor not in", values, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorBetween(String value1, String value2) {
            addCriterion("flavor between", value1, value2, "flavor");
            return (Criteria) this;
        }

        public Criteria andFlavorNotBetween(String value1, String value2) {
            addCriterion("flavor not between", value1, value2, "flavor");
            return (Criteria) this;
        }

        public Criteria andVcpuModelIsNull() {
            addCriterion("vcpu_model is null");
            return (Criteria) this;
        }

        public Criteria andVcpuModelIsNotNull() {
            addCriterion("vcpu_model is not null");
            return (Criteria) this;
        }

        public Criteria andVcpuModelEqualTo(String value) {
            addCriterion("vcpu_model =", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelNotEqualTo(String value) {
            addCriterion("vcpu_model <>", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelGreaterThan(String value) {
            addCriterion("vcpu_model >", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("vcpu_model >=", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelLessThan(String value) {
            addCriterion("vcpu_model <", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelLessThanOrEqualTo(String value) {
            addCriterion("vcpu_model <=", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelLike(String value) {
            addCriterion("vcpu_model like", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelNotLike(String value) {
            addCriterion("vcpu_model not like", value, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelIn(List<String> values) {
            addCriterion("vcpu_model in", values, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelNotIn(List<String> values) {
            addCriterion("vcpu_model not in", values, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelBetween(String value1, String value2) {
            addCriterion("vcpu_model between", value1, value2, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andVcpuModelNotBetween(String value1, String value2) {
            addCriterion("vcpu_model not between", value1, value2, "vcpuModel");
            return (Criteria) this;
        }

        public Criteria andMigrationContextIsNull() {
            addCriterion("migration_context is null");
            return (Criteria) this;
        }

        public Criteria andMigrationContextIsNotNull() {
            addCriterion("migration_context is not null");
            return (Criteria) this;
        }

        public Criteria andMigrationContextEqualTo(String value) {
            addCriterion("migration_context =", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextNotEqualTo(String value) {
            addCriterion("migration_context <>", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextGreaterThan(String value) {
            addCriterion("migration_context >", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextGreaterThanOrEqualTo(String value) {
            addCriterion("migration_context >=", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextLessThan(String value) {
            addCriterion("migration_context <", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextLessThanOrEqualTo(String value) {
            addCriterion("migration_context <=", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextLike(String value) {
            addCriterion("migration_context like", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextNotLike(String value) {
            addCriterion("migration_context not like", value, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextIn(List<String> values) {
            addCriterion("migration_context in", values, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextNotIn(List<String> values) {
            addCriterion("migration_context not in", values, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextBetween(String value1, String value2) {
            addCriterion("migration_context between", value1, value2, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andMigrationContextNotBetween(String value1, String value2) {
            addCriterion("migration_context not between", value1, value2, "migrationContext");
            return (Criteria) this;
        }

        public Criteria andKeypairsIsNull() {
            addCriterion("keypairs is null");
            return (Criteria) this;
        }

        public Criteria andKeypairsIsNotNull() {
            addCriterion("keypairs is not null");
            return (Criteria) this;
        }

        public Criteria andKeypairsEqualTo(String value) {
            addCriterion("keypairs =", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsNotEqualTo(String value) {
            addCriterion("keypairs <>", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsGreaterThan(String value) {
            addCriterion("keypairs >", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsGreaterThanOrEqualTo(String value) {
            addCriterion("keypairs >=", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsLessThan(String value) {
            addCriterion("keypairs <", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsLessThanOrEqualTo(String value) {
            addCriterion("keypairs <=", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsLike(String value) {
            addCriterion("keypairs like", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsNotLike(String value) {
            addCriterion("keypairs not like", value, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsIn(List<String> values) {
            addCriterion("keypairs in", values, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsNotIn(List<String> values) {
            addCriterion("keypairs not in", values, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsBetween(String value1, String value2) {
            addCriterion("keypairs between", value1, value2, "keypairs");
            return (Criteria) this;
        }

        public Criteria andKeypairsNotBetween(String value1, String value2) {
            addCriterion("keypairs not between", value1, value2, "keypairs");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataIsNull() {
            addCriterion("device_metadata is null");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataIsNotNull() {
            addCriterion("device_metadata is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataEqualTo(String value) {
            addCriterion("device_metadata =", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataNotEqualTo(String value) {
            addCriterion("device_metadata <>", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataGreaterThan(String value) {
            addCriterion("device_metadata >", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataGreaterThanOrEqualTo(String value) {
            addCriterion("device_metadata >=", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataLessThan(String value) {
            addCriterion("device_metadata <", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataLessThanOrEqualTo(String value) {
            addCriterion("device_metadata <=", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataLike(String value) {
            addCriterion("device_metadata like", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataNotLike(String value) {
            addCriterion("device_metadata not like", value, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataIn(List<String> values) {
            addCriterion("device_metadata in", values, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataNotIn(List<String> values) {
            addCriterion("device_metadata not in", values, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataBetween(String value1, String value2) {
            addCriterion("device_metadata between", value1, value2, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andDeviceMetadataNotBetween(String value1, String value2) {
            addCriterion("device_metadata not between", value1, value2, "deviceMetadata");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsIsNull() {
            addCriterion("trusted_certs is null");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsIsNotNull() {
            addCriterion("trusted_certs is not null");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsEqualTo(String value) {
            addCriterion("trusted_certs =", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsNotEqualTo(String value) {
            addCriterion("trusted_certs <>", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsGreaterThan(String value) {
            addCriterion("trusted_certs >", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsGreaterThanOrEqualTo(String value) {
            addCriterion("trusted_certs >=", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsLessThan(String value) {
            addCriterion("trusted_certs <", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsLessThanOrEqualTo(String value) {
            addCriterion("trusted_certs <=", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsLike(String value) {
            addCriterion("trusted_certs like", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsNotLike(String value) {
            addCriterion("trusted_certs not like", value, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsIn(List<String> values) {
            addCriterion("trusted_certs in", values, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsNotIn(List<String> values) {
            addCriterion("trusted_certs not in", values, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsBetween(String value1, String value2) {
            addCriterion("trusted_certs between", value1, value2, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andTrustedCertsNotBetween(String value1, String value2) {
            addCriterion("trusted_certs not between", value1, value2, "trustedCerts");
            return (Criteria) this;
        }

        public Criteria andVpmemsIsNull() {
            addCriterion("vpmems is null");
            return (Criteria) this;
        }

        public Criteria andVpmemsIsNotNull() {
            addCriterion("vpmems is not null");
            return (Criteria) this;
        }

        public Criteria andVpmemsEqualTo(String value) {
            addCriterion("vpmems =", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsNotEqualTo(String value) {
            addCriterion("vpmems <>", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsGreaterThan(String value) {
            addCriterion("vpmems >", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsGreaterThanOrEqualTo(String value) {
            addCriterion("vpmems >=", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsLessThan(String value) {
            addCriterion("vpmems <", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsLessThanOrEqualTo(String value) {
            addCriterion("vpmems <=", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsLike(String value) {
            addCriterion("vpmems like", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsNotLike(String value) {
            addCriterion("vpmems not like", value, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsIn(List<String> values) {
            addCriterion("vpmems in", values, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsNotIn(List<String> values) {
            addCriterion("vpmems not in", values, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsBetween(String value1, String value2) {
            addCriterion("vpmems between", value1, value2, "vpmems");
            return (Criteria) this;
        }

        public Criteria andVpmemsNotBetween(String value1, String value2) {
            addCriterion("vpmems not between", value1, value2, "vpmems");
            return (Criteria) this;
        }

        public Criteria andResourcesIsNull() {
            addCriterion("resources is null");
            return (Criteria) this;
        }

        public Criteria andResourcesIsNotNull() {
            addCriterion("resources is not null");
            return (Criteria) this;
        }

        public Criteria andResourcesEqualTo(String value) {
            addCriterion("resources =", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesNotEqualTo(String value) {
            addCriterion("resources <>", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesGreaterThan(String value) {
            addCriterion("resources >", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesGreaterThanOrEqualTo(String value) {
            addCriterion("resources >=", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesLessThan(String value) {
            addCriterion("resources <", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesLessThanOrEqualTo(String value) {
            addCriterion("resources <=", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesLike(String value) {
            addCriterion("resources like", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesNotLike(String value) {
            addCriterion("resources not like", value, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesIn(List<String> values) {
            addCriterion("resources in", values, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesNotIn(List<String> values) {
            addCriterion("resources not in", values, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesBetween(String value1, String value2) {
            addCriterion("resources between", value1, value2, "resources");
            return (Criteria) this;
        }

        public Criteria andResourcesNotBetween(String value1, String value2) {
            addCriterion("resources not between", value1, value2, "resources");
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