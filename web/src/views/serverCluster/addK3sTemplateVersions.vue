<template>
  <div>
    <!-- 版本信息开始 -->
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #303133">{{
          $t("cluster.versionInfo")
        }}</span>
      </div>
      <div class="text item">
        <el-form
          ref="form"
          :model="versionForm"
          label-width="80px"
          :rules="rules"
        >
          <el-form-item
            :label="$t('cluster.versionNumber') + ':'"
            prop="version"
          >
            <el-input
              v-model="versionForm.version"
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
          <el-form-item :label="$t('cluster.versionDescription') + ':'">
            <el-input
              v-model="versionForm.desc"
              type="textarea"
              maxlength="255"
              show-word-limit
              :placeholder="$t('form.pleaseInputDesc')"
            />
          </el-form-item>
        </el-form>
        <div style="margin-bottom: 20px; font-weight: bold">
          {{ $t("cluster.identifier") }}
        </div>
        <span style="margin-bottom: 20px">
          <el-form ref="form" :model="versionForm" @submit.native.prevent>
            <el-form-item>
              <el-tag
                v-for="tag in versionForm.tags"
                :key="tag"
                closable
                :disable-transitions="false"
                style="margin-right: 30px"
                @close="handleCloseVersion(tag)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                v-if="inputVisibleV"
                ref="saveTagInput"
                v-model="inputValueV"
                class="input-new-tag"
                size="small"
                style="display: inline-block"
                @keyup.enter.native="handleInputConfirmVersion"
                @blur="handleInputConfirmVersion"
              />
              <el-button
                v-else
                class="button-new-tag"
                size="small"
                @click="showInputVersion"
                >{{ $t("form.newTag") }}</el-button
              >
            </el-form-item>
            <el-form-item
              :label="$t('cluster.enableVersion') + ':'"
              class="formContentBlock"
            >
              <el-switch v-model="versionForm.enable" />
            </el-form-item>
          </el-form>
        </span>
      </div>
    </el-card>
    <!-- 版本信息结束-->
    <!-- 集群选项 -->
    <K3SinfoFromVue ref="infoRef" @getYamlLint="toYamlLint($event)" />
  </div>
</template>
<script>
var yaml = require("js-yaml");

import initData from "@/mixins/initData";
import K3SinfoFromVue from "./components/K3SinfoFrom.vue";
import { getTemplatesVersions, getBps, getUsers } from "@/api/mainApi";
export default {
  name: "AddK3sTemplateVersions",
  components: { K3SinfoFromVue },
  mixins: [initData],
  data() {
    return {
      yamlLintState: false,
      templatesInfoData: "", // 模板信息
      // 版本信息
      versionForm: {
        // 模板ID
        template_id: "",
        // 版本号
        version: "",
        // 版本描述
        desc: "",
        // 是否启用
        enable: true,
        // 关键标识信息
        tags: [],
        // 集群参数
        k3s_config: {
          k3s_version: "", // k3s version
          deploy_job_time: 0, // 安装部署时，每步骤容器的超时时间 部署设置 时间
          runtime_type: "containerd",
          runtime_endpoint: "",
          runtime_version: "",
          registries: [], // 镜像仓库配置
          // 数据源配置
          dataStoreConfig: {
            enable_etcd: false,
            datastore_endpoint: "",
            datastore_cafile: "",
            datastore_certfile: "",
            datastore_keyfile: "",
            etcd_expose_metrics: "",
            etcd_disable_snapshots: "",
            etcd_snapshot_name: "",
            etcd_snapshot_schedule_cron: "",
            etcd_snapshot_retention: "",
            etcd_snapshot_dir: "",
            etcd_s3: "",
            etcd_s3_endpoint: "",
            etcd_s3_endpoint_ca: "",
            etcd_s3_skip_ssl_verify: "",
            etcd_s3_access_key: "",
            etcd_s3_secret_key: "",
            etcd_s3_bucket: "",
            etcd_s3_region: "",
            etcd_s3_folder: "",
          },
          // 网络配置
          networkConfig: {
            cluster_cidr: "",
            service_cidr: "",
            service_node_port_range: "",
            cluster_dns: "",
            cluster_domain: "",
          },
          // 额外自定义参数
          extra_args: [],
        },
        // 拥有者信息
        owner: "",
      },
      rules: {
        version: [
          {
            required: true,
            message: this.$t("cluster.pleaseEnterVersionNumber"),
            trigger: "blur",
          },
        ],
      },
      // 模板版本标识显示
      inputVisibleV: false,
      inputValueV: "",
      props: {
        // 用户选择
        lazy: true,
        lazyLoad(node, resolve) {
          const { level } = node;
          const nodes = [];
          if (level === 0) {
            getBps().then((res) => {
              if (res.bps != null && res.bps.length > 0) {
                res.bps.map((item) => {
                  nodes.push({
                    value: item.id,
                    label: item.name,
                    leaf: level >= 1,
                  });
                });
              }

              resolve(nodes);
            });
          } else {
            getUsers({ bp_id: node.value }).then((res) => {
              if (res.users != null && res.users.length > 0) {
                res.users.map((item) => {
                  nodes.push({
                    value: item.id,
                    label: item.name,
                    leaf: level >= 1,
                  });
                });
              }
              resolve(nodes);
            });
          }
        },
      },
    };
  },
  watch: {},

  created() {},
  mounted() {
    this.$nextTick(() => {
      this.$refs.infoRef.status = 1;
    });
  },
  methods: {
    toYamlLint(data) {
      this.yamlLintState = data;
    },
    async getVersions() {
      await getTemplatesVersions(
        this.removePropertyOfNull(
          this.removePropertyOfNull(
            JSON.parse(JSON.stringify(this.versionForm))
          )
        )
      )
        .then((res) => {
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.$router.push("/serverCluster/templateList");
        })
        .catch((err) => {});
    },
    onSubmit() {
      // 以下信息为集群选项信息
      var infoFrom = this.$refs.infoRef.infoFrom; // 集群选项基本信息
      var k3s_config = this.$refs.infoRef.k3s_configMain;
      // var yamlJson = this.$refs.infoRef.yamlJson; //yaml转json
      var yamlJson = this.$refs.infoRef.infoFrom.stack_compose; // yaml转json
      if (infoFrom.create_type == "import") {
        // 如果是yaml导入
        this.versionForm.k3s_config = yaml.load(yamlJson);
      } else {
        this.versionForm.k3s_config = k3s_config;
      }
      if (this.versionForm.version == "") {
        this.$notify({
          title: this.$t("cluster.pleaseEnterVersionNumber"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      this.getVersions();
    },
    removePropertyOfNull(data) {
      if (typeof data === "object" && data.constructor == Array) {
        data.forEach((item, index, arr) => {
          if (
            item === null ||
            (typeof item === "object" &&
              item.constructor == Object &&
              Object.keys(item).length < 1) ||
            (typeof item === "object" &&
              item.constructor == Array &&
              item.length < 1) ||
            item === ""
          ) {
            delete arr[index];
          } else {
            this.removePropertyOfNull(arr[index]);
          }
        });
      }
      if (typeof data === "object" && data.constructor == Object) {
        for (const key in data) {
          if (
            data[key] === null ||
            (typeof data[key] === "object" &&
              data[key].constructor == Object &&
              Object.keys(data[key]).length < 1) ||
            (typeof data[key] === "object" &&
              data[key].constructor == Array &&
              data[key].length < 1) ||
            data[key] === ""
          ) {
            delete data[key];
          } else {
            this.removePropertyOfNull(data[key]);
          }
        }
      }
      return data;
    },
    // 版本标识
    handleCloseVersion(tag) {
      this.versionForm.tags.splice(this.versionForm.tags.indexOf(tag), 1);
    },

    showInputVersion() {
      this.inputVisibleV = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirmVersion() {
      const inputValue = this.inputValueV;
      if (inputValue) {
        this.versionForm.tags.push(inputValue);
      }
      this.inputVisibleV = false;
      this.inputValueV = "";
    },
    // 重置模板
    resetForm() {
      var infoFrom = this.$refs.infoRef.infoFrom; // 集群选项基本信息
      if (infoFrom.create_type == "import") {
        // 如果是yaml导入
        this.$refs.infoRef.yamlJson = [];
      } else {
        this.$refs.infoRef.k3s_config = [];
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.box-card {
  border: none;

  ::v-deep .el-card__header {
    padding: 10px 20px;
  }
}

.el-collapse {
  border-bottom: none;

  ::v-deep .el-collapse-item__wrap {
    border-bottom: none;
  }
}

.el-tag + .el-tag {
  margin-left: 10px;
}

.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}

.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}

::v-deep .el-collapse-item__header {
  padding-left: 10px;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}
</style>
