<template>
  <div class="warpMain">
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      size="small"
      label-width="100px"
    >
      <div class="add_rqview p20">
        <el-form-item :label="$t('form.name') + ':'" prop="name">
          <el-input
            v-model="form.name"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.description") + ":" }}</div>
          <el-input
            v-model="form.description"
            type="textarea"
            maxlength="255"
            show-word-limit
            :placeholder="$t('form.pleaseInputDescription')"
            :rows="2"
          />
        </el-form-item>
        <div class="Dev_need_info" style="padding: 0">
          <el-row type="flex">
            <el-form-item
              :label="$t('form.region') + ':'"
              prop="target_nodes.dst_region_id"
              :rules="[
                { required: true, message: $t('message.pleaseSelectRegion') },
              ]"
            >
              <el-select
                v-model="form.target_nodes.dst_region_id"
                filterable
                style="width: 230px"
                :placeholder="$t('form.pleaseSelect')"
                @change="handleRegionChange"
              >
                <el-option
                  v-for="(item, index) in arealist"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label-width="90px">
              <div slot="label">{{ $t("form.site") + ":" }}</div>
              <el-select
                v-model="form.target_nodes.dst_site_id"
                filterable
                style="width: 230px"
                :placeholder="$t('form.pleaseSelect')"
                clearable
                @change="handleSiteChange"
              >
                <el-option
                  v-for="(item, index) in website"
                  :key="index"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label-width="90px">
              <div slot="label">{{ $t("form.node") + ":" }}</div>
              <el-select
                v-model="form.target_nodes.dst_node_id"
                filterable
                style="width: 230px"
                :placeholder="$t('form.pleaseSelect')"
                clearable
                @change="handleNodeChange"
              >
                <el-option
                  v-for="(item, index) in nodeList"
                  :key="index"
                  :label="item.node_name"
                  :value="item.node_id"
                />
              </el-select>
            </el-form-item>
          </el-row>
        </div>
        <el-row type="flex">
          <el-form-item v-if="cooperative">
            <div slot="label">{{ $t("form.bp") + ":" }}</div>
            <el-select
              v-model="form.bp_id"
              filterable
              :placeholder="$t('form.pleaseSelect')"
              style="width: 230px"
              clearable
              @clear="clear"
              @change="change_bp_id"
            >
              <el-option
                v-for="(item, index) in BpsData"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item v-if="userStatus" label-width="90px">
            <div slot="label">{{ $t("form.user") }}:</div>
            <el-select
              v-model="form.user_id"
              filterable
              style="width: 230px"
              :placeholder="$t('form.pleaseSelect')"
              clearable
              @change="change_user_id"
            >
              <el-option
                v-for="(item, index) in userData"
                :key="index"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-row>
        <el-form-item prop="registry_id" :label="$t('form.image') + ':'">
          <!-- <div slot="label">镜像:</div> -->
          <el-row type="flex">
            <el-select
              v-model="form.registry_id"
              filterable
              style="width: 230px"
              :placeholder="$t('form.pleaseSelect')"
              clearable
              @change="change_registry_id"
            >
              <el-option
                v-for="(item, index) in registriesData"
                :key="index"
                :label="item.registry_name"
                :value="item.registry_id"
              />
            </el-select>
            <el-form-item
              prop="inst_params.Image"
              :rules="[
                { required: true, message: $t('form.pleaseInputImage') },
              ]"
            >
              <el-input
                v-model="form.inst_params.Image"
                style="width: 500px; margin-left: 5px"
                :placeholder="$t('form.pleaseInputImage')"
              />
            </el-form-item>
            <div class="tip1" style="padding-left: 10px; flex: 1">
              {{ $t("form.registryTip") }}
            </div>
          </el-row>
        </el-form-item>
        <el-form-item
          :label="$t('form.imagePull') + ':'"
          label-width="90px"
          style="margin-top: -18px"
          prop="extra_params.image_pull_policy"
        >
          <el-select
            v-model="form.extra_params.image_pull_policy"
            style="width: 230px"
            :placeholder="$t('form.pleaseSelect')"
            @change="changePullpolicy"
          >
            <el-option :label="$t('form.alwaysPull')" value="Always" />
            <el-option :label="$t('form.ifNotPresent')" value="IfNotPresent" />
            <el-option :label="$t('form.neverPull')" value="Never" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.command") + ":" }}</div>
          <el-input
            v-model="form.inst_params.Cmd"
            :placeholder="$t('form.pleaseInputCommand')"
          />
        </el-form-item>
        <el-row type="flex">
          <el-form-item>
            <div slot="label">{{ $t("form.exclusiveMode") + ":" }}</div>
            <el-select
              v-model="form.on_one_node"
              style="width: 230px"
              :placeholder="$t('form.pleaseSelect')"
            >
              <el-option :label="$t('form.anyDeploy')" :value="0" />
              <el-option
                :label="$t('form.singleNodeMultiReplica')"
                :value="1"
              />
              <el-option :label="$t('form.dispersedDeploy')" :value="2" />
              <el-option :label="$t('form.nodeExclusive')" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item :label="$t('form.quantity') + ':'" prop="replica_num">
            <div style="width: 230px">
              <el-input-number
                v-model="form.replica_num"
                :min="0"
                :max="100"
                :placeholder="$t('form.pleaseInput')"
              />
            </div>
          </el-form-item>

          <el-form-item>
            <div slot="label">{{ $t("form.deployAfterStart") + ":" }}</div>
            <el-checkbox v-model="form.auto_run">
              {{ $t("form.deployAfterStart") }}
            </el-checkbox>
          </el-form-item>
        </el-row>
        <el-form-item>
          <div slot="label">{{ $t("form.restartPolicy") + ":" }}</div>
          <el-select
            v-model="form.restart_policy"
            style="width: 230px; float: left"
            :placeholder="$t('form.pleaseSelect')"
          >
            <el-option label="no" value="no" />
            <el-option label="on-failure" value="on-failure" />
            <el-option label="always" value="always" />
            <el-option label="unless-stopped" value="unless-stopped" />
          </el-select>
          <el-input
            v-if="form.restart_policy == 'on-failure'"
            v-model="restart_policy_num"
            style="width: 120px; margin-left: 5px"
            :placeholder="$t('form.maxRestartTimes')"
          />
          <el-tooltip
            v-if="form.restart_policy == 'on-failure'"
            class="item"
            effect="dark"
            :content="$t('form.nonNormalExitContainer')"
            placement="right-start"
          >
            <i class="el-icon-info" />
          </el-tooltip>
        </el-form-item>
        <el-collapse v-model="activeNames1">
          <el-collapse-item name="1">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.resourceSetting") }}
              </div>
            </template>

            <div class="Dev_need_info">
              <!-- 卷操作 -->
              <div class="juan" />
              <!-- 网络 -->
              <div>
                <el-form-item
                  :label="$t('form.network') + ':'"
                  label-width="90px"
                  class="h6 tal"
                >
                  <el-select
                    v-model="form.inst_params.HostConfig.NetworkMode"
                    :placeholder="$t('form.pleaseSelect')"
                    style="width: 230px"
                  >
                    <el-option label="bridge" value="bridge" />
                    <el-option label="host" value="host" />
                    <el-option label="none" value="none" />
                    <el-option label="container" value="container" />
                  </el-select>
                </el-form-item>
              </div>
              <!-- 端口映射 start -->
              <div class="add_rqview1 p20 pb35">
                <div class="label_title" size="small" type="primary">
                  {{ $t("form.portMapping") }}
                </div>

                <div class="label_ipconfig">
                  <div class="label_IP">{{ $t("form.publicHostIP") }}</div>
                  <div class="label_IP">{{ $t("form.publicHostPort") }}</div>
                  <div class="label_IP">
                    {{ $t("form.privateContainerPort") }}
                  </div>
                  <div class="label_IP">{{ $t("form.protocol") }}</div>
                </div>

                <div
                  v-for="(item, index) in hostConfig"
                  :key="index"
                  class="inter_address"
                  style="display: flex; align-items: center"
                >
                  <div class="del_labe">
                    <img
                      style="margin-bottom: 18px; margin-right: 10px"
                      src="../../assets/jdglicon/del_label.png"
                      alt=""
                      @click="delHostConfig(index)"
                    />
                  </div>
                  <div
                    class="add_inputs"
                    style="margin-bottom: 18px; margin-right: 10px"
                  >
                    <img
                      src="../../assets/jdglicon/addbtn.png"
                      alt=""
                      @click="toHostConfig()"
                    />
                  </div>
                  <div class="jg_input">
                    <el-form-item>
                      <el-input
                        v-model="item.ip"
                        :placeholder="$t('form.example') + ': 0.0.0.0'"
                      />
                    </el-form-item>
                  </div>
                  <div class="jg_lable">:</div>
                  <div class="jg_input">
                    <el-form-item>
                      <el-input
                        v-model="item.HostPort"
                        :placeholder="$t('form.example') + ': 8080'"
                      />
                    </el-form-item>
                  </div>
                  <div class="jg_lable">></div>
                  <div class="jg_input">
                    <el-form-item>
                      <el-input
                        v-model="item.HostPort1"
                        :placeholder="$t('form.example') + ': 8088'"
                      />
                    </el-form-item>
                  </div>
                  <div class="jg_lable">/</div>

                  <div class="jg_input">
                    <el-form-item>
                      <el-select
                        v-model="item.value"
                        :placeholder="$t('form.pleaseSelect')"
                        style="width: 100%"
                      >
                        <el-option label="TCP" value="tcp" />
                        <el-option label="UDP" value="udp" />
                      </el-select>
                    </el-form-item>
                  </div>
                </div>
              </div>
              <!-- 端口映射 End -->
              <!--环境变量 start -->
              <div class="add_rqview1 p20 pb35">
                <div class="label_title" size="small" type="primary">
                  {{ $t("form.environmentVariables") }}
                </div>

                <div class="label_ipconfig">
                  <div class="label_IP">{{ $t("form.variable") }}</div>
                  <div class="label_IP">{{ $t("form.value") }}</div>
                </div>

                <div
                  v-for="(item, index) in pathConfig"
                  :key="index"
                  class="inter_address"
                  style="display: flex; align-items: center"
                >
                  <div class="del_labe">
                    <img
                      style="margin-bottom: 18px; margin-right: 10px"
                      src="../../assets/jdglicon/del_label.png"
                      alt=""
                      @click="delPathConfig(index)"
                    />
                  </div>
                  <div
                    class="add_inputs"
                    style="margin-bottom: 18px; margin-right: 10px"
                  >
                    <img
                      src="../../assets/jdglicon/addbtn.png"
                      alt=""
                      @click="toPathConfig()"
                    />
                  </div>
                  <div class="jg_input">
                    <el-form-item>
                      <el-input
                        v-model="item.key"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                  </div>
                  <div class="jg_lable">=</div>
                  <div class="jg_input">
                    <el-form-item>
                      <el-input
                        v-model="item.value"
                        :placeholder="$t('form.pleaseInput')"
                      />
                    </el-form-item>
                  </div>
                </div>
              </div>
              <!--环境变量 end -->
              <div>
                <div
                  style="margin-bottom: 20px; padding: 0 20px"
                  class="label_title"
                >
                  {{ $t("form.containerResourceLimit") }}
                </div>
                <el-row type="flex">
                  <el-form-item :label="$t('form.cpu') + ':'">
                    <el-input-number
                      v-model="form.dev_need_info.cpu.cpu_num"
                      style="width: 200px"
                      :min="0"
                      :label="$t('form.quantity')"
                    />
                  </el-form-item>

                  <el-form-item>
                    <div slot="label">{{ $t("form.memory") + ":" }}(MB):</div>
                    <el-input-number
                      v-model="form.dev_need_info.mem.mem_limit"
                      style="width: 200px"
                      :min="0"
                      :label="$t('form.size')"
                    />
                  </el-form-item>

                  <el-form-item>
                    <div slot="label">{{ $t("form.disk") + ":" }}(MB):</div>
                    <el-input-number
                      v-model="form.dev_need_info.disk.disk_limit"
                      style="width: 200px"
                      :min="0"
                      :label="$t('form.size')"
                    />
                  </el-form-item>
                </el-row>
              </div>
              <el-row type="flex">
                <el-form-item
                  :label="$t('form.isPrivilegedMode') + ':'"
                  label-width="140px"
                  class="h6 tal formContentBlock"
                  style="margin-top: 1%"
                >
                  <el-radio-group
                    v-model="form.inst_params.HostConfig.Privileged"
                  >
                    <el-radio :label="true">{{ $t("form.yes") }}</el-radio>
                    <el-radio :label="false">{{ $t("form.no") }}</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-row>
              <el-row type="flex">
                <el-form-item
                  :label="$t('form.isNeedGPU') + ':'"
                  label-width="140px"
                  class="h6 tal formContentBlock"
                >
                  <el-radio-group v-model="gpuForm.status">
                    <el-radio :label="true">{{ $t("form.yes") }}</el-radio>
                    <el-radio :label="false">{{ $t("form.no") }}</el-radio>
                  </el-radio-group>
                </el-form-item>
                <div class="tip" style="line-height: 32px; color: #333">
                  {{ $t("form.isNeedGPUTip") }}
                </div>
              </el-row>
              <div v-if="gpuForm.status" class="Dev_need_info">
                <div class="Dev_need_info_item">
                  <el-form-item>
                    <div slot="label">{{ $t("form.gpuType2") + ":" }}</div>
                    <el-input
                      v-model="gpuForm.gpu_type"
                      type="text"
                      :placeholder="$t('form.pleaseInput')"
                    />
                  </el-form-item>
                  <el-form-item>
                    <div slot="label">{{ $t("form.gpuModel") + ":" }}</div>
                    <el-input
                      v-model="gpuForm.gpu_model"
                      type="text"
                      :placeholder="$t('form.pleaseInput')"
                    />
                  </el-form-item>
                  <el-form-item>
                    <div slot="label">{{ $t("form.gpuNumber") + ":" }}</div>
                    <el-input-number
                      v-model="gpuForm.gpu_num"
                      :min="0"
                      :label="$t('form.gpuNumber')"
                      style="width: 130px"
                    />
                    <span
                      class="tip"
                      style="line-height: 32px; color: #333; margin-left: 10px"
                    >
                      {{ $t("form.gpuNumberTip") }}
                    </span>
                  </el-form-item>
                  <el-form-item>
                    <div slot="label">{{ $t("form.driverVersion") + ":" }}</div>
                    <el-input
                      v-model="gpuForm.driver_version"
                      type="text"
                      :placeholder="$t('form.pleaseInput')"
                    />
                  </el-form-item>
                </div>
                <div class="Dev_need_info_item">
                  <el-form-item>
                    <div slot="label">CUDA:</div>
                    <el-input
                      v-model="gpuForm.cuda_version"
                      type="text"
                      :placeholder="$t('form.pleaseInput')"
                    />
                  </el-form-item>
                  <el-form-item>
                    <div slot="label">cuDNN:</div>
                    <el-input
                      v-model="gpuForm.cudnn_version"
                      type="text"
                      :placeholder="$t('form.pleaseInput')"
                    />
                  </el-form-item>
                  <el-form-item>
                    <div slot="label">{{ $t("form.driverCapability") }}:</div>
                    <el-input
                      v-model="gpuForm.driver_capabilities"
                      type="text"
                      :placeholder="$t('form.pleaseInput')"
                    />
                  </el-form-item>
                </div>
              </div>
            </div>
          </el-collapse-item>
          <el-collapse-item name="2">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.schedulingStrategy") }}
              </div>
            </template>
            <div class="Dev_need_info">
              <div class="tip1" style="padding-left: 0">
                {{ $t("form.schedulingStrategyTip") }}
              </div>
              <div class="label">
                <div style="margin: 10px 0">{{ $t("form.site") }}</div>
                <el-table
                  :data="siteData"
                  style="width: 100%"
                  size="small"
                  border
                >
                  <el-table-column :label="$t('form.type')" align="center">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.type" size="small">
                        <el-option label="Must" value="Must" />
                        <el-option label="Prefer" value="Prefer" />
                        <el-option label="MustNot" value="MustNot" />
                        <el-option label="PreferNot" value="PreferNot" />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.key')" align="center">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.key" size="small" />
                    </template>
                  </el-table-column>
                  <el-table-column
                    :label="$t('form.operatorWithV')"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.operator" size="small">
                        <el-option label="In" value="In" />
                        <el-option label="NotIn" value="NotIn" />
                        <el-option label="Exists" value="Exists" />
                        <el-option label="NotExists" value="NotExists" />
                        <el-option label="Gt" value="Gt" />
                        <el-option label="Lt" value="Lt" />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.value')" align="center">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.value" size="small" />
                    </template>
                  </el-table-column>

                  <el-table-column width="100" align="center">
                    <template slot="header" slot-scope="scope">
                      <el-button size="mini" @click="handleAddSite"
                        >+</el-button
                      >
                    </template>
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        @click="handlRemoveSite(scope.$index, scope.row)"
                        >-</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
                <div style="margin: 10px 0">{{ $t("form.node") }}</div>
                <el-table :data="nodeData" style="width: 100%" border>
                  <el-table-column :label="$t('form.type')" align="center">
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.type" size="small">
                        <el-option label="Must" value="Must" />
                        <el-option label="Prefer" value="Prefer" />
                        <el-option label="MustNot" value="MustNot" />
                        <el-option label="PreferNot" value="PreferNot" />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.key')" align="center">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.key" size="small" />
                    </template>
                  </el-table-column>
                  <el-table-column
                    :label="$t('form.operatorWithV')"
                    align="center"
                  >
                    <template slot-scope="scope">
                      <el-select v-model="scope.row.operator" size="small">
                        <el-option label="In" value="In" />
                        <el-option label="NotIn" value="NotIn" />
                        <el-option label="Exists" value="Exists" />
                        <el-option label="NotExists" value="NotExists" />
                        <el-option label="Gt" value="Gt" />
                        <el-option label="Lt" value="Lt" />
                      </el-select>
                    </template>
                  </el-table-column>
                  <el-table-column :label="$t('form.value')" align="center">
                    <template slot-scope="scope">
                      <el-input v-model="scope.row.value" size="small" />
                    </template>
                  </el-table-column>

                  <el-table-column width="100" align="center">
                    <template slot="header" slot-scope="scope">
                      <el-button size="mini" @click="handleAddNode"
                        >+</el-button
                      >
                    </template>
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        @click="handlRemoveNode(scope.$index, scope.row)"
                        >-</el-button
                      >
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </div>
          </el-collapse-item>
          <el-collapse-item name="3">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.failoverMigration") }}
              </div>
            </template>
            <div class="Dev_need_info">
              <el-form-item
                :label="$t('form.isNeedFailover') + ':'"
                class="formContentBlock"
              >
                <el-radio-group
                  v-model="form.failover_policy.need_failover"
                  :disabled="form.restart_policy != 'always'"
                >
                  <el-radio :label="true" :value="true">{{
                    $t("form.yes")
                  }}</el-radio>
                  <el-radio :label="false" :value="false">{{
                    $t("form.no")
                  }}</el-radio>
                </el-radio-group>
              </el-form-item>
              <el-row v-if="form.failover_policy.need_failover" type="flex">
                <el-form-item
                  prop="failover_policy.need_failover"
                  label-width="145px"
                >
                  <template slot="label">
                    <span style="line-height: 1; display: inline-flex">{{
                      $t("form.failoverMigrationDelay") + ":"
                    }}</span>
                  </template>
                  <div style="width: 230px">
                    <el-input-number
                      v-model="form.failover_policy.delays"
                      style="width: 130px"
                      :min="5"
                      :label="$t('form.failoverMigrationDelay')"
                    />
                    <span style="margin-left: 5px">{{
                      $t("form.unitWithMinutes")
                    }}</span>
                  </div>
                </el-form-item>
                <el-form-item
                  :label="$t('form.failoverRange') + ':'"
                  prop="failover_policy.failover_range"
                >
                  <el-select
                    v-model="form.failover_policy.failover_range"
                    style="width: 235px"
                    :placeholder="$t('form.pleaseSelect')"
                  >
                    <el-option
                      :label="$t('form.systemDetermined')"
                      value="default"
                    />
                    <el-option
                      :label="$t('form.regionMigration')"
                      value="region"
                    />
                    <el-option :label="$t('form.siteMigration')" value="site" />
                  </el-select>
                </el-form-item>
              </el-row>
            </div>
          </el-collapse-item>
          <el-collapse-item name="4">
            <template slot="title">
              <div
                style="padding-left: 10px; font-size: 14px; font-weight: bold"
              >
                {{ $t("form.storageConfig") }}
              </div>
            </template>
            <div class="Dev_need_info">
              <!-- 卷操作开始 -->
              <div class="juan">
                <div
                  v-for="(item, index) in Binds"
                  :key="index"
                  class="add_rqview3"
                  style="display: block; align-items: center"
                >
                  <div class="inter_address">
                    <div class="jg_input">
                      <el-form-item
                        :label="$t('form.volume') + ':'"
                        label-width="90px"
                        class="tal"
                      >
                        <el-input
                          v-model="item.value1"
                          style="width: 230px; margin-right: 5px"
                          :placeholder="$t('form.example') + ': /path/on/host'"
                        />
                        <span
                          style="
                            width: 30px;
                            line-height: 36px;
                            font-size: 14px;
                            font-family: Microsoft YaHei;
                            font-weight: bold;
                            color: #999999;
                            margin: 0px 5px 0px 2px;
                          "
                          >:</span
                        >
                        <el-input
                          v-model="item.value2"
                          style="width: 350px"
                          :placeholder="
                            $t('form.example') + ': /path/in/container'
                          "
                        />
                        <span style="margin-left: 10px"
                          ><el-checkbox v-model="item.checked">{{
                            $t("form.readOnly")
                          }}</el-checkbox></span
                        >
                      </el-form-item>
                    </div>
                  </div>
                  <div
                    class="del_labe"
                    style="
                      display: inline-block;
                      vertical-align: middle;
                      margin-left: 10px;
                    "
                  >
                    <img
                      src="../../assets/jdglicon/del_label.png"
                      alt=""
                      @click="delBinds(index)"
                    />
                  </div>
                </div>
              </div>
              <!-- 卷操作结束-->
              <!-- 配置文件开始 -->
              <div class="juan">
                <div
                  v-for="(item, index) in configureList"
                  :key="index"
                  class="add_rqview3"
                  style="display: block; align-items: center"
                >
                  <div class="inter_address">
                    <div class="jg_input">
                      <el-form-item
                        :label="$t('form.configureFile') + ':'"
                        label-width="90px"
                        class="tal"
                      >
                        <el-select
                          v-model="item.items"
                          :placeholder="$t('form.pleaseSelect')"
                          style="width: 230px; margin-right: 17px"
                          value-key="id"
                        >
                          <el-option
                            v-for="item in fileList"
                            :key="item.id"
                            :label="`${item.group} / ${item.dataId}`"
                            :value="item"
                          />
                        </el-select>
                        <el-input
                          v-model="item.path"
                          style="width: 350px"
                          :placeholder="
                            $t('form.pleaseInputContainerConfigureFilePath')
                          "
                        />
                        <span style="margin-left: 10px"
                          ><el-checkbox v-model="item.checked">{{
                            $t("form.readOnly")
                          }}</el-checkbox></span
                        >
                      </el-form-item>
                    </div>
                  </div>
                  <div
                    class="del_labe"
                    style="
                      display: inline-block;
                      vertical-align: middle;
                      margin-left: 10px;
                    "
                  >
                    <img
                      src="../../assets/jdglicon/del_label.png"
                      alt=""
                      @click="delConfigure(index)"
                    />
                  </div>
                </div>
                <div
                  v-if="configureList.length > 0"
                  style="margin-left: 90px; display: block; width: 100%"
                >
                  <span>{{ $t("form.pleaseSelect") }}</span
                  ><span
                    style="color: rgb(64, 158, 255); cursor: pointer"
                    @click="handleRouter"
                    >{{ $t("form.applicationConfigureCenter") }}</span
                  ><span>{{ $t("form.savedConfigureFile") }}.</span>
                </div>
              </div>
              <!-- 配置文件结束-->
            </div>
            <el-form-item label-width="90px" class="h6 tal">
              <el-dropdown>
                <el-button type="primary" style="width: 230px">
                  {{ $t("form.addVolume")
                  }}<i class="el-icon-arrow-down el-icon--right" />
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item>
                    <el-button
                      type="text"
                      style="width: 190px"
                      @click="addConfigure()"
                    >
                      {{ $t("form.mountConfigureFile") }}
                    </el-button></el-dropdown-item
                  >
                  <el-dropdown-item>
                    <el-button
                      type="text"
                      style="width: 190px"
                      @click="addBinds()"
                      >{{ $t("form.mountStorage") }}</el-button
                    ></el-dropdown-item
                  >
                </el-dropdown-menu>
              </el-dropdown>
            </el-form-item>
          </el-collapse-item>
        </el-collapse>
        <div class="add_rqview p20" />
        <!-- 端口映射 end -->
      </div>
      <el-form-item
        class="btn_rq"
        style="display: flex; justify-content: center; align-content: center"
        label-width="0px"
      >
        <el-button
          type="primary"
          size="small"
          :loading="loading"
          @click="onSubmit"
          >{{ $t("form.createImmediately") }}</el-button
        >
        <el-button size="small" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import TableTitles from "./components/TableTitles";
import {
  getSites,
  getRegions,
  addinstances,
  edges,
  getBps,
  getUsers,
  registries,
  registries3rd,
  configs,
} from "@/api/mainApi";
import { mapGetters } from "vuex";
export default {
  components: {
    TableTitles,
  },
  data() {
    return {
      // 合作
      cooperative: true,
      // 用户
      userStatus: true,
      activeNames1: "",
      arealist: [],
      website: [],
      nodeList: [],
      BpsData: [],
      userData: [],
      siteData: [],
      nodeData: [],
      registries3rd: [],
      registriesData: [],
      isAdd: true,
      loading: false,
      bpList: [],
      hostConfig: [
        {
          ip: "",
          HostPort: "",
          HostPort1: "",
          value: "tcp",
        },
      ],
      pathConfig: [
        {
          key: "",
          value: "",
        },
      ],
      Binds: [],
      // 存储配置-配置文件list
      configureList: [],
      // 配置文件列表
      fileList: [],
      gpuForm: {
        status: false,
        gpu_type: "nvidia",
        gpu_model: "",
        gpu_num: 0,
        driver_version: "",
        cuda_version: "",
        cudnn_version: "",
        visible_devices: "",
        driver_capabilities: "",
      },
      form: {
        name: "",
        description: "",
        registry_id: "",
        dev_need_info: {
          cpu: {
            cpu_num: "",
            cpu_model: "",
          },
          mem: {
            mem_limit: "",
          },
          disk: {
            disk_type: "",
            disk_limit: "",
          },
          gpu: {},
          network_band_need: {
            transmit_band: "",
            recv_band: "",
          },
        },
        on_one_node: 0,
        target_nodes: {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        },
        replica_num: 1,
        inst_params: {
          Image: "",
          Cmd: "",
          ExposedPorts: {},
          HostConfig: {
            PortBindings: {},
            Privileged: false,
            NetworkMode: "bridge",
          },
          Env: {
            MYSQL_ROOT_PASSWORD: "",
          },
        },
        scheduling_strategy: {
          label_selector_map: {},
        },
        auto_run: true, // 部署后拉起
        restart_policy: "no", // 重启策略
        failover_policy: {
          // 故障迁移
          need_failover: false, // 是否需要故障迁移
          delays: 10, // 延迟时间
          failover_range: "default",
        },
        bp_id: "",
        user_id: "",
        extra_params: { image_pull_policy: "IfNotPresent" },
      },
      restart_policy_num: "", // 重启策略 重启次数
      rules: {
        name: [
          {
            required: true,
            message: this.$t("form.pleaseInputName"),
            trigger: "blur",
          },
          {
            pattern: /^[a-zA-Z0-9\\-]+$/,
            message: this.$t("form.pleaseInputNameOnlyLetterOrNumberOrHyphen"),
            trigger: "blur",
          },
        ],
      },
      rq_control: [
        {
          name: this.$t("form.command"),
        },
        {
          name: this.$t("form.volume"),
        },
        {
          name: this.$t("form.network"),
        },
        {
          name: this.$t("form.securityHost"),
        },
        {
          name: this.$t("form.secretText"),
        },
        {
          name: this.$t("form.healthCheck"),
        },
        {
          name: this.$t("form.label"),
        },
        {
          name: this.$t("form.scheduling"),
        },
      ],
      rq_controlIndex: 3, // 默认选择操作
    };
  },
  watch: {
    "form.restart_policy": {
      deep: true,
      handler(val) {
        this.restart_policy_num = "";
      },
    },
    "form.restart_policy": {
      deep: true,
      handler(val) {
        this.form.failover_policy.need_failover = false;
      },
    },
  },
  created() {
    if (this.userInfo.kind == 4) {
      this.cooperative = false;
      this.userStatus = false;
      this.form.user_id = this.userInfo.id;
      this.form.bp_id = this.userInfo.bp_id;
    } else if (this.userInfo.kind == 2) {
      this.cooperative = false;
      this.userStatus = true;
      this.form.bp_id = this.userInfo.bp_id;
      this.userinit();
    } else {
      this.bpsinit();
      this.userinit();
      this.cooperative = true;
      this.userStatus = true;
      this.form.user_id = "";
      this.form.bp_id = "";
    }
  },
  computed: {
    ...mapGetters(["kind", "userInfo"]),
  },
  mounted() {
    this.areainit();
    this.getappConfigure();
  },

  methods: {
    // 跳转应用配置
    handleRouter() {
      this.$router.push("/containerApplicationService/appConfigure");
    },
    handleRegionChange(val) {
      if (val != "" && val != undefined) {
        this.form.target_nodes.dst_region_id = val;
        this.form.target_nodes.dst_site_id = "";
        this.form.target_nodes.dst_node_id = "";
        this.form.registry_id = "";
        // this.nodeList.length = 0;
        this.websiteinit();
        if (this.form.user_id != "" && this.form.user_id != undefined) {
          this.registries3rd_R();
          this.registries();
        } else {
          this.registries();
        }
      }
    },
    handleSiteChange(val) {
      if (val != "" && val != undefined) {
        (this.form.target_nodes.dst_site_id = val),
          (this.form.target_nodes.dst_node_id = "");
        this.edgesinit();
      } else {
        this.form.target_nodes.dst_node_id = "";
        this.nodeList = [];
      }
    },
    handleNodeChange(val) {
      this.form.target_nodes.dst_node_id = val;
    },
    addBinds() {
      this.Binds.push({ value1: "", value2: "", checked: false });
    },
    delBinds(index) {
      if (this.Binds.length > 0) {
        this.Binds.splice(index, 1);
      } else {
        this.Binds = [];
      }
    },
    addConfigure() {
      this.configureList.push({ items: "", path: "", checked: false });
    },
    delConfigure(index) {
      if (this.configureList.length > 0) {
        this.configureList.splice(index, 1);
      } else {
        this.configureList = [];
      }
    },
    getStrCount(scrstr, armstr) {
      // scrstr 源字符串 armstr 特殊字符
      var count = 0;
      while (scrstr.indexOf(armstr) != -1) {
        scrstr = scrstr.replace(armstr, "");
        count++;
      }
      return count;
    },
    findIndex(str, cha, num) {
      var x = str.indexOf(cha);
      for (var i = 0; i < num; i++) {
        x = str.indexOf(cha, x + 1);
      }
      return x;
    },
    async bpsinit() {
      const list = await getBps();
      this.BpsData = list.bps;
    },
    async userinit() {
      const params = {};
      if (this.form.bp_id != "" && this.form.bp_id != undefined) {
        params.bp_id = this.form.bp_id;
        var list = await getUsers(params);
      } else {
        var list = await getUsers();
      }
      this.form.registry_id = "";
      this.userData = list.users;
    },
    async areainit() {
      const list = await getRegions();
      this.arealist = list.regions;
      if (this.arealist != null && this.arealist.length > 0) {
        this.form.target_nodes.dst_region_id = this.arealist[0].id;
        this.websiteinit();
        this.registries();
      }
    },
    async registries() {
      this.registriesData = [];
      const params = {};
      params.region_id = this.form.target_nodes.dst_region_id;
      const list = await registries(params);
      this.registriesData = list.registries;
      if (
        this.form.user_id != "" &&
        this.form.user_id != undefined &&
        this.registries3rdData != undefined &&
        this.registries3rdData.length > 0
      ) {
        for (var i = 0; i < this.registries3rdData.length; i++) {
          this.registriesData.push(this.registries3rdData[i]);
        }
      }
    },
    async registries3rd_R() {
      // 第三方镜像仓库
      const params = {};
      params.user_id = this.form.user_id;
      this.registries3rdData = [];
      if (
        this.form.user_id != "" &&
        this.form.user_id != undefined &&
        this.form.target_nodes.dst_region_id != "" &&
        this.form.target_nodes.dst_region_id != undefined
      ) {
        var list = await registries3rd(params);
        this.registries3rdData = list.registries;
      } else if (this.form.user_id != "" && this.form.user_id != undefined) {
        var list = await registries3rd(params);
        this.registriesData = list.registries;
      } else {
        // var list = await registries3rd(params);
        // this.registriesData = list.registries;
      }
    },
    async websiteinit() {
      const list = await getSites({
        region_id: this.form.target_nodes.dst_region_id,
      });
      this.website = [];
      list.sites.forEach((res) => {
        this.website.push(...res.sites);
      });
      if (this.website != null && this.website.length > 0) {
        this.form.target_nodes.dst_site_id = this.website[0].id;
        this.edgesinit();
      }
    },
    async edgesinit() {
      // 节点
      const list = await edges({ site: this.form.target_nodes.dst_site_id });
      this.nodeList = list.nodes;
      if (this.nodeList != null && this.nodeList.length > 0) {
        this.form.target_nodes.dst_node_id = this.nodeList[0].node_id;
      }
    },
    // 配置文件数据
    async getappConfigure() {
      const list = await configs();
      this.fileList = list.pageItems;
    },
    onSubmit() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          this.loading = true;
          if (this.isAdd) {
            this.doAdd();
          } else this.doEdit();
        } else {
          this.$notify({
            title: this.$t("form.pleaseCompleteRequiredFields"),
            type: "error",
            duration: 2500,
          });
          return false;
        }
      });
    },
    resetForm() {
      this.gpuForm.status = false;
      this.form = {
        name: "",
        description: "",
        dev_need_info: {
          cpu: {
            cpu_num: "",
            cpu_model: "",
          },
          mem: {
            mem_limit: "",
          },
          disk: {
            disk_type: "",
            disk_limit: "",
          },
          gpu: {},
          network_band_need: {
            transmit_band: "",
            recv_band: "",
          },
        },
        on_one_node: 0,
        target_nodes: {
          dst_region_id: "",
          dst_site_id: "",
          dst_node_id: "",
        },
        replica_num: 1,
        inst_params: {
          Image: "",
          Cmd: "",
          HostConfig: {
            PortBindings: {},
            Privileged: false,
            NetworkMode: "bridge",
          },
          Env: {
            key: "",
          },
        },
        scheduling_strategy: {
          label_selector_map: {},
        },
        auto_run: true,
        restart_policy: "no", // 重启策略
        failover_policy: {
          // 故障迁移
          need_failover: false, // 是否需要故障迁移
          delays: 3, // 延迟时间
          failover_range: "default",
        },
        bp_id: "",
        extra_params: { image_pull_policy: "IfNotPresent" },
      };
      this.restart_policy_num = ""; // 重启策略 重启次数
      this.siteData = [];
      this.nodeData = [];
    },

    doAdd() {
      var formData = JSON.parse(JSON.stringify(this.form));
      if (this.gpuForm.status) {
        formData.dev_need_info.gpu = {
          gpu_type: this.gpuForm.gpu_type,
          gpu_model: this.gpuForm.gpu_model,
          gpu_num: this.gpuForm.gpu_num,
          driver_version: this.gpuForm.driver_version,
          cuda_version: this.gpuForm.cuda_version,
          cudnn_version: this.gpuForm.cudnn_version,
          visible_devices: this.gpuForm.visible_devices,
          driver_capabilities: this.gpuForm.driver_capabilities,
        };
      }
      for (var key in formData.dev_need_info) {
        for (var key1 in formData.dev_need_info[key]) {
          if (
            formData.dev_need_info[key][key1] === undefined ||
            formData.dev_need_info[key][key1] === "" ||
            JSON.stringify(formData.dev_need_info[key][key1]) === "{}"
          ) {
            delete formData.dev_need_info[key][key1];
          }
        }

        if (
          formData.dev_need_info[key] === undefined ||
          formData.dev_need_info[key] === "" ||
          JSON.stringify(formData.dev_need_info[key]) === "{}"
        ) {
          delete formData.dev_need_info[key];
        }
      }
      var target_nodes = {
        dst_region_id: "",
        dst_site_id: "",
        dst_node_id: "",
      };
      if (formData.target_nodes.dst_region_id != "") {
        target_nodes.dst_region_id = formData.target_nodes.dst_region_id;
      }
      if (formData.target_nodes.dst_site_id != "") {
        target_nodes.dst_site_id = formData.target_nodes.dst_site_id;
      }
      if (formData.target_nodes.dst_node_id != "") {
        target_nodes.dst_node_id = formData.target_nodes.dst_node_id;
      }
      formData.target_nodes = [];
      formData.target_nodes.push(target_nodes);

      if (
        formData.inst_params.Cmd != "" &&
        formData.inst_params.Cmd != undefined
      ) {
        if (formData.inst_params.Cmd.indexOf('"') != -1) {
          var cmdData = [];
          for (
            var i = 0;
            i < this.getStrCount(formData.inst_params.Cmd, '"');
            i++
          ) {
            if (i == 0) {
              cmdData.push(
                ...formData.inst_params.Cmd.substring(
                  0,
                  formData.inst_params.Cmd.indexOf('"') - 1
                ).split(" ")
              );
            } else {
              if (i % 2 == 0) {
                cmdData.push(
                  ...formData.inst_params.Cmd.substring(
                    this.findIndex(formData.inst_params.Cmd, '"', i - 1) + 1,
                    this.findIndex(formData.inst_params.Cmd, '"', i)
                  ).split(" ")
                );
              } else {
                cmdData.push(
                  formData.inst_params.Cmd.substring(
                    this.findIndex(formData.inst_params.Cmd, '"', i - 1) + 1,
                    this.findIndex(formData.inst_params.Cmd, '"', i)
                  )
                );
              }
            }
          }

          formData.inst_params.Cmd = cmdData.filter((s) => {
            return s && s.trim();
          });
        } else {
          formData.inst_params.Cmd = formData.inst_params.Cmd.split(" ").filter(
            (s) => {
              return s && s.trim();
            }
          );
        }
      }

      if (this.hostConfig.length > 0 && this.hostConfig != "") {
        this.hostConfig.forEach((item) => {
          if (item.HostPort == "" || item.HostPort1 == "") {
          } else {
            this.$set(
              formData.inst_params.HostConfig.PortBindings,
              item.HostPort1 + "/" + item.value,
              item.ip == ""
                ? [{ HostPort: item.HostPort }]
                : [{ HostIp: item.ip, HostPort: item.HostPort }]
            );
            this.$set(
              formData.inst_params.ExposedPorts,
              item.HostPort1 + "/" + item.value,
              {}
            );
          }
        });
      }

      if (this.pathConfig.length > 0 && this.pathConfig != "") {
        this.pathConfig.forEach((item, index) => {
          if (item.key == "" || item.value == "") {
          } else {
            formData.inst_params.Env[item.key] = item.value;
          }
        });
      }
      var bindlist = [];
      var configureList = [];
      for (var i = 0; i < this.Binds.length; i++) {
        if (
          this.Binds[i].value1 != "" &&
          this.Binds[i].value2 != "" &&
          this.Binds[i].value2 != undefined &&
          this.Binds[i].value2 != undefined
        ) {
          if (this.Binds[i].checked) {
            bindlist.push(
              this.Binds[i].value1 + ":" + this.Binds[i].value2 + ":ro"
            );
          } else {
            bindlist.push(this.Binds[i].value1 + ":" + this.Binds[i].value2);
          }
        }
      }
      for (var j = 0; j < this.configureList.length; j++) {
        if (
          this.configureList[j].items != "" &&
          this.configureList[j].items != undefined
        ) {
          if (this.configureList[j].checked) {
            configureList.push(
              "cfg://" +
                this.configureList[j].items.tenant +
                "/" +
                this.configureList[j].items.group +
                "/" +
                this.configureList[j].items.dataId +
                ":" +
                this.configureList[j].path +
                ":ro"
            );
          } else {
            configureList.push(
              "cfg://" +
                this.configureList[j].items.tenant +
                "/" +
                this.configureList[j].items.group +
                "/" +
                this.configureList[j].items.dataId +
                ":" +
                this.configureList[j].path
            );
          }
        }
      }
      formData.inst_params.HostConfig.Binds = bindlist.concat(configureList);
      for (var key in formData) {
        if (
          formData[key] === undefined ||
          formData[key] === "" ||
          JSON.stringify(formData[key]) === "[]" ||
          JSON.stringify(formData[key]) === "{}"
        ) {
          delete formData[key];
        }
      }
      for (var key in formData.inst_params.HostConfig) {
        if (
          formData.inst_params.HostConfig[key] === undefined ||
          formData.inst_params.HostConfig[key] === "" ||
          JSON.stringify(formData.inst_params.HostConfig[key]) === "[]" ||
          JSON.stringify(formData.inst_params.HostConfig[key]) === "{}"
        ) {
          delete formData.inst_params.HostConfig[key];
        }
      }
      for (var key in formData.inst_params.Env) {
        if (
          formData.inst_params.Env[key] === undefined ||
          formData.inst_params.Env[key] === "" ||
          JSON.stringify(formData.inst_params.Env[key]) === "[]" ||
          JSON.stringify(formData.inst_params.Env[key]) === "{}"
        ) {
          delete formData.inst_params.Env[key];
        }
      }
      for (var key in formData.inst_params) {
        if (
          formData.inst_params[key] === undefined ||
          formData.inst_params[key] === "" ||
          JSON.stringify(formData.inst_params[key]) === "[]" ||
          JSON.stringify(formData.inst_params[key]) === "{}"
        ) {
          delete formData.inst_params[key];
        }
      }
      formData.scheduling_strategy.label_selector_map = {
        node: this.nodeData,
        site: this.siteData,
      };
      if (
        formData.restart_policy == "on-failure" &&
        this.restart_policy_num != 0 &&
        this.restart_policy_num != undefined &&
        this.restart_policy_num != null &&
        this.restart_policy_num != ""
      ) {
        formData.restart_policy = "on-failure:" + this.restart_policy_num;
      }
      formData.failover_policy.delays =
        formData.failover_policy.delays * 1000 * 60;

      addinstances(formData)
        .then(() => {
          this.resetForm();
          this.loading = false;
          this.$router.push({
            path: "/containerApplicationService/containerDeploy",
          });
          this.$notify({
            title: this.$t("message.addSuccess"),
            type: "success",
            duration: 2500,
          });
        })
        .catch((err) => {
          this.loading = false;
          this.$notify({
            title: this.$t("message.addFailed"),
            type: "error",
            duration: 2500,
          });
        });
    },
    cancel() {
      this.$router.push({
        path: "/containerApplicationService/containerDeploy",
      });
    },
    changeIndex(index) {
      this.rq_controlIndex = index;
    },
    toHostConfig() {
      this.hostConfig.push({
        ip: "",
        HostPort: "",
        HostPort1: "",
        value: "tcp",
      });
    },
    delHostConfig(index) {
      if (this.hostConfig.length != 1) {
        this.hostConfig.splice(index, 1);
      } else {
        this.hostConfig = [
          {
            ip: "",
            HostPort: "",
            HostPort1: "",
            value: "tcp",
          },
        ];
      }
    },
    // 环境变量
    toPathConfig() {
      this.pathConfig.push({
        key: "",
        value: "",
      });
    },
    delPathConfig(index) {
      if (this.pathConfig.length != 1) {
        this.pathConfig.splice(index, 1);
      } else {
        this.pathConfig = [
          {
            key: "",
            value: "",
          },
        ];
      }
    },
    // 添加 站点
    handleAddSite() {
      this.siteData.push({ label_type: "node" });
    },
    handlRemoveSite(index) {
      this.siteData.splice(index, 1);
    },
    // 添加节点
    handleAddNode() {
      this.nodeData.push({ label_type: "site" });
    },
    handlRemoveNode(index) {
      this.nodeData.splice(index, 1);
    },
    // 选择组织机构，改变用户选择
    change_bp_id() {
      this.userinit();
      this.form.user_id = "";
      if (
        this.form.target_nodes.dst_region_id != "" &&
        this.form.target_nodes.dst_region_id != undefined
      ) {
        this.registries();
      }
    },
    clear() {
      this.form.user_id = "";
    },
    // 用户改变触发
    change_user_id(value) {
      this.form.user_id = value;
      this.form.registry_id = "";
      if (
        this.form.target_nodes.dst_region_id != "" &&
        this.form.target_nodes.dst_region_id != undefined
      ) {
        this.registries3rd_R();
        this.registries();
      } else {
        this.registries3rd_R();
      }
    },
    // 选择仓库
    change_registry_id(value) {
      this.form.registry_id = value;
    },
    // 选择镜像拉取策略
    changePullpolicy(value) {
      this.form.extra_params.image_pull_policy = value;
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-collapse-item__content {
  padding-bottom: 0px;
}

::v-deep .el-select-dropdown__item {
  width: 200px;
}

::v-deep .el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 18px;
}

::v-deep .label {
  .el-table td,
  .el-table th {
    padding: 5px 0 !important;
  }
}

::v-deep .el-table__empty-block {
  display: none;
}

.h6 {
  height: 60px !important;
}

.mb20 {
  margin-bottom: 20px;
}

.w35 {
  width: 35px;
}

.w100b {
  width: 100%;
}

.w22b {
  width: 22%;
}

.h8 {
  height: 80px !important;
}

.tal {
  text-align: left !important;
}

.p20 {
  padding: 0 20px;
}

.pt20 {
  padding-top: 20px;
}

.pb35 {
  padding-bottom: 35px;
}

.add_rqview {
  background: #ffffff;
  box-sizing: border-box;
  align-items: center;
}

.Dev_need_info {
  overflow: hidden;

  .Dev_need_info_item {
    width: 50%;
    float: left;
    height: 200px;
  }

  h6 {
    margin: 40px 0 10px;
  }

  .el-form-item {
    .el-input {
      width: 300px;
    }

    .el-select {
      width: 300px;
    }

    .el-input-number {
      width: 300px;
    }
  }

  .jg_lable {
    margin-bottom: 18px;
    text-align: center;
  }

  .jg_input {
    .el-input {
      width: 100%;
    }

    .el-select {
      width: 100%;
    }

    .el-input-number {
      width: 100%;
    }
  }
}

.add_rqview1 {
  background: #ffffff;
  box-sizing: border-box;
  position: relative;

  .abs_el {
    position: absolute;
    left: 51%;
    top: 0px;
  }

  .dkys {
    width: 140px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    background: #0f97ff;
    border-radius: 4px;
    font-size: 13px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #ffffff;
    cursor: pointer;
  }

  .inter_address {
    display: flex;
    height: 50px;
  }

  .jg_lable {
    width: 30px;
    line-height: 36px;
    font-size: 14px;
    font-family: Microsoft YaHei;
    font-weight: bold;
    color: #999999;
  }

  .jg_input {
    .el-form-item {
      ::v-deep .el-form-item__content {
        margin: 0 !important;
      }
    }

    width: 22%;
  }

  .label_ipconfig {
    margin: 30px 0px 18px 0;
    display: flex;
    padding-left: 34px;
  }

  .label_IP {
    width: 22%;
    padding-left: 50px;
    text-align: left;
    font-size: 13px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #333333;
    margin-right: 30px;
  }

  .del_labe {
    margin-right: 16px;
  }
}

.add_rqview3 {
  background: #ffffff;
  box-sizing: border-box;
  position: relative;
  padding-right: 20px;

  .abs_el {
    position: absolute;
    left: 51%;
    top: 0px;
  }

  .dkys {
    width: 140px;
    height: 36px;
    line-height: 36px;
    text-align: center;
    background: #0f97ff;
    border-radius: 4px;
    font-size: 13px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #ffffff;
  }

  .inter_address {
    display: inline-block;
    height: 50px;
  }

  .jg_input {
    width: 100%;
  }

  .label_ipconfig {
    display: flex;
  }

  .label_IP {
    width: 100%;
    padding-left: 40px;
    text-align: left;
    font-size: 13px;
    font-family: Microsoft YaHei;
    font-weight: 400;
    color: #333333;
    margin-right: 30px;
  }

  .del_labe {
    margin-right: 16px;
  }
}

.juan {
  ::v-deep {
    flex-direction: column;
  }
}

.add_rqContect {
  margin-top: 2px;
  background: #ffffff;
  padding: 40px 20px;

  .rq_contects {
    display: flex;
    margin-bottom: 30px;

    .rq_contect {
      border: none;
      padding: 14px 26px;
      height: 40px;
      box-sizing: border-box;
      line-height: 12px;
      background: #ffffff;
      box-shadow: 0px 1px 7px 0px rgba(204, 204, 204, 0.5);
      border-radius: 4px;
      font-size: 14px;
      font-family: Microsoft YaHei;
      font-weight: 400;
      color: #666666;
      margin-right: 14px;
      cursor: pointer;
    }

    .act_rq_contect {
      outline: none;
      background: #0f97ff;
      box-shadow: 0px 3px 7px 0px rgba(15, 151, 255, 0.5);
      color: #ffffff;
    }
  }
}

.tip {
  font-size: 12px;
  font-family: Microsoft YaHei;
  font-weight: 400;
  color: #999999;
  margin-left: 40px;
}

.add_inputs {
  font-size: 13px;
  font-family: Microsoft YaHei;
  font-weight: bold;
  color: #0f97ff;
  display: flex;
  align-items: center;

  img {
    margin-right: 5px;
  }
}

.label_title {
  font-size: 14px;
  color: #606266;
  line-height: 40px;
  padding: 0 12px 0 0;
  font-weight: 700;
}

.btn_rq {
  margin-top: 20px !important;
  text-align: left;
}

.tip1 {
  color: #fbb561;
  text-align: left;
  padding-left: 80px;
  font-size: 12px;
}
</style>
