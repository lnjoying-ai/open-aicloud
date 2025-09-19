<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.modifyAdminPassword')"
      width="800px"
      :close-on-click-modal="false"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="120px"
      >
        <el-form-item
          :label="$t('form.adminOldPassword') + ':'"
          prop="old_password"
        >
          <el-input
            v-model="form.old_password"
            :placeholder="$t('form.pleaseInput')"
            show-password
          />
        </el-form-item>
        <el-form-item :label="$t('form.newPassword') + ':'" prop="new_password">
          <el-input
            v-model="form.new_password"
            show-password
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.confirmPassword') + ':'"
          prop="new_rpassword"
        >
          <el-input
            v-model="form.new_rpassword"
            show-password
            :placeholder="$t('form.pleaseInput')"
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
    <el-dialog
      :title="$t('form.generatedCommand')"
      :visible.sync="cmdModal"
      :close-on-click-modal="false"
      width="800px"
      @close="cmdInfo = null"
    >
      <div>{{ $t("form.pleaseCopyTheFollowingInformationAndExecute") }}</div>
      <div class="text-lg text-red-500">{{ cmdInfo }}</div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="cmdModal = false">{{
          $t("form.close")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { registriesPassword } from "@/api/mainApi";
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
        callback(new Error(this.$t("message.pleaseInputPasswordAgain")));
      } else if (value !== this.form.new_password) {
        callback(new Error(this.$t("message.passwordNotMatch")));
      } else {
        callback();
      }
    };
    var valiatePass1 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.passwordCannotBeEmpty")));
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
            message: this.$t("message.pleaseInputPassword"),
            trigger: "change",
          },
        ],
        new_password: [
          {
            required: true,
            message: this.$t("message.pleaseInputPassword"),
            trigger: "change",
          },
          { trigger: "change", validator: valiatePass1 },
        ],
        new_rpassword: [{ trigger: "change", validator: valiatePass2 }],
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
      this.registry_id = obj.registry_id;
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
      registriesPassword(this.registry_id, params)
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
      this.form = { old_password: "", new_password: "", new_rpassword: "" };
    },
  },
};
</script>

<style lang="scss"></style>
