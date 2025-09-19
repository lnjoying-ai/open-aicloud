<template>
  <div class="mt-2 relative" style="height: calc(100vh - 144px - 0.5rem)">
    <el-card
      class="mb-2 overflow-auto"
      shadow="never"
      style="height: calc((100vh - 185px) - 0.5rem)"
    >
      <el-form
        ref="ruleForm"
        :model="form"
        :rules="rules"
        label-width="140px"
        class="demo-ruleForm"
        size="small"
      >
        <h1 class="mb-5 ml-4 componentadd_title">
          1.{{ $t("form.selectCloudPrometheusInstance") }}:
        </h1>
        <el-form-item
          :label="$t('form.prometheusInstance') + ':'"
          prop="data_source_id"
        >
          <el-select
            v-model="form.data_source_id"
            :placeholder="$t('form.pleaseSelectPrometheusInstance')"
          >
            <el-option
              v-for="(item, index) in monitorPrometheusList"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-divider />
        <h1 class="mb-5 ml-4 componentadd_title">
          2.{{ $t("form.installMonitoringComponent") }}:
        </h1>
        <el-form-item :label="$t('form.targetSite') + ':'">
          <el-radio-group v-model="siteOpt">
            <el-radio :label="'all'">
              {{
                $t("form.allSites") +
                "(" +
                $t("form.quantity") +
                ":" +
                queryData.totalNum +
                ")"
              }}</el-radio
            >
            <el-radio :label="'site'">{{ $t("form.selectSite") }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="siteOpt == 'site'" :label="$t('form.site') + ':'">
          <el-input
            v-model="queryData.name"
            :placeholder="$t('form.pleaseInputSiteName')"
            @input="getSitesdata"
          />
        </el-form-item>
        <el-form-item v-if="siteOpt == 'site'" label="" prop="targets">
          <el-table
            ref="multipleTable"
            :data="Sitesdata"
            :row-key="(row) => row.id"
            :multiple="true"
            stripe
            style="width: 100%"
            tooltip-effect="dark"
            :default-sort="{ prop: 'date', order: 'descending' }"
            @selection-change="handleSelectionChange"
          >
            <el-table-column
              type="selection"
              width="55"
              :reserve-selection="true"
            />
            <el-table-column prop="name" :label="$t('form.siteName')">
              <template slot-scope="scope">
                {{ scope.row.name ? scope.row.name : "-" }}
              </template>
            </el-table-column>
            <el-table-column prop="region_name" :label="$t('form.regionName')">
              <template slot-scope="scope">
                {{ scope.row.region_name ? scope.row.region_name : "-" }}
              </template>
            </el-table-column>
            <el-table-column
              prop="node_num"
              :label="$t('form.nodeNum')"
              sortable
            >
              <template slot-scope="scope">
                {{ scope.row.node_num }}
              </template>
            </el-table-column>
          </el-table>
          <div class="flex justify-end mt-2 px-4">
            <el-pagination
              :current-page="queryData.page_num"
              :page-sizes="[5, 10, 20, 50, 100]"
              :page-size="queryData.page_size"
              layout="total, sizes, prev, pager, next, jumper"
              :total="queryData.totalNum"
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
            />
          </div>
          <div class="">
            {{ $t("form.selected") }}:
            <el-tag
              v-for="(tag, index) in multipleSelection"
              :key="index"
              size="mini"
              class="ml-2"
              type="info"
              closable
              @close="handleCloseTags(tag)"
              >{{ tag.name }}</el-tag
            >
          </div>
        </el-form-item>
        <el-divider />

        <h1 class="mb-5 ml-4 componentadd_title">
          3.{{ $t("form.configureParameters") }}:
        </h1>
        <el-form-item label="">
          <!-- <el-collapse class="mt-4"
                       accordion
                       v-if="multipleSelection.length != 0">
            <el-collapse-item v-for="(item, index) in multipleSelection"
                              :key="index"
                              :title="item.name"
                              :name="item.id"> -->
          <div v-if="form.monitorIntegrationsParamsList.length > 0">
            <el-form-item
              v-for="(items, indexs) in form.monitorIntegrationsParamsList"
              :key="indexs"
              :label="items.label"
              label-width="200px"
              :rules="[
                {
                  required: items.required,
                  message: '请输入' + items.description,
                  trigger: 'blur',
                },
                {
                  required: items.required,
                  message: '请输入' + items.description,
                  trigger: 'change',
                },
              ]"
              :prop="
                'monitorIntegrationsParamsList.' + indexs + '.default_value'
              "
            >
              <el-select
                v-if="items.type == 'enum'"
                v-model="items.default_value"
                style="width: 400px"
                :placeholder="items.description"
              >
                <el-option
                  v-for="(item1, index1) in items.options"
                  :key="index1"
                  :label="item1"
                  :value="item1"
                />
              </el-select>
              <el-select
                v-if="items.type == 'CfgOption'"
                v-model="items.default_value"
                style="width: 400px"
                :placeholder="items.description"
                value-key="id"
              >
                <el-option
                  v-for="(item1, index1) in fileList"
                  :key="index1"
                  :label="`${item1.group} / ${item1.dataId}`"
                  :value="
                    'cfg://' +
                    item1.tenant +
                    '/' +
                    item1.group +
                    '/' +
                    item1.dataId
                  "
                />
              </el-select>

              <el-input
                v-if="items.type == 'string'"
                v-model="items.default_value"
                style="width: 400px"
                :placeholder="$t('form.pleaseInput') + items.description"
              />

              <el-tooltip
                class="item"
                effect="dark"
                :content="items.description"
                placement="top-end"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </el-form-item>
          </div>

          <!-- </el-collapse-item>
          </el-collapse> -->
        </el-form-item>
      </el-form>
    </el-card>
    <div
      class="absolute z-30 px-3 py-2 bg-white"
      style="
        bottom: -15px;
        right: -10px;
        left: -9px;
        border-top: 1px solid rgba(0, 0, 0, 0.09);
        box-shadow: 0 2px 30px 0 rgba(0, 0, 0, 0.09);
      "
    >
      <el-row>
        <el-col :span="12">
          <div>
            <span class="text-red-500" style="line-height: 32px">
              <span><span class="font-bold text-xl ml-2" /></span>
            </span>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="text-right">
            <el-button
              type="primary"
              size="small"
              @click="pushalarmmanagement()"
              >{{ $t("form.cancel") }}</el-button
            >
            <el-button
              type="primary"
              size="small"
              :loading="btnLoading"
              @click="submitForm('ruleForm')"
              >{{ $t("form.installOneKey") }}</el-button
            >
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import {
  getSites,
  getMonitorPrometheus,
  addMonitorIntegrations,
  templatesInfo,
  configs,
} from "@/api/mainApi";
export default {
  components: {},
  data() {
    return {
      monitorPrometheusList: [], // prometheus实例
      btnLoading: false,
      form: {
        data_source_id: "", // prometheus实例
        Sites: "", // 站点
        monitorIntegrationsParamsList: [{}], // 监控组件stack参数
        targets: [], // 监控目标
      },

      rules: {
        data_source_id: [
          {
            required: true,
            message: this.$t("form.pleaseSelectPrometheusInstance"),
            trigger: "change",
          },
        ],
        targets: [
          {
            required: true,
            message: this.$t("form.pleaseSelectSite"),
            trigger: "change",
          },
        ],
      },

      // 选中的数据
      multipleSelection: [],
      // 站点的数据
      Sitesdata: [],
      // 站点的搜索分页数据
      queryData: {
        page_size: 5,
        page_num: 1,
        totalNum: 0,
        name: "",
      },
      fileList: [],
      siteOpt: "all",
    };
  },
  watch: {},
  watch: {},
  async created() {
    this.init();
  },
  mounted() {
    this.$route.meta.title =
      this.$t("form.componentInstallation") + "/" + this.$route.params.name;
    this.getSitesdata();
    this.getappConfigure();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    // 配置文件数据
    async getappConfigure() {
      const list = await configs({
        namespace: "",
        group: "system-monitor-prometheus-agent",
      });
      this.fileList = list.pageItems;
    },
    // 获取站点的信息
    async getSitesdata() {
      const list = await getSites(this.queryData);
      this.Sitesdata = [];
      list.sites.forEach((res) => {
        this.Sitesdata.push(...res.sites);
      });
      this.queryData.totalNum = list.totalNum;
    },
    // 站点表格的分页
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.getSitesdata();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.getSitesdata();
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.form.targets = val.map((item) => {
        return item.id;
      });
      // var lengthInfo = this.multipleSelection.length - this.form.monitorIntegrationsParamsData.length
      // if ((lengthInfo) > 0) {
      //   // 获取增加了那条数据
      //   var arr = this.multipleSelection.filter((item) => {
      //     return this.form.monitorIntegrationsParamsData.every((item1) => {
      //       return item.id != item1.id
      //     })
      //   })
      //   arr.forEach((item, index) => {
      //     this.form.monitorIntegrationsParamsData.push({
      //       id: item.id,
      //       item: this.monitorIntegrationsParamsList.show_inputs ? JSON.parse(JSON.stringify(this.monitorIntegrationsParamsList.show_inputs)) : [],
      //     })
      //   })
      // }
      // if (lengthInfo < 0) {
      //   // 获取减少了那条数据
      //   var arr = this.form.monitorIntegrationsParamsData.filter((item) => {
      //     return this.multipleSelection.every((item1) => {
      //       return item.id != item1.id
      //     })
      //   })
      //   arr.forEach((item, index) => {
      //     // 删除 id为 item.id的数据
      //     this.form.monitorIntegrationsParamsData = this.form.monitorIntegrationsParamsData.filter((item1) => {
      //       return item1.id != item.id
      //     })
      //   })

      // }
    },
    // 清空表格中选择框的状态
    clearSelection() {
      this.$refs.multipleTable.clearSelection(); // 清空选择框的选中状态
    },
    // 点击标签的关闭
    handleCloseTags(val) {
      this.$refs.multipleTable.toggleRowSelection(val, false);
      this.multipleSelection = this.multipleSelection.filter(
        (item) => item.id !== val.id
      );
      this.form.targets = this.form.targets.filter((item) => {
        return item != val.id;
      });
      // this.form.monitorIntegrationsParamsData = this.form.monitorIntegrationsParamsData.filter((item) => {
      //   return item.id != val.id
      // })
    },
    async init() {
      const params = {
        type: 0,
      };
      const monitorPrometheusList = await getMonitorPrometheus(params);
      this.monitorPrometheusList = monitorPrometheusList.prometheus_instances;
      this.monitorPrometheusList && this.monitorPrometheusList.length > 0
        ? (this.form.data_source_id = this.monitorPrometheusList[0].id)
        : "";
      const monitorIntegrationsParamsList = await templatesInfo(
        this.$route.params.id
      );
      this.form.monitorIntegrationsParamsList =
        monitorIntegrationsParamsList.show_inputs
          ? monitorIntegrationsParamsList.show_inputs
          : [];
      this.$forceUpdate();
    },
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.btnLoading = true;
          var data = {
            name: this.$route.params.name,
            data_source_id: this.form.data_source_id, // prometheus实例
            integration_type: 5, // 组件类型
            target_type: 4, // 监控目标类型
            targets: this.siteOpt == "all" ? [] : this.form.targets, // 监控目标
            stack_template_version_id: this.$route.params.id,
            stack_params: this.form.monitorIntegrationsParamsList,
            // stack_params: this.form.monitorIntegrationsParamsData.map((item) => {
            //   return { [item.id]: JSON.stringify(item.item) }
            // }),//监控组件stack参数
          };
          addMonitorIntegrations(data)
            .then((res) => {
              this.btnLoading = false;
              this.$notify({
                title: this.$t("message.addSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$router.push({
                path: "/component/installation",
              });
            })
            .catch((err) => {
              this.btnLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    pushalarmmanagement() {
      this.$router.go("-1");
    },
  },
};
</script>
<style lang="scss" scoped>
.componentadd_title {
  font-size: 14px;
  font-weight: 600;
}
</style>
