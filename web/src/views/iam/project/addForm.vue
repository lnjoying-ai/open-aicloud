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
        <el-form-item :label="$t('form.projectName') + ':'" prop="display_name">
          <el-input
            v-model="form.display_name"
            :placeholder="$t('form.pleaseInputProjectName')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.projectDescription') + ':'"
          prop="description"
        >
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputProjectDescription')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.projectStatus') + ':'">
          <el-select
            v-model="form.enable"
            :placeholder="$t('form.pleaseSelect')"
            style="width: 100%"
          >
            <el-option :value="1" :label="$t('form.enable')" />
            <el-option :value="-1" :label="$t('form.disable')" />
          </el-select>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="mb-2" shadow="never">
      <div slot="header">
        <el-collapse v-model="activeNames">
          <el-collapse-item :title="getTitle()" name="1">
            <el-tag
              v-for="(items, index) in multipleSelection"
              :key="index"
              effect="plain"
              closable
              clsss="tags"
              style="margin: 5px 5px 0px 0px"
              @close="handleTagClose(items, index)"
              >{{ items.name }}</el-tag
            >
          </el-collapse-item>
        </el-collapse>
      </div>
      <el-row :gutter="40">
        <el-col :span="24">
          <el-form :model="queryData" label-width="80px" size="small">
            <el-form-item
              :label="$t('form.userName') + ':'"
              style="float: left"
            >
              <el-input
                v-model="queryData.name"
                size="small"
                style="width: 300px"
              />
              <el-button
                type="primary"
                size="small"
                style="margin-left: 10px"
                @click="initUser()"
                >{{ $t("form.query") }}</el-button
              >
              <el-button size="small" @click="resetUser()">{{
                $t("form.reset")
              }}</el-button>
            </el-form-item>
          </el-form>
          <el-table
            ref="multipleTable"
            :data="tableData.users"
            stripe
            tooltip-effect="dark"
            style="width: 100%"
            :default-sort="{ prop: 'date', order: 'descending' }"
            :row-key="getRowKeys"
            max-height="530"
            @expand-change="expandChange"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="expand">
              <template slot-scope="props">
                <empowerVue
                  ref="empowerVue"
                  :sup_this="sup_this"
                  :is-add="true"
                  @getlocalStorage="getlocalStorage"
                />
              </template>
            </el-table-column>
            <el-table-column type="selection" width="55" />
            <el-table-column prop="name" :label="$t('form.name')">
              <template slot-scope="scope">
                <router-link
                  style="color: #409eff"
                  :to="'/iam/user/detail/' + scope.row.id"
                >
                  {{ scope.row.name }}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="contact_info" :label="$t('form.email')">
              <template slot-scope="scope">
                <div v-if="scope.row.contact_info" class=" ">
                  {{
                    scope.row.contact_info.email
                      ? scope.row.contact_info.email
                      : "-"
                  }}
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="bp_name"
              :label="$t('form.belongOrganization')"
            >
              <template slot-scope="scope">
                {{ scope.row.bp_name ? scope.row.bp_name : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              prop="create_time"
              :label="$t('form.createTime')"
              sortable
            />
            <el-table-column
              prop="update_time"
              :label="$t('form.updateTime')"
              sortable
            >
              <template slot-scope="scope">
                {{ scope.row.update_time ? scope.row.update_time : "-" }}
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="mb-2">
      <div class="flex justify-center px-4">
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
import { addProject, addUserToProject } from "@/api/iam/project";
import { attachUserAuthz } from "@/api/iam/authz";
import { getUsers } from "@/api/iam/user";
import empowerVue from "./components/empower.vue";
export default {
  components: {
    empowerVue,
  },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    return {
      id: "",
      loading: false,
      selNumber: 0,
      user_ids: [],
      tableData: [],
      activeNames: ["1"],
      multipleSelection: [],
      localStorageData: [],
      form: {
        name: "",
        display_name: "",
        parent_id: "",
        enable: 1,
        description: "",
      },
      queryData: {
        name: "",
      },
      authzForm: {
        project_id: "",
        project_name: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      },
      total_num: 0,
      rules: {
        display_name: [
          {
            required: true,
            message: this.$t("message.pleaseInputProjectName"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
    multipleSelection: {
      deep: true,
      handler(val) {
        this.user_ids = [];
        val.forEach((element) => {
          this.user_ids.push(element.id);
        });
        this.selNumber = val.length;
      },
    },
  },
  async created() {
    this.initUser();
  },
  mounted() {
    // window.localStorage.clear();
  },
  methods: {
    getTitle() {
      return (
        this.$t("form.empowerUser") +
        "(" +
        this.selNumber +
        "/" +
        this.total_num +
        ")"
      );
    },
    async initUser() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getUsers(this.queryData);
      this.tableData = list;
      this.total_num = list.total_num;
    },
    // 重置表格
    resetUser() {
      (this.queryData = { name: "" }), this.initUser();
    },
    getlocalStorage(user_id, policy_ids, role_ids) {
      window.localStorage.setItem(
        user_id,
        JSON.stringify({
          user_id: user_id,
          policys: policy_ids,
          roles: role_ids,
        })
      );
    },
    // 异步加载数据
    expandChange(row) {
      this.$nextTick(() => {
        if (this.$refs.empowerVue) {
          this.$refs.empowerVue.user_id = row.id;
          this.$refs.empowerVue.initData();
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 关闭tag调用
    handleTagClose(item, index) {
      this.multipleSelection.splice(index, 1);
      this.$refs.multipleTable.toggleRowSelection(item, false);
    },
    // 设置keys
    getRowKeys(row) {
      return row.id;
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
    cancel() {
      this.resetAuthorzForm();
      this.resetForm();
      this.$router.push({
        path: "/iam/project",
      });
    },
    doAdd() {
      this.form.parent_id = this.$route.query.id;
      var data = JSON.parse(JSON.stringify(this.form));
      addProject(data)
        .then((res) => {
          this.id = res;
          this.user_ids.length > 0 ? this.bindUser() : "";
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$router.push({
            path: "/iam/project",
          });
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    // 授权用户
    async bindUser() {
      await addUserToProject(this.id, { user_ids: this.user_ids })
        .then((res) => {
          this.$notify({
            title: this.$t("message.authorizeSuccess"),
            type: "success",
            duration: 2500,
          });
          this.saveAuthz();
        })
        .catch((err) => {
          return;
        });
    },
    // 用户授权策略角色
    async saveAuthz() {
      for (var i = 0; i < this.user_ids.length; i++) {
        this.authzForm.user_id = this.user_ids[i];
        this.authzForm.project_id = this.id;
        this.authzForm.project_name = this.form.display_name;
        var user_id = JSON.parse(window.localStorage.getItem(this.user_ids[i]));
        if (user_id) {
          this.authzForm.policy_ids = user_id.policys.map((element) => {
            return element.id;
          });
          this.authzForm.role_ids = user_id.roles.map((element) => {
            return element.role_id;
          });
        }
        var data = this.authzForm.policy_ids.concat(this.authzForm.role_ids);
        if (data.length > 0) {
          await attachUserAuthz(this.authzForm)
            .then((res) => {})
            .catch((err) => {});
        }
      }
      this.authzForm = {
        project_id: "",
        project_name: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      };
      this.resetForm();
    },
    // 重置授权列表
    resetAuthorzForm() {
      this.authzForm = {
        project_id: "",
        project_name: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      };
      this.$refs.multipleTable.clearSelection();
    },
    resetForm() {
      this.form = {
        name: "",
        display_name: "",
        parent_id: "",
        enable: 1,
        description: "",
      };
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-table__expanded-cell[class*="cell"] {
  padding: 20px 10px;
  background-color: #c2c9d03d !important;
}

::v-deep .el-table__expanded-cell:hover {
  background-color: #c2c9d03d !important;
}

::v-deep .el-collapse-item__header {
  border: none;
  font-size: 16px;
}

::v-deep .el-collapse-item__wrap {
  border: none;
}

::v-deep .el-collapse-item__content {
  padding-bottom: 20px;
}

::v-deep .el-collapse {
  border: none;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}
</style>
