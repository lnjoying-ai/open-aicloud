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
        <el-form-item :label="$t('form.name') + ':'" prop="mapName">
          <el-input
            v-model="form.mapName"
            class="w-60"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          :loading="loading"
          size="small"
          @click="toSnapsEdit()"
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
      editEipPoolForm: {},
      form: {
        mapName: "",
      },
      rules: {
        mapName: [
          {
            required: true,
            message: this.$t("message.pleaseInputNatGatewayName"),
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
    toSnapsEdit() {
      this.loading = true;
      this.$refs.editEipPoolForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .eipsEdit(this.id, {
              name: this.form.mapName,
            })
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
        .portMapDetail(this.id)
        .then((res) => {
          this.form.mapName = res.mapName;
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
