<template>
  <div class="eipPoolAddPage h-full">
    <el-form
      ref="addVmForm"
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
        <el-form-item :label="$t('form.vlanId') + ':'" prop="vlanId">
          <el-input
            v-model="form.vlanId"
            class="w-60"
            :placeholder="$t('form.pleaseInputVlanId')"
            @input="vlanIdchange()"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            class="w-96"
            maxlength="255"
            show-word-limit
            type="textarea"
            :rows="2"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toEipPoolAdd()"
          >{{ $t("form.createImmediately") }}</el-button
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
      loading: false,
      form: {
        name: "",
        vlanId: "",
        description: "",
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "blur",
          },
        ],
        vlanId: [
          {
            required: true,
            message: this.$t("form.pleaseInputVlanId"),
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
    resetForm() {
      // 重置
      this.$refs.addVmForm.resetFields();
    },
    toEipPoolAdd() {
      // 快照add
      this.loading = true;
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .eipPoolsAdd(this.form)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$emit("close");
            })
            .catch((error) => {
              this.resetForm();
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    // 过滤掉数字以外的字符
    vlanIdchange() {
      this.form.vlanId = this.form.vlanId.replace(/[^\d]/g, "");

      if (this.form.vlanId > 4094) {
        this.form.vlanId = 4094;
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.eipPoolAddPage {
}
</style>
