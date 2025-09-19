<template>
  <div class="warpMain">
    <div
      class="mb-4"
      style="display: flex; justify-content: space-between; align-items: center"
    >
      <div :style="{ width: cardWidth + '%' }">
        <el-card class="flex" shadow="never">
          <span class="flex">
            <div class="circle relative">
              <div style="position: absolute; top: 5px; left: 11px">
                <svg
                  t="1695352626013"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="13020"
                  width="14"
                  height="14"
                >
                  <path
                    d="M384 0l256 0 0 960-256 0 0-960Z"
                    p-id="13021"
                    fill="#36429B"
                  />
                  <path
                    d="M704 256l256 0 0 704-256 0 0-704Z"
                    p-id="13022"
                    fill="#36429B"
                  />
                  <path
                    d="M64 448l256 0 0 512-256 0 0-512Z"
                    p-id="13023"
                    fill="#36429B"
                  />
                </svg>
              </div>
            </div>
            <div class="ml-2" style="margin-top: -8px">
              <div>
                <span
                  style="font-size: 22px; font-weight: 600; margin-right: 2px"
                  >{{ nodeMain.total_num ? nodeMain.total_num : 0 }}</span
                >
                <span style="font-size: 14px">{{ $t("unit.PCS") }}</span>
              </div>
              <div style="font-size: 14px; color: #9ea3ab">
                {{ $t("form.totalDevice") }}
              </div>
            </div>
          </span>
        </el-card>
      </div>
      <div :style="{ width: cardWidth + '%' }" style="margin-left: 1%">
        <el-card class="box-card" shadow="never">
          <span class="flex">
            <div class="circleline relative">
              <div style="position: absolute; top: 5px; left: 11px">
                <svg
                  t="1695352626013"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="13020"
                  width="14"
                  height="14"
                >
                  <path
                    d="M384 0l256 0 0 960-256 0 0-960Z"
                    p-id="13021"
                    fill="#4CA73F"
                  />
                  <path
                    d="M704 256l256 0 0 704-256 0 0-704Z"
                    p-id="13022"
                    fill="#4CA73F"
                  />
                  <path
                    d="M64 448l256 0 0 512-256 0 0-512Z"
                    p-id="13023"
                    fill="#4CA73F"
                  />
                </svg>
              </div>
            </div>
            <div class="ml-2" style="margin-top: -10px">
              <div>
                <span
                  style="font-size: 21px; font-weight: 600; margin-right: 2px"
                  >{{ nodeMain.online ? nodeMain.online : 0 }}</span
                >
                <span style="font-size: 14px">{{ $t("unit.PCS") }}</span>
              </div>
              <div style="font-size: 14px; color: #9ea3ab">
                {{ $t("form.onlineDevice") }}
              </div>
            </div>
          </span>
        </el-card>
      </div>
      <div :style="{ width: cardWidth + '%' }" style="margin-left: 1%">
        <el-card class="box-card" shadow="never">
          <span class="flex">
            <div class="circleoffline relative">
              <div style="position: absolute; top: 5px; left: 11px">
                <svg
                  t="1695352626013"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="13020"
                  width="14"
                  height="14"
                >
                  <path
                    d="M384 0l256 0 0 960-256 0 0-960Z"
                    p-id="13021"
                    fill="#EC9B37"
                  />
                  <path
                    d="M704 256l256 0 0 704-256 0 0-704Z"
                    p-id="13022"
                    fill="#EC9B37"
                  />
                  <path
                    d="M64 448l256 0 0 512-256 0 0-512Z"
                    p-id="13023"
                    fill="#EC9B37"
                  />
                </svg>
              </div>
            </div>
            <div class="ml-2" style="margin-top: -8px">
              <div>
                <span
                  style="font-size: 22px; font-weight: 600; margin-right: 2px"
                  >{{ nodeMain.offline ? nodeMain.offline : 0 }}</span
                >
                <span style="font-size: 14px">{{ $t("unit.PCS") }}</span>
              </div>
              <div style="font-size: 14px; color: #9ea3ab">
                {{ $t("form.offlineDevice") }}
              </div>
            </div>
          </span>
        </el-card>
      </div>
      <div :style="{ width: cardWidth + '%' }" style="margin-left: 1%">
        <el-card class="box-card" shadow="never">
          <span class="flex">
            <div class="circleAcitve relative">
              <div style="position: absolute; top: 5px; left: 11px">
                <svg
                  t="1695352626013"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="13020"
                  width="14"
                  height="14"
                >
                  <path
                    d="M384 0l256 0 0 960-256 0 0-960Z"
                    p-id="13021"
                    fill="#505968"
                  />
                  <path
                    d="M704 256l256 0 0 704-256 0 0-704Z"
                    p-id="13022"
                    fill="#505968"
                  />
                  <path
                    d="M64 448l256 0 0 512-256 0 0-512Z"
                    p-id="13023"
                    fill="#505968"
                  />
                </svg>
              </div>
            </div>
            <div class="ml-2" style="margin-top: -8px">
              <div>
                <span
                  style="font-size: 22px; font-weight: 600; margin-right: 2px"
                  >{{ nodeMain.deactive ? nodeMain.deactive : 0 }}</span
                >
                <span style="font-size: 14px">{{ $t("unit.PCS") }}</span>
              </div>
              <div style="font-size: 14px; color: #9ea3ab">
                {{ $t("form.unactivatedDevice") }}
              </div>
            </div>
          </span>
        </el-card>
      </div>
    </div>
    <div>
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.node") + ":" }}</div>
          <el-input
            v-model="queryData.name"
            style="width: 125px"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.region") + ":" }}</div>
          <el-select
            v-model="queryData.region"
            style="width: 100px"
            :placeholder="$t('form.pleaseSelect')"
            filterable
            clearable
            @change="handleRegionChange"
          >
            <el-option
              v-for="(item, index) in arealist"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.site") + ":" }}</div>
          <el-select
            v-model="queryData.site"
            style="width: 100px"
            :placeholder="$t('form.pleaseSelect')"
            filterable
            clearable
            @change="handleWebsiteChange"
          >
            <el-option
              v-for="(item, index) in website"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.status") + ":" }}</div>
          <el-select
            v-model="queryData.online_status"
            style="width: 100px"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            @change="handleOnlineStatusChange"
          >
            <el-option :value="1" :label="$t('form.online')" />
            <el-option :value="0" :label="$t('form.offline')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.activation") + ":" }}</div>
          <el-select
            v-model="queryData.active_status"
            style="width: 100px"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            @change="handleActiveStatusChange"
          >
            <el-option :value="0" :label="$t('form.notActivated')" />
            <el-option :value="1" :label="$t('form.activated')" />
            <el-option :value="2" :label="$t('form.upgrading')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            class="add_search"
            type="primary"
            @click="handleCurrentChange(1)"
            >{{ $t("form.query") }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-button
          style="float: right; margin-bottom: 20px"
          size="small"
          type="primary"
          @click="clickAddBtn"
          >{{ $t("form.add") }}</el-button
        >
        <el-button
          style="float: right; margin-bottom: 20px; margin-right: 15px"
          size="small"
          type="primary"
          :disabled="disabled"
          @click="clickUpgradeBtn"
          >{{ $t("form.upgrade") }}</el-button
        >
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.nodes"
      stripe
      :row-key="(row) => row.node_id"
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="45" :reserve-selection="true" />
      <el-table-column
        prop="node_name"
        :label="$t('form.nodeName')"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <div
            style="
              text-overflow: ellipsis;
              white-space: nowrap;
              word-wrap: normal;
              overflow: hidden;
            "
          >
            <router-link
              style="color: #409eff"
              :to="'/infrastructure/nodeList/' + scope.row.node_id"
            >
              <el-tooltip
                v-if="scope.row.online_status == 1"
                effect="dark"
                :content="$t('form.online')"
                placement="top-end"
              >
                <img
                  src="@/assets/svg/wifion.svg"
                  alt=""
                  width="15px"
                  style="display: inline-block"
                />
              </el-tooltip>
              <el-tooltip
                v-if="scope.row.online_status == 0"
                effect="dark"
                :content="$t('form.offline')"
                placement="top-end"
              >
                <img
                  src="@/assets/svg/wifioff.svg"
                  alt=""
                  width="15px"
                  style="display: inline-block"
                />
              </el-tooltip>

              <span> {{ scope.row.node_name }}</span>
            </router-link>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="labels" :label="$t('form.type')">
        <template slot-scope="scope">
          {{ scope.row.labels["com.justice.node/orchestration"] || "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="core_version" :label="$t('form.version')">
        <template slot-scope="scope">
          {{ scope.row.core_version || "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="bp_name" :label="$t('form.organization')">
        <template slot-scope="scope">
          {{ scope.row.bp_name || "-" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="region_name"
        :label="$t('form.region') + '/' + $t('form.site')"
      >
        <template slot-scope="scope">
          {{ scope.row.region_name || "-" }}/{{ scope.row.site_name || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.activation')">
        <template slot-scope="scope">
          <div v-if="scope.row.active_status == 0" class="error">
            <span>{{ $t("form.notActivated") }}</span>
          </div>
          <div v-if="scope.row.active_status == 1" class="success">
            <span>{{ $t("form.activated") }}</span>
          </div>
          <div v-if="scope.row.active_status == 2" class="upgrade">
            <span>{{ $t("form.upgrading") }}</span>
            <p>
              {{ $getStatus.getNodeUpgradeStatus(scope.row.upgrade_status) }}
            </p>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="130">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.active_status == 2 && scope.row.upgrade_status == 4"
            type="primary"
            size="mini"
            style="margin-bottom: 4px"
            @click="confirmUpgradeEnd(scope.row)"
            >{{ $t("form.confirmUpgrade") }}</el-button
          >
          <el-button
            v-if="scope.row.active_status == 2 && scope.row.upgrade_status == 4"
            type="warning"
            size="mini"
            @click="UpgradeBack(scope.row)"
            >{{ $t("form.rollBack") }}</el-button
          >
          <div v-if="scope.row.active_status != 2" class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <router-link
                style="color: #409eff"
                :to="
                  '/mirror/imageFromNode/' +
                  scope.row.node_id +
                  '/' +
                  scope.row.node_name
                "
              >
                <div class="icon_cz">
                  <i class="el-icon-tickets" />
                  {{ $t("form.imageList") }}
                </div>
              </router-link>

              <div class="icon_cz" @click="clickDelBtn(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>
              <div
                v-if="scope.row.active_status == 1"
                class="icon_cz"
                @click="clickdeactiveBtn(scope.row)"
              >
                <i class="el-icon-video-pause" />
                {{ $t("form.deactivate") }}
              </div>
              <div
                v-if="scope.row.active_status == 0"
                class="icon_cz"
                @click="clickactiveBtn(scope.row)"
              >
                <i class="el-icon-video-play" />
                {{ $t("form.activate") }}
              </div>
              <div class="icon_cz" @click="clickconfigBtn(scope.row)">
                <i class="el-icon-edit" />
                {{ $t("form.configNode") }}
              </div>
              <div class="icon_cz" @click="clickUnbindBtn(scope.row)">
                <i class="el-icon-refresh-right" />
                {{ $t("form.unbind") }}
              </div>

              <div
                v-if="
                  scope.row.active_status == 1 && scope.row.online_status == 1
                "
                class="icon_cz"
                @click="clickcmdBtn(scope.row)"
              >
                <i class="el-icon-monitor" />
                {{ $t("form.remoteTTY") }}
              </div>
              <div v-else class="no_icon_cz">
                <i class="el-icon-monitor" />
                {{ $t("form.remoteTTY") }}
              </div>
              <router-link
                style="color: #409eff"
                :to="'/statistics?type=monitor&nodeId=' + scope.row.node_id"
              >
                <div class="icon_cz">
                  <i class="el-icon-s-marketing" />
                  {{ $t("form.statisticsAnalysis") }}
                </div>
              </router-link>

              <el-button
                slot="reference"
                icon="el-icon-more"
                class="czbtn right_czbtn"
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
    <!-- <addForm ref="addForm"
             :sup_this="sup_this" /> -->
    <configNode ref="configNode" />
    <!-- 节点升级弹框开始-->
    <el-dialog
      :visible.sync="batchVisible"
      width="800px"
      :before-close="batchHandleClose"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        label-width="100px"
        :inline="true"
        class="demo-form-inline"
        size="small"
      >
        <el-form-item :label="$t('form.nodeVersion') + ':'">
          <el-select
            v-model="form.new_version"
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              v-for="(item, key) in versionList"
              :key="key"
              :label="item.version"
              :value="item.version"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.upgradeComplete') + ':'">
          <el-radio-group v-model="form.need_confirm">
            <el-radio :label="true">{{ $t("form.manualConfirm") }}</el-radio>
            <el-radio :label="false">{{ $t("form.autoConfirm") }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div v-if="nothingness">
        <h2 style="padding-bottom: 10px">
          <i class="el-icon-warning-outline" style="color: #fbb561">{{
            $t("form.unOperableNode")
          }}</i>
        </h2>
      </div>
      <span>
        <el-table
          :data="batchData"
          style="width: 100%"
          :cell-style="cellStyle"
          tooltip-effect="dark"
        >
          <el-table-column
            prop="node_name"
            :label="$t('form.nodeName')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <el-tooltip
                v-if="scope.row.online_status == 1"
                effect="dark"
                :content="$t('form.online')"
                placement="top-end"
              >
                <img
                  src="@/assets/svg/wifion.svg"
                  alt=""
                  width="15px"
                  style="display: inline-block"
                />
              </el-tooltip>
              <el-tooltip
                v-if="scope.row.online_status == 0"
                effect="dark"
                :content="$t('form.offline')"
                placement="top-end"
              >
                <img
                  src="@/assets/svg/wifioff.svg"
                  alt=""
                  width="15px"
                  style="display: inline-block"
                />
              </el-tooltip>
              <span> {{ scope.row.node_name }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="host_name"
            :label="$t('form.hostName')"
            show-overflow-tooltip
          />
          <el-table-column
            prop="core_version"
            :label="$t('form.currentVersion')"
            show-overflow-tooltip
          />
          <el-table-column :label="$t('form.activation')">
            <template slot-scope="scope">
              <div v-if="scope.row.active_status == 0" class="error">
                <span>{{ $t("form.notActivated") }}</span>
              </div>
              <div v-if="scope.row.active_status == 1" class="success">
                <span>{{ $t("form.activated") }}</span>
              </div>
              <div v-if="scope.row.active_status == 2" class="upgrade">
                <span>{{ $t("form.upgrading") }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.operationStatus')">
            <template slot-scope="scope">
              <div>
                <span
                  v-if="
                    scope.row.operationStatus == 'OperableStatusNotOperable'
                  "
                >
                  {{ $t("form.notOperable") }}
                </span>
                <span
                  v-if="scope.row.operationStatus == 'OperableStatusNormal'"
                >
                  {{ $t("form.normal") }}
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" width="120">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click.native.prevent="deleteRow(scope.$index, batchData)"
              >
                {{ $t("form.cancelSelect") }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="batchHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" size="small" @click="batchVisibleClick()">{{
          $t("form.confirm")
        }}</el-button>
      </span>
    </el-dialog>
    <!--节点升级弹框结束 -->

    <!--安装容器节点弹框开始-->
    <el-dialog
      :title="$t('form.installContainerNode')"
      :visible.sync="installNodes"
      :close-on-click-modal="false"
      width="800px"
      :before-close="handleClose"
    >
      <el-form
        v-if="$route.params.cmdModal"
        size="small"
        label-width="120px"
        style="display: flex"
      >
        <el-form-item :label="$t('form.installCommand')" style="width: 80%">
          <el-input
            v-model="$route.params.cmdInfo"
            type="textarea"
            :autosize="{ minRows: 3, maxRows: 6 }"
          />
        </el-form-item>
        <el-form-item label="" label-width="10px" style="width: 20%">
          <span
            style="font-size: 16px"
            @click="copy($event, $route.params.cmdInfo)"
            ><i class="el-icon-document-copy"
          /></span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer" />
    </el-dialog>
    <!--安装容器节点弹框结束-->
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  edges,
  edgesVersions,
  deledges,
  deactiveedges,
  activeedges,
  createstacks,
  deleteStacks,
  getSites,
  getRegions,
  edgesUpgrade,
  setEcrmEdges,
  getNodeQRCode,
  instances,
  getDockerStacks,
  unbindsNodes,
  statsNodesStatusMetrics,
  statsStatusMetrics,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import configNode from "./node/configNode";
import Clipboard from "clipboard";

export default {
  components: {
    configNode,
  },
  mixins: [initData],
  data() {
    return {
      nodeTimer: null,
      timer: null,
      arealist: [], // 区域列表
      website: [], // 站点列表
      versionList: [], // 版本列表
      sup_this: this,
      installNodes: false,
      // 事件ID
      stack_id: "",
      queryData: {
        region: "",
        site: "",
        name: "",
        active_status: "",
        online_status: "",
        page_size: 10,
        page_num: 1,
      },
      form: {
        new_version: "", // 升级到的版本
        need_confirm: false, // 是否需要确认
      },
      activeTotal: 0, // 已激活
      tableData: [],
      // 选择的行
      multipleSelection: [],
      // 批量操作
      batch: "",
      // 批量操作标题
      batchTitle: "",
      // 批量操作显示
      batchVisible: false,
      // 批量操作类型
      batchType: "",
      // 批量删除数据
      batchData: [],
      // 批量操作提示
      nothingness: false,
      // 失败的数据
      errorData: [],
      // 按钮禁用
      disabled: true,

      // 节点状态统计
      nodeMain: {
        offline: 0,
        online: 0,
        total_num: 0,
        deactive: 0,
      },

      // 动态宽度
      cardWidth: 24.25,
    };
  },
  created() {
    this.init();
    this.initNodes();
    this.getEdgesVersions(); // 获取节点版本
    this.areainit();
    this.websiteinit();
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  watch: {
    "$route.params.cmdModal": {
      deep: true,
      handler(val) {
        if (val) {
          setTimeout(() => {
            this.installNodes = true;
          }, 500);
        }
      },
      immediate: true,
    },
  },
  mounted() {
    this.timer = setInterval(() => {
      this.getttyStatus(); // 获取tty状态
    }, 1000);
    this.nodeTimer = setInterval(() => {
      this.initTime();
    }, 1000 * 5);
  },
  beforeDestroy() {},
  destroyed() {
    // 在页面销毁后，清除计时器
    this.clear();
  },
  methods: {
    handleClose() {
      this.installNodes = false;
      this.$route.params.cmdModal = false;
      this.$route.params.cmdInfo = null;
    },
    async initNodes() {
      const nodeData = await statsStatusMetrics({ filter: "node-online" });
      const deactive = await statsNodesStatusMetrics({ filter: "inactive" });
      nodeData.status_metrics
        ? nodeData.status_metrics.forEach((item, index) => {
            item.status.desc.en == "offline"
              ? (this.nodeMain.offline = item.count)
              : "",
              item.status.desc.en == "online"
                ? (this.nodeMain.online = item.count)
                : "";
          })
        : "";

      (this.nodeMain.total_num = nodeData.total_num ? nodeData.total_num : ""),
        (this.nodeMain.deactive = deactive.all_node_metrics
          ? deactive.all_node_metrics.inactive_num
          : "");
    },
    // 激活触发
    handleActiveStatusChange(val) {
      this.queryData.active_status = val;
      this.init();
    },
    clear() {
      clearInterval(this.timer); // 清除计时器
      this.timer = null; // 设置为null
      clearInterval(this.nodeTimer); // 清除计时器
      this.nodeTimer = null; // 设置为null
    },
    confirmUpgradeEnd(item) {
      setEcrmEdges(item.node_id, { action: "confirm" })
        .then((res) => {
          this.$notify({
            title: this.$t("form.confirmUpgradeComplete"),
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    UpgradeBack(item) {
      setEcrmEdges(item.node_id, { action: "rollback" })
        .then((res) => {
          this.$notify({
            title: this.$t("form.rollbackOperationSuccess"),
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    getttyStatus() {
      if (localStorage.getItem("ttyStatus")) {
        deleteStacks(localStorage.getItem("ttyStatus"))
          .then((res) => {
            localStorage.setItem("ttyStatus", "");
          })
          .catch((err) => {});
      }
    },
    // 升级节点
    clickUpgradeBtn() {
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectNode"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        for (let i = 0; i < this.batchData.length; i++) {
          if (
            this.batchData[i].active_status == 1 &&
            this.batchData[i].online_status == 1
          ) {
            this.batchData[i].operationStatus = "OperableStatusNormal";
          } else {
            this.batchData[i].operationStatus = "OperableStatusNotOperable";
            this.nothingness = true;
          }
        }
        this.batchVisible = true;
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.batchData = val;
      if (this.multipleSelection.length > 0) {
        this.disabled = false;
      } else {
        this.disabled = true;
      }
    },
    // 批量删除弹框取消调用
    batchHandleClose() {
      this.batchVisible = false;
      this.batchData = [];
      this.nothingness = false;
      this.errorData = [];
      this.$refs.multipleTable.clearSelection();
    },
    // 单元格样式设置
    cellStyle(row, column, rowIndex, columnIndex) {
      if (row.row.operationStatus == "OperableStatusNotOperable") {
        return "color:red";
      }
    },
    // 移除选中行
    deleteRow(index, rows) {
      rows.splice(index, 1);
      if (this.batchData.length > 0) {
        for (let i = 0; i < this.batchData.length; i++) {
          if (
            this.batchData[i].operationStatus.indexOf(
              "OperableStatusNotOperable"
            ) != -1
          ) {
            this.nothingness = true;
          } else {
            this.nothingness = false;
          }
        }
      } else {
        this.nothingness = false;
      }
    },
    batchVisibleClick() {
      this.errorData = [];
      if (this.form.new_version == "") {
        this.$notify({
          title: this.$t("form.pleaseSelectNodeVersion"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.nothingness === true) {
        this.$notify({
          title: this.$t("form.pleaseCancelUnOperableNode"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.batchData.length === 0) {
        this.$notify({
          title: this.$t("form.noOperationOptions"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      this.$confirm(
        this.$t("form.confirmBatchUpgradeNode"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          for (let i = 0; i < this.batchData.length; i++) {
            var formData = this.batchData[i];
            edgesUpgrade(
              {
                node_id: formData.node_id,
                new_version: this.form.new_version, // 升级到的版本
                need_confirm: this.form.need_confirm, // 是否需要确认
              },
              formData.node_id
            )
              .then((res) => {
                if (i == this.batchData.length - 1) {
                  this.$refs.multipleTable.clearSelection();
                  this.$notify({
                    title: this.$t("form.batchUpgradeOperationComplete"),
                    type: "success",
                    duration: 2500,
                  });
                  this.init();
                  this.batchVisible = false;
                }
              })
              .catch((err) => {
                this.errorData.push(this.batchData[i].node_name);
                if (this.errorData.length > 0) {
                  for (let j = 0; j < this.errorData.length; j++) {
                    if (j == this.batchData.length - 1) {
                      this.$notify({
                        title: this.$t(
                          "form.batchOperationFailedWithNodeName",
                          { name: this.errorData.join(",") }
                        ),
                        type: "error",
                        duration: 2500,
                      });
                    }
                  }
                }
              });
          }
        })
        .catch(() => {});
    },
    handleRegionChange(val) {
      this.queryData.region = val;
      this.queryData.site = "";
      this.websiteinit();
      this.init();
    },
    handleWebsiteChange(val) {
      this.queryData.site = val;
      this.init();
    },
    handleOnlineStatusChange(val) {
      this.queryData.online_status = val;
      this.init();
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
    },
    async websiteinit() {
      const list = await getSites({ region_id: this.queryData.region });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    getGpuList(arr) {
      const newObj = {};
      arr.forEach(function (item) {
        if (newObj[item.gpu_model]) {
          newObj[item.gpu_model].push({
            gpu_model: item.gpu_model,
            gpu_type: item.gpu_type,
          });
        } else {
          newObj[item.gpu_model] = [];
          newObj[item.gpu_model] = [
            { gpu_model: item.gpu_model, gpu_type: item.gpu_type },
          ];
        }
      });
      const newArr = [];
      Object.keys(newObj).forEach(function (item) {
        newArr.push({
          gpu_model: newObj[item][0].gpu_model,
          gpu_type: newObj[item][0].gpu_type,
          gpu_num: newObj[item].length,
        });
      });
      return newArr;
    },
    getEdgesVersions() {
      edgesVersions().then((res) => {
        this.versionList = res;
        if (res.length > 0) {
          this.form.new_version = res[0].version;
        }
      });
    },
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await edges(this.queryData);
      this.tableData = list;
      const listNum = await edges({
        page_size: 9999,
        page_num: 1,
        active_status: 1,
      });
      this.activeTotal = listNum.total_num;
    },
    async initTime() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await edges(this.queryData);
      this.tableData = list;
    },
    clickDelBtn(value) {
      this.$confirm(this.$t("form.confirmDeleteNode"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          deledges(value.node_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickdeactiveBtn(value) {
      this.$confirm(
        this.$t("form.confirmDeactivateNode"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          deactiveedges(value.node_id)
            .then((res) => {
              this.$notify({
                title: this.$t("form.nodeDeactivationCommandExecuted"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickactiveBtn(value) {
      this.$confirm(this.$t("form.confirmActivateNode"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          activeedges(value.node_id)
            .then((res) => {
              this.$notify({
                title: this.$t("form.nodeActivationCommandExecuted"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    searchinit() {
      this.queryData = {
        region: "",
        site: "",
        name: "",
        active_status: "",
        online_status: "",
        page_size: 10,
        page_num: 1,
      };
      this.handleCurrentChange(1);
    },
    clickconfigBtn(value) {
      // 配置节点
      this.$nextTick(() => {
        this.$refs.configNode.getLabelOption("node");
        this.$refs.configNode.getTaintOptions("node");
      });
      setTimeout(() => {
        this.$refs.configNode.show(JSON.parse(JSON.stringify(value)));
      }, 700);
    },
    // 添加节点调用
    clickAddBtn() {
      this.$router.push({
        path: "/infrastructure/addNodes",
      });
    },
    //   this.$router.push(
    //     path: "/infrastructure/addNodes"
    //   // this.$refs.addForm.getLabelOption("node");
    //   // this.$refs.addForm.getTaintOptions("node");
    //   // this.$refs.addForm.dialog = true;
    // },
    // 节点命令行调用
    clickcmdBtn(value) {
      // 创建节点上的应用
      var data = {
        node_id: value.node_id,
        type: "tty",
      };
      createstacks(data)
        .then((res) => {
          if (res == "busy") {
            this.$notify({
              title: this.$t("form.nodeResourceBusy"),
              type: "warning",
              duration: 2500,
            });
          } else {
            const routeUrl = this.$router.resolve({
              path: "/command" + "/" + res + "/" + value.node_name,
            });
            window.open(routeUrl.href, "_blank");
          }
        })
        .catch((err) => {
          this.stack_id = err.response.data.message;
        });
    },
    clickUnbindBtn(value) {
      this.$confirm(this.$t("form.confirmUnbindNode"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(async () => {
          var data = {};
          data["node_id"] = value.node_id;
          const list = await getDockerStacks(data);
          const containersList = await instances(data);

          if (containersList.containers.length > 0 || list.stacks.length > 0) {
            console.log(11111);
            this.$notify({
              title: this.$t("form.nodeOccupied"),
              type: "warning",
              duration: 2500,
            });
            return;
          }

          unbindsNodes({ node_ids: [value.node_id] })
            .then((res) => {
              this.$notify({
                title: this.$t("message.unbindSuccess"),
                type: "success",
                duration: 2500,
              });
            })
            .catch((err) => {});
        })
        .catch(() => {});
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
  },
};
</script>

<style lang="scss" scoped>
.circle {
  color: #ebecf5;

  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 36px;
    height: 36px;
    background: #ebecf5;
    border-radius: 50%;
  }
}

.circleline {
  color: #edf6eb;

  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 36px;
    height: 36px;
    background: #edf6eb;
    border-radius: 50%;
  }
}

.circleoffline {
  color: #fdf5eb;

  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 36px;
    height: 36px;
    background: #fdf5eb;
    border-radius: 50%;
  }
}

.circleAcitve {
  color: #edeef0;

  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 36px;
    height: 36px;
    background: #edeef0;
    border-radius: 50%;
  }
}

.smallTitle {
  color: #4789dd;
  font-size: 17px;
  line-height: 85px;
  margin-left: 75px;
}

::v-deep .xterm .xterm-screen canvas {
  position: absolute;
  left: 0;
  top: 0;
  height: 100vh !important;
}

::v-deep .el-dialog__body {
  padding: 17px 20px;
  color: #606266;
  font-size: 14px;
  word-break: break-all;
}

::v-deep .shell-dot-icon {
  opacity: 0;
}

.el-popover {
  min-width: 0px !important;
  padding: 0px !important;
}

.icon_cz {
  display: block;
  height: 24px;
  color: #666666;
  align-items: center;
  cursor: pointer;
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 24px;
  text-align: center;
}

.icon_cz:hover {
  background: #daefff;
}

.no_icon_cz {
  display: block;
  height: 24px;
  color: #ccc;
  align-items: center;
  margin-top: 10px;
  text-align: center;
}

.no_icon_cz:hover {
  background: transparent;
}

.czbtn {
  width: 23px;
  height: 26px;
  border: 1px solid #d0d0d0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_czbtn {
  border-radius: 3px 0 0 3px;
  border-right: 0px;
  cursor: pointer;
}

.left_czbtn img {
  height: 11px;
}

.right_czbtn {
  border-radius: 3px;
}

.normal {
  color: #0f97ff;

  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 8px;
    height: 8px;
    background: #ffbc00;
    border-radius: 50%;
  }
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

::v-deep .el-card__header {
  padding: 0px 0px;
  border-bottom: 0px solid #ebeef5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  height: 300px;
}

.flexStart {
  display: flex;
}
</style>
