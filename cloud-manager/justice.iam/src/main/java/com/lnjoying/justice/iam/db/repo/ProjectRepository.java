package com.lnjoying.justice.iam.db.repo;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lnjoying.justice.iam.db.mapper.TblIamAssignmentMapper;
import com.lnjoying.justice.iam.db.mapper.TblIamProjectMapper;
import com.lnjoying.justice.iam.db.mapper.TblUserInfoMapper;
import com.lnjoying.justice.iam.db.model.TblIamAssignment;
import com.lnjoying.justice.iam.db.model.TblIamProject;
import com.lnjoying.justice.iam.db.model.TblUserInfo;
import com.lnjoying.justice.iam.domain.dto.response.project.ProjectsRsp;
import com.lnjoying.justice.iam.domain.dto.response.project.TblIamProjectDetail;
import com.lnjoying.justice.iam.domain.model.IamAssignment;
import com.lnjoying.justice.iam.domain.model.IamProject;
import com.lnjoying.justice.iam.utils.tree.Tree;
import com.lnjoying.justice.iam.utils.tree.TreeUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lnjoying.justice.iam.common.constant.ProjectConstants.DEFAULT_ADMIN_PROJECT;
import static com.lnjoying.justice.iam.domain.model.IamProject.IamProjectComparator.DEFAULT_COMPARATOR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2023/2/9 16:11
 */

@Repository
public class ProjectRepository
{
    @Autowired
    private TblIamProjectMapper projectMapper;

    @Autowired
    private TblIamAssignmentMapper iamAssignmentMapper;

    @Autowired
    private TblUserInfoMapper userInfoMapper;

    public List<TblIamProject> selectByNameAndBpId(String name, String bpId, Integer type)
    {
        return projectMapper.selectByNameAndBpId(name, bpId, type);
    }

    public int countByNameAndBpId(String name, String bpId)
    {
        Integer count = projectMapper.countByNameAndBpId(name, bpId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public int countByIdAndBpId(String id, String bpId)
    {
        Integer count = projectMapper.countByIdAndBpId(id, bpId);
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public TblIamProject selectByPrimaryKey(@Nullable String id)
    {
        //Assert.hasText(id, "The given project id must not be null!");
        return projectMapper.selectByPrimaryKey(id);
    }

    public int insertSelective(TblIamProject record)
    {
        return projectMapper.insertSelective(record);
    }

    public int updateProject(TblIamProject record)
    {
        return projectMapper.updateByPrimaryKeySelective(record);
    }

    public ProjectsRsp getProjectList(String name, String queryBpId, Integer status, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblIamProjectDetail> tblIamProjects = projectMapper.selectAll(name, status, queryBpId, false);
        PageInfo<TblIamProjectDetail> pageInfo = new PageInfo<>(tblIamProjects);
        return ProjectsRsp.builder().totalNum(pageInfo.getTotal())
                .projects(tblIamProjects.stream().map(IamProject::of).collect(Collectors.toList())).build();
    }


    public ProjectsRsp getProjectTree(String name, String queryBpId, Integer status, Integer pageSize, Integer pageNum)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblIamProjectDetail> tblIamProjects = projectMapper.selectAll(name, status, queryBpId, true);
        PageInfo<TblIamProjectDetail> pageInfo = new PageInfo<>(tblIamProjects);
        if (!CollectionUtils.isEmpty(tblIamProjects))
        {
            List<String> ids = tblIamProjects.stream().map(project -> project.getId()).collect(Collectors.toList());
            List<TblIamProjectDetail> tblIamProjectDetails = projectMapper.selectAllChildren(ids);
            List<IamProject> projects = tblIamProjectDetails.stream().map(IamProject::of).collect(Collectors.toList());
            List<Tree<IamProject>> trees = new TreeUtils<>(projects).setSort(DEFAULT_COMPARATOR).buildForest();
            return ProjectsRsp.builder().totalNum(pageInfo.getTotal())
                    .projects(trees).build();
        }

        return ProjectsRsp.builder().totalNum(0).projects(Collections.emptyList()).build();
    }

    public TblIamProjectDetail selectByIdAndBpId(String id, String bpId)
    {
        return projectMapper.selectByIdAndBpId(id, bpId);
    }


    public int deleteByPrimaryKey(String id)
    {
        return projectMapper.deleteByPrimaryKey(id);
    }

    public void batchInsertAssignments(List<TblIamAssignment> list)
    {
        if (!CollectionUtils.isEmpty(list))
        {
            iamAssignmentMapper.batchInsert(list);
        }
    }

    public void batchDeleteAssignments(List<TblIamAssignment> list)
    {
        if (!CollectionUtils.isEmpty(list))
        {
            list.stream().parallel().forEach(tblIamAssignment -> {
                iamAssignmentMapper.deleteByActorIdAndTargetIdAndType(tblIamAssignment.getActorId(), tblIamAssignment.getTargetId(), tblIamAssignment.getType());
            });
        }
    }

    public int countUsersInProject(String projectId)
    {
        Integer count = iamAssignmentMapper.countByTargetIdAndType(projectId, TblIamAssignment.AssignmentType.USER.value());
        return Objects.nonNull(count) ? count.intValue() : 0;
    }

    public List<IamAssignment.IamUserAssignment> selectUsersInProject(String projectId)
    {
        List<TblIamAssignment> iamAssignments = iamAssignmentMapper.selectByTargetIdAndType(projectId, TblIamAssignment.AssignmentType.USER.value());
        if (!CollectionUtils.isEmpty(iamAssignments))
        {
            List<IamAssignment.IamUserAssignment> iamUserAssignments = iamAssignments.stream().map(iamAssignment ->
            {
                IamAssignment.IamUserAssignment iamUserAssignment = new IamAssignment.IamUserAssignment();
                String userId = iamAssignment.getActorId();
                iamUserAssignment.setProjectId(iamAssignment.getTargetId());
                iamUserAssignment.setUserId(userId);
                TblUserInfo tblUserInfo = userInfoMapper.selectByPrimaryKey(userId);
                if (Objects.nonNull(tblUserInfo))
                {
                    iamUserAssignment.setUserName(tblUserInfo.getUserName());
                    iamUserAssignment.setEmail(tblUserInfo.getEmail());
                    iamUserAssignment.setCreateTime(tblUserInfo.getCreateTime());
                }

                return iamUserAssignment;
            }).collect(Collectors.toList());

            return iamUserAssignments;
        }

        return Collections.EMPTY_LIST;
    }

    public List<IamAssignment.IamGroupAssignment> selectGroupsInProject(String projectId)
    {
        List<TblIamAssignment> iamAssignments = iamAssignmentMapper.selectByTargetIdAndType(projectId, TblIamAssignment.AssignmentType.GROUP.value());
        if (!CollectionUtils.isEmpty(iamAssignments))
        {
            List<IamAssignment.IamGroupAssignment> iamGroupAssignments = iamAssignments.stream().map(iamAssignment ->
            {
                IamAssignment.IamGroupAssignment iamGroupAssignment = new IamAssignment.IamGroupAssignment();
                String groupId = iamAssignment.getActorId();
                iamGroupAssignment.setProjectId(iamAssignment.getTargetId());
                iamGroupAssignment.setGroupId(groupId);
                // todo set group name
                iamGroupAssignment.setGroupName("");
                return iamGroupAssignment;
            }).collect(Collectors.toList());

            return iamGroupAssignments;
        }

        return Collections.EMPTY_LIST;
    }

    public String selectLatestNameByName(String name)
    {
        return projectMapper.selectLatestNameByName(name);
    }

    public int batchInsert(List<TblIamProject> list)
    {
        return projectMapper.batchInsert(list);
    }

    public int insertDefaultProject(TblIamProject tblIamProject)
    {
        return projectMapper.insertSelective(tblIamProject);
    }

    public TblIamProject selectDefaultProject(String name, String userId)
    {
        return projectMapper.selectDefaultProject(name, userId, DEFAULT_ADMIN_PROJECT, TblIamProject.ProjectType.BP_DEFAULT.value());
    }

    public List<TblIamProject> selectByName(String name)
    {
        List<TblIamProject> tblIamProjects = projectMapper.selectByName(name);
        return tblIamProjects;
    }

    public TblIamProject selectAdminDefaultProject()
    {
        List<TblIamProject> tblIamProjects = projectMapper.selectByName(DEFAULT_ADMIN_PROJECT);
        if (!CollectionUtils.isEmpty(tblIamProjects))
        {
            return tblIamProjects.get(0);
        }

        return null;
    }

    public String selectAdminDefaultProjectId()
    {
        TblIamProject tblIamProject = selectAdminDefaultProject();
        return Optional.ofNullable(tblIamProject).map(project -> project.getId()).orElse("");
    }
}
