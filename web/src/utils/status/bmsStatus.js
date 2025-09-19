import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("statusAndType.initializing"), // INSTANCE_INIT
  1: i18n.t("statusAndType.getting_image_connection"), // INSTANCE_GETTING_IMAGE_CONNECTION
  2: i18n.t("statusAndType.getting_image_connection_success"), // INSTANCE_GET_IMAGE_CONNECTION_SUCCESS
  3: i18n.t("statusAndType.getting_image_connection_failed"), // INSTANCE_GET_IMAGE_CONNECTION_FAILED
  4: i18n.t("statusAndType.creating_setting_to_deploy_network"), // INSTANCE_CREATING_SETTING_TO_DEPLOY_NETWORK
  5: i18n.t("statusAndType.creating_setting_to_deploy_network_success"), // INSTANCE_SET_TO_DEPLOY_NETWORK_SUCCESS
  6: i18n.t("statusAndType.creating_setting_to_deploy_network_failed"), // INSTANCE_SET_TO_DEPLOY_NETWORK_FAILED
  7: i18n.t("statusAndType.deploying_by_pxe_agent"), // INSTANCE_DEPLOYING_BY_PXE_AGENT
  8: i18n.t("statusAndType.deployed_by_pxe_agent_success"), // INSTANCE_DEPLOYED_BY_PXE_AGENT_SUCCESS
  9: i18n.t("statusAndType.deployed_by_pxe_agent_failed"), // INSTANCE_DEPLOYED_BY_PXE_AGENT_FAILED
  10: i18n.t("statusAndType.deleting_from_deploy_network"), // INSTANCE_DELETING_FROM_DEPLOY_NETWORK
  11: i18n.t("statusAndType.deleting_from_deploy_network_success"), // INSTANCE_DELETED_FROM_DEPLOY_NETWORK_SUCCESS
  12: i18n.t("statusAndType.deleting_from_deploy_network_failed"), // INSTANCE_DELETED_FROM_DEPLOY_NETWORK_FAILED
  13: i18n.t("statusAndType.setting_to_tenant_network"), // INSTANCE_SETTING_TO_TENANT_NETWORK
  14: i18n.t("statusAndType.setting_to_tenant_network_success"), // INSTANCE_SET_TO_TENANT_NETWORK_SUCCESS
  15: i18n.t("statusAndType.setting_to_tenant_network_failed"), // INSTANCE_SET_TO_TENANT_NETWORK_FAILED
  16: i18n.t("statusAndType.injecting_iso_to_cloudinit"), // INSTANCE_INJECTING_ISO_TO_CLOUDINIT
  17: i18n.t("statusAndType.injecting_iso_to_cloudinit_success"), // INSTANCE_INJECT_ISO_TO_CLOUDINIT_SUCCESS
  18: i18n.t("statusAndType.injecting_iso_to_cloudinit_failed"), // INSTANCE_INJECT_ISO_TO_CLOUDINIT_FAILED
  19: i18n.t("statusAndType.injecting_iso_to_cloudinit_processing"), // WAIT_INSTANCE_CLOUD_INIT_RESULT
  20: i18n.t("statusAndType.cloudinit_injected"), // INSTANCE_CLOUD_INIT_SUCCESS
  21: i18n.t("statusAndType.cloudinit_injection_failed"), // INSTANCE_CLOUD_INIT_FAILED
  22: i18n.t("statusAndType.using_arpping_to_detect_instance"), // INSTANCE_RPOBE_INSTANCE_BY_ARP_PING
  23: i18n.t("statusAndType.using_arpping_to_detect_instance_success"), // INSTANCE_ARP_PING_SUCCESS
  24: i18n.t("statusAndType.using_arpping_to_detect_instance_failed"), // INSTANCE_ARP_PING_FAILED
  100: i18n.t("statusAndType.createFailed"), // INSTANCE_CREATE_FAILED
  101: i18n.t("statusAndType.running"), // INSTANCE_RUNNING
  102: i18n.t("statusAndType.poweroffing"), // INSTANCE_POWEROFFING
  103: i18n.t("statusAndType.poweroff"), // INSTANCE_POWEROFF
  104: i18n.t("statusAndType.poweroff_failed"), // INSTANCE_POWEROFF_FAILED
  110: i18n.t("statusAndType.poweroning"), // INSTANCE_POWERONING
  120: i18n.t("statusAndType.rebooting"), // INSTANCE_REBOOTING
  121: i18n.t("statusAndType.poweroffing_rebooting"), // INSTANCE_REBOOT_POWEROFFING
  122: i18n.t("statusAndType.poweroning_rebooting"), // INSTANCE_REBOOT_POWERONING
  200: i18n.t("statusAndType.pending_delete"), // INSTANCE_WAITING_REMOVED
  201: i18n.t("statusAndType.deleteFailed"), // INSTANCE_REMOVED_FAILED
  202: i18n.t("statusAndType.deleting_from_tenant_network"), // INSTANCE_REMOVE_DELETING_FROM_TENANT_NETWORK
  203: i18n.t("statusAndType.joining_to_deploy_network"), // INSTANCE_REMOVE_SETTING_TO_DEPLOY_NETWORK
  204: i18n.t("statusAndType.destroying_by_pxe_agent"), // INSTANCE_DESTROYING_BY_PXE_AGENT
};
const codeMessageTagMap = {
  0: "", // INSTANCE_INIT 初始化',
  1: "info", // INSTANCE_GETTING_IMAGE_CONNECTION 获取镜像连接',
  2: "success", // INSTANCE_GET_IMAGE_CONNECTION_SUCCESS 获取镜像连接成功',
  3: "danger", // INSTANCE_GET_IMAGE_CONNECTION_FAILED 获取镜像连接失败',
  4: "info", // INSTANCE_CREATING_SETTING_TO_DEPLOY_NETWORK 加入部署网络',
  5: "success", // INSTANCE_SET_TO_DEPLOY_NETWORK_SUCCESS 加入部署网络成功',
  6: "danger", // INSTANCE_SET_TO_DEPLOY_NETWORK_FAILED 加入部署网络失败',
  7: "", // INSTANCE_DEPLOYING_BY_PXE_AGENT pxe Agent 部署中
  8: "success", // INSTANCE_DEPLOYED_BY_PXE_AGENT_SUCCESS pxe Agent 部署成功
  9: "danger", // INSTANCE_DEPLOYED_BY_PXE_AGENT_FAILED pxe Agent 部署失败
  10: "warning", // INSTANCE_DELETING_FROM_DEPLOY_NETWORK 从部署网络中删除',
  11: "danger", // INSTANCE_DELETED_FROM_DEPLOY_NETWORK_SUCCESS 从部署网络中删除成功',
  12: "danger", // INSTANCE_DELETED_FROM_DEPLOY_NETWORK_FAILED 从部署网络中删除失败',
  13: "", // INSTANCE_SETTING_TO_TENANT_NETWORK 加入到租户网络',
  14: "success", // INSTANCE_SET_TO_TENANT_NETWORK_SUCCESS 加入到租户网络成功',
  15: "danger", // INSTANCE_SET_TO_TENANT_NETWORK_FAILED 加入到租户网络失败',
  16: "", // INSTANCE_INJECTING_ISO_TO_CLOUDINIT 注入ISO到CLOUDINIT',
  17: "success", // INSTANCE_INJECT_ISO_TO_CLOUDINIT_SUCCESS 注入ISO到CLOUDINIT成功',
  18: "danger", // INSTANCE_INJECT_ISO_TO_CLOUDINIT_FAILED 注入ISO到CLOUDINIT失败',
  19: "warning", // WAIT_INSTANCE_CLOUD_INIT_RESULT 正在注入ISO到CLOUDINIT中',
  20: "success", // INSTANCE_CLOUD_INIT_SUCCESS CLOUDINIT成功',
  21: "danger", // INSTANCE_CLOUD_INIT_FAILED CLOUDINIT失败',
  22: "info", // INSTANCE_RPOBE_INSTANCE_BY_ARP_PING 使用ARPPING进行实例探测',
  23: "success", // INSTANCE_ARP_PING_SUCCESS 使用ARPPING进行实例探测成功',
  24: "danger", // INSTANCE_ARP_PING_FAILED 使用ARPPING进行实例探测失败',
  100: "danger", // INSTANCE_CREATE_FAILED 创建失败',
  101: "success", // INSTANCE_RUNNING 运行中',
  102: "warning", // INSTANCE_POWEROFFING 关机中',
  103: "", // INSTANCE_POWEROFF 已关机',
  104: "danger", // INSTANCE_POWEROFF_FAILED 关机失败',
  110: "", // INSTANCE_POWERONING 开机中',
  120: "warning", // INSTANCE_REBOOTING 重启中',
  121: "warning", // INSTANCE_REBOOT_POWEROFFING 关机中(正在
  122: "", // INSTANCE_REBOOT_POWERONING 开机中(正在
  200: "warning", // INSTANCE_WAITING_REMOVED 等待删除',
  201: "danger", // INSTANCE_REMOVED_FAILED 删除失败',
  202: "warning", // INSTANCE_REMOVE_DELETING_FROM_TENANT_NETWORK 正在从租户网络中删除',
  203: "warning", // INSTANCE_REMOVE_SETTING_TO_DEPLOY_NETWORK 加入到部署网络中准备删除',
  204: "danger", // INSTANCE_DESTROYING_BY_PXE_AGENT 通过PXE AGENT
};
const showStatusMessage = (code, type) => {
  if (type == "tag") {
    return codeMessageTagMap[JSON.stringify(code)] != undefined
      ? codeMessageTagMap[JSON.stringify(code)]
      : "info";
  } else {
    return (
      codeMessageMap[JSON.stringify(code)] || i18n.t("statusAndType.unknown")
    );
  }
};

export default showStatusMessage;
