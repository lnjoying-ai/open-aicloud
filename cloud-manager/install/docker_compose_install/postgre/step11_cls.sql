/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2022/8/19 20:04:01                           */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_cluster_agent_info                                */
/*==============================================================*/
create table tbl_cluster_agent_info (
   agent_id             VARCHAR(64)          not null,
   cluster_id           VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null,
   online_status        INT4                 null,
   core_version         VARCHAR(64)          null,
   constraint PK_TBL_CLUSTER_AGENT_INFO primary key (agent_id)
);

/*==============================================================*/
/* Index: cls_a_cls_id                                          */
/*==============================================================*/
create unique index cls_a_cls_id on tbl_cluster_agent_info (
cluster_id
);

/*==============================================================*/
/* Index: cls_a_agent_id_index                                  */
/*==============================================================*/
create unique index cls_a_agent_id_index on tbl_cluster_agent_info (
agent_id
);

/*==============================================================*/
/* Table: tbl_cluster_cert_info                                 */
/*==============================================================*/
create table tbl_cluster_cert_info (
   cluster_id           VARCHAR(64)          not null,
   cert                 TEXT                 not null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null,
   constraint PK_TBL_CLUSTER_CERT_INFO primary key (cluster_id)
);

/*==============================================================*/
/* Table: tbl_cluster_extmpl_info                               */
/*==============================================================*/
create table tbl_cluster_extmpl_info (
   cluster_id           VARCHAR(64)          null,
   template_name        VARCHAR(64)          not null,
   template_id          VARCHAR(64)          null,
   versions             VARCHAR(64)          null,
   description          TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null
);

/*==============================================================*/
/* Table: tbl_cluster_info                                      */
/*==============================================================*/
create table tbl_cluster_info (
   id                   VARCHAR(64)          not null,
   tmpl_version_id      VARCHAR(64)          null,
   name                 VARCHAR(64)          not null,
   description          VARCHAR(256)         null,
   cluster_engine_config TEXT                 null,
   dev_need             TEXT                 null,
   target_nodes         TEXT                 null,
   members              TEXT                 null,
   create_type          VARCHAR(20)          null,
   cluster_type         VARCHAR(20)          not null,
   labels               TEXT                 null,
   annotations          TEXT                 null,
   owner                VARCHAR(64)          not null,
   bp                   VARCHAR(64)          null,
   token                VARCHAR(64)          not null,
   creator              VARCHAR(64)          not null,
   status               INT4                 not null,
   service_state        INT4                 not null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null,
   constraint PK_TBL_CLUSTER_INFO primary key (id)
);

/*==============================================================*/
/* Index: cls_id_index                                          */
/*==============================================================*/
create unique index cls_id_index on tbl_cluster_info (
id
);

/*==============================================================*/
/* Index: cls_owner_name                                        */
/*==============================================================*/
create unique index cls_owner_name on tbl_cluster_info (
name,
owner
);

/*==============================================================*/
/* Index: cls_token                                             */
/*==============================================================*/
create unique index cls_token on tbl_cluster_info (
token
);

/*==============================================================*/
/* Index: cls_bp_name                                           */
/*==============================================================*/
create unique index cls_bp_name on tbl_cluster_info (
name,
bp
);

/*==============================================================*/
/* Table: tbl_cluster_node_info                                 */
/*==============================================================*/
create table tbl_cluster_node_info (
   cluster_id           VARCHAR(64)          null,
   region_id            VARCHAR(64)          not null,
   site_id              VARCHAR(64)          not null,
   node_id              VARCHAR(64)          not null,
   node_name            VARCHAR(64)          null,
   cluster_roles        VARCHAR(64)          not null,
   internal_address     VARCHAR(64)          not null,
   external_address     VARCHAR(64)          not null,
   docker_info          TEXT                 null,
   status               INT4                 not null,
   labels               TEXT                 null,
   taints               TEXT                 null,
   annotations          TEXT                 null,
   deploy_plan          TEXT                 null,
   undeploy_plan        TEXT                 null,
   monitor_plan         TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null,
   constraint AK_KEY_1_TBL_CLUS unique (cluster_id, node_id)
);

/*==============================================================*/
/* Index: cls_n_node_id                                         */
/*==============================================================*/
create unique index cls_n_node_id on tbl_cluster_node_info (
cluster_id,
node_id
);

/*==============================================================*/
/* Table: tbl_cluster_sa_info                                   */
/*==============================================================*/
create table tbl_cluster_sa_info (
   cluster_id           VARCHAR(64)          not null,
   secret_name          VARCHAR(64)          null,
   secret_token         TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_CLUSTER_SA_INFO primary key (cluster_id)
);

/*==============================================================*/
/* Table: tbl_cluster_template_info                             */
/*==============================================================*/
create table tbl_cluster_template_info (
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(64)          not null,
   description          VARCHAR(256)         null,
   cluster_id           VARCHAR(64)          null,
   members              TEXT                 null,
   type                 INT4                 not null,
   tags                 TEXT                 null,
   owner                VARCHAR(64)          not null,
   bp                   VARCHAR(64)          null,
   creator              VARCHAR(64)          not null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null,
   cluster_type         VARCHAR(20)          not null,
   constraint PK_TBL_CLUSTER_TEMPLATE_INFO primary key (id)
);

/*==============================================================*/
/* Table: tbl_cluster_tmpl_ver_info                             */
/*==============================================================*/
create table tbl_cluster_tmpl_ver_info (
   version_id           VARCHAR(64)          not null,
   version              VARCHAR(64)          null,
   template_id          VARCHAR(64)          null,
   description          TEXT                 null,
   cluster_engine_config TEXT                 not null,
   tags                 TEXT                 null,
   enable               BOOL                 not null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE not null,
   constraint PK_TBL_CLUSTER_TMPL_VER_INFO primary key (version_id)
);

alter table tbl_cluster_agent_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (cluster_id)
      references tbl_cluster_info (id)
      on delete restrict on update restrict;

alter table tbl_cluster_cert_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (cluster_id)
      references tbl_cluster_info (id)
      on delete restrict on update restrict;

alter table tbl_cluster_extmpl_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (cluster_id)
      references tbl_cluster_info (id)
      on delete restrict on update restrict;

alter table tbl_cluster_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (tmpl_version_id)
      references tbl_cluster_tmpl_ver_info (version_id)
      on delete restrict on update restrict;

alter table tbl_cluster_node_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (cluster_id)
      references tbl_cluster_info (id)
      on delete restrict on update restrict;

alter table tbl_cluster_sa_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (cluster_id)
      references tbl_cluster_info (id)
      on delete restrict on update restrict;

alter table tbl_cluster_tmpl_ver_info
   add constraint FK_TBL_CLUS_REFERENCE_TBL_CLUS foreign key (template_id)
      references tbl_cluster_template_info (id)
      on delete restrict on update restrict;

