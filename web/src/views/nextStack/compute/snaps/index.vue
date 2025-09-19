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
        <el-form-item :label="$t('form.instance') + ':'">
          <el-select
            v-model="vmId"
            class="w-48"
            :placeholder="$t('form.pleaseInputInstanceName')"
            :remote-method="remoteMethod"
            :loading="vmLoading"
            filterable
            remote
            reserve-keyword
            @change="onVmChange"
          >
            <el-option
              v-for="(item, index) in vmListData"
              :key="index"
              :label="item.name"
              :value="item.instanceId"
              >{{ item.name }}</el-option
            >
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="onSubmit">{{
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
          @click="addSnaps"
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
        <el-table-column prop="name" :label="$t('form.name')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row)"
              >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="vmInstanceName" :label="$t('form.instance')">
          <template #default="scope">
            <router-link
              :to="'/nextStack/vm/detail/' + scope.row.vmInstanceId"
              class="text-blue-400"
              >{{ scope.row.vmInstanceName || "-" }}</router-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="current" :label="$t('form.currentSnapshot')">
          <template #default="scope">
            <span>
              <el-tag
                :size="$store.state.nextStack.viewSize.tagStatus"
                :type="scope.row.current ? 'success' : 'danger'"
                >{{
                  scope.row.current ? $t("form.yes") : $t("form.no")
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
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="toEdit(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
                </div>
                <div class="icon_cz" @click="toDelete(scope.row)">
                  <i class="el-icon-delete" />
                  {{ $t("form.delete") }}
                </div>
                <div class="icon_cz" @click="toRestore(scope.row)">
                  <img
                    src="@/assets/nextStack/btn/rollBack.png"
                    class="w-3 inline-block mr-1"
                    alt=""
                  />
                  {{ $t("form.rollBack") }}
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
        class="flex justify-end mt-4 px-4"
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
      :title="$t('form.createSnapshot')"
      :visible.sync="addDialogVisible"
      width="800px"
      :before-close="addHandleClose"
    >
      <snapsadd ref="snapsaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editSnapshot')"
      :visible.sync="editDialogVisible"
      width="500px"
      :before-close="editHandleClose"
    >
      <snapsedit :id="editId" ref="snapseditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import snapsadd from "./add.vue";
import snapsedit from "./edit.vue";

export default {
  components: {
    snapsadd,
    snapsedit,
  },
  data() {
    return {
      filtersFun: filtersFun,
      addDialogVisible: false,
      editDialogVisible: false, // 编辑规格弹窗
      editId: "",
      timer: "",
      vmListData: [],
      vmId: "",
      loading: false,
      vmLoading: false,
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
    this.getsnapsList();
  },
  mounted() {
    this.timer = setInterval(async () => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getsnapsListTime();
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
    addSnaps() {
      // 创建快照
      this.addDialogVisible = true;
      this.$nextTick(() => {
        this.$refs.snapsaddRef.init();
      });
    },
    addHandleClose() {
      // 创建快照关闭
      this.$refs.snapsaddRef.resetForm();
      this.addDialogVisible = false;
      this.getsnapsList();
    },
    toEdit(item) {
      // 编辑规格
      this.editId = item.snapId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      this.editId = "";
      this.editDialogVisible = false;
    },
    toDetail(row) {
      // 快照详情
      this.$router.push({
        path: "/nextStack/snap/detail/" + row.snapId,
      });
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      $store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getsnapsList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.form.page_num = val;
      this.getsnapsList();
    },
    toRestore(row) {
      // 回滚
      this.$confirm(
        this.$t("message.confirmRollBack"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi.snapsSwitch(row.snapId).then((res) => {
            this.$notify({
              title: this.$t("message.startRollBack"),
              type: "success",
              duration: 2500,
            });
            this.getsnapsList();
          });
        })
        .catch(() => {});
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteSnapshot"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.delete"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi.snapsDel(item.snapId).then((res) => {
            this.$notify({
              title: this.$t("message.startDelete"),
              type: "success",
              duration: 2500,
            });
            this.getsnapsList();
          });
        })
        .catch(() => {});
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getsnapsList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.vmId = ""; // 实例id
      this.vmListData = []; // 实例列表
      this.getsnapsList();
    },
    getsnapsList() {
      // 快照列表
      this.loading = true;

      const params = {
        name: this.form.name,
        instance_id: this.vmId,
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
        .snapsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.snaps;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getsnapsListTime() {
      // 快照循环列表

      const params = {
        name: this.form.name,
        instance_id: this.vmId,
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

      mainApi.snapsList(params).then((res) => {
        this.tableData = res.snaps;
        this.form.total = res.totalNum;
      });
    },
    onVmChange(val) {
      // 实例列表
      this.vmId = val;
      this.getsnapsList();
    },
    remoteMethod(query) {
      // 实例搜索
      if (query) {
        this.getVmsInstabcesList(query);
      } else {
        this.vmListData = [];
      }
    },
    getVmsInstabcesList(query) {
      // 实例列表
      this.vmLoading = true;
      const params = {
        name: query,
        page_num: 1,
        page_size: 10,
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
          this.vmListData = res.vmInstancesInfo;
        })
        .catch((error) => {
          this.vmLoading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.snapsPage {
}
</style>
