<template>
  <div class="mt-2 relative" style="height: calc(100vh - 144px - 0.5rem)">
    <div
      id="vm_screen"
      class="mb-2 p-2 overflow-auto bg-white"
      shadow="never"
      style="height: calc((100vh - 185px) - 0.5rem)"
    >
      <el-row :gutter="20" style="margin: 0px; min-height: 100vh">
        <el-col :span="18">
          <el-row>
            <el-col :span="24">
              <div class="py-6 mx-auto" style="width: 700px">
                <el-steps
                  :active="vmAddStep"
                  class="steps"
                  align-center
                  finish-status="success"
                >
                  <el-step :title="$t('titleAndText.basicInfo')" />
                  <el-step :title="$t('titleAndText.networkConfig')" />
                  <el-step :title="$t('titleAndText.systemConfig')" />
                </el-steps>
              </div>
            </el-col>
            <el-col :span="24">
              <div class="mx-auto" style="max-width: 800px">
                <el-form
                  ref="addVmForm"
                  class="overflow-auto pb-10 addVmFormStyle"
                  :size="$store.state.nextStack.viewSize.main"
                  :model="form"
                  :rules="rules"
                  label-position="top"
                >
                  <div v-if="vmAddStep == 0">
                    <div class="text item">
                      <el-form-item
                        :label="$t('form.region')"
                        prop="nowcloudId"
                      >
                        <div class="w-full">
                          <div class="w-full">
                            <el-radio-group
                              v-model="nowCloudId"
                              class="region"
                              :size="$store.state.nextStack.viewSize.main"
                              @input="nowCloudInputChange"
                            >
                              <el-radio-button
                                v-for="item in cloudListData"
                                :key="item.cloud_id"
                                border
                                :label="item.cloud_id"
                                :value="item.cloud_id"
                                >{{ item.name }}</el-radio-button
                              >
                              <el-radio-button
                                v-if="
                                  !cloudListData || cloudListData.length == 0
                                "
                                border
                                :disabled="true"
                                >{{
                                  $t("titleAndText.noAvailableCloud")
                                }}</el-radio-button
                              >
                            </el-radio-group>
                          </div>
                        </div>
                      </el-form-item>
                      <el-form-item
                        v-if="addType != 'node' && (kind == '0' || kind == '1')"
                      >
                        <div class="w-full">
                          <div class="w-full">
                            <el-radio-group
                              v-model="vmAddType"
                              :size="$store.state.nextStack.viewSize.main"
                            >
                              <el-radio-button :label="0" :value="0">
                                <span style="padding: 9px 6px">{{
                                  $t("titleAndText.quickCreate")
                                }}</span>
                              </el-radio-button>

                              <el-radio-button :label="1" :value="1">{{
                                $t("titleAndText.customCreate")
                              }}</el-radio-button>
                            </el-radio-group>
                          </div>
                        </div>
                      </el-form-item>
                      <el-form-item
                        v-if="addType == 'node'"
                        :label="$t('form.hypervisorNode')"
                      >
                        <div class="w-full">
                          <el-table
                            :size="$store.state.nextStack.viewSize.main"
                            :data="[drawerData]"
                            highlight-current-row
                            class="w-full mt-2"
                          >
                            <el-table-column
                              prop="date"
                              :label="$t('form.name')"
                            >
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
                                  (scope.row.cpuLogCount
                                    ? scope.row.cpuLogCount
                                    : 0) -
                                  (scope.row.usedCpuSum
                                    ? scope.row.usedCpuSum
                                    : 0)
                                }}{{ $t("unit.cpu") }}
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="memAllocation"
                              :label="$t('form.availableMemory')"
                            >
                              <template #default="scope">
                                {{
                                  (scope.row.memTotal
                                    ? scope.row.memTotal
                                    : 0) -
                                  (scope.row.usedMemSum
                                    ? scope.row.usedMemSum
                                    : 0)
                                }}{{ $t("unit.mem") }}
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="availableGpuCount"
                              :label="$t('form.availableGpu')"
                            >
                              <template #default="scope">
                                <span
                                  >{{ scope.row.gpuName }}*{{
                                    scope.row.availableGpuCount
                                  }}</span
                                >
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="manageIp"
                              :label="$t('form.ip')"
                            />
                          </el-table>
                          <span class="align-text-top mr-4 text-gray-500">
                            {{ $t("form.selectedNode") }}：{{
                              drawerData.nodeName || drawerData.name
                            }}
                          </span>
                        </div>
                      </el-form-item>

                      <el-form-item
                        :label="$t('form.flavor')"
                        prop="flavorId"
                        class="relative"
                        label-position="top"
                      >
                        <el-radio-group
                          v-model="flavorsListType"
                          :disabled="
                            addType == 'node' && drawerData.gpuCount > 0
                          "
                          size="small"
                          @change="changeflavorsListType"
                        >
                          <el-radio-button label="gpu" value="gpu">
                            <span>{{ $t("form.gpuCompute") }}</span>
                          </el-radio-button>
                          <el-radio-button label="info" value="info">
                            <span>{{ $t("form.generalCompute") }}</span>
                          </el-radio-button>
                        </el-radio-group>

                        <div class="mt-4">
                          <el-table
                            ref="singleTableRef"
                            :size="$store.state.nextStack.viewSize.main"
                            :data="flavorsList"
                            style="width: 100%"
                            :row-class-name="tableRowClassName"
                            @current-change="flavorsHandleCurrentChange"
                          >
                            <el-table-column label="" width="40px">
                              <template #default="scope">
                                <span
                                  v-if="!scope.row.available"
                                  class="w-3 h-3 block border rounded-sm border-gray-300 disabled-element"
                                  style="background: rgb(245, 247, 250)"
                                />
                                <span v-else>
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
                                </span>
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="date"
                              :label="$t('form.name')"
                            >
                              <template #default="scope">
                                {{ scope.row.name || "-" }}
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="type"
                              :label="$t('form.type')"
                            >
                              <template #default="scope">
                                <span v-if="scope.row.gpuCount">{{
                                  $t("form.gpuCompute")
                                }}</span>
                                <span v-else>
                                  {{ $t("form.generalCompute") }}
                                </span>
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="cpu"
                              :label="$t('form.cpuAndMemory')"
                            >
                              <template #default="scope">
                                {{ scope.row.cpu || "-" }}
                                {{ $t("unit.cpu") }} |
                                {{ scope.row.mem || "-" }}
                                {{ $t("unit.mem") }}
                              </template>
                            </el-table-column>
                            <el-table-column
                              v-if="flavorsListType == 'gpu'"
                              prop="gpuCount"
                              :label="$t('form.gpu')"
                            >
                              <template #default="scope">
                                <span
                                  v-if="
                                    scope.row.gpuCount && scope.row.gpuCount > 0
                                  "
                                >
                                  {{ scope.row.gpuName }}*{{
                                    scope.row.gpuCount
                                  }}
                                </span>
                                <span v-else>-</span>
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="gpuCount"
                              :label="$t('form.ib')"
                            >
                              <template #default="scope">
                                <span v-if="scope.row.needIb">
                                  <i
                                    style="color: rgb(103, 194, 58)"
                                    class="el-icon-success"
                                  />
                                </span>
                                <span v-if="!scope.row.needIb">-</span>
                              </template>
                            </el-table-column>
                          </el-table>

                          <span class="align-text-top mr-4 text-gray-500">
                            {{ $t("form.selected") }}：{{
                              currentFlavorRow.name
                            }}
                          </span>
                        </div>
                      </el-form-item>

                      <el-form-item
                        v-if="vmAddType == 1"
                        :label="$t('form.hypervisorNode')"
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
                                  <!-- <i-dashicons:yes class="text-white w-3.5 h-3.5 -m-0.5 leading-none table"></i-dashicons:yes> -->
                                  <i
                                    class="el-icon-check text-white w-3.5 h-3.5 -m-0.5 leading-none table"
                                  />
                                </span>
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="date"
                              :label="$t('form.name')"
                            >
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
                                  (scope.row.cpuLogCount
                                    ? scope.row.cpuLogCount
                                    : 0) -
                                  (scope.row.usedCpuSum
                                    ? scope.row.usedCpuSum
                                    : 0)
                                }}
                                {{ $t("unit.cpu") }}
                              </template>
                            </el-table-column>
                            <el-table-column
                              prop="memAllocation"
                              :label="$t('form.availableMemory')"
                            >
                              <template #default="scope">
                                {{
                                  (scope.row.memTotal
                                    ? scope.row.memTotal
                                    : 0) -
                                  (scope.row.usedMemSum
                                    ? scope.row.usedMemSum
                                    : 0)
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
                            <el-table-column
                              prop="manageIp"
                              :label="$t('form.ip')"
                            />
                          </el-table>
                          <span class="align-text-top mr-4 text-gray-500">
                            {{ $t("form.selectedNode") }}：{{
                              currentNodeRow.nodeName || currentNodeRow.name
                            }}
                          </span>
                        </div>
                      </el-form-item>

                      <!-- <el-form-item label="启动源">
                  <el-radio-group :value="0" :size="$store.state.nextStack.viewSize.main">
                    <el-radio-button :label="0" :value="0">镜像</el-radio-button>
                  </el-radio-group>
                </el-form-item> -->
                      <el-form-item
                        :label="$t('form.image')"
                        :prop="'imageId'"
                        label-position="top"
                      >
                        <el-radio-group
                          v-model="nowImageIsPublic"
                          :size="$store.state.nextStack.viewSize.main"
                          @change="imagesIsPublicChange"
                        >
                          <el-radio-button :label="true" :value="true">{{
                            $t("form.baseImage")
                          }}</el-radio-button>
                          <el-radio-button :label="false" :value="false">{{
                            $t("form.privateImage")
                          }}</el-radio-button>
                        </el-radio-group>
                        <div class="relative mt-6">
                          <span
                            class="absolute top-1/2 left-3 z-50 transform -translate-y-1/2"
                          >
                            <i
                              v-if="nowImageType == 1"
                              class="iconfont icon-windows-fill inline-block"
                              style="
                                color: rgba(0, 120, 212, 1);
                                font-size: 16px;
                              "
                            />
                            <i
                              v-if="nowImageType == 2"
                              class="iconfont icon-ubuntu_ inline-block"
                              style="
                                color: rgba(0, 120, 212, 1);
                                font-size: 16px;
                              "
                            />
                            <i
                              v-if="nowImageType == 3"
                              class="iconfont icon-centos-stream inline-block"
                              style="
                                color: rgba(0, 120, 212, 1);
                                font-size: 16px;
                              "
                            />
                          </span>
                          <el-select
                            v-model="nowImageType"
                            class="w-36 imageTypeSelectStyle"
                            style="width: 500px"
                            :placeholder="$t('form.pleaseSelectType')"
                            @change="imagesTypeChange"
                          >
                            <el-option
                              v-for="item in imagesTypeList"
                              :key="item.value"
                              :label="item.label"
                              :value="item.value"
                            >
                              <i
                                v-if="item.value == 1"
                                class="iconfont icon-windows-fill inline-block"
                                style="
                                  color: rgba(0, 120, 212, 1);
                                  font-size: 16px;
                                "
                              />
                              <i
                                v-if="item.value == 2"
                                class="iconfont icon-ubuntu_ inline-block"
                                style="
                                  color: rgba(0, 120, 212, 1);
                                  font-size: 16px;
                                "
                              />
                              <i
                                v-if="item.value == 3"
                                class="iconfont icon-centos-stream inline-block"
                                style="
                                  color: rgba(0, 120, 212, 1);
                                  font-size: 16px;
                                "
                              />
                              {{ item.label }}
                            </el-option>
                          </el-select>
                          <el-select
                            v-if="nowImageIsPublic && nowImageType == 2"
                            v-model="form.imageId"
                            class="ml-2"
                            style="width: 300px"
                            :placeholder="$t('form.pleaseSelectImage')"
                            @change="changeimage"
                          >
                            <el-option
                              v-for="item in newimagesList"
                              :key="item"
                              :label="'ubuntu-' + item"
                              :value="item"
                            />
                          </el-select>
                          <el-select
                            v-if="nowImageIsPublic && nowImageType == 1"
                            v-model="form.imageId"
                            class="ml-2"
                            style="width: 300px"
                            :placeholder="$t('form.pleaseSelectImage')"
                            @change="changeimage"
                          >
                            <el-option
                              v-for="item in newimagesList"
                              :key="item"
                              :label="'windowsserver-' + item"
                              :value="item"
                            />
                          </el-select>
                          <el-select
                            v-if="nowImageIsPublic && nowImageType == 3"
                            v-model="form.imageId"
                            class="ml-2"
                            style="width: 300px"
                            :placeholder="$t('form.pleaseSelectImage')"
                            @change="changeimage"
                          >
                            <el-option
                              v-for="item in newimagesList"
                              :key="item"
                              :label="'centos-' + item"
                              :value="item"
                            />
                          </el-select>
                          <el-select
                            v-if="!nowImageIsPublic"
                            v-model="form.imageId"
                            class="ml-2"
                            style="width: 300px"
                            :placeholder="$t('form.pleaseSelectImage')"
                            @change="changePrivateimage"
                          >
                            <el-option
                              v-for="item in privateImagesList[
                                this.nowImageType
                              ]"
                              :key="item.imageId"
                              :label="item.imageName"
                              :value="item.imageId"
                            />
                          </el-select>
                        </div>
                      </el-form-item>
                      <div
                        v-if="
                          isshowinstall &&
                          flavorsListType != 'info' &&
                          nowImageIsPublic
                        "
                      >
                        <el-checkbox v-model="isshowdetails">{{
                          $t("form.installGpuDriver")
                        }}</el-checkbox>
                        <div
                          class="ml-6 mt-1"
                          style="font-size: 12px; color: rgb(242, 164, 76)"
                        >
                          {{ $t("form.installGpuDriverTips") }}
                        </div>
                      </div>
                      <el-form-item
                        v-if="isshowdetails && flavorsListType != 'info'"
                        label=""
                        prop="selectinstall"
                      >
                        <el-cascader
                          v-model="form.selectinstall"
                          :placeholder="$t('form.pleaseSelectGpuDriver')"
                          class="mt-2"
                          style="width: 500px"
                          :options="options"
                          @change="handleChange"
                        />
                      </el-form-item>
                      <el-form-item
                        :label="$t('form.systemDisk')"
                        prop="rootDisk"
                        class="mt-4"
                      >
                        <el-input-number
                          v-model="form.rootDisk"
                          :min="60"
                          :max="5000"
                          :step="10"
                          :precision="0"
                          class="w-36"
                        />
                        <span class="pl-2">GB</span>
                      </el-form-item>
                      <el-form-item :label="$t('form.cloudDisk')">
                        <el-radio-group
                          v-model="isNeedvolume"
                          :size="$store.state.nextStack.viewSize.main"
                          @change="isNeedvolumeChange"
                        >
                          <el-radio-button :label="false" :value="false">{{
                            $t("form.noMount")
                          }}</el-radio-button>
                          <el-radio-button :label="true" :value="true">{{
                            $t("form.mountCloudDisk")
                          }}</el-radio-button>
                        </el-radio-group>
                        <div v-if="isNeedvolume" class="pl-8 pt-4">
                          <el-form-item
                            :label="$t('form.cloudDiskType')"
                            prop="storagePoolId"
                          >
                            <!-- <el-select v-model="form.storagePoolId" class="ml-0 w-60" @change="changeStoragePool"
                          placeholder="请选择云盘类型">
                          <el-option v-for="(item, index) in storagePoolsList" :key="index" :label="item.name"
                            :value="item.poolId" />
                        </el-select> -->

                            <ul
                              v-if="
                                storagePoolsList && storagePoolsList.length > 0
                              "
                              class="overflow-hidden border pt-2 px-2"
                              style="max-width: 457px"
                            >
                              <li
                                v-for="(item, index) in storagePoolsList"
                                :key="index"
                                class="mb-2"
                                @click="changeStoragePool(item.poolId)"
                              >
                                <div
                                  class="border rounded leading-6 cursor-pointer overflow-hidden"
                                  style="padding: 5px 18px; margin: 0 5px"
                                  :class="
                                    form.storagePoolId == item.poolId
                                      ? '  bg-blue-100 font-bold text-blue-500 border-blue-500'
                                      : ''
                                  "
                                >
                                  <el-radio
                                    v-model="form.storagePoolId"
                                    class="float-left mt-1"
                                    :label="item.poolId"
                                    >{{ "" }}</el-radio
                                  >

                                  <span>{{ item.name }}</span>
                                </div>
                              </li>
                            </ul>
                          </el-form-item>
                          <div
                            v-for="(item, index) in form.diskInfos"
                            :key="index"
                          >
                            <el-form-item
                              :prop="'diskInfos'"
                              :required="index == 0 ? true : false"
                              class="mb-2"
                              style="max-width: 457px"
                              :label="index == 0 ? $t('form.dataDisk') : ''"
                            >
                              <div class="bg-slate-100 px-2 py-2 flex group">
                                <!-- <el-form-item label="" class="float-left mr-2 mb-0">
                              <el-select v-model="form.storagePoolId" class="ml-0 w-60" placeholder="请选择云盘类型"
                                :disabled="true">
                                <el-option v-for="(item, index) in storagePoolsList" :key="index" :label="item.name"
                                  :value="item.poolId" />
                              </el-select>
                            </el-form-item> -->
                                <el-form-item
                                  label=""
                                  class="float-left ml-2 mr-4 mb-0"
                                >
                                  {{ index + 1 }}.
                                </el-form-item>
                                <el-form-item
                                  v-if="!item.diskStatus"
                                  label=""
                                  :prop="'diskInfos.' + index + '.volumeId'"
                                  class="float-left mb-0"
                                  :rules="{
                                    required: true,
                                    message: $t(
                                      'form.pleaseSelectMountCloudDisk'
                                    ),
                                    trigger: 'change',
                                  }"
                                >
                                  <el-select
                                    v-model="item.volumeId"
                                    class="w-60"
                                    :placeholder="
                                      $t('form.pleaseSelectCloudDisk')
                                    "
                                  >
                                    <el-option
                                      v-for="(item, index) in volumesList"
                                      :key="index"
                                      :disabled="
                                        getCheckedVolumes(item.volumeId)
                                      "
                                      :label="
                                        item.name + ' (' + item.size + 'GB)'
                                      "
                                      :value="item.volumeId"
                                    />
                                  </el-select>
                                </el-form-item>
                                <el-form-item
                                  v-if="item.diskStatus"
                                  label=""
                                  :prop="'diskInfos.' + index + '.size'"
                                  class="float-left mb-0"
                                  :rules="{
                                    required: true,
                                    message: $t('form.pleaseInputDataDiskSize'),
                                    trigger: 'blur',
                                  }"
                                >
                                  <el-input-number
                                    v-model="item.size"
                                    :min="10"
                                    :max="5000"
                                    :step="10"
                                    :precision="0"
                                    controls-position="right"
                                    class="w-60"
                                  />
                                  <span class="pl-2">{{ $t("unit.mem") }}</span>
                                </el-form-item>
                                <span
                                  class="absolute top-0 right-0 w-6 h-6 hidden group-hover:block cursor-pointer"
                                  @click="removeDiskData(form.diskInfos, index)"
                                >
                                  <i
                                    style="font-size: 16px"
                                    class="iconfont icon-close"
                                  />
                                </span>
                              </div>
                            </el-form-item>
                          </div>
                          <el-form-item>
                            <div class="overflow-hidden">
                              <div
                                v-if="addVmCount == 1"
                                v-show="form.storagePoolId"
                                class="border border-gray-300 border-dashed px-2 py-1 w-48 cursor-pointer mr-2 float-left"
                                @click="addDisk(false)"
                              >
                                <span class="rounded bg-gray-200 p-1 mr-2">
                                  <i class="el-icon-plus text-base" />
                                </span>
                                <span class="text-xs text-gray-500">{{
                                  $t("form.mountExistingCloudDisk")
                                }}</span>
                              </div>
                              <div
                                v-show="form.storagePoolId"
                                class="border border-gray-300 border-dashed px-2 py-1 w-48 cursor-pointer float-left"
                                @click="addDisk(true)"
                              >
                                <span class="rounded bg-gray-200 p-1 mr-2">
                                  <i class="el-icon-plus text-base" />
                                </span>
                                <span class="text-xs text-gray-500">{{
                                  $t("form.createCloudDisk")
                                }}</span>
                              </div>
                            </div>
                          </el-form-item>
                        </div>
                      </el-form-item>
                    </div>
                  </div>
                  <div v-if="vmAddStep == 1">
                    <div class="text item">
                      <div
                        v-for="(item, index) in form.networkInfos"
                        :key="index"
                        class="overflow-hidden"
                      >
                        <el-form-item
                          :label="$t('form.network')"
                          :prop="'networkInfos.' + index + '.subnetId'"
                          required
                          class="float-left"
                          :class="kind == '0' || kind == '1' ? 'w-120' : ''"
                          :rules="{
                            required: true,
                            message: $t('form.pleaseSelectSubnet'),
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
                                v-show="
                                  form.networkInfos.length > 1 && index > 0
                                "
                                class="overflow-hidden h-6 float-right"
                              >
                                <el-button
                                  size="small"
                                  circle
                                  class="align-top p-0 w-4 h-4 flex-center"
                                  type="danger"
                                  @click="
                                    removeNetworkData(form.networkInfos, index)
                                  "
                                >
                                  <i
                                    class="iconfont icon-jianshao- iconClose"
                                  />
                                </el-button>
                              </div>
                            </div>
                          </template>
                          <div
                            :class="
                              kind == '0' || kind == '1'
                                ? 'mb-2'
                                : 'float-left mr-8'
                            "
                          >
                            <el-select
                              v-model="item.vpcId"
                              class="mr-2"
                              style="width: 250px"
                              :placeholder="$t('form.pleaseSelectVPCNetwork')"
                              @change="vpcIdChange(form.networkInfos, index)"
                            >
                              <el-option
                                v-for="(vpcItem, index) in vpcList"
                                :key="index"
                                :label="
                                  vpcItem.name + ' (' + vpcItem.cidr + ')'
                                "
                                :value="vpcItem.vpcId"
                              />
                            </el-select>
                            <el-button
                              icon="el-icon-refresh"
                              @click="getVpcList"
                            />
                            <p class="block mt-1 text-xs text-gray-500">
                              {{ $t("form.pleaseCreateNewVPCNetwork") }}
                              <el-link
                                class="text-xs contents"
                                :underline="false"
                                style="color: rgb(64, 158, 255)"
                                @click="createanewjump('nextStack-vpc')"
                                >{{ $t("form.createNewVPCNetwork") }}</el-link
                              >
                            </p>
                          </div>
                          <div
                            :class="
                              kind == '0' || kind == '1' ? '' : 'float-left'
                            "
                          >
                            <el-select
                              v-model="item.subnetId"
                              :disabled="!item.vpcId"
                              class="mr-2"
                              style="width: 250px"
                              :placeholder="$t('form.pleaseSelectSubnet')"
                              @change="staticChange(form.networkInfos, index)"
                            >
                              <template
                                v-for="(subnetItem, index) in subnetsDataList"
                              >
                                <el-option
                                  v-if="item.vpcId == subnetItem.vpcId"
                                  :key="index"
                                  :label="getSubnetNameAndCidr(subnetItem)"
                                  :value="subnetItem.subnetId"
                                />
                              </template>
                            </el-select>
                            <el-button
                              icon="el-icon-refresh"
                              @click="
                                refreshSubNetList(form.networkInfos, index)
                              "
                            />
                            <p
                              v-if="item.vpcId"
                              class="block mt-1 text-xs text-gray-500"
                            >
                              {{ $t("form.pleaseCreateNewPrivateNetwork") }}
                              <el-link
                                class="text-xs contents"
                                :underline="false"
                                style="color: rgb(64, 158, 255)"
                                @click="
                                  createanewjump(
                                    'nextStack-vpc-desc',
                                    item.vpcId
                                  )
                                "
                              >
                                {{ $t("form.createNewSubnet") }}
                              </el-link>
                            </p>
                          </div>
                        </el-form-item>
                        <div
                          v-if="kind == '0' || kind == '1'"
                          class="float-left mt-8 ml-10"
                        >
                          <el-form-item
                            :label="$t('form.ipAllocation')"
                            required
                            class="formLabelPositionStyle"
                          >
                            <el-radio-group
                              v-model="item.staticStatus"
                              :disabled="addVmCount != 1"
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
                            :label="$t('form.fixedIP')"
                            required
                            class="formLabelPositionStyle mt-8"
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
                                <span
                                  v-if="
                                    subnetsDataList.length > 0 && item.subnetId
                                  "
                                >
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
                      <!-- <el-form-item class="-mt-2">
                        <el-button size="small" class="" type="text" @click="addData()">
                          <i class="el-icon-plus"></i>添加网络
                        </el-button>
                      </el-form-item> -->
                      <el-form-item
                        label=""
                        :prop="'eipId'"
                        :rules="{
                          required: eipData.eipType == 2,
                          message: $t('form.pleaseSelectEip'),
                          trigger: 'change',
                        }"
                      >
                        <template #label>
                          <span class="inline-block">{{
                            $t("form.elasticPublicIPEIP")
                          }}</span>
                        </template>
                        <div class="mb-2">
                          <el-radio-group
                            v-model="eipData.eipType"
                            :size="$store.state.nextStack.viewSize.main"
                          >
                            <el-radio-button :label="0" :value="0">
                              {{ $t("form.doNotBind") }}
                            </el-radio-button>
                            <el-radio-button
                              :label="1"
                              :value="1"
                              :disabled="!nowEipPoolId"
                              >{{ $t("form.bindNewEip") }}</el-radio-button
                            >
                            <el-radio-button
                              :label="2"
                              :value="2"
                              :disabled="
                                eipsDataList.length == 0 || addVmCount > 1
                              "
                              >{{ $t("form.bindExistingEip") }}</el-radio-button
                            >
                          </el-radio-group>
                        </div>
                        <div>
                          <el-select
                            v-if="eipData.eipType == 2"
                            v-model="form.eipId"
                            class="mr-2"
                            style="width: 250px"
                            :placeholder="$t('form.pleaseSelectEip')"
                          >
                            <template>
                              <el-option
                                v-for="(item, index) in eipsDataList"
                                :key="index"
                                :label="getEipLabelName(item)"
                                :value="item.eipId"
                              />
                            </template>
                          </el-select>
                        </div>
                      </el-form-item>
                      <el-form-item
                        :label="$t('form.securityGroup')"
                        prop="sgIds"
                      >
                        <el-select
                          v-model="form.sgIds"
                          multiple
                          style="width: 250px"
                          class="mr-2"
                          :placeholder="$t('form.pleaseSelectSecurityGroup')"
                          @change="changesecgroup"
                        >
                          <el-option
                            v-for="item in secGroupsList"
                            :key="item.sgId"
                            :label="item.name"
                            :value="item.sgId"
                          />
                        </el-select>
                        <el-button
                          icon="el-icon-refresh"
                          @click="getsecGroupsList"
                        />
                        <p
                          v-if="!defaultgroups"
                          class="block mt-1 text-xs text-gray-500"
                        >
                          {{ $t("form.ifYouHaveOtherRequirements") }}
                          <el-link
                            class="text-xs contents"
                            :underline="false"
                            style="color: rgb(64, 158, 255)"
                            @click="createanewjump('nextStack-secGroups')"
                            >{{ $t("form.createNewSecurityGroup") }}</el-link
                          >
                        </p>
                        <p
                          v-if="defaultgroups"
                          class="block mt-1 text-xs text-gray-500"
                        >
                          {{ $t("form.initialStateSecurityGroupTips") }}
                          <el-link
                            class="text-xs contents"
                            :underline="false"
                            style="color: rgb(64, 158, 255)"
                            @click="createanewjump('nextStack-secGroups')"
                            >{{ $t("form.createNewSecurityGroup") }}</el-link
                          >
                        </p>
                      </el-form-item>
                      <el-form-item
                        v-if="
                          currentFlavorRow.gpuName &&
                          currentFlavorRow.gpuName.includes('H800')
                        "
                        class="mt-2"
                      >
                        <el-alert
                          :title="$t('form.specialSecurityGroupTips')"
                          type="warning"
                          show-icon
                          :closable="false"
                        />
                      </el-form-item>
                    </div>
                  </div>
                  <div v-if="vmAddStep == 2">
                    <el-form-item :label="$t('form.instanceName')" prop="name">
                      <el-input
                        v-model="form.name"
                        class="w-96"
                        :placeholder="$t('form.pleaseInputInstanceName')"
                      />
                    </el-form-item>
                    <el-form-item
                      :label="$t('form.description')"
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
                    <el-form-item :label="$t('form.loginMethod')" required>
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
                    <el-form-item prop="sysUsername" class="item__error">
                      <div style="display: flex">
                        <div style="width: 70px; font-size: 12px">
                          {{ $t("form.sysUsername") }}:
                        </div>
                        <el-input
                          v-model="form.sysUsername"
                          style="width: 315px"
                          :disabled="nowImageType == 1"
                          :placeholder="$t('form.pleaseInputSysUsername')"
                        />
                      </div>
                    </el-form-item>

                    <el-form-item
                      v-if="loginType"
                      prop="sysPassword"
                      class="item__error"
                    >
                      <div style="display: flex">
                        <div style="width: 70px; font-size: 12px">
                          {{ $t("form.loginPassword") }}:
                        </div>
                        <el-input
                          v-model="form.sysPassword"
                          style="width: 315px"
                          type="password"
                          :placeholder="$t('form.pleaseInputLoginPassword')"
                          show-password
                        />
                      </div>
                    </el-form-item>
                    <el-form-item
                      v-if="!loginType"
                      prop="pubkeyId"
                      class="item__error"
                    >
                      <div style="display: flex">
                        <div style="width: 70px; font-size: 12px">
                          {{ $t("form.keyPair") }}:
                        </div>
                        <el-select
                          v-model="form.pubkeyId"
                          style="width: 260px"
                          :placeholder="$t('form.pleaseSelectKeyPair')"
                          class="mr-3"
                        >
                          <el-option
                            v-for="(item, index) in pubkeysDataList"
                            :key="index"
                            :label="item.name"
                            :value="item.pubkeyId"
                            class="custom-option"
                          >
                            <div class="option-content">
                              <span>{{ item.name }}</span>
                              <span class="value">ID:{{ item.pubkeyId }}</span>
                            </div>
                          </el-option>
                        </el-select>
                        <el-button
                          icon="el-icon-refresh"
                          @click="getpubkeysList"
                        />
                      </div>
                      <p
                        class="block mt-1 text-xs text-gray-500"
                        style="margin-left: 70px"
                      >
                        {{ $t("form.needCreateNewKeyPair") }}
                        <el-link
                          class="text-xs contents"
                          :underline="false"
                          style="color: rgb(64, 158, 255)"
                          @click="createanewjump('nextStack-publicKey')"
                          >{{ $t("form.createNewKeyPair") }}</el-link
                        >
                      </p>
                    </el-form-item>
                    <el-form-item label="">
                      <el-checkbox v-model="isAdvanced">
                        {{ $t("titleAndText.advancedConfig") }}
                        <small>(hostname)</small>
                      </el-checkbox>
                    </el-form-item>
                    <el-form-item
                      v-if="isAdvanced"
                      label="Hostname"
                      prop="hostname"
                      class="item__error"
                    >
                      <el-input
                        v-model="form.hostname"
                        class="w-96"
                        :placeholder="$t('form.pleaseInputHostname')"
                      />
                      <span class="block mt-1 text-xs text-gray-500">{{
                        $t("form.hostnameTips")
                      }}</span>
                    </el-form-item>
                  </div>
                </el-form>
              </div>
            </el-col>
          </el-row>
        </el-col>
        <el-col
          :span="6"
          class="p-0"
          style="position: absolute; top: 0; right: 0; height: calc(100% - 2px)"
        >
          <div class="bg-slate-100 h-full p-6">
            <h3 class="text-md mt-2 mb-4">{{ $t("titleAndText.preview") }}</h3>
            <el-form
              :size="$store.state.nextStack.viewSize.main"
              label-width="100px"
              label-position="left"
              class="vmAddPreviewForm"
            >
              <el-form-item class="mb-0" :label="$t('form.region') + ':'">
                <div class="leading-snug pt-1.5">
                  <span v-if="nowCloudId">{{
                    cloudListData.filter((e) => {
                      return nowCloudId == e.cloud_id;
                    })[0].name
                  }}</span>
                </div>
              </el-form-item>
              <el-form-item class="mb-0" :label="$t('form.image') + ':'">
                <div class="leading-snug pt-1.5">
                  <span v-if="lastimagename && nowImageIsPublic">
                    {{ lastimagename }}
                  </span>

                  <span
                    v-if="!nowImageIsPublic"
                    class="leading-snug inline-block"
                  >
                    {{ getPrivateImageName(form.imageId) }}
                  </span>
                </div>
              </el-form-item>
              <el-form-item class="mb-0" :label="$t('form.flavor') + ':'">
                <div class="leading-snug pt-1.5">
                  <span v-if="currentFlavorRow">
                    <span v-if="currentFlavorRow.gpuCount">{{
                      $t("form.gpuCompute")
                    }}</span>
                    <span v-else>{{ $t("form.generalCompute") }}</span> |
                  </span>
                  <span v-if="currentFlavorRow && currentFlavorRow.cpu"
                    >{{ currentFlavorRow.cpu }} {{ $t("unit.cpu") }} |
                  </span>
                  <span v-if="currentFlavorRow && currentFlavorRow.mem"
                    >{{ currentFlavorRow.mem }} {{ $t("unit.mem") }} |
                  </span>

                  <span v-if="currentFlavorRow && currentFlavorRow.gpuName">
                    {{ currentFlavorRow.gpuName }}*{{
                      currentFlavorRow.gpuCount
                    }}
                  </span>
                  <span
                    v-if="currentFlavorRow && currentFlavorRow.gpuName"
                    class="inline-block"
                    style="font-size: 12px; color: rgb(242, 164, 76)"
                  >
                    {{ $t("form.multiCardInstanceTips") }}
                  </span>
                </div>
              </el-form-item>
              <el-form-item class="mb-0" :label="$t('form.systemDisk') + ':'">
                <div class="leading-snug pt-1.5">
                  <span>{{ form.rootDisk }}GB</span>
                </div>
              </el-form-item>
              <el-form-item
                v-for="(item, index) in form.diskInfos"
                :key="index"
                class="mb-0"
                :label="$t('form.dataDisk') + ':'"
              >
                <div class="leading-snug pt-1.5">
                  <span v-if="item.diskStatus">
                    {{ $t("form.newAdd") }} {{ item.size }}GB
                  </span>
                  <span v-if="!item.diskStatus && item.volumeId">
                    {{ item.size }}GB ({{
                      volumesList.filter((e) => {
                        return item.volumeId == e.volumeId;
                      })[0].name
                    }})
                  </span>
                </div>
              </el-form-item>
              <div
                v-if="
                  form.networkInfos &&
                  form.networkInfos.length > 0 &&
                  form.networkInfos[0].vpcId
                "
              >
                <el-form-item
                  v-for="(item, index) in form.networkInfos"
                  :key="index"
                  class="mb-0"
                  :label="$t('form.network') + ':'"
                >
                  <div class="leading-snug pt-1.5">
                    <span v-if="item.vpcId">
                      {{
                        vpcList.filter((e) => {
                          return item.vpcId == e.vpcId;
                        })[0].name
                      }}
                      <span v-if="item.subnetId">
                        |
                        {{
                          subnetsDataList.filter((e) => {
                            return item.subnetId == e.subnetId;
                          })[0].name
                        }}
                      </span>
                      <span v-if="item.staticStatus === 0">
                        {{ item.ip1 }}.{{ item.ip2 }}.{{ item.ip3 }}.{{
                          item.ip4
                        }}
                      </span>
                      <span v-if="item.staticStatus === 1">
                        {{ $t("form.dynamicAllocation") }}
                      </span>
                    </span>
                  </div>
                </el-form-item>
              </div>
              <el-form-item
                class="mb-0"
                :label="$t('form.securityGroup') + ':'"
              >
                <div class="leading-snug pt-1.5">
                  <span
                    v-if="
                      form.sgIds && secGroupsList && secGroupsList.length > 0
                    "
                  >
                    <div
                      v-for="(name, index) in filteredSecGroupNames"
                      :key="index"
                    >
                      {{ name }}
                    </div>
                  </span>
                </div>
              </el-form-item>
              <el-divider />

              <el-form-item class="mb-3" :label="$t('form.quantity')">
                <el-input-number
                  v-model="addVmCount"
                  class="w-24 mr-6"
                  :min="1"
                  :max="10"
                  :step="1"
                  :size="$store.state.nextStack.viewSize.main"
                  controls-position="right"
                  @change="addVmCountChange"
                />
              </el-form-item>
            </el-form>
          </div>
        </el-col>
      </el-row>
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
      <div class="text-right">
        <el-button
          v-if="vmAddStep != 0"
          size="small"
          type="primary"
          @click="backStep(vmAddStep)"
          >{{ $t("button.previousStep") }}</el-button
        >
        <el-button
          v-if="vmAddStep == 2"
          size="small"
          type="primary"
          :loading="loading"
          @click="toVmAdd()"
          >{{ $t("button.createImmediately") }}</el-button
        >
        <el-button
          v-if="vmAddStep != 2"
          size="small"
          type="primary"
          @click="nextStep(vmAddStep)"
          >{{ $t("button.nextStep") }}
          {{
            vmAddStep == 0
              ? $t("titleAndText.networkConfig")
              : vmAddStep == 1
              ? $t("titleAndText.systemConfig")
              : ""
          }}</el-button
        >
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import mainApi from "@/api/nextStack/mainApi";
import { getCloudysInfo } from "@/api/mainApi";
import filtersFun from "@/utils/statusFun";
import netmask from "netmask";
var Netmask = netmask.Netmask;

export default {
  data() {
    const validatePass = (rule, value, callback) => {
      if (this.form.sysPassword === "") {
        callback(new Error(this.$t("message.pleaseInputPassword")));
      } else {
        // 必须包含数字 字母 符号三种组合

        const reg =
          /(?=.*[a-zA-Z])(?=.*\d)(?=.*[~!@#$%^&*()_+\-=}{:";<>?,./]).{6,18}$/;
        if (!reg.test(this.form.sysPassword)) {
          callback(new Error(this.$t("message.pleaseInputPasswordRule")));
        } else {
          callback();
        }
      }
    };
    // 主机名的校验
    const validatehostname = (rule, value, callback) => {
      if (!value) {
        callback();
        return;
      }
      if (value.length > 63 || value.length < 2) {
        callback(new Error(this.$t("message.hostnameLength")));
        return;
      }
      if (value.startsWith("-") || value.endsWith("-")) {
        callback(new Error(this.$t("message.hostnameStartWithHyphen")));
        return;
      }
      if (!/^[a-zA-Z0-9-]+$/.test(value)) {
        callback(new Error(this.$t("message.hostnameOnlyAlphanumericHyphen")));
        return;
      }
      callback(); // 校验通过
    };
    const nowcloudIdrule = (rule, value, callback) => {
      if (this.nowCloudId) {
        callback();
      } else {
        callback(new Error(this.$t("message.pleaseSelectRegion")));
      }
    };
    return {
      eipPoolTableData: [], // EIP池列表
      nowEipPoolId: "", // 当前EIP池ID
      eipsDataList: [], // EIP列表
      eipData: {
        eipType: 0,
        eipId: "",
      },
      nowCloudId: "",
      nowCloudInfo: {},
      cloudListData: [],
      vmAddStep: 0,
      filtersFun: filtersFun,
      validatePass: validatePass,
      loading: false,
      currentFlavorRow: "", // 规格选择 当前行
      nowImageTypeOs: "", // 当前镜像类型
      loginType: true,
      nodeIdAuto: 0,
      vmAddType: 0,

      imagesTypeList: [
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
          label: "Windows",
          type: "Windows",
          typeValue: 1,
          vendor: 0,
          value: 1,
        },
      ],

      options: [],
      isAdvanced: false,
      addVmCount: 1,
      isNeedvolume: false, // 是否需要云硬盘
      form: {
        // 表单
        selectinstall: "",
        name: "",
        description: "",
        flavorId: "", // 规格ID
        nodeId: "", // 计算节点ID
        imageOsType: 0, // 镜像类型
        imageId: "", // 镜像ID
        rootDisk: 60, // 系统盘
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
        eipId: "", // EIPID
        storagePoolId: "", // 云盘类型ID
        diskInfos: [], // 云硬盘
      },
      volumesList: [], // 云硬盘列表
      storagePoolsList: [], // 云盘类型列表
      CpuList: [], // CPU列表
      MemList: [], // 内存列表
      DiskList: [], // 磁盘列表

      vpcList: [], // VPC网络列表
      imagesList: [], // 镜像列表
      subnetsDataList: [], // 子网列表
      pubkeysDataList: [], // 密钥对列表
      flavorsList: [], // 规格列表
      flavorsListData: [], // 规格列表
      flavorsListType: "gpu", // 规格列表
      vmsHypervisorNodesList: [], // 计算节点列表
      secGroupsList: [], // 安全组列表
      rules: {
        nowcloudId: [
          {
            required: true,
            validator: nowcloudIdrule,
            trigger: "change",
          },
        ],
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
            message: this.$t("message.pleaseSelectFlavor"),
            trigger: "change",
          },
        ],
        nodeId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectComputeNode"),
            trigger: "change",
          },
        ],
        imageOsType: [
          {
            required: true,
            message: this.$t("message.pleaseSelectOperatingSystem"),
            trigger: "change",
          },
        ],
        imageId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectOperatingSystem"),
            trigger: "change",
          },
        ],
        rootDisk: [
          {
            required: true,
            message: this.$t("message.pleaseFillSystemDisk"),
            trigger: "change",
          },
        ],
        eipId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectEIP"),
            trigger: "change",
          },
        ],
        hostname: [
          { required: false, validator: validatehostname, trigger: "blur" },
        ],
        sysUsername: [
          {
            required: true,
            message: this.$t("message.pleaseInputLoginName"),
            trigger: "blur",
          },
          {
            pattern: /^(?!root$).+$/,
            message: this.$t("message.loginNameCannotBeRoot"),
            trigger: "blur",
          },
          {
            pattern: /^(?!root$)[a-zA-Z0-9][a-zA-Z0-9-]{0,29}[a-zA-Z0-9]$/,
            message: this.$t("message.loginNameFormat"),
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
            message: this.$t("message.pleaseSelectSecurityGroup"),
            trigger: "change",
          },
        ],
        storagePoolId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectDiskType"),
            trigger: "change",
          },
        ],
        selectinstall: [
          {
            required: true,
            message: this.$t("message.pleaseSelectGpuDriver"),
            trigger: "change",
          },
        ],
        pubkeyId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectKeyPair"),
            trigger: "change",
          },
        ],
      },
      privateImagesList: {}, // 私有镜像列表
      nowImageType: 2, // 镜像类型
      nowImageIsPublic: true, // 镜像是否公共
      currentNodeRow: "", // 计算节点选择 当前行
      vmsHypervisorNodesLoading: false,
      nodeAddVmForm: {
        name: "", // 名称
        nodeId: "", // 计算节点ID
        manageIp: "", // 管理IP
      },
      addType: "vm",
      drawerData: {},
      cachingoperatingsystem: {}, // 用于操作系统的缓存
      // 是否是选择的默认安全组
      defaultgroups: false,
      // 用于存储默认的安全组id是什么
      defaultgroupssgId: "",
      isshowdetails: false,
      newimagesList: [],
      isshowinstall: false,
      selectimagelist: [],
      selectversion: "",
      // 最后的镜像id
      lastimageid: "",
      lastimagename: "",
      copy_params: {},
    };
  },
  watch: {
    loginType: {
      handler(val) {
        if (val) {
          this.form.pubkeyId = "";
        }
      },
    },
    vmAddType: {
      handler(val) {
        this.currentNodeRow = "";
        this.form.nodeId = "";
      },
    },
    isshowdetails: {
      handler(val) {
        if (!val) {
          this.form.selectinstall = [];
        }
      },
    },
    flavorsListType: {
      handler(val) {
        // 切换规格类型镜像重置
        this.imagesTypeChange(2);
        this.currentFlavorRow = "";
        this.form.flavorId = "";
        // this.copy_params.flavorId = "";
        this.filterFlavor();
      },
    },

    "form.imageOsType": {
      handler(val) {
        this.handleImageOsType();
      },
    },

    "form.selectinstall": {
      handler: function (val, oldVal) {
        if (!this.selectimagelist.length) {
          this.lastimagename = "";
          this.lastimageid = "";
        } else {
          this.selectimagelist.map((res) => {
            if (res.gpuDriverVersion == val[0]) {
              this.lastimagename = res.imageName;
              this.lastimageid = res.imageId;
            }
          });
        }
      },
      deep: true,
    },

    "eipData.eipType": {
      handler: function (val, oldVal) {
        this.form.eipId = "";
        this.eipId = "";
      },
      deep: true,
    },
  },

  created() {
    if (this.$route.params.storagePoolId) {
      this.copy_params = this.$route.params;
      this.form.rootDisk = this.copy_params.rootDisk;
      this.isNeedvolume = this.copy_params.diskInfos.length >= 1;
      this.form.diskInfos = this.copy_params.diskInfos;
      this.form.storagePoolId = this.copy_params.storagePoolId;
      this.changeStoragePool(this.copy_params.storagePoolId);
      this.flavorsListType = this.copy_params.flavorsListType;

      this.nowImageIsPublic = true;
      this.nowImageType = this.copy_params.nowImageType;

      this.form.imageId = this.copy_params.imageId;

      this.loginType = this.copy_params.loginType;
      this.form.name = this.copy_params.name;
      this.form.pubkeyId = this.copy_params.pubkeyId;
      this.form.sysPassword = this.copy_params.sysPassword;
      this.form.sysUsername = this.copy_params.sysUsername;
    }
  },
  mounted() {
    this.getcloudListData(); // 获取区域列表
    this.handleImageOsType(); // 操作系统类型
  },
  computed: {
    ...mapGetters(["kind"]),
    filteredSecGroupNames() {
      return this.secGroupsList
        .filter((group) => this.form.sgIds.includes(group.sgId))
        .map((group) => group.name);
    },
  },

  methods: {
    getEipLabelName(eipItem) {
      if (this.kind == 0 || this.kind == 1) {
        if (eipItem.eeUserName) {
          return (
            (eipItem.publicIp || eipItem.ipAddress) +
            " (" +
            eipItem.eeUserName +
            ")"
          );
        } else {
          return eipItem.publicIp || eipItem.ipAddress;
        }
      } else {
        return eipItem.publicIp || eipItem.ipAddress;
      }
    },
    getSubnetNameAndCidr(subnetItem) {
      return `${subnetItem.name} (${subnetItem.cidr})`;
    },
    getEipPoolList() {
      // EIP列表

      mainApi.eipPoolsList().then((res) => {
        if (res.eipPools && res.eipPools.length > 0) {
          this.eipPoolTableData = res.eipPools.filter((res) => {
            return res.available === true;
          });
          if (this.eipPoolTableData.length > 0) {
            this.nowEipPoolId = this.eipPoolTableData[0].poolId;
          }
        }
        this.geteipsList(); // EIP列表
      });
    },
    changePrivateimage() {
      // 切换私有镜像
    },
    getPrivateImageName(id) {
      // 获取私有镜像名称
      let name = "";

      if (this.privateImagesList[this.nowImageType]) {
        var data = this.privateImagesList[this.nowImageType].filter((res) => {
          return res.imageId == id;
        });
        if (data && data.length > 0) {
          name = data[0].imageName;
        }
      }
      return name;
    },
    // 选择镜像
    changeimage(e) {
      if (e) {
        this.form.selectinstall = [];
        this.isshowdetails = false;
        this.selectimagelist = [];
        this.imagesList.map((res) => {
          if (res.imageOsVersion == e) {
            this.selectimagelist.push(res);
          }
        });
        if (this.selectimagelist.length > 1) {
          this.isshowinstall = true;
          this.options = [];
          if (
            this.selectimagelist.length == 2 ||
            this.selectimagelist.length == 3
          ) {
            this.selectimagelist.map((res) => {
              if (res.gpuDriverVersion == "550") {
                this.options.push({
                  value: "550",
                  label: "GPU Dirver 550.78",
                  children: [
                    {
                      value: "CUDA 12.4",
                      label: "CUDA 12.4",
                      children: [
                        {
                          value: "CUDNN 9.1.1",
                          label: "CUDNN 9.1.1",
                        },
                      ],
                    },
                  ],
                });
              } else if (res.gpuDriverVersion == "535") {
                this.options.push({
                  value: "535",
                  label: "GPU Dirver 535.154.05",
                  children: [
                    {
                      value: "CUDA 12.2",
                      label: "CUDA 12.2",
                      children: [
                        {
                          value: "CUDNN 9.1.1",
                          label: "CUDNN 9.1.1",
                        },
                      ],
                    },
                  ],
                });
              } else if (res.gpuDriverVersion == "551") {
                this.options.push({
                  value: "551",
                  label: "GPU Dirver 551",
                  children: [
                    {
                      value: "CUDA 12.4",
                      label: "CUDA 12.4",
                      children: [
                        {
                          value: "CUDNN 10.2",
                          label: "CUDNN 10.2",
                        },
                      ],
                    },
                  ],
                });
              }
            });
          } else {
            this.options = [
              {
                value: "550",
                label: "GPU Dirver 550.78",
                children: [
                  {
                    value: "CUDA 12.4",
                    label: "CUDA 12.4",
                    children: [
                      {
                        value: "CUDNN 9.1.1",
                        label: "CUDNN 9.1.1",
                      },
                    ],
                  },
                ],
              },
              {
                value: "535",
                label: "GPU Dirver 535.154.05",
                children: [
                  {
                    value: "CUDA 12.2",
                    label: "CUDA 12.2",
                    children: [
                      {
                        value: "CUDNN 9.1.1",
                        label: "CUDNN 9.1.1",
                      },
                    ],
                  },
                ],
              },
              {
                value: "551",
                label: "GPU Dirver 551",
                children: [
                  {
                    value: "CUDA 12.4",
                    label: "CUDA 12.4",
                    children: [
                      {
                        value: "CUDNN 10.2",
                        label: "CUDNN 10.2",
                      },
                    ],
                  },
                ],
              },
            ];
          }
        } else {
          this.isshowinstall = false;
        }
      } else {
        this.selectimagelist = [];
      }
    },
    changeflavorsListType() {
      this.copy_params.flavorId = "";
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.available) {
        return "";
      } else {
        return "warning-row";
      }
    },
    handleImageOsType() {
      this.nowImageTypeOs = this.form.imageOsType;
      if (this.form.imageOsType == 1) {
        this.form.sysUsername = "Administrator";
      } else {
        this.form.sysUsername = "cloud";
      }
    },
    async getcloudListData() {
      const list = await getCloudysInfo({ page_size: 99999, page_num: 1 });
      if (!list.clouds || list.clouds.length == 0) {
        return;
      }
      this.cloudListData = list.clouds.filter((item) => {
        return item.product == "NextStack";
      });
      if (this.$store.state.app.hxcloudData) {
        this.nowCloudId = this.$store.state.app.hxcloudData.cloud_id;
      } else {
        this.nowCloudId = this.cloudListData[0].cloud_id;
        this.nowCloudInputChange(this.nowCloudId);
      }
      this.init();
    },
    nowCloudInputChange(val) {
      // 区域切换
      this.$nextTick(() => {
        this.$refs["addVmForm"].resetFields();
        this.copy_params.flavorId = "";
        this.vmAddType = 0;
        this.flavorsListType = "gpu";
        this.form.sgIds = [];
        this.form.diskInfos = [];
        this.form.eipId = "";
        this.eipData.eipType = 0;
        this.nowCloudInfo = this.cloudListData.filter((item) => {
          return item.cloud_id == val;
        })[0];
        this.nowCloudId = val;

        setTimeout(() => {
          this.$store.commit("app/sethxcloudData", this.nowCloudInfo);
        }, 100);
      });
    },
    init() {
      var nodeId = "";
      nodeId = this.$route.query.nodeId;

      if (nodeId) {
        this.addType = "node";
        this.form.nodeId = nodeId;
        mainApi
          .vmsHypervisorNodesDetail(nodeId)
          .then((res) => {
            this.drawerData = res;
            this.nodeAddVmForm.name = this.drawerData.name;
            this.nodeAddVmForm.nodeId = this.drawerData.nodeId;
            this.nodeAddVmForm.manageIp = this.drawerData.manageIp;
          })
          .catch((error) => {});
      }

      this.getVpcList(); // VPC列表
      if (this.copy_params.nowImageType) {
        this.getImageList(this.copy_params.nowImageType); // 镜像列表
        this.imagesTypeChange(this.copy_params.nowImageType);
      } else {
        this.getImageList(2); // 镜像列表
      }
      this.getSubNetList(); // 子网列表
      this.getpubkeysList(); // 公钥列表
      this.getFlavorList(); // 规格列表
      // this.getvmsHypervisorNodesList(); //计算节点列表
      // this.getVolumesList(); //云盘列表
      this.getStoragePools(); // 云盘类型列表
      this.getsecGroupsList(); // 安全组列表
      this.getEipPoolList(); // 获取EIPPool列表
    },

    isNeedvolumeChange(val) {
      // 是否需要云硬盘
      if (!val) {
        this.form.diskInfos = [];
      }
    },
    imagesIsPublicChange(val) {
      // 镜像是否公共切换
      this.form.imageId = "";
      this.nowImageIsPublic = val;

      this.imagesTypeChange(2);
    },
    imagesTypeChange(type) {
      // 镜像类型切换
      this.form.imageId = "";
      var item = this.imagesTypeList.filter((v) => {
        return v.value == type;
      })[0];
      // 镜像类型切换
      this.form.selectinstall = [];
      this.isshowdetails = false;
      this.form.imageOsType = item.typeValue;
      this.nowImageType = item.value;
      this.imagesList = [];
      if (item.value != 0) {
        this.getImageList(item.vendor);
      }
    },
    staticChange(item, index) {
      // IP分配类型切换
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
        if (
          this.ipToint(ip) > this.ipToint(netBlock.first) &&
          this.ipToint(ip) < this.ipToint(netBlock.last)
        ) {
        } else {
          item[index].ipStatus = true;
        }
      } else {
        item[index].ipStatus = true;
      }
    },
    geteipsList() {
      // 获取数据
      mainApi
        .eipsList({
          oneToOne: true, // 是否一对一
          // vpc_id: this.vpcId,
          page_size: 99999,
          page_num: 1,
        })
        .then((res) => {
          this.eipsDataList = res.eips || [];
        });
    },
    flavorsHandleCurrentChange(val) {
      if (!val.available) {
        return;
      }
      // 切换规格镜像重置
      this.imagesTypeChange(2);
      // 规格选择
      if (val) {
        this.currentFlavorRow = val;
        this.form.flavorId = val.flavorId;
        if ((this.kind == 0 || this.kind == 1) && this.vmAddType == 1) {
          this.getvmsHypervisorNodesList();
        }
      } else {
        this.currentFlavorRow = "";
        this.form.flavorId = "";
        this.vmsHypervisorNodesList = [];
      }
    },
    nodeHandleCurrentChange(val) {
      // 计算节点选择
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
      this.$refs["addVmForm"].resetFields();
    },
    vpcIdChange(item, index) {
      item[index].subnetId = "";
    },
    backStep(val) {
      // 上一步
      this.vmAddStep = val - 1;
    },
    nextStep(val) {
      // 下一步
      if (val == 0 || val == 1) {
        this.$refs["addVmForm"].validate((valid) => {
          if (valid) {
            this.vmAddStep = val + 1;
          }
        });
      }
    },
    toVmAdd() {
      // 实例add
      var data = JSON.parse(JSON.stringify(this.form));
      if (this.nowImageIsPublic) {
        data.imageId = this.lastimageid;
      }
      data.diskInfos.map((v) => {
        delete v.diskStatus;
      });
      var ipStatusData = [];
      data.sgIds = data.sgIds;
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

      if (this.eipData.eipType == 0) {
        delete data.eipId;
      } else if (this.eipData.eipType == 1) {
        delete data.eipId;
        data.applyEip = true;
      }

      this.$refs["addVmForm"].validate((valid) => {
        if (valid) {
          if (this.addVmCount == 1) {
            mainApi
              .vmsInstabcesAdd(data)
              .then((res) => {
                this.loading = false;

                this.resetForm();
                this.$notify({
                  title: this.$t("message.startCreate"),
                  type: "success",
                  duration: 2500,
                });
                this.$router.push({ path: "/nextStack/vm" });
              })
              .catch((error) => {
                // this.resetForm();
                this.loading = false;
              });
          } else {
            mainApi
              .vmsInstabcesAddCount({
                vmInstanceCreateReq: data,
                count: this.addVmCount,
              })
              .then((res) => {
                this.loading = false;
                const switchcloud = this.cloudListData.filter((e) => {
                  return this.nowCloudId == e.cloud_id;
                })[0];
                this.$store.commit("app/sethxcloudData", switchcloud);
                this.resetForm();
                this.$notify({
                  title: this.$t("message.startCreate"),
                  type: "success",
                  duration: 2500,
                });
                this.$router.push({ path: "/nextStack/vm" });
              })
              .catch((error) => {
                // this.resetForm();
                this.loading = false;
              });
          }
        } else {
          this.loading = false;
        }
      });
    },
    getVpcList() {
      // VPC列表
      mainApi
        .vpcList({ vpc_phase: 1, page_num: 1, page_size: 99999 })
        .then((res) => {
          if (res.vpcs && res.vpcs.length > 0) {
            this.vpcList = res.vpcs.filter((v) => {
              return v.phaseStatus == 1;
            });
          } else {
            this.vpcList = [];
          }
        })
        .catch((error) => {});
    },
    getImageList(vendor) {
      if (this.nowImageIsPublic && this.cachingoperatingsystem[vendor]) {
        this.imagesList = this.cachingoperatingsystem[vendor];

        if (this.imagesList.length > 0) {
          const array = [];
          this.imagesList.map((res) => {
            array.push(res.imageOsVersion);
          });
          this.newimagesList = [...new Set(array)];
          this.form.imageId = this.newimagesList[0];
          this.changeimage(this.imagesList[0].imageOsVersion);
          // this.$refs["addVmForm"].validateField("imageId");
        } else {
          this.imagesList = [];
          this.newimagesList = [];
          this.lastimagename = "";
          this.lastimageid = "";
          this.changeimage();
        }
      } else if (!this.nowImageIsPublic && this.privateImagesList[vendor]) {
        this.form.imageId = this.privateImagesList[vendor][0].imageId;
      } else {
        // 镜像列表
        mainApi
          .imageList({
            is_vm: true,
            image_os_vendor: vendor,
            is_ok: true,
            is_public: this.nowImageIsPublic,
            page_num: 1,
            page_size: 99999,
          })
          .then((res) => {
            if (!this.nowImageIsPublic) {
              this.privateImagesList[vendor] = res.images ? res.images : [];
              if (this.privateImagesList[vendor]) {
                if (this.privateImagesList[vendor].length > 0) {
                  this.form.imageId = this.privateImagesList[vendor][0].imageId;
                  // this.$refs["addVmForm"].validateField("imageId");
                }
              }
            } else {
              this.cachingoperatingsystem[vendor] = res.images
                ? res.images
                : [];
              this.imagesList = this.cachingoperatingsystem[vendor];
              if (this.imagesList.length > 0) {
                this.$nextTick(() => {
                  const array = [];
                  this.imagesList.map((res) => {
                    array.push(res.imageOsVersion);
                  });
                  this.newimagesList = [...new Set(array)];
                  this.form.imageId = this.newimagesList[0];
                  this.changeimage(this.imagesList[0].imageOsVersion);
                  // this.$refs["addVmForm"].validateField("imageId");
                });
              } else {
                this.imagesList = [];
                this.newimagesList = [];
                this.lastimagename = "";
                this.lastimageid = "";
                this.changeimage();
              }
            }
          })
          .catch((error) => {});
      }
    },
    getSubNetList() {
      // 子网列表

      mainApi
        .subnetsList({ page_num: 1, page_size: 99999 })
        .then((res) => {
          if (res.subnets && res.subnets.length > 0) {
            this.subnetsDataList = res.subnets.filter((v) => {
              return v.phaseStatus == 1;
            });
          } else {
            this.subnetsDataList = [];
          }
        })
        .catch((error) => {});
    },
    getpubkeysList() {
      // 公钥列表

      mainApi
        .pubkeysList({})
        .then((res) => {
          this.pubkeysDataList = res.pubkeys ? res.pubkeys : [];
        })
        .catch((error) => {});
    },
    filterFlavor() {
      if (this.flavorsListData && this.flavorsListData.length > 0) {
        if (this.flavorsListType == "info") {
          this.flavorsList = this.flavorsListData.filter((v) => {
            return !v.gpuCount;
          });
        } else {
          this.flavorsList = this.flavorsListData.filter((v) => {
            return v.gpuCount && v.gpuCount > 0;
          });
        }

        if (this.flavorsList && this.flavorsList.length > 0) {
          this.$nextTick(() => {
            for (let i = 0; i < this.flavorsList.length; i++) {
              const item = this.flavorsList[i];
              if (item.available) {
                this.currentFlavorRow = item;
                if (this.copy_params.flavorId) {
                  this.form.flavorId = this.copy_params.flavorId;
                } else {
                  this.form.flavorId = item.flavorId;
                }
                break;
              } else {
                this.currentFlavorRow = "";
                this.form.flavorId = "";
              }
            }
          });
        }
        this.$nextTick(() => {
          this.$refs["addVmForm"].validateField("flavorId");
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
          this.flavorsList = [];
          this.currentFlavorRow = "";
          this.flavorsListData = res.flavors ? res.flavors : [];
          this.filterFlavor();
          if (res.flavors && res.flavors.length > 0) {
            this.getvmsHypervisorNodesList();
            res.flavors.forEach((item) => {
              this.CpuList.push(item.cpu);
              this.MemList.push(item.mem);
              this.DiskList.push(item.rootDisk);
            });
          }
        })
        .catch((error) => {});
    },

    addVmCountChange(val) {
      this.form.diskInfos = this.form.diskInfos.filter((item) => {
        // 过滤数据盘
        return item.diskStatus === true;
      });
      this.form.networkInfos.forEach((v) => {
        // 过滤网络
        v.staticStatus = 1;
      });
      if (this.eipData.eipType == 2) {
        this.eipData.eipType = 0;
        this.form.eipId = "";
      }
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
      // 添加云硬盘
      // 添加数据盘
      if (this.form.diskInfos.length >= 4) {
        this.$notify({
          title: this.$t("message.maxAdd4DataDisk"),
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
      this.$nextTick(() => {
        var element = document.getElementById("vm_screen");
        element.scrollTop = element.scrollHeight;
      });
    },
    removeNetworkData(data, index) {
      // 删除数据
      data.splice(index, 1);
    },
    removeDiskData(data, index) {
      // 删除数据
      data.splice(index, 1);
      this.$nextTick(() => {
        this.$refs["addVmForm"].validate();
      });
    },
    getCheckedVolumes(data) {
      // 获取选中的云盘
      var volumeIdData = this.form.diskInfos.map((item) => {
        return item.volumeId;
      });
      return volumeIdData.includes(data);
    },
    // 改变安全组
    changesecgroup(e) {
      if (e.includes(this.defaultgroupssgId)) {
        this.defaultgroups = true;
      } else {
        this.defaultgroups = false;
      }
    },
    getsecGroupsList() {
      // 安全组列表

      mainApi
        .sgsList({ page_num: 1, page_size: 99999 })
        .then((res) => {
          if (res.securityGroups && res.securityGroups.length > 0) {
            res.securityGroups.map((item) => {
              if (item.isDefault) {
                if (item.name) {
                  item.name =
                    item.name +
                    "(" +
                    this.$t("message.defaultSecurityGroup") +
                    ")";
                } else {
                  item.name = this.$t("message.defaultSecurityGroup");
                }
                this.defaultgroups = true;
                this.defaultgroupssgId = item.sgId;
                this.form.sgIds = [item.sgId];
              }
              this.secGroupsList = res.securityGroups;
            });
          } else {
            this.secGroupsList = [];
          }
        })
        .catch((error) => {});
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
          this.vmsHypervisorNodesList = res.nodeAllocationInfos
            ? res.nodeAllocationInfos
            : [];
          if (this.addType == "node") {
            this.currentNodeRow = this.drawerData;
            this.form.nodeId = this.drawerData.nodeId;
          }
          this.vmsHypervisorNodesLoading = false;
        })
        .catch((error) => {
          this.vmsHypervisorNodesLoading = false;
        });
    },
    // changeStoragePool (storagePoolId) {
    //   //改变云盘类型
    //   // this.form.diskInfos = this.form.diskInfos.filter((item) => {
    //   //   return item.diskStatus === true;
    //   // });
    //   this.form.diskInfos = [];
    //   this.getVolumesList(storagePoolId);
    // },
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
          this.volumesList = res.volumes ? res.volumes : [];
        });
    },
    changeStoragePool(item) {
      // 改变云盘类型
      if (this.form.storagePoolId == item) {
        return;
      }
      this.form.storagePoolId = item;
      this.form.diskInfos = [];
      this.getVolumesList(item);
    },
    getstoragePoolName(id) {
      // 获取云盘类型名称
      return this.storagePoolsList.filter((e) => {
        return id == e.poolId;
      })[0].name;
    },
    getStoragePools() {
      // 云盘类型列表
      mainApi
        .storagePoolsList({ page_num: 1, page_size: 99999 })
        .then((res) => {
          // this.form.storagePoolId = "";
          this.storagePoolsList = res.storagePools ? res.storagePools : [];
          if (res.storagePools && res.storagePools.length > 0) {
            if (this.form.storagePoolId) {
            } else {
              this.form.storagePoolId = res.storagePools[0].poolId;
            }
            this.getVolumesList(this.form.storagePoolId);
          }
        });
    },
    // 新建跳转
    createanewjump(name, vpcId) {
      const route = this.$router.resolve({
        name: name,
        params: { id: vpcId },
      });
      window.open(route.href, "_blank");
    },
    // 子网的刷新
    refreshSubNetList(item, index) {
      this.getSubNetList();
      this.changeIp(item, index);
    },
  },
};
</script>

<style lang="scss" scoped>
.iconfont {
  font-size: 12px;
}

.addVmFormStyle {
  ::v-deep .el-form-item {
    .el-form-item__label {
      font-weight: bold;
      padding-bottom: 4px;
    }

    &.formLabelPositionStyle {
      .el-form-item__label {
        float: left;
        margin-right: 20px;
      }

      .el-form-item__content {
        display: inline-block;
      }
    }
  }

  ::v-deep .formLabelStyle {
    .el-form-item__label {
      margin-top: 10px;
    }
  }

  .imageTypeSelectStyle {
    ::v-deep .el-input__inner {
      padding-left: 30px;
    }
  }
}

::v-deep .region .el-radio-button__orig-radio:checked + .el-radio-button__inner,
::v-deep
  .regionyear
  .el-radio-button__orig-radio:checked
  + .el-radio-button__inner {
  color: rgb(64, 158, 255);
  background-color: rgb(64, 158, 255, 0.2);
  border-color: rgb(64, 158, 255);
  -webkit-box-shadow: -1px 0 0 0 rgb(64, 158, 255);
  box-shadow: -1px 0 0 0 rgb(64, 158, 255);
}

::v-deep .region .el-radio-button__inner {
  width: 135px !important;
  font-size: 15px;
  height: 35px;
  line-height: 15px;
}

::v-deep .regionyear .el-radio-button__inner {
  width: 80px !important;
  font-size: 13px;
  height: 35px;
  line-height: 20px;
}

// 步骤条的样式
::v-deep .steps {
  .el-step {
    .el-step__line {
      background-color: rgba(0, 0, 0, 0.15);
      margin-right: 30px !important;
      margin-left: 105px !important;
      top: 50%;
      height: 1px;
    }

    .el-step__icon {
      width: 35px;
      height: 35px;
      font-size: 16px;
      border: 1px solid;

      .el-step__icon-inner {
        font-weight: unset !important;
      }
    }

    .el-step__head.is-process {
      color: rgb(64, 158, 255, 0.8);
      border-color: rgb(64, 158, 255, 0.8);
    }

    .el-step__head.is-success {
      color: rgb(64, 158, 255, 0.8);
      border-color: rgb(64, 158, 255, 0.8);
    }

    .is-process .el-step__icon.is-text {
      background: rgb(64, 158, 255, 0.8);
      color: #fff;
    }

    .el-step__title.is-process {
      color: rgb(56, 69, 87);
    }

    .el-step__title.is-success {
      color: #565656;
    }

    .el-step__title {
      position: absolute;
      top: calc((100% - 35px) / 2);
      left: calc(50% + 25px);
    }
  }
}

.custom-option .option-content {
  display: flex;
  flex-direction: column;
  line-height: 20px;
}

.custom-option {
  height: 42px !important;
}

.custom-option .value {
  color: #8492a6;
  font-size: 10px;
}

.disabled-element:hover {
  opacity: 0.5;
  cursor: not-allowed;
}

// 表格禁用的背景颜色
::v-deep .el-table .warning-row {
  // background: rgb(241, 245, 249) !important;
  color: #8492a6;
}

::v-deep .el-table .warning-row:hover {
  cursor: not-allowed;
}

::v-deep .item__error .el-form-item__error {
  margin-left: 70px;
}
</style>
