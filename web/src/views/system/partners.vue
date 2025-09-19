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
                v-model="queryData.name"
                :placeholder="$t('form.pleaseInput')"
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
            <el-form-item
              v-if="kind == '0' || kind == '1'"
              style="float: right"
            >
              <el-button type="primary" size="small" @click="clickAddBtn">{{
                $t("form.add")
              }}</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-row :gutter="12">
      <el-col :span="24">
        <el-table
          ref="multipleTable"
          :data="tableData.bps"
          stripe
          tooltip-effect="dark"
          style="width: 100%"
        >
          <el-table-column prop="name" :label="$t('form.bpName')">
            <template slot-scope="scope">
              {{ scope.row.name ? scope.row.name : "-" }}
            </template>
          </el-table-column>

          <el-table-column
            prop="description"
            :label="$t('form.bpDescription')"
            show-overflow-tooltip
          >
            <template slot-scope="scope">
              {{ scope.row.description ? scope.row.description : "-" }}
            </template>
          </el-table-column>

          <el-table-column prop="master_user" :label="$t('form.masterUser')">
            <template slot-scope="scope">
              {{ scope.row.master_user ? scope.row.master_user : "-" }}
            </template>
          </el-table-column>
          <el-table-column prop="contact_info" :label="$t('form.masterPhone')">
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
          <el-table-column
            v-if="kind == '0' || kind == '1'"
            :label="$t('form.operation')"
            width="100"
          >
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
                    v-if="
                      scope.row.master_user == '' ||
                      scope.row.master_user == null
                    "
                    class="icon_cz"
                    @click="clickAddManageBtn(scope.row)"
                  >
                    <i class="el-icon-plus" />
                    {{ $t("form.addManage") }}
                  </div>
                  <div
                    v-if="scope.row.master_user"
                    class="icon_cz"
                    @click="clickEditManageBtn(scope.row)"
                  >
                    <i class="el-icon-edit-outline" />
                    {{ $t("form.editManage") }}
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
      </el-col>
    </el-row>
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
import { getBps, delBps } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./partner/addForm";
import { mapGetters } from "vuex";
import addFormManage from "./partner/addFormManage";
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
  computed: {
    ...mapGetters(["kind"]),
  },

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
      this.$confirm(this.$t("form.confirmDeleteBp"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
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

<style lang="scss" scoped></style>
