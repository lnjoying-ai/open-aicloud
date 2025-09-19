<template>
  <div class="warpMain">
    <el-row>
      <el-col :span="24">
        <el-form
          size="small"
          :model="queryData"
          class="demo-form-inline"
          label-width="120px"
        >
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
          <el-form-item label="chart:">
            <span>
              <router-link
                style="color: #409eff"
                :to="'/appMarket/appDetail?id=' + queryData.chart_app_id"
              >
                {{ queryData.chart_name }}({{ $t("form.version") }}:{{
                  queryData.chart_version
                }})
              </router-link>
            </span>
          </el-form-item>
          <el-form-item :label="$t('form.cluster') + ':'">
            <span>
              <router-link
                style="color: #409eff"
                :to="
                  '/serverCluster/detail/' +
                  queryData.cluster_id +
                  '/' +
                  queryData.cluster_name
                "
              >
                {{ queryData.cluster_name }}
              </router-link>
            </span>
          </el-form-item>
          <el-form-item :label="$t('form.status') + ':'">
            <span>
              {{
                queryData.status == 1
                  ? $t("form.appCreating")
                  : queryData.status == 2
                  ? $t("form.running")
                  : queryData.status == 3
                  ? $t("form.stop")
                  : queryData.status == 4
                  ? $t("form.deployFailed")
                  : queryData.status == 5
                  ? $t("form.appCleared")
                  : queryData.status == 6
                  ? $t("form.partialRunning")
                  : $t("form.unknown")
              }}
            </span>
          </el-form-item>
          <el-form-item :label="$t('form.updateTime') + ':'">
            <span>
              {{ queryData.update_time }}
            </span>
          </el-form-item>
          <el-form-item :label="$t('form.createTime') + ':'">
            <span>
              {{ queryData.create_time }}
            </span>
          </el-form-item>
          <el-form-item label="value.yaml:">
            <yaml-editor
              v-model="queryData.stack_compose"
              :download-name="queryData.name"
              :download-type="'yml'"
              style="margin-top: 12px; min-width: 100%"
              :readonly="'cursor'"
              :placeholder="''"
              :is-add="false"
            />
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { helmStacksDetail } from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: { YamlEditor },
  data() {
    return {
      queryData: "",
      defaultImg:
        'this.src="' +
        require("../../assets/images/appMarket/icons8-app-48.png") +
        '"',
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
      const list = await helmStacksDetail(this.$route.query.id);
      this.queryData = list;
    },
  },
};
</script>

<style lang="scss" scoped></style>
