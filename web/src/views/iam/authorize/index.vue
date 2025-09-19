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
          >{{ $t("form.query") }}
        </el-button>
      </el-form-item>
      <el-form-item>
        <el-button class="add_search" @click="searchinit()">{{
          $t("form.reset")
        }}</el-button>
      </el-form-item>
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
        <el-table-column
          prop="policy_name"
          :label="$t('form.name')"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <span>
              {{ scope.row.policy_display_name }}
            </span>
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
          prop="principal_name"
          :label="$t('form.principalName')"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <div v-if="scope.row.principal_type == 1">
              <router-link
                v-if="scope.row.principal_name"
                style="color: #409eff"
                :to="'/iam/user/detail/' + scope.row.principal_id"
              >
                <span>{{ scope.row.principal_name }}</span>
              </router-link>
              <span v-else>{{ "-" }}</span>
            </div>
            <div v-else-if="scope.row.principal_type == 3">
              <router-link
                v-if="scope.row.principal_name"
                style="color: #409eff"
                :to="'/iam/role/detail/' + scope.row.principal_id"
              >
                <span>{{ scope.row.principal_name }}</span>
              </router-link>
              <span v-else>{{ "-" }}</span>
            </div>
            <div v-else>
              <span>{{
                scope.row.principal_name ? scope.row.principal_name : "-"
              }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="principal_type"
          :label="$t('form.principalType')"
        >
          <template slot-scope="scope">
            <div v-if="scope.row.principal_type == 1">
              <svg
                t="1681701629708"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="3471"
                width="14"
                height="14"
              >
                <path
                  d="M895.853333 926.08a53.333333 53.333333 0 0 1-53.333333 55.253333H53.446667a53.333333 53.333333 0 0 1-53.3-55.286666c1.093333-30.206667 8.733333-72.1 38.933333-100.173334 25.086667-23.333333 51.913333-41.58 82-55.833333 26.606667-12.593333 53.453333-21.04 79.413333-29.213333 27.6-8.666667 56.14-17.666667 86.073334-32C316.666667 694.5 334.126667 670 338.666667 635.886667a199.1 199.1 0 0 1-60.053334-57.553334 188.206667 188.206667 0 0 1-29.126666-71.206666c-11.033333-2.593333-25.486667-10.866667-36.466667-34.46-8.28-17.74-12.833333-40.853333-12.833333-65.08 0-15.206667-2.853333-29.54-5.38-42.193334-3.78-18.986667-7.046667-35.386667 3.26-47.96 3.073333-3.74 9.906667-9.913333 21.846666-10.433333-0.82-11.493333-1.233333-22.04-1.24-31.533333-0.04-45.42 3.72-81.413333 11.48-110 10.106667-37.26 27.78-64 52.526667-79.48a21.333333 21.333333 0 0 1 20.426667-1.333334 21.086667 21.086667 0 0 1 12 16.366667 48.826667 48.826667 0 0 0 3.013333 10 120.26 120.26 0 0 1 14.133333-11.2c12.493333-8.626667 29.66-17.92 51.026667-27.62C418.666667 56.106667 453.573333 44.28 455.046667 43.786667a21.333333 21.333333 0 0 1 22.733333 34.413333c-7.113333 8-10.766667 13.666667-12.666667 17.333333 10.133333-0.066667 20.966667-1.72 32.373334-3.46 38.566667-5.886667 86.566667-13.213333 144.373333 34.78 37.38 31.033333 41.28 85.926667 35.393333 176.366667q-0.133333 2-0.32 4.186667a50.666667 50.666667 0 0 1 6.76-0.48c13.193333 0 20.766667 6 24.8 11.033333 10.36 12.946667 6.733333 29.14 2.526667 47.893333-2.813333 12.54-6 26.746667-6 41.693334 0 24.566667-5.193333 48-14.626667 66-10.666667 20.366667-26.546667 32.666667-44 34.433333C636.973333 558.333333 607 603.12 562.666667 632.486667c3.68 35.913333 20.973333 61.6 51.513333 76.433333 29.393333 14.28 57.426667 23.253333 84.533333 31.933333 25.54 8.18 51.953333 16.666667 78.14 29.246667 29.633333 14.266667 56.04 32.553333 80.733334 55.9 29.693333 28.066667 37.2 69.913333 38.266666 100.08zM874.666667 298.666667h128a21.333333 21.333333 0 0 0 0-42.666667h-128a21.333333 21.333333 0 0 0 0 42.666667z m128 469.333333h-42.666667a21.333333 21.333333 0 0 0 0 42.666667h42.666667a21.333333 21.333333 0 0 0 0-42.666667z m0-256h-170.666667a21.333333 21.333333 0 0 0 0 42.666667h170.666667a21.333333 21.333333 0 0 0 0-42.666667z"
                  fill="#515151"
                  p-id="3472"
                />
              </svg>
              {{ $t("form.user") }}
            </div>
            <div v-if="scope.row.principal_type == 2">
              <svg
                t="1681701784780"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="9049"
                width="14"
                height="14"
              >
                <path
                  d="M558.4 602.432c88.128-49.28 148.288-142.336 148.288-250.368a288 288 0 1 0-576-0.064c0 107.2 59.328 199.616 146.24 249.216C115.84 653.824 0 794.56 0 960h64c0-174.848 157.888-317.12 351.936-317.12C610.048 642.88 768 785.152 768 960h64c0-164.224-114.24-304.128-273.6-357.568zM194.688 352c0-123.52 100.48-224 223.936-224s224.064 100.48 224.064 224S542.208 576 418.624 576a224.256 224.256 0 0 1-223.936-224zM1024 768h-64a254.848 254.848 0 0 0-233.6-253.952 354.56 354.56 0 0 0 30.848-77.312 127.936 127.936 0 0 0-22.016-240.384 350.08 350.08 0 0 0-42.944-67.136c3.968-0.256 7.68-1.216 11.712-1.216 105.856 0 192 86.144 192 192 0 61.12-28.672 115.584-73.28 150.784A320.64 320.64 0 0 1 1024 768z"
                  fill="#515151"
                  p-id="9050"
                /></svg
              >{{ $t("form.userGroup") }}
            </div>
            <div v-if="scope.row.principal_type == 3">
              <svg
                t="1681701738912"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="7197"
                width="14"
                height="14"
              >
                <path
                  d="M79.23846992 961.89615371v-25.44230742c0-109.28076943 28.83461573-214.89230742 81.13846113-297.41538515 48.42692315-76.39615372 115.30384629-131.57307685 195.50769258-161.89615372A240.78461573 240.78461573 0 0 1 279.48846992 300.5c0-131.53846114 104.33076943-238.53461572 232.54615372-238.53461572s232.51153887 106.99615372 232.51153798 238.53461572a240.85384629 240.85384629 0 0 1-76.74230742 176.98846114c190.86923057 73.00384629 276.99230742 277.13076943 276.99230742 458.96538514v25.44230743H79.23846992zM694.90770049 300.5c0-103.43076943-82.03846114-187.61538427-182.87307686-187.61538427-100.83461573 0-182.87307685 84.18461573-182.87307685 187.61538427 0 103.46538427 82.03846114 187.65 182.87307685 187.65 100.83461573 0 182.87307685-84.18461573 182.87307686-187.65z m-79.16538516 213.50769258a226.45384629 226.45384629 0 0 1-103.7076917 25.0961537 225.93461572 225.93461572 0 0 1-104.12307686-25.30384628c-195.02307685 51.12692315-271.10769258 239.05384629-278.41153886 397.17692315h765.03461573c-7.99615372-167.4-95.22692315-347.74615372-278.79230831-396.96923057z m-143.41153799 37.2461537h79.40769258l39.73846114-8.48076943-45.24230742 65.66538429 30.6 227.52692313-64.8 56.90769258-69.19615372-56.90769258 40.53461485-227.52692313-50.78076944-65.66538429 39.73846201 8.48076944z"
                  p-id="7198"
                  fill="#515151"
                /></svg
              >{{ $t("form.role") }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="project_display_name"
          :label="$t('form.projectName')"
        >
          <template slot-scope="scope">
            {{
              scope.row.project_display_name
                ? scope.row.project_display_namen
                : "-"
            }}
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
          prop="attach_time"
          :label="$t('form.attachTime')"
          sortable
        />
      </el-table>
    </div>
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
import { mapGetters } from "vuex";
import { getAuthz } from "@/api/iam/authz";
import initData from "@/mixins/initData";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      tableData: [],
      queryData: {
        page_size: 10,
        page_num: 1,
        name: "",
        total: 0,
      },
      nodeList: [],
      activeInfo: {},
      drawer: false,
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
      const list = await getAuthz(this.queryData);
      this.tableData = list.attachments;
      this.queryData.total = list.total_num;
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
