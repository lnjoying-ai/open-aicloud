import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  active: i18n.t("statusAndType.running"),
  saving: i18n.t("statusAndType.saving"),
  queued: i18n.t("statusAndType.queued"),
  pending_delete: i18n.t("statusAndType.pending_delete"),
  killed: i18n.t("statusAndType.killed"),
  deactivated: i18n.t("statusAndType.deactivated"),
  deleted: i18n.t("statusAndType.deleted"),
  importing: i18n.t("statusAndType.importing"),
};
const codeMessageTagMap = {
  active: "success",
  saving: "",
  queued: "",
  pending_delete: "warning",
  killed: "info",
  deactivated: "",
  deleted: "danger",
  importing: "",
};
const showStatusMessage = (code, type) => {
  if (type == "tag") {
    return codeMessageTagMap[code] != undefined
      ? codeMessageTagMap[code]
      : "info";
  } else {
    return codeMessageMap[code] || i18n.t("statusAndType.unknownStatus");
  }
};

export default showStatusMessage;
