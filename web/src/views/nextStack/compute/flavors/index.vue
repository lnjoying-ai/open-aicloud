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
            :placeholder="$t('form.pleaseInputFlavorName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.type') + ':'">
          <el-select
            v-model="form.type"
            class="w-48"
            :placeholder="$t('form.pleaseSelectType')"
            @change="onSubmit"
          >
            <el-option :label="$t('form.all')" value="0" />
            <el-option :label="$t('form.instance')" value="1" />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit">
            {{ $t("form.query") }}
          </el-button>
          <el-button class="resetBtn" @click="onReset">
            {{ $t("form.reset") }}
          </el-button>
        </el-form-item>
        <el-button
          v-if="kind == 0 || kind == 1"
          type="primary"
          class="float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="addFlavors"
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
        <el-table-column prop="type" :label="$t('form.type')">
          <template #default="scope">
            <span v-if="scope.row.gpuCount">{{ $t("form.gpuCompute") }}</span>
            <span v-else>{{ $t("form.generalCompute") }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="cpu" :label="$t('form.cpu')">
          <template #default="scope">
            {{ scope.row.cpu || "-" }}
            {{ $t("unit.cpu") }}
          </template>
        </el-table-column>
        <el-table-column prop="mem" :label="$t('form.mem')">
          <template #default="scope">
            {{ scope.row.mem || "-" }}
            {{ $t("unit.mem") }}
          </template>
        </el-table-column>

        <el-table-column prop="gpuCount" :label="$t('form.gpu')">
          <template #default="scope">
            <span v-if="scope.row.gpuCount && scope.row.gpuCount > 0">
              {{ scope.row.gpuName }}*{{ scope.row.gpuCount }}
            </span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="needIb" :label="$t('form.ib')">
          <template #default="scope">
            <span v-if="scope.row.needIb">
              <i style="color: rgb(103, 194, 58)" class="el-icon-success" />
            </span>
            <span v-if="!scope.row.needIb">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />
        <el-table-column
          v-if="kind == 0 || kind == 1"
          :label="$t('form.operation')"
          width="120"
        >
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
      :title="$t('form.createFlavor')"
      :visible.sync="addDialogVisible"
      width="600px"
      :before-close="addHandleClose"
    >
      <flavorsadd ref="flavorsaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editFlavor')"
      :visible.sync="editDialogVisible"
      width="500px"
      :before-close="editHandleClose"
    >
      <flavorsedit :id="editId" ref="flavorseditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

import flavorsadd from "./add.vue";
import flavorsedit from "./edit.vue";
// import flavorsdetail from "./detail.vue";
export default {
  components: {
    flavorsadd,
    flavorsedit,
  },
  data() {
    return {
      addDialogVisible: false, // 创建规格弹窗
      editDialogVisible: false, // 编辑规格弹窗
      editId: "",
      timer: "",
      loading: false,
      tableData: [],
      form: {
        name: "",
        type: "0",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
    };
  },
  created() {
    this.getflavorsList();
  },
  mounted() {
    this.timer = setInterval(async () => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getflavorsListTime();
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
    addFlavors() {
      // 创建规格
      this.addDialogVisible = true;
    },
    addHandleClose() {
      this.$refs.flavorsaddRef.resetForm();
      this.addDialogVisible = false;
      this.getflavorsList();
    },
    toEdit(item) {
      // 编辑规格
      this.editId = item.flavorId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      this.editId = "";
      this.editDialogVisible = false;
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/flavor/detail/${item.flavorId}`,
      });
    },
    toDelete(item) {
      // 确认删除该规格吗？
      this.$confirm(
        this.$t("message.confirmDeleteFlavor"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          // 删除
          mainApi
            .flavorsDel(item.flavorId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getflavorsList();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getflavorsList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getflavorsList();
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getflavorsList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.type = "0";
      this.form.page_num = 1;
      this.getflavorsList();
    },
    getflavorsList() {
      // 规格列表
      this.loading = true;

      const params = {
        name: this.form.name,
        type: this.form.type,
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
        .flavorsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.flavors;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getflavorsListTime() {
      // 规格循环列表

      const params = {
        name: this.form.name,
        type: this.form.type,
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

      mainApi.flavorsList(params).then((res) => {
        this.tableData = res.flavors;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.flavorsPage {
}
</style>
