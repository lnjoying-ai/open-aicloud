<template>
  <div class="hypervisorNodesAddPage h-full">
    <el-form
      ref="addVmForm"
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
        <el-form-item :label="$t('form.manageIp') + ':'" prop="manageIp">
          <el-input
            v-model="form.manageIp"
            class="w-60"
            :placeholder="$t('form.pleaseInputManageIp')"
          />
        </el-form-item>
        <el-form-item label="">
          <el-checkbox
            v-model="checked1"
            :label="$t('form.advancedConfig')"
            size="large"
            @change="changeChecked1"
          />
        </el-form-item>
      </div>
      <div v-if="checked1" class="text item">
        <el-form-item :label="$t('form.hostname') + ':'" prop="hostname">
          <el-input
            v-model="form.hostname"
            class="w-60"
            :placeholder="$t('form.pleaseInputHostname')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.sysUsername') + ':'" prop="sysUsername">
          <el-input
            v-model="form.sysUsername"
            class="w-60"
            :placeholder="$t('form.pleaseInputSysUsername')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.loginCredential') + ':'">
          <el-radio-group v-model="loginType">
            <el-radio :label="true" :value="true">{{
              $t("form.password")
            }}</el-radio>
            <el-radio :label="false" :value="false">{{
              $t("form.keyPair")
            }}</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item v-if="loginType" :label="$t('form.password') + ':'">
          <el-input
            v-model="isPassword"
            class="w-60"
            type="password"
            :placeholder="$t('form.pleaseInputPassword')"
          />
        </el-form-item>
        <el-form-item
          v-if="loginType"
          :label="$t('form.confirmPassword') + ':'"
          prop="sysPassword"
        >
          <el-input
            v-model="form.sysPassword"
            class="w-60"
            type="password"
            :placeholder="$t('form.pleaseInputConfirmPassword')"
          />
        </el-form-item>
        <el-form-item
          v-if="!loginType"
          :label="$t('form.keyPair') + ':'"
          prop="pubkeyId"
        >
          <el-select
            v-model="form.pubkeyId"
            class="w-60"
            :placeholder="$t('form.pleaseSelectKeyPair')"
          >
            <el-option
              v-for="(item, index) in pubkeysDataList"
              :key="index"
              :label="item.name"
              :value="item.pubkeyId"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toHypervisorNodesAdd()"
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
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputPassword")));
      } else {
        if (value !== this.isPassword) {
          callback(new Error(this.$t("message.passwordNotMatch")));
        } else {
          callback();
        }
      }
    };
    return {
      checked1: false,
      loading: false,
      loginType: true,
      isPassword: "",
      form: {
        name: "",
        description: "",
        manageIp: "", // 管理IP
        hostname: "", // 主机名
        sysUsername: "", // 系统用户名
        sysPassword: "", // 系统密码
        pubkeyId: "", // 密钥对ID
      },
      pubkeysDataList: [], // 密钥对列表
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        manageIp: [
          {
            required: true,
            message: this.$t("message.pleaseInputManageIp"),
            trigger: "blur",
          },
        ],

        hostname: [
          {
            required: true,
            message: this.$t("message.pleaseInputHostname"),
            trigger: "blur",
          },
          {
            min: 3,
            max: 64,
            message: this.$t("message.pleaseInput364"),
            trigger: "blur",
          },
        ],
        sysUsername: [
          {
            required: true,
            message: this.$t("message.pleaseInputLoginName"),
            trigger: "blur",
          },
          {
            min: 3,
            max: 64,
            message: this.$t("message.pleaseInput364"),
            trigger: "blur",
          },
        ],
        sysPassword: [
          { required: true, validator: validatePass, trigger: "change" },
        ],
      },
    };
  },
  watch: {
    loginType: {
      handler(val) {
        if (val) {
          this.form.pubkeyId = "";
        } else {
          this.form.sysUsername = "";
          this.form.sysPassword = "";
          this.isPassword = "";
        }
      },
      immediate: true,
    },
  },

  created() {
    this.getpubkeysList(); // 公钥列表
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    changeChecked1(val) {
      if (val) {
        this.form.hostname = "";
        this.form.sysUsername = "";
        this.form.sysPassword = "";
        this.form.pubkeyId = "";
        this.loginType = true;
        this.isPassword = "";
      }
    },
    resetForm() {
      // 重置
      this.$refs.addVmForm.resetFields();
      this.checked1 = false;
      this.isPassword = "";
    },
    toHypervisorNodesAdd() {
      // 计算节点add
      this.loading = true;
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .vmsHypervisorNodesAdd(this.form)
            .then((res) => {
              this.loading = false;
              this.resetForm();
              this.$emit("close");
            })
            .catch((error) => {
              this.resetForm();
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    getpubkeysList() {
      // 公钥列表

      mainApi
        .pubkeysList()
        .then((res) => {
          this.pubkeysDataList = res.pubkeys;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.hypervisorNodesAddPage {
}
</style>
