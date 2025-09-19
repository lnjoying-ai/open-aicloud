<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="editPublicKeyForm"
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
            :placeholder="$t('form.pleaseInputDesc')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.createTime') + ':'">
          <span>{{ form.createTime || "-" }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.updateTime') + ':'">
          <span>{{ form.updateTime || "-" }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.pubKeyId') + ':'">
          <span>{{ form.pubkeyId || "-" }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.pubKey') + ':'">
          <span class="w-96 block break-all leading-normal">{{
            form.pubkey || "-"
          }}</span>
        </el-form-item>
        <el-form-item label="">
          <div class="text item text-right mt-2">
            <el-button
              type="primary"
              :loading="loading"
              @click="toPubkeysEdit"
              >{{ $t("form.update") }}</el-button
            >
          </div>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

export default {
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      loading: false,
      form: {},
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
  watch: {
    id: {
      handler(val) {
        if (val) {
          this.getDetail();
        }
      },
      immediate: true,
    },
  },

  created() {
    this.getDetail(); // 获取详情
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    toPubkeysEdit() {
      this.loading = true;
      this.$refs.editPublicKeyForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .pubkeysModify(this.form, this.id)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.updateSuccess"),
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
    getDetail() {
      mainApi
        .pubkeysDetail(this.id)
        .then((res) => {
          this.form = res;
        })
        .catch((error) => {});
    },
    resetForm() {
      this.$refs.editPublicKeyForm.resetFields();
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
