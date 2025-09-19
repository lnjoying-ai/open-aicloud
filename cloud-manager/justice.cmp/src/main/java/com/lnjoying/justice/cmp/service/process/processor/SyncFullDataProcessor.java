package com.lnjoying.justice.cmp.service.process.processor;

import com.lnjoying.justice.cmp.common.*;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.repo.CloudRepository;
import com.lnjoying.justice.cmp.service.cloud.CloudService;
import com.lnjoying.justice.cmp.service.syncdata.ESKSyncDataService;
import com.lnjoying.justice.cmp.service.syncdata.NSKSyncDataService;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@Component
public class SyncFullDataProcessor extends AbstractRunnableProcessor
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
        LOGGER.info("sync data processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("sync data processor stopped");
    }
    
    @Override
    public void run()
    {
        syncData();
    }

    /**
     * @Title syncData
     * @Description
     */
    private void syncData()
    {
        try
        {
            long time = new Date().getTime();
            List<String> cloudIds = RedisUtil.zrangebyscore(RedisCache.CLOUD_SYNC_IDS, 0, (double)time);
            if (CollectionUtils.isEmpty(cloudIds))
            {
                return;
            }
            for(String cloudId : cloudIds)
            {
                TblCloudInfo tblCloudInfo = cloudRepository.getCloud(cloudId);
                if (tblCloudInfo == null || tblCloudInfo.getStatus() < CloudStatus.UPDATING.getCode() || tblCloudInfo.getStatus() >= CloudStatus.WAIT_REMOVE.getCode())
                {
                    RedisUtil.zrem(RedisCache.CLOUD_SYNC_IDS, cloudId);
                    continue;
                }
                else if (tblCloudInfo.getStatus() == CloudStatus.IMPORT_FILED.getCode() || tblCloudInfo.getStatus() == CloudStatus.MAINTAIN_FILED.getCode())
                {
                    continue;
                }

                if (CloudService.getCloudHealthStatus(cloudId).equals(HealthStatus.HEALTHY))
                {
                    if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.NEXTSTACK.getName()))
                    {
                        nskSyncDataService.syncData(cloudId);
                    }
                    else if (tblCloudInfo.getProduct().equalsIgnoreCase(CloudProduct.EASYSTACK.getName()))
                    {
                        eskSyncDataService.syncData(cloudId);
                    }
                }

                RedisUtil.zincrby(RedisCache.CLOUD_SYNC_IDS, 30 * 30 * 1000, cloudId);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("syncData error {}", e.getMessage());
        }
    }
}
