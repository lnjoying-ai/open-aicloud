<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="editEipPoolForm"
      v-loading="loading"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="120px"
      :element-loading-text="$t('domain.loading')"
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
            maxlength="255"
            show-word-limit
            type="textarea"
            :rows="2"
            :placeholder="$t('form.pleaseInputDesc')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.capacity') + ':'" prop="size">
          <el-input-number
            v-model="form.size"
            :disabled="true"
            :min="10"
            :max="5000"
            :step="10"
            :precision="0"
            controls-position="right"
            class="w-60"
            style="width: 215px"
          />
          <span class="ml-2">GB</span>
        </el-form-item>
        <!-- <el-form-item label="云盘类型"
                        prop="type">
            <div class="w-full">
              <el-radio-group v-model="nodeIdAuto">
                <el-radio :label="true"
                          :value="true">普通云盘</el-radio>
                <el-radio :label="false"
                          :value="false">共享云盘</el-radio>
              </el-radio-group>
            </div>
          </el-form-item> -->
        <!-- <el-form-item label="镜像"
                        prop="type">
            <el-select v-model="form.type"
                       placeholder="请选择镜像">
              <el-option label="全部"
                         value="0" />
              <el-option label="实例"
                         value="1" />
              <el-option label="裸金属"
                         value="2" />
            </el-select>
          </el-form-item> -->
        <el-form-item
          :label="$t('form.cloudDiskType') + ':'"
          prop="storagePoolId"
        >
          <el-select
            v-model="form.storagePoolId"
            :disabled="true"
            class="w-60"
            :placeholder="$t('form.pleaseSelectCloudDiskType')"
          >
            <el-option
              v-for="(item, index) in storagePoolList"
              :key="index"
              :label="item.name"
              :value="item.poolId"
            />
          </el-select>
        </el-form-item>
        <!-- <el-form-item label="实例"
                        prop="type">
            <el-select v-model="form.type"
                       placeholder="请选择实例">
              <el-option label="全部"
                         value="0" />
              <el-option label="实例"
                         value="1" />
              <el-option label="裸金属"
                         value="2" />
            </el-select>
          </el-form-item> -->
      </div>
      <!-- </el-card>

      <el-card class="!border-none mb-3"> -->
      <div class="text item text-right">
        <el-button type="primary" size="small" @click="toBmsEdit()">{{
          $t("form.update")
        }}</el-button>
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
      storagePoolList: [],
      form: {
        name: "",
        description: "",
        size: 1,
        storagePoolId: 0,
      },
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        storagePoolId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectCloudDiskType"),
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
          this.getStoragePools();
          this.getDetail();
        }
      },
      immediate: true,
    },
  },

  created() {
    this.getStoragePools();
    this.getDetail(); // 获取详情
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
            .volumesEdit(this.form, this.id)
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
        .volumesDetail(this.id)
        .then((res) => {
          this.form.name = res.name;
          this.form.description = res.description;
          this.form.size = res.size;
          this.form.storagePoolId = res.storagePoolId;
        })
        .catch((error) => {});
    },
    getStoragePools() {
      mainApi.storagePoolsList().then((res) => {
        this.storagePoolList = res.storagePools;
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
