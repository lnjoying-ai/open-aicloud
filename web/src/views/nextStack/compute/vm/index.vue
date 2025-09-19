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
        <el-form-item :label="$t('form.eip') + ':'">
          <el-select
            v-model="form.eip_id"
            class="w-48"
            :placeholder="$t('form.pleaseSelectEip')"
            @change="onSubmit"
          >
            <el-option :label="$t('form.all')" value="" />
            <el-option
              v-for="(item, index) in eipTableData"
              :key="index"
              :label="item.publicIp || item.ipAddress"
              :value="item.eipId"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit">{{
            $t("form.query")
          }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button class="resetBt" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-button
          type="primary"
          class="float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="addVm"
          >{{ $t("form.create") }}</el-button
        >
      </el-form>
    </div>
    <div>
      <el-table
        v-loading="vmLoading"
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

        <el-table-column prop="hostname" :label="$t('form.hostname')" />
        <el-table-column prop="portInfo.ipAddress" :label="$t('form.ip')">
          <template #default="scope">
            <span>{{ scope.row.portInfo.ipAddress || "-" }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="eip" :label="$t('form.eip')">
          <template #default="scope">
            <span v-if="!scope.row.boundPhaseStatus">{{ "-" }}</span>
            <span v-if="scope.row.boundType && scope.row.boundType === 'port'">
              <span v-if="scope.row.boundPhaseStatus == 82">{{
                scope.row.publicIp || scope.row.eip || "-"
              }}</span>
              <el-tag
                v-else
                :size="$store.state.nextStack.viewSize.tagStatus"
                :type="
                  filtersFun.getVmToEipStatus(scope.row.boundPhaseStatus, 'tag')
                "
                >{{
                  filtersFun.getVmToEipStatus(
                    scope.row.boundPhaseStatus,
                    "status"
                  )
                }}</el-tag
              >
            </span>
            <span v-if="scope.row.boundType && scope.row.boundType === 'nat'">
              <span v-if="scope.row.boundPhaseStatus == 7">
                <span v-if="scope.row.eip">
                  <el-tag :size="$store.state.nextStack.viewSize.tagStatus">{{
                    $t("form.nat")
                  }}</el-tag>
                  {{ scope.row.publicIp || scope.row.eip }}
                </span>
                <span v-else>-</span>
              </span>
              <el-tag
                v-else
                :size="$store.state.nextStack.viewSize.tagStatus"
                :type="
                  filtersFun.getNatStatus(scope.row.boundPhaseStatus, 'tag')
                "
                >{{
                  filtersFun.getNatStatus(scope.row.boundPhaseStatus, "status")
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
            <span v-if="scope.row.phaseStatus == 66">
              <el-tooltip
                class="item"
                effect="dark"
                :content="$t('message.computeResourceInsufficient')"
                placement="top"
              >
                <i class="el-icon-info mt-1 ml-1" />
              </el-tooltip>
            </span>
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
          v-if="kind == 0 || kind == 1"
          prop="eeUserName"
          :label="$t('form.user')"
        >
          <template #default="scope">
            <span>{{ scope.row.eeUserName ? scope.row.eeUserName : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />

        <el-table-column :label="$t('form.operation')" width="90">
          <template #default="scope">
            <operate
              :key="scope.row.instanceId"
              :prop-vm-detail="scope.row"
              :prop-show-type="1"
              :prop-show-btn="[
                'poweron',
                'poweroff',
                'reboot',
                'edit',
                'resetPassword',
                'delete',
                'eip',
                'snaps',
                'flavor',
                'images',
                'transfer',
                'secGroup',
                'vnc',
                'renew',
              ]"
              @initVmList="onReset"
              @restartDetail="getVmsInstabcesList"
            />
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
import operate from "./operate.vue";
export default {
  components: {
    operate,
  },
  data() {
    return {
      eipTableData: [],
      filtersFun: filtersFun,
      timer: "",
      vmLoading: false,
      form: {
        eip_id: "",
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      tableData: [],
    };
  },

  created() {
    // this.getVmsInstabcesList(); //请求实例列表
  },
  mounted() {
    const params = {
      page_num: 1,
      page_size: 99999,
    };
    mainApi.eipsList(params).then((res) => {
      this.eipTableData = res.eips;
    });
    this.getrouteparams();
    this.getVmsInstabcesList(); // 请求实例列表
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getVmsInstabcesListTime(); // 请求实例循环列表
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
    // 路由中是否有参数
    getrouteparams() {
      if (this.$route.params && this.$route.params.eipId) {
        this.form.eip_id = this.$route.params.eipId;
        this.$confirm(
          this.$t("message.confirmUnbindInstance", {
            vmName: this.$route.params.vmName,
            ipAddress: this.$route.params.ipAddress,
          }),
          this.$t("message.unbind"),
          {
            confirmButtonText: this.$t("message.confirm"),
            cancelButtonText: this.$t("message.cancel"),
          }
        )
          .then(() => {
            mainApi
              .eipsDetach(this.form.eip_id)
              .then((res) => {
                this.$notify({
                  title: this.$t("message.startUnbind"),
                  type: "success",
                  duration: 2500,
                });
                this.getVmsInstabcesList();
              })
              .catch((error) => {});
          })
          .catch(() => {});
      }
    },
    addVm() {
      // 创建实例
      this.$router.push({
        path: "/nextStack/vm/add",
      });
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/vm/detail/${item.instanceId}`,
      });
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`${val} items per page`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getVmsInstabcesList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.form.page_num = val;
      this.getVmsInstabcesList();
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getVmsInstabcesList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.eip_id = "";
      this.form.page_num = 1;
      this.getVmsInstabcesList();
    },

    getVmsInstabcesList() {
      // 实例列表
      this.vmLoading = true;
      const params = {
        name: this.form.name,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
        eip_id: this.form.eip_id,
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
          this.tableData = res.vmInstancesInfo;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.vmLoading = false;
        });
    },
    getVmsInstabcesListTime() {
      // 实例循环列表
      const params = {
        name: this.form.name,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
        eip_id: this.form.eip_id,
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

      mainApi.vmsInstabcesList(params).then((res) => {
        this.tableData = res.vmInstancesInfo;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.vmPage {
  ::v-deep .el-table {
    .success-row {
      background-color: rgba(149, 212, 117, 0.3);
    }
  }
}

.hypervisorNodesDialog {
  ::v-deep .el-table__header .el-checkbox {
    // 找到表头那一行，然后把里面的复选框隐藏掉
    display: none !important;
  }
}
</style>
