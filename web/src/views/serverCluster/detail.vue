<template>
  <div>
    <!-- 集群信息 -->
    <el-card class="box-card" shadow="never" style="position: relative">
      <div style="position: absolute; top: 0; right: 40px">
        <el-tooltip
          class="item"
          effect="dark"
          :content="$t('cluster.exportClusterTemplate')"
          placement="bottom-start"
        >
          <el-button
            type="primary"
            size="mini"
            :disabled="detailMain.create_type != 'custom'"
            @click="clickExportBtn(detailMain)"
          >
            <svg
              t="1662014790374"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1925"
              width="14"
              height="14"
            >
              <path
                d="M469.333333 162.133333h85.333334v469.333334c0 12.8-8.533333 21.333333-21.333334 21.333333h-42.666666c-12.8 0-21.333333-8.533333-21.333334-21.333333v-469.333334z"
                fill="#ffffff"
                p-id="1926"
              />
              <path
                d="M315.733333 392.533333L285.866667 362.666667c-8.533333-8.533333-8.533333-21.333333 0-29.866667l211.2-211.2c8.533333-8.533333 21.333333-8.533333 29.866666 0l44.8 44.8-226.133333 226.133333c-8.533333 8.533333-21.333333 8.533333-29.866667 0z"
                fill="#ffffff"
                p-id="1927"
              />
              <path
                d="M452.266667 166.4l44.8-44.8c8.533333-8.533333 21.333333-8.533333 29.866666 0l211.2 211.2c8.533333 8.533333 8.533333 21.333333 0 29.866667l-29.866666 29.866666c-8.533333 8.533333-21.333333 8.533333-29.866667 0L452.266667 166.4zM896 503.466667h-42.666667c-12.8 0-21.333333 8.533333-21.333333 21.333333v277.333333c0 12.8-8.533333 21.333333-21.333333 21.333334H213.333333c-12.8 0-21.333333-8.533333-21.333333-21.333334v-277.333333c0-12.8-8.533333-21.333333-21.333333-21.333333H128c-12.8 0-21.333333 8.533333-21.333333 21.333333v362.666667c0 12.8 8.533333 21.333333 21.333333 21.333333h768c12.8 0 21.333333-8.533333 21.333333-21.333333v-362.666667c0-12.8-8.533333-21.333333-21.333333-21.333333z"
                fill="#ffffff"
                p-id="1928"
              />
              <path
                d="M277.333333 588.8H149.333333v-85.333333h128c12.8 0 21.333333 8.533333 21.333334 21.333333v42.666667c0 10.666667-8.533333 21.333333-21.333334 21.333333zM874.666667 588.8h-128c-12.8 0-21.333333-8.533333-21.333334-21.333333v-42.666667c0-12.8 8.533333-21.333333 21.333334-21.333333h128v85.333333z"
                fill="#ffffff"
                p-id="1929"
              />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          content="Dashboard"
          placement="bottom-start"
        >
          <el-link
            :underline="false"
            :href="detailMain.dashboard_url"
            :disabled="detailMain.online_state != 1"
          >
            <el-button
              :disabled="
                detailMain.online_state != 1 || detailMain.status.code == 0
              "
              type="primary"
              size="mini"
              class="mx-2"
            >
              <svg
                t="1662016080580"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="2668"
                width="14"
                height="14"
              >
                <path
                  d="M924.8 385.6c-22.6-53.4-54.9-101.3-96-142.4-41.1-41.1-89-73.4-142.4-96C631.1 123.8 572.5 112 512 112s-119.1 11.8-174.4 35.2c-53.4 22.6-101.3 54.9-142.4 96-41.1 41.1-73.4 89-96 142.4C75.8 440.9 64 499.5 64 560c0 132.7 58.3 257.7 159.9 343.1l1.7 1.4c5.8 4.8 13.1 7.5 20.6 7.5h531.7c7.5 0 14.8-2.7 20.6-7.5l1.7-1.4C901.7 817.7 960 692.7 960 560c0-60.5-11.9-119.1-35.2-174.4zM761.4 836H262.6C184.5 765.5 140 665.6 140 560c0-99.4 38.7-192.8 109-263 70.3-70.3 163.7-109 263-109 99.4 0 192.8 38.7 263 109 70.3 70.3 109 163.7 109 263 0 105.6-44.5 205.5-122.6 276z"
                  fill="#ffffff"
                  p-id="2669"
                />
                <path
                  d="M623.5 421.5c-3.1-3.1-8.2-3.1-11.3 0L527.7 506c-18.7-5-39.4-0.2-54.1 14.5-21.9 21.9-21.9 57.3 0 79.2 21.9 21.9 57.3 21.9 79.2 0 14.7-14.7 19.5-35.4 14.5-54.1l84.5-84.5c3.1-3.1 3.1-8.2 0-11.3l-28.3-28.3zM490 320h44c4.4 0 8-3.6 8-8v-80c0-4.4-3.6-8-8-8h-44c-4.4 0-8 3.6-8 8v80c0 4.4 3.6 8 8 8zM750 538v44c0 4.4 3.6 8 8 8h80c4.4 0 8-3.6 8-8v-44c0-4.4-3.6-8-8-8h-80c-4.4 0-8 3.6-8 8zM762.7 340.8l-31.1-31.1c-3.1-3.1-8.2-3.1-11.3 0l-56.6 56.6c-3.1 3.1-3.1 8.2 0 11.3l31.1 31.1c3.1 3.1 8.2 3.1 11.3 0l56.6-56.6c3.1-3.1 3.1-8.2 0-11.3zM304.1 309.7c-3.1-3.1-8.2-3.1-11.3 0l-31.1 31.1c-3.1 3.1-3.1 8.2 0 11.3l56.6 56.6c3.1 3.1 8.2 3.1 11.3 0l31.1-31.1c3.1-3.1 3.1-8.2 0-11.3l-56.6-56.6zM262 530h-80c-4.4 0-8 3.6-8 8v44c0 4.4 3.6 8 8 8h80c4.4 0 8-3.6 8-8v-44c0-4.4-3.6-8-8-8z"
                  fill="#ffffff"
                  p-id="2670"
                />
              </svg>
            </el-button>
          </el-link>
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          content="kubeconfig"
          placement="bottom-start"
        >
          <el-button
            type="primary"
            size="mini"
            :disabled="
              detailMain.status.code != 8 &&
              detailMain.status.code != 9 &&
              detailMain.status.code != 10
            "
            @click="clickkubeconfigBtn(detailMain)"
          >
            <svg
              t="1662018589150"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="4636"
              width="14"
              height="14"
            >
              <path
                d="M928 96v635.008h-832V96h832zM186.368 185.6v455.232h651.264V185.6H186.368z m481.28 648.32v90.24h-315.52v-90.24h315.52zM310.848 542.464l-63.936-63.68 193.344-192.832L569.344 414.72l129.408-129.088 63.936 63.744L569.344 542.08 440.192 413.44 310.784 542.464z"
                fill="#ffffff"
                p-id="4637"
              />
            </svg>
          </el-button>
        </el-tooltip>
        <el-tooltip
          class="item"
          effect="dark"
          content="kubectl"
          placement="bottom-start"
        >
          <el-button
            type="primary"
            size="mini"
            :disabled="detailMain.online_state != 1"
            @click="clickkubectlBtn(detailMain)"
          >
            <svg
              t="1662018665323"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1643"
              width="14"
              height="14"
            >
              <path
                d="M917.504 835.584H106.496V188.416h810.496l0.512 647.168zM186.88 755.2h650.752v-486.4H186.88v486.4z"
                fill="#ffffff"
                p-id="1644"
              />
              <path
                d="M343.04 648.704l-56.32-56.32 88.064-88.064L286.72 415.744l56.32-56.832 144.896 144.896L343.04 648.704z m163.84-63.488h230.4v79.872H506.88v-79.872z"
                fill="#ffffff"
                p-id="1645"
              />
            </svg>
          </el-button>
        </el-tooltip>
      </div>

      <div v-if="detailMain" class="text item">
        <!-- 集群信息 -->
        <el-form ref="form" label-width="140px" :inline="true">
          <el-form-item
            :label="$t('form.name') + ':'"
            style="width: 50%; margin-right: 0px"
          >
            <span>{{ detailMain.name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.deploymentStatus') + ':'"
            style="width: 50%; margin-right: 0px"
          >
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getClusterStatus(detailMain.status.code, 'tag')"
              >{{
                filtersFun.getClusterStatus(detailMain.status.code, "status")
              }}</el-tag
            >
          </el-form-item>
          <el-form-item :label="$t('form.desc') + ':'" style="width: 100%">
            <span>{{ detailMain.desc || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.bp') + ':'"
            style="width: 50%; margin-right: 0px"
          >
            <span>{{ detailMain.bp_name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.owner') + ':'"
            style="width: 50%; margin-right: 0px"
          >
            <span>{{ detailMain.owner_name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; margin-right: 0px"
          >
            <span>{{ detailMain.create_time || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.updateTime') + ':'"
            style="width: 50%; margin-right: 0px"
          >
            <span>{{ detailMain.update_time || "-" }}</span>
          </el-form-item>

          <el-form-item
            v-if="detailMain.create_type == 'custom'"
            :label="$t('form.status') + ':'"
            style="width: 100%"
          >
            <el-tag
              v-for="(item, index) in coreServiceMetricsData"
              :key="index"
              :type="item.running_num > 0 ? 'success' : 'danger'"
              size="mini"
              class="mx-0.5"
            >
              <i v-if="item.running_num > 0" class="el-icon-check" />
              <i v-else class="el-icon-close" />
              {{ item.service_name }}
            </el-tag>
          </el-form-item>
        </el-form>
        <div style="margin-top: 20px">
          <el-tabs
            v-if="detailMain.create_type != 'import'"
            v-model="activeName"
          >
            <el-tab-pane
              v-if="detailMain.cluster_type == 'k8s'"
              :label="$t('cluster.clusterEngineParams')"
              name="first"
            >
              <info-show
                :yaml-editor-name="detailMain.name"
                :jke-config-data="detailMain.jke_config"
                :detail-main="detailMain"
              />
            </el-tab-pane>
            <el-tab-pane
              v-if="detailMain.cluster_type == 'k3s'"
              :label="$t('cluster.clusterEngineParams')"
              name="first"
            >
              <K3SinfoShowVue
                :yaml-editor-name="detailMain.name"
                :k3s-config-data="detailMain.k3s_config"
                :detail-main="detailMain"
              />
            </el-tab-pane>
            <el-tab-pane
              :label="$t('cluster.templateExportRecord')"
              name="second"
            >
              <el-table
                ref="multipleTable"
                :data="templatesInfoData"
                stripe
                tooltip-effect="dark"
                style="width: 100%"
              >
                <el-table-column
                  prop="template_name"
                  :label="$t('cluster.templateName')"
                />
                <el-table-column
                  prop="versions"
                  :label="$t('cluster.versionNumber')"
                >
                  <template slot-scope="scope">
                    <a
                      style="color: #409eff"
                      @click="versionInformation(scope.row)"
                    >
                      {{
                        scope.row.versions && scope.row.versions.version
                          ? scope.row.versions.version
                          : "-"
                      }}
                    </a>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane
              v-if="detailMain.create_type == 'custom'"
              :label="$t('cluster.clusterNode')"
              name="third"
            >
              <el-table
                ref="multipleTable"
                :data="tableData.nodes"
                stripe
                tooltip-effect="dark"
                style="width: 100%"
              >
                <el-table-column prop="name" :label="$t('form.name')">
                  <template slot-scope="scope">
                    <span>{{ scope.row.node_name }}</span>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.status')">
                  <template slot-scope="scope">
                    <div v-if="detailMain.cluster_type == 'k8s'">
                      <el-tag
                        v-for="(item, index) in scope.row.core_services"
                        v-show="'kubelet' == item.service_name"
                        :key="index"
                        size="mini"
                        class="mx-0.5"
                        :type="
                          item.status.desc.en == 'running'
                            ? 'success'
                            : 'danger'
                        "
                      >
                        <i
                          v-if="item.status.desc.en == 'running'"
                          class="el-icon-check"
                        />
                        <i v-else class="el-icon-close" />
                        {{ item.service_name }}
                      </el-tag>
                    </div>
                    <div v-if="detailMain.cluster_type == 'k3s'">
                      <div v-if="scope.row.core_services != null">
                        <el-tag
                          size="mini"
                          class="mx-0.5"
                          :type="
                            scope.row.core_services[0].status.desc.en ==
                            'running'
                              ? 'success'
                              : 'danger'
                          "
                        >
                          <i
                            v-if="
                              scope.row.core_services[0].status.desc.en ==
                              'running'
                            "
                            class="el-icon-check"
                          />
                          <i v-else class="el-icon-close" />
                          {{ "kubelet" }}
                        </el-tag>
                      </div>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="external_address"
                  :label="$t('form.networkAddress')"
                >
                  <template slot-scope="scope">
                    <span class="block">
                      {{ $t("cluster.publicNetwork") }}:{{
                        scope.row.external_address || "-"
                      }}
                    </span>
                    <span class="block">
                      {{ $t("cluster.privateNetwork") }}:{{
                        scope.row.internal_address || "-"
                      }}
                    </span>
                  </template>
                </el-table-column>

                <el-table-column
                  prop="region_name"
                  :label="$t('form.regionSite')"
                >
                  <template slot-scope="scope">
                    <span
                      >{{ scope.row.region_name }}/{{
                        scope.row.site_name
                      }}</span
                    >
                  </template>
                </el-table-column>

                <el-table-column
                  prop="cluster_roles"
                  :label="$t('form.role')"
                />
                <el-table-column
                  prop="docker_version"
                  :label="$t('form.dockerVersion')"
                />

                <el-table-column :label="$t('form.operation')" width="100">
                  <template slot-scope="scope">
                    <div class="czlb">
                      <el-popover
                        placement="bottom"
                        width="110"
                        trigger="click"
                      >
                        <div
                          class="icon_cz"
                          @click="clickContinueBtn(scope.row)"
                        >
                          <i class="el-icon-video-play" />
                          {{ $t("cluster.continueExecution") }}
                        </div>
                        <div
                          class="icon_cz"
                          @click="clickRefreshBtn(scope.row)"
                        >
                          <i class="el-icon-refresh-right" />
                          {{ $t("cluster.reinstall") }}
                        </div>

                        <el-button
                          slot="reference"
                          icon="el-icon-more"
                          class="czbtn right_czbtn"
                          :disabled="scope.row.status.code != 6"
                        />
                      </el-popover>
                    </div>
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
        </div>
      </div>
    </el-card>
    <el-card
      v-if="
        detailMain.create_type == 'import' &&
        clustersImportCmdsDesc &&
        detailMain.status.code == 0
      "
      class="box-card mt-2 mb-2"
      shadow="never"
    >
      <div>
        <el-row class="px-8 pb-4">
          <el-col class="mb-6">
            <p class="text-sm">{{ $t("cluster.TipImportCluster") }}:</p>
            <span
              class="inline-block pl-4 pr-10 text-white bg-black rounded relative text-sm leading-8"
            >
              {{ clustersImportCmdsDesc.command }}
              <span
                class="absolute top-0 right-0 w-6 block h-full border-l text-center cursor-pointer bg-blue-500 rounded-r"
              >
                <i
                  class="el-icon-copy-document"
                  @click="copy($event, clustersImportCmdsDesc.command)"
                />
              </span>
            </span>
          </el-col>
          <el-col>
            <p class="text-sm">{{ $t("cluster.TipImportCluster2") }}:</p>
            <span
              class="inline-block pl-4 pr-10 text-white bg-black rounded relative text-sm leading-8"
            >
              {{ clustersImportCmdsDesc.insecureCommand }}
              <span
                class="absolute top-0 right-0 w-6 block h-full border-l text-center cursor-pointer bg-blue-500 rounded-r"
              >
                <i
                  class="el-icon-copy-document"
                  @click="copy($event, clustersImportCmdsDesc.insecureCommand)"
                />
              </span>
            </span>
          </el-col>
        </el-row>
      </div>
    </el-card>
    <el-dialog
      :title="$t('cluster.exportTemplate')"
      :before-close="handleClosetemplate"
      :visible.sync="dialogFormVisible"
      width="800px"
    >
      <el-form ref="templateFormref" :model="templateFrom" label-width="80px">
        <el-form-item
          :label="$t('cluster.templateName') + ':'"
          prop="template_name"
          :rules="[
            {
              required: true,
              validator: validate.k8sName,
              trigger: ['blur', 'change'],
            },
          ]"
        >
          <el-input v-model="templateFrom.template_name" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('cluster.templateDesc') + ':'">
          <el-input
            v-model="templateFrom.template_desc"
            type="textarea"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('form.version') + ':'">
          <el-input
            v-model="templateFrom.template_version"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.overwrite') + ':'"
          class="formContentBlock"
        >
          <el-switch v-model="templateFrom.overwrite" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" type="text" @click="handleClosetemplate">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toExportBtn()"
        >
          {{ $t("form.export") }}</el-button
        >
      </div>
    </el-dialog>
    <el-drawer
      title="kubeconfig"
      :visible.sync="dialogkubeconfigVisible"
      direction="btt"
      :wrapper-closable="false"
      size="50%"
      :modal="false"
    >
      <p class="px-4">
        {{ $t("cluster.KubeConfig") }}
      </p>
      <yaml-editor
        v-model="kubeConfigDesc"
        :download-name="detailMain.name + '-kubeconfig'"
        :download-type="'yml'"
        style="margin-top: 12px; min-width: 100%"
        :placeholder="''"
        :is-add="false"
        :readonly="'cursor'"
      />
    </el-drawer>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import filtersFun from "@/utils/statusFun";

import {
  exportClusterTemplates,
  getClustersDetail,
  getClustersImportCmds,
  coreServiceMetrics,
  getClustersNodes,
  kubeconfig,
  getClustersTerminal,
  clustersRepair,
} from "@/api/mainApi";
import Clipboard from "clipboard";
import infoShow from "./components/infoShow";
import K3SinfoShowVue from "./components/K3SinfoShow.vue";
import YamlEditor from "@/components/yaml/editor.vue";
import validate from "@/utils/validate";
var termm = require("@/utils/term.js");

export default {
  components: {
    infoShow,
    YamlEditor,
    K3SinfoShowVue,
  },
  data() {
    return {
      filtersFun: filtersFun,
      validate: validate,
      loading: false,
      clustersImportCmdsDesc: "", // 命令信息
      kubeConfigDesc: "",
      dialogkubeconfigVisible: false, // kubeconfig导出弹框
      coreServiceMetricsData: [], // 集群节点监控数据
      nowClsData: "", // 当前选中的集群
      dialogFormVisible: false, // 导出模板弹窗
      templateFrom: {
        template_name: "",
        template_desc: "",
        template_version: "v1",
        overwrite: false,
      },
      activeName: "first", // 标签页
      tabsAndNotes: "1", // 折叠面板
      detailMain: "", // 详情内容
      tableData: [], // 节点列表
      templatesInfoData: "", // 模板信息
      queryData: {
        cluster_id: "", // 集群id
        page_size: 10,
        page_num: 1,
      },
      // 模板名称
      template_name: "",
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  watch: {
    dialogFormVisible(val) {
      if (!val) {
        this.nowClsData = "";
      }
    },
  },
  mounted() {
    this.$route.meta.smalltitle =
      this.$t("cluster.cluster") + "：" + this.$route.params.name;
    this.queryData.cluster_id = this.$route.params.id;
    this.getDetail(this.$route.params.id);
    this.getCoreServiceMetrics(this.$route.params.id);
  },
  created() {},

  methods: {
    handleClosetemplate() {
      this.dialogFormVisible = false;
      (this.templateFrom = {
        template_name: "",
        template_desc: "",
        template_version: "v1",
        overwrite: false,
      }),
        this.$refs["templateFormref"].resetFields();
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
          title: this.$t("message.copyFailed"),
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
    // 导出模板
    clickExportBtn(item) {
      this.nowClsData = item;
      this.dialogFormVisible = true;
    },
    toExportBtn() {
      // 导出模板
      this.$refs["templateFormref"].validate((valid) => {
        if (valid) {
          this.loading = true;
          exportClusterTemplates(this.nowClsData.id, this.templateFrom)
            .then((res) => {
              this.$notify({
                title: this.$t("message.exportSuccess"),
                type: "success",
                duration: 2500,
              });
              this.templateFrom = {
                template_name: "",
                template_desc: "",
                template_version: "v1",
                overwrite: false,
              };
              this.dialogFormVisible = false;
              this.getDetail(this.$route.params.id);
              this.loading = false;
            })
            .catch((err) => {
              console.error(err.response.data.message);
              this.loading = false;
            });
        }
      });
    },
    // 带参数跳转到模板版本信息界面
    versionInformation(message) {
      this.$router.push(
        "/serverCluster/versionInformation/" +
          this.detailMain.cluster_type +
          "/" +
          message.versions.version_id
      );
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.getNodes();
    },

    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.getNodes();
    },
    // 集群节点数据
    async getNodes() {
      await getClustersNodes(this.queryData, this.queryData.cluster_id).then(
        (res) => {
          this.tableData = res;
        }
      );
    },
    getCoreServiceMetrics(id) {
      coreServiceMetrics(id).then((res) => {
        this.coreServiceMetricsData = res;
      });
    },
    getDetail(id) {
      // 获取详情
      getClustersDetail(id).then((res) => {
        if (res.id) {
          this.detailMain = res;
          this.getNodes();
          this.templatesInfoData = [];
          if (this.detailMain.templates.length > 0) {
            for (var i = 0; i < this.detailMain.templates.length; i++) {
              if (this.detailMain.templates[i].versions.length > 1) {
                for (
                  var j = 0;
                  j < this.detailMain.templates[i].versions.length;
                  j++
                ) {
                  this.templatesInfoData.push({
                    template_name: this.detailMain.templates[i].template_name,
                    versions: this.detailMain.templates[i].versions[j],
                  });
                }
              } else {
                this.templatesInfoData.push({
                  template_name: this.detailMain.templates[i].template_name,
                  versions: this.detailMain.templates[i].versions[0],
                });
              }
            }
          }
        }
        if (res.create_type == "import") {
          getClustersImportCmds(id).then((res) => {
            this.clustersImportCmdsDesc = res;
          });
        }
      });
    },
    // 获取集群kubeconfig信息
    async clickkubeconfigBtn(item) {
      if (item.id != "" && item.id != undefined) {
        await kubeconfig(item.id)
          .then((res) => {
            this.dialogkubeconfigVisible = true;
            this.kubeConfigDesc = res;
          })
          .catch((err) => {});
      }
    },
    clickkubectlBtn(item) {
      getClustersTerminal(item.id).then((res) => {
        if (res.status == "running") {
          // 打开新页面
          window.open(res.dashboard_url);
        } else {
          this.$notify({
            title: this.$t("cluster.environmentPreparing"),
            type: "warning",
            duration: 2500,
          });
        }
      });
    },
    clickContinueBtn(value) {
      this.$confirm(
        this.$t("cluster.continueExecutionNode"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          clustersRepair(this.queryData.cluster_id, {
            type: "continue",
            node_ids: [value.node_id],
          })
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getNodes();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickRefreshBtn(value) {
      this.$confirm(
        this.$t("cluster.reinstallNode"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          clustersRepair(this.queryData.cluster_id, {
            type: "redeploy-node",
            node_ids: [value.node_id],
          })
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getNodes();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-link {
  vertical-align: inherit;
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

  &.collapseStyle {
    ::v-deep .el-collapse-item__arrow {
      margin: 0 8px 0 0;
    }
  }
}

.formItem {
  padding: 10px 20px;

  .formItemTitle {
    font-weight: bold;
    font-size: 16px;
    padding-bottom: 10px;
  }

  .formSmallItem {
    padding: 10px 20px;
    width: 50%;
    float: left;
    height: 180px;
    min-width: 400px;
    max-width: 600px;

    .formSmallItemTitle {
      font-weight: bold;
      font-size: 14px;
      padding-bottom: 10px;
    }
  }
}

::v-deep .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
  z-index: 1;
}

::v-deep .el-collapse-item__header {
  padding-left: 10px;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}
</style>
