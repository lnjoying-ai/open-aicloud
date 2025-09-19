<template>
  <div>
    <!-- 集群选项 -->
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("cluster.clusterOptions") }}</span>
        <span v-if="status === 1" style="float: right">
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-edit"
            @click="
              infoFrom.create_type == 'custom'
                ? (infoFrom.create_type = 'import')
                : (infoFrom.create_type = 'custom')
            "
            >{{
              infoFrom.create_type == "custom"
                ? $t("cluster.editYaml")
                : $t("cluster.editForm")
            }}
          </el-button>
        </span>
      </div>
      <div class="text item">
        <el-form ref="form" :model="infoFrom" label-width="150px">
          <div v-if="status === 0">
            <el-form-item
              :label="$t('cluster.useTemplate') + ':'"
              class="formContentBlock"
            >
              <el-switch v-model="infoFrom.isUse" style="position: relative" />
              <el-button
                v-if="!infoFrom.isUse"
                style="position: absolute; right: 0px"
                type="primary"
                size="mini"
                icon="el-icon-edit"
                @click="
                  infoFrom.create_type == 'custom'
                    ? (infoFrom.create_type = 'import')
                    : (infoFrom.create_type = 'custom')
                "
                >{{
                  infoFrom.create_type == "custom"
                    ? $t("cluster.editYaml")
                    : $t("cluster.editForm")
                }}
              </el-button>
            </el-form-item>
            <el-form-item
              v-if="infoFrom.isUse"
              :label="$t('cluster.versionSelect') + ':'"
              :rules="[
                {
                  required: true,
                  message: $t('cluster.pleaseSelectVersion'),
                },
              ]"
            >
              <el-cascader
                v-model="infoFrom.version"
                :options="infoFromVersionList"
                style="width: 230px"
              />
            </el-form-item>
          </div>
          <el-form-item
            v-show="infoFrom.create_type == 'import' && infoFrom.isUse == false"
            :label="$t('cluster.clusterConfig') + ':'"
            style="font-size: 14px; max-height: 52vh; overflow: auto"
          >
            <div>
              <el-button
                type="primary"
                size="small"
                class="drbtn"
                @click="yamlclickLoad"
                >{{ $t("form.import") }}
              </el-button>
              <input
                id="files"
                ref="yamlrefFile"
                type="file"
                style="display: none; margin-top: 5px"
                @change="yamlfileLoad"
              />
            </div>

            <div style="line-height: 1.4; min-width: 100%">
              <yaml-editor
                v-model="infoFrom.stack_compose"
                :download-name="'clusterConfig'"
                :download-type="'yml'"
                :placeholder="''"
                :is-add="false"
                :lint="true"
                @changeLint="getLintState($event)"
              />
            </div>
          </el-form-item>
        </el-form>
        <!-- 集群选项 自定义 -->
        <el-collapse
          v-show="infoFrom.create_type == 'custom' && infoFrom.isUse == false"
          v-model="clusterCollapse"
        >
          <el-collapse-item name="1" :title="$t('cluster.clusterType')">
            <template slot="title" />
            <el-form ref="form" :inline="true" label-width="120px">
              <el-form-item :label="$t('cluster.clusterVersion') + ':'">
                <el-select
                  v-model="k3s_config.k3s_version"
                  :placeholder="$t('cluster.pleaseSelectClusterVersion')"
                  style="width: 230px"
                >
                  <el-option
                    v-for="(item, index) in clusterTypeFormVersionList"
                    :key="index"
                    :label="item"
                    :value="item"
                  />
                </el-select>
              </el-form-item>
            </el-form>
          </el-collapse-item>
          <el-collapse-item :title="$t('cluster.imageLibrary')" name="2">
            <el-table
              :data="k3s_config.registries"
              style="width: 100%; margin-left: 120px; max-width: 1100px"
            >
              <el-table-column
                prop="server"
                :label="$t('cluster.imageLibraryAddress')"
              />
              <el-table-column prop="username" :label="$t('form.loginName')" />
              <el-table-column prop="type" :label="$t('form.type')" />
              <el-table-column :label="$t('form.operation')">
                <template slot-scope="scope">
                  <el-button
                    type="text"
                    @click="k3s_config.registries.splice(scope.$index, 1)"
                    >{{ $t("form.delete") }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div style="width: 100%; margin-left: 120px; max-width: 1100px">
              <el-button
                type="primary"
                icon="el-icon-plus"
                size="small"
                style="margin: 10px auto; display: block"
                @click="imagesDialogVisible = true"
                >{{ $t("form.add") }}
              </el-button>
            </div>
          </el-collapse-item>
          <el-collapse-item :title="$t('form.advancedConfig')" name="3">
            <div class="formItem">
              <el-form
                ref="form"
                :inline="true"
                label-width="130px"
                :model="k3s_config"
              >
                <div class="formItemTitle">
                  {{ $t("cluster.runtimeParameters") }}
                </div>
                <el-form-item :label="$t('form.type') + ':'">
                  <el-select
                    v-model="k3s_config.runtime_type"
                    :placeholder="$t('form.pleaseSelect')"
                    style="width: 230px"
                  >
                    <el-option label="docker" value="docker" />
                    <el-option label="containerd" value="containerd" />
                  </el-select>
                </el-form-item>
                <el-form-item :label="$t('cluster.endpoint') + ':'">
                  <el-input
                    v-model="k3s_config.runtime_endpoint"
                    style="width: 230px"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
                <el-form-item :label="$t('form.version') + ':'">
                  <el-input
                    v-model="k3s_config.runtime_version"
                    style="width: 230px"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
                <div class="formItemTitle">
                  {{ $t("cluster.dataSourceConfiguration") }}
                </div>
                <el-form-item
                  :label="$t('cluster.enableBuiltInEtcd') + ':'"
                  style="width: 100%"
                  label-width="140px"
                >
                  <el-switch
                    v-model="k3s_config.data_store_config.enable_etcd"
                  />
                </el-form-item>

                <el-form-item
                  v-if="!k3s_config.data_store_config.enable_etcd"
                  style="width: 100%"
                  prop="data_store_config.datastore_endpoint"
                >
                  <template #label>
                    <span class="leading-tight inline-block">
                      {{ $t("cluster.dataSourceEndpoint") + ":" }}
                    </span>
                  </template>
                  <el-input
                    v-model="k3s_config.data_store_config.datastore_endpoint"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                    style="width: 230px"
                  />
                </el-form-item>
              </el-form>
            </div>
          </el-collapse-item>
        </el-collapse>
        <!-- 集群选项 导入 -->
      </div>
    </el-card>
    <el-dialog
      :title="$t('form.imageList')"
      :visible.sync="imagesDialogVisible"
      width="800px"
      :before-close="handleClose"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane :label="$t('cluster.privateImageLibrary')" name="private">
          <el-table :data="tableData.registries">
            <el-table-column
              property="url"
              :label="$t('cluster.imageLibraryAddress')"
            />
            <el-table-column
              property="admin_name"
              :label="$t('form.loginName')"
            />
            <el-table-column :label="$t('form.type')">{{
              $t("form.private")
            }}</el-table-column>
            <el-table-column :label="$t('form.operation')" width="120">
              <template slot-scope="scope">
                <el-button
                  v-if="
                    !k3s_config.registries
                      .map((item) => {
                        return item.server;
                      })
                      .includes(scope.row.url)
                  "
                  type="text"
                  size="mini"
                  @click="getImagesItem(scope.row)"
                  >{{ $t("form.select") }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
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
        <el-tab-pane :label="$t('form.thirdPartyImageLibrary')" name="public">
          <el-table :data="registries3rdData">
            <el-table-column
              property="url"
              :label="$t('cluster.imageLibraryAddress')"
            />
            <el-table-column
              property="access_key"
              :label="$t('form.loginName')"
            />
            <el-table-column :label="$t('form.type')">{{
              $t("form.public")
            }}</el-table-column>
            <el-table-column :label="$t('form.operation')" width="120">
              <template slot-scope="scope">
                <el-button
                  v-if="
                    !k3s_config.registries
                      .map((item) => {
                        return item.server;
                      })
                      .includes(scope.row.url)
                  "
                  type="text"
                  size="mini"
                  @click="getImagesItem(scope.row)"
                  >{{ $t("form.select") }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
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
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
var yaml = require("js-yaml");
import { removePropertyOfNull } from "@/utils/index.js";
import {
  k8stemplates,
  clustersVersions,
  registries,
  registries3rd,
} from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";
import { mapGetters } from "vuex";

export default {
  components: { YamlEditor },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  data() {
    return {
      activeName: "private",
      registries3rdData: [],
      // 数据源提示信息
      hoverMessage: "",
      // 控制显示字段
      status: 0,
      imagesDialogVisible: false, // 镜像列表
      tableData: [], // 镜像列表
      queryData: {
        status: "",
        page_size: 10,
        page_num: 1,
      },
      clusterCollapse: ["1"], // 手风琴
      //  集群选项基本信息  //
      infoFrom: {
        // 集群选项基本信息
        isUse: false, // 是否使用模板
        version: [], // 版本选择
        create_type: "custom", // custom:用户自定义 import：导入
        stack_compose: "",
      },
      infoFromVersionList: [], // 集群选项基本信息 版本列表
      //  集群类型  //
      clusterTypeForm: {
        // 集群类型
        cluster_type: "k3s",
        version: "", // 集群版本选择
      },
      clusterTypeFormVersionList: [], // 集群版本列表
      //  镜像库  //
      imagesTableData: [], // 镜像列表数据
      //  高级配置  //
      k3s_configMain: "",
      k3s_configInit: "",
      k3s_config: {
        k3s_version: "", // k3s version
        deploy_job_time: 0, // 安装部署时，每步骤容器的超时时间 部署设置 时间
        runtime_type: "containerd",
        runtime_endpoint: "",
        runtime_version: "",
        registries: [], // 镜像仓库配置
        // 数据源配置
        data_store_config: {
          enable_etcd: false,
          // datastore_type: "Etcd",
          datastore_endpoint: "",
          datastore_cafile: "",
          datastore_certfile: "",
          datastore_keyfile: "",
          etcd_expose_metrics: false,
          etcd_disable_snapshots: false,
          etcd_snapshot_name: "",
          etcd_snapshot_schedule_cron: "",
          etcd_snapshot_retention: 0,
          etcd_snapshot_dir: "",
          etcd_s3: false,
          etcd_s3_endpoint: "",
          etcd_s3_endpoint_ca: "",
          etcd_s3_skip_ssl_verify: false,
          etcd_s3_access_key: "",
          etcd_s3_secret_key: "",
          etcd_s3_bucket: "",
          etcd_s3_region: "",
          etcd_s3_folder: "",
        },
        // 网络配置
        network_config: {
          cluster_cidr: "",
          service_cidr: "",
          service_node_port_range: "",
          cluster_dns: "",
          cluster_domain: "",
        },
        // 额外自定义参数
        extra_args: [],
      },
    };
  },
  watch: {
    "infoFrom.create_type": {
      deep: true,
      handler(val) {
        if (val == "custom") {
          var stack_compose = yaml.load(this.infoFrom.stack_compose);
          var data = this.$script.jkeconfigServicesCustom(
            JSON.parse(JSON.stringify(stack_compose))
          );
          this.k3s_config = this.deepMerge(this.k3s_configInit, data);
        } else if (val == "import") {
          var data = this.$script.jkeconfigServices(
            JSON.parse(JSON.stringify(this.k3s_config))
          );
          this.infoFrom.stack_compose = yaml.dump(data);
        }
      },
    },
    "infoFrom.isUse": {
      deep: true,
      handler(val) {
        if (val == true) {
        } else if (val == false) {
        }
      },
    },
    k3s_config: {
      deep: true,
      handler(val) {
        this.k3s_configMain = this.$script.jkeconfigServices(
          JSON.parse(JSON.stringify(val))
        );
      },
    },
    imagesDialogVisible: {
      deep: true,
      handler(val) {
        if (val) {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
  },
  mounted() {
    this.k3s_configInit = JSON.parse(JSON.stringify(this.k3s_config));
    this.k3s_configMain = this.$script.jkeconfigServices(
      JSON.parse(JSON.stringify(this.k3s_config))
    );
    if (
      this.$route.query.type == "isUse" &&
      this.$route.query.temId &&
      this.$route.query.verId
    ) {
      this.infoFrom.isUse = true;
      this.infoFrom.version = [
        this.$route.query.temId,
        this.$route.query.verId,
      ];
    }
  },
  created() {
    this.getK3stemplates(); // 获取模板列表
    this.getClustersVersions(); // 集群模板获取集群名称
  },
  methods: {
    handleClose() {
      (this.activeName = "private"), (this.imagesDialogVisible = false);
    },
    getLintState(state) {
      this.yamlLintState = state;
      this.$emit("getYamlLint", state);
    },
    // 选择外接数据源触发
    handleRadioChange(value) {
      if (value == "etcd") {
        this.hoverMessage = "hostname1:port, hostname1:port, hostname1:port";
      } else if (value == "Mysql") {
        this.hoverMessage =
          "username:password@tcp(hostname:3306)/database-name";
      } else if (value == "Postgres") {
        this.hoverMessage =
          "postgresql://[user[:password]@][netloc][:port][/dbname][?param1=value1&...]";
      } else if (value == "Sqlite") {
        this.hoverMessage = "file:data.db";
      }
    },
    deepMerge(obj1, obj2) {
      var key;
      for (key in obj2) {
        // 如果target(也就是obj1[key])存在，且是对象的话再去调用deepMerge，否则就是obj1[key]里面没这个对象，需要与obj2[key]合并
        obj1[key] =
          obj1[key] && obj1[key].toString() === "[object Object]"
            ? this.deepMerge(obj1[key], obj2[key])
            : (obj1[key] = obj2[key]);
      }
      return obj1;
    },
    // 接收父组件传值
    message(message) {
      this.k3s_config = message;
    },
    nowStatus() {
      this.imagesTableData
        .map((item) => {
          return item.id;
        })
        .include(scope.row.registry_id);
    },

    yamlclickLoad() {
      // 授权集群端点 点击上传证书文件
      this.$refs.yamlrefFile.dispatchEvent(new MouseEvent("click"));
    },
    yamlfileLoad() {
      var _this = this;
      const selectedFile = this.$refs.yamlrefFile.files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        _this.infoFrom.stack_compose = this.result;
        _this.yamlJson = yaml.load(this.result);
      };
    },
    // 镜像库数据处理
    getImagesItem(item) {
      if (this.activeName == "public") {
        this.k3s_config.registries.push({
          server: item.url, // 镜像库地址
          username: item.access_key, // 登录名
          password: item.access_secret,
          type: "public", // 类型
        });
      } else if (this.activeName == "private") {
        this.k3s_config.registries.push({
          server: item.url, // 镜像库地址
          username: item.admin_name, // 登录名
          type: "private", // 类型
        });
      }
    },
    // 获取K3s 的模板版本
    getK3stemplates() {
      k8stemplates({ cluster_type: "k3s" }).then((res) => {
        // 获取模板列表
        this.infoFromVersionList = [];
        res.templates.map((item) => {
          var data = {
            value: item.id,
            label: item.name,
            children: [],
          };
          if (item.versions != null && item.versions.length > 0) {
            item.versions.map((item1) => {
              data.children.push({
                value: item1.id,
                label: item1.version,
              });
            });
          }
          this.infoFromVersionList.push(data);
        });
      });
    },
    // k3s的集群版本
    getClustersVersions() {
      clustersVersions(this.clusterTypeForm.cluster_type).then((res) => {
        if (res.k3s != undefined) {
          this.clusterTypeFormVersionList = res.k3s;
          this.k3s_config.k3s_version = res.k3s[0];
        }
      });
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      if (this.activeName == "public") {
        this.getRegistries3rd();
      } else {
        this.getRegistries();
      }
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      if (this.activeName == "public") {
        this.getRegistries3rd();
      } else {
        this.getRegistries();
      }
    },
    init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      this.getRegistries();
      this.getRegistries3rd();
    },
    async getRegistries() {
      const list = await registries(this.queryData);
      this.tableData = list;
    },
    async getRegistries3rd() {
      const RDlist = await registries3rd(this.queryData);
      this.registries3rdData = RDlist.registries;
    },
  },
};
</script>

<style lang="scss" scoped>
.el-card__header {
  padding: 18px 20px;
  border-bottom: 0px solid #ebeef5;
  -webkit-box-sizing: border-box;
  box-sizing: border-box;
}

.box-card {
  border: none;

  ::v-deep .el-card__header {
    padding: 10px 20px;
    font-weight: bold;
  }
}

.el-collapse {
  border-bottom: none;

  ::v-deep .el-collapse-item__wrap {
    border-bottom: none;
  }
}

.formItem {
  padding: 10px 20px;

  .formItemTitle {
    // font-weight: bold;
    font-size: 14px;
    padding-bottom: 10px;
  }
}

.textareaStyle {
  ::v-deep .el-textarea__inner {
    background-color: #000;
    color: #fff;

    &::-webkit-scrollbar-track-piece {
      background: #d3dce6;
    }

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: #99a9bf;
      border-radius: 20px;
    }
  }
}
</style>
