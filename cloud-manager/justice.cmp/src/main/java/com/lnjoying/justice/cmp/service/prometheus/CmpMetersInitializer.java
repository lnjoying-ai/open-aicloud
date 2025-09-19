package com.lnjoying.justice.cmp.service.prometheus;

import com.google.common.eventbus.EventBus;
import com.netflix.spectator.api.Registry;
import com.netflix.spectator.api.SpectatorUtils;
import org.apache.servicecomb.foundation.metrics.MetricsBootstrapConfig;
import org.apache.servicecomb.foundation.metrics.MetricsInitializer;
import org.apache.servicecomb.foundation.metrics.registry.GlobalRegistry;

public class CmpMetersInitializer implements MetricsInitializer
{
    private CmpMeter cmpMeter;

    public CmpMetersInitializer() {
    }

    public void init(GlobalRegistry globalRegistry, EventBus eventBus, MetricsBootstrapConfig config) {
        Registry registry = globalRegistry.getDefaultRegistry();
        this.cmpMeter = new CmpMeter(registry);
        SpectatorUtils.registerMeter(registry, this.cmpMeter);
    }

    public CmpMeter getCmpMeter() {
        return this.cmpMeter;
    }
}
