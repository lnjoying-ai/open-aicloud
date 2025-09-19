<template>
  <div class="warpMain">
    <div class="head_rq">
      <el-form
        :inline="true"
        :model="queryData"
        size="small"
        class="demo-form-inline"
      >
        <el-form-item>
          <div slot="label">{{ $t("form.name") }}</div>
          <el-input
            v-model="queryData.registry_name"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.regionName") + ":" }}</div>
          <el-select
            v-model="queryData.region_id"
            :placeholder="$t('form.pleaseSelect')"
            filterable
            clearable
            @change="change_Region_id"
          >
            <el-option
              v-for="(item, index) in arealist"
              :key="index"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <div slot="label">{{ $t("form.status") + ":" }}</div>
          <el-select
            v-model="queryData.status"
            :placeholder="$t('form.pleaseSelect')"
            clearable
            @change="clickQueryStatus"
          >
            <el-option :value="8" :label="$t('form.all')" />
            <el-option :value="1" :label="$t('form.enable')" />
            <el-option :value="0" :label="$t('form.other')" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button
            icon="el-icon-search"
            class="add_search"
            type="primary"
            @click="handleCurrentChange(1)"
          >{{ $t("form.query") }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button class="add_search" @click="searchinit()">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="small" @click="downMirror">{{
            $t("form.imagePreDownload")
          }}</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" @click="clickAddWarehouse">{{
            $t("form.addImageRepository")
          }}</el-button>
        </el-form-item>
      </el-form>
    </div>
    <el-table
      ref="multipleTable"
      :data="tableData.registries"
      stripe
      tooltip-effect="dark"
      style="width: 100%"
      :header-cell-class-name="cellClass"
      :default-sort="{ prop: 'date', order: 'descending' }"
      @selection-change="handleSelectionChange"
      @select="dialogCheck"
    >
      <el-table-column type="expand" width="30" style="background: #e0e0e0">
        <template slot-scope="scope">
          <div class="ml-10">
            <div v-if="scope.row.regions.length > 0" style="margin-bottom: 5px">
              {{ $t("form.regionInfo") }}：<el-tag
                v-for="(item, index) in scope.row.regions"
                :key="index"
                style="margin-left: 10px"
                size="mini"
                type="info"
              >{{ item.region_name ? item.region_name : "-" }}</el-tag>
            </div>
            <div v-else style="margin-bottom: 5px">
              {{ $t("form.regionInfo") }}：<span>{{ "-" }}</span>
            </div>
            <div>
              {{ $t("form.descriptionInfo") }}：{{
                scope.row.registry_desc || "-"
              }}
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="30" />
      <el-table-column
        prop="registry_name"
        :label="$t('form.name')"
        show-overflow-tooltip
      />
      <el-table-column prop="type" :label="$t('form.type')">
        <template slot-scope="scope">
          {{
            !scope.row.type || scope.row.type == "0" ? "harbor" : "huawei-SWR"
          }}
        </template>
      </el-table-column>
      <el-table-column prop="url" :label="$t('form.url')" />
      <el-table-column :label="$t('form.status')" prop="status">
        <template slot-scope="scope">
          <span
            :style="{
              display: 'inline-block',
              width: '8px',
              height: '8px',
              'border-radius': '50%',
              'margin-right': '8px',
              background: status[scope.row.status].name
                ? status[scope.row.status].color
                : '#FF260f',
            }"
          />

          <span style="color: #666">{{ status[scope.row.status].name }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="regions" :label="$t('form.regionInfo')">
        <template slot-scope="scope">
          <span v-for="(item, index) in scope.row.regions" :key="index">
            <span v-if="index != 0">,</span>{{ item.region_name ? item.region_name : "-" }}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        :label="$t('form.createTime')"
        prop="create_time"
        sortable
      />
      <el-table-column
        :label="$t('form.updateTime')"
        prop="update_time"
        sortable
      />
      <el-table-column :label="$t('form.operation')" width="110">
        <template slot-scope="scope">
          <div class="czlb">
            <el-popover placement="bottom" width="110" trigger="click">
              <div
                v-if="scope.row.status != 6"
                class="icon_cz"
                @click="clickactiveBtn(scope.row)"
              >
                <i class="el-icon-video-play" />
                {{ $t("form.enable") }}
              </div>
              <div
                v-if="scope.row.status != 7"
                class="icon_cz"
                @click="clickdeactiveBtn(scope.row)"
              >
                <i class="el-icon-video-pause" />
                {{ $t("form.stopUse") }}
              </div>
              <div class="icon_cz" @click="checkWarehouse(scope.row)">
                <i class="el-icon-edit" />
                {{ $t("form.edit") }}
              </div>
              <div class="icon_cz" @click="clickDelBtn(scope.row)">
                <i class="el-icon-delete" />
                {{ $t("form.delete") }}
              </div>
              <div class="icon_cz" @click="changePassword(scope.row)">
                <i class="el-icon-edit-outline" />
                {{ $t("form.modifyAdminPassword") }}
              </div>

              <el-button
                slot="reference"
                icon="el-icon-more"
                class="czbtn right_czbtn"
              />
            </el-popover>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :current-page="queryData.page_num"
        :page-sizes="[5, 10, 20, 50, 100]"
        :page-size="queryData.page_size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="tableData.total_num"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <addUser ref="addUser" :sup_this="sup_this" />
    <addWarehouse ref="addWarehouse" />
    <changePassword ref="changePassword" />
    <download ref="download" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'

import {
  registries,
  delRegistries,
  getRegions,
  deactivegck,
  activegck,
  getUsers
} from '@/api/mainApi'
import initData from '@/mixins/initData'
import addWarehouse from './service/addWarehouse'
import addUser from './service/addUser'
import changePassword from './service/changePassword'
import download from './service/download'

export default {
  components: {
    addWarehouse,
    addUser,
    changePassword,
    download
  },
  mixins: [initData],
  data() {
    return {
      arealist: [],
      sup_this: this,
      tableData: [],
      status: {
        1: {
          name: this.$t('statusAndType.building'),
          color: '#0079D1'
        },
        2: {
          name: this.$t('statusAndType.failed'),
          color: '#FF260f'
        },
        3: {
          name: this.$t('statusAndType.success'),
          color: '#00AB5C'
        },
        4: {
          name: this.$t('statusAndType.unhealthy'),
          color: '#FFBC00'
        },
        5: {
          name: this.$t('statusAndType.healthy'),
          color: '#00AB5C'
        },
        6: {
          name: this.$t('statusAndType.enable'),
          color: '#00AB57'
        },
        7: {
          name: this.$t('statusAndType.disable'),
          color: '#BDBEC0'
        },
        '-1': {
          name: this.$t('statusAndType.delete'),
          color: '#FFBC00'
        }
      },
      queryData: {
        active_status: '',
        online_status: '',
        region: '',
        status: '',
        page_size: 10,
        page_num: 1
      },
      userData: [],
      selectioned: {}, // 选中行
      multipleSelection: []
    }
  },
  computed: {
    ...mapGetters(['kind', 'userInfo'])
  },
  watch: {},
  created() {
    this.areainit()
    this.init()
    this.userinit()
  },
  mounted() {},
  methods: {
    // 隐藏表头中的全选框
    cellClass(row) {
      if (row.columnIndex === 1) {
        return 'disabledCheck'
      }
    },

    clickQueryStatus(val) {
      this.queryData.status = val
      this.init()
    },
    change_Region_id(val) {
      this.queryData.region_id = val
      this.init()
    },
    change_Region_name(val) {
      this.queryData.Region_name = val
      this.init()
    },
    change_user_id(val) {
      this.queryData.user_id = val
      this.init()
    },
    checkboxSelect(row, rowIndex) {
      if (row.status == '3' || row.status == 6) {
        return true // 禁用
      } else {
        return false // 不禁用
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    dialogCheck(selection, row) {
      this.$refs.multipleTable.clearSelection()
      if (selection.length === 0) {
        return
      }
      if (row) {
        this.selectioned = row
        this.$refs.multipleTable.toggleRowSelection(row, true)
      }
    },
    async userinit() {
      const list = await getUsers()
      this.userData = list.users
    },
    searchinit() {
      this.queryData = {
        active_status: '',
        online_status: '',
        region: '',
        page_size: 10,
        page_num: 1
      }
      this.init()
    },
    async areainit() {
      const list = await getRegions()
      this.arealist = list.regions
    },
    handleSizeChange(val) {
      this.queryData.page_size = val
      this.init()
    },
    handleCurrentChange(val) {
      this.queryData.page_num = val
      this.init()
    },
    async init() {
      for (var key in this.queryData) {
        if (this.queryData[key] === undefined || this.queryData[key] === '') {
          delete this.queryData[key]
        }
      }
      const list = await registries(this.queryData)
      this.tableData = list
      this.list = list.registries
    },

    clickDelBtn(value) {
      this.$confirm(
        this.$t('message.confirmDeleteWarehouse'),
        this.$t('message.tip'),
        {
          confirmButtonText: this.$t('message.confirm'),
          cancelButtonText: this.$t('message.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          delRegistries(value.registry_id)
            .then((res) => {
              this.$notify({
                title: this.$t('message.deleteSuccess'),
                type: 'success',
                duration: 2500
              })
              this.init()
            })
            .catch((err) => {
              console.error(err.response.data.message)
            })
        })
        .catch(() => {})
    },
    clickdeactiveBtn(value) {
      this.$confirm(
        this.$t('message.confirmDisableWarehouse'),
        this.$t('message.tip'),
        {
          confirmButtonText: this.$t('message.confirm'),
          cancelButtonText: this.$t('message.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          deactivegck(value.registry_id)
            .then((res) => {
              this.$notify({
                title: this.$t('message.stopUseSuccess'),
                type: 'success',
                duration: 2500
              })
              this.init()
            })
            .catch((err) => {
              console.error(err.response.data.message)
            })
        })
        .catch(() => {})
    },
    clickactiveBtn(value) {
      this.$confirm(
        this.$t('message.confirmEnableWarehouse'),
        this.$t('message.tip'),
        {
          confirmButtonText: this.$t('message.confirm'),
          cancelButtonText: this.$t('message.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          activegck(value.registry_id)
            .then((res) => {
              this.$notify({
                title: this.$t('message.activateSuccess'),
                type: 'success',
                duration: 2500
              })
              this.init()
            })
            .catch((err) => {
              console.error(err.response.data.message)
            })
        })
        .catch(() => {})
    },
    // 添加镜像
    clickAddWarehouse() {
      this.$refs.addWarehouse.add()
    },
    // 查看仓库
    checkWarehouse(id) {
      this.$refs.addWarehouse.check(JSON.parse(JSON.stringify(id)))
    },
    // 添加用户
    clickAddUser() {
      this.$refs.addUser.add()
    },
    // 镜像预下载
    downMirror() {
      if (this.multipleSelection.length === 0) {
        this.$notify({
          title: this.$t('message.pleaseSelect'),
          type: 'warning',
          duration: 2500
        })
      } else {
        if (!this.selectioned || JSON.stringify(this.selectioned) === '{}') {
          this.$notify({
            title: this.$t('message.pleaseSelect'),
            type: 'warning',
            duration: 2500
          })
        } else {
          this.$refs.download.toData = []
          this.$refs.download.add(this.selectioned)
        }
      }
    },
    // 修改管理员密码
    changePassword(value) {
      this.$refs.changePassword.add(value)
    }
  }
}
</script>

<style lang="scss" scoped>
::v-deep .el-popover {
  min-width: 130px !important;
  padding: 0px !important;
}

.service:hover {
  transform: scale(1.3);
}

::v-deep .el-table .disabledCheck .cell .el-checkbox__inner {
  display: none !important;
}

::v-deep .el-table .disabledCheck .cell::before {
  content: "";
  text-align: center;
  line-height: 37px;
}

::v-deep .el-table__expanded-cell {
  background: #f5f5f5;
}
</style>
