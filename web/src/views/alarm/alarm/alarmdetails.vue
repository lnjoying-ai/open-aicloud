<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <el-form
        ref="form"
        :model="detailMain"
        size="small"
        label-position="right"
        style="display: flex; flex-wrap: wrap"
        label-width="120px"
      >
        <el-form-item :label="$t('form.name') + ':'" style="width: 30%">
          <span class="detaillong">{{
            detailMain.name ? detailMain.name : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.alarmLevel') + ':'" style="width: 30%">
          <span v-if="detailMain.level == 1">
            <el-tag size="small">{{ $t("form.prompt") }}</el-tag>
          </span>
          <span v-if="detailMain.level == 2">
            <el-tag type="warning" size="small">{{
              $t("form.serious")
            }}</el-tag>
          </span>
          <span v-if="detailMain.level == 3">
            <el-tag type="danger" size="small">{{ $t("form.urgent") }}</el-tag>
          </span>
        </el-form-item>
        <el-form-item :label="$t('form.durationTime') + ':'" style="width: 30%">
          <span
            >{{ detailMain.duration_time ? detailMain.duration_time : 0 }}
            {{ $t("form.minutes") }}</span
          >
        </el-form-item>
        <el-form-item :label="$t('form.alarmGroup') + ':'" style="width: 30%">
          <span>{{
            detailMain.alert_group_name ? detailMain.alert_group_name : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.alarmMetric') + ':'" style="width: 30%">
          <span>{{
            detailMain.alert_metric_name ? detailMain.alert_metric_name : "-"
          }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.prometheusInstanceId') + ':'"
          label-width="140px"
          style="width: 30%"
        >
          <span>{{
            detailMain.data_source_id ? detailMain.data_source_id : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.ruleType') + ':'" style="width: 30%">
          <span v-if="detailMain.rule_type == 1">{{
            $t("form.staticThreshold")
          }}</span>
          <span v-if="detailMain.rule_type == 2">{{
            $t("form.customPromQL")
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.targetType') + ':'" style="width: 30%">
          {{
            detailMain.target_resource_type
              ? detailMain.target_resource_type
              : "-"
          }}
        </el-form-item>

        <el-form-item :label="$t('form.label') + ':'" style="width: 100%">
          <span v-if="Object.keys(detailMain.labels).length != 0">
            <span v-for="(value, key) in detailMain.labels" :key="key">
              <el-tag class="mb-1 mr-1" size="mini"
                >{{ key }} : {{ value }}</el-tag
              >
            </span>
          </span>
          <span v-else> - </span>
        </el-form-item>
        <el-form-item :label="$t('form.annotation') + ':'" style="width: 100%">
          <span v-if="Object.keys(detailMain.annotations).length != 0">
            <span v-for="(value, key) in detailMain.annotations" :key="key">
              <el-tag class="mb-1 mr-1" size="mini"
                >{{ key }} : {{ value }}</el-tag
              >
            </span>
          </span>
          <span v-else> - </span>
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" style="width: 100%">
          <span>{{
            detailMain.description ? detailMain.description : "-"
          }}</span>
        </el-form-item>
        <el-form-item :label="$t('form.ruleInfo') + ':'" style="width: 100%">
          <span> {{ detailMain.expr ? detailMain.expr : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.alarmContent') + ':'"
          style="width: 100%"
        >
          <span>{{
            detailMain.alert_message ? detailMain.alert_message : "-"
          }}</span>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="mb-2">
      <el-form
        ref="form"
        :model="detailMain"
        size="small"
        label-position="left"
        label-width="8  0px"
      >
        <el-tabs v-model="activeName">
          <el-tab-pane :label="$t('form.detail')" name="first">
            <el-card
              class="box-card w-1/2 max-w-xl"
              :body-style="{ padding: '0px 20px 0px 20px' }"
            >
              <div v-if="detailMain.alert_notify_rules">
                <el-form-item
                  :label="$t('form.notifyRule') + ':'"
                  label-width="120px"
                  class="mb-1 font-bold"
                />
                <el-form-item
                  :label="$t('form.repeatNotify') + ':'"
                  class="mb-0"
                >
                  <span class="break-all">{{
                    detailMain.alert_notify_rules[0].repeat_notify
                      ? $t("form.yes")
                      : $t("form.no")
                  }}</span>
                </el-form-item>
                <el-form-item
                  :label="$t('form.startNotifyTime') + ':'"
                  class="mb-0"
                >
                  <span class="break-all inline-block leading-normal">{{
                    detailMain.alert_notify_rules[0].notify_start_time
                      ? detailMain.alert_notify_rules[0].notify_start_time
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item
                  :label="$t('form.endNotifyTime') + ':'"
                  class="mb-0"
                >
                  <span class="break-all">{{
                    detailMain.alert_notify_rules[0].notify_end_time
                      ? detailMain.alert_notify_rules[0].notify_end_time
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item
                  :label="$t('form.notifyObject') + ':'"
                  class="mb-0"
                >
                  <div style="display: flex">
                    <el-table
                      :data="detailMain.alert_notify_rules[0].notify_objects"
                      stripe
                      max-height="300"
                      size="mini"
                    >
                      <el-table-column :label="$t('form.notifyObject')">
                        <template slot-scope="scope">
                          {{ scope.row.receiver_name }}
                        </template>
                      </el-table-column>
                      <el-table-column :label="$t('form.email')">
                        <template slot-scope="scope">
                          <!-- 是否包含字符串 0 -->
                          {{
                            scope.row.notify_channels.includes("0")
                              ? $t("form.yes")
                              : $t("form.no")
                          }}
                        </template>
                      </el-table-column>
                      <el-table-column :label="$t('form.phone')">
                        <template slot-scope="scope">
                          {{
                            scope.row.notify_channels.includes("1")
                              ? $t("form.yes")
                              : $t("form.no")
                          }}
                        </template>
                      </el-table-column>
                    </el-table>
                  </div>
                </el-form-item>
                <!-- <el-form-item label="接收者id:" class="mb-0">
                  <span class="break-all">{{
                    detailMain.alert_notify_rules[0].notify_objects.receiver_id
                      ? detailMain.alert_notify_rules[0].notify_objects.receiver_id
                      : "-"
                  }}</span>
                </el-form-item>
                <el-form-item label="通知方式:" class="mb-0">
                  <span class="break-all">
                    <span
                      v-if="
                        detailMain.alert_notify_rules[0].notify_objects
                          .notify_channels == 0
                      "
                      >邮箱</span
                    >
                    <span
                      v-if="
                        detailMain.alert_notify_rules[0].notify_objects
                          .notify_channels == 1
                      "
                      >手机号</span
                    >
                  </span>
                </el-form-item> -->
              </div>
              <div v-else>
                <el-form-item
                  :label="$t('form.notifyRule') + ':'"
                  label-width="120px"
                  class="mb-1 font-bold"
                />

                <el-empty
                  :description="$t('form.noNotifyRule')"
                  :image-size="60"
                />
              </div>
            </el-card>
          </el-tab-pane>
        </el-tabs>
      </el-form>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import YamlEditor from "@/components/yaml/editor.vue";
import { getAlarmsInfo } from "@/api/mainApi";
export default {
  components: { YamlEditor },
  data() {
    return {
      activeName: "first",
      detailMain: {},
    };
  },

  async created() {},
  mounted() {
    this.init();
  },
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    async init() {
      const list = await getAlarmsInfo(this.$route.params.id);
      this.detailMain = list;
      this.detailMain.labels = JSON.parse(list.labels);
      this.detailMain.annotations = JSON.parse(list.annotations);
    },
  },
};
</script>
<style lang="scss" scoped></style>
