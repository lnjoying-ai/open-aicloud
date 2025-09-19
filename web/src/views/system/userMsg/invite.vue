<template>
  <div>
    <h3 style="font-size: 18px; font-weight: blod; margin-bottom: 30px">
      {{ $t("form.inviteLink") }}
    </h3>
    <div>
      <!-- <el-alert title="当前链接状态" type="warning" show-icon :closable="false">
        <template v-slot:default>
          <div>
            <p>目前还没有定</p>
          </div>
        </template>
</el-alert>
<p class="mt-4" style="font-weight: blod">链接地址</p> -->
      <el-input
        v-model="linkaddress"
        size="small"
        style="width: 400px"
        :placeholder="$t('form.inviteCode')"
        :disabled="true"
      />
      <el-button
        size="small"
        class="ml-4"
        type="primary"
        icon="el-icon-link"
        @click="generate"
        >{{ $t("form.generateLink") }}</el-button
      >
      <el-button
        size="small"
        class="ml-4"
        type="success"
        :disabled="!linkaddress"
        icon="el-icon-document-copy"
        @click="copy"
        >{{ $t("form.copyLink") }}</el-button
      >
      <div v-if="linkaddress" class="mt-4">
        <el-alert title="" type="info" show-icon :closable="false">
          <template v-slot:default>
            <div>
              <p>{{ $t("form.inviteLink1") }}</p>
              <p>{{ $t("form.inviteLink2") }}</p>
            </div>
          </template>
        </el-alert>
      </div>
    </div>
  </div>
</template>

<script>
import { generatelink } from "@/api/iam/user";
import {} from "vuex";
export default {
  data() {
    return {
      // 邀请的链接地址
      linkaddress: "",
    };
  },
  watch: {},
  created() {},
  methods: {
    // 生成链接
    generate() {
      generatelink().then((res) => {
        this.linkaddress = res.link;
      });
    },
    // 复制链接
    copy() {
      this.$script.copyText(this.linkaddress);
    },
  },
};
</script>
<style lang="scss" scoped></style>
