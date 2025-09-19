<template>
  <div class="mt-2">
    <el-card class="mb-2" shadow="never">
      <!-- <div slot="header"
           style="display: flex;justify-content: space-between;align-items: center;">
        <div>
          <div>基本信息</div>
        </div>
      </div> -->
      <el-form
        ref="form"
        :model="detailMain"
        :rules="mainRules"
        size="small"
        label-width="140px"
      >
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.name') + ':'"
          prop="name"
        >
          <el-input
            v-model="detailMain.name"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <!-- <el-form-item label="名称"
                      style="width: 50%; float: left">
          <span>{{ detailMain.name ? detailMain.name : "-" }}</span>
        </el-form-item> -->
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.description') + ':'"
          prop="description"
        >
          <el-input
            v-model="detailMain.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            :autosize="{ minRows: 2, maxRows: 4 }"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
        <el-form-item
          v-if="userInfo.kind == 0 || userInfo.kind == 1"
          :label="$t('form.purpose') + ':'"
        >
          <div>
            <el-checkbox-group v-model="checkList" size="small">
              <el-checkbox label="inner_monitor" value="inner_monitor">{{
                $t("form.resourceMonitoring")
              }}</el-checkbox>
              <el-checkbox label="service_user" value="service_user">{{
                $t("form.userConfiguration")
              }}</el-checkbox>
            </el-checkbox-group>
          </div>
        </el-form-item>
        <!-- <el-form-item label="描述"
                      style="width: 50%; float: left">
          <span>{{ detailMain.description ? detailMain.description : "-" }}</span>
        </el-form-item> -->
        <el-form-item
          :label="$t('form.endpoint') + ':'"
          style="width: 50%; float: left"
        >
          <span>{{ detailMain.endpoint ? detailMain.endpoint : "-" }}</span>
        </el-form-item>
        <el-form-item
          :label="$t('form.status') + ':'"
          style="width: 50%; float: left"
        >
          <span v-if="detailMain.status.code == 0" style="color: #67c23a">
            {{ $t("form.online") }}
          </span>
          <span v-if="detailMain.status.code == 1" style="color: #e6a23c">
            {{ $t("form.offline") }}
          </span>
          <span v-if="detailMain.status.code == 99" style="color: #f56c6c">
            {{ $t("statusAndType.deleted") }}
          </span>
        </el-form-item>
        <el-form-item
          :label="$t('form.createTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>
            {{ detailMain.create_time ? detailMain.create_time : "-" }}</span
          >
        </el-form-item>
        <el-form-item
          :label="$t('form.updateTime') + ':'"
          style="width: 50%; float: left"
        >
          <span>
            {{ detailMain.update_time ? detailMain.update_time : "-" }}</span
          >
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never">
      <div slot="header">
        <div>{{ $t("form.gatewayAvailableIpAndPortManagement") }}</div>
      </div>
      <el-row :gutter="40">
        <el-col :span="24" style="clear: both">
          <el-form
            ref="formRef"
            :model="form"
            :rules="rules"
            label-width="0px"
            size="mini"
          >
            <el-table
              ref="multipleTable"
              :data="form.tableData"
              style="width: 100%; border-radius: 4px"
              stripe
              :row-key="getRowKeys"
              :default-sort="{ prop: 'date', order: 'descending' }"
              max-height="600"
            >
              <el-table-column
                type="index"
                :label="$t('form.index')"
                width="80"
              />
              <el-table-column :label="$t('form.address')" min-width="250">
                <template slot-scope="scope">
                  <div>
                    <span class="titleAddress"
                      >{{ $t("form.external") }}：</span
                    >
                    <span v-if="scope.row.port_range_id">{{
                      scope.row.external_ip ? scope.row.external_ip : "-"
                    }}</span>
                    <span v-else>
                      <el-form-item
                        :prop="'tableData.' + scope.$index + '.external_ip'"
                        :rules="rules.external_ip"
                        style="display: inline-block"
                      >
                        <el-input
                          v-model="scope.row.external_ip"
                          size="mini"
                          :placeholder="$t('form.pleaseInputIpOrDomain')"
                          style="width: 180px"
                        />
                      </el-form-item>
                    </span>
                  </div>
                  <div class="mt-1">
                    <span class="titleAddress"
                      >{{ $t("form.internal") }}：</span
                    >
                    <span v-if="scope.row.port_range_id">{{
                      scope.row.internal_ip ? scope.row.internal_ip : "-"
                    }}</span>
                    <span v-else>
                      <el-form-item
                        :prop="'tableData.' + scope.$index + '.internal_ip'"
                        :rules="rules.internal_ip"
                        style="display: inline-block"
                      >
                        <el-input
                          v-model="scope.row.internal_ip"
                          size="mini"
                          :placeholder="$t('form.pleaseInputIpOrDomain')"
                          style="width: 180px"
                        />
                      </el-form-item>
                    </span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.portRange')" min-width="500">
                <template slot-scope="scope">
                  <div>
                    <div class="">
                      <div class="mr-14">
                        <span class="title">{{ $t("form.start") }}：</span>
                        <span
                          v-if="scope.row.port_range_id"
                          class="titleValue"
                          >{{
                            scope.row.port_range_min
                              ? scope.row.port_range_min
                              : "-"
                          }}</span
                        >
                        <span v-else class="title">
                          <el-form-item
                            :prop="
                              'tableData.' + scope.$index + '.port_range_min'
                            "
                            :rules="rules.port_range_min"
                            style="width: 120px"
                          >
                            <el-input
                              v-model="scope.row.port_range_min"
                              size="mini"
                              style="width: 120px"
                              :placeholder="$t('form.startPort')"
                              @change="
                                handlePortRangMinChange(scope.$index, scope.row)
                              "
                            />
                          </el-form-item>
                        </span>
                      </div>
                      <div class=" ">
                        <span class="title">{{ $t("form.end") }}：</span>
                        <span
                          v-if="scope.row.port_range_id"
                          class="titleValue"
                          >{{
                            scope.row.port_range_max
                              ? scope.row.port_range_max
                              : "-"
                          }}</span
                        >
                        <span v-else class="title">
                          <el-form-item
                            :prop="
                              'tableData.' + scope.$index + '.port_range_max'
                            "
                            :rules="rules.port_range_max"
                            style="width: 120px"
                          >
                            <el-input
                              v-model="scope.row.port_range_max"
                              size="mini"
                              style="width: 120px"
                              :placeholder="$t('form.endPort')"
                            />
                          </el-form-item>
                        </span>
                      </div>
                    </div>
                    <div class="mt-1">
                      <div class="mr-14">
                        <span class="title"
                          >{{ $t("form.listenStart") }}：</span
                        >
                        <span
                          v-if="scope.row.port_range_id"
                          class="titleValue"
                          >{{
                            scope.row.listen_port_range_min
                              ? scope.row.listen_port_range_min
                              : "-"
                          }}</span
                        >
                        <span v-else class="title">
                          <el-form-item
                            style="width: 120px"
                            :prop="
                              'tableData.' +
                              scope.$index +
                              '.listen_port_range_min'
                            "
                            :rules="rules.listen_port_range_min"
                          >
                            <el-input
                              v-model="scope.row.listen_port_range_min"
                              size="mini"
                              style="width: 120px"
                              :placeholder="$t('form.listenStartPort')"
                              @change="
                                handleListenPortRangMinChange(
                                  scope.$index,
                                  scope.row
                                )
                              "
                            />
                          </el-form-item>
                        </span>
                      </div>
                      <div class="">
                        <span class="title">{{ $t("form.listenEnd") }}：</span>
                        <span
                          v-if="scope.row.port_range_id"
                          class="titleValue"
                          >{{
                            scope.row.listen_port_range_max
                              ? scope.row.listen_port_range_max
                              : "-"
                          }}</span
                        >
                        <span v-else class="title">
                          <el-form-item
                            :prop="
                              'tableData.' +
                              scope.$index +
                              '.listen_port_range_max'
                            "
                            :rules="rules.listen_port_range_max"
                            style="width: 120px"
                          >
                            <el-input
                              v-model="scope.row.listen_port_range_max"
                              size="mini"
                              style="width: 120px"
                              :placeholder="$t('form.listenEndPort')"
                            />
                          </el-form-item>
                        </span>
                      </div>
                    </div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                :label="$t('form.purposeDescription')"
                min-width="200"
              >
                <template slot-scope="scope">
                  <div class="flex">
                    <span v-if="scope.row.port_range_id">{{
                      scope.row.description ? scope.row.description : "-"
                    }}</span>
                    <span v-else>
                      <el-form-item
                        :prop="'tableData.' + scope.$index + '.description'"
                      >
                        <el-input
                          v-model="scope.row.description"
                          size="mini"
                          type="textarea"
                          maxlength="255"
                          show-word-limit
                          :placeholder="
                            $t('form.pleaseInputPurposeDescription')
                          "
                        />
                      </el-form-item>
                    </span>
                  </div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.operation')" width="120">
                <template slot-scope="scope">
                  <el-button
                    v-if="!scope.row.port_range_id"
                    type="text"
                    size="small"
                    @click="handleSubmitClick(scope.$index, scope.row)"
                  >
                    <i
                      class="iconfont icon-duigou iconAdd"
                      @click="handleSubmitClick(scope.$index, scope.row)"
                    />
                  </el-button>
                  <el-button
                    v-else
                    type="text"
                    size="small"
                    class="-mt-2"
                    @click="handleEditClick(scope.$index, scope.row)"
                  >
                    <span>{{ $t("form.edit") }}</span>
                  </el-button>
                  <el-button
                    type="text"
                    size="small"
                    class="-mt-2"
                    @click="handleDeteteClick(scope.$index, scope.row)"
                  >
                    <span>{{ $t("form.remove") }}</span>
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form>
        </el-col>
      </el-row>
      <el-row :gutter="40">
        <el-col :span="24">
          <el-button
            type="primary"
            icon="el-icon-plus"
            size="mini"
            class="mt-4"
            @click="handleAddClick()"
            >{{ $t("form.add") }}</el-button
          >
        </el-col>
      </el-row>
      <el-row :gutter="40">
        <el-col :span="24">
          <div class="mt-3 mb-2" style="text-align: center">
            <el-button type="primary" size="small" @click="doEdit()">{{
              $t("form.save")
            }}</el-button>
            <el-button size="small" @click="cancel()">{{
              $t("form.cancel")
            }}</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>
<script>
import { mapGetters } from "vuex";
import { getServiceGatewaysDetail, editServiceGateways } from "@/api/mainApi";
export default {
  components: {},
  data() {
    return {
      loadingForm: false,
      detailMain: {},
      port_range_min: true,
      listen_port_range_min: true,
      form: {
        tableData: [],
      },
      checkList: [],
      mainRules: {
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputGatewayName"),
            trigger: "blur",
          },
        ],
      },
      rules: {
        port_range_min: [
          {
            required: true,
            message: this.$t("message.pleaseInputStartPort"),
            trigger: "blur",
          },
          {
            pattern: /^[0-9]*[1-9][0-9]*$/,
            message: this.$t("message.pleaseInputPositiveInteger"),
          },
        ],
        port_range_max: [
          {
            required: true,
            message: this.$t("message.pleaseInputEndPort"),
            trigger: "blur",
          },
          {
            pattern: /^[0-9]*[1-9][0-9]*$/,
            message: this.$t("message.pleaseInputPositiveInteger"),
          },
        ],
        listen_port_range_min: [
          {
            required: true,
            message: this.$t("message.pleaseInputListenStartPort"),
            trigger: "blur",
          },
          {
            pattern: /^[0-9]*[1-9][0-9]*$/,
            message: this.$t("message.pleaseInputPositiveInteger"),
            trigger: "blur",
          },
        ],
        listen_port_range_max: [
          {
            required: true,
            message: this.$t("message.pleaseInputListenEndPort"),
            trigger: "blur",
          },
          {
            pattern: /^[0-9]*[1-9][0-9]*$/,
            message: this.$t("message.pleaseInputPositiveInteger"),
            trigger: "blur",
          },
        ],
      },
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
    // 第一次起始端口填写，结束端口也填写
    handlePortRangMinChange(index, row) {
      if (this.port_range_min) {
        row.port_range_max = row.port_range_min;
        this.port_range_min = false;
      }
    },
    // 第一次起始端口填写，结束端口也填写
    handleListenPortRangMinChange(index, row) {
      if (this.listen_port_range_min) {
        row.listen_port_range_max = row.listen_port_range_min;
        this.listen_port_range_min = false;
      }
    },
    // 设置keys
    getRowKeys(row) {
      return row.port_range_id;
    },
    handleAddClick() {
      this.form.tableData.push({
        external_ip: "",
        internal_ip: "",
        port_range_id: "",
        listen_port_range_max: "",
        listen_port_range_min: "",
        port_range_max: "",
        port_range_min: "",
        description: "",
      });
    },
    handleDeteteClick(index, row) {
      this.$confirm(
        this.$t("message.confirmDeleteDataEditAfterSave"),
        this.$t("message.prompt"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.form.tableData.forEach((item, index) => {
            item == row ? this.form.tableData.splice(index, 1) : "";
          });
        })
        .catch(() => {
          this.$notify({
            title: this.$t("message.cancelDelete"),
            type: "info",
            duration: 2500,
          });
        });
    },
    handleSubmitClick(index, row) {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.tableData[index].port_range_id = Date.now();
          this.$set(this.form.tableData, index, this.form.tableData[index]);
          this.$forceUpdate();
        } else {
          return false;
        }
      });
    },
    handleEditClick(index, row) {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.form.tableData[index].port_range_id = "";
          this.$set(this.form.tableData, index, this.form.tableData[index]);
          this.$forceUpdate();
        } else {
          return false;
        }
      });
    },
    async init() {
      const list = await getServiceGatewaysDetail(this.$route.params.id);
      this.detailMain = list;
      if (this.detailMain.purpose) {
        if (this.detailMain.purpose == "") {
          this.checkList = [];
        } else {
          this.checkList = this.detailMain.purpose.split(",");
        }
      } else {
        this.checkList = [];
      }

      this.form.tableData = list.port_ranges;
    },
    doEdit() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          var detailMain = JSON.parse(JSON.stringify(this.detailMain));

          var newArr = [...new Set(this.checkList)];
          var data = {
            name: detailMain.name,
            description: detailMain.description,
            purpose: this.checkList.length > 0 ? newArr.join(",") : "",
            service_gateway_id: detailMain.service_gateway_id,
            port_ranges: detailMain.port_ranges.map((item) => {
              return {
                external_ip: item.external_ip,
                internal_ip: item.internal_ip,
                listen_port_range_max: item.listen_port_range_max,
                listen_port_range_min: item.listen_port_range_min,
                port_range_max: item.port_range_max,
                port_range_min: item.port_range_min,
                description: item.description ? item.description : "",
              };
            }),
          };
          data.port_ranges.forEach((item, index) => {
            item.external_ip == "" ? delete item.external_ip : "";
            item.internal_ip == "" ? delete item.internal_ip : "";
            item.listen_port_range_max == ""
              ? delete item.listen_port_range_max
              : "";
            item.listen_port_range_min == ""
              ? delete item.listen_port_range_min
              : "";
            item.port_range_max == "" ? delete item.port_range_max : "";
            item.port_range_min == "" ? delete item.port_range_min : "";
          });

          this.loadingForm = true;
          editServiceGateways(this.$route.params.id, data)
            .then((res) => {
              this.loadingForm = false;
              this.init();
              this.$notify({
                title: this.$t("message.editSuccess"),
                type: "success",
                duration: 2500,
              });
              this.$router.push("/middleware/service/gateway");
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
        path: "/middleware/service/gateway",
      });
    },
  },
};
</script>
<style scoped>
.titleAddress {
  width: 45px;
}

.title {
  text-align: end;
  /* width: 72px; */
  display: inline-block;
}

.titleValue {
  min-width: 120px;
  display: inline-block;
}

::v-deep .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 14px;
}
</style>
