<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="$t('form.configNode')"
      width="800px"
      :close-on-click-modal="false"
      @closed="cancel"
    >
      <el-form
        ref="form"
        size="small"
        label-width="120px"
        class="form"
        :rules="rules"
        :model="configNodeData"
      >
        <el-form-item :label="$t('form.region') + ':'" prop="region_id">
          <el-select
            v-model="configNodeData.region_id"
            filterable
            :placeholder="$t('form.pleaseSelect')"
            @change="changeRegion"
          >
            <el-option
              v-for="item in regions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.site') + ':'" prop="site_id">
          <el-select
            v-model="configNodeData.site_id"
            filterable
            :placeholder="$t('form.pleaseSelect')"
          >
            <el-option
              v-for="item in sites"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('form.nodeName') + ':'"
          prop="node_name"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputNodeName'),
              trigger: 'blur',
            },
            {
              pattern: /^[a-zA-Z0-9][a-zA-Z0-9-_@]{0,64}$/,
              message: $t('form.pleaseInputNodeName'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="configNodeData.node_name"
            :placeholder="$t('form.pleaseInputNodeName')"
            style="width: 215px"
          />
        </el-form-item>
        <el-collapse v-model="activeNames">
          <el-collapse-item :title="$t('form.resourceAllocation')" name="1">
            <el-form-item label-width="0px">
              <el-table :data="tableData" style="width: 100%" border>
                <el-table-column :label="$t('form.cpu')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.cpu_num" />
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.memory')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.mem_num" />
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.disk')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.disk_info" />
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-collapse-item>
          <el-collapse-item :title="$t('form.advancedOptions')" name="2">
            <el-form-item
              v-for="(item, index) in labelData"
              :key="index"
              :label="item.show_name"
            >
              <template slot="label">
                {{ item.show_name }}
                <el-tooltip
                  v-show="item.description"
                  class="item"
                  effect="dark"
                  :content="item.description"
                  placement="top-start"
                >
                  <i class="el-icon-info" />
                </el-tooltip>
              </template>
              <div v-if="item.type === 'bool'" style="width: 260px">
                <el-radio-group
                  v-model="item.default_value"
                  :disabled="!item.immutable"
                  size="small"
                >
                  <el-radio :label="true">{{ $t("form.yes") }}</el-radio>
                  <el-radio :label="false">{{ $t("form.no") }}</el-radio>
                </el-radio-group>
              </div>
              <div
                v-if="
                  item.type === 'string' &&
                  item.key.indexOf('bp') == -1 &&
                  item.key.indexOf('owner') == -1
                "
              >
                <el-input
                  v-model="item.default_value"
                  style="width: 260px"
                  size="small"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === 'enum'">
                <el-select
                  v-model="item.default_value"
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 260px"
                  clearable
                  :disabled="!item.immutable"
                >
                  <el-option
                    v-for="items in item.options"
                    :key="items.index"
                    :value="items"
                  />
                </el-select>
              </div>
              <div v-if="item.type === 'float'">
                <el-input-number
                  v-model="item.default_value"
                  style="width: 260px"
                  size="small"
                  :precision="2"
                  :step="0.1"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === 'int'">
                <el-input-number
                  v-model="item.default_value"
                  style="width: 260px"
                  size="small"
                  :min="0"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === '-'">
                <el-input
                  v-model="item.default_value"
                  style="width: 260px"
                  size="small"
                  :disabled="!item.immutable"
                />
              </div>
              <div
                v-if="item.type === 'string' && item.key.indexOf('bp') != -1"
              >
                <el-select
                  v-model="item.default_value"
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 260px"
                  clearable
                  :disabled="!item.immutable"
                  @change="labelbpChange"
                >
                  <el-option
                    v-for="items in labelBpsData"
                    :key="items.id"
                    :label="items.name"
                    :value="items.id"
                  />
                </el-select>
              </div>
              <div
                v-if="item.type === 'string' && item.key.indexOf('owner') != -1"
              >
                <el-select
                  v-model="item.default_value"
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 260px"
                  clearable
                  :disabled="!item.immutable"
                >
                  <el-option
                    v-for="items in labelUserData"
                    :key="items.id"
                    :label="items.name"
                    :value="items.id"
                  />
                </el-select>
              </div>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          :loading="loading"
          size="small"
          type="primary"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  configEdges,
  getLabelOption,
  getTaintOptions,
  getBps,
  getUsers,
  getRegions,
  getSites,
} from "@/api/mainApi";
import { mapGetters } from "vuex";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
  },
  data() {
    return {
      // 区域列表
      regions: [],
      // 站点
      sites: [],
      configNodeData: "",
      activeNames: ["1"],
      // 选择的组织机构ID
      labelbp_id: "",
      stainbp_id: "",
      dialog: false,
      loading: false,
      tableData: [],
      labelData: [],
      stainData: [],
      // 折中数据
      labelsData: [],
      stainsData: [],
      // 禁用表格
      disabled: true,
      form: {},
      radio: false,
      // 组织机构数据
      labelBpsData: [],
      // 用户
      labelUserData: [],
      // 组织机构数据
      stainBpsData: [],
      // 用户
      stainUserData: [],
      rules: {
        region_id: [
          {
            required: true,
            message: this.$t("form.pleaseSelectRegion"),
            trigger: "change",
          },
        ],
        site_id: [
          {
            required: true,
            message: this.$t("form.pleaseSelectSite"),
            trigger: "change",
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  created() {
    this.labelbpsinit();
    this.labeluserinit();
    this.stainbpsinit();
  },
  methods: {
    // 获取区域列表
    getRegions() {
      getRegions({}).then((res) => {
        this.regions = res.regions.map((item) => {
          return {
            value: item.id,
            label: item.name,
          };
        });
      });
    },
    getSites() {
      var region_id = this.configNodeData.region_id;
      this.sites = [];
      getSites({
        region_id,
      }).then((res) => {
        if (res.sites.length > 0 && res.sites[0].sites.length > 0) {
          this.sites = res.sites[0].sites.map((item) => {
            return {
              value: item.id,
              label: item.name,
            };
          });
        } else {
          this.sites = [];
        }
      });
    },
    changeRegion(value) {
      this.configNodeData.region_id = value;
      this.configNodeData.site_id = "";
      this.getSites();
    },
    // 标签组织机构改变
    labelbpChange(value) {
      this.labelbp_id = value;
      this.labeluserinit();
    },
    stainbpChange(value) {
      this.stainbp_id = value;
      this.stainuserinit();
    },
    async labelbpsinit() {
      const list = await getBps();
      this.labelBpsData = list.bps;
    },
    async labeluserinit(value) {
      this.labelUserData = [];
      const params = {};
      if (this.labelbp_id != "" && this.labelbp_id != undefined) {
        params.bp_id = this.labelbp_id;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      if (list.users != undefined && list.users != null) {
        this.labelUserData = list.users;
      } else {
        this.labelUserData = [];
      }
      for (var i = 0; i < this.labelData.length; i++) {
        if (this.labelData[i].key.indexOf("owner") != -1) {
          value == "1"
            ? (this.labelData[i].default_value =
                this.labelData[i].default_value)
            : (this.labelData[i].default_value = "");
        }
      }
    },
    async stainbpsinit() {
      const list = await getBps();
      this.stainBpsData = list.bps;
    },
    async stainuserinit(value) {
      this.stainUserData = [];
      const params = {};
      if (this.stainbp_id != "" && this.stainbp_id != undefined) {
        params.bp_id = this.stainbp_id;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      if (list.users != undefined && list.users != null) {
        this.stainUserData = list.users;
      } else {
        this.stainUserData = [];
      }
      for (var i = 0; i < this.stainData.length; i++) {
        if (this.stainData[i].key.indexOf("owner") != -1) {
          value == "1"
            ? (this.stainData[i].default_value =
                this.stainData[i].default_value)
            : (this.stainData[i].default_value = "");
        }
      }
    },
    // 展示labeloption
    async getLabelOption() {
      this.labelsData = [];
      const list = await getLabelOption("node");
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          if (list[i].key != undefined && list[i].key != null) {
            if (list[i].key.indexOf("bpId") != -1) {
              if (this.userInfo.kind == 2) {
                list[i].default_value = this.userInfo.bp_id;
                list[i].immutable = false;
              }
            } else if (list[i].key.indexOf("owner") != -1) {
              if (this.userInfo.kind == 2) {
                list[i].default_value = this.userInfo.id;
                list[i].immutable = false;
              }
            }
            list[i].required == true
              ? (list[i].required = this.$t("form.yes"))
              : (list[i].required = this.$t("form.no"));
          }
        }
        this.labelsData = list;
      }
    },
    async getTaintOptions() {
      this.stainsData = [];
      const list = await getTaintOptions("node");
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          if (list[i].key != undefined && list[i].key != null) {
            if (list[i].key.indexOf("bpId") != -1) {
              if (this.userInfo.kind == 2) {
                list[i].default_value = this.userInfo.bp_id;
                list[i].immutable = false;
              }
            } else if (list[i].key.indexOf("owner") != -1) {
              if (this.userInfo.kind == 2) {
                list[i].default_value = this.userInfo.id;
                list[i].immutable = false;
              }
            }
            list[i].required == true
              ? (list[i].required = this.$t("form.yes"))
              : (list[i].required = this.$t("form.no"));
          }
        }
        this.stainsData = list;
      }
    },
    show(value) {
      this.configNodeData = value;
      this.getRegions();
      this.getSites();
      const that = this;
      that.dialog = true;
      that.tableData = [];
      that.form = Object.assign({}, value);
      if (
        value.resource_limit &&
        JSON.stringify(value.resource_limit) != "{}"
      ) {
        that.tableData.push({
          cpu_num: value.resource_limit.cpu_limit,
          disk_info: value.resource_limit.disk_limit,
          mem_num: value.resource_limit.mem_limit,
        });
      } else {
        that.tableData.push({
          cpu_num: value.dev_info.cpu_info.cpu_logic_num,
          disk_info: value.dev_info.disk_info.disk_total,
          mem_num: value.dev_info.mem_info.mem_total,
        });
      }

      that.stainData = [];
      that.labelData = [];
      var formLabels = [];
      if (that.form.labels && JSON.stringify(that.form.labels) != "{}") {
        Object.keys(that.form.labels).forEach(function (item, index) {
          formLabels.push(item);
        });
        for (var i = 0; i < that.labelsData.length; i++) {
          if (formLabels.indexOf(that.labelsData[i].key) != -1) {
            that.labelsData[i].default_value =
              that.form.labels[that.labelsData[i].key];
            if (that.labelsData[i].key.indexOf("bpId") != -1) {
              that.labelbp_id = that.form.labels[that.labelsData[i].key];
              that.labeluserinit(1);
            }
          }
          that.labelData.push(that.labelsData[i]);
        }
      } else {
        that.labelData = that.labelsData;
      }
      var taintsValue = [];
      if (that.form.taints && JSON.stringify(that.form.taints) != "{}") {
        Object.keys(that.form.taints).forEach(function (item, index) {
          taintsValue.push(item);
        });
        for (var i = 0; i < that.stainsData.length; i++) {
          if (taintsValue.indexOf(that.stainsData[i].key) != -1) {
            that.stainsData[i].default_value =
              that.form.taints[that.stainsData[i].key];
            if (that.stainsData[i].key.indexOf("bpId") != -1) {
              that.stainbp_id = that.form.taints[that.stainsData[i].key];
              that.stainuserinit(1);
            }
          }
          that.stainData.push(that.stainsData[i]);
        }
      } else {
        that.stainData = that.stainsData;
      }
    },
    cancel() {
      this.dialog = false;
    },
    // 提交
    doSubmit() {
      let flag = true;
      const labels = this.labelData.filter(
        (item) => item.key || item.default_value
      );
      const taints = this.stainData.filter(
        (item) => item.key || item.default_value
      );
      const labelsObj = {};
      labels.forEach(function (item) {
        labelsObj[item.key] = item.default_value;
      });
      const taintsObj = {};
      taints.forEach(function (item) {
        taintsObj[item.key] = item.default_value;
      });
      labels.forEach((element) => {
        if (!element.key) {
          this.$notify({
            title: this.$t("form.pleaseInputTaintKey"),
            type: "info",
            duration: 2500,
          });
          flag = false;
          return false;
        }
      });
      taints.forEach((element) => {
        if (!element.key) {
          this.$notify({
            title: this.$t("form.pleaseInputKey"),
            type: "info",
            duration: 2500,
          });
          flag = false;
          return false;
        }
      });
      if (flag) {
        const params = {
          resource_limit: {
            cpu_limit: this.tableData[0].cpu_num,
            disk_limit: this.tableData[0].disk_info,
            mem_limit: this.tableData[0].mem_num,
          },
          labels: labelsObj,
          taints: taintsObj,
          region_id: this.configNodeData.region_id,
          site_id: this.configNodeData.site_id,
          node_name: this.configNodeData.node_name,
        };

        configEdges(this.form.node_id, params).then((res) => {
          this.dialog = false;
          this.$parent.searchinit();
        });
      }
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

p {
  font-family: "PingFangSC-Regular", "PingFang SC", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 12px;
  color: #a0afaf;
  width: 330px;
  line-height: 22px;
}

.nav {
  font-family: "PingFangSC-Regular", "PingFang SC", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 14px;
  color: rgba(0, 0, 0, 0.847058823529412);
  line-height: 32px;
  width: 10%;
  text-align: center;
}

::v-deep .el-collapse-item__header {
  background-color: #fafafa !important;
  margin-bottom: 5px;
  color: #8a8a8a;
  font-size: 14px;
  padding-left: 6px;
}

::v-deep .el-table__empty-block {
  display: none !important;
}

::v-deep .is-leaf {
  background: #f7f7f7 !important;
  padding: 5px 0;
}

::v-deep .el-form--label-top .el-form-item__label {
  float: none;
  display: inline-block;
  text-align: left;
  padding: 0 0 0px !important;
}

::v-deep .el-form-item__content {
  line-height: 32px;
  position: relative;
  font-size: 14px;
}

.form {
  .el-table td,
  .el-table th {
    padding: 5px 0;
  }
}
</style>
