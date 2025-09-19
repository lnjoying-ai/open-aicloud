<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.addNoticeObject') : $t('form.editNoticeObject')"
      width="1000px"
      :close-on-click-modal="false"
      :before-close="cancel"
      @open="onOpen"
    >
      <el-table
        ref="multipleTable"
        :data="tableData_notice"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
        :row-key="getRowKeys"
        :default-selection="defaultSelection"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="55"
          :reserve-selection="true"
        />
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
      </el-table>
      <div class="mt-3">
        {{ $t("form.selected") }}:
        <el-tag
          v-for="(items, index) in multipleSelection"
          :key="items.id"
          class="ml-2"
          type="info"
          size="mini"
          closable
          @close="handleCloseTags(index)"
        >
          <span>{{ items.name }}</span></el-tag
        >
      </div>
      <div class="flex justify-end mt-1 px-4">
        <el-pagination
          :current-page="queryData.page_num"
          :page-sizes="[5, 10, 20, 50, 100]"
          :page-size="queryData.page_size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="queryData.totalNum"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getReceivers } from "@/api/mainApi";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Array,
  },
  data() {
    return {
      id: "",
      isAdd: true,
      dialog: false,
      loading: false,
      multipleSelection: [],
      tableData_notice: [],
      defaultSelection: [],
      form: {},
      fromdata: {},
      queryData: {
        page_size: 10,
        page_num: 1,
        totalNum: 0,
      },
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  created() {},
  mounted() {},
  methods: {
    selectedit(data) {
      this.$nextTick(() => {
        data.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row, true);
        });
      });
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init_notice();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init_notice();
    },
    resetForm() {
      // this.$refs["securityGroupsForm"].resetFields();
      this.securityGroupsForm = {};
      this.$nextTick(() => {
        this.$refs.multipleTable.clearSelection();
      });
    },
    cancel() {
      this.dialog = false;
      this.$nextTick(() => {
        this.$refs.multipleTable.clearSelection();
      });
      // this.$refs["securityGroupsForm"].resetFields();
    },
    createSecurityGroups() {
      this.loading = true;
      this.loading = false;
    },
    updateSecurityGroups() {
      this.loading = true;
      this.loading = false;
    },
    doSubmit() {
      this.$emit("child-msg", this.multipleSelection);
      this.dialog = false;
    },
    resetFrom() {
      // this.$refs["securityGroupsForm"].resetFields();
    },
    // 获取对象的接口
    async init_notice() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getReceivers(this.queryData);
      this.tableData_notice = list.receivers;
      this.queryData.totalNum = list.total_num;
    },
    async onOpen() {
      this.init_notice();
    },
    // 设置keys
    getRowKeys(row) {
      return row.id;
    },
    // 选择的通知对象
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    // 点击标签的关闭
    handleCloseTags(index) {
      this.$refs.multipleTable.selection.forEach((element) => {
        if (element.id === this.multipleSelection[index].id) {
          this.$refs.multipleTable.toggleRowSelection(element, false);
        } else {
          this.$refs.multipleTable.toggleRowSelection(element, true);
        }
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
