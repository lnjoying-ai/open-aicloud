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
              label-width="80px"
              :element-loading-text="$t('domain.loading')"
            >
              <el-form-item :label="$t('form.eipType') + ':'" prop="eipPoolId">
                <div>
                  <span v-if="form.eipPoolId" class="text-sm"
                    >{{ $t("form.currentSelected") + ":" }}
                    {{ getstoragePoolName(form.eipPoolId) }}</span
                  >
                </div>
                <ul class="overflow-hidden" style="max-width: 720px">
                  <li
                    v-for="(item, index) in eipPoolList"
                    :key="index"
                    class="float-left mb-2"
                    style="width: 180px"
                    @click="changeEipPool(item)"
                  >
                    <div
                      class="border rounded leading-6 cursor-pointer"
                      style="padding: 10px 18px; margin: 0 5px"
                      :class="
                        form.eipPoolId == item.poolId
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
              label-width="70px"
              label-position="left"
            >
              <el-form-item class="mb-0" :label="$t('form.eipType') + ':'">
                <span v-if="form.eipPoolId">
                  {{
                    eipPoolList.filter((e) => {
                      return form.eipPoolId == e.poolId;
                    })[0].name
                  }}</span
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
          @click="toEipAdd()"
          >{{ $t("form.applyNow") }}</el-button
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
      eipPoolList: [],

      form: {
        eipPoolId: "",
        startIpAddress: "",
        endIpAddress: "",
        addressType: 0, // 0：IPV4 1：IPV6
      },
      rules: {
        eipPoolId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectEipType"),
            trigger: "change",
          },
        ],
      },
    };
  },
  watch: {},

  created() {
    this.getEipPoolList();
  },
  mounted() {},

  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    changeEipPool(item) {
      this.form.eipPoolId = item.poolId;
    },
    getstoragePoolName(id) {
      return this.eipPoolList.filter((e) => {
        return id == e.poolId;
      })[0].name;
    },

    resetForm() {
      // 重置
      this.$refs.addVmForm.resetFields();
    },
    toEipAdd() {
      this.loading = true;

      const newdata = {
        poolId: this.form.eipPoolId,
      };
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .applyforeip(newdata)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.applyEipSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$router.push("/nextStack/eip");

              this.resetForm();
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    getEipPoolList() {
      mainApi.eipPoolsList().then((res) => {
        this.eipPoolList = res.eipPools ? res.eipPools : [];
        if (res.eipPools && res.eipPools.length > 0) {
          this.form.eipPoolId = res.eipPools[0].poolId;
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
