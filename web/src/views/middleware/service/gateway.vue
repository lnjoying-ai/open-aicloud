<template>
  <div class="warpMain">
    <el-table
      ref="multipleTable"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" :label="$t('form.gatewayName')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="
              '/middleware/service/gateway/detail/' +
              scope.row.service_gateway_id
            "
            class="inline-block"
          >
            <span class="block leading-none">{{
              scope.row.name ? scope.row.name : "-"
            }}</span>
          </router-link>
          <div class="new-small-size">
            ID:{{ scope.row.service_gateway_id }}
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="description"
        :label="$t('form.description')"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{ scope.row.description ? scope.row.description : "-" }}</span>
          <span v-if="scope.row.purpose">
            <el-popover
              v-if="scope.row.purpose.split(',').length == 2"
              placement="top-start"
              width="200"
              trigger="hover"
              popper-class="tablepopover"
              :content="$t('form.purposeAndResourceAndUser')"
            >
              <span slot="reference"> <i class="el-icon-info" /></span>
            </el-popover>
            <el-popover
              v-if="
                scope.row.purpose.split(',').length == 1 &&
                scope.row.purpose == 'inner_monitor'
              "
              placement="top-start"
              width="200"
              trigger="hover"
              popper-class="tablepopover"
              :content="$t('form.purposeAndResource')"
            >
              <span slot="reference"> <i class="el-icon-info" /></span>
            </el-popover>
            <el-popover
              v-if="
                scope.row.purpose.split(',').length == 1 &&
                scope.row.purpose == 'service_user'
              "
              placement="top-start"
              width="200"
              trigger="hover"
              popper-class="tablepopover"
              :content="$t('form.purposeAndUser')"
            >
              <span slot="reference"> <i class="el-icon-info" /></span>
            </el-popover>
          </span>
          <span v-else>
            <el-popover
              placement="top-start"
              width="200"
              trigger="hover"
              popper-class="tablepopover"
              content="-"
            >
              <span slot="reference"> <i class="el-icon-info" /></span>
            </el-popover>
          </span>
          <div>
            <span />{{ $t("form.gatewayEndpoint") }}：<span>
              {{ scope.row.endpoint ? scope.row.endpoint : "-" }}</span
            >
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('form.status')">
        <template slot-scope="scope">
          <span v-if="scope.row.status.code == 0" style="color: #67c23a">
            {{ $t("form.online") }}
          </span>
          <span v-if="scope.row.status.code == 1" style="color: #e6a23c">
            {{ $t("form.offline") }}
          </span>
          <span v-if="scope.row.status.code == 99" style="color: #f56c6c">
            {{ $t("statusAndType.deleted") }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        prop="create_time"
        :label="$t('form.createTime')"
        sortable
      >
        <template slot-scope="scope">
          {{ scope.row.create_time ? scope.row.create_time : "-" }}
        </template>
      </el-table-column>
      <el-table-column
        prop="update_time"
        :label="$t('form.updateTime')"
        sortable
      >
        <template slot-scope="scope">
          {{ scope.row.update_time ? scope.row.update_time : "-" }}
        </template>
      </el-table-column>
      <!-- <el-table-column prop="registry_name"
                       label="操作"
                       width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom"
                        width="110"
                        trigger="click">
              <div class="icon_cz"
                   @click="clickEditBtn(scope.row)">
                <i class="el-icon-edit"></i>
                编辑 / 查看
              </div>

              <el-button slot="reference"
                         icon="el-icon-more"
                         class="czbtn right_czbtn">
              </el-button>
            </el-popover>
          </div>
        </template>
      </el-table-column> -->
    </el-table>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="queryData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="queryData.totalNum"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { getServiceGatewayDetail } from "@/api/mainApi";
import initData from "@/mixins/initData";

export default {
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      tableData: [],
      siteData: [], // 站点
      queryData: {
        port_ranges: "",
        site_id: "",
        target_type: "",
        page_size: 10,
        page_num: 1,
        totalNum: 0,
      },
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "timeInterval"]),
  },
  components: {},
  async created() {
    this.init();
  },
  mounted() {},

  methods: {
    searchinit() {
      this.queryData = {
        port_ranges: "",
        site_id: "",
        page_size: 10,
        page_num: 1,
        totalNum: 0,
      };
      this.init();
    },

    handleSiteChange(val) {
      this.queryData.site_id = val;
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
      const list = await getServiceGatewayDetail(this.queryData);
      this.tableData = list.service_gateways;
      this.queryData.totalNum = list.total_num;
    },
    clickEditBtn(value) {
      this.$router.push({
        path: "/middleware/service/gateway/detail/" + value.service_gateway_id,
      });
    },
  },
};
</script>
<style lang="scss" scoped></style>
