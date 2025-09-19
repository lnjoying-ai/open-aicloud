<template>
  <div class="transferList">
    <!-- 进程 -->
    <div
      v-show="dialogprogress"
      ref="progressMain"
      class="progressMain"
      :class="viewSize ? 'active' : ''"
    >
      <div v-show="progressIconType" ref="progresstitle" class="progressdiv">
        <h4 style="">
          {{ $t("transfer.fileTransfer") }}
          <span class="icon2" @click="progressIconType = false">
            <svg
              t="1650522677323"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="4508"
              width="200"
              height="200"
            >
              <path
                d="M548.992 503.744L885.44 167.328a31.968 31.968 0 1 0-45.248-45.248L503.744 458.496 167.328 122.08a31.968 31.968 0 1 0-45.248 45.248l336.416 336.416L122.08 840.16a31.968 31.968 0 1 0 45.248 45.248l336.416-336.416L840.16 885.44a31.968 31.968 0 1 0 45.248-45.248L548.992 503.744z"
                p-id="4509"
                fill="#999999"
              />
            </svg>
          </span>
          <span v-show="!viewSize" class="icon1" @click="viewclick">
            <svg
              t="1650522463463"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="4158"
              width="200"
              height="200"
            >
              <path
                d="M368.896 192H224a32 32 0 0 0-32 32v137.888a32 32 0 0 0 64 0V256h112.896a32 32 0 0 0 0-64zM784.864 192H640a32 32 0 1 0 0 64h112.864v105.888a32 32 0 1 0 64 0V224a32 32 0 0 0-32-32zM368.896 777.92H256V672a32 32 0 1 0-64 0v137.92a32 32 0 0 0 32 32h144.896a32 32 0 1 0 0-64zM784.864 640a32 32 0 0 0-32 32v105.92H640a32 32 0 1 0 0 64h144.864a32 32 0 0 0 32-32V672a32 32 0 0 0-32-32z"
                p-id="4159"
                fill="#999999"
              />
              <path
                d="M912 48h-800c-35.296 0-64 28.704-64 64v800c0 35.296 28.704 64 64 64h800c35.296 0 64-28.704 64-64v-800c0-35.296-28.704-64-64-64z m-800 864v-800h800l0.064 800H112z"
                p-id="4160"
                fill="#999999"
              />
            </svg>
          </span>
          <span v-show="viewSize" class="icon1" @click="viewclick">
            <svg
              t="1650522587037"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="4333"
              width="200"
              height="200"
            >
              <path
                d="M912 48h-800c-35.296 0-64 28.704-64 64v800c0 35.296 28.704 64 64 64h800c35.296 0 64-28.704 64-64v-800c0-35.296-28.704-64-64-64z m-800 864v-800h800l0.064 800H112z"
                p-id="4334"
                fill="#999999"
              />
              <path
                d="M368.896 192a32 32 0 0 0-32 32v105.888H224a32 32 0 0 0 0 64h144.896a32 32 0 0 0 32-32V224a32 32 0 0 0-32-32zM784.864 329.888H672V224a32 32 0 1 0-64 0v137.888a32 32 0 0 0 32 32h144.864a32 32 0 1 0 0-64zM368.896 640H224a32 32 0 1 0 0 64h112.896v105.92a32 32 0 1 0 64 0V672a32 32 0 0 0-32-32zM784.864 640H640a32 32 0 0 0-32 32v137.92a32 32 0 1 0 64 0V704h112.864a32 32 0 1 0 0-64z"
                p-id="4335"
                fill="#999999"
              />
            </svg>
          </span>
        </h4>
        <div class="btnAllStyle">
          <el-button
            type="primary"
            size="mini"
            @click="file_stop({ transId: '' })"
          >
            {{ $t("transfer.allPause") }}
          </el-button>
          <el-button
            type="primary"
            size="mini"
            @click="file_start({ transId: '' })"
          >
            {{ $t("transfer.allStart") }}
          </el-button>
        </div>
        <div style="padding: 15px">
          <el-table
            :data="filemain"
            size="mini"
            stripe
            :max-height="viewSize ? 'calc(100vh - 85px)' : '300'"
            style="width: 100%"
          >
            <el-table-column
              type="index"
              width="50"
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
            <el-table-column :label="$t('transfer.NameFolder')" width="100">
              <template slot-scope="scope">
                <span class="overflow-ellipsis">
                  <span v-if="scope.row.direction == 'UP'">
                    <el-tooltip
                      class="box-item"
                      effect="dark"
                      :content="getLocalPath(scope.row.localPath)"
                      placement="top-start"
                      open-delay="1000"
                    >
                      <div class="overflow-ellipsis">
                        <file-icon
                          style="width: 14px"
                          :file-name="
                            JSON.parse(JSON.stringify(scope.row.localPath))
                          "
                          :file-type="scope.row.type"
                        />
                        <span style="margin-left: 4px">
                          {{ getLocalPath(scope.row.localPath) }}
                        </span>
                      </div>
                    </el-tooltip>
                  </span>
                  <span v-if="scope.row.direction != 'UP'">
                    <el-tooltip
                      class="box-item"
                      effect="dark"
                      :content="getRemotePath(scope.row.remotePath)"
                      placement="top-start"
                      open-delay="1000"
                    >
                      <div class="overflow-ellipsis">
                        <file-icon
                          style="width: 14px"
                          :file-name="
                            JSON.parse(JSON.stringify(scope.row.remotePath))
                          "
                          :file-type="scope.row.type"
                        />
                        <span style="margin-left: 4px">
                          {{ getRemotePath(scope.row.remotePath) }}
                        </span>
                      </div>
                    </el-tooltip>
                  </span>
                </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="name"
              :label="$t('transfer.fileSize')"
              width="180"
            >
              <template slot-scope="scope">
                <span
                  :style="
                    scope.row.transSize.replace(/[^0-9]/gi, '') !=
                    scope.row.totalSize.replace(/[^0-9]/gi, '')
                      ? 'color:#0F97FF'
                      : ''
                  "
                  >{{
                    getfilesize(scope.row.transSize.replace(/[^0-9]/gi, ""))
                  }}</span
                >
                <span
                  >/{{
                    getfilesize(scope.row.totalSize.replace(/[^0-9]/gi, ""))
                  }}({{ Math.floor(scope.row.progress) }}%)</span
                >
              </template>
            </el-table-column>
            <el-table-column
              prop="address"
              width="120"
              :label="$t('transfer.filespeed')"
            >
              <template slot-scope="scope">
                <i
                  v-if="scope.row.direction == 'UP'"
                  style="font-size: 12px; padding-right: 4px"
                  class="iconfont icon-shangchuan"
                />
                <i
                  v-if="scope.row.direction != 'UP'"
                  style="font-size: 12px; padding-right: 4px"
                  class="iconfont icon-xiazai"
                />
                {{
                  getfilesize(scope.row.speed.replace(/[^0-9]/gi, ""), "传输")
                }}
              </template>
            </el-table-column>
            <el-table-column
              prop="address"
              width="150"
              :label="$t('transfer.state')"
            >
              <template slot-scope="scope">
                <span v-if="scope.row.status == '1'">
                  <span
                    :style="
                      scope.row.costtime != scope.row.totaltime
                        ? 'color:#0F97FF'
                        : ''
                    "
                    >{{ initTime(scope.row.costtime) }}</span
                  >
                  <span>/{{ initTime(scope.row.totaltime) }}</span>
                </span>
                <span
                  v-else
                  :style="scope.row.status == '5' ? 'color:red;' : ''"
                >
                  {{ getStatus(scope.row.status) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="address"
              width="100"
              :label="$t('transfer.operation')"
            >
              <template slot-scope="scope">
                <el-button
                  v-if="scope.row.status == '3'"
                  type="text"
                  @click="file_start(scope.row)"
                >
                  <i style="font-size: 20px" class="iconfont icon-qidong" />
                </el-button>
                <el-button
                  v-if="scope.row.status == '0' || scope.row.status == '1'"
                  type="text"
                  @click="file_stop(scope.row)"
                >
                  <i style="font-size: 20px" class="iconfont icon-zanting" />
                </el-button>
                <el-button type="text" @click="file_delprogress(scope.row)">
                  <i
                    style="font-size: 19px; font-weight: bold"
                    class="iconfont icon-cuowutishi"
                  />
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <transfer-loading
        v-show="!progressIconType"
        :list-number="filemain.length"
        class="transgerLoadingIcon"
        @click.native="progressIconType = true"
      />
    </div>
  </div>
</template>
<script>
import {
  transmissionQuery,
  transmissionDeleteProgress,
  transmissionStopProgress,
  transmissionStartProgress,
} from "@/api/transfer/mainApi";
import fileIcon from "@/components/transfer/fileIcon.vue";
import transferLoading from "@/components/transfer/transferListLoading.vue";

export default {
  components: { fileIcon, transferLoading },

  data() {
    return {
      timer: null, // 定时器
      filemain: [], // 传输列表
      nowProgressList: [], // 上次请求内容
      dialogprogressform: true,
      dialogprogress: false,
      progressIconType: false,
      viewSize: false,
      viewSet: {
        mouseStatus: false, // false:鼠标抬起，true:鼠标按下
        mouseX: 0, // 鼠标按下时的X坐标
        mouseY: 0, // 鼠标按下时的Y坐标
        mouseMoveX: 0, // 鼠标移动时的X坐标
        mouseMoveY: 0, // 鼠标移动时的Y坐标
        translateX: 0, // X轴移动距离
        translateY: 0, // Y轴移动距离
      },
    };
  },
  watch: {
    progressIconType(val) {
      if (!val) {
        this.viewInit();
      }
    },
  },
  beforeDestroy() {
    clearInterval(this.timer);
    this.timer = null;
  },
  mounted() {
    this.timer = setInterval(this.getprogress, "1000");
    // this.$refs.progresstitle.style.display = 'none'
    var progressTitle = this.$refs.progresstitle;
    progressTitle.onmousedown = (e) => {
      // 按下
      console.log(e);
      this.viewSet.mouseX = e.clientX; // 鼠标按下时的X坐标
      this.viewSet.mouseY = e.clientY; // 鼠标按下时的Y坐标
      this.viewSet.mouseStatus = true; // 鼠标按下

      // this.$refs.progressMain
    };
    progressTitle.onmouseup = (e) => {
      // 抬起
      console.log(e);
      this.viewSet.mouseStatus = false;
    };
    document.onmousemove = (e) => {
      // console.log(e)
      if (this.viewSet.mouseStatus) {
        // 鼠标按下
        this.viewSet.mouseMoveX = e.clientX; // 鼠标移动时的X坐标
        this.viewSet.mouseMoveY = e.clientY; // 鼠标移动时的Y坐标
        this.viewSet.translateX =
          this.viewSet.translateX +
          (this.viewSet.mouseMoveX - this.viewSet.mouseX);
        this.viewSet.translateY =
          this.viewSet.translateY +
          (this.viewSet.mouseMoveY - this.viewSet.mouseY);
        this.$refs.progressMain.style.transform =
          "translate(" +
          this.viewSet.translateX +
          "px," +
          this.viewSet.translateY +
          "px)";
        this.$refs.progressMain.style.webkitTransform =
          "translate(" +
          this.viewSet.translateX +
          "px," +
          this.viewSet.translateY +
          "px)";
        this.$refs.progressMain.style.mozTransform =
          "translate(" +
          this.viewSet.translateX +
          "px," +
          this.viewSet.translateY +
          "px)";
        this.$refs.progressMain.style.msTransform =
          "translate(" +
          this.viewSet.translateX +
          "px," +
          this.viewSet.translateY +
          "px)";
        this.$refs.progressMain.style.oTransform =
          "translate(" +
          this.viewSet.translateX +
          "px," +
          this.viewSet.translateY +
          "px)";

        this.viewSet.mouseX = this.viewSet.mouseMoveX; // 鼠标按下时的X坐标
        this.viewSet.mouseY = this.viewSet.mouseMoveY; // 鼠标按下时的Y坐标
      } else {
      }
    };
  },
  methods: {
    getLocalPath(val) {
      if (this.$store.state.transfer.windowstatus) {
        return val.substring(val.lastIndexOf("\\") + 1, val.length);
      } else if (this.$store.state.transfer.macstatus) {
        return val.substring(val.lastIndexOf("/") + 1, val.length);
      } else {
        return val.substring(val.lastIndexOf("/") + 1, val.length);
      }
    },
    getRemotePath(val) {
      if (this.$store.state.transfer.winstatus) {
        return val.substring(val.lastIndexOf("/") + 1, val.length);
      } else if (this.$store.state.transfer.macstatus) {
        return val.substring(val.lastIndexOf("/") + 1, val.length);
      } else {
        return val.substring(val.lastIndexOf("/") + 1, val.length);
      }
    },
    getStatus(item) {
      if (item == "0" || item === "0") {
        return this.$t("transfer.waiting");
      } else if (item == "1" || item === 1) {
        return this.$t("transfer.transferring");
      } else if (item == "2" || item === 2) {
        return this.$t("transfer.done");
      } else if (item == "3" || item === 3) {
        return this.$t("transfer.paused");
      } else if (item == "4" || item === 4) {
        return this.$t("transfer.cancelled");
      } else if (item == "5" || item === 5) {
        return this.$t("transfer.fail");
      } else {
        return "-";
      }
    },
    viewInit() {
      this.$refs.progressMain.style.transform = "translate(0px,0px)";
      this.$refs.progressMain.style.webkitTransform = "translate(0px,0px)";
      this.$refs.progressMain.style.mozTransform = "translate(0px,0px)";
      this.$refs.progressMain.style.msTransform = "translate(0px,0px)";
      this.$refs.progressMain.style.oTransform = "translate(0px,0px)";
      this.viewSet.mouseX = 0; // 鼠标按下时的X坐标
      this.viewSet.mouseY = 0; // 鼠标按下时的Y坐标
      this.viewSet.mouseMoveX = 0; // 鼠标移动时的X坐标
      this.viewSet.mouseMoveY = 0; // 鼠标移动时的Y坐标
      this.viewSet.translateX = 0; // X轴移动距离
      this.viewSet.translateY = 0; // Y轴移动距离
    },
    viewclick() {
      this.viewInit();
      this.viewSize = !this.viewSize;
    },
    async getprogress() {
      await transmissionQuery()
        .then((res) => {
          this.$store.state.transfer.transferDialogStatus = false;
          if (res.transInfo != "") {
            this.dialogprogress = true;
          }
          if (res.transInfo.length > 0) {
            for (var i = 0; i < res.transInfo.length; i++) {
              if (res.transInfo[i].progress == 100) {
                if (res.transInfo[i].totaltime != res.transInfo[i].costtime) {
                  res.transInfo[i].totaltime = res.transInfo[i].costtime;
                }
              }
            }
            this.filemain = res.transInfo;
            // console.log(this.filemain)
            // console.log('---刷新进度----')
            var nowProgressList = res.transInfo.map((item) => {
              return item.progress;
            });
            if (nowProgressList.toString() == this.nowProgressList.toString()) {
              // console.log('相等')
              // console.log(this.nowProgressList)
            } else {
              // console.log('不相等，请求文件列表')
              // console.log(nowProgressList)
              // console.log(this.nowProgressList)
              var nowProgressData = nowProgressList.filter((item) => {
                return item == "100";
              });
              var nowthisProgressData = this.nowProgressList.filter((item) => {
                return item == "100";
              });
              if (nowProgressData.length == nowthisProgressData.length) {
                // console.log('完成数量相等')
              } else {
                // console.log('完成数量相等多了')
                // console.log('传输完成-请求列表')
                this.$emit("totransferCallback", "1"); // 1传输完成
                // this.$parent.transferCallback('1') //1传输完成
              }
              // console.log(nowProgressList)
              // console.log(this.nowProgressList)
              this.nowProgressList = nowProgressList;
            }
          } else {
            this.filemain = res.transInfo;
          }
        })
        .catch((error) => {});
    },
    async file_start(item) {
      await transmissionStartProgress(item.transId)
        .then((res) => {})
        .catch((error) => {});
    },
    async file_stop(item) {
      await transmissionStopProgress(item.transId)
        .then((res) => {})
        .catch((error) => {});
    },
    async file_delprogress(item) {
      // 删除上传中的进程
      if (item.progress == "100") {
        await transmissionDeleteProgress(item.transId)
          .then((res) => {
            this.$notify({
              title: this.$t("transfer.completed"),
              type: "success",
              duration: 2500,
            });
            this.getFile();
          })
          .catch((error) => {});
      } else {
        this.$confirm(
          this.$t("transfer.deleteProgress"),
          this.$t("transfer.tip"),
          {
            confirmButtonText: this.$t("transfer.determine"),
            cancelButtonText: this.$t("transfer.cancel"),
            type: "warning",
          }
        ).then((action) => {
          transmissionDeleteProgress(item.transId)
            .then((res) => {
              this.$notify({
                title: this.$t("transfer.completed"),
                type: "success",
                duration: 2500,
              });
              this.getFile();
            })
            .catch((error) => {});
        });
      }
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
    initTime(DateStr) {
      // 时间戳转换
      if (DateStr != "") {
        if (DateStr.indexOf(",") != -1) {
          DateStr = DateStr.replace(new RegExp(",", "g"), "");
        }
        DateStr = Number(DateStr);
        // 相差的总秒数
        var totalSeconds = DateStr;
        // 取模（余数）
        var modulo = totalSeconds % (60 * 60 * 24);
        // 小时数
        var hours = Math.floor(totalSeconds / (60 * 60));
        modulo = modulo % (60 * 60);
        // 分钟
        var minutes = Math.floor(modulo / 60);
        // 秒
        var seconds = modulo % 60;
        // 输出到页面

        // return ((hours < 10 ? '0' + hours : hours) + ":" + (minutes < 10 ? '0' + minutes : minutes) +
        //   ":" + (seconds < 10 ? '0' + seconds : seconds));
        if (Object.is(hours, NaN)) {
          return "00" + ":" + "00" + ":" + "00" + "";
        } else {
          return (
            (hours < 10 ? "0" + hours : hours) +
            ":" +
            (minutes < 10 ? "0" + minutes : minutes) +
            ":" +
            (seconds < 10 ? "0" + seconds : seconds) +
            ""
          );
        }
      }
    },
  },
};
</script>
<style lang="stylus" scoped>
.transferList {
  .el-button {
    &.el-button--text {
      padding: 0;
    }
  }
  .transgerLoadingIcon {
    position: fixed;
    right: 20px;
    bottom: 20px;
    z-index: 3000;
    width: 60px;
    height: 60px;
    cursor: pointer;
  }
  .progressMain {
    position: fixed;
    right: 0;
    bottom: 0;
    z-index: 2000;
    &.active {
      .progressdiv {
        width: 100vw;
        height: 100vh;
      }
    }
    .progressdiv {
      width: 750px;
      border-top-left-radius: 7px;
      border-top-right-radius: 7px;
      border: 1px solid #e2e2e2;
      box-shadow: 0 0 10px #ccc;
      background-color: #fff;
      padding-bottom: 5px;
    }
    h4 {
      color: #333;
      margin: 0;
      padding: 10px 15px;
      overflow: hidden;
      font-size: 14px;
      span {
        cursor: pointer;
        float: right;
        display: block;
        width: 18px;
        height: 18px;
        margin-right: 15px;
        svg {
          width: 18px;
          height: 18px;
        }
        &.icon1 {
          // border: 1px solid #999;
        }
        &.icon2 {
          // border-bottom: 1px solid #999;
        }
      }
    }
  }
}
.btnAllStyle {
  text-align: right;
  padding-right: 20px;
}
.overflow-ellipsis {
  width: 100%;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
