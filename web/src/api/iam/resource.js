import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getResource(params) {
  return request({
    url: iamApi + "/schema/common-resources",
    method: "get",
    params,
  });
}
export function detailResource(id) {
  return request({
    url: iamApi + "/schema/common-resources/" + id,
    method: "get",
  });
}
export function addResource(data) {
  return request({
    url: iamApi + "/schema/common-resources",
    method: "post",
    data,
  });
}
export function editResource(id, data) {
  return request({
    url: iamApi + "/schema/common-resources/" + id,
    method: "put",
    data,
  });
}
export function delResource(id) {
  return request({
    url: iamApi + "/schema/common-resources/" + id,
    method: "delete",
  });
}

export function getResourceActions(params) {
  return request({
    url: iamApi + "/schema/common-resources/actions",
    method: "get",
    params,
  });
}
export function getResourceConditions(params) {
  return request({
    url: iamApi + "/schema/common-resources/conditions",
    method: "get",
    params,
  });
}
