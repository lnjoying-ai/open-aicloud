import i18n from "@/utils/i18n/index.js";

export default {
  getStatus(code) {
    return (
      codeMessageMap[JSON.stringify(code)] ||
      i18n.t("statusAndType.unknownStatus")
    );
  },
  getNodeUpgradeStatus(code) {
    return (
      NodeUpgradeStatusCodeMessageMap[JSON.stringify(code)] ||
      i18n.t("statusAndType.unknownStatus")
    );
  },
};

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
const NodeUpgradeStatusCodeMessageMap = {
  1: i18n.t("statusAndType.upgradingBuildingDone"),
  2: i18n.t("statusAndType.upgrading"),
  3: i18n.t("statusAndType.autoConfirm"),
  4: i18n.t("statusAndType.manualConfirm"),
  5: i18n.t("statusAndType.rollingBack"),

  20: i18n.t("statusAndType.upgradingFailed"),
  40: i18n.t("statusAndType.upgradingDone"),
};
