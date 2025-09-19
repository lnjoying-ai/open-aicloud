import request from "@/utils/request";
import store from "@/store";

const version = "v1";

function getApi(type) {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    store.getters.hxcloudData.product_short +
    "/clouds/" +
    store.getters.hxcloudData.cloud_id +
    "/api/" +
    type +
    "/" +
    version
  );
}
function getCloudApi(data, apiType) {
  return (
    process.env.VUE_APP_BASE_API +
    "api/cmp/v1/" +
    data.product_short +
    "/clouds/" +
    data.cloud_id +
    "/api/" +
    apiType +
    "/" +
    version
  );
}
const mainApi = {
  // 裸金属实例
  bmsAdd: (data) =>
    request({
      url: getApi("bm") + `/baremetal_instances`,
      method: "post",
      data,
    }), //添加
  bmsDel: (id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id,
      method: "delete",
    }), //删除
  bmsList: (params) =>
    request({
      url: getApi("bm") + `/baremetal_instances`,
      method: "get",
      params,
    }), //列表
  bmsDetail: (id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id,
      method: "get",
    }), //详情
  bmsPoweroff: (id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id + `/poweroff`,
      method: "post",
    }), //关机
  bmsPoweron: (id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id + `/poweron`,
      method: "post",
    }), //开机
  bmsReboot: (id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id + `/reboot`,
      method: "post",
    }), //重启
  bmsEditPwd: (data, id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id + "/reset",
      method: "put",
      data,
    }), //修改密码
  bmsEdit: (data, id) =>
    request({
      url: getApi("bm") + `/baremetal_instances/` + id,
      method: "put",
      data,
    }), //修改
  vmsResetPassword: (data, id) =>
    request({
      url: getApi("vm") + `/instances/` + id + "/reset",
      method: "put",
      data,
    }), //重置密码

  // 裸金属设备
  bmsDevListUsable: (params) =>
    request({
      url: getApi("bm") + `/baremetal_available_devices`,

      method: "get",
      params,
    }), //列表
  bmsDevList: (params) =>
    request({
      url: getApi("bm") + `/baremetal_devices`,
      method: "get",
      params,
    }), //列表
  bmsDevAdd: (data) =>
    request({ url: getApi("bm") + `/baremetal_devices`, method: "post", data }), //添加
  bmsDevDel: (id) =>
    request({
      url: getApi("bm") + `/baremetal_devices/` + id,
      method: "delete",
    }), //删除
  bmsDevDetail: (id) =>
    request({ url: getApi("bm") + `/baremetal_devices/` + id, method: "get" }), //详情
  bmsDevEdit: (data, id) =>
    request({
      url: getApi("bm") + `/baremetal_devices/` + id,
      method: "put",
      data,
    }), //修改
  bmsDevConfig: (params) =>
    request({
      url: getApi("bm") + `/baremetal_devices_configs`,

      method: "get",
      params,
    }), //规格

  // VPC
  vpcAdd: (data) =>
    request({ url: getApi("network") + `/vpcs`, method: "post", data }), //添加
  vpcEdit: (data, id) =>
    request({ url: getApi("network") + `/vpcs/` + id, method: "put", data }), //修改

  vpcDel: (id) =>
    request({ url: getApi("network") + `/vpcs/` + id, method: "delete" }), //删除
  vpcList: (params) =>
    request({ url: getApi("network") + `/vpcs`, method: "get", params }), //列表

  vpcDetail: (id) =>
    request({ url: getApi("network") + `/vpcs/` + id, method: "get" }), //详情
  subnetInVpc: (params) =>
    request({ url: getApi("network") + `/subnets`, method: "get", params }), //列表

  // subnets
  subnetsAdd: (data) =>
    request({ url: getApi("network") + `/subnets`, method: "post", data }), //添加
  subnetsEdit: (data, id) =>
    request({ url: getApi("network") + `/subnets/` + id, method: "put", data }), //修改
  subnetsDel: (id) =>
    request({ url: getApi("network") + `/subnets/` + id, method: "delete" }), //删除
  subnetsList: (params) =>
    request({ url: getApi("network") + `/subnets`, method: "get", params }), //列表

  subnetsDetail: (id) =>
    request({ url: getApi("network") + `/subnets/` + id, method: "get" }), //详情
  // portMaps
  portMapsAdd: (data) =>
    request({ url: getApi("network") + `/portMaps`, method: "post", data }), //添加
  portMapsDel: (id) =>
    request({ url: getApi("network") + `/portMaps/` + id, method: "delete" }), //删除
  portMapsList: (params) =>
    request({ url: getApi("network") + `/portMaps`, method: "get", params }), //列表
  portMapsDetail: (id) =>
    request({ url: getApi("network") + `/portMaps/` + id, method: "get" }), //详情
  //images
  imageList: (params) =>
    request({ url: getApi("repo") + `/images`, method: "get", params }), //镜像列表
  imageList_cloud: (params, config) =>
    request({
      url: getCloudApi(config, "repo") + `/images`,
      method: "get",
      params,
    }), //镜像列表
  imageDetail: (id) =>
    request({ url: getApi("repo") + `/images/` + id, method: "get" }), //镜像详情
  imageModify: (data, id) =>
    request({ url: getApi("repo") + `/images/` + id, method: "put", data }), //修改镜像
  imageDel: (id) =>
    request({ url: getApi("repo") + `/images/` + id, method: "delete" }), //删除镜像
  imageVisibility: (data, id) =>
    request({
      url: getApi("repo") + `/images/` + id + "/visibility?visibility=" + data,
      method: "put",
    }), //修改镜像可见性
  // pubkeys
  pubkeysList: (params) =>
    request({ url: getApi("bm") + `/pubkeys`, method: "get", params }), //列表
  pubkeysList_cloud: (params, config) =>
    request({
      url: getCloudApi(config, "bm") + `/pubkeys`,
      method: "get",
      params,
    }), //列表
  pubkeysAdd: (data) =>
    request({ url: getApi("bm") + `/pubkeys`, method: "post", data }), //创建密钥对
  pubkeysModify: (data, id) =>
    request({ url: getApi("bm") + `/pubkeys/` + id, method: "put", data }), //修改
  pubkeysUpload: (data) =>
    request({ url: getApi("bm") + `/pubkeys/upload`, method: "post", data }), //上传密钥对
  pubkeysDel: (id) =>
    request({ url: getApi("bm") + `/pubkeys/` + id, method: "delete" }), //删除
  pubkeysDetail: (id) =>
    request({ url: getApi("bm") + `/pubkeys/` + id, method: "get" }), //详情
  // eip
  eipsList: (params) =>
    request({ url: getApi("network") + `/eips`, method: "get", params }), //列表
  eipsEdit: (id, data) =>
    request({
      url: getApi("network") + "/nat-gateways/" + id,
      method: "put",
      data,
    }),
  eipsPortEdit: (id, data) =>
    request({
      url: getApi("network") + "/nat-gateways/" + id + "/ports",
      method: "put",
      data,
    }),
  eipsAdd: (data) =>
    request({ url: getApi("network") + `/eips`, method: "post", data }), //添加
  // 申请eip
  applyforeip: (data) =>
    request({ url: getApi("network") + `/eips/apply`, method: "post", data }), //添加
  // 释放eip
  releaseeip: (eipid) =>
    request({
      url: getApi("network") + `/eips/${eipid}/release`,
      method: "post",
    }), //添加
  eipsAllocate: (id) =>
    request({
      url: getApi("network") + `/eips/allocate/` + id,
      method: "post",
    }), //分配
  eipsDel: (id) =>
    request({ url: getApi("network") + `/eips/` + id, method: "delete" }), //删除
  eipsAttach: (id, data) =>
    request({
      url: getApi("network") + `/eips/` + id + "/attach",
      method: "put",
      data,
    }), //挂载
  eipsDetach: (id, data) =>
    request({
      url: getApi("network") + `/eips/` + id + "/detach",
      method: "put",
      data,
    }), //卸载

  // eip pool
  eipPoolsList: (params) =>
    request({ url: getApi("network") + `/eip_pools`, method: "get", params }), //列表
  eipPoolsAdd: (data) =>
    request({ url: getApi("network") + `/eip_pools`, method: "post", data }), //添加
  eipPoolsDel: (id) =>
    request({ url: getApi("network") + `/eip_pools/` + id, method: "delete" }), //删除
  eipPoolsDetail: (id) =>
    request({ url: getApi("network") + `/eip_pools/` + id, method: "get" }), //详情
  eipPoolsModify: (data, id) =>
    request({
      url: getApi("network") + `/eip_pools/` + id,
      method: "put",
      data,
    }), //修改

  // portMap
  portMapList: (params) =>
    request({ url: getApi("network") + `/portMaps`, method: "get", params }), //列表
  portMapAdd: (data) =>
    request({ url: getApi("network") + `/portMaps`, method: "post", data }), //添加
  portMapDel: (id) =>
    request({ url: getApi("network") + `/portMaps/` + id, method: "delete" }), //删除
  portMapDetail: (id) =>
    request({ url: getApi("network") + `/portMaps/` + id, method: "get" }), //详情

  topology: (id) =>
    request({ url: getApi("network") + `/topology/` + id, method: "get" }), //列表

  // 检查端口是否被占用
  portsCheck: (params, id) =>
    request({
      url: getApi("network") + `/eips/` + id + "/ports",

      method: "get",
      params,
    }), //检查端口是否被占用

  // 快照
  snapsList: (params) =>
    request({ url: getApi("vm") + `/snaps`, method: "get", params }), //实例下快照列表 params不传为全部快照
  snapsAdd: (data) =>
    request({ url: getApi("vm") + `/snaps`, method: "post", data }), //添加快照
  snapsDel: (id) =>
    request({ url: getApi("vm") + `/snaps/` + id, method: "delete" }), //删除快照
  snapsDetail: (id) =>
    request({ url: getApi("vm") + `/snaps/` + id, method: "get" }), //快照详情
  snapsEdit: (data, id) =>
    request({ url: getApi("vm") + `/snaps/` + id, method: "put", data }), //修改
  snapsSwitch: (id) =>
    request({ url: getApi("vm") + `/snaps/` + id + "/switch", method: "put" }), //回滚快照

  // 规格
  flavorsList: (params) =>
    request({ url: getApi("repo") + `/flavors`, method: "get", params }), //规格列表

  flavorsAdd: (data) =>
    request({ url: getApi("repo") + `/flavors`, method: "post", data }), //添加规格
  flavorsDel: (id) =>
    request({ url: getApi("repo") + `/flavors/` + id, method: "delete" }), //删除规格
  flavorsDetail: (id) =>
    request({ url: getApi("repo") + `/flavors/` + id, method: "get" }), //规格详情
  flavorsEdit: (data, id) =>
    request({ url: getApi("repo") + `/flavors/` + id, method: "put", data }), //修改规格
  flavorsMaxInfo: () =>
    request({ url: getApi("repo") + `/flavors/max_info`, method: "get" }), //规格最大值限制
  flavorGPUInfo: () =>
    request({ url: getApi("repo") + `/flavors/gpus`, method: "get" }), //GPU规格最大值限制

  // 计算节点
  vmsHypervisorNodesList: (params) =>
    request({ url: getApi("vm") + `/hypervisor_nodes`, method: "get", params }), //计算节点列表
  // 实例的迁移获取计算节点的接口
  vmsHypervisorNodesListallocation: (params) =>
    request({
      url: getApi("vm") + `/hypervisor_nodes/allocation`,
      method: "get",
      params,
    }), //计算节点列表
  vmsHypervisorNodesAdd: (data) =>
    request({ url: getApi("vm") + `/hypervisor_nodes`, method: "post", data }), //添加计算节点
  vmsHypervisorNodesDel: (id) =>
    request({
      url: getApi("vm") + `/hypervisor_nodes/` + id,
      method: "delete",
    }), //删除计算节点
  vmsHypervisorNodesDetail: (id) =>
    request({ url: getApi("vm") + `/hypervisor_nodes/` + id, method: "get" }), //计算节点详情

  vmsHypervisorNodesEdit: (data, id) =>
    request({
      url: getApi("vm") + `/hypervisor_nodes/` + id,
      method: "put",
      data,
    }), //修改
  vmsHypervisorNodesAllocation: (params) =>
    request({
      url: getApi("vm") + `/hypervisor_nodes/allocation`,
      method: "get",
      params,
    }), //计算节点分配

  // 实例
  vmsToVolumesList: (data, id) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/volumes`,

      method: "put",
      data,
    }), //云盘挂载

  vmsInstabcesList: (params) =>
    request({ url: getApi("vm") + `/instances`, method: "get", params }), //实例列表
  vmsInstabcesAdd: (data) =>
    request({ url: getApi("vm") + `/instances`, method: "post", data }), //添加实例
  vmsInstabcesAdd_cloud: (data, config) =>
    request({
      url: getCloudApi(config, "vm") + `/instances`,
      method: "post",
      data,
    }), //添加实例
  vmsInstabcesAddCount: (data) =>
    request({ url: getApi("vm") + `/instances/counts`, method: "post", data }), //添加实例数量

  vmsInstabcesPoweron: (id) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/poweron`,
      method: "put",
    }), //实例启动
  vmsInstabcesReboot: (id) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/reboot`,
      method: "put",
    }), //实例重启
  vmsInstabcesPoweroff: (id, data) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/poweroff`,
      method: "put",
      data,
    }), //实例停止
  vmsInstabcesDetachmentPoweroff: (id) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/poweroff?detachment=true`,
      method: "put",
    }), //实例停止 节省模式
  vmsInstabcesDel: (id) =>
    request({ url: getApi("vm") + `/instances/` + id, method: "delete" }), //删除实例
  vmsInstabcesDetail: (id) =>
    request({ url: getApi("vm") + `/instances/` + id, method: "get" }), //实例详情
  vmsnapsListDetail: (id) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/snaps`,
      method: "get",
    }), //实例详情中的快照详情
  volumessnapsListDetail: (id) =>
    request({
      url: getApi("repo") + `/volumes/` + id + `/snaps`,
      method: "get",
    }), //云盘详情中的快照详情
  vmsInstabcesDetail_cloud: (id, config) =>
    request({
      url: getCloudApi(config, "vm") + `/instances/` + id,
      method: "get",
    }), //实例详情
  vmsInstabcesBoundSg: (data, id) =>
    request({
      url: getApi("vm") + `/instances/` + id + "/bound_sgs",

      method: "post",
      data,
    }), //关联实例
  vmsInstabcesBoundSgSort: (data, id) =>
    request({
      url: getApi("vm") + `/instances/` + id + "/bound_sgs",

      method: "put",
      data,
    }), //关联实例 排序
  vmsInstabcesEdit: (data, id) =>
    request({ url: getApi("vm") + `/instances/` + id, method: "put", data }), //修改
  vmsMigratet: (data, id) =>
    request({
      url: getApi("vm") + `/instances/` + id + `/migrate`,

      method: "put",
      data,
    }), //迁移
  vmsRenews: (data) =>
    request({ url: getApi("vm") + `/instances/renews`, method: "post", data }), //恢复

  // 虚机组
  vmInstanceGroupsList: (params) =>
    request({ url: getApi("vm") + `/instance-groups`, method: "get", params }), //虚机组列表
  vmInstanceGroupsAdd: (data) =>
    request({ url: getApi("vm") + `/instance-groups`, method: "post", data }), //添加虚机组
  vmInstanceGroupsDel: (id) =>
    request({ url: getApi("vm") + `/instance-groups/` + id, method: "delete" }), //删除虚机组
  vmInstanceGroupsDelVm: (groupId, id) =>
    request({
      url: getApi("vm") + `/instance-groups/` + groupId + `/` + id,
      method: "delete",
    }), //删除虚机组
  vmInstanceGroupsDetail: (id) =>
    request({ url: getApi("vm") + `/instance-groups/` + id, method: "get" }), //虚机组详情
  vmInstanceGroupsEdit: (data, id) =>
    request({
      url: getApi("vm") + `/instance-groups/` + id,
      method: "put",
      data,
    }), //修改

  // 安全组
  sgsList: (params) =>
    request({ url: getApi("network") + `/sgs`, method: "get", params }), //安全组列表

  sgsAdd: (data) =>
    request({ url: getApi("network") + `/sgs`, method: "post", data }), //添加安全组
  sgsDel: (id) =>
    request({ url: getApi("network") + `/sgs/` + id, method: "delete" }), //删除安全组
  sgsDetail: (id) =>
    request({ url: getApi("network") + `/sgs/` + id, method: "get" }), //安全组详情
  sgsEdit: (data, id) =>
    request({ url: getApi("network") + `/sgs/` + id, method: "put", data }), //修改

  // 安全组规则
  sgsRuleAdd: (data, id) =>
    request({
      url: getApi("network") + `/sgs/` + id + "/rules",

      method: "post",
      data,
    }), //添加安全组规则
  sgsRulesDel: (id) =>
    request({ url: getApi("network") + `/rules/` + id, method: "delete" }), //删除安全组规则
  sgsRulesEdit: (data, sgId, ruleId) =>
    request({
      url: getApi("network") + `/sgs/` + sgId + `/rules/` + ruleId,

      method: "put",
      data,
    }), //修改安全组规则

  // 安全组和实例关联
  sgsBoundAdd: (data, id) =>
    request({
      url: getApi("network") + `/sgs/` + id + "/bound",

      method: "post",
      data,
    }), //关联实例
  sgsUnboundAdd: (data, id) =>
    request({
      url: getApi("network") + `/sgs/` + id + "/unbound",

      method: "post",
      data,
    }), //取消关联实例

  // 监控
  computePanels: (params) =>
    request({
      url: getApi("vm") + `/monitor/compute_panels`,
      method: "get",
      params,
    }), //计算节点详情统计
  vmPanels: (params) =>
    request({
      url: getApi("vm") + `/monitor/vm_panels`,
      method: "get",
      params,
    }), //实例详情统计
  gpuPanels: (params) =>
    request({
      url: getApi("vm") + `/monitor/gpu_panels`,
      method: "get",
      params,
    }), //GPU详情统计

  vmsResourceStats: (params) =>
    request({ url: getApi("vm") + `/resource_stats`, method: "get", params }), //监控总览 ?name=mem&days=7
  vmsUserStorageStats: (params) =>
    request({
      url: getApi("vm") + `/user_storage_stats`,
      method: "get",
      params,
    }), //用户存储
  vmsAllStorageStats: (params) =>
    request({
      url: getApi("vm") + `/all_storage_stats`,
      method: "get",
      params,
    }), //全部存储
  baremetalsBmStats: (params) =>
    request({ url: getApi("bm") + `/bm_stats`, method: "get", params }), //裸金属内容
  vmsVmStats: (params) =>
    request({ url: getApi("vm") + `/vm_stats`, method: "get", params }), //实例内容
  networkNetStats: (params) =>
    request({ url: getApi("network") + `/net_stats`, method: "get", params }), //网络内容
  networkVpcCount: (params) =>
    request({ url: getApi("network") + `/vpc_count`, method: "get", params }), //vpc数量
  networkSubnetCount: (params) =>
    request({
      url: getApi("network") + `/subnet_count`,
      method: "get",
      params,
    }), //子网数量
  vmsMemStats: (params) =>
    request({ url: getApi("vm") + `/mem_stats`, method: "get", params }), //内存统计
  vmsCpuStats: (params) =>
    request({ url: getApi("vm") + `/cpu_stats`, method: "get", params }), //cpu统计
  vmsVmCount: (params) =>
    request({ url: getApi("vm") + `/vm_count`, method: "get", params }), //实例数量

  // volume云盘管理 /instances/{instanceId}/volumes
  volumesList: (params) =>
    request({ url: getApi("repo") + `/volumes`, method: "get", params }), //云盘列表
  volumesList_cloud: (params, config) =>
    request({
      url: getCloudApi(config, "repo") + `/volumes`,
      method: "get",
      params,
    }), //云盘列表
  volumesRecycleList: (params) =>
    request({
      url: getApi("repo") + `/volumes/recycle`,
      method: "get",
      params,
    }), //回收站列表
  volumesAdd: (data) =>
    request({ url: getApi("repo") + `/volumes`, method: "post", data }), //添加云盘
  volumesDel: (id) =>
    request({ url: getApi("repo") + `/volumes/` + id, method: "delete" }), //删除云盘
  volumesDetail: (id) =>
    request({ url: getApi("repo") + `/volumes/` + id, method: "get" }), //云盘详情
  volumesEdit: (data, id) =>
    request({ url: getApi("repo") + `/volumes/` + id, method: "put", data }), //修改
  volumesAttach: (data, id) =>
    request({
      url: getApi("repo") + `/volumes/` + id + "/attach",

      method: "put",
      data,
    }), //挂载
  volumesDetach: (id) =>
    request({
      url: getApi("repo") + `/volumes/` + id + "/detach",
      method: "put",
    }), //卸载
  volumesExport: (data, id) =>
    request({
      url: getApi("repo") + `/volumes/` + id + "/export",

      method: "put",
      data,
    }), //导出
  // volume云盘快照
  volumesSnapsList: (params, id) =>
    request({ url: getApi("repo") + `/volume_snaps`, method: "get", params }), //云盘快照列表
  volumesSnapsAdd: (data, id) =>
    request({ url: getApi("repo") + `/volume_snaps`, method: "post", data }), //添加云盘快照
  volumesSnapsDel: (id) =>
    request({ url: getApi("repo") + `/volume_snaps/` + id, method: "delete" }), //删除云盘快照
  volumesSnapsDetail: (id) =>
    request({ url: getApi("repo") + `/volume_snaps/` + id, method: "get" }), //云盘快照详情
  volumesSnapsEdit: (data, id) =>
    request({
      url: getApi("repo") + `/volume_snaps/` + id,
      method: "put",
      data,
    }), //修改
  volumesSnapsSwitch: (id) =>
    request({
      url: getApi("repo") + `/volume_snaps/` + id + `/switch`,
      method: "put",
    }), //切换

  // 文件存储
  nfsList: (params) =>
    request({ url: getApi("vm") + `/nfs`, method: "get", params }), //文件存储NFS列表

  addnfsList: (data) =>
    request({ url: getApi("vm") + `/nfs`, method: "post", data }), //添加文件存储NFS

  deletenfsList: (id) =>
    request({ url: getApi("vm") + `/nfs/${id}`, method: "delete" }), //删除文件存储NFS

  descnfs: (id) => request({ url: getApi("vm") + `/nfs/${id}`, method: "get" }), //文件存储NFS详情

  putnfs: (data, id) =>
    request({ url: getApi("vm") + `/nfs/${id}`, method: "put", data }), //文件存储NFS修改

  // 存储池

  storagePoolsList: (params) =>
    request({ url: getApi("repo") + `/storage_pools`, method: "get", params }), //存储池列表
  storagePoolsList_cloud: (params, config) =>
    request({
      url: getCloudApi(config, "repo") + `/storage_pools`,
      method: "get",
      params,
    }), //实例详情
  storagePoolsEdit: (data, id) =>
    request({
      url: getApi("repo") + `/storage_pools/` + id,
      method: "put",
      data,
    }), //修改
  storagePoolsDetail: (id) =>
    request({ url: getApi("repo") + `/storage_pools/` + id, method: "get" }), //存储池详情

  // GPU
  pciNodeList: (params) =>
    request({
      url: getApi("vm") + `/pci_devices/available_nodes`,

      method: "get",
      params,
    }), //
  pciDeviceList: (params) =>
    request({ url: getApi("vm") + `/pci_devices`, method: "get", params }), // 节点下的GPU列表
  pciAvailableList: (params) =>
    request({
      url: getApi("vm") + `/pci_devices/available_devices`,

      method: "get",
      params,
    }), //
  pciAttach: (id, data) =>
    request({
      url: getApi("vm") + `/pci_devices/` + id + "/attach",

      method: "put",
      data,
    }), //挂载
  pciDetach: (id, data) =>
    request({
      url: getApi("vm") + `/pci_devices/` + id + "/detach",

      method: "put",
      data,
    }), //卸载

  // 通知对象
  operationReceivers: (params) =>
    request({ url: getApi("operation") + `/receivers`, method: "get", params }), //通知对象-查询
  operationReceiversAdd: (data) =>
    request({ url: getApi("operation") + `/receivers`, method: "post", data }), //通知对象-添加
  operationReceiversDel: (id) =>
    request({
      url: getApi("operation") + `/receivers/` + id,
      method: "delete",
    }), //通知对象-删除
  operationReceiversDetail: (id) =>
    request({ url: getApi("operation") + `/receivers/` + id, method: "get" }), //通知对象-详情
  operationReceiversEdit: (data, id) =>
    request({
      url: getApi("operation") + `/receivers/` + id,
      method: "put",
      data,
    }), //通知对象-修改
  // 报警器
  operationAlarmRules: (params) =>
    request({
      url: getApi("operation") + `/alarm-rules`,
      method: "get",
      params,
    }), //报警器-查询
  operationAlarmRulesAdd: (data) =>
    request({
      url: getApi("operation") + `/alarm-rules`,
      method: "post",
      data,
    }), //报警器-添加
  operationAlarmRulesDel: (id) =>
    request({
      url: getApi("operation") + `/alarm-rules/` + id,
      method: "delete",
    }), //报警器-删除
  operationAlarmRulesDetail: (id) =>
    request({ url: getApi("operation") + `/alarm-rules/` + id, method: "get" }), //报警器-详情
  operationAlarmRulesEdit: (data, id) =>
    request({
      url: getApi("operation") + `/alarm-rules/` + id,

      method: "put",
      data,
    }), //报警器-修改
  // 运维管理-报警
  operationAlarmInfosList: (params) =>
    request({
      url: getApi("operation") + `/alarm-infos`,
      method: "get",
      params,
    }), //运维管理-报警-分页查询
  operationAlarmMarkResolvedEdit: (data) =>
    request({
      url: getApi("operation") + `/alarm-mark-resolved`,

      method: "put",
      data,
    }), //运维管理-报警-标记解决

  operationAlarmDistribution: (params) =>
    request({
      url: getApi("operation") + `/alarm-distribution`,

      method: "get",
      params,
    }), //运维管理-报警--近一周报警分布
  operationAlarmStatistics: (params) =>
    request({
      url: getApi("operation") + `/alarm-statistics`,
      method: "get",
      params,
    }), //运维管理-报警-近一周报警统计

  // 事件
  operationEventsList: (params) =>
    request({ url: getApi("operation") + `/events`, method: "get", params }), //事件
  // 日志
  operationLogsList: (params) =>
    request({
      url: process.env.VUE_APP_BASE_API + "api/omc/v1" + `/apilogs`,
      method: "get",
      params,
    }), //日志

  // 负载均衡
  loadBalancersList: (params) =>
    request({
      url: getApi("network") + `/loadbalancers`,
      method: "get",
      params,
    }), //负载均衡列表
  loadBalancersAdd: (data) =>
    request({
      url: getApi("network") + `/loadbalancers`,
      method: "post",
      data,
    }), //添加负载均衡
  loadBalancersDel: (id) =>
    request({
      url: getApi("network") + `/loadbalancers/` + id,
      method: "delete",
    }), //删除负载均衡
  loadBalancersDetail: (id) =>
    request({ url: getApi("network") + `/loadbalancers/` + id, method: "get" }), //负载均衡详情
  loadBalancersEdit: (data, id) =>
    request({
      url: getApi("network") + `/loadbalancers/` + id,

      method: "put",
      data,
    }), //修改负载均衡

  // 负载均衡后端服务
  loadBalancersBackendsList: (params) =>
    request({ url: getApi("network") + `/backends`, method: "get", params }), //负载均衡后端服务列表
  loadBalancersBackendsAdd: (data) =>
    request({ url: getApi("network") + `/backends`, method: "post", data }), //添加负载均衡后端服务
  loadBalancersBackendsDel: (id) =>
    request({ url: getApi("network") + `/backends/` + id, method: "delete" }), //删除负载均衡后端服务
  loadBalancersBackendsEdit: (data, id) =>
    request({
      url: getApi("network") + `/backends/` + id,
      method: "put",
      data,
    }), //修改负载均衡后端服务

  // 负载均衡监听器
  loadBalancersFrontendsList: (params) =>
    request({ url: getApi("network") + `/frontends`, method: "get", params }), //负载均衡监听器列表
  loadBalancersFrontendsAdd: (data) =>
    request({ url: getApi("network") + `/frontends`, method: "post", data }), //添加负载均衡监听器
  loadBalancersFrontendsDel: (id) =>
    request({ url: getApi("network") + `/frontends/` + id, method: "delete" }), //删除负载均衡监听器
  loadBalancersFrontendsEdit: (data, id) =>
    request({
      url: getApi("network") + `/frontends/` + id,
      method: "put",
      data,
    }), //修改负载均衡监听器
};

export default mainApi;
