<template>
  <div class="warpMain">
    <el-row>
      <el-col :span="24">
        <el-form
          size="small"
          :model="form"
          class="demo-form-inline"
          label-width="120px"
        >
          <el-form-item :label="$t('form.name') + ':'">
            <span>{{ form.repo_name ? form.repo_name : "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.description') + ':'">
            <span>{{ form.description ? form.description : "-" }}</span>
          </el-form-item>
          <el-form-item :label="$t('form.targetType') + ':'">
            <el-radio-group v-model="type">
              <el-radio label="1" value="1">http(s)URL</el-radio>
            </el-radio-group>
          </el-form-item>
          <div class="pl-20">
            <el-form-item :label="$t('form.url') + ':'">
              <span>{{ form.repo_url ? form.repo_url : "-" }}</span>
            </el-form-item>
            <el-form-item :label="$t('form.auth') + ':'">
              <span>{{
                form.auth_method === 0
                  ? $t("form.authMethod.noAuth")
                  : form.auth_method === 1
                  ? $t("form.authMethod.basicAuth")
                  : form.auth_method === 3
                  ? $t("form.authMethod.tokenAuth")
                  : ""
              }}</span>
            </el-form-item>
          </div>

          <el-form-item :label="$t('form.label') + ':'">
            <el-tag
              v-for="tag in form.labels"
              :key="tag"
              size="mini"
              :disable-transitions="false"
              class="mx-1"
            >
              {{ tag }}
            </el-tag>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { HelmReposDetail } from "@/api/mainApi";

export default {
  components: {},
  data() {
    return {
      inputVisible: false,
      inputValue: "",
      type: "1", // 目标类型
      form: {},
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  created() {
    this.init();
  },
  mounted() {},
  methods: {
    async init() {
      const list = await HelmReposDetail(this.$route.params.name);
      list.labels = JSON.parse(list.labels);
      console.log(list);
      this.form = list;
    },
  },
};
</script>

<style lang="scss" scoped></style>
