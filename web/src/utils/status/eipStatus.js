import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  80: i18n.t("statusAndType.attachEipInit"), // ATTACH_EIP_INIT
  81: i18n.t("statusAndType.attachEipIng"), // ATTACH_EIP_ING
  82: i18n.t("statusAndType.attachEipDone"), // ATTACH_EIP_DONE
  83: i18n.t("statusAndType.detachEipInit"), // DETACH_EIP_INIT
  84: i18n.t("statusAndType.detachEipIng"), // DETACH_EIP_ING
  85: i18n.t("statusAndType.detachEipDone"), // DETACH_EIP_DONE
  86: i18n.t("statusAndType.attachEipErr"), // ATTACH_EIP_ERR
  87: i18n.t("statusAndType.detachEipErr"), // DETACH_EIP_ERR
};
const codeMessageTagMap = {
  80: "", // ATTACH_EIP_INIT
  81: "", // ATTACH_EIP_ING
  82: "success", // ATTACH_EIP_DONE
  83: "warning", // DETACH_EIP_INIT
  84: "warning", // DETACH_EIP_ING
  85: "success", // DETACH_EIP_DONE
  86: "danger", // ATTACH_EIP_ERR
  87: "danger", // DETACH_EIP_ERR
};
const showVmToEipStatusMessage = (code, type) => {
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

export default showVmToEipStatusMessage;
