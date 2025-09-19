<template>
  <div class="warpMain">
    <div>
      <el-form
        :inline="true"
        size="small"
        :model="queryData"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.name") }}:</div>
          <el-input
            v-model="queryData.name"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.type") }}:</div>
          <el-select
            v-model="queryData.target_type"
            :placeholder="$t('form.pleaseSelect')"
            @change="handleCurrentChange(1)"
          >
            <el-option
              v-for="item in target_type_list"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.purpose") }}:</div>
          <el-select
            v-model="queryData.purpose"
            :placeholder="$t('form.pleaseSelect')"
            @change="handleCurrentChange(1)"
          >
            <el-option
              :label="$t('form.userConfiguration')"
              value="service_user"
            />
            <el-option
              :label="$t('form.resourceMonitoring')"
              value="inner_monitor"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            class="add_search"
            type="primary"
            @click="handleCurrentChange(1)"
            >{{ $t("form.query") }}
          </el-button>
        </el-form-item>
        <el-form-item>
          <el-button class="add_search" @click="searchinit()">
            {{ $t("form.reset") }}
          </el-button>
        </el-form-item>
        <el-button
          style="float: right; margin-bottom: 20px; display: inline-block"
          size="small"
          type="primary"
          @click="clickAddBtn"
          >{{ $t("form.create") }}</el-button
        >
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="name" :label="$t('form.idName')">
        <template slot-scope="scope">
          <router-link
            style="color: #409eff"
            :to="
              '/middleware/service/detail/' +
              scope.row.service_port_id +
              '/' +
              scope.row.target_type +
              '/' +
              scope.row.deployment
            "
            class="inline-block"
          >
            <span class="block leading-none">{{
              scope.row.name ? scope.row.name : "-"
            }}</span>
          </router-link>
          <div class="new-small-size">ID:{{ scope.row.service_port_id }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="target_type" :label="$t('form.type')">
        <template slot-scope="scope">
          {{
            target_type_list.find(
              (item) => item.value === scope.row.target_type
            )
              ? target_type_list.find(
                  (item) => item.value === scope.row.target_type
                ).label
              : ""
          }}
        </template>
      </el-table-column>
      <el-table-column prop="status" :label="$t('form.status')">
        <template slot-scope="scope">
          {{ statusList[scope.row.status.code] }}
        </template>
      </el-table-column>
      <el-table-column prop="target_services" :label="$t('form.ready')">
        <template slot-scope="scope">
          <span
            v-if="
              scope.row.target_services && scope.row.target_services.length > 0
            "
          >
            {{
              scope.row.target_services.filter((item) => item.status.code === 2)
                .length
            }}/{{ scope.row.target_services.length }}
          </span>
          <span v-else>0/0</span>
        </template>
      </el-table-column>
      <el-table-column prop="description" :label="$t('form.description')">
        <template slot-scope="scope">
          <span>{{ scope.row.description ? scope.row.description : "-" }}</span>
          <el-popover
            v-if="scope.row.purpose.split(',').length == 2"
            placement="top-start"
            width="200"
            trigger="hover"
            popper-class="tablepopover"
            :content="$t('form.purposeAndResourceAndUser')"
          >
            <span slot="reference"> <i class="el-icon-info" /></span>
          </el-popover>
          <el-popover
            v-if="
              scope.row.purpose.split(',').length == 1 &&
              scope.row.purpose == 'inner_monitor'
            "
            placement="top-start"
            width="200"
            trigger="hover"
            popper-class="tablepopover"
            :content="$t('form.purposeAndResource')"
          >
            <span slot="reference"> <i class="el-icon-info" /></span>
          </el-popover>
          <el-popover
            v-if="
              scope.row.purpose.split(',').length == 1 &&
              scope.row.purpose == 'service_user'
            "
            placement="top-start"
            width="200"
            trigger="hover"
            popper-class="tablepopover"
            :content="$t('form.purposeAndUser')"
          >
            <span slot="reference"> <i class="el-icon-info" /></span>
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column prop="create_time" :label="$t('form.createTime')">
        <template slot-scope="scope">
          {{ scope.row.create_time ? scope.row.create_time : "-" }}
        </template>
      </el-table-column>
      <el-table-column :label="$t('form.operation')" width="100">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div class="icon_cz" @click="openTest(scope.row)">
                <i class="el-icon-link" />
                {{ $t("form.connectivityTest") }}
              </div>
              <div class="icon_cz" @click="clickedit(scope.row)">
                <i class="el-icon-edit" />
                {{ $t("form.edit") }}
              </div>
              <div class="icon_cz" @click="clickdelete(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>

              <el-button
                slot="reference"
                icon="el-icon-more"
                class="czbtn right_czbtn"
              />
            </el-popover>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-4 px-4">
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
    <el-dialog
      :visible.sync="testDialogVisible"
      width="1002px"
      :close-on-click-modal="false"
      destroy-on-close
      :before-close="testHandleClose"
    >
      <!-- title -->
      <template slot="title">
        <span>
          {{ $t("form.connectivityTest")
          }}<small>({{ $t("form.onlyShowReadyPort") }})</small>
        </span>
      </template>
      <el-form
        v-if="testDataform && testDataform.length > 0"
        ref="form"
        size="small"
        label-position="right"
        style="display: flex; flex-wrap: wrap"
        label-width="0px"
      >
        <el-form-item label="" prop="data_source_id">
          <div class="portmapping_border">
            <div
              v-for="(item, index) in testDataform"
              :key="index"
              class="mb-4 overflow-hidden"
            >
              <div class="portmapping_content">
                <!-- 基本信息 start -->
                <div class="block">
                  <div class="portmapping_content_left">
                    <div class="mr-2">
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                        viewBox="0 0 32 32"
                        width="44"
                        height="44"
                      >
                        <defs data-reactroot="" />
                        <g>
                          <path
                            d="M9 3.333c3.13 0 5.667 2.537 5.667 5.667v0 5.667h-5.667c-3.13 0-5.667-2.537-5.667-5.667s2.537-5.667 5.667-5.667v0zM12 12v-3c0-1.657-1.343-3-3-3s-3 1.343-3 3c0 1.657 1.343 3 3 3v0h3zM9 17.333h5.667v5.667c0 3.13-2.537 5.667-5.667 5.667s-5.667-2.537-5.667-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM9 20c-1.657 0-3 1.343-3 3s1.343 3 3 3c1.657 0 3-1.343 3-3v0-3h-3zM23 3.333c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667v0h-5.667v-5.667c0-3.13 2.537-5.667 5.667-5.667v0zM23 12c1.657 0 3-1.343 3-3s-1.343-3-3-3c-1.657 0-3 1.343-3 3v0 3h3zM17.333 17.333h5.667c3.13 0 5.667 2.537 5.667 5.667s-2.537 5.667-5.667 5.667c-3.13 0-5.667-2.537-5.667-5.667v0-5.667zM20 20v3c0 1.657 1.343 3 3 3s3-1.343 3-3c0-1.657-1.343-3-3-3v0h-3z"
                          />
                        </g>
                      </svg>
                      <span class="block" style="width: 48px"
                        ><small>{{ $t("form.nodePort") }}</small></span
                      >
                    </div>
                    <div class="mr-2">
                      <el-form-item
                        class="mb-2"
                        label-width="100px"
                        :label="formLabel + ':'"
                      >
                        <el-popover
                          v-if="nowData.deployment_name > 12"
                          placement="top-start"
                          popper-class="tablepopover"
                          width="200"
                          trigger="hover"
                          :content="nowData"
                        >
                          <span
                            slot="reference"
                            class="ellipsis block w-32"
                            style="cursor: pointer"
                          >
                            {{
                              nowData
                                ? nowData.deployment_name
                                : $t("form.notSelected")
                            }}</span
                          >
                        </el-popover>
                        <span v-else class="ellipsis block w-32">
                          {{
                            nowData
                              ? nowData.deployment_name
                              : $t("form.notSelected")
                          }}</span
                        >
                      </el-form-item>
                      <el-form-item
                        v-if="
                          nowData.target_type == 'container' ||
                          nowData.target_type == 'compose'
                        "
                        class="mb-2"
                        label-width="100px"
                        :label="
                          nowData.target_type == 'container'
                            ? $t('form.container') + ':'
                            : $t('form.compose') + ':'
                        "
                        :rules="[
                          {
                            required: true,
                            message: $t('form.pleaseEnterPort'),
                            trigger: 'blur',
                          },
                        ]"
                        :prop="'target_services.' + index + '.service_name'"
                      >
                        <el-popover
                          v-if="testDataform[index].service_name.length > 12"
                          placement="top-start"
                          :title="
                            nowData.target_type == 'container'
                              ? $t('form.container')
                              : $t('form.compose')
                          "
                          popper-class="tablepopover"
                          width="200"
                          trigger="hover"
                          :content="testDataform[index].service_name"
                        >
                          <span
                            slot="reference"
                            class="block w-32 ellipsis"
                            style="cursor: pointer"
                            >{{ testDataform[index].service_name }}</span
                          >
                        </el-popover>
                        <span v-else class="block w-32 ellipsis">{{
                          testDataform[index].service_name
                        }}</span>
                        <!-- <span class="block w-32">{{ testDataform[index].service_name }}</span> -->
                      </el-form-item>
                      <el-form-item
                        class="mb-2"
                        label-width="100px"
                        :label="$t('form.port') + ':'"
                        :rules="[
                          {
                            required: true,
                            message: $t('form.pleaseEnterPort'),
                            trigger: 'blur',
                          },
                        ]"
                        :prop="'target_services.' + index + '.target_port'"
                      >
                        <span class="block w-32">{{
                          testDataform[index].target_port
                        }}</span>
                      </el-form-item>
                    </div>
                    <div class="ml-2 mr-5">
                      <el-form-item
                        label-width="50px"
                        class="mb-0"
                        :label="$t('form.protocol') + ':'"
                        :rules="[
                          {
                            required: true,
                            message: $t('form.pleaseSelectProtocol'),
                            trigger: 'blur',
                          },
                        ]"
                        :prop="'target_services.' + index + '.protocol'"
                      >
                        <span class="block w-32">{{
                          testDataform[index].protocol
                        }}</span>
                      </el-form-item>
                      <el-form-item class="mb-0">
                        <div class="arrow" />
                      </el-form-item>
                      <el-form-item
                        v-if="testDataform[index].protocol == 'HTTPS'"
                        label-width="50px"
                        :label="$t('form.certificate') + ':'"
                        :prop="'target_services.' + index + '.cert'"
                        :rules="[
                          {
                            required: true,
                            message: $t('form.pleaseEnterCertificateContent'),
                            trigger: 'blur',
                          },
                        ]"
                      >
                        <el-popover
                          :ref="`node-popover-${testDataform[index].cert}`"
                          placement="top"
                          :title="$t('form.certificate')"
                          width="600"
                          popper-class="tablepopover"
                          trigger="hover"
                          :content="testDataform[index].cert"
                        >
                          <span
                            v-show="testDataform[index].cert"
                            slot="reference"
                            class="w-32"
                            style="cursor: pointer"
                            >{{
                              testDataform[index].cert
                                ? testDataform[index].cert.slice(0, 12) + "..."
                                : ""
                            }}</span
                          >
                        </el-popover>
                        <!-- <span class="block w-32">{{ testDataform[index].cert }}</span> -->
                      </el-form-item>
                      <el-form-item v-if="item.status" class="mb-0">
                        <div class="text-center">
                          <span
                            v-if="item.status.code == 0"
                            class="text-blue-500"
                            >{{ item.status.desc.cn }}</span
                          >
                          <span
                            v-if="item.status.code == 1"
                            class="text-yellow-500"
                            >{{ item.status.desc.cn }}</span
                          >
                          <span
                            v-if="item.status.code == 2"
                            class="text-green-500"
                            >{{ item.status.desc.cn }}</span
                          >
                          <span
                            v-if="item.status.code == 50"
                            class="text-red-500"
                            >{{ item.status.desc.cn }}</span
                          >
                          <span
                            v-if="item.status.code == 51"
                            class="text-red-500"
                            >{{ item.status.desc.cn }}</span
                          >
                          <span
                            v-if="item.status.code == 52"
                            class="text-red-500"
                            >{{ item.status.desc.cn }}</span
                          >
                          <span
                            v-if="item.status.code == 90"
                            class="text-red-300"
                            >{{ item.status.desc.cn }}</span
                          >
                        </div>
                      </el-form-item>
                    </div>
                    <div class="ml-5 mr-2">
                      <svg
                        xmlns="http://www.w3.org/2000/svg"
                        class="styles__StyledSVGIconPathComponent-sc-gbp7ch-0 gFGDaY svg-icon-path-icon fill"
                        viewBox="0 0 48 48"
                        width="44"
                        height="44"
                      >
                        <defs data-reactroot="" />
                        <g>
                          <rect
                            width="48"
                            height="48"
                            fill="white"
                            fill-opacity="0.01"
                          />
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M8 12C10.2091 12 12 10.2091 12 8C12 5.79086 10.2091 4 8 4C5.79086 4 4 5.79086 4 8C4 10.2091 5.79086 12 8 12Z"
                            fill="none"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M10 42C13.3137 42 16 39.3137 16 36C16 32.6863 13.3137 30 10 30C6.68629 30 4 32.6863 4 36C4 39.3137 6.68629 42 10 42Z"
                            fill="none"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M38 44C41.3137 44 44 41.3137 44 38C44 34.6863 41.3137 32 38 32C34.6863 32 32 34.6863 32 38C32 41.3137 34.6863 44 38 44Z"
                            fill="none"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M22 28C26.4183 28 30 24.4183 30 20C30 15.5817 26.4183 12 22 12C17.5817 12 14 15.5817 14 20C14 24.4183 17.5817 28 22 28Z"
                            fill="none"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            fill-rule="evenodd"
                            clip-rule="evenodd"
                            d="M34 12C36.2091 12 38 10.2091 38 8C38 5.79086 36.2091 4 34 4C31.7909 4 30 5.79086 30 8C30 10.2091 31.7909 12 34 12Z"
                            fill="none"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            d="M11 11L15 15"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            d="M30 12L28 14"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            d="M34 33.5L28 26"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                          <path
                            d="M14 31L18 27"
                            stroke="#333"
                            stroke-width="4"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                          />
                        </g>
                      </svg>
                      <span class="block" style="width: 48px"
                        ><small>{{ $t("form.servicePort") }}</small></span
                      >
                    </div>
                    <div class="ml-2 mr-2 w-56">
                      <el-form-item
                        class="mb-2"
                        label-width="45px"
                        :label="$t('form.gateway') + ':'"
                      >
                        <span class="block w-72 leading-4 mt-2">
                          <span class="block">
                            {{
                              item.svc_node_name ||
                              item.old_svc_node ||
                              item.target_svc_node_name ||
                              item.target_svc_node ||
                              " "
                            }}</span
                          >
                          <small v-if="item.target_svc_node">
                            {{ $t("form.userSpecified") }}
                          </small>
                          <small v-else>
                            {{ $t("form.systemAssigned") }}
                          </small>
                        </span>
                      </el-form-item>
                      <el-form-item
                        class="mb-0"
                        label-width="45px"
                        :label="$t('form.port') + ':'"
                      >
                        <span class="block w-72 leading-4 mt-2">
                          <span class="block"> {{ item.svc_port }}</span>
                          <small v-if="item.target_svc_port">
                            {{ $t("form.userSpecified") }}
                          </small>
                          <small v-else>
                            {{ $t("form.systemAssigned") }}
                          </small>
                        </span>
                      </el-form-item>
                      <span v-if="item.svc_ip"
                        >{{ $t("form.accessibleIP") }}：{{ item.svc_ip }}</span
                      >
                    </div>
                    <div class="ml-2 mr-2 leading-4">
                      <span v-if="item.network_status != undefined">
                        <span
                          v-if="item.network_status == 998"
                          class="text-green-500"
                          >{{ $t("form.detecting") }}</span
                        >
                        <span
                          v-if="item.network_status == 0"
                          class="text-green-500"
                          >{{ $t("form.networkNormal") }}</span
                        >
                        <span
                          v-if="item.network_status == 1"
                          class="text-yellow-500"
                          >{{ $t("form.serviceGatewayUnreachable") }}</span
                        >
                        <span
                          v-if="item.network_status == 2"
                          class="text-yellow-500"
                          >{{ $t("form.serviceAgentUnreachable") }}</span
                        >
                        <span
                          v-if="item.network_status == 3"
                          class="text-yellow-500"
                          >{{ $t("form.targetServiceUnreachable") }}</span
                        >
                      </span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 基本信息 end -->
        </el-form-item>
      </el-form>
      <el-empty
        v-else
        :description="$t('form.noReadyContent')"
        :image-size="80"
      />
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="testHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          size="small"
          :disabled="!(testDataform && testDataform.length > 0)"
          @click="testStart()"
          >{{ $t("form.startDetection") }}</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import {
  getServicePorts,
  delServicePorts,
  testTargetService,
} from "@/api/mainApi";
import initData from "@/mixins/initData";

export default {
  mixins: [initData],
  data() {
    return {
      sup_this: this,
      target_type_list: [
        {
          value: "edge",
          label: this.$t("form.node"),
        },
        {
          value: "container",
          label: this.$t("form.container"),
        },
        {
          value: "compose",
          label: this.$t("form.compose"),
        },
        // {
        //   value: "k8s",
        //   label: "K8s集群",
        // },
        // {
        //   value: "cloud",
        //   label: "多云",
        // },
        // {
        //   value: "other",
        //   label: "其他",
        // },
      ],
      statusList: {
        0: this.$t("form.creating"), // Creating
        1: this.$t("form.updating"), // Updating
        2: this.$t("form.ready"), // Ready
        3: this.$t("statusAndType.partialReady"), // Partial Ready
        50: this.$t("form.failed"), // Filed
        90: this.$t("statusAndType.deleting"), // Removing
        99: this.$t("statusAndType.deleted"), // Removed
      },
      tableData: [],
      queryData: {
        name: "",
        purpose: "service_user",
        target_type: "",
        page_size: 10,
        page_num: 1,
        totalNum: 0,
      },
      // 连通性探测
      testDialogVisible: false,
      testDataform: [],
      nowData: {},
      testBtnLoading: false,
      formLabel: "",
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "timeInterval"]),
  },
  components: {},
  async created() {
    this.init();
  },
  mounted() {},

  methods: {
    searchinit() {
      this.queryData = {
        name: "",
        purpose: "service_user",
        target_type: "",
        page_size: 10,
        page_num: 1,
        totalNum: 0,
      };
      this.init();
    },

    handleSizeChange(val) {
      this.queryData.page_size = val;
      this.init();
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      this.init();
    },
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === "") {
          delete this.queryData[key];
        }
      }
      const list = await getServicePorts(this.queryData);
      this.tableData = list.service_ports;
      this.queryData.totalNum = list.total_num;
    },
    clickAddBtn() {
      this.$router.push("/middleware/service/add");
    },
    clickdelete(value) {
      this.$confirm(
        this.$t("message.confirmDeleteData"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          delServicePorts(value.service_port_id)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startDelete"),
                type: "success",
                duration: 2500,
              });
              this.init();
            })
            .catch((err) => {});
        })
        .catch(() => {});
    },
    clickedit(value) {
      this.$router.push(
        "/middleware/service/detail/" +
          value.service_port_id +
          "/" +
          value.target_type +
          "/" +
          value.deployment
      );
    },
    openTest(row) {
      // 连通性探测
      this.nowData = row;
      this.testDataform = JSON.parse(
        JSON.stringify(row)
      ).target_services.filter((item) => item.status.code === 2);
      this.testDialogVisible = true;
      this.nowData.target_type == "container"
        ? (this.formLabel = this.$t("form.containerDeployment"))
        : "";
      this.nowData.target_type == "compose"
        ? (this.formLabel = this.$t("form.composeDeployment"))
        : "";
      this.nowData.target_type == "edge"
        ? (this.formLabel = this.$t("form.node"))
        : "";
    },
    testHandleClose() {
      // 连通性探测
      this.testDialogVisible = false;
    },
    testStart() {
      // 连通性探测
      this.testBtnLoading = true;
      setTimeout(() => {
        this.testBtnLoading = false;
      }, 3000);
      this.testDataform.forEach((element, index, arr) => {
        this.testDataform[index].network_status = 998;
        this.$forceUpdate();

        testTargetService(this.nowData.service_port_id, element.id)
          .then((res) => {
            this.testDataform[index].network_status = res[0].network_status;
            this.$forceUpdate();
          })
          .catch((err) => {});
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.ellipsis {
  max-width: 125px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.portmapping_border {
  border: 1px solid rgb(187, 187, 187);
  padding: 15px;
  border-radius: 10px;
  display: inline-block;

  .portmapping_content {
    width: 932px;
    float: left;
    border: 1px solid rgb(187, 187, 187);
    padding: 10px 15px;
    border-radius: 10px;
    justify-content: space-between;
    align-items: center;

    &:last-child {
      margin-bottom: 0 !important;
    }

    .portmapping_content_left {
      display: flex;
      align-items: center;

      .arrow {
        position: relative;
        margin-right: 10px;
        position: relative;
        width: 170px;
        height: 4px;
        /* 控制箭头高度 */
        background: rgb(108, 108, 108);

        &::before {
          content: "";
          display: block;
          position: absolute;
          right: 0;
          top: 50%;
          width: 0;
          height: 0;
          border-top: 10px solid transparent;
          /* 控制箭头高度 */
          border-bottom: 10px solid transparent;
          /* 控制箭头高度 */
          border-right: 10px solid rgb(108, 108, 108);
          /* 控制箭头宽度和颜色 */
          transform: translate(10px, -50%) rotate(180deg);
          /* 控制箭头朝向和位置 */
        }
      }

      .portmapping_content_delete {
        cursor: pointer;
        font-size: 18px;
      }
    }
  }
}
</style>
