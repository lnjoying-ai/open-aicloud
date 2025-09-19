<template>
  <div class="subNetAddPage h-full">
    <el-form
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="160px"
    >
      <el-card class="!border-none mb-3 mt-2 pb-3">
        <template #header>
          <div class="">
            <span>{{ $t("form.basicInfo") }}</span>
          </div>
        </template>
        <div class="text item">
          <el-form-item
            :label="$t('form.name') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.name || "-" }}</span>
          </el-form-item>

          <el-form-item
            :label="$t('form.vlanId') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.vlanId }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.detail') + ':'"
            style="width: 100%; float: left"
          >
            <span>
              {{ form.description || "-" }}
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.updateTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.updateTime || "-" }}
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.createTime || "-" }}
            </span>
          </el-form-item>
        </div>
      </el-card>
      <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>{{ $t("form.eipInfo") }}</span>
            <el-button
              v-if="userInfo.kind == 0 || userInfo.kind == 1"
              type="primary"
              class="mr-3 float-right"
              :size="$store.state.nextStack.viewSize.main"
              @click="addEip"
              >{{ $t("form.createEip") }}</el-button
            >
          </div>
        </template>
        <div class="text item">
          <el-table
            v-loading="loading"
            :data="tableData"
            stripe
            :element-loading-text="$t('domain.loading')"
            style="width: 100%"
          >
            <el-table-column prop="date" :label="$t('form.eip')">
              <template #default="scope">
                {{ scope.row.publicIp || scope.row.ipAddress }}
              </template>
            </el-table-column>

            <el-table-column prop="addressType" :label="$t('form.IPV4address')">
              <template #default="scope">
                <span>{{
                  scope.row.addressType === 0
                    ? $t("form.IPv4")
                    : $t("form.IPv6")
                }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="boundType" :label="$t('form.applyTo')">
              <template #default="scope">
                <span v-if="scope.row.boundType">
                  <span>
                    <span v-if="scope.row.boundType === 'port'">
                      {{ $t("form.cloudHost") }}
                    </span>
                    <span v-if="scope.row.boundType === 'nat'">
                      {{ $t("form.natGateway") }}
                    </span>
                  </span>
                </span>
                <span v-else> - </span>
              </template>
            </el-table-column>
            <el-table-column prop="boundName" :label="$t('form.target')">
              <template #default="scope">
                <span v-if="scope.row.boundType">
                  <span v-if="scope.row.boundName">
                    <!-- <el-tag
                      class="mr-1 mb-1"
                      v-for="(item, index) in scope.row.boundName.split(',')"
                      :key="index"
                      >{{ item }}</el-tag
                    > -->
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
                <span>{{
                  scope.row.eeUserName ? scope.row.eeUserName : "-"
                }}</span>
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
                    <div class="icon_cz" @click="toReleaseeip(scope.row)">
                      <i class="el-icon-delete" />{{ $t("form.release") }}
                    </div>
                    <div class="icon_cz" @click="toDelete(scope.row)">
                      <i class="el-icon-delete" />
                      {{ $t("form.delete") }}
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
              :page_num="formList.page_num"
              :page-size="formList.page_size"
              :page-sizes="$store.state.nextStack.page_sizes"
              :current-page="formList.page_num"
              layout="total, sizes, prev, pager, next, jumper"
              :total="formList.total"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </div>
      </el-card>
    </el-form>
    <el-dialog
      :title="$t('form.createEip')"
      :visible.sync="addDialogVisible"
      width="800px"
      :before-close="addHandleClose"
    >
      <eipadd ref="eipaddRef" @close="addHandleClose" />
    </el-dialog>
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
import eipadd from "../eip/add.vue";
import filtersFun from "@/utils/statusFun";

export default {
  components: {
    eipadd,
  },
  data() {
    return {
      filtersFun: filtersFun,
      loading: false,
      addDialogVisible: false,
      tableData: [],
      form: {},
      formList: {
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },

      eipsAttachLoading: false,
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
    this.getDetail();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo", "kind"]),
  },

  methods: {
    getDetail() {
      // 获取详情
      var id = this.$route.params.id;
      mainApi
        .eipPoolsDetail(id)
        .then((res) => {
          this.form = res;
          this.getEipList();
        })
        .catch((error) => {});
    },
    getEipList() {
      // eip列表
      var id = this.$route.params.id;
      const params = {
        name: this.formList.name,
        page_num: this.formList.page_num,
        page_size: this.formList.page_size,
        eip_pool_id: id,
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
        this.formList.total = res.totalNum;
      });
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.formList.page_size = val;
      this.getEipList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.formList.page_num = val;
      this.getEipList();
    },
    addEip() {
      // 创建EIP
      this.addDialogVisible = true;
    },
    addHandleClose() {
      // 创建EIP
      this.$refs.eipaddRef.resetForm();
      this.addDialogVisible = false;
      this.getEipList();
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
            title: this.$t("message.startBind"),
            type: "success",
            duration: 2500,
          });
          this.vmHandleClose();
        })
        .catch((error) => {
          this.eipsAttachLoading = false;
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
    toReleaseeip(item) {
      if (item.boundType) {
        this.$notify({
          title: this.$t("message.pleaseUnbindEipFirst"),
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
                this.formList.page_num = 1;
                this.getEipList();
              })
              .catch((error) => {
                this.loading = false;
              });
          })
          .catch(() => {});
      }
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteEip"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.delete"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .eipsDel(item.eipId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.formList.page_num = 1;
              this.getEipList();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    // 判断计费方式是否即将过期和过期
    isExpired(data) {
      const currentDate = new Date();
      const specifiedTime = new Date(data);
      const timeDifference = specifiedTime.getTime() - currentDate.getTime();
      const daysDifference = timeDifference / (1000 * 3600 * 24);
      if (currentDate.getTime() > specifiedTime.getTime()) {
        return 0;
      } else if (currentDate.getTime() < specifiedTime.getTime()) {
        if (daysDifference > 7) {
          return 1;
        } else {
          return 2;
        }
      } else {
        return 1;
      }
    },
    // 时间戳转换时间
    formatDate(date) {
      var date = new Date(date);
      var YY = date.getFullYear() + "-";
      var MM =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hh =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var mm =
        (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
        ":";
      var ss =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return YY + MM + DD + " " + hh + mm + ss;
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
