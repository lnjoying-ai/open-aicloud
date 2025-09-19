<template>
  <div class="statistics1Page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="mb-2">
          <div slot="header" class="clearfix">
            <span class="font-bold">{{ $t("form.instance") }}</span>
          </div>
          <div style="margin: -15px 10px -5px">
            <div class="text-center pt-5 pb-3.5">
              <i
                class="iconfont icon-shebeileixing-copy text-blue-500 text-3xl mr-4"
              />
              <span class="text-3xl font-bold">{{
                statisticsMain.total_vms.success_status
                  ? statisticsMain.total_vms.success_status
                  : 0
              }}</span>
              <div style="font-size: 15px; margin-top: 10px; height: 45px">
                {{ $t("form.runningInstanceCount") }}
              </div>
              <div
                style="
                  font-size: 15px;
                  font-weight: 500;
                  margin-top: 15px;
                  margin-bottom: 25px;
                  cursor: pointer;
                "
              >
                <div
                  v-if="
                    Object.keys(
                      statisticsMain.total_vms.vm_instance_status_metrics
                    ).length != 0
                  "
                >
                  <el-popover
                    placement="top"
                    title=""
                    width="300"
                    popper-class="tablepopover"
                    trigger="hover"
                  >
                    <div
                      v-for="(value, key) in statisticsMain.total_vms
                        .vm_instance_status_metrics"
                      :key="key"
                    >
                      <p>{{ value.cloud_name }}</p>
                      <p
                        v-for="(values, keys) in value.status_metrics"
                        :key="keys"
                      >
                        <span>{{ keys + ":" }} </span><span>{{ values }}</span>
                      </p>
                    </div>
                    <div slot="reference">
                      <span
                        class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                      />
                      <span class="mr-1"
                        >{{ $t("form.failedInstanceCount") }}:</span
                      >
                      <span class="text-base">{{
                        statisticsMain.total_vms.failed_status
                          ? statisticsMain.total_vms.failed_status
                          : 0
                      }}</span>
                    </div>
                  </el-popover>
                </div>
                <div v-else>
                  <span
                    class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                  />
                  <span class="mr-1"
                    >{{ $t("form.failedInstanceCount") }}:</span
                  >
                  <span class="text-base">{{
                    statisticsMain.total_vms.failed_status
                      ? statisticsMain.total_vms.failed_status
                      : 0
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t("form.containerNode") }}</span>
          </div>
          <div style="margin: -15px 10px -5px">
            <div class="text-center pt-5 pb-3.5">
              <i
                class="iconfont icon-jiedianguanli_1 text-blue-500 text-3xl mr-4"
              />
              <span class="text-3xl font-bold">{{
                statisticsMain.total_nodes.success_status
                  ? statisticsMain.total_nodes.success_status
                  : 0
              }}</span>
              <div style="font-size: 15px; margin-top: 10px; height: 45px">
                {{ $t("form.onlineContainerNodeCount") }}
              </div>
              <div
                style="
                  font-size: 15px;
                  font-weight: 500;
                  margin-top: 15px;
                  margin-bottom: 25px;
                  cursor: pointer;
                "
              >
                <div>
                  <span
                    class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                  />
                  <span class="mr-1">{{ $t("form.offlineDeviceCount") }}</span>
                  <span class="text-base">{{
                    statisticsMain.total_nodes.failed_status
                      ? statisticsMain.total_nodes.failed_status
                      : 0
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t("form.cluster") }}</span>
          </div>
          <div style="margin: -15px 10px -5px">
            <div class="text-center pt-5 pb-3.5">
              <i class="iconfont icon-jiqun1 text-blue-500 text-3xl mr-4" />
              <span class="text-3xl font-bold">{{
                statisticsMain.total_clusters.success_status
                  ? statisticsMain.total_clusters.success_status
                  : 0
              }}</span>
              <div style="font-size: 15px; margin-top: 10px; height: 45px">
                {{ $t("form.successDeployedClusterCount") }}
              </div>
              <div
                style="
                  font-size: 15px;
                  font-weight: 500;
                  margin-top: 15px;
                  margin-bottom: 25px;
                  cursor: pointer;
                "
              >
                <div
                  v-if="
                    Object.keys(statisticsMain.total_clusters.status_metrics)
                      .length != 0
                  "
                >
                  <el-popover
                    placement="top"
                    title=""
                    width="300"
                    popper-class="tablepopover"
                    trigger="hover"
                  >
                    <p
                      v-for="(value, key) in statisticsMain.total_clusters
                        .status_metrics"
                      :key="key"
                    >
                      <span>{{ key + ":" }} </span><span>{{ value }}</span>
                    </p>
                    <div slot="reference">
                      <span
                        class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                      />
                      <span class="mr-1">{{
                        $t("form.failedClusterCount")
                      }}</span>
                      <span class="text-base">{{
                        statisticsMain.total_clusters.failed_status
                          ? statisticsMain.total_clusters.failed_status
                          : 0
                      }}</span>
                    </div>
                  </el-popover>
                </div>
                <div v-else>
                  <span
                    class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                  />
                  <span class="mr-1">{{ $t("form.failedClusterCount") }}</span>
                  <span class="text-base">{{
                    statisticsMain.total_clusters.failed_status
                      ? statisticsMain.total_clusters.failed_status
                      : 0
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>{{ $t("form.container") }}</span>
          </div>
          <div style="margin: -15px 10px -5px">
            <div class="text-center pt-5 pb-3.5">
              <i
                class="iconfont icon-rongqifuwu-copy text-blue-500 text-3xl mr-4"
              />
              <span class="text-3xl font-bold">{{
                statisticsMain.total_containers.success_status
                  ? statisticsMain.total_containers.success_status
                  : 0
              }}</span>
              <div style="font-size: 15px; margin-top: 10px; height: 45px">
                {{ $t("form.runningContainerCount") }}
              </div>
              <div
                style="
                  font-size: 15px;
                  font-weight: 500;
                  margin-top: 15px;
                  margin-bottom: 25px;
                  cursor: pointer;
                "
              >
                <div
                  v-if="
                    Object.keys(statisticsMain.total_containers.status_metrics)
                      .length != 0
                  "
                >
                  <el-popover
                    placement="top"
                    title=""
                    width="300"
                    popper-class="tablepopover"
                    trigger="hover"
                  >
                    <p
                      v-for="(value, key) in statisticsMain.total_containers
                        .status_metrics"
                      :key="key"
                    >
                      <span>{{ key + ":" }} </span><span>{{ value }}</span>
                    </p>
                    <div slot="reference">
                      <span
                        class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                      />
                      <span class="mr-1">{{
                        $t("form.failedContainerCount")
                      }}</span>
                      <span class="text-base">{{
                        statisticsMain.total_containers.failed_status
                          ? statisticsMain.total_containers.failed_status
                          : 0
                      }}</span>
                    </div>
                  </el-popover>
                </div>
                <div v-else>
                  <span
                    class="inline-block w-2 h-2 rounded-full bg-red-500 mr-2"
                  />
                  <span class="mr-1">{{
                    $t("form.failedContainerCount")
                  }}</span>
                  <span class="text-base">{{
                    statisticsMain.total_containers.failed_status
                      ? statisticsMain.total_containers.failed_status
                      : 0
                  }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div style="margin-top: 15px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ $t("form.platformAlarmCount") }}</span>
            </div>
            <div class="overflow-hidden">
              <virtualMachineEcharts
                ref="myChart1"
                :line-echart-i-d="'myChart1'"
                class="myChart"
                width="100%"
                height="27rem"
                :cur-data="alarmCurData"
              />
            </div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card class="box-card">
            <div slot="header" class="clearfix">
              <span>{{ $t("form.platformLogCount") }}</span>
            </div>
            <div class="overflow-hidden">
              <eventsEcharts
                ref="myChart2"
                :line-echart-i-d="'myChart2'"
                class="myChart"
                width="100%"
                height="27rem"
                :cur-data="eventsCurData"
              />
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getStatsCounts,
  getAlertLogsDaily,
  getApiLogsHourly,
} from "@/api/mainApi";
import virtualMachineEcharts from "./echarts/virtualMachineEcharts.vue";
import eventsEcharts from "./echarts/eventsEcharts.vue";

export default {
  components: { virtualMachineEcharts, eventsEcharts },
  computed: {
    ...mapGetters(["kind"]),
  },
  data() {
    return {
      statisticsMain: {
        total_containers: {
          status_metrics: {},
          success_status: 0,
          failed_status: 0,
        },
        total_clusters: {
          status_metrics: {},
          success_status: 0,
          failed_status: 0,
        },
        total_nodes: {
          success_status: 0,
          failed_status: 0,
        },
        total_vms: {
          success_status: 0,
          vm_instance_status_metrics: {},
          failed_status: 0,
        },
      },
      virtualMachineCurData: [],
      alarmCurData: [],
      eventsCurData: [],
      edgeNodeCurData: [],
      queryData: {
        start_date: "",
        end_date: "",
        resource_type: "",
      },
    };
  },
  watch: {},
  created() {
    this.getStatsCounts();
    this.getStatisticsEcharts();
  },
  mounted() {
    window.addEventListener("resize", this.onResize);
  },
  methods: {
    async getStatsCounts() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getStatsCounts(this.queryData);
      // 集群状态统计
      var clustersIndex = 0;
      list.total_clusters && list.total_clusters.status_metrics
        ? list.total_clusters.status_metrics.forEach((item, index) => {
            item.status.desc.en == "deployed" && item.count != 0
              ? (this.statisticsMain.total_clusters.success_status = item.count)
              : "";
            if (
              (item.status.desc.en == "deploy failed" ||
                item.status.desc.en == "Assigned failed" ||
                item.status.desc.en == "build failed" ||
                item.status.desc.en == "Remove failed") &&
              item.count != 0
            ) {
              clustersIndex == 0
                ? (this.statisticsMain.total_clusters.status_metrics[
                    item.status.desc.cn
                  ] = item.count)
                : (this.statisticsMain.total_clusters.status_metrics[
                    item.status.desc.cn
                  ] += item.count);
              clustersIndex++;
            }
          })
        : "";
      if (
        Object.keys(this.statisticsMain.total_clusters.status_metrics).length !=
        0
      ) {
        for (const [key, value] of Object.entries(
          this.statisticsMain.total_clusters.status_metrics
        )) {
          this.statisticsMain.total_clusters.failed_status += value;
        }
      }
      // 边缘计算节点
      list.total_nodes && list.total_nodes.status_metrics
        ? list.total_nodes.status_metrics.forEach((item, index) => {
            item.status.desc.en == "online" && item.count != 0
              ? (this.statisticsMain.total_nodes.success_status = item.count)
              : "";
            item.status.desc.en == "offline" && item.count != 0
              ? (this.statisticsMain.total_nodes.failed_status = item.count)
              : "";
          })
        : "";
      // 容器
      var containersIndex = 0;
      list.total_containers && list.total_containers.status_metrics
        ? list.total_containers.status_metrics.forEach((item, index) => {
            item.status.desc.en == "running" && item.count != 0
              ? (this.statisticsMain.total_containers.success_status +=
                  item.count)
              : "";
            if (
              (item.status.desc.en == "create failed" ||
                item.status.desc.en == "system abnormal" ||
                item.status.desc.en == "inst not exist") &&
              item.count != 0
            ) {
              containersIndex == 0
                ? (this.statisticsMain.total_containers.status_metrics[
                    item.status.desc.cn
                  ] = item.count)
                : (this.statisticsMain.total_containers.status_metrics[
                    item.status.desc.cn
                  ] += item.count);
              containersIndex++;
            }
          })
        : "";
      if (
        Object.keys(this.statisticsMain.total_containers.status_metrics)
          .length != 0
      ) {
        for (const [key, value] of Object.entries(
          this.statisticsMain.total_containers.status_metrics
        )) {
          this.statisticsMain.total_containers.failed_status += value;
        }
      }
      // 实例
      var vmNXIndex = 0;
      var vmEYIndex = 0;
      list.total_vms && list.total_vms.vm_instance_status_metrics
        ? list.total_vms.vm_instance_status_metrics.forEach((item, index) => {
            if (item.product == "NextStack") {
              this.statisticsMain.total_vms.vm_instance_status_metrics[
                item.cloud_name
              ] = {
                cloud_name: item.cloud_name,
                status_metrics: {},
              };
              item.status_metrics.forEach((el, indexs) => {
                el.status.desc.en == "INSTANCE_RUNNING" && el.count != 0
                  ? (this.statisticsMain.total_vms.success_status += el.count)
                  : "";
                el.status.desc.en == "INSTANCE_MONITOR_TAG_DONE" &&
                el.count != 0
                  ? (this.statisticsMain.total_vms.success_status += el.count)
                  : "";
                el.status.desc.en == "INSTANCE_MIGRATE_CLEAN" && el.count != 0
                  ? (this.statisticsMain.total_vms.success_status += el.count)
                  : "";
                if (
                  (el.status.desc.en == "INSTANCE_CREATE_FAILED" ||
                    el.status.desc.en == "INSTANCE_REMOVE_FAILED" ||
                    el.status.desc.en == "SNAP_SWITCH_FAILED" ||
                    el.status.desc.en == "INSTANCE_MIGRATE_FAILED") &&
                  item.count != 0
                ) {
                  vmNXIndex == 0
                    ? (this.statisticsMain.total_vms.vm_instance_status_metrics[
                        item.cloud_name
                      ].status_metrics[el.status.desc.cn] = el.count)
                    : (this.statisticsMain.total_vms.vm_instance_status_metrics[
                        item.cloud_name
                      ].status_metrics[el.status.desc.cn] += el.count);
                  vmNXIndex++;
                }
              });
            } else {
              this.statisticsMain.total_vms.vm_instance_status_metrics[
                item.cloud_name
              ] = {
                cloud_name: item.cloud_name,
                status_metrics: {},
              };
              item.status_metrics.forEach((el, indexs) => {
                el.status.desc.en == "active" && el.count != 0
                  ? (this.statisticsMain.total_vms.success_status += el.count)
                  : "";
                if (el.status.desc.en == "error" && el.count != 0) {
                  vmEYIndex == 0
                    ? (this.statisticsMain.total_vms.vm_instance_status_metrics[
                        item.cloud_name
                      ].status_metrics[el.status.desc.cn] = el.count)
                    : (this.statisticsMain.total_vms.vm_instance_status_metrics[
                        item.cloud_name
                      ].status_metrics[el.status.desc.cn] = el.count +=
                        el.count);
                  vmEYIndex++;
                }
              });
            }
          })
        : "";
      for (var key in this.statisticsMain.total_vms
        .vm_instance_status_metrics) {
        if (
          Object.keys(
            this.statisticsMain.total_vms.vm_instance_status_metrics[key]
              .status_metrics
          ).length == 0
        ) {
          delete this.statisticsMain.total_vms.vm_instance_status_metrics[key];
        } else {
          for (var keys in this.statisticsMain.total_vms
            .vm_instance_status_metrics[key].status_metrics) {
            var num =
              this.statisticsMain.total_vms.vm_instance_status_metrics[key]
                .status_metrics[keys];
            this.statisticsMain.total_vms.failed_status += num;
          }
        }
      }
    },
    async getStatisticsEcharts() {
      const alarmCurData = await getAlertLogsDaily();
      if (alarmCurData.daily_trends && alarmCurData.daily_trends.length > 0) {
        var alarmData = [];
        var keyData = {
          info: this.$t("echarts.statusLevel.info"),
          critical: this.$t("echarts.statusLevel.critical"),
          warn: this.$t("echarts.statusLevel.warn"),
          all: this.$t("echarts.statusLevel.all"),
        };
        alarmCurData.daily_trends.map((item, index) => {
          alarmData.push({
            alert_day: item.alert_day,
            alert_log_level_count: {
              [keyData.info]: item.alert_log_level_count.info_count,
              [keyData.critical]: item.alert_log_level_count.critical_count,
              [keyData.warn]: item.alert_log_level_count.warn_count,
              [keyData.all]: item.alert_log_level_count.all_count,
            },
          });
        });
        this.alarmCurData = alarmData;
      }
      const eventsCurData = await getApiLogsHourly();
      this.eventsCurData = eventsCurData.hourly_trends;
      this.eventsCurData.map((element) => {
        element.log_hour = element.log_hour.replace("-", "/");
      });
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-card__header {
  padding: 15px 30px;
  border: none;
}

::v-deep #expense .el-card__body {
  padding: 5px 20px 15px 20px;
}

::v-deep .el-card__body {
  padding: 15px 20px;
}

::v-deep .el-dropdown {
  display: block;
}

::v-deep .el-tabs__item {
  color: #7a7a7a;
}

::v-deep .el-tabs__item.is-active {
  color: #409eff;
}

svg {
  display: inline-block;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

.statistics1Page {
  margin: 0;

  ::v-deep .el-card__header {
    padding: 10px 20px;
    border: none;
    font-weight: bold;
  }

  .error {
    color: #d1200d;
    font-size: 14px;
    margin-right: 3px;
  }

  .error::before {
    display: inline-block;
    margin-right: 0px;
    content: "";
    width: 10px;
    height: 10px;
    background: #d1200d;
    border-radius: 50%;
  }

  .upgrade {
    color: #e6a23c;
    font-size: 14px;
    margin-right: 3px;
  }

  .upgrade::before {
    display: inline-block;
    margin-right: 0px;
    content: "";
    width: 10px;
    height: 10px;
    background: #e6a23c;
    border-radius: 50%;
  }
}
</style>
