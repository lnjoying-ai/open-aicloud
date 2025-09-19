<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item :label="$t('transfer.keyword') + ':'" label-width="80px">
          <el-input
            v-model="keywordValue"
            :placeholder="$t('transfer.pleaseEnter')"
            class="input-with-select"
          >
            <template slot="prepend">
              <el-select v-model="keywordKey" style="width: 110px">
                <el-option
                  class="selectStyle"
                  :label="$t('transfer.filePath')"
                  value="filePath"
                />
                <el-option
                  class="selectStyle"
                  :label="$t('transfer.client') + ' IP'"
                  value="transClientIp"
                />
                <el-option
                  class="selectStyle"
                  :label="$t('transfer.client') + ' ID'"
                  value="transClientNodeId"
                />
              </el-select>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("transfer.type") }}:</div>
          <el-select
            v-model="queryData.transType"
            :placeholder="$t('transfer.pleaseSelect')"
            clearable
            @change="handleTypeChange"
          >
            <el-option :value="0" :label="$t('transfer.upload')" />
            <el-option :value="1" :label="$t('transfer.downloads')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("transfer.status") }}:</div>
          <el-select
            v-model="queryData.transStatus"
            :placeholder="$t('transfer.pleaseSelect')"
            clearable
            @change="handleStatusChange"
          >
            <el-option :value="0" :label="$t('transfer.done')" />
            <el-option :value="1" :label="$t('transfer.paused')" />
            <el-option :value="2" :label="$t('transfer.transferring')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("transfer.timeRange") }}:</div>
          <el-date-picker
            v-model="timeFrame"
            type="daterange"
            :range-separator="$t('transfer.to')"
            :start-placeholder="$t('transfer.startDate')"
            :end-placeholder="$t('transfer.endDate')"
            value-format="yyyy-MM-dd"
            @change="handleTimeChange"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="handleCurrentChange(1)"
            >{{ $t("transfer.query") }}</el-button
          >
          <el-button class="add_search" @click="searchinit()">{{
            $t("transfer.reset")
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
      <el-table-column
        type="index"
        width="50"
        align="center"
        :lable="$t('transfer.serialNo')"
      >
        <template slot="header">
          <span>{{ $t("transfer.serialNo") }}</span>
        </template>
        <template slot-scope="scope">
          <span>{{ scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="transClientNodeId"
        :label="$t('transfer.client') + 'ID'"
        align="center"
        width="180px"
      >
        <template slot-scope="scope">
          <el-tooltip
            class="item"
            effect="dark"
            :content="scope.row.transClientNodeId"
            placement="top-start"
          >
            <span>···{{ scope.row.transClientNodeId.slice(24, 37) }}</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="transType" :label="$t('transfer.type')">
        <template slot-scope="scope">
          <span v-if="scope.row.transType == 1" style="color: #096dd9">
            <img
              src="@/assets/transfer/down.png"
              alt=""
              style="width: 16px; vertical-align: middle"
            />
            {{ $t("transfer.downloads") }}
          </span>
          <span v-else style="color: #91d5ff">
            <img
              src="@/assets/transfer/up.png"
              alt=""
              style="width: 16px; vertical-align: middle"
            />
            {{ $t("transfer.upload") }}
          </span>
        </template>
      </el-table-column>

      <el-table-column prop="filePath" :label="$t('transfer.fileName')">
        <template slot-scope="scope">
          <div class="overflow-ellipsis">
            <el-tooltip
              class="box-item"
              effect="dark"
              :content="scope.row.filePath"
              placement="top-start"
              :open-delay="1000"
            >
              <template #content>
                <span class="displayB"
                  >{{ $t("transfer.fileName") }}：{{
                    scope.row.filePath == null
                      ? "-"
                      : scope.row.filePath.substring(
                          scope.row.filePath.lastIndexOf("/") + 1,
                          scope.row.filePath.length
                        )
                  }}
                </span>
                <span class="displayB"
                  >{{ $t("transfer.filePath") }}：{{
                    scope.row.filePath == null ? "-" : scope.row.filePath
                  }}
                  <i
                    class="iconfont icon-fuzhi"
                    style="cursor: pointer"
                    @click.stop="copy(scope.row.filePath)"
                  />
                </span>
              </template>
              <div class="overflow-ellipsis" style="cursor: pointer">
                <span>{{
                  scope.row.filePath == null
                    ? "-"
                    : scope.row.filePath.substring(
                        scope.row.filePath.lastIndexOf("/") + 1,
                        scope.row.filePath.length
                      )
                }}</span>
              </div>
            </el-tooltip>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="tranSize" :label="$t('transfer.taskFileSize')">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.tranSize ? getfilesize(scope.row.tranSize) : "0"
            }}</span
          >
        </template>
      </el-table-column>
      <el-table-column prop="transStatus" :label="$t('transfer.status')">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.transStatus == 1
                ? $t("transfer.paused")
                : scope.row.transStatus == 2
                ? $t("transfer.transferring")
                : $t("transfer.done")
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="downloadName" :label="$t('transfer.userName')">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.userName
                ? scope.row.userName == "13000000000"
                  ? $t("transfer.anonymousUser")
                  : scope.row.userName
                : "-"
            }}</span
          >
        </template>
      </el-table-column>
      <el-table-column prop="transClientIp" :label="$t('transfer.clientIp')">
        <template slot-scope="scope">
          <span> {{ scope.row.transClientIp || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('transfer.location')">
        <template slot-scope="scope">
          <span>
            {{ scope.row.country }}
            {{ scope.row.city ? "/" + scope.row.city : "" }}</span
          >
        </template>
      </el-table-column>
      <el-table-column prop="createTimestamp" :label="$t('transfer.taskTime')">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.createTimestamp
                ? formatDate(scope.row.createTimestamp)
                : "-"
            }}</span
          >
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.pageNumber"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="queryData.pageSize"
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
  TransStatisticsQueryInfo,
  transmissionLogin,
  transmissionUserInfo,
  transmissionFsStore,
} from "@/api/transfer/mainApi";
import Cookies from "js-cookie";
export default {
  components: {},
  data() {
    return {
      keywordKey: "transClientIp",
      keywordValue: "",
      queryData: {
        transType: "",
        transStatus: "",
        startTime: "",
        endTime: "",
        pageSize: 10,
        pageNumber: 1,
      },
      // 时间范围
      timeFrame: [],
      // 表格总数
      tableDataTotal: 0,
      // 表格数据
      tableData: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  created() {
    if (localStorage.getItem("transmissionUserInfo")) {
      this.init();
    } else {
      this.isLogin();
    }
  },
  mounted() {},
  methods: {
    // 登录
    async isLogin() {
      await transmissionLogin(Cookies.get("Access-Token"))
        .then((res) => {
          this.getInfoList();
          this.gettransInfo();
          this.init(); // 请求文件列表
        })
        .catch((error) => {});
    },
    async getInfoList() {
      await transmissionUserInfo()
        .then((res) => {
          localStorage.setItem("transmissionUserInfo", JSON.stringify(res));
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    async gettransInfo() {
      await transmissionFsStore("CPU")
        .then((res) => {
          localStorage.setItem("transmissionInfo", JSON.stringify(res));
        })
        .catch((err) => {
          console.error(err.response.data.message);
        });
    },
    // 复制
    copy(value) {
      const textarea = document.createElement("textarea");
      textarea.value = value;
      document.body.appendChild(textarea);
      textarea.select();
      const isCopy = document.execCommand("copy"); // isCopy可以表示是否调用execCommand()成功
      document.body.removeChild(textarea);
      ElMessage({
        message: this.$t("transfer.copySuccess"),
        type: "success",
        duration: 2000,
      });
    },
    // 状态选择触发
    handleStatusChange(value) {
      this.queryData.pageNumber = 1;
      this.queryData.transStatus = value;
      this.init();
    },
    // 类型选择触发
    handleTypeChange(value) {
      this.queryData.pageNumber = 1;
      this.queryData.transType = value;
      this.init();
    },
    // 时间选择触发
    handleTimeChange(value) {
      this.queryData.pageNumber = 1;
      if (value) {
        this.queryData.startTime = value[0];
        this.queryData.endTime = value[1];
      } else {
        this.queryData.startTime = "";
        this.queryData.endTime = "";
      }
      this.init();
    },
    // 文件大小
    getfilesize(fileByte) {
      // 文件大小单位进位
      if (fileByte == 0) {
        return 0;
      }
      var fileSizeByte = (fileByte + "").replace(new RegExp(",", "g"), "") * 1;
      var fileSizeMsg = "";
      if (fileSizeByte < 1024) {
        fileSizeByte = Number(fileSizeByte);
        fileSizeMsg = this.$script.processing_numbers(fileSizeByte) + "B";
      } else if (fileSizeByte == 1024) {
        fileSizeMsg = "1.00KB";
      } else if (fileSizeByte > 1024 && fileSizeByte < 1048576) {
        fileSizeMsg =
          this.$script.processing_numbers(fileSizeByte / 1024) + "KB";
      } else if (fileSizeByte == 1048576) {
        fileSizeMsg = "1.00MB";
      } else if (fileSizeByte > 1048576 && fileSizeByte < 1073741824) {
        fileSizeMsg =
          this.$script.processing_numbers(fileSizeByte / (1024 * 1024)) + "MB";
      } else if (fileSizeByte > 1048576 && fileSizeByte == 1073741824) {
        fileSizeMsg = "1.00GB";
      } else if (fileSizeByte > 1073741824 && fileSizeByte < 1099511627776) {
        this.$script.processing_numbers(fileSizeByte / (1024 * 1024 * 1024)) +
          "GB";
      } else {
        fileSizeMsg =
          this.$script.processing_numbers(
            fileSizeByte / (1024 * 1024 * 1024 * 1024)
          ) + "TB";
      }

      return fileSizeMsg;
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
    async init() {
      var data = {
        transType: this.queryData.transType,
        transStatus: this.queryData.transStatus,
        startTime: this.queryData.startTime,
        endTime: this.queryData.endTime,
        pageNumber: this.queryData.pageNumber,
        pageSize: this.queryData.pageSize,
      };
      data[this.keywordKey] = this.keywordValue;
      for (const key in data) {
        if (data[key] == null || data[key] == undefined || data[key] === "") {
          delete data[key];
        }
      }
      const list = await TransStatisticsQueryInfo(data);
      if (list.obj != null) {
        this.tableData = list.obj.transStatisticsInfos;
        this.tableDataTotal = list.obj.total;
      } else {
        this.tableData = [];
        this.tableDataTotal = 0;
      }
    },
    handleSizeChange(val) {
      this.queryData.pageSize = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.pageNumber = val;
      this.init();
    },
    searchinit() {
      this.keywordKey = "transClientIp";
      this.keywordValue = "";
      this.timeFrame = [];
      this.queryData = {
        transType: "",
        transStatus: "",
        startTime: "",
        endTime: "",
        pageSize: 10,
        pageNumber: 1,
      };
      this.init();
    },
  },
};
</script>

<style lang="scss" scoped>
img,
video {
  display: inline-block;
}
</style>
