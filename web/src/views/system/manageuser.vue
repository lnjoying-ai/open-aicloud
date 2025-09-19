<template>
  <div>
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.name") }}:</div>
          <el-input
            v-model="queryData.name"
            :placeholder="$t('form.pleaseInputName')"
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
          <el-button class="add_search" @click="ResetData">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>

        <el-form-item
          v-if="kind == '0' || kind == '1' || kind == '2'"
          style="float: right"
        >
          <el-button type="primary" @click="clickAddBtn">{{
            $t("form.add")
          }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.users"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column :label="$t('form.status')">
        <template slot-scope="scope">
          <div v-if="scope.row.status == 0" class="examine">
            {{ $t("form.pendingActivation") }}
          </div>
          <div v-if="scope.row.status == 1" class="normal">
            {{ $t("form.normal") }}
          </div>
          <div v-if="scope.row.status == 2" class="error">
            {{ $t("form.overdue") }}
          </div>
          <div v-if="scope.row.status == 3" class="error">
            {{ $t("form.locked") }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="id" show-overflow-tooltip label="ID" />
      <el-table-column prop="name" :label="$t('form.name')" />
      <el-table-column prop="kind" :label="$t('form.role')">
        <template slot-scope="scope">
          <div v-if="scope.row.kind == 0">System</div>
          <div v-if="scope.row.kind == 1">admin</div>
          <div v-if="scope.row.kind == 2">BP admin</div>
          <div v-if="scope.row.kind == 3">personal</div>
          <div v-if="scope.row.kind == 4">Bp user</div>
        </template>
      </el-table-column>
      <el-table-column prop="level" :label="$t('form.level')" />
      <el-table-column
        prop="contact_info"
        :label="$t('form.contactInfo')"
        width="125"
      >
        <template slot-scope="scope">
          <div v-if="scope.row.contact_info" class="normalCursor">
            {{
              scope.row.contact_info.phone +
              " " +
              scope.row.contact_info.email +
              " " +
              scope.row.contact_info.address +
              " "
            }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="bp_id" label="bp_ID" />
      <el-table-column prop="is_allowed" :label="$t('form.accessControl')">
        <template slot-scope="scope">
          <div>
            {{
              scope.row.is_allowed == 1
                ? $t("form.allowAccess")
                : $t("form.denyAccess")
            }}
          </div>
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
        :total="tableData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>

<script>
import { getUsers, delUsers } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./manageuser/addForm";
import { mapGetters } from "vuex";
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
        name: "",
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
    ...mapGetters(["kind"]),
  },
  methods: {
    ResetData() {
      this.queryData.name = "";
      this.getList();
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
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
      const list = await getUsers(this.queryData);
      if (list.users != null) {
        for (var i = 0; i < list.users.length; i++) {
          if (list.users[i].contact_info.address == null) {
            list.users[i].contact_info.address = "";
          }
          if (list.users[i].contact_info.email == null) {
            list.users[i].contact_info.email = "";
          }
          if (list.users[i].contact_info.phone == null) {
            list.users[i].contact_info.phone = "";
          }
        }
        this.tableData = list;
      } else {
        this.tableData = [];
      }
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
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
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
