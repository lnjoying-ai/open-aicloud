import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("clusterStatus.Created"),
  1: i18n.t("clusterStatus.Spawn"),
  2: i18n.t("clusterStatus.Assigning"),
  3: i18n.t("clusterStatus.Assigned"),
  4: i18n.t("clusterStatus.buildingDeployPlan"),
  5: i18n.t("clusterStatus.buildDeployPlanSuccess"),
  6: i18n.t("clusterStatus.buildDeployPlanFailed"),
  7: i18n.t("clusterStatus.deploying"),
  8: i18n.t("clusterStatus.deployed"),
  9: i18n.t("clusterStatus.deployPartialSuccess"),
  10: i18n.t("clusterStatus.imported"),
  11: i18n.t("clusterStatus.deployFailed"),
  12: i18n.t("clusterStatus.AssignedFailed"),
  13: i18n.t("clusterStatus.buildFailed"),
  90: i18n.t("clusterStatus.Removing"),
  91: i18n.t("clusterStatus.RemoveFailed"),
  99: i18n.t("clusterStatus.Removed"),
};
const codeMessageTagMap = {
  0: "success",
  1: "",
  2: "",
  3: "success",
  4: "",
  5: "success",
  6: "danger",
  7: "",
  8: "success",
  9: "warning",
  10: "success",
  11: "danger",
  12: "danger",
  13: "danger",
  90: "warning",
  91: "danger",
  99: "",
};
const showClusterStatusMessage = (code, type) => {
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

export default showClusterStatusMessage;
