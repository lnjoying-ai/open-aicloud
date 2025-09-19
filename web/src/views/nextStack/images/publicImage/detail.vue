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
            <span>{{ form.imageName || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.id') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.imageId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.os') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              <span v-if="form.imageOsType == 0">linux</span>
              <span v-else-if="form.imageOsType == 1">windows</span>
              <span v-else>-</span>
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.isPublic') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              <span v-if="form.isPublic">{{ $t("form.yes") }}</span>
              <span v-else>{{ $t("form.no") }}</span>
            </span>
          </el-form-item>
          <el-form-item
            :label="$t('form.imageType') + ':'"
            style="width: 50%; float: left"
          >
            <span>
              <span v-if="form.imageFormat == 4">{{
                $t("form.bareMetal")
              }}</span>
              <span v-else-if="form.imageFormat == 3">{{
                $t("form.instance")
              }}</span>
              <span v-else>-</span>
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
      mainApi.imageDetail(id).then((res) => {
        this.form = res;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.subNetAddPage {
}
</style>
