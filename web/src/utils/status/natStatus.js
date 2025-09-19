import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  1: i18n.t("statusAndType.deleteFailed"), // AGENT_UNMAPPING_ERR
  2: i18n.t("statusAndType.deleting"), // AGENT_UNMAPPING
  3: i18n.t("statusAndType.deleting"), // UNMAPPING
  4: i18n.t("statusAndType.creating"), // MAPPING
  5: i18n.t("statusAndType.creating"), // AGENT_MAPPING
  6: i18n.t("statusAndType.createFailed"), // AGENT_MAPPING_ERR
  7: i18n.t("statusAndType.running"), // MAPPED
  70: i18n.t("statusAndType.creating"), // PORT_CREATING
};
const codeMessageTagMap = {
  1: "danger", // AGENT_UNMAPPING_ERR 删除失败
  2: "warning", // AGENT_UNMAPPING 删除中
  3: "warning", // UNMAPPING 删除中
  4: "", // MAPPING 创建中
  5: "", // AGENT_MAPPING 创建中
  6: "danger", // AGENT_MAPPING_ERR 创建失败
  7: "success", // MAPPED 运行中
  70: "", // PORT_CREATING 创建中
};
const showNatStatusMessage = (code, type) => {
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

export default showNatStatusMessage;
