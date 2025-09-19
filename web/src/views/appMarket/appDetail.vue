<template>
  <div
    class="warpMain"
    v-loading="loading"
    :element-loading-text="$t('domain.loading')"
  >
    <div
      style="display: flex; justify-content: end; align-items: center"
      v-show="!installStatus"
    >
      <el-button type="primary" @click="download" size="mini">
        {{ $t("form.downloadAppPackage") }}
      </el-button>
      <el-button
        type="primary"
        :disabled="!fileText"
        @click="installStatus = true"
        size="mini"
      >
        {{ $t("form.installApp") }}
      </el-button>
    </div>
    <el-row v-show="!installStatus">
      <el-col :span="24">
        <el-form
          size="small"
          :model="queryData"
          class="demo-form-inline"
          label-width="120px"
        >
          <div class="w-1/2 float-left">
            <el-form-item :label="$t('form.name') + ':'">
              <span>
                {{ queryData.name }}
              </span>
            </el-form-item>
            <el-form-item :label="$t('form.description') + ':'">
              <span>
                {{ queryData.description || "-" }}
              </span>
            </el-form-item>
            <el-form-item :label="$t('form.createTime') + ':'">
              <span>
                {{ queryData.create_time }}
              </span>
            </el-form-item>
          </div>
          <div class="w-1/2 overflow-hidden">
            <el-form-item :label="$t('form.icon') + ':'">
              <div v-if="queryData.icon != null && queryData.icon != undefined">
                <img
                  :src="queryData.icon + ''"
                  alt=""
                  :onerror="defaultImg"
                  class="w-10 h-10"
                  style=""
                />
              </div>
              <div v-else>
                <img
                  :src="
                    require('../../assets/images/appMarket/icons8-app-48.png')
                  "
                  class="w-10 h-10"
                  style=""
                />
              </div>
            </el-form-item>
            <el-form-item :label="$t('form.version') + ':'">
              <el-select
                filterable
                v-model="nowVersion"
                :placeholder="$t('form.pleaseSelect')"
                clearable
              >
                <el-option
                  v-for="(item, index) in versionList"
                  :key="index"
                  :label="item.version"
                  :value="item.version"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('form.file') + ':'">
              <el-select
                v-model="fileText"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                clearable
              >
                <el-option
                  v-for="(item, index) in versionsFiles.files"
                  :key="index"
                  :label="index"
                  :value="getDecode(item)"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </el-form>
      </el-col>
      <el-col>
        <yaml-editor
          :downloadName="queryData.name"
          :downloadType="'yml'"
          style="
            margin-top: 12px;
            min-width: 100%;
            max-height: 600px;
            overflow-y: auto;
          "
          :readonly="'cursor'"
          :placeholder="''"
          :isAdd="false"
          :lint="false"
          v-model="fileText"
        ></yaml-editor>
      </el-col>
    </el-row>
    <el-row v-show="installStatus">
      <div
        v-if="installStatus && installForm.stack_compose"
        style="float: right"
      >
        <div class="text-center">
          <el-button @click="installStatus = false" size="mini">
            {{ $t("form.back") }}
          </el-button>
          <el-button type="primary" @click="onSubmit" size="mini">{{
            deployStatus
          }}</el-button>
        </div>
      </div>
      <el-col :span="24">
        <el-form
          size="small"
          ref="installFormRef"
          :model="installForm"
          :rules="installFormRules"
          class="demo-form-inline"
          label-width="120px"
        >
          <el-row>
            <el-col :span="12">
              <el-form-item :label="$t('form.deployName') + ':'" prop="name">
                <el-input
                  class="w-72"
                  v-model="installForm.name"
                  :placeholder="$t('form.pleaseInput')"
                ></el-input>
              </el-form-item>
              <el-form-item :label="$t('form.description') + ':'">
                <el-input
                  class="w-72"
                  v-model="installForm.description"
                  :placeholder="$t('form.pleaseInput')"
                  type="textarea"
                  maxlength="255"
                  show-word-limit
                  :rows="2"
                ></el-input>
              </el-form-item>
              <el-form-item
                :label="$t('form.cluster') + ':'"
                :rules="[
                  {
                    required: true,
                    message: $t('form.pleaseSelectClusterName'),
                    trigger: 'change',
                  },
                ]"
              >
                <el-select
                  v-model="target_clusters"
                  :placeholder="$t('form.pleaseSelectCluster')"
                  filterable
                  style="margin: 0 5px 2px 0px"
                >
                  <el-option
                    v-for="(item, index) in clusterList"
                    :key="index"
                    :label="item.name"
                    :value="item.id"
                    :disabled="item.online_state != 1"
                  >
                  </el-option>
                </el-select>
                <el-select
                  v-model="target_namespace"
                  filterable
                  :placeholder="$t('form.pleaseSelectNamespace')"
                >
                  <el-option
                    v-for="(item, index) in namespaceList"
                    :key="index"
                    :label="item.objectMeta.name"
                    :value="item.objectMeta.name"
                  >
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('form.version') + ':'">
                <span>{{ installForm.version }}</span>
              </el-form-item>
              <el-form-item :label="$t('form.aosType') + ':'">
                <el-radio-group v-model="installForm.aos_type" size="mini">
                  <el-radio-button label="k8s">k8s</el-radio-button>
                </el-radio-group>
              </el-form-item>
              <el-form-item :label="$t('form.fileType') + ':'" size="mini">
                <el-radio-group v-model="installForm.content_type">
                  <el-radio-button label="yaml">yaml</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
      <el-col v-if="installStatus && installForm.stack_compose">
        <yaml-editor
          :downloadName="queryData.name"
          :downloadType="'yml'"
          style="
            margin-top: 12px;
            min-width: 100%;
            max-height: 600px;
            overflow-y: auto;
          "
          :readonly="''"
          :lint="true"
          v-model="installForm.stack_compose"
          @changeLint="getLintState($event)"
        ></yaml-editor>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  helmChartsDetail,
  helmChartsVersions,
  helmChartsVersionsFiles,
  getClusters,
  getNamespace,
  helmStatcks,
} from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";
let Base64 = require("js-base64").Base64;
export default {
  data() {
    var validateName = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputDeployName")));
      }
      // 校验数字字母
      var reg =
        /^([a-z0-9]([-a-z0-9]*[a-z0-9])?(\.[a-z0-9]([-a-z0-9]*[a-z0-9])?)*)$/;
      if (!reg.test(value)) {
        callback(new Error(this.$t("form.pleaseInputDeployNameTips")));
      } else if (value.length > 52) {
        callback(new Error(this.$t("form.deployNameLengthTips")));
      } else {
        callback();
      }
    };
    return {
      yamlLintState: false,
      loading: false,
      nowVersion: "",
      deployStatus: this.$t("form.deployNow"),
      target_clusters: "",
      target_namespace: "",
      clusterList: [], //集群列表
      namespaceList: [], //命名空间列表
      installStatus: false, //安装状态
      fileText: "", //文件内容
      versionList: [], //版本列表
      versionsFiles: "", //版本文件
      queryData: "",
      defaultImg:
        'this.src="' +
        require("../../assets/images/appMarket/icons8-app-48.png") +
        '"',
      installForm: {
        name: "",
        app_id: "",
        version: "",
        aos_type: "k8s",
        content_type: "yaml",
        description: "",
        stack_compose: "", //
        target_clusters: [],
      },
      installFormRules: {
        name: [{ required: true, validator: validateName, trigger: "blur" }],
      },
    };
  },

  components: { YamlEditor },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  created() {
    this.init();
    this.getClusterList();
  },
  mounted() {},
  watch: {
    nowVersion(val) {
      this.fileText = "";
      this.installForm.version = val;
      this.getVersionFiles(val);
    },

    target_clusters(val) {
      this.getNamespaceList(val);
    },
  },
  methods: {
    getLintState(state) {
      this.yamlLintState = state;
    },
    async init() {
      this.loading = true;
      const list = await helmChartsDetail(this.$route.query.id);
      this.queryData = list;
      this.installForm.app_id = list.app_id;
      this.installForm.name =
        list.name + "-" + this.$script.createRandomStr(6, "a0");
      this.nowVersion = list.latest_version.version;
      this.loading = false;
      this.getVersion();
    },
    async getVersion() {
      const list = await helmChartsVersions(this.$route.query.id);
      this.versionList = list;
    },
    async getVersionFiles(version) {
      const list = await helmChartsVersionsFiles(version, this.$route.query.id);
      this.versionsFiles = list;
      this.fileText = this.getDecode(
        list.files[this.queryData.name + "/values.yaml"]
      );
      this.installForm.stack_compose = this.getDecode(
        list.files[this.queryData.name + "/values.yaml"]
      );
    },
    getDecode(str) {
      return Base64.decode(str);
    },
    download() {
      this.versionList.forEach((item) => {
        if (item.version === this.nowVersion) {
          window.open(item.urls[0]);
        }
      });
    },
    getClusterList() {
      //获取集群列表
      getClusters().then((res) => {
        this.clusterList = res.clusters;
      });
    },
    getNamespaceList(id) {
      //获取命名空间列表
      getNamespace(id).then((res) => {
        this.namespaceList = res.namespaces;
      });
    },
    onSubmit() {
      if (this.yamlLintState) {
        this.$notify({
          title: this.$t("form.yamlError"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      var data = JSON.parse(JSON.stringify(this.installForm));
      if (
        this.target_clusters == "" ||
        this.target_clusters == undefined ||
        this.target_clusters == null
      ) {
        this.$notify({
          title: this.$t("form.pleaseSelectCluster"),
          type: "error",
          duration: 2500,
        });
        return;
      } else if (
        this.target_namespace == "" ||
        this.target_namespace == undefined ||
        this.target_namespace == null
      ) {
        this.$notify({
          title: this.$t("form.pleaseSelectNamespace"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      data.target_clusters.push({
        cluster_id: this.target_clusters,
        cluster_name: this.clusterList.filter(
          (item) => item.id === this.target_clusters
        )[0].name,
        namespace: this.target_namespace,
      });
      this.$refs.installFormRef.validate((valid) => {
        if (valid) {
          helmStatcks(data)
            .then((res) => {
              this.$notify({
                title: this.$t("form.deploySuccess"),
                type: "success",
                duration: 2500,
              });
              this.installStatus = false;
              this.$router.push({
                path: "/appMarket/appInstalled",
              });
            })
            .catch((err) => {
              this.$notify({
                title: err.message,
                type: "error",
                duration: 2500,
              });
            });
        } else {
          this.$notify({
            title: this.$t("form.pleaseCheckForm"),
            type: "error",
            duration: 2500,
          });
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
  color: #000000;
  border: none;
  background-color: transparent;
  margin: 0 -12px;
  font-size: 14px;
}
</style>
