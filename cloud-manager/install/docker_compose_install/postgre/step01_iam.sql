/*==============================================================*/
/* DBMS name:      PostgreSQL 9.x                               */
/* Created on:     2021/10/19 16:24:02                          */
/*==============================================================*/

/*==============================================================*/
/* Table: tbl_bp_info                                           */
/*==============================================================*/

CREATE SEQUENCE "tbl_iam_role_role_id_seq"
    INCREMENT 1
    MINVALUE  105
    MAXVALUE 9223372036854775807
    START 105
    CACHE 1;

CREATE TABLE "tbl_bp_info" (
                               "bp_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                               "bp_name" varchar(64) COLLATE "pg_catalog"."default",
                               "website" varchar(64) COLLATE "pg_catalog"."default",
                               "license_id" varchar(64) COLLATE "pg_catalog"."default",
                               "master_user" varchar(64) COLLATE "pg_catalog"."default",
                               "status" int4,
                               "contact_info" varchar(512) COLLATE "pg_catalog"."default",
                               "create_time" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
                               "update_time" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
                               "description" varchar(255) COLLATE "pg_catalog"."default",
                               CONSTRAINT "pk_tbl_bp_info" PRIMARY KEY ("bp_id")
)
;


CREATE TABLE "tbl_iam_action" (
                                  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                  "project_id" varchar(64) COLLATE "pg_catalog"."default",
                                  "action_name" varchar(64) COLLATE "pg_catalog"."default",
                                  "action_type" int4,
                                  "enable" int4 NOT NULL DEFAULT 1,
                                  "description" varchar(255) COLLATE "pg_catalog"."default",
                                  "associated_apis" text COLLATE "pg_catalog"."default",
                                  "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "resource_id" varchar(64) COLLATE "pg_catalog"."default",
                                  CONSTRAINT "tbl_iam_action_pkey" PRIMARY KEY ("id")
)
;


COMMENT ON COLUMN "tbl_iam_action"."action_type" IS 'action type(1:list;2:write;3:read)';

COMMENT ON COLUMN "tbl_iam_action"."enable" IS 'enable(1:enable;-1:disable)';

COMMENT ON COLUMN "tbl_iam_action"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_action"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_assignment" (
                                      "actor_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                      "target_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                      "type" int4
)
;



COMMENT ON COLUMN "tbl_iam_assignment"."actor_id" IS 'userId or groupId';

COMMENT ON COLUMN "tbl_iam_assignment"."target_id" IS 'projectId';

COMMENT ON COLUMN "tbl_iam_assignment"."type" IS 'type(1:user;2:group)';

CREATE TABLE "tbl_iam_attachment" (
                                      "project_id" varchar COLLATE "pg_catalog"."default",
                                      "project_name" varchar(64) COLLATE "pg_catalog"."default",
                                      "principal_id" varchar COLLATE "pg_catalog"."default" NOT NULL,
                                      "principal_type" int4 NOT NULL,
                                      "target_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                      "target_type" int4 NOT NULL,
                                      "description" varchar(255) COLLATE "pg_catalog"."default",
                                      "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                      "attach_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                      "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                      CONSTRAINT "tbl_iam_attachment_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_attachment"."principal_type" IS 'principal type(1:user;2:group;3:role)';

COMMENT ON COLUMN "tbl_iam_attachment"."target_type" IS 'target type(1:policy; 3:role)';

COMMENT ON COLUMN "tbl_iam_attachment"."attach_time" IS 'attach time';

CREATE TABLE "tbl_iam_common_resource" (
                                           "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                           "service_id" varchar(64) COLLATE "pg_catalog"."default",
                                           "project_id" varchar(64) COLLATE "pg_catalog"."default",
                                           "name" varchar(64) COLLATE "pg_catalog"."default",
                                           "description" varchar(255) COLLATE "pg_catalog"."default",
                                           "lrn" varchar(255) COLLATE "pg_catalog"."default",
                                           "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           CONSTRAINT "tbl_iam_common_resource_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_common_resource"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_common_resource"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_condition_func" (
                                          "id" int4 NOT NULL,
                                          "condition_type" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "operation" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "description" varchar(255) COLLATE "pg_catalog"."default",
                                          CONSTRAINT "tbl_iam_condition_func_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_condition_func"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_condition_func"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_condition_key" (
                                         "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "project_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "condition_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                         "fixed_condition_name" bool,
                                         "condition_type" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "description" varchar(255) COLLATE "pg_catalog"."default",
                                         "is_global" bool NOT NULL,
                                         "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "service_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "resource_id" varchar(64) COLLATE "pg_catalog"."default",
                                         CONSTRAINT "tbl_iam_condition_key_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_condition_key"."condition_name" IS 'condition name:aos:tag, g:tag';

COMMENT ON COLUMN "tbl_iam_condition_key"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_condition_key"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_policy" (
                                  "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                  "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                  "description" varchar(255) COLLATE "pg_catalog"."default",
                                  "display_name" varchar(64) COLLATE "pg_catalog"."default",
                                  "policy_type" int4 NOT NULL,
                                  "is_autogen" bool,
                                  "default_version" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                  "attachment_count" int4,
                                  "is_attachable" bool,
                                  "tags" jsonb,
                                  "arn" varchar(255) COLLATE "pg_catalog"."default",
                                  "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                  "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                  "is_base" bool,
                                  CONSTRAINT "tbl_iam_policy_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_policy"."policy_type" IS 'policy type(1:system;2:custom;)';

COMMENT ON COLUMN "tbl_iam_policy"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_policy"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_policy_version" (
                                          "policy_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "version_id" varchar COLLATE "pg_catalog"."default",
                                          "document" text COLLATE "pg_catalog"."default",
                                          "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                          "description" varchar(255) COLLATE "pg_catalog"."default",
                                          CONSTRAINT "tbl_iam_policy_version_policy_id_version_id_key" UNIQUE ("policy_id", "version_id")
)
;



COMMENT ON COLUMN "tbl_iam_policy_version"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_policy_version"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_project" (
                                   "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                   "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                   "display_name" varchar(64) COLLATE "pg_catalog"."default",
                                   "description" varchar(255) COLLATE "pg_catalog"."default",
                                   "status" int4 NOT NULL DEFAULT 1,
                                   "enable" int4 NOT NULL DEFAULT 1,
                                   "parent_id" varchar(64) COLLATE "pg_catalog"."default",
                                   "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                   "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                   "type" int4,
                                   CONSTRAINT "tbl_iam_project_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_project"."name" IS 'project name';

COMMENT ON COLUMN "tbl_iam_project"."status" IS 'status(1:normal;-1:delete)';

COMMENT ON COLUMN "tbl_iam_project"."enable" IS 'enable(1:enable;-1:disable)';

COMMENT ON COLUMN "tbl_iam_project"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_project"."update_time" IS 'update time';

COMMENT ON COLUMN "tbl_iam_project"."type" IS 'type(system:0;admin:1; bp:2;personal:3;bp_user:4;bp default:10;system default; other:20;)';

CREATE TABLE "tbl_iam_resource_action" (
                                           "action_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                           "resource_id" varchar(64) COLLATE "pg_catalog"."default"
)
;



CREATE TABLE "tbl_iam_resource_attr" (
                                         "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "resource_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "attr_type" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "attr_name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "description" varchar(255) COLLATE "pg_catalog"."default",
                                         "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "model" varchar(64) COLLATE "pg_catalog"."default",
                                         CONSTRAINT "tbl_iam_resource_attr_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_resource_attr"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_resource_attr"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_resource_condition" (
                                              "resource_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                              "condition_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL
)
;



CREATE TABLE "tbl_iam_role" (
                                "role_id" int8 NOT NULL DEFAULT nextval('tbl_iam_role_role_id_seq'::regclass),
                                "platform" varchar(20) COLLATE "pg_catalog"."default",
                                "role" varchar(20) COLLATE "pg_catalog"."default",
                                "project_id" varchar(64) COLLATE "pg_catalog"."default",
                                "description" varchar(255) COLLATE "pg_catalog"."default",
                                "role_type" int4,
                                "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT "tbl_role_info_copy1_pkey" PRIMARY KEY ("role_id")
)
;



COMMENT ON COLUMN "tbl_iam_role"."role_type" IS 'role type(1:system;2:custom;)';

COMMENT ON COLUMN "tbl_iam_role"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_role"."update_time" IS 'update time';

CREATE TABLE "tbl_iam_service" (
                                   "id" varchar COLLATE "pg_catalog"."default" NOT NULL,
                                   "name" varchar(64) COLLATE "pg_catalog"."default",
                                   "display_name" varchar(255) COLLATE "pg_catalog"."default",
                                   "iam_code" varchar(64) COLLATE "pg_catalog"."default",
                                   "parent_id" varchar(64) COLLATE "pg_catalog"."default",
                                   "lrn_format" varchar(255) COLLATE "pg_catalog"."default",
                                   "lrn_regex" varchar(255) COLLATE "pg_catalog"."default",
                                   "status" int4 NOT NULL DEFAULT 1,
                                   "enable" int4 NOT NULL DEFAULT 1,
                                   "description" varchar(255) COLLATE "pg_catalog"."default",
                                   "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT "tbl_iam_service_pkey" PRIMARY KEY ("id")
)
;



COMMENT ON COLUMN "tbl_iam_service"."status" IS 'status(1:normal;-1:delete)';

COMMENT ON COLUMN "tbl_iam_service"."enable" IS 'enable(1:enable;-1:disable)';

COMMENT ON COLUMN "tbl_iam_service"."create_time" IS 'create time';

COMMENT ON COLUMN "tbl_iam_service"."update_time" IS 'update time';

CREATE TABLE "tbl_user_info" (
                                 "user_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                 "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "user_name" varchar(64) COLLATE "pg_catalog"."default",
                                 "password" varchar(100) COLLATE "pg_catalog"."default",
                                 "phone" varchar(256) COLLATE "pg_catalog"."default",
                                 "email" varchar(256) COLLATE "pg_catalog"."default",
                                 "address" varchar(512) COLLATE "pg_catalog"."default",
                                 "is_allowed" bool,
                                 "gender" int4,
                                 "status" int4,
                                 "kind" int4,
                                 "level" int4,
                                 "create_time" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
                                 "update_time" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
                                 "weixin" varchar(128) COLLATE "pg_catalog"."default",
                                 "access_from" int4,
                                 "invitation_code" varchar(512) COLLATE "pg_catalog"."default",
                                 "inviter" varchar(512) COLLATE "pg_catalog"."default",
                                 CONSTRAINT "pk_tbl_user_info" PRIMARY KEY ("user_id")
)
;



COMMENT ON COLUMN "tbl_user_info"."access_from" IS '0:register;1:admin;2:weixin';

ALTER TABLE "tbl_user_info" ADD CONSTRAINT "fk_tbl_user_reference_tbl_bp_i" FOREIGN KEY ("bp_id") REFERENCES "tbl_bp_info" ("bp_id") ON DELETE RESTRICT ON UPDATE RESTRICT;

SELECT setval('"tbl_iam_role_role_id_seq"', 115, true);


CREATE UNIQUE INDEX "user_index" ON "tbl_user_info" USING btree (
                                                                 "user_id" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );

CREATE UNIQUE INDEX "user_name_index" ON "tbl_user_info" USING btree (
                                                                      "user_name" COLLATE "pg_catalog"."default" "pg_catalog"."text_ops" ASC NULLS LAST
    );


