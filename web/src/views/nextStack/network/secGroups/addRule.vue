<template>
  <div class="sgsAddPage h-full">
    <el-dialog
      :visible.sync="addruleDialog"
      :title="
        (isAddruleType ? $t('form.add') : $t('form.edit')) +
        (addruleType === 0
          ? $t('form.inDirectionRule')
          : $t('form.outDirectionRule'))
      "
      width="1000px"
      :before-close="handleClose"
      :close-on-click-modal="false"
    >
      <el-alert
        :title="$t('form.specialSecurityGroupTips')"
        type="warning"
        show-icon
        :closable="false"
      />
      <el-form
        ref="addRuleDataRef"
        key="0"
        class="mt-4"
        :model="addRuleData"
        label-width="0px"
      >
        <el-table
          :size="$store.state.nextStack.viewSize.main"
          :data="addRuleData.list"
          style="width: 100%"
          class="rulesTable"
        >
          <el-table-column
            prop="priority"
            :label="$t('form.priority')"
            width="100px"
          >
            <template #header>
              {{ $t("form.priority") }}

              <el-tooltip placement="top" effect="dark">
                <template #content>
                  <div class="w-200px">
                    {{ $t("form.priorityRange") }}
                  </div>
                </template>
                <span class="text-xs inline-block align-middle cursor-pointer">
                  <i class="el-icon-info" />
                </span>
              </el-tooltip>
            </template>
            <template #default="scope">
              <el-form-item
                class="m-0"
                label=""
                :prop="'list.' + scope.$index + '.priority'"
                :rules="[
                  {
                    required: true,
                    validator: validatePriority,
                    trigger: 'change',
                  },
                  {
                    required: true,
                    validator: validatePriority,
                    trigger: 'blur',
                  },
                ]"
              >
                <el-input
                  v-model="scope.row.priority"
                  class="w-4/5"
                  :size="$store.state.nextStack.viewSize.main"
                  placeholder="1-100"
                />
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            prop="action"
            :label="$t('form.strategy')"
            width="100"
          >
            <template #header>
              {{ $t("form.strategy") }}

              <el-tooltip placement="top" effect="dark">
                <template #content>
                  <div class="w-200px">
                    {{ $t("form.prioritySame") }}
                  </div>
                </template>
                <span class="text-xs inline-block align-middle cursor-pointer">
                   <i class="el-icon-info" />
                </span>
              </el-tooltip>
            </template>
            <template #default="scope">
              <el-select
                v-model="scope.row.action"
                class=""
                :size="$store.state.nextStack.viewSize.main"
              >
                <el-option key="accept" :label="$t('form.allow')" :value="1" />
                <el-option key="drop" :label="$t('form.deny')" :value="0" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="port" :label="$t('form.protocolPort')">
            <template #header>
              <span v-if="addRuleData.list[0].protocol == 3">{{
                $t("form.protocol")
              }}</span>
              <span
                v-if="
                  addRuleData.list[0].protocol == 0 ||
                  addRuleData.list[0].protocol == 1
                "
                >{{ $t("form.protocolOrPort") }}</span
              >
              <span v-if="addRuleData.list[0].protocol == 4">{{
                $t("form.protocolType")
              }}</span>
              <el-tooltip placement="top" effect="dark">
                <template #content>
                  <div class="w-220px">
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
                <span class="text-xs inline-block align-middle cursor-pointer">
                   <i class="el-icon-info" />
                </span>
              </el-tooltip>
            </template>
            <template #default="scope">
              <el-form-item label="" :prop="'list.' + scope.$index + '.port'">
                <el-select
                  v-model="scope.row.protocol"
                  class="w-52 mt-6"
                  :size="$store.state.nextStack.viewSize.main"
                  @change="changeProtocol(scope.row)"
                >
                  <el-option key="IP" :label="$t('form.all')" :value="3" />
                  <el-option key="TCP" label="TCP" :value="0" />
                  <el-option key="UDP" label="UDP" :value="1" />
                  <el-option key="ICMP" label="ICMP" :value="4" />
                </el-select>
                <el-form-item
                  v-if="scope.row.protocol != 4 && scope.row.protocol != 3"
                  label=""
                  :prop="'list.' + scope.$index + '.port'"
                  :rules="[
                    {
                      validator: validatePort,
                      trigger: 'blur',
                      message: '端口号必须在1到65535之间',
                    },
                  ]"
                >
                  <el-input
                    v-model="scope.row.port"
                    :size="$store.state.nextStack.viewSize.main"
                    :disabled="scope.row.protocol == 3"
                    :placeholder="
                      scope.row.protocol == 3
                        ? $t('form.all')
                        : $t('form.defaultAllPort')
                    "
                    class="w-52"
                    @input="changePort(addRuleData, scope.$index)"
                  />
                </el-form-item>
                <el-select
                  v-if="scope.row.protocol == 4"
                  v-model="scope.row.port"
                  class="w-52"
                  multiple
                  clearable
                  :size="$store.state.nextStack.viewSize.main"
                  :placeholder="$t('form.defaultAllPort')"
                  @change="changePortIcmp(addRuleData, scope.$index)"
                >
                  <el-option
                    v-for="(item, index) in $script.getICMP()"
                    :key="index"
                    :label="item.name"
                    :value="item.value"
                    >{{ item.name }}</el-option
                  >
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            prop="addressType"
            :label="$t('form.type')"
            width="100"
          >
            <template #default="scope">
              <el-select
                v-model="scope.row.addressType"
                :size="$store.state.nextStack.viewSize.main"
                :disabled="true"
              >
                <el-option key="IPv4" label="IPv4" :value="0" />
                <el-option key="IPv6" label="IPv6" :value="1" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column
            prop="addressRefType"
            :label="
              addruleType === 0
                ? $t('form.sourceAddress')
                : $t('form.destinationAddress')
            "
            width="180"
          >
            <template #header>
              {{
                addruleType === 0
                  ? $t("form.sourceAddress")
                  : $t("form.destinationAddress")
              }}

              <el-tooltip placement="top" effect="dark">
                <template #content>
                  <div class="w-220px">
                    <p>
                      {{
                        addruleType === 0
                          ? $t("form.sourceAddressTips")
                          : $t("form.destinationAddressTips")
                      }}
                    </p>
                    <ul>
                      <li>
                        {{ $t("form.destinationAddressTips1") }}
                      </li>
                      <li>
                        {{ $t("form.destinationAddressTips2") }}
                      </li>
                      <li>
                        {{ $t("form.destinationAddressTips3") }}
                      </li>
                      <li>
                        {{ $t("form.destinationAddressTips4") }}
                      </li>
                    </ul>
                  </div>
                </template>
                <span class="text-xs inline-block align-middle cursor-pointer">
                   <i class="el-icon-info" />
                </span>
              </el-tooltip>
            </template>
            <template #default="scope">
              <el-form-item
                class="mt-6"
                label=""
                :prop="'list.' + scope.$index + '.addressRef'"
                :rules="[
                  {
                    required: true,
                    validator: (rule, value, callback) => {
                      validateAddressRef(rule, value, callback, scope.$index);
                    },
                    trigger: 'change',
                  },
                  {
                    required: true,
                    validator: (rule, value, callback) => {
                      validateAddressRef(rule, value, callback, scope.$index);
                    },
                    trigger: 'blur',
                  },
                ]"
              >
                <el-select
                  v-model="scope.row.addressRefType"
                  :size="$store.state.nextStack.viewSize.main"
                  @change="changeAddressRefType(scope.row)"
                >
                  <el-option
                    key="cidr"
                    :label="$t('form.ipAddress')"
                    value="cidr"
                  />
                  <el-option
                    key="sgId"
                    :label="$t('form.securityGroup')"
                    value="sgId"
                  />
                  <!-- <el-option key="ipPoolId" label="IP地址组" value="ipPoolId" /> -->
                </el-select>
                <el-input
                  v-if="scope.row.addressRefType == 'cidr'"
                  v-model="scope.row.addressRef.cidr"
                  :size="$store.state.nextStack.viewSize.main"
                  placeholder="0.0.0.0/0"
                />
                <el-select
                  v-if="scope.row.addressRefType == 'sgId'"
                  v-model="scope.row.addressRef.sgId"
                  :size="$store.state.nextStack.viewSize.main"
                >
                  <el-option
                    v-for="(item, index) in sgsListData"
                    :key="index"
                    :label="item.name"
                    :value="item.sgId"
                  />
                </el-select>
              </el-form-item>
            </template>
          </el-table-column>
          <el-table-column
            prop="description"
            :label="$t('form.description')"
            width="160"
          >
            <template #default="scope">
              <el-input
                v-model="scope.row.description"
                :size="$store.state.nextStack.viewSize.main"
                autosize
                type="textarea"
                maxlength="255"
                show-word-limit
              />
            </template>
          </el-table-column>
          <el-table-column
            v-if="isAddruleType"
            prop="name"
            :label="$t('form.operation')"
            width="100"
          >
            <template #default="scope">
              <el-button
                :size="$store.state.nextStack.viewSize.main"
                type="text"
                @click="sgAddCopy(scope.row)"
                >{{ $t("form.copy") }}</el-button
              >
              <el-button
                :size="$store.state.nextStack.viewSize.main"
                type="text"
                :disabled="!(addRuleData.list.length > 1)"
                @click="sgAddDel(scope)"
                >{{ $t("form.delete") }}</el-button
              >
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            v-if="isAddruleType"
            type="text"
            :size="$store.state.nextStack.viewSize.main"
            class="float-left"
            @click="sgAddCopy(initRuleData[0])"
          >
            <!-- <i-ic:baseline-add></i-ic:baseline-add> -->
            {{ $t("form.createNewOneRule") }}
          </el-button>

          <el-button
            :size="$store.state.nextStack.viewSize.main"
            @click="handleClose()"
            >{{ $t("form.cancel") }}</el-button
          >
          <el-button
            v-if="isAddruleType"
            :size="$store.state.nextStack.viewSize.main"
            type="primary"
            :loading="loading"
            @click="toAddSg()"
            >{{ $t("form.confirm") }}</el-button
          >
          <el-button
            v-else
            :size="$store.state.nextStack.viewSize.main"
            type="primary"
            :loading="loading"
            @click="toEditSg()"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

import netmask from "netmask";
var Netmask = netmask.Netmask;
export default {
  props: {
    itemInfo: {
      type: Object,
      default: {},
    },
  },
  data() {
    const validatePriority = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputPriority")));
      } else {
        if (!/^\d+$/.test(value) || value < 1 || value > 100) {
          callback(new Error(this.$t("message.pleaseInputNumber1To100")));
        } else {
          callback();
        }
      }
    };
    const validateAddressRef = (rule, value, callback, index) => {
      if (this.addRuleData.list[index].addressRefType === "cidr") {
        if (this.addRuleData.list[index].addressRef.cidr === "") {
          callback(new Error(this.$t("message.pleaseInputCIDR")));
        } else {
          var cidr = this.addRuleData.list[index].addressRef.cidr;
          var ip = cidr.split("/")[0];
          const netBlock = new Netmask(cidr);
          const netBlock1 = new Netmask(netBlock.base);

          if (netBlock1.contains(ip)) {
            callback();
          } else {
            callback(new Error(this.$t("message.pleaseInputCorrectCIDR")));
          }
        }
      } else if (this.addRuleData.list[index].addressRefType === "sgId") {
        if (this.addRuleData.list[index].addressRef.sgId === "") {
          callback(new Error(this.$t("message.pleaseSelectSecurityGroup")));
        } else {
          callback();
        }
      } else {
        callback();
      }
    };
    return {
      validatePriority: validatePriority,
      validateAddressRef: validateAddressRef,
      loading: false,
      addruleDialog: false, // 添加规则弹窗
      addruleType: 0, // 添加规则 类型 in/out 0/1
      isAddruleType: true, // 规则 类型 add OR edit
      sgsListData: [], // 安全组列表
      initRuleData: [
        {
          // 添加规则表单
          description: "", // 描述
          priority: "1", // 优先级
          direction: 0, // 出入规则 in/out
          protocol: 0, // 协议端口 TCP/UDP/IP/ICMP 0/1/3/4
          port: "", // 端口
          addressType: 0, // 源地址类型 IPv4/IPv6 0/1
          addressRefType: "cidr",
          addressRef: {
            cidr: "0.0.0.0/0", // 源地址 0.0.0.0/0
            sgId: "", // 安全组ID
            // ipPoolId: '', //IP地址组
          },
          action: 1, // 拒绝/允许  drop/accept 0/1xz
        },
      ],
      addRuleData: {
        list: [],
      }, // 添加规则列表
    };
  },
  // 监听props下的itemInfo
  watch: {
    itemInfo: {
      handler(val) {
        if (val) {
          if (val.dialogVisible && val.isAdd) {
            this.isAddruleType = val.isAdd; // 规则弹窗为add
            this.addruleType = val.ruleType; // 打开添加规则弹窗
            this.initRuleData[0].direction = val.ruleType;
            this.addRuleData.list.push(
              JSON.parse(JSON.stringify(this.initRuleData[0]))
            );
            this.addruleDialog = val.dialogVisible; // 打开添加规则弹窗
          }
          if (val.dialogVisible && !val.isAdd) {
            this.isAddruleType = val.isAdd; // 规则弹窗为add
            this.addruleType = val.ruleType; // 打开添加规则弹窗
            this.addruleDialog = val.dialogVisible; // 打开添加规则弹窗
            this.addRuleData.list.push({
              // 添加规则表单
              id: this.itemInfo.item.ruleId,
              description: this.itemInfo.item.description, // 描述
              priority: this.itemInfo.item.priority, // 优先级
              direction: this.itemInfo.item.direction, // 出入规则 in/out
              protocol: this.itemInfo.item.protocol, // 协议端口 TCP/UDP/IP/ICMP 0/1/3/4
              port:
                this.itemInfo.item.protocol == 4
                  ? this.itemInfo.item.port == "all"
                    ? ["0"]
                    : this.itemInfo.item.port.split(",")
                  : this.itemInfo.item.port == "all"
                  ? ""
                  : this.itemInfo.item.port, // 端口
              addressType: this.itemInfo.item.addressType, // 源地址类型 IPv4/IPv6 0/1
              addressRefType: this.itemInfo.item.addressRef.cidr
                ? "cidr"
                : this.itemInfo.item.addressRef.sgId
                ? "sgId"
                : "",
              addressRef: {
                cidr:
                  this.itemInfo.item.addressRef &&
                  this.itemInfo.item.addressRef.cidr
                    ? this.itemInfo.item.addressRef.cidr
                    : "", // 源地址 0.0.0.0/0
                sgId:
                  this.itemInfo.item.addressRef &&
                  this.itemInfo.item.addressRef.sgId
                    ? this.itemInfo.item.addressRef.sgId
                    : "", // 安全组ID
                // ipPoolId: '', //IP地址组
              },
              action: this.itemInfo.item.action, // 拒绝/允许  drop/accept 0/1xz
            });
          }
        }
      },
      immediate: true,
    },
  },

  created() {},
  mounted() {
    this.getSgsList();
  },
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    validatePort(rule, value, callback) {
      if (value === "" || value === undefined || value === null) {
        // 如果值为空，则认为校验通过，不执行任何校验逻辑
        callback();
      } else {
        const port = parseInt(value, 10);
        if (port >= 1 && port <= 65535) {
          callback(); // 通过校验
        } else {
          callback(new Error(this.$t("message.portNumberBetween1And65535"))); // 校验失败，返回错误信息
        }
      }
    },
    handleClose() {
      this.resetForm();
      this.addruleDialog = false; // 关闭添加规则弹窗
    },
    resetForm() {
      this.addRuleData.list.splice(0, this.addRuleData.list.length);
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
    changePort(val, index) {
      // 过滤去掉特殊字符
      val.list[index].port = val.list[index].port.replace(/[^\d,-]/g, "");
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
    changeAddressRefType(item) {
      // 源地址改变
      var id = this.itemInfo.sgId;

      if (item.addressRefType == "cidr") {
        item.addressRef.cidr = "0.0.0.0/0";
        item.addressRef.sgId = "";
      }
      if (item.addressRefType == "sgId") {
        item.addressRef.cidr = "";
        item.addressRef.sgId = id;
      }
    },
    sgAddCopy(item) {
      // 添加规则复制一条
      this.addRuleData.list.push(JSON.parse(JSON.stringify(item)));
    },
    sgAddDel(item) {
      // 添加规则删除一条
      this.addRuleData.list.splice(item.$index, 1);
    },
    toEditSg() {
      // 确认修改规则
      var id = this.itemInfo.sgId;

      this.$refs.addRuleDataRef.validate(async (valid) => {
        if (valid) {
          this.loading = true;
          var data = JSON.parse(JSON.stringify(this.addRuleData.list));
          var ruleId = JSON.parse(JSON.stringify(data[0].id));
          data.filter((item) => {
            item.priority = item.priority * 1;
            if (item.protocol == 4) {
              item.port = item.port.toString();
            }
            delete item.addressRefType;
            delete item.id;
          });
          mainApi
            .sgsRulesEdit(data[0], id, ruleId)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.modifySuccess"),
                type: "success",
                duration: 2500,
              });
              this.handleClose(); // 初始化输入框 关闭弹窗
              this.$emit("addRuleClose");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    toAddSg() {
      // 确认添加规则
      var id = this.itemInfo.sgId;

      this.$refs.addRuleDataRef.validate(async (valid) => {
        if (valid) {
          this.loading = true;

          var data = JSON.parse(JSON.stringify(this.addRuleData.list));
          data.filter((item) => {
            item.priority = item.priority * 1;
            if (item.protocol == 4) {
              item.port = item.port.toString();
            }
            delete item.addressRefType;
          });
          mainApi
            .sgsRuleAdd({ createSgRules: data }, id)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.addSuccess"),
                type: "success",
                duration: 2500,
              });
              this.handleClose(); // 初始化输入框 关闭弹窗
              this.$emit("addRuleClose");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
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
  },
};
</script>

<style lang="scss" scoped>
.sgsAddPage {
}
</style>
