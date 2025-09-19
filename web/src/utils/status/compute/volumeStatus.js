import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  available: i18n.t("statusAndType.available"),
  attaching: i18n.t("statusAndType.attaching"),
  backing_up: i18n.t("statusAndType.backingUp"),
  creating: i18n.t("statusAndType.creating"),
  deleting: i18n.t("statusAndType.deleting"),
  downloading: i18n.t("statusAndType.downloading"),
  upLoading: i18n.t("statusAndType.upLoading"),
  error: i18n.t("statusAndType.error"),
  error_deleting: i18n.t("statusAndType.errorDeleting"),
  error_restoring: i18n.t("statusAndType.errorRestoring"),
  "in-use": i18n.t("statusAndType.inUse"),
  restoring_backup: i18n.t("statusAndType.restoringBackup"),
  detaching: i18n.t("statusAndType.detaching"),
  unrecognized: i18n.t("statusAndType.unrecognized"),
  restoring: i18n.t("statusAndType.restoring"),
  reverting: i18n.t("statusAndType.reverting"),
};
const codeMessageTagMap = {
  available: "",
  attaching: "",
  backing_up: "",
  creating: "",
  deleting: "",
  downloading: "",
  upLoading: "",
  error: "danger",
  error_deleting: "warning",
  error_restoring: "warning",
  "in-use": "success",
  restoring_backup: "",
  detaching: "",
  unrecognized: "info",
  restoring: "",
  reverting: "",
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
