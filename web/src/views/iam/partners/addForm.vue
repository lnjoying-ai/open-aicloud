<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    :title="isAdd ? $t('form.addOrganization') : $t('form.editOrganization')"
    width="600px"
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
      <el-form-item :label="$t('form.name') + ':'" prop="name">
        <el-input
          v-model="form.name"
          class="w-60"
          :placeholder="$t('form.pleaseInputName')"
          @blur.native.capture="onChange"
        />
      </el-form-item>
      <el-form-item :label="$t('form.description') + ':'">
        <el-input
          v-model="form.description"
          class="w-96"
          :placeholder="$t('form.pleaseInputDescription')"
          type="textarea"
          maxlength="255"
          show-word-limit
        />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" size="small" @click="cancel">{{
        $t("form.cancel")
      }}</el-button>
      <el-button
        :loading="loading"
        size="small"
        type="primary"
        @click="doSubmit"
        >{{ $t("form.confirm") }}</el-button
      >
    </div>
  </el-dialog>
</template>

<script>
import { addBps, editBps, bpsUniqueness } from "@/api/iam/partners";
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
      disabled: false,
      id: "",
      form: {
        name: "",
        description: "",
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputName"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  created() {},
  methods: {
    // Input 失去光标事件
    onChange() {
      if (this.isAdd) {
        if (this.form.name != "" && this.form.name != undefined) {
          var data = {
            name: this.form.name,
          };
          bpsUniqueness(data)
            .then((res) => {
              if (res.name == true) {
                this.name_flag = false;
                this.$notify({
                  title: this.$t("message.organizationNameExists"),
                  type: "error",
                  duration: 2500,
                });
              } else {
                this.name_flag = true;
              }
            })
            .catch(() => {});
        } else {
          this.form.name = "";
          this.id_flag = true;
        }
      } else {
        var name = JSON.parse(JSON.stringify(this.info.name));
        if (
          this.form.name != name &&
          this.form.name != "" &&
          this.form.name != undefined
        ) {
          var data = {
            name: this.form.name,
          };
          bpsUniqueness(data)
            .then((res) => {
              if (res.name == true) {
                this.name_flag = false;
                this.$notify({
                  title: this.$t("message.organizationNameExists"),
                  type: "error",
                  duration: 2500,
                });
              } else {
                this.name_flag = true;
              }
            })
            .catch(() => {});
        }
      }
    },
    onOpen() {
      if (this.$refs.form) this.$refs.form.resetFields();
      if (this.isAdd === false) {
        this.$set(this.form, "name", this.info.name);
        this.$set(this.form, "description", this.info.description);
      } else {
        this.$set(this.form, "name", "");
        this.$set(this.form, "description", "");
      }
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      if (this.name_flag == false) {
        this.$notify({
          title: this.$t("message.organizationNameExists"),
          type: "error",
          duration: 2500,
        });
        return;
      }
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
      var data = JSON.parse(JSON.stringify(this.form));
      addBps(data)
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
      var data = JSON.parse(JSON.stringify(this.form));
      editBps(this.id, data)
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
        name: "",
        description: "",
      };
    },
  },
};
</script>

<style scoped></style>
