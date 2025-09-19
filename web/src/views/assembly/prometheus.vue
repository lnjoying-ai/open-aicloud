<template>
  <div class="warpMain">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="Server" name="first">
        <el-form
          :inline="true"
          size="small"
          :model="queryData"
          class="demo-form-inline"
        >
          <el-form-item>
            <div slot="label">{{ $t("form.name") }}:</div>
            <el-input
              v-model="queryData.name"
              :placeholder="$t('form.pleaseInputName')"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              class="add_search"
              type="primary"
              @click="handleCurrentChange(1)"
              >{{ $t("form.query") }}
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button class="add_search" @click="searchinit()">
              {{ $t("form.reset") }}
            </el-button>
          </el-form-item>
        </el-form>
        <el-table
          ref="multipleTable"
          v-loading="loadingServer"
          :data="tableData.prometheus_instances"
          stripe
          :element-loading-text="$t('domain.loading')"
          tooltip-effect="dark"
          style="width: 100%"
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column
            prop="name"
            :label="$t('form.name')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <router-link
                :to="'/component/prometheus/' + scope.row.id"
                style="
                  text-decoration: none;
                  font-size: 14px;
                  cursor: pointer;
                  color: rgb(64, 158, 255);
                "
              >
                <span>{{ scope.row.name ? scope.row.name : "-" }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="status" :label="$t('form.status')">
            <template slot-scope="scope">
              <div v-if="scope.row.status == '1'">
                <span
                  ><el-tag size="small" type="success">{{
                    $t("form.running")
                  }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '2'">
                <span
                  ><el-tag>{{ $t("form.starting") }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '3'">
                <span
                  ><el-tag size="small" type="danger">{{
                    $t("form.error")
                  }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '4'">
                <span
                  ><el-tag size="small" type="danger">{{
                    $t("form.down")
                  }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '5'">
                <span
                  ><el-tag>{{ $t("form.updating") }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '6'">
                <span
                  ><el-tag type="warning">{{ $t("form.pause") }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '7'">
                <span
                  ><el-tag type="info">{{ $t("form.unknown") }}</el-tag></span
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="type" :label="$t('form.type')">
            <template slot-scope="scope">
              <div v-if="scope.row.type == '0'"><span>server</span></div>
              <div v-if="scope.row.type == '1'"><span>agent</span></div>
            </template>
          </el-table-column>
          <el-table-column
            prop="target_num"
            :label="$t('form.collectionTerminalCount')"
            sortable
          >
            <template slot-scope="scope">
              {{ scope.row.target_num }}
            </template>
          </el-table-column>
          <el-table-column
            prop="create_time"
            :label="$t('form.createTime')"
            sortable
          >
            <template slot-scope="scope">
              {{ scope.row.create_time ? scope.row.create_time : "-" }}
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
      </el-tab-pane>
      <el-tab-pane label="Agent" name="second">
        <el-form
          :inline="true"
          size="small"
          :model="queryData"
          class="demo-form-inline"
        >
          <el-form-item>
            <div slot="label">{{ $t("form.name") }}:</div>
            <el-input
              v-model="queryData.name"
              :placeholder="$t('form.pleaseInputName')"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              class="add_search"
              type="primary"
              @click="handleCurrentChange(1)"
              >{{ $t("form.query") }}
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button class="add_search" @click="searchinit()">{{
              $t("form.reset")
            }}</el-button>
          </el-form-item>
        </el-form>
        <el-table
          ref="multipleTable"
          v-loading="loadingAgent"
          :data="tableData.prometheus_instances"
          stripe
          :element-loading-text="$t('domain.loading')"
          tooltip-effect="dark"
          style="width: 100%"
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column
            prop="name"
            :label="$t('form.name')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              <router-link
                :to="'/component/prometheus/' + scope.row.id"
                style="
                  text-decoration: none;
                  font-size: 14px;
                  cursor: pointer;
                  color: rgb(64, 158, 255);
                "
              >
                <span>{{ scope.row.name ? scope.row.name : "-" }}</span>
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="status" :label="$t('form.status')">
            <template slot-scope="scope">
              <div v-if="scope.row.status == '1'">
                <span
                  ><el-tag size="small" type="success">{{
                    $t("form.running")
                  }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '2'">
                <span
                  ><el-tag>{{ $t("form.starting") }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '3'">
                <span
                  ><el-tag size="small" type="danger">{{
                    $t("form.error")
                  }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '4'">
                <span
                  ><el-tag size="small" type="danger">{{
                    $t("form.down")
                  }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '5'">
                <span
                  ><el-tag>{{ $t("form.updating") }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '6'">
                <span
                  ><el-tag type="warning">{{ $t("form.pause") }}</el-tag></span
                >
              </div>
              <div v-if="scope.row.status == '7'">
                <span
                  ><el-tag type="info">{{ $t("form.unknown") }}</el-tag></span
                >
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="type" :label="$t('form.type')">
            <template slot-scope="scope">
              <div v-if="scope.row.type == '0'"><span>server</span></div>
              <div v-if="scope.row.type == '1'"><span>agent</span></div>
            </template>
          </el-table-column>
          <el-table-column
            prop="target_num"
            :label="$t('form.collectionTerminalCount')"
            sortable
          >
            <template slot-scope="scope">
              {{ scope.row.target_num }}
            </template>
          </el-table-column>
          <el-table-column
            prop="create_time"
            :label="$t('form.createTime')"
            sortable
          >
            <template slot-scope="scope">
              {{ scope.row.create_time ? scope.row.create_time : "-" }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="bp_name"
                           label="组织机构">
            <template slot-scope="scope">
              {{ scope.row.bp_name?scope.row.bp_name:"-" }}
            </template>
          </el-table-column>
          <el-table-column prop="user_name"
                           label="用户名称">
            <template slot-scope="scope">
              {{ scope.row.user_name?scope.row.user_name:"-" }}
            </template>
          </el-table-column> -->
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
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getMonitorPrometheus } from "@/api/mainApi";
import initData from "@/mixins/initData";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      activeName: "first",
      sup_this: this,
      tableData: [],
      queryData: {
        page_size: 10,
        page_num: 1,
        type: 0,
        name: "",
      },
      loadingAgent: false,
      loadingServer: false,
    };
  },
  async created() {
    this.init();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    searchinit() {
      var type = this.queryData.type;
      this.queryData = {
        page_size: 10,
        page_num: 1,
        name: "",
        type: type,
      };
      this.init();
    },
    handleClick() {
      this.queryData = {
        page_size: 10,
        page_num: 1,
        name: "",
        type: "",
      };
      this.activeName == "first"
        ? (this.queryData.type = 0)
        : (this.queryData.type = 1);
      this.init();
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
      this.activeName == "first"
        ? (this.loadingServer = true)
        : (this.loadingAgent = true);
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getMonitorPrometheus(this.queryData);
      this.tableData = list;
      this.activeName == "first"
        ? (this.loadingServer = false)
        : (this.loadingAgent = false);
    },
  },
};
</script>

<style lang="scss" scoped></style>
