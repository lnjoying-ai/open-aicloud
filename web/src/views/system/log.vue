<template>
  <div class="warpMain">
    <div>
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.serviceName") }}:</div>
          <el-select
            v-model="queryData.service"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            filterable
            @change="handleServiceChange"
          >
            <el-option
              v-for="(item, index) in serviceData"
              :key="index"
              :label="item.display_name"
              :value="item.iam_code"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.resourceId") }}:</div>
          <el-input
            v-model="queryData.resource_id"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item v-if="kind == 0 || kind == 1 || kind == 2">
          <div slot="label">{{ $t("form.operator") }}:</div>
          <el-select
            v-model="queryData.user_id"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            filterable
            @change="handleUserChange"
          >
            <el-option
              v-for="(item, index) in usersData"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.timeRange') + ':'">
          <el-date-picker
            v-model="queryData.time"
            type="datetimerange"
            :picker-options="pickerOptions"
            :range-separator="$t('form.to')"
            :start-placeholder="$t('form.startDate')"
            :end-placeholder="$t('form.endDate')"
            format="yyyy-MM-dd HH:mm:ss"
            @change="handleTimeChange"
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
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column
        prop="resource"
        :label="$t('form.logSource')"
        min-width="120"
      >
        <template slot-scope="scope">
          {{ scope.row.resource ? scope.row.resource : "-" }}
          :
          {{ scope.row.service ? scope.row.service : "-" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        :label="$t('form.logDetail')"
        min-width="380"
      >
        <template slot-scope="scope">
          <span>
            <div>
              <el-tag
                v-if="scope.row.level == 'ERROR'"
                size="mini"
                type="danger"
                class="mr-3"
                >{{ $t("form.failed") }}</el-tag
              >
              <el-tag
                v-if="scope.row.level == 'INFO'"
                size="mini"
                type="success"
                class="mr-3"
                >{{ $t("form.success") }}</el-tag
              >
              <span v-html="scope.row.description" />
            </div>
            <div v-if="scope.row.level == 'ERROR'" style="color: #f56c6c">
              {{ scope.row.result ? scope.row.result : "-" }}
            </div>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="operator" :label="$t('form.operator')">
        <template slot-scope="scope">
          {{ scope.row.operator ? scope.row.operator : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="access_ip" :label="$t('form.ip')">
        <template slot-scope="scope">
          {{ scope.row.access_ip ? scope.row.access_ip : "-" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="trigger_time"
        :label="$t('form.operationTime')"
        sortable
      >
        <template slot-scope="scope">
          {{
            scope.row.trigger_time ? formatDate(scope.row.trigger_time) : "-"
          }}
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="queryData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="totalNum"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getApiLogs, getUsers } from "@/api/mainApi";
import { getService } from "@/api/iam/serve";
import initData from "@/mixins/initData";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      pickerOptions: {
        shortcuts: [
          {
            text: this.$t("message.lastOneHour"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 1);
              picker.$emit("pick", [start, end]);
            },
          },

          {
            text: this.$t("message.lastOneDay"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneWeek"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneMonth"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThreeMonths"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      tableData: [],
      usersData: [],
      logLevel: {
        INFO: this.$t("message.logLevel.info"),
        WARNING: this.$t("message.logLevel.warning"),
        CRITICAL: this.$t("message.logLevel.critical"),
        ERROR: this.$t("message.logLevel.error"),
      },
      queryData: {
        page_size: 10,
        page_num: 1,
        user_id: "",
        time: null,
        level: "",
        service: "",
        resource_id: "",
      },
      serviceData: [],
      totalNum: 0,
    };
  },
  watch: {},
  async created() {
    this.init();
    this.usersInit();
    this.initService();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    // 时间
    handleTimeChange(value) {
      value ? (this.queryData.time = value) : (this.queryData.time = "");
      this.init();
    },
    handleUserChange(value) {
      this.queryData.user_id = value;
      this.init();
    },
    handleServiceChange(value) {
      this.queryData.service = value;
      this.init();
    },
    searchinit() {
      this.queryData = {
        page_size: 10,
        page_num: 1,
        service: "",
        resource_id: "",
        user_id: "",
        time: null,
      };
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
      var data = {
        page_size: this.queryData.page_size,
        page_num: this.queryData.page_num,
        user_id: this.queryData.user_id,
        start_date:
          this.queryData.time && this.queryData.time.length > 0
            ? this.formatDate(this.queryData.time[0])
            : "",
        end_date:
          this.queryData.time && this.queryData.time.length > 0
            ? this.formatDate(this.queryData.time[1])
            : "",
        resource_id: this.queryData.resource_id,
        service: this.queryData.service,
      };
      const list = await getApiLogs(data);
      this.tableData = list.logs;
      this.totalNum = list.total_num;
    },
    async initService() {
      const list = await getService();
      this.serviceData = list.services;
    },
    async usersInit() {
      const listUsers = await getUsers();
      this.usersData = listUsers.users;
    },
    // 时间戳转换时间
    formatDate(date) {
      var date = new Date(date);
      var YY = date.getFullYear() + "-";
      var MM =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hh =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var mm =
        (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
        ":";
      var ss =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return YY + MM + DD + " " + hh + mm + ss;
    },
  },
};
</script>

<style lang="scss" scoped></style>
