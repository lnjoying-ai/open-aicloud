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
        :label="$t('form.eventSource')"
        min-width="120"
      >
        <template slot-scope="scope">
          <span>
            {{ scope.row.resource ? scope.row.resource : "-" }}
            :
            {{ scope.row.service ? scope.row.service : "-" }}
          </span>

          <el-popover
            :ref="`node-popover-${scope.row.id}`"
            placement="top-start"
            title=""
            width="500"
            popper-class="tablepopover"
            trigger="hover"
          >
            <p>
              <span v-if="getDescription(scope.row.description).status">
                <el-table
                  :data="getDescription(scope.row.description).data"
                  stripe
                  size="mini"
                  max-height="200"
                  style="width: 100%"
                >
                  <el-table-column :label="$t('form.changeContent')">
                    <template slot-scope="scope">
                      <div class="leading-tight">
                        {{ scope.row.key }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.changeBefore')">
                    <template slot-scope="scope">
                      <div class="leading-tight">
                        {{
                          scope.row.value.oldValue
                            ? scope.row.value.oldValue
                            : "-"
                        }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    :label="$t('form.changeBeforeDescription')"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div class="leading-tight">
                        {{
                          scope.row.value.oldValueCnDescription
                            ? scope.row.value.oldValueCnDescription
                            : "-"
                        }}
                      </div>
                      <div class="leading-tight">
                        {{
                          scope.row.value.oldValueEnDescription
                            ? scope.row.value.oldValueEnDescription
                            : "-"
                        }}
                      </div>
                    </template>
                  </el-table-column>

                  <el-table-column :label="$t('form.changeAfter')">
                    <template slot-scope="scope">
                      <div class="leading-tight">
                        {{ scope.row.value.newValue }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column
                    :label="$t('form.changeAfterDescription')"
                    show-overflow-tooltip
                  >
                    <template slot-scope="scope">
                      <div class="leading-tight">
                        {{
                          scope.row.value.newValueCnDescription
                            ? scope.row.value.newValueCnDescription
                            : "-"
                        }}
                      </div>
                      <div class="leading-tight">
                        {{
                          scope.row.value.newValueEnDescription
                            ? scope.row.value.newValueEnDescription
                            : "-"
                        }}
                      </div>
                    </template>
                  </el-table-column>
                </el-table>
              </span>
              <span v-else>
                <span>{{ $t("form.changeContent") }}：</span
                >{{ getDescription(scope.row.description).data }}
              </span>
            </p>
            <span slot="reference"> <i class="el-icon-info" /></span>
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column
        prop="resource_inst_name"
        :label="$t('form.resourceName')"
        min-width="200"
      >
        <template slot-scope="scope">
          {{
            scope.row.resource_inst_name ? scope.row.resource_inst_name : "-"
          }}
          <span class="block leading-none new-small-size"
            >ID:{{ scope.row.resource_id }}</span
          >
        </template>
      </el-table-column>
      <el-table-column
        prop="friendly_description"
        :label="$t('form.eventDetail')"
        min-width="380"
      >
        <template slot-scope="scope">
          {{
            scope.row.friendly_description
              ? scope.row.friendly_description
              : "-"
          }}
        </template>
      </el-table-column>
      <el-table-column prop="type" :label="$t('form.type')">
        <template slot-scope="scope">
          {{ scope.row.type ? scope.row.type : "-" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="trigger_time"
        :label="$t('form.triggerTime')"
        sortable
        width="220"
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
import { getApiEvents, getUsers } from "@/api/mainApi";
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
      eventLevel: {
        INFO: this.$t("message.eventLevel.info"),
        WARNING: this.$t("message.eventLevel.warning"),
        CRITICAL: this.$t("message.eventLevel.critical"),
      },
      queryData: {
        page_size: 10,
        page_num: 1,
        service: "",
        resource_id: "",
        user_id: "",
        time: null,
      },
      totalNum: 0,
      serviceData: [],
    };
  },
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
    async usersInit() {
      const listUsers = await getUsers();
      this.usersData = listUsers.users;
    },
    handleUserChange(value) {
      this.queryData.user_id = value;
      this.init();
    },
    handleServiceChange(value) {
      this.queryData.service = value;
      this.init();
    },
    handleLevelChange(value) {
      this.queryData.level = value;
      this.init();
    },
    // 时间
    handleTimeChange(value) {
      value ? (this.queryData.time = value) : (this.queryData.time = "");
      this.init();
    },
    searchinit() {
      this.queryData = {
        page_size: 10,
        page_num: 1,
        resource_id: "",
        user_id: "",
        time: null,
        service: "",
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
      // for (var key in this.queryData) {
      //   if (this.queryData[key] === undefined || this.queryData[key] === "") {
      //     delete this.queryData[key];
      //   }
      // }
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
      const list = await getApiEvents(data);
      this.tableData = list.events;
      this.totalNum = list.total_num;
    },
    async initService() {
      const list = await getService();
      this.serviceData = list.services;
    },
    getDescription(description) {
      // 获取变动内容 格式化
      if (!description) {
        return {
          status: false,
          data: "-",
        };
      }
      try {
        var data = JSON.parse(description);
        var dataInfo = {
          status: true,
          data: Object.keys(data).map((key) => {
            return {
              key: key,
              value: data[key],
            };
          }),
        };
        return dataInfo;
      } catch (e) {
        var dataInfo = {
          status: false,
          data: description,
        };
        return dataInfo;
      }
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
