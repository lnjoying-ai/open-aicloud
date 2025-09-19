<template>
  <div class="footerPage" :style="'background-color:' + bgColor">
    <div class="footerMain">
      <el-row class="pt-14 pb-6">
        <el-col :span="12" :xs="24">
          <p class="text-sm text-gray-700 mb-3">
            {{ $t("other.powerServiceConsult") }}：service@lnjoying.com
          </p>
          <p class="text-sm text-gray-700 mb-3">
            {{ $t("other.phone") }}：<a href="tel:4001107899">400-110-7899</a>
          </p>
          <p class="text-sm text-gray-700 mb-3">
            {{ $t("other.email") }}：service@lnjoying.com
          </p>
          <p class="text-sm text-gray-700 mb-3">
            {{ $t("other.address") }}：北京市海淀区双清路学研大厦
          </p>
        </el-col>
        <el-col :span="12" :xs="24">
          <div class="float-right qrCode">
            <img src="@/assets/images/public/qrCode.jpg" alt="" class="w-28" />
            <p class="text-center text-sm">{{ $t("other.scanCode") }}</p>
          </div>
        </el-col>
      </el-row>
      <div class="footerBottom pt-4 pb-2">
        <p class="text-gray-500">
          Copyright © 2024 秒如科技 LNJOYING
          <span class="pl-4"
            ><a href="https://beian.miit.gov.cn">京ICP备19050439号</a></span
          >
        </p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Footer",
  props: {
    bgColor: {
      type: String,
      default: "#ecf2fb",
    },
  },
  data() {
    return {};
  },
  mounted() {},
  methods: {},
};
</script>

<style lang="scss" scoped>
.footerBottom {
  font-size: 12px;
  text-align: center;
}

.footerPage {
  .footerMain {
    max-width: 1400px;
    margin: 0 auto;
    padding: 0 15px;
    .qrCode {
      width: 400px;
      text-align: center;

      img {
        display: inline-block;
      }
    }
  }
}

@media (max-width: 768px) {
  .footerPage {
    .footerMain {
      .qrCode {
        width: 100%;
        margin-top: 20px;
        text-align: center;
        float: none !important;

        img {
          display: inline-block;
        }
      }
    }
  }
}
</style>
