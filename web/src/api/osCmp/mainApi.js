import request from "@/utils/request";
import store from "@/store";

const computeImageVersion = "v2"; //镜像模块版本
const computeVersion = "v2.1"; //计算模块版本
const storageVersion = "v3"; //存储模块版本
const networkVersion = "v2.0"; //网络模块版本


const getComputeApi = () => {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    (store.state.app.hxcloudData && store.state.app.hxcloudData.product_short
      ? store.state.app.hxcloudData.product_short
      : "") +
    "/clouds/" +
    (store.state.app.hxcloudData.cloud_id
      ? store.state.app.hxcloudData.cloud_id
      : "") +
    "/" +
    computeVersion
  );
};
const getComputeImageApi = () => {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    (store.state.app.hxcloudData && store.state.app.hxcloudData.product_short
      ? store.state.app.hxcloudData.product_short
      : "") +
    "/clouds/" +
    (store.state.app.hxcloudData.cloud_id
      ? store.state.app.hxcloudData.cloud_id
      : "") +
    "/" +
    computeImageVersion
  );
};
const getStorageApi = () => {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    (store.state.app.hxcloudData && store.state.app.hxcloudData.product_short
      ? store.state.app.hxcloudData.product_short
      : "") +
    "/clouds/" +
    (store.state.app.hxcloudData.cloud_id
      ? store.state.app.hxcloudData.cloud_id
      : "") +
    "/" +
    storageVersion
  );
};
const getNetworkApi = () => {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    (store.state.app.hxcloudData && store.state.app.hxcloudData.product_short
      ? store.state.app.hxcloudData.product_short
      : "") +
    "/clouds/" +
    (store.state.app.hxcloudData.cloud_id
      ? store.state.app.hxcloudData.cloud_id
      : "") +
    "/" +
    networkVersion
  );
};
const getNewApi = () => {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    (store.state.app.hxcloudData && store.state.app.hxcloudData.product_short
      ? store.state.app.hxcloudData.product_short
      : "") +
    "/clouds/" +
    (store.state.app.hxcloudData.cloud_id
      ? store.state.app.hxcloudData.cloud_id
      : "")
  );
};
const mainApi = {
  /***
  ********
  ************
  云主机
  包含：云主机、已挂载硬盘，快照，镜像、规格、密钥
  ***********
  *******
  ***/

  /***********/
  /****** 云主机 ******/
  getInstancesList: (params) =>
    request({ url: getComputeApi() + "/servers", method: "get", params }), //获取云主机列表

  getInstancesDetailList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.1/servers/detail",
      method: "get",
      params,
    }), //获取详细云主机列表

  createInstance: (data) =>
    request({ url: getComputeApi() + "/servers", method: "post", data }), //创建云主机

  getInstanceDetail: (server_id) =>
    request({
      url: getNewApi() + "/extension/v2.1/servers/" + server_id,
      method: "get",
    }), //获取云主机详情

  putInstance: (server_id, data) =>
    request({
      url: getComputeApi() + "/servers/" + server_id,
      method: "put",
      data,
    }), //修改云主机
  postInstance: (server_id, data) =>
    request({
      url: getComputeApi() + "/servers/" + server_id + "/action",
      method: "post",
      data,
    }), //修改云主机
  delInstance: (server_id) =>
    request({
      url: getComputeApi() + "/servers/" + server_id,
      method: "delete",
    }), //删除云主机(放入回收站)
  actionInstance: (server_id, data) =>
    request({
      url: getComputeApi() + "/servers/" + server_id + "/action",
      method: "post",
      data,
    }), //操作云主机
  configurationmodification: (server_id, data) =>
    request({
      url: getNewApi() + "/extension/v2.1/servers/" + server_id + "/action",
      method: "post",
      data,
    }), //确认修改配置/迁移 回滚修改配置/迁移
  remoteConsoles: (server_id, data) =>
    request({
      url: getComputeApi() + "/servers/" + server_id + "/remote-consoles",
      method: "post",
      data,
    }), //远程控制台

  getInterfaceList: (server_id, params) =>
    request({
      url:
        getNewApi() + "/extension/v2.1/servers/" + server_id + "/os-interface",
      method: "get",
      params,
    }), //获取云主机接口列表

  getInterfaceDetail: (server_id, port_id) =>
    request({
      url:
        getComputeApi() + "/servers/" + server_id + "/os-interface/" + port_id,
      method: "get",
    }), //获取云主机接口详情

  createInterface: (server_id, data) =>
    request({
      url: getComputeApi() + "/servers/" + server_id + "/os-interface",
      method: "post",
      data,
    }), //创建云主机接口

  delInterface: (server_id, port_id) =>
    request({
      url:
        getComputeApi() + "/servers/" + server_id + "/os-interface/" + port_id,
      method: "delete",
    }), //删除云主机接口

  /***********/
  /****** 已挂载硬盘 ******/
  getVolumeAttachmentsList: (server_id, params) =>
    request({
      url:
        getNewApi() +
        "/extension/v2.1/servers/" +
        server_id +
        "/os-volume_attachments",
      method: "get",
      params,
    }), //获取已挂载硬盘列表

  attachVolumeAttachment: (server_id, data) =>
    request({
      url: getComputeApi() + "/servers/" + server_id + "/os-volume_attachments",
      method: "post",
      data,
    }), //挂载硬盘到云主机

  getVolumeAttachmentDetail: (server_id, volumeAttachment_id) =>
    request({
      url:
        getComputeApi() +
        "/servers/" +
        server_id +
        "/os-volume_attachments/" +
        volumeAttachment_id,
      method: "get",
    }), //获取已挂载硬盘详情

  putVolumeAttachment: (server_id, volumeAttachment_id, data) =>
    request({
      url:
        getComputeApi() +
        "/servers/" +
        server_id +
        "/os-volume_attachments/" +
        volumeAttachment_id,
      method: "put",
      data,
    }), //修改已挂载硬盘

  delVolumeAttachment: (server_id, volumeAttachment_id) =>
    request({
      url:
        getComputeApi() +
        "/servers/" +
        server_id +
        "/os-volume_attachments/" +
        volumeAttachment_id,
      method: "delete",
    }), //删除已挂载硬盘

  /***********/
  /****** 规格 ******/
  getFlavorsList: (params) =>
    request({ url: getComputeApi() + "/flavors", method: "get", params }), //获取规格列表

  getFlavorDetailList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.1/flavors/detail",
      method: "get",
      params,
    }), //获取详细规格列表
  createFlavor: (data) =>
    request({ url: getComputeApi() + "/flavors", method: "post", data }), //创建规格

  getFlavorDetail: (flavor_id) =>
    request({ url: getComputeApi() + "/flavors/" + flavor_id, method: "get" }), //获取规格详情

  putFlavor: (flavor_id, data) =>
    request({
      url: getComputeApi() + "/flavors/" + flavor_id,
      method: "put",
      data,
    }), //修改规格

  delFlavor: (flavor_id) =>
    request({
      url: getComputeApi() + "/flavors/" + flavor_id,
      method: "delete",
    }), //删除规格

  /***********/
  /****** 密钥 ******/
  getKeypairsList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.1/os-keypairs",
      method: "get",
      params,
    }), //获取密钥列表

  addKeypair: (data) =>
    request({ url: getComputeApi() + "/os-keypairs", method: "post", data }), //导入或创建密钥

  getKeypairDetail: (keypair_name) =>
    request({
      url: getComputeApi() + "/os-keypairs/" + keypair_name,
      method: "get",
    }), //获取密钥详情

  delKeypair: (keypair_name) =>
    request({
      url: getComputeApi() + "/os-keypairs/" + keypair_name,
      method: "delete",
    }), //删除密钥

  /***********/
  /****** 镜像 ******/

  getImagesList: (params) =>
    request({
      url: getNewApi() + "/extension/v2/images",
      method: "get",
      params,
    }), //获取镜像列表
  getcloudList: (server_id, params) =>
    request({
      url: getNewApi() + `/extension/v2.1/servers/${server_id}/images`,
      method: "get",
      params,
    }), //云主机重建云主机的中列表接口
  getImagesDetailList: (params) =>
    request({
      url: getComputeImageApi() + "/images/detail",
      method: "get",
      params,
    }), //获取详细镜像列表

  getImageDetail: (image_id) =>
    request({
      url: getComputeImageApi() + "/images/" + image_id,
      method: "get",
    }), //获取镜像详情

  delImage: (image_id) =>
    request({
      url: getComputeImageApi() + "/images/" + image_id,
      method: "delete",
    }), //删除镜像

  /***********/
  /****** 快照 ******/

  /***
  *******
  ***********
  存储
  包含：云硬盘、云硬盘快照、云硬盘备份、云盘类型
  ***********
  *******
  ***/

  /***********/
  /****** 云硬盘 ******/

  getVolumesList: (params) =>
    request({
      url: getStorageApi() + "/{project_id}/volumes",
      method: "get",
      params,
    }), //获取云硬盘列表

  getVolumeDetailList: (params) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/volumes/detail",
      method: "get",
      params,
    }), //获取详细云硬盘列表

  createVolume: (data) =>
    request({
      url: getStorageApi() + "/{project_id}/volumes",
      method: "post",
      data,
    }), //创建云硬盘

  getVolumeDetail: (volume_id, params) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/volumes/" + volume_id,
      method: "get",
      params,
    }), //获取云硬盘详情

  putVolume: (volume_id, data) =>
    request({
      url: getStorageApi() + "/{project_id}/volumes/" + volume_id,
      method: "put",
      data,
    }), //修改云硬盘

  delVolume: (volume_id) =>
    request({
      url: getStorageApi() + "/{project_id}/volumes/" + volume_id,
      method: "delete",
    }), //删除云硬盘
  actionVolume: (volume_id, data) =>
    request({
      url: getStorageApi() + "/{project_id}/volumes/" + volume_id + "/action",
      method: "post",
      data,
    }), //操作云主机
  volumeTransfers: (data) =>
    request({
      url: getStorageApi() + "/{project_id}/volume-transfers",
      method: "post",
      data,
    }), //云硬盘转让

  /***********/
  /****** 云硬盘快照 ******/
  getSnapshotsList: (params) =>
    request({
      url: getStorageApi() + "/{project_id}/snapshots",
      method: "get",
      params,
    }), //获取云硬盘快照列表

  getSnapshotDetailList: (params) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/snapshots/detail",
      method: "get",
      params,
    }), //获取详细云硬盘快照列表

  createSnapshot: (data) =>
    request({
      url: getStorageApi() + "/{project_id}/snapshots",
      method: "post",
      data,
    }), //创建云硬盘快照

  getSnapshotDetail: (snapshot_id) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/snapshots/" + snapshot_id,
      method: "get",
    }), //获取云硬盘快照详情

  putSnapshot: (snapshot_id, data) =>
    request({
      url: getStorageApi() + "/{project_id}/snapshots/" + snapshot_id,
      method: "put",
      data,
    }), //修改云硬盘快照

  delSnapshot: (snapshot_id) =>
    request({
      url: getStorageApi() + "/{project_id}/snapshots/" + snapshot_id,
      method: "delete",
    }), //删除云硬盘快照

  /***********/
  /****** 云硬盘备份 ******/
  getBackupsList: (params) =>
    request({
      url: getStorageApi() + "/{project_id}/backups",
      method: "get",
      params,
    }), //获取云硬盘备份列表

  getBackupDetailList: (params) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/backups/detail",
      method: "get",
      params,
    }), //获取详细云硬盘备份列表

  createBackup: (data) =>
    request({
      url: getStorageApi() + "/{project_id}/backups",
      method: "post",
      data,
    }), //创建云硬盘备份

  getBackupDetail: (backup_id) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/backups/" + backup_id,
      method: "get",
    }), //获取云硬盘备份详情

  putBackup: (backup_id, data) =>
    request({
      url: getStorageApi() + "/{project_id}/backups/" + backup_id,
      method: "put",
      data,
    }), //修改云硬盘备份

  delBackup: (backup_id) =>
    request({
      url: getStorageApi() + "/{project_id}/backups/" + backup_id,
      method: "delete",
    }), //删除云硬盘备份

  restoreBackup: (backup_id, data) =>
    request({
      url: getStorageApi() + "/{project_id}/backups/" + backup_id + "/restore",
      method: "post",
      data,
    }), //恢复云硬盘备份

  exportRecordBackup: (backup_id, data) =>
    request({
      url:
        getStorageApi() +
        "/{project_id}/backups/" +
        backup_id +
        "/export_record",
      method: "get",
      data,
    }), //导出云硬盘备份？

  importRecordBackup: (data) =>
    request({
      url: getStorageApi() + "/{project_id}/backups/import_record",
      method: "post",
      data,
    }), //导入云硬盘备份？

  /***********/
  /****** 云盘类型 ******/
  getVolumeTypesList: (params) =>
    request({
      url: getNewApi() + "/extension/v3/{project_id}/types",
      method: "get",
      params,
    }), //获取云盘类型列表

  /***
  *******
  ***********
  网络
  包含：网络、网络ip使用情况、子网、端口、路由、安全组、浮动IP，网络拓扑
  ***********
  *******
  ***/

  /***********/
  /****** 网络 ******/
  getNetworksList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/networks",
      method: "get",
      params,
    }), //获取网络列表

  creteNetwork: (data) =>
    request({ url: getNetworkApi() + "/networks", method: "post", data }), //创建网络(单独创建{},批量创建[])

  getNetworkDetail: (network_id) =>
    request({
      url: getNetworkApi() + "/networks/" + network_id,
      method: "get",
    }), //获取网络详情

  putNetwork: (network_id, data) =>
    request({
      url: getNetworkApi() + "/networks/" + network_id,
      method: "put",
      data,
    }), //修改网络

  delNetwork: (network_id) =>
    request({
      url: getNetworkApi() + "/networks/" + network_id,
      method: "delete",
    }), //删除网络
  // actionNetwork: (network_id, data) =>
  //   request({
  //     url: getNetworkApi() + "/networks/" + network_id + "/action",
  //     method: "post",
  //     data,
  //   }), //操作网络 data(associate_host:value，关联主机 / disassociate_host:null, 取消关联主机 / disassociate:null，取消关联网络 / disassociate_project:null，取消关联项目)

  /***********/
  /******  网络ip使用情况 ******/
  getNetworkIpAvailabilitiesList: (params) =>
    request({
      url: getNetworkApi() + "/network-ip-availabilities",
      method: "get",
      params,
    }), //获取网络ip使用情况列表

  getNetworkIpAvailabilitiesDetail: (network_id) =>
    request({
      url: getNetworkApi() + "/network-ip-availabilities/" + network_id,
      method: "get",
    }), //获取指定网络下网络ip使用情况

  /***********/
  /****** 子网 ******/
  getSubnetsList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/subnets",
      method: "get",
      params,
    }), //获取子网列表

  createSubnet: (data) =>
    request({ url: getNetworkApi() + "/subnets", method: "post", data }), //创建子网(单独创建{},批量创建[])

  getSubnetDetail: (subnet_id) =>
    request({ url: getNetworkApi() + "/subnets/" + subnet_id, method: "get" }), //获取子网详情

  putSubnet: (subnet_id, data) =>
    request({
      url: getNetworkApi() + "/subnets/" + subnet_id,
      method: "put",
      data,
    }), //修改子网

  delSubnet: (subnet_id) =>
    request({
      url: getNetworkApi() + "/subnets/" + subnet_id,
      method: "delete",
    }), //删除子网

  /***********/
  /****** 浮动IP ******/
  getFloatingipsList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/floatingips",
      method: "get",
      params,
    }), //获取浮动IP列表

  creteFloatingip: (data) =>
    request({ url: getNetworkApi() + "/floatingips", method: "post", data }), //创建浮动IP

  getFloatingipDetail: (floatingip_id) =>
    request({
      url: getNewApi() + "/extension/v2.0/floatingips/" + floatingip_id,
      method: "get",
    }), //获取浮动IP详情

  putFloatingip: (floatingip_id, data) =>
    request({
      url: getNetworkApi() + "/floatingips/" + floatingip_id,
      method: "put",
      data,
    }), //修改浮动IP

  delFloatingip: (floatingip_id) =>
    request({
      url: getNetworkApi() + "/floatingips/" + floatingip_id,
      method: "delete",
    }), //删除浮动IP

  /***********/
  /****** 端口 ******/
  getPortsList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/ports",
      method: "get",
      params,
    }), //获取端口列表

  createPort: (data) =>
    request({ url: getNetworkApi() + "/ports", method: "post", data }), //创建端口(单独创建{},批量创建[])

  getPortDetail: (port_id) =>
    request({
      url: getNewApi() + "/extension/v2.0/ports/" + port_id,
      method: "get",
    }), //获取端口详情

  putPort: (port_id, data) =>
    request({
      url: getNetworkApi() + "/ports/" + port_id,
      method: "put",
      data,
    }), //修改端口

  delPort: (port_id) =>
    request({ url: getNetworkApi() + "/ports/" + port_id, method: "delete" }), //删除端口

  addAllowedAddressPair: (port_id, data) =>
    request({
      url: getNetworkApi() + "/ports/" + port_id + "/add_allowed_address_pairs",
      method: "put",
      data,
    }), //添加允许地址对

  removeAllowedAddressPair: (port_id, data) =>
    request({
      url:
        getNetworkApi() + "/ports/" + port_id + "/remove_allowed_address_pairs",
      method: "put",
      data,
    }), //移除允许地址对

  /***********/
  /****** 路由 ******/
  getRoutersList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/routers",
      method: "get",
      params,
    }), //获取路由列表

  creteRouter: (data) =>
    request({ url: getNetworkApi() + "/routers", method: "post", data }), //创建路由

  getRouterDetail: (router_id) =>
    request({
      url: getNewApi() + "/extension/v2.0/routers/" + router_id,
      method: "get",
    }), //获取路由详情

  putRouter: (router_id, data) =>
    request({
      url: getNewApi() + "/extension/v2.0/routers/" + router_id,
      method: "put",
      data,
    }), //修改路由

  delRouter: (router_id) =>
    request({
      url: getNetworkApi() + "/routers/" + router_id,
      method: "delete",
    }), //删除路由

  addRouterInterface: (router_id, data) =>
    request({
      url: getNetworkApi() + "/routers/" + router_id + "/add_router_interface",
      method: "put",
      data,
    }), //添加接口到路由

  removeRouterInterface: (router_id, data) =>
    request({
      url:
        getNetworkApi() + "/routers/" + router_id + "/remove_router_interface",
      method: "put",
      data,
    }), //从路由移除接口

  addExtraRoute: (router_id, data) =>
    request({
      url: getNetworkApi() + "/routers/" + router_id + "/add_extraroutes",
      method: "put",
      data,
    }), //添加额外路由

  removeExtraRoute: (router_id, data) =>
    request({
      url: getNetworkApi() + "/routers/" + router_id + "/remove_extraroutes",
      method: "put",
      data,
    }), //移除额外路由

  addExternalGateway: (router_id, data) =>
    request({
      url: getNetworkApi() + "/routers/" + router_id + "/add_external_gateways",
      method: "put",
      data,
    }), //添加外部网关

  removeExternalGateway: (router_id, data) =>
    request({
      url:
        getNetworkApi() + "/routers/" + router_id + "/remove_external_gateways",
      method: "put",
      data,
    }), //移除外部网关

  updateExternalGateway: (router_id, data) =>
    request({
      url:
        getNetworkApi() + "/routers/" + router_id + "/update_external_gateways",
      method: "put",
      data,
    }), //更新外部网关

  /***********/
  /****** 安全组 ******/
  getSecurityGroupsList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/security-groups",
      method: "get",
      params,
    }), //获取安全组列表

  creteSecurityGroups: (data) =>
    request({
      url: getNetworkApi() + "/security-groups",
      method: "post",
      data,
    }), //创建安全组

  getSecurityGroupsDetail: (security_group_id) =>
    request({
      url: getNetworkApi() + "/security-groups/" + security_group_id,
      method: "get",
    }), //获取安全组详情

  putSecurityGroups: (security_group_id, data) =>
    request({
      url: getNetworkApi() + "/security-groups/" + security_group_id,
      method: "put",
      data,
    }), //修改安全组

  delSecurityGroups: (security_group_id) =>
    request({
      url: getNetworkApi() + "/security-groups/" + security_group_id,
      method: "delete",
    }), //删除安全组

  /***********/
  /****** 安全组出入规则 ******/
  getSecurityGroupRulesList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/security-group-rules",
      method: "get",
      params,
    }), //获取安全组出入规则列表

  creteSecurityGroupRule: (data) =>
    request({
      url: getNetworkApi() + "/security-group-rules",
      method: "post",
      data,
    }), //创建安全组出入规则

  getSecurityGroupRuleDetail: (security_group_rule_id) =>
    request({
      url: getNetworkApi() + "/security-group-rules/" + security_group_rule_id,
      method: "get",
    }), //获取安全组出入规则详情

  putSecurityGroupRule: (security_group_rule_id, data) =>
    request({
      url: getNetworkApi() + "/security-group-rules/" + security_group_rule_id,
      method: "put",
      data,
    }), //修改安全组出入规则
  delSecurityGroupRule: (security_group_rule_id) =>
    request({
      url: getNetworkApi() + "/security-group-rules/" + security_group_rule_id,
      method: "delete",
    }), //删除安全组出入规则


  // -------------------云主机回收站----------------------

  // 数据恢复接口
  restoredata: (cloud_id, data) =>
    request({
      url: getNewApi() + `/v2.1/servers/${cloud_id}/action`,
      method: "post",
      data,
    }),

  // 彻底删除接口
  completelydeletedata: (cloud_id, data) =>
    request({
      url: getNewApi() + `/v2.1/servers/${cloud_id}/action`,
      method: "post",
      data,
    }),

  // -------------------防火墙，防火墙策略，防火墙规则----------------------

  // 获取防火墙列表接口
  getfirewallList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/fw/firewalls",
      method: "get",
      params,
    }),

  // 删除防火墙接口
  deletefirewallList: (id) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewalls/" + id,
      method: "delete",
    }),

  // 添加防火墙接口
  postfirewall: (data) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewalls",
      method: "post",
      data,
    }),

  // 修改防火墙接口
  putfirewall: (firewall_id, data) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewalls/" + firewall_id,
      method: "put",
      data,
    }),

  // 获取防火墙策略接口
  getfirewallstrategyList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/fw/firewall_policies",
      method: "get",
      params,
    }),

  // 添加防火墙策略接口
  postfirewallstrategy: (data) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewall_policies",
      method: "post",
      data,
    }),

  // 编辑防火墙策略接口
  putfirewallstrategy: (firewalls_id, data) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewall_policies/" + firewalls_id,
      method: "put",
      data,
    }),

  // 删除防火墙策略接口
  deletefirewallstrategy: (id) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewall_policies/" + id,
      method: "delete",
    }),
  // 添加防火墙下的规则接口
  putaddstrategyrule: (strategy_id, data) =>
    request({
      url: getNewApi() + `/v2.0/fw/firewall_policies/${strategy_id}/insert_rule`,
      method: "put",
      data,
    }),
  // 删除防火墙策略下的规则接口
  putdeletestrategyrule: (strategy_id, data) =>
    request({
      url: getNewApi() + `/v2.0/fw/firewall_policies/${strategy_id}/remove_rule`,
      method: "put",
      data,
    }),
  // 获取防火墙规则接口
  getfirewallruleList: (params) =>
    request({
      url: getNewApi() + "/extension/v2.0/fw/firewall_rules",
      method: "get",
      params,
    }),
  // 删除防火墙规则接口
  deletefirewallrule: (id) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewall_rules/" + id,
      method: "delete",
    }),
  // 添加防火墙规则接口
  postfirewallrule: (data) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewall_rules",
      method: "post",
      data,
    }),
  // 编辑防火墙规则接口
  putfirewallrule: (wallrule_id, data) =>
    request({
      url: getNewApi() + "/v2.0/fw/firewall_rules/" + wallrule_id,
      method: "put",
      data,
    }),
};

export default mainApi;
