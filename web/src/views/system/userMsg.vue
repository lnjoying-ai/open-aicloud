/* eslint-disable space-before-blocks */
<template>
  <div class="warpMain">
    <el-container>
      <el-aside width="200px">
        <el-menu
          v-model="activeIndex"
          :default-active="activeIndex"
          class="el-menu-demo"
          @select="handleSelect"
        >
          <el-menu-item index="1">{{ $t("form.basicInfo") }}</el-menu-item>
          <el-menu-item index="2">{{
            $t("form.securitySettings")
          }}</el-menu-item>
          <el-menu-item v-if="kind == '0' || kind == '1'" index="3">{{
            $t("form.inviteLink")
          }}</el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <userMsg v-if="activeIndex == 1" ref="userMsg" />
        <safeCenter v-if="activeIndex == 2" ref="safeCenter" />
        <invite v-if="activeIndex == 3" ref="invite" />
      </el-main>
    </el-container>
  </div>
</template>

<script>
import userMsg from "./userMsg/baseMsg.vue";
import safeCenter from "./userMsg/safeCenter.vue";
import invite from "./userMsg/invite.vue";
import { mapGetters } from "vuex";
export default {
  components: {
    userMsg,
    safeCenter,
    invite,
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  data() {
    return {
      activeIndex: "1",
    };
  },
  mounted() {
    if (this.$route.query.activeIndex) {
      this.activeIndex = this.$route.query.activeIndex;
    }
  },
  methods: {
    // 切换菜单
    handleSelect(e) {
      this.activeIndex = e;
    },
  },
};
</script>

<style lang="scss" scoped>
.is-active {
  border-right: 3px solid #409eff;
}
</style>
