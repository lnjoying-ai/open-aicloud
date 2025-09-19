<template>
  <div class="commandPage bg-black h-full">
    <div
      class="text-white text-base w-full mx-auto block border-b border-gray-500 border-solid"
    >
      <div class="relative leading-5 m-auto text-center" style="width: 1280px">
        <div class="block overflow-hidden text-white bg-black pt-1 pb-1">
          <span v-if="vmDetailData">
            {{ vmDetailData.name }}
            <el-tag
              v-if="vmDetailData"
              size="small"
              :type="filtersFun.getVmStatus(vmDetailData.phaseStatus, 'tag')"
              >{{
                filtersFun.getVmStatus(vmDetailData.phaseStatus, "status")
              }}</el-tag
            >
          </span>
          <!-- <div class="float-left text-sm">

            <el-tooltip class="box-item" effect="dark" v-if="nowIsoMsg" placement="bottom">
              <template #content> <span v-html="isoMsg"></span> </template>
<span class="mr-2">状态:{{ nowIsoMsg }}</span>
</el-tooltip>
<el-tooltip class="box-item" effect="dark" v-if="isoFile && isoFile.name" :content="isoFile.name" placement="bottom">
  <span style="width:300px;vertical-align: text-top;" class="mr-2 inline-block truncate">文件名称:{{
    isoFile.name
    }}</span>
</el-tooltip>

<el-button size="small" @click="dialogVisible = true">
  挂载
</el-button>
<el-button size="small" @click="stop_server()" class="mr-2">
  取消
</el-button>
</div> -->
          <div class="float-right w-96 text-right">
            <el-button
              v-if="$route.query.type != 'vnc' && rfb == null"
              size="small"
              class="mr-2"
              @click="connectVnc"
            >
              {{ $t("form.showRemoteDesktop") }}
            </el-button>
            <el-button
              v-if="$route.query.type != 'vnc' && rfb != null"
              size="small"
              class="mr-2"
              @click="closeVnc"
            >
              {{ $t("form.closeRemoteDesktop") }}
            </el-button>
            <el-button size="small" class="mr-2 !ml-0" @click="toReboot">
              {{ $t("form.reboot") }}
            </el-button>
            <!-- <el-dropdown v-if="vmDetailData" width="200px" class="mr-2">
              <el-button size="small">
                启动项:{{ vmDetailData.bootDev == 'hd' ? 'HD硬盘' : vmDetailData.bootDev == 'cdrom' ? 'CDROM光驱' : '-'
                }}<el-icon class="el-icon--right">
                  <i-ic-twotone-keyboard-arrow-down></i-ic-twotone-keyboard-arrow-down>
                </el-icon>
              </el-button>

              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="vmDetailData.bootDev != 'hd'">
                    <span @click="changeBootDev('hd')">切换至:HD硬盘</span>

                  </el-dropdown-item>
                  <el-dropdown-item v-if="vmDetailData.bootDev != 'cdrom'">
                    <span @click="changeBootDev('cdrom')">
                      切换至:CDROM光驱
                    </span>
                  </el-dropdown-item>

                </el-dropdown-menu>
              </template>
            </el-dropdown> -->
            <el-dropdown>
              <el-button size="small">
                {{ $t("form.combinationKey") }}
                <el-icon class="el-icon--right">
                  <i-ic-twotone-keyboard-arrow-down />
                </el-icon>
              </el-button>

              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item>
                    <span @click="rfbunlock"> Ctrl-Alt-Del </span>
                  </el-dropdown-item>
                  <!-- <el-dropdown-item @click="toFullScreen">全屏</el-dropdown-item> -->
                  <el-dropdown-item>
                    <span @click="toPaste"> {{ $t("form.paste") }} </span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
            <el-button size="small" class="ml-2 !mr-0" @click="toFullScreen">
              {{ $t("form.fullScreen") }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div id="screen" style="height: calc(100% - 41px)" />
    <el-dialog
      :visible.sync="dialogVisible"
      :title="$t('form.virtualMedia')"
      width="600px"
      :close-on-click-modal="false"
      :before-close="handleClose"
    >
      <el-row class="w-full">
        <el-col :span="24">
          <el-upload
            ref="upload"
            class="upload-demo text-center"
            :limit="1"
            :action="''"
            drag
            :on-exceed="handleExceed"
            :on-remove="handleRemove"
            :on-change="handleChange"
            :auto-upload="false"
          >
            <el-icon class="el-icon--upload"> <i-ion-cloud-upload /></el-icon>

            <div class="el-upload__text" v-html="$t('form.dragFileHere')" />

            <template #tip>
              <div class="el-upload__tip">
                {{ $t("form.onlySupportISOFormatFile") }}
              </div>
            </template>
          </el-upload>
        </el-col>
        <!-- <el-col :span="10">
           <el-input v-model="isoMsg"
                    placeholder="未连接"
                    type="textarea" maxlength="255" show-word-limit
                    :rows="8"
                    :disabled="true" />
        </el-col> -->
      </el-row>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleClose"> {{ $t("form.cancel") }} </el-button>
          <el-button type="primary" @click="start_server">
            {{ $t("form.confirm") }}
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import RFB from "@novnc/novnc/core/rfb";
import mainApi from "@/api/nextStack/mainApi";
import NBDServer from "@/utils/nbd.js";
import filtersFun from "@/utils/statusFun";

export default {
  data() {
    return {
      filtersFun: filtersFun,
      menuShow: false,
      url: "",
      XK_Shift_L: 0xffe1,
      rfb: null,
      rfbStatus: true,
      dialogVisible: false,
      upload: null,
      isoFile: null,
      isoloading: false,
      isoserver: "",
      isoMsg: "",
      nowIsoMsg: "",
      timer: null,
      vmDetailData: "",
      productShort: "NSK",
    };
  },

  created() {
    var index =
      this.$route.query.cloudId.indexOf("-") > -1
        ? this.$route.query.cloudId.indexOf("-")
        : this.$route.query.cloudId.indexOf("_");
    var productShort = this.$route.query.cloudId.substring(0, index);
    this.productShort = productShort.toUpperCase();
  },
  async mounted() {
    window.addEventListener("beforeunload", (e) => this.handleUnload(e)); // 监听浏览器关闭事件
    // 同步请求详情接口
    this.vmDetailData = await mainApi.vmsInstabcesDetail_cloud(
      this.$route.query.instanceId,
      {
        cloud_id: this.$route.query.cloudId,
        product_short: this.productShort,
      }
    );

    try {
      document.title =
        this.vmDetailData.name + " - " + this.$t("message.remoteDesktop");

      this.url = this.getUrl();
      this.timer = setInterval(async () => {
        if (this.$store.getters.hxcloudData.cloud_id) {
          this.getDetail(); // 获取详情
        }
      }, this.$store.state.nextStack.listRefreshTime);
      if (this.$route.query.type == "vnc") {
        this.connectVnc();
      }
    } catch (error) {}
  },
  // 组件销毁后
  beforeDestroy() {
    clearInterval(this.timer);
    window.removeEventListener("beforeunload", (e) => this.handleUnload(e));
  },
  // 组件销毁前
  beforeUnmount() {
    this.rfb && this.rfb.disconnect();
    // this.stop_server();
  },
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    getCookie(cname) {
      var name = cname + "=";
      var ca = document.cookie.split(";");
      for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == " ") c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
      }
      return "";
    },
    getUrl() {
      let protocol = "";

      if (window.location.protocol === "https:") {
        protocol = "wss://";
      } else {
        protocol = "ws://";
      }
      const cloudApi =
        "/proxy/" + this.productShort + "/clouds/" + this.$route.query.cloudId;

      const wsUrl =
        protocol +
        window.location.host +
        cloudApi +
        "/api/vnc/" +
        this.vmDetailData.instanceId;

      // const wsUrl =
      //   protocol +
      //   "192.168.1.250:9080" + cloudApi +
      //   "/api/vnc/" +
      //   this.vmDetailData.instanceId

      return wsUrl;
    },
    disconnectedFromServer(msg) {
      console.log("断开连接", msg);
      // clean是boolean指示终止是否干净。在发生意外终止或错误时 clean将设置为 false。
      if (msg.detail.clean) {
        // 根据 断开信息的msg.detail.clean 来判断是否可以重新连接
        setTimeout(() => {
          this.rfb = null;
          this.connectVnc();
        }, 3000);
      } else {
        // 这里做不可重新连接的一些操作
        console.log("连接不可用（可能需要重新登录）");
        this.rfb = null;
        this.rfbStatus = false;
      }
    },
    connectedToServer() {
      console.log("连接成功");
    },
    closeVnc() {
      this.rfb && this.rfb.disconnect();
      this.rfb = null;
    },
    connectVnc() {
      const PASSWORD = "";
      const rfbConnect = new RFB(document.getElementById("screen"), this.url, {
        // 向vnc 传递的一些参数，比如说实例的开机密码等
        // credentials: { password: PASSWORD, token: '1' },
      });
      rfbConnect.addEventListener("connect", this.connectedToServer);
      rfbConnect.addEventListener("disconnect", this.disconnectedFromServer);
      // scaleViewport指示是否应在本地扩展远程会话以使其适合其容器。禁用时，如果远程会话小于其容器，则它将居中，或者根据clipViewport它是否更大来处理。默认情况下禁用。
      rfbConnect.scaleViewport = true;
      // 是一个boolean指示是否每当容器改变尺寸应被发送到调整远程会话的请求。默认情况下禁用
      rfbConnect.resizeSession = true;
      rfbConnect.qualityLevel = 9;
      this.rfb = rfbConnect;
    },
    handleUnload(e) {
      // stop_server();

      e = e || window.event;
      e.returnValue = this.$t("message.confirmLeavePage");
      if (e) {
        e.returnValue = this.$t("message.confirmLeavePage");
      }
      return this.$t("message.confirmLeavePage");

      // rfb && rfb.disconnect();
    },
    rfbunlock() {
      this.rfb && this.rfb.sendCtrlAltDel();
    },
    toFullScreen() {
      const screen = document.getElementById("screen");
      // 兼容不同浏览器的全屏方法
      if (screen.requestFullscreen) {
        screen.requestFullscreen();
      } else if (screen.mozRequestFullScreen) {
        screen.mozRequestFullScreen();
      } else if (screen.webkitRequestFullscreen) {
        screen.webkitRequestFullscreen();
      } else if (screen.msRequestFullscreen) {
        screen.msRequestFullscreen();
      }
    },
    toPaste() {
      navigator.clipboard.readText().then((text) => {
        this.tttt(text.split(""));
      });
    },
    tttt(val) {
      var character = val.shift(); //

      var i = [];
      var code = character.charCodeAt();
      var needs_shift = character.match(/[A-Z!@#$%^&*()_+{}:\"<>?~|]/);

      if (needs_shift) {
        this.rfb.sendKey(this.XK_Shift_L, "ShiftLeft", true);
      }
      this.rfb.sendKey(code, character, true);
      this.rfb.sendKey(code, character, false);

      if (needs_shift) {
        this.rfb.sendKey(this.XK_Shift_L, "ShiftLeft", false);
      }

      if (val.length > 0) {
        setTimeout(() => {
          this.tttt(val);
        }, 10);
      }
    },

    toReboot() {
      this.$confirm(
        this.$t("message.confirmRebootInstance"),
        this.$t("message.reboot")
      ).then(() => {
        mainApi
          .vmsInstabcesReboot(this.$route.query.instanceId)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startReboot"),
              type: "success",
              duration: 2500,
            });
            this.getDetail(); // 获取详情
          })
          .catch((error) => {});
      });
    },
    handleClose(done) {
      this.dialogVisible = false;
      done();
    },
    stop_server() {
      this.isoloading = false;
      if (this.isoserver) {
        this.isoserver.stop();
      }
    },
    start_server() {
      if (!this.isoFile) {
        this.$notify({
          title: this.$t("message.pleaseSelectISOFile"),
          type: "warning",
          duration: 2500,
        });
        return false;
      }
      this.isoloading = true;
      this.dialogVisible = false;
      this.isoMsg = "";
      this.nowIsoMsg = "";

      var file = this.isoFile;
      let protocol = "";
      if (window.location.protocol === "https:") {
        protocol = "wss://";
      } else {
        protocol = "ws://";
      }
      const cloudApi =
        "/api/cmp/v1/" +
        this.productShort +
        "/clouds/" +
        this.$route.query.cloudId;

      const wsUrl =
        protocol +
        window.location.hostname +
        cloudApi +
        "/api/iso/" +
        this.vmDetailData.instanceId +
        "?token=" +
        this.getCookie("Access-Token");
      // const wsUrl =
      //   protocol +
      //   '192.168.1.250' + cloudApi +
      //   '/api/iso/' +
      //   this.vmDetailData.instanceId +
      //   '?token=' +
      //   this.getCookie('Access-Token');
      this.isoserver = new NBDServer(wsUrl, file);
      this.isoserver.onlog = (msg) => {
        // var container = document.getElementById("log");
        this.isoMsg += msg + "<br />";
        this.nowIsoMsg = msg;
        this.isoloading = false;
      };
      this.isoserver.start();
    },
    handleChange(file, fileList) {
      // 限制格式

      const isIso = file.name.split(".").pop() === "iso";
      if (!isIso) {
        this.$notify({
          title: this.$t("message.uploadFileFormatOnlyISO"),
          type: "error",
          duration: 2500,
        });
        // 清空上传的文件
        this.isoFile = "";
        this.upload.clearFiles();
        return false;
      }
      this.isoFile = file.raw;
    },
    handleExceed(files) {
      this.$notify({
        title: this.$t("message.maxUploadFile"),
        type: "warning",
        duration: 2500,
      });
    },
    handleRemove(file, fileList) {
      this.isoFile = "";
    },
    getDetail() {
      // 获取详情
      mainApi
        .vmsInstabcesDetail_cloud(this.$route.query.instanceId, {
          cloud_id: this.$route.query.cloudId,
          product_short: this.productShort,
        })
        .then((res) => {
          this.vmDetailData = res;
          if (!this.rfbStatus && [6, 10, 29, 40].includes(res.phaseStatus)) {
            this.rfbStatus = true;
            this.connectVnc();
          }
        })
        .catch((error) => {});
    },
    changeBootDev(val) {
      // 改变启动盘

      var data = {
        // name: form.value.name,
        // description: form.value.description,
        // flavorId: form.value.flavorId,
        bootDev: val,
      };
      mainApi
        .vmsInstabcesEdit(data, this.$route.query.instanceId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.startSwitch"),
            type: "success",
            duration: 2500,
          });
          this.getDetail(); // 获取详情
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.commandPage {
}
</style>
