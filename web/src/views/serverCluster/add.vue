<template>
  <div>
    <!-- 第一步 集群基础信息 -->
    <!-- 集群信息 -->
    <el-card class="box-card mt-2 mb-2" shadow="never">
      <div slot="header" class="clearfix">
        <span>{{ $t("cluster.clusterInfo") }}</span>
      </div>
      <div class="text item">
        <!-- 集群信息 -->
        <el-form
          ref="clusterForm"
          :model="clusterForm"
          label-width="100px"
          :rules="rules"
        >
          <el-form-item :label="$t('form.name')+ ':'" prop="name">
            <el-input
              v-model="clusterForm.name"
              :placeholder="$t('message.pleaseInputNameDesc')"
              @blur="handleBlurChange"
            />
          </el-form-item>
          <el-form-item :label="$t('form.desc')+ ':'">
            <el-input
              v-model="clusterForm.desc"
              type="textarea"
              maxlength="255"
              show-word-limit
              :placeholder="$t('message.pleaseInput')"
            />
          </el-form-item>
        </el-form>
        <el-collapse v-model="tabsAndNotes">
          <el-collapse-item :title="$t('form.user')" name="1">
            <el-form ref="clusterForm" :model="clusterForm" label-width="150px">
              <el-form-item
                :label="$t('cluster.isOtherUserCreate')+ ':'"
                class="formContentBlock"
                label-width="180px"
              >
                <el-switch v-model="clusterIsUse" />
              </el-form-item>
              <el-form-item v-if="clusterIsUse" label-width="0px">
                <!-- <div> -->
                <el-row type="flex">
                  <el-form-item label-width="100px">
                    <div slot="label">{{ $t("form.bp") }}:</div>
                    <el-select
                      v-model="bpId"
                      filterable
                      :placeholder="$t('message.pleaseSelect')"
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
                  <el-form-item label-width="100px">
                    <div slot="label">{{ $t("form.user") }}:</div>
                    <el-select
                      v-model="userId"
                      filterable
                      style="width: 230px"
                      :placeholder="$t('message.pleaseSelect')"
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
                <!-- </div> -->
                <!-- <el-cascader v-model="clusterUser" :props="props"></el-cascader> -->
              </el-form-item>
            </el-form>
          </el-collapse-item>
          <el-collapse-item :title="$t('cluster.tagsAndNotes')" name="2">
            <el-row :gutter="40">
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-table
                    size="small"
                    :data="clusterForm.labels"
                    style="width: 100%; margin: 0 auto; max-width: 600px"
                  >
                    <el-table-column prop="key" :label="$t('form.key')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.key"
                          size="small"
                          :placeholder="$t('message.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="value" :label="$t('form.value')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.value"
                          size="small"
                          :placeholder="$t('message.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.operation')">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          @click="tabshandleClose(scope.$index)"
                        >{{ $t("form.delete") }}</el-button>
                      </template>
                    </el-table-column>
                  </el-table>

                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="small"
                    style="margin: 10px auto; display: block"
                    @click="tabsshowInput()"
                  >{{ $t("cluster.addTag") }}</el-button>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="grid-content bg-purple">
                  <el-table
                    size="small"
                    :data="clusterForm.annotations"
                    style="width: 100%; margin: 0 auto; max-width: 500px"
                  >
                    <el-table-column prop="key" :label="$t('form.key')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.key"
                          size="small"
                          :placeholder="$t('message.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column prop="value" :label="$t('form.value')">
                      <template slot-scope="scope">
                        <el-input
                          v-model="scope.row.value"
                          size="small"
                          :placeholder="$t('message.pleaseInput')"
                        />
                      </template>
                    </el-table-column>
                    <el-table-column :label="$t('form.operation')">
                      <template slot-scope="scope">
                        <el-button
                          type="text"
                          @click="noteshandleClose(scope.$index)"
                        >{{ $t("form.delete") }}</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="small"
                    style="margin: 10px auto; display: block"
                    @click="notesshowInput()"
                  >{{ $t("cluster.addNote") }}</el-button>
                </div>
              </el-col>
            </el-row>
          </el-collapse-item>
        </el-collapse>
        <el-collapse v-model="activeNames">
          <el-collapse-item :title="$t('cluster.nodeOptions')" name="1">
            <el-card class="box-card" shadow="never">
              <div slot="header" class="clearfix">
                <span>{{ $t("cluster.deploymentOptions") }}</span>
              </div>
              <div class="text item">
                <el-form
                  ref="deploymentForm"
                  :inline="true"
                  :model="deploymentForm"
                >
                  <el-row type="flex">
                    <el-form-item :label="$t('form.region')+ ':'" required>
                      <el-select
                        v-model="nowRegions"
                        filterable
                        :placeholder="$t('message.pleaseSelect')"
                        style="width: 230px"
                      >
                        <el-option
                          v-for="(item, index) in regionsData"
                          :key="index"
                          :label="item.name"
                          :value="item.id"
                        />
                      </el-select>
                    </el-form-item>
                    <el-form-item :label="$t('form.site')+ ':'" required>
                      <el-select
                        v-model="nowSite"
                        filterable
                        :placeholder="$t('message.pleaseSelect')"
                        style="width: 230px"
                      >
                        <el-option
                          v-for="(item, index) in siteData"
                          :key="index"
                          :label="item.name"
                          :value="item.id"
                        />
                      </el-select>
                    </el-form-item>
                  </el-row>
                </el-form>
              </div>
            </el-card>
            <el-card class="box-card" shadow="never">
              <div slot="header" class="clearfix">
                <el-button
                  type="primary"
                  size="mini"
                  style="float: right"
                  icon="el-icon-plus"
                  @click="addNode()"
                >
                  {{ $t("cluster.addNodeRoleAndConfiguration") }}
                </el-button>
              </div>
              <div class="text item">
                <!-- 节点编排计划 -->
                <el-collapse
                  v-model="nodeCollapse"
                  class="collapseStyle"
                  accordion
                >
                  <el-form
                    ref="clusterForm"
                    :inline="true"
                    :model="clusterForm"
                    label-width="150px"
                    :rules="rules"
                  >
                    <el-collapse-item
                      v-for="(item, index) in clusterForm.dev_needs"
                      :key="index"
                      class="nodeCollapse"
                      :name="index + 1"
                    >
                      <template slot="title">
                        <div style="width: 650px; float: left">
                          {{ $t("cluster.nodeRoleAndConfiguration") }}
                          {{ index + 1 }}
                          <small>(
                            <span>
                              {{ $t("cluster.nodeRole") }}:
                              <i v-for="(v, i) in item.cluster_roles" :key="i">
                                {{ i > 0 ? "," : "" }}{{ v }}
                              </i>
                              ;
                            </span>
                            <span>
                              {{ $t("cluster.number") }}:{{ item.num }};
                            </span>
                            <span>
                              {{
                                clusterDevSpec[index]
                                  ? $t("cluster.customNodeSpecification")
                                  : $t("cluster.defaultNodeSpecification")
                              }}
                            </span>
                            )</small>
                        </div>
                        <el-button
                          v-if="clusterForm.dev_needs.length > 1"
                          type="primary"
                          size="mini"
                          class="nodeDel"
                          @click.stop="delNode(index)"
                        >{{ $t("form.delete") }}</el-button>
                      </template>

                      <div class="formItem">
                        <el-form-item
                          :label="$t('cluster.nodeRole')+ ':'"
                          style="width: 100%"
                          label-width="200px"
                          :rules="[
                            {
                              required: true,
                              message: $t(
                                'cluster.pleaseSelectAtLeastOneNodeRole'
                              ),
                              trigger: 'change',
                            },
                          ]"
                        >
                          <el-checkbox-group
                            v-model="item.cluster_roles"
                            style="display: inline-block"
                            @change="handleRolesChange"
                          >
                            <el-checkbox label="etcd">etcd</el-checkbox>
                            <el-checkbox
                              label="controller"
                            >controller</el-checkbox>
                            <el-checkbox label="worker">worker</el-checkbox>
                          </el-checkbox-group>
                          <el-input-number
                            v-model="item.num"
                            controls-position="right"
                            size="medium"
                            :min="1"
                            style="
                              width: 230px;
                              display: inline-block;
                              margin-left: 15px;
                            "
                          />
                        </el-form-item>
                      </div>
                      <div class="formItem">
                        <el-form-item
                          :label="$t('cluster.customNodeSpecification')+ ':'"
                          style="width: 100%"
                          label-width="200px"
                        >
                          <el-switch v-model="clusterDevSpec[index]" />
                        </el-form-item>
                        <div v-if="clusterDevSpec[index]">
                          <div class="formSmallItem">
                            <div class="formSmallItemTitle">
                              {{ $t("cluster.cpuResourceDemand") }}
                            </div>
                            <el-form-item
                              :label="$t('cluster.number')+ ':'"
                              style="width: 100%"
                            >
                              <el-input-number
                                v-model="item.dev_spec.cpu.cpu_num"
                                controls-position="right"
                                :min="0"
                                style="width: 230px"
                              />
                            </el-form-item>
                            <el-form-item
                              :label="$t('cluster.model')+ ':'"
                              style="width: 100%"
                            >
                              <el-input
                                v-model="item.dev_spec.cpu.cpu_model"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                          </div>
                          <div class="formSmallItem">
                            <div class="formSmallItemTitle">
                              {{ $t("cluster.memoryResourceDemand") }}
                            </div>
                            <el-form-item
                              :label="$t('cluster.size')+ ':'"
                              style="width: 100%"
                            >
                              <el-input-number
                                v-model="item.dev_spec.mem.mem_limit"
                                controls-position="right"
                                :min="0"
                                style="width: 230px"
                              />
                            </el-form-item>
                          </div>
                          <div class="formSmallItem">
                            <div class="formSmallItemTitle">
                              {{ $t("cluster.diskResourceDemand") }}
                            </div>
                            <el-form-item
                              :label="$t('form.type')+ ':'"
                              style="width: 100%"
                            >
                              <el-select
                                v-model="item.dev_spec.disk.disk_type"
                                :placeholder="$t('message.pleaseSelect')"
                                style="width: 230px"
                              >
                                <el-option label="SSD" value="SSD" />
                                <el-option label="HDD" value="HDD" />
                              </el-select>
                            </el-form-item>
                            <el-form-item
                              :label="$t('cluster.size')+ ':'"
                              style="width: 100%"
                            >
                              <el-input-number
                                v-model="item.dev_spec.disk.disk_limit"
                                controls-position="right"
                                :min="0"
                                style="width: 230px"
                              />
                            </el-form-item>
                          </div>
                          <div class="formSmallItem">
                            <div class="formSmallItemTitle">
                              {{ $t("cluster.networkBandwidthResourceDemand") }}
                            </div>
                            <el-form-item
                              :label="$t('cluster.sendBandwidth')+ ':'"
                              style="width: 100%"
                            >
                              <el-input-number
                                v-model="
                                  item.dev_spec.network_band_need.transmit_band
                                "
                                :min="0"
                                controls-position="right"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                            <el-form-item
                              :label="$t('cluster.receiveBandwidth')+ ':'"
                              style="width: 100%"
                            >
                              <el-input-number
                                v-model="
                                  item.dev_spec.network_band_need.recv_band
                                "
                                :min="0"
                                controls-position="right"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                          </div>

                          <el-form-item label="GPU" style="width: 100%">
                            <el-radio-group v-model="clustergpu[index]">
                              <el-radio :label="true">
                                {{ $t("cluster.yes") }}
                              </el-radio>
                              <el-radio :label="false">
                                {{ $t("cluster.no") }}
                              </el-radio>
                            </el-radio-group>
                          </el-form-item>
                          <div v-if="clustergpu[index]">
                            <el-form-item
                              :label="$t('cluster.gpuType')+ ':'"
                              style="width: 100%"
                            >
                              <el-input
                                v-model="item.dev_spec.gpu.gpu_type"
                                type="text"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                            <el-form-item
                              :label="$t('cluster.gpuModel')+ ':'"
                              style="width: 100%"
                            >
                              <el-input
                                v-model="item.dev_spec.gpu.gpu_model"
                                type="text"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                            <el-form-item :label="$t('cluster.gpuCount')+ ':'">
                              <el-input-number
                                v-model="item.dev_spec.gpu.gpu_num"
                                :min="0"
                                controls-position="right"
                                style="width: 230px"
                              />
                              <span
                                class="tip"
                                style="
                                  line-height: 32px;
                                  color: #333;
                                  margin-left: 10px;
                                "
                              >
                                {{ $t("cluster.gpuCountTip") }}
                              </span>
                            </el-form-item>
                            <el-form-item
                              :label="$t('cluster.driverVersion')+ ':'"
                            >
                              <el-input
                                v-model="item.dev_spec.gpu.driver_version"
                                type="text"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                          </div>
                          <div
                            v-if="clustergpu[index]"
                            class="Dev_need_info_item"
                          >
                            <el-form-item label="CUDA:">
                              <el-input
                                v-model="item.dev_spec.gpu.cuda_version"
                                type="text"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                            <el-form-item label="cuDNN:">
                              <el-input
                                v-model="item.dev_spec.gpu.cudnn_version"
                                type="text"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>

                            <el-form-item
                              :label="$t('cluster.driverCapabilities')+ ':'"
                            >
                              <el-input
                                v-model="item.dev_spec.gpu.driver_capabilities"
                                type="text"
                                :placeholder="$t('message.pleaseInput')"
                                style="width: 230px"
                              />
                            </el-form-item>
                          </div>
                        </div>
                      </div>
                    </el-collapse-item>
                    <div
                      style="
                        background-color: #fafafa !important;
                        margin-bottom: 5px;
                        font-size: 14px;
                        font-weight: 600;
                        color: #303133;
                        width: 100%;
                        height: 48px;
                      "
                    >
                      <span style="line-height: 48px; margin-left: 15px">
                        {{ "Number of nodes required:" }}
                      </span>
                      <span
                        v-if="workerNum > 0"
                        style="
                          line-height: 48px;
                          float: right;
                          padding-right: 20px;
                          color: #27aa5e;
                        "
                      >
                        <span>worker:</span><span style="margin: 0px 5px">{{ "1 or more" }}</span>
                      </span>
                      <span
                        v-else
                        style="
                          line-height: 48px;
                          float: right;
                          padding-right: 20px;
                          color: #f15354;
                        "
                      >
                        <span>worker:</span><span style="margin: 0px 5px">{{ "1 or more" }}</span>
                      </span>
                      <span
                        v-if="controllerNum > 0"
                        style="
                          line-height: 48px;
                          float: right;
                          padding-right: 20px;
                          color: #27aa5e;
                        "
                      >
                        <span>controller:</span><span style="margin: 0px 5px">{{ "1 or more" }}</span>
                      </span>
                      <span
                        v-else
                        style="
                          line-height: 48px;
                          float: right;
                          padding-right: 20px;
                          color: #f15354;
                        "
                      >
                        <span>controller:</span><span style="margin: 0px 5px">{{ "1 or more" }}</span>
                      </span>
                      <span
                        v-if="etcdNum == 1 || etcdNum == 3 || etcdNum == 5"
                        style="
                          line-height: 48px;
                          float: right;
                          padding-right: 20px;
                          color: #27aa5e;
                        "
                      >
                        <span>etcd:</span><span style="margin: 0px 5px">{{ "1, 3, or 5" }}</span>
                      </span>
                      <span
                        v-else
                        style="
                          line-height: 48px;
                          float: right;
                          padding-right: 20px;
                          color: #f15354;
                        "
                      >
                        <span>etcd:</span><span style="margin: 0px 5px">{{ "1, 3, or 5" }}</span>
                      </span>
                    </div>
                  </el-form>
                </el-collapse>
              </div>
            </el-card>
          </el-collapse-item>
        </el-collapse>
      </div>
    </el-card>
    <!-- 集群选项 -->
    <info-form ref="infoRef" />
    <el-card class="box-card mt-2 mb-2" shadow="never">
      <div class="btnMain" style="text-align: center">
        <el-button size="small" @click="handelCancelClick">{{
          $t("form.cancel")
        }}</el-button>
        <el-button type="primary" size="small" @click="onSubmit">{{
          $t("form.submit")
        }}</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
var that = null
var yaml = require('js-yaml')
// var fs = require("fs");
import {
  getRegions,
  getSites,
  edges,
  getUsers,
  postClusters,
  getClusters,
  getBps
} from '@/api/mainApi'
import infoForm from './components/infoForm'
import validate from '@/utils/validate'
export default {
  components: {
    infoForm
  },
  data() {
    return {
      // 集群名称状态
      nameStatus: false,
      etcdNum: 0,
      controllerNum: 0,
      workerNum: 0,
      validate: validate,
      stepsActive: 1, // 步骤条
      clusterIsUse: false, // 是否为其他用户创建
      clusterUser: [], // 用户选择
      // clusterNode: [], //节点选择
      nowRegions: '', // 区域
      nowSite: '', // 站点
      regionsData: [], // 区域
      siteData: [], // 站点
      BpsData: [], // 组织机构
      userData: [], // 用户
      regionData: [],
      siteData: [],
      bpId: '', // 组织机构ID
      userId: '', // 用户ID
      clustergpu: [false], // 自定义gpu
      clusterDevSpec: [false], // 自定义节点规格
      activeNames: ['1'], // 折叠面板
      clusterForm: {
        // 集群信息
        name: '',
        desc: '',
        target_nodes: [
          {
            dst_region_id: '', // 目标区域id 取nowRegions
            dst_site_id: '' // 目标站点id 取nowSite
            // dst_node_id: "", //目标节点编号 暂不使用
          }
        ], // 部署的集群
        dev_needs: [
          {
            cluster_roles: [], // 节点配置 多选
            num: 1, // 角色配置 数量
            dev_spec: {
              cpu: {
                cpu_num: 0, // CPU资源需求 数量
                cpu_model: '' // CPU资源需求 型号
              },
              mem: {
                mem_limit: 0 // 内存资源需求 大小(MB)
              },
              disk: {
                disk_limit: 0, // 磁盘资源需求 大小(MB)
                disk_type: '' // 磁盘资源需求 类型
              },
              network_band_need: {
                transmit_band: '', // 网络带宽资源需求 发送带宽
                recv_band: '' // 网络带宽资源需求 接收带宽
              },
              gpu: {
                gpu_num: 0, // 显卡数量
                gpu_model: '', // 显卡型号
                driver_capabilities: '', // 驱动能力
                gpu_type: 'nvidia', // 显卡类型
                cuda_version: '', // CUDA
                cudnn_version: '', // cuDNN
                driver_version: '' // 驱动版本
              }
            }
          }
        ], // 集群设备数量需求
        create_type: 'custom', // custom:用户自定义 import：导入
        cluster_type: 'k8s', // k8s or k3s
        tmpl_ver_id: '', // 使用模板时的模板版本号
        jke_config: {
          k8s_version: ''
        }, // 集群参数配置,导入集群无此值
        owner: '', // 集群拥有者 取clusterUser[1]
        bp: '', // 拥有集群的bp 取clusterUser[0]
        labels: [], // 标签与注释 标签内容
        annotations: [] // 标签与注释 注释内容
      },
      tabsAndNotes: [''], // 标签和注释手风琴
      tabsinputVisible: false, // 标签与注释 标签内容
      tabsinputValue: '', // 标签与注释 标签内容
      notesinputVisible: false, // 标签与注释 注释内容
      notesinputValue: '', // 标签与注释 注释内容
      props: {
        // 用户选择
        lazy: true,
        lazyLoad(node, resolve) {
          const { level } = node
          const nodes = []
          if (level === 0) {
            getBps().then((res) => {
              if (res.bps != null && res.bps.length > 0) {
                res.bps.map((item) => {
                  nodes.push({
                    key: item.id,
                    value: item.name,
                    label: item.name,
                    leaf: level >= 1
                  })
                })
              }

              resolve(nodes)
            })
          } else {
            getUsers({ bp_id: node.key }).then((res) => {
              if (res.users != null && res.users.length > 0) {
                res.users.map((item) => {
                  nodes.push({
                    key: item.id,
                    value: item.name,
                    label: item.name,
                    leaf: level >= 1
                  })
                })
              }
              resolve(nodes)
            })
          }
        }
      },

      nodeCollapse: [1], // 节点手风琴
      initNodeForm: {
        cluster_roles: [], // 节点配置 多选
        num: 1, // 角色配置 数量
        dev_spec: {
          cpu: {
            cpu_num: 0, // CPU资源需求 数量
            cpu_model: '' // CPU资源需求 型号
          },
          mem: {
            mem_limit: 0 // 内存资源需求 大小(MB)
          },
          disk: {
            disk_limit: 0, // 磁盘资源需求 大小(MB)
            disk_type: '' // 磁盘资源需求 类型
          },
          network_band_need: {
            transmit_band: '', // 网络带宽资源需求 发送带宽
            recv_band: '' // 网络带宽资源需求 接收带宽
          },
          gpu: {
            gpu_num: 0, // 显卡数量
            gpu_model: '', // 显卡型号
            driver_capabilities: '', // 驱动能力
            gpu_type: 'nvidia', // 显卡类型
            cuda_version: '', // CUDA
            cudnn_version: '', // cuDNN
            driver_version: '' // 驱动版本
          }
        }
      },
      deploymentForm: {
        // 部署选项 表单
        node: true, // 目标节点
        node: true // 目标节点
      },
      rules: {
        name: [
          {
            required: true,
            validator: validate.k8sName,
            trigger: ['blur', 'change']
          }
        ]
      }
    }
  },
  watch: {
    nowRegions(res) {
      this.nowSite = ''
      this.websiteinit(res)
    }
  },
  mounted() {
    this.bpsinit()
    this.userinit()
    this.toRegions()
  },
  created() {
    that = this
  },
  methods: {
    // 角色选择触发
    handleRolesChange() {
      this.etcdNum = 0
      this.controllerNum = 0
      this.workerNum = 0
      for (var i = 0; i < this.clusterForm.dev_needs.length; i++) {
        if (this.clusterForm.dev_needs[i].cluster_roles.indexOf('etcd') != -1) {
          this.etcdNum++
        }
        if (
          this.clusterForm.dev_needs[i].cluster_roles.indexOf('controller') !=
          -1
        ) {
          this.controllerNum++
        }
        if (
          this.clusterForm.dev_needs[i].cluster_roles.indexOf('worker') != -1
        ) {
          this.workerNum++
        }
      }
    },
    async toRegions() {
      const list = await getRegions()
      this.regionsData = list.regions
      this.nowRegions = list.regions[0].id
    },
    async websiteinit(id) {
      const list = await getSites({
        region_id: id
      })
      this.siteData = []
      list.sites.forEach((res) => {
        this.siteData.push(...res.sites)
      })
      this.nowSite = this.siteData[0].id
    },
    // 选择组织机构，改变用户选择
    change_bp_id(value) {
      this.bpId = value
      this.userId = ''
      this.userinit()
    },
    // 用户改变触发
    change_user_id(value) {
      this.userId = value
    },
    clear() {
      this.userId = ''
    },
    async bpsinit() {
      const list = await getBps()
      this.BpsData = list.bps
    },
    async userinit() {
      const params = {}
      if (this.bpId != '' && this.bpId != undefined) {
        params.bp_id = this.bpId
        var list = await getUsers(params)
      } else {
        var list = await getUsers()
      }
      this.userData = list.users
    },
    // 取消增加操作调用
    handelCancelClick() {
      this.$router.push({
        path: '/serverCluster/list'
      })
    },
    tabshandleClose(index) {
      // 删除标签
      this.clusterForm.labels.splice(index, 1)
    },

    tabsshowInput() {
      // 新增标签
      this.$nextTick((_) => {
        this.clusterForm.labels.push({
          key: '',
          value: ''
        })
      })
    },

    noteshandleClose(index) {
      // 删除注释
      this.clusterForm.annotations.splice(index, 1)
    },

    notesshowInput() {
      // 新增注释
      this.$nextTick((_) => {
        this.clusterForm.annotations.push({
          key: '',
          value: ''
        })
      })
    },

    addNode() {
      this.nodeCollapse = []
      // 添加 节点编排计划
      this.clusterForm.dev_needs.push(
        JSON.parse(JSON.stringify(this.initNodeForm))
      )
      this.nodeCollapse.push(this.clusterForm.dev_needs.length)
      this.clusterDevSpec.push(false)
      this.clustergpu.push(false)
    },
    delNode(index) {
      // 删除 节点编排计划
      this.$confirm(this.$t('cluster.confirmDelete'), this.$t('message.tip'), {
        confirmButtonText: this.$t('form.confirm'),
        cancelButtonText: this.$t('form.cancel'),
        type: 'warning'
      })
        .then((_) => {
          this.clusterForm.dev_needs.splice(index, 1) // 删除当前内容
          this.clusterDevSpec.splice(index, 1) // 删除当前自定义节点规格
          this.clustergpu.splice(index, 1) // 删除当前自定义节点规格
          if (this.clusterForm.dev_needs.length < 1) {
            this.clusterForm.dev_needs.push(
              // 如果列表少于1条，则初始化1条默认数据
              JSON.parse(JSON.stringify(this.initNodeForm))
            )
            this.clusterDevSpec = [false]
            this.clustergpu = [false]
          }
          this.nodeCollapse = []
          this.handleRolesChange()
          done()
        })
        .catch((_) => {})
    },
    // 光标离开验证集群名称
    handleBlurChange() {
      var queryData = {
        name: this.clusterForm.name, // 集群名
        page_size: 99,
        page_num: 1
      }
      getClusters(queryData).then((res) => {
        if (res.total_num > 0) {
          var nameList = res.clusters.filter((item) => {
            return item.name == this.clusterForm.name
          })
          if (nameList.length > 0) {
            this.$notify({
              title: this.$t('cluster.clusterNameExists'),
              type: 'warning',
              duration: 2500
            })
            this.nameStatus = false
          } else {
            this.nameStatus = true
          }
        } else {
          this.nameStatus = true
        }
      })
    },
    onSubmit() {
      if (this.clusterForm.name == '') {
        this.$notify({
          title: this.$t('cluster.pleaseEnterClusterName'),
          type: 'error',
          duration: 2500
        })
        return
      }

      if (!this.nameStatus) {
        this.$notify({
          title: this.$t('cluster.clusterNameExists'),
          type: 'error',
          duration: 2500
        })
        return
      }
      this.$refs['clusterForm'].validate((valid) => {
        if (valid) {
          this.submitCluster()
        } else {
          this.$notify({
            title: this.$t('cluster.pleaseCompleteRequiredFields'),
            type: 'warning',
            duration: 2500
          })
          return false
        }
      })
    },

    submitCluster() {
      if (this.clusterForm.dev_needs[0].cluster_roles.length == 0) {
        this.$notify({
          title: this.$t('cluster.pleaseSelectAtLeastOneNodeRole'),
          type: 'error',
          duration: 2500
        })
        return
      }
      if (
        (this.etcdNum != 1 && this.etcdNum != 3 && this.etcdNum != 5) ||
        this.controllerNum <= 0 ||
        this.workerNum <= 0
      ) {
        this.$notify({
          title: this.$t('cluster.pleaseSelectNodeRole'),
          type: 'error',
          duration: 2500
        })
        return
      }
      if (this.$refs.infoRef.yamlLintState) {
        this.$notify({
          title: this.$t('cluster.yamlFormatError'),
          type: 'error',
          duration: 2500
        })
        return
      }
      if (this.nowRegions == '' || this.nowSite == '') {
        this.$notify({
          title: this.$t('cluster.pleaseSelectDeploymentRegionAndSite'),
          type: 'warning',
          duration: 2500
        })
        return
      } else {
        var jke_config = this.$refs.infoRef.jke_configMain
        this.clusterForm.jke_config.k8s_version = jke_config.k8s_version
      }

      // 提交信息
      var infoFrom = this.$refs.infoRef.infoFrom // 集群选项基本信息
      var configForm = this.$refs.infoRef.configForm
      var jke_config = this.$refs.infoRef.jke_configMain
      var yamlJson = this.$refs.infoRef.infoFrom.stack_compose // yaml转json
      var data = JSON.parse(JSON.stringify(this.clusterForm))
      data.dev_needs.map((item, index) => {
        item.cluster_roles = item.cluster_roles.toString()
        if (!this.clustergpu[index]) {
          delete item.dev_spec.gpu
        }
      })
      data.owner = this.userId // 集群拥有者 取clusterUser[1]
      data.bp = this.bpId // 拥有集群的bp 取clusterUser[0]

      // data.create_type = infoFrom.create_type; //custom:用户自定义 import：导入
      data.create_type = 'custom' // custom:用户自定义 import：导入
      data.cluster_type = 'k8s' // k8s or k3s
      data.target_nodes[0].dst_region_id = this.nowRegions
      data.target_nodes[0].dst_site_id = this.nowSite
      data.annotations = {}
      data.labels = {}

      if (
        this.clusterForm.annotations.length > 0 &&
        this.clusterForm.annotations[0].key != '' &&
        this.clusterForm.annotations[0].value != ''
      ) {
        this.clusterForm.annotations.forEach((item) => {
          if (item.key != '' && item.value != '') {
            data.annotations[item.key] = item.value
          }
        })
      }
      if (
        this.clusterForm.labels.length > 0 &&
        this.clusterForm.labels[0].key != '' &&
        this.clusterForm.labels[0].value != ''
      ) {
        this.clusterForm.labels.forEach((item) => {
          if (item.key != '' && item.value != '') {
            data.labels[item.key] = item.value
          }
        })
      }

      if (infoFrom.create_type == 'import') {
        // 如果是yaml导入

        data.jke_config = yaml.load(yamlJson)
      } else {
        data.jke_config = jke_config
      }

      if (
        data.jke_config &&
        data.jke_config.upgrade_strategy &&
        data.jke_config.upgrade_strategy.node_drain_input &&
        data.jke_config.upgrade_strategy.node_drain_input.grace_period
      ) {
        if (configForm.nodePodIntervalUnit == 'H') {
          data.jke_config.upgrade_strategy.node_drain_input.grace_period =
            data.jke_config.upgrade_strategy.node_drain_input.grace_period *
            3600
        }
        if (configForm.nodePodIntervalUnit == 'M') {
          data.jke_config.upgrade_strategy.node_drain_input.grace_period =
            data.jke_config.upgrade_strategy.node_drain_input.grace_period * 60
        }
      }
      if (
        data.jke_config &&
        data.jke_config.upgrade_strategy &&
        data.jke_config.upgrade_strategy.node_drain_input &&
        data.jke_config.upgrade_strategy.node_drain_input.timeout
      ) {
        if (configForm.nodeTimeoutIntervalUnit == 'H') {
          data.jke_config.upgrade_strategy.node_drain_input.timeout =
            data.jke_config.upgrade_strategy.node_drain_input.timeout * 3600
        }
        if (configForm.nodeTimeoutIntervalUnit == 'M') {
          data.jke_config.upgrade_strategy.node_drain_input.timeout =
            data.jke_config.upgrade_strategy.node_drain_input.timeout * 60
        }
      }
      if (data.jke_config && data.jke_config.deploy_job_time) {
        if (configForm.deployIntervalUnit == 'H') {
          data.jke_config.deploy_job_time =
            data.jke_config.deploy_job_time * 3600
        }
        if (configForm.deployIntervalUnit == 'M') {
          data.jke_config.deploy_job_time =
            data.jke_config.deploy_job_time * 60
        }
      }
      if (infoFrom.isUse) {
        // 是否使用模板
        data.tmpl_ver_id = infoFrom.version[1] // 模板 版本选择
        delete data.jke_config
      }

      postClusters(
        this.removePropertyOfNull(
          this.removePropertyOfNull(JSON.parse(JSON.stringify(data)))
        )
      ).then((res) => {
        if (res.id) {
          this.$notify({
            title: this.$t('message.addSuccess'),
            type: 'success',
            duration: 2500
          })
          this.$router.push({
            path: '/serverCluster/list'
          })
        }
      })
    },
    removePropertyOfNull(data) {
      if (typeof data === 'object' && data.constructor == Array) {
        data.forEach((item, index, arr) => {
          if (
            item === null ||
            (typeof item === 'object' &&
              item.constructor == Object &&
              Object.keys(item).length < 1) ||
            (typeof item === 'object' &&
              item.constructor == Array &&
              item.length < 1) ||
            item === ''
          ) {
            delete arr[index]
          } else {
            this.removePropertyOfNull(arr[index])
          }
        })
      }
      if (typeof data === 'object' && data.constructor == Object) {
        for (const key in data) {
          if (
            data[key] === null ||
            (typeof data[key] === 'object' &&
              data[key].constructor == Object &&
              Object.keys(data[key]).length < 1) ||
            (typeof data[key] === 'object' &&
              data[key].constructor == Array &&
              data[key].length < 1) ||
            data[key] === ''
          ) {
            delete data[key]
          } else {
            this.removePropertyOfNull(data[key])
          }
        }
      }
      return data
    },
    prevSteps() {
      // 上一步
      this.stepsActive--
      if (this.stepsActive < 1) {
        this.stepsActive = 1
      }
    },
    nextSteps() {
      this.clusterForm.jke_config.k8s_version = ''
      // 下一步
      if (this.stepsActive > 3) {
        this.stepsActive = 3
      }
      if (this.stepsActive == 2) {
        if (this.nowRegions == '' || this.nowSite == '') {
          this.$notify({
            title: this.$t('cluster.pleaseSelectDeploymentRegionAndSite'),
            type: 'warning',
            duration: 2500
          })
          return
        } else {
          var jke_config = this.$refs.infoRef.jke_configMain
          this.clusterForm.jke_config.k8s_version = jke_config.k8s_version
          this.stepsActive++
        }
      }
      if (this.stepsActive == 1) {
        this.$refs['clusterForm'].validate((valid) => {
          if (valid) {
            var queryData = {
              name: this.clusterForm.name, // 集群名
              page_size: 99,
              page_num: 1
            }
            getClusters(queryData).then((res) => {
              // 验证是否已有同名集群
              if (res.total_num == 0) {
                this.stepsActive++
              } else {
                var nameList = res.clusters.filter((item) => {
                  return item.name == this.clusterForm.name
                })
                if (nameList.length < 1) {
                  this.stepsActive++
                } else {
                  this.$notify({
                    title: this.$t('cluster.clusterNameExists'),
                    type: 'warning',
                    duration: 2500
                  })
                }
              }
            })

            // this.stepsActive++;
          } else {
            this.$notify({
              title: this.$t('cluster.pleaseCompleteRequiredFields'),
              type: 'warning',
              duration: 2500
            })
            return false
          }
        })
      }
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-collapse {
  border-top: 0px solid #ebeef5;
  border-bottom: 0px solid #ebeef5;
}

::v-deep .el-collapse-item__header {
  background-color: rgba(250, 250, 250, 1) !important;
  margin-bottom: 5px;
  padding-left: 10px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  width: 100%;
}

.nodeCollapse {
  .nodeDel {
    position: absolute;
    top: 10px;
    right: 10px;
  }

  ::v-deep .el-collapse-item__header {
    position: relative;
    padding-left: 20px;

    display: inline-block;

    .el-collapse-item__arrow {
      position: absolute;
      top: 36%;
      left: 4px;
    }
  }
}

::v-deep .table_head {
  font-weight: 600 !important;
}

.box-card {
  border: none;

  ::v-deep .el-card__header {
    padding: 10px 20px;
    font-weight: 600;
    border-bottom: 0px solid #ebeef5;
  }
}

::v-deep .el-collapse {
  border-bottom: none;

  ::v-deep .el-collapse-item__wrap {
    border-bottom: none;
  }

  &.collapseStyle {
    ::v-deep .el-collapse-item__arrow {
      margin: 0 8px 0 0;
    }
  }
}

.formItem {
  padding: 5px 20px;

  .formItemTitle {
    font-weight: bold;
    font-size: 16px;
  }

  .formSmallItem {
    padding: 10px 20px;
    width: 50%;
    float: left;
    height: 180px;
    min-width: 400px;
    max-width: 600px;

    .formSmallItemTitle {
      font-size: 14px;
      padding-bottom: 10px;
    }
  }
}
</style>
