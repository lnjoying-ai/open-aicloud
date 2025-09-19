<template>
  <div class="operatePage">
    <div v-if="propShowType == 1">
      <el-popover ref="popover" placement="bottom" width="160" trigger="click">
        <div
          v-if="
            propShowBtn.includes('poweron') &&
            ([8, 63].includes(propVmDetail.phaseStatus) ||
              propVmDetail.phaseStatus == 66)
          "
          class="icon_cz"
          @click="toPoweron(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/switch.png"
            class="w-3 inline-block mr-1"
            alt=""
          />
          {{ $t("form.poweron") }}
        </div>
        <div
          v-if="
            propShowBtn.includes('poweroff') &&
            [6, 10, 29, 40].includes(propVmDetail.phaseStatus)
          "
          class="icon_cz"
          @click="openPoweroff(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/switch.png"
            class="w-3 inline-block mr-1"
            alt=""
          />
          {{ $t("form.poweroff") }}
        </div>
        <div
          v-if="
            propShowBtn.includes('reboot') &&
            [6, 10, 29, 40].includes(propVmDetail.phaseStatus)
          "
          class="icon_cz"
          @click="toReboot(propVmDetail)"
        >
          <i class="el-icon-refresh-right" />
          {{ $t("form.reboot") }}
        </div>
        <div
          v-if="propShowBtn.includes('edit')"
          class="icon_cz"
          @click="toEdit(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/edit.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.edit") }}
        </div>
        <div
          v-if="propShowBtn.includes('resetPassword')"
          class="icon_cz"
          @click="openResetPassword(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/resetPassword.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.resetPassword") }}
        </div>
        <div
          v-if="propShowBtn.includes('delete')"
          class="icon_cz"
          @click="openDelete"
        >
          <img
            src="@/assets/nextStack/btn/delete.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.delete") }}
        </div>
        <div
          v-if="propShowBtn.includes('eip') && !propVmDetail.boundPhaseStatus"
          class="icon_cz"
          @click="openEip(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/attach.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.attachEip") }}
        </div>
        <div
          v-if="
            propShowBtn.includes('eip') && propVmDetail.boundPhaseStatus == 82
          "
          class="icon_cz"
          @click="detachEip(propVmDetail, propVmDetail.eipId)"
        >
          <img
            src="@/assets/nextStack/btn/detach.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.detachEip") }}
        </div>
        <div
          v-if="propShowBtn.includes('snaps')"
          class="icon_cz"
          @click="openSnaps(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/snaps.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.snaps") }}
        </div>
        <div
          v-if="propShowBtn.includes('flavor')"
          class="icon_cz"
          @click="openFlavor(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/changeFlavor.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.changeFlavor") }}
        </div>

        <div
          v-if="propShowBtn.includes('images')"
          class="icon_cz"
          @click="openImages(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/rollBack.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.exportImage") }}
        </div>
        <div
          v-if="propShowBtn.includes('transfer') && (kind == 0 || kind == 1)"
          class="icon_cz"
          @click="openTransfer(propVmDetail)"
        >
          <img
            src="@/assets/nextStack/btn/qy.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.transfer") }}
        </div>

        <router-link
          v-if="propShowBtn.includes('secGroup')"
          :to="
            '/nextStack/vm/detail/' +
            propVmDetail.instanceId +
            '?type=' +
            'second'
          "
        >
          <div class="icon_cz">
            <img
              src="@/assets/nextStack/btn/secGroup.png"
              class="w-3 inline-block mr-1"
              alt=""
            />{{ $t("form.securityGroupPolicy") }}
          </div>
        </router-link>
        <router-link
          v-if="propShowBtn.includes('vnc')"
          :to="
            '/nextStack/vmCommand?instanceId=' +
            propVmDetail.instanceId +
            '&cloudId=' +
            propVmDetail.cloudId +
            '&type=' +
            'vnc'
          "
          target="_blank"
        >
          <div class="icon_cz">
            <img
              src="@/assets/nextStack/btn/code.png"
              class="w-3 inline-block mr-1"
              alt=""
            />{{ $t("form.openVnc") }}
          </div>
        </router-link>
        <template #reference>
          <el-button icon="el-icon-more" class="czbtn right_czbtn" />
        </template>
      </el-popover>
    </div>

    <div v-if="propShowType == 2">
      <el-button
        v-if="
          propShowBtn.includes('poweron') &&
          ([8, 63].includes(propVmDetail.phaseStatus) ||
            propVmDetail.phaseStatus == 66)
        "
        :size="$store.state.nextStack.viewSize.listSet"
        @click="toPoweron(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/switch.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.poweron") }}</el-button
      >
      <el-button
        v-if="
          propShowBtn.includes('poweroff') &&
          [6, 10, 29, 40].includes(propVmDetail.phaseStatus)
        "
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openPoweroff(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/switch.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.poweroff") }}</el-button
      >
      <el-button
        v-if="
          propShowBtn.includes('reboot') &&
          [6, 10, 29, 40].includes(propVmDetail.phaseStatus)
        "
        :size="$store.state.nextStack.viewSize.listSet"
        @click="toReboot(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/switch.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.reboot") }}</el-button
      >
      <el-button
        v-if="propShowBtn.includes('edit')"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="toEdit(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/edit.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.edit") }}</el-button
      >
      <el-button
        v-if="propShowBtn.includes('resetPassword')"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openResetPassword(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/resetPassword.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.resetPassword") }}</el-button
      >
      <el-button
        v-if="propShowBtn.includes('delete')"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openDelete"
        ><img
          src="@/assets/nextStack/btn/delete.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.delete") }}</el-button
      >

      <el-button
        v-if="propShowBtn.includes('eip') && !propVmDetail.boundPhaseStatus"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openEip(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/attach.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.attachEip") }}</el-button
      >
      <el-button
        v-if="
          propShowBtn.includes('eip') &&
          propVmDetail.networkDetailInfos &&
          propVmDetail.networkDetailInfos.length > 0 &&
          propVmDetail.networkDetailInfos[0].boundPhaseStatus == 82
        "
        :size="$store.state.nextStack.viewSize.listSet"
        @click="
          detachEip(propVmDetail, propVmDetail.networkDetailInfos[0].eipId)
        "
        ><img
          src="@/assets/nextStack/btn/detach.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.detachEip") }}</el-button
      >
      <el-button
        v-if="propShowBtn.includes('snaps')"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openSnaps(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/snaps.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.snaps") }}</el-button
      >
      <el-button
        v-if="propShowBtn.includes('flavor')"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openFlavor(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/changeFlavor.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.changeFlavor") }}</el-button
      >

      <el-button
        v-if="propShowBtn.includes('images')"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openImages(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/rollBack.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.exportImage") }}</el-button
      >
      <el-button
        v-if="propShowBtn.includes('transfer') && (kind == 0 || kind == 1)"
        :size="$store.state.nextStack.viewSize.listSet"
        @click="openTransfer(propVmDetail)"
        ><img
          src="@/assets/nextStack/btn/qy.png"
          class="w-3 inline-block mr-1"
          alt=""
        />{{ $t("form.transfer") }}</el-button
      >
      <router-link
        v-if="propShowBtn.includes('secGroup')"
        :to="
          '/nextStack/vm/detail/' +
          propVmDetail.instanceId +
          '?type=' +
          'second'
        "
        class="ml-2"
      >
        <el-button :size="$store.state.nextStack.viewSize.listSet"
          ><img
            src="@/assets/nextStack/btn/secGroup.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.securityGroupPolicy") }}</el-button
        >
      </router-link>
      <router-link
        v-if="propShowBtn.includes('vnc')"
        :to="
          '/nextStack/vmCommand?instanceId=' +
          propVmDetail.instanceId +
          '&cloudId=' +
          propVmDetail.cloudId +
          '&type=' +
          'vnc'
        "
        target="_blank"
        class="ml-2"
      >
        <el-button :size="$store.state.nextStack.viewSize.listSet"
          ><img
            src="@/assets/nextStack/btn/code.png"
            class="w-3 inline-block mr-1"
            alt=""
          />{{ $t("form.openVnc") }}</el-button
        >
      </router-link>
    </div>

    <!-- eip 绑定 -->
    <el-dialog
      :visible.sync="dialogEipVisible"
      :close-on-click-modal="false"
      width="800px"
      destroy-on-close
      :before-close="eipHandleClose"
      :append-to-body="true"
      :title="$t('form.attachEip')"
    >
      <div class="mb-4">
        <el-table
          ref="singleTableRef"
          max-height="300px"
          :data="eipTableData"
          highlight-current-row
          style="width: 100%"
          @current-change="eipHandleChange"
        >
          <el-table-column label="" width="40px">
            <template #default="scope">
              <span
                v-if="scope.row.eipId != eipForm.eipId"
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

          <el-table-column prop="date" label="EIP">
            <template #default="scope">
              {{ scope.row.publicIp || scope.row.ipAddress }}
            </template>
          </el-table-column>

          <el-table-column prop="addressType" :label="$t('form.IPV4address')">
            <template #default="scope">
              <span>{{ scope.row.addressType === 0 ? "IPv4" : "Ipv6" }}</span>
            </template>
          </el-table-column>

          <el-table-column prop="createTime" :label="$t('form.createTime')" />
        </el-table>
        <div class="flex justify-end mt-4 px-4">
          <el-pagination
            :page_num="eipForm.page_num"
            :page-size="eipForm.page_size"
            :page-sizes="[10]"
            :current-page="eipForm.page_num"
            layout="total, prev, pager, next, jumper"
            :total="eipForm.total"
            @size-change="eipHandleSizeChange"
            @current-change="eipHandleCurrentChange"
          />
        </div>
      </div>

      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            size="small"
            :loading="eipVisibleloading"
            @click="toEip()"
            >{{ $t("form.bindImmediately") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
    <!-- 快照add -->
    <el-dialog
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
      width="600px"
      destroy-on-close
      :before-close="snapsHandleClose"
      :append-to-body="true"
      :title="$t('form.createSnaps')"
    >
      <el-form
        ref="snapsformRef"
        :rules="rules"
        :model="snapsform"
        :size="$store.state.nextStack.viewSize.main"
        label-width="120px"
      >
        <el-form-item :label="$t('form.instance') + ':'">
          <el-input
            v-model="nowVmData.name"
            class="w-60"
            :disabled="true"
            autocomplete="off"
            placeholder="-"
          />
        </el-form-item>
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="snapsform.name"
            class="w-60"
            autocomplete="off"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.description') + ':'" prop="description">
          <el-input
            v-model="snapsform.description"
            class="w-96"
            type="textarea"
            maxlength="255"
            show-word-limit
            :rows="2"
            autocomplete="off"
            :placeholder="$t('form.pleaseInputDescription')"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            size="small"
            :loading="snapLoading"
            @click="toSnaps()"
            >{{ $t("form.createImmediately") }}</el-button
          >
        </span>
      </template>
    </el-dialog>
    <!-- 变更规格 -->
    <el-dialog
      :visible.sync="dialogFlavorVisible"
      :close-on-click-modal="false"
      width="1000px"
      destroy-on-close
      :before-close="flavorHandleClose"
      :append-to-body="true"
      :title="$t('form.changeFlavor')"
    >
      <el-form
        ref="flavorformRef"
        :rules="flavorrules"
        :model="flavorform"
        label-width="110px"
        :size="$store.state.nextStack.viewSize.main"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item :label="$t('form.name') + ':'">
              <span>{{ nowVmData.name || "-" }}</span>
            </el-form-item>

            <el-form-item :label="$t('form.os') + ':'">
              <span>{{ nowVmData.imageName || "-" }}</span>
            </el-form-item>

            <el-form-item :label="$t('form.description') + ':'">
              <span>{{ nowVmData.description || "-" }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('form.id') + ':'">
              <span>{{ nowVmData.instanceId || "-" }}</span>
            </el-form-item>
            <el-form-item
              v-if="kind == 0 || kind == 1"
              :label="$t('form.hypervisorNode')"
            >
              <span>
                <router-link
                  :to="
                    '/nextStack/hypervisorNodes/detail/' +
                    nowVmData.hypervisorNodeId
                  "
                  target="_blank"
                  class="text-blue-400"
                  >{{ nowVmData.hypervisorNodeName || "-" }}</router-link
                >
              </span>
            </el-form-item>
            <el-form-item :label="$t('form.status') + ':'">
              <el-tag
                :size="$store.state.nextStack.viewSize.tagStatus"
                :type="filtersFun.getVmStatus(nowVmData.phaseStatus, 'tag')"
                >{{
                  filtersFun.getVmStatus(nowVmData.phaseStatus, "status")
                }}</el-tag
              >
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item
          :label="$t('form.flavor') + ':'"
          prop="flavorId"
          class="relative"
        >
          <div class="overflow-hidden w-full pt-2">
            <el-button
              class="float-right mb-2"
              :size="$store.state.nextStack.viewSize.tabChange"
              type="primary"
              @click="clearFilter"
              >{{ $t("form.resetSort") }}</el-button
            >
          </div>
          <el-table
            ref="singleTableRef"
            max-height="300px"
            :size="$store.state.nextStack.viewSize.main"
            :data="flavorsList"
            highlight-current-row
            :row-class-name="flavorsTableRowClassName"
            style="width: 100%"
            @current-change="flavorsHandleCurrentChange"
          >
            <el-table-column label="" width="50px">
              <template #default="scope">
                <span v-if="scope.row.disabled">
                  <small>{{ $t("form.current") }}</small>
                </span>
                <span v-else>
                  <span
                    v-if="!scope.row.available"
                    class="w-3 h-3 block border rounded-sm border-gray-300 disabled-element"
                    style="background: rgb(245, 247, 250)"
                  />
                  <span v-else>
                    <span
                      v-if="scope.row.flavorId != flavorform.flavorId"
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
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="date" :label="$t('form.name')">
              <template #default="scope">
                <router-link
                  v-if="kind == 0 || kind == 1"
                  :to="'/nextStack/flavor/detail/' + scope.row.flavorId"
                  target="_blank"
                  class="text-blue-400"
                  >{{ scope.row.name || "-" }}</router-link
                >
                <span v-if="kind != 0 && kind != 1">{{
                  scope.row.name || "-"
                }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" :label="$t('form.type')">
              <template #default="scope">
                <span v-if="scope.row.gpuCount">{{
                  $t("form.gpuCompute")
                }}</span>
                <span v-else>{{ $t("form.generalCompute") }}</span>
              </template>
            </el-table-column>
            <el-table-column
              prop="cpu"
              :label="$t('form.cpu')"
              :filters="flavorCpu"
              :filter-method="filterCpu"
            >
              <template #default="scope">
                {{ scope.row.cpu || "-" }}{{ $t("unit.cpu") }}
              </template>
            </el-table-column>
            <el-table-column
              prop="mem"
              :label="$t('form.mem')"
              :filters="flavorMem"
              :filter-method="filterMem"
            >
              <template #default="scope">
                {{ scope.row.mem || "-" }}{{ $t("unit.mem") }}
              </template>
            </el-table-column>

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
            {{ $t("form.selected") }}:{{ currentFlavorRow.name }}
          </span>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button
            type="primary"
            size="small"
            :loading="flavorLoading"
            @click="toFlavor()"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </template>
    </el-dialog>

    <!-- 导出镜像 -->
    <el-dialog
      :visible.sync="dialogExport"
      :close-on-click-modal="false"
      width="600px"
      destroy-on-close
      :element-loading-text="$t('domain.loading')"
      :before-close="exportHandleClose"
      :append-to-body="true"
      :title="$t('form.exportImage')"
    >
      <div class="block overflow-hidden">
        <el-form
          ref="imagesFormRef"
          :model="imagesForm"
          label-width="120px"
          :rules="imagesFormRules"
          :size="$store.state.nextStack.viewSize.main"
        >
          <el-form-item :label="$t('form.imageName') + ':'" prop="imageName">
            <el-input
              v-model="imagesForm.imageName"
              class="!w-50"
              :placeholder="$t('form.pleaseInputImageName')"
            />
          </el-form-item>
        </el-form>

        <el-alert type="info" class="mb-4" :closable="false">
          <div class="mb-2">
            <span class="block">{{ $t("form.imageTips.text1") }}</span>
          </div>
          <div class="mb-2">
            <span class="block">{{ $t("form.imageTips.text2") }}</span>
          </div>
          <div class="mb-2">
            <span class="block">{{ $t("form.imageTips.text3") }}</span>
            <span class="block">{{ $t("form.imageTips.text4") }}</span>
            <span class="block">{{ $t("form.imageTips.text5") }}</span>
            <span class="block">{{ $t("form.imageTips.text6") }}</span>
          </div>
          <div class="mb-2">
            <span class="block">{{ $t("form.imageTips.text7") }}</span>
            <span class="block">{{ $t("form.imageTips.text8") }}</span>
            <span class="block">{{ $t("form.imageTips.text9") }}</span>
            <span class="block">{{ $t("form.imageTips.text10") }}</span>
            <span class="block">{{ $t("form.imageTips.text11") }}</span>
            <span class="block">{{ $t("form.imageTips.text12") }}</span>
            <span class="block">{{ $t("form.imageTips.text13") }}</span>
            <span class="block">{{ $t("form.imageTips.text14") }}</span>
          </div>
          <div class="mb-2">
            <span class="block">
              {{ $t("form.imageTips.text15") }}
            </span>
          </div>
        </el-alert>
      </div>
      <div class="dialog-footer text-right">
        <el-button type="text" size="small" @click="exportHandleClose()">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" size="small" @click="toExport()">{{
          $t("form.export")
        }}</el-button>
      </div>
    </el-dialog>
    <!-- --迁移-- -->
    <el-dialog
      :visible.sync="dialogTransfer"
      :close-on-click-modal="false"
      width="1200px"
      destroy-on-close
      :before-close="transferHandleClose"
      :append-to-body="true"
      :title="$t('form.transfer')"
    >
      <div class="block overflow-hidden">
        <el-row :gutter="10">
          <el-col :span="18">
            <el-table
              ref="multipleTableRef"
              v-loading="transferLoading"
              :element-loading-text="$t('domain.loading')"
              :data="transferTableData"
              max-height="calc(100% - 3rem)"
              class="!overflow-y-auto hypervisorNodesDialog transfertable"
              stripe
              :scrollbar-always-on="false"
              @select="handleSelectionChange"
            >
              <el-table-column
                type="selection"
                width="55"
                :selectable="selectable"
              />
              <el-table-column prop="date" :label="$t('form.name')">
                <template #default="scope">
                  <router-link
                    :to="
                      '/nextStack/hypervisorNodes/detail/' + scope.row.nodeId
                    "
                  >
                    <span class="text-blue-400 cursor-pointer">{{
                      scope.row.name
                    }}</span>
                  </router-link>
                </template>
              </el-table-column>
              <!-- <el-table-column prop="hostname" label="主机名" /> -->
              <el-table-column prop="manageIp" :label="$t('form.manageIp')" />
              <el-table-column :label="$t('form.cpuAvailable')">
                <template #default="scope">
                  <div v-if="scope.row.cpuLogCount">
                    <p>
                      {{ scope.row.cpuLogCount - scope.row.usedCpuSum }}
                      {{ $t("unit.cpu") }}/{{ scope.row.cpuLogCount }}
                      {{ $t("unit.cpu") }}
                    </p>
                    <p>{{ scope.row.cpuModel }}</p>
                  </div>
                  <div v-else>-</div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.gpuAvailable')">
                <template #default="scope">
                  <div v-if="scope.row.gpuTotal">
                    <p>
                      {{ scope.row.availableGpuCount }}/{{ scope.row.gpuTotal }}
                    </p>
                    <p>{{ scope.row.gpuName }}</p>
                  </div>
                  <div v-else>-</div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.memAvailable')">
                <template #default="scope">
                  <div v-if="scope.row.memTotal">
                    {{ scope.row.memTotal - scope.row.usedMemSum }}
                    {{ $t("unit.mem") }}/{{ scope.row.memTotal }}
                    {{ $t("unit.mem") }}
                  </div>
                  <div v-else>-</div>
                </template>
              </el-table-column>
              <el-table-column :label="$t('form.ibAvailable')">
                <template #default="scope">
                  <div v-if="scope.row.ibTotal">
                    {{ scope.row.availableIbCount }}/{{ scope.row.ibTotal }}
                  </div>
                  <div v-else>-</div>
                </template>
              </el-table-column>
              <el-table-column
                prop="createTime"
                :label="$t('form.createTime')"
              />
            </el-table>
            <div class="flex justify-end mt-4 px-4">
              <el-pagination
                :page_num="transferForm.page_num"
                :page-size="transferForm.page_size"
                :page-sizes="[10]"
                :current-page="transferForm.page_num"
                layout="total, prev, pager, next, jumper"
                :total="transferForm.total"
                @size-change="transferHandleSizeChange"
                @current-change="transferHandleCurrentChange"
              />
            </div>
          </el-col>
          <el-col :span="6">
            <el-card class="!border-none mb-3">
              <template #header>
                <div class="">
                  <span>{{ $t("form.instance") }}:{{ vmDetail.name }}</span>
                </div>
              </template>
              <div class="text item">
                <el-form
                  :model="vmDetail"
                  label-width="100px"
                  :size="$store.state.nextStack.viewSize.main"
                >
                  <el-form-item :label="$t('form.destinationNode') + ':'">
                    <span v-if="multipleSelection.name">
                      <router-link
                        :to="
                          '/nextStack/hypervisorNodes/detail/' +
                          multipleSelection.nodeId
                        "
                        class="text-blue-400"
                        >{{ multipleSelection.name }}</router-link
                      >
                    </span>
                    <span v-else> {{ $t("form.noDestinationNode") }} </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.currentNode') + ':'">
                    <span>
                      <router-link
                        :to="
                          '/nextStack/hypervisorNodes/detail/' +
                          vmDetail.hypervisorNodeId
                        "
                        class="text-blue-400"
                        >{{ vmDetail.hypervisorNodeName || "-" }}</router-link
                      >
                    </span>
                  </el-form-item>

                  <el-form-item :label="$t('form.os') + ':'">
                    <span>{{ vmDetail.imageName || "-" }}</span>
                  </el-form-item>

                  <el-form-item :label="$t('form.status') + ':'">
                    <el-tag
                      :size="$store.state.nextStack.viewSize.tagStatus"
                      :type="
                        filtersFun.getVmStatus(vmDetail.phaseStatus, 'tag')
                      "
                      >{{
                        filtersFun.getVmStatus(vmDetail.phaseStatus, "status")
                      }}</el-tag
                    >
                  </el-form-item>

                  <el-form-item :label="$t('form.cpu') + ':'">
                    <span>
                      {{ vmDetail.cpu || "-" }} {{ $t("unit.cpu") }}
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.mem') + ':'">
                    <span>
                      {{ vmDetail.mem || "-" }} {{ $t("unit.mem") }}
                    </span>
                  </el-form-item>
                  <el-form-item :label="$t('form.rootDisk') + ':'">
                    <span>
                      {{ vmDetail.rootDisk || "-" }} {{ $t("unit.disk") }}
                    </span>
                  </el-form-item>
                  <el-row
                    v-if="vmDetail.diskInfos && vmDetail.diskInfos.length > 0"
                  >
                    <el-col
                      v-for="(item, index) in vmDetail.diskInfos"
                      :key="index"
                      :span="24"
                    >
                      <el-form-item
                        :label="$t('form.dataDisk') + (index + 1) + ':'"
                      >
                        <span>
                          {{ item.volumeName }} ({{ item.size }}
                          {{ $t("unit.disk") }})
                        </span>
                      </el-form-item>
                    </el-col>
                  </el-row>
                </el-form>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
      <div class="dialog-footer text-right">
        <el-button type="text" size="small" @click="transferHandleClose()">
          {{ $t("form.cancel") }}</el-button
        >
        <el-button
          type="primary"
          size="small"
          :loading="transloading"
          @click="toTransfer()"
          >{{ $t("form.transfer") }}</el-button
        >
      </div>
    </el-dialog>
    <el-dialog
      :title="$t('form.editInstance')"
      :visible.sync="editDialogVisible"
      :close-on-click-modal="false"
      width="600px"
      :append-to-body="true"
      :before-close="editHandleClose"
    >
      <vmedit :id="editId" ref="vmeditRef" @close="editHandleClose" />
    </el-dialog>
    <!-- 重置密码 -->
    <el-dialog
      :visible.sync="dialogResetPassword"
      :close-on-click-modal="false"
      width="500px"
      destroy-on-close
      :before-close="resetPasswordHandleClose"
      :append-to-body="true"
      :title="$t('form.resetHostnamePassword')"
    >
      <el-alert
        :title="$t('form.resetHostnamePasswordTips')"
        class="mb-5"
        type="warning"
        :closable="false"
        show-icon
      />
      <el-form-item :label="$t('form.instance') + ':'">
        <el-input
          v-model="nowVmData.name"
          :disabled="true"
          autocomplete="off"
          placeholder="-"
        />
      </el-form-item>
      <el-form
        ref="resetPasswordFormRef"
        :rules="resetPasswordRules"
        :model="resetPasswordForm"
        label-width="120px"
        :size="$store.state.nextStack.viewSize.main"
      >
        <el-form-item :label="$t('form.instance') + ':'">
          <el-input
            v-model="nowVmData.name"
            class="w-60"
            :disabled="true"
            autocomplete="off"
            placeholder="-"
          />
        </el-form-item>
        <el-form-item :label="$t('form.loginName') + ':'">
          <el-input
            v-model="exampleinfo.sysUsername"
            class="w-60"
            :disabled="true"
            autocomplete="off"
            placeholder="-"
          />
        </el-form-item>
        <el-form-item :label="$t('form.hostname') + ':'" prop="hostname">
          <el-input
            v-model="resetPasswordForm.hostname"
            class="w-60"
            autocomplete="off"
            :placeholder="$t('form.pleaseInputHostname')"
          />
        </el-form-item>
        <el-form-item :label="$t('form.password') + ':'" prop="sysPassword">
          <el-input
            v-model="resetPasswordForm.sysPassword"
            class="w-60"
            type="password"
            show-password
            autocomplete="off"
            :placeholder="$t('form.noNeedToModifyPassword')"
          />
        </el-form-item>
      </el-form>
      <div class="dialog-footer text-right">
        <span class="dialog-footer">
          <el-button
            type="primary"
            size="small"
            :loading="resetPasswordLoading"
            @click="toResetPassword()"
            >{{ $t("form.confirm") }}</el-button
          >
        </span>
      </div>
      <!-- <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="toResetPassword()" size="small">确认</el-button>
        </span>
      </template> -->
    </el-dialog>
    <!-- 删除实例 -->
    <el-dialog
      :title="$t('form.deleteInstance')"
      :visible.sync="deleteDialogVisible"
      :close-on-click-modal="false"
      width="800px"
      :append-to-body="true"
      :before-close="deleteHandleClose"
    >
      <div class="ml-9 mr-9">
        <div>
          <el-alert
            v-if="deleteinfo.snapInfos || iseip"
            style="
              background-color: rgb(254, 240, 240);
              color: rgb(245, 108, 108);
            "
            :title="$t('form.deleteInstanceTips')"
            type="warning"
            show-icon
            :closable="false"
          />
          <p class="mt-5" style="font-size: 16px">
            {{ $t("form.confirmDeleteInstance") }}
          </p>
          <div style="display: flex; font-size: 13px" class="italic">
            <p class="mr-5">
              {{ deleteinfo.name ? deleteinfo.name : "-" }}
            </p>
            <p>ID:{{ deleteinfo.instanceId ? deleteinfo.instanceId : "-" }}</p>
          </div>
          <div class="mt-6" style="font-size: 13px; display: flex">
            <p>{{ $t("form.associatedResources") }}:</p>
            <div>
              <!-- 云盘相关资源 -->
              <div
                v-for="diskdata in deleteinfo.diskInfos"
                :key="diskdata.vpcId"
              >
                <div style="display: flex">
                  <p class="mr-5">
                    【{{ $t("form.cloudDisk") }}】
                    <router-link
                      :to="'/nextStack/volumes/detail/' + diskdata.volumeId"
                      class="text-blue-400 italic"
                      target="_blank"
                      >{{
                        diskdata.volumeName ? diskdata.volumeName : "-"
                      }}</router-link
                    >
                  </p>
                  <p class="italic">
                    ID:{{ diskdata.volumeId ? diskdata.volumeId : "-" }}
                  </p>
                </div>
              </div>
              <!-- 快照相关资源 -->
              <div
                v-for="snapdata in deleteinfo.snapInfos"
                :key="snapdata.snapId"
              >
                <div style="display: flex">
                  <div class="mr-5">
                    【{{ $t("form.snapshot") }}】
                    <router-link
                      v-if="kind == 0 || kind == 1"
                      :to="'/nextStack/snap/detail/' + snapdata.snapId"
                      class="text-blue-400 italic"
                      target="_blank"
                      >{{
                        snapdata.snapName ? snapdata.snapName : "-"
                      }}</router-link
                    >
                    <span v-if="kind != 0 && kind != 1" class="italic">
                      {{ snapdata.snapName ? snapdata.snapName : "-" }}
                    </span>
                  </div>
                  <div class="italic">
                    ID:{{ snapdata.snapId ? snapdata.snapId : "-" }}
                  </div>
                </div>
              </div>
              <!-- vpc相关资源 -->
              <div
                v-for="vpcdata in deleteinfo.networkDetailInfos"
                :key="vpcdata.vpcId"
              >
                <div style="display: flex">
                  <div class="mr-5">
                    【{{ $t("form.vpc") }}】
                    <router-link
                      v-if="kind == 0 || kind == 1"
                      :to="'/nextStack/vpc/detail/' + vpcdata.vpcId"
                      class="text-blue-400 italic"
                      target="_blank"
                      >{{
                        vpcdata.vpcName ? vpcdata.vpcName : "-"
                      }}</router-link
                    >
                    <span v-if="kind != 0 && kind != 1" class="italic">
                      {{ vpcdata.vpcName ? vpcdata.vpcName : "-" }}
                    </span>
                  </div>
                  <div class="italic">
                    ID:{{ vpcdata.vpcId ? vpcdata.vpcId : "-" }}
                  </div>
                </div>
              </div>
              <!-- EIP相关资源 -->
              <div
                v-for="eipdata in deleteinfo.networkDetailInfos"
                :key="eipdata.eipId"
              >
                <div v-if="eipdata.eip" style="display: flex">
                  【{{ $t("form.eip") }}】
                  <p class="mr-5 italic">
                    {{ eipdata.eip ? eipdata.eip : "-" }}
                  </p>
                  <p class="italic">
                    ID:{{ eipdata.eipId ? eipdata.eipId : "-" }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="mt-5">
          <el-alert
            :title="$t('form.deleteInstanceTips2')"
            type="warning"
            show-icon
            :closable="false"
          />
          <el-alert
            class="mt-5"
            :title="$t('form.deleteInstanceTips3')"
            type="warning"
            show-icon
            :closable="false"
          />
        </div>
      </div>
      <div class="dialog-footer text-right mt-10">
        <el-button type="text" size="small" @click="deleteHandleClose()">
          {{ $t("form.cancel") }}</el-button
        >
        <el-button
          type="primary"
          :loading="exampleloading"
          size="small"
          @click="todelete(propVmDetail)"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
    <!-- 关闭实例 -->
    <el-dialog
      :title="$t('form.instancePoweroff')"
      :visible.sync="powerOffDialogVisible"
      :close-on-click-modal="false"
      width="800px"
      :append-to-body="true"
      :before-close="powerOffHandleClose"
    >
      <div class="ml-9 mr-9">
        <div>
          <div class="mt-6 overflow-hidden" style="font-size: 13px">
            <p class="w-20 float-left">{{ $t("form.instance") }}：</p>
            <div>
              <strong>{{ propVmDetail.name }}</strong>
              {{ $t("form.instancePoweroffTips") }}
            </div>
          </div>
          <div class="mt-6 overflow-hidden" style="font-size: 13px">
            <p class="w-20 float-left" style="word-break: break-word">
              {{ $t("form.poweroffMode") }}：
            </p>
            <div class="table">
              <div
                class="border cursor-pointer px-4 py-2 mb-2"
                :class="powerOffType == '1' ? 'border-blue-300 bg-blue-50' : ''"
                @click="powerOffType = '1'"
              >
                <el-radio v-model="powerOffType" label="1">{{
                  $t("form.normalPoweroff")
                }}</el-radio>
                <div class="mt-1 pl-4">
                  <p>{{ $t("form.normalPoweroffTips1") }}</p>
                </div>
              </div>

              <div
                class="border cursor-pointer px-4 py-2"
                :class="powerOffType == '2' ? 'border-blue-300 bg-blue-50' : ''"
                @click="powerOffType = '2'"
              >
                <el-radio v-model="powerOffType" label="2">{{
                  $t("form.savingPoweroff")
                }}</el-radio>
                <div class="mt-1 pl-4">
                  <p>
                    {{ $t("form.savingPoweroffTips1") }}
                  </p>
                  <p>
                    {{ $t("form.savingPoweroffTips2") }}
                  </p>
                  <p>
                    {{ $t("form.savingPoweroffTips3") }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="dialog-footer text-right mt-10">
        <el-button type="text" size="small" @click="powerOffHandleClose()">
          {{ $t("form.cancel") }}</el-button
        >
        <el-button
          type="primary"
          :loading="exampleloading"
          size="small"
          @click="toPoweroff(propVmDetail)"
          >{{ $t("form.confirm") }}</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import filtersFun from "@/utils/statusFun";
import mainApi from "@/api/nextStack/mainApi";
import vmedit from "./edit.vue";
export default {
  components: {
    vmedit,
  },
  props: {
    propVmDetail: {
      type: Object,
      default: {},
    },
    propShowType: {
      type: Number,
      default: 1,
    },
    propShowBtn: {
      type: Array,
      default: [],
    },
  },
  data() {
    const validatePass = (rule, value, callback) => {
      if (value === "") {
        callback();
      } else {
        const reg =
          /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\|\;\:\'\"\,\.\<\>\/\?])[\da-zA-Z\~\!\@\#\$\%\^\&\*\(\)\_\+\-\=\|\;\:\'\"\,\.\<\>\/\?]{8,20}$/;

        if (!reg.test(value)) {
          callback(new Error(this.$t("message.pleaseInput820")));
        } else {
          callback();
        }
      }
    };
    return {
      transloading: false,
      exampleloading: false,
      addVmCount: 1,
      resetPasswordLoading: false,
      collapseNames: ["1", "2"],
      // 打开的实例的数据
      virtualmachine: 0,

      filtersFun: filtersFun,
      nowVmData: "", // 当前实例信息
      // 关闭实例
      powerOffDialogVisible: false,
      powerOffType: "1",
      // 编辑
      editDialogVisible: false,
      editId: "",
      // eip
      eipTableData: [],
      dialogEipVisible: false,
      eipVisibleloading: false,
      // eip 搜索 筛选
      eipForm: {
        eipId: "",
        name: "",
        page_num: 1,
        page_size: 10,
        total: 0,
      },
      // 快照
      dialogFormVisible: false,
      snapLoading: false,
      snapsform: {
        name: "",
        description: "",
        vmInstanceId: "",
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t("message.pleaseInputName"),
            trigger: "blur",
          },
          {
            min: 3,
            max: 64,
            message: this.$t("message.pleaseInput364"),
            trigger: "blur",
          },
        ],
      },
      resetPasswordRules: {
        hostname: [
          {
            required: true,
            message: this.$t("message.pleaseInputHostname"),
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
      },
      // 规格
      dialogFlavorVisible: false,
      flavorLoading: false,
      flavorform: {
        flavorId: "",
      },
      flavorrules: {
        flavorId: [
          {
            required: true,
            message: this.$t("message.pleaseSelectFlavor"),
            trigger: "change",
          },
        ],
      },

      // 重置密码
      resetPasswordForm: {
        hostname: "",
        sysPassword: "",
      },
      dialogResetPassword: false,
      currentFlavorRow: "",
      flavorsList: [],
      CpuList: [],
      MemList: [],
      DiskList: [],
      flavorCpu: "",
      flavorMem: "",
      flavorDisk: "",
      // 导出镜像
      dialogExport: false,
      imagesForm: {
        imageName: "",
        // imageOsType: 0,
        isPublic: false,
      },
      imagesFormRules: {
        imageName: [
          {
            required: true,
            validator: this.$script.validateName,
            trigger: "change",
          },
        ],
      },
      // 迁移
      dialogTransfer: false,
      transferloadingDialog: false,
      transferLoading: false,
      transferTableData: [],
      transferForm: {
        // 搜索 筛选
        name: "",
        page_num: 1,
        page_size: 10,
        total: 0,
      },
      vmDetail: "",
      multipleSelection: "",
      selectflavorId: "",

      // 删除实例的弹框
      deleteDialogVisible: false,
      // 点击删除绑定的数据
      deleteinfo: {},
      // 实例的详情信息
      exampleinfo: {},
      // 是否有eip
      iseip: false,
    };
  },
  created() {},
  mounted() {},
  computed: {
    ...mapGetters(["kind"]),
  },

  methods: {
    toEdit(item) {
      // 编辑
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.editId = item.instanceId;
      this.editDialogVisible = true;
    },
    editHandleClose() {
      this.$refs.vmeditRef.resetForm();
      this.editId = "";
      this.editDialogVisible = false;
      this.$emit("initVmList");
    },

    openResetPassword(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      mainApi.vmsInstabcesDetail(item.instanceId).then((res) => {
        this.exampleinfo = res;
        this.nowVmData = item;
        this.resetPasswordForm.hostname = item.hostname;
        this.dialogResetPassword = true;
      });
    },
    toResetPassword() {
      this.$refs.resetPasswordFormRef.validate(async (valid) => {
        if (valid) {
          this.resetPasswordLoading = true;
          var data = JSON.parse(JSON.stringify(this.resetPasswordForm));
          if (!data.sysPassword) {
            delete data.sysPassword;
          }
          mainApi
            .vmsResetPassword(data, this.nowVmData.instanceId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startResetHostnamePassword"),
                type: "success",
                duration: 2500,
              });
              this.resetPasswordLoading = false;
              this.dialogResetPassword = false;
              this.$refs.resetPasswordFormRef.resetFields();
              setTimeout(() => {
                this.$emit("restartDetail");
              }, 5000);
            })
            .catch((error) => {
              this.resetPasswordLoading = false;
            });
        } else {
          this.resetPasswordLoading = false;
        }
      });
    },
    resetPasswordHandleClose(done) {
      this.$refs.resetPasswordFormRef.resetFields();
      done();
    },
    snapsHandleClose(done) {
      this.$refs.snapsformRef.resetFields();
      done();
    },
    toPoweron(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.$confirm(
        this.$t("message.confirmOpenInstance", { name: item.name }),
        this.$t("message.openInstance"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .vmsInstabcesPoweron(item.instanceId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startOpenInstance"),
                type: "success",
                duration: 2500,
              });
              setTimeout(() => {
                this.$emit("restartDetail");
              }, 5000);
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    // 打开确认关闭实例窗口
    openPoweroff(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.powerOffDialogVisible = true;
    },
    // 关闭确认关闭实例
    powerOffHandleClose() {
      this.powerOffDialogVisible = false;
    },
    // 确认关闭实例
    toPoweroff(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.exampleloading = true;
      if (this.powerOffType == "1") {
        mainApi
          .vmsInstabcesPoweroff(item.instanceId)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startPoweroffInstance"),
              type: "success",
              duration: 2500,
            });
            this.exampleloading = false;
            this.powerOffDialogVisible = false;
            setTimeout(() => {
              this.$emit("restartDetail");
            }, 5000);
          })
          .catch((error) => {
            this.exampleloading = false;
          });
      } else if (this.powerOffType == "2") {
        mainApi
          .vmsInstabcesDetachmentPoweroff(item.instanceId)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startPoweroffInstance"),
              type: "success",
              duration: 2500,
            });
            this.exampleloading = false;
            this.powerOffDialogVisible = false;
            setTimeout(() => {
              this.$emit("restartDetail");
            }, 5000);
          })
          .catch((error) => {
            this.exampleloading = false;
          });
      }
    },
    toReboot(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.$confirm(
        this.$t("message.confirmRebootInstanceWithName", { name: item.name }),
        this.$t("message.rebootInstance"),
        {
          confirmButtonText: this.$t("message.confirm"),
          cancelButtonText: this.$t("message.cancel"),
          type: "warning",
        }
      )
        .then(() => {
          mainApi
            .vmsInstabcesReboot(item.instanceId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startRebootInstance"),
                type: "success",
                duration: 2500,
              });
              setTimeout(() => {
                this.$emit("restartDetail");
              }, 5000);
            })
            .catch((error) => {});
        })
        .catch(() => {});
    },
    // 打开确认删除
    openDelete() {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      // 获取详情
      var id = this.propVmDetail.instanceId;
      mainApi
        .vmsInstabcesDetail(id)
        .then((res) => {
          this.deleteinfo = res;
          this.iseip = this.deleteinfo.networkDetailInfos.some(
            (info) => info.eipId
          );
          this.deleteDialogVisible = true;
        })
        .catch((error) => {});
    },
    // 关闭确认删除
    deleteHandleClose() {
      this.deleteDialogVisible = false;
    },
    // 确认删除云主机
    todelete(item) {
      this.exampleloading = true;
      mainApi
        .vmsInstabcesDel(item.instanceId)
        .then((res) => {
          this.$notify({
            title: this.$t("message.startDelete"),
            type: "success",
            duration: 2500,
          });
          this.deleteDialogVisible = false;
          setTimeout(() => {
            this.$emit("restartDetail");
            this.exampleloading = false;
          }, 5000);
        })
        .catch((error) => {
          this.exampleloading = false;
        });
    },
    // 绑定EIP
    openEip(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.dialogEipVisible = true;
      this.nowVmData = item;
      this.eipForm.name = "";
      this.eipForm.eipId = "";
      this.eipForm.page_num = 1;
      this.eipForm.page_size = 10;
      this.getEipList();
    },
    // 关闭EIP弹窗
    eipHandleClose(done) {
      done();
    },
    // 解绑EIP
    detachEip(item, eipId) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.$confirm(
        this.$t("message.confirmUnbindInstanceWithNameAndEIP", {
          name: item.name,
          eip: item.publicIp || item.eip,
        }),
        this.$t("message.unbind")
      ).then(() => {
        mainApi
          .eipsDetach(eipId)
          .then((res) => {
            this.$notify({
              title: this.$t("message.startUnbindEip"),
              type: "success",
              duration: 2500,
            });
            setTimeout(() => {
              this.$emit("restartDetail");
            }, 5000);
          })
          .catch((error) => {});
      });
    },
    // eip列表
    getEipList() {
      const params = {
        name: this.eipForm.name,
        page_num: this.eipForm.page_num,
        page_size: this.eipForm.page_size,
        bound_type: "unbound",
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
      mainApi.eipsList(params).then((res) => {
        this.eipTableData = res.eips;
        this.eipForm.total = res.totalNum;
      });
    },
    // eip改变每页显示数量
    eipHandleSizeChange(val) {
      this.eipForm.page_size = val;
      this.getEipList();
    },
    // eip改变页码
    eipHandleCurrentChange(val) {
      this.eipForm.page_num = val;
      this.getEipList();
    },
    // 选择eip
    eipHandleChange(val) {
      this.eipForm.eipId = val.eipId;
    },
    // 绑定EIP
    toEip() {
      this.eipVisibleloading = true;
      if (!this.eipForm.eipId) {
        this.$notify({
          title: this.$t("message.pleaseSelectEip"),
          type: "warning",
          duration: 2500,
        });
        this.eipVisibleloading = false;
        return;
      }
      mainApi
        .eipsAttach(this.eipForm.eipId, {
          portId: this.nowVmData.portInfo.portId,
        })
        .then((res) => {
          this.$notify({
            title: this.$t("message.startBindEip"),
            type: "success",
            duration: 2500,
          });
          this.dialogEipVisible = false;
          this.eipVisibleloading = false;
          this.$emit("initVmList");
        })
        .catch(() => {
          this.eipVisibleloading = false;
        });
    },
    // ------快照------
    // 快照弹窗
    openSnaps(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }
      this.nowVmData = item;
      this.dialogFormVisible = true;
    },
    // 创建快照
    toSnaps() {
      this.$refs.snapsformRef.validate(async (valid) => {
        if (valid) {
          this.snapLoading = true;
          this.snapsform.vmInstanceId = this.nowVmData.instanceId;
          mainApi
            .snapsAdd(this.snapsform)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startCreate"),
                type: "success",
                duration: 2500,
              });
              this.snapLoading = false;
              this.dialogFormVisible = false;
              this.$refs.snapsformRef.resetFields();
              setTimeout(() => {
                this.$emit("restartDetail");
              }, 5000);
            })
            .catch((error) => {
              this.snapLoading = false;
            });
        } else {
          this.snapLoading = false;
        }
      });
    },
    // 快照关闭
    snapsHandleClose(done) {
      this.$refs.snapsformRef.resetFields();
      done();
    },
    // -------变更规格-------
    // 变更规格弹窗
    openFlavor(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }

      if (![8, 63].includes(this.propVmDetail.phaseStatus)) {
        this.$notify({
          title: this.$t("message.instanceNotPoweroff"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      this.virtualmachine = item;
      mainApi.vmsInstabcesDetail(item.instanceId).then((res) => {
        this.getFlavorList(res);
        this.dialogFlavorVisible = true;
        this.nowVmData = res;
      });
    },
    flavorHandleClose(done) {
      done();
    },
    // 规格列表
    getFlavorList(vmDetail) {
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
          // 查找 id 为item.flavorId 的规格
          const flavor = res.flavors.find(
            (flavor) => flavor.flavorId === vmDetail.flavorId
          );
          var flavorData = res.flavors;

          // var flavorData = [];
          // if (flavor.gpuCount && flavor.gpuCount > 0) {
          //   flavorData = res.flavors.filter(
          //     (item) => item.gpuCount && item.gpuCount > 0
          //   );
          // } else {
          //   flavorData = res.flavors.filter(
          //     (item) => !item.gpuCount || item.gpuCount == 0
          //   );
          // }
          flavorData.forEach((item) => {
            if (item.flavorId == vmDetail.flavorId) {
              this.selectflavorId = item.flavorId;
              item.disabled = true;
            }
          });
          // 查找 id 为item.flavorId 的规格并置顶
          const index = flavorData.findIndex(
            (flavor) => flavor.flavorId === vmDetail.flavorId
          );
          if (index > -1) {
            const nowFlavor = flavorData.splice(index, 1);
            flavorData.unshift(nowFlavor[0]);
          }
          this.flavorsList = flavorData.filter(
            (item) => item.rootDisk == flavor.rootDisk
          );

          this.flavorsList.forEach((item) => {
            this.CpuList.push(item.cpu);
            this.MemList.push(item.mem);
            this.DiskList.push(item.rootDisk);
          });
          this.flavorCpu = Array.from(new Set(this.CpuList))
            .sort((a, b) => a - b)
            .map((item) => {
              return { text: item + this.$t("unit.cpu"), value: item };
            });
          this.flavorMem = Array.from(new Set(this.MemList))
            .sort((a, b) => a - b)
            .map((item) => {
              return { text: item + this.$t("unit.mem"), value: item };
            });
          this.flavorDisk = Array.from(new Set(this.DiskList))
            .sort((a, b) => a - b)
            .map((item) => {
              return { text: item + this.$t("unit.disk"), value: item };
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
    flavorsHandleCurrentChange(val) {
      if (val.disabled) {
        return;
      }
      if (!val.available) {
        return;
      }
      this.currentFlavorRow = val;
      this.flavorform.flavorId = val.flavorId;
    },
    flavorsTableRowClassName(row, rowIndex, item) {
      if (row.row.disabled) {
        return "success-row";
      }
      return "";
    },
    // 变更规格
    toFlavor() {
      // 变更规格add
      this.$refs.flavorformRef.validate(async (valid) => {
        if (valid) {
          this.flavorLoading = true;

          mainApi
            .vmsInstabcesEdit(this.flavorform, this.nowVmData.instanceId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startChange"),
                type: "success",
                duration: 2500,
              });
              this.flavorLoading = false;
              this.dialogFlavorVisible = false;
              this.$refs.flavorformRef.resetFields();
              setTimeout(() => {
                this.$emit("restartDetail");
              }, 5000);
            })
            .catch((error) => {
              this.flavorLoading = false;
            });
        } else {
          this.flavorLoading = false;
        }
      });
    },

    // ------镜像------
    openImages(item) {
      if (![8, 63].includes(this.propVmDetail.phaseStatus)) {
        this.$notify({
          title: this.$t("message.instanceNotPoweroff"),
          type: "error",
          duration: 2500,
        });
        return;
      }
      this.nowVmData = item;
      this.dialogExport = true;
    },
    exportHandleClose() {
      this.nowVmData = "";
      this.imagesForm.imageName = "";
      // this.imagesForm.imageOsType = 0;
      this.imagesForm.isPublic = false;
      this.dialogExport = false;
    },
    toExport() {
      // 导出
      // this.imagesForm
      this.$refs.imagesFormRef.validate(async (valid) => {
        if (valid) {
          mainApi
            .volumesExport(this.imagesForm, this.nowVmData.volumeId)
            .then((res) => {
              this.$notify({
                title: this.$t("message.startExport"),
                type: "success",
                duration: 2500,
              });
              this.exportHandleClose();
            })
            .catch((error) => {});
        }
      });
    },
    // -----迁移-----
    openTransfer(item) {
      if (this.$props.propShowType == 1) {
        this.$refs[`popover`].doClose();
      }

      mainApi
        .vmsInstabcesDetail(item.instanceId)
        .then((res) => {
          this.vmDetail = res;
          this.getvmsHypervisorNodesList();
          this.dialogTransfer = true;
          this.nowVmData = item;
        })
        .catch((error) => {});
    },
    transferHandleClose(done) {
      this.nowVmData = "";
      this.multipleSelection = "";
      this.dialogTransfer = false;
      this.vmDetail = "";
      done();
    },
    toTransfer() {
      // 迁移
      this.transloading = true;
      if (!this.multipleSelection) {
        this.$notify({
          title: this.$t("message.pleaseSelectComputeNode"),
          type: "warning",
          duration: 2500,
        });
        this.transloading = false;
        return;
      }
      this.$confirm(
        this.$t("message.confirmMigrate", {
          name: this.vmDetail.name,
          hypervisorNodeName: this.vmDetail.hypervisorNodeName,
          migrateNodeName: this.multipleSelection.name,
        })
      )
        .then(() => {
          this.transferloadingDialog = true;
          mainApi
            .vmsMigratet(
              { destNodeId: this.multipleSelection.nodeId },
              this.nowVmData.instanceId
            )
            .then((res) => {
              this.$notify({
                title: this.$t("message.startMigrate"),
                type: "success",
                duration: 2500,
              });
              this.transferloadingDialog = false;
              this.transloading = false;
              this.transferHandleClose();
              this.$emit("initVmList");
            })
            .catch((error) => {
              this.transferloadingDialog = false;
              this.transloading = false;
            });
        })
        .catch(() => {
          // catch error
        });
    },
    selectable(row) {
      return row.nodeId != this.vmDetail.hypervisorNodeId;
    },
    handleSelectionChange(val, row) {
      this.$refs.multipleTableRef.clearSelection();

      this.$refs.multipleTableRef.toggleRowSelection(row, undefined);
      this.multipleSelection = row;
    },
    getvmsHypervisorNodesList() {
      // 计算节点列表
      this.transferLoading = true;
      const params = {
        page_num: this.transferForm.page_num,
        page_size: this.transferForm.page_size,
        flavor_id: this.vmDetail.flavorId,
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
        .vmsHypervisorNodesListallocation(params)
        .then((res) => {
          this.transferLoading = false;
          this.transferTableData = res.nodeAllocationInfos;
          this.transferForm.total = res.totalNum;
        })
        .catch((error) => {
          this.transferLoading = false;
        });
    },
    transferHandleSizeChange(val) {
      // 改变每页显示数量
      this.transferForm.page_size = val;
      this.getvmsHypervisorNodesList();
    },
    transferHandleCurrentChange(val) {
      // 改变页码
      this.transferForm.page_num = val;
      this.getvmsHypervisorNodesList();
    },
  },
  // 在续费的时候监听选择的数据盘
  watch: {},
};
</script>

<style lang="scss" scoped>
.operatePage {
  position: relative;
  z-index: 999;
}

.operatePage {
  ::v-deep .el-table {
    .success-row {
      background-color: rgba(149, 212, 117, 0.3);
    }
  }
}

::v-deep .el-popconfirm__main {
  padding: 10px 0px;
}

::v-deep .el-dropdown-menu {
  font-size: 14px;
}

.ellipsis {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.italic {
  font-style: italic;
}

::v-deep .transfertable .has-gutter .el-checkbox__input {
  display: none;
}
</style>
