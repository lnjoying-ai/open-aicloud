<template>
  <div>
    <div v-if="showContent" class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.region") + ":" }}</div>
          <el-select
            v-model="queryData.region"
            filterable
            :placeholder="$t('form.pleaseSelect')"
            style="width: 212px"
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
            filterable
            :placeholder="$t('form.pleaseSelect')"
            style="width: 212px"
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
          <div slot="label">{{ $t("form.node") + ":" }}</div>
          <el-select
            v-model="queryData.node_id"
            filterable
            :placeholder="$t('form.pleaseSelect')"
            style="width: 212px"
          >
            <el-option
              v-for="(item, index) in nodeList"
              :key="index"
              :label="item.node_name"
              :value="item.node_id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.status") + ":" }}</div>
          <el-select
            v-model="queryData.status"
            :placeholder="$t('form.pleaseSelect')"
            style="width: 212px"
          >
            <el-option
              v-for="(item, index) in newStatus"
              :key="index"
              :label="item.name"
              :value="item.code"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button class="add_search" size="small" @click="initform()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button size="mini" type="primary" @click="clickAddAppBtn">{{
            $t("form.add")
          }}</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <router-link to="/containerApplicationService/appTemplate">
            <el-button size="mini" type="primary">{{
              $t("form.templateAdd")
            }}</el-button>
          </router-link>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      :data="tableData.stacks"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column
        :label="$t('form.applicationName')"
        prop="name"
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
              style="color: #409eff"
              :to="
                '/containerApplicationService/serviceFromApp/' +
                scope.row.id +
                '/' +
                scope.row.name
              "
            >
              <span>{{ scope.row.name }}</span>
            </router-link>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.applicationTemplate')" prop="template">
        <template slot-scope="scope">
          {{ scope.row.template.name || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.versionNumber')" prop="version">
        <template slot-scope="scope">
          {{ scope.row.template.version || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.status')" prop="status">
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
          {{ getStatusName(scope.row.status.code) }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('form.serviceNumber')"
        prop="service_ids"
        sortable
      >
        <template slot-scope="scope">
          {{ scope.row.service_ids ? scope.row.service_ids.length : 0 }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="clickstartBtn(scope.row)">
                <i class="el-icon-video-play" />
                {{ $t("form.bootUp") }}
              </div>
              <div class="icon_cz" @click="clickRestartBtn(scope.row)">
                <i class="el-icon-refresh-right" />
                {{ $t("form.restart") }}
              </div>
              <div class="icon_cz" @click="clickstopBtn(scope.row)">
                <i class="el-icon-video-pause" />
                {{ $t("form.stop") }}
              </div>
              <div class="icon_cz" @click="clickDelBtn(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>
              <div class="icon_cz" @click="clickappConfig(scope.row)">
                <i class="el-icon-view" />
                {{ $t("form.viewConfig") }}
              </div>
              <a
                :href="
                  nowLocalhost +
                  '/api/aos/v1/docker/stacks/' +
                  scope.row.id +
                  '/compose-archives'
                "
              >
                <div class="icon_cz">
                  <i class="el-icon-download" />
                  {{ $t("form.exportConfig") }}
                </div>
              </a>

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
    <addApp ref="addApp" :sup_this="sup_this" :info="activeInfoApp" />
    <el-dialog
      :title="$t('form.applicationConfig')"
      :visible.sync="dialogVisible"
      width="800px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <div>
        <el-form label-width="130px">
          <el-form-item label="justice_compse:">
            <yaml-editor
              v-if="dialogVisible"
              ref="yamlEditor"
              v-model="appConfig.justice_compse"
              style="
                line-height: 1.2;
                max-height: 350px;
                overflow: auto;
                margin-top: 15px;
                border-radius: 4px;
                min-width: 100%;
              "
              :download-name="appConfig.name + '-justice_compse'"
              :download-type="'yml'"
              :readonly="'cursor'"
            />
          </el-form-item>
          <el-form-item label="stack_compose:">
            <yaml-editor
              v-if="dialogVisible"
              ref="yamlEditor"
              v-model="appConfig.stack_compose"
              style="
                line-height: 1.2;
                max-height: 350px;
                overflow: auto;
                margin-top: 15px;
                border-radius: 4px;
                min-width: 100%;
              "
              :download-name="appConfig.name + '-stack_compose'"
              :download-type="'yml'"
              :readonly="'cursor'"
            />
          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getDockerStacks,
  delDockerStacks,
  setDockerStacks,
  getSites,
  edges,
  getRegions,
  getDockerStacksConfig,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addApp from "./application/addForm";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: {
    addApp,
    YamlEditor,
  },
  mixins: [initData],
  data() {
    return {
      showContent: true,
      nowLocalhost: "",
      dialogVisible: false,
      appConfig: "",
      activeInfo: {},
      activeInfoApp: "",
      sup_this: this,
      tableData: {
        stacks: [],
      },
      queryData: {
        name: "",
        status: "",
        region: "",
        site: "",
        node_id: "",
        user_id: "",
        page_size: 10,
        page_num: 1,
      },
      node_id: "",
      arealist: [],
      website: [],
      nodeList: [],
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
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },

  watch: {
    nowText(val) {},
    "queryData.region": {
      deep: true,
      handler(val) {
        this.queryData.site = "";
        this.queryData.node_id = "";
        this.websiteinit();
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
    "queryData.site": {
      deep: true,
      handler(val) {
        this.queryData.node_id = "";

        this.edgesinit();
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
    "queryData.node_id": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
    "queryData.status": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
  },

  created() {},
  mounted() {
    this.nowLocalhost = window.location.origin;
  },
  methods: {
    getStatusCode(code) {
      return this.newStatus.find((item) => item.code == code).include;
    },
    getStatusName(code) {
      const item = this.newStatus.find((item) => item.include.includes(code));
      return item ? item.name : "-";
    },
    initform() {
      this.queryData = {
        name: "",
        status: "",
        region: "",
        site: "",
        node_id: "",
        user_id: "",
        page_size: 10,
        page_num: 1,
      };
      this.handleCurrentChange(1);
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
    async edgesinit() {
      const list = await edges({ site: this.queryData.site });
      this.nodeList = list.nodes;
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    async init(nodeId) {
      if (nodeId) {
        this.queryData["node_id"] = nodeId;
      }
      if (this.showContent) {
        this.areainit();
      }
      const list = await getDockerStacks(this.queryData);
      this.tableData = list;
    },

    clickAddAppBtn(item) {
      this.$refs.addApp.isAdd = true;
      this.$refs.addApp.dialog = true;
    },
    clickstartBtn(item) {
      // 启动
      this.$confirm(this.$t("form.confirmBootUp"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          setDockerStacks(item.id, { action: "start" })
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
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
    // 重启
    clickRestartBtn(item) {
      this.$confirm(this.$t("form.confirmRestart"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          setDockerStacks(item.id, { action: "restart" })
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
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
    clickstopBtn(item) {
      // 停止
      this.$confirm(this.$t("form.confirmStop"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          setDockerStacks(item.id, { action: "stop" })
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
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
    clickDelBtn(value) {
      this.$confirm(this.$t("form.confirmDelete"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          delDockerStacks(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
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
    clickappConfig(value) {
      getDockerStacksConfig(value.id)
        .then((res) => {
          this.dialogVisible = true;
          this.appConfig = res;
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    handleClose() {
      this.dialogVisible = false;
      this.appConfig = "";
    },
    clickEditBtn(info) {
      this.activeInfoApp = info;
      this.$refs.addApp.id = info.id;
      this.$refs.addApp.isAdd = false;
      this.$refs.addApp.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-select {
  display: inline-block;
  position: relative;
}
</style>
