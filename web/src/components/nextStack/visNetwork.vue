<template>
  <div class="networkPage">
    <div
      id="mynetwork"
      v-loading="loading"
      :element-loading-text="$t('domain.loading')"
    />
    <el-drawer :visible.sync="drawerStatus" :size="'60%'" direction="rtl">
      <template #title>
        <h4>
          {{
            networkLevel == 2
              ? $t("titleAndText.subnetDetail")
              : networkLevel == 3
              ? $t("titleAndText.bareMetalInstanceDetail")
              : networkLevel == 33
              ? $t("titleAndText.instanceDetail")
              : $t("titleAndText.detail")
          }}
        </h4>
      </template>
      <template #default>
        <div v-if="networkLevel == 2">
          <el-form :model="subnetForm" label-width="160px" size="small">
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("titleAndText.vpcInfo") }}</span>
                </div>
              </template>
              <div class="text item">
                <el-form-item :label="$t('form.vpcNetwork') + ':'">
                  <span>{{ subnetForm.vpcName || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.vpcId') + ':'">
                  <span>
                    {{ subnetForm.vpcId }}
                  </span>
                </el-form-item>
              </div>
            </el-card>
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("titleAndText.basicInfo") }}</span>
                </div>
              </template>
              <div class="text item">
                <el-form-item :label="$t('form.subnetName') + ':'">
                  <span>{{ subnetForm.name || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.subnetId') + ':'">
                  <span>{{ subnetForm.subnetId || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.subnetCidr') + ':'">
                  <span>{{ subnetForm.cidr || "-" }}</span>
                </el-form-item>
              </div>
            </el-card>
          </el-form>
        </div>
        <div v-if="networkLevel == 3">
          <el-form :model="bmsForm" label-width="120px" size="small">
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("titleAndText.basicConfig") }}</span>
                </div>
              </template>
              <div class="text item">
                <el-form-item :label="$t('form.name') + ':'">
                  <span>{{ bmsForm.name || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.cpuArchitecture') + ':'">
                  <span>{{ bmsForm.a || "X86(当前固定值)" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.flavorSelect') + ':'">
                  <span>{{ bmsForm.a || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.os') + ':'">
                  <span>{{ bmsForm.imageName || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.status') + ':'">
                  <span>{{
                    filtersFun.getBmsStatus(bmsForm.phaseStatus, "status")
                  }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.createTime') + ':'">
                  <span>{{ bmsForm.createTime || "-" }}</span>
                </el-form-item>
              </div>
            </el-card>
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("titleAndText.networkConfig") }}</span>
                </div>
              </template>
              <div class="text item">
                <el-form-item :label="$t('form.vpcNetwork') + ':'">
                  <span>
                    <router-link
                      :to="'/vpc/' + bmsForm.vpcId"
                      class="text-blue-400"
                      >{{ bmsForm.vpcName }}</router-link
                    >
                  </span>
                </el-form-item>
                <el-form-item :label="$t('form.subnet') + ':'">
                  <span>
                    <router-link
                      :to="'/subnet/' + bmsForm.subnetId"
                      class="text-blue-400"
                      >{{ bmsForm.subnetName }}</router-link
                    >
                  </span>
                </el-form-item>
                <el-form-item :label="$t('form.ip') + ':'">
                  <span>{{ bmsForm.ip || "-" }}</span>
                </el-form-item>
              </div>
            </el-card>
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("titleAndText.advancedConfig") }}</span>
                </div>
              </template>
              <div class="text item">
                <el-form-item :label="$t('form.hostname') + ':'">
                  <span>{{ bmsForm.hostname || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.sysUsername') + ':'">
                  <span>{{ bmsForm.sysUsername || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.loginCredential') + ':'">
                  <span>{{
                    bmsForm.pubkeyId ? $t("form.keyPair") : $t("form.password")
                  }}</span>
                </el-form-item>

                <el-form-item
                  v-if="bmsForm.pubkeyId"
                  :label="$t('form.keyPair') + ':'"
                >
                  <span>
                    <router-link
                      :to="'/publicKey/' + bmsForm.pubkeyId"
                      class="text-blue-400"
                      >{{ bmsForm.pubkeyId }}</router-link
                    >
                  </span>
                </el-form-item>
              </div>
            </el-card>
          </el-form>
        </div>
        <div v-if="networkLevel == 33">
          <el-form
            :model="vmForm"
            label-width="120px"
            size="small"
            label-position="left"
          >
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("titleAndText.basicConfig") }}</span>
                </div>
              </template>
              <el-row class="mt-4">
                <el-col :span="12">
                  <el-form-item :label="$t('form.name') + ':'">
                    <router-link
                      :to="'/nextStack/vm/detail/' + vmForm.instanceId"
                      class="text-blue-400"
                      >{{ vmForm.name || "-" }}</router-link
                    >
                  </el-form-item>
                  <el-form-item :label="$t('form.id') + ':'">
                    <span>{{ vmForm.instanceId || "-" }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.desc') + ':'">
                    <span>{{ vmForm.desc || "-" }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.imageOsType') + ':'">
                    <span v-if="vmForm.imageOsType == 0">Linux</span>
                    <span v-else-if="vmForm.imageOsType == 1">Windows</span>
                    <span v-else>-</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.imageOs') + ':'">
                    <span>{{ vmForm.imageName || "-" }}</span>
                  </el-form-item>
                  <el-form-item
                    v-if="kind == 0 || kind == 1"
                    :label="$t('form.hypervisorNode') + ':'"
                  >
                    <span>
                      <router-link
                        :to="
                          '/nextStack/hypervisorNodes/detail/' +
                          vmForm.hypervisorNodeId
                        "
                        class="text-blue-400"
                        >{{ vmForm.hypervisorNodeName || "-" }}</router-link
                      >
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.status') + ':'">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="filtersFun.getVmStatus(vmForm.phaseStatus, 'tag')"
                      >{{
                        filtersFun.getVmStatus(vmForm.phaseStatus, "status")
                      }}</el-tag
                    >
                  </el-form-item>
                  <el-form-item :label="$t('form.modifyTime') + ':'">
                    <span>{{ vmForm.updateTime || "-" }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('form.flavorName') + ':'">
                    <span v-if="kind == 0 || kind == 1">
                      <router-link
                        :to="'/nextStack/flavor/detail/' + vmForm.flavorId"
                        class="text-blue-400"
                        >{{ vmForm.flavorName || "-" }}</router-link
                      >
                    </span>
                    <span v-if="kind != 0 && kind != 1">{{
                      vmForm.flavorName || "-"
                    }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.cpu') + ':'">
                    <span> {{ vmForm.cpu || "-" }} {{ $t("form.core") }} </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.mem') + ':'">
                    <span> {{ vmForm.mem || "-" }}GB </span>
                  </el-form-item>
                  <!-- <el-form-item label="根盘：">
                    <span> {{ vmForm.rootDisk || "-" }}GB </span>
                  </el-form-item> -->
                  <el-form-item :label="$t('form.gpu') + ':'">
                    <span>
                      {{
                        vmForm.pciInfos && vmForm.pciInfos.length > 0
                          ? vmForm.pciInfos.length
                          : $t("form.unMounted")
                      }}
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.ib') + ':'">
                    <span v-if="vmForm.ib">
                      <i
                        style="color: rgb(103, 194, 58)"
                        class="el-icon-success"
                      />
                    </span>
                    <span v-if="!vmForm.ib">-</span>
                  </el-form-item>

                  <el-form-item :label="$t('form.createTime') + ':'">
                    <span>{{ vmForm.createTime || "-" }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-card>
          </el-form>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import i18n from "@/utils/i18n";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import { DataSet, Network } from "vis-network/standalone/esm/vis-network";
import imgInternet from "@/assets/nextStack/visNetwork/internet.png";
import imgRoute from "@/assets/nextStack/visNetwork/route.png";
import imgSubnet from "@/assets/nextStack/visNetwork/subnet.png";
import imgInstance from "@/assets/nextStack/visNetwork/instance.png";
import imgVm from "@/assets/nextStack/visNetwork/vm.png";

export default {
  props: {
    id: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      filtersFun: filtersFun,
      loading: false,
      drawerStatus: false,
      networkLevel: 0,
      nodes: [],
      edges: [],
      network: null,
      subnetForm: {},
      bmsForm: {},
      vmForm: {},
      topologyData: {},
    };
  },

  created() {},
  mounted() {
    this.getTopology();
  },
  computed: {
    ...mapGetters(["kind"]),
  },
  watch: {
    // id: {
    //   handler (val) {
    //     if (val) {
    //       this.getTopology();
    //     }
    //   },
    //   immediate: true,
    // },
  },
  methods: {
    // 判断计费方式是否即将过期和过期
    isExpired(data) {
      const currentDate = new Date();
      const specifiedTime = new Date(data);
      const timeDifference = specifiedTime.getTime() - currentDate.getTime();
      const daysDifference = timeDifference / (1000 * 3600 * 24);
      if (currentDate.getTime() > specifiedTime.getTime()) {
        return 0;
      } else if (currentDate.getTime() < specifiedTime.getTime()) {
        if (daysDifference > 7) {
          return 1;
        } else {
          return 2;
        }
      } else {
        return 1;
      }
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
    destroy() {
      if (this.network !== null) {
        this.network.destroy();
        this.network = null;
      }
    },
    getTopology() {
      // VPC列表
      this.loading = true;
      mainApi
        .topology(this.id)
        .then((res) => {
          this.loading = false;

          res.subnetTopologies.forEach((subnet) => {
            this.nodes.push({
              id: subnet.subnetId,
              label: subnet.subnetName + "\n" + subnet.cidr,
              level: 2,
              image: imgSubnet,
              shape: "image",
            });

            this.edges.push({
              from: "route",
              to: subnet.subnetId,
            });
            this.topologyData[subnet.subnetId] = {
              level: 2,
              id: subnet.subnetId,
            };

            if (subnet.instanceInfos && subnet.instanceInfos.length > 0) {
              subnet.instanceInfos.forEach((instance) => {
                if (this.nodes.map((v) => v.id).includes(instance.instanceId)) {
                } else {
                  this.nodes.push({
                    id: instance.instanceId,
                    vm: instance.vm,
                    label:
                      instance.name + (instance.ip ? "\n" + instance.ip : ""),
                    level: 3,
                    image: instance.vm ? imgVm : imgInstance,
                    shape: "image",
                  });
                }

                this.edges.push({
                  from: subnet.subnetId,
                  to: instance.instanceId,
                });
                this.topologyData[instance.instanceId] = {
                  level: 3,
                  vm: instance.vm,
                  id: instance.instanceId,
                };
              });
            }
          });
          this.draw();
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    draw() {
      this.destroy();

      this.nodes.push({
        id: "internet",
        label: "internet",
        level: 0,
        image: imgInternet,
        shape: "image",
      });
      this.nodes.push({
        id: "route",
        label: i18n.t("other.route"),
        level: 1,
        image: imgRoute,
        shape: "image",
      });
      this.edges.push({
        from: "internet",
        to: "route",
      });

      // create a network
      var container = document.getElementById("mynetwork");
      var data = {
        nodes: this.nodes,
        edges: this.edges,
      };

      var options = {
        nodes: {
          // 节点
          size: 20, // 图标大小
          color: {
            border: "#1296db",
          },
        },
        edges: {
          // 连接线
          smooth: {
            type: "cubicBezier",
            forceDirection: "horizontal", // 横向排列
            roundness: 0.8, // 连接线弯曲度
          },
          arrows: {
            // 箭头
            to: {
              enabled: false,
              type: "arrow", // 箭头指向
            },
          },
          length: 50, // 连接线长度
        },

        layout: {
          hierarchical: {
            // 层级布局
            direction: "LR",
          },
        },
        physics: true, // 物理态 保持顺序整洁
      };
      this.network = new Network(container, data, options);
      this.network.on("click", (params) => {
        this.getDetail(this.topologyData[params.nodes[0]]);
      });
    },
    getDetail(item) {
      if (item.level === 2) {
        // 获取子网详情
        mainApi.subnetsDetail(item.id).then((res) => {
          this.subnetForm = res;
          this.networkLevel = item.level;
          this.drawerStatus = true;
        });
      } else if (item.level === 3) {
        // 获取实例详情
        if (item.vm) {
          mainApi.vmsInstabcesDetail(item.id).then((res) => {
            this.vmForm = res;
            this.networkLevel = 33;
            this.drawerStatus = true;
          });
        } else {
          mainApi.bmsDetail(item.id).then((res) => {
            this.bmsForm = res;
            this.networkLevel = 3;
            this.drawerStatus = true;
          });
        }
      }
    },
    goSubnet() {
      this.$router.push("/subNetAdd");
    },
    goBms() {
      this.$router.push("/bmsAdd");
    },
    goVm() {
      this.$router.push("/vmAdd");
    },
  },
};
</script>

<style lang="scss" scpoed>
.networkPage {
  #mynetwork {
    width: 100%;
    height: 600px;
    margin: 0 auto;
  }
}
</style>
