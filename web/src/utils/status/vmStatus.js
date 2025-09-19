import i18n from "@/utils/i18n/index.js";
// 运行中 [6,10,29,40]
// 已关机 [8,63]

const codeMessageMap = {
  0: i18n.t("statusAndType.creating"), // INSTANCE_INIT
  1: i18n.t("statusAndType.creating"), // INSTANCE_CREATING
  2: i18n.t("statusAndType.creating"), // INSTANCE_CREATED
  3: i18n.t("statusAndType.creating"), // INSTANCE_INJECTING
  4: i18n.t("statusAndType.creating"), // INSTANCE_INJECT_BOOTING
  5: i18n.t("statusAndType.created"), // INSTANCE_CLOUDINIT_DONE
  6: i18n.t("statusAndType.running"), // INSTANCE_RUNNING
  7: i18n.t("statusAndType.poweroffing"), // INSTANCE_POWEROFFING
  8: i18n.t("statusAndType.poweroff"), // INSTANCE_POWEROFF
  9: i18n.t("statusAndType.poweroning"), // INSTANCE_POWERONING
  10: i18n.t("statusAndType.running"), // INSTANCE_RUNNING
  11: i18n.t("statusAndType.creating"), // GET_PORT_PHASE_STATUS
  12: i18n.t("statusAndType.creating"), // GET_INSTANCE_CREATED_STATUS
  13: i18n.t("statusAndType.creating"), // WAIT_INSTANCE_CLOUD_INIT_RESULT
  14: i18n.t("statusAndType.deleting"), // GET_INSTANCE_REMOVED_STATUS
  15: i18n.t("statusAndType.deleting"), // GET_SNAP_REMOVED_STATUS
  16: i18n.t("statusAndType.poweroning"), // GET_INSTANCE_POWERON_RESULT
  17: i18n.t("statusAndType.poweroffing"), // GET_INSTANCE_POWEROFF_RESULT
  18: i18n.t("statusAndType.snapshotPreparing"), // GET_SNAP_SWITCHED_STATUS
  21: i18n.t("statusAndType.createFailed"), // INSTANCE_CREATE_FAILED
  22: i18n.t("statusAndType.deleteFailed"), // INSTANCE_REMOVE_FAILED
  23: i18n.t("statusAndType.createFailed"), // INSTANCE_INJECT_BOOT_FAILED
  24: i18n.t("statusAndType.createFailed"), // INSTANCE_EJECT_FAILED
  25: i18n.t("statusAndType.createFailed"), // SNAP_CREATE_FAILED
  26: i18n.t("statusAndType.deleteFailed"), // SNAP_REMOVE_FAILED
  27: i18n.t("statusAndType.snapSwitchFailed"), // SNAP_SWITCH_FAILED
  28: i18n.t("statusAndType.migrateFailed"), // INSTANCE_MIGRATE_FAILED
  29: i18n.t("statusAndType.running"), // INSTANCE_MIGRATE_CLEAN
  30: i18n.t("statusAndType.creating"), // SNAP_INIT
  31: i18n.t("statusAndType.creating"), // SNAP_CREATING
  32: i18n.t("statusAndType.created"), // SNAP_CREATED
  33: i18n.t("statusAndType.rollbacking"), // SNAP_SWITCHING
  34: i18n.t("statusAndType.rollbackSuccess"), // SNAP_SWITCHED
  35: i18n.t("statusAndType.deleting"), // SNAP_REMOVING
  40: i18n.t("statusAndType.running"), // HYPERVISOR_NODE_CREATED
  41: i18n.t("statusAndType.adding"), // HYPERVISOR_NODE_CHECKING
  42: i18n.t("statusAndType.offline"), // HYPERVISOR_NODE_OFFLINE
  60: i18n.t("statusAndType.poweroffing"),
  61: i18n.t("statusAndType.poweroffing"), // INSTANCE_POWERING_OFF_DETACH_PCI
  62: i18n.t("statusAndType.poweroffing"), // GET_INSTANCE_POWERING_OFF_DETACH_PCI_STATUS
  63: i18n.t("statusAndType.poweroff"), // INSTANCE_POWERED_OFF_DETACH_PCI
  64: i18n.t("statusAndType.poweroning"), // INSTANCE_POWERING_ON_PREPARE_PCI
  65: i18n.t("statusAndType.poweroning"), // INSTANCE_POWERING_ON_ATTACH_PCI
  66: i18n.t("statusAndType.powerOnFailed"), // INSTANCE_POWER_ON_FAILED
  70: i18n.t("statusAndType.creating"), // PORT_CREATE
  101: i18n.t("statusAndType.deleting"), // INSTANCE_REMOVING
  103: i18n.t("statusAndType.deleteFailed"), // INSTANCE_REMOVED_FAILED
  104: i18n.t("statusAndType.creating"), // INSTANCE_EJECTING
  105: i18n.t("statusAndType.created"), // INSTANCE_EJECTED
  110: i18n.t("statusAndType.createFailed"), // INSTANCE_CREATE_FAILED_CLEANING
  111: i18n.t("statusAndType.createFailed"), // INSTANCE_CREATE_FAILED_CLEANED
  300: i18n.t("statusAndType.migrateInit"), // INSTANCE_MIGRATE_INIT
  301: i18n.t("statusAndType.suspending"), // INSTANCE_SUSPENDING
  302: i18n.t("statusAndType.suspending"), // INSTANCE_SUSPENDED
  303: i18n.t("statusAndType.suspending"), // GET_PORT_MIGRATED_STATUS
  304: i18n.t("statusAndType.suspending"), // GET_INSTANCE_RESUME_STATUS
  305: i18n.t("statusAndType.suspending"), // INSTANCE_RESUMED
  350: i18n.t("statusAndType.updating"), // INSTANCE_RESIZE_INIT
  351: i18n.t("statusAndType.updating"), // GET_INSTANCE_UPDATED_STATUS
  352: i18n.t("statusAndType.bootDevSwitching"), // INSTANCE_BOOT_DEV_SWITCHING
  353: i18n.t("statusAndType.bootDevSwitching"), // GET_INSTANCE_BOOT_DEV_STATUS
  360: i18n.t("statusAndType.resetPasswordHostname"), // INSTANCE_RESET_PASSWORD_HOSTNAME //'主机名/密码重置',
  361: i18n.t("statusAndType.resetPasswordHostnameProcessing"), // 主机名/密码重置中',//WAIT_INSTANCE_RESET_PASSWORD_HOSTNAME
};
const codeMessageTagMap = {
  0: "", // INSTANCE_INIT  创建中
  1: "", // INSTANCE_CREATING  创建中
  2: "", // INSTANCE_CREATED  创建中
  3: "", // INSTANCE_INJECTING  创建中
  4: "", // INSTANCE_INJECT_BOOTING  创建中
  5: "success", // INSTANCE_CLOUDINIT_DONE  创建成功
  6: "success", // INSTANCE_RUNNING  运行中
  7: "warning", // INSTANCE_POWEROFFING  关机中
  8: "warning", // INSTANCE_POWEROFF  已关机
  9: "", // INSTANCE_POWERONING  开机中
  10: "success", // INSTANCE_RUNNING  运行中
  11: "", // GET_PORT_PHASE_STATUS  创建中
  12: "", // GET_INSTANCE_CREATED_STATUS  创建中
  13: "", // WAIT_INSTANCE_CLOUD_INIT_RESULT  创建中
  14: "warning", // GET_INSTANCE_REMOVED_STATUS  删除中
  15: "warning", // GET_SNAP_REMOVED_STATUS  删除中
  16: "", // GET_INSTANCE_POWERON_RESULT  开机中
  17: "warning", // GET_INSTANCE_POWEROFF_RESULT  关机中
  18: "", // GET_SNAP_SWITCHED_STATUS  创建中
  21: "danger", // INSTANCE_CREATE_FAILED  创建失败
  22: "danger", // INSTANCE_REMOVE_FAILED  删除失败
  23: "danger", // INSTANCE_INJECT_BOOT_FAILED  创建失败
  24: "danger", // INSTANCE_EJECT_FAILED  创建失败
  25: "danger", // SNAP_CREATE_FAILED  创建失败
  26: "danger", // SNAP_REMOVE_FAILED  删除失败
  27: "danger", // SNAP_SWITCH_FAILED  快照切换失败
  28: "danger", // INSTANCE_MIGRATE_FAILED 迁移失败
  29: "success", // INSTANCE_MIGRATE_CLEAN 运行中
  30: "", // SNAP_INIT  创建中
  31: "", // SNAP_CREATING  创建中
  32: "success", // SNAP_CREATED  创建完成
  33: "warning", // SNAP_SWITCHING  回滚中
  34: "success", // SNAP_SWITCHED  回滚成功
  35: "warning", // SNAP_REMOVING  删除中
  40: "success", // HYPERVISOR_NODE_CREATED  已添加
  41: "", // HYPERVISOR_NODE_CHECKING  添加中
  42: "", // HYPERVISOR_NODE_OFFLINE 已离线
  60: "warning", //  关机中
  61: "warning", // INSTANCE_POWERING_OFF_DETACH_PCI  正在卸载GPU---关机中
  62: "warning", // GET_INSTANCE_POWERING_OFF_DETACH_PCI_STATUS  卸载GPU---关机中
  63: "warning", // INSTANCE_POWERED_OFF_DETACH_PCI  已卸载GPU---已关机
  64: "", // INSTANCE_POWERING_ON_PREPARE_PCI  准备挂载GPU---开机中
  65: "", // INSTANCE_POWERING_ON_ATTACH_PCI  正在挂载GPU---开机中
  66: "danger", // INSTANCE_POWER_ON_FAILED  开机失败
  70: "", // PORT_CREATE 创建中
  101: "warning", // INSTANCE_REMOVING  删除中
  103: "danger", // INSTANCE_REMOVED_FAILED  删除失败
  104: "", // INSTANCE_EJECTING  创建中
  105: "success", // INSTANCE_EJECTED  创建成功
  110: "danger", // INSTANCE_CREATE_FAILED_CLEANING
  111: "danger", // INSTANCE_CREATE_FAILED_CLEANED
  300: "", // INSTANCE_MIGRATE_INIT
  301: "", // INSTANCE_SUSPENDING
  302: "", // INSTANCE_SUSPENDED
  303: "", // GET_PORT_MIGRATED_STATUS
  304: "", // GET_INSTANCE_RESUME_STATUS
  305: "", // INSTANCE_RESUMED
  350: "", // INSTANCE_RESIZE_INIT //更新中
  351: "", // GET_INSTANCE_UPDATED_STATUS //更新中
  352: "", // INSTANCE_BOOT_DEV_SWITCHING //启动项切换中
  353: "", // GET_INSTANCE_BOOT_DEV_STATUS //启动项切换中
  360: "success", // INSTANCE_RESET_PASSWORD_HOSTNAME //'主机名/密码重置',
  361: "", // 主机名/密码重置中',//WAIT_INSTANCE_RESET_PASSWORD_HOSTNAME
};
const showVmStatusMessage = (code, type) => {
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

export default showVmStatusMessage;
