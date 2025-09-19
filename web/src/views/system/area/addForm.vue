<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    :title="isAdd ? $t('form.addArea') : $t('form.editArea')"
    width="800px"
    :before-close="handleCloseDialog"
    :close-on-click-modal="false"
    @open="onOpen"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      size="small"
      label-width="120px"
    >
      <el-form-item :label="$t('form.areaName') + ':'" prop="name">
        <el-input v-model="form.name" :placeholder="$t('form.pleaseInput')" />
      </el-form-item>
      <el-form-item
        :label="$t('form.areaDescription') + ':'"
        prop="description"
      >
        <el-input
          v-model="form.description"
          type="textarea"
          maxlength="255"
          show-word-limit
          :placeholder="$t('form.pleaseInput')"
        />
      </el-form-item>
      <el-collapse v-model="activeNames" accordion>
        <el-collapse-item name="1">
          <template slot="title">
            <div
              style="
                padding-left: 10px;
                font-size: 14px;
                font-weight: 400;
                font-style: normal;
                font-size: 14px;
                color: rgba(0, 0, 0, 0.847058823529412);
                line-height: 22px;
              "
            >
              {{ $t("form.gateway") }}
              <div
                style="
                  font-weight: 400;
                  font-style: normal;
                  font-size: 10px;
                  color: rgba(0, 0, 0, 0.447058823529412);
                  line-height: 22px;
                "
              >
                {{ $t("form.configureGateway") }}
              </div>
            </div>
          </template>
          <div style="margin-top: 20px">
            <el-form-item prop="gw_node_ids">
              <el-transfer
                v-model="form.gw_node_ids"
                :data="nodes"
                :titles="[$t('form.gatewayList'), $t('form.selected')]"
              />
            </el-form-item>
          </div>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <div
              style="
                padding-left: 10px;
                font-size: 14px;
                font-weight: 400;
                font-style: normal;
                font-size: 14px;
                color: rgba(0, 0, 0, 0.847058823529412);
                line-height: 22px;
              "
            >
              {{ $t("form.label") }}/{{ $t("form.stain") }}
              <div
                style="
                  font-weight: 400;
                  font-style: normal;
                  font-size: 10px;
                  color: rgba(0, 0, 0, 0.447058823529412);
                  line-height: 22px;
                "
              >
                {{ $t("form.configureLabelAndStain") }}
              </div>
            </div>
          </template>
          <h4 class="nav">{{ $t("form.label") }}:</h4>
          <div>
            <el-table
              :data="labelData"
              style="width: 95%; margin-left: 20px"
              border
              size="small"
            >
              <el-table-column :label="$t('form.parameterName')" width="120">
                <template slot-scope="scope">
                  <div v-if="scope.row.type === '未定义'">
                    <p id="uncolor">{{ scope.row.show_name }}</p>
                  </div>
                  <div v-if="scope.row.type != '未定义'">
                    {{ scope.row.show_name }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.type')" width="100">
                <template slot-scope="scope">
                  <div v-if="scope.row.type === '未定义'">
                    <p id="uncolor">{{ scope.row.type }}</p>
                  </div>
                  <div v-if="scope.row.type != '未定义'">
                    {{ scope.row.type }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.parameterValue')">
                <template slot-scope="scope">
                  <div v-if="scope.row.type === 'bool'" style="width: 260px">
                    <el-radio-group
                      v-model="scope.row.default_value"
                      :disabled="!scope.row.immutable"
                      size="small"
                    >
                      <el-radio :label="true">{{ $t("form.yes") }}</el-radio>
                      <el-radio :label="false">{{ $t("form.no") }}</el-radio>
                    </el-radio-group>
                  </div>
                  <div
                    v-if="
                      scope.row.type === 'string' &&
                      scope.row.key.indexOf('bp') == -1 &&
                      scope.row.key.indexOf('owner') == -1
                    "
                  >
                    <el-input
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div v-if="scope.row.type === 'enum'">
                    <el-select
                      v-model="scope.row.default_value"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 260px"
                      clearable
                      :disabled="!scope.row.immutable"
                    >
                      <el-option
                        v-for="items in scope.row.options"
                        :key="items.index"
                        :value="items"
                      />
                    </el-select>
                  </div>
                  <div v-if="scope.row.type === 'float'">
                    <el-input-number
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :precision="2"
                      :step="0.1"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div v-if="scope.row.type === 'int'">
                    <el-input-number
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :min="0"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div v-if="scope.row.type === '未定义'">
                    <el-input
                      id="uncolor"
                      v-model="scope.row.default_value"
                      style="width: 260px; color: #67c23a !important"
                      size="small"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div
                    v-if="
                      scope.row.type === 'string' &&
                      scope.row.key.indexOf('bp') != -1
                    "
                  >
                    <el-select
                      v-model="scope.row.default_value"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 260px"
                      clearable
                      :disabled="!scope.row.immutable"
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
                    v-if="
                      scope.row.type === 'string' &&
                      scope.row.key.indexOf('owner') != -1
                    "
                  >
                    <el-select
                      v-model="scope.row.default_value"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 260px"
                      clearable
                      :disabled="!scope.row.immutable"
                    >
                      <el-option
                        v-for="items in labelUserData"
                        :key="items.id"
                        :label="items.name"
                        :value="items.id"
                      />
                    </el-select>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.required')" width="60">
                <template slot-scope="scope">
                  <div v-if="scope.row.type === '未定义'">
                    <p id="uncolor">{{ scope.row.required }}</p>
                  </div>
                  <div v-if="scope.row.type != '未定义'">
                    {{ scope.row.required }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('form.parameterDescription')"
                width="160"
              >
                <template slot-scope="scope">
                  {{ scope.row.description }}
                </template>
              </el-table-column>
            </el-table>
            <h4 class="nav">{{ $t("form.stain") }}:</h4>
            <el-table
              :data="stainData"
              style="width: 95%; margin-left: 20px"
              border
              size="small"
            >
              <el-table-column :label="$t('form.parameterName')" width="120">
                <template slot-scope="scope">
                  <div v-if="scope.row.type != '未定义'">
                    {{ scope.row.show_name }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.type')" width="100">
                <template slot-scope="scope">
                  <div v-if="scope.row.type != '未定义'">
                    {{ scope.row.type }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.parameterValue')">
                <template slot-scope="scope">
                  <div v-if="scope.row.type === 'bool'" style="width: 260px">
                    <el-radio-group
                      v-model="scope.row.default_value"
                      :disabled="!scope.row.immutable"
                      size="small"
                    >
                      <el-radio :label="true">{{ $t("form.yes") }}</el-radio>
                      <el-radio :label="false">{{ $t("form.no") }}</el-radio>
                    </el-radio-group>
                  </div>
                  <div
                    v-if="
                      scope.row.type === 'string' &&
                      scope.row.key.indexOf('bp') == -1 &&
                      scope.row.key.indexOf('owner') == -1
                    "
                  >
                    <el-input
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div v-if="scope.row.type === 'enum'">
                    <el-select
                      v-model="scope.row.default_value"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 260px"
                      clearable
                      :disabled="!scope.row.immutable"
                    >
                      <el-option
                        v-for="items in scope.row.options"
                        :key="items.index"
                        :value="items"
                      />
                    </el-select>
                  </div>
                  <div v-if="scope.row.type === 'float'">
                    <el-input-number
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :precision="2"
                      :step="0.1"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div v-if="scope.row.type === 'int'">
                    <el-input-number
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :min="0"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div v-if="scope.row.type === '-'">
                    <el-input
                      id="uncolor"
                      v-model="scope.row.default_value"
                      style="width: 260px"
                      size="small"
                      :disabled="!scope.row.immutable"
                    />
                  </div>
                  <div
                    v-if="
                      scope.row.type === 'string' &&
                      scope.row.key.indexOf('bp') != -1
                    "
                  >
                    <el-select
                      v-model="scope.row.default_value"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 260px"
                      clearable
                      :disabled="!scope.row.immutable"
                      @change="stainbpChange"
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
                    v-if="
                      scope.row.type === 'string' &&
                      scope.row.key.indexOf('owner') != -1
                    "
                  >
                    <el-select
                      v-model="scope.row.default_value"
                      :placeholder="$t('form.pleaseSelect')"
                      style="width: 260px"
                      clearable
                      :disabled="!scope.row.immutable"
                    >
                      <el-option
                        v-for="items in stainUserData"
                        :key="items.id"
                        :label="items.name"
                        :value="items.id"
                      />
                    </el-select>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.required')" width="60">
                <template slot-scope="scope">
                  <div v-if="scope.row.type != '未定义'">
                    {{ scope.row.required }}
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('form.parameterDescription')"
                width="160"
              >
                <template slot-scope="scope">
                  {{ scope.row.description }}
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-collapse-item>
      </el-collapse>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button type="text" size="small" @click="cancel">{{
        $t("form.cancel")
      }}</el-button>
      <el-button
        :loading="loading"
        type="primary"
        size="small"
        @click="doSubmit"
        >{{ $t("form.confirm") }}</el-button
      >
    </div>
  </el-dialog>
</template>

<script>
import {
  addRegions,
  gws,
  editRegions,
  getLabelOption,
  getTaintOptions,
  uniqueness,
  getBps,
  getUsers,
} from "@/api/mainApi";
import { mapGetters } from "vuex";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    return {
      // 选择的组织机构ID
      labelbp_id: "",
      stainbp_id: "",
      nodes: [],
      isAdd: true,
      loading: false,
      dialog: false,
      radio: false,
      activeNames: [""],
      websock: "",
      id: "",
      form: {
        id: "",
        name: "",
        description: "",
        gw_node_ids: [],
        labels: {},
        taints: {},
      },
      // 表格数据
      labelData: [],
      stainData: [],
      // 折中数据
      labelsData: [],
      stainsData: [],
      // 禁用表格
      disabled: true,
      radio: false,
      // ID重复标识
      id_flag: false,
      // 组织机构数据
      labelBpsData: [],
      // 用户
      labelUserData: [],
      // 组织机构数据
      stainBpsData: [],
      // 用户
      stainUserData: [],
      rules: {
        id: [
          {
            required: true,
            message: this.$t("message.pleaseInputRegionId"),
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputRegionName"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  beforeDestroy() {
    const that = this;
    if (that.websock != "") {
      that.websock.close();
    }
  },
  created() {
    const that = this;
    that.labelbpsinit();
    that.labeluserinit();
    that.stainbpsinit();
  },
  mounted() {},
  methods: {
    // 标签组织机构改变
    labelbpChange(value) {
      const that = this;
      that.labelbp_id = value;
      that.labeluserinit();
    },
    stainbpChange(value) {
      const that = this;
      that.stainbp_id = value;
      that.stainbpsinit();
    },
    // Input 失去光标事件
    onChange() {
      if (this.form.id != "" && this.form.id != undefined) {
        var data = {
          region_id: this.form.id,
          site_id: "",
        };

        uniqueness(data)
          .then((res) => {
            if (res.region_id == true) {
              this.id_flag = false;
            } else {
              this.id_flag = true;
              this.$notify({
                title: this.$t("message.regionIdExists"),
                type: "error",
                duration: 2500,
              });
            }
          })
          .catch(() => {});
      } else {
        this.form.id = "";
        this.id_flag = true;
      }
    },

    // 展示labeloption
    async getLabelOption() {
      const that = this;
      that.labelsData = [];
      const list = await getLabelOption("region");
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          list[i].required == true
            ? (list[i].required = this.$t("form.yes"))
            : (list[i].required = this.$t("form.no"));
        }
        that.labelsData = list;
      }
    },
    async getTaintOptions() {
      const that = this;
      that.stainsData = [];
      const list = await getTaintOptions("region");
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          list[i].required == true
            ? (list[i].required = this.$t("form.yes"))
            : (list[i].required = this.$t("form.no"));
        }
        that.stainsData = list;
      }
    },
    async onOpen() {
      const that = this;
      const list = await gws();
      that.nodes = list.nodes.map((item) => {
        return {
          key: item.node_id,
          value: item.node_id,
          label: item.node_name,
        };
      });

      if (that.isAdd === false) {
        that.$set(that.form, "id", that.info.id);
        that.$set(that.form, "name", that.info.name);
        that.$set(that.form, "description", that.info.description);
        that.$set(that.form, "labels", that.info.labels);
        that.$set(that.form, "taints", that.info.taints);
        that.$set(
          that.form,
          "gw_node_ids",
          that.info.gw_node_ids.map((item) => {
            return item.id;
          })
        );
        that.stainData = [];
        that.labelData = [];
        var keyValue = [];
        var formLabels = [];
        if (that.info.labels && JSON.stringify(that.info.labels) != "{}") {
          Object.keys(that.info.labels).forEach(function (item, index) {
            formLabels.push(item);
          });
          for (var i = 0; i < that.labelsData.length; i++) {
            if (formLabels.indexOf(that.labelsData[i].key) != -1) {
              that.labelsData[i].default_value =
                that.info.labels[that.labelsData[i].key];
              keyValue.push(that.labelsData[i].key);
              if (that.labelsData[i].key.indexOf("bpId") != -1) {
                that.labelbp_id = that.info.labels[that.labelsData[i].key];
                that.labeluserinit(1);
              }
            }
            that.labelData.push(that.labelsData[i]);
          }
          for (var j = 0; j < formLabels.length; j++) {
            if (keyValue.indexOf(formLabels[j]) == -1) {
              that.labelData.push({
                key: formLabels[j],
                show_name: formLabels[j].split("/")[1],
                default_value: that.info.labels[formLabels[j]],
                type: "-",
                immutable: "true",
                description: "",
                required: this.$t("form.no"),
              });
            }
          }
        } else {
          that.labelData = that.labelsData;
        }
        var taintsKeyValue = [];
        var taintsValue = [];
        if (that.info.taints && JSON.stringify(that.info.taints) != "{}") {
          Object.keys(that.info.taints).forEach(function (item, index) {
            taintsValue.push(item);
          });
          for (var i = 0; i < that.stainsData.length; i++) {
            if (taintsValue.indexOf(that.stainsData[i].key) != -1) {
              that.stainsData[i].default_value =
                that.info.taints[that.stainsData[i].key];
              taintsKeyValue.push(that.stainsData[i].key);
            }
            if (that.stainsData[i].key.indexOf("bpId") != -1) {
              that.stainbp_id = that.info.taints[that.stainsData[i].key];
              that.stainuserinit(1);
            }
            that.stainData.push(that.stainsData[i]);
          }
          for (var j = 0; j < taintsValue.length; j++) {
            if (taintsKeyValue.indexOf(taintsValue[j]) == -1) {
              that.stainData.push({
                key: taintsValue[j],
                show_name: taintsValue[j].split("/")[1],
                default_value: that.info.taints[taintsValue[j]],
                type: "-",
                immutable: "true",
                description: "",
                required: this.$t("form.no"),
              });
            }
          }
        } else {
          that.stainData = that.stainsData;
        }
      } else {
        that.stainData = [];
        that.labelData = [];
        that.labelData = that.labelsData;
        that.stainData = that.stainsData;
      }
    },
    cancel() {
      const that = this;
      that.resetForm();
    },
    async labelbpsinit() {
      const that = this;
      const list = await getBps();
      that.labelBpsData = list.bps;
    },
    async labeluserinit(value) {
      const that = this;
      that.labelUserData = [];
      const params = {};
      if (that.labelbp_id != "" && that.labelbp_id != undefined) {
        params.bp_id = that.labelbp_id;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      if (list.users != undefined && list.users != null) {
        that.labelUserData = list.users;
      } else {
        that.labelUserData = [];
      }
      for (var i = 0; i < that.labelData.length; i++) {
        if (that.labelData[i].key.indexOf("owner") != -1) {
          value == "1"
            ? (that.labelData[i].default_value =
                that.labelData[i].default_value)
            : (that.labelData[i].default_value = "");
        }
      }
    },
    async stainbpsinit() {
      const that = this;
      const list = await getBps();
      that.stainBpsData = list.bps;
    },
    async stainuserinit(value) {
      const that = this;
      that.stainUserData = [];
      const params = {};
      if (that.stainbp_id != "" && that.stainbp_id != undefined) {
        params.bp_id = that.stainbp_id;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      if (list.users != undefined && list.users != null) {
        that.stainUserData = list.users;
      } else {
        that.stainUserData = [];
      }
      for (var i = 0; i < that.stainData.length; i++) {
        if (that.stainData[i].key.indexOf("owner") != -1) {
          value == "1"
            ? (that.stainData[i].default_value =
                that.stainData[i].default_value)
            : (that.stainData[i].default_value = "");
        }
      }
    },
    doSubmit() {
      const that = this;
      if (that.id_flag == true) {
        return;
      }
      that.$refs["form"].validate((valid) => {
        if (valid) {
          let flag = true;
          const labels = that.labelData.filter(
            (item) => item.key || item.default_value
          );
          const taints = that.stainData.filter(
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
                title: this.$t("message.pleaseInputTaintKey"),
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
                title: this.$t("message.pleaseInputKey"),
                type: "info",
                duration: 2500,
              });
              flag = false;
              return false;
            }
          });
          if (flag) {
            that.form["labels"] = labelsObj;
            that.form["taints"] = taintsObj;
            that.loading = true;
            if (that.isAdd) {
              that.doAdd();
            } else that.doEdit();
          }
        } else {
          return false;
        }
      });
    },
    doAdd() {
      var data = JSON.parse(JSON.stringify(this.form));

      data.id = "";
      addRegions(data)
        .then(() => {
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.init();
          this.resetForm();
        })
        .catch(() => {
          this.loading = false;
        });
    },
    doEdit() {
      const that = this;
      editRegions(that.id, that.form)
        .then(() => {
          that.resetForm();
          that.$notify({
            title: this.$t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          that.loading = false;
          that.$parent.init();
        })
        .catch(() => {
          that.loading = false;
        });
    },
    handleCloseDialog() {
      const that = this;
      that.resetForm();
    },
    resetForm() {
      const that = this;
      that.dialog = false;
      if (that.$refs["form"] !== undefined) {
        that.$refs["form"].resetFields();
      }
      that.form = {
        id: "",
        name: "",
        description: "",
        gw_node_ids: [],
        labels: {},
        taints: {},
      };
      that.labelData = [];
      that.stainData = [];
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

.font {
  font-family: "PingFangSC-Regular", "PingFang SC", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 10px;
  line-height: 22px;
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

::v-deep .el-table__empty-block {
  display: none !important;
}

::v-deep .is-leaf {
  background: #f7f7f7 !important;
  padding: 5px 0;
}

::v-deep .el-collapse-item__header {
  background-color: rgba(250, 250, 250, 1) !important;
  margin-bottom: 5px;
}

.form {
  .el-table td,
  .el-table th {
    padding: 5px 0;
  }
}

::v-deep .el-form-item__content {
  line-height: 32px;
  position: relative;
  font-size: 14px;
}

::v-deep .el-input__inner {
  color: #606266;
}

::v-deep #uncolor {
  color: #f54518 !important;
}

::v-deep .el-form-item--small .el-form-item__content,
.el-form-item--small .el-form-item__label {
  line-height: 32px;
  margin-top: 7px;
}
</style>
