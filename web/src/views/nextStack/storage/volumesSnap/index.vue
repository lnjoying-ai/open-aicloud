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
          @click="addEipPool"
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

        <el-table-column prop="volumeName" :label="$t('form.cloudDisk')">
          <template #default="scope">
            {{ scope.row.volumeName }}
          </template>
        </el-table-column>
        <el-table-column prop="isCurrent" :label="$t('form.currentSnapshot')">
          <template #default="scope">
            <span>
              <el-tag
                :size="$store.state.nextStack.viewSize.tagStatus"
                :type="scope.row.isCurrent ? 'success' : 'danger'"
                >{{
                  scope.row.isCurrent ? $t("form.yes") : $t("form.no")
                }}</el-tag
              >
            </span>
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

        <el-table-column prop="description" :label="$t('form.description')">
          <template #default="scope">
            <div class="text-ellipsis-2">
              {{ scope.row.description }}
            </div>
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
                <div class="icon_cz" @click="toSwitch(scope.row)">
                  <img
                    src="@/assets/nextStack/btn/rollBack.png"
                    class="w-3 inline-block mr-1"
                    alt=""
                  />{{ $t("form.rollBack") }}
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
    <el-dialog
      :title="$t('form.createVolumeSnapshot')"
      :visible.sync="addDialogVisible"
      width="600px"
      :before-close="addHandleClose"
    >
      <volumesSnapadd ref="volumesSnapaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editVolumeSnapshot')"
      :visible.sync="editDialogVisible"
      width="600px"
      :before-close="editHandleClose"
    >
      <volumesSnapedit
        :id="editId"
        ref="volumesSnapeditRef"
        @close="editHandleClose"
      />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import volumesSnapadd from "./add.vue";
import volumesSnapedit from "./edit.vue";

export default {
  components: {
    volumesSnapadd,
    volumesSnapedit,
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
    this.getVolumesList();
  },
  mounted() {
    this.timer = setInterval(async () => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getVolumesListTime();
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
    addEipPool() {
      // 创建云盘快照
      this.addDialogVisible = true;
    },
    addHandleClose() {
      this.$refs.volumesSnapaddRef.resetForm();
      this.addDialogVisible = false;
      this.getVolumesList();
    },
    toEdit(item) {
      this.editId = item.volumeSnapId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      this.$refs.volumesSnapeditRef.resetForm();
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
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/volumesSnap/detail/${item.volumeSnapId}`,
      });
    },
    toSwitch(item) {
      this.$confirm(
        this.$t("message.confirmRestoreVolumeSnapshot"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .volumesSnapsSwitch(item.volumeSnapId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startRestore"),
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
    toDelete(item) {
      this.$confirm(
        this.$t("message.confirmDeleteVolumeSnapshot"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .volumesSnapsDel(item.volumeSnapId)
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
      this.form.page_num = 1;
      this.getVolumesList();
    },
    getVolumesList() {
      // 云盘快照列表
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
        .volumesSnapsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.volumeSnaps;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getVolumesListTime() {
      // 云盘快照列表
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
      mainApi.volumesSnapsList(params).then((res) => {
        this.tableData = res.volumeSnaps;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetPage {
}
</style>
