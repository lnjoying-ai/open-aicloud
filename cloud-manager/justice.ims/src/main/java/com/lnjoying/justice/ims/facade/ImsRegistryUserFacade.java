package com.lnjoying.justice.ims.facade;

import com.lnjoying.justice.ims.db.model.TblImsRegistryUser;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryUserReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryUsersRsp;
import org.apache.commons.lang3.tuple.Pair;

/**
 * ims  registry user facade
 *
 * @author merak
 **/

public interface ImsRegistryUserFacade
{
    /**
     * add registry user to db
     *
     * @param req
     * @return rsp
     */
    void addRegistryUser(AddRegistryUserReq req);

    /**
     * get user list
     *
     * @param registryUserName
     * @param bpId
     * @param userId
     * @param pageSize
     * @param pageNum
     * @return
     */
    RegistryUsersRsp getRegistryUserList(String registryUserName, String bpId, String userId, Integer pageSize, Integer pageNum);

    /**
     * Synchronize users to other registry
     */
    void processSyncRegistryUser();

    /**
     * Get the registry user corresponding to the user,
     * and one user corresponds to one registry user
     *
     * @param userId
     * @return
     */
    TblImsRegistryUser selectByUserId(String userId);

    /**
     * Check whether the harbor user exists
     *
     * @param userId
     */
    Boolean exist(String userId);

    /**
     * update registry user
     *
     * @param registryUserReq
     */
    void updateRegistryUser(AddRegistryUserReq registryUserReq);

    TblImsRegistryUser selectByUserName(String userName);
}
