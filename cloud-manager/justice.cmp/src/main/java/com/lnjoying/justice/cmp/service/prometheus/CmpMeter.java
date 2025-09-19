package com.lnjoying.justice.cmp.service.prometheus;

import com.google.common.annotations.VisibleForTesting;
import com.netflix.spectator.api.Measurement;
import com.netflix.spectator.api.Registry;
import org.apache.servicecomb.foundation.metrics.meter.AbstractPeriodMeter;

import java.util.ArrayList;
import java.util.List;

public class CmpMeter extends AbstractPeriodMeter
{
    private CloudHealthMeter cloudHealthMeter;
    private HypervisorNodeMeter hypervisorNodeMeter;

    public CmpMeter(Registry registry) {
        this.id = registry.createId("cmp");
        this.cloudHealthMeter = new CloudHealthMeter(this.id.withTag("type", "cloudHealth"));
        this.hypervisorNodeMeter = new HypervisorNodeMeter(this.id.withTag("type", "hypervisorNodeStatus"));
    }

    public void calcMeasurements(long msNow, long secondInterval) {
        List<Measurement> measurements = new ArrayList();
        this.calcMeasurements(measurements, msNow, secondInterval);
        this.allMeasurements = measurements;
    }

    public void calcMeasurements(List<Measurement> measurements, long msNow, long secondInterval) {
        this.cloudHealthMeter.calcMeasurements(measurements, msNow);
        this.hypervisorNodeMeter.calcMeasurements(measurements, msNow);
    }

    @VisibleForTesting
    public CloudHealthMeter getCloudHealthMeter() {
        return this.cloudHealthMeter;
    }

    @VisibleForTesting
    public HypervisorNodeMeter getHypervisorNodeMeter() {
        return this.hypervisorNodeMeter;
    }
}
