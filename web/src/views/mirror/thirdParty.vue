<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.warehouseName") + ":" }}</div>
          <el-input
            v-model="queryData.registry_name"
            :placeholder="$t('form.pleaseInputWarehouseName')"
          />
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == '0' || userInfo.kind == '1'"
          :label="$t('form.bp')"
        >
          <el-select
            v-model="queryData.bp_id"
            filterable
            clearable
            :placeholder="$t('form.pleaseSelect')"
            @change="changeValue"
          >
            <el-option
              v-for="(item, index) in BpsData"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="userInfo.kind == '0' || userInfo.kind == '1'">
          <div slot="label">{{ $t("form.userName") + ":" }}</div>
          <el-select
            v-model="queryData.user_id"
            filterable
            clearable
            :placeholder="$t('form.pleaseSelect')"
            @change="handeleUserChange"
          >
            <el-option
              v-for="(item, index) in userData"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            @click="handleCurrentChange(1)"
            >{{ $t("form.query") }}</el-button
          >
        </el-form-item>
        <el-form-item>
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == '0' || userInfo.kind == '1'"
          style="float: right"
        >
          <el-button type="primary" size="small" @click="downMirror">{{
            $t("form.imagePreDownload")
          }}</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" @click="clickAddBtn">{{
            $t("form.addWarehouse")
          }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.registries"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :header-cell-class-name="cellClass"
      :default-sort="{ prop: 'date', order: 'descending' }"
      @selection-change="handleSelectionChange"
      @select="dialogCheck"
    >
      <el-table-column type="selection" width="30" />
      <el-table-column prop="registry_name" :label="$t('form.warehouseName')" />
      <el-table-column :label="$t('form.type')">
        <template slot-scope="scope">
          {{ scope.row.type == "0" ? "harbor" : "docker hub" }}
        </template>
      </el-table-column>
      <el-table-column prop="user_name" :label="$t('form.userName')">
        <template slot-scope="scope">
          {{ scope.row.user_name || "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="url" :label="$t('form.url')">
        <template slot-scope="scope">
          {{ scope.row.url || "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.isVerifyCertificate')">
        <template slot-scope="scope">
          {{ scope.row.insecure ? $t("form.verify") : $t("form.unverify") }}
        </template>
      </el-table-column>
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
    <addForm ref="addForm" />
    <download ref="download" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { registries3rd, del3rd, getUsers, getBps } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./thirdParty/addForm";
import download from "./thirdParty/download.vue";

export default {
  components: {
    addForm,
    download,
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      tableData: {
        registries: [],
        total_num: 0,
      },
      queryData: {
        registry_name: "",
        page_size: 10,
        page_num: 1,
        user_id: "",
        bp_id: "",
      },
      activeInfo: {},
      userData: [],
      BpsData: [],
      multipleSelection: [],
      selectioned: {},
    };
  },

  mounted() {},
  created() {
    this.getList();
    this.userinit();
    if (this.userInfo.kind != "3" && this.userInfo.kind == "0") {
      this.bpsinit();
    } else {
      this.queryData.user_id = this.userInfo.id;
    }
  },

  methods: {
    // 隐藏表头中的全选框
    cellClass(row) {
      if (row.columnIndex === 0) {
        return "disable_cell";
      }
    },
    async userinit(id) {
      let list = [];
      if (id) {
        list = await getUsers({ bp_id: id });
      } else {
        list = await getUsers();
      }
      this.userData = list.users;
    },
    async bpsinit() {
      const list = await getBps();
      this.BpsData = list.bps;
    },
    changeValue(e) {
      this.queryData.bp_id = e;
      this.queryData.user_id = "";
      this.userinit(e);
      this.getList();
    },
    handeleUserChange(e) {
      this.queryData.user_id = e;
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
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    dialogCheck(selection, row) {
      this.$refs.multipleTable.clearSelection();
      if (selection.length === 0) {
        return;
      }
      if (row) {
        this.selectioned = row;
        this.$refs.multipleTable.toggleRowSelection(row, true);
      }
    },
    // 镜像预下载
    // 镜像预下载
    downMirror() {
      if (this.multipleSelection.length === 0) {
        this.$notify({
          title: this.$t("message.pleaseSelect"),
          type: "warning",
          duration: 2500,
        });
      } else {
        if (!this.selectioned || JSON.stringify(this.selectioned) === "{}") {
          this.$notify({
            title: this.$t("message.pleaseSelect"),
            type: "warning",
            duration: 2500,
          });
        } else {
          this.$refs.download.toData = [];
          this.$refs.download.add(this.selectioned);
        }
      }
    },
    async getList() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      if (this.userInfo.kind == "3") {
        this.queryData["user_id"] = this.userInfo.id;
      }
      const list = await registries3rd(this.queryData);
      this.tableData = list;
    },
    clickAddBtn() {
      this.$refs.addForm.add();
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteWarehouse"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          del3rd(value.registry_id)
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
    searchinit() {
      this.queryData = {
        registry_name: "",
        page_size: 10,
        page_num: 1,
        user_id: "",
        bp_id: "",
      };
      this.userinit();
      this.handleCurrentChange(1);
    },
    clickEditBtn(info) {
      this.$refs.addForm.edit(JSON.parse(JSON.stringify(info)));
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-table .disable_cell .cell .el-checkbox__inner {
  display: none !important;
}

::v-deep .el-table .disable_cell .cell::before {
  content: "";
  text-align: center;
  line-height: 37px;
}
</style>
