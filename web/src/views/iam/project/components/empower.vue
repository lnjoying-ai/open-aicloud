<template>
  <div>
    <el-row :gutter="15">
      <el-col :span="17" style="position: relative">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>{{ $t("form.userAuthorization") }}</span>
          </div>
          <div style="height: 550px; overflow: hidden">
            <!-- <div style="position: absolute;right: 25px;z-index: 99;">
                            <el-button type="primary" size="small" @click="AuthorizeUser()">一键授权</el-button>
                        </div> -->
            <el-tabs v-model="activeName" shadow="never">
              <el-tab-pane :label="$t('form.policy')" name="first">
                <el-form
                  :model="queryPoliceData"
                  label-width="120px"
                  size="mini"
                >
                  <el-form-item
                    :label="$t('form.policyName') + ':'"
                    style="float: left"
                  >
                    <el-input
                      v-model="queryPoliceData.name"
                      size="mini"
                      style="width: 230px"
                    />
                    <el-button
                      type="primary"
                      size="mini"
                      style="margin-left: 10px"
                      @click="initPolicie()"
                      >{{ $t("form.query") }}</el-button
                    >
                    <el-button size="mini" @click="resetPolicie()">{{
                      $t("form.reset")
                    }}</el-button>
                  </el-form-item>
                  <el-form-item style="float: right" label-width="0px">
                    <el-pagination
                      :current-page="queryPoliceData.page_num"
                      :page-size="queryPoliceData.page_size"
                      layout="prev, pager, next"
                      :total="queryPoliceData.total_num"
                      size="mini"
                      @size-change="handlePoliceSizeChange"
                      @current-change="handlePoliceCurrentChange"
                    />
                  </el-form-item>
                </el-form>
                <el-table
                  ref="multiplePoliceTable"
                  :data="policieData"
                  style="width: 100%; border-radius: 4px"
                  stripe
                  :default-sort="{ prop: 'date', order: 'descending' }"
                  size="mini"
                  :row-key="getRowPolicieKeys"
                  @selection-change="handleSelectionPoliceChange"
                  @select="handleSelect"
                >
                  <el-table-column
                    type="selection"
                    width="50"
                    :reserve-selection="true"
                    :selectable="selected"
                  />
                  <el-table-column type="expand">
                    <template slot-scope="props">
                      <yaml-editor
                        ref="yamlEditor"
                        v-model="props.row.rules"
                        class="leading-tight overflow-auto rounded max-h-96 min-w-full"
                        :download-name="'策略内容'"
                        :download-type="'yml'"
                        :readonly="'true'"
                        :lint="false"
                      />
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="display_name"
                    :label="$t('form.name')"
                    show-overflow-tooltip
                  />
                  <el-table-column prop="policy_type" :label="$t('form.type')">
                    <template slot-scope="scope">
                      <div v-if="scope.row.policy_type == 1">
                        {{ $t("form.systemPolicy") }}
                      </div>
                      <div v-if="scope.row.policy_type == 2">
                        {{ $t("form.customPolicy") }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="default_version"
                    :label="$t('form.defaultVersion')"
                  >
                    <template slot-scope="scope">
                      <span>
                        {{ scope.row.default_version }}
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="description"
                    :label="$t('form.description')"
                    show-overflow-tooltip
                  />
                </el-table>
              </el-tab-pane>
              <el-tab-pane :label="$t('form.role')" name="second">
                <el-form
                  :model="queryRolesData"
                  label-width="120px"
                  size="mini"
                >
                  <el-form-item
                    :label="$t('form.roleName') + ':'"
                    style="float: left"
                  >
                    <el-input
                      v-model="queryRolesData.name"
                      size="mini"
                      style="width: 230px"
                    />
                    <el-button
                      type="primary"
                      size="mini"
                      style="margin-left: 10px"
                      @click="initRole()"
                      >{{ $t("form.query") }}</el-button
                    >
                    <el-button size="mini" @click="resetRoles()">{{
                      $t("form.reset")
                    }}</el-button>
                  </el-form-item>
                  <el-form-item style="float: right" label-width="0px">
                    <el-pagination
                      :current-page="queryRolesData.page_num"
                      :page-size="queryRolesData.page_size"
                      layout="prev, pager, next"
                      :total="queryRolesData.total_num"
                      size="mini"
                      @size-change="handleRolesSizeChange"
                      @current-change="handleRolesCurrentChange"
                    />
                  </el-form-item>
                </el-form>
                <el-table
                  ref="multipleRoleTable"
                  :data="roleData"
                  style="width: 100%; border-radius: 4px"
                  stripe
                  :default-sort="{ prop: 'date', order: 'descending' }"
                  size="mini"
                  :row-key="getRowKeys"
                  @selection-change="handleSelectionRolesChange"
                  @select="handleSelectRole"
                >
                  <el-table-column
                    type="selection"
                    width="50"
                    :reserve-selection="true"
                    :selectable="selected"
                  />
                  <el-table-column prop="name" :label="$t('form.name')" />
                  <el-table-column
                    prop="role_type"
                    :label="$t('form.roleType')"
                  >
                    <template slot-scope="scope">
                      <div v-if="scope.row.role_type == 1">{{ "system" }}</div>
                      <div v-if="scope.row.role_type == 2">{{ "custom" }}</div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="iam_code"
                    :label="$t('form.serviceCode')"
                  />
                  <el-table-column
                    prop="description"
                    :label="$t('form.description')"
                    show-overflow-tooltip
                  />
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
      <el-col :span="7">
        <el-card shadow="never">
          <div
            slot="header"
            style="
              display: flex;
              justify-content: space-between;
              align-items: center;
            "
          >
            <span>{{ $t("form.selected") }}</span>
          </div>
          <div style="height: 550px; overflow: auto">
            <el-tag
              v-for="(items, index) in multipleSelectionPolice"
              :key="items.id"
              effect="plain"
              closable
              :type="items.type"
              @close="handleTagBindPolicieClose(items, index)"
              >{{ items.display_name }}</el-tag
            >
            <el-tag
              v-for="(items, index) in multipleSelectionRole"
              :key="items.role_id"
              effect="plain"
              closable
              :type="items.type"
              @close="handleTagBindRoleClose(items, index)"
              >{{ items.name }}</el-tag
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getAuthz, detachUserAuthz, getRolesAuthz } from "@/api/iam/authz";
import { getPolicie } from "@/api/iam/policie";
import { getRoles } from "@/api/iam/role";
import { attachUserAuthz } from "@/api/iam/authz";
import YamlEditor from "@/components/yaml/editor.vue";
var Base64 = require("js-base64").Base64;
export default {
  components: { YamlEditor },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    isAdd: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      activeName: "first",
      multipleSelectionPolice: [],
      multipleSelectionRole: [],
      user_id: "",
      project_id: "",
      roleData: [],
      policieData: [],
      authzForm: {
        project_id: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      },
      queryPoliceData: {
        name: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      },
      queryRolesData: {
        name: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      },
      // 初始化已绑定的策略和角色
      initBindPolicieData: [],
      initBindRoleData: [],
      // 存储的数据
      localStorageData: [],
    };
  },
  computed: {},
  watch: {
    multipleSelectionPolice: {
      deep: true,
      handler(val) {
        this.authzForm.policy_ids = [];
        val.forEach((element) => {
          this.authzForm.policy_ids.push(element.id);
        });
        this.$emit(
          "getlocalStorage",
          this.user_id,
          this.multipleSelectionPolice,
          this.multipleSelectionRole
        );
      },
    },
    multipleSelectionRole: {
      deep: true,
      handler(val) {
        this.authzForm.role_ids = [];
        val.forEach((element) => {
          this.authzForm.role_ids.push(element.role_id);
        });
        this.$emit(
          "getlocalStorage",
          this.user_id,
          this.multipleSelectionPolice,
          this.multipleSelectionRole
        );
      },
    },
  },
  async created() {},
  mounted() {},
  beforeDestroy() {},
  methods: {
    initData() {
      if (JSON.parse(window.localStorage.getItem(this.user_id))) {
        this.initPolicie();
        this.initRole();
        this.localStorageData = JSON.parse(
          window.localStorage.getItem(this.user_id)
        );
        this.localStorageData.policys.forEach((element) => {
          this.$refs.multiplePoliceTable.toggleRowSelection(element, true);
        });
        this.localStorageData.roles.forEach((element) => {
          this.$refs.multipleRoleTable.toggleRowSelection(element, true);
        });
      } else {
        this.initPolicieAuthz();
        this.initRoleAuthz();
      }
    },
    // 授权策略
    async initPolicieAuthz() {
      this.initBindRoleData = [];
      this.policieData = [];
      this.multipleSelectionPolice = [];
      const params = {};
      params.user_id = this.user_id;
      params.project_id = this.project_id;
      const list = await getAuthz(params);
      if (this.project_id) {
        this.initBindPolicieData = list.attachments;
      } else {
        this.initBindPolicieData = list.attachments.filter(
          (item) => item.project_id == null
        );
      }
      if (this.initBindPolicieData.length == 0) {
        this.$emit(
          "getlocalStorage",
          this.user_id,
          this.multipleSelectionPolice,
          this.multipleSelectionRole
        );
        this.initPolicie();
      } else {
        const listPolice = await getPolicie();
        listPolice.policies.forEach((element, index) => {
          if (index < 10) {
            this.policieData.push(element);
          }
          this.initBindPolicieData.forEach((item) => {
            if (item.policy_id == element.id) {
              this.$refs.multiplePoliceTable.toggleRowSelection(element, true);
            }
          });
          element.document = JSON.parse(element.document);
          element.rules = "";
          element.document.statement.forEach((el, index) => {
            index == element.document.statement.length - 1
              ? (el.rule = Base64.decode(el.rule))
              : (el.rule =
                  Base64.decode(el.rule) +
                  "\n" +
                  "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" +
                  "\n" +
                  "\n");
            element.rules += el.rule;
          });
        });
        this.queryPoliceData.total_num = listPolice.total_num;
      }
    },
    // 授权角色
    async initRoleAuthz() {
      this.roleData = [];
      this.initBindRoleData = [];
      this.multipleSelectionRole = [];
      const params = {};
      params.user_id = this.user_id;
      params.project_id = this.project_id;
      const list = await getRolesAuthz(params);
      if (this.project_id) {
        this.initBindRoleData = list.attachments;
      } else {
        this.initBindRoleData = list.attachments.filter(
          (item) => item.project_id == null
        );
      }
      if (this.initBindRoleData.length == 0) {
        this.$emit(
          "getlocalStorage",
          this.user_id,
          this.multipleSelectionPolice,
          this.multipleSelectionRole
        ),
          this.initRole();
      } else {
        const rolelist = await getRoles();
        rolelist.roles.forEach((element, index) => {
          if (index < 10) {
            this.roleData.push(element);
          }
          this.initBindRoleData.forEach((item) => {
            if (Number(item.role_id) == element.role_id) {
              this.$refs.multipleRoleTable.toggleRowSelection(element, true);
            }
          });
        });
        this.queryRolesData.total_num = rolelist.total_num;
      }
    },

    async initPolicie() {
      const listPolice = await getPolicie(this.queryPoliceData);
      this.policieData = listPolice.policies;
      this.queryPoliceData.total_num = listPolice.total_num;
      this.policieData.forEach((element) => {
        element.document = JSON.parse(element.document);
        element.rules = "";
        element.document.statement.forEach((el, index) => {
          index == element.document.statement.length - 1
            ? (el.rule = Base64.decode(el.rule))
            : (el.rule =
                Base64.decode(el.rule) +
                "\n" +
                "------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------" +
                "\n" +
                "\n");
          element.rules += el.rule;
        });
      });
    },

    async initRole() {
      const list = await getRoles(this.queryRolesData);
      this.roleData = list.roles;
      this.queryRolesData.total_num = list.total_num;
    },
    selected(row) {
      if (row.disabled == true) {
        return false; // 不可勾选
      } else {
        return true; // 可勾选
      }
    },
    resetRoles() {
      this.queryRolesData = {
        name: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      };
      this.initRole();
    },
    resetPolicie() {
      this.queryPoliceData = {
        name: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      };
      this.initPolicie();
    },
    resetAuthorzForm() {
      (this.authzForm = {
        project_id: "",
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      }),
        (this.queryPoliceData = {
          name: "",
          page_size: 10,
          page_num: 1,
          total_num: 0,
        }),
        (this.queryRolesData = {
          name: "",
          page_size: 10,
          page_num: 1,
          total_num: 0,
        }),
        (this.activeName = "first"),
        this.$refs.multiplePoliceTable.clearSelection();
      this.$refs.multipleRoleTable.clearSelection();
    },
    // 设置key
    getRowKeys(row) {
      return row.role_id;
    },
    getRowPolicieKeys(row) {
      return row.id;
    },
    handleSelect(rows, row) {
      const selected = rows.length && rows.indexOf(row) !== -1;
      selected
        ? ""
        : (this.initBindPolicieData = this.initBindPolicieData.filter(
            (item) => item.policy_id != row.id
          ));
    },
    handleSelectRole(rows, row) {
      const selected = rows.length && rows.indexOf(row) !== -1;
      selected
        ? ""
        : (this.initBindRoleData = this.initBindRoleData.filter(
            (item) => item.role_id != row.role_id
          ));
    },
    handleSelectionPoliceChange(val) {
      this.multipleSelectionPolice = val;
    },
    handleSelectionRolesChange(val) {
      this.multipleSelectionRole = val;
    },
    handleTagBindPolicieClose(value, index) {
      this.$refs.multiplePoliceTable.selection.forEach((element) => {
        if (element.id === this.multipleSelectionPolice[index].id) {
          this.$refs.multiplePoliceTable.toggleRowSelection(element, false);
        } else {
          this.$refs.multiplePoliceTable.toggleRowSelection(element, true);
        }
      });
      this.initBindPolicieData = this.initBindPolicieData.filter(
        (item) => item.policy_id != value.id
      );
    },
    handleTagBindRoleClose(value, index) {
      this.$refs.multipleRoleTable.selection.forEach((element) => {
        if (element.role_id == this.multipleSelectionRole[index].role_id) {
          this.$refs.multipleRoleTable.toggleRowSelection(element, false);
        } else {
          this.$refs.multipleRoleTable.toggleRowSelection(element, true);
        }
      });
      this.initBindRoleData = this.initBindRoleData.filter(
        (item) => item.role_id != value.role_id
      );
    },

    handlePoliceSizeChange(val) {
      this.queryPoliceData.page_size = val;
      this.initPolicie();
    },
    handlePoliceCurrentChange(val) {
      this.queryPoliceData.page_num = val;
      this.initPolicie();
    },
    handleRolesCurrentChange(val) {
      this.queryRolesData.page_num = val;
      this.initRole();
    },
    handleRolesSizeChange(val) {
      this.queryRolesData.page_size = val;
      this.initRole();
    },
    // 为指定用户授权
    AuthorizeUser() {
      this.authzForm.user_id = this.user_id;
      this.authzForm.project_id = this.project_id;
      attachUserAuthz(this.authzForm)
        .then((res) => {
          this.resetAuthorzForm();
          this.$notify({
            title: this.$t("message.authorizeSuccess"),
            type: "success",
            duration: 2500,
          });
        })
        .catch((err) => {});
    },
    // 授权策略
    // singleAuthorzPolicice(value) {
    //     this.$confirm("此操作将授权该策略, 是否继续?", "提示", {
    //         confirmButtonText: "确定",
    //         cancelButtonText: "取消",
    //         type: "warning",
    //     })
    //         .then(() => {
    //             let params = {}
    //             params.user_id = this.user_id;
    //             params.project_id = this.project_id;
    //             params.policy_ids = [value.policy_id]
    //             attachUserAuthz(params)
    //                 .then((res) => {
    //                     this.resetAuthorzForm()
    //                     this.$notify({
    //                         title: "授权成功",
    //                         type: "success",
    //                         duration: 2500,
    //                     });
    //                     this.initPolicieAuthz()
    //                 })
    //                 .catch((err) => {
    //                 });
    //         })
    //         .catch(() => { });
    // },
    // //授权角色
    // singleAuthorzRole(value) {
    //     this.$confirm("此操作将授权该角色, 是否继续?", "提示", {
    //         confirmButtonText: "确定",
    //         cancelButtonText: "取消",
    //         type: "warning",
    //     })
    //         .then(() => {
    //             let params = {}
    //             params.user_id = this.user_id;
    //             params.project_id = this.project_id;
    //             params.role_ids = [value.role_id]
    //             attachUserAuthz(params)
    //                 .then((res) => {
    //                     this.resetAuthorzForm()
    //                     this.$notify({
    //                         title: "授权成功",
    //                         type: "success",
    //                         duration: 2500,
    //                     });
    //                     this.initRoleAuthz()
    //                 })
    //                 .catch((err) => {
    //                 });
    //         })
    //         .catch(() => { });
    // },
    // clickUnbindRoleBtn(value) {
    //     this.$confirm("此操作将取消授权该角色, 是否继续?", "提示", {
    //         confirmButtonText: "确定",
    //         cancelButtonText: "取消",
    //         type: "warning",
    //     })
    //         .then(() => {
    //             let params = {}
    //             params.user_id = this.user_id;
    //             params.project_id = this.project_id;
    //             params.role_ids = [value.role_id]
    //             detachUserAuthz(params)
    //                 .then((res) => {
    //                     this.resetAuthorzForm()
    //                     this.$notify({
    //                         title: "取消授权成功",
    //                         type: "success",
    //                         duration: 2500,
    //                     });
    //                     this.initRoleAuthz()

    //                 })
    //                 .catch((err) => { });
    //         })
    //         .catch(() => { });

    // },
    // clickUnbindPolicieBtn(value) {
    //     this.$confirm("此操作将取消授权该策略, 是否继续?", "提示", {
    //         confirmButtonText: "确定",
    //         cancelButtonText: "取消",
    //         type: "warning",
    //     })
    //         .then(() => {
    //             let params = {}
    //             params.user_id = this.user_id;
    //             params.project_id = this.project_id;
    //             params.policy_ids = [value.policy_id]
    //             detachUserAuthz(params)
    //                 .then((res) => {
    //                     this.resetAuthorzForm()
    //                     this.$notify({
    //                         title: "取消授权成功",
    //                         type: "success",
    //                         duration: 2500,
    //                     });
    //                     this.initPolicieAuthz()
    //                 })
    //                 .catch((err) => { });
    //         })
    //         .catch(() => { });
    // },
  },
};
</script>
<style scoped>
.yaml-editor >>> .CodeMirror {
  height: 600px;
  min-height: 450px;
  border-radius: 4px;
}

::v-deep .el-tag--plain {
  display: block;
  margin: 7px 0px 0px 0px;
}

::v-deep .el-tag--plain .el-tag__close {
  float: right;
  margin-top: 10px;
}

svg {
  display: inline-block;
  vertical-align: middle;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

::v-deep .el-table__expanded-cell[class*="cell"] {
  padding: 20px 10px;
  background-color: #c2c9d03d !important;
}

::v-deep .el-table__expanded-cell:hover {
  background-color: #c2c9d03d !important;
}
</style>
