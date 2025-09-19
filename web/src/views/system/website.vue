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
          <div slot="label">{{ $t("form.region") }}:</div>
          <el-select
            v-model="queryData.region_id"
            filterable
            clearable
            :placeholder="$t('form.pleaseSelect')"
          >
            <el-option
              v-for="(item, index) in arealist"
              :key="index"
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
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <el-button type="primary" size="small" @click="clickAddBtn"
            >{{ $t("form.add") }}
          </el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" :label="$t('form.siteName')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="
              '/websiteDetail/' +
              scope.row.id +
              '/' +
              scope.row.name +
              '/' +
              scope.row.region_id
            "
          >
            {{ scope.row.name ? scope.row.name : "-" }}
          </router-link>
        </template>
      </el-table-column>

      <el-table-column prop="region_name" :label="$t('form.regionName')">
        <template slot-scope="scope">
          {{ scope.row.region_name ? scope.row.region_name : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="location" :label="$t('form.location')">
        <template slot-scope="scope">
          <div v-if="scope.row.location != null">
            {{
              scope.row.location.address.slice(
                0,
                scope.row.location.address.indexOf("&")
              ) +
              (scope.row.location.detail_address != ""
                ? "/" + scope.row.location.detail_address
                : "-")
            }}
          </div>
          <div v-else>{{ "-" }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="description" :label="$t('form.description')">
        <template slot-scope="scope">
          {{ scope.row.description ? scope.row.description : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="longitude" :label="$t('form.longitude')">
        <template slot-scope="scope">
          <span>
            {{
              scope.row.location.longitude ? scope.row.location.longitude : "-"
            }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="latitude" :label="$t('form.latitude')">
        <template slot-scope="scope">
          {{ scope.row.location.latitude ? scope.row.location.latitude : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="adcode" :label="$t('form.adcode')">
        <template slot-scope="scope">
          {{ scope.row.location.adcode ? scope.row.location.adcode : "-" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="cloud_num"
        :label="$t('form.privateCloud')"
        sortable
      >
        <template slot-scope="scope">
          {{ scope.row.cloud_num }}
        </template>
      </el-table-column>
      <el-table-column
        prop="node_num"
        :label="$t('form.containerNode')"
        sortable
      >
        <template slot-scope="scope">
          {{ scope.row.node_num }}
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
        :total="tableDataTotal"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <div class="flex justify-between mt-4 px-4" />
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getSites, delSites, getRegions } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./website/addForm";

export default {
  components: {
    addForm,
  },
  mixins: [initData],
  data() {
    return {
      arealist: [],
      sup_this: this,
      queryData: {
        region_id: "",
        orchestration: "",
        page_size: 10,
        page_num: 1,
      },
      tableData: [],
      tableDataTotal: 0,
      multipleSelection: [],
      activeInfo: {},
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {
    "queryData.region_id": {
      deep: true,
      handler(val) {
        this.init();
      },
    },
  },
  created() {
    this.areainit();
    this.init();
  },
  mounted() {},
  methods: {
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    searchinit() {
      this.queryData = {
        region_id: "",
        orchestration: "",
        page_size: 10,
        page_num: 1,
      };
      this.init();
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
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
      const list = await getSites(this.queryData);
      this.tableData = [];
      this.tableDataTotal = list.totalNum;
      list.sites.forEach((res) => {
        this.tableData.push(...res.sites);
      });
    },

    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.deleteSiteConfirm"),
        this.$t("message.confirm"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delSites(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {
              console.error(err.response.data.message);
            });
        })
        .catch(() => {});
    },
    clickEditBtn(info) {
      this.activeInfo = JSON.parse(JSON.stringify(info));
      this.$refs.addForm.id = info.id;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
      this.$refs.addForm.getLabelOption("site");
      this.$refs.addForm.getTaintOptions("site");
    },
    // 添加站点调用
    clickAddBtn(info) {
      this.activeInfo = JSON.parse(JSON.stringify(info));
      this.$refs.addForm.getLabelOption("site");
      this.$refs.addForm.getTaintOptions("site");
      this.$refs.addForm.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped></style>
