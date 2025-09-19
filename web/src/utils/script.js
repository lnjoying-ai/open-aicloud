import { Notification } from "element-ui";
import i18n from "@/utils/i18n/index.js";

export default {
  validateName(rule, value, callback) {
    // 验证名称
    if (value === "") {
      callback(new Error(i18n.t("form.pleaseInputContent")));
    } else {
      if (value.length > 64) {
        callback(new Error(i18n.t("message.pleaseInput164")));
      } else if (!/^[a-zA-Z0-9\u4e00-\u9fa5\_\-\.\@]{0,}$/.test(value)) {
        callback(new Error(i18n.t("message.usernameFormat")));
      } else {
        callback();
      }
    }
  },
  // B kib mib gib 转换
  formatSizeB(size) {
    if (size == "" || size == null) {
      return "";
    }
    if (size < 1024) {
      return size + "B";
    } else if (size < 1024 * 1024) {
      return processing_numbers(size / 1024) + "KB";
    } else if (size < 1024 * 1024 * 1024) {
      return processing_numbers(size / 1024) + "MB";
    } else {
      return processing_numbers(size / 1024 / 1024 / 1024) + "GB";
    }
  },
  // mib gib 转换
  formatSizeMib(size) {
    if (size == "" || size == null) {
      return "";
    }
    if (size < 1024) {
      return size + "MB";
    } else {
      return processing_numbers(size / 1024) + "GB";
    }
  },
  createRandomStr(len, type) {
    // 生成随机字母数字 长度/类型
    len = len || 32;
    var chars = "";
    if (type.indexOf("A") > -1) {
      chars += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    }
    if (type.indexOf("a") > -1) {
      chars += "abcdefghijklmnopqrstuvwxyz";
    }
    if (type.indexOf("0") > -1) {
      chars += "0123456789";
    }
    var pwd = "";
    for (var i = 0; i < len; i++) {
      pwd += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return pwd;
  },
  parseIntIpNum(num) {
    // 格式化ip数字
    var data = parseInt(num);
    if (data > 255) {
      data = 255;
    }
    return data || 0;
  },
  copyText(text) {
    // 复制文本
    const textarea = document.createElement("textarea");
    textarea.value = text;
    document.body.appendChild(textarea);
    textarea.select();
    const isCopy = document.execCommand("copy"); // isCopy可以表示是否调用execCommand()成功
    document.body.removeChild(textarea);
    Notification({
      type: "success",
      title: "复制成功",
      duration: 2500,
    });
    // var input = document.createElement("input");
    // input.setAttribute("readonly", "readonly");
    // input.setAttribute("value", text);
    // document.body.appendChild(input);
    // input.select();
    // input.setSelectionRange(0, 9999); // 选中文本
    // if (document.execCommand("copy")) {
    //   document.execCommand("copy");
    //   Message.success("复制成功");
    // }
  },
  jkeconfigServices(val) {
    var jke_configMain = JSON.parse(JSON.stringify(val));
    if (val.services) {
      jke_configMain.services.kube_apiserver.extra_args = {};
      jke_configMain.services.kube_controller.extra_args = {};
      jke_configMain.services.kubelet.extra_args = {};
      jke_configMain.services.kube_scheduler.extra_args = {};
      jke_configMain.services.kube_proxy.extra_args = {};
      if (
        val.services.kube_apiserver.extra_args.length > 0 &&
        val.services.kube_apiserver.extra_args[0].key != "" &&
        val.services.kube_apiserver.extra_args[0].value != ""
      ) {
        val.services.kube_apiserver.extra_args.forEach((item) => {
          if (item.key != "" && item.value != "") {
            jke_configMain.services.kube_apiserver.extra_args[item.key] =
              item.value;
          }
        });
      } // kube_apiserver 额外参数
      if (
        val.services.kube_controller.extra_args.length > 0 &&
        val.services.kube_controller.extra_args[0].key != "" &&
        val.services.kube_controller.extra_args[0].value != ""
      ) {
        val.services.kube_controller.extra_args.forEach((item) => {
          if (item.key != "" && item.value != "") {
            jke_configMain.services.kube_controller.extra_args[item.key] =
              item.value;
          }
        });
      } // kube_controller 额外参数
      if (
        val.services.kubelet.extra_args.length > 0 &&
        val.services.kubelet.extra_args[0].key != "" &&
        val.services.kubelet.extra_args[0].value != ""
      ) {
        val.services.kubelet.extra_args.forEach((item) => {
          if (item.key != "" && item.value != "") {
            jke_configMain.services.kubelet.extra_args[item.key] = item.value;
          }
        });
      } // kubelet 额外参数
      if (
        val.services.kube_scheduler.extra_args.length > 0 &&
        val.services.kube_scheduler.extra_args[0].key != "" &&
        val.services.kube_scheduler.extra_args[0].value != ""
      ) {
        val.services.kube_scheduler.extra_args.forEach((item) => {
          if (item.key != "" && item.value != "") {
            jke_configMain.services.kube_scheduler.extra_args[item.key] =
              item.value;
          }
        });
      } // kube_scheduler 额外参数
      if (
        val.services.kube_proxy.extra_args.length > 0 &&
        val.services.kube_proxy.extra_args[0].key != "" &&
        val.services.kube_proxy.extra_args[0].value != ""
      ) {
        val.services.kube_proxy.extra_args.forEach((item) => {
          if (item.key != "" && item.value != "") {
            jke_configMain.services.kube_proxy.extra_args[item.key] =
              item.value;
          }
        });
      } // kube_proxy 额外参数
    }
    return jke_configMain;
  },
  jkeconfigServicesCustom(data) {
    var jke_configMain = JSON.parse(JSON.stringify(data));

    if (data.services) {
      jke_configMain.services.kube_apiserver.extra_args = [];
      jke_configMain.services.kube_controller.extra_args = [];
      jke_configMain.services.kubelet.extra_args = [];
      jke_configMain.services.kube_scheduler.extra_args = [];
      jke_configMain.services.kube_proxy.extra_args = [];

      var kube_apiserverArgs = data.services.kube_apiserver.extra_args;
      var kube_controllerArgs = data.services.kube_controller.extra_args;
      var kubeletArgs = data.services.kubelet.extra_args;
      var kube_schedulerArgs = data.services.kube_scheduler.extra_args;
      var kube_proxyArgs = data.services.kube_proxy.extra_args;

      if (kube_apiserverArgs) {
        // 循环参数
        Object.keys(kube_apiserverArgs).forEach((item) => {
          jke_configMain.services.kube_apiserver.extra_args.push({
            key: item,
            value: kube_apiserverArgs[item],
          });
        });
      }
      if (kube_controllerArgs) {
        // 循环参数
        Object.keys(kube_controllerArgs).forEach((item) => {
          jke_configMain.services.kube_controller.extra_args.push({
            key: item,
            value: kube_controllerArgs[item],
          });
        });
      }
      if (kubeletArgs) {
        // 循环参数
        Object.keys(kubeletArgs).forEach((item) => {
          jke_configMain.services.kubelet.extra_args.push({
            key: item,
            value: kubeletArgs[item],
          });
        });
      }
      if (kube_schedulerArgs) {
        // 循环参数
        Object.keys(kube_schedulerArgs).forEach((item) => {
          jke_configMain.services.kube_scheduler.extra_args.push({
            key: item,
            value: kube_schedulerArgs[item],
          });
        });
      }
      if (kube_proxyArgs) {
        // 循环参数
        Object.keys(kube_proxyArgs).forEach((item) => {
          jke_configMain.services.kube_proxy.extra_args.push({
            key: item,
            value: kube_proxyArgs[item],
          });
        });
      }
    }
    return jke_configMain;
  },
  getICMP() {
    var data = [
      // ICMP
      {
        name: "全部",
        value: "0",
      },
      {
        name: "Echo",
        value: "1",
      },
      {
        name: "Echo reply",
        value: "2",
      },
      {
        name: "Fragment need DF set",
        value: "3",
      },
      {
        name: "Host redirect",
        value: "4",
      },
      {
        name: "Host TOS redirect",
        value: "5",
      },
      {
        name: "Host unreachable",
        value: "6",
      },
      {
        name: "Information reply",
        value: "7",
      },
      {
        name: "Information request",
        value: "8",
      },
      {
        name: "Net redirect",
        value: "9",
      },
      {
        name: "Net TOS redirect",
        value: "10",
      },
      {
        name: "Net unreachable",
        value: "11",
      },
      {
        name: "Parameter problem",
        value: "12",
      },
      {
        name: "Port unreachable",
        value: "13",
      },
      {
        name: "Protocol unreachable",
        value: "14",
      },
      {
        name: "Reassembly timeout",
        value: "15",
      },
      {
        name: "Source quench",
        value: "16",
      },
      {
        name: "Source route failed",
        value: "17",
      },
      {
        name: "Timestamp reply",
        value: "18",
      },
      {
        name: "Timestamp request",
        value: "19",
      },
      {
        name: "TTL exceeded",
        value: "20",
      },
    ];
    return data;
  },
  // Math.floor(a*100)/100 处理金额文件大小
  processing_numbers(num) {
    return Math.floor(num * 100) / 100;
  },
};
