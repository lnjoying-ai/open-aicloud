<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    width="800px"
    :before-close="handleCloseDialog"
    :close-on-click-modal="false"
    :title="$t('form.addProject')"
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
      <el-form-item v-if="isAdd" :label="$t('form.region') + ':'" prop="region">
        <el-select
          v-model="form.region"
          :disabled="!isAdd"
          filterable
          :placeholder="$t('form.pleaseSelect')"
          @change="changeRegion"
        >
          <el-option
            v-for="(item, index) in arealist"
            :key="index"
            :label="item.region_name"
            :value="item.region_id"
          />
        </el-select>
      </el-form-item>
      <el-alert
        v-if="isAdd && registriesList.length <= 0 && region"
        :title="$t('form.currentRegionHasNoRepository')"
        type="error"
      />
      <el-form-item :label="$t('form.projectName') + ':'" prop="name">
        <el-tooltip
          class="item"
          effect="dark"
          :content="$t('form.projectNameTips')"
          placement="right"
        >
          <el-input
            v-model="form.name"
            :disabled="!isAdd"
            :placeholder="$t('form.pleaseInputProjectName')"
          />
        </el-tooltip>
      </el-form-item>
      <el-form-item
        :label="$t('form.projectDescription') + ':'"
        prop="description"
      >
        <el-input
          v-model="form.description"
          type="textarea"
          maxlength="255"
          show-word-limit
          :placeholder="$t('form.projectDescriptionTips')"
        />
      </el-form-item>
      <el-form-item
        :label="$t('form.projectAccessScope') + ':'"
        prop="scope"
        style="display: block"
        class="formContentBlock"
      >
        <el-radio-group
          v-model="form.scope"
          :disabled="!isAdd"
          class="formContentBlock"
        >
          <el-radio :label="2">{{ $t("form.public") }}</el-radio>
          <el-radio :label="1">{{ $t("form.bpVisible") }}</el-radio>
          <el-radio :label="0">{{ $t("form.private") }}</el-radio>
        </el-radio-group>
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
import {
  editProjects,
  registries,
  addProjects,
  getUserRegions,
} from "@/api/mainApi";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    const validateName = (rule, value, callback) => {
      const nameReg = /^[a-z0-9_-]*$/;
      setTimeout(() => {
        if (nameReg.test(value)) {
          callback();
        } else {
          callback(
            new Error(this.$t("message.pleaseInputCorrectProjectNameFormat"))
          );
        }
      }, 100);
    };
    return {
      isAdd: true,
      loading: false,
      dialog: false,
      id: "",
      region: "",
      type: "0",
      form: {
        name: "",
        region: "",
        registry_id: "",
        description: "",
        scope: "",
      },
      arealist: [],
      registriesList: [],
      rules: {
        region: [
          {
            required: true,
            message: this.$t("message.pleaseSelectRegion"),
            trigger: "change",
          },
        ],
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputProjectName"),
            trigger: "blur",
          },
          { trigger: "blur", validator: validateName },
        ],
        scope: [
          {
            required: true,
            message: this.$t("message.pleaseSelectAccessScope"),
            trigger: "blur",
          },
        ],
      },
      registry_id: "",
      project_id: "",
    };
  },
  methods: {
    add() {
      this.isAdd = true;
      this.dialog = true;
    },
    edit(obj) {
      this.isAdd = false;
      this.dialog = true;
      const params = {
        registry_id: obj.registry_id,
        name: obj.project_name,
        description: obj.project_desc,
        scope: obj.scope,
        registry_name: obj.registry_name,
      };
      this.registry_id = obj.registry_id;
      this.project_id = obj.project_id;
      this.form = Object.assign({}, params);
    },
    // 切换区域
    changeRegion(e) {
      this.form.region = e;
      const obj = {
        region_id: e,
        page_size: 100,
        page_num: 1,
      };
    },
    async onOpen() {
      const list = await getUserRegions();
      this.arealist = list;
    },
    cancel() {
      this.resetForm();
      this.dialog = false;
    },
    handleSelect(e) {
      this.type = e;
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

      if (
        this.arealist.length > 0 &&
        this.arealist != undefined &&
        this.arealist != null
      ) {
        for (var i = 0; i < this.arealist.length; i++) {
          if (this.arealist[i].region_id == data.region) {
            data.registry_id = this.arealist[i].registry_id;
          }
        }
      }
      if (data.registry_id) {
        addProjects(data.registry_id, data)
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
      } else {
        this.loading = false;
        this.$notify({
          title: this.$t("message.pleaseSelectRepository"),
          type: "warning",
          duration: 2500,
        });
      }
    },
    doEdit() {
      editProjects(this.registry_id, this.project_id, {
        description: this.form.description,
      })
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
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.type = 0;
      this.form = { name: "", description: "", registry_id: "", scope: "" };
    },
  },
};
</script>

<style scoped lng="scss">
::v-deep .el-menu-demo {
  display: flex;
  justify-content: center;
}
</style>
