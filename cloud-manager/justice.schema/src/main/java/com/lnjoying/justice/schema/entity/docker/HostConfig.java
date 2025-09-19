package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Author: Regulus
 * @Date: 11/16/21 11:02 AM
 * @Description:
 */

@Data
public class HostConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private static final List<String> PREDEFINED_NETWORKS = Arrays.asList("bridge", "host", "none");
    @SerializedName("Binds")
    private List<String> binds;              //["/src:/dst","/src1:/dst1"]
    @SerializedName("BlkioWeight")
    private Integer blkioWeight;
    @SerializedName("BlkioWeightDevice")
    private List<BlkioWeightDevice> blkioWeightDevice;
    @SerializedName("BlkioDeviceReadBps")
    private List<BlkioRateDevice> blkioDeviceReadBps;
    @SerializedName("BlkioDeviceWriteBps")
    private List<BlkioRateDevice> blkioDeviceWriteBps;
    @SerializedName("BlkioDeviceReadIOps")
    private List<BlkioRateDevice> blkioDeviceReadIOps;
    @SerializedName("BlkioDeviceWriteIOps")
    private List<BlkioRateDevice> blkioDeviceWriteIOps;
    @SerializedName("MemorySwappiness")
    private Long memorySwappiness;
    @SerializedName("NanoCpus")
    private Long nanoCPUs;
    @SerializedName("CapAdd")
    private List<String> capAdd;
    @SerializedName("CapDrop")
    private List<String>  capDrop;
    @SerializedName("ContainerIDFile")
    private String containerIDFile;
    @SerializedName("CpuPeriod")
    private Long cpuPeriod;
    @SerializedName("CpuRealtimePeriod")
    private Long cpuRealtimePeriod;
    @SerializedName("CpuRealtimeRuntime")
    private Long cpuRealtimeRuntime;
    @SerializedName("CpuShares")
    private Integer cpuShares;
    @SerializedName("CpuQuota")
    private Long cpuQuota;
    @SerializedName("CpusetCpus")
    private String cpusetCpus;
    @SerializedName("CpusetMems")
    private String cpusetMems;
    @SerializedName("Devices")
    private List<Device> devices;
    @SerializedName("DeviceCgroupRules")
    private List<String> deviceCgroupRules;
    @SerializedName("DeviceRequests")
    private List<DeviceRequest> deviceRequests;
    @SerializedName("DiskQuota")
    private Long diskQuota;
    @SerializedName("Dns")
    private List<String>  dns;
    @SerializedName("DnsOptions")
    private List<String> dnsOptions;
    @SerializedName("DnsSearch")
    private List<String>  dnsSearch;
    @SerializedName("ExtraHosts")
    private List<String>  extraHosts;
    @SerializedName("GroupAdd")
    private List<String> groupAdd;
    @SerializedName("IpcMode")
    private String ipcMode;
    @SerializedName("Cgroup")
    private String cgroup;
    @SerializedName("Links")
    private List<String> links;
    @SerializedName("LogConfig")
    private LogConfig logConfig;
    @SerializedName("LxcConf")
    private List<LxcConf> lxcConf;
    @SerializedName("Memory")
    private Long memory;
    @SerializedName("MemorySwap")
    private Long memorySwap;
    @SerializedName("MemoryReservation")
    private Long memoryReservation;
    @SerializedName("KernelMemory")
    private Long kernelMemory;
    @SerializedName("NetworkMode")
    private String networkMode;
    @SerializedName("OomKillDisable")
    private Boolean oomKillDisable;
    @SerializedName("Init")
    private Boolean init;
    @SerializedName("AutoRemove")
    private Boolean autoRemove;
    @SerializedName("OomScoreAdj")
    private Integer oomScoreAdj;
    @SerializedName("PortBindings")
    private Map<String, List<PortBind>> portBindings;
    @SerializedName("Privileged")
    private Boolean privileged;
    @SerializedName("PublishAllPorts")
    private Boolean publishAllPorts;
    @SerializedName("ReadonlyRootfs")
    private Boolean readonlyRootfs;
    @SerializedName("RestartPolicy")
    private RestartPolicy restartPolicy;
    @SerializedName("Ulimits")
    private List<Ulimit> ulimits;
    @SerializedName("CpuCount")
    private Long cpuCount;
    @SerializedName("CpuPercent")
    private Long cpuPercent;
    @SerializedName("IOMaximumIOps")
    private Long ioMaximumIOps;
    @SerializedName("IOMaximumBandwidth")
    private Long ioMaximumBandwidth;
    @SerializedName("VolumesFrom")
    private List<String> volumesFrom; //A list of volumes to inherit from another container,specified in the form <container name>[:<ro|rw>].
    @SerializedName("Mounts")
    private List<Mount> mounts;
    @SerializedName("PidMode")
    private String pidMode;
    @SerializedName("Isolation")
    private String isolation;   //"default""process""hyperv" Isolation technology of the container. (Windows only)
    @SerializedName("SecurityOpt")
    private List<String> securityOpts;
    @SerializedName("StorageOpt")
    private Map<String, String> storageOpt;
    @SerializedName("CgroupParent")
    private String cgroupParent;
    @SerializedName("VolumeDriver")
    private String volumeDriver;
    @SerializedName("ShmSize")
    private Long shmSize;
    @SerializedName("PidsLimit")
    private Long pidsLimit;
    @SerializedName("Runtime")
    private String runtime;
    @SerializedName("Tmpfs")
    private Map<String, String> tmpFs;
    @SerializedName("UTSMode")
    private String utSMode;
    @SerializedName("UsernsMode")
    private String usernsMode;
    @SerializedName("Sysctls")
    private Map<String, String> sysctls;
    @SerializedName("ConsoleSize")
    private List<Integer> consoleSize;
}
