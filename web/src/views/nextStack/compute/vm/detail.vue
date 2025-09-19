<template>
  <div class="vmsInstabcesDetailPage h-full relative">
    <div class="absolute z-10 top-0">
      <el-button
        :size="$store.state.nextStack.viewSize.listSet"
        type="primary"
        class="float-left mr-2"
        @click="restartDetail()"
      >
        <img
          src="@/assets/nextStack/btn/restart_b.png"
          class="w-3 inline-block mr-1"
          alt=""
        />
        {{ $t("domain.refresh") }}
      </el-button>

      <operate
        :key="vmDetailData.instanceId"
        :prop-vm-detail="vmDetailData"
        class="float-left"
        :prop-show-type="2"
        :prop-show-btn="[
          'poweron',
          'poweroff',
          'reboot',
          'delete',
          'edit',
          'snaps',
          'flavor',
          'images',
          'transfer',
          'vnc',
          'renew',
        ]"
        @restartDetail="restartDetail"
        @initVmList="getDetail"
      />
    </div>
    <div class="mt-2 mb-2 pt-10">
      <el-card shadow="never">
        <el-form
          :model="form"
          label-width="120px"
          label-position="left"
          style="padding: 0 0 0 1rem"
          :size="$store.state.nextStack.viewSize.main"
        >
          <el-tabs v-model="activeName" class="demo-tabs bg-white vmDetailTabs">
            <el-tab-pane :label="$t('form.basicInfo')" name="first">
              <el-row class="mt-4">
                <el-col :span="12">
                  <el-form-item :label="$t('form.name') + ':'">
                    <span>{{ form.name || "-" }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.id') + ':'">
                    <span>{{ form.instanceId || "-" }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.description') + ':'">
                    <span>{{ form.description || "-" }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.imageType') + ':'">
                    <span v-if="form.imageOsType == 0">Linux</span>
                    <span v-else-if="form.imageOsType == 1">Windows</span>
                    <span v-else>-</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.imageOs') + ':'">
                    <span>{{ form.imageName || "-" }}</span>
                  </el-form-item>
                  <el-form-item
                    v-if="kind == 0 || kind == 1"
                    :label="$t('form.hypervisorNode') + ':'"
                  >
                    <span>
                      <router-link
                        :to="
                          '/nextStack/hypervisorNodes/detail/' +
                          form.hypervisorNodeId
                        "
                        class="text-blue-400"
                        >{{ form.hypervisorNodeName || "-" }}</router-link
                      >
                    </span>
                  </el-form-item>

                  <el-form-item :label="$t('form.status') + ':'">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="filtersFun.getVmStatus(form.phaseStatus, 'tag')"
                      >{{
                        filtersFun.getVmStatus(form.phaseStatus, "status")
                      }}</el-tag
                    >
                    <span v-if="form.phaseStatus == 66">
                      <el-tooltip
                        class="item"
                        effect="dark"
                        :content="$t('form.computeResourceInsufficient')"
                        placement="top"
                      >
                        <i class="el-icon-info mt-1 ml-1" />
                      </el-tooltip>
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.modifyTime') + ':'">
                    <span>{{ form.updateTime || "-" }}</span>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item :label="$t('form.flavorName') + ':'">
                    <span v-if="kind == 0 || kind == 1">
                      <router-link
                        :to="'/nextStack/flavor/detail/' + form.flavorId"
                        class="text-blue-400"
                        >{{ form.flavorName || "-" }}</router-link
                      >
                    </span>
                    <span v-if="kind != 0 && kind != 1">{{
                      form.flavorName || "-"
                    }}</span>
                  </el-form-item>
                  <el-form-item :label="$t('form.cpu') + ':'">
                    <span> {{ form.cpu || "-" }} {{ $t("unit.cpu") }} </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.mem') + ':'">
                    <span> {{ form.mem || "-" }} {{ $t("unit.mem") }} </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.rootDisk') + ':'">
                    <span>
                      {{ form.rootDisk || "-" }} {{ $t("unit.disk") }}
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.gpu') + ':'">
                    <span>
                      {{
                        form.pciInfos && form.pciInfos.length > 0
                          ? form.pciInfos.length
                          : $t("form.notMounted")
                      }}
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.ib') + ':'">
                    <span v-if="form.ib">
                      <i
                        style="color: rgb(103, 194, 58)"
                        class="el-icon-success"
                      />
                    </span>
                    <span v-if="!form.ib">-</span>
                  </el-form-item>

                  <el-form-item :label="$t('form.createTime') + ':'">
                    <span>{{ form.createTime || "-" }}</span>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.networkConfig')" name="second">
              <div class="mt-4">
                <el-form-item :label="$t('form.networkInfo') + ':'">
                  <el-table :data="form.networkDetailInfos" class="w-auto">
                    <el-table-column :label="$t('form.vpcNetwork')">
                      <template #default="scope">
                        <span>
                          <router-link
                            :to="'/nextStack/vpc/detail/' + scope.row.vpcId"
                            class="text-blue-400"
                            >{{ scope.row.vpcName }}</router-link
                          >
                        </span>
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.subnet')">
                      <template #default="scope">
                        <span>
                          {{ scope.row.subnetName }}
                        </span>
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.ip')">
                      <template #default="scope">
                        <span>{{ scope.row.ipAddress || "-" }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.eip')">
                      <template #default="scope">
                        <span>{{
                          scope.row.publicIp || scope.row.eip || "-"
                        }}</span>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-form-item>
                <el-form-item :label="$t('form.securityGroup')">
                  <div class="text-right block w-full mb-12">
                    <el-button
                      v-if="!sortStatus"
                      type="primary"
                      class="float-right mx-2"
                      :size="$store.state.nextStack.viewSize.main"
                      @click="openSort()"
                      >{{ $t("form.sort") }}</el-button
                    >
                    <el-button
                      type="primary"
                      class="float-right"
                      :size="$store.state.nextStack.viewSize.main"
                      @click="addInstance()"
                    >
                      {{ $t("form.associateSecurityGroup") }}
                    </el-button>
                  </div>
                  <el-table
                    ref="sgsListRef"
                    :key="detailSgsListData"
                    :data="detailSgsListData"
                    :row-key="rowKeyFunc"
                    class="w-full moveTable"
                  >
                    <el-table-column v-if="!sortStatus" key="0" type="expand">
                      <template #default="scope">
                        <div class="px-10 pb-10">
                          <div class="mb-2">
                            <el-radio-group
                              v-model="rulesSwitch[scope.$index]"
                              :size="$store.state.nextStack.viewSize.tabChange"
                            >
                              <el-radio-button :label="0" :value="0">{{
                                $t("form.inbound")
                              }}</el-radio-button>

                              <el-radio-button :label="1" :value="1">{{
                                $t("form.outbound")
                              }}</el-radio-button>
                            </el-radio-group>
                            <el-button
                              class="float-right mt-1 mb-2"
                              type="primary"
                              :size="$store.state.nextStack.viewSize.tabChange"
                              @click="
                                addRule(
                                  scope.row.sgId,
                                  rulesSwitch[scope.$index]
                                )
                              "
                              >{{ $t("form.addRule") }}</el-button
                            >
                          </div>
                          <el-table
                            :data="
                              scope.row.rules.filter((v) => {
                                return (
                                  v.direction === rulesSwitch[scope.$index]
                                );
                              })
                            "
                            style="width: 100%"
                          >
                            <el-table-column
                              prop="priority"
                              width="160"
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
                                <span class="block">
                                  {{ scope.row.priority }}
                                </span>
                                <div class="leading-tight">
                                  <small>(id:{{ scope.row.ruleId }})</small>
                                </div>
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="action"
                              :label="$t('form.strategy')"
                            >
                              <template #default="scope">
                                {{
                                  scope.row.action === 0
                                    ? $t("form.deny")
                                    : scope.row.action === 1
                                    ? $t("form.allow")
                                    : "-"
                                }}
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="name"
                              :label="$t('form.protocolPort')"
                            >
                              <template #default="props"
                                >{{
                                  props.row.protocol === 0
                                    ? "TCP:"
                                    : props.row.protocol === 1
                                    ? "UDP:"
                                    : props.row.protocol === 3
                                    ? $t("form.all")
                                    : props.row.protocol === 4
                                    ? "ICMP:"
                                    : ""
                                }}
                                <span v-if="props.row.protocol === 0">
                                  <el-tag
                                    v-for="(item, index) in getport(
                                      props.row.port
                                    )"
                                    :key="index"
                                    size="small"
                                    class="mr-0.5 mb-0.5"
                                    >{{ item }}</el-tag
                                  >
                                </span>
                                <span v-if="props.row.protocol === 1">
                                  <el-tag
                                    v-for="(item, index) in getport(
                                      props.row.port
                                    )"
                                    :key="index"
                                    size="small"
                                    class="mr-0.5 mb-0.5"
                                    >{{ item }}</el-tag
                                  >
                                </span>
                                <span v-if="props.row.protocol === 3" />
                                <span v-if="props.row.protocol === 4">
                                  <el-tag
                                    v-for="(item, index) in getport(
                                      props.row.port
                                    )"
                                    :key="index"
                                    size="small"
                                    class="mr-0.5 mb-0.5"
                                    >{{
                                      getICMPname(item == "all" ? "0" : item)
                                    }}</el-tag
                                  >
                                </span>
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="name"
                              :label="$t('form.type')"
                            >
                              <template #default="props">
                                {{
                                  props.row.addressType === 0
                                    ? "IPv4"
                                    : props.row.addressType === 1
                                    ? "IPv6"
                                    : "-"
                                }}</template
                              >
                            </el-table-column>
                            <el-table-column
                              prop="name"
                              :label="$t('form.sourceAddress')"
                            >
                              <template #default="props">
                                <span
                                  v-if="
                                    props.row.addressRef &&
                                    props.row.addressRef.cidr
                                  "
                                  >{{ props.row.addressRef.cidr }}</span
                                >
                                <span
                                  v-if="
                                    props.row.addressRef &&
                                    props.row.addressRef.sgId
                                  "
                                  >{{ getSg(props.row.addressRef) }}</span
                                >
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="description"
                              :label="$t('form.description')"
                            >
                              <template #default="scope">
                                {{
                                  scope.row.description
                                    ? scope.row.description
                                    : "-"
                                }}
                              </template></el-table-column
                            >
                            <el-table-column
                              v-if="!sortStatus"
                              prop="address"
                              :label="$t('form.operation')"
                              width="120"
                            >
                              <template #default="props">
                                <span
                                  class="text-blue-400 cursor-pointer"
                                  @click="
                                    editRule(
                                      scope.row.sgId,
                                      rulesSwitch[scope.$index],
                                      props.row
                                    )
                                  "
                                  >{{ $t("form.edit") }}</span
                                >
                                <el-popconfirm
                                  :confirm-button-text="$t('form.delete')"
                                  cancel-button-text="$t('form.cancel')"
                                  popper-class="tablepopover"
                                  icon-color="#626AEF"
                                  :title="$t('message.confirmDeleteRule')"
                                  @confirm="toDeleteRule(props.row)"
                                >
                                  <template #reference>
                                    <span
                                      class="text-blue-400 cursor-pointer ml-2"
                                    >
                                      {{ $t("form.delete") }}</span
                                    >
                                  </template>
                                </el-popconfirm>
                              </template>
                            </el-table-column>
                          </el-table>
                        </div>
                      </template>
                    </el-table-column>
                    <el-table-column v-if="!sortStatus" key="1" width="120">
                      <template #header>
                        {{ $t("form.priority") }}
                        <el-tooltip placement="top" effect="dark">
                          <template #content>
                            <div class="w-200px">
                              {{ $t("form.sortTips") }}
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
                        <span>{{ scope.$index + 1 }}</span>
                      </template>
                    </el-table-column>
                    <el-table-column v-if="sortStatus" key="2" width="120">
                      <template #default="scope">
                        <span class="move cursor-pointer">
                          <img
                            src="@/assets/nextStack/sortBtn.png"
                            class="w-7"
                          />
                        </span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      key="3"
                      prop="sgName"
                      :label="$t('form.securityGroup')"
                    >
                      <template #default="scope">
                        <span>
                          {{ scope.row.sgName }}
                          <router-link
                            :to="
                              '/nextStack/secGroups/detail/' +
                              scope.row.sgId +
                              '?type=info'
                            "
                          >
                            <br />
                            <small class="inline-block text-blue-400"
                              >(Id:{{ scope.row.sgId }})</small
                            >
                          </router-link>
                        </span>
                      </template>
                    </el-table-column>
                    <el-table-column
                      key="4"
                      prop="description"
                      :label="$t('form.description')"
                    >
                      <template #default="scope">
                        {{
                          scope.row.description ? scope.row.description : "-"
                        }}
                      </template>
                    </el-table-column>
                    <el-table-column
                      v-if="!sortStatus"
                      key="5"
                      prop="address"
                      :label="$t('form.operation')"
                      width="120"
                    >
                      <template #default="scope">
                        <el-popconfirm
                          :confirm-button-text="$t('form.unbind')"
                          cancel-button-text="$t('form.cancel')"
                          icon-color="#626AEF"
                          popper-class="tablepopover"
                          :title="$t('message.confirmUnbind')"
                          @confirm="toUnbound(scope.row.sgId)"
                        >
                          <template #reference>
                            <span class="text-blue-400 cursor-pointer">{{
                              $t("form.unbind")
                            }}</span>
                          </template>
                        </el-popconfirm>
                      </template>
                    </el-table-column>
                  </el-table>
                  <div v-if="sortStatus" class="block w-full pt-2">
                    <el-button
                      class="mx-2"
                      :size="$store.state.nextStack.viewSize.main"
                      @click="cancelSort()"
                      >{{ $t("form.cancel") }}</el-button
                    >
                    <el-button
                      type="primary"
                      class=""
                      :size="$store.state.nextStack.viewSize.main"
                      @click="saveSort()"
                      >{{ $t("form.save") }}</el-button
                    >
                  </div>
                </el-form-item>
              </div>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.storageConfig')" name="sixth">
              <el-button
                class="float-right mt-4 mb-4"
                :size="$store.state.nextStack.viewSize.tabChange"
                type="primary"
                @click="addVolumes"
                >{{ $t("form.addVolumes") }}</el-button
              >
              <el-table
                :data="form.diskInfos"
                max-height="calc(100% - 3rem)"
                class="!overflow-y-auto"
                stripe
                :scrollbar-always-on="false"
              >
                <el-table-column
                  prop="volumeName"
                  :label="$t('form.name')"
                  width="400"
                >
                  <template #default="scope">
                    <span>{{ scope.row.volumeName }}</span>
                  </template>
                </el-table-column>

                <el-table-column prop="type" :label="$t('form.type')">
                  <template #default="scope">
                    <span v-if="scope.row.type == 2">{{
                      $t("form.fileSystem")
                    }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="size" :label="$t('form.size')">
                  <template #default="scope">
                    <span>{{ scope.row.size }}GB</span>
                  </template>
                </el-table-column>
                <el-table-column prop="phaseStatus" :label="$t('form.status')">
                  <template #default="scope">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getVolumeStatus(scope.row.phaseStatus, 'tag')
                      "
                      >{{
                        filtersFun.getVolumeStatus(
                          scope.row.phaseStatus,
                          "status"
                        )
                      }}</el-tag
                    >
                  </template>
                </el-table-column>
                <el-table-column :label="$t('form.operation')" width="120">
                  <template #default="scope">
                    <el-popconfirm
                      :confirm-button-text="$t('form.detach')"
                      cancel-button-text="$t('form.cancel')"
                      icon-color="#626AEF"
                      popper-class="tablepopover"
                      :title="$t('message.confirmDetach')"
                      @confirm="toVolumesDetach(scope.row)"
                    >
                      <template #reference>
                        <span class="text-blue-400 cursor-pointer">{{
                          $t("form.detach")
                        }}</span>
                      </template>
                    </el-popconfirm>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.pciDevice')" name="fifth">
              <el-button
                v-if="kind == 0 || kind == 1"
                class="float-right mt-4 mb-4"
                :size="$store.state.nextStack.viewSize.tabChange"
                type="primary"
                @click="addPci"
                >{{ $t("form.addPci") }}</el-button
              >
              <el-table
                :data="form.pciInfos"
                max-height="calc(100% - 3rem)"
                class="!overflow-y-auto"
                stripe
                :scrollbar-always-on="false"
              >
                <el-table-column
                  prop="pciDeviceName"
                  :label="$t('form.name')"
                  width="400"
                >
                  <template #default="scope">
                    <span>{{ scope.row.pciDeviceName }}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="pciDeviceGroupId"
                  :label="$t('form.group')"
                >
                  <template #default="scope">
                    <span>{{ scope.row.pciDeviceGroupId || "-" }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="pciDeviceType" :label="$t('form.type')">
                  <template #default="scope">
                    <span>{{ scope.row.pciDeviceType }}</span>
                  </template>
                </el-table-column>
                <el-table-column prop="phaseStatus" :label="$t('form.status')">
                  <template #default="scope">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getPciStatus(scope.row.phaseStatus, 'tag')
                      "
                      >{{
                        filtersFun.getPciStatus(scope.row.phaseStatus, "status")
                      }}</el-tag
                    >
                  </template>
                </el-table-column>

                <!-- <el-table-column prop="createTime" label="创建时间" /> -->
                <el-table-column
                  prop="createTime"
                  :label="$t('form.createTime')"
                >
                  <template slot-scope="scope">
                    {{
                      scope.row.createTime
                        ? formatDate(scope.row.createTime)
                        : "-"
                    }}
                  </template>
                </el-table-column>
                <el-table-column
                  v-if="kind == 0 || kind == 1"
                  :label="$t('form.operation')"
                  width="120"
                >
                  <template #default="scope">
                    <el-popconfirm
                      :confirm-button-text="$t('form.detach')"
                      cancel-button-text="$t('form.cancel')"
                      icon-color="#626AEF"
                      popper-class="tablepopover"
                      :title="$t('message.confirmDetach')"
                      @confirm="toPciDetach(scope.row)"
                    >
                      <template #reference>
                        <span class="text-blue-400 cursor-pointer">{{
                          $t("form.detach")
                        }}</span>
                      </template>
                    </el-popconfirm>
                  </template>
                </el-table-column>
              </el-table>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.advancedConfig')" name="third">
              <div class="mt-4">
                <el-form-item :label="$t('form.hostname') + ':'">
                  <span>{{ form.hostname || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.sysUsername') + ':'">
                  <span>{{ form.sysUsername || "-" }}</span>
                </el-form-item>
                <el-form-item :label="$t('form.loginCredential') + ':'">
                  <span>{{
                    form.pubkeyId ? $t("form.keyPair") : $t("form.password")
                  }}</span>
                </el-form-item>
                <el-form-item
                  v-if="form.pubkeyId"
                  :label="$t('form.keyPair') + ':'"
                >
                  <span>
                    <router-link
                      :to="'/nextStack/publicKey/detail/' + form.pubkeyId"
                      class="text-blue-400"
                      >{{ form.pubkeyId }}</router-link
                    >
                  </span>
                </el-form-item>
              </div>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.snapshot')" name="ninth">
              <div class="mt-4">
                <el-table
                  :data="snapsList"
                  style="width: 100%; margin-bottom: 20px"
                  row-key="snapId"
                  default-expand-all
                  :tree-props="{
                    children: 'children',
                    hasChildren: 'hasChildren',
                  }"
                >
                  <el-table-column prop="name" :label="$t('form.name')">
                    <template #default="scope">
                      <router-link
                        :to="'/nextStack/snap/detail/' + scope.row.snapId"
                        class="text-blue-400"
                        >{{ scope.row.name || "-" }}</router-link
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="current"
                    :label="$t('form.currentSnapshot')"
                  >
                    <template #default="scope">
                      <span>
                        <el-tag
                          :size="$store.state.nextStack.viewSize.tagStatus"
                          :type="scope.row.current ? 'success' : 'danger'"
                          >{{
                            scope.row.current ? $t("form.yes") : $t("form.no")
                          }}</el-tag
                        >
                      </span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="phaseStatus"
                    :label="$t('form.status')"
                  >
                    <template #default="scope">
                      <el-tag
                        :size="$store.state.nextStack.viewSize.tagStatus"
                        :type="
                          filtersFun.getVmStatus(scope.row.phaseStatus, 'tag')
                        "
                        >{{
                          filtersFun.getVmStatus(
                            scope.row.phaseStatus,
                            "status"
                          )
                        }}</el-tag
                      >
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="createTime"
                    :label="$t('form.createTime')"
                  />
                  <el-table-column prop="address" :label="$t('form.operation')">
                    <template #default="scope">
                      <span
                        class="text-blue-400 cursor-pointer"
                        @click="toDeletesnapshot(scope.row)"
                        >{{ $t("form.delete") }}</span
                      >
                      <span
                        class="text-blue-400 cursor-pointer ml-2"
                        @click="toRestore(scope.row)"
                        >{{ $t("form.rollBack") }}</span
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.monitor')" name="fourth">
              <el-form
                ref="ruleForm"
                size="small"
                :inline="true"
                :model="example"
                class="demo-ruleForm mt-5"
                label-width="70px"
                label-position="right"
              >
                <el-form-item :label="$t('form.time') + ':'">
                  <el-date-picker
                    v-model="example.time"
                    type="datetimerange"
                    :picker-options="pickerOptions"
                    :range-separator="$t('form.to')"
                    :start-placeholder="$t('form.startDate')"
                    :end-placeholder="$t('form.endDate')"
                    format="yyyy-MM-dd HH:mm:ss"
                    @change="handleTimeChange"
                  />
                </el-form-item>
                <el-form-item>
                  <el-button size="small" @click="resetexample()">{{
                    $t("form.reset")
                  }}</el-button>
                </el-form-item>
              </el-form>
              <el-tabs v-model="activemonitor">
                <el-tab-pane :label="$t('form.instance')" name="example">
                  <div
                    v-if="exampleurl"
                    style="height: calc(100vh - 225px)"
                    class="mt-4"
                  >
                    <iframe
                      width="100%"
                      height="100%"
                      :src="exampleurl"
                      frameborder="0"
                    />
                  </div>
                  <div v-else>
                    <el-empty :description="$t('form.noData')" />
                  </div>
                </el-tab-pane>
                <el-tab-pane
                  v-if="form.pciInfos && form.pciInfos.length > 0"
                  :label="$t('form.gpu')"
                  name="gpu"
                >
                  <div
                    v-if="exampleurl"
                    style="height: calc(100vh - 225px)"
                    class="mt-4"
                  >
                    <iframe
                      width="100%"
                      height="100%"
                      :src="exampleurl"
                      frameborder="0"
                    />
                  </div>
                  <div v-else>
                    <el-empty :description="$t('form.noData')" />
                  </div>
                </el-tab-pane>
              </el-tabs>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.log')" name="seventh">
              <div class="mt-4">
                <el-table
                  :data="logList"
                  class="!overflow-y-auto"
                  stripe
                  :scrollbar-always-on="false"
                >
                  <el-table-column
                    prop="description"
                    width="700"
                    :label="$t('form.logDetail')"
                  >
                    <template slot-scope="scope">
                      <span>
                        <div>
                          <el-tag
                            v-if="scope.row.level == 'ERROR'"
                            size="mini"
                            type="danger"
                            class="mr-3"
                            >{{ $t("form.fail") }}</el-tag
                          >
                          <el-tag
                            v-if="scope.row.level == 'INFO'"
                            size="mini"
                            type="success"
                            class="mr-3"
                            >{{ $t("form.success") }}</el-tag
                          >
                          <span v-html="scope.row.description" />
                        </div>
                        <div
                          v-if="scope.row.level == 'ERROR'"
                          style="color: #f56c6c"
                        >
                          {{ scope.row.result ? scope.row.result : "-" }}
                        </div>
                      </span>
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
                    :page_num="logForm.page_num"
                    :page-size="logForm.page_size"
                    :page-sizes="$store.state.nextStack.page_sizes"
                    :current-page="logForm.page_num"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="logForm.total"
                    @size-change="logHandleSizeChange"
                    @current-change="logHandleCurrentChange"
                  />
                </div>
              </div>
            </el-tab-pane>
            <el-tab-pane :label="$t('form.event')" name="eighth">
              <div class="mt-4">
                <el-table
                  :data="eventList"
                  class="!overflow-y-auto"
                  stripe
                  :scrollbar-always-on="false"
                >
                  <el-table-column prop="content" :label="$t('form.event')">
                    <template #default="scope">
                      {{ scope.row.content }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="detailInfo"
                    :label="$t('form.detailInfo')"
                  >
                    <template #default="scope">
                      {{ scope.row.detailInfo }}
                    </template>
                  </el-table-column>

                  <el-table-column prop="result" :label="$t('form.result')">
                    <template #default="scope">
                      {{ scope.row.result }}
                    </template>
                  </el-table-column>
                  <!-- <el-table-column prop="userId" label="发起者">
                    <template #default="scope">
                      <span class="block">{{ scope.row.username }}</span>
                      <span><small>({{ scope.row.userId }})</small></span>
                    </template>
                  </el-table-column> -->
                  <el-table-column
                    prop="createTime"
                    :label="$t('form.triggerTime')"
                  />
                </el-table>
                <div class="flex justify-end mt-4 px-4">
                  <el-pagination
                    :page_num="eventForm.page_num"
                    :page-size="eventForm.page_size"
                    :page-sizes="$store.state.nextStack.page_sizes"
                    :current-page="eventForm.page_num"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="eventForm.total"
                    @size-change="eventHandleSizeChange"
                    @current-change="eventHandleCurrentChange"
                  />
                </div>
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-form>
      </el-card>
    </div>
    <!-- <el-form :model="form" label-width="120px" :size="$store.state.nextStack.viewSize.main">
      <el-card class="!border-none mb-3" v-if="activeName == 'first'">
        <div class="text item">
          <el-form-item label="启动项：" label-width="150px">
            <el-dropdown class="mr-2">
              <el-button size="small" type="primary" style="width:105px;text-align: center;">
                {{ form.bootDev == 'hd' ? 'HD硬盘' : form.bootDev == 'cdrom' ? 'CDROM光驱' : '-' }}<el-icon
                  class="el-icon--right">
                  <i-ic-twotone-keyboard-arrow-down></i-ic-twotone-keyboard-arrow-down>
                </el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item v-if="form.bootDev != 'hd'">
                    <span @click="changeBootDev('hd')">切换至:HD硬盘</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="form.bootDev != 'cdrom'">
                    <span @click="changeBootDev('cdrom')">切换至:CDROM光驱</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>

          </el-form-item>

          <el-form-item label="iso镜像：" class="w-full" label-width="150px">
            <div class="block w-full">

              <el-button :size="$store.state.nextStack.viewSize.listSet" type="primary" class="" @click="openNewPage()">
                连接虚拟介质
              </el-button>

            </div>
          </el-form-item>
        </div>
      </el-card>
    </el-form> -->
    <!-- 关联安全组 start -->
    <el-dialog
      :visible.sync="addInstanceDialog"
      :title="$t('form.addSecurityGroup')"
      width="1000px"
      :before-close="sgsHandleCloseInstance"
      :close-on-click-modal="false"
    >
      <el-form
        :model="sgsForm"
        :inline="true"
        label-width="100px"
        :size="$store.state.nextStack.viewSize.main"
      >
        <el-form-item :label="$t('form.name') + ':'">
          <el-input
            v-model="sgsForm.name"
            class="w-48"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSubmit">{{
            $t("form.query")
          }}</el-button>
          <el-button type="warning" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
      </el-form>
      <el-table
        ref="multipleTableRef"
        :size="$store.state.nextStack.viewSize.main"
        :data="sgsListData"
        style="width: 100%"
        @selection-change="sgsHandleSelectionChange"
      >
        <el-table-column
          type="selection"
          width="45"
          :selectable="vmTableSelected"
        />
        <el-table-column prop="name" :label="$t('form.name')">
          <template #default="scope">
            <router-link
              :to="
                '/nextStack/secGroups/detail/' + scope.row.sgId + '?type=info'
              "
              class="text-blue-400"
              >{{ scope.row.name }}</router-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('form.securityGroupRule')">
          <template #default="scope">
            <router-link
              :to="
                '/nextStack/secGroups/detail/' +
                scope.row.sgId +
                '?type=ingress'
              "
              class="text-blue-400"
              >{{ scope.row.ruleCount }}</router-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('form.associatedInstance')">
          <template #default="scope">
            <router-link
              :to="
                '/nextStack/secGroups/detail/' +
                scope.row.sgId +
                '?type=instances'
              "
              class="text-blue-400"
              >{{ scope.row.computeInstanceCount }}</router-link
            >
          </template>
        </el-table-column>
        <el-table-column prop="phaseStatus" :label="$t('form.status')">
          <template #default="scope">
            <el-tag
              :size="$store.state.nextStack.viewSize.tagStatus"
              :type="
                filtersFun.getSecGroupsStatus(scope.row.phaseStatus, 'tag')
              "
              >{{
                filtersFun.getSecGroupsStatus(scope.row.phaseStatus, "status")
              }}</el-tag
            >
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="$t('form.description')">
          <template #default="scope">
            {{ scope.row.description ? scope.row.description : "-" }}
          </template>
        </el-table-column>
      </el-table>
      <div class="flex justify-end mt-4 px-4">
        <el-pagination
          :page_num="sgsForm.page_num"
          :page-size="sgsForm.page_size"
          :page-sizes="$store.state.nextStack.page_sizes"
          :current-page="sgsForm.page_num"
          layout="total, sizes, prev, pager, next, jumper"
          :total="sgsForm.total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            :size="$store.state.nextStack.viewSize.main"
            @click="sgsHandleCloseInstance()"
            >{{ $t("form.cancel") }}</el-button
          >
          <el-button
            type="primary"
            :size="$store.state.nextStack.viewSize.main"
            :loading="addInstanceLoading"
            @click="toAddInstance()"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
    <!-- 关联安全组 end -->
    <el-dialog
      :visible.sync="dialogPci"
      :close-on-click-modal="false"
      width="1000px"
      destroy-on-close
      :before-close="pciHandleClose"
      :title="$t('form.mount')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="24">
            <el-table
              ref="multipleTableRef"
              v-loading="pciLoading"
              :element-loading-text="$t('domain.loading')"
              :data="pciDeviceTableData"
              max-height="500vh"
              class="!overflow-y-auto hypervisorNodesDialog"
              stripe
              :scrollbar-always-on="false"
              @current-change="pciHandleCheckChange"
            >
              <el-table-column label="" width="40px">
                <template #default="scope">
                  <span>
                    <span
                      v-if="scope.row.deviceId != nowCheckpci.deviceId"
                      class="w-3 h-3 block border rounded-sm border-gray-300"
                    />
                    <span
                      v-else
                      class="w-3 h-3 block border rounded-sm border-blue-500 bg-blue-500 text-center"
                    >
                      <i
                        class="el-icon-check text-white w-3.5 h-3.5 -m-0.5 leading-none table"
                      />
                    </span>
                  </span>
                </template>
              </el-table-column>
              <el-table-column
                prop="pciDeviceName"
                :label="$t('form.name')"
                width="400"
              >
                <template #default="scope">
                  <span>{{ scope.row.pciDeviceName }}</span>
                </template>
              </el-table-column>
              <el-table-column
                prop="pciDeviceGroupId"
                :label="$t('form.group')"
              >
                <template #default="scope">
                  <span>{{ scope.row.pciDeviceGroupId }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="pciDeviceType" :label="$t('form.type')">
                <template #default="scope">
                  <span>{{ scope.row.pciDeviceType }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="phaseStatus" :label="$t('form.status')">
                <template #default="scope">
                  <el-tag
                    :size="$store.state.nextStack.viewSize.tagStatus"
                    :type="
                      filtersFun.getPciStatus(scope.row.phaseStatus, 'tag')
                    "
                  >
                    {{
                      filtersFun.getPciStatus(scope.row.phaseStatus, "status")
                    }}
                  </el-tag>
                </template>
              </el-table-column>

              <el-table-column
                prop="createTime"
                :label="$t('form.createTime')"
              />
            </el-table>
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-right mt-2">
        <el-button type="text" size="small" @click="pciHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          size="small"
          :loading="loadingPci"
          @click="toPciAttach()"
          >{{ $t("form.mount") }}</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      :visible.sync="dialogVolumes"
      :close-on-click-modal="false"
      width="1000px"
      destroy-on-close
      :before-close="volumesHandleClose"
      :title="$t('form.mount')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="24">
            <el-form
              :model="volumesForm"
              :inline="true"
              :size="$store.state.nextStack.viewSize.main"
            >
              <el-form-item :label="$t('form.cloudDiskType') + ':'">
                <el-select
                  v-model="volumesForm.storage_pool_id"
                  class="w-48"
                  :placeholder="$t('form.pleaseSelectCloudDiskType')"
                  @change="onVolumesSubmit"
                >
                  <el-option :label="$t('form.all')" :value="''" />
                  <el-option
                    v-for="(item, index) in storagePoolsList"
                    :key="index"
                    :label="item.name"
                    :value="item.poolId"
                  />
                </el-select>
              </el-form-item>
            </el-form>

            <el-table
              ref="multipleTableRef"
              v-loading="volumenLoading"
              :element-loading-text="$t('domain.loading')"
              :data="volumenDeviceTableData"
              max-height="500vh"
              class="!overflow-y-auto hypervisorNodesDialog"
              stripe
              :scrollbar-always-on="false"
              @row-click="volumesHandleCheckChange"
            >
              <el-table-column label="" width="40px">
                <template #default="scope">
                  <span>
                    <span
                      v-if="getCheckStatus(scope.row)"
                      class="w-3 h-3 block border rounded-sm border-gray-300 cursor-pointer"
                    />
                    <span
                      v-else
                      class="w-3 h-3 block border rounded-sm border-blue-500 bg-blue-500 text-center cursor-pointer"
                    >
                      <i
                        class="el-icon-check text-white w-3.5 h-3.5 -m-0.5 leading-none table"
                      />
                    </span>
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="name" :label="$t('form.name')">
                <template #default="scope">
                  <span>{{ scope.row.name }}</span>
                </template>
              </el-table-column>

              <el-table-column prop="type" :label="$t('form.type')">
                <template #default="scope">
                  <span v-if="scope.row.type == 2">{{
                    $t("form.fileSystem")
                  }}</span>
                  <span v-else>-</span>
                </template>
              </el-table-column>
              <el-table-column prop="size" :label="$t('form.size')">
                <template #default="scope"> {{ scope.row.size }}GB </template>
              </el-table-column>
              <el-table-column prop="phaseStatus" :label="$t('form.status')">
                <template #default="scope">
                  <el-tag
                    :size="$store.state.nextStack.viewSize.tagStatus"
                    :type="
                      filtersFun.getVolumeStatus(scope.row.phaseStatus, 'tag')
                    "
                  >
                    {{
                      filtersFun.getVolumeStatus(
                        scope.row.phaseStatus,
                        "status"
                      )
                    }}
                  </el-tag>
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
                width="160px"
                :label="$t('form.createTime')"
              />
            </el-table>
            <div class="flex justify-end mt-4 px-4">
              <el-pagination
                :page_num="volumesForm.page_num"
                :page-size="volumesForm.page_size"
                :page-sizes="$store.state.nextStack.page_sizes"
                :current-page="volumesForm.page_num"
                layout="total, sizes, prev, pager, next, jumper"
                :total="volumesForm.total"
                @size-change="volumesHandleSizeChange"
                @current-change="volumesHandleCurrentChange"
              />
            </div>
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-right mt-4">
        <el-button type="text" size="small" @click="volumesHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button
          type="primary"
          size="small"
          :loading="loadingVolumes"
          @click="toVolumesAttach()"
          >{{ $t("form.mount") }}</el-button
        >
      </div>
    </el-dialog>
    <addRulePage :item-info="addRuleInfo" @addRuleClose="addRuleClose" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import NBDServer from "@/utils/nbd.js";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import Sortable from "sortablejs";
import operate from "./operate.vue";
import addRulePage from "@/views/nextStack/network/secGroups/addRule.vue";
import { getMonitorLink } from "@/api/mainApi";

export default {
  components: {
    operate,
    addRulePage,
  },
  data() {
    return {
      // 监控激活的实例
      activemonitor: "example",
      // 监控的搜索字段
      example: {
        time: [
          new Date(new Date().setHours(new Date().getHours() - 6)),
          new Date(),
        ],
        gpu: false,
      },
      // 监控的url
      exampleurl: "",
      pickerOptions: {
        shortcuts: [
          {
            text: this.$t("message.lastTenMinutes"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 60 * 1000 * 10);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThirtyMinutes"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 60 * 1000 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneHour"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastSixHours"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 6);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastTwelveHours"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 12);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneDay"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 1);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThreeDays"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 3);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneWeek"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastOneMonth"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit("pick", [start, end]);
            },
          },
          {
            text: this.$t("message.lastThreeMonths"),
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit("pick", [start, end]);
            },
          },
        ],
      },
      filtersFun: filtersFun,
      form: {},
      activeName: "first",
      sgsForm: {
        name: "",
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      rulesSwitch: [],
      sortStatus: false, // 是否正在排序
      sgsListData: [], // 安全组列表
      initDetailSgsListData: [], // 详情安全组列表
      detailSgsListData: [], // 详情安全组列表
      addInstanceLoading: false, // 添加关联实例
      addInstanceDialog: false, // 添加实例弹窗
      multipleSelection: [], // 关联实例 添加实例 当前选中内容
      multipleSelectionId: [], // 关联实例 添加实例 当前选中内容ID
      formVmInstancesIds: [], // 当前安全组列表 id集合
      pciDeviceTableData: [], // pci设备列表
      dialogPci: false, // pci设备弹窗
      pciLoading: false, // pci设备弹窗加载状态
      nowCheckpci: "", // 当前选中的pci设备
      storagePoolsList: [], // 云盘类型列表
      volumenDeviceTableData: [], // volumen设备列表
      dialogVolumes: false, // volumen设备弹窗
      nowVolumesNum: 0, // 当前选中的volumen设备数量
      volumenLoading: false, // volumen设备弹窗加载状态
      nowCheckvolumen: [], // 当前选中的volumen设备
      volumesForm: {
        name: "",
        storage_pool_id: "",
        detached: true,
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      vmDetailData: {}, // 实例详情

      nodeDetail: "",
      logList: [],
      eventList: [],
      logForm: {
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },
      eventForm: {
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0,
      },

      upload: "",
      isoFile: "",
      isoloading: false,
      isoserver: "",
      isoMsg: "",

      addRuleInfo: {
        isAdd: true,
        dialogVisible: false,
        sgId: "",
        ruleType: "",
      },
      monitorLink: "", // 监控链接
      snapsList: [],
      loadingPci: false,
      loadingVolumes: false,
    };
  },
  watch: {
    activeName(newValue) {
      if (newValue == "first") {
        this.getDetail();
      }
      if (newValue == "eighth") {
        this.getEventsList(); // 事件列表
      }
      if (newValue == "fourth") {
        this.getMonitorLink(); // 获取监控链接
      }
      if (newValue == "seventh") {
        this.getLogsList(); // 日志列表
      }
      if (newValue == "ninth") {
        this.getsnapsList(); // 快照列表
      }
      this.$router.push({
        query: { type: newValue },
      });
    },
    activemonitor(newValue, oldValue) {
      if (newValue == "example") {
        this.example.gpu = false;
        this.getMonitorLink();
      } else if (newValue == "gpu") {
        this.example.gpu = true;
        this.getMonitorLink();
      } else {
        this.exampleurl = "";
      }
    },
  },
  created() {},
  mounted() {
    this.activeName = this.$route.query.type ? this.$route.query.type : "first";
    window.addEventListener("beforeunload", this.handleUnload);
    this.getDetail();
  },
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    // 重置监控的搜索条件
    resetexample() {
      this.example.time = [
        new Date(new Date().setHours(new Date().getHours() - 6)),
        new Date(),
      ];
      this.getMonitorLink();
    },
    // 改变时间
    handleTimeChange(value) {
      this.example.time = value;
      this.getMonitorLink();
    },

    // 删除高级配置的快照
    toDeletesnapshot(item) {
      // 删除
      this.$confirm(
        this.$t("message.confirmDeleteSnapshot"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.delete"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi.snapsDel(item.snapId).then((res) => {
            this.$notify({
              title: this.$t("message.startDelete"),
              type: "success",
              duration: 2500,
            });
            this.getsnapsList();
          });
        })
        .catch(() => {});
    },
    addPci() {
      this.getPciDeviceList(); // 获取pci设备列表

      this.dialogPci = true;
    },
    pciHandleCheckChange(val) {
      this.nowCheckpci = val;
    },
    toPciAttach() {
      this.loadingPci = true;
      // 挂载
      mainApi
        .pciAttach(this.nowCheckpci.deviceId, {
          vmInstanceId: this.form.instanceId,
        })
        .then((res) => {
          this.$notify({
            title: this.$t("message.startMount"),
            type: "success",
            duration: 2500,
          });
          this.getDetail(); // 获取详情
          this.pciHandleClose();
          this.loadingPci = false;
        })
        .catch((error) => {
          this.loadingPci = false;
        });
      return true;
    },
    toPciDetach(item) {
      // 卸载
      mainApi
        .pciDetach(item.deviceId, { vmInstanceId: this.form.instanceId })
        .then((res) => {
          this.$notify({
            title: this.$t("message.startDetach"),
            type: "success",
            duration: 2500,
          });
          this.getDetail(); // 获取详情
        })
        .catch((error) => {});
      return true;
    },
    pciHandleClose() {
      this.dialogPci = false;
      this.nowCheckpci = "";
    },
    getport(val) {
      return val.split(",");
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
    getDetail() {
      // 获取详情
      var id = this.$route.params.id;

      mainApi
        .vmsInstabcesDetail(id)
        .then((res) => {
          this.vmDetailData = res;
          this.form = res;
          if (res.hypervisorNodeId) {
            this.getNodeDetail(res.hypervisorNodeId);
          }
          this.initDetailSgsListData = JSON.parse(JSON.stringify(res.sgInfos));
          this.detailSgsListData = JSON.parse(JSON.stringify(res.sgInfos));
          this.formVmInstancesIds = res.sgInfos.map((v) => {
            this.rulesSwitch.push(0);
            return v.sgId;
          });
          this.detailSgsListData.forEach((item, index) => {
            if (item.rules && item.rules.length > 0) {
            } else {
              item.rules = [];
            }
          });
        })
        .catch((error) => {});
    },
    getNodeDetail(id) {
      // 获取详情

      mainApi
        .vmsHypervisorNodesDetail(id)
        .then((res) => {
          this.nodeDetail = res;
        })
        .catch((error) => {});
    },
    vmTableSelected(row, index) {
      // 筛选出已关联的实例
      return !this.formVmInstancesIds.includes(row.sgId);
    },
    sgsHandleSelectionChange(val) {
      this.multipleSelection = val;
      this.multipleSelectionId = val.map((item) => {
        return item.sgId;
      });
    },
    addInstance() {
      this.getSgsList(); // sgs列表
      // 点击添加实例
      this.addInstanceDialog = true; // 打开添加实例弹窗
    },
    sgsHandleCloseInstance() {
      this.$refs.multipleTableRef.clearSelection();
      this.multipleSelectionId = [];

      this.addInstanceDialog = false; // 关闭关联实例弹窗
    },
    toUnbound(item) {
      // 取消关联
      var id = this.$route.params.id;

      mainApi
        .sgsUnboundAdd({ vmInstances: [id] }, item)
        .then((res) => {
          this.$notify({
            title: this.$t("message.unboundSuccess"),
            type: "success",
            duration: 2500,
          });
          this.getDetail(); // 请求详情
        })
        .catch((error) => {});

      return true;
    },
    toSortSg(item) {
      // 关联安全组排序
      var id = this.$route.params.id;
      mainApi
        .vmsInstabcesBoundSgSort({ sgIds: item }, id)
        .then((res) => {
          this.$notify({
            title: this.$t("message.sortSuccess"),
            type: "success",
            duration: 2500,
          });
          this.sortStatus = false;

          this.getDetail(); // 请求详情
        })
        .catch((error) => {});
    },
    toAddInstance() {
      // 关联安全组
      this.addInstanceLoading = true;
      if (this.multipleSelectionId.length == 0) {
        this.$notify({
          title: this.$t("message.pleaseSelectSecurityGroup"),
          type: "warning",
          duration: 2500,
        });
        this.addInstanceLoading = false;
        return;
      }
      this.addInstanceLoading = true;
      var id = this.$route.params.id;
      mainApi
        .vmsInstabcesBoundSg({ sgIds: this.multipleSelectionId }, id)
        .then((res) => {
          this.addInstanceLoading = false;
          this.$notify({
            title: this.$t("message.boundSuccess"),
            type: "success",
            duration: 2500,
          });
          this.sgsHandleCloseInstance(); // 初始化输入框 关闭弹窗
          this.getDetail(); // 请求详情
        })
        .catch((error) => {
          this.addInstanceLoading = false;
        });
    },
    getSg(item) {
      var data = this.sgsListData.filter((v) => {
        return v.sgId == item.sgId;
      })[0];
      return data && data.name ? data.name : "";
    },
    onSubmit() {
      // 提交查询
      this.sgsForm.page_num = 1;
      this.getSgsList();
    },
    onReset() {
      // 重置查询
      this.sgsForm.name = "";
      this.sgsForm.page_num = 1;
      this.getSgsList();
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.sgsForm.page_size = val;

      this.getSgsList();
    },
    handleCurrentChange(val) {
      // 改变页码
      this.sgsForm.page_num = val;
      this.getSgsList();
    },
    addRule(sgId, type) {
      this.addRuleInfo = {
        isAdd: true,
        dialogVisible: true,
        sgId: sgId,
        ruleType: type,
      };
    },
    editRule(sgId, type, item) {
      this.addRuleInfo = {
        isAdd: false,
        dialogVisible: true,
        sgId: sgId,
        ruleType: type,
        item: item,
      };
    },
    toDeleteRule(item) {
      mainApi
        .sgsRulesDel(item.ruleId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.deleteSuccess"),
            type: "success",
            duration: 2500,
          });
          this.restartDetail(); // 请求详情
        })
        .catch((error) => {});
    },
    addRuleClose() {
      this.addRuleInfo = {
        isAdd: true,
        dialogVisible: false,
        sgId: "",
        ruleType: "",
      };
      this.restartDetail();
    },
    getSgsList() {
      // sgs列表
      const params = {
        name: this.sgsForm.name,
        page_num: this.sgsForm.page_num,
        page_size: this.sgsForm.page_size,
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
        .sgsList(params)
        .then((res) => {
          this.sgsListData = res.securityGroups;
          this.sgsForm.total = res.totalNum;
        })
        .catch((error) => {});
    },
    rowKeyFunc(row) {
      return row.sgId;
    },
    cancelSort() {
      // 取消排序
      this.sortStatus = false;
      // this.detailSgsListData = JSON.parse(
      //   JSON.stringify(this.initDetailSgsListData)
      // );
      this.getDetail();
    },
    saveSort() {
      // 保存排序
      var ids = this.detailSgsListData.map((v) => {
        return v.sgId;
      });

      console.log(ids);
      this.toSortSg(ids);
    },
    openSort() {
      // 点击排序按钮
      this.sortStatus = true;

      setTimeout(() => {
        this.rowDrop();
      }, 1000);
    },
    rowDrop() {
      const tbody = document.querySelector(
        ".moveTable .el-table__body-wrapper tbody"
      );
      Sortable.create(tbody, {
        handle: ".move",
        animation: 150,
        onEnd: (e) => {
          const currRow = this.detailSgsListData.splice(e.oldIndex, 1)[0];

          this.detailSgsListData.splice(e.newIndex, 0, currRow);
        },
      });
    },
    toRestore(item) {
      // 回滚

      this.$confirm(
        this.$t("message.confirmRollback"),
        this.$t("message.tip"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .snapsSwitch(item.snapId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startRollback"),
                type: "success",
                duration: 2500,
              });
              this.getsnapsList(); // 获取详情
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    toDeleteSnaps(item) {
      // 删除
      mainApi
        .snapsDel(item.snapId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.startDelete"),
            type: "success",
            duration: 2500,
          });
          this.getDetail();
        })
        .catch((error) => {});
      return true;
    },
    async getMonitorLink() {
      var url = "";
      this.example.gpu
        ? (url = "?type=nextstack&type=gpu")
        : this.form.type == "node"
        ? (url = "?type=hypervisor_node")
        : (url = "?type=libvirt");
      const res = await getMonitorLink(url);
      this.monitorLink = res.links;
      this.getVmPanelsUrl();
    },
    getVmPanelsUrl() {
      var id = this.$route.params.id;
      var urlOrigin = window.location.origin;
      // var urlOrigin = 'http://192.168.1.112:3000';
      var from = "";
      var to = "";

      this.example.time ? (from = this.example.time[0].getTime()) : "";
      this.example.time ? (to = this.example.time[1].getTime()) : "";
      if (this.monitorLink && this.monitorLink.length > 0) {
        var url =
          urlOrigin +
          this.monitorLink[0].link +
          "?orgId=1&kiosk&var-vm_instance_id=" +
          id +
          "&var-cloud_id=" +
          this.$store.state.app.hxcloudData.cloud_id +
          "&var-node=All" +
          "&from=" +
          from +
          "&to=" +
          to;
        this.exampleurl = url;
      } else {
        this.exampleurl = "";
      }
    },

    getPciDeviceList() {
      this.pciLoading = true;
      // GPU列表
      var id = this.$route.params.id;
      var data = {
        node_id: this.form.hypervisorNodeId,
        vm_id: id,
      };
      mainApi
        .pciAvailableList(data)
        .then((res) => {
          this.pciDeviceTableData = res;
          this.pciLoading = false;
        })
        .catch((error) => {
          this.pciLoading = false;
        });
    },
    toVolumesDetach(item) {
      // 云盘卸载
      mainApi
        .volumesDetach(item.volumeId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.startDetach"),
            type: "success",
            duration: 2500,
          });
          this.getDetail();
        })
        .catch((error) => {});
      return true;
    },
    addVolumes() {
      this.getStoragePools(); // 云盘类型列表
      // 添加云盘
      this.getVolumesList(); // 获取云盘列表

      this.dialogVolumes = true;
    },
    volumesHandleClose() {
      this.dialogVolumes = false;

      this.nowCheckvolumen = [];
    },
    toVolumesAttach() {
      this.loadingVolumes = true;
      // 挂载 nowCheckvolumen
      var ids = this.nowCheckvolumen.map((item) => {
        return item.volumeId;
      });
      if (ids.length == 0) {
        this.$notify({
          title: this.$t("message.pleaseSelectVolume"),
          type: "warning",
          duration: 2500,
        });
        this.loadingVolumes = false;
        return false;
      }
      mainApi
        .vmsToVolumesList({ volumeIds: ids }, this.form.instanceId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.startMount"),
            type: "success",
            duration: 2500,
          });
          this.getDetail(); // 获取详情
          this.volumesHandleClose();
          this.loadingVolumes = false;
        })
        .catch((error) => {
          this.loadingVolumes = false;
        });
      return true;
    },
    getVolumesList() {
      // 云盘列表
      this.volumenLoading = true;
      const params = {
        name: this.volumesForm.name,
        storage_pool_id: this.volumesForm.storage_pool_id,
        detached: this.volumesForm.detached,
        page_num: this.volumesForm.page_num,
        page_size: this.volumesForm.page_size,
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
        .volumesList(params)
        .then((res) => {
          this.volumenLoading = false;
          this.volumenDeviceTableData = res.volumes;
          this.volumesForm.total = res.totalNum;
        })
        .catch((error) => {
          this.volumenLoading = false;
        });
    },
    volumesHandleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.volumesForm.page_size = val;
      this.getVolumesList();
    },
    volumesHandleCurrentChange(val) {
      // 改变页码
      this.volumesForm.page_num = val;
      this.getVolumesList();
    },
    onVolumesSubmit() {
      this.volumesForm.page_num = 1;
      this.getVolumesList();
    },
    getCheckStatus(val) {
      var ids = this.nowCheckvolumen.map((item) => {
        return item.volumeId;
      });
      if (ids.includes(val.volumeId)) {
        return false;
      } else {
        return true;
      }
    },
    volumesHandleCheckChange(val) {
      var ids = this.nowCheckvolumen.map((item) => {
        return item.volumeId;
      });
      if (ids.includes(val.volumeId)) {
        this.nowCheckvolumen = this.nowCheckvolumen.filter((item) => {
          return item.volumeId != val.volumeId;
        });
      } else {
        var num = this.form.diskInfos ? this.form.diskInfos.length : 0;
        if (this.nowCheckvolumen.length + num >= 4) {
          this.$notify({
            title: this.$t("message.maxMount4Volumes"),
            type: "warning",
            duration: 2500,
          });
          return false;
        }
        this.nowCheckvolumen.push(val);
      }
    },
    getStoragePools() {
      // 云盘类型列表
      mainApi
        .storagePoolsList()
        .then((res) => {
          this.storagePoolsList = res.storagePools;
        })
        .catch((error) => {});
    },
    eventHandleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.eventForm.page_size = val;
      this.getEventsList();
    },
    eventHandleCurrentChange(val) {
      // 改变页码
      this.eventForm.page_num = val;
      this.getEventsList();
    },
    logHandleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem("page_size", val);
      this.$store.state.nextStack.page_size = val;
      this.logForm.page_size = val;
      this.getLogsList();
    },
    logHandleCurrentChange(val) {
      // 改变页码
      this.logForm.page_num = val;
      this.getLogsList();
    },
    getLogsList() {
      // 日志列表
      var id = this.$route.params.id;
      const params = {
        page_num: this.logForm.page_num,
        page_size: this.logForm.page_size,
        resource_id: id,
      };
      mainApi
        .operationLogsList(params)
        .then((res) => {
          this.logList = res.logs;
          this.logForm.total = res.total_num;
        })
        .catch((error) => {});
    },
    getEventsList() {
      // 事件列表
      var id = this.$route.params.id;
      const params = {
        detail_info: id,
        page_num: this.eventForm.page_num,
        page_size: this.eventForm.page_size,
      };
      mainApi
        .operationEventsList(params)
        .then((res) => {
          this.eventList = res.events;
          this.eventForm.total = res.totalNum;
        })
        .catch((error) => {});
    },
    restartDetail() {
      this.getDetail(); // 获取详情
      this.getStoragePools(); // 云盘类型列表
      this.getSgsList(); // sgs列表
      this.getLogsList(); // 日志列表
      this.getEventsList(); // 事件列表
      this.getMonitorLink(); // 获取监控链接
      this.getsnapsList();
    },

    changeBootDev(val) {
      // 改变启动盘
      var id = this.$route.params.id;

      var data = {
        // name: form.value.name,
        // description: form.value.description,
        // flavorId: form.value.flavorId,
        bootDev: val,
      };
      mainApi
        .vmsInstabcesEdit(data, id)
        .then((res) => {
          this.$notify({
            title: this.$t("message.startSwitchBoot"),
            type: "success",
            duration: 2500,
          });
          this.getDetail(); // 获取详情
        })
        .catch((error) => {});
    },
    getCookie(cname) {
      var name = cname + "=";
      var ca = document.cookie.split(";");
      for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == " ") c = c.substring(1);
        if (c.indexOf(name) != -1) return c.substring(name.length, c.length);
      }
      return "";
    },
    stop_server() {
      this.isoloading = false;
      if (this.isoserver) {
        this.isoserver.stop();
      }
    },
    start_server() {
      console.log(this.isoFile);
      if (!this.isoFile) {
        this.$notify({
          title: this.$t("message.pleaseSelectISOFile"),
          type: "warning",
          duration: 2500,
        });
        return false;
      }
      this.isoloading = true;
      this.isoMsg = "";

      var file = this.isoFile;
      let protocol = "";
      if (window.location.protocol === "https:") {
        protocol = "wss://";
      } else {
        protocol = "ws://";
      }
      // const wsUrl =
      //   protocol +
      //   window.location.hostname +
      //   '/api/iso/' +
      //   vmDetailData.value.instanceId +
      //   '?token=' +
      //   getCookie('Access-Token');
      const wsUrl =
        protocol +
        "192.168.8.87" +
        "/api/iso/" +
        this.vmDetailData.instanceId +
        "?token=" +
        this.getCookie("Access-Token");
      this.isoserver = new NBDServer(wsUrl, file);
      this.isoserver.onlog = (msg) => {
        // var container = document.getElementById("log");
        this.isoMsg += msg + "\n";
        this.isoloading = false;
      };
      this.isoserver.start();
    },
    handleChange(file, fileList) {
      // 限制格式

      const isIso = file.name.split(".").pop() === "iso";
      if (!isIso) {
        this.$notify({
          title: this.$t("message.uploadFileFormatOnlyISO"),
          type: "error",
          duration: 2500,
        });
        // 清空上传的文件
        this.isoFile = "";
        this.upload.clearFiles();
        return false;
      }
      this.isoFile = file.raw;
    },
    handleExceed(files) {
      this.$notify({
        title: this.$t("message.maxUploadFile"),
        type: "warning",
        duration: 2500,
      });
    },
    handleRemove(file, fileList) {
      this.isoFile = "";
    },
    handleUnload() {
      this.stop_server();
    },
    openNewPage() {
      // window.open(
      //   "/nextStack/vmCommand?instanceId=" +
      //   this.vmDetailData.instanceId +
      // '&cloudId=' +
      // propVmDetail.cloudId +
      //   "&instanceName=" +
      //   this.vmDetailData.name +
      //   "&type=" +
      //   "iso",
      //   "newwindow",
      //   "height=800,width=1220,top=190,left=350,toolbar=no,menubar=no,scrollbars=no,resizable=yes, location=no,status=no"
      // );
      // 新窗口打开
      const routeData = this.$router.resolve({
        path: "/nextStack/vmCommand",
        query: {
          instanceId: this.vmDetailData.instanceId,
          cloudId: this.vmDetailData.cloudId,
          type: "iso",
        },
      });
      window.open(
        routeData.href,
        "newwindow",
        "height=800,width=1220,top=190,left=350,toolbar=no,menubar=no,scrollbars=no,resizable=yes, location=no,status=no"
      );
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
    getsnapsList() {
      var id = this.$route.params.id;
      mainApi.vmsnapsListDetail(id).then((res) => {
        this.snapsList = res;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.vmsInstabcesDetailPage {
  display: block;

  .listDelBtn {
    display: flex;
    cursor: pointer;
    padding: 2px 10px;
  }

  .vmDetailTabs {
    border-top-left-radius: 0.4rem;
    border-top-right-radius: 0.4rem;

    ::v-deep .el-tabs__header {
      // padding: 0 0 0 1rem;
      margin-bottom: 0;
    }
  }
}
</style>
