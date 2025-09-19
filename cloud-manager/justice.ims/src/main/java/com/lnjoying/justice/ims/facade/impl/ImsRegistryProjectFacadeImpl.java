package com.lnjoying.justice.ims.facade.impl;

import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.db.model.TblImsRegistryUser;
import com.lnjoying.justice.ims.db.repo.ImsProjectRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryUserRepository;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryProjectsRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryProject;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistryProjectFacade;
import com.lnjoying.justice.ims.harbor.ApiClient;
import com.lnjoying.justice.ims.harbor.api.MemberApi;
import com.lnjoying.justice.ims.harbor.api.ProjectApi;
import com.lnjoying.justice.ims.harbor.api.UserApi;
import com.lnjoying.justice.ims.harbor.model.*;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.ims.util.HarborUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserAttributes;
import static com.lnjoying.justice.ims.db.model.TblImsRegistryProject.ProjectScope.*;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * Implementation class
 *
 * @author merak
 **/

@Service
@Slf4j
public class ImsRegistryProjectFacadeImpl implements ImsRegistryProjectFacade
{
    @Autowired
    private ImsProjectRepository imsProjectRepository;

    @Autowired
    private ImsRegistryRepository imsRegistryRepository;

    @Autowired
    private ImsRegistryUserRepository imsRegistryUserRepository;

    @Autowired
    private Executor timedExecutor;

    @Autowired
    private Executor executor;

    @Autowired
    CombRpcService combRpcService;

    public void insert(TblImsRegistryProject record)
    {
        imsProjectRepository.insert(record);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRegistryProject(AddRegistryProjectReq req)
    {
        String projectId = imsProjectRepository.insertRegistryProject(req);

        TblImsRegistry registry = imsRegistryRepository.getRegistry(req.getRegistryId(), null);
        // update harbor  createProjectOnHarbor
        TblImsRegistryProject.ProjectScope scope = fromValue(req.getScope());
        switch (scope)
        {
            case PRIVATE:
                createPrivateOrPublicProjectOnHarbor(req, registry, PRIVATE);
                break;
            case BP:
                createBpProjectOnHarbor(req, registry);
                break;
            case PUBLIC:
            default:
                createPrivateOrPublicProjectOnHarbor(req, registry, PUBLIC);
        }

        return projectId;
    }

    @Override
    public RegistryProjectsRsp getProjectList(String registryId, String projectName, Integer scope, String userId, String bpId, Integer pageNum, Integer pageSize, boolean isAdmin, boolean onlyOwner)
    {
        return imsProjectRepository.getProjectList(registryId, projectName, scope, userId, bpId, pageNum, pageSize, isAdmin, onlyOwner);
    }

    @Override
    public void insertSelective(TblImsRegistryProject record)
    {
        imsProjectRepository.insert(record);
    }

    @Override
    public void updateRegistryProject(UpdateRegistryProjectReq req)
    {
        Pair<String, String> userAttributes = getUserAttributes();
        TblImsRegistryProject project = getImsRegistryProject(req.getProjectId(), userAttributes.getLeft(), userAttributes.getRight());
        if (Objects.nonNull(project))
        {
            TblImsRegistryProject tblImsRegistryProject = new TblImsRegistryProject();
            BeanUtils.copyProperties(req, tblImsRegistryProject);
            tblImsRegistryProject.setUpdateTime(LocalDateTime.now());
            imsProjectRepository.update(tblImsRegistryProject);
        }
    }

    @Override
    public void deleteRegistryProject(String projectId, String bpId, String userId)
    {
        // Delete project in the registry
        deleteProjectOnHarbor(projectId, bpId, userId);

        imsProjectRepository.deletePhysical(projectId, bpId, userId);
    }

    @Override
    public ImsRegistryProject getRegistryProject(String projectId, String bpId, String operUserId)
    {
        TblImsRegistryProject tblImsRegistryProject = getImsRegistryProject(projectId, bpId, operUserId);
        ImsRegistryProject imsRegistryProject = new ImsRegistryProject();
        BeanUtils.copyProperties(tblImsRegistryProject, imsRegistryProject);

        TblImsRegistry registry = imsRegistryRepository.getRegistry(tblImsRegistryProject.getRegistryId(), null);
        imsRegistryProject.setRegistryName(registry.getRegistryName());
        return imsRegistryProject;
    }

    @Override
    public void processSyncRegistryUser2BpProject()
    {
        List<TblImsRegistryProject> tblImsRegistryProjects = imsProjectRepository.selectByScope(BP.value());
        if (CollectionUtils.isNotEmpty(tblImsRegistryProjects))
        {
           tblImsRegistryProjects.stream().forEach(registryProject ->
            {
                String registryId = registryProject.getRegistryId();
                TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
                String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
                ApiClient apiClient = new ApiClient(url, registry.getAdminName(), password);
                if (Objects.nonNull(registryProject.getBpId()))
                {
                    List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserRepository.selectByBpId(registryProject.getBpId());
                    if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
                    {
                        checkIfTheHarborUserExists(apiClient, tblImsRegistryUsers, true);
                        // User and project association
                        userAndProjectAssociation(registryProject.getProjectName(), apiClient, tblImsRegistryUsers, true);
                    }
                }
            });
        }
    }

    private void createPrivateOrPublicProjectOnHarbor(AddRegistryProjectReq req, TblImsRegistry registry, TblImsRegistryProject.ProjectScope projectScope)
    {
        ProjectApi projectApi = getProjectApi(registry);
        String projectName = req.getProjectName();
        // Check if the harbor project exists
        try
        {
            List<Project> projects = projectApi.listProjects(null, "name=" + projectName, null, null, null, null, null, null, null);

            if (CollectionUtils.isEmpty(projects))
            {
                // create harbor project
                ProjectReq projectReq = new ProjectReq();
                projectReq.setProjectName(projectName);
                projectReq.setPublic(projectScope.equals(PUBLIC) ? true : false);
                projectReq.setStorageLimit(-1L);
                projectApi.createProject(projectReq, null, null);
            }
            else
            {
                throw new ImsWebSystemException(THE_PROJECT_ALREADY_EXISTS_IN_THE_REGISTRY, INFO);
            }
        }
        catch (Exception e)
        {
            log.error("Failed to create project：{}", e);
            throw new ImsWebSystemException(FAILED_TO_CREATE_REGISTRY_PROJECT, INFO);
        }

        // check harbor user
        List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserRepository.selectByUserId(req.getUserId());
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            checkIfTheHarborUserExists(projectApi.getApiClient(), tblImsRegistryUsers, false);
            // User and project association
            userAndProjectAssociation(projectName, projectApi.getApiClient(), tblImsRegistryUsers, false);
        }
    }

    private void userAndProjectAssociation(String projectName, ApiClient apiClient, List<TblImsRegistryUser> tblImsRegistryUsers, boolean isTimedTask)
    {
        Executor  executor = isTimedTask ? this.timedExecutor : this.executor;
        MemberApi memberApi = new MemberApi(apiClient);
        List<CompletableFuture<Void>> futureList = tblImsRegistryUsers.stream().map(user -> CompletableFuture.runAsync(() ->
        {
            ProjectMember projectMember = new ProjectMember();
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(user.getRegistryUserName());
            projectMember.setMemberUser(userEntity);
            projectMember.setRoleId(RoleType.PROJECTADMIN.getValue());
            try
            {
                memberApi.createProjectMember(projectName, null, null, projectMember);
            }
            catch (Exception e)
            {
                log.error("Failed to associated users to the project {} : {}", projectName, e);
                throw new ImsWebSystemException(FAILED_TO_ASSOCIATE_PROJECT_AND_USER, INFO);
            }
        }, executor)).collect(Collectors.toList());
        long count = futureList.stream().map(CompletableFuture::join).count();
        log.info("Associated {} users to the project {}", count, projectName);
    }

    private void checkIfTheHarborUserExists(ApiClient apiClient, List<TblImsRegistryUser> tblImsRegistryUsers, boolean isTimedTask)
    {
        Executor  executor = isTimedTask ? this.timedExecutor : this.executor;
        List<CompletableFuture<Void>> futureList = tblImsRegistryUsers.stream().map(user ->
                CompletableFuture.runAsync(() ->
                {
                    String registryUserName = user.getRegistryUserName();
                    UserApi userApi = new UserApi(apiClient);
                    // Maximum number of queries 100
                    List<UserSearchRespItem> userSearchRespItems = userApi.searchUsers(registryUserName, null, 1L, 100L);
                    boolean userIsNotSynced = CollectionUtils.isEmpty(userSearchRespItems) ||
                            (CollectionUtils.isNotEmpty(userSearchRespItems) &&
                                    !userSearchRespItems.stream().map(item -> item.getUsername()).collect(Collectors.toList()).contains(registryUserName));
                    if (userIsNotSynced)
                    {
                        //The user has not been synchronized to the harbor
                        log.info("Create harbor user, userName:{}", registryUserName);
                        UserCreationReq userCreationReq = new UserCreationReq();
                        userCreationReq.setUsername(registryUserName);
                        userCreationReq.setPassword(AesCryptoUtils.cbcDecryptStr(user.getRegistryUserPassword()));
                        userCreationReq.setRealname(registryUserName);
                        //userCreationReq.setEmail(registryUserName + "@lnjoying.com");
                        HarborUserUtils.doSetEmail(combRpcService, registryUserName, userCreationReq, user.getUserId());
                        try
                        {
                            userApi.createUser(userCreationReq, null);
                        }
                        catch (Exception e)
                        {
                            log.error("Failed to sync user : {}", e);
                            throw new ImsWebSystemException(FAILED_TO_ASSOCIATE_PROJECT_AND_USER, INFO);
                        }
                    }
                }, executor)).collect(Collectors.toList());

        futureList.stream().map(CompletableFuture::join).count();
    }

    private void createBpProjectOnHarbor(AddRegistryProjectReq req, TblImsRegistry registry)
    {
        String projectName = req.getProjectName();
        ProjectApi projectApi = getProjectApi(registry);
        ProjectReq projectReq = new ProjectReq();
        projectReq.setProjectName(projectName);
        projectReq.setStorageLimit(-1L);
        projectReq.setPublic(false);
        projectApi.createProject(projectReq, null, null);
        // The bp id with admin rights is null by default
        List<TblImsRegistryUser> tblImsRegistryUsers = imsRegistryUserRepository.selectByBpId(req.getBpId());
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            checkIfTheHarborUserExists(projectApi.getApiClient(), tblImsRegistryUsers, false);
            userAndProjectAssociation(projectName, projectApi.getApiClient(), tblImsRegistryUsers, false);
        }
    }

    private void createPrivateProjectOnHarbor(AddRegistryProjectReq req, TblImsRegistry registry)
    {
        ProjectApi projectApi = getProjectApi(registry);
        ProjectReq projectReq = new ProjectReq();
        projectReq.setProjectName(req.getProjectName());
        projectReq.setStorageLimit(-1L);
        projectReq.setPublic(false);
    }

    private ProjectApi getProjectApi(TblImsRegistry registry)
    {
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        return new ProjectApi(url, registry.getAdminName(), password);
    }

    private TblImsRegistryProject getImsRegistryProject(String projectId, String bpId, String operUserId)
    {
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, operUserId);
        return registryProject;
    }


    private void deleteProjectOnHarbor(String projectId, String bpId, String userId)
    {

        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, userId);
        if (Objects.nonNull(registryProject))
        {
            String registryId = registryProject.getRegistryId();
            TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
            if (Objects.nonNull(registry))
            {
                ProjectApi projectApi = getProjectApi(registry);
                try {
                    projectApi.deleteProject(registryProject.getProjectName(), null, null);
                }
                catch (HttpClientErrorException e)
                {
                    if (e.getStatusCode().value() == HttpStatus.SC_PRECONDITION_FAILED)
                    {
                        log.error("There are associated resources in the project： {}", e);
                        throw new ImsWebSystemException(ASSOCIATED_RESOURCES_IN_THE_PROJECT, ERROR);
                    }

                    throw  e;
                }
            }
        }
    }
}
