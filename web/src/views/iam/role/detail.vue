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
        <el-form-item :label="$t('form.roleName') + ':'" prop="name">
          <el-input
            v-model="detailMain.name"
            :placeholder="$t('form.pleaseInputRoleName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'">
          <el-input
            v-model="detailMain.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.roleType') + ':'"
          prop="role_type"
        >
          <el-select
            v-model="detailMain.role_type"
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
                style="diaplay: inline"
                effect="dark"
                :content="$t('form.belongProjectTips')"
                placement="top"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </span>
          </span>
          <el-cascader
            v-model="detailMain.project_id"
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
            v-model="detailMain.iam_code"
            :placeholder="$t('form.pleaseSelectServiceCode')"
            style="width: 100%"
          >
            <el-option
              v-for="item in serveData"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            >
              <span style="float: left">{{ item.name }}</span>
              <span style="float: right; color: #8492a6; font-size: 13px">{{
                item.description
              }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 2 || userInfo.kind == 3 || userInfo.kind == 4"
          :label="$t('form.roleType') + ':'"
        >
          <span>{{ detailMain.role_type == 1 ? "system" : "custom" }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.createTime') + ':'">
          <span>{{
            detailMain.create_time ? detailMain.create_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.updateTime') + ':'">
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
    <el-card class="mb-2" shadow="never">
      <div
        slot="header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <span>{{ $t("form.empowerPolicy") }}</span>
        <el-button
          type="primary"
          size="small"
          style="float: right"
          @click="initEmpower()"
          >{{ $t("form.addPolicy") }}</el-button
        >
      </div>
      <el-table
        ref="multipleTable"
        :data="tableData"
        style="width: 100%; border-radius: 4px"
        stripe
        :default-sort="{ prop: 'date', order: 'descending' }"
        max-height="600"
        @expand-change="expandChange"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <yaml-editor
              ref="yamlEditor"
              v-model="policieMain.rules"
              class="leading-tight overflow-auto rounded max-h-96 min-w-full"
              :download-name="'策略内容'"
              :download-type="'yml'"
              :readonly="'true'"
              :lint="false"
            />
          </template>
        </el-table-column>
        <el-table-column
          prop="policy_display_name"
          :label="$t('form.policyName')"
        >
          <template slot-scope="scope">
            <router-link
              style="color: #409eff; cursor: pointer"
              :to="'/iam/policie/contact/' + scope.row.policy_id"
            >
              <span>
                {{ scope.row.policy_display_name }}
              </span>
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="policy_type" :label="$t('form.policyType')">
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
          prop="principal_name"
          :label="$t('form.principalName')"
        >
          <template slot-scope="scope">
            {{ scope.row.principal_name ? scope.row.principal_name : "-" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="principal_type"
          :label="$t('form.principalType')"
        >
          <template slot-scope="scope">
            <div v-if="scope.row.principal_type == 1">
              <svg
                t="1681701629708"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="3471"
                width="14"
                height="14"
              >
                <path
                  d="M895.853333 926.08a53.333333 53.333333 0 0 1-53.333333 55.253333H53.446667a53.333333 53.333333 0 0 1-53.3-55.286666c1.093333-30.206667 8.733333-72.1 38.933333-100.173334 25.086667-23.333333 51.913333-41.58 82-55.833333 26.606667-12.593333 53.453333-21.04 79.413333-29.213333 27.6-8.666667 56.14-17.666667 86.073334-32C316.666667 694.5 334.126667 670 338.666667 635.886667a199.1 199.1 0 0 1-60.053334-57.553334 188.206667 188.206667 0 0 1-29.126666-71.206666c-11.033333-2.593333-25.486667-10.866667-36.466667-34.46-8.28-17.74-12.833333-40.853333-12.833333-65.08 0-15.206667-2.853333-29.54-5.38-42.193334-3.78-18.986667-7.046667-35.386667 3.26-47.96 3.073333-3.74 9.906667-9.913333 21.846666-10.433333-0.82-11.493333-1.233333-22.04-1.24-31.533333-0.04-45.42 3.72-81.413333 11.48-110 10.106667-37.26 27.78-64 52.526667-79.48a21.333333 21.333333 0 0 1 20.426667-1.333334 21.086667 21.086667 0 0 1 12 16.366667 48.826667 48.826667 0 0 0 3.013333 10 120.26 120.26 0 0 1 14.133333-11.2c12.493333-8.626667 29.66-17.92 51.026667-27.62C418.666667 56.106667 453.573333 44.28 455.046667 43.786667a21.333333 21.333333 0 0 1 22.733333 34.413333c-7.113333 8-10.766667 13.666667-12.666667 17.333333 10.133333-0.066667 20.966667-1.72 32.373334-3.46 38.566667-5.886667 86.566667-13.213333 144.373333 34.78 37.38 31.033333 41.28 85.926667 35.393333 176.366667q-0.133333 2-0.32 4.186667a50.666667 50.666667 0 0 1 6.76-0.48c13.193333 0 20.766667 6 24.8 11.033333 10.36 12.946667 6.733333 29.14 2.526667 47.893333-2.813333 12.54-6 26.746667-6 41.693334 0 24.566667-5.193333 48-14.626667 66-10.666667 20.366667-26.546667 32.666667-44 34.433333C636.973333 558.333333 607 603.12 562.666667 632.486667c3.68 35.913333 20.973333 61.6 51.513333 76.433333 29.393333 14.28 57.426667 23.253333 84.533333 31.933333 25.54 8.18 51.953333 16.666667 78.14 29.246667 29.633333 14.266667 56.04 32.553333 80.733334 55.9 29.693333 28.066667 37.2 69.913333 38.266666 100.08zM874.666667 298.666667h128a21.333333 21.333333 0 0 0 0-42.666667h-128a21.333333 21.333333 0 0 0 0 42.666667z m128 469.333333h-42.666667a21.333333 21.333333 0 0 0 0 42.666667h42.666667a21.333333 21.333333 0 0 0 0-42.666667z m0-256h-170.666667a21.333333 21.333333 0 0 0 0 42.666667h170.666667a21.333333 21.333333 0 0 0 0-42.666667z"
                  fill="#515151"
                  p-id="3472"
                />
              </svg>
              {{ $t("form.user") }}
            </div>
            <div v-if="scope.row.principal_type == 2">
              <svg
                t="1681701784780"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="9049"
                width="14"
                height="14"
              >
                <path
                  d="M558.4 602.432c88.128-49.28 148.288-142.336 148.288-250.368a288 288 0 1 0-576-0.064c0 107.2 59.328 199.616 146.24 249.216C115.84 653.824 0 794.56 0 960h64c0-174.848 157.888-317.12 351.936-317.12C610.048 642.88 768 785.152 768 960h64c0-164.224-114.24-304.128-273.6-357.568zM194.688 352c0-123.52 100.48-224 223.936-224s224.064 100.48 224.064 224S542.208 576 418.624 576a224.256 224.256 0 0 1-223.936-224zM1024 768h-64a254.848 254.848 0 0 0-233.6-253.952 354.56 354.56 0 0 0 30.848-77.312 127.936 127.936 0 0 0-22.016-240.384 350.08 350.08 0 0 0-42.944-67.136c3.968-0.256 7.68-1.216 11.712-1.216 105.856 0 192 86.144 192 192 0 61.12-28.672 115.584-73.28 150.784A320.64 320.64 0 0 1 1024 768z"
                  fill="#515151"
                  p-id="9050"
                />
              </svg>
              {{ $t("form.userGroup") }}
            </div>
            <div v-if="scope.row.principal_type == 3">
              <svg
                t="1681701738912"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="7197"
                width="14"
                height="14"
              >
                <path
                  d="M79.23846992 961.89615371v-25.44230742c0-109.28076943 28.83461573-214.89230742 81.13846113-297.41538515 48.42692315-76.39615372 115.30384629-131.57307685 195.50769258-161.89615372A240.78461573 240.78461573 0 0 1 279.48846992 300.5c0-131.53846114 104.33076943-238.53461572 232.54615372-238.53461572s232.51153887 106.99615372 232.51153798 238.53461572a240.85384629 240.85384629 0 0 1-76.74230742 176.98846114c190.86923057 73.00384629 276.99230742 277.13076943 276.99230742 458.96538514v25.44230743H79.23846992zM694.90770049 300.5c0-103.43076943-82.03846114-187.61538427-182.87307686-187.61538427-100.83461573 0-182.87307685 84.18461573-182.87307685 187.61538427 0 103.46538427 82.03846114 187.65 182.87307685 187.65 100.83461573 0 182.87307685-84.18461573 182.87307686-187.65z m-79.16538516 213.50769258a226.45384629 226.45384629 0 0 1-103.7076917 25.0961537 225.93461572 225.93461572 0 0 1-104.12307686-25.30384628c-195.02307685 51.12692315-271.10769258 239.05384629-278.41153886 397.17692315h765.03461573c-7.99615372-167.4-95.22692315-347.74615372-278.79230831-396.96923057z m-143.41153799 37.2461537h79.40769258l39.73846114-8.48076943-45.24230742 65.66538429 30.6 227.52692313-64.8 56.90769258-69.19615372-56.90769258 40.53461485-227.52692313-50.78076944-65.66538429 39.73846201 8.48076944z"
                  p-id="7198"
                  fill="#515151"
                />
              </svg>
              {{ $t("form.role") }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="$t('form.description')">
          <template slot-scope="scope">
            {{ scope.row.description ? scope.row.description : "-" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="attach_time"
          :label="$t('form.attachTime')"
          sortable
        />
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <el-tooltip
              class="item"
              effect="dark"
              :content="$t('form.cancelUnbind')"
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
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.addPolicy')"
      width="1200px"
    >
      <empowerVue
        ref="empowerVue"
        :sup_this="sup_this"
        :policie-i-ds="empowerPolices"
      />
      <div slot="footer" class="dialog-footer">
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
    </el-dialog>
  </div>
</template>

<script>
import { detaillRole, editRoles } from "@/api/iam/role";
import { getAuthz, detachRoleAuthz } from "@/api/iam/authz";
import { detailPolicie } from "@/api/iam/policie";
import { getService } from "@/api/iam/serve";
import { getProject } from "@/api/iam/project";
import YamlEditor from "@/components/yaml/editor.vue";
import empowerVue from "./components/empower.vue";
import { mapGetters } from "vuex";
var Base64 = require("js-base64").Base64;
export default {
  async created() {
    this.init();
    this.serviceinit();
    this.projectinit();
    this.initPolicie();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  components: { YamlEditor, empowerVue },
  watch: {},
  data() {
    return {
      sup_this: this,
      detailMain: {},
      dialog: false,
      loading: false,
      loadingForm: false,
      empowerPolices: [],
      // 服务数据
      serveData: [],
      tableData: [],
      projectData: [],
      queryData: {
        name: "",
        page_size: 10,
        page_num: 1,
        total: 0,
      },
      policieMain: {},
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
            message: this.$t("form.pleaseSelectProject"),
            trigger: "change",
          },
        ],
      },
    };
  },
  methods: {
    async init() {
      const list = await detaillRole(this.$route.params.id);
      this.detailMain = list;
    },
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
    async initPolicie() {
      const params = {};
      params.role_id = this.$route.params.id;
      const list = await getAuthz(params);
      this.tableData = list.attachments;
      this.empowerPolices = this.tableData.map((item) => {
        return item.policy_id;
      });
    },
    async expandChange(row) {
      // if (this.$refs.yamlEditor) {
      const list = await detailPolicie(row.policy_id);
      this.policieMain = list;
      this.policieMain.document = JSON.parse(this.policieMain.document);
      this.policieMain.rules = "";
      this.policieMain.document.statement.forEach((el) => {
        el.rule = Base64.decode(el.rule) + "\n";
        this.policieMain.rules += el.rule;
      });
      // }
    },
    initEmpower() {
      this.dialog = true;
      this.$nextTick(() => {
        this.$refs.empowerVue.initPolicie();
      });
    },
    doSubmit() {
      this.loading = true;
      this.$refs.empowerVue.AuthorzRoles(
        this.$route.params.id,
        this.detailMain.project_id
      );
      this.loading = false;
      this.dialog = false;
      setTimeout(() => {
        this.initPolicie();
      }, 500);
    },
    cancel() {
      this.$refs.empowerVue.resetAuthorzForm();
      this.dialog = false;
    },
    doEdit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loadingForm = true;
          editRoles(this.$route.params.id, this.detailMain)
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
    clickUnbindPolicieBtn(value) {
      this.$confirm(
        this.$t("message.confirmCancelAuthorizationPolicy"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          const params = {};
          params.role_id = this.$route.params.id;
          params.policy_ids = [value.policy_id];
          detachRoleAuthz(params)
            .then((res) => {
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
::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

svg {
  display: inline-block;
  margin: -2px 2px 0px 0px;
}

::v-deep .el-table__expanded-cell[class*="cell"] {
  padding: 20px 10px;
  background-color: #c2c9d03d !important;
}

::v-deep .el-table__expanded-cell:hover {
  background-color: #c2c9d03d !important;
}
</style>
