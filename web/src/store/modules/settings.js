import defaultSettings from "@/settings";

const { showSettings, fixedHeader, sidebarLogo } = defaultSettings;

const state = {
  routerGroup: "",
  showSlider: false,
  hidePageTitle: false,
  showSettings: showSettings,
  fixedHeader: fixedHeader,
  sidebarLogo: sidebarLogo,
};

const mutations = {
  CHANGE_SETTING: (state, { key, value }) => {
    // eslint-disable-next-line no-prototype-builtins
    if (state.hasOwnProperty(key)) {
      state[key] = value;
    }
  },
  SET_ROUTER_GROUP: (state, group) => {
    state.routerGroup = group;
  },
  SET_SHOW_SILDER: (state, status) => {
    state.showSlider = status;
  },
  SET_HIDE_PAGE_TITLE: (state, status) => {
    state.hidePageTitle = status;
  },
};

const actions = {
  changeSetting({ commit }, data) {
    commit("CHANGE_SETTING", data);
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
