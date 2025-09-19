<template>
  <div class="warpMain">
    <el-row>
      <el-col :span="24">
        <el-form
          ref="refForm"
          size="small"
          :model="form"
          :rules="rules"
          class="demo-form-inline"
          label-width="120px"
        >
          <el-form-item :label="$t('form.name') + ':'" prop="repo_name">
            <el-input v-model="form.repo_name" class="w-52" />
          </el-form-item>
          <el-form-item :label="$t('form.description') + ':'">
            <el-input v-model="form.description" />
          </el-form-item>
          <el-form-item :label="$t('form.targetType') + ':'">
            <el-radio-group v-model="type">
              <el-radio label="1" value="1">http(s)URL</el-radio>
            </el-radio-group>
          </el-form-item>
          <div class="pl-20">
            <el-form-item :label="$t('form.url') + ':'" prop="repo_url">
              <el-input v-model="form.repo_url" />
            </el-form-item>
            <el-form-item :label="$t('form.auth') + ':'">
              <el-select
                v-model="form.auth_method"
                class="w-52"
                :placeholder="$t('form.pleaseSelect')"
              >
                <el-option :label="$t('form.authMethod.noAuth')" :value="0" />
                <el-option
                  :label="$t('form.authMethod.basicAuth')"
                  :value="1"
                />
                <el-option
                  :label="$t('form.authMethod.tokenAuth')"
                  :value="3"
                />
              </el-select>
            </el-form-item>
            <div v-if="form.auth_method == 1">
              <el-form-item
                :label="$t('form.userName') + ':'"
                prop="basic_auth.username"
                :rules="[
                  {
                    required: true,
                    message: $t('form.pleaseInputUserName'),
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input v-model="form.basic_auth.username" class="w-52" />
              </el-form-item>
              <el-form-item
                :label="$t('form.password') + ':'"
                prop="basic_auth.password"
                :rules="[
                  {
                    required: true,
                    message: $t('form.pleaseInputPassword'),
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="form.basic_auth.password"
                  type="password"
                  class="w-52"
                />
              </el-form-item>
            </div>
            <div v-if="form.auth_method == 3">
              <el-form-item
                :label="$t('form.certificate') + ':'"
                prop="basic_auth.cert"
                :rules="[
                  {
                    required: true,
                    message: $t('form.pleaseEnterCertificateContent'),
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="form.basic_auth.cert"
                  type="textarea"
                  class="w-80"
                />
              </el-form-item>
              <el-form-item
                :label="$t('form.secretKey') + ':'"
                prop="basic_auth.key"
                :rules="[
                  {
                    required: true,
                    message: $t('form.pleaseInputSecretKey'),
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="form.basic_auth.key"
                  type="textarea"
                  class="w-80"
                />
              </el-form-item>
              <el-form-item
                :label="$t('form.caCertificate') + ':'"
                prop="basic_auth.ca_cert"
                :rules="[
                  {
                    required: true,
                    message: $t('form.pleaseEnterCaCertificateContent'),
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="form.basic_auth.ca_cert"
                  type="textarea"
                  class="w-80"
                />
              </el-form-item>
            </div>
          </div>
          <el-form-item :label="$t('form.repoPermission') + ':'">
            <el-checkbox-group v-model="form.access">
              <el-checkbox label="list" />
              <el-checkbox label="push" />
              <el-checkbox label="remove" />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item :label="$t('form.label') + ':'">
            <el-tag
              v-for="tag in form.labels"
              :key="tag"
              closable
              size="mini"
              :disable-transitions="false"
              class="mx-1"
              @close="handleClose(tag)"
            >
              {{ tag }}
            </el-tag>
            <el-input
              v-if="inputVisible"
              ref="saveTagInput"
              v-model="inputValue"
              class="input-new-tag w-52"
              size="mini"
              @keyup.enter.native="handleInputConfirm"
              @blur="handleInputConfirm"
            />
            <el-button
              v-else
              class="button-new-tag"
              size="mini"
              @click="showInput"
              >{{ $t("form.newTag") }}</el-button
            >
          </el-form-item>
          <el-form-item class="text-center pt-10">
            <el-button type="primary" @click="onSubmit">{{
              $t("form.create")
            }}</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { addHelmRepos } from "@/api/mainApi";

export default {
  components: {},
  data() {
    return {
      inputVisible: false,
      inputValue: "",
      type: "1", // 目标类型
      form: {
        repo_name: "", // 仓库名称

        description: "", // 仓库描述
        repo_url: "", // 仓库地址
        auth_method: 0, // 认证方式 0不鉴权 1basic鉴权 b3sercret鉴权
        basic_auth: {
          username: "", // 用户名
          password: "", // 密码
        },
        secret_auth: {
          cert: "", // 证书
          key: "", // 密钥key
          ca_cert: "", // ca证书
        },
        access: ["list"], // 访问控制
        labels: [], // 标签
      },
      rules: {
        repo_name: [
          {
            required: true,
            message: this.$t("form.pleaseInputRepositoryName"),
            trigger: "blur",
          },
        ],
        repo_url: [
          {
            required: true,
            message: this.$t("form.pleaseInputRepoUrl"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  created() {},
  mounted() {},
  methods: {
    onSubmit() {
      var data = JSON.parse(JSON.stringify(this.form));
      //  labels list转map
      const map = {};
      data.labels.forEach((item, index) => {
        map[index] = item;
      });
      data.labels = map;

      // 必填项
      this.$refs["refForm"].validate((valid) => {
        if (valid) {
          addHelmRepos(data)
            .then((res) => {
              this.$notify({
                title: this.$t("message.createSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$router.push("/appMarket/repoList");
            })
            .catch((err) => {
              this.$notify({
                title: err.message,
                type: "error",
                duration: 2500,
              });
            });
        }
      });
    },
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
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
        this.form.labels.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
  },
};
</script>

<style lang="scss" scoped></style>
