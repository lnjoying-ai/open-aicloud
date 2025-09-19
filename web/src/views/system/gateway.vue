<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        :model="queryData"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.activeStatus") }}:</div>
          <el-select
            v-model="queryData.active_status"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            @change="handleActiveChange"
          >
            <el-option :value="0" :label="$t('form.inactive')" />
            <el-option :value="1" :label="$t('form.active')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.onlineStatus") }}:</div>
          <el-select
            v-model="queryData.online_status"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            @change="handleOnlineChange"
          >
            <el-option :value="0" :label="$t('form.offline')" />
            <el-option :value="1" :label="$t('form.online')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.region") }}:</div>
          <el-select
            v-model="queryData.region"
            filterable
            :placeholder="$t('form.pleaseSelect')"
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
          <el-button
            v-if="userInfo.kind == '0' || userInfo.kind == '1'"
            type="primary"
            size="small"
            @click="clickAddBtn"
            >{{ $t("form.add") }}</el-button
          >
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.nodes"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column :label="$t('form.hostName')">
        <template slot-scope="scope">
          <span
            style="
              text-overflow: ellipsis;
              white-space: nowrap;
              word-wrap: normal;
              overflow: hidden;
            "
          >
            <el-tooltip
              v-if="scope.row.online_status == 1"
              effect="dark"
              content="在线"
              placement="top-end"
            >
              <img
                src="@/assets/svg/wifion.svg"
                alt=""
                width="15px"
                style="display: inline-block"
              />
            </el-tooltip>
            <el-tooltip
              v-if="scope.row.online_status == 0"
              effect="dark"
              content="离线"
              placement="top-end"
            >
              <img
                src="@/assets/svg/wifioff.svg"
                alt=""
                width="15px"
                style="display: inline-block"
              />
            </el-tooltip>
            <el-tooltip :content="scope.row.node_id" placement="bottom-start">
              <span> {{ scope.row.node_name }}</span>
            </el-tooltip>
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="host_net" :label="$t('form.address')">
        <template slot="header" slot-scope="scope">
          {{ $t("form.address") }}
          <el-tooltip
            class="item"
            effect="dark"
            :content="$t('form.externalAddress')"
            placement="top-end"
          >
            <i class="el-icon-info" />
          </el-tooltip>
        </template>
        <template slot-scope="scope">
          {{ scope.row.host_net.public_ip }}:{{
            scope.row.host_net.public_port
          }}
        </template>
      </el-table-column>

      <el-table-column :label="$t('form.activeStatus')">
        <template slot-scope="scope">
          <div v-if="scope.row.active_status == 0" class="error">
            <span>{{ $t("form.inactive") }}</span>
          </div>
          <div v-if="scope.row.active_status == 1" class="success">
            <span>{{ $t("form.active") }}</span>
          </div>
          <div v-if="scope.row.active_status == 2" class="upgrade">
            <span>{{ $t("form.upgrading") }}</span>
          </div>
        </template>
      </el-table-column>

      <el-table-column prop="regions" :label="$t('form.regionList')">
        <template slot-scope="scope">
          <span v-for="(item, index) in scope.row.regions" :key="index">
            <span v-if="index != 0">,</span>{{ item.name }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        v-if="userInfo.kind == '0' || userInfo.kind == '1'"
        :label="$t('form.operation')"
        width="100"
      >
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="clickDelBtn(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>
              <div
                v-if="scope.row.active_status == 1"
                class="icon_cz"
                @click="clickdeactiveBtn(scope.row)"
              >
                <i class="el-icon-video-pause" />
                {{ $t("form.stopUse") }}
              </div>
              <div
                v-if="scope.row.active_status == 0"
                class="icon_cz"
                @click="clickactiveBtn(scope.row)"
              >
                <i class="el-icon-video-play" />
                {{ $t("form.activate") }}
              </div>
              <div class="icon_cz" @click="clickrebootBtn(scope.row)">
                <i class="el-icon-refresh-right" />
                {{ $t("form.hardReboot") }}
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
        :total="tableData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addForm ref="addForm" :sup_this="sup_this" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  gws,
  delgws,
  getRegions,
  deactivegws,
  activegws,
  rebootgws,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./gateway/addForm";
export default {
  mixins: [initData],
  data() {
    return {
      arealist: [],
      sup_this: this,
      tableData: [],
      queryData: {
        active_status: "",
        online_status: "",
        region: "",
        page_size: 10,
        page_num: 1,
      },
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  components: {
    addForm,
  },
  watch: {},
  created() {
    this.areainit();
    this.init();
  },
  mounted() {},
  methods: {
    handleActiveChange(val) {
      this.queryData.active_status = val;
      this.init();
    },
    handleOnlineChange(val) {
      this.queryData.online_status = val;
      this.init();
    },
    handleRegionChange(val) {
      this.queryData.region = val;
      this.init();
    },
    searchinit() {
      this.queryData = {
        active_status: "",
        online_status: "",
        region: "",
        page_size: 10,
        page_num: 1,
      };
      this.init();
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
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
      const list = await gws(this.queryData);
      this.tableData = list;
      this.list = list.regions;
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteGateway"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delgws(value.node_id)
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
    clickdeactiveBtn(value) {
      this.$confirm(
        this.$t("message.confirmDisableGateway"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          deactivegws(value.node_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.executeDisableCommandSuccess"),
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
    clickactiveBtn(value) {
      this.$confirm(
        this.$t("message.confirmActivateGateway"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          activegws(value.node_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.executeActivateCommandSuccess"),
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
    clickrebootBtn(value) {
      this.$confirm(
        this.$t("message.confirmHardRebootGateway"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          rebootgws(value.node_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.executeHardRebootCommandSuccess"),
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
  },
};
</script>

<style lang="scss" scoped></style>
