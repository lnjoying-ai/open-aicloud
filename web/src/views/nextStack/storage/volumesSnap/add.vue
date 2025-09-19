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
            :placeholder="$t('form.pleaseSelectCloudDisk')"
          >
            <el-option
              v-for="(item, index) in volumesList"
              :key="index"
              :label="item.name + ' (' + item.size + $t('unit.disk') + ')'"
              :value="item.volumeId"
            />
          </el-select>
        </el-form-item>
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          :loading="loading"
          size="small"
          @click="toVolumesSnapsAdd()"
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
      volumesList: [],
      form: {
        name: "",
        description: "",
        volumeId: "",
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

  created() {
    this.getVolumesList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    resetForm() {
      // 重置
      this.$refs.addVmForm.resetFields();
    },
    toVolumesSnapsAdd() {
      this.loading = true;
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .volumesSnapsAdd(this.form)
            .then((res) => {
              this.loading = false;
              this.resetForm();
              this.$notify({
                title: this.$t("message.startCreate"),
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
    getVolumesList() {
      mainApi.volumesList({ page_size: 99999, page_num: 1 }).then((res) => {
        this.volumesList = res.volumes;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.eipPoolAddPage {
}
</style>
