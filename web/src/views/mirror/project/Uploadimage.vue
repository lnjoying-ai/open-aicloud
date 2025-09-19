<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    width="500px"
    :close-on-click-modal="false"
    :title="$t('form.uploadImage')"
    @closed="cancel"
  >
    <el-form
      ref="form"
      :model="form"
      size="small"
      label-width="120px"
      label-position="top"
    >
      <el-form-item v-if="msg1" :label="$t('form.loginInstruction') + ':'">
        <div class="codeboxall">
          <div class="mr-2">
            <p class="codebox">{{ msg1 }}</p>
          </div>
          <i
            class="el-icon-document-copy"
            style="font-size: 17px; cursor: pointer"
            @click="copy($event, msg1)"
          />
        </div>
      </el-form-item>
      <el-form-item v-if="msg2" :label="$t('form.pullInstruction') + ':'">
        <div class="codeboxall">
          <div class="mr-2">
            <p class="codebox">{{ msg2 }}</p>
          </div>
          <i
            class="el-icon-document-copy"
            style="font-size: 17px; cursor: pointer"
            @click="copy($event, msg2)"
          />
        </div>
      </el-form-item>
      <el-form-item v-if="msg3" :label="$t('form.pushInstruction') + ':'">
        <div class="codeboxall">
          <div class="mr-2">
            <p class="codebox">{{ msg3 }}</p>
          </div>
          <i
            class="el-icon-document-copy"
            style="font-size: 17px; cursor: pointer"
            @click="copy($event, msg3)"
          />
        </div>
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" size="small" @click="cancel">{{
        $t("form.cancel")
      }}</el-button>
      <el-button
        :loading="loading"
        type="primary"
        size="small"
        @click="cancel"
        >{{ $t("form.confirm") }}</el-button
      >
    </div>
  </el-dialog>
</template>

<script>
import { getcommand } from "@/api/mainApi";
import Clipboard from "clipboard";
export default {
  props: {
    registry_id: {
      type: String,
      default: null,
    },
  },
  data() {
    return {
      loading: false,
      dialog: false,
      form: {},
      msg1: "",
      msg2: "",
      msg3: "",
    };
  },
  methods: {
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          title: this.$t("message.copySuccess"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.on("error", () => {
        // 不支持复制

        this.$notify({
          title: this.$t("message.copyFailed"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
    onOpen() {
      // console.log("触发了几次");
      this.dialog = true;
      this.commandInit(this.registry_id);
    },
    cancel() {
      this.dialog = false;
    },
    async commandInit(registry_id) {
      getcommand({ registry_id: registry_id, command_type: 0 }).then((res) => {
        this.msg1 = res;
      });
      getcommand({ registry_id: registry_id, command_type: 1 }).then((res) => {
        this.msg2 = res;
      });
      getcommand({ registry_id: registry_id, command_type: 2 }).then((res) => {
        this.msg3 = res;
      });
    },
  },
};
</script>

<style scoped lng="scss">
.codeboxall {
  display: flex;
  align-items: center;
}
.codebox {
  padding-left: 5px;
  padding-right: 5px;
  width: 100%;
  height: 100%;
  background: rgb(202, 207, 216);
  border-radius: 4px;
}
::v-deep .el-form--label-top .el-form-item__label {
  padding: 0px;
}
</style>
