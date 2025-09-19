package com.lnjoying.justice.servicemanager.service.process.processor;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.lnjoying.justice.schema.msg.MessagePack;
import com.lnjoying.justice.servicemanager.common.ProcessorName;
import com.lnjoying.justice.servicemanager.common.ServiceManagerMsg;
import com.lnjoying.justice.servicemanager.common.ServicePortStatus;
import com.lnjoying.justice.servicemanager.common.TargetServiceStatus;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortInfoExample;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfo;
import com.lnjoying.justice.servicemanager.db.model.TblServicePortTargetServiceInfoExample;
import com.lnjoying.justice.servicemanager.db.repo.ServicePortRepository;
import com.lnjoying.justice.servicemanager.service.process.ServiceManagerMsgProcessStrategy;
import com.micro.core.process.processor.AbstractRunnableProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ScanProcessor extends AbstractRunnableProcessor
{
    private static Logger LOGGER = LogManager.getLogger();

    public ScanProcessor()
    {
        LOGGER.info("init scan cloud processor");
    }

    @Autowired
    private ServicePortRepository servicePortRepository;

    @Autowired
    private ServiceManagerMsgProcessStrategy serviceManagerMsgProcessStrategy;

    @Override
    public void start()
    {
        LOGGER.info("start scan cloud job");
    }

    @Override
    public void stop()
    {
        LOGGER.info("stop can cloud check");
    }

    @Override
    public void run()
    {
        loadServicePortCreating();
        loadServicePortUpdating();
        loadServicePortRemoving();
        loadServicePortTargetServiceRemoving();
    }

    private void loadServicePortCreating()
    {
        try
        {
            TblServicePortInfoExample example = new TblServicePortInfoExample();
            TblServicePortInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo(ServicePortStatus.CREATING.getCode());
            List<TblServicePortInfo> tblServicePortInfos = servicePortRepository.getServicePortsByExample(example);

            if (CollectionUtils.isEmpty(tblServicePortInfos))
            {
                return;
            }

            for (TblServicePortInfo tblServicePortInfo : tblServicePortInfos)
            {
                List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId());

                if (CollectionUtils.isEmpty(tblServicePortTargetServiceInfos))
                {
                    tblServicePortInfo.setStatus(ServicePortStatus.READY.getCode());
                    tblServicePortInfo.setUpdateTime(new Date());
                    servicePortRepository.updateServicePort(tblServicePortInfo);
                }
                else
                {
                    AtomicInteger creatingNum = new AtomicInteger();
                    AtomicInteger failedNum = new AtomicInteger();
                    tblServicePortTargetServiceInfos.forEach(tblServicePortTargetServiceInfo -> {
                        TargetServiceStatus targetServiceStatus = TargetServiceStatus.fromCode(tblServicePortTargetServiceInfo.getStatus());
                        switch(targetServiceStatus)
                        {
                            case CREATING:
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.ALLOC_PORT);
                                creatingNum.getAndIncrement();
                                break;
                            case UPDATING:
                                tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.CREATING.getCode());
                                tblServicePortTargetServiceInfo.setUpdateTime(new Date());
                                servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.ALLOC_PORT);
                                creatingNum.getAndIncrement();
                                break;
                            case NO_RESOURCE:
                            case NOT_SATISFIED:
                            case CREATE_FILED:
                                failedNum.getAndIncrement();
                                break;
                            case REMOVING:
                            case READY:
                            case REMOVED:
                                break;
                        }
                    });

                    if (creatingNum.get() == 0)
                    {
                        if (failedNum.get() == 0)
                        {
                            tblServicePortInfo.setStatus(ServicePortStatus.READY.getCode());
                        }
                        else if (failedNum.get() == tblServicePortTargetServiceInfos.size())
                        {
                            tblServicePortInfo.setStatus(ServicePortStatus.FILED.getCode());
                        }
                        else
                        {
                            tblServicePortInfo.setStatus(ServicePortStatus.PARTIAL_READY.getCode());
                        }
                        tblServicePortInfo.setUpdateTime(new Date());
                        servicePortRepository.updateServicePort(tblServicePortInfo);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadServicePortCreating error:{}", e.getMessage());
        }
    }

    private void loadServicePortUpdating()
    {
        try
        {
            TblServicePortInfoExample example = new TblServicePortInfoExample();
            TblServicePortInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo(ServicePortStatus.UPDATING.getCode());
            List<TblServicePortInfo> tblServicePortInfos = servicePortRepository.getServicePortsByExample(example);

            if (CollectionUtils.isEmpty(tblServicePortInfos))
            {
                return;
            }

            for (TblServicePortInfo tblServicePortInfo : tblServicePortInfos)
            {
                List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId());

                if (CollectionUtils.isEmpty(tblServicePortTargetServiceInfos))
                {
                    tblServicePortInfo.setStatus(ServicePortStatus.READY.getCode());
                    tblServicePortInfo.setUpdateTime(new Date());
                    servicePortRepository.updateServicePort(tblServicePortInfo);
                }
                else
                {
                    AtomicInteger creatingNum = new AtomicInteger();
                    AtomicInteger failedNum = new AtomicInteger();
                    tblServicePortTargetServiceInfos.forEach(tblServicePortTargetServiceInfo -> {
                        TargetServiceStatus targetServiceStatus = TargetServiceStatus.fromCode(tblServicePortTargetServiceInfo.getStatus());
                        switch(targetServiceStatus)
                        {
                            case CREATING:
                                tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.UPDATING.getCode());
                                tblServicePortTargetServiceInfo.setUpdateTime(new Date());
                                servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.ALLOC_PORT);
                                creatingNum.getAndIncrement();
                                break;
                            case UPDATING:
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.ALLOC_PORT);
                                creatingNum.getAndIncrement();
                                break;
                            case NO_RESOURCE:
                            case NOT_SATISFIED:
                            case CREATE_FILED:
                                failedNum.getAndIncrement();
                                break;
                            case READY:
                            case REMOVING:
                            case REMOVED:
                                break;
                        }
                    });

                    if (creatingNum.get() == 0)
                    {
                        if (failedNum.get() == 0)
                        {
                            tblServicePortInfo.setStatus(ServicePortStatus.READY.getCode());
                        }
                        else if (failedNum.get() == tblServicePortTargetServiceInfos.size())
                        {
                            tblServicePortInfo.setStatus(ServicePortStatus.FILED.getCode());
                        }
                        else
                        {
                            tblServicePortInfo.setStatus(ServicePortStatus.PARTIAL_READY.getCode());
                        }
                        tblServicePortInfo.setUpdateTime(new Date());
                        servicePortRepository.updateServicePort(tblServicePortInfo);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadServicePortUpdating error:{}", e.getMessage());
        }
    }

    private void loadServicePortRemoving()
    {
        try
        {
            TblServicePortInfoExample example = new TblServicePortInfoExample();
            TblServicePortInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo(ServicePortStatus.REMOVING.getCode());
            List<TblServicePortInfo> tblServicePortInfos = servicePortRepository.getServicePortsByExample(example);

            if (CollectionUtils.isEmpty(tblServicePortInfos))
            {
                return;
            }

            for (TblServicePortInfo tblServicePortInfo : tblServicePortInfos)
            {
                List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByServicePort(tblServicePortInfo.getId());

                if (CollectionUtils.isEmpty(tblServicePortTargetServiceInfos))
                {
                    tblServicePortInfo.setStatus(ServicePortStatus.REMOVED.getCode());
                    tblServicePortInfo.setUpdateTime(new Date());
                    servicePortRepository.updateServicePort(tblServicePortInfo);
                }
                else
                {
                    tblServicePortTargetServiceInfos.forEach(tblServicePortTargetServiceInfo -> {
                        TargetServiceStatus targetServiceStatus = TargetServiceStatus.fromCode(tblServicePortTargetServiceInfo.getStatus());
                        switch(targetServiceStatus)
                        {
                            case CREATING:
                            case UPDATING:
                            case READY:
                                tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.REMOVING.getCode());
                                tblServicePortTargetServiceInfo.setUpdateTime(new Date());
                                servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.RELEASE_PORT);
                                break;
                            case NO_RESOURCE:
                            case NOT_SATISFIED:
                            case CREATE_FILED:
                                tblServicePortTargetServiceInfo.setStatus(TargetServiceStatus.REMOVED.getCode());
                                tblServicePortTargetServiceInfo.setUpdateTime(new Date());
                                servicePortRepository.updateServicePortTargetService(tblServicePortTargetServiceInfo);
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.RELEASE_PORT);
                                break;
                            case REMOVING:
                                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.RELEASE_PORT);
                                break;
                            case REMOVED:
                                break;
                        }
                    });
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadCloudRemoving error:{}", e.getMessage());
        }
    }

    private void loadServicePortTargetServiceRemoving()
    {
        try
        {
            TblServicePortTargetServiceInfoExample example = new TblServicePortTargetServiceInfoExample();
            TblServicePortTargetServiceInfoExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo(ServicePortStatus.REMOVING.getCode());
            List<TblServicePortTargetServiceInfo> tblServicePortTargetServiceInfos = servicePortRepository.getServicePortTargetServicesByExample(example);

            if (CollectionUtils.isEmpty(tblServicePortTargetServiceInfos))
            {
                return;
            }

            for (TblServicePortTargetServiceInfo tblServicePortTargetServiceInfo : tblServicePortTargetServiceInfos)
            {
                sendMessageToMsgProcessorIfNotInQueue(tblServicePortTargetServiceInfo.getId(), ServiceManagerMsg.RELEASE_PORT);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LOGGER.error("loadServicePortTargetServiceRemoving error:{}", e.getMessage());
        }
    }

    public void sendMessageToMsgProcessorIfNotInQueue(Object o, String msgType)
    {
        MessagePack messagePack = new MessagePack();
        messagePack.setMessageObj(o);
        messagePack.setMsgType(msgType);
        serviceManagerMsgProcessStrategy.sendMessageIfNotInQueue(messagePack, ProcessorName.SERVICE_MANAGER_MSG);
    }
}
