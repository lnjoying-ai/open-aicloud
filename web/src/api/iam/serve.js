import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getService(params) {
  return request({
    url: iamApi + "/services",
    method: "get",
    params,
  });
}
export function detailService(id) {
  return request({
    url: iamApi + "/services/" + id,
    method: "get",
  });
}
export function addService(data) {
  return request({
    url: iamApi + "/services",
    method: "post",
    data,
  });
}
export function editService(id, data) {
  return request({
    url: iamApi + "/services/" + id,
    method: "put",
    data,
  });
}
export function delService(id) {
  return request({
    url: iamApi + "/services/" + id,
    method: "delete",
  });
}
