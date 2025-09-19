package com.lnjoying.justice.cmp.service.process.processor;

import com.lnjoying.justice.cmp.common.CreateResourceMsg;
import com.lnjoying.justice.cmp.domain.model.CreateResourceInfo;
import com.lnjoying.justice.cmp.service.nextstack.network.NetworkService;
import com.lnjoying.justice.cmp.service.nextstack.network.SgService;
import com.lnjoying.justice.cmp.service.nextstack.repo.VolumeServiceBiz;
import com.lnjoying.justice.cmp.service.nextstack.vm.VmInstanceServiceBiz;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;

@Component
public class CreateResourceProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    @Autowired
    private VmInstanceServiceBiz vmComputeService;

    @Autowired
    private NetworkService networkService;

    @Autowired
    private SgService securityGroupService;

    @Autowired
    private VolumeServiceBiz volumeServiceBiz;
    
    @Override
    public void start()
    {
        LOGGER.info("create resource processor started");
    }
    
    @Override
    public void stop()
    {
        LOGGER.info("create resource processor stopped");
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
        CreateResourceInfo createResourceInfo = null;
        try
        {
            switch (pack.getMsgType())
            {
                case CreateResourceMsg.NS_VM_INSTANCE:
                    createResourceInfo = (CreateResourceInfo) pack.getMessageObj();
                    vmComputeService.addVmInstance(createResourceInfo);
                    break;
                case CreateResourceMsg.NS_VOLUME:
                    createResourceInfo = (CreateResourceInfo) pack.getMessageObj();
                    volumeServiceBiz.createVolume(createResourceInfo);
                    break;
                case CreateResourceMsg.NS_VPC:
                    createResourceInfo = (CreateResourceInfo) pack.getMessageObj();
                    networkService.createVpc(createResourceInfo);
                    break;
                case CreateResourceMsg.NS_SUBNET:
                    createResourceInfo = (CreateResourceInfo) pack.getMessageObj();
                    networkService.createSubnet(createResourceInfo);
                    break;
                case CreateResourceMsg.NS_SECURITY_GROUP:
                    createResourceInfo = (CreateResourceInfo) pack.getMessageObj();
                    securityGroupService.createSecurityGroup(createResourceInfo);
                    break;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("CreateResourceProcessor.process error {}", e.getMessage());
        }
    }
}
