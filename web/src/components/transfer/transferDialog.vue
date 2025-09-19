<template>
  <div class="transferDialog">
    <!-- 启动下载传输插件 -->
    <el-dialog
      :visible.sync="$store.state.transfer.transferDialogStatus"
      :title="$t('transfer.tip')"
      width="620px"
      :close-on-click-modal="false"
    >
      <div v-if="downloadLinkData" class="pingModel">
        <div
          v-if="
            $store.state.transfer.windowstatus ||
            $store.state.transfer.macstatus
          "
        >
          <div class="titleSmall">
            {{ $t("transfer.transferService") }}
            <span>
              <span v-if="$store.state.transfer.windowstatus">
                <a
                  v-if="downloadLinkData.winx64.hidden"
                  :href="'./download/' + downloadLinkData.winx64.link"
                  :download="downloadLinkData.winx64.name"
                  class="winLink"
                  >{{ $t("transfer.download") }}</a
                >
              </span>
              <span v-if="$store.state.transfer.macstatus">
                <a
                  v-if="downloadLinkData.mac.hidden"
                  :href="'./download/' + downloadLinkData.mac.link"
                  :download="downloadLinkData.mac.name"
                  class="macLink"
                >
                  {{ $t("transfer.download") }}
                </a>
              </span>
            </span>
            {{ $t("transfer.clientPlugin") }}
          </div>
          <div class="prompt">
            <p>{{ $t("transfer.supportWindows") }}</p>
            <p>
              {{ $t("transfer.linuxClient") }}
              <span>
                <a
                  href="./EdgeTurbo客户端（linux版）使用指南.pdf"
                  download="EdgeTurbo客户端（linux版）使用指南.pdf"
                >
                  {{ $t("transfer.getClient") }}
                </a>
              </span>
              {{ $t("transfer.linuxManual") }}
            </p>
          </div>
          <div class="existing">
            <div>{{ $t("transfer.installedPlugin") }}:</div>
            <div>
              <span class="winLink cursor-pointer" @click="startClient">{{
                $t("transfer.startClient")
              }}</span>
            </div>
            <div class="startprompt">
              {{ $t("transfer.refreshPage") }}
            </div>
          </div>
          <div class="kindreminder mt-5">
            <div style="width: 20%">{{ $t("transfer.kindReminder") }}:</div>
            <div style="width: 80%">
              <div class="step">
                <p class="circular" />
                <p>{{ $t("transfer.step1") }}:</p>
                <p class="stepline" />
                <p class="circular" />
                <p>{{ $t("transfer.step2") }}:</p>
              </div>
              <div class="steptitle">
                <p class="stepfirst">{{ $t("transfer.stepfirst") }}</p>
                <p class="stepsecond">{{ $t("transfer.stepsecond") }}</p>
              </div>
            </div>
          </div>
        </div>
        <div v-else>
          <div class="titleSmall">
            {{ $t("transfer.transferServiceManual") }}
          </div>
          <div class="prompt">
            <p>
              {{ $t("transfer.linuxClient") }}
              <span>
                <a
                  href="./EdgeTurbo客户端（linux版）使用指南.pdf"
                  download="EdgeTurbo产品文档.pdf"
                >
                  {{ $t("transfer.getClient") }}
                </a>
              </span>
              {{ $t("transfer.linuxManual") }}
            </p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { mapGetters } from "vuex";

import { getDownloadJson } from "@/api/transfer/mainApi";
export default {
  props: {
    lnjoyingToken: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      downloadLinkData: "",
    };
  },
  watch: {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  mounted() {
    this.getDownload();
  },
  created() {},
  methods: {
    startClient() {
      var userName = this.userInfo.name;
      var token = "HappySRending" + this.lnjoyingToken;
      var location = window.location.origin;

      var hrefUrl =
        "EdgeTurbo://?userName=" +
        userName +
        "&token=" +
        token +
        "&cloudAddress=" +
        location +
        "&cloudName=lnjoying" +
        "&userAgent=" +
        navigator.userAgent;
      // 创建 a 标签
      const a = document.createElement("a");
      // 设置 a 标签的 href 属性
      a.href = hrefUrl;
      // 触发 a 标签的点击事件
      a.click();
      // 销毁 a 标签
      a.remove();
      // 关闭弹窗
      this.$store.state.transfer.transferDialogStatus = false;
    },
    getDownload() {
      getDownloadJson()
        .then((res) => {
          this.downloadLinkData = res;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.kindreminder {
  border: 1px dashed rgb(187, 187, 187);
  padding: 20px;
  display: flex;
  width: 80%;
  margin: 0 auto;
  margin-top: 10px;
  color: grey;

  .step {
    display: flex;
    align-items: center;

    .circular {
      margin-right: 3px;
      width: 10px;
      height: 10px;
      background: rgb(9, 109, 217);
      border-radius: 50%;
    }

    .stepline {
      margin-left: 5px;
      margin-right: 5px;
      width: 135px;
      height: 1px;
      background: rgb(187, 187, 187);
    }
  }

  .steptitle {
    margin-top: 3px;
    display: flex;
    font-size: 12px;

    .stepfirst {
      width: 180px;
      margin-right: 60px;
    }

    .stepsecond {
      width: 180px;
    }
  }
}

.pingModel {
  padding: 30px;
  padding-top: 0px;
  color: black;

  .titleSmall {
    font-size: 15px;

    a {
      text-decoration: none;
      color: #096dd9;
    }
  }

  .prompt {
    margin-top: 20px;
    font-size: 12px;
    color: grey;

    a {
      text-decoration: none;
      color: #096dd9;
    }
  }

  .existing {
    margin-top: 20px;
    font-size: 15px;

    .winLink {
      font-size: 14px;
      font-weight: 600;
      height: 32px;
      line-height: 32px;
      width: 80%;
      text-align: center;
      display: table;
      color: rgb(255, 255, 255);
      background-color: #096dd9;
      border-radius: 4px;
      margin: 10px auto 10px;
      text-decoration: none;
    }

    .startprompt {
      color: grey;
      font-size: 13px;
      width: 80%;
      margin: 0 auto;
    }
  }
}
</style>
