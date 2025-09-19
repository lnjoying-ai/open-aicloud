import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getAuthz(params) {
  return request({
    url: iamApi + "/authz/policies",
    method: "get",
    params,
  });
}
export function getRolesAuthz(params) {
  return request({
    url: iamApi + "/authz/roles",
    method: "get",
    params,
  });
}
export function checkAuthz(data) {
  return request({
    url: iamApi + "/authz/check",
    method: "post",
    data,
  });
}
export function allowedAuthz(data) {
  return request({
    url: iamApi + "/authz/allowed",
    method: "post",
    data,
  });
}

export function attachUserAuthz(data) {
  return request({
    url: iamApi + "/authz/attach/user",
    method: "post",
    data,
  });
}
export function detachUserAuthz(data) {
  return request({
    url: iamApi + "/authz/detach/user",
    method: "post",
    data,
  });
}
export function attachGroupAuthz(data) {
  return request({
    url: iamApi + "/authz/attach/group",
    method: "post",
    data,
  });
}
export function detachGroupAuthz(data) {
  return request({
    url: iamApi + "/authz/detach/group",
    method: "post",
    data,
  });
}
export function attachRoleAuthz(data) {
  return request({
    url: iamApi + "/authz/attach/role",
    method: "post",
    data,
  });
}
export function detachRoleAuthz(data) {
  return request({
    url: iamApi + "/authz/detach/role",
    method: "post",
    data,
  });
}
