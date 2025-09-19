<template>
  <div class="warpMain">
    <!-- <h5 class="mb-3 px-5 pt-2">
      <span class="text-lg">{{ $route.meta.title }}</span>
      <el-divider class="!my-2"></el-divider>
    </h5> -->
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
          @click="addvpc"
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
        <el-table-column prop="cidr" :label="$t('form.ipv4Cidr')" />
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getStatus(scope.row.phaseStatus, 'tag')"
              >{{
                filtersFun.getStatus(scope.row.phaseStatus, "status")
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
        <el-table-column
          prop="address"
          :label="$t('form.operation')"
          width="120"
        >
          <template #default="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="addSubNet(scope.row)">
                  <i class="el-icon-plus" />
                  {{ $t("form.addSubnet") }}
                </div>
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
      :title="$t('form.createVPCNetwork')"
      :visible.sync="addDialogVisible"
      width="800px"
      :before-close="addHandleClose"
    >
      <vpcadd ref="vpcaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editVPCNetwork')"
      :visible.sync="editDialogVisible"
      width="500px"
      :before-close="editHandleClose"
    >
      <vpcedit :id="editId" ref="vpceditRef" @close="editHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.createSubnet')"
      :visible.sync="addSubNetDialogVisible"
      width="600px"
      :before-close="addSubNetHandleClose"
      destroy-on-close
    >
      <subnetadd
        ref="subnetaddRef"
        :vpc-info="addSubNetVPCInfo"
        @close="addSubNetHandleClose"
      />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import vpcadd from "./add.vue";
import vpcedit from "./edit.vue";
import subnetadd from "../subNet/add.vue";

export default {
  components: {
    vpcadd,
    vpcedit,
    subnetadd,
  },
  data() {
    return {
      filtersFun: filtersFun,
      addSubNetVPCInfo: {}, // 创建子网VPCID
      addSubNetDialogVisible: false, // 创建子网弹窗
      addDialogVisible: false, // 创建VPC网络弹窗
      editDialogVisible: false, // 编辑VPC网络弹窗
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
    this.getVpcList();
  },
  mounted() {
    this.timer = setInterval(async () => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getVpcListTime();
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
    addSubNet(item) {
      // 创建子网
      this.addSubNetVPCInfo = item;
      this.addSubNetDialogVisible = true;
    },
    addSubNetHandleClose() {
      // 关闭创建子网弹窗
      this.$refs.subnetaddRef.resetForm();
      this.addSubNetVPCInfo = {};
      this.addSubNetDialogVisible = false;
    },
    addvpc() {
      this.addDialogVisible = true;
      setTimeout(() => {
        this.$refs.vpcaddRef.changeform();
      });
    },
    addHandleClose() {
      this.addDialogVisible = false;
    },
    toEdit(item) {
      // 编辑
      this.editId = item.vpcId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      // 编辑
      this.$refs.vpceditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
    },
    toDetail(item) {
      this.$router.push({
        path: "/nextStack/vpc/detail/" + item.vpcId,
      });
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`${val} items per page`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getVpcList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getVpcList();
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteVPCNetwork"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .vpcDel(item.vpcId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getVpcList();
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
      this.getVpcList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.getVpcList();
    },
    getVpcList() {
      // VPC列表
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
        .vpcList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.vpcs;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getVpcListTime() {
      // VPC循环列表
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

      mainApi.vpcList(params).then((res) => {
        this.tableData = res.vpcs;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.vpcPage {
}
</style>
