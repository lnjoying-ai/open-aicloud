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
        <el-table-column prop="description" :label="$t('form.description')">
          <template #default="scope">
            <div class="text-ellipsis-2">
              {{ scope.row.description ? scope.row.description : "-" }}
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="createTime" :label="$t('form.createTime')">
          <template #default="scope">
            {{ scope.row.createTime ? formatDate(scope.row.createTime) : "" }}
          </template>
        </el-table-column>
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
      :title="$t('form.createEipPool')"
      :visible.sync="addDialogVisible"
      width="600px"
      :before-close="addHandleClose"
    >
      <eipPooladd ref="eipPooladdRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editEipPool')"
      :visible.sync="editDialogVisible"
      width="600px"
      :before-close="editHandleClose"
    >
      <eipPooledit :id="editId" ref="eipPooleditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";

import eipPooladd from "./add.vue";
import eipPooledit from "./edit.vue";

export default {
  components: {
    eipPooladd,
    eipPooledit,
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
    ...mapGetters(["kind"]),
  },
  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    addEipPool() {
      // 创建EIP Pool
      this.addDialogVisible = true;
    },
    addHandleClose() {
      // 创建EIP Pool
      this.$refs.eipPooladdRef.resetForm();
      this.addDialogVisible = false;
      this.getEipList();
    },
    toEdit(item) {
      // 编辑
      this.editId = item.poolId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      // 编辑
      this.$refs.eipPooleditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`每页 ${val} 条`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getEipList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`当前页: ${val}`);
      this.form.page_num = val;
      this.getEipList();
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/eipPool/detail/${item.poolId}`,
      });
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteEipPool"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          // 删除
          mainApi
            .eipPoolsDel(item.poolId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.deleteSuccess"),
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
      // EIP列表
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
        .eipPoolsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.eipPools;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getEipListTime() {
      // EIP列表
      mainApi.eipPoolsList().then((res) => {
        this.tableData = res.eipPools;
        this.form.total = res.totalNum;
      });
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
.subNetPage {
}
</style>
