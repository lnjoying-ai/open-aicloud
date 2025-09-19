<template>
  <div>
    <el-card class="box-card" shadow="never" style="position: relative">
      <router-link
        :to="
          '/serverCluster/add?type=isUse&temId=' +
          detailMain.template_id +
          '&verId=' +
          detailMain.id
        "
      >
        <el-button
          v-if="detailMain.enable"
          type="primary"
          style="position: absolute; top: 0; right: 20px"
          size="mini"
        >
          {{ $t("cluster.clusterCreate") }}
        </el-button>
      </router-link>
      <div v-if="detailMain" class="text item">
        <!-- 集群信息 -->
        <el-form ref="form" label-width="150px" :inline="true">
          <el-form-item
            :label="$t('cluster.versionNumber') + ':'"
            style="width: 50%"
          >
            <span>{{ detailMain.version || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('cluster.isEnabled') + ':'"
            style="width: 50%"
          >
            <span v-if="detailMain.enable == true">{{
              $t("cluster.yes")
            }}</span>
            <span v-if="detailMain.enable == false">{{
              $t("cluster.no")
            }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('cluster.versionDescription') + ':'"
            style="width: 50%"
          >
            <span>{{ detailMain.desc || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('cluster.versionIdentifier') + ':'"
            style="width: 50%"
          >
            <el-tag
              v-for="(tag, index) in detailMain.tags"
              :key="index"
              style="margin-right: 5px"
              ><span>{{ tag }}</span></el-tag
            >
          </el-form-item>
          <el-form-item
            :label="$t('cluster.createTime') + ':'"
            style="width: 50%"
          >
            <span>{{ detailMain.create_time || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('cluster.updateTime') + ':'"
            style="width: 50%"
          >
            <span>{{ detailMain.update_time || "-" }}</span>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 表格部分 -->
    <el-row type="flex" class="row-bg" style="margin-top: 10px">
      <el-col v-if="this.$route.params.type == 'k8s'" :span="24">
        <info-show
          ref="infoRef"
          :yaml-editor-name="
            detailMain.template_name + '-' + detailMain.version
          "
          :jke-config-data="detailMain.jke_config"
          :detail-main="detailMain"
        />
      </el-col>
      <el-col v-if="this.$route.params.type == 'k3s'" :span="24">
        <K3SinfoShowVue
          ref="infoRef"
          :yaml-editor-name="
            detailMain.template_name + '-' + detailMain.version
          "
          :k3s-config-data="detailMain.k3s_config"
          :detail-main="detailMain"
        />
      </el-col>
    </el-row>
  </div>
</template>
<script>
import { templatesVersions } from "@/api/mainApi";
import K3SinfoShowVue from "./components/K3SinfoShow.vue";
import infoShow from "./components/infoShow";
export default {
  components: {
    infoShow,
    K3SinfoShowVue,
  },

  data() {
    return {
      detailMain: "", // 版本详情
    };
  },
  watch: {},
  created() {},
  mounted() {
    this.init();
    this.$refs.infoRef.status = 1;
  },
  methods: {
    init() {
      // 请求模板详情
      templatesVersions(this.$route.params.id).then((res) => {
        if (this.$route.params.type == "k8s") {
          if (typeof res.jke_config === "string") {
            res.jke_config = JSON.parse(res.jke_config);
          }
        } else {
          if (typeof res.k3s_config === "string") {
            res.k3s_config = JSON.parse(res.k3s_config);
          }
        }
        this.detailMain = res;
      });
    },
  },
};
</script>
<style lang="scss" scoped>
.row-bg {
  padding: 10px 0;
}

.el-col-4 {
  width: 40%;
  font-size: 15px;
  font-family: "Microsoft YaHei ", "Microsoft YaHei", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: rgba(0, 0, 0, 0.8470588235294118);
  line-height: 22px;
}

::v-deep .el-table th {
  background: #fafafa;
}

::v-deep .el-card {
  border: 0px solid #ebeef5;
  background-color: #fff;
  color: #303133;
  -webkit-transition: 0.3s;
  transition: 0.3s;
}

::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

::v-deep .el-collapse-item__header {
  background-color: rgba(250, 250, 250, 1) !important;
  margin-bottom: 5px;
}
</style>
