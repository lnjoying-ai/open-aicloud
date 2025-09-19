<template>
  <div class="bmsAddPage h-full">
    <el-form
      ref="editPortEipPoolForm"
      v-loading="detailLoading"
      :size="$store.state.nextStack.viewSize.main"
      :model="form"
      :rules="rules"
      label-width="110px"
      :element-loading-text="$t('domain.loading')"
    >
      <div class="text item">
        <el-form-item :label="$t('form.name') + ':'">
          {{ form.mapName }}
        </el-form-item>
        <el-form-item :label="$t('form.cloudServer') + ':'">
          {{ form.instanceName }}
          <span v-if="form.insideIp">({{ form.insideIp }})</span>
        </el-form-item>

        <el-form-item :label="$t('form.protocolType') + ':'">
          {{ form.oneToOne ? "IP" : "TCP/UDP" }}
        </el-form-item>
        <el-form-item :label="$t('form.eipAddress') + ':'">
          {{ form.eipAddress }}
        </el-form-item>
        <div>
          <el-form-item :label="$t('form.rule') + ':'">
            <div v-for="(item, index) in form.portMaps" :key="index">
              <div class="overflow-hidden">
                <el-form-item
                  :label="$t('form.protocol')"
                  class="w-40 float-left mb-3.5"
                  label-width="42px"
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
                  label-width="80px"
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
                  :label="$t('form.privatePort') + ':'"
                  class="w-48 float-left mb-3.5"
                  label-width="80px"
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
                  class="w-16 float-left ml-2 mb-3.5"
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
      </div>
      <div class="text item text-right">
        <el-button
          type="primary"
          :loading="loading"
          size="small"
          @click="toSnapsEdit()"
          >{{ $t("form.update") }}</el-button
        >
      </div>
    </el-form>
  </div>
</template>

<script>
import { mapGetters } from "vuex";

import mainApi from "@/api/nextStack/mainApi";

export default {
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      detailLoading: false,
      loading: false,
      portsCheckTCPList: [], // TCP端口检查列表
      portsCheckUDPList: [], // UDP端口检查列表
      form: {},
      rules: {},
    };
  },

  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },
  watch: {
    id: {
      handler(val) {
        if (val) {
          this.getDetail();
        }
      },
      immediate: true,
    },
  },
  methods: {
    changeprotocol(index) {
      this.$refs["editPortEipPoolForm"].validateField(
        "portMaps." + index + ".globalPort"
      );
      this.$refs["editPortEipPoolForm"].validateField(
        "portMaps." + index + ".localPort"
      );
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
    toSnapsEdit() {
      this.loading = true;
      this.$refs.editPortEipPoolForm.validate(async (valid) => {
        if (valid) {
          var data = [];
          this.form.portMaps.forEach((item) => {
            data.push({
              globalPort: item.globalPort * 1,
              localPort: item.localPort * 1,
              protocol: item.protocol,
            });
          });
          mainApi
            .eipsPortEdit(this.id, {
              portMaps: data,
            })
            .then((res) => {
              this.$notify({
                title: this.$t("message.modifySuccess"),
                type: "success",
                duration: 2500,
              });
              this.loading = false;
              this.$emit("close");
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    getPortsCheck(e) {
      // 获取端口检查
      var tcpPort = [];
      var udpPort = [];
      e.portMaps.forEach((item) => {
        if (item.protocol == 1) {
          udpPort.push(item.globalPort);
        } else {
          tcpPort.push(item.globalPort);
        }
      });
      mainApi.portsCheck({ protocol: 0 }, e.eipId).then((res) => {
        // 从 res.ports 中删除 tcpPort
        this.portsCheckTCPList = res.ports.filter((item) => {
          return !tcpPort.includes(item);
        });
      });
      mainApi.portsCheck({ protocol: 1 }, e.eipId).then((res) => {
        // 从 res.ports 中删除 udpPort
        this.portsCheckUDPList = res.ports.filter((item) => {
          return !udpPort.includes(item);
        });
      });
    },
    getDetail() {
      // 获取详情
      this.detailLoading = true;
      mainApi
        .portMapDetail(this.id)
        .then((res) => {
          this.getPortsCheck(res);
          this.form = res;
          this.detailLoading = false;
        })
        .catch((error) => {
          this.detailLoading = false;
        });
    },
    validatePort(rule, value, callback) {
      // 端口是否被占用校验
      var isnumber = /^\d+$/.test(value);
      var checkPortData = [];
      var checkPortData2 = [];
      var tcpPort = [];
      var udpPort = [];
      console.log(this.form.portMaps);
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
  },
};
</script>

<style lang="scss" scoped>
.bmsAddPage {
}
</style>
