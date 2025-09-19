<template>
  <div>
    <div class="mt-2">
      <div>
        <el-card class="mb-2">
          <div slot="header" class="clearfix">
            <span>{{ $t("form.siteDetail") }}</span>
          </div>
          <el-form ref="form" label-width="120px" :inline="true">
            <el-form-item
              :label="$t('form.privateCloud') + ':'"
              style="width: 25%; margin-right: 0"
            >
              <span>{{ detailMain.cloud_num }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.containerNode') + ':'"
              style="width: 25%; margin-right: 0"
            >
              <span>{{ detailMain.node_num }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.createTime') + ':'"
              style="width: 25%; margin-right: 0"
            >
              <span>{{ detailMain.create_time || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.updateTime') + ':'"
              style="width: 25%; margin-right: 0"
            >
              <span>{{ detailMain.update_time || "-" }}</span>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="mb-2">
          <el-tabs
            v-model="activeName"
            class="demo-tabs"
            @tab-click="handleClick"
          >
            <el-tab-pane :label="$t('form.privateCloud')" name="first">
              <el-table
                :data="clondyData"
                stripe
                style="width: 100%"
                tooltip-effect="dark"
              >
                <el-table-column prop="name" :label="$t('form.name')" />
                <el-table-column prop="product" :label="$t('form.type')" />

                <el-table-column :label="$t('form.status')">
                  <template slot-scope="scope">
                    <el-tag
                      size="small"
                      :type="
                        filtersFun.getCloudStatus(scope.row.status.code, 'tag')
                      "
                      >{{
                        filtersFun.getCloudStatus(
                          scope.row.status.code,
                          "status"
                        )
                      }}</el-tag
                    >
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.instanceDevice')" sortable>
                  <template slot-scope="scope">
                    {{ scope.row.metrics.vm_instance_num || 0 }}
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.baremetalDevice')" sortable>
                  <template slot-scope="scope">
                    {{ scope.row.metrics.baremetal_device_num || 0 }}
                  </template>
                </el-table-column>

                <el-table-column :label="$t('form.baremetalInstance')" sortable>
                  <template slot-scope="scope">
                    {{ scope.row.metrics.baremetal_instance_num || 0 }}
                  </template>
                </el-table-column>
                <el-table-column
                  :label="$t('form.computeNodeCount')"
                  sortable
                  min-width="100"
                >
                  <template slot-scope="scope">
                    {{ scope.row.metrics.hypervisor_node_num || 0 }}
                  </template>
                </el-table-column>
                <el-table-column prop="url" :label="$t('form.path')" />
                <el-table-column
                  prop="bp_name"
                  :label="$t('form.organization')"
                />
                <el-table-column prop="user_name" :label="$t('form.user')" />
              </el-table>
              <div class="flex justify-end mt-4 px-4">
                <el-pagination
                  :current-page="queryCloudsData.page_num"
                  :page-sizes="[5, 10, 20, 50, 100]"
                  :page-size="queryCloudsData.page_size"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="cloudstotal"
                  @size-change="handlesCloudSizeChange"
                  @current-change="handleCloudCurrentChange"
                />
              </div>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.containerNode')" name="five">
              <el-table :data="nodeData" stripe style="width: 100%">
                <el-table-column
                  prop="node_name"
                  :label="$t('form.nodeName')"
                  show-overflow-tooltip
                />
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
                <el-table-column
                  prop="bp_name"
                  :label="$t('form.organization')"
                  show-overflow-tooltip
                >
                  <template slot-scope="scope">
                    {{ scope.row.bp_name || "-" }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="region_name"
                  :label="$t('form.regionSite')"
                  show-overflow-tooltip
                >
                  <template slot-scope="scope">
                    {{ scope.row.region_name || "-" }}/{{
                      scope.row.site_name || "-"
                    }}
                  </template>
                </el-table-column>
                <el-table-column
                  :label="$t('form.activation')"
                  show-overflow-tooltip
                >
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
              </el-table>
              <div class="flex justify-end mt-4 px-4">
                <el-pagination
                  :current-page="queryNodesData.page_num"
                  :page-sizes="[5, 10, 20, 50, 100]"
                  :page-size="queryNodesData.page_size"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="Nodestotal"
                  @size-change="handleNodeSizeChange"
                  @current-change="handleNodeCurrentChange"
                />
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
        <el-card class="mb-2">
          <div slot="header">
            <div>{{ $t("form.siteAgent") }}</div>
          </div>
          <el-tabs
            v-model="activeNameAgent"
            class="demo-tabs"
            @tab-click="handleAgentClick"
          >
            <el-tab-pane :label="$t('form.serviceAgent')" name="second">
              <div class="text-right mb-2">
                <el-button
                  size="small"
                  type="primary"
                  @click="addServiceAgentDialogVisible = true"
                >
                  {{ $t("form.create") }}
                </el-button>
              </div>
              <el-table :data="serviceAgentData" stripe style="width: 100%">
                <el-table-column
                  prop="description"
                  :label="$t('form.agentName')"
                  show-overflow-tooltip
                >
                  <template slot-scope="scope">
                    {{ scope.row.description ? scope.row.description : "-" }}
                  </template>
                </el-table-column>

                <el-table-column prop="status" :label="$t('form.agentStatus')">
                  <template slot-scope="scope">
                    <span v-if="scope.row.status.code == 105">
                      <span v-if="scope.row.online_status == 0">{{
                        $t("form.offline")
                      }}</span>
                      <span v-if="scope.row.online_status == 1">{{
                        $t("form.online")
                      }}</span>
                    </span>
                    <span v-else>
                      {{ getContainerStatus(scope.row.status.code) }}
                    </span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="node_name"
                  :label="$t('form.deploymentLocation')"
                >
                  <template slot-scope="scope">
                    {{ scope.row.node_name ? scope.row.node_name : "-" }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="create_time"
                  :label="$t('form.deploymentTime')"
                  sortable
                />
                <el-table-column
                  prop=""
                  :label="$t('form.operation')"
                  width="130"
                >
                  <template slot-scope="scope">
                    <el-button
                      type="text"
                      size="small"
                      @click="toDelServiceAgent(scope.row)"
                      >{{ $t("form.delete") }}</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
              <div class="flex justify-end mt-4 px-4">
                <el-pagination
                  :current-page="queryServiceAgentData.page_num"
                  :page-sizes="[5, 10, 20, 50, 100]"
                  :page-size="queryServiceAgentData.page_size"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="serviceAgenttotal"
                  @size-change="handlesServiceAgentSizeChange"
                  @current-change="handleServiceAgentCurrentChange"
                />
              </div>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.monitoringAgent')" name="third">
              <el-table :data="monitorAgentData" stripe style="width: 100%">
                <el-table-column
                  prop="name"
                  :label="$t('form.agentName')"
                  show-overflow-tooltip
                />

                <el-table-column prop="status" :label="$t('form.agentStatus')">
                  <template slot-scope="scope">
                    <div v-if="scope.row.status == '1'">
                      <span>{{ $t("form.running") }}</span>
                    </div>
                    <div v-if="scope.row.status == '2'">
                      <span>{{ $t("form.starting") }}</span>
                    </div>
                    <div v-if="scope.row.status == '3'">
                      <span>{{ $t("form.error") }}</span>
                    </div>
                    <div v-if="scope.row.status == '4'">
                      <span>{{ $t("form.down") }}</span>
                    </div>
                    <div v-if="scope.row.status == '5'">
                      <span>{{ $t("form.updating") }}</span>
                    </div>
                    <div v-if="scope.row.status == '6'">
                      <span>{{ $t("form.paused") }}</span>
                    </div>
                    <div v-if="scope.row.status == '7'">
                      <span>{{ $t("form.unknown") }}</span>
                    </div>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="node_name"
                  :label="$t('form.deploymentLocation')"
                >
                  <template slot-scope="scope">
                    {{ scope.row.node_name ? scope.row.node_name : "-" }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="target_num"
                  :label="$t('form.collectionTerminalCount')"
                  sortable
                >
                  <template slot-scope="scope">
                    {{ scope.row.target_num }}
                  </template>
                </el-table-column>
                <el-table-column
                  prop="create_time"
                  :label="$t('form.createTime')"
                  sortable
                />
              </el-table>
              <div class="flex justify-end mt-4 px-4">
                <el-pagination
                  :current-page="queryMonitorAgentData.page_num"
                  :page-sizes="[5, 10, 20, 50, 100]"
                  :page-size="queryMonitorAgentData.page_size"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="monitorAgenttotal"
                  @size-change="handleMonitorAgentSizeChange"
                  @current-change="handleMonitorAgentCurrentChange"
                />
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
    <el-dialog
      :title="$t('form.addAgent')"
      :visible.sync="addServiceAgentDialogVisible"
      destroy-on-close
      width="800px"
      :before-close="serviceAgentHandleClose"
    >
      <el-form
        ref="serviceAgentFormRef"
        :model="serviceAgentForm"
        label-width="120px"
        size="small"
      >
        <el-form-item :label="$t('form.agentName') + ':'">
          <el-input
            v-model="serviceAgentForm.description"
            :placeholder="$t('form.pleaseInputAgentName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.deploymentNode') + ':'">
          <el-select
            v-model="serviceAgentForm.node_id"
            :placeholder="$t('form.pleaseSelectNode')"
            style="width: 100%"
          >
            <el-option
              v-for="(item, index) in nodeInitData"
              :key="index"
              :label="item.node_name"
              :value="item.node_id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.image') + ':'">
          <el-input
            v-model="serviceAgentForm.image"
            :placeholder="$t('form.pleaseInputImage')"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="serviceAgentHandleClose()">
          {{ $t("form.cancel") }}
        </el-button>
        <el-button
          type="primary"
          size="small"
          @click="saveServiceAgent('serviceAgentFormRef')"
        >
          {{ $t("form.confirm") }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  getSites,
  edges,
  getCloudys,
  serviceAgent,
  addServiceAgent,
  delServiceAgent,
  getMonitorPrometheus,
} from "@/api/mainApi";
import filtersFun from "@/utils/statusFun";

export default {
  components: {},
  data() {
    return {
      filtersFun: filtersFun,
      nodeList: [],
      params: {},
      detailMain: "",
      activeName: "first",
      activeNameAgent: "second",
      clondyData: [], // 私有云
      nodeData: [], // 容器节点
      serviceAgentData: [], // service-agent
      monitorAgentData: [],
      queryNodesData: {
        site: "",
        page_size: 10,
        page_num: 1,
      },
      Nodestotal: 0,
      queryCloudsData: {
        site: "",
        page_size: 10,
        page_num: 1,
      },
      cloudstotal: 0,
      queryServiceAgentData: {
        site_id: "",
        page_size: 10,
        page_num: 1,
      },
      queryMonitorAgentData: {
        site_id: "",
        page_size: 10,
        page_num: 1,
        type: 1,
      },
      nodeInitData: [],
      addServiceAgentDialogVisible: false,
      serviceAgenttotal: 0,
      monitorAgenttotal: 0,
      serviceAgentForm: {
        node_id: "",
        image: "",
        description: "",
      },
      containerStatus: [
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
    };
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  created() {},
  mounted() {
    this.params = this.$route.params;
    this.$route.meta.smalltitle =
      this.$t("message.siteName") + ":" + this.params.name;
    this.init();
    this.getInitNodes();
    this.getNodes();
    this.getClouds();
    this.getServiceAgentData();
    this.getMonitorAgentData();
  },
  methods: {
    getContainerStatus(code) {
      return this.containerStatus.find((item) => item.code === code).name;
    },
    handleClick(tab, event) {
      this.activeName == "first" ? this.getClouds() : this.getNodes();
    },
    handleAgentClick(tab, event) {
      this.activeNameAgent == "second"
        ? this.getServiceAgentData()
        : this.getMonitorAgentData();
    },
    async init() {
      const list = await getSites({ site_id: this.params.id });
      this.detailMain = list.sites[0].sites[0];
    },
    async getInitNodes() {
      const list = await edges({ site: this.params.id });
      this.nodeInitData = list.nodes;
      if (this.nodeInitData != null && this.nodeInitData.length > 0) {
        this.serviceAgentForm.node_id = this.nodeInitData[0].node_id;
      }
    },
    async getNodes() {
      this.queryNodesData.site = this.params.id;
      const list = await edges(this.queryNodesData);
      this.nodeData = list.nodes;
      this.Nodestotal = list.total_num;
    },

    handleNodeSizeChange(val) {
      this.queryNodesData.page_size = val;
      this.getNodes();
    },
    handleNodeCurrentChange(val) {
      this.queryNodesData.page_num = val;
      this.getNodes();
    },
    async getClouds() {
      // 私有云
      this.queryCloudsData.site = this.params.id;
      const list = await getCloudys(this.queryCloudsData);
      this.clondyData = list.clouds;
      this.cloudstotal = list.total_num;
    },
    handlesCloudSizeChange(val) {
      this.queryCloudsData.page_size = val;
      this.getClouds();
    },
    handleCloudCurrentChange(val) {
      this.queryCloudsData.page_num = val;
      this.getClouds();
    },

    async getServiceAgentData() {
      // service agent
      this.queryServiceAgentData.site_id = this.params.id;
      const list = await serviceAgent(this.queryServiceAgentData);
      this.serviceAgentData = list.service_agents;
      this.serviceAgenttotal = list.total_num;
    },
    async getMonitorAgentData() {
      // monitor  agent
      this.queryMonitorAgentData.site_id = this.params.id;
      const list = await getMonitorPrometheus(this.queryMonitorAgentData);
      this.monitorAgentData = list.prometheus_instances;
      this.monitorAgenttotal = list.total_num;
    },
    handleMonitorAgentSizeChange(val) {
      this.queryMonitorAgentData.page_size = val;
      this.getMonitorAgentData();
    },
    handleMonitorAgentCurrentChange(val) {
      this.queryMonitorAgentData.page_num = val;
      this.getMonitorAgentData();
    },
    handlesServiceAgentSizeChange(val) {
      this.queryServiceAgentData.page_size = val;
      this.getServiceAgentData();
    },
    handleServiceAgentCurrentChange(val) {
      this.queryServiceAgentData.page_num = val;
      this.getServiceAgentData();
    },
    serviceAgentHandleClose() {
      // this.$refs['serviceAgentFormRef'].resetFields();
      this.addServiceAgentDialogVisible = false;
    },
    toDelServiceAgent(row) {
      this.$confirm(
        this.$t("message.confirmDeleteServiceAgent"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      ).then(() => {
        delServiceAgent(row.agent_id)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startDelete"),
              type: "success",
              duration: 2500,
            });
            this.getServiceAgentData();
          })
          .catch((err) => {
            console.log(err);
          });
      });
    },
    saveServiceAgent(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var data = {
            description: this.serviceAgentForm.description,
            target_nodes: [
              {
                dst_region_id: this.detailMain.region_id,
                dst_site_id: this.detailMain.id,
                dst_node_id: this.serviceAgentForm.node_id,
              },
            ],
            image: this.serviceAgentForm.image,
          };
          data.target_nodes[0].dst_node_id == ""
            ? delete data.target_nodes[0].dst_node_id
            : "";
          addServiceAgent(data)
            .then((res) => {
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getServiceAgentData();
              this.serviceAgentHandleClose();
            })
            .catch((err) => {
              console.log(err);
            });
        } else {
          return false;
        }
      });
    },
    resetForm() {},
  },
};
</script>
<style lang="scss" scoped></style>
