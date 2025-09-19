const { color } = require("echarts");

module.exports = {
  purge: [],
  important: true,
  darkMode: false, // or 'media' or 'class'
  theme: {
    extend: {},
  },
  variants: {
    extend: {
      display: ["group-hover"],
    },
  },
  // corePlugins: {
  //   preflight: false,
  // },
  plugins: [],
};
