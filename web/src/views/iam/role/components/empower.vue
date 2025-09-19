<template>
  <div>
    <el-row :gutter="40">
      <el-col :span="24">
        <el-card shadow="never">
          <div slot="header">
            <el-collapse v-model="activeName">
              <el-collapse-item :title="getTitle()" name="1">
                <el-tag
                  v-for="(items, index) in multipleSelection"
                  :key="index"
                  effect="plain"
                  closable
                  clsss="tags"
                  style="margin: 0px 5px 0px 0px"
                  @close="handleTagClose(items, index)"
                  >{{ items.display_name }}</el-tag
                >
              </el-collapse-item>
            </el-collapse>
          </div>
          <el-form
            :model="queryPoliceData"
            label-width="120px"
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
            :row-key="getRowKeys"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              :reserve-selection="true"
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
            <el-table-column prop="display_name" :label="$t('form.name')" />
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
            />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { getPolicie } from "@/api/iam/policie";
import { attachRoleAuthz } from "@/api/iam/authz";
import YamlEditor from "@/components/yaml/editor.vue";
var Base64 = require("js-base64").Base64;
export default {
  components: { YamlEditor },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    policieIDs: {
      type: Array,
      default: [],
    },
  },
  data() {
    return {
      multipleSelection: [],
      queryPoliceData: {
        name: "",
        page_size: 10,
        page_num: 1,
        total: 0,
      },
      authzForm: {
        policy_ids: [],
        role_id: "",
        project_id: "",
      },
      selNumber: 0,
      // 策略列表
      policieData: [],
      activeName: ["1"],
    };
  },
  watch: {
    multipleSelection: {
      deep: true,
      handler(val) {
        this.authzForm.policy_ids = [];
        val.forEach((element) => {
          this.authzForm.policy_ids.push(element.id);
        });
        this.selNumber = val.length;
      },
    },
  },
  created() {},
  mounted() {},
  methods: {
    getTitle() {
      return (
        this.$t("form.empowerPolicy") +
        "(" +
        this.selNumber +
        "/" +
        this.queryPoliceData.total +
        ")"
      );
    },
    // 策略列表
    async initPolicie() {
      const list = await getPolicie(this.queryPoliceData);
      this.policieData = list.policies;
      this.policieData.forEach((element) => {
        element.document = JSON.parse(element.document);
        element.rules = "";
        element.document.statement.forEach((el, index) => {
          index == element.document.statement.length - 1
            ? (el.rule = Base64.decode(el.rule))
            : (el.rule =
                Base64.decode(el.rule) +
                "\n" +
                "----------------------------------------------------------------------------------------------------------------------------------------------------" +
                "\n" +
                "\n");
          element.rules += el.rule;
        });
      });
      if (this.policieIDs.length > 0) {
        for (var i = 0; i < this.policieIDs.length; i++) {
          this.policieData = this.policieData.filter(
            (item) => item.id != this.policieIDs[i]
          );
        }
        this.queryPoliceData.total = list.total_num;
      } else {
        this.queryPoliceData.total = list.total_num;
      }
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
    handlePoliceSizeChange(val) {
      this.queryPoliceData.page_size = val;
      this.initPolicie();
    },
    handlePoliceCurrentChange(val) {
      this.queryPoliceData.page_num = val;
      this.initPolicie();
    },
    handleTagClose(item, index) {
      this.$refs.multipleTable.selection.forEach((element) => {
        element.id === this.multipleSelection[index].id
          ? this.$refs.multipleTable.toggleRowSelection(element, false)
          : this.$refs.multipleTable.toggleRowSelection(element, true);
      });
      // this.multipleSelection.splice(index, 1);
      // this.$refs.multipleTable.toggleRowSelection(item, false);
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 为指定角色授权
    AuthorzRoles(id, project_id) {
      this.authzForm.role_id = id;
      this.authzForm.project_id = project_id;
      if (this.authzForm.policy_ids.length > 0) {
        attachRoleAuthz(this.authzForm)
          .then((res) => {
            this.resetAuthorzForm();
            this.$notify({
              title: this.$t("message.authorizeSuccess"),
              type: "success",
              duration: 2500,
            });
          })
          .catch((err) => {});
      }
    },
    // 设置keys
    getRowKeys(row) {
      return row.id;
    },
    resetAuthorzForm() {
      this.$refs.multipleTable.clearSelection();
      this.authzForm = {
        policy_ids: [],
        role_id: "",
        project_id: "",
      };
    },
  },
};
</script>

<style scoped>
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

::v-deep .el-table__expanded-cell[class*="cell"] {
  padding: 20px 10px;
  background-color: #c2c9d03d !important;
}

::v-deep .el-table__expanded-cell:hover {
  background-color: #c2c9d03d !important;
}
</style>
