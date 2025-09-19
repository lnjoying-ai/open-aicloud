package com.lnjoying.justice.cis.domain.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class InspectInfo
{
    @SerializedName("Id")
    private String id;

    @SerializedName("Created")
    private Date created;

    @SerializedName("Path")
    private String path;

    @SerializedName("Args")
    private List<String> args;

    @SerializedName("State")
    private InspectState state;

    @SerializedName("Image")
    private String image;

    @SerializedName("ResolvConfPath")
    private String resolvConfPath;

    @SerializedName("HostnamePath")
    private String hostnamePath;

    @SerializedName("HostsPath")
    private String hostsPath;

    @SerializedName("LogPath")
    private String logPath;

//    private Object Node;

    @SerializedName("Name")
    private String name;

    @SerializedName("RestartCount")
    private Integer restartCount;

    @SerializedName("Driver")
    private String driver;

    @SerializedName("MountLabel")
    private String mountLabel;

    @SerializedName("ProcessLabel")
    private String processLabel;

    @SerializedName("AppArmorProfile")
    private String appArmorProfile;

//    private String execIDs;

//    private HostConfig hostConfig;

//    private GraphDriverData graphDriver;

    @SerializedName("SizeRw")
    private Long sizeRw;

    @SerializedName("SizeRootFs")
    private Long sizeRootFs;

//    private MountPoint mounts;

//    private ContainerConfig config;

//    private NetworkSettings NetworkSettings;

    @Data
    public class InspectState
    {
        @SerializedName("Status")
        private String status;

        @SerializedName("Running")
        private Boolean running;

        @SerializedName("Paused")
        private Boolean paused;

        @SerializedName("Restarting")
        private Boolean restarting;

        @SerializedName("OOMKilled")
        private Boolean oOMKilled;

        @SerializedName("Dead")
        private Boolean dead;

        @SerializedName("Pid")
        private Integer pid;

        @SerializedName("ExitCode")
        private Integer exitCode;

        @SerializedName("Error")
        private String error;

        @SerializedName("StartedAt")
        private Date startedAt;

        @SerializedName("FinishedAt")
        private Date finishedAt;
    }

    public enum InspectStateEnum
    {
        CREATED("created"),
        RUNNING("running"),
        PAUSED("paused"),
        RESTARTING("restarting"),
        REMOVING("removing"),
        EXITED("exited"),
        DEAD("dead");

        private String name;

        InspectStateEnum(String name)
        {
            this.name = name;
        }
    }
}
