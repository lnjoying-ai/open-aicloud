package com.lnjoying.justice.aos.helm.inner;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 20:07
 */

@Slf4j
public class CommandLineExecutorRunner
{
    public static final int INTERRUPTED_EXIT_CODE = -1000;

    public static final long DEFAULT_PROCESS_TIMEOUT = 60;

    private  static  ExecutorService  executor;

    public CommandLineExecutorRunner()
    {
        this(null);
    }

    public CommandLineExecutorRunner(ExecutorService  executor)
    {
        if (Objects.nonNull(executor))
        {
           this.executor =  executor;
        }
        else
        {
            if (Objects.isNull(this.executor))
            {
                this.executor = newSingleScheduledExecutorService();
            }

        }
    }

    public int run(File currentDirectory, Consumer<String> outputProcessor, String... command)
    {
        Process process = null;

        try
        {
            process = new ProcessBuilder().command(command)
                    .directory(currentDirectory).redirectErrorStream(true).start();

        }
        catch (IOException e)
        {
            throw new UncheckedIOException(String.format("I/O Error while executing command [%s] in directory [%s]", command[0], currentDirectory.getAbsolutePath()), e);
        }

        Future<?> futureTask = executor.submit(new StreamGobbler(process.getInputStream(), outputProcessor));
        //Executor backgroundProcessor = Executors.newSingleThreadExecutor();
        //CompletableFuture.runAsync(new StreamGobbler(process.getInputStream(), outputProcessor))
        int exitCode = 0;

/*
        try {
            process.waitFor(DEFAULT_PROCESS_TIMEOUT, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            exitCode = INTERRUPTED_EXIT_CODE;
        }
*/

        try
        {
            exitCode = process.waitFor();
            futureTask.get();
        }
        catch (InterruptedException| ExecutionException e)
        {
            exitCode = INTERRUPTED_EXIT_CODE;
        }

        return exitCode;
    }

    private ScheduledExecutorService newSingleScheduledExecutorService()
    {
        ThreadFactory threadFactory = r -> {
            Thread t = new Thread(r);
            t.setName("helm-command-executor");
            t.setDaemon(true);
            return t;
        };

        return ExecutorFactory.newSingleScheduledExecutorService(threadFactory);
    }


}
