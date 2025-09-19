package com.lnjoying.justice.aos.facade;

import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lnjoying.justice.aos.config.HelmConfig;
import com.lnjoying.justice.aos.db.model.*;
import com.lnjoying.justice.aos.db.repo.HelmRepository;
import com.lnjoying.justice.aos.domain.dto.req.AddHelmRepoReq;
import com.lnjoying.justice.aos.domain.dto.req.ImportChartReq;
import com.lnjoying.justice.aos.domain.dto.req.InstallStackReq;
import com.lnjoying.justice.aos.domain.dto.rsp.*;
import com.lnjoying.justice.aos.domain.model.helm.*;
import com.lnjoying.justice.aos.helm.command.CommandStatusCode;
import com.lnjoying.justice.aos.helm.command.impl.*;
import com.lnjoying.justice.aos.helm.command.impl.plugin.CmPushCommand;
import com.lnjoying.justice.aos.helm.command.impl.registry.RegistryLoginCommand;
import com.lnjoying.justice.aos.helm.command.impl.repo.RepoAddCommand;
import com.lnjoying.justice.aos.helm.command.impl.repo.RepoRemoveCommand;
import com.lnjoying.justice.aos.helm.command.impl.repo.RepoUpdateCommand;
import com.lnjoying.justice.aos.helm.command.result.DefaultCommandResult;
import com.lnjoying.justice.aos.helm.command.result.InstallCommandResult;
import com.lnjoying.justice.aos.helm.command.result.ListCommandResult;
import com.lnjoying.justice.aos.helm.command.result.UninstallCommandResult;
import com.lnjoying.justice.aos.helm.kubectl.PodsGetCommand;
import com.lnjoying.justice.aos.helm.model.IndexFile;
import com.lnjoying.justice.aos.helm.model.ReleaseStatus;
import com.lnjoying.justice.aos.service.CombRpcService;
import com.lnjoying.justice.aos.util.AesCryptoUtils;
import com.lnjoying.justice.aos.util.JacksonUtils;
import com.lnjoying.justice.aos.util.TarGzipUtils;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.commonweb.util.Base64Utils;
import com.lnjoying.justice.commonweb.util.FileUtils;
import com.lnjoying.justice.commonweb.util.YamlParserUtil;
import com.lnjoying.justice.schema.service.ims.ImsRepoService;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lnjoying.justice.aos.common.RedisCache.*;
import static com.lnjoying.justice.aos.db.model.TblHelmStackInfo.PREFIX;
import static com.lnjoying.justice.aos.db.model.TblHelmStackInfo.StackStatus.*;
import static com.lnjoying.justice.aos.db.model.TblRepoInfo.AuthenticationMethod.BASIC_AUTHENTICATION;
import static com.lnjoying.justice.aos.db.model.TblRepoInfo.AuthenticationMethod.fromValue;
import static com.lnjoying.justice.aos.db.model.TblRepoInfo.RepoStatus.*;
import static com.lnjoying.justice.aos.helm.command.impl.ListCommand.OutputType.JSON;
import static com.lnjoying.justice.aos.helm.kubectl.PodsGetCommand.PodStatus.*;
import static com.lnjoying.justice.aos.helm.kubectl.PodsGetCommand.setStatusJsonPath;
import static com.lnjoying.justice.aos.helm.model.ReleaseStatus.StatusDeployed;
import static com.lnjoying.justice.commonweb.util.FileUtils.FILE_SEPARATOR;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserId;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.getUserName;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.ERROR;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/5 15:19
 */

@Slf4j
@Service
@SuppressWarnings("unchecked")
public class HelmFacade
{

    @Autowired
    private HelmRepository helmRepository;

    @Autowired
    private Executor timedExecutor;

    @Autowired
    private HelmConfig helmConfig;

    private static Integer uncategorizedCategoryId;

    @Autowired
    private CombRpcService combRpcService;

    private TransactionTemplate transactionTemplate;

    // 6 hour
    private static final int KUBE_CONFIG_EXPIRED = 60 * 60 * 24;

    public HelmFacade(PlatformTransactionManager transactionManager)
    {
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public RepoInfo addRepo(AddHelmRepoReq req)
    {
        if (checkRepoNameExists(req.getRepoName()))
        {
            throw new WebSystemException(HELM_REPO_NAME_EXIST, ERROR);
        }

        TblRepoInfo tblRepoInfo = new TblRepoInfo();
        BeanUtils.copyProperties(req, tblRepoInfo);
        Object labels = CollectionUtils.isEmpty(req.getLabels()) ? new Object() : req.getLabels();
        tblRepoInfo.setLabels(labels);

        TblRepoInfo.AuthenticationMethod authenticationMethod = fromValue(req.getAuthMethod());
        switch (authenticationMethod)
        {

            case BASIC_AUTHENTICATION:
                AddHelmRepoReq.BasicAuth basicAuth = req.getBasicAuth();
                if (Objects.nonNull(basicAuth))
                {
                    String encryptPassword = AesCryptoUtils.cbcEncryptHex(basicAuth.getPassword());
                    basicAuth.setPassword(encryptPassword);
                }
                tblRepoInfo.setAuthInfo(basicAuth);
                break;
            case SECRET_AUTHENTICATION:
                AddHelmRepoReq.SecretAuth secretAuth = req.getSecretAuth();
                if (Objects.nonNull(secretAuth))
                {
                    String encryptKey = AesCryptoUtils.cbcEncryptHex(secretAuth.getKey());
                    String encryptCert = AesCryptoUtils.cbcEncryptHex(secretAuth.getCert());
                    secretAuth.setKey(encryptKey);
                    secretAuth.setCert(encryptCert);
                }
                tblRepoInfo.setAuthInfo(secretAuth);
                break;
            case NOT_AUTHENTICATED:
            default:
                tblRepoInfo.setAuthInfo(new Object());
        }

        checkAccess(req.getAccess());
        tblRepoInfo.setAccess(req.getAccess());
        tblRepoInfo.setStatus(CREATED.value());
        tblRepoInfo.setCreateTime(new Date());
        tblRepoInfo.setUpdateTime(tblRepoInfo.getCreateTime());
         helmRepository.insertSelective(tblRepoInfo);
        return RepoInfo.of(tblRepoInfo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteRepo(String repoName, String userId, String bpId)
    {
        TblRepoInfo repoInfo = getRepoInfoByRepoName(repoName);

        RepoRemoveCommand repoRemoveCommand = new RepoRemoveCommand();
        repoRemoveCommand.setRepoNames(Lists.newArrayList(repoName));
        try
        {
            DefaultCommandResult result = repoRemoveCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                log.error("execute helm repo remove  success:{}", result.getStatusMessage());
            }
            else
            {
                log.error("execute helm repo remove  fail:{}", result.getStatusMessage());
            }
        }
        catch (Exception e)
        {
            log.error("execute helm repo remove error:{}", e);
            throw new WebSystemException(HELM_COMMAND_EXECUTION_ERROR, ERROR);
        }

        helmRepository.deleteAppCategoryBindInfoByRepoId(repoInfo.getId());
        helmRepository.deleteAppVersionInfosByRepoName(repoName);
        helmRepository.deleteAppInfosByRepoName(repoName);
        helmRepository.deleteByRepoName(repoName);
    }

    public RepoRsp getRepoList(String repoName, Integer status, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<TblRepoInfo> repoInfos = helmRepository.selectAll(repoName, status);
        PageInfo<TblRepoInfo> pageInfo = new PageInfo<>(repoInfos);
        return RepoRsp.builder().repos(repoInfos.stream().map(RepoInfo::of).collect(Collectors.toList()))
                .totalNum(pageInfo.getTotal()).build();
    }

    public void validRepo()
    {
        List<Integer> statusList = Lists.newArrayList(CREATED.value());
        executeRepoCommandByStatus(statusList, repo -> executeRepoAdd(repo));
    }

    public void parseRepoIndex()
    {
        List<Integer> statusList = Lists.newArrayList(ACTIVATION.value());
        executeRepoCommandByStatus(statusList, repo -> executeParseRepoIndex(repo, null));
    }

    public void checkHelmRepoUpdate()
    {
        List<Integer> statusList = Lists.newArrayList(DATA_SYNC.value());
        executeRepoCommandByStatus(statusList, repo -> executeRepoUpdate(repo));

    }

    public void executeParseRepoIndex(TblRepoInfo repo, String md5)
    {
        String repoName = repo.getRepoName();
        int repoId = repo.getId();
        // get from local directory or http request, Temporarily realize the acquisition from the local directory
        String indexPath = getHelmIndexPath(repoName);
        //
        IndexFile indexFile = IndexFile.loadFrom(indexPath);
        if (Objects.nonNull(indexFile))
        {
            if (StringUtils.isEmpty(md5))
            {
                md5 = md5Index(repo.getRepoName());
            }
            updateRepoInfo(repo, md5);
            Map<String, Integer> categoryKeywords = getCategoryKeywords();

            SortedMap<String, SortedSet<IndexFile.ChartVersion>> entries = indexFile.getEntries();
            if (!CollectionUtils.isEmpty(entries))

            {

                // If no app has been added before, perform batch creation
                List<TblRepoAppInfo> tblRepoAppInfos = helmRepository.selectAppInfosAllPrecise(repoName, null);
                if (CollectionUtils.isEmpty(tblRepoAppInfos))
                {
                    transactionTemplate.executeWithoutResult(status -> {
                        batchInsertAppInfos(repo, entries);
                        batchInsertAppVersionInfos(repo, entries);
                        batchInsertCategoryBindInfos(repo, categoryKeywords, entries);
                    });
                }
                else
                {
                    // update app & app version & app category bind
                    entries.entrySet().forEach(entry ->
                    {
                        Set<String> keywords = entry.getValue().stream().map(chartVersion ->
                        {
                            return chartVersion.getKeywords();
                        }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet());
                        List<String> versions = entry.getValue().stream().map(chartVersion -> chartVersion.getVersion()).collect(Collectors.toList());
                        transactionTemplate.executeWithoutResult(status -> {
                            insertRepoAppInfo(repo, keywords, entry, versions);
                        });
                    });
                }

               // compare and delete old apps, select again
                tblRepoAppInfos = helmRepository.selectAppInfosAllPrecise(repoName, null);
                Set<String> newApps = entries.entrySet().stream().map(entry -> entry.getKey()).collect(Collectors.toSet());
                Set<String> oldApps = tblRepoAppInfos.stream().map(appInfo -> appInfo.getAppName()).collect(Collectors.toSet());
                Sets.SetView<String> difference = Sets.difference(oldApps, newApps);
                if (!difference.isEmpty())
                {
                    List<String> appIds = tblRepoAppInfos.stream().filter(appInfo -> difference.contains(appInfo.getAppName())).map(appInfo -> appInfo.getAppId()).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(appIds))
                    {
                        appIds.stream().forEach(appId -> {
                            cleanCategoryBindInfo(repoId, appId);
                        });
                    }

                    difference.forEach(appName -> {
                        cleanAppInfoAndRelatedResources(repoName, appName);
                    });

                }


                List<TblRepoAppVersionInfo> tblRepoAppVersionInfos = helmRepository.selectAppVersionInfos(repoName, null, null);
                if (!CollectionUtils.isEmpty(tblRepoAppVersionInfos))
                {
                    // compare and delete old versions, select again
                    Set<String> newVersions = entries.values().stream().flatMap(Collection::stream)
                            .map(chartVersion -> chartVersion.getName() + ":" + chartVersion.getVersion()).collect(Collectors.toSet());
                    Set<String> oldVersions =
                            tblRepoAppVersionInfos.stream().map(tblRepoAppVersionInfo -> tblRepoAppVersionInfo.getAppName() + ":" + tblRepoAppVersionInfo.getVersion())
                            .collect(Collectors.toSet());
                    Sets.SetView<String> versionDifference = Sets.difference(oldVersions, newVersions);
                    if (!versionDifference.isEmpty())
                    {
                        versionDifference.forEach(version -> {
                            String[] appVersion = version.split(":");
                            helmRepository.deleteAppVersion(repoName, appVersion[0], appVersion[1]);
                        });

                    }
                }

            }

        }
    }

    private void cleanCategoryBindInfo(int repoId, String appId)
    {
        helmRepository.deleteAppCategoryBindInfo(repoId, appId);
    }


    private void executeRepoCommandByStatus(List<Integer> statusList, Consumer<TblRepoInfo> consumer)
    {
        List<TblRepoInfo> repoInfos = helmRepository.selectAllByStatus(statusList);
        if (!CollectionUtils.isEmpty(repoInfos))
        {
            List<CompletableFuture<Void>> futureList = repoInfos.stream().map(repo -> CompletableFuture.runAsync(() ->
            {
                consumer.accept(repo);

            }, timedExecutor)).collect(Collectors.toList());

            futureList.stream().map(CompletableFuture::join).count();
        }
    }

    private void executeRepoAdd(TblRepoInfo repo)
    {
        // execute the helm repo add command
        RepoAddCommand repoAddCommand = new RepoAddCommand();
        repoAddCommand.setRepoName(repo.getRepoName());
        repoAddCommand.setRepoUrl(repo.getRepoUrl());
        repoAddCommand.setInsecureSkipTlsVerify(true);
        // set authentication information
        Object authInfo = repo.getAuthInfo();
        TblRepoInfo.AuthenticationMethod authenticationMethod = fromValue(repo.getAuthMethod());
        switch (authenticationMethod)
        {

            case BASIC_AUTHENTICATION:

                try
                {
                    AddHelmRepoReq.BasicAuth basicAuth = JacksonUtils.strToObj((String) authInfo, AddHelmRepoReq.BasicAuth.class);
                    repoAddCommand.setUsername(basicAuth.getUsername());
                    repoAddCommand.setPassword(AesCryptoUtils.cbcDecryptStr(basicAuth.getPassword()));
                }
                catch (IOException e)
                {
                   log.error("auth info parse fail: {}", e);
                }

                break;
            case SECRET_AUTHENTICATION:
                try
                {
                    AddHelmRepoReq.SecretAuth secretAuth = JacksonUtils.strToObj((String) authInfo, AddHelmRepoReq.SecretAuth.class);
                    repoAddCommand.setKeyFile(AesCryptoUtils.cbcDecryptStr(secretAuth.getKey()));
                    repoAddCommand.setCertFile(AesCryptoUtils.cbcDecryptStr(secretAuth.getCert()));
                    repoAddCommand.setCaFile(secretAuth.getCaCert());
                }
                catch (IOException e)
                {
                    log.error("auth info parse fail: {}", e);
                }
                break;
            case NOT_AUTHENTICATED:
            default:
        }

        try
        {
            TblRepoInfo tblRepoInfo = new TblRepoInfo();
            DefaultCommandResult result = repoAddCommand.call();
            if (Objects.nonNull(result))
            {
                CommandStatusCode statusCode = result.getStatusCode();
                if (CommandStatusCode.SUCCESS.equals(statusCode))
                {
                    // activation: execute the add operation successfully
                    tblRepoInfo.setStatus(ACTIVATION.value());
                }
                else
                {
                    String statusMessage = result.getStatusMessage();
                    if (StringUtils.hasText(statusMessage))
                    {
                        if (statusMessage.contains("Unauthorized"))
                        {
                            tblRepoInfo.setStatus(AUTHENTICATION_FAILED.value());
                        }
                        else
                        {
                            tblRepoInfo.setStatus(OTHER_ERROR.value());
                        }
                    }
                    else
                    {
                        tblRepoInfo.setStatus(OTHER_ERROR.value());
                    }

                }
            }

            tblRepoInfo.setId(repo.getId());
            tblRepoInfo.setHash(md5Index(repo.getRepoName()));
            helmRepository.updateByPrimaryKeySelective(tblRepoInfo);
        }
        catch (Exception e)
        {
            log.error("failed to execute helm command, error: {}", e);
        }
    }

    private void executeRepoUpdate(TblRepoInfo repo)
    {
        // execute the helm repo update command
        RepoUpdateCommand repoUpdateCommand=new RepoUpdateCommand();
        repoUpdateCommand.setRepoNames(Lists.newArrayList(repo.getRepoName()));
        try
        {
            DefaultCommandResult result = repoUpdateCommand.call();
            if (Objects.nonNull(result))
            {
                CommandStatusCode statusCode = result.getStatusCode();
                if (CommandStatusCode.SUCCESS.equals(statusCode))
                {
                    String md5 = md5Index(repo.getRepoName());
                    if (md5.equals(repo.getHash()))
                    {
                        log.info("repo:{} update done, no change ", repo.getRepoName());
                    }
                    else
                    {
                        // update app & app version & app category bind
                        executeParseRepoIndex(repo, md5);
                    }
                }
            }
        }
        catch (Exception e)
        {
            log.error("failed to execute helm command, error: {}", e);
        }
    }

    private void updateRepoInfo(TblRepoInfo repo, String md5)
    {
        TblRepoInfo tblRepoInfo = new TblRepoInfo();
        tblRepoInfo.setId(repo.getId());
        tblRepoInfo.setStatus(DATA_SYNC.value());
        tblRepoInfo.setHash(md5);
        helmRepository.updateByPrimaryKeySelective(tblRepoInfo);
    }

    private void batchInsertCategoryBindInfos(TblRepoInfo repo, Map<String, Integer> categoryKeywords, SortedMap<String, SortedSet<IndexFile.ChartVersion>> entries)
    {
        try{
            List<TblRepoAppCategoryBindInfo> tblRepoAppCategoryBindInfoList = entries.entrySet().stream().map(entry ->
            {

                Set<String> keywords = entry.getValue().stream().map(chartVersion ->
                {
                    return chartVersion.getKeywords();
                }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet());

                TblRepoAppCategoryBindInfo tblRepoAppCategoryBindInfo = doBuildTblRepoAppCategoryBindInfo(repo, categoryKeywords, entry.getKey(), keywords);
                return tblRepoAppCategoryBindInfo;
            }).filter(Objects::nonNull).collect(Collectors.toList());

            helmRepository.batchRepoAppCategoryBindInfoInsert(tblRepoAppCategoryBindInfoList);
        }
        catch (Exception e)
        {
            log.error("batch insert category bind infos failed:{}", e);
        }

    }

    private  TblRepoAppCategoryBindInfo doBuildTblRepoAppCategoryBindInfo(TblRepoInfo repo, Map<String, Integer> categoryKeywords, String appName, Set<String> keywords)
    {
        TblRepoAppCategoryBindInfo tblRepoAppCategoryBindInfo = new TblRepoAppCategoryBindInfo();
        String repoName = repo.getRepoName();
        if (!CollectionUtils.isEmpty(keywords))
        {
            Integer categoryIdFromMap = keywords.stream().map(keyword ->
            {
                Integer categoryId = categoryKeywords.get(keyword);
                if (Objects.nonNull(categoryId))
                {
                    return categoryId;
                }
                return getUncategorizedCategoryId();
            }).filter(Objects::nonNull).findFirst().orElse(null);

           if (Objects.nonNull(categoryIdFromMap))
           {
               tblRepoAppCategoryBindInfo.setCategoryId(categoryIdFromMap);
           }
        }
        else
        {
            tblRepoAppCategoryBindInfo.setCategoryId(getUncategorizedCategoryId());
        }
        tblRepoAppCategoryBindInfo.setRepoId(repo.getId());
        tblRepoAppCategoryBindInfo.setAppId(repoName + "-" + appName);
        tblRepoAppCategoryBindInfo.setCreateTime(new Date());
        tblRepoAppCategoryBindInfo.setUpdateTime(new Date());
        return tblRepoAppCategoryBindInfo;
    }

    private void batchInsertAppVersionInfos(TblRepoInfo repo, SortedMap<String, SortedSet<IndexFile.ChartVersion>> entries)
    {
        try
        {
            // batch insert app version infos
            entries.entrySet().stream().forEach(entry ->
            {
                String repoName = repo.getRepoName();
                doBatchInsertAppVersionInfos(repoName, entry);
            });
        }
        catch (Exception e)
        {
            log.error("batch insert app version infos failed:{}", e);
        }

    }

    private void doBatchInsertAppVersionInfos(String repoName, Map.Entry<String, SortedSet<IndexFile.ChartVersion>> entry)
    {
        String appName = entry.getKey();
        List<TblRepoAppVersionInfo> tblRepoAppVersionInfoList = entry.getValue().stream().map(chartVersion ->
        {
            return buildTblRepoAppVersionInfo(repoName, appName, chartVersion);
        }).collect(Collectors.toList());
        helmRepository.batchAppVersionInfoInsert(tblRepoAppVersionInfoList);
    }

    private static TblRepoAppVersionInfo buildTblRepoAppVersionInfo(String repoName, String appName, IndexFile.ChartVersion chartVersion)
    {
        TblRepoAppVersionInfo versionInfo = new TblRepoAppVersionInfo();

        versionInfo.setRepoName(repoName);
        versionInfo.setAppId(repoName + "-" + appName);
        versionInfo.setAppName(appName);
        versionInfo.setVersion(chartVersion.getVersion());
        versionInfo.setAppInfo(chartVersion);
        versionInfo.setCreateTime(new Date());
        versionInfo.setUpdateTime(new Date());
        return versionInfo;
    }

    private void updateAppVersionInfos(String repoName, Map.Entry<String, SortedSet<IndexFile.ChartVersion>> entry)
    {
        String appName = entry.getKey();
        entry.getValue().stream().forEach(chartVersion ->
        {
            List<TblRepoAppVersionInfo> tblRepoAppVersionInfos = helmRepository.selectAppVersionInfos(repoName, appName, chartVersion.getVersion());
            if (CollectionUtils.isEmpty(tblRepoAppVersionInfos))
            {
                TblRepoAppVersionInfo versionInfo = buildTblRepoAppVersionInfo(repoName, appName, chartVersion);
                helmRepository.insertAppVersionSelective(versionInfo);
            }
            else
            {
                TblRepoAppVersionInfo versionInfo = tblRepoAppVersionInfos.get(0);
                versionInfo.setAppInfo(chartVersion);
                versionInfo.setUpdateTime(new Date());
                helmRepository.updateAppVersionByPrimaryKeySelective(versionInfo);
            }
        });
    }

    private void batchInsertAppInfos(TblRepoInfo repo, SortedMap<String, SortedSet<IndexFile.ChartVersion>> entries)
    {
        try {
            // batch insert app infos
            List<TblRepoAppInfo> tblRepoAppInfos = entries.entrySet().stream().map(entry ->
            {
                return buildTblRepoAppInfo(repo, entry);
            }).collect(Collectors.toList());
            helmRepository.batchAppInfoInsert(tblRepoAppInfos);
        }
       catch (Exception e)
       {
           log.error("batch insert app infos failed:{}", e);
       }
    }

    private static TblRepoAppInfo buildTblRepoAppInfo(TblRepoInfo repo, Map.Entry<String, SortedSet<IndexFile.ChartVersion>> entry)
    {
        String appName = entry.getKey();
        List<String> versions = entry.getValue().stream().map(chartVersion -> chartVersion.getVersion()).collect(Collectors.toList());
        TblRepoAppInfo tblRepoAppInfo = new TblRepoAppInfo();
        String repoName = repo.getRepoName();
        tblRepoAppInfo.setAppId(repoName + "-" + appName);
        tblRepoAppInfo.setAppName(appName);
        tblRepoAppInfo.setRepoName(repoName);
        tblRepoAppInfo.setVersions(versions);
        tblRepoAppInfo.setCreateTime(new Date());
        tblRepoAppInfo.setUpdateTime(new Date());
        return tblRepoAppInfo;
    }

    private Map<String, Integer> getCategoryKeywords()
    {
        Map<String, Integer> categoryMap = new HashMap<>();
        List<TblRepoCategoryInfo> tblRepoCategoryInfos = helmRepository.selectAllCategoryList(null);
        if (!CollectionUtils.isEmpty(tblRepoCategoryInfos))
        {
            tblRepoCategoryInfos.forEach(tblRepoCategoryInfo -> {
                Integer categoryId = tblRepoCategoryInfo.getCategoryId();
                String keyWord = tblRepoCategoryInfo.getKeyWord();
                if (StringUtils.hasText(keyWord))
                {
                    try
                    {
                        List<String> keywords = JacksonUtils.strToObjType(keyWord, new TypeReference<List<String>>(){});
                        if (!CollectionUtils.isEmpty(keywords))
                        {
                            keywords.forEach(keyword -> categoryMap.put(keyword, categoryId));
                        }
                    }
                    catch (IOException e)
                    {
                       log.error("get category keyword failed: {}", e);
                    }
                }
            });
        }

        return categoryMap;
    }

    public Integer selectUncategorizedCategoryId()
    {
        TblRepoCategoryInfo tblRepoCategoryInfo = helmRepository.selectUncategorizedCategory();
        if (Objects.nonNull(tblRepoCategoryInfo))
        {
            return tblRepoCategoryInfo.getCategoryId();
        }

        return null;
    }

    public RepoInfo getRepo(String repoName)
    {
        List<TblRepoInfo> tblRepoInfos = helmRepository.selectByRepoName(repoName);
        if (!CollectionUtils.isEmpty(tblRepoInfos))
        {
            return RepoInfo.of(tblRepoInfos.get(0));
        }

        return null;
    }

    public ChartRsp getChartList(String repoName, String appName, String category, Integer pageNum, Integer pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<String> repoNameList = Collections.emptyList();
        if (StringUtils.hasText(repoName))
        {
            repoNameList = Lists.newArrayList(repoName.split(","));
        }
        List<TblRepoAppInfo> chartInfos = helmRepository.selectAllChartList(repoNameList, appName, category);
        PageInfo<TblRepoAppInfo> pageInfo = new PageInfo<>(chartInfos);
        return ChartRsp.builder().charts(chartInfos.stream().map(chartInfo -> ChartInfo.of(chartInfo, helmRepository, this)).collect(Collectors.toList()))
                .totalNum(pageInfo.getTotal()).build();
    }

    public ChartInfo getChart(String appId)
    {
        TblRepoAppInfo tblRepoAppInfo = helmRepository.selectAppInfoByPrimaryKey(appId);
        return ChartInfo.of(tblRepoAppInfo, helmRepository, this);
    }

    public void deleteChart(String appId, String userId, String bpId)
    {
        TblRepoAppInfo tblRepoAppInfo = selectAppInfoByPrimaryKey(appId);
        String repoName = tblRepoAppInfo.getRepoName();
        if (StringUtils.hasText(repoName))
        {
            TblRepoInfo repoInfo = getRepoInfoByRepoName(repoName);
            checkDeleteAccess(repoInfo);
            helmRepository.deleteAppCategoryBindInfo(repoInfo.getId(), tblRepoAppInfo.getAppId());
            helmRepository.deleteAppVersion(repoInfo.getRepoName(), tblRepoAppInfo.getAppName(), null);
            helmRepository.deleteAppInfoByRepoNameAndAppName(repoInfo.getRepoName(), tblRepoAppInfo.getAppName());
            deleteRemoteRepoChart(tblRepoAppInfo, repoInfo);

        }
        else
        {
            log.warn("delete chart:{} fail, cant find repo info", appId);
        }

    }

    public List<CategoryStatsInfo> getChartStats(String repoName, String category)
    {
        List<String> repoNameList = Collections.emptyList();
        List<String> categoryList = Collections.emptyList();

        if (StringUtils.hasText(repoName))
        {
           repoNameList = Lists.newArrayList(repoName.split(","));
        }

        if (StringUtils.hasText(category))
        {
            categoryList = Lists.newArrayList(category.split(","));
        }

        return helmRepository.selectAllChartStats(repoNameList, categoryList);
    }


    public List<ChartVersionInfo> getChartVersions(String appId)
    {
        List<TblRepoAppVersionInfo> tblRepoAppVersionInfos = helmRepository.selectAppVersionsByAppId(appId);
        if (CollectionUtils.isEmpty(tblRepoAppVersionInfos))
        {
            return Collections.emptyList();
        }

        List<ChartVersionInfo> res = tblRepoAppVersionInfos.stream().map(chartVersionInfo -> ChartVersionInfo.of(chartVersionInfo, this)).collect(Collectors.toList());

        return res;
    }

    @Transactional(rollbackFor = Exception.class)
    public Object installStack(InstallStackReq installStackReq)
    {
        String name = installStackReq.getName();
        List<InstallStackReq.ClusterInfo> targetClusters = installStackReq.getTargetClusters();

        if (checkStackByStackNameExists(name))
        {
            throw new WebSystemException(HELM_STACK_NAME_EXIST, ERROR);
        }

        TblRepoAppInfo tblRepoAppInfo = selectAppInfoByPrimaryKey(installStackReq.getAppId());

        checkTargetClusterInfo(targetClusters);

        List<String> res = new ArrayList<>();
        targetClusters.stream().forEach( clusterInfo -> {
            if (checkStackNameExists(name, clusterInfo))
            {
                throw new WebSystemException(HELM_STACK_NAME_EXIST, ERROR);
            }

            String stackId = insertInstallStackInfo(installStackReq, clusterInfo);
            res.add(stackId);
            CompletableFuture.runAsync(() -> helmInstall(installStackReq, clusterInfo, tblRepoAppInfo, stackId, true));
            ;
        });

        return res.stream().collect(Collectors.joining(","));
    }


    public TblRepoAppInfo selectAppInfoByPrimaryKey(String appId)
    {
        TblRepoAppInfo tblRepoAppInfo = helmRepository.selectAppInfoByPrimaryKey(appId);
        if (Objects.isNull(tblRepoAppInfo))
        {
            throw new WebSystemException(HELM_APP_INFO_NOT_EXIST, ERROR);
        }

        return tblRepoAppInfo;
    }


    public StackRsp getStackList(String stackName, String clusterIds, String category, String bpId, String userId, Integer pageNum,
                                 Integer pageSize)
    {
        List<String> clusterIdList = new ArrayList<>();
        if (StringUtils.hasText(clusterIds))
        {
            clusterIdList = Lists.newArrayList(clusterIds.split(","));
        }
        PageHelper.startPage(pageNum, pageSize);
        List<TblHelmStackInfo> stackInfos = helmRepository.selectAllStackList(stackName, clusterIdList, category, bpId, userId);
        PageInfo<TblHelmStackInfo> pageInfo = new PageInfo<>(stackInfos);
        return StackRsp.builder().apps(stackInfos.stream().parallel().map(info -> HelmStackInfo.of(info, helmRepository)).collect(Collectors.toList()))
                .totalNum(pageInfo.getTotal()).build();
    }

    public HelmStackInfo getStack(String stackId)
    {
        TblHelmStackInfo tblHelmStackInfo = helmRepository.selectStackInfoByPrimaryKey(stackId);
        return HelmStackInfo.of(tblHelmStackInfo, helmRepository);
    }

    public ChartVersionFilesRsp getChartVersionFiles(String appId, String version)
    {
        Map<String, String> files = new HashMap<>();

        TblRepoAppVersionInfo versionInfo = helmRepository.selectAppVersionByAppIdAndVersion(appId, version);


        if (Objects.nonNull(versionInfo))
        {
            String appName = versionInfo.getAppName();
            String repoName = versionInfo.getRepoName();

            // if it has been pulled before get it directly
            String path = helmConfig.getHelmChartTarHome() + FILE_SEPARATOR + repoName + FILE_SEPARATOR + appName + FILE_SEPARATOR + version;
            if (!Files.exists(Paths.get(path)))
            {
                pullPackage(version, appName, repoName);
                // wait for the pull to complete  A better approach is to try to detect the existence of the file multiple times
                //sleep(100);
            }
            files = FileUtils.listFilesContentWithBase64Encode(path, true);
        }

        return ChartVersionFilesRsp.builder().files(files).version(version).appId(appId).build();
    }

    public void deleteStack(String stackId, String userId, String bpId)
    {
        TblHelmStackInfo stackInfo = getStackByStackId(stackId);

        CompletableFuture.runAsync(() -> {
            boolean uninstallSuccessful = doUninstallStack(stackInfo);
            if (!uninstallSuccessful)
            {
                try
                {
                    String stackInfoStr = JacksonUtils.objToStr(stackId);
                    RedisUtil.sadd(HELM_STACK_TO_BE_DELETED, "", stackInfoStr);
                }
                catch (IOException e)
                {
                    log.error("stack info serialization failed:{}", e);
                }

            }
        });

        helmRepository.deleteByPrimaryKey(stackId);
    }

    public void ctrStack(String stackId, int action)
    {
        TblHelmStackInfo stackInfo = getStackByStackId(stackId);

        TblHelmStackInfo.ActionType actionType = TblHelmStackInfo.ActionType.fromValue(action);
        switch (actionType)
        {
            case UPGRADE:
                doUpgradeStack(stackInfo);
                break;
            case ROLLBACK:
                doRollbackStack(stackInfo);
                break;
            default:
                throw new WebSystemException(HELM_STACK_ACTION_TYPE_ERROR, ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public ImportChartRsp importCharts(String repoName, ImportChartReq importChartReq)
    {
        TblRepoInfo repoInfo = getRepoInfoByRepoName(repoName);

        String targetPath = helmConfig.getHelmChartTarHome() + FILE_SEPARATOR + repoName + FILE_SEPARATOR + "importCharts" + FILE_SEPARATOR + Utils.assignUUId();
        Path targetDir = Paths.get(targetPath);
        String tarFilePath = doDeCompressTarGzip(targetDir, repoName, importChartReq);

        ChartVersionInfo chartVersionInfo = doParseChartVersionInfo(targetDir);
        List<String> urls = chartVersionInfo.getUrls();
        if (!CollectionUtils.isEmpty(urls))
        {
            List<String> collect = urls.stream().map(url -> repoInfo.getRepoUrl() + "/" + url).collect(Collectors.toList());
        }
        else
        {
            chartVersionInfo.setUrls(Lists.newArrayList(repoInfo.getRepoUrl() + "/" + "charts/" + chartVersionInfo.getName() + "-" + chartVersionInfo.getVersion() + ".tgz"));
        }
        checkPushAccess(repoInfo);

        pushToRepo(repoName, targetDir, tarFilePath);

        String appName = chartVersionInfo.getName();
        String version = chartVersionInfo.getVersion();
        Set<String> keywords = Sets.newHashSet(chartVersionInfo.getKeywords());
        SortedMap<String, SortedSet<IndexFile.ChartVersion>> entries = new TreeMap<String,SortedSet<IndexFile.ChartVersion>>();
        TreeSet<IndexFile.ChartVersion> chartVersions = new TreeSet<>();
        chartVersions.add(chartVersionInfo);
        entries.put(appName,chartVersions);
        Map.Entry<String, SortedSet<IndexFile.ChartVersion>> entry = entries.entrySet().iterator().next();
        insertRepoAppInfo(repoInfo, keywords, entry, Lists.newArrayList(version));

        ImportChartRsp rsp = ImportChartRsp.builder().description(chartVersionInfo.getDescription())
                .name(appName).version(version + "[" + chartVersionInfo.getAppVersion() + "]")
                .build();
        return rsp;
    }

    public Integer countHelmStack(String ownerBpId,String ownerId)
    {
        return helmRepository.countHelmStack(ownerBpId,ownerId);
    }

    public void checkHelmStackStatus()
    {
        List<TblHelmStackInfo> tblHelmStackInfos = helmRepository.selectAllStackList(null, null, null, null, null);
        if (!CollectionUtils.isEmpty(tblHelmStackInfos))
        {
            tblHelmStackInfos.stream().forEach(tblHelmStackInfo -> {
                String releaseName = tblHelmStackInfo.getReleaseName();
                ListCommand listCommand = new ListCommand();
                String clusterId = tblHelmStackInfo.getClusterId();
                String clusterName = tblHelmStackInfo.getClusterName();
                if (StringUtils.isEmpty(clusterId))
                {
                    log.warn("helm stack cluster id:{} cluster name: {} is empty so there is no way to know the state", clusterId, clusterName);
                    return;
                }
                String kubeConfigPath = getKubeConfig(clusterId);
                listCommand.setKubeConfig(kubeConfigPath);
                listCommand.setAllNamespace(true);
                listCommand.setFilter(releaseName);
                listCommand.setOutput(JSON.getType());
                try
                {
                    ListCommandResult<String> result = listCommand.call();
                    if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
                    {
                        String data = result.getData();
                        log.info("helm list result:{}", data);
                        List<ListCommand.ListResult> list = JacksonUtils.strToObjType(data, new TypeReference<List<ListCommand.ListResult>>(){});
                        if (!CollectionUtils.isEmpty(list))
                        {
                           list.stream().forEach(listResult -> {
                               if (releaseName.equals(listResult.getName()))
                               {
                                   TblHelmStackInfo stackInfo= new TblHelmStackInfo();
                                   stackInfo.setStackId(tblHelmStackInfo.getStackId());
                                   String status = listResult.getStatus();
                                   if (StatusDeployed.getName().equals(status))
                                   {
                                       // use kubectl to confirm the status of the pod
                                       getStatusByKubectlCommand(tblHelmStackInfo, kubeConfigPath, stackInfo);
                                   }
                                   else
                                   {
                                       stackInfo.setStatus(releaseStatus2StackStatus(status));
                                   }

                                   if (Objects.nonNull(stackInfo.getStatus()))
                                   {
                                       helmRepository.updateStackByPrimaryKeySelective(stackInfo);
                                   }
                               }
                           });
                        }
                    }
                }
                catch (Exception e)
                {
                    log.error("execute helm list error:{}", e);
                }
            });

        }
    }

    public void checkHelmStackIsDeleted()
    {
        Long nodeIds =  RedisUtil.scard(HELM_STACK_TO_BE_DELETED);
        if (nodeIds.longValue() > 0)
        {
            RedisUtil.smembers(HELM_STACK_TO_BE_DELETED).stream().forEach(stackInfoStr -> {
                TblHelmStackInfo tblHelmStackInfo = null;
                try
                {
                    tblHelmStackInfo = JacksonUtils.strToObj(stackInfoStr, TblHelmStackInfo.class);
                }
                catch (IOException e)
                {
                    log.error("helm stack parse error:{}", e);
                }

                if (Objects.nonNull(tblHelmStackInfo))
                {
                    boolean uninstallSuccessful  = doUninstallStack(tblHelmStackInfo);
                    if (uninstallSuccessful)
                    {
                        RedisUtil.srem(HELM_STACK_TO_BE_DELETED, "", stackInfoStr);
                    }
                }
            });
        }
    }

    public TblRepoInfo getRepoInfoByRepoName(String repoName)
    {
        List<TblRepoInfo> tblRepoInfos = helmRepository.selectByRepoName(repoName);
        if (CollectionUtils.isEmpty(tblRepoInfos))
        {
            throw new WebSystemException(HELM_REPO_NOT_EXIST, ERROR);
        }

        return tblRepoInfos.get(0);
    }

    private void insertRepoAppInfo(TblRepoInfo repoInfo, Set<String> keywords, Map.Entry<String, SortedSet<IndexFile.ChartVersion>> entry, List<String> versions)
    {
        String repoName = repoInfo.getRepoName();
        String appName = entry.getKey();
        List<TblRepoAppInfo> tblRepoAppInfos = helmRepository.selectAppInfosAllPrecise(repoName, appName);
        if (CollectionUtils.isEmpty(tblRepoAppInfos))
        {
            TblRepoAppInfo tblRepoAppInfo= new TblRepoAppInfo();
            tblRepoAppInfo.setAppId(repoName + "-" + appName);
            tblRepoAppInfo.setAppName(appName);
            tblRepoAppInfo.setRepoName(repoName);
            tblRepoAppInfo.setVersions(versions);
            tblRepoAppInfo.setCreateTime(new Date());
            tblRepoAppInfo.setUpdateTime(new Date());
            helmRepository.insertAppInfoSelective(tblRepoAppInfo);
            doBatchInsertAppVersionInfos(repoName, entry);
            TblRepoAppCategoryBindInfo tblRepoAppCategoryBindInfo = doBuildTblRepoAppCategoryBindInfo(repoInfo, getCategoryKeywords(), appName, keywords);
            helmRepository.insertAppCategoryBindInfoSelective(tblRepoAppCategoryBindInfo);
        }
        else
        {
            TblRepoAppInfo tblRepoAppInfo = tblRepoAppInfos.get(0);
            try
            {
                List<String> versionsInDB = JacksonUtils.strToObjType((String) tblRepoAppInfo.getVersions(), new TypeReference<List<String>>(){});

                versionsInDB.addAll(versions);
                List<String> collect = versionsInDB.stream().distinct().collect(Collectors.toList());
                tblRepoAppInfo.setVersions(collect);
            }
            catch (IOException e)
            {
                log.error("parse versions fail: {}", e);
                tblRepoAppInfo.setVersions(versions);
            }
            tblRepoAppInfo.setUpdateTime(new Date());
            helmRepository.updateAppInfoByPrimaryKeySelective(tblRepoAppInfo);
            // update appVersion
            updateAppVersionInfos(repoName, entry);

        }
    }

    private  void pushToRepo(String repoName, Path targetDir, String tarFilePath)
    {
        File chartDir = Arrays.stream(targetDir.toFile().listFiles()).filter(file -> file.isDirectory()).findFirst().get();
        if (helmConfig.isHelmPush())
        {
            TblRepoInfo repoInfo = getRepoInfoByRepoName(repoName);
            // login first then push to  registry
            registryLogin(repoInfo);
            pushToRegistry(tarFilePath, repoInfo);
        }
        else
        {
            CmPushCommand cmPushCommand = new CmPushCommand();

            cmPushCommand.setPackagePath(chartDir.getAbsolutePath());
            cmPushCommand.setInsecure(true);
            cmPushCommand.setRepoName(repoName);
            try
            {
                DefaultCommandResult result = cmPushCommand.call();
                if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
                {
                    log.info("execute helm cm-push result success:{}", result.getStatusMessage());
                }
                else
                {
                    log.error("execute helm cm-push result fail:{}", result.getStatusMessage());
                }
            }
            catch (Exception e)
            {
                log.error("execute helm cm-push error:{}", e);
                throw new WebSystemException(HELM_COMMAND_EXECUTION_ERROR, ERROR);
            }
        }

    }

    private static void pushToRegistry(String tarFilePath, TblRepoInfo repoInfo)
    {
        PushCommand pushCommand = new PushCommand();
        pushCommand.setPackagePath(tarFilePath);
        pushCommand.setRemote(repoInfo.getRepoUrl());
        pushCommand.setInsecureSkipTlsVerify(true);
        try
        {
            DefaultCommandResult result = pushCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                log.info("execute helm push result success:{}", result.getStatusMessage());
            }
            else
            {
                log.error("execute helm push result fail:{}", result.getStatusMessage());
            }
        }
        catch (Exception e)
        {
            log.error("execute helm push error:{}", e);
            throw new WebSystemException(HELM_COMMAND_EXECUTION_ERROR, ERROR);
        }
    }

    private void registryLogin(TblRepoInfo repoInfo)
    {
        RegistryLoginCommand registryLoginCommand = new RegistryLoginCommand();
        registryLoginCommand.setInsecure(true);
        registryLoginCommand.setHost(repoInfo.getRepoUrl());
        TblRepoInfo.AuthenticationMethod authenticationMethod = fromValue(repoInfo.getAuthMethod());
        if (BASIC_AUTHENTICATION.equals(authenticationMethod))
        {
            try
            {
                AddHelmRepoReq.BasicAuth basicAuth = JacksonUtils.strToObj((String) repoInfo.getAuthInfo(), AddHelmRepoReq.BasicAuth.class);
                registryLoginCommand.setUsername(basicAuth.getUsername());
                registryLoginCommand.setPassword(AesCryptoUtils.cbcDecryptStr(basicAuth.getPassword()));
            }
            catch (IOException e)
            {
                log.error("auth info parse fail: {}", e);
            }
        }

        try
        {
            DefaultCommandResult result = registryLoginCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                log.info("execute helm registry login result success:{}", result.getStatusMessage());
            }
            else
            {
                log.error("execute helm registry login result fail:{}", result.getStatusMessage());
            }
        }
        catch (Exception e)
        {
            log.error("execute helm registry login error:{}", e);
            throw new WebSystemException(HELM_COMMAND_EXECUTION_ERROR, ERROR);
        }
    }

    private static void checkPushAccess(TblRepoInfo repoInfo)
    {
        try
        {
            List<String> access = JacksonUtils.strToObjType((String) repoInfo.getAccess(), new TypeReference<List<String>>(){});
            if (!access.contains(TblRepoInfo.AccessType.PUSH.getVerb()))
            {
                throw new WebSystemException(HELM_REPO_ACCESS_NOT_ALLOWED, ERROR);
            }
        }
        catch (IOException e)
        {
            log.error("parse access fail: {}", e);
        }
    }

    private static void checkDeleteAccess(TblRepoInfo repoInfo)
    {
        try
        {
            List<String> access = JacksonUtils.strToObjType((String) repoInfo.getAccess(), new TypeReference<List<String>>(){});
            if (!access.contains(TblRepoInfo.AccessType.REMOVE.getVerb()))
            {
                throw new WebSystemException(HELM_REPO_ACCESS_NOT_ALLOWED, ERROR);
            }
        }
        catch (IOException e)
        {
            log.error("parse access fail: {}", e);
        }
    }

    private static ChartVersionInfo doParseChartVersionInfo(Path targetDir)
    {
        ChartVersionInfo chartVersionInfo = null;
        try
        {
            Stream<Path> walk = Files.walk(targetDir, 2);
            Path chartFile = walk.filter(file -> file.toFile().getName().contains("Chart.yaml")).findFirst().get();
            chartVersionInfo = YamlParserUtil.readYamlConfig(chartFile.toFile().getPath(), ChartVersionInfo.class);
        }
        catch (IOException e)
        {
           log.error("parse chart yaml fail: {}", e);
           throw new WebSystemException(PARSE_CHART_YAML_FAIL, ERROR);
        }

        log.info("import chart info: {}", chartVersionInfo);
        return chartVersionInfo;
    }

    private String doDeCompressTarGzip(Path targetDir, String repoName, ImportChartReq importChartReq)
    {
        String tarFilePath = targetDir.toAbsolutePath().toString() + ".tgz";
        byte[] packageContent = Base64Utils.decode(importChartReq.getPackageContent());

        try
        {
            Files.deleteIfExists(targetDir);
            Path path = Paths.get(tarFilePath);

            if (Files.exists(path)) {
                Files.delete(path);
            }

            Files.write(path, packageContent);
            TarGzipUtils.deCompressTarGzip(packageContent, targetDir);
        }
        catch (IOException e)
        {
            log.error("deCompress tarGzip file error: {}", e);
        }

        return tarFilePath;
    }

    public void helmInstall(InstallStackReq installStackReq, InstallStackReq.ClusterInfo clusterInfo,  TblRepoAppInfo tblRepoAppInfo, String stackId, boolean retry)
    {

        TblHelmStackInfo tblHelmStackInfo = new TblHelmStackInfo();
        tblHelmStackInfo.setStackId(stackId);

        InstallCommand installCommand = new InstallCommand();
        installCommand.setReleaseName(installStackReq.getName());
        String chartName = tblRepoAppInfo.getRepoName() + "/" + tblRepoAppInfo.getAppName();
        installCommand.setChartDirectory(chartName);
        installCommand.setNamespace(clusterInfo.getNamespace());
        installCommand.setVersion(installStackReq.getChartVersion());
        String kubeConfigPath = getKubeConfig(clusterInfo.getClusterId());
        installCommand.setKubeConfig(kubeConfigPath);
        installCommand.setInsecureSkipTlsVerify(true);
        installCommand.setValues(getValuesPath(installStackReq.getStackCompose()));

        try
        {
            InstallCommandResult result = installCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                log.info("execute helm install success:{}", result.getStatusMessage());
            }
            else
            {
                String statusMessage = result.getStatusMessage();
                if (StringUtils.hasText(statusMessage))
                {
                    if (statusMessage.contains("Kubernetes cluster unreachable"))
                    {
                        //kube config problem try again
                        RedisUtil.delete(String.format(KUBE_CONFIG_PATH, clusterInfo.getClusterId()));
                        if (retry)
                        {
                            helmInstall(installStackReq, clusterInfo, tblRepoAppInfo, stackId, false);
                        }

                    }
                }
                log.error("execute helm install fail:{}", result.getStatusMessage());
                tblHelmStackInfo.setStatus(DEPLOY_FAILED.value());
                tblHelmStackInfo.setUpdateTime(new Date());
                helmRepository.updateStackByPrimaryKeySelective(tblHelmStackInfo);
            }
        }
        catch (Exception e)
        {
            log.error("execute helm install error:{}", e);
            throw new WebSystemException(HELM_COMMAND_EXECUTION_ERROR, ERROR);
        }


    }

    private String getKubeConfig(String clusterId)
    {
        String kubeConfigPath = RedisUtil.get(String.format(KUBE_CONFIG_PATH, clusterId));
        if (StringUtils.hasText(kubeConfigPath))
        {
            return kubeConfigPath;
        }

        String kubeConfig = "";
        try
        {
            kubeConfig = combRpcService.getClusterManagerService().getKubeConfig(clusterId, null);
        }
       catch (Exception e)
       {
           log.error("get clusterId:{} kubeConfig fail:{}", clusterId, e);
       }

        if (StringUtils.hasText(kubeConfig))
        {
            kubeConfigPath = getKubeConfigPath(clusterId, kubeConfig);
        }

        if (StringUtils.isEmpty(kubeConfigPath))
        {
            log.error("helm install lack kube config, cluster id: {}", clusterId);
            throw new WebSystemException(HELM_INSTALL_LACK_KUBE_CONFIG, ERROR);
        }

        return kubeConfigPath;
    }

    private void checkTargetClusterInfo(List<InstallStackReq.ClusterInfo> targetClusters)
    {
        if (CollectionUtils.isEmpty(targetClusters))
        {
            throw new WebSystemException(HELM_CHART_INSTALL_MISSING_CLUSTER_INFO, ERROR);
        }
    }

    private String insertInstallStackInfo(InstallStackReq installStackReq, InstallStackReq.ClusterInfo clusterInfo)
    {
        TblHelmStackInfo tblHelmStackInfo = new TblHelmStackInfo();
        BeanUtils.copyProperties(installStackReq, tblHelmStackInfo);
        String name = installStackReq.getName();
        tblHelmStackInfo.setStackId(PREFIX + clusterInfo.getClusterId() + "-" + name);
        tblHelmStackInfo.setReleaseName(name);

        String appId = installStackReq.getAppId();
        TblRepoAppInfo tblRepoAppInfo = selectAppInfoByPrimaryKey(appId);
        String appName = tblRepoAppInfo.getAppName();
        tblHelmStackInfo.setChartName(appName);
        tblHelmStackInfo.setNamespace(clusterInfo.getNamespace());
        tblHelmStackInfo.setClusterId(clusterInfo.getClusterId());
        tblHelmStackInfo.setClusterName(clusterInfo.getClusterName());

        tblHelmStackInfo.setStatus(CREATING.value());
        tblHelmStackInfo.setCreateTime(new Date());
        tblHelmStackInfo.setUpdateTime(tblHelmStackInfo.getCreateTime());

        tblHelmStackInfo.setOwnerId(installStackReq.getUserId());
        tblHelmStackInfo.setOwnerName(installStackReq.getUserName());
        tblHelmStackInfo.setCreatorId(getUserId());
        tblHelmStackInfo.setCreatorName(getUserName());
        tblHelmStackInfo.setOwnerBpId(installStackReq.getBpId());
        tblHelmStackInfo.setOwnerBpName(installStackReq.getBpName());
        helmRepository.insertStackInfoSelective(tblHelmStackInfo);
        return tblHelmStackInfo.getStackId();
    }

    private boolean checkStackNameExists(String name, InstallStackReq.ClusterInfo clusterInfo)
    {
        List<TblHelmStackInfo> tblHelmStackInfos = helmRepository.selectStackByChartName(name);
        if (!CollectionUtils.isEmpty(tblHelmStackInfos))
        {
            return true;
        }

        // verify that it is installed in the cluster
        ListCommand listCommand = new ListCommand();
        String kubeConfigPath = getKubeConfig(clusterInfo.getClusterId());
        listCommand.setKubeConfig(kubeConfigPath);
        listCommand.setAllNamespace(true);
        listCommand.setFilter(name);
        listCommand.setOutput(JSON.getType());
        try
        {
            ListCommandResult<String> result = listCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                String data = result.getData();
                log.info("helm list result:{}", data);
                List<ListCommand.ListResult> list = JacksonUtils.strToObjType(data, new TypeReference<List<ListCommand.ListResult>>(){});
                if (!CollectionUtils.isEmpty(list))
                {
                    return true;
                }
            }
        }
        catch (Exception e)
        {
            log.error("execute helm list error:{}", e);
        }
        return false;
    }

    private boolean checkRepoNameExists(String repoName)
    {
        List<TblRepoInfo> tblRepoInfos = helmRepository.selectByRepoName(repoName);
        if (!CollectionUtils.isEmpty(tblRepoInfos))
        {
            return true;
        }

        return false;
    }


    private void checkAccess(List<String> accesses)
    {
        if (CollectionUtils.isEmpty(accesses))
        {
            accesses.add(TblRepoInfo.AccessType.LIST.getVerb());
        }
        else
        {
            for (String access: accesses)
            {
                boolean contain = TblRepoInfo.AccessType.contain(access);
                if (!contain)
                {
                    throw new WebSystemException(HELM_REPO_ACCESS_INCORRECT, ERROR);
                }
            }
        }

    }


    private void pullPackage(String version, String appName, String repoName)
    {

        PullCommand pullCommand = new PullCommand();
        pullCommand.setRepoChartName(repoName + "/" + appName);
        pullCommand.setVersion(version);
        pullCommand.setUnTar(true);
        pullCommand.setDestination(helmConfig.getHelmChartTarHome());
        pullCommand.setUnTarDir(repoName + FILE_SEPARATOR + appName + FILE_SEPARATOR + version);
        pullCommand.setInsecureSkipTlsVerify(true);

        try
        {
            DefaultCommandResult result = pullCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                log.info("pull package success");
            }
            else
            {
                log.error("pull package failed: {}", result.getStatusMessage());
                ociPull(appName, repoName, pullCommand);
            }
        }
        catch (Exception e)
        {
            ociPull(appName, repoName, pullCommand);
            log.error("execute helm pull error:{}", e);
        }
    }

    private void ociPull(String appName, String repoName, PullCommand pullCommand)
    {
        DefaultCommandResult result;
        String repoUrl;
        RepoInfo repo = getRepo(repoName);
        if (Objects.nonNull(repo))
        {
            repoUrl = repo.getRepoUrl();
            if (StringUtils.hasText(repoUrl))
            {
                pullCommand.setChartUrl(repoUrl + "/" + appName);
                try
                {
                    result = pullCommand.call();
                    if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
                    {
                        log.info("pull package success");
                    }
                    else
                    {
                        log.error("pull package failed: {}", result.getStatusMessage());
                    }
                }
                catch (Exception e)
                {
                    log.error("execute helm pull error:{}", e);
                }
            }
        }
    }

    private static void sleep(int time)
    {
        try
        {
            TimeUnit.MICROSECONDS.sleep(time);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    private boolean checkStackByStackNameExists(String stackName)
    {
        List<TblHelmStackInfo> tblHelmStackInfos = helmRepository.selectStacksAllByReleaseName(stackName);
        if (!CollectionUtils.isEmpty(tblHelmStackInfos))
        {
            return true;
        }

        return false;
    }

    private TblHelmStackInfo getStackByStackId(String stackId)
    {
        TblHelmStackInfo tblHelmStackInfo = helmRepository.selectStackByPrimaryKey(stackId);
        if (Objects.isNull(tblHelmStackInfo))
        {
            throw new WebSystemException(HELM_STACK_NOT_EXIST, ERROR);
        }

        return tblHelmStackInfo;
    }

    private boolean checkStackByStackIdExists(String stackId)
    {
        TblHelmStackInfo tblHelmStackInfos = helmRepository.selectStackByPrimaryKey(stackId);
        if (Objects.nonNull(tblHelmStackInfos))
        {
            return true;
        }

        return false;
    }


    private Integer getUncategorizedCategoryId()
    {
        if (Objects.isNull(uncategorizedCategoryId))
        {
            this.uncategorizedCategoryId = helmRepository.selectUncategorizedCategory().getCategoryId();
        }
        return uncategorizedCategoryId;
    }

    private void doRollbackStack(TblHelmStackInfo stackInfo)
    {
    }

    private void doUpgradeStack(TblHelmStackInfo stackInfo)
    {

    }

    private String getKubeConfigPath(String clusterId, String config)
    {
        String kubeConfigPath = "";

        try
        {
            Path path = Paths.get(helmConfig.getHelmCacheHome() +  File.separator + "kube-config" + File.separator + clusterId +  File.separator + "config");

            if (Files.exists(path))
            {
                try {
                    BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
                    long ms = new Date().getTime() - attr.lastModifiedTime().toMillis();
                    // update kube config in more than  hours
                    if (ms < 1000 * KUBE_CONFIG_EXPIRED)
                    {
                        // File not expired, return
                        return path.toFile().getAbsolutePath();
                    }


                } catch (IOException ex) {
                   log.error("get file attr error:", ex);
                }

            }

            if (!Files.exists(path.getParent()))
            {
                Files.createDirectories(path.getParent());
            }

            RandomAccessFile stream = new RandomAccessFile(path.toFile(), "rw");
            FileChannel channel = stream.getChannel();

            FileLock lock = null;
            try {
                lock = channel.tryLock();
            } catch (final OverlappingFileLockException e) {
                stream.close();
                channel.close();
            }
            ByteBuffer buff = ByteBuffer.wrap(config.getBytes(StandardCharsets.UTF_8));
            channel.write(buff);
            lock.release();

            stream.close();
            channel.close();
            kubeConfigPath = path.toFile().getAbsolutePath();
            setPerms(path);
            RedisUtil.set(String.format(KUBE_CONFIG_PATH, clusterId), kubeConfigPath, KUBE_CONFIG_EXPIRED);
            return kubeConfigPath;
        }
        catch (Exception e)
        {
            log.error("get kube config file error: {}", e);
        }

        return "";
    }

    private String getValuesPath(String values)
    {
        try
        {
            String tmpdir = Files.createTempDirectory("helm-values").toFile().getAbsolutePath();
            String valuesPath = tmpdir + File.separator + "values.yaml";
            System.out.println(valuesPath);
            RandomAccessFile stream = new RandomAccessFile(valuesPath, "rw");
            FileChannel channel = stream.getChannel();

            FileLock lock = null;
            try
            {
                lock = channel.tryLock();
            }
            catch (final OverlappingFileLockException e)
            {
                stream.close();
                channel.close();
            }
            ByteBuffer buff = ByteBuffer.wrap(values.getBytes(StandardCharsets.UTF_8));
            channel.write(buff);
            lock.release();

            stream.close();
            channel.close();
            Paths.get(valuesPath).toFile().deleteOnExit();
            return valuesPath;
        }
        catch (Exception e)
        {
            log.error("get chart values file error: {}", e);
        }

        return "";
    }


    private int releaseStatus2StackStatus(String releaseName)
    {
        ReleaseStatus releaseStatus = ReleaseStatus.fromName(releaseName);
        switch (releaseStatus)
        {

            case StatusDeployed:
                return RUNNING.value();
            case StatusUninstalling:
            case StatusUninstalled:
                return STOP.value();
            case StatusFailed:
                return DEPLOY_FAILED.value();
            case StatusPendingInstall:
                return CREATING.value();
            case StatusUnknown:
            case StatusSuperseded:
            case StatusPendingUpgrade:
            case StatusPendingRollback:
                return UNKNOWN.value();
        }
        return  UNKNOWN.value();
    }

    private String getHelmIndexPath(String repoName)
    {
        String helmCacheHome = helmConfig.getHelmCacheHome();
        return  helmCacheHome + File.separator + "repository" + File.separator + repoName + "-index.yaml";
    }

    private String md5Index(String repoName)
    {
        String indexPath = getHelmIndexPath(repoName);
        Path path = Paths.get(indexPath);
        if (Files.exists(path))
        {
            return SecureUtil.md5(path.toFile());
        }
        return "";
    }

    private void cleanAppInfoAndRelatedResources(String repoName, String appName)
    {
        helmRepository.deleteAppVersion(repoName, appName, null);
        helmRepository.deleteAppInfoByRepoNameAndAppName(repoName, appName);
    }


    private void setPerms(Path path)
    {
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        try
        {
            Files.setPosixFilePermissions(path, perms);
        }
        catch (IOException e)
        {
            log.error("set perm for path:{} error", path.toAbsolutePath());
        }
    }

    private static void getStatusByKubectlCommand(TblHelmStackInfo tblHelmStackInfo, String kubeConfigPath, TblHelmStackInfo stackInfo)
    {
        // Use different labelS queries and merge query results
        List<String> podStatusList = new ArrayList<>();
        Map<String, String> labels = new HashMap();
        labels.put("app", tblHelmStackInfo.getChartName());
        labels.put("release", tblHelmStackInfo.getReleaseName());
        List<String> statusList = doPodsGetCommand(tblHelmStackInfo, kubeConfigPath, labels);
        podStatusList.addAll(statusList);
        labels.clear();
        labels.put("app.kubernetes.io/instance", tblHelmStackInfo.getReleaseName());
        statusList = doPodsGetCommand(tblHelmStackInfo, kubeConfigPath, labels);
        podStatusList.addAll(statusList);
        podStatus2StackStatus(tblHelmStackInfo, stackInfo, podStatusList);
    }

    private static  List<String> doPodsGetCommand(TblHelmStackInfo tblHelmStackInfo, String kubeConfigPath, Map<String, String> labels)
    {
        List<String> statusList = new ArrayList<>();
        PodsGetCommand podsGetCommand = new PodsGetCommand();

        podsGetCommand.setSelector(labels);
        podsGetCommand.setAllNamespaces(true);
        podsGetCommand.setOutput(setStatusJsonPath());
        podsGetCommand.setKubeConfig(kubeConfigPath);
        try
        {
            ListCommandResult listCommandResult = podsGetCommand.call();
            if (CommandStatusCode.SUCCESS.equals(listCommandResult.getStatusCode()))
            {
                String resultData = (String)listCommandResult.getData();
                statusList = JacksonUtils.strToObjType(resultData, new TypeReference<List<String>>() {});
            }
            else
            {
                log.error("execute kubectl get status error:{}", listCommandResult.getStatusMessage());
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        return statusList;
    }

    private static void podStatus2StackStatus(TblHelmStackInfo tblHelmStackInfo, TblHelmStackInfo stackInfo, List<String> statusList)
    {
        if (CollectionUtils.isEmpty(statusList))
        {
            log.warn("there is no pod release named :{} in the cluster:{}", tblHelmStackInfo.getReleaseName(), tblHelmStackInfo.getClusterName());
            //stackInfo.setStatus(APP_CLEARED.value());
        }
        else
        {
            long runningCount = statusList.stream().filter(status -> status.equals(PodRunning.getPhase())).count();
            if (runningCount == statusList.size())
            {
                // all pods are in running state
                stackInfo.setStatus(RUNNING.value());
                return;
            }

            if (statusList.contains(PodRunning.getPhase()))
            {
                stackInfo.setStatus(PARTIAL_RUN.value());
                return;
            }

            if (statusList.contains(PodPending.getPhase()))
            {
                stackInfo.setStatus(CREATING.value());
                return;
            }

            if (statusList.contains(PodFailed.getPhase()))
            {
                stackInfo.setStatus(DEPLOY_FAILED.value());
                return;
            }

            if (statusList.contains(PodUnknown.getPhase()))
            {
                stackInfo.setStatus(UNKNOWN.value());
                return;
            }

            if (statusList.contains(PodSucceeded.getPhase()))
            {
                log.warn(" pod release named :{} in the cluster:{}, status:{}", stackInfo.getReleaseName(), stackInfo.getClusterName(), PodSucceeded.getPhase());
                stackInfo.setStatus(APP_CLEARED.value());
            }
        }
    }


    private boolean doUninstallStack(TblHelmStackInfo stackInfo)
    {
        boolean uninstallSuccessful = false;
        UninstallCommand uninstallCommand = new UninstallCommand();
        uninstallCommand.setReleaseName(stackInfo.getReleaseName());
        uninstallCommand.setNamespace(stackInfo.getNamespace());
        String kubeConfigPath = getKubeConfig(stackInfo.getClusterId());
        uninstallCommand.setKubeConfig(kubeConfigPath);
        try
        {
            UninstallCommandResult result = uninstallCommand.call();
            if (CommandStatusCode.SUCCESS.equals(result.getStatusCode()))
            {
                log.info("execute helm uninstall result success:{}", result.getStatusMessage());
                uninstallSuccessful = true;
            }
            else
            {
                log.error("execute helm uninstall result fail:{}", result.getStatusMessage());
            }
        }
        catch (Exception e)
        {
            log.error("execute helm uninstall error:{}", e);
            throw new WebSystemException(HELM_COMMAND_EXECUTION_ERROR, ERROR);
        }
        return uninstallSuccessful;
    }

    private void deleteRemoteRepoChart(TblRepoAppInfo tblRepoAppInfo, TblRepoInfo repoInfo)
    {
        ImsRepoService.RepoChart repoChart = new ImsRepoService.RepoChart();
        repoChart.setUrl(repoInfo.getRepoUrl());
        TblRepoInfo.AuthenticationMethod authenticationMethod = fromValue(repoInfo.getAuthMethod());
        if (BASIC_AUTHENTICATION.equals(authenticationMethod))
        {
            try
            {
                AddHelmRepoReq.BasicAuth basicAuth = JacksonUtils.strToObj((String) repoInfo.getAuthInfo(), AddHelmRepoReq.BasicAuth.class);
                repoChart.setUserName(basicAuth.getUsername());
                repoChart.setPassword(AesCryptoUtils.cbcDecryptStr(basicAuth.getPassword()));
            }
            catch (IOException e)
            {
                log.error("auth info parse fail: {}", e);
            }
        }

        repoChart.setRepo(repoInfo.getRepoName());
        repoChart.setChartName(tblRepoAppInfo.getAppName());
        try
        {
            combRpcService.getImsRepoService().deleteRepoChart(repoChart);
        }
        catch (Exception e)
        {
            log.error("delete remote repo chart url:{} failed:{}", repoChart.getUrl(), e);
        }
    }

}
