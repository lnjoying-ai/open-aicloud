import showInstanceStatusMessage from "@/utils/status/compute/instanceStatus";
import showImageStatusMessage from "@/utils/status/compute/imageStatus";
import showNetworkStatusMessage from "@/utils/status/compute/networkStatus";
import showComputeVolumesStatusMessage from "@/utils/status/compute/volumeStatus";

import showStatusMessage from "@/utils/status/status";
import showBmsStatusMessage from "@/utils/status/bmsStatus";
import showBmsDeviceStatusMessage from "@/utils/status/bmsDeviceStatus";
import showVmStatusMessage from "@/utils/status/vmStatus";
import showPciStatusMessage from "@/utils/status/pciStatus";
import showVolumeStatusMessage from "@/utils/status/volumeStatus";
import showNatStatusMessage from "@/utils/status/natStatus";
import showSecGroupsStatusMessage from "@/utils/status/secGroupsStatus";
import showVmToEipStatusMessage from "@/utils/status/eipStatus";
import showCloudStatusMessage from "@/utils/status/cloudStatus";
import showClusterStatusMessage from "@/utils/status/clusterStatus";

const filtersFun = {
  // easyStack
  getInstanceStatus(code, type) {
    // 云主机状态
    return showInstanceStatusMessage(code, type);
  },
  getImageStatus(code, type) {
    // 镜像状态
    return showImageStatusMessage(code, type);
  },
  getNetworkStatus(code, type) {
    // 网络状态
    return showNetworkStatusMessage(code, type);
  },
  getComputeVolumeStatus(code, type) {
    // 云盘
    return showComputeVolumesStatusMessage(code, type);
  },
  // nextstack
  getStatus(code, type) {
    // vpc subnet状态
    return showStatusMessage(code, type);
  },
  getBmsStatus(code, type) {
    // 裸金属实例
    return showBmsStatusMessage(code, type);
  },
  getBmsDeviceStatus(code, type) {
    // 裸金属设备
    return showBmsDeviceStatusMessage(code, type);
  },
  getVmStatus(code, type) {
    // 实例 快照状态
    return showVmStatusMessage(code, type);
  },
  getPciStatus(code, type) {
    // pci设备
    return showPciStatusMessage(code, type);
  },
  getVolumeStatus(code, type) {
    // 云盘
    return showVolumeStatusMessage(code, type);
  },
  getNatStatus(code, type) {
    // NAT网关
    return showNatStatusMessage(code, type);
  },
  getSecGroupsStatus(code, type) {
    // 安全组
    return showSecGroupsStatusMessage(code, type);
  },
  getVmToEipStatus(code, type) {
    // 实例下 eip
    return showVmToEipStatusMessage(code, type);
  },
  getCloudStatus(code, type) {
    // 云状态
    return showCloudStatusMessage(code, type);
  },
  getClusterStatus(code, type) {
    // 集群状态
    return showClusterStatusMessage(code, type);
  },
};

export default filtersFun;
