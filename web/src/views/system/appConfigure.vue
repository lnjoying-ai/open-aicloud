<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-row :gutter="12">
          <el-col :span="24">
            <el-form-item>
              <div slot="label">{{ $t("form.name") }}:</div>
              <el-input
                v-model="queryData.data_id"
                :placeholder="$t('form.pleaseInput')"
              />
            </el-form-item>
            <el-form-item>
              <div slot="label">{{ $t("form.group") }}:</div>
              <el-input
                v-model="queryData.group"
                :placeholder="$t('form.pleaseInput')"
              />
            </el-form-item>
            <el-form-item>
              <el-button
                size="small"
                icon="el-icon-search"
                class="add_search"
                type="primary"
                @click="init()"
                >{{ $t("form.query") }}</el-button
              >
            </el-form-item>
            <el-form-item>
              <el-button size="small" class="add_search" @click="ResetData">{{
                $t("form.reset")
              }}</el-button>
            </el-form-item>
            <el-form-item style="float: right">
              <el-button type="primary" size="small" @click="clickAddBtn">{{
                $t("form.add")
              }}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.pageItems"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column
        prop="dataId"
        :label="$t('form.name')"
        show-overflow-tooltip
      />
      <el-table-column
        prop="group"
        :label="$t('form.group')"
        show-overflow-tooltip
      />
      <el-table-column
        prop="tenant_name"
        :label="$t('form.createUser')"
        show-overflow-tooltip
      />
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="clickEditBtn(scope.row)">
                <i class="el-icon-edit" />
                {{ $t("form.edit") }} / {{ $t("form.view") }}
              </div>
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
        :total="queryData.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { configs, delConfiguer } from "@/api/mainApi";
import initData from "@/mixins/initData";
import { mapGetters } from "vuex";
export default {
  mixins: [initData],
  data() {
    return {
      // 表格数据
      tableData: [],
      // 传参
      queryData: {
        page_size: 10,
        page_num: 1,
        data_id: "",
        group: "",
        total: 0,
      },
    };
  },
  watch: {},
  computed: {
    ...mapGetters(["kind"]),
  },
  created() {
    this.init();
  },
  mounted() {},

  methods: {
    // 表格数据
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await configs(this.queryData);
      this.tableData = list;
      this.queryData.total = list.totalCount;
    },
    // 新增
    clickAddBtn() {
      this.$router.push("/containerApplicationService/configureInformation");
    },
    // 编辑
    clickEditBtn(row) {
      this.$router.push({
        path: "/containerApplicationService/configureInformation",
        query: { id: row.dataId },
      });
    },
    // 删除
    clickDelBtn(value) {
      this.$confirm(
        this.$t("form.confirmDeleteAppConfigure"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delConfiguer({
            namespace: value.tenant,
            data_id: value.dataId,
            group: value.group,
          })
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    // 重置
    ResetData() {
      this.queryData = {
        dataId: "",
        group: "",
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
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-table .disabledCheck .cell .el-checkbox__inner {
  display: none !important;
}

::v-deep .el-table__expanded-cell {
  background: #f5f5f5;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

::v-deep .el-table .disabledCheck .cell::before {
  content: "";
  text-align: center;
  line-height: 37px;
}
</style>
