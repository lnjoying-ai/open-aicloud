import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  80: i18n.t("statusAndType.attaching"), // DEVICE_ATTACHING
  81: i18n.t("statusAndType.attaching"), // DEVICE_AGENT_ATTACHING
  82: i18n.t("statusAndType.detaching"), // DEVICE_DETACHING
  83: i18n.t("statusAndType.detaching"), // DEVICE_AGENT_DETACHING
  84: i18n.t("statusAndType.attachingFailed"), // DEVICE_ATTACH_FAILED
  85: i18n.t("statusAndType.detachingFailed"), // DEVICE_DETACH_FAILED
  86: i18n.t("statusAndType.attached"), // DEVICE_ATTACHED
  87: i18n.t("statusAndType.unMounted"), // DEVICE_DETACHED
  88: i18n.t("statusAndType.attaching"), // DEVICE_INIT_CREATE
};
const codeMessageTagMap = {
  80: "", // DEVICE_ATTACHING  挂载中
  81: "", // DEVICE_AGENT_ATTACHING 挂载中
  82: "warning", // DEVICE_DETACHING 卸载中
  83: "warning", // DEVICE_AGENT_DETACHING 卸载中
  84: "danger", // DEVICE_ATTACH_FAILED 挂载失败
  85: "danger", // DEVICE_DETACH_FAILED 卸载失败
  86: "success", // DEVICE_ATTACHED 已挂载
  87: "", // DEVICE_DETACHED 未挂载
  88: "", // DEVICE_INIT_CREATE 创建中
};
const showPciStatusMessage = (code, type) => {
  if (type == "tag") {
    return codeMessageTagMap[JSON.stringify(code)] != undefined
      ? codeMessageTagMap[JSON.stringify(code)]
      : "info";
  } else {
    return (
      codeMessageMap[JSON.stringify(code)] ||
      i18n.t("statusAndType.unknownStatus")
    );
  }
};

export default showPciStatusMessage;
