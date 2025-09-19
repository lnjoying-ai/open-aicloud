<template>
  <div>
    <el-row :gutter="15">
      <el-col :span="17" style="position: relative">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>{{ $t("form.userAuthorization") }}</span>
          </div>
          <div style="height: 630px; overflow: hidden">
            <div style="position: absolute; right: 25px; z-index: 99">
              <el-button
                v-if="!isAdd"
                type="primary"
                size="small"
                @click="AuthorzUser($route.params.id)"
                >{{ $t("form.quickEmpower") }}</el-button
              >
            </div>
            <el-tabs v-model="activeName" shadow="never">
              <el-tab-pane :label="$t('form.policy')" name="first">
                <el-form
                  :model="queryPoliceData"
                  label-width="100px"
                  class="demo-ruleForm"
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
                  <el-form-item style="float: right">
                    <el-pagination
                      :page-size="queryPoliceData.page_size"
                      :current-page.sync="queryPoliceData.page_num"
                      layout="prev, pager, next"
                      :total="queryPoliceData.total"
                      @size-change="handlePoliceSizeChange"
                      @current-change="handlePoliceCurrentChange"
                    />
                  </el-form-item>
                </el-form>
                <el-table
                  ref="multipleTable"
                  :data="policieData"
                  style="width: 100%; border-radius: 4px"
                  stripe
                  :default-sort="{ prop: 'date', order: 'descending' }"
                  max-height="530"
                  :row-key="getRowPolicieKeys"
                  @selection-change="handleSelectionChange"
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
                  <el-table-column
                    v-if="!isAdd"
                    :label="$t('form.operation')"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-tooltip
                        v-if="scope.row.disabled"
                        class="item"
                        effect="dark"
                        :content="$t('form.cancelEmpower')"
                        placement="top"
                      >
                        <el-button
                          type="text"
                          size="small"
                          @click="clickUnbindPolicieBtn(scope.row)"
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
                      <el-tooltip
                        v-else
                        class="item"
                        effect="dark"
                        :content="$t('form.empower')"
                        placement="top"
                      >
                        <el-button
                          type="text"
                          size="small"
                          @click="singleAuthorzPolicice(scope.row)"
                        >
                          <svg
                            t="1681460761727"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                            p-id="6763"
                            width="16"
                            height="16"
                          >
                            <path
                              d="M219.049746 901.655425c-76.8-76.8-76.8-201.303007 0-278.103007L631.477459 211.124706c76.8-76.8 201.303007-76.8 278.103006 0 76.8 76.8 76.8 201.303007 0 278.103006L497.152753 901.655425c-76.8 76.8-201.303007 76.8-278.103007 0z"
                              fill="#B3F995"
                              p-id="6764"
                            />
                            <path
                              d="M293.038766 356.141176l62.192941 62.243138L137.430923 636.252026c-66.894641 69.053072-66.024575 178.999216 1.957647 246.981438 67.982222 67.982222 177.928366 68.852288 246.981438 1.957647L604.204256 667.373595l62.243137 62.209673L448.596413 947.417516c-103.47085 101.07817-268.984052 100.107712-371.250196-2.175163-102.282876-102.282876-103.236601-267.796078-2.175163-371.250196l217.867712-217.850981z m62.192941-62.192941L573.082688 76.080523c103.47085-101.061438 268.984052-100.107712 371.250196 2.175163 102.266144 102.266144 103.236601 267.779346 2.175163 371.250196l-217.85098 217.850981-62.209674-62.243138 217.817517-217.85098c66.894641-69.053072 66.024575-178.999216-1.957647-246.981438-67.982222-67.982222-177.928366-68.852288-246.981438-1.957647L417.474844 356.141176l-62.243137-62.192941z m0 311.182223L604.204256 356.141176a44.058771 44.058771 0 0 1 43.018039-12.448627 44.025307 44.025307 0 0 1 31.673726 31.673726c3.998954 15.544052-0.769673 32.025098-12.448628 43.018039l-248.972549 248.972549a44.021961 44.021961 0 0 1-43.018039 12.448627 44.052078 44.052078 0 0 1-31.673725-31.673725c-3.998954-15.52732 0.769673-32.008366 12.448627-43.001307z m0 0"
                              fill="#00B38A"
                              p-id="6765"
                            />
                          </svg>
                        </el-button>
                      </el-tooltip>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
              <el-tab-pane :label="$t('form.role')" name="second">
                <el-form
                  :model="queryPoliceData"
                  label-width="100px"
                  class="demo-ruleForm"
                  size="mini"
                >
                  <el-form-item
                    :label="$t('form.roleName')"
                    style="float: left"
                  >
                    <el-input
                      v-model="queryData.name"
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
                    <el-button size="mini" @click="resetRole()">{{
                      $t("form.reset")
                    }}</el-button>
                  </el-form-item>
                  <el-form-item style="float: right">
                    <el-pagination
                      :page-size="queryData.page_size"
                      :current-page.sync="queryData.page_num"
                      layout="prev, pager, next"
                      :total="queryData.total"
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                    />
                  </el-form-item>
                </el-form>
                <el-table
                  ref="multipleRoleTable"
                  :data="roleData"
                  style="width: 100%; border-radius: 4px"
                  stripe
                  :default-sort="{ prop: 'date', order: 'descending' }"
                  max-height="535"
                  :row-key="getRowKeys"
                  @selection-change="handleRoleSelectionChange"
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
                      <div v-if="scope.row.role_type == 1">system</div>
                      <div v-if="scope.row.role_type == 2">custom</div>
                    </template>
                  </el-table-column>
                  <el-table-column prop="bp_name" :label="$t('form.bp')">
                    <template slot-scope="scope">
                      {{ scope.row.bp_name ? scope.row.bp_name : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="iam_code"
                    :label="$t('form.serviceCode')"
                  >
                    <template slot-scope="scope">
                      {{ scope.row.iam_code ? scope.row.iam_code : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="user_name" :label="$t('form.user')" />
                  <el-table-column
                    prop="description"
                    :label="$t('form.description')"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      {{ scope.row.description ? scope.row.description : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    v-if="!isAdd"
                    :label="$t('form.operation')"
                    width="100"
                  >
                    <template slot-scope="scope">
                      <el-tooltip
                        v-if="scope.row.disabled"
                        class="item"
                        effect="dark"
                        :content="$t('form.cancelEmpower')"
                        placement="top"
                      >
                        <el-button
                          type="text"
                          size="small"
                          @click="clickUnbindRoleBtn(scope.row)"
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
                      <el-tooltip
                        v-else
                        class="item"
                        effect="dark"
                        :content="$t('form.empower')"
                        placement="top"
                      >
                        <el-button
                          type="text"
                          size="small"
                          @click="singleAuthorzRole(scope.row)"
                        >
                          <svg
                            t="1681460761727"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                            p-id="6763"
                            width="16"
                            height="16"
                          >
                            <path
                              d="M219.049746 901.655425c-76.8-76.8-76.8-201.303007 0-278.103007L631.477459 211.124706c76.8-76.8 201.303007-76.8 278.103006 0 76.8 76.8 76.8 201.303007 0 278.103006L497.152753 901.655425c-76.8 76.8-201.303007 76.8-278.103007 0z"
                              fill="#B3F995"
                              p-id="6764"
                            />
                            <path
                              d="M293.038766 356.141176l62.192941 62.243138L137.430923 636.252026c-66.894641 69.053072-66.024575 178.999216 1.957647 246.981438 67.982222 67.982222 177.928366 68.852288 246.981438 1.957647L604.204256 667.373595l62.243137 62.209673L448.596413 947.417516c-103.47085 101.07817-268.984052 100.107712-371.250196-2.175163-102.282876-102.282876-103.236601-267.796078-2.175163-371.250196l217.867712-217.850981z m62.192941-62.192941L573.082688 76.080523c103.47085-101.061438 268.984052-100.107712 371.250196 2.175163 102.266144 102.266144 103.236601 267.779346 2.175163 371.250196l-217.85098 217.850981-62.209674-62.243138 217.817517-217.85098c66.894641-69.053072 66.024575-178.999216-1.957647-246.981438-67.982222-67.982222-177.928366-68.852288-246.981438-1.957647L417.474844 356.141176l-62.243137-62.192941z m0 311.182223L604.204256 356.141176a44.058771 44.058771 0 0 1 43.018039-12.448627 44.025307 44.025307 0 0 1 31.673726 31.673726c3.998954 15.544052-0.769673 32.025098-12.448628 43.018039l-248.972549 248.972549a44.021961 44.021961 0 0 1-43.018039 12.448627 44.052078 44.052078 0 0 1-31.673725-31.673725c-3.998954-15.52732 0.769673-32.008366 12.448627-43.001307z m0 0"
                              fill="#00B38A"
                              p-id="6765"
                            />
                          </svg>
                        </el-button>
                      </el-tooltip>
                    </template>
                  </el-table-column>
                </el-table>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </el-col>
      <el-col :span="7">
        <el-card shadow="never">
          <div slot="header" class="clearfix">
            <span>{{
              isAdd ? $t("form.selected") : $t("form.authorized")
            }}</span>
          </div>
          <div v-if="isAdd" style="height: 630px; overflow: auto">
            <el-tag
              v-for="(items, index) in multipleSelection"
              :key="items.id"
              effect="plain"
              closable
              @close="handleTagClose(items, index)"
              >{{ items.display_name }}</el-tag
            >
            <el-tag
              v-for="(items, index) in multipleSelectionRole"
              :key="items.role_id"
              effect="plain"
              closable
              @close="handleTagRoleClose(items, index)"
              >{{ items.name }}</el-tag
            >
          </div>
          <div v-else style="height: 630px; overflow: auto">
            <el-tag
              v-for="items in policyList"
              :key="items.id"
              effect="plain"
              closable
              @close="clickUnbindPolicieBtn(items)"
              >{{ items.policy_display_name }}</el-tag
            >
            <el-tag
              v-for="items in roleList"
              :key="items.role_id"
              effect="plain"
              closable
              @close="clickUnbindRoleBtn(items)"
              >{{ items.role_name }}</el-tag
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getPolicie } from "@/api/iam/policie";
import { getRoles } from "@/api/iam/role";
import YamlEditor from "@/components/yaml/editor.vue";
import { attachUserAuthz, detachUserAuthz } from "@/api/iam/authz";
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
    authorizePolicy: {
      type: Array,
      default: [],
    },
    authorizeRole: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      activeName: "first",
      multipleSelection: [],
      multipleSelectionRole: [],
      roleData: [],
      policieData: [],
      authzForm: {
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      },
      queryData: {
        name: "",
        page_size: 10,
        page_num: 1,
        total: 0,
      },
      queryPoliceData: {
        name: "",
        page_size: 10,
        page_num: 1,
        total: 0,
      },
      roleList: [],
      policyList: [],
    };
  },
  computed: {},
  watch: {
    multipleSelection(val) {
      this.authzForm.policy_ids = [];
      val.forEach((element) => {
        this.authzForm.policy_ids.push(element.id);
      });
    },
    multipleSelectionRole(val) {
      this.authzForm.role_ids = [];
      val.forEach((element) => {
        this.authzForm.role_ids.push(element.role_id);
      });
    },
    authorizePolicy: {
      deep: true,
      handler(val) {
        if (val) {
          val = val.filter((item) => item.project_id == null);
          if (
            !this.isAdd &&
            JSON.stringify(val) != JSON.stringify(this.policyList)
          ) {
            this.policyList = val;
            this.initPolicie();
          }
        }
      },
    },
    authorizeRole: {
      deep: true,
      handler(val) {
        if (val) {
          val = val.filter((item) => item.project_id == null);
          if (
            !this.isAdd &&
            JSON.stringify(val) != JSON.stringify(this.roleList)
          ) {
            this.roleList = val;
            this.initRole();
          }
        }
      },
    },
  },
  created() {
    this.initPolicie();
    this.initRole();
  },
  mounted() {
    this.policyList = this.authorizePolicy.filter(
      (item) => item.project_id == null
    );
    this.roleList = this.authorizeRole.filter(
      (item) => item.project_id == null
    );
  },
  methods: {
    // 策略列表
    async initPolicie() {
      const list = await getPolicie(this.queryPoliceData);
      this.policieData = list.policies;
      this.queryPoliceData.total = list.total_num;
      this.policieData.forEach((element) => {
        element.policy_id = element.id;
        this.policyList.forEach((el) => {
          if (el.policy_id == element.id) {
            element.description =
              element.description + "(" + this.$t("form.added") + ")";
            element.disabled = true;
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
                "-------------------------------------------------------------------------------------------------" +
                "\n" +
                "\n");
          element.rules += el.rule;
        });
      });
    },
    async initRole() {
      const list = await getRoles(this.queryData);
      this.roleData = list.roles;
      this.queryData.total = list.total_num;
      this.roleData.forEach((element) => {
        this.roleList.forEach((el) => {
          if (el.role_id == element.role_id) {
            element.description =
              element.description + "(" + this.$t("form.added") + ")";
            element.disabled = true;
          }
        });
      });
    },
    resetPolicie() {
      this.queryPoliceData = {
        name: "",
        page_size: 10,
        page_num: 1,
        total: 0,
      };
      this.initPolicie();
    },
    resetRole() {
      (this.queryData = {
        name: "",
        page_size: 10,
        page_num: 1,
        total: 0,
      }),
        this.initRole();
    },
    // 设置key
    getRowKeys(row) {
      return row.role_id;
    },
    // 设置key
    getRowPolicieKeys(row) {
      return row.id;
    },
    selected(row) {
      if (row.disabled == true) {
        return false; // 不可勾选
      } else {
        return true; // 可勾选
      }
    },
    // 关闭tag调用
    handleTagClose(item, index) {
      // this.multipleSelection.splice(index, 1);
      // this.$refs.multipleTable.toggleRowSelection(item, false);
      this.$refs.multipleTable.selection.forEach((element) => {
        element.id === this.multipleSelection[index].id
          ? this.$refs.multipleTable.toggleRowSelection(element, false)
          : this.$refs.multipleTable.toggleRowSelection(element, true);
      });
      this.policieData = Object.assign([], this.policieData);
    },
    // 关闭tag调用
    handleTagRoleClose(item, index) {
      // console.log(this.$refs.multipleRoleTable)
      // this.multipleSelectionRole.splice(index, 1);
      // this.$refs.multipleRoleTable.toggleRowSelection(item, false);
      this.$refs.multipleRoleTable.selection.forEach((element) => {
        element.role_id === this.multipleSelectionRole[index].role_id
          ? this.$refs.multipleRoleTable.toggleRowSelection(element, false)
          : this.$refs.multipleRoleTable.toggleRowSelection(element, true);
      });
      this.roleData = Object.assign([], this.roleData);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleRoleSelectionChange(val) {
      this.multipleSelectionRole = val;
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.initRole();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.initRole();
    },
    handlePoliceSizeChange(val) {
      this.queryPoliceData.page_size = val;
      this.initPolicie();
    },
    handlePoliceCurrentChange(val) {
      this.queryPoliceData.page_num = val;
      this.initPolicie();
    },
    // 一键授权
    AuthorzUser(id) {
      var data = this.multipleSelection.concat(this.multipleSelectionRole);
      if (data.length == 0) {
        this.$notify({
          title: this.$t("message.pleaseSelectPolicyAndRole"),
          type: "succwarningess",
          duration: 2500,
        });
        return;
      }
      this.$confirm(this.$t("message.confirmEmpower"), this.$t("message.tip"), {
        confirmButtonText: this.$t("message.confirm"),
        cancelButtonText: this.$t("message.cancel"),
        type: "warning",
      })
        .then(() => {
          this.authzForm.user_id = id;
          attachUserAuthz(this.authzForm)
            .then((res) => {
              this.multipleSelectionRole.forEach((el) => {
                this.roleList.push({ role_id: el.role_id, role_name: el.name });
              });
              this.multipleSelection.forEach((el) => {
                this.policyList.push({
                  policy_id: el.id,
                  policy_display_name: el.display_name,
                });
              });
              this.$notify({
                title: this.$t("message.authorizeSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initRole();
              this.initPolicie();
              this.resetAuthorzForm();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    AuthorzUserAdd(id) {
      var data = this.multipleSelection.concat(this.multipleSelectionRole);
      if (data.length > 0) {
        this.authzForm.user_id = id;
        attachUserAuthz(this.authzForm)
          .then((res) => {
            this.$notify({
              title: this.$t("message.authorizeSuccess"),
              type: "success",
              duration: 2500,
            });
          })
          .catch((err) => {});
      } else {
        return;
      }
    },
    // 授权策略
    singleAuthorzPolicice(value) {
      this.$confirm(
        this.$t("message.confirmAuthorizePolicy"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          const params = {};
          params.user_id = this.$route.params.id;
          params.policy_ids = [value.id];
          attachUserAuthz(params)
            .then((res) => {
              this.policyList.push({
                policy_id: value.id,
                policy_display_name: value.display_name,
              });
              this.resetAuthorzForm();
              this.$notify({
                title: this.$t("message.authorizeSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initPolicie();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    // 授权策略
    singleAuthorzRole(value) {
      this.$confirm(
        this.$t("message.confirmAuthorizeRole"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          const params = {};
          params.user_id = this.$route.params.id;
          params.role_ids = [value.role_id];
          attachUserAuthz(params)
            .then((res) => {
              this.roleList.push({
                role_id: value.role_id,
                role_name: value.name,
              });
              this.resetAuthorzForm();
              this.$notify({
                title: this.$t("message.authorizeSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initRole();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    resetAuthorzForm() {
      this.$refs.multipleTable.clearSelection();
      this.$refs.multipleRoleTable.clearSelection();
      this.authzForm = {
        user_id: "",
        policy_ids: [],
        role_ids: [],
        description: "",
      };
    },
    clickUnbindRoleBtn(value) {
      console.log(value);

      this.$confirm(
        this.$t("message.confirmCancelAuthorizeRole"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          const params = {};
          params.user_id = this.$route.params.id;
          params.role_ids = [value.role_id];
          detachUserAuthz(params)
            .then((res) => {
              this.roleList = this.roleList.filter(
                (el) => el.role_id !== value.role_id
              );
              console.log(this.roleList);
              this.resetAuthorzForm();
              this.$notify({
                title: this.$t("message.cancelAuthorizationSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initRole();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickUnbindPolicieBtn(value) {
      console.log(value);
      this.$confirm(
        this.$t("message.confirmCancelAuthorizePolicy"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          const params = {};
          params.user_id = this.$route.params.id;
          params.policy_ids = [value.policy_id];
          detachUserAuthz(params)
            .then((res) => {
              this.policyList = this.policyList.filter(
                (el) => el.policy_id !== value.policy_id
              );
              this.resetAuthorzForm();
              this.$notify({
                title: this.$t("message.cancelAuthorizationSuccess"),
                type: "success",
                duration: 2500,
              });
              this.initPolicie();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-tag--plain {
  background-color: #fff;
  border-color: #b3d8ff;
  color: #409eff;
  display: block;
  margin: 7px 0px 0px 0px;
}

::v-deep .el-tag--plain .el-tag__close {
  float: right;
  margin-top: 10px;
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
