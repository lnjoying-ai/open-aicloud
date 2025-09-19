<template>
  <div class="warpMain">
    <!-- <el-card shadow="never" style="padding:0px  10px" id="formCard"> -->
    <el-form
      :inline="true"
      size="small"
      :model="queryData"
      class="demo-form-inline"
    >
      <el-form-item>
        <div slot="label">{{ $t("form.name") }}</div>
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
        <el-button class="add_search" @click="searchinit()">{{
          $t("form.reset")
        }}</el-button>
      </el-form-item>
      <el-button
        v-if="userInfo.kind == 0 || userInfo.kind == 1"
        style="float: right; margin: 10px 0px 0px 0px"
        size="small"
        type="primary"
        icon="el-icon-plus"
        @click="clickAddBtn"
        >{{ $t("form.add") }}</el-button
      >
    </el-form>
    <!-- </el-card> -->
    <div class="mt-2" />
    <el-table
      ref="multipleTable"
      :data="tableData.services"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="display_name" :label="$t('form.name')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff; cursor: pointer"
            :to="'/iam/serve/' + scope.row.id"
          >
            {{ scope.row.display_name }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="iam_code" :label="$t('form.serveCode')" />
      <el-table-column
        prop="create_time"
        :label="$t('form.createTime')"
        sortable
      />
      <el-table-column
        prop="update_time"
        :label="$t('form.modifyTime')"
        sortable
      />
      <el-table-column
        v-if="userInfo.kind == 0 || userInfo.kind == 1"
        :label="$t('form.operation')"
        width="100"
      >
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="clickDelBtn(scope.row)">{{
            $t("form.delete")
          }}</el-button>

          <!-- <el-button size="mini"
                     @click="clickDelBtn(scope.row)"
                     type="text"
                     icon="el-icon-delete"></el-button> -->
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
  </div>
</template>

<script>
import { getService, delService } from "@/api/iam/serve";
import initData from "@/mixins/initData";
import { mapGetters } from "vuex";
export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      tableData: [],
      queryData: {
        page_size: 10,
        page_num: 1,
        name: "",
      },
      nodeList: [],
      activeInfo: {},
    };
  },
  async created() {
    this.init();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    searchinit() {
      this.queryData = {
        page_size: 10,
        page_num: 1,
        name: "",
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
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getService(this.queryData);
      this.tableData = list;
    },
    clickAddBtn() {
      this.$router.push({
        path: "/iam/serve/addForm",
      });
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteServe"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delService(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
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

svg {
  display: inline-block;
  margin: -2px 2px 0px 0px;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}
</style>
