package com.lnjoying.justice.schema.entity.docker;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

@Data
public class DockerInstParams implements Serializable
{
    public DockerInstParams()
    {
        hostConfig = new HostConfig();
    }
    @SerializedName("Hostname")
    private String hostName;
    @SerializedName("Domainname")
    private String domainName;
    @SerializedName("User")
    private String user;
    @SerializedName("AttachStdin")
    private Boolean attachStdin;
    @SerializedName("AttachStdout")
    private Boolean attachStdout;
    @SerializedName("AttachStderr")
    private Boolean attachStderr;
    @SerializedName("PortSpecs")
    private String[] portSpecs;
    @SerializedName("Tty")
    private Boolean tty;
    @SerializedName("OpenStdin")
    private Boolean stdinOpen;
    @SerializedName("StdinOnce")
    private Boolean stdInOnce;
    @SerializedName("Env")
    private List<String> env;
    @SerializedName("Cmd")
    private String[] cmd;
    @SerializedName("Healthcheck")
    private HealthCheck healthcheck;
    @SerializedName("ArgsEscaped")
    private Boolean argsEscaped;
    @SerializedName("Entrypoint")
    private String[] entrypoint;
    @SerializedName("Image")
    private String image;
    @SerializedName("Volumes")
    private Map<String,Map<String,String>> volumes;
//    private Volumes volumes = new Volumes(new Volume[0]);
    
    @SerializedName("WorkingDir")
    private String workingDir;
    @SerializedName("MacAddress")
    private String macAddress;
    @SerializedName("OnBuild")
    private List<String> onBuild;
    @SerializedName("NetworkDisabled")
    private Boolean networkDisabled;
    @SerializedName("ExposedPorts")
    private Map<String,Map<String,String>> exposedPorts;
    @SerializedName("StopSignal")
    private String stopSignal;
    @SerializedName("StopTimeout")
    private Integer stopTimeout;
    @SerializedName("HostConfig")
    private HostConfig hostConfig = new HostConfig();
    @SerializedName("Labels")
    private Map<String, String> labels;
    @SerializedName("Shell")
    private List<String> shell;
    
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException
    {
        Gson gson = new Gson();
        DockerInstParams dockerInstParams = new DockerInstParams();
        dockerInstParams.setHostName("11111");
        HostConfig hostConfig = new HostConfig();
        dockerInstParams.setHostConfig(hostConfig);
        String ttt = gson.toJson(dockerInstParams);
        System.out.println(ttt);
    }
    
//    String Hostname;                          // Hostname
//    String Domainname;                        // Domainname
//    String User;                              // User that will run the command(s) inside the container, also support user:group
//    Boolean AttachStdin;                      // Attach the standard input, makes possible user interaction
//    Boolean AttachStdout;                     // Attach the standard output
//    Boolean AttachStderr;                     // Attach the standard error
//    Map<String, Map<String,String>> ExposedPorts;   // List of exposed ports
//    Boolean Tty;                              // Attach standard streams to a tty, including stdin if it is not closed.
//    Boolean OpenStdin                         // Open stdin
//    StdinOnce       bool                // If true, close stdin after the 1 attached client disconnects.
//    Env             []string            // List of environment variable to set in the container
//    Cmd             strslice.StrSlice   // Command to run when starting the container
//    Healthcheck     *HealthConfig       `json:",omitempty"` // Healthcheck describes how to check the container is healthy
//    ArgsEscaped     bool                `json:",omitempty"` // True if command is already escaped (meaning treat as a command line) (Windows specific).
//    Image           string              // Name of the image as it was passed by the operator (e.g. could be symbolic)
//    Volumes         map[string]struct{} // List of volumes (mounts) used for the container
//    WorkingDir      string              // Current directory (PWD) in the command will be launched
//    Entrypoint      strslice.StrSlice   // Entrypoint to run when starting the container
//    NetworkDisabled bool                `json:",omitempty"` // Is network disabled
//    MacAddress      string              `json:",omitempty"` // Mac Address of the container
//    OnBuild         []string            // ONBUILD metadata that were defined on the image Dockerfile
//    Labels          map[string]string   // List of labels set to this container
//    StopSignal      string              `json:",omitempty"` // Signal to stop a container
//    StopTimeout     *int                `json:",omitempty"` // Timeout (in seconds) to stop a container
//    Shell           strslice.StrSlice   `json:",omitempty"` // Shell for shell-form of RUN, CMD, ENTRYPOINT
}
