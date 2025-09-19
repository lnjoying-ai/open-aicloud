<template>
  <div class="warpMain">
    <div>
      <el-form
        :model="form"
        :inline="true"
        label-width="100px"
        size="small"
        class="el-form demo-form-inline el-form--inline"
      >
        <el-form-item :label="$t('form.instanceGroupName') + ':'">
          <el-input
            v-model="form.name"
            class="w-48"
            :placeholder="$t('form.pleaseInputInstanceGroupName')"
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
          @click="addVmGroup"
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
        <el-table-column prop="name" :label="$t('form.instanceGroupName')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row)"
              >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="$t('form.desc')">
          <template #default="scope">
            {{ scope.row.description ? scope.row.description : "-" }}
          </template>
        </el-table-column>
        <el-table-column prop="instanceCount" :label="$t('form.instanceCount')">
          <template #default="scope">
            {{ scope.row.instanceCount }}
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
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";

export default {
  components: {},
  data() {
    return {
      filtersFun: filtersFun,
      addDialogVisible: false, // 创建实例组
      editDialogVisible: false, // 编辑实例组
      editId: "",
      timer: "",
      vmListData: [],
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
    this.getvmGroupList();
  },
  mounted() {
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getvmGroupListTime();
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
    addVmGroup() {
      this.$router.push({
        path: "/nextStack/vmGroup/add",
      });
    },

    toEdit(item) {
      this.$router.push({
        path: "/nextStack/vmGroup/edit/" + item.instanceGroupId,
      });
    },
    toDetail(item) {
      this.$router.push({
        path: "/nextStack/vmGroup/detail/" + item.instanceGroupId,
      });
    },

    handleSizeChange() {
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getvmGroupList();
    },
    handleCurrentChange() {
      this.form.page_num = val;
      this.getvmGroupList();
    },
    toDelete(item) {
      this.$confirm(
        this.$t("message.confirmDeleteFlavor"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.delete"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .vmInstanceGroupsDel(item.instanceGroupId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getvmGroupList();
            })
            .catch((error) => {
              loading.value = false;
            });
        })
        .catch(() => {});
    },
    onSubmit() {
      this.form.page_num = 1;
      this.getvmGroupList();
    },
    onReset() {
      this.form.name = "";
      this.form.page_num = 1;
      this.getvmGroupList();
    },
    getvmGroupList() {
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
        .vmInstanceGroupsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.instanceGroupInfos;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getvmGroupListTime() {
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
      mainApi.vmInstanceGroupsList(params).then((res) => {
        this.tableData = res.instanceGroupInfos;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.vmGroupPage {
}
</style>
