import i18n from "@/utils/i18n/index.js";

const codeMessageMap = {
  0: i18n.t("statusAndType.initializing"), // DEVICE_INIT
  1: i18n.t("statusAndType.adding"), // DEVICE_CRETATING
  2: i18n.t("statusAndType.adding"), // DEVICE_CREATED_SUCCESS
  3: i18n.t("statusAndType.created_failed"), // DEVICE_CREATED_FAILED
  4: i18n.t("statusAndType.removing"), // DEVICE_REMOVING
  5: i18n.t("statusAndType.removing"), // DEVICE_REMOVING_CHECK
  6: i18n.t("statusAndType.deleted"), // DEVICE_REMOVED_SUCCESS
  7: i18n.t("statusAndType.removed_failed"), // DEVICE_REMOVED_FAILED
  8: i18n.t("statusAndType.device_inspecting"), // DEVICE_INSPECTING
  9: i18n.t("statusAndType.device_inspecting"), // DEVICE_INSPECTING_CHECK
  11: i18n.t("statusAndType.device_inspected_failed"), // DEVICE_INSPECTED_FAILED
  12: i18n.t("statusAndType.available"), // DEVICE_INSPECTED_SUCCESS //自检成功
  20: i18n.t("statusAndType.nic_initializing"), // DEVICE_NIC_INIT
  21: "", // DEVICE_NIC_SLOT_CREATING
  22: "", // DEVICE_NIC_SLOT_CREATING_CHECK
  23: "", // DEVICE_NIC_SLOT_CREATED_SUCCESS
  24: "", // DEVICE_NIC_SLOT_CREATED_FAILED
};
const codeMessageTagMap = {
  0: "", // DEVICE_INIT 初始化
  1: "", // DEVICE_CRETATING 添加中
  2: "", // DEVICE_CREATED_SUCCESS 添加中
  3: "danger", // DEVICE_CREATED_FAILED 添加失败
  4: "warning", // DEVICE_REMOVING 删除中
  5: "warning", // DEVICE_REMOVING_CHECK 删除中
  6: "danger", // DEVICE_REMOVED_SUCCESS 已删除
  7: "danger", // DEVICE_REMOVED_FAILED 删除失败
  8: "warning", // DEVICE_INSPECTING 设备自检
  9: "warning", // DEVICE_INSPECTING_CHECK 设备自检
  11: "danger", // DEVICE_INSPECTED_FAILED 自检失败
  12: "success", // DEVICE_INSPECTED_SUCCESS 可用 //自检成功
  20: "", // DEVICE_NIC_INIT 网卡初始化
  21: "", // DEVICE_NIC_SLOT_CREATING
  22: "", // DEVICE_NIC_SLOT_CREATING_CHECK
  23: "", // DEVICE_NIC_SLOT_CREATED_SUCCESS
  24: "", // DEVICE_NIC_SLOT_CREATED_FAILED
};
const showBmsDeviceStatusMessage = (code, type) => {
  if (type == "tag") {
    return codeMessageTagMap[JSON.stringify(code)] != undefined
      ? codeMessageTagMap[JSON.stringify(code)]
      : "info";
  } else {
    return codeMessageMap[JSON.stringify(code)] || "未知状态";
  }
};

export default showBmsDeviceStatusMessage;
