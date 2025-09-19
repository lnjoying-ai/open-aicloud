/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2023/11/6 19:57:53                           */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_edge_compute_gpu_info                             */
/*==============================================================*/
create table tbl_edge_compute_gpu_info (
   gpu_id               VARCHAR(64)          not null,
   gpu_type             VARCHAR(20)          null,
   gpu_model            VARCHAR(64)          null,
   driver_version       VARCHAR(20)          null,
   cuda_version         VARCHAR(20)          null,
   cudnn_version        VARCHAR(20)          null,
   node_id              VARCHAR(64)          null,
   index                INT4                 null,
   status               INT4                 null,
   ref_id               VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_EDGE_COMPUTE_GPU_INFO primary key (gpu_id)
);

/*==============================================================*/
/* Index: gpu_index                                             */
/*==============================================================*/
create unique index gpu_index on tbl_edge_compute_gpu_info (
gpu_id
);

/*==============================================================*/
/* Table: tbl_edge_compute_info                                 */
/*==============================================================*/
create table tbl_edge_compute_info (
   node_id              VARCHAR(64)          not null,
   node_name            VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   region_id            VARCHAR(64)          null,
   host_name            VARCHAR(64)          null,
   network              TEXT                 null,
   active_status        INT4                 null,
   online_status        INT4                 null,
   cpu_limit            INT4                 null,
   mem_limit            INT8                 null,
   disk_limit           INT8                 null,
   vender               VARCHAR(64)          null,
   uuid                 VARCHAR(64)          null,
   product              VARCHAR(64)          null,
   sn                   VARCHAR(64)          null,
   cpu_logic_num        INT4                 null,
   cpu_physical_num     INT4                 null,
   cpu_model            VARCHAR(64)          null,
   cpu_frequency        FLOAT8               null,
   cpu_num              INT4                 null,
   mem_total            INT8                 null,
   disk_total           INT8                 null,
   disk_type            VARCHAR(20)          null,
   disk_detail          TEXT                 null,
   os                   VARCHAR(64)          null,
   core_version         VARCHAR(64)          null,
   software_map         TEXT                 null,
   software_version     TEXT                 null,
   scope                CHAR(10)             null,
   user_id              VARCHAR(64)          null,
   bp_id                VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   constraint PK_TBL_EDGE_COMPUTE_INFO primary key (node_id)
);

/*==============================================================*/
/* Index: edge_compute_index                                    */
/*==============================================================*/
create unique index edge_compute_index on tbl_edge_compute_info (
node_id
);

/*==============================================================*/
/* Table: tbl_edge_compute_label_info                           */
/*==============================================================*/
create table tbl_edge_compute_label_info (
   node_id              VARCHAR(64)          not null,
   label_key            VARCHAR(64)          not null,
   label_value          VARCHAR(64)          null,
   constraint PK_TBL_EDGE_COMPUTE_LABEL_INFO primary key (node_id, label_key)
);

/*==============================================================*/
/* Table: tbl_edge_compute_taint_info                           */
/*==============================================================*/
create table tbl_edge_compute_taint_info (
   node_id              VARCHAR(64)          null,
   taint_key            VARCHAR(64)          null,
   taint_value          VARCHAR(64)          null,
   constraint AK_KEY_1_TBL_EDGE unique (node_id, taint_key)
);

/*==============================================================*/
/* Table: tbl_edge_gw_info                                      */
/*==============================================================*/
create table tbl_edge_gw_info (
   node_id              VARCHAR(64)          not null,
   node_name            VARCHAR(64)          null,
   host_name            VARCHAR(64)          null,
   host_public_ip       VARCHAR(64)          null,
   host_public_port     INT4                 null,
   host_inner_ip        VARCHAR(64)          null,
   host_inner_port      INT4                 null,
   network              TEXT                 null,
   active_status        INT4                 null,
   online_status        INT4                 null,
   cpu_limit            INT4                 null,
   mem_limit            INT8                 null,
   vender               VARCHAR(64)          null,
   uuid                 VARCHAR(64)          null,
   product              VARCHAR(64)          null,
   sn                   VARCHAR(64)          null,
   cpu_logic_num        INT4                 null,
   cpu_physical_num     INT4                 null,
   cpu_model            VARCHAR(64)          null,
   cpu_frequency        FLOAT8               null,
   mem_total            INT8                 null,
   disk_total           INT8                 null,
   disk_type            VARCHAR(20)          null,
   disk_detail          TEXT                 null,
   os                   VARCHAR(64)          null,
   core_version         VARCHAR(64)          null,
   software_version     TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   constraint PK_TBL_EDGE_GW_INFO primary key (node_id)
);

/*==============================================================*/
/* Index: edge_gw_index                                         */
/*==============================================================*/
create unique index edge_gw_index on tbl_edge_gw_info (
node_id
);

/*==============================================================*/
/* Table: tbl_edge_gw_label_info                                */
/*==============================================================*/
create table tbl_edge_gw_label_info (
   node_id              VARCHAR(64)          not null,
   label_value          VARCHAR(64)          null,
   label_key            VARCHAR(64)          not null,
   constraint PK_TBL_EDGE_GW_LABEL_INFO primary key (node_id, label_key)
);

/*==============================================================*/
/* Index: edge_gw_label_index                                   */
/*==============================================================*/
create unique index edge_gw_label_index on tbl_edge_gw_label_info (
node_id
);

/*==============================================================*/
/* Table: tbl_edge_image_info                                   */
/*==============================================================*/
create table tbl_edge_image_info (
   node_id              VARCHAR(64)          not null,
   images               TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_EDGE_IMAGE_INFO primary key (node_id)
);

/*==============================================================*/
/* Index: edge_image_index                                      */
/*==============================================================*/
create unique index edge_image_index on tbl_edge_image_info (
node_id
);

/*==============================================================*/
/* Table: tbl_edge_port_info                                    */
/*==============================================================*/
create table tbl_edge_port_info (
   node_id              VARCHAR(64)          not null,
   ports                TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_EDGE_PORT_INFO primary key (node_id)
);

/*==============================================================*/
/* Index: edge_port                                             */
/*==============================================================*/
create unique index edge_port on tbl_edge_port_info (
node_id
);

/*==============================================================*/
/* Table: tbl_edge_upgrade_info                                 */
/*==============================================================*/
create table tbl_edge_upgrade_info (
   node_id              VARCHAR(64)          not null,
   upgrade_status       INT4                 null,
   old_version          VARCHAR(64)          null,
   new_version          VARCHAR(64)          null,
   upgrade_plan         TEXT                 null,
   oper_user            VARCHAR(64)          null,
   oper_bp              VARCHAR(64)          null,
   region_id            VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_EDGE_UPGRADE_INFO primary key (node_id)
);

/*==============================================================*/
/* Table: tbl_label_option_info                                 */
/*==============================================================*/
create table tbl_label_option_info (
   key                  VARCHAR(256)         null,
   label_type           VARCHAR(32)          null,
   type                 VARCHAR(32)          null,
   label_option         TEXT                 null,
   order_num            INT4                 null
);

/*==============================================================*/
/* Index: ak_labeltype_type_key                                 */
/*==============================================================*/
create unique index ak_labeltype_type_key on tbl_label_option_info (
( label_type ),
( type ),
( key )
);

/*==============================================================*/
/* Table: tbl_region_bind_info                                  */
/*==============================================================*/
create table tbl_region_bind_info (
   region_id            VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   constraint AK_ECRM_REGION_GW_KEY_TBL_REGI unique (region_id, node_id)
);

/*==============================================================*/
/* Index: region_Gw_index                                       */
/*==============================================================*/
create unique index region_Gw_index on tbl_region_bind_info (
region_id,
node_id
);

/*==============================================================*/
/* Table: tbl_region_info                                       */
/*==============================================================*/
create table tbl_region_info (
   region_id            VARCHAR(64)          not null,
   region_name          VARCHAR(64)          null,
   region_desc          VARCHAR(200)         null,
   status               INT4                 not null default 1,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   labels               JSONB                null,
   taints               JSONB                null,
   constraint PK_TBL_REGION_INFO primary key (region_id)
);

/*==============================================================*/
/* Index: region_index                                          */
/*==============================================================*/
create unique index region_index on tbl_region_info (
region_id
);

/*==============================================================*/
/* Table: tbl_service_agent_info                                */
/*==============================================================*/
create table tbl_service_agent_info (
   agent_id             VARCHAR(64)          not null,
   target_nodes         TEXT                 null,
   image                VARCHAR(1024)        null,
   type                 VARCHAR(64)          null,
   description          TEXT                 null,
   online_status        INT4                 null,
   core_version         VARCHAR(64)          null,
   region_id            VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   status               INT4                 null,
   worker_id            VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_SERVICE_AGENT_INFO primary key (agent_id)
);

/*==============================================================*/
/* Index: service_agent_index                                   */
/*==============================================================*/
create  index service_agent_index on tbl_service_agent_info (
site_id
);

/*==============================================================*/
/* Table: tbl_site_info                                         */
/*==============================================================*/
create table tbl_site_info (
   site_id              VARCHAR(64)          not null,
   region_id            VARCHAR(64)          null,
   site_name            VARCHAR(64)          null,
   site_location        TEXT                 null,
   site_desc            VARCHAR(200)         null,
   scope                VARCHAR(10)          null,
   user_id              VARCHAR(64)          null,
   bp_id                VARCHAR(64)          null,
   adcode               VARCHAR(20)          null,
   status               INT4                 not null default 1,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   labels               JSONB                null,
   taints               JSONB                null,
   constraint PK_TBL_SITE_INFO primary key (site_id)
);

/*==============================================================*/
/* Index: site_index                                            */
/*==============================================================*/
create unique index site_index on tbl_site_info (
site_id
);

alter table tbl_edge_compute_gpu_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_compute_info (node_id)
      on delete restrict on update restrict;

alter table tbl_edge_compute_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_REGI foreign key (region_id)
      references tbl_region_info (region_id)
      on delete restrict on update cascade;

alter table tbl_edge_compute_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_SITE foreign key (site_id)
      references tbl_site_info (site_id)
      on delete restrict on update cascade;

alter table tbl_edge_compute_label_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_compute_info (node_id)
      on delete restrict on update restrict;

alter table tbl_edge_compute_taint_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_compute_info (node_id)
      on delete restrict on update restrict;

alter table tbl_edge_gw_label_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_gw_info (node_id)
      on delete restrict on update restrict;

alter table tbl_edge_image_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_compute_info (node_id)
      on delete restrict on update restrict;

alter table tbl_edge_port_info
   add constraint FK_TBL_EDGE_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_compute_info (node_id)
      on delete restrict on update restrict;

alter table tbl_region_bind_info
   add constraint FK_TBL_REGI_REFERENCE_TBL_REGI foreign key (region_id)
      references tbl_region_info (region_id)
      on delete restrict on update restrict;

alter table tbl_region_bind_info
   add constraint FK_TBL_REGI_REFERENCE_TBL_EDGE foreign key (node_id)
      references tbl_edge_gw_info (node_id)
      on delete restrict on update restrict;

alter table tbl_site_info
   add constraint FK_TBL_SITE_REFERENCE_TBL_REGI foreign key (region_id)
      references tbl_region_info (region_id)
      on delete restrict on update cascade;

