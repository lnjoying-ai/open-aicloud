import request from "@/utils/request";
import store from "@/store";
const billApi = process.env.VUE_APP_BASE_API + "api/bill/v1";

// 续费接口
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
      : "")
  );
};
export function getDiscounts(params) {
  return request({
    url: billApi + "/discounts",
    method: "get",
    params,
  });
}
export function addDiscounts(data) {
  return request({
    url: billApi + "/discounts",
    method: "post",
    data,
  });
}
export function editDiscounts(discount_id, data) {
  return request({
    url: billApi + "/discounts/" + discount_id,
    method: "put",
    data,
  });
}

export function delDiscounts(discount_id) {
  return request({
    url: billApi + "/discounts/" + discount_id,
    method: "DELETE",
  });
}
//预计费询价计算
export function getInquiry(data) {
  return request({
    url: billApi + "/price/inquiry",
    method: "post",
    data,
  });
}
//next的实例续费接口
export function nextvmrenew(instance_id, data) {
  return request({
    url: getNetworkApi() + `/api/vm/v1/instances/${instance_id}/renewOrder`,
    method: "post",
    data,
  });
}
//next的磁盘续费接口
export function nextvolumesrenew(volume_id, data) {
  return request({
    url: getNetworkApi() + `/api/repo/v1/volumes/${volume_id}/renewOrder`,
    method: "post",
    data,
  });
}
// easy的云主机续费
export function easyinstancerenew(server_id, data) {
  return request({
    url: getNetworkApi() + `/extension/v2.1/servers/${server_id}/renewOrder`,
    method: "post",
    data,
  });
}

// easy的云硬盘续费
export function easyvolumesrenew(volume_id, data) {
  return request({
    url:
      getNetworkApi() +
      `/extension/v3/{project_id}/volumes/${volume_id}/renewOrder`,
    method: "post",
    data,
  });
}
