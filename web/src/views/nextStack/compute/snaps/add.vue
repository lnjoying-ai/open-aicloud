<template>
  <div class="snapsAddPage h-full">
    <el-form
      ref="addVmForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <div class="text item">
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            class="w-60"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            class="w-96"
            maxlength="255"
            show-word-limit
            type="textarea"
            :rows="2"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.network') + ':'" required>
          <el-select
            v-model="vpcId"
            class="mr-2 ml-0 w-60"
            :placeholder="$t('form.pleaseSelectVPCNetwork')"
            @change="vpcIdChange()"
          >
            <el-option
              v-for="(item, index) in vpcList"
              :key="index"
              :label="item.name + ' (' + item.cidr + ')'"
              :value="item.vpcId"
            />
          </el-select>
          <el-select
            v-model="subnetId"
            class="ml-0 w-60"
            :placeholder="$t('form.pleaseSelectSubnet')"
            :disabled="!vpcId"
            @change="subnetChange(subnetId)"
          >
            <template v-for="(item, index) in subnetsDataList">
              <el-option
                v-if="vpcId == item.vpcId"
                :key="index"
                :label="item.name + ' (' + item.cidr + ')'"
                :value="item.subnetId"
              />
            </template>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.instance') + ':'" prop="vmInstanceId">
          <el-select
            v-model="form.vmInstanceId"
            class="w-60 mr-2 ml-0"
            :disabled="!subnetId"
            :placeholder="$t('form.pleaseSelectInstance')"
          >
            <el-option
              v-for="(item, index) in tableData"
              :key="index + 1"
              :label="item.name"
              :value="item.instanceId"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toFlavorssAdd()"
          >{{ $t("form.createImmediately") }}</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

export default {
  data() {
    return {
      loading: false,
      tableData: [],
      vpcList: [],
      subnetsDataList: [],
      vpcId: "",
      subnetId: "",
      form: {
        name: "",
        description: "",
        vmInstanceId: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        subnetId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectVPCNetworkAndSubnet"),
            trigger: "change",
          },
        ],
        vmInstanceId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectVPCNetworkAndSubnetInstance"),
            trigger: "change",
          },
        ],
      },
    };
  },

  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  // 离开页面时清除定时器
  beforeDestroy() {},
  methods: {
    init() {
      // 初始化
      this.getVpcList(); // VPC列表
      this.getSubNetList(); // 子网列表
    },

    resetForm() {
      // 重置
      this.vpcId = "";
      this.subnetId = "";
      this.$refs.addVmForm.resetFields();
    },
    toFlavorssAdd() {
      // 快照add
      this.loading = true;
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .snapsAdd(this.form)
            .then((res) => {
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.loading = false;
              this.$emit("close");
              this.resetForm();
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    getVmsInstabcesList() {
      // 实例列表

      mainApi
        .vmsInstabcesList({ subnet_id: this.subnetId })
        .then((res) => {
          this.tableData = res.vmInstancesInfo;
        })
        .catch((error) => {});
    },
    getVpcList() {
      // VPC列表
      mainApi
        .vpcList({ vpc_phase: 1 })
        .then((res) => {
          this.vpcList = res.vpcs;
        })
        .catch((error) => {});
    },
    getSubNetList() {
      // 子网列表

      mainApi
        .subnetsList()
        .then((res) => {
          this.subnetsDataList = res.subnets;
        })
        .catch((error) => {});
    },
    vpcIdChange() {
      // vpc 改变
      this.subnetId = "";
      this.form.vmInstanceId = "";
    },
    subnetChange(e) {
      // 子网变更

      if (e != "") {
        this.form.vmInstanceId = "";
        this.tableData = [];
        this.getVmsInstabcesList();
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.snapsAddPage {
}
</style>
