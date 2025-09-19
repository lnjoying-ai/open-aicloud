<template>
  <div>
    <div class="transferListLoading">
      <el-badge :value="listNumber" class="item">
        <img src="@/assets/transfer/transfer.png" alt="" style="width: 100%" />
      </el-badge>
    </div>
  </div>
</template>
<script>
export default {
  components: {},
  props: {
    listNumber: {
      type: Number,
      default: 0,
    },
  },
  data () {
    return {};
  },
  watch: {},

  methods: {},
  mounted () { },
};
</script>
<style lang="stylus" scoped>
.transferListLoading {
  width: 100%;
  height: 100%;
  ::v-deep .el-badge {
    .el-badge__content {
      top: 0;
      right: 0;
      transform: translate(0, 0);
    }
  }
  .transferListLoadingMain {
    position: relative;
    width: 100%;
    height: 100%;
    background-color: #545c64;
    border-radius: 50%;
    box-shadow: 1px 1px 0px 1px rgba(255, 255, 75, 0.3);
    .div1 {
      position: absolute;
      top: 10%;
      right: 0;
      left: 0;
      display: block;
      margin: 0 auto;
      width: 80%;
      height: 80%;
      border-radius: 50%;
      border-top: 0px solid #fff;
      border-right: 3px solid #fff;
      border-bottom: 3px dotted #fff;
      border-left: 3px dotted #fff;
      animation: rotation 1s linear infinite;
    }
    .div2 {
      position: absolute;
      top: 20%;
      right: 0;
      left: 0;
      display: block;
      margin: 0 auto;
      width: 60%;
      height: 60%;
      border-radius: 50%;
      border-top: 3px dotted #ffd04b;
      border-right: 3px dotted #ffd04b;
      border-bottom: 0px solid #ffd04b;
      border-left: 3px solid #ffd04b;
      animation: rotation1 1s linear infinite;
    }
    .div3 {
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      color: #fff;
      text-align: center;
      .iconfont {
        font-size: 28px;
        font-weight: 100;
        background-color: #fff;
        -webkit-background-clip: text;
        color: transparent;
        -webkit-animation: bg-pan-right 4s both infinite;
        animation: bg-pan-right 4s both infinite;
      }
    }
  }
}
@keyframes rotation {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
@keyframes rotation1 {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(-360deg);
  }
}
@keyframes bg-pan-right {
  0% {
    background-color: #fff;
  }
  50% {
    background-color: #ffd04b;
  }
  100% {
    background-color: #fff;
  }
}
@keyframes bg-pan-right {
  0% {
    background-color: #fff;
  }
  50% {
    background-color: #ffd04b;
  }
  100% {
    background-color: #fff;
  }
}
</style>
