<template>
  <div class="warpMain">
    <container-list ref="containerList" :api-type="'deploy'" />
  </div>
</template>

<script>
import containerList from "@/views/system/container";

export default {
  components: { containerList },
  data() {
    return {};
  },
  computed: {},
  created() {
    this.$route.meta.smalltitle =
      this.$t("form.deploy") + "：" + this.$route.params.name;
  },
  mounted() {},
  methods: {},
};
</script>
<style lang="scss" scoped></style>
