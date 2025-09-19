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
            maxlength="255"
            show-word-limit
            type="textarea"
            :rows="2"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.cloudDisk') + ':'" prop="volumeId">
          <el-select
            v-model="form.volumeId"
            class="w-60"
            :disabled="true"
            :placeholder="$t('form.pleaseSelectCloudDisk')"
          >
            <el-option
              v-for="(item, index) in volumesList"
              :key="index"
              :label="item.name"
              :value="item.volumeId"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toVolumesSnapEdit()"
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
      volumesList: [],
      form: {
        name: "",
        description: "",
        volumeId: 0,
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        volumeId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectCloudDisk"),
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
          this.getVolumesList();
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
    toVolumesSnapEdit() {
      this.loading = true;

      this.$refs.editEipPoolForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .volumesSnapsEdit(this.form, this.id)
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
        .volumesSnapsDetail(this.id)
        .then((res) => {
          this.form.name = res.name;
          this.form.description = res.description;
          this.form.volumeId = res.volumeId;
        })
        .catch((error) => {});
    },
    getVolumesList() {
      mainApi.volumesList({ page_size: 99999, page_num: 1 }).then((res) => {
        this.volumesList = res.volumes;
      });
    },
    resetForm() {
      // 重置
      this.$refs.editEipPoolForm.resetFields();
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
