<template>
  <div class="relative">
    <div class="mb-2 mt-4">
      <el-steps
        :active="active"
        :align-center="true"
        finish-status="success"
        simple
        :space="400"
      >
        <el-step :title="$t('form.basicInfo')" />
        <el-step :title="$t('form.confirmConfig')" />
      </el-steps>
    </div>
    <div style="height: calc(100vh - 275px); overflow-y: auto">
      <div v-if="active == 0">
        <el-card class="mb-2" shadow="never">
          <div class="hFpJSC">{{ $t("form.basicInfo") }}</div>
          <el-form
            ref="form"
            :model="form"
            :rules="rules"
            size="small"
            :inline="true"
            label-position="right"
            label-width="120px"
          >
            <el-form-item :label="$t('form.nodeName')+ ':'" prop="node_name">
              <el-input
                v-model="form.node_name"
                :placeholder="$t('form.pleaseInput')"
                style="width: 230px"
              />
            </el-form-item>
            <el-form-item :label="$t('form.belongRegion')+ ':'" prop="region_id">
              <el-select
                v-model="form.region_id"
                filterable
                :placeholder="$t('form.pleaseSelect')"
                style="width: 230px"
                @change="changeRegion"
              >
                <el-option
                  v-for="item in regions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('form.belongSite')+ ':'" prop="site_id">
              <el-select
                v-model="form.site_id"
                :placeholder="$t('form.pleaseSelect')"
                filterable
                clearable
                style="width: 230px"
                @change="changeSites"
              >
                <el-option
                  v-for="item in sites"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('form.vendor')+ ':'">
              <el-input
                v-model="form.vendor"
                :placeholder="$t('form.pleaseInput')"
                style="width: 230px"
              />
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="mb-2" shadow="never">
          <div class="hFpJSC">{{ $t("form.advancedOptions") }}</div>
          <el-form
            size="small"
            :inline="true"
            label-position="right"
            label-width="120px"
          >
            <el-form-item
              v-for="(item, index) in labelData"
              :key="index"
              :label="item.show_name"
            >
              <template slot="label">
                {{ item.show_name }}
                <el-tooltip
                  v-show="item.description"
                  class="item"
                  effect="dark"
                  :content="item.description"
                  placement="top-start"
                >
                  <i class="el-icon-info" />
                </el-tooltip>
              </template>
              <div
                v-if="item.type === 'bool'"
                style="width: 230px; display: inline-block"
              >
                <el-radio-group
                  v-model="item.default_value"
                  :disabled="!item.immutable"
                  size="small"
                >
                  <el-radio :label="true"> {{ $t("form.yes") }}</el-radio>
                  <el-radio :label="false"> {{ $t("form.no") }}</el-radio>
                </el-radio-group>
              </div>
              <div
                v-if="
                  item.type === 'string' &&
                    item.key.indexOf('bp') == -1 &&
                    item.key.indexOf('owner') == -1
                "
                style="width: 230px; display: inline-block"
              >
                <el-input
                  v-model="item.default_value"
                  style="width: 230px"
                  size="small"
                  :placeholder="$t('form.pleaseInput')"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === 'enum'">
                <el-select
                  v-model="item.default_value"
                  filterable
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 230px"
                  clearable
                  :disabled="!item.immutable"
                >
                  <el-option
                    v-for="items in item.options"
                    :key="items.index"
                    :value="items"
                  />
                </el-select>
              </div>
              <div v-if="item.type === 'float'">
                <el-input-number
                  v-model="item.default_value"
                  style="width: 230px"
                  size="small"
                  :precision="2"
                  :step="0.1"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === 'int'">
                <el-input-number
                  v-model="item.default_value"
                  style="width: 230px"
                  size="small"
                  :min="0"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === '未定义'">
                <el-input
                  id="uncolor"
                  v-model="item.default_value"
                  style="width: 260px; color: #67c23a !important"
                  size="small"
                  :placeholder="$t('form.pleaseInput')"
                  :disabled="!item.immutable"
                />
              </div>
              <div
                v-if="item.type === 'string' && item.key.indexOf('bp') != -1"
              >
                <el-select
                  v-model="item.default_value"
                  filterable
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 230px"
                  clearable
                  :disabled="!item.immutable"
                  @change="labelbpChange"
                >
                  <el-option
                    v-for="items in labelBpsData"
                    :key="items.id"
                    :label="items.name"
                    :value="items.id"
                  />
                </el-select>
              </div>
              <div
                v-if="item.type === 'string' && item.key.indexOf('owner') != -1"
              >
                <el-select
                  v-model="item.default_value"
                  filterable
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 230px"
                  clearable
                  :disabled="!item.immutable"
                  @change="labelUseChange"
                >
                  <el-option
                    v-for="items in labelUserData"
                    :key="items.id"
                    :label="items.name"
                    :value="items.id"
                  />
                </el-select>
              </div>
            </el-form-item>
            <el-form-item
              v-for="(item, index) in stainData"
              :key="index"
              :label="item.show_name"
            >
              <template slot="label">
                {{ item.show_name }}
                <el-tooltip
                  v-show="item.description"
                  class="item"
                  effect="dark"
                  :content="item.description"
                  placement="top-start"
                >
                  <i class="el-icon-info" />
                </el-tooltip>
              </template>
              <div
                v-if="item.type === 'bool'"
                style="width: 230px; display: inline-block"
              >
                <el-radio-group
                  v-model="item.default_value"
                  :disabled="!item.immutable"
                  size="small"
                >
                  <el-radio :label="true"> {{ $t("form.yes") }}</el-radio>
                  <el-radio :label="false"> {{ $t("form.no") }}</el-radio>
                </el-radio-group>
              </div>
              <div
                v-if="
                  item.type === 'string' &&
                    item.key.indexOf('bp') == -1 &&
                    item.key.indexOf('owner') == -1
                "
                style="width: 230px; display: inline-block"
              >
                <el-input
                  v-model="item.default_value"
                  style="width: 230px"
                  size="small"
                  :placeholder="$t('form.pleaseInput')"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === 'enum'">
                <el-select
                  v-model="item.default_value"
                  filterable
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 230px"
                  clearable
                  :disabled="!item.immutable"
                >
                  <el-option
                    v-for="items in item.options"
                    :key="items.index"
                    :value="items"
                  />
                </el-select>
              </div>
              <div v-if="item.type === 'float'">
                <el-input-number
                  v-model="item.default_value"
                  style="width: 230px"
                  size="small"
                  :precision="2"
                  :step="0.1"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === 'int'">
                <el-input-number
                  v-model="item.default_value"
                  style="width: 230px"
                  size="small"
                  :min="0"
                  :disabled="!item.immutable"
                />
              </div>
              <div v-if="item.type === '未定义'">
                <el-input
                  id="uncolor"
                  v-model="item.default_value"
                  style="width: 260px; color: #67c23a !important"
                  size="small"
                  :placeholder="$t('form.pleaseInput')"
                  :disabled="!item.immutable"
                />
              </div>
              <div
                v-if="item.type === 'string' && item.key.indexOf('bp') != -1"
              >
                <el-select
                  v-model="item.default_value"
                  filterable
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 230px"
                  clearable
                  :disabled="!item.immutable"
                  @change="stainbpChange"
                >
                  <el-option
                    v-for="items in labelBpsData"
                    :key="items.id"
                    :label="items.name"
                    :value="items.id"
                  />
                </el-select>
              </div>
              <div
                v-if="item.type === 'string' && item.key.indexOf('owner') != -1"
              >
                <el-select
                  v-model="item.default_value"
                  filterable
                  :placeholder="$t('form.pleaseSelect')"
                  style="width: 230px"
                  clearable
                  :disabled="!item.immutable"
                  @change="stainUseChange"
                >
                  <el-option
                    v-for="items in stainUserData"
                    :key="items.id"
                    :label="items.name"
                    :value="items.id"
                  />
                </el-select>
              </div>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="mb-2" shadow="never">
          <div class="hFpJSC">{{ $t("form.monitorComponent") }}</div>
          <el-form
            :model="form"
            :rules="rules"
            size="small"
            :inline="true"
            label-position="right"
            label-width="120px"
          >
            <el-form-item :label="$t('form.isEnable')+ ':'">
              <el-switch v-model="switchMonitor" />
            </el-form-item>
          </el-form>
        </el-card>
      </div>
      <div
        v-if="active == 1"
        style="height: calc(100vh - 275px); overflow-y: auto"
      >
        <el-card class="mb-2 pb-6" shadow="never">
          <div class="hFpJSC">{{ $t("form.basicInfo") }}</div>
          <el-form size="small" label-width="120px">
            <el-form-item
              :label="$t('form.nodeName')+ ':'"
              style="width: 50%; float: left"
            >
              {{ form.node_name }}
            </el-form-item>
            <el-form-item
              :label="$t('form.belongRegion')+ ':'"
              style="width: 50%; float: left"
            >
              {{ region_name ? region_name : "-" }}
            </el-form-item>
            <el-form-item
              :label="$t('form.belongSite')+ ':'"
              style="width: 50%; float: left"
            >
              {{ site_name ? site_name : "-" }}
            </el-form-item>
            <el-form-item
              :label="$t('form.vendor')+ ':'"
              style="width: 50%; float: left"
            >
              {{ form.vendor ? form.vendor : "-" }}
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="mb-2 pb-6" shadow="never">
          <div class="hFpJSC">{{ $t("form.advancedOptions") }}</div>
          <el-form size="small" label-width="120px" class="mb-4">
            <el-form-item
              v-for="(item, index) in labelData"
              :key="index"
              :label="item.show_name"
              style="width: 50%; float: left"
            >
              <template slot="label">
                {{ item.show_name }}
                <el-tooltip
                  v-show="item.description"
                  class="item"
                  effect="dark"
                  :content="item.description"
                  placement="top-start"
                >
                  <i class="el-icon-info" />
                </el-tooltip>
              </template>
              <span
                v-if="item.type === 'string' && item.key.indexOf('bp') != -1"
              >{{ labelBpName ? labelBpName : "-" }}</span>
              <span
                v-else-if="
                  item.type === 'string' && item.key.indexOf('owner') != -1
                "
              >{{ labelUserName ? labelUserName : "-" }}</span>
              <span v-else>{{
                item.default_value ? item.default_value : "-"
              }}</span>
            </el-form-item>
            <el-form-item
              v-for="(item, index) in stainData"
              :key="index"
              :label="item.show_name"
            >
              <template slot="label">
                {{ item.show_name }}
                <el-tooltip
                  v-show="item.description"
                  class="item"
                  effect="dark"
                  :content="item.description"
                  placement="top-start"
                >
                  <i class="el-icon-info" />
                </el-tooltip>
              </template>
              <span
                v-if="item.type === 'string' && item.key.indexOf('bp') != -1"
              >{{ stainBpName ? stainBpName : "-" }}</span>
              <span
                v-else-if="
                  item.type === 'string' && item.key.indexOf('owner') != -1
                "
              >{{ stainUserName ? stainUserName : "-" }}</span>
              <span v-else>{{
                item.default_value ? item.default_value : "-"
              }}</span>
            </el-form-item>
          </el-form>
        </el-card>
        <el-card class="mb-2 pb-6" shadow="never">
          <div class="hFpJSC">{{ $t("form.monitorComponent") }}</div>
          <el-form size="small" label-width="120px">
            <el-form-item
              :label="$t('form.isEnable')+ ':'"
              style="width: 50%; float: left"
            >
              {{ switchMonitor ? $t("form.yes") : $t("form.no") }}
            </el-form-item>
          </el-form>
        </el-card>
      </div>
    </div>
    <div class="absolute z-30 py-2" style="width: 100%">
      <el-card class="box-card" shadow="never" style="text-align: right">
        <el-button
          v-if="active == 0"
          size="small"
          type="primary"
          @click="next('form')"
        >{{ $t("form.nextStep") }}</el-button>
        <el-button
          v-if="active != 0"
          size="small"
          type="primary"
          @click="prev()"
        >{{ $t("form.prevStep") }}</el-button>

        <el-button
          v-if="active != 0"
          :loading="loading"
          size="small"
          type="primary"
          @click="doSubmit"
        >{{ $t("form.confirm") }}</el-button>
        <el-button size="small" @click="cancel">{{
          $t("form.cancel")
        }}</el-button>
      </el-card>
    </div>
  </div>
</template>

<script>
import {
  addEdges,
  getRegions,
  getSites,
  getLabelOption,
  getTaintOptions,
  getBps,
  getUsers
} from '@/api/mainApi'
import { mapGetters } from 'vuex'
import Clipboard from 'clipboard'
export default {
  props: {
    sup_this: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      // 选择的组织机构ID
      labelbp_id: '',
      stainbp_id: '',
      regions: [],
      sites: [],
      loading: false,
      installNodes: false,
      cmdModal: false,
      cmdInfo: null,
      labelData: [],
      stainData: [],
      radio: false,
      form: {
        region_id: '',
        site_id: '',
        node_name: '',
        taints: {},
        labels: {},
        // 供应商
        vendor: ''
      },
      switchMonitor: false,
      // 组织机构数据
      labelBpsData: [],
      labelUserData: [],
      // 组织机构数据
      stainUserData: [],
      rules: {
        region_id: [
          {
            required: true,
            message: this.$t('form.pleaseSelectRegion'),
            trigger: 'change'
          }
        ],
        site_id: [
          {
            required: true,
            message: this.$t('form.pleaseSelectSite'),
            trigger: 'change'
          }
        ],
        node_name: [
          {
            required: true,
            message: this.$t('form.pleaseInputNodeName'),
            trigger: 'blur'
          },
          {
            pattern: /^[a-zA-Z0-9][a-zA-Z0-9-_@]{0,64}$/,
            message: this.$t('form.pleaseInputNodeNameTips'),
            trigger: 'blur'
          }
        ]
      },
      // 步骤条步数
      active: 0,
      // 标签组织机构名称
      labelBpName: '',
      // 标签用户名称
      labelUserName: '',
      // 污点组织机构名称
      stainBpName: '',
      // 污点用户名称
      stainUserName: ''
    }
  },
  watch: {},

  computed: {
    ...mapGetters(['userInfo'])
  },
  created() {
    this.onOpen()
  },
  mounted() {},
  methods: {
    handleClose() {
      this.installNodes = false
      this.cmdModal = false
      this.cmdInfo = null
    },
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text })
      clipboard.on('success', () => {
        this.$notify({
          title: this.$t('message.copySuccess'),
          type: 'success',
          duration: 2500
        })
        // 释放内存
        clipboard.off('error')
        clipboard.off('success')
        clipboard.destroy()
      })
      clipboard.on('error', () => {
        // 不支持复制
        this.$notify({
          title: this.$t('message.copyFailed'),
          type: 'success',
          duration: 2500
        })
        // 释放内存
        clipboard.off('error')
        clipboard.off('success')
        clipboard.destroy()
      })
      clipboard.onClick(e)
    },
    // 上一步 下一步
    prev() {
      if (this.active == 0) {
        return
      }
      this.active--
    },
    next() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          if (this.active++ > 1) this.active = 0
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    // 标签组织机构改变
    labelbpChange(value) {
      this.labelbp_id = value
      this.labelBpsData.forEach((el, index) => {
        value == el.id ? (this.labelBpName = el.name) : ''
      })
      this.labeluserinit()
    },
    // 标签所有者改变
    labelUseChange(value) {
      this.labelData.forEach((el, index) => {
        el.show_name == '所有者'
          ? this.$set(this.labelData, el.default_value, value)
          : ''
      })
      this.labelUserData.forEach((el, index) => {
        el.id == value ? (this.labelUserName = el.name) : ''
      })
    },
    // 污点组织机构改变
    stainbpChange(value) {
      this.stainbp_id = value
      this.stainuserinit()
      this.labelBpsData.forEach((el, index) => {
        el.id == value ? (this.stainUserName = el.name) : ''
      })
    },
    // 污点所有者改变
    stainUseChange(value) {
      this.stainData.forEach((el, index) => {
        el.show_name == '所有者'
          ? this.$set(this.stainData, el.default_value, value)
          : ''
      })
      this.labelUserData.forEach((el, index) => {
        el.id == value ? (this.stainUserName = el.name) : ''
      })
    },
    async labelbpsinit() {
      const list = await getBps()
      this.labelBpsData = list.bps
    },

    // 展示labeloption
    async getLabelOption() {
      const list = await getLabelOption('node')
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          if (
            list[i].key != undefined &&
            list[i].key != null &&
            list[i].key.indexOf('bpId') != -1
          ) {
            if (this.userInfo.kind == 2) {
              list[i].default_value = this.userInfo.bp_id
              list[i].immutable = false
            }
          } else if (
            list[i].key != undefined &&
            list[i].key != null &&
            list[i].key.indexOf('owner') != -1
          ) {
            if (this.userInfo.kind == 2) {
              list[i].default_value = this.userInfo.id
              list[i].immutable = false
            }
          }
          list[i].required == true
            ? (list[i].required = this.$t('form.yes'))
            : (list[i].required = this.$t('form.no'))
        }
        this.labelData = list
      } else {
        this.labelData = []
      }
    },
    copy(e, text) {
      const clipboard = new Clipboard(e.target, { text: () => text })
      clipboard.on('success', () => {
        this.$notify({
          title: this.$t('message.copySuccess'),
          type: 'success',
          duration: 2500
        })
        // 释放内存
        clipboard.off('error')
        clipboard.off('success')
        clipboard.destroy()
      })
      clipboard.on('error', () => {
        // 不支持复制
        this.$notify({
          title: this.$t('message.copyFailed'),
          type: 'success',
          duration: 2500
        })
        // 释放内存
        clipboard.off('error')
        clipboard.off('success')
        clipboard.destroy()
      })
      clipboard.onClick(e)
    },
    async getTaintOptions() {
      const list = await getTaintOptions('node')
      if (list.length > 0) {
        for (var i = 0; i < list.length; i++) {
          if (list[i].key != undefined && list[i].key != null) {
            if (list[i].key.indexOf('bpId') != -1) {
              if (this.userInfo.kind == 2) {
                list[i].default_value = this.userInfo.bp_id
                list[i].immutable = false
              }
            } else if (list[i].key.indexOf('owner') != -1) {
              if (this.userInfo.kind == 2) {
                list[i].default_value = this.userInfo.id
                list[i].immutable = false
              }
            }
            list[i].required == true
              ? (list[i].required = this.$t('form.yes'))
              : (list[i].required = this.$t('form.no'))
          }
        }
        this.stainData = list
      } else {
        this.stainData = []
      }
    },
    async labeluserinit() {
      this.labelUserData = []
      const params = {
        bp_id: this.labelbp_id
      }
      var list = ''
      this.labelData.forEach((el, index) => {
        el.show_name == '所有者' ? (el.default_value = '') : ''
      })
      this.labelbp_id != '' && this.labelbp_id != undefined
        ? (list = await getUsers(params))
        : (list = await getUsers())
      list.users != undefined && list.users != null
        ? (this.labelUserData = list.users)
        : (this.labelUserData = [])
    },
    async stainuserinit() {
      this.stainUserData = []
      var stainlist = []
      const params = {
        bp_id: this.stainbp_id
      }
      this.stainbp_id != '' && this.stainbp_id != undefined
        ? (stainlist = await getUsers(params))
        : (stainlist = await getUsers())
      stainlist.users != undefined && stainlist.users != null
        ? (this.stainUserData = stainlist.users)
        : (this.stainUserData = [])
    },
    onOpen() {
      if (this.$refs.form) this.$refs.form.resetFields()
      getRegions({}).then((res) => {
        this.regions = res.regions.map((item) => {
          return {
            value: item.id,
            label: item.name
          }
        })
        if (this.regions != undefined && this.regions.length > 0) {
          this.form.region_id = this.regions[0].value
          this.region_name = this.regions[0].label
          this.websiteinit()
        }
      })
      this.getLabelOption('node')
      this.getTaintOptions('node')
      this.labelbpsinit()
      this.labeluserinit()
      this.stainuserinit()
    },
    changeRegion(region_id) {
      this.form.region_id = region_id
      this.regions.forEach((el) => {
        console.log(el)
        region_id == el.value ? (this.region_name = el.label) : ''
      })

      this.form.site_id = ''
      this.websiteinit()
    },
    async websiteinit() {
      const list = await getSites({
        region_id: this.form.region_id
      })
      this.sites = []
      if (list.sites.length > 0 && list.sites[0].sites.length > 0) {
        this.sites = list.sites[0].sites.map((item) => {
          return {
            value: item.id,
            label: item.name
          }
        })
      } else {
        this.sites = []
      }
      if (this.sites != undefined && this.sites.length > 0) {
        this.form.site_id = this.sites[0].value
        this.site_name = this.sites[0].label
      }
    },
    changeSites(site_id) {
      this.form.site_id = site_id
      this.sites.forEach((el) => {
        site_id == el.value ? (this.site_name = el.label) : ''
      })
    },
    cancel() {
      this.$router.push({
        path: '/infrastructure/node'
      })
      this.resetForm()
    },
    doSubmit() {
      let flag = true
      const labels = this.labelData.filter(
        (item) => item.key || item.default_value
      )
      const taints = this.stainData.filter(
        (item) => item.key || item.default_value
      )
      const labelsObj = {}
      labels.forEach(function(item) {
        labelsObj[item.key] = item.default_value
      })
      const taintsObj = {}
      taints.forEach(function(item) {
        taintsObj[item.key] = item.default_value
      })
      labels.forEach((element) => {
        if (!element.key) {
          this.$notify({
            title: this.$t('form.pleaseInputTaintKey'),
            type: 'info',
            duration: 2500
          })
          flag = false
          return false
        }
      })
      taints.forEach((element) => {
        if (!element.key) {
          this.$notify({
            title: this.$t('form.pleaseInputKey'),
            type: 'info',
            duration: 2500
          })
          flag = false
          return false
        }
      })
      if (flag) {
        this.form.labels = labelsObj
        this.form.taints = taintsObj
        this.form.labels.NO_MONITOR = !this.switchMonitor
        this.loading = true
        this.doAdd()
      }
    },
    doAdd() {
      console.log(this.form)
      addEdges(this.form)
        .then((res) => {
          this.resetForm()
          this.loading = false
          this.$router.push({
            name: 'Node',
            params: {
              cmdModal: true,
              cmdInfo: res.cmd
            }
          })
        })
        .catch(() => {
          this.loading = false
        })
    },
    resetForm() {
      this.$nextTick(() => {
        if (this.$refs['form'] !== undefined) {
          this.$refs['form'].resetFields()
        }
      })
      this.form = {
        region_id: '',
        site_id: '',
        node_name: '',
        taints: {},
        labels: {}
      }
      this.labelData = []
      this.stainData = []
      this.region_name = ''
      this.site_name = '';
      (this.labelBpsData = []),
      (this.labelUserData = []),
      (this.stainBpsData = []),
      (this.stainUserData = []);
      // 标签组织机构名称
      (this.labelBpName = ''),
      // 标签用户名称
      (this.labelUserName = ''),
      // 污点组织机构名称
      (this.stainBpName = ''),
      // 污点用户名称
      (this.stainUserName = '')
    }
  }
}
</script>

<style lang="scss" scoped>
.hFpJSC {
  font-size: 16px;
  font-weight: 700;
  color: rgb(0, 0, 0);
  margin-bottom: 20px;
}

::v-deep .el-collapse-item__header {
  font-size: 16px;
  font-weight: 700;
  color: rgb(0, 0, 0);
  margin-bottom: 20px;
}

::v-deep .el-steps {
  display: flex;
  justify-content: center;
  align-items: center;
}

::v-deep .el-step__head.is-process {
  color: #c0c4cc !important;
  border-color: #c0c4cc !important;
}

::v-deep .el-steps--simple {
  padding: 13px 14% 13px 30%;
  border-radius: 4px;
  background: #ffffff !important;
}
</style>
