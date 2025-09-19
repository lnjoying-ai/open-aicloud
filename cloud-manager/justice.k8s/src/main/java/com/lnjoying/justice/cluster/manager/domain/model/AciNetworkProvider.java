package com.lnjoying.justice.cluster.manager.domain.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class AciNetworkProvider implements Serializable
{
        private String                   system_id;
    
        private String                  apic_hosts;
    
        private String                       token;
    
        private String              apic_user_name;
    
        private String               apic_user_key;
    
        private String               apic_user_crt;
    
        private String           apic_refresh_time;
    
        private String                  vmm_domain;
    
        private String              vmm_controller;
    
        private String                  encap_type;
    
        private String                 node_subnet;
    
        private String           mcast_range_start;
    
        private String             mcast_range_end;
    
        private String                         aep;
    
        private String                    vrf_name;
    
        private String                  vrf_tenant;
    
        private String                       l3out;
    
        private String     l3out_external_networks;
    
        private String              extern_dynamic;
    
        private String               extern_static;
    
        private String             node_svc_subnet;
        private String               kube_api_vlan;
        private String                service_vlan;
        private String                  infra_vlan;
        private String                      tenant;
    
        private String            ovs_memory_limit;
    
        private String           image_pull_policy;
    
        private String           image_pull_secret;
    
        private String    service_monitor_interval;
    
        private String       pbr_tracking_non_snat;
    
        private String               install_istio;
    
        private String               istio_profile;
    
        private String             drop_log_enable;
    
        private String        controller_log_level;
    
        private String        host_agent_log_level;
    
        private String            opflex_log_level;
    
        private String  use_aci_cni_priority_class;
    
        private String           no_priority_class;
    
        private String         max_nodes_svc_graph;
    
        private String         snat_contract_scope;
    
        private String       pod_subnet_chunk_size;
    
        private String       enable_endpoint_slice;
    
        private String              snat_namespace;
    
        private String                 ep_registry;
    
        private String                 opflex_mode;
    
        private String       snat_port_range_start;
    
        private String         snat_port_range_end;
    
        private String         snat_ports_per_node;
    
        private String           opflex_client_ssl;
    
        private String    use_privileged_container;
    
        private String       use_host_netns_volume;
    
        private String    use_opflex_server_volume;
    
        private String          subnet_domain_name;
    
        private String               kafka_brokers;
    
        private String            kafka_client_crt;
    
        private String            kafka_client_key;
    
        private String                       capic;
    
        private String        use_aci_anywhere_crd;
    
        private String            overlay_vrf_name;
    
        private String              gbp_pod_subnet;
    
        private String           run_gbp_container;
    
        private String run_opflex_server_container;
    
        private String          opflex_server_port;
}
