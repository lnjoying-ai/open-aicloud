import request from "@/utils/request";

const umsApi = process.env.VUE_APP_BASE_API + "api/ums/v1";
export function authzCheck(data) {
  return request({
    url: umsApi + "/authz/check",
    method: "post",
    data,
  });
}
export function login(data) {
  return request({
    url: umsApi + "/auth/password/tokens",
    method: "post",
    data,
  });
}

export function register(data) {
  return request({
    url: umsApi + "/users/registration",
    method: "post",
    data,
  });
}
// 注册获取验证码
export function registerSms(params) {
  return request({
    url: umsApi + "/verification/registration/sms/" + params,
    method: "get",
  });
}
// 登录获取验证吗
export function getsms(params) {
  return request({
    url: umsApi + "/verification/auth/sms/" + params,
    method: "get",
  });
}
// 用户短信登录认证
export function phoneLogin(data) {
  return request({
    url: umsApi + "/auth/sms/tokens",
    method: "post",
    data,
  });
}
// 找回密码
export function retPassword(data) {
  return request({
    url: umsApi + "/users/retrieved-password",
    method: "PATCH",
    data,
  });
}

export function getInfo(token) {
  return request({
    url: umsApi + "/vue-admin-template/user/info",
    method: "get",
    params: { token },
  });
}
export function getCurrent() {
  return request({
    url: umsApi + "/users/current",
    method: "get",
  });
}
// 修改用户信息
export function putCurrent(data) {
  return request({
    url: umsApi + "/users/current",
    method: "put",
    data,
  });
}

// 修改密码
export function changeUserPwd(data) {
  return request({
    url: umsApi + "/users/password",
    method: "PATCH",
    data,
  });
}
// 修改手机号

export function changePhone(data) {
  return request({
    url: umsApi + "/users/phone",
    method: "PATCH",
    data,
  });
}
// 6.4.4.	获取用户注册的邮件验证码

// 获取邮箱验证码
export function getEmail(email_addr) {
  return request({
    url: umsApi + "/verification/auth/email/" + email_addr,
    method: "get",
  });
}
// 更改手机号获取验证吗
export function getPatchSms(params) {
  return request({
    url: umsApi + "/verification/patch/sms/" + params,
    method: "get",
  });
}
// 修改邮箱获取验证吗
export function getPatchEmail(params) {
  return request({
    url: umsApi + "/verification/patch/email/" + params,
    method: "get",
  });
}
export function changeEmail(data) {
  return request({
    url: umsApi + "/users/email",
    method: "PATCH",
    data,
  });
}

export function logout() {
  return request({
    url: umsApi + "/auth/tokens",
    method: "post",
  });
}
