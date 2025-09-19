package com.lnjoying.justice.ims.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryMapper;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryProjectMapper;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryProjectsRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryProject;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.lnjoying.justice.ims.db.model.RecordStatus.DELETED;
import static com.lnjoying.justice.ims.db.model.RecordStatus.NORMAL;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * project repository
 *
 * @author merak
 **/

@Repository
public class ImsProjectRepository
{

    @Autowired
    TblImsRegistryProjectMapper imsProjectMapper;

    @Autowired
    TblImsRegistryMapper imsRegistryMapper;

    public String insertRegistryProject(AddRegistryProjectReq req)
    {
        checkIfTheProjectExists(req.getProjectName());

        TblImsRegistryProject tblImsRegistryProject = new TblImsRegistryProject();
        BeanUtils.copyProperties(req, tblImsRegistryProject);
        tblImsRegistryProject.setCreateTime(LocalDateTime.now());
        tblImsRegistryProject.setUpdateTime(tblImsRegistryProject.getCreateTime());
        tblImsRegistryProject.setStatus(NORMAL.value());
        tblImsRegistryProject.setProjectId(Utils.assignUUId());
        imsProjectMapper.insertSelective(tblImsRegistryProject);
        return tblImsRegistryProject.getProjectId();
    }

    public RegistryProjectsRsp getProjectList(String registryId, String projectName, Integer scope, String userId, String bpId, Integer pageNum, Integer pageSize, boolean isAdmin, boolean onlyOwner)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblImsRegistryProject> tblImsRegistryProjects = new ArrayList<>();
        if (isAdmin)
        {
            tblImsRegistryProjects = imsProjectMapper.selectAll(registryId, projectName, scope, userId, bpId, NORMAL.value());
        }
        else
        {
            if (onlyOwner)
            {
                tblImsRegistryProjects = imsProjectMapper.selectAll(registryId, projectName, scope, userId, bpId, NORMAL.value());
            }
            else
            {
                tblImsRegistryProjects = imsProjectMapper.selectAllOrdinaryTenant(registryId, projectName, scope, userId, bpId, NORMAL.value());
            }
        }
        PageInfo<TblImsRegistryProject> pageInfo = new PageInfo<>(tblImsRegistryProjects);
        return RegistryProjectsRsp.builder().totalNum(pageInfo.getTotal())
                .projects(tblImsRegistryProjects.stream().map(ImsRegistryProject::of)
                        .map(imsRegistryProject -> fillRegistryName(imsRegistryProject)).collect(Collectors.toList())).build();
    }

    /**
     * tenant project query
     *
     * @param userId
     * @param bpId
     * @return
     */
    public List<TblImsRegistryProject> getAllProjectByTenantUserId(String userId, String bpId)
    {

        List<TblImsRegistryProject> tblImsRegistryProjects = imsProjectMapper.selectAllOrdinaryTenant(null, null, null, userId, bpId, NORMAL.value());
        if (CollectionUtils.isNotEmpty(tblImsRegistryProjects))
        {
            return tblImsRegistryProjects;
        }

        return Collections.EMPTY_LIST;
    }

    public void insert(TblImsRegistryProject record)
    {
        imsProjectMapper.insert(record);
    }

    public void update(TblImsRegistryProject tblImsRegistryProject)
    {
        imsProjectMapper.updateByPrimaryKeySelective(tblImsRegistryProject);
    }

    /**
     * Use physical delete instead of delete state
     *
     * @param projectId
     * @param userId
     */
    @Deprecated
    public void delete(String projectId, String bpId, String userId)
    {
        TblImsRegistryProject registryProject = getRegistryProject(projectId, bpId, userId);
        if (Objects.nonNull(registryProject))
        {
            TblImsRegistryProject tblImsRegistryProject = new TblImsRegistryProject();
            tblImsRegistryProject.setProjectId(projectId);
            tblImsRegistryProject.setStatus(DELETED.value());
            tblImsRegistryProject.setUpdateTime(LocalDateTime.now());
            tblImsRegistryProject.setUserId(userId);
            String toBeDeleted = registryProject.getProjectName() + "_" + DELETED.name();
            tblImsRegistryProject.setProjectName(toBeDeleted);
            imsProjectMapper.updateByPrimaryKeySelective(tblImsRegistryProject);
        }
    }

    public void deletePhysical(String projectId, String bpId, String userId)
    {
        TblImsRegistryProject registryProject = getRegistryProject(projectId, bpId, userId);
        if (Objects.nonNull(registryProject))
        {
            imsProjectMapper.deleteByPrimaryKey(projectId);
        }
    }

    public TblImsRegistryProject getRegistryProject(String projectId, String bpId, String operUserId)
    {
        TblImsRegistryProject registryProject = imsProjectMapper.selectByPrimaryKey(projectId);

        if (Objects.isNull(registryProject))
        {
            throw new ImsWebSystemException(REGISTRY_PROJECT_NOT_EXIST, INFO);
        }

        if (DELETED.value() == registryProject.getStatus())
        {
            throw new ImsWebSystemException(REGISTRY_PROJECT_DROPPED, INFO);
        }

        if (Objects.nonNull(bpId))
        {
            if (registryProject.getScope().equals(TblImsRegistryProject.ProjectScope.BP))
            {
                if (!bpId.equals(registryProject.getBpId()))
                {
                    throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
                }
            }
        }

        if (Objects.nonNull(operUserId))
        {
            if (registryProject.getScope().equals(TblImsRegistryProject.ProjectScope.PRIVATE))
            {
                if (!operUserId.equals(registryProject.getUserId()))
                {
                    throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
                }
            }
        }

        return registryProject;
    }

    public TblImsRegistryProject getRegistryProjectByProjectName(String projectName)
    {
        TblImsRegistryProject registryProject = imsProjectMapper.selectByProjectName(projectName);

        if (Objects.isNull(registryProject))
        {
            throw new ImsWebSystemException(REGISTRY_PROJECT_NOT_EXIST, INFO);
        }

        if (DELETED.value() == registryProject.getStatus())
        {
            throw new ImsWebSystemException(REGISTRY_PROJECT_DROPPED, INFO);
        }
        return registryProject;
    }

    public List<TblImsRegistryProject> selectByScope(Integer scope)
    {
        return imsProjectMapper.selectByScope(scope);
    }

    public List<TblImsRegistryProject> selectByRegistryId(String registryId)
    {
        return imsProjectMapper.selectByRegistryId(registryId);
    }

    private void checkIfTheProjectExists(String registryProjectName)
    {
        TblImsRegistryProject tblImsRegistryProject = imsProjectMapper.selectByProjectName(registryProjectName);
        if (Objects.nonNull(tblImsRegistryProject))
        {
            throw new ImsWebSystemException(REGISTRY_PROJECT_EXIST, ErrorLevel.INFO);
        }
    }

    private ImsRegistryProject fillRegistryName(ImsRegistryProject imsRegistryProject)
    {
        String registryId = imsRegistryProject.getRegistryId();
        if (StringUtils.isNotBlank(registryId))
        {
            TblImsRegistry tblImsRegistry = imsRegistryMapper.selectByPrimaryKey(registryId);
            if (Objects.nonNull(tblImsRegistry))
            {
                imsRegistryProject.setRegistryName(tblImsRegistry.getRegistryName());
            }
        }

        return imsRegistryProject;
    }
}
