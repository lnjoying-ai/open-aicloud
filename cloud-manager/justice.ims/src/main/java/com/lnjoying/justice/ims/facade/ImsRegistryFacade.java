package com.lnjoying.justice.ims.facade;

import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.domain.dto.req.*;
import com.lnjoying.justice.ims.domain.dto.rsp.*;
import com.lnjoying.justice.ims.domain.model.ImsPreDownload;
import com.lnjoying.justice.ims.domain.model.ImsRegistry;
import com.lnjoying.justice.ims.domain.model.RegistryRegion;
import com.lnjoying.justice.schema.msg.EeCommonDef;
import com.lnjoying.justice.schema.msg.EeImageDef;

import javax.validation.constraints.Max;
import java.util.Collection;
import java.util.List;

/**
 * ims  registry facade
 *
 * @author merak
 **/

public interface ImsRegistryFacade
{
    /**
     * add RegionRegistry to db
     *
     * @param req
     * @return rsp
     */
    RegistryInstallInfoRsp addRegistry(AddRegistryReq req);

    /**
     * return list
     *
     * @param name
     * @param regionId
     * @param regionName
     * @param userId
     * @param status
     * @param pageSize
     * @param pageNum
     * @return
     */
    RegistriesRsp getRegistryList(String name, String regionId, String regionName, String userId,
                                  Collection<Integer> status, Integer pageSize, Integer pageNum);

    /**
     * get registry info
     *
     * @param registryId
     * @param operUserId
     * @return
     */
    TblImsRegistry getImsRegistry(String registryId, String operUserId);

    /**
     * get registry info
     *
     * @param registryId
     * @param operUserId
     * @return
     */
    ImsRegistry getRegistry(String registryId, String operUserId);

    /**
     * update
     *
     * @param registryId
     * @param req
     */
    void updateRegistry(String registryId, UpdateRegistryReq req);

    /**
     * delete registry info
     *
     * @param registryId
     * @param operUserId
     * @param operBpId
     */
    void deleteRegistry(String registryId, String operUserId, String operBpId);

    /**
     * change registry Password
     *
     * @param req
     */
    void changePassword(RegistryPasswordReq req);

    /**
     * Set the status of the registry
     *
     * @param registryId
     * @param userId
     * @param bpId
     * @param action
     */
    void action(String registryId, String userId, String bpId, String action);

    /**
     * Scan registry status
     */
    void processPingRegistry();

    /**
     * Check whether the registry is authenticated
     */
    void processAuthenticateRegistry();

    /**
     * Check whether the registry components are healthy
     */
    void processEnableRegistry();

    /**
     * Get all the images in the registry
     *
     * @param registryId
     * @return
     */
    RegistryImagesRsp getAllRegistryRepos(String registryId);

    /**
     * Get all the nodes in the registry region
     *
     * @param registryId
     * @return
     */
    RegistryNodesRsp getAllRegistryNodes(String registryId);

    /**
     * Send repos to the corresponding node
     *
     * @param req
     */
    void preDownload(PreDownloadReq req);

    /**
     * process
     *
     * @param imsPreDownload
     */
    void processPreDownload(ImsPreDownload imsPreDownload);

    /**
     * process image pull rsp
     *
     * @param imagePullRspBody
     * @param head
     */
    void processImagePullRsp(EeImageDef.msg_image_pull_rsp_body imagePullRspBody, EeCommonDef.msg_header head);

    /**
     * process image oper rpt req
     *
     * @param imageOperRptReqBody
     * @param route
     * @param head
     */
    void processImageOperRptReq(EeImageDef.msg_image_oper_rpt_req_body imageOperRptReqBody, EeCommonDef.msg_route route, EeCommonDef.msg_header head);

    /**
     * Get command
     *
     * @param req
     * @return command
     */
    String command(TenantCommandReq req);

    /**
     * Fuzzy search for all registries
     *
     * @param repoName
     * @param pageNum
     * @param pageSize
     * @param bpId
     * @param userId
     * @return
     */
    RegistryReposRsp searchRepos(String repoName, Integer pageNum, Integer pageSize, String bpId, String userId);

    /**
     * get Repos by project id
     * @param registryId
     * @param projectId
     * @return
     */
    RegistryImagesRsp getAllRegistryReposByProjectId(String registryId, String projectId);

    /**
     * Get regions of all registries mappings
     * @return
     */
    List<RegistryRegion>  getRegistryRegions();

    /**
     * get preDownloads infos
     * @return
     * @param registryId
     * @param imageName
     * @param nodeId
     * @param queryBpId
     * @param queryUserId
     * @param pageNum
     * @param pageSize
     */
    ImageDownloadInfoRsp getPreDownloads(String registryId, String imageName, String nodeId, String queryBpId, String queryUserId, Integer pageNum, @Max(100) Integer pageSize);

    void deletePreDownloads(String jobIds);

    void deletePreDownloadsJobs(String ids);

    TblImsRegistry selectRegistryByUrl(String url);

    String getRegistryUrlById(String registryId);
}
