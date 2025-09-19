package com.lnjoying.justice.cluster.manager.service.agent;

import com.lnjoying.justice.cluster.manager.common.*;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterAgentInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterInfo;
import com.lnjoying.justice.cluster.manager.db.model.TblClusterSaInfo;
import com.lnjoying.justice.cluster.manager.db.repo.ClusterRepo;
import com.lnjoying.justice.cluster.manager.domain.model.ClusterInnerInfo;
import com.lnjoying.justice.cluster.manager.domain.model.X509CertificateInfo;
import com.lnjoying.justice.cluster.manager.service.process.ClusterProcessStrategy;
import com.lnjoying.justice.cluster.manager.util.K8sRedisField;
import com.lnjoying.justice.commonweb.util.CollectionUtils;
import com.lnjoying.justice.schema.common.ErrorCode;
import com.lnjoying.justice.schema.constant.OnlineStatus;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfState;
import com.lnjoying.justice.schema.entity.edgeif.EdgeWorkerIfStateInfos;
import com.lnjoying.justice.schema.msg.EeCtrlMessageDef;
import com.lnjoying.justice.schema.msg.EeNetMessageApi;
import com.lnjoying.justice.schema.msg.EeWorkerDef;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.lnjoying.justice.cluster.manager.common.KeyCertName.CACertName;

@Service("clusterAgentService")
public class ClusterAgentServiceImpl implements ClusterAgentService
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private ClusterRepo clusterRepo;

    @Autowired
    private ClusterProcessStrategy clusterProcessStrategy;

    @Override
    public int addWorker(EeWorkerDef.msg_worker_reg_req_body workerRegReqBody)
    {
        try
        {
            TblClusterAgentInfo tblClusterAgentInfo = clusterRepo.getClusterAgentByPrimaryKey(workerRegReqBody.getWorkerId());
            if (tblClusterAgentInfo != null)
            {
                TblClusterInfo tblClusterInfo = clusterRepo.getCluster(tblClusterAgentInfo.getClusterId());
                if (tblClusterInfo.getToken().equals(workerRegReqBody.getToken()))
                {
                    return ErrorCode.SUCCESS.getCode();
                }
                else
                {
                    return ErrorCode.WORKER_REG_TOKEN_INVALID.getCode();
                }
            }
            else
            {
                tblClusterAgentInfo = new TblClusterAgentInfo();
            }

            //get cluster by token
            List<TblClusterInfo> tblClusterInfos = clusterRepo.getClusterByToken(workerRegReqBody.getToken());
            if (CollectionUtils.isEmpty(tblClusterInfos))
            {
                return ErrorCode.CLUSTER_NOT_EXIST.getCode();
            }

            TblClusterInfo tblClusterInfo = tblClusterInfos.get(0);
            if (tblClusterInfo.getStatus().equals(ClusterStatus.REMOVED.getCode()))
            {
                return ErrorCode.CLUSTER_DROPPED.getCode();
            }

            tblClusterAgentInfo.setAgentId(workerRegReqBody.getWorkerId());
            tblClusterAgentInfo.setClusterId(tblClusterInfo.getId());
            tblClusterAgentInfo.setOnlineStatus(OnlineStatus.ONLINE);
            tblClusterAgentInfo.setCreateTime(new Date());
            tblClusterAgentInfo.setUpdateTime(tblClusterAgentInfo.getCreateTime());

            clusterRepo.insertClusterAgent(tblClusterAgentInfo);

            if (tblClusterInfo.getCreateType().equals(ClusterCreateType.IMPORT) || tblClusterInfo.getClusterType().equals(ClusterType.K3S))
            {
                ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(tblClusterInfo.getId());
                if (clusterInnerInfo.getCertMap() == null)
                {
                    clusterInnerInfo.setCertMap(new HashMap<>());
                    X509CertificateInfo kubeCa = clusterInnerInfo.getCertMap().getOrDefault(CACertName, new X509CertificateInfo());
                    kubeCa.setCertificatePem(workerRegReqBody.getKubeCa());
                    kubeCa.setName(CACertName);
                    clusterInnerInfo.getCertMap().put(CACertName, kubeCa);
                    clusterRepo.insertClusterCert(clusterInnerInfo);
                }
                else
                {
                    X509CertificateInfo kubeCa = clusterInnerInfo.getCertMap().getOrDefault(CACertName, new X509CertificateInfo());
                    kubeCa.setCertificatePem(workerRegReqBody.getKubeCa());
                    kubeCa.setName(CACertName);
                    clusterInnerInfo.getCertMap().put(CACertName, kubeCa);
                    clusterRepo.updateClusterCert(clusterInnerInfo);
                }

                MessagePack messagePack = new MessagePack();
                messagePack.setMessageObj(clusterInnerInfo);
                if (clusterInnerInfo.getStatus().equals(ClusterStatus.CREATED))
                {
                    clusterInnerInfo.setStatus(ClusterStatus.IMPORTED);
                }
                messagePack.setMsgType(ClusterMsg.UPDATE_CLUSTER);
                clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);
            }

            TblClusterSaInfo tblClusterSaInfo = clusterRepo.getClusterSa(tblClusterAgentInfo.getClusterId());
            if (tblClusterSaInfo == null)
            {
                tblClusterSaInfo = new TblClusterSaInfo();

                tblClusterSaInfo.setClusterId(tblClusterAgentInfo.getClusterId());
                tblClusterSaInfo.setSecretToken(workerRegReqBody.getSaToken());
                tblClusterSaInfo.setCreateTime(new Date());
                tblClusterSaInfo.setUpdateTime(tblClusterSaInfo.getCreateTime());
                clusterRepo.insertClusterSa(tblClusterSaInfo);
            }
            else
            {
                tblClusterSaInfo.setSecretToken(workerRegReqBody.getSaToken());
                tblClusterSaInfo.setUpdateTime(tblClusterSaInfo.getCreateTime());
                clusterRepo.updateByPrimaryKeySelective(tblClusterSaInfo);
            }

            if (!tblClusterInfo.getServiceState().equals(ClusterActiveStatus.ACTIVE))
            {
                LOGGER.info("update cluster {} from status {} to active", tblClusterInfo.getId(), tblClusterInfo.getStatus());
                ClusterInnerInfo clusterInnerInfo = clusterRepo.getClusterInnerInfo(tblClusterInfo.getId());
                clusterInnerInfo.setServiceStatus(ClusterActiveStatus.ACTIVE);

                MessagePack messagePack = new MessagePack();
                messagePack.setMessageObj(clusterInnerInfo);
                messagePack.setMsgType(ClusterMsg.UPDATE_CLUSTER);
                clusterProcessStrategy.sendMessage(messagePack, ProcessorName.CLUSTER);
            }
            else
            {
                LOGGER.info("do not need update cluster {} status, status is {} ", tblClusterInfo.getId(), tblClusterInfo.getStatus());

            }

            return ErrorCode.SUCCESS.getCode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ErrorCode.PARAM_ERROR.getCode();
        }
    }

    @Override
    public int workerLogin(EeWorkerDef.msg_worker_login_req_body workerLoginReqBody)
    {
        TblClusterAgentInfo tblClusterAgentInfo = clusterRepo.getClusterAgentByPrimaryKey(workerLoginReqBody.getWorkerId());
        if (tblClusterAgentInfo == null)
        {
            return ErrorCode.WORKER_NOT_EXIST.getCode();
        }

        TblClusterInfo tblClusterInfo = clusterRepo.getCluster(tblClusterAgentInfo.getClusterId());

        if (tblClusterInfo == null || tblClusterInfo.getStatus().equals(ClusterStatus.REMOVED.getCode()))
        {
            return ErrorCode.CLUSTER_DROPPED.getCode();
        }

        if (StringUtils.isEmpty(tblClusterAgentInfo.getCoreVersion())
                || !workerLoginReqBody.getCoreVersion().equals(tblClusterAgentInfo.getCoreVersion()))
        {
            tblClusterAgentInfo.setCoreVersion(workerLoginReqBody.getCoreVersion());
            tblClusterAgentInfo.setUpdateTime(new Date());
            clusterRepo.updateClusterAgentInfo(tblClusterAgentInfo);
        }

        return ErrorCode.SUCCESS.getCode();
    }

    @Override
    public void processWorkerIFState(Pair<String, EeNetMessageApi.ee_net_message> ifState)
    {
        String gwNodeId = ifState.getKey();
        EeNetMessageApi.ee_net_message netMessage = ifState.getValue();
        EeCtrlMessageDef.edge_worker_if_state edgeWorkerIfState = netMessage.getCloudCtrlBody().getSyncWorkerIfStateReqBody().getWorkerIfState();

        TblClusterAgentInfo tblClusterAgentInfo = clusterRepo.getClusterAgentByPrimaryKey(edgeWorkerIfState.getWorkerId());
        if (tblClusterAgentInfo == null)
        {
            return;
        }

        EdgeWorkerIfStateInfos devIfStates = (EdgeWorkerIfStateInfos) RedisUtil.oget(K8sRedisField.WORKER_IF, edgeWorkerIfState.getWorkerId());
        if (devIfStates == null)
        {
            LOGGER.error("worker if state is error");
            return;
        }

        int status = OnlineStatus.OFFLINE;
        for (Map.Entry<String, EdgeWorkerIfState> entry : devIfStates.getEdgeWorkerIfStateMap().entrySet())
        {
            if (entry.getValue().getStatus() != OnlineStatus.ONLINE)
            {
                continue;
            }
            else
            {
                status = OnlineStatus.ONLINE;
            }
        }

        boolean needUpdateAgent = false;
        if (tblClusterAgentInfo.getOnlineStatus() == null || status != tblClusterAgentInfo.getOnlineStatus())
        {
            tblClusterAgentInfo.setOnlineStatus(edgeWorkerIfState.getIfStatus());
            needUpdateAgent = true;
        }

        if (StringUtils.isEmpty(tblClusterAgentInfo.getCoreVersion())
                || !edgeWorkerIfState.getCoreVersion().equals(tblClusterAgentInfo.getCoreVersion()))
        {
            tblClusterAgentInfo.setCoreVersion(edgeWorkerIfState.getCoreVersion());
            needUpdateAgent = true;
        }

        if (needUpdateAgent)
        {
            tblClusterAgentInfo.setUpdateTime(Utils.buildDate(System.currentTimeMillis()));
            clusterRepo.updateClusterAgentInfo(tblClusterAgentInfo);
        }

        if (edgeWorkerIfState.getIfStatus() == OnlineStatus.ONLINE)
        {
            //gw and worker relation, use for worker state check.
            RedisUtil.sadd(K8sRedisField.REGION_GW_WORKER + edgeWorkerIfState.getRegionId() + ":", gwNodeId, edgeWorkerIfState.getWorkerId());
        }
        else
        {
            //gw and worker relation, use for worker state check.
            RedisUtil.srem(K8sRedisField.REGION_GW_WORKER + edgeWorkerIfState.getRegionId() + ":", gwNodeId, edgeWorkerIfState.getWorkerId());
        }
    }
}
