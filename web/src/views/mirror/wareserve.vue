<template>
  <div>
    <el-card>
      <div slot="header" class="clearfix">
        <div
          style="
            display: flex;
            align-items: center;
            justify-content: space-between;
          "
        >
          <div>
            <el-breadcrumb
              separator="/"
              style="font-size: 18px; font-weight: blod"
            >
              <el-breadcrumb-item
                ><span style="cursor: pointer" @click="tabKey = 1">{{
                  $t("form.project")
                }}</span></el-breadcrumb-item
              >
              <el-breadcrumb-item v-if="tabKey >= 2"
                ><span style="cursor: pointer" @click="tabKey = 2">{{
                  tabKey == 2 ? $t("form.image") : name1
                }}</span></el-breadcrumb-item
              >
              <el-breadcrumb-item v-if="tabKey >= 3"
                ><span style="cursor: pointer" @click="tabKey = 3">{{
                  name2
                }}</span></el-breadcrumb-item
              >
            </el-breadcrumb>
          </div>
          <div style="display: flex; align-items: center; padding: 5px">
            <el-input
              v-model="keyword"
              :placeholder="$t('form.pleaseInputImageName')"
              style="width: 400px"
            />
            <el-button icon="el-icon-search" type="primary" @click="search">{{
              $t("form.search")
            }}</el-button>
          </div>
        </div>
      </div>
      <div v-show="tabKey == 1">
        <div class="head_rq">
          <el-form
            ref="form"
            :inline="true"
            size="small"
            :model="queryData"
            :rules="rules"
            class="demo-form-inline"
          >
            <el-row :span="24">
              <el-col :span="4">
                <el-form-item
                  :label="$t('form.region') + ':'"
                  prop="regions_id"
                >
                  <el-select
                    v-model="queryData.regions_id"
                    filterable
                    :placeholder="$t('form.pleaseSelect')"
                    @change="changeRegions"
                  >
                    <el-option
                      v-for="(item, index) in arealist"
                      :key="index"
                      :label="item.region_name"
                      :value="item.region_id"
                    />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item>
                  <div slot="label">{{ $t("form.projectName") + ":" }}</div>
                  <el-input
                    v-model="queryData.project_name"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item>
                  <div slot="label">{{ $t("form.visibleScope") + ":" }}</div>
                  <el-select
                    v-model="queryData.scope"
                    :placeholder="$t('form.pleaseSelect')"
                    @change="changeScope"
                  >
                    <el-option :label="$t('form.public')" :value="2" />
                    <el-option :label="$t('form.bpVisible')" :value="1" />
                    <el-option :label="$t('form.private')" :value="0" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="4">
                <el-form-item>
                  <div slot="label">{{ $t("form.user") + ":" }}</div>
                  <el-select
                    v-model="user_id"
                    filterable
                    :placeholder="$t('form.pleaseSelect')"
                    @change="changeUser"
                  >
                    <el-option :label="$t('form.my')" :value="0" />
                    <el-option :label="$t('form.all')" :value="1" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-form-item style="float: right">
                <el-button type="primary" @click="clickAddBtn">{{
                  $t("form.addProject")
                }}</el-button>
              </el-form-item>
              <el-form-item style="float: right">
                <el-button type="primary" @click="submitForm(queryData)">{{
                  $t("form.query")
                }}</el-button>
              </el-form-item>
              <el-form-item style="float: right">
                <el-button
                  icon="el-icon-search"
                  class="add_search"
                  @click="searchinit()"
                  >{{ $t("form.reset") }}</el-button
                >
              </el-form-item>
            </el-row>
          </el-form>
        </div>
        <el-table
          ref="multipleTable"
          :data="tableData.projects"
          stripe
          tooltip-effect="dark"
          style="width: 100%"
          @click="rowClick"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column :label="$t('form.projectName')">
            <template slot-scope="scope">
              <span
                style="color: #409eff; cursor: pointer"
                @click="rowClick(scope.row)"
                >{{ scope.row.project_name }}</span
              >
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.visibleScope')">
            <template slot-scope="scope">
              <span v-if="scope.row.scope == '0'">{{
                $t("form.private")
              }}</span>
              <span v-if="scope.row.scope == '1'">{{
                $t("form.bpVisible")
              }}</span>
              <span v-if="scope.row.scope == '2'">{{ $t("form.public") }}</span>
            </template>
          </el-table-column>
          <el-table-column
            prop="project_desc"
            :label="$t('form.projectDesc')"
          />
          <el-table-column
            prop="registry_name"
            :label="$t('form.registryName')"
          />
          <el-table-column
            v-if="
              userInfo.kind == '0' ||
              userInfo.kind == '1' ||
              userInfo.kind == '2' ||
              userInfo.kind == '3'
            "
            prop="user_name"
            :label="$t('form.userName')"
          />
          <el-table-column
            v-if="
              userInfo.kind == '0' ||
              userInfo.kind == '1' ||
              userInfo.kind == '3'
            "
            prop="bp_name"
            :label="$t('form.organization')"
          />
          <el-table-column prop="create_time" :label="$t('form.createTime')" />
          <el-table-column prop="update_time" :label="$t('form.updateTime')" />
          <el-table-column :label="$t('form.operation')" width="100">
            <template slot-scope="scope">
              <div v-if="userInfo.name == scope.row.user_name" class="czlb">
                <el-popover placement="bottom" width="110" trigger="hover">
                  <div class="icon_cz" @click="clickEditBtn(scope.row)">
                    <i class="el-icon-edit" />
                    {{ $t("form.edit") }}
                  </div>
                  <div class="icon_cz" @click="clickDelBtn(scope.row)">
                    <i class="el-icon-delete" />
                    {{ $t("form.delete") }}
                  </div>

                  <el-button
                    slot="reference"
                    icon="el-icon-more"
                    class="czbtn right_czbtn"
                  />
                </el-popover>
              </div>
              <el-button v-else size="mini">…</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="flex justify-end mt-4 px-4">
          <el-pagination
            :current-page="queryData.page_num"
            :page-sizes="[5, 10, 20, 50, 100]"
            :page-size="queryData.page_size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="tableData.total_num"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
        <div class="command">
          <el-row>
            <el-row v-if="msg1">
              {{ $t("form.loginCommand") }}：{{ msg1
              }}<i
                class="el-icon-document-copy"
                style="font-size: 17px"
                @click="copy($event, msg1)"
              />
            </el-row>
            <el-row v-if="msg2">
              {{ $t("form.pullCommand") }}：{{ msg2
              }}<i
                class="el-icon-document-copy"
                style="font-size: 17px"
                @click="copy($event, msg2)"
              />
            </el-row>
            <el-row v-if="msg3">
              {{ $t("form.pushCommand") }}：{{ msg3
              }}<i
                class="el-icon-document-copy"
                style="font-size: 17px"
                @click="copy($event, msg3)"
              />
            </el-row>
          </el-row>
        </div>
      </div>
      <div v-show="tabKey == 2">
        <warehouse ref="warehouse" @choseCallBack="choseCallBack" />
      </div>
      <div v-show="tabKey == 3">
        <detail ref="detail" @changeName="changeName" />
      </div>
    </el-card>
    <addForm ref="addForm" />
    <changeRegPwd ref="changeRegPwd" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Clipboard from "clipboard";
import {
  delProjects,
  registries,
  projects,
  getRegions,
  getcommand,
  getUserRegions,
  getReposList,
  usersExist,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./project/addForm";
import warehouse from "./wareserve/warehouse";
import detail from "./wareserve/detail";
import changeRegPwd from "../system/userMsg/changeRegPwd.vue";
export default {
  components: {
    addForm,
    warehouse,
    detail,
    changeRegPwd,
  },
  mixins: [initData],
  data() {
    return {
      tabKey: 1,
      name1: this.$t("form.image"),
      name2: "",
      sup_this: this,
      tableData: {
        projects: [],
        total_num: 0,
      },
      keyword: "",
      queryData: {
        registry_id: "",
        project_name: "",
        regions_id: "",
        scope: "",
        user_id: "",
        bp_id: "",
        page_size: 10,
        page_num: 1,
      },
      rules: {
        regions_id: {
          required: true,
          message: this.$t("message.pleaseSelectRegion"),
        },
      },
      regions_id: "",
      user_id: 0,
      activeInfo: {},
      registries: [], // 仓库列表
      multipleSelection: [],
      arealist: [],
      registry_id: "",
      msg1: "",
      msg2: "",
      msg3: "",
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  mounted() {
    this.clickAddBtn("check");
  },
  created() {
    this.areainit();

    this.queryData.user_id = this.userInfo.id;
  },
  methods: {
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text });
      clipboard.on("success", () => {
        this.$notify({
          title: this.$t("message.copySuccess"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.on("error", () => {
        // 不支持复制
        this.$notify({
          title: this.$t("message.copyError"),
          type: "success",
          duration: 2500,
        });
        // 释放内存
        clipboard.off("error");
        clipboard.off("success");
        clipboard.destroy();
      });
      clipboard.onClick(e);
    },
    changeUser(value) {
      if (value == 0) {
        this.queryData.user_id = this.userInfo.id;
      } else {
        this.queryData.user_id = "";
      }
      this.init();
    },
    changeScope(val) {
      this.queryData.scope = val;
      this.init();
    },
    getUserExi() {},
    rowClick(e) {
      this.tabKey = 2;
      this.$refs.warehouse.check(e);
    },
    async init(id) {
      const list = await registries({ region_id: id });
      this.registries = list.registries;
      if (list.registries && list.registries.length > 0) {
        this.queryData.registry_id = list.registries[0].registry_id;
        this.queryData.page_num = 1;
        this.getList();
      } else {
        this.tableData = [];
      }
    },
    search() {
      if (this.keyword) {
        this.tabKey = 2;
        this.$nextTick(() => {
          this.$refs.warehouse.search(this.keyword);
        });
      }
    },
    async areainit() {
      const list = await getUserRegions();
      this.arealist = list;
      if (this.arealist.length > 0) {
        this.queryData.regions_id = this.arealist[0].region_id;
        this.regions_id = this.arealist[0].region_id;
        this.init(this.arealist[0].region_id);
        this.commandInit(this.regions_id);
      }
    },
    async commandInit(region_id) {
      getcommand({ region_id: region_id, command_type: 0 }).then((res) => {
        this.msg1 = res;
        getcommand({ region_id: region_id, command_type: 1 }).then((res) => {
          this.msg2 = res;
        });
        getcommand({ region_id: region_id, command_type: 2 }).then((res) => {
          this.msg3 = res;
        });
      });
    },
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.queryData.page_num = 1;
          this.getList();
        } else {
          return false;
        }
      });
    },
    // 切换区域
    changeRegions(e) {
      this.regions_id = e;
      this.queryData.regions_id = e;
      this.commandInit(e);
      this.init(e);
    },
    // 点击回调
    choseCallBack(obj) {
      this.tabKey = 3;
      this.$refs.detail.show(obj);
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.getList();
    },
    changeName(obj) {
      this.name1 = obj.project_name;
      this.name2 = obj.repo_name;
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.getList();
    },
    async getList() {
      if (this.queryData.registry_id) {
        for (var key in this.queryData) {
          if (this.queryData[key] === undefined || this.queryData[key] === "") {
            delete this.queryData[key];
          }
        }

        const list = await projects(this.queryData.registry_id, this.queryData);
        this.tableData = list;
        if (
          list.projects != "" &&
          list.projects != null &&
          list.projects != undefined
        ) {
          if (this.tableData.projects.length > 0) {
            this.tableData.total_num = this.tableData.projects.length;
          } else {
            this.tableData.total_num = 0;
          }
        }
      }
    },
    clickAddBtn(type) {
      usersExist()
        .then(() => {
          if (type != "check") {
            this.$refs.addForm.add();
          }
        })
        .catch(() => {
          this.$confirm(
            this.$t("message.pleaseSetWarehousePassword"),
            this.$t("message.tip"),
            {
              confirmButtonText: this.$t("message.confirm"),
              cancelButtonText: this.$t("message.cancel"),
              type: "warning",
              showCancelButton: false,
              showClose: false,
              closeOnClickModal: false,
            }
          )
            .then(() => {
              this.$refs.changeRegPwd.add({ user_name: this.userInfo.name });
            })
            .catch(() => {});
        });
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteProject"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delProjects(value.registry_id, value.project_id)
            .then(() => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getList();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    searchinit() {
      this.queryData = {
        registry_id: "",
        project_name: "",
        scope: "",
        user_id: "",
        bp_id: "",
        page_size: 10,
        page_num: 1,
      };
      this.regions_id = "";
      this.user_id = "";
      this.tableData = [];
      this.getList();
    },

    clickEditBtn(info) {
      this.$refs.addForm.edit(JSON.parse(JSON.stringify(info)));
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .search .el-card__body {
  padding: 0 !important;
}

.demo-form-inline {
  .el-form-item {
    display: flex;
    align-items: center;

    ::v-deep .el-form-item__content {
      flex: 1;
      width: 100%;
    }
  }
}

.command {
  border-top: 1px solid #e0e0e0;
  margin-top: 30px;
  padding: 30px 10px;

  .el-row {
    line-height: 36px;
    color: #333;
    font-size: 16px;
  }

  i {
    margin-left: 10px;
    font-size: 36px;
  }
}
</style>
