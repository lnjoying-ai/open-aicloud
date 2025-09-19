<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.edit')"
      width="800px"
      :close-on-click-modal="false"
      @open="onOpen"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="108px"
      >
        <el-form-item
          :label="$t('form.imageRepositoryDescription') + ':'"
          prop="description"
        >
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>

        <el-form-item :label="$t('form.label') + ':'" prop="label">
          <el-tag
            v-for="tag in form.label"
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
  </div>
</template>

<script>
import { upDateRepo } from "@/api/mainApi";

export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      regions: [],
      sites: [],
      loading: false,
      dialog: false,
      form: {
        description: "",
        label: [],
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        description: [
          {
            required: true,
            message: this.$t("message.pleaseInputDescription"),
            trigger: "change",
          },
        ],
      },
      project_id: "",
      registry_id: "",
      repo_name: "",
    };
  },
  watch: {},
  methods: {
    onOpen() {},
    show(info) {
      this.resetForm();
      this.dialog = true;
      this.project_id = info.project_id;
      this.registry_id = info.registry_id;
      this.repo_name = info.repo_name;
      if (info.description) {
        this.$set(this.form, "description", info.description);
      }
      if (info.labels && info.labels.length > 0) {
        this.$set(this.form, "label", info.labels);
      }
    },
    cancel() {
      this.resetForm();
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
      this.repo_name = encodeURIComponent(encodeURIComponent(this.repo_name));
      upDateRepo(this.registry_id, this.project_id, this.repo_name, this.form)
        .then(() => {
          this.resetForm();
          this.loading = false;
          this.$parent.handleCurrentChange();
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
      this.form = {
        description: "",
        label: [],
      };
      this.project_id = "";
      this.registry_id = "";
      this.repo_name = "";
    },
    handleClose(tag) {
      this.form.label.splice(this.form.label.indexOf(tag), 1);
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.form.label.push(inputValue);
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
  },
};
</script>

<style lang="scss"></style>
