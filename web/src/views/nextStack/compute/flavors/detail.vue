<template>
  <div class="flavorsAddPage h-full mt-2">
    <el-form
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="120px"
    >
      <el-card class="!border-none pb-3">
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
            <span>{{ form.flavorId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.type') + ':'"
            style="width: 50%; float: left"
          >
            <span v-if="form.gpuCount">{{ $t("form.gpuCompute") }}</span>
            <span v-else>{{ $t("form.generalCompute") }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.cpu') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.cpu || "-" }}{{ $t("form.core") }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.mem') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.mem || "-" }}GB</span>
          </el-form-item>

          <el-form-item
            v-if="form.gpuName"
            :label="$t('form.gpu') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.gpuName }}*{{ form.gpuCount }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.ib') + ':'"
            style="width: 50%; float: left"
          >
            <span v-if="form.needIb">
              <i style="color: rgb(103, 194, 58)" class="el-icon-success" />
            </span>
            <span v-if="!form.needIb">-</span>
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

export default {
  data() {
    return {
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
        .flavorsDetail(id)
        .then((res) => {
          this.form = res;
        })
        .catch((error) => {});
    },
    goBack() {
      this.$router.push("/flavors");
    },
  },
};
</script>

<style lang="scss" scoped>
.flavorsAddPage {
}
</style>
