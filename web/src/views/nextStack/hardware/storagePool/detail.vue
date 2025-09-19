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
            <span>{{ form.poolId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.type') + ':'"
            style="width: 50%; float: left"
          >
            <span v-if="form.type == 1">{{ $t("form.fileSystem") }}</span>
            <span v-else>-</span>
          </el-form-item>
          <!-- <el-form-item label="paras：">
            <span>{{ form.paras || '-' }}</span>
          </el-form-item> -->
          <!-- <el-form-item label="sid：">
            <span>{{ form.sid || '-' }}</span>
          </el-form-item> -->
          <el-form-item
            :label="$t('form.description') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              {{ form.description || "-" }}
            </span>
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
      var id = this.$route.params.id;
      mainApi
        .storagePoolsDetail(id)
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
