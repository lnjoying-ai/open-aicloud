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
          <div slot="label">{{ $t("form.name") }}:</div>
          <el-input
            v-model="queryData.repo_name"
            :placeholder="$t('form.pleaseInput')"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="init()">{{
            $t("form.search")
          }}</el-button>
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-form-item style="float: right">
          <router-link to="/appMarket/repoAdd">
            <el-button type="primary" size="small">{{
              $t("form.add")
            }}</el-button>
          </router-link>
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
      <el-table-column prop="repo_name" :label="$t('form.name')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="'/appMarket/repoDetail/' + scope.row.repo_name"
          >
            <span> {{ scope.row.repo_name ? scope.row.repo_name : "-" }}</span>
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="repo_url" :label="$t('form.url')">
        <template slot-scope="scope">
          {{ scope.row.repo_url ? scope.row.repo_url : "-" }}
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('form.status')">
        <template slot-scope="scope">
          {{
            scope.row.status == 1
              ? this.$t("form.creating")
              : scope.row.status == 2
              ? this.$t("form.dataSynchronization")
              : scope.row.status == 3
              ? this.$t("form.activated")
              : scope.row.status == 4
              ? this.$t("form.authenticationFailed")
              : scope.row.status == 5
              ? this.$t("form.dataDownloadFailed")
              : this.$t("form.unknown")
          }}
        </template>
      </el-table-column>
      <el-table-column prop="auth_method" :label="$t('form.auth')">
        <template slot-scope="scope">
          {{
            scope.row.auth_method == 0
              ? this.$t("form.authMethod.noAuth")
              : scope.row.auth_method == 1
              ? this.$t("form.authMethod.basicAuth")
              : scope.row.auth_method == 3
              ? this.$t("form.authMethod.tokenAuth")
              : this.$t("form.unknown")
          }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="130">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="clickDelBtn(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>

              <div
                v-if="scope.row.access && scope.row.access.includes('push')"
                class="icon_cz"
                @click="clickUploadBtn(scope.row)"
              >
                <i class="el-icon-upload2" />
                {{ $t("form.upload") }}
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
    <el-dialog
      :append-to-body="true"
      :visible.sync="uploadHelmDialog"
      :title="$t('form.uploadApplication')"
      width="800px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <el-form
        ref="uploadForm"
        size="small"
        :model="uploadForm"
        class="demo-form-inline"
        label-width="120px"
      >
        <el-form-item :label="$t('form.name') + ':'">
          <span>{{ nowRepo.repo_name }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.upload') + ':'">
          <el-upload
            class="upload-demo"
            action="#"
            :http-request="HttpRequest"
            :on-preview="handlePreview"
            :on-remove="handleRemove"
            :limit="1"
            :on-exceed="handleExceed"
            :before-upload="beforeUpload"
          >
            <el-button size="small" type="primary">{{
              $t("form.clickUpload")
            }}</el-button>
            <div slot="tip" class="el-upload__tip">
              {{ $t("form.uploadApplicationTip") }}
            </div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="handleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" size="small" @click="toUpload">{{
          $t("form.confirm")
        }}</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { helmRepos, delHelmRepos, helmUploadCharts } from "@/api/mainApi";

export default {
  components: {},
  data() {
    return {
      fileList: "",
      uploadForm: {},
      nowRepo: "", // 当前操作的仓库
      uploadHelmDialog: false, // 上传弹窗
      queryData: {
        repo_name: "", // 仓库名称
        page_size: 10,
        page_num: 1,
      },
      tableDataTotal: 0,
      tableData: [],
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  watch: {},
  created() {
    this.init();
  },
  mounted() {},
  methods: {
    getBase64(file) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => resolve(reader.result);
        reader.onerror = (error) => reject(error);
      });
    },
    HttpRequest(file) {
      this.getBase64(file.file).then((resBase64) => {
        this.fileList = resBase64.split(",")[1]; // 直接拿到base64信息
      });
    },

    toUpload() {
      if (this.fileList.length == 0) {
        this.$notify({
          title: this.$t("form.pleaseUploadFile"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      var data = {
        package: this.fileList,
      };
      helmUploadCharts(data, this.nowRepo.repo_name)
        .then((res) => {
          this.$notify({
            title: this.$t("message.uploadSuccess"),
            type: "success",
            duration: 2500,
          });
          this.handleClose();
        })
        .catch((err) => {});
    },
    handleRemove(file, fileList) {
      this.fileList = "";
    },
    handlePreview(file) {},
    handleExceed(files, fileList) {
      this.$notify({
        title: this.$t("message.maxUploadFile"),
        type: "warning",
        duration: 2500,
      });
    },
    // 限制文件类型
    beforeUpload(file) {
      var FileExt = file.name.replace(/.+\./, "");
      if (["tgz"].indexOf(FileExt.toLowerCase()) === -1) {
        this.$notify({
          title: this.$t("form.pleaseUploadFileTgz"),
          type: "warning",
          duration: 2500,
        });
        return false;
      }

      const isLt1M = file.size / 1024 / 1024 < 1;
      if (!isLt1M) {
        this.$notify({
          title: this.$t("form.uploadFileSize"),
          type: "error",
          duration: 2500,
        });
      }
      return isLt1M;
    },
    beforeRemove(file, fileList) {
      return this.$confirm(this.$t("form.remove") + ` ${file.name}？`);
    },
    handleClose() {
      this.uploadHelmDialog = false;
      this.nowRepo = "";
      this.fileList = "";
    },
    clickUploadBtn(item) {
      this.nowRepo = item;
      this.uploadHelmDialog = true;
    },
    clickDelBtn(value) {
      this.$confirm(this.$t("form.deleteRepoConfirm"), this.$t("message.tip"), {
        confirmButtonText: this.$t("form.confirm"),
        cancelButtonText: this.$t("form.cancel"),
        type: "warning",
      })
        .then(() => {
          delHelmRepos(value.repo_name)
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
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await helmRepos(this.queryData);
      this.tableData = list.repos;
      this.tableDataTotal = list.total_num;
    },
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
        repo_name: "", // 仓库名称
        page_size: 10,
        page_num: 1,
      };
      this.init();
    },
  },
};
</script>

<style lang="scss" scoped></style>
