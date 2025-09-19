import Cookies from "js-cookie";

var localStorageCloudData =
  localStorage.getItem("hxcloudData") &&
  localStorage.getItem("hxcloudData") != "undefined"
    ? JSON.parse(localStorage.getItem("hxcloudData"))
    : "";
const state = {
  sidebar: {
    opened: Cookies.get("sidebarStatus")
      ? !!+Cookies.get("sidebarStatus")
      : true,
    withoutAnimation: false,
  },
  device: "desktop",
  searchList: [],
  bottomContral: null,
  filesize: 4,
  timeInterval: 5000,

  hxcloudData: localStorageCloudData,
};

const mutations = {
  TOGGLE_SIDEBAR: (state) => {
    state.sidebar.opened = !state.sidebar.opened;
    state.sidebar.withoutAnimation = false;
    if (state.sidebar.opened) {
      Cookies.set("sidebarStatus", 1);
    } else {
      Cookies.set("sidebarStatus", 0);
    }
  },
  CLOSE_SIDEBAR: (state, withoutAnimation) => {
    Cookies.set("sidebarStatus", 0);
    state.sidebar.opened = false;
    state.sidebar.withoutAnimation = withoutAnimation;
  },
  TOGGLE_DEVICE: (state, device) => {
    state.device = device;
  },

  ADD_SEARCH_LIST: (state, info) => {
    let index = state.searchList.indexOf(info);
    if (index >= 0) {
      state.searchList.splice(index, 1);
    }
    state.searchList.unshift(info);
    if (state.searchList.length > 20) {
      state.searchList.pop();
    }
    localStorage.setItem("searchList", JSON.stringify(state.searchList));
  },
  INIT_STORE: (state) => {
    state.searchList = localStorage.getItem("searchList")
      ? JSON.parse(localStorage.getItem("searchList"))
      : [];
  },

  setBottomContral(state, type) {
    state.bottomContral = type;
  },

  sethxcloudData(state, data) {
    if (data == "") {
      state.hxcloudData = "";
      localStorage.removeItem("hxcloudData");
    } else {
      state.hxcloudData = data;
      localStorage.setItem("hxcloudData", JSON.stringify(data));
    }
  },
};

const actions = {
  toggleSideBar({ commit }) {
    commit("TOGGLE_SIDEBAR");
  },
  closeSideBar({ commit }, { withoutAnimation }) {
    commit("CLOSE_SIDEBAR", withoutAnimation);
  },
  toggleDevice({ commit }, device) {
    commit("TOGGLE_DEVICE", device);
  },

  addSearchList({ commit }, info) {
    commit("ADD_SEARCH_LIST", info);
  },
  initStore({ commit }) {
    commit("INIT_STORE");
  },
  setBottomContral({ commit }, type) {
    commit("setBottomContral", type);
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
