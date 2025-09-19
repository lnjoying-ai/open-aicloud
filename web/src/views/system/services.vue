<template>
  <div>
    <div class="itemMian">
      <div v-if="userInfo.kind == '0' || userInfo.kind == '1'" class="head_rq">
        <el-form
          :inline="true"
          size="small"
          :model="queryData"
          class="demo-form-inline"
        >
          <el-form-item>
            <div slot="label">{{ $t("form.region") }}:</div>
            <el-select
              v-model="queryData.region"
              filterable
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
            <div slot="label">{{ $t("form.site") }}:</div>
            <el-select
              v-model="queryData.site"
              :placeholder="$t('form.pleaseSelect')"
              clearable
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
              v-model="queryData.node_id"
              :placeholder="$t('form.pleaseSelect')"
              clearable
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
            <div slot="label">{{ $t("form.status") }}:</div>
            <el-select
              v-model="queryData.status"
              :placeholder="$t('form.pleaseSelect')"
              clearable
            >
              <el-option
                v-for="(item, index) in newStatus"
                :key="index"
                :label="item.name"
                :value="item.code"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button class="add_search" @click="initform()">
              {{ $t("form.reset") }}
            </el-button>
          </el-form-item>
        </el-form>
      </div>

      <el-table
        :data="tableData.services"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column :label="$t('form.serviceName')" prop="name">
          <template slot-scope="scope">
            <router-link
              style="color: #409eff"
              :to="
                '/containerApplicationService/containerFromService/' +
                scope.row.id +
                '/' +
                scope.row.name
              "
            >
              {{ scope.row.name }}
            </router-link>
          </template>
        </el-table-column>

        <el-table-column :label="$t('form.instanceNumber')" prop="inst_num">
          <template slot-scope="scope">
            {{ scope.row.inst_num }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.status')" prop="status">
          <template slot-scope="scope">
            {{ getStatus(scope.row.status.code) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.autoRun')" prop="auto_run">
          <template slot-scope="scope">
            {{ scope.row.auto_run ? $t("form.yes") : $t("form.no") }}
          </template>
        </el-table-column>
        <el-table-column
          :label="$t('form.serviceDescription')"
          prop="description"
        >
          <template slot-scope="scope">
            {{ scope.row.description }}
          </template>
        </el-table-column>

        <el-table-column
          :label="$t('form.applicationName')"
          prop="stack_name"
        />

        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <div
              v-if="userInfo.kind == '0' || userInfo.kind == '1'"
              class="czlb"
            >
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="clickstartBtn(scope.row)">
                  <i class="el-icon-video-play" />
                  {{ $t("form.bootUp") }}
                </div>
                <div class="icon_cz" @click="clickRestartBtn(scope.row)">
                  <i class="el-icon-refresh-right" />
                  {{ $t("form.restart") }}
                </div>
                <div class="icon_cz" @click="clickstopBtn(scope.row)">
                  <i class="el-icon-video-pause" />
                  {{ $t("form.stop") }}
                </div>
                <div class="icon_cz" @click="clickUpgradeStartBtn(scope.row)">
                  <i class="el-icon-upload" />
                  {{ $t("form.startUpgrade") }}
                </div>
                <div class="icon_cz" @click="clickFinishupgrateBtn(scope.row)">
                  <i class="el-icon-circle-check" />
                  {{ $t("form.upgradeComplete") }}
                </div>
                <div class="icon_cz" @click="clickRollbackBtn(scope.row)">
                  <i class="el-icon-refresh" />
                  {{ $t("form.upgradeRollback") }}
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
            <el-button v-else size="mini">…</el-button>
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
    <addApp
      ref="addApp"
      :sup_this="sup_this"
      :template="activeInfoTemplate"
      :info="activeInfoApp"
    />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getDockerServices,
  delDockerServices,
  setDockerServices,
  getSites,
  edges,
  getRegions,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addForm from "./appTemplate/addForm";
import addApp from "./appTemplate/addApp";
export default {
  components: {
    addForm,
    addApp,
  },
  mixins: [initData],
  data() {
    return {
      activeInfo: {},
      activeInfoApp: "",
      activeInfoTemplate: "",
      nowMouse: "",
      sup_this: this,
      tableData: {},
      queryData: {
        stack_name: "",
        service_name: "",
        status: "",
        region: "",
        site: "",
        bp_id: "",
        node_id: "",
        user_id: "",
        page_size: 10,
        page_num: 1,
      },
      arealist: [],
      website: [],
      nodeList: [],
      newStatus: [
        {
          code: 1100,
          name: this.$t("statusAndType.creating"),
        },
        {
          code: 1101,
          name: this.$t("statusAndType.created"),
        },
        {
          code: 1102,
          name: this.$t("statusAndType.createFailed"),
        },
        {
          code: 1103,
          name: this.$t("statusAndType.running"),
        },
        {
          code: 1104,
          name: this.$t("statusAndType.containerExit"),
        },
        {
          code: 1105,
          name: this.$t("statusAndType.containerNotExist"),
        },
        {
          code: 1110,
          name: this.$t("statusAndType.systemException"),
        },
      ],
    };
  },
  watch: {
    nowText(val) {},
    "queryData.region": {
      deep: true,
      handler(val) {
        this.queryData.site = "";
        this.queryData.node_id = "";
        this.websiteinit();
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
    "queryData.site": {
      deep: true,
      handler(val) {
        this.queryData.node_id = "";

        this.edgesinit();
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
    "queryData.node_id": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
    "queryData.status": {
      deep: true,
      handler(val) {
        if (val != "") {
          this.queryData.page_num = 1;
          this.init();
        }
      },
    },
  },
  created() {
    this.init();
    this.areainit();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    getStatus(code) {
      return this.newStatus.find((item) => item.code === code)?.name;
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
    },
    async websiteinit() {
      const list = await getSites({ region_id: this.queryData.region });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
    },
    async edgesinit() {
      const list = await edges({ site: this.queryData.site });
      this.nodeList = list.nodes;
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
      const list = await getDockerServices(this.queryData);
      this.tableData = list;
    },
    initform() {
      this.queryData = {
        stack_name: "",
        service_name: "",
        status: "",
        region: "",
        site: "",
        node_id: "",
        bp_id: "",
        user_id: "",
        page_size: 10,
        page_num: 1,
        total_num: 0,
      };
      this.handleCurrentChange(1);
    },
    clickAddAppBtn(item) {
      this.$refs.addApp.isAdd = true;
      this.$refs.addApp.dialog = true;
    },
    clickstartBtn(item) {
      // 启动
      this.$confirm(this.$t("form.confirmStartService"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          setDockerServices(item.id, { action: "start" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.serviceStartSuccess"),
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
    clickstopBtn(item) {
      // 停止
      this.$confirm(this.$t("form.confirmStopService"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          setDockerServices(item.id, { action: "stop" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.stopSuccess"),
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
    clickUpgradeStartBtn(item) {
      // 10.18.升级服务-启动升级
      this.$confirm(
        this.$t("form.confirmUpgradeService"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setDockerServices(item.id, { action: "upgrade-start" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.upgradeStartSuccess"),
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
    clickFinishupgrateBtn(item) {
      // 10.19.升级服务-确认升级完成
      this.$confirm(
        this.$t("form.confirmUpgradeCompleteIsNext"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setDockerServices(item.id, { action: "finishupgrate" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.confirmUpgradeComplete"),
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
    clickRollbackBtn(item) {
      // 升级服务-回滚
      this.$confirm(
        this.$t("form.confirmRollbackUpgradeService"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setDockerServices(item.id, { action: "rollback" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.rollbackUpgradeServiceSuccess"),
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
    clickRestartBtn(item) {
      // 重启
      this.$confirm(
        this.$t("form.confirmRestartService"),
        this.$t("form.tip"),
        {
          confirmButtonText: this.$t("form.confirm"),
          cancelButtonText: this.$t("form.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          setDockerServices(item.id, { action: "restart" })
            .then((res) => {
              this.$notify({
                title: this.$t("form.restartSuccess"),
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
    clickDelBtn(value) {
      this.$confirm(this.$t("form.confirmDeleteService"), this.$t("form.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          delDockerServices(value.id)
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
      this.activeInfo = info;
      this.$refs.addForm.id = info.id;
      this.$refs.addForm.isAdd = false;
      this.$refs.addForm.dialog = true;
    },
  },
};
</script>

<style lang="scss" scoped>
.itemMian {
  overflow: hidden;
}
</style>
