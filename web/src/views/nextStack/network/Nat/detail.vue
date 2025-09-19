<template>
  <div class="bmsAddPage h-full">
    <el-form
      v-loading="loading"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="120px"
      :element-loading-text="$t('domain.loading')"
    >
      <el-card class="!border-none mb-2 pb-3 mt-2">
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
            <span>{{ form.mapName || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.id') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.eipMapId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.createTime || "-" }}</span>
          </el-form-item>
        </div>
      </el-card>

      <el-card class="!border-none mb-2 pb-3">
        <template #header>
          <div class="">
            <span>{{ $t("form.networkConfig") }}</span>
          </div>
        </template>
        <div class="text item">
          <el-form-item
            :label="$t('form.vpcNetwork') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              <router-link
                :to="'/nextStack/vpc/detail/' + form.vpcId"
                target="_blank"
                class="text-blue-400"
                >{{ form.vpcName }}</router-link
              >
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.subnet') + ':'"
            style="width: 50%; float: left"
          >
            <span> {{ form.subnetName }}({{ form.subnetCidr }}) </span>
          </el-form-item>
        </div>
      </el-card>

      <el-card class="!border-none mb-2 pb-3">
        <template #header>
          <div class="">
            <span>{{ $t("form.instance") }}</span>
          </div>
        </template>
        <div class="text item">
          <div class="overflow-hidden">
            <el-form-item
              :label="$t('form.instanceType') + ':'"
              style="width: 50%; float: left"
            >
              <span>{{
                form.vm ? $t("form.cloudHost") : $t("form.bareMetal")
              }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.instance') + ':'"
              style="width: 50%; float: left"
            >
              <span>
                <router-link
                  v-if="form.vm"
                  target="_blank"
                  :to="'/nextStack/vm/detail/' + form.instanceId"
                  class="text-blue-400"
                  >{{ form.instanceName }}</router-link
                >
                <router-link
                  v-else
                  target="_blank"
                  :to="'/nextStack/bms/detail/' + form.instanceId"
                  class="text-blue-400"
                  >{{ form.instanceName }}</router-link
                >
                ({{ form.insideIp }})
              </span>
            </el-form-item>
            <!-- <el-form-item label="IP：" style="width:50%;float: left;margin:0">
              <span>{{ form.insideIp || '-' }}</span>
            </el-form-item> -->

            <el-form-item
              :label="$t('form.protocolType') + ':'"
              style="width: 50%; float: left"
            >
              <span>{{
                form.oneToOne ? $t("form.ip") : $t("form.tcpUdp")
              }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.eipAddress') + ':'"
              style="width: 50%; float: left"
            >
              <span>{{ form.publicIp || form.eipAddress || "-" }}</span>
            </el-form-item>
          </div>
          <div v-if="!form.oneToOne" class="mt-4">
            <el-form-item :label="$t('form.rule') + ':'" style="width: 800px">
              <div class="text-right">
                <el-button type="primary" size="mini" @click="toPort(form)">{{
                  $t("form.edit")
                }}</el-button>
              </div>
              <el-table :data="form.portMaps" style="width: 100%">
                <el-table-column type="index" />
                <el-table-column prop="protocol" :label="$t('form.protocol')">
                  <template #default="scope">
                    <span v-if="scope.row.protocol === 0">TCP</span>
                    <span v-if="scope.row.protocol === 1">UDP</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="globalPort"
                  :label="$t('form.publicPort')"
                />
                <el-table-column
                  prop="localPort"
                  :label="$t('form.privatePort')"
                />
              </el-table>
            </el-form-item>
          </div>
        </div>
      </el-card>
    </el-form>
    <el-dialog
      :title="$t('form.editPort')"
      :visible.sync="portDialogVisible"
      width="800px"
      :before-close="portHandleClose"
    >
      <portNat :id="editId" ref="portNatref" @close="portHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import portNat from "./port.vue";

export default {
  components: {
    portNat,
  },
  data() {
    return {
      loading: false,
      form: {},
      portDialogVisible: false,
      editId: "",
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
      this.getDetail();
    },
    getDetail() {
      this.loading = true;
      var id = this.$route.params.id;
      // 获取详情
      mainApi
        .portMapDetail(id)
        .then((res) => {
          this.form = res;
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
