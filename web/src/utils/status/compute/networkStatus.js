import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  ACTIVE: i18n.t("statusAndType.running"),
  BUILD: i18n.t("statusAndType.building"),
  DOWN: i18n.t("statusAndType.down"),
  ERROR: i18n.t("statusAndType.error"),
  PENDING_CREATE: i18n.t("statusAndType.pendingCreate"),
  PENDING_UPDATE: i18n.t("statusAndType.pendingUpdate"),
  PENDING_DELETE: i18n.t("statusAndType.pendingDelete"),
  UNRECOGNIZED: i18n.t("statusAndType.unrecognized"),
  "N/A": i18n.t("statusAndType.na"),
  AVAILABLE: i18n.t("statusAndType.available"),
  PENDING: i18n.t("statusAndType.pending"),
};
const codeMessageTagMap = {
  ACTIVE: "success",
  BUILD: "",
  DOWN: "info",
  ERROR: "danger",
  PENDING_CREATE: "",
  PENDING_UPDATE: "",
  PENDING_DELETE: "warning",
  UNRECOGNIZED: "info",
  "N/A": "info",
  AVAILABLE: "success",
  PENDING: "",
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
