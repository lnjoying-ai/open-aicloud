package com.lnjoying.justice.ecrm.db.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TblEdgeComputeGpuInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TblEdgeComputeGpuInfoExample() {
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

        public Criteria andGpuIdIsNull() {
            addCriterion("gpu_id is null");
            return (Criteria) this;
        }

        public Criteria andGpuIdIsNotNull() {
            addCriterion("gpu_id is not null");
            return (Criteria) this;
        }

        public Criteria andGpuIdEqualTo(String value) {
            addCriterion("gpu_id =", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdNotEqualTo(String value) {
            addCriterion("gpu_id <>", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdGreaterThan(String value) {
            addCriterion("gpu_id >", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdGreaterThanOrEqualTo(String value) {
            addCriterion("gpu_id >=", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdLessThan(String value) {
            addCriterion("gpu_id <", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdLessThanOrEqualTo(String value) {
            addCriterion("gpu_id <=", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdLike(String value) {
            addCriterion("gpu_id like", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdNotLike(String value) {
            addCriterion("gpu_id not like", value, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdIn(List<String> values) {
            addCriterion("gpu_id in", values, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdNotIn(List<String> values) {
            addCriterion("gpu_id not in", values, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdBetween(String value1, String value2) {
            addCriterion("gpu_id between", value1, value2, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuIdNotBetween(String value1, String value2) {
            addCriterion("gpu_id not between", value1, value2, "gpuId");
            return (Criteria) this;
        }

        public Criteria andGpuTypeIsNull() {
            addCriterion("gpu_type is null");
            return (Criteria) this;
        }

        public Criteria andGpuTypeIsNotNull() {
            addCriterion("gpu_type is not null");
            return (Criteria) this;
        }

        public Criteria andGpuTypeEqualTo(String value) {
            addCriterion("gpu_type =", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeNotEqualTo(String value) {
            addCriterion("gpu_type <>", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeGreaterThan(String value) {
            addCriterion("gpu_type >", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeGreaterThanOrEqualTo(String value) {
            addCriterion("gpu_type >=", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeLessThan(String value) {
            addCriterion("gpu_type <", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeLessThanOrEqualTo(String value) {
            addCriterion("gpu_type <=", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeLike(String value) {
            addCriterion("gpu_type like", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeNotLike(String value) {
            addCriterion("gpu_type not like", value, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeIn(List<String> values) {
            addCriterion("gpu_type in", values, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeNotIn(List<String> values) {
            addCriterion("gpu_type not in", values, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeBetween(String value1, String value2) {
            addCriterion("gpu_type between", value1, value2, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuTypeNotBetween(String value1, String value2) {
            addCriterion("gpu_type not between", value1, value2, "gpuType");
            return (Criteria) this;
        }

        public Criteria andGpuModelIsNull() {
            addCriterion("gpu_model is null");
            return (Criteria) this;
        }

        public Criteria andGpuModelIsNotNull() {
            addCriterion("gpu_model is not null");
            return (Criteria) this;
        }

        public Criteria andGpuModelEqualTo(String value) {
            addCriterion("gpu_model =", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelNotEqualTo(String value) {
            addCriterion("gpu_model <>", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelGreaterThan(String value) {
            addCriterion("gpu_model >", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelGreaterThanOrEqualTo(String value) {
            addCriterion("gpu_model >=", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelLessThan(String value) {
            addCriterion("gpu_model <", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelLessThanOrEqualTo(String value) {
            addCriterion("gpu_model <=", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelLike(String value) {
            addCriterion("gpu_model like", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelNotLike(String value) {
            addCriterion("gpu_model not like", value, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelIn(List<String> values) {
            addCriterion("gpu_model in", values, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelNotIn(List<String> values) {
            addCriterion("gpu_model not in", values, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelBetween(String value1, String value2) {
            addCriterion("gpu_model between", value1, value2, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andGpuModelNotBetween(String value1, String value2) {
            addCriterion("gpu_model not between", value1, value2, "gpuModel");
            return (Criteria) this;
        }

        public Criteria andDriverVersionIsNull() {
            addCriterion("driver_version is null");
            return (Criteria) this;
        }

        public Criteria andDriverVersionIsNotNull() {
            addCriterion("driver_version is not null");
            return (Criteria) this;
        }

        public Criteria andDriverVersionEqualTo(String value) {
            addCriterion("driver_version =", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionNotEqualTo(String value) {
            addCriterion("driver_version <>", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionGreaterThan(String value) {
            addCriterion("driver_version >", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionGreaterThanOrEqualTo(String value) {
            addCriterion("driver_version >=", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionLessThan(String value) {
            addCriterion("driver_version <", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionLessThanOrEqualTo(String value) {
            addCriterion("driver_version <=", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionLike(String value) {
            addCriterion("driver_version like", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionNotLike(String value) {
            addCriterion("driver_version not like", value, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionIn(List<String> values) {
            addCriterion("driver_version in", values, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionNotIn(List<String> values) {
            addCriterion("driver_version not in", values, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionBetween(String value1, String value2) {
            addCriterion("driver_version between", value1, value2, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andDriverVersionNotBetween(String value1, String value2) {
            addCriterion("driver_version not between", value1, value2, "driverVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionIsNull() {
            addCriterion("cuda_version is null");
            return (Criteria) this;
        }

        public Criteria andCudaVersionIsNotNull() {
            addCriterion("cuda_version is not null");
            return (Criteria) this;
        }

        public Criteria andCudaVersionEqualTo(String value) {
            addCriterion("cuda_version =", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionNotEqualTo(String value) {
            addCriterion("cuda_version <>", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionGreaterThan(String value) {
            addCriterion("cuda_version >", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionGreaterThanOrEqualTo(String value) {
            addCriterion("cuda_version >=", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionLessThan(String value) {
            addCriterion("cuda_version <", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionLessThanOrEqualTo(String value) {
            addCriterion("cuda_version <=", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionLike(String value) {
            addCriterion("cuda_version like", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionNotLike(String value) {
            addCriterion("cuda_version not like", value, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionIn(List<String> values) {
            addCriterion("cuda_version in", values, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionNotIn(List<String> values) {
            addCriterion("cuda_version not in", values, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionBetween(String value1, String value2) {
            addCriterion("cuda_version between", value1, value2, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudaVersionNotBetween(String value1, String value2) {
            addCriterion("cuda_version not between", value1, value2, "cudaVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionIsNull() {
            addCriterion("cudnn_version is null");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionIsNotNull() {
            addCriterion("cudnn_version is not null");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionEqualTo(String value) {
            addCriterion("cudnn_version =", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionNotEqualTo(String value) {
            addCriterion("cudnn_version <>", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionGreaterThan(String value) {
            addCriterion("cudnn_version >", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionGreaterThanOrEqualTo(String value) {
            addCriterion("cudnn_version >=", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionLessThan(String value) {
            addCriterion("cudnn_version <", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionLessThanOrEqualTo(String value) {
            addCriterion("cudnn_version <=", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionLike(String value) {
            addCriterion("cudnn_version like", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionNotLike(String value) {
            addCriterion("cudnn_version not like", value, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionIn(List<String> values) {
            addCriterion("cudnn_version in", values, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionNotIn(List<String> values) {
            addCriterion("cudnn_version not in", values, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionBetween(String value1, String value2) {
            addCriterion("cudnn_version between", value1, value2, "cudnnVersion");
            return (Criteria) this;
        }

        public Criteria andCudnnVersionNotBetween(String value1, String value2) {
            addCriterion("cudnn_version not between", value1, value2, "cudnnVersion");
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

        public Criteria andIndexIsNull() {
            addCriterion("index is null");
            return (Criteria) this;
        }

        public Criteria andIndexIsNotNull() {
            addCriterion("index is not null");
            return (Criteria) this;
        }

        public Criteria andIndexEqualTo(Integer value) {
            addCriterion("index =", value, "index");
            return (Criteria) this;
        }

        public Criteria andIndexNotEqualTo(Integer value) {
            addCriterion("index <>", value, "index");
            return (Criteria) this;
        }

        public Criteria andIndexGreaterThan(Integer value) {
            addCriterion("index >", value, "index");
            return (Criteria) this;
        }

        public Criteria andIndexGreaterThanOrEqualTo(Integer value) {
            addCriterion("index >=", value, "index");
            return (Criteria) this;
        }

        public Criteria andIndexLessThan(Integer value) {
            addCriterion("index <", value, "index");
            return (Criteria) this;
        }

        public Criteria andIndexLessThanOrEqualTo(Integer value) {
            addCriterion("index <=", value, "index");
            return (Criteria) this;
        }

        public Criteria andIndexIn(List<Integer> values) {
            addCriterion("index in", values, "index");
            return (Criteria) this;
        }

        public Criteria andIndexNotIn(List<Integer> values) {
            addCriterion("index not in", values, "index");
            return (Criteria) this;
        }

        public Criteria andIndexBetween(Integer value1, Integer value2) {
            addCriterion("index between", value1, value2, "index");
            return (Criteria) this;
        }

        public Criteria andIndexNotBetween(Integer value1, Integer value2) {
            addCriterion("index not between", value1, value2, "index");
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

        public Criteria andRefIdIsNull() {
            addCriterion("ref_id is null");
            return (Criteria) this;
        }

        public Criteria andRefIdIsNotNull() {
            addCriterion("ref_id is not null");
            return (Criteria) this;
        }

        public Criteria andRefIdEqualTo(String value) {
            addCriterion("ref_id =", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotEqualTo(String value) {
            addCriterion("ref_id <>", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdGreaterThan(String value) {
            addCriterion("ref_id >", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdGreaterThanOrEqualTo(String value) {
            addCriterion("ref_id >=", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdLessThan(String value) {
            addCriterion("ref_id <", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdLessThanOrEqualTo(String value) {
            addCriterion("ref_id <=", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdLike(String value) {
            addCriterion("ref_id like", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotLike(String value) {
            addCriterion("ref_id not like", value, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdIn(List<String> values) {
            addCriterion("ref_id in", values, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotIn(List<String> values) {
            addCriterion("ref_id not in", values, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdBetween(String value1, String value2) {
            addCriterion("ref_id between", value1, value2, "refId");
            return (Criteria) this;
        }

        public Criteria andRefIdNotBetween(String value1, String value2) {
            addCriterion("ref_id not between", value1, value2, "refId");
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