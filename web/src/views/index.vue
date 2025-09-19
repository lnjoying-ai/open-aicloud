<template>
  <div class="-mt-2">
    <div class="-mx-2">
      <el-row>
        <el-col :span="16" class="pt-2 px-2">
          <el-row :gutter="10">
            <el-col :span="6">
              <el-card class="mb-2">
                <div slot="header" class="clearfix">
                  <span class="font-bold">{{
                    $t("titleAndText.multiCloudResources")
                  }}</span>
                </div>
                <div style="margin: -20px 0px -5px">
                  <div class="text-center pt-5 pb-0">
                    <i
                      class="iconfont icon-duoyun-xian text-blue-500 text-3xl mr-4"
                    />
                    <span class="text-3xl font-bold">{{
                      cloudsMain.total_num || 0
                    }}</span>
                  </div>
                  <ul class="border-transparen" style="padding-top: 3rem">
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-green-500"
                        />
                        {{ $t("statusAndType.healthy") }}
                      </span>
                      <span class="float-right text-base">{{
                        cloudsMain.health || 0
                      }}</span>
                    </li>
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-red-500"
                        />
                        {{ $t("statusAndType.unhealthy") }}
                      </span>
                      <span class="float-right text-base">{{
                        cloudsMain.unhealth || 0
                      }}</span>
                    </li>
                    <li class="overflow-hidden leading-5">
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-gray-500"
                        />
                        {{ $t("statusAndType.unknown") }}
                      </span>
                      <span class="float-right text-base">{{
                        cloudsMain.unknown || 0
                      }}</span>
                    </li>
                  </ul>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="mb-2">
                <div slot="header" class="clearfix">
                  <span class="font-bold">{{
                    $t("titleAndText.cloudHost")
                  }}</span>
                </div>
                <div style="margin: -20px 0px -5px">
                  <div class="text-center pt-5">
                    <i
                      class="iconfont icon-computer text-blue-500 text-3xl mr-4"
                    />
                    <span class="text-3xl font-bold">{{
                      instanceMain.total_num || 0
                    }}</span>
                  </div>
                  <ul style="padding-top: 3rem">
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-green-500"
                        />
                        {{ $t("statusAndType.running") }}
                      </span>
                      <span class="float-right text-base">{{
                        instanceMain.active || 0
                      }}</span>
                    </li>
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-yellow-500"
                        />
                        {{ $t("statusAndType.paused") }}
                      </span>
                      <span class="float-right text-base">{{
                        instanceMain.paused || 0
                      }}</span>
                    </li>
                    <li class="overflow-hidden leading-5">
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-red-500"
                        />
                        {{ $t("statusAndType.poweroff") }}
                      </span>
                      <span class="float-right text-base">{{
                        instanceMain.shutoff || 0
                      }}</span>
                    </li>
                    <!-- <li class="overflow-hidden leading-5">
                      <span class="float-left text-sm leading-5">
                        <span class="inline-block w-2 h-2 rounded-full bg-gray-500"></span>
                        未知
                      </span>
                      <span class="float-right text-base">{{
            instanceMain.unknown || 0
          }}</span>
                    </li> -->
                  </ul>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="mb-2">
                <div slot="header" class="clearfix">
                  <span class="font-bold">{{
                    $t("titleAndText.containerNode")
                  }}</span>
                </div>
                <div style="margin: -20px 0px -5px">
                  <div class="text-center pt-5 pb-0.5">
                    <i
                      class="iconfont icon-jiedianguanli_1 text-blue-500 text-3xl mr-4"
                    />
                    <span class="text-3xl font-bold">{{
                      nodeMain.total_num || 0
                    }}</span>
                  </div>
                  <ul class="border-transparent" style="padding-top: 2.875rem">
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-green-500"
                        />
                        {{ $t("statusAndType.online") }}
                      </span>
                      <span class="float-right text-base">{{
                        nodeMain.online || 0
                      }}</span>
                    </li>
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-red-500"
                        />
                        {{ $t("statusAndType.offline") }}
                      </span>
                      <span class="float-right text-base">{{
                        nodeMain.offline || 0
                      }}</span>
                    </li>
                    <li class="overflow-hidden leading-5">
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-gray-500"
                        />
                        {{ $t("statusAndType.unknown") }}
                      </span>
                      <span class="float-right text-base">{{
                        nodeMain.unknown || 0
                      }}</span>
                    </li>
                  </ul>
                </div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card class="mb-2">
                <div slot="header" class="clearfix">
                  <span class="font-bold">{{
                    $t("titleAndText.clusterOrchestration")
                  }}</span>
                </div>
                <div style="margin: -20px 0px -5px">
                  <div class="text-center pt-5 pb-3.5">
                    <i
                      class="iconfont icon-jiqun1 text-blue-500 text-3xl mr-4"
                    />
                    <span class="text-3xl font-bold">{{
                      clusterMain.k8s_num + clusterMain.k3s_num || 0
                    }}</span>
                  </div>
                  <ul style="padding-top: 4.2rem">
                    <li
                      class="overflow-hidden leading-5 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-blue-500"
                        />
                        {{ $t("statusAndType.k8sCluster") }}
                      </span>
                      <span class="float-right text-base">{{
                        clusterMain.k8s_num || 0
                      }}</span>
                    </li>
                    <li class="overflow-hidden leading-5">
                      <span class="float-left text-sm leading-5">
                        <span
                          class="inline-block w-2 h-2 rounded-full bg-yellow-300"
                        />
                        {{ $t("statusAndType.k3sCluster") }}
                      </span>
                      <span class="float-right text-base">{{
                        clusterMain.k3s_num || 0
                      }}</span>
                    </li>
                  </ul>
                </div>
              </el-card>
            </el-col>
          </el-row>

          <el-card class="mb-2 productRecommendation">
            <div slot="header" class="clearfix">
              <span class="font-bold">{{
                $t("titleAndText.productRecommendation")
              }}</span>
            </div>
            <div style="height: calc(27rem - 38px)">
              <el-tabs v-model="productTabs" style="margin: -20px 0px 0px 0px">
                <el-tab-pane
                  :label="$t('titleAndText.containerService')"
                  name="first"
                >
                  <div class="pt-4 pl-8 lb-4 overflow-hidden">
                    <div class="float-left w-7/12">
                      <h5 class="font-bold">
                        {{ $t("titleAndText.containerServiceDescription") }}
                      </h5>
                      <ul class="overflow-hidden py-6">
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.kernelSize") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            "< 10 MB"
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.deploymentTime") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            "< 1 s"
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.containerInstance") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            $t("table.10millionPlus")
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.deviceCompatibility") }}</span
                          >
                          <span class="block leading-loose font-bold"
                            >X86/ARM</span
                          >
                        </li>
                      </ul>
                      <span
                        class="w-24 mb-6 text-sm inline-block bg-blue-500 rounded leading-loose text-center text-white cursor-pointer"
                        @click="handleRouterClick('container')"
                        >{{ $t("button.startUsing") }}</span
                      >
                      <div class="overflow-hidden">
                        <span
                          class="inline-block float-left text-sm leading-loose text-gray-500"
                          >{{
                            $t("titleAndText.ourCooperativeCustomers")
                          }}</span
                        >
                        <ul class="overflow-hidden">
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/zhaoshang.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/jingdong.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/wanda.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div class="float-right pl-6 w-5/12">
                      <img src="@/assets/images/index/product2.png" alt="" />
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane
                  :label="$t('titleAndText.edgeTurbo')"
                  name="second"
                >
                  <div class="pt-4 pl-8 lb-4 overflow-hidden">
                    <div class="float-left w-7/12">
                      <h5 class="font-bold">
                        {{ $t("titleAndText.edgeTurboDescription") }}
                      </h5>
                      <ul class="overflow-hidden py-6">
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.speedUp") }}</span
                          >
                          <span class="block leading-loose font-bold">10x</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.singleServiceBandwidth") }}</span
                          >
                          <span class="block leading-loose font-bold"
                            >2 Gbps</span
                          >
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{
                              $t("table.intercontinentalTransmission")
                            }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            $t("table.support")
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.singleTaskFileNumber") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            $t("table.1millionPlus")
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.largeFileSupport") }}</span
                          >
                          <span class="block leading-loose font-bold"
                            >>1TB</span
                          >
                        </li>
                      </ul>
                      <span
                        class="w-24 mb-6 text-sm inline-block bg-blue-500 rounded leading-loose text-center text-white cursor-pointer"
                        @click="handleRouterClick('transfer')"
                        >{{ $t("button.startUsing") }}</span
                      >
                      <div class="overflow-hidden">
                        <span
                          class="inline-block float-left text-sm leading-loose text-gray-500"
                          >{{
                            $t("titleAndText.ourCooperativeCustomers")
                          }}</span
                        >
                        <ul class="overflow-hidden">
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/jiyin.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/jingdong.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                          <li
                            class="float-left px-1 mb-2 w-1/4"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/gaobo.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div class="float-right pl-6 w-5/12">
                      <img src="@/assets/images/index/product3.png" alt="" />
                    </div>
                  </div>
                </el-tab-pane>
                <el-tab-pane
                  :label="$t('titleAndText.streamEdge')"
                  name="third"
                >
                  <div class="pt-4 pl-8 lb-4 overflow-hidden">
                    <div class="float-left w-7/12">
                      <h5 class="font-bold">
                        {{
                          $t(
                            "titleAndText.intelligentEdgeVideoAnalysisAlarmPlatform"
                          )
                        }}
                      </h5>
                      <ul class="overflow-hidden py-6">
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.sceneSupport") }}</span
                          >
                          <span class="block leading-loose font-bold">10+</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.algorithmSupport") }}</span
                          >
                          <span class="block leading-loose font-bold">20+</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.multiAlgorithmParallel") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            $t("table.support")
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.analysisRuleSupport") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            $t("table.roundRobinRealTime")
                          }}</span>
                        </li>
                        <li class="float-left w-1/5">
                          <span
                            class="block text-sm leading-loose mb-1 text-gray-500"
                            >{{ $t("table.deploymentMode") }}</span
                          >
                          <span class="block leading-loose font-bold">{{
                            $t("table.containerization")
                          }}</span>
                        </li>
                      </ul>
                      <span
                        class="w-24 mb-6 text-sm inline-block bg-blue-500 rounded leading-loose text-center text-white cursor-pointer"
                        >{{ $t("button.viewDetails") }}</span
                      >
                      <div class="overflow-hidden">
                        <span
                          class="inline-block float-left text-sm leading-loose text-gray-500"
                          >{{
                            $t("titleAndText.ourCooperativeCustomers")
                          }}</span
                        >
                        <ul class="overflow-hidden">
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/zhaoshang.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/jinfeng.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                          <li
                            class="float-left px-1 mb-2"
                            style="margin-top: -10px; width: 120px"
                          >
                            <div class="text-center py-1 border-gray-200">
                              <img
                                src="@/assets/images/index/tieta.png"
                                class="h-10 inline-block"
                                alt=""
                              />
                            </div>
                          </li>
                        </ul>
                      </div>
                    </div>
                    <div class="float-right pl-6 w-5/12">
                      <img src="@/assets/images/index/product1.png" alt="" />
                    </div>
                  </div>
                </el-tab-pane>
              </el-tabs>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8" class="pt-2 pr-2">
          <el-card class="mb-2" style="height: 16rem">
            <div slot="header" class="clearfix" style="min-width: 360px">
              <div style="float: left">
                <span class="font-bold">{{
                  $t("titleAndText.alarmInfo")
                }}</span>
                <span class="ml-2.5">
                  <el-link
                    :underline="false"
                    type="primary"
                    style="font-size: 12px"
                    @click="pushalarm"
                    >{{ $t("button.viewAll") }}</el-link
                  >
                </span>
              </div>
              <div style="float: right">
                <el-button
                  type="text"
                  size="mini"
                  style="color: #f56c6c"
                  :class="{ isactive: emergentIsActive }"
                  @click="handleAlarmButtonClick(3)"
                  >{{ $t("statusAndType.emergency")
                  }}<span>{{
                    alarmStatus.total_criticals > 999
                      ? "(" + "999+" + ")"
                      : "(" + alarmStatus.total_criticals + ")"
                  }}</span></el-button
                >
                <el-button
                  type="text"
                  size="mini"
                  style="color: #e6a23c"
                  :class="{ isactive: seriousIsActive }"
                  @click="handleAlarmButtonClick(2)"
                  >{{ $t("statusAndType.serious")
                  }}<span>{{
                    alarmStatus.total_warnings > 999
                      ? "(" + "999+" + ")"
                      : "(" + alarmStatus.total_warnings + ")"
                  }}</span></el-button
                >
                <el-button
                  type="text"
                  size="mini"
                  :class="{ isactive: tipIsActive }"
                  @click="handleAlarmButtonClick(1)"
                  >{{ $t("statusAndType.prompt")
                  }}<span>{{
                    alarmStatus.total_infos > 999
                      ? "(" + "999+" + ")"
                      : "(" + alarmStatus.total_infos + ")"
                  }}</span></el-button
                >
              </div>
            </div>
            <div
              v-loading="loading"
              class="infinite-list-wrapper -mt-5"
              :element-loading-text="$t('domain.loading')"
              style="overflow: auto"
            >
              <ul
                v-if="alarmData && alarmData.length > 0"
                class="list"
                style="padding: 0px 0px"
                infinite-scroll-disabled="disabled"
              >
                <li
                  v-for="(item, index) in alarmData"
                  :key="index"
                  class="list-item pl-2"
                >
                  <div class="header">
                    <span
                      v-if="queryData.level == 3"
                      class="mr-2"
                      style="font-size: 12px; color: #f56c6c"
                    >
                      <el-tag type="danger" size="mini">{{
                        $t("statusAndType.emergency")
                      }}</el-tag>
                    </span>

                    <span
                      v-if="queryData.level == 2"
                      class="mr-2"
                      style="font-size: 12px; color: #e6a23c"
                    >
                      <el-tag type="warning" size="mini">
                        {{ $t("statusAndType.serious") }}</el-tag
                      >
                    </span>
                    <span
                      v-if="queryData.level == 1"
                      class="mr-2"
                      style="font-size: 12px; color: #409eff"
                    >
                      <el-tag size="mini">
                        {{ $t("statusAndType.prompt") }}</el-tag
                      >
                    </span>

                    <div v-if="item.summary" style="display: flex">
                      <div
                        v-if="
                          item.summary.indexOf(
                            JSON.parse(item.labels).node_id
                          ) != -1 &&
                          item.summary.indexOf(
                            JSON.parse(item.labels).container_ref_id
                          ) != -1
                        "
                      >
                        <span
                          v-if="
                            item.summary
                              .split(JSON.parse(item.labels).node_id)[0]
                              .indexOf(
                                JSON.parse(item.labels).container_ref_id
                              ) != -1
                          "
                        >
                          <span>{{
                            item.summary
                              .split(JSON.parse(item.labels).node_id)[0]
                              .split(
                                JSON.parse(item.labels).container_ref_id
                              )[0]
                          }}</span>
                          <span>
                            <router-link
                              style="color: #409eff"
                              :to="
                                '/containerApplicationService/deployContainDetail/' +
                                JSON.parse(item.labels).container_ref_id
                              "
                            >
                              {{ JSON.parse(item.labels).container_ref_id }}
                            </router-link>
                          </span>
                          <span>{{
                            item.summary
                              .split(JSON.parse(item.labels).node_id)[0]
                              .split(
                                JSON.parse(item.labels).container_ref_id
                              )[1]
                          }}</span>
                        </span>
                        <span v-else>{{
                          item.summary.split(JSON.parse(item.labels).node_id)[0]
                        }}</span>
                        <span>
                          <router-link
                            style="color: #409eff"
                            :to="
                              '/infrastructure/nodeList/' +
                              JSON.parse(item.labels).node_id
                            "
                          >
                            {{ JSON.parse(item.labels).node_id }}
                          </router-link>
                        </span>
                        <span
                          v-if="
                            item.summary
                              .split(JSON.parse(item.labels).node_id)[1]
                              .indexOf(
                                JSON.parse(item.labels).container_ref_id
                              ) != -1
                          "
                        >
                          <span>{{
                            item.summary
                              .split(JSON.parse(item.labels).node_id)[1]
                              .split(
                                JSON.parse(item.labels).container_ref_id
                              )[0]
                          }}</span>
                          <span>
                            <router-link
                              style="color: #409eff"
                              :to="
                                '/containerApplicationService/deployContainDetail/' +
                                JSON.parse(item.labels).container_ref_id
                              "
                            >
                              {{ JSON.parse(item.labels).container_ref_id }}
                            </router-link>
                          </span>
                          <span>{{
                            item.summary
                              .split(JSON.parse(item.labels).node_id)[1]
                              .split(
                                JSON.parse(item.labels).container_ref_id
                              )[1]
                          }}</span>
                        </span>
                        <span v-else
                          >{{
                            item.summary.split(
                              JSON.parse(item.labels).node_id
                            )[1]
                          }}
                        </span>
                      </div>
                      <div
                        v-else-if="
                          item.summary.indexOf(
                            JSON.parse(item.labels).node_id
                          ) != -1
                        "
                      >
                        <span
                          >{{
                            item.summary.split(
                              JSON.parse(item.labels).node_id
                            )[0]
                          }}
                        </span>
                        <span>
                          <router-link
                            style="color: #409eff"
                            :to="
                              '/infrastructure/nodeList/' +
                              JSON.parse(item.labels).node_id
                            "
                          >
                            {{ JSON.parse(item.labels).node_id }}
                          </router-link>
                        </span>
                        <span
                          >{{
                            item.summary.split(
                              JSON.parse(item.labels).node_id
                            )[1]
                          }}
                        </span>
                      </div>
                      <div
                        v-else-if="
                          item.summary.indexOf(
                            JSON.parse(item.labels).container_ref_id
                          ) != -1
                        "
                      >
                        <span
                          >{{
                            item.summary.split(
                              JSON.parse(item.labels).container_ref_id
                            )[0]
                          }}
                        </span>
                        <span>
                          <router-link
                            style="color: #409eff"
                            :to="
                              '/containerApplicationService/deployContainDetail/' +
                              JSON.parse(item.labels).container_ref_id
                            "
                          >
                            {{ JSON.parse(item.labels).container_ref_id }}
                          </router-link>
                        </span>
                        <span
                          >{{
                            item.summary.split(
                              JSON.parse(item.labels).container_ref_id
                            )[1]
                          }}
                        </span>
                      </div>
                      <div v-else>
                        <span>{{ item.summary }}</span>
                      </div>
                    </div>
                    <span v-else>
                      {{ item.description ? item.description : "-" }}
                    </span>
                  </div>
                  <div style="font-size: 14px; float: right; width: 145px">
                    {{ item.update_time ? item.update_time : "-" }}
                  </div>
                </li>
              </ul>
              <el-empty
                v-else
                :description="$t('form.noData')"
                :image-size="55"
              />
            </div>
          </el-card>
          <el-card class="mb-2">
            <div slot="header" class="clearfix">
              <span class="font-bold"
                >{{ $t("titleAndText.siteLocation") }}
                <small
                  >({{ siteListData.length }}{{ $t("unit.PCS") }})</small
                ></span
              >
            </div>
            <div class="myChartItem" style="margin: -20px">
              <map-echart
                id="myChart2"
                class="myChart"
                width="100%"
                :height="'31rem'"
                :cur-data="siteListData"
              />
            </div>
          </el-card>
          <el-card class="">
            <div slot="header" class="clearfix">
              <span class="font-bold">{{
                $t("titleAndText.helpDocument")
              }}</span>
            </div>
            <div style="height: calc(13rem - 35px)">
              <el-tabs v-model="helpTabs" style="margin: -20px 0px 0px 0px">
                <el-tab-pane
                  :label="$t('titleAndText.latestAnnouncement')"
                  name="first"
                >
                  <ul>
                    <li
                      class="overflow-hidden leading-6 mb-2 border-b border-solid border-gray-200"
                    >
                      <span class="float-left text-sm leading-6">
                        <span>
                          <svg
                            t="1692190312157"
                            class="icon"
                            viewBox="0 0 1024 1024"
                            version="1.1"
                            xmlns="http://www.w3.org/2000/svg"
                            p-id="1763"
                            width="16"
                            height="16"
                          >
                            <path
                              d="M868.186858 340.109324c-19.749564-40.29308-47.934871-76.41791-83.761969-107.382051C711.579764 169.707308 614.816825 134.971894 512 134.971894c-102.717581 0-199.480519 34.735414-272.524133 97.755379-35.827098 30.96414-64.012406 67.088971-83.761969 107.382051-20.543516 42.079473-30.96414 86.739291-30.96414 132.788524 0 46.049234 10.420624 90.808296 30.96414 132.788525 19.749564 40.29308 47.934871 76.41791 83.761969 107.382051 72.944369 63.019965 169.707308 97.755379 272.524133 97.755379 18.161659 0 36.422563-1.091684 54.385734-3.275053L717.038186 886.249273c3.572785 1.885637 7.542547 2.778833 11.512308 2.778833 4.565226 0 9.031208-1.290173 13.000969-3.672029 7.443303-4.565226 11.909285-12.802481 11.810041-21.535957l-2.183369-125.04749c42.972669-29.47548 78.799767-67.088971 104.106998-109.267687 28.780771-47.934871 43.96511-102.122117 43.965109-156.607095 0-46.049234-10.420624-90.709052-31.063384-132.788524zM712.869936 704.731925c-7.344059 4.66447-11.710797 12.703237-11.512309 21.436712l1.687149 96.862183-120.581508-62.920721c-3.572785-1.885637-7.542547-2.778833-11.512309-2.778833-1.190928 0-2.282613 0.099244-3.473541 0.198488-18.161659 2.580345-36.819539 3.870518-55.477418 3.870517-186.181818 0-337.628223-129.414228-337.628223-288.403179C174.371777 313.908897 325.818182 184.593914 512 184.593914s337.628223 129.414228 337.628223 288.403178c0 90.808296-51.11068 177.448343-136.758287 231.734833z"
                              fill="#515151"
                              p-id="1764"
                            />
                            <path
                              d="M558.446211 314.702849c-13.695677 0-24.81101 11.115332-24.81101 24.81101V433.696453c0 4.16825-3.374297 7.542547-7.542547 7.542547H396.479938c-4.16825 0-7.542547-3.374297-7.542547-7.542547v-94.182594c0-13.695677-11.115332-24.81101-24.81101-24.81101s-24.81101 11.115332-24.81101 24.81101V604.396201c0 13.695677 11.115332 24.81101 24.81101 24.81101s24.81101-11.115332 24.81101-24.81101V498.899787c0-4.16825 3.374297-7.542547 7.542547-7.542547h129.612716c4.16825 0 7.542547 3.374297 7.542547 7.542547V604.396201c0 13.695677 11.115332 24.81101 24.81101 24.81101s24.81101-11.115332 24.811009-24.81101V339.513859c0-13.695677-11.115332-24.81101-24.811009-24.81101zM660.171351 406.205854c-13.695677 0-24.81101 11.115332-24.81101 24.81101V604.396201c0 13.695677 11.115332 24.81101 24.81101 24.81101s24.81101-11.115332 24.81101-24.81101V431.016864c0-13.695677-11.115332-24.81101-24.81101-24.81101zM660.171351 385.96007c13.695677 0 24.81101-11.115332 24.81101-24.81101v-23.322349c0-13.695677-11.115332-24.81101-24.81101-24.81101s-24.81101 11.115332-24.81101 24.81101v23.322349c0 13.695677 11.115332 24.81101 24.81101 24.81101z"
                              fill="#515151"
                              p-id="1765"
                            />
                          </svg>
                        </span>
                        欢迎使用秒如分布式云。
                      </span>
                      <span class="float-right text-sm text-gray-500"
                        >2023-06-10</span
                      >
                    </li>
                  </ul>
                </el-tab-pane>
                <el-tab-pane
                  :label="$t('titleAndText.productDynamic')"
                  name="second"
                />
                <el-tab-pane
                  :label="$t('titleAndText.helpDocument')"
                  name="third"
                />
              </el-tabs>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>
<script>
import {
  getSites,
  statsStatusMetrics,
  statsClustersMetrics,
  getAlertLogs,
  getAlertLogsStatus,
} from "@/api/mainApi";

import { mapGetters } from "vuex";
import mapEchart from "./index/echart/mapEchart";
export default {
  components: { mapEchart },
  computed: {
    ...mapGetters(["kind"]),
  },
  data() {
    return {
      loading: false,
      productTabs: "first",
      helpTabs: "first",
      siteListData: [], // 站点列表
      cloudsMain: {},
      instanceMain: {
        active: 0,
        paused: 0,
        shutoff: 0,
        unknown: 0,
        total_num: 0,
      },
      nodeMain: {},
      clusterMain: {},
      emergentIsActive: true,
      seriousIsActive: false,
      tipIsActive: false,
      queryData: {
        page_size: 20,
        page_num: 1,
        level: 3,
        alert_status: 1, // 1:触发 2:已解决
        process_status: "unread",
      },
      alarmData: [],
      alarmStatus: {
        total_criticals: 0,
        total_infos: 0,
        total_warnings: 0,
      },
    };
  },
  created() {},
  mounted() {
    this.init();
    this.initCloud();
    this.initInstance();
    this.initNodes();
    this.initCluster();
    this.initAlarm();
    this.initAlarmStatus();
  },
  methods: {
    pushalarm() {
      this.$router.push("/statistics/alarm");
    },
    async initAlarm() {
      this.loading = true;
      const list = await getAlertLogs(this.queryData);
      this.alarmData = list.alert_logs;
      this.loading = false;
    },
    async initAlarmStatus() {
      const list = await getAlertLogsStatus();
      this.alarmStatus = list;
    },
    handleAlarmButtonClick(value) {
      this.alarmData = [];
      if (value == 3) {
        (this.emergentIsActive = true),
          (this.seriousIsActive = this.tipIsActive = false);
      } else if (value == 2) {
        (this.seriousIsActive = true),
          (this.emergentIsActive = this.tipIsActive = false);
      } else {
        (this.tipIsActive = true),
          (this.emergentIsActive = this.seriousIsActive = false);
      }
      this.queryData.level = value;
      this.initAlarm();
    },
    handleRouterClick(name) {
      if (name == "container") {
        this.$router.push("/containerApplicationService/containerDeploy");
      } else if (name == "transfer") {
        this.$router.push("/personalSpace");
      } else if (name == "consumer") {
        this.$router.push("/monthOverview");
      } else if (name == "wallet") {
        this.$router.push("/wallet");
      }
      // else {
      //   this.kind == 0 || this.kind == 1
      //     ? this.$router.push("/wallet")
      //     : this.$notify({
      //         title: "请联系管理员充值",
      //         type: "warning",
      //         duration: 2500,
      //       });
      // }
    },
    async init() {
      const siteData = await getSites();
      var siteList = [];
      this.siteListData = [];
      siteData.sites.forEach((res) => {
        siteList.push(...res.sites);
      });
      for (var i = 0; i < siteList.length; i++) {
        if (
          siteList[i].location.nation != "" &&
          siteList[i].location.longitude != 0 &&
          siteList[i].location.latitude != 0
        ) {
          this.siteListData.push(siteList[i]);
        }
      }
    },
    async initCluster() {
      const clusterData = await statsClustersMetrics();
      this.clusterMain = clusterData;
    },
    async initNodes() {
      const nodeData = await statsStatusMetrics({ filter: "node-online" });
      this.nodeMain = {
        offline: 0,
        online: 0,
        total_num: nodeData.total_num ? nodeData.total_num : 0,
      };
      nodeData.status_metrics
        ? nodeData.status_metrics.forEach((item, index) => {
            item.status.desc.en == "offline"
              ? (this.nodeMain.offline = item.count)
              : "",
              item.status.desc.en == "online"
                ? (this.nodeMain.online = item.count)
                : "";
          })
        : "";
      this.nodeMain.unknown =
        nodeData.total_num - (this.nodeMain.offline + this.nodeMain.online);
    },
    async initCloud() {
      const cloudData = await statsStatusMetrics({ filter: "cloud-health" });
      this.cloudsMain = {
        health: 0,
        unhealth: 0,
        unknown: 0,
        total_num: cloudData.total_num ? cloudData.total_num : 0,
      };
      cloudData.status_metrics
        ? cloudData.status_metrics.forEach((item, index) => {
            item.status.desc.en == "Healthy"
              ? (this.cloudsMain.health = item.count)
              : "",
              item.status.desc.en == "UnHealthy"
                ? (this.cloudsMain.unhealth = item.count)
                : "",
              item.status.desc.en == "Unknown"
                ? (this.cloudsMain.unknown = item.count)
                : "";
          })
        : "";
    },
    async initInstance() {
      const instanceData = await statsStatusMetrics({
        filter: "cloud-vm-instance",
      });
      instanceData.vm_instance_status_metrics
        ? instanceData.vm_instance_status_metrics.forEach((item, index) => {
            if (item.product == "NextStack") {
              if (item.status_metrics && item.status_metrics.length > 0) {
                item.status_metrics.forEach((el, indexs) => {
                  el.status.desc.en == "INSTANCE_RUNNING" && el.count != 0
                    ? (this.instanceMain.active += el.count)
                    : "";
                  el.status.desc.en == "INSTANCE_MONITOR_TAG_DONE" &&
                  el.count != 0
                    ? (this.instanceMain.active += el.count)
                    : "";
                  el.status.desc.en == "INSTANCE_MIGRATE_CLEAN" && el.count != 0
                    ? (this.instanceMain.active += el.count)
                    : "";
                  el.status.desc.en == "INSTANCE_POWEROFF" && el.count != 0
                    ? (this.instanceMain.shutoff += el.count)
                    : "";
                  el.count != 0
                    ? (this.instanceMain.total_num += el.count)
                    : "";
                });
              }
            } else {
              if (item.status_metrics && item.status_metrics.length > 0) {
                item.status_metrics.forEach((el, indexs) => {
                  el.status.desc.en == "active" && el.count != 0
                    ? (this.instanceMain.active += el.count)
                    : "";
                  el.status.desc.en == "paused" && el.count != 0
                    ? (this.instanceMain.paused += el.count)
                    : "";
                  el.status.desc.en == "stopped" && el.count != 0
                    ? (this.instanceMain.shutoff += el.count)
                    : "";
                  el.status.desc.en == "unknown" && el.count != 0
                    ? (this.instanceMain.unknown += el.count)
                    : "";
                  el.count != 0
                    ? (this.instanceMain.total_num += el.count)
                    : "";
                });
              }
            }
          })
        : "";
    },
  },
};
</script>
<style lang="scss" scoped>
::v-deep .el-tag--mini {
  height: 18px;
  padding: 0 5px;
  line-height: 18px;
}

::v-deep .el-tag--mini:hover {
  padding: 0 7px;
}

::v-deep .el-button--text:hover {
  background: #e4e9f3;
}

::v-deep .el-button--text {
  padding: 8px 8px;
  min-width: 62px;
}

::v-deep .el-card__header {
  padding: 15px 20px;
  border: none;
}

::v-deep #expense .el-card__body {
  padding: 5px 20px 15px 20px;
}

::v-deep .el-card__body {
  padding: 15px 20px;
}

::v-deep .el-dropdown {
  display: block;
}

::v-deep .el-tabs__item {
  color: #7a7a7a;
}

::v-deep .el-tabs__item.is-active {
  color: #409eff;
}

svg {
  display: inline-block;
}

.infinite-list-wrapper {
  height: 185px;
  // text-align: center;
}

.infinite-list-wrapper .list-item {
  overflow: hidden;
  line-height: 1.4;
  color: #909399;
  box-shadow: 0px 1.2px #cecece;
  padding: 8px 0px 0px 0px;
}

.infinite-list-wrapper .list-item:hover {
  background-color: #e4e9f3;
  border-radius: 4px;
}

.header {
  display: flex;
  float: left;
  font-size: 14px;
  color: #909399;
  word-break: break-all;
  justify-content: space-between;
}

.infinite-list-wrapper {
  &::-webkit-scrollbar-track-piece {
    background: #d3dce6;
  }

  &::-webkit-scrollbar {
    width: 4px;
    height: 5px;
  }

  &::-webkit-scrollbar-thumb {
    background: #99a9bf;
    border-radius: 20px;
  }
}

.isactive {
  background-color: #e4e9f3;
}
</style>
