<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="140px"
      >
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            :placeholder="$t('form.pleaseInputResourceName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputResourceDescription')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.resourceService') + ':'"
          prop="service_id"
        >
          <el-cascader
            v-model="form.service_id"
            :placeholder="$t('form.pleaseSelectResourceService')"
            :options="tableData"
            :show-all-levels="false"
            :props="{
              value: 'id',
              label: 'name',
              children: 'children',
              expandTrigger: 'hover',
              checkStrictly: true,
              emitPath: false,
            }"
            filterable
            clearable
            style="width: 100%"
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
import { addResource } from "@/api/iam/resource";
import { getService } from "@/api/iam/serve";
export default {
  data() {
    return {
      loading: false,
      form: {
        name: "",
        resource_type: "",
        lrn: "",
        service_id: "",
        description: "",
        actions: [],
        conditions: [],
        attr: [],
      },
      // 服务列表
      tableData: [],
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputResourceName"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  mounted() {
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
      addResource(data)
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
      this.form = {
        name: "",
        resource_type: "",
        lrn: "",
        service_id: "",
        description: "",
        actions: [],
        conditions: [],
        attr: [],
      };
      this.$router.push({
        path: "/iam/resource",
      });
    },
  },
};
</script>

<style scoped></style>
