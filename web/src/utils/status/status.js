import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("statusAndType.creating"), // ADDING
  1: i18n.t("statusAndType.running"), // ADDED
  2: i18n.t("statusAndType.createFailed"), // ADD_FAILED
  3: i18n.t("statusAndType.deleting"), // DELETING
  4: i18n.t("statusAndType.deleted"), // DELETED
  5: i18n.t("statusAndType.deleteFailed"), // DELETE_FAILED
  6: i18n.t("statusAndType.creating"), // AGENT_ADDING
  7: i18n.t("statusAndType.createFailed"), // AGENT_ADDING_ERR
  8: i18n.t("statusAndType.deleting"), // AGENT_DELETING
  9: i18n.t("statusAndType.deleteFailed"), // AGENT_DELETING_ERR
  10: "-", // ARPING
  11: "-", // ARPING_DONE
  12: "-", // ARPING_ERR
  13: "-", // ARPING_OK
};
const codeMessageTagMap = {
  0: "", // ADDING 创建中
  1: "success", // ADDED 运行中
  2: "danger", // ADD_FAILED 创建失败
  3: "warning", // DELETING 删除中
  4: "danger", // DELETED 已删除
  5: "danger", // DELETE_FAILED 删除失败
  6: "", // AGENT_ADDING 创建中
  7: "danger", // AGENT_ADDING_ERR 创建失败
  8: "warning", // AGENT_DELETING 删除中
  9: "danger", // AGENT_DELETING_ERR 删除失败
  10: "-", // ARPING
  11: "-", // ARPING_DONE
  12: "-", // ARPING_ERR
  13: "-", // ARPING_OK
};
const showStatusMessage = (code, type) => {
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

export default showStatusMessage;
