package com.lnjoying.justice.cmp.service.prometheus;

import com.lnjoying.justice.cmp.db.mapper.TblCmpHypervisorNodeMapper;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNode;
import com.lnjoying.justice.cmp.db.model.TblCmpHypervisorNodeExample;
import com.netflix.spectator.api.Id;
import com.netflix.spectator.api.Measurement;
import org.apache.servicecomb.foundation.common.utils.BeanUtils;
import org.apache.servicecomb.metrics.core.meter.os.NetMeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.lnjoying.justice.cmp.common.CommonPhaseStatus.REMOVED;

public class HypervisorNodeMeter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(NetMeter.class);
    private final Id id;
    private TblCmpHypervisorNodeMapper tblCmpHypervisorNodeMapper;

    public HypervisorNodeMeter(Id id) {
        this.id = id;
    }

    public void calcMeasurements(List<Measurement> measurements, long msNow)
    {
        try
        {
            if (this.tblCmpHypervisorNodeMapper == null)
            {
                this.tblCmpHypervisorNodeMapper = BeanUtils.getBean("tblCmpHypervisorNodeMapper");
            }
            TblCmpHypervisorNodeExample example = new TblCmpHypervisorNodeExample();
            example.setOrderByClause("create_time desc");
            TblCmpHypervisorNodeExample.Criteria criteria = example.createCriteria();
            criteria.andEeStatusNotEqualTo(REMOVED);
            List<TblCmpHypervisorNode> tblCmpHypervisorNodes = tblCmpHypervisorNodeMapper.selectByExample(example);
            for (TblCmpHypervisorNode tblCmpHypervisorNode : tblCmpHypervisorNodes)
            {
                measurements.add(new Measurement(id.withTag("cloudId", tblCmpHypervisorNode.getCloudId())
                        .withTag("nodeId", tblCmpHypervisorNode.getNodeId()).withTag("nodeName", tblCmpHypervisorNode.getName()),
                        msNow, tblCmpHypervisorNode.getPhaseStatus()));
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Failed to get hypervisor node health");
        }
    }
}
