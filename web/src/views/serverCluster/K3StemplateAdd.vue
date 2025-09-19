<template>
  <div style="margin-top: -20px">
    <el-card class="box-card mb-2" shadow="never">
      <div class="text item">
        <!-- 集群信息开始 -->
        <el-form ref="templateForm" :model="templateForm" label-width="80px">
          <el-form-item
            :label="$t('form.templateName') + ':'"
            prop="name"
            :rules="[
              {
                required: true,
                validator: validate.k8sName,
                trigger: ['blur', 'change'],
              },
            ]"
          >
            <el-input
              v-model="templateForm.name"
              :disabled="disableStatus"
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.templateDesc') + ':'">
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
        <div style="margin-bottom: 20px; font-weight: bold">
          {{ $t("cluster.identifier") }}
        </div>
        <span style="margin-bottom: 20px">
          <el-form ref="form" :model="templateForm">
            <el-form-item>
              <el-tag
                v-for="tag in templateForm.tags"
                :key="tag"
                closable
                :disable-transitions="false"
                style="margin-right: 30px"
                :disabled="disableStatus"
                @close="handleClose(tag)"
              >
                {{ tag }}
              </el-tag>
              <el-input
                v-if="inputVisible"
                ref="saveTagInput"
                v-model="inputValue"
                class="input-new-tag"
                size="small"
                style="display: inline-block"
                :disabled="disableStatus"
                @keyup.enter.native="handleInputConfirm"
                @blur="handleInputConfirm"
              />
              <el-button
                v-else
                class="button-new-tag"
                size="small"
                :disabled="disableStatus"
                @click="showInput"
                >{{ $t("form.newTag") }}</el-button
              >
            </el-form-item>
            <el-form-item
              v-if="userInfo.kind == '0' || userInfo.kind == '1'"
              :label="$t('cluster.isOtherUserCreate')"
              label-width="150px"
              class="formContentBlock"
            >
              <el-switch v-model="establish" :disabled="disableStatus" />
            </el-form-item>
            <el-form-item
              v-if="(userInfo.kind == '0' || userInfo.kind == '1') && establish"
              label-width="0px"
            >
              <!-- <div> -->
              <el-row type="flex">
                <el-form-item label-width="85px">
                  <div slot="label">
                    {{ $t("cluster.bp") }}
                  </div>
                  <el-select
                    v-model="bpId"
                    filterable
                    :placeholder="$t('form.pleaseSelect')"
                    style="width: 230px"
                    clearable
                    :disabled="disableStatus"
                    @clear="clear"
                    @change="change_bp_id"
                  >
                    <el-option
                      v-for="(item, index) in BpsData"
                      :key="index"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label-width="85px">
                  <div slot="label">
                    {{ $t("form.user") }}
                  </div>
                  <el-select
                    v-model="userId"
                    filterable
                    style="width: 230px"
                    :placeholder="$t('form.pleaseSelect')"
                    clearable
                    :disabled="disableStatus"
                    @change="change_user_id"
                  >
                    <el-option
                      v-for="(item, index) in userData"
                      :key="index"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
              </el-row>
              <!-- </div> -->
              <!-- <el-cascader v-model="clusterUser" :props="props"></el-cascader> -->
            </el-form-item>
          </el-form>
        </span>
      </div>
    </el-card>
    <!-- 集群信息结束-->
    <!-- 集群模板开始-->
    <add-template-versions ref="addTemplateVersions" />
    <!-- 集群模板结束-->
    <!-- 集群选项 -->
    <el-card class="box-card mt-2 mb-2" shadow="never">
      <div class="btnMain" style="text-align: center">
        <el-button type="primary" size="small" @click="onSubmit">{{
          $t("form.submit")
        }}</el-button>
      </div>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";

import initData from "@/mixins/initData";
import {
  getTemplates,
  getBps,
  getUsers,
  templatesInformation,
} from "@/api/mainApi";
import validate from "@/utils/validate";
import addTemplateVersions from "./addTemplateVersions.vue";

export default {
  components: { addTemplateVersions },
  mixins: [initData],
  computed: {
    ...mapGetters(["userInfo"]),
  },
  data() {
    return {
      // 禁用状态
      disableStatus: false,
      // 标题
      title: "",
      validate: validate,
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
    };
  },
  watch: {},

  created() {},
  mounted() {
    this.bpsinit();
    this.userinit();
    if (this.$route.params.id == "add") {
      this.disableStatus = false;
    } else {
      this.getTemInfo();
    }
  },
  methods: {
    // //创建版本
    getTemInfo() {
      // 获取模板信息
      templatesInformation(this.$route.params.id).then((res) => {
        this.templatesInit(res);
        this.title = res.name;
        this.disableStatus = true;
        this.$refs.addTemplateVersions.versionForm.template_id = res.id;
        this.$refs.addTemplateVersions.versionForm.owner = res.owner;
      });
    },
    // 模板数据初始化
    templatesInit(value) {
      this.templateForm.name = value.name;
      this.templateForm.desc = value.desc;
      this.templateForm.tags = value.tags;
      this.templateForm.owner = value.owner;
      this.templateForm.members = value.members;
      this.establish = value.owner != this.userInfo.id;
      this.bpId = value.bp;
      this.userId = value.owner;
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
    // 创建模板调用
    async getTemplates() {
      this.templateForm.owner = this.userId;
      await getTemplates(this.templateForm)
        .then((res) => {
          this.$refs.addTemplateVersions.versionForm.template_id = res;
          this.$refs.addTemplateVersions.versionForm.owner = this.userId;
          this.$refs.addTemplateVersions.onSubmit();
          // this.$router.push("/serverCluster/templateList");
        })
        .catch((err) => {});
    },
    onSubmit() {
      if (this.$refs.addTemplateVersions.yamlLintState) {
        this.$notify({
          title: this.$t("cluster.clusterConfigYamlFormatError"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      this.$refs["templateForm"].validate((valid) => {
        if (valid) {
          if (this.$route.params.id == "add") {
            this.getTemplates();
          } else {
            this.$refs.addTemplateVersions.onSubmit();
          }
        } else {
          this.$notify({
            title: this.$t("form.pleaseCompleteRequiredFields"),
            type: "warning",
            duration: 2500,
          });
          return false;
        }
      });
    },
    // 模板信息标识
    handleClose(tag) {
      if (this.disableStatus) {
        return;
      }
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
    // 重置模板
    resetForm() {
      this.templateForm = {
        name: "",
        desc: "",
        // 标识
        tags: [],
        // 共享成员
        members: { user_id: "", user_name: "", access_role: "" },
      };
    },
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

::v-deep .el-collapse-item__header {
  // padding-left: 10px;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
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
