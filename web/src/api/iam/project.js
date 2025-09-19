import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getProject(params) {
  return request({
    url: iamApi + "/projects",
    method: "get",
    params,
  });
}
export function detailProject(id) {
  return request({
    url: iamApi + "/projects/" + id,
    method: "get",
  });
}
export function addProject(data) {
  return request({
    url: iamApi + "/projects",
    method: "post",
    data,
  });
}
export function editProject(id, data) {
  return request({
    url: iamApi + "/projects/" + id,
    method: "put",
    data,
  });
}
export function delProject(id) {
  return request({
    url: iamApi + "/projects/" + id,
    method: "delete",
  });
}
export function actionProject(id, params) {
  // ?action=active ?action=deactive
  return request({
    url: iamApi + "/projects/" + id,
    method: "patch",
    params,
  });
}
export function addUserToProject(id, data) {
  return request({
    url: iamApi + "/projects/" + id + "/users",
    method: "post",
    data,
  });
}
export function addGroupToProject(id, data) {
  return request({
    url: iamApi + "/projects/" + id + "/groups",
    method: "post",
    data,
  });
}
export function getProjectUsers(id) {
  return request({
    url: iamApi + "/projects/" + id + "/users",
    method: "get",
  });
}
export function getProjectGroups(id) {
  return request({
    url: iamApi + "/projects/" + id + "/groups",
    method: "get",
  });
}
export function delProjectUser(id, data) {
  return request({
    url: iamApi + "/projects/" + id + "/users",
    method: "delete",
    data
  });
}

