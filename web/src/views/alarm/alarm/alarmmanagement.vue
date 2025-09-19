<template>
  <div class="warpMain">
    <el-tabs v-model="activeName">
      <el-tab-pane :label="$t('form.alarm')" name="first">
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
                  v-for="(item, index) in alarmGroupList"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <div slot="label">{{ $t("form.ruleType") + ":" }}</div>
              <el-select
                v-model="queryData.rule_type"
                :placeholder="$t('form.pleaseSelectRuleType')"
                @change="filterChange()"
              >
                <el-option
                  v-for="item in rule_type_list"
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
              @click="pushaddalarm"
              >{{ $t("form.create") }}
            </el-button>
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
                :to="'/alarmdetails/' + scope.row.id"
              >
                {{ scope.row.name ? scope.row.name : "-" }}
              </router-link>
            </template>
          </el-table-column>
          <el-table-column prop="alert_group_name" :label="$t('form.grouping')">
            <template slot-scope="scope">
              {{
                scope.row.alert_group_name ? scope.row.alert_group_name : "-"
              }}
            </template>
          </el-table-column>
          <el-table-column prop="alert_metric_name" :label="$t('form.metric')">
            <template slot-scope="scope">
              {{
                scope.row.alert_metric_name ? scope.row.alert_metric_name : "-"
              }}
            </template>
          </el-table-column>
          <el-table-column
            prop="description"
            :label="$t('form.description')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              {{ scope.row.description ? scope.row.description : "-" }}
            </template>
          </el-table-column>

          <el-table-column prop="status" :label="$t('form.alarmStatus')">
            <template slot-scope="scope">
              <span v-if="scope.row.status == 1">
                {{ $t("form.normal") }}
              </span>
              <span v-if="scope.row.status == 2">
                {{ $t("form.enabled") }}
              </span>
              <span v-if="scope.row.status == 3">
                {{ $t("form.deactivated") }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="alert_level" :label="$t('form.alarmLevel')">
            <template slot-scope="scope">
              <span v-if="scope.row.level == 1">
                <el-tag size="small">{{ $t("form.tip") }}</el-tag>
              </span>
              <span v-if="scope.row.level == 2">
                <el-tag type="warning" size="small">{{
                  $t("form.serious")
                }}</el-tag>
              </span>
              <span v-if="scope.row.level == 3">
                <el-tag type="danger" size="small">{{
                  $t("form.urgent")
                }}</el-tag>
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="alert_message"
            :label="$t('form.alarmContent')"
          >
            <template slot-scope="scope">
              {{ scope.row.alert_message ? scope.row.alert_message : "-" }}
            </template>
          </el-table-column>
          <el-table-column prop="target_type" :label="$t('form.targetType')">
            <template slot-scope="scope">
              {{
                scope.row.target_resource_type
                  ? scope.row.target_resource_type
                  : "-"
              }}
            </template>
          </el-table-column>

          <el-table-column :label="$t('form.operation')" width="100">
            <template slot-scope="scope">
              <div class="czlb">
                <el-popover placement="bottom" width="110" trigger="click">
                  <div class="icon_cz" @click="clickEditBtn(scope.row)">
                    <i class="el-icon-edit" />
                    {{ $t("form.edit") }}
                  </div>
                  <div class="icon_cz" @click="clickDelBtn(scope.row)">
                    <i class="el-icon-delete" />
                    {{ $t("form.delete") }}
                  </div>
                  <div
                    v-if="scope.row.status == 1 || scope.row.status == 2"
                    class="icon_cz"
                    @click="toDeactive(scope.row)"
                  >
                    <i class="el-icon-lock" />
                    {{ $t("form.deactivate") }}
                  </div>
                  <div
                    v-if="scope.row.status == 3"
                    class="icon_cz"
                    @click="toActive(scope.row)"
                  >
                    <i class="el-icon-unlock" />
                    {{ $t("form.enable") }}
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
            :total="totalNum"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </el-tab-pane>
      <el-tab-pane :label="$t('form.noticeObject')" name="second">
        <div>
          <el-form
            :inline="true"
            size="small"
            :model="queryData_notice"
            class="demo-form-inline"
          >
            <el-form-item>
              <div slot="label">{{ $t("form.name") + ":" }}</div>
              <el-input
                v-model="queryData_notice.name"
                :placeholder="$t('form.pleaseInputName')"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                icon="el-icon-search"
                class="add_search"
                type="primary"
                @click="handleCurrentChange_notice(1)"
                >{{ $t("form.query") }}
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-button class="add_search" @click="searchinit_notice()">
                {{ $t("form.reset") }}
              </el-button>
            </el-form-item>
            <el-button
              style="float: right; margin-bottom: 20px; display: inline-block"
              size="small"
              type="primary"
              class="ml-3"
              @click="pushnoticeadd"
              >{{ $t("form.create") }}
            </el-button>
          </el-form>
        </div>
        <el-table
          ref="multipleTable"
          :data="tableData_notice"
          stripe
          tooltip-effect="dark"
          style="width: 100%"
          :default-sort="{ prop: 'date', order: 'descending' }"
        >
          <el-table-column prop="name" :label="$t('form.name')">
            <template slot-scope="scope">
              {{ scope.row.name ? scope.row.name : "-" }}
            </template>
          </el-table-column>
          <el-table-column prop="description" :label="$t('form.description')">
            <template slot-scope="scope">
              {{ scope.row.description ? scope.row.description : "-" }}
            </template>
          </el-table-column>
          <el-table-column prop="email" :label="$t('form.email')">
            <template slot-scope="scope">
              {{ scope.row.email ? scope.row.email : "-" }}
            </template>
          </el-table-column>
          <el-table-column prop="sms" :label="$t('form.phone')">
            <template slot-scope="scope">
              {{ scope.row.phone ? scope.row.phone : "-" }}
            </template>
          </el-table-column>
          <!-- <el-table-column prop="bp_name"
                           label="组织机构">
            <template slot-scope="scope">
              {{ scope.row.bp_name ? scope.row.bp_name : "-" }}
            </template>
          </el-table-column> -->
          <el-table-column prop="user_name" :label="$t('form.user')">
            <template slot-scope="scope">
              {{ scope.row.iam_user_name ? scope.row.iam_user_name : "-" }}
            </template>
          </el-table-column>
          <el-table-column prop="update_time" :label="$t('form.updateTime')">
            <template slot-scope="scope">
              {{ scope.row.update_time ? scope.row.update_time : "-" }}
            </template>
          </el-table-column>
          <el-table-column :label="$t('form.operation')" width="100">
            <template slot-scope="scope">
              <div class="czlb">
                <el-popover placement="bottom" width="110" trigger="click">
                  <div class="icon_cz" @click="clickEditnoticeBtn(scope.row)">
                    <i class="el-icon-edit" />
                    {{ $t("form.edit") }}
                  </div>
                  <div class="icon_cz" @click="clickDelnoticeBtn(scope.row)">
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
            :current-page="queryData_notice.page_num"
            :page-sizes="[5, 10, 20, 50, 100]"
            :page-size="queryData_notice.page_size"
            layout="total, sizes, prev, pager, next, jumper"
            :total="notice_totalNum"
            @size-change="handleSizeChange_notice"
            @current-change="handleCurrentChange_notice"
          />
        </div>
      </el-tab-pane>
    </el-tabs>
    <Noticeadd ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getAlarmGroups,
  getAlarms,
  activeAlarms,
  deactiveAlarms,
  delAlarms,
  getReceivers,
  delReceivers,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import Noticeadd from "./noticeadd.vue";
export default {
  components: {
    Noticeadd,
  },
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      activeInfo: {},
      alarmGroupList: [], // 报警分组
      // 报警器的总数据
      totalNum: 0,
      // 通知对象的总数据
      notice_totalNum: 0,
      rule_type_list: [
        {
          value: 1,
          label: this.$t("form.staticThreshold"),
        },
        {
          value: 2,
          label: this.$t("form.customPromQL"),
        },
      ],
      activeName: "first",
      // 报警器列表
      tableData: [],
      // 报警器搜索的关键词
      queryData: {
        page_size: 10,
        page_num: 1,
        name: "",
        rule_type: "",
        group_id: "",
        totalNum: 0,
      },
      // 通知对象
      tableData_notice: [],
      // 通知对象搜索的关键词
      queryData_notice: {
        page_size: 10,
        page_num: 1,
        name: "",
        totalNum: 0,
      },
    };
  },
  async created() {
    this.init();
    this.init_notice();
    this.getAlarmGroupList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  methods: {
    async getAlarmGroupList() {
      // 获取报警分组
      const alarmGroupList = await getAlarmGroups();
      this.alarmGroupList = alarmGroupList.groups;
    },
    filterChange() {
      this.queryData.page_num = 1;
      this.init();
    },
    searchinit() {
      // 重置
      this.queryData = {
        page_size: 10,
        page_num: 1,
        name: "",
        rule_type: "",
        group_id: "",
        totalNum: 0,
      };
      this.init();
    },
    pushnoticeadd() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
    },
    pushaddalarm() {
      this.$router.push("/addalarm");
    },
    searchinit_notice() {
      this.queryData_notice = {
        page_size: 10,
        page_num: 1,
        name: "",
        totalNum: 0,
      };
      this.init_notice();
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    handleSizeChange_notice(val) {
      this.queryData_notice.page_size = val;
      this.init_notice();
    },
    handleCurrentChange_notice(val) {
      this.queryData_notice.page_num = val;
      this.init_notice();
    },
    // 获取报警器列表
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getAlarms(this.queryData);
      this.tableData = list.alarms;
      this.totalNum = list.total_num;
    },
    // 获取通知对象接口
    async init_notice() {
      for (var key in this.queryData_notice) {
        if (
          this.queryData_notice[key] === undefined ||
          this.queryData_notice[key] === ""
        ) {
          delete this.queryData_notice[key];
        }
      }
      delete this.queryData_notice.totalNum;
      const list = await getReceivers(this.queryData_notice);
      this.tableData_notice = list.receivers;
      this.notice_totalNum = list.total_num;
    },
    management() {
      this.$router.push("/alarmmanagement");
    },
    // 停用
    toDeactive(value) {
      this.$confirm(
        this.$t("message.confirmDisableAlarm", { name: value.name }),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          deactiveAlarms(value.id)
            .then((res) => {
              this.$notify({
                type: "success",
                title: this.$t("message.alarmDisabled", { name: value.name }),
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    // 启用
    toActive(value) {
      this.$confirm(
        this.$t("message.confirmEnableAlarm", { name: value.name }),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          activeAlarms(value.id)
            .then((res) => {
              this.$notify({
                type: "success",
                title: this.$t("message.alarmEnabled", { name: value.name }),
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteAlarm"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delAlarms(value.id)
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
    // 通知对象的删除
    clickDelnoticeBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteData", { name: value.name }),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delReceivers(value.id)
            .then((res) => {
              if (res != undefined) {
                this.$notify({
                  type: "success",
                  title: this.$t("message.deleteSuccess"),
                  duration: 2500,
                });
              }
              this.init_notice();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickEditBtn(info) {
      this.$router.push(`/addalarm/${info.id}`);
    },
    clickEditnoticeBtn(info) {
      this.activeInfo = info;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped></style>
