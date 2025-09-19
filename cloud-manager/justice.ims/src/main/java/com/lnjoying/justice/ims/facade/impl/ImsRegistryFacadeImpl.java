package com.lnjoying.justice.ims.facade.impl;

import com.github.pagehelper.Page;
import com.google.common.collect.Lists;
import com.lnjoying.justice.ims.common.ImsMsgType;
import com.lnjoying.justice.ims.common.ImsOperatorType;
import com.lnjoying.justice.ims.config.ImsConfig;
import com.lnjoying.justice.ims.db.model.*;
import com.lnjoying.justice.ims.db.repo.ImsProjectRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryPreDownloadRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryUserRepository;
import com.lnjoying.justice.ims.domain.dto.req.*;
import com.lnjoying.justice.ims.domain.dto.rsp.*;
import com.lnjoying.justice.ims.domain.model.*;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistryFacade;
import com.lnjoying.justice.ims.facade.ImsRegistryRepoFacade;
import com.lnjoying.justice.ims.facade.NetMessageServiceFacade;
import com.lnjoying.justice.ims.harbor.ApiClient;
import com.lnjoying.justice.ims.harbor.api.*;
import com.lnjoying.justice.ims.harbor.model.*;
import com.lnjoying.justice.ims.process.service.ImsMsgProcessStrategy;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.ims.util.Base64Utils;
import com.lnjoying.justice.ims.util.UrlEncoderDecoderUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.msg.*;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.ims.db.model.TblImsRegistry.RegistryStatus.*;
import static com.lnjoying.justice.ims.domain.dto.rsp.RegistryInstallInfoRsp.*;
import static com.lnjoying.justice.ims.domain.model.ImsRegistryRepo.projectName;
import static com.lnjoying.justice.ims.domain.model.ImsRegistryRepo.repoName;
import static com.lnjoying.justice.ims.harbor.api.PingApi.PONG;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * implementation class
 *
 * @author merak
 **/

@Service
@Slf4j
public class ImsRegistryFacadeImpl implements ImsRegistryFacade
{
    private static final String ACTIVE = "active";
    private static final String DEACTIVE = "deactive";

    private final ImsRegistryRepository registryRepository;

    private final ImsProjectRepository imsProjectRepository;

    private final ImsRegistryPreDownloadRepository preDownloadRepository;

    private final ImsMsgProcessStrategy imsMsgProcessStrategy;

    private final ImsRegistryUserRepository registryUserRepository;

    private final ImsRegistryRepoFacade imsRegistryRepoFacade;


    @Autowired
    private CombRpcService combRpcService;

    @Autowired
    private NetMessageServiceFacade netMessageServiceFacade;

    @Autowired
    private Executor timedExecutor;

    @Autowired
    private Executor executor;

    @Autowired
    @Lazy
    private ImsConfig imsConfig;

    @Autowired
    public ImsRegistryFacadeImpl(ImsRegistryRepository registryRepository, ImsRegistryPreDownloadRepository preDownloadRepository,
                                 ImsMsgProcessStrategy imsMsgProcessStrategy, ImsRegistryUserRepository registryUserRepository,
                                 ImsProjectRepository imsProjectRepository, ImsRegistryRepoFacade imsRegistryRepoFacade)
    {
        this.registryRepository = registryRepository;
        this.preDownloadRepository = preDownloadRepository;
        this.imsMsgProcessStrategy = imsMsgProcessStrategy;
        this.registryUserRepository = registryUserRepository;
        this.imsProjectRepository = imsProjectRepository;
        this.imsRegistryRepoFacade = imsRegistryRepoFacade;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegistryInstallInfoRsp addRegistry(AddRegistryReq req)
    {
        // Determine whether the registry exists by the registry name
        List<TblImsRegistry> tblImsRegistries = registryRepository.getRegistryByName(req.getRegistryName());
        if (CollectionUtils.isNotEmpty(tblImsRegistries))
        {
            throw new ImsWebSystemException(REGISTRY_EXIST, INFO);
        }

        HashSet<String> regions = new HashSet<>(req.getRegions());
        checkRegions(regions);

        TblImsRegistry imsRegistry = new TblImsRegistry();
        BeanUtils.copyProperties(req, imsRegistry);
        imsRegistry.setRegistryId(Utils.assignUUId());
        imsRegistry.setStatus(CREATING.value());
        String adminPasswd = imsRegistry.getAdminPasswd();
        imsRegistry.setAdminPasswd(AesCryptoUtils.cbcEncryptHex(adminPasswd));
        try
        {
            registryRepository.insertSelective(imsRegistry);
        }
        catch (DuplicateKeyException e)
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_DUP, INFO);
        }

        insertRegistryRegions(regions, imsRegistry.getRegistryId());

        //  After adding a new registry, users need to be synchronized ,
        //  so the users whose status is completed are partially completed.
        registryUserRepository.updateCompletedStatusToPartiallyCompleted();
        String harborInstallPath = imsConfig.getHarborInstallPath();
        RegistryInstallInfoRsp res = builder().registryId(imsRegistry.getRegistryId())
                .registryName(imsRegistry.getRegistryName())
                .harborInstallOnline("curl " + harborInstallPath + HARBOR_INSTALL_ONLINE + "  | sh -s " + imsRegistry.getUrl() + " " + adminPasswd)
                .harborInstallOnlinePart("curl " + harborInstallPath + HARBOR_INSTALL_ONLINE_PART + "  | sh -s " + imsRegistry.getUrl() + " " + adminPasswd)
                .harborInstallOffline("curl " + harborInstallPath + HARBOR_INSTALL_OFFLINE + "  | sh -s " + imsRegistry.getUrl() + " " + adminPasswd)
                .harborStop("curl " + harborInstallPath + HARBOR_STOP + "  | sh -s ")
                .harborRemove("curl " + harborInstallPath + HARBOR_REMOVE + "  | sh -s ").build();
        return res;

    }

    @Override
    public RegistriesRsp getRegistryList(String name, String regionId, String regionName, String userId, Collection<Integer> status, Integer pageSize, Integer pageNum)
    {
        Set<String> registryIds = new HashSet<>();

        if (StringUtils.hasText(regionId))
        {
            registryIds.addAll(registryRepository.selectByRegionId(regionId));
        }

        if (StringUtils.hasText(regionName))
        {
            Set<String> regionIdsByName = combRpcService.getRegionResourceService().getRegionIdsByName(regionName);
            regionIdsByName.stream().forEach(id -> {
                registryIds.addAll(registryRepository.selectByRegionId(id));
            });
        }

        if (StringUtils.hasText(regionId) || StringUtils.hasText(regionName))
        {
            if (CollectionUtils.isEmpty(registryIds))
            {
                return RegistriesRsp.builder().totalNum(0).registries(Collections.EMPTY_LIST).build();
            }
        }
        RegistriesRsp rsp = registryRepository.getRegistryList(name, registryIds, userId, status, pageSize, pageNum);
        rsp.getRegistries().stream().forEach(registry -> registry.setRegions(getRegions(registry.getRegistryId())));
        return rsp;
    }

    @Override
    public TblImsRegistry getImsRegistry(String registryId, String operUserId)
    {
        Assert.hasText(registryId, "registryId can not be empty");
        TblImsRegistry regionRegistry = registryRepository.getRegistry(registryId, operUserId);
        return regionRegistry;
    }

    @Override
    public ImsRegistry getRegistry(String registryId, String operUserId)
    {
        Assert.hasText(registryId, "registryId can not be empty");
        TblImsRegistry imsRegistry =  registryRepository.getRegistryDetail(registryId, operUserId);
        ImsRegistry res = new ImsRegistry();
        BeanUtils.copyProperties(imsRegistry, res);

        // get regions
        List<ImsRegistry.Region> imsRegions = getRegions(registryId);
        res.setRegions(imsRegions);
        return res;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRegistry(String registryId, UpdateRegistryReq req)
    {
        List<String> regions = req.getRegions();
        if (CollectionUtils.isNotEmpty(regions))
        {
            HashSet<String> regionSet = new HashSet<>(regions);
            checkRegions(regionSet);
            insertRegistryRegions(regionSet, registryId);
        }

        // update registry
        req.setRegistryId(registryId);
        TblImsRegistry imsRegistry = new TblImsRegistry();
        BeanUtils.copyProperties(req, imsRegistry);
        imsRegistry.setUpdateTime(LocalDateTime.now());
        registryRepository.updateRegistry(imsRegistry);

        // update registryRegion, Delete first and add later
        // registryRepository.deleteRegistryRegionsByRegistryId(registryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRegistry(String registryId, String operUserId, String operBpId)
    {
        // set registry status deleted
        TblImsRegistry imsRegistry = registryRepository.getRegistryDoNotCheckStatus(registryId, null);
        if (hasAssociatedResources(imsRegistry))
        {
            throw new ImsWebSystemException(ASSOCIATED_RESOURCES_IN_THE_REGISTRY, ERROR);
        }

        // delete the record of  TblImsRegistryRegion table
        registryRepository.deleteRegistryRegionsByRegistryId(registryId);

        registryRepository.deleteByPrimaryKey(registryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(RegistryPasswordReq req)
    {
        TblImsRegistry registry = getImsRegistry(req.getRegistryId(), null);

        String newPassword = req.getNewPassword();
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        if (!password.equals(req.getOldPassword()))
        {
            throw new ImsWebSystemException(REGISTRY_PASSWORD_ERROR, INFO);
        }

        // update record
        String encryptPassword = AesCryptoUtils.cbcEncryptHex(newPassword);
        TblImsRegistry record = TblImsRegistry.builder().adminPasswd(encryptPassword).registryId(req.getRegistryId()).build();
        registryRepository.updateRegistry(record);

        // update harbor
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        UserApi userApi = new UserApi(url, registry.getAdminName(), password);
        // adminId = 1
        PasswordReq passwordReq = new PasswordReq();
        passwordReq.setOldPassword(password);
        passwordReq.setNewPassword(newPassword);
        try
        {
            userApi.updateUserPassword(1, passwordReq, null);
        }
        catch (Exception e)
        {
            log.error("update registry admin password failed : {}", e);
            throw new ImsWebSystemException(MODIFY_REGISTRY_PASSWORD_FAILED, INFO);
        }
    }

    @Override
    public void action(String registryId, String userId, String bpId, String action)
    {
        TblImsRegistry imsRegistry = new TblImsRegistry();
        if (ACTIVE.equalsIgnoreCase(action))
        {
            imsRegistry.setStatus(TblImsRegistry.RegistryStatus.ENABLE.value());
        }

        if (DEACTIVE.equalsIgnoreCase(action))
        {
            imsRegistry.setStatus(TblImsRegistry.RegistryStatus.DISABLE.value());
        }

        imsRegistry.setRegistryId(registryId);
        imsRegistry.setUserId(userId);
        imsRegistry.setUpdateTime(LocalDateTime.now());
        registryRepository.updateRegistry(imsRegistry);
    }

    @Override
    public void processPingRegistry()
    {
        List<Integer> statusList = Lists.newArrayList(CREATING.value(), FAILED.value(), HEALTHY.value(), ENABLE.value());
        List<TblImsRegistry> tblImsRegistries = registryRepository.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistries))
        {
            List<CompletableFuture<Void>> futureList = tblImsRegistries.stream().map(registry1 -> CompletableFuture.runAsync(() ->
            {
                final TblImsRegistry registry = registry1;
                TblImsRegistry tblImsRegistry = new TblImsRegistry();
                tblImsRegistry.setRegistryId(registry.getRegistryId());
                tblImsRegistry.setUpdateTime(LocalDateTime.now());
                String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                PingApi pingApi = new PingApi(url, registry.getAdminName(), AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd()));
                String pong = "";
                try
                {
                    pong = pingApi.getPing(null);
                    if (PONG.equalsIgnoreCase(pong))
                    {
                        // Change record status
                        if (registry.getStatus() < SUCCEED.value())
                        {
                            tblImsRegistry.setStatus(SUCCEED.value());
                        }
                    }
                }
                catch (Exception e)
                {
                    if (log.isDebugEnabled())
                    {
                        log.error("registry ping failed： {}", e);
                    }
                    tblImsRegistry.setStatus(FAILED.value());
                }

                registryRepository.updateRegistry(tblImsRegistry);
            }, timedExecutor)).collect(Collectors.toList());

            futureList.stream().map(CompletableFuture::join).count();
        }
    }

    @Override
    public void processAuthenticateRegistry()
    {
        List<Integer> statusList = Lists.newArrayList(SUCCEED.value(), UNHEALTHY.value());
        List<TblImsRegistry> tblImsRegistries = registryRepository.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistries))
        {
            List<CompletableFuture<Void>> futureList = tblImsRegistries.stream().map(registry -> CompletableFuture.runAsync(() ->
            {
                TblImsRegistry tblImsRegistry = new TblImsRegistry();

                String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                UserApi userApi = new UserApi(url, registry.getAdminName(), AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd()));
                try
                {
                    // use ‘userApi.getCurrentUserInfo(null)’ is incorrect
                    // User id 1 is admin
                    UserResp currentUserInfo = userApi.getUser(1, null);
                    if (Objects.nonNull(currentUserInfo))
                    {
                        tblImsRegistry.setStatus(HEALTHY.value());
                    }
                }
                catch (HttpClientErrorException e)
                {
                    if (e.getStatusCode().value() == HttpStatus.SC_UNAUTHORIZED)
                    {
                        if (log.isDebugEnabled())
                        {
                            log.error("registry admin account password is incorrect，registry Authentication failed： {}", e);
                        }
                        tblImsRegistry.setStatus(UNHEALTHY.value());
                    }
                }
                if (Objects.nonNull(tblImsRegistry.getStatus()))
                {
                    tblImsRegistry.setRegistryId(registry.getRegistryId());
                    tblImsRegistry.setUpdateTime(LocalDateTime.now());
                    registryRepository.updateRegistry(tblImsRegistry);
                }
            }, timedExecutor)).collect(Collectors.toList());

            futureList.stream().map(CompletableFuture::join).count();
        }
    }

    @Override
    public RegistryImagesRsp getAllRegistryRepos(String registryId)
    {
        List<String> images = Collections.synchronizedList(new ArrayList<>());
        TblImsRegistry registry = getImsRegistry(registryId, null);
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        RepositoryApi repositoryApi = new RepositoryApi(url, registry.getAdminName(), AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd()));

        List<Repository> allRepositories = new ArrayList<>();
        long i= 0;
        while (true)
        {
            //  Get up to 100 entries at a time
            List<Repository> repositories = repositoryApi.listAllRepositories(null, null, null, (++i), 100L);;
            if (CollectionUtils.isEmpty(repositories))
            {
                break;
            }
            allRepositories.addAll(repositories);
        }
        if (CollectionUtils.isNotEmpty(allRepositories))
        {
            doHandleRepostories(allRepositories, registry, images, repositoryApi);
        }
        RegistryImagesRsp res = RegistryImagesRsp.builder().registryId(registryId).repos(images).build();
        return res;
    }

    @Override
    public RegistryImagesRsp getAllRegistryReposByProjectId(String registryId, String projectId)
    {
        List<String> images = Collections.synchronizedList(new ArrayList<>());
        TblImsRegistry registry = getImsRegistry(registryId, null);
        String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
        RepositoryApi repositoryApi = new RepositoryApi(url, registry.getAdminName(), AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd()));
        TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProject(projectId, null, null);
        if (Objects.nonNull(registryProject))
        {
            List<Repository> allRepositories = new ArrayList<>();
            long i= 0;
            while (true)
            {
                //  Get up to 100 entries at a time
                List<Repository> repositories = repositoryApi.listRepositories(registryProject.getProjectName(), null, null, null, (++i), 100L);
                if (CollectionUtils.isEmpty(repositories))
                {
                    break;
                }
                allRepositories.addAll(repositories);
            }
            if (CollectionUtils.isNotEmpty(allRepositories))
            {
                doHandleRepostories(allRepositories, registry, images, repositoryApi);
            }
        }
        RegistryImagesRsp res = RegistryImagesRsp.builder().registryId(registryId).repos(images).build();
        return res;
    }

    @Override
    public RegistryNodesRsp getAllRegistryNodes(String registryId)
    {
        List<RegionResourceService.RegionDto> regionDtos = new ArrayList<>();
        Set<String> regionIds = registryRepository.selectRegistryRegionsByRegistryId(registryId);
        if (CollectionUtils.isNotEmpty(regionIds))
        {
            regionDtos = combRpcService.getRegionResourceService().selectAllNodesByRegionIds(regionIds);
        }
        return RegistryNodesRsp.builder().registryId(registryId).regions(regionDtos).build();
    }

    @Override
    public void processEnableRegistry()
    {
        List<Integer> statusList = Lists.newArrayList(HEALTHY.value());
        List<TblImsRegistry> tblImsRegistries = registryRepository.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistries))
        {
            List<CompletableFuture<Void>> futureList = tblImsRegistries.stream().map(registry -> CompletableFuture.runAsync(() ->
            {
                String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                HealthApi healthyApi = new HealthApi(url, registry.getAdminName(), AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd()));
                OverallHealthStatus health = healthyApi.getHealth(null);
                if (Objects.nonNull(health) && OverallHealthStatus.HEALTHY.equalsIgnoreCase(health.getStatus()))
                {
                    TblImsRegistry tblImsRegistry = new TblImsRegistry();
                    tblImsRegistry.setRegistryId(registry.getRegistryId());
                    tblImsRegistry.setStatus(ENABLE.value());
                    tblImsRegistry.setUpdateTime(LocalDateTime.now());
                    registryRepository.updateRegistry(tblImsRegistry);
                }

            }, timedExecutor)).collect(Collectors.toList());

            futureList.stream().map(CompletableFuture::join).count();
        }
    }

    @Override
    public void preDownload(PreDownloadReq req)
    {

        TblImsRegistry registry = registryRepository.getRegistry(req.getRegistryId(), null);
        String password = AesCryptoUtils.cbcDecryptStr(registry.getAdminPasswd());
        String encodeRegistryPassword = Base64Utils.encode(password);

        List<String> nodes = req.getNodes();
        //String repos = repos1.stream().collect(Collectors.joining(","));
        if (CollectionUtils.isNotEmpty(nodes))
        {
            nodes.stream().forEach(nodeId ->
            {
                List<String> repos = req.getRepos();
                if (CollectionUtils.isNotEmpty(repos))
                {
                    AtomicInteger count = new AtomicInteger(0);
                    String uuid = Utils.assignUUId();
                    repos.stream().forEach(repo ->
                    {
                        String id = uuid + "-" + count.incrementAndGet();
                        TblImsRegistryPreDownload build = TblImsRegistryPreDownload.builder().registryId(req.getRegistryId())
                                .id(id).nodeId(nodeId).repos(repo)
                                .status(TblImsRegistryPreDownload.PreDownloadStatus.CREATING.value()).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                                .userId(req.getUserId()).bpId(req.getBpId()).userName(req.getUserName()).bpName(req.getBpName())
                                .build();
                        preDownloadRepository.insert(build);
                        MessagePack msgPack = new MessagePack();
                        msgPack.setMsgType(ImsMsgType.PRE_DOWNLOAD);
                        ImsPreDownload imsPreDownload = ImsPreDownload.builder().nodeId(nodeId).repos(repo).id(id)
                                .registryUrl(registry.getUrl()).registryUserName(registry.getAdminName())
                                .registryPassword(encodeRegistryPassword).build();

                        msgPack.setMessageObj(imsPreDownload);
                        imsMsgProcessStrategy.sendMessage(msgPack);
                    });
                }
            });

        }
    }

    @Override
    public void processPreDownload(ImsPreDownload imsPreDownload)
    {
        log.info("submit pre download msg to node:{}", imsPreDownload.getNodeId());
        EeImageDef.msg_image_pull_req_body.Builder pullReq = EeImageDef.msg_image_pull_req_body.newBuilder();
        pullReq.setOperId(imsPreDownload.getId());
        pullReq.setType("docker");
        pullReq.setImageName(imsPreDownload.getRepos());
        // registry Info
        EeCommonDef.registry_info.Builder registryInfo = EeCommonDef.registry_info.newBuilder();
        String registryUrl = imsPreDownload.getRegistryUrl();
        registryUrl = StringUtils.isEmpty(registryUrl) ? "" : registryUrl;
        registryInfo.setServer(registryUrl);
        String registryUserName = imsPreDownload.getRegistryUserName();
        registryUserName = StringUtils.isEmpty(registryUserName) ? "" : registryUserName;
        registryInfo.setUsername(registryUserName);
        String registryPassword = imsPreDownload.getRegistryPassword();
        registryPassword = StringUtils.isEmpty(registryPassword) ? "" : registryPassword;
        registryInfo.setPassword(registryPassword);
        pullReq.setRegistry(registryInfo);

        EeImageDef.msg_image_operator_body.Builder operatorBody = EeImageDef.msg_image_operator_body.newBuilder();
        operatorBody.setOperatorType(ImsOperatorType.IMAGE_PULL_REQ);
        operatorBody.setImagePullReqBody(pullReq);

        EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.IMAGE_OPERATOR, imsPreDownload.getId());
        EeNetMessageApi.ee_net_message.Builder netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setImageOperatorBody(operatorBody);
        byte[] messageByte = netMessage.build().toByteArray();
        netMessageServiceFacade.submitMessage(imsPreDownload.getNodeId(), messageByte);
    }

    @Override
    public void processImagePullRsp(EeImageDef.msg_image_pull_rsp_body imagePullRspBody, EeCommonDef.msg_header head)
    {
        String sessionId = head.getSessionId();
        int status = imagePullRspBody.getErrorCode();
        TblImsRegistryPreDownload build = TblImsRegistryPreDownload.builder().id(sessionId).status(status).build();
        log.info("pre download image pull rsp:{} ", build);
        //preDownloadRepository.update(build);
    }

    @Override
    public void processImageOperRptReq(EeImageDef.msg_image_oper_rpt_req_body imageOperRptReqBody, EeCommonDef.msg_route route, EeCommonDef.msg_header head)
    {
        String operId = imageOperRptReqBody.getOperId();
        int status = imageOperRptReqBody.getStatus();
        TblImsRegistryPreDownload build = TblImsRegistryPreDownload.builder().id(operId).status(status).build();
        int resStatus = SUCCESS.getCode();
        try
        {
            preDownloadRepository.update(build);
        }
        catch (Exception e)
        {
            log.error("failed to update pre download record: {}", e);
            resStatus = ErrorCode.FAILED_TO_UPDATE_PRE_DOWNLOAD.getCode();
        }

        buildImageOperRptRsp(head, route, resStatus);
    }

    @Override
    public String command(TenantCommandReq req)
    {
        TblImsRegistry registry = null;
        String registryId = req.getRegistryId();
        if (Objects.nonNull(registryId))
        {
            registry = registryRepository.getRegistry(registryId, null);
        }
        else
        {
            List<TblImsRegistry> tblImsRegistryList = registryRepository.getRegistryByRegionId(req.getRegionId());
            if (CollectionUtils.isEmpty(tblImsRegistryList))
            {
                throw new ImsWebSystemException(REGION_REGISTRY_NOT_FOUND, INFO);
            }
            // One region corresponds to one registry
            registry = tblImsRegistryList.get(0);
        }

        ImsCommandType commandType = ImsCommandType.fromValue(req.getCommandType());
        String res = "";
        switch (commandType)
        {
            case LOGIN:
                res = getLoginCommand(req.getUserId(), registry);
                break;
            case PULL:
                res = getPullCommand(registry);
                break;
            case PUSH:
                res = getPushCommand(registry);
                break;
            default:
                // The entrance has been restricted, and there is no situation of getting here
                throw new ImsWebSystemException(REGISTRY_COMMAND_TYPE_ERROR, INFO);
        }
        return res;
    }

    @Override
    public RegistryReposRsp searchRepos(String repoName, Integer pageNum, Integer pageSize, String bpId, String userId)
    {

        return doSearchRepos(repoName, pageNum, pageSize, userId);
    }

    @Override
    public List<RegistryRegion> getRegistryRegions()
    {
        List<RegistryRegion> res = new ArrayList<>();
        List<TblImsRegistryRegion> registryRegions = registryRepository.selectAllEnableRegistryRegions();
        if (CollectionUtils.isNotEmpty(registryRegions))
        {
            Set<String> regionIds = registryRegions.stream().map(region -> region.getRegionId()).distinct().collect(Collectors.toSet());
            List<RegionResourceService.Region> regions = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(regionIds))
            {
                 regions = combRpcService.getRegionResourceService().getRegionByRegionIds(regionIds);
            }

            List<RegionResourceService.Region> finalRegions = regions;
            List<RegistryRegion> imsRegions = registryRegions.stream().map(region ->
                {
                    String regionId = region.getRegionId();
                    String registryId = region.getRegistryId();
                    String regionName = "";
                    if (CollectionUtils.isNotEmpty(finalRegions))
                    {
                        Optional<String> name = finalRegions.stream().filter(x -> x.getRegionId().equals(regionId)).map(y -> y.getRegionName()).findFirst();
                        if (name.isPresent())
                        {
                            regionName = name.get();
                        }
                    }
                    return new RegistryRegion(registryId, regionId, regionName);
                }).collect(Collectors.toList());
                res.addAll(imsRegions);
                // Delete the one without a name, it may have been deleted
                res.removeIf(registryRegion -> !StringUtils.hasText(registryRegion.getRegionName()));
            }
        return res;
    }

    @Override
    public ImageDownloadInfoRsp getPreDownloads(String registryId, String imageName, String nodeId, String queryBpId, String queryUserId, Integer pageNum, Integer pageSize)
    {
        ImageDownloadInfoRsp  imageDownloadInfoRsp = preDownloadRepository.getPreDownloads(registryId, imageName, nodeId, queryBpId, queryUserId, pageNum, pageSize);
        imageDownloadInfoRsp.getImages().stream().forEach(imageDownloadInfo -> {
            String tempNodeId = imageDownloadInfo.getNodeId();
            ImageDownloadInfo.NodeInfo nodeInfoTarget = new ImageDownloadInfo.NodeInfo();
            nodeInfoTarget.setNodeId(tempNodeId);
            try
            {
                RegionResourceService.NodeInfo nodeInfo= combRpcService.getRegionResourceService().getNodeInfoByNodeId(tempNodeId);
                if (Objects.nonNull(nodeInfo))
                {
                    BeanUtils.copyProperties(nodeInfo, nodeInfoTarget);
                }
            }
            catch (Exception e)
            {
                log.error("get node info failed, reason: {}", e);
            }
            imageDownloadInfo.setNodeInfo(nodeInfoTarget);

            final Integer status = imageDownloadInfo.getStatus();
            TblImsRegistryPreDownload.SimplePreDownloadStatus simplePreDownloadStatus = TblImsRegistryPreDownload.SimplePreDownloadStatus.getSimpleStatus(status);
            ImageDownloadInfo.StatusCode statusCode = new ImageDownloadInfo.StatusCode();
            statusCode.setCode(status);
            statusCode.setDesc(simplePreDownloadStatus.toMap());
            imageDownloadInfo.setStatusCode(statusCode);

        });
        return imageDownloadInfoRsp;
    }

    @Override
    public void deletePreDownloads(String jobIds)
    {
        if (StringUtils.hasText(jobIds))
        {
            List<String> jobIdList = Lists.newArrayList(org.apache.commons.lang3.StringUtils.split(jobIds, ","));
            preDownloadRepository.deleteByJobIdList(jobIdList);
        }
    }

    @Override
    public void deletePreDownloadsJobs(String ids)
    {
        if (StringUtils.hasText(ids))
        {
            List<String> idList = Lists.newArrayList(org.apache.commons.lang3.StringUtils.split(ids, ","));
            preDownloadRepository.deleteByIdList(idList);
        }
    }

    public TblImsRegistry selectRegistryByUrl(String url)
    {
        return registryRepository.selectByUrl(url);
    }

    @Override
    public String getRegistryUrlById(String registryId)
    {
        TblImsRegistry registry = registryRepository.selectByPrimaryKey(registryId);
        if (Objects.nonNull(registry))
        {
            return registry.getUrl();
        }

        return "";
    }

    private RegistryReposRsp doSearchRepos(String repoName, Integer pageNum, Integer pageSize, String userId)
    {
        RegistryReposRsp registryReposRsp = RegistryReposRsp.builder().totalNum(0).repos(new ArrayList<>()).build();
        AtomicInteger totalNum = new AtomicInteger(0);
        List<Integer> statusList = Lists.newArrayList(ENABLE.value());
        TblImsRegistryUser tblImsRegistryUser = null;
        boolean admin = isAdmin();
        if (!admin)
        {
            tblImsRegistryUser = registryUserRepository.selectOneByUserId(userId);
            if (Objects.isNull(tblImsRegistryUser))
            {
                throw new ImsWebSystemException(REGISTRY_USER_NOT_EXIST, INFO);
            }
        }

        List<TblImsRegistry> tblImsRegistries = registryRepository.selectAllByStatus(statusList);
        if (CollectionUtils.isNotEmpty(tblImsRegistries))
        {
            TblImsRegistryUser finalTblImsRegistryUser = tblImsRegistryUser;
            List<CompletableFuture<List<ImsRegistryRepo>>> futureList = tblImsRegistries.stream().map(registry -> CompletableFuture.supplyAsync(() ->
            {
                List<SearchRepository> searchRepositories = new ArrayList<>();
                List<ImsRegistryRepo> imsRegistryRepos = new ArrayList<>();
                String registryUserName = "";
                String registryPassword = "";
                String url = ApiClient.buildHTTPSRequestUrl(registry.getUrl());
                if (admin)
                {
                    registryUserName = registry.getAdminName();
                    registryPassword = registry.getAdminPasswd();
                }
                else
                {
                    registryUserName = finalTblImsRegistryUser.getRegistryUserName();
                    registryPassword = finalTblImsRegistryUser.getRegistryUserPassword();
                }
                String password = AesCryptoUtils.cbcDecryptStr(registryPassword);
                SearchApi searchApi = new SearchApi(url, registryUserName, password);
                String deCodeRepoName = UrlEncoderDecoderUtils.decodeUtf8(repoName);
                Search search = searchApi.search(deCodeRepoName, null);
                if (Objects.nonNull(search))
                {
                    searchRepositories = search.getRepository();
                    if (CollectionUtils.isNotEmpty(searchRepositories))
                    {
                        totalNum.getAndAdd(searchRepositories.size());
                    }
                }
                else {
                    return imsRegistryRepos;
                }

                //Need a second call to get more information
                List<SearchRepository> pageInfo = handlerSearchRepositories(pageNum, pageSize, searchRepositories);
                if (CollectionUtils.isNotEmpty(pageInfo))
                {
                    List<CompletableFuture<ImsRegistryRepo>> repoFutureList = pageInfo.stream().map(searchRepository -> CompletableFuture.supplyAsync(() ->
                    {
                        try
                        {
                            RepositoryApi repositoryApi = new RepositoryApi(searchApi.getApiClient());
                            String projectName = searchRepository.getProjectName();
                            String name = repoName(searchRepository.getRepositoryName());
                            TblImsRegistryProject registryProject = imsProjectRepository.getRegistryProjectByProjectName(projectName);
                            Repository repository = repositoryApi.getRepository(projectName, UrlEncoderDecoderUtils.encodeUtf8(name), null);
                            ImsRegistryRepo repo = ImsRegistryRepo.of(repository, registryProject);
                            ImsRegistryRepo imsRegistryRepo = imsRegistryRepoFacade.buildRepo(repo, searchApi.getApiClient(), projectName);
                            return imsRegistryRepo;
                        }
                        catch (Exception e)
                        {
                            log.error("get repo list error ：{}", e);
                            throw new ImsWebSystemException(GET_REPO_LIST_ERROR, INFO);
                        }
                    }, executor).exceptionally(er ->
                    {
                        totalNum.decrementAndGet();
                        return null;
                    })).collect(Collectors.toList());
                    imsRegistryRepos = repoFutureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
                    return imsRegistryRepos;
                }
                return imsRegistryRepos;
            }, executor)).collect(Collectors.toList());
            List<ImsRegistryRepo> collect = futureList.stream().map(CompletableFuture::join)
                    .flatMap(Collection::stream).filter(Objects::nonNull).collect(Collectors.toList());
            List<ImsRegistryRepo> distinctCollect = collect.stream().distinct().collect(Collectors.toList());
            List<ImsRegistryRepo> pageInfo = handlerSearchRepositories(pageNum, pageSize, distinctCollect);
            registryReposRsp.setTotalNum(totalNum.get() - (collect.size() - distinctCollect.size()));
            registryReposRsp.setRepos(pageInfo);
        }
        return registryReposRsp;

    }

    private <T> List<T> handlerSearchRepositories(Integer pageNum, Integer pageSize, List<T> searchRepositories)
    {
        List<T> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(searchRepositories))
        {
            Page page = new Page(pageNum, pageSize);
            page.setTotal(searchRepositories.size());
            page.addAll(searchRepositories);
            long startRow = page.getStartRow() > page.getTotal() ? page.getTotal() : page.getStartRow();
            long endRow = page.getEndRow() > page.getTotal() ? page.getTotal() : page.getEndRow();
            list = page.subList(Long.valueOf(startRow).intValue(), Long.valueOf(endRow).intValue());
        }
        return list;
    }

    private String getLoginCommand(String userId, TblImsRegistry imsRegistry)
    {
        String registryUserName = "";
        if (isAdmin())
        {
            registryUserName = "admin";
        }
        else
        {
            List<TblImsRegistryUser> tblImsRegistryUsers = registryUserRepository.selectByUserId(userId);
            if (CollectionUtils.isEmpty(tblImsRegistryUsers))
            {
                throw new ImsWebSystemException(REGISTRY_USER_NOT_EXIST, INFO);
            }
            // A user can only have one registry user
            TblImsRegistryUser imsRegistryUser = tblImsRegistryUsers.get(0);
            registryUserName = imsRegistryUser.getRegistryUserName();
        }

        StringBuilder res = new StringBuilder();
        res.append("docker login");
        res.append(" -u " + registryUserName);
        // res.append(" -p " + AesCryptoUtils.cbcDecryptStr(imsRegistryUser.getRegistryUserPassword()));
        res.append(" -p " + "****");
        res.append(" " + imsRegistry.getUrl());
        return res.toString();
    }

    private String getPullCommand(TblImsRegistry imsRegistry)
    {
        StringBuilder res = new StringBuilder();
        res.append("docker pull ");
        res.append(imsRegistry.getUrl());
        res.append("/demoProject/demoRepo:1.0.0");
        return res.toString();
    }

    private String getPushCommand(TblImsRegistry imsRegistry)
    {
        StringBuilder res = new StringBuilder();
        res.append("docker push ");
        res.append(imsRegistry.getUrl());
        res.append("/demoProject/demoRepo:1.0.0");
        return res.toString();
    }

    private void buildImageOperRptRsp(EeCommonDef.msg_header head, EeCommonDef.msg_route route, int resStatus)
    {
        EeImageDef.msg_image_oper_rpt_rsp_body.Builder imageOperRptRspBody = EeImageDef.msg_image_oper_rpt_rsp_body.newBuilder();
        imageOperRptRspBody.setErrorCode(resStatus);

        EeImageDef.msg_image_operator_body.Builder operatorBody = EeImageDef.msg_image_operator_body.newBuilder();
        operatorBody.setOperatorType(ImsOperatorType.IMAGE_OPER_RPT_RSP);
        operatorBody.setImageOperRptRspBody(imageOperRptRspBody);

        EeCommonDef.msg_header.Builder reqHeader = netMessageServiceFacade.makeReqMsgHeader(MessageName.IMAGE_OPERATOR, head.getSessionId());
        EeNetMessageApi.ee_net_message netMessage = EeNetMessageApi.ee_net_message.newBuilder().setHead(reqHeader).setImageOperatorBody(operatorBody).build();
        netMessageServiceFacade.submitMessage(route.getONodeId(), netMessage.toByteArray());
    }

    private void checkRegions(Set<String> regions)
    {
        boolean correct = combRpcService.getRegionResourceService().getRegionIds().stream()
                .filter(regions::contains)
                .count() == regions.size();
        if (!correct)
        {
            log.error("error region id", regions);
            throw new ImsWebSystemException(ErrorCode.REGION_ID_ERROR, INFO);
        }
    }

    private void insertRegistryRegions(HashSet<String> regions, String registryId)
    {
        // region coverage, delete the previous
        if (CollectionUtils.isNotEmpty(regions))
        {
            registryRepository.deleteByRegions(regions);
        }
        registryRepository.deleteRegistryRegionsByRegistryId(registryId);
        List<TblImsRegistryRegion> registryRegions = regions.stream()
                .map(regionId -> new TblImsRegistryRegion(registryId, regionId))
                .collect(Collectors.toList());
        int insertCount = registryRepository.insertRegistryRegion(registryRegions);
        if (insertCount != regions.size())
        {
            throw new ImsWebSystemException(ErrorCode.REGISTRY_REGION_INSERT_FAILED, INFO);
        }
    }

    private List<ImsRegistry.Region> getRegions(String registryId)
    {
        Set<String> regions = registryRepository.selectRegistryRegionsByRegistryId(registryId);
        List<ImsRegistry.Region> imsRegions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(regions))
        {
            List<RegionResourceService.Region> regionByRegionIds = new ArrayList<>();
            try
            {
                regionByRegionIds = combRpcService.getRegionResourceService().getRegionByRegionIds(regions);
            }
            catch (Exception e)
            {
                log.error("getRegionByRegionIds error,regions: {}, error:", regions, e);
            }

            if (CollectionUtils.isNotEmpty(regionByRegionIds))
            {
                imsRegions = regionByRegionIds.stream()
                        .map(region -> new ImsRegistry.Region(region.getRegionId(), region.getRegionName())).collect(Collectors.toList());
            }
        }
        return imsRegions;
    }

    private void doHandleRepostories(List<Repository> repositories, TblImsRegistry registry,  List<String> images,   RepositoryApi repositoryApi) {
        ArtifactApi artifactApi = new ArtifactApi(repositoryApi.getApiClient());
        List<CompletableFuture<Void>> futureList = repositories.stream().map(repository -> CompletableFuture.runAsync(() ->
        {
            String name = repository.getName();
            if (StringUtils.hasText(name))
            {
                String repoName = repoName(name);
                String projectName = projectName(name);
                List<Artifact> artifacts = artifactApi.listArtifacts(projectName, UrlEncoderDecoderUtils.encodeUtf8(repoName), null, null, null, null, null, null, true, false, false, false, false);
                if (CollectionUtils.isNotEmpty(artifacts))
                {
                    artifacts.forEach(artifact -> {
                        List<Tag> tags = artifact.getTags();
                        if (CollectionUtils.isNotEmpty(tags))
                        {
                            tags.stream().forEach(tag ->
                            {
                                String tagName = tag.getName();
                                images.add(registry.getUrl() + "/" + name + ":" + tagName);
                            });
                        }
                    });

                }
            }
        }, executor)).collect(Collectors.toList());
        futureList.stream().map(CompletableFuture::join).count();
    }

    private boolean hasAssociatedResources(TblImsRegistry imsRegistry)
    {
        if (Objects.nonNull(imsRegistry))
        {
            // Verify that there are associated project
            List<TblImsRegistryProject> tblImsRegistryProjects = imsProjectRepository.selectByRegistryId(imsRegistry.getRegistryId());
            if (CollectionUtils.isNotEmpty(tblImsRegistryProjects))
            {
                return true;
            }
        }
        return false;
    }
}
