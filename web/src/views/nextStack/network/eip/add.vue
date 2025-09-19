<template>
  <div class="subNetAddPage h-full">
    <el-form
      ref="addSubnetForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="140px"
    >
      <div class="text item">
        <el-form-item :label="$t('form.eipPool') + ':'" prop="eipPoolId">
          <el-select
            v-model="form.eipPoolId"
            class="w-60"
            :placeholder="$t('form.pleaseSelectEipPool')"
          >
            <el-option
              v-for="(item, index) in eipPoolsData"
              :key="index"
              :label="item.name"
              :value="item.poolId"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.type') + ':'" prop="addressType">
          <el-select
            v-model="form.addressType"
            class="w-60"
            :placeholder="$t('form.pleaseSelectType')"
          >
            <el-option key="IPv4" :label="$t('form.IPv4')" :value="0" />
            <!-- <el-option key="IPv6" label="IPv6" :value="1"></el-option> -->
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.startEip') + ':'">
          <el-input-number
            v-model="ip1"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="ip2"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="ip3"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="ip4"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeIp"
          />
        </el-form-item>
        <el-form-item :label="$t('form.endEip') + ':'">
          <el-input-number
            v-model="ipEnd1"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeEndIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="ipEnd2"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeEndIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="ipEnd3"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeEndIp"
          />
          <span class="text-xl px-1">.</span>
          <el-input-number
            v-model="ipEnd4"
            class="w-15"
            :controls="false"
            :step="1"
            :min="0"
            :max="255"
            step-strictly
            @input="changeEndIp"
          />
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="toEipAdd"
        >
          {{ $t("form.createImmediately") }}
        </el-button>
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
  data() {
    return {
      loading: false,
      eipPoolsData: [],
      ip1: 0, // IP分配
      ip2: 0, // IP分配
      ip3: 0, // IP分配
      ip4: 0, // IP分配
      ipStatus: false, // IP分配状态
      ipEnd1: 0, // IP分配
      ipEnd2: 0, // IP分配
      ipEnd3: 0, // IP分配
      ipEnd4: 0, // IP分配
      ipEndStatus: false, // IP分配状态
      form: {
        // 表单
        eipPoolId: "", // EIP Pool
        startIpAddress: "",
        endIpAddress: "",
        addressType: 0, // 0：IPV4 1：IPV6
      },
      rules: {
        eipPoolId: [
          {
            required: true,
            message: this.$t("form.pleaseSelectEipPool"),
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    this.getEipList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    changeIp() {
      // IP变动
      this.ipStatus = false;
      this.ip1 = this.$scriptMain.parseIntIpNum(this.ip1);
      this.ip2 = this.$scriptMain.parseIntIpNum(this.ip2);
      this.ip3 = this.$scriptMain.parseIntIpNum(this.ip3);
      this.ip4 = this.$scriptMain.parseIntIpNum(this.ip4);
      const ip = `${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}`;
      const netBlock = new Netmask(ip);
      console.log(netBlock);

      if (`${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}` == netBlock.base) {
      } else {
        this.ipStatus = true;
      }
    },
    changeEndIp() {
      // IP变动
      this.ipEndStatus = false;
      this.ipEnd1 = this.$scriptMain.parseIntIpNum(this.ipEnd1);
      this.ipEnd2 = this.$scriptMain.parseIntIpNum(this.ipEnd2);
      this.ipEnd3 = this.$scriptMain.parseIntIpNum(this.ipEnd3);
      this.ipEnd4 = this.$scriptMain.parseIntIpNum(this.ipEnd4);
      const ipEnd = `${this.ipEnd1}.${this.ipEnd2}.${this.ipEnd3}.${this.ipEnd4}`;

      const netBlock = new Netmask(ipEnd);
      console.log(netBlock);
      if (
        `${this.ipEnd1}.${this.ipEnd2}.${this.ipEnd3}.${this.ipEnd4}` ==
        netBlock.base
      ) {
      } else {
        this.ipEndStatus = true;
      }
    },
    toEipAdd() {
      // 创建EIP

      if (this.ipStatus) {
        return;
      }
      this.loading = true;
      const ip = `${this.ip1}.${this.ip2}.${this.ip3}.${this.ip4}`;
      const ipEnd = `${this.ipEnd1}.${this.ipEnd2}.${this.ipEnd3}.${this.ipEnd4}`;

      const params = {
        eipPoolId: this.form.eipPoolId,
        startIpAddress: ip,
        endIpAddress: ipEnd,
        addressType: this.form.addressType,
      };
      this.$refs.addSubnetForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .eipsAdd(params)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.createSuccess"),
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
    getEipList() {
      // EIP Pool列表

      mainApi.eipPoolsList().then((res) => {
        this.eipPoolsData = res.eipPools;
      });
    },
    resetForm() {
      // 重置表单
      this.$refs.addSubnetForm.resetFields();
      this.ip1 = 0;
      this.ip2 = 0;
      this.ip3 = 0;
      this.ip4 = 0;
      this.ipStatus = false;
      this.ipEnd1 = 0;
      this.ipEnd2 = 0;
      this.ipEnd3 = 0;
      this.ipEnd4 = 0;
      this.ipEndStatus = false;
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
