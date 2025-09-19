<template>
  <div class="gatewayForm">
    <el-dialog
      :append-to-body="true"
      :visible.sync="dialog"
      :title="isAdd ? $t('form.addCloud') : $t('form.editCloud')"
      width="1050px"
      :destroy-on-close="true"
      :close-on-click-modal="false"
      :before-close="cancel"
      @open="onOpen"
    >
      <el-form
        ref="form"
        :model="form"
        :rules="rules"
        size="small"
        label-width="130px"
      >
        <el-form-item
          v-if="isAdd"
          :label="$t('form.cloudId') + ':'"
          prop="cloud_id"
          :rules="[
            {
              required: false,
              validator: validateCloudId,
              trigger: 'change',
            },
            {
              required: false,
              validator: validateCloudId,
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.cloud_id"
            :placeholder="$t('form.pleaseInputCloudId')"
          />
        </el-form-item>
        <el-form-item
          :label="$t('form.name') + ':'"
          prop="name"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputName'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.name"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>

        <el-row type="flex">
          <el-form-item
            :label="$t('form.region') + ':'"
            prop="target_nodes.dst_region_id"
            :rules="[{ required: true, message: $t('form.pleaseSelect') }]"
          >
            <el-select
              v-model="form.target_nodes.dst_region_id"
              filterable
              style="width: 230px"
              :placeholder="$t('form.pleaseSelect')"
              :disabled="!showBtn(form, [3, 21, 50, 51])"
              @change="handleRegionChange"
            >
              <el-option
                v-for="(item, index) in arealist"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label-width="94px">
            <div slot="label">{{ $t("form.site") + ":" }}</div>
            <el-select
              v-model="form.target_nodes.dst_site_id"
              filterable
              style="width: 230px"
              :placeholder="$t('form.pleaseSelect')"
              clearable
              :disabled="!showBtn(form, [3, 21, 50, 51])"
              @change="handleSiteChange"
            >
              <el-option
                v-for="(item, index) in website"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label-width="94px">
            <div slot="label">{{ $t("form.node") + ":" }}</div>
            <el-select
              v-model="form.target_nodes.dst_node_id"
              filterable
              style="width: 230px"
              :placeholder="$t('form.pleaseSelect')"
              clearable
              :disabled="!showBtn(form, [3, 21, 50, 51])"
              @change="handleNodeChange"
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
        <el-form-item
          :label="$t('form.url') + ':'"
          prop="url"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInput'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.url"
            :placeholder="$t('form.pleaseInput')"
            :disabled="!showBtn(form, [3, 21, 50, 51])"
          >
            <el-select
              slot="prepend"
              v-model="form.urlHead"
              :placeholder="$t('form.pleaseSelect')"
              style="width: 90px"
              :disabled="!showBtn(form, [3, 21, 50, 51])"
            >
              <el-option label="http://" value="http://" />
              <el-option label="https://" value="https://" />
            </el-select>
          </el-input>
        </el-form-item>
        <el-form-item
          v-if="form.urlHead == 'https://'"
          :label="$t('form.certificate') + ':'"
        >
          <el-button
            v-if="showBtn(form, [3, 21, 50, 51])"
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
            ref="yamlEditor"
            v-model="form.certificate"
            style="
              line-height: 1.2;
              max-height: 350px;
              overflow: auto;
              border-radius: 4px;
              margin-top: 10px;
              min-width: 100%;
            "
            :download-name="'certificate'"
            :download-type="'yml'"
            :lint="true"
            :is-add="isAdd"
            :placeholder="''"
          />
        </el-form-item>

        <el-form-item
          :label="$t('form.healthCheckUrl') + ':'"
          prop="health_check.health_url"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseInputHealthCheckUrl'),
              trigger: 'blur',
            },
          ]"
        >
          <el-input
            v-model="form.health_check.health_url"
            :placeholder="$t('form.pleaseInputHealthCheckUrl')"
            :disabled="!showBtn(form, [3, 21, 50, 51])"
          />
        </el-form-item>
        <el-form-item
          label-width="130px"
          prop="product"
          :label="$t('form.type') + ':'"
          :rules="[
            {
              required: true,
              message: $t('form.pleaseSelectType'),
              trigger: 'change',
            },
          ]"
        >
          <el-select
            v-model="form.product"
            filterable
            style="width: 230px"
            :placeholder="$t('form.pleaseSelect')"
            :disabled="!showBtn(form, [3, 21, 50, 51])"
            @change="handleProduceChange"
          >
            <el-option label="NextStack" value="NextStack" />
            <el-option label="EasyStack" value="EasyStack" />
          </el-select>
        </el-form-item>
        <el-row v-if="form.product == 'NextStack'" type="flex">
          <el-form-item
            :label="$t('form.accessKeyId') + ':'"
            prop="authorization.access_key.id"
            :rules="[
              {
                required: true,
                message: $t('form.pleaseInputAccessKeyId'),
                trigger: 'blur',
              },
            ]"
          >
            <el-input
              v-model="form.authorization.access_key.id"
              style="width: 230px"
              :placeholder="$t('form.pleaseInputAccessKeyId')"
              :disabled="!showBtn(form, [3, 21, 50, 51])"
            />
          </el-form-item>
          <el-form-item
            label-width="180px"
            :label="$t('form.accessKeySecret') + ':'"
            :prop="isAdd ? 'authorization.access_key.secret' : ''"
            :rules="[
              {
                required: isAdd ? true : false,
                message: $t('form.pleaseInputAccessKeySecret'),
                trigger: 'blur',
              },
            ]"
          >
            <el-input
              v-model="form.authorization.access_key.secret"
              style="width: 230px"
              :placeholder="$t('form.pleaseInputAccessKeySecret')"
              :disabled="!showBtn(form, [3, 21, 50, 51])"
            />
            <span v-if="!isAdd" class="ml-2 text-gray-500 text-xs">
              <i class="el-icon-info" />
              {{ $t("form.emptyIsNotModify") }}
            </span>
          </el-form-item>
        </el-row>
        <el-row v-if="form.product == 'EasyStack'" type="flex">
          <el-form-item
            :label="$t('form.userId') + ':'"
            prop="authorization.password.userid"
            :rules="[
              {
                required: true,
                message: $t('form.pleaseInputUserId'),
                trigger: 'blur',
              },
            ]"
          >
            <el-input
              v-model="form.authorization.password.userid"
              style="width: 230px"
              :placeholder="$t('form.pleaseInputUserId')"
            />
          </el-form-item>
          <el-form-item
            label-width="180px"
            :label="$t('form.userPassword') + ':'"
            :prop="isAdd ? 'authorization.password.password' : ''"
            :rules="[
              {
                required: isAdd ? true : false,
                message: $t('form.pleaseInputUserPassword'),
                trigger: 'blur',
              },
            ]"
          >
            <el-input
              v-model="form.authorization.password.password"
              style="width: 230px"
              show-password
              :placeholder="$t('form.pleaseInputUserPassword')"
            />
            <span v-if="!isAdd" class="ml-2 text-gray-500 text-xs">
              <i class="el-icon-info" />
              {{ $t("form.emptyIsNotModify") }}
            </span>
          </el-form-item>
        </el-row>

        <el-form-item :label="$t('form.label')" prop="labels">
          <div v-if="form.labels && form.labels.length > 0" class="mb-2">
            <span v-for="(item, index) in form.labels" :key="index">
              <el-popover
                ref="labelPopover"
                placement="top-start"
                trigger="hover"
              >
                <div class="p-2">
                  <div>
                    <span>{{ $t("form.labelContent") }}：</span>
                    <span>{{ JSON.parse(item).label }}</span>
                  </div>
                  <div>
                    <span>{{ $t("form.textColor") }}：</span>
                    <span>{{ JSON.parse(item).textColor }}</span>
                  </div>
                  <div>
                    <span>{{ $t("form.bgColor") }}：</span>
                    <span>{{ JSON.parse(item).bgColor }}</span>
                  </div>
                  <el-button
                    size="mini"
                    @click="editLabel(JSON.parse(item), index)"
                    >{{ $t("form.edit") }}</el-button
                  >
                  <el-button size="mini" @click="delLabel(index)">{{
                    $t("form.delete")
                  }}</el-button>
                  <el-button size="mini" @click="labelIndex(index, 1)">{{
                    $t("form.forward")
                  }}</el-button>
                  <el-button size="mini" @click="labelIndex(index, -1)">{{
                    $t("form.backward")
                  }}</el-button>
                </div>

                <span
                  slot="reference"
                  class="relative inline-block px-2 text-md leading-5 rounded h-5 mr-2 mb-2 align-bottom"
                  :style="
                    'color:' +
                    JSON.parse(item).textColor +
                    ';background-color:' +
                    JSON.parse(item).bgColor
                  "
                  >{{ JSON.parse(item).label }}
                  <span
                    v-if="JSON.parse(item).imgUrl"
                    class="inline-block h5"
                    :style="'width:' + (JSON.parse(item).imgSize - 8) + 'px'"
                  />
                  <img
                    v-if="JSON.parse(item).imgUrl"
                    :src="JSON.parse(item).imgUrl"
                    alt=""
                    class="absolute"
                    :class="getLabelImgPOsition(JSON.parse(item).imgPosition)"
                    :style="
                      'max-width:none;width:' + JSON.parse(item).imgSize + 'px'
                    "
                  />
                </span>
              </el-popover>
            </span>
          </div>
          <el-button
            v-if="!addLabel"
            type="primary"
            size="small"
            @click="openAddLabel()"
            >{{ $t("form.add") }}</el-button
          >
          <div v-else class="border p-4">
            <div class="mb-2">
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.content") }}：</span>
                <el-input
                  v-model="addLabelForm.label"
                  class="w-32"
                  size="small"
                  :placeholder="$t('form.pleaseInputContent')"
                />
              </span>
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.textColor") }}：</span>
                <el-color-picker
                  v-model="addLabelForm.textColor"
                  size="small"
                />
              </span>
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.bgColor") }}：</span>
                <el-color-picker v-model="addLabelForm.bgColor" size="small" />
              </span>
              <span class="inline-flex mx-4">
                <el-checkbox v-model="addLabelForm.isImg">{{
                  $t("form.isNeedBadge")
                }}</el-checkbox>
              </span>
            </div>
            <div v-if="addLabelForm.isImg" class="mb-2">
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.badge") }}：</span>
                <el-input
                  v-model="addLabelForm.imgUrl"
                  class="w-72"
                  size="small"
                  :placeholder="$t('form.pleaseInputBadgeUrl')"
                />
              </span>
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.size") }}：</span>
                <el-input-number
                  v-model="addLabelForm.imgSize"
                  :step="1"
                  :min="1"
                  :max="100"
                  controls-position="right"
                  class="w-24"
                  size="small"
                />
              </span>
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.position") }}：</span>
                <el-select
                  v-model="addLabelForm.imgPosition"
                  :placeholder="$t('form.pleaseSelect')"
                  class="w-32"
                >
                  <!-- <el-option value="left" label="居左">居左</el-option> -->
                  <el-option value="right" :label="$t('form.right')">{{
                    $t("form.right")
                  }}</el-option>
                  <el-option value="center" :label="$t('form.center')">{{
                    $t("form.center")
                  }}</el-option>
                  <!-- <el-option value="top-left" label="左上">左上</el-option> -->
                  <el-option value="top-right" :label="$t('form.topRight')">{{
                    $t("form.topRight")
                  }}</el-option>
                  <!-- <el-option value="bottom-left" label="左下">左下</el-option> -->
                  <el-option
                    value="bottom-right"
                    :label="$t('form.bottomRight')"
                    >{{ $t("form.bottomRight") }}</el-option
                  >
                </el-select>
              </span>
            </div>
            <div class="mb-2">
              <span class="inline-flex mx-4">
                <span class="float-left">{{ $t("form.preview") }}：</span>
                <span
                  class="relative inline-block px-2 mt-1.5 text-md leading-5 rounded h-5"
                  :class="addLabelForm.bgColor ? '' : 'masaike'"
                  :style="
                    'color:' +
                    addLabelForm.textColor +
                    ';background-color:' +
                    (addLabelForm.bgColor
                      ? addLabelForm.bgColor
                      : 'transparent')
                  "
                  >{{ addLabelForm.label }}
                  <span
                    v-if="addLabelForm.imgUrl"
                    class="inline-block h5"
                    :style="'width:' + (addLabelForm.imgSize - 8) + 'px'"
                  />
                  <img
                    v-if="addLabelForm.imgUrl"
                    :src="addLabelForm.imgUrl"
                    alt=""
                    class="absolute"
                    :class="getLabelImgPOsition(addLabelForm.imgPosition)"
                    :style="
                      'max-width:none;width:' + addLabelForm.imgSize + 'px'
                    "
                  />
                </span>
              </span>
            </div>
            <el-button size="small" @click="cancelLabel()">{{
              $t("form.cancel")
            }}</el-button>
            <el-button type="primary" size="small" @click="toAddLabel()">{{
              $t("form.confirm")
            }}</el-button>
          </div>
        </el-form-item>

        <el-collapse v-if="form.product == 'EasyStack'" v-model="tabsAndNotes">
          <el-collapse-item :title="$t('form.advancedOptions')" name="1">
            <div style="margin-left: 77px">
              <h5 class="ml-0 mb-2" style="font-weight: 600">
                {{ $t("form.serviceList") }}
                <span style="float: right; margin-right: 2px"
                  ><el-switch v-model="switchValue"
                /></span>
              </h5>
              <el-table
                v-if="switchValue"
                :data="tableData"
                style="width: 100%"
                stripe
                size="small"
                class="mb-3"
              >
                <el-table-column
                  prop="name"
                  :label="$t('form.serviceName')"
                  width="180"
                />
                <el-table-column
                  prop="type"
                  :label="$t('form.type')"
                  width="180"
                />
                <el-table-column prop="url" :label="$t('form.url')">
                  <template slot-scope="scope">
                    <el-input
                      v-model="scope.row.url"
                      :placeholder="scope.row.placeholder"
                      size="small"
                      @change="handleChange(scope.row)"
                    />
                  </template>
                </el-table-column>
              </el-table>
              <el-form-item
                :label="$t('form.projectId') + ':'"
                label-width="54px"
              >
                <el-input
                  v-model="form.os_service_endpoints.project_id"
                  :placeholder="$t('form.pleaseInputProjectId')"
                  style="width: 230px"
                  clearable
                />
              </el-form-item>
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
import {
  getSites,
  getRegions,
  updateCloudys,
  addCloudys,
  getBps,
  getUsers,
  edges,
} from "@/api/mainApi";
import { mapGetters } from "vuex";
import YamlEditor from "@/components/yaml/editor.vue";
export default {
  components: { YamlEditor },
  props: {
    sup_this: {
      type: Object,
      default: null,
    },
    info: Object,
  },
  data() {
    const validateCloudId = (rule, value, callback) => {
      if (value === "") {
        callback();
      } else {
        //  大小写字母，数字，下划线，中划线，点，1-48位，不能以中划线和点开头和结尾
        const reg = /^[a-zA-Z0-9_][a-zA-Z0-9._-]{0,46}[a-zA-Z0-9_]$/;
        if (reg.test(value)) {
          callback();
        } else {
          callback(new Error(this.$t("message.pleaseInputCloudId")));
        }
      }
    };
    return {
      validateCloudId: validateCloudId,
      isAdd: false,
      loading: false,
      dialog: false,
      cooperative: false,
      userStatus: false,
      yamlLintState: false,
      switchValue: false,
      addLabel: false,
      editLabelIndex: "",
      addLabelForm: {
        label: "",
        textColor: "#fff",
        bgColor: "#004579",
        imgUrl: "",
        imgSize: 10,
        imgPosition: "top-right",
        isImg: false,
      },
      form: {
        cloud_id: "",
        name: "",
        urlHead: "http://",
        description: "",
        product: "NextStack",
        mode: "proxy",
        certificate: "",
        target_nodes: {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        },
        bp_id: "",
        user_id: "",
        url: "",
        health_check: {
          health_url: "",
          response: "",
          interval: 0,
        },
        authorization: {
          access_key: {
            id: "",
            secret: "",
          },
          password: {
            userid: "",
            password: "",
          },
        },
        os_service_endpoints: {
          endpoints: {
            keystone: {
              type: "identity",
              name: "keystone",
              url: "",
              port: ":5000",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:5000  /  http://192.168.0.1/identity",
            },
            vnc: {
              type: "vnc",
              name: "vnc",
              url: "",
              port: ":6080",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:6080",
            },
            glance: {
              type: "image",
              name: "glance",
              url: "",
              port: ":9292",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:9292 / http://192.168.0.1/image",
            },
            cinderv2: {
              type: "volumev2",
              name: "cinderv2",
              url: "",
              port: ":8776",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8776 / http://192.168.0.1/volume",
            },
            cinderv3: {
              type: "volumev3",
              name: "cinderv3",
              url: "",
              port: ":8776",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8776 / http://192.168.0.1/volume",
            },
            nova: {
              type: "compute",
              name: "nova",
              url: "",
              port: ":8774",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8774 / http://192.168.0.1/compute",
            },
            neutron: {
              type: "network",
              name: "neutron",
              url: "",
              port: ":9696",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:9696 / http://192.168.0.1/networking",
            },
            aodh: {
              type: "alarming",
              name: "aodh",
              url: "",
              port: ":8042",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8042",
            },
            ceilometer: {
              type: "metering",
              name: "ceilometer",
              url: "",
              port: ":8777",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8777",
            },
            placement: {
              type: "placement",
              name: "placement",
              url: "",
              port: ":8778",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8778 / http://192.168.0.1/placement",
            },
            swift: {
              type: "object-store",
              name: "swift",
              url: "",
              port: ":8080",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8080",
            },
            gnocchi: {
              type: "metric",
              name: "gnocchi",
              url: "",
              port: ":8041",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8041",
            },
          },
          project_id: "",
        },
        labels: [],
      },
      tabsAndNotes: [""],
      rules: {},
      arealist: [],
      website: [],
      nodeList: [],
      BpsData: [],
      userData: [],
      tableData: [],
      entries: [],
      placeholders: [
        this.$t("domain.example") +
          "http://192.168.0.1:5000 / http://192.168.0.1/identity",
        this.$t("domain.example") + "http://192.168.0.1:6080",
        this.$t("domain.example") +
          "http://192.168.0.1:9292 / http://192.168.0.1/image",
        this.$t("domain.example") +
          "http://192.168.0.1:8776 / http://192.168.0.1/volume",
        this.$t("domain.example") +
          "http://192.168.0.1:8776 / http://192.168.0.1/volume",
        this.$t("domain.example") +
          "http://192.168.0.1:8774 / http://192.168.0.1/compute",
        this.$t("domain.example") +
          "http://192.168.0.1:9696 / http://192.168.0.1/networking",
        this.$t("domain.example") + "http://192.168.0.1:8042",
        this.$t("domain.example") + "http://192.168.0.1:8777",
        this.$t("domain.example") +
          "http://192.168.0.1:8778 / http://192.168.0.1/placement",
        this.$t("domain.example") + "http://192.168.0.1:8080",
        this.$t("domain.example") + "http://192.168.0.1:8041",
      ],
    };
  },
  computed: {
    ...mapGetters(["kind", "userInfo", "filesize"]),
  },
  created() {
    const that = this;
    if (that.userInfo.kind == 4) {
      that.cooperative = that.userStatus = false;
      that.form.user_id = that.userInfo.id;
      that.form.bp_id = that.userInfo.bp_id;
    } else if (that.userInfo.kind == 2) {
      that.cooperative = false;
      that.userStatus = true;
      that.form.bp_id = that.userInfo.bp_id;
    } else {
      that.cooperative = true;
      that.userStatus = true;
      that.form.user_id = "";
      that.form.bp_id = "";
    }
  },
  mounted() {},
  methods: {
    showBtn(item, data) {
      // data是否包含 status.code
      if (this.isAdd) {
        return true;
      }
      if (item.status && data.includes(item.status.code)) {
        return true;
      } else {
        return false;
      }
    },
    getLabelImgPOsition(data) {
      if (data == "top-left") {
        return "top-0 left-0";
      } else if (data == "top-right") {
        return "top-0 right-0";
      } else if (data == "bottom-left") {
        return "bottom-0 left-0";
      } else if (data == "bottom-right") {
        return "bottom-0 right-0";
      } else if (data == "left") {
        return "top-1/2 left-0 transform -translate-y-1/2";
      } else if (data == "right") {
        return "top-1/2 right-0 transform -translate-y-1/2";
      } else if (data == "center") {
        return "top-1/2 right-0 left-0 mx-auto transform -translate-y-1/2";
      } else {
        return "top-0 right-0";
      }
    },
    openAddLabel() {
      // 打开添加标签
      this.addLabel = true;
      this.addLabelForm = {
        label: "",
        textColor: "#fff",
        bgColor: "#004579",
        imgUrl: "",
        imgSize: 10,
        imgPosition: "top-right",
        isImg: false,
      };
    },

    toAddLabel() {
      // 确认添加标签
      this.addLabel = false;
      var data = JSON.stringify(this.addLabelForm);
      if (this.editLabelIndex != "" || this.editLabelIndex === 0) {
        this.form.labels.splice(this.editLabelIndex, 1, data);
        this.editLabelIndex = "";
      } else {
        this.form.labels.push(data);
      }
      this.addLabelForm = {
        label: "",
        textColor: "#fff",
        bgColor: "#004579",
        imgUrl: "",
        imgSize: 10,
        imgPosition: "top-right",
        isImg: false,
      };
    },
    editLabel(data, index) {
      // 编辑标签
      this.$refs.labelPopover[index].doClose();
      this.addLabel = true; // 打开添加标签
      this.editLabelIndex = index;
      this.addLabelForm = data;
    },
    delLabel(index) {
      // 删除标签
      this.$refs.labelPopover[index].doClose();
      this.form.labels.splice(index, 1);
    },
    cancelLabel() {
      // 取消添加标签
      this.addLabel = false;
      this.editLabelIndex = "";
      this.addLabelForm = {
        label: "",
        textColor: "#fff",
        bgColor: "#004579",
        imgUrl: "",
        imgSize: 10,
        imgPosition: "top-right",
        isImg: false,
      };
    },
    labelIndex(index, step) {
      // 标签前进后退
      this.$refs.labelPopover[index].doClose();
      if (step == 1) {
        if (index == 0) {
          return;
        }
        var temp = this.form.labels[index];
        // $set
        this.form.labels[index] = this.form.labels[index - 1];
        this.form.labels[index - 1] = temp;
      } else {
        if (index == this.form.labels.length - 1) {
          return;
        }
        var temp = this.form.labels[index];
        this.form.labels[index] = this.form.labels[index + 1];
        this.form.labels[index + 1] = temp;
      }
      this.$forceUpdate();
    },
    handleProduceChange(value) {
      // 产品类型切换
      this.form.product = value;
      if (value == "EasyStack") {
        this.$refs.form.clearValidate("authorization.access_key.id");
        this.$refs.form.clearValidate("authorization.access_key.secret");
      } else {
        this.$refs.form.clearValidate("authorization.password.userid");
        this.$refs.form.clearValidate("authorization.password.password");
      }
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
      if (this.arealist != null && this.arealist.length > 0) {
        this.arealist.forEach((item, index) => {
          item.id == "default"
            ? (this.form.target_nodes.dst_region_id = item.id)
            : "";
        });
        this.websiteinit();
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
        this.website.forEach((item, index) => {
          item.id == "default"
            ? (this.form.target_nodes.dst_site_id = item.id)
            : "";
        });
        this.edgesinit();
      }
    },
    async edgesinit() {
      const list = await edges({ site: this.form.target_nodes.dst_site_id });
      this.nodeList = list.nodes;
      if (this.nodeList != null && this.nodeList.length > 0) {
        this.form.target_nodes.dst_node_id = this.nodeList[0].node_id;
      }
    },
    async bpsinit() {
      const list = await getBps();
      this.BpsData = list.bps;
    },
    async userinit() {
      const params = {};
      if (this.form.bp_id != "" && this.form.bp_id != undefined) {
        params.bp_id = this.form.bp_id;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      this.userData = list.users;
    },
    handleChange(val) {
      for (const item in this.form.os_service_endpoints.endpoints) {
        if (this.form.os_service_endpoints.endpoints[item].name == val.name) {
          this.form.os_service_endpoints.endpoints[item].url = JSON.parse(
            JSON.stringify(val.url)
          );
        }
      }
    },
    handleRegionChange(val) {
      this.form.target_nodes.dst_region_id = val;
      this.form.target_nodes.dst_site_id = "";
      this.form.target_nodes.dst_node_id = "";
      this.nodeList = [];
      this.websiteinit();
    },
    handleSiteChange(val) {
      if (val != "" && val != undefined) {
        (this.form.target_nodes.dst_site_id = val),
          (this.form.target_nodes.dst_node_id = "");
        this.edgesinit();
      } else {
        this.form.target_nodes.dst_node_id = "";
        this.nodeList = [];
      }
    },
    handleNodeChange(val) {
      this.form.target_nodes.dst_node_id = val;
    },
    change_bp_id(val) {
      this.form.bp_id = val;
      this.form.user_id = "";
      this.userinit();
    },
    clear() {
      this.form.user_id = "";
    },
    change_user_id(val) {
      this.form.user_id = val;
    },
    async onOpen() {
      this.areainit();
      if (this.userInfo.kind == 2) {
        this.userinit();
      } else if (this.userInfo.kind != 2 && this.userInfo.kind != 4) {
        this.bpsinit();
        this.userinit();
      }
      const that = this;
      if (that.$refs.form) that.$refs.form.resetFields();
      if (that.isAdd === false) {
        var infoData = JSON.parse(JSON.stringify(that.info));
        for (var key in infoData) {
          if (key === "os_service_endpoints") {
            that.switchValue = true;
            Object.keys(infoData.os_service_endpoints.endpoints).forEach(
              (key) => {
                this.form.os_service_endpoints.endpoints[key].url = JSON.parse(
                  JSON.stringify(
                    infoData.os_service_endpoints.endpoints[key].url
                  )
                );
              }
            );
          } else {
            that.switchValue = false;
          }
          if (key === "url") {
            that.$set(that.form, key, infoData[key].split("//")[1]);
            that.form.urlHead = infoData[key].split("//")[0] + "//";
          }
          if (
            key != "node_id" &&
            key != "region_id" &&
            key != "site_id" &&
            key != "url"
          ) {
            that.$set(that.form, key, infoData[key]);
          }
        }
        that.form.target_nodes.dst_region_id = infoData.region_id;
        that.form.target_nodes.dst_site_id = infoData.site_id;
        that.form.target_nodes.dst_node_id = infoData.node_id;
        that.form.authorization = {
          access_key: {
            id: infoData.authorization.access_key
              ? infoData.authorization.access_key.id
              : "",
            secret: infoData.authorization.access_key
              ? infoData.authorization.access_key.secret
              : "",
          },
          password: {
            userid: infoData.authorization.password
              ? infoData.authorization.password.userid
              : "",
            password: infoData.authorization.password
              ? infoData.authorization.password.password
              : "",
          },
        };
      } else {
        this.resetForm();
      }
      this.entries = Object.values(this.form.os_service_endpoints.endpoints);
      this.entries.forEach(function (element, index) {
        element["placeholder"] = that.placeholders[index];
      });
      this.tableData = JSON.parse(JSON.stringify(this.entries));
    },
    cancel() {
      this.resetForm();
      this.dialog = false;
    },
    doSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAdd) {
            this.addData();
          } else {
            this.updateData();
          }
        } else {
          return false;
        }
      });
    },
    addData() {
      var data = JSON.parse(JSON.stringify(this.form));
      data.url = data.urlHead + data.url;
      data.urlHead == "http://" ? (data.certificate = "") : "";
      if (data.product == "NextStack") {
        delete data.os_service_endpoints;
        delete data.authorization.password;
      } else if (data.product == "EasyStack" && this.switchValue) {
        delete data.authorization.access_key;
        for (const item in data.os_service_endpoints.endpoints) {
          delete data.os_service_endpoints.endpoints[item].port;
          delete data.os_service_endpoints.endpoints[item].placeholder;
        }
      } else if (data.product == "EasyStack" && !this.switchValue) {
        delete data.authorization.access_key;
        delete data.os_service_endpoints;
      }
      delete data.urlHead;

      addCloudys(data)
        .then((res) => {
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
          this.dialog = false;
          this.resetForm();
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    updateData() {
      var data = JSON.parse(JSON.stringify(this.form));
      data.url = data.urlHead + data.url;
      data.urlHead == "http://" ? (data.certificate = "") : "";
      if (data.product == "NextStack") {
        delete data.os_service_endpoints;
        delete data.authorization.password;
      } else if (data.product == "EasyStack" && this.switchValue) {
        delete data.authorization.access_key;
        for (const item in data.os_service_endpoints.endpoints) {
          delete data.os_service_endpoints.endpoints[item].port;
          delete data.os_service_endpoints.endpoints[item].placeholder;
        }
      } else if (data.product == "EasyStack" && !this.switchValue) {
        delete data.authorization.access_key;
        delete data.os_service_endpoints;
      }
      delete data.urlHead;
      if (
        !this.isAdd &&
        this.form.status &&
        this.form.status.code &&
        ![3, 21, 50, 51].includes(this.form.status.code)
      ) {
        data = {
          cloud_id: this.form.cloud_id,
          name: this.form.name,
          labels: this.form.labels,
        };
      }
      updateCloudys(this.form.cloud_id, data)
        .then((res) => {
          this.$notify({
            title: this.$t("message.editSuccess"),
            type: "success",
            duration: 2500,
          });
          this.dialog = false;
          this.resetForm();
          this.loading = false;
          this.$parent.init();
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    resetForm() {
      this.switchValue = false;
      // if (this.$refs.form) this.$refs.form.resetFields();
      this.addLabel = false;
      this.addLabelForm = {
        label: "",
        textColor: "#fff",
        bgColor: "#004579",
        imgUrl: "",
        imgSize: 10,
        imgPosition: "top-right",
        isImg: false,
      };
      this.form = {
        name: "",
        urlHead: "http://",
        description: "",
        product: "NextStack",
        mode: "proxy",
        certificate: "",
        target_nodes: {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        },
        bp_id: "",
        user_id: "",
        url: "",
        health_check: {
          health_url: "",
          response: "",
          interval: 0,
        },
        authorization: {
          access_key: {
            id: "",
            secret: "",
          },
          password: {
            userid: "",
            password: "",
          },
        },
        os_service_endpoints: {
          endpoints: {
            keystone: {
              type: "identity",
              name: "keystone",
              url: "",
              port: ":5000",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:5000 / http://192.168.0.1/identity",
            },
            vnc: {
              type: "vnc",
              name: "vnc",
              url: "",
              port: ":6080",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:6080",
            },
            glance: {
              type: "image",
              name: "glance",
              url: "",
              port: ":9292",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:9292 / http://192.168.0.1/image",
            },
            cinderv2: {
              type: "volumev2",
              name: "cinderv2",
              url: "",
              port: ":8776",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8776 / http://192.168.0.1/volume",
            },
            cinderv3: {
              type: "volumev3",
              name: "cinderv3",
              url: "",
              port: ":8776",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8776 / http://192.168.0.1/volume ",
            },
            nova: {
              type: "compute",
              name: "nova",
              url: "",
              port: ":8774",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8774 / http://192.168.0.1/compute",
            },
            neutron: {
              type: "network",
              name: "neutron",
              url: "",
              port: ":9696",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:9696 / http://192.168.0.1/networking",
            },
            aodh: {
              type: "alarming",
              name: "aodh",
              url: "",
              port: ":8042",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8042 ",
            },
            ceilometer: {
              type: "metering",
              name: "ceilometer",
              url: "",
              port: ":8777",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8777",
            },
            placement: {
              type: "placement",
              name: "placement",
              url: "",
              port: ":8778",
              version: "",
              placeholder:
                this.$t("domain.example") +
                "http://192.168.0.1:8778 / http://192.168.0.1/placement ",
            },
            swift: {
              type: "object-store",
              name: "swift",
              url: "",
              port: ":8080",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8080",
            },
            gnocchi: {
              type: "metric",
              name: "gnocchi",
              url: "",
              port: ":8041",
              version: "",
              placeholder:
                this.$t("domain.example") + "http://192.168.0.1:8041",
            },
          },
          project_id: "",
        },
        labels: [],
      };
      this.switchValue = false;
    },
    getLintState(state) {
      this.yamlLintState = state;
      this.$emit("getYamlLint", state);
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
        _this.form.certificate = this.result;
      };
      document.getElementById("files").value = "";
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-input-group__append,
.el-input-group__prepend {
  padding: 0 10px;
}

::v-deep .el-input-group__append {
  background-color: #ffffff;
}

input {
  padding: 0;
  line-height: inherit;
  color: inherit;
  padding: 5px 5px;
  width: 50px;
}

input:focus {
  outline: 0;
  border: 0px solid #123456;
}

.masaike {
  --offsetX: 0px;
  --offsetY: 0px;
  --size: 6px;
  --color: #dedcdc;
  background-image: linear-gradient(
      45deg,
      var(--color) 25%,
      transparent 0,
      transparent 75%,
      var(--color) 0
    ),
    linear-gradient(
      45deg,
      var(--color) 25%,
      transparent 0,
      transparent 75%,
      var(--color) 0
    );
  background-position: var(--offsetX) var(--offsetY),
    calc(var(--size) + var(--offsetX)) calc(var(--size) + var(--offsetY));
  background-size: calc(var(--size) * 2) calc(var(--size) * 2);
  border: 1px solid #dedcdc;
}
</style>
