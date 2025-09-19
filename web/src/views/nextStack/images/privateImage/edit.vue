<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="editImageForm"
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
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toImageEdit()"
          >{{ $t("form.update") }}</el-button
        >
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
      editImageForm: {},
      form: {
        name: "",
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
  methods: {
    toImageEdit() {
      this.loading = true;

      this.$refs.editImageForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .imageModify(this.form, this.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.modifySuccess"),
                type: "success",
                duration: 2500,
              });
              this.loading = false;
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
      mainApi
        .imageDetail(this.id)
        .then((res) => {
          this.form.name = res.imageName;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
