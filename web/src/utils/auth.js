/*
 * @Author: your name
 * @Date: 2021-11-29 08:40:20
 * @LastEditTime: 2021-11-29 10:34:41
 * @LastEditors: your name
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \WEB的副本\src\utils\auth.js
 */
import Cookies from "js-cookie";
const lnjoyingToken = "lnjoyingToken";
const TokenKey = "Access-Token";
const userKind = "vue_admin_template_kind";
const projectKey = "projectKey";
const userRouter = "vue_admin_template_routers";
export function getToken() {
  return Cookies.get(TokenKey);
}
export function getLnjoyingToken() {
  return Cookies.get(lnjoyingToken);
}
export function setToken(token) {
  return Cookies.set(TokenKey, token);
}

export function removeToken() {
  return Cookies.remove(TokenKey);
}
export function getKind() {
  return Cookies.get(userKind);
}

export function setkind(kind) {
  return Cookies.set(userKind, kind);
}

export function removeKind() {
  return Cookies.remove(userKind);
}
export function getProject() {
  return Cookies.get(projectKey);
}
export function setProject(project_key) {
  return Cookies.set(projectKey, project_key);
}
export function removeProject() {
  return Cookies.remove(projectKey);
}
