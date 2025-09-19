<template>
  <div class="flavorsAddPage h-full">
    <el-form
      ref="addVmForm"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="120px"
    >
      <div v-if="maxInfo" class="text item">
        <el-form-item :label="$t('form.type') + ':'" prop="type">
          <el-radio-group v-model="form.addType">
            <el-radio-button label="info" value="info">
              <span style="padding: 9px 14px">{{
                $t("form.generalCompute")
              }}</span></el-radio-button
            >

            <el-radio-button label="gpu" value="gpu"
              ><span style="padding: 9px 14px">{{
                $t("form.gpuCompute")
              }}</span></el-radio-button
            >
          </el-radio-group>
        </el-form-item>
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            class="w-60"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>

        <el-form-item :label="$t('form.cpu') + ':'" prop="cpu">
          <el-input-number
            v-model="form.cpu"
            :min="0"
            :step="1"
            step-strictly
            :max="maxInfo.maxVcpu || ''"
            controls-position="right"
            class="w-60"
          />
          <span class="pl-2">{{ $t("unit.cpu") }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.mem') + ':'" prop="mem">
          <el-input-number
            v-model="form.mem"
            :min="0"
            :step="1"
            step-strictly
            :max="maxInfo.maxMemory || ''"
            controls-position="right"
            class="w-60"
          />
          <span class="pl-2">{{ $t("unit.mem") }}</span>
        </el-form-item>
        <!-- <el-form-item label="根盘" prop="rootDisk">
          <el-input-number
            v-model="form.rootDisk"
            :min="0"
            :step="1"
            step-strictly
            :max="maxInfo.maxRootDisk || ''"
            controls-position="right"
            class="w-60"
          ></el-input-number>
          <span class="pl-2">GB</span>
        </el-form-item> -->
        <el-form-item
          v-if="form.addType == 'gpu'"
          :label="$t('form.gpuType') + ':'"
          prop="gpuName"
        >
          <el-select
            v-model="form.gpuName"
            class="w-60"
            :placeholder="$t('form.pleaseSelectType')"
            @change="formGpuNameChange"
          >
            <el-option
              v-for="(item, index) in GPUInfo"
              :key="index"
              :label="item.gpuName"
              :value="item.gpuName"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="form.addType == 'gpu'"
          :label="$t('form.gpuCount') + ':'"
          prop="gpuCount"
        >
          <el-input-number
            v-model="form.gpuCount"
            class="w-60"
            :min="0"
            :step="1"
            :disabled="form.gpuName == ''"
            step-strictly
            :max="
              form.gpuName
                ? GPUInfo.filter((item) => item.gpuName == form.gpuName)[0]
                    .gpuCount
                : 0
            "
            controls-position="right"
          />
          <span v-if="form.gpuName" class="pl-2">
            {{
              $t("message.availableGPUCount", {
                count: GPUInfo.filter((item) => item.gpuName == form.gpuName)[0]
                  .gpuCount,
              })
            }}
          </span>
        </el-form-item>
        <el-form-item
          v-if="form.addType == 'gpu' && form.gpuName && isshowneedIb"
          :label="$t('form.ib') + ':'"
          prop="needIb"
        >
          <el-checkbox v-model="form.needIb" :disabled="needIbDisabled">{{
            $t("form.needIb")
          }}</el-checkbox>
        </el-form-item>
      </div>
      <div class="text item text-right mt-4">
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toFlavorssAdd()"
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
      needIbDisabled: false,
      form: {
        addType: "info",
        name: "",
        type: "1",
        cpu: 0,
        mem: 0,
        // rootDisk: 0,
        gpuCount: 0,
        gpuName: "",
        needIb: false,
      },
      maxInfo: {},
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        type: [
          {
            required: true,
            message: this.$t("message.pleaseSelectType"),
            trigger: "change",
          },
        ],
        // cpu最大值和最小值限制
        cpu: [
          {
            required: true,
            message: this.$t("message.pleaseInputCPU"),
            trigger: "change",
          },
          {
            validator: (rule, value, callback) => {
              if (value > this.maxInfo.maxVcpu) {
                callback(
                  new Error(
                    this.$t("message.CPUMaxValue", {
                      count: this.maxInfo.maxVcpu,
                    })
                  )
                );
              } else if (value < 1) {
                callback(new Error(this.$t("message.CPUCannotBe0")));
              } else {
                callback();
              }
            },
            trigger: "change",
          },
        ],
        // 内存最大值和最小值限制
        mem: [
          {
            required: true,
            message: this.$t("message.pleaseInputMemory"),
            trigger: "change",
          },
          {
            validator: (rule, value, callback) => {
              if (value > this.maxInfo.maxMemory) {
                callback(
                  new Error(
                    this.$t("message.memoryMaxValue", {
                      count: this.maxInfo.maxMemory,
                    })
                  )
                );
              } else if (value < 1) {
                callback(new Error(this.$t("message.memoryCannotBe0")));
              } else {
                callback();
              }
            },
            trigger: "change",
          },
        ],
        // 根盘最大值和最小值限制
        // rootDisk: [
        //   { required: true, message: "请输入根盘", trigger: "change" },
        //   {
        //     validator: (rule, value, callback) => {
        //       if (value > this.maxInfo.maxRootDisk) {
        //         callback(new Error("根盘最大值为" + this.maxInfo.maxRootDisk));
        //       } else if (value < 20) {
        //         callback(new Error("根盘不可小于20GB"));
        //       } else {
        //         callback();
        //       }
        //     },
        //     trigger: "change",
        //   },
        // ],
        gpuName: [
          {
            required: true,
            message: this.$t("message.pleaseSelectGPUType"),
            trigger: "change",
          },
        ],
        gpuCount: [
          {
            required: true,
            message: this.$t("message.pleaseInputGPUCount"),
            trigger: "change",
          },
          {
            validator: (rule, value, callback) => {
              if (this.form.gpuName == "") {
                callback(new Error(this.$t("message.pleaseSelectGPUType")));
              } else if (
                value >
                this.GPUInfo.filter(
                  (item) => item.gpuName == this.form.gpuName
                )[0].gpuCount
              ) {
                callback(
                  new Error(
                    this.$t("message.GPUCountMaxValue", {
                      count: this.GPUInfo.filter(
                        (item) => item.gpuName == this.form.gpuName
                      )[0].gpuCount,
                    })
                  )
                );
              } else if (value < 1) {
                callback(new Error(this.$t("message.GPUCountCannotBe0")));
              } else {
                callback();
              }
            },
            trigger: "change",
          },
        ],
      },
      isshowneedIb: true,
    };
  },
  created() {
    this.getflavorsMaxInfo();
    this.getflavorGPUInfo();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  watch: {
    "form.gpuCount"(val) {
      if (
        val > 1 &&
        (this.form.gpuName.includes("H800") ||
          this.form.gpuName.includes("H100") ||
          this.form.gpuName.includes("A100") ||
          this.form.gpuName.includes("A800"))
      ) {
        this.form.needIb = true;
        this.needIbDisabled = true;
      } else {
        this.needIbDisabled = false;
      }
    },
    "form.gpuName": function (newValue, oldValue) {
      if (
        newValue.includes("H800") ||
        newValue.includes("H100") ||
        newValue.includes("A100") ||
        newValue.includes("A800")
      ) {
        this.isshowneedIb = true;
      } else {
        this.isshowneedIb = false;
        this.form.needIb = false;
      }
    },
  },

  methods: {
    formGpuNameChange() {
      this.form.gpuCount = 0;
    },
    resetForm() {
      // 重置
      this.form.addType = "info";
      this.$refs.addVmForm.resetFields();
    },
    toFlavorssAdd() {
      // 规格add
      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          var data = {};
          if (this.form.addType == "info") {
            data = {
              name: this.form.name,
              type: this.form.type,
              cpu: this.form.cpu,
              mem: this.form.mem,
              // rootDisk: this.form.rootDisk,
              gpuName: "",
              gpuCount: 0,
            };
          } else {
            data = {
              name: this.form.name,
              type: this.form.type,
              cpu: this.form.cpu,
              mem: this.form.mem,
              // rootDisk: this.form.rootDisk,
              gpuName: this.form.gpuName,
              gpuCount: this.form.gpuCount,
              needIb: this.form.needIb,
            };
          }
          mainApi
            .flavorsAdd(data)
            .then((res) => {
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.loading = false;
              this.$emit("close");
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
    getflavorsMaxInfo() {
      // 规格限制

      mainApi
        .flavorsMaxInfo()
        .then((res) => {
          this.maxInfo = res;
        })
        .catch((error) => {});
    },
    getflavorGPUInfo() {
      // GPU型号
      mainApi
        .flavorGPUInfo()
        .then((res) => {
          this.GPUInfo = res;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped></style>
