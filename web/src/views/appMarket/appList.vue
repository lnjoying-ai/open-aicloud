<template>
  <div class="warpMain">
    <div class="head_rq">
      <div
        class="h-20 w-full rounded-2xl mb-5"
        style="
          background-color: #163f9d;
          display: flex;
          justify-content: center;
          align-items: center;
        "
      >
        <div style="color: #ffffff; font-size: 22px">
          <span class="mr-4"><i class="el-icon-menu" /></span
          ><span>App Store</span>
        </div>
      </div>
      <div style="position: absolute; right: 10%; top: 27px">
        <img
          src="@/assets/images/appMarket/application.png"
          class="w-24 h-24"
          style=""
        />
      </div>
      <div class="mb-3 mt-5">
        <span
          style="
            text-align: right;
            vertical-align: middle;
            font-size: 14px;
            color: #606266;
            line-height: 40px;
            padding: 0 12px 0 0;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
            font-weight: 700;
          "
          >{{ $t("form.applicationType") }}:</span
        >
        <span v-for="(item, index) in sortlist" :key="index">
          <span class="mr-2">
            <el-button
              :icon="item.category.icon"
              style="margin-bottom: 0.5rem"
              size="mini"
              @click="handleTypeClick(item.category.name)"
              >{{ item.category.show_name
              }}<span class="ml-1" style="line-height: 1">{{
                item.app_total
              }}</span>
            </el-button>
          </span>
        </span>
      </div>
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.applicationRepository") }}:</div>
          <el-select
            v-model="repo_name"
            multiple
            collapse-tags
            :placeholder="$t('form.pleaseSelect')"
            filterable
            popper-class="select"
            @change="handleChange"
          >
            <el-option
              v-for="(item, index) in repolist"
              :key="index"
              :label="item.repo_name"
              :value="item.repo_name"
            >
              <span style="float: left">{{ item.repo_name }}</span>
              <span
                style="
                  float: right;
                  margin-top: 10px;
                  width: 10px;
                  height: 10px;
                  border-top-width: 1px;
                  border-style: solid;
                  border-radius: 18px;
                  background-color: #8c8c8c;
                "
                :style="'background-color:' + item[item.repo_name]"
              />
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <div slot="label">{{ $t("form.applicationName") }}:</div>
          <el-input
            v-model="queryData.app_name"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="handleCurrentChange(1)"
            >{{ $t("form.search") }}</el-button
          >
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="itemMian">
      <div
        v-for="(item, index) in tableData"
        :key="index"
        @click="goDetail(item)"
      >
        <el-col
          :key="index"
          :xs="24"
          :sm="12"
          :md="12"
          :lg="8"
          :xl="6"
          style="padding: 5px"
        >
          <el-card class="box-card cursor-pointer">
            <div>
              <div>
                <div>
                  <div
                    v-if="item.remove == true"
                    class="-mt-3 float-right -mr-4"
                    @click.prevent.stop
                  >
                    <el-button
                      icon="el-icon-delete"
                      size="mini"
                      @click="handleDeleteAPP(item)"
                    />
                  </div>
                  <div v-if="item.icon != null">
                    <img
                      :src="item.icon + ''"
                      alt=""
                      :onerror="defaultImg"
                      class="w-10 h-10 float-left"
                      style=""
                    />
                  </div>
                  <div v-else>
                    <img
                      :src="defaultImg"
                      class="w-10 h-10 float-left"
                      style=""
                    />
                  </div>
                  <div class="overflow-hidden pl-6">
                    <span class="block text-base truncate">
                      {{ item.name }}</span
                    >
                    <span
                      class="block text-sm text-gray-400 h-10 break-all"
                      style="
                        display: -webkit-box;
                        -webkit-line-clamp: 2;
                        -webkit-box-orient: vertical;
                      "
                    >
                      {{ item.description }}</span
                    >
                  </div>
                </div>
              </div>
            </div>
            <div
              style="
                border-top-width: 2px;
                border-style: solid;
                margin: 15px -20px 0px -20px;
              "
              :style="'border-color:' + listColor[item.repo_name]"
            />
          </el-card>
        </el-col>
      </div>
    </div>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[12, 24, 48, 96]"
        :page-size="queryData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableDataTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  helmCharts,
  helmRepos,
  helmChartsStats,
  delHelmCharts,
} from "@/api/mainApi";
export default {
  components: {},
  data() {
    return {
      condition: true,
      colorList: [
        "#82c9ef",
        "#3686d3",
        "#0bcec3",
        "#f5c874",
        "#07b721",
        "#838fd6",
        "#ef8909",
        "#004198",
        "#e8989a",
        "#f4ea29",
        "#515151",
        "#aad08f",
        "#efb336",
        "#0e932e",
        "#1aaba8",
        "#7f14c1",
        "#84eba9",
        "#d1a25c",
        "#c9c32e",
        "#0a6720",
        "#19fa28",
        "#13227a",
        "#7cba59",
        "#ea986c",
        "#ea9518",
        "#eeb173",
        "#f9f28b",
        "#000000",
        "#88147f",
        "#594d9c",
      ],
      listColor: {},
      repo_name: [],
      defaultImg:
        'this.src="' +
        require("../../assets/images/appMarket/icons8-app-48.png") +
        '"',
      queryData: {
        repo_name: "", // 仓库名称
        category: "", // 类型
        app_name: "", // 名称
        page_size: 12,
        page_num: 1,
      },
      tableDataTotal: 0,
      tableData: [],
      repolist: [],
      sortlist: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  created() {
    this.init();
  },
  mounted() {},
  methods: {
    // 多选触发
    handleChange(value) {
      this.repo_name = value;
      this.queryData.repo_name = value.join(",");
      this.init();
    },
    // 类型选择
    handleTypeClick(value) {
      this.queryData.category = value;
      this.condition = false;
      this.init();
    },
    goDetail(item) {
      this.$router.push({
        path: "/appMarket/appDetail",
        query: {
          id: item.app_id,
        },
      });
    },
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await helmCharts(this.queryData);
      this.tableData = list.charts;
      this.tableDataTotal = list.total_num;
      const repolist = await helmRepos();
      this.repolist = repolist.repos;
      const sortlist = await helmChartsStats({
        repo_name: this.queryData.repo_name,
        category: this.queryData.category,
      });
      if (this.condition) {
        this.sortlist = [
          {
            app_total: 0,
            category: {
              category_id: 4,
              description: "image",
              name: "Image Registry",
              show_name: this.$t("form.imageRegistry"),
              icon: "el-icon-document-copy",
            },
          },
          {
            app_total: 0,
            category: {
              category_id: 6,
              description: "network",
              name: "Networking",
              show_name: this.$t("form.networking"),
              icon: "el-icon-bangzhu",
            },
          },
          {
            app_total: 0,
            category: {
              category_id: 2,
              description: "message",
              name: "Message Queueing",
              show_name: this.$t("form.messageQueueing"),
              icon: "el-icon-share",
            },
          },
          {
            app_total: 0,
            category: {
              category_id: 3,
              description: "storage",
              name: "storage",
              show_name: this.$t("form.storage"),
              icon: "el-icon-aim",
            },
          },
          {
            app_total: 0,
            category: {
              category_id: 1,
              description: "web",
              name: "Web Server",
              show_name: this.$t("form.webServer"),
              icon: "el-icon-set-up",
            },
          },
          {
            app_total: 0,
            category: {
              category_id: 5,
              description: "database",
              name: "Database & Cache",
              show_name: this.$t("form.databaseAndCache"),
              icon: "el-icon-coin",
            },
          },
          {
            app_total: 0,
            category: {
              category_id: 8,
              description: "uncategorized",
              name: "uncategorized",
              show_name: this.$t("form.uncategorized"),
              icon: "el-icon-edit-outline",
            },
          },
        ];
        for (var i = 0; i < this.sortlist.length; i++) {
          for (var j = 0; j < sortlist.length; j++) {
            if (this.sortlist[i].category.name == sortlist[j].category.name) {
              this.sortlist[i].app_total = sortlist[j].app_total;
            }
          }
        }
        this.sortlist.unshift({
          app_total: this.tableDataTotal,
          category: {
            category_id: "",
            description: "",
            name: "",
            show_name: this.$t("form.all"),
            icon: "el-icon-menu",
          },
        });
      }
      this.listColor = {};
      this.repolist.forEach((element, index) => {
        if (index < 30) {
          element[element.repo_name] = this.colorList[index];
        } else if (index >= 30 && index < 60) {
          var num = index - 30;
          element[element.repo_name] = this.colorList[num];
        } else {
          var nums = index - 60;
          element[element.repo_name] = this.colorList[nums];
        }
        this.listColor[element.repo_name] = element[element.repo_name];
      });
    },
    // 随机生成颜色
    randomHexColor() {
      // 随机生成十六进制颜色
      var hex = Math.floor(Math.random() * 16777216).toString(16); // 生成ffffff以内16进制数
      while (hex.length < 6) {
        // while循环判断hex位数，少于6位前面加0凑够6位
        hex = "0" + hex;
      }
      return "#" + hex; // 返回‘#'开头16进制颜色
    },
    // 删除应用
    handleDeleteAPP(value) {
      this.$confirm(this.$t("form.deleteAppConfirm"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          delHelmCharts(value.app_id)
            .then((res) => {
              this.$notify({
                type: "success",
                title: this.$t("message.deleteSuccess"),
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },

    searchinit() {
      (this.repo_name = []),
        (this.queryData = {
          repo_name: "", // 仓库名称
          category: "", // 类型
          app_name: "", // 名称
          page_size: 12,
          page_num: 1,
        });
      this.init();
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-button--mini {
  padding: 7px 8.5px;
}

.el-select-dropdown.is-multiple .el-select-dropdown__item.selected::after {
  content: "" !important;
}

.itemMian {
  overflow: hidden;

  .el-card {
    ::v-deep .el-card__body {
      padding: 15px 20px 0px 20px;
    }
  }

  ::v-deep .el-button--mini,
  .el-button--mini.is-round {
    border: none;
  }
}
</style>
