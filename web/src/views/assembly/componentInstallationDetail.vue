<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <!-- <div slot="header"
           style="display: flex;justify-content: space-between;align-items: center;">
        <span>基本信息</span>
      </div> -->
      <el-form
        ref="form"
        :model="detailMain"
        size="small"
        label-width="120px"
        :inline="true"
        label-position="right"
      >
        <el-form-item
          :label="$t('form.name') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span
            style="
              white-space: nowrap;
              overflow: hidden;
              text-overflow: ellipsis;
              max-width: 345px;
              display: block;
            "
            >{{ detailMain.name ? detailMain.name : "-" }}</span
          >
        </el-form-item>
        <el-form-item
          :label="$t('form.deploymentStatus') + ':'"
          style="width: 50%; margin-right: 0"
        >
          {{
            $t("form.success") +
            "/" +
            $t("form.deploying") +
            "/" +
            $t("form.failed") +
            "(" +
            detailMain.available_num +
            "/" +
            detailMain.processing_num +
            "/" +
            detailMain.failed_num +
            ")"
          }}
        </el-form-item>
        <el-form-item
          :label="$t('form.type') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span>{{
            detailMain.integration_type
              ? integrationList.find(
                  (item) => item.value == detailMain.integration_type
                ).label
              : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.collectionNodeCount') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span>{{ detailMain.replica_num }}</span>
        </el-form-item>

        <el-form-item
          :label="$t('form.user') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span>{{ detailMain.user_name ? detailMain.user_name : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.deploymentSiteCount') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span>
            {{
              detailMain.targets.length > 0
                ? detailMain.targets.length
                : site_total_num
            }}</span
          >
        </el-form-item>
        <el-form-item
          style="
            margin: 0px;
            display: flex;
            max-height: 64px;
            overflow-y: auto;
            width: 50%;
            float: left;
          "
        >
          <div slot="label" style="min-width: 120px; padding: 0px 12px 0px 0px">
            {{ $t("form.deploymentSite") + ":" }}
          </div>
          <span v-if="detailMain.targets.length > 0">
            <span v-for="(item, index) in detailMain.target_names" :key="index">
              <el-tag type="info" class="mr-2" size="small">{{ item }}</el-tag>
            </span>
          </span>
          <span v-else>{{ $t("form.all") }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.createTime') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span>{{
            detailMain.create_time ? detailMain.create_time : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.updateTime') + ':'"
          style="width: 50%; margin-right: 0"
        >
          <span>{{
            detailMain.update_time ? detailMain.update_time : "-"
          }}</span>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="mb-2" shadow="never">
      <div
        slot="header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <span>{{ $t("form.taskDetail") }}</span>
      </div>
      <el-row :gutter="40">
        <el-col :span="24" style="clear: both">
          <el-form
            :inline="true"
            size="small"
            :model="form"
            class="demo-form-inline"
          >
            <el-form-item>
              <div slot="label">{{ $t("form.site") }}:</div>
              <el-select
                v-model="form.site"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                clearable
                @change="handleSiteChange"
              >
                <el-option
                  v-for="(item, index) in website"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <div slot="label">{{ $t("form.node") }}:</div>
              <el-select
                v-model="form.node_id"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                clearable
                @change="handleNodeChange"
              >
                <el-option
                  v-for="(item, index) in nodeList"
                  :key="index"
                  :label="item.node_name"
                  :value="item.node_id"
                />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button class="add_search" @click="initform()">{{
                $t("form.reset")
              }}</el-button>
            </el-form-item>

            <el-form-item />
          </el-form>
          <el-table
            ref="multipleTable"
            :data="tableData.stacks"
            stripe
            highlight-current-row
            tooltip-effect="dark"
            style="width: 100%"
            max-height="450px"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column prop="name" :label="$t('form.idName')" width="360">
              <template slot-scope="scope">
                {{ scope.row.name }}

                <span class="block leading-none new-small-size"
                  >ID:{{ scope.row.id }}</span
                >
              </template>
            </el-table-column>
            <el-table-column
              prop="site_name"
              :label="$t('form.deploymentSite')"
            >
              <template slot-scope="scope">
                {{ scope.row.dst_node.dst_site_name || "-" }}
              </template>
            </el-table-column>
            <el-table-column
              prop="node_name"
              :label="$t('form.collectionNode')"
            >
              <template slot-scope="scope">
                {{ scope.row.dst_node.dst_node_name || "-" }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.status')" prop="status">
              <template slot-scope="scope">
                <span
                  v-if="
                    getStatusCode(1103).includes(scope.row.status.code) ||
                    getStatusCode(1101).includes(scope.row.status.code)
                  "
                  class="success"
                />
                <span
                  v-else-if="
                    getStatusCode(1100).includes(scope.row.status.code)
                  "
                  class="creating"
                />
                <span v-else class="error" />
                {{ getStatusName(scope.row.status.code) }}
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
          </el-table>
          <div class="flex justify-end mt-4 px-4">
            <el-pagination
              :current-page="form.page_num"
              :page-sizes="[5, 10, 20, 50, 100]"
              :page-size="form.page_size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="tableData.total_num"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  getMonitorIntegrationsDetail,
  getDeploysSystemInstances,
  getSites,
  edges,
} from "@/api/mainApi";

export default {
  components: {},
  data() {
    return {
      loadingForm: false,
      detailMain: {},
      tableData: [],
      website: [],
      nodeList: [],
      site_total_num: 0,
      form: {
        site: "",
        node_id: "",
        page_size: 10,
        page_num: 1,
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
      newStatus: [
        {
          code: 1100,
          name: this.$t("statusAndType.creating"),
          include: [0, 1, 2, 3, 4, 100, 102, 101, 5, 6],
        },
        {
          code: 1101,
          name: this.$t("statusAndType.created"),
          include: [103],
        },
        {
          code: 1102,
          name: this.$t("statusAndType.createFailed"),
          include: [50, 51, 81, 82, 122, 123, 124, 125],
        },
        {
          code: 1103,
          name: this.$t("statusAndType.running"),
          include: [104, 105, 108],
        },
        {
          code: 1104,
          name: this.$t("statusAndType.containerExited"),
          include: [60, 61, 62, 63, 64, 65, 110, 111, 112, 113],
        },
        {
          code: 1105,
          name: this.$t("statusAndType.containerNotExist"),
          include: [130, 131, 132],
        },
        {
          code: 1110,
          name: this.$t("statusAndType.systemException"),
          include: [-100, 99, 120, 190],
        },
      ],
    };
  },
  watch: {},

  async created() {},
  mounted() {
    this.init();
    this.websiteinit();
    this.edgesinit();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    getStatusCode(code) {
      return this.newStatus.find((item) => item.code == code).include;
    },
    getStatusName(code) {
      const item = this.newStatus.find((item) => item.include.includes(code));
      return item ? item.name : "-";
    },
    handleSiteChange(value) {
      this.form.site = value;
      this.form.node_id = "";
      this.edgesinit();
      this.initTable(this.detailMain.spec_id);
    },
    handleNodeChange(value) {
      this.form.node_id = value;
      this.initTable(this.detailMain.spec_id);
    },
    initform() {
      this.form = {
        site: "",
        node_id: "",
        page_size: 10,
        page_num: 1,
      };
      this.initTable(this.detailMain.spec_id);
    },
    async websiteinit() {
      const list = await getSites({ region_id: "" });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
      this.site_total_num = list.totalNum;
    },
    async edgesinit() {
      const list = await edges({ site: this.form.site });
      this.nodeList = list.nodes;
    },
    async init() {
      const list = await getMonitorIntegrationsDetail(this.$route.params.id);
      this.detailMain = list;
      this.initTable(this.detailMain.spec_id);
    },
    async initTable(id) {
      const list = await getDeploysSystemInstances(id, this.form);
      this.tableData = list;
    },
    handleSizeChange(val) {
      this.form.page_size = val;
      this.initTable(this.detailMain.spec_id);
    },
    handleCurrentChange(val) {
      this.form.page_num = val;
      this.initTable(this.detailMain.spec_id);
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

// 隐藏滚动条
* {
  scrollbar-width: thin;
}
</style>
