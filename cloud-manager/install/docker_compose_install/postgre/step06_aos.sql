/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2022/1/17 14:24:32                           */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_cfgdata_stack_info                                */
/*==============================================================*/
create table tbl_cfgdata_stack_info (
   cfg_volume_id        VARCHAR(64)          not null,
   data_id              VARCHAR(255)         null,
   data_group           VARCHAR(255)         null,
   data_hash            VARCHAR(255)         null,
   user_id              VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   stack_id             VARCHAR(64)          null,
   sync_state           INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_CFGDATA_STACK_INFO primary key (cfg_volume_id)
);

/*==============================================================*/
/* Table: tbl_stack_info                                        */
/*==============================================================*/
create table tbl_stack_info (
   stack_id             VARCHAR(64)          not null,
   spec_id              VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   bp_id                VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   create_user_id       VARCHAR(64)          null,
   status               INT4                 null,
   dst_node             JSONB                null,
   labels               TEXT                 null,
   dev_need_info        TEXT                 null,
   auto_run             BOOL                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   send_create_num      INT4                 null,
   start_num            INT4                 null,
   fail_num             INT4                 null,
   event_type           INT4                 not null default 0,
   report_time          TIMESTAMP WITH TIME ZONE null,
   always_online        BOOL                 null default false,
   stack_type           VARCHAR(20)          not null default 'deployment',
   use_service_penetration BOOL                 not null default false,
   expose_ports         JSONB                null,
   constraint PK_TBL_STACK_INFO primary key (stack_id)
);

/*==============================================================*/
/* Index: stack_index                                           */
/*==============================================================*/
create unique index stack_index on tbl_stack_info (
stack_id
);

/*==============================================================*/
/* Table: tbl_stack_inst_info                                   */
/*==============================================================*/
create table tbl_stack_inst_info (
   inst_id              VARCHAR(64)          not null,
   inst_name            TEXT                 null,
   service_id           VARCHAR(64)          null,
   ref_id               VARCHAR(64)          null,
   status               INT4                 null,
   user_id              VARCHAR(64)          null,
   image_id             VARCHAR(100)         null,
   image_name           TEXT                 null,
   node_id              VARCHAR(64)          null,
   exten_info           TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_STACK_INST_INFO primary key (inst_id)
);

/*==============================================================*/
/* Index: stack_inst_index                                      */
/*==============================================================*/
create unique index stack_inst_index on tbl_stack_inst_info (
inst_id
);

/*==============================================================*/
/* Table: tbl_stack_service_info                                */
/*==============================================================*/
create table tbl_stack_service_info (
   service_id           VARCHAR(64)          not null,
   stack_id             VARCHAR(64)          null,
   name                 VARCHAR(64)          null,
   stack_name           VARCHAR(64)          null,
   description          TEXT                 null,
   dst_node             JSONB                null,
   auto_run             BOOL                 null,
   status               INT4                 null,
   user_id              VARCHAR(64)          null,
   image_name           TEXT                 null,
   image_id             VARCHAR(100)         null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_STACK_SERVICE_INFO primary key (service_id),
   constraint AK_AOS_SERVICE_NAME_K_TBL_STAC unique (stack_id, name)
);

/*==============================================================*/
/* Index: stack_service_index                                   */
/*==============================================================*/
create unique index stack_service_index on tbl_stack_service_info (
service_id
);

/*==============================================================*/
/* Table: tbl_stack_spec_info                                   */
/*==============================================================*/
create table tbl_stack_spec_info (
   spec_id              VARCHAR(64)          not null,
   spec_name            VARCHAR(64)          null,
   description          TEXT                 null,
   template_id          VARCHAR(64)          null,
   template_name        VARCHAR(64)          null,
   template_version     VARCHAR(64)          null,
   bp_id                VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   create_user_id       VARCHAR(64)          null,
   status               INT4                 null,
   target_nodes         TEXT                 null,
   dst_nodes            JSONB                null,
   labels               TEXT                 null,
   stack_compose        TEXT                 null,
   justice_compose      TEXT                 null,
   replica_num          INT4                 null,
   aos_type             VARCHAR(20)          null,
   content_type         VARCHAR(20)          null,
   dev_need_info        TEXT                 null,
   auto_run             BOOL                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   registry_id          VARCHAR(64)          null,
   scheduling_strategy  TEXT                 null,
   extra_params         JSONB                null,
   image_names          JSONB                null,
   always_online        BOOL                 not null default false,
   failover_policy      TEXT                 null,
   stack_type           VARCHAR(20)          not null default 'deployment',
   use_service_penetration BOOL                 not null default false,
   deploy_strategy      INT4                 not null default 0,
   cfgs                 TEXT                 null,
   expose_ports         JSONB                null,
   constraint PK_TBL_STACK_SPEC_INFO primary key (spec_id),
   constraint AK_AOS_STACK_SPEC_NAM_TBL_STAC unique (spec_name, user_id)
);

/*==============================================================*/
/* Index: stack_index2                                          */
/*==============================================================*/
create unique index stack_index2 on tbl_stack_spec_info (
spec_id
);

/*==============================================================*/
/* Table: tbl_stack_template_base_info                          */
/*==============================================================*/
create table tbl_stack_template_base_info (
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(64)          not null,
   user_id              VARCHAR(64)          not null,
   bp_id                VARCHAR(64)          null,
   logo_url             VARCHAR(256)         null,
   vendor               VARCHAR(64)          null,
   description          TEXT                 null,
   labels               TEXT                 null,
   status               INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_STACK_TEMPLATE_BASE_INF primary key (id),
   constraint name unique (name, user_id, bp_id)
);

/*==============================================================*/
/* Index: template_index2                                       */
/*==============================================================*/
create unique index template_index2 on tbl_stack_template_base_info (
id
);

/*==============================================================*/
/* Table: tbl_stack_template_info                               */
/*==============================================================*/
create table tbl_stack_template_info (
   template_id          VARCHAR(64)          not null,
   root_id              VARCHAR(64)          not null,
   version              VARCHAR(64)          not null,
   aos_type             VARCHAR(20)          null,
   is_used              BOOL                 null,
   content_type         VARCHAR(20)          null,
   stack_compose        TEXT                 null,
   justice_compose      TEXT                 null,
   show_inputs          TEXT                 null,
   status               INT4                 null,
   description          TEXT                 null,
   labels               TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE not null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_STACK_TEMPLATE_INFO primary key (template_id),
   constraint AK_AOS_TMP_VER_U_KEY_TBL_STAC unique (root_id, version)
);

/*==============================================================*/
/* Index: template_index                                        */
/*==============================================================*/
create unique index template_index on tbl_stack_template_info (
template_id
);

alter table tbl_stack_info
   add constraint FK_TBL_STAC_REFERENCE_TBL_STAC foreign key (spec_id)
      references tbl_stack_spec_info (spec_id)
      on delete restrict on update restrict;

alter table tbl_stack_inst_info
   add constraint FK_TBL_STAC_REFERENCE_TBL_STAC foreign key (service_id)
      references tbl_stack_service_info (service_id)
      on delete restrict on update restrict;

alter table tbl_stack_service_info
   add constraint FK_TBL_STAC_REFERENCE_TBL_STAC foreign key (stack_id)
      references tbl_stack_info (stack_id)
      on delete restrict on update restrict;

alter table tbl_stack_spec_info
   add constraint FK_TBL_STAC_REFERENCE_TBL_STAC foreign key (template_id)
      references tbl_stack_template_info (template_id)
      on delete restrict on update restrict;

alter table tbl_stack_template_info
   add constraint FK_TBL_STAC_REFERENCE_TBL_STAC foreign key (root_id)
      references tbl_stack_template_base_info (id)
      on delete restrict on update restrict;


CREATE TRIGGER stack_event_trigger BEFORE UPDATE OF "status" ON tbl_stack_info
FOR EACH ROW
EXECUTE PROCEDURE "reset_event_on_status_change"();


/*==============================================================*/
/* Table: tbl_helm_stack_info                                   */
/*==============================================================*/
create table tbl_helm_stack_info (
                                     stack_id             VARCHAR(255)         not null,
                                     release_name         VARCHAR(64)          not null,
                                     app_id               VARCHAR(255)         not null,
                                     chart_name           VARCHAR(64)          null,
                                     chart_version        VARCHAR(64)          null,
                                     chart_package_name   VARCHAR(255)         null,
                                     description          VARCHAR(255)         null,
                                     status               INT4                 null,
                                     stack_compose        TEXT                 null,
                                     cluster_id           VARCHAR(64)          null,
                                     cluster_name         VARCHAR(64)          null,
                                     namespace            VARCHAR(64)          null,
                                     labels               JSONB                null,
                                     owner_id             VARCHAR(64)          null,
                                     owner_name           VARCHAR(64)          null,
                                     owner_bp_id          VARCHAR(64)          null,
                                     owner_bp_name        VARCHAR(64)          null,
                                     creator_id           VARCHAR(64)          null,
                                     creator_name         VARCHAR(64)          null,
                                     create_time          TIMESTAMP WITH TIME ZONE not null,
                                     update_time          TIMESTAMP WITH TIME ZONE null,
                                     constraint PK_TBL_HELM_STACK_INFO primary key (stack_id),
                                     constraint AK_KEY_2_TBL_HELM unique (release_name)
);

comment on column tbl_helm_stack_info.stack_id is
    'clsid+deploy name';

comment on column tbl_helm_stack_info.release_name is
    'release name';

comment on column tbl_helm_stack_info.app_id is
    'app id';

comment on column tbl_helm_stack_info.chart_name is
    'chart name';

comment on column tbl_helm_stack_info.chart_version is
    'chart version';

comment on column tbl_helm_stack_info.chart_package_name is
    'chart package name(xxx.tgz)';

comment on column tbl_helm_stack_info.description is
    'description';

comment on column tbl_helm_stack_info.status is
    'status(1:creating;2:runnning;3:stop;4:deploy failed;5:app cleared;6:partial run)';

comment on column tbl_helm_stack_info.stack_compose is
    'chart config';

comment on column tbl_helm_stack_info.cluster_id is
    'cluster id';

comment on column tbl_helm_stack_info.cluster_name is
    'cluster name';

comment on column tbl_helm_stack_info.namespace is
    'namespace';

comment on column tbl_helm_stack_info.labels is
    'labels';

comment on column tbl_helm_stack_info.owner_id is
    'owner id';

comment on column tbl_helm_stack_info.owner_name is
    'owner name';

comment on column tbl_helm_stack_info.owner_bp_id is
    'owner bp id';

comment on column tbl_helm_stack_info.owner_bp_name is
    'owner bp name';

comment on column tbl_helm_stack_info.creator_id is
    'creator id';

comment on column tbl_helm_stack_info.creator_name is
    'creator name';

/*==============================================================*/
/* Table: tbl_repo_app_category_bind_info                       */
/*==============================================================*/
create table tbl_repo_app_category_bind_info (
                                                 category_id          INT4                 not null,
                                                 app_id               VARCHAR(255)         null,
                                                 repo_id              INT4                 null,
                                                 create_time          TIMESTAMP WITH TIME ZONE not null,
                                                 update_time          TIMESTAMP WITH TIME ZONE null,
                                                 constraint AK_KEY_1_TBL_REPO unique (category_id, app_id)
);

comment on column tbl_repo_app_category_bind_info.category_id is
    'category id';

comment on column tbl_repo_app_category_bind_info.app_id is
    'app id';

comment on column tbl_repo_app_category_bind_info.repo_id is
    'repo id';

comment on column tbl_repo_app_category_bind_info.create_time is
    'create time';

comment on column tbl_repo_app_category_bind_info.update_time is
    'update time';

/*==============================================================*/
/* Table: tbl_repo_app_info                                     */
/*==============================================================*/
create table tbl_repo_app_info (
                                   app_id               VARCHAR(255)         not null,
                                   repo_name            VARCHAR(64)          null,
                                   app_name             VARCHAR(64)          null,
                                   versions             JSONB                null,
                                   create_time          TIMESTAMP WITH TIME ZONE not null,
                                   update_time          TIMESTAMP WITH TIME ZONE null,
                                   constraint PK_TBL_REPO_APP_INFO primary key (app_id)
);

comment on column tbl_repo_app_info.app_id is
    'repo_name+”-”+”app_name”';

comment on column tbl_repo_app_info.repo_name is
    'repo name';

comment on column tbl_repo_app_info.app_name is
    'app name';

comment on column tbl_repo_app_info.versions is
    'versions';

comment on column tbl_repo_app_info.create_time is
    'create time';

comment on column tbl_repo_app_info.update_time is
    'update time';

/*==============================================================*/
/* Index: idx_repo_name_app_name                                */
/*==============================================================*/
create  index idx_repo_name_app_name on tbl_repo_app_info (
                                                           repo_name,
                                                           app_name
    );
/*==============================================================*/
/* Table: tbl_repo_app_version_info                             */
/*==============================================================*/
create table tbl_repo_app_version_info (
                                           id                   SERIAL not null,
                                           app_id               VARCHAR(255)         not null,
                                           repo_name            VARCHAR(64)          not null,
                                           app_name             VARCHAR(64)          not null,
                                           version              VARCHAR(64)          not null,
                                           app_info             JSONB                null,
                                           create_time          TIMESTAMP WITH TIME ZONE not null,
                                           update_time          TIMESTAMP WITH TIME ZONE null,
                                           constraint PK_TBL_REPO_APP_VERSION_INFO primary key (id),
                                           constraint AK_KEY_2_TBL_REPO unique (repo_name, app_name, version)
);

comment on column tbl_repo_app_version_info.id is
    'id';

comment on column tbl_repo_app_version_info.app_id is
    'app id';

comment on column tbl_repo_app_version_info.repo_name is
    'repo name';

comment on column tbl_repo_app_version_info.app_name is
    'app name';

comment on column tbl_repo_app_version_info.version is
    'version';

comment on column tbl_repo_app_version_info.app_info is
    'app info';

comment on column tbl_repo_app_version_info.create_time is
    'create time';

comment on column tbl_repo_app_version_info.update_time is
    'update time';

/*==============================================================*/
/* Index: idx_repo_name_app_name_version                        */
/*==============================================================*/
create unique index idx_repo_name_app_name_version on tbl_repo_app_version_info (
                                                                                 repo_name,
                                                                                 app_name,
                                                                                 version
    );

/*==============================================================*/
/* Table: tbl_repo_category_info                                */
/*==============================================================*/
create table tbl_repo_category_info (
                                        category_id          SERIAL not null,
                                        category             VARCHAR(255)         not null,
                                        name                 VARCHAR(64)          not null,
                                        key_word             JSON                 null,
                                        description          VARCHAR(255)         null,
                                        create_time          TIMESTAMP WITH TIME ZONE not null,
                                        update_time          TIMESTAMP WITH TIME ZONE null,
                                        constraint PK_TBL_REPO_CATEGORY_INFO primary key (category_id)
);

comment on column tbl_repo_category_info.category_id is
    'category id';

comment on column tbl_repo_category_info.category is
    'category';

comment on column tbl_repo_category_info.name is
    'category name displayed on the front end';

comment on column tbl_repo_category_info.key_word is
    'As long as the keyword satisfies one of them, it can be considered to be classified as this category';

comment on column tbl_repo_category_info.description is
    'description';

comment on column tbl_repo_category_info.create_time is
    'create time';

comment on column tbl_repo_category_info.update_time is
    'update time';

/*==============================================================*/
/* Table: tbl_repo_info                                         */
/*==============================================================*/
create table tbl_repo_info (
                               id                   SERIAL not null,
                               repo_name            VARCHAR(64)          not null,
                               repo_url             VARCHAR(255)         not null,
                               auth_method          INT4                 null,
                               auth_info            JSONB                null,
                               hash                 VARCHAR(64)          null,
                               status               INT4                 null,
                               access               JSONB                null,
                               labels               JSONB                null,
                               create_time          TIMESTAMP WITH TIME ZONE not null,
                               update_time          TIMESTAMP WITH TIME ZONE null,
                               constraint PK_TBL_REPO_INFO primary key (id),
                               constraint AK_KEY_2_TBL_REPO_INFO unique (repo_name)
);

comment on column tbl_repo_info.id is
    'repo id';

comment on column tbl_repo_info.repo_name is
    'repo name';

comment on column tbl_repo_info.repo_url is
    'repo url';

comment on column tbl_repo_info.auth_method is
    'authentication method(0:not authenticated;1:basic authentication;3:secret authentication)';

comment on column tbl_repo_info.auth_info is
    'auth info';

comment on column tbl_repo_info.hash is
    'index file hash';

comment on column tbl_repo_info.status is
    'status(1: created;2: data sync;3: activation: execute the add operation successfully;4: authentication failed;5: Data download failed)';

comment on column tbl_repo_info.access is
    'access';

comment on column tbl_repo_info.labels is
    'label';

comment on column tbl_repo_info.create_time is
    'create time';

comment on column tbl_repo_info.update_time is
    'update time';


alter table tbl_repo_app_category_bind_info
    add constraint FK_TBL_REPO_REFERENCE_TBL_REPO2 foreign key (category_id)
        references tbl_repo_category_info (category_id)
        on delete restrict on update restrict;

alter table tbl_repo_app_category_bind_info
    add constraint FK_TBL_REPO_REFERENCE_TBL_REPO3 foreign key (repo_id)
        references tbl_repo_info (id)
        on delete restrict on update restrict;

alter table tbl_repo_app_category_bind_info
    add constraint FK_TBL_REPO_REFERENCE_TBL_REPO foreign key (app_id)
        references tbl_repo_app_info (app_id)
        on delete restrict on update restrict;

alter table tbl_repo_app_version_info
    add constraint FK_TBL_REPO_REFERENCE_TBL_REPO foreign key (app_id)
        references tbl_repo_app_info (app_id)
        on delete restrict on update restrict;

