<template>
  <div class="warpMain">
    <div>
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <!-- <el-form-item>
          <div slot="label">名称:</div>
          <el-input v-model="queryData.name"
                    placeholder="请输入名称" />
        </el-form-item> -->
        <el-form-item>
          <div slot="label">{{ $t("form.region") }}:</div>
          <el-select
            v-model="queryData.region"
            :placeholder="$t('form.pleaseSelect')"
            filterable
            clearable
            @change="handleRegionChange"
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
          <div slot="label">{{ $t("form.site") }}:</div>
          <el-select
            v-model="queryData.site"
            :placeholder="$t('form.pleaseSelect')"
            filterable
            clearable
            @change="handleWebsiteChange"
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
          v-if="
            userInfo.kind == '0' || userInfo.kind == '1' || userInfo.kind == '2'
          "
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
      v-loading="loading"
      :data="tableData.clouds"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :element-loading-text="$t('domain.loading')"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" :label="$t('form.name')" width="200">
        <template slot-scope="scope">
          <span>
            <span class="block leading-none">
              {{
                scope.row.region_name +
                "-" +
                scope.row.site_name +
                "-" +
                scope.row.name
              }}</span
            >
            <span class="block leading-none new-small-size"
              >ID:{{ scope.row.cloud_id }}</span
            >
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="site_name" :label="$t('form.regionSite')">
        <template slot-scope="scope">
          {{ scope.row.region_name ? scope.row.region_name : "-" }}/
          {{ scope.row.site_name ? scope.row.site_name : "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.instance')" prop="vm_instance_num">
        <template slot-scope="scope">
          {{ scope.row.metrics.vm_instance_num }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('form.baremetalDevice')"
        prop="baremetal_device_num"
      >
        <template slot-scope="scope">
          {{ scope.row.metrics.baremetal_device_num }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('form.baremetalInstance')"
        prop="baremetal_instance_num"
      >
        <template slot-scope="scope">
          {{ scope.row.metrics.baremetal_instance_num }}
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('form.computeNodeCount')"
        prop="hypervisor_node_num"
      >
        <template slot-scope="scope">
          {{ scope.row.metrics.hypervisor_node_num }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.healthStatus')">
        <template slot-scope="scope">
          <div v-if="scope.row.health_status.code == 0">
            <span>
              <svg
                t="1678691045357"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="26954"
                width="16"
                height="16"
              >
                <path
                  d="M829.248 420.576C815.264 274.56 693.696 160 544 160c-115.808 0-214.944 68.736-260.672 167.36A142.72 142.72 0 0 0 240.032 320a144.032 144.032 0 0 0-144 144c0 15.808 3.168 30.752 7.872 44.928C42.048 544.992 0.032 611.168 0.032 687.936a208 208 0 0 0 208 208V896H784v-0.064c132.576 0 240-107.424 240-240 0-116.992-83.872-214.176-194.752-235.36zM784 831.936V832H208.032a144.256 144.256 0 0 1-144-144.064c0-51.2 26.976-97.44 72.128-123.744 43.872-25.184 46.88-30.176 28.48-75.424a78.56 78.56 0 0 1-4.608-24.736c0-44.128 35.872-80 80-80 0 0 20.992-1.504 43.296 7.36 36.704 14.624 40.704 0.64 58.048-37.088C378.08 275.168 457.6 224 544 224c115.2 0 210.432 87.136 221.568 202.688 3.936 45.824 3.936 45.824 51.68 56.736A175.872 175.872 0 0 1 960 655.936c0 97.056-78.944 176-176 176z"
                  fill="#12ca3e"
                  p-id="26955"
                />
              </svg>
            </span>
            <span>
              {{ cloudHealthStatus[scope.row.health_status.code] }}
            </span>
          </div>
          <div v-if="scope.row.health_status.code == 1">
            <span>
              <svg
                t="1678690588998"
                class="icon"
                viewBox="0 0 1024 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="11208"
                width="16"
                height="16"
              >
                <path
                  d="M213.333333 874.666667C96 874.666667 0 778.666667 0 661.333333c0-76.8 42.666667-147.2 106.666667-185.6v-17.066666c0-100.266667 81.066667-181.333333 181.333333-181.333334 21.333333 0 44.8 4.266667 66.133333 12.8C403.2 192 505.6 128 618.666667 128c164.266667 0 298.666667 134.4 298.666666 298.666667v17.066666c66.133333 42.666667 106.666667 117.333333 106.666667 196.266667 0 115.2-85.333333 213.333333-198.4 232.533333-4.266667 2.133333-10.666667 2.133333-14.933333 2.133334H213.333333zM170.666667 541.866667c-51.2 19.2-85.333333 66.133333-85.333334 119.466666 0 70.4 57.6 128 128 128h593.066667c74.666667-8.533333 132.266667-72.533333 132.266667-149.333333 0-57.6-34.133333-110.933333-85.333334-134.4-17.066667-8.533333-27.733333-25.6-23.466666-44.8 2.133333-12.8 2.133333-23.466667 2.133333-34.133333 0-117.333333-96-213.333333-213.333333-213.333334-93.866667 0-174.933333 59.733333-202.666667 149.333334-4.266667 12.8-14.933333 23.466667-27.733333 27.733333-12.8 4.266667-27.733333 2.133333-38.4-6.4-17.066667-12.8-38.4-21.333333-59.733334-21.333333C234.666667 362.666667 192 405.333333 192 458.666667c0 10.666667 2.133333 19.2 4.266667 29.866666 6.4 21.333333-4.266667 44.8-25.6 53.333334z"
                  fill="#f14c06"
                  p-id="11209"
                />
                <path
                  d="M618.666667 384c36.266667 0 64 27.733333 64 64 0 23.466667 19.2 42.666667 42.666666 42.666667s42.666667-19.2 42.666667-42.666667c0-83.2-66.133333-149.333333-149.333333-149.333333-23.466667 0-42.666667 19.2-42.666667 42.666666s19.2 42.666667 42.666667 42.666667"
                  fill="#f14c06"
                  p-id="11210"
                />
              </svg>
            </span>
            <span>
              {{ cloudHealthStatus[scope.row.health_status.code] }}
            </span>
          </div>
          <div v-if="scope.row.health_status.code == 2">
            <span>
              <svg
                t="1677209107157"
                class="icon"
                viewBox="0 0 1027 1024"
                version="1.1"
                xmlns="http://www.w3.org/2000/svg"
                p-id="8295"
                width="16"
                height="16"
              >
                <path
                  d="M776.533333 870.4l8.533334-4.266667c121.6-29.866667 213.333333-110.933333 236.8-211.2 12.8-57.6 2.133333-104.533333-2.133334-123.733333-27.733333-108.8-121.6-187.733333-236.8-198.4-10.666667 0-21.333333-2.133333-29.866666 0-51.2-93.866667-149.333333-153.6-256-153.6-142.933333 0-264.533333 104.533333-288 241.066667-113.066667 10.666667-202.666667 110.933333-202.666667 228.266666C6.4 770.133333 104.533333 874.666667 228.266667 874.666667H746.666667m0-64H228.266667c-87.466667 0-157.866667-76.8-157.866667-162.133334 0-87.466667 70.4-162.133333 157.866667-162.133333 2.133333 0 2.133333-2.133333 4.266666-2.133333l34.133334 2.133333 2.133333-32c8.533333-119.466667 108.8-211.2 228.266667-211.2 89.6 0 170.666667 53.333333 209.066666 134.4l10.666667 21.333333 23.466667-2.133333c12.8-2.133333 23.466667-2.133333 38.4-2.133333 87.466667 8.533333 157.866667 68.266667 179.2 151.466666 4.266667 12.8 12.8 49.066667 2.133333 91.733334-17.066667 74.666667-91.733333 140.8-185.6 166.4"
                  fill="#9EB5C7"
                  p-id="8296"
                />
                <path
                  d="M544 657.066667h-55.466667c0-21.333333 2.133333-38.4 6.4-49.066667 6.4-12.8 17.066667-25.6 36.266667-38.4 17.066667-12.8 25.6-25.6 25.6-42.666667 0-14.933333-4.266667-25.6-12.8-34.133333-6.4-6.4-17.066667-8.533333-27.733333-8.533333-12.8 0-23.466667 4.266667-32 10.666666-8.533333 6.4-12.8 19.2-14.933334 38.4h-59.733333c0-34.133333 10.666667-59.733333 34.133333-76.8 19.2-14.933333 40.533333-21.333333 68.266667-21.333333 34.133333 0 61.866667 8.533333 81.066667 25.6 19.2 14.933333 27.733333 38.4 27.733333 66.133333 0 17.066667-4.266667 32-12.8 42.666667-6.4 10.666667-19.2 21.333333-38.4 34.133333-10.666667 6.4-17.066667 12.8-19.2 19.2-4.266667 8.533333-6.4 19.2-6.4 34.133334z m4.266667 27.733333v59.733333h-61.866667v-59.733333h61.866667z"
                  fill="#9EB5C7"
                  p-id="8297"
                />
              </svg>
            </span>
            <span>
              {{ cloudHealthStatus[scope.row.health_status.code] }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.status')">
        <template slot-scope="scope">
          <el-tag
            size="small"
            :type="filtersFun.getCloudStatus(scope.row.status.code, 'tag')"
            >{{
              filtersFun.getCloudStatus(scope.row.status.code, "status")
            }}</el-tag
          >
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.type')">
        <template slot-scope="scope">
          {{ scope.row.product ? scope.row.product : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="update_time" :label="$t('form.updateTime')" />
      <el-table-column
        v-if="kind == '0' || kind == '1'"
        :label="$t('form.operation')"
        width="100"
      >
        <template slot-scope="scope">
          <div
            v-if="showBtn(scope.row, [3, 11, 12, 13, 21, 31, 50, 51])"
            class="czlb"
          >
            <el-popover placement="bottom" width="110" trigger="click">
              <div
                v-if="showBtn(scope.row, [3])"
                class="icon_cz"
                @click="clickActionBtn(scope.row, 'open_test')"
              >
                <i class="icon-public-beta" />
                {{ $t("form.publicTest") }}
              </div>
              <div
                v-if="showBtn(scope.row, [11, 12, 13])"
                class="icon_cz"
                @click="clickActionBtn(scope.row, 'maintain')"
              >
                <i class="icon-houqiweihuweihuweihuguanli" />
                {{ $t("form.maintain") }}
              </div>
              <div
                v-if="showBtn(scope.row, [21])"
                class="icon_cz"
                @click="clickActionBtn(scope.row, 'complete_maintain')"
              >
                <i class="icon-baoxiuwanchengshuai" />
                {{ $t("form.completeMaintain") }}
              </div>
              <div
                v-if="showBtn(scope.row, [3, 11])"
                class="icon_cz"
                @click="clickActionBtn(scope.row, 'shelve')"
              >
                <i class="icon-shangjia" />
                {{ $t("form.shelve") }}
              </div>
              <div
                v-if="showBtn(scope.row, [12, 13])"
                class="icon_cz"
                @click="clickActionBtn(scope.row, 'off-shelve')"
              >
                <i class="icon-xiajiaxiawu" />
                {{ $t("form.offShelve") }}
              </div>
              <div
                v-if="showBtn(scope.row, [11, 12])"
                class="icon_cz"
                @click="clickActionBtn(scope.row, 'pre-off-shelve')"
              >
                <i class="icon-xiajiaxiawu" />
                {{ $t("form.preOffShelve") }}
              </div>
              <div class="icon_cz" @click="clickEditBtn(scope.row)">
                <i class="el-icon-edit" />
                {{ $t("form.edit") }}
                <!-- v-if="showBtn(scope.row, [3, 21, 50, 51])" -->
              </div>
              <div
                v-if="showBtn(scope.row, [3, 11, 31])"
                class="icon_cz"
                @click="clickDelBtn(scope.row)"
              >
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>
              <div
                v-if="showBtn(scope.row, [3, 11, 31])"
                class="icon_cz"
                @click="clickForceDelBtn(scope.row)"
              >
                <i class="el-icon-delete-solid" />
                {{ $t("form.forceDelete") }}
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
        :total="totalNum"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addForm ref="addForm" :sup_this="sup_this" :info="activeInfo" />
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getCloudys, delClouds, actionCloudys } from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./cloudy/addForm";
import { getRegions, getSites } from "@/api/mainApi";
import filtersFun from "@/utils/statusFun";

export default {
  components: {
    addForm,
  },
  mixins: [initData],
  data() {
    return {
      filtersFun: filtersFun,
      sup_this: this,
      tableData: [],
      queryData: {
        region: "",
        site: "",
        page_size: 10,
        page_num: 1,
        name: "",
      },
      activeInfo: {},
      totalNum: 0,
      arealist: [], // 区域列表
      website: [], // 站点列表
      loading: false,
      cloudHealthStatus: {
        0: this.$t("statusAndType.healthy"),
        1: this.$t("statusAndType.unhealthy"),
        2: this.$t("statusAndType.unknown"),
      },
    };
  },
  async created() {
    this.init();
  },
  mounted() {
    this.areainit();
    this.websiteinit();
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  methods: {
    showBtn(item, data) {
      // data是否包含 status.code
      if (item.status && data.includes(item.status.code)) {
        return true;
      } else {
        return false;
      }
    },
    // 获取区域数据
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
    },
    // 获取站点数据
    async websiteinit() {
      const list = await getSites({ region_id: this.queryData.region });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
    },
    handleRegionChange(val) {
      this.queryData.region = val;
      this.queryData.site = "";
      this.websiteinit();
      this.init();
    },
    handleWebsiteChange(val) {
      this.queryData.site = val;
      this.init();
    },
    searchinit() {
      this.queryData = {
        page_size: 10,
        page_num: 1,
        name: "",
        region: "",
        site: "",
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
      this.loading = true;
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getCloudys(this.queryData);
      this.tableData = list;
      this.totalNum = list.total_num;
      this.loading = false;
    },
    clickAddBtn() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
      this.init();
    },
    clickDelBtn(value) {
      this.$confirm(this.$t("message.deleteConfirm"), this.$t("message.tip"), {
        confirmButtonText: this.$t("message.confirm"),
        cancelButtonText: this.$t("message.cancel"),
        type: "warning",
      })
        .then(() => {
          delClouds(value.cloud_id, false)
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
    clickForceDelBtn(value) {
      this.$confirm(
        this.$t("message.forceDeleteConfirm"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delClouds(value.cloud_id, true)
            .then((res) => {
              if (res != undefined) {
                this.$notify({
                  title: this.$t("message.forceDeleteSuccess"),
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
      // 编辑
      this.activeInfo = info;
      this.$refs.addForm.id = info.cloud_id;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
    },
    clickActionBtn(info, action) {
      // open_test
      // maintain
      // complete_maintain
      // shelve
      // off-shelve
      // pre-off-shelve
      var text = "";
      switch (action) {
        case "open_test":
          text = this.$t("statusAndType.publicTest");
          break;
        case "maintain":
          text = this.$t("form.maintain");
          break;
        case "complete_maintain":
          text = this.$t("form.completeMaintain");
          break;
        case "shelve":
          text = this.$t("form.shelve");
          break;
        case "off-shelve":
          text = this.$t("form.offShelve");
          break;
        case "pre-off-shelve":
          text = this.$t("form.preOffShelve");
          break;
      }
      this.$confirm(
        this.$t("message.actionConfirm", { text: text }),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          actionCloudys(info.cloud_id, { action: action })
            .then((res) => {
              if (res != undefined) {
                this.$notify({
                  title: this.$t("message.operationSuccess"),
                  type: "success",
                  duration: 2000,
                });
              }
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
  },
};
</script>
<style lang="scss" scoped></style>
