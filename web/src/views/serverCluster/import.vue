<template>
  <div>
    <!-- 第一步 集群基础信息 -->
    <!-- 集群信息 -->
    <el-card class="box-card mt-2 mb-2" shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("form.basicInfo") }}</span>
      </div>
      <div class="text item">
        <!-- 集群信息 -->
        <el-form
          ref="importclusterForm"
          :model="clusterForm"
          label-width="100px"
          :rules="rules"
        >
          <el-form-item :label="$t('form.name') + ':'" prop="name">
            <el-input
              v-model="clusterForm.name"
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.desc') + ':'">
            <el-input
              v-model="clusterForm.desc"
              type="textarea"
              maxlength="255"
              show-word-limit
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
        </el-form>
        <el-collapse v-model="tabsAndNotes">
          <el-collapse-item :title="$t('form.user')" name="1">
            <el-form ref="clusterForm" :model="clusterForm" label-width="180px">
              <el-form-item
                :label="$t('cluster.isOtherUserCreate') + ':'"
                class="formContentBlock"
              >
                <el-switch v-model="clusterIsUse" />
              </el-form-item>
              <el-form-item v-if="clusterIsUse" label-width="0px">
                <!-- <div> -->
                <el-form-item class="mb-6">
                  <div slot="label">{{ $t("form.bp") }}:</div>
                  <el-select
                    v-model="bpId"
                    :placeholder="$t('form.pleaseSelect')"
                    style="width: 230px"
                    clearable
                    filterable
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
                <el-form-item>
                  <div slot="label">{{ $t("form.user") }}:</div>
                  <el-select
                    v-model="userId"
                    filterable
                    style="width: 230px"
                    :placeholder="$t('form.pleaseSelect')"
                    clearable
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
              </el-form-item>
            </el-form>
          </el-collapse-item>
          <el-collapse-item :title="$t('cluster.tagsAndNotes')" name="2">
            <el-row :gutter="40">
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-table
                    size="small"
                    :data="clusterForm.labels"
                    style="width: 100%; margin: 0 auto; max-width: 600px"
                  >
                    <el-table-column prop="key" :label="$t('form.key')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.key"
                          size="small"
                          :placeholder="$t('form.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="value" :label="$t('form.value')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.value"
                          size="small"
                          :placeholder="$t('form.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.operation')">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          @click="tabshandleClose(scope.$index)"
                          >{{ $t("form.delete") }}</el-button
                        >
                      </template>
                    </el-table-column>
                  </el-table>

                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="small"
                    style="margin: 10px auto; display: block"
                    @click="tabsshowInput()"
                    >{{ $t("cluster.addTag") }}</el-button
                  >
                </div>
              </el-col>
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-table
                    size="small"
                    :data="clusterForm.annotations"
                    style="width: 100%; margin: 0 auto; max-width: 500px"
                  >
                    <el-table-column prop="key" :label="$t('form.key')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.key"
                          size="small"
                          :placeholder="$t('form.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="value" :label="$t('form.value')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.value"
                          size="small"
                          :placeholder="$t('form.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.operation')">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          @click="noteshandleClose(scope.$index)"
                          >{{ $t("form.delete") }}</el-button
                        >
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="small"
                    style="margin: 10px auto; display: block"
                    @click="notesshowInput()"
                    >{{ $t("cluster.addNote") }}</el-button
                  >
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
        </el-collapse>
        <el-collapse v-model="activeNames">
          <el-collapse-item :title="$t('cluster.deploymentOptions')" name="1">
            <el-card class="box-card" shadow="never">
              <div class="text item">
                <el-form
                  ref="deploymentForm"
                  :inline="true"
                  :model="deploymentForm"
                >
                  <el-row type="flex">
                    <el-form-item :label="$t('form.region') + ':'" required>
                      <el-select
                        v-model="nowRegions"
                        :placeholder="$t('form.pleaseSelect')"
                        style="width: 230px"
                        filterable
                      >
                        <el-option
                          v-for="(item, index) in regionsData"
                          :key="index"
                          :label="item.name"
                          :value="item.id"
                        />
                      </el-select>
                    </el-form-item>
                  </el-row>
                </el-form>
              </div>
            </el-card>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-card>
    <!-- 集群选项 -->
    <el-card class="box-card mt-2 mb-2" shadow="never">
      <div class="btnMain" style="text-align: center">
        <el-button size="small" @click="handelCancelClick">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" size="small" @click="onSubmit">{{
          $t("form.submit")
        }}</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
var that = null;
import {
  getRegions,
  getUsers,
  postClusters,
  getClusters,
  getBps,
} from "@/api/mainApi";
import infoForm from "./components/infoForm";
import validate from "@/utils/validate";
export default {
  components: {
    infoForm,
  },
  data() {
    return {
      stepsActive: 1, // 步骤条
      clusterIsUse: false, // 是否为其他用户创建
      nowRegions: "", // 区域
      regionsData: [], // 区域
      BpsData: [], // 组织机构
      userData: [], // 用户
      regionData: [],
      bpId: "", // 组织机构ID
      userId: "", // 用户ID
      activeNames: ["1"], // 折叠面板
      clusterForm: {
        // 集群信息
        name: "",
        desc: "",
        target_nodes: [
          {
            dst_region_id: "", // 目标区域id 取nowRegions
          },
        ], // 部署的集群

        create_type: "import", // custom:用户自定义 import：导入
        cluster_type: "k8s", // k8s or k3s

        owner: "", // 集群拥有者 取clusterUser[1]
        bp: "", // 拥有集群的bp 取clusterUser[0]
        labels: [], // 标签与注释 标签内容
        annotations: [], // 标签与注释 注释内容
      },
      tabsAndNotes: [""], // 标签和注释手风琴
      tabsinputVisible: false, // 标签与注释 标签内容
      tabsinputValue: "", // 标签与注释 标签内容
      notesinputVisible: false, // 标签与注释 注释内容
      notesinputValue: "", // 标签与注释 注释内容

      nodeCollapse: [1], // 节点手风琴

      deploymentForm: {},
      rules: {
        name: [
          {
            required: true,
            validator: validate.k8sName,
            trigger: "blur",
          },
          {
            required: true,
            validator: validate.k8sName,
            trigger: "change",
          },
        ],
      },
    };
  },
  watch: {},
  mounted() {
    this.bpsinit();
    this.userinit();
    this.toRegions();
  },
  created() {
    that = this;
  },
  methods: {
    async toRegions() {
      const list = await getRegions();
      this.regionsData = list.regions;
      this.nowRegions = list.regions[0].id;
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
    // 取消增加操作调用
    handelCancelClick() {
      this.$router.push({
        path: "/serverCluster/list",
      });
    },
    tabshandleClose(index) {
      // 删除标签
      this.clusterForm.labels.splice(index, 1);
    },

    tabsshowInput() {
      // 新增标签
      this.$nextTick((_) => {
        this.clusterForm.labels.push({
          key: "",
          value: "",
        });
      });
    },

    noteshandleClose(index) {
      // 删除注释
      this.clusterForm.annotations.splice(index, 1);
    },

    notesshowInput() {
      // 新增注释
      this.$nextTick((_) => {
        this.clusterForm.annotations.push({
          key: "",
          value: "",
        });
      });
    },

    onSubmit() {
      this.$refs["importclusterForm"].validate((valid) => {
        if (valid) {
          var queryData = {
            name: this.clusterForm.name, // 集群名
            page_size: 99,
            page_num: 1,
          };
          getClusters(queryData).then((res) => {
            if (res.total_num > 0) {
              var nameList = res.clusters.filter((item) => {
                return item.name == this.clusterForm.name;
              });
              if (nameList.length > 0) {
                this.$notify({
                  title: this.$t("cluster.clusterNameExists"),
                  type: "warning",
                  duration: 2500,
                });
                return;
              }
            }
          });
        } else {
          this.$notify({
            title: this.$t("form.pleaseCompleteRequiredFields"),
            type: "warning",
            duration: 2500,
          });
          return false;
        }
      });
      if (this.nowRegions == "") {
        this.$notify({
          title: this.$t("cluster.pleaseSelectDeploymentRegion"),
          type: "warning",
          duration: 2500,
        });
        return;
      }

      // 提交信息
      var data = JSON.parse(JSON.stringify(this.clusterForm));

      data.owner = this.userId; // 集群拥有者
      data.bp = this.bpId; // 拥有集群的bp

      data.create_type = "import"; // custom:用户自定义 import：导入
      data.cluster_type = "k8s"; // k8s or k3s
      data.target_nodes[0].dst_region_id = this.nowRegions;
      data.annotations = {};
      data.labels = {};

      if (
        this.clusterForm.annotations.length > 0 &&
        this.clusterForm.annotations[0].key != "" &&
        this.clusterForm.annotations[0].value != ""
      ) {
        this.clusterForm.annotations.forEach((item) => {
          if (item.key != "" && item.value != "") {
            data.annotations[item.key] = item.value;
          }
        });
      }
      if (
        this.clusterForm.labels.length > 0 &&
        this.clusterForm.labels[0].key != "" &&
        this.clusterForm.labels[0].value != ""
      ) {
        this.clusterForm.labels.forEach((item) => {
          if (item.key != "" && item.value != "") {
            data.labels[item.key] = item.value;
          }
        });
      }

      postClusters(
        this.removePropertyOfNull(
          this.removePropertyOfNull(JSON.parse(JSON.stringify(data)))
        )
      ).then((res) => {
        if (res.id) {
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.$router.push({
            path:
              "/serverCluster/detail/" + res.id + "/" + this.clusterForm.name,
          });
        }
      });
    },
    removePropertyOfNull(data) {
      if (typeof data === "object" && data.constructor == Array) {
        data.forEach((item, index, arr) => {
          if (
            item === null ||
            (typeof item === "object" &&
              item.constructor == Object &&
              Object.keys(item).length < 1) ||
            (typeof item === "object" &&
              item.constructor == Array &&
              item.length < 1) ||
            item === ""
          ) {
            delete arr[index];
          } else {
            this.removePropertyOfNull(arr[index]);
          }
        });
      }
      if (typeof data === "object" && data.constructor == Object) {
        for (const key in data) {
          if (
            data[key] === null ||
            (typeof data[key] === "object" &&
              data[key].constructor == Object &&
              Object.keys(data[key]).length < 1) ||
            (typeof data[key] === "object" &&
              data[key].constructor == Array &&
              data[key].length < 1) ||
            data[key] === ""
          ) {
            delete data[key];
          } else {
            this.removePropertyOfNull(data[key]);
          }
        }
      }
      return data;
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
  padding-left: 10px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  width: 100%;
}

.nodeCollapse {
  .nodeDel {
    position: absolute;
    top: 10px;
    right: 10px;
  }

  ::v-deep .el-collapse-item__header {
    position: relative;
    padding-left: 20px;

    display: inline-block;

    .el-collapse-item__arrow {
      position: absolute;
      top: 36%;
      left: 4px;
    }
  }
}

::v-deep .table_head {
  font-weight: 600 !important;
}

.box-card {
  border: none;

  ::v-deep .el-card__header {
    padding: 10px 20px;
    font-weight: 600;
    border-bottom: 0px solid #ebeef5;
  }
}

::v-deep .el-collapse {
  border-bottom: none;

  ::v-deep .el-collapse-item__wrap {
    border-bottom: none;
  }

  &.collapseStyle {
    ::v-deep .el-collapse-item__arrow {
      margin: 0 8px 0 0;
    }
  }
}

.formItem {
  padding: 5px 20px;

  .formItemTitle {
    font-weight: bold;
    font-size: 16px;
    // padding-bottom: 10px;
  }

  .formSmallItem {
    padding: 10px 20px;
    width: 50%;
    float: left;
    height: 180px;
    min-width: 400px;
    max-width: 600px;

    .formSmallItemTitle {
      // font-weight: bold;
      font-size: 14px;
      padding-bottom: 10px;
    }
  }
}
</style>
