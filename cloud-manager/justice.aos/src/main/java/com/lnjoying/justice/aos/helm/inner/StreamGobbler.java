package com.lnjoying.justice.aos.helm.inner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/7/29 20:43
 */

public class StreamGobbler implements Runnable
{

    private InputStream inputStream;

    private Consumer<String> consumer;

    public StreamGobbler(InputStream inputStream, Consumer<String> consumer)
    {
        this.inputStream = inputStream;
        this.consumer = consumer;
    }

    @Override
    public void run()
    {
        new BufferedReader(new InputStreamReader(inputStream)).lines().forEach(consumer);
    }
}
