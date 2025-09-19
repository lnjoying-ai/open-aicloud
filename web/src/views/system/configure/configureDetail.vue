<template>
  <div v-if="detailMain">
    <el-card class="box-card" shadow="never">
      <div class="text item">
        <el-form ref="form" label-width="120px" size="small">
          <el-form-item :label="$t('form.dataId') + ':'">
            <el-input
              v-model="detailMain.dataId"
              :disabled="true"
              :placeholder="this.$t('form.pleaseInput')"
            />
          </el-form-item>
          <el-form-item :label="$t('form.group') + ':'">
            <el-input
              v-model="detailMain.group"
              :disabled="true"
              :placeholder="this.$t('form.pleaseInput')"
            />
          </el-form-item>
          <el-form-item
            :label="$t('form.format') + ':'"
            class="formContentBlock"
          >
            <el-radio-group v-model="detailMain.type" :disabled="true">
              <el-radio label="yaml" value="yaml">YAML</el-radio>
              <el-radio label="text" value="text">TEXT</el-radio>
              <el-radio label="properties" value="properties"
                >properties</el-radio
              >
            </el-radio-group>
          </el-form-item>
          <el-form-item>
            <template slot="label">
              <span style="line-height: 1; display: inline-flex"
                >Configuration Content:</span
              >
            </template>
            <div
              style="
                width: 100%;
                line-height: 1.2;
                max-height: 56.2vh;
                overflow: auto;
              "
            >
              <yaml-editor
                ref="yamlEditor"
                v-model="detailMain.content"
                :download-name="detailMain.dataId"
                :placeholder="''"
                :is-add="false"
                :download-type="
                  detailMain.type == 'text'
                    ? 'txt'
                    : detailMain.type == 'yaml'
                    ? 'yml'
                    : detailMain.type
                "
                :lint="detailMain.type == 'yaml'"
              />
            </div>
          </el-form-item>
          <el-form-item
            style="
              text-align: center;
              display: flex;
              align-items: center;
              justify-content: center;
            "
            label-width="0px"
          >
            <el-button size="small" @click="goBack()">{{
              this.$t("form.back")
            }}</el-button>
            <el-button type="primary" size="small" @click="onSubmit">{{
              this.$t("form.submit")
            }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>
<script>
import { configs, toConfigs } from "@/api/mainApi";
import initData from "@/mixins/initData";
import YamlEditor from "@/components/yaml/editor.vue";

export default {
  components: { YamlEditor },
  mixins: [initData],
  data() {
    return {
      detailMain: "",
    };
  },
  watch: {},
  created() {},
  mounted() {
    this.init();
  },
  methods: {
    goBack() {
      this.$router.go(-1);
    },
    async onSubmit() {
      var data = {
        id: this.detailMain.id,
        data_id: this.detailMain.dataId,
        group: this.detailMain.group,
        type: this.detailMain.type,
        content: this.detailMain.content,
      };
      const list = await toConfigs(data);
      this.$notify({
        title: this.$t("message.modifySuccess"),
        type: "success",
        duration: 2500,
      });
      this.$router.push({
        path: "/configure",
      });
      this.init();
    },
    async init() {
      // 请求容器详情
      const params = {};
      params.system = true;
      const list = await configs(params);
      this.detailMain = list.pageItems.filter(
        (item) => item.id === this.$route.query.id
      )[0];
    },
  },
};
</script>
<style lang="scss" scoped>
.row-bg {
  padding: 10px 0;
}

.el-col-4 {
  width: 40%;
  font-size: 15px;
  font-family: "Microsoft YaHei ", "Microsoft YaHei", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: rgba(0, 0, 0, 0.8470588235294118);
  line-height: 22px;
}

::v-deep .el-table th {
  background: #fafafa;
}

::v-deep .el-card {
  border: 0px solid #ebeef5;
  background-color: #fff;
  color: #303133;
  -webkit-transition: 0.3s;
  transition: 0.3s;
}

::v-deep .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
  // background-color: transparent;
  z-index: 1;
}
</style>
