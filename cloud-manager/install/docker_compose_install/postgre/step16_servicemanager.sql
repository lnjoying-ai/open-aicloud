/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2023/12/22 11:02:11                          */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_service_gateway_info                              */
/*==============================================================*/
create table tbl_service_gateway_info (
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(64)          null,
   description          VARCHAR(64)          null,
   endpoint             VARCHAR(256)         null,
   purpose              VARCHAR(256)         null,
   instance_id          VARCHAR(256)         null,
   status               INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_SERVICE_GATEWAY_INFO primary key (id)
);

/*==============================================================*/
/* Table: tbl_service_gateway_port_allocation                   */
/*==============================================================*/
create table tbl_service_gateway_port_allocation (
   id                   VARCHAR(64)          not null,
   port_range_id        VARCHAR(64)          null,
   external_ip          VARCHAR(256)         null,
   internal_ip          VARCHAR(256)         null,
   port                 INT4                 null,
   listen_port          INT4                 null,
   status               INT4                 null,
   service_port_target_service_id VARCHAR(64)          null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_SERVICE_GATEWAY_PORT_AL primary key (id)
);

/*==============================================================*/
/* Table: tbl_service_gateway_port_info                         */
/*==============================================================*/
create table tbl_service_gateway_port_info (
   id                   VARCHAR(64)          not null,
   service_gateway_id   VARCHAR(64)          null,
   internal_ip          VARCHAR(256)         null,
   external_ip          VARCHAR(256)         null,
   port_range_min       INT4                 null,
   port_range_max       INT4                 null,
   listen_port_range_min INT4                 null,
   listen_port_range_max INT4                 null,
   description          TEXT                 null,
   total                INT4                 null,
   "left"               INT4                 null,
   status               INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_SERVICE_GATEWAY_PORT_IN primary key (id)
);

/*==============================================================*/
/* Table: tbl_service_port_info                                 */
/*==============================================================*/
create table tbl_service_port_info (
   id                   VARCHAR(64)          not null,
   name                 VARCHAR(64)          not null,
   purpose              VARCHAR(64)          null,
   target_type          VARCHAR(64)          null,
   deployment           VARCHAR(64)          null,
   status               INT4                 null,
   description          TEXT                 null,
   bp_id                VARCHAR(64)          null,
   user_id              VARCHAR(64)          null,
   tags                 TEXT                 null,
   create_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   update_time          TIMESTAMP WITH TIME ZONE null default CURRENT_TIMESTAMP,
   constraint PK_TBL_SERVICE_PORT_INFO primary key (id)
);

/*==============================================================*/
/* Table: tbl_service_port_target_service_info                  */
/*==============================================================*/
create table tbl_service_port_target_service_info (
   id                   VARCHAR(64)          not null,
   service_port_id      VARCHAR(64)          null,
   description          TEXT                 null,
   port_allocation_id   VARCHAR(64)          null,
   svc_node             VARCHAR(64)          null,
   svc_ip               VARCHAR(256)         null,
   svc_port             INT4                 null,
   target_svc_node      VARCHAR(64)          null,
   target_svc_port      INT4                 null,
   protocol             VARCHAR(64)          null,
   site                 VARCHAR(64)          null,
   service              VARCHAR(64)          null,
   target_ip            VARCHAR(64)          null,
   target_port          INT4                 null,
   cert                 TEXT                 null,
   status               INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_SERVICE_PORT_TARGET_SER primary key (id)
);

