<template>
  <div>
    <div v-if="detailMain" class="warpMain">
      <el-card class="box-card" shadow="never">
        <div class="text item">
          <!-- 集群信息 -->
          <el-form ref="form" label-width="75px" :inline="true">
            <el-form-item
              :label="$t('form.containerName') + ':'"
              style="width: 50%"
            >
              <span>{{ detailMain.name || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.containerDescription') + ':'"
              style="width: 100%"
            >
              <span>{{ detailMain.description || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.containerStatus') + ':'"
              style="width: 50%"
            >
              <span>{{ getStatusName(detailMain.status.code) }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.containerImage') + ':'"
              style="width: 50%"
            >
              <span>{{ detailMain.image_name || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.containerCommand') + ':'"
              style="width: 100%"
            >
              <span>{{ detailMain.cmd || "-" }}</span>
            </el-form-item>
            <el-form-item :label="$t('form.nodeName') + ':'" style="width: 50%">
              <span>{{ detailMain.node_name || "-" }}</span>
            </el-form-item>
            <el-form-item :label="$t('form.userName') + ':'" style="width: 50%">
              <span>{{ detailMain.user_name || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.organization') + ':'"
              style="width: 50%"
            >
              <span>{{ detailMain.bp_name || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.createTime') + ':'"
              style="width: 50%"
            >
              <span>{{ detailMain.create_time || "-" }}</span>
            </el-form-item>
            <el-form-item :label="$t('form.stopTime') + ':'" style="width: 50%">
              <span>{{ detailMain.stop_time || "-" }}</span>
            </el-form-item>
            <el-form-item
              :label="$t('form.storageConfig') + ':'"
              style="width: 50%"
            >
              <div style="max-height: 120px; overflow-y: auto; width: 100%">
                <div
                  v-if="
                    detailMain.container_configs != null &&
                    detailMain.container_configs.length > 0
                  "
                >
                  <span
                    v-for="(item, index) in detailMain.container_configs"
                    :key="index"
                  >
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="
                        item.group +
                        '/' +
                        item.data_id +
                        ':' +
                        item.container_dest
                      "
                      placement="bottom"
                      :open-delay="1000"
                    >
                      <div
                        style="
                          text-overflow: ellipsis;
                          width: 400px;
                          white-space: nowrap;
                          overflow: hidden;
                          color: #409eff;
                        "
                        @click="handleRuter(item.data_id)"
                      >
                        <span>
                          <svg
                            t="1668484560399"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                            p-id="2576"
                            width="16"
                            height="16"
                          >
                            <path
                              d="M704.721322 209.163502m0-3.952447l0-25.295662q0-3.952447 3.952447-3.952447l195.567083 0q3.952447 0 3.952447 3.952447l0 25.295662q0 3.952447-3.952447 3.952447l-195.567083 0q-3.952447 0-3.952447-3.952447Z"
                              p-id="2577"
                            />
                            <path
                              d="M737.76378 209.163502m-3.952448 0l-25.295661 0q-3.952447 0-3.952447-3.952447l0-110.826618q0-3.952447 3.952447-3.952447l25.295661 0q3.952447 0 3.952448 3.952447l0 110.826618q0 3.952447-3.952448 3.952447Z"
                              p-id="2578"
                            />
                            <path
                              d="M915.088064 191.7282m-2.794802 2.794802l-17.886734 17.886734q-2.794802 2.794802-5.589604 0l-187.922497-187.922497q-2.794802-2.794802 0-5.589605l17.886734-17.886734q2.794802-2.794802 5.589604 0l187.922497 187.922498q2.794802 2.794802 0 5.589604Z"
                              p-id="2579"
                            />
                            <path
                              d="M432.476764 1006.767331m-16.600278 0a16.600278 16.600278 0 1 0 33.200556 0 16.600278 16.600278 0 1 0-33.200556 0Z"
                              p-id="2580"
                            />
                            <path
                              d="M365.285163 990.325151H177.306778a33.674849 33.674849 0 0 1-33.516752-33.67485v-885.348155a33.516752 33.516752 0 0 1 33.516752-32.88436h545.753898V5.21723H177.306778A66.717307 66.717307 0 0 0 110.58947 71.934538v885.348155a66.717307 66.717307 0 0 0 66.717308 66.717307h187.978385a15.809788 15.809788 0 1 0 0-33.042458zM908.193299 275.406515a15.809788 15.809788 0 0 0-33.042458 0v681.243786a33.674849 33.674849 0 0 1-33.516751 33.67485h-343.07241a15.809788 15.809788 0 0 0 0 33.042457h342.914312a66.717307 66.717307 0 0 0 66.717307-66.717307V275.406515z"
                              p-id="2581"
                            />
                            <path
                              d="M743.771499 347.815347H544.093871a15.809788 15.809788 0 1 1 0-31.619577h199.677628a15.809788 15.809788 0 1 1 0 31.619577zM337.776131 347.815347h-63.239154a15.809788 15.809788 0 1 1 0-31.619577h63.239154a15.809788 15.809788 0 1 1 0 31.619577zM743.771499 529.94411h-73.673614a15.809788 15.809788 0 0 1 0-31.619577h73.673614a15.809788 15.809788 0 1 1 0 31.619577zM490.024394 529.94411H275.011271a15.809788 15.809788 0 1 1 0-31.619577H490.024394a15.809788 15.809788 0 0 1 0 31.619577z"
                              p-id="2582"
                            />
                            <path
                              d="M404.967732 399.513355A67.823993 67.823993 0 1 1 472.633627 332.005558a67.98209 67.98209 0 0 1-67.665895 67.507797z m0-104.344604A36.678709 36.678709 0 1 0 441.488343 332.005558a36.678709 36.678709 0 0 0-36.520611-36.836807zM743.771499 712.547167H544.093871a15.809788 15.809788 0 1 1 0-31.619577h199.677628a15.809788 15.809788 0 0 1 0 31.619577zM337.776131 712.547167h-63.239154a15.809788 15.809788 0 1 1 0-31.619577h63.239154a15.809788 15.809788 0 0 1 0 31.619577z"
                              p-id="2583"
                            />
                            <path
                              d="M404.967732 764.719469a67.823993 67.823993 0 1 1 67.665895-67.823993 67.98209 67.98209 0 0 1-67.665895 67.823993z m0-104.344604a36.520611 36.520611 0 1 0 36.520611 36.520611 36.520611 36.520611 0 0 0-36.520611-36.520611zM613.65694 582.116412a67.823993 67.823993 0 1 1 67.665895-67.823993 67.98209 67.98209 0 0 1-67.665895 67.823993z m0-104.344604a36.678709 36.678709 0 1 0 36.520611 36.520611 36.678709 36.678709 0 0 0-36.520611-36.520611z"
                              p-id="2584"
                            />
                          </svg>
                        </span>
                        <span> {{ item.group || "-" }}</span>
                        <span>{{ "/" + item.data_id || "-" }}</span>
                        <span>{{ ":" + item.container_dest || "-" }}</span
                        ><span>{{ ":" }}</span>
                        <span>{{
                          item.read_only
                            ? $t("form.readOnly")
                            : $t("form.readWrite")
                        }}</span>
                      </div>
                    </el-tooltip>
                  </span>
                </div>
                <div
                  v-if="
                    detailMain.container_volumes != null &&
                    detailMain.container_volumes.length > 0
                  "
                >
                  <span
                    v-for="(item, index) in detailMain.container_volumes"
                    :key="index"
                  >
                    <el-tooltip
                      class="item"
                      effect="dark"
                      :content="item"
                      placement="bottom"
                      :open-delay="1000"
                    >
                      <div
                        style="
                          text-overflow: ellipsis;
                          width: 400px;
                          white-space: nowrap;
                          overflow: hidden;
                        "
                      >
                        <span
                          ><svg
                            t="1668484737954"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                            p-id="5039"
                            width="16"
                            height="16"
                          >
                            <path
                              d="M88.32 936.32h640a157.44 157.44 0 0 0 78.72 21.76 159.36 159.36 0 0 0 159.36-159.36V192a159.36 159.36 0 0 0-314.88-32h-563.2a32 32 0 0 0-32 32v710.4a32 32 0 0 0 32 33.92zM808.32 896a95.36 95.36 0 1 1 95.36-95.36A95.36 95.36 0 0 1 808.32 896zM120.32 225.92h561.28a32 32 0 0 0 32-32 95.36 95.36 0 0 1 192 0v478.08a156.16 156.16 0 0 0-192 0v-320a32 32 0 0 0-64 0v428.16a30.72 30.72 0 0 0 0 6.4v12.8a156.8 156.8 0 0 0 18.56 72.96H120.32z"
                              fill="#1B213F"
                              p-id="5040"
                            />
                            <path
                              d="M540.8 512L453.12 597.12l-128-128a16 16 0 0 0-22.4 0L215.04 552.96a16 16 0 0 0 22.4 23.04l74.24-74.88 128 128a16 16 0 0 0 22.4 0l101.76-97.28A16 16 0 1 0 540.8 512z"
                              fill="#1B213F"
                              p-id="5041"
                            />
                          </svg>
                        </span>
                        <span>{{
                          item.substr(-2, 2) == "ro"
                            ? item.substr(0, item.length - 2) +
                              $t("form.readOnly")
                            : item.substr(0, item.length - 2) +
                              $t("form.readWrite")
                        }}</span>
                      </div>
                    </el-tooltip>
                  </span>
                </div>
              </div>
            </el-form-item>
          </el-form>
          <div style="margin-top: 20px">
            <el-tabs v-model="activeName" @tab-click="handleClick">
              <el-tab-pane :label="$t('form.containerEvent')" name="event">
                <el-table
                  ref="multipleTable"
                  :data="EventData"
                  stripe
                  tooltip-effect="dark"
                  style="width: 100%"
                  :default-sort="{ prop: 'date', order: 'descending' }"
                >
                  <el-table-column
                    prop="resource"
                    :label="$t('form.eventSource')"
                    min-width="120"
                  >
                    <template slot-scope="scope">
                      <span>
                        {{ scope.row.resource ? scope.row.resource : "-" }}
                        :
                        {{ scope.row.service ? scope.row.service : "-" }}
                      </span>

                      <el-popover
                        :ref="`node-popover-${scope.row.id}`"
                        placement="top-start"
                        title=""
                        width="500"
                        popper-class="tablepopover"
                        trigger="hover"
                      >
                        <p>
                          <span
                            v-if="getDescription(scope.row.description).status"
                          >
                            <el-table
                              :data="getDescription(scope.row.description).data"
                              stripe
                              size="mini"
                              max-height="200"
                              style="width: 100%"
                            >
                              <el-table-column
                                :label="$t('form.changeContent')"
                              >
                                <template slot-scope="scope">
                                  {{ scope.row.key }}
                                </template>
                              </el-table-column>
                              <el-table-column :label="$t('form.changeBefore')">
                                <template slot-scope="scope">
                                  <div>
                                    {{
                                      scope.row.value.oldValue
                                        ? scope.row.value.oldValue
                                        : "-"
                                    }}
                                  </div>
                                </template>
                              </el-table-column>
                              <el-table-column
                                :label="$t('form.changeBeforeDescription')"
                                show-overflow-tooltip
                              >
                                <template slot-scope="scope">
                                  <div>
                                    {{
                                      scope.row.value.oldValueCnDescription
                                        ? scope.row.value.oldValueCnDescription
                                        : "-"
                                    }}
                                  </div>
                                  <div>
                                    {{
                                      scope.row.value.oldValueEnDescription
                                        ? scope.row.value.oldValueEnDescription
                                        : "-"
                                    }}
                                  </div>
                                </template>
                              </el-table-column>

                              <el-table-column :label="$t('form.changeAfter')">
                                <template slot-scope="scope">
                                  <div>{{ scope.row.value.newValue }}</div>
                                </template>
                              </el-table-column>
                              <el-table-column
                                :label="$t('form.changeAfterDescription')"
                                show-overflow-tooltip
                              >
                                <template slot-scope="scope">
                                  <div>
                                    {{
                                      scope.row.value.newValueCnDescription
                                        ? scope.row.value.newValueCnDescription
                                        : "-"
                                    }}
                                  </div>
                                  <div>
                                    {{
                                      scope.row.value.newValueEnDescription
                                        ? scope.row.value.newValueEnDescription
                                        : "-"
                                    }}
                                  </div>
                                </template>
                              </el-table-column>
                            </el-table>
                          </span>
                          <span v-else>
                            <span>{{ $t("form.changeContent") }}：</span
                            >{{ getDescription(scope.row.description).data }}
                          </span>
                        </p>
                        <span slot="reference">
                          <i class="el-icon-info"
                        /></span>
                      </el-popover>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="resource_inst_name"
                    :label="$t('form.resourceName')"
                    min-width="200"
                  >
                    <template slot-scope="scope">
                      {{
                        scope.row.resource_inst_name
                          ? scope.row.resource_inst_name
                          : "-"
                      }}
                      <span class="block leading-none new-small-size"
                        >ID:{{ scope.row.resource_id }}</span
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="friendly_description"
                    :label="$t('form.eventDetail')"
                    min-width="380"
                  >
                    <template slot-scope="scope">
                      {{
                        scope.row.friendly_description
                          ? scope.row.friendly_description
                          : "-"
                      }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" :label="$t('form.type')">
                    <template slot-scope="scope">
                      {{ scope.row.type ? scope.row.type : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="trigger_time"
                    :label="$t('form.triggerTime')"
                    sortable
                    width="220"
                  >
                    <template slot-scope="scope">
                      {{
                        scope.row.trigger_time
                          ? formatDate(scope.row.trigger_time)
                          : "-"
                      }}
                    </template>
                  </el-table-column>
                </el-table>
                <div class="flex justify-end mt-4 px-4">
                  <el-pagination
                    :current-page="queryData.page_num"
                    :page-sizes="[5, 10, 20, 50, 100]"
                    :page-size="queryData.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalNum"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                  />
                </div>
              </el-tab-pane>
              <el-tab-pane
                :label="$t('form.containerOperationRecord')"
                name="record"
              >
                <el-table
                  ref="multipleTable"
                  :data="RecordData"
                  stripe
                  tooltip-effect="dark"
                  style="width: 100%"
                  :default-sort="{ prop: 'date', order: 'descending' }"
                >
                  <el-table-column
                    prop="resource"
                    :label="$t('form.logSource')"
                    min-width="120"
                  >
                    <template slot-scope="scope">
                      {{ scope.row.resource ? scope.row.resource : "-" }}
                      :
                      {{ scope.row.service ? scope.row.service : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="description"
                    :label="$t('form.logDetail')"
                    min-width="380"
                  >
                    <template slot-scope="scope">
                      <span v-if="scope.row.level == 'ERROR'">
                        <div>
                          <el-tag size="mini" type="danger" class="mr-3">{{
                            $t("form.important")
                          }}</el-tag>
                          {{
                            scope.row.description ? scope.row.description : "-"
                          }}
                        </div>
                        <div style="color: #f56c6c">
                          {{ scope.row.result ? scope.row.result : "-" }}
                        </div>
                      </span>
                      <span v-else>
                        <el-tag size="mini" type="info" class="mr-3">{{
                          $t("form.general")
                        }}</el-tag
                        >{{
                          scope.row.description ? scope.row.description : "-"
                        }}</span
                      >
                    </template>
                  </el-table-column>
                  <el-table-column prop="operator" :label="$t('form.operator')">
                    <template slot-scope="scope">
                      {{ scope.row.operator ? scope.row.operator : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column prop="access_ip" :label="$t('form.ip')">
                    <template slot-scope="scope">
                      {{ scope.row.access_ip ? scope.row.access_ip : "-" }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="trigger_time"
                    :label="$t('form.operationTime')"
                    sortable
                  >
                    <template slot-scope="scope">
                      {{
                        scope.row.trigger_time
                          ? formatDate(scope.row.trigger_time)
                          : "-"
                      }}
                    </template>
                  </el-table-column>
                </el-table>
                <div class="flex justify-end mt-4 px-4">
                  <el-pagination
                    :current-page="queryData.page_num"
                    :page-sizes="[5, 10, 20, 50, 100]"
                    :page-size="queryData.page_size"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="totalNum"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                  />
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </div>
      </el-card>
    </div>
    <div v-else>
      <el-card class="box-card" shadow="never">
        <el-empty :description="$t('form.noData')" />
      </el-card>
    </div>
  </div>
</template>
<script>
import {
  getInstances,
  Operation,
  Runlog,
  getApiEvents,
  getApiLogs,
} from "@/api/mainApi";
import initData from "@/mixins/initData";
export default {
  components: {},
  mixins: [initData],
  data() {
    return {
      detailMain: "", // 容器详情
      activeName: "event",
      // 容器事件数据
      EventData: [],
      // 容器操作记录数据
      RecordData: [],
      levelList: {
        INFO: this.$t("form.info"),
        WARNING: this.$t("form.warning"),
        CRITICAL: this.$t("form.critical"),
      },
      queryData: {
        page_size: 10,
        page_num: 1,
        resource_id: "",
        level: "",
      },
      totalNum: 0,
      newStatus: [
        {
          code: 1100,
          name: this.$t("statusAndType.creating"),
          include: [0, 1, 2, 3, 4, 100, 102, 101, 5, 6],
        },
        {
          code: 1101,
          name: this.$t("statusAndType.created"),
          include: [103],
        },
        {
          code: 1102,
          name: this.$t("statusAndType.createFailed"),
          include: [50, 51, 81, 82, 122, 123, 124, 125],
        },
        {
          code: 1103,
          name: this.$t("statusAndType.running"),
          include: [104, 105, 108],
        },
        {
          code: 1104,
          name: this.$t("statusAndType.containerExited"),
          include: [60, 61, 62, 63, 64, 65, 110, 111, 112, 113],
        },
        {
          code: 1105,
          name: this.$t("statusAndType.containerNotExist"),
          include: [130, 131, 132],
        },
        {
          code: 1110,
          name: this.$t("statusAndType.systemException"),
          include: [-100, 99, 120, 190],
        },
      ],
      status: [
        {
          code: -100,
          name: this.$t("statusAndType.resourceUnknown"),
        },
        {
          code: 0,
          name: this.$t("statusAndType.instanceParameterConfigured"),
        },
        {
          code: 1,
          name: this.$t("statusAndType.cloudStartDispatchCreateInstance"),
        },
        {
          code: 2,
          name: this.$t("statusAndType.selectTargetDevice"),
        },
        {
          code: 3,
          name: this.$t("statusAndType.assignTargetDevice"),
        },
        {
          code: 4,
          name: this.$t("statusAndType.prepareToCreate"),
        },
        {
          code: 50,
          name: this.$t("statusAndType.unreachable"),
        },
        {
          code: 51,
          name: this.$t("statusAndType.noMatchingNode"),
        },
        {
          code: 60,
          name: this.$t("statusAndType.stopping"),
        },
        {
          code: 61,
          name: this.$t("statusAndType.stopping"),
        },
        {
          code: 62,
          name: this.$t("statusAndType.stopping"),
        },
        {
          code: 63,
          name: this.$t("statusAndType.systemStop"),
        },
        {
          code: 64,
          name: this.$t("statusAndType.userStop"),
        },
        {
          code: 65,
          name: this.$t("statusAndType.overdueStop"),
        },
        {
          code: 81,
          name: this.$t("statusAndType.parameterErrorCreateFailed"),
        },
        {
          code: 82,
          name: this.$t("statusAndType.overdueCreateFailed"),
        },
        {
          code: 83,
          name: this.$t("statusAndType.deleting"),
        },
        {
          code: 84,
          name: this.$t("statusAndType.deleted"),
        },
        {
          code: 99,
          name: this.$t("statusAndType.cloudException"),
        },
        {
          code: 100,
          name: this.$t("statusAndType.deviceWaitingDispatch"),
        },
        {
          code: 101,
          name: this.$t("statusAndType.imageDownloading"),
        },
        {
          code: 102,
          name: this.$t("statusAndType.dataDownloading"),
        },
        {
          code: 103,
          name: this.$t("statusAndType.containerCreateSuccess"),
        },
        {
          code: 104,
          name: this.$t("statusAndType.containerStarting"),
        },
        {
          code: 105,
          name: this.$t("statusAndType.containerRunning"),
        },
        {
          code: 106,
          name: this.$t("statusAndType.uploadResult"),
        },
        {
          code: 108,
          name: this.$t("statusAndType.restarting"),
        },
        {
          code: 110,
          name: this.$t("statusAndType.containerExited"),
        },
        {
          code: 111,
          name: this.$t("statusAndType.userStop"),
        },
        {
          code: 112,
          name: this.$t("statusAndType.overdueStop"),
        },
        {
          code: 113,
          name: this.$t("statusAndType.abnormalExit"),
        },
        {
          code: 120,
          name: this.$t("statusAndType.systemStop"),
        },
        {
          code: 121,
          name: this.$t("statusAndType.resourceParameterError"),
        },
        {
          code: 122,
          name: this.$t("statusAndType.hardwareError"),
        },
        {
          code: 123,
          name: this.$t("statusAndType.noImage"),
        },
        {
          code: 124,
          name: this.$t("statusAndType.imageDownloadFailed"),
        },
        {
          code: 125,
          name: this.$t("statusAndType.instanceCreateFailed"),
        },
        {
          code: 130,
          name: this.$t("statusAndType.instanceDeleted"),
        },
        {
          code: 131,
          name: this.$t("statusAndType.instanceNotExist"),
        },
        {
          code: 132,
          name: this.$t("statusAndType.instanceAutoDeleted"),
        },
        {
          code: 190,
          name: this.$t("statusAndType.systemException"),
        },
      ],
    };
  },
  watch: {},
  created() {},
  mounted() {
    this.init();
  },
  methods: {
    getStatus(code) {
      return this.status.find((item) => item.code === code)?.name;
    },
    getStatusName(code) {
      const item = this.newStatus.find((item) => item.include.includes(code));
      return item ? item.name : "-";
    },
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
    handleRuter(id) {
      this.$router.push({
        path: "/containerApplicationService/configureInformation",
        query: {
          id: id,
        },
      });
    },
    async init() {
      // 请求容器详情
      const list = await getInstances(this.$route.params.id);
      this.detailMain = list;
      this.queryData.resource_id = this.detailMain.id;
      this.getevent();
    },
    // Tab切换
    handleClick(tab) {
      if (tab.name == "event") {
        this.queryData.level = "";
        this.queryData.page_num = 1;
        this.getevent();
      } else if (tab.name == "record") {
        this.queryData.level = "";
        this.queryData.page_num = 1;
        this.getrecord();
      }
    },
    // 获取容器事件数据
    async getevent() {
      // 请求容器详情
      const list = await getApiEvents(this.queryData);
      this.EventData = list.events;
      this.totalNum = list.total_num;
    },
    // 获取容器事件数据
    async getrecord() {
      // 请求容器详情
      const list = await getApiLogs(this.queryData);
      this.RecordData = list.logs;
      this.totalNum = list.total_num;
    },
    handleSizeChange(val) {
      this.queryData.page_size = val;
      if (this.activeName == "event") {
        this.getevent();
      } else if (this.activeName == "record") {
        this.getrecord();
      }
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val;
      if (this.activeName == "event") {
        this.getevent();
      } else if (this.activeName == "record") {
        this.getrecord();
      }
    },
    handleLevelChange(value) {
      this.queryData.level = value;
      if (this.activeName == "event") {
        this.getevent();
      } else if (this.activeName == "record") {
        this.getrecord();
      }
    },
    searchinit() {
      this.queryData.level = "";
      this.queryData.page_num = 1;
      this.queryData.page_size = 10;
      if (this.activeName == "event") {
        this.getevent();
      } else if (this.activeName == "record") {
        this.getrecord();
      }
    },
    getDescription(description) {
      // 获取变动内容 格式化
      if (!description) {
        return {
          status: false,
          data: "-",
        };
      }
      try {
        var data = JSON.parse(description);
        var dataInfo = {
          status: true,
          data: Object.keys(data).map((key) => {
            return {
              key: key,
              value: data[key],
            };
          }),
        };
        return dataInfo;
      } catch (e) {
        var dataInfo = {
          status: false,
          data: description,
        };
        return dataInfo;
      }
    },
    // 生命周期计算
    lifeCycle(start_time, stop_time) {
      const start = new Date(start_time);
      const stop = new Date(stop_time);
      const time = stop.getTime() - start.getTime();
      const day = parseInt(time / (1000 * 60 * 60 * 24));
      return day;
    },
  },
};
</script>
<style lang="scss" scoped>
.row-bg {
  padding: 10px 0;
}

.el-col-4 {
  width: 40%;
  font-size: 15px;
  font-family: "Microsoft YaHei ", "Microsoft YaHei", sans-serif;
  font-weight: 400;
  font-style: normal;
  color: rgba(0, 0, 0, 0.8470588235294118);
  line-height: 22px;
}

::v-deep .el-table th {
  background: #fafafa;
}

::v-deep .el-card {
  border: 0px solid #ebeef5;
  background-color: #fff;
  color: #303133;
  -webkit-transition: 0.3s;
  transition: 0.3s;
}

::v-deep .el-tabs__nav-wrap::after {
  content: "";
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0px;
  z-index: 1;
}

svg:not(:root) {
  overflow: hidden;
  display: inline-block;
  margin: 0px 5px;
}
</style>
