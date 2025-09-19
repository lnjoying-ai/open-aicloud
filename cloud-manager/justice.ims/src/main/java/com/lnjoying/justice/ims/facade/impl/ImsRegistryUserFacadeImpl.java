package com.lnjoying.justice.ims.facade.impl;

import com.google.common.collect.Lists;
import com.lnjoying.justice.ims.db.mapper.TblImsRegistryUserMapper;
import com.lnjoying.justice.ims.db.model.TblImsRegistry;
import com.lnjoying.justice.ims.db.model.TblImsRegistryUser;
import com.lnjoying.justice.ims.db.repo.ImsRegistryRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryUserRepository;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistryUserReq;
import com.lnjoying.justice.ims.domain.dto.rsp.RegistryUsersRsp;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistryUserFacade;
import com.lnjoying.justice.ims.harbor.ApiClient;
import com.lnjoying.justice.ims.harbor.api.UserApi;
import com.lnjoying.justice.ims.harbor.model.PasswordReq;
import com.lnjoying.justice.ims.harbor.model.UserCreationReq;
import com.lnjoying.justice.ims.harbor.model.UserResp;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.ims.util.HarborUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
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

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserId;
import static com.lnjoying.justice.ims.db.model.TblImsRegistry.RegistryStatus.*;
import static com.lnjoying.justice.ims.db.model.TblImsRegistryUser.RegistryUserStatus.*;
import static com.lnjoying.justice.ims.db.model.TblImsRegistryUser.RegistryUserStatus.CREATING;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * Implementation class
 *
 * @author merak
 **/

@Service
@Slf4j
public class ImsRegistryUserFacadeImpl implements ImsRegistryUserFacade
{
    @Autowired
    private TblImsRegistryUserMapper imsRegistryUserMapper;

    @Autowired
    private ImsRegistryUserRepository registryUserRepository;

    @Autowired
    private ImsRegistryRepository registryRepository;

    @Autowired
    private Executor timedExecutor;

    @Autowired
    CombRpcService combRpcService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRegistryUser(AddRegistryUserReq req)
    {
        registryUserRepository.insertRegistryUser(req);

        // Randomly select a registry, create a user,
        // and verify whether the operation created by the user is successful or not
        // Other synchronization through timed tasks
        TblImsRegistry imsRegistry = registryRepository.selectOne();

        // update harbor
        createHarborUser(req.getRegistryUserName(), req.getRegistryPassword(), imsRegistry, req.getUserId());

        // update record status
        TblImsRegistryUser imsRegistryUser = registryUserRepository.selectByRegistryUser(req.getRegistryUserName());
        TblImsRegistryUser update = TblImsRegistryUser.builder().registryUserId(imsRegistryUser.getRegistryUserId())
                .status(PARTIALLY_COMPLETED.value())
                .updateTime(LocalDateTime.now()).build();
        registryUserRepository.updateByPrimaryKeySelective(update);
    }

    @Override
    public void updateRegistryUser(AddRegistryUserReq req)
    {
        TblImsRegistryUser imsRegistryUser = registryUserRepository.selectByUserId(req.getUserId()).get(0);
        req.setRegistryUserName(imsRegistryUser.getRegistryUserName());
        TblImsRegistryUser update = TblImsRegistryUser.builder().registryUserId(imsRegistryUser.getRegistryUserId())
                .registryUserPassword(AesCryptoUtils.cbcEncryptHex(req.getRegistryPassword()))
                .registryUserOldPassword(imsRegistryUser.getRegistryUserPassword())
                .userId(req.getUserId()).userName(req.getUserName()).bpId(req.getBpId()).bpName(req.getBpName())
                .status(PARTIALLY_COMPLETED.value())
                .updateTime(LocalDateTime.now()).build();
        // Use transaction to ensure successful password update，Save the original password as the old password
        registryUserRepository.updateByPrimaryKeySelective(update);

        // Batch modify the passwords of all registries user
        List<Integer> statusList = Lists.newArrayList(ENABLE.value());
        List<TblImsRegistry> tblImsRegistries = registryRepository.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistries))
        {
            List<CompletableFuture<Integer>> futureList = tblImsRegistries.stream().map(registry -> CompletableFuture.runAsync(() ->
            {
                if (isTheHarborUserExist(req.getRegistryUserName(), registry))
                {
                    updateHarborUser(req.getRegistryUserName(), req.getRegistryPassword(), imsRegistryUser.getRegistryUserPassword(), registry);
                }
                else
                {
                    createHarborUser(req.getRegistryUserName(), req.getRegistryPassword(), registry, req.getUserId());
                }
            }, timedExecutor).handle((res, ex) ->
            {
                if (Objects.nonNull(ex))
                {
                    log.error(" modify the passwords of registry:{} user error", registry.getUrl(), ex);
                    return 0;
                }
                return 1;
            })).collect(Collectors.toList());

            int sum = futureList.stream().map(CompletableFuture::join).mapToInt(Integer::intValue).sum();
            if (sum != tblImsRegistries.size())
            {
                log.error(" Batch modify the passwords of all registries user error, need {}, actual {}", tblImsRegistries.size(), sum);
            }
            if (sum == 0)
            {
                throw new ImsWebSystemException(UPDATE_REGISTRY_USER_PASSWORD_FAIL, INFO);
            }
        }

    }

    @Override
    public RegistryUsersRsp getRegistryUserList(String registryUserName, String bpId, String userId, Integer pageSize, Integer pageNum)
    {
        return registryUserRepository.getRegistryUserList(registryUserName, bpId, userId, pageSize, pageNum);
    }

    @Override
    public void processSyncRegistryUser()
    {
        // Get all users and add them to all registries in turn
        List<Integer> statusList = Lists.newArrayList(PARTIALLY_COMPLETED.value(), CREATING.value());
        List<TblImsRegistryUser> tblImsRegistryUsers = registryUserRepository.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            List<TblImsRegistry> tblImsRegistries = registryRepository.selectAll();
            if (CollectionUtils.isNotEmpty(tblImsRegistries))
            {
                tblImsRegistryUsers.stream().forEach(tblImsRegistryUser ->
                {
                    List<CompletableFuture<Integer>> registryFutureList = tblImsRegistries.stream().map(registry -> CompletableFuture.supplyAsync(() ->
                    {
                        if (registry.getStatus() != ENABLE.value())
                        {
                            return 0;
                        }
                        String userName = tblImsRegistryUser.getRegistryUserName();
                        String password = tblImsRegistryUser.getRegistryUserPassword();
                        String oldPassword = tblImsRegistryUser.getRegistryUserOldPassword();
                        password = AesCryptoUtils.cbcDecryptStr(password);
                        if (StringUtils.isNotBlank(oldPassword))
                        {
                            oldPassword = AesCryptoUtils.cbcDecryptStr(oldPassword);
                        }
                        boolean isExist = isTheHarborUserExist(userName, registry);
                        if (!isExist)
                        {
                            createHarborUser(userName, password, registry, null);
                        }
                        else
                        {
                            if (StringUtils.isNotBlank(tblImsRegistryUser.getRegistryUserOldPassword()))
                            {
                                try
                                {
                                    //Try to obtain user information with new password
                                    String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                                    UserApi userApi = new UserApi(url, userName, password);
                                    userApi.getCurrentUserInfo(null);
                                }
                                catch (HttpClientErrorException e)
                                {
                                    log.error("processSyncRegistryUser  HttpClientErrorException:{}", e);
                                    if (e.getStatusCode().value() == HttpStatus.SC_UNAUTHORIZED)
                                    {
                                        // Try to obtain user information with old password
                                        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                                        UserApi userApi = new UserApi(url, userName, oldPassword);
                                        UserResp currentUserInfo = userApi.getCurrentUserInfo(null);
                                        if (Objects.nonNull(currentUserInfo))
                                        {
                                            // Change old password to new password
                                            updateHarborUser(userName, password, oldPassword, registry);
                                        }
                                    }
                                    return 0;
                                }
                                catch (Exception e)
                                {
                                    log.error("processSyncRegistryUser failed:{}", e);
                                    return 0;
                                }

                            }
                        }
                        return 1;
                    }, timedExecutor).exceptionally(e -> 0)).collect(Collectors.toList());
                    long count = registryFutureList.stream().map(CompletableFuture::join).mapToInt(Integer::intValue).sum();

                    if (tblImsRegistries.size() == count)
                    {
                        tblImsRegistryUser.setStatus(COMPLETED.value());
                        // Passwords have been synchronized in all registry, delete old passwords to reduce repeated judgments
                        tblImsRegistryUser.setRegistryUserOldPassword(null);
                    }
                    else
                    {
                        tblImsRegistryUser.setStatus(PARTIALLY_COMPLETED.value());
                    }
                    tblImsRegistryUser.setUpdateTime(LocalDateTime.now());
                    registryUserRepository.updateByPrimaryKey(tblImsRegistryUser);
                });
            }
        }
    }

    @Override
    public TblImsRegistryUser selectByUserId(String userId)
    {
        List<TblImsRegistryUser> tblImsRegistryUsers = registryUserRepository.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            return tblImsRegistryUsers.get(0);
        }
        return null;
    }

    @Override
    public Boolean exist(String userId)
    {
        List<TblImsRegistryUser> tblImsRegistryUsers = registryUserRepository.selectByUserId(userId);
        if (CollectionUtils.isEmpty(tblImsRegistryUsers))
        {
            return false;
        }
        return true;
    }

    @Override
    public TblImsRegistryUser selectByUserName(String userName)
    {
        List<TblImsRegistryUser> tblImsRegistryUsers = registryUserRepository.selectByUserName(userName);
        if (CollectionUtils.isNotEmpty(tblImsRegistryUsers))
        {
            return tblImsRegistryUsers.get(0);
        }
        return null;
    }

    private boolean isTheHarborUserExist(String userName, TblImsRegistry imsRegistry)
    {
        String url = ApiClient.buildHTTPSRequestUrl(imsRegistry.getUrl());
        String adminPasswd = AesCryptoUtils.cbcDecryptStr(imsRegistry.getAdminPasswd());
        UserApi userApi = new UserApi(url, imsRegistry.getAdminName(), adminPasswd);
        List<UserResp> userResps = userApi.listUsers(null, "username=" + userName, null, 1L, 1L);
        if (CollectionUtils.isNotEmpty(userResps) && userResps.size() == 1)
        {
            return true;
        }
        return false;
    }

    private void createHarborUser(String userName, String password, TblImsRegistry imsRegistry, String currentUserId) throws ImsWebSystemException
    {
        String url = ApiClient.buildHTTPSRequestUrl(imsRegistry.getUrl());
        String adminPasswd = AesCryptoUtils.cbcDecryptStr(imsRegistry.getAdminPasswd());
        UserApi userApi = new UserApi(url, imsRegistry.getAdminName(), adminPasswd);
        UserCreationReq userCreationReq = new UserCreationReq();
        userCreationReq.setUsername(userName);
        userCreationReq.setPassword(password);
        userCreationReq.setRealname(userName);
        //userCreationReq.setEmail(userName + "@lnjoying.com");
        HarborUserUtils.doSetEmail(combRpcService, userName, userCreationReq, currentUserId);
        try
        {
            userApi.createUser(userCreationReq, null);
        }
        catch (Exception e)
        {
            log.error("add registry user  failed : {}", e);
            throw new ImsWebSystemException(ADD_REGISTRY_USER_ERROR, INFO);
        }
    }

    private void updateHarborUser(String userName, String password, String oldPassword, TblImsRegistry imsRegistry) throws ImsWebSystemException
    {
        String url = ApiClient.buildHTTPSRequestUrl(imsRegistry.getUrl());
        String adminPasswd = AesCryptoUtils.cbcDecryptStr(imsRegistry.getAdminPasswd());
        UserApi userApi = new UserApi(url, imsRegistry.getAdminName(), adminPasswd);
        List<UserResp> userResps = userApi.listUsers(null, "username=" + userName, null, 1L, 1L);
        if (CollectionUtils.isNotEmpty(userResps) && userResps.size() == 1)
        {
            UserResp userResp = userResps.get(0);
            Integer userId = userResp.getUserId();
            PasswordReq passwordReq = new PasswordReq();
            passwordReq.setNewPassword(password);
            passwordReq.setOldPassword(oldPassword);
            try
            {
                userApi.updateUserPassword(userId, passwordReq, null);
            }
            catch (Exception e)
            {
                log.error("update registry password  failed : {}", e);
                throw new ImsWebSystemException(UPDATE_REGISTRY_USER_PASSWORD_FAIL, INFO);
            }

        }
    }
}
