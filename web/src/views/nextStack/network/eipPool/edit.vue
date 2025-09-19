<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="editEipPoolForm"
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
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toBmsEdit()"
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

  created() {
    this.getDetail();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    toBmsEdit() {
      this.loading = true;
      this.$refs.editEipPoolForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .eipPoolsModify(this.form, this.id)
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
      mainApi
        .eipPoolsDetail(this.id)
        .then((res) => {
          this.form.name = res.name;
          this.form.description = res.description;
        })
        .catch((error) => {});
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
