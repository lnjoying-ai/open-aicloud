<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-row>
          <el-form-item>
            <div slot="label">{{ $t("form.name") }}</div>
            <el-input v-model="queryData.repo_name" />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              type="primary"
              @click="searchList()"
              >{{ $t("form.query") }}</el-button
            >
          </el-form-item>
          <el-form-item>
            <el-button class="add_search" @click="resetList()">{{
              $t("form.reset")
            }}</el-button>
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
    >
      <el-table-column :label="$t('form.image')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff; cursor: pointer"
            :to="
              '/mirror/wareserve/detail/' +
              scope.row.registry_id +
              '/' +
              scope.row.project_id +
              '/' +
              encodeURIComponent(scope.row.repo_name)
            "
          >
            {{ scope.row.name }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="user_name" :label="$t('form.userName')" />
      <el-table-column prop="bp_name" :label="$t('form.bp')">
        <template slot-scope="scope">
          {{ scope.row.bp_name || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.size')">
        <template slot-scope="scope">
          {{ scope.row.size ? sizeChange(scope.row.size) : "-" }}</template
        >
      </el-table-column>
      <el-table-column
        prop="artifact_count"
        :label="$t('form.downloadCount')"
      />
      <el-table-column prop="create_time" :label="$t('form.createTime')" />
      <el-table-column prop="update_time" :label="$t('form.updateTime')" />
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <el-popover
            v-if="userInfo.name == scope.row.user_name || userInfo.kind == '1'"
            placement="bottom"
            width="110"
            trigger="click"
          >
            <div class="icon_cz" @click.stop="clickEditBtn(scope.row)">
              <i class="el-icon-edit" />
              {{ $t("form.edit") }}
            </div>

            <el-button
              slot="reference"
              icon="el-icon-more"
              class="czbtn right_czbtn"
            />
          </el-popover>
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
import { mapGetters } from "vuex";
import { repositories } from "@/api/mainApi";
import initData from "@/mixins/initData";
import edit from "../warehouse/edit";

export default {
  components: {
    edit,
  },
  mixins: [initData],
  data() {
    return {
      tableData: {
        repos: [],
        total_num: 0,
      },
      queryData: {
        page_size: 10,
        page_num: 1,
        repo_name: "",
      },
      registry_id: "",
      project_id: "",
    };
  },

  mounted() {
    this.registry_id = this.$route.params.registryId;
    this.project_id = this.$route.params.projectId;
    this.getList();
  },
  created() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    searchList() {
      this.handleCurrentChange(1);
    },
    sizeChange(val) {
      var data;
      if (val < 1024) {
        data = val + "B";
      } else {
        if (val / 1024 < 1024) {
          const num = val / 1024;
          data = this.$script.processing_numbers(num) + "KB";
        } else if (val / 1024 / 1024 < 1024) {
          const num = val / 1024 / 1024;
          data = this.$script.processing_numbers(num) + "MB";
        } else if (val / 1024 / 1024 / 1024 < 1024) {
          const num = val / 1024 / 1024 / 1024;
          data = this.$script.processing_numbers(num) + "GB";
        }
      }
      return data;
    },
    // 点击行
    rowClick(row) {
      this.$router.push({
        path:
          "/mirror/wareserve/detail/" +
          row.registry_id +
          "/" +
          row.project_id +
          "/" +
          encodeURIComponent(row.repo_name),
      });
    },

    handleSizeChange(val) {
      if (val) {
        this.queryData.page_size = val;
      }
      this.getList();
    },
    // 重置
    resetList() {
      this.queryData = {
        page_size: 10,
        page_num: 1,
        repo_name: "",
      };
      this.handleCurrentChange(1);
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.getList();
    },
    async getList() {
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
        }
        const list = await repositories(
          this.registry_id,
          this.project_id,
          this.queryData
        );
        this.tableData = list;
      } else {
        this.tableData = { repos: [], total_num: 0 };
      }
    },

    clickEditBtn(info) {
      this.$refs.edit.show(JSON.parse(JSON.stringify(info)));
    },
  },
};
</script>

<style lang="scss" scoped></style>
