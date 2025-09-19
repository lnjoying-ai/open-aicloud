<template>
  <div>
    <!-- 集群选项 -->
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("cluster.clusterOptions") }}</span>
        <span v-if="status === 1" style="float: right">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="
              infoFrom.create_type == 'custom'
                ? (infoFrom.create_type = 'import')
                : (infoFrom.create_type = 'custom')
            "
            >{{
              infoFrom.create_type == "custom"
                ? $t("cluster.editYaml")
                : $t("cluster.editForm")
            }}
          </el-button>
        </span>
      </div>
      <div class="text item">
        <el-form ref="form" :model="infoFrom" label-width="150px">
          <div v-if="status === 0">
            <el-form-item
              :label="$t('cluster.useTemplate') + ':'"
              class="formContentBlock"
            >
              <el-switch v-model="infoFrom.isUse" style="position: relative" />
              <el-button
                v-if="!infoFrom.isUse"
                style="position: absolute; right: 0px"
                type="primary"
                size="mini"
                icon="el-icon-edit"
                @click="
                  infoFrom.create_type == 'custom'
                    ? (infoFrom.create_type = 'import')
                    : (infoFrom.create_type = 'custom')
                "
                >{{
                  infoFrom.create_type == "custom"
                    ? $t("cluster.editYaml")
                    : $t("cluster.editForm")
                }}
              </el-button>
            </el-form-item>
            <el-form-item
              v-if="infoFrom.isUse"
              :label="$t('cluster.versionSelect') + ':'"
              :rules="[
                {
                  required: true,
                  message: $t('cluster.pleaseSelectVersion'),
                },
              ]"
            >
              <el-cascader
                v-model="infoFrom.version"
                :options="infoFromVersionList"
                style="width: 230px"
              />
            </el-form-item>
          </div>
          <el-form-item
            v-show="infoFrom.create_type == 'import' && infoFrom.isUse == false"
            :label="$t('cluster.clusterConfig') + ':'"
            style="font-size: 14px"
          >
            <div>
              <el-button
                type="primary"
                size="small"
                class="drbtn"
                @click="yamlclickLoad"
                >{{ $t("form.import") }}</el-button
              >
              <input
                id="files"
                ref="yamlrefFile"
                type="file"
                style="display: none; margin-top: 5px"
                @change="yamlfileLoad"
              />
            </div>

            <div
              style="
                line-height: 1.4;
                min-width: 100%;
                max-height: 52vh;
                overflow: auto;
              "
            >
              <yaml-editor
                v-model="infoFrom.stack_compose"
                :download-name="'clusterConfig'"
                :download-type="'yml'"
                :lint="true"
                :placeholder="''"
                :is-add="false"
                @changeLint="getLintState($event)"
              />
            </div>
          </el-form-item>
        </el-form>
        <!-- 集群选项 自定义 -->
        <el-collapse
          v-show="infoFrom.create_type == 'custom' && infoFrom.isUse == false"
          v-model="clusterCollapse"
        >
          <el-collapse-item name="1" :title="$t('cluster.clusterType')">
            <template slot="title" />
            <el-form ref="form" :inline="true" label-width="120px">
              <el-form-item :label="$t('cluster.clusterVersion') + ':'">
                <el-select
                  v-model="jke_config.k8s_version"
                  :placeholder="$t('cluster.pleaseSelectClusterVersion')"
                  style="width: 230px"
                >
                  <el-option
                    v-for="(item, index) in clusterTypeFormVersionList"
                    :key="index"
                    :label="item"
                    :value="item"
                    @change="changeVersion"
                  />
                </el-select>
              </el-form-item>
              <div style="display: inline-block">
                <el-form-item
                  :label="$t('cluster.dockerVersionRequirement') + ':'"
                  label-width="210px"
                >
                  <el-switch v-model="clusterTypeForm.isUse" />
                </el-form-item>
                <el-form-item
                  v-if="clusterTypeForm.isUse"
                  :label="$t('form.version') + ':'"
                  label-width="80px"
                >
                  <el-input
                    v-model="jke_config.docker_version"
                    style="width: 230px"
                  />
                </el-form-item>
              </div>
            </el-form>
          </el-collapse-item>
          <el-collapse-item :title="$t('form.imageLibrary')" name="2">
            <el-table
              :data="jke_config.registries"
              style="width: 100%; margin-left: 120px; max-width: 1100px"
            >
              <el-table-column
                prop="server"
                :label="$t('cluster.imageLibraryAddress')"
              />
              <el-table-column prop="username" :label="$t('form.loginName')" />
              <el-table-column prop="type" :label="$t('form.type')" />
              <el-table-column :label="$t('form.operation')">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    @click="jke_config.registries.splice(scope.$index, 1)"
                    >{{ $t("form.delete") }}</el-button
                  >
                </template>
              </el-table-column>
            </el-table>
            <div style="width: 100%; margin-left: 120px; max-width: 1100px">
              <el-button
                type="primary"
                icon="el-icon-plus"
                size="small"
                style="margin: 10px auto; display: block"
                @click="imagesDialogVisible = true"
                >{{ $t("form.add") }}</el-button
              >
            </div>
          </el-collapse-item>
          <el-collapse-item :title="$t('form.advancedConfig')" name="3">
            <div class="formItem">
              <el-form ref="form" :inline="true" label-width="130px">
                <el-form-item label="Ingress:">
                  <el-select
                    v-model="jke_config.system_addons.ingress.provider"
                    :placeholder="$t('form.pleaseSelect')"
                    style="width: 230px"
                  >
                    <el-option key="nginx" label="nginx" value="nginx" />
                  </el-select>
                </el-form-item>

                <el-form-item label="http port:">
                  <el-input
                    v-model="jke_config.system_addons.ingress.http_port"
                    :placeholder="$t('form.pleaseInput')"
                    style="width: 230px"
                  />
                </el-form-item>
                <el-form-item label="https port:">
                  <el-input
                    v-model="jke_config.system_addons.ingress.https_port"
                    :placeholder="$t('form.pleaseInput')"
                    style="width: 230px"
                  />
                </el-form-item>

                <el-form-item label="Network mode:">
                  <el-select
                    v-model="jke_config.system_addons.ingress.network_mode"
                    :placeholder="$t('form.pleaseSelect')"
                    style="width: 230px"
                  >
                    <el-option
                      key="hostNetwork"
                      label="hostNetwork"
                      value="hostNetwork"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="default_backend:" style="width: 100%">
                  <el-switch
                    v-model="jke_config.system_addons.ingress.default_backend"
                  />
                </el-form-item>
                <div
                  v-if="jke_config.system_addons.ingress.default_backend"
                  style="width: 100%"
                >
                  <el-form-item label="Monitor:">
                    <el-select
                      v-model="jke_config.system_addons.monitor.provider"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 230px"
                    >
                      <el-option
                        key="metrics-server"
                        label="metrics-server"
                        value="metrics-server"
                      />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="DNS:">
                    <el-select
                      v-model="jke_config.system_addons.dns.provider"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 230px"
                      @change="handleDNSclick"
                    >
                      <el-option
                        key="kube-dns"
                        label="kube-dns"
                        value="kube-dns"
                      />
                      <el-option
                        key="coredns"
                        label="coredns"
                        value="coredns"
                      />
                    </el-select>
                    <el-form-item
                      v-if="addressVisible"
                      :label="$t('form.address') + ':'"
                    >
                      <el-input
                        v-model="
                          jke_config.system_addons.dns.node_local.ip_address
                        "
                        style="width: 230px"
                      />
                    </el-form-item>
                  </el-form-item>
                  <el-form-item label="Network:">
                    <el-select
                      v-model="jke_config.system_addons.network.provider"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 230px"
                    >
                      <el-option key="calico" label="calico" value="calico" />
                      <el-option key="canal" label="canal" value="canal" />
                      <el-option
                        key="flannel"
                        label="flannel"
                        value="flannel"
                      />
                      <el-option key="weave" label="weave" value="weave" />
                      <el-option key="aci" label="aci" value="aci" />
                    </el-select>
                  </el-form-item>
                  <el-form-item label="mtu:">
                    <el-input-number
                      v-model="jke_config.system_addons.network.mtu"
                      style="width: 230px"
                      controls-position="right"
                      :min="0"
                    />
                  </el-form-item>
                </div>
              </el-form>
            </div>

            <el-form
              ref="form"
              :inline="true"
              :model="configForm"
              label-width="250px"
            >
              <div class="formItem">
                <div class="formItemTitle">{{ $t("cluster.nodeStatus") }}</div>
                <el-form-item
                  :label="$t('cluster.maxUnavailableWorkerNodes') + ':'"
                  style="width: 100%"
                >
                  <el-input-number
                    v-model="jke_config.upgrade_strategy.max_unavailable_worker"
                    controls-position="right"
                    :min="0"
                    style="width: 230px"
                  />
                </el-form-item>
                <el-form-item
                  :label="$t('cluster.nodePodEviction') + ':'"
                  style="width: 100%"
                >
                  <el-switch v-model="jke_config.upgrade_strategy.drain" />
                </el-form-item>
                <div
                  v-if="jke_config.upgrade_strategy.drain"
                  style="width: 100%"
                >
                  <div style="width: 100%">
                    <el-form-item
                      :label="$t('cluster.deleteEmptyDirectoryData') + ':'"
                    >
                      <el-switch
                        v-model="
                          jke_config.upgrade_strategy.node_drain_input
                            .delete_local_data
                        "
                      />
                    </el-form-item>
                    <el-form-item
                      :label="$t('cluster.force') + ':'"
                      label-width="80px"
                    >
                      <el-switch
                        v-model="
                          jke_config.upgrade_strategy.node_drain_input.force
                        "
                      />
                    </el-form-item>
                  </div>
                  <el-form-item
                    :label="$t('cluster.podSelfTerminationGracePeriod') + ':'"
                    style="width: 100%"
                  >
                    <el-radio-group v-model="configForm.nodePodRadio">
                      <el-radio label="1">{{
                        $t("cluster.usePodDefaultValues")
                      }}</el-radio>
                      <el-radio label="2">{{
                        $t("cluster.ignorePodDefaultValues")
                      }}</el-radio>
                    </el-radio-group>
                    <div
                      v-if="configForm.nodePodRadio == '2'"
                      style="margin-left: 25px; float: right"
                    >
                      <el-input-number
                        v-model="
                          jke_config.upgrade_strategy.node_drain_input
                            .grace_period
                        "
                        controls-position="right"
                        :min="0"
                        style="width: 230px"
                      />

                      <el-select
                        v-model="configForm.nodePodIntervalUnit"
                        :placeholder="$t('form.pleaseSelect')"
                        style="width: 230px"
                      >
                        <el-option
                          key="H"
                          :label="$t('cluster.hour')"
                          value="H"
                        />
                        <el-option
                          key="M"
                          :label="$t('cluster.minute')"
                          value="M"
                        />
                        <el-option
                          key="S"
                          :label="$t('cluster.second')"
                          value="S"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                  <el-form-item
                    :label="$t('cluster.evictionTimeout') + ':'"
                    style="width: 100%"
                  >
                    <el-radio-group v-model="configForm.nodeTimeoutRaiod">
                      <el-radio label="1">{{ $t("cluster.retry") }}</el-radio>
                      <el-radio label="2">{{ $t("cluster.giveUp") }}</el-radio>
                    </el-radio-group>
                    <div
                      v-if="configForm.nodeTimeoutRaiod == '2'"
                      style="margin-left: 187px; float: right"
                    >
                      <el-input-number
                        v-model="
                          jke_config.upgrade_strategy.node_drain_input.timeout
                        "
                        controls-position="right"
                        :min="0"
                        style="width: 230px"
                      />

                      <el-select
                        v-model="configForm.nodeTimeoutIntervalUnit"
                        :placeholder="$t('form.pleaseSelect')"
                        style="width: 230px"
                      >
                        <el-option
                          key="H"
                          :label="$t('cluster.hour')"
                          value="H"
                        />
                        <el-option
                          key="M"
                          :label="$t('cluster.minute')"
                          value="M"
                        />
                        <el-option
                          key="S"
                          :label="$t('cluster.second')"
                          value="S"
                        />
                      </el-select>
                    </div>
                  </el-form-item>
                </div>
              </div>
            </el-form>
          </el-collapse-item>
        </el-collapse>
        <!-- 集群选项 导入 -->
      </div>
    </el-card>
    <el-dialog
      :title="$t('form.imageList')"
      :visible.sync="imagesDialogVisible"
      width="800px"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane :label="$t('cluster.privateImageLibrary')" name="private">
          <el-table :data="tableData.registries">
            <el-table-column
              property="url"
              :label="$t('cluster.imageLibraryAddress')"
            />
            <el-table-column
              property="admin_name"
              :label="$t('form.loginName')"
            />
            <el-table-column :label="$t('form.type')">{{
              $t("form.private")
            }}</el-table-column>
            <el-table-column :label="$t('form.operation')" width="120">
              <template slot-scope="scope">
                <el-button
                  v-if="
                    !jke_config.registries
                      .map((item) => {
                        return item.server;
                      })
                      .includes(scope.row.url)
                  "
                  type="text"
                  size="mini"
                  @click="getImagesItem(scope.row)"
                  >{{ $t("form.select") }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="flex justify-end mt-4 px-4">
            <el-pagination
              :current-page="queryData.page_num"
              :page-sizes="[5, 10, 20, 50, 100]"
              :page-size="queryData.page_size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="tableData.total_num"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-tab-pane>
        <el-tab-pane :label="$t('form.thirdPartyImageLibrary')" name="public">
          <el-table :data="registries3rdData">
            <el-table-column
              property="url"
              :label="$t('cluster.imageLibraryAddress')"
            />
            <el-table-column
              property="access_key"
              :label="$t('form.loginName')"
            />
            <el-table-column :label="$t('form.type')">{{
              $t("form.public")
            }}</el-table-column>
            <el-table-column :label="$t('form.operation')" width="120">
              <template slot-scope="scope">
                <el-button
                  v-if="
                    !jke_config.registries
                      .map((item) => {
                        return item.server;
                      })
                      .includes(scope.row.url)
                  "
                  type="text"
                  size="mini"
                  @click="getImagesItem(scope.row)"
                  >{{ $t("form.select") }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
          <div class="flex justify-end mt-4 px-4">
            <el-pagination
              :current-page="queryData.page_num"
              :page-sizes="[5, 10, 20, 50, 100]"
              :page-size="queryData.page_size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="tableData.total_num"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
var yaml = require("js-yaml");
import { mapGetters } from "vuex";

import {
  k8stemplates,
  clustersVersions,
  registries,
  registries3rd,
} from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: { YamlEditor },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  data() {
    return {
      activeName: "private",
      yamlLintState: false,
      changeVersion(value) {},
      // 控制显示字段
      status: 0,
      imagesDialogVisible: false, // 镜像列表
      tableData: [], // 镜像列表\
      registries3rdData: [], // 第三方镜像列表
      queryData: {
        status: "",
        page_size: 10,
        page_num: 1,
      },
      clusterCollapse: [""], // 手风琴
      //  集群选项基本信息  //
      infoFrom: {
        // 集群选项基本信息
        isUse: false, // 是否使用模板
        version: [], // 版本选择
        create_type: "custom", // custom:用户自定义 import：导入
        stack_compose: "",
      },
      infoFromVersionList: [], // 集群选项基本信息 版本列表
      //  集群类型  //
      clusterTypeForm: {
        // 集群类型
        cluster_type: "k8s",
        version: "", // 集群版本选择
        isUse: false, // docker版本要求
        provider: "", // 云提供商
      },
      clusterTypeFormVersionList: [], // 集群版本列表
      //  镜像库  //
      imagesTableData: [], // 镜像列表数据

      //  高级配置  //
      configForm: {
        nodePodRadio: "1", // pod 自行终止的宽限期
        nodePodIntervalUnit: "S", // pod 自行终止的宽限期 单位
        nodeTimeoutRaiod: "1", // 驱散超时
        nodeTimeoutIntervalUnit: "S", // 驱散超时 单位
      },

      jke_configMain: "",
      jke_configInit: "",
      jke_config: {
        k8s_version: "", // k8s version
        deploy_job_time: 0, // 安装部署时，每步骤容器的超时时间 部署设置 时间
        local_cluster_auth_endpoint: {
          // 本地鉴权配置
          cacert: "", // 验证的证书
          enable: true, // 是否允许直接访问集群默认为false
          fqdn: "", // 直接访问的集群的所在的内网地址
        },
        enable_alerting: false, // 集群是否启用告警
        enable_monitoring: false, // 集群是否启用告警
        authentication: {
          strategy: "x509", // 认证策略，默认值为x509
          sans: [], // List of additional hostnames and IPs to include in the api server PKI cert
          webhook: {
            config_file: "", // ConfigFile is a multiline string that represent a custom webhook config file
            cache_timeout: "", // CacheTimeout controls how long to cache authentication decisions
          },
        },
        authorization: {
          mode: "rbac", // 授权策略，默认值为rbac
          // options: {}, //Authorization mode options 待启用
        },
        docker_version: "", // 若为空，或为空串，表示忽略版本的要求，根据系统后端配置的docker版本的范围去匹配，否则按这里配置的值去匹配节点
        cloud_provider: {
          // 若指定了云厂商，则在指定的云厂商上部署后续版本考虑
          provider: "", // cloud类型：aws
          cloud_args: "", // cloud参数
        },
        registries: [], // 镜像仓库配置
        system_addons: {
          // 系统插件系统
          dns: {
            // dns插件配置
            provider: "coredns", // kube-dns or coredns
            // 更多...//待启用
            node_local: {
              ip_address: "10.43.0.10",
              update_strategy: {
                strategy: "RollingUpdate",
                rolling_update: {
                  max_surge: 10,
                  max_unavailable: 3,
                },
              },
            },
          },
          ingress: {
            // ingress插件配置
            provider: "nginx", // 默认为nginx
            http_port: "8080", // http端口
            https_port: "8443", // https端口
            default_backend: true, // 是否启用
            network_mode: "hostNetwork", //
            // 更多...//待启用
          },
          monitor: {
            // monitor插件配置
            provider: "metrics-server", // 默认metrics-server
            // 更多...//待启用
          },
          network: {
            // 网络插件配置
            provider: "calico", // 默认calico,canal,flannel,weave,aci
            mtu: 1450, // 网络插件mtu
            // 更多...//待启用
          },
        },
        apps: {}, // 待启用
        services: {
          // k8s的核心组件的服务参数
          etcd: {
            // etcd服务参数
            external_config: {
              // 外部etcd配置
              urls: [], // url地址
              ca_cert: "", // ca证书
              client_cert: "", // 客户端证书
              key: "", // 私钥
            },
            backup_config: {
              // etcd备份配置
              enable: true, // 外部etcd配置
              interval_hours: 24, // etcd备份周期 ,单位小时，默认24小时
              backup_replicas: 3, // 备份副本数量，默认3份
              time_out: 500, // 备份超时时间秒级，默认500s
            },
            extra_args: [], // 额外的参数配置key值election-timeout,heartbeat-interval
            extra_binds: [], // 额外的绑定关系
            extra_envs: [], // 额外的环境变量
          },
          kube_apiserver: {
            audit_log: {
              enable: false,
            },
            // 待启用 //kube-apiserver服务参数
            always_pull_images: false, // 是否总是下载镜像
            pod_security_policy: false, // 是否启用pod安全策略
            service_node_port_range: "30000-32767", // 集群服务分配的端口范围 默认为30000-32767
            service_cluster_ip_range: "10.43.0.0/16", // 集群分配的ip地址范围。若未设置，默认为10.43.0.0/16
            secrets_encryption_config: {
              enable: false, // 是否启用
              custom_config: {
                // 用户的加密配信息
                type_meta: {
                  kind: "",
                  api_version: "",
                }, //
                resources: {}, //
              },
            }, //
            extra_args: [
              // 额外参数
              {
                key: "",
                value: "",
              },
            ], // 额外的参数配置
            extra_binds: [], // 额外的绑定关系
            extra_envs: [], // 额外的环境变量
          },
          kube_controller: {
            cluster_cidr: "10.42.0.0/16", //
            service_cluster_ip_range: "10.43.0.0/16", //
            extra_args: [
              // 额外参数
              {
                key: "",
                value: "",
              },
            ], // 额外的参数配置
            extra_binds: [], // 额外的绑定关系
            extra_envs: [], // 额外的环境变量
          }, // 待启用//kube-controller服务参数
          kubelet: {
            cluster_domain: "", // 集群域名参数配置
            cluster_dns_server: "", // dns server配置
            generate_serving_certificate: false, // 是否每个节点都生成独立的证书
            fail_swap_on: false, // 如果swap_on开启，则启动失败。默认为false
            extra_args: [
              // 额外参数
              {
                key: "",
                value: "",
              },
            ], // 额外的参数配置
            extra_binds: [], // 额外的绑定关系
            extra_envs: [], // 额外的环境变量
          }, // 待启用//kubelet 服务参数
          kube_scheduler: {
            extra_args: [
              // 额外参数
              {
                key: "",
                value: "",
              },
            ], // 额外的参数配置
            extra_binds: [], // 额外的绑定关系
            extra_envs: [], // 额外的环境变量
          }, // 待启用 //kube_scheduler 服务参数
          kube_proxy: {
            extra_args: [
              // 额外参数
              {
                key: "",
                value: "",
              },
            ], // 额外的参数配置
            extra_binds: [], // 额外的绑定关系
            extra_envs: [], // 额外的环境变量
          }, // 待启用 //kube_proxy 服务参数
        },
        upgrade_strategy: {
          // 更新策略
          drain: false, // 升级节点之前是否要驱散pod，若为true，在升级节点之前先驱散相关的节点。
          max_unavailable_controller: 0, // 最大不可用的控制节点的数量。数量超过此值时不可以升级
          max_unavailable_worker: 0, // 最大不可用的工作节点的数量.不可用的设备的数量超过此值时，不可以升级
          node_drain_input: {
            // 节点驱逐的配置信息
            delete_local_data: false, // 是否删除本地数据
            force: false, // 是否强制驱逐
            grace_period: 30, // pod 自行终止的宽限期 时间
            ignore_daemonsets: false, // 未启用
            timeout: 0, // 驱散超时 时间
          },
        },
        cluster_scan: {
          // 集群扫描策略
          enable: false, // 是否启用集群扫描
          scheduler_config: {
            cron_schedule: 1, // 扫描计划
            keep_num: "", // 保留的集群扫描计划数量
          },
          scan_config: {
            override_skip: "", //
            override_bentmark_version: "", //
            profile_type: "", //
            debug_worker: true, //
            debug_controller: true, //
          },
        },
        encryption_config: {
          // 待启用
        },
      },
      // 地址输入框
      addressVisible: false,
    };
  },
  watch: {
    "infoFrom.create_type": {
      deep: true,
      handler(val) {
        if (val == "custom") {
          var stack_compose = yaml.load(this.infoFrom.stack_compose);
          var data = this.$script.jkeconfigServicesCustom(
            JSON.parse(JSON.stringify(stack_compose))
          );
          this.jke_config = this.deepMerge(this.jke_configInit, data);
        } else if (val == "import") {
          var data = this.$script.jkeconfigServices(
            JSON.parse(JSON.stringify(this.jke_config))
          );
          this.infoFrom.stack_compose = yaml.dump(data);
        }
      },
    },
    "infoFrom.isUse": {
      deep: true,
      handler(val) {
        if (val == true) {
        } else if (val == false) {
        }
      },
    },
    jke_config: {
      deep: true,
      handler(val) {
        this.jke_configMain = this.$script.jkeconfigServices(
          JSON.parse(JSON.stringify(val))
        );
      },
    },
    "clusterTypeForm.cluster_type": {
      deep: true,
      handler(val) {
        this.getClustersVersions();
      },
    },
    "configForm.nodePodRadio": {
      // pod 自行终止的宽限期
      deep: true,
      handler(val) {
        this.jke_config.upgrade_strategy.node_drain_input.grace_period = 30; // pod 自行终止的宽限期 时间
      },
    },
    "configForm.nodeTimeoutRaiod": {
      // 驱散超时
      deep: true,
      handler(val) {
        this.jke_config.upgrade_strategy.node_drain_input.timeout = 0; // 驱散超时 时间
      },
    },
    imagesDialogVisible: {
      deep: true,
      handler(val) {
        if (val) {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
  },
  mounted() {
    this.jke_configInit = JSON.parse(JSON.stringify(this.jke_config));
    this.jke_configMain = this.$script.jkeconfigServices(
      JSON.parse(JSON.stringify(this.jke_config))
    );
    if (
      this.$route.query.type == "isUse" &&
      this.$route.query.temId &&
      this.$route.query.verId
    ) {
      this.infoFrom.isUse = true;
      this.infoFrom.version = [
        this.$route.query.temId,
        this.$route.query.verId,
      ];
    }
  },
  created() {
    this.getK8stemplates(); // 获取模板列表
    this.getClustersVersions(); // 集群模板获取集群名称
  },
  methods: {
    getLintState(state) {
      this.yamlLintState = state;
      this.$emit("getYamlLint", state);
    },
    // 切换DNS调用
    handleDNSclick(value) {
      if (value == "coredns") {
        this.addressVisible = true;
      } else {
        this.addressVisible = false;
        this.jke_config.system_addons.dns.node_local.ip_address = "";
      }
    },
    deepMerge(obj1, obj2) {
      var key;
      for (key in obj2) {
        // 如果target(也就是obj1[key])存在，且是对象的话再去调用deepMerge，否则就是obj1[key]里面没这个对象，需要与obj2[key]合并
        obj1[key] =
          obj1[key] && obj1[key].toString() === "[object Object]"
            ? this.deepMerge(obj1[key], obj2[key])
            : (obj1[key] = obj2[key]);
      }
      return obj1;
    },
    // 接收父组件传值
    message(message) {
      this.jke_config = message;
    },
    nowStatus() {
      this.imagesTableData
        .map((item) => {
          return item.id;
        })
        .include(scope.row.registry_id);
    },
    getImagesItem(item) {
      if (this.activeName == "public") {
        this.jke_config.registries.push({
          server: item.url, // 镜像库地址
          username: item.access_key, // 登录名
          password: item.access_secret,
          type: "public", // 类型
        });
      } else if (this.activeName == "private") {
        this.jke_config.registries.push({
          server: item.url, // 镜像库地址
          username: item.admin_name, // 登录名
          type: "private", // 类型
        });
      }
    },

    yamlclickLoad() {
      // 授权集群端点 点击上传证书文件
      this.$refs.yamlrefFile.dispatchEvent(new MouseEvent("click"));
    },
    yamlfileLoad() {
      var _this = this;
      const selectedFile = this.$refs.yamlrefFile.files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        _this.infoFrom.stack_compose = this.result;
        _this.yamlJson = yaml.load(this.result);
      };
    },
    getK8stemplates() {
      k8stemplates({ cluster_type: "k8s" }).then((res) => {
        // 获取模板列表
        this.infoFromVersionList = [];
        res.templates.map((item) => {
          var data = {
            value: item.id,
            label: item.name,
            children: [],
          };
          if (item.versions != null && item.versions.length > 0) {
            item.versions.map((item1) => {
              data.children.push({
                value: item1.id,
                label: item1.version,
              });
            });
          }
          this.infoFromVersionList.push(data);
        });
      });
    },
    getClustersVersions() {
      clustersVersions(this.clusterTypeForm.cluster_type).then((res) => {
        // 集群模板获取集群名称
        this.clusterTypeFormVersionList = res.k8s;
        this.jke_config.k8s_version = res.k8s[0];
      });
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      if (this.activeName == "public") {
        this.getRegistries3rd();
      } else {
        this.getRegistries();
      }
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      if (this.activeName == "public") {
        this.getRegistries3rd();
      } else {
        this.getRegistries();
      }
    },
    init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      this.getRegistries();
      this.getRegistries3rd();
    },
    async getRegistries() {
      const list = await registries(this.queryData);
      this.tableData = list;
    },
    async getRegistries3rd() {
      const RDlist = await registries3rd(this.queryData);
      this.registries3rdData = RDlist.registries;
    },
  },
};
</script>

<style lang="scss" scoped>
.el-card__header {
  padding: 18px 20px;
  border-bottom: 0px solid #ebeef5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}

.box-card {
  border: none;

  ::v-deep .el-card__header {
    padding: 10px 20px;
    font-weight: bold;
  }
}

.el-collapse {
  border-bottom: none;

  ::v-deep .el-collapse-item__wrap {
    border-bottom: none;
  }
}

.formItem {
  padding: 10px 20px;

  .formItemTitle {
    // font-weight: bold;
    font-size: 14px;
    padding-bottom: 10px;
  }
}

.textareaStyle {
  ::v-deep .el-textarea__inner {
    background-color: #000;
    color: #fff;

    &::-webkit-scrollbar-track-piece {
      background: #d3dce6;
    }

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: #99a9bf;
      border-radius: 20px;
    }
  }
}
</style>
