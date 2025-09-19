<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="addNatForm"
      :size="$store.state.nextStack.viewSize.main"
      :rules="rules"
      :model="form"
      label-width="120px"
    >
      <!-- <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>基本信息</span>
          </div>
        </template> -->
      <el-form-item>
        <el-alert
          :title="$t('form.portTips')"
          class="py-0"
          style="width: 560px"
          type="warning"
          :closable="false"
          show-icon
          size="mini"
        />
      </el-form-item>
      <el-form-item :label="$t('form.name') + ':'" prop="mapName">
        <el-input
          v-model="form.mapName"
          class="w-60"
          style="width: 215px"
          :placeholder="$t('form.pleaseInputName')"
        />
      </el-form-item>
      <!-- </el-card>

      <el-card class="!border-none mb-3">
        <template #header>
          <div class="">
            <span>高级配置</span>
          </div>
        </template> -->
      <el-form-item :label="$t('form.network') + ':'" prop="subnetId">
        <el-select
          v-model="vpcId"
          class="mr-2 w-60"
          :placeholder="$t('form.pleaseSelectVpcNetwork')"
          @change="vpcIdChange()"
        >
          <el-option
            v-for="(item, index) in vpcList"
            :key="index"
            :label="item.name + ' (' + item.cidr + ')'"
            :value="item.vpcId"
          />
        </el-select>
        <el-select
          v-model="form.subnetId"
          class="mr-2 w-60"
          :placeholder="$t('form.pleaseSelectSubnet')"
          :disabled="!vpcId"
          @change="subnetChange(form.subnetId)"
        >
          <template v-for="(item, index) in subnetsDataList">
            <el-option
              v-if="vpcId == item.vpcId"
              :key="index"
              :label="item.name + ' (' + item.cidr + ')'"
              :value="item.subnetId"
            />
          </template>
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('form.instanceType') + ':'" prop="portId">
        <el-select
          v-model="portIdStatus"
          class="mr-2 w-60"
          :placeholder="$t('form.pleaseSelectInstanceType')"
          @change="portIdChange()"
        >
          <!-- <el-option label="裸金属" :value="0" /> -->
          <el-option :label="$t('form.cloudHost')" :value="1" />
        </el-select>
        <el-select
          v-model="form.portId"
          class="mr-2 w-60"
          :placeholder="$t('form.pleaseSelectInstance')"
          :disabled="!form.subnetId"
        >
          <el-option
            v-for="(item, index) in bmsDataList"
            :key="index"
            :label="item.name"
            :value="item.portInfo.portId"
          />
        </el-select>
      </el-form-item>

      <el-form-item :label="$t('form.protocolType') + ':'" prop="oneToOne">
        <el-radio-group v-model="form.oneToOne" @change="protocolChange">
          <el-radio :label="true" :value="true">{{ $t("form.ip") }}</el-radio>
          <el-radio :label="false" :value="false">{{
            $t("form.tcpUdp")
          }}</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item :label="$t('form.eipAddress') + ':'" prop="eipId">
        <el-select
          v-model="form.eipId"
          class="w-60 mr-2"
          :placeholder="$t('form.pleaseSelectEipAddress')"
          :disabled="vpcId == ''"
          @change="eipChange(form.eipId)"
        >
          <template v-if="eipsDataloading">
            <el-option
              v-for="(item, index) in eipsDataList"
              :key="index"
              :label="item.publicIp || item.ipAddress"
              :value="item.eipId"
            />
          </template>
        </el-select>
        <el-button
          type="primary"
          :disabled="form.subnetId == ''"
          :size="$store.state.nextStack.viewSize.main"
          @click="applyEip"
          >{{ $t("form.applyEip") }}</el-button
        >
      </el-form-item>
      <el-form-item v-if="!form.oneToOne && form.eipId && form.portId">
        <span class="mr-4">{{ $t("form.eip") }}: {{ getEip(form.eipId) }}</span
        ><span
          >{{ $t("form.instanceIp") }}: {{ getInstancesIp(form.portId) }}</span
        >
      </el-form-item>
      <div v-if="!form.oneToOne">
        <el-form-item :label="$t('form.rule') + ':'">
          <div v-for="(item, index) in form.portMaps" :key="index">
            <div class="overflow-hidden">
              <el-form-item
                :label="$t('form.protocol') + ':'"
                class="w-40 float-left mb-3.5"
                label-width="70px"
              >
                <el-select
                  v-model="item.protocol"
                  size="mini"
                  class="m-0 ml-0 w-24"
                  :placeholder="$t('form.pleaseSelectProtocol')"
                  @change="changeprotocol(index)"
                >
                  <el-option key="TCP" :label="$t('form.tcp')" :value="0" />
                  <el-option key="UDP" :label="$t('form.udp')" :value="1" />
                </el-select>
              </el-form-item>
              <el-form-item
                :label="$t('form.publicPort') + ':'"
                class="w-48 float-left mb-3.5"
                label-width="110px"
                :prop="'portMaps.' + index + '.globalPort'"
                :rules="[
                  {
                    required: true,
                    validator: validatePort,
                    trigger: 'blur',
                  },
                  {
                    required: true,
                    validator: validatePort,
                    trigger: 'change',
                  },
                ]"
              >
                <el-input
                  v-model="item.globalPort"
                  class="w-24"
                  size="mini"
                  :placeholder="$t('form.pleaseInput')"
                />
              </el-form-item>
              <el-form-item
                :label="$t('form.localPort') + ':'"
                class="w-48 float-left mb-3.5"
                label-width="150px"
                :prop="'portMaps.' + index + '.localPort'"
                :rules="[
                  {
                    required: true,
                    validator: validatePort2,
                    trigger: 'blur',
                  },
                  {
                    required: true,
                    validator: validatePort2,
                    trigger: 'change',
                  },
                ]"
              >
                <el-input
                  v-model="item.localPort"
                  class="w-24"
                  size="mini"
                  :placeholder="$t('form.pleaseInput')"
                />
              </el-form-item>
              <el-form-item
                label=" "
                class="w-16 float-left ml-14 mb-3.5"
                label-width="0"
              >
                <el-button
                  type="danger"
                  size="mini"
                  @click="delProtocol(index)"
                  >{{ $t("form.delete") }}</el-button
                >
              </el-form-item>
            </div>
          </div>
        </el-form-item>
        <el-form-item label=" ">
          <el-button type="primary" @click="addProtocol">{{
            $t("form.addProtocol")
          }}</el-button>
        </el-form-item>
      </div>
      <!-- </el-card>
      <el-card class="!border-none mb-3"> -->
      <div class="text item text-right">
        <el-button
          type="primary"
          size="small"
          :loading="addLoading"
          @click="toNatAdd"
          >{{ $t("form.createImmediately") }}</el-button
        >
      </div>
      <!-- </el-card> -->
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

export default {
  data() {
    return {
      loading: false,
      addLoading: false,
      vpcList: [],
      subnetsDataList: [],
      bmsDataList: [],
      eipsDataList: [],
      eipsDataloading: false,
      portsCheckTCPList: [], // TCP端口检查列表
      portsCheckUDPList: [], // UDP端口检查列表
      vpcId: "",
      portIdStatus: 1,
      form: {
        // 表单
        mapName: "",
        subnetId: "",
        eipId: "",
        addressType: 0,
        portId: "",
        portMaps: [
          { globalPort: "", localPort: "", protocol: 0 },
          // { globalPort: 53, localPort: 53, protocol: 1 },
        ],
        oneToOne: true,
        bandwidth: "",
      },
      rules: {
        mapName: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        subnetId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectVPCNetworkAndSubnet"),
            trigger: "change",
          },
        ],
        portId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectVPCNetworkAndSubnetInstance"),
            trigger: "change",
          },
        ],
        eipId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectEipAddress"),
            trigger: "change",
          },
        ],
      },
    };
  },

  created() {
    this.getVpcList(); // VPC列表
    this.getSubNetList(); // 子网列表
    this.getBmsList(); // 获取数据
  },
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    getInstancesIp(portId) {
      // 获取实例IP
      var data = this.bmsDataList.filter((item) => {
        return item.portInfo.portId == portId;
      });
      if (data.length > 0) {
        return data[0].portInfo.ipAddress;
      } else {
        return "";
      }
    },
    getEip(eipId) {
      // 获取EIP
      var data = this.eipsDataList.filter((item) => {
        return item.eipId == eipId;
      });
      if (data.length > 0) {
        return data[0].publicIp || data[0].ipAddress;
      } else {
        return "";
      }
    },
    changeprotocol(index) {
      this.$refs["addNatForm"].validateField(
        "portMaps." + index + ".globalPort"
      );
      this.$refs["addNatForm"].validateField(
        "portMaps." + index + ".localPort"
      );
    },
    validatePort(rule, value, callback) {
      // 端口是否被占用校验
      var isnumber = /^\d+$/.test(value);
      var checkPortData = [];
      var checkPortData2 = [];
      var tcpPort = [];
      var udpPort = [];
      this.form.portMaps.forEach((item) => {
        if (item.protocol == 0) {
          tcpPort.push(item.globalPort * 1);
        } else {
          udpPort.push(item.globalPort * 1);
        }
      });
      if (this.form.portMaps[rule.field.split(".")[1]].protocol === 0) {
        checkPortData = [...this.portsCheckTCPList];
        checkPortData2 = tcpPort.filter((item) => {
          return item * 1 == value * 1;
        });
      } else if (this.form.portMaps[rule.field.split(".")[1]].protocol === 1) {
        checkPortData = [...this.portsCheckUDPList];
        checkPortData2 = udpPort.filter((item) => {
          return item * 1 == value * 1;
        });
      } else {
        checkPortData = [];
        checkPortData2 = [];
      }

      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputPort")));
      } else if (value < 1 || value > 65535 || !isnumber) {
        callback(new Error(this.$t("message.pleaseInputCorrectPort")));
      } else if (checkPortData.includes(value * 1)) {
        callback(new Error(this.$t("message.portHasBeenOccupied")));
      } else if (checkPortData2.length > 1) {
        callback(new Error(this.$t("message.samePortAlreadyExists")));
      } else {
        callback();
      }
    },
    validatePort2(rule, value, callback) {
      // 端口是否被占用校验
      var isnumber = /^\d+$/.test(value);
      var checkPortData2 = [];
      var tcpPort = [];
      var udpPort = [];
      this.form.portMaps.forEach((item) => {
        if (item.protocol == 0) {
          tcpPort.push(item.localPort * 1);
        } else {
          udpPort.push(item.localPort * 1);
        }
      });
      if (this.form.portMaps[rule.field.split(".")[1]].protocol === 0) {
        checkPortData2 = tcpPort.filter((item) => {
          return item * 1 == value * 1;
        });
      } else if (this.form.portMaps[rule.field.split(".")[1]].protocol === 1) {
        checkPortData2 = udpPort.filter((item) => {
          return item * 1 == value * 1;
        });
      } else {
        checkPortData2 = [];
      }

      if (value === "") {
        callback(new Error(this.$t("message.pleaseInputPort")));
      } else if (value < 1 || value > 65535 || !isnumber) {
        callback(new Error(this.$t("message.pleaseInputCorrectPort")));
      } else if (checkPortData2.length > 1) {
        callback(new Error(this.$t("message.samePortAlreadyExists")));
      } else {
        callback();
      }
    },
    applyEip() {
      // 申请EIP
      this.loading = true;
      mainApi
        .eipsAllocate(this.vpcId)
        .then((res) => {
          this.loading = false;
          this.$notify({
            title: this.$t("message.applySuccess"),
            type: "success",
            duration: 2500,
          });
          this.geteipsList();
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    toNatAdd() {
      // 立即创建
      var data = JSON.parse(JSON.stringify(this.form));
      if (data.oneToOne) {
        data.portMaps = [];
      } else {
        data.portMaps.forEach((item) => {
          item.globalPort = item.globalPort * 1;
          item.localPort = item.localPort * 1;
        });
      }

      this.addLoading = true;
      this.$refs.addNatForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .portMapAdd(data)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startCreate"),
                type: "success",
                duration: 2500,
              });
              this.addLoading = false;
              this.$emit("close");
            })
            .catch((error) => {
              this.addLoading = false;
            });
        } else {
          this.addLoading = false;
        }
      });
    },
    delProtocol(index) {
      // 删除协议
      if (this.form.portMaps.length > 1) {
        this.form.portMaps.splice(index, 1);
      } else {
        this.form.portMaps[0].globalPort = "";
        this.form.portMaps[0].localPort = "";
        this.form.portMaps[0].protocol = 0;
      }
    },
    addProtocol() {
      // 新增协议
      this.form.portMaps.push({ globalPort: "", localPort: "", protocol: 0 });
    },
    eipChange(e) {
      // EIP变更
      this.getPortsCheck(e);
    },
    getPortsCheck(e) {
      // 获取端口检查
      mainApi.portsCheck({ protocol: 0 }, e).then((res) => {
        this.portsCheckTCPList = res.ports;
      });
      mainApi.portsCheck({ protocol: 1 }, e).then((res) => {
        this.portsCheckUDPList = res.ports;
      });
    },
    getVpcList() {
      // VPC列表
      mainApi
        .vpcList({ vpc_phase: 1 })
        .then((res) => {
          this.vpcList = res.vpcs;
        })
        .catch((error) => {});
    },
    getSubNetList() {
      // 子网列表

      mainApi
        .subnetsList()
        .then((res) => {
          this.subnetsDataList = res.subnets;
        })
        .catch((error) => {});
    },
    portIdChange() {
      this.form.portId = "";
      this.getBmsList();
    },
    vpcIdChange() {
      // vpc 改变
      this.form.subnetId = "";
      this.form.portId = "";
      this.geteipsList(); // 获取数据
    },
    subnetChange(e) {
      // 子网变更
      this.form.portId = "";
      if (e != "") {
        this.getBmsList();
      }
    },
    getBmsList() {
      // 服务器列表
      if (this.form.subnetId == "") {
        return;
      }
      if (this.portIdStatus === 0) {
        mainApi
          .bmsList({
            port_id_is_null: false,
            eipMap_is_using: false,
            subnet_id: this.form.subnetId,
          })
          .then((res) => {
            this.bmsDataList = res.instances;
          })
          .catch((error) => {});
      } else {
        mainApi
          .vmsInstabcesList({
            port_id_is_null: false,
            eipMap_is_using: false,
            subnet_id: this.form.subnetId,
          })
          .then((res) => {
            this.bmsDataList = res.vmInstancesInfo;
          })
          .catch((error) => {});
      }
    },
    geteipsList() {
      this.eipsDataloading = false;
      // 获取数据
      mainApi
        .eipsList({
          oneToOne: this.form.oneToOne,
          vpc_id: this.vpcId,
          page_size: 99999,
          page_num: 1,
        })
        .then((res) => {
          this.eipsDataList = res.eips;
          this.eipsDataloading = true;
        })
        .catch((error) => {
          this.eipsDataloading = true;
        });
    },
    // 监听协议类型
    protocolChange() {
      this.form.eipId = "";
      this.geteipsList();
    },
    resetForm() {
      // 重置
      this.form.portMaps = [{ globalPort: "", localPort: "", protocol: 0 }];
      this.portIdStatus = 1;
      this.vpcId = "";
      setTimeout(() => {
        this.$refs.addNatForm.resetFields();
      });
    },
  },
};
</script>

<style lang="scss" scoped></style>
