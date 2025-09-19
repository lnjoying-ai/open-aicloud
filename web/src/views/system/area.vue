<template>
  <div class="warpMain">
    <div>
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.areaName") }}:</div>
          <el-input
            v-model="queryData.name"
            :placeholder="$t('form.pleaseInputAreaName')"
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
          v-if="kind == '0' || kind == '1'"
          style="float: right; margin-bottom: 20px; display: inline-block"
          size="small"
          type="primary"
          @click="clickAddBtn"
          >{{ $t("form.add") }}</el-button
        >
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.regions"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" :label="$t('form.areaName')">
        <template slot-scope="scope">
          {{ scope.row.name ? scope.row.name : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="description" :label="$t('form.description')">
        <template slot-scope="scope">
          {{ scope.row.description ? scope.row.description : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="gw_node_ids" :label="$t('form.gatewayList')">
        <template slot-scope="scope">
          <span v-for="(item, index) in scope.row.gw_node_ids" :key="index">
            {{ (index != 0 ? "," : "") + item.name }}
          </span>
          <span v-if="scope.row.gw_node_ids.length == 0">{{ "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="create_time"
        :label="$t('form.createTime')"
        sortable
      />
      <el-table-column
        prop="upate_time"
        :label="$t('form.updateTime')"
        sortable
      />
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
        :total="tableData.totalNum"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getRegions, gws, delRegions } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./area/addForm";

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
    async getNodeData() {
      const list = await gws();
      if (list.nodes != null) {
        this.nodeList = list.nodes.map((item) => {
          return {
            value: item.node_id,
            label: item.node_name,
          };
        });
      }
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
      const list = await getRegions(this.queryData);
      this.tableData = list;
    },
    clickAddBtn() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
      this.$refs.addForm.getLabelOption("region");
      this.$refs.addForm.getTaintOptions("region");
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteRegion"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delRegions(value.id)
            .then((res) => {
              if (res != undefined) {
                this.$notify({
                  title: this.$t("message.deleteSuccess"),
                  type: "success",
                  duration: 2500,
                });
              }
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickEditBtn(info) {
      this.activeInfo = info;
      this.$refs.addForm.id = info.id;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
      this.$refs.addForm.getLabelOption("region");
      this.$refs.addForm.getTaintOptions("region");
    },
  },
};
</script>

<style lang="scss" scoped></style>
