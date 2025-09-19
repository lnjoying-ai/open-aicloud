<template>
  <div class="fileIcon">
    <img :src="getSvg(getFileType(fileName))" />
  </div>
</template>
<script>
import fileMain from "@/assets/svg/file.svg";
import imgMain from "@/assets/svg/img.svg";
import linkMain from "@/assets/svg/link.svg";
import mp3Main from "@/assets/svg/mp3.svg";
import mp4Main from "@/assets/svg/mp4.svg";
import noneMain from "@/assets/svg/none.svg";
import pdfMain from "@/assets/svg/pdf.svg";
import pptMain from "@/assets/svg/ppt.svg";
import txtMain from "@/assets/svg/txt.svg";
import wordMain from "@/assets/svg/word.svg";
import xlsMain from "@/assets/svg/xls.svg";
import zipMain from "@/assets/svg/zip.svg";
export default {
  props: {
    // 成功之后的函数
    fileName: {
      type: String,
      default: "",
    },
    fileType: {
      type: Number,
    },
  },
  data() {
    return {
      fileMain: fileMain,
      imgMain: imgMain,
      linkMain: linkMain,
      mp3Main: mp3Main,
      mp4Main: mp4Main,
      noneMain: noneMain,
      pptMain: pptMain,
      pdfMain: pdfMain,
      txtMain: txtMain,
      wordMain: wordMain,
      xlsMain: xlsMain,
      zipMain: zipMain,
      svgLink: "",
      fileIconName: "",
    };
  },

  methods: {
    getSvg(type) {
      if (this.fileType == 1) {
        return this.fileMain;
      } else {
        if (["ppt", "pptx"].includes(type)) {
          return this.pptMain;
        }
        if (["txt", "json"].includes(type)) {
          return this.txtMain;
        }

        if (["xls", "xlsx", "xml"].includes(type)) {
          return this.xlsMain;
        }
        if (["mp4", "avi"].includes(type)) {
          return this.mp4Main;
        }
        if (["jpg", "jpeg", "png", "gif"].includes(type)) {
          return this.imgMain;
        }
        if (["zip", "rar", "tar", "tgz"].includes(type)) {
          return this.zipMain;
        }
        if (["htm", "html"].includes(type)) {
          return this.linkMain;
        }
        if (["pdf"].includes(type)) {
          return this.pdfMain;
        }
        if (["doc", "docx"].includes(type)) {
          return this.wordMain;
        }
        if (["mp3"].includes(type)) {
          return this.mp3Main;
        }
        return this.noneMain;
      }
    },
    // 根据文件名获取文件类型
    getFileType(fileName) {
      let fileType = "";
      let fileNameArr = fileName.split(".");
      let fileNameArrLength = fileNameArr.length;
      if (fileNameArrLength > 1) {
        fileType = fileNameArr[fileNameArrLength - 1];
      }
      return fileType;
    },
  },
  mounted() {},
};
</script>
<style lang="stylus" scoped>
.fileIcon {
  display: inline-block;
  height: 100%;

  img {
    width: 100%;
    max-height: 100%;
    vertical-align: middle;
  }
}
</style>
