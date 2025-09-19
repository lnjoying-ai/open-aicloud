import request from "@/utils/request";
import store from "@/store";

const umsApi = process.env.VUE_APP_BASE_API + "api/ums/v1";
const ecrmApi = process.env.VUE_APP_BASE_API + "api/ecrm/v1";
const cisApi = process.env.VUE_APP_BASE_API + "api/cis/v1";
const aosApi = process.env.VUE_APP_BASE_API + "api/aos/v1";
const imsApi = process.env.VUE_APP_BASE_API + "api/ims/v1";
const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";
const clsApi = process.env.VUE_APP_BASE_API + "api/cls/v1";
const sysApi = process.env.VUE_APP_BASE_API + "api/sys/v1";
const cmpApi = process.env.VUE_APP_BASE_API + "api/cmp/v1";
const omcApi = process.env.VUE_APP_BASE_API + "api/omc/v1";
const serviceManagerApi =
  process.env.VUE_APP_BASE_API + "api/service-manager/v1";

export function getBps(params) {
  return request({
    url: umsApi + "/bps",
    method: "get",
    params,
  });
}

export function addBps(data) {
  return request({
    url: umsApi + "/bps",
    method: "post",
    data,
  });
}

export function editBps(params, data) {
  return request({
    url: umsApi + "/bps/" + params,
    method: "put",
    data,
  });
}
export function delBps(params) {
  return request({
    url: umsApi + "/bps/" + params,
    method: "delete",
  });
}
export function bpsUniqueness(data) {
  return request({
    url: umsApi + "/bps/" + "uniqueness",
    method: "PUT",
    data,
  });
}
export function getUsers(params) {
  return request({
    url: umsApi + "/users",
    method: "get",
    params,
  });
}
export function addUsers(data) {
  return request({
    url: umsApi + "/users",
    method: "post",
    data,
  });
}
export function editUsers(params, data) {
  return request({
    url: umsApi + "/users/" + params,
    method: "put",
    data,
  });
}
export function delUsers(params) {
  return request({
    url: umsApi + "/users/" + params,
    method: "delete",
  });
}
export function userUniqueness(data) {
  return request({
    url: umsApi + "/users/" + "uniqueness",
    method: "put",
    data,
  });
}
export function getRegions(params) {
  return request({
    url: ecrmApi + "/regions",
    method: "get",
    params,
  });
}
export function getUserRegions(params) {
  return request({
    url: imsApi + "/docker/registries/regions",
    method: "get",
    params,
  });
}
export function addRegions(data) {
  return request({
    url: ecrmApi + "/regions",
    method: "post",
    data,
  });
}

export function editRegions(params, data) {
  return request({
    url: ecrmApi + "/regions/" + params,
    method: "put",
    data,
  });
}
export function delRegions(params) {
  return request({
    url: ecrmApi + "/regions/" + params,
    method: "delete",
  });
}

export function getSites(params) {
  return request({
    url: ecrmApi + "/sites",
    method: "get",
    params,
  });
}
export function editSites(params, data) {
  return request({
    url: ecrmApi + "/sites/" + params,
    method: "put",
    data,
  });
}
export function delSites(params) {
  return request({
    url: ecrmApi + "/sites/" + params,
    method: "delete",
  });
}
export function addSites(data) {
  return request({
    url: ecrmApi + "/sites",
    method: "post",
    data,
  });
}
export function gws(params) {
  return request({
    url: ecrmApi + "/gws",
    method: "get",
    params,
  });
}

export function addgws(data) {
  return request({
    url: ecrmApi + "/gws",
    method: "post",
    data,
  });
}
export function delgws(params) {
  return request({
    url: ecrmApi + "/gws/" + params,
    method: "delete",
  });
}
export function deactivegws(params) {
  return request({
    url: ecrmApi + "/gws/" + params + "?action=deactive",
    method: "post",
  });
}
export function activegws(params) {
  return request({
    url: ecrmApi + "/gws/" + params + "?action=active",
    method: "post",
  });
}
export function rebootgws(params) {
  return request({
    url: ecrmApi + "/gws/" + params + "?action=reboot",
    method: "post",
  });
}
export function edgesUpgrade(params, nodeId) {
  //节点升级
  return request({
    url: ecrmApi + "/edges/" + nodeId + "/upgrade",
    method: "post",
    params,
  });
}
export function edgesVersions() {
  //节点版本
  return request({
    url: ecrmApi + "/edges/versions",
    method: "get",
  });
}
export function edges(params) {
  //节点列表
  return request({
    url: ecrmApi + "/edges",
    method: "get",
    params,
  });
}
// 节点详情
export function edgesDetail(id) {
  return request({
    url: ecrmApi + "/edges/" + id,
    method: "get",
  });
}
// 修改节点部分信息
export function updateEdgesDetail(id, data) {
  return request({
    url: ecrmApi + "/edges/" + id,
    method: "patch",
    data,
  });
}
// 节点上镜像查询
export function getImagesFromEdges(id) {
  return request({
    url: ecrmApi + "/edges/" + id + "/images",
    method: "get",
  });
}
export function deledges(params) {
  return request({
    url: ecrmApi + "/edges/" + params,
    method: "delete",
  });
}
export function deactiveedges(params) {
  return request({
    url: ecrmApi + "/edges/" + params + "?action=deactive",
    method: "post",
  });
}
export function activeedges(params) {
  return request({
    url: ecrmApi + "/edges/" + params + "?action=active",
    method: "post",
  });
}
export function rebootedges(params) {
  return request({
    url: ecrmApi + "/edges/" + params + "?action=reboot",
    method: "post",
  });
}

export function addEdges(data) {
  return request({
    url: ecrmApi + "/edges",
    method: "post",
    data,
  });
}
//----------7.8.service-agent管理-----
// 7.8.2.service-agent列表
// GET /service-agent
export function serviceAgent(params) {
  return request({
    url: ecrmApi + "/service-agents",
    method: "get",
    params,
  });
}
// 7.8.3.新建service-agent
// POST /service-agent
export function addServiceAgent(data) {
  return request({
    url: ecrmApi + "/service-agents",
    method: "post",
    data,
  });
}
// 7.8.4.删除service-agent
// DELETE /service-agent/{agent_id}
export function delServiceAgent(params) {
  return request({
    url: ecrmApi + "/service-agents/" + params,
    method: "delete",
  });
}

export function instances(params) {
  return request({
    url: cisApi + "/docker/instances",
    method: "get",
    params,
  });
}

export function addinstances(data) {
  return request({
    url: cisApi + "/docker/instances",
    method: "POST",
    data,
  });
}

export function setInstances(val, params) {
  return request({
    url: cisApi + "/docker/instances/" + val,
    method: "POST",
    params,
  });
}
export function setInstances01(val, data) {
  return request({
    url: cisApi + "/docker/instances/" + val,
    method: "POST",
    data,
  });
}
export function statsInstances(data) {
  return request({
    url: cisApi + "/docker/instances/" + data + "/stats",
    method: "GET",
  });
}
export function getInstances(data) {
  return request({
    url: cisApi + "/docker/instances/" + data,
    method: "GET",
  });
}
export function delInstances(data) {
  return request({
    url: cisApi + "/docker/instances/" + data,
    method: "delete",
  });
}
export function nations() {
  return request({
    url: ecrmApi + "/nations",
    method: "get",
  });
}
export function divistions(data1, data2) {
  return request({
    url: ecrmApi + "/divisions/" + data1 + "/" + data2,
    method: "get",
  });
}
// 仓库管理
export function registries(params) {
  return request({
    url: imsApi + "/docker/registries",
    method: "get",
    params,
  });
}
// 删除仓库
export function delRegistries(params) {
  return request({
    url: imsApi + "/docker/registries/" + params,
    method: "DELETE",
  });
}
// 停用仓库

export function deactivegck(params, data) {
  return request({
    url: imsApi + "/docker/registries/" + params + "?action=deactive",
    method: "post",
    data,
  });
}
// 启用仓库
export function activegck(params, data) {
  return request({
    url: imsApi + "/docker/registries/" + params + "?action=active",
    method: "post",
    data,
  });
}
// 按照仓库id查看仓库所属区域的设备
export function getnodesbyId(params) {
  return request({
    url: imsApi + "/docker/registries/" + params + "/nodes",
    method: "get",
  });
}

// 按照第三方仓库id查看仓库所属区域的设备
export function getnodesby3rdId(params) {
  return request({
    url: imsApi + "/docker/3rd-registries/" + "nodes",
    method: "get",
    params,
  });
}
// 12.1.39.镜像预下载查询
export function getImagesPreDownloads(params) {
  return request({
    url: imsApi + "/docker/registries/pre-downloads",
    method: "get",
    params,
  });
}
export function delImagesPreDownloads(params) {
  return request({
    url: imsApi + "/docker/registries/pre-downloads/jobs",
    method: "delete",
    params,
  });
}
// 镜像预下载
export function predown(registry_id, data) {
  return request({
    url: imsApi + "/docker/registries/" + registry_id + "/pre-download",
    method: "post",
    data,
  });
}
//第三方镜像预下载
export function predown3rd(registry_id, data) {
  return request({
    url: imsApi + "/docker/3rd-registries/" + registry_id + "/pre-download",
    method: "post",
    data,
  });
}
// 修改管理员密码
export function passwordck(params, date) {
  return request({
    url: imsApi + "/docker/registries/" + params + "/password",
    method: "put",
    date,
  });
}
// 获取指定仓库
export function registriesById(params) {
  return request({
    url: imsApi + "/docker/registries/" + params,
    method: "get",
  });
}

// 获取指定第三方仓库
export function registries3rdById(params) {
  return request({
    url: imsApi + "/docker/3rd-registries/" + params,
    method: "get",
  });
}
export function addRegistries(data) {
  return request({
    url: imsApi + "/docker/registries",
    method: "post",
    data,
  });
}
// 修改
export function editRegistries(params, data) {
  return request({
    url: imsApi + "/docker/registries/" + params,
    method: "put",
    data,
  });
}

export function registriesPassword(params, data) {
  return request({
    url: imsApi + "/docker/registries/" + params + "/password",
    method: "put",
    data,
  });
}
// 仓库用户列表

export function registriesUser(params) {
  return request({
    url: imsApi + "/docker/registries/users",
    method: "get",
    params,
  });
}

export function addRegistriesUser(data) {
  return request({
    url: imsApi + "/docker/registries/users",
    method: "post",
    data,
  });
}
// 判断用户是否存在
export function usersExist(params) {
  return request({
    url: imsApi + "/docker/registries/users/exist",
    method: "get",
    params,
  });
}

// 项目列表
export function projects(registry_id, params) {
  return request({
    url: imsApi + "/docker/registries/" + registry_id + "/projects",
    method: "get",
    params,
  });
}
// 按照仓库id查看可预下载的镜像仓库列表
export function reposjcck(registry_id, params) {
  return request({
    url: imsApi + "/docker/registries/" + registry_id + "/repos",
    method: "get",
    params,
  });
}
// 添加项目 /docker/registries/{registry_id}/projects
export function addProjects(params, data) {
  return request({
    url: imsApi + "/docker/registries/" + params + "/projects",
    method: "post",
    data,
  });
}
// 修改项目
export function editProjects(registry_id, project_id, data) {
  return request({
    url:
      imsApi + "/docker/registries/" + registry_id + "/projects/" + project_id,
    method: "put",
    data,
  });
}
// 删除项目
export function delProjects(registry_id, project_id) {
  return request({
    url:
      imsApi + "/docker/registries/" + registry_id + "/projects/" + project_id,
    method: "DELETE",
  });
}
// 镜像仓库列表
export function repositories(registry_id, project_id, params) {
  return request({
    url:
      imsApi +
      "/docker/registries/" +
      registry_id +
      "/projects/" +
      project_id +
      "/repositories",
    method: "GET",
    params,
  });
}
// 更新镜像仓库信息
export function upDateRepo(registry_id, project_id, repo_name, data) {
  return request({
    url:
      imsApi +
      "/docker/registries/" +
      registry_id +
      "/projects/" +
      project_id +
      "/repositories/" +
      repo_name,
    method: "PUT",
    data,
  });
}
// 根据仓库名查看镜像仓库信息
export function getReoInfo(registry_id, project_id, repo_name) {
  return request({
    url:
      imsApi +
      "/docker/registries/" +
      registry_id +
      "/projects/" +
      project_id +
      "/repositories/" +
      repo_name,
    method: "get",
  });
}

// 获取指定镜像仓库下的tag
export function getTags(registry_id, project_id, repo_name, params) {
  return request({
    url:
      imsApi +
      "/docker/registries/" +
      registry_id +
      "/projects/" +
      project_id +
      "/repositories/" +
      repo_name +
      "/tags",
    method: "get",
    params,
  });
}
// 删除指定镜像仓库下的tag
export function delTags(registry_id, project_id, repo_name, tag_name) {
  return request({
    url:
      imsApi +
      "/docker/registries/" +
      registry_id +
      "/projects/" +
      project_id +
      "/repositories/" +
      repo_name +
      "/tags/" +
      tag_name,
    method: "DELETE",
  });
}
// 12.1.28.	查看镜像仓库构建历史
export function getBuildHistory(registry_id, project_id, repo_name, params) {
  return request({
    url:
      imsApi +
      "/docker/registries/" +
      registry_id +
      "/projects/" +
      project_id +
      "/repositories/" +
      repo_name +
      "/build-history",
    method: "get",
    params,
  });
}
// 租户指令
export function getcommand(data) {
  return request({
    url: imsApi + "/docker/registries/command",
    method: "post",
    data,
  });
}
// 通过镜像名称搜索镜像

export function getReposList(params) {
  return request({
    url: imsApi + "/docker/registries/repos",
    method: "get",
    params,
  });
}

// 第三方仓库

export function registries3rd(params) {
  return request({
    url: imsApi + "/docker/3rd-registries",
    method: "get",
    params,
  });
}
// 添加第三方仓库
export function add3rd(data) {
  return request({
    url: imsApi + "/docker/3rd-registries",
    method: "post",
    data,
  });
}
// 删除第三方仓库
export function del3rd(params) {
  return request({
    url: imsApi + "/docker/3rd-registries/" + params,
    method: "DELETE",
  });
}
// 修改第三方仓库
export function edit3rd(params, data) {
  return request({
    url: imsApi + "/docker/3rd-registries/" + params,
    method: "PUT",
    data,
  });
}
// 配置节点
export function configEdges(node_id, data) {
  return request({
    url: ecrmApi + "/edges/" + node_id,
    method: "PUT",
    data,
  });
}
// 应用模板
export function addTemplates(data) {
  return request({
    url: aosApi + "/templates",
    method: "POST",
    data,
  });
}
export function templates(params) {
  return request({
    url: aosApi + "/templates",
    method: "get",
    params,
  });
}
//组件中心模板用
export function templatesTags(params) {
  return request({
    url: aosApi + "/templates?labels=" + params,
    method: "get",
  });
}
export function templatesInfo(id) {
  return request({
    url: aosApi + "/templates/" + id,
    method: "get",
  });
}
export function templatesArchives(id) {
  return request({
    url: aosApi + "/templates/" + id + "/archives",
    method: "get",
  });
}
export function editTemplates(data, params) {
  return request({
    url: aosApi + "/templates/" + params,
    method: "put",
    data,
  });
}

export function delTemplates(name, id) {
  return request({
    url: aosApi + "/templates?name=" + name + "&id=" + id,
    method: "DELETE",
  });
}
export function appTemplatesInfo(id) {
  return request({
    url: aosApi + "/templates/versions/" + id,
    method: "get",
  });
}
// 应用
export function getDockerStacks(params) {
  return request({
    url: aosApi + "/docker/stacks",
    method: "GET",
    params,
  });
}
export function getDockerStacksNode(params) {
  return request({
    url: cisApi + "/docker/stacks",
    method: "GET",
    params,
  });
}
export function addDockerStacks(data) {
  return request({
    url: aosApi + "/docker/stacks",
    method: "POST",
    data,
  });
}

export function delDockerStacks(id) {
  return request({
    url: aosApi + "/docker/stacks/" + id,
    method: "DELETE",
  });
}

export function setDockerStacks(id, params) {
  return request({
    url: aosApi + "/docker/stacks/" + id,
    method: "POST",
    params,
  });
}
export function getDockerStacksConfig(id) {
  // 查看应用配置
  return request({
    url: aosApi + "/docker/stacks/" + id + "/compose-config",
    method: "GET",
  });
}
export function getDockerStacksArchives(id) {
  // 导出应用配置文件
  return aosApi + "/docker/stacks/" + id + "/compose-archives";
}
export function getDockerStacksInfo(id) {
  // 获取应用信息
  return request({
    url: aosApi + "/docker/stacks/" + id,
    method: "GET",
  });
}
export function getDockerStacksServices(id, params) {
  // 根据应用id获取服务列表
  return request({
    url: aosApi + "/docker/stacks/" + id + "/services",
    method: "GET",
    params,
  });
}
//部署容器模式
export function getDeploys(params) {
  return request({
    url: cisApi + "/docker/deployments",
    method: "GET",
    params,
  });
}
//部署应用模式
export function getApplicationDeploys(params) {
  return request({
    url: aosApi + "/docker/deployments",
    method: "GET",
    params,
  });
}
// 部署容器
export function getDeploysInstances(id, params) {
  return request({
    url: cisApi + "/docker/deployments/" + id + "/instances",
    method: "GET",
    params,
  });
}
// 部署应用
export function getDeploysSystemInstances(id, params) {
  return request({
    url: aosApi + "/docker/deployments/" + id + "/stacks",
    method: "GET",
    params,
  });
}
//删除部署容器
export function delDeployContainer(id) {
  return request({
    url: cisApi + "/docker/deployments/" + id,
    method: "DELETE",
  });
}
//删除部署应用
export function delDeploySystem(id) {
  return request({
    url: aosApi + "/docker/deployments/" + id,
    method: "DELETE",
  });
}
// 服务
export function getDockerServices(params) {
  return request({
    url: aosApi + "/docker/services",
    method: "GET",
    params,
  });
}

export function delDockerServices(id) {
  return request({
    url: aosApi + "/docker/services/" + id,
    method: "DELETE",
  });
}

export function setDockerServices(id, params) {
  return request({
    url: aosApi + "/docker/services/" + id,
    method: "POST",
    params,
  });
}

export function getDockerServicesInstances(id) {
  // 根据服务id获取容器列表
  return request({
    url: aosApi + "/docker/services/" + id + "/instances",
    method: "GET",
  });
}
// 账单
export function getContainerDetail(bill_cycle, params) {
  // 容器
  return request({
    url: billApi + "/bills/months/" + bill_cycle + "/container/detail",
    method: "GET",
    params,
  });
}
export function getStackDetail(bill_cycle, params) {
  // 应用
  return request({
    url: billApi + "/bills/months/" + bill_cycle + "/stack/detail",
    method: "GET",
    params,
  });
}
// 统计
export function getstatsBps(params, id) {
  // 查看业务组织机构的资源占用
  return request({
    url:
      omcApi +
      "/stats/bps/" +
      id +
      "/stat-infos?start_time=" +
      params.start_time +
      "&end_time=" +
      params.end_time,
    method: "GET",
  });
}
export function getstatsUsers(params, id) {
  // 查看某个用户的资源占用
  return request({
    url:
      omcApi +
      "/stats/users/" +
      id +
      "/stat-infos?start_time=" +
      params.start_time +
      "&end_time=" +
      params.end_time,
    method: "GET",
  });
}
export function getstatsNodes(params, id) {
  // 节点资源使用查看
  return request({
    url:
      omcApi +
      "/stats/nodes/" +
      id +
      "?start_time=" +
      params.start_time +
      "&end_time=" +
      params.end_time,
    method: "GET",
  });
}
export function getstatsNodesInfos(params) {
  // 节点资源全量使用统计
  return request({
    url:
      omcApi +
      "/stats/nodes/stat-infos?start_time=" +
      params.start_time +
      "&end_time=" +
      params.end_time,
    method: "GET",
  });
}
export function getstatsInstances(params, id) {
  // 查看某个用户的资源占用
  return request({
    url:
      omcApi +
      "/stats/instances/" +
      id +
      "?start_time=" +
      params.start_time +
      "&end_time=" +
      params.end_time,
    method: "GET",
  });
}

export function getstatsStacksInfos(params, id) {
  // 查看应用资源消耗统计
  return request({
    url:
      omcApi +
      "/stats/stacks/" +
      id +
      "?start_time=" +
      params.start_time +
      "&end_time=" +
      params.end_time,
    method: "GET",
  });
}
export function setEcrmEdges(id, params) {
  return request({
    url: ecrmApi + "/edges/" + id + "/confirm",
    method: "POST",
    params,
  });
}
//获取标签labeloption
export function getLabelOption(type) {
  return request({
    url: ecrmApi + "/labels/show-inputs/" + type,
    method: "get",
  });
}
//获取污点labeloption
export function getTaintOptions(type) {
  return request({
    url: ecrmApi + "/taints/show-inputs/" + type,
    method: "get",
  });
}
// dashboard接口
export function getNamespace(id) {
  return request({
    url:
      process.env.VUE_APP_BASE_API +
      "dashboard/clusters/" +
      id +
      "/api/v1/namespace",
    method: "GET",
  });
}
export function getkubectlWssID(id, data) {
  return request({
    url:
      process.env.VUE_APP_BASE_API +
      "dashboard/clusters/" +
      id +
      "/api/v1/pod/" +
      data.namespace +
      "/" +
      data.pod_name +
      "/shell/" +
      data.container_name,
    method: "GET",
  });
}
// ** **
// ** cluster service分系统的web接口 begin
// ** **
//集群模板获取集群名称
export function clustersVersions(data) {
  return request({
    url: clsApi + "/clusters/versions?type=" + (data ? data : ""),
    method: "get",
  });
}
//集群列表
export function getClusters(params) {
  return request({
    url: clsApi + "/clusters",
    method: "get",
    params,
  });
}
//集群下节点列表
export function getClustersToNodes(id) {
  return request({
    url: clsApi + "/clusters/nodes/" + id,
    method: "get",
  });
}
export function getClustersTerminal(id) {
  return request({
    url: clsApi + "/clusters/" + id + "/terminal",
    method: "get",
  });
}
//删除集群
export function delClusters(data) {
  return request({
    url: clsApi + "/clusters/" + data,
    method: "delete",
  });
}
//6.1.9.获取集群安装的设备
export function getClustersNodes(params, id) {
  return request({
    url: clsApi + "/clusters/" + id + "/nodes",
    method: "get",
    params,
  });
}
//6.1.5.获得指定集群信息
export function getClustersDetail(data) {
  return request({
    url: clsApi + "/clusters/" + data,
    method: "get",
  });
}
export function getClustersImportCmds(id) {
  return request({
    url: clsApi + "/clusters/" + id + "/import-cmds",
    method: "get",
  });
}
//6.1.17.获得集群服务状态
export function coreServiceMetrics(id) {
  return request({
    url: clsApi + "/clusters/" + id + "/core-service-metrics",
    method: "get",
  });
}
//修改集群
export function putClustersDetail(id, data) {
  return request({
    url: clsApi + "/clusters/" + id,
    method: "put",
    data,
  });
}
//获取导入集群的命令
export function importCmds(clusterId) {
  return request({
    url: clsApi + "/clusters/" + clusterId + "/import-cmds",
    method: "get",
  });
}
//创建集群
export function postClusters(data) {
  return request({
    url: clsApi + "/clusters",
    method: "post",
    data,
  });
}

//创建模板
export function getTemplates(data) {
  return request({
    url: clsApi + "/templates",
    method: "post",
    data,
  });
}
//修改模板
export function reviseTemplates(params, data) {
  return request({
    url: clsApi + "/templates/" + params,
    method: "PUT",
    data,
  });
}
//删除模板
export function deleteTemplates(template_id) {
  return request({
    url: clsApi + "/templates/" + template_id,
    method: "DELETE",
  });
}
//创建模板版本
export function getTemplatesVersions(data) {
  return request({
    url: clsApi + "/templates/versions",
    method: "post",
    data,
  });
}

//修改模板版本
export function reviseTemplatesVersions(version_id, data) {
  return request({
    url: clsApi + "/templates/versions/" + version_id,
    method: "PUT",
    data,
  });
}
//删除模板版本
export function deleteTemplatesVersions(version_id) {
  return request({
    url: clsApi + "/templates/versions/" + version_id,
    method: "DELETE",
  });
}
//获取指定模板信息
export function templatesInformation(template_id) {
  return request({
    url: clsApi + "/templates/" + template_id,
    method: "get",
  });
}
//获取模板列表
export function k8stemplates(params) {
  return request({
    url: clsApi + "/templates",
    method: "get",
    params,
  });
}
//获取指定版本的模板
export function templatesVersions(version_id) {
  return request({
    url: clsApi + "/templates/versions/" + version_id,
    method: "get",
  });
}
//导出集群模板
export function exportClusterTemplates(id, data) {
  return request({
    url: clsApi + "/clusters/" + id + "?action=export_template",
    method: "POST",
    data,
  });
}
//获取集群kubeconfig信息
export function kubeconfig(clusterId) {
  return request({
    url: clsApi + "/clusters/" + clusterId + "/kubeconfig",
    method: "get",
  });
}
//获取容器操作日志
export function Operation(instId) {
  return request({
    url: cisApi + "/docker/instances/" + instId + "/oper-logs",
    method: "get",
  });
}
//获取容器运行日志
export function Runlog(instId) {
  return request({
    url: cisApi + "/docker/instances/" + instId + "/run-logs",
    method: "get",
  });
}
//创建节点上的应用
export function createstacks(data) {
  return request({
    url: aosApi + "/nodes/stacks",
    method: "POST",
    data,
  });
}
//查询节点上的应用
export function nodesStacks(stackId) {
  return request({
    url: aosApi + "/nodes/stacks/" + stackId,
    method: "get",
  });
}
//删除节点上的应用
export function deleteStacks(stackId) {
  return request({
    url: aosApi + "/nodes/stacks/" + stackId,
    method: "DELETE",
  });
}
export function getDeleteStacksUrl(stackId) {
  return aosApi + "/nodes/stacks/" + stackId;
}
//信息查重
export function uniqueness(data) {
  return request({
    url: ecrmApi + "/uniqueness",
    method: "POST",
    data,
  });
}
// ** **
// ** cluster service分系统的web接口 end
// ** **

//14.2.2.配置列表
export function configs(params) {
  return request({
    url: sysApi + "/configs",
    method: "get",
    params,
  });
}
//14.2.2.发布配置
export function toConfigs(data) {
  return request({
    url: sysApi + "/configs",
    method: "POST",
    data,
  });
}

// 应用市场start
// 获取应用列表
export function helmCharts(params) {
  return request({
    url: aosApi + "/helm/charts",
    method: "get",
    params,
  });
}
export function helmUploadCharts(data, name) {
  return request({
    url: aosApi + "/helm/charts?repo_name=" + name,
    method: "post",
    data,
  });
}
export function delHelmCharts(id) {
  return request({
    url: aosApi + "/helm/charts/" + id,
    method: "delete",
  });
}
export function helmChartsDetail(id) {
  return request({
    url: aosApi + "/helm/charts/" + id,
    method: "get",
  });
}
export function helmChartsVersions(id) {
  return request({
    url: aosApi + "/helm/charts/" + id + "/versions",
    method: "get",
  });
}
export function helmChartsVersionsFiles(version, id) {
  return request({
    url: aosApi + "/helm/charts/" + id + "/versions/" + version + "/files",
    method: "get",
  });
}

// 获取已安装应用列表
export function helmStacks(params) {
  return request({
    url: aosApi + "/helm/stacks",
    method: "get",
    params,
  });
}
export function helmStacksDetail(id) {
  return request({
    url: aosApi + "/helm/stacks/" + id,
    method: "get",
  });
}
export function delHelmStacks(id) {
  return request({
    url: aosApi + "/helm/stacks/" + id,
    method: "delete",
  });
}
export function helmStatcks(data) {
  return request({
    url: aosApi + "/helm/stacks",
    method: "post",
    data,
  });
}
// 获取repo列表
export function helmRepos(params) {
  return request({
    url: aosApi + "/helm/repos",
    method: "get",
    params,
  });
}
export function helmChartsStats(params) {
  return request({
    url: aosApi + "/helm/charts/stats",
    method: "get",
    params,
  });
}
export function addHelmRepos(data) {
  return request({
    url: aosApi + "/helm/repos",
    method: "post",
    data,
  });
}
export function HelmReposDetail(name) {
  return request({
    url: aosApi + "/helm/repos/" + name,
    method: "get",
  });
}
export function delHelmRepos(name) {
  return request({
    url: aosApi + "/helm/repos/" + name,
    method: "delete",
  });
}
//系统级分类统计
export function statsSystemsMetrics(params) {
  return request({
    url: omcApi + "/stats/systems/metrics",
    method: "get",
    params,
  });
}
//节点状态统计
export function statsNodesStatusMetrics(data) {
  return request({
    url: omcApi + "/stats/nodes/status/metrics",
    method: "post",
    data,
  });
}
//15.2.节点资源消耗信息
export function statsNodesMetrics(params) {
  return request({
    url: omcApi + "/stats/nodes/" + params.node_id + "/metrics",
    method: "get",
    params,
  });
}
//15.5.查看业务伙伴的物理资源占用
export function statsBpsMetrics(params) {
  return request({
    url: omcApi + "/stats/bps/" + params.bps_id + "/metrics",
    method: "get",
    params,
  });
}
//15.5.查看用户的物理资源占用
export function statsUserMetrics(params) {
  return request({
    url: omcApi + "/stats/users/" + params.user_id + "/metrics",
    method: "get",
    params,
  });
}
//15.5.查看容器的物理资源占用
export function statsInstancesMetrics(params) {
  return request({
    url: omcApi + "/stats/instances/" + params.inst_id + "/metrics",
    method: "get",
    params,
  });
}
//15.5.查看应用的资源消耗
export function statsStackMetrics(params) {
  return request({
    url: omcApi + "/stats/stack/" + params.stack_id + "/metrics",
    method: "get",
    params,
  });
}
//7.4.21 获取设备的二维码
export function getNodeQRCode(params) {
  return request({
    url: ecrmApi + "/edges/qrcodes/" + params,
    method: "get",
  });
}
//8.18 根据配置数据检索容器
export function getConfiguerrContainer(params) {
  return request({
    url: cisApi + "/docker/cfgs/",
    method: "get",
    params,
  });
}
//10.32 根据用户配置检索应用
export function getConfiguerApp(params) {
  return request({
    url: aosApi + "/docker/stacks/cfgs",
    method: "get",
    params,
  });
}
//10.33 更新应用配置文件
export function upDateConfiguerApp(data) {
  return request({
    url: aosApi + "/docker/stacks/cfgs",
    method: "PUT",
    data,
  });
}
//8.19 更新容器配置文件
export function upDateConfiguerContainer(data) {
  return request({
    url: cisApi + "/docker/cfgs",
    method: "PUT",
    data,
  });
}
//14.2.4 删除配置
export function delConfiguer(params) {
  return request({
    url: sysApi + "/configs/",
    method: "DELETE",
    params,
  });
}

//多云管理查询（管理员）
export function getCloudys(params) {
  return request({
    url: cmpApi + "/clouds",
    method: "get",
    params,
  });
}
//多云管理查询
export function getCloudysInfo(params) {
  return request({
    url: cmpApi + "/clouds/basic-info",
    method: "get",
    params,
  });
}
//修改多云列表
export function updateCloudys(cloudID, data) {
  return request({
    url: cmpApi + "/clouds/" + cloudID,
    method: "put",
    data,
  });
}
//添加多云列表
export function addCloudys(data) {
  return request({
    url: cmpApi + "/clouds",
    method: "POST",
    data,
  });
}
//删除多云列表
export function delClouds(cloudID, data) {
  return request({
    url: cmpApi + "/clouds/" + cloudID + "?force=" + data,
    method: "delete",
  });
}
//测试cloud访问地址连通性
export function cloudsPing() {
  return request({
    url: cmpApi + "/mcc/clouds/ping",
    method: "get",
  });
}
//16.9.修改cloud状态
export function actionCloudys(cloudID, data) {
  return request({
    url: cmpApi + "/clouds/" + cloudID + "/action",
    method: "POST",
    data,
  });
}
//查看集群分类统计
export function statsClustersMetrics() {
  return request({
    url: omcApi + "/stats/clusters/metrics",
    method: "get",
  });
}
//查看多云分类统计
export function statsCloudsMetrics(params) {
  return request({
    url: omcApi + "/stats/clouds/metrics",
    method: "get",
    params,
  });
}
//集群状态统计
export function statsStatusMetrics(params) {
  return request({
    url: omcApi + "/stats/status/metrics",
    method: "get",
    params,
  });
}
//节点资源总量查询
export function statsNodesSort(params) {
  return request({
    url: omcApi + "/stats/nodes/metrics/sort",
    method: "get",
    params,
  });
}
//节点解绑
export function unbindsNodes(data) {
  return request({
    url: ecrmApi + "/edges/unbinds",
    method: "POST",
    data,
  });
}
//集群状态修复
export function clustersRepair(clusterID, data) {
  return request({
    url: clsApi + "/clusters/repair/" + clusterID,
    method: "POST",
    data,
  });
}

// ------------------19服务管理------------------
// 19.2.服务网关列表
// 请求：GET /service-gateways
export function getServiceGateways(params) {
  return request({
    url: serviceManagerApi + "/service-gateways",
    method: "get",
    params,
  });
}
//
export function getServiceGatewayDetail(params) {
  return request({
    url: serviceManagerApi + "/service-gateways/detail",
    method: "get",
    params,
  });
}
export function getServiceGatewaysDetail(id) {
  return request({
    url: serviceManagerApi + "/service-gateways/" + id,
    method: "get",
  });
}
// 19.3.更新服务网关
// 请求：PUT  /service-gateways/{service_gateway_id }
export function editServiceGateways(service_gateway_id, data) {
  return request({
    url: serviceManagerApi + "/service-gateways/" + service_gateway_id,
    method: "put",
    data,
  });
}
// 19.4.添加服务端口
// 请求：POST  /service-ports
export function addServicePorts(data) {
  return request({
    url: serviceManagerApi + "/service-ports",
    method: "post",
    data,
  });
}

// 19.5.服务端口列表
// 请求：GET  /service-ports
export function getServicePorts(params) {
  return request({
    url: serviceManagerApi + "/service-ports",
    method: "get",
    params,
  });
}

// 19.6.服务端口详情
// 请求：GET  /service-ports/{service_port_id}
export function getServicePortsDetail(service_port_id) {
  return request({
    url: serviceManagerApi + "/service-ports/" + service_port_id,
    method: "get",
  });
}

// 19.7.更新服务端口
// 请求: PUT /service-ports/{service_port_id}
export function editServicePorts(service_port_id, data) {
  return request({
    url: serviceManagerApi + "/service-ports/" + service_port_id,
    method: "put",
    data,
  });
}

// 19.8.删除服务端口
// 请求: DELETE /service-ports/{service_port_id}
export function delServicePorts(service_port_id) {
  return request({
    url: serviceManagerApi + "/service-ports/" + service_port_id,
    method: "delete",
  });
}

// 19.9.重新分配服务端口
// 请求: PUT /service-ports/{service_port_id}/reallocate
export function reallocateServicePorts(service_port_id) {
  return request({
    url:
      serviceManagerApi + "/service-ports/" + service_port_id + "/reallocate",
    method: "put",
  });
}

// 19.10.服务端口测试
// 请求: GET /service-ports/{service_port_id}/test
export function testServicePorts(service_port_id) {
  return request({
    url: serviceManagerApi + "/service-ports/" + service_port_id + "/test",
    method: "get",
  });
}
// 19.11.服务端口测试
// 请求: GET /service-ports/{service_port_id}/target-services/{target_service_id}/test
export function testTargetService(service_port_id, target_service_id) {
  return request({
    url:
      serviceManagerApi +
      "/service-ports/" +
      service_port_id +
      "/target-services/" +
      target_service_id +
      "/test",
    method: "get",
  });
}

// ------------------20运维监控------------------
// 20.1.2.监控组件stack参数
// GET /monitor/integrations/params
export function getMonitorIntegrationsParams(params) {
  return request({
    url: omcApi + "/monitor/integrations/params",
    method: "get",
    params,
  });
}
// 20.1.3.监控组件安装
// POST /monitor/integrations
export function addMonitorIntegrations(data) {
  return request({
    url: omcApi + "/monitor/integrations",
    method: "post",
    data,
  });
}
// 20.1.4.安装历史
// GET /monitor/integrations
export function getMonitorIntegrations(params) {
  return request({
    url: omcApi + "/monitor/integrations",
    method: "get",
    params,
  });
}
// 20.1.4.安装历史
// GET /monitor/integrations
export function getMonitorIntegrationsDetail(id) {
  return request({
    url: omcApi + "/monitor/integrations/" + id,
    method: "get",
  });
}
// 20.1.5.重装监控组件
// PATCH /monitor/integrations/{id}?action=reinstall
export function reinstallMonitorIntegrations(id) {
  return request({
    url: omcApi + "/monitor/integrations/" + id + "?action=reinstall",
    method: "patch",
  });
}
// 20.1.6.卸载监控组件
// PATCH /monitor/integrations/{id}?action=uninstall
export function uninstallMonitorIntegrations(id) {
  return request({
    url: omcApi + "/monitor/integrations/" + id + "?action=uninstall",
    method: "patch",
  });
}
//  20.1.7.prometheus实例列表
// GET /monitor/prometheus
export function getMonitorPrometheus(params) {
  return request({
    url: omcApi + "/monitor/prometheus",
    method: "get",
    params,
  });
}

// 20.1.8.监控任务列表
// GET /monitor/jobs
export function getMonitorJobs(id) {
  return request({
    url: omcApi + "/monitor/prometheus/" + id + "/jobs",
    method: "get",
  });
}
// 20.1.9.监控数据地址
// GET /monitor/link
export function getMonitorLink(url) {
  return request({
    url: omcApi + "/monitor/link" + url,
    method: "get",
  });
}
// 20.1.10.监控总览-统计
// GET /stats/counts
export function getStatsCounts(params) {
  return request({
    url: omcApi + "/stats/counts",
    method: "get",
    params,
  });
}
// 20.1.11.监控总览-近7天状态统计
// GET /stats/daily/status-counts
export function getStatsDailyStatusCounts(params) {
  return request({
    url: omcApi + "/stats/daily/status-counts",
    method: "get",
    params,
  });
}
// 20.1.12.查看指定prometheus实例
export function getMonitorPrometheusDetail(id) {
  return request({
    url: omcApi + "/monitor/prometheus/" + id,
    method: "get",
  });
}
// 20.1.13.修改prometheus配置
export function monitorPrometheusUpdate(id, data) {
  return request({
    url: omcApi + "/monitor/prometheus/" + id,
    method: "patch",
    data,
  });
}
// 20.1.14. 监控数据获取(及时查询)
export function monitorPrometheusDataQuery(id, params) {
  return request({
    url: omcApi + "/monitor/prometheus/" + id + "/data/query",
    method: "get",
    params,
  });
}

// 20.2.2 告警分组列表
// GET /alarms/groups
export function getAlarmGroups(params) {
  return request({
    url: omcApi + "/alarms/groups",
    method: "get",
    params,
  });
}
// 告警的已读未读
export function readAlarmGroups(alarm_id, data) {
  return request({
    url: omcApi + "/alarms/alert-logs/process/" + alarm_id,
    method: "post",
    data,
  });
}
//
// 20.2.3 指定告警分组的指标数据
// GET /alarms/metrics
export function getAlarmMetrics(params) {
  return request({
    url: omcApi + "/alarms/metrics",
    method: "get",
    params,
  });
}
// 20.2.4 创建通知对象
// POST /receivers
export function addReceivers(data) {
  return request({
    url: omcApi + "/receivers",
    method: "post",
    data,
  });
}
// 20.2.5 修改通知对象
// PUT /receivers/{receiver_id}
export function editReceivers(receiver_id, data) {
  return request({
    url: omcApi + "/receivers/" + receiver_id,
    method: "put",
    data,
  });
}

// 20.2.6 通知对象列表
// GET /receivers
export function getReceivers(params) {
  return request({
    url: omcApi + "/receivers",
    method: "get",
    params,
  });
}
// 20.2.7 删除通知对象
// DELETE /receivers/{receiver_id}
export function delReceivers(receiver_id) {
  return request({
    url: omcApi + "/receivers/" + receiver_id,
    method: "delete",
  });
}
// 20.2.8 创建告警器
// POST /alarms
export function addAlarms(data) {
  return request({
    url: omcApi + "/alarms",
    method: "post",
    data,
  });
}
// 20.2.9 修改告警器
// PUT /alarms/{alarm_id}
export function editAlarms(alarm_id, data) {
  return request({
    url: omcApi + "/alarms/" + alarm_id,
    method: "put",
    data,
  });
}
// 20.2.10 告警列表
// GET /alarms
export function getAlarms(params) {
  return request({
    url: omcApi + "/alarms",
    method: "get",
    params,
  });
}
// 20.2.11 删除告警器
// DELETE /alarms
export function delAlarms(alarm_id) {
  return request({
    url: omcApi + "/alarms/" + alarm_id,
    method: "delete",
  });
}
// 20.2.12 查看指定告警器
// GET / alarms/{alarm_id}
export function getAlarmsInfo(alarm_id) {
  return request({
    url: omcApi + "/alarms/" + alarm_id,
    method: "get",
  });
}
// 20.2.13 启用告警器
// PATCH /alarm/{alarm_id}?action=active
export function activeAlarms(alarm_id) {
  return request({
    url: omcApi + "/alarms/" + alarm_id + "?action=active",
    method: "patch",
  });
}
// 20.2.14 停用告警器
// PATCH /alarm/{alarm_id}?action=deactive
export function deactiveAlarms(alarm_id) {
  return request({
    url: omcApi + "/alarms/" + alarm_id + "?action=deactive",
    method: "patch",
  });
}
// 20.2.15 告警历史列表
// GET /alarms/alert-logs
export function getAlertLogs(params) {
  return request({
    url: omcApi + "/alarms/alert-logs",
    method: "get",
    params,
  });
}
// 20.2.16 告警历史--统计
// GET /alarms/alert-logs/counts
export function getAlertLogsStatus(params) {
  return request({
    url: omcApi + "/alarms/alert-logs/counts",
    method: "get",
    params,
  });
}
// 20.2.18.监控总览-近7天平台告警数
// GET /alarms/alert-logs/counts
export function getAlertLogsDaily(params) {
  return request({
    url: omcApi + "/alarms/alert-logs/daily/trend",
    method: "get",
    params,
  });
}
// 20.3.2日志列表
export function getApiLogs(params) {
  return request({
    url: omcApi + "/apilogs",
    method: "get",
    params,
  });
}
//20.3.3事件列表
export function getApiEvents(params) {
  return request({
    url: omcApi + "/apievents",
    method: "get",
    params,
  });
}
//20.3.4.监控总览-近一天按小时操作日志数
export function getApiLogsHourly(params) {
  return request({
    url: omcApi + "/apilogs/hourly/trend",
    method: "get",
    params,
  });
}

// 服务条款json
export function getTermsJson() {
  return request({
    url: "/terms/terms.json?time=" + Date.now(),
    method: "get",
  });
}
