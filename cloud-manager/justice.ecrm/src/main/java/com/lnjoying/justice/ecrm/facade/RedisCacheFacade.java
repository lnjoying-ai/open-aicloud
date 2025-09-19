package com.lnjoying.justice.ecrm.facade;

import com.google.gson.reflect.TypeToken;
import com.lnjoying.justice.commonweb.util.JsonUtils;
import com.lnjoying.justice.ecrm.common.constant.RedisCache;
import com.lnjoying.justice.ecrm.db.model.*;
import com.lnjoying.justice.ecrm.db.repo.EdgeRepository;
import com.lnjoying.justice.ecrm.db.repo.RegionRepository;
import com.lnjoying.justice.ecrm.db.repo.SiteRepository;
import com.lnjoying.justice.schema.constant.ActiveStatus;
import com.micro.core.common.Utils;
import com.micro.core.persistence.redis.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RedisCacheFacade
{

    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    RegionRepository regionRepository;
    @Autowired
    SiteRepository siteRepository;
    @Autowired
    EdgeRepository edgeRepository;

    public void initRedisData()
    {
        Long labelRegionStatus = RedisUtil.exists(RedisCache.LABEL_REGION_SET);
        Long labelSiteStatus = RedisUtil.exists(RedisCache.LABEL_SITE_SET);
        Long labelEdgeStatus = RedisUtil.exists(RedisCache.LABEL_EDGE_SET);

        if(null != labelRegionStatus && labelRegionStatus == 0 && null != labelSiteStatus && labelSiteStatus == 0 && null != labelEdgeStatus && labelEdgeStatus == 0)
        {
            LOGGER.info("init redis data start");
            //region
            List<TblRegionInfo> tblRegionInfoList = regionRepository.getRegions(null, null);
            for(TblRegionInfo regionInfo:tblRegionInfoList)
            {
                if (null != regionInfo.getLabels() && !regionInfo.getLabels().isEmpty())
                {
                    Map<String, String> labels = JsonUtils.fromJson(regionInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
                    for (Map.Entry<String, String> entry : labels.entrySet())
                    {
                        RedisUtil.sadd(RedisCache.LABEL_REGION, entry.getKey() + ":" + entry.getValue(), regionInfo.getRegionId());
                        RedisUtil.sadd(RedisCache.LABEL_REGION, entry.getKey(), regionInfo.getRegionId());
                        RedisUtil.sadd(RedisCache.LABEL_REGION_SET, "", entry.getKey());
                    }
                }

                //site
                TblSiteInfoExample siteExample = new TblSiteInfoExample();
                TblSiteInfoExample.Criteria siteCriteria = siteExample.createCriteria();
                siteCriteria.andRegionIdEqualTo(regionInfo.getRegionId());
                siteCriteria.andStatusNotEqualTo(ActiveStatus.REMOVED);
                List<TblSiteInfo> tblSiteInfoList = siteRepository.getSites(siteExample);
                for(TblSiteInfo siteInfo:tblSiteInfoList)
                {
                    if (null != siteInfo.getLabels() && !siteInfo.getLabels().isEmpty())
                    {
                        Map<String, String> labels = JsonUtils.fromJson(siteInfo.getLabels(), new TypeToken<Map<String, String>>(){}.getType());
                        for (Map.Entry<String, String> entry : labels.entrySet())
                        {
                            RedisUtil.sadd(RedisCache.LABEL_SITE, entry.getKey() + ":" + entry.getValue(), siteInfo.getSiteId());
                            RedisUtil.sadd(RedisCache.LABEL_SITE, entry.getKey(), siteInfo.getSiteId());
                            RedisUtil.sadd(RedisCache.LABEL_SITE_SET, "", entry.getKey());
                        }
                    }

                    //edge
                    TblEdgeComputeInfoExample edgeExample = new TblEdgeComputeInfoExample();
                    TblEdgeComputeInfoExample.Criteria edgeCriteria = edgeExample.createCriteria();
                    edgeCriteria.andSiteIdEqualTo(siteInfo.getSiteId());
                    List<TblEdgeComputeInfo> tblEdgeComputeInfoList = edgeRepository.getEdgesByExample(edgeExample);
                    for (TblEdgeComputeInfo tblEdgeComputeInfo:tblEdgeComputeInfoList)
                    {
                        List<TblEdgeComputeLabelInfo> tblEdgeComputeLabelInfoList = edgeRepository.selectLabelByNodeId(tblEdgeComputeInfo.getNodeId());
                        if (null == tblEdgeComputeLabelInfoList || tblEdgeComputeLabelInfoList.isEmpty())
                        {
                            continue;
                        }
                        for(TblEdgeComputeLabelInfo tblEdgeComputeLabelInfo:tblEdgeComputeLabelInfoList)
                        {
                            RedisUtil.sadd(RedisCache.LABEL_EDGE, tblEdgeComputeLabelInfo.getLabelKey() + ":" + tblEdgeComputeLabelInfo.getLabelValue(), tblEdgeComputeLabelInfo.getNodeId());
                            RedisUtil.sadd(RedisCache.LABEL_EDGE, tblEdgeComputeLabelInfo.getLabelKey(), tblEdgeComputeLabelInfo.getNodeId());
                            RedisUtil.sadd(RedisCache.LABEL_EDGE_SET, "", tblEdgeComputeLabelInfo.getLabelKey());
                        }
                    }
                }
            }
            LOGGER.info("init redis data end");
        }
        else
        {
            LOGGER.info("init redis data skip");
        }
    }

    public void setLabelCache(Map<String, String> labels, String labelSetKey, String labelKeyPrefix, String id)
    {
        for (Map.Entry<String, String> entry : labels.entrySet())
        {
            if (null == entry.getValue() || entry.getValue().isEmpty())
            {
                continue;
            }
            String[] values = entry.getValue().split(",");
            for (String value:values)
            {
                RedisUtil.sadd(labelKeyPrefix, entry.getKey() + ":" + value, id);
            }
            RedisUtil.sadd(labelKeyPrefix, entry.getKey(), id);
            RedisUtil.sadd(labelSetKey, "", entry.getKey());
        }
    }
    
    public void deleteOldLabelCache(String oldLabels, String labelSetKey, String labelKeyPrefix, String id)
    {
        if (StringUtils.isNotEmpty(oldLabels))
        {
            Map<String, String> labels = JsonUtils.fromJson(oldLabels, new TypeToken<Map<String, String>>(){}.getType());
            for (Map.Entry<String, String> entry : labels.entrySet())
            {
                if (null == entry.getValue() || entry.getValue().isEmpty())
                {
                    continue;
                }
                String[] values = entry.getValue().split(",");
                for (String value:values)
                {
                    RedisUtil.srem(labelKeyPrefix, entry.getKey() + ":" + value, id);
                    Long keyValueMembers =  RedisUtil.scard(labelKeyPrefix + entry.getKey() + ":" + value);
                    if (null != keyValueMembers && keyValueMembers <= 0)
                    {
                        RedisUtil.delete(labelKeyPrefix + entry.getKey() + ":" + value);
                    }
                }
                RedisUtil.srem(labelKeyPrefix, entry.getKey(), id);
                Long keyMembers =  RedisUtil.scard(labelKeyPrefix + entry.getKey());
                if (null != keyMembers && keyMembers <= 0)
                {
                    RedisUtil.delete(labelKeyPrefix + entry.getKey());
                    RedisUtil.srem(labelSetKey, "", entry.getKey());
                }
            }
        }
    }
}
