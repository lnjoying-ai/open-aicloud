/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */

import i18n from "@/utils/i18n/index.js";

export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ["admin", "editor"];
  return valid_map.indexOf(str.trim()) >= 0;
}
export default {
  k8sName(rule, value, callback) {
    if (!value) {
      return callback(new Error(i18n.t("message.pleaseInputName")));
    }
    if (/^[a-zA-Z]([-_a-zA-Z0-9]{0,63})$/.test(value)) {
      callback();
    } else {
      callback(new Error(i18n.t("message.pleaseInputNameDesc")));
    }
  },
};
