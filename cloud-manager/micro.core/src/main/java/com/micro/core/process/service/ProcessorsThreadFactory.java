package com.micro.core.process.service;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ProcessorsThreadFactory implements ThreadFactory
{
    /** Counts threads */
    private AtomicInteger counter = new AtomicInteger();
    private String threadDesc;
    public ProcessorsThreadFactory(String desc)
    {
        this.threadDesc = desc;
    }

    public Thread newThread(Runnable r)
    {
        return new Thread(r,threadDesc + counter.incrementAndGet());
    }
}
