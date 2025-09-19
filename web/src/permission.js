import router from "./router";
import GetConfig from "./router/config_fig";
import store from "./store";
import { Form, Message } from "element-ui";
import NProgress from "nprogress"; // progress bar
import "nprogress/nprogress.css"; // progress bar style
import { getToken } from "@/utils/auth"; // get token from cookie
import getPageTitle from "@/utils/get-page-title";
import Cookies from "js-cookie";
import { resetRouter } from "@/router";
import i18n from "@/utils/i18n";

NProgress.configure({ showSpinner: false }); // NProgress Configuration
const whiteList = [
  "/login",
  "/register",
  "/Terms-of-Service",
  "/market",
  "/warehouse",
  "/",
]; // no redirect whitelist
const phoneStyle = [
  "/login",
  "/register",
  "/Terms-of-Service",
  "/market",
  "/warehouse",
  "/",
]; // 适配手机的页面

router.beforeEach(async (to, from, next) => {
  if (
    phoneStyle.indexOf(to.path) != -1 ||
    to.path.indexOf("warehouse/") != -1
  ) {
    // 获取 body 样式，插入 min-width：100vw
    document.body.style.minWidth = "100vw";
  } else {
    document.body.style.minWidth = "1200px";
  }
  if (to.meta.group) {
    store.commit("settings/SET_ROUTER_GROUP", to.meta.group);
    store.commit("settings/SET_SHOW_SILDER", to.meta.showSlider);
    store.commit("settings/SET_HIDE_PAGE_TITLE", to.meta.hidePageTitle);
  }
  // start progress bar
  NProgress.start();
  // set page title
  document.title = getPageTitle(i18n.t("router." + to.meta.languageTitle));

  // determine whether the user has logged in
  const hasToken = getToken();
  if (hasToken) {
    if (to.path === "/login") {
      // if is logged in, redirect to the home page
      goPage(to, next);
      NProgress.done();
    } else {
      if (!store.getters.kind && store.getters.kind !== 0) {
        store
          .dispatch("user/getCurrent")
          .then((res) => {
            // 拉取用户信息
            const menus = GetConfig(res.kind, res.permissions);
            const username = res.name;

            // const username = res.data.username
            store.dispatch("GenerateRoutes", { menus, username }).then(() => {
              // 生成可访问的路由表
              router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表
              goPage(from, next);
              // next({ path: "/index" });
            });
          })
          .catch((err) => {
            Cookies.remove("Access-Token");
            Cookies.remove("vue_admin_template_kind");
            next({ path: "/index" });
            // store.dispatch("user/logout").then(() => {
            //   Message.error(err || "Verification failed, please login again");
            //   next({ path: "/" });
            // });
          });
      } else {
        if (store.getters.addRouters.length <= 0) {
          store
            .dispatch("user/getCurrent")
            .then((res) => {
              // 拉取用户信息
              const menus = GetConfig(res.kind, res.permissions);
              const username = res.name;

              store.dispatch("GenerateRoutes", { menus, username }).then(() => {
                // 生成可访问的路由表
                router.addRoutes(store.getters.addRouters); // 动态添加可访问路由表
                if (from.path.indexOf("login") != -1) {
                  next({ path: "/index" });
                } else {
                  next({ ...to, replace: true });
                }
              });
            })
            .catch((err) => {
              Cookies.remove("Access-Token");
              Cookies.remove("vue_admin_template_kind");
              next({ path: "/index" });
              // store.dispatch("user/logout").then(() => {
              //   Message.error(err || "Verification failed, please login again");
              //   next({ path: "/" });
              // });
            });
        } else {
          // store.state.path = to.path;
          // to.path == "/index" ? store.state.hidden = true : store.state.hidden = false
          next();
        }
      }
    }
  } else {
    for (const key in localStorage) {
      if (key !== "hxcloudData") {
        localStorage.removeItem(key);
      }
    }
    Cookies.remove("Access-Token");
    Cookies.remove("vue_admin_template_kind");
    store.commit("user/SET_KING", "");
    resetRouter();
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next();
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`);
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  // finish progress bar
  NProgress.done();
});
function goPage(path, next) {
  // 获取当前路由是否携带参数

  var redirect = path.query.redirect;
  if (redirect) {
    next({ path: redirect });
  } else {
    next({ path: "/index" });
  }
}
