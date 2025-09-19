<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.modifyPassword')"
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
        <el-form-item :label="$t('form.oldPassword') + ':'" prop="old_password">
          <el-input
            v-model="form.old_password"
            show-password
            :placeholder="$t('form.pleaseInputOldPassword')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.newPassword') + ':'" prop="new_password">
          <el-input
            v-model="form.new_password"
            show-password
            :placeholder="$t('form.pleaseInputNewPassword')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.confirmPassword') + ':'"
          prop="new_rpassword"
        >
          <el-input
            v-model="form.new_rpassword"
            show-password
            :placeholder="$t('form.pleaseInputConfirmPassword')"
          />
        </el-form-item>
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
import { changeUserPwd } from "@/api/user/mainApi";
import Cookies from "js-cookie";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
  },
  data() {
    var valiatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputConfirmPassword")));
      } else if (value !== this.form.new_password) {
        callback(new Error(this.$t("form.passwordNotMatch")));
      } else {
        callback();
      }
    };
    var valiatePass1 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.passwordCannotBeEmpty")));
      } else if (
        !/(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+\-=}{:";<>?,./]).{6,18}$/.test(
          value
        )
      ) {
        callback(new Error(this.$t("message.passwordLength")));
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
      form: {
        old_password: "",
        new_password: "",
        new_rpassword: "",
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        old_password: [
          {
            required: true,
            message: this.$t("form.pleaseInputPassword"),
            trigger: "blur",
          },
        ],
        new_password: [
          {
            required: true,
            message: this.$t("form.pleaseInputPassword"),
            trigger: "blur",
          },
          { required: true, trigger: "blur", validator: valiatePass1 },
        ],
        new_rpassword: [
          {
            required: true,
            message: this.$t("form.pleaseInputConfirmPassword"),
            trigger: "blur",
          },
          { required: true, trigger: "blur", validator: valiatePass2 },
        ],
      },
      registry_id: "",
    };
  },
  methods: {
    add(obj) {
      this.form = {
        old_password: "",
        new_password: "",
        new_rpassword: "",
      };
      this.resetForm();
      this.dialog = true;
    },
    cancel() {
      this.dialog = false;
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
      const params = Object.assign({}, this.form);
      delete params["new_rpassword"];
      changeUserPwd(params)
        .then((res) => {
          this.$notify({
            title: this.$t("form.passwordModified"),
            type: "success",
            duration: 2500,
          });
          this.logout();
          this.resetForm();
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    async logout() {
      localStorage.clear();
      Cookies.remove("Access-Token");
      this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    },
    resetForm() {
      this.dialog = false;
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = { old_password: "", new_password: "", new_rpassword: "" };
    },
  },
};
</script>

<style lang="scss"></style>
