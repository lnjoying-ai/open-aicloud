import Vue from "vue";

import "normalize.css/normalize.css"; // A modern alternative to CSS resets
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

import "tailwindcss/tailwind.css";

import "@/styles/index.scss"; // global css

import i18n from "./utils/i18n";
import App from "./App";
import store from "./store";

import router from "./router";
import Terminal from "vue-web-terminal";
import Print from "vue-print-nb";
import "@/icons"; // icon
import "@/permission"; // permission control
// 自定义方法库
import script from "@/utils/script.js";
import { VueMasonryPlugin } from "vue-masonry";
import mavonEditor from "mavon-editor";
import "./assets/font/iconfont.css";
Vue.use(mavonEditor);
Vue.use(VueMasonryPlugin);
Vue.prototype.$script = script;

// 状态管理库
import status from "@/utils/status.js";
Vue.prototype.$getStatus = status;
/**
 * If you don't want to use mock-server
 * you want to use MockJs for mock api
 * you can execute: mockXHR()
 *
 * Currently MockJs will be used in the production environment,
 * please remove it before going online ! ! !
 */

if (process.env.NODE_ENV === "production") {
  const { mockXHR } = require("../mock");
  mockXHR();
}

// set ElementUI lang to EN
// Vue.use(ElementUI, { locale })
// 如果想要中文版 element-ui，按如下方式声明
Vue.use(ElementUI);
Vue.use(Print);
Vue.config.productionTip = false;
Vue.use(Terminal);
new Vue({
  el: "#app",
  router,
  store,
  i18n,
  render: (h) => h(App),
});
