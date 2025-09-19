<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.modifyPhoneNumber')"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="140px"
      >
        <el-form-item :label="$t('form.userPassword') + ':'" prop="password">
          <el-input
            v-model="form.password"
            show-password
            :placeholder="$t('form.pleaseInputPassword')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.phoneNumber') + ':'" prop="phone">
          <el-input
            ref="phone"
            v-model="form.phone"
            :placeholder="$t('form.pleaseInputPhoneNumber')"
            name="phone"
            type="text"
            tabindex="1"
            auto-complete="on"
          />
        </el-form-item>
        <div style="display: flex">
          <el-form-item
            :label="$t('form.verificationCode') + ':'"
            prop="verification_code"
            style="flex: 1"
          >
            <el-input
              ref="verification_code"
              v-model="form.verification_code"
              :placeholder="$t('form.pleaseInputVerificationCode')"
              name="verification_code"
              type="text"
              tabindex="1"
              auto-complete="on"
            />
          </el-form-item>
          <el-button
            v-show="regShow"
            type="button"
            class="sendCode"
            style="height: 32px; line-height: 32px; padding: 0 15px"
            @click="sendSms('phone')"
            >{{ $t("form.getVerificationCode") }}</el-button
          >
          <el-button
            v-show="!regShow"
            type="button"
            style="height: 32px; line-height: 32px; padding: 0 15px"
            class="sendCode"
            >{{ regCount }} s</el-button
          >
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { changePhone, getPatchSms } from "@/api/user/mainApi";
const TIME_COUNT = 60; // 更改倒计时时间
import Cookies from "js-cookie";
import { resetRouter } from "@/router";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
  },
  data() {
    var checkPhone = (rule, value, callback) => {
      const phoneReg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
      if (!value) {
        return callback(new Error(this.$t("form.phoneNumberCannotBeEmpty")));
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,但是我实际用的时候输入的数字总是识别成字符串
        // 所以我就在前面加了一个+实现隐式转换

        if (!Number.isInteger(+value)) {
          callback(new Error(this.$t("form.pleaseInputCorrectPhoneNumber")));
        } else {
          if (phoneReg.test(value)) {
            callback();
          } else {
            callback(new Error(this.$t("form.pleaseInputCorrectPhoneNumber")));
          }
        }
      }, 100);
    };
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error(this.$t("form.passwordMustBeAtLeast6Characters")));
      } else {
        callback();
      }
    };
    return {
      regions: [],
      loading: false,
      dialog: false,
      cmdModal: false,
      cmdInfo: null,
      regShow: true,
      regCount: 60,
      form: {
        password: "",
        phone: "",
        verification_code: "",
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        phone: [{ required: true, trigger: "blur", validator: checkPhone }],
        password: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("form.pleaseInputPassword"),
          },
        ],
        verification_code: [
          {
            required: true,
            trigger: "blur",
            message: this.$t("form.pleaseInputVerificationCode"),
          },
        ],
      },
      registry_id: "",
    };
  },
  methods: {
    add(obj) {
      this.form = {
        password: "",
        phone: "",
        verification_code: "",
      };
      this.resetForm();
      this.dialog = true;
    },
    cancel() {
      this.dialog = false;
    },
    async sendSms(name) {
      if (!this.timer) {
        const phoneReg = /^1[3|4|5|6|7|8|9][0-9]{9}$/;
        if (this.form[name] && phoneReg.test(this.form[name])) {
          this.regCount = TIME_COUNT;
          this.regShow = false;
          await getPatchSms(this.form[name]);
        } else {
          this.regCount = this.$t("form.getVerificationCode");
          clearInterval(this.timer); // 清除定时器
          this.timer = null;
          this.$notify({
            title: this.$t("form.pleaseInputCorrectPhoneNumber"),
            type: "info",
            duration: 2500,
          });
        }
        this.timer = setInterval(() => {
          if (this.regCount > 0 && this.regCount <= TIME_COUNT) {
            this.regCount--;
          } else {
            this.regShow = true;
            clearInterval(this.timer); // 清除定时器
            this.timer = null;
          }
        }, 1000);
      }
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          this.doAdd();
        } else {
          return false;
        }
      });
    },
    doAdd() {
      changePhone(this.form)
        .then(() => {
          this.resetForm();
          this.$parent.getUserInfo();
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    async logout() {
      localStorage.clear();
      Cookies.remove("Access-Token");
      Cookies.remove("vue_admin_template_kind");
      this.$store.commit("user/SET_KING", "");
      resetRouter();
      this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    },
    resetForm() {
      this.dialog = false;
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = { password: "", phone: "", verification_code: "" };
    },
  },
};
</script>

<style lang="scss"></style>
