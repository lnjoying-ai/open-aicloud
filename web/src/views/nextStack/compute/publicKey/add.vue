<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="addPublicKeyForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="100px"
    >
      <div class="text item">
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            class="w-60"
            :disabled="pubkeysInfo != ''"
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
            :disabled="pubkeysInfo != ''"
            :rows="2"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <div class="text item text-right">
          <el-button
            v-if="pubkeysInfo == ''"
            type="primary"
            size="small"
            class="text-right"
            :loading="loading"
            @click="toPubkeysAdd"
            >{{ $t("form.createImmediately") }}</el-button
          >
        </div>
      </div>
      <div v-if="pubkeysInfo" class="text item">
        <el-form-item :label="$t('form.pubKeyId') + ':'">
          <span>{{ pubkeysInfo.pubkeyId }}</span>
        </el-form-item>

        <el-form-item :label="$t('form.privateKey') + ':'">
          <div>
            <el-alert
              :closable="false"
              :title="$t('form.pleaseCopyPrivateKey')"
              type="warning"
              show-icon
            />
            <el-input
              v-model="pubkeysInfo.privateKey"
              class="w-96 mt-2 break-all overflow-auto"
              type="textarea"
              :rows="12"
              disabled
            />
          </div>
        </el-form-item>
        <el-form-item label="">
          <el-button
            type="primary"
            class="float-left"
            size="small"
            @click="copy(pubkeysInfo.privateKey)"
            >{{ $t("form.copyPrivateKey") }}</el-button
          >
          <el-button
            type="success"
            class="float-left"
            size="small"
            @click="toDownload(pubkeysInfo.privateKey)"
            >{{ $t("form.downloadPrivateKey") }}</el-button
          >
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

// import useClipboard from "vue-clipboard3";

export default {
  data() {
    return {
      loading: false,
      pubkeysInfo: "",
      form: {
        name: "",
        description: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
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
    // 清空pubkeysInfo的数据
    clearpubkeysInfo() {
      this.pubkeysInfo = "";
    },
    toPubkeysAdd() {
      this.loading = true;
      this.$refs.addPublicKeyForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .pubkeysAdd(this.form)
            .then((res) => {
              this.loading = false;
              this.pubkeysInfo = res;
              this.$notify({
                type: "success",
                title: this.$t("message.createSuccess"),
                duration: 2500,
              });
              this.toDownload(res.privateKey);
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    toDownload(privateKey) {
      // 下载私钥
      const blob = new Blob([privateKey]); // 处理文档流
      const fileName = "id_rsa(" + this.form.name + ").pem";
      const down = document.createElement("a");
      down.download = fileName;
      down.style.display = "none"; // 隐藏,没必要展示出来
      down.href = URL.createObjectURL(blob);
      document.body.appendChild(down);
      down.click();
      URL.revokeObjectURL(down.href); // 释放URL 对象
      document.body.removeChild(down);
    },
    copy(Msg) {
      this.$script.copyText(Msg);
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
