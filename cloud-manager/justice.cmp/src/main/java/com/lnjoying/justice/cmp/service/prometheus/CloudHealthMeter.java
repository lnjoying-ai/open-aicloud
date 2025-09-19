package com.lnjoying.justice.cmp.service.prometheus;

import com.lnjoying.justice.cmp.common.CloudStatus;
import com.lnjoying.justice.cmp.common.HealthStatus;
import com.lnjoying.justice.cmp.common.RedisCache;
import com.lnjoying.justice.cmp.db.mapper.TblCloudInfoMapper;
import com.lnjoying.justice.cmp.db.model.TblCloudInfo;
import com.lnjoying.justice.cmp.db.model.TblCloudInfoExample;
import com.micro.core.persistence.redis.RedisUtil;
import com.netflix.spectator.api.Id;
import com.netflix.spectator.api.Measurement;
import org.apache.commons.lang3.StringUtils;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.metrics.core.meter.os.NetMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class CloudHealthMeter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NetMeter.class);
    private final Id id;
    private TblCloudInfoMapper tblCloudInfoMapper;

    public CloudHealthMeter(Id id) {
        this.id = id;
    }

    public void calcMeasurements(List<Measurement> measurements, long msNow)
    {
        try
        {
            if (this.tblCloudInfoMapper == null)
            {
                this.tblCloudInfoMapper = BeanUtils.getBean("tblCloudInfoMapper");
            }
            TblCloudInfoExample example = new TblCloudInfoExample();
            example.setOrderByClause("create_time desc");
            TblCloudInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusBetween(CloudStatus.INTERNAL_TEST.getCode(), CloudStatus.OFF_SHELF.getCode());
            List<TblCloudInfo> tblCloudInfoList = tblCloudInfoMapper.selectByExample(example);
            for (TblCloudInfo tblCloudInfo : tblCloudInfoList)
            {
                String status = RedisUtil.get(RedisCache.CLOUD_HEALTH_STATUS + tblCloudInfo.getCloudId());
                int statusCode = !StringUtils.isNumeric(status) ? HealthStatus.UNKNOWN.getCode() : Integer.parseInt(status);
                measurements.add(new Measurement(id.withTag("cloudId", tblCloudInfo.getCloudId()).withTag("cloudName", tblCloudInfo.getName()), msNow, statusCode));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to get cloud health");
        }
    }
}
