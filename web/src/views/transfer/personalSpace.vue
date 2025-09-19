<template>
  <div class="warpMain">
    <el-row :gutter="12">
      <el-col :span="16">
        <el-form
          :inline="true"
          size="small"
          :model="queryData"
          class="demo-form-inline"
        >
          <el-row :gutter="12">
            <el-col :span="24">
              <el-form-item>
                <div slot="label">{{ $t("transfer.fileName") }}:</div>
                <el-input
                  v-model="queryData.name"
                  :placeholder="$t('transfer.searchByFileName')"
                />
              </el-form-item>
              <el-form-item>
                <el-button
                  size="small"
                  icon="el-icon-search"
                  class="add_search"
                  type="primary"
                  @click="Search"
                  >{{ $t("form.query") }}</el-button
                >
              </el-form-item>
              <el-form-item>
                <el-button size="small" class="add_search" @click="ResetData">{{
                  $t("form.reset")
                }}</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </el-col>
      <el-col :span="8">
        <div class="help_document">
          <p class="mr-2">{{ $t("transfer.helpDocument") }}:</p>
          <p class="mr-2 text cursor-pointer" @click="pushhelp">
            {{ $t("transfer.onlinePreview") }}
          </p>
          <el-divider direction="vertical" />

          <a
            class="ml-2 text"
            href="./EdgeTurbo客户端（linux版）使用指南.pdf"
            download="EdgeTurbo客户端（linux版）使用指南.pdf"
          >
            {{ $t("transfer.documentDownload") }}
          </a>
        </div>
        <!-- <el-tooltip class="box-item" effect="dark" placement="left-start">
          <template #content>
            <div style="font-size: 14px">
              <p>节点--个人中心网盘数据传输操作指南，</p>
              <p style="margin-top: 5px">
                请远程登录节点使用Linux 端进行操作。
              </p>
            </div>
          </template>

<div style="text-align: center; width: 50px; float: right">
  <a href="./EdgeTurbo客户端（linux版）使用指南.pdf" download="EdgeTurbo客户端（linux版）使用指南.pdf">
    <svg t="1669291987864" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
      p-id="14924" width="50" height="32">
      <path
        d="M800 340.8l-144-144H224V832h576V340.8zM256 800V228.8h376v136H768V800H256z m392-566.4l115.2 115.2h-115.2v-115.2z"
        fill="#409EFF" p-id="14925"></path>
      <path
        d="M611.2 619.2l-6.4 6.4 44.8 44.8H368l44.8-44.8-6.4-6.4-4.8-4.8-57.6 57.6 4.8 14.4h320l6.4-14.4-57.6-57.6zM627.2 492.8l-11.2-11.2-96 96V356.8h-16v220.8l-96-96-11.2 11.2L512 608z"
        fill="#409EFF" p-id="14926"></path>
    </svg>
  </a>
  <span style="
                font-size: 10px;
                text-align: center;
                display: block;
                margin-top: -3px;
                color: #409eff;
              ">操作指南</span>
</div>
</el-tooltip> -->
      </el-col>
    </el-row>

    <div class="listTop">
      <div class="breadcrumbMain">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item
            v-for="(item, index) in getfilepathData(getfilepath)"
            :key="index"
          >
            <span
              class="cursor-pointer transferFileStyle"
              @click="rootdirectory(index)"
              >{{ item == "" ? $t("transfer.rootDirectory") : item }}</span
            >
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <!-- <div class="fl icon">
        <span>
          <i class="iconfont icon-wenjianliebiao1"
             @click="listShowType = 'list'"
             :style="listShowType == 'list' ? 'color:#096DD9' : ''"></i>
        </span>
        <span style="margin: 0px 16px">
          <i class="iconfont icon-wenjianliebiao"
             @click="listShowType = 'box'"
             :style="listShowType == 'box' ? 'color:#096DD9' : ''"></i>
        </span>
      </div> -->
      <div class="buttonMain">
        <el-button
          type="primary"
          size="mini"
          :disabled="!(multipleSelection.length < 1)"
          @click="openDialog(0)"
        >
          {{ $t("transfer.upload") }}</el-button
        >
        <el-button
          type="primary"
          size="mini"
          :disabled="multipleSelection.length < 1"
          @click="openDialog(1)"
          >{{ $t("transfer.downloads") }}</el-button
        >
        <el-button
          type="primary"
          size="mini"
          :disabled="multipleSelection.length < 1"
          @click="delFile"
          >{{ $t("transfer.delete") }}</el-button
        >
        <el-button
          type="primary"
          size="mini"
          :disabled="!(multipleSelection.length == 1)"
          @click="rename"
          >{{ $t("transfer.rename") }}</el-button
        >
        <el-button type="primary" size="mini" @click="addFile">{{
          $t("transfer.createDirectory")
        }}</el-button>
      </div>
    </div>
    <!-- 文件列表开始 -->
    <div class="fileList">
      <!-- 列表模式 -->
      <el-table
        v-show="listShowType == 'list'"
        ref="multipleTableRef"
        v-loading="loading"
        :element-loading-text="$t('domain.loading')"
        :data="showfilelist"
        stripe
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
        @selection-change="handleSelectionChange"
        @row-dblclick="dblclickFile(row, $event)"
        @row-click="clickFile(row, $event)"
      >
        <el-table-column type="selection" width="40" />
        <el-table-column
          type="index"
          width="60"
          align="center"
          :lable="$t('transfer.serialNo')"
        >
          <template slot="header">
            <span>{{ $t("transfer.serialNo") }}</span>
          </template>
          <template slot-scope="scope">
            <span>{{ scope.$index + 1 }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('transfer.fileName')" prop="name" sortable>
          <template slot-scope="scope">
            <div
              v-if="
                scope.row.mode &&
                (scope.row.mode == 'add' || scope.row.mode == 'edit')
              "
              class="addFileSet"
            >
              <el-input v-model="scope.row.name" size="mini">
                <template slot="append">
                  <i
                    class="iconfont icon-duigou iconAdd"
                    @click.stop="toAddFile(scope.row)"
                  />

                  <i
                    class="iconfont icon-chacha iconClose"
                    @click.stop="closeAddFile(scope.row)"
                  />
                </template>
              </el-input>
            </div>
            <el-tooltip
              v-else
              class="box-item"
              effect="dark"
              :content="scope.row.name"
              placement="top-start"
              :open-delay="1000"
            >
              <div class="overflow-ellipsis">
                <file-icon
                  class="align-text-bottom"
                  style="width: 16px"
                  :file-name="JSON.parse(JSON.stringify(scope.row.name))"
                  :file-type="scope.row.type"
                />
                <span
                  :class="
                    scope.row.type == 1
                      ? 'cursor-pointer transferFileStyle'
                      : ''
                  "
                  style="margin-left: 4px"
                  @click="dblclickFile(scope.row, scope.row)"
                  >{{ scope.row.name }}</span
                >
              </div>
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column
          property="size"
          :label="$t('transfer.size')"
          prop="size"
          sortable
        >
          <template slot-scope="scope">
            <span v-if="scope.row.type == 1"> - </span>
            <span v-else>
              {{ scope.row.size == null ? "-" : getfilesize(scope.row.size) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('transfer.filePath')"
          prop="filePath"
          sortable
        >
          <template slot-scope="scope">
            <div class="overflow-ellipsis">
              <el-tooltip
                class="box-item"
                effect="dark"
                :content="scope.row.filePath"
                placement="top-start"
                :open-delay="1000"
              >
                <div class="overflow-ellipsis">
                  <span class="svg" @click.stop="copy(scope.row.filePath)">
                    <svg
                      t="1668653791310"
                      class="icon"
                      viewBox="0 0 1024 1024"
                      version="1.1"
                      xmlns="http://www.w3.org/2000/svg"
                      p-id="1444"
                      width="16"
                      height="16"
                    >
                      <path
                        d="M720 192h-544A80.096 80.096 0 0 0 96 272v608C96 924.128 131.904 960 176 960h544c44.128 0 80-35.872 80-80v-608C800 227.904 764.128 192 720 192z m16 688c0 8.8-7.2 16-16 16h-544a16 16 0 0 1-16-16v-608a16 16 0 0 1 16-16h544a16 16 0 0 1 16 16v608z"
                        p-id="1445"
                        fill="#515151"
                      />
                      <path
                        d="M848 64h-544a32 32 0 0 0 0 64h544a16 16 0 0 1 16 16v608a32 32 0 1 0 64 0v-608C928 99.904 892.128 64 848 64z"
                        p-id="1446"
                        fill="#515151"
                      />
                      <path
                        d="M608 360H288a32 32 0 0 0 0 64h320a32 32 0 1 0 0-64zM608 520H288a32 32 0 1 0 0 64h320a32 32 0 1 0 0-64zM480 678.656H288a32 32 0 1 0 0 64h192a32 32 0 1 0 0-64z"
                        p-id="1447"
                        fill="#515151"
                      />
                    </svg>
                  </span>
                  <span>{{ scope.row.filePath }}</span>
                </div>
              </el-tooltip>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          sortable
          prop="lastModified"
          property="lastModified"
          :label="$t('transfer.modificationTime')"
        >
          <template slot-scope="scope">{{
            scope.row.lastModified == null ? "-" : scope.row.lastModified
          }}</template>
        </el-table-column>
      </el-table>

      <!-- 盒子模式 -->
      <div v-show="listShowType == 'box'" class="boxList">
        <ul>
          <li v-for="(item, index) in filelist" :key="index">
            <div
              class="boxList-item"
              :class="
                multipleSelection.includes(item)
                  ? 'active'
                  : nowMouse === index
                  ? 'active'
                  : ''
              "
              @mouseover="nowMouse = index"
              @mouseout="nowMouse = ''"
              @dblclick="dblclickFile(item, item)"
              @click="clickFile(item, item)"
            >
              <!-- 添加或修改文件 确认及取消按钮 -->
              <div
                v-if="item.mode && (item.mode == 'add' || item.mode == 'edit')"
                class="fileNameEdit"
              >
                <i
                  class="iconfont icon-duigou iconAdd"
                  @click="toAddFile(item)"
                />

                <i
                  class="iconfont icon-chacha iconClose"
                  @click="closeAddFile(item)"
                />
              </div>
              <!-- 是否选中图标显示 -->
              <div
                v-if="
                  item.mode
                    ? false
                    : multipleSelection.includes(item)
                    ? true
                    : nowMouse === index
                    ? true
                    : false
                "
                class="nowFileIcon"
              >
                <i
                  class="iconfont iconNo"
                  :class="
                    multipleSelection.includes(item)
                      ? 'icon-duigou iconYes'
                      : ''
                  "
                />
              </div>
              <div class="file-icon">
                <file-icon
                  :file-name="JSON.parse(JSON.stringify(item.name))"
                  :file-type="item.type"
                />
              </div>
              <div class="file-name">
                <span
                  v-if="
                    item.mode && (item.mode == 'add' || item.mode == 'edit')
                  "
                  class="addFileSet"
                >
                  <el-input v-model="item.name" size="mini" placeholder="" />
                  <span class="itemTime">{{
                    item.lastModified == null
                      ? "-"
                      : item.lastModified.substring(
                          5,
                          item.lastModified.length - 3
                        )
                  }}</span>
                </span>
                <el-tooltip
                  v-else
                  class="box-item"
                  effect="dark"
                  :content="item.name"
                  placement="bottom-start"
                  :open-delay="500"
                >
                  <div slot="content">
                    <span class="displayB"
                      >{{ $t("transfer.name") }}：{{ item.name }}</span
                    >
                    <span class="displayB"
                      >{{ $t("transfer.size") }}：{{
                        item.size == null ? "-" : getfilesize(item.size)
                      }}</span
                    >
                    <span class="displayB"
                      >{{ $t("transfer.modificationTime") }}：{{
                        item.lastModified == null ? "-" : item.lastModified
                      }}</span
                    >
                    <span class="displayB"
                      >{{ $t("transfer.filePath") }}：{{
                        item.filePath == null ? "-" : item.filePath
                      }}
                      <i
                        class="iconfont icon-fuzhi"
                        style="cursor: pointer"
                        @click.stop="copy(item.filePath)"
                      />
                    </span>
                  </div>
                  <div class="overflow-ellipsis">
                    <span
                      class="itemName overflow-ellipsis"
                      @click="dblclickFile(item, item)"
                      >{{ item.name }}</span
                    >
                    <span class="itemTime">{{
                      item.lastModified == null
                        ? "-"
                        : item.lastModified.substring(
                            5,
                            item.lastModified.length - 3
                          )
                    }}</span>
                  </div>
                </el-tooltip>
              </div>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <!-- 文件列表结束 -->
    <!-- 本地文件列表 -->
    <transprop
      ref="transferRef"
      @toupdate="fileclick"
      @todownload="downloadfile"
    />
    <!-- 文件传输列表 -->
    <!-- <transfer-list ref="transferListRef" @totransferCallback="transferCallback"></transfer-list> -->
    <transfer-dialog ref="transferdialogRef" :lnjoying-token="lnjoyingToken" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  transmissionBrowse,
  transmissionRmdir,
  transmissionMkdir,
  transmissionUpdateDir,
  transmissionPing,
  transmissionLogin,
  transmissionUserInfo,
  transmissionFsStore,
  transmissionUpload,
  transmissionDownload,
  getStoreNameSpace,
} from "@/api/transfer/mainApi";
import fileIcon from "@/components/transfer/fileIcon.vue";
import transprop from "@/components/transfer/transfer.vue"; // 传输弹窗（上传下载）
// import transferList from "@/components/transfer/transferList.vue"; //传输弹窗（上传下载）
import transferDialog from "@/components/transfer/transferDialog.vue"; // 温馨提示
import Cookies from "js-cookie";
export default {
  name: "Transfer",
  components: { fileIcon, transprop, transferDialog },
  data() {
    return {
      // 查询传参
      lnjoyingToken: "",
      queryData: {
        name: "",
      },
      file_settype: "",
      // 加载中
      loading: false,
      // 鼠标移入本地文件
      nowMouseLocal: "",
      // 鼠标移入
      nowMouse: "",
      // list box
      listShowType: "list",
      // 文件列表
      filelist: [],
      showfilelist: [],

      // 多选
      multipleSelection: [],
      // path为URL地址中的参数，为空时，表示浏览的是根目录；非空时，表示该目录下的内容
      getfilepath: "",
      // 用户名
      userName: "",
      storeNsId: "",
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  watch: {},
  created() {},
  mounted() {
    this.isLogin();
  },
  methods: {
    // 在线预览
    pushhelp() {
      window.open("/help/helpCenter/transferService", "_blank");
    },
    // 登录
    async isLogin() {
      await transmissionLogin(Cookies.get("Access-Token"))
        .then((res) => {
          this.lnjoyingToken = res.obj || "11";
          this.getInfoList();
          this.gettransInfo();
          this.getSiteList();
        })
        .catch((error) => {});
    },

    fileListPage(list) {
      this.showfilelist = list;
    },
    // 查询
    Search() {
      const search = this.queryData.name.trim(); // 去除空格
      const list = [];
      if (search) {
        this.filelist.filter((data) => {
          if (data.name.indexOf(search) > -1) {
            list.push(data);
          }
        });
        this.fileListPage(list);
      } else {
        this.fileListPage(this.filelist);
      }
    },
    // 重置
    ResetData() {
      this.queryData.name = "";
      this.getFile();
    },
    // 复制
    copy(value) {
      const textarea = document.createElement("textarea");
      textarea.value = value;
      document.body.appendChild(textarea);
      textarea.select();
      const isCopy = document.execCommand("copy"); // isCopy可以表示是否调用execCommand()成功
      document.body.removeChild(textarea);
      this.$notify({
        title: this.$t("transfer.copySuccess"),
        type: "success",
        duration: 2500,
      });
    },
    // transferCallback (data) {
    //   //传输回调 1.有任务完成
    //   if (data == "1") {
    //     this.getFile();
    //   }
    // },
    async getping() {
      try {
        const a = await transmissionPing();
        return false;
      } catch (error) {
        return true;
      }
    },
    async openDialog(type) {
      var thisT = this;
      // 上传
      if (await thisT.getping()) {
        thisT.$store.state.transfer.transferDialogStatus = true;
        return;
      }
      thisT.file_settype = type; // 当前上传类型
      if (type == 1) {
        if (thisT.multipleSelection.length < 1) {
          // 是否选择了文件
          thisT.$notify({
            title: this.$t("transfer.pleaseSelectFile"),
            type: "warning",
            duration: 2500,
          });
          return;
        }
        // 下载
        thisT.$refs.transferRef.browseliststatus = true;
        thisT.$refs.transferRef.file_updata(thisT.file_settype);
      } else {
        // 上传
        thisT.$refs.transferRef.browseliststatus = true;
        thisT.$refs.transferRef.file_updata(thisT.file_settype);
      }
    },
    getfilepathData(data) {
      return data.split("/");
    },
    rootdirectory(index) {
      var data = (this.getfilepath + "/").substring(
        0,
        this.findww(this.getfilepath + "/", "/", index)
      );
      this.$refs.multipleTableRef.clearSelection(); // 文件选择清空
      this.getfilepath = data; // 跳转页面的路径
      this.getFile(); // 请求文件列表
    },
    findww(str, cha, num) {
      var x = str.indexOf(cha);
      for (var i = 0; i < num; i++) {
        x = str.indexOf(cha, x + 1);
      }
      return x;
    },
    clickFile(row, item) {
      var _this = this;
      _this.$refs.multipleTableRef.toggleRowSelection(item);
    },
    dblclickFile(row, event) {
      // 双击文件
      if (event.mode == "add" || event.mode == "edit") {
        return;
      }
      if (event.type == 1) {
        this.$refs.multipleTableRef.clearSelection(); // 文件选择清空
        this.getfilepath = event.filePath;
        this.getFile();
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    async getFile() {
      this.filelist = [];
      this.showfilelist = [];
      if (this.storeNsId == "") {
        return;
      }
      this.loading = true;
      await transmissionBrowse(
        "",
        {
          storeNsId: this.storeNsId,
        },
        {
          path: this.getfilepath,
        }
      )
        .then((res) => {
          this.loading = false;
          this.filelist = res; // 文件列表
          this.fileListPage(res);
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    delFile() {
      // 删除文件
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("transfer.pleaseSelectFile"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      var pathMain = this.multipleSelection.map((item) => {
        // 获取全部选中的文件的path
        return item.filePath;
      });
      this.$confirm(
        this.$t("transfer.deleteSelectedFile"),
        this.$t("transfer.tip"),
        {
          confirmButtonText: this.$t("transfer.determine"),
          cancelButtonText: this.$t("transfer.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          transmissionRmdir(this.storeNsId, {
            paths: pathMain,
          })
            .then((res) => {
              this.$notify({
                title: this.$t("transfer.deletedSelectedFile"),
                type: "success",
                duration: 2500,
              });
              this.getFile();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    rename() {
      // 重命名
      if (this.multipleSelection.length == 0) {
        this.$notify({
          title: this.$t("transfer.pleaseSelectFile"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (this.multipleSelection.length > 1) {
        this.$notify({
          title: this.$t("transfer.onlyOneFolder"),
          type: "warning",
          duration: 2500,
        });
        return;
      }

      var oldName = JSON.parse(JSON.stringify(this.multipleSelection[0].name));
      this.$set(this.multipleSelection[0], "mode", "edit");
      this.$set(this.multipleSelection[0], "oldName", oldName);
    },
    addFile() {
      // 创建目录
      this.filelist.unshift({
        name: "",
        mode: "add",
      });
    },
    toAddFile(item) {
      console.log(item);
      var pattern = /^[\w\s~!@.#$%^&()_\-{}'A-Za-z0-9\u4e00-\u9fa5]{1,248}$/;
      // 确认创建目录或修改
      if (item.name == "") {
        this.$notify({
          title: this.$t("transfer.pleaseInputDirectoryName"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      console.log(!pattern.test(item.name));
      if (!pattern.test(item.name)) {
        this.$notify({
          title: this.$t("transfer.onlySupportChineseLettersNumbersSymbols"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (item.mode == "add") {
        transmissionMkdir(this.storeNsId, {
          path: this.getfilepath + "/" + item.name,
        })
          .then((res) => {
            this.$notify({
              title: this.$t("transfer.createDirectorySuccess"),
              type: "success",
              duration: 2500,
            });
            this.getFile(); // 请求文件列表
          })
          .catch((err) => {
            console.error(err.response.data.message);
          });
      } else if (item.mode == "edit") {
        transmissionUpdateDir(this.storeNsId, {
          src: this.getfilepath + "/" + item.oldName,
          dst: this.getfilepath + "/" + item.name,
        })
          .then((res) => {
            this.$notify({
              title: this.$t("transfer.modifyDirectorySuccess"),
              type: "success",
              duration: 2500,
            });
            this.getFile(); // 请求文件列表
          })
          .catch((err) => {
            console.error(err.response.data.message);
          });
      }
    },
    closeAddFile(item) {
      // 关闭创建目录或修改
      if (item.mode == "add") {
        this.filelist.shift();
      } else if (item.mode == "edit") {
        item.name = JSON.parse(JSON.stringify(item.oldName));
        item.mode = "";
        item.oldName = "";
      }
    },
    async getInfoList() {
      await transmissionUserInfo()
        .then((res) => {
          if (res != null) {
            console.log(res);
            localStorage.setItem("transmissionUserInfo", JSON.stringify(res));
            this.userName = res.userName;
          }
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    async getSiteList() {
      var data = {
        pageNumber: 1,
        pageSize: 99999,
        active: 1,
      };
      await getStoreNameSpace(data)
        .then((res) => {
          res.obj && res.obj.storeNameSpaceInfos
            ? (this.storeNsId = res.obj.storeNameSpaceInfos[0].storeNs)
            : "";
          this.getFile();
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    async gettransInfo() {
      await transmissionFsStore("CPU")
        .then((res) => {
          localStorage.setItem("transmissionInfo", JSON.stringify(res));
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    downloadfile(list) {
      // 下载文件
      if (list.length == 0) {
        this.$notify({
          title: this.$t("transfer.pleaseSelectFile"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      if (list.length > 1) {
        this.$notify({
          title: this.$t("transfer.onlyOneFolder"),
          type: "warning",
          duration: 2500,
        });
        return;
      }

      console.log(2);
      transmissionDownload({
        remotePaths: this.multipleSelection.map((item) => {
          return item.filePath;
        }), // 文件要传到目标位置的路径
        localPath: list[0].localPath, // 要传输的本地文件（文件夹）路径
        root: JSON.parse(localStorage.getItem("transmissionInfo")).root,
        storeNameSpace: {
          storeNameSpace: JSON.parse(localStorage.getItem("transmissionInfo"))
            .storeInfo[0].storeNameSpace,
          netEntityInfo: JSON.parse(localStorage.getItem("transmissionInfo"))
            .storeInfo[0].netEntityInfo,
        }, // 存储空间的信息文件上传的存储空间的信息
      })
        .then((res) => {
          this.$notify({
            title: this.$t("transfer.startDownload"),
            type: "success",
            duration: 2500,
          });
        })
        .catch((error) => {
          this.$notify({
            title: this.$t("transfer.downloadFailed"),
            type: "warning",
            duration: 2500,
          });
        });
      this.$refs.transferRef.browseliststatus = false;
      this.$refs.transferRef.$refs.multipleSelectionLocal.clearSelection(); // 本地文件选择清空
    },
    getfilesize(fileByte) {
      // 文件大小单位进位
      if (fileByte == 0) {
        return 0;
      }
      var fileSizeByte = (fileByte + "").replace(new RegExp(",", "g"), "") * 1;
      var fileSizeMsg = "";
      if (fileSizeByte < 1024) {
        fileSizeByte = Number(fileSizeByte);
        fileSizeMsg = this.$script.processing_numbers(fileSizeByte) + "B";
      } else if (fileSizeByte == 1024) {
        fileSizeMsg = "1.00KB";
      } else if (fileSizeByte > 1024 && fileSizeByte < 1048576) {
        fileSizeMsg =
          this.$script.processing_numbers(fileSizeByte / 1024) + "KB";
      } else if (fileSizeByte == 1048576) {
        fileSizeMsg = "1.00MB";
      } else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) {
        fileSizeMsg =
          this.$script.processing_numbers(fileSizeByte / (1024 * 1024)) + "MB";
      } else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) {
        fileSizeMsg = "1.00GB";
      } else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) {
        fileSizeMsg =
          this.$script.processing_numbers(fileSizeByte / (1024 * 1024 * 1024)) +
          "GB";
      } else {
        fileSizeMsg =
          this.$script.processing_numbers(
            fileSizeByte / (1024 * 1024 * 1024 * 1024)
          ) + "TB";
      }

      return fileSizeMsg;
    },
    fileclick(list) {
      // 组件调用 上传文件
      if (list.length == 0) {
        this.$notify({
          title: this.$t("transfer.pleaseSelectFile"),
          type: "warning",
          duration: 2500,
        });
        return;
      }

      list.forEach((item) => {
        transmissionUpload({
          remotePath: this.getfilepath, // 文件要传到目标位置的路径
          localPath: item.localPath, // 要传输的本地文件（文件夹）路径
          root: JSON.parse(localStorage.getItem("transmissionInfo")).root,
          storeNameSpace: {
            storeNameSpace: JSON.parse(localStorage.getItem("transmissionInfo"))
              .storeInfo[0].storeNameSpace,
            netEntityInfo: JSON.parse(localStorage.getItem("transmissionInfo"))
              .storeInfo[0].netEntityInfo,
          }, // 存储空间的信息文件上传的存储空间的信息
          storeBack: JSON.parse(localStorage.getItem("transmissionInfo"))
            .storeInfo[0].storeNameSpace,
        })
          .then((res) => {
            this.$notify({
              title: item.name + this.$t("transfer.startUpload"),
              type: "success",
              duration: 2500,
            });
          })
          .catch((error) => {
            this.$notify({
              title: item.name + this.$t("transfer.uploadFailed"),
              type: "warning",
              duration: 2500,
            });
          });
      });
      this.$refs.transferRef.browseliststatus = false;
      this.$refs.transferRef.$refs.multipleSelectionLocal.clearSelection(); // 本地文件选择清空
    },
  },
};
</script>

<style lang="stylus" scoped>
.help_document{
  display:flex;
  align-items: center;
  width: 250px;
  float: right;
  font-size: 14px;
  line-height: 32px;
  .text{
    color:rgb(64, 158, 255);
  }
  .line{
    color:rgb(195, 195, 195);
  }
}
::v-deep .el-card__body {
  padding: 10px 0px 10px 20px;
}

::v-deep .el-card {
  margin-top: -10px;
}

.transferFileStyle:hover {
  color: #096dd9;
 }

.breadcrumbMain {
  padding: 10px 20px 2px 0;
  float: left;
  width: 550px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;

  .el-breadcrumb {
    padding: 0;
    margin: 0;

    ::v-deep .el-breadcrumb__inner {
      padding: 0;
      margin: 0;
      font-size: 14px;
      cursor: pointer;

      &:hover {
        color: #096DD9;
        font-weight: bold;
      }

      a {
        padding: 0;
        margin: 0;
      }
    }

    ::v-deep .el-breadcrumb__separator {
      margin: 0 1px;
    }
  }
}

.svg svg {
  display: inline-block;
  margin-left: 4px;
}

svg:hover {
  transform: scale(1.25);
}

.buttonMain {
  float: right;
}

::v-deep .el-table .cell {
  overflow: inherit;
}

.listTop {
  margin-bottom: 15px;
  background-color: #fff;
  overflow: hidden;
  padding: 20px 0px 0;
}

::v-deep .addFileSet .el-input-group__append, .el-input-group__prepend {
  padding: 0;
}

.addFileSet .el-input .el-input-group__append i {
  display: inline-block;
  font-size: 12px;
  border-radius: 0px;
  width: 26px;
  height: 26px;
  line-height: 24px;
  text-align: center;
  cursor: pointer;
}

.addFileSet .el-input .el-input-group__append i.iconAdd {
  border: 1px solid #096dd9;
  background-color: #096dd9;
  color: #fff;
}

.addFileSet .el-input .el-input-group__append i.iconClose {
  border: 1px solid #f56c6c;
  background-color: #f56c6c;
  color: #fff;
}

.overflow-ellipsis {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
