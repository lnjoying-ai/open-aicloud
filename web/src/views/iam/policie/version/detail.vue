<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form ref="form" :model="detailMain" size="small" label-width="140px">
        <el-form-item
          :label="$t('form.policyVersion') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ detailMain.version_id ? detailMain.version_id : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.defaultVersion') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.default_version ? $t("form.yes") : $t("form.no")
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.versionDescription') + ':'"
          style="width: 100%; float: left"
        >
          <span>{{
            detailMain.description ? detailMain.description : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.createTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.create_time ? detailMain.create_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.modifyTime')"
          style="width: 50%; float: left"
        >
          <span>{{
            detailMain.update_time ? detailMain.update_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.policyContent') + ':'"
          style="width: 100%; margin: 0px 0px 20px 0px; float: left"
        >
          <div
            v-for="(item, index) in detailMain.document.statement"
            :key="index"
          >
            <el-row>
              <el-col :span="24">
                <yaml-editor
                  ref="yamlEditor"
                  v-model="item.rule"
                  class="leading-tight overflow-auto rounded mt-2 max-h-96 min-w-full"
                  :download-name="$t('form.policyContent')"
                  :download-type="'yml'"
                  :readonly="'true'"
                  :lint="false"
                />
              </el-col>
            </el-row>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { detailPolicieVersion } from "@/api/iam/policie";
import YamlEditor from "@/components/yaml/editor.vue";
var Base64 = require("js-base64").Base64;

export default {
  components: { YamlEditor },
  data() {
    return {
      detailMain: {
        version_id: "",
        default_version: "",
        description: "",
        document: {
          version: "",
          type: "rego",
          statement: [
            {
              package: "",
              rule: "",
            },
          ],
        },
        create_time: "",
        update_time: "",
      },
    };
  },

  async created() {
    this.init();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    async init() {
      const list = await detailPolicieVersion(
        this.$route.params.policyId,
        this.$route.params.versionId
      );
      this.detailMain = list;
      this.detailMain.document = JSON.parse(this.detailMain.document);
      for (var i = 0; i < this.detailMain.document.statement.length; i++) {
        this.detailMain.document.statement[i].rule = Base64.decode(
          this.detailMain.document.statement[i].rule
        );
      }
    },
  },
};
</script>
<style scoped>
.yaml-editor {
  height: 100%;
  position: relative;
}
</style>
