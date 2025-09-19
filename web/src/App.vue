<template>
  <div id="app">
    <router-view v-if="reloadPage" />
  </div>
</template>

<script>
export default {
  name: "App",
  components: {},
  data() {
    return {
      reloadPage: true,
    };
  },
  watch: {
    "$store.state.app.hxcloudData"(val) {
      // 不刷新页面 重新执行methods
      this.reloadPage = false;
      this.$nextTick(() => {
        this.reloadPage = true;
      });
      // reload
      // setTimeout(() => {
      //   this.$router.go(0);
      // })
    },
  },

  created() {
    this.$store.dispatch("app/initStore");
    this.OSnow();
  },
  mounted() {},
  methods: {
    OSnow() {
      var agent = navigator.userAgent.toLowerCase();
      console.log(agent);
      var isMac = /macintosh|mac os x/i.test(navigator.userAgent);
      console.log(isMac);

      if (agent.indexOf("win32") >= 0 || agent.indexOf("wow32") >= 0) {
      }
      if (agent.indexOf("win64") >= 0 || agent.indexOf("wow64") >= 0) {
        this.$store.state.transfer.windowstatus = true;
        this.$store.state.transfer.macstatus = false;
      }
      if (isMac) {
        this.$store.state.transfer.windowstatus = false;
        this.$store.state.transfer.macstatus = true;
      }
    },
  },
};
</script>

<style lang="scss"></style>
