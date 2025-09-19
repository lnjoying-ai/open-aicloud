import request from "@/utils/request";

const iamApi = process.env.VUE_APP_BASE_API + "api/iam/v1";

export function getUsers(params) {
  return request({
    url: iamApi + "/users",
    method: "get",
    params,
  });
}
export function addUsers(data) {
  return request({
    url: iamApi + "/users",
    method: "post",
    data,
  });
}
export function editUsers(id, data) {
  return request({
    url: iamApi + "/users/" + id,
    method: "put",
    data,
  });
}
export function delUsers(id) {
  return request({
    url: iamApi + "/users/" + id,
    method: "delete",
  });
}
export function userUniqueness(data) {
  return request({
    url: iamApi + "/users/" + "uniqueness",
    method: "put",
    data,
  });
}
export function detailUsers(id) {
  return request({
    url: iamApi + "/users/" + id,
    method: "get",
  });
}
export function getCurrent() {
  return request({
    url: iamApi + "/users/current",
    method: "get",
  });
}
// 修改用户信息
export function putCurrent(data) {
  return request({
    url: iamApi + "/users/current",
    method: "put",
    data,
  });
}
export function nations() {
  return request({
    url: iamApi + "/nations",
    method: "get",
  });
}
export function divistions(data1, data2) {
  return request({
    url: iamApi + "/divisions/" + data1 + "/" + data2,
    method: "get",
  });
}
// 修改邮箱获取验证吗
export function getPatchEmail(params) {
  return request({
    url: iamApi + "/verification/patch/email/" + params,
    method: "get",
  });
}
export function changeEmail(data) {
  return request({
    url: iamApi + "/users/email",
    method: "PATCH",
    data,
  });
}
// 修改密码
export function changeUserPwd(data) {
  return request({
    url: iamApi + "/users/password",
    method: "PATCH",
    data,
  });
}

// 生成邀请链接
export function generatelink() {
  return request({
    url: iamApi + "/users/invitation-link",
    method: "get",
  });
}