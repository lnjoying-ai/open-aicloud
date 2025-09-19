/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2024/12/4 17:20:29                           */
/*==============================================================*/


/*==============================================================*/
/* Table: tbl_cloud_agent_info                                  */
/*==============================================================*/
create table tbl_cloud_agent_info (
   agent_id             VARCHAR(64)          not null,
   cloud_id             VARCHAR(64)          null,
   online_status        INT4                 null,
   core_version         VARCHAR(64)          null,
   region_id            VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   status               INT4                 null,
   worker_id            VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_CLOUD_AGENT_INFO primary key (agent_id)
);

/*==============================================================*/
/* Table: tbl_cloud_info                                        */
/*==============================================================*/
create table tbl_cloud_info (
   cloud_id             VARCHAR(64)          not null,
   name                 VARCHAR(128)         null,
   product              VARCHAR(64)          null,
   status               INT4                 null,
   mode                 VARCHAR(20)          null,
   url                  TEXT                 null,
   certificate          TEXT                 null,
   region_id            VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   health_check         TEXT                 null,
   "authorization"      TEXT                 null,
   owner                VARCHAR(64)          null,
   bp                   VARCHAR(64)          null,
   labels               JSONB                null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   os_service_endpoints TEXT                 null,
   version              VARCHAR(20)          null,
   provider_id          VARCHAR(64)          null,
   constraint PK_TBL_CLOUD_INFO primary key (cloud_id)
);

/*==============================================================*/
/* Table: tbl_cmp_baremetal_cluster                             */
/*==============================================================*/
create table tbl_cmp_baremetal_cluster (
   cloud_id             VARCHAR(64)          not null,
   cluster_id           VARCHAR(64)          not null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_BAREMETAL_CLUSTER primary key (cloud_id, cluster_id)
);

/*==============================================================*/
/* Table: tbl_cmp_baremetal_device                              */
/*==============================================================*/
create table tbl_cmp_baremetal_device (
   cloud_id             VARCHAR(64)          not null,
   device_id            VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   cluster_id           VARCHAR(64)          null,
   device_spec_id       VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   address_type         INT2                 null,
   ipmi_ip              VARCHAR(40)          null,
   ipmi_port            INT2                 null,
   ipmi_username        VARCHAR(64)          null,
   ipmi_password        VARCHAR(128)         null,
   bmcType              VARCHAR(64)          null,
   ipmi_mac             VARCHAR(17)          null,
   device_id_from_agent VARCHAR(64)          null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_BAREMETAL_DEVICE primary key (cloud_id, device_id)
);

/*==============================================================*/
/* Table: tbl_cmp_baremetal_device_spec                         */
/*==============================================================*/
create table tbl_cmp_baremetal_device_spec (
   cloud_id             VARCHAR(64)          not null,
   device_spec_id       VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   product_name         VARCHAR(128)         null,
   serial_number        VARCHAR(64)          null,
   manufacturer         VARCHAR(256)         null,
   architecture         VARCHAR(32)          null,
   processor_count      INT4                 null,
   cpu_logic_num        INT4                 null,
   cpu_physical_num     INT4                 null,
   cpu_model_name       VARCHAR(64)          null,
   cpu_frequency        FLOAT8               null,
   cpu_num              INT4                 null,
   mem_total            INT8                 null,
   disk_total           INT8                 null,
   disk_type            VARCHAR(20)          null,
   disk_detail          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_BAREMETAL_DEVICE_SP primary key (cloud_id, device_spec_id)
);

/*==============================================================*/
/* Table: tbl_cmp_baremetal_instance                            */
/*==============================================================*/
create table tbl_cmp_baremetal_instance (
   cloud_id             VARCHAR(64)          not null,
   instance_id          VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   device_id            VARCHAR(64)          null,
   phase_status         INT4                 null,
   image_id             VARCHAR(64)          null,
   vpc_id               VARCHAR(64)          null,
   subnet_id            VARCHAR(64)          null,
   static_ip            VARCHAR(40)          null,
   host_name            VARCHAR(128)         null,
   sys_username         VARCHAR(64)          null,
   sys_password         VARCHAR(128)         null,
   pubkey_id            VARCHAR(64)          null,
   iscsi_target         VARCHAR(256)         null,
   iscsi_initiator      VARCHAR(256)         null,
   iscsi_ipport         VARCHAR(256)         null,
   share_id_from_agent  VARCHAR(64)          null,
   nic_id_from_agent    VARCHAR(64)          null,
   port_id_from_agent   VARCHAR(64)          null,
   instance_id_from_agent VARCHAR(64)          null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_BAREMETAL_INSTANCE primary key (cloud_id, instance_id)
);

/*==============================================================*/
/* Table: tbl_cmp_disk_info                                     */
/*==============================================================*/
create table tbl_cmp_disk_info (
   cloud_id             VARCHAR(64)          not null,
   disk_id              VARCHAR(64)          not null,
   volume_id            VARCHAR(64)          null,
   vm_instance_id       VARCHAR(64)          null,
   name                 VARCHAR(128)         null,
   size                 INT4                 null,
   disk_type            INT4                 null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   is_new               BOOL                 null,
   constraint PK_TBL_CMP_DISK_INFO primary key (cloud_id, disk_id)
);

/*==============================================================*/
/* Table: tbl_cmp_disk_spec                                     */
/*==============================================================*/
create table tbl_cmp_disk_spec (
   cloud_id             VARCHAR(64)          null,
   disk_spec_id         VARCHAR(64)          null,
   device_spec_id       VARCHAR(64)          null,
   disk_type            VARCHAR(20)          null,
   vendor               VARCHAR(64)          null,
   model                VARCHAR(64)          null,
   size                 INT8                 null,
   trans_speed          INT8                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null
);

/*==============================================================*/
/* Table: tbl_cmp_eip                                           */
/*==============================================================*/
create table tbl_cmp_eip (
   cloud_id             VARCHAR(64)          not null,
   eip_id               VARCHAR(64)          not null,
   pool_id              VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   address_type         INT2                 null,
   ipaddr               VARCHAR(128)         null,
   prefix_len           INT2                 null,
   status               INT2                 null,
   bandwidth            VARCHAR(32)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   bound_type           VARCHAR(64)          null,
   bound_id             TEXT                 null,
   public_ip            VARCHAR(128)         null,
   charge_type          INT4                 null,
   price_unit           INT4                 null,
   period               INT4                 null,
   expire_time          TIMESTAMP WITH TIME ZONE null,
   bound_ee_id          VARCHAR(64)          null,
   constraint PK_TBL_CMP_EIP primary key (cloud_id, eip_id)
);

/*==============================================================*/
/* Table: tbl_cmp_eip_map                                       */
/*==============================================================*/
create table tbl_cmp_eip_map (
   cloud_id             VARCHAR(64)          not null,
   eip_map_id           VARCHAR(64)          not null,
   map_name             VARCHAR(64)          null,
   eip_id               VARCHAR(64)          null,
   subnet_id            VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   port_id              VARCHAR(64)          null,
   is_static_ip         BOOL                 null,
   inside_ip            VARCHAR(128)         null,
   status               INT2                 null,
   bandwidth            VARCHAR(32)          null,
   is_one_to_one        BOOL                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_EIP_MAP primary key (cloud_id, eip_map_id)
);

/*==============================================================*/
/* Table: tbl_cmp_eip_pool                                      */
/*==============================================================*/
create table tbl_cmp_eip_pool (
   cloud_id             VARCHAR(64)          not null,
   pool_id              VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   description          TEXT                 null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_EIP_POOL primary key (cloud_id, pool_id)
);

/*==============================================================*/
/* Table: tbl_cmp_eip_pool_vpc_ref                              */
/*==============================================================*/
create table tbl_cmp_eip_pool_vpc_ref (
   cloud_id             VARCHAR(64)          not null,
   pool_vpc_id          varchar(64)          not null,
   pool_id              varchar(64)          null,
   vpc_id               VARCHAR(64)          null,
   vlan_id              INT4                 null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_EIP_POOL_VPC_REF primary key (cloud_id, pool_vpc_id)
);

/*==============================================================*/
/* Table: tbl_cmp_es_firewall_bindings                          */
/*==============================================================*/
create table tbl_cmp_es_firewall_bindings (
   cloud_id             VARCHAR(64)          not null,
   firewall_id          VARCHAR(64)          not null,
   router_id            VARCHAR(64)          not null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_TBL_CMP_ES_FIREWALL_BINDING primary key (cloud_id, firewall_id, router_id)
);

/*==============================================================*/
/* Table: tbl_cmp_es_firewall_policies                          */
/*==============================================================*/
create table tbl_cmp_es_firewall_policies (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(256)         null,
   tenant_id            VARCHAR(64)          null,
   project_id           VARCHAR(64)          null,
   shared               INT2                 null,
   description          TEXT                 null,
   audited              INT2                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_TBL_CMP_ES_FIREWALL_POLICIE primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_es_firewall_rules                             */
/*==============================================================*/
create table tbl_cmp_es_firewall_rules (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(256)         null,
   protocol             VARCHAR(16)          null,
   description          TEXT                 null,
   source_port          VARCHAR(64)          null,
   source_ip_address    VARCHAR(64)          null,
   destination_ip_address VARCHAR(64)          null,
   firewall_policy_id   VARCHAR(64)          null,
   "position"           INT4                 null,
   destination_port     VARCHAR(64)          null,
   tenant_id            VARCHAR(64)          null,
   enabled              INT2                 null,
   action               VARCHAR(64)          null,
   ip_version           INT4                 null,
   shared               INT2                 null,
   project_id           VARCHAR(64)          null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_TBL_CMP_ES_FIREWALL_RULES primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_es_firewalls                                  */
/*==============================================================*/
create table tbl_cmp_es_firewalls (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(256)         null,
   admin_state_up       INT2                 null,
   status               VARCHAR(16)          null,
   tenant_id            VARCHAR(64)          null,
   project_id           VARCHAR(64)          null,
   description          TEXT                 null,
   firewall_policy_id   VARCHAR(64)          null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_TBL_CMP_ES_FIREWALLS primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_flavor                                        */
/*==============================================================*/
create table tbl_cmp_flavor (
   cloud_id             VARCHAR(64)          not null,
   flavor_id            VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   type                 INT2                 null,
   cpu                  INT4                 null,
   mem                  INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   phase_status         INT4                 null,
   user_id              VARCHAR(64)          null,
   gpu_count            INT4                 null,
   gpu_name             VARCHAR(128)         null,
   need_ib              BOOL                 null,
   constraint PK_TBL_CMP_FLAVOR primary key (cloud_id, flavor_id)
);

/*==============================================================*/
/* Table: tbl_cmp_hypervisor_node                               */
/*==============================================================*/
create table tbl_cmp_hypervisor_node (
   cloud_id             VARCHAR(64)          not null,
   node_id              VARCHAR(64)          not null,
   instance_id          VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   manage_ip            VARCHAR(40)          null,
   host_name            VARCHAR(128)         null,
   sys_username         VARCHAR(64)          null,
   sys_password         VARCHAR(128)         null,
   pubkey_id            VARCHAR(64)          null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   backup_node_id       VARCHAR(64)          null,
   error_count          INT4                 null,
   cpu_model            VARCHAR(128)         null,
   cpu_log_count        INT4                 null,
   mem_total            INT4                 null,
   cpu_phy_count        INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   available_ib_count   INT4                 null,
   ib_count             INT4                 null,
   constraint PK_TBL_CMP_HYPERVISOR_NODE primary key (cloud_id, node_id)
);

/*==============================================================*/
/* Table: tbl_cmp_image                                         */
/*==============================================================*/
create table tbl_cmp_image (
   cloud_id             VARCHAR(64)          not null,
   image_id             VARCHAR(64)          not null,
   file_id_from_agent   VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   image_os_type        INT2                 null,
   image_os_vendor      INT2                 null,
   image_os_version     VARCHAR(32)          null,
   image_name           VARCHAR(64)          null,
   image_format         INT2                 null,
   agent_ip             VARCHAR(64)          null,
   phase_status         INT2                 null,
   phase_info           VARCHAR(64)          null,
   is_public            BOOL                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   description          TEXT                 null,
   vm_instance_id       VARCHAR(64)          null,
   image_base           VARCHAR(64)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   ee_visibility        BOOL                 null,
   constraint PK_TBL_CMP_IMAGE primary key (cloud_id, image_id)
);

/*==============================================================*/
/* Table: tbl_cmp_instance_group                                */
/*==============================================================*/
create table tbl_cmp_instance_group (
   cloud_id             VARCHAR(64)          not null,
   instance_group_id    VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   description          TEXT                 null,
   user_id              VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   phase_status         INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_INSTANCE_GROUP primary key (cloud_id, instance_group_id)
);

/*==============================================================*/
/* Table: tbl_cmp_instance_network_ref                          */
/*==============================================================*/
create table tbl_cmp_instance_network_ref (
   cloud_id             VARCHAR(64)          not null,
   instance_network_id  VARCHAR(64)          not null,
   instance_id          VARCHAR(64)          null,
   vpc_id               VARCHAR(64)          null,
   subnet_id            VARCHAR(64)          null,
   port_id              VARCHAR(64)          null,
   instance_type        INT4                 null,
   static_ip            VARCHAR(40)          null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   is_vip               BOOL                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_INSTANCE_NETWORK_RE primary key (cloud_id, instance_network_id)
);

/*==============================================================*/
/* Table: tbl_cmp_ip_pool                                       */
/*==============================================================*/
create table tbl_cmp_ip_pool (
   cloud_id             VARCHAR(64)          not null,
   pool_id              VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   address_type         INT2                 null,
   user_id              VARCHAR(64)          null,
   ip_addresses         TEXT                 null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_IP_POOL primary key (cloud_id, pool_id)
);

/*==============================================================*/
/* Table: tbl_cmp_nfs                                           */
/*==============================================================*/
create table tbl_cmp_nfs (
   cloud_id             VARCHAR(64)          not null,
   nfs_id               VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   vpc_id               VARCHAR(64)          null,
   subnet_id            VARCHAR(64)          null,
   port_id              VARCHAR(64)          null,
   size                 INT4                 null,
   nfs_id_from_agent    VARCHAR(64)          null,
   phase_status         INT4                 null,
   node_ip              VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   user_id              VARCHAR(64)          null,
   description          TEXT                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_NFS primary key (cloud_id, nfs_id)
);

/*==============================================================*/
/* Table: tbl_cmp_nic_info                                      */
/*==============================================================*/
create table tbl_cmp_nic_info (
   cloud_id             VARCHAR(64)          not null,
   nic_id               VARCHAR(64)          not null,
   device_id            VARCHAR(64)          null,
   nic_name             VARCHAR(64)          null,
   link_state           VARCHAR(32)          null,
   network_type         INT2                 null,
   ipmi_mac             VARCHAR(17)          null,
   ip                   VARCHAR(40)          null,
   address_type         INT2                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_NIC_INFO primary key (cloud_id, nic_id)
);

/*==============================================================*/
/* Table: tbl_cmp_nic_spec                                      */
/*==============================================================*/
create table tbl_cmp_nic_spec (
   cloud_id             VARCHAR(64)          not null,
   nic_spec_id          VARCHAR(64)          not null,
   device_spec_id       VARCHAR(64)          null,
   nic_product_name     VARCHAR(64)          null,
   speed                VARCHAR(64)          null,
   mac                  VARCHAR(17)          null,
   link_is_up           BOOL                 null,
   slot_id_from_agent   VARCHAR(64)          null,
   phase_status         INT4                 null,
   switch_id_from_agent VARCHAR(64)          null,
   switch_interface     VARCHAR(32)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_NIC_SPEC primary key (cloud_id, nic_spec_id)
);

/*==============================================================*/
/* Table: tbl_cmp_node_image                                    */
/*==============================================================*/
create table tbl_cmp_node_image (
   cloud_id             VARCHAR(64)          not null,
   node_image_id        VARCHAR(64)          not null,
   image_id             VARCHAR(64)          null,
   storage_pool_id      VARCHAR(64)          null,
   node_image_from_agent VARCHAR(64)          null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   user_id              VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   storage_pool_id_from_agent VARCHAR(64)          null,
   node_ip              VARCHAR(64)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_NODE_IMAGE primary key (cloud_id, node_image_id)
);

/*==============================================================*/
/* Table: tbl_cmp_node_storage_pool                             */
/*==============================================================*/
create table tbl_cmp_node_storage_pool (
   cloud_id             VARCHAR(64)          not null,
   node_storage_pool_id VARCHAR(64)          not null,
   storage_pool_id      VARCHAR(64)          null,
   storage_pool_id_from_agent VARCHAR(64)          null,
   node_ip              VARCHAR(64)          null,
   sid                  VARCHAR(64)          null,
   paras                VARCHAR(128)         null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   type                 INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_NODE_STORAGE_POOL primary key (cloud_id, node_storage_pool_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_allowedaddresspairs                        */
/*==============================================================*/
create table tbl_cmp_os_allowedaddresspairs (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(36)          not null,
   mac_address          VARCHAR(32)          not null,
   ip_address           VARCHAR(64)          not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_allowedaddresspairs primary key (cloud_id, port_id, mac_address, ip_address)
);

/*==============================================================*/
/* Table: tbl_cmp_os_backups                                    */
/*==============================================================*/
create table tbl_cmp_os_backups (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   VARCHAR(36)          not null,
   volume_id            VARCHAR(36)          null,
   user_id              VARCHAR(255)         null,
   project_id           VARCHAR(255)         null,
   host                 VARCHAR(255)         null,
   availability_zone    VARCHAR(255)         null,
   display_name         VARCHAR(255)         null,
   display_description  VARCHAR(255)         null,
   container            VARCHAR(255)         null,
   status               VARCHAR(255)         null,
   fail_reason          VARCHAR(255)         null,
   service_metadata     VARCHAR(255)         null,
   service              VARCHAR(255)         null,
   size                 INT4                 null,
   object_count         INT4                 null,
   parent_id            VARCHAR(36)          null,
   temp_volume_id       VARCHAR(36)          null,
   temp_snapshot_id     VARCHAR(36)          null,
   num_dependent_backups INT4                 null,
   snapshot_id          VARCHAR(36)          null,
   data_timestamp       TIMESTAMP WITH TIME ZONE null,
   restore_volume_id    VARCHAR(36)          null,
   encryption_key_id    VARCHAR(36)          null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_backups_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_block_device_mapping                       */
/*==============================================================*/
create table tbl_cmp_os_block_device_mapping (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   id                   INT4                 null,
   device_name          VARCHAR(255)         null,
   delete_on_termination INT2                 null,
   snapshot_id          VARCHAR(36)          null,
   volume_id            VARCHAR(36)          null,
   volume_size          INT4                 null,
   no_device            INT2                 null,
   connection_info      TEXT                 null,
   instance_uuid        VARCHAR(36)          null,
   deleted              INT4                 null,
   source_type          VARCHAR(255)         null,
   destination_type     VARCHAR(255)         null,
   guest_format         VARCHAR(255)         null,
   device_type          VARCHAR(255)         null,
   disk_bus             VARCHAR(255)         null,
   boot_index           INT4                 null,
   image_id             VARCHAR(36)          null,
   tag                  VARCHAR(255)         null,
   attachment_id        VARCHAR(36)          null,
   uuid                 VARCHAR(36)          not null,
   volume_type          VARCHAR(255)         null,
   encrypted            INT2                 null,
   encryption_secret_uuid VARCHAR(36)          null,
   encryption_format    VARCHAR(128)         null,
   encryption_options   VARCHAR(4096)        null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_blockdevicemapping_uuid primary key (cloud_id, uuid)
);

/*==============================================================*/
/* Table: tbl_cmp_os_dnsnameservers                             */
/*==============================================================*/
create table tbl_cmp_os_dnsnameservers (
   cloud_id             VARCHAR(64)          not null,
   address              VARCHAR(128)         not null,
   subnet_id            VARCHAR(36)          not null,
   "order"              INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_dnsnameservers_address_subnet primary key (cloud_id, address, subnet_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_dvr_host_macs                              */
/*==============================================================*/
create table tbl_cmp_os_dvr_host_macs (
   cloud_id             VARCHAR(64)          not null,
   host                 VARCHAR(255)         not null,
   mac_address          VARCHAR(32)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_dvr_host_macs_host primary key (cloud_id, host),
   constraint AK_dvr_host_macs_macaddress unique (cloud_id, mac_address)
);

/*==============================================================*/
/* Table: tbl_cmp_os_extradhcpopts                              */
/*==============================================================*/
create table tbl_cmp_os_extradhcpopts (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(36)          not null,
   port_id              VARCHAR(36)          not null,
   opt_name             VARCHAR(64)          not null,
   opt_value            VARCHAR(255)         null,
   ip_version           INT4                 not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_extradhcpopts_port_opt_ip primary key (cloud_id, port_id, opt_name, ip_version)
);

/*==============================================================*/
/* Table: tbl_cmp_os_flavor_extra_specs                         */
/*==============================================================*/
create table tbl_cmp_os_flavor_extra_specs (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   id                   INT4                 not null,
   flavor_id            VARCHAR(255)         not null,
   key                  VARCHAR(255)         not null,
   value                VARCHAR(255)         null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_flavorextraspecs_flavorid_key primary key (cloud_id, flavor_id, key),
   constraint AK_flavorextraspecs_id unique (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_flavors                                    */
/*==============================================================*/
create table tbl_cmp_os_flavors (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   name                 VARCHAR(255)         null,
   id                   INT4                 null,
   memory_mb            INT4                 null,
   vcpus                INT4                 null,
   swap                 INT4                 null,
   vcpu_weight          INT4                 null,
   flavorid             VARCHAR(255)         not null,
   rxtx_factor          FLOAT4               null,
   root_gb              INT4                 null,
   ephemeral_gb         INT4                 null,
   disabled             INT2                 null,
   is_public            INT2                 null,
   description          TEXT                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_flavors_flavorid primary key (cloud_id, flavorid),
   constraint AK_flavors_name unique (cloud_id, name)
);

/*==============================================================*/
/* Table: tbl_cmp_os_floatingips                                */
/*==============================================================*/
create table tbl_cmp_os_floatingips (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   floating_ip_address  VARCHAR(64)          null,
   floating_network_id  VARCHAR(36)          null,
   floating_port_id     VARCHAR(36)          null,
   fixed_port_id        VARCHAR(36)          null,
   fixed_ip_address     VARCHAR(64)          null,
   router_id            VARCHAR(36)          null,
   last_known_router_id VARCHAR(36)          null,
   status               VARCHAR(16)          null,
   standard_attr_id     INT8                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   dns_name             VARCHAR(255)         null,
   dns_domain           VARCHAR(255)         null,
   published_dns_name   VARCHAR(255)         null,
   published_dns_domain VARCHAR(255)         null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   policy_id            VARCHAR(36)          null,
   constraint PK_floatingip_id primary key (cloud_id, id),
   constraint AK_floatingip_standard_attr_id unique (cloud_id, standard_attr_id),
   constraint AK_floatingip_network_port_ip unique (cloud_id, floating_network_id, fixed_port_id, fixed_ip_address)
);

/*==============================================================*/
/* Table: tbl_cmp_os_images                                     */
/*==============================================================*/
create table tbl_cmp_os_images (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   size                 INT8                 null,
   status               VARCHAR(30)          null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   disk_format          VARCHAR(20)          null,
   container_format     VARCHAR(20)          null,
   checksum             VARCHAR(32)          null,
   owner                VARCHAR(255)         null,
   min_disk             INT4                 null,
   min_ram              INT4                 null,
   protected            INT2                 null,
   virual_size          INT8                 null,
   visibility           VARCHAR(20)          null,
   os_hidden            INT2                 null,
   os_hash_algo         VARCHAR(64)          null,
   os_hash_value        VARCHAR(128)         null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   properties           TEXT                 null,
   constraint PK_images_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_instance_extra                             */
/*==============================================================*/
create table tbl_cmp_os_instance_extra (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT4                 null,
   id                   INT4                 not null,
   instance_uuid        VARCHAR(36)          not null,
   numa_topology        TEXT                 null,
   pci_requests         TEXT                 null,
   flavor               TEXT                 null,
   vcpu_model           TEXT                 null,
   migration_context    TEXT                 null,
   keypairs             TEXT                 null,
   device_metadata      TEXT                 null,
   trusted_certs        TEXT                 null,
   vpmems               TEXT                 null,
   resources            TEXT                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_instance_extra_uuid primary key (cloud_id, instance_uuid)
);

/*==============================================================*/
/* Table: tbl_cmp_os_instance_faults                            */
/*==============================================================*/
create table tbl_cmp_os_instance_faults (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   id                   INT4                 null,
   instance_uuid        VARCHAR(36)          not null,
   code                 INT4                 null,
   message              VARCHAR(255)         null,
   details              TEXT                 null,
   host                 VARCHAR(255)         null,
   deleted              INT4                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_instance_fault_uuid primary key (cloud_id, instance_uuid)
);

/*==============================================================*/
/* Table: tbl_cmp_os_instance_metadata                          */
/*==============================================================*/
create table tbl_cmp_os_instance_metadata (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   id                   INT4                 not null,
   key                  VARCHAR(255)         null,
   value                VARCHAR(255)         null,
   instance_uuid        VARCHAR(36)          null,
   deleted              INT4                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_instance_metadata primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_instances                                  */
/*==============================================================*/
create table tbl_cmp_os_instances (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   id                   INT4                 null,
   internal_id          INT4                 null,
   user_id              VARCHAR(255)         null,
   project_id           VARCHAR(255)         null,
   image_ref            VARCHAR(255)         null,
   kernel_id            VARCHAR(255)         null,
   ramdisk_id           VARCHAR(255)         null,
   launch_index         INT4                 null,
   key_name             VARCHAR(255)         null,
   key_data             TEXT                 null,
   power_state          INT4                 null,
   vm_state             VARCHAR(255)         null,
   memory_mb            INT4                 null,
   vcpus                INT4                 null,
   hostname             VARCHAR(255)         null,
   host                 VARCHAR(255)         null,
   user_data            TEXT                 null,
   reservation_id       VARCHAR(255)         null,
   launched_at          TIMESTAMP WITH TIME ZONE null,
   terminated_at        TIMESTAMP WITH TIME ZONE null,
   display_name         VARCHAR(255)         null,
   display_description  VARCHAR(255)         null,
   availability_zone    VARCHAR(255)         null,
   locked               INT2                 null,
   os_type              VARCHAR(255)         null,
   launched_on          TEXT                 null,
   instance_type_id     VARCHAR(255)         null,
   vm_mode              VARCHAR(255)         null,
   uuid                 VARCHAR(36)          not null,
   architecture         VARCHAR(255)         null,
   root_device_name     VARCHAR(255)         null,
   access_ip_v4         VARCHAR(39)          null,
   access_ip_v6         VARCHAR(39)          null,
   config_drive         VARCHAR(255)         null,
   task_state           VARCHAR(255)         null,
   default_ephemeral_device VARCHAR(255)         null,
   default_swap_device  VARCHAR(255)         null,
   progress             INT4                 null,
   auto_disk_config     INT2                 null,
   shutdown_terminate   INT2                 null,
   disable_terminate    INT2                 null,
   root_gb              INT4                 null,
   ephemeral_gb         INT4                 null,
   cell_name            VARCHAR(255)         null,
   node                 VARCHAR(255)         null,
   deleted              INT4                 null,
   locked_by            VARCHAR(20)          null,
   cleand               INT4                 null,
   ephemeral_key_uuid   VARCHAR(36)          null,
   hidden               INT2                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   status               VARCHAR(32)          null,
   network_info         TEXT                 null,
   charge_type          INT4                 null,
   price_unit           INT4                 null,
   period               INT4                 null,
   expire_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_instance_uuid primary key (cloud_id, uuid)
);

/*==============================================================*/
/* Table: tbl_cmp_os_ipallocationpools                          */
/*==============================================================*/
create table tbl_cmp_os_ipallocationpools (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(36)          null,
   subnet_id            VARCHAR(36)          not null,
   first_ip             VARCHAR(64)          not null,
   last_ip              VARCHAR(64)          not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_ipallocationpools_subnet_ip primary key (cloud_id, subnet_id, first_ip, last_ip)
);

/*==============================================================*/
/* Table: tbl_cmp_os_ipallocations                              */
/*==============================================================*/
create table tbl_cmp_os_ipallocations (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(36)          null,
   ip_address           VARCHAR(64)          not null,
   subnet_id            VARCHAR(36)          not null,
   network_id           VARCHAR(36)          not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_ipallocations_ip_subnet_network primary key (cloud_id, ip_address, subnet_id, network_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_key_pairs                                  */
/*==============================================================*/
create table tbl_cmp_os_key_pairs (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   id                   INT4                 not null,
   name                 VARCHAR(255)         null,
   user_id              VARCHAR(255)         null,
   fingerprint          VARCHAR(255)         null,
   public_key           TEXT                 null,
   type                 VARCHAR(10)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   ee_name              VARCHAR(255)         null,
   constraint PK_keypairs_id primary key (cloud_id, id),
   constraint AK_key_pairs_userid_name unique (cloud_id, user_id, name)
);

/*==============================================================*/
/* Table: tbl_cmp_os_ml2_distributed_port_bindings              */
/*==============================================================*/
create table tbl_cmp_os_ml2_distributed_port_bindings (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(36)          not null,
   host                 VARCHAR(255)         not null,
   router_id            VARCHAR(36)          null,
   vif_type             VARCHAR(64)          null,
   vif_details          VARCHAR(4095)        null,
   vnic_type            VARCHAR(64)          null,
   profile              VARCHAR(64)          null,
   status               VARCHAR(16)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_ml2distributedportbinding_port_host primary key (cloud_id, port_id, host)
);

/*==============================================================*/
/* Table: tbl_cmp_os_ml2_port_binding_levels                    */
/*==============================================================*/
create table tbl_cmp_os_ml2_port_binding_levels (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(36)          not null,
   host                 VARCHAR(255)         not null,
   level                INT4                 not null,
   driver               VARCHAR(64)          null,
   segment_id           VARCHAR(36)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_ml2portbindinglevels_port_host_level primary key (cloud_id, port_id, host, level)
);

/*==============================================================*/
/* Table: tbl_cmp_os_ml2_port_bindings                          */
/*==============================================================*/
create table tbl_cmp_os_ml2_port_bindings (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(36)          not null,
   host                 VARCHAR(255)         not null,
   vif_type             VARCHAR(64)          null,
   vnic_type            VARCHAR(64)          null,
   profile              VARCHAR(4095)        null,
   vif_details          VARCHAR(4095)        null,
   status               VARCHAR(16)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_ml2portbindings_port_host primary key (cloud_id, port_id, host)
);

/*==============================================================*/
/* Table: tbl_cmp_os_networks                                   */
/*==============================================================*/
create table tbl_cmp_os_networks (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   status               VARCHAR(16)          null,
   admin_state_up       INT2                 null,
   vlan_transparent     INT2                 null,
   standard_attr_id     INT8                 null,
   availability_zone_hints VARCHAR(64)          null,
   mtu                  INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   shared               INT2                 null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   is_default           INT2                 null,
   dns_domain           VARCHAR(255)         null,
   port_security_enabled INT2                 null,
   policy_id            CHAR(10)             null,
   constraint PK_network_id primary key (cloud_id, id),
   constraint AK_network_standard_attr_id unique (cloud_id, standard_attr_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_networksegments                            */
/*==============================================================*/
create table tbl_cmp_os_networksegments (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(36)          not null,
   network_id           VARCHAR(36)          null,
   network_type         VARCHAR(32)          null,
   physical_network     VARCHAR(64)          null,
   segmentation_id      INT4                 null,
   is_dynamic           INT2                 null,
   segment_index        INT4                 null,
   standard_attr_id     INT8                 null,
   name                 VARCHAR(64)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   constraint PK_networksegments_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_portforwardings                            */
/*==============================================================*/
create table tbl_cmp_os_portforwardings (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(36)          not null,
   floatingip_id        VARCHAR(36)          null,
   external_port        INT4                 null,
   internal_neutron_port_id VARCHAR(36)          null,
   protocol             VARCHAR(40)          null,
   socket               VARCHAR(36)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_portforwardings_id primary key (cloud_id, id),
   constraint AK_portforwarding_floatingip_externalport_protocol unique (cloud_id, floatingip_id, external_port, protocol),
   constraint AK_portforwarding_internal_socket_protocol unique (cloud_id, internal_neutron_port_id, socket, protocol)
);

/*==============================================================*/
/* Table: tbl_cmp_os_ports                                      */
/*==============================================================*/
create table tbl_cmp_os_ports (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   network_id           VARCHAR(36)          null,
   mac_address          VARCHAR(32)          null,
   admin_state_up       INT2                 null,
   status               VARCHAR(16)          null,
   device_id            VARCHAR(255)         null,
   device_owner         VARCHAR(255)         null,
   standard_attr_id     INT8                 null,
   ip_allocation        VARCHAR(16)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   data_plane_status    VARCHAR(16)          null,
   current_dns_name     VARCHAR(255)         null,
   current_dns_domain   VARCHAR(255)         null,
   previous_dns_name    VARCHAR(255)         null,
   previous_dns_domain  VARCHAR(255)         null,
   dns_name             VARCHAR(255)         null,
   dns_domain           VARCHAR(255)         null,
   policy_id            VARCHAR(36)          null,
   propagate_uplink_status INT2                 null,
   mac_learning_enabled INT2                 null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   port_security_enabled INT2                 null,
   constraint PK_ports_id primary key (cloud_id, id),
   constraint AK_ports_networkid_macaddress unique (cloud_id, network_id, mac_address),
   constraint AK_ports_standardattrid unique (cloud_id, standard_attr_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_project                                    */
/*==============================================================*/
create table tbl_cmp_os_project (
   cloud_id             VARCHAR(64)          not null,
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   extra                TEXT                 null,
   description          TEXT                 null,
   enabled              INT2                 null,
   domain_id            VARCHAR(64)          null,
   parent_id            VARCHAR(64)          null,
   is_domain            INT2                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_project_id primary key (cloud_id, id),
   constraint AK_project_domain_name unique (cloud_id, domain_id, name)
);

/*==============================================================*/
/* Table: tbl_cmp_os_routerports                                */
/*==============================================================*/
create table tbl_cmp_os_routerports (
   cloud_id             VARCHAR(64)          not null,
   router_id            VARCHAR(36)          not null,
   port_id              VARCHAR(36)          not null,
   port_type            VARCHAR(255)         null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_routerports_id primary key (cloud_id, router_id, port_id),
   constraint AK_routerports_port_id unique (cloud_id, port_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_routerroutes                               */
/*==============================================================*/
create table tbl_cmp_os_routerroutes (
   cloud_id             VARCHAR(64)          not null,
   destination          VARCHAR(64)          not null,
   nexthop              VARCHAR(64)          not null,
   router_id            VARCHAR(36)          not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_routerroutes primary key (cloud_id, destination, nexthop, router_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_routers                                    */
/*==============================================================*/
create table tbl_cmp_os_routers (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   status               VARCHAR(16)          null,
   admin_state_up       INT2                 null,
   gw_port_id           VARCHAR(36)          null,
   enable_snat          INT2                 null,
   standard_attr_id     INT8                 null,
   flavor_id            VARCHAR(36)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   distributed          INT2                 null,
   service_router       INT2                 null,
   ha                   INT2                 null,
   ha_vr_id             INT4                 null,
   availability_zone_hints VARCHAR(255)         null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   constraint PK_routers_id primary key (cloud_id, id),
   constraint AK_routers_standard_attr_id unique (cloud_id, standard_attr_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_securitygroupportbindings                  */
/*==============================================================*/
create table tbl_cmp_os_securitygroupportbindings (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(36)          not null,
   security_group_id    VARCHAR(36)          not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_securitygroupportbindings_port_security primary key (cloud_id, port_id, security_group_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_securitygrouprules                         */
/*==============================================================*/
create table tbl_cmp_os_securitygrouprules (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   security_group_id    VARCHAR(36)          null,
   remote_group_id      VARCHAR(36)          null,
   direction            VARCHAR(20)          null,
   ethertype            VARCHAR(40)          null,
   protocol             VARCHAR(40)          null,
   port_range_min       INT4                 null,
   port_range_max       INT4                 null,
   remote_ip_prefix     VARCHAR(255)         null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_securitygrouprules_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_securitygroups                             */
/*==============================================================*/
create table tbl_cmp_os_securitygroups (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   shared               INT2                 null,
   ee_name              VARCHAR(255)         null,
   constraint PK_securitygroup_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_snapshot_metadata                          */
/*==============================================================*/
create table tbl_cmp_os_snapshot_metadata (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   INT4                 not null,
   snapshot_id          VARCHAR(36)          null,
   key                  VARCHAR(255)         null,
   value                VARCHAR(255)         null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_snapshot_metadata primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_snapshots                                  */
/*==============================================================*/
create table tbl_cmp_os_snapshots (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   VARCHAR(36)          not null,
   volume_id            VARCHAR(36)          null,
   user_id              VARCHAR(255)         null,
   project_id           VARCHAR(255)         null,
   status               VARCHAR(255)         null,
   progress             VARCHAR(255)         null,
   volume_size          INT4                 null,
   scheduled_at         TIMESTAMP WITH TIME ZONE null,
   display_name         VARCHAR(255)         null,
   display_description  VARCHAR(255)         null,
   provider_location    VARCHAR(255)         null,
   encryption_key_id    VARCHAR(36)          null,
   volume_type_id       VARCHAR(36)          null,
   cgsnapshot_id        VARCHAR(36)          null,
   provider_id          VARCHAR(255)         null,
   provider_auth        VARCHAR(255)         null,
   group_snapshot_id    VARCHAR(36)          null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_snapshot_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_subnet_service_types                       */
/*==============================================================*/
create table tbl_cmp_os_subnet_service_types (
   cloud_id             VARCHAR(64)          not null,
   subnet_id            VARCHAR(36)          not null,
   service_type         VARCHAR(255)         not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_subnetservicetype primary key (cloud_id, subnet_id, service_type)
);

/*==============================================================*/
/* Table: tbl_cmp_os_subnetroutes                               */
/*==============================================================*/
create table tbl_cmp_os_subnetroutes (
   cloud_id             VARCHAR(64)          not null,
   destination          VARCHAR(64)          not null,
   nexthop              VARCHAR(64)          not null,
   subnet_id            VARCHAR(36)          not null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_subnetroutes_destination_nexthop_subnet primary key (cloud_id, destination, nexthop, subnet_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_subnets                                    */
/*==============================================================*/
create table tbl_cmp_os_subnets (
   cloud_id             VARCHAR(64)          not null,
   project_id           VARCHAR(255)         null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   network_id           VARCHAR(36)          null,
   ip_version           INT4                 null,
   cidr                 VARCHAR(64)          null,
   gateway_ip           VARCHAR(64)          null,
   enable_dhcp          INT2                 null,
   ipv6_ra_mode         VARCHAR(32)          null,
   ipv6_address_mode    VARCHAR(32)          null,
   subnetpool_id        VARCHAR(36)          null,
   standard_attr_id     INT8                 null,
   segment_id           VARCHAR(36)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   description          VARCHAR(255)         null,
   revision_number      INT8                 null,
   constraint PK_subnet_id primary key (cloud_id, id),
   constraint AK_subnet_standard_attr_id unique (cloud_id, standard_attr_id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_volume_attachment                          */
/*==============================================================*/
create table tbl_cmp_os_volume_attachment (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   VARCHAR(36)          not null,
   volume_id            VARCHAR(36)          null,
   attached_host        VARCHAR(255)         null,
   instance_uuid        VARCHAR(36)          null,
   mountpoint           VARCHAR(255)         null,
   attach_time          TIMESTAMP WITH TIME ZONE null,
   detach_time          TIMESTAMP WITH TIME ZONE null,
   attach_mode          VARCHAR(36)          null,
   attach_status        VARCHAR(255)         null,
   connection_info      TEXT                 null,
   connector            TEXT                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_volume_attachment_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_volume_metadata                            */
/*==============================================================*/
create table tbl_cmp_os_volume_metadata (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   INT4                 not null,
   volume_id            VARCHAR(36)          null,
   key                  VARCHAR(255)         null,
   value                VARCHAR(255)         null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_volume_metadata_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_volume_types                               */
/*==============================================================*/
create table tbl_cmp_os_volume_types (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   VARCHAR(36)          not null,
   name                 VARCHAR(255)         null,
   qos_specs_id         VARCHAR(36)          null,
   is_public            INT2                 null,
   description          VARCHAR(255)         null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   constraint PK_volume_types_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_os_volumes                                    */
/*==============================================================*/
create table tbl_cmp_os_volumes (
   cloud_id             VARCHAR(64)          not null,
   created_at           TIMESTAMP WITH TIME ZONE null,
   updated_at           TIMESTAMP WITH TIME ZONE null,
   deleted_at           TIMESTAMP WITH TIME ZONE null,
   deleted              INT2                 null,
   id                   VARCHAR(36)          not null,
   ec2_id               VARCHAR(255)         null,
   user_id              VARCHAR(255)         null,
   project_id           VARCHAR(255)         null,
   host                 VARCHAR(255)         null,
   size                 INT4                 null,
   availability_zone    VARCHAR(255)         null,
   status               VARCHAR(255)         null,
   attach_status        VARCHAR(255)         null,
   scheduled_at         TIMESTAMP WITH TIME ZONE null,
   launched_at          TIMESTAMP WITH TIME ZONE null,
   terminated_at        TIMESTAMP WITH TIME ZONE null,
   display_name         VARCHAR(255)         null,
   display_description  VARCHAR(255)         null,
   provider_location    VARCHAR(256)         null,
   provider_auth        VARCHAR(256)         null,
   snapshot_id          VARCHAR(36)          null,
   volume_type_id       VARCHAR(36)          null,
   source_volid         VARCHAR(36)          null,
   bootable             INT2                 null,
   provider_geometry    VARCHAR(255)         null,
   _name_id             VARCHAR(36)          null,
   encryption_key_id    VARCHAR(36)          null,
   migration_status     VARCHAR(255)         null,
   replication_status   VARCHAR(255)         null,
   replication_extended_status VARCHAR(255)         null,
   replication_driver_data VARCHAR(255)         null,
   consistencygroup_id  VARCHAR(36)          null,
   provider_id          VARCHAR(255)         null,
   multiattch           INT2                 null,
   previous_status      VARCHAR(255)         null,
   cluster_name         VARCHAR(255)         null,
   group_id             VARCHAR(36)          null,
   service_uuid         VARCHAR(36)          null,
   shared_targets       INT2                 null,
   ee_status            INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   charge_type          INT4                 null,
   price_unit           INT4                 null,
   period               INT4                 null,
   volume_glance_metadata TEXT                 null,
   expire_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_volumes_id primary key (cloud_id, id)
);

/*==============================================================*/
/* Table: tbl_cmp_pci_device                                    */
/*==============================================================*/
create table tbl_cmp_pci_device (
   cloud_id             VARCHAR(64)          not null,
   device_id            VARCHAR(64)          not null,
   device_group_id      VARCHAR(64)          null,
   type                 VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   user_id              VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   device_id_from_agent VARCHAR(64)          null,
   version              INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   vm_instance_id       VARCHAR(64)          null,
   partition_id         VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   constraint PK_TBL_CMP_PCI_DEVICE primary key (cloud_id, device_id)
);

/*==============================================================*/
/* Table: tbl_cmp_pci_device_group                              */
/*==============================================================*/
create table tbl_cmp_pci_device_group (
   cloud_id             VARCHAR(64)          not null,
   device_group_id      VARCHAR(64)          not null,
   node_id              VARCHAR(64)          null,
   type                 INT4                 null,
   name                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   user_id              VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   device_group_id_from_agent VARCHAR(64)          null,
   vm_instance_id       VARCHAR(64)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_PCI_DEVICE_GROUP primary key (cloud_id, device_group_id)
);

/*==============================================================*/
/* Table: tbl_cmp_port                                          */
/*==============================================================*/
create table tbl_cmp_port (
   cloud_id             VARCHAR(64)          not null,
   port_id              VARCHAR(64)          not null,
   subnet_id            VARCHAR(64)          null,
   port_id_from_agent   VARCHAR(64)          null,
   ip_address           VARCHAR(128)         null,
   mac_address          VARCHAR(20)          null,
   phase_status         INT2                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   type                 INT2                 null,
   instance_id          VARCHAR(64)          null,
   of_port              INT4                 null,
   host_id_from_agent   VARCHAR(64)          null,
   node_port_id         VARCHAR(64)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   eip_id               VARCHAR(64)          null,
   eip_phase_status     INT4                 null,
   constraint PK_TBL_CMP_PORT primary key (cloud_id, port_id)
);

/*==============================================================*/
/* Table: tbl_cmp_port_map                                      */
/*==============================================================*/
create table tbl_cmp_port_map (
   cloud_id             VARCHAR(64)          not null,
   port_map_id          VARCHAR(64)          not null,
   eip_map_id           VARCHAR(64)          null,
   protocol             INT2                 null,
   global_port          INT4                 null,
   local_port           INT4                 null,
   status               INT2                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_PORT_MAP primary key (cloud_id, port_map_id)
);

/*==============================================================*/
/* Table: tbl_cmp_pubkey                                        */
/*==============================================================*/
create table tbl_cmp_pubkey (
   cloud_id             VARCHAR(64)          not null,
   pubkey_id            VARCHAR(64)          not null,
   user_id              VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   pubkey               TEXT                 null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_PUBKEY primary key (cloud_id, pubkey_id)
);

/*==============================================================*/
/* Table: tbl_cmp_resource_stats                                */
/*==============================================================*/
create table tbl_cmp_resource_stats (
   cloud_id             VARCHAR(64)          not null,
   stats_id             VARCHAR(64)          not null,
   user_id              VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   total                INT4                 null,
   used                 INT4                 null,
   running              INT4                 null,
   unit                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_RESOURCE_STATS primary key (cloud_id, stats_id)
);

/*==============================================================*/
/* Table: tbl_cmp_security_group                                */
/*==============================================================*/
create table tbl_cmp_security_group (
   cloud_id             VARCHAR(64)          not null,
   sg_id                VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   description          TEXT                 null,
   user_id              VARCHAR(64)          null,
   sg_id_from_agent     VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   is_default           BOOL                 null,
   ee_id                VARCHAR(64)          null,
   constraint PK_TBL_CMP_SECURITY_GROUP primary key (cloud_id, sg_id)
);

/*==============================================================*/
/* Table: tbl_cmp_security_group_rule                           */
/*==============================================================*/
create table tbl_cmp_security_group_rule (
   cloud_id             VARCHAR(64)          not null,
   rule_id              VARCHAR(64)          not null,
   sg_id                VARCHAR(64)          null,
   phase_status         INT4                 null,
   priority             INT2                 null,
   direction            INT2                 null,
   protocol             INT2                 null,
   port                 VARCHAR(128)         null,
   cidr                 VARCHAR(40)          null,
   sg_id_reference      VARCHAR(64)          null,
   pool_id              VARCHAR(64)          null,
   description          TEXT                 null,
   address_type         INT2                 null,
   action               INT2                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_SECURITY_GROUP_RULE primary key (cloud_id, rule_id)
);

/*==============================================================*/
/* Table: tbl_cmp_sg_vm_instance                                */
/*==============================================================*/
create table tbl_cmp_sg_vm_instance (
   cloud_id             VARCHAR(64)          not null,
   sg_vm_id             VARCHAR(64)          not null,
   sg_id                VARCHAR(64)          null,
   instance_id          VARCHAR(64)          null,
   phase_status         INT4                 null,
   sg_id_from_agent     VARCHAR(64)          null,
   vpc_id_from_agent    VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_SG_VM_INSTANCE primary key (cloud_id, sg_vm_id)
);

/*==============================================================*/
/* Table: tbl_cmp_share                                         */
/*==============================================================*/
create table tbl_cmp_share (
   cloud_id             VARCHAR(64)          not null,
   share_id             VARCHAR(64)          not null,
   share_id_from_agent  VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   baremetal_id         VARCHAR(64)          null,
   image_id             VARCHAR(64)          null,
   iscsi_initiator      VARCHAR(80)          null,
   iscsi_target         VARCHAR(96)          null,
   iscsi_ipport         VARCHAR(64)          null,
   phase_status         INT2                 null,
   phase_info           VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_SHARE primary key (cloud_id, share_id)
);

/*==============================================================*/
/* Table: tbl_cmp_storage_pool                                  */
/*==============================================================*/
create table tbl_cmp_storage_pool (
   cloud_id             VARCHAR(64)          not null,
   storage_pool_id      VARCHAR(64)          not null,
   storage_pool_id_from_agent VARCHAR(64)          null,
   type                 INT4                 null,
   sid                  VARCHAR(64)          null,
   paras                VARCHAR(128)         null,
   phase_status         INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   name                 VARCHAR(64)          null,
   description          TEXT                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   constraint PK_TBL_CMP_STORAGE_POOL primary key (cloud_id, storage_pool_id)
);

/*==============================================================*/
/* Table: tbl_cmp_subnet                                        */
/*==============================================================*/
create table tbl_cmp_subnet (
   cloud_id             VARCHAR(64)          not null,
   subnet_id            VARCHAR(64)          not null,
   subnet_id_from_agent VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   vpc_id               VARCHAR(64)          null,
   phase_status         INT2                 null,
   phase_info           VARCHAR(64)          null,
   address_type         INT2                 null,
   subnet_cidr          VARCHAR(40)          null,
   gateway_ip           VARCHAR(40)          null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   description          TEXT                 null,
   ee_id                VARCHAR(64)          null,
   constraint PK_TBL_CMP_SUBNET primary key (cloud_id, subnet_id)
);

/*==============================================================*/
/* Table: tbl_cmp_vm_instance                                   */
/*==============================================================*/
create table tbl_cmp_vm_instance (
   cloud_id             VARCHAR(64)          not null,
   vm_instance_id       VARCHAR(64)          not null,
   instance_id_from_agent VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   phase_status         INT4                 null,
   user_id              VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   flavor_id            VARCHAR(64)          null,
   image_id             VARCHAR(64)          null,
   vpc_id               VARCHAR(64)          null,
   subnet_id            VARCHAR(64)          null,
   port_id              VARCHAR(64)          null,
   static_ip            VARCHAR(40)          null,
   host_name            VARCHAR(128)         null,
   sys_username         VARCHAR(64)          null,
   sys_password         VARCHAR(128)         null,
   pubkey_id            VARCHAR(64)          null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   last_node_id         VARCHAR(64)          null,
   dest_node_id         VARCHAR(64)          null,
   volume_id            VARCHAR(64)          null,
   storage_pool_id      VARCHAR(64)          null,
   instance_group_id    VARCHAR(64)          null,
   boot_dev             VARCHAR(64)          null,
   os_type              VARCHAR(64)          null,
   cmp_tenant_id        VARCHAR(64)          null,
   cmp_user_id          VARCHAR(64)          null,
   cpu_count            INT4                 null,
   mem_size             INT4                 null,
   eip_id               VARCHAR(64)          null,
   root_disk            INT4                 null,
   recycle_mem_size     INT4                 null,
   recycle_cpu_count    INT4                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   charge_type          INT4                 null,
   price_unit           INT4                 null,
   period               INT4                 null,
   expire_time          TIMESTAMP WITH TIME ZONE null,
   ee_id                VARCHAR(64)          null,
   constraint PK_TBL_CMP_VM_INSTANCE primary key (cloud_id, vm_instance_id)
);

/*==============================================================*/
/* Table: tbl_cmp_vm_snap                                       */
/*==============================================================*/
create table tbl_cmp_vm_snap (
   cloud_id             VARCHAR(64)          not null,
   snap_id              VARCHAR(64)          not null,
   vm_instance_id       VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   snap_id_from_agent   VARCHAR(64)          null,
   phase_status         INT4                 null,
   description          text                 null,
   is_current           BOOL                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   parent_id            VARCHAR(64)          null,
   constraint PK_TBL_CMP_VM_SNAP primary key (cloud_id, snap_id)
);

/*==============================================================*/
/* Table: tbl_cmp_volume                                        */
/*==============================================================*/
create table tbl_cmp_volume (
   cloud_id             VARCHAR(64)          not null,
   volume_id            VARCHAR(64)          not null,
   storage_pool_id      VARCHAR(64)          null,
   volume_id_from_agent VARCHAR(64)          null,
   image_id             VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   user_id              VARCHAR(64)          null,
   phase_status         INT4                 null,
   type                 INT4                 null,
   description          TEXT                 null,
   size                 INT4                 null,
   export_name          VARCHAR(255)         null,
   vm_id                VARCHAR(64)          null,
   node_ip              VARCHAR(64)          null,
   last_ip              VARCHAR(64)          null,
   dest_ip              VARCHAR(64)          null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   charge_type          INT4                 null,
   price_unit           INT4                 null,
   period               INT4                 null,
   expire_time          TIMESTAMP WITH TIME ZONE null,
   source               INT4                 null,
   ee_id                VARCHAR(64)          null,
   constraint PK_TBL_CMP_VOLUME primary key (cloud_id, volume_id)
);

/*==============================================================*/
/* Table: tbl_cmp_volume_snap                                   */
/*==============================================================*/
create table tbl_cmp_volume_snap (
   cloud_id             VARCHAR(64)          not null,
   volume_snap_id       VARCHAR(64)          not null,
   volume_snap_id_from_agent VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   volume_id            VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   phase_status         INT4                 null,
   description          TEXT                 null,
   name                 VARCHAR(64)          null,
   is_current           BOOL                 null,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   parent_id            VARCHAR(64)          null,
   constraint PK_TBL_CMP_VOLUME_SNAP primary key (cloud_id, volume_snap_id)
);

/*==============================================================*/
/* Table: tbl_cmp_vpc                                           */
/*==============================================================*/
create table tbl_cmp_vpc (
   cloud_id             VARCHAR(64)          not null,
   vpc_id               VARCHAR(64)          not null,
   vpc_id_from_agent    VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   vlan_id              int4                 null,
   phase_status         INT2                 null,
   phase_info           VARCHAR(64)          null,
   address_type         INT2                 null,
   vpc_cidr             VARCHAR(40)          null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   ee_bp                VARCHAR(64)          null,
   ee_user              VARCHAR(64)          null,
   ee_status            INT4                 null,
   description          TEXT                 null,
   ee_id                VARCHAR(64)          null,
   constraint PK_TBL_CMP_VPC primary key (cloud_id, vpc_id)
);

alter table tbl_cloud_agent_info
   add constraint FK_TBL_CLOU_REFERENCE_TBL_CLOU foreign key (cloud_id)
      references tbl_cloud_info (cloud_id)
      on delete restrict on update restrict;

