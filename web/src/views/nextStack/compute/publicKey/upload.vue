<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="addPublicKeyForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <div class="text item">
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            class="w-60"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            class="w-96"
            type="textarea"
            maxlength="255"
            show-word-limit
            :rows="2"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.pubKey') + ':'" prop="pubKey">
          <el-upload
            ref="upload"
            action="#"
            :auto-upload="false"
            :before-upload="handleBeforeUpload"
            :on-change="handleChange"
          >
            <el-button slot="trigger" size="small" type="primary">{{
              $t("form.selectFile")
            }}</el-button>
          </el-upload>
          <el-input
            v-model="form.pubKey"
            class="w-96 mt-2"
            :rows="8"
            type="textarea"
            :placeholder="$t('form.pleaseInputPubKey')"
          />
        </el-form-item>
        <el-form-item label="">
          <el-alert
            :closable="false"
            class="w-96 !ml-30 !py-1"
            :title="$t('form.doNotUploadPrivateKey')"
            type="warning"
            show-icon
          />
        </el-form-item>
      </div>
      <div class="text item text-right mt-2">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="topubkeysUpload()"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

export default {
  data() {
    return {
      // 处理文件上传成功事件
      handleFileUpload(response, file) {
        console.log(response, file);
        // 读取上传的文本文件内容
        const reader = new FileReader();
        reader.onload = (e) => {
          // 将文本内容保存到数据中
          this.uploadedText = e.target.result;
        };
        reader.readAsText(file.raw);
      },
      uploadedText: null, // 保存上传的文本内容
      loading: false,
      form: {
        name: "",
        description: "",
        pubKey: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "blur",
          },
        ],
        pubKey: [
          {
            required: true,
            message: this.$t("message.pleaseInputPubKey"),
            trigger: "blur",
          },
          {
            pattern:
              /^(ssh-rsa|ssh-dss|ecdsa-sha2-nistp256|ecdsa-sha2-nistp384|ecdsa-sha2-nistp521)\s+([a-zA-Z0-9+/]+={0,3})\s*(\S+)?\s*$/,
            message: this.$t("message.pleaseInputCorrectPubKey"),
            trigger: "blur",
          },
        ],
      },
    };
  },

  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    handleBeforeUpload(file) {
      const reader = new FileReader();
      reader.onload = (e) => {
        this.form.pubKey = e.target.result;
      };
      reader.readAsText(file);
      return false; // 阻止自动上传
    },
    handleChange(file, fileList) {
      this.$refs.upload.submit();
    },
    topubkeysUpload() {
      this.loading = true;
      this.$refs.addPublicKeyForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .pubkeysUpload(this.form)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.uploadSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$emit("close");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    resetForm() {
      this.$refs.addPublicKeyForm.resetFields();
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
