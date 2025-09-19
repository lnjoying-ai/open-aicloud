<template>
  <div class="warpMain">
    <div class="itemMian">
      <el-table :data="tableData.services" style="width: 100%">
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
        <el-table-column
          :label="$t('form.serviceDescription')"
          prop="description"
        >
          <template slot-scope="scope">
            {{ scope.row.description || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.instanceNumber')" prop="inst_num">
          <template slot-scope="scope">
            {{ scope.row.inst_num || "-" }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.autoRun')" prop="auto_run">
          <template slot-scope="scope">
            {{ scope.row.auto_run ? $t("form.yes") : $t("form.no") }}
          </template>
        </el-table-column>

        <el-table-column
          :label="$t('form.applicationName')"
          prop="stack_name"
          show-overflow-tooltip
        />
        <el-table-column :label="$t('form.createTime')" prop="create_time" />
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <div
              v-if="
                userInfo.kind == '0' ||
                userInfo.kind == '1' ||
                userInfo.kind == '2'
              "
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
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getDockerStacksServices,
  delDockerServices,
  setDockerServices,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      activeInfo: {},
      activeInfoApp: "",
      activeInfoTemplate: "",
      nowMouse: "",
      sup_this: this,
      tableData: [],
      queryData: {
        stack_name: "",
        service_name: "",
        page_size: 10,
        page_num: 1,
      },
    };
  },

  created() {
    this.init();
  },
  mounted() {
    this.$route.meta.smalltitle =
      this.$t("form.serviceFromApp") + ":" + this.$route.params.name;
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  methods: {
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    async init() {
      const list = await getDockerStacksServices(
        this.$route.params.id,
        this.queryData
      );
      this.tableData = list;
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
                title: this.$t("form.serviceStopSuccess"),
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
                title: this.$t("form.serviceUpgradeSuccess"),
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
                title: this.$t("form.serviceUpgradeSuccess"),
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
                title: this.$t("form.serviceRestartSuccess"),
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
      this.$confirm(this.$t("form.confirmDeleteApp"), this.$t("form.tip"), {
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
.el-popover {
  min-width: 0px !important;
  padding: 0px !important;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}

.itemMian {
  overflow: hidden;
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
</style>
