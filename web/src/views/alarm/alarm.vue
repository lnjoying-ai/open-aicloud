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
          <div slot="label">{{ $t("form.grouping") + ":" }}</div>
          <el-select
            v-model="queryData.group_id"
            :placeholder="$t('form.pleaseSelectGrouping')"
            @change="filterChange()"
          >
            <el-option
              v-for="item in group_id_list"
              :key="item.value"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.status") + ":" }}</div>
          <el-select
            v-model="queryData.alert_status"
            :placeholder="$t('form.pleaseSelectStatus')"
            @change="filterChange()"
          >
            <el-option
              v-for="item in alert_status_list"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <div slot="label">{{ $t("form.alarmLevel") + ":" }}</div>
          <el-select
            v-model="queryData.level"
            :placeholder="$t('form.pleaseSelectAlarmLevel')"
            @change="filterChange()"
          >
            <el-option
              v-for="item in level_list"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
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
        <el-button
          style="float: right; margin-bottom: 20px; display: inline-block"
          size="small"
          type="primary"
          class="ml-3"
          @click="management"
          >{{ $t("form.alarmManager") }}</el-button
        >
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      v-loading="loading"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
      :element-loading-text="$t('domain.loading')"
      @selection-change="handleSelectionChange"
    >
      <!-- <el-table-column type="selection"
                       width="55"> </el-table-column> -->
      <el-table-column prop="alarm_id" width="240" :label="$t('form.idName')">
        <template slot-scope="scope">
          <div>
            {{ scope.row.alarm_name ? scope.row.alarm_name : "-" }}
            <el-popover
              placement="top-start"
              title=""
              width="300"
              popper-class="tablepopover"
              trigger="hover"
            >
              {{ scope.row.description ? scope.row.description : "-" }}
              <span slot="reference"><i class="el-icon-info" /> </span>
            </el-popover>
          </div>
          <div class="new-small-size">ID:{{ scope.row.id }}</div>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="alert_type" label="报警类型">
        <template slot-scope="scope">
          <span v-if="scope.row.alert_type == 1">事件</span>
          <span v-if="scope.row.alert_type == 2">监控</span>
        </template>
      </el-table-column> -->
      <el-table-column
        prop="alert_status"
        width="100"
        :label="$t('form.alarmStatus')"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.alert_status == 1">{{
            $t("form.trigger")
          }}</span>
          <span v-if="scope.row.alert_status == 2">{{
            $t("form.resolved")
          }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="level" width="100" :label="$t('form.alarmLevel')">
        <template slot-scope="scope">
          <!-- {{ scope.row.level ? scope.row.level : "-" }} -->
          <span v-if="scope.row.level == 1">
            <el-tag size="small">{{ $t("form.prompt") }}</el-tag>
          </span>
          <span v-if="scope.row.level == 2">
            <el-tag type="warning" size="small">{{
              $t("form.serious")
            }}</el-tag>
          </span>
          <span v-if="scope.row.level == 3">
            <el-tag type="danger" size="small">{{ $t("form.urgent") }}</el-tag>
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="notify_channels"
        width="100"
        :label="$t('form.notifyObject')"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.notify_objects">
            <span v-if="scope.row.notify_objects.length > 0">
              <span>{{ scope.row.notify_objects.length }}</span>
              <el-popover
                :ref="`node-popover-${scope.row.id}`"
                placement="top-start"
                title=""
                width="400"
                popper-class="tablepopover"
                trigger="hover"
              >
                <el-table
                  :data="scope.row.notify_objects"
                  stripe
                  max-height="300"
                  size="mini"
                  style="width: 100%"
                >
                  <el-table-column :label="$t('form.notifyObject')">
                    <template slot-scope="scope">
                      {{ scope.row.receiver_name }}
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.email')">
                    <template slot-scope="scope">
                      <!-- 是否包含字符串 0 -->
                      <!-- {{ scope.row.notify_channels.includes("0") ? '是' : "否" }} -->
                      <div v-if="scope.row.notify_channels.includes('0')">
                        <svg
                          t="1703646706739"
                          class="icon"
                          viewBox="0 0 1024 1024"
                          version="1.1"
                          xmlns="http://www.w3.org/2000/svg"
                          p-id="9755"
                          width="14"
                          height="14"
                        >
                          <path
                            d="M512 0C228.430769 0 0 228.430769 0 512s228.430769 512 512 512 512-228.430769 512-512S795.569231 0 512 0z m256 413.538462l-271.753846 271.753846c-7.876923 7.876923-19.692308 11.815385-31.507692 11.815384-11.815385 0-23.630769-3.938462-31.507693-11.815384l-169.353846-169.353846c-15.753846-15.753846-15.753846-47.261538 0-63.015385 15.753846-15.753846 47.261538-15.753846 63.015385 0l137.846154 137.846154 240.246153-240.246154c15.753846-15.753846 47.261538-15.753846 63.015385 0 19.692308 15.753846 19.692308 47.261538 0 63.015385z"
                            fill="#94C86C"
                            p-id="9756"
                          />
                        </svg>
                      </div>
                      <div v-else>
                        {{ "-" }}
                      </div>
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.phone')">
                    <template slot-scope="scope">
                      <div v-if="scope.row.notify_channels.includes('1')">
                        <svg
                          t="1703646706739"
                          class="icon"
                          viewBox="0 0 1024 1024"
                          version="1.1"
                          xmlns="http://www.w3.org/2000/svg"
                          p-id="9755"
                          width="14"
                          height="14"
                        >
                          <path
                            d="M512 0C228.430769 0 0 228.430769 0 512s228.430769 512 512 512 512-228.430769 512-512S795.569231 0 512 0z m256 413.538462l-271.753846 271.753846c-7.876923 7.876923-19.692308 11.815385-31.507692 11.815384-11.815385 0-23.630769-3.938462-31.507693-11.815384l-169.353846-169.353846c-15.753846-15.753846-15.753846-47.261538 0-63.015385 15.753846-15.753846 47.261538-15.753846 63.015385 0l137.846154 137.846154 240.246153-240.246154c15.753846-15.753846 47.261538-15.753846 63.015385 0 19.692308 15.753846 19.692308 47.261538 0 63.015385z"
                            fill="#94C86C"
                            p-id="9756"
                          />
                        </svg>
                      </div>
                      <div v-else>
                        {{ "-" }}
                      </div>
                    </template>
                  </el-table-column>
                </el-table>

                <span slot="reference">
                  <!-- <span v-if="scope.row.notify_objects"> -->
                  <i class="el-icon-info" />
                  <!-- </span> -->
                </span>
              </el-popover>
            </span>
            <span v-else> {{ "不通知" }}</span>
          </span>
          <span v-else> {{ "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="description" :label="$t('form.alarmContent')">
        <template slot-scope="scope">
          <span v-if="scope.row.summary">
            <span
              v-if="
                JSON.parse(scope.row.labels).node_id &&
                scope.row.summary.indexOf(
                  JSON.parse(scope.row.labels).node_id
                ) != -1 &&
                JSON.parse(scope.row.labels).container_ref_id &&
                scope.row.summary.indexOf(
                  JSON.parse(scope.row.labels).container_ref_id
                ) != -1
              "
            >
              <span
                v-if="
                  scope.row.summary
                    .split(JSON.parse(scope.row.labels).node_id)[0]
                    .indexOf(JSON.parse(scope.row.labels).container_ref_id) !=
                  -1
                "
              >
                <span>{{
                  scope.row.summary
                    .split(JSON.parse(scope.row.labels).node_id)[0]
                    .split(JSON.parse(scope.row.labels).container_ref_id)[0]
                }}</span>
                <span>
                  <router-link
                    style="color: #409eff"
                    :to="
                      '/containerApplicationService/deployContainDetail/' +
                      JSON.parse(scope.row.labels).container_ref_id
                    "
                  >
                    {{ JSON.parse(scope.row.labels).container_ref_id }}
                  </router-link>
                </span>
                <span>{{
                  scope.row.summary
                    .split(JSON.parse(scope.row.labels).node_id)[0]
                    .split(JSON.parse(scope.row.labels).container_ref_id)[1]
                }}</span>
              </span>
              <span v-else>{{
                scope.row.summary.split(JSON.parse(scope.row.labels).node_id)[0]
              }}</span>
              <span>
                <router-link
                  style="color: #409eff"
                  :to="
                    '/infrastructure/nodeList/' +
                    JSON.parse(scope.row.labels).node_id
                  "
                >
                  {{ JSON.parse(scope.row.labels).node_id }}
                </router-link>
              </span>
              <span
                v-if="
                  scope.row.summary
                    .split(JSON.parse(scope.row.labels).node_id)[1]
                    .indexOf(JSON.parse(scope.row.labels).container_ref_id) !=
                  -1
                "
              >
                <span>{{
                  scope.row.summary
                    .split(JSON.parse(scope.row.labels).node_id)[1]
                    .split(JSON.parse(scope.row.labels).container_ref_id)[0]
                }}</span>
                <span>
                  <router-link
                    style="color: #409eff"
                    :to="
                      '/containerApplicationService/deployContainDetail/' +
                      JSON.parse(scope.row.labels).container_ref_id
                    "
                  >
                    {{ JSON.parse(scope.row.labels).container_ref_id }}
                  </router-link>
                </span>
                <span>{{
                  scope.row.summary
                    .split(JSON.parse(scope.row.labels).node_id)[1]
                    .split(JSON.parse(scope.row.labels).container_ref_id)[1]
                }}</span>
              </span>
              <span v-else
                >{{
                  scope.row.summary.split(
                    JSON.parse(scope.row.labels).node_id
                  )[1]
                }}
              </span>
            </span>
            <span
              v-else-if="
                JSON.parse(scope.row.labels).container_ref_id &&
                scope.row.summary.indexOf(
                  JSON.parse(scope.row.labels).container_ref_id
                ) != -1 &&
                JSON.parse(scope.row.labels).node_id &&
                scope.row.summary.indexOf(
                  JSON.parse(scope.row.labels).node_id
                ) == -1
              "
            >
              <span
                >{{
                  scope.row.summary.split(
                    JSON.parse(scope.row.labels).container_ref_id
                  )[0]
                }}
              </span>
              <span>
                <router-link
                  style="color: #409eff"
                  :to="
                    '/containerApplicationService/deployContainDetail/' +
                    JSON.parse(scope.row.labels).container_ref_id
                  "
                >
                  {{ JSON.parse(scope.row.labels).container_ref_id }}
                </router-link>
              </span>
              <span
                >{{
                  scope.row.summary.split(
                    JSON.parse(scope.row.labels).container_ref_id
                  )[1]
                }}
              </span>
            </span>
            <span
              v-else-if="
                JSON.parse(scope.row.labels).container_ref_id &&
                scope.row.summary.indexOf(
                  JSON.parse(scope.row.labels).container_ref_id
                ) == -1 &&
                JSON.parse(scope.row.labels).node_id &&
                scope.row.summary.indexOf(
                  JSON.parse(scope.row.labels).node_id
                ) != -1
              "
            >
              <span
                >{{
                  scope.row.summary.split(
                    JSON.parse(scope.row.labels).node_id
                  )[0]
                }}
              </span>
              <span>
                <router-link
                  style="color: #409eff"
                  :to="
                    '/infrastructure/nodeList/' +
                    JSON.parse(scope.row.labels).node_id
                  "
                >
                  {{ JSON.parse(scope.row.labels).node_id }}
                </router-link>
              </span>
              <span
                >{{
                  scope.row.summary.split(
                    JSON.parse(scope.row.labels).node_id
                  )[1]
                }}
              </span>
            </span>
            <span v-else>
              <span>{{ scope.row.summary ? scope.row.summary : "-" }}</span>
            </span>
          </span>
          <span v-else>
            {{ "-" }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="process_status"
        align="center"
        :label="$t('form.handleStatus')"
      >
        <template slot-scope="scope">
          <span class="block">
            {{
              scope.row.alert_log_process_list
                ? scope.row.alert_log_process_list[0].update_time
                : ""
            }}
          </span>
          <span class="block">
            <small v-if="scope.row.alert_log_process_list">
              {{
                scope.row.alert_log_process_list[0].process_status == "read"
                  ? $t("form.readSuccess")
                  : $t("form.unreadSuccess")
              }}
            </small>
            <span v-else>{{ $t("form.unread") }}</span>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="update_time" :label="$t('form.updateTime')">
        <template slot-scope="scope">
          {{ scope.row.update_time ? formatDate(scope.row.update_time) : "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="
              scope.row.alert_log_process_list
                ? scope.row.alert_log_process_list[0].process_status == 'unread'
                : true
            "
            type="text"
            size="small"
            @click="markread(scope.row.id)"
            >{{ $t("form.markAsRead") }}</el-button
          >
          <el-button
            v-if="
              scope.row.alert_log_process_list
                ? scope.row.alert_log_process_list[0].process_status == 'read'
                : false
            "
            type="text"
            size="small"
            @click="marknoread(scope.row.id)"
            >{{ $t("form.markAsUnread") }}</el-button
          >
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
import { getAlertLogs, getAlarmGroups, readAlarmGroups } from "@/api/mainApi";
import initData from "@/mixins/initData";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      loading: false,
      alert_status_list: [
        {
          value: 1,
          label: this.$t("form.trigger"),
        },
        {
          value: 2,
          label: this.$t("form.resolved"),
        },
      ],
      level_list: [
        {
          value: 1,
          label: this.$t("form.prompt"),
        },
        {
          value: 2,
          label: this.$t("form.serious"),
        },
        {
          value: 3,
          label: this.$t("form.urgent"),
        },
      ],
      group_id_list: [], // 报警分组
      totalNum: 0,
      resource_type_id_list: [],
      sup_this: this,
      tableData: [],
      queryData: {
        page_size: 10,
        page_num: 1,
        alert_status: "",
        resource_id: "",
        level: "",
        group_id: "",
        resource_type_id: "",
      },
    };
  },
  async created() {
    this.init();
    this.getAlarmGroupList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    // 搜索选择框选择之后搜索
    filterChange() {
      this.queryData.page_num = 1;
      this.init();
    },
    // 标记已读
    markread(id) {
      this.$confirm(
        this.$t("message.confirmMarkAsRead"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          readAlarmGroups(id, {
            status: "read",
          })
            .then((res) => {
              this.$notify({
                type: "success",
                title: this.$t("message.markSuccess"),
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    // 标记未读
    marknoread(id) {
      this.$confirm(
        this.$t("message.confirmMarkAsUnread"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          readAlarmGroups(id, {
            status: "unread",
          })
            .then((res) => {
              this.$notify({
                type: "success",
                title: this.$t("message.markSuccess"),
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    async getAlarmGroupList() {
      // 获取报警分组
      const alarmGroupList = await getAlarmGroups();
      this.group_id_list = alarmGroupList.groups;
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
    searchinit() {
      (this.queryData = {
        page_size: 10,
        page_num: 1,
        alert_status: "",
        resource_id: "",
        level: "",
        group_id: "",
        resource_type_id: "",
        totalNum: 0,
      }),
        this.init();
    },
    // 多选选中的
    handleSelectionChange(val) {
      this.multipleSelection = val;
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
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      delete this.queryData["totalNum"];
      const list = await getAlertLogs(this.queryData);
      this.tableData = list.alert_logs;
      this.totalNum = list.total_num;
      this.loading = false;
    },
    management() {
      this.$router.push("/alarmmanagement");
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteRegion"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delRegions(value.id)
            .then((res) => {
              if (res != undefined) {
                this.$notify({
                  type: "success",
                  title: this.$t("message.deleteSuccess"),
                  duration: 2500,
                });
              }
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickEditBtn(info) {},
  },
};
</script>

<style lang="scss" scoped></style>
