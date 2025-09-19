<template>
  <div class="warpMain">
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
                :placeholder="$t('form.pleaseInputName')"
              />
            </el-form-item>

            <el-form-item v-if="userInfo.kind == '0' || userInfo.kind == '1'">
              <div slot="label">{{ $t("form.userName") }}:</div>
              <el-select
                v-model="queryData.user_id"
                filterable
                clearable
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
              <el-button class="add_search" @click="initform()">
                {{ $t("form.reset") }}
              </el-button>
            </el-form-item>
            <el-form-item
              v-if="
                userInfo.kind == '0' ||
                userInfo.kind == '1' ||
                userInfo.kind == '2' ||
                userInfo.kind == '4'
              "
              style="float: right"
            >
              <el-button type="primary" @click="clickAddAppBtn">
                {{ $t("form.add") }}
              </el-button>
            </el-form-item>
            <el-form-item
              v-if="
                userInfo.kind == '0' ||
                userInfo.kind == '1' ||
                userInfo.kind == '2' ||
                userInfo.kind == '4'
              "
              style="float: right"
            >
              <router-link to="/containerApplicationService/appTemplate">
                <el-button type="primary">
                  {{ $t("form.templateAdd") }}
                </el-button>
              </router-link>
            </el-form-item>
            <el-form-item style="float: right">
              <el-dropdown>
                <el-button type="primary" :disabled="handledisabledApp">
                  {{ $t("form.batchOperation") }}
                  <i class="el-icon-arrow-down el-icon--right" />
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
          show-overflow-tooltip
        />
        <el-table-column
          :label="$t('form.replicaNum')"
          prop="replica_num"
          sortable
          width="110"
        >
          <template slot-scope="scope">
            {{ scope.row.replica_num }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('form.complete')"
          prop="ready_num"
          sortable
          width="90"
        >
          <template slot-scope="scope">
            {{ scope.row.ready_num }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('form.available')"
          prop="available_num"
          sortable
          width="90"
        >
          <template slot-scope="scope">
            {{ scope.row.available_num }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.user')" prop="user_name">
          <template slot-scope="scope">
            {{ scope.row.user_name || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.organization')" prop="bp_name">
          <template slot-scope="scope">
            {{ scope.row.bp_name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          :label="$t('form.createTime')"
          sortable
        />
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
    </div>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="queryData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="queryData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
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

      <span v-if="batchType == 'Application'">
        <el-table
          :data="batchData"
          style="width: 100%"
          :cell-style="cellStyle"
          tooltip-effect="dark"
        >
          <el-table-column
            prop="name"
            :label="$t('form.applicationName')"
            show-overflow-tooltip
          />
          <el-table-column
            :label="$t('form.imageName')"
            prop="image_name"
            :formatter="imageData"
          />
          <el-table-column :label="$t('form.user')" prop="user_name">
            <template slot-scope="scope">
              {{ scope.row.user_name || "-" }}
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
          <el-table-column fixed="right" :label="$t('form.operation')">
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
  getUsers,
  getApplicationDeploys,
  delDeploySystem,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addApp from "../application/addForm";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: {
    addApp,
    YamlEditor,
  },
  mixins: [initData],
  data() {
    return {
      batchData: [],
      activeInfoApp: "",
      appConfig: "",
      sup_this: this,
      handledisabledApp: true,
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
    "queryData.user_id": {
      deep: true,
      handler(val) {
        this.queryData.user_id = val;
        this.queryData.page_num = 1;
        this.applicationInit();
      },
    },
  },
  created() {
    this.applicationInit();
    this.userinit();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  methods: {
    // 批量删除弹框取消调用
    batchHandleClose() {
      this.batchVisible = false;
      this.batchData = [];
      this.batchApplicationData = [];
      this.batchType = "";
      this.batchTitle = "";
      this.nothingness = false;
      this.errorData = [];
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
        this.batchTitle = this.$t("form.batchDeleteApplication");
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
          title: this.$t("form.noOperableOption"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.batchType == "Application") {
        this.$confirm(
          this.$t("form.confirmDeleteApplication"),
          this.$t("form.prompt"),
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
                          title: this.$t("form.batchOperationFailed", {
                            name: this.errorData.join(","),
                          }),
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

    handleApplicationChange(val) {
      this.multipleApplicationSelection = val;
      this.batchData = val;
      if (this.multipleApplicationSelection.length > 0) {
        this.handledisabledApp = false;
      } else {
        this.handledisabledApp = true;
      }
    },

    imageData(row) {
      if (
        row.image_names != null &&
        row.image_names.length != undefined &&
        row.image_names.length > 0
      ) {
        return row.image_names.join("\n");
      } else {
        return "-";
      }
    },
    async userinit() {
      var list = await getUsers();
      this.userData = list.users;
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;

      this.applicationInit();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.applicationInit();
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
      this.handleCurrentChange(1);
    },

    clickDelBtnSystem(value) {
      this.$confirm(
        this.$t("form.confirmDeleteAllApplication"),
        this.$t("form.prompt"),
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
svg {
  display: inline-block;
  vertical-align: middle;
  margin-left: -10px;
  margin-right: -1px;
}

::v-deep .el-tooltip__popper.is-dark {
  background: #303133;
  color: #fff;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: pre-line;
}

.itemMian {
  overflow: hidden;
}

::v-deep .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
  z-index: 1;
}

.el-dropdown-menu__item {
  padding: 0px 0;
}
</style>
