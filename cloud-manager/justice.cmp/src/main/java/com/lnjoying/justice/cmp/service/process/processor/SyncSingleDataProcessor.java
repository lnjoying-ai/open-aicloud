package com.lnjoying.justice.cmp.service.process.processor;

import com.lnjoying.justice.cmp.common.HealthStatus;
import com.lnjoying.justice.cmp.common.SyncMsg;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.service.syncdata.NSKSyncDataService;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.common.Pair;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class SyncSingleDataProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();
    
    @Autowired
    private NSKSyncDataService nskSyncDataService;

    @Autowired
    private ESKSyncDataService eskSyncDataService;

    @Autowired
    private CloudRepository cloudRepository;
    
    @Override
    public void start()
    {
        LOGGER.info("sync processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("sync processor stopped");
    }
    
    @Override
    public void run()
    {
        BlockingQueue<Object> queue = getBlockQueue();
        while (true)
        {
            try
            {
                MessagePack pack = (MessagePack) queue.take();
                process(pack);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
                LOGGER.error("SyncProcessor.run error {}", e.getMessage());
            }
        }
    }
    
    void process(MessagePack pack)
    {
        String cloudId = null;
        Pair<String, String> pair = null;
        TblCloudInfo tblCloudInfo = null;
        try
        {
            switch (pack.getMsgType())
            {
                case SyncMsg.NS_BAREMETAL_DEVICE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncBaremetalDevice(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_BAREMETAL_INSTANCE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncBaremetalInstance(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_PUBKEY:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncPubkey(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_FLAVOR:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncFlavor(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_IMAGE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncImage(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_STORAGE_POOL:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncStoragePool(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_VOLUME:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncVolume(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_VOLUME_SNAP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncVolumeSnap(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_HYPERVISOR_NODE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncHypervisorNode(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_PCI_DEVICE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncPciDevice(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_VM_INSTANCE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncVmInstance(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_VM_INSTANCES:
                    cloudId = (String) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(cloudId).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncVmInstances(cloudId);
                    }
                    break;
                case SyncMsg.NS_SNAP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncSnap(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_INSTANCE_GROUP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncInstanceGroup(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_RESOURCE_STATS:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncResourceStats(pair.getLeft());
                    }
                    break;
                case SyncMsg.NS_NFS_SERVER:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncNfsServer(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_VPC:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncVpc(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_SUBNET:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncSubnet(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_EIP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncEip(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_PORT_MAP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncPortMap(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_EIP_POOL:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncEipPool(pair.getLeft(), pair.getRight());
                    }
                    break;
                case SyncMsg.NS_SECURITY_GROUP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        nskSyncDataService.syncSecurityGroup(pair.getLeft(), pair.getRight());
                    }
                    break;

                case SyncMsg.OS_FLAVOR:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncFlavor(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_INSTANCE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncServer(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_KEYPAIR:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncKeypair(tblCloudInfo, pair.getRight(), null, null, null);
                    }
                    break;
                case SyncMsg.OS_NETWORK:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncNetwork(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_PORT:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncPort(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_SUBNET:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncSubnet(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_ROUTER:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncRouter(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_FLOATING_IP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncFloatingIp(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_SECURITY_GROUP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncSecurityGroup(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_SECURITY_GROUP_RULE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncSecurityGroupRule(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_VOLUME:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncVolume(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_SNAPSHOT:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncSnapshot(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_BACKUP:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncBackup(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_VOLUME_ATTACHMENTS:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncServerVolumeAttachments(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.OS_IMAGE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncImage(tblCloudInfo, pair.getRight());
                    }
                    break;

                case SyncMsg.ES_FIREWALL:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncFirewall(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.ES_FIREWALL_POLICY:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncFirewallPolicy(tblCloudInfo, pair.getRight());
                    }
                    break;
                case SyncMsg.ES_FIREWALL_RULE:
                    pair = (Pair<String, String>) pack.getMessageObj();
                    if (CloudService.getCloudHealthStatus(pair.getLeft()).equals(HealthStatus.HEALTHY))
                    {
                        tblCloudInfo = cloudRepository.getCloud(pair.getLeft());
                        eskSyncDataService.syncFirewallRule(tblCloudInfo, pair.getRight());
                    }
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("SyncProcessor.process error {}", e.getMessage());
        }
    }
}
