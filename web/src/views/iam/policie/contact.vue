<template>
  <div class="mt-2">
    <el-card shadow="never" class="pb-2">
      <el-form
        ref="form"
        :model="info"
        :rules="rules"
        size="mini"
        label-width="120px"
      >
        <el-form-item
          v-if="
            userInfo.kind != 0 && userInfo.kind != 1 && info.policy_type == 1
          "
          :label="$t('form.name') + ':'"
        >
          <span>{{ info.display_name ? info.display_name : "-" }}</span>
        </el-form-item>
        <el-form-item v-else :label="$t('form.name') + ':'" prop="display_name">
          <el-input
            v-model="info.display_name"
            :placeholder="$t('form.pleaseInputPolicyName')"
          />
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.type') + ':'"
          prop="policy_type"
        >
          <el-select
            v-model="info.policy_type"
            :placeholder="$t('form.pleaseSelectPolicyType')"
            style="width: 100%"
          >
            <el-option
              :value="1"
              :label="$t('form.policyTypeOptions.system')"
            />
            <el-option
              :value="2"
              :label="$t('form.policyTypeOptions.custom')"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-else :label="$t('form.type') + ':'">
          <span>
            {{
              info.policy_type == 1
                ? $t("form.policyTypeOptions.system")
                : $t("form.policyTypeOptions.custom")
            }}
          </span>
        </el-form-item>
        <el-form-item
          v-if="
            userInfo.kind != 0 && userInfo.kind != 1 && info.policy_type == 1
          "
          :label="$t('form.description') + ':'"
        >
          {{ info.description ? info.description : "-" }}
        </el-form-item>
        <el-form-item v-else :label="$t('form.description') + ':'">
          <el-input
            v-model="info.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            :placeholder="$t('form.pleaseInputPolicyDescription')"
            autosize
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.policyContent') + ':'"
          :prop="'policy_document.statement'"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputPolicyContent'),
              trigger: 'blur',
            },
          ]"
        >
          <div
            v-if="
              (userInfo.kind != 0 &&
                userInfo.kind != 1 &&
                info.policy_type == 2) ||
              userInfo.kind == 0 ||
              userInfo.kind == 1
            "
            style="margin-bottom: 10px"
          >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="handleAddDocument()"
              >{{ $t("form.rule") }}</el-button
            >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="handleAddDocument()"
              >{{ $t("form.operation") }}</el-button
            >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="handleAddDocument()"
              >{{ $t("form.resource") }}</el-button
            >
          </div>
          <el-collapse
            v-if="info.policy_document && info.policy_document.statement"
            @change="handleChange"
          >
            <div
              v-for="(item, index) in info.policy_document.statement"
              :key="item.rule + index"
            >
              <el-collapse-item :name="index" style="position: relative">
                <template slot="title">
                  <div>
                    <span
                      >{{ $t("form.policyStatement") }} {{ index + 1 }}
                    </span>
                    <el-button
                      v-if="
                        (userInfo.kind != 0 &&
                          userInfo.kind != 1 &&
                          info.policy_type == 2) ||
                        userInfo.kind == 0 ||
                        userInfo.kind == 1
                      "
                      size="mini"
                      icon="el-icon-delete"
                      plain
                      type="text"
                      style="position: absolute; right: 30px; top: 10px"
                      @click="handleDeleteDocument(item, index)"
                    >
                      {{ $t("form.delete") }}</el-button
                    >
                  </div>
                </template>
                <el-row :gutter="20">
                  <el-col :span="24" class="line03">
                    <yaml-editor
                      v-if="collapseData.includes(index)"
                      ref="yamlEditor"
                      v-model="item.rule"
                      class="leading-tight overflow-auto rounded max-h-96 min-w-full"
                      :download-name="$t('form.policyContent')"
                      :download-type="'yml'"
                      :readonly="false"
                      :lint="false"
                    />
                  </el-col>
                </el-row>
              </el-collapse-item>
            </div>
          </el-collapse>
        </el-form-item>
        <el-form-item
          :label="$t('form.defaultVersion') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.default_version ? info.default_version : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.attachmentCount') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.attachment_count ? info.attachment_count : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.isAuthorized') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.attachable ? $t("form.yes") : $t("form.no") }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.resourceNumber') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.arn ? info.arn : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.belongOrganization') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.bp_name ? info.bp_name : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.policyUser') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.user_name ? info.user_name : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.createTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.create_time ? info.create_time : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.modifyTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ info.update_time ? info.update_time : "-" }}</span>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button
            v-if="
              (userInfo.kind != 0 &&
                userInfo.kind != 1 &&
                info.policy_type == 2) ||
              userInfo.kind == 0 ||
              userInfo.kind == 1
            "
            :loading="loadingForm"
            type="primary"
            size="small"
            @click="doEdit()"
            >{{ $t("form.save") }}</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
    <div class="mt-2" shadow="never">
      <el-card shadow="never">
        <div slot="header" class="clearfix">
          <span>{{ $t("form.relatedEntities") }}</span>
        </div>
        <el-tabs v-model="activeName" style="margin: 0px 10px">
          <el-tab-pane :label="$t('form.userInfo')" name="first">
            <el-table
              :data="detailMain.users"
              stripe
              tooltip-effect="dark"
              style="width: 100%"
              max-height="400"
              :default-sort="{ prop: 'date', order: 'descending' }"
            >
              <el-table-column prop="userName" :label="$t('form.userName')">
                <template slot-scope="scope">
                  <router-link
                    style="color: #409eff"
                    :to="'/iam/user/detail/' + scope.row.user_id"
                  >
                    {{ scope.row.user_name }}
                  </router-link>
                </template>
              </el-table-column>
              <el-table-column prop="bp_name" :label="$t('form.bpName')" />
              <el-table-column prop="email" :label="$t('form.email')">
                <template slot-scope="scope">
                  {{ scope.row.email }}
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
          <el-tab-pane :label="$t('form.userGroupInfo')" name="second">
            <el-table
              :data="detailMain.groups"
              stripe
              tooltip-effect="dark"
              style="width: 100%"
              max-height="400"
              :default-sort="{ prop: 'date', order: 'descending' }"
            >
              <el-table-column prop="name" :label="$t('form.userGroupName')" />
              <el-table-column prop="desc" :label="$t('form.description')" />
              <el-table-column
                prop="create_time"
                :label="$t('form.createTime')"
                sortable
              />
              <el-table-column
                prop="update_time"
                :label="$t('form.modifyTime')"
                sortable
              />
            </el-table>
          </el-tab-pane>
          <el-tab-pane :label="$t('form.roleInfo')" name="third">
            <el-table
              :data="detailMain.roles"
              stripe
              tooltip-effect="dark"
              style="width: 100%"
              max-height="400"
              :default-sort="{ prop: 'date', order: 'descending' }"
            >
              <el-table-column prop="name" :label="$t('form.roleName')">
                <template slot-scope="scope">
                  <router-link
                    style="color: #409eff"
                    :to="'/iam/role/detail/' + scope.row.role_id"
                  >
                    {{ scope.row.name }}
                  </router-link>
                </template>
              </el-table-column>
              <el-table-column prop="role_type" :label="$t('form.roleType')" />
              <el-table-column prop="bp_name" :label="$t('form.bpName')" />
              <el-table-column
                prop="iam_code"
                :label="$t('form.serviceCode')" />
              <el-table-column prop="user_name" :label="$t('form.userInfo')" />
              <el-table-column
                prop="description"
                :label="$t('form.description')" />
              <el-table-column
                prop="create_time"
                :label="$t('form.createTime')"
                sortable />
              <el-table-column
                prop="update_time"
                :label="$t('form.modifyTime')"
                sortable /></el-table
          ></el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script>
import { entitiesPolicie, detailPolicie, editPolicie } from "@/api/iam/policie";
import YamlEditor from "@/components/yaml/editor.vue";
import { mapGetters } from "vuex";
var Base64 = require("js-base64").Base64;
export default {
  components: {
    YamlEditor,
  },
  data() {
    return {
      Base64: require("js-base64").Base64,
      collapseData: [], // 折叠面板数据
      detailMain: {},
      loadingForm: false,
      loading: false,
      info: {
        display_name: "",
        policy_document: {
          version: "",
          type: "rego",
          statement: [],
        },
      },
      activeName: "first",
      rules: {
        display_name: [
          {
            required: true,
            message: this.$t("message.pleaseInputPolicyName"),
            trigger: "blur",
          },
        ],
        policy_document: [
          {
            required: true,
            message: this.$t("message.pleaseInputPolicyContent"),
            trigger: "blur",
          },
        ],
      },
    };
  },

  async created() {
    this.init();
    this.getEntities();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    async getEntities() {
      const list = await entitiesPolicie(this.$route.params.id);
      this.detailMain = list;
    },
    async init() {
      const list = await detailPolicie(this.$route.params.id);
      this.info = list;
      var document = JSON.parse(list.document);
      this.info.policy_document = document;
      for (var i = 0; i < this.info.policy_document.statement.length; i++) {
        this.info.policy_document.statement[i].rule = Base64.decode(
          this.info.policy_document.statement[i].rule
        );
      }
    },
    handleAddDocument() {
      this.info.policy_document.statement.push({
        package: "",
        rule: "",
      });
      this.$forceUpdate();
    },
    handleDeleteDocument(item, index) {
      this.info.policy_document.statement.splice(index, 1);
      this.$forceUpdate();
    },
    handleChange(value) {
      this.collapseData = value;
    },

    doEdit() {
      delete this.info.document;
      this.loadingForm = true;
      var data = JSON.parse(JSON.stringify(this.info));
      data.policy_document.statement.forEach((element) => {
        element.rule = Base64.encode(element.rule);
      });
      data.policy_document = JSON.stringify(data.policy_document);
      editPolicie(this.$route.params.id, data)
        .then((res) => {
          this.loadingForm = false;
          this.$notify({
            title: $t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((err) => {
          this.loadingForm = false;
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.line03 {
  animation: animation01_line01 0.5s ease-in forwards;
}

@keyframes animation01_line01 {
  0% {
    opacity: 0.1;
  }

  50% {
    opacity: 0.5;
  }

  60% {
    opacity: 0.6;
  }

  90% {
    opacity: 0.9;
  }

  100% {
    opacity: 1;
  }
}

::v-deep .el-button.is-plain:hover {
  border-color: transparent;
}
</style>
