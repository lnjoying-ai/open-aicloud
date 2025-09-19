/*
 * @Author: your name
 * @Date: 2021-11-24 08:42:03
 * @LastEditTime: 2021-12-15 18:49:59
 * @LastEditors: Please set LastEditors
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: \WEB的副本\src\store\modules\user.js
 */
import store from "@/store";
import {
  login,
  logout,
  getInfo,
  phoneLogin,
  getCurrent,
} from "@/api/user/mainApi";
import { getCloudysInfo } from "@/api/mainApi";
import {
  getToken,
  removeToken,
  getKind,
  setkind,
  removeKind,
} from "@/utils/auth";
import { resetRouter } from "@/router";
const getDefaultState = () => {
  return {
    token: getToken(),
    name: "",
    avatar: "",
    kind: getKind(),
    routers: [],
    userInfo: {},
    tipMessageStatus: true,
  };
};

const state = getDefaultState();

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState());
  },
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_KING: (state, kind) => {
    state.kind = kind;
  },

  SET_ROUTERS: (state, routers) => {
    state.routers = routers;
  },
  SET_USERINFO: (state, userInfo) => {
    state.userInfo = userInfo;
  },
  SET_TipMessageStatus: (state, value) => {
    state.tipMessageStatus = value;
  },
};

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo;
    return new Promise((resolve, reject) => {
      login({ name: username.trim(), password: password })
        .then((response) => {
          resolve(response);
        })
        .catch((error) => {
          reject(error);
        });
      commit("SET_TipMessageStatus", true);
    });
  },

  getCurrent({ commit, state }) {
    return new Promise((resolve, reject) => {
      getCurrent()
        .then((response) => {
          commit("SET_KING", response.kind);
          setkind(response.kind);
          commit("SET_USERINFO", response);
          localStorage.setItem("userInfo", JSON.stringify(response));
          // 下方是多云列表切换相关
          var roleData = [];
          if (response.permissions && response.permissions.length > 0) {
            roleData = response.permissions.map((item) => {
              return item.role;
            });
          }
          if (
            response.kind === 0 ||
            response.kind === 1 ||
            roleData.indexOf("ADMIN") > -1 ||
            roleData.indexOf("TENANT_ADMIN") > -1 ||
            roleData.indexOf("TENANT") > -1
          ) {
            getCloudysInfo({ page_size: 99999, page_num: 1 })
              .then((cloudsRes) => {
                if (!cloudsRes.clouds || cloudsRes.clouds.length == 0) {
                  // 暂无可用云
                  store.commit("app/sethxcloudData", ""); // 设置为默认云
                  resolve(response);
                  return;
                }
                var hxcloudData =
                  localStorage.getItem("hxcloudData") &&
                  localStorage.getItem("hxcloudData") != "undefined"
                    ? JSON.parse(localStorage.getItem("hxcloudData"))
                    : "";
                if (hxcloudData) {
                  var data = cloudsRes.clouds.filter((item) => {
                    return item.cloud_id == hxcloudData.cloud_id;
                  });
                  if (data.length == 0) {
                    if (cloudsRes.clouds && cloudsRes.clouds.length > 0) {
                      // 云大于0 个
                      store.commit("app/sethxcloudData", cloudsRes.clouds[0]); // 设置为默认云
                    }
                  }
                } else {
                  // 没有缓存
                  var nowCloud = cloudsRes.clouds;
                  if (nowCloud && nowCloud.length > 0) {
                    // 云大于0 个
                    store.commit("app/sethxcloudData", nowCloud[0]); // 设置为默认云
                  } else {
                    // 暂无可用云
                  }
                }
                resolve(response);
              })
              .catch(() => {
                resolve(response);
              });
          } else {
            resolve(response);
          }
        })
        .catch((error) => {
          console.log(error);
          reject(error);
        });
    });
  },

  phoneLogin({ commit }, userInfo) {
    const { phone_num, verification_code } = userInfo;
    return new Promise((resolve, reject) => {
      phoneLogin({ phone: phone_num.trim(), code: verification_code })
        .then((response) => {
          getCurrent().then((response) => {
            commit("SET_KING", response.kind);
            setkind(response.kind);
            commit("SET_USERINFO", response);
            localStorage.setItem("userInfo", JSON.stringify(response));
            resolve();
          });
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token)
        .then((response) => {
          if (!response) {
            return reject("Verification failed, please Login again.");
          }
          const { name, avatar } = response;
          commit("SET_NAME", name);
          commit("SET_AVATAR", avatar);
          resolve(response);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token)
        .then(() => {
          removeToken(); // must remove  token  first
          resetRouter();
          removeKind();
          commit("RESET_STATE");
          commit("SET_ROUTERS", []);
          commit("SET_KING", "");
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  // remove token
  resetToken({ commit }) {
    return new Promise((resolve) => {
      commit("SET_KING", "");
      commit("SET_ROUTERS", []);
      commit("SET_USERINFO", {});
      resolve();
    });
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
