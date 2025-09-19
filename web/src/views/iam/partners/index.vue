<template>
  <div class="warpMain">
    <!-- <el-card shadow="never" id="formCard"> -->
    <el-form :inline="true" size="small" :model="queryData">
      <el-form-item>
        <div slot="label">{{ $t("form.name") }}:</div>
        <el-input
          v-model="queryData.name"
          :placeholder="$t('form.pleaseInputName')"
        />
      </el-form-item>
      <el-form-item>
        <el-button
          size="small"
          icon="el-icon-search"
          class="add_search"
          type="primary"
          @click="handleCurrentChange(1)"
          >{{ $t("form.query") }}</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button size="small" class="add_search" @click="ResetData">{{
          $t("form.reset")
        }}</el-button>
      </el-form-item>
      <el-button
        style="float: right; margin: 10px 0px 0px 0px"
        size="small"
        type="primary"
        icon="el-icon-plus"
        @click="clickAddBtn"
        >{{ $t("form.add") }}</el-button
      >
    </el-form>
    <!-- </el-card> -->
    <div class="mt-2">
      <el-table
        ref="multipleTable"
        :data="tableData.bps"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
      >
        <el-table-column prop="name" :label="$t('form.name')">
          <template slot-scope="scope">
            {{ scope.row.name ? scope.row.name : "-" }}
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
        <el-table-column
          prop="user_total"
          :label="$t('form.userTotal')"
          sortable
        >
          <template slot-scope="scope">
            {{ scope.row.user_total ? scope.row.user_total : "-" }}
          </template>
        </el-table-column>
        <el-table-column prop="master_user" :label="$t('form.masterUser')">
          <template slot-scope="scope">
            {{ scope.row.master_user ? scope.row.master_user : "-" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="contact_info"
          :label="$t('form.masterPhone')"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <div class="normalCursor">
              {{
                scope.row.contact_info
                  ? scope.row.contact_info.phone
                    ? scope.row.contact_info.phone
                    : "-"
                  : "-"
              }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="140" trigger="click">
                <div class="icon_cz" @click="clickEditBtn(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
                </div>
                <div class="icon_cz" @click="clickDelBtn(scope.row)">
                  <i class="el-icon-delete" />
                  {{ $t("form.delete") }}
                </div>
                <div
                  v-if="
                    scope.row.master_user == '' || scope.row.master_user == null
                  "
                  class="icon_cz"
                  @click="clickAddManageBtn(scope.row)"
                >
                  <i class="el-icon-plus" />
                  {{ $t("form.addAdmin") }}
                </div>
                <div
                  v-if="scope.row.master_user"
                  class="icon_cz"
                  @click="clickEditManageBtn(scope.row)"
                >
                  <i class="el-icon-edit-outline" />
                  {{ $t("form.editAdmin") }}
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
    </div>
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
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
    <addFormManage
      ref="addFormManage"
      :sup_this="sup_this"
      :info="activeInfoManage"
    />
  </div>
</template>

<script>
import { getBps, delBps } from "@/api/iam/partners";
import initData from "@/mixins/initData";
import addForm from "./addForm";
import addFormManage from "./addFormManage";
export default {
  components: {
    addForm,
    addFormManage,
  },
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      disabled: false,
      tableData: [],
      queryData: {
        name: "",
        page_size: 10,
        page_num: 1,
      },
      activeInfo: {},
      activeInfoManage: {},
    };
  },
  computed: {},

  mounted() {},
  created() {
    this.init();
  },

  methods: {
    // 添加管理员
    clickAddManageBtn(row) {
      this.activeInfoManage = {};
      this.$refs.addFormManage.form.bp_id = row.id;
      this.$refs.addFormManage.isAdd = true;
      this.$refs.addFormManage.dialog = true;
    },
    // 修改管理员
    clickEditManageBtn(Manageinfo) {
      this.activeInfoManage = Manageinfo;
      this.$refs.addFormManage.form.bp_id = Manageinfo.id;
      this.$refs.addFormManage.id = Manageinfo.master_user_id;
      this.$refs.addFormManage.isAdd = false;
      this.$refs.addFormManage.dialog = true;
    },
    ResetData() {
      this.queryData.name = "";
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
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getBps(this.queryData);
      this.tableData = list;
    },
    clickAddBtn() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteOrganization"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delBps(value.id)
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
    clickEditBtn(info) {
      this.activeInfo = info;
      this.$refs.addForm.id = info.id;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep #formCard .el-card__body {
  padding: 0px 0px;
}

::v-deep .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 0px;
  padding: 10px 0px;
}

.el-popover {
  min-width: 0px !important;
  padding: 0px !important;
}

.icon_cz {
  display: block;
  height: 24px;
  color: #666666;
  align-items: center;
  cursor: pointer;
  margin-top: 5px;
  margin-bottom: 5px;
  line-height: 24px;
  text-align: center;
}

.icon_cz:hover {
  background: #daefff;
}

.cz_icon {
  margin-left: 16px;
  margin-right: 23px;
}

.czlb {
  display: flex;
  align-items: center;
}

.czbtn {
  width: 23px;
  height: 26px;
  border: 1px solid #d0d0d0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_czbtn {
  border-radius: 3px 0 0 3px;
  border-right: 0px;
  cursor: pointer;
}

.left_czbtn img {
  height: 11px;
}

.right_czbtn {
  border-radius: 3px;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}
</style>
