<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <div
        slot="header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <div>
          <div>{{ $t("form.basicInfo") }}</div>
        </div>
      </div>
      <el-form
        ref="form"
        :model="detailMain"
        :rules="rules"
        size="small"
        label-width="130px"
      >
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          {{ detailMain.name }}
        </el-form-item>
        <el-form-item :label="$t('form.type') + ':'">
          <span v-if="detailMain.type === 0">server</span>
          <span v-if="detailMain.type === 1">agent</span>
        </el-form-item>
        <el-form-item :label="$t('form.status') + ':'">
          <div v-if="detailMain.status == '1'">
            <span
              ><el-tag size="small" type="success">{{
                $t("form.running")
              }}</el-tag></span
            >
          </div>
          <div v-if="detailMain.status == '2'">
            <span
              ><el-tag>{{ $t("form.starting") }}</el-tag></span
            >
          </div>
          <div v-if="detailMain.status == '3'">
            <span
              ><el-tag size="small" type="danger">{{
                $t("form.error")
              }}</el-tag></span
            >
          </div>
          <div v-if="detailMain.status == '4'">
            <span
              ><el-tag size="small" type="danger">{{
                $t("form.down")
              }}</el-tag></span
            >
          </div>
          <div v-if="detailMain.status == '5'">
            <span
              ><el-tag>{{ $t("form.updating") }}</el-tag></span
            >
          </div>
          <div v-if="detailMain.status == '6'">
            <span
              ><el-tag type="warning">{{ $t("form.pause") }}</el-tag></span
            >
          </div>
          <div v-if="detailMain.status == '7'">
            <span
              ><el-tag type="info">{{ $t("form.unknown") }}</el-tag></span
            >
          </div>
        </el-form-item>
        <el-form-item
          v-if="this.detailMain.type == 0"
          :label="$t('form.authInfo') + ':'"
        >
          <p>
            <span>{{ $t("form.userName") }}: </span>
            <span>{{
              detailMain.basic_auth.username
                ? detailMain.basic_auth.username
                : "-"
            }}</span>
          </p>
          <p>
            <span>{{ $t("form.password") }}: </span>
            <span>{{
              getBasicAuthPassword(detailMain.basic_auth.password)
            }}</span>
            <span
              class="cursor-pointer ml-2"
              @click="basicAuthPasswordStatus = !basicAuthPasswordStatus"
              ><i v-if="!basicAuthPasswordStatus" class="icon-browse" /><i
                v-if="basicAuthPasswordStatus"
                class="icon-eye-close"
            /></span>
          </p>
        </el-form-item>

        <el-form-item
          v-if="this.detailMain.type == 0"
          :label="$t('form.remoteReadAddress') + ':'"
        >
          <el-table
            :data="readUrlData"
            style="width: 100%; border-radius: 4px"
            stripe
            size="small"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column :label="$t('form.network')" width="160">
              <template slot-scope="scope">
                {{ scope.row.network ? scope.row.network : "-" }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.url')">
              <template slot-scope="scope">
                {{ scope.row.url ? scope.row.url : "-" }}
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>

        <el-form-item
          v-if="this.detailMain.type == 0"
          :label="$t('form.remoteWriteAddress') + ':'"
        >
          <el-table
            :data="writeUrlData"
            style="width: 100%; border-radius: 4px"
            stripe
            size="small"
            :default-sort="{ prop: 'date', order: 'descending' }"
          >
            <el-table-column :label="$t('form.network')" width="160">
              <template slot-scope="scope">
                {{ scope.row.network ? scope.row.network : "-" }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('form.url')">
              <template slot-scope="scope">
                {{ scope.row.url ? scope.row.url : "-" }}
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.configuration") + ":" }}</div>
          <div
            style="
              width: 100%;
              line-height: 1.2;
              max-height: 380px;
              overflow: auto;
            "
          >
            <yaml-editor
              ref="yamlEditor"
              v-model="detailMain.config"
              :download-name="detailMain.name"
              :placeholder="''"
              :is-add="false"
              :lint="detailMain.type == 'yaml'"
            />
          </div>
        </el-form-item>
        <div class="mt-2" style="text-align: right">
          <el-button
            type="primary"
            size="small"
            :loading="loadingForm"
            @click="doEdit()"
            >{{ $t("form.save") }}</el-button
          >
        </div>
      </el-form>
    </el-card>
    <el-card class="mb-2">
      <div
        slot="header"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <div>
          <div>{{ $t("form.scrapeTarget") }}</div>
        </div>
        <el-button
          type="primary"
          size="small"
          style="float: right"
          @click="getMonitorJobs()"
          >{{ $t("form.refresh") }}</el-button
        >
      </div>
      <el-row :gutter="40">
        <el-col :span="24" style="clear: both">
          <el-table
            ref="multipleTable"
            :data="tableData"
            style="width: 100%; border-radius: 4px"
            stripe
            :row-key="getRowKeys"
            :default-sort="{ prop: 'date', order: 'descending' }"
            max-height="600"
          >
            <el-table-column prop="scrapeUrl" :label="$t('form.endpoints')">
              <template slot-scope="scope">
                {{ scope.row.scrapeUrl ? scope.row.scrapeUrl : "-" }}
              </template>
            </el-table-column>
            <el-table-column prop="health" :label="$t('form.status')">
              <template slot-scope="scope">
                <div v-if="scope.row.health == 'up'">
                  <el-tag type="success">{{
                    scope.row.health ? scope.row.health : "-"
                  }}</el-tag>
                </div>
                <div v-if="scope.row.health == 'down'">
                  <el-tag type="danger"
                    >{{ scope.row.health ? scope.row.health : "-" }}
                  </el-tag>
                </div>
              </template>
            </el-table-column>
            <el-table-column
              prop="labels"
              :label="$t('form.label')"
              min-width="180"
            >
              <template slot-scope="scope">
                <span
                  v-for="(value, key, index) in scope.row.labels"
                  :key="index"
                >
                  <el-tag class="ml-2 mt-1.5"
                    >{{ key + ":" }}{{ value }}</el-tag
                  >
                </span>
              </template>
            </el-table-column>
            <el-table-column
              prop="lastScrape"
              :label="$t('form.latestScrapeTime')"
            >
              <template slot-scope="scope">
                {{
                  scope.row.lastScrape ? formatDate(scope.row.lastScrape) : "-"
                }}
                <!-- {{ scope.row.lastScrape ?(new Date().getTime() - new Date(scope.row.lastScrape).getTime() )+"s" +" "+"ago" : "-" }} -->
              </template>
            </el-table-column>
            <el-table-column
              prop="lastScrapeDuration"
              :label="$t('form.latestScrapeDuration')"
            >
              <template slot-scope="scope">
                {{
                  scope.row.lastScrapeDuration
                    ? scope.row.lastScrapeDuration.toFixed(3) + "s"
                    : "-"
                }}
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import YamlEditor from "@/components/yaml/editor.vue";
import {
  getMonitorPrometheusDetail,
  monitorPrometheusUpdate,
  getMonitorJobs,
} from "@/api/mainApi";
export default {
  components: { YamlEditor },
  data() {
    return {
      loadingForm: false,
      detailMain: {},
      rules: {},
      tableData: [],
      readUrlData: [],
      writeUrlData: [],
      basicAuthPasswordStatus: false, // 是否显示密码
    };
  },

  async created() {
    this.init();
  },
  mounted() {},
  computed: {
    ...mapGetters(["userInfo"]),
  },
  methods: {
    getBasicAuthPassword(password) {
      if (!this.basicAuthPasswordStatus) {
        return password.replace(/./g, "*");
      } else {
        return password;
      }
    },
    getRowKeys(row) {
      return row.id;
    },
    // 封装的时间戳转换日期函数
    // 时间戳转换时间
    formatDate(date) {
      var date = new Date(date);
      var YY = date.getFullYear() + "-";
      var MM =
        (date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1) + "-";
      var DD = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      var hh =
        (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
      var mm =
        (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
        ":";
      var ss =
        date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();
      return YY + MM + DD + " " + hh + mm + ss;
    },
    async init() {
      const list = await getMonitorPrometheusDetail(this.$route.params.id);
      this.detailMain = list;
      this.getMonitorJobs();
      if (this.detailMain.type == 0) {
        this.readUrlData.push(
          {
            network: this.$t("form.internalNetwork"),
            url: this.detailMain.internal_remote_read_url,
          },
          {
            network: this.$t("form.externalNetwork"),
            url: this.detailMain.remote_read_url,
          }
        );
        this.writeUrlData.push(
          {
            network: this.$t("form.internalNetwork"),
            url: this.detailMain.internal_remote_write_url,
          },
          {
            network: this.$t("form.externalNetwork"),
            url: this.detailMain.remote_write_url,
          }
        );
      }
    },
    // 采集目标列表
    async getMonitorJobs() {
      const list = await getMonitorJobs(this.$route.params.id);
      this.tableData = list.activeTargets;
    },

    doEdit() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          var detailMain = JSON.parse(JSON.stringify(this.detailMain));
          var data = {
            config: detailMain.config,
          };
          this.loadingForm = true;
          monitorPrometheusUpdate(this.$route.params.id, data)
            .then((res) => {
              this.loadingForm = false;
              this.init();
              this.$notify({
                title: this.$t("message.saveSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$router.push("/component/prometheus");
            })
            .catch((err) => {
              this.loadingForm = false;
              console.error(err.response.data.message);
            });
        } else {
          return false;
        }
      });
    },
    cancel() {
      this.$router.push({
        path: "/component/prometheus",
      });
    },
  },
};
</script>
<style scoped>
::v-deep .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 14px;
}
</style>
