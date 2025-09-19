<template>
  <el-dialog
    :append-to-body="true"
    :visible.sync="dialog"
    :title="isAdd ? $t('form.addSite') : $t('form.editSite')"
    width="800px"
    :before-close="handleClose"
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
      <el-form-item :label="$t('form.belongRegion') + ':'" prop="region_id">
        <el-select
          v-model="form.region_id"
          :placeholder="$t('form.pleaseSelect')"
          size="small"
          filterable
        >
          <el-option
            v-for="item in regions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item :label="$t('form.siteName') + ':'" prop="name">
        <el-input
          v-model="form.name"
          :placeholder="$t('form.pleaseInputContent')"
          size="small"
        />
      </el-form-item>
      <el-form-item :label="$t('form.description') + ':'" prop="description">
        <el-input
          v-model="form.description"
          :placeholder="$t('form.pleaseInputContent')"
          size="small"
          type="textarea"
          maxlength="255"
          show-word-limit
        />
      </el-form-item>
      <el-collapse accordion>
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
              {{ $t("form.locationInfo") }}
              <div
                style="
                  font-weight: 400;
                  font-style: normal;
                  font-size: 10px;
                  color: rgba(0, 0, 0, 0.447058823529412);
                  line-height: 22px;
                "
              >
                {{ $t("form.configureLocationInfo") }}
              </div>
            </div>
          </template>
          <el-form-item :label="$t('form.country') + ':'" prop="nation">
            <el-select
              v-model="form.location.nation"
              :placeholder="$t('form.pleaseSelect')"
              size="small"
              clearable
            >
              <el-option
                v-for="(item, index) in nationsData"
                :key="index"
                :label="item.cn_name"
                :value="item.nation"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('form.province') + ':'" prop="address">
            <el-select
              v-model="form.location.address0"
              :placeholder="$t('form.pleaseSelect')"
              size="small"
              clearable
            >
              <el-option
                v-for="(item, index) in divistionsData"
                :key="index"
                :label="item.cn_name"
                :value="item.adcode"
              />
            </el-select>
            <el-select
              v-model="form.location.address1"
              :placeholder="$t('form.pleaseSelect')"
              size="small"
              clearable
            >
              <el-option
                v-for="(item, index) in divistionsData1"
                :key="index"
                :label="item.cn_name"
                :value="item.adcode"
              />
            </el-select>
            <el-select
              v-model="form.location.address2"
              :placeholder="$t('form.pleaseSelect')"
              size="small"
              clearable
            >
              <el-option
                v-for="(item, index) in divistionsData2"
                :key="index"
                :label="item.cn_name"
                :value="item.adcode"
              />
            </el-select>
            <el-select
              v-model="form.location.address3"
              :placeholder="$t('form.pleaseSelect')"
              size="small"
            >
              <el-option
                v-for="(item, index) in divistionsData3"
                :key="index"
                :label="item.cn_name"
                :value="item.adcode"
              />
            </el-select>
          </el-form-item>
          <el-form-item
            :label="$t('form.detailedAddress') + ':'"
            prop="detail_address"
          >
            <el-input
              v-model="form.location.detail_address"
              :placeholder="$t('form.pleaseInputDetailedAddress')"
              size="small"
            />
          </el-form-item>
          <el-form-item
            :label="$t('form.administrativeDivisionCode') + ':'"
            prop="adcode"
          >
            <el-input
              v-model="form.location.adcode"
              :placeholder="$t('form.pleaseInputContent')"
              size="small"
            />
          </el-form-item>
          <el-form-item :label="$t('form.longitude') + ':'" prop="longitude">
            <el-input
              v-model="form.location.longitude"
              :placeholder="$t('form.pleaseInputLongitude')"
              size="small"
            />
          </el-form-item>
          <el-form-item :label="$t('form.latitude') + ':'" prop="latitude">
            <el-input
              v-model="form.location.latitude"
              :placeholder="$t('form.pleaseInputLatitude')"
              size="small"
            />
          </el-form-item>
          <el-form-item :label="$t('form.altitude') + ':'" prop="elevation">
            <el-input
              v-model="form.location.elevation"
              :placeholder="$t('form.pleaseInputAltitude')"
              size="small"
            />
          </el-form-item>
        </el-collapse-item>
        <el-collapse-item name="2">
          <template slot="title">
            <div
              style="
                padding-left: 10px;
                font-size: 14px;
                font-family: 'PingFangSC-Regular', 'PingFang SC', sans-serif;
                font-weight: 400;
                font-style: normal;
                font-size: 14px;
                color: rgba(0, 0, 0, 0.847058823529412);
                line-height: 22px;
              "
            >
              {{ $t("form.label") }} / {{ $t("form.stain") }}
              <div
                style="
                  font-family: 'PingFangSC-Regular', 'PingFang SC', sans-serif;
                  font-weight: 400;
                  font-style: normal;
                  font-size: 10px;
                  color: rgba(0, 0, 0, 0.447058823529412);
                  line-height: 22px;
                "
              >
                {{ $t("form.configureLabelAndStainWithNode") }}
              </div>
            </div>
          </template>
          <div class="Dev_need_info">
            <h4 class="nav">{{ $t("form.label") }}:</h4>
            <el-table
              :data="labelData"
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
  addSites,
  getRegions,
  editSites,
  nations,
  divistions,
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
      nowatch: true,
      regions: [],
      id: "",
      nationsData: [],
      divistionsData: [],
      divistionsData1: [],
      divistionsData2: [],
      divistionsData3: [],
      // 折中数据
      labelsData: [],
      stainsData: [],
      // 表格数据
      labelData: [],
      stainData: [],
      radio: false,
      isAdd: true,
      loading: false,
      dialog: false,
      // 组织机构数据
      labelBpsData: [],
      // 用户
      labelUserData: [],
      // 组织机构数据
      stainBpsData: [],
      // 用户
      stainUserData: [],
      // ID重复标识
      id_flag: false,
      form: {
        name: "",
        id: "",
        region_id: "",
        description: "",
        location: {
          nation: "",
          address: "",
          address0: "",
          address1: "",
          address2: "",
          address3: "",
          detail_address: "",
          adcode: "",
          longitude: "",
          latitude: "",
          elevation: "",
        },
        labels: {},
        taints: {},
      },

      rules: {
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputSiteName"),
            trigger: "blur",
          },
        ],
        id: [
          {
            required: true,
            message: this.$t("message.pleaseInputSiteId"),
            trigger: "blur",
          },
        ],
        region_id: [
          {
            required: true,
            message: this.$t("message.pleaseSelectRegion"),
            trigger: "change",
          },
        ],
      },
    };
  },
  watch: {
    "form.location.nation": {
      deep: true,
      handler(val) {
        if (this.nowatch) {
          this.form.location.address0 = "";
          this.form.location.address1 = "";
          this.form.location.address2 = "";
          this.form.location.address3 = "";
          this.form.location.adcode = "";
          this.form.location.longitude = "";
          this.form.location.latitude = "";
        }

        if (val != "") {
          this.getdivistions(val, "");
        } else {
          this.form.location.address0 = "";
          this.form.location.address1 = "";
          this.form.location.address2 = "";
          this.form.location.address3 = "";
          this.divistionsData = [];
          this.divistionsData1 = [];
          this.divistionsData2 = [];
          this.divistionsData3 = [];
          this.form.location.adcode = "";
          this.form.location.longitude = "";
          this.form.location.latitude = "";
        }
      },
    },
    "form.location.address0": {
      deep: true,
      handler(val) {
        if (this.nowatch && val != "") {
          this.form.location.address1 = "";
          this.form.location.address2 = "";
          this.form.location.address3 = "";
          this.form.location.adcode = "";
          this.form.location.longitude = "";
          this.form.location.latitude = "";
          var data = this.getaddresstext(this.divistionsData, val);

          if (data != undefined) {
            this.form.location.adcode = data.adcode;
            this.form.location.longitude = data.max_longitude;
            this.form.location.latitude = data.max_latitude;
          }
        }
        if (val != "") {
          this.getdivistions1(this.form.location.nation, val);
        } else {
          this.form.location.address1 = "";
          this.form.location.address2 = "";
          this.form.location.address3 = "";
          this.divistionsData1 = [];
          this.divistionsData2 = [];
          this.divistionsData3 = [];
        }
      },
    },
    "form.location.address1": {
      deep: true,
      handler(val) {
        if (this.nowatch && val != "") {
          this.form.location.address2 = "";
          this.form.location.address3 = "";
          this.form.location.adcode = "";
          this.form.location.longitude = "";
          this.form.location.latitude = "";
          var data = this.getaddresstext(this.divistionsData1, val);
          if (data != undefined) {
            this.form.location.adcode = data.adcode;
            this.form.location.longitude = data.max_longitude;
            this.form.location.latitude = data.max_latitude;
          }
        }
        if (val != "") {
          this.getdivistions2(this.form.location.nation, val);
        } else {
          this.form.location.address1 = "";
          this.form.location.address2 = "";
          this.form.location.address3 = "";
          this.divistionsData2 = [];
          this.divistionsData3 = [];
        }
      },
    },
    "form.location.address2": {
      deep: true,
      handler(val) {
        if (this.nowatch && val != "") {
          this.form.location.address3 = "";
          this.form.location.adcode = "";
          this.form.location.longitude = "";
          this.form.location.latitude = "";
          var data = this.getaddresstext(this.divistionsData2, val);
          if (data != undefined) {
            this.form.location.adcode = data.adcode;
            this.form.location.longitude = data.max_longitude;
            this.form.location.latitude = data.max_latitude;
          }
        }
        if (val != "") {
          this.getdivistions3(this.form.location.nation, val);
        } else {
          this.form.location.address3 = "";
          this.divistionsData3 = [];
        }
      },
    },
    "form.location.address3": {
      deep: true,
      handler(val) {
        if (this.nowatch && val != "") {
          this.form.location.adcode = "";
          this.form.location.longitude = "";
          this.form.location.latitude = "";
          var data = this.getaddresstext(this.divistionsData3, val);
          if (data != undefined) {
            this.form.location.adcode = data.adcode;
            this.form.location.longitude = data.max_longitude;
            this.form.location.latitude = data.max_latitude;
          }
        }
      },
    },
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  created() {
    this.getnations();
    this.labelbpsinit();
    this.labeluserinit();
    this.stainuserinit();
  },
  methods: {
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
    // Input 失去光标事件
    onChange() {
      if (this.form.id != "" && this.form.id != undefined) {
        var data = {
          region_id: "",
          site_id: this.form.id,
        };

        uniqueness(data)
          .then((res) => {
            if (res.site_id == true) {
              this.id_flag = false;
            } else {
              this.id_flag = true;
              this.$notify({
                title: $t("form.siteIdAlreadyExists"),
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
      this.labelsData = [];
      const list = await getLabelOption("site");
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          if (list[i].key != undefined && list[i].key != null) {
            list[i].required == true
              ? (list[i].required = $t("form.yes"))
              : (list[i].required = $t("form.no"));
          }
        }
        this.labelsData = list;
      }
    },
    async getTaintOptions() {
      this.stainsData = [];
      const list = await getTaintOptions("site");
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          if (list[i].key != undefined && list[i].key != null) {
            list[i].required == true
              ? (list[i].required = $t("form.yes"))
              : (list[i].required = $t("form.no"));
          }
        }
        this.stainsData = list;
      }
    },
    handleClose() {
      this.resetForm();
    },
    getaddresstext(list, val) {
      var data;
      if (list.length > 0) {
        list.forEach((item) => {
          if (item.adcode == val) {
            data = item;
          }
        });
      }
      return data;
    },
    getaddressCode(list, val) {
      var data;
      if (list == "" || list.length === 0) {
        return "";
      } else {
        list.forEach((item) => {
          if (item.adcode == val) {
            data = item.cn_name;
          }
        });
        if (data == undefined) {
          return "";
        } else {
          return data;
        }
      }
    },
    async getnations() {
      const list = await nations();
      this.nationsData = list;
    },
    async getdivistions(nation, adcode) {
      const list = await divistions(nation, adcode);
      this.divistionsData = list;
    },
    async getdivistions1(nation, adcode) {
      const list = await divistions(nation, adcode);
      this.divistionsData1 = list;
    },
    async getdivistions2(nation, adcode) {
      const list = await divistions(nation, adcode);
      this.divistionsData2 = list;
    },
    async getdivistions3(nation, adcode) {
      const list = await divistions(nation, adcode);
      this.divistionsData3 = list;
      this.nowatch = true;
    },
    async onOpen() {
      const that = this;
      if (that.$refs.form) that.$refs.form.resetFields();
      that.$set(that.form, "name", that.info.name);
      that.$set(that.form, "description", that.info.description);
      that.$set(that.form, "id", that.info.id);
      that.$set(that.form, "region_id", that.info.region_id);
      that.$set(that.form, "labels", that.info.labels);
      that.$set(that.form, "taints", that.info.taints);

      const list = await getRegions();
      that.regions = list.regions.map((item) => {
        return {
          value: item.id,
          label: item.name,
        };
      });
      that.form.region_id = that.regions[0].value;
      if (that.isAdd === false) {
        that.$nextTick(() => {
          that.nowatch = false;
        });
        var addressText = that.info.location.address;
        var codeMain;
        if (addressText.indexOf("&") != -1) {
          codeMain = this.info.location.address
            .slice(addressText.indexOf("&") + 1, addressText.length)
            .split(",");
        } else {
          codeMain = [];
        }
        if (that.info.location.nation) {
          that.divistionsData = await divistions(that.info.location.nation, "");

          if (codeMain[0]) {
            that.divistionsData1 = await divistions(
              that.info.location.nation,
              codeMain[0]
            );
          }
          if (codeMain[1]) {
            that.divistionsData2 = await divistions(
              that.info.location.nation,
              codeMain[1]
            );
          }
          if (codeMain[2]) {
            that.divistionsData3 = await divistions(
              that.info.location.nation,
              codeMain[2]
            );
          }
          that.info.location.address0 = codeMain[0];
          that.info.location.address1 = codeMain[1];
          that.info.location.address2 = codeMain[2];
          that.info.location.address3 = codeMain[3];
          var locationData = {
            adcode: that.info.location.adcode,
            address: that.info.location.address,
            address0: codeMain[0],
            address1: codeMain[1],
            address2: codeMain[2],
            address3: codeMain[3],
            detail_address: that.info.location.detail_address,
            elevation: that.info.location.elevation,
            latitude: that.info.location.latitude,
            longitude: that.info.location.longitude,
            nation: that.info.location.nation,
          };
          that.$set(that.form, "location", locationData);
        }
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
                required: $t("form.no"),
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
              if (that.stainsData[i].key.indexOf("bpId") != -1) {
                that.stainbp_id = that.info.taints[that.stainsData[i].key];
                that.stainuserinit(1);
              }
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
                required: $t("form.no"),
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
      this.resetForm();
    },
    doSubmit() {
      if (this.id_flag == true) {
        return;
      }
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
            title: $t("message.pleaseInputTaintKey"),
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
            title: $t("message.pleaseInputKey"),
            type: "info",
            duration: 2500,
          });
          flag = false;
          return false;
        }
      });
      if (flag) {
        this.form["labels"] = labelsObj;
        this.form["taints"] = taintsObj;
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
      }
    },
    doAdd() {
      var data = JSON.parse(JSON.stringify(this.form));

      data.location.addressCode = [
        data.location.address0,
        data.location.address1,
        data.location.address2,
        data.location.address3,
      ];
      data.location.address =
        this.getaddressCode(this.divistionsData, data.location.address0) +
        (this.getaddressCode(this.divistionsData1, data.location.address1) != ""
          ? "/"
          : "") +
        this.getaddressCode(this.divistionsData1, data.location.address1) +
        (this.getaddressCode(this.divistionsData2, data.location.address2) != ""
          ? "/"
          : "") +
        this.getaddressCode(this.divistionsData2, data.location.address2) +
        (this.getaddressCode(this.divistionsData3, data.location.address3) != ""
          ? "/"
          : "") +
        this.getaddressCode(this.divistionsData3, data.location.address3) +
        "&" +
        data.location.addressCode;

      addSites(data)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.addSuccess"),
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
    doEdit() {
      this.form.location.addressCode = [
        this.form.location.address0,
        this.form.location.address1,
        this.form.location.address2,
        this.form.location.address3,
      ];

      this.form.location.address =
        this.getaddressCode(this.divistionsData, this.form.location.address0) +
        (this.getaddressCode(
          this.divistionsData1,
          this.form.location.address1
        ) != ""
          ? "/"
          : "") +
        this.getaddressCode(this.divistionsData1, this.form.location.address1) +
        (this.getaddressCode(
          this.divistionsData2,
          this.form.location.address2
        ) != ""
          ? "/"
          : "") +
        this.getaddressCode(this.divistionsData2, this.form.location.address2) +
        (this.getaddressCode(
          this.divistionsData3,
          this.form.location.address3
        ) != ""
          ? "/"
          : "") +
        this.getaddressCode(this.divistionsData3, this.form.location.address3) +
        "&" +
        this.form.location.addressCode;
      editSites(this.id, this.form)
        .then((res) => {
          this.resetForm();
          this.$notify({
            title: this.$t("message.modifySuccess"),
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
    resetForm() {
      this.dialog = false;
      this.$nextTick(() => {
        if (this.$refs["form"] !== undefined) {
          this.$refs["form"].resetFields();
        }
      });
      this.isAdd = true;
      this.labelData = [];
      this.stainData = [];
      this.form = {
        name: "",
        region_id: "",
        id: "",
        labels: {},
        taints: {},
        description: "",
        location: {
          nation: "",
          address: "",
          address0: "",
          address1: "",
          address2: "",
          address3: "",
          detail_address: "",
          adcode: "",
          longitude: "",
          latitude: "",
          elevation: "",
        },
      };
    },
  },
};
</script>

<style scoped lang="scss">
::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

::v-deep .el-table__empty-block {
  display: none !important;
}

::v-deep .el-collapse-item__header {
  background-color: rgba(250, 250, 250, 1) !important;
  margin-bottom: 5px;
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

::v-deep .el-table--border,
.el-table--group {
  border: 1px solid #ebeef5;
}

::v-deep .el-form-item__content {
  line-height: 32px;
  position: relative;
  font-size: 14px;
}

::v-deep .el-table--border,
.el-table--group {
  border: 1px solid #ebeef5;
}

::v-deep #uncolor {
  color: #f54518 !important;
}
</style>
