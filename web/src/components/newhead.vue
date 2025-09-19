<template>
  <div class="navbar">
    <div class="left-menu">
      <el-button
        style="
          background-color: rgba(255, 255, 255, 0.2);
          border-color: transparent;
          color: rgb(204, 208, 212);
          font-weight: normal;
          width: 50px;
          height: 50px;
          text-align: center;
          border-radius: none;
        "
        class="hover float-left"
        @click="handelDrawerOpen"
      >
        <svg
          t="1684930103131"
          class="icon"
          viewBox="0 0 1024 1024"
          version="1.1"
          xmlns="http://www.w3.org/2000/svg"
          p-id="26296"
          width="14"
          height="14"
        >
          <path
            d="M896 128H128c-17.7 0-32 14.3-32 32s14.3 32 32 32h768c17.7 0 32-14.3 32-32s-14.3-32-32-32zM896 480H128c-17.7 0-32 14.3-32 32s14.3 32 32 32h768c17.7 0 32-14.3 32-32s-14.3-32-32-32zM896 832H128c-17.7 0-32 14.3-32 32s14.3 32 32 32h768c17.7 0 32-14.3 32-32s-14.3-32-32-32z"
            fill="#ffffff"
            p-id="26297"
          />
        </svg>
      </el-button>
      <div
        class="inline-block"
        style="height: 50px; vertical-align: top; padding-top: 7px"
      >
        <img src="@/assets/images/public/logo.png" alt="logo" class="logoImg" />
      </div>
      <div
        v-if="kind == 0 || kind == 1"
        class="inline-block h-8 leading-8 mr-4 align-top mt-2.5"
      >
        <router-link to="/index">
          <span class="sumtitle rounded">{{
            $t("titleAndText.overview")
          }}</span>
        </router-link>
      </div>

      <div v-if="isShowCloudsChange()" class="inline-block h-8 mt-2.5 rounded">
        <div class="inline-block cloudChangeStyle h-8 pt-1 rounded">
          <el-popover
            v-if="cloudListData && cloudListData.length > 0"
            placement="bottom-start"
            trigger="click"
          >
            <div slot="reference">
              <div class="overflow-hidden mb-4 px-4 cloudChangeList">
                <span class="inline-block float-left mr-2 w-12 h-6">
                  <span v-if="cloudNowData.usage || cloudNowData.usage === 0">
                    <el-tag
                      v-if="cloudNowData.usage < 0.5"
                      type="success"
                      size="small"
                      class="align-top cloudUsageStatus"
                      effect="dark"
                    >
                      {{ $t("statusAndType.idle") }}
                    </el-tag>
                    <el-tag
                      v-if="
                        cloudNowData.usage >= 0.5 && cloudNowData.usage < 0.9
                      "
                      type="warning"
                      size="small"
                      class="align-top cloudUsageStatus"
                      effect="dark"
                    >
                      {{ $t("statusAndType.busy") }}
                    </el-tag>
                    <el-tag
                      v-if="cloudNowData.usage > 0.9 && cloudNowData.usage < 1"
                      type="danger"
                      size="small"
                      class="align-top cloudUsageStatus"
                      effect="dark"
                    >
                      {{ $t("statusAndType.tight") }}
                    </el-tag>
                    <el-tag
                      v-if="cloudNowData.usage === 1"
                      type="info"
                      size="small"
                      class="align-top cloudUsageStatus"
                      effect="dark"
                    >
                      {{ $t("statusAndType.full") }}
                    </el-tag>
                  </span>
                </span>

                <span
                  class="inline-block cursor-pointer truncate float-left pr-6"
                >
                  {{ cloudNowData.name }}
                </span>
              </div>
            </div>
            <div class="cloudChangeList p-2 overflow-hidden rounded">
              <ul class="inline-block float-left cloudNameStyle">
                <li
                  v-for="(item, index) in cloudListData"
                  :key="item.cloud_id"
                  class="overflow-hidden rounded px-2 py-1.5"
                >
                  <span class="inline-block float-left mr-2 w-12 h-6">
                    <span v-if="item.usage || item.usage === 0">
                      <el-tag
                        v-if="item.usage < 0.5"
                        type="success"
                        size="small"
                        effect="dark"
                        class="cloudUsageStatus"
                      >
                        {{ $t("statusAndType.idle") }}
                      </el-tag>
                      <el-tag
                        v-if="item.usage >= 0.5 && item.usage < 0.9"
                        type="warning"
                        size="small"
                        effect="dark"
                        class="cloudUsageStatus"
                      >
                        {{ $t("statusAndType.busy") }}
                      </el-tag>
                      <el-tag
                        v-if="item.usage > 0.9 && item.usage < 1"
                        type="danger"
                        size="small"
                        effect="dark"
                        class="cloudUsageStatus"
                      >
                        {{ $t("statusAndType.tight") }}
                      </el-tag>
                      <el-tag
                        v-if="item.usage === 1"
                        type="info"
                        size="small"
                        effect="dark"
                        class="cloudUsageStatus"
                      >
                        {{ $t("statusAndType.full") }}
                      </el-tag>
                    </span>
                  </span>
                  <span
                    class="inline-block text-base cursor-pointer truncate float-left mr-2 leading-6"
                    style="min-width: 120px"
                    @click="handleCloudChange(item)"
                  >
                    {{ item.name }}
                  </span>
                  <span v-if="item.labels && item.labels.length > 0">
                    <span
                      v-for="(item, index) in item.labels.slice(0, 3)"
                      :key="index"
                      class="relative inline-block mt-0.5 px-2 text-md leading-5 rounded h-5 mr-2 text-center align-bottom labelMinWidth"
                      :style="
                        'color:' +
                        JSON.parse(item).textColor +
                        ';background-color:' +
                        JSON.parse(item).bgColor
                      "
                      >{{ JSON.parse(item).label }}
                      <span
                        v-if="JSON.parse(item).imgUrl"
                        class="inline-block h5"
                        :style="
                          'width:' + (JSON.parse(item).imgSize - 8) + 'px'
                        "
                      />
                      <img
                        v-if="JSON.parse(item).imgUrl"
                        :src="JSON.parse(item).imgUrl"
                        alt=""
                        class="absolute"
                        :class="
                          getLabelImgPOsition(JSON.parse(item).imgPosition)
                        "
                        :style="
                          'max-width:none;width:' +
                          JSON.parse(item).imgSize +
                          'px'
                        "
                      />
                    </span>

                    <span v-if="item.labels.length > 3">
                      <el-popover
                        placement="top"
                        trigger="hover"
                        popper-class="cloudLabelPopoverBg"
                      >
                        <span slot="reference">
                          <span
                            class="relative inline-block px-2 text-md leading-5 rounded h-5 text-center cursor-pointer"
                            style="color: #fff; background-color: #004579"
                          >
                            +{{ item.labels.length - 3 }}
                          </span>
                        </span>
                        <div class="p-2 leading-5">
                          <span
                            v-for="(labelsItem, index) in item.labels.slice(
                              3,
                              item.labels.length
                            )"
                            :key="index"
                          >
                            {{ JSON.parse(labelsItem).label }}
                            <img
                              v-if="JSON.parse(labelsItem).imgUrl"
                              :src="JSON.parse(labelsItem).imgUrl"
                              alt=""
                              class="inline-block"
                              :style="
                                'max-width:none;width:' +
                                JSON.parse(labelsItem).imgSize +
                                'px'
                              "
                            />
                            <span v-if="index < item.labels.length - 4">,</span>
                          </span>
                        </div>
                      </el-popover>
                    </span>
                  </span>
                </li>
              </ul>
            </div>
          </el-popover>
          <span v-else class="text-sm px-4">
            {{ $t("message.noCloud") }}
          </span>
        </div>
      </div>
    </div>
    <div class="right-menu">
      <span class="help_file" @click="push_help_file">
        {{ $t("titleAndText.help") }}
      </span>
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <span class="svg">
            <svg
              t="1651716262906"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="1945"
              width="20"
              height="20"
            >
              <path
                d="M492.862682 477.511893c-111.446732 0-202.118729-90.671997-202.118729-202.118729S381.41595 73.274435 492.862682 73.274435c111.446732 0 202.118729 90.671997 202.118729 202.118729S604.309414 477.511893 492.862682 477.511893zM492.862682 118.303418c-86.578453 0-157.089746 70.511293-157.089746 157.089746S406.284229 432.48291 492.862682 432.48291c86.578453 0 157.089746-70.511293 157.089746-157.089746S579.441135 118.303418 492.862682 118.303418z"
                p-id="1946"
                fill="#CCD0D4"
              />
              <path
                d="M173.770937 796.296622c-12.485309 0-22.514491-10.029182-22.514491-22.514491 0-46.154707 9.005797-90.876674 26.812712-132.937837 17.192884-40.730762 41.856486-77.163302 73.172097-108.581251 31.315611-31.315611 67.85049-55.979212 108.581251-73.172097 42.163502-17.806916 86.885469-26.812712 132.937837-26.812712 54.853488 0 107.148511 12.587647 155.657006 37.455926 11.052568 5.628623 15.453128 19.239656 9.722167 30.292225s-19.239656 15.453128-30.292225 9.722167c-42.061163-21.593444-87.4995-32.441335-135.086948-32.441335-163.537078 0-296.577254 133.040176-296.577254 296.577254C196.285429 786.165101 186.153908 796.296622 173.770937 796.296622z"
                p-id="1947"
                fill="#CCD0D4"
              />
              <path
                d="M741.75015 566.546472l-171.928843 0c-12.38297 0-22.514491-10.131521-22.514491-22.514491l0 0c0-12.38297 10.131521-22.514491 22.514491-22.514491l171.928843 0c12.38297 0 22.514491 10.131521 22.514491 22.514491l0 0C764.264641 556.414951 754.13312 566.546472 741.75015 566.546472z"
                p-id="1948"
                fill="#CCD0D4"
              />
              <path
                d="M785.448731 683.724166l-216.957825 0c-12.38297 0-22.514491-10.131521-22.514491-22.514491l0 0c0-12.38297 10.131521-22.514491 22.514491-22.514491l216.957825 0c12.38297 0 22.514491 10.131521 22.514491 22.514491l0 0C807.963222 673.592644 797.831701 683.724166 785.448731 683.724166z"
                p-id="1949"
                fill="#CCD0D4"
              />
              <path
                d="M852.787527 801.925245l-282.454527 0c-12.38297 0-22.514491-10.131521-22.514491-22.514491l0 0c0-12.38297 10.131521-22.514491 22.514491-22.514491l282.454527 0c12.38297 0 22.514491 10.131521 22.514491 22.514491l0 0C875.302019 791.793724 865.170498 801.925245 852.787527 801.925245z"
                p-id="1950"
                fill="#CCD0D4"
              />
            </svg>
          </span>
          <span class="infoName">{{ userInfo.name }} </span>
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <div class="pl-5 pr-5">
            <div class="userinfo_top">
              <div>
                <svg
                  t="1685452113555"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="9434"
                  width="26"
                  height="26"
                >
                  <path
                    d="M512 1024a512 512 0 1 1 512-512 512 512 0 0 1-512 512zM512 64a448 448 0 1 0 448 448 448 448 0 0 0-448-448z m0 768a233.472 233.472 0 0 1-256-256V256h128v320a112 112 0 0 0 128 128 113.984 113.984 0 0 0 128-128V256h128v320c0 173.248-94.976 256-256 256z"
                    fill="#7dc5eb"
                    p-id="9435"
                  />
                </svg>
              </div>
              <div class="userInfo_name ml-1">
                {{ userInfo.name }}
              </div>
              <!-- <div class="isrealname">
                未实名
              </div> -->
              <div style="width: 50px" />
            </div>
          </div>

          <router-link to="/userMsg">
            <el-dropdown-item icon="el-icon-s-custom" divided>
              {{ $t("titleAndText.personalCenter") }}
            </el-dropdown-item>
          </router-link>

          <el-dropdown-item
            icon="el-icon-switch-button"
            divided
            style="text-align: center"
            @click.native="logout"
          >
            <span style="display: inline-block">
              {{ $t("titleAndText.logout") }}
            </span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!---抽屉开始-->
    <el-drawer
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose"
      :with-header="false"
      size="1060px"
    >
      <div style="">
        <el-menu
          :background-color="'#fff'"
          :text-color="'#000'"
          :default-active="active_menu"
          class="float-left"
          style="width: 200px; height: calc(100vh - 50px)"
        >
          <el-menu-item index="1" class="basemenu" @click="rightrouter(1)">
            <i class="icon-tubiaozhizuomoban not-italic mr-3" />
            <span slot="title"
              >{{ $t("titleAndText.allProducts") }}
              <span style="position: absolute; right: 0px">
                <i class="el-icon-arrow-right" /></span
            ></span>
          </el-menu-item>
          <div v-for="router_item in routerData" :key="router_item.path">
            <el-menu-item
              v-if="isShowMenu(router_item.meta.group)"
              :index="router_item.meta.group"
              class="basemenu"
              @click="rightrouter(router_item)"
            >
              <i :class="router_item.meta.icon" class="not-italic mr-3" />
              <span slot="title"
                >{{ $t("router." + router_item.meta.languageTitle) }}
                <span style="position: absolute; right: 0px">
                  <i class="el-icon-arrow-right" /></span
              ></span>
            </el-menu-item>
          </div>
        </el-menu>
        <!-- 所有菜单 -->
        <div
          v-if="active_menu == 1"
          style="height: calc(100vh - 50px)"
          class="overflow-hidden"
        >
          <div
            style="font-size: 22px; position: absolute; right: 10px; top: 10px"
            @click="handleClose"
          >
            <i class="el-icon-close" />
          </div>
          <div
            style="width: 860px; padding-left: 20px; padding-bottom: 100px"
            class="h-full scrollStyle overflow-x-hidden overflow-y-auto"
          >
            <!-- 处理完毕 -->
            <div v-for="route in routerData" :key="route.path">
              <div v-if="isShowMenu(route.meta.group)" class="mt-3">
                <div class="new_overview">
                  <span
                    style="color: #3f5cda; font-size: 13px; margin-left: 20px"
                  >
                    <i class="el-icon-menu"
                  /></span>
                  <span class="firstTitle">{{
                    $t("router." + route.meta.languageTitle)
                  }}</span>
                </div>
                <div
                  class="nav_router"
                  style="display: flex; flex-wrap: wrap; width: 740px"
                >
                  <div v-for="item in route.children" :key="item.path + 1">
                    <div
                      v-if="!item.hidden && item.children.length != 0"
                      class="nochildren"
                    >
                      <div>
                        <div class="secondTitle">
                          <span class="blockall" />
                          {{ $t("router." + item.meta.languageTitle) }}
                        </div>
                        <div v-for="items in item.children" :key="items.path">
                          <router-link :to="items.path">
                            <div
                              v-if="!items.hidden"
                              class="thirdTitle"
                              @click="handleRouterChange()"
                            >
                              <span class="allDot" />
                              {{ $t("router." + items.meta.languageTitle) }}
                            </div>
                          </router-link>
                        </div>
                      </div>
                    </div>
                    <div
                      v-if="!item.hidden && item.children.length == 0"
                      class="nochildren"
                    >
                      <div>
                        <router-link :to="item.path">
                          <div class="thirdTitle" @click="handleRouterChange()">
                            <span class="allDot" />
                            {{ $t("router." + item.meta.languageTitle) }}
                          </div>
                        </router-link>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- 单独模块菜单 -->
        <div
          v-if="active_menu != 1"
          style="height: calc(100vh - 50px)"
          class="overflow-hidden"
        >
          <div
            style="font-size: 22px; position: absolute; right: 10px; top: 10px"
            @click="handleClose"
          >
            <i class="el-icon-close" />
          </div>
          <div style="width: 860px; padding-left: 20px; padding-bottom: 100px">
            <div class="new_overview mt-3">
              <span style="color: #3f5cda; font-size: 13px; margin-left: 20px">
                <i class="el-icon-menu"
              /></span>
              <span class="firstTitle">{{
                $t("router." + rightrouterdata.meta.languageTitle)
              }}</span>
            </div>
            <div class="cloudcomputing">
              <div
                v-for="item in rightrouterdata.children"
                :key="item.path + 1"
              >
                <div
                  v-if="!item.hidden && item.children.length != 0"
                  class="nochildren"
                >
                  <div>
                    <div class="secondTitle">
                      <span class="blockall" />
                      {{ $t("router." + item.meta.languageTitle) }}
                    </div>
                    <div v-for="items in item.children" :key="items.path">
                      <router-link :to="items.path">
                        <div
                          v-if="!items.hidden"
                          class="thirdTitle_other"
                          @click="handleRouterChange()"
                        >
                          <span class="allDot" />
                          {{ $t("router." + items.meta.languageTitle) }}
                        </div>
                      </router-link>
                    </div>
                  </div>
                </div>
                <div
                  v-if="!item.hidden && item.children.length == 0"
                  class="nochildren"
                >
                  <div>
                    <router-link :to="item.path">
                      <div
                        class="thirdTitle_other"
                        @click="handleRouterChange()"
                      >
                        <span class="allDot" />
                        {{ $t("router." + item.meta.languageTitle) }}
                      </div>
                    </router-link>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </el-drawer>
    <!---抽屉结束-->
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Cookies from "js-cookie";
import { resetRouter } from "@/router";
import { getCloudysInfo } from "@/api/mainApi";
import Head from "@/components/head.vue";
export default {
  components: {
    Head,
  },
  data() {
    return {
      active_menu: 1,
      drawer: false,
      direction: "ltr",
      inputValue: "",
      routerData: [],
      firstRouter: [],
      cloudListData: [],
      cloudNowData: "",
      queryData: {
        page_size: 99999,
        page_num: 1,
      },
      rightrouterdata: [],
    };
  },
  computed: {
    ...mapGetters([
      "sidebar",
      "avatar",
      "kind",
      "userInfo",
      "routers",
      "hxcloudData",
    ]),
  },

  created() {
    this.$watch("$route", (to, from) => {
      if (to.meta.group == "index") {
        this.rightrouter("1");
      } else {
        this.routerData.map((res) => {
          if (res.meta.group == to.meta.group) {
            this.rightrouter(res);
          }
        });
      }
      var dataStatus = this.isShowCloudsChange();
      if (dataStatus) {
        this.getcloudListData();
      }
    });
  },
  mounted() {
    this.getcloudListData();
    this.routes();
    if (this.$route.meta.group == "index") {
      this.rightrouter("1");
    } else {
      this.routerData.map((res) => {
        if (res.meta.group == this.$route.meta.group) {
          this.rightrouter(res);
        }
      });
    }
  },
  methods: {
    // 跳转到帮助中心
    push_help_file() {
      this.$router.push("/help");
    },
    getLabelImgPOsition(data) {
      // 获取标签图片位置
      if (data == "top-left") {
        return "top-0 left-0";
      } else if (data == "top-right") {
        return "top-0 right-0";
      } else if (data == "bottom-left") {
        return "bottom-0 left-0";
      } else if (data == "bottom-right") {
        return "bottom-0 right-0";
      } else if (data == "left") {
        return "top-1/2 left-0 transform -translate-y-1/2";
      } else if (data == "right") {
        return "top-1/2 right-0 transform -translate-y-1/2";
      } else if (data == "center") {
        return "top-1/2 right-0 left-0 mx-auto transform -translate-y-1/2";
      } else {
        return "top-0 right-0";
      }
    },

    isShowCloudsChange() {
      // 是否展示多云切换
      var status = false;
      const computeMain =
        this.$route.meta.group &&
        (this.$route.meta.group == "nextStack" ||
          this.$route.meta.group == "compute"); // 云计算模块
      const statisticsMain =
        this.$route.path == "/statistics" &&
        this.$route.query &&
        this.$route.query.type == "clouds"; // 统计模块
      const nextStackVmAdd = this.$route.name == "nextStack-vm-add"; // nextStack虚拟机添加
      if (computeMain || statisticsMain) {
        status = true;
      } else {
        status = false;
      }
      if (nextStackVmAdd) {
        status = false;
      }

      return status;
    },
    isShowMenu(item) {
      // 是否展示云计算
      // if (item == "nextStack") {
      //   return (
      //     this.hxcloudData &&
      //     this.hxcloudData.product &&
      //     this.hxcloudData.product.toLowerCase() == "nextstack"
      //   );
      // } else
      if (item == "compute") {
        return (
          this.hxcloudData &&
          this.hxcloudData.product &&
          (this.hxcloudData.product.toLowerCase() == "easystack" ||
            this.hxcloudData.product.toLowerCase() == "openstack")
        );
      } else {
        return true;
      }
    },
    handleCloudChange(value) {
      var data = value;
      // 当前页面是云计算模块
      if (
        (this.$route.meta.group == "nextStack" ||
          this.$route.meta.group == "compute") &&
        this.$store.state.app.hxcloudData.product != data.product
      ) {
        // 如果切换了不同类型的云
        if (data.product.toLowerCase() == "easystack") {
          this.$router.push("/instance");
        }
        if (data.product.toLowerCase() == "openstack") {
          this.$router.push("/instance");
        }
        if (data.product.toLowerCase() == "nextstack") {
          this.$router.push("/nextStack/vm");
        }
      }

      setTimeout(() => {
        this.$store.commit("app/sethxcloudData", data);
      }, 100);
    },

    // 右边路由的显示
    rightrouter(routerdata) {
      // 判断是不是所有产品
      if (routerdata == 1) {
        this.active_menu = routerdata;
      } else {
        // 对不是所有产品进行处理
        this.active_menu = routerdata.meta.group;
        this.rightrouterdata = routerdata;
      }
    },
    determineproduct(value) {
      if (value.toLowerCase() == "easystack") {
        return "compute";
      }
      if (value.toLowerCase() == "openstack") {
        return "compute";
      }
      if (value.toLowerCase() == "nextstack") {
        return "nextStack";
      }
    },
    async getcloudListData() {
      const list = await getCloudysInfo(this.queryData);
      this.cloudListData = list.clouds;
      if (!this.cloudListData || this.cloudListData.length == 0) {
        this.$store.commit("app/sethxcloudData", ""); // 设置为默认云
        this.cloudNowData = "";
        return;
      }

      var hxcloudData =
        localStorage.getItem("hxcloudData") &&
        localStorage.getItem("hxcloudData") != "undefined"
          ? JSON.parse(localStorage.getItem("hxcloudData"))
          : "";
      if (hxcloudData) {
        var data = this.cloudListData.filter((item) => {
          return item.cloud_id == hxcloudData.cloud_id;
        });

        if (data.length == 0) {
          data = this.cloudListData;
        }
        this.cloudNowData = data[0];
      } else {
        // 没有缓存
        var nowCloud = this.cloudListData;

        if (nowCloud && nowCloud.length > 0) {
          // 健康的云大于0 个
          this.$store.commit("app/sethxcloudData", nowCloud[0]); // 设置为默认云
          this.cloudNowData = nowCloud[0];
        } else {
          // 暂无可用云
        }
      }
    },
    handelDrawerOpen() {
      this.drawer = true;
    },
    handleClose() {
      this.drawer = false;
    },
    handleRouterChange() {
      this.drawer = false;
    },

    routes() {
      this.routerData = this.routers.filter((item, index) => {
        return item.children != undefined && !item.hidden;
      });
    },

    async logout() {
      for (const key in localStorage) {
        if (key !== "hxcloudData") {
          localStorage.removeItem(key);
        }
      }
      Cookies.remove("Access-Token");
      Cookies.remove("vue_admin_template_kind");
      this.$store.commit("user/SET_KING", "");
      resetRouter();
      this.$router.push(`/login?redirect=${this.$route.fullPath}`);
    },
  },
};
</script>
<style>
.cloudLabelPopoverBg {
  background-color: #111f29;
  color: #fff;
  border: none;
  border-radius: 0 !important;
}
</style>
<style lang="scss" scoped>
.info-email {
  font-size: 12px;
  line-height: 14px;
  width: 130px;
  max-width: 130px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  text-align: start;
  margin-left: 12px;
}

.el-icon-close:hover {
  background-color: #f3f4f6;
  color: #4b5563d4;
  cursor: pointer;
}

.firstTitle {
  font-size: 15px;
  color: rgba(0, 0, 0, 0.95);
  padding: 10px 20px;
  line-height: 30px;
}

.secondTitle {
  font-size: 14px;
  background-color: transparent;
  color: rgba(0, 0, 0, 0.85);
  padding: 5px 25px 5px 25px;
}

.thirdTitle {
  font-size: 13px;
  background-color: transparent;
  color: rgba(0, 0, 0, 0.75);
  padding: 4px 25px 4px 25px;
}

.thirdTitle_other {
  font-size: 13px;
  background-color: transparent;
  color: rgba(0, 0, 0, 0.75);
  padding: 8px 25px 8px 25px;
}

.disabledthird {
  font-size: 13px;
  background-color: transparent;
  color: #bebebe;
  padding: 4px 25px 4px 25px;
}

.disabledthirdTitle {
  font-size: 13px;
  background-color: transparent;
  color: #bebebe;
  padding: 7px 25px 7px 0px;
}

.thirdTitle:hover {
  background-color: #f0f5ff;
  color: #4461eb;
  width: 100%;
  border-radius: 2px;
}

.thirdTitle_other:hover {
  background-color: #f0f5ff;
  color: #4461eb;
  width: 100%;
  border-radius: 2px;
}

.allDot {
  width: 33px;
  display: inline-block;
}

.allDot::after {
  content: "";
  width: 3px;
  height: 3px;
  background: rgba(0, 0, 0, 0.25);
  display: inline-block;
  border-radius: 50%;
}

.blockall {
  width: 30px;
  display: inline-block;
}

.disabledblockall {
  width: 30px;
  display: inline-block;
}

.disabledblockall::after {
  content: "";
  border-radius: 0;
  background: #bebebe;
  width: 5px;
  height: 5px;
  display: inline-block;
}

.blockall::after {
  content: "";
  border-radius: 0;
  background: #3f5cda;
  width: 5px;
  height: 5px;
  display: inline-block;
}

::v-deep .el-menu-item {
  height: 40px;
  line-height: 40px;
  background-color: #f0f5ff;
  color: #4461eb;
}

::v-deep .el-col-8 {
  width: 33.33333%;
  min-height: 270px;
}

.v-modal {
  position: fixed;
  left: 0;
  top: 50px;
  width: 100%;
  height: 100%;
  opacity: 0.5;
  background: #000;
}

::v-deep .el-drawer__wrapper {
  position: fixed;
  top: 50px;
  right: 0;
  bottom: 0;
  left: 0;
  overflow: hidden;
  margin: 0;
}

::v-deep .el-button {
  border-radius: 0px;
}

.el-dropdown-menu {
  width: 200px !important;
  position: absolute !important;
  right: 0px !important;
  top: 38px !important;
  padding: 0px !important;
}

.hover:hover {
  background-color: hsla(0, 0%, 100%, 0.5);
}

.sumtitle {
  color: hsla(0, 0%, 100%, 0.85);
  background-color: hsla(0, 0%, 100%, 0.09);
  width: 60px;
  text-align: center;
  display: block;
  font-size: 13px;
}

.cloudChangeStyle {
  color: #fff;
  background-color: #172a38;
}

.cloudChangeList {
  color: #fff;
  background-color: #172a38;
}

.cloudNameStyle {
  li {
    &:hover {
      background-color: rgba(0, 0, 0, 0.3);
    }
  }
}

.cloudUsageStatus {
  transform: scale(0.9);
  -webkit-transform: scale(0.9);
  -moz-transform: scale(0.9);
  -o-transform: scale(0.9);
}

.labelMinWidth {
  // min-width: 80px;
}

.logoImg {
  height: 36px;
  width: 121px;
  max-width: 121px;
  margin: 0px 24px 0px 15px;
  display: inline-block;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #001529;
  // background: rgb(48, 65, 86);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 44px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .left-menu {
    float: left;
    height: 100%;
    min-width: 900px;
    // display: flex;
    justify-content: space-between;
    align-items: center;

    &:focus {
      outline: none;
    }
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 51px;

    .help_file {
      color: #ccd0d4;
      height: 19px;
      margin-right: 30px;
      font-size: 14px;
      cursor: pointer;
    }

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        position: relative;

        .svg {
          float: right;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        .infoName {
          color: #ccd0d4;
          display: flex;
          justify-content: center;
          align-items: center;
          height: 19px;
          margin-right: 30px;
        }

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 2px;
          font-size: 12px;
          color: #ccd0d4;
        }
      }
    }
  }
}

#click-scroll-X {
  width: 800px;
  display: flex;
  align-items: center;

  .left_btn,
  .right_btn {
    font-size: 30px;
    cursor: pointer;
    margin: -10px -5px 0;
  }

  .scroll_wrapper {
    width: 100%;
    overflow-x: scroll;

    .scroll_list_nums {
      display: flex;
      align-items: center;

      .item {
        cursor: pointer;
        width: 250px;
        height: 80px;
        background: rgb(167, 198, 251);
        color: white;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        flex-shrink: 0;
        margin: 0 2px;
      }

      .avtive-item {
        cursor: pointer;
        width: 250px;
        height: 80px;
        background: rgb(89, 148, 248);
        color: white;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        flex-shrink: 0;
        margin: 0 2px;
      }
    }

    .scroll_list_num {
      display: flex;
      align-items: center;
      justify-content: space-evenly;

      .item {
        cursor: pointer;
        width: 250px;
        height: 100px;
        background: rgb(167, 198, 251);
        color: white;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        flex-shrink: 0;
        margin: 0 2px;
      }

      .avtive-item {
        cursor: pointer;
        width: 250px;
        height: 100px;
        background: rgb(89, 148, 248);
        color: white;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        flex-shrink: 0;
        margin: 0 2px;
      }
    }

    .scroll_list {
      display: flex;
      align-items: center;
      justify-content: space-between;

      .item {
        cursor: pointer;
        width: 250px;
        height: 80px;
        background: rgb(167, 198, 251);
        color: white;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        flex-shrink: 0;
        margin: 0 2px;
      }

      .avtive-item {
        cursor: pointer;
        width: 250px;
        height: 80px;
        background: rgb(89, 148, 248);
        color: white;
        border-radius: 5px;
        display: flex;
        justify-content: center;
        flex-shrink: 0;
        margin: 0 2px;
      }
    }
  }
}

/*隐藏滚动条*/
.scroll_wrapper::-webkit-scrollbar {
  display: none;
}

.new_overview {
  height: 50px;
  line-height: 50px;
  width: 800px;
  background: rgb(247, 250, 255);
}

.cloudcomputing {
  margin-left: 50px;
  margin-top: 10px;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
}

.nav-card {
  text-align: center;

  .nav-card-title {
    margin-top: 10px;
  }

  .nav-card-content {
    margin-top: 10px;
  }
}

.nochildren {
  width: 250px;
}

.cloudy-icon {
  margin-right: 3px;
}

.nav_router {
  margin-left: 50px;
  margin-top: 5px;
}

.basemenu {
  padding-left: 15px !important;
  background-color: #f0f5ff;
  color: black;
  height: 50px;
  line-height: 50px;
}

.userinfo_top {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .isrealname {
    font-size: 12px;
    color: white;
    width: 50px;
    height: 20px;
    line-height: 20px;
    background: rgb(166, 173, 178);
    border-radius: 5px;
    text-align: center;
  }
}

.info-balance {
  padding: 10px;
  margin-top: 10px;
  margin-bottom: 15px;
  color: white;
  width: 100%;
  height: 65px;
  background: rgb(89, 148, 248);
  border-radius: 5px;

  .info-balance-title {
    font-size: 12px;
    display: flex;
    height: 25px;
    justify-content: space-between;

    .recharge {
      cursor: pointer;
    }
  }

  .info_money {
    font-size: 18px;
    font-weight: 600;
    margin-left: -3px;
  }
}

.userInfo_name {
  color: rgb(117, 98, 102);
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
