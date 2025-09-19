<script>
import Layout from "@/layout";
export default {
  name: "MenuItem",
  components: {
    Layout,
  },
  functional: true,
  props: {
    icon: {
      type: String,
      default: "",
    },
    title: {
      type: String,
      default: "",
    },
    layout: {
      type: String,
      default: "",
    },
  },
  render(h, context) {
    const { icon, title, layout } = context.props;
    const vnodes = [];

    if (icon) {
      if (icon.includes("el-icon")) {
        vnodes.push(<i class={[icon, "sub-el-icon"]} />);
      } else {
        vnodes.push(<i class={["iconfont", icon]} />)
        // vnodes.push(<svg-icon icon-class={icon} />);
      }
    }

    if (title) {
      vnodes.push(<span slot="title">{title}</span>);
    }
    if (layout) {
      vnodes.push(<Layout />);
    }

    return vnodes;
  },
};
</script>

<style scoped>
.sub-el-icon {
  color: currentColor;
  width: 1em;
  height: 1em;
}

.iconfont {
  font-family: "iconfont" !important;
  font-size: 18px;
  margin-right: 12px;
  /* margin-left: -2px; */
  font-style: normal;
  color: rgba(0, 0, 0, 0.65);
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

::v-deep .el-menu-item i {
  color: #909399;
  font-size: 18px;
}
</style>
