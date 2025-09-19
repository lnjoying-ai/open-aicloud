import Vue from "vue";
import {} from "vuex";
import Router from "vue-router";

Vue.use(Router);

/* Layout */
import Layout from "@/layout";

export const constantRouterMap = [
  {
    path: "/",
    name: "welcome",
    component: () => import("@/views/login/welcome"),
    meta: {
      title: "91GPU算力云",
      smalltitle: "91GPU算力云",
      languageTitle: "welcome",
      icon: "icon-houtaizonglan",
      group: "index",
      showSlider: false,
      showBack: false,
    },
    authority: ["system", "admin", "bp_Admin", "personal", "bp_user"],
  },
  {
    path: "/login",
    name: "login",
    component: () => import("@/views/login/index"),
    hidden: true,
  },

  {
    path: "/register",
    component: () => import("@/views/login/register"),
    hidden: true,
  },
  {
    path: "/Terms-of-Service",
    component: () => import("@/views/login/terms"),
    hidden: true,
  },
  {
    path: "/404",
    component: () => import("@/views/404"),
    hidden: true,
  },
];

const createRouter = () =>
  new Router({
    mode: "history", // require service support

    scrollBehavior: () => ({ y: 0 }),
    routes: constantRouterMap,
  });

const router = createRouter();
export function resetRouter() {
  const newRouter = createRouter();
  router.matcher = newRouter.matcher; // reset router
}

export default router;
