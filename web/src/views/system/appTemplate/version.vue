<template>
  <div class="warpMain">
    <div class="addappStyle">
      <el-button type="primary" size="small" @click="createAddApp">{{
        $t("form.createApp")
      }}</el-button>
    </div>
    <el-row>
      <el-col :span="24">
        <el-form label-width="170px" style="width: 100%" :inline="true">
          <div style="overflow: hidden">
            <el-form-item
              :label="$t('form.templateName') + ':'"
              style="width: 50%; float: left"
            >
              <span>{{ tempInfo.name || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.templateVersion') + ':'"
              style="width: 50%; float: left"
            >
              <el-select
                v-model="tempInfo.id"
                :placeholder="$t('form.pleaseSelect')"
                size="mini"
                @change="getTempInfo(tempInfo.id)"
              >
                <el-option
                  v-for="(item, index) in versionList"
                  :key="index"
                  :label="item.version"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item
              id="inlineForm"
              :label="$t('form.logoUrl') + ':'"
              style="width: 50%; float: left"
            >
              <el-tooltip
                effect="dark"
                :content="tempInfo.logo_url"
                placement="top-end"
              >
                <span>
                  {{ tempInfo.logo_url || "-" }}
                </span>
              </el-tooltip>
            </el-form-item>
            <el-form-item
              :label="$t('form.aosType') + ':'"
              style="width: 50%; float: left"
            >
              <span>{{ tempInfo.aos_type || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.templateFormat') + ':'"
              style="width: 50%; float: left"
            >
              <span>{{ tempInfo.content_type || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.templateLabel') + ':'"
              style="width: 100%; float: left"
            >
              <el-tag
                v-for="(item, index) in tempInfo.labels"
                :key="index"
                style="margin-right: 5px"
                size="small"
                ><span>{{ item }}</span></el-tag
              >
            </el-form-item>
          </div>
          <el-form-item
            :label="$t('form.templateDescription') + ':'"
            style="width: 100%"
          >
            <span>{{ tempInfo.description || "-" }}</span>
          </el-form-item>
          <el-form-item
            id="stack_compose"
            label="stack_compose:"
            style="width: 100%"
          >
            <yaml-editor
              ref="yamlEditor"
              v-model="tempInfo.stack_compose"
              style="
                line-height: 1.2;
                max-height: 350px;
                overflow: auto;
                border-radius: 4px;
                margin-top: 10px;
              "
              :download-name="
                tempInfo.name + '-' + tempInfo.version + '-stack_compose'
              "
              :is-add="false"
              :download-type="'yml'"
              :readonly="'nocursor'"
            />
          </el-form-item>
          <el-form-item
            id="justice_compose"
            label="justice_compose:"
            style="width: 100%"
          >
            <yaml-editor
              ref="yamlEditor"
              v-model="tempInfo.justice_compose"
              style="
                line-height: 1.2;
                max-height: 350px;
                min-width: 90%;
                overflow: auto;
                border-radius: 4px;
                margin-top: 10px;
              "
              :download-name="
                tempInfo.name + '-' + tempInfo.version + '-justice_compose'
              "
              :is-add="false"
              :download-type="'yml'"
              :readonly="'nocursor'"
            />
          </el-form-item>
        </el-form>
      </el-col>
      <!-- <el-col :span="8">
        <el-form
          :inline="true"
          size="small"
          :model="tempInfo"
          class="demo-form-inline"
        >
          <el-form-item label="模板版本:" style="float: right">
            <el-select
              v-model="tempInfo.nowVersion"
              placeholder="请选择模板版本"
              style="display: inline-block"
              @change="changeVersions"
            >
              <el-option
                v-for="item in versions"
                :key="item.id"
                :label="item.version"
                :value="item.id"
              >
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
      </el-col> -->
    </el-row>
    <addApp
      ref="addApp"
      :sup_this="sup_this"
      :template="activeInfoTemplate"
      :now-version="tempInfo.version"
    />
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { templatesInfo, appTemplatesInfo } from "@/api/mainApi";
import addApp from "./addApp";
import YamlEditor from "@/components/yaml/editor.vue";
export default {
  components: { YamlEditor, addApp },
  mixins: [],
  data() {
    return {
      tempInfo: {},
      versions: [],
      activeInfoTemplate: "",
      activeInfoApp: "",
      sup_this: this,
      versionList: [],
    };
  },
  created() {},
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  mounted() {
    this.getTemplatesVersionInfo(this.$route.query.nowVersion);
    this.getTemplatesInfo(this.$route.query.temId);
    if (this.$route.query.versions != undefined) {
      this.versions = JSON.parse(this.$route.query.versions);
    }
    console.log(this.$route.query.versions);
  },
  beforeDestroy() {},
  destroyed() {},
  methods: {
    getTempInfo(val) {
      this.getTemplatesVersionInfo(val);
    },
    // 创建应用
    createAddApp() {
      this.activeInfoTemplate = this.tempInfo.nowVersion;
      this.$refs.addApp.id = "";
      this.$refs.addApp.isAdd = true;
      this.$refs.addApp.dialog = true;
    },
    // 版本下拉框改变触发
    changeVersions(val) {
      this.tempInfo.nowVersion = val;
      this.getTemplatesVersionInfo(val);
    },
    // 获取版本详情信息
    getTemplatesVersionInfo(val) {
      templatesInfo(val)
        .then((res) => {
          this.tempInfo = res;
          this.tempInfo.nowVersion = res.id;
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    // 获取模板详情信息
    getTemplatesInfo(val) {
      appTemplatesInfo(val)
        .then((res) => {
          this.versionList = res.versions;
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.addappStyle {
  text-align: right;
  margin-bottom: 15px;
}

::v-deep #inlineForm .el-form-item__content {
  display: inline-block;
  vertical-align: top;
  width: 50%;
  text-overflow: ellipsis;
  overflow: hidden;
  white-space: nowrap;
}

::v-deep #stack_compose .el-form-item__content {
  width: calc(100% - 170px);
  display: inline-block;
}

::v-deep #justice_compose .el-form-item__content {
  width: calc(100% - 170px);
  display: inline-block;
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
