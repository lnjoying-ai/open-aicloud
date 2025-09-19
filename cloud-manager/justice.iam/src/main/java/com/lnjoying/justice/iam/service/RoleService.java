package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.db.model.TblIamRole;
import com.lnjoying.justice.iam.db.repo.AuthzRepository;
import com.lnjoying.justice.iam.db.repo.RoleRepository;
import com.lnjoying.justice.iam.domain.dto.request.role.AddRoleReq;
import com.lnjoying.justice.iam.domain.dto.request.role.UpdateRoleReq;
import com.lnjoying.justice.iam.domain.dto.response.role.RoleRsp;
import com.lnjoying.justice.iam.domain.dto.response.role.TblIamRoleDetail;
import com.lnjoying.justice.iam.domain.model.IamRole;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;


import static com.lnjoying.justice.iam.utils.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/3/10 10:27
 */

@Slf4j
@Service
public class RoleService
{
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthzRepository authzRepository;

    public Object addRole(AddRoleReq req)
    {
        String projectId = req.getProjectId();
        String bpId = req.getBpId();
        String platform = req.getPlatform();
        String role = req.getRole();
        if (checkRoleNameExists(projectId, bpId, platform, role))
        {
            throw new WebSystemException(IAM_ROLE_NAME_EXIST, ErrorLevel.ERROR);
        }

        TblIamRole tblIamRole = new TblIamRole();
        BeanUtils.copyProperties(req, tblIamRole);
        tblIamRole.setPlatform(platform);
        tblIamRole.setRole(role);
        tblIamRole.setProjectId(projectId);

        // todo check only admin can set system role
        tblIamRole.setRole(req.getRole());
        tblIamRole.setCreateTime(new Date());
        tblIamRole.setUpdateTime(tblIamRole.getCreateTime());
        roleRepository.insertSelective(tblIamRole);
        return tblIamRole.getRoleId();
    }


    public void updateRole(long roleId, UpdateRoleReq req)
    {
        if (!checkRoleExists(roleId))
        {
            throw new WebSystemException(IAM_ROLE_NOT_EXIST, ErrorLevel.ERROR);
        }
        checkAllowed(roleId, req.getBpId());
        TblIamRole tblIamRole = new TblIamRole();
        BeanUtils.copyProperties(req, tblIamRole);
        tblIamRole.setRoleId(roleId);
        tblIamRole.setUpdateTime(new Date());
        roleRepository.updateByPrimaryKeySelective(tblIamRole);
    }



    public RoleRsp getRoleList(String name, String projectId, String queryBpId, Integer roleType, Integer pageSize, Integer pageNum)
    {
        return roleRepository.getRoleList(name, projectId, queryBpId, roleType, pageSize, pageNum);
    }


    public Object getRole(long roleId, String queryBpId)
    {
        TblIamRoleDetail roleDetail = roleRepository.selectByIdAndBpId(roleId, queryBpId);
        if (Objects.nonNull(roleDetail))
        {
            return IamRole.of(roleDetail);
        }

        return null;
    }

    private boolean checkRoleNameExists(String projectId, String bpId,String platform,String role)
    {
        int count = roleRepository.countByName(projectId, bpId, platform, role);
        return count > 0 ? true : false;
    }

    private boolean checkRoleExists(long roleId)
    {
        int count = roleRepository.countById(roleId);
        return count > 0 ? true : false;
    }

    public void deleteRole(long roleId, String queryBpId)
    {
        checkAllowed(roleId, queryBpId);
        Integer numberOfAssociatedResources = authzRepository.countByUniqueId(String.valueOf(roleId));
        if (numberOfAssociatedResources > 0)
        {
            throw new WebSystemException(IAM_ROLE_HAS_ASSOCIATED_RESOURCES, ErrorLevel.ERROR);
        }
        roleRepository.deleteRole(roleId);
    }


    public void checkAllowed(long roleId, String queryBpId) {
        TblIamRoleDetail roleDetail = roleRepository.selectByIdAndBpId(roleId, queryBpId);
        if (Objects.nonNull(roleDetail))
        {
            if (StringUtils.isNotBlank(queryBpId) && StringUtils.isBlank(roleDetail.getBpId()))
            {
                throw new WebSystemException(IAM_ROLE_NOT_ALLOWED_ACTION, ErrorLevel.ERROR);
            }
            if (StringUtils.isNotBlank(roleDetail.getBpId()) && StringUtils.isNotBlank(queryBpId) && roleDetail.getBpId() != queryBpId)
            {
                throw new WebSystemException(IAM_ROLE_NOT_ALLOWED_ACTION, ErrorLevel.ERROR);
            }
        }
    }


}
