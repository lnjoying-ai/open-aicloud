<template>
  <div>
    <el-card class="box-card">
      <div slot="header">
        <span>{{ this.$route.params.name }}</span>
        <!--
        <el-button
          style="float: right; padding: 3px 0"
          icon="el-icon-close"
          @click="deleteStack"
        ></el-button> -->
      </div>
      <span>
        <div v-if="commandUrl">
          <div>
            <iframe
              ref="iframe"
              style="
                width: 100vw;
                min-width: 1200px;
                height: calc(100vh - 63px);
              "
              :src="commandUrl"
              frameborder="0"
              scrolling="auto"
            />
          </div>
        </div>
        <span v-else>
          <div
            ref="journal"
            class="journalStyle"
            style="font-size: 16px; overflow: auto"
            v-html="cmdValue == '' ? '连接中……' : cmdValue"
          />
        </span>
      </span>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import axios from "axios";
import { nodesStacks, deleteStacks, getDeleteStacksUrl } from "@/api/mainApi";
export default {
  components: {},
  mixins: [],
  data() {
    return {
      stack_id: "",
      timer: null,
      commandUrl: "",
      cmdValue: "",
      _beforeUnload_time: "",
      _gap_time: "",
    };
  },
  watch: {},
  // 解除窗口关闭[监听]事件
  destroyed() {
    window.removeEventListener("beforeunload", (e) =>
      this.beforeunloadHandler(e)
    );
    window.removeEventListener("unload", (e) => this.unloadHandler(e));
  },
  mounted() {
    window.addEventListener("beforeunload", (e) => this.beforeunloadHandler(e));
    window.addEventListener("unload", (e) => this.unloadHandler(e));

    this.getStack();
  },
  created() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    beforeunloadHandler(e) {
      this._beforeUnload_time = new Date().getTime();
    },
    unloadHandler(e) {
      this._gap_time = new Date().getTime() - this._beforeUnload_time;
      // 判断是窗口关闭还是刷新
      if (this._gap_time <= 5) {
        // 如果是登录状态，关闭窗口前，移除用户
        localStorage.setItem("ttyStatus", this.$route.params.id);
      }
    },
    verificationUrl(url) {
      axios
        .get(url)
        .then((res) => {
          this.commandUrl = url;
        })
        .catch((error) => {
          this.cmdValue = this.$t("form.connecting");
          this.commandUrl = "";
        });
    },
    // 查询节点上的应用
    async getStack() {
      await nodesStacks(this.$route.params.id)
        .then((res) => {
          if (res.status.code == "1100") {
            this.cmdValue = this.$t("form.componentAbnormal");
          } else if (res.status.code == "1102") {
            this.cmdValue = this.$t("form.initializationFailed");
          } else {
            this.cmdValue = res.status.desc.cn + "...";
          }

          if (res.status.code == "1101") {
            this.refrehStatus();
          } else if (res.status.code == "1103") {
            if (
              res.extra_info.url != undefined &&
              res.extra_info.url != "" &&
              res.extra_info.url != null
            ) {
              this.verificationUrl(res.extra_info.url);
            }
            this.refrehStatus();
          } else {
            this.clear();
          }
        })
        .catch((err) => {});
    },
    refrehStatus() {
      // 计时器正在进行中，退出函数
      if (this.timer != null) {
        return;
      }
      // 计时器为空，操作
      this.timer = setInterval(() => {
        this.getStack(); // 加载数据函数
      }, 10000);
    },
    // 停止定时器
    clear() {
      clearInterval(this.timer); // 清除计时器
      this.timer = null; // 设置为null
    },
    // 删除节点上应用
    async deleteStack() {
      if (this.$route.params.id != undefined && this.$route.params.id != "") {
        await deleteStacks(this.$route.params.id)
          .then((res) => {
            this.$notify({
              title: this.$t("message.closeSuccess"),
              type: "success",
              duration: 2500,
            });
          })
          .catch((err) => {});
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.journalStyle {
  padding: 10px;
  outline: none;
  background-color: rgb(0, 0, 0);
  color: rgb(240, 240, 240);
  height: calc(100vh - 63px);
  overflow: auto;
  white-space: pre-line;
}

.el-card {
  ::v-deep .el-card__header {
    text-align: center;
    background-color: #000;
    color: #fff;
    border-color: #333;
  }

  ::v-deep .el-card__body {
    padding: 0;
  }
}
</style>
