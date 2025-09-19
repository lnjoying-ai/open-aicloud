<template>
  <div>
    <div class="head_rq">
      <el-form
        :inline="true"
        :model="queryData"
        size="small"
        class="demo-form-inline"
      >
        <el-row>
          <el-form-item>
            <div slot="label">{{ $t("form.name") }}</div>
            <el-input
              v-model="queryData.registry_name"
              :placeholder="$t('form.pleaseInputName')"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              class="add_search"
              type="primary"
              @click="handleCurrentChange(1)"
              >{{ $t("form.query") }}</el-button
            >
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-refresh-left"
              class="add_search"
              @click="searchinit()"
              >{{ $t("form.reset") }}</el-button
            >
          </el-form-item>
        </el-row>
        <el-row style="float: right">
          <el-form-item>
            <el-button type="primary" size="small" @click="clickAddUser">{{
              $t("form.addUser")
            }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="downMirror">{{
              $t("form.imagePreDownload")
            }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="changePassword">{{
              $t("form.modifyAdminPassword")
            }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" size="small" @click="clickAddWarehouse">{{
              $t("form.addWarehouse")
            }}</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.registries"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
      @select="dialogCheck"
    >
      <el-table-column type="selection" width="55" />

      <el-table-column
        prop="registry_name"
        :label="$t('form.name')"
        show-overflow-tooltip
      />
      <el-table-column prop="type" :label="$t('form.type')">
        <template slot-scope="scope">
          {{
            !scope.row.type || scope.row.type == "0" ? "harbor" : "huawei-SWR"
          }}
        </template>
        <!--  -->
      </el-table-column>
      <el-table-column prop="url" :label="$t('form.url')" />
      <el-table-column :label="$t('form.status')" prop="status">
        <template slot-scope="scope">
          {{ status[scope.row.status] }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.createTime')" prop="create_time" />
      <el-table-column :label="$t('form.updateTime')" prop="update_time" />
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="clickactiveBtn(scope.row)">
                <i class="el-icon-video-play" />
                {{ $t("form.enable") }}
              </div>
              <div class="icon_cz" @click="clickdeactiveBtn(scope.row)">
                <i class="el-icon-video-pause" />
                {{ $t("form.stopUse") }}
              </div>
              <div class="icon_cz" @click="checkWarehouse(scope.row)">
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
        :total="tableData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addUser ref="addUser" :sup_this="sup_this" />
    <addWarehouse ref="addWarehouse" />
    <changePassword ref="changePassword" />
    <download ref="download" />
  </div>
</template>

<script>
import {} from "vuex";
import {
  registries,
  delRegistries,
  getRegions,
  deactivegck,
  activegck,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import addWarehouse from "./service/addWarehouse";
import addUser from "./service/addUser";
import changePassword from "./service/changePassword";
import download from "./service/download";

export default {
  components: {
    addWarehouse,
    addUser,
    changePassword,
    download,
  },
  mixins: [initData],
  data() {
    return {
      arealist: [],
      sup_this: this,
      tableData: [],
      status: {
        1: "creating",
        2: "failed",
        3: "succeed",
        4: "unhealthy",
        5: "healthy",
        6: "enable",
        7: "disable",
        "-1": "deleted",
      },
      queryData: {
        active_status: "",
        online_status: "",
        region: "",
        page_size: 10,
        page_num: 1,
      },
      selectioned: {}, // 选中行
    };
  },
  watch: {
    "queryData.active_status": {
      deep: true,
      handler(val) {
        if (val !== "") {
          this.init();
        }
      },
    },
    "queryData.online_status": {
      deep: true,
      handler(val) {
        if (val !== "") {
          this.init();
        }
      },
    },
    "queryData.region": {
      deep: true,
      handler(val) {
        if (val !== "") {
          this.init();
        }
      },
    },
  },
  created() {
    this.areainit();

    this.init();
  },
  mounted() {},
  methods: {
    handleSelectionChange(e) {},
    dialogCheck(selection, row) {
      this.$refs.multipleTable.clearSelection();
      if (selection.length === 0) {
        return;
      }
      if (row) {
        this.selectioned = row;
        this.$refs.multipleTable.toggleRowSelection(row, true);
      }
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
      const list = await registries(this.queryData);
      this.tableData = list;
      this.list = list.registries;
    },
    clickDelBtn(value) {
      this.$confirm(
        this.$t("message.confirmDeleteWarehouse"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delRegistries(value.registry_id)
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
    clickdeactiveBtn(value) {
      this.$confirm(
        this.$t("message.confirmDisableWarehouse"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          deactivegck(value.registry_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.stopUseSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickactiveBtn(value) {
      this.$confirm(
        this.$t("message.confirmEnableWarehouse"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          activegck(value.registry_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.activateSuccess"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    // 添加镜像
    clickAddWarehouse() {
      this.$refs.addWarehouse.add();
    },
    // 查看仓库
    checkWarehouse(id) {
      this.$refs.addWarehouse.check(JSON.parse(JSON.stringify(id)));
    },
    // 添加用户
    clickAddUser() {
      this.$refs.addUser.add();
    },
    // 镜像预下载
    downMirror() {
      if (!this.selectioned || JSON.stringify(this.selectioned) === "{}") {
        this.$notify({
          title: this.$t("message.pleaseSelect"),
          type: "warning",
          duration: 2500,
        });
      } else {
        this.$refs.download.add(this.selectioned);
      }
    },
    // 修改管理员密码
    changePassword() {
      if (!this.selectioned || JSON.stringify(this.selectioned) === "{}") {
        this.$notify({
          title: this.$t("message.pleaseSelect"),
          type: "warning",
          duration: 2500,
        });
      } else {
        this.$refs.changePassword.add(this.selectioned);
      }
    },
  },
};
</script>

<style lang="scss" scoped></style>
