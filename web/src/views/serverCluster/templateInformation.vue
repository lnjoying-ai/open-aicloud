<template>
  <div v-if="detailMain">
    <el-card class="box-card" shadow="never">
      <div class="text item">
        <!-- 集群信息 -->
        <el-form ref="form" label-width="150px" :inline="true">
          <el-form-item
            :label="$t('form.name') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.desc') + ':'"
            style="width: 100%; margin-right: 0"
          >
            <span>{{ detailMain.desc || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.bp') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.bp_name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.owner') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.owner_name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.type') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span v-if="detailMain.type == 0">{{ $t("form.create") }}</span>
            <span v-if="detailMain.type == 1">{{ $t("form.export") }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.clusterName') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.cluster_name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.create_time || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.updateTime') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.update_time || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('cluster.templateNum') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <span>{{ detailMain.version_num || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('cluster.identifier') + ':'"
            style="width: 50%; margin-right: 0"
          >
            <div v-if="detailMain.tags">
              <span
                v-for="(item, index) in detailMain.tags"
                :key="index"
                style="margin-right: 5px"
              >
                <el-tag size="mini">{{ item }}</el-tag>
              </span>
            </div>
            <div v-else>
              <span> - </span>
            </div>
          </el-form-item>
        </el-form>
        <div style="margin-top: 20px">
          <el-table :data="detailMain.versions">
            <el-table-column :label="$t('form.status')">
              <template slot-scope="scope">
                <div v-if="scope.row.enable == true">
                  {{ $t("form.enable") }}
                </div>
                <div v-if="scope.row.enable == false">
                  {{ $t("form.disable") }}
                </div>
              </template>
            </el-table-column>

            <el-table-column prop="version" :label="$t('form.version')">
              <template slot-scope="scope">
                <a
                  style="color: #409eff"
                  @click="versionInformation(scope.row)"
                >
                  {{ scope.row.version ? scope.row.version : "-" }}
                </a>
              </template>
            </el-table-column>
            <el-table-column prop="desc" sortable :label="$t('form.desc')">
              <template slot-scope="scope">
                {{ scope.row.desc ? scope.row.desc : "-" }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('cluster.identifier')">
              <template slot-scope="tags">
                <div v-if="tags.row.tags">
                  <span
                    v-for="(tag, index) in tags.row.tags"
                    :key="index"
                    style="margin-right: 5px"
                  >
                    <el-tag size="mini">{{ tag }}</el-tag>
                  </span>
                </div>
                <div v-else>
                  <span> - </span>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="create_time"
              sortable
              :label="$t('form.createTime')"
            />
            <el-table-column
              prop="update_time"
              sortable
              :label="$t('form.updateTime')"
            />
            <el-table-column :label="$t('form.operation')" width="100">
              <template slot-scope="scopeset">
                <div class="czlb">
                  <el-popover placement="bottom" width="110" trigger="click">
                    <div
                      class="icon_cz"
                      @click="clickVersionStatusBtn(scopeset.row)"
                    >
                      <i class="el-icon-refresh-right" />
                      {{
                        scopeset.row.enable
                          ? $t("form.disable")
                          : $t("form.enable")
                      }}
                    </div>

                    <div
                      class="icon_cz"
                      @click="clickDelVersionBtn(scopeset.row)"
                    >
                      <i class="el-icon-delete" />
                      {{ $t("form.delete") }}
                    </div>
                    <div
                      class="icon_cz"
                      @click="clickEditVersionBtn(scopeset.row, detailMain)"
                    >
                      <i class="el-icon-edit" />
                      {{ $t("form.edit") }}
                    </div>
                    <div
                      class="icon_cz"
                      @click="clickCopyBtn(scopeset.row, detailMain)"
                    >
                      <i class="el-icon-document-copy" />
                      {{ $t("cluster.clone") }}
                    </div>

                    <el-button
                      slot="reference"
                      icon="el-icon-more"
                      class="czbtn right_czbtn"
                    />
                  </el-popover>
                </div>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
    <!-- 表格部分 -->
    <el-row type="flex" class="row-bg" style="margin-top: 10px">
      <el-col :span="14" />
    </el-row>
  </div>
</template>
<script>
import { mapGetters } from "vuex";

import {
  templatesInformation,
  deleteTemplatesVersions,
  reviseTemplatesVersions,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
export default {
  mixins: [initData],
  data() {
    return {
      detailMain: "", // 模板详情
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  components: {},
  watch: {},
  created() {},
  mounted() {
    this.init();
  },
  methods: {
    clickVersionStatusBtn(value) {
      reviseTemplatesVersions(value.id, { enable: !value.enable })
        .then((res) => {
          this.$notify({
            title: value.enable
              ? this.$t("form.disable")
              : this.$t("form.enable"),
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((err) => {});
    },
    // 删除模板版本
    clickDelVersionBtn(value) {
      this.$confirm(
        this.$t("cluster.deleteTemplateVersion"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          deleteTemplatesVersions(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    // 克隆调用的方法
    clickCopyBtn(value, item) {
      if (this.detailMain.cluster_type == "k8s") {
        this.$router.push(
          "/serverCluster/modifyTemplateVersions/clone/" +
            value.id +
            "?owner=" +
            item.owner
        );
      } else {
        this.$router.push(
          "/serverCluster/modifyK3sTemplateVersions/clone/" +
            value.id +
            "?owner=" +
            item.owner
        );
      }
    },
    // 模板版本修改调用的方法
    clickEditVersionBtn(value, item) {
      if (this.detailMain.cluster_type == "k8s") {
        this.$router.push(
          "/serverCluster/modifyTemplateVersions/edit/" +
            value.id +
            "?owner=" +
            item.owner
        );
      } else {
        this.$router.push(
          "/serverCluster/modifyK3sTemplateVersions/edit/" +
            value.id +
            "?owner=" +
            item.owner
        );
      }
    },
    // 带参数跳转到模板版本信息界面
    versionInformation(message) {
      console.log(message);
      if (message.jke_config == null) {
        this.$router.push(
          "/serverCluster/versionInformation/k3s/" + message.id
        );
      } else {
        this.$router.push(
          "/serverCluster/versionInformation/k8s/" + message.id
        );
      }
    },
    init() {
      // 请求模板详情
      templatesInformation(this.$route.params.id).then((res) => {
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
</style>
