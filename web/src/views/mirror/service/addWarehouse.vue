<template>
  <div>
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.addWarehouse') : $t('form.editWarehouse')"
      width="800px"
      :before-close="handleCloseDialog"
      :close-on-click-modal="false"
      @open="onOpen"
      @closed="cancel"
    >
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
            :placeholder="$t('form.warehouseNameTips')"
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
            :placeholder="$t('form.warehouseDescTips')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.warehouseType') + ':'"
          prop="type"
          class="formContentBlock"
        >
          <el-radio-group
            v-model="form.type"
            :disabled="!isAdd"
            @change="changeRadio"
          >
            <el-radio :label="0">harbor</el-radio>
            <el-radio :label="1">{{ $t("form.other") }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('form.url') + ':'" prop="url">
          <el-input
            v-model="form.url"
            :placeholder="$t('form.pleaseInputWarehouseAddress')"
          />
        </el-form-item>
        <el-form-item
          v-if="isAdd"
          :label="$t('form.warehouseAdminAccount') + ':'"
          prop="admin_name"
        >
          <input type="password" hidden autocomplete="new-password" />

          <el-input
            v-model="form.admin_name"
            :placeholder="$t('form.pleaseInputWarehouseAdminAccount')"
          />
        </el-form-item>
        <el-form-item
          v-if="isAdd"
          :label="$t('form.warehouseAdminPassword') + ':'"
          prop="admin_passwd"
        >
          <el-input
            v-model="form.admin_passwd"
            type="password"
            show-password
            :placeholder="$t('form.pleaseInputWarehouseAdminPassword')"
          />
        </el-form-item>

        <el-form-item :label="$t('form.regionAddress') + ':'" prop="regions">
          <el-transfer
            v-model="form.regions"
            :data="nodes"
            :titles="[$t('form.sourceRegion'), $t('form.targetRegion')]"
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
      :title="$t('form.warehouseCommandTips')"
      :visible.sync="VisibleTitle"
      width="800px"
    >
      <span>{{ $t("form.warehouseName") }}：{{ registry_name }}</span>
      <br />
      <span
        >{{ $t("form.offlineInstallCommand") }}：{{ offLine
        }}<i
          class="el-icon-document-copy"
          style="font-size: 17px; margin-left: 1%"
          @click="copy($event, offLine)"
      /></span>
      <br />
      <span
        >{{ $t("form.onlineInstallCommand") }}：{{ onLine
        }}<i
          class="el-icon-document-copy"
          style="font-size: 17px; margin-left: 1%"
          @click="copy($event, onLine)"
      /></span>
      <br />
      <span
        >{{ $t("form.partialOnlineInstallCommand") }}：{{ partialLine
        }}<i
          class="el-icon-document-copy"
          style="font-size: 17px; margin-left: 1%"
          @click="copy($event, partialLine)"
      /></span>
      <br />
      <span
        >{{ $t("form.deleteCommand") }}：{{ delLine
        }}<i
          class="el-icon-document-copy"
          style="font-size: 17px; margin-left: 1%"
          @click="copy($event, delLine)"
      /></span>
      <br />
      <span
        >{{ $t("form.stopCommand") }}：{{ stopLine
        }}<i
          class="el-icon-document-copy"
          style="font-size: 17px; margin-left: 1%"
          @click="copy($event, stopLine)"
      /></span>
      <div style="color: red; font-size: 12px; padding-top: 20px">
        <span style="margin-bottom: 5px; display: block">{{
          $t("form.installationInstructions")
        }}</span>
        <span
          style="text-indent: 2em; display: block"
          v-html="$t('message.harborInstall')"
        />

        <span
          style="text-indent: 2em; display: block"
          v-html="$t('message.harborInstall1')"
        />
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="VisibleTitle = false">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" @click="VisibleTitle = false">{{
          $t("form.confirm")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { addRegistries, editRegistries, getRegions } from "@/api/mainApi";
import Clipboard from "clipboard";
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
    var valiateUrl = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputUrl")));
      } else if (
        !/^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+(:([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5]))?$/.test(
          value
        )
      ) {
        callback(
          new Error(this.$t("message.pleaseInputCorrectRepositoryAddress"))
        );
      } else {
        callback();
      }
    };
    var validateAdminName = (rule, value, callback) => {
      const regStartsWithLetter = /^[a-zA-Z]/;
      const regContainsAllowedCharacters = /^[^,~#$%]+$/;
      const regValidLength = /^.{2,64}$/;
      if (!regStartsWithLetter.test(value)) {
        callback(
          new Error(this.$t("message.repositoryAdminMustStartWithLetter"))
        );
        return;
      }
      if (!regContainsAllowedCharacters.test(value)) {
        callback(
          new Error(this.$t("message.repositoryAdminContainsIllegalCharacters"))
        );
        return;
      }
      if (!regValidLength.test(value)) {
        callback(
          new Error(this.$t("message.repositoryAdminLengthBetween2And64"))
        );
        return;
      }
      callback();
    };
    return {
      nodes: [],
      isAdd: true,
      loading: false,
      dialog: false,
      VisibleTitle: false,
      id: "",
      form: {
        type: 0,
        admin_name: "",
        registry_name: "",
        url: "",
        admin_passwd: "",
        admin_rpasswd: "",
      },
      offLine: "",
      onLine: "",
      partialLine: "",
      delLine: "",
      stopLine: "",
      registry_name: "",
      registry_id: "",
      rules: {
        registry_name: [
          {
            required: true,
            message: this.$t("message.pleaseInputRepositoryName"),
            trigger: "change",
          },
        ],

        type: [
          {
            required: true,
            message: this.$t("message.pleaseSelectRepositoryType"),
            trigger: "change",
          },
        ],
        url: [{ required: true, trigger: "change", validator: valiateUrl }],
        admin_name: [
          // {
          //   required: true,
          //   message: "请输入仓库管理员账号",
          //   trigger: "change",
          // },
          { required: true, trigger: "blur", validator: validateAdminName },
        ],
        admin_passwd: [
          { required: true, trigger: "change", validator: valiatePass1 },
        ],
        regions: [
          {
            required: true,
            message: this.$t("message.pleaseAddRegionAddress"),
            trigger: "change",
          },
        ],
      },
      arealist: [],
    };
  },
  methods: {
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          title: this.$t("message.copySuccess"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
        this.VisibleTitle = true;
      });
      clipboard.on("error", () => {
        // 不支持复制
        this.$notify({
          title: this.$t("message.copyFailed"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
    add() {
      this.dialog = true;
      this.isAdd = true;
    },
    check(obj) {
      this.dialog = true;
      this.isAdd = false;
      const regions = obj.regions.map((item) => {
        return item.region_id;
      });
      this.form = Object.assign({}, obj, { regions: regions });
    },
    async onOpen() {
      const list = await getRegions();
      this.nodes = list.regions.map((item) => {
        return {
          key: item.id,
          value: item.id,
          label: item.name,
        };
      });
    },
    cancel() {
      this.resetForm();
      this.dialog = false;
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
      var data = JSON.parse(JSON.stringify(this.form));

      data.registry_id = "";

      addRegistries(data)
        .then((res) => {
          (this.offLine = res.harbor_install_offline),
            (this.onLine = res.harbor_install_online),
            (this.partialLine = res.harbor_install_online_part),
            (this.delLine = res.harbor_remove);
          this.stopLine = res.harbor_stop;
          this.registry_id = res.registry_id;
          this.registry_name = res.registry_name;
          this.VisibleTitle = true;
          this.loading = false;
          this.resetForm();
          this.$parent.init();
        })
        .catch(() => {
          this.loading = false;
        });
    },

    Visible() {
      this.VisibleTitle = false;
    },
    doEdit() {
      editRegistries(this.form.registry_id, this.form)
        .then(() => {
          this.$notify({
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.resetForm();
          this.$parent.init();
        })
        .catch(() => {
          this.loading = false;
        });
    },
    handleCloseDialog() {
      this.dialog = false;
    },
    changeRadio(e) {
      this.form.type = e;
    },
    resetForm() {
      this.dialog = false;
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = {
        type: 0,
        admin_name: "",
        registry_name: "",
        url: "",
        admin_passwd: "",
        admin_rpasswd: "",
      };
    },
  },
};
</script>
