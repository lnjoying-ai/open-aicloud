<template>
  <div>
    <h3 style="font-size: 18px; font-weight: blod; margin-bottom: 30px">
      {{ $t("form.safeCenter") }}
    </h3>
    <el-row type="flex" class="row-bg">
      <el-col :span="22">
        <el-row class="title">{{ $t("form.accountPassword") }}</el-row>
        <el-row>{{ $t("form.currentPasswordStrength") }}</el-row>
        <el-row style="color: #52c41a">{{ $t("form.strong") }}</el-row>
      </el-col>
      <el-col :span="2"
        ><span @click="changeUserPwd">{{ $t("form.modify") }}</span></el-col
      >
    </el-row>
    <el-row type="flex" class="row-bg">
      <el-col :span="22">
        <el-row class="title">{{ $t("form.secretPhone") }}</el-row>
        <el-row>{{ $t("form.boundPhone") }}</el-row>
        <el-row>{{ form.contact_info.phone }}</el-row></el-col
      >
      <el-col :span="2"
        ><span @click="changePhone">{{ $t("form.modify") }}</span></el-col
      >
    </el-row>
    <el-row type="flex" class="row-bg">
      <el-col :span="22">
        <el-row class="title">{{ $t("form.backupEmail") }}</el-row>
        <el-row>{{ $t("form.boundEmail") }}</el-row>
        <el-row>{{ form.contact_info.email }}</el-row></el-col
      >
      <el-col :span="2"
        ><span @click="changeEmail">{{ $t("form.modify") }}</span></el-col
      >
    </el-row>
    <el-row type="flex" class="row-bg">
      <el-col :span="22">
        <el-row class="title">{{ $t("form.modifyRepositoryPassword") }}</el-row>
        <el-row>{{ $t("form.repositoryUsername") }}</el-row>
        <el-row>{{ form.name }}</el-row>
      </el-col>
      <el-col :span="2"
        ><span @click="changeRegPwd(form.name)">{{
          $t("form.modify")
        }}</span></el-col
      >
    </el-row>
    <changePassword ref="changePassword" />
    <changePhone ref="changePhone" />
    <changeEmail ref="changeEmail" />
    <changeRegPwd ref="changeRegPwd" />
  </div>
</template>
<script>
import changePassword from "./changePassword";
import changeEmail from "./changeEmail";
import changePhone from "./changePhone";
import changeRegPwd from "./changeRegPwd.vue";
import { getCurrent } from "@/api/user/mainApi";

export default {
  components: {
    changePassword,
    changeEmail,
    changePhone,
    changeRegPwd,
  },
  data() {
    return {
      form: {
        contact_info: {
          address: "",
          email: "",
          phone: "",
        },
      },
      registrieList: [],
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    async getUserInfo() {
      const userMsg = await getCurrent();
      this.form = Object.assign({}, userMsg);
    },
    changeUserPwd() {
      this.$refs.changePassword.add();
    },
    changePhone() {
      this.$refs.changePhone.add();
    },
    changeEmail() {
      this.$refs.changeEmail.add();
    },
    changeRegPwd(id) {
      this.$refs.changeRegPwd.add({ user_name: id });
    },
  },
};
</script>
<style lang="scss" scoped>
.row-bg {
  border-bottom: 1px solid #e0e0e0;
  align-items: center;
  padding: 10px 0;

  .el-row {
    font-size: 12px;
    line-height: 24px;
    color: #666;
  }

  .title {
    font-size: #000;
    font-weight: bold;
  }

  .el-col-2 {
    font-size: 14px;
    line-height: 24px;
    color: #409eff;
    cursor: pointer;
  }
}
</style>
