<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-row :gutter="12">
          <el-col :span="24">
            <el-form-item>
              <div slot="label">{{ $t("form.name") }}</div>
              <el-input
                v-model="queryData.name"
                :placeholder="$t('form.pleaseInput')"
                style="width: 180px"
              />
            </el-form-item>
            <el-form-item>
              <div slot="label">{{ $t("form.owner") }}</div>
              <el-select
                v-model="queryData.owner"
                filterable
                style="width: 100%"
                :placeholder="$t('form.pleaseSelect')"
                @change="handleChangeUser"
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
                size="small"
                icon="el-icon-search"
                class="add_search"
                type="primary"
                @click="handleCurrentChange(1)"
                >{{ $t("form.query") }}</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-button size="small" class="add_search" @click="restInit(1)">{{
                $t("form.reset")
              }}</el-button>
            </el-form-item>
            <el-form-item style="float: right">
              <el-button type="primary" size="small" @click="clickAddBtn">{{
                $t("form.add")
              }}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-row :gutter="12">
      <el-col :span="24">
        <el-table
          ref="multipleTable"
          :data="tableData.clusters"
          stripe
          tooltip-effect="dark"
          style="width: 100%"
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column prop="name" :label="$t('form.clusterName')">
            <template slot-scope="scope">
              <router-link
                style="color: #409eff"
                :to="
                  '/serverCluster/detail/' + scope.row.id + '/' + scope.row.name
                "
              >
                <span>{{ scope.row.name }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.status')">
            <template slot-scope="scope">
              <el-tag
                :size="$store.state.nextStack.viewSize.tagStatus"
                :type="
                  filtersFun.getClusterStatus(scope.row.status.code, 'tag')
                "
                >{{
                  filtersFun.getClusterStatus(scope.row.status.code, "status")
                }}</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.createType')">
            <template slot-scope="scope">
              <div>{{ scope.row.create_type }}</div>
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.clusterVersion')">
            <template slot-scope="scope">
              <div
                v-if="
                  scope.row.cluster_type == 'k8s' &&
                  scope.row.jke_config != null
                "
              >
                {{ scope.row.jke_config.k8s_version }}
              </div>
              <div
                v-if="
                  scope.row.cluster_type == 'k3s' &&
                  scope.row.k3s_config != null
                "
              >
                {{ scope.row.k3s_config.k3s_version }}
              </div>
              <div v-if="scope.row.create_type == 'import'">
                <span style="text-align: center">{{ "-" }}</span>
              </div>
            </template>
          </el-table-column>
          <el-table-column
            prop="cluster_type"
            :label="$t('form.clusterType')"
          />
          <el-table-column prop="owner_name" :label="$t('form.owner')" />

          <el-table-column :label="$t('form.operation')" width="100">
            <template slot-scope="scope">
              <div class="czlb">
                <el-popover placement="bottom" width="110" trigger="click">
                  <div class="icon_cz" @click="clickDelBtn(scope.row)">
                    <i class="el-icon-delete" />
                    {{ $t("form.delete") }}
                  </div>

                  <div
                    v-if="scope.row.create_type != 'import'"
                    class="icon_cz"
                    @click="clickExportBtn(scope.row)"
                  >
                    <i class="el-icon-refresh" />
                    {{ $t("form.exportClusterTemplate") }}
                  </div>
                  <div
                    v-if="scope.row.status.code == 11"
                    class="icon_cz"
                    @click="clickRefreshBtn(scope.row)"
                  >
                    <i class="el-icon-refresh-right" />
                    {{ $t("form.reinstall") }}
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
          <el-table-column width="125">
            <template slot-scope="scope">
              <el-link
                v-if="scope.row.online_state == 1"
                type="primary"
                :underline="false"
                :href="scope.row.dashboard_url"
              >
                <el-button size="mini" :disabled="scope.row.online_state != 1">
                  Dashboard
                </el-button>
              </el-link>
              <el-link
                v-if="scope.row.online_state == 1"
                type="primary"
                :underline="false"
              >
                <el-button size="mini" :disabled="true"> Dashboard </el-button>
              </el-link>
              <el-link
                v-if="scope.row.online_state != 1"
                type="info"
                :underline="false"
              >
                <el-button size="mini" :disabled="scope.row.online_state != 1">
                  Dashboard
                </el-button>
              </el-link>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
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
    <el-dialog
      :title="$t('form.exportTemplate')"
      :before-close="handleClosetemplate"
      :visible.sync="dialogFormVisible"
      width="800px"
    >
      <el-form ref="templateFormref" :model="templateFrom" label-width="80px">
        <el-form-item
          :label="$t('form.templateName') + ':'"
          prop="template_name"
          :rules="[
            {
              required: true,
              validator: validate.k8sName,
              trigger: ['blur', 'change'],
            },
          ]"
        >
          <el-input v-model="templateFrom.template_name" autocomplete="off" />
        </el-form-item>
        <el-form-item :label="$t('form.templateDesc') + ':'">
          <el-input
            v-model="templateFrom.template_desc"
            type="textarea"
            maxlength="255"
            show-word-limit
          />
        </el-form-item>
        <el-form-item :label="$t('form.version') + ':'">
          <el-input
            v-model="templateFrom.template_version"
            autocomplete="off"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.overwrite') + ':'"
          class="formContentBlock"
        >
          <el-switch v-model="templateFrom.overwrite" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="handleClosetemplate">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="toExportBtn()"
          >{{ $t("form.export") }}</el-button
        >
      </div>
    </el-dialog>
    <!--集群导入命令开始-->
    <el-dialog
      :title="$t('form.importClusterCommand')"
      :visible.sync="dialogVisible"
      width="800px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <span>{{ $t("form.certificateEnvironmentCommand") }}:</span>
      <el-row :gutter="20">
        <el-col :span="19">
          <el-input
            id="copyCertificates"
            v-model="certificate"
            type="textarea"
            readonly
            style="margin: 15px 0px"
            size="large"
          />
        </el-col>
        <el-col :span="1">
          <el-button
            icon="el-icon-document-copy"
            type="primary"
            style="margin: 15px -17px; height: 80px; font-size: 20px"
            @click="copyCertificateclick"
          />
        </el-col>
      </el-row>

      <span>{{ $t("form.noCertificateEnvironmentCommand") }}:</span>
      <el-row :gutter="20">
        <el-col :span="19">
          <el-input
            id="copyNoCertificates"
            v-model="noCertificate"
            type="textarea"
            readonly
            style="margin: 15px 0px"
            size="large"
          />
        </el-col>
        <el-col :span="1">
          <el-button
            icon="el-icon-document-copy"
            type="primary"
            style="margin: 15px -17px; height: 80px; font-size: 20px"
            @click="copyNoCertificateClick"
          />
        </el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" @click="dialogVisible = false">{{
          $t("form.confirm")
        }}</el-button>
      </span>
    </el-dialog>
    <!--集群导入命令结束-->
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getUsers,
  getClusters,
  delClusters,
  exportClusterTemplates,
  importCmds,
  clustersRepair,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import validate from "@/utils/validate";
import filtersFun from "@/utils/statusFun";
export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      filtersFun: filtersFun,
      loading: false,
      validate: validate,
      dialogVisible: false, // 导入弹框
      dialogFormVisible: false, // 导出模板弹窗
      dialogkubeconfigVisible: false, // kubiconfig
      certificate: "", // 有证书命令
      noCertificate: "", // 无证书命令
      // 用户数据
      userData: [],
      templateFrom: {
        template_name: "",
        template_desc: "",
        template_version: "v1",
        overwrite: false,
      },
      nowClsData: "", // 当前选中的集群
      sup_this: this,
      tableData: {},
      queryData: {
        name: "", // 集群名
        owner: "", // 拥有者名称
        shared_user: "", // 共享的用户
        page_size: 10,
        page_num: 1,
      },
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  watch: {
    dialogkubeconfigVisible(val) {
      if (!val) {
        this.nowClsData = "";
      }
    },
    dialogFormVisible(val) {
      if (!val) {
        this.nowClsData = "";
      }
    },
  },
  mounted() {},
  created() {
    this.init();
    this.userinit();
  },
  methods: {
    // 切换路由详情
    handleClickRouter(value) {
      if (value.cluster_type == "k3s") {
      } else if (value.cluster_type == "k8s") {
        this.$router.push(
          "/serverCluster/detail/" + value.id + "/" + value.name
        );
      }
    },
    async userinit() {
      var list = await getUsers();
      this.userData = list.users;
    },
    // 关闭导入弹框
    handleClose() {
      this.dialogVisible = false;
    },
    handleClosetemplate() {
      this.dialogFormVisible = false;
      (this.templateFrom = {
        template_name: "",
        template_desc: "",
        template_version: "v1",
        overwrite: false,
      }),
        this.$refs["templateFormref"].resetFields();
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleChangeUser(val) {
      this.queryData.owner = val;
      this.init();
    },
    restInit(val) {
      this.queryData.name = "";
      this.queryData.owner = "";
      this.queryData.shared_user = "";
      this.queryData.page_num = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getClusters(this.queryData);
      this.tableData = list;
    },
    // 获取导入集群命令
    async importCmds(clusterId) {
      if (clusterId != "" && clusterId != undefined) {
        const list = await importCmds(clusterId);
        this.copyCertificate = list.command;
        this.copyCertificate = list.insecure_command;
      }
    },

    // 复制有证书命令
    copyCertificateclick() {
      var copyCertificate = document.getElementById("copyCertificates");
      copyCertificate.select(); // 选择对象
      document.execCommand("Copy"); // 执行浏览器复制命令
      this.$notify({
        title: this.$t("message.copySuccess"),
        type: "success",
        duration: 2500,
      });
    },
    // 复制无证书命令
    copyNoCertificateClick() {
      var copyNoCertificate = document.getElementById("copyNoCertificates");
      copyNoCertificate.select(); // 选择对象
      document.execCommand("Copy"); // 执行浏览器复制命令
      this.$notify({
        title: this.$t("message.copySuccess"),
        type: "success",
        duration: 2500,
      });
    },
    clickAddBtn() {
      this.$router.push("/serverCluster/clusterType");
    },
    // 导入集群命令
    clickImportBtn(item) {
      this.dialogVisible = true;
      this.importCmds(item.id);
    },
    // 导出模板
    clickExportBtn(item) {
      this.nowClsData = item;
      this.dialogFormVisible = true;
    },

    fakeClick(obj) {
      var ev = document.createEvent("MouseEvents");
      ev.initMouseEvent(
        "click",
        true,
        false,
        window,
        0,
        0,
        0,
        0,
        0,
        false,
        false,
        false,
        false,
        0,
        null
      );
      obj.dispatchEvent(ev);
    },
    toExportBtn() {
      this.$refs["templateFormref"].validate((valid) => {
        if (valid) {
          this.loading = true;
          exportClusterTemplates(this.nowClsData.id, this.templateFrom)
            .then((res) => {
              this.$notify({
                title: this.$t("message.exportSuccess"),
                type: "success",
                duration: 2500,
              });
              this.templateFrom = {
                template_name: "",
                template_desc: "",
                template_version: "v1",
                overwrite: false,
              };
              this.nowClsData = "";
              this.dialogFormVisible = false;
              this.loading = false;
            })
            .catch((err) => {
              this.loading = false;
              console.error(err.response.data.message);
            });
        }
      });
    },
    clickDelBtn(value) {
      this.$confirm(this.$t("form.deleteCluster"), this.$t("form.prompt"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          delClusters(value.id)
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
    clickEditBtn(info) {
      this.$router.push("/serverCluster/edit/" + info.id);
    },
    // 重新安装
    clickRefreshBtn(value) {
      this.$confirm(this.$t("form.reinstallCluster"), this.$t("form.prompt"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          clustersRepair(value.id, { type: "redeploy" })
            .then((res) => {
              this.$notify({
                title: this.$t("message.operationSuccess"),
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
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-textarea__inner {
  height: 80px !important;
}

::v-deep .el-dialog__footer {
  display: flex;
  justify-content: center;
  align-items: center;
}

::v-deep .el-divider__text,
.el-link {
  font-weight: 500;
  font-size: 12px;
}

.editor-container {
  position: relative;
  height: 100%;
}

#kubeconfig {
  width: 100%;
  height: 100%;
  font-size: inherit;
  color: #606266;
  background-color: #fff;
  background-image: none;
  box-sizing: border-box;
  display: block;
  resize: vertical;
  padding: 5px 15px;
  line-height: 1.5;
}
</style>
