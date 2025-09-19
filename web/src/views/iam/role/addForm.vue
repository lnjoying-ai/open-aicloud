<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="108px"
      >
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            :placeholder="$t('form.pleaseInputRoleName')"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'">
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputRoleDescription')"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.roleType') + ':'"
          prop="role_type"
        >
          <el-select
            v-model="form.role_type"
            :placeholder="$t('form.pleaseSelectRoleType')"
            style="width: 100%"
          >
            <el-option label="system" :value="1" />
            <el-option label="custom" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label-width="120px">
          <span slot="label">
            <span class="span-box">
              <span>{{ $t("form.belongProject") }}</span>
              <el-tooltip
                style=""
                effect="dark"
                :content="$t('form.belongProjectTips')"
                placement="top"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </span>
          </span>
          <el-cascader
            v-model="form.project_id"
            :placeholder="$t('form.pleaseSelectBelongProject')"
            :options="projectData"
            :show-all-levels="false"
            :props="{
              value: 'id',
              label: 'display_name',
              children: 'children',
              expandTrigger: 'hover',
              checkStrictly: true,
              emitPath: false,
            }"
            filterable
            clearable
            style="width: 100.7%; margin-left: -10px"
          >
            <template slot-scope="{ node, data }">
              <span>{{ data.display_name }}</span>
            </template>
          </el-cascader>
        </el-form-item>
        <el-form-item :label="$t('form.serviceCode') + ':'">
          <el-select
            v-model="form.iam_code"
            :placeholder="$t('form.pleaseSelectServiceCode')"
            style="width: 100%"
            filterable
          >
            <el-option
              v-for="item in serveData"
              :key="item.id"
              :label="item.name"
              :value="item.iam_code"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{
                item.description
              }}</span>
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="mb-2" shadow="never">
      <empowerVue ref="empowerVue" :sup_this="sup_this" :policie-i-ds="[]" />
    </el-card>
    <el-card class="mb-2">
      <div class="flex justify-center px-4">
        <el-button type="text" size="small" @click="cancel">
          {{ $t("form.cancel") }}
        </el-button>
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
import { addRoles } from "@/api/iam/role";
import { mapGetters } from "vuex";
import { getService } from "@/api/iam/serve";
import { getProject } from "@/api/iam/project";
import empowerVue from "./components/empower.vue";
export default {
  data() {
    return {
      loading: false,
      sup_this: this,
      form: {
        name: "",
        description: "",
        iam_code: "",
        role_type: 2,
        project_id: "",
      },
      serveData: [],
      projectData: [],
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputRoleName"),
            trigger: "blur",
          },
          {
            pattern: /^[a-zA-Z0-9_-]{1,64}$/,
            message: this.$t("form.roleNameTips"),
            trigger: "blur",
          },
        ],
        role_type: [
          {
            required: true,
            message: this.$t("form.pleaseSelectRoleType"),
            trigger: "change",
          },
        ],
        project_id: [
          {
            required: true,
            message: this.$t("form.pleaseSelectBelongProject"),
            trigger: "change",
          },
        ],
      },
    };
  },
  created() {
    this.serviceinit();
    this.projectinit();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  components: { empowerVue },
  mounted() {
    this.$nextTick(() => {
      this.$refs.empowerVue.initPolicie();
    });
  },
  methods: {
    // 获取服务数据
    async serviceinit() {
      const list = await getService();
      this.serveData = list.services;
    },
    async projectinit() {
      const list = await getProject();
      this.projectData = list.projects;
      this.projectData = this.getTreeData(this.projectData);
    },
    getTreeData(data) {
      for (let i = 0; i < data.length; i++) {
        data[i].display_name = data[i].nodeData.display_name || data[i].name;
        if (data[i].children.length < 1) {
          // 最后一级没有数据将children变成undefined
          data[i].children = undefined;
        } else {
          // children不为空时继续调用该方法
          this.getTreeData(data[i].children);
        }
      }
      return data;
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
      addRoles(data)
        .then((res) => {
          this.resetForm();
          this.$refs.empowerVue.AuthorzRoles(res, data.project_id);
          this.loading = false;
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.$router.push({
            path: "/iam/role",
          });
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.form = {
        name: "",
        description: "",
        iam_code: "",
        role_type: 2,
        project_id: "",
      };
    },
  },
};
</script>

<style scoped></style>
