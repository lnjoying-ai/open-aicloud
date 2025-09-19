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
        label-width="145px"
        class="demo-ruleForm"
        size="small"
      >
        <el-form-item :label="$t('form.alarmName') + ':'" prop="name">
          <el-input
            v-model="form.name"
            class="w-60"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            class="w-96"
          />
        </el-form-item>
        <el-divider />
        <el-form-item :label="$t('form.ruleType') + ':'" prop="rule_type">
          <div>
            <el-radio-group v-model="form.rule_type" size="small">
              <el-radio-button :label="1">{{
                $t("form.staticThreshold")
              }}</el-radio-button>
              <el-radio-button :label="2">{{
                $t("form.customPromQL")
              }}</el-radio-button>
            </el-radio-group>
          </div>
        </el-form-item>
        <el-form-item
          :label="$t('form.prometheusInstance') + ':'"
          prop="data_source_id"
        >
          <el-select
            v-model="form.data_source_id"
            class="w-60"
            :placeholder="$t('form.pleaseSelectPrometheusInstance')"
            @change="handlePrometheusChange"
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
        <el-form-item
          :label="$t('form.alarmGroup') + ':'"
          :prop="form.rule_type == 1 ? 'alarm_group_id' : ''"
        >
          <el-select
            v-model="form.alarm_group_id"
            class="w-60"
            :placeholder="$t('form.pleaseSelectAlarmGroup')"
            @change="getAlarmMetricsList(form.alarm_group_id)"
          >
            <el-option
              v-for="(item, index) in alarmGroupList"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          :label="$t('form.alarmMetric') + ':'"
          :prop="form.rule_type == 1 ? 'alarm_metric_id' : ''"
        >
          <el-select
            v-model="form.alarm_metric_id"
            class="w-60"
            :placeholder="$t('form.pleaseSelectAlarmMetric')"
            @change="
              getAlarmMetricsInfo(form.alarm_group_id, form.alarm_metric_id)
            "
          >
            <el-option
              v-for="(item, index) in alarmMetricList"
              :key="index"
              :label="item.metric_name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="form.rule_type == 2"
          :label="$t('form.customPromQL') + ':'"
          prop="expr"
        >
          <el-input
            v-model="montageexpr"
            class="w-96"
            type="textarea"
            :rows="3"
            :placeholder="$t('form.pleaseInputCustomPromQL')"
          />
        </el-form-item>
        <el-form-item
          v-if="form.rule_type == 1 && form.alarm_metric_id"
          :label="$t('form.alarmConditions') + ':'"
          prop="alarmconditions"
        >
          <div class="memory_outsides">
            <div
              v-for="(item, index) in form.alert_rule.alert_condition_contents"
              :key="index"
              class="memory"
            >
              <div v-if="item.type == 'text'">
                {{ item.value }}
              </div>
              <div v-if="item.type == 'var'">
                <div
                  v-for="(item_alarm, index_alarm) in form.alert_rule
                    .alert_condition_params"
                  :key="index_alarm"
                >
                  <div v-if="item.value == item_alarm.labelName">
                    <span v-if="item_alarm.showType == 'text'">{{
                      item_alarm.detail.value
                    }}</span>

                    <span
                      v-if="
                        item_alarm.showType == 'input' &&
                        item_alarm.valueType == 'number'
                      "
                    >
                      <el-input-number
                        v-model="item_alarm.detail.value"
                        :style="item_alarm.detail.minWidth"
                        oninput="value=value.replace(/[^\d]/g,'')"
                        :placeholder="item_alarm.detail.placeholder"
                        class="w-32 ml-1"
                        :precision="2"
                        :step="0.1"
                        @change="
                          handleMonitorPrometheusChange('alarm', item_alarm)
                        "
                      />
                    </span>
                    <span
                      v-else-if="
                        item_alarm.showType == 'input' &&
                        item_alarm.valueType != 'number'
                      "
                    >
                      <el-input
                        v-model="item_alarm.detail.value"
                        :style="item_alarm.detail.minWidth"
                        class="w-32"
                        :placeholder="item_alarm.detail.placeholder"
                        @change="
                          handleMonitorPrometheusChange('alarm', item_alarm)
                        "
                      />
                    </span>
                    <span v-else-if="item_alarm.showType == 'select'">
                      <el-select
                        v-model="item_alarm.detail.value"
                        :style="item_alarm.detail.minWidth"
                        class="w-32"
                        :placeholder="item_alarm.detail.placeholder"
                        @change="
                          handleMonitorPrometheusChange('alarm', item_alarm)
                        "
                      >
                        <el-option
                          v-for="(items, index) in item_alarm.detail.values"
                          :key="index"
                          :label="items.label"
                          :value="items.value"
                        /> </el-select
                    ></span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-form-item>
        <el-divider v-if="form.rule_type == 1 && form.alarm_metric_id" />
        <!-- <div>{{ form.rule_type }}</div>
        <div>{{ form.alarm_metric_id }}</div>
        <div> {{ this.alarmMetricInfo.data_filters }}</div> -->

        <el-form-item
          v-if="
            form.rule_type == 1 &&
            form.alarm_metric_id &&
            alarmMetricInfo.data_filters
          "
          :label="$t('form.filterConditions') + ':'"
        >
          <div>
            <span
              v-for="(item, index) in form.alert_rule.data_condition_params"
              :key="index"
            >
              {{ item.filterDimName }}
              <el-select
                v-model="item.opt"
                class="w-32"
                :placeholder="$t('form.pleaseSelect')"
                @change="handleMonitorPrometheusChange('condition')"
              >
                <el-option
                  v-for="(items, index) in item.supportOpts"
                  :key="index"
                  :label="items.label"
                  :value="items.value"
                />
              </el-select>
              <el-input
                v-if="item.opt !== 'ALL' && !item.filterDimResourceType"
                v-model="item.value"
                style="width: 200px"
                :placeholder="
                  item.placeholder ? item.placeholder : $t('form.pleaseInput')
                "
                @change="handleMonitorPrometheusChange('condition')"
              />
              <el-select
                v-if="
                  item.opt !== 'ALL' &&
                  validResourceType(item.filterDimResourceType)
                "
                v-model="item.value"
                style="width: 200px"
                filterable
                allow-create
                :placeholder="
                  item.placeholder ? item.placeholder : $t('form.pleaseInput')
                "
                @change="handleMonitorPrometheusChange('condition')"
              >
                <el-option
                  v-for="(option, index) in getResourceList(
                    item.filterDimResourceType,
                    item.filterDimPrefixField,
                    item.filterDimSuffixField
                  )"
                  :key="index"
                  :label="option.name"
                  :value="option.value"
                />
              </el-select>
            </span>
          </div>
        </el-form-item>
        <el-form-item
          v-show="form.alarm_metric_id && form.rule_type == 1"
          :label="$t('form.expression') + ':'"
        >
          <div>
            <div
              v-for="(item, index) in form.expr"
              :key="index"
              class="datapreview"
              style="float: left"
            >
              <span v-if="item.type == 'text'">
                {{ item.value }}
              </span>
              <span v-if="item.type == 'var'">
                <div
                  v-for="(item_alarm, index_alarm) in form.alert_rule
                    .data_condition_params"
                  :key="index_alarm"
                >
                  <div v-if="item.value == item_alarm.filterLabel">
                    <span v-if="item_alarm.opt == 'ALL'" />
                    <span v-if="item_alarm.opt != 'ALL'">
                      {{ item.value }}{{ item_alarm.opt
                      }}{{ '"' + item_alarm.value + '"' + "," }}
                    </span>
                  </div>
                </div>
                <div
                  v-for="(item_alarm, index_alarm) in form.alert_rule
                    .alert_condition_params"
                  :key="index_alarm"
                >
                  <div v-if="item.value != item_alarm.labelName" />
                  <div v-if="item.value == item_alarm.labelName">
                    {{ item_alarm.detail.value }}
                  </div>
                </div>
              </span>
            </div>
            <div
              v-for="(item, index) in form.expr"
              :key="index"
              class="datapreview-expr"
              style="float: left; display: none"
            >
              <span v-if="item.type == 'text'">
                {{ item.value }}
              </span>
              <span v-if="item.type == 'var'">
                <div
                  v-for="(item_alarm, index_alarm) in form.alert_rule
                    .data_condition_params"
                  :key="index_alarm"
                >
                  <div v-if="item.value == item_alarm.filterLabel">
                    <span v-if="item_alarm.opt == 'ALL'" />
                    <span v-if="item_alarm.opt != 'ALL'">
                      {{ item.value }}{{ item_alarm.opt
                      }}{{ '"' + item_alarm.value + '"' + "," }}
                    </span>
                  </div>
                </div>
                <div
                  v-for="(item_alarm, index_alarm) in form.alert_rule
                    .alert_condition_params"
                  :key="index_alarm"
                >
                  <div v-if="item.value != item_alarm.labelName" />
                  <div v-if="item.value == item_alarm.labelName && checkValue">
                    {{ item_alarm.detail.value }}
                  </div>
                </div>
              </span>
            </div>
            <div>
              <span v-if="showCheckout" style="float: right">
                <el-checkbox v-model="checkValue" @change="handleValueChange">{{
                  $t("form.filterByThreshold")
                }}</el-checkbox>
              </span>
            </div>
          </div>
        </el-form-item>
        <el-form-item
          v-show="form.alarm_metric_id && form.rule_type == 1"
          label=""
        >
          <el-table
            ref="multipleTable"
            v-loading="loading"
            :data="monitorPrometheusData"
            tooltip-effect="dark"
            style="width: 100%"
            :element-loading-text="$t('domain.loading')"
            max-height="300"
            size="small"
          >
            <el-table-column show-overflow-tooltip label="key">
              <template slot-scope="scope">
                <span
                  v-for="(value, key, index) in scope.row.metric"
                  :key="index"
                >
                  <span v-if="key == '__name__'">{{ value }} </span>
                  <span v-else>{{ key + "= " }}{{ value }} </span>
                </span>
              </template>
            </el-table-column>
            <el-table-column label="value" show-overflow-tooltip width="180">
              <template slot-scope="scope">
                <span
                  v-for="(value, key, index) in scope.row.value"
                  :key="index"
                >
                  <span v-if="key == 1">{{ value }}</span>
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-divider />
        <el-form-item
          :label="$t('form.durationTime') + ':'"
          prop="duration_time"
        >
          {{ $t("form.whenTheAlarmConditionIsSatisfied") }}
          <el-input
            v-model="form.duration_time"
            class="w-20"
            :placeholder="$t('form.pleaseInputDurationTime')"
          />
          {{ $t("form.minutes") }}{{ $t("form.produceAlarmEvents") }}
        </el-form-item>
        <el-form-item :label="$t('form.alarmLevel') + ':'" prop="level">
          <el-select
            v-model="form.level"
            class="w-60"
            :placeholder="$t('form.pleaseSelectAlarmLevel')"
          >
            <el-option
              v-for="(item, index) in dalert_level_list"
              :key="index"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="form.alarm_metric_id"
          :label="$t('form.alarmContent') + ':'"
          prop="alert_message"
        >
          <el-input
            v-model="form.alert_message"
            class="w-96"
            type="textarea"
            :rows="3"
            :placeholder="$t('form.pleaseInputAlarmContent')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.label') + ':'" prop="labels">
          <span v-for="(item, index) in labelsimidata" :key="index">
            <el-form
              :model="item"
              size="small"
              :inline="true"
              :rules="labelrules"
            >
              <el-form-item label="" prop="key">
                <el-input
                  v-model="labelsimidata[index].key"
                  class="w-32"
                  :placeholder="$t('form.pleaseInput')"
                />
              </el-form-item>
              :
              <el-form-item label="" prop="value">
                <el-input
                  v-model="labelsimidata[index].value"
                  class="w-32"
                  :placeholder="$t('form.pleaseInput')"
                />
                <i
                  style="color: red; cursor: pointer; font-size: 18px"
                  class="el-icon-remove-outline ml-2"
                  @click="deletelabels(index)"
                />
                <br />
              </el-form-item>
            </el-form>
          </span>

          <el-button class=" " type="primary" size="mini" @click="pushlabels">
            {{ $t("form.addLabel") }}
          </el-button>
        </el-form-item>
        <el-form-item :label="$t('form.annotation') + ':'" prop="annotations">
          <span v-for="(item, index) in annotationsdata" :key="index">
            <el-form
              :model="item"
              size="small"
              :inline="true"
              :rules="labelrules"
            >
              <el-form-item label="" prop="key">
                <el-input
                  v-model="annotationsdata[index].key"
                  class="w-32"
                  :placeholder="$t('form.pleaseInput')"
                />
              </el-form-item>
              :
              <el-form-item label="" prop="value">
                <el-input
                  v-model="annotationsdata[index].value"
                  class="w-32"
                  :placeholder="$t('form.pleaseInput')"
                />
                <i
                  style="color: red; cursor: pointer; font-size: 18px"
                  class="el-icon-remove-outline ml-2"
                  @click="deleteannotations(index)"
                />
                <br />
              </el-form-item>
            </el-form>
          </span>
          <el-button
            class=" "
            type="primary"
            size="mini"
            @click="pushannotations"
            >{{ $t("form.addAnnotation") }}</el-button
          >
        </el-form-item>
        <el-divider />
        <el-form-item :label="$t('form.notifyObject') + ':'">
          <el-radio-group
            v-model="notificationStatus"
            @change="handleCheckChange"
          >
            <el-radio :label="true">{{ $t("form.noNotification") }}</el-radio>
            <el-radio :label="false">{{ $t("form.notification") }}</el-radio>
          </el-radio-group>
          <el-button
            v-if="!notificationStatus"
            type="text"
            class="ml-4"
            @click="openobjectframe"
            ><i class="el-icon-plus" />{{
              $t("form.addNotifyObject")
            }}</el-button
          >
          <ul v-if="!notificationStatus" class="w-addAlarm2">
            <li
              v-for="(item, index) in form.notify_rules.notify_objects"
              :key="index"
              class="mt-2"
            >
              <el-row class="ntificationobject px-4">
                <el-col :span="12">
                  <div class="grid-content bg-purple">
                    <span>{{ $t("form.userName") }}:{{ item.name }}</span>
                  </div>
                </el-col>
                <el-col :span="12">
                  <div
                    class="grid-content bg-purple-light"
                    style="display: flex"
                  >
                    {{ $t("form.notifyType") }}:
                    <el-checkbox-group
                      v-if="isAdd && !notificationStatus"
                      v-model="item.notify_type"
                      class="ml-5"
                    >
                      <el-checkbox
                        v-if="item.notify_type_new.includes('0')"
                        label="0"
                        >{{ $t("form.email") }}</el-checkbox
                      >
                      <el-checkbox
                        v-if="item.notify_type_new.includes('1')"
                        label="1"
                        >{{ $t("form.mobile") }}</el-checkbox
                      >
                    </el-checkbox-group>
                    <el-checkbox-group
                      v-if="!isAdd && !notificationStatus"
                      v-model="item.notify_channels"
                      class="ml-5"
                    >
                      <el-checkbox
                        v-if="item.notify_channels_new.includes('0')"
                        label="0"
                        >{{ $t("form.email") }}</el-checkbox
                      >
                      <el-checkbox
                        v-if="item.notify_channels_new.includes('1')"
                        label="1"
                        >{{ $t("form.mobile") }}</el-checkbox
                      >
                    </el-checkbox-group>
                  </div>
                </el-col>
              </el-row>
            </li>
          </ul>
        </el-form-item>
        <el-form-item
          v-if="!notificationStatus"
          :label="$t('form.notifyTime') + ':'"
        >
          <el-time-picker
            v-model="form.notify_rules.notify_start_time"
            class="w-32"
            :placeholder="$t('form.startTime')"
          />
          -
          <el-time-picker
            v-model="form.notify_rules.notify_end_time"
            class="w-32"
            :placeholder="$t('form.endTime')"
          />
          <p class="prompt">
            {{ $t("form.notifyTimeTips") }}
          </p>
        </el-form-item>
        <el-form-item
          v-if="!notificationStatus"
          :label="$t('form.repeatStrategy') + ':'"
        >
          <el-radio v-model="form.notify_rules.repeat_notify" :label="true">{{
            $t("form.yes")
          }}</el-radio>
          <el-radio v-model="form.notify_rules.repeat_notify" :label="false">{{
            $t("form.no")
          }}</el-radio>
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
              >{{ $t("form.confirm") }}</el-button
            >
          </div>
        </el-col>
      </el-row>
    </div>
    <Noticeadd
      ref="addForm"
      :sup_this="sup_this"
      :info="selectobjectdta"
      @child-msg="handleChildMsg"
    />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Noticeadd from "./selectobject.vue";
import { getCloudys } from "@/api/mainApi";
import {
  edges,
  getDeploys,
  getMonitorPrometheus,
  getAlarmGroups,
  getAlarmMetrics,
  addAlarms,
  editAlarms,
  getAlarmsInfo,
  monitorPrometheusDataQuery,
} from "@/api/mainApi";
import mainApi from "@/api/nextStack/mainApi";
export default {
  components: {
    Noticeadd,
  },
  data() {
    // 通知对象的自定义校验
    var notify_objects_custom = (rule, value, callback) => {
      if (this.form.notify_rules.notify_objects.length == 0) {
        callback(new Error(this.$t("message.pleaseSelectNotifyObject")));
      } else {
        callback();
      }
    };
    // 自定义自定义promQL语句的校验
    var promQL_custom = (rule, value, callback) => {
      if (!this.montageexpr) {
        callback(new Error(this.$t("message.pleaseInputCustomPromQL")));
      } else {
        callback();
      }
    };
    return {
      showCheckout: true,
      checkValue: true,
      monitorPrometheusData: [],
      // 编辑的对象
      edit_notify_channels: [],
      // 标签
      labelsimidata: [],
      // 注释
      annotationsdata: [],
      sup_this: this,
      btnLoading: false,
      loading: false,
      monitorPrometheusList: [], // prometheus实例id
      alarmGroupList: [], // 报警分组
      alarmMetricList: [], // 报警指标
      alarmMetricInfo: "", // 当前选中报警指标详情
      // 拼接好的expr
      montageexpr: "",
      // 不通知状态
      notificationStatus: true,
      form: {
        name: "",
        description: "",
        alarm_group_id: "",
        alarm_metric_id: "",
        data_source_id: "",
        rule_type: 1,
        expr: "",
        duration_time: 0,
        level: "",
        alert_message: "",
        labels: {},
        annotations: {},
        alert_rule: {
          prom_content: "",
          alert_condition_contents: "",
          alert_condition_params: [],
          data_condition_contents: "",
          data_condition_params: "",
          unit: "",
        },
        notify_rules: {
          repeat_notify: true,
          notify_start_time: "",
          notify_end_time: "",
          notify_objects: [],
        },
      },
      dalert_level_list: [
        {
          value: 1,
          label: this.$t("form.prompt"),
        },
        {
          value: 2,
          label: this.$t("form.serious"),
        },
        {
          value: 3,
          label: this.$t("form.urgent"),
        },
      ],
      labelrules: {
        key: [
          {
            required: true,
            message: this.$t("message.pleaseInputKey"),
            trigger: "blur",
          },
          {
            pattern: /^[^\d]+$/,
            message: this.$t("message.pleaseNotInputNumber"),
            trigger: "blur",
          },
        ],
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputName"),
            trigger: "blur",
          },
        ],
        alert_message: [
          {
            required: true,
            message: this.$t("message.pleaseInputAlertMessage"),
            trigger: "blur",
          },
        ],
        alarm_group_id: [
          {
            required: true,
            message: this.$t("message.pleaseSelectAlarmGroup"),
            trigger: "change",
          },
        ],
        alarm_metric_id: [
          {
            required: true,
            message: this.$t("message.pleaseSelectAlarmMetric"),
            trigger: "change",
          },
        ],
        level: [
          {
            required: true,
            message: this.$t("message.pleaseSelectAlarmLevel"),
            trigger: "change",
          },
        ],
        expr: [
          {
            required: true,
            validator: promQL_custom,
            trigger: "blur",
          },
        ],
        data_source_id: [
          {
            required: true,
            message: this.$t("message.pleaseSelectPrometheusInstance"),
            trigger: "change",
          },
        ],
        duration_time: [
          {
            required: true,
            message: this.$t("message.pleaseInputDurationTime"),
            trigger: "blur",
          },
        ],
        notify_objects: [
          {
            required: true,
            validator: notify_objects_custom,
            trigger: "blur",
          },
        ],
      },
      selectobjectdta: [],
      isAdd: "",
      // 监听规则类型的变化的字段
      isFirstTime: true,
      // 编辑不监听
      isalarm_metric_id: true,
      message_status: false,
      save_alert_message: "",
      alert_message_opt: "",
      alert_message_value: "",
      resource_list: {},
      // 需要调用资源的接口列表
      resource_Interface: [],
    };
  },
  watch: {
    // 监听规则类型的变化
    "form.rule_type": function (newVal, oldVal) {
      if (this.isAdd) {
        this.montageexpr = "";
        // this.form.name = "";
        // this.form.description = "";
        this.form.alarm_group_id = "";
        this.form.alarm_metric_id = "";
        // this.form.data_source_id = "";
        this.form.expr = "";
        this.form.duration_time = 0;
        this.form.level = "";
        this.form.alert_message = "";
        this.form.labels = {};
        this.form.annotations = {};
        (this.form.alert_rule = {
          prom_content: "",
          alert_condition_contents: "",
          alert_condition_params: [],
          data_condition_contents: "",
          data_condition_params: "",
          unit: "",
        }),
          (this.form.notify_rules = {
            repeat_notify: true,
            notify_start_time: "",
            notify_end_time: "",
            notify_objects: [],
          });
        setTimeout(() => {
          this.$refs["ruleForm"].clearValidate();
        });
      } else {
        if (this.isFirstTime) {
          this.isFirstTime = false;
          return;
        }
        this.form.alarm_group_id = "";
        this.form.alarm_metric_id = "";
        this.form.data_source_id = "";
        this.form.expr = "";
        this.montageexpr = "";
        setTimeout(() => {
          this.$refs["ruleForm"].clearValidate();
        });
      }
    },
    // 监听调用资源的接口
    resource_Interface: {
      handler(newValue, oldValue) {
        this.resource_list = {};
        if (newValue.includes("ns_vm_instance")) {
          mainApi.vmsInstabcesList().then((res) => {
            this.resource_list.ns_vm_instance = res.vmInstancesInfo;
          });
        }
        if (newValue.includes("ns_hypervisor_node")) {
          mainApi.vmsHypervisorNodesList().then((res) => {
            this.resource_list.ns_hypervisor_node = res.nodeAllocationInfos;
          });
        }
        if (newValue.includes("edge")) {
          edges().then((res) => {
            this.resource_list.edge = res.nodes;
          });
        }
        if (newValue.includes("container")) {
          getDeploys().then((res) => {
            this.resource_list.container = res.deployments;
          });
        }
        if (newValue.includes("cloud")) {
          getCloudys().then((res) => {
            this.resource_list.cloud = res.clouds;
          });
        }
      },
      deep: true,
    },
  },
  created() {
    // 监听数据的变化拼接expr
    var unwatch = this.$watch(
      "form.alert_message",
      (newVal, oldVal) => {
        if (this.isAdd) {
          setTimeout(() => {
            var spans = document.querySelectorAll(".datapreview > span");
            var result = "";
            spans.forEach(function (span) {
              result += span.innerText;
            });
            this.montageexpr = result;
            unwatch();
          });
        }
      },
      { deep: true }
    );
    this.$watch(
      "form.alarm_metric_id",
      (newVal, oldVal) => {
        if (this.isAdd) {
          setTimeout(() => {
            var spans = document.querySelectorAll(".datapreview > span");
            var result = "";
            spans.forEach(function (span) {
              result += span.innerText;
            });
            this.montageexpr = result;
          }, 1000);
        } else {
          if (this.isalarm_metric_id) {
            this.isalarm_metric_id = false;
            return;
          }
          setTimeout(() => {
            var spans = document.querySelectorAll(".datapreview > span");
            var result = "";
            spans.forEach(function (span) {
              result += span.innerText;
            });
            this.montageexpr = result;
          }, 1000);
        }
      },
      { deep: true }
    );
  },
  mounted() {
    this.init();
    // 判断是编辑还是添加
    if (!this.$route.params.id) {
      this.isAdd = true;
    } else {
      this.isAdd = false;
      this.getdesc();
    }
    setTimeout(() => {
      var spans = document.querySelectorAll(".datapreview > span");
      var result = "";
      spans.forEach(function (span) {
        result += span.innerText;
      });
      this.montageexpr = result;
      this.getMonitorPrometheusDataQuery();
    });
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    validResourceType(type) {
      return [
        "cloud",
        "ns_hypervisor_node",
        "ns_vm_instance",
        "edge",
        "container",
      ].includes(type);
    },
    getResourceList(type, filterDimPrefixField, filterDimSuffixField) {
      const prefix = filterDimPrefixField || "";
      const suffix = filterDimSuffixField || "";
      switch (type) {
        case "cloud":
          if (this.resource_list.cloud) {
            return this.resource_list.cloud.map((item) => ({
              key: item.cloud_id,
              name: item.name,
              value: prefix + item.cloud_id + suffix,
            }));
          }
        case "ns_hypervisor_node":
          return this.resource_list.ns_hypervisor_node.map((item) => ({
            key: item.manageIp,
            name: item.name,
            value: prefix + item.manageIp + suffix,
          }));
        case "ns_vm_instance":
          return this.resource_list.ns_vm_instance.map((item) => ({
            key: item.instanceId,
            name: item.name,
            value: prefix + item.instanceId + suffix,
          }));
        case "edge":
          return this.resource_list.edge.map((item) => ({
            key: item.node_id,
            name: item.node_name,
            value: prefix + item.node_id + suffix,
          }));
        case "container":
          return this.resource_list.container.map((item) => ({
            key: item.instanceId,
            name: item.name,
            value: prefix + item.instanceId + suffix,
          }));
        default:
          return [];
      }
    },
    handleValueChange(value) {
      this.checkValue = value;
      setTimeout(() => {
        this.getMonitorPrometheusDataQuery();
      }, 200);
    },
    async getMonitorPrometheusDataQuery() {
      var spans = document.querySelectorAll(".datapreview-expr > span");
      var result = "";
      spans.forEach(function (span) {
        result += span.innerText;
      });

      if (result != "") {
        const params = { query: result };
        const prometheusDataList = await monitorPrometheusDataQuery(
          this.form.data_source_id,
          params
        );
        prometheusDataList
          ? (this.monitorPrometheusData =
              JSON.parse(prometheusDataList).data.result)
          : (this.monitorPrometheusData = []);
      }
    },
    handleMonitorPrometheusChange(type, value) {
      if (type == "alarm") {
        this.save_alert_message = this.alarmMetricInfo.message;
        this.form.alert_rule.alert_condition_params.forEach((item, index) => {
          if (item.labelName == "opt") {
            this.save_alert_message = this.replaceChar(
              this.save_alert_message,
              "{{$labels.metrics_params_opt_label_value}}",
              item.detail.value
            );
          } else if (item.detail.placeholder == "阈值") {
            this.save_alert_message = this.replaceChar(
              this.save_alert_message,
              "{{$labels.metrics_params_value}}",
              item.detail.value
            );
          }
        });
        this.form.alert_message = this.save_alert_message;
      }
      setTimeout(() => {
        this.getMonitorPrometheusDataQuery();
      }, 200);
    },
    replaceChar(str, oldChar, newChar) {
      if (str.indexOf(oldChar) != -1) {
        str = str.replace(oldChar, newChar);
        return this.replaceChar(str, oldChar, newChar);
      } else {
        return str;
      }
    },
    handlePrometheusChange(value) {
      this.form.data_source_id = value;
    }, // 不通知选择触发
    handleCheckChange(value) {
      if (value) {
        this.form.notify_rules.notify_objects = [];
        this.$refs.addForm.resetForm();
      }
    },
    // 时间修改
    edititem(data) {
      const dateTime = new Date(data);
      const isoString = dateTime.toISOString();
      return isoString;
    },
    // 如果是编辑就获取详情
    async getdesc() {
      await getAlarmsInfo(this.$route.params.id).then((res) => {
        this.form.name = res.name; // 名称
        this.form.description = res.description; // 描述
        this.form.rule_type = res.alert_rule.rule_type; // 规则类型
        this.form.data_source_id = res.data_source_id; // prometheus实例id
        this.form.duration_time = res.duration_time; // 持续时间
        this.annotationsdata = Object.keys(JSON.parse(res.annotations)).map(
          (key) => {
            return { key: key, value: JSON.parse(res.annotations)[key] };
          }
        ); // 这是注释
        this.labelsimidata = Object.keys(JSON.parse(res.labels)).map((key) => {
          return { key: key, value: JSON.parse(res.labels)[key] };
        }); // 这是标签
        setTimeout(() => {
          this.montageexpr = res.expr;
          // this.form.expr = this.parseexpression(res.expr);
        });
        this.form.alarm_group_id = res.alert_group_id; // 报警分组id
        this.getAlarmMetricsList(res.alert_group_id); // 获取报警指标id
        this.form.alarm_metric_id = res.alert_metric_id; // 报警指标id
        setTimeout(() => {
          this.getAlarmMetricsInfo(
            this.form.alarm_group_id,
            this.form.alarm_metric_id
          );
        });
        // 选择指标的时候触发改变自定义promQL语句
        this.form.level = res.level; // 报警等级
        this.form.alert_message = res.alert_message; // 报警内容
        res.alert_condition_params
          ? (this.form.alert_rule.alert_condition_params = JSON.parse(
              res.alert_condition_params
            ))
          : (this.form.alert_rule.alert_condition_params = ""); // 报警条件
        // res.alert_rule.alert_condition_contents
        //   ? (this.form.alert_rule.alert_condition_contents = JSON.parse(
        //       res.alert_rule.alert_condition_contents
        //     ))
        //   : (this.form.alert_rule.alert_condition_contents = "");
        res.alert_rule.data_condition_contents
          ? (this.form.alert_rule.data_condition_contents = JSON.parse(
              res.alert_rule.data_condition_contents
            ))
          : (this.form.alert_rule.data_condition_contents = "");
        res.alert_rule.data_condition_params
          ? (this.form.alert_rule.data_condition_params = JSON.parse(
              res.alert_rule.data_condition_params
            ))
          : (this.form.alert_rule.data_condition_params = "");
        if (this.form.alert_rule.data_condition_params) {
          this.resource_Interface = [];
          this.form.alert_rule.data_condition_params.map((res) => {
            if (res.filterDimResourceType) {
              this.resource_Interface.push(res.filterDimResourceType);
            }
          });
        }
        // 通知对象处理
        if (res.alert_notify_rules) {
          this.notificationStatus = false;
          this.form.notify_rules.notify_start_time = this.edititem(
            res.alert_notify_rules[0].notify_start_time
          ); // 起始时间
          this.form.notify_rules.notify_end_time = this.edititem(
            res.alert_notify_rules[0].notify_end_time
          ); // 结束时间
          this.form.notify_rules.repeat_notify =
            res.alert_notify_rules[0].repeat_notify;
          if (res.alert_notify_rules[0].notify_objects) {
            this.form.notify_rules.notify_objects =
              res.alert_notify_rules[0].notify_objects;
            res.alert_notify_rules[0].notify_objects.map((item, index) => {
              this.form.notify_rules.notify_objects[index].notify_channels_new =
                item.optional_notify_channels.split(",");
              this.form.notify_rules.notify_objects[index].notify_channels =
                item.notify_channels.split(",");
              this.form.notify_rules.notify_objects[index].id =
                item.receiver_id;
              this.form.notify_rules.notify_objects[index].name =
                item.receiver_name;
            });
          }
        } else {
          this.notificationStatus = true;
        }
      });
    },
    // 解析表达式的方法
    parseexpression(sentence) {
      // sentence = sentence.replace(",$", "$");
      sentence = sentence.replace(/}(,)/g, "}");
      const regex = /\${(\w+)}|([^\$]*)/g;
      const matches = sentence.match(regex);
      const result = [];
      if (matches) {
        matches.forEach((match) => {
          if (match.startsWith("${")) {
            // 匹配到变量
            result.push({
              type: "var",
              value: match.substring(2, match.length - 1),
            });
          } else {
            if (match.trim() != "") {
              // 匹配到文字
              result.push({ type: "text", value: match.trim() });
            }
          }
        });
      }
      return result;
    },
    // 解析报警条件的方法
    parseCondition(sentence) {
      const regex = /\${(\w+)}|([^\${}]*)/g;
      const matches = sentence.match(regex);
      const result = [];
      if (matches) {
        matches.forEach((match) => {
          if (match.startsWith("${")) {
            // 匹配到变量
            result.push({
              type: "var",
              value: match.substring(2, match.length - 1),
            });
          } else {
            // 匹配到文字
            result.push({ type: "text", value: match });
          }
        });
      }
      return result;
    },
    // 添加标签
    pushlabels() {
      this.labelsimidata.push({
        key: "",
        value: "",
      });
    },
    // 删除标签
    deleteannotations(index) {
      this.annotationsdata.splice(index, 1);
    },
    // 添加注释
    pushannotations() {
      this.annotationsdata.push({
        key: "",
        value: "",
      });
    },
    // 删除注释
    deletelabels(index) {
      this.labelsimidata.splice(index, 1);
    },
    // 获取子组件选择用户的信息
    handleChildMsg(data) {
      if (this.isAdd) {
        data.map((res, index) => {
          const notify_type_data = res.notify_type.split(",");
          data[index].notify_type = notify_type_data;
          data[index].notify_type_new = notify_type_data;
        });
        this.form.notify_rules.notify_objects = data;
      } else {
        // this.isAdd = true;
        data.map((res, index) => {
          const notify_type_data = res.notify_type.split(",");
          data[index].notify_channels = notify_type_data;
          data[index].notify_channels_new = notify_type_data;
        });
        this.form.notify_rules.notify_objects = data;
      }
    },
    // 打开添加通知对象的对话框
    openobjectframe() {
      if (this.isAdd) {
        this.activeInfo = {};
        this.$refs.addForm.id = "";
        this.$refs.addForm.isAdd = true;
        this.$refs.addForm.dialog = true;
      } else {
        this.activeInfo = {};
        this.$refs.addForm.id = "";
        this.$refs.addForm.isAdd = false;
        this.$refs.addForm.dialog = true;
        this.$refs.addForm.multipleSelection =
          this.form.notify_rules.notify_objects;
        this.$refs.addForm.defaultSelection =
          this.form.notify_rules.notify_objects;
        this.$refs.addForm.selectedit(this.form.notify_rules.notify_objects);
      }
    },
    async init() {
      const params = {
        type: 0,
      };
      const alarmGroupList = await getAlarmGroups();
      const monitorPrometheusList = await getMonitorPrometheus(params);
      monitorPrometheusList.prometheus_instances
        ? (this.monitorPrometheusList =
            monitorPrometheusList.prometheus_instances)
        : [];
      this.monitorPrometheusList && this.monitorPrometheusList.length > 0
        ? (this.form.data_source_id = this.monitorPrometheusList[0].id)
        : "";
      this.alarmGroupList = alarmGroupList.groups;
    },
    getAlarmMetricsList(id) {
      // 分组改变 获取报警指标
      this.form.alarm_metric_id = "";
      getAlarmMetrics({ group_id: id }).then((res) => {
        this.alarmMetricList = res.metrics;
      });
    },
    getAlarmMetricsInfo(group_id, id) {
      if (this.isAdd) {
        getAlarmMetrics({ group_id: group_id }).then((res) => {
          this.alarmMetricList = res.metrics;
          this.alarmMetricInfo = this.alarmMetricList.filter(
            (item) => item.id == id
          )[0];
          this.form.alert_rule.alert_condition_contents = this.parseCondition(
            this.alarmMetricInfo.alert_condition_display_statement
          );
          this.form.expr = this.parseexpression(
            this.alarmMetricInfo.prom_content
          );
          if (this.alarmMetricInfo.alert_condition_param) {
            this.form.alert_rule.alert_condition_params = JSON.parse(
              this.alarmMetricInfo.alert_condition_param
            );
          } else {
            this.form.alert_rule.alert_condition_params = [];
          }
          if (this.alarmMetricInfo.data_condition_label) {
            this.form.alert_rule.data_condition_contents = JSON.parse(
              this.alarmMetricInfo.data_condition_label
            );
          } else {
            this.form.alert_rule.data_condition_contents = "";
          }
          if (this.alarmMetricInfo.data_filters) {
            this.form.alert_rule.data_condition_params = JSON.parse(
              this.alarmMetricInfo.data_filters
            );
            this.resource_Interface = [];
            this.form.alert_rule.data_condition_params.map((res) => {
              if (res.filterDimResourceType) {
                this.resource_Interface.push(res.filterDimResourceType);
              }
            });
          } else {
            this.form.alert_rule.data_condition_params = "";
          }
          this.form.duration_time = this.alarmMetricInfo.duration_time;
          if (this.alarmMetricInfo.prom_content.indexOf("opt") != -1) {
            (this.showCheckout = true), (this.checkValue = true);
          } else {
            this.showCheckout = false;
            this.checkValue = false;
          }
          this.form.alert_message = this.alarmMetricInfo.message;
          this.form.alert_rule.alert_condition_params.forEach((item, index) => {
            if (item.labelName == "opt") {
              this.form.alert_message = this.replaceChar(
                this.form.alert_message,
                "{{$labels.metrics_params_opt_label_value}}",
                item.detail.value
              );
            } else if (item.detail.placeholder == "阈值") {
              this.form.alert_message = this.replaceChar(
                this.form.alert_message,
                "{{$labels.metrics_params_value}}",
                item.detail.value
              );
            }
          });
          this.form.level = this.alarmMetricInfo.level;
          if (this.form.rule_type == 1) {
            setTimeout(() => {
              this.getMonitorPrometheusDataQuery();
            }, 200);
          }
        });
      } else {
        getAlarmMetrics({ group_id: group_id }).then((res) => {
          this.alarmMetricList = res.metrics;
          this.alarmMetricInfo = this.alarmMetricList.filter(
            (item) => item.id == id
          )[0];
          this.form.alert_rule.alert_condition_contents = this.parseCondition(
            this.alarmMetricInfo.alert_condition_display_statement
          );
          this.form.expr = this.parseexpression(
            this.alarmMetricInfo.prom_content
          );
          if (this.message_status) {
            this.form.alert_message = this.alarmMetricInfo.message;
            if (this.alarmMetricInfo.data_filters) {
              this.form.alert_rule.data_condition_params = JSON.parse(
                this.alarmMetricInfo.data_filters
              );
              this.resource_Interface = [];
              this.form.alert_rule.data_condition_params.map((res) => {
                if (res.filterDimResourceType) {
                  this.resource_Interface.push(res.filterDimResourceType);
                }
              });
            } else {
              this.form.alert_rule.data_condition_params = "";
            }
          }
          if (this.alarmMetricInfo.prom_content.indexOf("opt") != -1) {
            (this.showCheckout = true), (this.checkValue = true);
          } else {
            this.showCheckout = false;
            this.checkValue = false;
          }

          // if (this.alarmMetricInfo.data_filters) {
          //   this.form.alert_rule.data_condition_params = JSON.parse(
          //     this.alarmMetricInfo.data_filters
          //   );
          //   this.resource_Interface = [];
          //   this.form.alert_rule.data_condition_params.map((res) => {
          //     if (res.filterDimResourceType) {
          //       this.resource_Interface.push(res.filterDimResourceType);
          //     }
          //   });
          // } else {
          //   this.form.alert_rule.data_condition_params = "";
          // }
          this.form.alert_rule.alert_condition_params.forEach((item, index) => {
            if (item.labelName == "opt") {
              this.form.alert_message = this.replaceChar(
                this.form.alert_message,
                "{{$labels.metrics_params_opt_label_value}}",
                item.detail.value
              );
            } else if (item.detail.placeholder == "阈值") {
              this.form.alert_message = this.replaceChar(
                this.form.alert_message,
                "{{$labels.metrics_params_value}}",
                item.detail.value
              );
            }
          });
          // this.form.expr = this.parseexpression(
          //   this.alarmMetricInfo.prom_content
          // );
          this.form.level = this.alarmMetricInfo.level;
          this.message_status = true;
          if (this.form.rule_type == 1) {
            setTimeout(() => {
              this.getMonitorPrometheusDataQuery();
            }, 200);
          }
        });
      }
    },
    // 报警器的添加和编辑
    submitForm(formName) {
      this.$refs[formName].validate(async (valid) => {
        if (valid) {
          this.btnLoading = true;
          // 对报警规则进行处理
          var detailMain = JSON.parse(JSON.stringify(this.form));
          // 对注释和标签进行处理
          const objlabels = this.labelsimidata.reduce((acc, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {});
          const objannotations = this.annotationsdata.reduce((acc, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {});
          detailMain.labels = objlabels;
          detailMain.annotations = objannotations;
          if (detailMain.rule_type != 1) {
            detailMain.alert_rule.prom_content = this.montageexpr;
            detailMain.expr = this.montageexpr;
          } else {
            var spans = document.querySelectorAll(".datapreview > span");
            var result = "";
            spans.forEach(function (span) {
              result += span.innerText;
            });
            this.montageexpr = result;
            detailMain.alert_rule.prom_content = result;
            detailMain.expr = result;
          }
          if (this.isAdd) {
            const new_notify_objects = [];
            detailMain.notify_rules.notify_objects.map((res, index) => {
              detailMain.notify_rules.notify_objects[index].notify_type =
                res.notify_type.join(",");
              new_notify_objects.push({
                receiver_id: detailMain.notify_rules.notify_objects[index].id,
                notify_channels:
                  detailMain.notify_rules.notify_objects[index].notify_type,
              });
            });
            detailMain.notify_rules.notify_objects = new_notify_objects;
          } else {
            const new_notify_objects = [];
            detailMain.notify_rules.notify_objects.map((res, index) => {
              detailMain.notify_rules.notify_objects[index].notify_type =
                res.notify_channels.join(",");
              new_notify_objects.push({
                receiver_id: detailMain.notify_rules.notify_objects[index].id,
                notify_channels:
                  detailMain.notify_rules.notify_objects[index].notify_type,
              });
            });
            detailMain.notify_rules.notify_objects = new_notify_objects;
          }
          var data = {
            name: detailMain.name,
            level: detailMain.level,
            expr: detailMain.expr,
            alarm_group_id: detailMain.alarm_group_id,
            alarm_metric_id: detailMain.alarm_metric_id,
            alert_message: detailMain.alert_message,
            labels: detailMain.labels,
            annotations: detailMain.annotations,
            data_source_id: detailMain.data_source_id,
            duration_time: detailMain.duration_time,
            description: detailMain.description,
            alert_rule: {
              alert_condition_contents: JSON.stringify(
                detailMain.alert_rule.alert_condition_contents
              ),
              alert_condition_params: JSON.stringify(
                detailMain.alert_rule.alert_condition_params
              ),
              data_condition_contents: JSON.stringify(
                detailMain.alert_rule.data_condition_contents
              ),
              data_condition_params: JSON.stringify(
                detailMain.alert_rule.data_condition_params
              ),
              prom_content: detailMain.alert_rule.prom_content,
              unit: detailMain.alert_rule.unit,
            },
            notify_rules: {
              notify_end_time: detailMain.notify_rules.notify_end_time,
              notify_start_time: detailMain.notify_rules.notify_start_time,
              repeat_notify: detailMain.notify_rules.repeat_notify,
              notify_objects: detailMain.notify_rules.notify_objects,
            },
            rule_type: detailMain.rule_type,
          };
          this.notificationStatus ? delete data.notify_rules : "";

          if (this.isAdd) {
            addAlarms(data)
              .then((res) => {
                this.btnLoading = false;
                this.$notify({
                  title: this.$t("message.addSuccess"),
                  type: "success",
                  duration: 2500,
                });
                this.$router.push({ path: "/alarmmanagement" });
              })
              .catch((err) => {
                this.btnLoading = false;
              });
          } else {
            editAlarms(this.$route.params.id, data)
              .then((res) => {
                this.btnLoading = false;
                this.$notify({
                  title: this.$t("message.editSuccess"),
                  type: "success",
                  duration: 2500,
                });
                this.$router.push({ path: "/alarmmanagement" });
              })
              .catch((err) => {
                this.btnLoading = false;
              });
          }
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
.prompt {
  font-size: 13px;
  color: #cecece;
}

::v-deep .my-label {
  background: rgb(239, 243, 249);
}

::v-deep .my-content {
  background: rgb(239, 243, 249);
}

.table-container {
  border: 1px dashed #999;
  /* 添加虚线边框样式 */
}

.memory_outside {
  background: rgb(248, 248, 249);
  padding: 5px;
  height: 100%;
}

.memory_outsides {
  background: rgb(248, 248, 249);
  padding: 5px;
  height: 45px;
}

.memory {
  float: left;
}

.el-form-item__content {
  background: rgb(248, 248, 249);
}

.ntificationobject {
  background: rgb(248, 248, 249);
}

.w-addAlarm {
  width: 450px;
}

.w-addAlarm2 {
  width: 600px;
}
</style>
