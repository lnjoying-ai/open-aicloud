<template>
  <div class="warpMain">
    <!-- <el-card shadow="never" style="padding:0px  10px" id="formCard"> -->
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
          icon="el-icon-search"
          class="add_search"
          type="primary"
          @click="handleCurrentChange(1)"
          >{{ $t("form.query") }}</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button class="add_search" @click="searchinit()">{{
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
        :data="tableData.policies"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column prop="name" :label="$t('form.name')">
          <template slot-scope="scope">
            <router-link
              style="color: #409eff; cursor: pointer"
              :to="'/iam/policie/contact/' + scope.row.id"
            >
              <span>
                {{ scope.row.display_name }}
              </span>
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="policy_type" :label="$t('form.policyType')">
          <template slot-scope="scope">
            <div v-if="scope.row.policy_type == 1">
              {{ $t("form.systemPolicy") }}
            </div>
            <div v-if="scope.row.policy_type == 2">
              {{ $t("form.customPolicy") }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="default_version"
          :label="$t('form.defaultVersion')"
        >
          <template slot-scope="scope">
            <div
              v-if="
                (userInfo.kind != 0 &&
                  userInfo.kind != 1 &&
                  scope.row.policy_type == 2) ||
                userInfo.kind == 0 ||
                userInfo.kind == 1
              "
            >
              <router-link
                style="color: #409eff"
                :to="'/iam/policie/version' + scope.row.id"
              >
                <span>
                  {{ scope.row.default_version }}
                </span>
              </router-link>
            </div>
            <div v-else>
              <span>
                {{ scope.row.default_version }}
              </span>
            </div>
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
          :label="$t('form.updateTime')"
          sortable
        />
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              :disabled="
                userInfo.kind != 0 &&
                userInfo.kind != 1 &&
                scope.row.policy_type == 1
              "
              @click="clickDelBtn(scope.row)"
              >{{ $t("form.delete") }}</el-button
            >
            <!-- <el-button size="mini"
                       @click="clickDelBtn(scope.row)"
                       type="text"
                       icon="el-icon-delete"
                       :disabled="userInfo.kind != 0 && userInfo.kind != 1 && scope.row.policy_type == 1"></el-button> -->
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
  </div>
</template>
<script>
import { getPolicie, delPolicie } from "@/api/iam/policie";
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
      const list = await getPolicie(this.queryData);
      this.tableData = list;
    },
    clickAddBtn() {
      this.$router.push({
        path: "/iam/policie/addForm",
      });
    },
    clickDelBtn(value) {
      this.$confirm(this.$t("message.deletePolicy"), this.$t("message.tip"), {
        confirmButtonText: this.$t("message.confirm"),
        cancelButtonText: this.$t("message.cancel"),
        type: "warning",
      })
        .then(() => {
          delPolicie(value.id)
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
