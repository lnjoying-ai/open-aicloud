<template>
  <div class="warpMain">
    <div class="addappStyle" />
    <el-row>
      <el-col :span="24">
        <el-form
          ref="appForm"
          :model="appForm"
          :rules="rules"
          label-width="180px"
          class="demo-ruleForm"
        >
          <el-form-item prop="group">
            <span slot="label">
              <span>{{ $t("form.group") + ":" }}</span>
              <el-tooltip
                effect="dark"
                :content="$t('form.groupTextTips')"
                placement="bottom-start"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </span>
            <el-input
              v-model="appForm.group"
              class="w-96"
              :placeholder="$t('form.groupTextTipsExample')"
            />
          </el-form-item>
          <el-form-item prop="data_id">
            <span slot="label">
              <span>{{ $t("form.configureName") + ":" }}</span>

              <el-tooltip
                effect="dark"
                :content="$t('form.configureNameTips')"
                placement="bottom-start"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </span>
            <el-input
              v-model="appForm.data_id"
              class="w-96"
              :placeholder="$t('form.configureNameTipsExample')"
            />
          </el-form-item>
          <el-form-item
            v-if="containerData.length > 0 || appData.length > 0"
            :label="$t('form.applyTo') + ':'"
          >
            <el-table
              v-if="containerData.length > 0"
              ref="multipleTable"
              :data="containerData"
              height="160"
              style="width: 100%"
              stripe
              tooltip-effect="dark"
              @selection-change="handleSelectionChange"
            >
              >
              <el-table-column
                prop="name"
                :label="$t('form.containerName')"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="kind == '0' || kind == '1'"
                prop="user_name"
                :label="$t('form.user')"
                show-overflow-tooltip
              />
              <el-table-column
                :label="$t('form.isRestartAll')"
                type="selection"
                width="200"
              />
            </el-table>

            <el-table
              v-if="appData.length > 0"
              ref="multipleTableAPP"
              :data="appData"
              height="160"
              style="width: 100%; margin-top: 10px"
              stripe
              tooltip-effect="dark"
              @selection-change="handleSelectionAPPChange"
            >
              <el-table-column
                prop="name"
                :label="$t('form.applicationName')"
                show-overflow-tooltip
              />
              <el-table-column
                v-if="kind == '0' || kind == '1'"
                prop="user_name"
                :label="$t('form.user')"
                show-overflow-tooltip
              />
              <el-table-column
                :label="$t('form.isRestartAll')"
                type="selection"
                width="200"
              />
            </el-table>
          </el-form-item>
          <el-form-item :label="$t('form.configureFile') + ':'" prop="content">
            <span slot="label">
              <span>{{ $t("form.configureFile") }}</span>

              <el-tooltip
                effect="dark"
                :content="$t('form.configureFileTips')"
                placement="bottom-start"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </span>
            <div>
              <el-button
                type="primary"
                size="small"
                class="drbtn"
                @click="yamlclickLoad"
                >{{ $t("form.import") }}</el-button
              >
              <input
                id="files"
                ref="yamlrefFile"
                type="file"
                style="display: none; margin-top: 5px"
                @change="yamlfileLoad"
              />
            </div>

            <yaml-editor
              ref="yamlEditor"
              v-model="appForm.content"
              style="
                line-height: 1.2;
                max-height: 400px;
                overflow: auto;
                border-radius: 4px;
                margin-top: 10px;
                min-width: 100%;
                display: block;
              "
              :download-name="$t('form.configureFile')"
              :placeholder="''"
              :is-add="false"
              :download-type="'yml'"
              :lint="true"
              @changeLint="getLintState($event)"
            />
          </el-form-item>

          <el-form-item
            style="
              display: flex;
              justify-content: center;
              align-content: center;
            "
          >
            <el-button
              type="primary"
              :loading="loading"
              size="small"
              @click="submitForm('appForm')"
              >{{ $t("form.submit") }}</el-button
            >
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  getConfiguerrContainer,
  getConfiguerApp,
  upDateConfiguerApp,
  upDateConfiguerContainer,
  toConfigs,
  configs,
} from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";
export default {
  components: { YamlEditor },
  mixins: [],
  data() {
    return {
      loading: false,
      appForm: {
        data_id: "",
        group: "",
        content: "",
        namespace: "",
      },
      rules: {
        group: [
          {
            required: true,
            message: this.$t("form.pleaseInputGroupName"),
            trigger: "blur",
          },
          {
            min: 1,
            max: 128,
            message: this.$t("form.groupNameLength"),
            trigger: "blur",
          },
        ],
        data_id: [
          {
            required: true,
            message: this.$t("form.pleaseInputConfigureName"),
            trigger: "blur",
          },
          {
            min: 1,
            max: 255,
            message: this.$t("form.configureNameLength"),
            trigger: "blur",
          },
        ],
        content: [
          {
            required: true,
            message: this.$t("form.pleaseInputConfigureContent"),
            trigger: "blur",
          },
        ],
      },
      // 容器列表
      containerData: [],
      // 表格选择
      multipleSelection: [],
      // 应用列表
      appData: [],
      // 应用选择
      multipleSelectionApp: [],
      // yaml 编辑器状态
      yamlLintState: false,
      // 查询传参
      queryData: {
        data_id: "",
        user_id: "",
        group: "",
      },
    };
  },
  created() {
    if (this.$route.query.id != undefined) {
      this.queryData.data_id = this.$route.query.id;
      this.queryData.user_id = this.userInfo.id;
      this.init();
    }
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  mounted() {},
  beforeDestroy() {},
  destroyed() {},
  methods: {
    // 获取容器列表
    async getContainerList() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getConfiguerrContainer(this.queryData);
      this.containerData = list;
      this.$nextTick(() => {
        for (var i = 0; i < list.length; i++) {
          this.$refs.multipleTable.toggleRowSelection(list[i], true);
        }
      });
    },
    // 获取应用列表
    async getAPPList() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getConfiguerApp(this.queryData);
      this.appData = list.stacks;
      this.$nextTick(() => {
        for (var i = 0; i < list.stacks.length; i++) {
          this.$refs.multipleTableAPP.toggleRowSelection(list.stacks[i], true);
        }
      });
    },
    // 新增应用配置信息
    clickAddData() {
      this.appForm.namespace = this.userInfo.id;
      var data = JSON.parse(JSON.stringify(this.appForm));
      toConfigs(data)
        .then((res) => {
          this.loading = false;
          this.$notify({
            title: this.$t("message.operationSuccess"),
            type: "success",
            duration: 2500,
          });
          if (this.appData.length > 0) {
            this.upDateConfigApp();
          }
          if (this.containerData.length > 0) {
            this.upDateContainerConfig();
          }
          this.$router.push({
            path: "/containerApplicationService/appConfigure",
          });
          this.resetForm("appForm");
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    // 更新应用配置文件
    upDateConfigApp() {
      var data = {
        cfg:
          "cfg:" +
          "//" +
          this.queryData.user_id +
          "/" +
          this.queryData.group +
          "/" +
          this.queryData.data_id,
        stack_ids: [],
        restart: true,
      };
      this.multipleSelectionApp.forEach((value, index) => {
        // value是每一项，index是索引
        data.stack_ids.push(value.id);
      });
      upDateConfiguerApp(data)
        .then((res) => {})
        .catch((err) => {});
    },
    // 更新容器配置文件
    upDateContainerConfig() {
      var data = {
        cfg:
          "cfg:" +
          "//" +
          this.queryData.user_id +
          "/" +
          this.queryData.group +
          "/" +
          this.queryData.data_id,
        container_ids: [],
        restart: true,
      };
      this.multipleSelection.forEach((value, index) => {
        // value是每一项，index是索引
        data.container_ids.push(value.id);
      });
      upDateConfiguerContainer(data)
        .then((res) => {})
        .catch((err) => {});
    },
    // 提交
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        this.loading = true;
        if (valid) {
          this.clickAddData();
        } else {
          this.loading = false;
          return false;
        }
      });
    },
    // 重置表单
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    // 选择框
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 应用
    handleSelectionAPPChange(val) {
      this.multipleSelectionApp = val;
    },
    // yaml 编辑器
    getLintState(state) {
      this.yamlLintState = state;
      this.$emit("getYamlLint", state);
    },
    yamlclickLoad() {
      this.$refs.yamlrefFile.dispatchEvent(new MouseEvent("click"));
    },
    // yaml导入
    yamlfileLoad() {
      var _this = this;
      const selectedFile = _this.$refs.yamlrefFile.files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      } else if (
        selectedFile.name.split("").reverse().join("").split(".")[0] != "txt" &&
        selectedFile.name.split("").reverse().join("").split(".")[0] != "lmy"
      ) {
        this.$notify({
          title: this.$t("form.configureContentFormat"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        _this.appForm.content = this.result;
      };
    },
    // 应用配置信息详情
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await configs(this.queryData);
      this.queryData.group = list.pageItems[0].group;
      list.pageItems[0].data_id = list.pageItems[0].dataId;
      this.appForm = list.pageItems[0];
      this.getContainerList();
      this.getAPPList();
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep
  .el-table__header
  .has-gutter
  tr
  .el-table-column--selection
  .cell
  .el-checkbox:before {
  content: "是否重启（默认全选） ";
  color: #909399;
  font-weight: bold;
}

.addappStyle {
  text-align: right;
  margin-bottom: 15px;
}

::-webkit-scrollbar-thumb {
  border-radius: 1em;
  background-color: rgba(50, 50, 50, 0.3);
}

::-webkit-scrollbar-track {
  border-radius: 1em;
  background-color: rgba(50, 50, 50, 0.1);
}
</style>
