<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="detailMain"
        :rules="rules"
        size="small"
        label-width="108px"
      >
        <el-form-item :label="$t('form.projectName') + ':'" prop="display_name">
          <el-input
            v-model="detailMain.display_name"
            :placeholder="$t('form.pleaseInputProjectName')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.projectDescription') + ':'"
          prop="description"
        >
          <el-input
            v-model="detailMain.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputProjectDescription')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.projectStatus') + ':'">
          <el-select
            v-model="detailMain.enable"
            :placeholder="$t('form.pleaseSelect')"
            style="width: 100%"
          >
            <el-option :value="1" :label="$t('form.enable')" />
            <el-option :value="-1" :label="$t('form.disable')" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.parentName') + ':'">
          <span>{{
            detailMain.parent_display_name
              ? detailMain.parent_display_name
              : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.createTime') + ':'">
          <span>{{
            detailMain.create_time ? detailMain.create_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.modifyTime') + ':'">
          <span>{{
            detailMain.update_time ? detailMain.update_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button
            :loading="loadingForm"
            type="primary"
            size="small"
            @click="doEdit()"
            >{{ $t("form.save") }}</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="box-card" shadow="never">
      <div
        slot="header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <span>{{ $t("form.empowerEntity") }}</span>
        <el-button type="primary" size="small" @click="saveAuthorize()">{{
          $t("form.addUser")
        }}</el-button>
      </div>
      <el-tabs v-model="activeName">
        <el-tab-pane :label="$t('form.userList')" name="first">
          <el-table
            :data="projectUserData"
            style="width: 100%"
            max-height="530"
            stripe
            tooltip-effect="dark"
          >
            <el-table-column prop="user_name" :label="$t('form.userName')">
              <template slot-scope="scope">
                <router-link
                  style="color: #409eff"
                  :to="'/iam/user/detail/' + scope.row.user_id"
                >
                  {{ scope.row.user_name || "-" }}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="email" :label="$t('form.userEmail')">
              <template slot-scope="scope">
                {{ scope.row.email || "-" }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.operation')" width="100">
              <template slot-scope="scope">
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="$t('form.modifyPolicyAndRole')"
                  placement="top"
                >
                  <el-button
                    type="text"
                    size="small"
                    @click="editAuthorzRole(scope.row)"
                  >
                    <svg
                      t="1681874572500"
                      class="icon"
                      viewBox="0 0 1024 1024"
                      version="1.1"
                      xmlns="http://www.w3.org/2000/svg"
                      p-id="6202"
                      width="16"
                      height="16"
                    >
                      <path
                        d="M512.7 908.2c0 11-9 20-20 20H117.8c-11 0-20-9-20-20v-7.1c0-11 9-20 20-20h374.9c11 0 20 9 20 20v7.1zM382.4 773.7c-7.8 7.8-20.5 7.8-28.3 0l-148-148c-7.8-7.8-7.8-20.5 0-28.3l5.1-5.1c7.8-7.8 20.5-7.8 28.3 0l148 148c7.8 7.8 7.8 20.5 0 28.3l-5.1 5.1z"
                        fill="#2680F0"
                        p-id="6203"
                      />
                      <path
                        d="M527.1 264.6c-7.7-7.8-20.5-7.9-28.3-0.1L183.3 577.3c-7.8 7.7-16.3 22.8-18.9 33.5l-59.5 245c-2.6 10.7 4.1 17.5 14.8 15.1l248.5-55c10.7-2.4 25.9-10.7 33.7-18.4l314.6-313c7.8-7.8 7.8-20.5 0.1-28.3L527.1 264.6zM369.9 757.1c-7.9 7.7-16.2 14.5-18.4 15.1-2.3 0.6-12.9 3.2-23.6 5.7l-147.4 34c-10.7 2.5-17.3-4.2-14.7-14.9L202 648.5c2.6-10.7 5.2-21.1 5.7-23 0.5-2 7.3-10 15-17.8l273-275.1c7.7-7.8 20.4-7.8 28.1 0L647.7 458c7.7 7.8 7.6 20.5-0.3 28.2L369.9 757.1zM678.9 110.7c-7.8-7.8-20.5-7.8-28.2 0l-107 107.7c-7.8 7.8-7.7 20.6 0.1 28.3l192.1 192.1c7.8 7.8 20.5 7.8 28.2 0L871 331.1c7.8-7.8 7.7-20.6-0.1-28.3l-192-192.1zM605 246.2c-7.8-7.8-7.9-20.6-0.3-28.6l43.4-45.4c7.6-8 20.2-8.1 28-0.4l135.1 134.4c7.8 7.8 7.8 20.5 0.1 28.3l-44.5 44.7c-7.8 7.8-20.5 7.8-28.3 0.1L605 246.2z"
                        fill="#2680F0"
                        p-id="6204"
                      />
                    </svg>
                  </el-button>
                </el-tooltip>
                <el-divider direction="vertical" />
                <el-tooltip
                  class="item"
                  effect="dark"
                  :content="$t('form.deleteUser')"
                  placement="top"
                >
                  <el-button
                    type="text"
                    size="small"
                    @click="clickUnbindUserBtn(scope.row)"
                  >
                    <svg
                      t="1681461090035"
                      class="icon"
                      viewBox="0 0 1024 1024"
                      version="1.1"
                      xmlns="http://www.w3.org/2000/svg"
                      p-id="9969"
                      width="16"
                      height="16"
                    >
                      <path
                        d="M396.032 79.296L627.968 944.64l-61.824 16.64-39.488-147.328-59.904 60.032a224 224 0 1 1-316.8-316.8l242.816-242.88-58.56-218.56 61.824-16.512z m15.872 306.432L195.2 602.496a160 160 0 0 0 218.624 233.472l7.68-7.168 86.016-86.144-38.272-142.72-70.4 70.4-45.248-45.184 96.512-96.64-38.208-142.784z m462.08-235.776a224 224 0 0 1 0 316.8l-242.816 242.752-19.136-71.36 216.768-216.64a160 160 0 0 0-218.624-233.472l-7.68 7.168L516.48 281.28 497.28 209.92l59.968-59.968a224 224 0 0 1 316.8 0zM625.216 353.6l45.248 45.248-96.64 96.576-19.072-71.424 70.4-70.4z"
                        fill="#d81e06"
                        p-id="9970"
                      />
                    </svg>
                  </el-button>
                </el-tooltip>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="$t('form.userGroupList')" name="second">
          <el-table
            :data="groupsData"
            style="width: 100%"
            max-height="530"
            stripe
            tooltip-effect="dark"
          >
            <el-table-column
              prop="group_name"
              :label="$t('form.userGroupName')"
            >
              <template slot-scope="scope">
                {{ scope.row.group_name || "-" }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.userAuthorization')"
      width="1200px"
      :before-close="handleCloseRole"
    >
      <el-card shadow="never" style="border: none">
        <div slot="header">
          <el-collapse v-model="activeNames">
            <el-collapse-item :title="getTitle()" name="1">
              <el-tag
                v-for="(items, index) in multipleSelection"
                :key="index"
                effect="plain"
                closable
                style="margin: 5px 5px 0px 0px"
                @close="handleTagClose(items, index)"
                >{{ items.name }}</el-tag
              >
            </el-collapse-item>
          </el-collapse>
        </div>
        <el-row :gutter="40">
          <el-col :span="24">
            <el-form :model="queryData" label-width="70px" size="small">
              <el-form-item
                :label="$t('form.userName') + ':'"
                style="float: left"
              >
                <el-input
                  v-model="queryData.name"
                  size="mini"
                  style="width: 300px"
                />
                <el-button
                  type="primary"
                  size="mini"
                  style="margin-left: 10px"
                  @click="initUser()"
                  >{{ $t("form.query") }}</el-button
                >
                <el-button size="mini" @click="resetForm()">{{
                  $t("form.reset")
                }}</el-button>
              </el-form-item>
            </el-form>
            <el-table
              ref="multipleTable"
              :data="tableData"
              stripe
              tooltip-effect="dark"
              style="width: 100%"
              :default-sort="{ prop: 'date', order: 'descending' }"
              max-height="530"
              @expand-change="expandChange"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="expand">
                <template slot-scope="props">
                  <empowerVue
                    ref="empowerUser"
                    :sup_this="sup_this"
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
              <el-table-column :label="$t('form.status')">
                <template slot-scope="scope">
                  <div v-if="scope.row.status == 0" class="examine">
                    {{ $t("form.pendingActivation") }}
                  </div>
                  <div v-if="scope.row.status == 1" class="normal">
                    {{ $t("form.normal") }}
                  </div>
                  <div v-if="scope.row.status == 2" class="error">
                    {{ $t("form.overdue") }}
                  </div>
                  <div v-if="scope.row.status == 3" class="error">
                    {{ $t("form.locked") }}
                  </div>
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
            </el-table>
          </el-col>
        </el-row>
      </el-card>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="handleCloseRole()">{{
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
    </el-dialog>
    <el-dialog
      :append-to-body="true"
      :visible.sync="visibleDialog"
      :title="detailMain.display_name"
      width="1200px"
      :before-close="handleClose"
    >
      <empowerVue
        ref="empowerVue"
        :sup_this="sup_this"
        :is-add="false"
        @getlocalStorage="getlocalStorage"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="handleClose">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="submitForm()"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  detailProject,
  getProjectUsers,
  getProjectGroups,
  editProject,
  addUserToProject,
  delProjectUser,
} from "@/api/iam/project";
import { getUsers } from "@/api/iam/user";
import empowerVue from "./components/empower.vue";
import { getAuthz, getRolesAuthz, attachUserAuthz } from "@/api/iam/authz";
export default {
  components: { empowerVue },
  data() {
    return {
      sup_this: this,
      dialog: false,
      visibleDialog: false,
      loading: false,
      detailMain: {},
      projectUserData: [],
      multipleSelection: [],
      groupsData: [],
      tableData: [],
      selNumber: 0,
      activeName: "first",
      loadingForm: false,
      user_ids: [],
      empowerUsers: [],
      queryData: {
        name: "",
      },
      authzForm: {
        project_id: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      },
      total_num: 0,
      activeNames: ["1"],
      rules: {
        display_name: [
          {
            required: true,
            message: this.$t("message.pleaseInputProjectName"),
            trigger: "blur",
          },
        ],
      },
      localStorageData: [],
      localStorage_ids: [],
    };
  },
  computed: {},
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
    this.init();
    this.initPojeceUsers();
    this.initGroups();
  },
  mounted() {
    window.localStorage.clear();
  },
  beforeDestroy() {},
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
    async init() {
      const list = await detailProject(this.$route.params.id);
      this.detailMain = list;
    },
    async initGroups() {
      const list = await getProjectGroups(this.$route.params.id);
      this.groupsData = list;
    },
    async initPojeceUsers() {
      const list = await getProjectUsers(this.$route.params.id);
      this.projectUserData = list;
      this.empowerUsers = list.map((item) => {
        return item.user_id;
      });
    },
    async initUser() {
      this.tableData = [];
      const list = await getUsers(this.queryData);
      list.users.forEach((element) => {
        if (this.empowerUsers.indexOf(element.id) == -1) {
          this.tableData.push(element);
        }
      });
      this.total_num = this.tableData.length;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 表格数据展示
    checkboxSelect(row, rowIndex) {
      if (row.status == "disabled") {
        return false; // 禁用
      } else {
        return true; // 不禁用
      }
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
    // 添加用户
    saveAuthorize() {
      this.dialog = true;
      window.localStorage.clear();
      this.initUser();
    },
    // 异步加载数据
    expandChange(row) {
      this.$nextTick(() => {
        if (this.$refs.empowerUser) {
          this.$refs.empowerUser.user_id = row.id;
          this.$refs.empowerUser.project_id = this.$route.params.id;
          this.$refs.empowerUser.initData();
        }
      });
    },
    editAuthorzRole(value) {
      this.visibleDialog = true;
      this.$nextTick(() => {
        this.$refs.empowerVue.user_id = value.user_id;
        this.$refs.empowerVue.project_id = this.$route.params.id;
        this.$refs.empowerVue.initPolicieAuthz();
        this.$refs.empowerVue.initRoleAuthz();
      });
    },
    async doSubmit() {
      this.loading = true;
      if (this.user_ids.length > 0) {
        await addUserToProject(this.$route.params.id, {
          user_ids: this.user_ids,
        })
          .then((res) => {
            this.saveAuthz();
            this.$notify({
              title: this.$t("message.authorizeSuccess"),
              type: "success",
              duration: 2500,
            });
          })
          .catch((err) => {
            this.loading = false;
            return;
          });
      } else {
        this.resetAuthorzForm();
        this.loading = false;
        this.dialog = false;
      }
    },
    submitForm() {
      this.$refs.empowerVue.AuthorizeUser();
      this.visibleDialog = false;
    },
    handleClose() {
      this.$refs.empowerVue.resetAuthorzForm();
      this.visibleDialog = false;
    },
    handleCloseRole() {
      this.dialog = false;
    },
    doEdit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loadingForm = true;
          const params = {};
          params.name = this.detailMain.name;
          params.display_name = this.detailMain.display_name;
          params.description = this.detailMain.description;
          params.enable = this.detailMain.enable;
          editProject(this.$route.params.id, params)
            .then((res) => {
              this.$notify({
                title: this.$t("message.modifySuccess"),
                type: "success",
                duration: 2500,
              });
              this.loadingForm = false;
              this.init();
            })
            .catch((err) => {
              this.loadingForm = false;
              console.error(err.response.data.message);
            });
        } else {
          return false;
        }
      });
    },
    // 用户授权策略列表
    async saveAuthz() {
      for (var i = 0; i < this.user_ids.length; i++) {
        this.authzForm.user_id = this.user_ids[i];
        this.authzForm.project_id = this.$route.params.id;
        this.authzForm.project_name = this.detailMain.display_name;
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
      this.resetAuthorzForm();
      this.queryData.name = "";
      this.loading = false;
      this.dialog = false;
      this.initPojeceUsers();
    },
    // 重置授权列表
    resetAuthorzForm() {
      this.authzForm = {
        project_id: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      };
      this.$refs.multipleTable.clearSelection();
    },
    // 重置表格
    resetForm() {
      (this.queryData = {
        name: "",
      }),
        this.initUser();
    },
    clickUnbindUserBtn(value) {
      this.$confirm(
        this.$t("message.confirmCancelAuthorization"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delProjectUser(this.$route.params.id, {
            user_ids: [value.user_id],
          })
            .then((res) => {
              this.$notify({
                title: this.$t("message.cancelAuthorizationSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initPojeceUsers();
              window.localStorage.removeItem(value.user_id);
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
  },
};
</script>
<style scoped>
.yaml-editor {
  height: 100%;
  position: relative;
  top: 20px;
}

::v-deep .el-dialog__body {
  padding: 0px 20px;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

::v-deep .el-collapse-item__header {
  border: none;
  font-size: 16px;
}

::v-deep .el-collapse-item__wrap {
  border: none;
}

::v-deep .el-collapse {
  border: none;
}

::v-deep .el-collapse-item__content {
  padding-bottom: 20px;
}

::v-deep .el-table__expanded-cell[class*="cell"] {
  padding: 20px 10px;
  background-color: #c2c9d03d !important;
}

::v-deep .el-table__expanded-cell:hover {
  background-color: #c2c9d03d !important;
}
</style>
