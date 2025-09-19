<template>
  <div class="warpMain">
    <div>
      <el-form
        :model="form"
        :inline="true"
        :size="$store.state.nextStack.viewSize.main"
        class="el-form demo-form-inline el-form--inline"
      >
        <el-form-item :label="$t('form.name') + ':'">
          <el-input
            v-model="form.name"
            class="w-48"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.type') + ':'">
          <el-select
            v-model="form.storage_pool_id"
            class="w-48"
            @change="onSubmit"
          >
            <el-option :label="$t('form.all')" :value="''" />
            <el-option
              v-for="(item, index) in storagePoolsList"
              :key="index"
              :label="item.name"
              :value="item.poolId"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.status') + ':'">
          <el-select v-model="form.detached" class="w-48" @change="onSubmit">
            <el-option :label="$t('form.all')" :value="undefined" />
            <el-option :label="$t('form.availableResource')" :value="false" />
            <el-option :label="$t('form.unMounted')" :value="true" />
            <!-- <el-option label="回收站" value="recycle" /> -->
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit">{{
            $t("form.query")
          }}</el-button>
          <el-button class="resetBtn" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-button
          type="primary"
          class="float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="addVolumes"
          >{{ $t("form.create") }}</el-button
        >
      </el-form>
    </div>
    <div>
      <el-table
        v-loading="loading"
        :element-loading-text="$t('domain.loading')"
        :data="tableData"
        max-height="calc(100% - 3rem)"
        class="!overflow-y-auto mt-4"
        stripe
        :scrollbar-always-on="false"
      >
        <el-table-column prop="date" :label="$t('form.name')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row)"
              >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>

        <el-table-column prop="type" :label="$t('form.type')">
          <template #default="scope">
            <span v-if="scope.row.type == 2">{{ $t("form.fileSystem") }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="size" :label="$t('form.size')">
          <template #default="scope">
            {{ scope.row.size }}
            {{ $t("unit.disk") }}
          </template>
        </el-table-column>
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getVolumeStatus(scope.row.phaseStatus, 'tag')"
              >{{
                filtersFun.getVolumeStatus(scope.row.phaseStatus, "status")
              }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="vmName" :label="$t('form.instance')">
          <template #default="scope">
            <router-link
              v-if="scope.row.vmInstanceId"
              :to="'/nextStack/vm/detail/' + scope.row.vmInstanceId"
              class="text-blue-400 mr-2"
            >
              <span>{{ scope.row.vmName || "-" }}</span>
            </router-link>
          </template>
        </el-table-column>

        <!-- <el-table-column prop="description"
                         label="描述">
          <template #default="scope">
            {{ scope.row.description }}
          </template>
        </el-table-column> -->

        <el-table-column
          v-if="kind == 0 || kind == 1"
          prop="eeUserName"
          :label="$t('form.user')"
        >
          <template #default="scope">
            <span>{{ scope.row.eeUserName ? scope.row.eeUserName : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />

        <el-table-column :label="$t('form.operation')" width="120">
          <template #default="scope">
            <div class="czlb">
              <div>
                <el-popover placement="bottom" width="110" trigger="click">
                  <div
                    v-if="scope.row.phaseStatus == '27'"
                    class="icon_cz"
                    @click="openVolumesAttach(scope.row)"
                  >
                    <img
                      src="@/assets/nextStack/btn/qy.png"
                      class="w-3 inline-block mr-1"
                      alt=""
                    />{{ $t("form.mount") }}
                  </div>
                  <div
                    v-if="scope.row.phaseStatus == '26'"
                    class="icon_cz"
                    @click="toVolumesDetach(scope.row)"
                  >
                    <img
                      src="@/assets/nextStack/btn/delete.png"
                      class="w-3 inline-block mr-1"
                      alt=""
                    />{{ $t("form.unmount") }}
                  </div>

                  <div class="icon_cz" @click="toEdit(scope.row)">
                    <i class="el-icon-edit" />
                    {{ $t("form.edit") }}
                  </div>
                  <div class="icon_cz" @click="toDelete(scope.row)">
                    <i class="el-icon-delete" />
                    {{ $t("form.delete") }}
                  </div>
                  <div class="icon_cz" @click="openSnaps(scope.row)">
                    <img
                      src="@/assets/nextStack/btn/snaps.png"
                      class="w-3 inline-block mr-1"
                      alt=""
                    />{{ $t("form.snapshot") }}
                  </div>
                  <el-button
                    slot="reference"
                    icon="el-icon-more"
                    class="czbtn right_czbtn"
                  />
                </el-popover>
              </div>
            </div>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :page_num="form.page_num"
        :page-size="form.page_size"
        :page-sizes="$store.state.nextStack.page_sizes"
        :current-page="form.page_num"
        layout="total, sizes, prev, pager, next, jumper"
        :total="form.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      :visible.sync="dialogVm"
      :close-on-click-modal="false"
      width="1200px"
      destroy-on-close
      :before-close="vmHandleClose"
      :title="$t('form.mount')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="24">
            <el-table
              ref="multipleTableRef"
              v-loading="vmLoading"
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
              class="flex justify-end mt-4 px-4"
              :page_num="vmForm.page_num"
              :page-size="vmForm.page_size"
              :page-sizes="[10]"
              :current-page="vmForm.page_num"
              layout="total, prev, pager, next, jumper"
              :total="vmForm.total"
              @size-change="transferHandleSizeChange"
              @current-change="transferHandleCurrentChange"
            />
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-right mt-4">
        <el-button type="text" size="small" @click="vmHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          size="small"
          :loading="loadingattach"
          @click="toVolumesAttach()"
          >{{ $t("form.mount") }}</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="600px"
      destroy-on-close
      :before-close="snapsHandleClose"
      :title="$t('form.createSnapshot')"
    >
      <el-form
        ref="snapsformRef"
        :rules="rules"
        :model="snapsform"
        label-width="120px"
        :size="$store.state.nextStack.viewSize.main"
      >
        <el-form-item :label="$t('form.cloudDisk') + ':'">
          <el-input
            v-model="nowVolumesData.name"
            class="w-60"
            :disabled="true"
            autocomplete="off"
            placeholder="-"
          />
        </el-form-item>
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="snapsform.name"
            class="w-60"
            autocomplete="off"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="snapsform.description"
            class="w-96"
            type="textarea"
            maxlength="255"
            show-word-limit
            :rows="2"
            autocomplete="off"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            size="small"
            :loading="loadingDialog"
            @click="toSnaps()"
            >{{ $t("form.create") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
    <el-dialog
      :title="$t('form.createFlavor')"
      :visible.sync="addDialogVisible"
      width="800px"
      :before-close="addHandleClose"
    >
      <volumesadd ref="volumesaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editFlavor')"
      :visible.sync="editDialogVisible"
      width="600px"
      :before-close="editHandleClose"
    >
      <volumesedit :id="editId" ref="volumeseditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import volumesadd from "./add.vue";
import volumesedit from "./edit.vue";

export default {
  components: {
    volumesadd,
    volumesedit,
  },
  data() {
    return {
      loadingattach: false,
      filtersFun: filtersFun,
      addDialogVisible: false,
      dialogFormVisible: false,
      editDialogVisible: false,
      vmAddDialogVisible: false,
      editId: "",
      nowVolumesData: "",
      snapsformRef: "",
      loadingDialog: false,
      snapsform: {
        name: "",
        description: "",
        volumeId: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
      timer: "",
      loading: false,
      vmLoading: false,
      dialogVm: false,
      vmTableData: [],
      nowCheckVm: "",
      nowCheckVolumes: "",
      vmForm: {
        // 搜索 筛选
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      form: {
        // 搜索 筛选
        name: "",
        storage_pool_id: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        detached: undefined,
        total: 0,
      },
      storagePoolsList: [],
      tableData: [],
    };
  },
  created() {
    this.getStoragePools(); // 云盘类型列表
    this.getVolumesList(); // 云盘列表
    this.getVmsInstabcesList();
  },
  mounted() {
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getVolumesListTime(); // 请求EIP循环列表
      }
    }, this.$store.state.nextStack.listRefreshTime);
  },

  computed: {
    ...mapGetters(["kind"]),
  },
  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    addVolumes() {
      // 创建云盘
      // this.addDialogVisible = true;
      this.$router.push("/nextStack/volumes/add");
    },
    addHandleClose() {
      // 创建云盘
      this.$refs.volumesaddRef.resetForm();
      this.addDialogVisible = false;
      this.getVolumesList();
    },
    toEdit(item) {
      // 编辑
      this.editId = item.volumeId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      // 编辑
      this.$refs.volumeseditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
      this.getVolumesList();
    },

    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`${val} items per page`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getVolumesList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getVolumesList();
    },
    handleCheckChange(val) {
      // 选中
      this.nowCheckVm = val;
    },
    openVolumesAttach(item) {
      // 挂载
      this.dialogVm = true;
      this.nowCheckVolumes = item;
    },
    toVolumesAttach() {
      this.loadingattach = true;
      if (!this.nowCheckVm) {
        this.$notify({
          title: this.$t("form.pleaseSelectInstance"),
          type: "warning",
          duration: 2500,
        });
        this.loadingattach = false;
        return false;
      }
      // 挂载
      mainApi
        .volumesAttach(
          { vmId: this.nowCheckVm.instanceId },
          this.nowCheckVolumes.volumeId
        )
        .then((res) => {
          this.loadingattach = false;
          this.$notify({
            title: this.$t("message.startMount"),
            type: "success",
            duration: 2500,
          });
          this.getVolumesList();
          this.vmHandleClose();
        })
        .catch((error) => {
          this.loadingattach = false;
        });
      return true;
    },
    vmHandleClose() {
      this.nowCheckVm = "";
      this.vmForm.name = "";
      this.vmForm.page_num = 1;
      this.dialogVm = false;
    },
    toVolumesDetach(item) {
      // 卸载
      this.$confirm(
        this.$t("message.confirmUnmountVolume"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      ).then(() => {
        // 卸载
        mainApi
          .volumesDetach(item.volumeId)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startUnmount"),
              type: "success",
              duration: 2500,
            });
            this.getVolumesList();
          })
          .catch((error) => {});
      });
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/volumes/detail/${item.volumeId}`,
      });
    },
    addVm(item) {
      // 恢复
      this.$router.push({
        path: `/nextStack/volumes/restore/${item.volumeId}`,
      });
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteVolume"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          // 删除
          mainApi
            .volumesDel(item.volumeId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getVolumesList();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getVolumesList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.storage_pool_id = "";
      this.form.page_num = 1;
      this.form.detached = undefined;
      this.getVolumesList();
    },
    getVolumesList() {
      // 云盘列表
      this.loading = true;
      const params = {
        name: this.form.name,
        storage_pool_id: this.form.storage_pool_id,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
        detached: this.form.detached,
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

      // if (this.tabContent === "recycle") {
      //   mainApi
      //     .volumesRecycleList(params)
      //     .then((res) => {
      //       this.loading = false;
      //       this.tableData = res.volumes;
      //       this.form.total = res.totalNum;
      //     })
      //     .catch((error) => {
      //       this.loading = false;
      //     });
      //   return;
      // }
      mainApi
        .volumesList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.volumes;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getVolumesListTime() {
      // 云盘列表
      const params = {
        name: this.form.name,
        storage_pool_id: this.form.storage_pool_id,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
        detached: this.form.detached,
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

      // if (this.tabContent === "recycle") {
      //   mainApi
      //     .volumesRecycleList(params)
      //     .then((res) => {
      //       this.loading = false;
      //       this.tableData = res.volumes;
      //       this.form.total = res.totalNum;
      //     })
      //     .catch((error) => {
      //       this.loading = false;
      //     });
      //   return;
      // }
      mainApi.volumesList(params).then((res) => {
        this.tableData = res.volumes;
        this.form.total = res.totalNum;
      });
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
    transferHandleSizeChange(val) {
      // 改变每页显示数量
      this.vmForm.page_size = val;
      this.getVmsInstabcesList();
    },
    transferHandleCurrentChange(val) {
      // 改变页码
      this.vmForm.page_num = val;
      this.getVmsInstabcesList();
    },
    getStoragePools() {
      // 云盘类型列表
      mainApi.storagePoolsList().then((res) => {
        this.storagePoolsList = res.storagePools;
      });
    },
    openSnaps(item) {
      this.dialogFormVisible = true;
      this.nowVolumesData = item;
      // 快照弹窗
    },
    snapsHandleClose(done) {
      this.resetForm();
      done();
    },
    resetForm() {
      // 重置
      this.$refs.snapsformRef.resetFields();
    },
    toSnaps() {
      // 创建快照
      // 快照add
      this.loadingDialog = true;
      this.$refs.snapsformRef.validate(async (valid) => {
        if (valid) {
          this.snapsform.volumeId = this.nowVolumesData.volumeId;

          mainApi
            .volumesSnapsAdd(this.snapsform)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startCreate"),
                type: "success",
                duration: 2500,
              });
              this.loadingDialog = false;
              this.dialogFormVisible = false;
              this.resetForm();
            })
            .catch((error) => {
              this.loadingDialog = false;
            });
        } else {
          this.loadingDialog = false;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetPage {
}
</style>
