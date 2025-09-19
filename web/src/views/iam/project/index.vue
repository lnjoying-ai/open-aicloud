<template>
  <div class="warpMain">
    <!-- <el-card shadow="never" style="padding:0px  10px" id="formCard"> -->
    <el-form :inline="true" size="small" :model="queryData">
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
        :data="tableData"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
        :row-key="getRowKeys"
        :tree-props="{ children: 'children' }"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column
          prop="nodeData.display_name"
          :label="$t('form.name')"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <router-link
              style="color: #409eff; cursor: pointer"
              :to="'/iam/project/' + scope.row.id"
            >
              {{ scope.row.nodeData.display_name }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column prop="level" :label="$t('form.level')">
          <template slot-scope="scope">
            <span>{{ scope.row.level }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.status')">
          <template slot-scope="scope">
            <div v-if="scope.row.nodeData.enable == 1">
              <svg
                t="1681131360914"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="6110"
                width="14"
                height="14"
              >
                <path
                  d="M440.7 314.3c-16.5-9.5-34.1-10.3-48.4-2-14.3 8.2-22.4 23.9-22.4 42.9v308.4c0 19 8.2 34.7 22.4 42.9 6.6 3.8 13.9 5.7 21.4 5.7 8.8 0 18.1-2.6 27-7.7l267.1-154.2c16.5-9.5 26-24.4 26-40.9s-9.5-31.4-26-40.9L440.7 314.3z m-16 336.1v-282l244.2 141-244.2 141z"
                  fill="#2c2c2c"
                  p-id="6111"
                />
                <path
                  d="M924 343.8c-7.1-17.6-27-26.1-44.6-19-17.6 7.1-26.1 27-19 44.6 17.9 44.6 27 91.9 27 140.4 0 207.8-169.1 376.9-376.9 376.9-207.8 0-376.9-169.1-376.9-376.9s169.1-376.9 376.9-376.9c82.3 0 160.4 26 226 75.3 15.1 11.4 36.6 8.3 48-6.8 11.4-15.1 8.3-36.6-6.8-48-77.6-58.2-170-89-267.2-89C264.9 64.4 65 264.2 65 509.9c0 245.6 199.8 445.5 445.5 445.5 245.6 0 445.5-199.8 445.5-445.5 0-57.4-10.8-113.3-32-166.1z"
                  fill="#2c2c2c"
                  p-id="6112"
                />
                <path
                  d="M826.8 268.1m-39.6 0a39.6 39.6 0 1 0 79.2 0 39.6 39.6 0 1 0-79.2 0Z"
                  fill="#2c2c2c"
                  p-id="6113"
                />
              </svg>
              {{ $t("form.enable") }}
            </div>
            <div v-if="scope.row.nodeData.enable == -1">
              <svg
                t="1681131388446"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="6973"
                width="14"
                height="14"
              >
                <path
                  d="M512 76.8a428.8 428.8 0 1 0 428.8 428.8c6.4-236.8-185.6-428.8-428.8-428.8z m0 800c-204.8 0-364.8-166.4-364.8-364.8S313.6 140.8 512 140.8s364.8 166.4 364.8 364.8-160 371.2-364.8 371.2z"
                  p-id="6974"
                />
                <path d="M236.8 473.6h556.8v64H236.8z" p-id="6975" />
              </svg>
              {{ $t("form.stopUse") }}
            </div>
          </template>
        </el-table-column>
        <el-table-column
          prop="proNum"
          :label="$t('form.subProjectCount')"
          sortable
        >
          <template slot-scope="scope">
            <span v-if="scope.row.proNum == 0">0</span>
            <span v-else>{{ scope.row.proNum }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="nodeData.create_time"
          :label="$t('form.createTime')"
          sortable
        />
        <el-table-column
          prop="nodeData.update_time"
          :label="$t('form.updateTime')"
          sortable
        />
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div
                  v-if="scope.row.nodeData.enable != 1"
                  class="icon_cz"
                  @click="clickActionBtn(scope.row, 'active')"
                >
                  <i class="el-icon-video-play" />
                  {{ $t("form.enable") }}
                </div>
                <div
                  v-if="scope.row.nodeData.enable == 1"
                  class="icon_cz"
                  @click="clickActionBtn(scope.row, 'deactive')"
                >
                  <i class="el-icon-video-pause" />
                  {{ $t("form.stopUse") }}
                </div>
                <div class="icon_cz" @click="clickDelBtn(scope.row)">
                  <i class="el-icon-delete" />
                  {{ $t("form.delete") }}
                </div>
                <div class="icon_cz" @click="clickAddChidrenBtn(scope.row)">
                  <i class="el-icon-circle-plus-outline" />
                  {{ $t("form.addSubProject") }}
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
        :total="queryData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { getProject, delProject, actionProject } from "@/api/iam/project";
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
        total_num: 0,
      },
      nodeList: [],
      activeInfo: {},
    };
  },
  computed: {},
  async created() {
    this.init();
  },
  mounted() {},
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
      const list = await getProject(this.queryData);
      this.tableData = list.projects;
      this.queryData.total_num = list.total_num;
    },
    // 设置keys
    getRowKeys(row) {
      return row.id;
    },
    clickAddBtn() {
      this.$router.push({
        path: "/iam/project/addForm",
        query: {
          id: "",
        },
      });
    },
    clickAddChidrenBtn(info) {
      this.$router.push({
        path: "/iam/project/addForm",
        query: {
          id: info.id,
        },
      });
    },
    clickActionBtn(value, type) {
      var typeTxt =
        type == "active" ? this.$t("form.enable") : this.$t("form.stopUse");
      this.$confirm(
        this.$t("message.confirmActionProject", { type: typeTxt }),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          actionProject(value.id, {
            action: type,
          })
            .then((res) => {
              this.$notify({
                title: typeTxt,
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteProject"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delProject(value.id)
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

svg {
  display: inline-block;
  margin: -2px 2px 0px 0px;
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
