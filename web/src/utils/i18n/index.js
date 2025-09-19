import Vue from "vue";
import VueI18n from "vue-i18n";
import zhCN from "./zh-CN.json";
import en from "./en.json";

// 导入 Element UI 语言包
import ElementUI from "element-ui";
import zhLocale from "element-ui/lib/locale/lang/zh-CN"; // 中文
import enLocale from "element-ui/lib/locale/lang/en"; // 英文

// 引入各个语言配置文件
Vue.use(VueI18n);

const language = localStorage.getItem("locale") || navigator.language;
localStorage.setItem("locale", language);

if (language == "en") {
  document.body.classList.add("languageEnStyle");
} else {
  document.body.classList.remove("languageEnStyle");
}

// 创建vue-i18n实例i18n
const i18n = new VueI18n({
  legacy: false, // 使用 Composition API 模式
  globalInjection: true, // 全局注册 $t 方法
  // 设置默认语言
  locale: language, // 语言标识
  // 添加多语言（每一个语言标示对应一个语言文件）
  messages: {
    "zh-CN": { ...zhCN, ...zhLocale },
    en: { ...en, ...enLocale },
  },
});
Vue.use(ElementUI, {
  i18n: (key, value) => i18n.t(key, value),
});

// 暴露i18n
export default i18n;
