<template>
  <div class="snapsAddPage h-full mt-2">
    <el-form
      :model="form"
      label-width="120px"
      :size="$store.state.nextStack.viewSize.main"
    >
      <el-card class="!border-none mb-3 pb-3">
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
            <span>{{ form.snapId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.description') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.description || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.instance') + ':'"
            style="width: 50%; float: left"
          >
            <router-link
              :to="'/nextStack/vm/detail/' + form.vmInstanceId"
              class="text-blue-400"
              >{{ form.vmInstanceName || "-" }}</router-link
            >
          </el-form-item>

          <el-form-item
            :label="$t('form.usageStatus') + ':'"
            style="width: 50%; float: left"
          >
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="form.current ? 'success' : 'danger'"
              >{{
                form.current ? $t("form.inUse") : $t("form.notInUse")
              }}</el-tag
            >
          </el-form-item>
          <el-form-item
            :label="$t('form.status') + ':'"
            style="width: 50%; float: left"
          >
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getVmStatus(form.phaseStatus, 'tag')"
              >{{ filtersFun.getVmStatus(form.phaseStatus, "status") }}</el-tag
            >
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.createTime || "-" }}</span>
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
  components: {},
  data() {
    return {
      filtersFun: filtersFun,
      form: {},
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
        .snapsDetail(id)
        .then((res) => {
          this.form = res;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.snapsAddPage {
}
</style>
