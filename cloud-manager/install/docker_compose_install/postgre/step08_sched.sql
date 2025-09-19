/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/11/12 20:57:12                          */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_sched_edge_monopoly                               */
/*==============================================================*/
create table tbl_sched_edge_monopoly (
   node_id              VARCHAR(64)          not null,
   ref_id               VARCHAR(64)          not null,
   status               INT4                 null,
   create_time          TIMESTAMP WITH TIME ZONE null,
   update_time          TIMESTAMP WITH TIME ZONE null,
   constraint PK_TBL_SCHED_EDGE_MONOPOLY primary key (node_id, ref_id)
);

