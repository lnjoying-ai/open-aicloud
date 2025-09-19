<template>
  <el-dialog
    :title="isAdd ? $t('form.addWarehouse') : $t('form.editWarehouse')"
    :append-to-body="true"
    :visible.sync="dialog"
    width="800px"
    :before-close="handleCloseDialog"
    :close-on-click-modal="false"
    :destrot-on-close="true"
  >
    <el-tabs
      v-model="type"
      class="registryAddTabsStyle"
      @tab-click="handleSelect(type)"
    >
      <el-tab-pane label="harbor" name="0" />
      <el-tab-pane label="docker hub" name="1" />
    </el-tabs>

    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      size="small"
      label-width="120px"
    >
      <el-form-item
        :label="$t('form.warehouseName') + ':'"
        prop="registry_name"
      >
        <el-input
          v-model="form.registry_name"
          :placeholder="$t('form.pleaseInputWarehouseName')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('form.warehouseDesc') + ':'"
        prop="registry_desc"
      >
        <el-input
          v-model="form.registry_desc"
          type="textarea"
          maxlength="255"
          show-word-limit
          :placeholder="$t('form.pleaseInputWarehouseDesc')"
        />
      </el-form-item>
      <el-form-item :label="$t('form.url') + ':'" prop="url">
        <el-input
          v-model="form.url"
          placeholder="harbor.io"
          :disabled="type == 1"
        />
      </el-form-item>
      <el-form-item :label="$t('form.userName') + ':'" prop="access_key">
        <el-input
          v-model="form.access_key"
          :placeholder="$t('form.pleaseInputUserName')"
        />
      </el-form-item>
      <el-form-item
        v-if="type == 0"
        :label="$t('form.password') + ':'"
        prop="access_secret"
      >
        <el-input
          v-model="form.access_secret"
          show-password
          :placeholder="$t('form.pleaseInputPassword')"
        />
      </el-form-item>
      <el-form-item
        v-if="type == 1"
        :label="$t('form.password') + ':'"
        prop="docker_access_secret"
      >
        <el-input
          v-model="form.docker_access_secret"
          show-password
          :placeholder="$t('form.pleaseInputPassword')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('form.verifyCertificate') + ':'"
        prop="insecure"
        style="display: block"
        class="formContentBlock"
      >
        <el-switch
          v-model="form.insecure"
          style="margin-top: -3px"
          active-color="#13ce66"
          inactive-color="#ff4949"
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
import { add3rd, edit3rd } from "@/api/mainApi";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    var valiatePass1 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputPassword")));
      } else if (!/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[^]{8,20}$/.test(value)) {
        callback(new Error(this.$t("form.passwordLength")));
      } else {
        callback();
      }
    };
    var valiatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputPassword")));
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
      isAdd: true,
      loading: false,
      dialog: false,
      disable: false,
      id: "",
      type: "0",
      editRegistryId: "", // 编辑的仓库id
      form: {
        registry_name: "",
        registry_desc: "",
        url: "",
        access_key: "",
        access_secret: "",
        docker_access_secret: "",
        insecure: false,
      },
      rules: {
        registry_name: [
          {
            required: true,
            message: this.$t("message.pleaseInputWarehouseName"),
            trigger: "change",
          },
        ],

        access_key: [
          {
            required: true,
            message: this.$t("message.pleaseInputWarehouseUserName"),
            trigger: "change",
          },
        ],
        access_secret: [
          {
            required: true,
            message: this.$t("message.pleaseInputPassword"),
            trigger: "change",
          },
          { trigger: "change", validator: valiatePass1 },
        ],
        docker_access_secret: [
          {
            required: true,
            message: this.$t("message.pleaseInputPassword"),
            trigger: "change",
          },
          { trigger: "change", validator: valiatePass2 },
        ],
        url: [
          {
            required: true,
            message: this.$t("message.pleaseInputUrl"),
            trigger: "change",
          },
        ],
      },
      harborStatus: false,
      dockerStatus: false,
    };
  },
  methods: {
    add() {
      this.isAdd = true;
      this.dialog = true;
      this.type = "0";
      this.dockerStatus = false;
      this.harborStatus = false;
    },
    edit(params) {
      this.type = params.type + "";
      this.isAdd = false;
      this.dialog = true;
      if (params.type == 0) {
        this.dockerStatus = true;
        this.harborStatus = false;
      } else {
        this.harborStatus = true;
        this.dockerStatus = false;
      }
      this.editRegistryId = params.registry_id;
      this.form = {
        registry_name: params.registry_name,
        registry_desc: params.registry_desc,
        url: params.url,
        access_key: params.access_key,
        access_secret: "",
        docker_access_secret: "",
        insecure: params.insecure,
      };
    },
    cancel() {
      this.resetForm();
    },
    handleSelect(e) {
      this.type = e;
      this.$refs["form"].resetFields();
      if (e == 1) {
        this.form.url = "docker.io";
      } else {
        this.form.url = "";
      }
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
      if (this.type == 1) {
        this.form.access_secret = this.form.docker_access_secret;
      }
      var data = JSON.parse(JSON.stringify(this.form));
      data.type = this.type;
      if (this.type == 1) {
        data.url = "docker.io";
      }
      add3rd(data)
        .then(() => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.searchinit();
        })
        .catch(() => {
          this.loading = false;
        });
    },
    doEdit() {
      this.form.type = this.type;
      if (this.type == 1) {
        this.form.access_secret = this.form.docker_access_secret;
      }
      edit3rd(this.editRegistryId, this.form)
        .then(() => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.searchinit();
        })
        .catch(() => {
          this.loading = false;
        });
    },
    handleCloseDialog() {
      this.resetForm();
    },
    resetForm() {
      this.dialog = false;

      this.type = "0";
      this.editRegistryId = "";
      this.$refs["form"].resetFields();
    },
  },
};
</script>

<style scoped lang="scss">
.registryAddTabsStyle {
  text-align: center;
  margin-bottom: 30px;

  ::v-deep .el-tabs__nav {
    float: none;
    margin: 0 auto;

    .el-tabs__active-bar {
      display: none;
    }

    .el-tabs__item {
      font-size: 16px;
      padding: 0;
      margin: 0 20px;

      &.is-active {
        border-bottom: 2px solid #409eff;
      }
    }
  }
}
</style>
