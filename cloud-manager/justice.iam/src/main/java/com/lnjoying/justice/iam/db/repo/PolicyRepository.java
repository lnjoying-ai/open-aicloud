package com.lnjoying.justice.iam.db.repo;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.iam.db.mapper.TblIamPolicyMapper;
import com.lnjoying.justice.iam.db.mapper.TblIamPolicyVersionMapper;
import com.lnjoying.justice.iam.db.model.TblIamPolicy;
import com.lnjoying.justice.iam.db.model.TblIamPolicyVersion;
import com.lnjoying.justice.iam.domain.dto.response.policy.PoliciesRsp;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import com.lnjoying.justice.iam.domain.dto.response.project.ProjectsRsp;
import com.lnjoying.justice.iam.domain.dto.response.project.TblIamProjectDetail;
import com.lnjoying.justice.iam.domain.model.IamPolicy;
import com.lnjoying.justice.iam.domain.model.IamPolicyDocument;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/10 19:06
 */

@Repository
public class PolicyRepository
{
    @Autowired
    private TblIamPolicyMapper policyMapper;

    @Autowired
    private TblIamPolicyVersionMapper policyVersionMapper;

    public PoliciesRsp getPolicyList(String name, String queryBpId, Integer policyType, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblIamPolicyDetail> tblIamPolicies = policyMapper.selectAll(name, queryBpId, policyType, false);
        PageInfo<TblIamPolicyDetail> pageInfo = new PageInfo<>(tblIamPolicies);
        return PoliciesRsp.builder().totalNum(pageInfo.getTotal())
                .policies(tblIamPolicies.stream().map(IamPolicy::of).collect(Collectors.toList())).build();
    }

    public int countByNameAndBpId(String policyName, String bpId)
    {
        Integer count = policyMapper.countByNameAndBpId(policyName, bpId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public int countByIdAndBpId(String policyId, String bpId)
    {
        Integer count = policyMapper.countByIdAndBpId(policyId, bpId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public void insertSelective(TblIamPolicy tblIamPolicy)
    {
        policyMapper.insertSelective(tblIamPolicy);
    }

    public TblIamPolicy selectByPrimaryKey(String id)
    {
        return policyMapper.selectByPrimaryKey(id);
    }

    public TblIamPolicyDetail selectByIdAndBpId(String id, String bpId)
    {
        return policyMapper.selectByIdAndBpId(id, bpId);
    }

    public int deletePolicyByPrimaryKey(String policyId)
    {
        return policyMapper.deleteByPrimaryKey(policyId);
    }

    public int updateByPrimaryKeySelective(TblIamPolicy record)
    {
        return policyMapper.updateByPrimaryKeySelective(record);
    }

    public List<TblIamPolicyVersion> selectPolicyVersionsByPolicyId(String policyId)
    {
        return policyVersionMapper.selectByPolicyId(policyId);
    }

    public TblIamPolicyVersion selectPolicyVersionByPolicyIdAndVersionId(String policyId,String versionId)
    {
        List<TblIamPolicyVersion> tblIamPolicyVersions = policyVersionMapper.selectByPolicyIdAndVersionId(policyId, versionId);
        if (CollectionUtils.isNotEmpty(tblIamPolicyVersions))
        {
            return tblIamPolicyVersions.get(0);
        }

        return null;
    }

    public int insertPolicyVersionSelective(TblIamPolicyVersion record)
    {
        return policyVersionMapper.insertSelective(record);
    }

    public int deletePolicyVersionByPolicyIdAndVersionId(String policyId,String versionId)
    {
        return policyVersionMapper.deleteByPolicyIdAndVersionId(policyId,versionId);
    }

    public Integer countPolicyVersions(String policyId ,String versionId)
    {
        return policyVersionMapper.countByPolicyIdAndVersionId(policyId, versionId);
    }

    public void batchQueryPolicyIds(List<String> policyIds)
    {

    }

    public int countPolicyIds(List<String> policyIds)
    {
        return policyMapper.countByIdIn(policyIds);
    }


    public String selectLatestNameByName(String name)
    {
        return policyMapper.selectLatestNameByName(name);
    }


    public List<IamPolicyDocument> selectByBase(Boolean base)
    {
        return policyMapper.selectByBase(base);
    }

    public String selectLatestVersionIdByPolicyId(String policyId)
    {
        return policyVersionMapper.selectLatestVersionIdByPolicyId(policyId);
    }

    public int updatePolicyVersion(TblIamPolicyVersion updated, String policyId, String versionId)
    {
        return policyVersionMapper.updateByPolicyIdAndVersionId(updated, policyId, versionId);
    }

    public List<TblIamPolicy> selectByNameAndBase(String name, Boolean base)
    {
        return policyMapper.selectByNameAndBase(name, base);
    }
}
