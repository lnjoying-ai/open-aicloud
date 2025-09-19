package com.lnjoying.justice.ims.facade.impl;

import com.google.common.collect.Lists;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.db.repo.ImsProjectRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryRepository;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistryRepoReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryRepoBuildHistoryRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryRepoTagsRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryReposRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryRepo;
import com.lnjoying.justice.ims.domain.model.ImsRegistryRepoTag;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistryRepoFacade;
import com.lnjoying.justice.ims.harbor.ApiClient;
import com.lnjoying.justice.ims.harbor.api.ArtifactApi;
import com.lnjoying.justice.ims.harbor.api.LabelApi;
import com.lnjoying.justice.ims.harbor.api.ProjectApi;
import com.lnjoying.justice.ims.harbor.api.RepositoryApi;
import com.lnjoying.justice.ims.harbor.model.*;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.ims.util.UrlEncoderDecoderUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * Implementation class
 *
 * @author merak
 **/

@Service
@Slf4j
public class ImsRegistryRepoFacadeImpl implements ImsRegistryRepoFacade
{
    @Autowired
    private ImsRegistryRepository imsRegistryRepository;

    @Autowired
    private ImsProjectRepository imsProjectRepository;

    @Autowired
    private Executor executor;

    @Override
    public RegistryReposRsp getRepoList(String registryId, String projectId, String repoName, Integer pageNum, Integer pageSize, String bpId, String userId)
    {
        TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, userId);
        String projectName = registryProject.getProjectName();
        RepositoryApi repositoryApi = getRepositoryApi(registry);

        String q = null;
        if (StringUtils.isNotBlank(repoName))
        {
            // repoName fuzzy match
            q = "name=~" + repoName;
        }

        RegistryReposRsp rsp = RegistryReposRsp.builder().repos(Lists.newArrayList()).build();
        try
        {
            List<Repository> repositories = repositoryApi.listRepositories(projectName, null, q, null, pageNum.longValue(), pageSize.longValue());
            if (CollectionUtils.isNotEmpty(repositories))
            {
                rsp.setTotalNum(repositories.size());
                List<ImsRegistryRepo> collect = repositories.stream().map(repository -> ImsRegistryRepo.of(repository, registryProject)).collect(Collectors.toList());
                List<CompletableFuture<ImsRegistryRepo>> futureList = collect.stream().map(repo -> CompletableFuture.supplyAsync(() ->
                {
                    ImsRegistryRepo imsRegistryRepo = buildRepo(repo, repositoryApi.getApiClient(), projectName);
                    return imsRegistryRepo;
                }, executor)).collect(Collectors.toList());

                List<ImsRegistryRepo> imsRegistryRepos = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
                rsp.setRepos(imsRegistryRepos);
            }
        }
        catch (Exception e)
        {
            log.error(GET_REPO_LIST_ERROR.getMessage(), e);
            throw new ImsWebSystemException(GET_REPO_LIST_ERROR, ErrorLevel.INFO);
        }
        return rsp;
    }

    @Override
    public void updateRegistryRepo(UpdateRegistryRepoReq req)
    {
        TblImsRegistry registry = imsRegistryRepository.getRegistry(req.getRegistryId(), null);
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(req.getProjectId(), req.getBpId(), req.getUserId());
        checkPermission(registryProject, req.getBpId(), req.getUserId());
        String projectName = registryProject.getProjectName();
        String repoName = req.getRepoName();

        RepositoryApi repositoryApi = getRepositoryApi(registry);
        // Check if the repo exists
        try
        {
            Repository repository = repositoryApi.getRepository(projectName, repoName, null);
        }
        catch (Exception e)
        {
            log.error("repo does not exist ：{}", e);
            throw new ImsWebSystemException(REGISTRY_REPO_NOT_EXIST, ErrorLevel.INFO);
        }

        Repository repository = new Repository();
        repository.setDescription(req.getDescription());
        try
        {
            repositoryApi.updateRepository(projectName, req.getRepoName(), repository, null);
        }
        catch (Exception e)
        {
            log.error("failed to modify registry repo description ：{}", e);
            throw new ImsWebSystemException(MODIFY_REGISTRY_REPO_FAILED, ErrorLevel.INFO);
        }

        addLabels(req, registry, projectName, repoName, repositoryApi);
    }

    @Override
    public ImsRegistryRepo getRegistryRepo(String registryId, String projectId, String repoName, String bpId, String userId)
    {
        ImsRegistryRepo res = new ImsRegistryRepo();

        TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, userId);
        String projectName = registryProject.getProjectName();

        RepositoryApi repositoryApi = getRepositoryApi(registry);
        try
        {
            Repository repository = repositoryApi.getRepository(projectName, repoName, null);
            res = ImsRegistryRepo.of(repository, registryProject);
        }
        catch (Exception e)
        {
            log.error("repo does not exist ：{}", e);
            throw new ImsWebSystemException(REGISTRY_REPO_NOT_EXIST, ErrorLevel.INFO);
        }

        buildRepo(res, repositoryApi.getApiClient(), projectName);
        return res;
    }

    @Override
    public RegistryRepoBuildHistoryRsp buildHistory(String registryId, String projectId, String repoName, String bpId, String userId)
    {
        String buildHistory = "[]";

        TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, userId);
        String projectName = registryProject.getProjectName();
        ArtifactApi artifactApi = getArtifactApi(registry);
        List<Artifact> artifacts = artifactApi.listArtifacts(projectName, repoName, null, null, null, null, null, null, false, true, false, false, false);
        if (CollectionUtils.isNotEmpty(artifacts))
        {
            Artifact artifact = artifacts.get(0);
            buildHistory = artifactApi.getAddition(projectName, repoName, artifact.getDigest(), "build_history", null);
        }

        return RegistryRepoBuildHistoryRsp.builder().buildHistory(buildHistory).build();
    }

    @Override
    public RegistryRepoTagsRsp tags(String registryId, String projectId, String repoName, String bpId, String userId)
    {
        TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, userId);
        String projectName = registryProject.getProjectName();
        ArtifactApi artifactApi = getArtifactApi(registry);
        List<Artifact> artifacts = artifactApi.listArtifacts(projectName, repoName, null, null, null, null, null, null, true, false, false, false, false);
        if (CollectionUtils.isNotEmpty(artifacts))
        {
            List allTags = new ArrayList<>();

            artifacts.forEach(artifact -> {
                List<Tag> tags = artifact.getTags();
                if (CollectionUtils.isNotEmpty(tags))
                {
                    List<ImsRegistryRepoTag> list = tags.stream().map(tag ->
                    {
                        String tagName = tag.getName();
                        String pullCommand = "docker pull " + registry.getUrl() + "/" + projectName + "/" + UrlEncoderDecoderUtils.decodeUtf8(repoName) + ":" + tagName;
                        return ImsRegistryRepoTag.of(tag, pullCommand, artifact.getDigest());
                    }).collect(Collectors.toList());
                    allTags.addAll(list);

                }
            });

            return RegistryRepoTagsRsp.builder().totalNum(allTags.size()).tags(allTags).build();
        }

        return RegistryRepoTagsRsp.builder().build();
    }

    @Override
    public void deleteTag(String registryId, String projectId, String repoName, String tagName, String bpId, String userId)
    {
        TblImsRegistry registry = imsRegistryRepository.getRegistry(registryId, null);
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, bpId, userId);
        checkPermission(registryProject, bpId, userId);
        String projectName = registryProject.getProjectName();
        ArtifactApi artifactApi = getArtifactApi(registry);
        List<Artifact> artifacts = artifactApi.listArtifacts(projectName, repoName, null, null, null, null, null, null, true, false, false, false, false);
        if (CollectionUtils.isNotEmpty(artifacts))
        {
            artifacts.forEach(artifact -> {
                try
                {
                    List<Tag> tags = artifact.getTags();
                    if (CollectionUtils.isNotEmpty(tags))
                    {
                        artifactApi.deleteTag(projectName, repoName, artifact.getDigest(), tagName, null);
                        if (tags.size() == 1)
                        {
                            // delete repo
                            artifactApi.deleteArtifact(projectName, repoName, artifact.getDigest(), null);
                            RepositoryApi repositoryApi = new RepositoryApi(artifactApi.getApiClient());
                            repositoryApi.deleteRepository(projectName, repoName, null);
                        }
                    }
                }
                catch (HttpClientErrorException e)
                {
                    if (e.getStatusCode().value() == HttpStatus.SC_NOT_FOUND)
                    {
                        log.error("registry repo tag not found： {}", e);
                        throw new ImsWebSystemException(REGISTRY_REPO_TAG_NOT_EXIST, ErrorLevel.INFO);
                    }
                }
            });

        }
    }

    /**
     * Fill in other attributes
     *
     * @param repo
     * @return
     */
    @Override
    public ImsRegistryRepo buildRepo(ImsRegistryRepo repo, ApiClient apiClient, String projectName)
    {
        ArtifactApi artifactApi = new ArtifactApi(apiClient);
        String repoName = UrlEncoderDecoderUtils.encodeUtf8(repo.getRepoName());
        List<Artifact> artifacts = artifactApi.listArtifacts(projectName, repoName, null, null, null, null, null, null, false, true, false, false, false);
        if (CollectionUtils.isNotEmpty(artifacts))
        {
            Artifact artifact = artifacts.get(0);
            repo.setDigest(artifact.getDigest());
            List<Label> list = artifact.getLabels();
            if (CollectionUtils.isNotEmpty(list))
            {
                List<String> labels = list.stream().map(label -> label.getName()).collect(Collectors.toList());
                repo.setLabels(labels);
            }
            else
            {
                repo.setLabels(Lists.newArrayList());
            }
            repo.setSize(artifact.getSize());
        }
        else
        {
            repo.setLabels(Lists.newArrayList());
        }

        return repo;
    }

    private void addLabels(UpdateRegistryRepoReq req, TblImsRegistry registry, String projectName, String repoName, RepositoryApi repositoryApi)
    {
        // get registry project id Used to add label
        ProjectApi projectApi = new ProjectApi(repositoryApi.getApiClient());
        Project project = projectApi.getProject(projectName, null, true);

        ArtifactApi artifactApi = getArtifactApi(registry);
        List<Artifact> artifacts = artifactApi.listArtifacts(projectName, repoName, null, null, null, null, null, null, false, false, false, false, false);
        if (CollectionUtils.isNotEmpty(artifacts))
        {
            List<String> labels = req.getLabels();
            if (CollectionUtils.isNotEmpty(labels))
            {
                artifacts.forEach(artifact -> {
                    LabelApi labelApi = getLabelApi(registry);
                    List<CompletableFuture<Void>> futureList = labels.stream().map(strLabel ->
                            CompletableFuture.runAsync(() ->
                            {
                                Label label = new Label();
                                label.setName(strLabel);
                                label.setScope("p");
                                label.setColor("#0065AB");
                                label.setProjectId(project.getProjectId().longValue());
                                try
                                {
                                    labelApi.createLabel(label, null);
                                }
                                catch (HttpClientErrorException e)
                                {
                                    if (e.getStatusCode().value() == HttpStatus.SC_CONFLICT)
                                    {
                                        // do nothing
                                    }
                                    else
                                    {
                                        log.error("failed to create label： {}", e);
                                        throw new ImsWebSystemException(REGISTRY_REQUEST_HTTP_CLIENT_ERROR, ErrorLevel.INFO);
                                    }
                                }

                                // get label id
                                List<Label> labelCollection = labelApi.listLabels(null, "name=" + strLabel, null, 1L, 1L,
                                        null, "p", project.getProjectId().longValue());
                                if (CollectionUtils.isNotEmpty(labelCollection) && labelCollection.size() == 1)
                                {
                                    Label l = new Label();
                                    l.setId(labelCollection.get(0).getId());
                                    try
                                    {
                                        artifactApi.addLabel(projectName, repoName, artifact.getDigest(), l, null);
                                    }
                                    catch (HttpClientErrorException e)
                                    {
                                        if (e.getStatusCode().value() == HttpStatus.SC_CONFLICT)
                                        {
                                            // do nothing
                                        }
                                        else
                                        {
                                            log.error("failed to add label to repo : {}", e);
                                            throw new ImsWebSystemException(REGISTRY_REQUEST_HTTP_CLIENT_ERROR, ErrorLevel.INFO);
                                        }
                                    }
                                }
                            }, executor)).collect(Collectors.toList());
                    futureList.stream().map(CompletableFuture::join).count();
                });

            }
        }
    }


    private ProjectApi getProjectApi(TblImsRegistry registry)
    {
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        return new ProjectApi(url, registry.getAdminName(), password);
    }

    private RepositoryApi getRepositoryApi(TblImsRegistry registry)
    {
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        return new RepositoryApi(url, registry.getAdminName(), password);
    }

    private ArtifactApi getArtifactApi(TblImsRegistry registry)
    {
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        return new ArtifactApi(url, registry.getAdminName(), password);
    }

    private LabelApi getLabelApi(TblImsRegistry registry)
    {
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        return new LabelApi(url, registry.getAdminName(), password);
    }

    private void checkPermission(TblImsRegistryProject registryProject, String bpId, String userId)
    {
        if (!isAdmin())
        {
            if (Objects.nonNull(bpId))
            {
                if (!bpId.equals(registryProject.getBpId()))
                {
                    throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
                }

            }

            if (Objects.nonNull(userId))
            {
                    if (!userId.equals(registryProject.getUserId()))
                    {
                        throw new ImsWebSystemException(ErrorCode.User_Not_Grant, INFO);
                    }
            }
        }
    }
}
