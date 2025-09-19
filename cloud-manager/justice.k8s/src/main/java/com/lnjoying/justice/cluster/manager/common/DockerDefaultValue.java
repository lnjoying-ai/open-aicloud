package com.lnjoying.justice.cluster.manager.common;

/**
 * @Author: Regulus
 * @Date: 12/9/21 2:42 PM
 * @Description:
 */
public interface DockerDefaultValue
{
    String DockerRegistryURL = "docker.io";
    // RestartTimeout in seconds
    int RestartTimeout = 5;
    // StopTimeout in seconds
    int StopTimeout = 5;
    // RetryCount is the amount of retries for Docker operations
    int RetryCount = 3;
    // WaitTimeout in seconds
    int WaitTimeout = 300;
    // WaitTimeoutContextKey name
    String WaitTimeoutContextKey = "wait_timeout";
}
