package com.lnjoying.justice.iam.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.iam.db.mapper.TblIamRoleMapper;
import com.lnjoying.justice.iam.db.mapper.TblRoleInfoMapper;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.domain.dto.response.authz.AttachmentEntitiesRsp;
import com.lnjoying.justice.iam.domain.dto.response.policy.PoliciesRsp;
import com.lnjoying.justice.iam.domain.dto.response.policy.TblIamPolicyDetail;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleRsp;
import com.lnjoying.justice.iam.domain.dto.response.role.TblIamRoleDetail;
import com.lnjoying.justice.iam.domain.model.IamPolicy;
import com.lnjoying.justice.iam.domain.model.IamRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/14 17:21
 */

@Repository
public class RoleRepository
{
    @Autowired
    private TblRoleInfoMapper roleInfoMapper;

    @Autowired
    private TblIamRoleMapper iamRoleMapper;

    public int countRoleIds(List<Long> roleIds)
    {
        Integer count = iamRoleMapper.countByRoleIdIn(roleIds);
        return count;
    }

    public int countByName(String projectId, String bpId,String platform,String role)
    {
        Integer count = iamRoleMapper.countByName(projectId, bpId, platform, role);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public int insertSelective(TblIamRole record)
    {
        return iamRoleMapper.insertSelective(record);
    }

    public int countById(long roleId)
    {
        return iamRoleMapper.countByRoleId(roleId);
    }


    public int updateByPrimaryKeySelective(TblIamRole record)
    {
        return iamRoleMapper.updateByPrimaryKeySelective(record);
    }

    public RoleRsp getRoleList(String name, String projectId, String queryBpId, Integer roleType, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblIamRoleDetail> tblIamRoles = iamRoleMapper.selectAll(projectId, queryBpId, null, name, roleType);
        PageInfo<TblIamRoleDetail> pageInfo = new PageInfo<>(tblIamRoles);
        return RoleRsp.builder().totalNum(pageInfo.getTotal())
                .roles(tblIamRoles.stream().map(IamRole::of).collect(Collectors.toList())).build();
    }

    public TblIamRoleDetail selectByIdAndBpId(long roleId, String bpId)
    {
        return iamRoleMapper.selectByRoleIdAndBpId(roleId, bpId);
    }

    public void deleteRole(long roleId)
    {
        iamRoleMapper.deleteByPrimaryKey(roleId);
    }

    public List<AttachmentEntitiesRsp.Role> selectAllByRoleIdIn(Collection<Long> roleIds)
    {

        List<TblIamRoleDetail> tblIamRoleDetails = iamRoleMapper.selectAllByRoleIdIn(roleIds);
        if (!CollectionUtils.isEmpty(tblIamRoleDetails))
        {
           return  tblIamRoleDetails.stream().map(AttachmentEntitiesRsp.Role::of).collect(Collectors.toList());
        }

        return Collections.EMPTY_LIST;
    }


    public Integer selectKindByRoleIds(Collection<Long> roleIdCollection)
    {
        return iamRoleMapper.selectKindByRoleIdIn(roleIdCollection);
    }

    public int countRolesInProject(String projectId)
    {
        Integer count = iamRoleMapper.countByProjectId(projectId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public void deleteByProjectId(String projectId)
    {
        iamRoleMapper.deleteByProjectId(projectId);
    }
}
