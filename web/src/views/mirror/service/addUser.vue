<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.addUser')"
      width="800px"
      :close-on-click-modal="false"
      @closed="cancel"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="108px"
      >
        <el-form-item
          :label="$t('form.warehouseUserName') + ':'"
          prop="user_name"
        >
          <el-input
            v-model="form.user_name"
            :placeholder="$t('form.pleaseInputWarehouseUserName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.password') + ':'" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            :placeholder="$t('form.pleaseInputPassword')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.confirmPassword') + ':'"
          prop="rpassword"
        >
          <el-input
            v-model="form.rpassword"
            type="password"
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
import { addRegistriesUser, usersExist } from "@/api/mainApi";
export default {
  data() {
    var valiatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputConfirmPassword")));
      } else if (value !== this.form.password) {
        callback(new Error(this.$t("form.passwordNotMatch")));
      } else {
        callback();
      }
    };
    var valiatePass1 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputPassword")));
      } else if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,20}$/.test(value)) {
        callback(new Error(this.$t("form.passwordLength")));
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
        user_name: "",
        password: "",
        rpassword: "",
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        user_name: [
          {
            required: true,
            message: this.$t("message.pleaseInputWarehouseAdminAccount"),
            trigger: "change",
          },
        ],
        password: [
          {
            required: true,
            message: this.$t("message.pleaseInputPassword"),
            trigger: "change",
          },
          { trigger: "change", validator: valiatePass1 },
        ],
        rpassword: [{ trigger: "change", validator: valiatePass2 }],
      },
    };
  },
  mounted() {},
  methods: {
    add() {
      this.dialog = true;
    },
    cancel() {
      this.resetForm();
    },
    checkUser() {
      usersExist()
        .then((res) => {
          if (res.exist) {
            callback({
              state: true,
              text: "",
            });
          } else {
            callback({ state: false, text: `用户不存在` });
          }
        })
        .catch(() => {});
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
      delete params["rpassword"];
      addRegistriesUser(params)
        .then(() => {
          this.resetForm();
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    resetForm() {
      this.dialog = false;
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = {
        user_name: "",
        password: "",
        rpassword: "",
      };
    },
  },
};
</script>

<style lang="scss"></style>
