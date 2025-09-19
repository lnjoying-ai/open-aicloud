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
            :placeholder="$t('form.pleaseInputComputeNodeName')"
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
        <el-button
          type="primary"
          class="float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="addhypervisorNodes"
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
        class="!overflow-y-auto"
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
        <!-- <el-table-column prop="hostname" label="主机名">
          <template #default="scope">
            {{ scope.row.hostname ? scope.row.hostname : "-" }}
          </template>
        </el-table-column> -->
        <el-table-column prop="manageIp" :label="$t('form.manageIp')" />
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
        <el-table-column :label="$t('form.cpuAvailable')">
          <template #default="scope">
            <div v-if="scope.row.cpuLogCount">
              <p>
                {{ scope.row.cpuLogCount - scope.row.usedCpuSum
                }}{{ $t("unit.cpu") }}/{{ scope.row.cpuLogCount
                }}{{ $t("unit.cpu") }}
              </p>
              <p>{{ scope.row.cpuModel }}</p>
            </div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.gpuAvailable')">
          <template #default="scope">
            <div v-if="scope.row.gpuTotal">
              <p>{{ scope.row.availableGpuCount }}/{{ scope.row.gpuTotal }}</p>
              <p>{{ scope.row.gpuName }}</p>
            </div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.memAvailable')">
          <template #default="scope">
            <div v-if="scope.row.memTotal">
              {{ scope.row.memTotal - scope.row.usedMemSum
              }}{{ $t("unit.mem") }}/{{ scope.row.memTotal
              }}{{ $t("unit.mem") }}
            </div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.ibAvailable')">
          <template #default="scope">
            <div v-if="scope.row.ibTotal">
              {{ scope.row.availableIbCount }}/{{ scope.row.ibTotal }}
            </div>
            <div v-else>-</div>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />
        <el-table-column :label="$t('form.operation')" width="120">
          <template #default="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="toEdit(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
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
      :title="$t('form.createComputeNode')"
      :visible.sync="addDialogVisible"
      width="600px"
      :before-close="addHandleClose"
    >
      <hypervisorNodesadd ref="hypervisorNodesaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editComputeNode')"
      :visible.sync="editDialogVisible"
      width="600px"
      :before-close="editHandleClose"
    >
      <hypervisorNodesedit
        :id="editId"
        ref="hypervisorNodeseditRef"
        @close="editHandleClose"
      />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import hypervisorNodesadd from "./add.vue";
import hypervisorNodesedit from "./edit.vue";

export default {
  components: {
    hypervisorNodesadd,
    hypervisorNodesedit,
  },
  data() {
    return {
      filtersFun: filtersFun,
      addDialogVisible: false,
      editDialogVisible: false,
      editId: "",
      timer: "",
      loading: false,
      form: {
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      tableData: [],
    };
  },
  created() {
    this.getvmsHypervisorNodesList();
  },
  mounted() {
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getvmsHypervisorNodesListTime();
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
    addhypervisorNodes() {
      // 创建计算节点
      this.addDialogVisible = true;
    },
    addHandleClose() {
      this.$refs.hypervisorNodesaddRef.resetForm();
      this.addDialogVisible = false;
      this.getvmsHypervisorNodesList();
    },
    toEdit(item) {
      // 编辑
      this.editDialogVisible = true;
      this.editId = item.nodeId;
    },
    editHandleClose() {
      this.$refs.hypervisorNodeseditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
      this.getvmsHypervisorNodesList();
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`${val} items per page`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getvmsHypervisorNodesList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getvmsHypervisorNodesList();
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/hypervisorNodes/detail/${item.nodeId}`,
      });
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteComputeNode"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.delete"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      ).then(() => {
        mainApi
          .vmsHypervisorNodesDel(item.nodeId)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startDelete"),
              type: "success",
              duration: 2500,
            });
          })
          .catch((error) => {
            this.loading = false;
          });
      });
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getvmsHypervisorNodesList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.getvmsHypervisorNodesList();
    },
    getvmsHypervisorNodesList() {
      // 计算节点列表
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
        .vmsHypervisorNodesList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.nodeAllocationInfos;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getvmsHypervisorNodesListTime() {
      // 计算节点循环列表

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

      mainApi.vmsHypervisorNodesList(params).then((res) => {
        this.tableData = res.nodeAllocationInfos;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
