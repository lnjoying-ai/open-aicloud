<template>
  <div class="warpMain">
    <div>
      <el-form
        :model="form"
        :inline="true"
        :size="$store.state.nextStack.viewSize.main"
        class="el-form demo-form-inline el-form--inline"
      >
        <el-form-item :label="$t('form.name') + ':'">
          <el-input
            v-model="form.name"
            class="w-48"
            :placeholder="$t('form.pleaseInputImageName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.type') + ':'">
          <el-select
            v-model="form.is_vm"
            class="w-48"
            :placeholder="$t('form.pleaseSelectImageType')"
            @change="onSubmit"
          >
            <el-option :label="$t('form.all')" :value="''" />
            <el-option :label="$t('form.instance')" :value="true" />
            <el-option :label="$t('form.bareMetal')" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="onSubmit">{{
            $t("form.query")
          }}</el-button>
          <el-button class="resetBtn" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <!-- <el-button type="primary"
                   class="float-right"
                   @click="addEipPool">创建镜像</el-button> -->
      </el-form>
    </div>
    <div>
      <el-table
        v-loading="loading"
        :element-loading-text="$t('domain.loading')"
        :data="tableData"
        max-height="calc(100% - 3rem)"
        class="!overflow-y-auto"
        stripe
        :scrollbar-always-on="false"
      >
        <el-table-column prop="date" :label="$t('form.name')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row)"
              >{{ scope.row.imageName }}</span
            >
          </template>
        </el-table-column>
        <el-table-column prop="imageOsType" :label="$t('form.os')">
          <template #default="scope">
            <span v-if="scope.row.imageOsType == 0">linux</span>
            <span v-else-if="scope.row.imageOsType == 1">windows</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isPublic" :label="$t('form.isPublic')">
          <template #default="scope">
            <span v-if="scope.row.isPublic">{{ $t("form.yes") }}</span>
            <span v-else>{{ $t("form.no") }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="imageFormat" :label="$t('form.imageType')">
          <template #default="scope">
            <span v-if="scope.row.imageFormat == 4">{{
              $t("form.bareMetal")
            }}</span>
            <span v-else-if="scope.row.imageFormat == 3">{{
              $t("form.instance")
            }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getVolumeStatus(scope.row.phaseStatus, 'tag')"
              >{{
                filtersFun.getVolumeStatus(scope.row.phaseStatus, "status")
              }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column
          v-if="kind == 0 || kind == 1"
          prop="eeUserName"
          :label="$t('form.user')"
        >
          <template #default="scope">
            <span>{{ scope.row.eeUserName ? scope.row.eeUserName : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />
        <el-table-column :label="$t('form.operation')" width="120">
          <template #default="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="toEdit(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
                </div>
                <div class="icon_cz" @click="toDelete(scope.row)">
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
    </div>
    <el-pagination
      class="flex justify-end mt-4 px-4"
      :page_num="form.page_num"
      :page-size="form.page_size"
      :page-sizes="$store.state.nextStack.page_sizes"
      :current-page="form.page_num"
      layout="total, sizes, prev, pager, next, jumper"
      :total="form.total"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <el-dialog
      :title="$t('form.editImage')"
      :visible.sync="editDialogVisible"
      width="500px"
      :before-close="editHandleClose"
    >
      <imageEdit :id="editId" ref="imageEditRef" @close="editHandleClose" />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import imageEdit from "./edit.vue";

export default {
  components: {
    imageEdit,
  },
  data() {
    return {
      editId: "",
      editDialogVisible: false,
      filtersFun: filtersFun,
      timer: "",
      loading: false,
      form: {
        name: "",
        is_vm: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      tableData: [],
    };
  },
  created() {
    this.getImagesList();
  },
  mounted() {
    this.timer = setInterval(async () => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getImagesListTime();
      }
    }, this.$store.state.nextStack.listRefreshTime);
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer);
  },
  methods: {
    toEdit(item) {
      // 编辑镜像
      this.editId = item.imageId;
      this.editDialogVisible = true;
    },
    toDelete(item) {
      // 确认删除该镜像吗？
      this.$confirm(
        this.$t("message.confirmDeleteImage"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          // 删除
          mainApi
            .imageDel(item.imageId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getImagesList();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    editHandleClose() {
      this.editId = "";
      this.editDialogVisible = false;
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      console.log(`${val} items per page`);
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.form.page_size = val;
      this.getImagesList();
    },
    handleCurrentChange(val) {
      // 改变页码
      console.log(`current page: ${val}`);
      this.form.page_num = val;
      this.getImagesList();
    },
    toDetail(row) {
      // 跳转详情
      this.$router.push({
        path: `/nextStack/privateImage/detail/${row.imageId}`,
      });
    },
    onSubmit() {
      // 提交查询
      this.form.page_num = 1;
      this.getImagesList();
    },
    onReset() {
      // 重置查询
      this.form.name = "";
      this.form.is_vm = "";
      this.form.page_num = 1;
      this.getImagesList();
    },
    getImagesList() {
      // images列表
      this.loading = true;

      const params = {
        name: this.form.name,
        is_vm: this.form.is_vm,
        is_public: false,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
      };
      for (const key in params) {
        if (
          params[key] === null ||
          params[key] === undefined ||
          params[key] === ""
        ) {
          delete params[key];
        }
      }

      mainApi
        .imageList(params)
        .then((res) => {
          this.loading = false;
          this.tableData = res.images;
          this.form.total = res.totalNum;
        })
        .catch(() => {
          this.loading = false;
        });
    },
    getImagesListTime() {
      // images列表
      const params = {
        name: this.form.name,
        is_vm: this.form.is_vm,
        is_public: false,
        page_num: this.form.page_num,
        page_size: this.form.page_size,
      };
      for (const key in params) {
        if (
          params[key] === null ||
          params[key] === undefined ||
          params[key] === ""
        ) {
          delete params[key];
        }
      }
      mainApi.imageList(params).then((res) => {
        this.tableData = res.images;
        this.form.total = res.totalNum;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetPage {
}
</style>
