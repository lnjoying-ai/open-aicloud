package com.lnjoying.justice.iam.service;

import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.iam.db.model.TblBpInfo;
import com.lnjoying.justice.iam.db.model.TblIamAssignment;
import com.lnjoying.justice.iam.db.model.TblIamAttachment;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.db.repo.AuthzRepository;
import com.lnjoying.justice.iam.db.repo.BpRepository;
import com.lnjoying.justice.iam.db.repo.ProjectRepository;
import com.lnjoying.justice.iam.db.repo.RoleRepository;
import com.lnjoying.justice.iam.domain.dto.request.project.*;
import com.lnjoying.justice.iam.domain.dto.response.project.ProjectsRsp;
import com.lnjoying.justice.iam.domain.dto.response.project.TblIamProjectDetail;
import com.lnjoying.justice.iam.domain.model.IamAssignment;
import com.lnjoying.justice.iam.domain.model.IamProject;
import com.lnjoying.justice.iam.domain.model.RecordStatus;
import com.lnjoying.justice.iam.utils.NameGenerator;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.RandomName;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.common.constant.IamConstants.SUBTRACT;
import static com.lnjoying.justice.iam.common.constant.ProjectConstants.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 16:07
 */

@Slf4j
@Service
public class ProjectService
{

    private static final String ACTIVE = "active";
    private static final String DEACTIVE = "deactive";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BpRepository bpRepository;

    @Autowired
    private AuthzRepository authzRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Object addProject(AddProjectReq projectReq)
    {

        if (!checkParentProjectExists(projectReq.getParentId()))
        {
            throw new WebSystemException(IAM_PARENT_PROJECT_NOT_EXIST, ErrorLevel.ERROR);
        }

        TblIamProject tblIamProject = new TblIamProject();
        BeanUtils.copyProperties(projectReq, tblIamProject);
        tblIamProject.setName(generateProjectName(projectReq, projectReq.getBpId()));
        tblIamProject.setId(Utils.assignUUId());
        // default enable
        int enable = Objects.nonNull(projectReq.getEnable()) ? projectReq.getEnable() : TblIamProject.ProjectEnableStatus.ENABLE.value();
        tblIamProject.setEnable(enable);
        tblIamProject.setStatus(RecordStatus.NORMAL.value());
        tblIamProject.setCreateTime(new Date());
        tblIamProject.setUpdateTime(tblIamProject.getCreateTime());

        projectRepository.insertSelective(tblIamProject);
        return tblIamProject.getId();
    }


    public void updateProject(String projectId, UpdateProjectReq req)
    {
        if (!checkProjectExists(projectId, req.getBpId()))
        {
            throw new WebSystemException(IAM_PROJECT_NOT_EXIST, ErrorLevel.ERROR);
        }

        TblIamProject tblIamProject = new TblIamProject();
        BeanUtils.copyProperties(req, tblIamProject);
        tblIamProject.setId(projectId);
        tblIamProject.setUpdateTime(new Date());
        projectRepository.updateProject(tblIamProject);
    }

    public void action(String projectId, String userId, String bpId, String action)
    {
        TblIamProject tblIamProject = new TblIamProject();
        if (ACTIVE.equalsIgnoreCase(action))
        {
            tblIamProject.setEnable(TblIamProject.ProjectEnableStatus.ENABLE.value());
        }

        if (DEACTIVE.equalsIgnoreCase(action))
        {
            tblIamProject.setEnable(TblIamProject.ProjectEnableStatus.DISABLE.value());
        }

        tblIamProject.setId(projectId);
        tblIamProject.setUserId(userId);
        tblIamProject.setUpdateTime(new Date());
        projectRepository.updateProject(tblIamProject);
    }

    public ProjectsRsp getProjectList(String name, String queryBpId, Integer status, Integer pageSize, Integer pageNum)
    {
        ProjectsRsp projectList = projectRepository.getProjectList(name, queryBpId, status, pageSize, pageNum);
        return projectList;
    }


    public  ProjectsRsp getProjectTree(String name, String queryBpId, Integer status, Integer pageSize, Integer pageNum)
    {
        return projectRepository.getProjectTree(name, queryBpId, status, pageSize, pageNum);
    }

    public Object getProject(String projectId, String queryBpId)
    {
        TblIamProjectDetail tblIamProject = projectRepository.selectByIdAndBpId(projectId, queryBpId);
        return IamProject.of(tblIamProject);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteProject(String projectId, String bpId)
    {
        TblIamProjectDetail tblIamProject = projectRepository.selectByIdAndBpId(projectId, bpId);
        if (Objects.nonNull(tblIamProject))
        {
            // todo delete associated resources
            int userCount = projectRepository.countUsersInProject(projectId);
            if (userCount > 0)
            {
                throw new WebSystemException(IAM_PROJECT_HAS_ASSOCIATED_USER, ErrorLevel.ERROR);
            }
            projectRepository.deleteByPrimaryKey(projectId);

            int roleCount = roleRepository.countRolesInProject(projectId);
            if (roleCount > 0)
            {
                throw new WebSystemException(IAM_PROJECT_HAS_ASSOCIATED_ROLE, ErrorLevel.ERROR);
            }

            roleRepository.deleteByProjectId(projectId);
        }
    }


    public List<IamAssignment.IamUserAssignment> getUsersInProject(String projectId)
    {
        List<IamAssignment.IamUserAssignment> iamUserAssignments = projectRepository.selectUsersInProject(projectId);
        return iamUserAssignments;
    }

    public void addUsersToProject(String projectId, ProjectUsersReq req)
    {
        if (Objects.nonNull(req) && !CollectionUtils.isEmpty(req.getUserIds()))
        {
            List<TblIamAssignment> iamAssignments = buildTblIamAssignments(req.getUserIds(), TblIamAssignment.AssignmentType.USER, projectId);

            projectRepository.batchInsertAssignments(iamAssignments);
        }
    }


    public void deleteUsersFromProject(String projectId, ProjectUsersReq req)
    {
        if (Objects.nonNull(req) && !CollectionUtils.isEmpty(req.getUserIds()))
        {
            List<TblIamAssignment> iamAssignments = buildTblIamAssignments(req.getUserIds(), TblIamAssignment.AssignmentType.USER, projectId);
            projectRepository.batchDeleteAssignments(iamAssignments);

            List<TblIamAttachment> attachments = buildTblIamAttachments(req.getUserIds(), projectId);
            authzRepository.batchDeleteAssignments(attachments);

        }
    }


    public Object getGroupsInProject(String projectId)
    {
        //todo
        List<IamAssignment.IamGroupAssignment> iamGroupAssignments = projectRepository.selectGroupsInProject(projectId);
        return iamGroupAssignments;
    }

    public void addGroupsToProject(String projectId, ProjectGroupsReq req)
    {
        if (Objects.nonNull(req) && !CollectionUtils.isEmpty(req.getGroupIds()))
        {
            List<TblIamAssignment> iamAssignments = buildTblIamAssignments(req.getGroupIds(), TblIamAssignment.AssignmentType.GROUP, projectId);

            projectRepository.batchInsertAssignments(iamAssignments);
        }
    }


    public void deleteGroupsFromProject(String projectId, ProjectGroupsReq req)
    {
        if (Objects.nonNull(req) && !CollectionUtils.isEmpty(req.getGroupIds()))
        {
            List<TblIamAssignment> iamAssignments = buildTblIamAssignments(req.getGroupIds(), TblIamAssignment.AssignmentType.GROUP, projectId);

            projectRepository.batchDeleteAssignments(iamAssignments);
        }
    }

    public boolean checkParentProjectExists(String parentId)
    {
        if (StringUtils.isNotBlank(parentId))
        {
            TblIamProject tblIamProject = projectRepository.selectByPrimaryKey(parentId);
            return Objects.nonNull(tblIamProject) ? true : false;
        }
        return true;
    }

    public void checkProject()
    {
        try
        {
            // default admin project
            checkAdminDefaultProject();

            // create bp default project
            checkBpDefaultProject();
        }
        catch (Exception e)
        {
            log.error("check default project error:{}", e);
        }

    }

    public void insertDefaultProject(String bpId, String bpName)
    {
        try
        {
            TblIamProject tblIamProject = doCreateDefaultProject(bpId, bpName);
            projectRepository.insertDefaultProject(tblIamProject);
        }
        catch (Exception e)
        {
            log.error("insert default project failed, bpId:{}", bpId);
            throw new WebSystemException(IAM_PROJECT_CREATE_FAILED, ErrorLevel.ERROR);
        }

    }

    private void checkBpDefaultProject()
    {
        List<TblBpInfo> tblBpInfos = bpRepository.selectAll();

        List<TblBpInfo> needCreateProjects = new ArrayList<>();
        if (!CollectionUtils.isEmpty(tblBpInfos))
        {
            List<TblIamProject> tblIamProjects = projectRepository.selectByNameAndBpId(DEFAULT_PROJECT_PREFIX, null, TblIamProject.ProjectType.BP_DEFAULT.value());
            if (!CollectionUtils.isEmpty(tblIamProjects))
            {
                Set<String> existsDefaultProjectBps = tblIamProjects.stream().map(project -> project.getBpId()).filter(Objects::nonNull).collect(Collectors.toSet());

                if (CollectionUtils.isEmpty(existsDefaultProjectBps))
                {
                    needCreateProjects.addAll(tblBpInfos);
                }
                else
                {
                    tblBpInfos.stream().filter(tblBpInfo -> !existsDefaultProjectBps.contains(tblBpInfo.getBpId())).forEach(tblBpInfo -> {
                        needCreateProjects.add(tblBpInfo);
                    });
                }
            }
            else
            {
               needCreateProjects.addAll(tblBpInfos);
            }

            if (!CollectionUtils.isEmpty(needCreateProjects))
            {
                List<TblIamProject> iamProjects = needCreateProjects.stream().map(bp ->
                {
                    return doCreateDefaultProject(bp.getBpId(), bp.getBpName());
                }).collect(Collectors.toList());

                projectRepository.batchInsert(iamProjects);
            }

        }
    }

    private static TblIamProject doCreateDefaultProject(String bpId, String bpName)
    {
        TblIamProject tblIamProject = new TblIamProject();
        tblIamProject.setName(DEFAULT_PROJECT_PREFIX + bpId);
        tblIamProject.setDisplayName(bpName + " project");
        tblIamProject.setId(Utils.assignUUId());
        tblIamProject.setEnable(TblIamProject.ProjectEnableStatus.ENABLE.value());
        tblIamProject.setStatus(RecordStatus.NORMAL.value());
        tblIamProject.setCreateTime(new Date());
        tblIamProject.setUpdateTime(tblIamProject.getCreateTime());
        tblIamProject.setDescription(bpName + " default project created by system, deletion is not recommended");
        tblIamProject.setBpId(bpId);
        tblIamProject.setType(TblIamProject.ProjectType.BP_DEFAULT.value());
        return tblIamProject;
    }

    private void checkAdminDefaultProject()
    {
        if(!checkProjectNameExists(DEFAULT_ADMIN_PROJECT, null))
        {
            TblIamProject tblIamProject = new TblIamProject();
            tblIamProject.setName(DEFAULT_ADMIN_PROJECT);
            tblIamProject.setDisplayName(DEFAULT_ADMIN_PROJECT_DISPLAY_NAME);
            tblIamProject.setId(Utils.assignUUId());
            tblIamProject.setEnable(TblIamProject.ProjectEnableStatus.ENABLE.value());
            tblIamProject.setStatus(RecordStatus.NORMAL.value());
            tblIamProject.setCreateTime(new Date());
            tblIamProject.setUpdateTime(tblIamProject.getCreateTime());
            tblIamProject.setDescription(DEFAULT_ADMIN_PROJECT_DESCRIPTION);

            projectRepository.insertSelective(tblIamProject);
        }
    }

    private String generateProjectName(AddProjectReq req, String bpId)
    {
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append("p");
        if (StringUtils.isNotBlank(bpId))
        {
            nameBuilder.append(SUBTRACT);
            nameBuilder.append(bpId);
        }
        nameBuilder.append(SUBTRACT);

        return genProjectName(nameBuilder.toString(), 6);
    }

    public String genProjectName(String prefix, int count)
    {
        String projectName = prefix + RandomName.getRandomName(count);
        List<TblIamProject> tblIamProjects = projectRepository.selectByName(projectName);

        if (!CollectionUtils.isEmpty(tblIamProjects))
        {
            if (count < 20)
            {
                return genProjectName(prefix, count + 1);
            }
            else
            {
                log.info("unable to generate appropriate project Name");
                throw new WebSystemException(IAM_GEN_NAME_ERROR, ErrorLevel.ERROR);
            }
        }
        else
        {
            return projectName;
        }
    }

    public String getProjectName(String policyNamePrefix, int versionNumber)
    {
        return NameGenerator.getName(policyNamePrefix, versionNumber);
    }

    private String getNextProjectName(String name)
    {
        return NameGenerator.getNextName(name);
    }


    private static List<TblIamAssignment> buildTblIamAssignments(List<String> req, TblIamAssignment.AssignmentType user, String projectId)
    {
        List<TblIamAssignment> iamAssignments = req.stream().map(userId ->
        {
            TblIamAssignment tblIamAssignment = new TblIamAssignment();
            tblIamAssignment.setType(user.value());
            tblIamAssignment.setTargetId(projectId);
            tblIamAssignment.setActorId(userId);
            return tblIamAssignment;
        }).collect(Collectors.toList());
        return iamAssignments;
    }


    private  List<TblIamAttachment> buildTblIamAttachments(List<String> userIds, String projectId)
    {
        if (!CollectionUtils.isEmpty(userIds))
        {
            List<TblIamAttachment> iamAttachmentList = userIds.stream().map(userId ->
            {
                TblIamAttachment tblIamAttachment = new TblIamAttachment();
                tblIamAttachment.setId(Utils.assignUUId());
                tblIamAttachment.setPrincipalType(TblIamAttachment.PrincipalType.USER.value());
                tblIamAttachment.setPrincipalId(userId);
                tblIamAttachment.setProjectId(projectId);
                return tblIamAttachment;
            }).collect(Collectors.toList());

            return iamAttachmentList;
        }

        return Collections.emptyList();

    }


    private boolean checkProjectNameExists(String projectName, String bpId)
    {
        int count = projectRepository.countByNameAndBpId(projectName, bpId);
        return count > 0 ? true : false;
    }

    private boolean checkProjectExists(String projectId, String bpId)
    {
        int count = projectRepository.countByIdAndBpId(projectId, bpId);
        return count > 0 ? true : false;
    }


}
