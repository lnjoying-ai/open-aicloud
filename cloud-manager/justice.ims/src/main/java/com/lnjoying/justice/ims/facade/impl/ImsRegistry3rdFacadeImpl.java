package com.lnjoying.justice.ims.facade.impl;

import com.google.common.collect.Sets;
import com.lnjoying.justice.ims.common.ImsMsgType;
import com.lnjoying.justice.ims.db.model.RecordStatus;
import com.lnjoying.justice.ims.db.model.TblImsRegistry3rd;
import com.lnjoying.justice.ims.db.model.TblImsRegistryPreDownload;
import com.lnjoying.justice.ims.db.repo.ImsRegistry3rdRepository;
import com.lnjoying.justice.ims.db.repo.ImsRegistryPreDownloadRepository;
import com.lnjoying.justice.ims.domain.dto.req.AddRegistry3rdReq;
import com.lnjoying.justice.ims.domain.dto.req.PreDownloadReq;
import com.lnjoying.justice.ims.domain.dto.req.UpdateRegistry3rdReq;
import com.lnjoying.justice.ims.domain.dto.rsp.Registries3rdRsp;
import com.lnjoying.justice.ims.domain.dto.rsp.Registry3rdNodesRsp;
import com.lnjoying.justice.ims.domain.model.ImsPreDownload;
import com.lnjoying.justice.ims.domain.model.ImsRegistry3rd;
import com.lnjoying.justice.ims.exception.ImsWebSystemException;
import com.lnjoying.justice.ims.facade.ImsRegistry3rdFacade;
import com.lnjoying.justice.ims.process.service.ImsMsgProcessStrategy;
import com.lnjoying.justice.ims.service.CombRpcService;
import com.lnjoying.justice.ims.util.AesCryptoUtils;
import com.lnjoying.justice.ims.util.Base64Utils;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.schema.service.ecrm.RegionResourceService;
import com.micro.core.common.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isAdmin;
import static com.lnjoying.justice.commonweb.util.ServiceCombRequestUtils.isBpAdmin;
import static com.lnjoying.justice.ims.db.repo.ImsRegistry3rdRepository.DOCKER_IO_URL;
import static com.lnjoying.justice.schema.common.ErrorCode.*;
import static com.lnjoying.justice.schema.common.ErrorLevel.INFO;

/**
 * implementation class
 *
 * @author merak
 **/

@Service
@Slf4j
public class ImsRegistry3rdFacadeImpl implements ImsRegistry3rdFacade
{
    @Autowired
    private ImsRegistry3rdRepository registry3rdRepository;

    @Autowired
    private ImsRegistryPreDownloadRepository preDownloadRepository;

    @Autowired
    private ImsMsgProcessStrategy imsMsgProcessStrategy;

    @Autowired
    private CombRpcService combRpcService;

    @Override
    public String addRegistry3rd(AddRegistry3rdReq req) throws ImsWebSystemException
    {

        List<TblImsRegistry3rd> tblImsRegistry3rds = registry3rdRepository.selectByRegistryName(req.getRegistryName());
        if (CollectionUtils.isNotEmpty(tblImsRegistry3rds))
        {
            throw new ImsWebSystemException(REGISTRY_3RD_EXIST, INFO);
        }
        TblImsRegistry3rd tblImsRegistry3rd = new TblImsRegistry3rd();
        BeanUtils.copyProperties(req, tblImsRegistry3rd);
        tblImsRegistry3rd.setRegistryId(Utils.assignUUId());
        tblImsRegistry3rd.setStatus(RecordStatus.NORMAL.value());
        String accessSecret = req.getAccessSecret();
        tblImsRegistry3rd.setAccessSecret(AesCryptoUtils.cbcEncryptHex(accessSecret));
        try
        {
            registry3rdRepository.insertSelective(tblImsRegistry3rd);
        }
        catch (DuplicateKeyException e)
        {
            log.error("third party registry duplicated: {}", e);
            throw new ImsWebSystemException(REGISTRY_3RD_DUP, INFO);
        }
        return tblImsRegistry3rd.getRegistryId();
    }

    @Override
    public Registries3rdRsp getRegistry3rdList(String registryName, String bpId, String userId, Integer pageNum, Integer pageSize)
    {
        return registry3rdRepository.getRegistry3rdList(registryName, bpId, userId, pageNum, pageSize);
    }

    @Override
    public ImsRegistry3rd getRegistry3rd(String registryId, String bpId, String userId)
    {
        TblImsRegistry3rd registry3rd = registry3rdRepository.getRegistry3rd(registryId, bpId, userId);
        return ImsRegistry3rd.of(registry3rd);
    }

    @Override
    public void updateRegistry3rd(UpdateRegistry3rdReq req)
    {
        checkOperUser(req.getRegistryId(), req.getBpId(), req.getUserId());

        TblImsRegistry3rd tblImsRegistry3rd = new TblImsRegistry3rd();
        BeanUtils.copyProperties(req, tblImsRegistry3rd);
        tblImsRegistry3rd.setUpdateTime(LocalDateTime.now());
        String accessSecret = req.getAccessSecret();
        if (StringUtils.isNotBlank(accessSecret)) {
            tblImsRegistry3rd.setAccessSecret(AesCryptoUtils.cbcEncryptHex(accessSecret));
        }
        registry3rdRepository.updateRegistry3rd(tblImsRegistry3rd);
    }

    @Override
    public void deleteRegistry3rd(String registryId, String userId, String bpId, String userName, String bpName)
    {
        checkOperUser(registryId, bpId, userId);

        registry3rdRepository.deleteByPrimaryKey(registryId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void preDownload(PreDownloadReq req)
    {
        TblImsRegistry3rd registry3rd = registry3rdRepository.getRegistry3rd(req.getRegistryId(), null, null);

        String accessSecret = registry3rd.getAccessSecret();
        if (StringUtils.isNotBlank(accessSecret))
        {
            accessSecret = AesCryptoUtils.cbcDecryptStr(accessSecret);
        }
        String encodeAccessSecret = Base64Utils.encode(accessSecret);
        String url = registry3rd.getUrl();
        String accessKey = registry3rd.getAccessKey();
        // If the registry URL is docker.io and username password is an empty string, the URL is set to empty string
        if (StringUtils.isNotBlank(url) && DOCKER_IO_URL.equals(url))
        {
            if (StringUtils.isBlank(accessKey) && StringUtils.isBlank(accessSecret))
            {
                url = "";
            }
        }

        List<String> nodes = req.getNodes();
        if (CollectionUtils.isNotEmpty(nodes))
        {
            String finalUrl = url;
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
                                .registryUrl(finalUrl).registryUserName(accessKey)
                                .registryPassword(encodeAccessSecret).build();

                        msgPack.setMessageObj(imsPreDownload);
                        imsMsgProcessStrategy.sendMessage(msgPack);
                    });
                }
            });

        }
    }

    @Override
    public Registry3rdNodesRsp getAllRegistryNodes(String regionId)
    {
        List<RegionResourceService.RegionDto> regionDtos = new ArrayList<>();
        Set<String> regionIds = StringUtils.isNotBlank(regionId) ? Sets.newHashSet(regionId) : Sets.newHashSet();
        regionDtos = combRpcService.getRegionResourceService().selectAllNodesByRegionIds(regionIds);
        return Registry3rdNodesRsp.builder().regions(regionDtos).build();
    }

    public TblImsRegistry3rd selectByUrlAndAccessKey(String url,String accessKey)
    {
        List<TblImsRegistry3rd> tblImsRegistry3rds = registry3rdRepository.selectByUrlAndAccessKey(url, accessKey);
        if (!CollectionUtils.isEmpty(tblImsRegistry3rds))
        {
            return tblImsRegistry3rds.get(0);
        }

        return null;
    }

    @Override
    public String getRegistryUrlById(String registryId)
    {
        TblImsRegistry3rd tblImsRegistry3rd = registry3rdRepository.selectByPrimaryKey(registryId);
        if (Objects.nonNull(tblImsRegistry3rd))
        {
            return tblImsRegistry3rd.getUrl();
        }

        return "";
    }

    /**
     * Ordinary user verification can be operated
     *
     * @param registryId
     * @param bpId
     * @param userId
     */
    private void checkOperUser(String registryId, String bpId, String userId)
    {
        if (isAdmin())
        {
            registry3rdRepository.checkOperUser(registryId, null, null);
        }
        else if (isBpAdmin())
        {
            registry3rdRepository.checkOperUser(registryId, bpId, null);
        }
        else
        {
            registry3rdRepository.checkOperUser(registryId, bpId, userId);
        }

    }
}
