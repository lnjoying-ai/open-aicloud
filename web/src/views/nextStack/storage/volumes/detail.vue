<template>
  <div class="subNetAddPage h-full">
    <el-form
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="160px"
    >
      <el-card class="!border-none mb-3 pb-3 mt-2">
        <template #header>
          <div class="">
            <span>{{ $t("form.basicInfo") }}</span>
          </div>
        </template>
        <div class="text item">
          <el-form-item
            :label="$t('form.name') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.id') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.volumeId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.description') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.description || "-" }}
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.type') + ':'"
            style="width: 50%; float: left"
          >
            <span v-if="form.type == 2">{{ $t("form.fileSystem") }}</span>
            <span v-else>-</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.status') + ':'"
            style="width: 50%; float: left"
          >
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getVolumeStatus(form.phaseStatus, 'tag')"
              >{{
                filtersFun.getVolumeStatus(form.phaseStatus, "status")
              }}</el-tag
            >
          </el-form-item>

          <el-form-item
            v-if="form.phaseStatus == 26"
            :label="$t('form.instance') + ':'"
            style="width: 50%; float: left"
          >
            <router-link
              v-if="form.vmInstanceId"
              :to="'/nextStack/vm/detail/' + form.vmInstanceId"
              target="_blank"
              class="text-blue-400 mr-2"
            >
              <span>{{ form.vmName || "-" }}</span>
            </router-link>
            <span v-else>-</span>
          </el-form-item>
          <el-form-item
            v-if="kind == 0 || kind == 1"
            :label="$t('form.user') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.eeUserName ? form.eeUserName : "-" }}
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.capacity') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.size || "-" }}{{ $t("unit.mem") }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.cloudDiskType') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.storagePoolName || "-" }}</span>
          </el-form-item>

          <el-form-item
            :label="$t('form.updateTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.updateTime || "-" }}
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.createTime || "-" }}
            </span>
          </el-form-item>
        </div>
      </el-card>
      <el-card class="!border-none mb-3 pb-3 mt-2">
        <template #header>
          <div class="">
            <span>{{ $t("form.snaps") }}</span>
          </div>
        </template>
        <div class="mt-4">
          <el-table
            v-loading="loading"
            :element-loading-text="$t('domain.loading')"
            :data="snapsList"
            style="width: 100%; margin-bottom: 20px"
            row-key="volumeSnapId"
            default-expand-all
            :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          >
            <el-table-column prop="name" :label="$t('form.name')">
              <template #default="scope">
                <router-link
                  :to="
                    '/nextStack/volumesSnap/detail/' + scope.row.volumeSnapId
                  "
                  class="text-blue-400"
                  >{{ scope.row.name || "-" }}</router-link
                >
              </template>
            </el-table-column>
            <el-table-column prop="current" :label="$t('form.currentSnapshot')">
              <template #default="scope">
                <span>
                  <el-tag
                    :size="$store.state.nextStack.viewSize.tagStatus"
                    :type="scope.row.isCurrent ? 'success' : 'danger'"
                    >{{
                      scope.row.isCurrent ? $t("form.yes") : $t("form.no")
                    }}</el-tag
                  >
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="phaseStatus" :label="$t('form.status')">
              <template #default="scope">
                <el-tag
                  :size="$store.state.nextStack.viewSize.tagStatus"
                  :type="
                    filtersFun.getVolumeStatus(scope.row.phaseStatus, 'tag')
                  "
                  >{{
                    filtersFun.getVolumeStatus(scope.row.phaseStatus, "status")
                  }}</el-tag
                >
              </template>
            </el-table-column>
            <el-table-column prop="createTime" :label="$t('form.createTime')" />
            <el-table-column prop="address" :label="$t('form.operation')">
              <template #default="scope">
                <span
                  class="text-blue-400 cursor-pointer"
                  @click="toDelete(scope.row)"
                  >{{ $t("form.delete") }}</span
                >
                <span
                  class="text-blue-400 cursor-pointer ml-2"
                  @click="toSwitch(scope.row)"
                  >{{ $t("form.rollBack") }}</span
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
export default {
  data() {
    return {
      filtersFun: filtersFun,
      tableData: [],
      form: {},
      formList: {
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      snapsList: [],
      loading: false,
    };
  },

  created() {
    this.getDetail(); // 获取详情
    this.getsnapsList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    toDelete(item) {
      this.$confirm(
        this.$t("message.confirmDeleteVolumeSnapshot"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .volumesSnapsDel(item.volumeSnapId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.getDetail();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    toSwitch(item) {
      this.$confirm(
        this.$t("message.confirmRestoreVolumeSnapshot"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .volumesSnapsSwitch(item.volumeSnapId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startRestore"),
                type: "success",
                duration: 2500,
              });
              this.getDetail();
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },
    getsnapsList() {
      var id = this.$route.params.id;
      mainApi.volumessnapsListDetail(id).then((res) => {
        this.snapsList = res;
      });
    },
    getDetail() {
      // 获取详情
      this.loading = true;
      var id = this.$route.params.id;
      mainApi
        .volumesDetail(id)
        .then((res) => {
          this.form = res;
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
