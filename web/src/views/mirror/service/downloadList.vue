<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        :model="queryData"
        size="small"
        class="demo-form-inline"
      >
        <el-row>
          <el-form-item>
            <div slot="label">{{ $t("form.imageName") + ":" }}</div>
            <el-input
              v-model="queryData.image_name"
              :placeholder="$t('form.pleaseInputName')"
              class="w-48"
            />
          </el-form-item>
          <el-form-item>
            <div slot="label">{{ $t("form.imageRepository") + ":" }}</div>
            <el-select
              v-model="queryData.registry_id"
              filterable
              clearable
              :placeholder="$t('form.pleaseSelect')"
              class="w-48"
            >
              <el-option
                v-for="(item, index) in registriesList"
                :key="index"
                :label="item.registry_name"
                :value="item.registry_id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <div slot="label">{{ $t("form.node") + ":" }}</div>
            <el-select
              v-model="queryData.node_id"
              :placeholder="$t('form.pleaseSelect')"
              class="w-48"
              filterable
              clearable
            >
              <el-option
                v-for="(item, index) in nodeListData"
                :key="index"
                :label="item.node_name"
                :value="item.node_id"
              />
            </el-select>
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
            <el-button class="add_search" @click="searchinit()">{{
              $t("form.reset")
            }}</el-button>
          </el-form-item>
          <el-form-item
            v-if="userInfo.kind == '0' || userInfo.kind == '1'"
            class="float-right"
          >
            <el-button
              type="primary"
              size="small"
              @click="dialogStatus = true"
              >{{ $t("form.imagePreDownload") }}</el-button
            >
            <el-button type="primary" size="small" @click="delImages()">{{
              $t("form.delete")
            }}</el-button>
          </el-form-item>
        </el-row>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.images"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column
        prop="registry_name"
        :label="$t('form.imageLibraryName')"
        show-overflow-tooltip
      >
        <template slot-scope="scope">
          <span>{{
            scope.row.registry_name ? scope.row.registry_name : "-"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column
        prop="image_name"
        :label="$t('form.imageName')"
        show-overflow-tooltip
      />
      <el-table-column prop="registry_name" :label="$t('form.node')">
        <template slot-scope="scope">
          <span>{{
            scope.row.node_info
              ? scope.row.node_info.node_name
                ? scope.row.node_info.node_name
                : "-"
              : "-"
          }}</span>
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.status')" prop="status">
        <template slot-scope="scope">
          <span
            :style="{
              display: 'inline-block',
              width: '8px',
              height: '8px',
              'border-radius': '50%',
              'margin-right': '8px',
              background: status[scope.row.status.code]
                ? status[scope.row.status.code].color
                : '#FF260f',
            }"
          />
          <span style="color: #666">{{
            status[scope.row.status.code]
              ? status[scope.row.status.code].name
              : "-"
          }}</span>
        </template>
      </el-table-column>

      <el-table-column
        :label="$t('form.createTime')"
        prop="create_time"
        sortable
      />
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
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialogStatus"
      :title="$t('form.imagePreDownload')"
      width="900px"
      :close-on-clic0k-modal="false"
      :before-close="handleClose"
    >
      <el-tabs v-model="activeName">
        <el-tab-pane :label="$t('form.imageLibrary')" name="first">
          <el-form
            :inline="true"
            size="small"
            class="demo-form-inline"
            label-width="140px"
          >
            <el-form-item :label="$t('form.imageLibrary')" required>
              <el-select
                v-model="imagesID"
                :placeholder="$t('form.pleaseSelect')"
                filterable
                @change="changeImages()"
              >
                <el-option
                  v-for="(item, index) in imagesTableData"
                  :key="index"
                  :label="item.registry_name"
                  :disabled="item.status != 6"
                  :value="item.registry_id"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <pre-download ref="preDownload" :sup_this="sup_this" />
          <div class="text-right">
            <el-button type="text" size="small" @click="preDownloadCancel">{{
              $t("form.cancel")
            }}</el-button>
            <el-button
              type="primary"
              size="small"
              @click="preDownloadDoSubmit"
              >{{ $t("form.confirm") }}</el-button
            >
          </div>
        </el-tab-pane>
        <el-tab-pane :label="$t('form.thirdPartyImageLibrary')" name="second">
          <el-form
            :inline="true"
            size="small"
            class="demo-form-inline"
            label-width="140px"
          >
            <el-form-item
              :label="$t('form.thirdPartyImageLibrary') + ':'"
              required
            >
              <el-select
                v-model="thirdID"
                :placeholder="$t('form.pleaseSelect')"
                filterable
                @change="changeThird()"
              >
                <el-option
                  v-for="(item, index) in thirdTableData"
                  :key="index"
                  :label="item.registry_name"
                  :value="item.registry_id"
                />
              </el-select>
            </el-form-item>
          </el-form>
          <pre-download3 ref="preDownload3" :sup_this="sup_this" />
          <div class="text-right">
            <el-button type="text" size="small" @click="preDownload3Cancel">{{
              $t("form.cancel")
            }}</el-button>
            <el-button
              type="primary"
              size="small"
              @click="preDownload3DoSubmit"
              >{{ $t("form.confirm") }}</el-button
            >
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getImagesPreDownloads,
  registries,
  registries3rd,
  edges,
  delImagesPreDownloads,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
import preDownload3 from "./preDownload3.vue";
import preDownload from "./preDownload.vue";

export default {
  components: { preDownload3, preDownload },
  mixins: [initData],
  computed: {
    ...mapGetters(["userInfo"]),
  },
  data() {
    return {
      nodeListData: [], // 节点列表
      multipleSelection: [], // 多选
      sup_this: this,
      imagesTableData: [],
      imagesID: "",

      thirdTableData: [],
      thirdID: "",
      activeName: "first",
      downloadType: "1",
      dialogStatus: false,
      registriesList: [],
      tableData: [],
      status: {
        100: {
          name: "下载中",
          color: "#0079D1",
        },
        102: {
          name: "下载失败",
          color: "#FF260f",
        },
        101: {
          name: "下载完成",
          color: "#00AB5C",
        },
      },
      queryData: {
        registry_id: "",
        node_id: "",
        image_name: "",
        page_size: 10,
        page_num: 1,
      },
    };
  },
  watch: {},
  created() {
    this.getRegistries();
    this.getNodeList();
    this.init();
    this.gethirdTList();
    this.getImagesList();
  },
  mounted() {},
  methods: {
    delImages() {
      if (this.multipleSelection.length === 0) {
        this.$notify({
          title: this.$t("message.pleaseSelectImagePreDownloadTask"),
          type: "warning",
          duration: 2500,
        });
      } else {
        this.$confirm(
          this.$t("message.confirmDeleteImagePreDownloadTask"),
          this.$t("message.tip"),
          {
            confirmButtonText: this.$t("message.confirm"),
            cancelButtonText: this.$t("message.cancel"),
            type: "warning",
          }
        )
          .then(() => {
            delImagesPreDownloads({
              ids: this.multipleSelection
                .map((item) => item.oper_id)
                .toString(),
            })
              .then((res) => {
                this.$notify({
                  title: this.$t("message.deleteSuccess"),
                  type: "success",
                  duration: 2500,
                });
                this.init();
              })
              .catch((err) => {
                this.$notify({
                  title: err,
                  type: "error",
                  duration: 2500,
                });
              });
          })
          .catch(() => {
            this.$notify({
              title: this.$t("message.cancelDelete"),
              type: "info",
              duration: 2500,
            });
          });
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleClose(done) {
      if (this.activeName == "first") {
        this.preDownloadCancel();
      }
      if (this.activeName == "second") {
        this.preDownload3Cancel();
      }
    },
    preDownloadCancel() {
      this.$refs.preDownload.resetForm();
      this.activeName = "first";
      this.imagesID = "";
      this.thirdID = "";
      this.dialogStatus = false;
      this.init();
    },
    preDownloadDoSubmit() {
      this.$refs.preDownload.doSubmit();
    },
    preDownload3Cancel() {
      this.$refs.preDownload3.resetForm();
      this.activeName = "first";
      this.imagesID = "";

      this.thirdID = "";
      this.dialogStatus = false;
      this.init();
    },
    preDownload3DoSubmit() {
      this.$refs.preDownload3.doSubmit();
    },
    changeThird() {
      const newthirdID = this.thirdTableData.filter((res) => {
        return res.registry_id == this.thirdID;
      });
      this.$refs.preDownload3.otherRepos = [];
      this.$refs.preDownload3.add(newthirdID[0]);
    },
    changeImages() {
      const newimagesTableData = this.imagesTableData.filter((res) => {
        return res.registry_id == this.imagesID;
      });
      this.$refs.preDownload.otherRepos = [];
      this.$refs.preDownload.toData = [];
      this.$refs.preDownload.add(newimagesTableData[0]);
    },
    async gethirdTList() {
      const list = await registries3rd();
      this.thirdTableData = list.registries;
    },
    async getImagesList() {
      const list = await registries();
      this.imagesTableData = list.registries;
    },
    async getRegistries() {
      var list1 = await registries();
      const list2 = await registries3rd();

      this.registriesList = [...list1.registries, ...list2.registries];
    },
    async getNodeList() {
      const list = await edges();
      this.nodeListData = list.nodes;
    },

    searchinit() {
      this.queryData = {
        registry_id: "",
        node_id: "",
        image_name: "",
        page_size: 10,
        page_num: 1,
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
      const list = await getImagesPreDownloads(this.queryData);
      this.tableData = list;
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-table .disabledCheck .cell .el-checkbox__inner {
  display: none !important;
}

::v-deep .el-table .disabledCheck .cell::before {
  content: "";
  text-align: center;
  line-height: 37px;
}

.el-popover {
  min-width: 0px !important;
  padding: 0px !important;
}

::v-deep .el-table__expanded-cell {
  background: #f5f5f5;
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

.normal {
  color: #0f97ff;
}

.examine {
  color: #0f97ff;
}

.error {
  color: #f53333;
}

.normalCursor {
  color: #0f97ff;
  cursor: pointer;
}

.weightd {
  font-size: 30px;
  font-weight: bold;
  margin-right: 5px;
}

.el-popover {
  min-width: 0px !important;
  padding: 0px imp !important;
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
}

.icon_cz:hover {
  background: #daefff;
}

.cz_icon {
  margin-left: 16px;
  margin-right: 23px;
}

::v-deep .el-table th {
  background: rgba(0, 0, 0, 0.09);
}
</style>
