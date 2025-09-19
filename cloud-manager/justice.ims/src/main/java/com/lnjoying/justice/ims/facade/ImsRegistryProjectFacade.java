package com.lnjoying.justice.ims.facade;

import com.lnjoying.justice.ims.db.model.TblImsRegistryProject;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistryProjectReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryProjectsRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistryProject;

/**
 * ims  registry project facade
 *
 * @author merak
 **/


public interface ImsRegistryProjectFacade
{
    /**
     * add project
     *
     * @param req
     * @return
     */
    String addRegistryProject(AddRegistryProjectReq req);

    /**
     * query project list
     *
     * @param registryId
     * @param projectName
     * @param scope
     * @param userId
     * @param bpId
     * @param pageNum
     * @param pageSize
     * @param isAdmin
     * @param onlyOwner  True means that only the projects created by the user are displayed, excluding BP projects and other public projects
     * @return
     */
    RegistryProjectsRsp getProjectList(String registryId, String projectName, Integer scope, String userId, String bpId, Integer pageNum, Integer pageSize, boolean isAdmin, boolean onlyOwner);

    /**
     * insert data
     *
     * @param record
     * @return
     */
    void insertSelective(TblImsRegistryProject record);

    /**
     * update project
     *
     * @param req
     */
    void updateRegistryProject(UpdateRegistryProjectReq req);

    /**
     * delete project
     *
     * @param projectId
     * @param bpId
     * @param userId
     */
    void deleteRegistryProject(String projectId, String bpId, String userId);

    /**
     * get registry project info
     *
     * @param projectId
     * @param bpId
     * @return
     */
    ImsRegistryProject getRegistryProject(String projectId, String bpId, String operUserId);

    /**
     * Sync bp user to bp project
     */
    void processSyncRegistryUser2BpProject();
}
