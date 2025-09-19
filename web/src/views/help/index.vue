<template>
  <div class="helpPage_index">
    <div class="helpBanner w-full inline-block">
      <div style="width: 1200px" class="mx-auto">
        <h2 class="font-bold text-5xl">91GPU-帮助文档</h2>
        <h2 class="font-bold text-4xl mt-2">构建智能计算基础设施</h2>
        <p class="text-2xl text-gray-500 mt-8">
          高效稳定的算力资源助力AI应用的开发、训练、部署
        </p>
        <div class="pt-12">
          <el-link
            href="http://lnjoying.com/"
            target="_blank"
            class="mr-4"
            :underline="false"
          >
            <el-button type="primary" size="medium" round>关于我们</el-button>
          </el-link>
          <router-link to="/" target="_blank">
            <el-button type="primary" size="medium" round>产品首页</el-button>
          </router-link>
        </div>
      </div>
    </div>
    <div :class="kind === 0 || kind === 1 ? '' : 'pt-10'">
      <div
        v-if="kind === 0 || kind === 1"
        style="width: 1200px; text-align: right"
        class="pt-4 mx-auto"
      >
        <el-button size="small" type="primary" @click.stop="addDirectory('')"
          >新增目录</el-button
        >
      </div>
      <div style="width: 1200px" class="mx-auto">
        <el-row class="pt-4 pb-20" :gutter="20">
          <el-col
            v-for="item in document_center_list"
            :key="item.id"
            class="mb-4"
            :span="6"
          >
            <div @click="push_more(item.label)">
              <el-card
                body-style="padding:25px 20px 10px;"
                style="
                  cursor: pointer;
                  border-radius: 8px;
                  background-color: rgba(255, 255, 255, 0.7);
                "
                shadow="hover"
              >
                <div class="titel_info">
                  <h3 v-if="item.disabled" class="text-base font-bold">
                    <div class="flex_title">
                      <div class="mr-2">
                        <el-tag size="mini" type="danger">停用</el-tag>
                      </div>
                      <div>{{ item.name }}</div>
                    </div>
                  </h3>
                  <h3 v-else class="text-lg font-bold">
                    <div class="flex_title">
                      <div>{{ item.name }}</div>
                    </div>
                  </h3>
                  <el-dropdown
                    v-if="kind === 0 || kind === 1"
                    size="small"
                    placement="bottom"
                  >
                    <span class="el-dropdown-link">
                      <i class="el-icon-setting" @click.stop />
                    </span>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item v-if="item.disabled === 1">
                          <span @click.stop="startDirectory(item)">启用</span>
                        </el-dropdown-item>
                        <el-dropdown-item v-if="item.disabled === 0">
                          <span @click.stop="stopDirectory(item)">停用</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="editDirectory(item)">编辑</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="delDirectory(item)">删除</span>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
                <div class="file_description mt-2">
                  {{ item.description ? item.description : "" }}
                </div>
                <div class="text-right text-gray-500 text-xl">
                  <i class="el-icon-right" />
                </div>
              </el-card>
            </div>
          </el-col>
        </el-row>
      </div>
      <el-dialog
        :visible.sync="directoryVisible"
        :title="isAddDirectory ? '新增目录' : '编辑目录'"
        width="500px"
        :before-close="handleCloseDirectory"
        :close-on-click-modal="false"
      >
        <div>
          <el-form
            ref="directoryFormRef"
            :model="directoryForm"
            size="mini"
            label-width="100px"
            style="width: 100%"
            :rules="rules"
          >
            <el-form-item label="目录名称" prop="name">
              <el-input
                v-model="directoryForm.name"
                placeholder="请输入目录名称"
                clearable
                style="width: 232px"
              />
            </el-form-item>
            <el-form-item label="所属模块" prop="label">
              <el-select
                v-model="directoryForm.label"
                placeholder="请选择所属模块"
                style="width: 232px"
              >
                <el-option
                  v-for="item in labelOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="描述" prop="description">
              <el-input
                v-model="directoryForm.description"
                maxlength="255"
                show-word-limit
                type="textarea"
                placeholder="请输入描述"
                style="width: 232px"
              />
            </el-form-item>

            <el-form-item label="排序" prop="oder">
              <el-input-number v-model="directoryForm.oder" :min="1" />
            </el-form-item>
          </el-form>
        </div>
        <template #footer>
          <span class="dialog-footer" style="text-align: right">
            <el-button size="mini" @click="handleCloseDirectory()"
              >取 消</el-button
            >
            <el-button
              :loading="loading"
              type="primary"
              size="mini"
              @click="submitDirectoryForm()"
            >
              确 认
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  directoryQuery,
  transmissionLogin,
  directoryAdd,
  directoryDel,
  directoryUpdate,
  documentQuery,
} from "@/api/transfer/mainApi";
import Cookies from "js-cookie";

export default {
  name: "HelpHome",
  components: {},
  data() {
    return {
      loading: false,
      copylabel: "",
      document_center_list: [],
      isAddDirectory: true,
      editDirectoryId: "",
      directoryVisible: false,
      directoryForm: {
        parentId: "",
        name: "",
        label: "",
        description: "",
        oder: 1, // 排序
        disabled: 1, // 0启用 1停用
      },
      rules: {
        name: [{ required: true, message: "请输入目录名称", trigger: "blur" }],
        oder: [{ required: true, message: "请输入排序", trigger: "blur" }],
        disabled: [{ required: true, message: "请选择状态", trigger: "blur" }],
        label: [
          { required: true, message: "请选择所属模块", trigger: "change" },
        ],
      },
      labelOptions: [
        {
          value: "nextStack",
          label: "nextStack",
        },
        {
          value: "easyStack",
          label: "easyStack",
        },
        {
          value: "containerService",
          label: "容器服务",
        },
        {
          value: "clusterService",
          label: "集群服务",
        },
        {
          value: "imagesService",
          label: "镜像服务",
        },
        {
          value: "middleware",
          label: "中间件",
        },
        {
          value: "transferService",
          label: "文件传输",
        },
        {
          value: "DevOps",
          label: "运维管理",
        },
        {
          value: "billingCenter",
          label: "计费中心",
        },
        {
          value: "iam",
          label: "IAM",
        },
        {
          value: "platformManagement",
          label: "平台管理",
        },
        {
          value: "operationCenter",
          label: "运营中心",
        },
      ],
    };
  },
  computed: {
    ...mapGetters(["userInfo", "kind"]),
  },
  mounted() {
    this.isLogin();
  },
  created() {},
  methods: {
    addDirectory() {
      // 新增目录
      this.isAddDirectory = true;
      this.directoryVisible = true;
    },
    handleCloseDirectory() {
      // 关闭新增目录
      this.resetForm();
      this.directoryVisible = false;
    },
    resetForm() {
      // 重置表单
      this.directoryForm = {
        parentId: "",
        name: "",
        label: "",
        description: "",
        oder: 1, // 排序
        disabled: 1, // 0启用 1停用
      };
      this.$refs.directoryFormRef.resetFields();
    },
    submitDirectoryForm() {
      // 提交目录表单
      this.$refs.directoryFormRef.validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAddDirectory) {
            // 新增
            directoryAdd(this.directoryForm)
              .then((res) => {
                if (res.status == 3303) {
                  this.$notify({
                    title: "已有该模块,不可添加",
                    type: "error",
                    duration: 2500,
                  });
                  this.loading = false;
                } else {
                  this.$notify({
                    title: "添加成功",
                    type: "success",
                    duration: 2500,
                  });
                  this.getdirectoryQuery();
                  this.handleCloseDirectory();
                  this.loading = false;
                }
              })
              .catch((error) => {});
          } else {
            if (this.directoryForm.label == this.copylabel) {
              delete this.directoryForm.label;
            }
            directoryUpdate({ id: this.editDirectoryId, ...this.directoryForm })
              .then((res) => {
                if (res.status == 3303) {
                  this.$notify({
                    title: "已有该模块,不可编辑",
                    type: "error",
                    duration: 2500,
                  });
                  this.loading = false;
                } else {
                  this.$notify({
                    title: "编辑成功",
                    type: "success",
                    duration: 2500,
                  });
                  this.getdirectoryQuery();
                  this.handleCloseDirectory();
                  this.loading = false;
                }
              })
              .catch((error) => {});
          }
        }
      });
    },
    deleteDirectory(id) {
      this.$confirm("是否删除该目录", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          directoryDel({ id })
            .then((res) => {
              this.$notify({
                title: "删除成功",
                type: "success",
                duration: 2500,
              });
              this.getdirectoryQuery();
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    deleteDirectorytips() {
      this.$confirm("该目录下存在其他目录或文档，请先删除后操作", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {})
        .catch(() => {});
    },
    // 删除目录
    delDirectory(data) {
      directoryQuery({
        label: data.label,
      })
        .then((res) => {
          if (res.obj[0].children && res.obj[0].children.length) {
            this.deleteDirectorytips();
          } else {
            documentQuery({
              id: res.obj[0].id,
            })
              .then((res) => {
                if (res.obj && res.obj.length) {
                  this.deleteDirectorytips();
                } else {
                  {
                    this.deleteDirectory(data.id);
                  }
                }
              })
              .catch((error) => {
                console.error(error);
              });
          }
        })
        .catch((error) => {
          console.error(error);
        });
    },
    // 编辑文档
    editDirectory(data) {
      // 编辑目录
      this.isAddDirectory = false;
      this.directoryVisible = true;
      this.editDirectoryId = data.id;
      this.directoryForm.name = data.name;
      this.directoryForm.description = data.description;
      this.directoryForm.label = data.label;
      this.copylabel = data.label;
      this.directoryForm.oder = data.oder;
    },
    startDirectory(data) {
      // 启用目录
      directoryUpdate({ id: data.id, disabled: 0 })
        .then((res) => {
          this.$notify({
            title: "操作成功",
            type: "success",
            duration: 2500,
          });
          this.getdirectoryQuery();
        })
        .catch((error) => {});
    },
    stopDirectory(data) {
      // 停用目录
      directoryUpdate({ id: data.id, disabled: 1 })
        .then((res) => {
          this.$notify({
            title: "操作成功",
            type: "success",
            duration: 2500,
          });
          this.getdirectoryQuery();
        })
        .catch((error) => {});
    },
    getdirectoryQuery() {
      directoryQuery({
        queryLevel: 1,
      }).then((res) => {
        this.document_center_list = res.obj;
      });
    },
    isLogin() {
      transmissionLogin(Cookies.get("Access-Token")).then((rs) => {
        this.getdirectoryQuery();
      });
    },
    push_more(id) {
      this.$router.push(`/help/helpCenter/${id}`);
    },
  },
};
</script>

<style lang="scss" scpoed>
.helpPage_index {
  height: calc(100vh - 50px);
  background-color: #f4f7fb;
  margin: -15px -10px;
  overflow-y: auto;

  .helpBanner {
    height: 380px;
    padding-top: 100px;
  }
}

.titel_info {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.flex_title {
  display: flex;
  align-items: center;
}

.file_description {
  height: 120px;
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  font-size: 14px;
  line-height: 20px;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
}
</style>
