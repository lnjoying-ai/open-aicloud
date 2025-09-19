<template>
  <div>
    <el-card>
      <div
        slot="header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <div>
          <span> {{ $t("form.nodeResourceTotalTop5") }} </span>
          <div
            style="
              display: inline-block;
              margin-left: 10px;
              font-size: 14px;
              color: #bfc0c0;
            "
          >
            ({{ currentDate }})
          </div>
          <div style="display: inline-block; margin-left: 10px">
            <el-button plain size="mini" @click="getTableData">
              {{ $t("form.refresh") }}
            </el-button>
          </div>
        </div>
        <div>
          <el-form
            ref="queryTableForm"
            size="small"
            :inline="true"
            :model="queryTableData"
            class="queryTableForm"
            label-width="100px"
            label-position="right"
          >
            <el-form-item
              :label="$t('form.sort') + ':'"
              style="margin-bottom: 0px"
            >
              <el-select
                v-model="queryTableData.condition"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                size="small"
                @change="handleConditionChange"
              >
                <el-option :label="$t('form.cpuUsageRate')" value="cpu" />
                <el-option :label="$t('form.memoryUsageRate')" value="mem" />
                <el-option
                  :label="$t('form.networkOutflowRate')"
                  value="network_out"
                />
                <el-option
                  :label="$t('form.networkInflowRate')"
                  value="network_in"
                />
                <el-option :label="$t('form.diskRead')" value="disk_read" />
                <el-option :label="$t('form.diskWrite')" value="disk_write" />
              </el-select>
            </el-form-item>
            <el-form-item
              :label="$t('form.timeInterval') + ':'"
              style="margin-bottom: 0px"
            >
              <el-select
                v-model="queryTableData.step"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                size="small"
                @change="handleTableStepChange"
              >
                <el-option
                  key="10s"
                  :label="$t('form.tenSeconds')"
                  value="10s"
                />
                <el-option key="1m" :label="$t('form.oneMinute')" value="1m" />
                <el-option
                  key="10m"
                  :label="$t('form.tenMinutes')"
                  value="10m"
                />
                <el-option key="1h" :label="$t('form.oneHour')" value="1h" />
              </el-select>
            </el-form-item>
          </el-form>
        </div>
      </div>
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="node_name" :label="$t('form.nodeInstance')">
          <template slot-scope="scope">
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
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.cpuUsageRate')">
          <template slot-scope="scope">
            <div v-if="scope.row.cpu_metrics">
              <div
                v-for="(value, key) in scope.row.cpu_metrics.utilisations"
                :key="key"
              >
                <p v-for="(values, keys) in value" :key="keys">
                  <span>{{ $script.processing_numbers(values) + " %" }}</span>
                </p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.memoryUsageRate')">
          <template slot-scope="scope">
            <div v-if="scope.row.mem_metrics">
              <div
                v-for="(value, key) in scope.row.mem_metrics.utilisations"
                :key="key"
              >
                <p v-for="(values, keys) in value" :key="keys">
                  <span>{{ $script.processing_numbers(values) + " %" }}</span>
                </p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="network_out"
          :label="$t('form.networkOutflowRate')"
        >
          <template slot-scope="scope">
            <div v-if="scope.row.network_metrics">
              <div
                v-for="(value, key) in scope.row.network_metrics.transmit"
                :key="key"
              >
                <p v-for="(values, keys) in value" :key="keys">
                  <span>{{
                    $script.processing_numbers(values) + " kbps"
                  }}</span>
                </p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="network_in"
          :label="$t('form.networkInflowRate')"
        >
          <template slot-scope="scope">
            <div v-if="scope.row.network_metrics">
              <div
                v-for="(value, key) in scope.row.network_metrics.recv"
                :key="key"
              >
                <p v-for="(values, keys) in value" :key="keys">
                  <span>{{
                    $script.processing_numbers(values) + " kbps"
                  }}</span>
                </p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.diskRead')">
          <template slot-scope="scope">
            <div v-if="scope.row.disk_metrics">
              <div
                v-for="(value, key) in scope.row.disk_metrics.disk_reads"
                :key="key"
              >
                <p v-for="(values, keys) in value" :key="keys">
                  <span>{{
                    $script.processing_numbers(values) + " kbps"
                  }}</span>
                </p>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.diskWrite')">
          <template slot-scope="scope">
            <div v-if="scope.row.disk_metrics">
              <div
                v-for="(value, key) in scope.row.disk_metrics.diskWrites"
                :key="key"
              >
                <p v-for="(values, keys) in value" :key="keys">
                  <span>{{
                    $script.processing_numbers(values) + " kbps"
                  }}</span>
                </p>
              </div>
            </div>
          </template>
        </el-table-column>

        <el-table-column width="125">
          <template slot-scope="scope">
            <span
              style="color: #409eff; cursor: pointer"
              @click="handleDetailChange(scope.row)"
              >{{ $t("form.viewDetails") }}</span
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <div class="statistics2Page">
      <el-card>
        <div slot="header" class="clearfix">
          <span> {{ $t("form.resourceMonitor") }} </span>
        </div>
        <div class="myChartList bg-white pt-3 pr-2" style="padding: 0px 20px">
          <el-form
            ref="ruleForm"
            size="small"
            :inline="true"
            :model="form"
            class="demo-ruleForm"
            label-width="50px"
            label-position="right"
          >
            <el-form-item :label="$t('form.type') + ':'">
              <el-select
                v-model="form.type"
                :placeholder="$t('form.pleaseSelect')"
                style="width: 130px"
                @change="handleTypeChange"
              >
                <el-option
                  key="node_id"
                  :label="$t('form.node')"
                  value="node_id"
                />
                <el-option
                  key="inst_id"
                  :label="$t('form.container')"
                  value="inst_id"
                />
                <el-option
                  key="resource_gpu"
                  :label="$t('form.gpu')"
                  value="resource_gpu"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              v-show="form.type != 'all'"
              :label="
                form.type == 'inst_id'
                  ? $t('form.container') + ':'
                  : form.type == 'node_id' || form.type == 'resource_gpu'
                  ? $t('form.node') + ':'
                  : ''
              "
            >
              <el-select
                v-model="form.id"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                style="width: 130px"
                @change="handleIDChange"
              >
                <el-option
                  v-for="(item, index) in nowListData"
                  :key="index"
                  :label="item.name || item.node_name"
                  :value="item.ref_id || item.node_id"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('form.time') + ':'">
              <el-date-picker
                v-model="form.time"
                type="datetimerange"
                :picker-options="pickerOptions"
                :range-separator="$t('form.to')"
                :start-placeholder="$t('form.startDate')"
                :end-placeholder="$t('form.endDate')"
                format="yyyy-MM-dd HH:mm:ss"
                style="width: 370px"
                @change="handleTimeChange"
              />
            </el-form-item>
            <el-form-item
              :label="$t('form.timeInterval') + ':'"
              label-width="100px"
            >
              <el-select
                v-model="form.step"
                :placeholder="$t('form.pleaseSelect')"
                style="width: 130px"
                @change="handleStepChange"
              >
                <el-option
                  key="10s"
                  :label="$t('form.tenSeconds')"
                  value="10s"
                />
                <el-option
                  key="10m"
                  :label="$t('form.tenMinutes')"
                  value="10m"
                />
                <el-option key="1h" :label="$t('form.oneHour')" value="1h" />
                <el-option key="1d" :label="$t('form.oneDay')" value="1d" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button size="small" @click="reset()">{{
                $t("form.reset")
              }}</el-button>
            </el-form-item>
          </el-form>
          <div v-if="url">
            <iframe width="100%" height="600px" :src="url" frameborder="0" />
          </div>
          <div v-else>
            <el-empty :description="$t('form.noData')" />
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  edges,
  instances,
  statsNodesSort,
  getMonitorLink,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
export default {
  components: {},
  mixins: [initData],
  computed: {
    ...mapGetters(["kind"]),
  },
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: this.$t("message.lastTenMinutes"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 60 * 1000 * 10);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThirtyMinutes"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 60 * 1000 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneHour"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastSixHours"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 6);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastTwelveHours"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 12);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneDay"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThreeDays"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneWeek"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneMonth"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThreeMonths"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      queryTableData: {
        condition: "cpu",
        sort: "desc",
        limit: 5,
        step: "10s",
      },
      pageloading: false,
      nowListData: [], // 当前列表数据
      nodeListData: [], // 节点列表
      gpuListData: [], // gpu节点列表
      instanceListData: [], // 实例列表
      tableData: [],
      currentDate: "",
      form: {
        id: "",
        name: "",
        type: "node_id",
        time: [
          new Date(new Date().setHours(new Date().getHours() - 6)),
          new Date(),
        ],
        step: "10m",
        filter: " ",
      },
      timer: null,
      monitorLink: [],
      url: "",
    };
  },

  watch: {},

  created() {},
  mounted() {
    this.initData();
    this.getMonitorLink();
    this.getTableData();
    this.timer = setInterval(() => {
      this.getTableData(); // 加载数据函数
    }, 60000);
  },
  beforeDestroy() {
    this.clear();
  },
  methods: {
    handleConditionChange(value) {
      this.queryTableData.condition = value;
      this.getTableData();
    },
    handleTableStepChange(value) {
      this.queryTableData.step = value;
      this.getTableData();
    },
    async getMonitorLink() {
      var url = "";
      if (this.form.type == "node_id") {
        url = "?type=node";
      } else if (this.form.type == "inst_id") {
        url = "?type=container";
      } else if (this.form.type == "resource_gpu") {
        url = "?type=resource_gpu";
      }
      const res = await getMonitorLink(url);
      this.monitorLink = res.links;
      this.getIframeUrl();
    },
    getIframeUrl() {
      var urlOrigin = window.location.origin;
      var from = "";
      var to = "";
      this.form.time ? (from = this.form.time[0].getTime()) : "";
      this.form.time ? (to = this.form.time[1].getTime()) : "";
      (this.monitorLink &&
        this.monitorLink.length > 0 &&
        this.form.id &&
        this.form.type == "node_id") ||
      this.form.type == "resource_gpu"
        ? (this.url =
            urlOrigin +
            this.monitorLink[0].link +
            "?orgId=1&kiosk&var-node_id=" +
            this.form.id +
            "&var-nodename=" +
            this.form.name +
            "&var-interval=" +
            this.form.step +
            "&from=" +
            from +
            "&to=" +
            to)
        : "";
      this.monitorLink &&
      this.monitorLink.length > 0 &&
      this.form.id &&
      this.form.type == "inst_id"
        ? (this.url =
            urlOrigin +
            this.monitorLink[0].link +
            "?orgId=1&kiosk&var-container_ref_id=" +
            this.form.id +
            "&var-container_name=" +
            this.form.name +
            "&var-interval=" +
            this.form.step +
            "&from=" +
            from +
            "&to=" +
            to)
        : "";
    },

    handleDetailChange(row) {
      this.form.type = "node_id";
      this.nowListData = this.nodeListData;
      this.form.id = row.node_id;
      this.getMonitorLink();
    },
    // 停止定时器
    clear() {
      clearInterval(this.timer); // 清除计时器
      this.timer = null; // 设置为null
    },
    async getTableData() {
      // 获取当前时间
      this.currentDate = new Date().toLocaleString();
      const tableData = await statsNodesSort(this.queryTableData);
      this.tableData = tableData.node_metrics;
    },

    // 类型切换触发
    handleTypeChange(value) {
      this.form.type = value;
      this.form.id = "";
      if (value == "node_id") {
        this.nowListData = this.nodeListData;
        this.nowListData && this.nowListData.length > 0
          ? (this.form.id = this.nowListData[0].node_id)
          : "";
        this.form.name = this.nowListData[0].node_name;
      } else if (value == "inst_id") {
        this.nowListData = this.instanceListData;
        this.nowListData && this.nowListData.length > 0
          ? (this.form.id = this.nowListData[0].ref_id)
          : "";
        this.form.name = this.nowListData[0].name;
      } else if (value == "resource_gpu") {
        this.nowListData = this.gpuListData;
        this.nowListData && this.nowListData.length > 0
          ? (this.form.id = this.nowListData[0].node_id)
          : "";
        this.form.name = this.nowListData[0].node_name;
      }
      this.getMonitorLink();
    },
    // ID
    handleIDChange(value) {
      this.form.id = value;
      if (this.form.type == "inst_id") {
        var data = this.nowListData.filter((item, index) => {
          return item.ref_id == value;
        });
        this.form.name = data[0].name;
      } else if (
        this.form.type == "node_id" ||
        this.form.type == "resource_gpu"
      ) {
        var data = this.nowListData.filter((item, index) => {
          return item.node_id == value;
        });
        this.form.name = data[0].node_name;
      }
      this.getIframeUrl();
    },
    // 间隔时间
    handleStepChange(value) {
      this.form.step = value;
      this.getIframeUrl();
    },
    // 时间
    handleTimeChange(value) {
      this.form.time = value;
      this.getIframeUrl();
    },
    reset() {
      this.nowListData = this.nodeListData; // 初始化列表数据
      this.form.type = "node_id";
      this.nowListData.length > 0
        ? (this.form.id = this.nowListData[0].node_id)
        : (this.form.id = "");
      this.form.time = [
        new Date(new Date().setHours(new Date().getHours() - 6)),
        new Date(),
      ];
      this.form.step = "10m";
      this.getMonitorLink();
    },
    async initData() {
      const params = {
        gpu: "",
      };
      const nodeData = await edges();
      this.nodeListData = nodeData.nodes;
      const instancesData = await instances();
      this.instanceListData = instancesData.containers;
      const gpuNodeData = await edges(params);
      this.gpuListData = gpuNodeData.nodes;
      if (this.form.type == "node_id") {
        this.nodeListData && this.nodeListData.length > 0
          ? (this.nowListData = this.nodeListData)
          : "";
      } else if (this.form.type == "inst_id") {
        this.instanceListData && this.instanceListData.length > 0
          ? (this.nowListData = this.instanceListData)
          : "";
      } else if (this.form.type == "resource_gpu") {
        this.gpuListData && this.gpuListData.length > 0
          ? (this.nowListData = this.gpuListData)
          : "";
      }
      if (this.form.type == "inst_id") {
        this.nowListData.length > 0 && this.form.id == this.nowListData[0]
          ? this.nowListData[0].ref_id
          : "";
        this.nowListData.length > 0 && this.form.name == this.nowListData[0]
          ? this.nowListData[0].name
          : "";
      } else {
        this.nowListData.length > 0 && this.form.id == this.nowListData[0]
          ? this.nowListData[0].node_id
          : "";
        this.nowListData.length > 0 && this.form.name == this.nowListData[0]
          ? this.nowListData[0].node_name
          : "";
      }
    },
    getTime(item) {
      // 时间戳转换年月日时分秒
      var date = new Date(item);
      var Y = date.getFullYear();
      var M = date.getMonth() + 1;
      var D = date.getDate();
      var h = date.getHours();
      var m = date.getMinutes();
      var s = date.getSeconds();
      return (
        Y +
        "-" +
        this.getTime0(M) +
        "-" +
        this.getTime0(D) +
        " " +
        this.getTime0(h) +
        ":" +
        this.getTime0(m) +
        ":" +
        this.getTime0(s)
      );
    },
    getTime0(item) {
      // 补0
      return item < 10 ? "0" + item : item;
    },
  },
};
</script>

<style lang="scss" scoped>
.statistics2Page {
  margin: 15px 0px 0px 0px;
  border-radius: 4px;

  ::v-deep .el-card__body {
    padding: 0;
  }

  ::v-deep .el-card {
    border: 0px solid #ebeef5;
  }
}
</style>
