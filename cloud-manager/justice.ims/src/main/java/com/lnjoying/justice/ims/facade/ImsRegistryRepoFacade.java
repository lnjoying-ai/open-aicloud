package com.lnjoying.justice.ims.facade;

import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistryRepoReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryRepoBuildHistoryRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryRepoTagsRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryReposRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryRepo;
import com.lnjoying.justice.ims.harbor.ApiClient;

/**
 * ims  registry repo facade
 *
 * @author merak
 **/

public interface ImsRegistryRepoFacade
{
    /**
     * update repo
     *
     * @param req
     */
    void updateRegistryRepo(UpdateRegistryRepoReq req);

    /**
     * query repo list
     *
     * @param registryId
     * @param projectId
     * @param repoName
     * @param pageNum
     * @param pageSiz
     * @param bpId
     * @param userId
     * @return
     */
    RegistryReposRsp getRepoList(String registryId, String projectId, String repoName, Integer pageNum, Integer pageSiz, String bpId, String userId);

    /**
     * get registry repo info
     *
     * @param registryId
     * @param projectId
     * @param repoName
     * @param bpId
     * @param userId
     * @return
     */
    ImsRegistryRepo getRegistryRepo(String registryId, String projectId, String repoName, String bpId, String userId);

    /**
     * get registry repo build history
     *
     * @param registryId
     * @param projectId
     * @param repoName
     * @param bpId
     * @param userId
     */
    RegistryRepoBuildHistoryRsp buildHistory(String registryId, String projectId, String repoName, String bpId, String userId);

    /**
     * get registry repo tags
     *
     * @param registryId
     * @param projectId
     * @param repoName
     * @param bpId
     * @param userId
     * @return
     */
    RegistryRepoTagsRsp tags(String registryId, String projectId, String repoName, String bpId, String userId);

    /**
     * delete repo tag
     *
     * @param registryId
     * @param projectId
     * @param repoName
     * @param tagName
     * @param bpId
     * @param userId
     */
    void deleteTag(String registryId, String projectId, String repoName, String tagName, String bpId, String userId);

    /**
     * build repo
     * @param repo
     * @param apiClient
     * @param projectName
     * @return
     */
    ImsRegistryRepo buildRepo(ImsRegistryRepo repo, ApiClient apiClient, String projectName);
}
