<template>
  <div class="nodeDetailPage h-full mt-2">
    <el-card shadow="never">
      <el-form
        :model="form"
        label-width="140px"
        :size="$store.state.nextStack.viewSize.main"
      >
        <el-tabs v-model="activeName" class="demo-tabs bg-white nodeDetailTabs">
          <el-tab-pane :label="$t('form.basicInfo')" name="first">
            <div class="text item mt-4">
              <el-form-item
                :label="$t('form.name') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.name || "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.id') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.nodeId || "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.description') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.description || "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.cpu') + ':'"
                style="width: 50%; float: left"
              >
                <div v-if="form.cpuLogCount">
                  <span
                    >{{ form.cpuLogCount - form.usedCpuSum
                    }}{{ $t("unit.cpu") }}/{{ form.cpuLogCount
                    }}{{ $t("unit.cpu") }}</span
                  >
                  <small
                    >{{ $t("form.available") }}/{{ $t("form.total") }}</small
                  >
                </div>
                <div v-else>-</div>
              </el-form-item>
              <el-form-item
                :label="$t('form.cpuModel') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.cpuModel ? form.cpuModel : "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.memory') + ':'"
                style="width: 50%; float: left"
              >
                <div v-if="form.memTotal">
                  <span
                    >{{ form.memTotal - form.usedMemSum
                    }}{{ $t("unit.mem") }}/{{ form.memTotal
                    }}{{ $t("unit.mem") }}</span
                  >
                  <small
                    >{{ $t("form.available") }}/{{ $t("form.total") }}</small
                  >
                </div>
                <div v-else>-</div>
              </el-form-item>
              <el-form-item
                :label="$t('form.ib') + ':'"
                style="width: 50%; float: left"
              >
                <div v-if="form.ibTotal">
                  <span>{{ form.availableIbCount }}/{{ form.ibTotal }}</span>
                  <small
                    >{{ $t("form.available") }}/{{ $t("form.total") }}</small
                  >
                </div>
                <div v-else>-</div>
              </el-form-item>
              <el-form-item
                :label="$t('form.manageIp') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.manageIp ? form.manageIp : "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.createTime') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.createTime || "-" }}</span>
              </el-form-item>
            </div>
          </el-tab-pane>
          <el-tab-pane :label="$t('form.instance')" name="second">
            <div class="text item mt-4">
              <el-table
                :data="nodeVmTableData"
                max-height="calc(100% - 3rem)"
                class="!overflow-y-auto"
                stripe
                style="margin: 0 0 0 1rem"
                :scrollbar-always-on="false"
              >
                <el-table-column prop="date" :label="$t('form.name')">
                  <template #default="scope">
                    <router-link
                      :to="'/nextStack/vm/detail/' + scope.row.instanceId"
                      class="text-blue-400 mr-2"
                    >
                      <span>{{ scope.row.name }}</span>
                    </router-link>
                  </template>
                </el-table-column>

                <el-table-column prop="hostname" :label="$t('form.hostname')" />
                <el-table-column
                  prop="portInfo.ipAddress"
                  :label="$t('form.ip')"
                >
                  <template #default="scope">
                    <span>{{ scope.row.portInfo.ipAddress || "-" }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="vpcInfo.cidr"
                  :label="$t('form.networkAddress')"
                >
                  <template #default="scope">
                    <span>{{ scope.row.subnetInfo.cidr || "-" }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="eip" :label="$t('form.eip')">
                  <template #default="scope">
                    <span v-if="!scope.row.boundPhaseStatus">{{ "-" }}</span>
                    <span
                      v-if="
                        scope.row.boundType && scope.row.boundType === 'port'
                      "
                    >
                      <span v-if="scope.row.boundPhaseStatus == 82">{{
                        scope.row.publicIp || scope.row.eip || "-"
                      }}</span>
                      <el-tag
                        v-else
                        :size="$store.state.nextStack.viewSize.tagStatus"
                        :type="
                          filtersFun.getVmToEipStatus(
                            scope.row.boundPhaseStatus,
                            'tag'
                          )
                        "
                        >{{
                          filtersFun.getVmToEipStatus(
                            scope.row.boundPhaseStatus,
                            "status"
                          )
                        }}</el-tag
                      >
                    </span>
                    <span
                      v-if="
                        scope.row.boundType && scope.row.boundType === 'nat'
                      "
                    >
                      <span v-if="scope.row.boundPhaseStatus == 7">
                        <span v-if="scope.row.eip">
                          <el-tag
                            :size="$store.state.nextStack.viewSize.tagStatus"
                            >{{ $t("form.nat") }}</el-tag
                          >
                          {{ scope.row.publicIp || scope.row.eip }}
                        </span>
                        <span v-else>-</span>
                      </span>
                      <el-tag
                        v-else
                        :size="$store.state.nextStack.viewSize.tagStatus"
                        :type="
                          filtersFun.getNatStatus(
                            scope.row.boundPhaseStatus,
                            'tag'
                          )
                        "
                        >{{
                          filtersFun.getNatStatus(
                            scope.row.boundPhaseStatus,
                            "status"
                          )
                        }}</el-tag
                      >
                    </span>
                  </template>
                </el-table-column>
                <el-table-column prop="phaseStatus" :label="$t('form.status')">
                  <template #default="scope">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getVmStatus(scope.row.phaseStatus, 'tag')
                      "
                      >{{
                        filtersFun.getVmStatus(scope.row.phaseStatus, "status")
                      }}</el-tag
                    >
                  </template>
                </el-table-column>
                <el-table-column prop="" :label="$t('form.system')">
                  <template #default="scope">
                    <span v-if="scope.row.imageInfo">{{
                      scope.row.imageInfo.name ? scope.row.imageInfo.name : "-"
                    }}</span>
                    <span v-else>-</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="createTime"
                  :label="$t('form.createTime')"
                />
                <el-table-column :label="$t('form.operation')" width="100">
                  <template #default="scope">
                    <operate
                      :key="scope.row.instanceId"
                      :prop-vm-detail="scope.row"
                      :prop-show-type="1"
                      :prop-show-btn="[
                        'poweron',
                        'poweroff',
                        'reboot',
                        'delete',
                        'edit',
                        'eip',
                        'snaps',
                        'flavor',
                        'images',
                        'transfer',
                        'secGroup',
                        'vnc',
                        'resetPassword',
                        'renew',
                      ]"
                      @initVmList="onReset"
                      @restartDetail="getVmsInstabcesList"
                    />
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane :label="$t('form.pciDevice')" name="third">
            <div class="text item mt-4" style="margin: 0 0 0 1rem">
              <el-table
                :data="pciDeviceTableData"
                max-height="calc(100% - 3rem)"
                class="!overflow-y-auto"
                stripe
                :scrollbar-always-on="false"
              >
                <el-table-column
                  prop="pciDeviceName"
                  :label="$t('form.name')"
                  width="400"
                >
                  <template #default="scope">
                    <span>{{ scope.row.pciDeviceName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="pciDeviceGroupId"
                  :label="$t('form.group')"
                >
                  <template #default="scope">
                    <span>{{ scope.row.pciDeviceGroupId }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="pciDeviceType" :label="$t('form.type')">
                  <template #default="scope">
                    <span>{{ scope.row.pciDeviceType }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="pciDeviceVendor"
                  :label="$t('form.instance')"
                >
                  <template #default="scope">
                    <router-link
                      v-if="scope.row.vmInstanceName && scope.row.vmInstanceId"
                      :to="'/nextStack/vm/detail/' + scope.row.vmInstanceId"
                      class="text-blue-400 mr-2"
                    >
                      <span>{{ scope.row.vmInstanceName }}</span>
                    </router-link>

                    <span v-else>-</span>
                  </template>
                </el-table-column>
                <el-table-column prop="phaseStatus" :label="$t('form.status')">
                  <template #default="scope">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getPciStatus(scope.row.phaseStatus, 'tag')
                      "
                      >{{
                        filtersFun.getPciStatus(scope.row.phaseStatus, "status")
                      }}</el-tag
                    >
                  </template>
                </el-table-column>

                <el-table-column
                  prop="createTime"
                  :label="$t('form.createTime')"
                />
                <!-- <el-table-column
                  label="操作"
                  v-if="kind == 0 || kind == 1"
                  width="120"
                >
                  <template #default="scope">
                    <el-popconfirm
                      confirm-button-text="卸载"
                      cancel-button-text="取消"
                      icon-color="#626AEF"
                      popper-class="tablepopover"
                      title="确认卸载该设备吗？"
                      @confirm="toPciDetach(scope.row)"
                    >
                      <template #reference>
                        <span class="text-blue-400 cursor-pointer">卸载</span>
                      </template>
                    </el-popconfirm>
                  </template>
                </el-table-column> -->
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane :label="$t('form.advancedConfig')" name="fourth">
            <div class="text item mt-4">
              <el-form-item
                :label="$t('form.hostname') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.hostname || "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.loginName') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{ form.sysUsername || "-" }}</span>
              </el-form-item>
              <el-form-item
                :label="$t('form.loginCredential') + ':'"
                style="width: 50%; float: left"
              >
                <span>{{
                  form.pubkeyId ? $t("form.keyPair") : $t("form.password")
                }}</span>
              </el-form-item>
              <el-form-item
                v-if="form.pubkeyId"
                :label="$t('form.keyPair') + ':'"
                style="width: 50%; float: left"
              >
                <span>
                  <router-link
                    :to="'/nextStack/publicKey/detail/' + form.pubkeyId"
                    class="text-blue-400"
                    >{{ form.pubkeyId }}</router-link
                  >
                </span>
              </el-form-item>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>

    <el-dialog
      v-loading="vmLoading"
      :visible.sync="dialogVm"
      :close-on-click-modal="false"
      width="1200px"
      destroy-on-close
      :element-loading-text="$t('domain.loading')"
      :before-close="vmHandleClose"
      :title="$t('form.attach')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="24">
            <el-table
              ref="multipleTableRef"
              v-loading="vmLoading"
              :size="$store.state.nextStack.viewSize.main"
              :element-loading-text="$t('domain.loading')"
              :data="vmTableData"
              max-height="calc(100% - 3rem)"
              class="!overflow-y-auto hypervisorNodesDialog"
              stripe
              :scrollbar-always-on="false"
              @current-change="handleCheckChange"
            >
              <el-table-column label="" width="40px">
                <template #default="scope">
                  <span
                    v-if="scope.row.instanceId != nowCheckVm.instanceId"
                    class="w-3 h-3 block border rounded-sm border-gray-300"
                  />
                  <span
                    v-else
                    class="w-3 h-3 block border rounded-sm border-blue-500 bg-blue-500 text-center"
                  >
                    <i
                      class="el-icon-check text-white w-3.5 h-3.5 -m-0.5 leading-none table"
                    />
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="date" :label="$t('form.name')">
                <template #default="scope">
                  <router-link
                    :to="'/nextStack/vm/detail/' + scope.row.instanceId"
                  >
                    <span class="text-blue-400 cursor-pointer">{{
                      scope.row.name
                    }}</span>
                  </router-link>
                </template>
              </el-table-column>
              <el-table-column prop="hostname" :label="$t('form.hostname')" />
              <el-table-column prop="portInfo.ipAddress" :label="$t('form.ip')">
                <template #default="scope">
                  <span>{{ scope.row.portInfo.ipAddress || "-" }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="vpcInfo.cidr"
                :label="$t('form.networkAddress')"
              >
                <template #default="scope">
                  <span>{{ scope.row.subnetInfo.cidr || "-" }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="eip" :label="$t('form.eip')">
                <template #default="scope">
                  <span v-if="!scope.row.boundPhaseStatus">{{ "-" }}</span>
                  <span
                    v-if="scope.row.boundType && scope.row.boundType === 'port'"
                  >
                    <span v-if="scope.row.boundPhaseStatus == 82">{{
                      scope.row.publicIp || scope.row.eip || "-"
                    }}</span>
                    <el-tag
                      v-else
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getVmToEipStatus(
                          scope.row.boundPhaseStatus,
                          'tag'
                        )
                      "
                      >{{
                        filtersFun.getVmToEipStatus(
                          scope.row.boundPhaseStatus,
                          "status"
                        )
                      }}</el-tag
                    >
                  </span>
                  <span
                    v-if="scope.row.boundType && scope.row.boundType === 'nat'"
                  >
                    <span v-if="scope.row.boundPhaseStatus == 7">
                      <span v-if="scope.row.eip">
                        <el-tag
                          :size="$store.state.nextStack.viewSize.tagStatus"
                          >{{ $t("form.nat") }}</el-tag
                        >
                        {{ scope.row.publicIp || scope.row.eip }}
                      </span>
                      <span v-else>-</span>
                    </span>
                    <el-tag
                      v-else
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getNatStatus(
                          scope.row.boundPhaseStatus,
                          'tag'
                        )
                      "
                      >{{
                        filtersFun.getNatStatus(
                          scope.row.boundPhaseStatus,
                          "status"
                        )
                      }}</el-tag
                    >
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="phaseStatus" :label="$t('form.status')">
                <template #default="scope">
                  <el-tag
                    :size="$store.state.nextStack.viewSize.tagStatus"
                    :type="filtersFun.getVmStatus(scope.row.phaseStatus, 'tag')"
                    >{{
                      filtersFun.getVmStatus(scope.row.phaseStatus, "status")
                    }}</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column prop="" :label="$t('form.system')">
                <template #default="scope">
                  <span v-if="scope.row.imageInfo">{{
                    scope.row.imageInfo.name ? scope.row.imageInfo.name : "-"
                  }}</span>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                :label="$t('form.createTime')"
              />
            </el-table>
            <el-pagination
              class="!pt-4 !pr-8 float-right"
              :page_num="vmForm.page_num"
              :page-size="vmForm.page_size"
              :current-page="vmForm.page_num"
              :page-sizes="[10]"
              :small="true"
              layout="total, prev, pager, next, jumper"
              :total="vmForm.total"
              @size-change="pciHandleSizeChange"
              @current-change="pciHandleCurrentChange"
            />
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-center">
        <el-button type="text" @click="vmHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" @click="toPciAttach()">{{
          $t("form.attach")
        }}</el-button>
      </div>
    </el-dialog>

    <el-dialog
      :title="$t('form.editInstance')"
      :visible.sync="editDialogVisible"
      width="800px"
      :before-close="editHandleClose"
    >
      <vmedit :id="editId" ref="vmeditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
// import vmadd from "../../compute/vm/add.vue";
import vmedit from "../../compute/vm/edit.vue";
import operate from "@/views/nextStack/compute/vm/operate.vue";

export default {
  components: {
    vmedit,
    operate,
  },
  data() {
    return {
      filtersFun: filtersFun,
      editDialogVisible: false,
      editId: "",
      form: {},
      activeName: "first",
      nodeVmTableData: [],
      nodeVmTableTotal: 0,
      pciDeviceTableData: [],
      vmLoading: false,
      dialogVm: false,
      vmTableData: [],
      nowCheckVm: "",
      nowCheckPci: "",
      vmForm: {
        // 搜索 筛选
        name: "",
        page_num: 1,
        page_size: 10,
        total: 0,
      },
      dialogFormVisible: false,
      nowVmData: "",
      loadingDialog: false,
      multipleSelection: "",
      snapsform: {
        name: "",
        description: "",
        vmInstanceId: "",
      },
      imagesForm: {
        imageName: "",
        // imageOsType: 0,
        isPublic: true,
      },
      dialogExport: false,
      dialogTransfer: false,
      vmDetail: "",
      transferForm: {
        // 搜索 筛选
        name: "",
        page_num: 1,
        page_size: 10,
        total: 0,
      },
      transferTableData: [],
      transferLoading: false,
      rules: {
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputName"),
            trigger: "blur",
          },
          {
            min: 3,
            max: 64,
            message: this.$t("message.pleaseInput364"),
            trigger: "blur",
          },
        ],
      },
      imagesFormRules: {
        imageName: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    this.getDetail();
    this.getNodeVmList();
    this.getPciDeviceList();
    this.getVmsInstabcesList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    addVm() {
      // 创建实例
      this.$router.push({
        path: "/nextStack/vm/add?nodeId=" + this.form.nodeId,
      });
    },
    getDetail() {
      // 获取详情
      var id = this.$route.params.id;
      mainApi
        .vmsHypervisorNodesDetail(id)
        .then((res) => {
          this.form = res;
        })
        .catch((error) => {});
    },
    getNodeVmList() {
      // 实例列表
      var id = this.$route.params.id;
      var data = {
        node_id: id,
      };
      mainApi
        .vmsInstabcesList(data)
        .then((res) => {
          this.nodeVmTableData = res.vmInstancesInfo;
          this.nodeVmTableTotal = res.totalNum;
        })
        .catch((error) => {});
    },
    getPciDeviceList() {
      // GPU列表
      var id = this.$route.params.id;
      var data = {
        node_id: id,
      };
      mainApi
        .pciDeviceList(data)
        .then((res) => {
          this.pciDeviceTableData = res;
        })
        .catch((error) => {});
    },
    handleCheckChange(val) {
      // 选中
      this.nowCheckVm = val;
    },
    openPciAttach(item) {
      // 挂载
      this.dialogVm = true;
      this.nowCheckPci = item;
    },
    toPciAttach() {
      // 挂载
      mainApi
        .pciAttach(this.nowCheckPci.deviceId, {
          vmInstanceId: this.nowCheckVm.instanceId,
        })
        .then((res) => {
          this.$notify({
            title: this.$t("message.startMount"),
            type: "success",
            duration: 2500,
          });
          this.getPciDeviceList(); // 请求GPU列表
          this.vmHandleClose();
        })
        .catch((error) => {});
      return true;
    },
    vmHandleClose() {
      this.nowCheckVm = "";
      this.vmForm.name = "";
      this.vmForm.page_num = 1;
      this.dialogVm = false;
    },
    toPciDetach(item) {
      // 卸载
      this.$confirm(
        this.$t("message.confirmDetachDevice"),
        this.$t("message.detach"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .pciDetach(item.deviceId, { vmInstanceId: item.vmInstanceId })
            .then((res) => {
              this.$notify({
                title: this.$t("message.startDetach"),
                type: "success",
                duration: 2500,
              });
              this.getPciDeviceList(); // 请求GPU列表
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    pciHandleSizeChange(val) {
      // 改变每页显示数量
      this.vmForm.page_size = val;
      this.getVmsInstabcesList();
    },
    pciHandleCurrentChange(val) {
      // 改变页码
      this.vmForm.page_num = val;
      this.getVmsInstabcesList();
    },
    getVmsInstabcesList() {
      // 虚机列表
      this.vmLoading = true;

      const params = {
        name: this.vmForm.name,
        page_num: this.vmForm.page_num,
        page_size: this.vmForm.page_size,
      };
      for (const key in params) {
        if (
          params[key] === null ||
          params[key] === undefined ||
          params[key] === ""
        ) {
          delete params[key];
        }
      }

      mainApi
        .vmsInstabcesList(params)
        .then((res) => {
          this.vmLoading = false;
          this.vmTableData = res.vmInstancesInfo;
          this.vmForm.total = res.totalNum;
        })
        .catch((error) => {
          this.vmLoading = false;
        });
    },

    toEdit(item) {
      this.editDialogVisible = true;
      this.editId = item.instanceId;
    },
    editHandleClose() {
      this.$refs.vmeditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
      this.getNodeVmList();
    },

    onReset() {
      this.form.name = "";
      this.vmForm.page_num = 1;
      this.getVmsInstabcesList();
    },
  },
};
</script>

<style lang="scss" scoped>
.nodeDetailPage {
  .nodeDetailTabs {
    border-top-left-radius: 0.4rem;
    border-top-right-radius: 0.4rem;

    ::v-deep .el-tabs__header {
      padding: 0 0 0 1rem;
      margin-bottom: 0;
    }
  }
}
</style>
