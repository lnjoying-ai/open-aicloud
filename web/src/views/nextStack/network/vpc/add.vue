<template>
  <div class="vpcAddPage h-full">
    <el-form>
      <el-form
        ref="addVpcForm"
        :size="$store.state.nextStack.viewSize.main"
        :model="form"
        :rules="rules"
        label-width="120px"
      >
        <div class="text item">
          <el-form-item :label="$t('form.name') + ':'" prop="name">
            <el-input
              v-model="form.name"
              class="w-60"
              :placeholder="$t('form.pleaseInputName')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.ipv4Cidr') + ':'" prop="cidr">
            <div class="block">
              <div class="block">
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
                  :disabled="ip5 < 9 || ip1 == 192"
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
                  :disabled="!(ip5 > 16)"
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
                  :disabled="!(ip5 > 24)"
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
                        v-show="
                          (ip1 == 10 && item > 7) ||
                          (ip1 == 172 && item > 11) ||
                          (ip1 == 192 && item > 15)
                        "
                        :label="item"
                        :value="item"
                        >{{ item }}</el-option
                      >
                    </span>
                  </el-select>
                </el-tooltip>
              </div>
              <div class="text-xs py-2 text-gray-500 block">
                {{ $t("form.recommendedUseCidr") }}:10.0.0.0/8-24
                <span
                  class="text-blue-400 mr-2 cursor-pointer"
                  @click="changeCidr(10, 0, 0, 0, 8, 24)"
                  >{{ $t("form.select") }}</span
                >
                172.16.0.0/12-24
                <span
                  class="text-blue-400 mr-2 cursor-pointer"
                  @click="changeCidr(172, 16, 0, 0, 12, 24)"
                  >{{ $t("form.select") }}</span
                >192.168.0.0/16-24
                <span
                  class="text-blue-400 mr-2 cursor-pointer"
                  @click="changeCidr(192, 168, 0, 0, 16, 24)"
                  >{{ $t("form.select") }}</span
                >
              </div>
            </div>
          </el-form-item>
        </div>
        <!-- </el-card> -->
      </el-form>
      <el-form
        :size="$store.state.nextStack.viewSize.main"
        :model="subnetForm"
        :rules="subnetRules"
        label-width="120px"
      >
        <!-- <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>子网信息</span>
          </div>
        </template> -->
        <el-divider content-position="left">{{
          $t("form.subnetInfo")
        }}</el-divider>
        <div class="text item">
          <el-form-item :label="$t('form.name') + ':'" prop="name">
            <el-input
              v-model="subnetForm.name"
              class="w-60"
              :placeholder="$t('form.pleaseInputName')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.subnetIpv4Cidr') + ':'">
            <el-input
              v-model="subnetIp1"
              class="w-14"
              :controls="false"
              :step="1"
              :min="0"
              :max="255"
              step-strictly
              :disabled="true"
              @input="subnetChangeIp"
            />
            <span class="text-xl px-1">.</span>
            <el-input
              v-model="subnetIp2"
              class="w-14"
              :controls="false"
              :step="1"
              :min="0"
              :max="255"
              step-strictly
              :disabled="ip5 > 15"
              @input="subnetChangeIp"
            />
            <span class="text-xl px-1">.</span>
            <el-input
              v-model="subnetIp3"
              class="w-14"
              :controls="false"
              :step="1"
              :min="0"
              :max="255"
              step-strictly
              :disabled="ip5 > 23"
              @input="subnetChangeIp"
            />
            <span class="text-xl px-1">.</span>
            <el-input
              v-model="subnetIp4"
              class="w-14"
              :controls="false"
              :step="1"
              :min="0"
              :max="255"
              step-strictly
              :disabled="subnetIp5 < 25"
              @input="subnetChangeIp"
            />
            <span class="text-xl px-1">/</span>
            <el-tooltip
              v-model="subnetIpStatus"
              :manual="true"
              placement="right"
            >
              <div slot="content">
                <span
                  class="inline-block w-4 h-4 bg-red-500 rounded-1/2 text-right leading-tight"
                  >！</span
                >
                {{ $t("form.ipAddressAndMaskDoNotMatch") }}
              </div>
              <el-select v-model="subnetIp5" class="w-20">
                <span v-for="(item, index) in maskList" :key="index">
                  <el-option
                    v-show="item > ip5 - 1"
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
        <div class="text item text-right mt-2">
          <el-button
            type="primary"
            size="small"
            :loading="loading"
            @click="toVpcAdd"
            >{{ $t("form.createImmediately") }}</el-button
          >
        </div>
      </el-form>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";
import netmask from "netmask";
var Netmask = netmask.Netmask;
export default {
  data() {
    return {
      loading: false,
      ip1: 10, // IP分配
      ip2: 0, // IP分配
      ip3: 0, // IP分配
      ip4: 0, // IP分配
      ip5: 16, // IP分配
      ipStatus: false, // IP状态
      subnetIpStatus: false, // 子网IP状态
      subnetIp1: 10, // IP分配
      subnetIp2: 0, // IP分配
      subnetIp3: 0, // IP分配
      subnetIp4: 0, // IP分配
      subnetIp5: 24, // IP分配
      maskList: [
        8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
        26, 27, 28,
      ],
      form: {
        // vpc表单
        name: "VPC-" + this.$script.createRandomStr(8, "a0"), // 名称
        addressType: 0, // 0：IPV4 1：IPV6
        cidr: "",
      },
      subnetForm: {
        // 子网表单
        name: "Subnet-" + this.$script.createRandomStr(8, "a0"),
        vpcId: "",
        addressType: 0, // 0：IPV4 1：IPV6
        cidr: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
      subnetRules: {
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
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  watch: {
    ip5(newValue) {
      this.changeIp();
    },
    subnetIp5(newValue) {
      this.subnetChangeIp();
    },
  },
  methods: {
    // 改变表单的数据
    changeform() {
      this.form = {
        name: "VPC-" + this.$script.createRandomStr(8, "a0"), // 名称
        addressType: 0, // 0：IPV4 1：IPV6
        cidr: "",
      };
      this.subnetForm = {
        // 子网表单
        name: "Subnet-" + this.$script.createRandomStr(8, "a0"),
        vpcId: "",
        addressType: 0, // 0：IPV4 1：IPV6
        cidr: "",
      };
      (this.ip1 = 10),
        (this.ip2 = 0),
        (this.ip3 = 0),
        (this.ip4 = 0),
        (this.ip5 = 16);
    },
    toVpcAdd() {
      // VPC网络add
      if (this.ipStatus) {
        return;
      }
      if (this.subnetIpStatus) {
        return;
      }
      this.loading = true;
      this.form.cidr = `${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}/${this.ip5}`;
      this.$refs.addVpcForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .vpcAdd(this.form)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startCreate"),
                type: "success",
                duration: 2500,
              });
              this.toSubNetAdd(res.vpcId);
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    toSubNetAdd(vpcId) {
      // 子网add

      this.loading = true;
      this.subnetForm.vpcId = vpcId;
      this.subnetForm.cidr = `${this.subnetIp1}.${this.subnetIp2}.${this.subnetIp3}.${this.subnetIp4}/${this.subnetIp5}`;
      mainApi
        .subnetsAdd(this.subnetForm)
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
    },
    changeCidr(ip01, ip02, ip03, ip04, min, max) {
      this.ip1 = ip01;
      this.ip2 = ip02;
      this.ip3 = ip03;
      this.ip4 = ip04;
      this.ip5 = min;
      this.changeIp();
    },
    subnetChangeIp() {
      // subnetIP变动
      this.subnetIpStatus = false;
      this.subnetIp1 = this.$script.parseIntIpNum(this.subnetIp1);
      this.subnetIp2 = this.$script.parseIntIpNum(this.subnetIp2);
      this.subnetIp3 = this.$script.parseIntIpNum(this.subnetIp3);
      this.subnetIp4 = this.$script.parseIntIpNum(this.subnetIp4);
      const subnetIp = `${this.subnetIp1}.${this.subnetIp2}.${this.subnetIp3}.${this.subnetIp4}/${this.subnetIp5}`;
      const netBlock = new Netmask(subnetIp);
      if (
        `${this.subnetIp1}.${this.subnetIp2}.${this.subnetIp3}.${this.subnetIp4}` ==
        netBlock.base
      ) {
        console.log("ok");
      } else {
        this.subnetIpStatus = true;
      }
    },
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
        this.subnetIp1 = this.ip1;
        this.subnetIp2 = this.ip2;
        this.subnetIp3 = this.ip3;
        this.subnetIp4 = this.ip4;
        this.subnetIp5 = this.ip5 < 24 ? 24 : this.ip5 + 1;
      } else {
        this.ipStatus = true;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.vpcAddPage {
}
</style>
