import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("statusAndType.creating"), // ADDING
  1: i18n.t("statusAndType.created"), // ADDED
  2: i18n.t("statusAndType.createFailed"), // ADD_FAILED
  3: i18n.t("statusAndType.deleting"), // DELETING
  4: i18n.t("statusAndType.deleted"), // DELETED
  5: i18n.t("statusAndType.deleteFailed"), // DELETE_FAILED
  6: i18n.t("statusAndType.creating"), // AGENT_ADDING
  7: i18n.t("statusAndType.createFailed"), // AGENT_ADDING_ERR
  8: i18n.t("statusAndType.deleting"), // AGENT_DELETING
  9: i18n.t("statusAndType.deleteFailed"), // AGENT_DELETING_ERR
  41: i18n.t("statusAndType.updating"), // UPDATING
  42: i18n.t("statusAndType.updated"), // UPDATED
  45: i18n.t("statusAndType.updateFailed"), // UPDATE_FAILED
  20: i18n.t("statusAndType.attaching"), // ATTACHING
  21: i18n.t("statusAndType.attaching"), // AGENT_ATTACHING
  22: i18n.t("statusAndType.detaching"), // DETACHING
  23: i18n.t("statusAndType.detaching"), // AGENT_DETACHING
  24: i18n.t("statusAndType.attachingFailed"), // ATTACH_FAILED
  25: i18n.t("statusAndType.detachingFailed"), // DETACH_FAILED
  26: i18n.t("statusAndType.attached"), // ATTACHED
  27: i18n.t("statusAndType.detached"), // DETACHED
  28: i18n.t("statusAndType.resuming"), // PRE_DEST_RESUMING
  29: i18n.t("statusAndType.resuming"), // RESUMING
  30: i18n.t("statusAndType.resuming"), // AGENT_RESUMING
  31: i18n.t("statusAndType.running"), // RESUMED
  32: i18n.t("statusAndType.suspending"), // SUSPEND
  33: i18n.t("statusAndType.exporting"), // EXPORTING
  34: i18n.t("statusAndType.exporting"), // AGENT_EXPORTING
  35: i18n.t("statusAndType.suspending"), // SUSPENDING
  36: i18n.t("statusAndType.suspending"), // AGENT_SUSPENDING
  37: i18n.t("statusAndType.suspendFailed"), // SUSPEND_FAILED
  38: i18n.t("statusAndType.resumingFailed"), // RESUME_FAILED
  39: i18n.t("statusAndType.importing"), // IMPORTING
  50: i18n.t("statusAndType.switching"), // SNAP_SWITCHING
  51: i18n.t("statusAndType.switching"), // AGENT_SWITCHING
  52: i18n.t("statusAndType.switchFailed"), // SNAP_SWITCH_FAILED
  53: i18n.t("statusAndType.exportingFailed"), // EXPORT_FAILED
};
const codeMessageTagMap = {
  0: "", // ADDING 创建中
  1: "success", // ADDED 已创建
  2: "danger", // ADD_FAILED  创建失败
  3: "warning", // DELETING  删除中
  4: "", // DELETED  已删除
  5: "danger", // DELETE_FAILED  删除失败
  6: "", // AGENT_ADDING 创建中
  7: "danger", // AGENT_ADDING_ERR  创建失败
  8: "warning", // AGENT_DELETING  删除中
  9: "danger", // AGENT_DELETING_ERR  删除失败
  41: "", // UPDATING 更新中
  42: "success", // UPDATED 已更新
  45: "danger", // UPDATE_FAILED 更新失败
  20: "", // ATTACHING 挂载中
  21: "", // AGENT_ATTACHING  挂载中
  22: "", // DETACHING 卸载中
  23: "", // AGENT_DETACHING 卸载中
  24: "danger", // ATTACH_FAILED 挂载失败
  25: "danger", // DETACH_FAILED 卸载失败
  26: "success", // ATTACHED 已挂载
  27: "", // DETACHED 未挂载
  28: "", // PRE_DEST_RESUMING 恢复中
  29: "", // RESUMING 恢复中
  30: "", // AGENT_RESUMING 恢复中
  31: "success", // RESUMED 运行中
  32: "", // SUSPEND 迁移中
  33: "", // EXPORTING 导出中
  34: "", // AGENT_EXPORTING 导出中
  35: "", // SUSPENDING 迁移中
  36: "", // AGENT_SUSPENDING 迁移中
  37: "danger", // SUSPEND_FAILED 迁移失败
  38: "danger", // RESUME_FAILED 恢复失败
  39: "", // IMPORTING 导入中
  50: "", // SNAP_SWITCHING 切换中
  51: "", // AGENT_SWITCHING  切换中
  52: "danger", // SNAP_SWITCH_FAILED 切换失败
  53: "danger", // EXPORT_FAILED 导出失败
};
const showVolumeStatusMessage = (code, type) => {
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

export default showVolumeStatusMessage;
