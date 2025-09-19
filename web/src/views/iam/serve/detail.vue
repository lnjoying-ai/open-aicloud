<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="detailMain"
        :rules="rules"
        size="small"
        label-width="120px"
      >
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.name') + ':'"
          prop="display_name"
        >
          <el-input
            v-model="detailMain.display_name"
            :placeholder="$t('form.pleaseInputServeName')"
          />
        </el-form-item>
        <el-form-item v-else :label="$t('form.name') + ':'">
          <span>{{
            detailMain.display_name ? detailMain.display_name : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.description') + ':'"
          prop="description"
        >
          <el-input
            v-model="detailMain.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            autosize
            :placeholder="$t('form.pleaseInputServeDescription')"
          />
        </el-form-item>
        <el-form-item v-else :label="$t('form.description')">
          <span>{{
            detailMain.description ? detailMain.description : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.serveCode')"
        >
          <el-input
            v-model="detailMain.iam_code"
            :placeholder="$t('form.pleaseInputServeCode')"
          />
        </el-form-item>
        <el-form-item v-else :label="$t('form.serveCode') + ':'">
          <span>{{ detailMain.iam_code ? detailMain.iam_code : "-" }}</span>
        </el-form-item>
        <!-- <el-form-item label="所属服务">
          <el-cascader v-model="detailMain.parent_id" placeholder="请选择所属服务" :options="tableData" :show-all-levels="false"
            :props="{ value: 'id', label: 'name', children: 'children', expandTrigger: 'hover', checkStrictly: true, emitPath: false }"
            filterable clearable style="width:100%;"></el-cascader>
        </el-form-item> -->
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.serveStatus') + ':'"
        >
          <el-select
            v-model="detailMain.enable"
            :placeholder="$t('form.pleaseSelectEnable')"
            style="width: 100%"
          >
            <el-option :value="1" :label="$t('form.enable')" />
            <el-option :value="-1" :label="$t('form.disable')" />
          </el-select>
        </el-form-item>
        <el-form-item v-else :label="$t('form.serveStatus') + ':'">
          <span>{{
            detailMain.enable ? $t("form.enable") : $t("form.disable")
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.createTime') + ':'">
          <span>
            {{ detailMain.create_time ? detailMain.create_time : "-" }}</span
          >
        </el-form-item>
        <el-form-item :label="$t('form.modifyTime') + ':'">
          <span>
            {{ detailMain.update_time ? detailMain.update_time : "-" }}</span
          >
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          style="float: right"
        >
          <el-button
            :loading="loadingForm"
            type="primary"
            size="small"
            @click="doEdit()"
            >{{ $t("form.save") }}</el-button
          >
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { detailService } from "@/api/iam/serve";
import { getService, editService } from "@/api/iam/serve";

export default {
  components: {},
  data() {
    return {
      loadingForm: false,
      detailMain: {},
      tableData: [],
      rules: {
        display_name: [
          {
            required: true,
            message: this.$t("form.pleaseInputServeName"),
            trigger: "blur",
          },
        ],
        iam_code: [
          {
            required: true,
            message: this.$t("form.pleaseInputServeCode"),
            trigger: "blur",
          },
        ],
      },
    };
  },

  async created() {
    this.init();
    this.getServiceList();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    async init() {
      const list = await detailService(this.$route.params.id);
      this.detailMain = list;
    },
    async getServiceList() {
      const list = await getService({
        page_size: 99999,
        page_num: 1,
      });
      this.tableData = list.services;
    },
    doEdit() {
      var data = JSON.parse(JSON.stringify(this.detailMain));
      this.loadingForm = true;
      editService(this.$route.params.id, data)
        .then((res) => {
          this.$notify({
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loadingForm = false;
          this.init();
        })
        .catch((err) => {
          this.loadingForm = false;
          console.error(err.response.data.message);
        });
    },
  },
};
</script>
<style scoped>
.yaml-editor {
  height: 100%;
  position: relative;
  top: 20px;
}
</style>
