<template>
  <section class="app-main">
    <div v-show="!$store.state.settings.hidePageTitle" class="table_head">
      <a v-show="$route.meta.showBack" class="btn-back" @click="ruturnBtn">
        <span>{{ $t("domain.back") }}</span>
      </a>
      <span>{{ $t("router." + $route.meta.languageTitle) }}</span>
    </div>
    <transition name="fade-transform" mode="out-in">
      <router-view :key="key" />
    </transition>
    <!-- <BottomControl></BottomControl> -->
  </section>
</template>
<script>
// import BottomControl from "@/components/BottomControl";
export default {
  name: "AppMain",
  components: {
    // BottomControl,
  },
  data() {
    return {
      tableTxt: "",
    };
  },
  computed: {
    key() {
      return this.$route.path;
    },
  },
  watch: {
    "$route.path": {
      deep: true,
      handler(val) {
        // this.$route.meta.smalltitle = this.$route.meta.title;
        this.$route.meta.smalltitle = this.$t(
          "router." + this.$route.meta.languageTitle
        );
      },
    },
  },
  methods: {
    ruturnBtn() {
      if (window.history.length <= 1) {
        this.$router.push({
          path: "/index",
        });
        return false;
      } else if (
        this.$route.name == "userMsg" ||
        this.$route.name == "wareserveDetail" ||
        this.$route.name == "serviceFromApp" ||
        this.$route.name == "containerFromService" ||
        this.$route.name == "ContainDetail" ||
        this.$route.name == "containerFromService" ||
        this.$route.name == "nodeList"
      ) {
        window.history.go(-1);
      } else if (
        this.$route.name == "alarmdetails" ||
        this.$route.name == "addalarm"
      ) {
        this.$router.push("/alarmmanagement");
      } else {
        this.$router.push(this.$route.meta.activeMenu);
        // console.log(this.$route.meta.activeMenu);
      }
    },
  },
};
</script>

<style scoped>
.app-main {
  /*50 = navbar  */
  min-height: calc(100vh - 50px);
  background-color: #f3f4f6;
  width: 100%;
  position: relative;
  overflow: hidden;
  padding: 15px 10px;
}

svg {
  display: inline-block;
  vertical-align: top;
  margin: 0px 2px 0px 0px;
}

.fixed-header + .app-main {
  padding-top: 50px;
}
</style>

<style lang="scss">
// fix css style bug in open el-dialog
.el-popup-parent--hidden {
  .fixed-header {
    padding-right: 15px;
  }
}

.table_head {
  background: #ffffff;
  font-size: 17px;
  line-height: 20px;
  font-weight: bold;
  color: #333333;
  padding: 20px 15px 15px;
  box-sizing: border-box;
  text-align: left;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.btn-back {
  text-decoration: none;
  position: relative;
  display: inline-block;
  margin-right: 10px;
}

.btn-back:before {
  content: "";
  display: inline-block;
  width: 20px;
  height: 20px;
  border: 1px solid #409eff;
  -webkit-transform-origin: 0 0;
  -moz-transform-origin: 0 0;
  -ms-transform-origin: 0 0;
  -webkit-transform: scaleX(0.8) rotate(45deg);
  -moz-transform: scaleX(0.8) rotate(45deg);
  -ms-transform: scaleX(0.8) rotate(45deg);
  border-radius: 3px 2px;
  position: absolute;
  left: 13px;
  top: -1px;
  background-color: #409eff;
}

.btn-back span {
  display: inline-block;
  font-size: 13px;
  line-height: 27px;
  height: 27px;
  padding: 0 10px 0 5px;
  // border: 1px solid #409EFF;
  border-left: 0;
  border-radius: 2px 5px 5px 2px;
  margin-left: 12px;
  position: relative;
  color: #ffffff;
  z-index: 2;
  // box-shadow: 0 1px 0 rgba(255, 255, 255, .3) inset, 1px 1px 1px rgba(255, 255, 255, .2);
  background-color: #409eff;
}
</style>
