<template>
  <div class="statisticsPage">
    <el-tabs v-model="activeName">
      <el-tab-pane
        v-if="kind == 0 || kind == 1"
        :label="$t('form.overview')"
        name="overview"
      />
      <!-- <el-tab-pane label="资源监控"
                   name="monitor"> </el-tab-pane> -->
      <el-tab-pane :label="$t('form.cloudsMonitor')" name="clouds">
        <template #label>
          <span v-if="kind == 0 || kind == 1">{{
            $t("form.cloudsMonitor")
          }}</span>
          <span v-else>{{ $t("form.instanceMonitor") }}</span>
        </template>
      </el-tab-pane>
      <el-tab-pane
        v-if="kind == 0 || kind == 1"
        :label="$t('form.containerResourceMonitor')"
        name="monitor"
      />
    </el-tabs>
    <statistics1 v-if="activeName == 'overview'" />
    <statistics2 v-if="activeName == 'monitor'" />
    <statistics3 v-if="activeName == 'clouds'" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import statistics1 from "./statistics/statistics1";
import statistics2 from "./statistics/statistics2";
import statistics3 from "./statistics/statistics3";
export default {
  components: { statistics1, statistics2, statistics3 },
  mixins: [],
  computed: {
    ...mapGetters(["kind"]),
  },
  data() {
    return {
      activeName: "overview",
    };
  },
  watch: {
    activeName: {
      deep: true,
      handler(val) {
        this.$router.push({
          query: {
            type: val,
          },
        });
      },
    },
  },
  created() {},
  mounted() {
    if (this.$route.query.type) {
      this.activeName = this.$route.query.type;
    } else {
      if (this.kind == 0 || this.kind == 1) {
        this.activeName = "overview";
      } else {
        this.activeName = "clouds";
      }
    }
  },
  methods: {},
};
</script>

<style lang="scss" scoped>
.statisticsPage {
  padding: 0 10px;

  ::v-deep .el-tabs {
    .el-tabs__header {
      background-color: #fff;
      padding-left: 30px;
      border-top-left-radius: 8px;
      border-top-right-radius: 8px;

      .el-tabs__nav-wrap {
        &::after {
          display: none;
        }
      }
    }
  }
}
</style>
