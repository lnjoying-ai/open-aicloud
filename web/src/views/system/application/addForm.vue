<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.addApplication') : $t('form.editApplication')"
      width="800px"
      class="dialogMain"
      :close-on-click-modal="false"
      @open="onOpen"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="130px"
      >
        <el-form-item :label="$t('form.applicationName') + ':'" prop="name">
          <el-input
            v-model="form.name"
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>

        <el-form-item
          :label="$t('form.applicationDescription') + ':'"
          prop="description"
        >
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.aosType') + ':'" prop="aos_type">
          <el-select
            v-model="form.aos_type"
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option
              key="docker-compose"
              label="docker-compose"
              value="docker-compose"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('form.applicationFormat') + ':'"
          prop="content_type"
        >
          <el-input
            v-model="form.content_type"
            :placeholder="$t('form.pleaseInputContent')"
          />
        </el-form-item>
        <el-form-item label="stack_compose:" prop="stack_compose">
          <el-button
            type="primary"
            size="mini"
            class="drbtn"
            @click="clickLoad"
            >{{ $t("form.import") }}</el-button
          >
          <input
            id="files"
            ref="refFile"
            type="file"
            style="display: none"
            @change="fileLoad"
          />
          <yaml-editor
            v-if="dialog"
            ref="yamlEditor"
            v-model="form.stack_compose"
            style="
              line-height: 1.2;
              max-height: 350px;
              overflow: auto;
              border-radius: 4px;
              margin-top: 10px;
              min-width: 100%;
            "
            :download-name="'addApplication-stack_compose'"
            :download-type="'yml'"
            :readonly="''"
            :lint="true"
            :placeholder="stackPlaceholder"
            :is-add="isAdd"
            @changeLint="getLintState1($event, 'stack_compose')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.label') + ':'" prop="labels">
          <el-tag
            v-for="tag in form.labels"
            :key="tag"
            class="mr-3"
            closable
            :disable-transitions="false"
            @close="handleClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="inputVisible"
            ref="saveTagInput"
            v-model="inputValue"
            class="w-28"
            size="small"
            @keyup.enter.native="handleInputConfirm"
            @blur="handleInputConfirm"
          />
          <el-button
            v-else
            class="button-new-tag"
            size="small"
            @click="showInput"
            >{{ $t("form.newTag") }}</el-button
          >
        </el-form-item>
        <el-form-item :label="$t('form.autoRun') + ':'" prop="auto_run">
          <el-select
            v-model="form.auto_run"
            :placeholder="$t('form.pleaseSelect')"
            clearable
          >
            <el-option :label="$t('form.yes')" :value="true" />
            <el-option :label="$t('form.no')" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('form.replicaNum') + ':'" prop="replica_num">
          <el-input-number
            v-model="form.replica_num"
            :min="1"
            :placeholder="$t('form.pleaseInputContent')"
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
              style="width: 100%"
              :placeholder="$t('form.pleaseSelect')"
              @change="onChangeRegion"
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
              clearable
              style="width: 100%"
              :placeholder="$t('form.pleaseSelect')"
              @change="onChangeSite"
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
              clearable
              style="width: 100%"
              :placeholder="$t('form.pleaseSelect')"
              @change="onChangeNode"
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
          <el-form-item v-if="cooperative" :label="$t('form.bp') + ':'">
            <el-select
              v-model="form.bp_id"
              :placeholder="$t('form.pleaseSelect')"
              filterable
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
          <el-form-item v-if="userStatus" :label="$t('form.user') + ':'">
            <el-select
              v-model="form.user_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              @change="userChange"
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
        <el-row type="flex">
          <el-form-item :label="$t('form.registry') + ':'" prop="registry_id">
            <el-select
              v-model="form.registry_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              @change="change_registry_id"
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
        </el-row>
        <el-form-item
          :label="$t('form.imagePullPolicy') + ':'"
          label-width="130px"
        >
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
        <el-collapse v-model="activeNames1">
          <el-collapse-item name="1">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.deviceDemand") }}
              </div>
            </template>

            <div class="Dev_need_info">
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.cpuDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.quantity") }}:</div>
                  <el-input-number
                    v-model="form.dev_need_info.cpu.cpu_num"
                    class="aaa"
                    :min="0"
                    :label="$t('form.quantity')"
                  />
                </el-form-item>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.model") }}:</div>
                  <el-input
                    v-model="form.dev_need_info.cpu.cpu_model"
                    type="text"
                    :placeholder="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.memoryDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.size") }}(MB):</div>
                  <el-input-number
                    v-model="form.dev_need_info.mem.mem_limit"
                    :min="0"
                    :label="$t('form.size')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.diskDemand") }}</h6>
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
                    :label="$t('form.size')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <h6>{{ $t("form.networkDemand") }}</h6>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.transmitBand") }}:</div>
                  <el-input-number
                    v-model="form.dev_need_info.network_band_need.transmit_band"
                    :min="0"
                    :label="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
                <el-form-item label-width="90px">
                  <div slot="label">{{ $t("form.receiveBand") }}:</div>
                  <el-input-number
                    v-model="form.dev_need_info.network_band_need.recv_band"
                    :min="0"
                    :label="$t('form.pleaseInputContent')"
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
                    :placeholder="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.graphicsCardModel") }}:</div>
                  <el-input
                    v-model="gpuForm.gpu_model"
                    type="text"
                    :placeholder="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.graphicsCardNumber") }}:</div>
                  <el-input-number
                    v-model="gpuForm.gpu_num"
                    :min="1"
                    :label="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.driverVersion") }}:</div>
                  <el-input
                    v-model="gpuForm.driver_version"
                    type="text"
                    :placeholder="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
              </div>
              <div class="Dev_need_info_item">
                <el-form-item>
                  <div slot="label">CUDA:</div>
                  <el-input
                    v-model="gpuForm.cuda_version"
                    type="text"
                    :placeholder="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">cuDNN:</div>
                  <el-input
                    v-model="gpuForm.cudnn_version"
                    type="text"
                    :placeholder="$t('form.pleaseInputContent')"
                  />
                </el-form-item>
                <el-form-item>
                  <div slot="label">{{ $t("form.driverCapability") }}:</div>
                  <el-input
                    v-model="gpuForm.driver_capabilities"
                    type="text"
                    :placeholder="$t('form.pleaseInputContent')"
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
            <div class="label">
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
                  <template slot="header" slot-scope="scope">
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
                  <template slot="header" slot-scope="scope">
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
  </div>
</template>

<script>
import { mapGetters } from "vuex";
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
} from "@/api/mainApi";
import YamlEditor from "@/components/yaml/editor.vue";
import yaml from "js-yaml";

export default {
  components: { YamlEditor },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: "",
    template: "",
  },
  data() {
    return {
      yamlLintState1: false,
      yamlLintState2: false,
      // 合作
      cooperative: true,
      // 用户
      userStatus: true,
      activeNames1: "",
      arealist: [],
      website: [],
      nodeList: [],
      bpsData: [],
      usersData: [],
      templatesData: [],
      registriesData: [],
      registries3rd: [],
      regions: [],
      sites: [],
      siteData: [],
      nodeData: [],
      isAdd: true,
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
        description: "",
        scheduling_strategy: {
          label_selector_map: {},
        },
        registry_id: "",
        aos_type: "",
        content_type: "yaml",
        stack_compose: "",
        justice_compose: "",
        labels: [],
        auto_run: true,
        replica_num: 1,
        bp_id: "",
        user_id: "",
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
        extra_params: { image_pull_policy: "IfNotPresent" },
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
        stack_compose: [
          {
            required: true,
            message: this.$t("form.pleaseInputStackCompose"),
            trigger: "blur",
          },
        ],
      },
      stackPlaceholder:
        "# version: '3'\n# services:\n#   hello:\n#     image: hello-world:latest\n#     labels:\n#       foo: bar",
    };
  },
  watch: {},
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  created() {},
  mounted() {
    if (this.userInfo.kind == 4) {
      this.cooperative = false;
      this.userStatus = false;
      this.form.user_id = this.userInfo.id;
      this.form.bp_id = this.userInfo.bp_id;
    } else if (this.userInfo.kind == 2) {
      this.cooperative = false;
      this.userStatus = true;
      this.form.bp_id = this.userInfo.bp_id;
      this.getUsers();
    } else {
      this.cooperative = true;
      this.userStatus = true;
      this.getUsers();
      this.bpsinit();
      this.form.user_id = "";
      this.form.bp_id = "";
    }
    this.areainit();
    this.resetForm();
  },
  methods: {
    getLintState1(state) {
      this.yamlLintState1 = state;
    },
    getLintState2(state) {
      this.yamlLintState2 = state;
    },
    async bpsinit() {
      const listBps = await getBps();
      this.bpsData = listBps.bps;
    },
    async getUsers() {
      const params = {};
      if (this.form.bp_id != "" && this.form.bp_id != undefined) {
        params.bp_id = this.form.bp_id;
        var listUsers = await getUsers(params);
      } else {
        var listUsers = await getUsers();
      }
      this.form.registry_id = "";
      this.usersData = listUsers.users;
    },
    onChangeRegion(val) {
      if (val) {
        this.form.target_nodes.dst_region_id = val;
        this.form.target_nodes.dst_site_id = "";
        this.form.target_nodes.dst_node_id = "";
        this.form.registry_id = "";
        this.nodeList = [];
        this.website = [];
        this.websiteinit();
        if (this.form.user_id != "" && this.form.user_id != undefined) {
          this.registries3rd_R();
          this.registries();
        } else {
          this.registries();
        }
      } else {
        this.form.target_nodes.dst_site_id = "";
        this.form.target_nodes.dst_node_id = "";
        this.form.registry_id = "";
        this.nodeList = [];
        this.website = [];
        this.registriesData = [];
        if (this.form.user_id) {
          this.registries3rd_R();
        }
      }
    },
    onChangeSite(val) {
      if (val) {
        this.form.target_nodes.dst_site_id = val;
        this.form.target_nodes.dst_node_id = "";
        this.edgesinit();
      } else {
        this.form.target_nodes.dst_node_id = "";
        this.nodeList = [];
      }
    },
    onChangeNode(val) {
      this.form.target_nodes.dst_node_id = val;
    },
    userChange(val) {
      this.form.user_id = val;
      this.form.registry_id = "";
      this.registriesData = [];
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
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
      if (this.arealist != null && this.arealist.length > 0) {
        this.form.target_nodes.dst_region_id = this.arealist[0].id;
        this.websiteinit();
        this.registries();
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
    async websiteinit() {
      const list = await getSites({
        region_id: this.form.target_nodes.dst_region_id,
      });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
      if (this.website != null && this.website.length > 0) {
        this.form.target_nodes.dst_site_id = this.website[0].id;
        this.edgesinit();
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
        if (this.nodeList != null) {
          if (this.nodeList.length > 0) {
            this.form.target_nodes.dst_node_id = this.nodeList[0].node_id;
          }
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
        this.$set(this.form, "description", this.info.description);
        this.$set(this.form, "vendor", this.info.vendor);
        this.$set(this.form, "aos_type", this.info.aos_type);
        this.$set(this.form, "content_type", this.info.content_type);
        this.$set(this.form, "stack_compose", this.info.stack_compose);
        this.$set(this.form, "justice_compose", this.info.justice_compose);
        if (this.info.dev_need_info.gpu) {
          this.gpuForm = {
            status: true,
            gpu_type: this.info.dev_need_info.gpu.gpu_type,
            gpu_model: this.info.dev_need_info.gpu.gpu_model,
            gpu_num: this.info.dev_need_info.gpu.gpu_num,
            driver_version: this.info.dev_need_info.gpu.driver_version,
            cuda_version: this.info.dev_need_info.gpu.cuda_version,
            cudnn_version: this.info.dev_need_info.gpu.cudnn_version,
            visible_devices: this.info.dev_need_info.gpu.visible_devices,
            driver_capabilities:
              this.info.dev_need_info.gpu.driver_capabilities,
          };
        } else {
          this.gpuForm = {
            status: false,
            gpu_type: "nvidia",
            gpu_model: "",
            gpu_num: 1,
            driver_version: "",
            cuda_version: "",
            cudnn_version: "",
            visible_devices: "",
            driver_capabilities: "",
          };
        }
        var dev_need_info = {
          cpu: {
            cpu_num: this.info.dev_need_info.cpu.cpu_num,
            cpu_model: this.info.dev_need_info.cpu.cpu_model,
          },
          mem: {
            mem_limit: this.info.dev_need_info.mem.mem_limit,
          },
          disk: {
            disk_type: this.info.dev_need_info.disk.disk_type,
            disk_limit: this.info.dev_need_info.disk.disk_limit,
          },
          network_band_need: {
            transmit_band: this.info.dev_need_info.network_band_need
              .transmit_band
              ? this.info.dev_need_info.network_band_need.transmit_band
              : 0,
            recv_band: this.info.dev_need_info.network_band_need.recv_band,
          },
        };
        var target_nodes = {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        };
        this.$set(this.form, "dev_need_info", dev_need_info);
      }

      const listTemplates = await templates();
      if (listTemplates.templates != null) {
        listTemplates.templates.forEach((item) => {
          if (item.versions.length > 0) {
            item.versions.forEach((item1) => {
              this.templatesData.push(item1);
            });
          }
        });
      }

      // this.templatesData = listTemplates.templates;
    },
    cancel() {
      this.resetForm();
    },
    doSubmit() {
      if (this.yamlLintState1) {
        this.$notify({
          title: $t("form.stackComposeFormatError"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      if (this.yamlLintState2) {
        this.$notify({
          title: $t("form.justiceComposeFormatError"),
          type: "error",
          duration: 2500,
        });
        return;
      }
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
      addDockerStacks(data)
        .then((res) => {
          this.dialog = false;
          this.$notify({
            title: $t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.$parent.applicationInit();
          this.resetForm();
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    doEdit() {
      editTemplates(this.form, this.form.id)
        .then((res) => {
          this.dialog = false;
          this.$notify({
            title: $t("message.modifySuccess"),
            type: "success",
            duration: 2500,
          });
          this.loading = false;
          this.$parent.init();
          this.resetForm();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.dialog = false;
      if (this.$refs["form"] !== undefined) {
        this.$refs["form"].resetFields();
      }
      this.form = {
        name: "",
        description: "",
        // input_params: "",
        aos_type: "",
        content_type: "yaml",
        stack_compose: "",
        justice_compose: "",
        labels: [],
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
        extra_params: { image_pull_policy: "IfNotPresent" },
      };
    },
    // 镜像仓库改变触发
    change_registry_id(val) {
      this.form.registry_id = val;
    },
    // 组织机构改变触发
    bpsChange(val) {
      this.form.bp_id = val;
      this.form.user_id = "";
      this.getUsers();
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
    handleClose(tag) {
      this.form.labels.splice(this.form.labels.indexOf(tag), 1);
    },
    handleInputConfirm() {
      const inputValue = this.inputValue;
      if (inputValue) {
        this.form.labels.push(inputValue);
      }
      this.inputVisible = false;
      this.inputValue = "";
    },
    showInput() {
      this.inputVisible = true;
      this.$nextTick((_) => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },
    clickLoad() {
      this.$refs.refFile.dispatchEvent(new MouseEvent("click"));
    },
    fileLoad() {
      var _this = this;
      const selectedFile = _this.$refs.refFile.files[0];
      if (selectedFile.size / 1024 / 1024 > _this.filesize) {
        _this.$notify({
          title: this.$t("message.uploadFileSizeExceed", {
            filesize: _this.filesize,
          }),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      _this.$refs.yamlEditor.placeholders = false;
      var reader = new FileReader();
      reader.readAsText(selectedFile);
      reader.onload = function () {
        _this.form.stack_compose = this.result;
      };
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
    // 镜像拉取策略
    changePullpolicy(value) {
      this.form.extra_params.image_pull_policy = value;
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

::v-deep .label {
  padding: 0 10px;
  box-sizing: border-box;

  .el-table td,
  .el-table th {
    padding: 5px 0 !important;
  }
}

::v-deep .el-table__empty-block {
  display: none;
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

.drbtn {
  outline: none;
  position: absolute;
  transform: translate(-120%, 100%);
}
</style>
