<template>
  <div class="warpMain">
    <!-- <h5 class="mb-3 px-5 pt-2">
      <span class="text-lg">{{ $route.meta.title }}</span>
      <el-divider class="!my-2"></el-divider>
    </h5> -->
    <div>
      <el-form
        :model="form"
        :inline="true"
        label-width="40px"
        :size="$store.state.nextStack.viewSize.main"
      >
        <el-form-item :label="$t('form.eip') + ':'">
          <el-input
            v-model="form.name"
            class="w-48"
            :placeholder="$t('form.pleaseSelectEip')"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit">{{
            $t("form.query")
          }}</el-button>
          <el-button class="resetBtn" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <div class="float-right">
          <el-button
            type="primary"
            :size="$store.state.nextStack.viewSize.main"
            @click="applyforEip"
            >{{ $t("form.applyEip") }}</el-button
          >
        </div>
      </el-form>
    </div>
    <div>
      <el-table
        v-loading="loading"
        :element-loading-text="$t('domain.loading')"
        :data="tableData"
        max-height="calc(100% - 3rem)"
        class="!overflow-y-auto"
        stripe
        :scrollbar-always-on="false"
      >
        <el-table-column prop="date" label="EIP">
          <template #default="scope">
            {{ scope.row.publicIp || scope.row.ipAddress }}
          </template>
        </el-table-column>

        <el-table-column prop="addressType" :label="$t('form.IPV4address')">
          <template #default="scope">
            <span>{{
              scope.row.addressType === 0 ? $t("form.IPv4") : $t("form.IPv6")
            }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="boundType" :label="$t('form.applyTo')">
          <template #default="scope">
            <span v-if="scope.row.boundType">
              <span>
                <span v-if="scope.row.boundType === 'port'">{{
                  $t("form.cloudHost")
                }}</span>
                <span v-if="scope.row.boundType === 'nat'">{{
                  $t("form.natGateway")
                }}</span>
              </span>
            </span>
            <span v-else> - </span>
          </template>
        </el-table-column>
        <el-table-column prop="boundName" :label="$t('form.target')">
          <template #default="scope">
            <span v-if="scope.row.boundType">
              <span v-if="scope.row.boundName">
                <el-tag
                  v-for="(item, index) in getBoundNameList(scope.row.boundName)"
                  :key="index"
                  class="mr-1 mb-1"
                  >{{ item }}</el-tag
                >
              </span>
            </span>
            <span v-else> - </span>
          </template>
        </el-table-column>

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
            <div v-if="scope.row.name != 'default'" class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div
                  v-if="!scope.row.boundType"
                  class="icon_cz"
                  @click="toOpenVm(scope.row)"
                >
                  <img
                    src="@/assets/nextStack/btn/attach.png"
                    class="w-3 inline-block mr-1"
                    alt=""
                  />
                  {{ $t("form.bindInstance") }}
                </div>
                <div
                  v-if="scope.row.boundType"
                  class="icon_cz"
                  @click="toDetach(scope.row)"
                >
                  <img
                    src="@/assets/nextStack/btn/detach.png"
                    class="w-3 inline-block mr-1"
                    alt=""
                  />{{ $t("form.unbindInstance") }}
                </div>

                <div class="icon_cz" @click="releaseeip(scope.row)">
                  <i class="el-icon-delete" />{{ $t("form.release") }}
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
    </div>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :page_num="form.page_num"
        :page-size="form.page_size"
        :page-sizes="$store.state.nextStack.page_sizes"
        :current-page="form.page_num"
        :small="true"
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
      :title="$t('form.bindInstance')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="24">
            <el-table
              ref="multipleTableRef"
              v-loading="vmLoading"
              :element-loading-text="$t('domain.loading')"
              :data="vmTableData"
              max-height="450"
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
            <div class="flex justify-end mt-4 px-4">
              <el-pagination
                :page_num="vmForm.page_num"
                :page-size="vmForm.page_size"
                :current-page="vmForm.page_num"
                :page-sizes="[10]"
                layout="total, prev, pager, next, jumper"
                :total="vmForm.total"
                @size-change="vmHandleSizeChange"
                @current-change="vmHandleCurrentChange"
              />
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-right mt-4">
        <el-button type="text" size="small" @click="vmHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          :loading="eipsAttachLoading"
          size="small"
          @click="toEip()"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";

export default {
  data() {
    return {
      eipsAttachLoading: false,
      filtersFun: filtersFun,
      addDialogVisible: false,
      applyforDialogVisible: false,
      timer: "",
      loading: false,
      form: {
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      tableData: [],

      vmLoading: false,
      dialogVm: false,
      vmTableData: [],
      nowCheckEip: "",
      nowCheckVm: "",
      vmForm: {
        // 搜索 筛选
        name: "",
        page_num: 1,
        page_size: 10,
        total: 0,
      },
    };
  },

  created() {
    this.getEipList();
  },
  mounted() {
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getEipListTime();
      }
    }, this.$store.state.nextStack.listRefreshTime);
  },
  computed: {
    ...mapGetters(["userInfo", "kind"]),
  },
  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    getBoundNameList(boundName) {
      return boundName.split(",").map((item) => item.trim());
    },
    // 申请EIP
    applyforEip() {
      this.$router.push("/nextStack/eip/add");
    },

    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getEipList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getEipList();
    },

    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getEipList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.getEipList();
    },
    getEipList() {
      // eip列表
      this.loading = true;
      const params = {
        name: this.form.name,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
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
        .eipsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.eips;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getEipListTime() {
      // eip列表
      const params = {
        name: this.form.name,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
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
      mainApi.eipsList(params).then((res) => {
        this.tableData = res.eips;
        this.form.total = res.totalNum;
      });
    },
    toDetach(item) {
      if (item.boundType === "port") {
        this.$router.push({
          name: "nextStack-vm",
          params: {
            eipId: item.eipId,
            vmName: item.boundName,
            autoDetachEip: 1,
            ipAddress: item.publicIp || item.ipAddress,
          },
        });
      } else if (item.boundType === "nat") {
        this.$router.push({
          name: "nextStack-Nat",
          params: {
            vmName: item.boundName,
            eipId: item.eipId,
          },
        });
      }
      // 解绑
      // this.$confirm("确认解绑该eip吗？", "提示", {
      //   confirmButtonText: "解绑",
      //   cancelButtonText: "取消",
      //   type: "warning",
      // })
      //   .then(() => {
      //     mainApi
      //       .eipsDetach(item.eipId)
      //       .then((res) => {
      // this.$notify({
      //           title: "解绑成功",
      //           type: "success",
      //           duration: 2500,
      //         })
      //         this.getEipList();
      //       })
      //       .catch((error) => {});
      //   })
      //   .catch(() => {});
    },

    // 释放eip
    releaseeip(item) {
      if (item.boundType) {
        this.$notify({
          title: this.$t("message.pleaseUnbindEip"),
          type: "warning",
          duration: 2500,
        });
      } else {
        this.$confirm(
          this.$t("message.confirmReleaseEip"),
          this.$t("message.tip"),
          {
            confirmButtonText: this.$t("message.release"),
            cancelButtonText: this.$t("message.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            this.loading = true;
            mainApi
              .releaseeip(item.eipId)
              .then((res) => {
                this.loading = false;
                this.$notify({
                  title: this.$t("message.releaseSuccess"),
                  type: "success",
                  duration: 2500,
                });
                this.getEipList();
              })
              .catch((error) => {
                this.loading = false;
              });
          })
          .catch(() => {});
      }
    },
    toOpenVm(item) {
      // 绑定实例
      this.dialogVm = true;
      this.nowCheckEip = item;
      this.getVmsInstabcesList();
    },
    vmHandleClose() {
      // 绑定实例
      this.nowCheckVm = "";
      this.vmForm.name = "";
      this.vmForm.page_num = 1;
      this.dialogVm = false;
    },
    handleCheckChange(val) {
      // 选中
      this.nowCheckVm = val;
    },
    vmHandleSizeChange(val) {
      // 改变每页显示数量
      this.vmForm.page_size = val;
      this.getVmsInstabcesList();
    },
    vmHandleCurrentChange(val) {
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
        eip_id_is_null: true,
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
    toEip() {
      // 绑定EIP
      this.eipsAttachLoading = true;

      if (this.nowCheckVm === "") {
        this.$notify({
          title: this.$t("message.pleaseSelectInstance"),
          type: "warning",
          duration: 2500,
        });
        this.eipsAttachLoading = false;
        return false;
      }
      mainApi
        .eipsAttach(this.nowCheckEip.eipId, {
          portId: this.nowCheckVm.portInfo.portId,
        })
        .then((res) => {
          this.eipsAttachLoading = false;
          this.$notify({
            title: this.$t("message.startBinding"),
            type: "success",
            duration: 2500,
          });
          this.vmHandleClose();
        })
        .catch((error) => {
          this.eipsAttachLoading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetPage {
}
</style>
