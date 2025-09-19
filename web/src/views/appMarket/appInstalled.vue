<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.cluster") }}:</div>
          <el-select
            v-model="queryData.cluster_id"
            filterable
            :placeholder="$t('form.pleaseSelect')"
          >
            <el-option
              v-for="(item, index) in clusterList"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.type") }}:</div>
          <el-select
            v-model="queryData.category"
            filterable
            :placeholder="$t('form.pleaseSelect')"
          >
            <el-option
              v-for="(item, index) in sortlist"
              :key="index"
              :label="item.category.show_name"
              :value="item.category.name"
              >{{ item.category.show_name }}</el-option
            >
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.name") }}:</div>
          <el-input
            v-model="queryData.stack_name"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="init()">{{
            $t("form.search")
          }}</el-button>
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" :label="$t('form.name')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="'/appMarket/appInstalledDetail?id=' + scope.row.stack_id"
          >
            {{ scope.row.name ? scope.row.name : "-" }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="chart_name" label="chart">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="'/appMarket/appDetail?id=' + scope.row.chart_app_id"
          >
            {{ scope.row.chart_name }}({{ $t("form.version") }}:{{
              scope.row.chart_version
            }})
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="name" :label="$t('form.clusterNamespace')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="
              '/serverCluster/detail/' +
              scope.row.cluster_id +
              '/' +
              scope.row.cluster_name
            "
            ><span>{{ scope.row.cluster_name }}</span> </router-link
          ><span>/{{ scope.row.namespace }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('form.status')">
        <template slot-scope="scope">
          {{
            scope.row.status == 1
              ? $t("form.appCreating")
              : scope.row.status == 2
              ? $t("form.running")
              : scope.row.status == 3
              ? $t("form.stop")
              : scope.row.status == 4
              ? $t("form.deployFailed")
              : scope.row.status == 5
              ? $t("form.appCleared")
              : scope.row.status == 6
              ? $t("form.partialRunning")
              : $t("form.unknown")
          }}
        </template>
      </el-table-column>

      <el-table-column prop="creator_name" :label="$t('form.creator')">
        <template slot-scope="scope">
          {{ scope.row.creator_name ? scope.row.creator_name : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="create_time" :label="$t('form.createTime')">
        <template slot-scope="scope">
          {{ scope.row.create_time ? scope.row.create_time : "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="130">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
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
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
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
  helmStacks,
  getClusters,
  delHelmStacks,
  helmChartsStats,
} from "@/api/mainApi";

export default {
  components: {},
  data() {
    return {
      queryData: {
        cluster_id: "", // 集群
        category: "", // 类型
        stack_name: "", // 名称
        page_size: 10,
        page_num: 1,
      },
      tableDataTotal: 0,
      tableData: [], // 应用列表
      clusterList: [], // 集群列表
      sortlist: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  created() {
    this.init();
    this.getCluster();
  },
  mounted() {},
  methods: {
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await helmStacks(this.queryData);
      this.tableData = list.apps;
      this.tableDataTotal = list.total_num;
      const sortlist = await helmChartsStats();
      this.sortlist = sortlist;
      for (var i = 0; i < this.sortlist.length; i++) {
        if (this.sortlist[i].category.show_name == "Image Registry") {
          this.sortlist[i].category.show_name = this.$t("form.imageRegistry");
        } else if (this.sortlist[i].category.show_name == "Networking") {
          this.sortlist[i].category.show_name = this.$t("form.networking");
        } else if (this.sortlist[i].category.show_name == "Message Queueing") {
          this.sortlist[i].category.show_name = this.$t("form.messageQueueing");
        } else if (this.sortlist[i].category.show_name == "storage") {
          this.sortlist[i].category.show_name = this.$t("form.storage");
        } else if (this.sortlist[i].category.show_name == "Web Server") {
          this.sortlist[i].category.show_name = this.$t("form.webServer");
        } else if (this.sortlist[i].category.show_name == "Database & Cache") {
          this.sortlist[i].category.show_name = this.$t(
            "form.databaseAndCache"
          );
        } else if (this.sortlist[i].category.show_name == "uncategorized") {
          this.sortlist[i].category.show_name = this.$t("form.uncategorized");
        }
      }
    },
    getCluster() {
      getClusters().then((res) => {
        this.clusterList = res.clusters;
      });
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
      this.queryData = {
        cluster_id: "", // 仓库名称
        category: "", // 类型
        stack_name: "", // 名称
        page_size: 10,
        page_num: 1,
      };
      this.init();
    },
    clickDelBtn(value) {
      this.$confirm(this.$t("form.deleteAppConfirm"), this.$t("message.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          delHelmStacks(value.stack_id)
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
  },
};
</script>

<style lang="scss" scoped></style>
