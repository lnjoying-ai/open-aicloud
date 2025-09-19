package com.lnjoying.justice.cis.process.processor;

import com.lnjoying.justice.cis.common.constant.CisRedisField;
import com.lnjoying.justice.cis.common.constant.InstanceState;
import com.lnjoying.justice.cis.db.model.*;
import com.lnjoying.justice.cis.db.repo.CisRepository;
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
    CisRepository cisRepository;

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
            TblContainerSpecInfoExample example = new TblContainerSpecInfoExample();
            TblContainerSpecInfoExample.Criteria criteria = example.createCriteria();
            criteria.andRestartPolicyEqualTo("always");
            criteria.andStatusNotEqualTo(RecordStatus.DELETED.value());

            long restartAlwaysNum = cisRepository.countSpecByExample(example);

            for (int i = 0; i < restartAlwaysNum; i = i + 20)
            {
                example.setStartRow(i * 20);
                example.setPageSize(20);
                List<TblContainerSpecInfo> tblContainerSpecInfos = cisRepository.selectSpecByExample(example);
                for (TblContainerSpecInfo tblContainerSpecInfo : tblContainerSpecInfos)
                {
                    if (StringUtils.isNotEmpty(tblContainerSpecInfo.getFailoverPolicy()) &&
                            tblContainerSpecInfo.getStatus() == InstanceState.ASSIGNED.getCode())
                    {
                        Double score = RedisUtil.zscore(CisRedisField.CIS_KEEPALIVE_SPECIDS, tblContainerSpecInfo.getSpecId());
                        if (score == null)
                        {
                            FailoverPolicy failoverPolicy = null;
                            try
                            {
                                failoverPolicy = JsonUtils.fromJson(tblContainerSpecInfo.getFailoverPolicy(), FailoverPolicy.class);
                            } catch (Exception e)
                            {
                                continue;
                            }

                            if (failoverPolicy != null && failoverPolicy.getNeedFailover())
                            {
                                RedisUtil.zadd(CisRedisField.CIS_KEEPALIVE_SPECIDS, tblContainerSpecInfo.getSpecId(), (double) new Date().getTime());
                                TblContainerInstInfoExample instExample = new TblContainerInstInfoExample();
                                TblContainerInstInfoExample.Criteria instCriteria = instExample.createCriteria();
                                instCriteria.andSpecIdEqualTo(tblContainerSpecInfo.getSpecId());
                                List<TblContainerInstInfo> tblContainerInstInfos = cisRepository.getContainerInstInfosByExample(instExample);
                                for (TblContainerInstInfo tblContainerInstInfo : tblContainerInstInfos)
                                {
                                    if ((tblContainerInstInfo.getStatus() > InstanceState.FWD.getCode() && tblContainerInstInfo.getStatus() <= InstanceState.EDGE_UNREACHABLE.getCode())
                                            || (tblContainerInstInfo.getStatus() >= InstanceState.QUEUEING.getCode() && tblContainerInstInfo.getStatus() <= InstanceState.RESTARTING.getCode()))
                                    {
                                        RedisUtil.zadd(CisRedisField.CIS_KEEPALIVE_SPEC_INSTIDS + tblContainerSpecInfo.getSpecId(), tblContainerInstInfo.getInstId(), (double) tblContainerInstInfo.getReportTime().getTime());
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
