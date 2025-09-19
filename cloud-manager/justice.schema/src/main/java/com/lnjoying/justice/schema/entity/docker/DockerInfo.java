package com.lnjoying.justice.schema.entity.docker;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/24/21 4:59 PM
 * @Description:
 */
@Data
public class DockerInfo
{
    @JsonProperty("Architecture")
    @SerializedName("Architecture")
    private String architecture;
    @JsonProperty("Containers")
    @SerializedName("Containers")
    private Integer containers;
    @JsonProperty("ContainersStopped")
    @SerializedName("ContainersStopped")
    private Integer containersStopped;
    @JsonProperty("ContainersPaused")
    @SerializedName("ContainersPaused")
    private Integer containersPaused;
    @JsonProperty("ContainersRunning")
    @SerializedName("ContainersRunning")
    private Integer containersRunning;
    @JsonProperty("CpuCfsPeriod")
    @SerializedName("CpuCfsPeriod")
    private Boolean cpuCfsPeriod;
    @JsonProperty("CpuCfsQuota")
    @SerializedName("CpuCfsQuota")
    private Boolean cpuCfsQuota;
    @JsonProperty("CPUShares")
    @SerializedName("CPUShares")
    private Boolean cpuShares;
    @JsonProperty("CPUSet")
    @SerializedName("CPUSet")
    private Boolean cpuSet;
    @JsonProperty("Debug")
    @SerializedName("Debug")
    private Boolean debug;
    @JsonProperty("DiscoveryBackend")
    @SerializedName("DiscoveryBackend")
    private String discoveryBackend;
    @JsonProperty("DockerRootDir")
    @SerializedName("DockerRootDir")
    private String dockerRootDir;
    @JsonProperty("Driver")
    @SerializedName("Driver")
    private String driver;
    @JsonProperty("DriverStatus")
    @SerializedName("DriverStatus")
    private List<List<String>> driverStatuses;
    @JsonProperty("SystemStatus")
    @SerializedName("SystemStatus")
    private List<Object> systemStatus;
    @JsonProperty("Plugins")
    @SerializedName("Plugins")
    private Map<String, List<String>> plugins;
    @JsonProperty("ExecutionDriver")
    @SerializedName("ExecutionDriver")
    private String executionDriver;
    @JsonProperty("LoggingDriver")
    @SerializedName("LoggingDriver")
    private String loggingDriver;
    @JsonProperty("ExperimentalBuild")
    @SerializedName("ExperimentalBuild")
    private Boolean experimentalBuild;
    @JsonProperty("HttpProxy")
    @SerializedName("HttpProxy")
    private String httpProxy;
    @JsonProperty("HttpsProxy")
    @SerializedName("HttpsProxy")
    private String httpsProxy;
    @JsonProperty("ID")
    @SerializedName("ID")
    private String id;
    @JsonProperty("IPv4Forwarding")
    @SerializedName("IPv4Forwarding")
    private Boolean ipv4Forwarding;
    @JsonProperty("BridgeNfIptables")
    @SerializedName("BridgeNfIptables")
    private Boolean bridgeNfIptables;
    @JsonProperty("BridgeNfIp6tables")
    @SerializedName("BridgeNfIp6tables")
    private Boolean bridgeNfIp6tables;
    @JsonProperty("Images")
    @SerializedName("Images")
    private Integer images;
    @JsonProperty("IndexServerAddress")
    @SerializedName("IndexServerAddress")
    private String indexServerAddress;
    @JsonProperty("InitPath")
    @SerializedName("InitPath")
    private String initPath;
    @JsonProperty("InitSha1")
    @SerializedName("InitSha1")
    private String initSha1;
    @JsonProperty("KernelVersion")
    @SerializedName("KernelVersion")
    private String kernelVersion;
    @JsonProperty("Labels")
    @SerializedName("Labels")
    private String[] labels;
    @JsonProperty("MemoryLimit")
    @SerializedName("MemoryLimit")
    private Boolean memoryLimit;
    @JsonProperty("MemTotal")
    @SerializedName("MemTotal")
    private Long memTotal;
    @JsonProperty("Name")
    @SerializedName("Name")
    private String name;
    @JsonProperty("NCPU")
    @SerializedName("NCPU")
    private Integer ncpu;
    @JsonProperty("NEventsListener")
    @SerializedName("NEventsListener")
    private Integer nEventsListener;
    @JsonProperty("NFd")
    @SerializedName("NFd")
    private Integer nfd;
    @JsonProperty("NGoroutines")
    @SerializedName("NGoroutines")
    private Integer nGoroutines;
    @JsonProperty("NoProxy")
    @SerializedName("NoProxy")
    private String noProxy;
    @JsonProperty("OomKillDisable")
    @SerializedName("OomKillDisable")
    private Boolean oomKillDisable;
    @JsonProperty("OSType")
    @SerializedName("OSType")
    private String osType;
    @JsonProperty("OomScoreAdj")
    @SerializedName("OomScoreAdj")
    private Integer oomScoreAdj;
    @JsonProperty("OperatingSystem")
    @SerializedName("OperatingSystem")
    private String operatingSystem;
    @JsonProperty("RegistryConfig")
    @SerializedName("RegistryConfig")
    private InfoRegistryConfig registryConfig;
    @JsonProperty("Sockets")
    @SerializedName("Sockets")
    private String[] sockets;
    @JsonProperty("SwapLimit")
    @SerializedName("SwapLimit")
    private Boolean swapLimit;
    @JsonProperty("SystemTime")
    @SerializedName("SystemTime")
    private String systemTime;
    @JsonProperty("ServerVersion")
    @SerializedName("ServerVersion")
    private String serverVersion;
    @JsonProperty("ClusterStore")
    @SerializedName("ClusterStore")
    private String clusterStore;
    @JsonProperty("ClusterAdvertise")
    @SerializedName("ClusterAdvertise")
    private String clusterAdvertise;
    @JsonProperty("Swarm")
    @SerializedName("Swarm")
    private Object swarm;
    @JsonProperty("Isolation")
    @SerializedName("Isolation")
    private String isolation;
    @JsonProperty("SecurityOptions")
    @SerializedName("SecurityOptions")
    private List<String> securityOptions;
}
