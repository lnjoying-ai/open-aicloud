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
          <el-select v-model="form.eip_id" class="w-48" @change="onSubmit">
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
          <el-button class="resetBtn" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-button
          type="primary"
          class="float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="addNat"
          >{{ $t("form.create") }}</el-button
        >
      </el-form>
    </div>

    <div>
      <el-table
        v-loading="loading"
        :data="tableData"
        max-height="calc(100% - 3rem)"
        class="!overflow-y-auto"
        stripe
        :scrollbar-always-on="false"
        :element-loading-text="$t('domain.loading')"
      >
        <el-table-column prop="date" :label="$t('form.name')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row)"
              >{{ scope.row.mapName }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="eipAddress" :label="$t('form.publicIp')">
          <template #default="scope">
            {{ scope.row.publicIp || scope.row.eipAddress }}
          </template>
        </el-table-column>
        <el-table-column prop="name" width="140">
          <template #header>
            {{ $t("form.rulePort")
            }}<small
              >({{ $t("form.publicIp") }}/{{ $t("form.privateIp") }})</small
            >
          </template>
          <template #default="scope">
            <span v-if="scope.row.oneToOne">{{ $t("form.ip") }}</span>
            <span v-else>
              <span
                v-for="(item, index) in scope.row.portMaps"
                :key="index"
                class="block"
              >
                {{
                  item.protocol == 0 ? "TCP" : item.protocol == 1 ? "UDP" : ""
                }}:{{ item.globalPort }}/{{ item.localPort }}
              </span>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="vpcId" :label="$t('form.vpcNetwork')">
          <template #default="scope">
            <router-link
              :to="'/nextStack/vpc/detail/' + scope.row.vpcId"
              target="_blank"
              class="text-blue-400"
              >{{ scope.row.vpcName }}</router-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="subnetCidr" :label="$t('form.subnet')">
          <template #default="scope">
            <span class="leading-none inline-block">
              {{ scope.row.subnetName }}
              <small class="block">({{ scope.row.subnetCidr }})</small>
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="instanceName" :label="$t('form.instance')">
          <template #default="scope">
            <router-link
              :to="'/nextStack/vm/detail/' + scope.row.instanceId"
              target="_blank"
              class="text-blue-400"
              >{{ scope.row.instanceName }}</router-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getNatStatus(scope.row.phaseStatus, 'tag')"
              >{{
                filtersFun.getNatStatus(scope.row.phaseStatus, "status")
              }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />
        <el-table-column :label="$t('form.operation')" width="100">
          <template #default="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="toEdit(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
                </div>
                <div
                  v-if="!scope.row.oneToOne"
                  class="icon_cz"
                  @click="toPort(scope.row)"
                >
                  <i class="el-icon-tickets" />
                  {{ $t("form.portManagement") }}
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
      :title="$t('form.createNatGateway')"
      :visible.sync="addDialogVisible"
      width="1000px"
      :before-close="addHandleClose"
    >
      <natadd ref="nataddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editNatGateway')"
      :visible.sync="editDialogVisible"
      width="500px"
      :before-close="editHandleClose"
    >
      <EditNat :id="editId" ref="EditNatref" @close="editHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editPort')"
      :visible.sync="portDialogVisible"
      width="1000px"
      :before-close="portHandleClose"
    >
      <portNat :id="editId" ref="portNatref" @close="portHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";

import natadd from "./add.vue";
import EditNat from "./edit.vue";
import portNat from "./port.vue";
// import natdetail from "./detail.vue";

export default {
  components: {
    natadd,
    EditNat,
    portNat,
  },
  data() {
    return {
      editId: "",
      editDialogVisible: false,
      portDialogVisible: false,
      eipTableData: "",
      filtersFun: filtersFun,
      addDialogVisible: false,
      timer: "",
      loading: false,
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
  created() {},
  mounted() {
    // eip列表
    const params = {
      page_num: 1,
      page_size: 99999,
    };
    mainApi.eipsList(params).then((res) => {
      this.eipTableData = res.eips;
    });
    this.getrouteparams();
    this.getportMapList();
    this.timer = setInterval(() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getportMapListTime();
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
    // 打开编辑Nat网关 端口
    toPort(item) {
      // 编辑规格
      this.editId = item.eipMapId;
      this.portDialogVisible = true;
    },
    // 关闭编辑Nat网关端口
    portHandleClose() {
      this.editId = "";
      this.portDialogVisible = false;
      this.getportMapList();
    },
    // 打开编辑Nat网关名称
    toEdit(item) {
      // 编辑规格
      this.editId = item.eipMapId;
      this.editDialogVisible = true;
    },
    // 关闭编辑Nat网关名称
    editHandleClose() {
      this.editId = "";
      this.editDialogVisible = false;
      this.getportMapList();
    },
    // 路由中是否有参数
    getrouteparams() {
      if (this.$route.params && this.$route.params.eipId) {
        this.form.eip_id = this.$route.params.eipId;
      }
    },
    addNat() {
      this.addDialogVisible = true;
    },
    addHandleClose() {
      this.$refs.nataddRef.resetForm();
      this.addDialogVisible = false;
      this.getportMapList();
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      this.form.page_size = val;
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.getportMapList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.form.page_num = val;
      this.getportMapList();
    },
    toDetail(item) {
      // 详情
      this.$router.push({ path: "/nextStack/Nat/detail/" + item.eipMapId });
    },
    toDelete(item) {
      // 删除

      this.$confirm(
        this.$t("message.confirmDeleteNatGateway"),
        this.$t("message.delete"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .portMapDel(item.eipMapId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.onSubmit();
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getportMapList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.page_num = 1;
      this.form.eip_id = "";
      this.getportMapList();
    },
    getportMapList() {
      // 获取数据
      this.loading = true;

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
        .portMapList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.eipPortMaps;
          this.form.total = res.totalNum;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getportMapListTime() {
      // 获取数据

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

      mainApi.portMapList(params).then((res) => {
        this.tableData = res.eipPortMaps;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.natPage {
}
</style>
