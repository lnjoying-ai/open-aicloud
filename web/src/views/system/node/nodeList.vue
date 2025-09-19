<!--
 * @Author: your name
 * @Date: 2021-12-09 08:44:54
 * @LastEditTime: 2021-12-09 18:31:15
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \MR\src\views\system\node\nodeList.vue
-->
<template>
  <div class="mt-2">
    <div>
      <el-card class="mb-2">
        <div slot="header" class="clearfix">
          <span>{{ $t("form.nodeDetail") }}</span>
        </div>
        <el-form v-if="detailMain" ref="form" size="small" label-width="140px">
          <el-row>
            <el-col :span="12">
              <el-form-item
                v-show="
                  detailMain.labels['com.justice.node/orchestration'] == 'k8s'
                "
                :label="$t('form.clusterName') + ':'"
              >
                <router-link
                  style="color: #409eff"
                  :to="
                    '/serverCluster/detail/' +
                    clustersNodeData.cluster_id +
                    '/' +
                    clustersNodeData.cluster_name
                  "
                >
                  {{ clustersNodeData.cluster_name }}
                </router-link>
              </el-form-item>
              <el-form-item
                v-show="
                  detailMain.labels['com.justice.node/orchestration'] == 'k8s'
                "
                :label="$t('form.clusterType') + ':'"
              >
                <span>{{
                  clustersNodeData.cluster_type
                    ? clustersNodeData.cluster_type
                    : "-"
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.nodeName') + ':'">
                <span>{{
                  detailMain.node_name ? detailMain.node_name : "-"
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.nodeId') + ':'">
                <span>{{ detailMain.node_id ? detailMain.node_id : "-" }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.nodeType') + ':'">
                <span>{{
                  detailMain.labels["com.justice.node/orchestration"] || "-"
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.dockerVersion') + ':'">
                <el-row
                  v-for="(item, index) in detailMain.dev_info.software_info"
                  :key="index"
                >
                  {{ item.version || "-" }}
                </el-row>
              </el-form-item>
              <el-form-item :label="$t('form.status') + ':'">
                <span>{{
                  detailMain.online_status == 1
                    ? $t("form.online")
                    : $t("form.offline")
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.activeStatus') + ':'">
                <div v-if="detailMain.active_status == 0" class="error">
                  <span>{{ $t("form.notActivated") }}</span>
                </div>
                <div v-if="detailMain.active_status == 1" class="success">
                  <span>{{ $t("form.activated") }}</span>
                </div>
                <div v-if="detailMain.active_status == 2" class="upgrade">
                  <span>{{ $t("form.upgrading") }}</span>
                  <p>
                    {{
                      $getStatus.getNodeUpgradeStatus(detailMain.upgrade_status)
                    }}
                  </p>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('form.hostName') + ':'">
                <span>{{
                  detailMain.host_name ? detailMain.host_name : "-"
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.version') + ':'">
                <span>{{
                  detailMain.core_version ? detailMain.core_version : "-"
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.organization') + ':'">
                <span>{{ detailMain.bp_name ? detailMain.bp_name : "-" }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.region') + ':'">
                <span>{{
                  detailMain.region_name ? detailMain.region_name : "-"
                }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.site') + ':'">
                <span>{{
                  detailMain.site_name ? detailMain.site_name : "-"
                }}</span>
              </el-form-item>
              <el-form-item
                v-if="
                  detailMain.labels &&
                  detailMain.labels['com.justice.node/ip:external']
                "
                :label="$t('form.publicNetworkAddress') + ':'"
              >
                <span>{{
                  detailMain.labels["com.justice.node/ip:external"]
                }}</span>
              </el-form-item>
              <el-form-item
                v-if="
                  detailMain.labels &&
                  detailMain.labels['com.justice.node/ip:internal']
                "
                :label="$t('form.internalNetworkAddress') + ':'"
              >
                <span>{{
                  detailMain.labels["com.justice.node/ip:internal"]
                }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item :label="$t('form.networkCard') + ':'" class="noflex">
            <el-row
              v-for="(item, index) in detailMain.dev_info.network_info"
              :key="index"
              style="width: 750px"
            >
              <el-col :span="9">nic_name:{{ item.nic_name }}</el-col>
              <el-col :span="7">ipv4:{{ item.ipv4 }}</el-col>
              <el-col :span="8">ipv6:{{ item.ipv6 }}</el-col>
            </el-row>
          </el-form-item>
        </el-form>
      </el-card>
      <el-card class="mb-2">
        <div slot="header" class="clearfix relative">
          <span>{{ $t("form.monitor") }}</span>
          <el-switch
            v-model="detailMain.labels.NO_MONITOR"
            class="ml-8"
            @change="handleSwitchChange"
          />
          <small class="ml-2">{{ $t("form.enableMonitorComponent") }}</small>

          <!-- </span> <span style="font-size:15px; margin-left:15px;">上次更新时间 : {{ updateDate ?dateTrans(updateDate) :"-"}}</span> -->
          <router-link
            :to="'/statistics?type=monitor&nodeId=' + detailMain.node_id"
            class="absolute right-0 top-0"
          >
            <el-button type="primary" size="mini" class="mx-2">{{
              $t("form.statisticsAnalysis")
            }}</el-button>
          </router-link>
        </div>
        <div v-if="detailMain.labels.NO_MONITOR">
          <el-row v-if="url" :gutter="15" class="mb-2">
            <el-col :span="24">
              <iframe width="100%" height="670px" :src="url" frameborder="0" />
            </el-col>
          </el-row>
          <el-row v-if="gpuUrl" :gutter="15" class="mb-2">
            <el-col :span="24">
              <iframe
                width="100%"
                height="337.5px"
                :src="gpuUrl"
                class="mt-2"
                frameborder="0"
              />
            </el-col>
          </el-row>
          <el-row v-if="url == '' && gpuUrl == ''" :gutter="15" class="mb-2">
            <el-col :span="24">
              <el-empty description="暂无数据" style="height: 300px"
            /></el-col>
          </el-row>
        </div>
      </el-card>
      <el-card
        v-if="
          detailMain.labels &&
          detailMain.labels['com.justice.node/orchestration'] &&
          detailMain.labels['com.justice.node/orchestration'] != 'k8s'
        "
        class="mb-2"
      >
        <div slot="header" class="clearfix">
          <span>{{ $t("form.container") }}</span>
        </div>
        <container-list
          ref="containerList"
          :api-type="'node'"
          :node-id="$route.params.id"
          style="margin-top: -58px"
        />
      </el-card>
      <el-card
        v-if="
          detailMain.labels &&
          detailMain.labels['com.justice.node/orchestration'] &&
          detailMain.labels['com.justice.node/orchestration'] != 'k8s'
        "
        class="mb-2"
      >
        <div slot="header" class="clearfix">
          <span>{{ $t("form.application") }}</span>
        </div>
        <application ref="application" />
      </el-card>
    </div>
  </div>
</template>
<script>
import containerList from "@/views/system/container";
import application from "@/views/system/application";
import {
  edgesDetail,
  getClustersToNodes,
  getMonitorLink,
  updateEdgesDetail,
} from "@/api/mainApi";

export default {
  components: { containerList, application },
  data() {
    return {
      params: {},
      detailMain: "",
      clustersNodeData: "", // 集群节点数据
      updateDate: "", // 更新时间
      url: "",
      gpuUrl: "",
    };
  },
  created() {
    this.init(this.$route.params.id);
  },
  mounted() {
    this.params = this.$route.params;
  },
  methods: {
    handleSwitchChange(value) {
      this.detailMain.labels.NO_MONITOR = value;
      if (!this.detailMain.labels.NO_MONITOR) {
        if (this.detailMain.dev_info.gpu_info.length > 0) {
          this.getGPUMonitorLink();
          this.getMonitorLink();
        } else {
          this.getMonitorLink();
        }
      }
      this.updateEdgesDetail();
    },
    // 更新节点信息
    async updateEdgesDetail() {
      const params = {
        labels: {
          NO_MONITOR: !this.detailMain.labels.NO_MONITOR,
        },
      };
      const res = await updateEdgesDetail(this.detailMain.node_id, params);
      this.init(this.$route.params.id);
    },
    async getMonitorLink() {
      var urlOrigin = window.location.origin;
      var url = "?type=node_detail";
      const res = await getMonitorLink(url);
      res.links.length > 0
        ? (this.url =
            urlOrigin +
            res.links[0].link +
            "?orgId=1&kiosk&var-node_id=" +
            this.detailMain.node_id +
            "&var-nodename=" +
            this.detailMain.node_name)
        : (this.url = "");
    },
    async getGPUMonitorLink() {
      var urlOrigin = window.location.origin;
      var url = "?type=resource_gpu_detail";
      const res = await getMonitorLink(url);

      res.links.length > 0
        ? (this.gpuUrl =
            urlOrigin +
            res.links[0].link +
            "?orgId=1&kiosk&var-node_id=" +
            this.detailMain.node_id +
            "&var-nodename=" +
            this.detailMain.node_name)
        : (this.gpuUrl = "");
    },

    // 封装的时间戳转换日期函数
    dateTrans(value) {
      if (value == null) {
        return;
      }
      const _date = new Date(parseInt(value));
      const y = _date.getFullYear();
      let m = _date.getMonth() + 1;
      m = m < 10 ? "0" + m : m;
      let d = _date.getDate();
      d = d < 10 ? "0" + d : d;
      let h = _date.getHours();
      h = h < 10 ? "0" + h : h;
      let minute = _date.getMinutes();
      let second = _date.getSeconds();
      minute = minute < 10 ? "0" + minute : minute;
      second = second < 10 ? "0" + second : second;
      const dates =
        y + "-" + m + "-" + d + " " + h + ":" + minute + ":" + second;
      return dates;
    },
    async init(id) {
      // 请求容器详情
      const list = await edgesDetail(id);
      this.detailMain = list;
      if (this.detailMain.labels["com.justice.node/orchestration"] == "k8s") {
        this.toClustersToNodes();
      } else {
        this.$nextTick(() => {
          this.$refs.application.showContent = false;
          this.$refs.application.init(this.params.id);
        });
      }
      if (
        this.detailMain.labels.NO_MONITOR &&
        (this.detailMain.labels.NO_MONITOR == "false" ||
          this.detailMain.labels.NO_MONITOR == false)
      ) {
        if (this.detailMain.dev_info.gpu_info.length > 0) {
          this.getGPUMonitorLink();
          this.getMonitorLink();
        } else {
          this.getMonitorLink();
        }
        this.detailMain.labels.NO_MONITOR = true;
      } else {
        this.detailMain.labels.NO_MONITOR = false;
      }
    },
    async toClustersToNodes() {
      const list = await getClustersToNodes(this.$route.params.id);
      this.clustersNodeData = list;
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-card__header {
  padding: 10px 20px;
}

.noflex {
  ::v-deep .el-form-item__content {
    display: block;
  }
}
</style>
