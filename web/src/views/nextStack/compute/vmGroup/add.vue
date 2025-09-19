<template>
  <div class="snapsAddPage h-full">
    <el-form
      ref="addVmForm"
      v-loading="loading"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="120px"
      :element-loading-text="$t('domain.loading')"
    >
      <el-card class="mt-2">
        <!-- <template #header>
          <div class="">
            <span>基本信息</span>
          </div>
        </template> -->
        <div class="text item">
          <el-form-item :label="$t('form.name') + ':'" prop="name">
            <el-input
              v-model="form.name"
              class="w-60"
              :placeholder="$t('form.pleaseInputName')"
            />
          </el-form-item>
          <el-form-item
            :label="$t('form.description') + ':'"
            prop="description"
          >
            <el-input
              v-model="form.description"
              class="w-96"
              maxlength="255"
              show-word-limit
              type="textarea"
              :rows="2"
              :placeholder="$t('form.pleaseInputDescription')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.instance') + ':'">
            <div class="text-right block w-full">
              <el-button
                type="primary"
                class="float-right mb-4"
                :size="$store.state.nextStack.viewSize.main"
                @click="addInstance()"
              >
                {{ $t("form.addInstance") }}
              </el-button>
            </div>
            <el-table
              :data="vmTableData"
              class="!overflow-y-auto"
              stripe
              :scrollbar-always-on="false"
            >
              <el-table-column prop="date" :label="$t('form.name')">
                <template #default="scope">
                  <!-- 新窗口打开 -->
                  <router-link
                    :to="'/nextStack/vm/detail/' + scope.row.instanceId"
                    target="_blank"
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
              <el-table-column prop="" :label="$t('form.os')">
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

              <el-table-column :label="$t('form.operation')" width="120">
                <template #default="scope">
                  <el-button
                    type="text"
                    :size="$store.state.nextStack.viewSize.listSet"
                    @click="toDelete(scope.$index)"
                  >
                    {{ $t("form.remove") }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </div>
        <div class="text item text-center">
          <el-button type="primary" size="small" @click="toVmGroupAdd()">{{
            $t("form.createImmediately")
          }}</el-button>
        </div>
      </el-card>
    </el-form>
    <el-dialog
      v-loading="vmLoading"
      :visible.sync="dialogVm"
      :close-on-click-modal="false"
      width="1250px"
      destroy-on-close
      :element-loading-text="$t('domain.loading')"
      :before-close="vmHandleClose"
      :title="$t('form.addInstance')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="24">
            <el-table
              ref="multipleTableRef"
              v-loading="vmLoading"
              :element-loading-text="$t('domain.loading')"
              :data="vmTableList"
              max-height="calc(100% - 3rem)"
              class="!overflow-y-auto hypervisorNodesDialog"
              stripe
              :scrollbar-always-on="false"
            >
              <el-table-column label="" width="40px">
                <template #default="scope">
                  <span
                    v-if="
                      JSON.stringify(vmTableData).includes(scope.row.instanceId)
                    "
                  >
                    <span
                      class="w-3 h-3 block border rounded-sm border-gray-300 bg-gray-300 text-center"
                    >
                      <i
                        class="el-icon-check text-white w-3.5 h-3.5 -m-0.5 leading-none table"
                      />
                    </span>
                  </span>
                  <span v-else>
                    <span
                      v-if="!nowCheckVmIncludes(scope.row)"
                      class="w-3 h-3 block border rounded-sm border-gray-300"
                      @click="handleCheckChange(scope.row, true)"
                    />
                    <span
                      v-else
                      class="w-3 h-3 block border rounded-sm border-blue-500 bg-blue-500 t text-center"
                      @click="handleCheckChange(scope.row, false)"
                    >
                      <i
                        class="el-icon-check text-white w-3.5 h-3.5 -m-0.5 leading-none table"
                      />
                    </span>
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="date" :label="$t('form.name')">
                <template #default="scope">
                  <router-link
                    :to="'/nextStack/vm/detail/' + scope.row.instanceId"
                    target="_blank"
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
              <el-table-column prop="" :label="$t('form.os')">
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
                :page-sizes="[10]"
                :current-page="vmForm.page_num"
                layout="total, prev, pager, next, jumper"
                :total="vmForm.total"
                @size-change="transferHandleSizeChange"
                @current-change="transferHandleCurrentChange"
              />
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-right mt-4">
        <el-button
          type="text"
          :size="$store.state.nextStack.viewSize.main"
          @click="vmHandleClose()"
          >{{ $t("form.cancel") }}</el-button
        >
        <el-button
          type="primary"
          :size="$store.state.nextStack.viewSize.main"
          @click="toAddVm()"
          >{{ $t("form.add") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import mainApi from "@/api/nextStack/mainApi";
import { mapGetters } from "vuex";

import filtersFun from "@/utils/statusFun";
export default {
  data() {
    return {
      filtersFun: filtersFun,

      loading: false,
      vpcList: [],
      subnetsDataList: [],
      vpcId: "",
      subnetId: "",
      form: {
        name: "",
        description: "",
      },
      tableData: [],
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
      vmTableData: [],
      nowCheckVm: [],
      vmTableList: [],
      dialogVm: false,
      vmLoading: false,
      vmForm: {
        name: "",
        page_num: 1,
        page_size: 10,
        total: 0,
      },
    };
  },

  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    resetForm() {
      this.$refs.addVmForm.resetFields();
    },
    toVmGroupAdd() {
      this.loading = true;
      var data = {
        name: this.form.name,
        description: this.form.description,
        vmInstanceIds: this.vmTableData.map((item) => item.instanceId),
      };
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .vmInstanceGroupsAdd(data)
            .then((res) => {
              this.loading = false;
              this.resetForm();
              this.$router.push(this.$route.meta.activeMenu);
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    vmHandleClose() {
      this.nowCheckVm = [];
      this.vmForm.name = "";
      this.vmForm.page_num = 1;
      this.dialogVm = false;
    },
    toAddVm() {
      if (this.nowCheckVm.length === 0) {
        this.$notify({
          title: this.$t("message.pleaseSelectInstance"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      this.vmTableData = [...this.vmTableData, ...this.nowCheckVm];
      this.vmHandleClose();
    },
    toDelete(index) {
      this.vmTableData.splice(index, 1);
    },
    nowCheckVmIncludes(val) {
      var data = this.nowCheckVm.filter(
        (item) => item.instanceId === val.instanceId
      );
      return data.length > 0;
    },
    handleCheckChange(val, type) {
      if (type) {
        this.nowCheckVm.push(val);
      } else {
        this.nowCheckVm = this.nowCheckVm.filter((item) => item !== val);
      }
    },
    getVmsInstabcesList() {
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
        .vmsInstabcesList({ instance_group_id_is_null: true, ...params })
        .then((res) => {
          this.vmLoading = false;

          this.vmTableList = res.vmInstancesInfo;
          this.vmForm.total = res.totalNum;
        })
        .catch((error) => {
          this.vmLoading = false;
        });
    },
    transferHandleSizeChange(val) {
      this.vmForm.page_size = val;
      this.getVmsInstabcesList();
    },
    transferHandleCurrentChange(val) {
      this.vmForm.page_num = val;
      this.getVmsInstabcesList();
    },
    addInstance() {
      this.dialogVm = true;
      this.getVmsInstabcesList();
    },
  },
};
</script>

<style lang="scss" scoped>
.snapsAddPage {
}
</style>
