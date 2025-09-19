<template>
  <div class="vpcAddPage h-full relative mt-2">
    <div class="overflow-hidden absolute z-10 right-4 -top-12">
      <el-radio-group
        v-model="tabContent"
        :size="$store.state.nextStack.viewSize.tabChange"
        class="overflow-hidden align-middle float-right"
      >
        <el-radio-button label="info">{{
          $t("form.detailInfo")
        }}</el-radio-button>
        <el-radio-button label="img">{{ $t("form.topology") }}</el-radio-button>
      </el-radio-group>
    </div>

    <el-card v-if="tabContent == 'img'" class="!border-none mb-3">
      <!-- <template #header>
        <div class="">
          <span>拓扑图</span>
        </div>
      </template> -->
      <div class="text item mt-2">
        <visNetwork :id="$route.params.id" />
      </div>
    </el-card>

    <el-form
      v-if="tabContent == 'info'"
      v-loading="loading"
      :size="$store.state.nextStack.viewSize.main"
      :element-loading-text="$t('domain.loading')"
      :model="form"
      label-width="120px"
    >
      <el-card class="mb-2 mt-2 pb-3" shadow="never">
        <template #header>
          <div class="">
            <span>{{ $t("form.basicInfo") }}</span>
          </div>
        </template>
        <div class="text item">
          <el-form-item
            :label="$t('form.name') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.id') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.vpcId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.ipv4Cidr') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.cidr || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.status') + ':'"
            style="width: 50%; float: left"
          >
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getStatus(form.phaseStatus, 'tag')"
              >{{ filtersFun.getStatus(form.phaseStatus, "status") }}</el-tag
            >
          </el-form-item>
          <el-form-item
            :label="$t('form.subnetCount') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.count || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.createTime || "-" }}</span>
          </el-form-item>
        </div>
      </el-card>
    </el-form>
    <el-card v-if="tabContent == 'info'" class="!border-none mb-3 pb-3">
      <template #header>
        <div class="">
          <span>{{ $t("form.subnetInfo") }}</span>
          <el-button
            type="primary"
            size="mini"
            class="float-right"
            @click="addSubnet()"
            >{{ $t("form.addSubnet") }}</el-button
          >
        </div>
      </template>
      <div class="text item">
        <el-table
          :data="subnetInvpcList"
          max-height="calc(100% - 3rem)"
          class="!overflow-y-auto"
          stripe
          :scrollbar-always-on="false"
        >
          <el-table-column prop="date" :label="$t('form.name')">
            <template #default="scope">
              {{ scope.row.name }}
            </template>
          </el-table-column>
          <el-table-column prop="date" :label="$t('form.id')">
            <template #default="scope">
              {{ scope.row.subnetId }}
            </template>
          </el-table-column>

          <el-table-column prop="cidr" :label="$t('form.ipv4Cidr')" />
          <el-table-column prop="gatewayIp" :label="$t('form.gateway')" />
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
              <span>{{
                scope.row.eeUserName ? scope.row.eeUserName : "-"
              }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" :label="$t('form.createTime')" />
          <el-table-column prop="updateTime" :label="$t('form.updateTime')" />
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
    </el-card>
    <el-dialog
      :title="$t('form.addSubnet')"
      :visible.sync="addDialogVisible"
      width="600px"
      :before-close="addHandleClose"
      destroy-on-close
    >
      <subnetadd ref="subnetaddRef" :vpc-info="form" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editSubnet')"
      :visible.sync="editDialogVisible"
      width="500px"
      :before-close="editHandleClose"
      destroy-on-close
    >
      <subnetedit :id="editId" ref="subneteditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import visNetwork from "@/components/nextStack/visNetwork.vue";
import subnetadd from "../subNet/add.vue";
import subnetedit from "../subNet/edit.vue";
export default {
  components: {
    visNetwork,
    subnetadd,
    subnetedit,
  },
  data() {
    return {
      addDialogVisible: false, // 创建子网弹窗
      editDialogVisible: false,
      editId: "",
      filtersFun: filtersFun,
      loading: false,
      tabContent: "info",
      form: {},
      subnetInvpcList: [],
    };
  },
  created() {
    this.getDetail();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    getDetail() {
      // 获取详情
      this.loading = true;
      var id = this.$route.params.id;

      mainApi
        .vpcDetail(id)
        .then((res) => {
          this.form = res;
          this.getSubNetListInVpc(res.vpcId);
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getSubNetListInVpc(id) {
      // 获取子网列表
      mainApi
        .subnetInVpc({ vpc_id: id })
        .then((res) => {
          this.loading = false;
          this.subnetInvpcList = res.subnets;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    toEdit(item) {
      // 编辑
      this.editId = item.subnetId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      // 编辑
      this.$refs.subneteditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
      this.getSubNetListInVpc(this.form.vpcId);
    },
    addSubnet() {
      // 创建子网
      this.addDialogVisible = true;
    },
    addHandleClose() {
      // 关闭创建子网弹窗
      this.$refs.subnetaddRef.resetForm();
      this.addDialogVisible = false;
      this.getSubNetListInVpc(this.form.vpcId);
    },
    toDelete(item) {
      // 删除
      this.$confirm(this.$t("message.confirmDeleteSubnet"), {
        confirmButtonText: this.$t("message.confirm"),
        cancelButtonText: this.$t("message.cancel"),
        type: "warning",
      })
        .then(() => {
          this.loading = true;
          mainApi
            .subnetsDel(item.subnetId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getSubNetListInVpc(this.form.vpcId);
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.vpcAddPage {
}
</style>
