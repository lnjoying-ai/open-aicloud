<template>
  <div>
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #303133">
          {{ $t("cluster.templateInfo") }}
        </span>
      </div>
      <div class="text item">
        <!-- 集群信息开始 -->
        <el-form ref="templateForm" :model="templateForm" label-width="150px">
          <el-form-item :label="$t('cluster.templateName') + ':'" prop="name">
            <el-input
              v-model="templateForm.name"
              :disabled="disableStatus"
              :placeholder="$t('form.pleaseInputTemplateName')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.templateDescription') + ':'">
            <el-input
              v-model="templateForm.desc"
              type="textarea"
              maxlength="255"
              show-word-limit
              :disabled="disableStatus"
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <!-- 集群信息结束-->

    <!-- 版本信息开始 -->
    <el-card class="box-card" shadow="never">
      <div slot="header" class="clearfix">
        <span style="font-weight: bold; color: #303133">{{
          $t("cluster.versionInfo")
        }}</span>
      </div>
      <div class="text item">
        <el-form ref="form" :model="versionForm" label-width="150px">
          <el-form-item :label="$t('form.version') + ':'">
            <el-input
              v-model="versionForm.version"
              :placeholder="$t('form.pleaseInput')"
              :disabled="disable"
            />
          </el-form-item>
          <el-form-item :label="$t('form.versionDescription') + ':'">
            <el-input
              v-model="versionForm.desc"
              type="textarea"
              maxlength="255"
              show-word-limit
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
        </el-form>
        <div style="margin-bottom: 20px; font-weight: bold">
          {{ $t("cluster.identifier") }}
        </div>
        <span style="margin-bottom: 20px">
          <el-form ref="form" :model="versionForm">
            <el-form-item>
              <el-tag
                v-for="tag in versionForm.tags"
                :key="tag"
                closable
                :disable-transitions="false"
                style="margin-right: 10px"
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
              :label="$t('cluster.enableVersion')"
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
    <K3SinfoFromVue ref="infoRef" />
    <el-card class="box-card mt-2 mb-2" shadow="never">
      <div class="btnMain" style="text-align: center">
        <el-button type="primary" @click="onSubmit">{{
          $t("form.submit")
        }}</el-button>
      </div>
    </el-card>
  </div>
</template>
<script>
var yaml = require("js-yaml");
import initData from "@/mixins/initData";
import validate from "@/utils/validate";
import K3SinfoFromVue from "./components/K3SinfoFrom.vue";
import {
  getTemplatesVersions,
  getBps,
  getUsers,
  templatesVersions,
  reviseTemplatesVersions,
} from "@/api/mainApi";
export default {
  components: { K3SinfoFromVue },
  mixins: [initData],
  data() {
    return {
      // 禁用状态
      disableStatus: false,
      // 标题
      title: "",
      // validate: validate,
      BpsData: [], // 组织机构
      userData: [], // 用户
      bpId: "", // 组织机构ID
      userId: "", // 用户ID
      // 集群信息
      templateForm: {
        name: "",
        desc: "",
        // 标识
        tags: [],
        // 共享成员
        members: { user_id: "", user_name: "", access_role: "" },
        // 集群模板拥有者
        owner: "",
      },
      // 共享伙伴信息
      partner: [],
      // 集群信息tags显示
      inputVisible: false,
      inputValue: "",
      // 是否为其他用户创建
      establish: true,
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
                    key: item.id,
                    value: item.name,
                    label: item.name,
                    leaf: level >= 1,
                  });
                });
              }

              resolve(nodes);
            });
          } else {
            getUsers({ bp_id: node.key }).then((res) => {
              if (res.users != null && res.users.length > 0) {
                res.users.map((item) => {
                  nodes.push({
                    key: item.id,
                    value: item.name,
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
      detailMain: "", //
      // 禁用控制
      disable: false,
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
      },
      // 模板版本标识显示
      inputVisibleV: false,
      inputValueV: "",
    };
  },
  watch: {},

  created() {},
  mounted() {
    this.init();
    this.bpsinit();
    this.userinit();
    this.$refs.infoRef.status = 1;
    if (this.$route.params.type == "edit") {
      this.disable = true;
      this.disableStatus = true;
    } else {
      this.disable = false;
      this.disableStatus = true;
    }
  },
  methods: {
    init() {
      // 请求模板详情
      templatesVersions(this.$route.params.id).then((res) => {
        this.templateForm.name = res.template_name;
        this.templateForm.desc = res.desc;
        if (typeof res.k3s_config === "string") {
          res.k3s_config = JSON.parse(res.k3s_config);
        }
        if (typeof res.tags === "string") {
          res.tags = JSON.parse(res.tags);
        }
        this.$refs.infoRef.k3s_config = {
          ...this.$refs.infoRef.k3s_config,
          ...res.k3s_config,
        };
        this.$refs.infoRef.k3s_config = this.deepMerge(
          this.$refs.infoRef.k3s_config
        );

        this.$refs.infoRef.clusterTypeForm.isUse = !!res.docker_version;
        this.detailMain = res;

        if (this.$route.params.type == "clone") {
          this.versionForm.version = this.detailMain.version + "-clone";
        } else {
          this.versionForm.version = this.detailMain.version;
        }
        this.versionForm.desc = this.detailMain.desc;
        this.versionForm.enable = this.detailMain.enable;
        if (this.detailMain.tags == null) {
          this.versionForm.tags = [];
        } else {
          this.versionForm.tags = this.detailMain.tags;
        }
      });
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
    // 选择组织机构，改变用户选择
    change_bp_id(value) {
      this.bpId = value;
      this.userId = "";
      this.userinit();
    },
    // 用户改变触发
    change_user_id(value) {
      this.userId = value;
    },
    clear() {
      this.userId = "";
    },
    async bpsinit() {
      const list = await getBps();
      this.BpsData = list.bps;
    },
    async userinit() {
      const params = {};
      if (this.bpId != "" && this.bpId != undefined) {
        params.bp_id = this.bpId;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      this.userData = list.users;
    },
    // 模板信息标识
    handleClose(tag) {
      this.templateForm.tags.splice(this.templateForm.tags.indexOf(tag), 1);
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.templateForm.tags.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
    // 克隆版本
    getVersions() {
      this.versionForm.template_id = this.detailMain.template_id;
      if (
        this.$route.query.owner != "" &&
        this.$route.query.owner != undefined &&
        this.$route.query.owner != null
      ) {
        this.versionForm.owner = this.$route.query.owner;
      }
      getTemplatesVersions(
        this.removePropertyOfNull(
          this.removePropertyOfNull(
            JSON.parse(JSON.stringify(this.versionForm))
          )
        )
      )
        .then((res) => {
          this.$notify({
            title: this.$t("cluster.cloneSuccess"),
            type: "success",
            duration: 2500,
          });
          this.$router.push("/serverCluster/templateList");
          this.resetForm();
        })
        .catch((err) => {});
    },
    // 更新版本
    updateVersions() {
      if (
        this.$route.query.owner != "" &&
        this.$route.query.owner != undefined &&
        this.$route.query.owner != null
      ) {
        this.versionForm.owner = this.$route.query.owner;
      }
      var data = {
        desc: this.versionForm.desc,
        enable: this.versionForm.enable,
        tags: this.versionForm.tags,
        k3s_config: this.versionForm.k3s_config,
      };
      reviseTemplatesVersions(
        this.detailMain.id,
        this.removePropertyOfNull(
          this.removePropertyOfNull(JSON.parse(JSON.stringify(data)))
        )
      )
        .then((res) => {
          this.$notify({
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.$router.push("/serverCluster/templateList");
          this.resetForm();
        })
        .catch((err) => {});
    },
    onSubmit() {
      // 提交信息
      // 以下信息为集群选项信息
      var infoFrom = this.$refs.infoRef.infoFrom; // 集群选项基本信息
      var k3s_config = this.$refs.infoRef.k3s_configMain;

      var yamlJson = this.$refs.infoRef.infoFrom.stack_compose; // yaml转json
      if (infoFrom.create_type == "import") {
        // 如果是yaml导入
        this.versionForm.k3s_config = yaml.load(yamlJson);
      } else {
        this.versionForm.k3s_config = k3s_config;
      }

      if (this.$route.params.type == "clone") {
        this.getVersions();
      } else {
        this.updateVersions();
      }
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
    resetForm() {},
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

::v-deep .el-collapse-item__header {
  background-color: rgba(250, 250, 250, 1) !important;
  margin-bottom: 5px;
  padding-left: 10px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

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
</style>
