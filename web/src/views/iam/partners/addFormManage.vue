<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    :title="isAdd ? $t('form.addAdmin') : $t('form.editAdmin')"
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
      <el-form-item
        :label="$t('form.name') + ':'"
        prop="name"
        :rules="[
          {
            required: true,
            message: $t('form.pleaseInputName'),
            trigger: 'blur',
          },
          {
            pattern: /^[a-zA-Z0-9][a-zA-Z0-9-_.@]{5,63}$/,
            message: $t('form.pleaseInputNameTips'),
            trigger: 'blur',
          },
        ]"
      >
        <el-input
          v-model="form.name"
          :placeholder="$t('form.pleaseInputNameTips')"
          @blur.native.capture="onChangeName"
        />
      </el-form-item>
      <el-form-item
        :label="$t('form.email') + ':'"
        prop="contact_info.email"
        :rules="[
          {
            required: true,
            message: $t('form.pleaseInputEmail'),
            trigger: 'blur',
          },
          {
            pattern:
              /^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{1,6}$/,
            message: $t('form.pleaseInputEmailTips'),
            trigger: 'blur',
          },
        ]"
      >
        <el-input
          v-model="form.contact_info.email"
          :placeholder="$t('form.pleaseInputEmail')"
          @blur.native.capture="onChangeEmail"
        />
      </el-form-item>
      <el-form-item
        :label="$t('form.phone') + ':'"
        prop="contact_info.phone"
        :rules="[
          {
            required: true,
            message: $t('form.pleaseInputPhone'),
            trigger: 'blur',
          },
          {
            pattern: /^1[3456789]\d{9}$/,
            message: $t('form.pleaseInputPhoneTips'),
            trigger: 'blur',
          },
        ]"
      >
        <input type="password" hidden autocomplete="new-password" />

        <el-input
          v-model="form.contact_info.phone"
          :placeholder="$t('form.pleaseInputPhone')"
          @blur.native.capture="onChangePhone"
        />
      </el-form-item>
      <el-form-item :label="$t('form.password') + ':'" prop="password">
        <el-input
          v-model="form.password"
          show-password
          :placeholder="$t('form.pleaseInputPassword')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('form.confirmPassword') + ':'"
        prop="confirmPassword"
      >
        <el-input
          v-model="form.confirmPassword"
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
</template>

<script>
import { addUsers, editUsers, userUniqueness } from "@/api/iam/user";
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
      nameFlag: false,
      phoneFlag: false,
      emailFlag: false,
      form: {
        status: 1,
        is_allowed: 1,
        name: "",
        password: "",
        bp_id: "",
        kind: "2",
        confirmPassword: "",
        contact_info: {
          name: "",
          email: "",
          phone: "",
        },
      },

      rules: {
        password: [
          {
            required: true,
            message: this.$t("form.pleaseInputPassword"),
            trigger: "blur",
          },
        ],

        level: [
          {
            required: true,
            message: this.$t("message.pleaseSelectLevel"),
            trigger: "change",
          },
        ],
        gender: [
          {
            required: true,
            message: this.$t("message.pleaseSelectGender"),
            trigger: "change",
          },
        ],
        confirmPassword: [
          {
            required: true,
            message: this.$t("form.pleaseInputConfirmPassword"),
            trigger: "change",
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  created() {},
  mounted() {},
  methods: {
    onChangeName(e) {
      if (
        this.isAdd === true &&
        this.form.name != "" &&
        this.form.name != null
      ) {
        var data = {
          username: this.form.name,
          phone: "",
          email: "",
        };
        userUniqueness(data)
          .then((res) => {
            if (res.username == true) {
              this.nameFlag = true;
              this.$notify({
                title: this.$t("message.nameExists"),
                type: "error",
                duration: 2500,
              });
            } else {
              this.nameFlag = false;
            }
          })
          .catch(() => {});
      } else {
        var name = JSON.parse(JSON.stringify(this.info.master_user));
        if (this.form.name != name && this.form.name != "") {
          var data = {
            username: this.form.name,
            phone: "",
            email: "",
          };
          userUniqueness(data)
            .then((res) => {
              if (res.username == true) {
                this.nameFlag = true;
                this.$notify({
                  title: this.$t("message.nameExists"),
                  type: "error",
                  duration: 2500,
                });
              } else {
                this.nameFlag = false;
              }
            })
            .catch(() => {});
        }
      }
    },
    onChangeEmail() {
      if (this.isAdd === true && this.form.contact_info.email != "") {
        var data = {
          username: "",
          phone: "",
          email: this.form.contact_info.email,
        };
        userUniqueness(data)
          .then((res) => {
            if (res.email == true) {
              this.emailFlag = true;
              this.$notify({
                title: this.$t("message.emailExists"),
                type: "error",
                duration: 2500,
              });
            } else {
              this.emailFlag = false;
            }
          })
          .catch(() => {});
      } else {
        var email = "";
        JSON.parse(JSON.stringify(this.info.contact_info.email))
          ? (email = JSON.parse(JSON.stringify(this.info.contact_info.email)))
          : "";
        if (
          this.form.contact_info.email != email &&
          this.form.contact_info.email != ""
        ) {
          var data = {
            username: "",
            phone: "",
            email: this.form.contact_info.email,
          };
          userUniqueness(data)
            .then((res) => {
              if (res.email == true) {
                this.phoneFlag = true;
                this.$notify({
                  title: this.$t("message.emailExists"),
                  type: "error",
                  duration: 2500,
                });
              } else {
                this.emailFlag = false;
              }
            })
            .catch(() => {});
        }
      }
    },
    onChangePhone() {
      if (this.isAdd === true) {
        if (this.form.contact_info.phone != "") {
          var data = {
            username: "",
            phone: this.form.contact_info.phone,
            email: "",
          };
          userUniqueness(data)
            .then((res) => {
              if (res.phone == true) {
                this.phoneFlag = true;
                this.$notify({
                  title: this.$t("message.phoneExists"),
                  type: "error",
                  duration: 2500,
                });
              } else {
                this.phoneFlag = false;
              }
            })
            .catch(() => {});
        }
      } else {
        var phone = "";
        JSON.parse(JSON.stringify(this.info.contact_info.phone))
          ? (phone = JSON.parse(JSON.stringify(this.info.contact_info.phone)))
          : "";
        if (
          this.form.contact_info.phone != phone &&
          this.form.contact_info.phone != ""
        ) {
          var data = {
            username: "",
            phone: this.form.contact_info.phone,
            email: "",
          };
          userUniqueness(data)
            .then((res) => {
              if (res.phone == true) {
                this.phoneFlag = true;
                this.$notify({
                  title: this.$t("message.phoneExists"),
                  type: "error",
                  duration: 2500,
                });
              } else {
                this.phoneFlag = false;
              }
            })
            .catch(() => {});
        }
      }
    },
    onOpen() {
      if (this.$refs.form) this.$refs.form.resetFields();
      if (this.isAdd === false) {
        this.$set(this.form, "name", this.info.master_user);
        this.$set(this.form, "password", this.info.password);
        if (this.info.contact_info != null) {
          this.$set(this.form, "contact_info", this.info.contact_info);
        } else {
          this.$set(this.form, "contact_info", {
            name: "",
            email: "",
            phone: "",
          });
        }
      } else {
        this.$set(this.form, "name", "");
        this.$set(this.form, "password", "");
        this.$set(this.form, "contact_info", {
          name: "",
          email: "",
          phone: "",
        });
      }
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      if (
        this.phoneFlag == false &&
        this.emailFlag == false &&
        this.nameFlag == false
      ) {
        this.$refs["form"].validate((valid) => {
          if (valid) {
            if (this.form.confirmPassword == this.form.password) {
              this.loading = true;
              if (this.isAdd) {
                this.doAdd();
              } else this.doEdit();
            } else {
              this.$notify({
                title: this.$t("message.passwordNotMatch"),
                type: "error",
                duration: 2500,
              });
              this.form.confirmPassword = "";
            }
          }
        });
      } else {
        return;
      }
    },
    doAdd() {
      this.loading = false;
      var data = JSON.parse(JSON.stringify(this.form));
      addUsers(data)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.init();
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
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    handleCloseDialog() {
      this.resetForm();
    },
    resetForm() {
      this.dialog = false;
      this.form = {
        status: 1,
        name: "",
        password: "",
        is_allowed: 1,
        kind: "2",
        bp_id: "",
        confirmPassword: "",
        contact_info: {
          name: "",
          email: "",
          phone: "",
        },
      };
    },
  },
};
</script>

<style scoped></style>
