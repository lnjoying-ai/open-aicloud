package com.micro.core.process.processor;

import java.beans.ExceptionListener;
import java.util.concurrent.BlockingQueue;

public abstract  class AbstractRunnableProcessor implements Runnable, ExceptionListener
{
    private BlockingQueue<Object> queue;
    public AbstractRunnableProcessor()
    {

    }

    public void start()
    {

    }

    public void stop()
    {

    }



    @Override
    public void run()
    {

    }

    @Override
    public void exceptionThrown(Exception e) {

    }

    public void setBlockQueue(BlockingQueue<Object> queue)
    {
        this.queue = queue;
    }

    public BlockingQueue<Object> getBlockQueue()
    {
        return queue;
    }

}
