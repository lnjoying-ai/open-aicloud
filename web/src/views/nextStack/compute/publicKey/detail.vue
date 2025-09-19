<template>
  <div class="bmsAddPage h-full">
    <el-form
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      label-width="120px"
    >
      <el-card class="mt-2 pb-3">
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
          <!-- <el-form-item label="ID："
                        style="width:50%;float: left;margin:0">
            <span>{{  form.pubkeyId || '-'  }}</span>
          </el-form-item> -->
          <el-form-item
            :label="$t('form.description') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.description || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.createTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.createTime || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.updateTime') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.updateTime || "-" }}</span>
          </el-form-item>

          <el-form-item
            :label="$t('form.pubKeyId') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.pubkeyId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.pubKey') + ':'"
            style="width: 100%; float: left"
          >
            <span class="w-96 block break-all leading-normal">{{
              form.pubkey || "-"
            }}</span>
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
      loading: false,
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
      var is = this.$route.params.id;
      mainApi.pubkeysDetail(is).then((res) => {
        this.form = res;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
