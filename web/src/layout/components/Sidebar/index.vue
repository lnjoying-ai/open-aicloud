<template>
  <div :class="{ 'has-logo': showLogo }">
    <!-- <div class="logoMain" :style="sidebar.opened ? 'width:20http://localhost:9528/0px' : 'width:199px'">
      <img src="@/assets/images/public/logo.png" alt="logo" class="logoImg" :class="{ active: this.sidebar.opened }" />
    </div> -->
    <el-menu class="pt-4" :default-active="activeMenu" :collapse="isCollapse" :background-color="variables.menuBg"
      :text-color="variables.menuText" :unique-opened="false" :active-text-color="variables.menuActiveText"
      :collapse-transition="false" mode="vertical">
      <sidebar-item v-for="route in routes" :key="route.path" :item="route" :base-path="route.path" />
    </el-menu>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/styles/variables.scss";
export default {
  components: { SidebarItem, Logo },
  data () {
    return {
      isActive: false,
      queryData: {
        page_size: 99999,
        page_num: 1,
      },
    }
  },
  created () {

  },
  mounted () {

  },
  methods: {

  },
  computed: {
    ...mapGetters(["sidebar", "routers"]),
    routes () {
      return this.routers;
    },
    activeMenu () {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }

      return path;

    },
    showLogo () {
      return this.$store.state.settings.sidebarLogo;
    },
    variables () {
      return variables;
    },
    isCollapse () {
      return false
    },
  },
};
</script>
<style lang="scss" scoped>
.logoMain {
  padding-top: 10px;
  height: 50px;
  margin: 0 auto;
  overflow: hidden;
  -webkit-transition: all 0.3s;
  transition: all 0.3s;

  .logoImg {
    height: 36px;
    margin-top: 1px;
    width: 121px;
    margin-left: 2px;
    max-width: 200px;
    display: inline-block;
  }

  .active {
    margin-left: 4px;
  }
}

.el-menu {
  height: calc(100vh - 50px);
  overflow: auto;
  padding-bottom: 50px;
  border-right: none;
  background: #ffffff !important;
  width: 200px !important;
}

::v-deep .el-menu-item,
.el-submenu__title {
  height: 48px;
  line-height: 48px;
}

::-webkit-scrollbar {
  width: 0px;
}

::-moz-scrollbar {
  width: 0px;
}

::-ms-scrollbar {
  width: 0px;
}
</style>
