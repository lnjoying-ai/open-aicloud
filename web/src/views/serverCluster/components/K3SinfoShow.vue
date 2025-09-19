<template>
  <div class="infoShowPage">
    <!-- 集群选项 -->
    <el-card v-if="k3s_configData" class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("cluster.clusterOptions") }}</span>
        <el-button
          type="primary"
          size="mini"
          style="float: right"
          @click="
            infoFrom.create_type == 'custom'
              ? (infoFrom.create_type = 'import')
              : (infoFrom.create_type = 'custom')
          "
          >{{
            infoFrom.create_type == "custom"
              ? $t("cluster.showYaml")
              : $t("cluster.showForm")
          }}</el-button
        >
      </div>
      <div class="text item">
        <el-form ref="form" label-width="140px">
          <div v-if="status === 0" style="display: flex">
            <el-form-item
              v-if="infoFrom.isUse"
              :label="$t('cluster.templateName') + ':'"
            >
              <span>{{ template_name }} </span>
            </el-form-item>
            <el-form-item
              v-if="infoFrom.isUse"
              :label="$t('cluster.versionNumber') + ':'"
            >
              <span
                >{{
                  infoFromVersionList.filter(
                    (item) => item.id == detailMain.tmpl_ver_id
                  )[0]
                    ? infoFromVersionList.filter(
                        (item) => item.id == detailMain.tmpl_ver_id
                      )[0].version
                    : "-"
                }}
              </span>
            </el-form-item>
          </div>
          <el-form-item
            v-show="infoFrom.create_type == 'import'"
            :label="$t('cluster.clusterConfig') + ':'"
          >
            <div
              style="
                line-height: 1.4;
                min-width: 100%;
                max-height: 52vh;
                overflow: auto;
              "
            >
              <yaml-editor
                ref="yamlEditor"
                v-model="infoFrom.stack_compose"
                :download-name="yamlEditorName"
                :download-type="'yml'"
                :placeholder="''"
                :is-add="false"
                :readonly="'nocursor'"
              />
            </div>
          </el-form-item>
        </el-form>
        <!-- 集群选项 自定义 -->
        <el-collapse
          v-show="infoFrom.create_type == 'custom'"
          v-model="clusterCollapse"
        >
          <el-collapse-item name="1" :title="$t('cluster.clusterInformation')">
            <el-form ref="form" :inline="true" label-width="140px">
              <el-form-item :label="$t('cluster.clusterType') + ':'">
                <span>{{ "k3s" }}</span>
              </el-form-item>
              <el-form-item :label="$t('cluster.clusterVersion') + ':'">
                <span>{{ k3s_configData.k3s_version || "-" }}</span>
              </el-form-item>
            </el-form>
          </el-collapse-item>
          <el-collapse-item :title="$t('cluster.imageLibrary')" name="2">
            <el-table
              :data="k3s_configData.registries"
              style="width: 100%; margin: 0 auto; max-width: 1100px"
            >
              <el-table-column
                prop="server"
                :label="$t('cluster.imageLibraryAddress')"
              />
              <el-table-column prop="username" :label="$t('form.loginName')" />
              <el-table-column prop="type" :label="$t('form.type')" />
            </el-table>
          </el-collapse-item>
          <el-collapse-item :title="$t('form.advancedConfig')" name="3">
            <el-form ref="form" :inline="true" label-width="160px">
              <div class="formItem">
                <div class="formItemTitle">
                  {{ $t("cluster.runtimeParameters") }}
                </div>
                <el-form-item :label="$t('form.type') + ':'">
                  <span>{{
                    k3s_configData.runtime_type
                      ? k3s_configData.runtime_type
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item :label="$t('cluster.endpoint') + ':'">
                  <span>{{
                    k3s_configData.runtime_endpoint
                      ? k3s_configData.runtime_endpoint
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.version') + ':'">
                  <span>{{
                    k3s_configData.runtime_version
                      ? k3s_configData.runtime_version
                      : "-"
                  }}</span>
                </el-form-item>
              </div>
            </el-form>
            <el-form
              v-if="k3s_configData.data_store_config != null"
              ref="form"
              :inline="true"
              label-width="160px"
            >
              <div class="formItem">
                <div class="formItemTitle">
                  {{ $t("cluster.dataSourceConfiguration") }}
                </div>
                <el-form-item :label="$t('cluster.enableBuiltInEtcd') + ':'">
                  <span>{{
                    k3s_configData.data_store_config.enable_etcd
                      ? $t("cluster.yes")
                      : $t("cluster.no")
                  }}</span>
                </el-form-item>
                <el-form-item
                  v-if="k3s_configData.data_store_config.enable_etcd == false"
                  :label="$t('cluster.externalDataSourceEndpoint') + ':'"
                >
                  <span>{{
                    k3s_configData.data_store_config.datastore_endpoint
                      ? k3s_configData.data_store_config.datastore_endpoint
                      : "-"
                  }}</span>
                </el-form-item>
              </div>
            </el-form>
          </el-collapse-item>
        </el-collapse>
        <!-- 集群选项 导入 -->
      </div>
    </el-card>
  </div>
</template>

<script>
var yaml = require("js-yaml");
import Clipboard from "clipboard";
import { k8stemplates, templatesVersions } from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: { YamlEditor },
  props: ["detailMain", "k3s_configData", "yamlEditorName"],
  data() {
    return {
      // 模板名称
      template_name: "",
      infoFromVersionList: [],
      // 控制显示字段
      status: 0,
      clusterCollapse: "1", // 默认展开第一个
      infoFrom: {
        // 集群选项基本信息
        isUse: true, // 是否使用模板
        version: [], // 版本选择
        create_type: "custom", // custom:用户自定义 import：导入
        stack_compose: "",
      },
    };
  },
  watch: {
    "infoFrom.create_type": {
      deep: true,
      handler(val) {
        this.infoFrom.stack_compose = yaml.dump(this.k3s_configData);
        this.$refs.yamlEditor.toRefresh();
      },
    },
  },
  mounted() {},
  created() {
    this.getK8stemplates(); // 获取模板列表
  },
  methods: {
    // 请求版本模板名称
    getTemplatesName() {
      this.template_name = "";
      if (
        this.detailMain.tmpl_ver_id != null &&
        this.detailMain.tmpl_ver_id != undefined &&
        this.detailMain.tmpl_ver_id != ""
      ) {
        // 请求模板详情
        templatesVersions(this.detailMain.tmpl_ver_id).then((res) => {
          this.template_name = res.template_name;
        });
      } else {
        this.template_name = "-";
      }
    },
    getK8stemplates() {
      k8stemplates({ cluster_type: "k3s" }).then((res) => {
        // 获取模板列表
        this.infoFromVersionList = [];
        res.templates.map((item) => {
          if (item.versions != null && item.versions.length > 0) {
            item.versions.map((item1) => {
              this.infoFromVersionList.push({
                id: item1.id,
                version: item1.version,
              });
            });
          }
        });
        this.getTemplatesName();
      });
    },
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          title: this.$t("message.copySuccess"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.on("error", () => {
        // 不支持复制
        this.$notify({
          title: this.$t("message.copyFailed"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
  },
};
</script>

<style lang="scss" scoped>
.infoShowPage {
  ::v-deep .el-collapse {
    .el-collapse-item__header {
      height: 40px;
      line-height: 40px;
      padding-left: 10px;
    }
  }
}

span {
  font-size: 14px;
  width: 200px;
  display: flex;
  justify-content: start;
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
