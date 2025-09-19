<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.addTemplate') : $t('form.editTemplate')"
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
        label-width="130px"
      >
        <el-form-item :label="$t('form.templateName') + ':'" prop="name">
          <el-input
            v-model="form.name"
            :disabled="!isAdd"
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.templateVersion') + ':'" prop="version">
          <div>
            <el-input
              v-model="form.version"
              :placeholder="$t('form.pleaseInputContent')"
              style="width: 215px; display: inline-block"
            />

            <span
              v-if="!isAdd"
              style="
                color: #fbb561;
                text-align: left;
                padding-left: 10px;
                font-size: 12px;
              "
              >{{ $t("form.modifyVersion") }}</span
            >
            <!-- <p>若修改版本号，则另存为新版本</p> -->
          </div>
        </el-form-item>
        <el-form-item
          :label="$t('form.templateDescription') + ':'"
          prop="description"
        >
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.templateLogo') + ':'" prop="logo_url">
          <el-input
            v-model="form.logo_url"
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.aosType') + ':'" prop="aos_type">
          <el-select
            v-model="form.aos_type"
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              key="docker-compose"
              label="docker-compose"
              value="docker-compose"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('form.templateFormat') + ':'"
          prop="content_type"
        >
          <el-input
            v-model="form.content_type"
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.label') + ':'" prop="labels">
          <el-tag
            v-for="tag in form.labels"
            :key="tag"
            class="mr-3"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="inputVisible"
            ref="saveTagInput"
            v-model="inputValue"
            class="w-28"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          />
          <el-button
            v-else
            class="button-new-tag"
            size="small"
            @click="showInput"
            >{{ $t("form.newTag") }}</el-button
          >
        </el-form-item>
        <el-form-item label="stack_compose:" prop="stack_compose">
          <el-button
            type="primary"
            size="mini"
            class="drbtn"
            @click="clickLoad"
            >{{ $t("form.import") }}</el-button
          >
          <input
            id="files"
            ref="refFile"
            type="file"
            style="display: none"
            @change="fileLoad"
          />
          <yaml-editor
            v-if="dialog"
            ref="yamlEditorStack"
            v-model="form.stack_compose"
            style="
              line-height: 1.2;
              max-height: 350px;
              overflow: auto;
              border-radius: 4px;
              margin-top: 10px;
              min-width: 100%;
            "
            :download-name="'addTemplate-stack_compose'"
            :download-type="'yml'"
            :lint="true"
            :is-add="isAdd"
            :placeholder="stackPlaceholder"
          />
        </el-form-item>
        <el-form-item label="justice_compose:" prop="justice_compose">
          <el-button
            type="primary"
            size="mini"
            class="drbtn"
            @click="clickLoad1"
            >{{ $t("form.import") }}</el-button
          >
          <input
            id="files1"
            ref="refFile1"
            type="file"
            style="display: none"
            @change="fileLoad1"
          />
          <yaml-editor
            v-if="dialog"
            ref="yamlEditorJustice"
            v-model="form.justice_compose"
            style="
              line-height: 1.2;
              max-height: 350px;
              overflow: auto;
              border-radius: 4px;
              margin-top: 10px;
              min-width: 100%;
            "
            :download-name="'addTemplate-justice_compose'"
            :download-type="'yml'"
            :placeholder="justicePlaceholder"
            :is-add="isAdd"
            :lint="true"
            @changeLint="getLintState2($event, 'justice_compose')"
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
import { addTemplates, editTemplates, templatesInfo } from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";
import yaml from "js-yaml";
import { mapGetters } from "vuex";
export default {
  components: { YamlEditor },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  data() {
    return {
      yamlLintState1: false,
      yamlLintState2: false,
      readonly: "nocursor",
      regions: [],
      sites: [],
      isAdd: true,
      id: "",
      loading: false,
      dialog: false,
      form: {
        name: "",
        version: "",
        description: "",
        logo_url: "",
        vendor: "",
        aos_type: "docker-compose",
        content_type: "yaml",
        labels: [],
        stack_compose: "",
        justice_compose: "",
      },
      inputVisible: false,
      inputValue: "",
      versions: [],
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputTemplateName"),
            trigger: "blur",
          },
        ],
        version: [
          {
            required: true,
            message: this.$t("form.pleaseInputTemplateVersion"),
            trigger: "blur",
          },
        ],
        stack_compose: [
          {
            required: true,
            message: this.$t("form.pleaseInputStackCompose"),
            trigger: "blur",
          },
        ],
      },
      stackPlaceholder:
        "# version: '3'\n# services:\n#   hello:\n#     image: hello-world:latest\n#     labels:\n#       foo: bar",
      justicePlaceholder:
        "# basic:\n#   name: justice_compose_example\n#   version: '1.0'\n#   description: justice compose example\n#   uuid: 1-1-1-1\n#   minimum_justice_version: v1.0\n#   input_params:\n#     - variable: 字符串参数\n#       description: 字符串参数示例\n#       label: string\n#       type: string\n#       required: true\n#       default_value: 默认值\n#     - variable: 枚举参数\n#       description: 枚举参数示例\n#       label: enum\n#       type: enum\n#       required: true\n#       default_value: k2\n#       options:\n#         - k1\n#         - k2\n#         - k3\n#     - variable: 应用配置参数\n#       description: 应用配置参数示例\n#       label: CfgOption\n#       type: CfgOption\n#       required: true\n#       default_value: ''",
    };
  },
  watch: {},
  mounted() {},
  methods: {
    getLintState1(state) {
      this.yamlLintState1 = state;
    },
    getLintState2(state) {
      this.yamlLintState2 = state;
    },
    onOpen() {
      if (this.$refs.form) this.$refs.form.resetFields();
      if (this.isAdd === false) {
        this.$set(this.form, "id", this.info.id);
        this.$set(this.form, "name", this.info.name);
        this.$set(this.form, "version", this.info.version);
        this.$set(this.form, "description", this.info.description);
        this.$set(this.form, "logo_url", this.info.logo_url);
        this.$set(this.form, "vendor", this.info.vendor);
        this.$set(this.form, "aos_type", this.info.aos_type);
        this.$set(this.form, "content_type", this.info.content_type);
        this.$set(
          this.form,
          "labels",
          this.info.labels == null ? [] : this.info.labels
        );
        this.$set(
          this.form,
          "stack_compose",
          this.info.stack_compose ? this.info.stack_compose : ""
        );
        this.$set(
          this.form,
          "justice_compose",
          this.info.justice_compose ? this.info.justice_compose : ""
        );
      } else {
        this.form = {
          name: "",
          version: "",
          description: "",
          logo_url: "",
          vendor: "",
          aos_type: "docker-compose",
          content_type: "yaml",
          labels: [],
          stack_compose: "",
          justice_compose: "",
        };
      }
    },
    // 版本下拉框改变触发
    changeVersions(val) {
      this.form.version = val;
      this.getTemplatesInfo(val);
    },
    // 获取详情信息
    getTemplatesInfo(val) {
      templatesInfo(val)
        .then((res) => {
          if (res.labels == null) {
            res.labels = [];
          }
          this.form = res;
          this.form.nowVersion = res.id;
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      if (this.yamlLintState1) {
        this.$notify({
          title: this.$t("form.stackComposeFormatError"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      if (this.yamlLintState2) {
        this.$notify({
          title: this.$t("form.justiceComposeFormatError"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      this.form.stack_compose == this.stackPlaceholder
        ? (this.form.stack_compose = "")
        : "";
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAdd || this.form.version != this.info.version) {
            this.doAdd();
          } else this.doEdit();
        } else {
          return false;
        }
      });
    },
    doAdd() {
      var data = JSON.parse(JSON.stringify(this.form));
      data.stack_compose == this.stackPlaceholder
        ? (data.stack_compose = "")
        : "";
      data.justice_compose == this.justicePlaceholder
        ? (data.justice_compose = "")
        : "";
      if (data.labels && data.labels.length == 0) {
        delete data.labels;
      }
      data.stack_compose == this.stackPlaceholder
        ? (data.stack_compose = "")
        : "";
      data.justice_compose == this.justicePlaceholder
        ? (data.justice_compose = "")
        : "";
      addTemplates(data)
        .then((res) => {
          this.$parent.init();
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.resetForm();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    doEdit() {
      this.form.stack_compose == this.stackPlaceholder
        ? (this.form.stack_compose = "")
        : "";
      this.form.justice_compose == this.justicePlaceholder
        ? (this.form.justice_compose = "")
        : "";
      editTemplates(this.form, this.form.id)
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
        version: "",
        description: "",
        logo_url: "",
        vendor: "",
        aos_type: "",
        content_type: "yaml",
        labels: [],
        stack_compose: "",
        justice_compose: "",
      };
    },
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.form.labels.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    clickLoad() {
      this.$refs.refFile.dispatchEvent(new MouseEvent("click"));
    },
    fileLoad() {
      var _this = this;
      const selectedFile = _this.$refs.refFile.files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      _this.$refs.yamlEditorStack.placeholders = false;
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        _this.form.stack_compose = this.result;
      };
      document.getElementById("files").value = "";
    },
    clickLoad1() {
      this.$refs.refFile1.dispatchEvent(new MouseEvent("click"));
    },
    fileLoad1() {
      var _this = this;
      const selectedFile = this.$refs.refFile1.files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      _this.$refs.yamlEditorJustice.placeholders = false;
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        _this.form.justice_compose = this.result;
      };
      document.getElementById("files1").value = "";
    },
  },
};
</script>

<style lang="scss">
.drbtn {
  outline: none;
  position: absolute;
  transform: translate(-120%, 100%);
}
</style>
