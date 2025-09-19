<template>
  <div class="warpMain">
    <el-form :inline="true" size="small" :model="queryData">
      <el-form-item>
        <div slot="label">{{ $t("form.name") }}:</div>
        <el-input
          v-model="queryData.name"
          :placeholder="$t('form.pleaseInputName')"
        />
      </el-form-item>
      <el-form-item>
        <div slot="label">{{ $t("form.belongOrganization") }}:</div>
        <el-select
          v-model="queryData.bp_id"
          :placeholder="$t('form.pleaseSelectBelongOrganization')"
          style="width: 100%"
          clearable
          @change="handleBpChange"
        >
          <el-option
            v-for="item in bpsData"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
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
        :data="tableData.users"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column prop="name" :label="$t('form.name')">
          <template slot-scope="scope">
            <router-link
              style="color: #409eff"
              :to="'/iam/user/detail/' + scope.row.id"
            >
              {{ scope.row.name }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="contact_info" :label="$t('form.email')">
          <template slot-scope="scope">
            <div v-if="scope.row.contact_info" class=" ">
              {{
                scope.row.contact_info.email
                  ? scope.row.contact_info.email
                  : "-"
              }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="contact_info" :label="$t('form.phone')">
          <template slot-scope="scope">
            <div v-if="scope.row.contact_info" class=" ">
              {{
                scope.row.contact_info.phone
                  ? scope.row.contact_info.phone
                  : "-"
              }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="bp_name" :label="$t('form.organization')">
          <template slot-scope="scope">
            <div>
              {{ scope.row.bp_name ? scope.row.bp_name : "-" }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="status" :label="$t('form.status')">
          <template slot-scope="scope">
            <div>
              <span v-if="scope.row.status == 0">
                <el-tag size="small" type="info">{{
                  $t("form.pendingActivation")
                }}</el-tag>
              </span>
              <span v-if="scope.row.status == 1">
                <el-tag size="small" type="success">{{
                  $t("form.normal")
                }}</el-tag></span
              >
              <span v-if="scope.row.status == 2">
                <el-tag size="small" type="warning">{{
                  $t("form.overdue")
                }}</el-tag></span
              >
              <span v-if="scope.row.status == 3">
                <el-tag size="small" type="danger">{{
                  $t("form.locked")
                }}</el-tag></span
              >
            </div>
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
        >
          <template slot-scope="scope">
            <div>
              {{ scope.row.update_time ? scope.row.update_time : "-" }}
            </div>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <div>
              <operate
                :prop-show-type="1"
                :propuser-detail="scope.row"
                @getuserList="init"
              />
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
  </div>
</template>

<script>
import { getUsers, delUsers } from "@/api/iam/user";
import { getBps } from "@/api/iam/partners";
import initData from "@/mixins/initData";
import { mapGetters } from "vuex";
import { editUsers } from "@/api/iam/user";
import operate from "./operate.vue";
export default {
  components: {
    operate,
  },
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      tableData: [],
      queryData: {
        page_size: 10,
        page_num: 1,
        bp_id: "",
        name: "",
      },
      bpsData: [],
    };
  },
  async created() {
    this.init();
    this.bpsinit();
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
        bp_id: "",
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
    handleBpChange(val) {
      this.queryData.bp_id = val;
      this.init();
    },
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getUsers(this.queryData);
      this.tableData = list;
    },
    // 组织机构
    async bpsinit() {
      const list = await getBps();
      this.bpsData = list.bps;
    },
    clickAddBtn() {
      this.$router.push({
        path: "/iam/user/addForm",
      });
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

.weightd {
  font-size: 30px;
  font-weight: bold;
  margin-right: 5px;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}
</style>
