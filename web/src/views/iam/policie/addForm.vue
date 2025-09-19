<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="120px"
      >
        <el-form-item :label="$t('form.name') + ':'" prop="display_name">
          <el-input
            v-model="form.display_name"
            :placeholder="$t('form.pleaseInputPolicyName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            :rows="2"
            :placeholder="$t('form.pleaseInputPolicyDescription')"
          />
        </el-form-item>
        <el-form-item
          v-if="kind == 0 || kind == 1"
          :label="$t('form.policyType') + ':'"
          prop="policy_type"
        >
          <el-select
            v-model="form.policy_type"
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
          <div>
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="handleAddDocument($t('form.policyRule'))"
              >{{ $t("form.rule") }}</el-button
            >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="handleAddDocument($t('form.policyOperation'))"
              >{{ $t("form.operation") }}</el-button
            >
            <el-button
              type="primary"
              size="mini"
              icon="el-icon-plus"
              @click="handleAddDocument($t('form.policyResource'))"
              >{{ $t("form.resource") }}</el-button
            >
            <el-button
              type="text"
              style="margin: 0px 5px 0px 20px"
              @click="handleDrawerClick"
              >{{ $t("form.howToWritePolicy") }}</el-button
            >
          </div>
          <div
            v-for="(item, index) in form.policy_document.statement"
            :key="index"
            style="margin-top: 10px"
          >
            <el-row :gutter="20">
              <el-col :span="22">
                <span
                  style="font-weight: 600; font-size: 14px; color: #606266"
                  >{{ item.title }}</span
                >
                <yaml-editor
                  ref="yamlEditor"
                  v-model="item.rule"
                  class="leading-tight overflow-auto rounded max-h-96 min-w-full"
                  :download-name="$t('form.policyContent')"
                  :download-type="'yml'"
                  :readonly="false"
                  :lint="false"
                />
              </el-col>
              <el-col :span="2" style="margin-top: 30px">
                <el-button
                  size="mini"
                  icon="el-icon-remove-outline"
                  plain
                  type="danger"
                  @click="handleDeleteDocument(item, index)"
                >
                  {{ $t("form.delete") }}</el-button
                >
              </el-col>
            </el-row>
          </div>
        </el-form-item>
      </el-form>
      <div class="flex justify-center mt-6 px-4">
        <el-button size="small" type="text" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-card>
    <el-drawer
      :title="$t('form.howToWritePolicy')"
      :visible.sync="drawer"
      :before-close="handleClose"
      size="50%"
    >
      <template>
        <article
          class="markdown-body"
          style="text-align: left"
          v-html="content"
        />
      </template>
    </el-drawer>
  </div>
</template>

<script>
import { addPolicie } from "@/api/iam/policie";
import "github-markdown-css/github-markdown.css";
import YamlEditor from "@/components/yaml/editor.vue";
var Base64 = require("js-base64").Base64;
import { mapGetters } from "vuex";

export default {
  components: {
    YamlEditor,
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  data() {
    return {
      loading: false,
      drawer: false,
      content: `<h2 id="1">1 规则语句</h2>
<h3 id="1-1">（1）普通规则</h3>
<p>普通规则用于对操作语句和资源语句的控制，一个策略中至少包含一个规则。规则必须以rule__（两个下划线）开头。</p>
<pre><code>package lnjoying.generated.conditionset.rules

import future.keywords.in
import data.lnjoying.generated.abac.utils.attributes
import data.lnjoying.generated.conditionset


rule__xxxx[action] {

    some action in conditionset.actionset__xx
    conditionset.resourceset_xx
}
</code></pre>
<h3 id="2">（2） 边界规则</h3>
<p>边界规则是一种安全策略，用于限制系统中的特定行为、资源访问或权限。它们帮助确保系统在预定的边界内运行，并防止非法或潜在危险的操作。边界规则用于对操作语句和资源语句的控制，一个策略中至少包含一个规则。规则必须以boundary__ rule__（两个下划线）开头。</p>
<pre><code>package lnjoying.generated.conditionset.rules

import future.keywords.in
import data.lnjoying.generated.abac.utils.attributes
import data.lnjoying.generated.conditionset


boundary__rule__xxxx {

    some action in conditionset.actionset__xx
    conditionset.resourceset_xx
}
</code></pre>
<h2 id="2-1">2 操作语句</h2>
<p>操作语句用于定于规则中使用的操作集合。操作语句以actionset_（两个下划线）开头。操作支持精确匹配和模糊匹配，精确匹配例如：input.action in ["iam:getXX", "iam:xx"]， 模糊匹配例如matches_actions(input.action, actions)， actions定义允许的操作集合。具体的操作名称在资源管理详情的操作列表中查找。</p>
<pre><code>package lnjoying.generated.conditionset

import future.keywords.in
import data.lnjoying.generated.abac.utils.attributes
import data.lnjoying.generated.abac.utils._matches_actions

actionset__xx [actions]{

    attributes.resource.type == "iam:xx"
     actions := ["iam:*", "*", "iam:xx"]

    # exact match
    #input.action in actions
    # fuzzy match
    _matches_actions(input.action, actions)
}
</code></pre>
<h2 id="3">3 资源语句</h2>
<p>资源语句用于对资源的控制。资源语句以resource__（两个下划线）开头。</p>
<pre><code>package lnjoying.generated.conditionset

import future.keywords.in
import data.lnjoying.generated.abac.utils.attributes

default resourceset_xx := false

resourceset_xx {
    attributes.resource.type == "iam:xx"
    resourceset_xx_any_of_0
    resourceset_xx_any_of_1
}

default resourceset_xx_any_of_0 = false

resourceset_xx_any_of_0 {
    attributes.resource.limit &gt;= 20
}

resourceset_xx_any_of_0 {
    attributes.resource.limit in [8, 18, 28]
}

default resourceset_xx_any_of_1 = false

resourceset_xx_any_of_1 {
    attributes.resource.limit &gt;= 60
}

resourceset_xx_any_of_1 {
    attributes.resource.name == "abc"
}
</code></pre>
<h2 id="4">4 策略变量</h2>
<h3 id="1input">（1） 输入相关(input)：</h3>
<ul>
<li>#### 用户相关：</li>
</ul>
<pre><code>input.user.key 当前用户id
input.user.bp  当前用户归属组织
input.user.kind 当前用户类型
input.user.attributes.userName 当前用户名
input.user.attributes.bpName 当前组织名
</code></pre>
<ul>
<li>#### 资源变量</li>
</ul>
<pre><code>input.resource.project 当前资源归属项目
input.resource.type    当前资源类型
input.resource.attributes["xx"]  xx可以是资源属性，标识当前资源的某个属性值
</code></pre>
<h3 id="2abacattributes">（2） 导入 abac attributes时</h3>
<p>import data.lnjoying.generated.abac.utils.attributes</p>
<pre><code>attributes.user.userName 当前用户名
attributes.user.bpName   当前组织名

attributes.resource.project 当前资源归属项目
attributes.resource.type 当前资源类型
attributes.resource["xx"]  xx可以是资源属性，标识当前资源的某个属性值
</code></pre>
<p>注：当不确定xx属性是否存在时，使用  object.get(input.resource.attributes,"xx", "") 来获取，</p>
<p><a href="https://www.openpolicyagent.org/docs/latest/policy-language/#the-basics">更多参考</a></p>
<h2 id="">内置函数</h2>
<p>除了rego自身的函数，也自定义了一些函数。</p>
<p>（1）matchesactions(haystack, needles)</p>
<p>模糊匹配。</p>
<p>导入import data.lnjoying.generated.abac.utils.matchesactions</p>
<pre><code>  actions := ["*"]
  _matches_actions(input.action, actions)
</code></pre>
<p>（2）cus_concat(a, b)</p>
<p>合并两个字符串。</p>
<p>导入import data.lnjoying.generated.abac.utils.cus_concat</p>
<pre><code>cus_concat("hello", "world") # helloworld
</code></pre>`,

      form: {
        policy_name: "",
        display_name: "",
        description: "",
        policy_type: 2,
        policy_document: {
          version: "",
          type: "rego",
          statement: [],
        },
      },
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
  methods: {
    // 监听markdown变化
    change(value, render) {
      this.html = render;
      this.blogInfo.blogMdContent = value;
      this.blogInfo.blogContent = render;
    },
    // 上传图片接口pos 表示第几个图片
    handleEditorImgAdd(pos, $file) {
      var formdata = new FormData();
      formdata.append("file", $file);
      this.$axios
        .post("http://localhost:8000/blogs/image/upload/", formdata)
        .then((res) => {
          var url = res.data.data;
          this.$refs.md.$img2Url(pos, url); // 这里就是引用ref = md 然后调用$img2Url方法即可替换地址
        });
    },
    handleEditorImgDel() {
      console.log("handleEditorImgDel"); // 我这里没做什么操作，后续我要写上接口，从七牛云CDN删除相应的图片
    },
    handleAddDocument(value) {
      var title = value;
      this.form.policy_document.statement.push({
        package: "",
        rule: "",
        title: title,
      });
    },
    handleDeleteDocument(item, index) {
      this.form.policy_document.statement.splice(index, 1);
    },
    handleDrawerClick() {
      this.drawer = true;
    },
    handleClose() {
      this.drawer = false;
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          this.doAdd();
        } else {
          return false;
        }
      });
    },
    cancel() {
      this.resetForm();
    },
    doAdd() {
      var data = JSON.parse(JSON.stringify(this.form));
      data.policy_document.statement.forEach((element) => {
        delete element.title;
        element.rule = Base64.encode(element.rule);
      });
      data.policy_document = JSON.stringify(data.policy_document);

      addPolicie(data)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.searchinit();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.form = {
        policy_name: "",
        display_name: "",
        description: "",
        policy_type: 2,
        policy_document: {
          statement: [
            {
              package: "",
              rule: "",
            },
          ],
        },
      };
      this.$router.push({
        path: "/iam/policie",
      });
    },
  },
};
</script>

<style scoped>
.markdown-body {
  box-sizing: border-box;
  min-width: 200px;
  margin: 0 auto;
  padding-left: 20px;
  padding-right: 15px;
  height: 89vh;
  overflow: auto;
  font-size: 14px;
}

.markdown-body .highlight pre,
.markdown-body pre {
  padding: 16px;
  overflow: auto;
  font-size: 85%;
  line-height: 1.45;
  background-color: var(--color-canvas-subtle);
  border-radius: 6px;
  font-family: "宋体";
}

::v-deep .el-drawer__header {
  color: black;
}
</style>
