<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    :title="isAdd ? $t('form.addVersion') : $t('form.editVersion')"
    width="1200px"
    :before-close="handleCloseDialog"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      size="small"
      label-width="120px"
    >
      <el-form-item
        :label="$t('form.policyContent') + ':'"
        :prop="'policy_document.statement'"
        :rules="[
          {
            required: true,
            message: $t('form.pleaseInputPolicyContent'),
            trigger: 'blur',
          },
        ]"
      >
        <div>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            @click="handleAddDocument($t('form.policyRule'))"
            >{{ $t("form.rule") }}</el-button
          >
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            @click="handleAddDocument($t('form.policyOperation'))"
            >{{ $t("form.operation") }}</el-button
          >
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-plus"
            @click="handleAddDocument($t('form.policyResource'))"
            >{{ $t("form.resource") }}</el-button
          >
        </div>
        <div
          v-for="(item, index) in form.policy_document.statement"
          :key="index"
          style="margin-top: 10px"
        >
          <el-row :gutter="20">
            <el-col :span="20">
              <span style="font-weight: 600; font-size: 14px; color: #606266">{{
                item.title
              }}</span>
              <yaml-editor
                ref="yamlEditor"
                v-model="item.rule"
                class="leading-tight overflow-auto rounded max-h-96 min-w-full"
                :download-name="$t('form.policyContent')"
                :download-type="'yml'"
                :readonly="false"
                :lint="false"
              />
            </el-col>
            <el-col :span="2" style="margin-top: 30px">
              <el-button
                size="mini"
                icon="el-icon-remove-outline"
                plain
                type="danger"
                @click="handleDeleteDocument(item, index)"
              >
                {{ $t("form.delete") }}</el-button
              >
            </el-col>
          </el-row>
        </div>
      </el-form-item>
      <el-form-item :label="$t('form.description') + ':'" prop="description">
        <el-row>
          <el-col :span="22">
            <el-input
              v-model="form.description"
              type="textarea"
              maxlength="255"
              show-word-limit
              :rows="2"
              :placeholder="$t('form.pleaseInputDescription')"
            />
          </el-col>
        </el-row>
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
import { addPolicieVersion } from "@/api/iam/policie";
import YamlEditor from "@/components/yaml/editor.vue";
var Base64 = require("js-base64").Base64;

export default {
  components: {
    YamlEditor,
  },
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
      form: {
        description: "",
        policy_document: {
          version: "",
          type: "rego",
          statement: [],
        },
      },
      rules: {},
      title: "",
    };
  },
  methods: {
    handleAddDocument(value) {
      var title = value;
      this.form.policy_document.statement.push({
        package: "",
        rule: "",
        title: title,
      });
    },
    handleDeleteDocument(item, index) {
      this.form.policy_document.statement.splice(index, 1);
    },
    cancel() {
      this.resetForm();
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
      var data = JSON.parse(JSON.stringify(this.form));
      data.policy_document.statement.forEach((element) => {
        delete element.title;
        element.rule = Base64.encode(element.rule);
      });
      data.policy_document = JSON.stringify(data.policy_document);
      addPolicieVersion(this.id, data)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.operationSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.searchinit();
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
        description: "",
        policy_document: {
          version: "",
          type: "rego",
          statement: [],
        },
      };
    },
  },
};
</script>

<style scoped></style>
