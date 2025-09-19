<template>
  <div class="warpMain">
    <div>
      <el-form
        :model="form"
        :inline="true"
        :size="$store.state.nextStack.viewSize.main"
        class="el-form demo-form-inline el-form--inline"
      >
        <el-form-item :label="$t('form.name')+ ':'">
          <el-input
            v-model="form.name"
            class="w-48"
            :placeholder="$t('form.pleaseInputName')"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            class=""
            @click="onSubmit"
          >{{ $t("form.query") }}</el-button>
          <el-button class="resetBtn" size="small" @click="onReset">{{
            $t("form.reset")
          }}</el-button>
        </el-form-item>
        <el-button
          type="primary"
          class="float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="uploadKey"
        >
          {{ $t("form.importExistingKey") }}
        </el-button>
        <el-button
          type="primary"
          class="mr-2 float-right"
          :size="$store.state.nextStack.viewSize.main"
          @click="addKey"
        >
          {{ $t("form.createNewKeyPair") }}
        </el-button>
      </el-form>
    </div>
    <div>
      <el-table
        :data="tableData"
        max-height="calc(100% - 3rem)"
        class="!overflow-y-auto"
        stripe
        :scrollbar-always-on="false"
      >
        <el-table-column prop="date" :label="$t('form.nameOrId')">
          <template #default="scope">
            <span
              class="text-blue-400 cursor-pointer"
              @click="toDetail(scope.row)"
            >{{ scope.row.name }}</span>
            <span
              class="block leading-none new-small-size"
            >ID:{{ scope.row.pubkeyId }}</span>
          </template>
        </el-table-column>
        <el-table-column
          v-if="kind == 0 || kind == 1"
          prop="eeUserName"
          :label="$t('form.user')"
        >
          <template #default="scope">
            <span>{{ scope.row.eeUserName ? scope.row.eeUserName : "-" }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="$t('form.description')">
          <template #default="scope">
            {{ scope.row.description ? scope.row.description : "-" }}
          </template>
        </el-table-column>
        <el-table-column prop="createTime" :label="$t('form.createTime')" />
        <el-table-column
          prop="address"
          :label="$t('form.operation')"
          width="120"
        >
          <template #default="scope">
            <div class="czlb">
              <el-popover placement="bottom" width="110" trigger="click">
                <div class="icon_cz" @click="toEdit(scope.row)">
                  <i class="el-icon-edit" />
                  {{ $t("form.edit") }}
                </div>
                <div class="icon_cz" @click="toDelete(scope.row)">
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
    </div>
    <div class="flex justify-end mt-4 px-4">
      <el-pagination
        :page_num="form.page_num"
        :page-size="form.page_size"
        :page-sizes="$store.state.nextStack.page_sizes"
        :current-page="form.page_num"
        layout="total, sizes, prev, pager, next, jumper"
        :total="form.total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
    <el-dialog
      :title="$t('form.createKey')"
      :visible.sync="addDialogVisible"
      width="600px"
      :before-close="addHandleClose"
    >
      <publickeyadd ref="publickeyaddRef" @close="addHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.importKey')"
      :visible.sync="uploadDialogVisible"
      width="600px"
      :before-close="uploadHandleClose"
    >
      <publickeyupload ref="publickeyuploadRef" @close="uploadHandleClose" />
    </el-dialog>
    <el-dialog
      :title="$t('form.editKey')"
      :visible.sync="editDialogVisible"
      width="600px"
      :before-close="editHandleClose"
    >
      <publickeyedit
        :id="editId"
        ref="publickeyeditRef"
        @close="editHandleClose"
      />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import mainApi from '@/api/nextStack/mainApi'

import publickeyadd from './add.vue'
import publickeyupload from './upload.vue'
import publickeyedit from './edit.vue'

export default {
  components: {
    publickeyadd,
    publickeyupload,
    publickeyedit
  },
  data() {
    return {
      addDialogVisible: false,
      uploadDialogVisible: false,
      editDialogVisible: false,
      editId: '',
      timer: '',
      loading: false,
      form: {
        name: '',
        page_num: 1,
        page_size: this.$store.state.nextStack.page_size,
        total: 0
      },
      tableData: []
    }
  },
  created() {
    this.getpubkeysList()
  },
  mounted() {
    this.timer = setInterval(async() => {
      if (this.$store.getters.hxcloudData.cloud_id) {
        this.getpubkeysListTime() // 请求公钥循环列表
      }
    }, this.$store.state.nextStack.listRefreshTime)
  },
  computed: {
    ...mapGetters(['kind'])
  },
  // 离开页面时清除定时器
  beforeDestroy() {
    clearInterval(this.timer)
  },
  methods: {
    addKey() {
      // 创建新的密钥对
      this.addDialogVisible = true
      this.$nextTick(() => {
        this.$refs.publickeyaddRef.clearpubkeysInfo()
      })
      // this.$refs.publickeyaddRef.clearpubkeysInfo()
    },
    addHandleClose() {
      // 创建新的密钥对
      this.$refs.publickeyaddRef.resetForm()
      this.addDialogVisible = false
      this.getpubkeysList()
    },
    uploadKey() {
      // 上传公钥
      this.uploadDialogVisible = true
    },
    uploadHandleClose() {
      // 上传公钥
      this.$refs.publickeyuploadRef.resetForm()
      this.uploadDialogVisible = false
      this.getpubkeysList()
    },
    toEdit(item) {
      // 编辑
      this.editId = item.pubkeyId
      this.editDialogVisible = true
    },
    editHandleClose() {
      // 编辑
      this.$refs.publickeyeditRef.resetForm()
      this.editId = ''
      this.editDialogVisible = false
      this.getpubkeysList()
    },
    handleSizeChange(val) {
      // 改变每页显示数量
      localStorage.setItem('page_size', val)
      this.$store.state.nextStack.page_size = val
      this.form.page_size = val
      this.getpubkeysList()
    },
    handleCurrentChange(val) {
      // 改变页码
      this.form.page_num = val
      this.getpubkeysList()
    },
    toDetail(item) {
      // 详情
      this.$router.push({
        path: `/nextStack/publicKey/detail/${item.pubkeyId}`
      })
    },
    toDelete(item) {
      // 删除
      this.$confirm(
        this.$t('message.confirmDeletePubKey'),
        this.$t('message.prompt'),
        {
          confirmButtonText: this.$t('message.delete'),
          cancelButtonText: this.$t('message.cancel'),
          type: 'warning'
        }
      )
        .then(() => {
          mainApi
            .pubkeysDel(item.pubkeyId)
            .then((res) => {
              this.loading = false
              this.$notify({
                title: this.$t('message.deleteSuccess'),
                type: 'success',
                duration: 2500
              })
              this.getpubkeysList()
            })
            .catch((error) => {
              this.loading = false
            })
        })
        .catch(() => {})
    },
    onSubmit() {
      // 提交
      this.form.page_num = 1
      this.getpubkeysList()
    },
    onReset() {
      // 重置
      this.form.name = ''
      this.form.page_num = 1
      this.getpubkeysList()
    },
    getpubkeysList() {
      // 公钥列表
      this.loading = true
      const params = {
        name: this.form.name,
        page_num: this.form.page_num,
        page_size: this.form.page_size
      }
      for (const key in params) {
        if (
          params[key] === null ||
          params[key] === undefined ||
          params[key] === ''
        ) {
          delete params[key]
        }
      }

      mainApi
        .pubkeysList(params)
        .then((res) => {
          this.loading = false
          this.tableData = res.pubkeys
          this.form.total = res.totalNum
        })
        .catch((error) => {
          this.loading = false
        })
    },
    getpubkeysListTime() {
      // 公钥循环列表
      const params = {
        name: this.form.name,
        page_num: this.form.page_num,
        page_size: this.form.page_size
      }
      for (const key in params) {
        if (
          params[key] === null ||
          params[key] === undefined ||
          params[key] === ''
        ) {
          delete params[key]
        }
      }

      mainApi.pubkeysList(params).then((res) => {
        this.tableData = res.pubkeys
        this.form.total = res.totalNum
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.bmsPage {
}
</style>
