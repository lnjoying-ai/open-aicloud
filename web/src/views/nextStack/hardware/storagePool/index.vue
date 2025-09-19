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
            :placeholder="$t('form.pleaseInputStoragePoolName')"
          />
        </el-form-item>

        <el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="onSubmit">{{
            $t("form.query")
          }}</el-button>
          <el-button class="resetBtn" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
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

        <el-table-column prop="type" :label="$t('form.type')">
          <template #default="scope">
            <span v-if="scope.row.type == 1">{{ $t("form.fileSystem") }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="paras"
                         label="paras">
          <template #default="scope">
            {{ scope.row.paras }}
          </template>
        </el-table-column> -->
        <el-table-column prop="description" :label="$t('form.description')">
          <template #default="scope">
            <div class="text-ellipsis-2">
              {{ scope.row.description ? scope.row.description : "-" }}
            </div>
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
      :title="$t('form.editStoragePool')"
      :visible.sync="editDialogVisible"
      width="600px"
      :before-close="editHandleClose"
    >
      <storagePooledit
        :id="editId"
        ref="storagePooleditRef"
        @close="editHandleClose"
      />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";

import storagePooledit from "./edit.vue";
export default {
  components: {
    storagePooledit,
  },
  data() {
    return {
      filtersFun: filtersFun,
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
    this.getStoragePools();
  },
  mounted() {
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getStoragePoolsTime();
      }
    }, 5000);
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`${val} items per page`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getStoragePools();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getStoragePools();
    },
    toEdit(item) {
      // 编辑
      this.editId = item.poolId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      // 编辑
      this.$refs.storagePooleditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
      this.getStoragePools();
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/storagePool/detail/${item.poolId}`,
      });
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteStoragePool"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .storagePoolsDel(item.poolId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getStoragePools();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {
          this.$notify({
            title: this.$t("message.cancelDelete"),
            type: "info",
            duration: 2500,
          });
        });
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getStoragePools();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.getStoragePools();
    },
    getStoragePools() {
      // 存储池列表
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
        .storagePoolsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.storagePools;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getStoragePoolsTime() {
      mainApi.storagePoolsList().then((res) => {
        this.tableData = res.storagePools;
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
