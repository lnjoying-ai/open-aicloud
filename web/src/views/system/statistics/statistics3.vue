<template>
  <div>
    <div class="statistics2Page">
      <el-card>
        <div class="myChartList bg-white pt-3 pr-2" style="padding: 0px 20px">
          <el-tabs v-model="activemonitor">
            <el-tab-pane :label="$t('form.instance')" name="first" />

            <el-tab-pane
              v-if="kind == 0 || kind == 1"
              :label="$t('form.computeNode')"
              name="second"
            />
          </el-tabs>
          <el-form
            ref="ruleForm"
            size="small"
            :inline="true"
            :model="form"
            class="demo-ruleForm"
            label-width="100px"
            label-position="right"
          >
            <el-form-item
              v-if="form.type == 'node'"
              :label="$t('form.node') + ':'"
              label-width="80px"
            >
              <el-select
                v-model="form.manageIp"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                style="width: 150px"
                @change="handleNodeChange"
              >
                <el-option
                  v-for="(item, index) in nodeListData"
                  :key="index"
                  :label="item.name"
                  :value="item.manageIp"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              v-if="form.type == 'virtual'"
              :label="$t('form.instance') + ':'"
              label-width="80px"
            >
              <el-select
                v-model="form.virtualId"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                style="width: 150px"
                @change="handleVirtualChange"
              >
                <el-option
                  v-for="(item, index) in virtualData"
                  :key="index"
                  :label="item.name"
                  :value="item.instanceId"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('form.timeRange') + ':'">
              <el-date-picker
                v-model="form.time"
                type="datetimerange"
                :picker-options="pickerOptions"
                :range-separator="$t('form.to')"
                :start-placeholder="$t('form.startDate')"
                :end-placeholder="$t('form.endDate')"
                format="yyyy-MM-dd HH:mm:ss"
                @change="handleTimeChange"
              />
            </el-form-item>
            <el-form-item>
              <el-button size="small" @click="reset()">
                {{ $t("form.reset") }}
              </el-button>
            </el-form-item>
          </el-form>
          <el-tabs v-model="tabsActive">
            <el-tab-pane
              v-if="activemonitor == 'first'"
              :label="$t('form.basicInfo')"
              name="vmFirst"
            >
              <div>
                <div v-if="vmUrl">
                  <iframe
                    width="100%"
                    :src="vmUrl"
                    frameborder="0"
                    style="height: 78vh"
                  />
                </div>
                <div v-else>
                  <el-empty description="暂无数据" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane
              v-if="isShowVmGPU && activemonitor == 'first'"
              label="GPU"
              name="vmSecond"
            >
              <div>
                <div v-if="vmGpuUrl">
                  <iframe
                    width="100%"
                    :src="vmGpuUrl"
                    frameborder="0"
                    style="height: 78vh"
                  />
                </div>
                <div v-else>
                  <el-empty description="暂无数据" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane
              v-if="activemonitor == 'second'"
              :label="$t('form.basicInfo')"
              name="nodeFirst"
            >
              <div>
                <div v-if="nodeUrl">
                  <iframe
                    width="100%"
                    :src="nodeUrl"
                    frameborder="0"
                    style="height: 78vh"
                  />
                </div>
                <div v-else>
                  <el-empty :description="$t('form.noData')" />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane
              v-if="activemonitor == 'second'"
              :label="$t('form.gpu')"
              name="nodeSecond"
            >
              <div>
                <div v-if="nodePciUrl">
                  <iframe
                    width="100%"
                    :src="nodePciUrl"
                    frameborder="0"
                    style="height: 78vh"
                  />
                </div>
                <div v-else>
                  <el-empty :description="$t('form.noData')" />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </div>
      </el-card>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import { getMonitorLink } from "@/api/mainApi";
import initData from "@/mixins/initData";
import store from "@/store";

export default {
  components: {},
  mixins: [initData],
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

      form: {
        type: "virtual",
        manageIp: "",
        virtualId: "",
        time: [
          new Date(new Date().setHours(new Date().getHours() - 6)),
          new Date(),
        ],
      },

      activemonitor: "first",
      tabsActive: "vmFirst",
      isShowVmGPU: false,

      vmUrl: "", // 实例监控
      vmGpuUrl: "", // GPU监控
      nodeUrl: "", // 节点监控
      nodePciUrl: "", // 节点GPU监控

      vmMonitorLink: "", // 实例监控
      gpuMonitorLink: "", // GPU监控
      nodeMonitorLink: "", // 节点监控

      nodeListData: [], // 节点列表
      virtualData: [], // 实例列表
    };
  },
  watch: {
    activemonitor(newValue, oldValue) {
      if (newValue == "first") {
        this.tabsActive = "vmFirst";
        this.getvmsinstabcesList();
      } else if (newValue == "second") {
        this.tabsActive = "nodeFirst";
        this.getvmsHypervisorNodesList();
      } else {
      }
    },
  },
  created() {
    this.initData();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    handleTypeChange(value) {
      this.form.type = value;
      this.vmUrl = "";
      this.vmGpuUrl = "";
      this.nodeUrl = "";
      this.nodePciUrl = "";
      if (
        (value == "node" && this.nodeListData.length == 0) ||
        (value == "virtual" && this.virtualData.length == 0)
      ) {
        return;
      }
      // this.form.manageIp = "";
      // if (value == "node") {
      //   this.form.virtualId = "";
      // }
      // value == "node"
      //   ? this.nodeListData && this.nodeListData.length > 0
      //     ? (this.form.manageIp = this.nodeListData[0].manageIp)
      //     : ""
      //   : this.virtualData && this.virtualData.length > 0 && !this.form.virtualId
      //     ? (this.form.virtualId = this.virtualData[0].instanceId)
      //     : "";
      this.getIframeUrl();
    },
    handleNodeChange(value) {
      this.form.manageIp = value;
      this.getIframeUrl();
    },
    handleVirtualChange(value) {
      // this.form.virtualId = value;
      this.getVmDetail(value);
      this.getIframeUrl();
    },
    getVmDetail(id) {
      // 获取详情
      mainApi
        .vmsInstabcesDetail(id)
        .then((res) => {
          this.isShowVmGPU = !!(res.pciInfos && res.pciInfos.length > 0);
          if (!this.isShowVmGPU) {
            this.activemonitor = "first";
          }
        })
        .catch((error) => {});
    },
    // 时间
    handleTimeChange(value) {
      this.form.time = value;
      this.getIframeUrl();
    },
    getIframeUrl() {
      var urlOrigin = window.location.origin;
      var from = "";
      var to = "";
      this.form.time ? (from = this.form.time[0].getTime()) : "";
      this.form.time ? (to = this.form.time[1].getTime()) : "";

      if (
        this.nodeMonitorLink &&
        this.form.type == "node" &&
        this.form.manageIp
      ) {
        this.nodeUrl =
          urlOrigin +
          this.nodeMonitorLink.link +
          "?orgId=1&kiosk&var-node=" +
          this.form.manageIp +
          "&var-cloud_id=" +
          this.$store.state.app.hxcloudData.cloud_id +
          "&from=" +
          from +
          "&to=" +
          to;
      }
      if (
        this.gpuMonitorLink &&
        this.form.type == "node" &&
        this.form.manageIp
      ) {
        this.nodePciUrl =
          urlOrigin +
          this.gpuMonitorLink.link +
          "?orgId=1&kiosk&var-node=" +
          this.form.manageIp.split(":")[0] +
          ":9835" +
          "&var-cloud_id=" +
          this.$store.state.app.hxcloudData.cloud_id +
          "&from=" +
          from +
          "&to=" +
          to;
      }
      if (
        this.vmMonitorLink &&
        this.form.type == "virtual" &&
        this.form.virtualId
      ) {
        this.vmUrl =
          urlOrigin +
          this.vmMonitorLink.link +
          "?orgId=1&kiosk&var-vm_instance_id=" +
          this.form.virtualId +
          "&var-cloud_id=" +
          this.$store.state.app.hxcloudData.cloud_id +
          "&var-node=All" +
          "&from=" +
          from +
          "&to=" +
          to;
      }
      if (
        this.gpuMonitorLink &&
        this.form.type == "virtual" &&
        this.form.virtualId
      ) {
        this.vmGpuUrl =
          urlOrigin +
          this.gpuMonitorLink.link +
          "?orgId=1&kiosk&var-vm_instance_id=" +
          this.form.virtualId +
          "&var-cloud_id=" +
          this.$store.state.app.hxcloudData.cloud_id +
          "&var-node=All" +
          "&from=" +
          from +
          "&to=" +
          to;
      }
    },

    reset() {
      this.virtualData && this.virtualData.length > 0
        ? (this.form.virtualId = this.virtualData[0].instanceId)
        : "";
      this.nodeListData && this.nodeListData.length > 0
        ? (this.form.manageIp = this.nodeListData[0].manageIp)
        : "";
      this.form.time = [
        new Date(new Date().setHours(new Date().getHours() - 6)),
        new Date(),
      ];
      this.getIframeUrl();
    },
    async initData() {
      var url = "?type=nextstack&type=gpu";
      const res = await getMonitorLink(url);
      this.gpuMonitorLink =
        res.links && res.links.length > 0 ? res.links[0] : "";
      this.getvmsinstabcesList();
    },
    async getvmsinstabcesList() {
      // 获取实例列表
      console.log(3);

      if (this.virtualData && this.virtualData.lenght > 0) {
        this.handleTypeChange("virtual");
        return;
      }
      var url = "?type=libvirt";
      const res = await getMonitorLink(url);
      this.vmMonitorLink =
        res.links && res.links.length > 0 ? res.links[0] : "";
      const list = await mainApi.vmsInstabcesList();
      this.virtualData = list.vmInstancesInfo || [];
      if (this.virtualData && this.virtualData.length > 0) {
        this.form.virtualId = this.virtualData[0].instanceId;
        this.getVmDetail(this.form.virtualId);
      }
      this.handleTypeChange("virtual");
    },
    async getvmsHypervisorNodesList() {
      if (this.nodeListData && this.nodeListData.lenght > 0) {
        this.handleTypeChange("node");
        return;
      }
      var url = "?type=hypervisor_node";
      const res = await getMonitorLink(url);
      this.nodeMonitorLink =
        res.links && res.links.length > 0 ? res.links[0] : "";
      const nodeData = await mainApi.vmsHypervisorNodesList();
      this.nodeListData = nodeData.nodeAllocationInfos || [];
      if (this.nodeListData && this.nodeListData.length > 0) {
        this.nodeListData.forEach((item, index) => {
          item.manageIp = item.manageIp + ":9100";
        });
        this.form.manageIp = this.nodeListData[0].manageIp;
      }
      this.handleTypeChange("node");
    },
  },
};
</script>
<style lang="scss" scoped>
.statistics2Page {
  margin: 0px 0px 0px 0px;
  border-radius: 4px;

  ::v-deep .el-card__body {
    padding: 0;
  }

  ::v-deep .el-card {
    border: 0px solid #ebeef5;
  }
}
</style>
