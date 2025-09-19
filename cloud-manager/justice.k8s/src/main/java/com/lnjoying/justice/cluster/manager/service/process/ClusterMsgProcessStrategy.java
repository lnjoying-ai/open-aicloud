package com.lnjoying.justice.cluster.manager.service.process;

import com.lnjoying.justice.cluster.manager.service.process.processor.ClusterMsgProcessor;
import com.micro.core.process.service.ProcessStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("clusterMsgProcessStrategy")
public class ClusterMsgProcessStrategy extends ProcessStrategy
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    ClusterMsgProcessor clusterMsgProcessor;
    public ClusterMsgProcessStrategy()
    {
        super("cls message service",2, 10000);
        LOGGER.info("init ClusterMsgProcessStrategy");
    }

    @PostConstruct
    public void start()
    {
        super.start(() -> clusterMsgProcessor,1);
    }
}
