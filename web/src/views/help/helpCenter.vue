<template>
  <div class="helpPage warpMain">
    <div class="leftDiv">
      <div class="breadcrumbMain">
        <div class="directoryTitle">
          <span class="title" :class="((kind === 0 || kind === 1) ? 'isAdmin' : '')">{{ treeFirstData.name }}</span>
          <el-dropdown v-if="kind === 0 || kind === 1" class="dropdownBtn float-right mt-1">
            <span class="el-dropdown-link ">
              <img @click.stop src="@/assets/transfer/add.png" alt="" class="w-4" />
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>
                  <span @click.stop="addDirectory(treeFirstData.id)">新增目录</span>
                </el-dropdown-item>
                <el-dropdown-item>
                  <span @click.stop="addDocument(treeFirstData.id)">新增文档</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <!-- <span class="setBtn" v-if="kind === 0 || kind === 1">
            <img
              src="@/assets/transfer/shuaxin.png"
              alt=""
              class="inline-block"
              @click.stop="init"
            />
            <img
              src="@/assets/transfer/add.png"
              alt=""
              class="inline-block"
              @click.stop="addDirectory('')"
            />
          </span> -->
        </div>
        <div class="treeMain" :class="((kind === 0 || kind === 1) ? 'nodeIsAdmin' : '')">
          <el-tree :data="directoryData" :props="directoryProps" node-key="id" default-expand-all ref="directoryRef">
            <template #default="{ node, data }">
              <span class="custom-tree-node inline-block flex-1"
                :class="(data.isDocument && data.id == documentDetail.id ? 'active' : '') + (' ') + (node.level > 1 ? 'nodeLineStyle' : '')">

                <!-- <img class="svgIcon inline-block" src="@/assets/svg/word.svg" alt="" v-if="data.isDocument" /> -->
                <!-- <img class="svgIcon inline-block" src="@/assets/svg/file.svg" alt="" v-else /> -->
                <span class="title" :class="data.disabled === 0 ? '' : 'stopStatus'" v-if="data.isDocument"
                  @click="getDocumentDetail(data.id)">{{ data.name }}</span>
                <span class="title" :class="data.disabled === 0 ? '' : 'stopStatus'" v-else>{{ data.name }}</span>
                <span class="setBtn" v-if="isDocumentDetail && (kind === 0 || kind === 1)">
                  <el-dropdown v-if="data.isDocument">
                    <span class="el-dropdown-link">
                      <img @click.stop src="@/assets/transfer/more.png" alt="" />
                    </span>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item v-if="data.disabled === 1">
                          <span @click.stop="startDocument(data)">启用</span>
                        </el-dropdown-item>
                        <el-dropdown-item v-if="data.disabled === 0">
                          <span @click.stop="stopDocument(data)">停用</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="editDocument(data)">编辑</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="delDocument(data.id)">删除</span>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                  <el-dropdown v-if="!data.isDocument && data.level != 1">
                    <span class="el-dropdown-link">
                      <img @click.stop src="@/assets/transfer/more.png" alt="" />
                    </span>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item v-if="data.disabled === 1">
                          <span @click.stop="startDirectory(data)">启用</span>
                        </el-dropdown-item>
                        <el-dropdown-item v-if="data.disabled === 0">
                          <span @click.stop="stopDirectory(data)">停用</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="editDirectory(data)">编辑</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="delDirectory(data)">删除</span>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                  <el-dropdown v-if="!data.isDocument">
                    <span class="el-dropdown-link">
                      <img @click.stop src="@/assets/transfer/add.png" alt="" />
                    </span>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item v-if="data.level != 3">
                          <span @click.stop="addDirectory(data.id)">新增目录</span>
                        </el-dropdown-item>
                        <el-dropdown-item>
                          <span @click.stop="addDocument(data.id)">新增文档</span>
                        </el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </span>
              </span>
            </template>
          </el-tree>
        </div>
      </div>
    </div>
    <div class="rightDiv">
      <help-detail v-if="isDocumentDetail" :documentDetail="documentDetail"></help-detail>
      <help-add :parentId="documentParentId" :isDocumentAdd="isDocumentAdd" :documentDetail="documentDetail"
        @addDocumentSuccess="addDocumentSuccess" @addDocumentDialogCancel="addDocumentDialogCancel" v-else></help-add>
    </div>
    <el-dialog :visible.sync="directoryVisible" :title="isAddDirectory ? '新增目录' : '编辑目录'" width="500px"
      :before-close="handleCloseDirectory" :close-on-click-modal="false">
      <div>
        <el-form :model="directoryForm" size="mini" label-width="100px" style="width: 100%" ref="directoryFormRef"
          :rules="rules">
          <el-form-item label="目录名称" prop="name">
            <el-input v-model="directoryForm.name" placeholder="请输入目录名称" clearable style="width: 232px" />
          </el-form-item>
          <el-form-item label="描述" prop="description">
            <el-input maxlength="255" show-word-limit type="textarea" v-model="directoryForm.description"
              placeholder="请输入描述" style="width: 232px" />
          </el-form-item>
          <!-- <el-form-item label="ID" prop="label">
            <el-input
              v-model="directoryForm.label"
              placeholder="请输入ID"
              clearable
              style="width: 232px"
            />
          </el-form-item> -->
          <el-form-item label="排序" prop="oder">
            <el-input-number v-model="directoryForm.oder" :min="1" />
          </el-form-item>

          <!-- <el-col :span="12">
              <el-form-item label="状态" prop="disabled">
                <el-radio-group v-model="directoryForm.disabled" size="mini">
                  <el-radio-button :label="0" :value="0">启用</el-radio-button>
                  <el-radio-button :label="1" :value="1">停用</el-radio-button>
                </el-radio-group>
              </el-form-item>
            </el-col> -->
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer" style="text-align: right">
          <el-button @click="handleCloseDirectory()" size="mini">取 消</el-button>
          <el-button type="primary" :loading="loading" size="mini" @click="submitDirectoryForm()">
            确 认
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import helpDetail from "./components/helpDetail.vue";
import helpAdd from "./components/helpAdd.vue";
import {
  documentQuery,
  documentDetail,
  documentUpdate,
  directoryUpdate,
  directoryDel,
  directoryAdd,
  directoryQuery,
  transmissionLogin,
  documentDel,
} from "@/api/transfer/mainApi";
import Cookies from "js-cookie";

export default {
  name: "helpCenter",
  components: {
    helpDetail,
    helpAdd,
  },
  data () {
    return {
      loading: false,
      isAddDirectory: true, //是否新增目录
      isDocumentDetail: true, //是否文档详情
      isDocumentAdd: true, //是否新增文档
      directoryVisible: false, //目录弹窗
      editDirectoryId: "",
      documentParentId: "", //文档父级id
      documentDetail: {}, //文档详情
      treeFirstData: "", //树标题
      directoryForm: {
        parentId: "",
        name: "",
        description: "",
        oder: 1, //排序
        disabled: 1, //0启用 1停用
      },
      rules: {
        // parentId: [{ required: true, message: "请选择上级目录", trigger: "blur" }],
        name: [{ required: true, message: "请输入目录名称", trigger: "blur" }],
        oder: [{ required: true, message: "请输入排序", trigger: "blur" }],
        disabled: [{ required: true, message: "请选择状态", trigger: "blur" }],
      },
      directoryData: [], //
      documentData: [], //
      directoryProps: {
        value: "id",
        label: "name",
        children: "children",
        checkStrictly: true,
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo", "kind"]),
  },
  created () {
    this.isLogin();
  },
  methods: {
    isLogin () {
      transmissionLogin(Cookies.get("Access-Token")).then((rs) => {
        this.init();
      });
    },
    addDocument (id) {
      //新增文档
      this.isDocumentAdd = true;
      this.documentParentId = id;
      this.isDocumentDetail = false;
    },
    addDocumentDialogCancel () {
      //新增文档取消
      this.documentParentId = "";
      this.isDocumentDetail = true;
    },
    addDocumentSuccess (id) {
      //新增文档成功
      this.documentParentId = "";
      this.isDocumentDetail = true;
      this.init(id);
    },
    editDocument (data) {
      //编辑文档
      this.documentParentId = data.parentId;
      documentDetail(data.id)
        .then((res) => {
          this.documentDetail = res.obj;
          this.isDocumentAdd = false;
          this.isDocumentDetail = false;
        })
        .catch((error) => { });
    },
    getDocumentDetail (id) {
      //获取文档详情
      if (!this.isDocumentDetail) return;
      documentDetail(id)
        .then((res) => {
          this.documentDetail = res.obj;
          this.isDocumentDetail = true;
        })
        .catch((error) => { });
    },

    startDocument (data) {
      //启用文档
      documentUpdate({ id: data.id, disabled: 0 })
        .then((res) => {
          this.$notify({
            title: "操作成功",
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((error) => { });
    },
    stopDocument (data) {
      //停用文档
      documentUpdate({ id: data.id, disabled: 1 })
        .then((res) => {
          this.$notify({
            title: "操作成功",
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((error) => { });
    },
    delDocument (id) {
      this.$confirm("是否删除该文档", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          documentDel({ id })
            .then((res) => {
              this.$notify({
                title: "删除成功",
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((error) => { });
        })
        .catch(() => { });
    },

    addDirectory (id) {
      //新增目录
      this.directoryForm.parentId = id;
      this.isAddDirectory = true;
      this.directoryVisible = true;
    },
    editDirectory (data) {
      //编辑目录
      this.isAddDirectory = false;
      this.directoryVisible = true;

      this.editDirectoryId = data.id;
      this.directoryForm.parentId = data.parentId;
      this.directoryForm.name = data.name;
      this.directoryForm.oder = data.oder;
      // this.directoryForm.disabled = data.disabled;
    },
    startDirectory (data) {
      //启用目录
      directoryUpdate({ id: data.id, disabled: 0 })
        .then((res) => {
          this.$notify({
            title: "操作成功",
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((error) => { });
    },
    stopDirectory (data) {
      //停用目录
      directoryUpdate({ id: data.id, disabled: 1 })
        .then((res) => {
          this.$notify({
            title: "操作成功",
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((error) => { });
    },
    //删除目录
    delDirectory (data) {
      if (data.children && data.children.length) {
        this.$confirm("该目录下存在其他目录或文档，请先删除后操作", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => { })
          .catch(() => { });
      } else {
        this.$confirm("是否删除该目录", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            directoryDel({ id: data.id })
              .then((res) => {
                this.$notify({
                  title: "删除成功",
                  type: "success",
                  duration: 2500,
                });
                this.init();
              })
              .catch((error) => { });
          })
          .catch(() => { });
      }
    },
    handleCloseDirectory () {
      //关闭新增目录
      this.resetForm();
      this.directoryVisible = false;
    },
    resetForm () {
      //重置表单
      this.directoryUpdateId = "";
      this.directoryForm.parentId = "";
      this.directoryForm.disabled = 1;
      this.$refs.directoryFormRef.resetFields();
    },
    submitDirectoryForm () {
      //提交目录表单
      this.$refs.directoryFormRef.validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAddDirectory) {
            //新增
            directoryAdd(this.directoryForm)
              .then((res) => {
                this.$notify({
                  title: "添加成功",
                  type: "success",
                  duration: 2500,
                });
                this.init();
                this.handleCloseDirectory();
                this.loading = false;
              })
              .catch((error) => { });
          } else {
            directoryUpdate({ id: this.editDirectoryId, ...this.directoryForm })
              .then((res) => {
                this.$notify({
                  title: "编辑成功",
                  type: "success",
                  duration: 2500,
                });
                this.init();
                this.handleCloseDirectory();
                this.loading = false;
              })
              .catch((error) => { });
          }
        }
      });
    },
    init (documentId) {

      directoryQuery({
        label: this.$route.params.id,
      })
        .then((res) => {
          this.directoryData = res.obj || [];
          documentQuery({
            id: this.directoryData[0].id,
          })
            .then((res) => {
              var data = {};
              res.obj.map((item) => {
                if (data[item.parentId]) {
                  data[item.parentId].push({
                    id: item.id,
                    name: item.title,
                    parentId: item.parentId,
                    disabled: item.disabled,
                    isDocument: true,
                  });
                } else {
                  data[item.parentId] = [
                    {
                      id: item.id,
                      name: item.title,
                      parentId: item.parentId,
                      disabled: item.disabled,
                      isDocument: true,
                    },
                  ];
                }
              });
              var newdata = JSON.parse(JSON.stringify(this.directoryData));
              this.directoryTreeMap(newdata, data);
              // this.directoryData = newdata;
              this.directoryData = newdata[0].children;
              this.treeFirstData = newdata[0];
              if (res.obj && res.obj.length > 0) {
                var id = documentId || res.obj[0].id;
                this.getDocumentDetail(id);
              }

            })
            .catch((error) => {
              console.error(error);
            });
        })
        .catch((error) => {
          console.error(error);
        });
    },

    directoryTreeMap (data, documentData) {
      data.forEach((item, index, arr) => {
        if (item.children) {
          this.directoryTreeMap(item.children, documentData);
        }
        if (documentData[item.id]) {
          if (arr[index].children) {
            arr[index].children.push(...documentData[item.id]);
          } else {
            arr[index].children = documentData[item.id];
          }
        }
      });
    },
  },
};
</script>

<style lang="scss" scpoed>
.helpPage {
  background-color: #fff;
  overflow: hidden;
  border-top: 4px solid #f3f4f6;

  .leftDiv {
    float: left;
    width: 240px;
    height: 100%;
    height: calc(100vh - 165px);
    border-right: 1px solid #ebeef5;

    .breadcrumbMain {
      height: 100%;
      padding: 0px 10px 0px 0px;

      .directoryTitle {
        margin-bottom: 20px;

        &:hover {
          .dropdownBtn {
            display: inline-block;
          }
        }

        .dropdownBtn {
          display: none;
        }

        .title {
          font-size: 15px;
          font-weight: bold;
          color: #333;
          display: inline-block;
          line-height: 24px;

          &.isAdmin {
            width: calc(100% - 20px);
          }
        }

        .setBtn {
          float: right;
          font-size: 14px;
          padding: 3px 5px;
          line-height: 20px;

          img {
            padding: 2px;
            width: 16px;
            height: 16px;
            cursor: pointer;

            &:hover {
              background-color: #e5e7ea;

            }
          }
        }
      }

      .el-breadcrumb {
        padding: 0;
        margin: 0;

        .el-breadcrumb__inner {
          padding: 0;
          margin: 0;
          font-size: 16px;
          cursor: pointer;

          a {
            padding: 0;
            margin: 0;
          }
        }

        .el-breadcrumb__separator {
          margin: 0 4px;
        }
      }
    }

    .treeMain {
      width: 100%;
      max-height: calc(100% - 68px);
      overflow: auto;

      // 隐藏滚动条
      &::-webkit-scrollbar {
        display: none;
      }

      // 兼容火狐
      scrollbar-width: none;
    }

    .nodeIsAdmin {
      .el-tree-node__content {
        &:hover {
          .el-tree-node__expand-icon {
            display: none;
          }
        }

      }
    }

    .el-tree {
      margin-bottom: 20px;

      .el-tree-node {
        &:focus {
          background-color: transparent;
        }

        .el-tree-node__content {
          display: flex;
          position: relative;
          background-color: transparent;

          &:hover {
            background-color: transparent;

          }

          &:focus {
            background-color: transparent;
          }

          .el-tree-node__expand-icon {
            position: absolute;
            top: 0;
            right: 0;
            z-index: 9;
          }

          .el-icon {
            vertical-align: top;
            float: left;
          }
        }


      }


    }

    .custom-tree-node {
      position: relative;
      overflow: hidden;
      display: block;
      height: 32px;
      line-height: 32px;
      vertical-align: text-top;
      padding-left: 10px;

      &.nodeLineStyle {
        &::before {
          position: absolute;
          top: 0;
          left: 0px;
          content: "";
          display: inline-block;
          width: 1px;
          height: 32px;
          background-color: #f5f7fa;
        }

      }


      &.active {
        background-color: transparent;
        color: rgb(64, 158, 255);

        &.nodeLineStyle {
          &::after {
            position: absolute;
            top: 2px;
            left: 0px;
            z-index: 10;
            content: "";
            display: inline-block;
            width: 3px;
            height: 28px;
            border-radius: 4px;
            background-color: rgb(64, 158, 255);
          }
        }
      }

      &:hover {
        // background-color: #f5f7fa;
        background-color: transparent;
        color: rgb(64, 158, 255);

        &.nodeLineStyle {
          &::after {
            position: absolute;
            top: 2px;
            left: 0px;
            z-index: 10;
            content: "";
            display: inline-block;
            width: 3px;
            height: 28px;
            border-radius: 4px;
            background-color: rgb(64, 158, 255);
          }
        }

        .title {
          width: calc(100% - 50px);
        }

        .setBtn {
          display: inline-block;
          top: 5px;
          height: 26px;
          padding: 3px 5px;
        }
      }

      .setBtn {
        position: absolute;
        top: 0px;
        right: 0px;
        // display: none;
        height: 0px;
        font-size: 14px;
        padding: 0px 5px;
        line-height: 20px;
        overflow: hidden;

        // background-color: #f5f7fa;
        img {
          padding: 2px;
          width: 16px;
          height: 16px;
          cursor: pointer;

          &:hover {
            background-color: #e5e7ea;
          }
        }
      }

      .title {
        font-size: 15px;
        display: inline-block;
        line-height: 32px;
        width: 100%;
        // 超出隐藏省略号
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        &.stopStatus {
          color: #f00;
        }
      }

      .svgIcon {
        width: 12px;
        margin-top: 5px;
        margin-right: 5px;
        vertical-align: top;
      }



    }
  }

  .rightDiv {
    position: relative;
    overflow: hidden;
    height: calc(100vh - 165px);
  }
}
</style>
