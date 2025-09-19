<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.modifyPassword')"
      width="800px"
      :close-on-click-modal="false"
      @closed="cancel"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="140px"
      >
        <el-form-item
          :label="$t('form.repositoryUsername') + ':'"
          prop="user_name"
        >
          <el-input
            v-model="form.user_name"
            :disabled="true"
            :placeholder="$t('form.repositoryUsername')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.password') + ':'" prop="password">
          <el-input
            v-model="form.password"
            type="password"
            show-password
            :placeholder="$t('form.password')"
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
            :placeholder="$t('form.confirmPassword')"
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
        callback(new Error(this.$t("form.pleaseInputPassword")));
      } else if (value !== this.form.password) {
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
    var valiateUserName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputRepositoryUsername")));
      } else {
        this.checkUser(value, function (data) {
          callback(data.state ? data.text : new Error(data.text));
        });
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
            message: this.$t("form.pleaseInputRepositoryUsername"),
            trigger: "change",
          },
          // { trigger: "change", validator: valiateUserName },
        ],
        password: [
          {
            required: true,
            message: this.$t("form.pleaseInputPassword"),
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
    add(obj) {
      this.form = Object.assign({}, this.form, { user_name: obj.user_name });

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
        .catch((err) => {});
    },
    doSubmit() {
      this.$refs["form"].validate((valid, value) => {
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
        .then((res) => {
          this.$emit("usersExist");
          this.resetForm();
          this.loading = false;
        })
        .catch((err) => {
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
