<template>
  <div class="welcomePage">
    <!-- <div
      class="welcomeNotice text-center text-white   text-sm fixed top-0 right-0 left-0 z-20 mx-auto bg-gradient-to-r from-blue-400 via-purple-500 to-blue-400">
      91GPU.cloud：更多GPU即将上架(原51GPT.cloud)
    </div> -->
    <!-- <div style="height:92px"> -->
    <div style="height: 60px">
      <!-- <Head style="margin-top: 32px;"></Head> -->

      <Head />
    </div>
    <div class="welcomePageTop">
      <div class="welcomemain">
        <div class="w-full h-full relative">
          <div
            class="welcomeBg2 text-center text-sm absolute bottom-0 right-0 left-0 z-10 mx-auto backdrop-filter backdrop-blur-md"
          >
            <ul class="text-center text-white">
              <li class="inline-block w-60 mx-20 cursor-pointer">
                <a
                  class="block py-4 w-full h-full"
                  @click.prevent="anchor('1')"
                >
                  <span class="title block text-lg font-medium mb-2">{{
                    $t("welcomePage.ourAdvantages")
                  }}</span>
                  <span class="text block text-sm">{{
                    $t("welcomePage.whyChoose91GPU")
                  }}</span>
                </a>
              </li>
              <li class="inline-block w-60 mx-20 cursor-pointer">
                <a
                  class="block py-4 w-full h-full"
                  @click.prevent="anchor('2')"
                >
                  <span class="title block text-lg font-medium mb-2">{{
                    $t("welcomePage.ourResources")
                  }}</span>
                  <span class="text block text-sm">{{
                    $t("welcomePage.GPUResources")
                  }}</span>
                </a>
              </li>
            </ul>
          </div>
          <el-carousel
            trigger="click"
            height="700px"
            :interval="6000"
            arrow="never"
            class="welcomeCarousel"
          >
            <el-carousel-item>
              <div class="welcomeCarouselItem welcomeCarouselItem1 h-full">
                <div class="welcomecontent1">
                  <h1 class="title">91GPU.Cloud</h1>
                  <h2 class="smallTitle">
                    {{ $t("welcomePage.bannerTitle1") }}
                  </h2>
                </div>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="welcomeCarouselItem welcomeCarouselItem2 h-full">
                <div class="welcomecontent2">
                  <h3 class="title">ElasticCloud</h3>
                  <h3 class="smallTitle">
                    {{ $t("welcomePage.bannerTitle2") }}
                  </h3>

                  <p class="text" v-html="$t('welcomePage.bannerText1')" />
                  <p class="text1" v-html="$t('welcomePage.bannerText2')" />
                </div>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="welcomeCarouselItem welcomeCarouselItem3 h-full">
                <div class="welcomecontent3">
                  <h3 class="title">{{ $t("welcomePage.bannerTitle3") }}</h3>
                  <h3 class="smallTitle">
                    {{ $t("welcomePage.bannerText3") }}
                  </h3>
                </div>
              </div>
            </el-carousel-item>
            <el-carousel-item>
              <div class="welcomeCarouselItem welcomeCarouselItem4 h-full">
                <div class="welcomecontent4">
                  <h1 class="title">
                    <span>{{ $t("welcomePage.bannerTitle4") }}</span
                    ><span>{{ $t("welcomePage.bannerText4") }}</span>
                  </h1>
                </div>
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
      </div>
    </div>
    <div id="1" class="welcomePage2 relative">
      <div class="mx-auto text-center welcomePage2Content">
        <h5 class="text-4xl font-medium mt-10 mb-10">
          {{ $t("welcomePage.ourService") }}
        </h5>
        <p class="mb-20 text-gray-700">
          {{ $t("welcomePage.ourServiceText") }}
        </p>
        <el-row class="itemRow" :gutter="20">
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-jiqun2" />
              </span>
              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle1") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText1") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-danxingsuanlitiaodu" />
              </span>
              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle2") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText2") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-sae" />
              </span>
              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle3") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText3") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-shujuguanli" />
              </span>

              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle4") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText4") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-wenjianchuanshu" />
              </span>
              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle5") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText5") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-shujuzaibei" />
              </span>
              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle6") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText6") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-yunying" />
              </span>
              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle7") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText7") }}
              </p>
            </div>
          </el-col>
          <el-col :span="6" class="itemCol mb-6">
            <div class="itemColContent px-5 pt-2 pb-2">
              <span
                class="itemColContentImg bg-clip-text text-transparent bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500"
              >
                <span class="icon iconfont text-5xl icon-yunwei" />
              </span>

              <h6 class="text-lg font-medium">
                {{ $t("welcomePage.ourServiceTitle8") }}
              </h6>
              <span
                class="block mx-auto mt-2 mb-6 w-10 h-1 bg-blue-500 rounded"
              />
              <p class="text-sm text-gray-700">
                {{ $t("welcomePage.ourServiceText8") }}
              </p>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div id="2" class="welcomePage3 relative">
      <div class="h-full backdrop-filter backdrop-blur">
        <div class="mx-auto text-center welcomePage3Content">
          <h5 class="text-4xl font-medium mt-10 mb-10">
            {{ $t("welcomePage.ourGPUResource") }}
          </h5>
          <p class="mb-20 text-gray-700">
            {{ $t("welcomePage.GPUCloudServerText") }}
          </p>
          <div class="suanliTypeMain">
            <ul class="inline-block rounded-xl py-1.5 mb-12 changeResourceType">
              <li
                class="inline-block text-lg font-medium mx-4 px-12 leading-loose rounded-xl cursor-pointer"
                :class="suanliType == 1 ? 'active' : ''"
                @click="changeSuanliType(1)"
              >
                {{ $t("welcomePage.platformRecommend") }}
              </li>
              <li
                class="inline-block text-lg font-medium mx-4 px-12 leading-loose rounded-xl cursor-pointer"
                :class="suanliType == 2 ? 'active' : ''"
                @click="changeSuanliType(2)"
              >
                {{ $t("welcomePage.highPerformanceComputing") }}
              </li>
            </ul>
            <div class="text-left">
              <el-row :gutter="20">
                <el-col
                  v-for="(item, index) in GPUDatas[suanliType - 1]"
                  :key="index"
                  :span="6"
                  :offset="
                    index == 0
                      ? ((100 - GPUDatas[suanliType - 1].length * 25) /
                          2 /
                          100) *
                        24
                      : 0
                  "
                >
                  <div class="bg-white rounded-xl overflow-hidden">
                    <div
                      class="pt-2 px-4"
                      style="background-color: rgba(240, 240, 240, 0.6)"
                    >
                      <div class="h-14 overflow-hidden pb-1 -mr-2 rounded-t-xl">
                        <span
                          v-if="item.isRecommend"
                          class="inline-block px-4 py-1 mt-4 text-white text-sm rounded-md"
                          style="background-color: rgb(251 146 60)"
                          >{{ $t("welcomePage.recommend") }}</span
                        >
                        <div v-if="item.isHot" class="float-right">
                          <span
                            class="inline-block bg-red-700 text-white text-sm px-9 pt-8 pb-1 transform rotate-45 translate-x-11 -translate-y-5"
                            >HOT</span
                          >
                        </div>
                      </div>
                      <h2 class="text-lg font-medium leading-none mb-2">
                        {{ item.name }}
                      </h2>
                      <div class="pb-4">
                        <span
                          v-for="(label, labelIndex) in item.label"
                          :key="labelIndex"
                          class="p-1 mr-1 text-xs text-blue-500 border-blue-500 border rounded"
                          >{{ label.name }}</span
                        >
                      </div>
                    </div>
                    <div class="p-4">
                      <ul>
                        <li
                          v-for="(content, contentIndex) in item.content"
                          :key="contentIndex"
                          class="border-b pt-2 pb-1"
                        >
                          <span class="block text-sm text-gray-600">{{
                            content.title
                          }}</span>
                          <span class="block text-xl">{{ content.text }}</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="3" class="welcomePage4 relative">
      <div class="h-full">
        <div class="mx-auto text-center welcomePage4Content">
          <img src="@/assets/images/logo.png" alt="" class="mx-auto w-44" />
          <h5 class="text-xl font-bold mt-10 mb-10 title4">
            {{ $t("welcomePage.ourMission") }}
          </h5>
          <p class="mb-10 text-black text-5xl font-bold text4">
            {{ $t("welcomePage.helpAIInnovation") }}
          </p>
          <p class="mb-6 text-xl text-gray-700 font-medium tips4">
            {{ $t("welcomePage.experienceNow") }}
          </p>
          <div>
            <el-button
              type="primary"
              size="large"
              class="w-36 mx-2 font-bold"
              @click="goExperience"
              >{{ $t("welcomePage.experienceNowBtn") }}</el-button
            >
            <a
              href="http://www.lnjoying.com"
              target="_blank"
              class="mx-2 font-bold"
            >
              <el-button size="large" class="w-36">{{
                $t("welcomePage.companyIntroduction")
              }}</el-button>
            </a>
          </div>
        </div>
      </div>
    </div>

    <Footer />
  </div>
</template>

<script>
import { getToken } from "@/utils/auth"; // get token from cookie
import Head from "@/components/head.vue";
import Footer from "@/layout/footer.vue";
export default {
  name: "Welcome",
  components: {
    Head,
    Footer,
  },
  data() {
    return {
      hasToken: false,
      suanliType: 1, // 1平台推荐 2高性能计算

      GPUDatas: [
        [
          {
            suanliType: 1,
            isHot: true,
            isRecommend: true,
            name: "NVIDIA RTX 4090*1",
            label: [
              {
                name: "GPU计算型",
                color: "#5e7ff3",
              },
            ],
            content: [
              {
                title: "处理器",
                text: "10C",
              },
              {
                title: "内存",
                text: "60GB",
              },
              {
                title: "单精",
                text: "82.58 TFLOPS",
              },
              {
                title: "半精",
                text: "165.2 Tensor TFLOPS",
              },
            ],
            unit: "时",
          },
          {
            suanliType: 1,
            isHot: true,
            isRecommend: true,
            name: "NVIDIA RTX 4090D*1",
            label: [
              {
                name: "GPU计算型",
                color: "#5e7ff3",
              },
            ],
            content: [
              {
                title: "处理器",
                text: "16C",
              },
              {
                title: "内存",
                text: "90GB",
              },
              {
                title: "单精",
                text: "73.54 TFLOPS",
              },
              {
                title: "半精",
                text: "147 Tensor TFLOPS",
              },
            ],
            unit: "时",
          },
          {
            suanliType: 1,
            isHot: true,
            isRecommend: true,
            name: "NVIDIA L20*1",
            label: [
              {
                name: "GPU计算型",
                color: "#5e7ff3",
              },
            ],
            content: [
              {
                title: "处理器",
                text: "24C",
              },
              {
                title: "内存",
                text: "200GB",
              },
              {
                title: "单精",
                text: "59.35 TFLOPS",
              },
              {
                title: "半精",
                text: "119.5 Tensor TFLOPS",
              },
            ],
            unit: "时",
          },
          {
            suanliType: 1,
            isHot: false,
            isRecommend: true,
            name: "NVIDIA RTX 3080 Ti*1",
            label: [
              {
                name: "GPU计算型",
                color: "#5e7ff3",
              },
            ],
            content: [
              {
                title: "处理器",
                text: "12C",
              },
              {
                title: "内存",
                text: "24GB",
              },
              {
                title: "单精",
                text: "34.10 TFLOPS",
              },
              {
                title: "半精",
                text: "70 Tensor TFLOPS",
              },
            ],
            unit: "时",
          },
        ],
        [
          {
            suanliType: 2,
            isHot: true,
            isRecommend: true,
            name: "NVIDIA A800*1",
            label: [
              {
                name: "GPU计算型",
                color: "#5e7ff3",
              },
            ],
            content: [
              {
                title: "处理器",
                text: "24C",
              },
              {
                title: "内存",
                text: "200GB",
              },
              {
                title: "单精",
                text: "19.5 TFLOPS",
              },
              {
                title: "半精",
                text: "312 Tensor TFLOPS",
              },
            ],
            unit: "时",
          },
          {
            suanliType: 2,
            isHot: true,
            isRecommend: true,
            name: "高性能算力卡*1",
            label: [
              {
                name: "GPU计算型",
                color: "#5e7ff3",
              },
            ],
            content: [
              {
                title: "处理器",
                text: "24C",
              },
              {
                title: "内存",
                text: "200GB",
              },
              {
                title: "单精",
                text: "60 TFLOPS",
              },
              {
                title: "半精",
                text: "1979 Tensor TFLOPS",
              },
            ],
            unit: "时",
          },
        ],
      ],
    };
  },

  created() {
    this.init();
  },
  methods: {
    goExperience() {
      const hasToken = getToken();
      if (hasToken) {
        this.$router.push("/index");
      } else {
        this.$router.push("/login");
      }
    },
    changeSuanliType(type) {
      // 1平台推荐 2高性能计算
      this.suanliType = type;
    },
    anchor(anchorName) {
      const anchorElement = document.getElementById(anchorName);
      if (anchorElement) {
        anchorElement.scrollIntoView();
      }
    },
    init() {
      const hasToken = getToken();
      if (hasToken) {
        this.hasToken = true;
      } else {
        this.hasToken = false;
      }
    },
  },
};
</script>

<style lang="scss" scpoed>
.welcomePage {
  width: 100%;
  height: 100vh;
  min-height: 600px;
  min-width: 1200px;
  overflow: auto;

  .welcomeNotice {
    padding: 6px 0;
    line-height: 20px;
  }

  &::-webkit-scrollbar-track-piece {
    background: #d3dce6;
  }

  &::-webkit-scrollbar {
    width: 0px;
    height: 0px;
  }

  &::-webkit-scrollbar-thumb {
    background: #99a9bf;
    border-radius: 20px;
  }

  .welcomePageTop {
    // height: 100vh;
    position: relative;

    .welcomeCarousel {
      .el-carousel__indicators {
        bottom: 100px;

        .el-carousel__indicator {
          &.is-active {
            .el-carousel__button {
              width: 30px;
              opacity: 1;
              border-radius: 8px;
              background-color: rgb(59, 130, 246);
            }
          }

          .el-carousel__button {
            width: 8px;
            height: 8px;
            opacity: 1;
            border-radius: 8px;
          }
        }
      }

      .el-carousel__item {
        &.is-active {
          .welcomecontent2 {
            .title {
              opacity: 1;
              transform: translateX(0%);
            }

            .smallTitle {
              opacity: 1;
              transform: translateX(0%);
            }

            .text {
              opacity: 1;
              transform: translateX(0%);
            }

            .text1 {
              opacity: 1;
              transform: translateX(0%);
            }

            .btn {
              opacity: 1;
              transform: translateX(0%);
            }
          }

          .welcomecontent3 {
            .title {
              opacity: 1;
              transform: translateX(0%);
            }

            .smallTitle {
              opacity: 1;
              transform: translateX(0%);
            }
          }
        }
      }
    }
  }
}

.welcomemain {
  width: 100%;
  overflow: hidden;

  .welcomeBg2 {
    background-color: rgba(240, 240, 240, 0.2);

    li {
      &:hover {
        background-color: rgba(240, 240, 240, 0.2);
        -webkit-backdrop-filter: blur(12px);
        backdrop-filter: blur(12px);
      }
    }
  }

  .welcomeCarouselItem {
    background-color: #000;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
  }

  .welcomeCarouselItem1 {
    background-image: url("~@/assets/images/welcome/banner/1.jpg");

    .welcomecontent1 {
      position: absolute;
      width: 1200px;
      margin: 0 auto;
      text-align: center;
      top: 30%;
      right: 0;
      left: 0;
      transform: translateY(-50%);

      .title {
        margin-bottom: 10px;
        color: #fff;
        font-size: 62px;
        text-shadow: 0px 0px 3px #000;
      }

      .smallTitle {
        color: #fff;
        font-size: 40px;
        text-shadow: 0px 0px 3px #000;
      }
    }
  }

  .welcomeCarouselItem2 {
    background-image: url("~@/assets/images/welcome/banner/2.jpg");

    .welcomecontent2 {
      position: absolute;
      width: 1200px;
      top: 50%;
      left: 15%;
      transform: translateY(-50%);

      .title {
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        color: #fff;
        font-size: 44px;
      }

      .smallTitle {
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        color: #fff;
        font-size: 36px;
        margin-bottom: 40px;
      }

      .text {
        display: block;
        word-break: break-all;
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        color: #f1f1f1;
        width: 460px;
        margin-bottom: 40px;
      }

      .text1 {
        display: none;
        word-break: break-all;
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        color: #f1f1f1;
        width: 460px;
        margin-bottom: 40px;
      }

      .btn {
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        cursor: pointer;
        color: #000;
        font-size: 14px;
        background-color: #91f3fc;
        padding: 5px 15px;
        border-radius: 20px;
        box-shadow: 0px 0px 4px 0px #91f3fc;
      }
    }
  }

  .welcomeCarouselItem3 {
    background-image: url("~@/assets/images/welcome/banner/3.jpg");

    .welcomecontent3 {
      position: absolute;
      width: 1200px;
      top: 40%;
      left: 15%;
      transform: translateY(-50%);

      .title {
        margin-bottom: 10px;
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        color: #fff;
        font-size: 48px;
      }

      .smallTitle {
        opacity: 0;
        transform: translateX(-100%);
        transition: all 0.5s ease-in-out 0.6s;
        color: #fff;
        font-size: 28px;
        margin-bottom: 40px;
      }
    }
  }

  .welcomeCarouselItem4 {
    background-image: url("~@/assets/images/welcome/banner/4.jpg");

    .welcomecontent4 {
      position: absolute;
      width: 1200px;
      margin: 0 auto;
      text-align: center;
      top: 40%;
      right: 0;
      left: 0;
      transform: translateY(-50%);

      .title {
        color: #fff;
        font-size: 54px;
        font-weight: 700;

        span {
          padding: 0 15px;
        }
      }
    }
  }
}

.welcomePage2 {
  background-color: #ecf2fb;

  .welcomePage2Content {
    width: 1200px;
    padding-top: 50px;
    padding-bottom: 100px;

    .itemRow {
      .itemCol {
        .itemColContent {
          height: 300px;
          background-color: #fff;
          border: 2px solid #f1f1f1;
          border-radius: 10px;

          .itemColContentImg {
            display: inline-block;
            margin: 30px auto 40px;
          }
        }
      }
    }
  }
}

.welcomePage3 {
  background-color: #ecf2fb;
  background-image: url("~@/assets/images/welcome/bg.jpg");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;

  .welcomePage3Content {
    width: 1200px;
    padding-top: 50px;
    padding-bottom: 100px;

    .suanliTypeMain {
      ul {
        background-color: rgba(255, 255, 255, 0.6);

        li {
          &.active {
            color: #5e7ff3;
            background-color: #fff;
          }
        }
      }

      .SoldOutDiv {
        height: 28px;
        position: relative;

        .SoldOutImg {
          position: absolute;
          width: 100px;
          height: 100px;
          top: -50px;
          opacity: 0.5;
        }
      }
    }
  }
}

.welcomePage4 {
  background-color: #ecf2fb;

  .welcomePage4Content {
    width: 1200px;
    padding-top: 120px;
    padding-bottom: 150px;
  }
}

@media (max-width: 768px) {
  .welcomePage {
    min-width: auto;
  }

  .welcomeNotice {
    font-size: 12px !important;
  }

  .welcomemain {
    .welcomeCarousel {
      .el-carousel__container {
        height: 500px !important;
      }

      .welcomeCarouselItem1 {
        .welcomecontent1 {
          width: 100%;
          top: 30%;
          transform: translateY(-50%);
          left: 0;

          .title {
            font-size: 28px;
          }

          .smallTitle {
            font-size: 20px;
          }
        }
      }

      .welcomeCarouselItem2 {
        .welcomecontent2 {
          width: 100%;
          top: 40%;
          transform: translateY(-50%);
          left: 0;
          padding: 0 15px;

          .title {
            font-size: 24px;
          }

          .smallTitle {
            font-size: 16px;
          }

          .text {
            width: 100%;
            display: none;
          }

          .text1 {
            width: 100%;
            display: block;
            font-size: 12px;
          }
        }
      }

      .welcomeCarouselItem3 {
        .welcomecontent3 {
          width: 100%;
          top: 40%;
          transform: translateY(-50%);
          left: 0;
          padding: 0 15px;

          .title {
            font-size: 28px;
          }

          .smallTitle {
            font-size: 20px;
          }
        }
      }

      .welcomeCarouselItem4 {
        .welcomecontent4 {
          width: 100%;
          top: 40%;
          transform: translateY(-50%);
          left: 0;

          .title {
            font-size: 28px;

            span {
              display: block;
              padding: 0 0;
            }
          }
        }
      }
    }
  }

  .welcomeBg2 {
    ul {
      overflow: hidden;

      li {
        float: left;
        width: 50% !important;
        margin-left: 0 !important;
        margin-right: 0 !important;

        &:hover {
          background: transparent !important;
        }

        .title {
          font-size: 14px !important;
        }

        .text {
          font-size: 12px !important;
        }
      }
    }
  }

  .welcomePage2 {
    height: auto;

    .welcomePage2Content {
      width: 100%;
      height: auto;
      padding: 20px 15px 50px;

      & > p {
        margin-bottom: 30px !important;
        font-size: 14px !important;
      }

      h5 {
        font-size: 24px !important;
      }

      .itemRow {
        .itemCol {
          width: 50%;
          margin-bottom: 10px !important;
          padding-right: 5px !important;
          padding-left: 5px !important;

          .itemColContent {
            height: auto;
            padding-bottom: 4px !important;
            padding-right: 10px !important;
            padding-left: 10px !important;

            h6 {
              font-size: 16px !important;
            }

            p {
              font-size: 12px !important;
              line-height: 16px !important;
              height: 64px !important;
            }

            .itemColContentImg {
              display: inline-block;

              margin: 15px auto 5px;
            }
          }
        }
      }
    }
  }

  .welcomePage3 {
    width: 100%;
    height: auto;
    background-color: #ecf2fb;

    .welcomePage3Content {
      width: 100%;
      height: auto;
      padding: 30px 15px 30px;

      & > p {
        margin-bottom: 30px !important;
        font-size: 14px !important;
      }

      h5 {
        font-size: 24px !important;
      }

      .suanliTypeMain {
        .el-row {
          .el-col {
            width: 100% !important;
            margin-bottom: 15px !important;
            margin-left: 0 !important;
          }
        }

        .changeResourceType {
          widows: 100%;

          li {
            margin: 0 5px !important;
            padding: 0px 15px !important;
            font-size: 16px !important;
          }
        }

        .viewAllResourceBtn {
          padding: 20px 0 !important;
        }
      }
    }
  }

  .welcomePage4 {
    .welcomePage4Content {
      width: 100%;
      padding-top: 50px;
      padding-bottom: 50px;

      h5 {
        font-size: 20px !important;
      }

      .text4 {
        font-size: 24px !important;
      }

      .tips4 {
        font-size: 14px !important;
      }
    }
  }
}
</style>
