import request from "@/utils/request";
const transmissionApi = process.env.VUE_APP_BASE_API + "sr";
const localurl = "http://127.0.0.1:9528";
//传输加速云接口
//登录
export function transmissionLogin(token) {
  return request({
    url: transmissionApi + "/user/authentication/edgecloud/" + token,
    method: "get",
  });
}
//登出
export function transmissionLoginOut(data) {
  return request({
    url: transmissionApi + "/user/logout",
    method: "PUT",
    data,
  });
}
//获取用户信息
export function transmissionUserInfo(params) {
  return request({
    url: transmissionApi + "/user/info/list",
    method: "get",
    params,
  });
}
//创建文件目录
export function transmissionMkdir(storeNsId, data) {
  return request({
    url: transmissionApi + "/fs/mkdir?" + "storeNsId=" + storeNsId,
    method: "POST",
    data,
  });
}
//修改文件/文件夹路径
export function transmissionUpdateDir(storeNsId, data) {
  return request({
    url: transmissionApi + "/fs/update?" + "storeNsId=" + storeNsId,
    method: "PUT",
    data,
  });
}
//删除文件夹/文件
export function transmissionRmdir(storeNsId, data) {
  return request({
    url: transmissionApi + "/fs/rm?" + "storeNsId=" + storeNsId,
    method: "DELETE",
    data,
  });
} //浏览文件目录/目录
export function transmissionBrowse(assetType, params, data) {
  return request({
    url: transmissionApi + "/fs/browse/CPU" + assetType,
    method: "POST",
    params,
    data,
  });
}
//获取用户存储空间传输地址信息
export function transmissionFsStore(assetType, params) {
  return request({
    url: transmissionApi + "/fs/transInfo/store/" + assetType,
    method: "get",
    params,
  });
}
//获取指定文件/文件夹的信息
export function transmissionFsInformation(assetType, params) {
  return request({
    url: transmissionApi + "/fs/stat/" + assetType,
    method: "get",
    params,
  });
}
//查询站点列表
export function getStoreNameSpace(data) {
  return request({
    url: transmissionApi + "/store/storeNameSpace/query",
    method: "POST",
    data,
  });
}
//前端与客户端接口
//确认本地文件传输客户端服务是否存在
export function transmissionPing(params) {
  return request({
    url: localurl + "/fs/ping",
    method: "get",
    params,
  });
}
//上传文件/文件夹
export function transmissionUpload(data) {
  return request({
    url: localurl + "/fs/upload",
    method: "POST",
    data,
  });
}
//下载文件/文件夹
export function transmissionDownload(data) {
  return request({
    url: localurl + "/fs/download",
    method: "POST",
    data,
  });
}
//进度查询
export function transmissionQuery(transId) {
  return request({
    url: localurl + "/fs/progress/query",
    method: "POST",
  });
}
//删除指定任务
export function transmissionDeleteProgress(transId) {
  return request({
    url: localurl + "/fs/task/rm/" + transId,
    method: "DELETE",
  });
}
//浏览本地文件/文件夹
export function transmissionLs(params) {
  return request({
    url: localurl + "/fs/ls",
    method: "get",
    params,
  });
}
//停止传输任务
export function transmissionStopProgress(transId) {
  return request({
    url: localurl + "/fs/task/stop/" + transId,
    method: "get",
  });
}
//启动传输任务
export function transmissionStartProgress(transId) {
  return request({
    url: localurl + "/fs/task/start/" + transId,
    method: "get",
  });
}
//打开下载对象所在的文件夹
export function transmissionOpenFile(transId) {
  return request({
    url: transmissionApi + "/fs/open/localpath/" + transId,
    method: "get",
  });
}
//查询下载日志
export function TransStatisticsQueryInfo(data) {
  return request({
    url: transmissionApi + "/fs/transInfo/statistics/log",
    method: "POST",
    data,
  });
}
// //查询JSON文件
export function getDownloadJson() {
  return request({
    url: "./download/downloadLink.json?time=" + Date.now(),
    method: "get",
  });
}

// ----文档管理----
export function directoryQuery(data) {
  return request({
    url: transmissionApi + "/user/announcement/directory/query",
    method: "post",
    data,
  });
}
export function directoryAdd(data) {
  return request({
    url: transmissionApi + "/user/announcement/directory/add",
    method: "POST",
    data,
  });
}
export function directoryUpdate(data) {
  return request({
    url: transmissionApi + "/user/announcement/directory/update",
    method: "POST",
    data,
  });
}
export function directoryDel(data) {
  return request({
    url: transmissionApi + "/user/announcement/directory/delete",
    method: "POST",
    data,
  });
}
export function documentQuery(params) {
  return request({
    url: transmissionApi + "/user/announcement/document/query",
    method: "get",
    params,
  });
}
export function documentAdd(data) {
  return request({
    url: transmissionApi + "/user/announcement/document/add",
    method: "POST",
    data,
  });
}
export function documentUpdate(data) {
  return request({
    url: transmissionApi + "/user/announcement/document/update",
    method: "POST",
    data,
  });
}
export function documentDel(data) {
  return request({
    url: transmissionApi + "/user/announcement/document/delete",
    method: "POST",
    data,
  });
}
export function documentDetail(id) {
  return request({
    url: transmissionApi + "/user/announcement/document/queryById/" + id,
    method: "get",
  });
}
