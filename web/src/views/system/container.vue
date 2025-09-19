<template>
  <div class="">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="form"
        class="demo-form-inline"
      >
        <el-form-item v-if="apiType == 'deploy'">
          <div slot="label">{{ $t("form.region") }}:</div>
          <el-select
            v-model="form.region"
            filterable
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              v-for="(item, index) in arealist"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="apiType == 'deploy'">
          <div slot="label">{{ $t("form.site") }}:</div>
          <el-select
            v-model="form.site"
            filterable
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              v-for="(item, index) in website"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="apiType == 'deploy'">
          <div slot="label">{{ $t("form.node") }}:</div>
          <el-select
            v-model="form.node_id"
            filterable
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              v-for="(item, index) in nodeList"
              :key="index"
              :label="item.node_name"
              :value="item.node_id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="apiType == 'deploy'">
          <div slot="label">{{ $t("form.status") }}:</div>
          <el-select
            v-model="form.status"
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              v-for="(item, index) in newStatus"
              :key="index"
              :label="item.name"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="apiType == 'deploy'">
          <el-button class="add_search" @click="initform()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-form-item v-if="apiType != 'service'" style="float: right">
          <el-dropdown>
            <el-button type="primary" size="mini" :disabled="handledisabled">
              {{ $t("form.batchOperation")
              }}<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>
                <el-button
                  style="width: 100px"
                  icon="el-icon-delete"
                  type="text"
                  @click="batchDelete"
                >
                  {{ $t("form.delete") }}
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  style="width: 100px"
                  icon="el-icon-video-play"
                  type="text"
                  @click="batchStart"
                >
                  {{ $t("form.bootUp") }}
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  style="width: 100px"
                  icon="el-icon-video-pause"
                  type="text"
                  @click="batchStop"
                >
                  {{ $t("form.stop") }}
                </el-button>
              </el-dropdown-item>
              <el-dropdown-item>
                <el-button
                  style="width: 100px"
                  icon="el-icon-refresh-right"
                  type="text"
                  @click="batchRestart"
                >
                  {{ $t("form.restart") }}
                </el-button>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTableRef"
      :data="tableData.containers"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
      @selection-change="handleSelectionChange"
    >
      <el-table-column
        v-if="apiType != 'service'"
        type="selection"
        width="55"
      />
      <el-table-column :label="$t('form.containerName')">
        <template slot-scope="scope">
          <div
            style="
              text-overflow: ellipsis;
              white-space: nowrap;
              word-wrap: normal;
              overflow: hidden;
            "
          >
            <span
              v-if="
                scope.row.tip_message != null &&
                scope.row.tip_message.level == 'error'
              "
              style="float: left; margin-right: 5px"
            >
              <el-tooltip
                effect="dark"
                :content="scope.row.tip_message.message"
                placement="bottom"
              >
                <svg
                  t="1655291507763"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="4205"
                  width="15"
                  height="15"
                >
                  <path
                    d="M512 74.666667C270.933333 74.666667 74.666667 270.933333 74.666667 512S270.933333 949.333333 512 949.333333 949.333333 753.066667 949.333333 512 753.066667 74.666667 512 74.666667z m0 810.666666c-204.8 0-373.333333-168.533333-373.333333-373.333333S307.2 138.666667 512 138.666667 885.333333 307.2 885.333333 512 716.8 885.333333 512 885.333333z"
                    p-id="4206"
                    fill="#d81e06"
                  />
                  <path
                    d="M657.066667 360.533333c-12.8-12.8-32-12.8-44.8 0l-102.4 102.4-102.4-102.4c-12.8-12.8-32-12.8-44.8 0-12.8 12.8-12.8 32 0 44.8l102.4 102.4-102.4 102.4c-12.8 12.8-12.8 32 0 44.8 6.4 6.4 14.933333 8.533333 23.466666 8.533334s17.066667-2.133333 23.466667-8.533334l102.4-102.4 102.4 102.4c6.4 6.4 14.933333 8.533333 23.466667 8.533334s17.066667-2.133333 23.466666-8.533334c12.8-12.8 12.8-32 0-44.8l-106.666666-100.266666 102.4-102.4c12.8-12.8 12.8-34.133333 0-46.933334z"
                    p-id="4207"
                    fill="#d81e06"
                  />
                </svg>
              </el-tooltip>
            </span>
            <span
              v-if="
                scope.row.tip_message != null &&
                scope.row.tip_message.level == 'warning'
              "
              style="float: left; margin-right: 5px"
            >
              <el-tooltip
                effect="dark"
                :content="scope.row.tip_message.message"
                placement="bottom"
              >
                <svg
                  t="1655291434825"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="3064"
                  width="15"
                  height="15"
                >
                  <path
                    d="M598.272514 158.17909C545.018272 71.994036 451.264177 71.951401 397.724122 158.397341L25.049726 760.118586C-28.93569 847.283607 14.324655 927.325257 116.435565 929.308966L891.057077 929.313666C993.88467 931.315989 1036.926865 868.038259 983.25955 781.189694 980.374633 776.521099 980.374633 776.521099 971.719878 762.515313 967.393745 755.514432 967.393745 755.514432 963.78822 749.679695 956.511588 737.90409 941.113263 734.285867 929.3951 741.59817 917.676937 748.910473 914.076365 764.384279 921.352996 776.159885 924.958522 781.994622 924.958522 781.994622 929.284655 788.995503 937.939409 803.001289 937.939409 803.001289 940.824326 807.669884 972.284602 858.581314 957.441559 880.402549 891.539823 879.122276L116.918309 879.117577C54.037254 877.891296 33.95555 840.735497 67.458075 786.642217L440.132471 184.920971C474.112981 130.055931 522.112175 130.077759 556.029583 184.965509L857.08969 656.83971C864.534622 668.508595 879.98329 671.9032 891.595253 664.421773 903.207217 656.940343 906.585263 641.415949 899.140331 629.747063L598.272514 158.17909Z"
                    p-id="3065"
                    fill="#e98f36"
                  />
                  <path
                    d="M474.536585 619.793346C474.536585 633.654611 485.718547 644.891386 499.512194 644.891386 513.305843 644.891386 524.487806 633.654611 524.487806 619.793346L524.487806 299.793346C524.487806 285.932082 513.305843 274.695307 499.512194 274.695307 485.718547 274.695307 474.536585 285.932082 474.536585 299.793346L474.536585 619.793346Z"
                    p-id="3066"
                    fill="#e98f36"
                  />
                  <path
                    d="M474.465781 776.736145C474.565553 790.597047 485.828105 801.75225 499.621393 801.651987 513.414679 801.551725 524.515467 790.233967 524.415695 776.373065L523.955031 712.375667C523.855258 698.514767 512.592708 687.359563 498.79942 687.459825 485.006133 687.560087 473.905346 698.877847 474.005118 712.738748L474.465781 776.736145Z"
                    p-id="3067"
                    fill="#e98f36"
                  />
                </svg>
              </el-tooltip>
            </span>
            <span
              v-if="
                scope.row.tip_message != null &&
                scope.row.tip_message.level == 'info'
              "
              style="float: left; margin-right: 5px"
            >
              <el-tooltip
                effect="dark"
                :content="scope.row.tip_message.message"
                placement="bottom"
              >
                <svg
                  t="1655290925029"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="2157"
                  width="15"
                  height="15"
                >
                  <path
                    d="M509.92 795.84c157.904 0 285.92-128.016 285.92-285.92C795.84 352 667.808 224 509.92 224 352 224 224 352 224 509.92c0 157.904 128 285.92 285.92 285.92z m0 48C325.504 843.84 176 694.32 176 509.92 176 325.504 325.504 176 509.92 176c184.416 0 333.92 149.504 333.92 333.92 0 184.416-149.504 333.92-333.92 333.92z m-2.448-400.704h16a16 16 0 0 1 16 16v201.728a16 16 0 0 1-16 16h-16a16 16 0 0 1-16-16V459.136a16 16 0 0 1 16-16z m0-100.176h16a16 16 0 0 1 16 16v23.648a16 16 0 0 1-16 16h-16a16 16 0 0 1-16-16v-23.648a16 16 0 0 1 16-16z"
                    p-id="2158"
                    fill="#1296db"
                  />
                </svg>
              </el-tooltip>
            </span>
            <router-link
              :to="
                apiType == 'node'
                  ? '/containerApplicationService/deployContainDetail/' +
                    scope.row.id
                  : apiType == 'service'
                  ? '/containerApplicationService/serviceContainDetail/' +
                    scope.row.id
                  : apiType == 'deploy'
                  ? '/containerApplicationService/deployContainDetail/' +
                    scope.row.id
                  : '/containerApplicationService/deployContainDetail/' +
                    scope.row.id
              "
              style="color: rgb(64, 158, 255)"
            >
              <el-tooltip
                class="item"
                effect="dark"
                :content="scope.row.name"
                placement="top-start"
              >
                <span> {{ scope.row.name }} </span>
              </el-tooltip>
            </router-link>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.status')">
        <template slot-scope="scope">
          <span
            v-if="
              getStatusCode(1103).includes(scope.row.status.code) ||
              getStatusCode(1101).includes(scope.row.status.code)
            "
            class="success"
          />
          <span
            v-else-if="getStatusCode(1100).includes(scope.row.status.code)"
            class="creating"
          />
          <span v-else class="error" />
          <span>{{ getStatusName(scope.row.status.code) }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="image_name"
        show-overflow-tooltip
        :label="$t('form.imageName')"
      />
      <el-table-column
        v-if="apiType == 'deploy'"
        prop="region_name"
        :label="$t('form.region')"
      />
      <el-table-column
        v-if="apiType == 'deploy'"
        prop="site_name"
        :label="$t('form.site')"
      />
      <el-table-column
        v-if="apiType == 'deploy' || apiType == 'service'"
        prop="node_name"
        :label="$t('form.node')"
      >
        <template slot-scope="scope">
          <span> {{ scope.row.node_name || "-" }} </span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="apiType != 'service'"
        prop="user_name"
        :label="$t('form.user')"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span> {{ scope.row.user_name || "-" }} </span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div
                v-if="apiType != 'service'"
                class="icon_cz"
                @click="clickstartBtn(scope.row)"
              >
                <i class="el-icon-video-play" />
                {{ $t("form.bootUp") }}
              </div>
              <div
                v-if="apiType != 'service'"
                class="icon_cz"
                @click="clickRestartBtn(scope.row)"
              >
                <i class="el-icon-refresh-right" />
                {{ $t("form.restart") }}
              </div>
              <div
                v-if="apiType != 'service'"
                class="icon_cz"
                @click="clickStopBtn(scope.row)"
              >
                <i class="el-icon-video-pause" />
                {{ $t("form.stop") }}
              </div>
              <div
                v-if="apiType != 'service'"
                class="icon_cz"
                @click="clickDelBtn(scope.row)"
              >
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>
              <div class="icon_cz" @click="clickExecuteBtn(scope.row)">
                <i class="el-icon-monitor" />
                {{ $t("form.executeCommand") }}
              </div>
              <div class="icon_cz" @click="clickLogsBtn(scope.row)">
                <i class="el-icon-document" />
                {{ $t("form.viewLogs") }}
              </div>

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
        :current-page="form.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="form.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <div v-show="dialogVisible" class="xtermbg">
      <div class="xtermmain">
        <h5>{{ $t("form.commandLine") }}:{{ cmdName }}</h5>
        <div id="xterm" class="xterm" style="overflow: auto">
          <div v-if="!cmdStatus" style="color: #fff; padding: 10px 15px">
            {{ $t("form.connecting") }}
          </div>
        </div>
        <el-button type="primary" size="small" @click="closeterm()">{{
          $t("form.close")
        }}</el-button>
      </div>
    </div>
    <el-dialog
      :title="titlenName"
      :visible.sync="journaldialogVisible"
      width="800px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div
        ref="journal"
        class="journalStyle"
        v-html="journalData == '' ? $t('form.loading') : journalData"
      />
      <el-button
        type="primary"
        size="small"
        style="margin: 20px auto 0; display: block"
        @click="saveJournal"
      >
        {{ $t("form.save") }}
      </el-button>
    </el-dialog>
    <!--批量操作弹框-->
    <el-dialog
      :visible.sync="batchVisible"
      width="800px"
      :before-close="batchHandleClose"
    >
      <div v-if="nothingness">
        <h2 style="padding-bottom: 10px">
          <i class="el-icon-warning-outline" style="color: #fbb561">{{
            $t("form.unOperableContainer")
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
            prop="name"
            :label="$t('form.containerName')"
            show-overflow-tooltip
          />
          <el-table-column :label="$t('form.status')">
            <template slot-scope="scope">
              <span
                v-if="
                  getStatusCode(1103).includes(scope.row.status.code) ||
                  getStatusCode(1101).includes(scope.row.status.code)
                "
                class="success"
              />
              <span
                v-else-if="getStatusCode(1100).includes(scope.row.status.code)"
                class="creating"
              />
              <span v-else class="error" />
              <span>{{ getStatusName(scope.row.status.code) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            v-if="apiType != 'service'"
            prop="user_name"
            :label="$t('form.user')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <span> {{ scope.row.user_name || "-" }} </span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.operationStatus')">
            <template slot-scope="scope">
              <div>
                <span
                  v-if="scope.row.operationStatus == 'containerNotOperable'"
                >
                  {{ $t("form.notOperable") }}
                </span>
                <span v-if="scope.row.operationStatus == 'containerNormal'">
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
        <el-button @click="batchHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" @click="batchVisibleClick()">{{
          $t("form.confirm")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getDeploysInstances,
  getDockerServicesInstances,
  instances,
  getSites,
  getRegions,
  edges,
  setInstances,
  setInstances01,
  statsInstances,
  delInstances,
} from "@/api/mainApi";
import initData from "@/mixins/initData";

var termm = require("@/utils/term.js");
export default {
  components: {},
  mixins: [initData],
  props: {
    apiType: {
      type: String,
      default: "node  ", // node 节点下容器，deploy 容器部署下容器，//service 应用服务下容器
    },
    nodeId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      cmdStatus: false, // 命令行连接状态
      journalData: "",
      nowWebsockData: "",
      websock: "",
      nowWebjournalsockData: "",
      webjournalsock: "",
      cmdName: "",
      journaldialogVisible: false,
      dialogVisible: false,
      handledisabled: true,
      titlenName: "",
      term: "",
      nowText: "",
      arealist: [],
      website: [],
      nodeList: [],
      sup_this: this,
      tableData: {
        containers: [],
        total_num: 0,
      },
      newStatus: [
        {
          code: 1100,
          name: this.$t("statusAndType.creating"),
          include: [0, 1, 2, 3, 4, 100, 102, 101, 5, 6],
        },
        {
          code: 1101,
          name: this.$t("statusAndType.created"),
          include: [103],
        },
        {
          code: 1102,
          name: this.$t("statusAndType.createFailed"),
          include: [50, 51, 81, 82, 122, 123, 124, 125],
        },
        {
          code: 1103,
          name: this.$t("statusAndType.running"),
          include: [104, 105, 108],
        },
        {
          code: 1104,
          name: this.$t("statusAndType.containerExited"),
          include: [60, 61, 62, 63, 64, 65, 110, 111, 112, 113],
        },
        {
          code: 1105,
          name: this.$t("statusAndType.containerNotExist"),
          include: [130, 131, 132],
        },
        {
          code: 1110,
          name: this.$t("statusAndType.systemException"),
          include: [-100, 99, 120, 190],
        },
      ],
      status: [
        {
          code: -100,
          name: this.$t("statusAndType.resourceUnknown"),
        },
        {
          code: 0,
          name: this.$t("statusAndType.instanceParameterConfigured"),
        },
        {
          code: 1,
          name: this.$t("statusAndType.cloudStartDispatchCreateInstance"),
        },
        {
          code: 2,
          name: this.$t("statusAndType.selectTargetDevice"),
        },
        {
          code: 3,
          name: this.$t("statusAndType.assignTargetDevice"),
        },
        {
          code: 4,
          name: this.$t("statusAndType.prepareToCreate"),
        },
        {
          code: 50,
          name: this.$t("statusAndType.unreachable"),
        },
        {
          code: 51,
          name: this.$t("statusAndType.noMatchingNode"),
        },
        {
          code: 60,
          name: this.$t("statusAndType.stopping"),
        },
        {
          code: 61,
          name: this.$t("statusAndType.stopping"),
        },
        {
          code: 62,
          name: this.$t("statusAndType.stopping"),
        },
        {
          code: 63,
          name: this.$t("statusAndType.systemStop"),
        },
        {
          code: 64,
          name: this.$t("statusAndType.userStop"),
        },
        {
          code: 65,
          name: this.$t("statusAndType.overdueStop"),
        },
        {
          code: 81,
          name: this.$t("statusAndType.parameterErrorCreateFailed"),
        },
        {
          code: 82,
          name: this.$t("statusAndType.overdueCreateFailed"),
        },
        {
          code: 83,
          name: this.$t("statusAndType.deleting"),
        },
        {
          code: 84,
          name: this.$t("statusAndType.deleted"),
        },
        {
          code: 99,
          name: this.$t("statusAndType.cloudException"),
        },
        {
          code: 100,
          name: this.$t("statusAndType.deviceWaitingDispatch"),
        },
        {
          code: 101,
          name: this.$t("statusAndType.imageDownloading"),
        },
        {
          code: 102,
          name: this.$t("statusAndType.dataDownloading"),
        },
        {
          code: 103,
          name: this.$t("statusAndType.containerCreateSuccess"),
        },
        {
          code: 104,
          name: this.$t("statusAndType.containerStarting"),
        },
        {
          code: 105,
          name: this.$t("statusAndType.containerRunning"),
        },
        {
          code: 106,
          name: this.$t("statusAndType.uploadResult"),
        },
        {
          code: 108,
          name: this.$t("statusAndType.restarting"),
        },
        {
          code: 110,
          name: this.$t("statusAndType.containerExited"),
        },
        {
          code: 111,
          name: this.$t("statusAndType.userStop"),
        },
        {
          code: 112,
          name: this.$t("statusAndType.overdueStop"),
        },
        {
          code: 113,
          name: this.$t("statusAndType.abnormalExit"),
        },
        {
          code: 120,
          name: this.$t("statusAndType.systemStop"),
        },
        {
          code: 121,
          name: this.$t("statusAndType.resourceParameterError"),
        },
        {
          code: 122,
          name: this.$t("statusAndType.hardwareError"),
        },
        {
          code: 123,
          name: this.$t("statusAndType.noImage"),
        },
        {
          code: 124,
          name: this.$t("statusAndType.imageDownloadFailed"),
        },
        {
          code: 125,
          name: this.$t("statusAndType.instanceCreateFailed"),
        },
        {
          code: 130,
          name: this.$t("statusAndType.instanceDeleted"),
        },
        {
          code: 131,
          name: this.$t("statusAndType.instanceNotExist"),
        },
        {
          code: 132,
          name: this.$t("statusAndType.instanceAutoDeleted"),
        },
        {
          code: 190,
          name: this.$t("statusAndType.systemException"),
        },
      ],
      form: {
        status: "",
        region: "",
        site: "",
        node_id: "",
        page_size: 10,
        page_num: 1,
      },
      node_id: "",
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
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  watch: {
    "form.region": {
      deep: true,
      handler(val) {
        this.form.site = "";
        this.form.node_id = "";
        if (val != "") {
          this.form.page_num = 1;
          this.websiteinit();
          this.edgesregion();
          this.init();
        }
      },
    },
    "form.site": {
      deep: true,
      handler(val) {
        this.form.node_id = "";
        if (val != "") {
          this.form.page_num = 1;
          this.edgesinit();
          this.init();
        }
      },
    },
    "form.node_id": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.form.page_num = 1;
          this.init();
        }
      },
    },
    "form.status": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.form.page_num = 1;
          this.init();
        }
      },
    },
  },
  created() {},
  mounted() {
    this.init();
    console.log(this.apiType);
  },
  beforeDestroy() {
    if (this.websock != "") {
      this.websock.close();
      this.cmdStatus = false;
    }
    if (this.webjournalsock != "") {
      this.webjournalsock.close();
    }
    if (this.term != "") {
      this.term.dispose();
    }
    this.dialogVisible = false;
  },
  methods: {
    getStatusCode(code) {
      return this.newStatus.find((item) => item.code == code).include;
    },
    getStatusName(code) {
      const item = this.newStatus.find((item) => item.include.includes(code));
      return item ? item.name : "-";
    },
    // 批量删除弹框取消调用
    batchHandleClose() {
      this.batchVisible = false;
      this.batchData = [];
      this.batchType = "";
      this.batchTitle = "";
      this.nothingness = false;
      this.errorData = [];
      this.$refs.multipleTableRef.clearSelection();
    },
    // 单元格样式设置
    cellStyle(row, column, rowIndex, columnIndex) {
      if (row.row.operationStatus == "containerNotOperable") {
        return "color:red";
      }
    },
    // 移除选中行
    deleteRow(index, rows) {
      rows.splice(index, 1);
      if (this.batchData.length > 0) {
        for (let i = 0; i < this.batchData.length; i++) {
          if (
            this.batchData[i].operationStatus.indexOf("containerNotOperable") !=
            -1
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
    // 批量删除
    batchDelete() {
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectContainer"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        this.batchType = "delete";
        this.batchTitle = this.$t("form.batchDelete");
        for (let i = 0; i < this.batchData.length; i++) {
          this.batchData[i].operationStatus = "containerNormal";
        }
      }
      this.batchVisible = true;
    },
    // 批量启动 处于创建成功，停止和退出状态。
    batchStart() {
      const codeData = [60, 61, 62, 63, 64, 65, 103, 110, 111, 113];
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectContainer"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        this.batchType = "start";
        this.batchTitle = this.$t("form.batchStart");
        for (let i = 0; i < this.batchData.length; i++) {
          if (codeData.indexOf(this.batchData[i].status.code) > -1) {
            this.batchData[i].operationStatus = "containerNormal";
          } else {
            this.batchData[i].operationStatus = "containerNotOperable";
            this.nothingness = true;
          }
        }
        this.batchVisible = true;
      }
    },
    // 批量停止 非停止和非退出状态的容器进行停止
    batchStop() {
      const codeData = [0, 1, 2, 3, 4, 100, 101, 102, 103, 104, 105, 108];
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectContainer"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        this.batchType = "stop";
        this.batchTitle = this.$t("form.batchStop");
        for (let i = 0; i < this.batchData.length; i++) {
          if (codeData.indexOf(this.batchData[i].status.code) > -1) {
            this.batchData[i].operationStatus = "containerNormal";
          } else {
            this.batchData[i].operationStatus = "containerNotOperable";
            this.nothingness = true;
          }
        }
        this.batchVisible = true;
      }
    },
    // 批量重启  处于运行、创建成功或退出状态的容器进行启动操作
    batchRestart() {
      const codeData = [
        60, 61, 62, 63, 64, 65, 103, 104, 105, 108, 110, 111, 113,
      ];
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectContainer"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        this.batchType = "restart";
        this.batchTitle = this.$t("form.batchRestart");
        for (let i = 0; i < this.batchData.length; i++) {
          if (codeData.indexOf(this.batchData[i].status.code) > -1) {
            this.batchData[i].operationStatus = "containerNormal";
          } else {
            this.batchData[i].operationStatus = "containerNotOperable";
            this.nothingness = true;
          }
        }
        this.batchVisible = true;
      }
    },
    // 批量操作调用的方法
    batchVisibleClick() {
      this.errorData = [];
      if (this.nothingness === true) {
        this.$notify({
          title: this.$t("form.pleaseCancelUnoperableContainer"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.batchData.length === 0) {
        this.$notify({
          title: this.$t("form.noOperationOption"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.batchType == "delete") {
        this.$confirm(
          this.$t("form.confirmBatchDeleteContainerIsNext"),
          this.$t("form.tip"),
          {
            confirmButtonText: this.$t("form.confirm"),
            cancelButtonText: this.$t("form.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            for (let i = 0; i < this.batchData.length; i++) {
              delInstances(this.batchData[i].id)
                .then((res) => {
                  if (i == this.batchData.length - 1) {
                    this.$refs.multipleTableRef.clearSelection();
                    this.$notify({
                      title: this.$t("form.batchDeleteSuccess"),
                      type: "success",
                      duration: 2500,
                    });
                    this.init();
                    this.batchVisible = false;
                  }
                })
                .catch((err) => {
                  this.errorData.push(this.batchData[i].name);
                  if (this.errorData.length > 0) {
                    for (let j = 0; j < this.errorData.length; j++) {
                      if (j == this.batchData.length - 1) {
                        this.$notify({
                          title: this.$t(
                            "form.batchOperationFailedWithContainerName",
                            {
                              name: this.errorData.join(","),
                            }
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
      } else if (this.batchType == "start") {
        this.$confirm(
          this.$t("form.confirmBatchStartContainerIsNext"),
          this.$t("form.tip"),
          {
            confirmButtonText: this.$t("form.confirm"),
            cancelButtonText: this.$t("form.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            for (let i = 0; i < this.batchData.length; i++) {
              setInstances(this.batchData[i].id, { action: "start" })
                .then((res) => {
                  if (i == this.batchData.length - 1) {
                    this.$refs.multipleTableRef.clearSelection();
                    this.$notify({
                      title: this.$t("form.batchStartSuccess"),
                      type: "success",
                      duration: 2500,
                    });
                    this.init();
                    this.batchVisible = false;
                  }
                })
                .catch((err) => {
                  this.errorData.push(this.batchData[i].name);
                  if (this.errorData.length > 0) {
                    for (let j = 0; j < this.errorData.length; j++) {
                      if (j == this.batchData.length - 1) {
                        this.$notify({
                          title: this.$t(
                            "form.batchOperationFailedWithContainerName",
                            {
                              name: this.errorData.join(","),
                            }
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
      } else if (this.batchType == "stop") {
        this.$confirm(
          this.$t("form.confirmBatchStopContainerIsNext"),
          this.$t("form.tip"),
          {
            confirmButtonText: this.$t("form.confirm"),
            cancelButtonText: this.$t("form.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            for (let i = 0; i < this.batchData.length; i++) {
              setInstances(this.batchData[i].id, { action: "stop" })
                .then((res) => {
                  if (i == this.batchData.length - 1) {
                    this.$refs.multipleTableRef.clearSelection();
                    this.$notify({
                      title: this.$t("form.batchStopSuccess"),
                      type: "success",
                      duration: 2500,
                    });
                    this.init();
                    this.batchVisible = false;
                  }
                })
                .catch((err) => {
                  this.errorData.push(this.batchData[i].name);
                  if (this.errorData.length > 0) {
                    for (let j = 0; j < this.errorData.length; j++) {
                      if (j == this.batchData.length - 1) {
                        this.$notify({
                          title: this.$t(
                            "form.batchOperationFailedWithContainerName",
                            {
                              name: this.errorData.join(","),
                            }
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
      } else if (this.batchType == "restart") {
        this.$confirm(
          this.$t("form.confirmBatchRestartContainerIsNext"),
          this.$t("form.tip"),
          {
            confirmButtonText: this.$t("form.confirm"),
            cancelButtonText: this.$t("form.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            for (let i = 0; i < this.batchData.length; i++) {
              setInstances(this.batchData[i].id, { action: "restart" })
                .then((res) => {
                  if (i == this.batchData.length - 1) {
                    this.$refs.multipleTableRef.clearSelection();
                    this.$notify({
                      title: this.$t("form.batchRestartSuccess"),
                      type: "success",
                      duration: 2500,
                    });
                    this.init();
                    this.batchVisible = false;
                  }
                })
                .catch((err) => {
                  this.errorData.push(this.batchData[i].name);
                  if (this.errorData.length > 0) {
                    for (let j = 0; j < this.errorData.length; j++) {
                      if (j == this.batchData.length - 1) {
                        this.$notify({
                          title: this.$t(
                            "form.batchOperationFailedWithContainerName",
                            {
                              name: this.errorData.join(","),
                            }
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
      }
    },
    clickFile(row, item) {
      var _this = this;
      _this.$refs.multipleTableRef.toggleRowSelection(item);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.batchData = val;
      if (this.multipleSelection.length > 0) {
        this.handledisabled = false;
      } else {
        this.handledisabled = true;
      }
    },

    saveJournal() {
      var blob = new Blob([this.journalData], { type: "text/plain" });
      var a = document.createElement("a");
      a.download = this.titlenName + ".txt";
      a.href = URL.createObjectURL(blob);
      a.dataset.downloadurl = ["text/plain", a.download, a.href].join(":");
      a.style.display = "none";
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      setTimeout(function () {
        URL.revokeObjectURL(a.href);
      }, 1500);
    },
    initform() {
      this.form = {
        status: "",
        region: "",
        site: "",
        node_id: "",
        page_size: 10,
        page_num: 1,
      };
      this.init();
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
    },
    async websiteinit() {
      const list = await getSites({ region_id: this.form.region });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
    },
    async edgesinit() {
      const list = await edges({ site: this.form.site });
      this.nodeList = list.nodes;
    },
    // 根据区域显示节点
    async edgesregion() {
      const list = await edges({ region: this.form.region });
      this.nodeList = list.nodes;
    },
    async init() {
      var data = JSON.parse(JSON.stringify(this.form));
      for (var key in data) {
        if (data[key] === undefined || data[key] === "") {
          delete data[key];
        }
      }
      var list;
      if (this.apiType == "deploy") {
        list = await getDeploysInstances(this.$route.params.id, data);
        if (list.containers != null && list.containers.length > 0) {
          this.tableData.containers = list.containers;
          this.tableData.total_num = list.total_num;
        } else {
          this.tableData.containers = [];
          this.tableData.total_num = 0;
        }
        this.areainit();
      } else if (this.apiType == "node") {
        data["node_id"] = this.nodeId;
        list = await instances(data);
        if (list.containers != null && list.containers.length > 0) {
          this.tableData.containers = list.containers;
          this.tableData.total_num = list.total_num;
        } else {
          this.tableData.containers = [];
          this.tableData.total_num = 0;
        }
      } else if (this.apiType == "service") {
        list = await getDockerServicesInstances(this.$route.params.id);
        if (list.containers != null && list.containers.length > 0) {
          this.tableData.containers = list.containers;
          this.tableData.total_num = list.total_num;
        } else {
          this.tableData.containers = [];
          this.tableData.total_num = 0;
        }
      } else {
        list = await instances(data);
        if (list.containers != null && list.containers.length > 0) {
          this.tableData.containers = list.containers;
          this.tableData.total_num = list.total_num;
        } else {
          this.tableData.containers = [];
          this.tableData.total_num = 0;
        }
        this.areainit();
      }
    },
    clickRestartBtn(item) {
      // 重启
      this.$confirm(
        this.$t("form.confirmRestartContainerIsNext"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setInstances(item.id, { action: "restart" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.restartCommandExecuted"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              this.$notify({
                title: err.response.data.message,
                type: "error",
                duration: 2500,
              });
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickStopBtn(item) {
      // 停止
      this.$confirm(
        this.$t("form.confirmStopContainerIsNext"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setInstances(item.id, { action: "stop" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.stopCommandExecuted"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              this.$notify({
                title: err.response.data.message,
                type: "error",
                duration: 2500,
              });
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickstartBtn(item) {
      // 启动
      this.$confirm(
        this.$t("form.confirmStartContainerIsNext"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setInstances(item.id, { action: "start" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.startCommandExecuted"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              this.$notify({
                title: err.response.data.message,
                type: "error",
                duration: 2500,
              });
            });
        })
        .catch(() => {});
    },
    clickExecuteBtn(item) {
      // 执行命令行
      this.$confirm(
        this.$t("form.confirmExecuteCommandIsNext"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.cmdName = item.name;
          var data = {
            cmds: [
              "/bin/sh",
              "-c",
              'TERM=xterm-256color; export TERM; [ -x /bin/bash ] && ([ -x /usr/bin/script ] && /usr/bin/script -q -c "/bin/bash" /dev/null || exec /bin/bash) || exec /bin/sh',
            ],
            tty: true,
            stdin: true,
            stdout: true,
          };
          setInstances01(item.id + "?action=execute", data)
            .then((res) => {
              this.nowWebsockData = res;
              this.dialogVisible = true;
              this.$notify({
                title: this.$t("form.executeCommandSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initWebSocket();

              this.init();
            })
            .catch((err) => {
              if (err.response.data.code == 1726) {
                this.$notify({
                  title: this.$t("form.containerCannotBeExecutedRemotely"),
                  type: "error",
                  duration: 2500,
                });
              }
            });
        })
        .catch(() => {});
    },
    clickLogsBtn(item) {
      this.titlenName = item.name + " " + this.$t("form.logs");
      // 查看日志
      setInstances(item.id, { action: "logs" })
        .then((res) => {
          this.nowWebjournalsockData = res;
          this.$notify({
            title: this.$t("form.logRequestSuccess"),
            type: "success",
            duration: 2500,
          });
          this.journalData = "";
          this.journaldialogVisible = true;

          this.initjournalWebSocket();
        })
        .catch((err) => {
          this.$notify({
            title: err.response.data.message,
            type: "error",
            duration: 2500,
          });
          console.error(err.response.data.message);
        });
    },
    clickStatsBtn(item) {
      // 资源消耗
      statsInstances(item.id)
        .then((res) => {
          this.$notify({
            title: this.$t("form.resourceConsumptionRequestSuccess"),
            type: "success",
            duration: 2500,
          });
        })
        .catch((err) => {
          this.$notify({
            title: err.response.data.message,
            type: "error",
            duration: 2500,
          });
          console.error(err.response.data.message);
        });
    },
    clickDelBtn(item) {
      // 删除
      this.$confirm(
        this.$t("form.confirmDeleteContainerIsNext"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delInstances(item.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              this.$notify({
                title: err.response.data.message,
                type: "error",
                duration: 2500,
              });
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.form.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.form.page_num = val;
      this.init();
    },
    getstatus(val) {
      var data;
      this.status.forEach((item) => {
        if (item.code == val) {
          data = item.name;
        }
      });
      return data;
    },
    closeterm() {
      if (this.websock != "") {
        this.websock.close();
        this.cmdStatus = false;
      }
      this.dialogVisible = false;
      this.term.destroy(); // 屏幕关闭
    },
    initTerm() {
      var _this = this;
      const term = new termm({
        cols: 62,
        rows: 20,
        fontSize: 14,
        screenKeys: false,
        useStyle: true,
        cursorBlink: true,
      });
      term.on("data", function (data) {
        const code = data.charCodeAt(0);
        _this.websock.send(data);
      });
      term.open(document.getElementById("xterm"));

      this.term = term;
    },
    initWebSocket() {
      // 初始化weosocket
      const wsuri = this.nowWebsockData.url;
      this.websock = new WebSocket(wsuri);
      this.websock.onmessage = this.websocketonmessage;
      this.websock.onopen = this.websocketonopen;
      this.websock.onerror = this.websocketonerror;
      this.websock.onclose = this.websocketclose;
    },
    websocketonopen() {
      // 连接建立之后执行send方法发送数据
      const actions = {
        token: this.nowWebsockData.token,
        url: this.nowWebsockData.url,
      };
      this.cmdStatus = true;
      this.initTerm();
    },
    websocketonerror(e) {
      this.$notify({
        title: this.$t("form.connectionEstablishmentFailed"),
        type: "warning",
        duration: 2500,
      });
      // 连接建立失败重连
    },
    websocketonmessage(e) {
      this.term.write(e.data);
    },
    websocketsend(Data) {
      // 数据发送
      this.websock.send(Data);
    },
    websocketclose(e) {
      // 关闭
    },
    // 日志
    initjournalWebSocket() {
      // 初始化weosocket
      const wsuri = this.nowWebjournalsockData.url;
      this.webjournalsock = new WebSocket(wsuri);
      this.webjournalsock.onmessage = this.webjournalsocketonmessage;
      this.webjournalsock.onopen = this.webjournalsocketonopen;
      this.webjournalsock.onerror = this.webjournalsocketonerror;
      this.webjournalsock.onclose = this.webjournalsocketclose;
    },
    webjournalsocketonopen() {
      // 连接建立之后执行send方法发送数据

      const actions = {
        token: this.nowWebjournalsockData.token,
        url: this.nowWebjournalsockData.url,
      };
    },
    webjournalsocketonerror(e) {
      // 连接建立失败重连
      this.$notify({
        title: this.$t("form.connectionEstablishmentFailed"),
        type: "warning",
        duration: 2500,
      });
    },
    webjournalsocketonmessage(e) {
      this.journalData = this.journalData + e.data;
      this.$nextTick(() => {
        this.$refs.journal.scrollTop = this.$refs.journal.scrollHeight;
      });
    },
    webjournalsocketsend(Data) {
      // 数据发送
      this.webjournalsock.send(Data);
    },
    webjournalsocketclose(e) {
      // 关闭
    },
    handleClose() {
      if (this.webjournalsock != "") {
        this.webjournalsock.close();
      }
      this.journaldialogVisible = false;
      this.journalData = "";
    },
  },
};
</script>

<style lang="scss" scoped>
.el-table .el-table__body tbody .red-row {
  color: #f56c6c;
}

::v-deep .el-dropdown-menu__item {
  padding: 0px 33px;
}

::v-deep .el-table .cell.el-tooltip {
  white-space: pre-wrap;
  min-width: 50px;
}

::v-deep .el-table .cell {
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  word-break: normal;
  line-height: 23px;
  padding-right: 10px;
}

.el-dropdown-menu__item {
  padding: 0px 0;
}
</style>
