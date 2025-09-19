<template>
  <div class="warpMain">
    <div style="margin-bottom: 10px">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item :label="$t('form.templateName') + ':'">
          <el-input
            v-model="queryData.name"
            :placeholder="$t('form.pleaseInput')"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            class="add_search"
            type="primary"
            @click="search()"
            >{{ $t("form.query") }}</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button class="add_search" @click="initform()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button size="small" type="primary" @click="clickAddBtn">{{
            $t("form.addTemplate")
          }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div
      v-loading="loading"
      class="itemMian"
      :element-loading-text="$t('domain.loading')"
    >
      <div v-if="tableData.length">
        <div
          v-for="(item, index) in tableData"
          :key="index"
          @click="handleVersionOpen(item)"
        >
          <el-col
            :key="index"
            :xs="24"
            :sm="12"
            :md="8"
            :lg="8"
            :xl="6"
            style="padding: 5px"
          >
            <el-card class="box-card" style="position: relative">
              <div class="czlb" @click.prevent.stop>
                <el-popover placement="bottom" width="110" trigger="click">
                  <div class="icon_cz" @click.stop="clickEditBtn(item)">
                    <i class="el-icon-edit" />
                    {{ $t("form.edit") }}
                  </div>
                  <div class="icon_cz" @click.stop="clickDelBtn(item)">
                    <i class="el-icon-delete" />
                    {{ $t("form.delete") }}
                  </div>
                  <div class="icon_cz">
                    <a
                      :href="
                        nowLocalhost +
                        '/api/aos/v1/templates/' +
                        item.nowVersion +
                        '/archives'
                      "
                    >
                      <i class="el-icon-download" />
                      {{ $t("form.download") }}
                    </a>
                  </div>
                  <el-button
                    slot="reference"
                    icon="el-icon-more"
                    class="czbtn right_czbtn"
                  />
                </el-popover>
              </div>
              <div class="content">
                <div class="headerspace">
                  <div>
                    <div>
                      <img
                        :src="item.logo_url + ''"
                        alt=""
                        :onerror="defaultImg"
                      />
                    </div>
                    <div class="name">
                      {{ item.name }}
                    </div>
                    <div class="description">
                      {{ getDesc(item).description }}
                    </div>
                  </div>
                </div>
                <div style="margin-top: 24px; color: #79879c">
                  <span v-if="!item.bpName" class="createName"
                    >{{ $t("form.creator") }}：{{ item.userName }}</span
                  >
                  <span v-else class="createName"
                    >{{ $t("form.creator") }}：{{ item.bpName }}</span
                  >
                  <span class="version">
                    <span style="max-width: 200px"
                      ><span>{{ $t("form.version") }}：</span>
                      <span>
                        <el-select
                          v-model="item.nowVersion"
                          :placeholder="$t('form.pleaseSelect')"
                          size="mini"
                          style="width: 65%"
                          @change="changeVersions(item, $event)"
                        >
                          <el-option
                            v-for="item in item.versions"
                            :key="item.id"
                            :label="item.version"
                            :value="item.id"
                          />
                        </el-select>
                      </span>
                    </span>
                  </span>
                </div>
              </div>
            </el-card>
          </el-col>
        </div>
      </div>
      <div v-else>
        <el-empty :description="$t('form.noData')" />
      </div>
    </div>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[12, 24, 48]"
        :page-size="queryData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="queryData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
    <addApp
      ref="addApp"
      :sup_this="sup_this"
      :template="activeInfoTemplate"
      :info="activeInfoApp"
    />
  </div>
</template>

<script>
import { templates, delTemplates } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./appTemplate/addForm";
import addApp from "./appTemplate/addApp";
import { mapGetters } from "vuex";

export default {
  components: {
    addForm,
    addApp,
  },
  mixins: [initData],
  data() {
    return {
      loading: false,
      value: "",
      nowLocalhost: "",
      tempInfo: "",
      activeInfo: {},
      activeInfoApp: "",
      activeInfoTemplate: "",
      nowMouse: "",
      sup_this: this,
      defaultImg:
        'this.src="' +
        require("../../assets/images/appMarket/icons8-app-48.png") +
        '"',
      tableData: [],
      options: [],
      queryData: {
        name: "",

        page_size: 12,
        page_num: 1,
        total_num: 0,
      },
    };
  },

  created() {
    this.init();
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  mounted() {
    this.nowLocalhost = window.location.origin;
  },

  methods: {
    // 查询
    search() {
      this.init();
    },
    initform() {
      this.queryData = {
        name: "",

        page_size: 12,
        page_num: 1,
        total_num: 0,
      };
      this.init();
    },
    // 版本选择后触发
    changeVersions(value, e) {
      // 强制更新
      this.$forceUpdate();
    },

    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    async init() {
      this.loading = true;
      var data = {
        name: this.queryData.name,
        page_size: this.queryData.page_size,
        page_num: this.queryData.page_num,
      };
      const list = await templates(data);
      if (
        list.templates != undefined &&
        list.templates.length > 0 &&
        list.templates != null
      ) {
        this.tableData = list.templates;
        this.tableData.map((item) => {
          item.nowVersion = item.versions[0].id;
        });
        this.queryData.total_num = list.total_num;
        this.loading = false;
      } else {
        this.tableData = [];
        this.loading = false;
      }
    },
    clickAddBtn() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
    },
    getDesc(item) {
      return item.versions.filter((val) => {
        return val.id == item.nowVersion;
      })[0];
    },
    clickAddAppBtn(item) {
      this.activeInfoTemplate = item.versions.filter((val) => {
        return val.id == item.nowVersion;
      })[0];

      this.$refs.addApp.id = "";
      this.$refs.addApp.isAdd = true;
      this.$refs.addApp.dialog = true;
    },
    handleVersionOpen(value) {
      this.$router.push({
        path: "/containerApplicationService/version",
        query: {
          nowVersion: value.nowVersion,
          temId: value.id,
        },
      });
    },
    clickDelBtn(item) {
      var value = item.versions.filter((val) => {
        return val.id == item.nowVersion;
      })[0];
      this.$confirm(
        this.$t("form.confirmDeleteTemplate"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delTemplates(value.name, value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickEditBtn(item) {
      this.activeInfo = item.versions.filter((val) => {
        return val.id == item.nowVersion;
      })[0];
      this.$refs.addForm.versions = item.versions;
      this.$refs.addForm.id = this.activeInfo.id;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
.itemMian {
  overflow: hidden;

  .content {
    height: 152px;
    padding: 24px;
    border-radius: 4px;
    background-color: #fff;

    .createName {
      -o-text-overflow: ellipsis;
      text-overflow: ellipsis;
      white-space: nowrap;
      word-wrap: normal;
      overflow: hidden;
      font-size: 14px;
      width: 60%;
      float: left;
    }

    .version {
      float: right;
      -o-text-overflow: ellipsis;
      text-overflow: ellipsis;
      white-space: nowrap;
      word-wrap: normal;
      overflow: hidden;
      font-size: 14px;
      position: absolute;
      left: 57.5%;
    }

    .headerspace {
      position: relative;
      margin-bottom: 12px;
      height: 60px;

      img {
        width: 48px;
        height: 48px;
        line-height: 48px;
        left: 0;
        position: absolute;
        top: 50%;
        -webkit-transform: translateY(-50%);
        -ms-transform: translateY(-50%);
        transform: translateY(-50%);
      }

      .name {
        font-family: Roboto, PingFang SC, Lantinghei SC, Helvetica Neue,
          Helvetica, Arial, Microsoft YaHei, 微软雅黑, STHeitiSC-Light, simsun,
          宋体, WenQuanYi Zen Hei, WenQuanYi Micro Hei, sans-serif;
        color: #242e42;
        font-weight: 500;
        font-size: 14px;
        position: absolute;
        left: 80px;
        width: 150px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .description {
        font-family: Roboto, PingFang SC, Lantinghei SC, Helvetica Neue,
          Helvetica, Arial, Microsoft YaHei, 微软雅黑, STHeitiSC-Light, simsun,
          宋体, WenQuanYi Zen Hei, WenQuanYi Micro Hei, sans-serif;
        line-height: 1.67;
        color: #79879c;
        position: absolute;
        left: 80px;
        top: 17px;
        text-overflow: ellipsis;
        white-space: nowrap;
        word-wrap: normal;
        overflow: hidden;
        max-width: 170px;
        font-size: 14px;
      }
    }
  }

  .el-card {
    ::v-deep .el-card__body {
      padding: 15px 20px;
    }
  }
}

.btn1 {
  margin: 0 auto;
  display: block;
}

.tempdialog {
  .el-form-item {
    margin-bottom: 0px;
  }
}

.icon_cz {
  display: block;
  height: 24px;
  color: #666666;
  align-items: center;
  cursor: pointer;
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 24px;
  text-align: center;
}

.icon_cz:hover {
  background: #daefff;
}

.cz_icon {
  margin-left: 16px;
  margin-right: 23px;
}

.czlb {
  position: absolute;
  top: 8px;
  right: 4px;
}

.czbtn {
  width: 23px;
  height: 26px;
  border: 0px solid #d0d0d0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_czbtn {
  border-radius: 3px 0 0 3px;
  border-right: 0px;
  cursor: pointer;
}

.left_czbtn img {
  height: 11px;
}

.right_czbtn {
  border-radius: 3px;
}
</style>
