<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="120px"
      >
        <el-form-item :label="$t('form.serveName') + ':'" prop="display_name">
          <el-input
            v-model="form.display_name"
            :placeholder="$t('form.pleaseInputServeName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.serveCode') + ':'" prop="iam_code">
          <el-input
            v-model="form.iam_code"
            :placeholder="$t('form.pleaseInputServeCode')"
          />
        </el-form-item>
        <!-- <el-form-item label="所属服务">
          <el-cascader v-model="form.parent_id" placeholder="请选择所属服务" :options="tableData" :show-all-levels="false"
            :props="{ value: 'id', label: 'name', children: 'children', expandTrigger: 'hover', checkStrictly: true, emitPath: false }"
            filterable clearable style="width:100%;"></el-cascader>
        </el-form-item> -->
        <el-form-item :label="$t('form.serveStatus') + ':'" prop="enable">
          <el-select
            v-model="form.enable"
            :placeholder="$t('form.pleaseSelectEnable')"
            style="width: 100%"
          >
            <el-option :value="1" :label="$t('form.enable')" />
            <el-option :value="-1" :label="$t('form.disable')" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputServeDescription')"
          />
        </el-form-item>
      </el-form>
      <div class="flex justify-end mt-2 px-4">
        <el-button type="text" size="small" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-card>
  </div>
</template>

<script>
import { getService, addService } from "@/api/iam/serve";
export default {
  data() {
    return {
      loading: false,
      tableData: [],
      form: {
        name: "",
        display_name: "",
        iam_code: "",
        parent_id: "",
        enable: 1,
        description: "",
      },
      rules: {
        display_name: [
          {
            required: true,
            message: this.$t("form.pleaseInputServeName"),
            trigger: "blur",
          },
        ],
        iam_code: [
          {
            required: true,
            message: this.$t("form.pleaseInputServeCode"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  async created() {
    this.getServiceList();
  },
  methods: {
    async getServiceList() {
      const list = await getService({
        page_size: 99999,
        page_num: 1,
      });
      this.tableData = list.services;
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          this.doAdd();
        } else {
          return false;
        }
      });
    },
    doAdd() {
      var data = JSON.parse(JSON.stringify(this.form));
      addService(data)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.searchinit();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.form = {
        name: "",
        display_name: "",
        iam_code: "",
        parent_id: "",
        enable: 1,
        description: "",
      };
      this.$router.push({
        path: "/iam/serve",
      });
    },
  },
};
</script>

<style scoped></style>
