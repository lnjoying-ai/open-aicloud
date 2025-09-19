<template>
  <div class="subNetAddPage h-full">
    <el-form
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="160px"
    >
      <el-card class="!border-none mb-3 mt-2 pb-3">
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
            <span>{{ form.volumeSnapId || "-" }}</span>
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
            :label="$t('form.cloudDisk') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.volumeName || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.currentSnapshot') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.isCurrent ? $t("form.yes") : $t("form.no") }}
            </span>
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
        // 搜索 筛选
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
    };
  },

  created() {
    this.getDetail();
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    getDetail() {
      // 获取详情
      var id = this.$route.params.id;
      mainApi
        .volumesSnapsDetail(id)
        .then((res) => {
          this.form = res;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
