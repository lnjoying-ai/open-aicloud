<template>
  <div class="warpMain">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane :label="$t('form.containerMode')" name="container">
        <div class="itemMian">
          <div class="head_rq">
            <el-form
              :inline="true"
              size="small"
              :model="queryData"
              class="demo-form-inline"
            >
              <el-form-item>
                <div slot="label">{{ $t("form.deployName") }}:</div>
                <el-input
                  v-model="queryData.name"
                  :placeholder="$t('form.pleaseInput')"
                />
              </el-form-item>

              <el-form-item>
                <div slot="label">{{ $t("form.userName") }}:</div>
                <el-select
                  v-model="queryData.user_id"
                  filterable
                  style="width: 100%"
                  :placeholder="$t('form.pleaseSelect')"
                >
                  <el-option
                    v-for="(item, index) in userData"
                    :key="index"
                    :label="item.name"
                    :value="item.id"
                  />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button
                  icon="el-icon-search"
                  class="add_search"
                  type="primary"
                  @click="handleCurrentChange(1)"
                  >{{ $t("form.query") }}</el-button
                >
              </el-form-item>
              <el-form-item>
                <el-button class="add_search" @click="initform()">{{
                  $t("form.reset")
                }}</el-button>
              </el-form-item>

              <el-form-item style="float: right">
                <router-link to="/containerApplicationService/addcontainer">
                  <el-button type="primary">{{ $t("form.add") }}</el-button>
                </router-link>
              </el-form-item>
              <el-form-item style="float: right">
                <el-dropdown>
                  <el-button type="primary" :disabled="handledisabled">
                    {{ $t("form.batchOperation")
                    }}<i class="el-icon-arrow-down el-icon--right" />
                  </el-button>
                  <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item>
                      <el-button
                        style="width: 100px"
                        icon="el-icon-delete"
                        type="text"
                        @click="batchDelete"
                      >
                        {{ $t("form.delete") }}
                      </el-button>
                    </el-dropdown-item>
                  </el-dropdown-menu>
                </el-dropdown>
              </el-form-item>
            </el-form>
          </div>
          <el-table
            ref="multipleTableRef"
            stripe
            tooltip-effect="dark"
            :data="tableData.deployments"
            style="width: 100%"
            :default-sort="{ prop: 'date', order: 'descending' }"
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column
              :label="$t('form.deployName')"
              prop="name"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <router-link
                  style="color: #409eff"
                  :to="
                    '/containerApplicationService/deployContainer/' +
                    scope.row.id +
                    '/' +
                    scope.row.name
                  "
                >
                  {{ scope.row.name }}
                </router-link>
              </template>
            </el-table-column>

            <el-table-column
              :label="$t('form.imageName')"
              prop="image_name"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                {{ scope.row.image_name }}
              </template>
            </el-table-column>

            <el-table-column
              :label="$t('form.replicaNum')"
              prop="replica_num"
              sortable
            >
              <template slot-scope="scope">
                {{ scope.row.replica_num }}
              </template>
            </el-table-column>

            <el-table-column
              :label="$t('form.complete')"
              prop="ready_num"
              :render-header="renderHeadeTime"
            >
              <template slot-scope="scope">
                {{ scope.row.ready_num }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('form.available')"
              prop="available_num"
              :render-header="renderHeadeUse"
            >
              <template slot-scope="scope">
                {{ scope.row.available_num }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.user')" prop="user_name">
              <template slot-scope="scope">
                {{ scope.row.user_name }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.operation')" width="100">
              <template slot-scope="scope">
                <div class="czlb">
                  <el-popover placement="bottom" width="110" trigger="click">
                    <div class="icon_cz" @click="clickDelBtn(scope.row)">
                      <i class="el-icon-delete" />
                      {{ $t("form.delete") }}
                    </div>

                    <el-button
                      slot="reference"
                      icon="el-icon-more"
                      class="czbtn right_czbtn"
                    />
                  </el-popover>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="flex justify-end mt-4 px-4">
          <el-pagination
            :current-page="queryData.page_num"
            :page-sizes="[5, 10, 20, 50, 100]"
            :page-size="queryData.page_size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="tableData.total_num"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane :label="$t('form.applicationMode')" name="application">
        <div class="itemMian">
          <div class="head_rq">
            <el-form
              :inline="true"
              size="small"
              :model="queryData"
              class="demo-form-inline"
            >
              <el-row>
                <el-form-item>
                  <div slot="label">{{ $t("form.name") }}:</div>
                  <el-input
                    v-model="queryData.name"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>

                <el-form-item>
                  <div slot="label">{{ $t("form.userName") }}:</div>
                  <el-select
                    v-model="queryData.user_id"
                    style="width: 100%"
                    :placeholder="$t('form.pleaseSelect')"
                  >
                    <el-option
                      v-for="(item, index) in userData"
                      :key="index"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button
                    icon="el-icon-search"
                    class="add_search"
                    type="primary"
                    @click="handleCurrentChange(1)"
                    >{{ $t("form.query") }}</el-button
                  >
                </el-form-item>
                <el-form-item>
                  <el-button class="add_search" @click="initform()">{{
                    $t("form.reset")
                  }}</el-button>
                </el-form-item>
                <el-form-item style="float: right">
                  <el-button type="primary" @click="clickAddAppBtn">{{
                    $t("form.add")
                  }}</el-button>
                </el-form-item>
                <el-form-item style="float: right">
                  <router-link to="/containerApplicationService/appTemplate">
                    <el-button type="primary">{{
                      $t("form.templateAdd")
                    }}</el-button>
                  </router-link>
                </el-form-item>
                <el-form-item style="float: right">
                  <el-dropdown>
                    <el-button type="primary" :disabled="handledisabledApp">
                      {{ $t("form.batchOperation")
                      }}<i class="el-icon-arrow-down el-icon--right" />
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                      <el-dropdown-item>
                        <el-button
                          style="width: 100px"
                          icon="el-icon-delete"
                          type="text"
                          @click="batchApplicationDelete"
                        >
                          {{ $t("form.delete") }}
                        </el-button>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </el-dropdown>
                </el-form-item>
              </el-row>
            </el-form>
          </div>

          <el-table
            ref="multipleApplicationTableRef"
            stripe
            tooltip-effect="dark"
            :data="ApplicationtableData.deployments"
            style="width: 100%"
            :default-sort="{ prop: 'date', order: 'descending' }"
            @selection-change="handleApplicationChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column
              :label="$t('form.name')"
              prop="name"
              show-overflow-tooltip
            >
              <template slot-scope="scope">
                <router-link
                  style="color: #409eff"
                  :to="
                    '/containerApplicationService/deploySystemContainer/' +
                    scope.row.id +
                    '/' +
                    scope.row.name
                  "
                >
                  {{ scope.row.name }}
                </router-link>
              </template>
            </el-table-column>

            <el-table-column
              :label="$t('form.imageName')"
              prop="image_name"
              :formatter="imageData"
            />

            <el-table-column
              :label="$t('form.replicaNum')"
              prop="replica_num"
              sortable
            >
              <template slot-scope="scope">
                {{ scope.row.replica_num }}
              </template>
            </el-table-column>
            <el-table-column
              :label="$t('form.complete')"
              prop="ready_num"
              sortable
            >
              <template slot-scope="scope">
                {{ scope.row.ready_num }}
              </template>
            </el-table-column>

            <el-table-column
              :label="$t('form.available')"
              prop="available_num"
              sortable
            >
              <template slot-scope="scope">
                {{ scope.row.available_num }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.user')" prop="user_name">
              <template slot-scope="scope">
                {{ scope.row.user_name }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.bp')" prop="bp_name">
              <template slot-scope="scope">
                {{ scope.row.bp_name }}
              </template>
            </el-table-column>
            <!-- <el-table-column label="创建时间" prop="create_time" sortable /> -->
            <el-table-column :label="$t('form.operation')" width="100">
              <template slot-scope="scope">
                <div class="czlb">
                  <el-popover placement="bottom" width="110" trigger="click">
                    <div
                      v-if="
                        userInfo.kind == '0' ||
                        userInfo.kind == '1' ||
                        userInfo.kind == '2' ||
                        userInfo.kind == '4'
                      "
                      class="icon_cz"
                      @click="clickDelBtnSystem(scope.row)"
                    >
                      <i class="el-icon-delete" />
                      {{ $t("form.delete") }}
                    </div>

                    <el-button
                      slot="reference"
                      icon="el-icon-more"
                      class="czbtn right_czbtn"
                    />
                  </el-popover>
                </div>
              </template>
            </el-table-column>
          </el-table>
          <addApp ref="addApp" :sup_this="sup_this" :info="activeInfoApp" />
          <el-dialog
            :title="$t('form.applicationConfig')"
            :visible.sync="dialogVisible"
            width="800px"
            :before-close="handleClose"
            :close-on-click-modal="false"
          >
            <div>
              <el-form label-width="130px">
                <el-form-item label="justice_compse:">
                  <yaml-editor
                    v-if="dialogVisible"
                    ref="yamlEditor"
                    v-model="appConfig.justice_compse"
                    style="
                      line-height: 1.2;
                      max-height: 350px;
                      overflow: auto;
                      margin-top: 15px;
                      border-radius: 4px;
                      min-width: 100%;
                    "
                    :readonly="'nocursor'"
                  />
                </el-form-item>
                <el-form-item label="stack_compose:">
                  <yaml-editor
                    v-if="dialogVisible"
                    ref="yamlEditor"
                    v-model="appConfig.stack_compose"
                    style="
                      line-height: 1.2;
                      max-height: 350px;
                      overflow: auto;
                      margin-top: 15px;
                      border-radius: 4px;
                      min-width: 100%;
                    "
                    :readonly="'nocursor'"
                  />
                </el-form-item>
              </el-form>
            </div>
          </el-dialog>
        </div>
        <div class="flex justify-between mt-4 px-4">
          <div>
            <!-- <el-button type="primary" @click="clickAddAppBtn" style="margin-right:20px">添加</el-button> -->
          </div>
          <el-pagination
            :current-page="queryData.page_num"
            :page-sizes="[5, 10, 20, 50, 100]"
            :page-size="queryData.page_size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="ApplicationtableData.total_num"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
    <!--批量操作弹框-->
    <el-dialog
      :visible.sync="batchVisible"
      width="800px"
      :before-close="batchHandleClose"
      :close-on-click-modal="false"
    >
      <div v-if="nothingness">
        <h2 style="padding-bottom: 10px">
          <i class="el-icon-warning-outline" style="color: #fbb561">{{
            $t("form.unOperableContainer")
          }}</i>
        </h2>
      </div>
      <span v-if="batchType == 'container'">
        <el-table
          :data="batchData"
          style="width: 100%"
          :cell-style="cellStyle"
          tooltip-effect="dark"
        >
          <el-table-column
            prop="id"
            :label="$t('form.containerId')"
            show-overflow-tooltip
          />
          <el-table-column
            prop="name"
            :label="$t('form.containerName')"
            show-overflow-tooltip
          />
          <el-table-column :label="$t('form.status')">
            <template slot-scope="scope">
              <div
                v-if="scope.row.ready_num == scope.row.replica_num"
                class="success"
              >
                <span>{{ $t("form.deployComplete") }}</span>
              </div>
              <div
                v-else-if="scope.row.failed_num == scope.row.replica_num"
                class="error"
              >
                <span>{{ $t("form.deployFailed") }}</span>
              </div>
              <span v-else class="creating">
                <span>{{ $t("form.deploying") }}</span>
              </span>
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.operationStatus')">
            <template slot-scope="scope">
              <div>
                <span
                  v-if="
                    scope.row.operationStatus == 'OperableStatusNotOperable'
                  "
                >
                  {{ $t("form.notOperable") }}
                </span>
                <span
                  v-if="scope.row.operationStatus == 'OperableStatusNormal'"
                >
                  {{ $t("form.normal") }}
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column fixed="right" width="120">
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click.native.prevent="deleteRow(scope.$index, batchData)"
              >
                {{ $t("form.cancelSelect") }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </span>
      <span v-else>
        <el-table
          :data="batchData"
          style="width: 100%"
          :cell-style="cellStyle"
          tooltip-effect="dark"
        >
          <el-table-column
            prop="id"
            :label="$t('form.applicationId')"
            show-overflow-tooltip
          />
          <el-table-column
            prop="name"
            :label="$t('form.applicationName')"
            show-overflow-tooltip
          />
          <el-table-column :label="$t('form.operationStatus')">
            <template slot-scope="scope">
              <div>
                <span
                  v-if="
                    scope.row.operationStatus == 'OperableStatusNotOperable'
                  "
                >
                  {{ $t("form.notOperable") }}
                </span>
                <span
                  v-if="scope.row.operationStatus == 'OperableStatusNormal'"
                >
                  {{ $t("form.normal") }}
                </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            :label="$t('form.operation')"
            width="120"
          >
            <template slot-scope="scope">
              <el-button
                type="text"
                size="small"
                @click.native.prevent="deleteRow(scope.$index, batchData)"
              >
                {{ $t("form.cancelSelect") }}
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="batchHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" @click="batchVisibleClick()">{{
          $t("form.confirm")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getDeploys,
  getUsers,
  getApplicationDeploys,
  delDeployContainer,
  delDeploySystem,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addApp from "./application/addForm";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: {
    addApp,
    YamlEditor,
  },
  mixins: [initData],
  data() {
    return {
      queryStatus: "",
      batchData: [],
      nowMouse: "",
      activeName: "container",
      activeInfoApp: "",
      dialogVisible: false,
      appConfig: "",
      sup_this: this,
      handledisabled: true,
      handledisabledApp: true,
      // 容器模式下表格数据
      tableData: {},
      // 应用模式下表格数据
      ApplicationtableData: {},
      queryData: {
        name: "",
        status: "",
        user_id: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      },
      userData: [],

      // 选择的行
      multipleSelection: [],
      multipleApplicationSelection: [],
      // 批量操作
      batch: "",
      // 批量操作标题
      batchTitle: "",
      // 批量操作显示
      batchVisible: false,
      // 批量操作类型
      batchType: "",
      // 批量删除数据
      batchData: [],
      // 批量操作提示
      nothingness: false,
      // 失败的数据
      errorData: [],
    };
  },
  watch: {
    nowText(val) {},
    "queryData.user_id": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.queryData.page_num = 1;
          if (this.activeName == "container") {
            this.init();
          } else {
            this.applicationInit();
          }
        }
      },
    },
  },
  created() {
    this.init();
    this.userinit();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  methods: {
    handleStausChange(value) {
      if (value == 0) {
        this.queryData.status = "";
      } else {
        this.queryData.status = value;
      }
      if (this.activeName == "container") {
        this.init();
      } else {
        this.applicationInit();
      }
    },
    // 批量删除弹框取消调用
    batchHandleClose() {
      this.batchVisible = false;
      this.batchData = [];
      this.batchApplicationData = [];
      this.batchType = "";
      this.batchTitle = "";
      this.nothingness = false;
      this.errorData = [];
      this.$refs.multipleTableRef.clearSelection();
      this.$refs.multipleApplicationTableRef.clearSelection();
    },
    // 单元格样式设置
    cellStyle(row, column, rowIndex, columnIndex) {
      if (row.row.operationStatus == "OperableStatusNotOperable") {
        return "color:red";
      }
    },
    // 移除选中行
    deleteRow(index, rows) {
      rows.splice(index, 1);
      if (this.batchData.length > 0) {
        for (let i = 0; i < this.batchData.length; i++) {
          if (
            this.batchData[i].operationStatus.indexOf(
              "OperableStatusNotOperable"
            ) != -1
          ) {
            this.nothingness = true;
          } else {
            this.nothingness = false;
          }
        }
      } else {
        this.nothingness = false;
      }
    },
    // 批量删除
    batchDelete() {
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectContainer"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        this.batchType = "container";
        this.batchTitle = this.$t("form.deployContainerBatchDelete");
        for (let i = 0; i < this.batchData.length; i++) {
          this.batchData[i].operationStatus = "OperableStatusNormal";
        }
      }
      this.batchVisible = true;
    },
    // 批量删除
    batchApplicationDelete() {
      if (this.multipleApplicationSelection.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseSelectApplication"),
          type: "warning",
          duration: 2500,
        });
        return;
      } else {
        this.batchType = "Application";
        this.batchTitle = this.$t("form.deployApplicationBatchDelete");
        for (let i = 0; i < this.batchData.length; i++) {
          this.batchData[i].operationStatus = "OperableStatusNormal";
        }
      }
      this.batchVisible = true;
    },
    // 批量操作调用的方法
    batchVisibleClick() {
      this.errorData = [];
      if (this.nothingness === true) {
        this.$notify({
          title: this.$t("form.pleaseCancelUnOperableData"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.batchData.length === 0) {
        this.$notify({
          title: this.$t("form.noOperationOption"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.batchType == "container") {
        this.$confirm(
          this.$t("form.confirmBatchDeleteContainer"),
          this.$t("form.tip"),
          {
            confirmButtonText: this.$t("form.confirm"),
            cancelButtonText: this.$t("form.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            for (let i = 0; i < this.batchData.length; i++) {
              delDeployContainer(this.batchData[i].id)
                .then((res) => {
                  if (i == this.batchData.length - 1) {
                    this.$refs.multipleTableRef.clearSelection();
                    this.$notify({
                      title: this.$t("form.batchDeleteSuccess"),
                      type: "success",
                      duration: 2500,
                    });
                    this.init();
                    this.batchVisible = false;
                  }
                })
                .catch((err) => {
                  this.errorData.push(this.batchData[i].name);
                  if (this.errorData.length > 0) {
                    for (let j = 0; j < this.errorData.length; j++) {
                      if (j == this.batchData.length - 1) {
                        this.$notify({
                          title: this.$t(
                            "form.batchOperationFailedWithContainerName",
                            { name: this.errorData.join(",") }
                          ),
                          type: "error",
                          duration: 2500,
                        });
                      }
                    }
                  }
                });
            }
          })
          .catch(() => {});
      } else {
        this.$confirm(
          this.$t("form.confirmBatchDeleteApplication"),
          this.$t("form.tip"),
          {
            confirmButtonText: this.$t("form.confirm"),
            cancelButtonText: this.$t("form.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            for (let i = 0; i < this.batchData.length; i++) {
              delDeploySystem(this.batchData[i].id)
                .then((res) => {
                  if (i == this.batchData.length - 1) {
                    this.$refs.multipleApplicationTableRef.clearSelection();
                    this.$notify({
                      title: this.$t("form.batchDeleteSuccess"),
                      type: "success",
                      duration: 2500,
                    });
                    this.applicationInit();
                    this.batchVisible = false;
                  }
                })
                .catch((err) => {
                  this.errorData.push(this.batchData[i].name);
                  if (this.errorData.length > 0) {
                    for (let j = 0; j < this.errorData.length; j++) {
                      if (j == this.batchData.length - 1) {
                        this.$notify({
                          title: this.$t(
                            "form.batchOperationFailedWithContainerName",
                            { name: this.errorData.join(",") }
                          ),
                          type: "error",
                          duration: 2500,
                        });
                      }
                    }
                  }
                });
            }
          })
          .catch(() => {});
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.batchData = val;
      if (this.multipleSelection.length > 0) {
        this.handledisabled = false;
      } else {
        this.handledisabled = true;
      }
    },
    handleApplicationChange(val) {
      this.multipleApplicationSelection = val;
      this.batchData = val;
      if (this.multipleApplicationSelection.length > 0) {
        this.handledisabledApp = false;
      } else {
        this.handledisabledApp = true;
      }
    },
    renderHeadeTime(h, { column, $index }) {
      // h 是一个渲染函数       column 是一个对象表示当前列      $index 第几列
      return h("div", [
        h("span", column.label + "  ", {
          align: "center",
          marginTop: "10px",
        }),
        h(
          "el-popover",
          {
            props: {
              placement: "top-start", // 一般 icon 处可添加浮层说明，浮层位置等属性
              width: "260",
              trigger: "hover",
            },
          },
          [
            h("p", this.$t("form.containerDeployed"), {
              class: "text-align: center; margin: 0;font-size:12px;",
            }),
            h("i", {
              // 生成 i 标签 ，添加icon 设置 样式，slot 必填
              class: "el-icon-info",
              style: "color:#ccc,margin:18px,padding-top:10px",
              slot: "reference",
            }),
          ]
        ),
      ]);
    },
    renderHeadeUse(h, { column, $index }) {
      // h 是一个渲染函数       column 是一个对象表示当前列      $index 第几列
      return h("div", [
        h("span", column.label + "  ", {
          align: "center",
          marginTop: "10px",
        }),
        h(
          "el-popover",
          {
            props: {
              placement: "top-start", // 一般 icon 处可添加浮层说明，浮层位置等属性
              width: "200",
              trigger: "hover",
            },
          },
          [
            h("p", this.$t("form.runningContainer"), {
              class: "text-align: center; margin: 0;font-size:12px;",
            }),
            h("i", {
              // 生成 i 标签 ，添加icon 设置 样式，slot 必填
              class: "el-icon-info",
              style: "color:#ccc,margin:18px,padding-top:10px",
              slot: "reference",
            }),
          ]
        ),
      ]);
    },
    imageData(row) {
      if (
        row.image_names != null &&
        row.image_names.length != undefined &&
        row.image_names.length > 0
      ) {
        return row.image_names.join("\n"); // 将有分隔符的颜色数组返回
      }
    },
    async userinit() {
      var list = await getUsers();
      this.userData = list.users;
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      if (this.activeName == "container") {
        this.init();
      } else {
        this.applicationInit();
      }
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      if (this.activeName == "container") {
        this.init();
        this.$refs.multipleApplicationTableRef.clearSelection();
      } else {
        this.applicationInit();
        this.$refs.multipleTableRef.clearSelection();
      }
    },
    handleClick(tab, event) {
      this.initform();
    },
    handleClose() {
      this.dialogVisible = false;
      this.appConfig = "";
    },
    async init() {
      const list = await getDeploys(this.queryData);
      this.tableData = list;
      this.queryData.total_num = list.total_num;
    },
    async applicationInit() {
      const list = await getApplicationDeploys(this.queryData);
      this.ApplicationtableData = list;
      this.queryData.total_num = list.total_num;
    },
    initform() {
      this.queryData = {
        name: "",
        status: "",
        user_id: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      };
      this.queryStatus = "";
      this.handleCurrentChange(1);
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("form.confirmDeleteContainer"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delDeployContainer(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickDelBtnSystem(value) {
      this.$confirm(
        this.$t("form.confirmDeleteCopyOfApplication"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delDeploySystem(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.applicationInit();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickAddAppBtn(item) {
      this.$refs.addApp.isAdd = true;
      this.$refs.addApp.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
.creating {
  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 8px;
    height: 8px;
    background: #0079d1;
    border-radius: 50%;
  }
}

.error {
  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 8px;
    height: 8px;
    background: #d1200d;
    border-radius: 50%;
  }
}

.normal1 {
  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 8px;
    height: 8px;
    background: #ffbc00;
    border-radius: 50%;
  }
}

.success {
  &::before {
    display: inline-block;
    margin-right: 8px;
    content: "";
    width: 8px;
    height: 8px;
    background: #00ab5c;
    border-radius: 50%;
  }
}

svg {
  display: inline-block;
  vertical-align: middle;
  margin-left: -10px;
  margin-right: -1px;
}

::v-deep .el-dropdown-menu__item {
  padding: 0px 33px;
}

// ::v-deep .el-table .cell.el-tooltip {
//   white-space: pre-wrap;
//   min-width: 50px;
// }
.el-popover {
  min-width: 0px !important;
  padding: 0px !important;
}

// ::v-deep .el-table .cell {
//   white-space: pre-line;
// }
::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

.itemMian {
  overflow: hidden;
}

.icon_cz {
  display: block;
  height: 24px;
  color: #666666;
  align-items: center;
  cursor: pointer;
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 24px;
  text-align: center;
}

.icon_cz:hover {
  background: #daefff;
}

.cz_icon {
  margin-left: 16px;
  margin-right: 23px;
}

.czbtn .czlb {
  display: flex;
  align-items: center;
}

.czbtn {
  width: 23px;
  height: 26px;
  border: 1px solid #d0d0d0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_czbtn {
  border-radius: 3px 0 0 3px;
  border-right: 0px;
  cursor: pointer;
}

.left_czbtn img {
  height: 11px;
}

.right_czbtn {
  border-radius: 3px;
}

.icon_cz {
  display: block;
  height: 24px;
  color: #666666;
  align-items: center;
  cursor: pointer;
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 24px;
}

.icon_cz:hover {
  background: #daefff;
}

.cz_icon {
  margin-left: 16px;
  margin-right: 23px;
}

::v-deep .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
  // background-color: transparent;
  z-index: 1;
}

.el-dropdown-menu__item {
  padding: 0px 0;
}
</style>
