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
          <div slot="label">{{ $t("form.componentType") }}:</div>

          <el-select
            v-model="queryData.integration_type"
            :placeholder="$t('form.pleaseSelectComponentType')"
            @change="handleCurrentChange(1)"
          >
            <el-option
              v-for="(item, index) in integrationList"
              :key="index"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <!-- <el-form-item>
          <el-button icon="el-icon-search"
                     class="add_search"
                     type="primary"
                     @click="handleCurrentChange(1)">查询
          </el-button>
        </el-form-item> -->
        <el-form-item>
          <el-button class="add_search" @click="searchinit()">
            {{ $t("form.reset") }}
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
      <el-table-column
        prop="name"
        :label="$t('form.name')"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <router-link
            :to="'/component/installation/' + scope.row.id"
            style="
              text-decoration: none;
              font-size: 14px;
              cursor: pointer;
              color: rgb(64, 158, 255);
            "
          >
            {{ scope.row.name ? scope.row.name : "" }}
          </router-link>
        </template>
      </el-table-column>

      <el-table-column :label="$t('form.monitoringComponentType')">
        <template slot-scope="scope">
          {{
            integrationList.find(
              (item) => item.value == scope.row.integration_type
            ).label
          }}
        </template>
      </el-table-column>

      <el-table-column
        prop="site_name"
        :label="$t('form.deploymentSiteCount')"
        sortable
      >
        <template slot-scope="scope">
          <span>
            {{
              scope.row.targets.length > 0
                ? scope.row.targets.length
                : $t("form.all")
            }}</span
          >
        </template>
      </el-table-column>

      <el-table-column prop="status" :label="$t('form.deploymentStatus')">
        <template slot-scope="scope">
          {{ scope.row.status ? getStatus[scope.row.status] : "-" }}
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
      <el-table-column
        v-if="kind == '0' || kind == '1'"
        :label="$t('form.operation')"
        width="100"
      >
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <!-- <div class="icon_cz"
                   @click="reassembly(scope.row)">
                <i class="el-icon-edit"></i>
                重装
              </div> -->
              <div class="icon_cz" @click="unload(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.uninstall") }}
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
        :total="queryData.totalNum"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getMonitorIntegrations,
  uninstallMonitorIntegrations,
  reinstallMonitorIntegrations,
} from "@/api/mainApi";
import initData from "@/mixins/initData";

export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      tableData: [],
      integrationList: [
        {
          value: "1",
          label: this.$t("form.lightweightNode"),
        },
        {
          value: "2",
          label: this.$t("form.nextStackCloudMonitoring"),
        },
        {
          value: "3",
          label: this.$t("form.openStackCloudMonitoring"),
        },
        {
          value: "4",
          label: this.$t("form.gpuMonitoring"),
        },
        {
          value: "5",
          label: this.$t("form.installFromTemplate"),
        },
        {
          value: "6",
          label: this.$t("form.custom"),
        },
      ],
      queryData: {
        page_size: 10,
        page_num: 1,
        integration_type: "", // 集成类型
        totalNum: 0,
      },

      getStatus: {
        1: this.$t("statusAndType.creating"),
        2: this.$t("statusAndType.running"),
        3: this.$t("statusAndType.success"),
        4: this.$t("statusAndType.failed"),
        5: this.$t("statusAndType.processing"),
        11: this.$t("statusAndType.portMappingSuccess"),
        12: this.$t("statusAndType.portMappingFailed"),
        13: this.$t("statusAndType.portRemove"),
        21: this.$t("statusAndType.collectAdd"),
        22: this.$t("statusAndType.collectRemove"),
        30: this.$t("statusAndType.terminating"),
        31: this.$t("statusAndType.terminated"),
        32: this.$t("statusAndType.deleted"),
      },
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
        integration_type: "",
        totalNum: 0,
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
      delete this.queryData.totalNum;
      const list = await getMonitorIntegrations(this.queryData);
      this.tableData = list.integrations;
      this.queryData.totalNum = list.total_num;
    },
    unload(value) {
      this.$confirm(
        this.$t("message.confirmUninstallComponent"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          uninstallMonitorIntegrations(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startUninstall"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    reassembly(value) {
      this.$confirm(
        this.$t("message.confirmReassemblyComponent"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          reinstallMonitorIntegrations(value.id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startReassembly"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickEditBtn(info) {},
  },
};
</script>

<style lang="scss" scoped></style>
