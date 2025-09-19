package com.lnjoying.justice.cluster.manager.domain.dto.model.cluster;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: Merak
 * @Date: 2022/8/11 11:32
 */

@Data
public class K3sConfig extends ClusterEngineConfig implements Serializable
{
    @NotBlank(message = "version is required")
    @ApiModelProperty(example = "v1.21.7+k3s1")
    @SerializedName("k3s_version")
    @JsonProperty("k3s_version")
    //@Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9_.]{3,20}$")
    private String k3sVersion;

    @ApiModelProperty(example = "300")
    @SerializedName("deploy_job_time")
    @JsonProperty("deploy_job_time")
    private int deployJobTime = 0;

    @ApiModelProperty(example = "containerd")
    @SerializedName("runtime_type")
    @JsonProperty("runtime_type")
    private String runtimeType = "containerd";

    @ApiModelProperty(example = "")
    @SerializedName("runtime_endpoint")
    @JsonProperty("runtime_endpoint")
    private String runtimeEndpoint;

    @ApiModelProperty(example = "")
    @SerializedName("runtime_version")
    @JsonProperty("runtime_version")
    private String runtimeVersion;

    @ApiModelProperty(example = "[{},{}]")
    @SerializedName("registries")
    @JsonProperty("registries")
    private List<RegistryInfo> registries;

    @ApiModelProperty(example = "{}")
    @SerializedName("data_store_config")
    @JsonProperty("data_store_config")
    private DataStoreConfig dataStoreConfig;

    @ApiModelProperty(example = "{}")
    @SerializedName("network_config")
    @JsonProperty("network_config")
    private NetworkConfig networkConfig;

    @ApiModelProperty(example = "{}")
    @SerializedName("extra_args")
    @JsonProperty("extra_args")
    private K3SServiceExtraArgs k3SServiceExtraArgs;

    @Data
    public static final class DataStoreConfig
    {
        @ApiModelProperty(example = "")
        @SerializedName("enable_etcd")
        @JsonProperty("enable_etcd")
        private boolean enableEtcd;

        @ApiModelProperty(example = "")
        @SerializedName("datastore_endpoint")
        @JsonProperty("datastore_endpoint")
        private String datastoreEndpoint;

        @ApiModelProperty(example = "")
        @SerializedName("datastore_cafile")
        @JsonProperty("datastore_cafile")
        private String datastoreCaFile;

        @ApiModelProperty(example = "")
        @SerializedName("datastore_certfile")
        @JsonProperty("datastore_certfile")
        private String datastoreCertFile;

        @ApiModelProperty(example = "")
        @SerializedName("datastore_keyfile")
        @JsonProperty("datastore_keyfile")
        private String datastoreKeyfile;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_expose_metrics")
        @JsonProperty("etcd_expose_metrics")
        private boolean etcdExposeMetrics;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_disable_snapshots")
        @JsonProperty("etcd_disable_snapshots")
        private boolean etcdDisableSnapshots;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_snapshot_name")
        @JsonProperty("etcd_snapshot_name")
        private String  etcdSnapshotName;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_snapshot_schedule_cron")
        @JsonProperty("etcd_snapshot_schedule_cron")
        private String etcdSnapshotScheduleCron;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_snapshot_retention")
        @JsonProperty("etcd_snapshot_retention")
        private int  etcdSnapshotRetention;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_snapshot_dir")
        @JsonProperty("etcd_snapshot_dir")
        private String  etcdSnapshotDir;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3")
        @JsonProperty("etcd_s3")
        private boolean etcdS3;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_endpoint")
        @JsonProperty("etcd_s3_endpoint")
        private String etcdS3Endpoint;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_endpoint_ca")
        @JsonProperty("etcd_s3_endpoint_ca")
        private String  etcdS3EndpointCa;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_skip_ssl_verify")
        @JsonProperty("etcd_s3_skip_ssl_verify")
        private boolean etcdS3SkipSslVerify;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_access_key")
        @JsonProperty("etcd_s3_access_key")
        private String etcdS3AccessKey;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_secret_key")
        @JsonProperty("etcd_s3_secret_key")
        private String etcdS3SecretKey;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_bucket")
        @JsonProperty("etcd_s3_bucket")
        private String  etcdS3Bucket;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_region")
        @JsonProperty("etcd_s3_region")
        private String etcdS3Region;

        @ApiModelProperty(example = "")
        @SerializedName("etcd_s3_folder")
        @JsonProperty("etcd_s3_folder")
        private String etcdS3Folder;

    }

    @Data
    public static final class NetworkConfig
    {
        @ApiModelProperty(example = "")
        @SerializedName("cluster_cidr")
        @JsonProperty("cluster_cidr")
        private String clusterCidr;

        @ApiModelProperty(example = "")
        @SerializedName("service_cidr")
        @JsonProperty("service_cidr")
        private String serviceCidr;

        @ApiModelProperty(example = "")
        @SerializedName("service_node_port_range")
        @JsonProperty("service_node_port_range")
        private String serviceNodePortRange;

        @ApiModelProperty(example = "")
        @SerializedName("cluster_dns")
        @JsonProperty("cluster_dns")
        private String clusterDns;

        @ApiModelProperty(example = "")
        @SerializedName("cluster_domain")
        @JsonProperty("cluster_domain")
        private String clusterDomain;
    }

    public static final class K3SServiceExtraArgs
    {
        @ApiModelProperty(example = "")
        @SerializedName("etcd_arg")
        @JsonProperty("etcd_arg")
        private Map<String, String> etcdArg;

        @ApiModelProperty(example = "")
        @SerializedName("kube_apiserver_arg")
        @JsonProperty("kube_apiserver_arg")
        private Map<String, String> kubeApiServerArg;

        @ApiModelProperty(example = "")
        @SerializedName("kube_scheduler_arg")
        @JsonProperty("kube_scheduler_arg")
        private Map<String, String> kubeSchedulerArg;

        @ApiModelProperty(example = "")
        @SerializedName("kube_controller_manager_arg")
        @JsonProperty("kube_controller_manager_arg")
        private Map<String, String> kubeControllerManagerArg;

        @ApiModelProperty(example = "")
        @SerializedName("kubelet_arg")
        @JsonProperty("kubelet_arg")
        private Map<String, String> kubeletArg;

        @ApiModelProperty(example = "")
        @SerializedName("kube_proxy_arg")
        @JsonProperty("kube_proxy_arg")
        private Map<String, String> kubeProxyArg;

    }

}
