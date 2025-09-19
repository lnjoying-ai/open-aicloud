<template>
  <div class="warpMain">
    <div>
      <div class="head_rq">
        <el-form
          ref="form"
          :inline="true"
          size="small"
          :model="queryData"
          :rules="rules"
          class="customWidth"
        >
          <el-form-item prop="registry_id" :label="$t('form.warehouse')+ ':'">
            <el-select
              v-model="queryData.registry_id"
              style="width: 120px"
              :placeholder="$t('form.pleaseSelect')"
              filterable
              @change="changeRegistry"
            >
              <el-option
                v-for="(item, index) in registries"
                :key="index"
                :value="item.registry_id"
                :label="item.registry_name"
                :disabled="item.disabled"
              />
            </el-select>
          </el-form-item>

          <el-form-item>
            <div slot="label">{{ $t("form.project") + ":" }}</div>
            <el-input
              v-model="queryData.project_name"
              style="width: 115px"
              :placeholder="$t('form.pleaseInput')"
            />
          </el-form-item>

          <el-form-item>
            <div slot="label">{{ $t("form.visibleRange") + ":" }}</div>
            <el-select
              v-model="queryData.scope"
              style="width: 90px"
              :placeholder="$t('form.pleaseSelect')"
              clearable
              @change="changeScope"
            >
              <el-option :label="$t('form.public')" :value="2" />
              <el-option :label="$t('form.bpVisible')" :value="1" />
              <el-option :label="$t('form.private')" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <div slot="label">{{ $t("form.userName") + ":" }}</div>
            <el-select
              v-model="queryData.user_id"
              style="width: 100px"
              filterable
              clearable
              :placeholder="$t('form.pleaseSelect')"
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

          <el-form-item style="float: right">
            <el-button
              v-if="
                userInfo.kind != 0 && userInfo.kind != 1 && propsregistry_id
              "
              type="primary"
              style="display: inline-block"
              size="small"
              @click="clickUploadimage"
            >{{ $t("form.uploadImage") }}</el-button>
            <el-button
              type="primary"
              style="display: inline-block"
              size="small"
              @click="clickAddBtn"
            >{{ $t("form.addProject") }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              icon="el-icon-search"
              type="primary"
              size="small"
              style="display: inline-block"
              @click="clickqueryBtn"
            >{{ $t("form.query") }}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              class="add_search"
              size="small"
              style="display: inline-block"
              @click="searchinit()"
            >{{ $t("form.reset") }}</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="multipleTable"
        :data="tableData.projects"
        stripe
        tooltip-effect="dark"
        style="width: 100%"
        :default-sort="{ prop: 'date', order: 'descending' }"
      >
        <el-table-column :label="$t('form.projectName')">
          <template slot-scope="scope">
            <router-link
              style="color: #409eff; cursor: pointer"
              :to="
                '/mirror/wareserve/warehouse/' +
                  scope.row.registry_id +
                  '/' +
                  scope.row.project_id
              "
            >
              {{ scope.row.project_name }}
            </router-link>
          </template>
        </el-table-column>
        <el-table-column :label="$t('form.visibleRange')">
          <template slot-scope="scope">
            <span v-if="scope.row.scope == '0'">{{ $t("form.private") }}</span>
            <span v-if="scope.row.scope == '1'">{{
              $t("form.bpVisible")
            }}</span>
            <span v-if="scope.row.scope == '2'">{{ $t("form.public") }}</span>
          </template>
        </el-table-column>
        <el-table-column
          prop="project_desc"
          :label="$t('form.projectDescription')"
        >
          <template slot-scope="scope">
            {{ scope.row.project_desc || "-" }}
          </template>
        </el-table-column>
        <el-table-column prop="registry_name" :label="$t('form.warehouseName')">
          <template slot-scope="scope">
            {{ scope.row.registry_name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          v-if="
            userInfo.kind == '0' ||
              userInfo.kind == '1' ||
              userInfo.kind == '2' ||
              userInfo.kind == '3'
          "
          prop="user_name"
          :label="$t('form.userName')"
        />
        <el-table-column
          v-if="
            userInfo.kind == '0' || userInfo.kind == '1' || userInfo.kind == '3'
          "
          prop="bp_name"
          :label="$t('form.bp')"
        >
          <template slot-scope="scope">
            {{ scope.row.bp_name || "-" }}
          </template>
        </el-table-column>
        <el-table-column
          prop="create_time"
          :label="$t('form.createTime')"
          sortable
        />
        <el-table-column
          prop="update_time"
          :label="$t('form.updateTime')"
          sortable
        />
        <el-table-column :label="$t('form.operation')" width="100">
          <template slot-scope="scope">
            <div
              v-if="
                userInfo.name == scope.row.user_name || userInfo.kind == '1'
              "
              class="czlb"
            >
              <el-popover placement="bottom" width="110" trigger="hover">
                <div class="icon_cz" @click="clickEditBtn(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
                </div>
                <div class="icon_cz" @click="clickDelBtn(scope.row)">
                  <i class="el-icon-delete" />
                  {{ $t("form.delete") }}
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
      <div v-if="userInfo.kind != 0 && userInfo.kind != 1" class="command">
        <!-- <el-row>
          <el-row v-if="msg1">
            登录指令：{{ msg1
            }}<i
              class="el-icon-document-copy"
              style="font-size: 17px"
              @click="copy($event, msg1)"
            />
          </el-row>
          <el-row v-if="msg2">
            拉取指令： {{ msg2
            }}<i
              class="el-icon-document-copy"
              style="font-size: 17px"
              @click="copy($event, msg2)"
            />
          </el-row>
          <el-row v-if="msg3">
            推送指令： {{ msg3
            }}<i
              class="el-icon-document-copy"
              style="font-size: 17px"
              @click="copy($event, msg3)"
            />
          </el-row>
        </el-row> -->
      </div>
    </div>
    <addForm ref="addForm" />
    <changeRegPwd ref="changeRegPwd" @usersExist="verification()" />
    <Uploadimage ref="Uploadimageref" :registry_id="propsregistry_id" />
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import {
  delProjects,
  registries,
  projects,
  getUsers,
  usersExist,
  getcommand
} from '@/api/mainApi'
import initData from '@/mixins/initData'
import addForm from './project/addForm'
import Uploadimage from './project/Uploadimage'
import Clipboard from 'clipboard'
import changeRegPwd from '../system/userMsg/changeRegPwd.vue'

export default {
  components: {
    addForm,
    changeRegPwd,
    Uploadimage
  },
  mixins: [initData],
  data() {
    return {
      propsregistry_id: '',
      tabKey: 1,

      userData: [],
      sup_this: this,
      tableData: {
        projects: [],
        total_num: 0
      },
      keyword: '',
      queryData: {
        registry_id: '',
        project_name: '',
        regions_id: '',
        scope: '',
        user_id: '',
        bp_id: '',
        page_size: 10,
        page_num: 1
      },
      rules: {
        registry_id: {
          required: true,
          message: this.$t('message.pleaseSelectWarehouse'),
          trigger: 'change'
        }
      },
      regions_id: '',
      activeInfo: {},
      registries: [], // 仓库列表
      multipleSelection: [],
      arealist: [],
      registry_id: '',
      msg1: '',
      msg2: '',
      msg3: ''
    }
  },
  computed: {
    ...mapGetters(['userInfo'])
  },
  mounted() {
    this.init()
    this.userinit()
  },
  created() {},

  methods: {
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
          title: this.$t('message.copyError'),
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
    async userinit() {
      const list = await getUsers()
      this.userData = list.users
    },
    verification() {
      usersExist()
        .then((res) => {
          if (this.registries.length > 0 && this.registries[0].registry_id) {
            this.propsregistry_id = this.registries[0].registry_id
          }
        })
        .catch((error) => {
          this.$confirm(
            this.$t('message.pleaseSetWarehousePassword'),
            this.$t('message.tip'),
            {
              confirmButtonText: this.$t('message.confirm'),
              cancelButtonText: this.$t('message.cancel'),
              type: 'warning',
              showCancelButton: false,
              showClose: false,
              closeOnClickModal: false
            }
          )
            .then(() => {
              this.$refs.changeRegPwd.add({ user_name: this.userInfo.name })
            })
            .catch(() => {})
        })
    },

    changeRegistry(val) {
      this.queryData.registry_id = val
      this.getList()
      if (this.userInfo.kind != 0 && this.userInfo.kind != 1) {
        this.propsregistry_id = val
      }
    },
    change_user_id(val) {
      this.queryData.user_id = val
      this.getList()
    },

    async init() {
      if (this.userInfo.kind != 0 && this.userInfo.kind != 1) {
        const params = {}
        params.status = 1
        var list = await registries(params)
      } else {
        var list = await registries()
      }

      if (list.registries && list.registries.length > 0) {
        this.registries = list.registries
        this.queryData.registry_id = this.registries[0].registry_id
        this.getList()
      } else {
        this.tableData = []
      }
      if (this.userInfo.kind != 0 && this.userInfo.kind != 1) {
        this.verification()
      }
    },

    // 查询
    clickqueryBtn() {
      if (
        this.queryData.registry_id != '' &&
        this.queryData.registry_id != null
      ) {
        this.getList()
      } else {
        this.$notify({
          title: this.$t('message.pleaseSelectWarehouse'),
          type: 'warning',
          duration: 2500
        })
      }
    },

    handleSizeChange(val) {
      this.queryData.page_size = val
      this.getList()
    },

    handleCurrentChange(val) {
      this.queryData.page_num = val
      this.getList()
    },
    changeScope(val) {
      this.queryData.scope = val
      this.getList()
    },
    async getList() {
      if (this.queryData.registry_id) {
        for (var key in this.queryData) {
          if (this.queryData[key] === undefined || this.queryData[key] === '') {
            delete this.queryData[key]
          }
        }
        const list = await projects(this.queryData.registry_id, this.queryData)
        this.tableData = list
      }
    },
    // 上传镜像
    clickUploadimage() {
      this.$refs.Uploadimageref.onOpen()
    },
    clickAddBtn() {
      this.$refs.addForm.add()
    },

    clickDelBtn(value) {
      this.$confirm(
        this.$t('message.confirmDeleteProject'),
        this.$t('message.tip'),
        {
          confirmButtonText: this.$t('message.confirm'),
          cancelButtonText: this.$t('message.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          delProjects(value.registry_id, value.project_id)
            .then(() => {
              this.$notify({
                title: this.$t('message.deleteSuccess'),
                type: 'success',
                duration: 2500
              })
              this.getList()
            })
            .catch((err) => {})
        })
        .catch(() => {})
    },
    searchinit() {
      this.queryData = {
        registry_id: this.registries.length
          ? this.registries[0].registry_id
          : '',
        project_name: '',
        scope: '',
        user_id: '',
        bp_id: '',
        page_size: 10,
        page_num: 1
      }
      this.regions_id = ''
      this.tableData = []
      this.getList()
    },

    clickEditBtn(info) {
      this.$refs.addForm.edit(JSON.parse(JSON.stringify(info)))
    }
  }
}
</script>

<style lang="scss" scoped>
.customWidth {
  .el-form-item {
    .el-input {
      ::v-deep .el-input__inner {
        padding: 0 10px;
      }
    }

    .el-select {
      ::v-deep .el-input {
        .el-input__inner {
          padding: 0 20px 0 10px;
        }
      }
    }
  }
}

::v-deep .el-form-item__content {
  line-height: 40px;
  position: relative;
  font-size: 14px;
}

::v-deep .search .el-card__body {
  padding: 0 !important;
}

.command {
  margin-top: 20px;
  padding: 30px 10px;

  .el-row {
    line-height: 36px;
    color: #333;
    font-size: 16px;
  }

  i {
    margin-left: 10px;
    font-size: 36px;
  }
}
</style>
