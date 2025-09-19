package com.lnjoying.justice.cmp.service.nextstack.baremetal;

import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.TblCmpPubkey;
import com.lnjoying.justice.cmp.db.model.TblCmpPubkeyExample;
import com.lnjoying.justice.cmp.db.repo.BaremetalComputeRepository;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.CommonReq;
import com.lnjoying.justice.cmp.domain.dto.request.nextstack.baremetal.UploadPubkeyReq;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.PubkeysRsp;
import com.lnjoying.justice.cmp.domain.dto.response.nextstack.baremetal.KeyPairInfo;
import com.lnjoying.justice.cmp.domain.info.baremetal.PubkeyDetailInfo;
import com.lnjoying.justice.cmp.entity.search.nextstack.baremetal.PubkeySearchCritical;
import com.lnjoying.justice.cmp.service.UserService;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.utils.JsonUtil;
import com.lnjoying.justice.commonweb.exception.WebSystemException;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.common.ErrorLevel;
import com.micro.core.common.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.SYNCING;
import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

@Service
public class PubkeyServiceBiz
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BaremetalComputeService.class);

    @Autowired
    private CloudService cloudService;

    @Autowired
    private UserService userService;

    @Autowired
    private BaremetalComputeRepository baremetalComputeRepository;

    public ResponseEntity createKeyPair(String cloudId, CommonReq req, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                KeyPairInfo keyPairInfo = JsonUtil.jsonToPojo(JsonUtil.objectToJson(response.getBody()), KeyPairInfo.class);
                if (null == keyPairInfo)
                {
                    LOGGER.error("add KeyPair error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    TblCmpPubkey tblCmpPubkey = new TblCmpPubkey();
                    tblCmpPubkey.setCloudId(cloudId);
                    tblCmpPubkey.setPubkeyId(keyPairInfo.getPubkeyId());
                    tblCmpPubkey.setPubkey(keyPairInfo.getPrivateKey());
                    tblCmpPubkey.setEeBp(bpId);
                    tblCmpPubkey.setEeUser(userId);
                    tblCmpPubkey.setEeStatus(SYNCING);

                    tblCmpPubkey.setName(req.getName());
                    tblCmpPubkey.setDescription(req.getDescription());
                    try
                    {
                        baremetalComputeRepository.insertPubkey(tblCmpPubkey);
                    }
                    catch (DuplicateKeyException e)
                    {
                        TblCmpPubkey updateTblCmpPubkey = baremetalComputeRepository.getPubkeyById(cloudId, keyPairInfo.getPubkeyId());
                        updateTblCmpPubkey.setEeBp(bpId);
                        updateTblCmpPubkey.setEeUser(userId);
                        updateTblCmpPubkey.setEeStatus(SYNCING);
                        baremetalComputeRepository.updatePubkey(updateTblCmpPubkey);
                    }

                    cloudService.syncSingleData(cloudId, keyPairInfo.getPubkeyId(), SyncMsg.NS_PUBKEY);
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
        }
    }

    public ResponseEntity uploadPubkey(String cloudId, UploadPubkeyReq req, String bpId, String userId) throws WebSystemException
    {
        try
        {
            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);
            if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK)
            {
                Map resultMap = (Map) response.getBody();
                if (null == resultMap)
                {
                    LOGGER.error("upload Pubkey error");
                    throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                }
                else
                {
                    String pubkeyId = (String) resultMap.get("pubkeyId");
                    if (StringUtils.isEmpty(pubkeyId))
                    {
                        LOGGER.error("upload Pubkey error");
                        throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
                    }
                    else
                    {
                        TblCmpPubkey tblCmpPubkey = new TblCmpPubkey();
                        tblCmpPubkey.setCloudId(cloudId);
                        tblCmpPubkey.setPubkeyId(pubkeyId);
                        tblCmpPubkey.setEeBp(bpId);
                        tblCmpPubkey.setEeUser(userId);
                        tblCmpPubkey.setEeStatus(SYNCING);

                        tblCmpPubkey.setName(req.getName());
                        tblCmpPubkey.setDescription(req.getDescription());
                        try
                        {
                            baremetalComputeRepository.insertPubkey(tblCmpPubkey);
                        }
                        catch (DuplicateKeyException e)
                        {
                            TblCmpPubkey updateTblCmpPubkey = baremetalComputeRepository.getPubkeyById(cloudId, pubkeyId);
                            updateTblCmpPubkey.setEeBp(bpId);
                            updateTblCmpPubkey.setEeUser(userId);
                            updateTblCmpPubkey.setEeStatus(SYNCING);
                            baremetalComputeRepository.updatePubkey(updateTblCmpPubkey);
                        }

                        Map<String, String> retValue = new HashMap<>();
                        retValue.put("pubkeyId", pubkeyId);

                        cloudService.syncSingleData(cloudId, pubkeyId, SyncMsg.NS_PUBKEY);
                    }
                }
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new WebSystemException(ErrorCode.SystemError, ErrorLevel.INFO);
        }
    }

    public ResponseEntity removePubkey(String cloudId, String pubkeyId, String userId) throws WebSystemException
    {
        try
        {
            TblCmpPubkey tblCmpPubkey = baremetalComputeRepository.getPubkeyById(cloudId, pubkeyId);

            if (tblCmpPubkey == null)
            {
                LOGGER.error("get Pubkey null: Pubkey id {}", pubkeyId);
                throw new WebSystemException(ErrorCode.PUBKEY_NOT_EXIST, ErrorLevel.INFO);
            }
            if (userId != null && !tblCmpPubkey.getEeUser().equals(userId))
            {
                throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
            }

            ResponseEntity response = cloudService.sendHttpRequestToCloud(cloudId);

            if (response.getStatusCode() == HttpStatus.NO_CONTENT || response.getStatusCode() == HttpStatus.OK || response.getStatusCode() == HttpStatus.ACCEPTED
                    || response.getStatusCode() == HttpStatus.NOT_FOUND || response.getStatusCode() == HttpStatus.CREATED)
            {
                tblCmpPubkey.setEeStatus(REMOVED);
                tblCmpPubkey.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
                baremetalComputeRepository.updatePubkey(tblCmpPubkey);
            }
            return response;
        }
        catch (WebSystemException e)
        {
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("remove Pubkey failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.INFO, e.getMessage());
        }
    }

    public PubkeyDetailInfo getPubkey(String cloudId, String pubkeyId, String userId) throws WebSystemException
    {
        TblCmpPubkey tblCmpPubkey = baremetalComputeRepository.getPubkeyById(cloudId, pubkeyId);
        if (null == tblCmpPubkey || tblCmpPubkey.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.PUBKEY_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpPubkey.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
        PubkeyDetailInfo pubkeyDetailInfo = new PubkeyDetailInfo();
        pubkeyDetailInfo.setPubkeyDetailInfo(tblCmpPubkey);
        pubkeyDetailInfo.setEeBpName(userService.getBpName(pubkeyDetailInfo.getEeBp()));
        pubkeyDetailInfo.setEeUserName(userService.getUserName(pubkeyDetailInfo.getEeUser()));

        cloudService.syncSingleData(cloudId, pubkeyId, SyncMsg.NS_PUBKEY);
        return pubkeyDetailInfo;
    }

    public PubkeysRsp getPubkeys(String cloudId, PubkeySearchCritical critical, String userId) throws WebSystemException
    {
        try
        {
            TblCmpPubkeyExample example = new TblCmpPubkeyExample();
            TblCmpPubkeyExample.Criteria criteria = example.createCriteria();
            criteria.andCloudIdEqualTo(cloudId);
            criteria.andEeStatusNotEqualTo(REMOVED);
            if (userId != null)
            {
                criteria.andEeUserEqualTo(userId);
            }
            if (null != critical.getName())
            {
                criteria.andNameLike("%" + critical.getName() + "%");
            }

            PubkeysRsp getPubkeysRsp = new PubkeysRsp();

            long totalNum = baremetalComputeRepository.countPubkeyByExample(example);
            getPubkeysRsp.setTotalNum(totalNum);
            if (totalNum < 1)
            {
                return getPubkeysRsp;
            }
            int begin = ((critical.getPageNum() - 1) * critical.getPageSize());
            example.setOrderByClause("create_time desc,pubkey_id desc");

            example.setStartRow(begin);
            example.setPageSize(critical.getPageSize());

            List<TblCmpPubkey> tblCmpPubkeyList = baremetalComputeRepository.getPubkeys(example);
            if (null == tblCmpPubkeyList)
            {
                return getPubkeysRsp;
            }

            List<PubkeyDetailInfo> pubkeyInfoList = tblCmpPubkeyList.stream().map((tblPubkey) ->
            {
                PubkeyDetailInfo pubkeyInfo = new PubkeyDetailInfo();
                pubkeyInfo.setPubkeyDetailInfo(tblPubkey);
                pubkeyInfo.setEeBpName(userService.getBpName(pubkeyInfo.getEeBp()));
                pubkeyInfo.setEeUserName(userService.getUserName(pubkeyInfo.getEeUser()));
                return pubkeyInfo;
            }).collect(Collectors.toList());

            getPubkeysRsp.setPubkeys(pubkeyInfoList);

            pubkeyInfoList.forEach(pubkeyInfo -> cloudService.syncSingleData(cloudId, pubkeyInfo.getPubkeyId(), SyncMsg.NS_PUBKEY));

            return getPubkeysRsp;
        }
        catch (Exception e)
        {
            LOGGER.error("get pubkeys failed: {}", e.getMessage());
            throw new WebSystemException(ErrorCode.PARAM_ERROR, ErrorLevel.ERROR);
        }
    }

    public void checkPubkeyUser(String cloudId, String pubkeyId, String userId)
    {
        TblCmpPubkey tblCmpPubkey = baremetalComputeRepository.getPubkeyById(cloudId, pubkeyId);
        if (null == tblCmpPubkey || tblCmpPubkey.getEeStatus() == REMOVED)
        {
            throw new WebSystemException(ErrorCode.PUBKEY_NOT_EXIST, ErrorLevel.INFO);
        }
        if (userId != null && !tblCmpPubkey.getEeUser().equals(userId))
        {
            throw new WebSystemException(ErrorCode.User_Not_Grant, ErrorLevel.INFO);
        }
    }
}
