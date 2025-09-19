package com.lnjoying.justice.cmp.domain.dto.request.openstack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.Map;

@Data
public class OSServiceEndpoints
{
    private Map<String, Endpoint> endpoints;

    @SerializedName("project_id")
    @JsonProperty("project_id")
    private String projectId;

    @Data
    public static class Endpoint
    {
        private String type;
        private String name;
        private String url;
        private String version;
    }

    public enum OSService
    {
        AODH("alarming", "aodh", 8042, ""),
        GLANCE("image", "glance", 9292, ""),
        CINDERV2("volumev2", "cinderv2", 8776, ""),
        CINDERV3("volumev3", "cinderv3", 8776, ""),
        CEILOMETER("metering", "ceilometer", 8777, ""),
        NOVA("compute", "nova", 8774, ""),
        PLACEMENT("placement", "placement", 8778, ""),
        SWIFT("object-store", "swift", 8080, ""),
        NEUTRON("network", "neutron", 9696, ""),
        GNOCCHI("metric", "gnocchi", 8041, ""),
        VNC("vnc", "vnc", 6080, ""),
        KEYSTONE("identity", "keystone", 5000, "");

        OSService(String type, String name, int port, String urlPrefix)
        {
            this.type = type;
            this.name = name;
            this.port = port;
            this.urlPrefix = urlPrefix;
        }

        private String type;
        private String name;
        private int port;
        private String urlPrefix;

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public int getPort() {
            return port;
        }

        public String getUrlPrefix()
        {
            return urlPrefix;
        }
    }

    public enum OSServiceResource
    {
        TYPES("types", "cinderv3"),
        DEFAULT_TYPES("default-types", "cinderv3"),
        VOLUME("volumes", "cinderv3"),
        MANAGEABLE_VOLUMES("manageable_volumes", "cinderv3"),
        VOLUME_SNAPSHOT("snapshots", "cinderv3"),
        MANAGEABLE_SNAPSHOTS("manageable_snapshots", "cinderv3"),
        OS_VOLUME_TRANSFER("os-volume-transfer", "cinderv3"),
        VOLUME_TRANSFERS("volume-transfers", "cinderv3"),
        ATTACHMENTS("attachments", "cinderv3"),
        SCHEDULER_STATS("scheduler-stats", "cinderv3"),
        BACKUPS("backups", "cinderv3"),
        CAPABILITIES("capabilities", "cinderv3"),
        CLUSTERS("clusters", "cinderv3"),
        CONSISTENCYGROUPS("consistencygroups", "cinderv3"),
        CGSNAPSHOTS("cgsnapshots", "cinderv3"),
        GROUPS("groups", "cinderv3"),
        GROUP_SNAPSHOTS("group_snapshots", "cinderv3"),
        GROUP_TYPES("group_types", "cinderv3"),
        OS_HOSTS("os-hosts", "cinderv3"),

        SERVER("servers", "nova"),
        FLAVOR("flavors", "nova"),
        KEYPAIR("os-keypairs", "nova"),
        LIMIT("limits", "nova"),
        OS_AGGREGATES("os-aggregates", "nova"),
        OS_ASSISTED_VOLUME_SNAPSHOTS("os-assisted-volume-snapshots", "nova"),
        OS_AVAILABILITY_ZONE("os-availability-zone", "nova"),
        OS_HYPERVISORS("os-hypervisors", "nova"),
        OS_INSTANCE_USAGE_AUDIT_LOG("os-instance_usage_audit_log", "nova"),
        OS_MIGRATIONS("os-migrations", "nova"),
        OS_QUOTA_SETS("os-quota-sets", "nova"),
        OS_QUOTA_CLASS_SETS("os-quota-class-sets", "nova"),
        OS_SERVER_GROUPS("os-server-groups", "nova"),
        OS_SIMPLE_TENANT_USAGE("os-simple-tenant-usage", "nova"),
        OS_SERVER_EXTERNAL_EVENTS("os-server-external-events", "nova"),
        OS_CERTIFICATES("os-certificates", "nova"),
        OS_CLOUDPIPE("os-cloudpipe", "nova"),
        OS_FPING("os-fping", "nova"),
        OS_FIXED_IPS("os-fixed-ips", "nova"),
        OS_FLOATING_IPS_BULK("os-floating-ips-bulk", "nova"),
        OS_FLOATING_IP_DNS("os-floating-ip-dns", "nova"),
        OS_CELLS("os-cells", "nova"),
        OS_SECURITY_GROUP_DEFAULT_RULES("os-security-group-default-rules", "nova"),
        OS_AGENTS("os-agents", "nova"),

        VNC_AUTO("vnc_auto.html", "vnc"),
        VNC_LITE("vnc_lite.html", "vnc"),
        VNC_VENDOR("vendor", "vnc"),
        VNC_CORE("core", "vnc"),

        NETWORK("networks", "neutron"),
        NETWORK_SEGMENT_RANGES("network_segment_ranges", "neutron"),
        PORT("ports", "neutron"),
        SEGMENTS("segments", "neutron"),
        TRUNKS("trunks", "neutron"),
        ADDRESS_SCOPES("address-scopes", "neutron"),
        ROUTER("routers", "neutron"),
        FLOATING_IP("floatingips", "neutron"),
        NDP_PROXIES("ndp_proxies", "neutron"),
        SUBNETPOOLS("subnetpools", "neutron"),
        SUBNET("subnets", "neutron"),
        LOCAL_IPS("local_ips", "neutron"),
        ADDRESS_GROUPS("address-groups", "neutron"),
        FWAAS("fwaas", "neutron"),
        RBAC_POLICIES("rbac-policies", "neutron"),
        SECURITY_GROUP_RULES("security-group-rules", "neutron"),
        SECURITY_GROUPS("security-groups", "neutron"),
        DEDAULT_SECURITY_GROUP_RULES("default-security-group-rules", "neutron"),
        VPN("vpn", "neutron"),
        METERING("metering", "neutron"),
        NETWORK_IP_AVAILABILITIES("network-ip-availabilities", "neutron"),
        QUOTAS("quotas", "neutron"),
        SERVICE_PROVIDERS("service-providers", "neutron"),
        QOS("qos", "neutron"),
        BGVPN("bgpvpn", "neutron"),

        IMAGES("images", "glance"),

        AUTH("auth", "keystone"),
        DOMAINS("domains", "keystone"),
        PROJECTS("projects", "keystone"),

        FW("fw", "neutron");

        OSServiceResource(String name, String service)
        {
            this.name = name;
            this.service = service;
        }

        private String name;

        private String service;

        public String getName() {
            return name;
        }

        public String getService() {
            return service;
        }

        public static OSServiceResource fromName(String name)
        {
            for (OSServiceResource current : OSServiceResource.values())
            {
                if (current.getName().equalsIgnoreCase(name))
                {
                    return current;
                }
            }
            return null;
        }
    }
}
