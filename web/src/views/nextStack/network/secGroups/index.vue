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
          <el-button
            type="primary"
            class="resetBtn"
            icon="el-icon-search"
            @click="onSubmit"
            >{{ $t("form.query") }}</el-button
          >
          <el-button class="resetBtn" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-button
          type="primary"
          :size="$store.state.nextStack.viewSize.main"
          class="float-right"
          @click="addSecGroups"
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
              @click="toDetail(scope.row, 'info')"
              >{{ scope.row.name }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('form.secGroupsRule')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row, 'info')"
              >{{ scope.row.ruleCount }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('form.associatedInstance')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row, 'instances')"
              >{{ scope.row.computeInstanceCount }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="
                filtersFun.getSecGroupsStatus(scope.row.phaseStatus, 'tag')
              "
              >{{
                filtersFun.getSecGroupsStatus(scope.row.phaseStatus, "status")
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
        <el-table-column prop="description" :label="$t('form.desc')">
          <template #default="scope">
            {{ scope.row.description ? scope.row.description : "-" }}
          </template>
        </el-table-column>

        <el-table-column :label="$t('form.operation')" width="120">
          <template #default="scope">
            <div v-if="scope.row.name != 'default'">
              <div v-if="!scope.row.isDefault" class="czlb">
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
    <add
      class="w-0 h-0"
      :add-status="addSecGroupsStatus"
      :add-type="addType"
      :add-form="addForm"
      :sg-id="sgId"
      @closeAdd="onCloseAdd"
      @getSgsList="getSgsList"
    />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import add from "./add.vue";
import filtersFun from "@/utils/statusFun";

// import secGroupsdetail from "./detail.vue";

export default {
  components: {
    add,
  },
  data() {
    return {
      filtersFun: filtersFun,
      timer: "",
      loading: false,
      addType: "add", // 添加还是编辑 add-edit
      addForm: {
        // 编辑安全组
        name: "",
        description: "",
      },
      sgId: "",
      addSecGroupsStatus: false,
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
    this.getSgsList(); // sgs列表
  },
  mounted() {
    this.timer = setInterval(async () => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getSgsListTime(); // 请求sgs循环列表
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
    handleSizeChange(val) {
      // 改变每页显示数量
      this.form.page_size = val;
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.getSgsList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.form.page_num = val;
      this.getSgsList();
    },
    addSecGroups() {
      // 打开添加安全组
      this.addSecGroupsStatus = true;
    },
    onCloseAdd() {
      // 关闭添加安全组弹窗
      this.addSecGroupsStatus = false;
      this.addType = "add"; // 添加还是编辑 add-edit
      this.addForm.name = "";
      this.addForm.description = "";
      this.sgId = "";
    },
    toEdit(item) {
      // 编辑
      this.addSecGroups();
      this.addType = "edit";
      this.addForm.name = item.name;
      this.addForm.description = item.description;
      this.sgId = item.sgId;
    },
    toDetail(item, type) {
      // 详情
      this.$router.push({
        path: "/nextStack/secGroups/detail/" + item.sgId,
        query: {
          type: type,
        },
      });
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteSecGroup"),
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
            .sgsDel(item.sgId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getSgsList();
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
      this.getSgsList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.getSgsList();
    },
    getSgsList() {
      // sgs列表
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
        .sgsList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.securityGroups;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getSgsListTime() {
      // sgs列表
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

      mainApi.sgsList(params).then((res) => {
        this.tableData = res.securityGroups;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.sgsPage {
}
</style>
