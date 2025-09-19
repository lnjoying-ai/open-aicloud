import Cookies from "js-cookie";
//列表每页显示条数
var getpageSize = localStorage.getItem("page_size");
const pageSize =
  getpageSize && getpageSize != "undefined" ? JSON.parse(getpageSize) : 10;

const state = {
  listRefreshTime: 5000, //列表刷新时间
  page_size: pageSize, //列表每页显示条数
  page_sizes: [5, 10, 20, 30, 40, 50, 100], //列表每页显示条数列表
  viewSize: {
    //large / default /small
    main: "small", //全部
    listSet: "small", //列表
    tagStatus: "small", //tag标签样式的状态
    tabChange: "small", //切换 标题后面跟的切换，单选框和按钮
  },
};

const mutations = {};

const actions = {};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
