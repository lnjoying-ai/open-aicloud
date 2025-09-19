<template>
  <div class="infoShowPage">
    <!-- 集群选项 -->
    <el-card v-if="jke_configData" class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("cluster.clusterOptions") }}</span>
        <el-button
          type="primary"
          size="mini"
          style="float: right"
          @click="
            infoFrom.create_type == 'custom'
              ? (infoFrom.create_type = 'import')
              : (infoFrom.create_type = 'custom')
          "
          >{{
            infoFrom.create_type == "custom"
              ? $t("cluster.showYaml")
              : $t("cluster.showForm")
          }}</el-button
        >
      </div>
      <div class="text item">
        <el-form ref="form" label-width="140px">
          <div v-if="status === 0" style="display: flex">
            <el-form-item
              v-if="infoFrom.isUse"
              :label="$t('cluster.templateName') + ':'"
            >
              <span>{{ template_name }} </span>
            </el-form-item>
            <el-form-item
              v-if="infoFrom.isUse"
              :label="$t('cluster.versionNumber') + ':'"
            >
              <span
                >{{
                  infoFromVersionList.filter(
                    (item) => item.id == detailMain.tmpl_ver_id
                  )[0]
                    ? infoFromVersionList.filter(
                        (item) => item.id == detailMain.tmpl_ver_id
                      )[0].version
                    : "-"
                }}
              </span>
            </el-form-item>
          </div>
          <el-form-item
            v-show="infoFrom.create_type == 'import'"
            :label="$t('cluster.clusterConfig') + ':'"
          >
            <div
              style="
                line-height: 1.4;
                min-width: 100%;
                max-height: 52vh;
                overflow: auto;
              "
            >
              <yaml-editor
                ref="yamlEditor"
                v-model="infoFrom.stack_compose"
                :download-name="yamlEditorName"
                :download-type="'yml'"
                :placeholder="''"
                :is-add="false"
                :readonly="'nocursor'"
              />
            </div>
          </el-form-item>
        </el-form>
        <!-- 集群选项 自定义 -->
        <el-collapse
          v-show="infoFrom.create_type == 'custom'"
          v-model="clusterCollapse"
        >
          <el-collapse-item name="1" :title="$t('cluster.clusterInfo')">
            <el-form ref="form" :inline="true" label-width="140px">
              <el-form-item :label="$t('cluster.clusterType') + ':'">
                <span>{{ "k8s" }}</span>
              </el-form-item>
              <el-form-item :label="$t('cluster.clusterVersion') + ':'">
                <span>{{ jke_configData.k8s_version || "-" }}</span>
              </el-form-item>
              <div style="display: inline-block">
                <el-form-item :label="$t('cluster.dockerVersion') + ':'">
                  <span>{{ jke_configData.docker_version || "-" }}</span>
                </el-form-item>
              </div>
            </el-form>
          </el-collapse-item>
          <el-collapse-item :title="$t('cluster.imageLibrary')" name="2">
            <el-table
              :data="jke_configData.registries"
              style="width: 100%; margin: 0 auto; max-width: 1100px"
            >
              <el-table-column
                prop="server"
                :label="$t('cluster.imageLibraryAddress')"
              />
              <el-table-column prop="username" :label="$t('form.loginName')" />
              <el-table-column prop="type" :label="$t('form.type')" />
            </el-table>
          </el-collapse-item>
          <el-collapse-item :title="$t('form.advancedConfig')" name="3">
            <el-form ref="form" :inline="true" label-width="140px">
              <div class="formItem">
                <div class="formItemTitle">
                  {{ $t("cluster.systemPlugin") }}
                </div>
                <el-form-item label="Ingress:">
                  <span>{{
                    jke_configData.system_addons &&
                    jke_configData.system_addons.ingress &&
                    jke_configData.system_addons.ingress.provider
                      ? jke_configData.system_addons.ingress.provider
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item label="http port:">
                  <span>{{
                    jke_configData.system_addons &&
                    jke_configData.system_addons.ingress &&
                    jke_configData.system_addons.ingress.http_port
                      ? jke_configData.system_addons.ingress.http_port
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item label="https port:">
                  <span>{{
                    jke_configData.system_addons &&
                    jke_configData.system_addons.ingress &&
                    jke_configData.system_addons.ingress.https_port
                      ? jke_configData.system_addons.ingress.https_port
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item label="Network mode:">
                  <span>{{
                    jke_configData.system_addons &&
                    jke_configData.system_addons.ingress &&
                    jke_configData.system_addons.ingress.network_mode
                      ? jke_configData.system_addons.ingress.network_mode
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item label="default_backend:" style="width: 100%">
                  <span>{{
                    jke_configData.system_addons &&
                    jke_configData.system_addons.ingress &&
                    jke_configData.system_addons.ingress.default_backend
                      ? $t("form.yes")
                      : $t("form.no")
                  }}</span>
                </el-form-item>
                <div style="width: 100%">
                  <el-form-item label="Monitor:">
                    <span>{{
                      jke_configData.system_addons &&
                      jke_configData.system_addons.monitor &&
                      jke_configData.system_addons.monitor.provider
                        ? jke_configData.system_addons.monitor.provider
                        : "-"
                    }}</span>
                  </el-form-item>
                  <el-form-item label="DNS:">
                    <span>{{
                      jke_configData.system_addons &&
                      jke_configData.system_addons.dns &&
                      jke_configData.system_addons.dns.provider
                        ? jke_configData.system_addons.dns.provider
                        : "-"
                    }}</span>
                  </el-form-item>
                  <el-form-item label="Network:">
                    <span>{{
                      jke_configData.system_addons &&
                      jke_configData.system_addons.network &&
                      jke_configData.system_addons.network.provider
                        ? jke_configData.system_addons.network.provider
                        : "-"
                    }}</span>
                  </el-form-item>
                  <el-form-item label="mtu:">
                    <span>{{
                      jke_configData.system_addons &&
                      jke_configData.system_addons.network &&
                      jke_configData.system_addons.network.mtu
                        ? jke_configData.system_addons.network.mtu
                        : "-"
                    }}</span>
                  </el-form-item>
                </div>
              </div>
            </el-form>
            <el-form ref="form" :inline="true" label-width="140px">
              <div class="formItem">
                <div class="formItemTitle">Etcd</div>
                <el-form-item
                  :label="$t('cluster.etcdSnapshot') + ':'"
                  style="width: 100%"
                >
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.etcd &&
                    jke_configData.services.etcd.backup_config &&
                    jke_configData.services.etcd.backup_config.enable
                      ? $t("form.yes")
                      : $t("form.no")
                  }}</span>
                </el-form-item>
                <div
                  v-if="
                    jke_configData.services &&
                    jke_configData.services.etcd &&
                    jke_configData.services.etcd.backup_config &&
                    jke_configData.services.etcd.backup_config.enable
                  "
                >
                  <el-form-item
                    :label="$t('cluster.etcdSnapshotInterval') + ':'"
                  >
                    <span>{{
                      jke_configData.services &&
                      jke_configData.services.etcd &&
                      jke_configData.services.etcd.backup_config &&
                      jke_configData.services.etcd.backup_config.interval_hours
                        ? jke_configData.services.etcd.backup_config
                            .interval_hours + "s"
                        : "-"
                    }}</span>
                  </el-form-item>
                  <el-form-item
                    :label="$t('cluster.etcdSnapshotRetention') + ':'"
                  >
                    <span>{{
                      jke_configData.services &&
                      jke_configData.services.etcd &&
                      jke_configData.services.etcd.backup_config &&
                      jke_configData.services.etcd.backup_config.retainNum
                        ? jke_configData.services.etcd.backup_config.retainNum +
                          "" +
                          jke_configData.services.etcd.backup_config.retainUnit
                        : "-"
                    }}</span>
                  </el-form-item>
                </div>
              </div>
              <div class="formItem">
                <div class="formItemTitle">KubeApi</div>
                <el-form-item
                  :label="$t('cluster.imageDownloadStrategy') + ':'"
                >
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kube_apiserver &&
                    jke_configData.services.kube_apiserver.always_pull_images
                      ? jke_configData.services.kube_apiserver
                          .always_pull_images
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item
                  :label="$t('cluster.serviceClusterIpRange') + ':'"
                >
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kube_apiserver &&
                    jke_configData.services.kube_apiserver
                      .service_cluster_ip_range
                      ? jke_configData.services.kube_apiserver
                          .service_cluster_ip_range
                      : "-"
                  }}</span>
                </el-form-item>

                <el-form-item :label="$t('cluster.nodePortRange') + ':'">
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kube_apiserver &&
                    jke_configData.services.kube_apiserver
                      .service_node_port_range
                      ? jke_configData.services.kube_apiserver
                          .service_node_port_range
                      : "-"
                  }}</span>
                </el-form-item>
              </div>
              <div class="formItem">
                <div class="formItemTitle">KubeController</div>
                <el-form-item :label="$t('cluster.clusterCidr') + ':'">
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kube_controller &&
                    jke_configData.services.kube_controller.cluster_cidr
                      ? jke_configData.services.kube_controller.cluster_cidr
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item
                  :label="$t('cluster.serviceClusterIpRange') + ':'"
                >
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kube_controller &&
                    jke_configData.services.kube_controller
                      .service_cluster_ip_range
                      ? jke_configData.services.kube_controller
                          .service_cluster_ip_range
                      : "-"
                  }}</span>
                </el-form-item>
              </div>
              <div class="formItem">
                <div class="formItemTitle">Kubelet</div>
                <el-form-item :label="$t('cluster.clusterRootDomain') + ':'">
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kubelet &&
                    jke_configData.services.kubelet.cluster_domain
                      ? jke_configData.services.kubelet.cluster_domain
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item :label="$t('cluster.dnsServiceIpAddress') + ':'">
                  <span>{{
                    jke_configData.services &&
                    jke_configData.services.kubelet &&
                    jke_configData.services.kubelet.cluster_dns_server
                      ? jke_configData.services.kubelet.cluster_dns_server
                      : "-"
                  }}</span>
                </el-form-item>
                <div style="width: 100%">
                  <el-form-item
                    :label="$t('cluster.nodeServiceCertificate') + ':'"
                  >
                    <span>{{
                      jke_configData.services &&
                      jke_configData.services.kubelet &&
                      jke_configData.services.kubelet
                        .generate_serving_certificate
                        ? $t("form.yes")
                        : $t("form.no")
                    }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('cluster.swapOpenFailed') + ':'">
                    <span>{{
                      jke_configData.services &&
                      jke_configData.services.kubelet &&
                      jke_configData.services.kubelet.fail_swap_on
                        ? $t("form.yes")
                        : $t("form.no")
                    }}</span>
                  </el-form-item>
                </div>
              </div>
            </el-form>
            <el-form ref="form" :inline="true" label-width="160px">
              <div class="formItem">
                <div class="formItemTitle">{{ $t("cluster.nodeStatus") }}</div>
                <el-form-item
                  :label="$t('cluster.unavailableWorkerNodes') + ':'"
                  style="width: 100%"
                >
                  <span>{{
                    jke_configData.upgrade_strategy &&
                    jke_configData.upgrade_strategy.max_unavailable_worker
                      ? jke_configData.upgrade_strategy.max_unavailable_worker
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item
                  :label="$t('cluster.nodePodEviction') + ':'"
                  style="width: 100%"
                >
                  <span>{{
                    jke_configData.upgrade_strategy &&
                    jke_configData.upgrade_strategy.drain
                      ? $t("form.yes")
                      : $t("form.no")
                  }}</span>
                </el-form-item>
                <div
                  v-if="
                    jke_configData.upgrade_strategy &&
                    jke_configData.upgrade_strategy.drain
                  "
                  style="width: 100%; padding-left: 120px"
                >
                  <div style="width: 100%">
                    <el-form-item
                      :label="$t('cluster.deleteEmptyDirectoryData') + ':'"
                    >
                      <span>{{
                        jke_configData.upgrade_strategy &&
                        jke_configData.upgrade_strategy.node_drain_input &&
                        jke_configData.upgrade_strategy.node_drain_input
                          .delete_local_data
                          ? $t("form.yes")
                          : $t("form.no")
                      }}</span>
                    </el-form-item>
                    <el-form-item :label="$t('cluster.force') + ':'">
                      <span>{{
                        jke_configData.upgrade_strategy &&
                        jke_configData.upgrade_strategy.node_drain_input &&
                        jke_configData.upgrade_strategy.node_drain_input.force
                          ? $t("form.yes")
                          : $t("form.no")
                      }}</span>
                    </el-form-item>
                  </div>
                  <el-form-item
                    :label="$t('cluster.podSelfTerminationGracePeriod') + ':'"
                    style="width: 100%"
                  >
                    <span>{{
                      jke_configData.upgrade_strategy &&
                      jke_configData.upgrade_strategy.node_drain_input &&
                      jke_configData.upgrade_strategy.node_drain_input
                        .grace_period
                        ? jke_configData.upgrade_strategy.node_drain_input
                            .grace_period + "s"
                        : $t("cluster.usePodDefaultValues")
                    }}</span>
                  </el-form-item>
                  <el-form-item
                    :label="$t('cluster.evictionTimeout') + ':'"
                    style="width: 100%"
                  >
                    <span>{{
                      jke_configData.upgrade_strategy &&
                      jke_configData.upgrade_strategy.node_drain_input &&
                      jke_configData.upgrade_strategy.node_drain_input.timeout
                        ? jke_configData.upgrade_strategy.node_drain_input
                            .timeout + "s"
                        : $t("cluster.retry")
                    }}</span>
                  </el-form-item>
                </div>
              </div>
              <div class="formItem">
                <div class="formItemTitle">
                  {{ $t("cluster.deploymentSettings") }}
                </div>
                <el-form-item
                  :label="$t('cluster.deploymentContainerTimeout') + ':'"
                  style="width: 100%"
                >
                  <span
                    >{{ jke_configData.deploy_job_time || "0" }}
                    <span> s</span></span
                  >
                </el-form-item>
              </div>
            </el-form>
          </el-collapse-item>
        </el-collapse>
        <!-- 集群选项 导入 -->
      </div>
    </el-card>
  </div>
</template>

<script>
var yaml = require("js-yaml");
import Clipboard from "clipboard";
import { k8stemplates, templatesVersions } from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: { YamlEditor },

  props: ["detailMain", "jke_configData", "yamlEditorName"],
  data() {
    return {
      // 模板名称
      template_name: "",
      infoFromVersionList: [],
      // 控制显示字段
      status: 0,
      clusterCollapse: "1", // 默认展开第一个
      infoFrom: {
        // 集群选项基本信息
        isUse: true, // 是否使用模板
        version: [], // 版本选择
        create_type: "custom", // custom:用户自定义 import：导入
        stack_compose: "",
      },
    };
  },
  watch: {
    "infoFrom.create_type": {
      deep: true,
      handler(val) {
        this.infoFrom.stack_compose = yaml.dump(this.jke_configData);

        this.$refs.yamlEditor.toRefresh();
      },
    },
  },
  mounted() {},
  created() {
    this.getK8stemplates(); // 获取模板列表
  },
  methods: {
    // 请求版本模板名称
    getTemplatesName() {
      this.template_name = "";
      if (
        this.detailMain.tmpl_ver_id != null &&
        this.detailMain.tmpl_ver_id != undefined &&
        this.detailMain.tmpl_ver_id != ""
      ) {
        // 请求模板详情
        templatesVersions(this.detailMain.tmpl_ver_id).then((res) => {
          this.template_name = res.template_name;
        });
      } else {
        this.template_name = "-";
      }
    },
    getK8stemplates() {
      k8stemplates({ cluster_type: "k8s" }).then((res) => {
        // 获取模板列表
        this.infoFromVersionList = [];
        res.templates.map((item) => {
          if (item.versions != null && item.versions.length > 0) {
            item.versions.map((item1) => {
              this.infoFromVersionList.push({
                id: item1.id,
                version: item1.version,
              });
            });
          }
        });
        this.getTemplatesName();
      });
    },
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          title: this.$t("message.copySuccess"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.on("error", () => {
        // 不支持复制
        this.$notify({
          title: this.$t("message.copyError"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
  },
};
</script>

<style lang="scss" scoped>
.infoShowPage {
  ::v-deep .el-collapse {
    .el-collapse-item__header {
      height: 40px;
      line-height: 40px;
      padding-left: 10px;
    }
  }
}

span {
  font-size: 14px;
  width: 200px;
  display: flex;
  justify-content: start;
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
