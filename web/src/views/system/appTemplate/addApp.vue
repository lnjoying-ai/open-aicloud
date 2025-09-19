<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.createApp') : $t('form.editApp')"
      width="800px"
      class="dialogMain"
      :before-close="handleCloseDialog"
      :close-on-click-modal="false"
      @open="onOpen"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="145px"
      >
        <el-form-item :label="$t('form.appName') + ':'" prop="name">
          <el-input v-model="form.name" :placeholder="$t('form.pleaseInput')" />
        </el-form-item>
        <el-row type="flex">
          <el-form-item
            :label="$t('form.appTemplate') + ':'"
            prop="template_id"
          >
            <el-select
              v-model="form.template_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              :disabled="disabled"
            >
              <el-option
                v-for="item in templatesData"
                :key="item.index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('form.templateVersion') + ':'">
            <el-input
              v-model="nowVersion"
              :disabled="true"
              :placeholder="$t('form.pleaseInput')"
              readonly
            />
          </el-form-item>
        </el-row>
        <el-form-item
          :label="$t('form.autoRun') + ':'"
          prop="auto_run"
          style="margin-top: 20px"
        >
          <el-select
            v-model="form.auto_run"
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option :label="$t('form.yes')" :value="true" />
            <el-option :label="$t('form.no')" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('form.replicaNumber') + ':'"
          prop="replica_num"
        >
          <el-input-number
            v-model="form.replica_num"
            :min="1"
            :placeholder="$t('form.pleaseInput')"
            style="min-width: 215px"
          />
        </el-form-item>
        <el-row>
          <el-form-item style="float: left; display: inline-block">
            <div slot="label">{{ $t("form.region") + ":" }}</div>
            <el-select
              v-model="form.target_nodes.dst_region_id"
              filterable
              clearable
              :placeholder="$t('form.pleaseSelect')"
            >
              <el-option
                v-for="(item, index) in arealist"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item style="display: inline-block">
            <div slot="label">{{ $t("form.site") + ":" }}</div>
            <el-select
              v-model="form.target_nodes.dst_site_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              clearable
            >
              <el-option
                v-for="(item, index) in website"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item style="display: inline-block">
            <div slot="label">{{ $t("form.node") + ":" }}</div>
            <el-select
              v-model="form.target_nodes.dst_node_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              clearable
            >
              <el-option
                v-for="(item, index) in nodeList"
                :key="index"
                :label="item.node_name"
                :value="item.node_id"
              />
            </el-select>
          </el-form-item>
        </el-row>
        <el-row type="flex">
          <el-form-item
            v-if="cooperative"
            :label="$t('form.bp') + ':'"
            prop="bp_id"
          >
            <el-select
              v-model="form.bp_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              clearable
              @clear="clear"
              @change="bpsChange"
            >
              <el-option
                v-for="item in bpsData"
                :key="item.index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            v-if="userStatus"
            :label="$t('form.user') + ':'"
            prop="user_id"
          >
            <el-select
              v-model="form.user_id"
              :placeholder="$t('form.pleaseSelect')"
              clearable
              filterable
              @change="handleUserChange"
            >
              <el-option
                v-for="(item, index) in usersData"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-row>
        <!-- @change="change_registry_id" -->
        <el-form-item
          :label="$t('form.imageRepository') + ':'"
          prop="registry_id"
        >
          <el-select
            v-model="form.registry_id"
            :placeholder="$t('form.pleaseSelect')"
            filterable
            clearable
          >
            <el-option
              v-for="(item, index) in registriesData"
              :key="index"
              :label="item.registry_name"
              :value="item.registry_id"
            />
          </el-select>
          <div class="tip1" style="padding-left: 10px; display: inline-block">
            {{ $t("form.registryTip") }}
          </div>
        </el-form-item>
        <el-form-item :label="$t('form.imagePullPolicy') + ':'">
          <el-select
            v-model="form.extra_params.image_pull_policy"
            :placeholder="$t('form.pleaseSelect')"
            @change="changePullpolicy"
          >
            <el-option :label="$t('form.alwaysPull')" value="Always" />
            <el-option :label="$t('form.ifNotPresent')" value="IfNotPresent" />
            <el-option :label="$t('form.neverPull')" value="Never" />
          </el-select>
        </el-form-item>
        <el-collapse v-model="activeNames">
          <el-collapse-item name="1">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.parameterSettings") }}
              </div>
            </template>
            <el-form-item
              v-for="(item, index) in nowTemp"
              :key="index"
              :label="item.label"
              :prop="item.variable"
              label-width="200px"
            >
              <el-select
                v-if="item.type == 'enum'"
                v-model="show_inputs[item.variable]"
                style="width: 400px"
                :placeholder="item.description"
              >
                <el-option
                  v-for="(item1, index1) in item.options"
                  :key="index1"
                  :label="item1"
                  :value="item1"
                />
              </el-select>
              <el-select
                v-if="item.type == 'CfgOption'"
                v-model="show_inputs[item.variable]"
                style="width: 400px"
                :placeholder="item.description"
                value-key="id"
              >
                <el-option
                  v-for="(item1, index1) in fileList"
                  :key="index1"
                  :label="`${item1.group} / ${item1.dataId}`"
                  :value="item1"
                />
              </el-select>
              <el-input
                v-if="item.type == 'string'"
                v-model="show_inputs[item.variable]"
                style="width: 400px"
                :placeholder="item.description"
              />
              <el-tooltip
                class="item"
                effect="dark"
                :content="item.description"
                placement="top-end"
              >
                <i class="el-icon-info" />
              </el-tooltip>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
        <el-collapse v-model="activeNames1">
          <el-collapse-item name="1">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.deviceRequirements") }}
              </div>
            </template>
            <div class="Dev_need_info" style="margin-left: 45px">
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.cpuResourceDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.quantity") }}:</div>
                  <el-input-number
                    v-model="form.dev_need_info.cpu.cpu_num"
                    class="aaa"
                    :min="0"
                    :placeholder="$t('form.quantity')"
                  />
                </el-form-item>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.model") }}:</div>
                  <el-input
                    v-model="form.dev_need_info.cpu.cpu_model"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.memoryResourceDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.size") }}(MB):</div>
                  <el-input-number
                    v-model="form.dev_need_info.mem.mem_limit"
                    :min="0"
                    :placeholder="$t('form.size')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.diskResourceDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.type") }}:</div>
                  <el-select
                    v-model="form.dev_need_info.disk.disk_type"
                    :placeholder="$t('form.pleaseSelect')"
                  >
                    <el-option label="SSD" value="SSD" />
                    <el-option label="HDD" value="HDD" />
                  </el-select>
                </el-form-item>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.size") }}(MB):</div>
                  <el-input-number
                    v-model="form.dev_need_info.disk.disk_limit"
                    :min="0"
                    :placeholder="$t('form.size')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.networkBandResourceDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.sendBandwidth") }}:</div>
                  <el-input-number
                    v-model="form.dev_need_info.network_band_need.transmit_band"
                    :min="0"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.receiveBandwidth") }}:</div>
                  <el-input-number
                    v-model="form.dev_need_info.network_band_need.recv_band"
                    :min="0"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
              </div>
            </div>
            <el-form-item
              :label="$t('form.gpu') + ':'"
              label-width="120px"
              class="h6 tal formContentBlock"
            >
              <el-radio-group v-model="gpuForm.status">
                <el-radio :label="true">{{ $t("form.yes") }}</el-radio>
                <el-radio :label="false">{{ $t("form.no") }}</el-radio>
              </el-radio-group>
              <div
                class="tip"
                style="line-height: 32px; color: #333; display: inline-block"
              >
                {{ $t("form.gpuTip") }}
              </div>
            </el-form-item>
            <div v-if="gpuForm.status" class="Dev_need_info">
              <div class="Dev_need_info_item">
                <el-form-item>
                  <div slot="label">{{ $t("form.graphicsCardType") }}:</div>
                  <el-input
                    v-model="gpuForm.gpu_type"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.graphicsCardModel") }}:</div>
                  <el-input
                    v-model="gpuForm.gpu_model"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.graphicsCardNumber") }}:</div>
                  <el-input-number
                    v-model="gpuForm.gpu_num"
                    :min="1"
                    :placeholder="$t('form.graphicsCardNumber')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.driverVersion") }}:</div>
                  <el-input
                    v-model="gpuForm.driver_version"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <el-form-item>
                  <div slot="label">CUDA:</div>
                  <el-input
                    v-model="gpuForm.cuda_version"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">cuDNN:</div>
                  <el-input
                    v-model="gpuForm.cudnn_version"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>

                <el-form-item>
                  <div slot="label">{{ $t("form.driverCapabilities") }}:</div>
                  <el-input
                    v-model="gpuForm.driver_capabilities"
                    type="text"
                    :placeholder="$t('form.pleaseInput')"
                  />
                </el-form-item>
              </div>
            </div>
          </el-collapse-item>
          <el-collapse-item name="2">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.scheduling") }}
              </div>
            </template>
            <div class="label" style="margin-left: 45px">
              <div style="margin: 10px 0">{{ $t("form.site") }}</div>
              <el-table :data="siteData" style="width: 100%" size="mini" border>
                <el-table-column :label="$t('form.type')" align="center">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" size="mini" clearable>
                      <el-option label="Must" value="Must" />
                      <el-option label="Prefer" value="Prefer" />
                      <el-option label="MustNot" value="MustNot" />
                      <el-option label="PreferNot" value="PreferNot" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.key')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.key" size="mini" />
                  </template>
                </el-table-column>
                <el-table-column
                  :label="$t('form.operatorWithV')"
                  align="center"
                >
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.operator"
                      size="mini"
                      clearable
                    >
                      <el-option label="In" value="In" />
                      <el-option label="NotIn" value="NotIn" />
                      <el-option label="Exists" value="Exists" />
                      <el-option label="NotExists" value="NotExists" />
                      <el-option label="Gt" value="Gt" />
                      <el-option label="Lt" value="Lt" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.value')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.value" size="mini" />
                  </template>
                </el-table-column>

                <el-table-column width="100" align="center">
                  <template slot="header">
                    <el-button size="mini" @click="handleAddSite">+</el-button>
                  </template>
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      @click="handlRemoveSite(scope.$index, scope.row)"
                      >-</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>

              <div style="margin: 10px 0">{{ $t("form.node") }}</div>
              <el-table :data="nodeData" style="width: 100%" size="mini" border>
                <el-table-column :label="$t('form.type')" align="center">
                  <template slot-scope="scope">
                    <el-select v-model="scope.row.type" size="mini" clearable>
                      <el-option label="Must" value="Must" />
                      <el-option label="Prefer" value="Prefer" />
                      <el-option label="MustNot" value="MustNot" />
                      <el-option label="PreferNot" value="PreferNot" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.key')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.key" size="mini" />
                  </template>
                </el-table-column>
                <el-table-column
                  :label="$t('form.operatorWithV')"
                  align="center"
                >
                  <template slot-scope="scope">
                    <el-select
                      v-model="scope.row.operator"
                      size="mini"
                      clearable
                    >
                      <el-option label="In" value="In" />
                      <el-option label="NotIn" value="NotIn" />
                      <el-option label="Exists" value="Exists" />
                      <el-option label="NotExists" value="NotExists" />
                      <el-option label="Gt" value="Gt" />
                      <el-option label="Lt" value="Lt" />
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.value')" align="center">
                  <template slot-scope="scope">
                    <el-input v-model="scope.row.value" size="mini" />
                  </template>
                </el-table-column>
                <el-table-column width="100" align="center">
                  <template slot="header">
                    <el-button size="mini" @click="handleAddNode">+</el-button>
                  </template>
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      @click="handlRemoveNode(scope.$index, scope.row)"
                      >-</el-button
                    >
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-collapse-item>
        </el-collapse>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="text" size="small" @click="cancel">
          {{ $t("form.cancel") }}
        </el-button>
        <el-button
          :loading="loading"
          type="primary"
          size="small"
          @click="doSubmit"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  addDockerStacks,
  editTemplates,
  getBps,
  getUsers,
  templates,
  getRegions,
  getSites,
  edges,
  registries,
  registries3rd,
  configs,
} from "@/api/mainApi";
import { mapGetters } from "vuex";
export default {
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: "",
    template: "",
    nowVersion: "",
  },
  data() {
    return {
      // 配置文件
      fileList: [],
      // 合作
      cooperative: true,
      // 用户
      userStatus: true,
      arealist: [],
      activeNames: "",
      activeNames1: "",
      bpsData: [],
      usersData: [],
      siteData: [],
      nodeData: [],
      nowTemp: [],
      registriesData: [],
      registries3rd: [],
      show_inputs: {},
      templatesData: [],
      regions: [],
      website: [],
      nodeList: [],
      sites: [],
      isAdd: true,
      disabled: false,
      loading: false,
      dialog: false,
      gpuForm: {
        status: false,
        gpu_type: "nvidia",
        gpu_model: "",
        gpu_num: 1,
        driver_version: "",
        cuda_version: "",
        cudnn_version: "",
        visible_devices: "",
        driver_capabilities: "",
      },
      form: {
        name: "",
        template_id: "",
        auto_run: true,
        replica_num: 1,
        bp_id: "",
        user_id: "",
        scheduling_strategy: {
          label_selector_map: {},
        },
        registry_id: "",
        dev_need_info: {
          cpu: {
            cpu_num: "",
            cpu_model: "",
          },
          mem: {
            mem_limit: "",
          },
          disk: {
            disk_type: "",
            disk_limit: "",
          },
          gpu: {},

          network_band_need: {
            transmit_band: "",
            recv_band: "",
          },
        },
        target_nodes: {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        },
        extra_params: {
          image_pull_policy: "IfNotPresent",
        },
      },
      inputVisible: false,
      inputValue: "",
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputTemplateName"),
            trigger: "blur",
          },
        ],
        version: [
          {
            required: true,
            message: this.$t("form.pleaseInputTemplateVersion"),
            trigger: "blur",
          },
        ],
      },
    };
  },
  watch: {
    "form.template_id": {
      deep: true,
      handler(val) {
        var data = this.templatesData.filter((item) => {
          return val == item.id;
        })[0];
        if (data != undefined && data != "") {
          this.nowTemp = data.show_inputs;
          this.nowTemp.forEach((item) => {
            this.$set(this.show_inputs, item.variable, item.default_value);
          });
        }
      },
    },
    "form.user_id": {
      deep: true,
      handler(val) {
        this.registriesData = [];
        this.form.registry_id = "";
        if (
          this.form.target_nodes.dst_region_id != "" &&
          this.form.target_nodes.dst_region_id != undefined
        ) {
          this.registries3rd_R();
          this.registries();
        } else {
          this.registries3rd_R();
        }
      },
    },
    "form.target_nodes.dst_region_id": {
      deep: true,
      handler(val) {
        if (val) {
          this.form.registry_id = "";
          this.form.target_nodes.dst_site_id = "";
          this.form.target_nodes.dst_node_id = "";
          this.websiteinit();
          if (this.form.user_id != "" && this.form.user_id != undefined) {
            this.registries3rd_R();
            this.registries();
          } else {
            this.registries();
          }
        } else {
          this.form.registry_id = "";
          this.form.target_nodes.dst_site_id = "";
          this.form.target_nodes.dst_node_id = "";
          this.website = [];
          this.nodeList = [];
          if (this.form.user_id) {
            this.registries3rd_R();
          }
        }
      },
    },
    "form.target_nodes.dst_site_id": {
      deep: true,
      handler(val) {
        if (val) {
          this.form.target_nodes.dst_node_id = "";
          this.edgesinit();
        } else {
          this.form.target_nodes.dst_node_id = "";
          this.nodeList = [];
        }
      },
    },
  },
  mounted() {
    this.areainit();
    this.usersInit();
    this.bpsInit();
    this.templatesInit();
    this.getappConfigure();
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  created() {
    if (this.userInfo.kind == 4) {
      this.cooperative = false;
      this.userStatus = false;
      this.form.user_id = this.userInfo.id;
      this.form.bp_id = this.userInfo.bp_id;
    } else if (this.userInfo.kind == 2) {
      this.cooperative = false;
      this.userStatus = true;
      this.form.bp_id = this.userInfo.bp_id;
    } else {
      this.cooperative = true;
      this.userStatus = true;
      this.form.user_id = "";
      this.form.bp_id = "";
    }
  },
  methods: {
    handleUserChange(value) {
      this.form.user_id = value;
      this.getappConfigure(value);
    },
    // 配置文件数据
    async getappConfigure(value) {
      const list = await configs({ namespace: value });
      this.fileList = list.pageItems;
    },
    async usersInit() {
      const listUsers = await getUsers(this.queryData);
      this.usersData = listUsers.users;
    },
    async bpsInit() {
      const listBps = await getBps(this.queryData);
      this.bpsData = listBps.bps;
    },
    async templatesInit() {
      const listTemplates = await templates(this.queryData);
      this.templatesData = [];
      if (listTemplates.templates != null) {
        listTemplates.templates.forEach((item) => {
          if (item.versions.length > 0) {
            item.versions.forEach((item1) => {
              this.templatesData.push(item1);
            });
          }
        });
      }
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
      if (this.arealist != null && this.arealist.length > 0) {
        this.form.target_nodes.dst_region_id = this.arealist[0].id;
        this.websiteinit();
        this.registries();
      }
    },
    async websiteinit() {
      const params = {};
      params.region_id = this.form.target_nodes.dst_region_id;
      const list = await getSites(params);
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
      if (this.website != undefined && this.website.length > 0) {
        this.form.target_nodes.dst_site_id = this.website[0].id;
        this.edgesinit();
      }
    },
    async registries() {
      this.registriesData.length = 0;
      const params = {};
      params.region_id = this.form.target_nodes.dst_region_id;
      const list = await registries(params);
      this.registriesData = list.registries;
      if (
        this.form.user_id != "" &&
        this.form.user_id != undefined &&
        this.registries3rdData != undefined &&
        this.registries3rdData.length > 0
      ) {
        for (var i = 0; i < this.registries3rdData.length; i++) {
          this.registriesData.push(this.registries3rdData[i]);
        }
      }
    },
    async registries3rd_R() {
      const params = {};
      params.user_id = this.form.user_id;
      this.registries3rdData = [];
      if (
        this.form.user_id != "" &&
        this.form.user_id != undefined &&
        this.form.target_nodes.dst_region_id != "" &&
        this.form.target_nodes.dst_region_id != undefined
      ) {
        var list = await registries3rd(params);
        this.registries3rdData = list.registries;
      } else if (this.form.user_id != "" && this.form.user_id != undefined) {
        var list = await registries3rd(params);
        this.registriesData = list.registries;
      }
    },
    async edgesinit() {
      if (
        this.form.target_nodes.dst_site_id != undefined &&
        this.form.target_nodes.dst_site_id != ""
      ) {
        const params = {};
        params.site = this.form.target_nodes.dst_site_id;
        const list = await edges(params);
        this.nodeList = list.nodes;
        if (this.nodeList != null && this.nodeList.length > 0) {
          this.form.target_nodes.dst_node_id = this.nodeList[0].node_id;
        }
      } else {
        this.nodeList = [];
      }
    },
    async onOpen() {
      if (this.isAdd === false) {
        this.$set(this.form, "id", this.info.id);
        this.$set(this.form, "name", this.info.name);
        this.$set(this.form, "version", this.info.version);
        this.$set(this.form, "vendor", this.info.vendor);
        this.disabled = false;
      } else {
        this.disabled = true;
      }
      this.$set(this.form, "template_id", this.template);
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAdd) {
            this.doAdd();
          } else this.doEdit();
        } else {
          return false;
        }
      });
    },
    doAdd() {
      var data = JSON.parse(JSON.stringify(this.form));
      if (this.gpuForm.status) {
        data.dev_need_info.gpu = {
          gpu_type: this.gpuForm.gpu_type,
          gpu_model: this.gpuForm.gpu_model,
          gpu_num: this.gpuForm.gpu_num,
          driver_version: this.gpuForm.driver_version,
          cuda_version: this.gpuForm.cuda_version,
          cudnn_version: this.gpuForm.cudnn_version,
          visible_devices: this.gpuForm.visible_devices,
          driver_capabilities: this.gpuForm.driver_capabilities,
        };
      }

      for (var i in this.show_inputs) {
        if (typeof this.show_inputs[i] === "object") {
          this.show_inputs[i] =
            "cfg://" +
            this.show_inputs[i].tenant +
            "/" +
            this.show_inputs[i].group +
            "/" +
            this.show_inputs[i].dataId;
        }
      }
      data.input_params = this.show_inputs;
      for (var key in data.dev_need_info) {
        for (var key1 in data.dev_need_info[key]) {
          if (
            data.dev_need_info[key][key1] === undefined ||
            data.dev_need_info[key][key1] === "" ||
            JSON.stringify(data.dev_need_info[key][key1]) === "{}"
          ) {
            delete data.dev_need_info[key][key1];
          }
        }

        if (
          data.dev_need_info[key] === undefined ||
          data.dev_need_info[key] === "" ||
          JSON.stringify(data.dev_need_info[key]) === "{}"
        ) {
          delete data.dev_need_info[key];
        }
      }

      var target_nodes = {
        dst_region_id: "",
        dst_site_id: "",
        dst_node_id: "",
      };
      if (data.target_nodes.dst_region_id != "") {
        target_nodes.dst_region_id = data.target_nodes.dst_region_id;
      }
      if (data.target_nodes.dst_site_id != "") {
        target_nodes.dst_site_id = data.target_nodes.dst_site_id;
      }

      if (data.target_nodes.dst_node_id != "") {
        target_nodes.dst_node_id = data.target_nodes.dst_node_id;
      }
      data.target_nodes = [];
      data.target_nodes.push(target_nodes);
      data.scheduling_strategy.label_selector_map = {
        node: this.nodeData,
        site: this.siteData,
      };
      let allValuesDefined = true;

      for (const key in this.show_inputs) {
        if (!this.show_inputs[key] && this.show_inputs[key] !== 0) {
          allValuesDefined = false;
          break;
        }
      }
      if (allValuesDefined) {
        addDockerStacks(data)
          .then((res) => {
            this.$notify({
              title: $t("message.addSuccess"),
              type: "success",
              duration: 2500,
            });
            this.dialog = false;
            this.$parent.init();
            this.resetForm();
            this.loading = false;
          })
          .catch((err) => {
            this.loading = false;
          });
      } else {
        this.$notify({
          title: $t("message.pleaseCompleteParameterSettingInformation"),
          type: "warning",
        });
        this.loading = false;
      }
    },
    doEdit() {
      editTemplates(this.form, this.form.id)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: $t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    handleCloseDialog() {
      this.resetForm();
    },
    // 添加 站点
    handleAddSite() {
      this.siteData.push({ label_type: "node" });
    },
    handlRemoveSite(index, row) {
      this.siteData.splice(index, 1);
    },
    // 添加节点
    handleAddNode() {
      this.nodeData.push({ label_type: "site" });
    },
    handlRemoveNode(index, row) {
      this.nodeData.splice(index, 1);
    },
    // 组织机构改变触发
    bpsChange(val) {
      this.form.bp_id = val;
      this.form.user_id = "";
      this.usersInit();
      if (
        this.form.target_nodes.dst_region_id != "" &&
        this.form.target_nodes.dst_region_id != undefined
      ) {
        this.registries();
      }
    },
    clear() {
      this.form.user_id = "";
    },

    // 镜像仓库改变触发
    change_registry_id(val) {
      this.form.registry_id = val;
    },
    // 策略
    changePullpolicy(value) {
      this.form.extra_params.image_pull_policy = value;
    },
    resetForm() {
      this.dialog = false;
      this.nowTemp = [];
      if (this.$refs["form"] !== undefined) {
        this.$refs["form"].resetFields();
      }
      this.form = {
        name: "",
        template_id: "",
        auto_run: true,
        replica_num: 1,
        bp_id: "",
        user_id: "",
        scheduling_strategy: {
          label_selector_map: {},
        },
        registry_id: "",
        dev_need_info: {
          cpu: {
            cpu_num: "",
            cpu_model: "",
          },
          mem: {
            mem_limit: "",
          },
          disk: {
            disk_type: "",
            disk_limit: "",
          },
          gpu: {},

          network_band_need: {
            transmit_band: "",
            recv_band: "",
          },
        },
        target_nodes: {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        },
        extra_params: {
          image_pull_policy: "IfNotPresent",
        },
      };
    },
  },
};
</script>

<style lang="scss" scoped>
.tip {
  font-size: 12px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #999999;
  margin-left: 40px;
}

.tip1 {
  color: #fbb561;
  text-align: left;
  padding-left: 80px;
  font-size: 12px;
}

.dialogMain {
  .Dev_need_info {
    padding-left: 0px;
    overflow: hidden;

    h6 {
      margin: 30px 0 10px;
    }

    .el-form-item {
      padding-left: 20px;

      .el-input {
        width: 100%;
      }

      .el-select {
        width: 100%;
      }

      .el-input-number {
        width: 100%;
      }
    }
  }
}
</style>
