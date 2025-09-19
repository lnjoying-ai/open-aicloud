/*
 * @Author: your name
 * @Date: 2021-11-29 08:40:20
 * @LastEditTime: 2021-12-15 18:50:15
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \WEB的副本\src\utils\request.js
 */
import axios from "axios";
import { Message } from "element-ui";
import store from "@/store";
import router from "@/router";
import Cookies from "js-cookie";
import msgFun from "@/utils/codeStatus";
import { resetRouter } from "@/router";
import i18n from "@/utils/i18n/index.js";

const service = axios.create({
  withCredentials: true, // send cookies when cross-domain requests
  timeout: 60 * 1000, // request timeout
});

// request 拦截器
service.interceptors.request.use(
  (config) => {
    if (config.url.includes("127.0.0.1")) {
      config.withCredentials = false;
    } else {
      config.withCredentials = true;
    }
    if (store.getters.token) {
      config.headers["Content-type"] = "application/json";
    }
    // 如果config.url中包含undefined，则不发送请求
    if (config.url.indexOf("undefined") != -1) {
      // 结束请求
      return Promise.reject("");
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

let isPermissionErrorShown = false;
// response 响应拦截器
service.interceptors.response.use(
  (response) => {
    if (
      response.status == 200 ||
      response.status == 201 ||
      response.status == 202 ||
      response.status == 204
    ) {
      return response.data;
    }
    Message({
      message: response.message || "Error",
      type: "error",
      duration: 5 * 1000,
    });
    return response;
  },
  (error) => {
    // 没有权限提示没有权限
    if (error.response.status == 400) {
      if (
        error.response &&
        error.response.status == 400 &&
        error.response.data.code == 3000
      ) {
        const errorMessage = error.response.data.message;
        if (errorMessage === "illegal Operation" && !isPermissionErrorShown) {
          Message({
            message: error.response.data.code
              ? msgFun.getCodeStatus(error.response.data.code, error.response)
                ? msgFun.getCodeStatus(error.response.data.code, error.response)
                : error.response.data.message ||
                  i18n.t("statusAndType.unknownStatus")
              : error.response.data.message ||
                i18n.t("statusAndType.unknownStatus"),
            type: "error",
          });
          isPermissionErrorShown = true;
          setTimeout(() => {
            isPermissionErrorShown = false;
          }, 2000);
        }
      } else {
        Message({
          message: error.response.data.code
            ? msgFun.getCodeStatus(error.response.data.code, error.response)
              ? msgFun.getCodeStatus(error.response.data.code, error.response)
              : error.response.data.message ||
                i18n.t("statusAndType.unknownStatus")
            : error.response.data.message ||
              i18n.t("statusAndType.unknownStatus"),
          type: "error",
          duration: 5 * 1000,
        });
      }
      return Promise.reject(error);
    }
    if (error.response.status == 401) {
      localStorage.clear();
      Cookies.remove("Access-Token");
      Cookies.remove("vue_admin_template_kind");
      store.commit("user/SET_KING", "");
      resetRouter();
      if (router.currentRoute.path != "/login") {
        router.push({
          path: "/login",
          query: { redirect: router.currentRoute.fullPath },
        });
        Message({
          message: error.response.data.code
            ? msgFun.getCodeStatus(error.response.data.code, error.response)
              ? msgFun.getCodeStatus(error.response.data.code, error.response)
              : error.response.data.message ||
                i18n.t("statusAndType.unknownStatus")
            : error.response.data.message ||
              i18n.t("statusAndType.unknownStatus"),
          type: "error",
          duration: 5 * 1000,
        });
      } else {
        router.push({
          path: "/login",
        });
        if (
          error.response.config.url.indexOf(
            "/api/ums/v1/auth/password/tokens"
          ) != -1
        ) {
          Message({
            message: error.response.data.code
              ? msgFun.getCodeStatus(error.response.data.code, error.response)
                ? msgFun.getCodeStatus(error.response.data.code, error.response)
                : error.response.data.message ||
                  i18n.t("statusAndType.unknownStatus")
              : error.response.data.message ||
                i18n.t("statusAndType.unknownStatus"),
            type: "error",
            duration: 5 * 1000,
          });
        }
      }
      return Promise.reject(error);
    } else {
      if (
        error.response.config.url.indexOf("/api/cis/v1/docker/instances") != -1
      ) {
        return Promise.reject(error);
      } else if (
        error.response.config.url.indexOf("/api/cis/v1/docker/deployments") !=
        -1
      ) {
        return Promise.reject(error);
      } else if (
        error.response.config.url.indexOf("/nodes/stacks") != -1 &&
        error.response.config.method == "post" &&
        error.response.status == 409
      ) {
        Message({
          message: i18n.t("message.terminalOpen"),
          type: "error",
          duration: 5 * 1000,
        });
        return Promise.reject(error);
      } else if (error.response.data.code == 2293) {
        // 2293 项目管理界面未设置仓库密码，页面已经有了弹窗，不需要在此处提示
        return Promise.reject(error);
      } else {
        Message({
          message: error.response.data.code
            ? msgFun.getCodeStatus(error.response.data.code, error.response)
              ? msgFun.getCodeStatus(error.response.data.code, error.response)
              : error.response.data.message ||
                i18n.t("statusAndType.unknownStatus")
            : error.response.data.message ||
              i18n.t("statusAndType.unknownStatus"),
          type: "error",
          duration: 5 * 1000,
        });
        return Promise.reject(error);
      }
    }
  }
);

export default service;
