<template>
  <div class="sgsAddPage h-full">
    <el-dialog
      :visible.sync="addStatus"
      :title="
        addType == 'edit'
          ? $t('form.editSecurityGroup')
          : $t('form.createSecurityGroup')
      "
      width="600px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <el-form
        ref="addSecGroupsForm"
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
          <el-form-item
            :label="$t('form.description') + ':'"
            prop="description"
          >
            <el-input
              v-model="form.description"
              class="w-96"
              maxlength="255"
              show-word-limit
              type="textarea"
              :rows="4"
              :placeholder="$t('form.pleaseInputDescription')"
            />
          </el-form-item>
        </div>

        <div class="text item text-right pt-5">
          <el-button
            v-if="addType == 'edit'"
            type="primary"
            size="small"
            :loading="loading"
            @click="toSgsUpdate"
            >{{ $t("form.update") }}</el-button
          >
          <el-button
            v-else
            type="primary"
            size="small"
            :loading="loading"
            @click="toSgsAdd"
            >{{ $t("button.createImmediately") }}</el-button
          >
        </div>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";

export default {
  props: {
    addStatus: {
      type: Boolean,
      default: false,
    },
    addType: {
      type: String,
      default: "add",
    },
    addForm: {
      type: Object,
      default: {
        name: "",
        description: "",
      },
    },
    sgId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      loading: false,
      form: {
        name: "sg-" + this.$script.createRandomStr(4, "Aa0"), // 安全组名称
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
    //  监听props的变化
    // props: {
    //   handler (val) {
    //     console.log(val);
    //     if (val.addType == "edit") {
    //       this.sgId = val.sgId;
    //       this.form.name = val.addForm.name;
    //       this.form.description = val.addForm.description;
    //     } else {
    //       this.form.name = "sg-" + this.$script.createRandomStr(4, "Aa0");
    //     }
    //   },
    //   immediate: true,
    // },
    addStatus: {
      handler(val) {
        console.log(val);
        console.log(this.$props);
        if (val) {
          if (this.addType == "edit") {
            this.sgId = this.$props.sgId;
            this.form.name = this.$props.addForm.name;
            this.form.description = this.$props.addForm.description;
          } else {
            this.form.name = "sg-" + this.$script.createRandomStr(4, "Aa0");
          }
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
    handleClose() {
      this.$emit("closeAdd");
      this.resetForm();
    },
    resetForm() {
      // 重置
      this.$refs.addSecGroupsForm.resetFields();
    },
    toSgsAdd() {
      // 安全组add
      this.loading = true;
      this.$refs.addSecGroupsForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .sgsAdd(this.form)
            .then((res) => {
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.loading = false;
              this.handleClose();
              this.$emit("getSgsList");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    toSgsUpdate() {
      // 安全组add
      this.loading = true;
      mainApi
        .sgsEdit(this.form, this.sgId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.updateSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.handleClose();
          this.$emit("getSgsList");
        })
        .catch((error) => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.sgsAddPage {
}
</style>
