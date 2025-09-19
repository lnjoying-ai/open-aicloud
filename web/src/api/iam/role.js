import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getRoles(params) {
  return request({
    url: iamApi + "/roles",
    method: "get",
    params,
  });
}
export function addRoles(data) {
  return request({
    url: iamApi + "/roles",
    method: "post",
    data,
  });
}
export function editRoles(id, data) {
  return request({
    url: iamApi + "/roles/" + id,
    method: "put",
    data,
  });
}
export function delRoles(id) {
  return request({
    url: iamApi + "/roles/" + id,
    method: "delete",
  });
}
export function detaillRole(id) {
  return request({
    url: iamApi + "/roles/" + id,
    method: "get",
  });
}
