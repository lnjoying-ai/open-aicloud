<template>
  <div class="warpMain">
    <container-list :api-type="'service'" style="margin-top: 0px" />
  </div>
</template>

<script>
import initData from "@/mixins/initData";
import containerList from "@/views/system/container";

export default {
  components: { containerList },
  mixins: [initData],

  data() {
    return {};
  },

  created() {
    this.$route.meta.smalltitle =
      this.$t("form.belongService") + ":" + this.$route.params.name;
  },
  mounted() {},

  methods: {},
};
</script>
