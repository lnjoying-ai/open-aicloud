/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2022/4/15 17:07:06                           */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_cfgdata_container_info                            */
/*==============================================================*/
create table tbl_cfgdata_container_info (
    cfg_volume_id        VARCHAR(64)          not null,
    data_id              VARCHAR(255)         null,
    data_group           VARCHAR(255)         null,
    data_hash            VARCHAR(255)         null,
    user_id              VARCHAR(64)          null,
    node_id              VARCHAR(64)          null,
    container_id         VARCHAR(64)          null,
    sync_state           INT4                 null,
    create_time          TIMESTAMP WITH TIME ZONE null,
    update_time          TIMESTAMP WITH TIME ZONE null,
    constraint PK_TBL_CFGDATA_CONTAINER_INFO primary key (cfg_volume_id)
);

/*==============================================================*/
/* Table: tbl_container_clsinst_info                            */
/*==============================================================*/
create table tbl_container_clsinst_info (
   inst_id              VARCHAR(64)          not null,
   ref_id               VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   region_id            VARCHAR(64)          null,
   container_name       VARCHAR(64)          null,
   status               INT4                 null,
   params               TEXT                 null,
   inspet_params        TEXT                 null,
   auto_run             BOOL                 null,
   orch_type            VARCHAR(20)          null,
   registry_info        TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   constraint PK_TBL_CONTAINER_CLSINST_INFO primary key (inst_id),
   constraint AK_INSTNAME_NODE_TBL_CONT unique (node_id, container_name)
);

/*==============================================================*/
/* Index: container_index2                                      */
/*==============================================================*/
create unique index container_index2 on tbl_container_clsinst_info (
inst_id
);

/*==============================================================*/
/* Table: tbl_container_inst_info                               */
/*==============================================================*/
create table tbl_container_inst_info (
   inst_id              VARCHAR(64)          not null,
   ref_id               VARCHAR(64)          null,
   node_id              VARCHAR(64)          null,
   site_id              VARCHAR(64)          null,
   region_id            VARCHAR(64)          null,
   spec_id              VARCHAR(64)          null,
   container_name       VARCHAR(64)          null,
   status               INT4                 null,
   inspet_container_params TEXT                 null,
   ip                   VARCHAR(64)          null,
   send_create_num      INT4                 null,
   start_num            INT4                 null,
   fail_num             INT4                 null,
   image_name           VARCHAR(256)         null,
   cmd                  TEXT                 null,
   bp_id                VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   event_type           INT4                 not null default 0,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   remove_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   report_time          TIMESTAMP WITH TIME ZONE null,
   restart_policy       VARCHAR(64)          null,
   constraint PK_TBL_CONTAINER_INST_INFO primary key (inst_id)
);

/*==============================================================*/
/* Index: container_index                                       */
/*==============================================================*/
create unique index container_index on tbl_container_inst_info (
inst_id
);

/*==============================================================*/
/* Table: tbl_container_operator_info                           */
/*==============================================================*/
create table tbl_container_operator_info (
   oper_id              VARCHAR(64)          not null,
   inst_id              VARCHAR(64)          null,
   oper_type            VARCHAR(20)          null,
   oper_time            TIMESTAMP WITH TIME ZONE null,
   oper_result          TEXT                 null,
   operator_id          VARCHAR(64)          null,
   operator_name        VARCHAR(64)          null,
   constraint PK_TBL_CONTAINER_OPERATOR_INFO primary key (oper_id)
);

/*==============================================================*/
/* Index: oper_index                                            */
/*==============================================================*/
create  index oper_index on tbl_container_operator_info (
( inst_id )
);

/*==============================================================*/
/* Table: tbl_container_run_info                                */
/*==============================================================*/
create table tbl_container_run_info (
   run_id               VARCHAR(64)          not null,
   inst_id              VARCHAR(64)          null,
   start_time           TIMESTAMP WITH TIME ZONE null,
   stop_time            TIMESTAMP WITH TIME ZONE null,
   state                INT4                 null,
   inst_detail_info     TEXT                 null,
   constraint PK_TBL_CONTAINER_RUN_INFO primary key (run_id)
);

/*==============================================================*/
/* Index: run_index                                             */
/*==============================================================*/
create unique index run_index on tbl_container_run_info (
run_id
);

/*==============================================================*/
/* Table: tbl_container_spec_info                               */
/*==============================================================*/
create table tbl_container_spec_info (
   spec_id              VARCHAR(64)          not null,
   spec_name            VARCHAR(64)          null,
   image_name           VARCHAR(256)         null,
   registry_id          VARCHAR(64)          null,
   cmd                  TEXT                 null,
   scheduling_strategy  TEXT                 null,
   cpu_num              INT4                 null,
   mem_limit            INT8                 null,
   disk_limit           INT8                 null,
   transmit_band_limit  INT4                 null,
   recv_band_limit      INT4                 null,
   gpu_num              INT4                 null,
   dev_need_info        TEXT                 null,
   container_params     TEXT                 null,
   auto_run             BOOL                 null,
   replica_num          INT4                 null,
   user_id              VARCHAR(64)          null,
   bp_id                VARCHAR(64)          null,
   status               INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   extra_params         JSONB                null,
   restart_policy       VARCHAR(64)          null,
   failover_policy      TEXT                 null,
   constraint PK_TBL_CONTAINER_SPEC_INFO primary key (spec_id),
   constraint AK_SPEC_NAME_USER_ID_TBL_CONT unique (spec_name, user_id)
);

/*==============================================================*/
/* Index: container_spec_index                                  */
/*==============================================================*/
create unique index container_spec_index on tbl_container_spec_info (
spec_id
);

alter table tbl_container_inst_info
   add constraint FK_TBL_CONT_REFERENCE_TBL_CONT foreign key (spec_id)
      references tbl_container_spec_info (spec_id)
      on delete restrict on update restrict;

alter table tbl_container_operator_info
   add constraint FK_TBL_CONT_REFERENCE_TBL_CONT foreign key (inst_id)
      references tbl_container_inst_info (inst_id)
      on delete restrict on update restrict;

alter table tbl_container_run_info
   add constraint FK_TBL_CONT_REFERENCE_TBL_CONT foreign key (inst_id)
      references tbl_container_inst_info (inst_id)
      on delete restrict on update restrict;


CREATE OR REPLACE FUNCTION reset_event_on_status_change()
RETURNS TRIGGER
AS $$
BEGIN
    NEW.event_type = 0;
    RETURN NEW;
END;
$$
    LANGUAGE 'plpgsql'
;


CREATE TRIGGER inst_event_trigger BEFORE UPDATE OF "status" ON tbl_container_inst_info
FOR EACH ROW
EXECUTE PROCEDURE "reset_event_on_status_change"();

