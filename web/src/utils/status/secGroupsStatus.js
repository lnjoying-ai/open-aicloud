import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("statusAndType.creating"), // ADDING
  1: i18n.t("statusAndType.available"), // ADDED
  2: i18n.t("statusAndType.createFailed"), // ADD_FAILED
  3: i18n.t("statusAndType.deleting"), // DELETING
  5: i18n.t("statusAndType.deleteFailed"), // DELETE_FAILED
  6: i18n.t("statusAndType.creating"), // AGENT_ADDING
  8: i18n.t("statusAndType.deleting"), // DELETING
  41: i18n.t("statusAndType.updating"), // UPDATING
  42: i18n.t("statusAndType.available"), // UPDATED
  43: i18n.t("statusAndType.updating"), // UPDATEING
  45: i18n.t("statusAndType.updateFailed"), // UPDATE_FAILED
};
const codeMessageTagMap = {
  0: "", // ADDING  创建中
  1: "success", // ADDED  可用
  2: "danger", // ADD_FAILED  创建失败
  3: "warning", // DELETING  删除中
  5: "danger", // DELETE_FAILED  删除失败
  6: "", // AGENT_ADDING  创建中
  8: "warning", // DELETING  删除中
  41: "", // UPDATING  更新中
  42: "success", // UPDATED  可用
  43: "", // UPDATEING  更新中
  45: "danger", // UPDATE_FAILED  更新失败
};
const showSecGroupsStatusMessage = (code, type) => {
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

export default showSecGroupsStatusMessage;
