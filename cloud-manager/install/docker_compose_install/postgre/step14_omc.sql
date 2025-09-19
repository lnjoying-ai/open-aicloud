

CREATE TABLE "tbl_omc_alarm" (
                                 "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                 "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                 "description" varchar(255) COLLATE "pg_catalog"."default",
                                 "alert_group_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "status" int4,
                                 "level" int4,
                                 "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "alert_metric_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_alarm"."level" IS 'level(1:info;2:warning;3:critical)';
COMMENT ON COLUMN "tbl_omc_alarm"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_alarm"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_alarm_receiver
-- ----------------------------

CREATE TABLE "tbl_omc_alarm_receiver" (
                                          "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "rule_id" varchar(64) COLLATE "pg_catalog"."default",
                                          "receiver_id" varchar(64) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for tbl_omc_alert
-- ----------------------------

CREATE TABLE "tbl_omc_alert" (
                                 "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                 "alarm_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "way" int4,
                                 "way_settings" jsonb,
                                 "silence" int4,
                                 "selience_expression" varchar(512) COLLATE "pg_catalog"."default",
                                 "alert_template" text COLLATE "pg_catalog"."default",
                                 "alert_template_type" int4,
                                 "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                 "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "tbl_omc_alert"."way" IS 'way(1:sms;2:email)';
COMMENT ON COLUMN "tbl_omc_alert"."alert_template_type" IS 'alert_template_type(1:text;2:markdown)';
COMMENT ON COLUMN "tbl_omc_alert"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_alert"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_alert_group
-- ----------------------------

CREATE TABLE "tbl_omc_alert_group" (
                                       "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                       "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                       "description" varchar(255) COLLATE "pg_catalog"."default",
                                       "status" int4,
                                       "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                       "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                       "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "tbl_omc_alert_group"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_alert_group"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_alert_log
-- ----------------------------

CREATE TABLE "tbl_omc_alert_log" (
                                     "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                     "alarm_id" varchar(64) COLLATE "pg_catalog"."default",
                                     "rule_id" varchar(64) COLLATE "pg_catalog"."default",
                                     "alert_group_id" varchar(64) COLLATE "pg_catalog"."default",
                                     "alert_type" int4,
                                     "alert_status" int4,
                                     "level" int4,
                                     "way" int4,
                                     "labels" jsonb,
                                     "summary" varchar(512) COLLATE "pg_catalog"."default",
                                     "description" varchar(512) COLLATE "pg_catalog"."default",
                                     "in_silence" bool,
                                     "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     "resource_type_id" varchar(64) COLLATE "pg_catalog"."default",
                                     "resource_id" varchar(64) COLLATE "pg_catalog"."default",
                                     "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                     "user_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_alert_log"."alert_type" IS 'alert type(1:event;2:monitor)';
COMMENT ON COLUMN "tbl_omc_alert_log"."alert_status" IS 'alert_status(1:firing;2:resolved;3pending;4:inhibited;5:silenced;6:expired;7:unknown)';
COMMENT ON COLUMN "tbl_omc_alert_log"."level" IS 'level(1:info;2:warning;3:critical)';
COMMENT ON COLUMN "tbl_omc_alert_log"."way" IS 'way(1:sms;2:email)';
COMMENT ON COLUMN "tbl_omc_alert_log"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_alert_log"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_alert_log_process
-- ----------------------------

CREATE TABLE "tbl_omc_alert_log_process" (
                                             "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                             "alert_log_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                             "process_status" varchar(255) COLLATE "pg_catalog"."default",
                                             "process_time" date,
                                             "processor" varchar(64) COLLATE "pg_catalog"."default",
                                             "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                             "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                             "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             "message" varchar(512) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_alert_log_process"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_alert_log_process"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_alert_metric
-- ----------------------------

CREATE TABLE "tbl_omc_alert_metric" (
                                        "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                        "group_id" varchar(64) COLLATE "pg_catalog"."default",
                                        "metric_name" varchar(64) COLLATE "pg_catalog"."default",
                                        "label" jsonb,
                                        "duration_time" int4,
                                        "level" int4,
                                        "message" varchar(255) COLLATE "pg_catalog"."default",
                                        "metric_key" varchar(64) COLLATE "pg_catalog"."default",
                                        "alert_condition_display_statement" text COLLATE "pg_catalog"."default",
                                        "alert_condition_param" text COLLATE "pg_catalog"."default",
                                        "data_condition_label" text COLLATE "pg_catalog"."default",
                                        "data_filters" text COLLATE "pg_catalog"."default",
                                        "prom_content" text COLLATE "pg_catalog"."default",
                                        "status" int4,
                                        "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                        "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                        "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "tbl_omc_alert_metric"."status" IS 'status(0:启用;1:停用)';
COMMENT ON COLUMN "tbl_omc_alert_metric"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_alert_metric"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_alert_send_log
-- ----------------------------

CREATE TABLE "tbl_omc_alert_send_log" (
                                          "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "alert_log_id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                          "send_status" int4 DEFAULT 0,
                                          "log" text COLLATE "pg_catalog"."default",
                                          "create_time" timestamp(6)
)
;
COMMENT ON COLUMN "tbl_omc_alert_send_log"."send_status" IS 'send status(1:success;2:failed;3:unknown)';

-- ----------------------------
-- Table structure for tbl_omc_event
-- ----------------------------

CREATE TABLE "tbl_omc_event" (
                                 "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                 "name" varchar(128) COLLATE "pg_catalog"."default" NOT NULL,
                                 "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "service" varchar(64) COLLATE "pg_catalog"."default",
                                 "resource" varchar(64) COLLATE "pg_catalog"."default",
                                 "action" varchar(64) COLLATE "pg_catalog"."default",
                                 "resource_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "resource_inst_name" varchar(256) COLLATE "pg_catalog"."default",
                                 "type" varchar(64) COLLATE "pg_catalog"."default",
                                 "level" varchar(64) COLLATE "pg_catalog"."default",
                                 "request_path" varchar(1000) COLLATE "pg_catalog"."default",
                                 "description" text COLLATE "pg_catalog"."default",
                                 "friendly_description" text COLLATE "pg_catalog"."default",
                                 "result" varchar(64) COLLATE "pg_catalog"."default",
                                 "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                 "operator" varchar(64) COLLATE "pg_catalog"."default",
                                 "trigger_time" timestamptz(6) NOT NULL,
                                 "created_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP
)
;

-- ----------------------------
-- Table structure for tbl_omc_log
-- ----------------------------

CREATE TABLE "tbl_omc_log" (
                               "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                               "level" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                               "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                               "service" varchar(128) COLLATE "pg_catalog"."default",
                               "resource" varchar(128) COLLATE "pg_catalog"."default",
                               "resource_id" varchar(64) COLLATE "pg_catalog"."default",
                               "action" varchar(128) COLLATE "pg_catalog"."default",
                               "access_ip" varchar(64) COLLATE "pg_catalog"."default",
                               "user_agent" varchar(128) COLLATE "pg_catalog"."default",
                               "description" varchar(255) COLLATE "pg_catalog"."default",
                               "http_method" varchar(64) COLLATE "pg_catalog"."default",
                               "request_path" varchar(1000) COLLATE "pg_catalog"."default",
                               "in_params" text COLLATE "pg_catalog"."default",
                               "out_params" text COLLATE "pg_catalog"."default",
                               "result" varchar(64) COLLATE "pg_catalog"."default",
                               "execution_time" int4,
                               "user_id" varchar(64) COLLATE "pg_catalog"."default",
                               "operator" varchar(64) COLLATE "pg_catalog"."default",
                               "trigger_time" timestamptz(6),
                               "created_at" timestamptz(6) DEFAULT CURRENT_TIMESTAMP,
                               "operation_type" varchar(64) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for tbl_omc_monitor_instance
-- ----------------------------

CREATE TABLE "tbl_omc_monitor_instance" (
                                            "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                            "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                            "task_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "task_type" int4 NOT NULL,
                                            "exporter_type" varchar(64) COLLATE "pg_catalog"."default",
                                            "configs" jsonb,
                                            "stack_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "labels" jsonb,
                                            "data_source_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "endpoint" text COLLATE "pg_catalog"."default",
                                            "region_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "site_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "node_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "target_type" int4,
                                            "target_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "status" int4,
                                            "detail" varchar(255) COLLATE "pg_catalog"."default",
                                            "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                            "stack_params" jsonb,
                                            "custom_params" jsonb,
                                            "stack_template_version_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "spec_id" varchar(64) COLLATE "pg_catalog"."default",
                                            "targets" jsonb
)
;
COMMENT ON COLUMN "tbl_omc_monitor_instance"."task_type" IS '任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务
)

';
COMMENT ON COLUMN "tbl_omc_monitor_instance"."target_type" IS '目标类型(1:轻量级节点;2:nextstack;3:openstack)';
COMMENT ON COLUMN "tbl_omc_monitor_instance"."target_id" IS '目标(节点的target保存节点信息，云target保存云id)';
COMMENT ON COLUMN "tbl_omc_monitor_instance"."status" IS '部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing)
';
COMMENT ON COLUMN "tbl_omc_monitor_instance"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_monitor_instance"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_monitor_task
-- ----------------------------

CREATE TABLE "tbl_omc_monitor_task" (
                                        "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                        "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                        "description" varchar(255) COLLATE "pg_catalog"."default",
                                        "task_type" int4 NOT NULL,
                                        "data_source_id" varchar(64) COLLATE "pg_catalog"."default",
                                        "configs" jsonb,
                                        "deployment_status" int4,
                                        "target_type" int4,
                                        "targets" jsonb,
                                        "start_time" date,
                                        "completion_time" date,
                                        "params" jsonb,
                                        "stack_ids" jsonb,
                                        "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                        "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                        "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                        "stack_params" jsonb,
                                        "custom_params" jsonb,
                                        "stack_template_version_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_monitor_task"."task_type" IS '任务类型(1:轻量级节点部署任务;2：nextstack云监控部署任务;3：openstack云监控部署任务;4：GPU监控任务
)

';
COMMENT ON COLUMN "tbl_omc_monitor_task"."deployment_status" IS '部署状态(1:pending;2：running;3:succeeded;4:failed;5:progressing)
';
COMMENT ON COLUMN "tbl_omc_monitor_task"."target_type" IS '目标类型(1:轻量级节点;2:nextstack;3:openstack)';
COMMENT ON COLUMN "tbl_omc_monitor_task"."targets" IS '目标(节点的target保存节点信息，云target保存云id)';
COMMENT ON COLUMN "tbl_omc_monitor_task"."start_time" IS '部署开始时间';
COMMENT ON COLUMN "tbl_omc_monitor_task"."completion_time" IS '部署完成事件';
COMMENT ON COLUMN "tbl_omc_monitor_task"."params" IS '组件参数';
COMMENT ON COLUMN "tbl_omc_monitor_task"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_monitor_task"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_notify_object
-- ----------------------------

CREATE TABLE "tbl_omc_notify_object" (
                                         "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "receiver_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "notify_rule_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "notify_channels" varchar(255) COLLATE "pg_catalog"."default"
)
;

-- ----------------------------
-- Table structure for tbl_omc_notify_rule
-- ----------------------------

CREATE TABLE "tbl_omc_notify_rule" (
                                       "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                       "repeat_notify" bool,
                                       "notify_start_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "notify_end_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       "alarm_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_notify_rule"."notify_start_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_notify_rule"."notify_end_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_prometheus
-- ----------------------------

CREATE TABLE "tbl_omc_prometheus" (
                                      "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                      "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                      "type" int4 NOT NULL DEFAULT 0,
                                      "status" int4  NOT NULL,
                                      "lables" jsonb,
                                      "auth" jsonb,
                                      "region_id" varchar(64) COLLATE "pg_catalog"."default",
                                      "site_id" varchar(64) COLLATE "pg_catalog"."default",
                                      "node_id" varchar(64) COLLATE "pg_catalog"."default",
                                      "is_global" bool,
                                      "system_default" bool,
                                      "extra" jsonb,
                                      "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                      "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                      "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      "internal_endpoint_url " varchar(255) COLLATE "pg_catalog"."default",
                                      "external_endpoint_url " varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_prometheus"."type" IS '类型(0:server;1:agent)';
COMMENT ON COLUMN "tbl_omc_prometheus"."status" IS '状态(1:running;2:starting;3:error;4:crashed;5:updating configguration;6:paused;7:unknown)';
COMMENT ON COLUMN "tbl_omc_prometheus"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_prometheus"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_receiver
-- ----------------------------

CREATE TABLE "tbl_omc_receiver" (
                                    "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                    "name" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                    "description" varchar(255) COLLATE "pg_catalog"."default",
                                    "iam_user_id" varchar(64) COLLATE "pg_catalog"."default",
                                    "email" varchar(255) COLLATE "pg_catalog"."default",
                                    "phone" varchar(255) COLLATE "pg_catalog"."default",
                                    "configs" jsonb,
                                    "status" int4,
                                    "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                    "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                    "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    "notify_type" varchar(255) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_receiver"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_receiver"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_resource_status_stat
-- ----------------------------

CREATE TABLE "tbl_omc_resource_status_stat" (
                                                "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                                "resource_type" int4,
                                                "status" varchar(127) COLLATE "pg_catalog"."default",
                                                "status_counts" int4,
                                                "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                "day" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                                "user_id" varchar(64) COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_resource_status_stat"."resource_type" IS 'resource type(1:vm;2:node;3:cluster;4:container)';
COMMENT ON COLUMN "tbl_omc_resource_status_stat"."status" IS ')';
COMMENT ON COLUMN "tbl_omc_resource_status_stat"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_resource_status_stat"."day" IS 'day';

-- ----------------------------
-- Table structure for tbl_omc_rule
-- ----------------------------

CREATE TABLE "tbl_omc_rule" (
                                "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                "rule_type" int4,
                                "expr" text COLLATE "pg_catalog"."default",
                                "duration_time" int4,
                                "data_source_id" varchar(64) COLLATE "pg_catalog"."default",
                                "labels" jsonb,
                                "annotations" jsonb,
                                "prom_content" text COLLATE "pg_catalog"."default",
                                "alert_condition_contents" text COLLATE "pg_catalog"."default",
                                "alert_condition_params" text COLLATE "pg_catalog"."default",
                                "data_condition_contents" text COLLATE "pg_catalog"."default",
                                "data_condition_params" text COLLATE "pg_catalog"."default",
                                "unit" varchar(64) COLLATE "pg_catalog"."default",
                                "notice" bool,
                                "alert_template" text COLLATE "pg_catalog"."default",
                                "alert_template_type" int4,
                                "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                "alarm_id" varchar COLLATE "pg_catalog"."default",
                                "alert_message" text COLLATE "pg_catalog"."default"
)
;
COMMENT ON COLUMN "tbl_omc_rule"."rule_type" IS 'rule_type(1:static threshold;2:custom promQL)';
COMMENT ON COLUMN "tbl_omc_rule"."expr" IS 'promQL';
COMMENT ON COLUMN "tbl_omc_rule"."alert_template_type" IS 'alert_template_type(1:text;2:markdown)';
COMMENT ON COLUMN "tbl_omc_rule"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_rule"."update_time" IS 'update time';

-- ----------------------------
-- Table structure for tbl_omc_rule_resource
-- ----------------------------

CREATE TABLE "tbl_omc_rule_resource" (
                                         "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                         "resource_type" varchar(64) COLLATE "pg_catalog"."default",
                                         "resource_type_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "resource_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "alarm_data_type" varchar(255) COLLATE "pg_catalog"."default",
                                         "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                         "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                         "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;
COMMENT ON COLUMN "tbl_omc_rule_resource"."create_time" IS 'create time';
COMMENT ON COLUMN "tbl_omc_rule_resource"."update_time" IS 'update time';

-- ----------------------------
-- Primary Key structure for table tbl_omc_alarm
-- ----------------------------
ALTER TABLE "tbl_omc_alarm" ADD CONSTRAINT "tbl_omc_alarm_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alarm_receiver
-- ----------------------------
ALTER TABLE "tbl_omc_alarm_receiver" ADD CONSTRAINT "tbl_omc_alarm_receiver_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alert
-- ----------------------------
ALTER TABLE "tbl_omc_alert" ADD CONSTRAINT "tbl_omc_alert_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alert_group
-- ----------------------------
ALTER TABLE "tbl_omc_alert_group" ADD CONSTRAINT "tbl_omc_alert_group_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alert_log
-- ----------------------------
ALTER TABLE "tbl_omc_alert_log" ADD CONSTRAINT "tbl_omc_alert_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alert_log_process
-- ----------------------------
ALTER TABLE "tbl_omc_alert_log_process" ADD CONSTRAINT "tbl_omc_alert_log_process_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alert_metric
-- ----------------------------
ALTER TABLE "tbl_omc_alert_metric" ADD CONSTRAINT "tbl_omc_alert_metric_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_alert_send_log
-- ----------------------------
ALTER TABLE "tbl_omc_alert_send_log" ADD CONSTRAINT "tbl_omc_alert_send_status_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_event
-- ----------------------------
ALTER TABLE "tbl_omc_event" ADD CONSTRAINT "tbl_omc_omc_event_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_log
-- ----------------------------
ALTER TABLE "tbl_omc_log" ADD CONSTRAINT "tbl_omc_log_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_monitor_instance
-- ----------------------------
ALTER TABLE "tbl_omc_monitor_instance" ADD CONSTRAINT "tbl_omc_monitor_instance_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_monitor_task
-- ----------------------------
ALTER TABLE "tbl_omc_monitor_task" ADD CONSTRAINT "tbl_omc_monitor_task_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_notify_object
-- ----------------------------
ALTER TABLE "tbl_omc_notify_object" ADD CONSTRAINT "tbl_omc_notify_object_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_notify_rule
-- ----------------------------
ALTER TABLE "tbl_omc_notify_rule" ADD CONSTRAINT "tbl_omc_notify_rule_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_prometheus
-- ----------------------------
ALTER TABLE "tbl_omc_prometheus" ADD CONSTRAINT "tbl_omc_prometheus_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_receiver
-- ----------------------------
ALTER TABLE "tbl_omc_receiver" ADD CONSTRAINT "tbl_omc_receiver_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_resource_status_stat
-- ----------------------------
ALTER TABLE "tbl_omc_resource_status_stat" ADD CONSTRAINT "tbl_omc_resource_status_stat_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_rule
-- ----------------------------
ALTER TABLE "tbl_omc_rule" ADD CONSTRAINT "tbl_omc_rule_pkey" PRIMARY KEY ("id");

-- ----------------------------
-- Primary Key structure for table tbl_omc_rule_resource
-- ----------------------------
ALTER TABLE "tbl_omc_rule_resource" ADD CONSTRAINT "tbl_omc_rule_resource_pkey" PRIMARY KEY ("id");

-- 20241021
ALTER TABLE "public"."tbl_omc_log"
    ALTER COLUMN "user_agent" TYPE varchar(256) COLLATE "pg_catalog"."default";

-- 20250818 补充丢失的表
CREATE TABLE "tbl_omc_alert_silence_rule" (
                                              "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                              "enable" bool,
                                              "match_all" bool,
                                              "name" varchar(64) COLLATE "pg_catalog"."default",
                                              "eval_interval" int4,
                                              "priorities" jsonb,
                                              "tags" jsonb,
                                              "days" jsonb,
                                              "weeks" jsonb,
                                              "type" int4,
                                              "times" int4,
                                              "start_time" timestamptz(6),
                                              "end_time" timestamptz(6),
                                              "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                              "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                              "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                              "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;

CREATE TABLE "tbl_omc_alert_inhibition_rule" (
                                                 "id" varchar(64) COLLATE "pg_catalog"."default" NOT NULL,
                                                 "enable" bool,
                                                 "match_all" bool,
                                                 "name" varchar(64) COLLATE "pg_catalog"."default",
                                                 "eval_interval" int4,
                                                 "priorities" jsonb,
                                                 "tags" jsonb,
                                                 "bp_id" varchar(64) COLLATE "pg_catalog"."default",
                                                 "user_id" varchar(64) COLLATE "pg_catalog"."default",
                                                 "create_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                 "update_time" timestamptz(6) NOT NULL DEFAULT CURRENT_TIMESTAMP
)
;