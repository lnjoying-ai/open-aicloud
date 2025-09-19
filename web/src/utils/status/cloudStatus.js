import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("statusAndType.create"), // created
  1: i18n.t("statusAndType.importing"), // importing
  2: i18n.t("statusAndType.upgrading"), // updating
  3: i18n.t("statusAndType.internalTest"), // internal test
  11: i18n.t("statusAndType.publicTest"), // public test
  12: i18n.t("statusAndType.onShelf"), // on-shelf
  13: i18n.t("statusAndType.preOffShelf"), // pre off-shelf
  21: i18n.t("statusAndType.maintaining"), // maintaining
  31: i18n.t("statusAndType.offShelf"), // off-shelf
  50: i18n.t("statusAndType.importFailed"), // import failed
  51: i18n.t("statusAndType.maintainFailed"), // maintain failed
  90: i18n.t("statusAndType.deleting"), // removing
  91: i18n.t("statusAndType.deleteFailed"), // remove failed
  99: i18n.t("statusAndType.deleted"), // removed
};
const codeMessageTagMap = {
  0: "", // created
  1: "", // importing
  2: "", // updating
  3: "success", // internal test
  11: "success", // public test
  12: "success", // on-shelf
  13: "warning", // pre off-shelf
  21: "warning", // maintaining
  31: "warning", // off-shelf
  50: "danger", // import failed
  51: "danger", // maintain failed
  90: "warning", // removing
  91: "danger", // remove failed
  99: "danger", // removed
};
const showCloudStatusMessage = (code, type) => {
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

export default showCloudStatusMessage;
