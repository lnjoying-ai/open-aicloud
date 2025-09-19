package com.micro.core.process.service;

import com.micro.core.process.processor.AbstractRunnableProcessor;
import com.micro.core.process.processor.NewProcessorInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * base class for process service
 */
public class ProcessMultiStrategy
{
    private static Logger LOGGER = LogManager.getLogger();
    private ExecutorService processorPool;
    private int threadNum = 5;
    private int maxQueueSize = 10000;
    Map<String,BlockingQueue<Object>> queueMap = new HashMap<>();
    private List<AbstractRunnableProcessor> processors = null;

    public ProcessMultiStrategy(String desc, int threadNum, int maxQueueSize)
    {
        this.threadNum = threadNum;
        this.maxQueueSize = maxQueueSize;
        processors = new ArrayList<>();
        this.maxQueueSize = maxQueueSize;

        processorPool = Executors.newFixedThreadPool(threadNum, new ProcessorsThreadFactory(desc));
    };
    
    public ProcessMultiStrategy(String desc, int threadNum)
    {
        this.threadNum = threadNum;
        processors = new ArrayList<>();
        
        processorPool = Executors.newFixedThreadPool(threadNum, new ProcessorsThreadFactory(desc));
    };


    public void start(NewProcessorInterface processorInterface, int num, String processName, int queueSize)
    {
        for (int i=0; i< num; i++)
        {
            start(processorInterface, processName, queueSize);
        }
    }
    
    public void start(NewProcessorInterface processorInterface, String processName, int queueSize)
    {
        AbstractRunnableProcessor processor = processorInterface.newProcessor();
        BlockingQueue<Object> queue = queueMap.get(processName);
        if (queue == null && queueSize > 0)
        {
            queue = new LinkedBlockingQueue<>(queueSize);
            queueMap.put(processName, queue);
        }
        if (queue != null)
        {
            processor.setBlockQueue(queue);
        }
        
        processor.start();
        processorPool.execute(processor);
        processors.add(processor);
        
        LOGGER.info("Started processing thread with queue size of {}.", queueSize);
    }
    
    public void start(NewProcessorInterface processorInterface, int num, String processName)
    {
        for (int i=0; i< num; i++)
        {
            start(processorInterface, processName);
        }
    }

    /**
     *
     * @param processorInterface: impl process for task, which will called
     */
    public void start(NewProcessorInterface processorInterface, String processName)
    {
        AbstractRunnableProcessor processor = processorInterface.newProcessor();
        BlockingQueue<Object> queue = queueMap.get(processName);
        if (queue == null && maxQueueSize > 0)
        {
            queue = new LinkedBlockingQueue<>(maxQueueSize);
            queueMap.put(processName, queue);
        }
        if (queue != null)
        {
            processor.setBlockQueue(queue);
        }

        processor.start();
        processorPool.execute(processor);
        processors.add(processor);

        LOGGER.info("Started processing thread with queue size of {} and {} threads.", maxQueueSize, threadNum);
    }

    public void stop()
    {

        processorPool.shutdownNow();

        processors.forEach(processor -> processor.stop());
    }

    public void sendMessage(Object obj, String processName)
    {
        BlockingQueue<Object> queue = queueMap.get(processName);
        if (queue == null)
        {
            queue = new LinkedBlockingQueue<>(maxQueueSize);
            queueMap.put(processName, queue);
        }
        
        try
        {
            queue.put(obj);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("Interrupted while sending message to queue.", e);
        }
    }

    public void sendMessageIfNotInQueue(Object obj, String processName)
    {
        BlockingQueue<Object> queue = queueMap.get(processName);
        if (queue == null)
        {
            queue = new LinkedBlockingQueue<>(maxQueueSize);
            queueMap.put(processName, queue);
        }

        try
        {
            if (!queue.contains(obj))
            {
                queue.put(obj);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public int getTaskQueueLength(String processName)
    {
        BlockingQueue<Object> queue = queueMap.get(processName);
        if (queue == null)
        {
            return 0;
        }
        return queue.size();
    }

}
