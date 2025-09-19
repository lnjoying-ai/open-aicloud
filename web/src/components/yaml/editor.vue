<template>
  <div class="yaml-editor" @mouseenter="handleEnter" @mouseleave="handleLeave">
    <transition name="el-fade-in-linear">
      <div v-show="showBtn" class="yamlEditorBtn">
        <el-button
          type="primary"
          size="mini"
          style="
            background-color: rgba(105, 105, 105, 0.7);
            border-color: #ffffff;
          "
          @click="copy($event, getValue())"
          >{{ $t("domain.copy") }}</el-button
        >
        <el-button
          type="primary"
          size="mini"
          style="
            background-color: rgba(105, 105, 105, 0.7);
            border-color: #ffffff;
          "
          @click="saveTxt(getValue())"
          >{{ $t("domain.download") }}</el-button
        >
      </div>
    </transition>
    <div>
      <textarea ref="textarea" />
    </div>
  </div>
</template>

<script>
import Clipboard from "clipboard";
import CodeMirror from "codemirror";
import "codemirror/addon/lint/lint.css";
import "codemirror/lib/codemirror.css";
import "codemirror/theme/monokai.css";
import "codemirror/mode/yaml/yaml";
import "codemirror/addon/lint/lint";
import "codemirror/addon/lint/yaml-lint";
window.jsyaml = require("js-yaml"); // 引入js-yaml为codemirror提高语法检查核心支持

export default {
  name: "YamlEditor",
  // eslint-disable-next-line vue/require-prop-types
  props: {
    value: {
      type: String,
      default: () => "",
    },
    readonly: {
      type: String,
      default: "",
    },
    lint: {
      type: Boolean,
      default: true,
    },
    downloadName: {
      type: String,
      default: Date.parse(new Date()),
    },
    downloadType: {
      type: String,
      default: "yml",
    },
    placeholder: {
      type: String,
      default: "",
    },
    isAdd: {
      type: Boolean,
      default: true,
    },
  },
  data() {
    return {
      nowCm: "",
      showBtn: false,
      yamlEditor: false,
    };
  },
  watch: {
    value(value) {
      const editorValue = this.yamlEditor.getValue();
      if (value !== editorValue) {
        this.yamlEditor.setValue(this.value);
      }
    },
    nowCm(value) {
      if (value) {
        setTimeout(() => {
          if (value.state.lint.marked.length > 0) {
            this.$emit("changeLint", true);
          } else {
            this.$emit("changeLint", false);
          }
        }, 1000);
      }
    },
  },
  mounted() {
    this.yamlEditor = CodeMirror.fromTextArea(this.$refs.textarea, {
      lineNumbers: true, // 显示行号
      mode: "text/x-yaml", // 语法model
      gutters: ["CodeMirror-lint-markers"], // 语法检查器
      theme: "monokai", // 编辑器主题
      lint: this.lint, // 开启语法检查
      readOnly: this.readonly, // nocursor, // 只读
    });
    this.yamlEditor.on("change", (cm) => {
      // 判断是否有报错
      this.nowCm = "";
      this.nowCm = cm;
      this.$emit("changed", cm.getValue());
      this.$emit("input", cm.getValue());
    });
    this.yamlEditor.setValue(this.value);
    if (!this.value && this.placeholder) {
      console.log("----");
      this.yamlEditor.setValue(this.placeholder);
    }
  },
  methods: {
    // 判断是否有值
    handleLeave() {
      this.showBtn = false;
    },
    handleEnter() {
      this.showBtn = true;
    },
    saveTxt(value) {
      var blob = new Blob([value], { type: "text/plain" });
      var a = document.createElement("a");
      a.download = this.downloadName + "." + this.downloadType;
      a.href = URL.createObjectURL(blob);
      a.dataset.downloadurl = ["text/plain", a.download, a.href].join(":");
      a.style.display = "none";
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      setTimeout(function () {
        URL.revokeObjectURL(a.href);
      }, 1500);
    },
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          type: "success",
          title: this.$t("message.copySuccess"),
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
          type: "success",
          title: this.$t("message.copyError"),
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
    toRefresh() {
      setTimeout(() => {
        this.yamlEditor.setValue(this.value);
      }, 100); // 让编辑器每次在调用的时候进行自动刷新
    },
    getValue() {
      return this.yamlEditor.getValue();
    },
  },
};
</script>

<style scoped>
::v-deep .el-textarea__inner {
  background: #272822;
  color: #f8f8f2;
  min-height: 300px !important;
  border-radius: 4px;
}

.yaml-editor {
  height: 100%;
  position: relative;
}

.yaml-editor >>> .CodeMirror-vscrollbar {
  display: none !important;
}

.yamlEditorBtn {
  position: absolute;
  top: 5px;
  right: 5px;
  z-index: 9;
}

.yaml-editor >>> .CodeMirror {
  height: auto;
  min-height: 300px;
  border-radius: 4px;
}

.yaml-editor >>> .CodeMirror-scroll {
  min-height: 300px;
}

.yaml-editor >>> .cm-s-rubyblue span.cm-string {
  color: #f08047;
}
</style>
