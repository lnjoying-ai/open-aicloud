<template>
  <div class="mt-2 relative" style="height: calc(100vh - 144px - 0.5rem)">
    <div
      class="mb-2 overflow-auto"
      shadow="never"
      style="height: calc((100vh - 185px) - 0.5rem)"
    >
      <el-form
        ref="addVmForm"
        v-loading="loading"
        :size="$store.state.nextStack.viewSize.main"
        :model="form"
        :rules="rules"
        label-width="120px"
        :element-loading-text="$t('domain.loading')"
      >
        <el-card class="!border-none mb-3">
          <template #header>
            <div class="">
              <span>{{ $t("form.basicInfo") }}</span>
            </div>
          </template>
          <div class="text item">
            <el-form-item :label="$t('form.name') + ':'" prop="name">
              <el-input
                v-model="form.name"
                class="w-60"
                :placeholder="$t('form.pleaseInputName')"
              />
            </el-form-item>
            <el-form-item
              :label="$t('form.description') + ':'"
              prop="description"
            >
              <el-input
                v-model="form.description"
                class="w-96"
                maxlength="255"
                show-word-limit
                type="textarea"
                :rows="2"
                :placeholder="$t('form.pleaseInputDescription')"
              />
            </el-form-item>
            <el-divider />
            <el-form-item>
              <div class="w-full">
                <div class="w-full">
                  <el-radio-group
                    v-model="vmAddType"
                    :size="$store.state.nextStack.viewSize.main"
                  >
                    <el-radio-button
                      :label="0"
                      :value="0"
                      style="padding: 9px 6px"
                      >{{ $t("form.quickCreate") }}</el-radio-button
                    >
                    <el-radio-button :label="1" :value="1">{{
                      $t("form.customCreate")
                    }}</el-radio-button>
                  </el-radio-group>
                </div>
              </div>
            </el-form-item>
            <el-form-item
              :label="$t('form.flavor') + ':'"
              prop="flavorId"
              class="relative"
            >
              <el-radio-group v-model="flavorsListType" size="small">
                <el-radio-button label="info" value="info">{{
                  $t("form.generalComputingType")
                }}</el-radio-button>

                <el-radio-button label="gpu" value="gpu">{{
                  $t("form.gpuComputingType")
                }}</el-radio-button>
              </el-radio-group>
              <el-button
                class="absolute z-10 right-2"
                :size="$store.state.nextStack.viewSize.tabChange"
                type="primary"
                @click="clearFilter"
                >{{ $t("form.resetSort") }}</el-button
              >
              <div class="mt-4">
                <el-table
                  ref="singleTableRef"
                  max-height="300px"
                  :size="$store.state.nextStack.viewSize.main"
                  :data="flavorsList"
                  highlight-current-row
                  style="width: 100%"
                  class="mt-4"
                  @current-change="flavorsHandleCurrentChange"
                >
                  <el-table-column label="" width="40px">
                    <template #default="scope">
                      <span
                        v-if="scope.row.flavorId != form.flavorId"
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
                    </template>
                  </el-table-column>
                  <el-table-column prop="date" :label="$t('form.name')">
                    <template #default="scope">
                      <router-link
                        :to="'/flavors/' + scope.row.flavorId"
                        class="text-blue-400"
                        >{{ scope.row.name || "-" }}</router-link
                      >
                    </template>
                  </el-table-column>
                  <el-table-column prop="type" :label="$t('form.type')">
                    <template #default="scope">
                      <span v-if="scope.row.gpuCount">{{
                        $t("form.gpuComputingType")
                      }}</span>
                      <span v-else>{{ $t("form.generalComputingType") }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="cpu"
                    :label="$t('form.cpu')"
                    :filters="flavorCpu"
                    :filter-method="filterCpu"
                  >
                    <template #default="scope">
                      {{ scope.row.cpu || "-" }} {{ $t("unit.cpu") }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="mem"
                    :label="$t('form.mem')"
                    :filters="flavorMem"
                    :filter-method="filterMem"
                  >
                    <template #default="scope">
                      {{ scope.row.mem || "-" }} {{ $t("unit.mem") }}
                    </template>
                  </el-table-column>
                  <!--
                  <el-table-column prop="rootDisk" label="根盘" :filters="flavorDisk" :filter-method="filterDisk">
                    <template #default="scope">
                      {{ scope.row.rootDisk || "-" }}GB
                    </template>
                  </el-table-column> -->
                  <el-table-column prop="gpuCount" :label="$t('form.gpu')">
                    <template #default="scope">
                      <span v-if="scope.row.gpuCount && scope.row.gpuCount > 0">
                        {{ scope.row.gpuName }}*{{ scope.row.gpuCount }}
                      </span>
                      <span v-else>-</span>
                    </template>
                  </el-table-column>
                </el-table>
                <span class="align-text-top mr-4 text-gray-500">
                  {{ $t("form.selected") }}：{{ currentFlavorRow.name }}
                </span>
              </div>
            </el-form-item>
            <el-form-item
              v-if="vmAddType == 1"
              :label="$t('form.computeNode')"
              :prop="vmAddType == 1 ? '' : 'nodeId'"
            >
              <div class="w-full">
                <el-table
                  ref="nodeTableRef"
                  :size="$store.state.nextStack.viewSize.main"
                  :data="vmsHypervisorNodesList"
                  max-height="300px"
                  highlight-current-row
                  class="w-full mt-2"
                  @current-change="nodeHandleCurrentChange"
                >
                  <el-table-column label="" width="40px">
                    <template #default="scope">
                      <span
                        v-if="scope.row.nodeId != form.nodeId"
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
                    </template>
                  </el-table-column>
                  <el-table-column prop="date" :label="$t('form.name')">
                    <template #default="scope">
                      <span class=" ">{{ scope.row.name }}</span>
                    </template>
                  </el-table-column>

                  <el-table-column
                    prop="cpuAllocation"
                    :label="$t('form.availableCpu')"
                    width="160"
                  >
                    <template #default="scope">
                      {{
                        (scope.row.cpuLogCount ? scope.row.cpuLogCount : 0) -
                        (scope.row.usedCpuSum ? scope.row.usedCpuSum : 0)
                      }}
                      {{ $t("unit.cpu") }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="memAllocation"
                    :label="$t('form.availableMem')"
                  >
                    <template #default="scope">
                      {{
                        (scope.row.memTotal ? scope.row.memTotal : 0) -
                        (scope.row.usedMemSum ? scope.row.usedMemSum : 0)
                      }}
                      {{ $t("unit.mem") }}
                    </template>
                  </el-table-column>
                  <el-table-column
                    prop="availableGpuCount"
                    :label="$t('form.availableGpu')"
                  >
                    <template #default="scope">
                      <span>{{ scope.row.availableGpuCount }}</span>
                    </template>
                  </el-table-column>
                  <el-table-column prop="manageIp" label="IP" />
                </el-table>
                <span class="align-text-top mr-4 text-gray-500">
                  {{ $t("form.selected") }}：{{
                    currentNodeRow.nodeName || currentNodeRow.name
                  }}
                </span>
              </div>
            </el-form-item>

            <el-form-item :label="$t('form.bootSource') + ':'">
              <el-radio-group
                :value="0"
                :size="$store.state.nextStack.viewSize.main"
              >
                <el-radio-button :label="0" :value="0">{{
                  $t("form.image")
                }}</el-radio-button>
              </el-radio-group>
            </el-form-item>

            <el-form-item
              :label="$t('form.os') + ':'"
              :prop="nowImageType == 0 ? 'imageOsType' : 'imageId'"
            >
              <div class="w-full">
                <el-row>
                  <el-col
                    v-for="(item, index) in imagesTypeList"
                    :key="index"
                    :span="4"
                  >
                    <div class="text-center" @click="imagesTypeChange(item)">
                      <!-- 置灰 -->
                      <span
                        class="text-xl block"
                        style="filter: grayscale(100%)"
                      >
                        <span class="text-xl block">
                          <i
                            v-if="item.value == 0"
                            class="iconfont icon-actions-thumbnail--1 inline-block"
                            style="color: rgba(0, 120, 212, 1); font-size: 22px"
                          />

                          <i
                            v-if="item.value == 1"
                            class="iconfont icon-windows-fill inline-block"
                            style="color: rgba(0, 120, 212, 1); font-size: 22px"
                          />
                          <i
                            v-if="item.value == 2"
                            class="iconfont icon-ubuntu_ inline-block"
                            style="color: rgba(0, 120, 212, 1); font-size: 22px"
                          />
                          <i
                            v-if="item.value == 3"
                            class="iconfont icon-centos-stream inline-block"
                            style="color: rgba(0, 120, 212, 1); font-size: 22px"
                          />
                        </span>
                      </span>
                      <span
                        class="text-xs"
                        :class="
                          nowImageType == item.value
                            ? 'text-blue-500 font-bold'
                            : ''
                        "
                      >
                        {{ item.label }}
                      </span>
                    </div>
                  </el-col>
                </el-row>
              </div>
              <el-table
                v-if="nowImageType == 0"
                ref="nodeTableRef"
                :size="$store.state.nextStack.viewSize.main"
                :data="emptyImagesList"
                max-height="300px"
                highlight-current-row
                style="width: 100%"
                @current-change="EmptyImagesHandleCurrentChange"
              >
                <el-table-column label="" width="40px">
                  <template #default="scope">
                    <span
                      v-if="scope.row.typeValue != form.imageOsType"
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
                  </template>
                </el-table-column>
                <el-table-column prop="date" :label="$t('form.name')">
                  <template #default="scope">
                    <span class=" ">{{ scope.row.label }}</span>
                  </template>
                </el-table-column>
              </el-table>
              <el-table
                v-if="nowImageType != 0"
                ref="nodeTableRef"
                :size="$store.state.nextStack.viewSize.main"
                :data="imagesList"
                max-height="300px"
                highlight-current-row
                style="width: 100%"
                @current-change="imagesHandleCurrentChange"
              >
                <el-table-column label="" width="40px">
                  <template #default="scope">
                    <span
                      v-if="scope.row.imageId != form.imageId"
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
                  </template>
                </el-table-column>
                <el-table-column prop="date" :label="$t('form.name')">
                  <template #default="scope">
                    <span class=" ">{{ scope.row.imageName }}</span>
                  </template>
                </el-table-column>
              </el-table>
              <span class="align-text-top mr-4 text-gray-500">
                {{ $t("form.selected") }}：<span v-if="nowImageType == 0"
                  >{{
                    form.imageOsType == 1
                      ? "Windows"
                      : form.imageOsType == 0
                      ? "Linux"
                      : ""
                  }}{{ $t("form.blankInstance") }}</span
                >
                <span v-if="nowImageType != 0">{{
                  currentImageRow.imageName
                }}</span>
              </span>
            </el-form-item>
            <el-divider />
            <el-form-item
              :label="$t('form.storagePool') + ':'"
              prop="storagePoolId"
            >
              <el-select
                v-model="form.storagePoolId"
                class="ml-0 w-60"
                :disabled="true"
                :placeholder="$t('form.pleaseSelectStoragePool')"
                @change="changeStoragePool"
              >
                <el-option
                  v-for="(item, index) in storagePoolsList"
                  :key="index"
                  :label="item.name"
                  :value="item.poolId"
                />
              </el-select>
            </el-form-item>
            <div class="overflow-hidden">
              <el-form-item
                :label="$t('form.dataDisk') + ':'"
                :prop="'diskInfos'"
                class="w-96"
              >
                <template #label>
                  <span class="">{{ $t("form.restoreDataDisk") }}</span>
                </template>
                <el-form-item label="" class="w-96">
                  <div class="w-96">
                    {{ drawerData.name }} ({{ drawerData.size
                    }}{{ $t("unit.disk") }})
                  </div>
                </el-form-item>
              </el-form-item>
            </div>
            <div
              v-for="(item, index) in form.diskInfos"
              :key="index"
              class="overflow-hidden"
            >
              <el-form-item
                :label="$t('form.dataDisk') + ':'"
                :prop="'diskInfos'"
                required
                class="w-96"
              >
                <template #label>
                  <span v-show="index == 0" class="">{{
                    $t("form.dataDisk")
                  }}</span>
                </template>
                <el-form-item
                  v-if="!item.diskStatus"
                  label=""
                  :prop="'diskInfos.' + index + '.volumeId'"
                  class="w-96"
                  :rules="{
                    required: true,
                    message: $t('form.pleaseSelect'),
                    trigger: 'change',
                  }"
                >
                  <div class="w-96">
                    <el-select
                      v-model="item.volumeId"
                      class="float-left w-60"
                      :placeholder="$t('form.pleaseSelect')"
                    >
                      <el-option
                        v-for="(item, index) in volumesList"
                        :key="index"
                        :disabled="getCheckedVolumes(item.volumeId)"
                        :label="item.name + ' (' + item.size + 'GB)'"
                        :value="item.volumeId"
                      />
                    </el-select>
                    <div class="overflow-hidden block text-right mt-2 ml-2">
                      <div
                        class="overflow-hidden float-left h-4 mx-2"
                        style="margin-top: 8px"
                      >
                        <el-button
                          size="small"
                          circle
                          class="align-top p-0 w-4 h-4 flex-center"
                          type="danger"
                          @click="removeData(form.diskInfos, index)"
                        >
                          <i class="iconfont icon-jianshao- iconClose" />
                        </el-button>
                      </div>
                    </div>
                  </div>
                </el-form-item>
                <el-form-item
                  v-if="item.diskStatus"
                  label=""
                  :prop="'diskInfos.' + index + '.size'"
                  class="w-96"
                  :rules="{
                    required: true,
                    message: '请输入数据盘大小',
                    trigger: 'blur',
                  }"
                >
                  <div class="w-96">
                    <el-input-number
                      v-model="item.size"
                      :min="1"
                      :max="2147483647"
                      :precision="0"
                      controls-position="right"
                      class="float-left w-60"
                    />
                    <span class="pl-2 float-left">GB</span>
                    <div class="overflow-hidden block text-right mt-2 ml-2">
                      <div
                        class="overflow-hidden float-left h-4 mx-2"
                        style="margin-top: 8px"
                      >
                        <el-button
                          size="small"
                          circle
                          class="align-top p-0 w-4 h-4 flex-center"
                          type="danger"
                          @click="removeData(form.diskInfos, index)"
                        >
                          <i class="iconfont icon-jianshao- iconClose" />
                        </el-button>
                      </div>
                    </div>
                  </div>
                </el-form-item>
              </el-form-item>
            </div>
            <el-form-item>
              <el-button
                size="small"
                class=""
                :disabled="!form.storagePoolId"
                type="text"
                @click="addDisk(false)"
              >
                <i class="el-icon-plus" />{{
                  $t("form.mountExistingCloudDisk")
                }}
              </el-button>
              <el-button
                size="small"
                class=""
                :disabled="!form.storagePoolId"
                type="text"
                @click="addDisk(true)"
              >
                <i class="el-icon-plus" />{{ $t("form.createDataDisk") }}
              </el-button>
            </el-form-item>
          </div>
        </el-card>
        <el-card class="!border-none mb-3">
          <template #header>
            <div class="">
              <span>{{ $t("form.networkConfig") }}</span>
            </div>
          </template>
          <div class="text item">
            <div
              v-for="(item, index) in form.networkInfos"
              :key="index"
              class="overflow-hidden"
            >
              <el-form-item
                :label="$t('form.network') + ':'"
                :prop="'networkInfos.' + index + '.subnetId'"
                required
                class="float-left w-80"
                :rules="{
                  required: true,
                  message: '请选择子网',
                  trigger: 'change',
                }"
              >
                <template #label>
                  <span class="">{{ $t("form.network") }}</span>
                  <div class="block text-right">
                    <!-- <div class="overflow-hidden h-6"
                       v-show="form.networkInfos.length - 1 == index">
                    <el-button size="small"
                               circle
                               class="align-top p-0 w-4 h-4"
                               type="primary"
                               @click="addData()">
                       <i class="el-icon-plus"></i>
                    </el-button>
                  </div> -->
                    <div
                      v-show="form.networkInfos.length > 1 && index > 0"
                      class="overflow-hidden h-6 float-right"
                    >
                      <el-button
                        size="small"
                        circle
                        class="align-top p-0 w-4 h-4 flex-center"
                        type="danger"
                        @click="removeData(form.networkInfos, index)"
                      >
                        <i class="iconfont icon-jianshao- iconClose" />
                      </el-button>
                    </div>
                  </div>
                </template>
                <el-select
                  v-model="item.vpcId"
                  class="m-2 ml-0 w-60"
                  :placeholder="$t('form.pleaseSelect')"
                  @change="vpcIdChange(form.networkInfos, index)"
                >
                  <el-option
                    v-for="(vpcItem, index) in vpcList"
                    :key="index"
                    :label="vpcItem.name + ' (' + vpcItem.cidr + ')'"
                    :value="vpcItem.vpcId"
                  />
                </el-select>
                <el-select
                  v-model="item.subnetId"
                  :disabled="!item.vpcId"
                  class="m-2 ml-0 w-60"
                  :placeholder="$t('form.pleaseSelect')"
                  @change="staticChange(form.networkInfos, index)"
                >
                  <template v-for="(subnetItem, index) in subnetsDataList">
                    <el-option
                      v-if="item.vpcId == subnetItem.vpcId"
                      :key="index"
                      :label="subnetItem.name + ' (' + subnetItem.cidr + ')'"
                      :value="subnetItem.subnetId"
                    />
                  </template>
                </el-select>
              </el-form-item>
              <div class="float-left">
                <el-form-item :label="$t('form.ipAllocation') + ':'" required>
                  <el-radio-group
                    v-model="item.staticStatus"
                    @change="staticChange(form.networkInfos, index)"
                  >
                    <el-radio :label="0" :value="0">{{
                      $t("form.fixedIP")
                    }}</el-radio>
                    <el-radio :label="1" :value="1">{{
                      $t("form.dynamicAllocation")
                    }}</el-radio>
                  </el-radio-group>
                  <!-- <el-checkbox v-model="item.isVip"
                           :disabled="(index==0)"
                           class="ml-8"
                           label="虚拟IP" /> -->
                </el-form-item>
                <el-form-item
                  v-show="item.staticStatus === 0"
                  :label="$t('form.fixedIP') + ':'"
                  required
                >
                  <el-input
                    v-model="item.ip1"
                    class="w-14"
                    :controls="false"
                    :step="1"
                    :min="0"
                    :max="255"
                    step-strictly
                    @input="changeIp(form.networkInfos, index)"
                  />
                  <span class="text-xl px-1">.</span>
                  <el-input
                    v-model="item.ip2"
                    class="w-14"
                    :controls="false"
                    :step="1"
                    :min="0"
                    :max="255"
                    step-strictly
                    @input="changeIp(form.networkInfos, index)"
                  />
                  <span class="text-xl px-1">.</span>
                  <el-input
                    v-model="item.ip3"
                    class="w-14"
                    :controls="false"
                    :step="1"
                    :min="0"
                    :max="255"
                    step-strictly
                    @input="changeIp(form.networkInfos, index)"
                  />
                  <span class="text-xl px-1">.</span>

                  <el-tooltip
                    v-model="item.ipStatus"
                    :manual="true"
                    placement="right"
                  >
                    <div slot="content">
                      <span
                        class="inline-block w-4 h-4 bg-red-500 rounded-1/2 text-right leading-tight"
                        >！</span
                      >
                      {{ $t("form.ipNotInRange") }}
                      <span v-if="subnetsDataList.length > 0 && item.subnetId">
                        {{
                          subnetsDataList.filter((v) => {
                            return item.subnetId === v.subnetId;
                          })[0].cidr
                        }}
                      </span>
                    </div>
                    <el-input
                      v-model="item.ip4"
                      class="w-14"
                      :controls="false"
                      :step="1"
                      :min="0"
                      :max="255"
                      step-strictly
                      @input="changeIp(form.networkInfos, index)"
                    />
                  </el-tooltip>
                </el-form-item>
              </div>
            </div>
            <el-form-item class="-mt-2">
              <el-button size="small" class="" type="text" @click="addData()">
                <i class="el-icon-plus" />{{ $t("form.addNetwork") }}
              </el-button>
            </el-form-item>
            <el-form-item :label="$t('form.securityGroup') + ':'" prop="sgIds">
              <el-select
                v-model="form.sgIds"
                multiple
                class="m-2 ml-0 w-60"
                :placeholder="$t('form.pleaseSelect')"
              >
                <el-option
                  v-for="(item, index) in secGroupsList"
                  :key="index"
                  :label="item.name"
                  :value="item.sgId"
                />
              </el-select>
            </el-form-item>
          </div>
        </el-card>
        <el-card class="!border-none mb-3">
          <template #header>
            <div class="">
              <span>{{ $t("form.advancedConfig") }}</span>
            </div>
          </template>
          <div class="text item">
            <el-form-item :label="$t('form.hostname') + ':'" prop="hostname">
              <el-input
                v-model="form.hostname"
                class="w-60"
                :placeholder="$t('form.pleaseInput')"
              />
            </el-form-item>
            <el-form-item
              :label="$t('form.loginName') + ':'"
              prop="sysUsername"
            >
              <el-input
                v-model="form.sysUsername"
                class="w-60"
                :disabled="isOs == 1"
                :placeholder="$t('form.pleaseInput')"
              />
            </el-form-item>

            <el-form-item :label="$t('form.loginCredential') + ':'">
              <el-radio-group v-model="loginType">
                <el-radio :label="true" :value="true">{{
                  $t("form.password")
                }}</el-radio>
                <el-radio
                  :label="false"
                  :disabled="nowImageTypeOs == 1"
                  :value="false"
                  >{{ $t("form.keyPair") }}</el-radio
                >
              </el-radio-group>
            </el-form-item>
            <el-form-item
              v-if="loginType"
              :label="$t('form.password') + ':'"
              prop="isPassword"
              :rules="[
                { required: true, validator: validatePass2, trigger: 'change' },
                { required: true, validator: validatePass2, trigger: 'blur' },
              ]"
            >
              <el-input
                v-model="isPassword"
                class="w-60"
                type="password"
                :placeholder="$t('form.pleaseInput')"
              />
            </el-form-item>
            <el-form-item
              v-if="loginType"
              :label="$t('form.confirmPassword') + ':'"
              prop="sysPassword"
            >
              <el-input
                v-model="form.sysPassword"
                class="w-60"
                type="password"
                :placeholder="$t('form.pleaseInput')"
              />
            </el-form-item>

            <el-form-item
              v-if="!loginType"
              :label="$t('form.keyPair') + ':'"
              prop="pubkeyId"
            >
              <el-select
                v-model="form.pubkeyId"
                class="m-2 ml-0"
                :placeholder="$t('form.pleaseSelect')"
              >
                <el-option
                  v-for="(item, index) in pubkeysDataList"
                  :key="index"
                  :label="item.name"
                  :value="item.pubkeyId"
                />
              </el-select>
            </el-form-item>
          </div>
        </el-card>
      </el-form>
    </div>
    <div
      class="absolute z-30 px-3 py-2 bg-white"
      style="
        bottom: -15px;
        right: -10px;
        left: -9px;
        border-top: 1px solid rgba(0, 0, 0, 0.09);
        box-shadow: 0 2px 30px 0 rgba(0, 0, 0, 0.09);
      "
    >
      <el-row>
        <el-col :span="12">
          <div class="text-right">
            <el-button type="primary" size="small" @click="toVmAdd()">{{
              $t("form.createImmediately")
            }}</el-button>
          </div>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import filtersFun from "@/utils/statusFun";
import { getInquiry } from "@/api/bill/discounts";
import netmask from "netmask";
var Netmask = netmask.Netmask;

export default {
  components: {},
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputPassword")));
      } else {
        const reg =
          /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\|\;\:\'\"\,\.\<\>\/\?])[\da-zA-Z\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\|\;\:\'\"\,\.\<\>\/\?]{8,20}$/;

        if (value !== isPassword.value) {
          callback(new Error(this.$t("form.passwordNotMatch")));
        } else if (!reg.test(value)) {
          callback(new Error(this.$t("form.passwordLength")));
        } else {
          callback();
        }
      }
    };
    const validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error(this.$t("form.pleaseInputPassword")));
      } else {
        const reg =
          /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\|\;\:\'\"\,\.\<\>\/\?])[\da-zA-Z\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\|\;\:\'\"\,\.\<\>\/\?]{8,20}$/;

        if (!reg.test(value)) {
          callback(new Error(this.$t("form.passwordLength")));
        } else {
          callback();
        }
      }
    };
    return {
      validatePass2: validatePass2,

      loading: false,
      drawerData: {},
      currentFlavorRow: "", // 规格选择 当前行
      nowImageTypeOs: "", // 当前镜像类型
      loginType: true,
      isPassword: "",
      isOs: 0,
      nodeIdAuto: 0,
      vmAddType: 0,
      imagesTypeList: [
        {
          label: "Windows",
          type: "Windows",
          typeValue: 1,
          vendor: 0,
          value: 1,
        },
        {
          label: "Ubuntu",
          type: "Linux",
          typeValue: 0,
          vendor: 2,
          value: 2,
        },
        {
          label: "Centos",
          type: "Linux",
          typeValue: 0,
          vendor: 1,
          value: 3,
        },
        {
          label: this.$t("form.blankInstance"),
          type: "",
          typeValue: 0,
          vendor: "",
          value: 0,
        },
      ],
      emptyImagesList: [
        {
          label: "Windows",
          type: "Windows",
          typeValue: 1,
          id: 1,
        },
        {
          label: "Linux",
          type: "Linux",
          typeValue: 0,
          id: 2,
        },
      ],
      form: {
        // 表单
        name: "",
        description: "",
        flavorId: "", // 规格ID
        nodeId: "", // 计算节点ID
        imageOsType: 0, // 镜像类型
        imageId: "", // 镜像ID

        hostname: "", // 主机名
        sysUsername: "", // 系统用户名
        sysPassword: "", // 系统密码
        pubkeyId: "", // 密钥对ID
        sgIds: [], // 安全组ID
        networkInfos: [
          {
            vpcId: "", // VPC网络ID
            subnetId: "", // 子网ID
            staticIp: "", // IP分配
            staticStatus: 1, // IP分配类型
            // isVip: false,

            ip1: 0, // IP分配
            ip2: 0, // IP分配
            ip3: 0, // IP分配
            ip4: 0, // IP分配
            ipStatus: false, // IP状态
          },
        ],
        storagePoolId: "", // 存储池ID
        diskInfos: [], // 云硬盘
      },
      singleTableRef: "",
      volumesList: [], // 云硬盘列表
      storagePoolsList: [], // 存储池列表
      CpuList: [], // CPU列表
      MemList: [], // 内存列表
      DiskList: [], // 磁盘列表
      flavorCpu: [], // 规格CPU
      flavorMem: [], // 规格内存
      flavorDisk: [], // 规格磁盘

      vpcList: [], // VPC网络列表
      imagesList: [], // 镜像列表
      subnetsDataList: [], // 子网列表
      pubkeysDataList: [], // 密钥对列表
      flavorsList: [], // 规格列表
      flavorsListData: [], // 规格列表
      flavorsListType: "info", // 规格列表类型
      vmsHypervisorNodesList: [], // 计算节点列表
      secGroupsList: [], // 安全组列表
      rules: {
        name: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
        flavorId: [
          {
            required: true,
            message: this.$t("form.pleaseSelectFlavor"),
            trigger: "change",
          },
        ],
        nodeId: [
          {
            required: true,
            message: this.$t("form.pleaseSelectNode"),
            trigger: "change",
          },
        ],
        imageOsType: [
          {
            required: true,
            message: this.$t("form.pleaseSelectImageType"),
            trigger: "change",
          },
        ],
        imageId: [
          {
            required: true,
            message: this.$t("form.pleaseSelectImage"),
            trigger: "change",
          },
        ],
        hostname: [
          {
            required: true,
            message: this.$t("form.pleaseInputHostname"),
            trigger: "blur",
          },
          {
            min: 3,
            max: 64,
            message: this.$t("message.pleaseInput364"),
            trigger: "blur",
          },
        ],
        sysUsername: [
          {
            required: true,
            message: this.$t("form.pleaseInputLoginName"),
            trigger: "blur",
          },
          {
            min: 3,
            max: 64,
            message: this.$t("message.pleaseInput364"),
            trigger: "blur",
          },
        ],
        sysPassword: [
          { required: true, validator: validatePass, trigger: "change" },
          { required: true, validator: validatePass, trigger: "blur" },
        ],

        sgIds: [
          {
            required: true,
            message: this.$t("form.pleaseSelectSecurityGroup"),
            trigger: "change",
          },
        ],

        storagePoolId: [
          {
            required: true,
            message: this.$t("form.pleaseSelectStoragePool"),
            trigger: "change",
          },
        ],
      },
      nowImageType: 0,
      currentImageRow: "",
      currentNodeRow: "",
      vmsHypervisorNodesLoading: false,
    };
  },
  watch: {
    loginType: {
      handler(val) {
        if (val) {
          this.form.pubkeyId = "";
        }
      },
      immediate: true,
    },
    vmAddType: {
      handler(val) {
        this.currentNodeRow = "";
        this.form.nodeId = "";
      },
    },
    flavorsListType: {
      handler(val) {
        this.currentFlavorRow = "";
        this.form.flavorId = "";
        this.filterFlavor();
      },
    },
    "form.imageOsType": {
      handler(val) {
        this.nowImageTypeOs = this.form.imageOsType;
        this.loginType = true;
        if (this.form.imageOsType === 1) {
          this.form.sysUsername = "Administrator";
        } else {
          this.form.sysUsername = "cloud";
        }
      },
      immediate: true,
    },
  },

  created() {
    this.getDetail();

    this.getVpcList(); // VPC列表
    // this.getImageList(); //镜像列表
    this.getSubNetList(); // 子网列表
    this.getpubkeysList(); // 公钥列表
    this.getFlavorList(); // 规格列表
    this.getVolumesList(this.form.storagePoolId); // 云盘列表
    this.getStoragePools(); // 存储池列表
    this.getsecGroupsList(); // 安全组列表
  },
  mounted() {},

  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    imagesTypeChange(item) {
      return; // 恢复系统不需要变更
      this.form.imageId = "";
      this.form.imageOsType = item.typeValue;
      this.nowImageType = item.value;
      this.imagesList = [];
      if (item.value != 0) {
        this.getImageList();
      }
    },
    EmptyImagesHandleCurrentChange(val) {
      this.currentImageRow = val;
      this.form.imageId = "";
      this.form.imageOsType = val.typeValue;
    },
    imagesHandleCurrentChange(val) {
      if (val) {
        this.currentImageRow = val;
        this.form.imageId = val.imageId;
      }
    },
    staticChange(item, index) {
      if (item[index].staticStatus === 1) {
        item[index].ipStatus = false;
      } else {
        this.changeIp(item, index);
      }
    },
    ipToint(ip) {
      const num = 0;
      ip = ip.split(".");
      num =
        Number(ip[0]) * 256 * 256 * 256 +
        Number(ip[1]) * 256 * 256 +
        Number(ip[2]) * 256 +
        Number(ip[3]);
      num = num >>> 0;
      return num;
    },
    changeIp(item, index) {
      // IP变动
      if (item[index].subnetId == "") {
        return;
      }
      item[index].ipStatus = false;
      item[index].ip1 = this.$script.parseIntIpNum(item[index].ip1);
      item[index].ip2 = this.$script.parseIntIpNum(item[index].ip2);
      item[index].ip3 = this.$script.parseIntIpNum(item[index].ip3);
      item[index].ip4 = this.$script.parseIntIpNum(item[index].ip4);
      var ip = `${item[index].ip1}.${item[index].ip2}.${item[index].ip3}.${item[index].ip4}`;

      var subnetIp = this.subnetsDataList.filter((v) => {
        return item[index].subnetId === v.subnetId;
      })[0].cidr;

      const netBlock = new Netmask(subnetIp);

      if (netBlock.contains(ip)) {
        console.log("ok");
        if (
          this.ipToint(ip) > this.ipToint(netBlock.first) &&
          this.ipToint(ip) < this.ipToint(netBlock.last)
        ) {
          console.log("ok");
        } else {
          item[index].ipStatus = true;
        }
      } else {
        item[index].ipStatus = true;
      }
    },
    flavorsHandleCurrentChange(val) {
      if (val) {
        this.currentFlavorRow = val;
        this.form.flavorId = val.flavorId;
        this.getvmsHypervisorNodesList();
      } else {
        this.currentFlavorRow = "";
        this.form.flavorId = "";
        this.vmsHypervisorNodesList = [];
      }
    },
    nodeHandleCurrentChange(val) {
      if (val) {
        this.currentNodeRow = val;
        this.form.nodeId = val.nodeId;
      } else {
        this.currentNodeRow = "";
        this.form.nodeId = "";
      }
    },

    resetForm() {
      // 重置
      this.$refs.addVmForm.resetFields();
      this.isPassword = "";
    },
    vpcIdChange(item, index) {
      // vpc 改变
      item[index].subnetId = "";
    },
    toVmAdd() {
      // 实例add
      var data = JSON.parse(JSON.stringify(this.form));

      data.diskInfos.map((v) => {
        delete v.diskStatus;
      });
      data.diskInfos.unshift({ volumeId: this.drawerData.volumeId });

      var ipStatusData = [];
      if (this.nodeIdAuto == 0) {
        delete data.nodeId;
      }
      data.networkInfos.forEach((v) => {
        ipStatusData.push(v.ipStatus);
        if (v.staticStatus === 0) {
          // 固定IP or 动态分配
          v.staticIp = `${v.ip1}.${v.ip2}.${v.ip3}.${v.ip4}`;
        } else {
          v.staticIp = "";
        }
        delete v.ip1;
        delete v.ip2;
        delete v.ip3;
        delete v.ip4;
        delete v.ipStatus;
        delete v.staticStatus;
      });

      if (ipStatusData.includes(true)) {
        return;
      }
      if (!this.loginType) {
        data.sysPassword = "";
      }

      this.loading = true;

      this.$refs.addVmForm.validate(async (valid) => {
        if (valid) {
          mainApi
            .vmsRenews(data)
            .then((res) => {
              this.loading = false;
              this.$notify({
                title: this.$t("message.startRestore"),
                type: "success",
                duration: 2500,
              });
              this.resetForm();
              this.$router.push({
                path: "/nextStack/volumes",
              });
            })
            .catch((error) => {
              this.loading = false;
            });
        } else {
          this.loading = false;
        }
      });
    },
    getVpcList() {
      // VPC列表
      mainApi
        .vpcList({ vpc_phase: 1, page_num: 1, page_size: 1000 })
        .then((res) => {
          this.vpcList = res.vpcs;
        })
        .catch((error) => {});
    },
    getImageList() {
      // 镜像列表
      var data = {
        image_type: this.nowImageTypeOs,
        page_num: 1,
        page_size: 1000,
      };
      mainApi
        .imageList(data)
        .then((res) => {
          this.imagesList = res;
        })
        .catch((error) => {});
    },
    getSubNetList() {
      // 子网列表
      mainApi
        .subnetsList({ page_num: 1, page_size: 1000 })
        .then((res) => {
          this.subnetsDataList = res.subnets ? res.subnets : [];
        })
        .catch((error) => {});
    },
    getpubkeysList() {
      // 密钥对列表
      mainApi
        .pubkeysList({ page_num: 1, page_size: 1000 })
        .then((res) => {
          this.pubkeysDataList = res.pubkeys;
        })
        .catch((error) => {});
    },
    filterFlavor() {
      if (this.flavorsListType == "info") {
        this.flavorsList = this.flavorsListData.filter((v) => {
          return !v.gpuCount;
        });
      } else {
        this.flavorsList = this.flavorsListData.filter((v) => {
          return v.gpuCount && v.gpuCount > 0;
        });
      }
    },
    getFlavorList() {
      // 规格列表
      mainApi
        .flavorsList({
          type: 1,
          subnet_phase: 1,
          page_num: 1,
          page_size: 99999,
        })
        .then((res) => {
          this.CpuList = [];
          this.MemList = [];
          this.DiskList = [];
          this.flavorsListData = res.flavors;
          this.filterFlavor();
          if (res.flavors && res.flavors.length > 0) {
            this.currentFlavorRow = res.flavors[0];
            this.form.flavorId = res.flavors[0].flavorId;
            this.getvmsHypervisorNodesList();
          }
          res.flavors.forEach((item) => {
            this.CpuList.push(item.cpu);
            this.MemList.push(item.mem);
            this.DiskList.push(item.rootDisk);
          });
          this.flavorCpu = Array.from(new Set(this.CpuList))
            .sort((a, b) => a - b)
            .map((item) => {
              return {
                label: item + this.$t("unit.cpu"),
                value: item,
              };
            });
          this.flavorMem = Array.from(new Set(this.MemList))
            .sort((a, b) => a - b)
            .map((item) => {
              return {
                label: item + this.$t("unit.mem"),
                value: item,
              };
            });
          this.flavorDisk = Array.from(new Set(this.DiskList))
            .sort((a, b) => a - b)
            .map((item) => {
              return {
                label: item + "GB",
                value: item,
              };
            });
        })
        .catch((error) => {});
    },
    clearFilter() {
      this.$refs.singleTableRef.clearFilter();
    },
    filterCpu(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    filterMem(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    filterDisk(value, row, column) {
      const property = column["property"];
      return row[property] === value;
    },
    addData() {
      // 添加数据
      this.form.networkInfos.push({
        vpcId: "", // VPC网络ID
        subnetId: "", // 子网ID
        staticIp: "", // IP分配
        staticStatus: 1, // IP分配类型
        // isVip: false,
        ip1: 0, // IP分配
        ip2: 0, // IP分配
        ip3: 0, // IP分配
        ip4: 0, // IP分配
        ipStatus: false, // IP状态
      });
      this.$forceUpdate();
    },
    addDisk(status) {
      // 添加数据盘
      if (this.form.diskInfos.length >= 4) {
        this.$notify({
          title: this.$t("form.max4DataDisk"),
          type: "warning",
          duration: 2500,
        });
        return;
      }
      this.form.diskInfos.push({
        diskStatus: status, // 状态
        size: 10, // 数据盘大小
        diskType: 0, // 数据盘类型
        volumeId: "", // 云盘ID
      });
      this.$forceUpdate();
    },
    removeData(data, index) {
      // 删除数据
      data.splice(index, 1);
    },
    getCheckedVolumes(data) {
      // 获取选中的云盘
      var volumeIdData = this.form.diskInfos.map((item, index) => {
        return item.volumeId;
      });
      return volumeIdData.includes(data);
    },
    getsecGroupsList() {
      // 安全组列表

      mainApi
        .sgsList({ page_num: 1, page_size: 99999 })
        .then((res) => {
          this.secGroupsList = res.securityGroups;
          if (res.securityGroups && res.securityGroups.length > 0) {
            this.form.sgIds = [res.securityGroups[0].sgId];
          }
        })
        .catch((error) => {
          this.loading = false;
        });
    },
    getvmsHypervisorNodesList() {
      // 计算节点列表
      if (!this.currentFlavorRow) {
        return;
      }
      this.currentNodeRow = "";
      this.form.nodeId = "";
      this.vmsHypervisorNodesLoading = true;

      mainApi
        .vmsHypervisorNodesAllocation({
          flavor_id: this.currentFlavorRow.flavorId,
          page_num: 1,
          page_size: 99999,
        })
        .then((res) => {
          this.vmsHypervisorNodesList = res.nodeAllocationInfos;

          this.vmsHypervisorNodesLoading = false;
        })
        .catch((error) => {
          this.vmsHypervisorNodesLoading = false;
        });
    },
    changeStoragePool(storagePoolId) {
      // 改变存储池
      this.form.diskInfos = this.form.diskInfos.filter((item) => {
        return item.diskStatus === true;
      });
      this.getVolumesList(storagePoolId);
    },
    getVolumesList(storagePoolId) {
      // 云盘列表
      mainApi
        .volumesList({
          storage_pool_id: storagePoolId,
          detached: true,
          page_num: 1,
          page_size: 99999,
        })
        .then((res) => {
          this.volumesList = res.volumes;
        });
    },
    getStoragePools() {
      // 存储池列表
      mainApi
        .storagePoolsList({ page_num: 1, page_size: 99999 })
        .then((res) => {
          this.storagePoolsList = res.storagePools;
          if (res.storagePools && res.storagePools.length > 0) {
            this.form.storagePoolId = res.storagePools[0].poolId;
          }
        });
    },

    getDetail() {
      // 获取详情
      var id = this.$route.params.id;
      mainApi
        .volumesDetail(id)
        .then((res) => {
          this.drawerData = res;
          this.form.storagePoolId = res.storagePoolId;
          this.form.imageOsType = res.imageOsType;
          this.isOs = res.imageOsType;

          this.nowImageType = res.imageOsType;
          if (res.imageOsVendor === 0) {
            this.nowImageType = 1;
            this.form.imageId = res.imageId;
          } else if (res.imageOsVendor === 1) {
            this.nowImageType = 3;
            this.form.imageId = res.imageId;
          } else if (res.imageOsVendor === 2) {
            this.nowImageType = 2;
            this.form.imageId = res.imageId;
          } else {
            this.nowImageType = 0;
            this.emptyImagesList = this.emptyImagesList.filter((item) => {
              return item.typeValue === res.imageOsType;
            });
          }

          if (res.imageOsType == 1) {
            this.form.sysUsername = "Administrator";
          } else {
            this.form.sysUsername = "cloud";
          }
        })
        .catch((error) => {});
    },
  },
};
</script>

<style lang="scss" scoped>
.vmAddPage {
}
</style>
