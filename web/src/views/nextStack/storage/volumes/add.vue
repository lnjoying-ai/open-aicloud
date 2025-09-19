<template>
  <div class="mt-2 relative" style="height: calc(100vh - 144px - 0.5rem)">
    <div
      id="vm_screen"
      class="mb-2 p-2 overflow-auto bg-white"
      shadow="never"
      style="height: calc((100vh - 185px) - 0.5rem)"
    >
      <el-row :gutter="20" class="m-0" style="min-height: 100vh">
        <el-col :span="18">
          <div class="mx-auto" style="max-width: 800px">
            <el-form
              ref="addVmForm"
              class="pb-10 addVmFormStyle mt-8"
              :size="$store.state.nextStack.viewSize.main"
              :model="form"
              :rules="rules"
              label-position="right"
              label-width="120px"
              :element-loading-text="$t('domain.loading')"
            >
              <el-form-item
                :label="$t('form.cloudDiskType') + ':'"
                prop="storagePoolId"
              >
                <!-- <el-select v-model="form.storagePoolId" class="w-60" placeholder="请选择云盘类型">
                  <el-option :label="item.name" :value="item.poolId" v-for="(item, index) in storagePoolList"
                    :key="index" />
                </el-select> -->
                <div>
                  <span v-if="form.storagePoolId" class="text-sm"
                    >{{ $t("form.currentSelected") }}
                    {{ getstoragePoolName(form.storagePoolId) }} | {{ form.size
                    }}{{ $t("unit.disk") }}</span
                  >
                </div>
                <ul class="overflow-hidden" style="max-width: 720px">
                  <li
                    v-for="(item, index) in storagePoolList"
                    :key="index"
                    class="float-left mb-2"
                    style="width: 180px"
                    @click="changeStoragePool(item)"
                  >
                    <div
                      class="border rounded leading-6 cursor-pointer"
                      style="padding: 10px 18px; margin: 0 5px"
                      :class="
                        form.storagePoolId == item.poolId
                          ? '  bg-blue-100 font-bold text-blue-500 border-blue-500'
                          : ''
                      "
                    >
                      <div>
                        <h5 class="truncate text-base">{{ item.name }}</h5>
                        <p
                          class="text-gray-500 text-sm leading-5 h-20 mt-2 break-all truncate whitespace-normal"
                          style="
                            -webkit-line-clamp: 4;
                            -webkit-box-orient: vertical;
                            display: -webkit-box;
                          "
                        >
                          {{ item.description }}
                        </p>
                      </div>
                    </div>
                  </li>
                </ul>
              </el-form-item>
              <el-form-item :label="$t('form.capacity') + ':'" prop="size">
                <el-input-number
                  v-model="form.size"
                  :min="10"
                  :max="5000"
                  :step="10"
                  :precision="0"
                  controls-position="right"
                  class="float-left w-60"
                  style="width: 215px"
                />
                <span class="ml-2">{{ $t("unit.disk") }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.name') + ':'" prop="name">
                <el-input
                  v-model="form.name"
                  class="w-60"
                  :placeholder="$t('form.pleaseInputName')"
                />
              </el-form-item>
              <el-form-item
                :label="$t('form.description') + ':'"
                prop="description"
              >
                <el-input
                  v-model="form.description"
                  class="w-60"
                  maxlength="255"
                  show-word-limit
                  type="textarea"
                  :rows="2"
                  :placeholder="$t('form.pleaseInputDesc')"
                />
              </el-form-item>
            </el-form>
          </div>
        </el-col>
        <el-col
          :span="6"
          class="h-full p-0 absolute"
          style="top: 0; right: 0; height: calc(100% - 2px)"
        >
          <div class="bg-slate-100 h-full p-6">
            <h3 class="text-md mt-2 mb-4">{{ $t("form.preview") }}</h3>
            <el-form
              :size="$store.state.nextStack.viewSize.main"
              label-width="100px"
              label-position="left"
            >
              <el-form-item :label="$t('form.cloudDiskType') + ':'">
                <span v-if="form.storagePoolId">
                  {{
                    storagePoolList.filter((e) => {
                      return form.storagePoolId == e.poolId;
                    })[0].name
                  }}</span
                >
              </el-form-item>
              <el-form-item :label="$t('form.capacity') + ':'">
                <span v-if="form.size"
                  >{{ form.size }}{{ $t("unit.disk") }}</span
                >
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
    </div>
    <div
      class="absolute z-30 px-3 py-2 bg-white"
      style="
        bottom: -15px;
        right: -10px;
        left: -9px;
        border-top: 1px solid rgba(0, 0, 0, 0.09);
        box-shadow: 0 2px 30px 0 rgba(0, 0, 0, 0.09);
      "
    >
      <div class="text-right">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toDiskAdd()"
          >{{ $t("form.createImmediately") }}</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import mainApi from "@/api/nextStack/mainApi";
export default {
  data() {
    return {
      loading: false,
      storagePoolList: [],
      form: {
        name: "",
        description: "",
        size: 10,
        storagePoolId: "",
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
            message: this.$t("message.pleaseSelectDiskType"),
            trigger: "change",
          },
        ],
      },
    };
  },
  watch: {},

  created() {
    this.getStoragePools();
  },
  mounted() {},

  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    changeStoragePool(item) {
      this.form.storagePoolId = item.poolId;
    },
    getstoragePoolName(id) {
      return this.storagePoolList.filter((e) => {
        return id == e.poolId;
      })[0].name;
    },

    resetForm() {
      // 重置
      this.$refs.addVmForm.resetFields();
    },
    toDiskAdd() {
      this.loading = true;

      const newdata = {
        description: this.form.description,
        name: this.form.name,
        size: this.form.size,
        storagePoolId: this.form.storagePoolId,
      };
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .volumesAdd(newdata)
            .then((res) => {
              this.loading = false;
              this.$router.push("/nextStack/volumes");
              this.$notify({
                title: this.$t("message.startCreate"),
                type: "success",
                duration: 2500,
              });
              this.resetForm();
              this.$router.push(-1);
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    getStoragePools() {
      mainApi.storagePoolsList().then((res) => {
        this.storagePoolList = res.storagePools ? res.storagePools : [];
        if (res.storagePools && res.storagePools.length > 0) {
          this.form.storagePoolId = res.storagePools[0].poolId;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
