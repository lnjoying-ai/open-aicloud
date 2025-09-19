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
        <el-form-item :label="$t('form.selectSite') + ':'" prop="Sites">
          <el-select
            v-model="form.Sites"
            :placeholder="$t('form.pleaseSelectSite')"
            @change="changesites"
          >
            <el-option
              v-for="item in Sitesdata"
              :key="item.value"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.selectNode') + ':'" prop="targets">
          <el-table
            ref="multipleTable"
            :data="edgesdata"
            :row-key="getRowKeys"
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
            <el-table-column prop="node_name" :label="$t('form.nodeName')">
              <template slot-scope="scope">
                {{ scope.row.node_name ? scope.row.node_name : "-" }}
              </template>
            </el-table-column>
            <el-table-column prop="node_name" :label="$t('form.label')">
              <template slot-scope="scope">
                {{ scope.row.node_name ? scope.row.node_name : "-" }}
              </template>
            </el-table-column>
            <el-table-column prop="node_name" :label="$t('form.componentName')">
              <template slot-scope="scope">
                {{ scope.row.node_name ? scope.row.node_name : "-" }}
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
              v-for="(items, index) in multipleSelection"
              :key="items.node_id"
              size="mini"
              class="ml-2"
              type="info"
              closable
              @close="handleCloseTags(index)"
              >{{ items.node_name }}</el-tag
            >
          </div>
        </el-form-item>
        <el-divider />
        <!--
        <h1 class="mb-5 ml-4 componentadd_title"
            v-if="multipleSelection.length != 0 && routeid != 6">
          3.配置参数:
        </h1>
        <el-form-item label="">
          <el-collapse class="mt-4"
                       v-model="activeName"
                       accordion
                       v-if="multipleSelection.length != 0 && routeid != 6">
            <el-collapse-item v-for="(item, index) in multipleSelection"
                              :key="index"
                              :title="item.node_name"
                              :name="item.id">
              <el-form-item v-for="(items, indexs) in form.monitorIntegrationsParamsData[index].item"
                            :key="indexs"
                            v-show="routeid != 6"
                            :label="items.label"
                            label-width="200px"
                            :rules="[{ required: items.required, message: '请输入' + items.description, trigger: 'blur' },{ required: items.required, message: '请输入' + items.description, trigger: 'change' }]"
                            :prop="'monitorIntegrationsParamsData.'+index+'.item.'+indexs+'.default_value'">

                <el-select v-if="item.type == 'enum'"
                           v-model="items.default_value"
                           style="width: 400px"
                           :placeholder="items.description">
                  <el-option v-for="(item1, index1) in items.options"
                             :key="index1"
                             :label="item1"
                             :value="item1" />
                </el-select>

                <el-input style="width: 300px"
                          v-if="items.type == 'string'"
                          v-model="items.default_value"
                          :placeholder="'请输入' + items.description"></el-input>

                <el-tooltip class="item"
                            effect="dark"
                            :content="items.description"
                            placement="top-end">
                  <i class="el-icon-question" />
                </el-tooltip>

              </el-form-item>
            </el-collapse-item>
          </el-collapse>
        </el-form-item> -->
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
  edges,
  getMonitorPrometheus,
  getMonitorIntegrationsParams,
  addMonitorIntegrations,
} from "@/api/mainApi";
export default {
  components: {},

  data() {
    return {
      monitorPrometheusList: [], // prometheus实例
      monitorIntegrationsParamsList: [], // 监控组件stack参数
      activeName: "",
      sup_this: this,
      btnLoading: false,
      form: {
        data_source_id: "", // prometheus实例
        Sites: "", // 站点
        monitorIntegrationsParamsData: [], // 监控组件stack参数
        targets: [], // 监控目标
      },
      target_type_list: [
        {
          value: 1,
          label: this.$t("form.lightweightNode"),
        },
        {
          value: 2,
          label: "nextstack",
        },
        {
          value: 3,
          label: "openstack",
        },
      ],
      rules: {
        Sites: [
          {
            required: true,
            message: this.$t("form.pleaseSelectSite"),
            trigger: "change",
          },
        ],
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
            message: this.$t("form.pleaseSelectNode"),
            trigger: "change",
          },
        ],
      },
      // 控制是哪个组件中心进来的
      routeid: "",
      // 选中的数据
      multipleSelection: [],
      // 前面表头的数据
      assemblytitle: "",
      // 站点的数据
      Sitesdata: [],
      // 节点的数据
      edgesdata: [],
      // 节点的的搜索分页数据
      queryData: {
        page_size: 5,
        page_num: 1,
        totalNum: 0,
      },
      // 选中节点的id
      edgesid: "",
      selection: [],
    };
  },
  watch: {},
  watch: {},
  async created() {
    this.init();
  },
  mounted() {
    this.$route.meta.title = "";
    if (this.$route.params.id) {
      this.routeid = this.$route.params.id;
      if (this.$route.params.id == 1) {
        this.assemblytitle = this.$t("form.lightweightNode");
      } else if (this.$route.params.id == 2) {
        this.assemblytitle = this.$t("form.nextStackCloudMonitoring");
      } else if (this.$route.params.id == 3) {
        this.assemblytitle = this.$t("form.openStackCloudMonitoring");
      } else if (this.$route.params.id == 4) {
        this.assemblytitle = this.$t("form.gpuMonitoring");
      } else if (this.$route.params.id == 5) {
        this.assemblytitle = this.$t("form.installFromTemplate");
      } else if (this.$route.params.id == 6) {
        this.assemblytitle = this.$t("form.custom");
      }
    }
    // meta.title
    this.$route.meta.title =
      this.$t("form.componentInstallation") + "/" + this.assemblytitle;
    // this.init();
    this.getgetSitesddata();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    // 获取站点的信息
    async getgetSitesddata() {
      const list = await getSites();
      this.Sitesdata = [];
      list.sites.forEach((res) => {
        this.Sitesdata.push(...res.sites);
      });
    },
    // 改变站点触发节点数据变化
    async changesites(e) {
      this.edgesid = e;
      this.multipleSelection = [];
      this.form.monitorIntegrationsParamsData = [];
      this.form.targets = [];
      this.clearSelection();
      this.getedges();
    },
    // 获取节点的信息
    async getedges() {
      const list = await edges({
        site: this.edgesid,
        page_size: this.queryData.page_size,
        page_num: this.queryData.page_num,
      });
      this.edgesdata = list.nodes;
      this.queryData.totalNum = list.total_num;
    },
    // 节点表格的分页
    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.getedges();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.getedges();
    },
    // 控制高级配置的显示和隐藏
    open() {
      this.advancedoptions = !this.advancedoptions;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.form.targets = val.map((item) => {
        return item.node_id;
      });
      var lengthInfo =
        this.multipleSelection.length -
        this.form.monitorIntegrationsParamsData.length;
      if (lengthInfo > 0) {
        // 获取增加了那条数据
        var arr = this.multipleSelection.filter((item) => {
          return this.form.monitorIntegrationsParamsData.every((item1) => {
            return item.node_id != item1.node_id;
          });
        });
        // arr.forEach((item, index) => {
        //   this.form.monitorIntegrationsParamsData.push({
        //     node_id: item.node_id,
        //     item: this.monitorIntegrationsParamsList.showInputs ? JSON.parse(JSON.stringify(this.monitorIntegrationsParamsList.showInputs)) : [],
        //   })
        // })
      }
      if (lengthInfo < 0) {
        // 获取减少了那条数据
        var arr = this.form.monitorIntegrationsParamsData.filter((item) => {
          return this.multipleSelection.every((item1) => {
            return item.node_id != item1.node_id;
          });
        });
        arr.forEach((item, index) => {
          // 删除 node_id 为 item.node_id 的数据
          this.form.monitorIntegrationsParamsData =
            this.form.monitorIntegrationsParamsData.filter((item1) => {
              return item1.node_id != item.node_id;
            });
        });
      }
    },
    // 清空表格中选择框的状态
    clearSelection() {
      this.$refs.multipleTable.clearSelection(); // 清空选择框的选中状态
    },
    getRowKeys(row) {
      return row.node_id;
    },
    // 点击标签的关闭
    handleCloseTags(index) {
      this.$refs.multipleTable.selection.forEach((element) => {
        if (element.node_id === this.multipleSelection[index].node_id) {
          this.$refs.multipleTable.toggleRowSelection(element, false);
        } else {
          this.$refs.multipleTable.toggleRowSelection(element, true);
        }
      });
    },
    // 重置
    openobjectframe() {
      this.activeInfo = {};
      this.$refs.addForm.id = "";
      this.$refs.addForm.isAdd = true;
      this.$refs.addForm.dialog = true;
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
      // const monitorIntegrationsParamsList = await getMonitorIntegrationsParams({ integration_type: this.$route.params.id });
      // monitorIntegrationsParamsList.showInputs = JSON.parse(monitorIntegrationsParamsList.showInputs).map((item) => {
      //   return item
      // })
      // this.monitorIntegrationsParamsList = monitorIntegrationsParamsList;
    },
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.btnLoading = true;
          var data = {
            data_source_id: this.form.data_source_id, // prometheus实例
            integration_type: this.$route.params.id, // 组件类型
            target_type: this.$route.params.id, // 监控目标类型
            targets: this.form.targets, // 监控目标
            stack_params: this.form.monitorIntegrationsParamsData.map(
              (item) => {
                return { [item.node_id]: JSON.stringify(item.item) };
              }
            ), // 监控组件stack参数
          };
          addMonitorIntegrations(data)
            .then((res) => {
              this.btnLoading = false;
              this.$route.meta.title = "";
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
      // this.$route.meta.title = ""
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
