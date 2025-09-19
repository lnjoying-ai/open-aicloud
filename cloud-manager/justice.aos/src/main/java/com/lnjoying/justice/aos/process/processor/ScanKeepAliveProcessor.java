package com.lnjoying.justice.aos.process.processor;

import com.lnjoying.justice.aos.common.RedisCache;
import com.lnjoying.justice.aos.common.StackState;
import com.lnjoying.justice.aos.db.model.TblStackInfo;
import com.lnjoying.justice.aos.db.model.TblStackInfoExample;
import com.lnjoying.justice.aos.db.model.TblStackSpecInfo;
import com.lnjoying.justice.aos.db.repo.StackRepository;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.schema.entity.dev.FailoverPolicy;
import com.micro.core.persistence.redis.RedisUtil;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ScanKeepAliveProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    StackRepository stackRepository;

    @Override
    public void start()
    {
        LOGGER.info("scan keep alive process started");
    }

    @Override
    public void stop()
    {
        LOGGER.info("scan keep alive process stopped");
    }

    @Override
    public void run()
    {
        LOGGER.info("start ScanKeepAliveProcessor");
        loadRestartAlwaysInst();
    }

    void loadRestartAlwaysInst()
    {
        try
        {
            long restartAlwaysNum = stackRepository.countAlwaysOnlineStackSpec();

            for (int i = 0; i < restartAlwaysNum; i = i + 20)
            {
                int startRow = i * 20;
                int pageSize = 20;
                List<TblStackSpecInfo> tblStackSpecInfos = stackRepository.selectAlwaysOnlineStackSpec(startRow, pageSize);
                for (TblStackSpecInfo tblStackSpecInfo : tblStackSpecInfos)
                {
                    if (StringUtils.isNotEmpty(tblStackSpecInfo.getFailoverPolicy()) &&
                            tblStackSpecInfo.getStatus() == StackState.ASSIGNED)
                    {
                        Double score = RedisUtil.zscore(RedisCache.AOS_KEEPALIVE_SPECIDS, tblStackSpecInfo.getSpecId());
                        if (score == null)
                        {
                            FailoverPolicy failoverPolicy = null;
                            try
                            {
                                failoverPolicy = JsonUtils.fromJson(tblStackSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
                            }
                            catch (Exception e)
                            {
                                continue;
                            }

                            if (failoverPolicy != null &&failoverPolicy.getNeedFailover())
                            {
                                RedisUtil.zadd(RedisCache.AOS_KEEPALIVE_SPECIDS, tblStackSpecInfo.getSpecId(), (double)new Date().getTime());
                                TblStackInfoExample stackExample = new TblStackInfoExample();
                                TblStackInfoExample.Criteria instCriteria = stackExample.createCriteria();
                                instCriteria.andSpecIdEqualTo(tblStackSpecInfo.getSpecId());
                                List<TblStackInfo> tblStackInfos = stackRepository.getStack(stackExample);
                                for (TblStackInfo tblStackInfo : tblStackInfos)
                                {
                                    if ((tblStackInfo.getStatus() > StackState.FWD && tblStackInfo.getStatus() <= StackState.EDGE_UNREACHABLE)
                                            || (tblStackInfo.getStatus() >= StackState.QUEUEING && tblStackInfo.getStatus() <= StackState.RESTARTING))
                                    {
                                        RedisUtil.zadd(RedisCache.AOS_KEEPALIVE_SPEC_STACKIDS + tblStackSpecInfo.getSpecId(), tblStackInfo.getStackId(), (double)tblStackInfo.getReportTime().getTime());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadRestartAlwaysInst error {}", e.getMessage());
        }
    }

    @Override
    public void exceptionThrown(Exception e)
    {
        return;
    }
}
