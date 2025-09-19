<template>
  <div class="warpMain">
    <!-- <el-card shadow="never" style="padding:0px  10px" id="formCard"> -->
    <el-form
      :inline="true"
      size="small"
      :model="queryData"
      class="demo-form-inline"
    >
      <el-button
        style="float: right; margin: 10px 0px 10px 0px"
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
        :data="tableData"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column prop="version_id" :label="$t('form.versionNumber')">
          <template slot-scope="scope">
            <router-link
              style="color: #409eff; cursor: pointer"
              :to="
                '/iam/policie/versionDetail/' +
                scope.row.policy_id +
                '/' +
                scope.row.version_id
              "
            >
              {{ scope.row.version_id }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column
          prop="default_version"
          :label="$t('form.isDefaultVersion')"
        >
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.default_version"
              size="small"
              @change="handleDefaultVersion(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="$t('form.description')" />
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
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              @click="clickDelBtn(scope.row)"
              >{{ $t("form.delete") }}</el-button
            >
            <!-- <el-button size="mini"
                       @click="clickDelBtn(scope.row)"
                       type="text"
                       icon="el-icon-delete"></el-button> -->
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
        :total="tableData.length"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getPolicieVersion,
  delPolicieVersion,
  setPolicieVersion,
} from "@/api/iam/policie";
import initData from "@/mixins/initData";
import addForm from "./addForm";

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
        page_size: 10,
        page_num: 1,
      },
      activeInfo: {},
    };
  },
  async created() {
    this.init();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
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
      const list = await getPolicieVersion(
        this.$route.params.id,
        this.queryData
      );
      this.tableData = list;
    },

    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeletePolicyVersion"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delPolicieVersion(value.policy_id, value.version_id)
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
    clickAddBtn() {
      this.activeInfo = {};
      this.$refs.addForm.id = this.$route.params.id;
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
    },
    handleDefaultVersion(value) {
      if (this.tableData.length == 1) {
        value.default_version = true;
        this.$notify({
          title: this.$t("message.uniquePolicyVersion"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      const params = {};
      params.default_version = value.default_version;
      setPolicieVersion(value.policy_id, value.version_id, params)
        .then((res) => {
          this.$notify({
            title: this.$t("message.operationSuccess"),
            type: "success",
            duration: 2500,
          });
          this.init();
        })
        .catch((err) => {});
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

.normal {
  color: #0f97ff;
}

.examine {
  color: #0f97ff;
}

.error {
  color: #f53333;
}

.normalCursor {
  color: #0f97ff;
  cursor: pointer;
}

.weightd {
  font-size: 30px;
  font-weight: bold;
  margin-right: 5px;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}
</style>
