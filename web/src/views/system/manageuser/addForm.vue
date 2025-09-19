<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    :title="isAdd ? $t('form.addUser') : $t('form.editUser')"
    width="800px"
    :before-close="handleCloseDialog"
    :close-on-click-modal="false"
    @open="onOpen"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      size="small"
      label-width="120px"
    >
      <el-form-item :label="$t('form.userName') + ':'" prop="name">
        <el-input
          v-model="form.name"
          :placeholder="$t('form.pleaseInputContent')"
        />
      </el-form-item>
      <el-form-item :label="$t('form.loginPassword') + ':'" prop="password">
        <el-input
          v-model="form.password"
          show-password
          :placeholder="$t('form.pleaseInputContent')"
        />
      </el-form-item>
      <el-form-item :label="$t('form.userType') + ':'" prop="kind">
        <el-select
          v-model="form.kind"
          :placeholder="$t('form.pleaseInputContent')"
          :disabled="disabled"
          @change="changeKind"
        >
          <el-option :value="0" label="System" />
          <el-option :value="1" label="admin" />
          <el-option :value="2" label="BP admin" />
          <el-option :value="3" label="personal" />
          <el-option :value="4" label="Bp user" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('form.userLevel') + ':'" prop="level">
        <el-rate v-model="form.level" />
      </el-form-item>
      <el-form-item :label="$t('form.gender') + ':'" prop="gender">
        <el-select
          v-model="form.gender"
          :placeholder="$t('form.pleaseInputContent')"
          clearable
        >
          <el-option :value="0" label="male" />
          <el-option :value="1" label="female" />
        </el-select>
      </el-form-item>
      <el-form-item
        :label="$t('form.contactEmail') + ':'"
        prop="contact_info.email"
        :rules="{ required: true, message: '请输入邮箱', trigger: 'blur' }"
      >
        <el-input v-model="form.contact_info.email" /> </el-form-item
      ><el-form-item
        :label="$t('form.contactPhone') + ':'"
        prop="contact_info.phone"
      >
        <el-input v-model="form.contact_info.phone" /> </el-form-item
      ><el-form-item
        :label="$t('form.contactAddress') + ':'"
        prop="contact_info.address"
      >
        <el-input v-model="form.contact_info.address" />
      </el-form-item>

      <el-form-item :label="$t('form.bindEnterpriseCode') + ':'" prop="bp_id">
        <el-input
          v-model="form.bp_id"
          :placeholder="$t('form.pleaseInputContent')"
          :disabled="disabled"
        />
      </el-form-item>
      <el-form-item :label="$t('form.accessControl') + ':'">
        <el-select
          v-model="form.is_allowed"
          :placeholder="$t('form.pleaseInputContent')"
          clearable
        >
          <el-option :value="0" :label="$t('form.denyAccess')" />
          <el-option :value="1" :label="$t('form.allowAccess')" />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('form.status') + ':'">
        <el-select
          v-model="form.status"
          :placeholder="$t('form.pleaseInputContent')"
          clearable
        >
          <el-option :value="0" :label="$t('form.pendingActivation')" />
          <el-option :value="1" :label="$t('form.normal')" />
          <el-option :value="2" :label="$t('form.overdue')" />
          <el-option :value="3" :label="$t('form.locked')" />
        </el-select>
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
</template>

<script>
import { addUsers, editUsers } from "@/api/mainApi";
import { mapGetters } from "vuex";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    return {
      isAdd: true,
      loading: false,
      dialog: false,
      id: "",
      disabled: false,
      form: {
        status: 0,
        name: "",
        password: "",
        kind: "",
        level: "",
        gender: "",
        bp_id: "",
        is_allowed: 1,
        contact_info: {
          name: "",
          email: "",
          phone: "",
          address: "",
        },
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputUserName"),
            trigger: "blur",
          },
        ],
        password: [
          {
            required: true,
            message: this.$t("form.pleaseInputLoginPassword"),
            trigger: "blur",
          },
        ],
        level: [
          {
            required: true,
            message: this.$t("form.pleaseSelectUserLevel"),
            trigger: "change",
          },
        ],
        gender: [
          {
            required: true,
            message: this.$t("form.pleaseSelectGender"),
            trigger: "change",
          },
        ],
        kind: [
          {
            required: true,
            message: this.$t("form.pleaseSelectKind"),
            trigger: "change",
          },
        ],
        bp_id: [],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  created() {
    if (this.userInfo.kind == 2) {
      this.form.bp_id = this.userInfo.bp_id;
      this.form.kind = 4;
      this.form.status = 1;
      this.disabled = true;
    } else {
      this.form.bp_id = "";
      this.form.kind = "";
      this.form.status = 0;
      this.disabled = false;
    }
  },
  methods: {
    onOpen() {
      if (this.isAdd === false) {
        this.$set(this.form, "name", this.info.name);
        this.$set(this.form, "password", this.info.password);
        this.$set(this.form, "kind", this.info.kind);
        this.$set(this.form, "level", this.info.level);
        this.$set(this.form, "gender", this.info.gender);
        this.$set(this.form, "bp_id", this.info.bp_id);
        this.$set(this.form, "is_allowed", this.info.is_allowed ? 1 : 0);
        this.$set(this.form, "contact_info", this.info.contact_info);
        this.$set(this.form, "status", this.info.status);
      }
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAdd) {
            this.doAdd();
          } else this.doEdit();
        } else {
          return false;
        }
      });
    },
    doAdd() {
      this.loading = false;
      addUsers(this.form)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: $t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.getList();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    doEdit() {
      editUsers(this.id, this.form)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: $t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.getList();
        })
        .catch((err) => {
          this.loading = false;
          console.error(err.response.data.message);
        });
    },
    handleCloseDialog() {
      this.resetForm();
    },
    changeKind(value) {
      if (value == "") {
        this.rules.bp_id.push({
          required: true,
          message: this.$t("form.pleaseInputBindEnterpriseCode"),
          trigger: "blur",
        });
      } else {
        this.rules.bp_id = [];
      }
    },
    resetForm() {
      this.dialog = false;
      this.form = {
        status: 0,
        name: "",
        password: "",
        kind: "",
        level: "",
        gender: "",
        bp_id: "",
        is_allowed: 1,
        contact_info: {
          name: "",
          email: "",
          phone: "",
          address: "",
        },
      };
    },
  },
};
</script>
