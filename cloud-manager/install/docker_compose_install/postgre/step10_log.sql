/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/10/25 10:06:39                          */
/*==============================================================*/


/*==============================================================*/
/* Table: tbl_operation_log                                     */
/*==============================================================*/
create table tbl_operation_log (
                                   id                   VARCHAR(64)          not null,
                                   description          VARCHAR(100)         null,
                                   user_id              VARCHAR(64)          null,
                                   user_name            VARCHAR(64)          null,
                                   start_time           TIMESTAMP WITH TIME ZONE null,
                                   spend_time           INT4                 null,
                                   uri                  VARCHAR(500)         null,
                                   method               VARCHAR(10)          null,
                                   parameter            TEXT                 null,
                                   user_agent           VARCHAR(500)         null,
                                   ip                   VARCHAR(30)          null,
                                   result               TEXT                 null,
                                   constraint PK_TBL_OPERATION_LOG primary key (id)
);

comment on table tbl_operation_log is
    'operation log';

comment on column tbl_operation_log.id is
    'id';

comment on column tbl_operation_log.spend_time is
    'ms';

