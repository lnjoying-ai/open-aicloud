package com.lnjoying.justice.ims.facade;

import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistry3rdReq;
import com.lnjoying.justice.ims.domain.dto.req.PreDownloadReq;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistry3rdReq;
import com.lnjoying.justice.ims.domain.dto.rsp.Registries3rdRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.Registry3rdNodesRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryNodesRsp;
import com.lnjoying.justice.ims.domain.model.ImsRegistry3rd;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;

/**
 * ims second party registry facade
 *
 * @author merak
 **/

public interface ImsRegistry3rdFacade
{
    /**
     * add registry
     *
     * @param req
     * @return
     * @throws ImsWebSystemException
     */
    String addRegistry3rd(AddRegistry3rdReq req) throws ImsWebSystemException;

    /**
     * get third party registries
     *
     * @param registryName
     * @param bpId
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    Registries3rdRsp getRegistry3rdList(String registryName, String bpId, String userId, Integer pageNum, Integer pageSize);

    /**
     * get registry info
     *
     * @param registryId
     * @param bpId
     * @param userId
     * @return
     */
    ImsRegistry3rd getRegistry3rd(String registryId, String bpId, String userId);

    /**
     * update
     *
     * @param req
     */
    void updateRegistry3rd(UpdateRegistry3rdReq req);

    /**
     * delete
     *
     * @param registryId
     * @param userId
     * @param bpId
     * @param userName
     * @param bpName
     */
    void deleteRegistry3rd(String registryId, String userId, String bpId, String userName, String bpName);

    /**
     * Send repos to the corresponding node
     * @param req
     */
    void preDownload(PreDownloadReq req);

    /**
     * query all nodes by regionId
     * @param regionId
     * @return
     */
    Registry3rdNodesRsp getAllRegistryNodes(String regionId);

    TblImsRegistry3rd selectByUrlAndAccessKey(String registryUrl, String registryName);

    String getRegistryUrlById(String registryId);
}
