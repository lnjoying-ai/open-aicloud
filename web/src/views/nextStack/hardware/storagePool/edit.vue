<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="editEipPoolForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <!-- <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>基本信息</span>
          </div>
        </template> -->
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
      </div>
      <!-- </el-card>

      <el-card class="!border-none mb-3"> -->
      <div class="text item text-right">
        <el-button
          type="primary"
          :loading="loading"
          size="small"
          @click="toStoragePoolEdit()"
          >{{ $t("form.update") }}</el-button
        >
      </div>
      <!-- </el-card> -->
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

  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    toStoragePoolEdit() {
      this.loading = true;
      this.$refs.editEipPoolForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .storagePoolsEdit(this.form, this.id)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.modifySuccess"),
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
      // 获取详情
      this.loading = true;
      mainApi
        .storagePoolsDetail(this.id)
        .then((res) => {
          this.form.name = res.name;
          this.form.description = res.description;
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.$refs.editEipPoolForm.resetFields();
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
