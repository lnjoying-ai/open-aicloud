import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  ACTIVE: i18n.t("statusAndType.running"),
  BUILD: i18n.t("statusAndType.building"),
  REBUILD: i18n.t("statusAndType.rebuild"),
  SUSPENDED: i18n.t("statusAndType.suspended"),
  PAUSED: i18n.t("statusAndType.paused"),
  RESIZE: i18n.t("statusAndType.resize"),
  VERIFY_RESIZE: i18n.t("statusAndType.verify_resize"),
  REVERT_RESIZE: i18n.t("statusAndType.revert_resize"),
  PASSWORD: i18n.t("statusAndType.password"),
  REBOOT: i18n.t("statusAndType.reboot"),
  HARD_REBOOT: i18n.t("statusAndType.hard_reboot"),
  DELETED: i18n.t("statusAndType.deleted"),
  UNKNOWN: i18n.t("statusAndType.unknown"),
  ERROR: i18n.t("statusAndType.error"),
  STOPPED: i18n.t("statusAndType.stopped"),
  SHUTOFF: i18n.t("statusAndType.shutoff"),
  MIGRATING: i18n.t("statusAndType.migrating"),
  SHELVED: i18n.t("statusAndType.shelved"),
  SHELVED_OFFLOADED: i18n.t("statusAndType.shelved_offloaded"),
  UNRECOGNIZED: i18n.t("statusAndType.unrecognized"),
  SOFT_DELETED: i18n.t("statusAndType.soft_deleted"),
};
const codeMessageTagMap = {
  ACTIVE: "success",
  BUILD: "",
  REBUILD: "",
  SUSPENDED: "warning",
  PAUSED: "warning",
  RESIZE: "",
  VERIFY_RESIZE: "",
  REVERT_RESIZE: "",
  PASSWORD: "warning",
  REBOOT: "",
  HARD_REBOOT: "",
  DELETED: "danger",
  UNKNOWN: "info",
  ERROR: "danger",
  STOPPED: "danger",
  SHUTOFF: "danger",
  MIGRATING: "",
  SHELVED: "info",
  SHELVED_OFFLOADED: "info",
  UNRECOGNIZED: "info",
  SOFT_DELETED: "warning",
};
const showStatusMessage = (code, type) => {
  if (type == "tag") {
    return codeMessageTagMap[code] != undefined
      ? codeMessageTagMap[code]
      : "info";
  } else {
    return codeMessageMap[code] || "未知状态";
  }
};

export default showStatusMessage;
