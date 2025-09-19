<template>
  <div :class="is_console ? 'navbar_console' : 'navbar'">
    <div class="left-menu">
      <el-button
        v-if="is_console"
        style="
          background-color: rgba(255, 255, 255, 0.2);
          border-color: transparent;
          color: rgb(204, 208, 212);
          font-weight: normal;
          width: 60px;
          height: 60px;
          text-align: center;
          border-radius: 0px;
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
        class="inline-block line_sixty"
        style="height: 60px; vertical-align: top; cursor: pointer"
        @click="push_index"
      >
        <img
          v-if="is_console"
          src="@/assets/images/public/logo.png"
          alt="logo"
          class="logoImg"
        />
        <img
          v-if="!is_console"
          src="@/assets/images/public/logoBlack.png"
          alt="logo"
          class="logoImg"
        />
      </div>
      <div
        v-if="is_console && (kind == 0 || kind == 1)"
        class="inline-block leading-8 mt-4 mr-4"
      >
        <router-link to="/index">
          <span class="sumtitle rounded">{{
            $t("titleAndText.overview")
          }}</span>
        </router-link>
      </div>
      <div v-if="isShowCloudsChange()" class="inline-block rounded mt-4 mr-4">
        <div class="cloudChangeStyle h-8 pt-1 rounded">
          <el-popover
            v-if="cloudListData && cloudListData.length > 0"
            placement="bottom-start"
            trigger="click"
          >
            <div slot="reference">
              <div class="px-4 cloudChangeList">
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
                  style="text-overflow: ellipsis; white-space: nowrap"
                  class="inline-block cursor-pointer pr-6"
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
          <span v-else class="text-sm px-4">{{ $t("message.noCloud") }}</span>
        </div>
      </div>
    </div>
    <div class="right-menu">
      <div
        class="inline-block"
        :class="
          is_console ? 'head_title_language_console' : 'head_title_language'
        "
      >
        <el-dropdown class="pl-4 pr-4" @command="changeLanguage">
          <span class="el-dropdown-link">
            <span>{{ $t("domain.language") }}</span>
            <i class="el-icon-arrow-down el-icon--right" />
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="en">English</el-dropdown-item>
            <el-dropdown-item command="zh-CN">中文</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div
        v-if="is_console"
        class="inline-block pr-5 pl-5 head_title_console"
        @click="push_help_file"
      >
        {{ $t("titleAndText.help") }}
      </div>
      <div v-if="!is_console" class="inline-block mr-3 consoleStyleBtn">
        <el-button size="small" type="primary" round @click="push_home">{{
          $t("titleAndText.console")
        }}</el-button>
      </div>
      <div
        v-if="!hasToken"
        style="font-size: 14px"
        class="inline-block pr-5 pl-5 head_title"
      >
        <p @click="push_login">{{ $t("titleAndText.loginOrRegister") }}</p>
      </div>
      <el-dropdown
        v-if="hasToken"
        class="avatar-container pr-5 pl-5"
        trigger="click"
      >
        <div class="avatar-wrapper">
          <span class="svg mr-2">
            <svg
              t="1729846526006"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="8684"
              width="20"
              height="20"
            >
              <path
                d="M512.010745 1022.082324c-282.335297 0-511.220241-228.798986-511.220241-511.036046C0.790504 228.798986 229.675448 0 512.010745 0c282.312784 0 511.198751 228.798986 511.198751 511.046279C1023.208473 793.285385 794.322505 1022.082324 512.010745 1022.082324zM512.010745 95.826486c-229.385341 0-415.371242 185.884594-415.371242 415.220816 0 107.22714 41.021276 204.6551 107.802238 278.339286 60.140729-29.092595 38.062897-4.88424 116.77254-37.274952 80.539314-33.089629 99.610672-44.639686 99.610672-44.639686l0.776689-76.29464c0 0-30.169113-22.890336-39.543621-94.683453-18.895349 5.426593-25.108864-21.988804-26.237571-39.429011-1.001817-16.863063-10.926864-69.487607 12.105712-64.739467-4.714372-35.144428-8.094352-66.844407-6.417153-83.633792 5.763261-58.938344 62.97324-120.518864 151.105486-125.017318 103.665011 4.486174 144.737452 66.028832 150.500713 124.9682 1.680269 16.800641-2.028193 48.511877-6.739495 83.594907 23.025413-4.686742 13.028735 47.861054 11.901051 64.726164-1.028423 17.440208-7.394411 44.756343-26.208918 39.34203-9.42158 71.79107-39.593763 94.498234-39.593763 94.498234l0.725524 75.924203c0 0 19.070334 10.788717 99.609649 43.892673 78.70862 32.387641 56.605206 9.609869 116.77561 38.765909 66.75231-73.686233 107.772562-171.101913 107.772562-278.339286C927.356404 281.712103 741.398132 95.826486 512.010745 95.826486z"
                p-id="8685"
                fill="#b3b9bf"
              />
            </svg>
          </span>
          <span :class="is_console ? 'infoName_console' : 'infoName'"
            >{{ userInfo.name }}
          </span>
          <i
            class="el-icon-caret-bottom"
            :style="is_console ? 'color:#ccd0d4' : 'color:rgb(59, 64, 72)'"
          />
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
            <span style="display: inline-block">{{
              $t("titleAndText.logout")
            }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <!---抽屉开始-->
    <el-drawer
      :visible.sync="drawer"
      :direction="direction"
      :modal="false"
      :before-close="handleClose"
      :with-header="false"
      size="1060px"
    >
      <div>
        <el-menu
          :background-color="'#fff'"
          :text-color="'#000'"
          :default-active="active_menu"
          class="float-left"
          style="width: 200px; height: calc(100vh - 60px)"
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
          style="height: calc(100vh - 60px)"
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
          style="height: calc(100vh - 60px)"
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
import { getToken } from "@/utils/auth";
import Cookies from "js-cookie";
import { resetRouter } from "@/router";
import { getCloudysInfo } from "@/api/mainApi";
export default {
  props: {
    is_console: {
      type: Boolean,
      default: false,
    },
    activepath: String,
  },
  data() {
    return {
      routerData: [],
      direction: "ltr",
      // 登录
      hasToken: false,

      activeIndex: "",
      drawer: false,
      cloudNowData: "",
      rightrouterdata: [],
      cloudListData: [],
      active_menu: 1,
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
  mounted() {
    if (this.$props.is_console) {
      this.getcloudListData();
      this.routes();
    }
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
  created() {
    this.init();
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
  methods: {
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
    routes() {
      this.routerData = this.routers.filter((item, index) => {
        return item.children != undefined && !item.hidden;
      });
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
    push_index() {
      this.$router.push("/");
    },
    push_login() {
      this.$router.push("/login?redirect=" + this.$route.fullPath);
    },
    push_home() {
      const hasToken = getToken();
      if (hasToken) {
        this.$router.push("/index");
      } else {
        this.$router.push("/login");
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
    pushrouter(path_data) {
      this.$router.push(path_data);
    },
    handleSelect(data) {},

    handleCloudChange(value) {
      var data = value;
      // 当前页面是云计算模块
      console.log(this.$route.meta.group);
      console.log(data);
      console.log(this.$store.state.app.hxcloudData.product);
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
    changeLanguage(lang) {
      this.$i18n.locale = lang;
      localStorage.setItem("locale", lang);

      this.$router.go(0);
    },
  },
};
</script>

<style lang="scss" scoped>
.user-dropdown {
  width: 200px !important;
  margin: 0px !important;
}

.logoImg {
  height: 36px;
  width: 121px;
  max-width: 121px;
  margin: 0px 25px 0px 15px;
  display: inline-block;
}

.line_sixty {
  line-height: 60px;
}

.navbar,
.navbar_console {
  height: 60px;
  width: 100%;
  min-width: 1200px;
  overflow: hidden;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1003;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .left-menu {
    float: left;
    height: 100%;

    justify-content: space-between;
    align-items: center;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 60px;

    .infoName_console {
      color: #ccd0d4;
    }

    .console {
      color: rgb(59, 64, 72);
    }
  }
}

.navbar {
  backdrop-filter: blur(4px);
  background: rgba(253, 253, 254, 0.8);
}

.navbar_console {
  background: #001529;
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
  color: rgb(59, 64, 72);
  font-size: 16px;
  font-weight: 600;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.head_title {
  color: rgb(59, 64, 72);
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
}

.head_title:hover {
  background: rgb(245, 245, 245);
}

.active_head_title {
  border-bottom: 3px solid rgb(64, 158, 255);
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
}
.head_title_language {
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
  .el-dropdown {
    color: rgb(96, 98, 102);
  }
}

.head_title_language_console {
  color: #ccd0d4;
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
  .el-dropdown {
    color: #ccd0d4;
  }
}
.head_title_language_console:hover {
  background: rgb(23, 42, 60);
}
.head_title_console {
  color: #ccd0d4;
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
}

.head_title_console:hover {
  background: rgb(23, 42, 60);
}

.active_head_title_console {
  color: #ccd0d4;
  border-bottom: 3px solid rgb(64, 158, 255);
  cursor: pointer;
  height: 60px;
  line-height: 60px;
  font-size: 14px;
}

.sumtitle {
  color: hsla(0, 0%, 100%, 0.85);
  background-color: hsla(0, 0%, 100%, 0.09);
  width: 60px;
  text-align: center;
  display: block;
  font-size: 13px;
}

::v-deep .el-menu-item {
  height: 40px;
  line-height: 40px;
  background-color: #f0f5ff;
  color: #4461eb;
}

.basemenu {
  padding-left: 15px !important;
  background-color: #f0f5ff;
  color: black;
  height: 50px;
  line-height: 50px;
}

.new_overview {
  height: 50px;
  line-height: 50px;
  width: 800px;
  background: rgb(247, 250, 255);
}

.firstTitle {
  font-size: 15px;
  color: rgba(0, 0, 0, 0.95);
  padding: 10px 20px;
  line-height: 30px;
}

.nav_router {
  margin-left: 50px;
  margin-top: 5px;
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

.blockall {
  width: 30px;
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

.nochildren {
  width: 250px;
}

.cloudcomputing {
  margin-left: 50px;
  margin-top: 10px;
  width: 100%;
  display: flex;
  flex-wrap: wrap;
}

.el-icon-close:hover {
  background-color: #f3f4f6;
  color: #4b5563d4;
  cursor: pointer;
}

::v-deep .el-drawer__wrapper {
  position: fixed;
  top: 60px;
  right: 0;
  bottom: 0;
  left: 0;
  overflow-y: hidden;
  margin: 0;
}

.cloudLabelPopoverBg {
  background-color: #111f29;
  color: #fff;
  border: none;
  border-radius: 0 !important;
}

.cloudChangeStyle {
  color: #fff;
  background-color: #172a38;
}

.cloudChangeList {
  color: #fff;
  background-color: #172a38;
}

.cloudUsageStatus {
  transform: scale(0.9);
  -webkit-transform: scale(0.9);
  -moz-transform: scale(0.9);
  -o-transform: scale(0.9);
}

.cloudNameStyle {
  li {
    &:hover {
      background-color: rgba(0, 0, 0, 0.3);
    }
  }
}

@media (max-width: 768px) {
  .navbar {
    min-width: auto;

    .left-menu {
      .line_sixty {
        width: 46px;
        overflow: hidden;
        margin-right: 10px;

        .logoImg {
          margin-left: 10px;
        }
      }

      .head_title {
        padding: 0 5px !important;
      }

      .active_head_title {
        padding: 0 5px !important;
      }
    }

    .right-menu {
      .consoleStyleBtn {
        margin-right: 5px !important;

        .el-button {
          padding: 5px 10px !important;
        }
      }

      .head_title {
        padding: 0 10px 0 5px !important;

        p {
          font-size: 14px;
        }
      }

      .avatar-container {
        padding: 0 10px !important;
      }
    }
  }
}
</style>
