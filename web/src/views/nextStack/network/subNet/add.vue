<template>
  <div class="subNetAddPage h-full">
    <el-form
      ref="addSubnetForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="150px"
    >
      <div class="text item">
        <el-form-item :label="$t('form.vpcNetwork') + ':'" prop="vpcId">
          {{ vpcInfo.name }}
        </el-form-item>
        <el-form-item :label="$t('form.vpcNetworkIpv4Cidr') + ':'" prop="vpcId">
          <el-input-number
            v-model="Vip1"
            disabled
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="Vip2"
            disabled
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="Vip3"
            disabled
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="Vip4"
            disabled
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
          />
          <span class="text-xl px-1">/</span>

          <el-select v-model="Vip5" disabled class="w-20">
            <span v-for="(item, index) in maskList" :key="index">
              <el-option :label="item" :value="item">{{ item }}</el-option>
            </span>
          </el-select>
        </el-form-item>
      </div>
      <div class="text item">
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            class="w-60"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.subnetIpv4Cidr') + ':'" prop="name">
          <el-input
            v-model="ip1"
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            :disabled="true"
            @input="changeIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input
            v-model="ip2"
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            :disabled="Vip5 > 15"
            @input="changeIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input
            v-model="ip3"
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            :disabled="Vip5 > 23"
            @input="changeIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input
            v-model="ip4"
            class="w-14"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            :disabled="ip5 < 25"
            @input="changeIp"
          />
          <span class="text-xl px-1">/</span>
          <el-tooltip v-model="ipStatus" :manual="true" placement="right">
            <div slot="content">
              <span
                class="inline-block w-4 h-4 bg-red-500 rounded-1/2 text-right leading-tight"
                >！</span
              >
              {{ $t("form.ipAddressAndMaskDoNotMatch") }}
            </div>
            <el-select v-model="ip5" class="w-20">
              <span v-for="(item, index) in maskList" :key="index">
                <el-option
                  v-show="item > Vip5 - 1"
                  :label="item"
                  :value="item"
                  >{{ item }}</el-option
                >
              </span>
              <el-option :label="29" :value="29">29</el-option>
            </el-select>
          </el-tooltip>
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toSubNetAdd"
          >{{ $t("form.createImmediately") }}</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";
import netmask from "netmask";
var Netmask = netmask.Netmask;

export default {
  components: {},
  props: {
    vpcInfo: {
      type: Object,
      default: () => {
        return {};
      },
    },
  },
  data() {
    return {
      loading: false,
      ip1: 0, // IP分配
      ip2: 0, // IP分配
      ip3: 0, // IP分配
      ip4: 0, // IP分配
      ip5: 24, // IP分配
      ipStatus: false, // IP状态
      Vip1: 0, // IP分配
      Vip2: 0, // IP分配
      Vip3: 0, // IP分配
      Vip4: 0, // IP分配
      Vip5: 0, // IP分配
      maskList: [
        8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
        26, 27, 28,
      ],
      form: {
        // 表单
        name: "Subnet-" + this.$script.createRandomStr(8, "a0"),
        vpcId: "",
        addressType: 0, // 0：IPV4 1：IPV6
        cidr: "",
      },
      tableData: [],
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {},
  mounted() {
    console.log(this.vpcInfo);
    this.form.vpcId = this.vpcInfo.vpcId;
    this.vpcChange(this.vpcInfo);
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  watch: {
    ip5(newValue) {
      this.changeIp();
    },
    vpcInfo(newValue) {
      if (!newValue.vpcId) {
        return;
      }
      this.form.vpcId = newValue.vpcId;
      this.vpcChange(newValue);
    },
  },
  methods: {
    changeIp() {
      // IP变动
      this.ipStatus = false;
      this.ip1 = this.$script.parseIntIpNum(this.ip1);
      this.ip2 = this.$script.parseIntIpNum(this.ip2);
      this.ip3 = this.$script.parseIntIpNum(this.ip3);
      this.ip4 = this.$script.parseIntIpNum(this.ip4);
      const ip = `${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}/${this.ip5}`;
      const netBlock = new Netmask(ip);
      if (`${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}` == netBlock.base) {
        console.log("ok");
      } else {
        this.ipStatus = true;
      }
    },
    vpcChange(val) {
      if (!val.vpcId) {
        return;
      }
      const nowvpc = val;
      this.Vip1 = nowvpc.cidr.split(".")[0];
      this.Vip2 = nowvpc.cidr.split(".")[1];
      this.Vip3 = nowvpc.cidr.split(".")[2];
      this.Vip4 = nowvpc.cidr.split(".")[3].split("/")[0];
      this.Vip5 = nowvpc.cidr.split(".")[3].split("/")[1];

      this.ip1 = nowvpc.cidr.split(".")[0];
      this.ip2 = nowvpc.cidr.split(".")[1];
      this.ip3 = nowvpc.cidr.split(".")[2];
      this.ip4 = nowvpc.cidr.split(".")[3].split("/")[0];
      this.ip5 =
        nowvpc.cidr.split(".")[3].split("/")[1] < 24
          ? 24
          : nowvpc.cidr.split(".")[3].split("/")[1] * 1 + 1;
    },

    toSubNetAdd() {
      // 子网add
      if (this.ipStatus) {
        return;
      }
      this.loading = true;
      this.form.cidr = `${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}/${this.ip5}`;
      this.$refs.addSubnetForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .subnetsAdd(this.form)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startCreate"),
                type: "success",
                duration: 2500,
              });
              this.$emit("close");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    resetForm() {
      // 重置
      this.$refs.addSubnetForm.resetFields();
      this.form.name = "Subnet-" + this.$script.createRandomStr(8, "a0");
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
