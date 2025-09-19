<template>
  <div>
    <h3 style="font-size: 18px; font-weight: blod; margin-bottom: 30px">
      {{ $t("form.basicInfo") }}
    </h3>
    <el-row>
      <el-col :span="12">
        <el-row type="flex" class="row-bg">
          <el-col :span="22">
            <el-row class="title"
              ><span
                ><svg
                  t="1652840841577"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="2014"
                  width="16"
                  height="16"
                >
                  <path
                    d="M884.363636 139.636364a116.363636 116.363636 0 0 1 116.363637 116.363636v535.272727a116.363636 116.363636 0 0 1-116.363637 116.363637H139.636364a116.363636 116.363636 0 0 1-116.363637-116.363637V256a116.363636 116.363636 0 0 1 116.363637-116.363636zM116.363636 269.661091V791.272727a23.272727 23.272727 0 0 0 23.272728 23.272728h744.727272a23.272727 23.272727 0 0 0 23.272728-23.272728V270.801455L581.282909 551.330909a93.090909 93.090909 0 0 1-115.618909 4.561455l-4.096-3.165091L116.363636 269.661091zM809.122909 232.727273H218.158545l302.429091 248.017454L809.122909 232.727273z"
                    p-id="2015"
                    fill="#666666"
                  /></svg></span
              ><span>{{ $t("form.email") }}:</span></el-row
            >
            <el-row>{{ form.contact_info.email }}</el-row>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg">
          <el-col :span="22">
            <el-row class="title"
              ><span
                ><svg
                  t="1652841554774"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="4699"
                  width="16"
                  height="16"
                >
                  <path
                    d="M650.3 535.5C721.1 489.9 768 410.4 768 320c0-141.4-114.6-256-256-256S256 178.6 256 320c0 90.5 46.9 170 117.8 215.5C211.9 592.5 96 746.7 96 928v0.3c0 17.5 14.4 31.7 31.9 31.7h769c17.2 0 31-13.9 31.1-31.1v-3.2c-1-180.8-116.5-333.6-277.7-390.2z m-274.1-79.7C340 419.5 320 371.3 320 320s20-99.5 56.2-135.8C412.5 148 460.7 128 512 128s99.5 20 135.8 56.2C684 220.5 704 268.7 704 320s-20 99.5-56.2 135.8C611.5 492 563.3 512 512 512s-99.5-20-135.8-56.2zM161.4 896.1c3.2-36.2 12-71.4 26.2-105 17.7-41.9 43.1-79.6 75.5-111.9 32.3-32.3 70-57.7 111.9-75.5 43.4-18.4 89.5-27.7 137-27.7s93.5 9.2 136.7 27.4c41.8 17.6 79.4 42.8 111.6 74.9 32.3 32.1 57.7 69.5 75.5 111.2 14.5 34 23.5 69.7 26.8 106.6H161.4z"
                    p-id="4700"
                  /></svg></span
              ><span>{{ $t("form.userName") }}:</span></el-row
            >
            <el-row>{{ form.name }}</el-row>
          </el-col>
        </el-row>
        <el-row type="flex" class="row-bg">
          <el-col :span="22">
            <el-row class="title"
              ><span
                ><svg
                  t="1652841590346"
                  class="icon"
                  viewBox="0 0 1024 1024"
                  version="1.1"
                  xmlns="http://www.w3.org/2000/svg"
                  p-id="5155"
                  width="16"
                  height="16"
                >
                  <path
                    d="M806 911h-76.6c-1.6 0-3.2-0.2-4.8-0.6-51.8-12.1-102.5-29.2-150.9-51.1-181.6-82.2-326.9-227.5-409-409-21.9-48.4-39.1-99.1-51.1-150.9-0.4-1.6-0.6-3.2-0.6-4.8V218c0-57.9 47.1-105 105-105h84c57.9 0 105 47.1 105 105v147c0 35.4-17.4 67.3-45.8 86.5 53 85.8 125.4 158.2 211.2 211.2 19.2-28.4 51.1-45.8 86.5-45.8h147c57.9 0 105 47.1 105 105v84c0.1 58-47 105.1-104.9 105.1z m-74.1-42H806c34.7 0 63-28.3 63-63v-84c0-34.7-28.3-63-63-63H659c-26.2 0-49.2 15.9-58.7 40.6-2.2 5.7-6.8 10.2-12.6 12.3-5.8 2.1-12.2 1.5-17.5-1.6-106.8-61-195.6-149.8-256.7-256.6-3-5.3-3.6-11.7-1.5-17.5 2.1-5.8 6.5-10.4 12.3-12.6C349 414.2 365 391.2 365 365V218c0-34.7-28.3-63-63-63h-84c-34.7 0-63 28.3-63 63v74.1c11.4 48.3 27.5 95.6 48 140.8C281 605.2 418.8 743 591.1 821c45.1 20.4 92.5 36.6 140.8 48z"
                    p-id="5156"
                  /></svg></span
              ><span>{{ $t("form.phoneNumber") }}:</span></el-row
            >
            <el-row>{{ form.contact_info.phone }}</el-row>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { nations, divistions } from "@/api/mainApi";
import { getCurrent, putCurrent } from "@/api/user/mainApi";
import {} from "vuex";
export default {
  data() {
    return {
      form: {
        contact_info: {
          address: "",
          email: "",
          phone: "",
        },
      },
      rules: {},
      nationsData: [],
      divistionsData: [],
      divistionsData1: [],
    };
  },
  watch: {},
  created() {
    this.getUserInfo();
  },
  methods: {
    async getUserInfo() {
      const userMsg = await getCurrent();
      this.form = Object.assign({}, userMsg);
    },

    async getnations() {
      const list = await nations();
      this.nationsData = list;
    },
    async getdivistions(nation, adcode) {
      const list = await divistions(nation, adcode);
      this.divistionsData = list;
    },
    async getdivistions1(nation, adcode) {
      const list = await divistions(nation, adcode);
      this.divistionsData1 = list;
    },
    async userInfo() {},
    // 更新用户信息
    upDateUserMsg() {
      const params = {
        gender: this.form.gender,
        address: this.form.contact_info.address,
      };
      putCurrent(params)
        .then(() => {
          this.$notify({
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
        })
        .catch(() => {
          this.loading = false;
        });
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

svg {
  display: inline-block;
  margin-top: -4px;
  margin-right: 4px;
}
</style>
