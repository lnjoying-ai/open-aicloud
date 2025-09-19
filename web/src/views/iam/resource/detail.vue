<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="detailMain"
        :rules="rules"
        size="small"
        label-width="140px"
      >
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.name') + ':'"
          prop="name"
        >
          <el-input
            v-model="detailMain.name"
            :placeholder="$t('form.pleaseInputResourceName')"
          />
        </el-form-item>
        <el-form-item
          v-else
          :label="$t('form.name') + ':'"
          style="width: 50%; float: left"
        >
          <span> {{ detailMain.name ? detailMain.name : "-" }}</span>
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.resourceService') + ':'"
        >
          <el-cascader
            v-model="detailMain.service_id"
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
        <el-form-item
          v-else
          :label="$t('form.resourceService') + ':'"
          style="width: 50%; float: left"
        >
          <span>
            {{ detailMain.service_name ? detailMain.service_name : "-" }}</span
          >
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.description') + ':'"
        >
          <el-input
            v-model="detailMain.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputResourceDescription')"
          />
        </el-form-item>
        <el-form-item
          v-else
          :label="$t('form.description') + ':'"
          style="width: 100%; float: left"
        >
          <span>
            {{ detailMain.description ? detailMain.description : "-" }}</span
          >
        </el-form-item>
        <el-form-item
          :label="$t('form.resourceNumber') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ detailMain.lrn ? detailMain.lrn : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.resourceType') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.resource_type ? detailMain.resource_type : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.resourceCondition') + ':'"
          style="width: 50%; float: left"
        >
          <span v-for="(item, index) in detailMain.conditions" :key="index">
            <el-tooltip
              class="item"
              effect="dark"
              :content="item"
              placement="top-start"
            >
              {{ item ? item : "-" }}
            </el-tooltip>
          </span>
        </el-form-item>
        <el-form-item
          :label="$t('form.operationCount') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.action_total ? detailMain.action_total : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.attributeCount') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ detailMain.attr_total ? detailMain.attr_total : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.conditionCount') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.condition_total ? detailMain.condition_total : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.createTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.create_time ? detailMain.create_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.modifyTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.update_time ? detailMain.update_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button
            v-if="userInfo.kind == 0 || userInfo.kind == 1"
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
      <el-tabs v-model="activeName">
        <el-tab-pane :label="$t('form.attributeList')" name="first">
          <el-table
            :data="attributeData"
            style="width: 100%"
            max-height="530"
            stripe
            tooltip-effect="dark"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column
              prop="attrName"
              :label="$t('form.attributeName')"
            />
            <el-table-column
              prop="attrType"
              :label="$t('form.attributeType')"
            />
            <el-table-column
              prop="description"
              :label="$t('form.description')"
            />
            <el-table-column
              prop="createTime"
              :label="$t('form.createTime')"
              sortable
            >
              <template slot-scope="scope">
                {{
                  scope.row.createTime ? formatDate(scope.row.createTime) : "-"
                }}
              </template>
            </el-table-column>

            <el-table-column
              prop="updateTime"
              :label="$t('form.updateTime')"
              sortable
            >
              <template slot-scope="scope">
                {{
                  scope.row.updateTime ? formatDate(scope.row.updateTime) : "-"
                }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <el-tab-pane :label="$t('form.operationList')" name="second">
          <el-table
            :data="operateData"
            style="width: 100%"
            max-height="530"
            stripe
            tooltip-effect="dark"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column type="expand">
              <template slot-scope="props">
                <yaml-editor
                  ref="yamlEditor"
                  v-model="props.row.associatedApis"
                  class="leading-tight overflow-auto rounded max-h-96 min-w-full"
                  :download-name="$t('form.associatedApis')"
                  :download-type="'yml'"
                  :readonly="'true'"
                  :lint="false"
                />
              </template>
            </el-table-column>
            <el-table-column prop="actionName" :label="$t('form.actionName')" />
            <el-table-column prop="actionType" :label="$t('form.actionType')">
              <template slot-scope="scope">
                <div v-if="scope.row.actionType == 1">list</div>
                <div v-if="scope.row.actionType == 2">write</div>
                <div v-if="scope.row.actionType == 3">read</div>
              </template>
            </el-table-column>
            <el-table-column prop="description" :label="$t('form.description')">
              <template slot-scope="scope">
                {{ scope.row.description ? scope.row.description : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              prop="createTime"
              :label="$t('form.createTime')"
              sortable
            >
              <template slot-scope="scope">
                {{
                  scope.row.createTime ? formatDate(scope.row.createTime) : "-"
                }}
              </template>
            </el-table-column>

            <el-table-column
              prop="updateTime"
              :label="$t('form.updateTime')"
              sortable
            >
              <template slot-scope="scope">
                {{
                  scope.row.updateTime ? formatDate(scope.row.updateTime) : "-"
                }}
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>
<script>
import { editResource } from "@/api/iam/resource";
import { getService } from "@/api/iam/serve";
import { mapGetters } from "vuex";
import { detailResource } from "@/api/iam/resource";
import YamlEditor from "@/components/yaml/editor.vue";
export default {
  components: { YamlEditor },
  data() {
    return {
      detailMain: {},
      attributeData: [],
      operateData: [],
      // 服务列表
      tableData: [],
      loadingForm: false,
      activeName: "first",
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

  async created() {
    this.init();
    this.getServiceList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    async init() {
      const list = await detailResource(this.$route.params.id);
      this.detailMain = list;
      this.attributeData = list.attrs;
      this.operateData = list.actions;
    },
    async getServiceList() {
      const list = await getService({
        page_size: 99999,
        page_num: 1,
      });
      this.tableData = list.services;
    },
    doEdit() {
      this.loadingForm = true;
      editResource(this.$route.params.id, this.detailMain)
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
        });
    },
    // 时间戳转换时间
    formatDate(date) {
      var date = new Date(date);
      var YY = date.getFullYear() + "-";
      var MM =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hh =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var mm =
        (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
        ":";
      var ss =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return YY + MM + DD + " " + hh + mm + ss;
    },
  },
};
</script>
<style scoped>
.yaml-editor {
  height: 100%;
  position: relative;
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
