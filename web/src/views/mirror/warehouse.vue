<template>
  <div style="position: relative">
    <el-row
      type="flex"
      justify="end"
      style="position: absolute; right: 10px; top: -25%; padding-bottom: 20px"
    >
      <el-input
        v-model="keyword"
        :placeholder="$t('form.pleaseInputRepoName')"
        style="width: 400px"
      />
      <el-button icon="el-icon-search" type="primary" @click="search">{{
        $t("form.search")
      }}</el-button>
    </el-row>

    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :rules="rules"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-row>
          <el-form-item
            :label="$t('form.warehouseName') + ':'"
            prop="registry_id"
          >
            <el-select
              v-model="queryData.registry_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              @change="changeRegistry"
            >
              <el-option
                v-for="(item, index) in registries"
                :key="index"
                :value="item.registry_id"
                :label="item.registry_name"
              />
            </el-select>
          </el-form-item>

          <el-form-item :label="$t('form.projectName') + ':'" prop="project_id">
            <el-select
              v-model="queryData.project_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              @change="changeProjectId"
            >
              <el-option
                v-for="(item, index) in projectsList"
                :key="index"
                :value="item.project_id"
                :label="item.project_name"
              />
            </el-select>
          </el-form-item>

          <el-form-item>
            <div slot="label">{{ $t("form.imageName") + ":" }}</div>
            <el-input v-model="queryData.repo_name" />
          </el-form-item>
          <el-form-item style="">
            <el-button type="primary" @click="searchList(1)">
              {{ $t("form.query") }}
            </el-button>
          </el-form-item>
          <el-form-item style="">
            <el-button class="add_search" @click="resetList()">
              {{ $t("form.reset") }}
            </el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.repos"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      @row-click="rowClick"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column :label="$t('form.image')">
        <template slot-scope="scope">
          <span style="color: #409eff; cursor: pointer">{{
            scope.row.name
          }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.userName')">
        <template slot-scope="scope">
          {{ scope.row.user_name || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.organizationName')">
        <template slot-scope="scope">
          {{ scope.row.bp_name || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.size')">
        <template slot-scope="scope"> {{ scope.row.size || "-" }} </template>
      </el-table-column>
      <el-table-column :label="$t('form.downloadCount')">
        <template slot-scope="scope">
          {{ scope.row.artifact_count || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.createTime')" />
      <el-table-column :label="$t('form.updateTime')" />
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <div class="icon_cz" @click.stop="clickEditBtn(scope.row)">
              <i class="el-icon-edit" />
              {{ $t("form.edit") }}
            </div>
          </div>
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
    <edit ref="edit" />
  </div>
</template>

<script>
import {} from "vuex";
import {
  registries,
  projects,
  repositories,
  delUsers,
  getReposList,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import edit from "./warehouse/edit";

export default {
  components: {
    edit,
  },
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      keyword: "",
      searchType: 1,
      tableData: [],
      queryData: {
        page_size: 10,
        page_num: 1,
        repo_name: "",
        registry_id: "",
      },
      registry_id: "",
      project_id: "",
      activeInfo: {},

      multipleSelection: [],
      registries: [], // 仓库列表
      projectsList: [], // 项目列表
      rules: {
        registry_id: {
          required: true,
          message: this.$t("message.pleaseSelectWarehouse"),
        },
        project_id: {
          required: true,
          message: this.$t("message.pleaseSelectProject"),
        },
      },
    };
  },

  mounted() {},
  created() {
    // this.getList()
    this.init();
  },

  methods: {
    async init() {
      const list = await registries();
      this.registries = list.registries;
    },
    async search() {
      if (this.keyword) {
        this.searchType = 2;
        if (
          this.queryData.repo_name != "" &&
          this.queryData.repo_name != undefined
        ) {
          this.queryData.repo_name = encodeURIComponent(this.keyword);
        } else {
          this.queryData.repo_name = "";
        }

        var list = await getReposList(this.queryData);

        if (list.repos.length > 0) {
          for (var i = 0; i < list.repos.length; i++) {
            if (list.repos[i].size < 1024) {
              list.repos[i].size = list.repos[i].size + "B";
            } else {
              if (list.repos[i].size / 1024 < 1024) {
                const num = list.repos[i].size / 1024;
                list.repos[i].size =
                  this.$script.processing_numbers(num) + "KB";
              } else if (list.repos[i].size / 1024 / 1024 < 1024) {
                const num = list.repos[i].size / 1024 / 1024;
                list.repos[i].size =
                  this.$script.processing_numbers(num) + "MB";
              } else if (list.repos[i].size / 1024 / 1024 / 1024 < 1024) {
                const num = list.repos[i].size / 1024 / 1024 / 1024;
                list.repos[i].size =
                  this.$script.processing_numbers(num) + "GB";
              }
            }
          }
        }
      }
      this.tableData = list;
    },
    searchList() {
      this.searchType = 1;
      this.handleSizeChange(1);
    },
    // 点击行
    rowClick(row) {
      const obj = {
        registry_id: row.registry_id,
        project_id: row.project_id,
        repo_name: row.repo_name,
      };
      this.$.push({
        path: "/mirror/warehouse/detail",
        query: obj,
      });
    },
    // 编辑仓库名称
    changeRegistry(e) {
      this.registry_id = e;
      this.getProjectList();
    },
    changeProjectId(e) {
      this.project_id = e;
    },
    // 获取项目列表
    getProjectList() {
      const params = {};
      params.page_size = 100;
      params.page_num = 1;
      params.registry_id = this.registry_id;
      projects(this.registry_id, params)
        .then((res) => {
          this.projectsList = res.projects;
          if (this.projectsList.length > 0) {
            this.project_id = this.projectsList[0]["project_id"];
            this.handleCurrentChange(1);
          } else {
            this.project_id = "";
          }
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      if (this.searchType == 1) {
        this.getList();
      } else {
        this.search();
      }
    },
    // 重置
    resetList() {
      this.tableData = [];
      this.queryData = Object.assign(
        {},
        {
          page_size: 10,
          page_num: 1,
        }
      );
      this.registry_id = "";
      this.project_id = "";
      if (this.registries.length > 0) {
        this.getProjectList();
      }
    },
    handleCurrentChange(val) {
      if (val) {
        this.queryData.page_num = val;
      }
      if (this.searchType == 1) {
        this.getList();
      } else {
        this.search();
      }
    },
    async getList() {
      this.searchType = 1;
      if (this.registry_id && this.project_id) {
        for (var key in this.queryData) {
          if (this.queryData[key] === undefined || this.queryData[key] === "") {
            delete this.queryData[key];
          }
        }
        if (
          this.queryData.repo_name != "" &&
          this.queryData.repo_name != undefined
        ) {
          this.queryData.repo_name = encodeURIComponent(
            this.queryData.repo_name
          );
        } else {
          this.queryData.repo_name = "";
        }

        const params = {};
        params.page_size = 1;
        params.page_num = 1;
        params.registry_id = this.registry_id;
        params.project_id = this.project_id;
        params.image_name = this.queryData.repo_name;
        const list = await repositories(
          this.registry_id,
          this.project_id,
          params
        );
        this.tableData = list;
      } else {
        this.tableData = { repos: [], total_num: 0 };
      }
    },
    clickAddBtn() {
      this.$refs.addForm.add();
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteImage"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delUsers(value.id)
            .then((res) => {
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
    clickEditBtn(info) {
      //
      this.$refs.edit.show(JSON.parse(JSON.stringify(info)));
    },
  },
};
</script>

<style lang="scss" scoped></style>
