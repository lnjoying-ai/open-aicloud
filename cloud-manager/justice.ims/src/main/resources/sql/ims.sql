
/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/10/23 16:36:58                          */
/*==============================================================*/


drop table tbl_ims_registry;

drop table tbl_ims_registry_3rd;

drop table tbl_ims_registry_pre_download;

drop table tbl_ims_registry_project;

drop table tbl_ims_registry_region;

drop table tbl_ims_registry_user;

/*==============================================================*/
/* Table: tbl_ims_registry                                      */
/*==============================================================*/
create table tbl_ims_registry (
                                  registry_id          VARCHAR(64)          not null,
                                  registry_name        VARCHAR(64)          not null,
                                  registry_desc        VARCHAR(255)         null,
                                  type                 INT4                 not null default 0,
                                  url                  VARCHAR(255)         not null,
                                  admin_name           VARCHAR(64)          not null,
                                  admin_passwd         VARCHAR(100)         not null,
                                  user_id              VARCHAR(64)          not null,
                                  status               INT4                 not null,
                                  create_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                  update_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                  constraint PK_TBL_IMS_REGISTRY primary key (registry_id),
                                  constraint registry_name unique (registry_name)
);

comment on table tbl_ims_registry is
    'region image registry table';

comment on column tbl_ims_registry.registry_id is
    'registry id';

comment on column tbl_ims_registry.registry_name is
    'registry name';

comment on column tbl_ims_registry.registry_desc is
    'registry desc';

comment on column tbl_ims_registry.type is
    'registry type(0:harbor;1:docker-hub)';

comment on column tbl_ims_registry.url is
    'registry url';

comment on column tbl_ims_registry.admin_name is
    'administrator account';

comment on column tbl_ims_registry.admin_passwd is
    'administrator password';

comment on column tbl_ims_registry.user_id is
    'user id';

comment on column tbl_ims_registry.status is
    'status(1:creating;2:failed;3:succeed;4:unhealthy;
    5:healthy;6:enable;7:disable;-1:deleted
    )';

comment on column tbl_ims_registry.create_time is
    'create time';

comment on column tbl_ims_registry.update_time is
    'update time';

/*==============================================================*/
/* Table: tbl_ims_registry_3rd                                  */
/*==============================================================*/
create table tbl_ims_registry_3rd (
                                      registry_id          VARCHAR(64)          not null,
                                      registry_name        VARCHAR(64)          not null,
                                      registry_desc        VARCHAR(255)          null,
                                      type                 INT4                 not null default 0,
                                      url                  VARCHAR(255)         not null,
                                      access_key           VARCHAR(255)         not null,
                                      access_secret        VARCHAR(4096)        not null,
                                      insecure             BOOL                 not null default false,
                                      user_id              VARCHAR(64)          not null,
                                      user_name            VARCHAR(64)          null,
                                      bp_id                VARCHAR(64)          null,
                                      bp_name              VARCHAR(64)          null,
                                      status               INT4                 not null default 1,
                                      create_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                      update_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                      constraint PK_TBL_IMS_REGISTRY_3RD primary key (registry_id)
);

comment on table tbl_ims_registry_3rd is
    'region image registry table';

comment on column tbl_ims_registry_3rd.registry_id is
    'registry id';

comment on column tbl_ims_registry_3rd.registry_name is
    'registry name';

comment on column tbl_ims_registry_3rd.registry_desc is
    'registry desc';

comment on column tbl_ims_registry_3rd.type is
    'registry type(0:harbor;1:docker-hub)';

comment on column tbl_ims_registry_3rd.url is
    'registry url';

comment on column tbl_ims_registry_3rd.access_key is
    'Third-party access key';

comment on column tbl_ims_registry_3rd.access_secret is
    'Third-party access password';

comment on column tbl_ims_registry_3rd.insecure is
    'Verify remote certificate';

comment on column tbl_ims_registry_3rd.user_id is
    'user id';

comment on column tbl_ims_registry_3rd.user_name is
    'user name';

comment on column tbl_ims_registry_3rd.bp_id is
    'bp id';

comment on column tbl_ims_registry_3rd.bp_name is
    'bp name';

comment on column tbl_ims_registry_3rd.status is
    'record status(1:normal;-1:deleted)';

comment on column tbl_ims_registry_3rd.create_time is
    'create time';

comment on column tbl_ims_registry_3rd.update_time is
    'update time';

/*==============================================================*/
/* Table: tbl_ims_registry_pre_download                         */
/*==============================================================*/
create table tbl_ims_registry_pre_download (
                                               id                   VARCHAR(64)          not null,
                                               registry_id          VARCHAR(64)          not null,
                                               node_id              VARCHAR(64)          not null,
                                               repos                TEXT                 null,
                                               status               INT4                 not null,
                                               user_id              VARCHAR(64)          not null,
                                               user_name            VARCHAR(64)          null,
                                               bp_id                VARCHAR(64)          null,
                                               bp_name              VARCHAR(64)          null,
                                               create_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                               update_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                               constraint PK_TBL_IMS_REGISTRY_PRE_DOWNLO primary key (id)
);

comment on column tbl_ims_registry_pre_download.id is
    ' id';

comment on column tbl_ims_registry_pre_download.registry_id is
    'registry id';

comment on column tbl_ims_registry_pre_download.node_id is
    'node id';

comment on column tbl_ims_registry_pre_download.repos is
    'repos,separated by comma';

comment on column tbl_ims_registry_pre_download.status is
    'record status(10:creating;11:downloading;0:success;1:auth fail;2:pull failed;3:push failed;4:image not exist;5:server is unreachable;
    )';

comment on column tbl_ims_registry_pre_download.user_id is
    'user id';

comment on column tbl_ims_registry_pre_download.user_name is
    'user name';

comment on column tbl_ims_registry_pre_download.bp_id is
    'bp id';

comment on column tbl_ims_registry_pre_download.bp_name is
    'bp name';

comment on column tbl_ims_registry_pre_download.create_time is
    'create time';

comment on column tbl_ims_registry_pre_download.update_time is
    'update time';

/*==============================================================*/
/* Table: tbl_ims_registry_project                              */
/*==============================================================*/
create table tbl_ims_registry_project (
                                          project_id           VARCHAR(64)          not null,
                                          project_name         VARCHAR(64)          not null,
                                          project_desc         VARCHAR(255)         null,
                                          registry_id          VARCHAR(64)          not null,
                                          scope                INT4                 not null default 0,
                                          status               INT4                 not null default 1,
                                          user_id              VARCHAR(64)          not null,
                                          user_name            VARCHAR(64)          null,
                                          bp_id                VARCHAR(64)          null,
                                          bp_name              VARCHAR(64)          null,
                                          create_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                          update_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                          constraint PK_TBL_IMS_REGISTRY_PROJECT primary key (project_id),
                                          constraint project_name unique (project_name)
);

comment on table tbl_ims_registry_project is
    'registry project';

comment on column tbl_ims_registry_project.project_id is
    'project id';

comment on column tbl_ims_registry_project.project_name is
    'project name';

comment on column tbl_ims_registry_project.project_desc is
    'project descripion';

comment on column tbl_ims_registry_project.registry_id is
    'registry id';

comment on column tbl_ims_registry_project.scope is
    'scope(0:private;1:bp;2:public)';

comment on column tbl_ims_registry_project.status is
    'record status(1:normal;-1:deleted)';

comment on column tbl_ims_registry_project.user_id is
    'user id';

comment on column tbl_ims_registry_project.user_name is
    'user name';

comment on column tbl_ims_registry_project.bp_id is
    'bp id';

comment on column tbl_ims_registry_project.bp_name is
    'bp name';

comment on column tbl_ims_registry_project.create_time is
    'create time';

comment on column tbl_ims_registry_project.update_time is
    'update time';

/*==============================================================*/
/* Table: tbl_ims_registry_region                               */
/*==============================================================*/
create table tbl_ims_registry_region (
                                         registry_id          VARCHAR(64)          not null,
                                         region_id            VARCHAR(64)          not null,
                                         constraint PK_TBL_IMS_REGISTRY_REGION primary key (registry_id, region_id)
);

comment on column tbl_ims_registry_region.registry_id is
    'registry id';

comment on column tbl_ims_registry_region.region_id is
    'region id';

/*==============================================================*/
/* Table: tbl_ims_registry_user                                 */
/*==============================================================*/
create table tbl_ims_registry_user (
                                       registry_user_id     VARCHAR(64)          not null,
                                       registry_user_name   VARCHAR(64)          not null,
                                       registry_user_old_password VARCHAR(32)          null,
                                       registry_user_password VARCHAR(32)          not null,
                                       user_id              VARCHAR(64)          not null,
                                       user_name            VARCHAR(64)          null,
                                       bp_id                VARCHAR(64)          null,
                                       bp_name              VARCHAR(64)          null,
                                       status               INT4                 not null,
                                       create_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                       update_time          TIMESTAMP WITH TIME ZONE not null default CURRENT_TIMESTAMP,
                                       constraint PK_TBL_IMS_REGISTRY_USER primary key (registry_user_id),
                                       constraint registry_user_name unique (registry_user_name)
);

comment on column tbl_ims_registry_user.registry_user_id is
    'registry user id';

comment on column tbl_ims_registry_user.registry_user_name is
    'registry user name';

comment on column tbl_ims_registry_user.registry_user_old_password is
    'registry user old password';

comment on column tbl_ims_registry_user.registry_user_password is
    'registry user password';

comment on column tbl_ims_registry_user.user_id is
    'user id';

comment on column tbl_ims_registry_user.user_name is
    'user name';

comment on column tbl_ims_registry_user.bp_id is
    'bp id';

comment on column tbl_ims_registry_user.bp_name is
    'bp name';

comment on column tbl_ims_registry_user.status is
    'record status(0:creating;1:partially completed;2:completed:-1:deleted)';

comment on column tbl_ims_registry_user.create_time is
    'create time';

comment on column tbl_ims_registry_user.update_time is
    'update time';

alter table tbl_ims_registry_project
    add constraint FK_TBL_IMS__REFERENCE_TBL_IMS_ foreign key (registry_id)
        references tbl_ims_registry (registry_id)
        on delete restrict on update restrict;

alter table tbl_ims_registry_region
    add constraint FK_TBL_IMS__REFERENCE_TBL_IMS_ foreign key (registry_id)
        references tbl_ims_registry (registry_id)
        on delete restrict on update restrict;

