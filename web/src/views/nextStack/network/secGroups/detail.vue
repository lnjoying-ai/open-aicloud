<template>
  <div class="detailPage h-full relative">
    <!-- 基本信息 start -->
    <el-card class="!border-none mb-3 pb-3 mt-2">
      <template #header>
        <div class="">
          <span>{{ $t("form.basicInfo") }}</span>
        </div>
      </template>
      <el-form
        v-loading="loadingDetail"
        :size="$store.state.nextStack.viewSize.main"
        :element-loading-text="$t('domain.loading')"
        :model="form"
        label-width="120px"
      >
        <div class="text item">
          <el-form-item
            :label="$t('form.name') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.name || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.id') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.sgId || "-" }}</span>
          </el-form-item>
          <el-form-item
            :label="$t('form.secGroupsRule') + ':'"
            style="width: 50%; float: left"
          >
            <span
              class="text-blue-400 cursor-pointer"
              @click="tabContent = 'info'"
              >{{ form.rules ? form.rules.length : 0 }}</span
            >
          </el-form-item>
          <el-form-item
            :label="$t('form.associatedInstance') + ':'"
            style="width: 50%; float: left"
          >
            <span
              class="text-blue-400 cursor-pointer"
              @click="tabContent = 'instances'"
              >{{ form.vmInstances ? form.vmInstances.length : 0 }}</span
            >
          </el-form-item>
          <el-form-item
            :label="$t('form.description') + ':'"
            style="width: 50%; float: left"
          >
            <span>{{ form.description || "-" }}</span>
          </el-form-item>
        </div>
      </el-form>
    </el-card>
    <!-- 基本信息 end -->
    <el-card class="!border-none" :body-style="{ padding: '15px 20px' }">
      <el-tabs v-model="tabContent">
        <el-tab-pane :label="$t('form.inOutRule')" name="info">
          <!-- 入方向规则 start -->
          <el-card class="border-0 mb-10 mt-0" shadow="never">
            <template #header>
              <div class="">
                <span>{{ $t("form.inDirectionRule") }}</span>

                <el-button
                  class="ml-4 float-right"
                  type="primary"
                  size="mini"
                  @click="addRule(form.sgId, 0)"
                  >{{ $t("form.addInRule") }}</el-button
                >
              </div>
            </template>
            <el-table
              v-loading="loadingDetail"
              :element-loading-text="$t('domain.loading')"
              :data="inList"
              max-height="calc(100% - 3rem)"
              class="!overflow-y-auto"
              stripe
              :scrollbar-always-on="false"
            >
              <el-table-column
                prop="priority"
                width="160px"
                :label="$t('form.priority')"
              >
                <template #header>
                  {{ $t("form.priority") }}

                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-200px">
                        {{ $t("form.priorityRange") }}
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope">
                  <span class="block">{{ scope.row.priority }}</span>
                  <div class="leading-tight">
                    <small>(id:{{ scope.row.ruleId }})</small>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="name" :label="$t('form.strategy')">
                <template #header>
                  {{ $t("form.strategy") }}

                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-200px">
                        {{ $t("form.prioritySame") }}
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope">
                  {{
                    scope.row.action === 0
                      ? $t("form.deny")
                      : scope.row.action === 1
                      ? $t("form.allow")
                      : "-"
                  }}</template
                >
              </el-table-column>
              <el-table-column
                prop="name"
                :label="$t('form.protocolPort')"
                width="340"
              >
                <template #header>
                  {{ $t("form.protocolPort") }}

                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-48">
                        <p>{{ $t("form.protocolPortTips") }}</p>
                        <ul>
                          <li>{{ $t("form.protocolPortTips1") }}</li>
                          <li>{{ $t("form.protocolPortTips2") }}</li>
                          <li>{{ $t("form.protocolPortTips3") }}</li>
                          <li>{{ $t("form.protocolPortTips4") }}</li>
                          <li>{{ $t("form.protocolPortTips5") }}</li>
                        </ul>
                        <p>{{ $t("form.protocolPortTips6") }}</p>
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope"
                  >{{
                    scope.row.protocol === 0
                      ? "TCP:"
                      : scope.row.protocol === 1
                      ? "UDP:"
                      : scope.row.protocol === 3
                      ? $t("form.all")
                      : scope.row.protocol === 4
                      ? "ICMP:"
                      : ""
                  }}
                  <span v-if="scope.row.protocol === 0">
                    <el-tag
                      v-for="(item, index) in getport(scope.row.port)"
                      :key="index"
                      size="small"
                      class="mr-0.5 mb-0.5"
                      >{{ item }}</el-tag
                    >
                  </span>
                  <span v-if="scope.row.protocol === 1">
                    <el-tag
                      v-for="(item, index) in getport(scope.row.port)"
                      :key="index"
                      size="small"
                      class="mr-0.5 mb-0.5"
                      >{{ item }}</el-tag
                    >
                  </span>
                  <span v-if="scope.row.protocol === 3" />
                  <span v-if="scope.row.protocol === 4">
                    <el-tag
                      v-for="(item, index) in getport(scope.row.port)"
                      :key="index"
                      size="small"
                      class="mr-0.5 mb-0.5"
                      >{{ getICMPname(item == "all" ? "0" : item) }}</el-tag
                    >
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="name" :label="$t('form.type')">
                <template #default="scope">
                  {{
                    scope.row.addressType === 0
                      ? "IPv4"
                      : scope.row.addressType === 1
                      ? "IPv6"
                      : "-"
                  }}</template
                >
              </el-table-column>

              <el-table-column prop="name" :label="$t('form.sourceAddress')">
                <template #header>
                  {{ $t("form.sourceAddress") }}

                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-48">
                        <p>{{ $t("form.sourceAddressTips") }}</p>
                        <ul>
                          <li>{{ $t("form.sourceAddressTips1") }}</li>
                          <li>{{ $t("form.sourceAddressTips2") }}</li>
                          <li>{{ $t("form.sourceAddressTips3") }}</li>
                          <li>{{ $t("form.sourceAddressTips4") }}</li>
                        </ul>
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope">
                  <span
                    v-if="scope.row.addressRef && scope.row.addressRef.cidr"
                    >{{ scope.row.addressRef.cidr }}</span
                  >
                  <span
                    v-if="scope.row.addressRef && scope.row.addressRef.sgId"
                    >{{ getSg(scope.row.addressRef) }}</span
                  >
                </template>
              </el-table-column>
              <el-table-column
                prop="description"
                :label="$t('form.description')"
              >
                <template #default="scope">
                  {{
                    scope.row.description ? scope.row.description : "-"
                  }}</template
                >
              </el-table-column>
              <el-table-column
                prop="createTime"
                :label="$t('form.createTime')"
              />
              <el-table-column
                prop="updateTime"
                :label="$t('form.updateTime')"
              />

              <el-table-column
                prop="address"
                :label="$t('form.operation')"
                width="120"
              >
                <template #default="scope">
                  <div class="czlb">
                    <el-popover placement="bottom" width="110" trigger="click">
                      <div
                        class="icon_cz"
                        @click="toEdit(form.sgId, 0, scope.row)"
                      >
                        <i class="el-icon-edit" />
                        {{ $t("form.edit") }}
                      </div>
                      <div class="icon_cz" @click="toDelete(scope.row)">
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
          </el-card>
          <!-- 入方向规则 end -->
          <!-- 出方向规则 start -->
          <el-card class="border-0 mb-3 mt-2" shadow="never">
            <template #header>
              <div class="">
                <span>{{ $t("form.outDirectionRules") }}</span>

                <el-button
                  class="ml-4 float-right"
                  type="primary"
                  size="mini"
                  @click="addRule(form.sgId, 1)"
                >
                  {{ $t("form.addOutRule") }}
                </el-button>
              </div>
            </template>

            <el-table
              v-loading="loadingDetail"
              :element-loading-text="$t('domain.loading')"
              :data="outList"
              max-height="calc(100% - 3rem)"
              class="!overflow-y-auto"
              stripe
              :scrollbar-always-on="false"
            >
              <el-table-column
                prop="priority"
                width="160px"
                :label="$t('form.priority')"
              >
                <template #header>
                  {{ $t("form.priority") }}
                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-200px">
                        {{ $t("form.priorityRange") }}
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope">
                  <span class="block">{{ scope.row.priority }}</span>
                  <div class="leading-tight">
                    <small>(id:{{ scope.row.ruleId }})</small>
                  </div>
                </template>
              </el-table-column>
              <el-table-column prop="name" :label="$t('form.strategy')">
                <template #header>
                  {{ $t("form.strategy") }}

                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-200px">
                        {{ $t("form.prioritySame") }}
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope">
                  {{
                    scope.row.action === 0
                      ? $t("form.deny")
                      : scope.row.action === 1
                      ? $t("form.allow")
                      : "-"
                  }}</template
                >
              </el-table-column>
              <el-table-column
                prop="name"
                :label="$t('form.protocolPort')"
                width="340"
              >
                <template #header>
                  {{ $t("form.protocolPort") }}

                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-48">
                        <p>{{ $t("form.protocolPortTips") }}</p>
                        <ul>
                          <li>{{ $t("form.protocolPortTips1") }}</li>
                          <li>{{ $t("form.protocolPortTips2") }}</li>
                          <li>{{ $t("form.protocolPortTips3") }}</li>
                          <li>{{ $t("form.protocolPortTips4") }}</li>
                          <li>{{ $t("form.protocolPortTips5") }}</li>
                        </ul>
                        <p>{{ $t("form.protocolPortTips6") }}</p>
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope"
                  >{{
                    scope.row.protocol === 0
                      ? "TCP:"
                      : scope.row.protocol === 1
                      ? "UDP:"
                      : scope.row.protocol === 3
                      ? $t("form.all")
                      : scope.row.protocol === 4
                      ? "ICMP:"
                      : ""
                  }}
                  <span v-if="scope.row.protocol === 0">
                    <el-tag
                      v-for="(item, index) in getport(scope.row.port)"
                      :key="index"
                      size="small"
                      class="mr-0.5 mb-0.5"
                      >{{ item }}</el-tag
                    >
                  </span>
                  <span v-if="scope.row.protocol === 1">
                    <el-tag
                      v-for="(item, index) in getport(scope.row.port)"
                      :key="index"
                      size="small"
                      class="mr-0.5 mb-0.5"
                      >{{ item }}</el-tag
                    >
                  </span>
                  <span v-if="scope.row.protocol === 3" />
                  <span v-if="scope.row.protocol === 4">
                    <el-tag
                      v-for="(item, index) in getport(scope.row.port)"
                      :key="index"
                      size="small"
                      class="mr-0.5 mb-0.5"
                      >{{ getICMPname(item == "all" ? "0" : item) }}</el-tag
                    >
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="name" :label="$t('form.type')">
                <template #default="scope">
                  {{
                    scope.row.addressType === 0
                      ? $t("form.IPv4")
                      : scope.row.addressType === 1
                      ? $t("form.IPv6")
                      : "-"
                  }}</template
                >
              </el-table-column>

              <el-table-column
                prop="name"
                :label="$t('form.destinationAddress')"
              >
                <template #header>
                  {{ $t("form.destinationAddress") }}
                  <el-tooltip placement="top" effect="dark">
                    <template #content>
                      <div class="w-48">
                        <p>{{ $t("form.destinationAddressTips") }}</p>
                        <ul>
                          <li>{{ $t("form.destinationAddressTips1") }}</li>
                          <li>{{ $t("form.destinationAddressTips2") }}</li>
                          <li>{{ $t("form.destinationAddressTips3") }}</li>
                          <li>{{ $t("form.destinationAddressTips4") }}</li>
                        </ul>
                      </div>
                    </template>
                    <span
                      class="text-xs inline-block align-middle cursor-pointer"
                    >
                       <i class="el-icon-info" />
                    </span>
                  </el-tooltip>
                </template>
                <template #default="scope">
                  <span
                    v-if="scope.row.addressRef && scope.row.addressRef.cidr"
                    >{{ scope.row.addressRef.cidr }}</span
                  >
                  <span
                    v-if="scope.row.addressRef && scope.row.addressRef.sgId"
                    >{{ getSg(scope.row.addressRef) }}</span
                  >
                </template>
              </el-table-column>
              <el-table-column
                prop="description"
                :label="$t('form.description')"
              >
                <template #default="scope">
                  {{ scope.row.description ? scope.row.description : "-" }}
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                :label="$t('form.createTime')"
              />
              <el-table-column
                prop="updateTime"
                :label="$t('form.updateTime')"
              />

              <el-table-column
                prop="address"
                :label="$t('form.operation')"
                width="120"
              >
                <template #default="scope">
                  <div class="czlb">
                    <el-popover placement="bottom" width="110" trigger="click">
                      <div
                        class="icon_cz"
                        @click="toEdit(form.sgId, 1, scope.row)"
                      >
                        <i class="el-icon-edit" />
                        {{ $t("form.edit") }}
                      </div>
                      <div class="icon_cz" @click="toDelete(scope.row)">
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
          </el-card>
          <!-- 出方向规则 end -->
        </el-tab-pane>
        <el-tab-pane :label="$t('form.associatedInstance')" name="instances">
          <!-- 关联实例 start -->
          <el-card class="border-0 mb-3 mt-0" shadow="never">
            <template #header>
              <div class="">
                <span>{{ $t("form.associatedInstance") }}</span>
              </div>
            </template>
            <el-button
              class="float-right mb-4"
              type="primary"
              :size="$store.state.nextStack.viewSize.main"
              @click="addInstance('vm')"
              >{{ $t("form.add") }}</el-button
            >

            <el-button
              :disabled="nowmultipleSelectionId.length === 0"
              class="float-right mr-4 mb-4"
              type="primary"
              :size="$store.state.nextStack.viewSize.main"
              @click="toUnbound(nowmultipleSelectionId)"
              >{{ $t("form.batchUnbound") }}</el-button
            >

            <el-table
              ref="nowmultipleTableRef"
              v-loading="loadingDetail"
              :element-loading-text="$t('domain.loading')"
              :data="form.vmInstances"
              max-height="calc(100% - 3rem)"
              class="!overflow-y-auto"
              stripe
              :scrollbar-always-on="false"
              :row-key="rowKey"
              @selection-change="nowhandleSelectionChange"
            >
              <el-table-column
                type="selection"
                width="55"
                :reserve-selection="true"
              />
              <el-table-column prop="date" :label="$t('form.name')">
                <template #default="scope">
                  <router-link
                    target="_blank"
                    :to="'/nextStack/vm/detail/' + scope.row.instanceId"
                    class="text-blue-400"
                    >{{ scope.row.name }}</router-link
                  >
                </template>
              </el-table-column>
              <el-table-column prop="phaseStatus" :label="$t('form.status')">
                <template #default="scope">
                  <el-tag
                    :size="$store.state.nextStack.viewSize.tagStatus"
                    :type="filtersFun.getVmStatus(scope.row.phaseStatus, 'tag')"
                    >{{
                      filtersFun.getVmStatus(scope.row.phaseStatus, "status")
                    }}</el-tag
                  >
                </template>
              </el-table-column>
              <el-table-column prop="ip" :label="$t('form.ip')" />
              <el-table-column prop="flavorName" :label="$t('form.flavor')" />

              <el-table-column
                prop="address"
                :label="$t('form.operation')"
                width="120"
              >
                <template #default="scope">
                  <el-button
                    type="text"
                    :size="$store.state.nextStack.viewSize.main"
                    @click="toUnbound([scope.row.instanceId])"
                  >
                    {{ $t("form.unbound") }}
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
          <!-- 关联实例 end -->
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 关联实例 start -->
    <!-- form.vmInstances -->
    <el-dialog
      :visible.sync="addInstanceDialog"
      :title="addInstanceType == 'vm' ? $t('form.addInstance') : ''"
      width="1000px"
      :before-close="handleCloseInstance"
      :close-on-click-modal="false"
    >
      <el-table
        ref="multipleTableRef"
        :data="vmTableData"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="45"
          :selectable="vmTableSelected"
        />
        <el-table-column prop="date" :label="$t('form.name')">
          <template #default="scope">
            <router-link
              target="_blank"
              :to="'/nextStack/vm/detail/' + scope.row.instanceId"
              class="text-blue-400"
              >{{ scope.row.name }}</router-link
            >
          </template>
        </el-table-column>

        <el-table-column prop="hostname" :label="$t('form.hostname')" />
        <el-table-column prop="portInfo.ipAddress" :label="$t('form.ip')">
          <template #default="scope">
            <span>{{ scope.row.portInfo.ipAddress || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="subnetInfo.cidr"
          :label="$t('form.networkAddress')"
        >
          <template #default="scope">
            <span>{{ scope.row.subnetInfo.cidr || "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="filtersFun.getVmStatus(scope.row.phaseStatus, 'tag')"
              >{{
                filtersFun.getVmStatus(scope.row.phaseStatus, "status")
              }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="imageInfo" :label="$t('form.system')">
          <template #default="scope">
            <span v-if="scope.row.imageInfo">{{
              scope.row.imageInfo.name ? scope.row.imageInfo.name : "-"
            }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          :label="$t('form.createTime')"
          width="160px"
        />
      </el-table>
      <div class="flex justify-end mt-4 px-4">
        <el-pagination
          :page_num="vmForm.page_num"
          :page-size="vmForm.page_size"
          :page-sizes="$store.state.nextStack.page_sizes"
          :current-page="vmForm.page_num"
          layout="total, sizes, prev, pager, next, jumper"
          :total="vmForm.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            :size="$store.state.nextStack.viewSize.main"
            @click="handleCloseInstance()"
            >{{ $t("form.cancel") }}</el-button
          >
          <el-button
            :size="$store.state.nextStack.viewSize.main"
            type="primary"
            :loading="loading"
            @click="toAddInstance()"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
    <!-- 关联实例 end -->
    <addRulePage :item-info="addRuleInfo" @addRuleClose="addRuleClose" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import addRulePage from "@/views/nextStack/network/secGroups/addRule.vue";

import netmask from "netmask";
var Netmask = netmask.Netmask;

export default {
  components: {
    addRulePage,
  },
  data() {
    return {
      filtersFun: filtersFun,
      loadingDetail: false,
      loading: false,
      tabContent: "info", // 标签页切换
      addInstanceDialog: false, // 添加实例弹窗
      addInstanceType: "vm", // 添加实例 vm/实例
      form: {}, // 表单数据
      formVmInstancesIds: [], // 当前实例列表 id集合
      inList: [], // 入方向规则列表
      outList: [], // 出方向规则列表
      sgsListData: [], // 安全组列表

      vmTableData: [], // 实例列表
      vmForm: {
        // 实例搜索 筛选
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      nowmultipleSelection: [], // 选中已关联的实例
      nowmultipleSelectionId: [],
      timer: null,
      multipleSelection: [], // 关联实例 添加实例 当前选中内容
      multipleSelectionId: [], // 关联实例 添加实例 当前选中内容ID
      rules: {},
      addRuleInfo: {
        isAdd: true,
        dialogVisible: false,
        sgId: "",
        ruleType: "",
      },
    };
  },
  watch: {
    tabContent(val) {
      if (val) {
        this.$router.push({
          query: {
            type: val,
          },
        });
      }
    },
  },
  created() {
    this.getSgsList(); // 请求安全组列表
    this.getVmsInstabcesList(); // 请求实例列表
    this.getDetail(); // 请求详情
  },
  mounted() {
    this.tabContent = this.$route.query.type ? this.$route.query.type : "info";
    this.openTimer();
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  beforeDestroy() {
    this.closeTimer();
  },
  methods: {
    openTimer() {
      var thisT = this;
      thisT.closeTimer();
      thisT.timer = setInterval(() => {
        if (this.$store.getters.hxcloudData.cloud_id) {
          thisT.getDetailInterval();
        }
      }, this.$store.state.nextStack.listRefreshTime);
    },
    /** 关闭定时器 */
    closeTimer() {
      if (this.timer) clearInterval(this.timer);
    },
    rowKey(row) {
      return row.instanceId;
    },
    addRule(sgId, type) {
      this.addRuleInfo = {
        isAdd: true,
        dialogVisible: true,
        sgId: sgId,
        ruleType: type,
      };
    },
    toEdit(sgId, type, item) {
      this.addRuleInfo = {
        isAdd: false,
        dialogVisible: true,
        sgId: sgId,
        ruleType: type,
        item: item,
      };
    },

    addRuleClose() {
      this.getDetail();
      this.addRuleInfo = {
        isAdd: true,
        dialogVisible: false,
        sgId: "",
        ruleType: "",
      };
    },
    nowhandleSelectionChange(val) {
      // 选中已关联的实例
      this.nowmultipleSelection = val;
      this.nowmultipleSelectionId = val.map((item) => {
        return item.instanceId;
      });
    },
    // 清空选中的值
    clearSelection() {
      this.$refs.nowmultipleTableRef.clearSelection();
    },
    handleSelectionChange(val) {
      // 关联实例 添加实例 当前选中内容
      this.multipleSelection = val;
      this.multipleSelectionId = val.map((item) => {
        return item.instanceId;
      });
    },
    changePortIcmp(val, index) {
      // 选择全部协议
      if (
        val.list[index].port.length > 1 &&
        val.list[index].port.includes("0")
      ) {
        val.list[index].port = ["0"];
      }
    },
    ifIp(val, type) {
      if (
        val != undefined &&
        val != "" &&
        /^[0-9]+$/.test(val) &&
        (val * 1 === 0 || (val * 1 > 0 && val * 1 < type * 1))
      ) {
        return true;
      } else {
        return false;
      }
    },
    changePort(val, index) {
      // 过滤去掉特殊字符
      val.list[index].port = val.list[index].port.replace(/[^\d,-]/g, "");
    },
    getSg(item) {
      var data = this.sgsListData.filter((v) => {
        return v.sgId == item.sgId;
      })[0];
      return data && data.name ? data.name : "";
    },
    vmTableSelected(row, index) {
      // 筛选出已关联的实例
      return !this.formVmInstancesIds.includes(row.instanceId);
    },
    getICMPname(val) {
      // 获取ICMP名称
      var nameData = "";
      if (val != "") {
        nameData = this.$script.getICMP().filter((item) => {
          return val == item.value;
        })[0].name;
      }
      return nameData;
    },
    addInstance(type) {
      // 点击添加实例
      this.addInstanceType = type; // 打开添加实例弹窗
      this.addInstanceDialog = true; // 打开添加实例弹窗
    },

    changeAddressRefType(item) {
      // 源地址改变
      var id = this.$route.params.id;
      if (item.addressRefType == "cidr") {
        item.addressRef.cidr = "0.0.0.0/0";
        item.addressRef.sgId = "";
      }
      if (item.addressRefType == "sgId") {
        item.addressRef.cidr = "";
        item.addressRef.sgId = id;
      }
    },
    changeProtocol(item) {
      // 添加规则 协议端口变化
      if (item.protocol == 4) {
        // ICMP
        item.port = [];
      } else {
        item.port = "";
      }
    },
    toUnbound(item) {
      // 取消关联
      var id = this.$route.params.id;
      this.$confirm(
        this.$t("message.confirmBatchUnbindInstance"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          this.loading = true;
          mainApi
            .sgsUnboundAdd({ vmInstances: item }, id)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.unboundSuccess"),
                type: "success",
                duration: 2500,
              });
              this.clearSelection();
              this.getDetail(); // 请求详情
            })
            .catch((error) => {
              this.loading = false;
            });
        })
        .catch(() => {});
    },

    toAddInstance() {
      // 关联实例
      this.loading = true;
      var id = this.$route.params.id;
      if (this.multipleSelectionId.length >= 1) {
        mainApi
          .sgsBoundAdd({ vmInstances: this.multipleSelectionId }, id)
          .then((res) => {
            this.loading = false;
            this.$notify({
              title: this.$t("message.startAdd"),
              type: "success",
              duration: 2500,
            });
            this.handleCloseInstance(); // 初始化输入框 关闭弹窗
            this.getDetail(); // 请求详情
          })
          .catch((error) => {
            this.loading = false;
          });
      } else {
        this.$notify({
          title: this.$t("message.pleaseSelectInstanceFirst"),
          type: "warning",
          duration: 2500,
        });
        this.loading = false;
      }
    },

    handleCloseInstance() {
      this.$refs.multipleTableRef.clearSelection();
      this.multipleSelectionId = [];

      this.addInstanceDialog = false; // 关闭关联实例弹窗
    },

    getDetail() {
      // 获取详情
      this.loadingDetail = true;
      var id = this.$route.params.id;
      mainApi
        .sgsDetail(id)
        .then((res) => {
          this.form = res;
          this.loadingDetail = false;

          if (res.rules && res.rules.length >= 1) {
            this.inList = res.rules.filter((v) => {
              return v.direction === 0;
            });
            this.outList = res.rules.filter((v) => {
              return v.direction === 1;
            });
          }

          this.formVmInstancesIds = res.vmInstances.map((v) => {
            return v.instanceId;
          });
        })
        .catch((error) => {
          this.loadingDetail = false;
        });
    },
    getDetailInterval() {
      // 获取详情
      var id = this.$route.params.id;
      mainApi
        .sgsDetail(id)
        .then((res) => {
          this.form = res;

          if (res.rules && res.rules.length >= 1) {
            this.inList = res.rules.filter((v) => {
              return v.direction === 0;
            });
            this.outList = res.rules.filter((v) => {
              return v.direction === 1;
            });
          }

          this.formVmInstancesIds = res.vmInstances.map((v) => {
            return v.instanceId;
          });
        })
        .catch((error) => {});
    },
    getport(val) {
      return val.split(",");
    },

    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteRule"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .sgsRulesDel(item.ruleId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.deleteSuccess"),
                type: "success",
                duration: 2500,
              });
              this.getDetail();
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    getSgsList() {
      // sgs列表

      mainApi
        .sgsList({})
        .then((res) => {
          this.sgsListData = res.securityGroups;
        })
        .catch((error) => {});
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.vmForm.page_size = val;
      this.getVmsInstabcesList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.vmForm.page_num = val;
      this.getVmsInstabcesList();
    },
    getVmsInstabcesList() {
      // 实例列表
      const params = {
        name: this.vmForm.name,
        page_num: this.vmForm.page_num,
        page_size: this.vmForm.page_size,
      };
      for (const key in params) {
        if (
          params[key] === null ||
          params[key] === undefined ||
          params[key] === ""
        ) {
          delete params[key];
        }
      }

      mainApi
        .vmsInstabcesList(params)
        .then((res) => {
          this.vmTableData = res.vmInstancesInfo;
          this.vmForm.total = res.totalNum;
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.detailPage {
  .rulesTable {
    ::v-deep .el-table__inner-wrapper {
      .el-table__body-wrapper {
        .el-table__row {
          .el-table__cell {
            padding-bottom: 18px !important;

            .cell {
              overflow: inherit;
            }
          }
        }
      }
    }
  }
}
</style>
