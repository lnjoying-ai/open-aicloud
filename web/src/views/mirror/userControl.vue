<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-row>
          <el-form-item>
            <div slot="label">{{ $t("form.warehouseUserName") }}:</div>
            <el-input
              v-model="queryData.registry_user_name"
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              class="add_search"
              @click="handleSizeChange"
              >{{ $t("form.query") }}</el-button
            >
          </el-form-item>
          <el-form-item>
            <el-button class="add_search" @click="resetList()">{{
              $t("form.reset")
            }}</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.registry_users"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column
        prop="registry_user_name"
        :label="$t('form.warehouseUserName')"
      />
      <el-table-column :label="$t('form.status')">
        <template slot-scope="scope">
          <div v-if="scope.row.status == 0" class="creating">
            {{ $t("form.creating") }}
          </div>
          <div v-if="scope.row.status == 1" class="normal1">
            {{ $t("form.partialSynchronization") }}
          </div>
          <div v-if="scope.row.status == 2" class="success">
            {{ $t("form.synchronizationCompleted") }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="
          userInfo.kind == '0' ||
          userInfo.kind == '1' ||
          userInfo.kind == '2' ||
          userInfo.kind == '3'
        "
        prop="user_name"
        :label="$t('form.user')"
      />
      <el-table-column
        v-if="
          userInfo.kind == '0' || userInfo.kind == '1' || userInfo.kind == '3'
        "
        prop="bp_name"
        :label="$t('form.organization')"
      />
      <el-table-column
        prop="create_time"
        :label="$t('form.createTime')"
        sortable
      />
      <el-table-column
        prop="update_time"
        :label="$t('form.updateTime')"
        sortable
      />
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
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { registriesUser, delUsers } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./service/addUser";

export default {
  components: {
    addForm,
  },
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      tableData: [],
      queryData: {
        registry_user_name: "",
        page_size: 10,
        page_num: 1,
      },
      activeInfo: {},

      multipleSelection: [],
    };
  },

  mounted() {},
  created() {
    this.getList();
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  methods: {
    handleSizeChange(val) {
      this.getList();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.getList();
    },
    async getList() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await registriesUser(this.queryData);
      this.tableData = list;
    },
    resetList() {
      this.queryData = {
        registry_user_name: "",
        page_size: 10,
        page_num: 1,
      };
      this.getList();
    },
    clickAddBtn() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteUser"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delUsers(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getList();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
  },
};
</script>

<style lang="scss" scoped></style>
