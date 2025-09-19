<template>
  <div class="transferListMain">
    <!-- 本地文件列表 -->
    <el-dialog
      :visible.sync="browseliststatus"
      :title="$t('transfer.filelist')"
      width="640px"
      :close-on-click-modal="false"
    >
      <div class="browselist">
        <div class="btnMain">
          <div>
            <el-button type="primary" size="mini" @click="file_root()">{{
              $t("transfer.rootDirectory")
            }}</el-button>
            <el-button
              type="primary"
              size="mini"
              @click="back_last(file_getpath)"
              >{{ $t("transfer.back") }}</el-button
            >
          </div>

          <!-- <div class="fr icon"
               style="padding: 0">
            <span style="margin-right: 10px; cursor: pointer">
              <i class="iconfont icon-wenjianliebiao1"
                 @click="listShowType = 'list'"
                 :style="listShowType == 'list' ? 'color:#0d84ff' : ''"></i>
            </span>
            <span style="cursor: pointer">
              <i class="iconfont icon-wenjianliebiao"
                 @click="listShowType = 'box'"
                 :style="listShowType == 'box' ? 'color:#0d84ff' : ''"></i>
            </span>
          </div> -->
        </div>
        <div style="padding: 8px 0px">
          <el-input
            v-model="file_getpath"
            :placeholder="$t('transfer.EnterPath')"
            style="width: 100%"
            size="small"
            clearable
          >
            <template #append>
              <el-button slot="append" @click="file_updata(file_settype)">{{
                $t("transfer.GoTo")
              }}</el-button>
            </template>
          </el-input>
        </div>
        <div>
          <div class="fileList">
            <!-- 本地文件列表展示 -->
            <el-table
              v-show="listShowType == 'list'"
              ref="multipleSelectionLocal"
              max-height="450px"
              :data="browselist"
              stripe
              style="width: 100%"
              :default-sort="{ prop: 'date', order: 'descending' }"
              @selection-change="handleSelectionChangeLocal"
              @row-dblclick="dblclickLocalFile(row, $event)"
              @row-click="clickLocalFile(row, $event)"
            >
              <el-table-column type="selection" width="55" />
              <el-table-column
                :label="$t('transfer.fileName')"
                prop="name"
                sortable
              >
                <template slot-scope="scope">
                  <div class="overflow-ellipsis">
                    <el-tooltip
                      class="box-item"
                      effect="dark"
                      :content="scope.row.name"
                      placement="top-start"
                      :open-delay="1000"
                    >
                      <div class="overflow-ellipsis">
                        <file-icon
                          class="align-text-bottom"
                          style="width: 14px"
                          :file-name="
                            JSON.parse(JSON.stringify(scope.row.name))
                          "
                          :file-type="Number(scope.row.type)"
                        />
                        <span
                          :class="
                            scope.row.type == 1
                              ? 'cursor-pointer transferFileStyle'
                              : ''
                          "
                          style="margin-left: 4px"
                          @click="dblclickLocalFile(scope.row, scope.row)"
                          >{{ scope.row.name }}</span
                        >
                      </div>
                    </el-tooltip>
                  </div>
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
                    {{
                      scope.row.size == null ? "-" : getfilesize(scope.row.size)
                    }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('transfer.filePath')"
                prop="localPath"
              >
                <template slot-scope="scope">
                  <div class="overflow-ellipsis">
                    <el-tooltip
                      class="box-item"
                      effect="dark"
                      :content="scope.row.localPath"
                      placement="top-start"
                      :open-delay="1000"
                    >
                      <div class="overflow-ellipsis">
                        <span
                          ><i
                            class="iconfont icon-fuzhi"
                            style="cursor: pointer"
                            @click.stop="copy(scope.row.localPath)"
                          />{{ scope.row.localPath }}</span
                        >
                      </div>
                    </el-tooltip>
                  </div>
                </template>
              </el-table-column>
            </el-table>
            <!-- 本地文件盒子展示 -->
            <div v-show="listShowType == 'box'" class="boxList">
              <ul>
                <li v-for="(item, index) in browselist" :key="index">
                  <div
                    class="boxList-item"
                    :class="
                      multipleSelectionLocal.includes(item)
                        ? 'active'
                        : nowMouseLocal === index
                        ? 'active'
                        : ''
                    "
                    @mouseover="nowMouseLocal = index"
                    @mouseout="nowMouseLocal = ''"
                    @dblclick="dblclickLocalFile(item, item)"
                    @click="clickLocalFile(item, item)"
                  >
                    <!-- 是否选中图标显示 -->
                    <div
                      v-if="
                        multipleSelectionLocal.includes(item)
                          ? true
                          : nowMouseLocal === index
                          ? true
                          : false
                      "
                      class="nowFileIcon"
                    >
                      <i
                        class="iconfont iconNo"
                        :class="
                          multipleSelectionLocal.includes(item)
                            ? 'icon-duigou iconYes'
                            : ''
                        "
                      />
                    </div>
                    <div class="file-icon">
                      <file-icon
                        :file-name="JSON.parse(JSON.stringify(item.name))"
                        :file-type="Number(item.type)"
                      />
                    </div>
                    <div class="file-name">
                      <el-tooltip
                        class="box-item"
                        effect="dark"
                        :content="item.name"
                        placement="bottom-start"
                        :open-delay="500"
                      >
                        <template #content>
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
                              item.lastModified == null
                                ? "-"
                                : item.lastModified
                            }}</span
                          >
                          <span class="displayB"
                            >{{ $t("transfer.filePath") }}：{{
                              item.localPath == null ? "-" : item.localPath
                            }}
                            <i
                              class="iconfont icon-fuzhi"
                              style="cursor: pointer"
                              @click.stop="copy(item.localPath)"
                            />
                          </span>
                        </template>
                        <div class="overflow-ellipsis">
                          <span
                            class="itemName overflow-ellipsis"
                            @click="dblclickLocalFile(item, item)"
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
        </div>
      </div>

      <span slot="footer" class="dialog-footer" style="text-align: right">
        <el-button
          v-if="file_settype == 0"
          type="primary"
          size="mini"
          @click="fileclick"
          >{{ $t("transfer.Determine") }}</el-button
        >
        <el-button
          v-if="file_settype == 1"
          type="primary"
          size="mini"
          @click="download_file"
          >{{ $t("transfer.Determine") }}</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>
<script>
import fileIcon from "@/components/transfer/fileIcon.vue";
import { transmissionLs } from "@/api/transfer/mainApi";
export default {
  components: { fileIcon },
  props: {
    // 成功之后的函数
    fileName: {
      type: String,
      default: "",
    },
    type: {
      type: Number,
    },
  },
  data() {
    return {
      loading: true,
      file_settype: "",
      nowMouseLocal: "", // 鼠标移入本地文件
      nowMouse: "", // 鼠标移入
      listShowType: "list", // list box
      filelist: [], // 文件列表
      multipleSelection: [], // 多选
      multipleSelectionLocal: [], // 本地文件多选
      getfilepath: "", // path为URL地址中的参数，为空时，表示浏览的是根目录；非空时，表示该目录下的内容
      pingmodel: false, // 启动下载传输插件
      transfer: false, // 不同操作系统显示
      windowstatus: false, // 不同操作系统显示
      file_getpath: "", // //浏览本地文件 初始目录参数
      browseliststatus: false, // 选择文件弹窗
      browselist: [], // 本地文件列表
      transInfo: "", // 传输信息
      timer: null, // 定时器
      filemain: [], // 传输列表
      nowProgressList: [], // 传输列表
      dialogprogressform: true,
      dialogprogress: false,
    };
  },
  watch: {
    browseliststatus(val) {
      if (
        val &&
        this.file_settype == 1 &&
        localStorage.getItem("downloadFilePath")
      ) {
        this.file_getpath = JSON.parse(
          localStorage.getItem("downloadFilePath")
        );
        this.file_updata(this.file_settype);
      }
    },
  },
  mounted() {},
  methods: {
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
        duration: 2000,
      });
    },
    download_file() {
      // 下载文件
      if (this.multipleSelectionLocal.length > 1) {
        this.$notify({
          title: this.$t("transfer.onlyOneFolder"),
          type: "warning",
          duration: 2000,
        });
        return;
      }
      var data;
      if (this.multipleSelectionLocal.length == 0) {
        data = [
          {
            localPath: this.file_getpath,
          },
        ];
      } else {
        data = this.multipleSelectionLocal;
      }
      localStorage.setItem(
        "downloadFilePath",
        JSON.stringify(this.file_getpath)
      );

      this.$emit("todownload", data);
    },
    fileclick() {
      // 上传文件
      if (this.multipleSelectionLocal.length == 0) {
        this.$notify({
          title: this.$t("transfer.pleaseSelectFile"),
          type: "warning",
          duration: 2000,
        });
        return;
      }

      this.$emit("toupdate", this.multipleSelectionLocal);
    },
    file_root() {
      // 返回根目录
      this.file_getpath = "";
      this.file_updata(this.file_settype);
    },
    back_last(val) {
      // 返回上一级
      if (this.$store.state.transfer.macstatus) {
        // 苹果机
        if (this.file_getpath != "") {
          if (val.indexOf("/") == val.length - 1) {
            this.file_getpath = "";
          } else {
            if (val.lastIndexOf("/") === 0) {
              this.file_getpath = "/";
            } else {
              this.file_getpath = val.substring(0, val.lastIndexOf("/"));
              if (this.file_getpath != "") {
                if (!(this.file_getpath.indexOf("/") != -1)) {
                  this.file_getpath = this.file_getpath + "/";
                }
              }
            }
          }
        }
      } else if (this.$store.state.windowstatus) {
        // windows机
        if (val.indexOf("\\") == val.length - 1) {
          this.file_getpath = "";
        } else {
          this.file_getpath = val.substring(0, val.lastIndexOf("\\"));
          if (this.file_getpath != "") {
            if (!(this.file_getpath.indexOf("\\") != -1)) {
              this.file_getpath = this.file_getpath + "\\";
            }
          }
        }
      } else {
        if (val.indexOf("/") == val.length - 1) {
          this.file_getpath = "";
        } else {
          this.file_getpath = val.substring(0, val.lastIndexOf("/"));
          if (this.file_getpath != "") {
            if (!(this.file_getpath.indexOf("/") != -1)) {
              this.file_getpath = this.file_getpath + "/";
            }
          }
        }
      }
      this.file_updata(this.file_settype);
    },

    getfilesize(fileByte, status) {
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
        this.$script.processing_numbers(fileSizeByte / (1024 * 1024 * 1024)) +
          "GB";
      } else {
        fileSizeMsg =
          this.$script.processing_numbers(
            fileSizeByte / (1024 * 1024 * 1024 * 1024)
          ) + "TB";
      }
      if (status == "传输") {
        return fileSizeMsg + "/s";
      } else {
        return fileSizeMsg;
      }
    },
    async file_updata(type) {
      this.file_settype = type; // 当前上传类型
      this.loading = true;
      if (type == 1) {
        let params = {};
        if (this.file_getpath == "") {
          params = "";
        } else {
          params.path = this.file_getpath;
        }
        await transmissionLs(params)
          .then((res) => {
            this.browselist = [];
            this.browseliststatus = true;
            this.browselist = res.lsInfo.filter((item) => {
              item.size = item.size == null ? 0 : item.size * 1;
              return item.type == "1";
            });
          })
          .catch((error) => {});
      } else {
        let params = {};
        if (this.file_getpath == "") {
          params = "";
        } else {
          params.path = this.file_getpath;
        }
        await transmissionLs(params)
          .then((res) => {
            this.browseliststatus = true;
            this.browselist = res.lsInfo;
            this.browselist.map((item) => {
              item.size = item.size == null ? 0 : item.size * 1;
              return item;
            });
          })
          .catch((error) => {});
      }
    },
    clickLocalFile(row, item) {
      this.$refs.multipleSelectionLocal.toggleRowSelection(item);
    },
    dblclickLocalFile(row, event) {
      // 双击本地文件
      if (event.type == 1) {
        this.$refs.multipleSelectionLocal.clearSelection(); // 本地文件选择清空
        this.file_getpath = event.localPath;
        this.file_updata(this.file_settype);
      }
    },

    handleSelectionChangeLocal(val) {
      // 本地文件列表全选
      this.multipleSelectionLocal = val;
    },
  },
};
</script>
<style lang="stylus" scoped>
.transferFileStyle:hover {
  color: #096dd9;
}

::v-deep .el-dialog {
  .el-dialog__footer {
    text-align: right;
  }
}

.transferListMain {
  .btnMain {
    display: flex;
    justify-content: space-between;
    padding: 0px 0px;
    align-items: center;
  }
}

.fileList {
  .boxList {
    ul {
      padding: 0;
      overflow: hidden;

      li {
        float: left;
        padding: 15px 10px;
        list-style: none;

        .boxList-item {
          position: relative;
          padding: 5px 5px 0;
          border-radius: 8px;
          width: 90px;
          height: 120px;
          cursor: pointer;

          &.active {
            background-color: rgba(13, 132, 255, 0.1);
          }

          .nowFileIcon {
            position: absolute;
            top: 0;
            left: 0;

            i {
              display: inline-block;
              font-size: 12px;
              border-radius: 0px;
              width: 14px;
              height: 14px;
              line-height: 14px;
              text-align: center;
              cursor: pointer;
              border-radius: 4px;

              &.iconNo {
                border: 1px solid #096dd9;
                background-color: #fff;
                color: #fff;
              }

              &.iconYes {
                border: 1px solid #096dd9;
                background-color: #096dd9;
                color: #fff;
              }
            }
          }

          .fileNameEdit {
            position: absolute;
            top: 0;
            right: 0;

            i {
              display: inline-block;
              font-size: 12px;
              border-radius: 0px;
              width: 14px;
              height: 14px;
              line-height: 14px;
              text-align: center;
              cursor: pointer;
              border-radius: 4px;

              &.iconClose {
                border: 1px solid #f56c6c;
                background-color: #f56c6c;
                color: #fff;
              }

              &.iconAdd {
                border: 1px solid #096dd9;
                background-color: #096dd9;
                color: #fff;
              }
            }
          }

          .file-icon {
            height: 50px;
            padding: 15px 0 5px;
            margin: 0 auto 0;
            text-align: center;
          }

          .file-name {
            text-align: center;

            .itemName {
              display: block;
              padding-top: 2px;
              font-size: 12px;
              line-height: 18px;
              color: #333;
            }

            .itemTime {
              display: block;
              font-size: 12px;
              line-height: 18px;
              color: #666;
            }

            .addFileSet {
              .el-input {
                .el-input__inner {
                  padding: 0 5px;
                  height: 22px;
                  line-height: 22px;
                }
              }
            }
          }
        }
      }
    }
  }
}
</style>
